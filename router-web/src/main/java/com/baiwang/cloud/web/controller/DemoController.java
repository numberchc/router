package com.baiwang.cloud.web.controller;

import java.util.List;

import com.baiwang.cloud.common.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baiwang.cloud.common.exception.SystemException;
import com.baiwang.cloud.common.model.BWJsonResult;
import com.baiwang.cloud.common.model.ProductVO;
import com.baiwang.cloud.service.DemoService;

/**
 * ClassName: DemoController <br/>
 * Function: 增加测试controller,用于模板框架演示. <br/>
 * Reason: 模板使用. <br/>
 * date: 2017年10月19日 下午7:08:17 <br/>
 * 模板测试接口，可以删除</br>
 *
 * @author wuyuegang
 * @since JDK 1.8
 */
@RestController
@ComponentScan(basePackages = {"com.baiwang.cloud"})
public class DemoController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DemoService routerService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        return "test";
    }

    @RequestMapping(value = "/testSmart", method = RequestMethod.GET)
    @ResponseBody
    public String testSmart() {
        String result = null;
        try {
            result = HttpUtil.doPost("http://www.baidu.com","");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 此方法用于查询商品信息列表，如果查不到数据，会返回自定义异常 SystemException . <br/>
     * 先查询缓存，如果缓存没有，再查询数据库.<br/>
     *
     * @return
     * @throws SystemException
     * @author wuyuegang
     * @since JDK 1.8
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public BWJsonResult<ProductVO> list()
            throws SystemException {
        long start = System.currentTimeMillis();
        // 输出服务入口日志，用于服务监控统计,必须输出
        logger.info("router list begin request入参:{}", "none");
        // 查询结果
        List<ProductVO> vos = routerService.getProductList();
        // 打印日志
        if (logger.isInfoEnabled()) {
            // 日志标准格式 : 入参:aaa 返回结果:bbb cost:[100]ms 业务标识-success
            logger.info("router list success request入参:{} 返回结果:{} cost {} ms ",
                    "none",
                    vos,
                    (System.currentTimeMillis() - start));
        }
        BWJsonResult<ProductVO> result = new BWJsonResult<ProductVO>(vos);
        return result;
    }

    /**
     * 请按照如下模板填写清楚方法的详细信息 <br/>
     * detail:(这里用一句话描述这个方法的作用). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * TODO(这里描述这个方法的执行流程 – 可选).<br/>
     * TODO(这里描述这个方法的使用方法 – 可选).<br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     *
     * @param itemCode
     * @return
     * @throws SystemException
     * @author wuyuegang
     * @since JDK 1.8
     */
    @RequestMapping(value = "/list/{itemCode}", method = RequestMethod.GET)
    @ResponseBody
    public BWJsonResult<ProductVO> detail(@PathVariable String itemCode)
            throws SystemException {
        long start = System.currentTimeMillis();
        // 输出服务入口日志，用于服务监控统计,必须输出
        logger.info("router detail begin request入参:{}", "none");
        // 查询结果
        ProductVO vo = routerService.getProductByCode(itemCode);
        // 打印日志
        if (logger.isInfoEnabled()) {
            // 日志标准格式 : 入参:aaa 返回结果:bbb cost:[100]ms 业务标识 success
            logger.info("router detail success request入参:{} 返回结果:{} cost {} ms",
                    itemCode,
                    vo.toString(),
                    (System.currentTimeMillis() - start));
        }
        BWJsonResult<ProductVO> result = new BWJsonResult<ProductVO>(vo);
        return result;
    }

}
