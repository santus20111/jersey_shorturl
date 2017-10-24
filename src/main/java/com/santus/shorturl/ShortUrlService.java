package com.santus.shorturl;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by prmanage on 24.10.2017.
 */
@Path("/")
public class ShortUrlService {
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
}
