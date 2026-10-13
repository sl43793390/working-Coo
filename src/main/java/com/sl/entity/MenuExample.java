package com.sl.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuExample implements Serializable {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private static final long serialVersionUID = 1L;

    protected Long offset;

    protected Long limit;

    protected Long end;

    public MenuExample() {
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

        public Criteria andIdMenuIsNull() {
            addCriterion("ID_MENU is null");
            return (Criteria) this;
        }

        public Criteria andIdMenuIsNotNull() {
            addCriterion("ID_MENU is not null");
            return (Criteria) this;
        }

        public Criteria andIdMenuEqualTo(String value) {
            addCriterion("ID_MENU =", value, "idMenu");
            return (Criteria) this;
        }

        public Criteria andIdMenuNotEqualTo(String value) {
            addCriterion("ID_MENU <>", value, "idMenu");
            return (Criteria) this;
        }

        public Criteria andIdMenuGreaterThan(String value) {
            addCriterion("ID_MENU >", value, "idMenu");
            return (Criteria) this;
        }

        public Criteria andIdMenuGreaterThanOrEqualTo(String value) {
            addCriterion("ID_MENU >=", value, "idMenu");
            return (Criteria) this;
        }

        public Criteria andIdMenuLessThan(String value) {
            addCriterion("ID_MENU <", value, "idMenu");
            return (Criteria) this;
        }

        public Criteria andIdMenuLessThanOrEqualTo(String value) {
            addCriterion("ID_MENU <=", value, "idMenu");
            return (Criteria) this;
        }

        public Criteria andIdMenuLike(String value) {
            addCriterion("ID_MENU like", value, "idMenu");
            return (Criteria) this;
        }

        public Criteria andIdMenuNotLike(String value) {
            addCriterion("ID_MENU not like", value, "idMenu");
            return (Criteria) this;
        }

        public Criteria andIdMenuIn(List<String> values) {
            addCriterion("ID_MENU in", values, "idMenu");
            return (Criteria) this;
        }

        public Criteria andIdMenuNotIn(List<String> values) {
            addCriterion("ID_MENU not in", values, "idMenu");
            return (Criteria) this;
        }

        public Criteria andIdMenuBetween(String value1, String value2) {
            addCriterion("ID_MENU between", value1, value2, "idMenu");
            return (Criteria) this;
        }

        public Criteria andIdMenuNotBetween(String value1, String value2) {
            addCriterion("ID_MENU not between", value1, value2, "idMenu");
            return (Criteria) this;
        }

        public Criteria andNameMenuIsNull() {
            addCriterion("NAME_MENU is null");
            return (Criteria) this;
        }

        public Criteria andNameMenuIsNotNull() {
            addCriterion("NAME_MENU is not null");
            return (Criteria) this;
        }

        public Criteria andNameMenuEqualTo(String value) {
            addCriterion("NAME_MENU =", value, "nameMenu");
            return (Criteria) this;
        }

        public Criteria andNameMenuNotEqualTo(String value) {
            addCriterion("NAME_MENU <>", value, "nameMenu");
            return (Criteria) this;
        }

        public Criteria andNameMenuGreaterThan(String value) {
            addCriterion("NAME_MENU >", value, "nameMenu");
            return (Criteria) this;
        }

        public Criteria andNameMenuGreaterThanOrEqualTo(String value) {
            addCriterion("NAME_MENU >=", value, "nameMenu");
            return (Criteria) this;
        }

        public Criteria andNameMenuLessThan(String value) {
            addCriterion("NAME_MENU <", value, "nameMenu");
            return (Criteria) this;
        }

        public Criteria andNameMenuLessThanOrEqualTo(String value) {
            addCriterion("NAME_MENU <=", value, "nameMenu");
            return (Criteria) this;
        }

        public Criteria andNameMenuLike(String value) {
            addCriterion("NAME_MENU like", value, "nameMenu");
            return (Criteria) this;
        }

        public Criteria andNameMenuNotLike(String value) {
            addCriterion("NAME_MENU not like", value, "nameMenu");
            return (Criteria) this;
        }

        public Criteria andNameMenuIn(List<String> values) {
            addCriterion("NAME_MENU in", values, "nameMenu");
            return (Criteria) this;
        }

        public Criteria andNameMenuNotIn(List<String> values) {
            addCriterion("NAME_MENU not in", values, "nameMenu");
            return (Criteria) this;
        }

        public Criteria andNameMenuBetween(String value1, String value2) {
            addCriterion("NAME_MENU between", value1, value2, "nameMenu");
            return (Criteria) this;
        }

        public Criteria andNameMenuNotBetween(String value1, String value2) {
            addCriterion("NAME_MENU not between", value1, value2, "nameMenu");
            return (Criteria) this;
        }

        public Criteria andNameClassIsNull() {
            addCriterion("NAME_CLASS is null");
            return (Criteria) this;
        }

        public Criteria andNameClassIsNotNull() {
            addCriterion("NAME_CLASS is not null");
            return (Criteria) this;
        }

        public Criteria andNameClassEqualTo(String value) {
            addCriterion("NAME_CLASS =", value, "nameClass");
            return (Criteria) this;
        }

        public Criteria andNameClassNotEqualTo(String value) {
            addCriterion("NAME_CLASS <>", value, "nameClass");
            return (Criteria) this;
        }

        public Criteria andNameClassGreaterThan(String value) {
            addCriterion("NAME_CLASS >", value, "nameClass");
            return (Criteria) this;
        }

        public Criteria andNameClassGreaterThanOrEqualTo(String value) {
            addCriterion("NAME_CLASS >=", value, "nameClass");
            return (Criteria) this;
        }

        public Criteria andNameClassLessThan(String value) {
            addCriterion("NAME_CLASS <", value, "nameClass");
            return (Criteria) this;
        }

        public Criteria andNameClassLessThanOrEqualTo(String value) {
            addCriterion("NAME_CLASS <=", value, "nameClass");
            return (Criteria) this;
        }

        public Criteria andNameClassLike(String value) {
            addCriterion("NAME_CLASS like", value, "nameClass");
            return (Criteria) this;
        }

        public Criteria andNameClassNotLike(String value) {
            addCriterion("NAME_CLASS not like", value, "nameClass");
            return (Criteria) this;
        }

        public Criteria andNameClassIn(List<String> values) {
            addCriterion("NAME_CLASS in", values, "nameClass");
            return (Criteria) this;
        }

        public Criteria andNameClassNotIn(List<String> values) {
            addCriterion("NAME_CLASS not in", values, "nameClass");
            return (Criteria) this;
        }

        public Criteria andNameClassBetween(String value1, String value2) {
            addCriterion("NAME_CLASS between", value1, value2, "nameClass");
            return (Criteria) this;
        }

        public Criteria andNameClassNotBetween(String value1, String value2) {
            addCriterion("NAME_CLASS not between", value1, value2, "nameClass");
            return (Criteria) this;
        }

        public Criteria andIdParentIsNull() {
            addCriterion("ID_PARENT is null");
            return (Criteria) this;
        }

        public Criteria andIdParentIsNotNull() {
            addCriterion("ID_PARENT is not null");
            return (Criteria) this;
        }

        public Criteria andIdParentEqualTo(String value) {
            addCriterion("ID_PARENT =", value, "idParent");
            return (Criteria) this;
        }

        public Criteria andIdParentNotEqualTo(String value) {
            addCriterion("ID_PARENT <>", value, "idParent");
            return (Criteria) this;
        }

        public Criteria andIdParentGreaterThan(String value) {
            addCriterion("ID_PARENT >", value, "idParent");
            return (Criteria) this;
        }

        public Criteria andIdParentGreaterThanOrEqualTo(String value) {
            addCriterion("ID_PARENT >=", value, "idParent");
            return (Criteria) this;
        }

        public Criteria andIdParentLessThan(String value) {
            addCriterion("ID_PARENT <", value, "idParent");
            return (Criteria) this;
        }

        public Criteria andIdParentLessThanOrEqualTo(String value) {
            addCriterion("ID_PARENT <=", value, "idParent");
            return (Criteria) this;
        }

        public Criteria andIdParentLike(String value) {
            addCriterion("ID_PARENT like", value, "idParent");
            return (Criteria) this;
        }

        public Criteria andIdParentNotLike(String value) {
            addCriterion("ID_PARENT not like", value, "idParent");
            return (Criteria) this;
        }

        public Criteria andIdParentIn(List<String> values) {
            addCriterion("ID_PARENT in", values, "idParent");
            return (Criteria) this;
        }

        public Criteria andIdParentNotIn(List<String> values) {
            addCriterion("ID_PARENT not in", values, "idParent");
            return (Criteria) this;
        }

        public Criteria andIdParentBetween(String value1, String value2) {
            addCriterion("ID_PARENT between", value1, value2, "idParent");
            return (Criteria) this;
        }

        public Criteria andIdParentNotBetween(String value1, String value2) {
            addCriterion("ID_PARENT not between", value1, value2, "idParent");
            return (Criteria) this;
        }

        public Criteria andNbrOrderIsNull() {
            addCriterion("NBR_ORDER is null");
            return (Criteria) this;
        }

        public Criteria andNbrOrderIsNotNull() {
            addCriterion("NBR_ORDER is not null");
            return (Criteria) this;
        }

        public Criteria andNbrOrderEqualTo(String value) {
            addCriterion("NBR_ORDER =", value, "nbrOrder");
            return (Criteria) this;
        }

        public Criteria andNbrOrderNotEqualTo(String value) {
            addCriterion("NBR_ORDER <>", value, "nbrOrder");
            return (Criteria) this;
        }

        public Criteria andNbrOrderGreaterThan(String value) {
            addCriterion("NBR_ORDER >", value, "nbrOrder");
            return (Criteria) this;
        }

        public Criteria andNbrOrderGreaterThanOrEqualTo(String value) {
            addCriterion("NBR_ORDER >=", value, "nbrOrder");
            return (Criteria) this;
        }

        public Criteria andNbrOrderLessThan(String value) {
            addCriterion("NBR_ORDER <", value, "nbrOrder");
            return (Criteria) this;
        }

        public Criteria andNbrOrderLessThanOrEqualTo(String value) {
            addCriterion("NBR_ORDER <=", value, "nbrOrder");
            return (Criteria) this;
        }

        public Criteria andNbrOrderLike(String value) {
            addCriterion("NBR_ORDER like", value, "nbrOrder");
            return (Criteria) this;
        }

        public Criteria andNbrOrderNotLike(String value) {
            addCriterion("NBR_ORDER not like", value, "nbrOrder");
            return (Criteria) this;
        }

        public Criteria andNbrOrderIn(List<String> values) {
            addCriterion("NBR_ORDER in", values, "nbrOrder");
            return (Criteria) this;
        }

        public Criteria andNbrOrderNotIn(List<String> values) {
            addCriterion("NBR_ORDER not in", values, "nbrOrder");
            return (Criteria) this;
        }

        public Criteria andNbrOrderBetween(String value1, String value2) {
            addCriterion("NBR_ORDER between", value1, value2, "nbrOrder");
            return (Criteria) this;
        }

        public Criteria andNbrOrderNotBetween(String value1, String value2) {
            addCriterion("NBR_ORDER not between", value1, value2, "nbrOrder");
            return (Criteria) this;
        }

        public Criteria andCdMenuTypeIsNull() {
            addCriterion("CD_MENU_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andCdMenuTypeIsNotNull() {
            addCriterion("CD_MENU_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andCdMenuTypeEqualTo(String value) {
            addCriterion("CD_MENU_TYPE =", value, "cdMenuType");
            return (Criteria) this;
        }

        public Criteria andCdMenuTypeNotEqualTo(String value) {
            addCriterion("CD_MENU_TYPE <>", value, "cdMenuType");
            return (Criteria) this;
        }

        public Criteria andCdMenuTypeGreaterThan(String value) {
            addCriterion("CD_MENU_TYPE >", value, "cdMenuType");
            return (Criteria) this;
        }

        public Criteria andCdMenuTypeGreaterThanOrEqualTo(String value) {
            addCriterion("CD_MENU_TYPE >=", value, "cdMenuType");
            return (Criteria) this;
        }

        public Criteria andCdMenuTypeLessThan(String value) {
            addCriterion("CD_MENU_TYPE <", value, "cdMenuType");
            return (Criteria) this;
        }

        public Criteria andCdMenuTypeLessThanOrEqualTo(String value) {
            addCriterion("CD_MENU_TYPE <=", value, "cdMenuType");
            return (Criteria) this;
        }

        public Criteria andCdMenuTypeLike(String value) {
            addCriterion("CD_MENU_TYPE like", value, "cdMenuType");
            return (Criteria) this;
        }

        public Criteria andCdMenuTypeNotLike(String value) {
            addCriterion("CD_MENU_TYPE not like", value, "cdMenuType");
            return (Criteria) this;
        }

        public Criteria andCdMenuTypeIn(List<String> values) {
            addCriterion("CD_MENU_TYPE in", values, "cdMenuType");
            return (Criteria) this;
        }

        public Criteria andCdMenuTypeNotIn(List<String> values) {
            addCriterion("CD_MENU_TYPE not in", values, "cdMenuType");
            return (Criteria) this;
        }

        public Criteria andCdMenuTypeBetween(String value1, String value2) {
            addCriterion("CD_MENU_TYPE between", value1, value2, "cdMenuType");
            return (Criteria) this;
        }

        public Criteria andCdMenuTypeNotBetween(String value1, String value2) {
            addCriterion("CD_MENU_TYPE not between", value1, value2, "cdMenuType");
            return (Criteria) this;
        }

        public Criteria andFlagLowestIsNull() {
            addCriterion("FLAG_LOWEST is null");
            return (Criteria) this;
        }

        public Criteria andFlagLowestIsNotNull() {
            addCriterion("FLAG_LOWEST is not null");
            return (Criteria) this;
        }

        public Criteria andFlagLowestEqualTo(String value) {
            addCriterion("FLAG_LOWEST =", value, "flagLowest");
            return (Criteria) this;
        }

        public Criteria andFlagLowestNotEqualTo(String value) {
            addCriterion("FLAG_LOWEST <>", value, "flagLowest");
            return (Criteria) this;
        }

        public Criteria andFlagLowestGreaterThan(String value) {
            addCriterion("FLAG_LOWEST >", value, "flagLowest");
            return (Criteria) this;
        }

        public Criteria andFlagLowestGreaterThanOrEqualTo(String value) {
            addCriterion("FLAG_LOWEST >=", value, "flagLowest");
            return (Criteria) this;
        }

        public Criteria andFlagLowestLessThan(String value) {
            addCriterion("FLAG_LOWEST <", value, "flagLowest");
            return (Criteria) this;
        }

        public Criteria andFlagLowestLessThanOrEqualTo(String value) {
            addCriterion("FLAG_LOWEST <=", value, "flagLowest");
            return (Criteria) this;
        }

        public Criteria andFlagLowestLike(String value) {
            addCriterion("FLAG_LOWEST like", value, "flagLowest");
            return (Criteria) this;
        }

        public Criteria andFlagLowestNotLike(String value) {
            addCriterion("FLAG_LOWEST not like", value, "flagLowest");
            return (Criteria) this;
        }

        public Criteria andFlagLowestIn(List<String> values) {
            addCriterion("FLAG_LOWEST in", values, "flagLowest");
            return (Criteria) this;
        }

        public Criteria andFlagLowestNotIn(List<String> values) {
            addCriterion("FLAG_LOWEST not in", values, "flagLowest");
            return (Criteria) this;
        }

        public Criteria andFlagLowestBetween(String value1, String value2) {
            addCriterion("FLAG_LOWEST between", value1, value2, "flagLowest");
            return (Criteria) this;
        }

        public Criteria andFlagLowestNotBetween(String value1, String value2) {
            addCriterion("FLAG_LOWEST not between", value1, value2, "flagLowest");
            return (Criteria) this;
        }

        public Criteria andNamePermissionIsNull() {
            addCriterion("NAME_PERMISSION is null");
            return (Criteria) this;
        }

        public Criteria andNamePermissionIsNotNull() {
            addCriterion("NAME_PERMISSION is not null");
            return (Criteria) this;
        }

        public Criteria andNamePermissionEqualTo(String value) {
            addCriterion("NAME_PERMISSION =", value, "namePermission");
            return (Criteria) this;
        }

        public Criteria andNamePermissionNotEqualTo(String value) {
            addCriterion("NAME_PERMISSION <>", value, "namePermission");
            return (Criteria) this;
        }

        public Criteria andNamePermissionGreaterThan(String value) {
            addCriterion("NAME_PERMISSION >", value, "namePermission");
            return (Criteria) this;
        }

        public Criteria andNamePermissionGreaterThanOrEqualTo(String value) {
            addCriterion("NAME_PERMISSION >=", value, "namePermission");
            return (Criteria) this;
        }

        public Criteria andNamePermissionLessThan(String value) {
            addCriterion("NAME_PERMISSION <", value, "namePermission");
            return (Criteria) this;
        }

        public Criteria andNamePermissionLessThanOrEqualTo(String value) {
            addCriterion("NAME_PERMISSION <=", value, "namePermission");
            return (Criteria) this;
        }

        public Criteria andNamePermissionLike(String value) {
            addCriterion("NAME_PERMISSION like", value, "namePermission");
            return (Criteria) this;
        }

        public Criteria andNamePermissionNotLike(String value) {
            addCriterion("NAME_PERMISSION not like", value, "namePermission");
            return (Criteria) this;
        }

        public Criteria andNamePermissionIn(List<String> values) {
            addCriterion("NAME_PERMISSION in", values, "namePermission");
            return (Criteria) this;
        }

        public Criteria andNamePermissionNotIn(List<String> values) {
            addCriterion("NAME_PERMISSION not in", values, "namePermission");
            return (Criteria) this;
        }

        public Criteria andNamePermissionBetween(String value1, String value2) {
            addCriterion("NAME_PERMISSION between", value1, value2, "namePermission");
            return (Criteria) this;
        }

        public Criteria andNamePermissionNotBetween(String value1, String value2) {
            addCriterion("NAME_PERMISSION not between", value1, value2, "namePermission");
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