package com.mfe.haydaycmd;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.webapp.WebAppContext;

import java.net.URISyntaxException;
import java.net.URL;


/**
 * Created by chenmin on 2017/3/30.
 */
public class Main {
    private static Logger log;
    public static void main(String[] args) throws Exception{
        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        loggerContext.getConfiguration().getRootLogger().setLevel(System.getProperty("logLevel", "info").equals("debug") ? Level.DEBUG : Level.INFO);
        log = LogManager.getLogger(Main.class);
        startWebApp();
    }



    private static void startWebApp(){
        //webapp context
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");
        webAppContext.setDescriptor("webapp" + "/WEB-INF/web.xml");

        URL webAppDir = Thread.currentThread().getContextClassLoader().getResource("webapp");
        if (webAppDir == null) {
            throw new RuntimeException(String.format("No %s directory was found into the JAR file", "webapp"));
        }
        try {
            //log.debug("webAppDir: "+webAppDir.toString());
            //log.debug("webAppContext.setResourceBase: "+webAppDir.toURI().toString());
            webAppContext.setResourceBase(webAppDir.toURI().toString());
            webAppContext.setParentLoaderPriority(true);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ////////////////////////


        String value = System.getProperty("port", "8089");
        final int port=Integer.valueOf(value);
        final Server server = new Server(port);
        server.setHandler(webAppContext);

        //CORS filter
        FilterHolder filter = new FilterHolder(CrossOriginFilter.class);
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "POST,GET,OPTIONS,PUT,DELETE,HEAD,PATCH");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With, Content-Type, Accept, Origin, access-control-allow-origin");
        filter.setName("cross-origin");
        FilterMapping fm = new FilterMapping();
        fm.setFilterName("cross-origin");
        fm.setPathSpec("*");
        webAppContext.getServletHandler().addFilter(filter, fm );

        ////////////////////////


        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    try {
                        server.start();
                        log.info("Haydaycmd server started@" + port);
                        server.join();
                    } catch (Exception e) {
                        log.error("Haydaycmd server start@" + port + " failed", e);
                        shutDown();
                    }
                } finally {
                    log.error("Haydaycmd server exit@" + port + " failed");
                    server.destroy();
                }

            }
        }).start();
    }

    private static void shutDown() {
        log.info("Shutdown.");
        System.exit(1);
    }
}
