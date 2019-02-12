package com.example.rodrigo.travelly;

import com.example.rodrigo.travelly.models.Trip;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.time.Instant;

public class MapsHelper {

    // This is a static variable which will store the latest directions called
    // This directions are set when the user clicks on a Trip in the Trips List View.
    public static DirectionsResult tripDirections;
    // Keep the trip ID of the latest request to avoid consuming the API for the same requests as last time
    public static int currentDirectionsTripID;
    // String used to output the total trip Kilometers
    public static String tripDistanceAsString;
    // Long integer which stores the trip distance in meters
    public static long tripDistanceInMeters;

    private static GeoApiContext getGeoContext() {
        // Build the context to use the Directions API
        // TODO If publish app, keep API secure
        GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey("AIzaSyC3jioXtoNAJ8Sua_ORvo7eQAzfE8D6GHY").build();
        return geoApiContext;
    }
    public static boolean getRoute(Trip selectedTrip) throws InterruptedException, ApiException, IOException {
        // Overloading of function get route
        // If this function is called is because it didn't specify the force update
        // Which in case it will only get the route if the Trip ID is different from the last Trip ID that loaded directions
        return getRoute(selectedTrip,false);
    }


    public static boolean getRoute(Trip selectedTrip, boolean forceUpdate) throws InterruptedException, ApiException, IOException {

        // Check whether the selected trip ID is different from the last stored Directions Trip ID\
        // If the force update is true, it will run this function either way
        // forceUpdate is used when a waypoint is added to the map
        // So it needs to recalculate the directions for the same trip that had already been calculated
        if (currentDirectionsTripID != selectedTrip.getId() || forceUpdate) {
            currentDirectionsTripID = selectedTrip.getId();
            // Conversion of Maps Coordinates to Directions Coordinates object
            com.google.maps.model.LatLng origin = new com.google.maps.model.LatLng();
            origin.lat = selectedTrip.getOriginPosition().latitude;
            origin.lng = selectedTrip.getOriginPosition().longitude;

            com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng();
            destination.lat = selectedTrip.getDestinationPosition().latitude;
            destination.lng = selectedTrip.getDestinationPosition().longitude;
            //------------------------------------------------------------------


            DirectionsResult result = null;
            // Check version supported for Directions API
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                // If the waypoints list is not empty:
                if (!selectedTrip.getWaypoints().isEmpty())
                    result = DirectionsApi.newRequest(getGeoContext())
                            .mode(TravelMode.DRIVING)
                            .origin(origin)
                            .destination(destination)
                            .waypoints(selectedTrip.getWaypointsForDirections()) // Send waypoints
                            .departureTime(Instant.now())
                            .optimizeWaypoints(true) // Optimize route for the user,
                            .await();
                else
                    result = DirectionsApi.newRequest(getGeoContext())
                            .mode(TravelMode.DRIVING)
                            .origin(origin)
                            .destination(destination)
                            .departureTime(Instant.now())
                            .await();
            }

            tripDirections = result;

            // Store the respective order for each waypoint in the route
            for (int i = 0; i < AppData.selectedTrip.getWaypoints().size(); i++) {
                int wpOrder = tripDirections.routes[0].waypointOrder[i];
                AppData.selectedTrip.getWaypoints().get(wpOrder).setOrder(i);
            }


            // Calculate entire distance in meters
            tripDistanceInMeters = 0;
            for (DirectionsLeg leg : result.routes[0].legs
            ) {
                tripDistanceInMeters += leg.distance.inMeters;
            }

            //Format output to display as kilometers only 1 decimal place. Example: 7.5Km
            tripDistanceAsString = String.format("%.1f", tripDistanceInMeters / 1000f) + " Km";

            return result != null;
        } else {
            return true;
        }
    }
}
