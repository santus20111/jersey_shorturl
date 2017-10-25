package com.santus.shorturl.models;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Date;

public class Statistic extends URLInfoEntity{
    public int views;
    public int uniq_views;

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getUniq_views() {
        return uniq_views;
    }

    public void setUniq_views(int uniq_views) {
        this.uniq_views = uniq_views;
    }


    public JSONArray getStatJson() {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("original", this.getOriginal_url());
        jsonObject.put("short", this.getShort_url());
        jsonObject.put("views", new Integer(this.getViews()).toString());
        jsonObject.put("uniq_views", new Integer(this.getUniq_views()).toString());
        jsonArray.add(jsonObject);
        return jsonArray;
    }
}
