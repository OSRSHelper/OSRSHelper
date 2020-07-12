package com.infonuascape.osrshelper.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.views.RSView;

import java.io.OutputStream;

/**
 * Created by marc-antoinehinse on 2018-01-13.
 */

public class ShareImageUtils {
    public static void shareHiscores(final Context context, final String username, final PlayerSkills playerSkills) {
        RSView rsView = new RSView(context);
        rsView.populateViewForImageShare(playerSkills, username, null);
        int width = context.getResources().getDimensionPixelSize(R.dimen.rs_view_width);
        int height = context.getResources().getDimensionPixelSize(R.dimen.rs_view_header_share_height);
        try {
            rsView.measure(width, height);
            rsView.layout(0, 0, width, height);
            final Bitmap image = getBitmapFromView(rsView);

            final String description = "Total level " + playerSkills.overall.getVirtualLevel();
            final Uri uri = storeImage(context, image, username, description);

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("image/*");
            i.putExtra(Intent.EXTRA_STREAM, uri);
            context.startActivity(Intent.createChooser(i, context.getString(R.string.hiscore_share_intent_name)));
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(context, R.string.error_when_sharing, Toast.LENGTH_SHORT).show();
        }
    }

    private static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Paint paint = new Paint();
        paint.setColor(view.getContext().getResources().getColor(R.color.rs_view_bg));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);
        view.draw(canvas);
        return returnedBitmap;
    }

    private static Uri storeImage(Context context, Bitmap image, final String title, final String description) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri uri = null;

        try {
            uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (image != null) {
                OutputStream imageOut = context.getContentResolver().openOutputStream(uri);
                try {
                    image.compress(Bitmap.CompressFormat.JPEG, 100, imageOut);
                } finally {
                    if (imageOut != null) {
                        imageOut.close();
                    }
                }
            } else if (uri != null) {
                context.getContentResolver().delete(uri, null, null);
                uri = null;
            }
        } catch (Exception e) {
            if (uri != null) {
                context.getContentResolver().delete(uri, null, null);
                uri = null;
            }
        }

        return uri;
    }
}
