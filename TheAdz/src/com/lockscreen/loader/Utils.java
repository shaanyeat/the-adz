package com.lockscreen.loader;

import java.io.InputStream;
import java.io.OutputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
 
public class Utils {
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=10240;
        try
        {
        	byte[] buffer = new byte[buffer_size];
            int count = 0;
            int n = 0;
            while (-1 != (n = is.read(buffer))) {
                os.write(buffer, 0, n);
                count += n;
            }
            
            System.out.println("Count :  " + count);
        }
        catch(Exception ex){}
    }

    
    public static Bitmap getRoundedBitmap(Bitmap bitmap, Resources resource) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        
        Bitmap map = Bitmap.createBitmap(bitmap.getWidth()+20, bitmap.getHeight()+20, Config.ARGB_8888);
        Canvas cvs = new Canvas(map);
        cvs.drawARGB(0, 0, 0, 0);
        Paint p = new Paint();
        p.setColor(Color.parseColor("#c6ced0"));
        cvs.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                (bitmap.getWidth() / 1.7f), p); 
        cvs.drawBitmap(output, 0, 0 , new Paint());
        return map;
    }
}