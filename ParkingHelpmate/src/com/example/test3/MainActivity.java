/**************************************************************************************
Copyright � 2013 Renu Biradar and Ashwini Guttal

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

package com.example.test3;
import com.example.test3.MainActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
 
public class MainActivity extends Activity implements OnClickListener
    {
	    final Context context = this;
        private MalibuCountDownTimer countDownTimer;
        private long timeElapsed;
        private boolean timerHasStarted = false;
        private Button startB;
        private TextView text;
        private TextView timeElapsedView;
 
        private  long startTime = 20000;
        private  long hourTime = 0;
        private  long minuteTime = 0;
        private final long interval = 1000;
        public  int value;
        String editA;
        String editB;
        EditText editAId;
        EditText editBId;

        /** Called when the activity is first created. */
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
            						MainActivity.this.finish();
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
                        //text.setText("Time remain:" + millisUntilFinished);
                	if(millisUntilFinished == 10000)
                	{
                		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
             
            			// set title
            			alertDialogBuilder.setTitle("Reminder");
             
            			// set dialog message
            			alertDialogBuilder
            				.setMessage("Your timer will expire in 10 mts")
            				.setCancelable(false)
            				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            					public void onClick(DialogInterface dialog,int id) {
            						// if this button is clicked, close current activity
            						MainActivity.this.finish();
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
                    }
            }
        
    	@Override
    	public boolean onCreateOptionsMenu(Menu menu) {
    		// Inflate the menu; this adds items to the action bar if it is present.
    		getMenuInflater().inflate(R.menu.main, menu);
    		return true;
    	}

    }










