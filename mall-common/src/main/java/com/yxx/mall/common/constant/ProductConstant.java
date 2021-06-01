package com.yxx.mall.common.constant;

/**
 * @author xyong
 * date 2021-05-21
 */
public class ProductConstant {

   public  enum AttrTypeEnum{
       ATTR_BASE_TYPE(1,"基本属性"),
       ATTR_SALE_TYPE(0,"销售属性");

       int code;
       String msg;

       AttrTypeEnum(int code,String msg){
           this.code=code;
           this.msg=msg;
       }

       public int getCode() {
           return code;
       }

       public String getMsg() {
           return msg;
       }
   }

   public enum PublishStatusEnum{

       PUBLISH_NEW(0,"新建"),
       PUBLISH_UP(1,"上架"),
       PUBLISH_OFF(2,"下架");

       int code;
       String msg;

       PublishStatusEnum(int code,String msg){
           this.code=code;
           this.msg=msg;
       }

       public int getCode() {
           return code;
       }

       public String getMsg() {
           return msg;
       }

   }
}
