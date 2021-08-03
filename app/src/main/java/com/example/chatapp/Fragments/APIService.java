package com.example.chatapp.Fragments;

import com.example.chatapp.Notifications.MyResponse;
import com.example.chatapp.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAeesCf2w:APA91bHX10yC5UK5QG0K38xrlOHesym5BL1b1eyQT_wxKbuw8-etwKtIeADCqQZ3OlH0bajAvioCjy3dNQmrIkyNU8DH0AF9MNjJD7QKWWbObc2YlR6Hp6YnJg5X0gRjNWoiGJQ1qxC-"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
