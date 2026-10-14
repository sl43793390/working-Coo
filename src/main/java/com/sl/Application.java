package com.sl;

import cn.hutool.core.net.NetUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.shared.ui.Transport;
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
// S3 实时通知：服务端有新通知时主动推到浏览器刷新铃铛红点。
// WEBSOCKET 传输 + AUTOMATIC 模式：能推就推，WebSocket 断开自动降级长轮询。
// NotificationBell 里仍保留 60s 低频轮询作兜底（如用户在多个标签页停留很久）。
@Push(transport = Transport.WEBSOCKET)
@Theme(value = "mytodo" ,variant = Lumo.LIGHT)
//@JsModule("@vaadin/vaadin-lumo-styles/presets/compact.js")//压缩模式
@NpmPackage(value = "line-awesome", version = "1.3.0")
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