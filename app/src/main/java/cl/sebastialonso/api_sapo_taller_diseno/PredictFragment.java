package cl.sebastialonso.api_sapo_taller_diseno;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by seba on 12/27/15.
 */
public class PredictFragment extends Fragment {

    private SupportMapFragment mSupportMapFragment;

    public static PredictFragment newInstance(String text){

        PredictFragment pf = new PredictFragment();
        Bundle args = new Bundle();

        args.putString("msg", text);
        pf.setArguments(args);

        return pf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.predict_fragment, container, false);
        //TextView textView = (TextView) view.findViewById(R.id.predict_instructions);

        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.predict_direction);
        final RadioButton toValpo = (RadioButton) view.findViewById(R.id.predict_direction_valpo);
        final RadioButton toVina= (RadioButton) view.findViewById(R.id.predict_direction_vina);

        final FloatingActionButton predictSapeada = (FloatingActionButton) view.findViewById(R.id.predict_sapeada);

        predictSapeada.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){

                //Button disabled
                predictSapeada.setEnabled(true);

                //Obtain value from RadioGroup

                int selected = radioGroup.getCheckedRadioButtonId();
                if (selected == toValpo.getId()) selected = 0;
                else selected = 1;

                //Time
                Calendar calendar = Calendar.getInstance();
                int seconds = calendar.get(Calendar.HOUR_OF_DAY)*3600 + calendar.get(Calendar.MINUTE)*60 + calendar.get(Calendar.SECOND);

                //Preferences for UserID
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

                //Request elements
                RequestQueue queue = Volley.newRequestQueue(getContext());

                JSONObject params = new JSONObject();
                JSONObject sighting = new JSONObject();


                try {
                    sighting.put("bus_id", 1);
                    sighting.put("user_id", prefs.getString("userId", "1"));
                    sighting.put("lat", "-33.036649");
                    sighting.put("lon", "-71.594667");
                    sighting.put("direction", selected);
                    sighting.put("time", seconds);
                    params.put("sighting", sighting);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                        Constants.URL_PREDICT,
                        sighting,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_predict);
                                if (mSupportMapFragment == null) {
                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    mSupportMapFragment = SupportMapFragment.newInstance();
                                    fragmentTransaction.replace(R.id.map_predict, mSupportMapFragment).commit();
                                }

                                if (mSupportMapFragment != null) {
                                    mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                                        @Override
                                        public void onMapReady(GoogleMap googleMap) {
                                            if (googleMap != null) {

                                                googleMap.getUiSettings().setAllGesturesEnabled(true);

                                                LatLng marker_latlng = new LatLng(-33.036245, -71.593605);

                                                CameraPosition cameraPosition = new CameraPosition.Builder().target(marker_latlng).zoom(12.0f).build();
                                                CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                                                googleMap.moveCamera(cameraUpdate);

                                            }

                                        }
                                    });
                                }
                                Snackbar snackbar = Snackbar
                                        .make(v, "Predicción exitosa.", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                                predictSapeada.setEnabled(true);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                OnErrorDialogFragment dialog = new OnErrorDialogFragment();
                                dialog.show(getFragmentManager(), "onError");

                                predictSapeada.setEnabled(true);
                            }
                        }
                );
                queue.add(jsonObjectRequest);
            }
        });

        return view;
    }
}
