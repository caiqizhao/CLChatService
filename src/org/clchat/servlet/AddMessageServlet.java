package org.clchat.servlet;

import com.mysql.cj.xdevapi.JsonParser;
import com.mysql.cj.xdevapi.JsonString;
import net.sf.json.JSONObject;
import net.sf.json.processors.JsonBeanProcessor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mysql.entity.Message;
import org.mysql.mapper.MySQLUserMapper;
import org.mysql.util.MySQLDB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AddMessageServlet",urlPatterns = "/addMessage")
public class AddMessageServlet extends HttpServlet {
    SqlSessionFactory sqlSessionFactory;
    SqlSession sqlSession;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        sqlSessionFactory = MySQLDB.getSqlSession();
        sqlSession = sqlSessionFactory.openSession();
        MySQLUserMapper sqlUtil;
        sqlUtil = sqlSession.getMapper(MySQLUserMapper.class);
        String json = request.getParameter("message");
        Message message = (Message) JSONObject.toBean(JSONObject.fromObject(json),Message.class);
        //信息发出方
        sqlUtil.addMessage(message.getUser_id(),message.getFriend_id(),message.getMessage(),message.getMessage_state(),message.getPut_id(),message.getTime());
        //信息接收方
        sqlUtil.addMessage(message.getFriend_id(),message.getUser_id(),message.getMessage(),0,0,message.getTime());
        sqlSession.commit();
        sqlSession.close();
    }
}
