package com.aks.mongodbredisintegration.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.aks.mongodbredisintegration.handler.BookHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
class RoutingConfiguration {

	/**
	 * monoRouterFunction.
	 * @param bookHandler a {@link org.mongodb.redis.integration.handler.BookHandler}
	 * object.
	 * @return a {@link org.springframework.web.reactive.function.server.RouterFunction}
	 * object.
	 */
	@Bean
	public RouterFunction<ServerResponse> monoRouterFunction(BookHandler bookHandler) {
		return route(GET("/api/book").and(accept(MediaType.APPLICATION_JSON)),
				bookHandler::getAll).andRoute(
						GET("/api/book/{id}").and(accept(MediaType.APPLICATION_JSON)),
						bookHandler::getBook)
						.andRoute(
								POST("/api/book/post")
										.and(accept(MediaType.APPLICATION_JSON)),
								bookHandler::postBook)
						.andRoute(
								PUT("/api/book/put/{id}")
										.and(accept(MediaType.APPLICATION_JSON)),
								bookHandler::putBook)
						.andRoute(
								DELETE("/api/book/delete/{id}")
										.and(accept(MediaType.APPLICATION_JSON)),
								bookHandler::deleteBook);
	}

}