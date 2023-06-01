# sample-tracing-java
Java tracing example 


Please update below mentioned in class OpenTelemetrySupport.java, from ingestion -> traces -> Open Telemetry from OpenObserve application.

String creds = "Basic YUBhLmNvbTph";
// set url from openobserve ingestion traces page
String openobserve_http_url = "http://127.0.0.1:5080/api/nexus123/traces";

String openobserve_grpc_url = "http://127.0.0.1:5081";
String openobserve_grpc_org = "dummy";


By default it exports traces using OtlpHttpSpanExporter , one can switch to OtlpGrpcSpanExporter by commenting out http exporter & uncommenting Grpc exporter.
