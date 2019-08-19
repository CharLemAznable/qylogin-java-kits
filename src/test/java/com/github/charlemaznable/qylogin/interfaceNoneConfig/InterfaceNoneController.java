package com.github.charlemaznable.qylogin.interfaceNoneConfig;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.github.charlemaznable.codec.Json.json;
import static com.github.charlemaznable.net.Http.fetchParameterMap;
import static com.github.charlemaznable.net.Http.responseJson;

@Controller
@RequestMapping("/interface/none")
public class InterfaceNoneController {

    @RequestMapping("/index")
    public void index(HttpServletRequest request, HttpServletResponse response) {
        responseJson(response, json(fetchParameterMap(request)));
    }
}
