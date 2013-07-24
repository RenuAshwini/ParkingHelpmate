/**************************************************************************************
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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;

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
        private final long interval = 5000;
        public  int value;
        String editA;
        String editB;
        EditText editAId;
        EditText editBId;

        @Override
        public void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                startB = (Button) this.findViewById(R.id.start_button);
                editAId = (EditText) this.findViewById(R.id.edit_message1);
                editBId = (EditText) this.findViewById(R.id.edit_message2);
                startB.setOnClickListener(this); 
                

                editAId.setOnKeyListener(new OnKeyListener() {

                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if(editAId.getText().length() == 2)
                            editBId.requestFocus();
                        return false;
                    }

					
                });

            }
 
        @Override
        public void onClick(View v)
            {
        	        	
            editA = editAId.getText().toString();
            editB = ((EditText) this.findViewById(R.id.edit_message2)).getText().toString();

            if(editA.isEmpty() == false && !editA.contentEquals("hh"))
            {
                hourTime = Integer.parseInt(editA) * 3600 * 1000;
            }
            
            if(editB.isEmpty() == false && !editB.contentEquals("mm"))
            {
            	minuteTime = Integer.parseInt(editB) * 60 * 1000;
            }
            
            startTime = hourTime + minuteTime;
            countDownTimer = new MalibuCountDownTimer(startTime, interval);
           
            Button b = (Button) v;
            /* if the button pressed is start button, start the timer*/
            if(b.getText().toString() == "Start")
        	{
              startB.setText("Stop");
              countDownTimer.start();
              timerHasStarted = true;
            }
            else
            {
               startB.setText("Start");
               
               if(timerHasStarted)
               {
                  countDownTimer.cancel();
               }
               
               timerHasStarted = false;
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

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
             
            			// set title
            			alertDialogBuilder.setTitle("Alert");
             
            			// set dialog message
            			alertDialogBuilder
            				.setMessage("Your timer expired")
            				.setCancelable(false)
            				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            					public void onClick(DialogInterface dialog,int id) {
            						// if this button is clicked, close current activity
            						ParkingTimer.this.finish();
            					}
            				  })
            				.setNegativeButton("No",new DialogInterface.OnClickListener() {
            					public void onClick(DialogInterface dialog,int id) {
            						// if this button is clicked, just close
            						// the dialog box and do nothing
            						dialog.cancel();
            					}
            				});
             
            				// create alert dialog
            				AlertDialog alertDialog = alertDialogBuilder.create();
             
            				// show it
            				alertDialog.show();
                        	}
            				
                            startB.setText("Start");

                      }
                    
 
                @Override
                public void onTick(long millisUntilFinished)
                    {
                	/* send an alert at "millisuntilFinished" before the timer expires */
                	if((!alertSent) && (millisUntilFinished < 10000))
                	{
                		AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(context);
             
            			// set title
            			alertDialogBuilder1.setTitle("Reminder");
             
            			// set dialog message
            			alertDialogBuilder1
            				.setMessage("Your timer will expire in 10 mts")
            				.setCancelable(false)
            				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            					public void onClick(DialogInterface dialog,int id) {
            						// if this button is clicked, close current activity
            						dialog.cancel();
            					}
            				  });			  
            				
             
            				// create alert dialog
            				AlertDialog alertDialog1 = alertDialogBuilder1.create();
            				
            				alertDialog1.show();
            				alertSent = true;

                	}
                    }
            }
        
    	@Override
    	public boolean onCreateOptionsMenu(Menu menu) {
    		getMenuInflater().inflate(R.menu.main, menu);
    		return true;
    	}
    	
    	/* Start new activity on the click of "Settings" button */
    	public void startSettingsActivity(View view) {
    		Intent intent = new Intent(this, AlertSettings.class);
    	    startActivity(intent);   
    	    }
    	
    }










