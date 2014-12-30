package test.com.adbirdads;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class Utilities extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void setupUI(View view) {
		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					hideSoftKeyboard(Utilities.this);
					return false;
				}

			});
		}
		// If a layout container, iterate over children and seed recursion.
		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

				View innerView = ((ViewGroup) view).getChildAt(i);

//				setupUI(innerView);
			}
		}
//		setupFontsGotham(view);

	}

	public void setupFontsHelLight(View view) {
		// set font
		if (view instanceof TextView) {
			Typeface font = Typeface.createFromAsset(getAssets(),
					"fonts/helvetica-neue-light.otf");
			((TextView) view).setTypeface(font);
		}
	}

	public void setupFontsHelReg(View view) {
		if (view instanceof TextView) {
			Typeface font = Typeface.createFromAsset(getAssets(),
					"fonts/helvetica-neue-regular.ttf");
			((TextView) view).setTypeface(font);
		}
	}

	public void setupFontsHelBold(View view) {
		if (view instanceof TextView) {
			Typeface font = Typeface.createFromAsset(getAssets(),
					"fonts/helvetica-neue-bold.ttf");
			((TextView) view).setTypeface(font);
		}
	}

	public void setupFontsFamiliar(View view) {
		// set font
		if (view instanceof TextView) {
			Typeface font = Typeface.createFromAsset(getAssets(),
					"fonts/Familiar_Pro-Bold.otf");
			((TextView) view).setTypeface(font);
		}
	}

	public void setupFontsGotham(View view) {
		// set font
//		if (view instanceof TextView) {
//			Typeface font = Typeface.createFromAsset(getAssets(),
//					"fonts/GothaProReg.otf");
//			((TextView) view).setTypeface(font);

//		}
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
	}
}
