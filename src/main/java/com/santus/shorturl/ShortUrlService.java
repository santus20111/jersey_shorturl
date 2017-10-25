package com.santus.shorturl;

import com.santus.shorturl.dao.RedirectInfoDao;
import com.santus.shorturl.dao.URLInfoDao;
import com.santus.shorturl.models.RedirectInfoEntity;
import com.santus.shorturl.models.Statistic;
import com.santus.shorturl.models.URLInfoEntity;
import com.santus.shorturl.utils.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.PostRemove;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

/**
 * Created by prmanage on 24.10.2017.
 */
@Path("/")
public class ShortUrlService {
    @GET
    @Path("/stat/json")
    @Produces(MediaType.APPLICATION_JSON)
    public String getStatJson(@QueryParam("short") String shortUrl) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        URLInfoDao urlInfoDao = new URLInfoDao(session);
        if(urlInfoDao.hasUrlInfo(shortUrl)) {
            URLInfoEntity urlInfoEntity = urlInfoDao.getURLInfo(shortUrl);
            Statistic statistic = urlInfoDao.getViewsStat(urlInfoEntity);
            session.close();
            return statistic.getStatJson().toJSONString();
        }
        session.close();
        return "no such link";
    }

    @GET
    @Path("/{short}")
    @Produces(MediaType.TEXT_HTML)
    public Response redirect(@PathParam("short") String shortUrl, @Context org.glassfish.grizzly.http.server.Request request) throws URISyntaxException {
        if(shortUrl.equals("stat")) {
            return getStatHtml();
        }
        if(shortUrl.equals("")) {
            return getIndex();
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        URLInfoDao urlInfoDao = new URLInfoDao(session);
        RedirectInfoDao redirectInfoDao = new RedirectInfoDao(session);
        if(urlInfoDao.hasUrlInfo(shortUrl)) {
            RedirectInfoEntity redirectInfoEntity = new RedirectInfoEntity();
            redirectInfoEntity.setDate(new Date());
            redirectInfoEntity.setUrl_info(urlInfoDao.getURLInfo(shortUrl));
            redirectInfoEntity.setIp(request.getRemoteAddr());
            redirectInfoDao.addRedirectInfo(redirectInfoEntity);
            session.close();
            URI uri = new URI(redirectInfoEntity.getUrl_info().getOriginal_url());
            return Response.temporaryRedirect(uri).build();
        }
        session.close();
        return Response.status(404).entity("no such link").build();
    }

    @GET
    @Path("/stat")
    @Produces(MediaType.TEXT_HTML)
    public Response getStatHtml() {
        Response.ResponseBuilder responseBuilder = Response.ok();
        responseBuilder.entity(new StreamingOutput() {
            @Override
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                Files.copy(Paths.get("src/main/static/html/stat.html"), outputStream);
            }
        });
        return responseBuilder.build();
    }



    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public Response getIndex() {
        Response.ResponseBuilder response = Response.ok();
        response.entity(new StreamingOutput() {
            @Override
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                Files.copy(Paths.get("src/main/static/html/index.html"), outputStream);
            }
        });
        return response.build();
    }


    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllUrl(@DefaultValue("0") @QueryParam("min") String min, @DefaultValue("10") @QueryParam("count") String count) {
        URLInfoDao urlInfoDao = new URLInfoDao(HibernateUtil.getSessionFactory().openSession());
        List<URLInfoEntity> urlInfoEntityList = urlInfoDao.getListUrlInfo(Integer.parseInt(min), Integer.parseInt(count));
        return URLInfoEntity.listUrlToJson(urlInfoEntityList).toJSONString();
    }


    @GET
    @Path("/new")
    @Produces(MediaType.TEXT_PLAIN)
    public String getNewShortUrl(@DefaultValue("") @QueryParam("original") String original) {
        System.out.println(original);
        Session session = HibernateUtil.getSessionFactory().openSession();
        URLInfoDao urlInfoDao = new URLInfoDao(session);
        URLInfoEntity urlInfoEntity = new URLInfoEntity();
        urlInfoEntity.setOriginal_url(original);
        urlInfoEntity.generateShortUrl(urlInfoDao.getSizeUrlInfo());
        urlInfoEntity.setCreated(new Date());
        urlInfoDao.addURLInfo(urlInfoEntity);
        session.close();
        if(original.equals("")) return "";
        return urlInfoEntity.getJson().toJSONString();
    }

    @GET
    @Path("/allpages")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllUrl() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        URLInfoDao urlInfoDao = new URLInfoDao(session);
        int pages = urlInfoDao.getSizeUrlInfo()/10 + 1;
        session.close();
        return new Integer(pages).toString();
    }

    @DELETE
    @Path("/delete")
    public void delete(@QueryParam("short") String shortUrl) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        URLInfoDao urlInfoDao = new URLInfoDao(session);
        urlInfoDao.deleteURLInfo(shortUrl);
        session.close();
    }
}

