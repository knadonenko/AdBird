package test.com.adbirdads;
 
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class InstructionActivity extends Activity implements OnClickListener{

	public String language = null;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private ViewFlipper mViewFlipper;	
	private AnimationListener mAnimationListener;
	

	public int counter = 0;
	@SuppressWarnings("deprecation")
	private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());
	
	public LinearLayout indicator1 = null;
	public LinearLayout indicator2 = null;
	public LinearLayout indicator3 = null;
	
	public String testRu[] = {"Инструкция 1","Инструкция 2","Инструкция 3"};
	public String testEn[] = {"Instruction 1","Instruction 2","Instruction 3"};
	public String test[] = null;
	
	public TextView text1 = null;
	public TextView text2 = null;
	public TextView text3 = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instruction_testing);
		
		ActionBar action_bar = getActionBar();
		action_bar.hide();
		
		SharedPreferences myPr = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String myPref_text = myPr.getString("adbirdlanguage", null);
		if(myPref_text == null){
			language = "ru";
		}else{
			if(myPref_text != null){
				language = myPref_text;
			}
		}
		
		text1 = (TextView)this.findViewById(R.id.textView1);
		text2 = (TextView)this.findViewById(R.id.textView2);
		text3 = (TextView)this.findViewById(R.id.textView3);
		
		ImageView next = (ImageView)this.findViewById(R.id.imageView1);
		next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),MainActivity.class);
				startActivity(intent);
			}
		});
		
		indicator1 = (LinearLayout)this.findViewById(R.id.layout1);
		indicator2 = (LinearLayout)this.findViewById(R.id.layout2);
		indicator3 = (LinearLayout)this.findViewById(R.id.layout3);
		

		mViewFlipper = (ViewFlipper) this.findViewById(R.id.view_flipper);
		mViewFlipper.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(final View view, final MotionEvent event) {
				detector.onTouchEvent(event);
				return true;
			}
		});

		mAnimationListener = new AnimationListener() {
			public void onAnimationStart(Animation animation) {
				//animation started event
			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				//TODO animation stopped event
			}
		};

		if(language.equals("ru")){
			action_bar.setTitle(getResources().getString(R.string.instructions_ru));
			next.setImageResource(R.drawable.next_button_ru);
			test = testRu;
		}else{
			action_bar.setTitle(getResources().getString(R.string.instructions));
			next.setImageResource(R.drawable.next_button);
			test = testEn;
		}
		
		indicator1.setBackgroundResource(R.drawable.circle_yellow);
		text1.setText(test[0]);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
	
	public void shiningIndicator(int index){
		if(index == 1){
			text2.setText(test[1]);
			indicator1.setBackgroundResource(R.drawable.circle_grey);
			indicator2.setBackgroundResource(R.drawable.circle_yellow);
			indicator3.setBackgroundResource(R.drawable.circle_grey);
		}
		if(index == 2){
			text3.setText(test[2]);
			indicator1.setBackgroundResource(R.drawable.circle_grey);
			indicator2.setBackgroundResource(R.drawable.circle_grey);
			indicator3.setBackgroundResource(R.drawable.circle_yellow);
		}
		if(index == 0){
			text1.setText(test[0]);
			indicator1.setBackgroundResource(R.drawable.circle_yellow);
			indicator2.setBackgroundResource(R.drawable.circle_grey);
			indicator3.setBackgroundResource(R.drawable.circle_grey);
		}
	}
	
	class SwipeGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			try {
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_in));
					mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left_out));
					mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
					mViewFlipper.showNext();
					counter++;
					if(counter >= 3){
						counter = 0;
					}
					shiningIndicator(counter);
					return true;
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right_in));
					mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.right_out));
					mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
					mViewFlipper.showPrevious();
					counter--;
					if(counter < 0){
						counter = 2;
					}
					shiningIndicator(counter);
					return true;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		}
	}
}
