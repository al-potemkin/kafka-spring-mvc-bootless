package com.kaf.bootless;

import com.kaf.bootless.config.Const;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;

public class Main {
    public static void main(String[] args) throws LifecycleException, ServletException {
        Tomcat tomcat = new Tomcat();
        tomcat.addWebapp(Const.CONTEXT_PATH, Const.APP_BASE);
        tomcat.getHost().setAppBase(Const.APP_BASE);
        tomcat.setPort(Const.DEFAULT_PORT);
        tomcat.start();
        tomcat.getServer().await();
    }
}
