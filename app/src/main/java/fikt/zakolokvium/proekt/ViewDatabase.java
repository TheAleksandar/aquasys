package fikt.zakolokvium.proekt;

import android.app.Notification;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static fikt.zakolokvium.proekt.App.CHANNEL_1_ID;

/**
 * Created by User on 2/8/2017.
 */

public class ViewDatabase extends AppCompatActivity {
    private static final String TAG = "ViewDatabase";
    private NotificationManagerCompat notificationManager;

    private static final String CHANNEL_ID ="cannel1" ;


    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private  String userID;

    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        notificationManager = NotificationManagerCompat.from(this);

        setContentView(R.layout.view_database_layout);

        mListView = (ListView) findViewById(R.id.listview);

        //declare the database reference object. This is what we use to access the database.
        //NOTE: Unless you are signed in, this will not be useable.
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
                // ...

            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }





    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            UserInformation uInfo = new UserInformation();

            uInfo.sethumidity(ds.child(userID).getValue(UserInformation.class).gethumidity()); //set the humidity
            uInfo.setWaterLevel(ds.child(userID).getValue(UserInformation.class).getWaterLevel()); //set the email
            uInfo.setsystemid(ds.child(userID).getValue(UserInformation.class).getsystemid()); //set the phone_num


            int num1=Integer.parseInt(uInfo.gethumidity());
            int num2=Integer.parseInt(uInfo.getWaterLevel());
            if(num1<30)
            {
               shownotification();

                toastMessage("Humidity state is Low!!!");
            }
            if(num2<3)
            {
                shownotification2();
                toastMessage("The Water Thank is low!!!");
            }


            //display all the information
            Log.d(TAG, "showData: humidity: " + uInfo.gethumidity());
            Log.d(TAG, "showData: email: " + uInfo.getWaterLevel());
            Log.d(TAG, "showData: phone_num: " + uInfo.getsystemid());

            ArrayList<String> array  = new ArrayList<>();
            array.add("Humidity:    "+uInfo.gethumidity());
            array.add("Water Level: "+uInfo.getWaterLevel());
            array.add("System ID:   "+uInfo.getsystemid());
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
            mListView.setAdapter(adapter);

//test the end

        }
    }

    private void shownotification() {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.humidity2)
                .setContentTitle("Alert!!!")
                .setContentText("Humidity state is low check your plant!!!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);

    }

    private void shownotification2() {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.humidity2)
                .setContentTitle("Alert!!!")
                .setContentText("Water level is low re fill water tank!!!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(2, notification);

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
