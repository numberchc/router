package com.baiwang.cloud.service.util;

import com.baiwang.cloud.common.constants.enums.OrgTypeEnum;
import com.baiwang.cloud.common.utils.StringUtil;
import com.baiwang.cloud.integration.IMoiraiClient;
import com.baiwang.moirai.model.common.BWJsonResult;
import com.baiwang.moirai.model.org.MoiraiOrg;
import com.baiwang.moirai.model.org.MoiraiOrgCondition;
import com.baiwang.moirai.model.user.MoiraiUser;
import com.baiwang.moirai.model.user.MoiraiUserCondition;
import com.baiwang.moirai.model.user.MoiraiUserOrgExtend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * 用户中心工具类
 *
 * @Author chenhuichang
 * @Date 2018/8/4 下午5:15
 */
@Component
public class MoiraiUtil {
    private static Logger logger = LoggerFactory.getLogger(MoiraiUtil.class);

    private static IMoiraiClient moiraiService;

    @Autowired
    private IMoiraiClient moiraiClient;

    //静态注入
    @PostConstruct
    public void beforeInit() {
        moiraiService = moiraiClient;
    }

    /**
     * 产品id
     */
    public static List<Long> productId = Arrays.asList(11L);

    private static MoiraiOrg getOrg(String tenantId, String orgId) {
        BWJsonResult<MoiraiOrg> orgResult;
        try {
            MoiraiOrgCondition moiraiOrgCondition = new MoiraiOrgCondition();
            if (StringUtil.isNotEmpty(tenantId)) {
                moiraiOrgCondition.setTenantId(Long.valueOf(tenantId));
            }
            if (StringUtil.isNotEmpty(orgId)) {
                moiraiOrgCondition.setOrgId(Long.valueOf(orgId));
            }
            logger.info("请求接口：{}，参数：{}", "getOrgByCondition", moiraiOrgCondition);
            Long startTime = System.currentTimeMillis();
            orgResult = moiraiService.getOrgByCondition(moiraiOrgCondition);
            orgResult = assertMoiraiOrg(orgResult);
            logger.info("请求结束，耗时：{}ms，结果：{}", System.currentTimeMillis() - startTime, orgResult);
        } catch (Exception e) {
            logger.error("获取当前组织机构信息异常：{}", e);
            return null;
        }
        if (orgResult != null && orgResult.getData() != null && orgResult.getData().size() > 0) {
            return orgResult.getData().get(0);
        } else {
            logger.info("获取当前组织机构信息为空");
            return null;
        }
    }

    /**
     * 根据条件查询组织机构<br>
     *
     * @return
     */
    public static List<MoiraiOrg> getOrgListByCondition(List<String> orgIdList) {
        if (orgIdList != null) {
            BWJsonResult<MoiraiOrg> orgResult;
            try {
                Map<String, List> param = new HashMap();
                param.put("orgId", orgIdList);
                logger.info("请求接口：{}，参数：{}", "getOrgListByCondition", param);
                Long startTime = System.currentTimeMillis();
                orgResult = moiraiService.getOrgListByCondition(param);
                orgResult = assertMoiraiOrg(orgResult);
                logger.info("请求结束，耗时：{}ms，结果：{}", System.currentTimeMillis() - startTime, orgResult);
            } catch (Exception e) {
                logger.error("获取纳税主体信息异常：{}", e);
                return null;
            }
            if (orgResult != null && orgResult.getData() != null && orgResult.getData().size() > 0) {
                return orgResult.getData();
            } else {
                logger.info("获取纳税主体信息为空");
            }
        }
        return null;
    }

    /**
     * 查询组织机构的纳税主体<br>
     * 如果当前是非纳税主体，则查询上级纳税主体
     */
    public static MoiraiOrg getOrgTax(String tenatId, String orgId) {
        //获取组织信息
        MoiraiOrgCondition mo = new MoiraiOrgCondition();
        if (StringUtil.isNotEmpty(tenatId)) {
            mo.setTenantId(Long.valueOf(tenatId));
        }
        if (StringUtil.isEmpty(orgId)) {
            return null;
        }
        MoiraiOrg org = getOrg(tenatId, orgId);
        //如果当前登录者是非纳税主体
        if (OrgTypeEnum.ORG.getIntCode() == org.getOrgType() && !"0".equals(org.getParentOrg() + "")) {
            return getOrgTaxEntity(orgId);
        }
        return org;
    }

    /**
     * 查询组织机构的纳税主体
     */
    public static MoiraiOrg getOrgTaxEntity(String orgId) {
        if (StringUtil.isEmpty(orgId)) {
            logger.info("查询组织机构的纳税主体时，传入orgId不能为空");
            return null;
        }
        //获取上级的纳税主体信息
        Map<String, Object> condition = new HashMap<>();
        condition.put("orgId", orgId);
        logger.info("请求接口：{}，参数：{}", "getOrgTaxEntity", condition);
        Long startTime = System.currentTimeMillis();
        BWJsonResult<MoiraiOrg> orgResult = moiraiService.getOrgTaxEntity(condition);
        orgResult = assertMoiraiOrg(orgResult);
        logger.info("请求结束，耗时：{}ms，结果：{}", System.currentTimeMillis() - startTime, orgResult);
        if (orgResult != null && orgResult.getData().size() > 0) {
            logger.info("查询到的纳税主体为：{}", orgResult.getData().get(0));
            return orgResult.getData().get(0);
        } else {
            logger.info("未查询到的纳税主体");
            return null;
        }
    }

    /**
     * 获取所有开通产品模块的税号
     *
     * @return
     */
    public static List<MoiraiOrg> getAllRiskTaxEntityList() {
        MoiraiOrgCondition moiraiOrgCondition = new MoiraiOrgCondition();
        // 需要增加分页参数，不传的话默认页大小是20
        moiraiOrgCondition.setProductCon(productId);
        BWJsonResult<MoiraiOrg> orgResult = moiraiService.getTaxEntityList(moiraiOrgCondition);
        orgResult = assertMoiraiOrg(orgResult);
        if (orgResult != null && orgResult.getData() != null && orgResult.getData().size() > 0) {
            return orgResult.getData();
        } else {
            logger.info("获取开通风控产品的信息为空");
            return null;
        }
    }

    public static List<MoiraiOrg> getUserOrgPageAll(String tenantId, String orgId, String userId) {
        return getUserOrgPage(tenantId, orgId, userId, OrgTypeEnum.ALL.getIntCode());// orgType，0代表查询全部
    }

    /**
     * 获取授权用户组织机构<br>
     * 可以获取指定组织机构类型数据
     *
     * @param orgType 1：纳税主体、2：非纳税主体。0代表全部
     * @return
     */
    public static List<MoiraiOrg> getUserOrgPage(String tenantId, String orgId, String userId, int orgType) {
        List<MoiraiOrg> userOrg = getUserOrgPage(tenantId, orgId, userId);
        if (userOrg == null || userOrg.size() == 0) {
            return null;
        }
        Iterator<MoiraiOrg> iterator = userOrg.iterator();
        if (orgType != 0) {
            while (iterator.hasNext()) {
                MoiraiOrg moiraiUserOrg = iterator.next();
                if (moiraiUserOrg.getOrgType() != orgType) {
                    iterator.remove();
                }
            }
        }
        return userOrg;
    }

//    private static void addMoiraiOrgList(List<MoiraiOrg> moiraiOrgList, MoiraiUserOrgExtend moiraiUserOrg, int curOrgType) {
//        MoiraiOrg moiraiOrg = new MoiraiOrg();
//        moiraiOrg.setTenantId(moiraiUserOrg.getTenantId());
//        moiraiOrg.setTaxCode(moiraiUserOrg.getTaxCode());
//        moiraiOrg.setOrgId(moiraiUserOrg.getOrgId());
//        moiraiOrg.setOrgName(moiraiUserOrg.getOrgName());
//        OrgTypeEnum orgTypeEnum = OrgTypeEnum.getByCode(curOrgType);
//        if (orgTypeEnum == null) {// 传入的值取不到则根据税号是否为空判断是否为纳税主体
//            if (StringUtil.isNotEmpty(moiraiUserOrg.getTaxCode())) {// 没有税号数据的是非纳税主体
//                orgTypeEnum = OrgTypeEnum.TAX;
//            } else {
//                orgTypeEnum = OrgTypeEnum.ORG;
//            }
//        }
//        moiraiOrg.setOrgType(orgTypeEnum.getIntCode());
//        moiraiOrgList.add(moiraiOrg);
//    }

    /**
     * 获取授权用户组织机构
     *
     * @return
     */
    public static List<MoiraiOrg> getUserOrgPage(String tenantId, String orgId, String userId) {
        if (StringUtil.isEmpty(tenantId) || StringUtil.isEmpty(orgId) || StringUtil.isEmpty(userId)) {
            return null;
        }
        MoiraiUserCondition condition = new MoiraiUserCondition();
        condition.setTenantId(Long.valueOf(tenantId));
        condition.setOrgId(Long.valueOf(orgId));
        condition.setUserId(Long.valueOf(userId));
//        condition.setPageNum(1);
//        condition.setPageSize(1000);
        logger.info("请求接口：{}，参数：{}", "getUserOrgPage", condition);
        Long startTime = System.currentTimeMillis();
        BWJsonResult<MoiraiUserOrgExtend> userOrgResult = moiraiService.getUserOrgPage(condition);
        userOrgResult = assertMoiraiUserOrg(userOrgResult);
        logger.info("请求结束，耗时：{}ms，结果：{}", System.currentTimeMillis() - startTime, userOrgResult);
        if (userOrgResult != null && userOrgResult.getData() != null && userOrgResult.getData().size() > 0) {
            List<MoiraiUserOrgExtend> data = userOrgResult.getData();
            logger.info("查询到的授权用户组织机构：{}", data);
            List<String> orgIds = new ArrayList<>();
            for (MoiraiUserOrgExtend orgExtend : data) {
                orgIds.add(orgExtend.getOrgId().toString());
            }
            List<MoiraiOrg> orgListByCondition = getOrgListByCondition(orgIds);
            return orgListByCondition;
        } else {
            logger.info("查询到的授权用户组织机构为空");
            return null;
        }
    }

    /**
     * 获取授权用户组织机构
     *
     * @return
     */
    public static List<MoiraiUserOrgExtend> getUserOrgs(String tenantId, String orgId, String userId) {
        if (StringUtil.isEmpty(tenantId) || StringUtil.isEmpty(orgId) || StringUtil.isEmpty(userId)) {
            return null;
        }
        MoiraiUserCondition condition = new MoiraiUserCondition();
        condition.setTenantId(Long.valueOf(tenantId));
        condition.setOrgId(Long.valueOf(orgId));
        condition.setUserId(Long.valueOf(userId));
//        condition.setPageNum(1);
//        condition.setPageSize(1000);
        logger.info("请求接口：{}，参数：{}", "getUserOrgPage", condition);
        Long startTime = System.currentTimeMillis();
        BWJsonResult<MoiraiUserOrgExtend> userOrgResult = moiraiService.getUserOrgPage(condition);
        userOrgResult = assertMoiraiUserOrg(userOrgResult);
        logger.info("请求结束，耗时：{}ms，结果：{}", System.currentTimeMillis() - startTime, userOrgResult);
        if (userOrgResult != null) {
            return userOrgResult.getData();
        } else {
            logger.info("查询到的授权用户组织机构为空");
            return null;
        }
    }

    /**
     * 断言结果，防止报转换错误<br>
     * 有结果，并且结果类型正确，将原值返回。否则，返回null
     *
     * @param bwJsonResult
     * @return
     */
    public static BWJsonResult<MoiraiOrg> assertMoiraiOrg(BWJsonResult bwJsonResult) {
        if (bwJsonResult != null && bwJsonResult.isSuccess()
                && bwJsonResult.getTotal() > 0 && bwJsonResult.getData() != null
                && bwJsonResult.getData().size() > 0 && bwJsonResult.getData().get(0) instanceof MoiraiOrg) {
            return bwJsonResult;
        }
        return null;
    }

    /**
     * 断言结果，防止报转换错误<br>
     * 有结果，并且结果类型正确，将原值返回。否则，返回null
     *
     * @param bwJsonResult
     * @return
     */
    public static BWJsonResult<MoiraiUserOrgExtend> assertMoiraiUserOrg(BWJsonResult bwJsonResult) {
        if (bwJsonResult != null && bwJsonResult.isSuccess()
                && bwJsonResult.getTotal() > 0 && bwJsonResult.getData() != null
                && bwJsonResult.getData().size() > 0 && bwJsonResult.getData().get(0) instanceof MoiraiUserOrgExtend) {
            return bwJsonResult;
        }
        return null;
    }

    /**
     * 断言结果，防止报转换错误<br>
     * 有结果，并且结果类型正确，将原值返回。否则，返回null
     *
     * @param bwJsonResult
     * @return
     */
    public static MoiraiUser assertMoiraiUser(BWJsonResult bwJsonResult) {
        if (bwJsonResult != null && bwJsonResult.isSuccess()
                && bwJsonResult.getTotal() > 0 && bwJsonResult.getData() != null
                && bwJsonResult.getData().size() > 0 && bwJsonResult.getData().get(0) instanceof MoiraiUser) {
            return (MoiraiUser) bwJsonResult.getData().get(0);
        }
        return null;
    }

    /**
     * 根据用户id获取用户信息
     *
     * @param userId
     * @return
     */
    public static MoiraiUser getUserById(String userId) {
        if (StringUtil.isEmpty(userId)) {
            return null;
        }
        MoiraiUserCondition condition = new MoiraiUserCondition();
        condition.setUserId(Long.parseLong(userId));
        logger.info("请求接口：{}，参数：{}", "findUserByCondition", condition);
        Long startTime = System.currentTimeMillis();
        BWJsonResult<MoiraiUser> user = moiraiService.findUserByCondition(condition);
        MoiraiUser moiraiUser = assertMoiraiUser(user);
        logger.info("请求结束，耗时：{}ms，结果：{}", System.currentTimeMillis() - startTime, user);
        if (moiraiUser != null) {
            return moiraiUser;
        }
        logger.info("查询到的授权用户组织机构为空");
        return null;
    }

}
