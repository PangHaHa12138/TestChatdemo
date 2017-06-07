package com.panghaha.it.testchatdemo.common;

import java.util.List;

/***
 * ━━━━ Code is far away from ━━━━━━
 * 　　  () 　　　  ()
 * 　　  ( ) 　　　( )
 * 　　  ( ) 　　　( )
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　┻　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━ bug with the more protecting ━━━
 * <p/>
 * Created by PangHaHa12138 on 2017/5/19.
 */
public class Data_ReceiverNews {


    /**
     * result : 17
     * news : [{"newsid":"c62bf4114f3c46c98112a5ee666aff3b",
     * "taskid":"efa1371ccf0641d5b42e0408e71d8bd6",
     * "userid":"02774bc536964386a68bd2b64145c910",
     * "creatime":"2017-05-19 16:32:05.0",
     * "newscontent":"๑乛◡乛๑嘿嘿",
     * "userName":"庞世宇",
     * "path":"http://123.56.97.229/upload/2017051361028810.jpg"}]
     */

    private String result;
    private List<NewsBean> news;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<NewsBean> getNews() {
        return news;
    }

    public void setNews(List<NewsBean> news) {
        this.news = news;
    }

    public static class NewsBean {
        /**
         * newsid : c62bf4114f3c46c98112a5ee666aff3b
         * taskid : efa1371ccf0641d5b42e0408e71d8bd6
         * userid : 02774bc536964386a68bd2b64145c910
         * creatime : 2017-05-19 16:32:05.0
         * newscontent : ๑乛◡乛๑嘿嘿
         * userName : 庞世宇
         * path : http://123.56.97.229/upload/2017051361028810.jpg
         */

        private String newsid;
        private String taskid;
        private String userid;
        private String creatime;
        private String newscontent;
        private String userName;
        private String path;

        public String getNewsid() {
            return newsid;
        }

        public void setNewsid(String newsid) {
            this.newsid = newsid;
        }

        public String getTaskid() {
            return taskid;
        }

        public void setTaskid(String taskid) {
            this.taskid = taskid;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getCreatime() {
            return creatime;
        }

        public void setCreatime(String creatime) {
            this.creatime = creatime;
        }

        public String getNewscontent() {
            return newscontent;
        }

        public void setNewscontent(String newscontent) {
            this.newscontent = newscontent;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }
}
