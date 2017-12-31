package com.wty.app.uexpress.db.entity;

import android.database.Cursor;

import com.wty.app.uexpress.data.entity.GetExpressInfoEntity;
import com.wty.app.uexpress.db.SqliteBaseDALEx;
import com.wty.app.uexpress.db.annotation.DatabaseField;
import com.wty.app.uexpress.db.annotation.SqliteDao;
import com.wty.app.uexpress.util.CoreTimeUtils;

import java.util.List;

/**
 * 快递查询表
 **/
public class EntityExpressDALEx extends SqliteBaseDALEx {

    private static final long serialVersionUID = 1L;

    @DatabaseField(primaryKey = true, Type = DatabaseField.FieldType.VARCHAR)
    //主键key 为 快递公司编码+快递单号
    private String expressid;
    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    //快递公司编码
    private String companycode;
    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    //快递单号
    private String expressnum;
    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    //最近的快递信息
    private String lastjson;
    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    //备注
    private String remark;
    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    //创建时间
    private String createtime;
    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    //最新跟踪信息时间
    private String steptime;
    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    //最新跟踪信息
    private String stepcontext;
    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    //物流信息查找状态  暂无结果  "201"   查询成功  "200"
    private String status;
    @DatabaseField(Type = DatabaseField.FieldType.VARCHAR)
    /**
     * 快递单当前的状态 ：　
     0：在途，即货物处于运输过程中；
     1：揽件，货物已由快递公司揽收并且产生了第一条跟踪信息；
     2：疑难，货物寄送过程出了问题；
     3：签收，收件人已签收；
     4：退签，即货物由于用户拒签、超区等原因退回，而且发件人已经签收；
     5：派件，即快递正在进行同城派件；
     6：退回，货物正处于退回发件人的途中；
     **/
    private String state;
    @DatabaseField(Type = DatabaseField.FieldType.INT)
    //删除状态
    private int recstatus;

    private String companyicon;
    private String companyname;

    public static EntityExpressDALEx get() {
        return (EntityExpressDALEx) SqliteDao.getDao(EntityExpressDALEx.class,true);
    }

    /**
     * 查找所有快递物流信息
     **/
    public List<EntityExpressDALEx> queryAllWithDelete(String searchkey){
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE "+ "( name LIKE '%"+searchkey+"%' OR remark"
                +" LIKE '%"+searchkey+"%') " + " ORDER BY datetime(createtime) DESC";
        return findList(sql);
    }

    /**
     * 查找所有快递物流信息 排查删除状态的
     **/
    public List<EntityExpressDALEx> queryAllWithDelete(){
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE recstatus = 1 "+ " ORDER BY datetime(createtime) DESC";
        return findList(sql);
    }

    /**
     * 查找所有已签收快递物流信息 排查删除状态的
     **/
    public List<EntityExpressDALEx> queryCheck(){
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE recstatus = 1 AND state = '3' "+ " ORDER BY datetime(createtime) DESC";
        return findList(sql);
    }

    /**
     * 查找所有未签收快递物流信息 排查删除状态的
     **/
    public List<EntityExpressDALEx> queryUnCheck(){
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE recstatus = 1 AND state <> '3' "+ " ORDER BY datetime(createtime) DESC";
        return findList(sql);
    }

    /**
     * 查找所有删除状态
     **/
    public List<EntityExpressDALEx> queryDelete(){
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE recstatus = 0 "+ " ORDER BY datetime(createtime) DESC";
        return findList(sql);
    }

    public static void saveExpressInfo(String json,GetExpressInfoEntity entity){
        EntityExpressDALEx express = new EntityExpressDALEx();
        express.setExpressid(entity.com+entity.nu);
        express.setCompanycode(entity.com);
        express.setExpressnum(entity.nu);
        express.setLastjson(json);
        express.setRecstatus(1);
        express.setStatus(entity.status);
        express.setState(entity.state);
        express.setCreatetime(CoreTimeUtils.getNowTime());
        if(entity.data !=null && entity.data.size()!=0){
            express.setSteptime(entity.data.get(0).time);
            express.setStepcontext(entity.data.get(0).context);
        }
        express.saveOrUpdate();
    }

    public String getExpressid() {
        return expressid;
    }

    public void setExpressid(String expressid) {
        this.expressid = expressid;
    }

    public String getCompanycode() {
        return companycode;
    }

    public void setCompanycode(String companycode) {
        this.companycode = companycode;
    }

    public String getExpressnum() {
        return expressnum;
    }

    public void setExpressnum(String expressnum) {
        this.expressnum = expressnum;
    }

    public String getLastjson() {
        return lastjson;
    }

    public void setLastjson(String lastjson) {
        this.lastjson = lastjson;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getRecstatus() {
        return recstatus;
    }

    public void setRecstatus(int recstatus) {
        this.recstatus = recstatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompanyicon() {
        return companyicon;
    }

    public void setCompanyicon(String companyicon) {
        this.companyicon = companyicon;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getSteptime() {
        return steptime;
    }

    public void setSteptime(String steptime) {
        this.steptime = steptime;
    }

    public String getStepcontext() {
        return stepcontext;
    }

    public void setStepcontext(String stepcontext) {
        this.stepcontext = stepcontext;
    }

    @Override
    protected void onSetCursorValueComplete(Cursor cursor) {
        EntityCompanyDALEx company = EntityCompanyDALEx.get().findById(companycode);
        if(company != null){
            setCompanyicon(company.getCompanyIcon());
            setCompanyname(company.getName());
        }
    }
}
