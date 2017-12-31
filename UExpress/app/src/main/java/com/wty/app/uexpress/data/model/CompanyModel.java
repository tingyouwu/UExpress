package com.wty.app.uexpress.data.model;

import java.io.Serializable;

/**
 * 快递公司详情
 */
public class CompanyModel implements Serializable {
    private String name;
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
