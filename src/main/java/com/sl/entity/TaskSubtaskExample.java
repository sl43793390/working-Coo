package com.sl.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskSubtaskExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    protected Long offset;

    protected Long limit;

    protected Long end;

    public TaskSubtaskExample() {
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

        public Criteria andIdSubtaskIsNull() {
            addCriterion("ID_SUBTASK is null");
            return (Criteria) this;
        }

        public Criteria andIdSubtaskIsNotNull() {
            addCriterion("ID_SUBTASK is not null");
            return (Criteria) this;
        }

        public Criteria andIdSubtaskEqualTo(String value) {
            addCriterion("ID_SUBTASK =", value, "idSubtask");
            return (Criteria) this;
        }

        public Criteria andIdSubtaskNotEqualTo(String value) {
            addCriterion("ID_SUBTASK <>", value, "idSubtask");
            return (Criteria) this;
        }

        public Criteria andIdSubtaskGreaterThan(String value) {
            addCriterion("ID_SUBTASK >", value, "idSubtask");
            return (Criteria) this;
        }

        public Criteria andIdSubtaskGreaterThanOrEqualTo(String value) {
            addCriterion("ID_SUBTASK >=", value, "idSubtask");
            return (Criteria) this;
        }

        public Criteria andIdSubtaskLessThan(String value) {
            addCriterion("ID_SUBTASK <", value, "idSubtask");
            return (Criteria) this;
        }

        public Criteria andIdSubtaskLessThanOrEqualTo(String value) {
            addCriterion("ID_SUBTASK <=", value, "idSubtask");
            return (Criteria) this;
        }

        public Criteria andIdSubtaskLike(String value) {
            addCriterion("ID_SUBTASK like", value, "idSubtask");
            return (Criteria) this;
        }

        public Criteria andIdSubtaskNotLike(String value) {
            addCriterion("ID_SUBTASK not like", value, "idSubtask");
            return (Criteria) this;
        }

        public Criteria andIdSubtaskIn(List<String> values) {
            addCriterion("ID_SUBTASK in", values, "idSubtask");
            return (Criteria) this;
        }

        public Criteria andIdSubtaskNotIn(List<String> values) {
            addCriterion("ID_SUBTASK not in", values, "idSubtask");
            return (Criteria) this;
        }

        public Criteria andIdSubtaskBetween(String value1, String value2) {
            addCriterion("ID_SUBTASK between", value1, value2, "idSubtask");
            return (Criteria) this;
        }

        public Criteria andIdSubtaskNotBetween(String value1, String value2) {
            addCriterion("ID_SUBTASK not between", value1, value2, "idSubtask");
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

        public Criteria andIdTaskParentIsNull() {
            addCriterion("ID_TASK_PARENT is null");
            return (Criteria) this;
        }

        public Criteria andIdTaskParentIsNotNull() {
            addCriterion("ID_TASK_PARENT is not null");
            return (Criteria) this;
        }

        public Criteria andIdTaskParentEqualTo(String value) {
            addCriterion("ID_TASK_PARENT =", value, "idTaskParent");
            return (Criteria) this;
        }

        public Criteria andIdTaskParentNotEqualTo(String value) {
            addCriterion("ID_TASK_PARENT <>", value, "idTaskParent");
            return (Criteria) this;
        }

        public Criteria andIdTaskParentGreaterThan(String value) {
            addCriterion("ID_TASK_PARENT >", value, "idTaskParent");
            return (Criteria) this;
        }

        public Criteria andIdTaskParentGreaterThanOrEqualTo(String value) {
            addCriterion("ID_TASK_PARENT >=", value, "idTaskParent");
            return (Criteria) this;
        }

        public Criteria andIdTaskParentLessThan(String value) {
            addCriterion("ID_TASK_PARENT <", value, "idTaskParent");
            return (Criteria) this;
        }

        public Criteria andIdTaskParentLessThanOrEqualTo(String value) {
            addCriterion("ID_TASK_PARENT <=", value, "idTaskParent");
            return (Criteria) this;
        }

        public Criteria andIdTaskParentLike(String value) {
            addCriterion("ID_TASK_PARENT like", value, "idTaskParent");
            return (Criteria) this;
        }

        public Criteria andIdTaskParentNotLike(String value) {
            addCriterion("ID_TASK_PARENT not like", value, "idTaskParent");
            return (Criteria) this;
        }

        public Criteria andIdTaskParentIn(List<String> values) {
            addCriterion("ID_TASK_PARENT in", values, "idTaskParent");
            return (Criteria) this;
        }

        public Criteria andIdTaskParentNotIn(List<String> values) {
            addCriterion("ID_TASK_PARENT not in", values, "idTaskParent");
            return (Criteria) this;
        }

        public Criteria andIdTaskParentBetween(String value1, String value2) {
            addCriterion("ID_TASK_PARENT between", value1, value2, "idTaskParent");
            return (Criteria) this;
        }

        public Criteria andIdTaskParentNotBetween(String value1, String value2) {
            addCriterion("ID_TASK_PARENT not between", value1, value2, "idTaskParent");
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

        public Criteria andCdTaskGroupIsNull() {
            addCriterion("CD_TASK_GROUP is null");
            return (Criteria) this;
        }

        public Criteria andCdTaskGroupIsNotNull() {
            addCriterion("CD_TASK_GROUP is not null");
            return (Criteria) this;
        }

        public Criteria andCdTaskGroupEqualTo(String value) {
            addCriterion("CD_TASK_GROUP =", value, "cdTaskGroup");
            return (Criteria) this;
        }

        public Criteria andCdTaskGroupNotEqualTo(String value) {
            addCriterion("CD_TASK_GROUP <>", value, "cdTaskGroup");
            return (Criteria) this;
        }

        public Criteria andCdTaskGroupGreaterThan(String value) {
            addCriterion("CD_TASK_GROUP >", value, "cdTaskGroup");
            return (Criteria) this;
        }

        public Criteria andCdTaskGroupGreaterThanOrEqualTo(String value) {
            addCriterion("CD_TASK_GROUP >=", value, "cdTaskGroup");
            return (Criteria) this;
        }

        public Criteria andCdTaskGroupLessThan(String value) {
            addCriterion("CD_TASK_GROUP <", value, "cdTaskGroup");
            return (Criteria) this;
        }

        public Criteria andCdTaskGroupLessThanOrEqualTo(String value) {
            addCriterion("CD_TASK_GROUP <=", value, "cdTaskGroup");
            return (Criteria) this;
        }

        public Criteria andCdTaskGroupLike(String value) {
            addCriterion("CD_TASK_GROUP like", value, "cdTaskGroup");
            return (Criteria) this;
        }

        public Criteria andCdTaskGroupNotLike(String value) {
            addCriterion("CD_TASK_GROUP not like", value, "cdTaskGroup");
            return (Criteria) this;
        }

        public Criteria andCdTaskGroupIn(List<String> values) {
            addCriterion("CD_TASK_GROUP in", values, "cdTaskGroup");
            return (Criteria) this;
        }

        public Criteria andCdTaskGroupNotIn(List<String> values) {
            addCriterion("CD_TASK_GROUP not in", values, "cdTaskGroup");
            return (Criteria) this;
        }

        public Criteria andCdTaskGroupBetween(String value1, String value2) {
            addCriterion("CD_TASK_GROUP between", value1, value2, "cdTaskGroup");
            return (Criteria) this;
        }

        public Criteria andCdTaskGroupNotBetween(String value1, String value2) {
            addCriterion("CD_TASK_GROUP not between", value1, value2, "cdTaskGroup");
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

        public Criteria andCdTagIsNull() {
            addCriterion("CD_TAG is null");
            return (Criteria) this;
        }

        public Criteria andCdTagIsNotNull() {
            addCriterion("CD_TAG is not null");
            return (Criteria) this;
        }

        public Criteria andCdTagEqualTo(String value) {
            addCriterion("CD_TAG =", value, "cdTag");
            return (Criteria) this;
        }

        public Criteria andCdTagNotEqualTo(String value) {
            addCriterion("CD_TAG <>", value, "cdTag");
            return (Criteria) this;
        }

        public Criteria andCdTagGreaterThan(String value) {
            addCriterion("CD_TAG >", value, "cdTag");
            return (Criteria) this;
        }

        public Criteria andCdTagGreaterThanOrEqualTo(String value) {
            addCriterion("CD_TAG >=", value, "cdTag");
            return (Criteria) this;
        }

        public Criteria andCdTagLessThan(String value) {
            addCriterion("CD_TAG <", value, "cdTag");
            return (Criteria) this;
        }

        public Criteria andCdTagLessThanOrEqualTo(String value) {
            addCriterion("CD_TAG <=", value, "cdTag");
            return (Criteria) this;
        }

        public Criteria andCdTagLike(String value) {
            addCriterion("CD_TAG like", value, "cdTag");
            return (Criteria) this;
        }

        public Criteria andCdTagNotLike(String value) {
            addCriterion("CD_TAG not like", value, "cdTag");
            return (Criteria) this;
        }

        public Criteria andCdTagIn(List<String> values) {
            addCriterion("CD_TAG in", values, "cdTag");
            return (Criteria) this;
        }

        public Criteria andCdTagNotIn(List<String> values) {
            addCriterion("CD_TAG not in", values, "cdTag");
            return (Criteria) this;
        }

        public Criteria andCdTagBetween(String value1, String value2) {
            addCriterion("CD_TAG between", value1, value2, "cdTag");
            return (Criteria) this;
        }

        public Criteria andCdTagNotBetween(String value1, String value2) {
            addCriterion("CD_TAG not between", value1, value2, "cdTag");
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

        public Criteria andIntStarIsNull() {
            addCriterion("INT_STAR is null");
            return (Criteria) this;
        }

        public Criteria andIntStarIsNotNull() {
            addCriterion("INT_STAR is not null");
            return (Criteria) this;
        }

        public Criteria andIntStarEqualTo(Boolean value) {
            addCriterion("INT_STAR =", value, "intStar");
            return (Criteria) this;
        }

        public Criteria andIntStarNotEqualTo(Boolean value) {
            addCriterion("INT_STAR <>", value, "intStar");
            return (Criteria) this;
        }

        public Criteria andIntStarGreaterThan(Boolean value) {
            addCriterion("INT_STAR >", value, "intStar");
            return (Criteria) this;
        }

        public Criteria andIntStarGreaterThanOrEqualTo(Boolean value) {
            addCriterion("INT_STAR >=", value, "intStar");
            return (Criteria) this;
        }

        public Criteria andIntStarLessThan(Boolean value) {
            addCriterion("INT_STAR <", value, "intStar");
            return (Criteria) this;
        }

        public Criteria andIntStarLessThanOrEqualTo(Boolean value) {
            addCriterion("INT_STAR <=", value, "intStar");
            return (Criteria) this;
        }

        public Criteria andIntStarIn(List<Boolean> values) {
            addCriterion("INT_STAR in", values, "intStar");
            return (Criteria) this;
        }

        public Criteria andIntStarNotIn(List<Boolean> values) {
            addCriterion("INT_STAR not in", values, "intStar");
            return (Criteria) this;
        }

        public Criteria andIntStarBetween(Boolean value1, Boolean value2) {
            addCriterion("INT_STAR between", value1, value2, "intStar");
            return (Criteria) this;
        }

        public Criteria andIntStarNotBetween(Boolean value1, Boolean value2) {
            addCriterion("INT_STAR not between", value1, value2, "intStar");
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

        public Criteria andNbrPriorIsNull() {
            addCriterion("NBR_PRIOR is null");
            return (Criteria) this;
        }

        public Criteria andNbrPriorIsNotNull() {
            addCriterion("NBR_PRIOR is not null");
            return (Criteria) this;
        }

        public Criteria andNbrPriorEqualTo(Integer value) {
            addCriterion("NBR_PRIOR =", value, "nbrPrior");
            return (Criteria) this;
        }

        public Criteria andNbrPriorNotEqualTo(Integer value) {
            addCriterion("NBR_PRIOR <>", value, "nbrPrior");
            return (Criteria) this;
        }

        public Criteria andNbrPriorGreaterThan(Integer value) {
            addCriterion("NBR_PRIOR >", value, "nbrPrior");
            return (Criteria) this;
        }

        public Criteria andNbrPriorGreaterThanOrEqualTo(Integer value) {
            addCriterion("NBR_PRIOR >=", value, "nbrPrior");
            return (Criteria) this;
        }

        public Criteria andNbrPriorLessThan(Integer value) {
            addCriterion("NBR_PRIOR <", value, "nbrPrior");
            return (Criteria) this;
        }

        public Criteria andNbrPriorLessThanOrEqualTo(Integer value) {
            addCriterion("NBR_PRIOR <=", value, "nbrPrior");
            return (Criteria) this;
        }

        public Criteria andNbrPriorIn(List<Integer> values) {
            addCriterion("NBR_PRIOR in", values, "nbrPrior");
            return (Criteria) this;
        }

        public Criteria andNbrPriorNotIn(List<Integer> values) {
            addCriterion("NBR_PRIOR not in", values, "nbrPrior");
            return (Criteria) this;
        }

        public Criteria andNbrPriorBetween(Integer value1, Integer value2) {
            addCriterion("NBR_PRIOR between", value1, value2, "nbrPrior");
            return (Criteria) this;
        }

        public Criteria andNbrPriorNotBetween(Integer value1, Integer value2) {
            addCriterion("NBR_PRIOR not between", value1, value2, "nbrPrior");
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