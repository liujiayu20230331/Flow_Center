package com.flowcenter.domain.vo;

public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> r = new ApiResponse<>();
        r.code = 0; r.message = "success"; r.data = data;
        return r;
    }

    public static <T> ApiResponse<T> fail(Integer code, String message) {
        ApiResponse<T> r = new ApiResponse<>();
        r.code = code; r.message = message;
        return r;
    }

    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
