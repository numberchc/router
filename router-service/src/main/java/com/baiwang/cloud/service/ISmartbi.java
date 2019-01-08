package com.baiwang.cloud.service;

/**
 * Smartbi服务类接口
 */
public interface ISmartbi {

//    /**
//     * 根据用户id获取具体报表
//     * @param userId
//     * @return
//     */
//    String getUrl(String userId);

    /**
     * 根据参数获取目标url
     * @param tenantId
     * @param orgId
     * @param userId
     * @return
     */
    String getUrl(String tenantId, String orgId, String userId);

}
