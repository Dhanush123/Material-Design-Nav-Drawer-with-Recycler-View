package com.x10host.dhanushpatel.materialtest;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends android.support.v4.app.Fragment {

   private RecyclerView recyclerView;

   public static final String PREF_FINAL_NAME = "testpref";
   public static final String KEY_USER_LEARNED_DRAWER="user_learned_drawer";
   private ActionBarDrawerToggle mDrawerToggle;
   private DrawerLayout mDrawerLayout;
   private InformationAdapter adapter;

   private boolean mUserLearnedDrawer;//sees if user is aware of drawer open
   private boolean mFromSavedInstanceState; //sees if opening drawer for first time or again
   private View containerView;
    public NavigationDrawerFragment() {

        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer=Boolean.valueOf(readFromPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,"false"));
        if(savedInstanceState!=null){
            //not null means activity or fragment is coming back from rotation
            mFromSavedInstanceState=true;
        }
    }

    public static List<Information> getData()
    {
        List<Information> data = new ArrayList<>();
        int[] icons={R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};
        String[] titles={"T1","T2","T3","T4"};
        for(int i=0;i<titles.length && i<icons.length;i++){
            Information current = new Information();
            current.iconId=icons[i];
            current.title=titles[i];
            data.add(current);
        }
        return data;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView= (RecyclerView) layout.findViewById(R.id.drawerList);
        adapter=new InformationAdapter(getActivity(),getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolbar) {
        containerView=getActivity().findViewById(fragmentId);
        mDrawerLayout=drawerLayout;
        mDrawerToggle= new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
             if(!mUserLearnedDrawer)
             {
                 super.onDrawerOpened(drawerView);
                 mUserLearnedDrawer=true;
                 saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,mUserLearnedDrawer+"");
             }
                getActivity().invalidateOptionsMenu();


            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };
        if(!mUserLearnedDrawer && !mFromSavedInstanceState){
            mDrawerLayout.openDrawer(containerView);
        }
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() { //lets us
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });


    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FINAL_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();
    }
    public static String readFromPreferences(Context context, String preferenceName, String preferenceValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FINAL_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,preferenceValue);
    }

}
