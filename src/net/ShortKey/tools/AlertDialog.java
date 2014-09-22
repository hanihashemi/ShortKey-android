package net.ShortKey.tools;

import android.content.Context;
import android.content.DialogInterface;
import net.ShortKey.R;

/**
 * Created by hani on 9/19/14.
 */
public class AlertDialog {
    Context context;

    public AlertDialog(Context context) {
        this.context = context;
    }

    public void show(String title, String summary) {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
        alert.setTitle(title);
        alert.setMessage(summary);
        alert.setPositiveButton(context.getString(R.string.notice_dialog_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        alert.show();
    }
}
