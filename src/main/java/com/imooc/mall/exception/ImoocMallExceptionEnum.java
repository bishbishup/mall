package com.imooc.mall.exception;

/**
 *  描述：    异常枚举
 */
public enum ImoocMallExceptionEnum {
    NEED_USER_NAME(10001,"用户名不能为空"),
    NEED_PASSWORD(10002,"密码不能为空"),
    PASSWORD_TOO_SHORT(10003,"密码长度不能小于8位"),
    NAME_EXISTED(10004,"不允许重名"),
    INSERT_FAILED(10005,"插入失败，请重试"),
    WRONG_PASSWORD(10006,"密码错误"),
    NEED_LOGIN(10007,"用户未登录"),
    UPDATE_FAILED(10008,"更新失败"),
    NEED_ADMIN(10009,"无管理员权限"),
    PARA_NOT_NULL(10010,"参数不能为空"),
    CREATE_FAILED(10011,"新增失败"),
    REQUEST_PARAM_ERROR(10012,"参数错误"),
    DELETE_FAILED(10013,"删除失败"),
    MKDIR_FAILE(10014,"文件夹创建失败"),
    UPLOAD_FIALED(10015,"图片上传失败"),
    NOT_SALE(10016,"商品状态不可售"),
    NOT_SELECTED(10017,"无法选中"),
    CART_EMPTY(10018,"购物车已勾选的商品为空"),
    NOT_ENOUGH(10019,"商品库存不足"),
    NO_ENUM(10020,"未找到对应的枚举类"),
    NO_ORDER(10021,"订单不存在"),
    NOT_YOUR_ORDER(10022,"订单不属于你"),
    WRONG_ORDER_STATUS(10023,"订单状态不符"),
    WRONG_EMAIL(10024,"非法的邮件地址"),
    EMAIL_ALREADY_BEEN_REGISTERED(10025,"email地址已被注册"),
    EMAIL_ALREADY_BEEN_SEND(10026,"email已发送，若无法收到，请稍后再试"),
    SYSTEM_ERROR(20000,"系统异常");
    /**
     *  异常码
     */
    Integer code;
    String msg;

    ImoocMallExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
