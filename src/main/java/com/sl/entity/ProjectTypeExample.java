package com.sl.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProjectTypeExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    protected Long offset;

    protected Long limit;

    protected Long end;

    public ProjectTypeExample() {
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

        public Criteria andCdTypeIsNull() {
            addCriterion("CD_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andCdTypeIsNotNull() {
            addCriterion("CD_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andCdTypeEqualTo(String value) {
            addCriterion("CD_TYPE =", value, "cdType");
            return (Criteria) this;
        }

        public Criteria andCdTypeNotEqualTo(String value) {
            addCriterion("CD_TYPE <>", value, "cdType");
            return (Criteria) this;
        }

        public Criteria andCdTypeGreaterThan(String value) {
            addCriterion("CD_TYPE >", value, "cdType");
            return (Criteria) this;
        }

        public Criteria andCdTypeGreaterThanOrEqualTo(String value) {
            addCriterion("CD_TYPE >=", value, "cdType");
            return (Criteria) this;
        }

        public Criteria andCdTypeLessThan(String value) {
            addCriterion("CD_TYPE <", value, "cdType");
            return (Criteria) this;
        }

        public Criteria andCdTypeLessThanOrEqualTo(String value) {
            addCriterion("CD_TYPE <=", value, "cdType");
            return (Criteria) this;
        }

        public Criteria andCdTypeLike(String value) {
            addCriterion("CD_TYPE like", value, "cdType");
            return (Criteria) this;
        }

        public Criteria andCdTypeNotLike(String value) {
            addCriterion("CD_TYPE not like", value, "cdType");
            return (Criteria) this;
        }

        public Criteria andCdTypeIn(List<String> values) {
            addCriterion("CD_TYPE in", values, "cdType");
            return (Criteria) this;
        }

        public Criteria andCdTypeNotIn(List<String> values) {
            addCriterion("CD_TYPE not in", values, "cdType");
            return (Criteria) this;
        }

        public Criteria andCdTypeBetween(String value1, String value2) {
            addCriterion("CD_TYPE between", value1, value2, "cdType");
            return (Criteria) this;
        }

        public Criteria andCdTypeNotBetween(String value1, String value2) {
            addCriterion("CD_TYPE not between", value1, value2, "cdType");
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