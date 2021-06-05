package com.learning.springgateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Primary
public class SwaggerConfig implements SwaggerResourcesProvider {
    public static final String API_URI = "/v3/api-docs";

    @Autowired
    private RouteLocator routeLocator;

    private final GatewayProperties gatewayProperties;

    public SwaggerConfig(GatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;
    }

    //    @Override
//    public List<SwaggerResource> get() {
//        List<SwaggerResource> resources = new ArrayList<>();
//
//        // I used service names with lowercase hence the toLowerCase.
//        // Remove the toLowerCase if you use service name with caps as is default with eureka.
//        routeLocator.getRoutes().subscribe(route -> {
//            String name = route.getId();
//            resources.add(swaggerResource(name, "/" + name.toLowerCase() + "/v3/api-docs", "2.0"));
//        });
//
//        return resources;
//    }
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        //Remove route from gateway
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
        //Combining route-path (Path) configuration with route filtering, only valid route nodes are obtained.
        gatewayProperties.getRoutes().stream().filter(routeDefinition -> routes.contains(routeDefinition.getId()))
                .forEach(routeDefinition -> routeDefinition.getPredicates().stream()
                        .filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
                        .forEach(predicateDefinition -> {
                            // FIXME has limited capabilities and can't think of a better way to integrate the grouping of individual services
                            String name = routeDefinition.getId();
                            // Name is the service name
                            resources.add(swaggerResource(name, "/" + name.toLowerCase() + "/v3/api-docs"));

                            resources.add(swaggerResource(name,
                                    predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
                                            .replace("/**", API_URI)));
                        }));
        return resources;
    }
    private SwaggerResource swaggerResource(final String name, final String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }


}
