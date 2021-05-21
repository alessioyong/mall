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
}
