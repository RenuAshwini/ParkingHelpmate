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

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

/*********************************************************************************************************
** AlertSettings is used to allow users to change the minutes for the reminder alert message and also
** to set or change the alert tone of the alert messages.  
*********************************************************************************************************/ 

public class AlertSettings extends Activity {
	
	private EditText editmts;
	private MediaPlayer mySong;
	private int selectedSound = 1;;

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
        
        // Checks to see which radio button was clicked and updates the minutes field accordingly 
        switch(view.getId()) {
            case R.id.fivemts_button:
            	if(checked)
            	editmts.setText("5");
            	editmts.setEnabled(false);
            	break;
            case R.id.tenmts_button:
            	if(checked)
                	editmts.setEnabled(false);
            	editmts.setText("10");
            	break;
            case R.id.fifteenmts_button:
            	if(checked)
                	editmts.setEnabled(false);

            	editmts.setText("15");
                break;	
            case R.id.other_button:
            	if(checked)
            	{            	editmts.setEnabled(true);

            		editmts.setText("");
                	editmts.requestFocus();   
            	}
                break;	    

        }
    }
    
    // Passes the user specified reminder minutes to the ParkingTimer on the click of back button
    @Override
    public void onBackPressed() {
    String mtsdata = editmts.getText().toString();
    Intent intent = new Intent();
    intent.putExtra("minutesdata", mtsdata);
    intent.putExtra("sounddata", selectedSound);
    setResult(Activity.RESULT_OK, intent);
    finish();
    }   
    
    
    // Allows users to select the alert tone of alert messages 
    public void onSoundButtonClicked(View view) {
    	//mySong = MediaPlayer.create(AlertSettings.this, R.raw.sound1);
    	    	
		// Checks to see if the radio button has been checked
	       Boolean checked = ((RadioButton) view).isChecked();
	        
	        // Checks to see which radio button is clicked and performs the corresponding operation 
	        switch(view.getId()) {
	            case R.id.alertSound1:
	            	if(checked)
	            	{
	                	mySong = MediaPlayer.create(AlertSettings.this, R.raw.sound1);
	            		mySong.start();
	            		selectedSound = 1;
	            	}
	            	break;
	            case R.id.alertSound2:
	            	if(checked)
	            		{
	                	mySong = MediaPlayer.create(AlertSettings.this, R.raw.sound2);

	            		mySong.start();
		            		selectedSound = 2;

	            		}
	            	break;
	            case R.id.alertSound3:
	            	if(checked)
	            	{
	                	mySong = MediaPlayer.create(AlertSettings.this, R.raw.sound3);

	            		mySong.start();
	            		selectedSound = 3;

	            	}
	                break;	
	            case R.id.alertSound4:
	            	if(checked)
	            	{	                	mySong = MediaPlayer.create(AlertSettings.this, R.raw.sound4);

	            		mySong.start();
	            		selectedSound = 4;

	            	}
	                break;	
	            case R.id.alertSound5:
	            	if(checked)
	            	{	                	mySong = MediaPlayer.create(AlertSettings.this, R.raw.sound5);

	            		mySong.start();
	            		selectedSound = 5;

	            	}
	                break;
	            case R.id.alertSound6:
	            	if(checked)
	            	{	                	mySong = MediaPlayer.create(AlertSettings.this, R.raw.sound6);

	            		mySong.start();
	            		selectedSound = 6;

	            	}
	                break;
	        }
	     }
    
    public void onClick_Home(View view)
    {
    	onBackPressed();
    }
     }

    

