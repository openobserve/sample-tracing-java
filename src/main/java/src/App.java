/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package src;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;

public class App {
    private ExecutorService es = Executors.newFixedThreadPool(5);

    public String getGreeting() {
        Tracer tracer = OpenTelemetrySupport.getTracer();
        Span span = tracer.spanBuilder("OpenObserve Tracing")
                .setParent(Context.current().with(Span.current())) // Optional. The system automatically configures the
                                                                   // settings.
                .startSpan();

        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("span-id", "111");

            es.submit(new Runnable() {
                @Override
                public void run() {
                    Span asyncSpan = tracer.spanBuilder("async")
                            .setParent(Context.current().with(span))
                            .startSpan();
                    try {
                        Thread.sleep(10); // some async jobs
                    } catch (Throwable e) {
                    }
                    asyncSpan.end();
                }
            });

            Thread.sleep(10);
            System.out.println("tracing done");
            OpenTelemetry openTelemetry = GlobalOpenTelemetry.get();
            openTelemetry.getPropagators();
        } catch (Throwable t) {
            span.setStatus(StatusCode.ERROR, "handle error");
        } finally {
            span.end();
        }
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
    }
}
