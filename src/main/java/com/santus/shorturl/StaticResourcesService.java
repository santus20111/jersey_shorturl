package com.santus.shorturl;

import com.sun.org.apache.regexp.internal.RE;
import sun.misc.IOUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by prmanage on 24.10.2017.
 */
@Path("/static")
public class StaticResourcesService{
    public static final String cssPath = "src/main/static/css/";
    public static final String imgPath = "src/main/static/img/";
    public static final String jsPath = "src/main/static/js/";
    public static final String jpegPath = "src/main/static/jpeg/";
    @GET
    @Path("css/{cssfile}")
    @Produces("text/css")
    public Response getCss(@PathParam("cssfile") final String cssfile) {
        if(Files.exists(Paths.get(cssPath + cssfile))) {
            Response.ResponseBuilder response = Response.ok();
            response.entity(new StreamingOutput() {
                @Override
                public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                    Files.copy(Paths.get(cssPath + cssfile), outputStream);
                }
            });
            return response.build();
        }
        return Response.status(422).entity("not such file").build();
    }

    @GET
    @Path("js/{jsfile}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getJs(@PathParam("jsfile") final String jsfile) {
        if(Files.exists(Paths.get(jsPath + jsfile))) {
            Response.ResponseBuilder response = Response.ok();
            response.entity(new StreamingOutput() {
                @Override
                public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                    Files.copy(Paths.get(jsPath + jsfile), outputStream);
                }
            });
            return response.build();
        }
        return Response.status(422).entity("not such file").build();
    }

    @GET
    @Path("jpeg/{jpegfile}")
    @Produces("image/jpeg")
    public Response getJpeg(@PathParam("jpegfile") final String jpegfile) throws IOException {
        java.nio.file.Path path = Paths.get(jpegPath + jpegfile);
        if(Files.exists(path)) {
            final byte[] bytes = Files.readAllBytes(path);
            Response.ResponseBuilder response = Response.ok();
            response.entity(new StreamingOutput() {
                @Override
                public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                    outputStream.write(bytes);
                }
            });
            return response.build();
        }
        return Response.status(422).entity("not such file").build();
    }


}
