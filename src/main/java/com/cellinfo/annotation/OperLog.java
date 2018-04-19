package com.cellinfo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Administrator
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OperLog {

	public String funcName() default "";

	/**
	 * １ 新增 add ２ 修改 edit ３ 删除 delete ４ 查询 query ５ 导入 import ６ 导出 export ７ 数据清理
	 * clear
	 * 
	 * @return
	 */
	public String methodName() default "";

	/**
	 * 自动获取方法名称和参数的toString()值
	 * 
	 * @return
	 */
	public String desc() default "";

}
