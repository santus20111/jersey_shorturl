package com.santus.shorturl.dao;

import com.santus.shorturl.models.RedirectInfoEntity;
import com.santus.shorturl.models.Statistic;

public interface IRedirectInfoDao {
    public void addRedirectInfo(RedirectInfoEntity urlInfo);
}
