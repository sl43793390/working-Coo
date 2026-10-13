package com.sl.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskBugfixExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    protected Long offset;

    protected Long limit;

    protected Long end;

    public TaskBugfixExample() {
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

        public Criteria andNameTaskIsNull() {
            addCriterion("NAME_TASK is null");
            return (Criteria) this;
        }

        public Criteria andNameTaskIsNotNull() {
            addCriterion("NAME_TASK is not null");
            return (Criteria) this;
        }

        public Criteria andNameTaskEqualTo(String value) {
            addCriterion("NAME_TASK =", value, "nameTask");
            return (Criteria) this;
        }

        public Criteria andNameTaskNotEqualTo(String value) {
            addCriterion("NAME_TASK <>", value, "nameTask");
            return (Criteria) this;
        }

        public Criteria andNameTaskGreaterThan(String value) {
            addCriterion("NAME_TASK >", value, "nameTask");
            return (Criteria) this;
        }

        public Criteria andNameTaskGreaterThanOrEqualTo(String value) {
            addCriterion("NAME_TASK >=", value, "nameTask");
            return (Criteria) this;
        }

        public Criteria andNameTaskLessThan(String value) {
            addCriterion("NAME_TASK <", value, "nameTask");
            return (Criteria) this;
        }

        public Criteria andNameTaskLessThanOrEqualTo(String value) {
            addCriterion("NAME_TASK <=", value, "nameTask");
            return (Criteria) this;
        }

        public Criteria andNameTaskLike(String value) {
            addCriterion("NAME_TASK like", value, "nameTask");
            return (Criteria) this;
        }

        public Criteria andNameTaskNotLike(String value) {
            addCriterion("NAME_TASK not like", value, "nameTask");
            return (Criteria) this;
        }

        public Criteria andNameTaskIn(List<String> values) {
            addCriterion("NAME_TASK in", values, "nameTask");
            return (Criteria) this;
        }

        public Criteria andNameTaskNotIn(List<String> values) {
            addCriterion("NAME_TASK not in", values, "nameTask");
            return (Criteria) this;
        }

        public Criteria andNameTaskBetween(String value1, String value2) {
            addCriterion("NAME_TASK between", value1, value2, "nameTask");
            return (Criteria) this;
        }

        public Criteria andNameTaskNotBetween(String value1, String value2) {
            addCriterion("NAME_TASK not between", value1, value2, "nameTask");
            return (Criteria) this;
        }

        public Criteria andCdTaskTypeIsNull() {
            addCriterion("CD_TASK_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andCdTaskTypeIsNotNull() {
            addCriterion("CD_TASK_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andCdTaskTypeEqualTo(String value) {
            addCriterion("CD_TASK_TYPE =", value, "cdTaskType");
            return (Criteria) this;
        }

        public Criteria andCdTaskTypeNotEqualTo(String value) {
            addCriterion("CD_TASK_TYPE <>", value, "cdTaskType");
            return (Criteria) this;
        }

        public Criteria andCdTaskTypeGreaterThan(String value) {
            addCriterion("CD_TASK_TYPE >", value, "cdTaskType");
            return (Criteria) this;
        }

        public Criteria andCdTaskTypeGreaterThanOrEqualTo(String value) {
            addCriterion("CD_TASK_TYPE >=", value, "cdTaskType");
            return (Criteria) this;
        }

        public Criteria andCdTaskTypeLessThan(String value) {
            addCriterion("CD_TASK_TYPE <", value, "cdTaskType");
            return (Criteria) this;
        }

        public Criteria andCdTaskTypeLessThanOrEqualTo(String value) {
            addCriterion("CD_TASK_TYPE <=", value, "cdTaskType");
            return (Criteria) this;
        }

        public Criteria andCdTaskTypeLike(String value) {
            addCriterion("CD_TASK_TYPE like", value, "cdTaskType");
            return (Criteria) this;
        }

        public Criteria andCdTaskTypeNotLike(String value) {
            addCriterion("CD_TASK_TYPE not like", value, "cdTaskType");
            return (Criteria) this;
        }

        public Criteria andCdTaskTypeIn(List<String> values) {
            addCriterion("CD_TASK_TYPE in", values, "cdTaskType");
            return (Criteria) this;
        }

        public Criteria andCdTaskTypeNotIn(List<String> values) {
            addCriterion("CD_TASK_TYPE not in", values, "cdTaskType");
            return (Criteria) this;
        }

        public Criteria andCdTaskTypeBetween(String value1, String value2) {
            addCriterion("CD_TASK_TYPE between", value1, value2, "cdTaskType");
            return (Criteria) this;
        }

        public Criteria andCdTaskTypeNotBetween(String value1, String value2) {
            addCriterion("CD_TASK_TYPE not between", value1, value2, "cdTaskType");
            return (Criteria) this;
        }

        public Criteria andCdTaskTagIsNull() {
            addCriterion("CD_TASK_TAG is null");
            return (Criteria) this;
        }

        public Criteria andCdTaskTagIsNotNull() {
            addCriterion("CD_TASK_TAG is not null");
            return (Criteria) this;
        }

        public Criteria andCdTaskTagEqualTo(String value) {
            addCriterion("CD_TASK_TAG =", value, "cdTaskTag");
            return (Criteria) this;
        }

        public Criteria andCdTaskTagNotEqualTo(String value) {
            addCriterion("CD_TASK_TAG <>", value, "cdTaskTag");
            return (Criteria) this;
        }

        public Criteria andCdTaskTagGreaterThan(String value) {
            addCriterion("CD_TASK_TAG >", value, "cdTaskTag");
            return (Criteria) this;
        }

        public Criteria andCdTaskTagGreaterThanOrEqualTo(String value) {
            addCriterion("CD_TASK_TAG >=", value, "cdTaskTag");
            return (Criteria) this;
        }

        public Criteria andCdTaskTagLessThan(String value) {
            addCriterion("CD_TASK_TAG <", value, "cdTaskTag");
            return (Criteria) this;
        }

        public Criteria andCdTaskTagLessThanOrEqualTo(String value) {
            addCriterion("CD_TASK_TAG <=", value, "cdTaskTag");
            return (Criteria) this;
        }

        public Criteria andCdTaskTagLike(String value) {
            addCriterion("CD_TASK_TAG like", value, "cdTaskTag");
            return (Criteria) this;
        }

        public Criteria andCdTaskTagNotLike(String value) {
            addCriterion("CD_TASK_TAG not like", value, "cdTaskTag");
            return (Criteria) this;
        }

        public Criteria andCdTaskTagIn(List<String> values) {
            addCriterion("CD_TASK_TAG in", values, "cdTaskTag");
            return (Criteria) this;
        }

        public Criteria andCdTaskTagNotIn(List<String> values) {
            addCriterion("CD_TASK_TAG not in", values, "cdTaskTag");
            return (Criteria) this;
        }

        public Criteria andCdTaskTagBetween(String value1, String value2) {
            addCriterion("CD_TASK_TAG between", value1, value2, "cdTaskTag");
            return (Criteria) this;
        }

        public Criteria andCdTaskTagNotBetween(String value1, String value2) {
            addCriterion("CD_TASK_TAG not between", value1, value2, "cdTaskTag");
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

        public Criteria andDtCreateIsNull() {
            addCriterion("DT_CREATE is null");
            return (Criteria) this;
        }

        public Criteria andDtCreateIsNotNull() {
            addCriterion("DT_CREATE is not null");
            return (Criteria) this;
        }

        public Criteria andDtCreateEqualTo(Date value) {
            addCriterion("DT_CREATE =", value, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateNotEqualTo(Date value) {
            addCriterion("DT_CREATE <>", value, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateGreaterThan(Date value) {
            addCriterion("DT_CREATE >", value, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateGreaterThanOrEqualTo(Date value) {
            addCriterion("DT_CREATE >=", value, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateLessThan(Date value) {
            addCriterion("DT_CREATE <", value, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateLessThanOrEqualTo(Date value) {
            addCriterion("DT_CREATE <=", value, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateIn(List<Date> values) {
            addCriterion("DT_CREATE in", values, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateNotIn(List<Date> values) {
            addCriterion("DT_CREATE not in", values, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateBetween(Date value1, Date value2) {
            addCriterion("DT_CREATE between", value1, value2, "dtCreate");
            return (Criteria) this;
        }

        public Criteria andDtCreateNotBetween(Date value1, Date value2) {
            addCriterion("DT_CREATE not between", value1, value2, "dtCreate");
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

        public Criteria andIdCreateUserIsNull() {
            addCriterion("ID_CREATE_USER is null");
            return (Criteria) this;
        }

        public Criteria andIdCreateUserIsNotNull() {
            addCriterion("ID_CREATE_USER is not null");
            return (Criteria) this;
        }

        public Criteria andIdCreateUserEqualTo(String value) {
            addCriterion("ID_CREATE_USER =", value, "idCreateUser");
            return (Criteria) this;
        }

        public Criteria andIdCreateUserNotEqualTo(String value) {
            addCriterion("ID_CREATE_USER <>", value, "idCreateUser");
            return (Criteria) this;
        }

        public Criteria andIdCreateUserGreaterThan(String value) {
            addCriterion("ID_CREATE_USER >", value, "idCreateUser");
            return (Criteria) this;
        }

        public Criteria andIdCreateUserGreaterThanOrEqualTo(String value) {
            addCriterion("ID_CREATE_USER >=", value, "idCreateUser");
            return (Criteria) this;
        }

        public Criteria andIdCreateUserLessThan(String value) {
            addCriterion("ID_CREATE_USER <", value, "idCreateUser");
            return (Criteria) this;
        }

        public Criteria andIdCreateUserLessThanOrEqualTo(String value) {
            addCriterion("ID_CREATE_USER <=", value, "idCreateUser");
            return (Criteria) this;
        }

        public Criteria andIdCreateUserLike(String value) {
            addCriterion("ID_CREATE_USER like", value, "idCreateUser");
            return (Criteria) this;
        }

        public Criteria andIdCreateUserNotLike(String value) {
            addCriterion("ID_CREATE_USER not like", value, "idCreateUser");
            return (Criteria) this;
        }

        public Criteria andIdCreateUserIn(List<String> values) {
            addCriterion("ID_CREATE_USER in", values, "idCreateUser");
            return (Criteria) this;
        }

        public Criteria andIdCreateUserNotIn(List<String> values) {
            addCriterion("ID_CREATE_USER not in", values, "idCreateUser");
            return (Criteria) this;
        }

        public Criteria andIdCreateUserBetween(String value1, String value2) {
            addCriterion("ID_CREATE_USER between", value1, value2, "idCreateUser");
            return (Criteria) this;
        }

        public Criteria andIdCreateUserNotBetween(String value1, String value2) {
            addCriterion("ID_CREATE_USER not between", value1, value2, "idCreateUser");
            return (Criteria) this;
        }

        public Criteria andCdDescriptionIsNull() {
            addCriterion("CD_DESCRIPTION is null");
            return (Criteria) this;
        }

        public Criteria andCdDescriptionIsNotNull() {
            addCriterion("CD_DESCRIPTION is not null");
            return (Criteria) this;
        }

        public Criteria andCdDescriptionEqualTo(String value) {
            addCriterion("CD_DESCRIPTION =", value, "cdDescription");
            return (Criteria) this;
        }

        public Criteria andCdDescriptionNotEqualTo(String value) {
            addCriterion("CD_DESCRIPTION <>", value, "cdDescription");
            return (Criteria) this;
        }

        public Criteria andCdDescriptionGreaterThan(String value) {
            addCriterion("CD_DESCRIPTION >", value, "cdDescription");
            return (Criteria) this;
        }

        public Criteria andCdDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("CD_DESCRIPTION >=", value, "cdDescription");
            return (Criteria) this;
        }

        public Criteria andCdDescriptionLessThan(String value) {
            addCriterion("CD_DESCRIPTION <", value, "cdDescription");
            return (Criteria) this;
        }

        public Criteria andCdDescriptionLessThanOrEqualTo(String value) {
            addCriterion("CD_DESCRIPTION <=", value, "cdDescription");
            return (Criteria) this;
        }

        public Criteria andCdDescriptionLike(String value) {
            addCriterion("CD_DESCRIPTION like", value, "cdDescription");
            return (Criteria) this;
        }

        public Criteria andCdDescriptionNotLike(String value) {
            addCriterion("CD_DESCRIPTION not like", value, "cdDescription");
            return (Criteria) this;
        }

        public Criteria andCdDescriptionIn(List<String> values) {
            addCriterion("CD_DESCRIPTION in", values, "cdDescription");
            return (Criteria) this;
        }

        public Criteria andCdDescriptionNotIn(List<String> values) {
            addCriterion("CD_DESCRIPTION not in", values, "cdDescription");
            return (Criteria) this;
        }

        public Criteria andCdDescriptionBetween(String value1, String value2) {
            addCriterion("CD_DESCRIPTION between", value1, value2, "cdDescription");
            return (Criteria) this;
        }

        public Criteria andCdDescriptionNotBetween(String value1, String value2) {
            addCriterion("CD_DESCRIPTION not between", value1, value2, "cdDescription");
            return (Criteria) this;
        }

        public Criteria andNameTesterIsNull() {
            addCriterion("NAME_TESTER is null");
            return (Criteria) this;
        }

        public Criteria andNameTesterIsNotNull() {
            addCriterion("NAME_TESTER is not null");
            return (Criteria) this;
        }

        public Criteria andNameTesterEqualTo(String value) {
            addCriterion("NAME_TESTER =", value, "nameTester");
            return (Criteria) this;
        }

        public Criteria andNameTesterNotEqualTo(String value) {
            addCriterion("NAME_TESTER <>", value, "nameTester");
            return (Criteria) this;
        }

        public Criteria andNameTesterGreaterThan(String value) {
            addCriterion("NAME_TESTER >", value, "nameTester");
            return (Criteria) this;
        }

        public Criteria andNameTesterGreaterThanOrEqualTo(String value) {
            addCriterion("NAME_TESTER >=", value, "nameTester");
            return (Criteria) this;
        }

        public Criteria andNameTesterLessThan(String value) {
            addCriterion("NAME_TESTER <", value, "nameTester");
            return (Criteria) this;
        }

        public Criteria andNameTesterLessThanOrEqualTo(String value) {
            addCriterion("NAME_TESTER <=", value, "nameTester");
            return (Criteria) this;
        }

        public Criteria andNameTesterLike(String value) {
            addCriterion("NAME_TESTER like", value, "nameTester");
            return (Criteria) this;
        }

        public Criteria andNameTesterNotLike(String value) {
            addCriterion("NAME_TESTER not like", value, "nameTester");
            return (Criteria) this;
        }

        public Criteria andNameTesterIn(List<String> values) {
            addCriterion("NAME_TESTER in", values, "nameTester");
            return (Criteria) this;
        }

        public Criteria andNameTesterNotIn(List<String> values) {
            addCriterion("NAME_TESTER not in", values, "nameTester");
            return (Criteria) this;
        }

        public Criteria andNameTesterBetween(String value1, String value2) {
            addCriterion("NAME_TESTER between", value1, value2, "nameTester");
            return (Criteria) this;
        }

        public Criteria andNameTesterNotBetween(String value1, String value2) {
            addCriterion("NAME_TESTER not between", value1, value2, "nameTester");
            return (Criteria) this;
        }

        public Criteria andDtFixedIsNull() {
            addCriterion("DT_FIXED is null");
            return (Criteria) this;
        }

        public Criteria andDtFixedIsNotNull() {
            addCriterion("DT_FIXED is not null");
            return (Criteria) this;
        }

        public Criteria andDtFixedEqualTo(Date value) {
            addCriterion("DT_FIXED =", value, "dtFixed");
            return (Criteria) this;
        }

        public Criteria andDtFixedNotEqualTo(Date value) {
            addCriterion("DT_FIXED <>", value, "dtFixed");
            return (Criteria) this;
        }

        public Criteria andDtFixedGreaterThan(Date value) {
            addCriterion("DT_FIXED >", value, "dtFixed");
            return (Criteria) this;
        }

        public Criteria andDtFixedGreaterThanOrEqualTo(Date value) {
            addCriterion("DT_FIXED >=", value, "dtFixed");
            return (Criteria) this;
        }

        public Criteria andDtFixedLessThan(Date value) {
            addCriterion("DT_FIXED <", value, "dtFixed");
            return (Criteria) this;
        }

        public Criteria andDtFixedLessThanOrEqualTo(Date value) {
            addCriterion("DT_FIXED <=", value, "dtFixed");
            return (Criteria) this;
        }

        public Criteria andDtFixedIn(List<Date> values) {
            addCriterion("DT_FIXED in", values, "dtFixed");
            return (Criteria) this;
        }

        public Criteria andDtFixedNotIn(List<Date> values) {
            addCriterion("DT_FIXED not in", values, "dtFixed");
            return (Criteria) this;
        }

        public Criteria andDtFixedBetween(Date value1, Date value2) {
            addCriterion("DT_FIXED between", value1, value2, "dtFixed");
            return (Criteria) this;
        }

        public Criteria andDtFixedNotBetween(Date value1, Date value2) {
            addCriterion("DT_FIXED not between", value1, value2, "dtFixed");
            return (Criteria) this;
        }

        public Criteria andCdBugReasonIsNull() {
            addCriterion("CD_BUG_REASON is null");
            return (Criteria) this;
        }

        public Criteria andCdBugReasonIsNotNull() {
            addCriterion("CD_BUG_REASON is not null");
            return (Criteria) this;
        }

        public Criteria andCdBugReasonEqualTo(String value) {
            addCriterion("CD_BUG_REASON =", value, "cdBugReason");
            return (Criteria) this;
        }

        public Criteria andCdBugReasonNotEqualTo(String value) {
            addCriterion("CD_BUG_REASON <>", value, "cdBugReason");
            return (Criteria) this;
        }

        public Criteria andCdBugReasonGreaterThan(String value) {
            addCriterion("CD_BUG_REASON >", value, "cdBugReason");
            return (Criteria) this;
        }

        public Criteria andCdBugReasonGreaterThanOrEqualTo(String value) {
            addCriterion("CD_BUG_REASON >=", value, "cdBugReason");
            return (Criteria) this;
        }

        public Criteria andCdBugReasonLessThan(String value) {
            addCriterion("CD_BUG_REASON <", value, "cdBugReason");
            return (Criteria) this;
        }

        public Criteria andCdBugReasonLessThanOrEqualTo(String value) {
            addCriterion("CD_BUG_REASON <=", value, "cdBugReason");
            return (Criteria) this;
        }

        public Criteria andCdBugReasonLike(String value) {
            addCriterion("CD_BUG_REASON like", value, "cdBugReason");
            return (Criteria) this;
        }

        public Criteria andCdBugReasonNotLike(String value) {
            addCriterion("CD_BUG_REASON not like", value, "cdBugReason");
            return (Criteria) this;
        }

        public Criteria andCdBugReasonIn(List<String> values) {
            addCriterion("CD_BUG_REASON in", values, "cdBugReason");
            return (Criteria) this;
        }

        public Criteria andCdBugReasonNotIn(List<String> values) {
            addCriterion("CD_BUG_REASON not in", values, "cdBugReason");
            return (Criteria) this;
        }

        public Criteria andCdBugReasonBetween(String value1, String value2) {
            addCriterion("CD_BUG_REASON between", value1, value2, "cdBugReason");
            return (Criteria) this;
        }

        public Criteria andCdBugReasonNotBetween(String value1, String value2) {
            addCriterion("CD_BUG_REASON not between", value1, value2, "cdBugReason");
            return (Criteria) this;
        }

        public Criteria andCdSeverityIsNull() {
            addCriterion("CD_SEVERITY is null");
            return (Criteria) this;
        }

        public Criteria andCdSeverityIsNotNull() {
            addCriterion("CD_SEVERITY is not null");
            return (Criteria) this;
        }

        public Criteria andCdSeverityEqualTo(String value) {
            addCriterion("CD_SEVERITY =", value, "cdSeverity");
            return (Criteria) this;
        }

        public Criteria andCdSeverityNotEqualTo(String value) {
            addCriterion("CD_SEVERITY <>", value, "cdSeverity");
            return (Criteria) this;
        }

        public Criteria andCdSeverityGreaterThan(String value) {
            addCriterion("CD_SEVERITY >", value, "cdSeverity");
            return (Criteria) this;
        }

        public Criteria andCdSeverityGreaterThanOrEqualTo(String value) {
            addCriterion("CD_SEVERITY >=", value, "cdSeverity");
            return (Criteria) this;
        }

        public Criteria andCdSeverityLessThan(String value) {
            addCriterion("CD_SEVERITY <", value, "cdSeverity");
            return (Criteria) this;
        }

        public Criteria andCdSeverityLessThanOrEqualTo(String value) {
            addCriterion("CD_SEVERITY <=", value, "cdSeverity");
            return (Criteria) this;
        }

        public Criteria andCdSeverityLike(String value) {
            addCriterion("CD_SEVERITY like", value, "cdSeverity");
            return (Criteria) this;
        }

        public Criteria andCdSeverityNotLike(String value) {
            addCriterion("CD_SEVERITY not like", value, "cdSeverity");
            return (Criteria) this;
        }

        public Criteria andCdSeverityIn(List<String> values) {
            addCriterion("CD_SEVERITY in", values, "cdSeverity");
            return (Criteria) this;
        }

        public Criteria andCdSeverityNotIn(List<String> values) {
            addCriterion("CD_SEVERITY not in", values, "cdSeverity");
            return (Criteria) this;
        }

        public Criteria andCdSeverityBetween(String value1, String value2) {
            addCriterion("CD_SEVERITY between", value1, value2, "cdSeverity");
            return (Criteria) this;
        }

        public Criteria andCdSeverityNotBetween(String value1, String value2) {
            addCriterion("CD_SEVERITY not between", value1, value2, "cdSeverity");
            return (Criteria) this;
        }

        public Criteria andCdModualIsNull() {
            addCriterion("CD_MODUAL is null");
            return (Criteria) this;
        }

        public Criteria andCdModualIsNotNull() {
            addCriterion("CD_MODUAL is not null");
            return (Criteria) this;
        }

        public Criteria andCdModualEqualTo(String value) {
            addCriterion("CD_MODUAL =", value, "cdModual");
            return (Criteria) this;
        }

        public Criteria andCdModualNotEqualTo(String value) {
            addCriterion("CD_MODUAL <>", value, "cdModual");
            return (Criteria) this;
        }

        public Criteria andCdModualGreaterThan(String value) {
            addCriterion("CD_MODUAL >", value, "cdModual");
            return (Criteria) this;
        }

        public Criteria andCdModualGreaterThanOrEqualTo(String value) {
            addCriterion("CD_MODUAL >=", value, "cdModual");
            return (Criteria) this;
        }

        public Criteria andCdModualLessThan(String value) {
            addCriterion("CD_MODUAL <", value, "cdModual");
            return (Criteria) this;
        }

        public Criteria andCdModualLessThanOrEqualTo(String value) {
            addCriterion("CD_MODUAL <=", value, "cdModual");
            return (Criteria) this;
        }

        public Criteria andCdModualLike(String value) {
            addCriterion("CD_MODUAL like", value, "cdModual");
            return (Criteria) this;
        }

        public Criteria andCdModualNotLike(String value) {
            addCriterion("CD_MODUAL not like", value, "cdModual");
            return (Criteria) this;
        }

        public Criteria andCdModualIn(List<String> values) {
            addCriterion("CD_MODUAL in", values, "cdModual");
            return (Criteria) this;
        }

        public Criteria andCdModualNotIn(List<String> values) {
            addCriterion("CD_MODUAL not in", values, "cdModual");
            return (Criteria) this;
        }

        public Criteria andCdModualBetween(String value1, String value2) {
            addCriterion("CD_MODUAL between", value1, value2, "cdModual");
            return (Criteria) this;
        }

        public Criteria andCdModualNotBetween(String value1, String value2) {
            addCriterion("CD_MODUAL not between", value1, value2, "cdModual");
            return (Criteria) this;
        }

        public Criteria andNameSystemIsNull() {
            addCriterion("NAME_SYSTEM is null");
            return (Criteria) this;
        }

        public Criteria andNameSystemIsNotNull() {
            addCriterion("NAME_SYSTEM is not null");
            return (Criteria) this;
        }

        public Criteria andNameSystemEqualTo(String value) {
            addCriterion("NAME_SYSTEM =", value, "nameSystem");
            return (Criteria) this;
        }

        public Criteria andNameSystemNotEqualTo(String value) {
            addCriterion("NAME_SYSTEM <>", value, "nameSystem");
            return (Criteria) this;
        }

        public Criteria andNameSystemGreaterThan(String value) {
            addCriterion("NAME_SYSTEM >", value, "nameSystem");
            return (Criteria) this;
        }

        public Criteria andNameSystemGreaterThanOrEqualTo(String value) {
            addCriterion("NAME_SYSTEM >=", value, "nameSystem");
            return (Criteria) this;
        }

        public Criteria andNameSystemLessThan(String value) {
            addCriterion("NAME_SYSTEM <", value, "nameSystem");
            return (Criteria) this;
        }

        public Criteria andNameSystemLessThanOrEqualTo(String value) {
            addCriterion("NAME_SYSTEM <=", value, "nameSystem");
            return (Criteria) this;
        }

        public Criteria andNameSystemLike(String value) {
            addCriterion("NAME_SYSTEM like", value, "nameSystem");
            return (Criteria) this;
        }

        public Criteria andNameSystemNotLike(String value) {
            addCriterion("NAME_SYSTEM not like", value, "nameSystem");
            return (Criteria) this;
        }

        public Criteria andNameSystemIn(List<String> values) {
            addCriterion("NAME_SYSTEM in", values, "nameSystem");
            return (Criteria) this;
        }

        public Criteria andNameSystemNotIn(List<String> values) {
            addCriterion("NAME_SYSTEM not in", values, "nameSystem");
            return (Criteria) this;
        }

        public Criteria andNameSystemBetween(String value1, String value2) {
            addCriterion("NAME_SYSTEM between", value1, value2, "nameSystem");
            return (Criteria) this;
        }

        public Criteria andNameSystemNotBetween(String value1, String value2) {
            addCriterion("NAME_SYSTEM not between", value1, value2, "nameSystem");
            return (Criteria) this;
        }

        public Criteria andCdSuspendIsNull() {
            addCriterion("CD_SUSPEND is null");
            return (Criteria) this;
        }

        public Criteria andCdSuspendIsNotNull() {
            addCriterion("CD_SUSPEND is not null");
            return (Criteria) this;
        }

        public Criteria andCdSuspendEqualTo(String value) {
            addCriterion("CD_SUSPEND =", value, "cdSuspend");
            return (Criteria) this;
        }

        public Criteria andCdSuspendNotEqualTo(String value) {
            addCriterion("CD_SUSPEND <>", value, "cdSuspend");
            return (Criteria) this;
        }

        public Criteria andCdSuspendGreaterThan(String value) {
            addCriterion("CD_SUSPEND >", value, "cdSuspend");
            return (Criteria) this;
        }

        public Criteria andCdSuspendGreaterThanOrEqualTo(String value) {
            addCriterion("CD_SUSPEND >=", value, "cdSuspend");
            return (Criteria) this;
        }

        public Criteria andCdSuspendLessThan(String value) {
            addCriterion("CD_SUSPEND <", value, "cdSuspend");
            return (Criteria) this;
        }

        public Criteria andCdSuspendLessThanOrEqualTo(String value) {
            addCriterion("CD_SUSPEND <=", value, "cdSuspend");
            return (Criteria) this;
        }

        public Criteria andCdSuspendLike(String value) {
            addCriterion("CD_SUSPEND like", value, "cdSuspend");
            return (Criteria) this;
        }

        public Criteria andCdSuspendNotLike(String value) {
            addCriterion("CD_SUSPEND not like", value, "cdSuspend");
            return (Criteria) this;
        }

        public Criteria andCdSuspendIn(List<String> values) {
            addCriterion("CD_SUSPEND in", values, "cdSuspend");
            return (Criteria) this;
        }

        public Criteria andCdSuspendNotIn(List<String> values) {
            addCriterion("CD_SUSPEND not in", values, "cdSuspend");
            return (Criteria) this;
        }

        public Criteria andCdSuspendBetween(String value1, String value2) {
            addCriterion("CD_SUSPEND between", value1, value2, "cdSuspend");
            return (Criteria) this;
        }

        public Criteria andCdSuspendNotBetween(String value1, String value2) {
            addCriterion("CD_SUSPEND not between", value1, value2, "cdSuspend");
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