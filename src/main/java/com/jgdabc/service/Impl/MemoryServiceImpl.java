package com.jgdabc.service.Impl;

import com.jgdabc.dao.MemoryDao;
import com.jgdabc.entity.Memory;
import com.jgdabc.service.MemoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: MemoryServiceImpl
 * @Description: 流年记业务层接口实现类
 * @Author ONESTAR
 * @Date: 2020/10/20 9:06
 * @QQ群：530311074
 * @URL：https://onestar.newstar.net.cn/
 * @Version 1.0
 */
@Slf4j
@Service
public class MemoryServiceImpl implements MemoryService {

    @Autowired
    private MemoryDao memoryDao;

    /**
     * 使用 @Cacheable 注解就可以将运行结果缓存，以后查询相同的数据，直接从缓存中取，不需要调用方法。
     * @return
     */
    /**
     * @Cacheable 注解就可以将运行结果缓存，以后查询相同的数据，直接从缓存中取，不需要调用方法。
     * @return
     */
    @Override
    @Cacheable(value = "memoryList",key = "'memory'")       // redis缓存
    public List<Memory> listMemory() {
        log.info("查询流年记忆{}",memoryDao.listMemory());
        return memoryDao.listMemory();
    }

    @Override
    public int saveMemory(Memory memory) {
        log.info("保存流年信息{}",memory);

        return memoryDao.saveMemory(memory);
    }

    @Override
    public Memory getMemory(Long id) {
        log.info("根据id获取流年信息{}",memoryDao.getMemory(id));
        return memoryDao.getMemory(id);
    }

    @Override
    public int updateMemory(Memory memory) {
        log.info("更新流年信息{}",memory);

        return memoryDao.updateMemory(memory);
    }

    @Override
    public void deleteMemory(Long id) {
        log.info("根据i查询流年信息{}",id);
        memoryDao.deleteMemory(id);
    }
}
