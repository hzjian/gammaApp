package com.cellinfo.utils;

//异常代码	异常描述	应用业务场景描述

public class ExceptionDesc {
	
	public static final String ERR1000 = "其它错误";

	public static final String ERR2001 = "";
	public static final String ERR2002 = "";
	
	public static final String NO_AUTHENTICATION = "该用户没有授权";

	public static final String ILLEGAL_ARGUMENT = null;

	public static final String SIGNATURE_EXCEPTION = null;


	public static final String UNSUPPORTEDJWT_EXCEPTION = null;

	public static final String MALFORMEDJWT_EXCEPTION = null;

	public static final String EXPIREDJWT_EXCEPTION = null;

	public static final String DB_OPRATION_EXCEPTION = "操作数据库出现异常：字段重复、有外键关联等";

	public static final String EXCEPTION = "系统异常";

	public static final String BUSINESS_EXCEPTION = "业务逻辑异常";

	public static final String CONTROLLER_BUSINESS_EXCEPTION = null;

	public static final String SERVICE_BUSINESS_EXCEPTION = null;

	public static final String UNSUPPORT_MEDIA_TYPE = "不支持当前媒体类型";

	public static final String UNSUPPORT_THE_METHOD = "不支持当前请求方法";

	public static final String VALIDATION_EXCEPTION = "参数验证失败";

	public static final String CONSTRAINT_VIOLATION_EXCEPTION = "参数冲突";
}
