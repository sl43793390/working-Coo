package com.sl.entity;

import java.io.Serializable;

public class Menu implements Serializable {
    private String idMenu;

    private String nameMenu;

    private String nameClass;

    private String idParent;

    private String nbrOrder;

    private String cdMenuType;

    private String flagLowest;

    private String namePermission;

    private static final long serialVersionUID = 1L;

    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu == null ? null : idMenu.trim();
    }

    public String getNameMenu() {
        return nameMenu;
    }

    public void setNameMenu(String nameMenu) {
        this.nameMenu = nameMenu == null ? null : nameMenu.trim();
    }

    public String getNameClass() {
        return nameClass;
    }

    public void setNameClass(String nameClass) {
        this.nameClass = nameClass == null ? null : nameClass.trim();
    }

    public String getIdParent() {
        return idParent;
    }

    public void setIdParent(String idParent) {
        this.idParent = idParent == null ? null : idParent.trim();
    }

    public String getNbrOrder() {
        return nbrOrder;
    }

    public void setNbrOrder(String nbrOrder) {
        this.nbrOrder = nbrOrder == null ? null : nbrOrder.trim();
    }

    public String getCdMenuType() {
        return cdMenuType;
    }

    public void setCdMenuType(String cdMenuType) {
        this.cdMenuType = cdMenuType == null ? null : cdMenuType.trim();
    }

    public String getFlagLowest() {
        return flagLowest;
    }

    public void setFlagLowest(String flagLowest) {
        this.flagLowest = flagLowest == null ? null : flagLowest.trim();
    }

    public String getNamePermission() {
        return namePermission;
    }

    public void setNamePermission(String namePermission) {
        this.namePermission = namePermission == null ? null : namePermission.trim();
    }
}