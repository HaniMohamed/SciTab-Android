package com.a3louq.sci_tab;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Boolean longClicked=false;
    RelativeLayout details;
    TextView selectedElement, nametxt, categorytxt, colortxt, electronPerShelltxt, phasetxt;

    private DatabaseReference mDatabase;
    String rw = "0";
    ArrayList<Integer> reactionWith = new ArrayList<Integer>();

    final TextView[] textViews= new TextView[118];


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
                    getDetails(textViews[finalI1],colorId);

                }
            });




        }
    }

    public void getReactions(TextView txt) {
        final Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
        final String symbol = txt.getText().toString().split("\n")[1];
        final int number = Integer.parseInt( txt.getText().toString().split("\n")[0]);


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
                            }else if (i==number-1){
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




    }

    public  void getDetails(TextView element, int colorID){

        String symbol = element.getText().toString().split("\n")[1];


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


        String name=null, category=null, color=null, phase=null, electrons=null;
        /*Get values from database here
        .
        .
        .
       */



        nametxt.setText(name);
        categorytxt.setText(category);
        colortxt.setText(color);
        phasetxt.setText(phase);
        electronPerShelltxt.setText(electrons);





    }

    public void outSide(View view) {
        setContentView(R.layout.activity_main);
        defElements();

        details.setVisibility(View.GONE);

    }

    @Override
    public void onBackPressed() {
        setContentView(R.layout.activity_main);
        defElements();

        details.setVisibility(View.GONE);

    }
}
