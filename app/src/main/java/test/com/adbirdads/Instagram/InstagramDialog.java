package test.com.adbirdads.Instagram;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InstagramDialog extends Dialog {

	float[] DIMENSIONS_LANDSCAPE = {480, 260};
	float[] DIMENSIONS_PORTRAIT = { 320, 520 };

	static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.FILL_PARENT);
	static final int MARGIN = 0;
	static final int PADDING = 0;

	private String mUrl;
	private OAuthDialogListener mListener;
	private ProgressDialog mSpinner;
	private WebView mWebView;
	private LinearLayout mContent;
	private TextView mTitle;
	private static final String TAG = "Instagram-WebView";
	public Context context = null;
	public String language = "";

	public InstagramDialog(Context context, String url,OAuthDialogListener listener) {
		super(context);
		this.context = context;
		mUrl = url;
		mListener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(context);
		String myPref_text = myPref.getString("adbirdlanguage", null);
		if(myPref_text == null){
			language = "ru";
		}else{
			if(myPref_text != null){
				language = myPref_text;
			}
		}

		mSpinner = new ProgressDialog(getContext());
		mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
		/*if(language.equals("ru")){
			mSpinner.setMessage(context.getResources().getString(R.string.loading_ru));
		}else{
			mSpinner.setMessage(context.getResources().getString(R.string.loading));
		}*/
		mContent = new LinearLayout(getContext());
		mContent.setOrientation(LinearLayout.VERTICAL);
		setUpTitle();
		setUpWebView();

		Display display = getWindow().getWindowManager().getDefaultDisplay();
		final float scale = getContext().getResources().getDisplayMetrics().density;
		float[] dimensions = (display.getWidth() < display.getHeight()) ? DIMENSIONS_PORTRAIT : DIMENSIONS_LANDSCAPE;

		addContentView(mContent, new FrameLayout.LayoutParams(
				(int) (dimensions[0] * scale + 0.5f), (int) (2*480)));
		CookieSyncManager.createInstance(getContext());
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();


	}

	private void setUpTitle() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mTitle = new TextView(getContext());
		mTitle.setText("");
		mTitle.setTextColor(Color.WHITE);
		mTitle.setTypeface(Typeface.DEFAULT_BOLD);
		mTitle.setBackgroundColor(Color.BLACK);
		mTitle.setPadding(MARGIN + PADDING, MARGIN, MARGIN, MARGIN);
		mContent.addView(mTitle);
	}

	private void setUpWebView() {
		mWebView = new WebView(getContext());
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.requestFocus(View.FOCUS_DOWN);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setWebViewClient(new OAuthWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl(mUrl);
		mWebView.setLayoutParams(FILL);
		mContent.addView(mWebView);
		InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null) {
			inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		}
	}

	private class OAuthWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(TAG, "Redirecting URL " + url);

			if (url.startsWith(InstagramApp.mCallbackUrl)) {
				String urls[] = url.split("=");
				mListener.onComplete(urls[1]);

				InstagramDialog.this.dismiss();
				return true;
			}
			return false;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			Log.d(TAG, "Page error: " + description);

			super.onReceivedError(view, errorCode, description, failingUrl);
			mListener.onError(description);

			InstagramDialog.this.dismiss();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d(TAG, "Loading URL: " + url);

			super.onPageStarted(view, url, favicon);
			mSpinner.show();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			String title = mWebView.getTitle();
			if (title != null && title.length() > 0) {
				//mTitle.setText(title);
			}

			Log.d(TAG, "onPageFinished URL: " + url);
			mSpinner.dismiss();
		}

	}

	public interface OAuthDialogListener {
		public abstract void onComplete(String accessToken);
		public abstract void onError(String error);
	}

}