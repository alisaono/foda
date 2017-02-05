package cz.vancura.searchapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		 final EditText eText;
		 Button btn;
		 
		  Log.d("search", "**** APP START");

			eText = (EditText) findViewById(R.id.edittext);
		    btn = (Button) findViewById(R.id.button);
			//final TextView result = (TextView) findViewById(R.id.textView1);
			final ImageView iv = (ImageView) findViewById(R.id.imageview);
		      
		    btn.setOnClickListener(new OnClickListener() {
		         public void onClick(View v) {
		          
		        	final String str = eText.getText().toString();
		            Log.d("search", "Searching for :" + str);
		            //result.setText("Searching for :" + str);
		           
		            
					Thread thread = new Thread(new Runnable()
					{
					    @Override
					    public void run() 
					    {
					  			
								try {
										
									
										// looking for
									 	String strNoSpaces = str.replace(" ", "+");
									
										// Your API key
									 	String key="AIzaSyAa4WTeDyTvZwR049Ic-rm2s_FDSHH3I8c";
									 	
									 	// Your Search Engine ID
										String cx = "002553951042914972765:ipfiihj_g6m";
										
										String url2 = "https://www.googleapis.com/customsearch/v1?q=" + strNoSpaces + "&key=" + key + "&cx=" + cx + "&alt=json"; 	
										Log.d("search", "Url = "+  url2);
										String result2 = httpGet(url2);
										/*result.setText("1");
										//JSONObject obj =new JSONObject("2222");
									    JSONArray array = new JSONArray(result2);
										result.setText("2");

										JSONObject obj =array.getJSONObject(0);
										result.setText("3");

										String snippet=obj.getString("snippet");
										result.setText("snippet");*/
										String result3=result2.substring(14,result2.length()-1);
										iv.setImageBitmap(getBitmapFromURL(result3));



								}
							        catch(Exception e) {
							            System.out.println("Error1 " + e.getMessage());
							        }
									
								 }

						public Bitmap getBitmapFromURL(String src) {
							try {
								Log.e("src",src);
								URL url = new URL(src);
								HttpURLConnection connection = (HttpURLConnection) url.openConnection();
								connection.setDoInput(true);
								connection.connect();
								InputStream input = connection.getInputStream();
								Bitmap myBitmap = BitmapFactory.decodeStream(input);
								Log.e("Bitmap","returned");
								return myBitmap;
							} catch (IOException e) {
								e.printStackTrace();
								Log.e("Exception",e.getMessage());
								return null;
							}
						}
						private String httpGet(String urlStr) throws IOException {
							
							URL url = new URL(urlStr);
							
						    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					        
					        if(conn.getResponseCode() != 200) {
					            throw new IOException(conn.getResponseMessage());
					        }
					         
					    	Log.d("search", "Connection status = " + conn.getResponseMessage());
					    	
					        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					        StringBuilder sb = new StringBuilder();
					        String line;
					        String output="";
					        while((line = rd.readLine()) != null) {
								String temp=rd.readLine();
					        	Log.d("search", "Line =" + temp);
								/*int i=temp.indexOf("snippet");
								if (i!=-1){
									des=temp.substring(13)+";";
								}*/
								int i=temp.indexOf("src");
								if (i!=-1){
									int len=temp.length();
									return temp;
								}
					      		sb.append(line+"\n");

					            
					        }
					        rd.close();
					        return output;
					        /*conn.disconnect();
					        return sb.toString();*/
					    }
					});

					thread.start(); 
			        
		         }
		      });
		}
		
	}
