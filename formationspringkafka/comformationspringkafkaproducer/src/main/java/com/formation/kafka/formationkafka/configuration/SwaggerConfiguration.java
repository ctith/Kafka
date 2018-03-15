package com.formation.kafka.formationkafka.configuration;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	private static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal Error";
	private static final String INTERNAL_SERVER_RESPONSE_MODEL_TYPE = "string";
	private final SwaggerProperties swagger;

	public SwaggerConfiguration(SwaggerProperties swagger) {
		this.swagger = swagger;
	}

	@Bean
	public ResponseMessage internalResponseMessage() {
		return new ResponseMessageBuilder().code(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
				.message(INTERNAL_SERVER_ERROR_MESSAGE).responseModel(new ModelRef(INTERNAL_SERVER_RESPONSE_MODEL_TYPE)).build();
	}

	@Bean
	public Docket newsApi(ResponseMessage responseMessage) {
		List<ResponseMessage> responseMessages = Collections.singletonList(responseMessage);
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.enable(swagger.isEnabled())
				.select()
				.apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
				.apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.cloud")))
				.apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.data.rest.webmvc")))
				.build()
				.globalResponseMessage(RequestMethod.GET, responseMessages)
				.globalResponseMessage(RequestMethod.DELETE, responseMessages)
				.globalResponseMessage(RequestMethod.PUT, responseMessages)
				.globalResponseMessage(RequestMethod.POST, responseMessages);


	}


	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title(swagger.getTitle())
				.description(swagger.getDescription())
				.version(swagger.getVersion())
				.build();
	}

}
