package com.goshujinsama.goshujin;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.app.*;
import android.widget.*;
import android.widget.PopupMenu.*;
import android.graphics.*;
import android.os.*;
import android.webkit.*;
import android.view.*;
import android.view.View.*;
import android.content.*;
import android.net.*;
import android.view.inputmethod.*;
import android.provider.*;
import java.util.*;
import java.util.zip.*;
import junit.runner.*;

public class MainActivity extends AppCompatActivity
{
	Toolbar mainToolbar;
	Menu menu;
	ImageButton indexButton;
	ImageButton searchButton;
	WebView webView;
	WebSettings webSets;
	TextView textView;
	RelativeLayout buttonLayout;
	int page;
	String topicHead;
	String currentUrl;
	ImageButton nextBtn;
	ImageButton backBtn;
	ImageButton hTopicBtn;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //run void showToolbarSetting
        showToolbarSetting();
        
        webView = (WebView) findViewById(R.id.mainWebView);
        startUrl("http://goshujin.tk");
        
        webViewSettings();
        
        buttonLayout = (RelativeLayout) findViewById(R.id.buttonLayout);
		buttonLayout.setVisibility(View.INVISIBLE);
		
		//HOME BUTTON
		indexButton = (ImageButton) findViewById(R.id.indexButton);
		indexButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				startUrl("http://goshujin.tk");
			}
		});
		
		//SEARCH BUTTON
		searchButton = (ImageButton) findViewById(R.id.searchButton);
		searchButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				startUrl("http://goshujin.tk/index.php?action=search");
			}
		});

    }
    
    /*--- Start void showToolbarSetting ---*/
    public void showToolbarSetting(){
		mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
		mainToolbar.setTitle("");
		setSupportActionBar(mainToolbar);
	}
	/*--- End void Show Toolbar Setting ---*/
	
	/*--- Start boolean onCreateOptionsMenu ---*/ 
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}
	/*--- End boolean onCreateOptionsMenu ---*/
	
	/*--- Start boolean onPrepareOptionsMenu ---*/ 
	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		return super.onPrepareOptionsMenu(menu);
	}
	/*--- End boolean onPrepareOptionsMenu ---*/
	
	/*--- Start Sub Menu Option ---*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		//Setting MenuItemClick
		switch (item.getItemId()) {
			case R.id.exitMenu:
				finish();
				return true;
			case R.id.refreshMenu:
				webView.reload();
				return true;
			case R.id.roomMenu:
				onRoomMenuOptions();
				return true;
			case R.id.inboxMenu:
				startUrl("http://goshujin.tk/index.php?action=pm");
				return true;
			case R.id.shareMenu:
				String url = webView.getUrl();
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, url);
				sendIntent.setType("text/plain");
				startActivity(sendIntent); 
				return true;
			case R.id.forwardMenu:
				if(webView.canGoForward()){
					webView.goForward();
				} else {
					Toast.makeText(getApplicationContext(),"ยังไม่ไปไหนเลย จะกดทำไม", Toast.LENGTH_LONG).show();
				}
				return true;
			case R.id.backMenu:
				if(webView.canGoBack()){
					webView.goBack();
				} else {
					Toast.makeText(getApplicationContext(),"ยังไม่ไปไหนเลย จะกดทำไม", Toast.LENGTH_LONG).show();
				}
				return true;
			
			case R.id.bookmarkMenu:
				Toast.makeText(getApplicationContext(),"ฟังก์ชั่นในอนาคต ตอนนี้ยังไม่มี", Toast.LENGTH_LONG).show();
				return true;
			/*
			case R.id.topicMenu:
				onTopicMenuOptions();
				return true;
			*/
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	/*--- End Sub Menu Option ---*/
	
	/*--- Start Room Menu Options ---*/
	public void onRoomMenuOptions(){
		//เริ่มโค้ดป๊อบอัพเลือกห้อง
		PopupMenu popup = new PopupMenu(MainActivity.this, mainToolbar);
		popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
		//ตั้งค่าป๊อบอัพเลือกห้อง
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
				public boolean onMenuItemClick(MenuItem item){
					//ตัวเลือกของแต่ละห้อง
					switch (item.getItemId()){
						case R.id.welcomeRoom:
							startUrl("http://goshujin.tk/index.php?board=2.0");
							return true;
						case R.id.translatorRoom:
							startUrl("http://goshujin.tk/index.php?board=1.0");
							return true;
						case R.id.writerRoom:
							startUrl("http://goshujin.tk/index.php?board=3.0");
							return true;
						case R.id.mangaRoom:
							startUrl("http://goshujin.tk/index.php?board=8.0");
							return true;
						case R.id.recommendRoom:
							startUrl("http://goshujin.tk/index.php?board=5.0");
							return true;
						case R.id.hobbyRoom:
							startUrl("http://goshujin.tk/index.php?board=4.0");
							return true;
						case R.id.playRoom:
							startUrl("http://goshujin.tk/index.php?board=9.0");
							return true;
						case R.id.newsRoom:
							startUrl("http://goshujin.tk/index.php?board=10.0");
							return true;
					}
					return true;
				}
			});
		popup.show();
		//จบโค้ดป๊อบอัพเลือกกระทู้
	}
	/*--- End Room Menu Options ---*/
	
	/*--- Start void startUrl ---*/
	public void startUrl(String url){
		webView.loadUrl(url);
		webView.setWebViewClient(new WebViewClient(){
			
			ProgressDialog progressDialog;
			
			@Override
			public void onLoadResource(WebView view, String url){
				
				if (progressDialog == null) {
					progressDialog = new ProgressDialog(MainActivity.this);
					progressDialog.setMessage("กำลังโหลด...");
					progressDialog.show();
				}				
				
			}
			
			public boolean shouldOverrideUrlLoading(WebView view, String url){
				
				view.loadUrl(url);				
				return false; 
				
			}

			public void onPageFinished(WebView view, String url){

				try {
					if(progressDialog.isShowing()){
						progressDialog.dismiss();
					}
				} catch(Exception exception){
					exception.printStackTrace();

				}
				
				/*--- Start Topic Button Layout ---*/
				
				currentUrl = webView.getUrl();
				
				textView = (TextView) findViewById(R.id.textView);
				
				if(currentUrl.contains("topic=")){
					
					nextBtn = (ImageButton) findViewById(R.id.nextBtn);
					backBtn = (ImageButton) findViewById(R.id.BackBtn);
					hTopicBtn = (ImageButton) findViewById(R.id.hTopicBtn);
					
					buttonLayout.setVisibility(View.VISIBLE);
					
					int i = currentUrl.lastIndexOf(".");
					String[] split = {currentUrl.substring(0, i++),currentUrl.substring(i)};
					
					topicHead = split[0];
					
					//เช็คว่าเป็นจำนวนเต็มหรือไม่
					if(split[1].matches("-?\\d+(\\.\\d+)?")){
						
						page = Integer.parseInt(split[1]);
						
						int pageIndex = (page / 20)+1;
						if(pageIndex==1){
							textView.setText("หน้าแรก");
						} else {
							textView.setText("หน้าที่ "+pageIndex);
						}
						
						nextBtn.setOnClickListener(new View.OnClickListener(){
							@Override
							public void onClick(View v){
								page = page + 20;
								webView.loadUrl(topicHead+"."+page);
							}
						});
						backBtn.setOnClickListener(new View.OnClickListener(){
							@Override
							public void onClick(View v){
								page = (page > 0 ? page-20 : 0);
								webView.loadUrl(topicHead+"."+page);
							}
						});
						
						//กรณีที่ไม่ใช่ตัวเลขแต่เป็นพวกข้อความ
						} else {
						//Toast.makeText(getApplicationContext(),"ไม่สามารถใช้ฟังก์ชั่นเปลี่ยนหน้าได้ กรุณากดที่หน้าใดหน้าหนึ่งก่อน หรือกดปุ่มหน้าหลัก", Toast.LENGTH_SHORT).show();
						}
					
					//ปุ่มหน้าหลักกระทู้
					hTopicBtn.setOnClickListener(new View.OnClickListener(){
						@Override
						public void onClick(View v){
							page = 0;
							webView.loadUrl(topicHead+"."+page);
							textView.setText("หน้าแรก");
						}
					});
						
					//กรณีไม่ใช่ลิ้งค์กระทู้
					} else {
					textView.setText("");
					buttonLayout.setVisibility(View.INVISIBLE);
				}
				
				/*--- End Topic Button Layout ---*/
	
			}

		});
	}
	/*--- End void startUrl ---*/
	
	/*--- Start void webViewSettings ---*/
	public void webViewSettings(){
		//webView.setWebChromeClient(new WebChromeClient());
		//ตั้งค่า webview
		webSets = webView.getSettings();
		//ติดตั้งแอพแคช
		webSets.setAppCacheEnabled(true);
		//ติดตั้งจาวาสคิปส์
		webSets.setJavaScriptEnabled(true);
		//ติดตั้งการซูม
		webSets.setBuiltInZoomControls(true);
		//เปิดหน้าด้วยขนาดเว็บจริง
		webSets.setLoadWithOverviewMode(true);
		webSets.setUseWideViewPort(true);
		//ปิดหน้าต่างคอนโทรล
		webSets.setDisplayZoomControls(false);
		//ปิด scrollbar
		webView.setHorizontalScrollBarEnabled(false);
		webView.setVerticalScrollBarEnabled(false);
	}
	/*--- Start void webViewSettings ---*/
	
	/*--- Start Hard Back Button Setting ---*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			switch(keyCode){
				case KeyEvent.KEYCODE_BACK:
					if(webView.canGoBack()){
						webView.goBack();
					} else {
						finish();
					}
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	/*--- End Hard Back Button Setting ---*/
}
