package edu.wustl.cse467.sleepingbeauty;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.TextView;

public class ConfigActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		
		EditText editText = (EditText) findViewById(R.id.urlText);
		editText.setText(MainActivity.URL);
		editText.setOnEditorActionListener(new UrlListener(editText, (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)));
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.config, menu);
		return true;
	}

}

class UrlListener implements EditText.OnEditorActionListener {

	EditText edit;
	InputMethodManager inm;
	
	public UrlListener(EditText edit, InputMethodManager inm) {
		this.edit = edit;
		this.inm = inm;
	}
	
	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE) {
			 
			String url = edit.getText().toString();
			if (URLUtil.isValidUrl(url)) {
				MainActivity.URL = url; 
			}
			inm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
			
			return true;             
		
		}
	
		return false;
	}
	
}
