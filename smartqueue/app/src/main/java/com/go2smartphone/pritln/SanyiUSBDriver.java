package com.go2smartphone.pritln;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.PendingIntent;
import android.content.Context;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

public class SanyiUSBDriver {

	protected static final int STD_USB_REQUEST_GET_DESCRIPTOR = 0x06;
	protected static final int LIBUSB_DT_STRING = 0x03;
	private UsbManager mUsbManager;
	private Context mContext;
	private PendingIntent mPermissionIntent;
	// private int timeOut = 500;
	// private UsbDevice mUsbDevice;
	// private UsbInterface mUsbInterface;
	// private UsbEndpoint mUsbEndpointOut, mUsbEndpointIn;
	private UsbDeviceConnection mUsbDeviceConnection;

	private UsbPrinter usingPrinter;

	private List<UsbPrinter> allPrinters = new ArrayList<UsbPrinter>();

	public SanyiUSBDriver(UsbManager usbManager, Context context, PendingIntent usbIntent) {
		mUsbManager = usbManager;
		mContext = context;
		mPermissionIntent = usbIntent;
		initAllUsbDevice();
	}

	/**
	 * 设置超时时间，该时间用于所有的端口读写。
	 * 
	 * @param timeOut
	 *            超时时间
	 */
	// public void setTimeOut(int timeOut) {
	// if (this.timeOut > 0)
	// this.timeOut = timeOut;
	// }

	/**
	 * 过滤USB设备，如果返回值为空，则说明该设备被过滤掉，否则，该设备可以显示
	 * 
	 * @param usbDevice
	 * @return
	 */
	private UsbDevice filterUsbDevice(UsbDevice usbDevice) {

		/* add filters */
		return usbDevice;
	}

	public void initPrinter(UsbDevice device) {
		allPrinters.add(generatePrinterInstance(device));
	}

	public UsbPrinter findPrinter(String id) {
		for (UsbPrinter p : allPrinters) {
			if (p.getId().equals(id)) {
				return p;
			}
		}
		return null;
	}

	public UsbDeviceConnection openUsingDevice() {
		return mUsbManager.openDevice(usingPrinter.getDevice());
	}

	public void initAllUsbDevice() {

		List<String> deviceNames = new ArrayList<String>();
		if (mUsbManager != null && mContext != null) {
			HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
			for (Map.Entry<String, UsbDevice> e : deviceList.entrySet()) {
				final UsbDevice device = filterUsbDevice(e.getValue());
				UsbInterface intf = device.getInterface(0);
				if (device.getDeviceClass() == UsbConstants.USB_CLASS_PRINTER || intf.getInterfaceClass() == UsbConstants.USB_CLASS_PRINTER) {
					if (!mUsbManager.hasPermission(device)) {
						mUsbManager.requestPermission(device, mPermissionIntent);
					} else {
						allPrinters.add(generatePrinterInstance(device));
					}
				}
			}
		}

	}

	public UsbPrinter generatePrinterInstance(UsbDevice device) {
		UsbDeviceConnection connection = mUsbManager.openDevice(device);
		UsbInterface intf = device.getInterface(0);
		connection.claimInterface(intf, true);
		int ep = -1;

		for (int i = 0; i < intf.getEndpointCount(); i++) {
			if (intf.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_OUT) {
				ep = i;
				break;
			}
		}
		if (ep != -1) {
			return new UsbPrinter((getPrinterDescription(device, intf)), device, ep);
		}
		return null;
	}

	public UsbDeviceConnection openUsingDevice(UsbDevice device) {
		return mUsbManager.openDevice(device);
	}

	private String getPrinterDescription(final UsbDevice device, UsbInterface intf) {
		UsbDeviceConnection connection = mUsbManager.openDevice(device);
		connection.claimInterface(intf, true);
		UsbEndpoint ep = null;
		for (int i = 0; i < intf.getEndpointCount(); i++) {
			if (intf.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_OUT) {
				ep = intf.getEndpoint(i);
			}
		}
		byte[] rawDescs = connection.getRawDescriptors();

		byte[] buffer = new byte[255];
		int idxMan = rawDescs[14];
		int idxPrd = rawDescs[15];

		int rdo = connection.controlTransfer(UsbConstants.USB_DIR_IN | UsbConstants.USB_TYPE_STANDARD, STD_USB_REQUEST_GET_DESCRIPTOR,
				(LIBUSB_DT_STRING << 8) | idxMan, 0x0409, buffer, 0xFF, 0);
		try {
			String manufacturer = new String(buffer, 2, rdo - 2, "UTF-16LE");
			rdo = connection.controlTransfer(UsbConstants.USB_DIR_IN | UsbConstants.USB_TYPE_STANDARD, STD_USB_REQUEST_GET_DESCRIPTOR,
					(LIBUSB_DT_STRING << 8) | idxPrd, 0x0409, buffer, 0xFF, 0);
			String product = new String(buffer, 2, rdo - 2, "UTF-16LE");
			return manufacturer + " " + product;
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "";
	}

	/**
	 * 枚举以发现设备，并设置即将要连接的设备。
	 * 
	 * @return 如果发现了，则返回true；否则，返回false。
	 */
	// public boolean enumerate() {
	// if (null == mUsbManager)
	// return false;
	// if (null == mContext)
	// return false;
	//
	// // mUsbDevice = null;
	// // 以后如果需要支持多串口，只需添加另一个函数来扩展一下即可
	// HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
	// Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
	// if (deviceList.size() > 0) {
	//
	// // 初始化选择对话框布局，并添加按钮和事件
	// LinearLayout llSelectDevice = new LinearLayout(mContext);
	// llSelectDevice.setOrientation(LinearLayout.VERTICAL);
	// AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
	// builder.setTitle("选择设备").setView(llSelectDevice);
	// final AlertDialog dialog = builder.create();
	//
	// while (deviceIterator.hasNext()) {
	// final UsbDevice device = filterUsbDevice(deviceIterator.next());
	// UsbInterface intf = device.getInterface(0);
	// if (device.getDeviceClass() == UsbConstants.USB_CLASS_PRINTER ||
	// intf.getInterfaceClass() == UsbConstants.USB_CLASS_PRINTER) {
	//
	// Button btDevice = new Button(llSelectDevice.getContext());
	// btDevice.setGravity(Gravity.LEFT);
	// btDevice.setText("ID: " + device.getDeviceId());
	// btDevice.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// mUsbDevice = device;
	// dialog.dismiss();
	// // 等下添加到事件中去
	// if (!mUsbManager.hasPermission(mUsbDevice)) {
	// mUsbManager.requestPermission(mUsbDevice, mPermissionIntent);
	// } else {
	// UsbInterface intf = device.getInterface(0);
	// UsbDeviceConnection connection = mUsbManager.openDevice(device);
	// connection.claimInterface(intf, true);
	// UsbEndpoint ep = null;
	// for (int i = 0; i < intf.getEndpointCount(); i++) {
	// if (intf.getEndpoint(i).getDirection() == UsbConstants.USB_DIR_OUT) {
	// ep = intf.getEndpoint(i);
	// }
	// }
	//
	// byte[] rawDescs = connection.getRawDescriptors();
	// try {
	// byte[] buffer = new byte[255];
	// int idxMan = rawDescs[14];
	// int idxPrd = rawDescs[15];
	//
	// int rdo = connection.controlTransfer(UsbConstants.USB_DIR_IN |
	// UsbConstants.USB_TYPE_STANDARD,
	// STD_USB_REQUEST_GET_DESCRIPTOR, (LIBUSB_DT_STRING << 8) | idxMan, 0x0409,
	// buffer, 0xFF, 0);
	// String manufacturer = new String(buffer, 2, rdo - 2, "UTF-16LE");
	//
	// rdo = connection.controlTransfer(UsbConstants.USB_DIR_IN |
	// UsbConstants.USB_TYPE_STANDARD,
	// STD_USB_REQUEST_GET_DESCRIPTOR, (LIBUSB_DT_STRING << 8) | idxPrd, 0x0409,
	// buffer, 0xFF, 0);
	// String product = new String(buffer, 2, rdo - 2, "UTF-16LE");
	//
	// BufferedEscPrinter buf = new BufferedEscPrinter();
	//
	// EscUtil escUtil = new EscUtil();
	// escUtil.printString(manufacturer, buf);
	// escUtil.printAndFeed(product, 5, buf);
	// escUtil.cut(buf);
	//
	// byte[] content = buf.getBytes();
	//
	// if (ep != null) {
	// int i = connection.bulkTransfer(ep, content, content.length, 3000);
	// Log.d("" + i, "pos test");
	// }
	//
	// Log.d("device name : " + device.getDeviceId(), "pos test");
	// Log.d("manufacturer : " + manufacturer, "pos test");
	// Log.d("product : " + product, "pos test");
	// Log.d("device class : " + Integer.toHexString(device.getDeviceClass()),
	// "pos test");
	// if (device.getDeviceClass() == UsbConstants.USB_CLASS_PER_INTERFACE) {
	// Log.d("device class : " + Integer.toHexString(intf.getInterfaceClass()),
	// "pos test");
	// }
	// } catch (UnsupportedEncodingException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// });
	// llSelectDevice.addView(btDevice);
	// }
	// }
	// if (llSelectDevice.getChildCount() != 1)
	// // llSelectDevice.getChildAt(0).callOnClick();
	// // else
	// dialog.show();
	// return true;
	// }

	// return false;
	// }

	public byte[] helloWorld() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			baos.write(0x1B);
			baos.write("@".getBytes());
			baos.write("Hello World".getBytes());
			baos.write(0x1B);
			baos.write("d".getBytes());
			baos.write((byte) 6);

			System.out.println("Hello World");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	/**
	 * 返回true则表示连接成功
	 * 
	 * @return
	 */
	// public boolean connect() {
	// if (null == mUsbDevice)
	// return false;
	// if (null == mUsbManager)
	// return false;
	// if (!mUsbManager.hasPermission(mUsbDevice))
	// return false;
	//
	// // 枚举，把读写控制端口什么的给弄过来。然后set
	// outer : for (int i = 0; i < mUsbDevice.getInterfaceCount(); i++) {
	// mUsbInterface = mUsbDevice.getInterface(i);
	// mUsbEndpointOut = null;
	// mUsbEndpointIn = null;
	// for (int j = 0; j < mUsbInterface.getEndpointCount(); j++) {
	// UsbEndpoint endpoint = mUsbInterface.getEndpoint(j);
	// if (endpoint.getDirection() == UsbConstants.USB_DIR_OUT &&
	// endpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
	// mUsbEndpointOut = endpoint;
	// } else if (endpoint.getDirection() == UsbConstants.USB_DIR_IN &&
	// endpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
	// mUsbEndpointIn = endpoint;
	// }
	//
	// // 如果在第一个接口就找到了符合要求的端点，那么break;
	// if ((null != mUsbEndpointOut) && (null != mUsbEndpointIn))
	// break outer;
	// }
	// }
	// if (null == mUsbInterface)
	// return false;
	// if ((null == mUsbEndpointOut) && (null == mUsbEndpointIn))
	// return false;
	// mUsbDeviceConnection = mUsbManager.openDevice(mUsbDevice);
	// if (null == mUsbDeviceConnection)
	// return false;
	// mUsbDeviceConnection.claimInterface(mUsbInterface, true);
	//
	// return true;
	// }
	//
	// public void disconnect() {
	// if ((null != mUsbInterface) && (null != mUsbDeviceConnection)) {
	// mUsbDeviceConnection.releaseInterface(mUsbInterface);
	// mUsbDeviceConnection.close();
	// }
	// }

	// public int write(byte[] buffer, int length) {
	// if ((null == mUsbEndpointOut) || (null == mUsbDeviceConnection) || (null
	// == buffer))
	// return -1;
	// if (length <= 0 || length > buffer.length)
	// return -1;
	// return mUsbDeviceConnection.bulkTransfer(mUsbEndpointOut, buffer, length,
	// timeOut);
	// }
	//
	// public int read(byte[] buffer, int length) {
	// if ((null == mUsbEndpointIn) || (null == mUsbDeviceConnection) || (null
	// == buffer))
	// return -1;
	//
	// if (length <= 0 || length > buffer.length)
	// return -1;
	// return mUsbDeviceConnection.bulkTransfer(mUsbEndpointIn, buffer, length,
	// timeOut);
	// }

	public List<UsbPrinter> getAllPrinters() {
		return allPrinters;
	}

	public UsbPrinter getUsingPrinter() {
		return usingPrinter;
	}

	public void setUsingPrinter(UsbPrinter usingPrinter) {
		this.usingPrinter = usingPrinter;
	}

}
