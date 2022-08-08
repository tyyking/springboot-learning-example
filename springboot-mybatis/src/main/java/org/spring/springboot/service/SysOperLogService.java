package org.spring.springboot.service;

import org.spring.springboot.common.log.Log;
import org.spring.springboot.dao.SysOperLogDao;
import org.spring.springboot.domain.SysOperLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysOperLogService {
    @Autowired
    private SysOperLogDao sysOperLogDao;
    @Log(title = "operLog", action = "save", channel = "2")
    public int save(SysOperLog sysOperLog){
        return sysOperLogDao.save(sysOperLog);
    }


}
