package com.example.larsandre.firebasechat.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import com.example.larsandre.firebasechat.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Chat_Room extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<POJO_ChatItem> feedItemList;
    private Chat_Adapter adapterList;

    private DatabaseReference mDatabase;

    private String myName;
    public String mRecipientName;
    private String mRecipientId;
    private String mCurrentUserId;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        //I send these variable from another intent, do it your way. This is mine
        mRecipientId = getIntent().getExtras().getString("mRecipientId");
        mRecipientName = getIntent().getExtras().getString("mRecipientName");
        myName = getIntent().getExtras().getString("myName");

        //UI find view
        final EditText editChat = (EditText) findViewById(R.id.editChat);
        ImageButton btnSend = (ImageButton) findViewById(R.id.btnSend);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);

        //Layout manager
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setStackFromEnd(true);
        recyclerView.setLayoutManager(lm);
        feedItemList = new ArrayList<>();
        adapterList = new Chat_Adapter(this, feedItemList, myName);
        recyclerView.setAdapter(adapterList);

        //Firebase database path
        mDatabase = FirebaseDatabase.getInstance().getReference().child("CHAT");

        //My structure of the database, choose what you prefer
        mDatabase.child(mCurrentUserId).child(mRecipientId).addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                POJO_ChatItem item = dataSnapshot.getValue(POJO_ChatItem.class);
                Log.e("ADDED", item.getMessage());
                feedItemList.add(item);
                adapterList.notifyDataSetChanged();
                //recyclerView.smoothScrollToPosition(feedItemList.size()-1); //If you want a smooth scroll
                recyclerView.scrollToPosition(feedItemList.size()-1);
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                POJO_ChatItem item = dataSnapshot.getValue(POJO_ChatItem.class);
                Log.e("CHANGED", item.getMessage());
            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                POJO_ChatItem item = dataSnapshot.getValue(POJO_ChatItem.class);
                Log.e("MOVED", item.getMessage());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HHmmss");
                String datetime = sdf.format(new Date());
                String chatmessage = editChat.getText().toString();

                if( !TextUtils.isEmpty(chatmessage)){
                    //Again, this is how I build my database, choose what you prefer
                    POJO_ChatItem chat_message = new POJO_ChatItem(mCurrentUserId, myName, chatmessage, datetime);
                    Task<Void> sendTo = mDatabase.child(mRecipientId).child(mCurrentUserId).push().setValue(chat_message);
                    Task<Void> fromMe = mDatabase.child(mCurrentUserId).child(mRecipientId).push().setValue(chat_message);
                    editChat.setText(""); //Clear text input area
                }
            }
        });


    }
}
