package com.infonuascape.osrshelper.views;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.utils.Utils;
import com.jjoe64.graphview.series.DataPointInterface;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;

/**
 * Created by marc_ on 2018-01-14.
 */

public class GrandExchangePointDialog {
    public static AlertDialog showDialog(final Context context, final String title, final DataPointInterface dataPoint) {
        AlertDialog dialog = null;
        if(context != null && dataPoint != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            final View dialogView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.grand_exchange_dialog, null, false);

            ((TextView) dialogView.findViewById(R.id.serie_title)).setText(title);
            ((TextView) dialogView.findViewById(R.id.date)).setText(DateFormat.getDateInstance(DateFormat.LONG).format(new Date((long) dataPoint.getX())));
            ((TextView) dialogView.findViewById(R.id.price)).setText(NumberFormat.getInstance().format((long) dataPoint.getY()) + "gp");

            builder.setView(dialogView);
            dialog = builder.create();
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.show();

            final AlertDialog finalDialog = dialog;
            dialog.findViewById(R.id.container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalDialog.dismiss();
                }
            });

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = (int) Utils.convertDpToPixel(300, context);
            dialog.getWindow().setAttributes(lp);

        }
        return dialog;
    }
}
