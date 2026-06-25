package com.touristsafety.util;

public final class GeoUtils {

    private static final double EARTH_RADIUS_KM = 6371.0;

    private GeoUtils() {
    }

    public static double distanceInKm(double latitudeA, double longitudeA, double latitudeB, double longitudeB) {
        double latitudeDistance = Math.toRadians(latitudeB - latitudeA);
        double longitudeDistance = Math.toRadians(longitudeB - longitudeA);
        double a = Math.sin(latitudeDistance / 2) * Math.sin(latitudeDistance / 2)
                + Math.cos(Math.toRadians(latitudeA)) * Math.cos(Math.toRadians(latitudeB))
                * Math.sin(longitudeDistance / 2) * Math.sin(longitudeDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    public static String googleNavigationUrl(double latitude, double longitude) {
        return "https://www.google.com/maps/dir/?api=1&destination=" + latitude + "," + longitude;
    }
}
