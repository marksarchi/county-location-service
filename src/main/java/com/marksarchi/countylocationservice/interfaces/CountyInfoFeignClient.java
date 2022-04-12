package com.marksarchi.countylocationservice.interfaces;

import com.marksarchi.countylocationservice.dto.ResponseWrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "location-service",url = "${county-info.service.url}")
public interface CountyInfoFeignClient {
    @GetMapping("/county")
    ResponseWrapper fetchCountyInfo(@RequestParam String name);

}
