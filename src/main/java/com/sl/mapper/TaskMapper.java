package com.sl.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sl.entity.Task;
import com.sl.entity.TaskExample;

public interface TaskMapper  extends BaseMapper<Task>{
    int deleteByExample(TaskExample example);

    int deleteByPrimaryKey(String idTask);

    int insert(Task record);

    int insertSelective(Task record);

    List<Task> selectByExample(TaskExample example);

    Task selectByPrimaryKey(String idTask);

    int updateByExampleSelective(@Param("record") Task record, @Param("example") TaskExample example);

    int updateByExample(@Param("record") Task record, @Param("example") TaskExample example);

    int updateByPrimaryKeySelective(Task record);

    int updateByPrimaryKey(Task record);

    int insertBatch(List<Task> records);
    
    /** 任务创建趋势；传 null 表示不限项目 */
    List<Map<String, Object>> getTaskTrending(@Param("idProject") String idProject);

    /** 分页查询任务（Grid 懒加载用）；viewUserId 非空时，该用户自己的私有任务也可见 */
    List<Task> selectTaskPage(@Param("idProject") String idProject,
                              @Param("flagPrivate") String flagPrivate,
                              @Param("viewUserId") String viewUserId,
                              @Param("taskIds") List<String> taskIds,
                              @Param("cdTaskType") String cdTaskType,
                              @Param("cdTag") String cdTag,
                              @Param("cdStatus") String cdStatus,
                              @Param("cdTaskGroup") String cdTaskGroup,
                              @Param("nameTask") String nameTask,
                              @Param("idUser") String idUser,
                              @Param("exeUser") String exeUser,
                              @Param("offset") int offset,
                              @Param("limit") int limit);

    long countTask(@Param("idProject") String idProject,
                   @Param("flagPrivate") String flagPrivate,
                   @Param("viewUserId") String viewUserId,
                   @Param("taskIds") List<String> taskIds,
                   @Param("cdTaskType") String cdTaskType,
                   @Param("cdTag") String cdTag,
                   @Param("cdStatus") String cdStatus,
                   @Param("cdTaskGroup") String cdTaskGroup,
                   @Param("nameTask") String nameTask,
                              @Param("idUser") String idUser,
                              @Param("exeUser") String exeUser);

    /**
     * 到期提醒用：落在 [from, to] 区间内、且还没结束的任务，连出执行人。
     * 一次 SQL 出「任务 + 执行人」，避免调度器里逐任务再查 task_user。
     */
    List<Map<String, Object>> selectDueTaskExecutors(@Param("from") Date from, @Param("to") Date to);
}