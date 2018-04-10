package com.go2smartphone.paidui.model;




import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;


public class Staff  {

    public Staff() { }

    public void copy(Staff bean) {
        if(bean!=null){
            bean.birthday=birthday;
            bean.sn=sn;
            bean.sex=sex;
            bean.workday=workday;
            bean.shopId=shopId;
            bean.roleId=roleId;
            bean.remark=remark;
            bean.createOn=createOn;
            bean.password=password;
            bean.id=id;
            bean.rfid=rfid;
            bean.oid=oid;
            bean.name=name;
            bean.salary=salary;
            bean.creditUsedMonth=creditUsedMonth;
        }
    }

    public String toString() {
        return   "\n[staff]\n "
            + "- birthday = " + birthday
            + "- sn = " + sn
            + "- sex = " + sex
            + "- workday = " + workday
            + "- shopId = " + shopId
            + "- roleId = " + roleId
            + "- remark = " + remark
            + "- createOn = " + createOn
            + "- password = " + password
            + "- id = " + id
            + "- rfid = " + rfid
            + "- oid = " + oid
            + "- name = " + name
            + "- salary = " + salary
            + "- creditUsedMonth = " + creditUsedMonth
            ;
    }

    public void clear() {
        birthday=null;
        sn=null;
        sex=null;
        workday=null;
        shopId=null;
        roleId=null;
        remark=null;
        createOn=null;
        password=null;
        id=null;
        rfid=null;
        oid=null;
        name=null;
        salary=null;
        creditUsedMonth=null;
    }


    @SerializedName("birthday")
    public java.util.Date birthday;    


    @SerializedName("sn")
    public String sn;    


    @SerializedName("sex")
    public String sex;    


    @SerializedName("workday")
    public java.util.Date workday;    


    @SerializedName("shop_id")
    public Long shopId;    


    @SerializedName("role_id")
    public Long roleId;    


    @SerializedName("remark")
    public String remark;    


    @SerializedName("create_on")
    public java.util.Date createOn;    


    @SerializedName("password")
    public String password;    


    @SerializedName("id")
    public Long id;    


    @SerializedName("rfid")
    public String rfid;    


    @SerializedName("oid")
    public Integer oid;    


    @SerializedName("name")
    public String name;    


    @SerializedName("salary")
    public Double salary;    


    @SerializedName("credit_used_month")
    public Double creditUsedMonth;    




    public static Staff fromJson(String json){
        Gson gson = new GsonBuilder()
        .serializeNulls()
        .create();
        
        return gson.fromJson(json,  Staff.class  );
    }

}
