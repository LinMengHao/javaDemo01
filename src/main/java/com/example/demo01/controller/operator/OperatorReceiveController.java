package com.example.demo01.controller.operator;

import com.alibaba.fastjson.JSONObject;
import com.example.demo01.common.Keys;
import com.example.demo01.common.R;
import com.example.demo01.conf.HttpsSkipRequestFactory;
import com.example.demo01.entity.operatorModel.*;
import com.example.demo01.utils.HttpHeaderUtil;
import com.example.demo01.utils.RSAUtils;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("xiuzhi/bj_mobile/MsgSync/oc/v1")
public class OperatorReceiveController {
    private static RestTemplate httpsTemplate=new RestTemplate(new HttpsSkipRequestFactory());
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

//===============================================Chatbot接口======================================================================


    /**
     * 订购信息（运营->CSP）
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
     * chatbot状态变更(运营平台->csp)
     * @return
     */
    @PostMapping("status")
    public OperatorResponse statusChange(@RequestBody ChatbotModel chatbotModel,HttpServletRequest request){

        //使用过滤器鉴权，下面代码可以去掉
        //鉴权
        boolean b = verify(request);
        if(!b){
            return OperatorResponse.error().resultDesc("鉴权失败");
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
            ChatbotModel order = JSONObject.parseObject(dencrypt, ChatbotModel.class);
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
     * CSP平台同步注销Chatbot到运营平台(运营->csp)
     */
    @PostMapping("cancel")
    public OperatorResponse cancel(@RequestBody ChatbotModel chatbotModel,HttpServletRequest request){
        //使用过滤器鉴权，下面代码可以去掉
        //鉴权
        boolean b = verify(request);
        if(!b){
            return OperatorResponse.error().resultDesc("鉴权失败");
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
            ChatbotModel order = JSONObject.parseObject(dencrypt, ChatbotModel.class);
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
     * 固定菜单审核结果通知（运营平台—>CSP平台)
     * @param authModel
     * @param request
     * @return
     */
    @PostMapping("authnotification")
    public OperatorResponse authnotification(@RequestBody AuthModel authModel,HttpServletRequest request){
//使用过滤器鉴权，下面代码可以去掉
        //鉴权
        boolean b = verify(request);
        if(!b){
            return OperatorResponse.error().resultDesc("鉴权失败");
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
            AuthModel order = JSONObject.parseObject(dencrypt, AuthModel.class);
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
     * Chatbot审核（运营平台—>CSP平台）
     * @param authModel
     * @param request
     * @return
     */
    @PostMapping("audit")
    public OperatorResponse audit(@RequestBody AuthModel authModel,HttpServletRequest request){
//使用过滤器鉴权，下面代码可以去掉
        //鉴权
        boolean b = verify(request);
        if(!b){
            return OperatorResponse.error().resultDesc("鉴权失败");
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
            AuthModel order = JSONObject.parseObject(dencrypt, AuthModel.class);
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
     * Chatbot 新增/变更配置信息（运营平台—>CSP平台）
     * @param confModel
     * @param request
     * @return
     */
    @PostMapping("syncconfigchatbot")
    public OperatorResponse syncconfigchatbot(@RequestBody ChatbotConfModel confModel,HttpServletRequest request){
//使用过滤器鉴权，下面代码可以去掉
        //鉴权
        boolean b = verify(request);
        if(!b){
            return OperatorResponse.error().resultDesc("鉴权失败");
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
            ChatbotConfModel order = JSONObject.parseObject(dencrypt, ChatbotConfModel.class);
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

//==================================================非直签客户接口===================================================

    /**
     * 2.8.1非直签客户新增（运营平台—>csp平台）
     * @param customerModel
     * @param request
     * @return
     */
    @PostMapping("client/new")
    public OperatorResponse clientNew(@RequestBody CustomerModel customerModel,HttpServletRequest request){

        //使用过滤器鉴权，下面代码可以去掉
        //鉴权
        boolean b = verify(request);
        if(!b){
            return OperatorResponse.error().resultDesc("鉴权失败");
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
            CustomerModel order = JSONObject.parseObject(dencrypt, CustomerModel.class);
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
     * 2.8.2非直签客户新增（CSP平台<—运营平台）
     * @param customerModel
     * @param request
     * @return
     */
    @PostMapping("client/change")
    public OperatorResponse clientChange(@RequestBody CustomerModel customerModel,HttpServletRequest request){

        //使用过滤器鉴权，下面代码可以去掉
        //鉴权
        boolean b = verify(request);
        if(!b){
            return OperatorResponse.error().resultDesc("鉴权失败");
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
            CustomerModel order = JSONObject.parseObject(dencrypt, CustomerModel.class);
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
     * 2.8.3非直签客户审核（运营平台—>CSP平台）
     * @param authModel
     * @param request
     * @return
     */
    @PostMapping("client/audit")
    public OperatorResponse clientAudit(@RequestBody AuthModel authModel,HttpServletRequest request){
//使用过滤器鉴权，下面代码可以去掉
        //鉴权
        boolean b = verify(request);
        if(!b){
            return OperatorResponse.error().resultDesc("鉴权失败");
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
            AuthModel order = JSONObject.parseObject(dencrypt, AuthModel.class);
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
     * 2.8.4非直签客户状态变更（运营平台—>CSP平台）
     * @param customerModel
     * @param request
     * @return
     */
    @PostMapping("client/status")
    public OperatorResponse clientStatusChange(@RequestBody CustomerModel customerModel,HttpServletRequest request){

        //使用过滤器鉴权，下面代码可以去掉
        //鉴权
        boolean b = verify(request);
        if(!b){
            return OperatorResponse.error().resultDesc("鉴权失败");
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
            CustomerModel order = JSONObject.parseObject(dencrypt, CustomerModel.class);
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
     * 2.8.5代理商分配服务代码（运营平台—>CSP平台）
     * @param serviceCodeModel
     * @param request
     * @return
     */
    @PostMapping("client/allotServiceCode")
    public OperatorResponse allotServiceCode(@RequestBody CustomerServiceCodeModel serviceCodeModel,HttpServletRequest request){

        //使用过滤器鉴权，下面代码可以去掉
        //鉴权
        boolean b = verify(request);
        if(!b){
            return OperatorResponse.error().resultDesc("鉴权失败");
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
            CustomerServiceCodeModel order = JSONObject.parseObject(dencrypt, CustomerServiceCodeModel.class);
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
     * 下载文件（运营->csp）
     * @param url
     * @param response
     * @throws IOException
     */
    @GetMapping("downloadFile")
    public void downloadFile(@RequestParam String url, HttpServletResponse response,HttpServletRequest request) throws IOException {
        boolean b = verify(request);
        if(!b){
            return ;
        }
        //TODO 路径问题在过程中总结规律
        String[] split = url.split("/");
        String filename = split[split.length - 1];
        File file=new File(url);
        byte[] bytes = Files.toByteArray(file);
        response.setHeader("Content-Disposition", "form-data; name=\"File\";filename="+filename);
//        response.setContentType("image/jpeg");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.close();
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
