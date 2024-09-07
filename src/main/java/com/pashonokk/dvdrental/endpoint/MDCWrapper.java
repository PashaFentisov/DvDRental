package com.pashonokk.dvdrental.endpoint;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MDCWrapper {

    private static final String REQUEST_ID_KEY = "requestId";

    public static void putRequestId() {
        MDC.put(REQUEST_ID_KEY, RandomStringUtils.randomAlphanumeric(16));
    }

    public static String getRequestId() {
        return MDC.get(REQUEST_ID_KEY);
    }

}