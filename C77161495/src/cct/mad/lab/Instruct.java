package cct.mad.lab;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Instruct extends Activity {
	Button button;
	TextView help;
	
	public void onCreate(Bundle savedInstancestate){
			super.onCreate(savedInstancestate);
			setContentView(R.layout.help);
			help =(TextView) findViewById (R.id.textView1);
			help.setText("\n How To Play:\n \n \n1.Press start game to begin.\n \n2."
					+ "Tap on the bird as many times as you can before the timer runs out.\n \n3."
					+ "Try to beat the high score.\n \n4."
					+ "Press the back key when game is over .\n \n5."
					+ "press clear high score button To reset highscore, . \n \n."
					+ "Enjoy The Game");
			}
}
