package com.example.demo01.entity.operatorModel;

public class OperatorResponse {
    //数据同步结果，00000表示成功，其它值是失败。
    private String resultCode;
    //数据同步结果描述
    private String resultDesc;

    public OperatorResponse resultCode(String code){
        this.resultCode=code;
        return this;
    }
    public OperatorResponse resultDesc(String desc){
        this.resultDesc=desc;
        return this;
    }
    public static OperatorResponse ok(){
        OperatorResponse response=new OperatorResponse();
        response.setResultCode("00000");
        response.setResultDesc("成功");
        return response;
    }

    public static OperatorResponse error(){
        OperatorResponse response=new OperatorResponse();
        response.setResultCode("00001");
        response.setResultDesc("失败");
        return response;
    }

    private OperatorResponse() {
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }
}
