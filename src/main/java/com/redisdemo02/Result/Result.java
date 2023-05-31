package com.redisdemo02.Result;

import java.io.Serializable;

import lombok.Data;

@Data
public class Result implements Serializable {

    public int code;
    public String msg;
    Object data;

    /**
     * @param c
     * @param str
     * @param d
     * @return
     */
    public static Result succ(int c, String str, Object d) {

        Result r = new Result();

        r.setCode(c);
        r.setMsg(str);
        r.setData(d);

        return r;
    }

    /**
     * @param d
     * @return
     */
    public static Result succ(Object d) {
        return succ(200, "succ msg", d);
    }

    /**
     * @param c
     * @param str
     * @param d
     * @return
     */
    public static Result fail(int c, String str, Object d) {

        Result r = new Result();

        r.setCode(c);
        r.setData(d);
        r.setMsg(str);

        return r;
    }

    /**
     * @param c
     * @param d
     * @return
     */
    public static Result fail(int c, Object d) {
        return fail(c, "fail msg", d);
    }

    /**
     * @param d
     * @return
     */
    public static Result fail(Object d) {
        return fail(400, "fail msg", d);
    }

}
