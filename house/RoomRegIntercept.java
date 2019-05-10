package com.house;

import java.util.Date;

import com.eova.aop.AopContext;
import com.eova.aop.MetaObjectIntercept;
import com.eova.common.utils.xx;
import com.house.model.Room;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**  
* package com.house;
*  
* @author  孙傲  
* @date 2018年6月22日  
*/
public class RoomRegIntercept extends MetaObjectIntercept {

	
	
    /**
	 * 新增前置任务(事务内)
	 * 
	 * <pre>
	 * ac.record 当前操作数据
	 * -------------
	 * 用法：自动赋值
	 * ac.record.set("create_time", TimestampUtil.getNow());
	 * ac.record.set("create_uid", ac.UID);
	 *
	 * 用法：唯一值判定
	 * int count = Db.queryInt("select count(*) from users where name = ?", ac.record.getStr("name"));
	 * if (count > 0) {
	 *     return Easy.error("名字不能重复");
	 * }
	 * </pre>
	 */
	@Override
    public String addBefore(AopContext ac) throws Exception {
    	
    	//上次登记时间
        Object upDate = ac.record.get("reg_date");
    	//本次登记时间
    	ac.record.set("reg_date",new Date());
    	//获得房间号
    	ac.record.set("room_no",Room.dao.findFirst("select room_no from h_room where id = ?",ac.record.getInt("id")).getInt("room_no"));
    	Record oldRecord =  ac.record;
    	//1.执行房屋信息修改
    	Record room = oldRecord.remove("reg_edittime");
    	System.err.println("room="+room);
    	Db.use(xx.DS_MAIN).update("h_room", room);
    	ac.record.set("reg_edittime",upDate);
    	//继承执行
    	return super.addBefore(ac);
    }
	
	
    /**
     * 新增后置任务(事务内)
     * <pre>
     * ac.record 当前操作数据
     * -------------
     * 用法：级联新增，需在同事务内完成
     * int id = ac.record.getInt("id");// 获取当前对象主键值，进行级联新增
     * </pre>
     */
	@Override
    public String addAfter(AopContext ac) throws Exception {
		
        return super.addAfter(ac);
    }
	
	
    /**
     * 更新后置任务(事务内)<br>
     * <pre>
     * ac.record 当前操作数据
     * -------------
     * 用法：级联修改，需在同事务内完成
     * </pre>
     */
    public String updateAfter(AopContext ac) throws Exception {
    	
    	
        return super.updateAfter(ac);
    }
	
}
