package com.sl.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ProjectExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Long offset;

    protected Long limit;

    protected Long end;

    public ProjectExample() {
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

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
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

        public Criteria andNameProjectIsNull() {
            addCriterion("NAME_PROJECT is null");
            return (Criteria) this;
        }

        public Criteria andNameProjectIsNotNull() {
            addCriterion("NAME_PROJECT is not null");
            return (Criteria) this;
        }

        public Criteria andNameProjectEqualTo(String value) {
            addCriterion("NAME_PROJECT =", value, "nameProject");
            return (Criteria) this;
        }

        public Criteria andNameProjectNotEqualTo(String value) {
            addCriterion("NAME_PROJECT <>", value, "nameProject");
            return (Criteria) this;
        }

        public Criteria andNameProjectGreaterThan(String value) {
            addCriterion("NAME_PROJECT >", value, "nameProject");
            return (Criteria) this;
        }

        public Criteria andNameProjectGreaterThanOrEqualTo(String value) {
            addCriterion("NAME_PROJECT >=", value, "nameProject");
            return (Criteria) this;
        }

        public Criteria andNameProjectLessThan(String value) {
            addCriterion("NAME_PROJECT <", value, "nameProject");
            return (Criteria) this;
        }

        public Criteria andNameProjectLessThanOrEqualTo(String value) {
            addCriterion("NAME_PROJECT <=", value, "nameProject");
            return (Criteria) this;
        }

        public Criteria andNameProjectLike(String value) {
            addCriterion("NAME_PROJECT like", value, "nameProject");
            return (Criteria) this;
        }

        public Criteria andNameProjectNotLike(String value) {
            addCriterion("NAME_PROJECT not like", value, "nameProject");
            return (Criteria) this;
        }

        public Criteria andNameProjectIn(List<String> values) {
            addCriterion("NAME_PROJECT in", values, "nameProject");
            return (Criteria) this;
        }

        public Criteria andNameProjectNotIn(List<String> values) {
            addCriterion("NAME_PROJECT not in", values, "nameProject");
            return (Criteria) this;
        }

        public Criteria andNameProjectBetween(String value1, String value2) {
            addCriterion("NAME_PROJECT between", value1, value2, "nameProject");
            return (Criteria) this;
        }

        public Criteria andNameProjectNotBetween(String value1, String value2) {
            addCriterion("NAME_PROJECT not between", value1, value2, "nameProject");
            return (Criteria) this;
        }

        public Criteria andCdStatusIsNull() {
            addCriterion("CD_STATUS is null");
            return (Criteria) this;
        }

        public Criteria andCdStatusIsNotNull() {
            addCriterion("CD_STATUS is not null");
            return (Criteria) this;
        }

        public Criteria andCdStatusEqualTo(String value) {
            addCriterion("CD_STATUS =", value, "cdStatus");
            return (Criteria) this;
        }

        public Criteria andCdStatusNotEqualTo(String value) {
            addCriterion("CD_STATUS <>", value, "cdStatus");
            return (Criteria) this;
        }

        public Criteria andCdStatusGreaterThan(String value) {
            addCriterion("CD_STATUS >", value, "cdStatus");
            return (Criteria) this;
        }

        public Criteria andCdStatusGreaterThanOrEqualTo(String value) {
            addCriterion("CD_STATUS >=", value, "cdStatus");
            return (Criteria) this;
        }

        public Criteria andCdStatusLessThan(String value) {
            addCriterion("CD_STATUS <", value, "cdStatus");
            return (Criteria) this;
        }

        public Criteria andCdStatusLessThanOrEqualTo(String value) {
            addCriterion("CD_STATUS <=", value, "cdStatus");
            return (Criteria) this;
        }

        public Criteria andCdStatusLike(String value) {
            addCriterion("CD_STATUS like", value, "cdStatus");
            return (Criteria) this;
        }

        public Criteria andCdStatusNotLike(String value) {
            addCriterion("CD_STATUS not like", value, "cdStatus");
            return (Criteria) this;
        }

        public Criteria andCdStatusIn(List<String> values) {
            addCriterion("CD_STATUS in", values, "cdStatus");
            return (Criteria) this;
        }

        public Criteria andCdStatusNotIn(List<String> values) {
            addCriterion("CD_STATUS not in", values, "cdStatus");
            return (Criteria) this;
        }

        public Criteria andCdStatusBetween(String value1, String value2) {
            addCriterion("CD_STATUS between", value1, value2, "cdStatus");
            return (Criteria) this;
        }

        public Criteria andCdStatusNotBetween(String value1, String value2) {
            addCriterion("CD_STATUS not between", value1, value2, "cdStatus");
            return (Criteria) this;
        }

        public Criteria andCdPriorIsNull() {
            addCriterion("CD_PRIOR is null");
            return (Criteria) this;
        }

        public Criteria andCdPriorIsNotNull() {
            addCriterion("CD_PRIOR is not null");
            return (Criteria) this;
        }

        public Criteria andCdPriorEqualTo(String value) {
            addCriterion("CD_PRIOR =", value, "cdPrior");
            return (Criteria) this;
        }

        public Criteria andCdPriorNotEqualTo(String value) {
            addCriterion("CD_PRIOR <>", value, "cdPrior");
            return (Criteria) this;
        }

        public Criteria andCdPriorGreaterThan(String value) {
            addCriterion("CD_PRIOR >", value, "cdPrior");
            return (Criteria) this;
        }

        public Criteria andCdPriorGreaterThanOrEqualTo(String value) {
            addCriterion("CD_PRIOR >=", value, "cdPrior");
            return (Criteria) this;
        }

        public Criteria andCdPriorLessThan(String value) {
            addCriterion("CD_PRIOR <", value, "cdPrior");
            return (Criteria) this;
        }

        public Criteria andCdPriorLessThanOrEqualTo(String value) {
            addCriterion("CD_PRIOR <=", value, "cdPrior");
            return (Criteria) this;
        }

        public Criteria andCdPriorLike(String value) {
            addCriterion("CD_PRIOR like", value, "cdPrior");
            return (Criteria) this;
        }

        public Criteria andCdPriorNotLike(String value) {
            addCriterion("CD_PRIOR not like", value, "cdPrior");
            return (Criteria) this;
        }

        public Criteria andCdPriorIn(List<String> values) {
            addCriterion("CD_PRIOR in", values, "cdPrior");
            return (Criteria) this;
        }

        public Criteria andCdPriorNotIn(List<String> values) {
            addCriterion("CD_PRIOR not in", values, "cdPrior");
            return (Criteria) this;
        }

        public Criteria andCdPriorBetween(String value1, String value2) {
            addCriterion("CD_PRIOR between", value1, value2, "cdPrior");
            return (Criteria) this;
        }

        public Criteria andCdPriorNotBetween(String value1, String value2) {
            addCriterion("CD_PRIOR not between", value1, value2, "cdPrior");
            return (Criteria) this;
        }

        public Criteria andPctCompleteIsNull() {
            addCriterion("PCT_COMPLETE is null");
            return (Criteria) this;
        }

        public Criteria andPctCompleteIsNotNull() {
            addCriterion("PCT_COMPLETE is not null");
            return (Criteria) this;
        }

        public Criteria andPctCompleteEqualTo(Double value) {
            addCriterion("PCT_COMPLETE =", value, "pctComplete");
            return (Criteria) this;
        }

        public Criteria andPctCompleteNotEqualTo(Double value) {
            addCriterion("PCT_COMPLETE <>", value, "pctComplete");
            return (Criteria) this;
        }

        public Criteria andPctCompleteGreaterThan(Double value) {
            addCriterion("PCT_COMPLETE >", value, "pctComplete");
            return (Criteria) this;
        }

        public Criteria andPctCompleteGreaterThanOrEqualTo(Double value) {
            addCriterion("PCT_COMPLETE >=", value, "pctComplete");
            return (Criteria) this;
        }

        public Criteria andPctCompleteLessThan(Double value) {
            addCriterion("PCT_COMPLETE <", value, "pctComplete");
            return (Criteria) this;
        }

        public Criteria andPctCompleteLessThanOrEqualTo(Double value) {
            addCriterion("PCT_COMPLETE <=", value, "pctComplete");
            return (Criteria) this;
        }

        public Criteria andPctCompleteIn(List<Double> values) {
            addCriterion("PCT_COMPLETE in", values, "pctComplete");
            return (Criteria) this;
        }

        public Criteria andPctCompleteNotIn(List<Double> values) {
            addCriterion("PCT_COMPLETE not in", values, "pctComplete");
            return (Criteria) this;
        }

        public Criteria andPctCompleteBetween(Double value1, Double value2) {
            addCriterion("PCT_COMPLETE between", value1, value2, "pctComplete");
            return (Criteria) this;
        }

        public Criteria andPctCompleteNotBetween(Double value1, Double value2) {
            addCriterion("PCT_COMPLETE not between", value1, value2, "pctComplete");
            return (Criteria) this;
        }

        public Criteria andPctOverdueIsNull() {
            addCriterion("PCT_OVERDUE is null");
            return (Criteria) this;
        }

        public Criteria andPctOverdueIsNotNull() {
            addCriterion("PCT_OVERDUE is not null");
            return (Criteria) this;
        }

        public Criteria andPctOverdueEqualTo(Double value) {
            addCriterion("PCT_OVERDUE =", value, "pctOverdue");
            return (Criteria) this;
        }

        public Criteria andPctOverdueNotEqualTo(Double value) {
            addCriterion("PCT_OVERDUE <>", value, "pctOverdue");
            return (Criteria) this;
        }

        public Criteria andPctOverdueGreaterThan(Double value) {
            addCriterion("PCT_OVERDUE >", value, "pctOverdue");
            return (Criteria) this;
        }

        public Criteria andPctOverdueGreaterThanOrEqualTo(Double value) {
            addCriterion("PCT_OVERDUE >=", value, "pctOverdue");
            return (Criteria) this;
        }

        public Criteria andPctOverdueLessThan(Double value) {
            addCriterion("PCT_OVERDUE <", value, "pctOverdue");
            return (Criteria) this;
        }

        public Criteria andPctOverdueLessThanOrEqualTo(Double value) {
            addCriterion("PCT_OVERDUE <=", value, "pctOverdue");
            return (Criteria) this;
        }

        public Criteria andPctOverdueIn(List<Double> values) {
            addCriterion("PCT_OVERDUE in", values, "pctOverdue");
            return (Criteria) this;
        }

        public Criteria andPctOverdueNotIn(List<Double> values) {
            addCriterion("PCT_OVERDUE not in", values, "pctOverdue");
            return (Criteria) this;
        }

        public Criteria andPctOverdueBetween(Double value1, Double value2) {
            addCriterion("PCT_OVERDUE between", value1, value2, "pctOverdue");
            return (Criteria) this;
        }

        public Criteria andPctOverdueNotBetween(Double value1, Double value2) {
            addCriterion("PCT_OVERDUE not between", value1, value2, "pctOverdue");
            return (Criteria) this;
        }

        public Criteria andDtStartIsNull() {
            addCriterion("DT_START is null");
            return (Criteria) this;
        }

        public Criteria andDtStartIsNotNull() {
            addCriterion("DT_START is not null");
            return (Criteria) this;
        }

        public Criteria andDtStartEqualTo(Date value) {
            addCriterionForJDBCDate("DT_START =", value, "dtStart");
            return (Criteria) this;
        }

        public Criteria andDtStartNotEqualTo(Date value) {
            addCriterionForJDBCDate("DT_START <>", value, "dtStart");
            return (Criteria) this;
        }

        public Criteria andDtStartGreaterThan(Date value) {
            addCriterionForJDBCDate("DT_START >", value, "dtStart");
            return (Criteria) this;
        }

        public Criteria andDtStartGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("DT_START >=", value, "dtStart");
            return (Criteria) this;
        }

        public Criteria andDtStartLessThan(Date value) {
            addCriterionForJDBCDate("DT_START <", value, "dtStart");
            return (Criteria) this;
        }

        public Criteria andDtStartLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("DT_START <=", value, "dtStart");
            return (Criteria) this;
        }

        public Criteria andDtStartIn(List<Date> values) {
            addCriterionForJDBCDate("DT_START in", values, "dtStart");
            return (Criteria) this;
        }

        public Criteria andDtStartNotIn(List<Date> values) {
            addCriterionForJDBCDate("DT_START not in", values, "dtStart");
            return (Criteria) this;
        }

        public Criteria andDtStartBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("DT_START between", value1, value2, "dtStart");
            return (Criteria) this;
        }

        public Criteria andDtStartNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("DT_START not between", value1, value2, "dtStart");
            return (Criteria) this;
        }
//        ===========================
        public Criteria andDtEndIsNull() {
            addCriterion("DT_END is null");
            return (Criteria) this;
        }

        public Criteria andDtEndIsNotNull() {
            addCriterion("DT_END is not null");
            return (Criteria) this;
        }

        public Criteria andDtEndEqualTo(Date value) {
            addCriterionForJDBCDate("DT_END =", value, "dtEnd");
            return (Criteria) this;
        }

        public Criteria andDtEndNotEqualTo(Date value) {
            addCriterionForJDBCDate("DT_END <>", value, "dtEnd");
            return (Criteria) this;
        }

        public Criteria andDtEndGreaterThan(Date value) {
            addCriterionForJDBCDate("DT_END >", value, "dtEnd");
            return (Criteria) this;
        }

        public Criteria andDtEndGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("DT_END >=", value, "dtEnd");
            return (Criteria) this;
        }

        public Criteria andDtEndLessThan(Date value) {
            addCriterionForJDBCDate("DT_END <", value, "dtEnd");
            return (Criteria) this;
        }

        public Criteria andDtEndLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("DT_END <=", value, "dtEnd");
            return (Criteria) this;
        }

        public Criteria andDtEndIn(List<Date> values) {
            addCriterionForJDBCDate("DT_END in", values, "dtEnd");
            return (Criteria) this;
        }

        public Criteria andDtCreateIsNull() {
            addCriterion("DT_CREATE is null");
            return (Criteria) this;
        }

        public Criteria andDtCreateIsNotNull() {
            addCriterion("DT_CREATE is not null");
            return (Criteria) this;
        }

        public Criteria andDtCreateEqualTo(Date value) {
            addCriterionForJDBCDate("DT_CREATE =", value, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateNotEqualTo(Date value) {
            addCriterionForJDBCDate("DT_CREATE <>", value, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateGreaterThan(Date value) {
            addCriterionForJDBCDate("DT_CREATE >", value, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("DT_CREATE >=", value, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateLessThan(Date value) {
            addCriterionForJDBCDate("DT_CREATE <", value, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("DT_CREATE <=", value, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateIn(List<Date> values) {
            addCriterionForJDBCDate("DT_CREATE in", values, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateNotIn(List<Date> values) {
            addCriterionForJDBCDate("DT_CREATE not in", values, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("DT_CREATE between", value1, value2, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("DT_CREATE not between", value1, value2, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andCdCreateUserIsNull() {
            addCriterion("CD_CREATE_USER is null");
            return (Criteria) this;
        }

        public Criteria andCdCreateUserIsNotNull() {
            addCriterion("CD_CREATE_USER is not null");
            return (Criteria) this;
        }

        public Criteria andCdCreateUserEqualTo(String value) {
            addCriterion("CD_CREATE_USER =", value, "cdCreateUser");
            return (Criteria) this;
        }

        public Criteria andCdCreateUserNotEqualTo(String value) {
            addCriterion("CD_CREATE_USER <>", value, "cdCreateUser");
            return (Criteria) this;
        }

        public Criteria andCdCreateUserGreaterThan(String value) {
            addCriterion("CD_CREATE_USER >", value, "cdCreateUser");
            return (Criteria) this;
        }

        public Criteria andCdCreateUserGreaterThanOrEqualTo(String value) {
            addCriterion("CD_CREATE_USER >=", value, "cdCreateUser");
            return (Criteria) this;
        }

        public Criteria andCdCreateUserLessThan(String value) {
            addCriterion("CD_CREATE_USER <", value, "cdCreateUser");
            return (Criteria) this;
        }

        public Criteria andCdCreateUserLessThanOrEqualTo(String value) {
            addCriterion("CD_CREATE_USER <=", value, "cdCreateUser");
            return (Criteria) this;
        }

        public Criteria andCdCreateUserLike(String value) {
            addCriterion("CD_CREATE_USER like", value, "cdCreateUser");
            return (Criteria) this;
        }

        public Criteria andCdCreateUserNotLike(String value) {
            addCriterion("CD_CREATE_USER not like", value, "cdCreateUser");
            return (Criteria) this;
        }

        public Criteria andCdCreateUserIn(List<String> values) {
            addCriterion("CD_CREATE_USER in", values, "cdCreateUser");
            return (Criteria) this;
        }

        public Criteria andCdCreateUserNotIn(List<String> values) {
            addCriterion("CD_CREATE_USER not in", values, "cdCreateUser");
            return (Criteria) this;
        }

        public Criteria andCdCreateUserBetween(String value1, String value2) {
            addCriterion("CD_CREATE_USER between", value1, value2, "cdCreateUser");
            return (Criteria) this;
        }

        public Criteria andCdCreateUserNotBetween(String value1, String value2) {
            addCriterion("CD_CREATE_USER not between", value1, value2, "cdCreateUser");
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

        public Criteria andFlagPrivateIsNull() {
            addCriterion("FLAG_PRIVATE is null");
            return (Criteria) this;
        }

        public Criteria andFlagPrivateIsNotNull() {
            addCriterion("FLAG_PRIVATE is not null");
            return (Criteria) this;
        }

        public Criteria andFlagPrivateEqualTo(String value) {
            addCriterion("FLAG_PRIVATE =", value, "flagPrivate");
            return (Criteria) this;
        }

        public Criteria andFlagPrivateNotEqualTo(String value) {
            addCriterion("FLAG_PRIVATE <>", value, "flagPrivate");
            return (Criteria) this;
        }

        public Criteria andFlagPrivateGreaterThan(String value) {
            addCriterion("FLAG_PRIVATE >", value, "flagPrivate");
            return (Criteria) this;
        }

        public Criteria andFlagPrivateGreaterThanOrEqualTo(String value) {
            addCriterion("FLAG_PRIVATE >=", value, "flagPrivate");
            return (Criteria) this;
        }

        public Criteria andFlagPrivateLessThan(String value) {
            addCriterion("FLAG_PRIVATE <", value, "flagPrivate");
            return (Criteria) this;
        }

        public Criteria andFlagPrivateLessThanOrEqualTo(String value) {
            addCriterion("FLAG_PRIVATE <=", value, "flagPrivate");
            return (Criteria) this;
        }

        public Criteria andFlagPrivateLike(String value) {
            addCriterion("FLAG_PRIVATE like", value, "flagPrivate");
            return (Criteria) this;
        }

        public Criteria andFlagPrivateNotLike(String value) {
            addCriterion("FLAG_PRIVATE not like", value, "flagPrivate");
            return (Criteria) this;
        }

        public Criteria andFlagPrivateIn(List<String> values) {
            addCriterion("FLAG_PRIVATE in", values, "flagPrivate");
            return (Criteria) this;
        }

        public Criteria andFlagPrivateNotIn(List<String> values) {
            addCriterion("FLAG_PRIVATE not in", values, "flagPrivate");
            return (Criteria) this;
        }

        public Criteria andFlagPrivateBetween(String value1, String value2) {
            addCriterion("FLAG_PRIVATE between", value1, value2, "flagPrivate");
            return (Criteria) this;
        }

        public Criteria andFlagPrivateNotBetween(String value1, String value2) {
            addCriterion("FLAG_PRIVATE not between", value1, value2, "flagPrivate");
            return (Criteria) this;
        }

        public Criteria andFlagDeleteIsNull() {
            addCriterion("FLAG_DELETE is null");
            return (Criteria) this;
        }

        public Criteria andFlagDeleteIsNotNull() {
            addCriterion("FLAG_DELETE is not null");
            return (Criteria) this;
        }

        public Criteria andFlagDeleteEqualTo(String value) {
            addCriterion("FLAG_DELETE =", value, "flagDelete");
            return (Criteria) this;
        }

        public Criteria andFlagDeleteNotEqualTo(String value) {
            addCriterion("FLAG_DELETE <>", value, "flagDelete");
            return (Criteria) this;
        }

        public Criteria andFlagDeleteGreaterThan(String value) {
            addCriterion("FLAG_DELETE >", value, "flagDelete");
            return (Criteria) this;
        }

        public Criteria andFlagDeleteGreaterThanOrEqualTo(String value) {
            addCriterion("FLAG_DELETE >=", value, "flagDelete");
            return (Criteria) this;
        }

        public Criteria andFlagDeleteLessThan(String value) {
            addCriterion("FLAG_DELETE <", value, "flagDelete");
            return (Criteria) this;
        }

        public Criteria andFlagDeleteLessThanOrEqualTo(String value) {
            addCriterion("FLAG_DELETE <=", value, "flagDelete");
            return (Criteria) this;
        }

        public Criteria andFlagDeleteLike(String value) {
            addCriterion("FLAG_DELETE like", value, "flagDelete");
            return (Criteria) this;
        }

        public Criteria andFlagDeleteNotLike(String value) {
            addCriterion("FLAG_DELETE not like", value, "flagDelete");
            return (Criteria) this;
        }

        public Criteria andFlagDeleteIn(List<String> values) {
            addCriterion("FLAG_DELETE in", values, "flagDelete");
            return (Criteria) this;
        }

        public Criteria andFlagDeleteNotIn(List<String> values) {
            addCriterion("FLAG_DELETE not in", values, "flagDelete");
            return (Criteria) this;
        }

        public Criteria andFlagDeleteBetween(String value1, String value2) {
            addCriterion("FLAG_DELETE between", value1, value2, "flagDelete");
            return (Criteria) this;
        }

        public Criteria andFlagDeleteNotBetween(String value1, String value2) {
            addCriterion("FLAG_DELETE not between", value1, value2, "flagDelete");
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