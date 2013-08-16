/***************************************************************************************
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.content.Context;
import android.content.Intent;
import cs.project.parkingHelpmate1.R;

/*********************************************************************************************************
** locateActivity is used to tag the current location of the user on google map and to get the directions 
** between the user's current position and the location of the parked vehicle 
*********************************************************************************************************/ 

public class locateActivity extends Activity implements OnMarkerClickListener, LocationListener{
	
	private GoogleMap loc_gMap;
    final Context context = this;
    private Location lastKnownLocation1;
    private Location lastKnownLocation2;

	private Location currentLocation;
	private LocationManager locationManager;
	LatLng src_lat_loc;
	LatLng dest_lat_loc;
    private double dest_lat = 0.0;
    private double dest_long = 0.0;
	private String LOCATION_SERVICE="location" ;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.locatelayout);		
	    
		// Save the location data obtained from tagActivity as the destination location
		Intent intent_data = getIntent();
		dest_lat =  intent_data.getDoubleExtra("destlat", 0.0);
		dest_long =  intent_data.getDoubleExtra("destlong", 0.0);
		dest_lat_loc = new LatLng(dest_lat, dest_long);
		
		loc_gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.loc_map)).getMap();		
		loc_gMap.setMyLocationEnabled(true);		
		loc_gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);  		
		 
	    locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
	     
	   
	    // Checks if GPS provider is enabled
	    if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
	    {
	  	    // Requests the periodic updates from the GPS location provider if the GPS provider is enabled
	    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, this);
	    	lastKnownLocation1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	
	    }
	    else
	    {
	    	// Display a dialog box if the GPS provider is disabled
	    	alertProviderDisabled();
	    }
	    	   
	    // Checks if network provider is enabled
	    if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
		{
	  	    	 
	    	// Requests the periodic updates from the network provider if the network provider is enabled
	    	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0, this);
	    	lastKnownLocation2 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	    	
	    }
	    else
	    {
	    	// Display a dialog box if the network provider is disabled
		    alertProviderDisabled();
	    }
	     
	     // Gets the latest location data
	     if ((lastKnownLocation1 != null) && (lastKnownLocation2 != null)) {
	    	 
	    	if(lastKnownLocation1.getTime() > lastKnownLocation1.getTime() )
	    	{
	    	
	    		currentLocation = lastKnownLocation1;
	    	}
	    	else
	    	{
	    		currentLocation = lastKnownLocation2;

	    	}
	    	  
	    }
	    else
	    {
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
	     	  	 
	    loc_gMap.setOnMarkerClickListener(this);
			  
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	// Navigate to the maps.google.com to get the directions on click of marker
	 @Override
	 public boolean onMarkerClick(Marker marker) {
		 
		 // Checks if the destination location has been tagged. If not, set the destination location to be source location
		 if((dest_lat_loc.latitude == 0.0) && (dest_lat_loc.longitude == 0.0))
		 {
			 dest_lat_loc = new LatLng(src_lat_loc.latitude, src_lat_loc.longitude);			 
		 }
		 
		 // Pass both the source and destination location data to find the directions between both locations
		 Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+src_lat_loc.latitude+","+src_lat_loc.longitude+"&daddr="+dest_lat_loc.latitude+","+dest_lat_loc.longitude));
         startActivity(intent);
         
         
	     return true;
	 }
	 
	 @Override
	public void onLocationChanged(Location loc) {
		 
		 	// Save the latest location data as current location	
		 
			if(currentLocation == null)
			{
				currentLocation = loc;
			}
			else if (loc.distanceTo(currentLocation) > 2)	
			 {
				currentLocation = loc;			 
				 
			}
			else
			{

				// Do nothing. Keep the last known location as current location			
			}
			 
			 // Remove the updates from both gps and network provider once you get the latest location data
			 locationManager.removeUpdates(this);
			 src_lat_loc = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
			 loc_gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(src_lat_loc, 18.0f));
		  	 loc_gMap.addMarker(new MarkerOptions().position(new LatLng(src_lat_loc.latitude, src_lat_loc.longitude))
		  				  .title("Click on marker for directions")).showInfoWindow();		   
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
       
 }

	
	

