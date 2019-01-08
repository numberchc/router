package com.baiwang.cloud.service.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class TestFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger(TestFilter.class);

    @Override
    public String filterType() {
        //pre 请求被路由之前被调用（一般做一些前置加工）
        //route 在路由请求时调用（将外部请求转发到具体的服务实例上）
        //post 路由请求返回时调用（包装加工返回信息）
        //error 处理请求发生错误时调用
        return "pre";
//        return "route";
    }

    /**
     * 过滤的优先级，数字越大，优先级越低。
     * 通过数字来表示filter的执行顺序
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return 20;
    }

    /**
     * 是否执行该过滤器。
     * true表示需要过滤；false表示不需要
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return false;
//        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        try {
//            ctx.put(FilterConstants.REQUEST_URI_KEY, "http://www.baidu.com");
//            ctx.getResponse().sendRedirect("http://www.baidu.com");
//            ctx.getResponse().sendRedirect("http://192.168.4.227:18080/smartbi/vision/openresource.jsp?resid=I4028218101680c320c32e1b701680d2b9d8b069a&showtoolbar=true&refresh=true&user=admin&password=admin");
//            ctx.put(FilterConstants.FORWARD_TO_KEY, "http://www.baidu.com");
//            HttpServletRequest request = ctx.getRequest();
//            request.getRequestDispatcher("http://www.baidu.com").forward(request, ctx.getResponse());
//            ctx.set("FORWARD_TO_KEY", "http://www.baidu.com");
//            ctx.getResponse().sendRedirect("http://www.baidu.com");
            ctx.getResponse().sendRedirect("http://192.168.4.227:18080/smartbi/vision/openresource.jsp?resid=I402821810167c93ac93ab3cf0167ea711ebc7a95&user=admin&password=admin");
//            ctx.getResponse().sendRedirect("http://192.168.4.227:18080/smartbi/vision/openresource.jsp?resid=I402821810167c93ac93ab3cf0167ea711ebc7a95");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

/*  *//**
 * 过滤器的具体逻辑。
 * http://localhost:8080/it/www?accessToken=www
 *
 * @return
 *//*
    @Override
    public Object run() {
        //实现对参数中accessToken参数的校验
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        logger.info("send {} request to {}", request.getMethod(), request.getRequestURL().toString());
        Object accessToken = request.getParameter("accessToken");
        if (accessToken == null) {
            logger.info("access token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(500);
            return null;
        }
        logger.info("access token ok");
        return null;
    }*/