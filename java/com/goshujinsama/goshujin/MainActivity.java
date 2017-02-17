package com.goshujinsama.goshujin;

import android.app.*;
import android.graphics.*;
import android.os.*;
import android.webkit.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class MainActivity extends Activity 
{
	public WebView webView;
	public ProgressBar pbar;
	
	//สร้างหน้าหลัก
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		//webview
		webView = (WebView) findViewById(R.id.mainWebView);
		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl("http://goshujin.tk");
		
		pbar = (ProgressBar) findViewById(R.id.mainProgressBar);
		pbar.setVisibility(webView.GONE);
		
		//ตั้งค่า webview
		WebSettings webSets = webView.getSettings();
		webSets.setAppCacheEnabled(true);
		webSets.setJavaScriptEnabled(true);
		webSets.setBuiltInZoomControls(true);
		webSets.setUseWideViewPort(true);
		webSets.setDisplayZoomControls(false);
		
		//สร้างปุ่มปิดไว้ในตัวหลัก
		//Button exitButton = (Button) findViewById(R.id.exitButton);
		//exitButton.setOnClickListener(new OnClickListener(){
		//	@Override
		//	public void onClick(View v){
		//		finish();
		//		System.exit(0);
		//	}
		//});
	}
	
	//สร้างคลาสของหน้าเว็บ
	public class WebViewClient extends android.webkit.WebViewClient{
		
		//เริ่มเปิดหน้าเว็บ
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon){
			super.onPageStarted(view, url, favicon);
		}
		
		//ทำให้เว็บวิวเข้าหน้าลิ้งค์โดยไม่ต้องเปิดเบาว์เซอร์
		@Override
		public boolean shouldOverrideUrlLoading(WebView webView, String url){
			webView.loadUrl(url);
			return true;
		}

		//เมื่อเพจโหลดหน้าเรียบร้อยแล้ว
		@Override
		public void onPageFinished(WebView view, String url)
		{
			super.onPageFinished(view, url);
			pbar.setVisibility(view.GONE);
		}
	}
	
	//ตั้งค่าปุ่ม Back
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
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
	
}
