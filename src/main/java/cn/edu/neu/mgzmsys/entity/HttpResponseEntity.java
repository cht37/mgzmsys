package cn.edu.neu.mgzmsys.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data

@NoArgsConstructor
public class HttpResponseEntity {

    private Integer code;     //状态码

    private Object data;     //返回数据

    private String message;  //状态消息

    public HttpResponseEntity ok(Object t) {
        return new HttpResponseEntity(ResponseEnum.LOGIN_SUCCESS.getCode(), t);
    }

    public HttpResponseEntity get(Object t) {
        return new HttpResponseEntity(ResponseEnum.GET_SUCCESS.getCode(), t, ResponseEnum.GET_SUCCESS.getMsg());
    }

    public static final HttpResponseEntity LOGIN_SUCCESS =
            new HttpResponseEntity(ResponseEnum.LOGIN_SUCCESS.getCode(),
                    ResponseEnum.LOGIN_SUCCESS.getMsg());

    public static final HttpResponseEntity LOGIN_FAIL =
            new HttpResponseEntity(ResponseEnum.LOGIN_FAIL.getCode(),
                    ResponseEnum.LOGIN_FAIL.getMsg());

    public static final HttpResponseEntity NO_LOGIN =
            new HttpResponseEntity(ResponseEnum.NO_LOGIN.getCode(),
                    ResponseEnum.NO_LOGIN.getMsg());
    public static final HttpResponseEntity REGISTER_SUCCESS =
            new HttpResponseEntity(ResponseEnum.REGISTER_SUCCESS.getCode(),
                    ResponseEnum.REGISTER_SUCCESS.getMsg());
    public static final HttpResponseEntity REGISTER_FAIL = new HttpResponseEntity(ResponseEnum.REGISTER_FAIL.getCode(),
            ResponseEnum.REGISTER_FAIL.getMsg());
    public static final HttpResponseEntity UPDATE_SUCCESS = new HttpResponseEntity(ResponseEnum.UPDATE_SUCCESS.getCode(),
            ResponseEnum.UPDATE_SUCCESS.getMsg());
    public static final HttpResponseEntity UPDATE_FAIL = new HttpResponseEntity(ResponseEnum.UPDATE_FAIL.getCode(),
            ResponseEnum.UPDATE_FAIL.getMsg());
    public static final HttpResponseEntity GET_FAIL = new HttpResponseEntity(ResponseEnum.GET_FAIL.getCode(),
            ResponseEnum.GET_FAIL.getMsg());
    public static final HttpResponseEntity ERROR = new HttpResponseEntity(ResponseEnum.ERROR.getCode(),
            ResponseEnum.ERROR.getMsg());
    public static final HttpResponseEntity LOGOUT_SUCCESS = new HttpResponseEntity(ResponseEnum.LOGOUT_SUCCESS.getCode(), ResponseEnum.LOGOUT_SUCCESS.getMsg());
    public static final HttpResponseEntity LOGOUT_FAIL = new HttpResponseEntity(ResponseEnum.LOGOUT_FAIL.getCode(), ResponseEnum.LOGOUT_FAIL.getMsg());

    public HttpResponseEntity(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }

    public HttpResponseEntity(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public HttpResponseEntity(Integer code, Object data, String msg) {
        this.code = code;
        this.data = data;
        this.message = msg;
    }

    public ResponseEntity<HttpResponseEntity> toResponseEntity() {
        // 将整数状态码转换为 HttpStatus 枚举
        HttpStatus httpStatus = HttpStatus.resolve(this.code);
        if (httpStatus == null) {
            // 如果 code 无法解析成有效的 HttpStatus，可以默认为 OK 或自定义错误
            httpStatus = HttpStatus.OK;
        }
        // 返回构建的 ResponseEntity
        return new ResponseEntity<>(this, httpStatus);
    }
}
