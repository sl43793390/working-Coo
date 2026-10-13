package com.sl.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InstitutionExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    protected Long offset;

    protected Long limit;

    protected Long end;

    public InstitutionExample() {
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

        public Criteria andIdInstitutionIsNull() {
            addCriterion("ID_INSTITUTION is null");
            return (Criteria) this;
        }

        public Criteria andIdInstitutionIsNotNull() {
            addCriterion("ID_INSTITUTION is not null");
            return (Criteria) this;
        }

        public Criteria andIdInstitutionEqualTo(String value) {
            addCriterion("ID_INSTITUTION =", value, "idInstitution");
            return (Criteria) this;
        }

        public Criteria andIdInstitutionNotEqualTo(String value) {
            addCriterion("ID_INSTITUTION <>", value, "idInstitution");
            return (Criteria) this;
        }

        public Criteria andIdInstitutionGreaterThan(String value) {
            addCriterion("ID_INSTITUTION >", value, "idInstitution");
            return (Criteria) this;
        }

        public Criteria andIdInstitutionGreaterThanOrEqualTo(String value) {
            addCriterion("ID_INSTITUTION >=", value, "idInstitution");
            return (Criteria) this;
        }

        public Criteria andIdInstitutionLessThan(String value) {
            addCriterion("ID_INSTITUTION <", value, "idInstitution");
            return (Criteria) this;
        }

        public Criteria andIdInstitutionLessThanOrEqualTo(String value) {
            addCriterion("ID_INSTITUTION <=", value, "idInstitution");
            return (Criteria) this;
        }

        public Criteria andIdInstitutionLike(String value) {
            addCriterion("ID_INSTITUTION like", value, "idInstitution");
            return (Criteria) this;
        }

        public Criteria andIdInstitutionNotLike(String value) {
            addCriterion("ID_INSTITUTION not like", value, "idInstitution");
            return (Criteria) this;
        }

        public Criteria andIdInstitutionIn(List<String> values) {
            addCriterion("ID_INSTITUTION in", values, "idInstitution");
            return (Criteria) this;
        }

        public Criteria andIdInstitutionNotIn(List<String> values) {
            addCriterion("ID_INSTITUTION not in", values, "idInstitution");
            return (Criteria) this;
        }

        public Criteria andIdInstitutionBetween(String value1, String value2) {
            addCriterion("ID_INSTITUTION between", value1, value2, "idInstitution");
            return (Criteria) this;
        }

        public Criteria andIdInstitutionNotBetween(String value1, String value2) {
            addCriterion("ID_INSTITUTION not between", value1, value2, "idInstitution");
            return (Criteria) this;
        }

        public Criteria andAddrUrlIsNull() {
            addCriterion("ADDR_URL is null");
            return (Criteria) this;
        }

        public Criteria andAddrUrlIsNotNull() {
            addCriterion("ADDR_URL is not null");
            return (Criteria) this;
        }

        public Criteria andAddrUrlEqualTo(String value) {
            addCriterion("ADDR_URL =", value, "addrUrl");
            return (Criteria) this;
        }

        public Criteria andAddrUrlNotEqualTo(String value) {
            addCriterion("ADDR_URL <>", value, "addrUrl");
            return (Criteria) this;
        }

        public Criteria andAddrUrlGreaterThan(String value) {
            addCriterion("ADDR_URL >", value, "addrUrl");
            return (Criteria) this;
        }

        public Criteria andAddrUrlGreaterThanOrEqualTo(String value) {
            addCriterion("ADDR_URL >=", value, "addrUrl");
            return (Criteria) this;
        }

        public Criteria andAddrUrlLessThan(String value) {
            addCriterion("ADDR_URL <", value, "addrUrl");
            return (Criteria) this;
        }

        public Criteria andAddrUrlLessThanOrEqualTo(String value) {
            addCriterion("ADDR_URL <=", value, "addrUrl");
            return (Criteria) this;
        }

        public Criteria andAddrUrlLike(String value) {
            addCriterion("ADDR_URL like", value, "addrUrl");
            return (Criteria) this;
        }

        public Criteria andAddrUrlNotLike(String value) {
            addCriterion("ADDR_URL not like", value, "addrUrl");
            return (Criteria) this;
        }

        public Criteria andAddrUrlIn(List<String> values) {
            addCriterion("ADDR_URL in", values, "addrUrl");
            return (Criteria) this;
        }

        public Criteria andAddrUrlNotIn(List<String> values) {
            addCriterion("ADDR_URL not in", values, "addrUrl");
            return (Criteria) this;
        }

        public Criteria andAddrUrlBetween(String value1, String value2) {
            addCriterion("ADDR_URL between", value1, value2, "addrUrl");
            return (Criteria) this;
        }

        public Criteria andAddrUrlNotBetween(String value1, String value2) {
            addCriterion("ADDR_URL not between", value1, value2, "addrUrl");
            return (Criteria) this;
        }

        public Criteria andNameInstitutionIsNull() {
            addCriterion("NAME_INSTITUTION is null");
            return (Criteria) this;
        }

        public Criteria andNameInstitutionIsNotNull() {
            addCriterion("NAME_INSTITUTION is not null");
            return (Criteria) this;
        }

        public Criteria andNameInstitutionEqualTo(String value) {
            addCriterion("NAME_INSTITUTION =", value, "nameInstitution");
            return (Criteria) this;
        }

        public Criteria andNameInstitutionNotEqualTo(String value) {
            addCriterion("NAME_INSTITUTION <>", value, "nameInstitution");
            return (Criteria) this;
        }

        public Criteria andNameInstitutionGreaterThan(String value) {
            addCriterion("NAME_INSTITUTION >", value, "nameInstitution");
            return (Criteria) this;
        }

        public Criteria andNameInstitutionGreaterThanOrEqualTo(String value) {
            addCriterion("NAME_INSTITUTION >=", value, "nameInstitution");
            return (Criteria) this;
        }

        public Criteria andNameInstitutionLessThan(String value) {
            addCriterion("NAME_INSTITUTION <", value, "nameInstitution");
            return (Criteria) this;
        }

        public Criteria andNameInstitutionLessThanOrEqualTo(String value) {
            addCriterion("NAME_INSTITUTION <=", value, "nameInstitution");
            return (Criteria) this;
        }

        public Criteria andNameInstitutionLike(String value) {
            addCriterion("NAME_INSTITUTION like", value, "nameInstitution");
            return (Criteria) this;
        }

        public Criteria andNameInstitutionNotLike(String value) {
            addCriterion("NAME_INSTITUTION not like", value, "nameInstitution");
            return (Criteria) this;
        }

        public Criteria andNameInstitutionIn(List<String> values) {
            addCriterion("NAME_INSTITUTION in", values, "nameInstitution");
            return (Criteria) this;
        }

        public Criteria andNameInstitutionNotIn(List<String> values) {
            addCriterion("NAME_INSTITUTION not in", values, "nameInstitution");
            return (Criteria) this;
        }

        public Criteria andNameInstitutionBetween(String value1, String value2) {
            addCriterion("NAME_INSTITUTION between", value1, value2, "nameInstitution");
            return (Criteria) this;
        }

        public Criteria andNameInstitutionNotBetween(String value1, String value2) {
            addCriterion("NAME_INSTITUTION not between", value1, value2, "nameInstitution");
            return (Criteria) this;
        }

        public Criteria andSiteIsNull() {
            addCriterion("SITE is null");
            return (Criteria) this;
        }

        public Criteria andSiteIsNotNull() {
            addCriterion("SITE is not null");
            return (Criteria) this;
        }

        public Criteria andSiteEqualTo(String value) {
            addCriterion("SITE =", value, "site");
            return (Criteria) this;
        }

        public Criteria andSiteNotEqualTo(String value) {
            addCriterion("SITE <>", value, "site");
            return (Criteria) this;
        }

        public Criteria andSiteGreaterThan(String value) {
            addCriterion("SITE >", value, "site");
            return (Criteria) this;
        }

        public Criteria andSiteGreaterThanOrEqualTo(String value) {
            addCriterion("SITE >=", value, "site");
            return (Criteria) this;
        }

        public Criteria andSiteLessThan(String value) {
            addCriterion("SITE <", value, "site");
            return (Criteria) this;
        }

        public Criteria andSiteLessThanOrEqualTo(String value) {
            addCriterion("SITE <=", value, "site");
            return (Criteria) this;
        }

        public Criteria andSiteLike(String value) {
            addCriterion("SITE like", value, "site");
            return (Criteria) this;
        }

        public Criteria andSiteNotLike(String value) {
            addCriterion("SITE not like", value, "site");
            return (Criteria) this;
        }

        public Criteria andSiteIn(List<String> values) {
            addCriterion("SITE in", values, "site");
            return (Criteria) this;
        }

        public Criteria andSiteNotIn(List<String> values) {
            addCriterion("SITE not in", values, "site");
            return (Criteria) this;
        }

        public Criteria andSiteBetween(String value1, String value2) {
            addCriterion("SITE between", value1, value2, "site");
            return (Criteria) this;
        }

        public Criteria andSiteNotBetween(String value1, String value2) {
            addCriterion("SITE not between", value1, value2, "site");
            return (Criteria) this;
        }

        public Criteria andHolderIsNull() {
            addCriterion("HOLDER is null");
            return (Criteria) this;
        }

        public Criteria andHolderIsNotNull() {
            addCriterion("HOLDER is not null");
            return (Criteria) this;
        }

        public Criteria andHolderEqualTo(String value) {
            addCriterion("HOLDER =", value, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderNotEqualTo(String value) {
            addCriterion("HOLDER <>", value, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderGreaterThan(String value) {
            addCriterion("HOLDER >", value, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderGreaterThanOrEqualTo(String value) {
            addCriterion("HOLDER >=", value, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderLessThan(String value) {
            addCriterion("HOLDER <", value, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderLessThanOrEqualTo(String value) {
            addCriterion("HOLDER <=", value, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderLike(String value) {
            addCriterion("HOLDER like", value, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderNotLike(String value) {
            addCriterion("HOLDER not like", value, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderIn(List<String> values) {
            addCriterion("HOLDER in", values, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderNotIn(List<String> values) {
            addCriterion("HOLDER not in", values, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderBetween(String value1, String value2) {
            addCriterion("HOLDER between", value1, value2, "holder");
            return (Criteria) this;
        }

        public Criteria andHolderNotBetween(String value1, String value2) {
            addCriterion("HOLDER not between", value1, value2, "holder");
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