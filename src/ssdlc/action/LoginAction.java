package ssdlc.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement; 

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ssdlc.bean.UserBean;
import ssdlc.model.DBModel;
import ssdlc.model.LogModel;

@WebServlet(name="login",urlPatterns={"/login"})
public class LoginAction extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	static Logger log = Logger.getLogger(LoginAction.class);
	
	private String account;
	private String password;
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		account = req.getParameter("account");
		password = req.getParameter("password");
		
		log.info(LogModel.log_sanitized("Call login method " + account + " " + password));
		
		boolean is_success = false;		
		ServletContext context= getServletContext();
		
		if(context.getAttribute(account)!=null) {
			
			UserBean user = (UserBean)context.getAttribute(account);
			
			if(user.getPassword().equals(password)) {				
				HttpSession session = req.getSession();
				//TODO Day2 針對 account 內容作 HTML消毒
				session.setAttribute("account",account);
				session.setAttribute("name",user.getName());				
				is_success = true;
				
				//TODO Day2 將 cookie 設定為只允許使用 HTTP 存取
				Cookie cookie = new Cookie("account",account);
				resp.addCookie(cookie);
				
				cookie = new Cookie("password",password);
				resp.addCookie(cookie);				
			}
			
		}
		
		if(is_success==false){
			//TODO Day2 針對訊息內容作對應的消毒
			String msg = "帳號"+account+"不存在或是密碼錯誤";
			req.setAttribute("msg",msg);
		}
		
		
		//因為安裝 DB 不方便,所以改成補充資料
		/*
		Connection conn = null;

		try {
			
			conn = new DBModel().getConnection();
						
			String sql = "select id, account, password,name from user where account like '%" + account + "%' and password='" + password + "'";
			log.debug(LogModel.log_sanitized("login sql:"+sql));
			
			//TODO Day2 使用 prepareStatement 預防 SQL Injection
			Statement stmt = conn.createStatement();			
			ResultSet rs = stmt.executeQuery(sql);
			
			
			req.setAttribute("sql",sql);
			
			if(rs.next()) {
				
				Integer id = rs.getInt("id");
				String account = rs.getString("account");
				String name = rs.getString("name");
				String password = rs.getString("password");
				
				
				//TODO Day2 將 cookie 設定為只允許使用 HTTP 存取
				Cookie cookie = new Cookie("account",account);
				resp.addCookie(cookie);
				
				cookie = new Cookie("password",password);
				resp.addCookie(cookie);
				
				
				HttpSession session = req.getSession();				
				session.setAttribute("id",id);
				//TODO Day2 針對 account 內容作 HTML消毒
				session.setAttribute("account",account);
				session.setAttribute("name",name);
				
				is_success = true;

			}
			else {
				if(account!=null) {
					//TODO Day2 針對訊息內容作對應的消毒
					String msg = "帳號"+account+"不存在或是密碼錯誤";
					req.setAttribute("msg",msg);
				}				
			}			
			
			rs.close();
			stmt.close();
			conn.close();

		} catch (Exception ex) {
			log.error("資料庫操作錯誤",ex);			
		}
		*/
		
		
		
		if(is_success) {
			RequestDispatcher view = req.getRequestDispatcher("main.jsp");
			view.forward(req,resp);
		}			
		else {
			RequestDispatcher view = req.getRequestDispatcher("index.jsp");
			view.forward(req,resp);
		}
	}
	
}
