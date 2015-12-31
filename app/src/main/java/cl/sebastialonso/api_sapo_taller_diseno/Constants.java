package cl.sebastialonso.api_sapo_taller_diseno;

/**
 * Created by seba on 12/28/15.
 */
public class Constants {
    public static final String URL_BASE = "http://159.203.82.3:3000";
    public static final String V1 = "/api/v1";
    public static final String URL_LOGIN = URL_BASE + V1 + "/users/sessions";
    public static final String URL_POST_SAPEADA = URL_BASE + V1 + "/sapeadas";
    public static final String URL_PREDICT = URL_BASE + V1 + "/prediccion";
}
