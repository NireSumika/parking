package campuslifecenter.common.model;

import campuslifecenter.common.exception.ProcessException;
import campuslifecenter.common.exception.ResponseException;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

public class Response<T> implements Serializable {

    @ApiModelProperty("代码")
    private int code;
    @ApiModelProperty("是否成功")
    private boolean success;
    @ApiModelProperty("消息")
    private String message;
    @ApiModelProperty("数据")
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
        } catch (ResponseException e) {
            e.printStackTrace();
            return new Response<R>()
                    .setSuccess(false)
                    .setMessage(e.getMessage())
                    .setCode(e.getCode());
        } catch (Throwable e) {
            e.printStackTrace();
            return new Response<R>()
                    .setSuccess(false)
                    .setMessage(failMessage.apply(e))
                    .setCode(200);
        }
    }

    /**
     * 检查并获取
     * @param module {@link ProcessException} 模块
     * @param message 错误消息
     */
    public T checkGet(String module, String message) {
        if (!isSuccess()) {
            throw new ProcessException(module, message, this);
        }
        return getData();
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
