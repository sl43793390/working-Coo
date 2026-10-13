package com.sl.entity;

import java.io.Serializable;
import java.util.Date;

public class Institution implements Serializable {
    private String idInstitution;//������ʶ

    private String addrUrl;//

    private String nameInstitution;//��������

    private String site;//

    private String holder;//

    private Date timestamp;//ʱ���

    private static final long serialVersionUID = 1L;

    public String getIdInstitution() {
        return idInstitution;
    }

    public void setIdInstitution(String idInstitution) {
        this.idInstitution = idInstitution == null ? null : idInstitution.trim();
    }

    public String getAddrUrl() {
        return addrUrl;
    }

    public void setAddrUrl(String addrUrl) {
        this.addrUrl = addrUrl == null ? null : addrUrl.trim();
    }

    public String getNameInstitution() {
        return nameInstitution;
    }

    public void setNameInstitution(String nameInstitution) {
        this.nameInstitution = nameInstitution == null ? null : nameInstitution.trim();
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site == null ? null : site.trim();
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder == null ? null : holder.trim();
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}