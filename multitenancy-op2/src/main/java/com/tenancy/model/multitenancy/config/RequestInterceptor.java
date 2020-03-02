package com.tenancy.model.multitenancy.config;

/**
 * Interceptor for every request to find the tenantid as the header value in it.
 */

import com.tenancy.model.multitenancy.util.Constants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
@Slf4j
public class RequestInterceptor extends HandlerInterceptorAdapter {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
    String requestURI = request.getRequestURI();
    String tenantID = request.getHeader(Constants.TENANTID);
    log.debug(String.format("Request URI: %s for tenant: %s",requestURI,tenantID));
    if (tenantID == null) {
      response.getWriter().write("tenantid header missing");
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return false;
    }
    TenantContext.setTenant(tenantID);
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView){
    TenantContext.clear();
  }

}