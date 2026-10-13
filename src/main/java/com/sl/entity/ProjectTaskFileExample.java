package com.sl.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectTaskFileExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ProjectTaskFileExample() {
        oredCriteria = new ArrayList<>();
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
            criteria = new ArrayList<>();
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

        public Criteria andIdFileIsNull() {
            addCriterion("ID_FILE is null");
            return (Criteria) this;
        }

        public Criteria andIdFileIsNotNull() {
            addCriterion("ID_FILE is not null");
            return (Criteria) this;
        }

        public Criteria andIdFileEqualTo(String value) {
            addCriterion("ID_FILE =", value, "idFile");
            return (Criteria) this;
        }

        public Criteria andIdFileNotEqualTo(String value) {
            addCriterion("ID_FILE <>", value, "idFile");
            return (Criteria) this;
        }

        public Criteria andIdFileGreaterThan(String value) {
            addCriterion("ID_FILE >", value, "idFile");
            return (Criteria) this;
        }

        public Criteria andIdFileGreaterThanOrEqualTo(String value) {
            addCriterion("ID_FILE >=", value, "idFile");
            return (Criteria) this;
        }

        public Criteria andIdFileLessThan(String value) {
            addCriterion("ID_FILE <", value, "idFile");
            return (Criteria) this;
        }

        public Criteria andIdFileLessThanOrEqualTo(String value) {
            addCriterion("ID_FILE <=", value, "idFile");
            return (Criteria) this;
        }

        public Criteria andIdFileLike(String value) {
            addCriterion("ID_FILE like", value, "idFile");
            return (Criteria) this;
        }

        public Criteria andIdFileNotLike(String value) {
            addCriterion("ID_FILE not like", value, "idFile");
            return (Criteria) this;
        }

        public Criteria andIdFileIn(List<String> values) {
            addCriterion("ID_FILE in", values, "idFile");
            return (Criteria) this;
        }

        public Criteria andIdFileNotIn(List<String> values) {
            addCriterion("ID_FILE not in", values, "idFile");
            return (Criteria) this;
        }

        public Criteria andIdFileBetween(String value1, String value2) {
            addCriterion("ID_FILE between", value1, value2, "idFile");
            return (Criteria) this;
        }

        public Criteria andIdFileNotBetween(String value1, String value2) {
            addCriterion("ID_FILE not between", value1, value2, "idFile");
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

        public Criteria andNameFileIsNull() {
            addCriterion("NAME_FILE is null");
            return (Criteria) this;
        }

        public Criteria andNameFileIsNotNull() {
            addCriterion("NAME_FILE is not null");
            return (Criteria) this;
        }

        public Criteria andNameFileEqualTo(String value) {
            addCriterion("NAME_FILE =", value, "nameFile");
            return (Criteria) this;
        }

        public Criteria andNameFileNotEqualTo(String value) {
            addCriterion("NAME_FILE <>", value, "nameFile");
            return (Criteria) this;
        }

        public Criteria andNameFileGreaterThan(String value) {
            addCriterion("NAME_FILE >", value, "nameFile");
            return (Criteria) this;
        }

        public Criteria andNameFileGreaterThanOrEqualTo(String value) {
            addCriterion("NAME_FILE >=", value, "nameFile");
            return (Criteria) this;
        }

        public Criteria andNameFileLessThan(String value) {
            addCriterion("NAME_FILE <", value, "nameFile");
            return (Criteria) this;
        }

        public Criteria andNameFileLessThanOrEqualTo(String value) {
            addCriterion("NAME_FILE <=", value, "nameFile");
            return (Criteria) this;
        }

        public Criteria andNameFileLike(String value) {
            addCriterion("NAME_FILE like", value, "nameFile");
            return (Criteria) this;
        }

        public Criteria andNameFileNotLike(String value) {
            addCriterion("NAME_FILE not like", value, "nameFile");
            return (Criteria) this;
        }

        public Criteria andNameFileIn(List<String> values) {
            addCriterion("NAME_FILE in", values, "nameFile");
            return (Criteria) this;
        }

        public Criteria andNameFileNotIn(List<String> values) {
            addCriterion("NAME_FILE not in", values, "nameFile");
            return (Criteria) this;
        }

        public Criteria andNameFileBetween(String value1, String value2) {
            addCriterion("NAME_FILE between", value1, value2, "nameFile");
            return (Criteria) this;
        }

        public Criteria andNameFileNotBetween(String value1, String value2) {
            addCriterion("NAME_FILE not between", value1, value2, "nameFile");
            return (Criteria) this;
        }

        public Criteria andCdFilePathIsNull() {
            addCriterion("CD_FILE_PATH is null");
            return (Criteria) this;
        }

        public Criteria andCdFilePathIsNotNull() {
            addCriterion("CD_FILE_PATH is not null");
            return (Criteria) this;
        }

        public Criteria andCdFilePathEqualTo(String value) {
            addCriterion("CD_FILE_PATH =", value, "cdFilePath");
            return (Criteria) this;
        }

        public Criteria andCdFilePathNotEqualTo(String value) {
            addCriterion("CD_FILE_PATH <>", value, "cdFilePath");
            return (Criteria) this;
        }

        public Criteria andCdFilePathGreaterThan(String value) {
            addCriterion("CD_FILE_PATH >", value, "cdFilePath");
            return (Criteria) this;
        }

        public Criteria andCdFilePathGreaterThanOrEqualTo(String value) {
            addCriterion("CD_FILE_PATH >=", value, "cdFilePath");
            return (Criteria) this;
        }

        public Criteria andCdFilePathLessThan(String value) {
            addCriterion("CD_FILE_PATH <", value, "cdFilePath");
            return (Criteria) this;
        }

        public Criteria andCdFilePathLessThanOrEqualTo(String value) {
            addCriterion("CD_FILE_PATH <=", value, "cdFilePath");
            return (Criteria) this;
        }

        public Criteria andCdFilePathLike(String value) {
            addCriterion("CD_FILE_PATH like", value, "cdFilePath");
            return (Criteria) this;
        }

        public Criteria andCdFilePathNotLike(String value) {
            addCriterion("CD_FILE_PATH not like", value, "cdFilePath");
            return (Criteria) this;
        }

        public Criteria andCdFilePathIn(List<String> values) {
            addCriterion("CD_FILE_PATH in", values, "cdFilePath");
            return (Criteria) this;
        }

        public Criteria andCdFilePathNotIn(List<String> values) {
            addCriterion("CD_FILE_PATH not in", values, "cdFilePath");
            return (Criteria) this;
        }

        public Criteria andCdFilePathBetween(String value1, String value2) {
            addCriterion("CD_FILE_PATH between", value1, value2, "cdFilePath");
            return (Criteria) this;
        }

        public Criteria andCdFilePathNotBetween(String value1, String value2) {
            addCriterion("CD_FILE_PATH not between", value1, value2, "cdFilePath");
            return (Criteria) this;
        }

        public Criteria andDtUploadIsNull() {
            addCriterion("DT_UPLOAD is null");
            return (Criteria) this;
        }

        public Criteria andDtUploadIsNotNull() {
            addCriterion("DT_UPLOAD is not null");
            return (Criteria) this;
        }

        public Criteria andDtUploadEqualTo(Date value) {
            addCriterion("DT_UPLOAD =", value, "dtUpload");
            return (Criteria) this;
        }

        public Criteria andDtUploadNotEqualTo(Date value) {
            addCriterion("DT_UPLOAD <>", value, "dtUpload");
            return (Criteria) this;
        }

        public Criteria andDtUploadGreaterThan(Date value) {
            addCriterion("DT_UPLOAD >", value, "dtUpload");
            return (Criteria) this;
        }

        public Criteria andDtUploadGreaterThanOrEqualTo(Date value) {
            addCriterion("DT_UPLOAD >=", value, "dtUpload");
            return (Criteria) this;
        }

        public Criteria andDtUploadLessThan(Date value) {
            addCriterion("DT_UPLOAD <", value, "dtUpload");
            return (Criteria) this;
        }

        public Criteria andDtUploadLessThanOrEqualTo(Date value) {
            addCriterion("DT_UPLOAD <=", value, "dtUpload");
            return (Criteria) this;
        }

        public Criteria andDtUploadIn(List<Date> values) {
            addCriterion("DT_UPLOAD in", values, "dtUpload");
            return (Criteria) this;
        }

        public Criteria andDtUploadNotIn(List<Date> values) {
            addCriterion("DT_UPLOAD not in", values, "dtUpload");
            return (Criteria) this;
        }

        public Criteria andDtUploadBetween(Date value1, Date value2) {
            addCriterion("DT_UPLOAD between", value1, value2, "dtUpload");
            return (Criteria) this;
        }

        public Criteria andDtUploadNotBetween(Date value1, Date value2) {
            addCriterion("DT_UPLOAD not between", value1, value2, "dtUpload");
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