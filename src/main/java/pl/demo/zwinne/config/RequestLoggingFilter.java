package pl.demo.zwinne.config;

import jakarta.servlet.*;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import pl.demo.zwinne.model.LogMessage;
import pl.demo.zwinne.respository.LogRepo;

import java.time.Instant;
import java.util.Enumeration;
import java.io.IOException;

@Component
@Slf4j
public class RequestLoggingFilter implements Filter {

    @Autowired
    Gson gson = new Gson();
    @Autowired
    private LogRepo logRepo;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String message = "[doFilter][" + request + "]" + "[" + request.getMethod() + "]" + "[" + request.getRequestURI() + getParameters(request) + "]" + request.getHeader("user-agent") + request.getRemoteHost();
        filterChain.doFilter(request, response);
        try {
            LogMessage logMessage = new LogMessage("0", message, Instant.now(), "Logger");
            logRepo.save(logMessage);
        } catch (Exception exception) {
            throw new RuntimeException("doFilter Producer Exception");
        }
    }

    private String getParameters(HttpServletRequest request) {
        StringBuilder posted = new StringBuilder();
        Enumeration<?> e = request.getParameterNames();
        if (e != null) {
            posted.append("?");
        }
        while (e.hasMoreElements()) {
            if (posted.length() > 1) {
                posted.append("&");
            }
            String curr = (String) e.nextElement();
            posted.append(curr).append("=");
            if (curr.contains("password") || curr.contains("answer") || curr.contains("pwd")) {
                posted.append("*****");
            } else {
                posted.append(request.getParameter(curr));
            }
        }
        String ip = request.getHeader("X-FORWARDED-FOR");
        String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
        if (!StringUtils.isEmpty(ipAddr)) {
            posted.append("&_psip=" + ipAddr);
        }
        return posted.toString();
    }

    private String getRemoteAddr(HttpServletRequest request) {
        String ipFromHeader = request.getHeader("X-Forwarded-For");
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }

}