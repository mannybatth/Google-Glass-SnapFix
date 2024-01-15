package com.angieslist.snapfix.glass.net;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by sjs on 5/10/14.
 */
public class ServiceRequest {

    private static final String TAG = "SnapFixGlass";
    private static ServiceRequest ourInstance = new ServiceRequest();

    public static ServiceRequest getInstance() {
        return ourInstance;
    }

    private ServiceRequest() {
    }


    public interface FutureCallback<T> {
        /**
         * onCompleted is called by the Future with the result or exception of the asynchronous operation.
         * @param e Exception encountered by the operation
         * @param result Result returned from the operation
         */
        public void onCompleted(Exception e, T result);
    }

    //static NSString *const ALSnapFixPathSRSubmit                = @"/api/service_request";
    //static NSString *const ALSnapFixPathSRMedia                 = @"/api/service_request/%@/media";
    //static NSString *const ALSnapFixPathSRMediaValidate         = @"/api/media/%@/validate";

    public static void submitServiceRequest(final Context context,
                                   final String fullPath) {

        String cookie = "authToken=4f03c658-6fbf-49a9-86b7-290a7ab10862";
        String url = "http://servicetown-qa.originate.com/api/service_request";

        JsonObject object = new JsonObject();
        object.addProperty("userId", 28);
        object.addProperty("addressId", 10);
        object.addProperty("category", "Other");
        object.addProperty("status", "created");
        object.addProperty("description", "Snapfix Project");
        object.addProperty("title", "Snapfix Project");


        JsonObject object1 = new JsonObject();
        object1.addProperty("date", 1401336000000l);
        object1.addProperty("timeBlock", "2pm-4pm");
        object1.addProperty("rank", 1);

        JsonParser parser = new JsonParser();
        String jsonStr = "[{\"date\":1401336000000,\"timeBlock\":\"2pm-4pm\",\"rank\":1}]";

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(object1);

        object.add("availability", jsonArray);


//        Ion.with(context)
//                .load(url)
//                .addHeader("Cookie", cookie)
//                .setJsonObjectBody(object)
//                .asJsonObject()
//                .setCallback(new com.koushikdutta.async.future.FutureCallback<JsonObject>() {
//                    @Override
//                    public void onCompleted(Exception e, JsonObject result) {
//                        // do stuff with the result or error
//                        Log.d(TAG, "JSON :" + result + " Error: " + e);
//                        JsonObject data = (JsonObject)result.getAsJsonArray("data").get(0);
//                        JsonObject serviceRequest = data.getAsJsonObject("serviceRequest");
//                        String projectId = serviceRequest.get("id").getAsString();
//
//                        Log.d(TAG, "ServiceRequest " + serviceRequest);
//
//                        uploadMedia(context, projectId, "image/jpeg", "project.jpeg", fullPath);
//                    }
//                });

    }


    public static void uploadMedia(final Context context,
                                   String projectId, String mediaType, final String fileName, final String fullPath) {

        String cookie = "authToken=4f03c658-6fbf-49a9-86b7-290a7ab10862";
        String url = "http://servicetown-qa.originate.com/api/service_request/" + projectId + "/media";
        Log.d(TAG, "Adding media to project " + projectId + " filename "
                + fileName + " using " + url//MemberServerUrls.getAddMediaUrl(projectId)
                + "cookie = " + cookie);


        JsonObject object = new JsonObject();
        object.addProperty("category", mediaType);
        object.addProperty("fileName", fileName);

        Ion.with(context)
                .load(url)
                .addHeader("Cookie", cookie).setJsonObjectBody(object)
                .asJsonObject()
                .setCallback(new com.koushikdutta.async.future.FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        //Log.d(TAG, "JSON :" + result + " Error: " + e);
                        JsonObject data = (JsonObject)result.getAsJsonArray("data").get(0);
                        String putURL = data.get("putUrl").getAsString();
                        String mediaId = data.get("id").getAsString();

                        Log.d(TAG, "PUTUrl " + putURL + "Media Id" + mediaId);

                        File file = new File(fullPath);
                        // Log.d(TAG, data.getAsString());
                        sendMedia(context, putURL,  fileName, file, "image/jpeg", mediaId);
                    }
                });

    }

    public static String sendMedia(Context context, final String uploadUrl,
                                   String fileName, final File file, final String contentType, String mediaId
    ) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        String cookie = "authToken=4f03c658-6fbf-49a9-86b7-290a7ab10862";
        String retVal = null;
        HttpURLConnection urlconnection = null;
        try {
            URL url = new URL(uploadUrl);
            urlconnection = (HttpURLConnection) url.openConnection();
            urlconnection.setDoOutput(true);
            urlconnection.setDoInput(true);
            try {
                urlconnection.setRequestMethod("PUT");
                urlconnection.setRequestProperty("Content-Type", contentType);
                Log.d(TAG, "Uploading content length " + file.length());
                urlconnection.setRequestProperty("Content-Length", "" + file.length());
                urlconnection.connect();
            } catch (ProtocolException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "Uploading file");
            BufferedOutputStream bos = new BufferedOutputStream(urlconnection
                    .getOutputStream());
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
                    file));
            int i;
            byte[] buffer = new byte[1024];
            // read by buffer length until end of stream
            while ((i = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, i);
            }
            bos.close();
            bis.close();
            Log.d(TAG, "Response Code " + urlconnection.getResponseCode());
            validateMedia(context,mediaId);
            //callback.onCompleted(null, urlconnection.getResponseMessage());
            retVal = urlconnection.getResponseMessage();
            urlconnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retVal;
    }


    public static void validateMedia(final Context context,
                                   String mediaId) {

        String cookie = "authToken=4f03c658-6fbf-49a9-86b7-290a7ab10862";
///api/media/%@/validate

        Log.d(TAG, "Media ID" + mediaId);
        String url = "http://servicetown-qa.originate.com/api/media/" + mediaId + "/validate";

        JsonObject object = new JsonObject();

        Ion.with(context)
                .load("PUT",url)
                .addHeader("Cookie", cookie)
                .asJsonObject()
                .setCallback(new com.koushikdutta.async.future.FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        Log.d(TAG, "JSON :" + result + " Error: " + e);
                        //JsonObject data = (JsonObject)result.getAsJsonArray("data").get(0);
                        //String url = data.get("url").getAsString();
                        // Log.d(TAG, "Data " + url);
                    }
                });

    }



}
