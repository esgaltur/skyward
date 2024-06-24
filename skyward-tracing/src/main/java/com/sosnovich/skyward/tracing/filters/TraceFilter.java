package com.sosnovich.skyward.tracing.filters;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.MultivaluedMap;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A filter that traces incoming HTTP requests and outgoing HTTP responses using OpenTelemetry.
 * This filter integrates with SLF4J MDC to add trace and span information to log entries.
 */
@Component
public class TraceFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private final Tracer tracer;

    /**
     * Constructs a new TraceFilter with the specified Tracer.
     *
     * @param tracer the OpenTelemetry Tracer to use for tracing
     */
    public TraceFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    /**
     * Filters incoming HTTP requests to start a new trace span and add trace information to the MDC.
     *
     * @param requestContext the context of the incoming request
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Span currentSpan = tracer.spanBuilder("request")
                .setSpanKind(SpanKind.SERVER)
                .startSpan();

        try (Scope scope = currentSpan.makeCurrent()) {
            SpanContext spanContext = currentSpan.getSpanContext();
            MDC.put("trace.id", spanContext.getTraceId());
            MDC.put("span.id", spanContext.getSpanId());

            String requestBody = getRequestBody(requestContext);
            Map<String, Object> requestHeaders = getHeaders(requestContext.getHeaders());

            MDC.put("http.request.method", requestContext.getMethod());
            MDC.put("http.request.uri", requestContext.getUriInfo().getRequestUri().toString());
            MDC.put("http.request.body", requestBody);
            MDC.put("http.request.headers", requestHeaders.toString());
        }
    }

    /**
     * Filters outgoing HTTP responses to add trace information to the MDC and end the current span.
     *
     * @param requestContext the context of the incoming request
     * @param responseContext the context of the outgoing response
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        try {
            String responseBody = getResponseBody(responseContext);
            Map<String, Object> responseHeaders = getHeaders(responseContext.getHeaders());

            MDC.put("http.response.status", String.valueOf(responseContext.getStatus()));
            MDC.put("http.response.body", responseBody);
            MDC.put("http.response.headers", responseHeaders.toString());
        } finally {
            MDC.clear();
            Span.current().end();
        }
    }

    /**
     * Retrieves the body of the incoming request.
     *
     * @param requestContext the context of the incoming request
     * @return the request body as a string
     * @throws IOException if an I/O error occurs
     */
    private String getRequestBody(ContainerRequestContext requestContext) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = requestContext.getEntityStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        byte[] requestEntity = out.toByteArray();
        requestContext.setEntityStream(new ByteArrayInputStream(requestEntity));
        return new String(requestEntity);
    }

    /**
     * Retrieves the body of the outgoing response if it is a string.
     *
     * @param responseContext the context of the outgoing response
     * @return the response body as a string, or null if there is no body or it is not a string
     */
    private String getResponseBody(ContainerResponseContext responseContext) {
        if (responseContext.hasEntity() && responseContext.getEntity() instanceof String) {
            return responseContext.getEntity().toString();
        }
        return "<empty body>";
    }

    /**
     * Retrieves the headers from the given multivalued map.
     *
     * @param headers the headers to retrieve
     * @return a map containing the headers
     */
    private Map<String, Object> getHeaders(MultivaluedMap<String, ?> headers) {
        Map<String, Object> headersMap = new HashMap<>();
        for (Map.Entry<String, ? extends List<?>> entry : headers.entrySet()) {
            headersMap.put(entry.getKey(), String.join(", ", entry.getValue().toString()));
        }
        return headersMap;
    }
}
