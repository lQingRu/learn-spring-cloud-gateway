# Spring Cloud Gateway

## Method 1: Routing Handler (Pure Java)
- Forwards requests to a Gateway Handler Mapping which determines what should be done with requests matching a specific route
```@Bean
public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
      .route("r1", r -> r.host("**.baeldung.com")
        .and()
        .path("/baeldung")
        .uri("http://baeldung.com"))
      .route(r -> r.host("**.baeldung.com")
        .and()
        .path("/myOtherRouting")
        .filters(f -> f.prefixPath("/myPrefix"))
        .uri("http://othersite.com")
        .id("myOtherID"))
    .build();
}
```
## Method 2: Dynamic Routing 
- Properties configuration
```
spring:
  application:
    name: gateway-service  
  cloud:
    gateway:
      routes:
      - id: baeldung
        uri: baeldung.com
      - id: myOtherRouting
        uri: localhost:9999
```

## Method 3 : Spring Cloud DiscoverClientSupport
- Integrate Spring Cloud Gateway with Service Discovery and Registry libraries, such as Eureka Server and Consul
```
@Configuration
@EnableDiscoveryClient
public class GatewayDiscoveryConfiguration {
 
    @Bean
    public DiscoveryClientRouteDefinitionLocator 
      discoveryClientRouteLocator(DiscoveryClient discoveryClient) {
 
        return new DiscoveryClientRouteDefinitionLocator(discoveryClient);
    }
}
```
## Method 4: LoadBalancerClientFilter
- LoadBalancerClientFilter looks for a URI in the exchange attribute property
    - If the URL has a lb scheme `e.g.: lb://baeldung-service`, it will used the Spring Cloud LoadBalancerClient to resolve the name to an actual host and port

## To do:
- Centralize Swagger API docs
    - Currently "try it out" all stripped service name hence 404 Exception
    (need to find a way to add in prefix)
    - [Supposed solution for that but not working, filter not being run](https://programmer.group/spring-cloud-gateway-swagger-nginx-integration.html)
        - Previously was not able to run because the filters only work for services that have been configured to be routed to 
- Another suggested solution: (Centralized API Documentation in Microservices Using Spring Boot, SpringFox Swagger-UI, and Eureka) [https://dzone.com/articles/centralized-documentation-in-microservice-spring-b]
- To explore: [Swagger, Spring Boot 2 With a Reverse-Proxy in Docker](https://medium.com/swlh/swagger-spring-boot-2-with-a-reverse-proxy-in-docker-8a8795aa3da4)

## Resources:
- [Exploring the New Spring Cloud Gateway](https://www.baeldung.com/spring-cloud-gateway)
- [Swagger Resource](https://swagger.io/docs/specification/about/)
- [Custom filters for Spring Gateway](https://www.baeldung.com/spring-cloud-custom-gateway-filters)
- [Quick Guide to Microservices With Spring Boot 2.0, Eureka, and Spring Cloud](https://dzone.com/articles/quick-guide-to-microservices-with-spring-boot-20-e)
- [Spring Cloud Gateway OIDC Tokenrelay](https://github.com/timtebeek/spring-security-samples/tree/master/spring-cloud-gateway-oidc-tokenrelay)
- https://stackoverflow.com/questions/49196368/context-path-with-webflux
- https://stackoverflow.com/questions/30534306/spring-boot-dynamic-servletpath-and-contextpath-for-embedded-tomcat
