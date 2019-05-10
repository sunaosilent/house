package com.house;

import java.util.List;

import com.eova.common.utils.EncryptUtil;
import com.eova.common.utils.xx;
import com.eova.config.EovaConfig;
import com.eova.config.EovaConst;
import com.eova.core.IndexController;
import com.eova.model.User;
import com.house.model.Floor;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**  
* package com.house;
*  
* @author  孙傲  
* @date 2018年9月12日  
*/
public class SsoController extends IndexController  {
	
	
	
	/**
	 * 192.168.1.9 白名单
	 * 
	 * 单点登录
	 */
	public void index() {
		/*访问IP*/
		String serverIp = getRequest().getRemoteAddr();
		//Integer serverPort = getRequest().getServerPort();
		System.out.println("come_ip="+serverIp);
/*		List<Record> rlist = Db.use(xx.DS_MAIN).find("select ip from h_ip");
		int flag = 0;
		for (Record r : rlist) {
			if(serverIp.equals(r.getStr("ip")))
				flag++;
		}
		//System.err.println("flag=="+flag);
		if (flag == 0) {
			setAttr("msg", "单点访问ip地址不在允许白名单内,请输入用户名密码！");
			super.toLogin();
			return;
		}*/

		String loginId = getPara("uid");


		String userDs = xx.getConfig("login.user.ds", xx.DS_EOVA);
		String userTable = xx.getConfig("login.user.table", "eova_user");
		String userId = xx.getConfig("login.user.id", "id");
		String userAccount = xx.getConfig("login.user.account", "login_id");
		//String userPassword = xx.getConfig("login.user.password", "login_pwd");
		String userRid = xx.getConfig("login.user.rid", "rid");

		Record r = Db.use(userDs).findFirst(String.format("select * from %s where %s = ?", userTable, userAccount), loginId);
		if (r == null) {
			setAttr("msg", "用户名不存在");
			super.toLogin();
			return;
		}

		User user = new User();
		user.set("id", r.get(userId));
		user.set("rid", r.getInt(userRid));
		user.put(userAccount, r.get(userAccount));

		try {
			loginInit(this, user);
			user.init();
		} catch (Exception e) {
			e.printStackTrace();
			setAttr("msg", e.getMessage());
			keepPara("loginId");
			super.toLogin();
			return;
		}
		setSessionAttr(EovaConst.USER, user);
		// 重定向到首页
		redirect("/");
		
	}

}
