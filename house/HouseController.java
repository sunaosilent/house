package com.house;

import java.util.Date;

import com.eova.common.base.BaseController;
import com.eova.common.utils.xx;
import com.eova.model.User;
import com.house.model.Floor;
import com.house.model.House;
import com.house.model.Room;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

/**  
* package com.house;
*  
* @author  孙傲  
* @date 2018年6月24日  
*/
public class HouseController  extends BaseController {

	
	/***
	 * 新增房屋界面
	 */
	@Before(Tx.class)
	public void addRoom() {

		//获得登录用户
		User user  = getSessionAttr("user");
		int userid = user.getInt("id");
		//屋子数
		int room_num = getParaToInt("rooms");
		int house_no = getParaToInt("house_no");
		int floor_no = getParaToInt("floor_no");
		Room room = null;
		//保存计数器
		int room_num_result = 0;
		Date date = new Date();
		//更新条数计数
		int rooms = room_num;
		int begingRoomNum = 0;
		Room maxRoom = Room.dao.findFirst("select room_no from h_room where house_no = ? and floor_no = ? order by room_no desc"
				,house_no,floor_no);
		//获得楼层房屋最大编号
		if(!xx.isEmpty(maxRoom)) {
			begingRoomNum = Math.abs(maxRoom.getInt("room_no"));
			int flagNum = begingRoomNum - (floor_no*1000);
			if(flagNum<0) {
				begingRoomNum = begingRoomNum - (floor_no*100);
			}else {
				begingRoomNum = flagNum;
			}
		}
		begingRoomNum++;
		
		room_num = room_num+begingRoomNum;
		
		int roomsOld = Room.dao.findFirst("select count(0) as hj from h_room where house_no = ? and floor_no = ?"
				,house_no,floor_no).getInt("hj");
		/**
		 * 增加房屋
		 */
		for(int i =begingRoomNum ;i<room_num;i++){
			//房间过滤包含4的编码
			room = new Room();
			String iStr = Integer.toString(i);
			if(!iStr.contains("4")) {
				room.set("house_no", house_no);
				room.set("room_no", i+(floor_no*100));
				room.set("floor_no", floor_no);
				room.set("room_state", 1);
				room.set("reg_userid", userid);
				room.set("reg_date", date);
				if(room.save()) {
					room_num_result++;
				}
			}else {
				room_num++;
			}
		}
		
		
		int num_flag = Floor.dao.updateRooms(rooms+roomsOld,house_no,floor_no);
		Record  r = new Record();
		
		if(rooms == room_num_result && num_flag == 1) {
			r.set("flag", true);
			r.set("msg", "添加成功,房屋起始编号:"+begingRoomNum);
		}else {	
			r.set("flag", false);
			r.set("msg", "添加失败");
		}
		renderJson(r);
	}
	
	/**
	 * 查看楼层平面图
	 */
	public void viewMap() {
		
		setAttr("floor", Floor.dao.getFloorInfo(getParaToInt("floor_id")));
		render("/house/floorMap.html");
	}
	
	/**
	 * 查看平面图
	 */
	public void viewMapHouse() {
		
		setAttr("house",House.dao.getHouseImage(getParaToInt("house_no")));
		String flagMsg  = "正面实景图";
		if(getParaToInt("flag") == 1) {
			flagMsg="侧面实景图";
		}
		setAttr("flagMsg",flagMsg);
		setAttr("flag", getParaToInt("flag"));
		render("/house/houseMap.html");
	}
	
	
	

}
