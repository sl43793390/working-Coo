package com.sl.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sl.app.component.util.ComponentUtil;
import com.sl.entity.OperationLog;
import com.sl.mapper.OperationLogMapper;

import cn.hutool.core.util.IdUtil;

/**
 * 操作历史（T2-10）。
 * <p>
 * 写入入口只有一个 {@link #record}，由 AOP 切面调用；业务代码也可以直接调它补记。
 * 落库失败绝不能影响主流程，所以这里吞掉异常只记日志。
 */
@Service
public class OperationLogService {

	private static final Logger log = LoggerFactory.getLogger(OperationLogService.class);

	@Autowired
	private OperationLogMapper operationLogMapper;

	public void record(String cdBizType, String idBiz, String idProject, String action, String detail) {
		try {
			OperationLog l = new OperationLog();
			l.setIdLog(IdUtil.getSnowflakeNextIdStr());
			String userId = null;
			String userName = null;
			try {
				if (null != ComponentUtil.getCurrentUser()) {
					userId = ComponentUtil.getCurrentUser().getUserId();
					userName = ComponentUtil.getCurrentUser().getUserName();
				}
			} catch (Exception ignore) {
				// 调度线程里没有 UI 会话，属于正常情况
			}
			l.setIdUser(userId);
			l.setNameUser(userName);
			l.setIdProject(idProject);
			l.setCdBizType(cdBizType);
			l.setIdBiz(idBiz);
			l.setCdAction(action);
			l.setCdDetail(detail);
			l.setDtOperation(new Date());
			operationLogMapper.insert(l);
		} catch (Exception e) {
			log.error("写操作日志失败，bizType={}, biz={}", cdBizType, idBiz, e);
		}
	}

	public List<OperationLog> timeline(String cdBizType, String idBiz, int limit) {
		return operationLogMapper.selectByBiz(cdBizType, idBiz, limit);
	}

	public List<OperationLog> recentByProject(String idProject, int limit) {
		return operationLogMapper.selectByProject(idProject, limit);
	}
}
