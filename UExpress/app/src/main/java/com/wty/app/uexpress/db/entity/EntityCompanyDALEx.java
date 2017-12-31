package com.wty.app.uexpress.db.entity;

import com.wty.app.uexpress.db.SqliteBaseDALEx;
import com.wty.app.uexpress.db.annotation.DatabaseField;
import com.wty.app.uexpress.db.annotation.SqliteDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 快递物流公司查询表
 **/
public class EntityCompanyDALEx extends SqliteBaseDALEx {

    private static final long serialVersionUID = 1L;

    @DatabaseField(primaryKey = true, Type = DatabaseField.FieldType.VARCHAR)
    private String code;
    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    private String name;
    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    private String contact;
    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    private String url;
    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    private String pinyin;
    @DatabaseField(Type = DatabaseField.FieldType.INT)
    private int iscommon;

    public static EntityCompanyDALEx get() {
        return (EntityCompanyDALEx) SqliteDao.getDao(EntityCompanyDALEx.class,true);
    }

    public List<EntityCompanyDALEx> queryAllCompany(String searchkey){
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE "+ "( name LIKE '%"+searchkey+"%' OR pinyin"
                +" LIKE '%"+searchkey+"%') " + " ORDER BY pinyin COLLATE NOCASE";
        List<EntityCompanyDALEx> commonlist = findList("SELECT * FROM " + TABLE_NAME + " WHERE iscommon = 1 AND " + "( name LIKE '%"+searchkey+"%' OR pinyin"
                +" LIKE '%"+searchkey+"%') " + " ORDER BY pinyin COLLATE NOCASE");
        for(EntityCompanyDALEx company:commonlist){
            company.setPinyin("*");
        }
        List<EntityCompanyDALEx> result = new ArrayList<>();
        List<EntityCompanyDALEx> allcompany = findList(sql);
        result.addAll(commonlist);
        result.addAll(allcompany);
        return result;
    }

    public String getCode() {
        return code;
    }

    public String getCompanyIcon(){
        return code+".png";
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public int getIscommon() {
        return iscommon;
    }

    public void setIscommon(int iscommon) {
        this.iscommon = iscommon;
    }
}
