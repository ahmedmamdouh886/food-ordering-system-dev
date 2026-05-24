package com.food.ordering.system.payment.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.PaymentOrderStatus;
import com.food.ordering.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.food.ordering.system.payment.service.domain.exception.PaymentNotFoundException;
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import com.food.ordering.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;


@Slf4j
@Component
public class PaymentRequestKafkaListener implements KafkaConsumer<PaymentRequestAvroModel> {


    private final PaymentRequestMessageListener paymentRequestMessageListener;
    private final PaymentMessagingDataMapper paymentMessagingDataMapper;

    public PaymentRequestKafkaListener(
            PaymentRequestMessageListener paymentRequestMessageListener,
            PaymentMessagingDataMapper paymentMessagingDataMapper
    ) {
        this.paymentRequestMessageListener = paymentRequestMessageListener;
        this.paymentMessagingDataMapper = paymentMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}",
            topics = "${payment-service.payment-request-topic-name}")
    public void receive(
            @Payload List<PaymentRequestAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offsets
    ) {
        log.info("{} number of payment requests received with keys:{}, partitions:{} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(paymentRequestAvroModel -> {
            try {
                if (PaymentOrderStatus.PENDING == paymentRequestAvroModel.getPaymentOrderStatus()) {
                    log.info("Processing payment for order id: {}", paymentRequestAvroModel.getOrderId());
                    paymentRequestMessageListener.completePayment(
                            paymentMessagingDataMapper
                            .paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel)
                    );
                } else if (PaymentOrderStatus.CANCELLED == paymentRequestAvroModel.getPaymentOrderStatus()) {
                    log.info("Cancelling payment for order id: {}", paymentRequestAvroModel.getOrderId());
                    paymentRequestMessageListener.cancelPayment(
                            paymentMessagingDataMapper
                            .paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel)
                    );
                }
            } catch (DataAccessException e) {
                // Episode "Refactoring Payment Messaging module for outbox pattern" in the outbox section.
                SQLException sqlException = (SQLException) e.getRootCause();
                /**
                 * As I mentioned, I cannot use optimistic logging as there is no records yet to update.
                 * So here I will catch data access exception.
                 * To be sure, I only prevent retrying the unique constraint error.
                 * This means if DataAccessException exception is thrown and the exception is UNIQUE_VIOLATION, it means the request is already processed by another thread.
                 * It will be thrown because if the following unique constraint:
                 *  CREATE UNIQUE INDEX "payment_order_outbox_saga_id_payment_status_outbox_status"
                 *      ON "payment".order_outbox
                 *      (type, saga_id, payment_status, outbox_status);
                 *
                 * written in the init-schema.sql
                 *
                 * This handles two threads working simultaneously.
                 * So only one will write in the outbox table.
                 */
                if (sqlException != null && sqlException.getSQLState() != null &&
                        PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {
                    // NO-OP for unique constraint exception. This won't retry to process the message as we don't throw any exception.
                    log.error("Caught unique constraint exception with sql state: {} " +
                                    "in PaymentRequestKafkaListener for order id: {}",
                            sqlException.getSQLState(), paymentRequestAvroModel.getOrderId());
                } else {
                    throw new PaymentApplicationServiceException("Throwing DataAccessException in" +
                            " PaymentRequestKafkaListener: " + e.getMessage(), e);
                }
            } catch (PaymentNotFoundException e) {
                // NO-OP for PaymentNotFoundException. This won't retry to process the message as we don't throw any exception.
                log.error("No payment found for order id: {}", paymentRequestAvroModel.getOrderId());
            }
        });
    }
}
