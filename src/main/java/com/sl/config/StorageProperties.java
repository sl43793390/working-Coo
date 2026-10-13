package com.sl.config;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 文件存储相关配置。
 *
 * 背景：原先上传根目录写死在 application.properties 的 user.file.path=d:\\
 * 且用 ResourceBundle 读取（绕过了 Spring 的环境抽象，环境变量 / profile 覆盖都失效），
 * 部署到 Linux 上必然失败。改为这里统一托管，默认值跨平台。
 */
@Component
@ConfigurationProperties(prefix = "app.storage")
public class StorageProperties {

	/**
	 * 上传文件存放的根目录。默认放在当前用户主目录下，Windows / Linux 都能直接跑。
	 * 生产环境建议通过 app.storage.base-dir 或环境变量 APP_STORAGE_DIR 显式指定。
	 */
	private String baseDir = System.getProperty("user.home") + File.separator + ".workingcoo"
			+ File.separator + "storage";

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

	/**
	 * 归一化后的根目录（去掉结尾的分隔符），便于做「文件必须位于根目录之内」的前缀校验。
	 */
	public String normalizedBaseDir() {
		if (null == baseDir || baseDir.isBlank()) {
			return new File(System.getProperty("user.home")).getAbsolutePath();
		}
		return new File(baseDir.trim()).getAbsolutePath();
	}
}
