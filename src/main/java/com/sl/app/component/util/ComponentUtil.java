package com.sl.app.component.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.sl.config.StorageProperties;
import com.sl.entity.Menu;
import com.sl.entity.Permission;
import com.sl.entity.Project;
import com.sl.entity.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import com.vaadin.flow.server.VaadinSession;

import cn.hutool.core.io.FileUtil;

@Service
public class ComponentUtil implements ApplicationContextAware{
	
	private static Logger logger = LoggerFactory.getLogger(ComponentUtil.class);
	
	public static ApplicationContext applicationContext;

	/** 存储配置缓存，避免每次都走 getBean */
	private static StorageProperties storage;

    private static final String CLASSPATH = "com.sl.";


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	
	
	public static  User getCurrentUser(){
		VaadinSession current = VaadinSession.getCurrent();
		if (null == current || null == VaadinSession.getCurrent().getAttribute("user")) {
			return null;
		}
		return (User) VaadinSession.getCurrent().getAttribute("user");
	}
	
	public static  String getCurrentInstitutionRole(){
		return VaadinSession.getCurrent().getAttribute("CURRENT_INST_ROLE").toString();
	}
	
    /**
     * createComponent:(工厂模式生成对应的CommonComponent)；<br/>
     * className；<br/>
     * CommonComponent)；<br/>
     * 
     * @author baiguomin
     */
    public static Component createComponent(Menu menu) {
        String className = null;
        if (menu == null || menu.getNameClass() == null) {
            return null;
        } else {
            className = menu.getNameClass();
        }
        Class<?> clazz = null;
        try {
            clazz = Class.forName(CLASSPATH + className);
        } catch (ClassNotFoundException e) {
            logger.error("className:ComponentFactory,methodName:createComponent,message:{}", e);
        }

        if (clazz == null) {
            return null;
        }

        Component bean = null;
        try {
            bean = (Component) applicationContext.getBean(clazz);
        } catch (BeansException e1) {
            try {
                bean = (Component) clazz.getDeclaredConstructor().newInstance();
                return bean;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            e1.printStackTrace();
        }
        if (bean == null) {
            try {
                bean = (Component) clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return bean;
    }

    
    /**
     * createComponent:(工厂模式生成对应的CommonComponent)；<br/>
     * className；<br/>
     * CommonComponent)；<br/>
     * 
     * @author 
     */
    public static Component createComponent(String  className) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(CLASSPATH + className);
        } catch (ClassNotFoundException e) {
            logger.error("className:ComponentFactory,methodName:createComponent,message:{}", e);
        }

        if (clazz == null) {
            return null;
        }

        Component bean = null;
        try {
            bean = (Component) applicationContext.getBean(clazz);
        } catch (BeansException e1) {
            try {
                bean = (Component) clazz.getDeclaredConstructor().newInstance();
                return bean;
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
            e1.printStackTrace();
        }
        if (bean == null) {
            try {
                bean = (Component) clazz.getDeclaredConstructor().newInstance();
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }
   
	public static String getCurrentInstitution() {
		if (null == getCurrentUser()) {
			return "";
		}
		return getCurrentUser().getIdInstitution();
	}
	public static String getCurrentUserName() {
		return getCurrentUser().getUserName();
	}
	/**
	 * 配置的目录+用户+项目ID
	 * @return
	 */
	/**
	 * 配置的存储根目录 + 用户ID + 项目ID。
	 * 原先从 ResourceBundle 读 user.file.path（硬编码 d:\\），改为走 StorageProperties，
	 * 支持 profile / 环境变量覆盖，Linux 上无需改代码。
	 */
	public static String getCurrentUserFilePath() {
		Project currentProject = getCurrentProject();
		String baseDir = storageBaseDir();
		StringBuilder sb = new StringBuilder(baseDir);
		if (!baseDir.endsWith(File.separator)) {
			sb.append(File.separator);
		}
		String parentPath = sb.append(ComponentUtil.getCurrentUser().getUserId())
				.append(File.separator)
				.append(currentProject.getIdProject())
				.toString();
		try {
			FileUtil.mkdir(parentPath);
		} catch (Exception e) {
			logger.error("创建用户文件目录失败：{}", parentPath, e);
		}
		return parentPath;
	}

	/**
	 * 存储根目录，取不到配置时回退到用户主目录。
	 */
	public static String storageBaseDir() {
		StorageProperties props = storage;
		if (null == props && null != applicationContext) {
			props = applicationContext.getBean(StorageProperties.class);
			storage = props;
		}
		if (null == props) {
			return System.getProperty("user.home") + File.separator + ".workingcoo" + File.separator + "storage";
		}
		return props.getBaseDir();
	}

	/**
	 * 校验目标文件是否位于存储根目录之内，防止路径穿越。
	 */
	public static boolean isInsideStorageDir(File file) {
		if (null == file) {
			return false;
		}
		try {
			String base = new File(storageBaseDir()).getCanonicalPath();
			String target = file.getCanonicalPath();
			return target.equals(base) || target.startsWith(base + File.separator);
		} catch (Exception e) {
			logger.error("校验文件路径失败", e);
			return false;
		}
	}
	
	public static Project getCurrentProject() {
		Project currentProject = (Project)VaadinSession.getCurrent().getAttribute("project");
		return currentProject;
	}
	
	public static <T> T getSelectedItemForGrid(Grid<T> grid) {
		Set<T> selectedItems = grid.getSelectedItems();
		if (null == selectedItems || selectedItems.isEmpty()) {
			return null;
		}
		for (T t : selectedItems) {
			return t;
		}
		return null;
	}
	
	public static void populateTreeGridContent(TreeGrid<Permission> treeGrid,List<Permission> permissions) {
		TreeDataProvider<Permission> dataProvider = (TreeDataProvider<Permission>) treeGrid.getDataProvider();
		TreeData<Permission> treeData = dataProvider.getTreeData();
		for (Permission pid : permissions) {
			if (pid.getIdParent() == null) {
				treeData.addRootItems(pid);
				List<Permission> children = permissions.stream().filter(e -> e.getIdParent()!= null && e.getIdParent().equals(pid.getPermissionId())).collect(Collectors.toList());
				for (Permission cid : children) {
					treeData.addItem(pid, cid);
				}
			}
			else {
				List<Permission> children = permissions.stream().filter(e -> e.getIdParent()!= null && e.getIdParent().equals(pid.getPermissionId())).collect(Collectors.toList());
				for (Permission cid : children) {
					treeData.addItem(pid, cid);
				}
			}
		}
		// 数据装完再统一展开：原来只展开根节点，二层以下的父节点保持折叠
		for (Permission root : treeData.getRootItems()) {
			expandDeep(treeGrid, treeData, root);
		}
	}

	/**
	 * 递归展开到叶子：凡是有子节点的都展开，叶子不展开。
	 * TreeData.getChildren 对不在树里的项会抛异常，用 contains 防御。
	 */
	private static void expandDeep(TreeGrid<Permission> treeGrid, TreeData<Permission> treeData, Permission item) {
		if (!treeData.contains(item)) {
			return;
		}
		List<Permission> children = treeData.getChildren(item);
		if (null == children || children.isEmpty()) {
			return;
		}
		treeGrid.expand(item);
		for (Permission child : children) {
			expandDeep(treeGrid, treeData, child);
		}
	}
	/**
	 * 生成树结构并带有选中的ID
	 * 如果要使用该方法需实现 StructuredEntity 接口重写两个方法即可
	 * @param treeGrid
	 * @param permissions
	 */
	public static void populateTreeGridContentWithSelectItems(TreeGrid<Permission> treeGrid,List<Permission> permissions,List<String> selectedIds) {
		TreeDataProvider<Permission> dataProvider = (TreeDataProvider<Permission>) treeGrid.getDataProvider();
		TreeData<Permission> treeData = dataProvider.getTreeData();
		treeData.clear();
		permissions.forEach(p -> treeGrid.deselect(p));//要先将原来选中的全部设置为未选中，treeData.clear方法只能清除数据但是不能取消选择，可能是个bug;
		for (Permission pid : permissions) {
			if (pid.getIdParent() == null) {
				treeData.addItem(null, pid);
				if (selectedIds.contains(pid.getId())) {
					treeGrid.select(pid);
				}
				List<Permission> children = permissions.stream().filter(e -> e.getIdParent()!= null && e.getIdParent().equals(pid.getId())).collect(Collectors.toList());
				for (Permission cid : children) {
					treeData.addItem(pid, cid);
					if (selectedIds.contains(cid.getId())) {
						treeGrid.select(cid);
					}
				}
			}else {
				List<Permission> children = permissions.stream().filter(e -> e.getIdParent()!= null && e.getIdParent().equals(pid.getId())).collect(Collectors.toList());
				for (Permission cid : children) {
					treeData.addItem(pid, cid);
					if (selectedIds.contains(cid.getId())) {
						treeGrid.select(cid);
					}
				}
			}
		}
		// 数据装完统一展开到叶子（原来整个方法没有任何 expand，角色权限树每次都是全折叠的）
		for (Permission root : treeData.getRootItems()) {
			expandDeep(treeGrid, treeData, root);
		}
	}
//	public static void populateTreeGridtWithSelectItems(TreeGrid<Permission> treeGrid,List<Permission> permissions,List<String> selectedIds) {
//		 TreeData<Permission> treeData = new TreeData<>();
//		 treeGrid.setTreeData(treeData);
//		permissions.forEach(p -> treeGrid.deselect(p));//要先将原来选中的全部设置为未选中，treeData.clear方法只能清除数据但是不能取消选择，可能是个bug;
//		for (Permission pid : permissions) {
//			if (pid.getIdParent() == null) {
//				treeData.addItem(null, pid);
//				if (selectedIds.contains(pid.getId())) {
//					treeGrid.select(pid);
//				}
//				List<Permission> children = permissions.stream().filter(e -> e.getIdParent()!= null && e.getIdParent().equals(pid.getId())).collect(Collectors.toList());
//				for (Permission cid : children) {
//					treeData.addItem(pid, cid);
//					if (selectedIds.contains(cid.getId())) {
//						treeGrid.select(cid);
//					}
//				}
//			}else {
//				List<Permission> children = permissions.stream().filter(e -> e.getIdParent()!= null && e.getIdParent().equals(pid.getId())).collect(Collectors.toList());
//				for (Permission cid : children) {
//					treeData.addItem(pid, cid);
//					if (selectedIds.contains(cid.getId())) {
//						treeGrid.select(cid);
//					}
//				}
//			}
//		}
////		treeGrid.expand(permissions);
//	}
	
	/**
	 * 生成树结构并带有选中的ID
	 * @param treeGrid
	 * @param permissions
	 */
	public static void populateGridContentWithSelectItems(Grid<User> grid,List<User> users,List<String> selectedIds) {
		grid.setItems(users);
		List<User> sel = users.stream().filter(e ->selectedIds.contains(e.getUserId())).collect(Collectors.toList());
		for (User u : sel) {
			grid.select(u);
		}
	}

	public static void initComponent(CommonComponent com) {
		com.initLayout();
		com.initContent();
		com.registerHandler();
	}
    /**
     * 传入要保留的列，名字是泛型对象的属性名；
     * @param grid
     * @param visiableColumns
     */
	public static void filterGridColumns(GridPro<?> grid,List<String> visiableColumns) {
		grid.getColumns().forEach(column -> {
			String key = column.getKey();
			if (null != key && !visiableColumns.contains(key)) {
				grid.removeColumnByKey(key);
			}
		});
	}
	/**
	 * 移除不是null的列，注意：自定义的列没有key，column.getKey()是null
	 * @param grid
	 */
	public static void removeNotNullColumns(GridPro<?> grid) {
		grid.getColumns().forEach(column -> {
			String key = column.getKey();
			if (null != key) {
				grid.removeColumnByKey(key);
			}
		});
	}
	
	public static void filterCurdColumns(Grid<?> grid,List<String> visiableColumns) {
		grid.getColumns().forEach(column -> {
			String key = column.getKey();
			if (!visiableColumns.contains(key)) {
				grid.removeColumnByKey(key);
			}
		});
	}
}
