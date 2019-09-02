package com.le123.ysdq.ng;

import androidx.annotation.NonNull;

import com.agx.scaffold.JxFunc;
import com.agx.scaffold.JxTextUtils;
import com.google.gson.annotations.SerializedName;

public abstract class Fetcher {
    public static class Response {
        /**
         * code : 200
         * data : {}
         * msg : success
         * ts : 1566959151091
         */


        @SerializedName("code") public String status  = "";
        @SerializedName("msg") public  String message = "";
        @SerializedName("ts") public   long   stamp   = 0;


        boolean success() {
            return is("200");
        }

        boolean is(@NonNull String expectCode) {
            return JxTextUtils.equals(status, expectCode);
        }

        boolean any(@NonNull String[] expectCodes) {
            for (String expectCode : expectCodes) {
                if (JxTextUtils.equals(expectCode, status)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static class Result<Response, Error> {
        public Object   in;
        public Object   out;
        public Response value;
        public Error    error;

        private Result(Object in, Object out, Response value, Error error) {
            this.in = in;
            this.out = out;
            this.value = value;
            this.error = error;
        }

        public static <Response, Error> Result<Response, Error> result(@NonNull Response value, Object in, Object out) {
            return new Result<>(in, out, value, null);
        }

        public static <Response, Error> Result<Response, Error> error(@NonNull Error error, Object in, Object out) {
            return new Result<>(in, out, null, error);
        }

        public Result<Response, Error> ifPresent(JxFunc.Action<Result<Response, Error>> action) {
            if (action != null && value != null && error == null) {
                action.yield(this);
            }
            return this;
        }

        public Result<Response, Error> orElse(JxFunc.Action<Result<Response, Error>> action) {
            if (action != null && value == null && error != null) {
                action.yield(this);
            }
            return this;
        }
    }
}
