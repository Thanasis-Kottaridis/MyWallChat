package com.unipi.kottarido.mywallchat.mywallchat;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogClass.DialogListener{

    //user info

    private String Username;
    private String Email;

    //orizw FirebaseAuth gia authentication
    //kai firebasedatabase gia ta minimata kai ta data ton xriston
    //episis orizoume kai to database reference
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    //i lista me ta minimata
    private List<Message> myMessages;

    //gia to recycle view
    private RecyclerView chatView;
    private RecyclerView.Adapter chatAdapter;
    private RecyclerView.LayoutManager mlayoutManager;

    //gia to NavigationView
    private NavigationView navigationView;

    private Toolbar CustomToolbar;
    private DrawerLayout drawer;

    //to FAB
    private FloatingActionButton WriteMessageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myMessages = new ArrayList<>();

        //initialize to instance tis FirebaseAuth
        //initialize to instance tis FirebaseDatabase
        // pernei kai to reference stin database apo to opoio tha kanoume read kai write
        //meso tou instance pou dimiourgisame
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        if(mAuth.getCurrentUser() == null){
            startActivity(new Intent(this,SignInActivity.class));
            finish();
        }
        else{
            //diavazei to username tou current user (diladei auton pou einai sindedemenos)
            FirebaseUser user = mAuth.getCurrentUser();
            if(user!= null){
                Username = user.getDisplayName();
                Email = user.getEmail();
            }
        }

        //prosthetei to custom Toolbar sto activity
        CustomToolbar = findViewById(R.id.CustomToolbar);
        setSupportActionBar(CustomToolbar);

        //prosthetei to menu pou ftia3ame sto custom Activity bar mas ??? to kanei me alo tropo apo ton sinithismeno
        drawer = findViewById(R.id.drawer_layout);

        //auto einai gia na kanei to icon na peristrefete otan emfanizete o drawer ???
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,CustomToolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //kanoume desplay ta chat items sto Recycle view!
        //vriskoume apo to id gia poio chat view milame
        chatView = findViewById(R.id.ChatView);

        //ti einai o layoutManager kai o LinearLayoutManager   ?????
        mlayoutManager = new LinearLayoutManager(this);
        //kanei to recycle view na 3ekinaei apo to telos kai na scrolarei pros tin arxi
        ((LinearLayoutManager) mlayoutManager).setStackFromEnd(true);
        chatView.setLayoutManager(mlayoutManager);

        //ftiaxnoume instance tou adapter pou dimiourgisame
        // kai pername auton ton adater sto chat view
        //me auton ton tropo tou leme pos na xiristi ta items
        chatAdapter = new ChatAdapter(myMessages);
        chatView.setAdapter(chatAdapter);

        //on Recycle view item click event einai to interface pou ftia3ame ston adapter
        //to xirizomaste opos xirizomaste kai ta apla onclick event
        ((ChatAdapter)chatAdapter).setOnItemClickListener(new ChatAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Message message) {
                ShowMessage("Message info","This message sent from user: "+message.getUsername()+"\n" +
                        "With Email: "+message.getEmail());
            }
        });

        //arxilopoiei to NavigationView
        navigationView = findViewById(R.id.nav_view);

        //vazw ta stixia tou xristi sta textview tou
        View headerView = navigationView.getHeaderView(0);
        ((TextView)headerView.findViewById(R.id.NavHeaderUsername)).setText(Username);
        ((TextView)headerView.findViewById(R.id.NavHeaderEmail)).setText(Email);

        //ftiaxnw listener gia otan ginete select kapio item tou nav view
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navManageAccount:
                        startActivity(new Intent(getApplicationContext(),ManageAccountActivity.class));
                        break;
                    case R.id.navSettings:
                        Toast.makeText(getApplicationContext(),"Settings", Toast.LENGTH_SHORT);
                        break;
                    case R.id.navLogOut:
                        mAuth.signOut();
                        startActivity(new Intent(getApplicationContext(),SignInActivity.class));
                        finish();
                        break;

                }
                return true;
            }
        });

        //arxikopiei to instance tou FAB kai sti sinexia ftiaxnw
        // ena onClick event pou tha emfanizei to Dialog gia na grapsei to message o user
        WriteMessageButton = findViewById(R.id.WriteMessageButton);
        WriteMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogClass dialog = new DialogClass();
                dialog.setTitle("New Message!");
                dialog.show(getSupportFragmentManager(),"Write new message");
            }
        });

        //pigenei stin firebase sto reference message pou einai ta minimata!
        DatabaseReference messageRef = databaseReference.child("messages");

        //diavazei ta minimata pou einai apothikeumena stin firebase
        //ftiaxnoume enan listener gia na diavazei ta messages apo tin FB
        //kathe fora pou iparxi kapia alagi sta data (prosthiki/tropopoiisi)
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //adiazei ti my message
                myMessages = new ArrayList<>();

                //kathe fora pou alazei kati sta dedomena  travaei ena snapshot ton messages
                //kai ma sto epistrefei opou kathe children autou tou snapshot einai ena message

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Message m = ds.getValue(Message.class);
                    myMessages.add(m);

                    //enimeronei ta messages ston adapter gia na ta kanei desplay sto Recycle view
                    ((ChatAdapter)chatAdapter).setMyMessages(myMessages);
                    chatView.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void applyText(String Answer) {
        Message message = new Message(Username,Email,Answer);

        //paw stin dieuthinsei tis firebase pou exw fiavasi
        //kai dimiourgw ena child node to opoio tha apothikeuei ta messages
        DatabaseReference messageRef = databaseReference.child("messages");

        //pame mesa sto message tis firebase kai vazoume to kenourio minima
       // DatabaseReference newMessageRef = messageRef.push();
        messageRef.push().setValue(message);

    }


    //SHOW DEFAULT DIALOG METHOD
    public void ShowMessage(String title,String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(text);
        builder.show();
    }
}
