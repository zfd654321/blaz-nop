package com.bl.nop.nis.init;


import java.util.EnumSet;

import javax.servlet.DispatcherType;

import com.bl.nop.common.util.NumberUtil;
import com.bl.nop.nis.util.PropertyUtil;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class ApplicationServerLauncher {

	private static Logger logger = LoggerFactory.getLogger(ApplicationServerLauncher.class);
	private static final int DEFLOUD_PORT = 8084;
	private static final String PROJECT_NAME = "nis";
	private static final String ENCODING = "UTF-8";
	private static Server server;
	
    public static void main(String[] args) throws Exception {
    	String serverPort = PropertyUtil.getProperty("server.port");
    	//服务器端口，默认8080
    	int port = NumberUtil.toInt(serverPort, DEFLOUD_PORT);
    	String serverName = PropertyUtil.getProperty("server.name");
    	//服务器端口，默认8080
    	serverName = StringUtils.isBlank(serverName)?PROJECT_NAME:serverName.trim();
    	server = new Server(port);
		ServletContextHandler handler = new ServletContextHandler();
		
		// 服务器根目录，类似于tomcat部署的项目。 完整的访问路径为ip:port/contextPath/realRequestMapping
		//ip:port/项目路径/api请求路径
		handler.setContextPath("/"+serverName);
		logger.info("开始配置请求数据为UTF-8>>>>>>>>>");
		FilterHolder fh = handler.addFilter(CharacterEncodingFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
		fh.setInitParameter("encoding", ENCODING);
		
		logger.info("开始配置Spring>>>>>>>>>");
		XmlWebApplicationContext context = new XmlWebApplicationContext();
		//加载spring配置文件
		context.setConfigLocations("classpath:spring/applicationContext.xml", "classpath:spring/spring-hessian.xml", "classpath:spring/spring-hessian-req.xml");
		//相当于web.xml中配置的ContextLoaderListener
		//handler.addEventListener(new ContextLoaderListener(context));
		//springmvc拦截规则 相当于web.xml中配置的DispatcherServlet
		handler.addServlet(new ServletHolder(new DispatcherServlet(context)), "/*");
		server.setHandler(handler);
		logger.info("开始启动项目["+PROJECT_NAME+"]，端口："+port+">>>>>>>>>");
		long l1 = System.currentTimeMillis();
		server.start();
		long l2 = System.currentTimeMillis();
		logger.info("完成启动项目["+PROJECT_NAME+"]，端口："+port+"，启动时长："+(l2-l1)/1000+"s>>>>>>>>>");
		server.join();
    }
        
}
