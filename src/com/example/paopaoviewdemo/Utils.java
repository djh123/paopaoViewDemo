package com.example.paopaoviewdemo;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class Utils {
	public static boolean isSystemApplication(Context context, String packageName){  
        PackageManager manager = context.getPackageManager();  
        try {  
            PackageInfo packageInfo = manager.getPackageInfo(packageName, PackageManager.GET_CONFIGURATIONS);  
            // 1  
            if(new File("/data/app/"+packageInfo.packageName+".apk").exists()){  
                return true;  
			}
			// 2
			if (packageInfo.versionName != null && packageInfo.applicationInfo.uid > 10000) {
				return true;
			}
			// 3
			if ((packageInfo.applicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0) {
				return true;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	// 2、Bitmap → byte[]
		public static byte[] bitmapToBytes(Bitmap bm) {
			if (bm == null) {
				return null;
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);

			return baos.toByteArray();
		}

		// 3、byte[] → Bitmap
		public static Bitmap bytesToBitmap(byte[] b) {
			if (b.length != 0) {
				return BitmapFactory.decodeByteArray(b, 0, b.length);
			} else {
				return BitmapFactory.decodeByteArray(null, 0, 0);
			}
		}

		public static byte[] drawableTobyte(Drawable drawable, Context context) {
			return bitmapToBytes(drawableToBitmap(drawable));
		}

		public static Bitmap drawableToBitmap(Drawable drawable) {
			int width = drawable.getIntrinsicWidth();
			int height = drawable.getIntrinsicHeight();
			Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
					: Bitmap.Config.RGB_565;
			Bitmap bitmap = Bitmap.createBitmap(width, height, config);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, width, height);
			drawable.draw(canvas);
			return bitmap;
		}
}
