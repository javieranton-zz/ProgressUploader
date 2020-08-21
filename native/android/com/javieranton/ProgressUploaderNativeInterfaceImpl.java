package com.javieranton;

import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import io.github.lizhangqu.coreprogress.ProgressUIListener;
import io.github.lizhangqu.coreprogress.ProgressHelper;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ProgressUploaderNativeInterfaceImpl {
    public void PostMultipart(String param, String param1) {
        //client
        OkHttpClient okHttpClient = new OkHttpClient();
        //request builder
        Request.Builder builder = new Request.Builder();
        builder.url(param);

        //your original request body
        MediaType CONTENT_TYPE = MediaType.parse("multipart/form-data");
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        java.io.File file = getFileFromString(param1);
        bodyBuilder.setType(CONTENT_TYPE);
        //below an example of how you could add normal form fields on top of files
        /*bodyBuilder.addFormDataPart("title", title);*/
        bodyBuilder.addFormDataPart("payload", file.getName(), RequestBody.create(null, file));
        MultipartBody body = bodyBuilder.build();

        //wrap your original request body with progress
        RequestBody requestBody = ProgressHelper.withProgress(body, new ProgressUIListener() {

            //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
            @Override
            public void onUIProgressStart(long totalBytes) {
                super.onUIProgressStart(totalBytes);
            }

            @Override
            public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                com.javieranton.ProgressUploader.progress(String.valueOf(percent));
            }

            //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
            @Override
            public void onUIProgressFinish() {
                super.onUIProgressFinish();
            }

        });

        //post the wrapped request body
        builder.post(requestBody);
        //call
        Call call = okHttpClient.newCall(builder.build());
        //enqueue
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                com.javieranton.ProgressUploader.progressError();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    com.javieranton.ProgressUploader.progressDone(responseBody.string());
                }catch(Exception e){}
            }
        });
    }

    public boolean isSupported() {
        return true;
    }
    public java.io.File getFileFromString(String content)
    {
        java.io.File file = new java.io.File(context().getFilesDir().getAbsolutePath() + "/temp.txt");
        InputStream i = null;
        String returned = "";
        try {
            if(!file.exists())
                file.createNewFile();
            else
            {
                file.delete();
                file.createNewFile();
            }
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(content);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        } catch (Exception ex) {
            //FileNotFound 
            com.javieranton.ProgressUploader.progressError();
        }
        return file;
    }
    private static android.content.Context context() {
        return com.codename1.impl.android.AndroidNativeUtil.getActivity().getApplicationContext();
    }
}
