package com.yxx.mall.member.exception;

import com.yxx.mall.common.utils.R;
import com.yxx.mall.common.utils.RRException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * @author xyong
 * date 2021-05-10
 * 全局异常处理
 */
@RestControllerAdvice
@Slf4j
public class RRExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(RRException.class)
    public R handleRRException(RRException e){
        R r = new R();
        r.put("code", e.getCode());
        r.put("msg", e.getMessage());
        return r;
    }
    /***
     * 数据校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R notValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> errors = bindingResult.getAllErrors();
        ObjectError error = errors.get(0);
        log.error(error.getDefaultMessage());
        return R.error(error.getDefaultMessage());
    }

    /**
     * 系统异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public R handleException(Exception e){
        log.error(e.getMessage(), e);
        return R.error();
    }


}
