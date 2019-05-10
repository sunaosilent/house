package com.house.model;


import com.alibaba.druid.sql.dialect.db2.visitor.DB2ASTVisitor;
import com.eova.cloud.auth.EovaApp;
import com.eova.common.base.BaseModel;
import com.eova.common.utils.xx;
import com.eova.config.EovaConfig;
import com.jfinal.plugin.activerecord.Db;

/**  
* package com.house.model;
*  
* @author  孙傲  
* @date 2018年6月22日  
*/
public class Floor extends BaseModel<Floor> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7408870344194274291L;
	
	public static final Floor dao = new Floor();
	
	/**
	 * 获得楼层信息的楼名，图片
	 * 
	 * @param floor_id
	 * @return
	 */
	public Floor getFloorInfo (int floor_id) {
		return this.findFirst("select a.house_no as house_no,b.`name` as `name`, "
				+ "a.floor_no,a.floor_imagepath as path from h_floor a,h_house b\r\n" + 
				"\r\n" + 
				"where a.house_no = b.house_no and floor_id = ?",floor_id);
	}
	
	/**
	 * 根据楼号，楼层更新房间数
	 * 
	 * @param house_no
	 * @param floor_no
	 */
	public int updateRooms(int rooms,int house_no,int floor_no) {
		
		return Db.use(xx.DS_MAIN).update(" update h_floor set rooms = ? where house_no =? and floor_no = ?"
				,rooms,house_no,floor_no);
	}
	

}
