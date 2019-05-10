package com.house;

import com.eova.common.base.BaseController;
import com.eova.common.utils.xx;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**  
* package com.house;
*  
* @author  孙傲  
* @date 2018年7月3日  
*/
public class CommonController extends BaseController {

	
	/**
	 * 默认跳转
	 */
	public void index() {
		
		Record r = Db.use(xx.DS_MAIN).findFirst("select * from v_status_all");
		
		setAttr("bar", r);
		
		setAttr("dqList", Db.use(xx.DS_MAIN).find("select * from (select * from h_room  where room_state = 3  order by reg_enddate desc )　 limit 0,5"));
		
		render("/house/mapIndex.html");
	}
	
	/**
	 * 默认跳转： 单位报表查询
	 */
	public void reportDW() {
		
		render("/house/search/reportDW.html");
	}
	
	/**
	 * 默认跳转  楼号报表查询
	 */
	public void reportLH() {
		
		render("/house/search/reportLH.html");
	}
	
	
	/**
	 * 默认跳转  综合查询
	 */
	public void reportZH() {
		
		render("/house/search/reportZH.html");
	}
	
	
	/**
	 * 默认跳转  汇总查询 附属楼宇
	 */
	public void reportHZ() {
		
		render("/house/search/reportHZ.html");
	}
	
	/**
	 * 默认跳转  汇总查询 附属楼宇
	 */
	public void reportHZFS() {
		
		render("/house/search/reportHZFS.html");
	}
	
}
