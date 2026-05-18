package com.food.ordering.system.outbox.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling // This will enable scheduling in the application.
public class SchedulerConfig {
}
