package com.example.ifind;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutFragment extends Fragment {
    ImageView fblogo;
    LinearLayout linlayout1, linlayout2, linlayout3, linlayout4, linlayout5, linlayout6;
    TextView txtdetail1, txtdetail2, txtdetail3, txtdetail4, txtdetail5, txtdetail6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        linlayout1 = root.findViewById(R.id.linlayout1);
        linlayout1.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        linlayout2 = root.findViewById(R.id.linlayout2);
        linlayout2.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        linlayout3 = root.findViewById(R.id.linlayout3);
        linlayout3.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        linlayout4 = root.findViewById(R.id.linlayout4);
        linlayout4.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        linlayout5 = root.findViewById(R.id.linlayout5);
        linlayout5.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        linlayout6 = root.findViewById(R.id.linlayout6);
        linlayout6.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        txtdetail1 = root.findViewById(R.id.faqdets1);
        txtdetail2 = root.findViewById(R.id.faqdets2);
        txtdetail3 = root.findViewById(R.id.faqdets3);
        txtdetail4 = root.findViewById(R.id.faqdets4);
        txtdetail5 = root.findViewById(R.id.faqdets5);
        txtdetail6 = root.findViewById(R.id.faqdets6);

        fblogo = root.findViewById(R.id.fblogo);

        return root;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void onStart(){
        super.onStart();
        linlayout1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int v = (txtdetail1.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(linlayout1, new AutoTransition());
                txtdetail1.setVisibility(v);
            }
        });
        linlayout2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int v = (txtdetail2.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(linlayout2, new AutoTransition());
                txtdetail2.setVisibility(v);
            }
        });

        linlayout3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int v = (txtdetail3.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(linlayout3, new AutoTransition());
                txtdetail3.setVisibility(v);
            }
        });

        linlayout4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int v = (txtdetail4.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(linlayout4, new AutoTransition());
                txtdetail4.setVisibility(v);
            }
        });

        linlayout5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int v = (txtdetail5.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(linlayout5, new AutoTransition());
                txtdetail5.setVisibility(v);
                fblogo.setVisibility(v);
            }
        });

        linlayout6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int v = (txtdetail6.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(linlayout6, new AutoTransition());
                txtdetail6.setVisibility(v);
            }
        });

        fblogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sAppLink = "https://www.facebook.com/rozvisuals/";
                String sPackage = "com.facebook.katana";
                String sWeblink = "https://www.facebook.com/rozvisuals/";

                openLink(sAppLink, sPackage, sWeblink);
            }
        });


    }
    private void openLink(String sAppLink, String sPackage, String sWebLink) {
        try {
            Uri uri = Uri.parse(sAppLink);
            Intent intent =new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(sPackage);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (ActivityNotFoundException activityNotFoundException){
            Uri uri = Uri.parse(sWebLink);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}