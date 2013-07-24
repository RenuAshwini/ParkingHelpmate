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

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

/*********************************************************************************************************
** AlertSettings is used to allow users to doing the settings for the reminder alert  
*********************************************************************************************************/ 

public class AlertSettings extends Activity {
	
	EditText editmts;

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
        
        // Check which radio button was clicked and perform the corresponding operation 
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
}
