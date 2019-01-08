package com.baiwang.cloud.service.filter.smartbi;

import com.baiwang.cloud.common.constants.Const;
import com.baiwang.cloud.common.util.SpringApplicationUtil;
import com.baiwang.cloud.service.ISmartbi;
import com.baiwang.cloud.service.util.MoiraiUtil;
import com.baiwang.moirai.model.user.MoiraiUser;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;

/**
 * Smartbi过滤器
 */
@Component
public class SmartbiFilter extends ZuulFilter {
    private static final Logger logger = LoggerFactory.getLogger(SmartbiFilter.class);

    @Override
    public String filterType() {
//        return FilterConstants.PRE_TYPE;
        return FilterConstants.ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return RequestContext.getCurrentContext().getRequest().getRequestURL().toString().contains(Const.ROURTER_smartbi);
//        return false;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        try {
            //获取参数
            String chartName = request.getParameter(Const.ROUTE_PARAM_chartName);//对于服务baen名称
            Assert.hasText(chartName, "名称不能为空");
            String tenantId = request.getParameter(Const.ROUTE_PARAM_tenantId);
            Assert.hasText(tenantId, "租户id不能为空");
            String orgId = request.getParameter(Const.ROUTE_PARAM_orgId);
            Assert.hasText(orgId, "机构id不能为空");
            String userId = request.getParameter(Const.ROUTE_PARAM_userId);
            Assert.hasText(userId, "用户id不能为空");
            //根据名称获取SmartbiService
            ISmartbi smartbiService = (ISmartbi) SpringApplicationUtil.getBeanByName(chartName);
            //获取具体url
            String url = smartbiService.getUrl(tenantId, orgId, userId);
            //跳转
            ctx.getResponse().sendRedirect(url);
            //ctx.getResponse().sendRedirect("http://www.baidu.com");
        } catch (Exception e) {
            logger.error("执行异常", e);
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(500);
        }
        return null;
    }
}
