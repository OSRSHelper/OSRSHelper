package com.infonuascape.osrshelper.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.views.RSView;

import java.io.ByteArrayOutputStream;

/**
 * Created by marc-antoinehinse on 2018-01-13.
 */

public class ShareImageUtils {
    public static void shareHiscores(final Context context, final String username, final PlayerSkills playerSkills) {
        RSView rsView = new RSView(context);
        rsView.populateViewForImageShare(playerSkills, username, null);
        int width = context.getResources().getDimensionPixelSize(R.dimen.rs_view_width);
        int height = context.getResources().getDimensionPixelSize(R.dimen.rs_view_header_height)
                + (8 * context.getResources().getDimensionPixelSize(R.dimen.rs_view_item_height))
                + (2 * context.getResources().getDimensionPixelSize(R.dimen.rs_view_padding));
        try {
            rsView.measure(width, height);
            rsView.layout(0, 0, width, height);
            shareViewAsBitmap(context, rsView, username + " (total " + playerSkills.overall.getVirtualLevel() + ")");
        } catch(Exception e) {
            e.printStackTrace();
            if(context != null) {
                Toast.makeText(context, R.string.error_when_sharing, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static void shareViewAsBitmap(final Context context, final View view, final String title) {

        Intent i = new Intent(Intent.ACTION_SEND);

        i.setType("image/*");

        i.putExtra(Intent.EXTRA_STREAM, getImageUri(context, getBitmapFromView(view), title));
        try {
            context.startActivity(Intent.createChooser(i, "My OSRS Stats"));
        } catch (android.content.ActivityNotFoundException ex) {

            ex.printStackTrace();
        }
    }

    private static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#453C33"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);
        view.draw(canvas);
        return returnedBitmap;
    }

    private static Uri getImageUri(Context context, Bitmap image, final String title) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), image, title, "OSRS Helper Hiscores");
        return Uri.parse(path);
    }
}
