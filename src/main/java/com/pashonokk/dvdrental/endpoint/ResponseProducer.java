package com.pashonokk.dvdrental.endpoint;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.function.Supplier;

import static com.pashonokk.dvdrental.endpoint.ResponseProducer.ResponseHelper.log;

/**
 * Interface determines component that can build REST responses, see {@link BaseResponse}
 */
public interface ResponseProducer {

    /**
     * Return ok base response
     *
     * @return ok response
     */
    default ResponseEntity<BaseResponse> ok() {
        return ResponseEntity.ok(new BaseResponse());
    }

    /**
     * Return specified {@link Status#OK} response
     *
     * @param dto response DTO
     * @param <T> response type
     * @return ok response
     */
    default <T extends BaseResponse> ResponseEntity<T> ok(T dto) {
        return ResponseEntity.ok(dto);
    }

    /**
     * Return {@link Status#FAIL} response built by specified supplier and logging specified exception
     *
     * @param e                    exception
     * @param responseBodySupplier failure response supplier
     * @param status               HTTP status code to be returned
     * @param <T>                  type of fail response
     * @return fail response
     */
    default <T extends BaseResponse> ResponseEntity<Object> fail(Throwable e,
                                                                 Supplier<T> responseBodySupplier,
                                                                 HttpStatus status) {
        T responseBody = responseBodySupplier.get();
        log(e);
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    default <T extends BaseResponse> ResponseEntity<Object> fail(Throwable e, Supplier<T> responseBodySupplier) {
        T responseBody = responseBodySupplier.get();
        log(e);
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(responseBody);
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Slf4j
    class ResponseHelper {
        public static void log(Throwable e) {
            log.error("Exception: {}",
                    e.getMessage(),
                    e);
        }

    }

}