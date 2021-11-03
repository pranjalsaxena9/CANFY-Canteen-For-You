package com.anchal.canteentest;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PaymentGateway extends AppCompatActivity
{
    EditText amount, note, name, upivirtualid;
    Button send;
    String TAG ="payment";
    final int UPI_PAYMENT = 0;

    private static double total_amount = 0.0;

    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);
        send = (Button) findViewById(R.id.send);
        amount = (EditText)findViewById(R.id.amount_et);
        note = (EditText)findViewById(R.id.note);


        // Getting reference of database .
        // This database is associated with firebase account of email account LIT2019007@iiitl.ac.in
        database = FirebaseDatabase.getInstance("https://fir-app-36b01-default-rtdb.asia-southeast1.firebasedatabase.app");
        requests = database.getReference("Requests");


        amount.setText(String.valueOf(total_amount));
        amount.setKeyListener(null);




        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting the values from the EditTexts
                if(TextUtils.isEmpty(note.getText().toString().trim())){
                    Toast.makeText(PaymentGateway.this," Note is invalid", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(amount.getText().toString().trim())){
                    Toast.makeText(PaymentGateway.this," Amount is invalid", Toast.LENGTH_SHORT).show();
                }else{
                    // Money will be paid to upi id 'amananc06@okaxis' on behalf of this app
                    // This upi id belongs to Aman Anchal.
                    payUsingUpi("IIITL Canteen App", "amananc06@okaxis",
                            note.getText().toString(), amount.getText().toString());
                }
            }
        });
    }
    void payUsingUpi(  String name,String upiId, String note, String amount)
    {
        Log.e("main ", "name "+name +"--up--"+upiId+"--"+ note+"--"+amount);

        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")

                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // This will allow the user to choose the upi from which the payee wants to pay the money.
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay Using");

        // If any UPI found or not then it will show a toast to the user.
        if(null != chooser.resolveActivity(getPackageManager()))
        {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(PaymentGateway.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply backs out without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }
    private void upiPaymentDataOperation(ArrayList<String> data)
    {
        if (isConnectionAvailable(PaymentGateway.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");

            for (int i = 0; i < response.length; i++)
            {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(PaymentGateway.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "payment successful: "+approvalRefNo);
                finish();
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(PaymentGateway.this, "Payment cancelled.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);
            }
            else {
                Toast.makeText(PaymentGateway.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(PaymentGateway.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public static void setTotal_amount(double amount)
    {
        total_amount = amount;
    }
}