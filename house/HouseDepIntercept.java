package com.house;


import com.eova.aop.AopContext;
import com.eova.aop.MetaObjectIntercept;
import com.eova.common.Easy;
import com.eova.common.utils.xx;
import com.house.model.Dep;


/**  
* package com.house;
*  
* @author  孙傲  
* @date 2018年6月22日  
*/
public class HouseDepIntercept extends MetaObjectIntercept {

	
	
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
    	
    	String  depName = ac.record.getStr("name");
    	
    	Dep d = Dep.dao.findFirst("select name from h_dep where name = ? ",depName);
    	if(!xx.isEmpty(d)) {
    		 return Easy.warn("部门：<strong style=\"color:red;\">"+depName+"</strong>  已存在,请更换其他部门名称！");
    	}
    	//继承执行
    	return super.addBefore(ac);
    }
	
	
	
    /**
     * 删除前置任务(事务内)
     * <pre>
     * ac.record    当前删除对象数据
     * -------------
     * 用法：删除前置检查
     * </pre>
     */
    public String deleteBefore(AopContext ac) throws Exception {
    	
    	int id = ac.record.getInt("id");
    	//顶层部门不可删除
    	if(id == 1) {
    		return Easy.warn("顶层部门不可删除!");
    	}
        return super.deleteBefore(ac);
    }
	
}
