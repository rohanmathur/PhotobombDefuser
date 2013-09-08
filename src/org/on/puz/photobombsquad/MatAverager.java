package org.on.puz.photobombsquad;

import org.opencv.core.Mat;

public final class MatAverager {
	private static native void natAverage(long out,long[] mats);
	public static void average(Mat out,Mat[] mats) {
		long[] natMats = new long[mats.length];
		for(int i=0;i<mats.length;++i) {
			natMats[i] = mats[i].getNativeObjAddr();
		}
		natAverage(out.getNativeObjAddr(),natMats);
	}
	static {
		System.loadLibrary("detection_based_tracker");
	}
}
