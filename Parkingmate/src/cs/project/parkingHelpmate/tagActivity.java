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

package cs.project.parkingHelpmate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import android.location.Location;
import android.location.LocationListener;
import android.content.Context;
import android.content.Intent;
import cs.project.parkingHelpmate1.R;

/*********************************************************************************************************
** tagActivity is used to tag the current location of the parked vehicle on google map and to save the 
** location coordinates in order to use it as destination point in locateActivity for getting the  
** directions. 
*********************************************************************************************************/ 

public class tagActivity extends Activity implements LocationListener{
	
	LatLng des_lat_loc;
    final Context context = this;	
	LocationManager locationManager_tag;
	private Location location;
	private Location currentBestLocation = null;
	private String LOCATION_SERVICE="location" ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taglayout);	
		
		//LocationManager locationManager_tag;
	   		
		 
	     locationManager_tag = (LocationManager) this.getSystemService(LOCATION_SERVICE);
	     	     
	     if (locationManager_tag.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
		    	
	    	 // Requests the periodic updates from the GPS location provider if the GPS provider is enabled
	    	 locationManager_tag.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000,0, this);
	    	 location = locationManager_tag.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		    }
	     else if(locationManager_tag.isProviderEnabled(LocationManager.GPS_PROVIDER))
	    	 {
	    	 locationManager_tag.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000,0, this);
	    	 location = locationManager_tag.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	
		    }
	     else
	     {
	    	 
	     }
	     
	     
	     
	   //Initialize the destination location fields
	     if (location != null) {
	    	 
	    	 currentBestLocation = location; 	
	    	 
	    	 }
	     else{
	    	 
	    	 final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
		 		
		 		dialog.setContentView(R.layout.confirmdialog);
		 		    		 
		 		// Set the custom dialog components - EditText and Buttons
		  		Button dialogOkButton = (Button) dialog.findViewById(R.id.yesbutton);
		  		EditText noProviderMessage = (EditText) dialog.findViewById(R.id.confirmmessage);
	            
		  		noProviderMessage.setText("Could not retrieve location");
		 		
		 		// Closes the dialog on click of OK button 
		 		dialogOkButton.setOnClickListener(new OnClickListener() {
		 						@Override
		 						public void onClick(View v) {
		 							dialog.dismiss();
		 							onBackPressed();              							

		 						}
		 					});
		 		 
		 		dialog.show();


	    	 //Toast.makeText(this, "Could not retrieve the location data ", Toast.LENGTH_SHORT).show();
	    	 
	     }		  
		 }
	
	//Send the destination latitude and longitude back to the ParkingTimer activity on click of back button
	@Override
    public void onBackPressed() {
    Intent intent = new Intent();
    intent.putExtra("1", des_lat_loc.latitude);
    intent.putExtra("2", des_lat_loc.longitude); 
    setResult(Activity.RESULT_OK, intent);
    finish();
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onLocationChanged(Location loc) {
		
		if(currentBestLocation == null)
		{
			currentBestLocation = loc;
		}
		else if (loc.distanceTo(currentBestLocation) > 2)	
		 {
			 currentBestLocation = loc;			 
			 
		}
		else
		{
			// Do nothing. Keep the currentBestLocation to be the last known location
		}
		 
		 locationManager_tag.removeUpdates(this);
		 des_lat_loc = new LatLng(currentBestLocation.getLatitude(), currentBestLocation.getLongitude());
		 confirmDialog();
	                     

	}
	
	public void onProviderEnabled(String provider) {
		Toast.makeText(this, "Enabled provider " + provider, Toast.LENGTH_SHORT).show();

	}

	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();

	}

	public void onStatusChanged(String provider, int status,
	            Bundle extras) {
	       
	    }
	
	// Displays alert dialog to ask if the users wants to enable the GPS by navigating to the settings page
	public void alertGPSDisabled() {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.gps_alert_dialog, (ViewGroup) findViewById(R.id.gps_root));

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setView(layout);
			
		// Creates alert dialog
		final AlertDialog alertDialog = alertDialogBuilder.create();
		
		Button enableButton = (Button) layout.findViewById(R.id.enable_button);
		Button ignoreButton = (Button) layout.findViewById(R.id.ignore_button);
		
		// On click of "go to settings" button, navigate to the settings page to enable GPS           						
		enableButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent callGPSSettingIntent = new Intent(
									android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
									startActivity(callGPSSettingIntent);
	    					              							
							
						}
					});
		
		// On click of "cancel" button, close the alert dialog           						
		ignoreButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								alertDialog.dismiss();
								
							}
					});

		alertDialogBuilder.setCancelable(false);		
	            		
		// Displays the alert dialog
		alertDialog.show();
		}
	
	public void confirmDialog()
	{
		// Custom dialog to save the parking notes entered by user  		
		
		final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
		
		dialog.setContentView(R.layout.confirmdialog);
		    		 
		// Set the custom dialog components - EditText and Buttons
 		Button dialogOkButton = (Button) dialog.findViewById(R.id.yesbutton);
		
		// Closes the dialog on click of OK button 
		dialogOkButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							onBackPressed();              							

						}
					});
		 
		dialog.show();
	}


	
	
}
