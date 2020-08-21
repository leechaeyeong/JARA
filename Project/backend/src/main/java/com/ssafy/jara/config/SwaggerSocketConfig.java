package com.ssafy.jara.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableWebSocketMessageBroker
public class SwaggerSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Bean
	public Docket postsApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("JARA")
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.ssafy.jara.controller"))
				.paths(PathSelectors.ant("/**"))
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("JARA API")
				.description("JARA API Reference for Developers / jaraauth@gmail.com")
				.license("JARA License")
				.licenseUrl("https://i3a308.p.ssafy.io/").version("1.0").build();
	}
	
	// Vue.js로 구현한 채팅
	// 클라이언트가 메시지를 구독할 endpoint 정의
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/send");
	}
	
	// Connection을 맺을 때, CORS 허용
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/jara").setAllowedOrigins("*").withSockJS();
	}
}
