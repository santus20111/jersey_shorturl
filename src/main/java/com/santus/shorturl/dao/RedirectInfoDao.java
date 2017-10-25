package com.santus.shorturl.dao;

import com.santus.shorturl.models.RedirectInfoEntity;
import com.santus.shorturl.models.Statistic;
import org.hibernate.Criteria;
import org.hibernate.Session;

public class RedirectInfoDao implements IRedirectInfoDao{
    Session session;

    public RedirectInfoDao(Session session) {
        this.session = session;
    }
    @Override
    public void addRedirectInfo(RedirectInfoEntity urlInfo) {
        session.beginTransaction();
        session.save(urlInfo);
        session.getTransaction().commit();
    }
}
