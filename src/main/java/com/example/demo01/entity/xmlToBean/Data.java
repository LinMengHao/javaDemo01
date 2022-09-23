package com.example.demo01.entity.xmlToBean;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.io.Serializable;

public class Data implements Serializable {
    //data标签，属性： url：xxx  until：xxx
    @JacksonXmlProperty(localName = "url",isAttribute = true)
    private String url;
    //data标签，属性： url：xxx  until：xxx
    @JacksonXmlProperty(localName = "until",isAttribute = true)
    private String until;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

    public Data() {
    }
}
