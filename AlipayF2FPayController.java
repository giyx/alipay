package com.sdnuode.shoppingmanager.modules.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradePayModel;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.sdnuode.shoppingmanager.modules.config.AlipayProperties;
import com.sdnuode.shoppingmanager.modules.entity.OrderWatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @Author: jkjk
 * @Date: 2019/4/14 0014 10:09
 */
@Slf4j
@RestController
public class AlipayF2FPayController {
    @Autowired
    private AlipayClient alipayClient;

    @Autowired
    private AlipayProperties aliPayProperties;

    @Autowired
    private HttpServletResponse response;

    @GetMapping("/alipay/page/gotoPayPage")
    public void gotoPayPage(@RequestParam("carte") String carte,@RequestParam("sale") String sale) throws AlipayApiException, IOException {
        // 订单模型
        String productCode = "FAST_INSTANT_TRADE_PAY";
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(UUID.randomUUID().toString());
        model.setSubject("订单支付");
        model.setTotalAmount(sale);
        model.setBody(carte);
        model.setProductCode(productCode);

        AlipayTradePagePayRequest pagePayRequest = new AlipayTradePagePayRequest();
        pagePayRequest.setReturnUrl(aliPayProperties.getReturnUrl());
        pagePayRequest.setNotifyUrl(aliPayProperties.getNotifyUrl());
        pagePayRequest.setBizModel(model);

        // 调用SDK生成表单, 并直接将完整的表单html输出到页面
        String form = alipayClient.pageExecute(pagePayRequest).getBody();
        response.setContentType("text/html;charset=" + aliPayProperties.getCharset());
        response.getWriter().write(form);
        response.getWriter().flush();
        response.getWriter().close();
    }

    /**
     * 当面付-扫码付
     * <p>
     * 扫码支付，指用户打开支付宝钱包中的“扫一扫”功能，扫描商户针对每个订单实时生成的订单二维码，并在手机端确认支付。
     * <p>
     * 发起预下单请求，同步返回订单二维码
     * <p>
     * 适用场景：商家获取二维码展示在屏幕上，然后用户去扫描屏幕上的二维码
     *
     * @return
     * @throws AlipayApiException
     */
    @PostMapping("/alipay/f2fpay/precreate")
    public void precreate() throws Exception {
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        AlipayTradePayModel model = new AlipayTradePayModel();
        request.setBizModel(model);

        model.setOutTradeNo(System.currentTimeMillis() + "");
        model.setSubject("Iphone6 16G");
        model.setTotalAmount("0.01");
        model.setAuthCode("xxxxx");//沙箱钱包中的付款码
        model.setScene("bar_code");

        AlipayTradePayResponse response = null;
        try {
            response = alipayClient.execute(request);
            System.out.println(response.getBody());
            System.out.println(response.getTradeNo());
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }
}
