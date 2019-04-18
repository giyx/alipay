package com.sdnuode.shoppingmanager.modules.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: jkjk
 * @Date: 2019/4/14 0014 10:07
 */
@Configuration
@EnableConfigurationProperties(AlipayProperties.class)
public class AlipayConfig {
    @Autowired
    private AlipayProperties properties;

    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(properties.getGatewayUrl(), properties.getAppid(), properties.getPrivateKey(), properties.getFormate(), properties.getCharset(), properties.getAlipayPublicKey(), properties.getSignType());
    }
}
