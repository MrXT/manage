package com.project.common.controller;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import springfox.documentation.annotations.ApiIgnore;

import com.pingplusplus.Pingpp;
import com.pingplusplus.exception.APIConnectionException;
import com.pingplusplus.exception.APIException;
import com.pingplusplus.exception.AuthenticationException;
import com.pingplusplus.exception.ChannelException;
import com.pingplusplus.exception.InvalidRequestException;
import com.pingplusplus.model.Charge;

/**
 * ping++支付controller ClassName: PingController <br/>
 * @author xt(di.xiao@karakal.com.cn)
 * @date 2017年2月6日
 * @version 1.0
 */
@RequestMapping("ping")
@Controller
@ApiIgnore
public class PingController {

    @Value("${pingApiKey}")
    private String apiKey;

    @Value("${pingAppId}")
    private String appId;

    @Value("${pingPrivateKeyPath}")
    private String privateKeyPath = "src/main/resources/rsa_private_key.pem";

    @RequestMapping("charge")
    @ResponseBody
    public Object charge() {
        Pingpp.apiKey = apiKey;
        Pingpp.privateKeyPath = privateKeyPath;
        Charge charge = null;
        Map<String, Object> chargeMap = new HashMap<String, Object>();
        chargeMap.put("amount", 100);// 订单总金额, 人民币单位：分（如订单总金额为 1 元，此处请填 100）
        chargeMap.put("currency", "cny");
        chargeMap.put("subject", "Your Subject");
        chargeMap.put("body", "Your Body");
        String orderNo = new Date().getTime() + randomString(7);
        chargeMap.put("order_no", orderNo);// 推荐使用 8-20 位，要求数字或字母，不允许其他字符
        chargeMap.put("channel", "upacp_pc");// 支付使用的第三方支付渠道取值，请参考：https://www.pingxx.com/api#api-c-new
        chargeMap.put("client_ip", "127.0.0.1"); // 发起支付请求客户端的 IP 地址，格式为 IPV4，如:
                                                 // 127.0.0.1
        Map<String, String> app = new HashMap<String, String>();
        app.put("id", appId);
        chargeMap.put("app", app);

        Map<String, Object> extra = new HashMap<String, Object>();
         extra.put("result_url", "https://127.0.0.1:8443/index");
        chargeMap.put("extra", extra);
        try {
            // 发起交易请求
            charge = Charge.create(chargeMap);
            // 传到客户端请先转成字符串 .toString(), 调该方法，会自动转成正确的 JSON 字符串
            String chargeString = charge.toString();
            System.out.println(chargeString);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (ChannelException e) {
            e.printStackTrace();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (APIException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        }
        return charge;
    }

    private static SecureRandom random = new SecureRandom();

    public static String randomString(int length) {
        String str = new BigInteger(130, random).toString(32);
        return str.substring(0, length);
    }
}
