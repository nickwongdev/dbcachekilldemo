package com.yskts.cachekill.controller;

import com.yskts.cachekill.manager.RedisManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by Nick on 8/24/2014.
 */
@Controller
public class CacheKillController {

    @Resource
    private RedisManager redisManager;

    @RequestMapping(value = "/cachekill", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void doGet(
            @RequestParam("entity") final String entity,
            @RequestParam("key") final String key
    ) {
        redisManager.del(entity, key);
        System.out.println("Cache Kill on Entity: " + entity + " Key: " + key);
    }
}
