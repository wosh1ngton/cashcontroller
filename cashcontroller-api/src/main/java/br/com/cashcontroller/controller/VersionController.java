package br.com.cashcontroller.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/version")
public class VersionController {

    @Value("${app.version:unknown}") String version;

    @GetMapping()
    public String getVersion() {
        return version;
    }

}
