package com.flowcenter.exception;

import com.flowcenter.domain.vo.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BizException.class)
    public ApiResponse<Void> handleBiz(BizException e) { return ApiResponse.fail(e.getCode(), e.getMessage()); }

    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    public ApiResponse<Void> handleParam(Exception e) {
        LOGGER.warn("validation failed", e);
        return ApiResponse.fail(ErrorCode.PARAM_ERROR, e.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ApiResponse<Void> handleDup(DuplicateKeyException e) {
        LOGGER.warn("duplicate key", e);
        return ApiResponse.fail(ErrorCode.DUPLICATE_KEY, "duplicate key");
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleSys(Exception e) {
        LOGGER.error("system error", e);
        return ApiResponse.fail(ErrorCode.SYSTEM_ERROR, "system error");
    }
}
