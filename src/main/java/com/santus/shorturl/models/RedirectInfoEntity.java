package com.santus.shorturl.models;

import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Table;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;


@Entity(name = "redirect_info")
public class RedirectInfoEntity {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String ip;


    @Column
    private Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    private URLInfoEntity url_info;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public URLInfoEntity getUrl_info() {
        return url_info;
    }

    public void setUrl_info(URLInfoEntity url_info) {
        this.url_info = url_info;
    }
}
