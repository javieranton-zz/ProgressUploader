/*
 * No copyright.. do with it what you want
 */
package com.javieranton;

import com.codename1.system.NativeLookup;
import com.codename1.ui.Display;
import com.codename1.util.OnComplete;

/**
 *
 * @author javier
 */
public class ProgressUploader {
    
    public static OnComplete<CustomNetworkEvent> progressCallback;
    public static OnComplete<CustomNetworkEvent> progressDoneCallback;
    public static OnComplete<CustomNetworkEvent> progressErrorCallback;
    public static void progress(String Progress){
        Display.getInstance().callSerially(()->{
            CustomNetworkEvent ne = new CustomNetworkEvent();
            ne.progress = Progress;
            progressCallback.completed(ne);
        });
    }
    public static void progressDone(String response){
        Display.getInstance().callSerially(()->{
            CustomNetworkEvent ne = new CustomNetworkEvent();
            ne.isDone = true;
            ne.response = response;
            progressDoneCallback.completed(ne);
        });
    }
    public static void progressError(){
        Display.getInstance().callSerially(()->{
            CustomNetworkEvent ne = new CustomNetworkEvent();
            ne.hasError = true;
            progressErrorCallback.completed(ne);
        });
    }
    public static void PostMultipart(String url,String fileContent)
    {
    	ProgressUploaderNativeInterface cn = NativeLookup.create(ProgressUploaderNativeInterface.class);
        if(cn!=null && cn.isSupported())
            cn.PostMultipart(url,fileContent);
    }
}
