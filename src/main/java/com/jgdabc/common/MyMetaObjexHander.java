package com.jgdabc.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author 兰舟千帆
 * @version 1.0
 * @date 2022/12/20 11:02
 * @Description 功能描述:公共字段自动填充配置
 */
@Component
@Slf4j
public class MyMetaObjexHander implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充--插入填充");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充---更新");
        log.info(metaObject.toString());
        metaObject.setValue("updateTime",LocalDateTime.now());


    }
}
