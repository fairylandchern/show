package com.example.a22607.show.httputils;

import com.example.a22607.show.model.User;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by chenmo on 17-3-25.
 */

public class UserDao {
         final static OkHttpClient okHttpClient=new OkHttpClient();
         static String url="http://www.fairybook.club:8080";
    //从服务器获取用户信息
         public static String getUser(String uemail, String upasswd){
             String result = null;
             FormBody formBody=new FormBody.Builder()
                              .add("uemail",uemail)
                              .add("upasswd",upasswd)
                              .build();
             Request request=new Request.Builder()
                     .url(url+"/api/user/login.api")
                     .post(formBody)
                     .build();
             try {
                 Response response=okHttpClient.newCall(request).execute();
                 result=response.body().string();
                 System.out.println("用户的信息是:"+result);
             } catch (IOException e) {
                 e.printStackTrace();
             }
             return result;
         }

         //进行用户信息注册，并且返回一个状态码给前台
         public static String saveUser(User user){
             String result=null;
             FormBody formBody=new FormBody.Builder()
                     .add("uemail",user.getUemail())
                     .add("uname",user.getUname())
                     .add("upasswd",user.getUpasswd())
                     .build();
             Request request=new Request.Builder()
                     .url(url+"/api/user/register.api")
                     .post(formBody)
                     .build();
             Response response= null;
             try {
                 response = okHttpClient.newCall(request).execute();
                 result=response.body().string();
                 System.out.println("返回的状态吗是："+result);

             } catch (IOException e) {
                 e.printStackTrace();
             }
            return  result;
         }
}
