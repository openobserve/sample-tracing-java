package src;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.http.trace.OtlpHttpSpanExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;

public class OpenTelemetrySupport {
        static {
                Resource resource = Resource.getDefault()
                                .merge(Resource.create(Attributes.of(
                                                ResourceAttributes.SERVICE_NAME, "test-zo-service",
                                                ResourceAttributes.HOST_NAME, "test-host")));
                // set url from openobserve ingestion traces page
                String creds = "Basic YUBhLmNvbTph";

                // set url from openobserve ingestion traces page
                String openobserve_http_url = "http://127.0.0.1:5080/api/nexus/traces";

                String openobserve_grpc_url = "http://127.0.0.1:5081";
                String openobserve_grpc_org = "dummy";

                // using HTTP exporter
                SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                                .addSpanProcessor(BatchSpanProcessor.builder(OtlpHttpSpanExporter.builder()
                                                .setEndpoint(openobserve_http_url) // set url from
                                                // opentraces
                                                .addHeader("Authorization",
                                                                creds)
                                                .build()).build())
                                .setResource(resource)
                                .build();

                // using GRPC exporter
                /*
                 * SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                 * .addSpanProcessor(BatchSpanProcessor.builder(OtlpGrpcSpanExporter.builder()
                 * .setEndpoint(openobserve_grpc_url)
                 * .addHeader("Authorization",
                 * creds)
                 * .addHeader("zinc-org-id",
                 * openobserve_grpc_org)
                 * .build()).build())
                 * .setResource(resource)
                 * .build();
                 */

                OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
                                .setTracerProvider(sdkTracerProvider)
                                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                                .buildAndRegisterGlobal();

                tracer = openTelemetry.getTracer("sample-tracing-java", "1.0.0");
        }

        private static Tracer tracer;

        public static Tracer getTracer() {
                return tracer;
        }

}