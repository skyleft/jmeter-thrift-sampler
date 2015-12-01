package utils;

/**
 * Created by zhangzongchao on 2015/12/1.
 */
public class Ret {
    private Boolean success;
    private String message;

    public static final Ret SUCCESS = new Ret(true,"操作成功");
    public static final Ret FAIL = new Ret(false,"操作失败");

    public Ret(boolean b, String msg) {
        this.success = b;
        this.message = msg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Ret{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
