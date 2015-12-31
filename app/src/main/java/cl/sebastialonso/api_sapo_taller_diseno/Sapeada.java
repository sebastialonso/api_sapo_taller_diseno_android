package cl.sebastialonso.api_sapo_taller_diseno;

/**
 * Created by seba on 12/29/15.
 */
public class Sapeada {

    double mLatitude;
    double mLongitude;
    int mTime;
    boolean mDirection;

    Sapeada(double lat, double lon, int time, boolean direction){
        mLatitude = lat;
        mLongitude = lon;
        mTime = time;
        mDirection = direction;
    }
}
