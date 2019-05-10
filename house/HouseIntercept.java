package com.house;

import java.util.Date;

import com.eova.aop.AopContext;
import com.eova.aop.MetaObjectIntercept;
import com.eova.common.Easy;
import com.eova.common.utils.xx;
import com.house.model.Floor;
import com.house.model.House;

/**  
* package com.house;
*  
* @author  孙傲  
* @date 2018年6月22日  
*/
public class HouseIntercept extends MetaObjectIntercept {

	
	
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
    	
    	ac.record.set("edittime", new Date());
    	//楼号主键
    	int house_no = ac.record.getInt("house_no");
    	House h = House.dao.findFirst("select house_no from h_house where house_no = ?",house_no);
    	if(!xx.isEmpty(h)) {
    		 return Easy.warn("楼号：<strong style=\"color:red;\">"+h.getInt("house_no")+"</strong>  已存在,请更换新增楼号！");
    	}
    	//继承执行
    	return super.addBefore(ac);
    }
	
    /**
     * 更新前置任务(事务内)
     * <pre>
     * ac.record 当前操作数据
     * -------------
     * 用法：自动赋值
     * ac.record.set("update_time", new Date());
     * ac.record.set("user_id", ac.user.get("id"));
     *
     * 用法：唯一值判定
     * int count = Db.queryInt("select count(*) from users where name = ?", ac.record.getStr("name"));
     * if (count > 0) {
     *     return Easy.error("名字被占用了");
     * }
     * </pre>
     */
    public String updateBefore(AopContext ac) throws Exception {
    	
    	ac.record.set("edittime", new Date());
        return super.updateBefore(ac);
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
		
		//获得楼号
		int id = ac.record.getInt("house_no");
		//获得楼层
		int floor_no = ac.record.getInt("floor_num");
		Floor f = null;
		//根据层数自动增加
		for(int i = 1 ;i<=floor_no; i++) {
			f = new Floor();
			f.set("house_no", id);
			f.set("floor_no", i);
			f.set("floor_imagepath", "");
			f.set("bak", "");
			f.save();
		}
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
    	
		//获得楼号
		int id = ac.record.getInt("house_no");
		//获得楼层
		int floor_no = ac.record.getInt("floor_num");
    	
        return null;
    }
	
}
