package com.ccnode.codegenerator.pojo;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/04/13 10:26
 */
public enum RetStatus {

    SUCCESS(0,"返回成功"),
    BOOKING_ERROR(1001,"booking失败"),
    BOOKING_CHECK_ERROR(1002,"booking校验失败"),
    BOOKING_SYSTEM_ERROR(1003,"booking系统错误"),
    BOOKING_GET_PRODUCT_ERROR(1004,"booking获取产品失败"),

    CREATE_ORDER_ERROR(2001,"生单失败"),
    CREATE_ORDER_CHECK_ERROR(2002,"生单校验失败"),

    REFUND_CHECK_ERROR(4002,"退款校验失败"),
    REFUND_PAYCENTER_ERROR(4003,"支付中心退款失败"),

    INSURE_CHECK_ERROR(5001,"承退保校验失败"),
    INSURE_OUT_FAILED(5002,"承保失败"),
    INSURE_RETURN_FAILED(5003,"退保失败"),
    FAILED(9001,"失败"),
    SYSTEM_ERROR(9002,"系统异常"),
    NONE(-1,"none");

    private Integer code;
    private String desc;

    private RetStatus(Integer code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static RetStatus fromName(String name){
        for(RetStatus e : RetStatus.values()){
            if (e.name().equalsIgnoreCase(name)){
                return e;
            }
        }
        return RetStatus.NONE;
    }

    public static RetStatus fromCode(Integer code){
        for(RetStatus e : RetStatus.values()){
            if (e.getCode() == code){
                return e;
            }
        }
        return RetStatus.NONE;
    }

    public static String codeToName(Integer code){
            RetStatus o = fromCode(code);
            return o.name();
        }

    public static Integer nameToCode(String name){
        RetStatus o = fromName(name);
        return o.getCode();
    }
}
