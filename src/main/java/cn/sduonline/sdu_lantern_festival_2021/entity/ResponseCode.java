package cn.sduonline.sdu_lantern_festival_2021.entity;

public enum ResponseCode {
    SUCCESS                          (  0, "success"),
    ERROR_NO_TOKEN                   (  1, "调用本接口时没有带上合法的token"),
    ERROR_MISSING_PARAMETER          (  2, "缺少必填参数"),
    ERROR_PARAMETER_TYPE_MISMATCH    (  3, "参数类型错误"),
    ERROR_PARAMETER_INVALID_VALUE    (  4, "参数取值错误"),

    USER_WRONG_ID_OR_PASSWORD        (100, "用户名或密码错误"),
    USER_IN_BLACKLIST                (101, "用户在黑名单中，无法登录"),

    ROOM_INVALID_ID                  (200, "无效的房间号"),
    ROOM_INVALID_USER                (201, "用户不在该房间内"),
    ROOM_INVALID_STATE               (202, "房间状态错误"),
    ROOM_INVALID_CALL_TIME           (230, "接口不在开放时间段内"),
    ROOM_MATCH_FAIL                  (231, "匹配失败"),
    ROOM_INVALID_ANSWER_NUM          (261, "当前不应该回答第num道题"),

    ERROR_UNKNOWN                    (999, "未预料到的错误");


    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
