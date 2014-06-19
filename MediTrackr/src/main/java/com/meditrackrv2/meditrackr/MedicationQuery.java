package com.meditrackrv2.meditrackr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.meditrackrv2.meditrackr.services.AbstractService;
import com.meditrackrv2.meditrackr.services.ServiceListener;
import com.meditrackrv2.meditrackr.services.UPCSearchService;


public class MedicationQuery extends Activity implements ServiceListener, View.OnClickListener {

    private static final int RESULT_SETTINGS = 1;

    private Thread thread;
    private JSONObject upcSearch;

    private TextView tvResult;
    private TextView tvDescription;
    private Button btnScan;
    private WebView wvSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_query);

        tvResult = (TextView)findViewById(R.id.resultTitle);
        tvDescription = (TextView)findViewById(R.id.resultDescription);
        btnScan = (Button)findViewById(R.id.btnScan);
        wvSearch = (WebView)findViewById(R.id.wvGoogle);

        btnScan.setOnClickListener(this);
    }

    public void onClick(View v){
        if(v.getId()==R.id.btnScan){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if(scanResult != null){
            // Result
            String upcQuery = scanResult.getContents();
            doSearch(upcQuery);
        } else {
            // No result
            tvResult.setText("No Results");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.medication_query, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void doSearch(String upcQuery){

        tvDescription.setText("");
        tvResult.setText("");

        UPCSearchService service = new UPCSearchService(upcQuery);
        service.addListeners(this);
        thread = new Thread(service);
        thread.start();

    }

    public void ServiceComplete(AbstractService service){

        if(!service.hasError()){
            upcSearch = ((UPCSearchService)service).getResult();

            try {
                tvResult.setText(upcSearch.getString("itemname"));
                tvDescription.setText(upcSearch.getString("description"));
                String url = "http://www.drugs.com/search.php?sources%5B%5D=uk&searchterm=" + upcSearch.getString("itemname");
                Log.i("URL", url);
                wvSearch.loadUrl(url);
            } catch (JSONException ex) {}

        } else {
            tvResult.setText("No Results");
        }

    }

}
