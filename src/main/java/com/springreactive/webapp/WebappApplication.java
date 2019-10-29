package com.springreactive.webapp;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class WebappApplication {

  public static void main(String[] args) {
    SpringApplication.run(WebappApplication.class, args);
  }

  @Bean
  public RouterFunction<ServerResponse> route(@NotNull GreetingsHandler greetingHandler) {
    return RouterFunctions
        .route(RequestPredicates.GET("/hello").and(RequestPredicates.accept(MediaType.TEXT_PLAIN)),
            greetingHandler::hello);
  }

  @Component
  class GreetingsHandler {

    public Mono<ServerResponse> hello(@NotNull ServerRequest request) {
      String name = request.queryParam("name").get();
      String reversed = reverse(name);
      return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN)
          .body(BodyInserters.fromValue("Hello, " + reversed + "!"));
    }

    String reverse(@NotNull String text) {
      return text.chars()
          .mapToObj(c -> (char) c)
          .reduce("", (s, c) -> c + s, (s1, s2) -> s2 + s1);
    }
  }
}
