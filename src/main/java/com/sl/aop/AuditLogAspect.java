package com.sl.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sl.entity.Project;
import com.sl.entity.ProjectMilestone;
import com.sl.entity.ProjectTaskFile;
import com.sl.entity.Task;
import com.sl.service.OperationLogService;

/**
 * 把带 {@link AuditLog} 注解的 Service 调用记进 operation_log（T2-10）。
 * <p>
 * 只在方法**正常返回**后记录：抛异常意味着操作没成功，记进去反而误导。
 * 落库失败由 OperationLogService 自己吞掉，绝不能因为审计把业务操作搞挂。
 */
@Aspect
@Component
public class AuditLogAspect {

	private static final Logger log = LoggerFactory.getLogger(AuditLogAspect.class);

	/** 明细最长长度，超了截断，避免 CD_DETAIL(2000) 溢出 */
	private static final int MAX_DETAIL = 1500;

	@Autowired
	private OperationLogService operationLogService;

	@AfterReturning(pointcut = "@annotation(auditLog)", returning = "ret")
	public void record(JoinPoint joinPoint, AuditLog auditLog, Object ret) {
		try {
			Object[] args = joinPoint.getArgs();
			String bizType = auditLog.bizType();
			String action = auditLog.action();
			if (null == action || action.isBlank()) {
				action = methodName(joinPoint);
			}
			String idBiz = null;
			String idProject = null;
			String detail = null;
			for (Object arg : args) {
				if (null == arg) {
					continue;
				}
				if (arg instanceof Task t) {
					idBiz = t.getIdTask();
					idProject = t.getIdProject();
					detail = describeTask(t);
					break;
				}
				if (arg instanceof Project p) {
					idBiz = p.getIdProject();
					idProject = p.getIdProject();
					detail = "项目名称=" + p.getNameProject();
					break;
				}
				if (arg instanceof ProjectMilestone m) {
					idBiz = m.getIdMilestone();
					idProject = m.getIdProject();
					detail = "里程碑=" + m.getNameMilestone();
					break;
				}
				if (arg instanceof ProjectTaskFile f) {
					idBiz = f.getIdFile();
					idProject = f.getIdProject();
					detail = "文件=" + f.getNameFile();
					break;
				}
			}
			if (null == idBiz && args.length > 0 && args[0] instanceof String s) {
				// 只有 ID 入参的方法（例如按 ID 删除）：直接用第一个字符串当业务 ID
				idBiz = s;
			}
			operationLogService.record(bizType, idBiz, idProject, action, truncate(detail));
		} catch (Exception e) {
			log.warn("记录操作历史失败，method={}", joinPoint.getSignature().toShortString(), e);
		}
	}

	private String methodName(JoinPoint joinPoint) {
		if (joinPoint.getSignature() instanceof MethodSignature ms) {
			Method m = ms.getMethod();
			return m.getDeclaringClass().getSimpleName() + "#" + m.getName();
		}
		return joinPoint.getSignature().getName();
	}

	/**
	 * 把任务的关键字段拼成一行明细，详情页的变更时间线直接展示。
	 * 只挑会被关心的字段，不做全字段 diff（全字段 diff 需要先查旧值，代价高于收益）。
	 */
	private String describeTask(Task t) {
		List<String> parts = new ArrayList<>();
		parts.add("任务=" + t.getNameTask());
		if (null != t.getCdStatus()) {
			parts.add("状态=" + t.getCdStatus());
		}
		if (null != t.getPctComplete()) {
			parts.add("完成率=" + t.getPctComplete());
		}
		if (null != t.getDtStart()) {
			parts.add("开始=" + t.getDtStart());
		}
		if (null != t.getDtEnd()) {
			parts.add("结束=" + t.getDtEnd());
		}
		return String.join("，", parts);
	}

	private String truncate(String s) {
		if (null == s || s.length() <= MAX_DETAIL) {
			return s;
		}
		return s.substring(0, MAX_DETAIL) + "…";
	}
}
