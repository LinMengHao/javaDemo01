package com.example.demo01.entity.xmlToBean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "file")
public class Multimedia implements Serializable {

    @JsonIgnore
    private String id;

    @JsonIgnore
    private String authstatus;

    @JsonIgnore
    private String tid;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "file-info")
    List<FileInfos>fileInfos=new ArrayList<>();

    public List<FileInfos> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(List<FileInfos> fileInfos) {
        this.fileInfos = fileInfos;
    }

    public Multimedia() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
}
