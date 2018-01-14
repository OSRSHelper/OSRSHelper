package com.infonuascape.osrshelper.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.utils.players.PlayerSkills;
import com.infonuascape.osrshelper.views.RSView;

import java.io.ByteArrayOutputStream;

/**
 * Created by marc-antoinehinse on 2018-01-13.
 */

public class ImageUtils {
    public static void shareHiscores(final Context context, final String username, final PlayerSkills playerSkills) {
        RSView rsView = new RSView(context);
        rsView.populateTable(playerSkills, username);
        int width = (int) Utils.convertDpToPixel(300, context);
        int height = (int) Utils.convertDpToPixel(90 + (8 * 55), context);
        rsView.measure(width, height);
        rsView.layout(0, 0, width, height);
        shareViewAsBitmap(context, rsView);
    }

    private static void shareViewAsBitmap(final Context context, final View view) {

        Intent i = new Intent(Intent.ACTION_SEND);

        i.setType("image/*");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();


        i.putExtra(Intent.EXTRA_STREAM, getImageUri(context, getBitmapFromView(view)));
        try {
            context.startActivity(Intent.createChooser(i, "My OSRS Stats"));
        } catch (android.content.ActivityNotFoundException ex) {

            ex.printStackTrace();
        }
    }

    private static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        view.draw(canvas);
        return returnedBitmap;
    }

    private static Uri getImageUri(Context context, Bitmap image) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), image, "OSRS Stats", null);
        return Uri.parse(path);
    }
}
