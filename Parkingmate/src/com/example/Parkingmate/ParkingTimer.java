/**************************************************************************************
ParkingHelpmate- An open source android project to keep track of parking meter time
Application written in Java

Copyright (C) 2013 Renu Biradar and Ashwini Guttal

This program is free software: you can redistribute it and/or modify it under 
the terms of the GNU General Public License as published by the Free Software Foundation, 
either version 3 of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program. 
If not, see http://www.gnu.org/licenses/.

Author - Renu Biradar and Ashwini Guttal
email: renu@pdx.edu and aguttal@pdx.edu

******************************************************************************************/

package com.example.Parkingmate;
import com.example.Parkingmate.ParkingTimer;
import com.example.Parkingmate.R;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/*********************************************************************************************************
** ParkingTimer is used to allow users to enter the parking meter time to set the alert
*********************************************************************************************************/ 

public class ParkingTimer extends Activity implements OnClickListener
    {
	    final Context context = this;
        private MalibuCountDownTimer countDownTimer;
        private boolean timerHasStarted = false;
        private boolean alertSent = false;
        private Button startB; 
        private  long startTime = 20000;
        private  long hourTime = 0;
        private  long minuteTime = 0;
        private  long interval = 5000;
        private  long alertTime = 0;
        private  long checkAlertTime = 10 * 60 * 1000;   
        String editA;
        String editB;
		String parkingNotes = "";
        EditText editAId;
        EditText editBId;
        TextView editreminderId;

        @Override
        public void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                startB = (Button) this.findViewById(R.id.start_button);
                editAId = (EditText) this.findViewById(R.id.edit_message1);
                editBId = (EditText) this.findViewById(R.id.edit_message2);
                editreminderId = (TextView) this.findViewById(R.id.reminder_text2);
                Button butt = (Button) this.findViewById(R.id.tag_button);
                butt.setBackgroundResource(R.drawable.settings_image);
                
                startB.setText("Start");

                startB.setOnClickListener(this); 
                

                editAId.setOnKeyListener(new OnKeyListener() {

                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if(editAId.getText().length() == 2)
                            editBId.requestFocus();
                        return false;
                    }

					
                });
                
               editAId.setOnTouchListener(new OnTouchListener(){
                	
                   
					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						editAId.setText("");
	                    return false;
					}
                });
                
                editBId.setOnTouchListener(new OnTouchListener(){
                	
                    
					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						editBId.setText("");
	                    return false;
					}
                });


            }
 
        @Override
        public void onClick(View v)
            {
        	
        	// Get the hour and minutes as entered by user
            editA = editAId.getText().toString();
            editB = editBId.getText().toString();

            // Check if the input entered by user contains data
            if(editA.isEmpty() == false && !editA.contentEquals("hh"))
            {
                hourTime = Integer.parseInt(editA) * 3600 * 1000;
            }
            
            // Check if the input entered by user contains data
            if(editB.isEmpty() == false && !editB.contentEquals("mm"))
            {
            	minuteTime = Integer.parseInt(editB) * 60 * 1000;
            }
            
            startTime = hourTime + minuteTime;
                        
            //Initialise the timer with the minutes as entered by user
            countDownTimer = new MalibuCountDownTimer(startTime, interval);
           
            Button b = (Button) v;
            
            /* If the user clicks on Start button, then start the timer */
            if((b.getText().toString() == "Start") && !timerHasStarted)
        	{
              countDownTimer.start();
              timerHasStarted = true;
              startB.setText("Stop");
              editAId.setEnabled(false);
              editBId.setEnabled(false);

            }
            else
            {               
               
               if(timerHasStarted)
               {
                  countDownTimer.cancel();
                  
               
               
               timerHasStarted = false;
               startB.setText("Start");
               editAId.setEnabled(true);
               editBId.setEnabled(true);
               editAId.setText("00");
               editBId.setText("00");
               editAId.requestFocus();
               }
             }
                    	
        	
                
           }
        
          
        // CountDownTimer class
        public class MalibuCountDownTimer extends CountDownTimer
            {
 
                public MalibuCountDownTimer(long startTime, long interval)
                    {
                        super(startTime, interval);
                    }
 
                @Override
                public void onFinish()
                    {
                	
                        if(startB.getText().toString() == "Stop")
                        {	

                        	LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                       		View layout = inflater.inflate(R.layout.alert_dialog, (ViewGroup) findViewById(R.id.layout_root));

                       		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                       		alertDialogBuilder.setView(layout);
                       		
                       		// create alert dialog
            				final AlertDialog alertDialog = alertDialogBuilder.create();

                       		
                    		Button OkButton = (Button) layout.findViewById(R.id.ok_button);
                    		
            				// On click of OK button, set the input time back to zero and close the alert dialog box            						
                    		OkButton.setOnClickListener(new OnClickListener() {
                    						@Override
                    						public void onClick(View v) {
                    							editAId.setEnabled(true);
                    							editBId.setEnabled(true);
                    							editAId.setText("00");
                            					editBId.setText("00");
                            					editAId.requestFocus();
                            					alertDialog.dismiss();               							
                   							
                    						}
                    					});

             
                       		// set dialog message
                       		alertDialogBuilder
            				.setCancelable(false);
            				
                                    		
            				// show it
            				alertDialog.show();
                        	}
            			
                        	// Change the text of the button to Start once the timer expires
                          	startB.setText("Start");

                      }
                    
 
                @Override
                public void onTick(long millisUntilFinished)
                    {
                	
                	// Set the reminder alert time based on the settings as provided by user
                	String alertMessage = editreminderId.getText().toString();
                	alertTime = Integer.parseInt(editreminderId.getText().toString()) * 60 * 1000;
                    
                    if(alertTime !=0)
                    {
                    	checkAlertTime = alertTime;
                    }
                    
                	/* send an alert to the user before the timer expires */
                	if((!alertSent) && (millisUntilFinished <= checkAlertTime) && (checkAlertTime < startTime))
                	{
                		LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                   		View layout = inflater.inflate(R.layout.alert_dialog, (ViewGroup) findViewById(R.id.layout_root));

                   		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                   		alertDialogBuilder.setView(layout);
                   		
                   		// create alert dialog
        				final AlertDialog alertDialog = alertDialogBuilder.create();

                		TextView reminderText = (TextView) layout.findViewById(R.id.textmessage);
                		
                		reminderText.setText("Your timer will expire in " + alertMessage + " mts");

                		Button OkButton = (Button) layout.findViewById(R.id.ok_button);
                		
        				// On click of OK button, set the input time back to zero and close the alert dialog box            						
                		OkButton.setOnClickListener(new OnClickListener() {
                						@Override
                						public void onClick(View v) {
                							alertDialog.dismiss();            							
                						  }
                					});

         
                   		// set dialog message
                   		alertDialogBuilder
        				.setCancelable(false);        				
                                		
        				// show alert dialog
        				alertDialog.show();
            			alertSent = true;    
                	  }
                      }
                 }
        
    	@Override
    	public boolean onCreateOptionsMenu(Menu menu) {
    		getMenuInflater().inflate(R.menu.main, menu);
    		return true;
    	}
    	
    	// Starts new activity on the click of "Settings" button 
    	public void startSettingsActivity(View view) {
    		Intent intent = new Intent(this, AlertSettings.class);
    		startActivityForResult(intent, 1);   
    	    }
    	
    	@Override 
    	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
    	  super.onActivityResult(requestCode, resultCode, data); 
    	  switch(requestCode) { 
    	    case (1) : { 
    	      if (resultCode == Activity.RESULT_OK) 
    	         { 
    	    	  	String newText = data.getStringExtra("2");
    	    	  	if(newText.isEmpty() == false)
    	    	  	{
    	    	  	if(Integer.parseInt(newText) != 0)
    	    	  		{
    	    	  		editreminderId.setText(newText);
    	    	  		}
    	    	  	}
    	         } 
    	      break; 
   	    	   }
  	         } 
    	  } 
    	
    	
    	// Starts new activity on the click of "Add Notes" button 
    	public void startNotesActivity(View view) {
    		
    		// custom dialog to save the parking notes   		
    		LayoutInflater factory = LayoutInflater.from(this);
    		final View deleteDialogView = factory.inflate(R.layout.notes_dialog, null);

    		final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);

    		dialog.setContentView(deleteDialogView);
    		dialog.setTitle("Title...");
    		 
    		// set the custom dialog components - text, image and button
    		 final EditText text = (EditText) dialog.findViewById(R.id.edit_message2);
    					
    		if(!parkingNotes.isEmpty())
    			{
    				text.setText(parkingNotes);
    			}
    					
    		Button dialogCancelButton = (Button) dialog.findViewById(R.id.cancel_button);
    		
    		// On click of Cancel button, close the dialog without saving the notes 
    		dialogCancelButton.setOnClickListener(new OnClickListener() {
    						@Override
    						public void onClick(View v) {
    						parkingNotes = "";
   							dialog.dismiss();
    						}
    					});
    					
    		Button dialogOkButton = (Button) dialog.findViewById(R.id.save_button);
    		
    		// On click of Save button, save the notes and return back to the main activity
    		dialogOkButton.setOnClickListener(new OnClickListener() {
    						@Override
    						public void onClick(View v) {
    							
    						parkingNotes = text.getText().toString();
    						dialog.dismiss();
    						}
    					});
    		 
    		dialog.show();
    		
    			}
       }




