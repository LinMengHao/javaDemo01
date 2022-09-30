package com.example.demo01.entity.msgModel;

import com.example.demo01.entity.FileInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//媒体审核
public class AuditResponseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    //审核状态
    private String authstatus;
    //文件传输ID
    //媒体文件上传接口生成，用于接收方定位本次媒体审核通知所指定的文件
    private String tid;
    //审核通过的文件信息
    private List<FileInfo> fileInfos=new ArrayList<>();

    public String getAuthstatus() {
        return authstatus;
    }

    public void setAuthstatus(String authstatus) {
        this.authstatus = authstatus;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public List<FileInfo> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(List<FileInfo> fileInfos) {
        this.fileInfos = fileInfos;
    }

    @Override
    public String toString() {
        return "AuditResponseModel{" +
                "authstatus='" + authstatus + '\'' +
                ", tid='" + tid + '\'' +
                ", fileInfos=" + fileInfos +
                '}';
    }
}
