package test.com.adbirdads;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageAdapter extends PagerAdapter {
	Context context;
	
	public void setLanguage(String language) {
		if(language.equals("English")) {
			this.GalImages = new int[] { R.drawable.tutorial_rating_en, R.drawable.tutorial_ads_en,
					R.drawable.tutorial_profile_en };
		} else {
			this.GalImages = new int[] { R.drawable.tutorial_rating_ru,R.drawable.tutorial_ads_ru,
					R.drawable.tutorial_profile_ru };
		}
	}
	
	private int[] GalImages = new int[] { R.drawable.tutorial_rating_ru,R.drawable.tutorial_ads_ru,
            R.drawable.tutorial_profile_ru };
	

	public ImageAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return GalImages.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((ImageView) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imageView = new ImageView(context);
		int padding = 0;// change later maybe
		imageView.setPadding(padding, padding, padding, padding);
		imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

		imageView.setImageResource(GalImages[position]);
		((ViewPager) container).addView(imageView, 0);
		return imageView;
	}
	
	

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((ImageView) object);
	}
}
