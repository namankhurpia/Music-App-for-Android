package com.namankhurpia.cred_challenge.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.namankhurpia.cred_challenge.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class Payment_subscription extends AppCompatActivity implements PaymentResultListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_subscription);

        //already included the razor pay api that allows you to pay from creadit card, net banking, UPI
        //methods of razor pay as given by razor pay documentation-  this is a dummy procudure - as it won't work,as i have removed by api key(for privacy concerns)
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", 50000); // amount in the smallest currency unit
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_rcptid_11");
            orderRequest.put("payment_capture", false);


            //Order order = razorpay.Orders.create(orderRequest);
        } catch (Exception e) {
            // Handle Exception
            System.out.println(e.getMessage());
        }

        Checkout.preload(getApplicationContext());



    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(),"Payment Successfull" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(),"Payment Unsuccessfull" , Toast.LENGTH_SHORT).show();
    }
}
