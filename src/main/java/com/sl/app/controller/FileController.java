package com.sl.app.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sl.entity.ProjectTaskFile;
import com.sl.entity.ProjectUser;
import com.sl.entity.User;
import com.sl.service.ProjectProcess;
import com.sl.user.UserManagementEnums;

import cn.hutool.system.SystemUtil;

/**
 * 文件下载接口
 * @author Administrator
 *
 */
@Controller
@RequestMapping("file")
public class FileController {

	private static final Logger log = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private ProjectProcess projectProcess;
	// 用来处理文件下载
	// 请求对应响应输出流${pageContext.request.contextPath}/file/download?fileName=aa.txt
//	调用方式：Anchor an = new Anchor("/file/download?fileId="+p.getIdFile(), "下载", AnchorTarget.BLANK);点击A标签即可下载
	/**
	 * // 4. 附件下载 attachment 附件 inline 在线打开(默认值) // 在线打开
	 * response.setHeader("content-disposition", inline + ";fileName=" + fileName);
	 * // 附件下载 response.setHeader("content-disposition", attachment + ";fileName=" +
	 * fileName);
	 * 
	 * @param fileName
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("download")
	public void download(String fileId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		serve(fileId, response, false);
	}

	/**
	 * 在线预览（S6）：与下载同一路径/鉴权，仅 Content-Disposition 改为 inline，
	 * Content-Type 按扩展名给出，图片/PDF 可在浏览器内直接渲染。
	 */
	@RequestMapping("preview")
	public void preview(String fileId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		serve(fileId, response, true);
	}

	/**
	 * 文件输出公共逻辑。inline=false 下载（attachment），true 预览（inline）。
	 */
	private void serve(String fileId, HttpServletResponse response, boolean inline) throws IOException {
		// 1. 登录校验：Shiro 过滤链是全放行（/** = anon），这里必须自己鉴权，
		//    否则任何人只要拿到 fileId 就能把文件拖走。
		User currentUser = getCurrentUser();
		if (null == currentUser) {
			sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "请先登录后再访问文件");
			return;
		}
		ProjectTaskFile projectFileByFileId = projectProcess.getProjectFileByFileId(fileId);
		if (null == projectFileByFileId) {
			sendError(response, HttpServletResponse.SC_NOT_FOUND, "文件记录不存在");
			return;
		}
		// 2. 权限校验：项目成员、上传者本人，或具备查看全部用户权限的管理员
		if (!canDownload(projectFileByFileId, currentUser)) {
			log.warn("用户 {} 尝试越权访问文件 {}", currentUser.getUserId(), fileId);
			sendError(response, HttpServletResponse.SC_FORBIDDEN, "无权访问该文件");
			return;
		}
		// 3. 路径净化：文件名只取最后一段，杜绝 ../../ 形式的路径穿越
		File dir = new File(projectFileByFileId.getCdFilePath());
		String safeName = FilenameUtils.getName(projectFileByFileId.getNameFile());
		File targetFile = new File(dir, safeName);
		try {
			if (!targetFile.exists() || !isUnder(targetFile, dir)) {
				sendError(response, HttpServletResponse.SC_NOT_FOUND, "文件不存在或路径非法");
				return;
			}
		} catch (IOException e) {
			log.error("校验文件路径失败，fileId={}", fileId, e);
			sendError(response, HttpServletResponse.SC_BAD_REQUEST, "文件路径非法");
			return;
		}
		FileInputStream is = null;
		ServletOutputStream os = null;
		try {
			// 4. 通过文件输入流读取文件
			is = new FileInputStream(targetFile);
			// 5. 获取响应输出流
			// 预览时按扩展名给出真实类型（image/png、application/pdf...），
			// 浏览器才能内嵌渲染；下载统一 octet-stream
			if (inline) {
				String contentType = guessContentType(safeName);
				response.setContentType(contentType + ";charset=UTF-8");
			} else {
				response.setContentType("application/octet-stream;charset=UTF-8");
			}
			String disposition = inline ? "inline" : "attachment";
			// 中文文件名按 RFC 5987 编码，避免下载/预览文件名乱码
			String encoded = URLEncoder.encode(safeName, StandardCharsets.UTF_8).replace("+", "%20");
			response.setHeader("content-disposition",
					disposition + "; fileName=\"" + encoded + "\"; filename*=UTF-8''" + encoded);

			// 6. 处理下载流复制
			os = response.getOutputStream();
			int len;
			byte[] b = new byte[1024];
			while (true) {
				len = is.read(b);
				if (len == -1)
					break;
				os.write(b, 0, len);
			}

		} catch (IOException e) {
			log.error("输出文件失败，fileId={}, inline={}", fileId, inline, e);
		} finally {
			// 释放资源
			if (null != os) {
				os.close();
			}
			if (null != is) {
				is.close();
			}
		}
	}

	/** 按扩展名猜 Content-Type；猜不出按 octet-stream（浏览器会转成下载） */
	private String guessContentType(String name) {
		String guessed = java.net.URLConnection.guessContentTypeFromName(name);
		if (null != guessed) {
			return guessed;
		}
		String ext = FilenameUtils.getExtension(name).toLowerCase();
		return switch (ext) {
			case "png" -> "image/png";
			case "jpg", "jpeg" -> "image/jpeg";
			case "gif" -> "image/gif";
			case "webp" -> "image/webp";
			case "bmp" -> "image/bmp";
			case "svg" -> "image/svg+xml";
			case "pdf" -> "application/pdf";
			default -> "application/octet-stream";
		};
	}
	/**
	 * 当前登录用户。下载请求不是 Vaadin 请求，VaadinSession 未必可用，
	 * 因此优先取 Shiro 的 principal（登录时放入的 User 对象）。
	 */
	private User getCurrentUser() {
		try {
			Subject subject = SecurityUtils.getSubject();
			if (null != subject) {
				Object principal = subject.getPrincipal();
				if (principal instanceof User user) {
					return user;
				}
			}
		} catch (Exception e) {
			log.warn("获取当前登录用户失败", e);
		}
		return null;
	}

	/**
	 * 判断当前用户能否下载该文件：项目成员、上传者本人，或管理员。
	 */
	private boolean canDownload(ProjectTaskFile file, User user) {
		if (null == file || null == user) {
			return false;
		}
		try {
			if (SecurityUtils.getSubject().isPermitted(UserManagementEnums.CHECKED_ALL_USER)) {
				return true;
			}
		} catch (Exception e) {
			log.warn("权限校验失败，按普通用户处理", e);
		}
		if (user.getUserId().equals(file.getIdUser())) {
			return true;
		}
		if (null == file.getIdProject()) {
			return false;
		}
		List<ProjectUser> members = projectProcess.getProjectUserListByIdProject(file.getIdProject());
		if (null == members) {
			return false;
		}
		return members.stream().anyMatch(m -> user.getUserId().equals(m.getIdUser()));
	}

	/**
	 * 校验目标文件确实位于给定目录之内（防止 ../ 穿越）。
	 */
	private boolean isUnder(File target, File dir) throws IOException {
		String dirPath = dir.getCanonicalPath();
		String targetPath = target.getCanonicalPath();
		return targetPath.equals(dirPath) || targetPath.startsWith(dirPath + File.separator);
	}

	/**
	 * 统一返回错误响应，避免原来「文件不存在也返回 200 空响应」的情况。
	 */
	private void sendError(HttpServletResponse response, int status, String message) {
		try {
			response.reset();
			response.setStatus(status);
			response.setContentType("text/plain;charset=UTF-8");
			response.getWriter().write(message);
			response.getWriter().flush();
		} catch (IOException e) {
			log.error("写入错误响应失败", e);
		}
	}

	/**
	 * 第二种下载文件的方法
	 * @param fileName
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/download2")
    public ResponseEntity<byte[]> test(String fileName, HttpServletRequest request) throws IOException{
		// 该接口直接接收文件名，原先既无登录校验也无路径净化，风险比 download 更大，一并加固
		User currentUser = getCurrentUser();
		if (null == currentUser) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		String safeName = FilenameUtils.getName(fileName);
		File dir = new File(System.getProperty("user.home"), ".workingcoo/demo");
		File f = new File(dir, safeName);
		if (!f.exists() || !isUnder(f, dir)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		//创建http头信息的对象
        HttpHeaders header=new HttpHeaders();
        //标记以流的方式做出响应
        header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String encoded = URLEncoder.encode(safeName, StandardCharsets.UTF_8).replace("+", "%20");
        header.add("content-disposition", "attachment; fileName=\"" + encoded + "\"; filename*=UTF-8''" + encoded);
        return new ResponseEntity<byte[]>
                        (FileUtils.readFileToByteArray(f), header, HttpStatus.OK);
    }

	@RequestMapping("test")
	public String test() {
		return "success";
	}
}
