package org.jak_linux.dns66;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jak on 15/10/16.
 */

public final class FileHelper {

    public static InputStream openRead(Context context, String filename) throws IOException {
        try {
            return context.openFileInput(filename);
        } catch (FileNotFoundException e) {
            return context.getAssets().open(filename);
        }
    }

    public static OutputStream openWrite(Context context, String filename) throws IOException {
        // TODO Keep an old version around
        return context.openFileOutput(filename, Context.MODE_PRIVATE);
    }
}
