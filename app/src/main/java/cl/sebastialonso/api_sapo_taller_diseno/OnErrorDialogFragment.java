package cl.sebastialonso.api_sapo_taller_diseno;

import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by seba on 12/30/15.
 */
public class OnErrorDialogFragment extends DialogFragment{

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState){
        return new AlertDialog.Builder(getActivity())
                .setMessage("El backend de la aplicación debe estar corriendo en el puerto 3000.\n" +
                "Puedes encontrar el source del sistema en https://github.com/sebastialonso/api_sapo_taller_diseno")
                .setTitle("El backend está apagado...")
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id){
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.dialog_see_code, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id){
                        Intent toSource = new Intent();
                        toSource.setData(Uri.parse(Constants.URL_TO_REPO));
                        startActivity(toSource);
                    }
                }).create();
    }
}
