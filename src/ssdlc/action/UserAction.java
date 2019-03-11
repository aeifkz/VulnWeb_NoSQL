package ssdlc.action;

import java.io.IOException;
import java.sql.Connection;
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

@WebServlet(name = "edit", urlPatterns = "/edit")
public class UserAction extends HttpServlet {

	private Integer id;
	private String account;
	private String password;
	private String name;

	static Logger log = Logger.getLogger(LoginAction.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// TODO Day2 實作 CSRF token 防禦措施
		
		
		id = (req.getParameter("id")==null || req.getParameter("id").isEmpty()) ? null : Integer.parseInt(req.getParameter("id"));
		account = req.getParameter("account"); ;
		password = req.getParameter("password");
		name = req.getParameter("name");
		
		log.info(LogModel.log_sanitized("Call edit method " + id + " " + account + " " + password + " " + name));
		
		
		ServletContext context = getServletContext();
				
	
		if(account!=null && context.getAttribute(account)!=null) {
			
			UserBean user = (UserBean)context.getAttribute(account);			
			HttpSession session = req.getSession();
			
			if (name != null && !name.isEmpty()) {
				user.setName(name);
				session.setAttribute("name", name);
			}			
			if (password != null && !password.isEmpty()) {
				user.setPassword(password);
			}
			
			req.setAttribute("msg", "修改資料成功");
			
		}
		else {
			req.setAttribute("msg", "修改失敗");
		}
		
		
		//因為安裝 DB 不方便,所以改成補充資料
		/*
		Connection conn = null;

		try {

			conn = new DBModel().getConnection();

			String sql = "update user set account=account ";

			if (password != null && !password.isEmpty()) {
				sql = sql + ", password='" + password + "'";
			}
			if (name != null && !name.isEmpty()) {
				sql = sql + ", name='" + name + "'";
			}

			sql = sql + " where id=" + id;

			log.debug(LogModel.log_sanitized("edit sql:" + sql));
			req.setAttribute("sql", sql);

			// TODO Day2 使用 prepareStatement 預防 SQL Injection
			Statement stmt = conn.createStatement();
			int rs = stmt.executeUpdate(sql);

			if (rs > 0) {
				
				req.setAttribute("msg", "修改資料成功");
				
				HttpSession session = req.getSession();
				if (name != null && !name.isEmpty()) {
					session.setAttribute("name", name);
				}

			} else {
				req.setAttribute("msg", "修改失敗");
			}

			stmt.close();
			conn.close();

		} catch (Exception ex) {
			log.error("資料庫操作錯誤", ex);
		}
		*/
		
		resp.sendRedirect("main.jsp");

	}

	

}
