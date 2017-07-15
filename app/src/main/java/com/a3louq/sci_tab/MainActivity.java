package com.a3louq.sci_tab;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    ImageView tab, arrow;
    Button start;
    Boolean longClicked=false;
    RelativeLayout details, intro;
    TextView introTxt,selectedElement, nametxt, categorytxt, colortxt, electronPerShelltxt, phasetxt;

    private DatabaseReference mDatabase;
    String rw = "0";
    ArrayList<Integer> reactionWith = new ArrayList<Integer>();

    final TextView[] textViews= new TextView[118];


    int state = 0;
    String[] eqTest = {"Reaction1","Reaction2","Reaction3","Reaction4","Reaction5"};
    String[] eqDetails = {"Details1","Details2","Details3","Details4","Details5"};
    String firstElement;
    String secondElement;
    String test;
    String[] tests;
    String eq;
    String equationDetails;


    List<Integer> allElements = Arrays.asList(R.id.e1,R.id.e2,R.id.e3,R.id.e4,R.id.e5,R.id.e6,R.id.e7,R.id.e8,R.id.e9,R.id.e10,
            R.id.e11,R.id.e12,R.id.e13,R.id.e14,R.id.e15,R.id.e16,R.id.e17,R.id.e18,R.id.e19,R.id.e20,
            R.id.e21,R.id.e22,R.id.e23,R.id.e24,R.id.e25,R.id.e26,R.id.e27,R.id.e28,R.id.e29,R.id.e30,
            R.id.e31,R.id.e32,R.id.e33,R.id.e34,R.id.e35,R.id.e36,R.id.e37,R.id.e38,R.id.e39,R.id.e40,
            R.id.e41,R.id.e42,R.id.e43,R.id.e44,R.id.e45,R.id.e46,R.id.e47,R.id.e48,R.id.e49,R.id.e50,
            R.id.e51,R.id.e52,R.id.e53,R.id.e54,R.id.e55,R.id.e56,R.id.e57,R.id.e58,R.id.e59,R.id.e60,
            R.id.e61,R.id.e62,R.id.e63,R.id.e64,R.id.e65,R.id.e66,R.id.e67,R.id.e68,R.id.e69,R.id.e70,
            R.id.e71,R.id.e72,R.id.e73,R.id.e74,R.id.e75,R.id.e76,R.id.e77,R.id.e78,R.id.e79,R.id.e80,
            R.id.e81,R.id.e82,R.id.e83,R.id.e84,R.id.e85,R.id.e86,R.id.e87,R.id.e88,R.id.e89,R.id.e90,
            R.id.e91,R.id.e92,R.id.e93,R.id.e94,R.id.e95,R.id.e96,R.id.e97,R.id.e98,R.id.e99,R.id.e100,
            R.id.e101,R.id.e102,R.id.e103,R.id.e104,R.id.e105,R.id.e106,R.id.e107,R.id.e108,R.id.e109,R.id.e110,
            R.id.e111,R.id.e112,R.id.e113,R.id.e114,R.id.e115,R.id.e116,R.id.e117,R.id.e118);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        defElements();




    }

    public  void defElements(){


        details = (RelativeLayout) findViewById(R.id.details);
        intro = (RelativeLayout) findViewById(R.id.intro);
        tab = (ImageView) findViewById(R.id.tab);
        arrow = (ImageView) findViewById(R.id.arrow);
        start = (Button) findViewById(R.id.start);
        introTxt = (TextView)findViewById(R.id.introText);
        selectedElement = (TextView)findViewById(R.id.selctedElement);
        nametxt = (TextView)findViewById(R.id.name);
        categorytxt = (TextView)findViewById(R.id.category);
        colortxt = (TextView)findViewById(R.id.color);
        phasetxt = (TextView)findViewById(R.id.phase);
        electronPerShelltxt = (TextView)findViewById(R.id.electrons);

        for(int i=0; i<textViews.length;i++){
            textViews[i]= (TextView) findViewById(allElements.get(i));
        }

        for(int i=0; i<allElements.size();i++){

            final int finalI = i;
            textViews[i].setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClicked=true;
                    setContentView(R.layout.activity_main);
                    defElements();
                    getReactions(textViews[finalI]);
                    return true;
                }
            });

            final int finalI1 = i;
            textViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(longClicked) {
                        setContentView(R.layout.activity_main);
                        defElements();
                        longClicked=false;
                    }

                    ColorDrawable viewColor = (ColorDrawable) textViews[finalI1].getBackground();
                    int colorId = viewColor.getColor();
                    if (state == 0) {
                        getDetails(textViews[finalI1], colorId);
                    }else if (state == 1){
                        secondElement = textViews[finalI].getText().toString().split("\n")[1];


                        for (int i=0;i<5;i++){
                            final int finalI2 = i;
                            mDatabase.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    test = dataSnapshot.child(firstElement).child(eqTest[finalI2]).getValue().toString();
                                    if (test.length()>0) {
                                        tests = test.split("\\+|→|1|2|3|4|5|6|7|8|9|0| |\\(");
                                        for (int j=0;j<tests.length;j++){
                                            System.out.println(tests[j] + " " + secondElement);
                                            if ((secondElement.toLowerCase()).equals(tests[j].toLowerCase())){
                                                eq = dataSnapshot.child(firstElement).child(eqTest[finalI2]).getValue().toString();
                                                equationDetails = dataSnapshot.child(firstElement).child(eqDetails[finalI2]).getValue().toString();
                                                react();
                                                break;
                                            }
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError error) {
                                }
                            });



                        }
                    }

                }
            });




        }


        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean firstLaunch = preferences.getBoolean("firstLaunch", true);

        if(firstLaunch){


            intro.setVisibility(View.VISIBLE);
            final Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
            tab.startAnimation(pulse);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    TextView e22 = (TextView) findViewById(R.id.e22);
                    ColorDrawable viewColor = (ColorDrawable) e22.getBackground();
                    int colorId = viewColor.getColor();
                    arrow.setVisibility(View.VISIBLE);
                    getDetails(e22,colorId);

                    start.setEnabled(true);
                }
            },5000);
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(start.getText().toString().equals("next")) {
                        start.setText("Start");
                        details.setVisibility(View.GONE);
                        start.setEnabled(false);
                        arrow.setVisibility(View.GONE);
                        introTxt.setText("Long Tab on Element to show Elments which can react with");
                        final Animation pulse = AnimationUtils.loadAnimation(getApplication(), R.anim.long_pulse);
                        tab.startAnimation(pulse);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                TextView e22 = (TextView) findViewById(R.id.e22);
                                getReactions(e22);
                                getReactions(e22);

                                tab.setVisibility(View.GONE);
                                introTxt.setText("Long Tab on Element to show Elments which can react with" +"\n"+"\n"+"Then you can tab on one of them to show the reaction equation");
                                start.setEnabled(true);
                            }
                        }, 5000);
                    }else {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("firstLaunch",false);
                        editor.apply();

                        setContentView(R.layout.activity_main);
                        defElements();
                    }
                }
            });



        }



    }







    public void react (){

        new SweetAlertDialog(this)
                .setTitleText("Reaction Equation")
                .setContentText(eq + "\n" + equationDetails)
                .show();

        state = 0;
    }










    public void getReactions(TextView txt) {
        final Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        final String symbol = txt.getText().toString().split("\n")[1];
        final int number = Integer.parseInt( txt.getText().toString().split("\n")[0]);
        firstElement = txt.getText().toString().split("\n")[1];


        final ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
        Dialog.setMessage("Loading...");
        Dialog.show();
        mDatabase.addValueEventListener(new ValueEventListener() {
            TextView txtView= (TextView) findViewById(allElements.get(number));

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                rw = dataSnapshot.child(symbol).child("Reaction with").getValue().toString();

                if (rw.equals("0")){
                    Toast.makeText(getApplicationContext(),"No elements can react with " + symbol,Toast.LENGTH_LONG).show();
                    state = 0;
                }else {
                    String[] RW = rw.split(",");

                    for (int i=0;i<RW.length;i++){
                        reactionWith.add(Integer.parseInt(RW[i]));
                    }
                    a:
                    for(int i =0; i<allElements.size(); i++){
                        for (int j=0; j<reactionWith.size();j++){
                            if((i==reactionWith.get(j)-1)){
                                txtView= (TextView) findViewById(allElements.get(i));
                                txtView.startAnimation(pulse);
                                continue a;
                            }
                            else if (i==number-1){
                                continue a;
                            }
                        }
                        txtView= (TextView) findViewById(allElements.get(i));
                        txtView.setBackgroundColor(getResources().getColor(R.color.numBack));
                        txtView.setTextColor(Color.GRAY);
                    }
                }
                reactionWith.clear();
                Dialog.hide();
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        state = 1;

    }

    public  void getDetails(TextView element, int colorID){

        final String symbol = element.getText().toString().split("\n")[1];


        details.setVisibility(View.VISIBLE);
        int cx = (details.getLeft() + details.getRight()) / 2;
        int cy = details.getTop();
        int finalRadius = Math.max(details.getWidth(), details.getHeight());

        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(details, cx, cy, 0, finalRadius);
        }
        details.setBackgroundColor(colorID);
        anim.setDuration(500);
        anim.start();
        selectedElement.setText(element.getText().toString());


        final String[] name = {null};
        final String[] category = {null};
        final String[] color={null};
        final String[] phase={null};
        final String[] electrons={null};


        final ProgressDialog Dialog = new ProgressDialog(MainActivity.this);
        Dialog.setMessage("Loading...");
        Dialog.show();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name[0] =dataSnapshot.child(symbol).child("Name").getValue().toString();
                category[0] =dataSnapshot.child(symbol).child("Category").getValue().toString();
                color[0] =dataSnapshot.child(symbol).child("Color").getValue().toString();
                phase[0] =dataSnapshot.child(symbol).child("Phase").getValue().toString();
                electrons[0] =dataSnapshot.child(symbol).child("Electrons per shell").getValue().toString();

                nametxt.setText(name[0]);
                categorytxt.setText(category[0]);
                colortxt.setText(color[0]);
                phasetxt.setText(phase[0]);
                electronPerShelltxt.setText(electrons[0]);

                Dialog.hide();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });






    }

    public void outSide(View view) {
        setContentView(R.layout.activity_main);
        defElements();
        state =0;

        details.setVisibility(View.GONE);

    }

    @Override
    public void onBackPressed() {
        setContentView(R.layout.activity_main);
        defElements();
        state = 0;

        details.setVisibility(View.GONE);

    }
}
