package com.mvc.frontController.servletController;

import com.mvc.frontController.servletController.memberInstance.MemberFormServlet;
import com.mvc.frontController.servletController.memberInstance.MemberListServlet;
import com.mvc.frontController.servletController.memberInstance.MemberSaveServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@WebServlet(name = "FrontServletController", urlPatterns = "/front-controller/*")
public class FrontServletController extends HttpServlet {

    private Map<String, MemberServletInterface> controllerMap = new HashMap<>();

    public FrontServletController() {
        controllerMap.put("/front-controller/members/new-form", new MemberFormServlet());
        controllerMap.put("/front-controller/members/save", new MemberSaveServlet());
        controllerMap.put("/front-controller/members", new MemberListServlet());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        MemberServletInterface controller = controllerMap.get(requestURI);
        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, String> paramMap = createParamMap(request);
        Map<String, Object> model = new HashMap<>();

        String viewName = controller.process(paramMap, model);

        MyView view = viewResolver(viewName);
        view.render(model, request, response);
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
