package com.yskts.cachekill.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by Nick on 8/24/2014.
 */

@Configuration
@EnableWebMvc
@ComponentScan({
        "com.yskts.cachekill.controller",
        "com.yskts.cachekill.manager",
})
public class CacheKillConfig {
}
