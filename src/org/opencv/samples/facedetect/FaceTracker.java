package org.opencv.samples.facedetect;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;

public class FaceTracker {
	private static class _FaceEntry {
		public Rect rect;
		public long earliestTimestamp;
		public long latestTimestamp;
	}
	private static class _FaceList {
		public ArrayList<_FaceEntry> faces = new ArrayList<_FaceEntry>();
		public long latestTimestamp = 0;
	}
	
	private static final ExecutorService _executor = Executors.newSingleThreadExecutor();
	
	private final DetectionBasedTracker _classifier;
	private final double _maxGrowthRate,_maxMoveRate;
	private final long _faceDecayTime,_minNewFaceTime;
	
	private ArrayList<Rect> _good = new ArrayList<Rect>(),
							_bad  = new ArrayList<Rect>();
	
	private final _FaceList _faceList = new _FaceList();
	
	private Future<Rect[]> _job = null;
	private long _jobTimestampMs = 0;
	
	private void _updateList(Rect[] faces,long timestampMs) {
		_good.clear();
		_bad.clear();
		double dt = (timestampMs-_faceList.latestTimestamp)/1000.0;
		ArrayList<_FaceEntry> newEntries = new ArrayList<_FaceEntry>();
		boolean[] used = new boolean[faces.length];
		for(_FaceEntry entry: _faceList.faces) {
			if(timestampMs-entry.latestTimestamp > _faceDecayTime) {
				continue;
			}
			for(int j=0;j<faces.length;++j) {
				if(used[j]) {
					continue;
				}
				double diffX = (faces[j].x+faces[j].width/2.0)-(entry.rect.x+entry.rect.width/2.0),
					   diffY = (faces[j].y+faces[j].height/2.0)-(entry.rect.y+entry.rect.height/2.0);
				if(Math.abs(faces[j].area()-entry.rect.area()) < _maxGrowthRate*dt &&
				   Math.hypot(diffX, diffY) < _maxMoveRate*dt) {
					used[j] = true;
					entry.rect = faces[j];
					entry.latestTimestamp = timestampMs;
					newEntries.add(entry);
					if(timestampMs-entry.earliestTimestamp > _minNewFaceTime) {
						_good.add(faces[j]);
					} else {
						_bad.add(faces[j]);
					}
					break;
				}
			}
		}
		for(int i=0;i<faces.length;++i) {
			if(used[i]) {
				continue;
			}
			_FaceEntry newEntry = new _FaceEntry();
			newEntry.rect = faces[i];
			newEntry.earliestTimestamp = timestampMs;
			newEntry.latestTimestamp = timestampMs;
			newEntries.add(newEntry);
			_bad.add(faces[i]);
		}
		_faceList.faces = newEntries;
	}

	public FaceTracker(DetectionBasedTracker classifier,
					   double maxGrowthRate,double maxMoveRate,
					   double faceDecayTime,double minNewFaceTime) {
		_classifier = classifier;
		_maxGrowthRate = maxGrowthRate;
		_maxMoveRate = maxMoveRate;
		_faceDecayTime = (long)(faceDecayTime*1000);
		_minNewFaceTime = (long)(minNewFaceTime*1000);
	}
	
	public void addFaceSet(final Mat img,long timestampMs) {
		if(_job != null) {
			if(_job.isDone()) {
				try {
					_updateList(_job.get(),_jobTimestampMs);
				} catch (InterruptedException e) {}
				  catch (ExecutionException e)   {}
			}
			if(_job.isDone() || _job.isCancelled()) {
				_job = null;
			}
		}
		if(_job == null) {
			_jobTimestampMs = timestampMs;
			_job = _executor.submit(new Callable<Rect[]>() {
				final Mat _img = img.clone();
				@Override
				public Rect[] call() {
			        MatOfRect faces = new MatOfRect();
					_classifier.detect(_img,faces);
					return faces.toArray();
				}
			});
		}
	}
	public Rect[] goodFaces() {
		Rect[] ret = new Rect[_good.size()];
		return _good.toArray(ret);
	}
	public Rect[] badFaces() {
		Rect[] ret = new Rect[_bad.size()];
		return _bad.toArray(ret);
	}
}
