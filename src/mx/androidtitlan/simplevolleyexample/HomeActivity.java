package mx.androidtitlan.simplevolleyexample;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class HomeActivity extends Activity {
	protected static final String TAG = "SimpleVolleyExample";
	String URL = "http://requestb.in/10mwjs41";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	public void useVolley(View view) {
		RequestQueue requestQueue = Volley.newRequestQueue(this);
		StringRequest stringRequest = new StringRequest(Method.POST, URL,
				new Listener<String>() {

					@Override
					public void onResponse(String response) {
						VolleyLog.v(TAG, response);
						Toast.makeText(HomeActivity.this,
								"Response: " + response, Toast.LENGTH_LONG)
								.show();

					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.e("Volley Error", "" + error.toString());
						Toast.makeText(HomeActivity.this,
								"Response: " + error.toString(),
								Toast.LENGTH_LONG).show();
					}
				}) {
			@Override
			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("gdg", "Androidtitlan");
				params.put("token", "hxf13371337hxf");
				params.put("name", "Enrique Diaz");
				params.put("type", "volley");

				return params;
			}
		};
		// use setRetryPolicy to reconfigure retry policies.
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(15000, 1, 1.0f));
		// add the request object to the queue to be executed.
		requestQueue.add(stringRequest);
	}

	public void useAsyncTask(View view) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("gdg", "Androidtitlan");
		params.put("token", "hxf13371337hxf");
		params.put("name", "Enrique Diaz");
		params.put("type", "asynctask");
		AsyncHttpPost asyncHttpPost = new AsyncHttpPost(params);
		asyncHttpPost.execute(URL);
	}

	public class AsyncHttpPost extends AsyncTask<String, String, String> {
		private HashMap<String, String> mData = null;// post data

		/**
		 * constructor
		 */
		public AsyncHttpPost(HashMap<String, String> data) {
			mData = data;
		}

		/**
		 * background
		 */
		@Override
		protected String doInBackground(String... params) {
			byte[] result = null;
			String str = "";
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(params[0]);// in this case, params[0]
													// is URL
			try {
				// set up post data
				ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
				Iterator<String> it = mData.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					nameValuePair.add(new BasicNameValuePair(key, mData
							.get(key)));
				}

				post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
				HttpResponse response = client.execute(post);
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpURLConnection.HTTP_OK) {
					result = EntityUtils.toByteArray(response.getEntity());
					str = new String(result, "UTF-8");
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (Exception e) {
			}
			return str;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.v(TAG, result);
			Toast.makeText(HomeActivity.this, "Result: " + result,
					Toast.LENGTH_LONG).show();
		}
	}

}
