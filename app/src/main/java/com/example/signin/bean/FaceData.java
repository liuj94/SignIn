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

    public ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
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


            private String user_id = "";

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }


        }
    }
}
