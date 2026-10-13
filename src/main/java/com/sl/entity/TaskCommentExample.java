package com.sl.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskCommentExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    protected Long offset;

    protected Long limit;

    protected Long end;

    public TaskCommentExample() {
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

        public Criteria andIdTaskIsNull() {
            addCriterion("ID_TASK is null");
            return (Criteria) this;
        }

        public Criteria andIdTaskIsNotNull() {
            addCriterion("ID_TASK is not null");
            return (Criteria) this;
        }

        public Criteria andIdTaskEqualTo(String value) {
            addCriterion("ID_TASK =", value, "idTask");
            return (Criteria) this;
        }

        public Criteria andIdTaskNotEqualTo(String value) {
            addCriterion("ID_TASK <>", value, "idTask");
            return (Criteria) this;
        }

        public Criteria andIdTaskGreaterThan(String value) {
            addCriterion("ID_TASK >", value, "idTask");
            return (Criteria) this;
        }

        public Criteria andIdTaskGreaterThanOrEqualTo(String value) {
            addCriterion("ID_TASK >=", value, "idTask");
            return (Criteria) this;
        }

        public Criteria andIdTaskLessThan(String value) {
            addCriterion("ID_TASK <", value, "idTask");
            return (Criteria) this;
        }

        public Criteria andIdTaskLessThanOrEqualTo(String value) {
            addCriterion("ID_TASK <=", value, "idTask");
            return (Criteria) this;
        }

        public Criteria andIdTaskLike(String value) {
            addCriterion("ID_TASK like", value, "idTask");
            return (Criteria) this;
        }

        public Criteria andIdTaskNotLike(String value) {
            addCriterion("ID_TASK not like", value, "idTask");
            return (Criteria) this;
        }

        public Criteria andIdTaskIn(List<String> values) {
            addCriterion("ID_TASK in", values, "idTask");
            return (Criteria) this;
        }

        public Criteria andIdTaskNotIn(List<String> values) {
            addCriterion("ID_TASK not in", values, "idTask");
            return (Criteria) this;
        }

        public Criteria andIdTaskBetween(String value1, String value2) {
            addCriterion("ID_TASK between", value1, value2, "idTask");
            return (Criteria) this;
        }

        public Criteria andIdTaskNotBetween(String value1, String value2) {
            addCriterion("ID_TASK not between", value1, value2, "idTask");
            return (Criteria) this;
        }

        public Criteria andDtCommentIsNull() {
            addCriterion("DT_COMMENT is null");
            return (Criteria) this;
        }

        public Criteria andDtCommentIsNotNull() {
            addCriterion("DT_COMMENT is not null");
            return (Criteria) this;
        }

        public Criteria andDtCommentEqualTo(Date value) {
            addCriterion("DT_COMMENT =", value, "dtComment");
            return (Criteria) this;
        }

        public Criteria andDtCommentNotEqualTo(Date value) {
            addCriterion("DT_COMMENT <>", value, "dtComment");
            return (Criteria) this;
        }

        public Criteria andDtCommentGreaterThan(Date value) {
            addCriterion("DT_COMMENT >", value, "dtComment");
            return (Criteria) this;
        }

        public Criteria andDtCommentGreaterThanOrEqualTo(Date value) {
            addCriterion("DT_COMMENT >=", value, "dtComment");
            return (Criteria) this;
        }

        public Criteria andDtCommentLessThan(Date value) {
            addCriterion("DT_COMMENT <", value, "dtComment");
            return (Criteria) this;
        }

        public Criteria andDtCommentLessThanOrEqualTo(Date value) {
            addCriterion("DT_COMMENT <=", value, "dtComment");
            return (Criteria) this;
        }

        public Criteria andDtCommentIn(List<Date> values) {
            addCriterion("DT_COMMENT in", values, "dtComment");
            return (Criteria) this;
        }

        public Criteria andDtCommentNotIn(List<Date> values) {
            addCriterion("DT_COMMENT not in", values, "dtComment");
            return (Criteria) this;
        }

        public Criteria andDtCommentBetween(Date value1, Date value2) {
            addCriterion("DT_COMMENT between", value1, value2, "dtComment");
            return (Criteria) this;
        }

        public Criteria andDtCommentNotBetween(Date value1, Date value2) {
            addCriterion("DT_COMMENT not between", value1, value2, "dtComment");
            return (Criteria) this;
        }

        public Criteria andCommentUserIsNull() {
            addCriterion("COMMENT_USER is null");
            return (Criteria) this;
        }

        public Criteria andCommentUserIsNotNull() {
            addCriterion("COMMENT_USER is not null");
            return (Criteria) this;
        }

        public Criteria andCommentUserEqualTo(String value) {
            addCriterion("COMMENT_USER =", value, "commentUser");
            return (Criteria) this;
        }

        public Criteria andCommentUserNotEqualTo(String value) {
            addCriterion("COMMENT_USER <>", value, "commentUser");
            return (Criteria) this;
        }

        public Criteria andCommentUserGreaterThan(String value) {
            addCriterion("COMMENT_USER >", value, "commentUser");
            return (Criteria) this;
        }

        public Criteria andCommentUserGreaterThanOrEqualTo(String value) {
            addCriterion("COMMENT_USER >=", value, "commentUser");
            return (Criteria) this;
        }

        public Criteria andCommentUserLessThan(String value) {
            addCriterion("COMMENT_USER <", value, "commentUser");
            return (Criteria) this;
        }

        public Criteria andCommentUserLessThanOrEqualTo(String value) {
            addCriterion("COMMENT_USER <=", value, "commentUser");
            return (Criteria) this;
        }

        public Criteria andCommentUserLike(String value) {
            addCriterion("COMMENT_USER like", value, "commentUser");
            return (Criteria) this;
        }

        public Criteria andCommentUserNotLike(String value) {
            addCriterion("COMMENT_USER not like", value, "commentUser");
            return (Criteria) this;
        }

        public Criteria andCommentUserIn(List<String> values) {
            addCriterion("COMMENT_USER in", values, "commentUser");
            return (Criteria) this;
        }

        public Criteria andCommentUserNotIn(List<String> values) {
            addCriterion("COMMENT_USER not in", values, "commentUser");
            return (Criteria) this;
        }

        public Criteria andCommentUserBetween(String value1, String value2) {
            addCriterion("COMMENT_USER between", value1, value2, "commentUser");
            return (Criteria) this;
        }

        public Criteria andCommentUserNotBetween(String value1, String value2) {
            addCriterion("COMMENT_USER not between", value1, value2, "commentUser");
            return (Criteria) this;
        }

        public Criteria andCdCommentIsNull() {
            addCriterion("CD_COMMENT is null");
            return (Criteria) this;
        }

        public Criteria andCdCommentIsNotNull() {
            addCriterion("CD_COMMENT is not null");
            return (Criteria) this;
        }

        public Criteria andCdCommentEqualTo(String value) {
            addCriterion("CD_COMMENT =", value, "cdComment");
            return (Criteria) this;
        }

        public Criteria andCdCommentNotEqualTo(String value) {
            addCriterion("CD_COMMENT <>", value, "cdComment");
            return (Criteria) this;
        }

        public Criteria andCdCommentGreaterThan(String value) {
            addCriterion("CD_COMMENT >", value, "cdComment");
            return (Criteria) this;
        }

        public Criteria andCdCommentGreaterThanOrEqualTo(String value) {
            addCriterion("CD_COMMENT >=", value, "cdComment");
            return (Criteria) this;
        }

        public Criteria andCdCommentLessThan(String value) {
            addCriterion("CD_COMMENT <", value, "cdComment");
            return (Criteria) this;
        }

        public Criteria andCdCommentLessThanOrEqualTo(String value) {
            addCriterion("CD_COMMENT <=", value, "cdComment");
            return (Criteria) this;
        }

        public Criteria andCdCommentLike(String value) {
            addCriterion("CD_COMMENT like", value, "cdComment");
            return (Criteria) this;
        }

        public Criteria andCdCommentNotLike(String value) {
            addCriterion("CD_COMMENT not like", value, "cdComment");
            return (Criteria) this;
        }

        public Criteria andCdCommentIn(List<String> values) {
            addCriterion("CD_COMMENT in", values, "cdComment");
            return (Criteria) this;
        }

        public Criteria andCdCommentNotIn(List<String> values) {
            addCriterion("CD_COMMENT not in", values, "cdComment");
            return (Criteria) this;
        }

        public Criteria andCdCommentBetween(String value1, String value2) {
            addCriterion("CD_COMMENT between", value1, value2, "cdComment");
            return (Criteria) this;
        }

        public Criteria andCdCommentNotBetween(String value1, String value2) {
            addCriterion("CD_COMMENT not between", value1, value2, "cdComment");
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