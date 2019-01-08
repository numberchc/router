package com.baiwang.cloud.integration.impl;

import com.baiwang.cloud.common.exception.SystemException;
import com.baiwang.cloud.integration.IMoiraiClient;
import com.baiwang.moirai.model.common.BWJsonResult;
import com.baiwang.moirai.model.org.MoiraiOrg;
import com.baiwang.moirai.model.org.MoiraiOrgCondition;
import com.baiwang.moirai.model.user.MoiraiUser;
import com.baiwang.moirai.model.user.MoiraiUserCondition;
import com.baiwang.moirai.model.user.MoiraiUserOrgExtend;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author guoting
 * @Title: MoiraiOrgClientHystrix
 * @ProjectName business-rule
 * @Description:
 * @date 2018/8/21上午11:32
 */
@Component
public class MoiraiClientHystrix implements FallbackFactory<IMoiraiClient> {
    private static final Logger logger = LoggerFactory.getLogger(MoiraiClientHystrix.class);

    @Override
    public IMoiraiClient create(Throwable cause) {
        logger.info("fallback; reason was: {}", cause);
        return new IMoiraiClient() {

            @Override
            public BWJsonResult<MoiraiOrg> getTaxEntityList(@RequestBody MoiraiOrgCondition var1) throws SystemException {
                logger.error("接口出现异常，参数{}", var1);
                BWJsonResult<MoiraiOrg> execResult = new BWJsonResult(new Exception());
                return execResult;
            }

            @Override
            public BWJsonResult<MoiraiOrg> getOrgTaxTypeList(@RequestBody MoiraiOrg var1) throws SystemException {
                logger.error("接口出现异常");
                BWJsonResult<MoiraiOrg> execResult = new BWJsonResult(new Exception());
                return execResult;
            }

            @Override
            public BWJsonResult<MoiraiOrg> getOrgListByCondition(@RequestBody Map<String, List> var1) throws SystemException {
                logger.error("MoiraiOrgClientHystrix.getOrgListByCondition 用户中心接口,通过组织机构ID获取机构信息异常,接口入参{}", var1);
                List<MoiraiOrg> list = new ArrayList<>();
                MoiraiOrg org = new MoiraiOrg();
                list.add(org);
                BWJsonResult<MoiraiOrg> execResult = new BWJsonResult(list);
                execResult.setSuccess(false);
                execResult.setMessage("进入接口断路器 MoiraiOrgClientHystrix.getOrgListByCondition");
                return execResult;
            }

            @Override
            public BWJsonResult<MoiraiOrg> getUserOrg(@RequestBody MoiraiUserOrgExtend var1) throws SystemException {
                logger.error("接口出现异常，参数{}", var1);
                BWJsonResult<MoiraiOrg> execResult = new BWJsonResult(new Exception());
                return execResult;
            }

            @Override
            public BWJsonResult<MoiraiOrg> getOrgByCondition(@RequestBody MoiraiOrgCondition var1) {
                logger.error("接口出现异常，参数{}", var1);
                BWJsonResult<MoiraiOrg> execResult = new BWJsonResult(new Exception());
                return execResult;
            }

            @Override
            public BWJsonResult<MoiraiOrg> getOrgTaxEntity(@RequestBody Map<String, Object> var1) {
                logger.error("接口出现异常，参数{}", var1);
                BWJsonResult<MoiraiOrg> execResult = new BWJsonResult(new Exception());
                return execResult;
            }

            @Override
            public BWJsonResult<MoiraiUserOrgExtend> getUserOrgPage(MoiraiUserCondition var1) {
                logger.error("接口出现异常，参数{}", var1);
                BWJsonResult<MoiraiUserOrgExtend> execResult = new BWJsonResult(new Exception());
                return execResult;
            }

            @Override
            public BWJsonResult<MoiraiUser> findUserByCondition(@RequestBody MoiraiUserCondition moiraiUserCondition) {
                logger.error("用户列表查询,用户查询断路器 ——调用用户查询接口出现异常");
                BWJsonResult<MoiraiUser> execResult = new BWJsonResult(new Exception());
                return execResult;
            }

            @Override
            public BWJsonResult<MoiraiUser> findUserListByCondition(@RequestBody MoiraiUserCondition moiraiUserCondition) {
                logger.error("用户列表查询,用户查询断路器 ——调用用户查询接口出现异常");
                BWJsonResult<MoiraiUser> execResult = new BWJsonResult(new Exception());
                return execResult;
            }

        };

    }
}
