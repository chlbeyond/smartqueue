package com.go2smartphone.paidui.model;




import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;


public class Shop  {

    public Shop() { }

    public void copy(Shop bean) {
        if(bean!=null){
            bean.contactEmail=contactEmail;
            bean.remark=remark;
            bean.zoneId=zoneId;
            bean.createOn=createOn;
            bean.brandId=brandId;
            bean.contactPerson=contactPerson;
            bean.id=id;
            bean.openingTime=openingTime;
            bean.address=address;
            bean.contactMobile=contactMobile;
            bean.oid=oid;
            bean.name=name;
            bean.domain=domain;
            bean.closedTime=closedTime;
            bean.telephone=telephone;
            bean.salt=salt;
        }
    }

    public String toString() {
        return   "\n[shop]\n "
            + "- contactEmail = " + contactEmail
            + "- remark = " + remark
            + "- zoneId = " + zoneId
            + "- createOn = " + createOn
            + "- brandId = " + brandId
            + "- contactPerson = " + contactPerson
            + "- id = " + id
            + "- openingTime = " + openingTime
            + "- address = " + address
            + "- contactMobile = " + contactMobile
            + "- oid = " + oid
            + "- name = " + name
            + "- domain = " + domain
            + "- closedTime = " + closedTime
            + "- telephone = " + telephone
            + "- salt = " + salt
            ;
    }

    public void clear() {
        contactEmail=null;
        remark=null;
        zoneId=null;
        createOn=null;
        brandId=null;
        contactPerson=null;
        id=null;
        openingTime=null;
        address=null;
        contactMobile=null;
        oid=null;
        name=null;
        domain=null;
        closedTime=null;
        telephone=null;
        salt=null;
    }


    @SerializedName("contact_email")
    public String contactEmail;    


    @SerializedName("remark")
    public String remark;    


    @SerializedName("zone_id")
    public Long zoneId;    


    @SerializedName("create_on")
    public java.util.Date createOn;    


    @SerializedName("brand_id")
    public Long brandId;    


    @SerializedName("contact_person")
    public String contactPerson;    


    @SerializedName("id")
    public Long id;    


    @SerializedName("opening_time")
    public java.util.Date openingTime;    


    @SerializedName("address")
    public String address;    


    @SerializedName("contact_mobile")
    public String contactMobile;    


    @SerializedName("oid")
    public Integer oid;    


    @SerializedName("name")
    public String name;    


    @SerializedName("domain")
    public String domain;    


    @SerializedName("closed_time")
    public java.util.Date closedTime;    


    @SerializedName("telephone")
    public String telephone;    


    @SerializedName("salt")
    public String salt;    




    public static Shop fromJson(String json){
        Gson gson = new GsonBuilder()
        .serializeNulls()
        .create();
        
        return gson.fromJson(json,  Shop.class  );
    }

}
