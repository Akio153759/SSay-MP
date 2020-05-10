package com.tamadev.ssay_mp.utils;

import android.os.AsyncTask;

import com.google.gson.Gson;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendNotificationFCM extends AsyncTask<Void, Void,Void> {
    private final String svKey = "AAAAaUqC024:APA91bEz9PTSkgAx7270_KcbybDWgGKcAZ72jHIue1QOUHnN7eQOx431jmYSHDhxYxX5gCVAGuPWUTabPVefXXCxBhXcIzCQcqMSIV6IIR80TSDtpQ9X5uKj64XEPCl38dy38alxFzef";
    private final String url = "https://fcm.googleapis.com/fcm/send";
    private final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String notificationTitle;
    private String notificationBody;
    private String tokenUser;

    public SendNotificationFCM(String notificationTitle, String notificationBody, String tokenUser) {
        this.notificationTitle = notificationTitle;
        this.notificationBody = notificationBody;
        this.tokenUser = tokenUser;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        JSONObject object = null;
        OkHttpClient client = new OkHttpClient();
        String jsonBody = "{\n" +
                "  \"to\": \"" + tokenUser + "\",\n" +
                "  \"notification\": {\n" +
                "    \"title\": \"" + notificationTitle +"\",\n" +
                "    \"body\": \"" + notificationBody + "\"\n" +
                "  }\n" +
                "}";
        RequestBody body = RequestBody.create(JSON,jsonBody);
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "key="+svKey)
                .header("Content-Type", "application/json")
                .post(body)
                .build();


        try (Response response = client.newCall(request).execute()) {
            object = new JSONObject(response.body().string());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
