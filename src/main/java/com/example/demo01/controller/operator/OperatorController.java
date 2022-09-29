package com.example.demo01.controller.operator;

import com.alibaba.fastjson.JSONObject;
import com.example.demo01.common.Keys;
import com.example.demo01.entity.operatorModel.OperatorResponse;
import com.example.demo01.entity.operatorModel.OrderModel;
import com.example.demo01.utils.HttpHeaderUtil;
import com.example.demo01.utils.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

@RestController
@RequestMapping("xiuzhi/bj_mobile/MsgSync/oc/v1")
public class OperatorController {
    //YD运营公钥
    private static final String publicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0bL+oIFm9AhGJmvOlL8jn5XBkpGucfe1o2+1kvw7Npwq0Amb5fHoGnurtF+pwLm4uEuTIe98dq/d7v4ykjS39ISesrhkNw+UB/UpqoL4D50O5gqTNxOrLFyIN4BxdrxLA9sWBfQF6aqLhXDN5Uzf8Ibc+H2MjkF7rycPl2Xxckzabr5201rH91Tz4jZXdqdVO//8mbmoaOfTY0UR/VJcNXOfFKOLnLXBAbcusDfsC+JjyYXbSD55lST32jUwxYS5SzLrTfuj0RFEGAbDqA2g4sN2NZP+NuomPc6K7X9eLr6FGnT7HdMNNRxbQK0kqt3WlGL+cw4xMyDt8YQsTc0YcQIDAQAB";

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    HttpHeaderUtil httpHeaderUtil;

    @Autowired
    Keys keys;

    /**
     * 订购信息
     * @param orderModel
     * @param request
     * @return
     */
    @PostMapping("syncproduct")
    public OperatorResponse getOrderInfo(@RequestBody OrderModel orderModel, HttpServletRequest request){
//    public OperatorResponse getOrderInfo(HttpServletRequest request){
        //使用过滤器鉴权，下面代码可以去掉
        //鉴权
        boolean b = verify(request);
        if(!b){
            return OperatorResponse.error().resultDesc("鉴权失败");
        }
        if(orderModel==null){
            return OperatorResponse.error().resultCode("00001").resultDesc("参数非法");
        }
        //TODO 请求体解密
        BufferedReader reader = null;
        try {
            reader=request.getReader();
            StringBuilder s=new StringBuilder();
            String s1=null;
            while ((s1=reader.readLine())!=null){
                s.append(s1);
            }
            String dencrypt = RSAUtils.dencrypt(s.toString(), keys.getXzcspPrivateKey());
            System.out.println("解密报文："+dencrypt);
            OrderModel order = JSONObject.parseObject(dencrypt, OrderModel.class);
            System.out.println("解密后:"+order.toString());

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        //TODO 存日志，存数据，同步数据
        return OperatorResponse.ok();
    }

    /**
     * chatbot状态变更
     * @return
     */
    @PostMapping("status")
    public OperatorResponse statusChange(){
        return OperatorResponse.ok();
    }

    public boolean verify(HttpServletRequest request){
        String authorization = request.getHeader("Authorization").split(" ")[1];
        String timestamp = request.getHeader("Timestamp");
        String requestId = request.getHeader("Request-ID");
        String appId = request.getHeader("App-ID");
        //通过appid找到token，先写死测试，后面可以使用名单这种方式
        String token="b4e10cf1b467e25247400a454c5099971448aeea798921dd94524af25224ba82";
        String sign=token+timestamp+requestId;
        boolean b = RSAUtils.verifySign(publicKey, sign, authorization);
        return b;
    }

    @RequestMapping("test")
    public void test(@RequestBody OrderModel orderModel){
        try {
            String s = JSONObject.toJSONString(orderModel);
            System.out.println("明文："+s);
            JSONObject jsonObject = JSONObject.parseObject(s);
            String encrypt = RSAUtils.encrypt(jsonObject.toString(), keys.getXzcspPublicKey());
            System.out.println("加密报文："+encrypt);
            restTemplate.postForEntity("http://localhost:8888/xiuzhi/bj_mobile/MsgSync/oc/v1/syncproduct",encrypt,String.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
