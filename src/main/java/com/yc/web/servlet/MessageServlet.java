package com.yc.web.servlet;

import com.yc.bean.user;
import com.yc.model.JsonModel;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@WebServlet(value = "/message.action")
public class MessageServlet extends BaseServlet{

    protected void getMessage(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        JsonModel jm = new JsonModel();
        HttpSession session = req.getSession();
        //拼接字符串  存到application中
        List<String> linkedList = new LinkedList<>();
        ServletContext application = session.getServletContext();
        if ( application.getAttribute("messages")!=null ){
            linkedList = (List<String>) application.getAttribute("messages");
        }
        //判断是否登录  没登录则无法获取  公共消息  每个人有自己的独立的session空间 保存登录信息
        if (session.getAttribute("loginuser")==null){
            jm.setCode(0);
            super.writeJson(jm,resp);
            return;
        }
        jm.setCode(1);
        jm.setObj(linkedList);
        super.writeJson(jm,resp);
    }
    protected void sendMessage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonModel jm = new JsonModel();
        HttpSession session = req.getSession();
        //session中保存的是用户的  user对象
        user u = (user) session.getAttribute("loginuser");
        String message = req.getParameter("message");
        String str = u.getName()+"悄悄的说:<br />"+message+"<br />发布时间:"+new Date()+"<hr />";
        //拼接字符串  存到application中
        List<String> linkedList = new LinkedList<>();
        ServletContext application = session.getServletContext();
        if ( application.getAttribute("messages")!=null ){
            linkedList = (List<String>) application.getAttribute("messages");
        }
        ((LinkedList)linkedList).addFirst(  str );

        //存到  appliction中  共享空间
        application.setAttribute("messages",linkedList);

        jm.setCode(1);
        super.writeJson(jm,resp);
    }
}
