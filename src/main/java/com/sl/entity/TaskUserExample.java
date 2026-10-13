package com.sl.entity;

import java.util.ArrayList;
import java.util.List;

public class TaskUserExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;
    
    protected Long offset;

    protected Long limit;

    protected Long end;
    

    public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public Long getLimit() {
		return limit;
	}

	public void setLimit(Long limit) {
		this.limit = limit;
	}

	public Long getEnd() {
		return end;
	}

	public void setEnd(Long end) {
		this.end = end;
	}

	public TaskUserExample() {
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

    protected abstract static class GeneratedCriteria {
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

        public Criteria andIdExeUserIsNull() {
            addCriterion("ID_EXE_USER is null");
            return (Criteria) this;
        }

        public Criteria andIdExeUserIsNotNull() {
            addCriterion("ID_EXE_USER is not null");
            return (Criteria) this;
        }

        public Criteria andIdExeUserEqualTo(String value) {
            addCriterion("ID_EXE_USER =", value, "idExeUser");
            return (Criteria) this;
        }

        public Criteria andIdExeUserNotEqualTo(String value) {
            addCriterion("ID_EXE_USER <>", value, "idExeUser");
            return (Criteria) this;
        }

        public Criteria andIdExeUserGreaterThan(String value) {
            addCriterion("ID_EXE_USER >", value, "idExeUser");
            return (Criteria) this;
        }

        public Criteria andIdExeUserGreaterThanOrEqualTo(String value) {
            addCriterion("ID_EXE_USER >=", value, "idExeUser");
            return (Criteria) this;
        }

        public Criteria andIdExeUserLessThan(String value) {
            addCriterion("ID_EXE_USER <", value, "idExeUser");
            return (Criteria) this;
        }

        public Criteria andIdExeUserLessThanOrEqualTo(String value) {
            addCriterion("ID_EXE_USER <=", value, "idExeUser");
            return (Criteria) this;
        }

        public Criteria andIdExeUserLike(String value) {
            addCriterion("ID_EXE_USER like", value, "idExeUser");
            return (Criteria) this;
        }

        public Criteria andIdExeUserNotLike(String value) {
            addCriterion("ID_EXE_USER not like", value, "idExeUser");
            return (Criteria) this;
        }

        public Criteria andIdExeUserIn(List<String> values) {
            addCriterion("ID_EXE_USER in", values, "idExeUser");
            return (Criteria) this;
        }

        public Criteria andIdExeUserNotIn(List<String> values) {
            addCriterion("ID_EXE_USER not in", values, "idExeUser");
            return (Criteria) this;
        }

        public Criteria andIdExeUserBetween(String value1, String value2) {
            addCriterion("ID_EXE_USER between", value1, value2, "idExeUser");
            return (Criteria) this;
        }

        public Criteria andIdExeUserNotBetween(String value1, String value2) {
            addCriterion("ID_EXE_USER not between", value1, value2, "idExeUser");
            return (Criteria) this;
        }

        public Criteria andIdProjectIsNull() {
            addCriterion("ID_PROJECT is null");
            return (Criteria) this;
        }

        public Criteria andIdProjectIsNotNull() {
            addCriterion("ID_PROJECT is not null");
            return (Criteria) this;
        }

        public Criteria andIdProjectEqualTo(String value) {
            addCriterion("ID_PROJECT =", value, "idProject");
            return (Criteria) this;
        }

        public Criteria andIdProjectNotEqualTo(String value) {
            addCriterion("ID_PROJECT <>", value, "idProject");
            return (Criteria) this;
        }

        public Criteria andIdProjectGreaterThan(String value) {
            addCriterion("ID_PROJECT >", value, "idProject");
            return (Criteria) this;
        }

        public Criteria andIdProjectGreaterThanOrEqualTo(String value) {
            addCriterion("ID_PROJECT >=", value, "idProject");
            return (Criteria) this;
        }

        public Criteria andIdProjectLessThan(String value) {
            addCriterion("ID_PROJECT <", value, "idProject");
            return (Criteria) this;
        }

        public Criteria andIdProjectLessThanOrEqualTo(String value) {
            addCriterion("ID_PROJECT <=", value, "idProject");
            return (Criteria) this;
        }

        public Criteria andIdProjectLike(String value) {
            addCriterion("ID_PROJECT like", value, "idProject");
            return (Criteria) this;
        }

        public Criteria andIdProjectNotLike(String value) {
            addCriterion("ID_PROJECT not like", value, "idProject");
            return (Criteria) this;
        }

        public Criteria andIdProjectIn(List<String> values) {
            addCriterion("ID_PROJECT in", values, "idProject");
            return (Criteria) this;
        }

        public Criteria andIdProjectNotIn(List<String> values) {
            addCriterion("ID_PROJECT not in", values, "idProject");
            return (Criteria) this;
        }

        public Criteria andIdProjectBetween(String value1, String value2) {
            addCriterion("ID_PROJECT between", value1, value2, "idProject");
            return (Criteria) this;
        }

        public Criteria andIdProjectNotBetween(String value1, String value2) {
            addCriterion("ID_PROJECT not between", value1, value2, "idProject");
            return (Criteria) this;
        }

        public Criteria andCdMarkIsNull() {
            addCriterion("CD_MARK is null");
            return (Criteria) this;
        }

        public Criteria andCdMarkIsNotNull() {
            addCriterion("CD_MARK is not null");
            return (Criteria) this;
        }

        public Criteria andCdMarkEqualTo(String value) {
            addCriterion("CD_MARK =", value, "cdMark");
            return (Criteria) this;
        }

        public Criteria andCdMarkNotEqualTo(String value) {
            addCriterion("CD_MARK <>", value, "cdMark");
            return (Criteria) this;
        }

        public Criteria andCdMarkGreaterThan(String value) {
            addCriterion("CD_MARK >", value, "cdMark");
            return (Criteria) this;
        }

        public Criteria andCdMarkGreaterThanOrEqualTo(String value) {
            addCriterion("CD_MARK >=", value, "cdMark");
            return (Criteria) this;
        }

        public Criteria andCdMarkLessThan(String value) {
            addCriterion("CD_MARK <", value, "cdMark");
            return (Criteria) this;
        }

        public Criteria andCdMarkLessThanOrEqualTo(String value) {
            addCriterion("CD_MARK <=", value, "cdMark");
            return (Criteria) this;
        }

        public Criteria andCdMarkLike(String value) {
            addCriterion("CD_MARK like", value, "cdMark");
            return (Criteria) this;
        }

        public Criteria andCdMarkNotLike(String value) {
            addCriterion("CD_MARK not like", value, "cdMark");
            return (Criteria) this;
        }

        public Criteria andCdMarkIn(List<String> values) {
            addCriterion("CD_MARK in", values, "cdMark");
            return (Criteria) this;
        }

        public Criteria andCdMarkNotIn(List<String> values) {
            addCriterion("CD_MARK not in", values, "cdMark");
            return (Criteria) this;
        }

        public Criteria andCdMarkBetween(String value1, String value2) {
            addCriterion("CD_MARK between", value1, value2, "cdMark");
            return (Criteria) this;
        }

        public Criteria andCdMarkNotBetween(String value1, String value2) {
            addCriterion("CD_MARK not between", value1, value2, "cdMark");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
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