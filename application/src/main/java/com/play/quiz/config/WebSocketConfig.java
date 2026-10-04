package com.play.quiz.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.play.quiz.controller.RestEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.play.quiz.controller.RestEndpoint.WS_BROKER_PARTY;
import static com.play.quiz.controller.RestEndpoint.WS_BROKER_LIVE;
import static com.play.quiz.controller.RestEndpoint.WS_BROKER_SOLO;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Value("#{'${application.security.allowed-origins}'.split(',')}")
    private final List<String> allowedOrigins;

    private final ObjectMapper objectMapper;

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry messageBrokerRegistry) {
        messageBrokerRegistry.setApplicationDestinationPrefixes(RestEndpoint.CONTEXT_PATH + "/app");
        messageBrokerRegistry.enableSimpleBroker(WS_BROKER_PARTY, WS_BROKER_SOLO, WS_BROKER_LIVE);
    }

    // Clients may only send to the application (@MessageMapping, under /api/app); the broker's
    // topics are the server's to send on. Otherwise anyone could SEND straight to /party/... and
    // reach every player, a fake announcement included.
    @Override
    public void configureClientInboundChannel(final ChannelRegistration registration) {
        String applicationPrefix = RestEndpoint.CONTEXT_PATH + "/app/";
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(final Message<?> message, final MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (accessor == null || accessor.getCommand() != StompCommand.SEND) return message;
                String destination = accessor.getDestination();
                if (destination != null && destination.startsWith(applicationPrefix)) return message;
                log.warn("Dropped a client SEND to {}", destination);
                return null;
            }
        });
    }

    @Override
    public void registerStompEndpoints(final StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry
                .addEndpoint(RestEndpoint.CONTEXT_PATH + "/pq")
                .setHandshakeHandler(new DefaultHandshakeHandler())
                .setAllowedOrigins(allowedOrigins.toArray(new String[0]))
                .withSockJS();
    }

    @Override
    public boolean configureMessageConverters(final List<MessageConverter> messageConverters) {
        final DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);

        final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper.registerModule(javaTimeModule()));
        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);

        return false;
    }

    private JavaTimeModule javaTimeModule() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));

        return javaTimeModule;
    }
}
