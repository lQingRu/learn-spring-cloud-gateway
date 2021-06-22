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
public class SwaggerRouting implements SwaggerResourcesProvider {
    public static final String API_URI = "/v3/api-docs";

    @Autowired
    private RouteLocator routeLocator;

    private final GatewayProperties gatewayProperties;

    public SwaggerRouting(GatewayProperties gatewayProperties) {
        this.gatewayProperties = gatewayProperties;
    }

        @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();

        // Remove the toLowerCase if you use service name with caps as is default with eureka
        routeLocator.getRoutes().subscribe(route -> {
            String name = route.getId();
            resources.add(swaggerResource(name, "/" + name.toLowerCase() + "/v3/api-docs"));
        });

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
