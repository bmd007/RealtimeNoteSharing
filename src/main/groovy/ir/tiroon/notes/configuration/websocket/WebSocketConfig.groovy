package ir.tiroon.notes.configuration.websocket

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

    @Override
    void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic","/queue")
        config.setApplicationDestinationPrefixes("/app")
    }


    //                .setAllowedOrigins("*")
    //is not secure at all. its for test purposes on our remote server
    @Override
    void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/noteAppStopmEndpoint")
                .addInterceptors(new HttpHandshakeInterceptor())
                .setAllowedOrigins("*")
                .withSockJS()
    }


}

