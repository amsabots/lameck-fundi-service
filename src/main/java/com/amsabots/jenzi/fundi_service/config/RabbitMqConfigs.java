package com.amsabots.jenzi.fundi_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author andrew mititi on Date 2/2/22
 * @Project lameck-fundi-service
 */
@Configuration
public class RabbitMqConfigs {
    @Bean
    Queue messageQueue() {
        return QueueBuilder.durable(ConfigConstants.OUT_GOING_MESSAGE_QUEUE).build();
    }

    @Bean
    Queue dlrQueue() {
        return QueueBuilder.durable(ConfigConstants.DLR_MESSAGE_QUEUE).build();
    }

    @Bean
    Queue removeDLRQueue() {
        return QueueBuilder.durable(ConfigConstants.REMOVE_DLR_QUEUE).build();
    }

    @Bean
    Queue connectUsersQueue() {
        return QueueBuilder.durable(ConfigConstants.CONNECT_USERS_QUEUE).build();
    }
    @Bean
    public Queue fundiQueue() {
        return QueueBuilder.durable(ConfigConstants.FUNDI_NEW_PROJECT_QUEUE).build();
    }


    @Bean
    DirectExchange directExchange() {
        return ExchangeBuilder.directExchange(ConfigConstants.MESSAGE_EXCHANGE).durable(true).build();
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



    @Bean
    Binding removeDLRQueueBinder() {
        return BindingBuilder.bind(removeDLRQueue()).to(directExchange()).with(ConfigConstants.REMOVE_DLR_KEY);
    }
    @Bean
    public Binding fundiProjectBinder() {
        return BindingBuilder.bind(fundiQueue()).to(directExchange()).with(ConfigConstants.FUNDI_NEW_PROJECT_QUEUE_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper());
    }
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }


}