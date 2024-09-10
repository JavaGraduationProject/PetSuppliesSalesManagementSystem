package com.example.exception;

import com.example.result.Result;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobleExceptionHandler {

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result CustomException(CustomException e){
        return Result.buildFail(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result BindException(BindException e, Model model){
        model.addAttribute("error", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();
        String errorMessage = "";
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage += fieldError.getDefaultMessage()+"；";
        }
        return Result.buildFail(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public String handlerException(Exception e, Model model){
        model.addAttribute("error", e.getMessage());
        return "error";
    }


}
