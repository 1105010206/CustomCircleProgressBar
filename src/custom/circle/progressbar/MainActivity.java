package custom.circle.progressbar;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

	
	CustomCircleProgressBar circleProgressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		initView();
		
	}

	private void initView(){
		circleProgressBar = (CustomCircleProgressBar)findViewById(R.id.first_view);
		
		TestProgressThread thread = new TestProgressThread();
		thread.start();
	}
	
	class TestProgressThread extends Thread{
		
		@Override
		public void run(){
			Log.d("text_custom_attribute", "for----------------");
			for (int i = 0; i < 101; i++) {
				//circleProgressBar.setProgress(i);
				//Log.d("text_custom_attribute", "for----------------"+i);
				Bundle bundle = new Bundle();
				bundle.putInt("progress", i);
				Message msgMessage = Message.obtain();
				msgMessage.setData(bundle);
				mHandler.sendMessage(msgMessage);
				
				try {
					Thread.sleep(200);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
			
			
		}
		
	}
	
	Handler mHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg){
			
			Bundle bundle = msg.getData();
			circleProgressBar.setProgress(bundle.getInt("progress"));
			Log.d("text_custom_attribute", "for----------------"+bundle.getInt("progress"));
			
		}
		
	};
}
