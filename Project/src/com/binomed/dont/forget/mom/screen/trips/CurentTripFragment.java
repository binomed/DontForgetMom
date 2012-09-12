package com.binomed.dont.forget.mom.screen.trips;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.binomed.dont.forget.mom.R;

@SuppressLint("NewApi")
public class CurentTripFragment extends Fragment {// SherlockFragment { // RoboSherlockFragment {

	// @InjectView(R.id.webview)
	WebView webView;

	private SherlockFragmentActivity activity;

	private static final String MAP_URL = "http://gmaps-samples.googlecode.com/svn/trunk/articles-android-webmap/simple-android-map.html";

	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View mainView = inflater.inflate(R.layout.fragment_curent_trips, container, false);

		webView = (WebView) mainView.findViewById(R.id.webview);
		// Wait for the page to load then send the location information
		webView.setWebViewClient(mWebViewClient);
		webView.setWebChromeClient(mWebChromeClient);
		// webView.loadUrl(MAP_URL);

		// WebView webView = (WebView) mainView.findViewById(R.id.webview);
		// webView.getSettings().setJavaScriptEnabled(true);
		// // Wait for the page to load then send the location information
		// webView.setWebViewClient(new WebViewClient());
		// webView.loadUrl(MAP_URL);

		return mainView;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		webView.clearCache(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
		webView.loadUrl(MAP_URL);
		// webView.loadUrl("http://www.google.com");
		// webView.loadUrl("https://developers.google.com/maps/documentation/javascript/examples/map-simple");
		// webView.loadUrl("file:///android_asset/helloWorld.html");
		// webView.loadUrl("http://maps.google.com/maps?" + "saddr=43.0054446,-87.9678884" + "&daddr=42.9257104,-88.0508355");

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (SherlockFragmentActivity) activity;
	}

	private final WebViewClient mWebViewClient = new WebViewClient() {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			webView.setVisibility(View.INVISIBLE);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			webView.setVisibility(View.VISIBLE);
			((SherlockFragmentActivity) activity).setProgressBarVisibility(false);
			((SherlockFragmentActivity) activity).setProgressBarIndeterminateVisibility(false);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	};

	private final WebChromeClient mWebChromeClient = new WebChromeClient() {
		@Override
		public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
			return false;
		}
	};

}
