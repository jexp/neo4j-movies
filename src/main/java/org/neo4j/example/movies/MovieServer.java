package org.neo4j.example.movies;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.webapp.WebAppContext;
import org.neo4j.example.util.Util;
import org.slf4j.Logger;

public class MovieServer
{
    private Server server;
    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(MovieServer.class);

    public static void main(String[] args) throws Exception
    {
        int port = (args.length>0) ? Integer.parseInt(args[0]): Util.getWebPort();
        String url = (args.length>1) ? args[1] : Util.getNeo4jUrl();
        final MovieServer movieServer = new MovieServer();
        movieServer.start(port);
        movieServer.join();
    }

    public void start(int port) throws Exception {
        LOG.warn("Port used: " + port + " location " + Util.WEBAPP_LOCATION);
        server = new Server(port);
        WebAppContext root = new WebAppContext();
        root.setContextPath("/");
        root.setDescriptor(Util.WEBAPP_LOCATION + "/WEB-INF/web.xml");
        root.setResourceBase(Util.WEBAPP_LOCATION);
        root.setParentLoaderPriority(true);
        final HandlerList handlers = new HandlerList();
        final Handler resourceHandler = createResourceHandler("/assets", Util.WEBAPP_LOCATION);
        handlers.setHandlers(new Handler[]{resourceHandler,root});
        server.setHandler(handlers);
        server.start();
    }

    private Handler createResourceHandler(String context, String resourceBase) {
        WebAppContext ctx = new WebAppContext();
        ctx.setContextPath(context);
        ctx.setResourceBase(resourceBase);
        ctx.setParentLoaderPriority(true);
        return ctx;
    }

    public void join() throws InterruptedException {
        server.join();
    }

    public void stop() throws Exception {
        server.stop();
    }
}

