package com.amsabots.jenzi.fundi_service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author andrew mititi on Date 2/2/22
 * @Project lameck-fundi-service
 */
@Configuration
public class RabbitMqConfigs {
    @Bean
    Queue messageQueue() {
        return new Queue(ConfigConstants.OUT_GOING_MESSAGE_QUEUE);
    }

    @Bean
    Queue dlrQueue() {
        return new Queue(ConfigConstants.DLR_MESSAGE_QUEUE);
    }
    @Bean
    Queue connectUsersQueue() {
        return new Queue(ConfigConstants.CONNECT_USERS_QUEUE);
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(ConfigConstants.MESSAGE_EXCHANGE);
    }

    @Bean
    Binding messageQueueBinder() {
        return BindingBuilder.bind(messageQueue()).to(directExchange()).with(ConfigConstants.OUT_GOING_MESSAGE_KEY);
    }
    @Bean
    Binding connectUsersBinder() {
        return BindingBuilder.bind(connectUsersQueue()).to(directExchange()).with(ConfigConstants.CONNECT_USERS_KEY);
    }

    @Bean
    Binding dlrQueueBinder() {
        return BindingBuilder.bind(dlrQueue()).to(directExchange()).with(ConfigConstants.DLR_MESSAGE_KEY);
    }

}