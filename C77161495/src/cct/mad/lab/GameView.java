package cct.mad.lab;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;





public class GameView extends SurfaceView implements SurfaceHolder.Callback {
	
	int value = 1;
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	
	
	/* Member (state) fields   */
	private GameLoopThread gameLoopThread;
	private Paint paint; //Reference a paint object 
    /** The drawable to use as the background of the animation canvas */
    private Bitmap mBackgroundImage;
    private Sprite sprite;
    private int hitCount;
    /* For the countdown timer */
    private long  startTime ;			//Timer to count down from
    private final long interval = 1 * 1000; 	//1 sec interval
    private CountDownTimer countDownTimer; 	//Reference to class
    private boolean timerRunning = false;
    private String displayTime; 		//To display time on the screen
    private boolean gameOver;
    private int highScore = 0;
    
    private SoundPool sounds;
    private int hit;
    public static final String PREPS_NAME = "Score";
    private ArrayList<Sprite> spriteArrayList;
    private int i, sprites = 8;

  
	public GameView(Context context) {
		super(context);
		// Focus must be on GameView so that events can be handled.
		this.setFocusable(true);
		// For intercepting events on the surface.
		this.getHolder().addCallback(this);
		
		mBackgroundImage = BitmapFactory.decodeResource(this.getResources(),R.drawable.bg);
		
		sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		hit = sounds.load(context, R.raw.hit, 1);
		
		spriteArrayList= new ArrayList<Sprite>();
		
	}
	
	 /* Called immediately after the surface created */
	public void surfaceCreated(SurfaceHolder holder) {
		ResetGame();//Set up a new game up - could be called by a 'play again option'
		
		
		gameLoopThread = new GameLoopThread(this.getHolder(), this);
		gameLoopThread.running = true;
		gameLoopThread.start();
	
		mBackgroundImage = Bitmap.createScaledBitmap(mBackgroundImage, getWidth(), getHeight(), true);

	}
	//To initialise/reset game
	private void ResetGame(){
		/* Set paint details */
	    paint = new Paint();
		paint.setColor(Color.WHITE); 
		paint.setTextSize(20); 
		sprite = new Sprite(this);
		for(i=0; i<sprites; i++){
			spriteArrayList.add(new Sprite(this));
		}
		//Set timer
		startTime = 20;//Start at 10s to count down
		//Create new object - convert startTime to milliseconds
		countDownTimer=new MyCountDownTimer(startTime*1000,interval);
		countDownTimer.start();//Start it running
		timerRunning = true;
		gameOver = false;

	}
	//This class updates and manages the assets prior to drawing - called from the Thread
	public void update(){
		if ( gameOver == false){
			sprite.update();
			for(i=0; i<sprites; i++){
				spriteArrayList.get(i).update();
		}}
	}
	/**
	 * To draw the game to the screen
	 * This is called from Thread, so synchronization can be done
	 */
	public void doDraw(Canvas canvas) {
		canvas.drawBitmap(mBackgroundImage, 0, 0, null);
		//Draw all the objects on the canvas
		canvas.drawText("Total Hits: "+ hitCount, 5, 25, paint);
		canvas.drawText("Time: " + displayTime,  200, 25, paint);
		if (gameOver == true){
			canvas.drawText("Game Over Total Score: " + hitCount, 200, 50, paint);
			canvas.drawText("Press The Back Key To Go To Menu", 110, 75, paint);
		}
		//sprite.draw(canvas); 
		for(i=0; i<sprites; i++){
			spriteArrayList.get(i).draw(canvas);
		}
		
	}
	
	//To be used if we need to find where screen was touched
	public boolean onTouchEvent(MotionEvent event) {
		
		if (sprite.wasItTouched(event.getX(), event.getY())){
			sounds.play(hit, 1.0f, 1.0f, 0, 0, 2.5f);
		}
		
		if (gameOver == false){
			if (sprite.wasItTouched(event.getX(), event.getY())){
				/* For now, just renew the Sprite */
				sprite = new Sprite(this);
	    	   	hitCount++;   
			}
			for(i=0; i<sprites; i++){
				if (spriteArrayList.get(i).wasItTouched(event.getX(), event.getY())){
					/* For now, just renew the Sprite */
							spriteArrayList.set(i, new Sprite(this));
					    	   	hitCount++;   	
							sounds.play(hit, 1.0f, 1.0f, 0, 0, 2.5f);
					    	}}
		return true;
		}
		return gameOver;
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		gameLoopThread.running = false;
		
		// Shut down the game loop thread cleanly.
		boolean retry = true;
		while(retry) {
			try {
				gameLoopThread.join();
				retry = false;
			} catch (InterruptedException e) {}
		}
	}
	/* Countdown Timer - private class */
	private class MyCountDownTimer extends CountDownTimer {

	  public MyCountDownTimer(long startTime, long interval) {
			super(startTime, interval);
	  }
	  public void onFinish() {
			displayTime = "Games Over!";
			timerRunning = false;
			countDownTimer.cancel();
			gameOver = true;
	  }
	  public void onTick(long millisUntilFinished) {
			displayTime = " " + millisUntilFinished / 1000;
	  }
	}//End of MyCountDownTimer

	public int getHitCount() {
		return hitCount ;
    }

	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}


}

