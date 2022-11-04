package com.example.demo01.controller.operator;


import com.example.demo01.common.Keys;
import com.example.demo01.common.R;
import com.example.demo01.entity.operatorModel.ChatbotModel;
import com.example.demo01.entity.operatorModel.CustomerModel;
import com.example.demo01.entity.operatorModel.CustomerServiceCodeModel;
import com.example.demo01.entity.operatorModel.OperatorResponse;
import com.example.demo01.utils.Base64Utils;
import com.example.demo01.utils.HttpHeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * csp->运营平台
 */
@Slf4j
@RestController
@RequestMapping("xiuzhi/bj_mobile/operator")
public class OperatorSendController {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    HttpHeaderUtil httpHeaderUtil;

    @Autowired
    Keys keys;





    //==========================================2.7Chatbot接口===================================================

    /**
     * chatbot状态变更(csp->运营平台)
     * @return
     */
    @PostMapping("status")
    public R statusChange(@RequestBody ChatbotModel chatbotModel){

        HttpHeaders header = httpHeaderUtil.getHttpHeaderByRAS();
        header.set("Content-Type",MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<ChatbotModel> entity=new HttpEntity<>(chatbotModel,header);
        ResponseEntity<OperatorResponse> response = restTemplate.postForEntity("http://183.233.87.225:8092/iodd/operation/v1/status", entity, OperatorResponse.class);
        log.info("获取响应码："+response.getStatusCode());
        log.info("响应内容："+response.getBody());
        return R.ok();
    }

    /**
     * CSP平台同步注销Chatbot到运营平台(csp->运营)
     * @param chatbotModel
     * @return
     */
    @PostMapping("cancel")
    public R cancel(@RequestBody ChatbotModel chatbotModel){
        HttpHeaders header = httpHeaderUtil.getHttpHeaderByRAS();
        header.set("Content-Type",MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<ChatbotModel> entity=new HttpEntity<>(chatbotModel,header);
        ResponseEntity<OperatorResponse> response = restTemplate.postForEntity("http://183.233.87.225:8092/iodd/operation/v1/cancel", entity, OperatorResponse.class);
        log.info("获取响应码："+response.getStatusCode());
        log.info("响应内容："+response.getBody());
        return R.ok();
    }

    /**
     * Chatbot固定菜单信息（CSP平台—>运营平台)
     * @param chatbotModel
     * @return
     */
    @PostMapping("syncmenu")
    public R syncmenu(@RequestBody ChatbotModel chatbotModel){
        HttpHeaders header = httpHeaderUtil.getHttpHeaderByRAS();
        header.set("Content-Type",MediaType.APPLICATION_JSON_VALUE);
        //菜单，源json 需要base64加密
        String encode = Base64Utils.encode(chatbotModel.getMenu());
        chatbotModel.setMenu(encode);
        HttpEntity<ChatbotModel> entity=new HttpEntity<>(chatbotModel,header);
        ResponseEntity<OperatorResponse> response = restTemplate.postForEntity("http://183.233.87.225:8092/iodd/operation/v1/syncmenu", entity, OperatorResponse.class);
        log.info("获取响应码："+response.getStatusCode());
        log.info("响应内容："+response.getBody());
        return R.ok();
    }

    /**
     * Chatbot新增（CSP平台—>运营平台）
     * @param chatbotModel
     * @return
     */
    @PostMapping("addChatbot")
    public R addChatbot(@RequestBody ChatbotModel chatbotModel){
        HttpHeaders header = httpHeaderUtil.getHttpHeaderByRAS();
        header.set("Content-Type",MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<ChatbotModel> entity=new HttpEntity<>(chatbotModel,header);
        ResponseEntity<OperatorResponse> response = restTemplate.postForEntity("http://183.233.87.225:8092/iodd/operation/v1/new", entity, OperatorResponse.class);
        log.info("获取响应码："+response.getStatusCode());
        log.info("响应内容："+response.getBody());
        return R.ok();
    }

    /**
     * Chatbot变更（CSP平台—>运营平台）
     * @param chatbotModel
     * @return
     */
    @PostMapping("change")
    public R change(@RequestBody ChatbotModel chatbotModel){
        HttpHeaders header = httpHeaderUtil.getHttpHeaderByRAS();
        header.set("Content-Type",MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<ChatbotModel> entity=new HttpEntity<>(chatbotModel,header);
        ResponseEntity<OperatorResponse> response = restTemplate.postForEntity("http://183.233.87.225:8092/iodd/operation/v1/change", entity, OperatorResponse.class);
        log.info("获取响应码："+response.getStatusCode());
        log.info("响应内容："+response.getBody());
        return R.ok();
    }

    /**
     * Chatbot上架申请（CSP平台—>运营平台）
     * @param chatbotModel
     * @return
     */
    @PostMapping("putAway")
    public R putAway(@RequestBody ChatbotModel chatbotModel){
        HttpHeaders header = httpHeaderUtil.getHttpHeaderByRAS();
        header.set("Content-Type",MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<ChatbotModel> entity=new HttpEntity<>(chatbotModel,header);
        ResponseEntity<OperatorResponse> response = restTemplate.postForEntity("http://183.233.87.225:8092/iodd/operation/v1/putAway", entity, OperatorResponse.class);
        log.info("获取响应码："+response.getStatusCode());
        log.info("响应内容："+response.getBody());
        return R.ok();
    }

    /**
     * 2.7.12Chatbot 重新提交白名单（CSP平台—>运营平台）
     * @param chatbotModel
     * @return
     */
    @PostMapping("resubmitDebugWhite")
    public R resubmitDebugWhite(@RequestBody ChatbotModel chatbotModel){
        HttpHeaders header = httpHeaderUtil.getHttpHeaderByRAS();
        header.set("Content-Type",MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<ChatbotModel> entity=new HttpEntity<>(chatbotModel,header);
        ResponseEntity<OperatorResponse> response = restTemplate.postForEntity("http://183.233.87.225:8092/iodd/operation/v1/resubmitDebugWhite", entity, OperatorResponse.class);
        log.info("获取响应码："+response.getStatusCode());
        log.info("响应内容："+response.getBody());
        return R.ok();
    }


    //============================================================2.8非直签客户接口================================================================


    /**
     * 2.8.1非直签客户新增（CSP平台—>运营平台）
     * @param customerModel
     * @return
     */
    @PostMapping(value = "addCustomer",consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public R addCustomer(@RequestBody CustomerModel customerModel){
        HttpHeaders header = httpHeaderUtil.getHttpHeaderByRAS();
        header.set("Content-Type",MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<CustomerModel> entity=new HttpEntity<>(customerModel,header);
        ResponseEntity<OperatorResponse> response = restTemplate.postForEntity("http://183.233.87.225:8092/iodd/operation/v1/client/new", entity, OperatorResponse.class);
        log.info("获取响应码："+response.getStatusCode());
        log.info("响应内容："+response.getBody());
        return R.ok();
    }

    /**
     * 2.8.2非直签客户变更（CSP平台—>运营平台）
     * @param customerModel
     * @return
     */
    @PostMapping(value = "updateCustomer",consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public R updateCustomer(@RequestBody CustomerModel customerModel){
        HttpHeaders header = httpHeaderUtil.getHttpHeaderByRAS();
        header.set("Content-Type",MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<CustomerModel> entity=new HttpEntity<>(customerModel,header);
        ResponseEntity<OperatorResponse> response = restTemplate.postForEntity("http://183.233.87.225:8092/iodd/operation/v1/client/change", entity, OperatorResponse.class);
        log.info("获取响应码："+response.getStatusCode());
        log.info("响应内容："+response.getBody());
        return R.ok();
    }

    /**
     * 2.8.5代理商分配服务代码（csp平台—>运营平台）
     * @param serviceCodeModel
     * @return
     */
    @PostMapping(value = "allotServiceCode",consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public R allotServiceCode(@RequestBody CustomerServiceCodeModel serviceCodeModel){
        HttpHeaders header = httpHeaderUtil.getHttpHeaderByRAS();
        header.set("Content-Type",MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<CustomerServiceCodeModel> entity=new HttpEntity<>(serviceCodeModel,header);
        ResponseEntity<OperatorResponse> response = restTemplate.postForEntity("http://183.233.87.225:8092/iodd/operation/v1/client/ allotServiceCode", entity, OperatorResponse.class);
        log.info("获取响应码："+response.getStatusCode());
        log.info("响应内容："+response.getBody());
        return R.ok();
    }



    /**
     * 下载运营文件保存本地，并将返回文件本地地址
     * @param url
     * @return
     */
    public String downloadFile(String url){
        String filePath="";
        HttpHeaders header = httpHeaderUtil.getHttpHeaderByRAS();
        HttpEntity<CustomerServiceCodeModel> entity=new HttpEntity<>(header);
        ResponseEntity<byte[]> response = restTemplate.exchange("http://183.233.87.225:8092/iodd/v1/downloadImage?attachFileId="+url, HttpMethod.GET, entity, byte[].class);
        FileOutputStream out=null;
        try{
            byte[] body=response.getBody();
            //获取文件名
            HttpHeaders headers1 = response.getHeaders();
            List<String> list = headers1.get("Content-Disposition");
            String s2 = list.get(0);
            String[] split = s2.split(";");
            String filename=null;
            for (String value : split) {
                if (value.trim().startsWith("filename")) {
                    filename = value.substring(value.indexOf('=') + 1).trim();
                }
                if(StringUtils.hasText(filename)){
                    if(body!=null){
                        String file="./FILE/KING/"+filename;
                        File dest = new File(file);
                        // 检测是否存在目录
                        if (!dest.exists()) {
                            dest.mkdirs();// 新建文件夹
                        }
                        out = new FileOutputStream(dest);
                        out.write(body,0,body.length);
                        out.flush();
                        filePath=file;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return filePath;
    }
}
