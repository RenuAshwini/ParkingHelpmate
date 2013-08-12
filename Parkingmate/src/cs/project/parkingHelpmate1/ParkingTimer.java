/**************************************************************************************
** ParkingHelpmate- An open source android project to keep track of parking meter time
** Application written in Java
** Application uses Google Maps Android API v2
**
** Copyright (C) 2013 Renu Biradar and Ashwini Guttal
**
** This program is free software: you can redistribute it and/or modify it under 
** the terms of the GNU General Public License as published by the Free Software Foundation, 
** either version 3 of the License, or (at your option) any later version.
**
** This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
** without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
** See the GNU General Public License for more details.
**
** You should have received a copy of the GNU General Public License along with this program. 
** If not, see http://www.gnu.org/licenses/.
** Below is the link to the file License.
** https://github.com/RenuAshwini/ParkingHelpmate/License
**
** Following is the link for the repository- https://github.com/RenuAshwini/ParkingHelpmate
**
** Author - Renu Biradar and Ashwini Guttal
** email: renu@pdx.edu and aguttal@pdx.edu
**
** References - 
** License - 
******************************************************************************************/

package cs.project.parkingHelpmate1;
import cs.project.parkingHelpmate1.ParkingTimer;
import cs.project.parkingHelpmate1.R;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/*********************************************************************************************************
** ParkingTimer is used to allow users to enter the parking meter time to receive the reminder alerts at 
** the user specified minutes before the expiration of the parking meter time and also helps users in   
** tracing the location of their parked vehicle from the current position.
*********************************************************************************************************/ 

public class ParkingTimer extends Activity implements OnClickListener, TextWatcher
    {
	    final Context context = this;
        private MyCountDownTimer countDownTimer;
        private boolean timerHasStarted = false;
        private boolean alertFlag = false;
        private Button startB; 
        private Button tagButton;
        private  long startTime = 20000;
        private  long hourTime = 0;
        private  long minuteTime = 0;
        private  long interval = 5000;
        private  long alertTime = 0;
        String editA;
        String editB;
		String parkingNotes = "";
        EditText editAId;
        EditText editBId;
        TextView editreminderId;
  	    private double dest_lat = 0.0;
  	    private double dest_long = 0.0;
  		private MediaPlayer alertTone;
  		private int toneID = 1;

        @Override
        public void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                
                startB = (Button) this.findViewById(R.id.start_button);
                editAId = (EditText) this.findViewById(R.id.edit_message1);
                editBId = (EditText) this.findViewById(R.id.edit_message2);
                editreminderId = (TextView) this.findViewById(R.id.reminder_text2);
                tagButton = (Button) this.findViewById(R.id.tag_button);               

                editAId.addTextChangedListener(this);

                startB.setText("START");                
                
                startB.setOnClickListener(this);            
                
               // Clears the text in the hour field when the hour field is touched
               editAId.setOnTouchListener(new OnTouchListener(){
                	                   
					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						editAId.setText("");
						
						// Show the soft keyboard when the hour field is on focus
						InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        manager.showSoftInput(editAId, 0);

	                    return false;
					}
                });
                
               // Clears the text in the minute field when the minute field is touched
                editBId.setOnTouchListener(new OnTouchListener(){                	
                    
					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						editBId.setText("");
						
						// Shows the soft keyboard when the minute field is on focus
						InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        manager.showSoftInput(editBId, 0);

	                    return false;
					}
                });
             }
 
        @Override
        public void onClick(View v)
            {
        	
        	Button b = (Button) v;
        	
        	// Get the values from hour and minute fields as entered by user
            editA = editAId.getText().toString();
            editB = editBId.getText().toString();

            // Checks to see if the hour field contains any valid data other than "hh"
            if(editA.isEmpty() == false && !editA.contentEquals("hh"))
            {
            	// Converts the value retrieved from hour field to its equivalent milliseconds
                hourTime = Integer.parseInt(editA) * 3600 * 1000;
            }
            
            // Checks to see if the hour field contains any valid data other than "mm"
            if(editB.isEmpty() == false && !editB.contentEquals("mm"))
            {
            	// Converts the value retrieved from minute field to its equivalent milliseconds
            	minuteTime = Integer.parseInt(editB) * 60 * 1000;
            }
            
            // Sets the start time for the timer using the user entered values from hour and minute fields 
            startTime = hourTime + minuteTime;
                        
            //Initializes the timer 
            countDownTimer = new MyCountDownTimer (startTime, interval);
           
           
            // If the user clicks on Start button, then starts the count down timer, disables the hour 
            // and minute fields and changes the text of start button to "Stop"
            if((b.getText().toString() == "START") && !timerHasStarted && !(startTime == 0))
        	{
    		  alertFlag = false;  
              countDownTimer.start();
              timerHasStarted = true;
              startB.setText("STOP");
              editAId.setEnabled(false);
              editBId.setEnabled(false);
            }
            else
            {   
            	
               // If the user clicks on Stop button, then stops the count down timer, enables the hour 
               // and minute fields and changes the text of Stop button to "Start"  
               if(timerHasStarted)
               {
               alertFlag = true;
               countDownTimer.cancel();               
               timerHasStarted = false;
               startB.setText("START");
               editAId.setEnabled(true);
               editBId.setEnabled(true);
               editAId.setText("00");
               editBId.setText("00");
               editAId.requestFocus();
               tagButton.setText("Tag");
               }
             }          
           }
        
          
        // CountDownTimer class
        public class MyCountDownTimer extends CountDownTimer
            {
 
                public MyCountDownTimer (long startTime, long interval)
                    {
                        super(startTime, interval);
                    }
 
                @Override
                public void onFinish()
                    {
                	
                		// Sends an alert to the user notifying the expiration of parking meter time if the user
                	    // has not stopped the timer by then
                        if(startB.getText().toString() == "STOP")
                        {	
                        	// Updates the alert tone with the user specified tone
                        	updateTone();
                        	                       		
                        	LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                       		View layout = inflater.inflate(R.layout.alert_dialog, (ViewGroup) findViewById(R.id.layout_root));

                       		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                       		alertDialogBuilder.setView(layout);
                       		
                       		// Creates alert dialog
            				final AlertDialog alertDialog = alertDialogBuilder.create();

                       		
                    		Button OkButton = (Button) layout.findViewById(R.id.ok_button);
                    		
            				// On click of OK button, sets the input of hour and minute fields to zero and closes the alert dialog box            						
                    		OkButton.setOnClickListener(new OnClickListener() {
                    						@Override
                    						public void onClick(View v) {
                    							editAId.setEnabled(true);
                    							editBId.setEnabled(true);
                    							editAId.setText("00");
                            					editBId.setText("00");
                            					editAId.requestFocus();
                            					tagButton.setText("Tag");
                            					alertDialog.dismiss();               							
                   							
                    						}
                    					});

             
                       		alertDialogBuilder.setCancelable(false);            				
                                    		
            				// Shows the alert dialog
            				alertDialog.show();
                       		alertTone.start();

                        	}
            			
                        	// Changes the text of the Start button to "Start" once the timer expires
                          	startB.setText("START");

                      }
                    
 
                @Override
                public void onTick(long millisUntilFinished)
                    {
                	
                	// Set the reminder alert time based on the settings provided by user
                	String alertMessage = editreminderId.getText().toString();
                	alertTime = Integer.parseInt(alertMessage) * 60 * 1000;
                    
                    if(alertTime !=0)
                    {
                    	//checkAlertTime = alertTime;
                    
                    
                	/* Sends an alert to the user at the user specified minutes before the timer expires */
                	if((!alertFlag) && (millisUntilFinished <= alertTime) && (alertTime < startTime))
                	{
                    	
                		// Updates the alert tone with the user specified tone
                		updateTone();
                   		                   		
                		LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                   		View layout = inflater.inflate(R.layout.alert_dialog, (ViewGroup) findViewById(R.id.layout_root));

                   		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                   		alertDialogBuilder.setView(layout);
                   		
                   		
                   		// Creates alert dialog to dispaly the reminder alert message
        				final AlertDialog alertDialog = alertDialogBuilder.create();
                		TextView reminderText = (TextView) layout.findViewById(R.id.textmessage2);
                		
                		// Updates the text of the alert message based on the minutes in the reminder text on the screen
                		reminderText.setText("Timer will expire in " + alertMessage + " minutes");

                		Button OkButton = (Button) layout.findViewById(R.id.ok_button);
                		
        				// Closes the alert dialog box on click of OK button             						
                		OkButton.setOnClickListener(new OnClickListener() {
                						@Override
                						public void onClick(View v) {
                							alertDialog.dismiss();            							
                						  }
                					});

         
                   		alertDialogBuilder.setCancelable(false);               		                   		
                   		
        				// Shows the alert dialog
        				alertDialog.show();
                   		alertTone.start();
        				
        				// Flag to indicate that the reminder alert dialog has been displayed
            			alertFlag = true;    
                	  }
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
    	   	
    	
    	// Retrieving the values from AlertSettings class and tagActivity class
    	@Override 
    	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
    	  super.onActivityResult(requestCode, resultCode, data); 
    	  switch(requestCode) { 
    	    case (1) : { 
    	      if (resultCode == Activity.RESULT_OK) 
    	         { 
    	    	  	String newText = data.getStringExtra("minutesdata");
    	    	  	
    	    	  	toneID = data.getIntExtra("sounddata", 1);
    	    	  	
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
    	    case (2) : { 
      	      if (resultCode == Activity.RESULT_OK) 
      	         { 
      	    	dest_lat = data.getDoubleExtra("1",0.0);
      	    	dest_long = data.getDoubleExtra("2",0.0);
      	         }	
      	      break; 
     	    	   }
  	         } 
    	  } 
    	
    	// Starts new activity on the click of "Notes" button 
    	public void startNotesActivity(View view) {
    		
    		// Custom dialog to save the parking notes entered by user  		
    		/*LayoutInflater factory = LayoutInflater.from(this);
    		final View deleteDialogView = factory.inflate(R.layout.notes_dialog, null);*/

    		final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    		
    		dialog.setContentView(R.layout.notes_dialog);
    		    		 
    		// Set the custom dialog components - EditText and Buttons
    		 final EditText text = (EditText) dialog.findViewById(R.id.edit_message3);
     		Button dialogOkButton = (Button) dialog.findViewById(R.id.save_button);
    		Button dialogCancelButton = (Button) dialog.findViewById(R.id.cancel_button);
    					
    		if(!parkingNotes.isEmpty())
    			{
    				text.setText(parkingNotes);
    			}
    					
    		
    		// Closes the dialog without saving the notes on click of Cancel button
    		dialogCancelButton.setOnClickListener(new OnClickListener() {
    						@Override
    						public void onClick(View v) {
    						parkingNotes = "";
   							dialog.dismiss();
    						}
    					});
    					
    		
    		// Saves the notes and returns back to the main activity on click of Save button
    		dialogOkButton.setOnClickListener(new OnClickListener() {
    						@Override
    						public void onClick(View v) {
    							
    						parkingNotes = text.getText().toString();
    						dialog.dismiss();
    						}
    					});
    		 
    		dialog.show();
    		
    			}
    	
    	// Starts new activity on the click of "Tag" button 
    	public void onClick_tag(View view) {
    		
    		tagButton.setText("Retag");
    		Intent intent = new Intent(this, tagActivity.class);
    		startActivityForResult(intent, 2);
    		
    			}

    	// Starts new activity on the click of "Locate" button 
    	public void onClick_locate(View view) {
    		Intent intent = new Intent(this, locateActivity.class);
    		
    		// Passes the destination latitude and longitude values obtained from tagActivity to locateActivity 
    		intent.putExtra("destlat", dest_lat);
    		intent.putExtra("destlong", dest_long);
    		
    		startActivity(intent);    		
    		    		
    			}
    	
    	// Sets the alert tone with the value specified by the user in settings activity
    	public void updateTone()
    	{
    		// Checks to see which radio button is clicked and performs the corresponding operation 
	        switch(toneID) {
	            case 1:
	            	alertTone = MediaPlayer.create(ParkingTimer.this, R.raw.sound1);
	                break;
	            case 2:
	            	alertTone = MediaPlayer.create(ParkingTimer.this, R.raw.sound2);
	                break;
	            case 3:
	            	alertTone = MediaPlayer.create(ParkingTimer.this, R.raw.sound3);
	                break;	
	            case 4:
	            	alertTone = MediaPlayer.create(ParkingTimer.this, R.raw.sound4);
	                break;
	            case 5:
	            	alertTone = MediaPlayer.create(ParkingTimer.this, R.raw.sound5);
	                break;
	            case 6:
	            	alertTone = MediaPlayer.create(ParkingTimer.this, R.raw.sound6);
	                break;

	        }
    	}

		@Override
		public void afterTextChanged(Editable s) {
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

        // Changes the focus from hour field to minute field when the input entered in hour field reaches a size of two characters
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
			if(editAId.getText().length() == 2)
                editBId.requestFocus();
		}

       }




