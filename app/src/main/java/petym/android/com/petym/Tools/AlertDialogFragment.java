package petym.android.com.petym.Tools;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import petym.android.com.petym.R;

/**
 * Created by k3nt on 2017/8/5.
 */
public  class AlertDialogFragment
        extends DialogFragment implements DialogInterface.OnClickListener {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.title)
                .setIcon(R.drawable.alert)
                .setMessage(R.string.msg_Alert)
                .setPositiveButton(R.string.text_btYes, this)
                .setNegativeButton(R.string.text_btNo, this)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                getActivity().finish();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialog.cancel();
                break;
            default:
                break;
        }
    }
}
