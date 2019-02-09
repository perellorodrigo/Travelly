package com.example.rodrigo.travelly;

import com.example.rodrigo.travelly.models.Trip;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.time.Instant;

public class MapsHelper {

    public static DirectionsResult tripDirections;

    private static GeoApiContext getGeoContext(){
        GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey("AIzaSyC3jioXtoNAJ8Sua_ORvo7eQAzfE8D6GHY").build();
        return geoApiContext;
    }

    public static boolean getRoute(Trip selectedTrip) throws InterruptedException, ApiException, IOException {
        com.google.maps.model.LatLng origin = new com.google.maps.model.LatLng();
        origin.lat = selectedTrip.getOriginPosition().latitude;
        origin.lng = selectedTrip.getOriginPosition().longitude;

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng();
        destination.lat = selectedTrip.getDestinationPosition().latitude;
        destination.lng = selectedTrip.getDestinationPosition().longitude;

        DirectionsResult result = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (selectedTrip.getWaypoints() != null)
                result = DirectionsApi.newRequest(getGeoContext())
                        .mode(TravelMode.DRIVING)
                        .origin(origin)
                        .destination(destination)
                        .waypoints(selectedTrip.getWaypointsForDirections())
                        .departureTime(Instant.now())
                        .await();
            else
                result = DirectionsApi.newRequest(getGeoContext())
                        .mode(TravelMode.DRIVING)
                        .origin(origin)
                        .destination(destination)
                        .departureTime(Instant.now())
                        .optimizeWaypoints(true)
                        .await();
        }

        tripDirections = result;


        return result != null;
    }
}
