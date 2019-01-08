package com.baiwang.cloud.integration;

import com.baiwang.cloud.common.exception.SystemException;
import com.baiwang.cloud.integration.impl.MoiraiClientHystrix;
import com.baiwang.moirai.model.common.BWJsonResult;
import com.baiwang.moirai.model.org.MoiraiOrg;
import com.baiwang.moirai.model.org.MoiraiOrgCondition;
import com.baiwang.moirai.model.user.MoiraiUser;
import com.baiwang.moirai.model.user.MoiraiUserCondition;
import com.baiwang.moirai.model.user.MoiraiUserOrgExtend;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * 组织机构远程服务
 *
 * @Author chenhuichang
 */
@FeignClient(value = "moirai-service", fallbackFactory = MoiraiClientHystrix.class)
public interface IMoiraiClient {

    /**
     * 1.19 按条件查询税号相关列表
     *
     * @param var1
     * @return
     * @throws SystemException
     */
    @RequestMapping(value = {"/org/getTaxEntityList"}, method = {RequestMethod.POST})
    BWJsonResult<MoiraiOrg> getTaxEntityList(@RequestBody MoiraiOrgCondition var1) throws SystemException;

    /**
     * 1.24 查询机构下的纳税主体或非纳税本体列表
     *
     * @param var1
     * @return
     * @throws SystemException
     */
    @RequestMapping(
            value = {"/org/getOrgTaxTypeList"},
            method = {RequestMethod.POST},
            consumes = {"application/json;charset=UTF-8"}
    )
    BWJsonResult<MoiraiOrg> getOrgTaxTypeList(@RequestBody MoiraiOrg var1) throws SystemException;

    /**
     * 1.15 批量查询组织机构
     *
     * @param var1
     * @return
     * @throws SystemException
     */
    @RequestMapping(
            value = {"/org/getOrgListByCondition"},
            method = {RequestMethod.POST},
            consumes = {"application/json;charset=UTF-8"}
    )
    BWJsonResult<MoiraiOrg> getOrgListByCondition(@RequestBody Map<String, List> var1) throws SystemException;

    /**
     * 1.9 按条件查询组织机构
     *
     * @param var1
     * @return
     */
    @RequestMapping(
            value = {"/org/getOrgByCondition"},
            method = {RequestMethod.POST},
            consumes = {"application/json;charset=UTF-8"}
    )
    BWJsonResult<MoiraiOrg> getOrgByCondition(@RequestBody MoiraiOrgCondition var1);

    /**
     * 1.25 查询组织机构的纳税主体
     *
     * @param condition
     * @return
     */
    @RequestMapping(
            value = {"/org/getOrgTaxEntity"},
            method = {RequestMethod.POST}
    )
    BWJsonResult<MoiraiOrg> getOrgTaxEntity(@RequestBody Map<String, Object> condition);

    /**
     * 2.15 获取用户授权下级机构
     * （已弃用）
     *
     * @param var1
     * @return
     * @throws SystemException
     */
    @RequestMapping(
            value = {"/user/getUserOrg"},
            method = {RequestMethod.POST},
            consumes = {"application/json;charset=UTF-8"}
    )
    BWJsonResult<MoiraiOrg> getUserOrg(@RequestBody MoiraiUserOrgExtend var1) throws SystemException;

    /**
     * 2.23 获取分页授权用户组织机构
     *
     * @param var1
     * @return
     */
    @RequestMapping(
            value = {"/user/getUserOrgPage"},
            method = {RequestMethod.POST},
            consumes = {"application/json;charset=UTF-8"}
    )
    BWJsonResult<MoiraiUserOrgExtend> getUserOrgPage(@RequestBody MoiraiUserCondition var1);

    /**
     * 根据查询条件查询用户信息
     * @param moiraiUserCondition
     * @return
     */
    @RequestMapping(value = "/user/findUserByCondition", method = RequestMethod.POST)
    BWJsonResult<MoiraiUser> findUserByCondition(@RequestBody MoiraiUserCondition moiraiUserCondition);

    /**
     * 根据查询条件查询用列表信息
     * @param moiraiUserCondition
     * @return
     */
    @RequestMapping(value = "/user/findUserListByCondition", method = RequestMethod.POST)
    BWJsonResult<MoiraiUser> findUserListByCondition(@RequestBody MoiraiUserCondition moiraiUserCondition);

}
