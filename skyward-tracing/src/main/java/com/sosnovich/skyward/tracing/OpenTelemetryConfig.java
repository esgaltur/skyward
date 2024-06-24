package com.sosnovich.skyward.tracing;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up OpenTelemetry tracing.
 * This class configures the OpenTelemetry SDK, exporter, and tracer for the application.
 */
@Configuration
public class OpenTelemetryConfig {

    @Value("${otel.exporter.otlp.endpoint:http://127.0.0.1:14250}")
    private String otlpEndpoint;

    @Value("${otel.resource.attributes:service.name=skyward-service}")
    private String serviceName;

    /**
     * Configures and returns an instance of {@link OpenTelemetry}.
     * The configuration includes setting up the OTLP gRPC span exporter and the resource attributes.
     *
     * @return the configured {@link OpenTelemetry} instance
     */
    @Bean
    public OpenTelemetry openTelemetry() {
        OtlpGrpcSpanExporter otlpExporter = OtlpGrpcSpanExporter.builder()
                .setEndpoint(otlpEndpoint)
                .build();

        Resource serviceNameResource = Resource.create(io.opentelemetry.api.common.Attributes.of(
                ResourceAttributes.SERVICE_NAME, serviceName));

        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(BatchSpanProcessor.builder(otlpExporter).build())
                .setResource(Resource.getDefault().merge(serviceNameResource))
                .build();

        return OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .build();
    }

    /**
     * Configures and returns an instance of {@link Tracer}.
     * The tracer is obtained from the provided {@link OpenTelemetry} instance.
     *
     * @param openTelemetry the {@link OpenTelemetry} instance to get the tracer from
     * @return the configured {@link Tracer} instance
     */
    @Bean
    public Tracer tracer(OpenTelemetry openTelemetry) {
        return openTelemetry.getTracer("skyward-tracer");
    }
}
