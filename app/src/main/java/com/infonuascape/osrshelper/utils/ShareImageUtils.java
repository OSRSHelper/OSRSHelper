package com.infonuascape.osrshelper.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.infonuascape.osrshelper.R;
import com.infonuascape.osrshelper.models.players.PlayerSkills;
import com.infonuascape.osrshelper.views.RSView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

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
            shareViewAsBitmap(context, rsView, username, "total " + playerSkills.overall.getVirtualLevel());
        } catch(Exception e) {
            e.printStackTrace();
            if(context != null) {
                Toast.makeText(context, R.string.error_when_sharing, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static void shareViewAsBitmap(final Context context, final View view, final String title, final String description) {

        Intent i = new Intent(Intent.ACTION_SEND);

        i.setType("image/*");

        i.putExtra(Intent.EXTRA_STREAM, getImageUri(context, getBitmapFromView(view), title, description));
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

    private static Uri getImageUri(Context context, Bitmap image, final String title, final String description) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        Uri url = null;

        try {
            url = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (image != null) {
                OutputStream imageOut = context.getContentResolver().openOutputStream(url);
                try {
                    image.compress(Bitmap.CompressFormat.JPEG, 100, imageOut);
                } finally {
                    imageOut.close();
                }

                long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                Bitmap miniThumb = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), id, MediaStore.Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                storeThumbnail(context.getContentResolver(), miniThumb, id, 50F, 50F, MediaStore.Images.Thumbnails.MICRO_KIND);
            } else {
                context.getContentResolver().delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            if (url != null) {
                context.getContentResolver().delete(url, null, null);
                url = null;
            }
        }

        return url;
    }

    /**
     * A copy of the Android internals StoreThumbnail method, it used with the insertImage to
     * populate the android.provider.MediaStore.Images.Media#insertImage with all the correct
     * meta data. The StoreThumbnail method is private so it must be duplicated here.
     * @see android.provider.MediaStore.Images.Media (StoreThumbnail private method)
     */
    private static final Bitmap storeThumbnail(
            ContentResolver cr,
            Bitmap source,
            long id,
            float width,
            float height,
            int kind) {

        // create the matrix to scale it
        Matrix matrix = new Matrix();

        float scaleX = width / source.getWidth();
        float scaleY = height / source.getHeight();

        matrix.setScale(scaleX, scaleY);

        Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
                source.getWidth(),
                source.getHeight(), matrix,
                true
        );

        ContentValues values = new ContentValues(4);
        values.put(MediaStore.Images.Thumbnails.KIND,kind);
        values.put(MediaStore.Images.Thumbnails.IMAGE_ID,(int)id);
        values.put(MediaStore.Images.Thumbnails.HEIGHT,thumb.getHeight());
        values.put(MediaStore.Images.Thumbnails.WIDTH,thumb.getWidth());

        Uri url = cr.insert(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

        try {
            OutputStream thumbOut = cr.openOutputStream(url);
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }
}
