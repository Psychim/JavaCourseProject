package com.util.network;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.regex.*;
/**
 * 发送http请求
 * @author 施超敏
 *
 */
public class HttpRequest {
    private String url;
    private String cookie;
    private String param;
    public static enum _METHOD{
        GET,POST
    }
    private _METHOD method;
    public HttpRequest(){
        method=_METHOD.GET;
    }
    public HttpRequest(String pUrl){
        this();
        setUrl(pUrl);
    }
    public void setUrl(String pUrl){
        url=pUrl;
    }
    public void setCookie(String pCookie){
        cookie=pCookie;
    }
    public void setParam(String pParam){
        param=pParam;
    }
    public void setMethod(_METHOD pMethod){
        method=pMethod;
    }
    public String send(){
        if(url!=null&&!url.isEmpty()){
            switch(method){
                case GET:
                    return sendGet();
                case POST:
                    return sendPost();
            }
        }
        return null;
    }
    public String sendGet(){
        String result=null;
        BufferedReader in =null;
        try{
            String urlString=url+"?"+param;
            URL urlObj=new URL(urlString);
            URLConnection connection=urlObj.openConnection();
            connection.setRequestProperty("User-Agent"," Mozilla/5.0 (Windows NT 10.0; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            if(cookie!=null&&!cookie.isEmpty())
                connection.setRequestProperty("Cookie",cookie);
            connection.connect();
            Map<String,List<String>> map=connection.getHeaderFields();
            //获取网页字符集
            String charset="GBK";
            List<String> values=map.get("Content-Type");
            if(values!=null&&!values.isEmpty()) {
                for (String value : values) {
                    Matcher matcher = Pattern.compile("charset=(.*)").matcher(value);
                    if (matcher.find()) {
                        charset = matcher.group(1);
                    }
                }
            }
            in=new BufferedReader(new InputStreamReader(connection.getInputStream(),charset));
            String line;
            while((line=in.readLine())!=null){
                result+=line;
            }
        }catch(Exception e){
            System.out.println("发送GET请求出现异常"+e);
            e.printStackTrace();
        }
        finally{
            try{
                if(in!=null)
                    in.close();
            }catch(Exception e2){
                e2.printStackTrace();
            }
        }
        return result;
    }
    public String sendPost(){
        String result="";
        BufferedReader in =null;
        PrintWriter out =null;
        try{
            String urlString=url;
            URL urlObj=new URL(urlString);
            URLConnection connection=urlObj.openConnection();
            connection.setRequestProperty("User-Agent"," Mozilla/5.0 (Windows NT 10.0; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("accept","*/*");
            connection.setRequestProperty("connection","Keep-Alive");
            if(cookie!=null&&!cookie.isEmpty())
                connection.setRequestProperty("Cookie",cookie);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            out=new PrintWriter(connection.getOutputStream());
            out.print(param);
            out.flush();
            Map<String,List<String>> map=connection.getHeaderFields();
            //获取网页字符集
            String charset="GBK";
            List<String> values=map.get("Content-Type");
            if(values!=null&&!values.isEmpty()) {
                for (String value : values) {
                    Matcher matcher = Pattern.compile("charset=(.*)").matcher(value);
                    if (matcher.find()) {
                        charset = matcher.group(1);
                    }
                }
            }
            in=new BufferedReader(new InputStreamReader(connection.getInputStream(),charset));
            String line;
            while((line=in.readLine())!=null){
                result+=line;
            }
        }catch(Exception e){
            System.out.println("发送POST请求出现异常\n"+e);
            e.printStackTrace();
        }
        finally{
            try{
                if(out!=null)
                    out.close();
                if(in!=null)
                    in.close();
            }catch(Exception e2){
                e2.printStackTrace();
            }
        }
        return result;
    }
    /*
    public static void main(String[] args){
        HttpRequest get=new HttpRequest();
        get.url="http://xk.urp.seu.edu.cn/jw_service/service/stuCurriculum.action";
        get.param="returnStr=&queryStudentId=213150865&queryAcademicYear=16-17-2";
        get.method=_METHOD.POST;
        System.out.print(get.send());
    }
    */
}
