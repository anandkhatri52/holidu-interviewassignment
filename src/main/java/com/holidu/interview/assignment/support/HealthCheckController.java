package com.holidu.interview.assignment.support;

import com.holidu.interview.assignment.service.StreetTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthCheckController {

    @Autowired
    private StreetTreeService streetTreeService;

    @RequestMapping(
            name = "healthCheckEndpoint",
            method = RequestMethod.GET,
            value = "/"
    )
    public String healthCheck() {
        return "Greetings from the Holidu interview assignment!";
    }

    @RequestMapping(
            name = "retrieveCommonTreeEndpoint",
            method = RequestMethod.GET,
            value = "/tree"
    )
    public Map<String, Integer> retrieveCommonTree(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam("radius") double radius) {
        return streetTreeService.retrieveCommonStreetTree(latitude, longitude, radius);
    }
}
