package com.binomed.dont.forget.mom.screen.trips;

import roboguice.RoboGuice;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.utils.RoboSherlockFragment;

public class CurentTripFragment extends RoboSherlockFragment {
	
	@InjectView(R.id.webview)
	WebView webView;
	
	private SherlockFragmentActivity activity;

	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View mainView = inflater.inflate(R.layout.fragment_curent_trips, container, false);

		return mainView;

	}
	
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSupportZoom(true);
//		webView.loadUrl("file:///android_asset/map.html");
//		webView.setWebChromeClient(new WebChromeClient() {
//	        public void onProgressChanged(WebView view, int progress) {
//	            // Activities and WebViews measure progress with different scales.
//	            // The progress meter will automatically disappear when we reach 100%
//	            ((Activity) activity).setProgress(progress * 1000);
//	        }
//
//	    });
		webView.setWebViewClient(new WebViewClient(){
			public void onProgressChanged(WebView view, int progress) {
	            // Activities and WebViews measure progress with different scales.
	            // The progress meter will automatically disappear when we reach 100%
//	            ((SherlockFragmentActivity) activity).setSupportProgress(progress * 1000);
	        }

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				((SherlockFragmentActivity) activity).setSupportProgressBarVisibility(false);
			}
			
			
		});
		webView.loadUrl("http://www.google.fr");
//		webView.loadUrl("http://gmaps-samples.googlecode.com/svn/trunk/articles-android-webmap/simple-android-map.html");
//		webView.loadUrl("https://developers.google.com/maps/documentation/javascript/examples/map-simple");
//		webView.loadUrl("file:///android_asset/helloWorld.html");
		
	}



	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (SherlockFragmentActivity) activity;
	}

}
