package com.santus.shorturl.dao;

import com.santus.shorturl.models.RedirectInfoEntity;
import com.santus.shorturl.models.Statistic;
import com.santus.shorturl.models.URLInfoEntity;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.List;

public class URLInfoDao implements IURLInfoDao{

    Session session;

    public URLInfoDao(Session session) {
        this.session = session;
    }
    @Override
    public void addURLInfo(URLInfoEntity urlInfoEntity) {
        session.beginTransaction();
        session.save(urlInfoEntity);
        session.getTransaction().commit();
    }

    @Override
    public URLInfoEntity getURLInfo(String shortURL) {
        Criteria criteria = session.createCriteria(URLInfoEntity.class).add(Restrictions.eq("short_url",shortURL));
        URLInfoEntity urlInfoEntity = (URLInfoEntity) criteria.uniqueResult();
        return urlInfoEntity;
    }

    @Override
    public void deleteURLInfo(String shortURL) {
        session.beginTransaction();
        Criteria criteria = session.createCriteria(URLInfoEntity.class).add(Restrictions.eq("short_url", shortURL));
        session.delete((URLInfoEntity)criteria.uniqueResult());
        session.getTransaction().commit();
    }

    @Override
    public List<URLInfoEntity> getListUrlInfo(int min, int count) {
        Criteria criteria = session.createCriteria(URLInfoEntity.class).setFirstResult(min).setMaxResults(count);
        return criteria.list();
    }

    @Override
    public int getSizeUrlInfo() {
        Criteria criteria = session.createCriteria(URLInfoEntity.class);
        return criteria.list().size();
    }

    @Override
    public boolean hasUrlInfo(String shortURL) {
        Criteria criteria = session.createCriteria(URLInfoEntity.class).add(Restrictions.eq("short_url", shortURL));
        if(criteria.list().size() > 0) return true;
        return false;
    }

    @Override
    public Statistic getViewsStat(URLInfoEntity urlInfoEntity) {
        Criteria criteria = session.createCriteria(URLInfoEntity.class).add(Restrictions.eq("short_url", urlInfoEntity.getShort_url()));
        urlInfoEntity = (URLInfoEntity) criteria.uniqueResult();
        List<RedirectInfoEntity> redirectInfoEntities = urlInfoEntity.getRedirects();

        Statistic statistic = new Statistic();

        statistic.setCreated(urlInfoEntity.getCreated());
        statistic.setViews(redirectInfoEntities.size());
        statistic.setOriginal_url(urlInfoEntity.getOriginal_url());
        statistic.setShort_url(urlInfoEntity.getShort_url());

        List<RedirectInfoEntity> redirectInfoEntitiesUniq = new ArrayList<>();

        boolean hasCopy = true;
        for(int i = 0;i < redirectInfoEntities.size();i++) {
            for(int j = 0; j < redirectInfoEntitiesUniq.size();j++) {
                if(redirectInfoEntities.get(i).getIp().equals(redirectInfoEntitiesUniq.get(j).getIp())) hasCopy = false;
            }
            if(hasCopy == true) redirectInfoEntitiesUniq.add(redirectInfoEntities.get(i));
            hasCopy = true;
        }
        statistic.setUniq_views(redirectInfoEntitiesUniq.size());
        return statistic;
    }


}
