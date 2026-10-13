package com.sl.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sl.app.component.util.ComponentUtil;
import com.sl.aop.AuditLog;
import com.sl.entity.OperationLog;
import com.sl.entity.Project;
import com.sl.entity.ProjectUser;
import com.sl.entity.ProjectUserExample;
import com.sl.entity.User;
import com.sl.mapper.ProjectUserMapper;
import com.sl.user.UserManagementEnums;

/**
 * 项目级角色（T2-5）。
 * <p>
 * 全局 RBAC（用户-角色-权限）表达不了「项目负责人 / 成员 / 只读访客」这一层，
 * 所以项目内的权限判断统一走这里：
 * <ul>
 *   <li>全局管理员（checkedAllUser）对所有项目拥有全部权限；</li>
 *   <li>项目负责人 OWNER 可改项目、管成员、恢复自己的项目；</li>
 *   <li>成员 MEMBER 可改任务；</li>
 *   <li>只读访客 VIEWER 只能看。</li>
 * </ul>
 */
@Service
public class ProjectRoleService {

	private static final Logger log = LoggerFactory.getLogger(ProjectRoleService.class);

	@Autowired
	private ProjectUserMapper projectUserMapper;
	@Autowired
	private UserManageMentProcess userManageMentProcess;
	@Autowired
	private ProjectProcess projectProcess;

	/**
	 * 取用户在项目内的角色；不在项目里返回 null。
	 */
	public String getRole(String idProject, String idUser) {
		if (null == idProject || null == idUser) {
			return null;
		}
		ProjectUserExample ex = new ProjectUserExample();
		ex.createCriteria().andIdProjectEqualTo(idProject).andIdUserEqualTo(idUser);
		List<ProjectUser> list = projectUserMapper.selectByExample(ex);
		if (null == list || list.isEmpty()) {
			return null;
		}
		String role = list.get(0).getCdProjectRole();
		return (null == role || role.isBlank()) ? ProjectUser.ROLE_MEMBER : role;
	}

	/** 当前登录用户在当前项目里的角色 */
	public String getCurrentRole() {
		Project p = ComponentUtil.getCurrentProject();
		com.sl.entity.User u = ComponentUtil.getCurrentUser();
		if (null == p || null == u) {
			return null;
		}
		return getRole(p.getIdProject(), u.getUserId());
	}

	/** 全局管理员对所有项目放行 */
	public boolean isGlobalAdmin() {
		try {
			return SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER);
		} catch (Exception e) {
			log.debug("判断全局管理员权限失败，按非管理员处理", e);
			return false;
		}
	}

	public boolean isOwner(String idProject, String idUser) {
		return isGlobalAdmin() || ProjectUser.ROLE_OWNER.equals(getRole(idProject, idUser));
	}

	/** 能否修改项目内的业务对象（任务、文档等） */
	public boolean canEdit(String idProject, String idUser) {
		if (isGlobalAdmin()) {
			return true;
		}
		String role = getRole(idProject, idUser);
		return ProjectUser.ROLE_OWNER.equals(role) || ProjectUser.ROLE_MEMBER.equals(role);
	}

	/** 能否管理项目本身（改项目信息、管成员、恢复回收站里的项目） */
	public boolean canManageProject(String idProject, String idUser) {
		return isOwner(idProject, idUser);
	}

	/** 当前用户能否编辑当前项目 */
	public boolean canEditCurrentProject() {
		Project p = ComponentUtil.getCurrentProject();
		com.sl.entity.User u = ComponentUtil.getCurrentUser();
		if (null == p || null == u) {
			return false;
		}
		return canEdit(p.getIdProject(), u.getUserId());
	}

	/**
	 * 项目成员列表（带姓名）。列表页直接展示，不需要 UI 再自己拼。
	 */
	public List<MemberVO> listMembers(String idProject) {
		List<ProjectUser> pus = projectProcess.getProjectUserListByIdProject(idProject);
		if (null == pus || pus.isEmpty()) {
			return new ArrayList<>();
		}
		Set<String> ids = pus.stream().map(ProjectUser::getIdUser).collect(Collectors.toSet());
		List<User> users = userManageMentProcess.getUserByIds(ids);
		java.util.Map<String, String> nameById = new java.util.HashMap<>();
		for (User u : users) {
			nameById.put(u.getUserId(), u.getUserName());
		}
		List<MemberVO> result = new ArrayList<>();
		for (ProjectUser pu : pus) {
			MemberVO vo = new MemberVO();
			vo.setIdProject(pu.getIdProject());
			vo.setIdUser(pu.getIdUser());
			vo.setNameUser(nameById.getOrDefault(pu.getIdUser(), pu.getIdUser()));
			vo.setCdProjectRole(null == pu.getCdProjectRole() ? ProjectUser.ROLE_MEMBER : pu.getCdProjectRole());
			result.add(vo);
		}
		return result;
	}

	@AuditLog(bizType = OperationLog.BIZ_PROJECT, action = "调整项目成员角色")
	public void updateRole(String idProject, String idUser, String role) {
		ProjectUser pu = new ProjectUser();
		pu.setIdProject(idProject);
		pu.setIdUser(idUser);
		pu.setCdProjectRole(role);
		pu.setDtUpdate(new Date());
		projectUserMapper.updateByPrimaryKeySelective(pu);
	}

	@AuditLog(bizType = OperationLog.BIZ_PROJECT, action = "添加项目成员")
	public int addMember(String idProject, String idUser, String role) {
		ProjectUser pu = new ProjectUser(idProject, idUser, role);
		pu.setDtUpdate(new Date());
		try {
			return projectUserMapper.insert(pu);
		} catch (Exception e) {
			log.error("添加项目成员失败，project={}, user={}", idProject, idUser, e);
			return 0;
		}
	}

	@AuditLog(bizType = OperationLog.BIZ_PROJECT, action = "移除项目成员")
	public void removeMember(String idProject, String idUser) {
		projectUserMapper.deleteByPrimaryKey(idProject, idUser);
	}

	/** 项目是否至少还有一个负责人（防止把最后一个 OWNER 降成成员后项目没人管） */
	public boolean hasOtherOwner(String idProject, String excludeUser) {
		for (MemberVO m : listMembers(idProject)) {
			if (ProjectUser.ROLE_OWNER.equals(m.getCdProjectRole()) && !m.getIdUser().equals(excludeUser)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 成员 + 姓名的展示对象。单独放在这里而不是塞进 ProjectUser，
	 * 是因为姓名来自 users 表，不属于 project_user 的列。
	 */
	public static class MemberVO {
		private String idProject;
		private String idUser;
		private String nameUser;
		private String cdProjectRole;

		public String getIdProject() {
			return idProject;
		}

		public void setIdProject(String idProject) {
			this.idProject = idProject;
		}

		public String getIdUser() {
			return idUser;
		}

		public void setIdUser(String idUser) {
			this.idUser = idUser;
		}

		public String getNameUser() {
			return nameUser;
		}

		public void setNameUser(String nameUser) {
			this.nameUser = nameUser;
		}

		public String getCdProjectRole() {
			return cdProjectRole;
		}

		public void setCdProjectRole(String cdProjectRole) {
			this.cdProjectRole = cdProjectRole;
		}

		public String getRoleName() {
			if (ProjectUser.ROLE_OWNER.equals(cdProjectRole)) {
				return "项目负责人";
			}
			if (ProjectUser.ROLE_VIEWER.equals(cdProjectRole)) {
				return "只读访客";
			}
			return "项目成员";
		}
	}
}
