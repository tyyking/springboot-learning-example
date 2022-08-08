package org.spring.springboot.controller;

import org.spring.springboot.common.log.Log;
import org.spring.springboot.domain.City;
import org.spring.springboot.domain.SysOperLog;
import org.spring.springboot.service.CityService;
import org.spring.springboot.service.SysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bysocket on 07/02/2017.
 */
@RestController
public class CityRestController {

    @Autowired
    private CityService cityService;

    @Autowired
    private SysOperLogService sysOperLogService;

    @Log(title = "city", action = "find", channel = "1")
    @RequestMapping(value = "/api/city", method = RequestMethod.GET)
    public City findOneCity(@RequestParam(value = "cityName", required = true) String cityName) {
        return cityService.findCityByName(cityName);
    }
    @RequestMapping(value = "/api/sysOperLog/save", method = RequestMethod.GET)
    public int save(@RequestParam(value = "title", required = true) String title) {
        SysOperLog sysOperLog = new SysOperLog();
        sysOperLog.setTitle(title);
        return sysOperLogService.save(sysOperLog);
    }

}
