package com.binomed.dont.forget.mom.screen.trips;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.binomed.dont.forget.mom.R;
import com.binomed.dont.forget.mom.utils.LocalActivityManagerFragment;

@SuppressLint("NewApi")
public class CurentTripFragment extends LocalActivityManagerFragment {

	// @InjectView(R.id.webview)
	WebView webView;

	private SherlockFragmentActivity activity;
	private final static String ACTIVITY_TAG = "hosted";

	private static final String MAP_URL = "http://gmaps-samples.googlecode.com/svn/trunk/articles-android-webmap/simple-android-map.html";

	/** Called when the activity is first created. */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View mainView = inflater.inflate(R.layout.fragment_curent_trips, container, false);

		Intent intent = new Intent(getActivity(), InnerMapActivity.class);

		ViewGroup mapViewContainer = (ViewGroup) mainView.findViewById(R.id.mapviewcontainer);
		final Window w = getLocalActivityManager().startActivity(ACTIVITY_TAG, intent);
		final View wd = w != null ? w.getDecorView() : null;

		if (wd != null) {
			ViewParent parent = wd.getParent();
			if (parent != null) {
				ViewGroup v = (ViewGroup) parent;
				v.removeView(wd);
			}

			wd.setVisibility(View.VISIBLE);
			wd.setFocusableInTouchMode(true);
			if (wd instanceof ViewGroup) {
				((ViewGroup) wd).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
			}
		}

		mapViewContainer.addView(wd);

		// webView = (WebView) mainView.findViewById(R.id.webview);
		// // Wait for the page to load then send the location information
		// webView.setWebViewClient(mWebViewClient);
		// webView.setWebChromeClient(mWebChromeClient);

		return mainView;

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// webView.clearCache(true);
		// webView.getSettings().setJavaScriptEnabled(true);
		// webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
		// webView.loadUrl(MAP_URL);

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
			activity.setProgressBarVisibility(false);
			activity.setProgressBarIndeterminateVisibility(false);
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
