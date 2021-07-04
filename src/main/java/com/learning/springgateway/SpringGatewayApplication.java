package com.learning.springgateway;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(servers = {
        @Server(url = "http://localhost:8100/", description = "docker"),
        @Server(url = "https://localhost/preis-api", description = "kubernetes")
})
public class SpringGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringGatewayApplication.class, args);
    }
// This does not work probably because of using spring cloud gateway instead of zuul..maybe paths cannot be found so no groups
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
//
//        });
//
//        return groups;
//    }
}
