package com.sl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sl.entity.Schedule;
import com.sl.entity.ScheduleExample;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ScheduleMapper extends BaseMapper<Schedule>{
    int deleteByExample(ScheduleExample example);

    int deleteByPrimaryKey(@Param("idUser") String idUser, @Param("idSchedule") String idSchedule);

    int insert(Schedule record);

    int insertSelective(Schedule record);

    List<Schedule> selectByExample(ScheduleExample example);

    Schedule selectByPrimaryKey(@Param("idUser") String idUser, @Param("idSchedule") String idSchedule);

    int updateByExampleSelective(@Param("record") Schedule record, @Param("example") ScheduleExample example);

    int updateByExample(@Param("record") Schedule record, @Param("example") ScheduleExample example);

    int updateByPrimaryKeySelective(Schedule record);

    int updateByPrimaryKey(Schedule record);

    /**
     * T2-11：编辑日程的专用全字段更新。
     * 与 updateByPrimaryKeySelective 的区别：null 字段也会写入（用于清空
     * DT_END / CD_NATURE / NBR_REMIND_MINUTES），否则「全天→非全天」「提醒→不提醒」
     * 这类把字段置空的修改会被 selective 跳过，DB 残留旧值导致回显错乱。
     * 不更新 TIMESTAMP 列，保留 ON UPDATE CURRENT_TIMESTAMP 的自动刷新。
     */
    int updateScheduleEditable(Schedule record);

    int insertBatch(List<Schedule> records);

    /**
     * T2-11：待提醒的日程 —— 起始时间落在「now ~ now + 该日程自己的提前量」窗口内。
     * @param now 当前时间
     */
    List<Schedule> selectPendingRemind(@Param("now") java.util.Date now);

    /**
     * T2-11：标记某条日程已提醒，避免重复推送。
     */
    int markReminded(@Param("idUser") String idUser, @Param("idSchedule") String idSchedule);
}