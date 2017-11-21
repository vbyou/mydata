package cn.itcast.core.tools;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * session工具类
 * 
 * @author Administrator
 *
 */
public class SessionTool {

	/**
	 * 获得自定义sessionID，并在初次访问时，分配maosessionid，并将maosessionid写入到浏览器的cookie中
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static String getSessionID(HttpServletRequest request,
			HttpServletResponse response) {

		// 检查浏览器传来的cookie中有没有maosid
		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("maosessionid")) {
					return cookie.getValue();
				}
			}
		}

		// 就要给浏览器分配maosid
		String maosessionid = UUID.randomUUID().toString().replaceAll("-", "");

		// 将maosid放入浏览器的cookie中
		Cookie cookie = new Cookie("maosessionid", maosessionid);

		// 浏览器关掉后，cookie就删除
		cookie.setMaxAge(-1);

		// 设置路径 如果不设置，端口后面的目录名称（xxx:8080/login）会对cookie存储照成影响
		cookie.setPath("/");

		// 设置二级跨域，由于本次课程中都是localhost，所有无需设置，但是项目正式上限后需要设置该项
		// cookie.setDomain(".babasport.com");

		response.addCookie(cookie);

		return maosessionid;
	}

	public static void main(String[] args) {

		System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
	}

}
