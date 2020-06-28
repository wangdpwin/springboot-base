package com.precisource.api;

import com.google.common.collect.Maps;
import com.precisource.annotation.PassSecure;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * @Author: xinput
 * @Date: 2020-06-18 22:03
 */
@RestController
public class Applications {

    @PassSecure
    @GetMapping("/status")
    public Map<String, String> status() {
        Map<String, String> status = Maps.newHashMap();
        status.put("status", "ok");
        status.put("server_time", Instant.now().toString());

        return status;
    }
}
