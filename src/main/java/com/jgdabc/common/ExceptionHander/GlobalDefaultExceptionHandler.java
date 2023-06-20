package com.jgdabc.common.ExceptionHander;


import com.jgdabc.common.R_;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 兰舟千帆
 * @version 1.0
 * @date 2022/12/9 21:06
 */
@ControllerAdvice(annotations = Controller.class)
@Slf4j
//做一个捕获参数校验异常的类
public class GlobalDefaultExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public R_ handleParamCheckExcepion(HttpServletRequest req, MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder paramErrorMsg = new StringBuilder();
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError objectError : allErrors) {
                String msg = objectError.getDefaultMessage();
                if (paramErrorMsg.length() == 0) {
                    paramErrorMsg.append(msg);
                } else {
                    paramErrorMsg.append(',');
                    paramErrorMsg.append(msg);
                }
            }
        }
        logger.error("参数校验失败! uri:{},错误信息:{}", req.getRequestURI(), paramErrorMsg.toString());
        return R_.error(paramErrorMsg.toString());
    }
}
