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

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

/*********************************************************************************************************
** AlertSettings is used to allow users to change the settings for the reminder alert  
*********************************************************************************************************/ 

public class AlertSettings extends Activity {
	
	EditText editmts;
	MediaPlayer ourSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
      editmts = (EditText) this.findViewById(R.id.settings_edit1);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    
    public void onmtsButtonClicked(View view) {
    	
        // Checks if the radio button is checked
       boolean checked = ((RadioButton) view).isChecked();
        
        // Checks to see which radio button was clicked and performs the corresponding operation 
        switch(view.getId()) {
            case R.id.fivemts_button:
            	if(checked)
            	editmts.setText("5");
            	break;
            case R.id.tenmts_button:
            	if(checked)
            	editmts.setText("10");
            	break;
            case R.id.fifteenmts_button:
            	if(checked)
            	editmts.setText("15");
                break;	
            case R.id.other_button:
            	if(checked)
            	{
            		editmts.setText("");
                	editmts.requestFocus();   
            	}
                break;	    

        }
    }
    
    // Update the reminder minutes from Alertsettings activity in ParkingTimer activity on the click 
    // of back button
    @Override
    public void onBackPressed() {
    String mtsdata = editmts.getText().toString();
    Intent intent = new Intent();
    intent.putExtra("2", mtsdata);
    setResult(Activity.RESULT_OK, intent);
    finish();
    }
    
    
    
    
    //Changes the tone of alert sound to the specified tone on the click of Sound button 
    public void onSoundButtonClicked(View view) {
    	ourSong = MediaPlayer.create(AlertSettings.this, R.raw.tritone);
    	
		// Checks to see if the radio button has been checked
	       boolean checked = ((RadioButton) view).isChecked();
	        
	        // Checks to see which radio button is clicked and performs the corresponding operation 
	        switch(view.getId()) {
	            case R.id.alertSound1:
	            	if(checked)
	            	{
	            		ourSong.start();
	            	}
	            	break;
	            case R.id.alertSound2:
	            	if(checked)
	            		{

	            		ourSong.start();
	            		}
	            	break;
	            case R.id.alertSound3:
	            	if(checked)
	            	{
	            		
	            	ourSong.start();
	            	}
	                break;	
	            case R.id.alertSound4:
	            	if(checked)
	            	{
	            		ourSong.start();   
	            	}
	                break;	    

	        }
	     }
     }

    

