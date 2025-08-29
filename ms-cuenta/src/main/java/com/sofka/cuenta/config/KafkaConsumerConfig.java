package com.sofka.cuenta.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

	/*
    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(Object.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;

	// Define el bean para ReplyingKafkaTemplate
	@Bean
	public ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate(
			ProducerFactory<String, String> pf,
			ConcurrentMessageListenerContainer<String, String> repliesContainer) {

		return new ReplyingKafkaTemplate<>(pf, repliesContainer);
	}

	// Define un contenedor para escuchar las respuestas
	@Bean
	public ConcurrentMessageListenerContainer<String, String> repliesContainer(
			ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {

		// Crea un contenedor específico para el topic de respuestas
		ConcurrentMessageListenerContainer<String, String> container =
				containerFactory.createContainer("validar-cliente-reply-topic");

		container.getContainerProperties().setGroupId("ms-cuenta-group");

		return container;
	}
	*/

	@Bean
	public ConcurrentMessageListenerContainer<String, String> repliesContainer(
			ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {

		// Crea un contenedor específico para el topic de respuestas
		ConcurrentMessageListenerContainer<String, String> container =
				containerFactory.createContainer("validar-cliente-reply-topic");

		container.getContainerProperties().setGroupId("ms-cuenta-group");

		// **IMPORTANTE**: Establece la propiedad para enviar el ConsumerRecord en el header
		container.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
		container.getContainerProperties().setMissingTopicsFatal(false);

		return container;
	}

	// El bean de ReplyingKafkaTemplate
	@Bean
	public ReplyingKafkaTemplate<String, String, String> replyingKafkaTemplate(
			ProducerFactory<String, String> pf,
			ConcurrentMessageListenerContainer<String, String> repliesContainer) {

		return new ReplyingKafkaTemplate<>(pf, repliesContainer);
	}
}
