package com.sl;

import cn.hutool.core.net.NetUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
// 通知中心的到期扫描 / 里程碑提醒依赖定时任务（T2-4）
@EnableScheduling
@Theme(value = "mytodo" ,variant = Lumo.LIGHT)
//@JsModule("@vaadin/vaadin-lumo-styles/presets/compact.js")//压缩模式
@NpmPackage(value = "line-awesome", version = "1.3.0")
//@Push
@PreserveOnRefresh
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        System.out.println("success!");

        String port = ctx.getEnvironment().getProperty("server.port");
        String path = ctx.getEnvironment().getProperty("server.servlet.context-path");
        System.out.println("====================address============================");
        for (NetworkInterface networkInterface : NetUtil.getNetworkInterfaces()) {
            try {
                if (networkInterface.isUp()){
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()){
                        InetAddress inetAddress = inetAddresses.nextElement();
                        if (!inetAddress.isSiteLocalAddress()){
                            continue;
                        }
                        System.out.println(inetAddress.getHostAddress()+":"+port+path);
                    }
                }
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }
        }
    }

}