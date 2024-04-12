package com.example.jjplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class BlurUtils {
    public static Bitmap blur(Context context, Bitmap bitmap, float radius) {
        // Create a RenderScript context
        RenderScript rs = RenderScript.create(context);

        // Create an input allocation from the bitmap
        Allocation input = Allocation.createFromBitmap(rs, bitmap);

        // Create an output allocation
        Allocation output = Allocation.createTyped(rs, input.getType());

        // Create a blur script
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        blurScript.setRadius(radius);

        // Perform the blur operation
        blurScript.setInput(input);
        blurScript.forEach(output);

        // Copy the blurred output to a new Bitmap
        Bitmap blurredBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        output.copyTo(blurredBitmap);

        // Destroy the RenderScript context to free resources
        rs.destroy();

        return blurredBitmap;
    }
}
