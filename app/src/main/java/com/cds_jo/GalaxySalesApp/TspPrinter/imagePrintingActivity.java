package com.cds_jo.GalaxySalesApp.TspPrinter;

import android.app.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import android.widget.ImageView;
import com.cds_jo.GalaxySalesApp.TspPrinter.RasterDocument.RasSpeed;
import com.cds_jo.GalaxySalesApp.TspPrinter.RasterDocument.RasPageEndMode;
import com.cds_jo.GalaxySalesApp.TspPrinter.RasterDocument.RasTopMargin;

import com.cds_jo.GalaxySalesApp.R;

import java.util.ArrayList;
import java.util.Locale;

public class imagePrintingActivity extends Activity implements OnItemSelectedListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.printingimage);

	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		ImageView imageView_Image = (ImageView) findViewById(R.id.imageView_Image);
		imageView_Image.setImageResource(R.drawable.a);
	}

	public void onNothingSelected(AdapterView<?> arg0) {

	}

	public void PrintText(View view) {
		if (!checkClick.isClickEvent()) {
			return;
		}

		Bitmap myBitmap = null;
		myBitmap= BitmapFactory.decodeFile("//sdcard//z1.jpg");
		int source = R.drawable.add_check;
		//getResources();


		int paperWidth = 576;
		  paperWidth = 384; // 2inch (384 dot)
		  paperWidth = 832; // 4inch (832 dot)
		  paperWidth = 576; // 3inch (576 dot)

		String portName ="BT:TSP100-L0528";// PrinterTypeActivity.getPortName();
		String portSettings = "";
			PrinterFunctions.RasterCommand rasterType = PrinterFunctions.RasterCommand.Standard;
			//PrinterFunctions.PrintBitmapImage(this, portName, portSettings, getResources(), source, paperWidth, false, rasterType);

		 	PrinterFunctions.PrintBitmap(this,portName,portSettings,myBitmap, paperWidth, false, rasterType);


		PrinterFunctions.PrintSampleReceipt(this, portName, portSettings, "Line", getResources(),  "3inch (80mm)", null);
		/*RasterDocument rasterDoc = new RasterDocument(RasSpeed.Medium, RasPageEndMode.FeedAndFullCut, RasPageEndMode.FeedAndFullCut, RasTopMargin.Standard, 0, 0, 0);
		ArrayList<byte[]> list = new ArrayList<byte[]>();
		list.add(new byte[] { 0x1b, 0x1d, 0x61, 0x01 }); // Alignment (center)
		list.add("\nStar Clothing Boutique\r\n".getBytes());
		list.add("123 Star Road\r\nCity, State 12345\r\n\r\n".getBytes());

		list.add(new byte[] { 0x1b, 0x1d, 0x61, 0x00 }); // Alignment
		list.add(new byte[] { 0x1b, 0x44, 0x02, 0x10, 0x22, 0x00 }); // Set horizontal tab

		list.add("Date: MM/DD/YYYY".getBytes());

		list.add(new byte[] { ' ', 0x09, ' ' }); // Moving Horizontal Tab

		list.add("Time:HH:MM PM\r\n------------------------------------------------\r\n\r\n".getBytes());

		list.add(new byte[] { 0x1b, 0x45 }); // bold

		list.add("SALE \r\n".getBytes());

		list.add(new byte[] { 0x1b, 0x46 }); // bolf off

		list.add("SKU ".getBytes());

		list.add(new byte[] { 0x09 });

		// Notice that we use a unicode representation because that is
		// how Java expresses these bytes as double byte unicode
		// This will TAB to the next horizontal position
		list.add("  Description   \u0009         Total\r\n".getBytes());
		list.add("300678566 \u0009  PLAIN T-SHIRT\u0009         10.99\r\n".getBytes());
		list.add("300692003 \u0009  BLACK DENIM\u0009         29.99\r\n".getBytes());
		list.add("300651148 \u0009  BLUE DENIM\u0009         29.99\r\n".getBytes());
		list.add("300642980 \u0009  STRIPED DRESS\u0009         49.99\r\n".getBytes());
		list.add("300638471 \u0009  BLACK BOOTS\u0009         35.99\r\n\r\n".getBytes());
		list.add("Subtotal \u0009\u0009        156.95\r\n".getBytes());
		list.add("Tax \u0009\u0009          0.00\r\n".getBytes());
		list.add("------------------------------------------------\r\n".getBytes());
		list.add("Total".getBytes());

		// Character expansion
		list.add(new byte[] { 0x06, 0x09, 0x1b, 0x69, 0x01, 0x01 });

		list.add("        $156.95\r\n".getBytes());

		list.add(new byte[] { 0x1b, 0x69, 0x00, 0x00 }); // Cancel Character Expansion

		list.add("------------------------------------------------\r\n\r\n".getBytes());
		list.add("Charge\r\n159.95\r\n".getBytes());
		list.add("Visa XXXX-XXXX-XXXX-0123\r\n\r\n".getBytes());

		// Specify/Cancel White/Black Invert
		list.add("\u001b\u0034Refunds and Exchanges\u001b\u0035\r\n".getBytes());

		// Specify/Cancel Underline Printing
		list.add(("Within " + "\u001b\u002d\u0001" + "30 days\u001b\u002d\u0000" + " with receipt\r\n").getBytes());

		list.add("And tags attached\r\n\r\n".getBytes());

		// 1D barcode example
		list.add(new byte[] { 0x1b, 0x1d, 0x61, 0x01 });
		list.add(new byte[] { 0x1b, 0x62, 0x06, 0x02, 0x02 });
		list.add(" 12ab34cd56\u001e\r\n".getBytes());

		list.add(new byte[] { 0x1b, 0x64, 0x02 }); // Cut
*/

	}

}
