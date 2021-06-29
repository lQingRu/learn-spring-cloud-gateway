package com.learning.springgateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//@Component
@Configuration
public class OpenApi {
    @Autowired
    private RouteLocator routeLocator;

    public OpenApi(@Autowired RouteLocator routeLocator, @Autowired SwaggerUiConfigProperties swaggerUiConfig) {

        List<Route> routes = routeLocator.getRoutes().collectList().block();

//        // deduplication
//        Collection<Route> distinctRoutes = routes.stream()
//                .collect(Collectors.toMap(Route::getUri, p -> p, (p, q) -> p)).values();
//        swaggerUiConfig.getUrls().add(new SwaggerUiConfigProperties.SwaggerUrl("pokemon-service", "http://localhost:8100/v3/api-docs/pokemon-service"));

        // Add the service name to the drop-down box
       routes.stream().filter(route -> route.getId().matches(".+-service")).forEach(route -> {
            String serviceName = route.getId();
            swaggerUiConfig.addGroup(serviceName);
            swaggerUiConfig.addUrl("http://localhost:8100/v3/api-docs");
//            swaggerUiConfig.setConfigUrl("http://localhost:8100/v3/api-docs/");
//            swaggerUiConfig.getUrls().add(new SwaggerUiConfigProperties.SwaggerUrl(serviceName, "http://localhost:8100/v3/api-docs/"+ serviceName));
        });
        this.routeLocator = routeLocator;

    }
//    @Autowired
//    RouteDefinitionLocator locator;
//
//    @Bean
//    public List<GroupedOpenApi> apis() {
//        List<GroupedOpenApi> groups = new ArrayList<>();
//        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
//        definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service")).forEach(routeDefinition -> {
//            String name = routeDefinition.getId();
//            groups.add(GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build());
//        });
//        return groups;
//    }

//    @Bean
//    public GroupedOpenApi publicApi() {
//        GroupedOpenApi g= GroupedOpenApi.builder()
//                .group("pokemon-service")
//                .pathsToMatch("/pokemon-service/**")
//                .build();
//        return g;
//    }
//
    @Bean
    public OpenAPI newOpenAPI(){
        List<Route> routes = routeLocator.getRoutes().collectList().block();
        List<String> serviceNames = new ArrayList<>();
        routes.stream().filter(route -> route.getId().matches(".+-service")).forEach(route ->
                    serviceNames.add(route.getId()));
        OpenAPI openAPI = new OpenAPI();
        for (String sr : serviceNames){
            Server server = new Server();
            server.setUrl(String.format("http://localhost:8100/%s", sr));
            openAPI.addServersItem(server);
        }
        return openAPI;
    }

}
