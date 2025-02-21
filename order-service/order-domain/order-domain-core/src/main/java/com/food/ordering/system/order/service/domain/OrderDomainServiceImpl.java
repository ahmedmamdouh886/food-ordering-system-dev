package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    private static final String UTC = "UTC";

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initializeOrder();

        log.info("Order with id: {} is initiated.", order.getId().getValue());

        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (! restaurant.isActive()) {
            throw new OrderDomainException("Restaurant with id " + restaurant.getId().getValue() +
                    " is currently not active!");
        }
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {
        /*
            TODO: Improve the TC.
            Currently, it runs in N-square time complexity in big notation because of the two inner loops.
            If you use an additional data structure here for products such as Hash Map you can reduce the time complexity to a linear time with a small space cost.
            I will leave it to you.
         */
        order.getItems().forEach(orderItem -> restaurant.getProducts().forEach(restaurantProduct -> {
            Product currentProduct = orderItem.getProduct();
            if (currentProduct.equals(restaurantProduct)) {
                currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
            }
        }));
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();

        log.info("Order with id: {} is paid.", order.getId().getValue());

        return new OrderPaidEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void approveOrder(Order order) {
        /*
            As you see, these methods doesn't return any events because this is one of the last step in order processing and after approval, I don't need to fire an event.
            Instead, the clients will fetch the data the get endpoint with tracking id.
            If you will the client that has capable of capturing events.
            You can hire final events even here that will be consumed by the client to continue with the delivery process.
            However, I will not implement the client as an event consumer.
            As mentioned here, I just use a HTTP client with Postman for the sake of this course.
         */
        order.approve();

        log.info("Order with id: {} is approved.", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);

        log.info("Order payment is cancelling for order id: {}", order.getId().getValue());

        return new OrderCancelledEvent(order, ZonedDateTime.now(ZoneId.of(UTC)));
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);

        log.info("Order with id: {} is cancelled", order.getId().getValue());
    }
}
