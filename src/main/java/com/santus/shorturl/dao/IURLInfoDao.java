package com.santus.shorturl.dao;

import com.santus.shorturl.models.Statistic;
import com.santus.shorturl.models.URLInfoEntity;

import java.util.List;

public interface IURLInfoDao {
    public void addURLInfo(URLInfoEntity urlInfoEntity);
    public URLInfoEntity getURLInfo(String shortURL);
    public void deleteURLInfo(String shortURL);
    public List<URLInfoEntity> getListUrlInfo(int min,int count);
    public int getSizeUrlInfo();
    public boolean hasUrlInfo(String shortURL);
    public Statistic getViewsStat(URLInfoEntity statistic);

}
