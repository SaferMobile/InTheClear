package org.safermobile.clear.micro.data;

import javax.microedition.location.Location;
import javax.microedition.location.LocationException;
import javax.microedition.location.LocationListener;
import javax.microedition.location.LocationProvider;

public class MovementTracker implements LocationListener 
{
    float movementDistance;
    LocationProvider provider;
    Location lastValidLocation;
    UpdateHandler handler;
    boolean done;

    public MovementTracker(float movementDistance) throws LocationException {
		this.movementDistance = movementDistance;
		done = false;
		handler = new UpdateHandler();
		new Thread(handler).start();
		provider = LocationProvider.getInstance(null);
		provider.setLocationListener(this, -1, -1, -1);
    }

    public void locationUpdated(LocationProvider provider, Location location) {
	handler.handleUpdate(location);
    }

    public void providerStateChanged(LocationProvider provider, int newState) {
    }

    class UpdateHandler implements Runnable {
	private Location updatedLocation = null;

	// The run method performs the actual processing of the location 
	// updates
	public void run() {
	    Location locationToBeHandled = null;
	    while (!done) {
		synchronized(this) {
		    if (updatedLocation == null) {
			try {
			    wait();
			} catch (Exception e) {
			    // Handle interruption
			}
		    }   
		    locationToBeHandled = updatedLocation;
		    updatedLocation = null;
		}

		// The benefit of the MessageListener is here.
		// This thread could via similar triggers be
		// handling other kind of events as well in
		// addition to just receiving the location updates.
		if (locationToBeHandled != null) 
		    processUpdate(locationToBeHandled);
	    }
	}

	public synchronized void handleUpdate(Location update) {
	    updatedLocation = update;
	    notify();
	}

	private void processUpdate(Location update) {
	    if ( update.getQualifiedCoordinates().distance(
			lastValidLocation.getQualifiedCoordinates() ) 
		 > movementDistance ) {
		// Alert user to movement...
		    
		// Cache new position as we have moved a sufficient distance 
		// from last one
		lastValidLocation = update;
	    }
	}
    }
}
