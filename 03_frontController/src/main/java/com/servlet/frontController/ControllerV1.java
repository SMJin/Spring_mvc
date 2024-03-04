package com.servlet.frontController;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.Banner;

import java.io.IOException;
import java.util.Map;

public interface ControllerV1 {
    ModelView process(Map<String, String> paramMap);
}
