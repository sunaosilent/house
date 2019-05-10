package com.house;


import com.eova.common.base.BaseController;
import com.house.model.Floor;
import com.house.model.Room;

/**  
* package com.house;
*  
* @author  孙傲  
* @date 2018年8月1日  
*/
public class RoomController  extends BaseController {


	/**
	 * 获得房间
	 * 
	 */
	public void getRoomNum() {
		//获得房间号
		renderJson(Floor.dao.find("select id as ID ,CONCAT(room_no,'') as CN from h_room where house_no = ? and floor_no = ?  ",getParaToInt(1),getParaToInt(0)));
	}
	

	/**
	 * 根据房间主键Id获得房屋基本信息
	 * 
	 */
	public void getRoomInfo() {
		renderJson(Room.dao.findById(getParaToInt(0)));
	}
	
	
	
	
}
