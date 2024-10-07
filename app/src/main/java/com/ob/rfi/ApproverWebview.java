package com.ob.rfi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ob.rfi.db.RfiDatabase;
import com.ob.rfi.service.Webservice;

public class ApproverWebview extends CustomTitle {

    private WebView webView;
    private String loadUrl;
    private String method;
    private String[] param;
    private String[] value;
    private RfiDatabase db;
    private String responseData;
    private final String REPORT_URL = "https://www.cqra-rfi.com/";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        if (getIntent().getExtras() != null) {
            loadUrl = getIntent().getExtras().getString("rfiNumber", "");
        }
        reportData();
    }

    //changed by sayali
    //1-3-20224
    public void reportData() {
        method = "getHtmlReport";
        param = new String[]{"rfiId"};
        value = new String[]{db.selectedrfiId};
        callService();
    }

    protected void callService() {
        Webservice service = new Webservice(ApproverWebview.this,
                network_available, "Loading.. Please wait..", method, param,
                value);
        service.setdownloadListener(new Webservice.downloadListener() {
            @Override
            public void dataDownloadedSuccessfully(String data) {
                // if(requestid == 1)

                responseData = data;
                retriveUrl(data);
                System.out.println("success data");
                //saveCoverageData(data);

            }

            @Override
            public void dataDownloadFailed() {
                displayDialog("Error", "Problem in connection.");
            }

            @Override
            public void netNotAvailable() {
                displayDialog("Error", "No network connection.");
            }
        });
        service.execute("");
    }

    private void retriveUrl(String data) {
        String[] urlList = data.split("\\\\");
        if (urlList != null && urlList.length > 0) {
            loadUrl = REPORT_URL + urlList[2] + "\\" + urlList[4];
        }

        setWebviewClient();
    }

    protected void displayDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);

        if (message.equals("Problem in connection.")) {
            alertDialog.setButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            alertDialog.setButton2("Try again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            // retry();
                        }
                    });
        } else {
            alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        alertDialog.show();
    }

    public void setWebviewClient() {
        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        if (loadUrl != null && !loadUrl.isEmpty()) {
            webView.loadUrl(loadUrl);
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
