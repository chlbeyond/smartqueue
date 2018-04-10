package com.go2smartphone.paidui;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.go2smartphone.paidui.model.Restaurant;
import com.go2smartphone.pritln.BufferedEscPrinter;
import com.go2smartphone.pritln.EscUtil;
import com.go2smartphone.pritln.SanyiUSBDriver;
import com.go2smartphone.pritln.UsbPrinter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends Activity {
	private FragmentManager fragmentManager;
	private int currentSetting;
	private String currentSettingDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		TextView textView_setting_version = (TextView) findViewById(R.id.textView_setting_version);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting, menu);
		return true;
	}

	public void clickButton(View view) {
		SendMeassage sendMeassage = new SendMeassage();
		switch (view.getId()) {

		case R.id.textView_setting_back:
			finish();
			break;
		// case R.id.textView_setting_picture_speed:
		// showDialog("图片播放速度", 1, 10, 5);
		// currentSetting = 1;
		// break;
		// case R.id.textView_setting_picture_once:
		// showDialog("图片播放间隔", 1, 10, 5);
		// currentSetting = 2;
		// break;
		case R.id.textView_setting_picture_call:
			showDialog("叫号次数", 1, 3, 2);
			currentSetting = 3;
			break;
		case R.id.textView_setting_show_video:
			currentSetting = 4;
			sendMeassage.execute();
			break;
		case R.id.textView_setting_show_picture:
			currentSetting = 5;
			sendMeassage.execute();
			break;
		case R.id.textView_setting_next_video:
			currentSetting = 6;
			sendMeassage.execute();
			break;
		case R.id.textView_setting_print:
			initPrint();
			break;
		default:
			break;
		}
	}

	public void initPrint() {
		UsbManager usbManager = (UsbManager) this.getSystemService(Context.USB_SERVICE);
		Restaurant.usbDriver = new SanyiUSBDriver(usbManager, this, PendingIntent.getBroadcast(this, 0, new Intent(Restaurant.ACTION_USB_PERMISSION),
				0));
		final List<UsbPrinter> allPrinters = Restaurant.usbDriver.getAllPrinters();
		if (allPrinters.size() > 0) {
			List<String> printStringList = new ArrayList<String>();
			for (final UsbPrinter printer : allPrinters) {
				printStringList.add(printer.getDescription());
			}
			final Dialog printDialog = new Dialog(this);
			printDialog.setTitle("请选择打印机");
			printDialog.setContentView(R.layout.printdialog);
			ListView listView_content_dialog = (ListView) printDialog.findViewById(R.id.listView_content_dialog);
			listView_content_dialog.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, printStringList));
			listView_content_dialog.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					Restaurant.usbDriver.setUsingPrinter(allPrinters.get(position));
					printConnectSuccess();
					printDialog.dismiss();
				}

			});
			printDialog.show();
		} else {
			Toast.makeText(this, "找不到打印机", Toast.LENGTH_LONG).show();
		}
	}

	public void printConnectSuccess() {
		// TODO Auto-generated method stub
		BufferedEscPrinter buf = new BufferedEscPrinter(Charset.forName("GBK"));
		EscUtil escUtil = new EscUtil();
		escUtil.escInit(buf);
		escUtil.setJustification(0, buf);
		escUtil.fontsize(1, buf);
		escUtil.printString("打印机连接成功 ", buf);
		escUtil.printString(Restaurant.usbDriver.getUsingPrinter().getDescription(), buf);
		escUtil.printAndFeed("排队系统由\"深圳三亿互联科技\"提供，技术支持或合作洽谈请登陆 www.sanyipos.com", 6, buf);
		escUtil.cut(buf);
		byte[] content = buf.getBytes();
		UsbDevice device = Restaurant.usbDriver.getUsingPrinter().getDevice();
		UsbDeviceConnection connection = Restaurant.usbDriver.openUsingDevice();
		UsbInterface intf = device.getInterface(0);
		connection.claimInterface(intf, true);
		UsbEndpoint ep = intf.getEndpoint(Restaurant.usbDriver.getUsingPrinter().getEp());
		int i = connection.bulkTransfer(ep, content, content.length, 3000);
	}

	public void showDialog(String type, final int minNumber, final int maxNumber, int normal) {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.order_operation_option);
		dialog.setTitle(Html.fromHtml("<font color='#16a4fa'>" + type + "</font>"));
		final EditText textNumOfPeople = (EditText) dialog.findViewById(R.id.editTextNumOfPeopleServed);
		textNumOfPeople.setFocusable(false);
		textNumOfPeople.setEnabled(false);
		textNumOfPeople.setText(normal + "");
		Button buttonOpenTableMinusSign = (Button) dialog.findViewById(R.id.buttonOpenTableMinusSign);
		buttonOpenTableMinusSign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String temp = textNumOfPeople.getText().toString();
				int value = Integer.parseInt(temp);
				if (value > minNumber) {
					value--;
					textNumOfPeople.setText(Integer.toString(value));
				}
			}
		});
		Button buttonOpenTablePlusSign = (Button) dialog.findViewById(R.id.buttonOpenTablePlusSign);
		buttonOpenTablePlusSign.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String temp = textNumOfPeople.getText().toString();
				int value = Integer.parseInt(temp);
				if (value < maxNumber) {
					value++;
					textNumOfPeople.setText(Integer.toString(value));
				}
			}
		});
		Button buttonOpenTable = (Button) dialog.findViewById(R.id.buttonOpenTable);
		// if button is clicked, close the custom dialog

		buttonOpenTable.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				currentSettingDetail = textNumOfPeople.getText().toString();
				SendMeassage sendMessage = new SendMeassage();
				sendMessage.execute();
				dialog.dismiss();
			}
		});
		Button buttonCancelOpenTable = (Button) dialog.findViewById(R.id.buttonCancelOpenTable);
		buttonCancelOpenTable.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.setCancelable(false);
		dialog.show();
	}

	public class SendMeassage extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			boolean status = false;
			DatagramSocket clientSocket;
			try {
				clientSocket = new DatagramSocket();
				clientSocket.setBroadcast(true);
				// construct request data for master service discovery
				String data = null;
				if (currentSetting == 1) {
					data = "{\"picture_speed\":\"" + currentSettingDetail + "\"}";
				} else if (currentSetting == 2) {
					data = "{\"picture_once\":\"" + currentSettingDetail + "\"}";
				} else if (currentSetting == 3) {
					data = "{\"call_frequency\":\"" + currentSettingDetail + "\"}";
//					Restaurant.CALL_FREQUENCY = Integer.valueOf(currentSettingDetail);
				} else if (currentSetting == 4) {
					data = "{\"show_fragment\":\"" + 1 + "\"}";
				} else if (currentSetting == 5) {
					data = "{\"show_fragment\":\"" + 2 + "\"}";
				} else if (currentSetting == 6) {
					data = "{\"next_video\":\"" + 0 + "\"}";
				}

				String message = null;
				// send 10 times discovery (broadcast request)

				try {
					DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), getBroadcastAddress(),
							2);
					clientSocket.send(packet);

					byte[] recvBuf = new byte[15000];
					DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
					clientSocket.setSoTimeout(3000);
					clientSocket.receive(receivePacket);
					message = new String(receivePacket.getData()).trim();

				} catch (java.net.SocketTimeoutException e) {

				}

				clientSocket.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			return null;

		}
	}

	InetAddress getBroadcastAddress() throws IOException {
		WifiManager wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		// handle null somehow
		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
		return InetAddress.getByAddress(quads);
	}
}
