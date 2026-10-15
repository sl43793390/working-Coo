package com.sl.config;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 启动时校验数据库表结构（S2）。
 * <p>
 * 背景：institutions.sql 是唯一权威脚本且不做自动迁移，历史上出现过
 * 「代码先于脚本」——task_dependency / project_milestone / notification /
 * operation_log 四张表有实体有 Mapper，脚本里却没有，部署新环境直接缺表报错。
 * <p>
 * 这里在启动后用 DatabaseMetaData 查一遍核心表，缺表时打 ERROR 级日志
 * 提示去执行脚本里对应的 DDL，绝不阻断启动（应用其余功能可能还能用）。
 */
@Component
public class SchemaChecker implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(SchemaChecker.class);

	/** 应用运行必需的表 → 缺失时的修复提示 */
	private static final List<String[]> REQUIRED_TABLES = List.of(
			new String[] { "institutions", "institutions.sql 全量脚本" },
			new String[] { "menu", "institutions.sql 全量脚本" },
			new String[] { "permissions", "institutions.sql 全量脚本" },
			new String[] { "roles", "institutions.sql 全量脚本" },
			new String[] { "users", "institutions.sql 全量脚本" },
			new String[] { "projects", "institutions.sql 全量脚本" },
			new String[] { "task", "institutions.sql 全量脚本" },
			new String[] { "task_user", "institutions.sql 全量脚本" },
			new String[] { "task_comment", "institutions.sql 全量脚本" },
			new String[] { "task_subtask", "institutions.sql 全量脚本" },
			new String[] { "task_dependency", "institutions.sql 尾部「结构补齐」段" },
			new String[] { "project_milestone", "institutions.sql 尾部「结构补齐」段" },
			new String[] { "notification", "institutions.sql 尾部「结构补齐」段" },
			new String[] { "operation_log", "institutions.sql 尾部「结构补齐」段" },
			new String[] { "task_checklist_item", "institutions.sql 尾部「结构补齐」段" });

	@Override
	public void run(ApplicationArguments args) throws Exception {
		try (Connection conn = dataSource.getConnection()) {
			DatabaseMetaData meta = conn.getMetaData();
			List<String> missing = new ArrayList<>();
			for (String[] table : REQUIRED_TABLES) {
				if (!tableExists(meta, conn.getCatalog(), table[0])) {
					missing.add(table[0] + "（修复脚本：" + table[1] + "）");
				}
			}
			if (missing.isEmpty()) {
				log.info("数据库表结构校验通过，共 {} 张核心表", REQUIRED_TABLES.size());
				return;
			}
			log.error("数据库缺表 {} 张：{}。对应功能会报错，请按 institutions.sql 补齐后重启。",
					missing.size(), String.join("；", missing));
		} catch (Exception e) {
			// 连不上库等情况交给 HikariCP/启动流程自身的报错，这里不重复吵
			log.warn("数据库表结构校验未执行：{}", e.getMessage());
		}
	}

	private boolean tableExists(DatabaseMetaData meta, String catalog, String tableName) throws Exception {
		try (ResultSet rs = meta.getTables(catalog, null, tableName, new String[] { "TABLE" })) {
			if (rs.next()) {
				return true;
			}
		}
		// 部分 MySQL 驱动/配置下大小写敏感，再按小写查一次
		try (ResultSet rs = meta.getTables(catalog, null, tableName.toLowerCase(), new String[] { "TABLE" })) {
			return rs.next();
		}
	}

	private final DataSource dataSource;

	public SchemaChecker(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
