package com.bifrost.aplication.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "platform")

public class Platform {

    @Id
    @Column(name="platform_name")
    private String platformName;

    @Column(name="platform_company")
    private String platformCompany;

    @Column(name="platform_year")
    private int platformYear;

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformCompany() {
        return platformCompany;
    }

    public void setPlatformCompany(String platformCompany) {
        this.platformCompany = platformCompany;
    }

    public int getPlatformYear() {
        return platformYear;
    }

    public void setPlatformYear(int platformYear) {
        this.platformYear = platformYear;
    }



}
