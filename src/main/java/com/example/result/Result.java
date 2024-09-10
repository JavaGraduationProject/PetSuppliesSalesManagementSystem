package com.example.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 2L;

    private Integer code;
    private String message;
    private T data;


    public Result setCode(Integer code) {
        this.code = code;
        return this;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Result setData(T data) {
        this.data = data;
        return this;
    }

    public static Result build(){
        return new Result();
    }

    public static Result buildSuccess() {
        return build().setCode(200).setMessage("操作成功");
    }

    public static Result buildSuccess(String message) {
        return build().setCode(200).setMessage(message);
    }

    public static Result buildFail() {
        return build().setCode(400).setMessage("操作失败");
    }

    public static Result buildFail(String message) {
        return build().setCode(400).setMessage(message);
    }


}
