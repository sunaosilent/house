package com.house;

import java.util.Date;

import com.eova.aop.AopContext;
import com.eova.aop.MetaObjectIntercept;
import com.eova.common.utils.ToolsUtil;
import com.house.model.Room;
import com.house.model.RoomReg;
import com.jfinal.plugin.activerecord.Record;


/**  
* package com.house;
*  
* @author  孙傲  
* @date 2018年6月22日  
*/
public class RoomIntercept extends MetaObjectIntercept {

	
	
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
    	
		//录入时间默认
    	ac.record.set("reg_date", new Date());
    	
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
     * 更新前置任务(事务内)<br>
     * <pre>
     * ac.record 当前操作数据
     * -------------
     * 用法：级联修改，需在同事务内完成
     * </pre>
     */
    public String updateBefore(AopContext ac) throws Exception {
    	
    	
    	Record r  = ac.record;
    	Record room  = Room.dao.findById(r.getInt("id")).toRecord();
    	
    	boolean flag = ToolsUtil.getRecordStatus(r, room);
    	RoomReg roomReg = null;
    	Date nowDate = new Date();
    	//判断是否进行数据变更
    	if(!flag) 
    		roomReg = new RoomReg();
    		roomReg.put(room);
    		//修改时间
    		roomReg.set("reg_edittime", nowDate);
    		roomReg.save();
    	
		ac.record.set("reg_date", nowDate);	
        return super.updateBefore(ac);
    }
	
}
