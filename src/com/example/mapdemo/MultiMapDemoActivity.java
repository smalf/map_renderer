/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mapdemo;

import com.example.mapdemo.renderer.TakeSnapshotTask;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * This shows how to create a simple activity with multiple maps on screen.
 */
public class MultiMapDemoActivity extends FragmentActivity implements OnClickListener {
	private ImageView ivSnapshot;
	private SupportMapFragment mapFragment;
	private Handler mUiHandler;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multimap_demo);
        
        mUiHandler = new Handler();
        init();
    }
    
	@Override
	public void onClick(View v) {
		new TakeSnapshotTask(mapFragment, null, new TakeSnapshotTask.TakeSnapshotListener() {

			@Override
			public void onSnapshotReady(SupportMapFragment mapFragment, final Bitmap snapshot) {
				replaseImage(snapshot);
			}
			
		}).run();
		Toast.makeText(this, getString(R.string.take_snapshot), Toast.LENGTH_LONG).show();
		
	}
	
	private void init() {
		mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);
		mapFragment.getMapAsync(new MapReadyListener());
		
		
		Button takeSnapshotBTN = (Button) findViewById(R.id.btn_snapshot);
		takeSnapshotBTN.setOnClickListener(this);
		
		ivSnapshot = (ImageView) findViewById(R.id.map_snapshot);
	} 
	
	private class MapReadyListener implements OnMapReadyCallback {

		@Override
		public void onMapReady(final GoogleMap map) {
			map.setOnMapLoadedCallback(new OnMapLoadedCallback() {

				@Override
				public void onMapLoaded() {
					new TakeSnapshotTask(mapFragment, null, new TakeSnapshotTask.TakeSnapshotListener() {

						@Override
						public void onSnapshotReady(SupportMapFragment mapFragment, final Bitmap snapshot) {
							replaseImage(snapshot);
						}
						
					}).run();
					map.setOnCameraChangeListener(new CameraChangedListener());
				}
				
			});
			
		}
		
	}
	
	private class CameraChangedListener implements OnCameraChangeListener {

		@Override
		public void onCameraChange(CameraPosition arg0) {
			mapFragment.getMap().setOnMapLoadedCallback(new OnMapLoadedCallback() {

				@Override
				public void onMapLoaded() {
					new TakeSnapshotTask(mapFragment, null, new TakeSnapshotTask.TakeSnapshotListener() {

						@Override
						public void onSnapshotReady(SupportMapFragment mapFragment, final Bitmap snapshot) {
							replaseImage(snapshot);
						}
						
					}).run();
				}
				
			});
		}
		
	}
	
	private void replaseImage(final Bitmap snapshot) {
		mUiHandler.post(new Runnable() {

			@Override
			public void run() {
				ivSnapshot.setImageBitmap(snapshot);
			}
			
		});
	}
}
