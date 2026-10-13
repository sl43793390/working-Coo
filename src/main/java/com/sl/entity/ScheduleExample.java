package com.sl.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    protected Long offset;

    protected Long limit;

    protected Long end;

    public ScheduleExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setPagination(long offset, long limit) {
        this.offset = offset;
        this.limit = limit;
        this.end = offset + limit;
    }

    protected abstract static class GeneratedCriteria implements Serializable {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdUserIsNull() {
            addCriterion("ID_USER is null");
            return (Criteria) this;
        }

        public Criteria andIdUserIsNotNull() {
            addCriterion("ID_USER is not null");
            return (Criteria) this;
        }

        public Criteria andIdUserEqualTo(String value) {
            addCriterion("ID_USER =", value, "idUser");
            return (Criteria) this;
        }

        public Criteria andIdUserNotEqualTo(String value) {
            addCriterion("ID_USER <>", value, "idUser");
            return (Criteria) this;
        }

        public Criteria andIdUserGreaterThan(String value) {
            addCriterion("ID_USER >", value, "idUser");
            return (Criteria) this;
        }

        public Criteria andIdUserGreaterThanOrEqualTo(String value) {
            addCriterion("ID_USER >=", value, "idUser");
            return (Criteria) this;
        }

        public Criteria andIdUserLessThan(String value) {
            addCriterion("ID_USER <", value, "idUser");
            return (Criteria) this;
        }

        public Criteria andIdUserLessThanOrEqualTo(String value) {
            addCriterion("ID_USER <=", value, "idUser");
            return (Criteria) this;
        }

        public Criteria andIdUserLike(String value) {
            addCriterion("ID_USER like", value, "idUser");
            return (Criteria) this;
        }

        public Criteria andIdUserNotLike(String value) {
            addCriterion("ID_USER not like", value, "idUser");
            return (Criteria) this;
        }

        public Criteria andIdUserIn(List<String> values) {
            addCriterion("ID_USER in", values, "idUser");
            return (Criteria) this;
        }

        public Criteria andIdUserNotIn(List<String> values) {
            addCriterion("ID_USER not in", values, "idUser");
            return (Criteria) this;
        }

        public Criteria andIdUserBetween(String value1, String value2) {
            addCriterion("ID_USER between", value1, value2, "idUser");
            return (Criteria) this;
        }

        public Criteria andIdUserNotBetween(String value1, String value2) {
            addCriterion("ID_USER not between", value1, value2, "idUser");
            return (Criteria) this;
        }

        public Criteria andIdScheduleIsNull() {
            addCriterion("ID_SCHEDULE is null");
            return (Criteria) this;
        }

        public Criteria andIdScheduleIsNotNull() {
            addCriterion("ID_SCHEDULE is not null");
            return (Criteria) this;
        }

        public Criteria andIdScheduleEqualTo(String value) {
            addCriterion("ID_SCHEDULE =", value, "idSchedule");
            return (Criteria) this;
        }

        public Criteria andIdScheduleNotEqualTo(String value) {
            addCriterion("ID_SCHEDULE <>", value, "idSchedule");
            return (Criteria) this;
        }

        public Criteria andIdScheduleGreaterThan(String value) {
            addCriterion("ID_SCHEDULE >", value, "idSchedule");
            return (Criteria) this;
        }

        public Criteria andIdScheduleGreaterThanOrEqualTo(String value) {
            addCriterion("ID_SCHEDULE >=", value, "idSchedule");
            return (Criteria) this;
        }

        public Criteria andIdScheduleLessThan(String value) {
            addCriterion("ID_SCHEDULE <", value, "idSchedule");
            return (Criteria) this;
        }

        public Criteria andIdScheduleLessThanOrEqualTo(String value) {
            addCriterion("ID_SCHEDULE <=", value, "idSchedule");
            return (Criteria) this;
        }

        public Criteria andIdScheduleLike(String value) {
            addCriterion("ID_SCHEDULE like", value, "idSchedule");
            return (Criteria) this;
        }

        public Criteria andIdScheduleNotLike(String value) {
            addCriterion("ID_SCHEDULE not like", value, "idSchedule");
            return (Criteria) this;
        }

        public Criteria andIdScheduleIn(List<String> values) {
            addCriterion("ID_SCHEDULE in", values, "idSchedule");
            return (Criteria) this;
        }

        public Criteria andIdScheduleNotIn(List<String> values) {
            addCriterion("ID_SCHEDULE not in", values, "idSchedule");
            return (Criteria) this;
        }

        public Criteria andIdScheduleBetween(String value1, String value2) {
            addCriterion("ID_SCHEDULE between", value1, value2, "idSchedule");
            return (Criteria) this;
        }

        public Criteria andIdScheduleNotBetween(String value1, String value2) {
            addCriterion("ID_SCHEDULE not between", value1, value2, "idSchedule");
            return (Criteria) this;
        }

        public Criteria andDtScheduleIsNull() {
            addCriterion("DT_SCHEDULE is null");
            return (Criteria) this;
        }

        public Criteria andDtScheduleIsNotNull() {
            addCriterion("DT_SCHEDULE is not null");
            return (Criteria) this;
        }

        public Criteria andDtScheduleEqualTo(Date value) {
            addCriterion("DT_SCHEDULE =", value, "dtSchedule");
            return (Criteria) this;
        }

        public Criteria andDtScheduleNotEqualTo(Date value) {
            addCriterion("DT_SCHEDULE <>", value, "dtSchedule");
            return (Criteria) this;
        }

        public Criteria andDtScheduleGreaterThan(Date value) {
            addCriterion("DT_SCHEDULE >", value, "dtSchedule");
            return (Criteria) this;
        }

        public Criteria andDtScheduleGreaterThanOrEqualTo(Date value) {
            addCriterion("DT_SCHEDULE >=", value, "dtSchedule");
            return (Criteria) this;
        }

        public Criteria andDtScheduleLessThan(Date value) {
            addCriterion("DT_SCHEDULE <", value, "dtSchedule");
            return (Criteria) this;
        }

        public Criteria andDtScheduleLessThanOrEqualTo(Date value) {
            addCriterion("DT_SCHEDULE <=", value, "dtSchedule");
            return (Criteria) this;
        }

        public Criteria andDtScheduleIn(List<Date> values) {
            addCriterion("DT_SCHEDULE in", values, "dtSchedule");
            return (Criteria) this;
        }

        public Criteria andDtScheduleNotIn(List<Date> values) {
            addCriterion("DT_SCHEDULE not in", values, "dtSchedule");
            return (Criteria) this;
        }

        public Criteria andDtScheduleBetween(Date value1, Date value2) {
            addCriterion("DT_SCHEDULE between", value1, value2, "dtSchedule");
            return (Criteria) this;
        }

        public Criteria andDtScheduleNotBetween(Date value1, Date value2) {
            addCriterion("DT_SCHEDULE not between", value1, value2, "dtSchedule");
            return (Criteria) this;
        }

        public Criteria andDtEndIsNull() {
            addCriterion("DT_END is null");
            return (Criteria) this;
        }

        public Criteria andDtEndIsNotNull() {
            addCriterion("DT_END is not null");
            return (Criteria) this;
        }

        public Criteria andDtEndEqualTo(Date value) {
            addCriterion("DT_END =", value, "dtEnd");
            return (Criteria) this;
        }

        public Criteria andDtEndNotEqualTo(Date value) {
            addCriterion("DT_END <>", value, "dtEnd");
            return (Criteria) this;
        }

        public Criteria andDtEndGreaterThan(Date value) {
            addCriterion("DT_END >", value, "dtEnd");
            return (Criteria) this;
        }

        public Criteria andDtEndGreaterThanOrEqualTo(Date value) {
            addCriterion("DT_END >=", value, "dtEnd");
            return (Criteria) this;
        }

        public Criteria andDtEndLessThan(Date value) {
            addCriterion("DT_END <", value, "dtEnd");
            return (Criteria) this;
        }

        public Criteria andDtEndLessThanOrEqualTo(Date value) {
            addCriterion("DT_END <=", value, "dtEnd");
            return (Criteria) this;
        }

        public Criteria andDtEndIn(List<Date> values) {
            addCriterion("DT_END in", values, "dtEnd");
            return (Criteria) this;
        }

        public Criteria andDtEndNotIn(List<Date> values) {
            addCriterion("DT_END not in", values, "dtEnd");
            return (Criteria) this;
        }

        public Criteria andDtEndBetween(Date value1, Date value2) {
            addCriterion("DT_END between", value1, value2, "dtEnd");
            return (Criteria) this;
        }

        public Criteria andDtEndNotBetween(Date value1, Date value2) {
            addCriterion("DT_END not between", value1, value2, "dtEnd");
            return (Criteria) this;
        }

        public Criteria andCdNatureIsNull() {
            addCriterion("CD_NATURE is null");
            return (Criteria) this;
        }

        public Criteria andCdNatureIsNotNull() {
            addCriterion("CD_NATURE is not null");
            return (Criteria) this;
        }

        public Criteria andCdNatureEqualTo(String value) {
            addCriterion("CD_NATURE =", value, "cdNature");
            return (Criteria) this;
        }

        public Criteria andCdNatureNotEqualTo(String value) {
            addCriterion("CD_NATURE <>", value, "cdNature");
            return (Criteria) this;
        }

        public Criteria andCdNatureGreaterThan(String value) {
            addCriterion("CD_NATURE >", value, "cdNature");
            return (Criteria) this;
        }

        public Criteria andCdNatureGreaterThanOrEqualTo(String value) {
            addCriterion("CD_NATURE >=", value, "cdNature");
            return (Criteria) this;
        }

        public Criteria andCdNatureLessThan(String value) {
            addCriterion("CD_NATURE <", value, "cdNature");
            return (Criteria) this;
        }

        public Criteria andCdNatureLessThanOrEqualTo(String value) {
            addCriterion("CD_NATURE <=", value, "cdNature");
            return (Criteria) this;
        }

        public Criteria andCdNatureLike(String value) {
            addCriterion("CD_NATURE like", value, "cdNature");
            return (Criteria) this;
        }

        public Criteria andCdNatureNotLike(String value) {
            addCriterion("CD_NATURE not like", value, "cdNature");
            return (Criteria) this;
        }

        public Criteria andCdNatureIn(List<String> values) {
            addCriterion("CD_NATURE in", values, "cdNature");
            return (Criteria) this;
        }

        public Criteria andCdNatureNotIn(List<String> values) {
            addCriterion("CD_NATURE not in", values, "cdNature");
            return (Criteria) this;
        }

        public Criteria andCdNatureBetween(String value1, String value2) {
            addCriterion("CD_NATURE between", value1, value2, "cdNature");
            return (Criteria) this;
        }

        public Criteria andCdNatureNotBetween(String value1, String value2) {
            addCriterion("CD_NATURE not between", value1, value2, "cdNature");
            return (Criteria) this;
        }

        public Criteria andNameScheduleIsNull() {
            addCriterion("NAME_SCHEDULE is null");
            return (Criteria) this;
        }

        public Criteria andNameScheduleIsNotNull() {
            addCriterion("NAME_SCHEDULE is not null");
            return (Criteria) this;
        }

        public Criteria andNameScheduleEqualTo(String value) {
            addCriterion("NAME_SCHEDULE =", value, "nameSchedule");
            return (Criteria) this;
        }

        public Criteria andNameScheduleNotEqualTo(String value) {
            addCriterion("NAME_SCHEDULE <>", value, "nameSchedule");
            return (Criteria) this;
        }

        public Criteria andNameScheduleGreaterThan(String value) {
            addCriterion("NAME_SCHEDULE >", value, "nameSchedule");
            return (Criteria) this;
        }

        public Criteria andNameScheduleGreaterThanOrEqualTo(String value) {
            addCriterion("NAME_SCHEDULE >=", value, "nameSchedule");
            return (Criteria) this;
        }

        public Criteria andNameScheduleLessThan(String value) {
            addCriterion("NAME_SCHEDULE <", value, "nameSchedule");
            return (Criteria) this;
        }

        public Criteria andNameScheduleLessThanOrEqualTo(String value) {
            addCriterion("NAME_SCHEDULE <=", value, "nameSchedule");
            return (Criteria) this;
        }

        public Criteria andNameScheduleLike(String value) {
            addCriterion("NAME_SCHEDULE like", value, "nameSchedule");
            return (Criteria) this;
        }

        public Criteria andNameScheduleNotLike(String value) {
            addCriterion("NAME_SCHEDULE not like", value, "nameSchedule");
            return (Criteria) this;
        }

        public Criteria andNameScheduleIn(List<String> values) {
            addCriterion("NAME_SCHEDULE in", values, "nameSchedule");
            return (Criteria) this;
        }

        public Criteria andNameScheduleNotIn(List<String> values) {
            addCriterion("NAME_SCHEDULE not in", values, "nameSchedule");
            return (Criteria) this;
        }

        public Criteria andNameScheduleBetween(String value1, String value2) {
            addCriterion("NAME_SCHEDULE between", value1, value2, "nameSchedule");
            return (Criteria) this;
        }

        public Criteria andNameScheduleNotBetween(String value1, String value2) {
            addCriterion("NAME_SCHEDULE not between", value1, value2, "nameSchedule");
            return (Criteria) this;
        }

        public Criteria andCommentIsNull() {
            addCriterion("COMMENT is null");
            return (Criteria) this;
        }

        public Criteria andCommentIsNotNull() {
            addCriterion("COMMENT is not null");
            return (Criteria) this;
        }

        public Criteria andCommentEqualTo(String value) {
            addCriterion("COMMENT =", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotEqualTo(String value) {
            addCriterion("COMMENT <>", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentGreaterThan(String value) {
            addCriterion("COMMENT >", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentGreaterThanOrEqualTo(String value) {
            addCriterion("COMMENT >=", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentLessThan(String value) {
            addCriterion("COMMENT <", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentLessThanOrEqualTo(String value) {
            addCriterion("COMMENT <=", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentLike(String value) {
            addCriterion("COMMENT like", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotLike(String value) {
            addCriterion("COMMENT not like", value, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentIn(List<String> values) {
            addCriterion("COMMENT in", values, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotIn(List<String> values) {
            addCriterion("COMMENT not in", values, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentBetween(String value1, String value2) {
            addCriterion("COMMENT between", value1, value2, "comment");
            return (Criteria) this;
        }

        public Criteria andCommentNotBetween(String value1, String value2) {
            addCriterion("COMMENT not between", value1, value2, "comment");
            return (Criteria) this;
        }

        public Criteria andTimestampIsNull() {
            addCriterion("TIMESTAMP is null");
            return (Criteria) this;
        }

        public Criteria andTimestampIsNotNull() {
            addCriterion("TIMESTAMP is not null");
            return (Criteria) this;
        }

        public Criteria andTimestampEqualTo(Date value) {
            addCriterion("TIMESTAMP =", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampNotEqualTo(Date value) {
            addCriterion("TIMESTAMP <>", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampGreaterThan(Date value) {
            addCriterion("TIMESTAMP >", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampGreaterThanOrEqualTo(Date value) {
            addCriterion("TIMESTAMP >=", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampLessThan(Date value) {
            addCriterion("TIMESTAMP <", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampLessThanOrEqualTo(Date value) {
            addCriterion("TIMESTAMP <=", value, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampIn(List<Date> values) {
            addCriterion("TIMESTAMP in", values, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampNotIn(List<Date> values) {
            addCriterion("TIMESTAMP not in", values, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampBetween(Date value1, Date value2) {
            addCriterion("TIMESTAMP between", value1, value2, "timestamp");
            return (Criteria) this;
        }

        public Criteria andTimestampNotBetween(Date value1, Date value2) {
            addCriterion("TIMESTAMP not between", value1, value2, "timestamp");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria implements Serializable {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion implements Serializable {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}