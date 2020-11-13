package com.cds_jo.GalaxySalesApp.TspPrinter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.cds_jo.GalaxySalesApp.TspPrinter.RasterDocument.RasSpeed;
import com.cds_jo.GalaxySalesApp.TspPrinter.RasterDocument.RasPageEndMode;
import com.cds_jo.GalaxySalesApp.TspPrinter.RasterDocument.RasTopMargin;

import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;
import java.util.Locale;

import static com.cds_jo.GalaxySalesApp.TspPrinter.PrinterFunctions.RasterCommand.Standard;

public class SampleReciptActivity extends Activity {

	private Context me = this;
	private Intent intent;
	private String strPrintArea = "";
	String portName,portSettings;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.language);
		  portName ="BT:TSP100-L0528";// PrinterTypeActivity.getPortName();
		  portSettings = "";
		intent = getIntent();

		if (intent.getIntExtra("PRINTERTYPE", 1) == 2) { // RasterMode
			TextView titleSBCSText = (TextView) findViewById(R.id.text_SBCS);
			TextView titleDBCSText = (TextView) findViewById(R.id.text_DBSC);
			TextView titleDBCSDescriptionText = (TextView) findViewById(R.id.text_DBSC_Description);
			titleSBCSText.setVisibility(View.GONE);
			titleDBCSText.setVisibility(View.GONE);
			titleDBCSDescriptionText.setVisibility(View.GONE);

			setTitle("StarIOSDK - Raster Mode Samples");
		} else if ((intent.getIntExtra("PRINTERTYPE", 1) == 1)) { // Thermal LineMode
			TextView rasterDescriptionText = (TextView) findViewById(R.id.text_Raster_Description);
			rasterDescriptionText.setVisibility(View.GONE);



			setTitle("StarIOSDK - Line Mode Samples");
		} else if (intent.getIntExtra("PRINTERTYPE", 1) == 0) { // DotPrinter
			TextView rasterDescriptionText = (TextView) findViewById(R.id.text_Raster_Description);
			rasterDescriptionText.setVisibility(View.GONE);

			setTitle("StarIOSDK - Impact Dot Matrix Printer Samples");
		} else if (intent.getIntExtra("PRINTERTYPE", 1) == 3) { // Portable Printer
			TextView rasterDescriptionText = (TextView) findViewById(R.id.text_Raster_Description);
			rasterDescriptionText.setVisibility(View.GONE);

			TextView DBCSDescriptionText = (TextView) findViewById(R.id.text_DBSC_Description);
			DBCSDescriptionText.setText("These samples will require the correct DBCS character set to be loaded.");

			Button RussianButton = (Button) findViewById(R.id.button_Russian);
			RussianButton.setVisibility(View.GONE);
			
			Button SimplifiedChineseButton = (Button) findViewById(R.id.button_SimplifiedChinese);
			SimplifiedChineseButton.setVisibility(View.GONE);

			setTitle("StarIOSDK - Portable Printer Samples");
		} else if ((intent.getIntExtra("PRINTERTYPE", 1) == 4)) { // Thermal LineMode UTF8
		    TextView rasterDescriptionText = (TextView) findViewById(R.id.text_Raster_Description);
		    rasterDescriptionText.setVisibility(View.GONE);
		    
			TextView titleSBCSText = (TextView) findViewById(R.id.text_SBCS);
			titleSBCSText.setText("These samples will require the correct UTF-8 character set to be loaded and a memory switch change to print correctly.\nPlease contact your local support to discuss.");
			//titleSBCSText.setVisibility(View.GONE);
			
			TextView titleDBCSText = (TextView) findViewById(R.id.text_DBSC);
			titleDBCSText.setVisibility(View.GONE);
			
			TextView titleDBCSDescriptionText = (TextView) findViewById(R.id.text_DBSC_Description);
			titleDBCSDescriptionText.setVisibility(View.GONE);


		    setTitle("StarIOSDK - Line Mode Samples (UTF8)");
		}
	}

	public void English(View view) {

		if (!checkClick.isClickEvent())
			return;

	//	PrinterFunctions.PrintSampleReceipt(me, portName,portSettings, "Line", getResources(), strPrintArea, null);
		PrinterFunctions.PrintSampleReceipt(me, portName, portSettings, "Raster", getResources(), strPrintArea, Standard);







	}




}