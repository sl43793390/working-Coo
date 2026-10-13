package com.sl.config;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

public class MySessionListener implements SessionListener {

    @Override
    public void onStart(Session session) {
        System.out.println("会话创建：" + session.getId());  
    }

    @Override
    public void onStop(Session session) {


    }

    @Override
    public void onExpiration(Session session) {


    }

}
