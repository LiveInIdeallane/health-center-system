package utils;

public class JsonResult {
    private int code;
    private String msg;
    private Object data;

    public JsonResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static JsonResult ok() { return new JsonResult(200, "ok", null); }
    public static JsonResult ok(Object data) { return new JsonResult(200, "ok", data); }
    public static JsonResult fail(String msg) { return new JsonResult(500, msg, null); }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMsg() { return msg; }
    public void setMsg(String msg) { this.msg = msg; }
    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }
}