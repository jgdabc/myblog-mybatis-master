package com.jgdabc.common.ExceptionHander;

import com.jgdabc.common.R_;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 兰舟千帆
 * @version 1.0
 * @date 2022/11/12 18:01
 */
@ResponseBody
@ControllerAdvice(annotations = {Controller.class})
public class GlobalException {
    @ExceptionHandler(CustomException.class)
    public R_<String> exceptionHander(CustomException ex)
    {
        System.out.println("捕获到异常");
        System.out.println(ex.getMessage());
        return R_.error(ex.getMessage());
    }


}
