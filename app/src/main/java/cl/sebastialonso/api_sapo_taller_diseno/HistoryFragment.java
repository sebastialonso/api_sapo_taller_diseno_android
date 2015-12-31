package cl.sebastialonso.api_sapo_taller_diseno;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seba on 12/27/15.
 */
public class HistoryFragment extends Fragment {

    private List<Sapeada> sapeadas;
    private SupportMapFragment mSupportMapFragment;

    private void initializeDate(){
        sapeadas = new ArrayList<>();
        sapeadas.add(new Sapeada(-33.027754, -71.544145, 35000, false));
        sapeadas.add(new Sapeada(-33.037540, -71.598046, 46876, false));
        sapeadas.add(new Sapeada(-33.024155, -71.567834, 60473, true));
        sapeadas.add(new Sapeada(-33.027754, -71.576417, 60473, true));
        sapeadas.add(new Sapeada(-33.025163, -71.559938, 60473, true));
    }

    public static HistoryFragment newInstance(String text) {

        HistoryFragment hf = new HistoryFragment();
        Bundle args = new Bundle();

        args.putString("msg", text);
        hf.setArguments(args);

        return hf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        initializeDate();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.history_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.sapeadas_recyclerview);

        //LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Adapter
        final SapeadaAdapter adapter = new SapeadaAdapter(sapeadas);
        adapter.setOnItemClickListener(new SapeadaAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                final Sapeada current = sapeadas.get(position);

                if (mSupportMapFragment != null){
                    mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng moveToThis = new LatLng(current.mLatitude, current.mLongitude);

                            //Clean the markers
                            googleMap.clear();
                            googleMap.addMarker(new MarkerOptions().position(moveToThis));

                            CameraPosition cameraPosition = new CameraPosition.Builder().target(moveToThis).zoom(13.0f).build();
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                            googleMap.moveCamera(cameraUpdate);
                        }
                    });
                }
            }
        });
        recyclerView.setAdapter(adapter);

        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapwhere);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapwhere, mSupportMapFragment).commit();
        }

        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if (googleMap != null) {

                        googleMap.getUiSettings().setAllGesturesEnabled(true);

                        //Draw marker on first sapeada
                        LatLng marker_latlng = new LatLng(sapeadas.get(0).mLatitude, sapeadas.get(0).mLongitude);

                        googleMap.addMarker(new MarkerOptions().position(marker_latlng));
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(marker_latlng).zoom(13.0f).build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        googleMap.moveCamera(cameraUpdate);

                    }

                }
            });
        }
        return view;
    }
}