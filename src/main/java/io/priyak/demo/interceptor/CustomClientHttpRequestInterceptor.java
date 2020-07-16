package io.priyak.demo.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Base64;

/**
 * CustomImplementation for {@link ClientHttpRequestInterceptor}.
 * There are already spring implementations of this class and any one of them can be used to fulfill the
 * user-case.
 *
 * {@link ClientHttpRequestInterceptor} is also an {@link FunctionalInterface}, the implementation
 * could have been passed inline as a lambda
 *
 * @author priyakdey
 */
public class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    private final String username;
    private final String password;

    public CustomClientHttpRequestInterceptor(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        final byte[] byteArrayBytes = (username + ":" + password).getBytes();
        final String encodedToken = Base64.getEncoder().encodeToString(byteArrayBytes);
        final HttpHeaders headers = request.getHeaders();
        if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
            headers.add(HttpHeaders.AUTHORIZATION, "Basic " + encodedToken);
        }

        return execution.execute(request, body);
    }
}
