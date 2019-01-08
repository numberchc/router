package com.baiwang.cloud.service.impl.smartbi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baiwang.cloud.service.ISmartbi;
import com.baiwang.cloud.service.util.MoiraiUtil;
import com.baiwang.moirai.model.user.MoiraiUserOrgExtend;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * Smartbi服务抽象类
 */
public abstract class AbstractSmartbiService implements ISmartbi {

    @Value("${smartbi.user}")
    private String user;

    @Value("${smartbi.password}")
    private String password;

    @Value("${smartbi.url}")
    private String url;

    /**
     * 根据参数获取目标url
     *
     * @param userId
     * @return
     */
    @Override
    public String getUrl(String tenantId, String orgId, String userId) {
        //参数
//        MoiraiUser user = MoiraiUtil.getUserById(userId);
//        String tenantId = user.getTenantId().toString();
//        String orgId = user.getOrgId().toString();
        //获取权限机构
        List<MoiraiUserOrgExtend> userOrgs = MoiraiUtil.getUserOrgs(tenantId, orgId, userId);
        //组织Smartbi请求参数
        JSONArray stanbyValue = new JSONArray();
        String orgName = null;
        for (MoiraiUserOrgExtend org : userOrgs) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(org.getOrgId());
            jsonArray.add(org.getOrgName());
            stanbyValue.add(jsonArray);
            if (org.getOrgId() == Long.valueOf(orgId)) {
                orgName = org.getOrgName();
            }
        }
        JSONArray paramsInfo = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "机构名称");
        jsonObject.put("value", orgId);
        jsonObject.put("displayValue", orgName);
        jsonObject.put("stanbyValue", stanbyValue);
        paramsInfo.add(jsonObject);
        String resid = getResid();
        StringBuffer targetUrl = new StringBuffer(url);
        targetUrl.append("?").append(paramsInfo).append("&resid=" + resid).append("&user=" + user).append("&password=" + password);
        return targetUrl.toString();
    }

//    //测试
//    @Override
//    public String getUrl(String tenantId, String orgId, String userId) {
//        //组织Smartbi请求参数
//        JSONArray stanbyValue = new JSONArray();
//        String orgName = "沫涯泉（北京）餐饮管理有限公司";
//        JSONArray jsonArray = new JSONArray();
//        jsonArray.add("1700000815738");
//        jsonArray.add("沫涯泉（北京）餐饮管理有限公司");
//        stanbyValue.add(jsonArray);
//        JSONArray jsonArray1 = new JSONArray();
//        jsonArray1.add("1700000000003");
//        jsonArray1.add("滴滴出行有限公司");
//        stanbyValue.add(jsonArray1);
//        JSONArray paramsInfo = new JSONArray();
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("name", "机构名称");
//        jsonObject.put("value", "1700000815738");
//        jsonObject.put("displayValue", orgName);
//        jsonObject.put("stanbyValue", stanbyValue);
//        paramsInfo.add(jsonObject);
//        String resid = getResid();
//        StringBuffer targetUrl = new StringBuffer(url);
//        targetUrl.append("?paramsInfo=").append(paramsInfo).append("&resid=" + resid).append("&user=" + user).append("&password=" + password);
//        return targetUrl.toString();
//    }

    /**
     * 获取资源id<br>
     * 由子类实现
     */
    public abstract String getResid();

}
