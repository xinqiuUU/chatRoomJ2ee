package com.yc.web.servlet;

import com.yc.bean.user;
import com.yc.dao.DBHelper;
import com.yc.model.JsonModel;
import com.yc.utils.Md5;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@WebServlet(value = "/user.action")
public class userServlet extends BaseServlet{
    private DBHelper db = new DBHelper();

    protected void register(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonModel jm = new JsonModel();
        String name = req.getParameter("name");
        String pwd = req.getParameter("pwd");
        List<Map<String,Object>> map =  db.select("select * from user where name=?",name);
        if (map.size()>0){
            jm.setCode(0);
            jm.setError("姓名已被注册。。。");
            super.writeJson(jm,resp);
            return;
        }
        pwd = Md5.toMD5(Md5.toMD5(pwd));
        String sql = "insert into user(name, pwd) values (?,?)";
        int result = db.doUpdate(sql,name,pwd);
        if (result>0){
            jm.setCode(1);
        }else {
            jm.setCode(0);
        }
        super.writeJson(jm,resp);
    }
    protected void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        session.removeAttribute("loginuser");
        JsonModel jm = new JsonModel();
        jm.setCode(1);
        super.writeJson(jm,resp);
    }
    protected void getLoginStatus(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        JsonModel jm = new JsonModel();
        //取出session中的验证码
        HttpSession session = req.getSession();
        if (session.getAttribute("loginuser")==null){
            jm.setCode(0);
        }else {
            jm.setCode(1);
            jm.setObj(  session.getAttribute("loginuser") );
        }
        super.writeJson(jm,resp);
    }
    protected void login(HttpServletRequest req, HttpServletResponse resp) throws InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        JsonModel jm = new JsonModel();
        user c = super.parseObjectFromRequest(req,user.class);

        String valcode = req.getParameter("valcode");
        //取出session中的验证码
        HttpSession session = req.getSession();
        String code = (String) session.getAttribute("code");
        if (!code.equals(valcode)){
            jm.setCode(0);
            jm.setError("验证码错误。。。");
            super.writeJson(jm,resp);
            return;
        }
        String newPwd = Md5.toMD5(Md5.toMD5(c.getPwd()));
        c.setPwd(  newPwd );

        String sql = "select * from user where name=? and pwd=?";
        List<user> list = db.select(user.class,sql,c.getName(),c.getPwd());
        if (  list!=null &&list.size()>0){

            //记录用户的登录状态
            session.setAttribute("loginuser",list.get(0) );

            jm.setCode(1);
            jm.setObj(  list.get(0) );
        }else {
            jm.setCode(0);
            jm.setError("用户名或密码错误。。。");
        }
        super.writeJson(jm,resp);
    }
}
