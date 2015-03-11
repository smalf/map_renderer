package com.example.mapdemo.renderer;

import android.graphics.Bitmap;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;

public class TakeSnapshotTask extends MapRenderTask {
	
	public static interface TakeSnapshotListener {
		public void onSnapshotReady(SupportMapFragment mapFragment, Bitmap snapshot);
	}
	
	private TakeSnapshotListener mSnapshotListener;
	private SnapshotReadyCallback mSapshotReadyCallback = new SnapshotReadyCallback() {

		@Override
		public void onSnapshotReady(final Bitmap bitmap) {
			mSnapshotListener.onSnapshotReady(mMapFragment, bitmap);
			finishTask();
		}
		
	};

	public TakeSnapshotTask(SupportMapFragment mapFragment,	RenderCallback callback, TakeSnapshotListener listener) {
		super(mapFragment, callback);
		mSnapshotListener = listener;
	}

	@Override
	protected void onRenderFinished() {
		mMapFragment.getMap().snapshot(mSapshotReadyCallback);
	}

}
