package com.sofka.cuenta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@Configuration
public class KafkaConsumerConfig {

	@Bean
	public ConcurrentMessageListenerContainer<String, String> repliesContainer(
			ConcurrentKafkaListenerContainerFactory<String, String> containerFactory) {

		ConcurrentMessageListenerContainer<String, String> container =
				containerFactory
						.createContainer("validar-cliente-reply-topic", "obtener-cliente-reply-topic");

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
