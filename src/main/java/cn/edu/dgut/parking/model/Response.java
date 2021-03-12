package cn.edu.dgut.parking.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class Response<T> implements Serializable {

    private int code;
    private boolean success;
    private String message;
    private T data;

    public static <R> Response<R> withData(R data) {
        if (data == null) {
            return new Response<R>()
                    .setSuccess(false)
                    .setMessage("未查询到数据")
                    .setCode(201);
        }
        return new Response<R>()
                .setData(data)
                .setSuccess(true)
                .setMessage("成功")
                .setCode(100);
    }

    public static <R> Response<R> withData(Supplier<R> supplier, Function<Throwable, String> failMessage) {
        try {
            return withData(supplier.get());
        } catch (Throwable e) {
            e.printStackTrace();
            return new Response<R>()
                    .setSuccess(false)
                    .setMessage(failMessage.apply(e))
                    .setCode(200);
        }
    }

    public static <R> Response<R> withData(Supplier<R> supplier) {
        return withData(supplier, Throwable::getMessage);
    }

    public int getCode() {
        return code;
    }

    public Response<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public Response<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Response<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public Response<T> setData(T data) {
        this.data = data;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Response<?> response = (Response<?>) o;
        return success == response.success &&
                Objects.equals(message, response.message) &&
                Objects.equals(data, response.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, message, data);
    }

    @Override
    public String toString() {
        return "Response{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

}
