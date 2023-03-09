package com.example.signin.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class FaceData implements Serializable {

    /**
     * result : {"face_token":"db7f317080bbcda387099fbfd2fe49ee","user_list":[{"score":100,"group_id":"huiyi_group","user_id":"115","user_info":""}]}
     * log_id : 3441188188
     * error_msg : SUCCESS
     * cached : 0
     * error_code : 0
     * timestamp : 1678330641
     */

    private ResultBean result;
    private long log_id;
    private String error_msg;
    private int cached;
    private int error_code;
    private int timestamp;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public int getCached() {
        return cached;
    }

    public void setCached(int cached) {
        this.cached = cached;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public static class ResultBean implements Serializable {
        /**
         * face_token : db7f317080bbcda387099fbfd2fe49ee
         * user_list : [{"score":100,"group_id":"huiyi_group","user_id":"115","user_info":""}]
         */

        private String face_token;
        private List<UserListBean> user_list = new ArrayList<>();

        public String getFace_token() {
            return face_token;
        }

        public void setFace_token(String face_token) {
            this.face_token = face_token;
        }

        public List<UserListBean> getUser_list() {
            return user_list;
        }

        public void setUser_list(List<UserListBean> user_list) {
            this.user_list = user_list;
        }

        public static class UserListBean implements Serializable {
            /**
             * score : 100
             * group_id : huiyi_group
             * user_id : 115
             * user_info :
             */

            private int score;
            private String group_id;
            private String user_id = "";
            private String user_info;

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public String getGroup_id() {
                return group_id;
            }

            public void setGroup_id(String group_id) {
                this.group_id = group_id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getUser_info() {
                return user_info;
            }

            public void setUser_info(String user_info) {
                this.user_info = user_info;
            }
        }
    }
}
