package com.ricamgar.challenge.utils;


import android.net.Uri;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;
import java.util.Locale;

public class PlaceMother {

    public final static String ANY_PLACE_NAME = "ANY_PLACE_NAME";
    public final static LatLng ANY_LAT_LNG = new LatLng(1.0, 2.0);
    public final static Place ANY_PLACE = new Place() {
        @Override
        public String getId() {
            return null;
        }

        @Override
        public List<Integer> getPlaceTypes() {
            return null;
        }

        @Override
        public CharSequence getAddress() {
            return null;
        }

        @Override
        public Locale getLocale() {
            return null;
        }

        @Override
        public CharSequence getName() {
            return ANY_PLACE_NAME;
        }

        @Override
        public LatLng getLatLng() {
            return ANY_LAT_LNG;
        }

        @Override
        public LatLngBounds getViewport() {
            return null;
        }

        @Override
        public Uri getWebsiteUri() {
            return null;
        }

        @Override
        public CharSequence getPhoneNumber() {
            return null;
        }

        @Override
        public float getRating() {
            return 0;
        }

        @Override
        public int getPriceLevel() {
            return 0;
        }

        @Override
        public CharSequence getAttributions() {
            return null;
        }

        @Override
        public Place freeze() {
            return null;
        }

        @Override
        public boolean isDataValid() {
            return false;
        }
    };
}
