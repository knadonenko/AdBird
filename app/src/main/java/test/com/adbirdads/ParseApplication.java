package test.com.adbirdads;

import java.util.HashMap;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.parse.Parse;
import com.parse.PushService;

import android.app.Application;
import android.content.res.Configuration;

public class ParseApplication extends Application {

	public enum TrackerName {
		APP_TRACKER, // Tracker used only in this app
	}

	HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

	synchronized Tracker getTracker(TrackerName trackerId) {
		if (!mTrackers.containsKey(trackerId)) {

			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			Tracker t = analytics.newTracker(R.xml.app_tracker);
			mTrackers.put(trackerId, t);

		}
		return mTrackers.get(trackerId);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "UPhfafiV55mpdVcRwomSSNGW246s8v0vxLrZJBw4","0PF8Cd9q1s52JniwOVjtopnczy5qm8oRAz7mucPn");
		PushService.setDefaultPushCallback(this, MainActivity.class);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
