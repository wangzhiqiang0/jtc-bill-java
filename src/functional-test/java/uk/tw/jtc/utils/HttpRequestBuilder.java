package uk.tw.jtc.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Map;

public class HttpRequestBuilder {
    public static HttpEntity<String> getUsageHttpEntity(Object obj) {
        HttpEntity<String> entity = new HttpEntity(obj, getHttpHeaders());
        return entity;
    }

    public static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("customerId", TestUtils.CUSTOMER_ID);
        return headers;
    }
}
