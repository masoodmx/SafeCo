package com.parse.f8.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.facebook.UiLifecycleHelper;
import com.parse.f8.PickerActivity;
import com.parse.f8.R;
import com.parse.f8.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class SettingAdvCoUser extends Fragment {

	private UiLifecycleHelper uiHelper;
	static final int PICK_FRIENDS_REQUEST = 1;
	public static final String ADV_SETTING_PREFS = "AdvSettingPrefs";
	Button btnFriendPicker;
	Button btnFBPicker;
	EditText txtCoUserAdd;
	ListView listViewFriends;
	TextView advUserTitle;
	TextView advCoUserSelect;
	RadioGroup rGroupAdvUser;
	RadioButton rbtnEveryone;
	RadioButton rbtnSelective;
	ArrayList<String> friendsList = new ArrayList<String>();
	String key;
	String advUserTitleText;
	String advCoUserSelectText;
	ArrayAdapter<String> adapter;
	String userFlag;
	String friendsType;
	
	public SettingAdvCoUser() {
		// Required empty public constructor
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		final View advCoUserView = inflater.inflate(R.layout.setting_advcouser, container, false);
		
	    Bundle args = getArguments();
	    if (args != null && args.containsKey("key")) {
	        key = args.getString("key");
	        Log.d("Bundle", key);
	    }
	    onHelpClicked(advCoUserView, key);
	    	    
	    if (key == "coUser") {
	    	userFlag = "coUserFlag";
	    	friendsType = "coUserIds";
	    	advUserTitleText = "Co-User";
	    	advCoUserSelectText = "Select the users must not have co-location with you:";
	    	
	    }
	    
	    else if (key == "viUser") {
	    	userFlag = "viUserFlag";
	    	friendsType = "viUserIds";
	    	advCoUserSelectText = "Select the users must not view co-location posts you are tagged in:";
	    	advUserTitleText = "View-User";
	    }
	    
	    rbtnEveryone = (RadioButton) advCoUserView.findViewById(R.id.rbutton_everyone);
	    rbtnSelective = (RadioButton) advCoUserView.findViewById(R.id.rbutton_selective);
	    advUserTitle = (TextView) advCoUserView.findViewById(R.id.lbl_advuser);
	    advUserTitle.setText(advUserTitleText);
	    advCoUserSelect = (TextView) advCoUserView.findViewById(R.id.lbl_advCoUserSelect);
	    advCoUserSelect.setText(advCoUserSelectText);
	    txtCoUserAdd = (EditText) advCoUserView.findViewById(R.id.txt_couser_enter);
		btnFriendPicker = (Button) advCoUserView.findViewById(R.id.btn_add_couser);
		btnFBPicker = (Button) advCoUserView.findViewById(R.id.btn_fb_picker);
		
	    listViewFriends = (ListView) advCoUserView.findViewById(R.id.list_cousers_added);
	    listViewFriends.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				v.getParent().requestDisallowInterceptTouchEvent(true);
				return false;
			}
		});
//		setListViewHeightBasedOnChildren(listViewFriends);
		
		initWidgets();
		onSwitchClicked(advCoUserView);
		onOKClicked(advCoUserView);
		
		rGroupAdvUser = (RadioGroup) advCoUserView.findViewById(R.id.radioGroup_advUser);
		rGroupAdvUser.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
				if (checkedId == R.id.rbutton_everyone) {

//					saveAdvSettingPref(friendsType, "$everyone");
					
					txtCoUserAdd.setEnabled(false);
					btnFriendPicker.setEnabled(false);
					btnFBPicker.setEnabled(false);
					if (adapter != null) {
						adapter.clear();
						listViewFriends.setVisibility(View.INVISIBLE);
					}
				}
				else if (checkedId == R.id.rbutton_selective) {
					
					txtCoUserAdd.setEnabled(true);
					btnFriendPicker.setEnabled(true);
					btnFBPicker.setEnabled(true);
				}
			}
		});
		
		btnFriendPicker.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
								
				String friendName = txtCoUserAdd.getText().toString();
				if (friendName != null) {
					friendsList.add(friendName);
				}
				
				listViewFriends.setVisibility(View.VISIBLE);
				adapter = new ArrayAdapter<String>(getActivity(), 
						android.R.layout.simple_list_item_1, friendsList);
				listViewFriends.setAdapter(adapter);

				saveAdvSettingArrayPref(friendsType, friendsList);
				
				txtCoUserAdd.setText("");
				
				
			}
		});
		
		btnFBPicker.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// FIXMED FriendPickFragment is empty! fix or try another solution. Try to use the activity class instead of fragment
				// TASK After friend list worked, get the userFbIds and replace it in Shared Prefs Value.
				
//				Fragment fragment = new FBPickerFragment();
//			    FragmentTransaction transaction = getFragmentManager().beginTransaction();
//			    transaction.replace(R.id.fragment_couser, fragment);
//			    transaction.addToBackStack(null);
//			    transaction.setTransition(4099);	
//			    transaction.commit(); 
				
				startPickerActivity(PickerActivity.FRIEND_PICKER, PICK_FRIENDS_REQUEST);
			}
		});
		
		return advCoUserView;
	}
	
	private void startPickerActivity(Uri data, int requestCode) {
		
//		Toast.makeText(this.getActivity(), "Clicked on Button", Toast.LENGTH_LONG).show();
	     Intent intent = new Intent();
	     intent.setData(data);
	     intent.setClass(getActivity(), PickerActivity.class);
	     startActivityForResult(intent, requestCode);
	 }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == PICK_FRIENDS_REQUEST) {
	      uiHelper.onActivityResult(requestCode, resultCode, data);
	    } else if (resultCode == Activity.RESULT_OK) {
	        // Do nothing for now
	    }
	}
	
	
	private void saveAdvSettingArrayPref(String type, ArrayList<String> values) {
		
		SharedPreferences advSettingPref = this.getActivity().getSharedPreferences(ADV_SETTING_PREFS, 0);
	    SharedPreferences.Editor editor = advSettingPref.edit();

	    String userString = null;
	    for (String item : values) {
	    	if (userString == null) {
	    		userString = item;
	    	} else {
	    		userString = userString + " , " + item;
	    	}
	    }
	    editor.putString(type , userString);
	    editor.putBoolean(userFlag, true);
   
		editor.commit();
		
	}


	public ArrayList<String> restoreAdvSettingPref(String type) {
		SharedPreferences advSettingPref = this.getActivity().getSharedPreferences(ADV_SETTING_PREFS, 0);
	    String json = advSettingPref.getString(type, null);
	    ArrayList<String> friendsList = new ArrayList<String>();
	    if (json != null) {
	        try {
	            JSONArray a = new JSONArray(json);
	            for (int i = 0; i < a.length(); i++) {
	                String friendName = a.optString(i);
	                friendsList.add(friendName);
	            }
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	    }
	    return friendsList;
	}
	
	public void onSwitchClicked(final View v) {
		
		Switch disableSwitch = (Switch) v.findViewById(R.id.switch_user);
		disableSwitch.setChecked(true);
		disableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isChecked) {
					enableWidgets(true);		
				}
				else {
					
					enableWidgets(false);
					removePrefsKeys();
				}
			}
		});

	}

	private void enableWidgets(Boolean enable) {
		
		txtCoUserAdd.setText("");
		if (!enable) {
			txtCoUserAdd.setEnabled(enable);
			btnFriendPicker.setEnabled(enable);
			btnFBPicker.setEnabled(enable);
		}
		if (!enable && (adapter != null)) {
			adapter.clear();
			listViewFriends.setVisibility(View.INVISIBLE);
		}
		for(int i = 0; i < rGroupAdvUser.getChildCount(); i++){
            ((RadioButton)rGroupAdvUser.getChildAt(i)).setEnabled(enable);
        }
		rbtnEveryone.setChecked(true);
//        if (!enable) {
//        	rGroupAdvUser.clearCheck();
//        }
	}
	
	private void removePrefsKeys() {
		
		SharedPreferences advSettingPref = this.getActivity().getSharedPreferences(ADV_SETTING_PREFS, 0);
	    SharedPreferences.Editor editor = advSettingPref.edit();
	    
    	editor.remove(friendsType);
    	editor.putBoolean(userFlag, false);
	    
		editor.commit();
	}
	
	private void onOKClicked(View v) {
		
		ImageView imageOK = (ImageView) v.findViewById(R.id.image_user_OK);
		imageOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getFragmentManager();
				fm.popBackStack();
			}
		});
	}
	
	private void onHelpClicked(View v, final String userType) {
		
		ImageView imageHelp = (ImageView) v.findViewById(R.id.image_user_help);
		imageHelp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
				alertDialog.setTitle("Help");
				if (userType == "coUser") {
					alertDialog.setMessage(getResources().getString(R.string.help_couser));
				}
				else {
					alertDialog.setMessage(getResources().getString(R.string.help_viuser));
				}
				alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
				    new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) {
				            dialog.dismiss();
				        }
				    });
				alertDialog.show();
			}
		});
	}
	
	private void initWidgets() {
		
		SharedPreferences advSettingPref = this.getActivity().getSharedPreferences(ADV_SETTING_PREFS, 0);
		if (advSettingPref.getBoolean(userFlag, false)) {
		
			rbtnSelective.setChecked(true);
			txtCoUserAdd.setEnabled(true);
			btnFriendPicker.setEnabled(true);
	    	friendsList.addAll(Arrays.asList(advSettingPref.getString(friendsType, "null").split("\\s*,\\s*")));
	    	listViewFriends.setVisibility(View.VISIBLE);
			adapter = new ArrayAdapter<String>(getActivity(), 
					android.R.layout.simple_list_item_1, friendsList);
			listViewFriends.setAdapter(adapter);
		}

	}
	
	public static void setListViewHeightBasedOnChildren(ListView listView) {
	    ListAdapter listAdapter = listView.getAdapter();
	    if (listAdapter == null)
	        return;

	    int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.UNSPECIFIED);
	    int totalHeight = 0;
	    View view = null;
	    for (int i = 0; i < listAdapter.getCount(); i++) {
	        view = listAdapter.getView(i, view, listView);
	        if (i == 0)
	            view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LayoutParams.WRAP_CONTENT));

	        view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
	        totalHeight += view.getMeasuredHeight();
	    }
	    ViewGroup.LayoutParams params = listView.getLayoutParams();
	    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	    listView.setLayoutParams(params);
	    listView.requestLayout();
	}
	
//	private void saveAdvSettingPref(String type, String value) {
//		
//		SharedPreferences advSettingPref = this.getActivity().getSharedPreferences(ADV_SETTING_PREFS, 0);
//	    SharedPreferences.Editor editor = advSettingPref.edit();
//	    editor.putString(type , value);
//		editor.commit();
//		
//	}
}



