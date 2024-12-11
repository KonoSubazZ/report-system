package com.novo.report.common;

public class Result<T> {
    private int status;
    private String message;
    private T data;

    // 成功状态码
    public static final int SUCCESS_STATUS = 200;
    // 失败状态码
    public static final int FAILURE_STATUS = 500;

    public Result() {}

    public Result(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // 成功时调用的方法
    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS_STATUS, "Success", data);
    }

    // 成功时调用的方法，自定义消息
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(SUCCESS_STATUS, message, data);
    }

    // 失败时调用的方法
    public static <T> Result<T> failure(String message) {
        return new Result<>(FAILURE_STATUS, message, null);
    }

    // 失败时调用的方法，自定义状态码
    public static <T> Result<T> failure(int status, String message) {
        return new Result<>(status, message, null);
    }

    // Getter 和 Setter 方法
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
