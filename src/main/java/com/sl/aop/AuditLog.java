package com.sl.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作历史标记（T2-10）。
 * <p>
 * 打在 Service 方法上，由 {@link AuditLogAspect} 在方法成功返回后落一条 operation_log。
 * 业务 ID 与项目 ID 由切面从方法入参里自动识别（Task / Project / ProjectMilestone / ProjectTaskFile），
 * 所以绝大多数场景只需要写 bizType 和 action。
 *
 * <pre>
 * &#64;AuditLog(bizType = OperationLog.BIZ_TASK, action = "修改任务")
 * public int updateTask(Task t) { ... }
 * </pre>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLog {

	/** 业务类型，取 {@link com.sl.entity.OperationLog} 里的 BIZ_* 常量 */
	String bizType();

	/** 动作描述，会用注解值（为空时退回方法名） */
	String action() default "";
}
