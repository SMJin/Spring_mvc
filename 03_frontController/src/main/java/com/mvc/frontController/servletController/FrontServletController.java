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

@WebServlet(name = "FrontServletControllerV1", urlPatterns = "/front-controller/v1/*")
public class FrontServletController extends HttpServlet {

    private Map<String, MemberServletInterface> controllerMap = new HashMap<>();

    public FrontServletController() {
        controllerMap.put("/front-controller/v1/members/new-form", new MemberFormServlet());
        controllerMap.put("/front-controller/v1/members/save", new MemberSaveServlet());
        controllerMap.put("/front-controller/v1/members", new MemberListServlet());
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

        ModelView mv = controller.process(paramMap);
        MyView view = viewResolver(mv);
        view.render(mv.getModel(), request, response);
    }

    private MyView viewResolver(ModelView modelView) {
        return new MyView("/WEB-INF/views/" + modelView.getViewName() + ".jsp");
    }

    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
