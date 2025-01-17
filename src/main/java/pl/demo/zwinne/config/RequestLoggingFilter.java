package pl.demo.zwinne.config;

import jakarta.servlet.*;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import pl.demo.zwinne.respository.LogRepo;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Enumeration;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class RequestLoggingFilter implements Filter {

    @Autowired
    Gson gson = new Gson();
//    @Autowired
//    private LogRepo logRepo;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String message = "[doFilter][" + request + "]" + "[" + request.getMethod() + "]" + "[" + request.getRequestURI() + getParameters(request) + "]" + request.getHeader("user-agent") + request.getRemoteHost();
        filterChain.doFilter(request, response);
        try {
            Map<String, String> logData = new HashMap<>();
            logData.put("Date", String.valueOf(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault())));
            logData.put("Type", "INFO");
            logData.put("Message", message);
            log.info(logData.get("Message"));
//            logRepo.save(logData);
        } catch (Exception exception) {
            throw new RuntimeException("doFilter Producer Exception");
        }
//        try (Producer<String, String> logProducer = producerFactory.createProducer()) {
//
//            Map<String, String> logData = new HashMap<>();
//            logData.put("Date", String.valueOf(LocalDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault())));
//            logData.put("Type", "INFO");
//            logData.put("Message", message);
//            ProducerRecord<String, String> producerRecord = new ProducerRecord<>("logs", gson.toJson(logData));
//            logProducer.send(producerRecord);
//
//        } catch (Exception exception) {
//            throw new ProducerSendException("doFilter Producer Exception");
//        }
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