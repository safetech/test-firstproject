package smoketest.utils;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class RestApiClient {
    public static final int WAIT_TIMEOUT = 30000;

    private static final RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());

    private HttpHeaders headers;
    private HttpMethod httpMethod;
    private String hostName;
    private String restUri;
    private String requestBody;
    private ResponseEntity<String> responseEntity;


    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getRestUri() {
        return restUri;
    }

    public void setRestUri(String restUri) {
        this.restUri = restUri;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }


    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public ResponseEntity<String> getResponseEntity() {
        return responseEntity;
    }

    public static class HttpEntityEnclosingDeleteRequest extends HttpEntityEnclosingRequestBase {

        public HttpEntityEnclosingDeleteRequest(final URI uri) {
            super();
            setURI(uri);
        }

        @Override
        public String getMethod() {
            return "DELETE";
        }
    }

    private static ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory() {
            @Override
            protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
                if (HttpMethod.DELETE == httpMethod) {
                    return new HttpEntityEnclosingDeleteRequest(uri);
                }
                return super.createHttpUriRequest(httpMethod, uri);
            }
        };
        factory.setReadTimeout(WAIT_TIMEOUT);
        factory.setConnectTimeout(WAIT_TIMEOUT);

        return factory;
    }



    public void execute() {

        HttpEntity<String> request = new HttpEntity<>(getRequestBody(), getHeaders());
        CustomRestTemplateResponseErrorHandler responseErrorHandler = new CustomRestTemplateResponseErrorHandler();
        restTemplate.setErrorHandler(responseErrorHandler);
        responseEntity = restTemplate.exchange(getHostName() + getRestUri(), getHttpMethod(), request, String.class);
    }
}
