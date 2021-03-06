package com.parse.f8.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.facebook.model.GraphUser;
import com.parse.f8.R;
import com.parse.f8.R.layout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class SettingAdvTime extends Fragment {

	public static final String ADV_SETTING_PREFS = "AdvSettingPrefs";
	static final int TIME_DIALOG_ID = 999;
	private Spinner spinnerAdvTime;
	private RadioGroup radioGroupAdvTime;
	private Button btnAdvStartTime;
	private Button btnAdvEndTime;
	private TextView txtSpecificDate;
	private Calendar calendar;
	Boolean initSpecDate = false;
	
	public SettingAdvTime() {
		// Required empty public constructor
	}
	// TASK Check input data validation!
	// FIXMED Add Whole day and put it as default, also put everyday as default
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View advTimeView = inflater.inflate(R.layout.setting_advtime, container, false);
		txtSpecificDate = (TextView) advTimeView.findViewById(R.id.txt_specificDate);
		calendar = Calendar.getInstance();

		
		
		advTimePeriodListener(advTimeView);
		
		advTimeDayPartListener(advTimeView);
		
		onSwitchClicked(advTimeView);
		
		onSpecificDateClicked();
		
		onOKClicked(advTimeView);
		
		onHelpClicked(advTimeView);
		
		initWidgets(advTimeView);
		
		return advTimeView;
	}

	
	private void advTimePeriodListener(View v) {
		
		spinnerAdvTime = (Spinner) v.findViewById(R.id.spinner_time); 
		ArrayAdapter<CharSequence> advTimeAdapter = ArrayAdapter.createFromResource
				(getActivity(), R.array.spinner_time_list, android.R.layout.simple_spinner_item);
		advTimeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerAdvTime.setAdapter(advTimeAdapter);
		spinnerAdvTime.setPrompt("Select a time period:");
		spinnerAdvTime.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				
				txtSpecificDate.setVisibility(View.GONE);
				String selectedItem = parent.getItemAtPosition(pos).toString();
				Log.d("MyDebug", "Spinner selected item is: " + selectedItem);
				if (pos != 0) {
					saveAdvSettingPref("timePeriod", selectedItem);
					saveAdvSettingIntegerPref("dayOfWeek", pos);
					if (pos == 10) {
						if (!initSpecDate) {
							setSpecificDate();
						} else {
							txtSpecificDate.setVisibility(View.VISIBLE);
						}
					}
//					Toast.makeText(parent.getContext(), "MyDebug:" + selectedItem, Toast.LENGTH_SHORT).show();
				}
				initSpecDate = false;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				
			}
		});
	}
	
	private void advTimeDayPartListener(final View v) {
		
		radioGroupAdvTime = (RadioGroup) v.findViewById(R.id.rgroup_advTime);
		radioGroupAdvTime.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
				switch (checkedId) {
		        case -1:
		          Log.d("MyDebug", "All Choices cleared!");
		          break;
		        case R.id.rbtn_advTime_allday:
//		          Log.d("MyDebug", "Chose rbtn_advTime_wholeday");
		          enableTimePickers(v, false);
		          saveAdvSettingPref("timeDayPart", "allday");
		          saveAdvSettingPref("timeStart", "0:0");
		          saveAdvSettingPref("timeEnd", "23:59");
		          break;
		        case R.id.rbtn_advTime_mornings:
		          Log.d("MyDebug", "Chose rbtn_advTime_mornings");
		          enableTimePickers(v, false);
		          saveAdvSettingPref("timeDayPart", "mornings");
		          saveAdvSettingPref("timeStart", "6:0");
		          saveAdvSettingPref("timeEnd", "10:0");
		          break;
		        case R.id.rbtn_advTime_afternoons:
//		          Log.d("MyDebug", "Chose rbtn_advTime_afternoons");
		          enableTimePickers(v, false);
		          saveAdvSettingPref("timeDayPart", "afternoons");
		          saveAdvSettingPref("timeStart", "12:0");
		          saveAdvSettingPref("timeEnd", "15:0");
		          break;
		        case R.id.rbtn_advTime_evenings:
//		          Log.d("MyDebug", "Chose rbtn_advTime_evenings");
		          enableTimePickers(v, false);
		          saveAdvSettingPref("timeDayPart", "evenings");
		          saveAdvSettingPref("timeStart", "18:0");
		          saveAdvSettingPref("timeEnd", "23:0");
		          break;
		        case R.id.rbtn_advTime_nights:
//		          Log.d("MyDebug", "Chose rbtn_advTime_nights");
		          enableTimePickers(v, false);
		          saveAdvSettingPref("timeDayPart", "nights");
		          saveAdvSettingPref("timeStart", "0:0");
		          saveAdvSettingPref("timeEnd", "6:0");
		          break;
		        case R.id.rbtn_advTime_between:
//		          Log.d("MyDebug", "Chose rbtn_advTime_between");
		          enableTimePickers(v, true);
		          saveAdvSettingPref("timeDayPart", "slot");
		          advTimePickerListener(v);
//		          getScrollFocus(v);
		          break;
		        default:
		          Log.d("MyDebug", "Nothing!");
		          break;
		        }
			}
		});
		
	}
	
	private void advTimePickerListener(View v) {
		
		btnAdvStartTime = (Button) v.findViewById(R.id.btn_advTimePicker1);
		btnAdvEndTime = (Button) v.findViewById(R.id.btn_advTimePicker2);
		
		if(btnAdvStartTime.getText().toString() != getString(R.string.start_time)) {
			saveAdvSettingPref("timeStart", btnAdvStartTime.getText().toString());
		}
		if(btnAdvEndTime.getText().toString() != getString(R.string.end_time)) {
			saveAdvSettingPref("timeEnd", btnAdvEndTime.getText().toString());
		}
		
		btnAdvStartTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Boolean startValue = true;
				onTimePickerButtonClicked(v, startValue);
			}
		});
		
		btnAdvEndTime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Boolean startValue = false;
				onTimePickerButtonClicked(v, startValue);
			}
		});
	}
	
	private void onTimePickerButtonClicked(final View v, final Boolean startValue) {
		
		// Process to get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        
        // Launch Time Picker Dialog
        TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                            int minute) {
                        // Display Selected time in textbox
                    	String timeStr = pad(hourOfDay) + ":" + pad(minute);
//                    	String timeStr = Integer.toString(hourOfDay) + ":" + Integer.toString(minute);
//                    	String timeStr = new StringBuilder().append(pad(hourOfDay)).append(":").append(pad(minute));
                        Log.d("MyDebug", timeStr);
                        if (startValue) {
                        	btnAdvStartTime = (Button) v.findViewById(R.id.btn_advTimePicker1);
                        	// TASK Time on the button is not shown properly when it's like: 02:05. It shows 2:5
                        	btnAdvStartTime.setText(timeStr);
                        	saveAdvSettingPref("timeStart", timeStr);
                        }
                        else {
                        	btnAdvEndTime = (Button) v.findViewById(R.id.btn_advTimePicker2);
                        	btnAdvEndTime.setText(timeStr);
                        	saveAdvSettingPref("timeEnd", timeStr);
                        }
                    }
                }, mHour, mMinute, false);
        tpd.show();
	}
	
	private String pad(int c) {
		if (c >= 10)
		   return String.valueOf(c);
		else
		   return "0" + String.valueOf(c);
	}
	
	private void enableTimePickers (View v, Boolean enable) {
		
		btnAdvStartTime = (Button) v.findViewById(R.id.btn_advTimePicker1);
		TextView lblAdvTimeUntil = (TextView) v.findViewById(R.id.lbl_advTime_until);
		btnAdvEndTime = (Button) v.findViewById(R.id.btn_advTimePicker2);
		btnAdvStartTime.setEnabled(enable);
		btnAdvEndTime.setEnabled(enable);
		if (enable)
			lblAdvTimeUntil.setTextColor(Color.BLACK);
		else
			lblAdvTimeUntil.setTextColor(Color.GRAY);
	}
	
	private void saveAdvSettingPref(String type, String value) {
		
		SharedPreferences advSettingPref = this.getActivity().getSharedPreferences(ADV_SETTING_PREFS, 0);
	    SharedPreferences.Editor editor = advSettingPref.edit();
	    editor.putString(type , value);
	    editor.putBoolean("timeFlag", true);
		editor.commit();
		
	}
	
	private void saveAdvSettingIntegerPref(String type, int value) {
		
		SharedPreferences advSettingPref = this.getActivity().getSharedPreferences(ADV_SETTING_PREFS, 0);
	    SharedPreferences.Editor editor = advSettingPref.edit();
	    editor.putInt(type ,value);
		editor.commit();
		
	}
	
	private Date stringToDateConvertor(String strTime) {
		// TASK Use it before save all info in Parse and convert to the proper format! CODE NOT TESTED!!!
		
		Date time = null;
		SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
		try {
			time = format.parse(strTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}
	
	
	
	public void onSwitchClicked(final View v) {
		
		spinnerAdvTime = (Spinner) v.findViewById(R.id.spinner_time); 
		radioGroupAdvTime = (RadioGroup) v.findViewById(R.id.rgroup_advTime);
		final RadioButton rbtnMorning = (RadioButton) v.findViewById(R.id.rbtn_advTime_mornings);
		final LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout_advtime);
		btnAdvStartTime = (Button) v.findViewById(R.id.btn_advTimePicker1);
		btnAdvEndTime = (Button) v.findViewById(R.id.btn_advTimePicker2);
		Switch disableSwitch = (Switch) v.findViewById(R.id.switch_time);
		disableSwitch.setChecked(true);
		disableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isChecked) {
					enableRadioGroup(radioGroupAdvTime, true);
					enableLinearLayout(layout, true);		
				}
				else {
					spinnerAdvTime.setSelection(0);
					rbtnMorning.setChecked(true);
					enableRadioGroup(radioGroupAdvTime, false);
					enableLinearLayout(layout, false);	
					enableTimePickers(v, false);
					btnAdvStartTime.setText(getString(R.string.start_time));
					btnAdvEndTime.setText(getString(R.string.end_time));
					txtSpecificDate.setText("");
					txtSpecificDate.setVisibility(View.GONE);
					removePrefsKeys();
				}
			}
		});

	}
	
	private void onSpecificDateClicked() {
		
		txtSpecificDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				setSpecificDate();
			}
		});
	}
	
	private void setSpecificDate() {

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dpd = new DatePickerDialog(getActivity(), 
				new OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {

//						String specificDate = "" + dayOfMonth + " " + (monthOfYear+1) + " " + year;
						String specificDate = "" + pad(dayOfMonth) + " / " + pad(monthOfYear+1) + " / " + pad(year);
						saveAdvSettingPref("exactDate", specificDate);
						txtSpecificDate.setVisibility(View.VISIBLE);
						txtSpecificDate.setText(specificDate);
						
						calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						calendar.set(Calendar.MONTH, monthOfYear);
						calendar.set(Calendar.YEAR, year);
					}
				}, mYear, mMonth, mDay);
		dpd.show();
	}
	
	private void onOKClicked(View v) {
		
		ImageView imageOK = (ImageView) v.findViewById(R.id.image_time_OK);
		imageOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getFragmentManager();
				fm.popBackStack();
			}
		});
	}
	
	private void onHelpClicked(View v) {
		
		ImageView imageHelp = (ImageView) v.findViewById(R.id.image_time_help);
		imageHelp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
				alertDialog.setTitle("Help");
				alertDialog.setMessage(getResources().getString(R.string.help_time));
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
	
	private void enableRadioGroup(RadioGroup radioGroup, Boolean enable) {
		
        for(int i = 0; i < radioGroup.getChildCount(); i++){
            ((RadioButton)radioGroup.getChildAt(i)).setEnabled(enable);
        }
        if (!enable) {
        	radioGroup.clearCheck();
        }
	}
	
	private void enableLinearLayout(LinearLayout layout, Boolean enable) {
		
		for ( int i = 0; i < layout.getChildCount();  i++ ){
		    View view = layout.getChildAt(i);
		    view.setEnabled(enable);		
		}
	}
	
	private void removePrefsKeys() {
		
		SharedPreferences advSettingPref = this.getActivity().getSharedPreferences(ADV_SETTING_PREFS, 0);
	    SharedPreferences.Editor editor = advSettingPref.edit();
	    editor.remove("timeStart");
	    editor.remove("timeEnd");
	    editor.remove("timeDayPart");
	    editor.remove("exactDate");
	    editor.remove("timePeriod");
	    editor.remove("dayOfWeek");
	    editor.putBoolean("timeFlag", false);
		editor.commit();
	}
	
	private void initWidgets(View v) {
		
		SharedPreferences advSettingPref = this.getActivity().getSharedPreferences(ADV_SETTING_PREFS, 0);
		if (advSettingPref.getBoolean("timeFlag", false)) {
			
			int dayOfWeek = advSettingPref.getInt("dayOfWeek", 0);
			initSpecDate = true;
			spinnerAdvTime.setSelection(dayOfWeek);
			if (dayOfWeek == 10) {
				txtSpecificDate.setVisibility(View.VISIBLE);
				txtSpecificDate.setText(advSettingPref.getString("exactDate", "N/A"));
			}
			
			String timeDayPart = advSettingPref.getString("timeDayPart", null);
			switch (timeDayPart) {
			case "allday":
				RadioButton rbtnallday = (RadioButton) v.findViewById(R.id.rbtn_advTime_allday);
				rbtnallday.setChecked(true);
				break;
			case "mornings":
				RadioButton rbtnMornings = (RadioButton) v.findViewById(R.id.rbtn_advTime_mornings);
				rbtnMornings.setChecked(true);
				break;
			case "afternoons":
				RadioButton rbtnAfternoons = (RadioButton) v.findViewById(R.id.rbtn_advTime_afternoons);
				rbtnAfternoons.setChecked(true);
				break;
			case "evenings":
				RadioButton rbtnEvenings = (RadioButton) v.findViewById(R.id.rbtn_advTime_evenings);
				rbtnEvenings.setChecked(true);
				break;
			case "nights":
				RadioButton rbtnNights = (RadioButton) v.findViewById(R.id.rbtn_advTime_nights);
				rbtnNights.setChecked(true);
				break;
			case "slot":
				RadioButton rbtnSlot = (RadioButton) v.findViewById(R.id.rbtn_advTime_between);
				rbtnSlot.setChecked(true);
				btnAdvStartTime = (Button) v.findViewById(R.id.btn_advTimePicker1);
				btnAdvEndTime = (Button) v.findViewById(R.id.btn_advTimePicker2);
				btnAdvStartTime.setText(advSettingPref.getString("timeStart", "N/A"));
				btnAdvEndTime.setText(advSettingPref.getString("timeEnd", "N/A"));
				break;
			default:
				break;
			}
		}
	}
}






/////////////////////////////  Extra Unusable Codes ///////////////////////// 
////////////////////////////////////////////////////////////////////////////


//class TimePickerFragment extends DialogFragment 
//										implements TimePickerDialog.OnTimeSetListener {
//
//	@Override
//	public Dialog onCreateDialog(Bundle savedInstanceState) {
//		// Use the current time as the default values for the picker
//		final Calendar c = Calendar.getInstance();
//		int hour = c.get(Calendar.HOUR_OF_DAY);
//		int minute = c.get(Calendar.MINUTE);
//		
//		// Create a new instance of TimePickerDialog and return it
//		return new TimePickerDialog(getActivity(), this, hour, minute,
//		DateFormat.is24HourFormat(getActivity()));
//	}
//	
//	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//	// Do something with the time chosen by the user
//	}
//}


//private void getScrollFocus(View v) {
//	
//	final ScrollView scrollView = (ScrollView) v.findViewById(R.id.scrollview_advTime);
//	scrollView.post(new Runnable() {
//		
//		@Override
//		public void run() {
//			scrollView.fullScroll(ScrollView.FOCUS_DOWN);
//			
//		}
//	});
//}
