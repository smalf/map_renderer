package com.example.mapdemo.renderer;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;

public abstract class MapRenderTask implements Runnable {
	
	/**
	 * This guy provides contract to listening for render process
	 * 
	 * @author Serge
	 *
	 */
	public static interface RenderCallback {
		
		/**
		 * called when rendering process finished
		 * 
		 * @param mMapFragment
		 */
		public void onRenderFinished(SupportMapFragment mMapFragment);
	}
	
	protected SupportMapFragment mMapFragment;
	protected RenderCallback mRenderCallback;
	
	public MapRenderTask(SupportMapFragment mapFragment, RenderCallback callback) {
		mMapFragment = mapFragment;
		mRenderCallback = callback;
	}
	
	@Override
	public void run() {
		mMapFragment.getMap().setOnMapLoadedCallback(new OnMapLoadedCallback() {

			@Override
			public void onMapLoaded() {
				onRenderFinished();
			}
			
		});
	}
	
	protected void finishTask() {
		if (mRenderCallback != null) {
			mRenderCallback.onRenderFinished(mMapFragment);
		}
	}
	
	/**
	 * after this method finished you need to call method finishTask()
	 */
	protected abstract void onRenderFinished();
	

}
