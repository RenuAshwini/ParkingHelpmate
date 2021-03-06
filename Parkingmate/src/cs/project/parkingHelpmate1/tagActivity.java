/**************************************************************************************
** ParkingHelpmate- An open source android project that helps to track parking meter time 
** and to navigate to the vehicle location from current location
** Application uses Google Maps Android API v2
**
** Copyright(C) 2013 Renu Biradar and Ashwini Guttal
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
** Please see the file "License" in this distribution for license terms. 
** Below is the link to the file License.
** https://github.com/RenuAshwini/ParkingHelpmate/License.txt
**
** Following is the link for the repository- https://github.com/RenuAshwini/ParkingHelpmate
**
** Authors - Renu Biradar and Ashwini Guttal
** email  - renu.biradar@gmail.com and aguttal@gmail.com
**
** References - http://stackoverflow.com/questions/3145089/what-is-the-simplest-and-most-robust-way-to-get-the-users-current-location-in-a 
**            - http://developer.android.com/google/play-services/location.html
**            - http://www.mkyong.com/android/android-alert-dialog-example/
******************************************************************************************/

package cs.project.parkingHelpmate1;

import android.app.Activity;
import android.app.Dialog;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
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
	private LocationManager locationManager_tag;
	private Location gpsLocation;
	private Location nwLocation;
	private Location currentBestLocation = null;
	private String LOCATION_SERVICE="location" ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.taglayout);	
		
		locationManager_tag = (LocationManager) this.getSystemService(LOCATION_SERVICE);
	    
	    // Checks if GPS provider is enabled
		if(locationManager_tag.isProviderEnabled(LocationManager.GPS_PROVIDER))
   	 	{
	  	    // Requests the periodic updates from the GPS location provider if the GPS provider is enabled
	    	locationManager_tag.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, this);
	    	gpsLocation = locationManager_tag.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	
	    }
		else
		{
	    	// Display a dialog box if the GPS provider is disabled
			alertProviderDisabled();
		}
		
	    // Checks if network provider is enabled
   	   	if(locationManager_tag.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
	    {
	  	    // Requests the periodic updates from the network location provider if the network provider is enabled
   	   		locationManager_tag.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0, this);
	    	nwLocation = locationManager_tag.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
   	
		}
	    else
	    {
	    	// Display a dialog box if the network provider is disabled
	    	alertProviderDisabled();
	    }
   
	    // Gets the latest location data
   	   	if ((gpsLocation != null) && (nwLocation != null)) {
   	 
   	   		if(gpsLocation.getTime() > nwLocation.getTime() )
   	   		{
    	
   	   			currentBestLocation = gpsLocation;
   	   		}
   	   		else
   	   		{
   	   			currentBestLocation = nwLocation;

   	   		}
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
		
	 	// Save the latest location data as current location	
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
			// Do nothing. Keep the last known location as current location
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
	
	// Displays alert dialog if both the GPS and network providers are disabled 
	public void alertProviderDisabled() {
		
		final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
 		
 		dialog.setContentView(R.layout.confirmdialog);
 		    		 
 		// Set the custom dialog components - EditText and Buttons
  		Button dialogOkButton = (Button) dialog.findViewById(R.id.yesbutton);
  		EditText noProviderMessage = (EditText) dialog.findViewById(R.id.confirmmessage);
        
  		noProviderMessage.setText("GPS and Network providers are unavailable");
 		
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
		
	// Display dialog box once the location is tagged
	public void confirmDialog()
	{
		
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
