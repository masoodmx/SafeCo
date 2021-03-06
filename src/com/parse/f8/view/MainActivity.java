package com.parse.f8.view;

import com.parse.f8.R;
import com.parse.f8.R.layout;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements TabListener {
	
	public static final String ADV_SETTING_PREFS = "AdvSettingPrefs";
	ViewPager viewPager;
	ActionBar actionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		///// Initialization
		
		clearAdvSettingPref ();
		// Clear Advanced setting shared preferences values;
		
		// Listener to fragment backstack
//		getSupportFragmentManager().addOnBackStackChangedListener(getListener());
		
		viewPager=(ViewPager) findViewById(R.id.pager);
		FragmentManager fragmentManager = getSupportFragmentManager();
		viewPager.setAdapter(new MyAdapter(fragmentManager));
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				actionBar.setSelectedNavigationItem(arg0);
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		actionBar=getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		ActionBar.Tab home_tab = actionBar.newTab();
		home_tab.setText("Home");
		home_tab.setTabListener(this);
		
		ActionBar.Tab profile_tab = actionBar.newTab();
		profile_tab.setText("Profile");
		profile_tab.setTabListener(this);
		
		ActionBar.Tab setting_tab = actionBar.newTab();
		setting_tab.setText("Setting");
		setting_tab.setTabListener(this);
		
		actionBar.addTab(home_tab);
		actionBar.addTab(profile_tab);
		actionBar.addTab(setting_tab);
	}
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
		
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	private void clearAdvSettingPref () {
		
		SharedPreferences advSettingPref = getSharedPreferences(ADV_SETTING_PREFS, 0);
	    SharedPreferences.Editor editor = advSettingPref.edit();
	    editor.clear();
		editor.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.safeco_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    switch (item.getItemId()) {
        case R.id.menu_about:
            showAboutDialog();
            return true;
        case R.id.menu_signout:

            return true;
        case R.id.menu_exit:
        	finish();
            System.exit(0);
            return true;
        default:
            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void showAboutDialog() {
		
		AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
		alertDialog.setTitle("About");
		alertDialog.setMessage(getResources().getString(R.string.about_text));
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
		    new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		            dialog.dismiss();
		        }
		    });
		alertDialog.show();
	}
	
	private OnBackStackChangedListener getListener()
    {
        OnBackStackChangedListener result = new OnBackStackChangedListener()
        {
            public void onBackStackChanged() 
            {                   
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null)
                {
					SharedPreferences advSettingPref = getSharedPreferences(ADV_SETTING_PREFS, 0);
					if (advSettingPref.getBoolean("editMode", false)) {
						SharedPreferences.Editor editor = advSettingPref.edit();
						editor.putBoolean("editMode", false);
						editor.commit();	
						
	                    SettingAdvEdit currFrag = (SettingAdvEdit)manager.
	                    findFragmentById(R.id.fragment_advedit);
	                    currFrag.onResume();
					}

                }                   
            }
        };

        return result;
    }

}



class MyAdapter extends FragmentPagerAdapter
{

	public MyAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		Fragment fragment=null;
		if(arg0==0)
		{
			fragment=new HomeFragment();
		}	
		if(arg0==1)
		{
			fragment=new ProfileFragment();
		}	
		if(arg0==2)
		{
//			Bundle bundle = new Bundle();
//			bundle.putString("key", "set");
//			fragment.setArguments(bundle);
			fragment=new SettingFragment();
//			fragment=(Fragment) new SettingAdvMain();
		}	
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}
	
}
