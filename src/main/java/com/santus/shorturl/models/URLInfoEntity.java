package com.santus.shorturl.models;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;

import javax.json.JsonObject;
import javax.persistence.*;
import javax.ws.rs.ext.ParamConverter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.simple.*;
@Entity(name="url_info")
public class URLInfoEntity {
    static final String url ="localhost:8080";
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private Date created;
    @Column
    private String original_url;
    @Column
    private String short_url;

    @OneToMany(mappedBy = "url_info")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<RedirectInfoEntity> redirects= new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getOriginal_url() {
        if(original_url.contains("http://") || original_url.contains("https://")) {
            return original_url;
        }
        return "http://" + original_url;
    }

    public void setOriginal_url(String original_url) {
        this.original_url = original_url;
    }

    public String getShort_url() {
        return short_url;
    }

    public void setShort_url(String short_url) {
        this.short_url = short_url;
    }

    public List<RedirectInfoEntity> getRedirects() {
        return redirects;
    }

    public void setRedirects(List<RedirectInfoEntity> redirects) {
        this.redirects = redirects;
    }

    public static JSONArray listUrlToJson(List<URLInfoEntity> urlInfoEntities) {
        JSONArray mainObject = new JSONArray();
        for(int i = 0;i < urlInfoEntities.size();i++) {
            JSONObject child = new JSONObject();
            child.put("original", urlInfoEntities.get(i).getOriginal_url());
            child.put("short", urlInfoEntities.get(i).getShort_url());
            child.put("created", urlInfoEntities.get(i).getCreated().toString());
            mainObject.add(child);
        }
        return mainObject;
    }

    public void generateShortUrl(int count) {
        this.short_url = new Integer(count + 1).toString();
    }


    public JSONObject getJson() {
        JSONObject main = new JSONObject();
        main.put("original", getOriginal_url());
        main.put("short",url + "/" + getShort_url());
        return main;
    }


}
