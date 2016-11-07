package cct.mad.lab;

import android.os.Bundle;
import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.*;
import android.media.MediaPlayer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;
import android.view.View;
import android.view.View.OnClickListener;




public class MainMenu extends Activity {
	public static final String PREPS_NAME = "Score";
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	int high;
	int gameScore;
	MediaPlayer sound;
	Button button; 
	private static final int SCORE_REQUEST_CODE = 1;// The request code for the intent
	GameActivity gameActivity;
	GameView gameView;
	TextView tvScore;
	String score;
	Intent gameIntent;
	TextView highScore;
	Bitmap mBackgroundImage;
	static int xSpeed;
	static int ySpeed;

		
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		settings = getSharedPreferences(PREPS_NAME, 0);
		setContentView(R.layout.game_start);
		tvScore = (TextView)findViewById(R.id.tvOutput);
		highScore =(TextView)findViewById(R.id.textView3);
		high = settings.getInt("highscore", 0);
		gameScore = 0;
		highScore.setText(Integer.toString(high));
		sound = MediaPlayer.create(MainMenu.this, R.raw.music);
		sound.setLooping(true);
		sound.start();
		addListenerOnButton();
		mBackgroundImage = BitmapFactory.decodeResource(this.getResources(),R.drawable.background);
	}
	
	public void onCreate1(Bundle savedInstanceState) {

	}
	
	
	private void addListenerOnButton() {
		final Context context=this;
		button = (Button) findViewById(R.id.button3);
		
		button.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v) {           
				Intent intentMain = new Intent(MainMenu.this , 
			    Instruct.class);
				MainMenu.this.startActivity(intentMain);
				Log.i("Content "," Main layout ");
			}
		});
	}
	
	public void startGame(View v){
		gameIntent = new Intent(this, GameActivity.class);
	    startActivityForResult(gameIntent, SCORE_REQUEST_CODE );
	}
    /* Create Options Menu */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	// Respond to item selected on OPTIONS MENU
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		//put data in Intent
		case R.id.easy:
			xSpeed = 5;
			ySpeed = 3;
			Toast.makeText(this, "Easy Selected!", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.medium:
			xSpeed = 10;
			ySpeed = 7;
			Toast.makeText(this, "Medium Selected!", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.hard:
			xSpeed = 20;
			ySpeed = 17;
			Toast.makeText(this, "Hard Selected!", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent retIntent) {
	    // Check which request we're responding to
	    if (requestCode == SCORE_REQUEST_CODE) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	        	if (retIntent.hasExtra("GAME_SCORE")) {
					gameScore = retIntent.getExtras().getInt("GAME_SCORE");
					tvScore.setText(gameScore+"");
					if(gameScore > high) {
						editor = settings.edit();
						editor.putInt("highscore", gameScore);
						editor.commit();
						Toast.makeText(MainMenu.this,"Congratulations! You Have Bet The Highscore " + high + " New Highscore " + gameScore + "\n", Toast.LENGTH_LONG).show();
						high = settings.getInt("highscore", 0);  
						highScore.setText(Integer.toString(high));
						Toast.makeText(MainMenu.this,"High Score " + high, Toast.LENGTH_LONG).show();
					}
	        	}
	        }	
	    }
	}
	

	
	public void clearPreferences(View V) {
		  editor = settings.edit();// Create a new editor
		  editor.clear();
		  editor.commit();
		  highScore.setText("0");
		  Toast.makeText( MainMenu.this,"High Scoree Has Been Reset!",Toast.LENGTH_LONG).show();
	}
	
	public static int setXSpeed(){
		return xSpeed;
	}
	public static int setYSpeed(){
		return ySpeed;
	}
	
	
}
