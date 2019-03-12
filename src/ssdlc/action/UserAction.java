package ssdlc.action;

import java.io.IOException;
import java.io.StringReader;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import ssdlc.bean.UserBean;
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
		
		
		try {
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
			//TODO Day2 針對XML解析函式關閉外部引入功能
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			//透過 DocumentBuilderFactory 解析輸入參數 
			Document doc = builder.parse(new InputSource(new StringReader(name)));
			
			NodeList nodes = doc.getElementsByTagName("name");
			for(int i=0; i<nodes.getLength(); i++) {
				name = nodes.item(i).getTextContent();
			}
		}
		catch(Exception ex) {
			log.info("XML parse錯誤");
		}
		
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
			
			context.setAttribute(account,user);
			
			req.setAttribute("msg", "修改資料成功");
			
		}
		else {
			req.setAttribute("msg", "修改失敗");
		}
		
		resp.sendRedirect("main.jsp");

	}

	

}
