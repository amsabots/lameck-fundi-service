package com.amsabots.jenzi.fundi_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author andrew mititi on Date 2/2/22
 * @Project lameck-fundi-service
 */
@Configuration

public class RabbitMqConfigs {
    @Bean
    DirectExchange directExchange() {
        return ExchangeBuilder.directExchange(ConfigConstants.MESSAGE_EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue jenziGeneralQueue() {
        return QueueBuilder.durable(ConfigConstants.JENZI_GENERAL_QUEUE).build();
    }

    @Bean
    Binding generalMessageQueueBinder() {
        return BindingBuilder.bind(jenziGeneralQueue()).to(directExchange()).with(ConfigConstants.JENZI_GENERAL_QUEUE_KEY);
    }
    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


}