package org.on.puz.photobombsquad;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.opencv.core.Mat;

public class FrameTracker {
	private static final long MILLISECONDS_TO_REMEMBER = 4000;
	private static final int MAX_SNAPSHOTS_TO_REMEMBER = 8;
	private static final int DOWN_SCALE_FACTOR = 8;

	private final Map<Long, Mat> rememberedFrames;

	public FrameTracker() {
		rememberedFrames = new LinkedHashMap<Long, Mat>();
	}

	private boolean checkInterval(long now) {
		long oldestRemembered = rememberedFrames.keySet().iterator().next().longValue();
		return now > oldestRemembered + rememberedFrames.size() * (MILLISECONDS_TO_REMEMBER / MAX_SNAPSHOTS_TO_REMEMBER);
	}

	public void addFrame(final Mat img,long timestampMs) {
		//camera has its own thread in CameraBridgeViewBase#connectCamera(int, int),
		//so take precautions with UI thread that calls #getNoBomberFrame(current)
		synchronized (rememberedFrames) {
			//kick out remembered frames that are older than MILLISECONDS_TO_REMEMBER.
			//assert LinkedHashMap preserves insertion order, which should be in timestamp order
			for (Iterator<Long> iter = rememberedFrames.keySet().iterator(); iter.hasNext() && timestampMs > iter.next().longValue() + MILLISECONDS_TO_REMEMBER; )
				iter.remove();
			//add if rememberedFrames.size() is less than MAX_SNAPSHOTS_TO_REMEMBER and check the interval to space out remembered frames
			if (rememberedFrames.isEmpty() || rememberedFrames.size() < MAX_SNAPSHOTS_TO_REMEMBER && checkInterval(timestampMs))
				rememberedFrames.put(Long.valueOf(timestampMs), img);
		}
	}

	public Mat getNoBomberFrame(Mat current) {
		int rows = current.rows() / DOWN_SCALE_FACTOR, cols = current.cols() / DOWN_SCALE_FACTOR;
		Mat avg = new Mat(rows, cols, current.type());
		double[] means = new double[4];
		synchronized (rememberedFrames) {
			//accessing elements in local variable arrays is much much faster
			//than accessing elements in instance variable collections
			Mat[] framesToAverage = rememberedFrames.values().toArray(new Mat[rememberedFrames.size()]);
			int framesCount = framesToAverage.length;
			MatAverager.average(avg, framesToAverage);
			/*
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					for (int k = 0; k < 4; k++) {
						means[k] = 0;
						for (int l = 0; l < framesCount; l++)
							means[k] += framesToAverage[l].get(i * DOWN_SCALE_FACTOR, j * DOWN_SCALE_FACTOR)[k];
						means[k] /= framesCount;
					}
					avg.put(i, j, means);
				}
			}*/
		}
		return avg;
	}
}
