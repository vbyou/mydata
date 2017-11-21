package cn.itcast.core.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.itcast.core.pojo.Buyer;
import cn.itcast.core.service.BuyerService;
import cn.itcast.core.service.SessionService;
import cn.itcast.core.tools.Encryption;
import cn.itcast.core.tools.SessionTool;

/**
 * 登录控制器
 * 
 * @author Administrator
 *
 */
@Controller
public class LoginAction {

	@Autowired
	private BuyerService buyerService;

	@Autowired
	private SessionService sessionService;

	// 显示登录页面
	@RequestMapping(value = "/login.aspx", method = RequestMethod.GET)
	public String showLogin() {
		System.out.println("显示登录页面");

		return "login";
	}

	// 执行登录
	@RequestMapping(value = "/login.aspx", method = RequestMethod.POST)
	public String doLogin(Model model, HttpServletRequest request,
			HttpServletResponse response, String username, String password,
			String returnUrl) {
		System.out.println("执行登录");
		System.out.println(username);
		System.out.println(password);

		if (username != null) {
			if (password != null) {
				// 验证用户名在数据库中是否存在
				Buyer buyer = buyerService.findByUsername(username);
				if (buyer != null) {
					// 用户名存在，开始验证密码
					if (buyer.getPassword()
							.equals(Encryption.encrypt(password))) {
						// 用户和密码都正确，验证成功，可以登录

						String maosessionID = SessionTool.getSessionID(request,
								response);
						sessionService.addUsernameToRedis(maosessionID,
								buyer.getUsername());

						if (returnUrl != null
								&& returnUrl.trim().length() > 1) {
							return "redirect:" + returnUrl;
						}

						return "redirect:http://localhost:8082/";

					} else {
						// 密码错误
						model.addAttribute("error", "密码错误");
					}

				} else {
					// 用户名不存在
					model.addAttribute("error", "用户名不存在");
				}

			} else {
				// 密码不能为空
				model.addAttribute("error", "密码不能为空");
			}
		} else {
			// 用户名不能为空
			model.addAttribute("error", "用户名不能为空");
		}

		return "login";
	}
	
	
	//检测用户是否登录
	@RequestMapping(value="/isLogin.aspx")
	@ResponseBody
	public MappingJacksonValue isLogin(String callback,HttpServletRequest request,
			HttpServletResponse response)
	{
		
		System.out.println("检测用户是否登录");
		System.out.println(callback);
		String sessionID = SessionTool.getSessionID(request, response);
		String username = sessionService.getUsernameForRedis(sessionID);
		
		System.out.println(username);
		
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(username);
		mappingJacksonValue.setJsonpFunction(callback);
		
		return mappingJacksonValue;
	}

}
