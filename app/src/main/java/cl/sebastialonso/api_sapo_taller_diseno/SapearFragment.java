package cl.sebastialonso.api_sapo_taller_diseno;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by seba on 12/27/15.
 */
public class SapearFragment extends Fragment {

    public static SapearFragment newInstance(String text){
        SapearFragment sf =  new SapearFragment();
        Bundle args = new Bundle();

        args.putString("msg", text);

        sf.setArguments(args);

        return sf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.sapear_fragment, container, false);

        //final TextView textView = (TextView) view.findViewById(R.id.sapear_instrucctions);
        //textView.setText("Cargado dinamicamente");

        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.direction);
        final RadioButton toValpo = (RadioButton) view.findViewById(R.id.direction_valpo);
        final RadioButton toVina= (RadioButton) view.findViewById(R.id.direction_vina);

        final FloatingActionButton submitSapeada = (FloatingActionButton) view.findViewById(R.id.report_sapeada);

        submitSapeada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                //Button disabled
                submitSapeada.setEnabled(false);

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

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        Constants.URL_POST_SAPEADA,
                        sighting,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Snackbar snackbar = Snackbar
                                        .make(v, "Sapeada guardada exitosamente.", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                                submitSapeada.setEnabled(true);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                OnErrorDialogFragment dialog = new OnErrorDialogFragment();
                                dialog.show(getFragmentManager(), "onError");

                                submitSapeada.setEnabled(true);
                            }
                        }
                );
                queue.add(jsonObjectRequest);
            }
        });
        return view;
    }
}
