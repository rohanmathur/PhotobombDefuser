package org.on.puz.photobombsquad;

import java.io.File;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

public class SingleMediaScanner implements MediaScannerConnectionClient {
	private MediaScannerConnection conn;
	private File file;
	
	public SingleMediaScanner(Context context, File f) {
	    file = f;
	    conn = new MediaScannerConnection(context, this);
	    conn.connect();
	}
	
	@Override
	public void onMediaScannerConnected() {
	    conn.scanFile(file.getAbsolutePath(), null);
	}
	
	@Override
	public void onScanCompleted(String path, Uri uri) {
	    conn.disconnect();
	}

}