package com.ob.rfi.service;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ob.rfi.BuildConfig;
import com.ob.rfi.R;

public class Webservice extends AsyncTask<String, Integer, String> {
 
	/**Original
	 * parcentage userd for progress bar is Show horizontal or vertical.
	 */ 
	private boolean parcentage;
	private Context context;
	private boolean network;


	private String message;
	private String methodName;  
	private String[] paramNames;
	private String[] paramValues;

//	private String serviceNameSpace = "http://192.169.158.217:82/DemoService";
//	private String URL ="http://192.169.158.217:82/DemoService/RFIService.asmx";

//	private String serviceNameSpace = "192.169.158.217:82/YashOneHinjawadiService";
//	private String URL ="192.169.158.217:82/YashOneHinjawadiService/RFIService.asmx";

//	private String serviceNameSpace = "http://192.169.158.217:82/YashwinOrizzonteService";
//	private String URL ="http://192.169.158.217:82/YashwinOrizzonteService/RFIService.asmx";

//	private String serviceNameSpace = "192.169.158.217:82/YashOneHinjawadiService";
//	private String URL ="192.169.158.217:82/YashOneHinjawadiService/RFIService.asmx";

//	private String serviceNameSpace = "http://192.169.158.217:82/YashwinService";
//	private String URL ="http://192.169.158.217:82/YashwinService/RFIService.asmx";

//	private String serviceNameSpace = "http://192.169.158.217:82/YashOneHinjawadi1Service";
//	private String URL ="http://192.169.158.217:82/YashOneHinjawadi1Service/RFIService.asmx";

	//private String serviceNameSpace = "http://92.204.136.64:82/MyHomeTridasaService";
	//private String URL ="http://92.204.136.64:82/MyHomeTridasaService/RFIService.asmx";

	private String serviceNameSpace = "http://92.204.136.64:82/RFIDemoService";
	private String URL ="http://92.204.136.64:82/RFIDemoService/RFIService.asmx";
//	private String serviceNameSpace = "http://92.204.136.64:82/RFIMyHomeTridasaDemoService";
//	private String URL ="http://92.204.136.64:82/RFIMyHomeTridasaDemoService/RFIService.asmx";

	//private String serviceNameSpace = "http://92.204.136.64:82/YashOneHinjawadiService";
	//private String URL ="http://92.204.136.64:82/YashOneHinjawadiService/RFIService.asmx";

	//private String serviceNameSpace = "http://92.204.136.64:82/EminenceService";
	//private String URL ="http://92.204.136.64:82/EminenceService/RFIService.asmx";


	//http://92.204.136.64:82/FusionTowerPhase1Service


//	private String serviceNameSpace = "http://192.169.158.217:82/Vantage21Service";
//	private String URL ="http://192.169.158.217:82/Vantage21Service/RFIService.asmx";

	//private String serviceNameSpace = "http://192.169.158.217:82/YashOneWakadCentralService";
	//private String URL ="http://192.169.158.217:82/YashOneWakadCentralService/RFIService.asmx";

//	private String serviceNameSpace = "http://192.169.158.217:82/ShriramGrandOneService";
//	private String URL ="http://192.169.158.217:82/ShriramGrandOneService/RFIService.asmx";

//	private String serviceNameSpace = "http://192.169.158.217:82/YashwinSukhniwasService";
//	private String URL ="http://192.169.158.217:82/YashwinSukhniwasService/RFIService.asmx";

//	private String serviceNameSpace = "http://192.169.158.217:82/WAMILService";
//	private String URL ="http://192.169.158.217:82/WAMILService/RFIService.asmx";

//	private String serviceNameSpace = "http://192.169.158.217:82/TILYarnDivisionRWService";
//	private String URL ="http://192.169.158.217:82/TILYarnDivisionRWService/RFIService.asmx";

//	private String serviceNameSpace = "http://192.169.158.217:82/MyHomeTridasaService";
//	private String URL ="http://192.169.158.217:82/MyHomeTridasaService/rfiservice.asmx";

//	private String serviceNameSpace = "http://192.169.158.217:82/ParadiseOneService";
//	private String URL ="http://192.169.158.217:82/ParadiseOneService/RFIService.asmx";

//	private String serviceNameSpace = "http://192.169.158.217:82/HappyCityVaraleService";
//	private String URL ="http://192.169.158.217:82/HappyCityVaraleService/rfiservice.asmx";

//	private String serviceNameSpace = "http://192.169.158.217:82/VTPService";
//	private String URL ="http://192.169.158.217:82/VTPService/rfiservice.asmx";

//	private String serviceNameSpace = "http://192.169.158.217:82/YashwinEncoreService";
//	private String URL ="http://192.169.158.217:82/YashwinEncoreService/rfiservice.asmx";

//    private String serviceNameSpace = "http://192.169.158.217:82/TV9StudioService";
//    private String URL ="http://192.169.158.217:82/TV9StudioService/rfiservice.asmx";

//	private String serviceNameSpace = "http://192.169.158.217:82/LeonaraTownSquareService";
//	private String URL ="http://192.169.158.217:82/LeonaraTownSquareService/RFIService.asmx";

//	private String serviceNameSpace = "http://192.169.158.217:82/BelAirAlpineService";
//	private String URL ="http://192.169.158.217:82/BelAirAlpineService/rfiservice.asmx";

//	private String serviceNameSpace = "http://192.169.158.217:82/SpaceStation1Service";
//	private String URL ="http://192.169.158.217:82/SpaceStation1Service/rfiservice.asmx";

//	private String serviceNameSpace = "http://192.169.158.217:82/SkylandService";
//	private String URL ="http://192.169.158.217:82/SkylandService/rfiservice.asmx";

    SoapObject soapRequest = null;
	Object result = null;  
	String resultString = ""; 
	private int timeout = 60 * 20000;
	ProgressDialog progressDialog;
	downloadListener dataDownloadListener;
	private HttpTransportSE androidHttpTransport;

	/**   
	 * @param ctx
	 * @param networkAvailable
	 * @param message 
	 * @param methodName
	 * @param paramNames
	 * @param paramValues
	 */
	public 	Webservice(Context ctx, boolean networkAvailable, String message,
			String methodName, String[] paramNames, String[] paramValues) {
		this.context = ctx;
		this.network = networkAvailable;
		this.message = message;
		this.methodName = methodName;
		this.paramNames = paramNames;
		this.paramValues = paramValues;
		this.parcentage = false;
	}

	
	public boolean isParcentage() {
		return parcentage;
	}

	public void setParcentage(boolean parcentage) {
		this.parcentage = parcentage;
	}

	@Override
	synchronized protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = new ProgressDialog(context, R.style.MyProgressBarStyle);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(message);

		if (parcentage) {
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setProgress(0);
		} else {
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		}
		progressDialog.show();
	}

	synchronized public void setdownloadListener(
			downloadListener dataDownloadListener) {
		this.dataDownloadListener = dataDownloadListener;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		progressDialog.setProgress(values[0]);
	}

	@Override
	synchronized protected String doInBackground(String... params) {
		if (network) {

			try {
				publishProgress(10);
				serviceNameSpace = BuildConfig.Service_NameSpace;
				URL =BuildConfig.Service_URL;
				 androidHttpTransport = new HttpTransportSE(URL,
						timeout);
				soapRequest = new SoapObject(serviceNameSpace, methodName);

				if (paramNames != null && paramValues != null) {

					for (int index = 0; index < paramValues.length; index++) {
						soapRequest.addProperty(paramNames[index],
								paramValues[index]);
					}
					/*soapRequest.addProperty("userName","sandeep");
					soapRequest.addProperty("password","test123");*/
					//soapRequest.addProperty("userID","3");
				}
				
				
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.setOutputSoapObject(soapRequest);
				envelope.dotNet=true;
				/*envelope.env="http://schemas.xmlsoap.org/soap/envelope/";*/
				Log.e("Request", soapRequest.toString());
				publishProgress(30);

				androidHttpTransport.call(serviceNameSpace+"/"+methodName, envelope);
				//androidHttpTransport.call(serviceNameSpace+methodName, envelope);
				
				result = envelope.getResponse();
				resultString = result.toString();
				Log.e("Response", resultString);
				publishProgress(70);// 

				// Log.d("SOAP",androidHttpTransport.requestDump);

			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				resultString = "";
				System.out.println("1111111============="+e.getMessage());
				
			} catch (SocketException e) {
				e.printStackTrace();
				System.out.println("22222222============="+e.getMessage());
				resultString = "";
			} catch (Exception e) {
				System.out.println("33333333============="+e.getMessage());
				e.printStackTrace();
				resultString = "";
			}
		} else {
			resultString = "noNetwork";
		}
		Log.e(">>>Result", resultString);
		return resultString;
	}

	@Override
	synchronized protected void onPostExecute(String result) {
		progressDialog.setProgress(100);
		if(androidHttpTransport!=null)
		{
		try {
			androidHttpTransport.getConnection().disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		/**
		 * this for activity get switched and context of activity still in used
		 * in background task that application get crashed that time this
		 * following try block solve problem.
		 */
		try {
			progressDialog.dismiss();
			progressDialog = null;
			
		} catch (Exception e) {
			// nothing 
		}

		if (result.equals("noNetwork"))
			dataDownloadListener.netNotAvailable();
		
		else if ((result.length() == 0) || result.equalsIgnoreCase("problem in connection"))
			dataDownloadListener.dataDownloadFailed();
		else
			dataDownloadListener.dataDownloadedSuccessfully(result);
	}

	public static interface downloadListener {
		void dataDownloadedSuccessfully(String data);

		void dataDownloadFailed();

		void netNotAvailable();
	}
}