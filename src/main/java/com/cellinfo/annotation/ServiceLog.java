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
@Target(ElementType.TYPE)
public @interface ServiceLog {
	/**
	 * 数据文件管理 DataFileMgr  系统管理 SysMgr
	 * DataClearMgr
	 * 
	 * @return
	 */
	public String moduleName() default "";

}
