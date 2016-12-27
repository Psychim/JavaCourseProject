package com.util.network;

import java.util.concurrent.*;

import com.util.network.HttpRequest._METHOD;

public class HttpRequestThread implements Callable<String> {
    private HttpRequest request;
    public HttpRequestThread(){}
    public HttpRequestThread(HttpRequest newRequest){
        setRequest(newRequest);
    }
    public void setRequest(HttpRequest newRequest){
        request=newRequest;
    }
    public String call(){
        if(request!=null)
            return request.send();
        return null;
    }
    /*
    public static void main(String[] args){
        HttpRequest request=new HttpRequest();
        request.setUrl("http://xk.urp.seu.edu.cn/jw_service/service/stuCurriculum.action");
        request.setParam("returnStr=&queryStudentId=213150865&queryAcademicYear=16-17-2");
        request.setMethod(_METHOD.POST);
        ExecutorService exec=Executors.newCachedThreadPool();
        Future<String> result=exec.submit(new HttpRequestThread(request));
        while(!result.isDone());
        try{
            System.out.print(result.get());
        }catch(InterruptedException e){
            System.out.println(e);
            return;
        }catch(ExecutionException e){
            System.out.println(e);
        }finally{
            exec.shutdown();
        }
    }
    */
}
