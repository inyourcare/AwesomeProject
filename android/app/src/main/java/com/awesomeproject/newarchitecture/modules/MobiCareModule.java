// package com.awesomeproject.newarchitecture.modules;

// import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

// import android.bluetooth.BluetoothDevice;
// import android.bluetooth.BluetoothGatt;
// import android.os.Handler;
// import android.os.Looper;
// import android.os.Message;
// import android.util.Log;

// import com.facebook.react.bridge.Arguments;
// import com.facebook.react.bridge.ReactApplicationContext;
// import com.facebook.react.bridge.ReactContextBaseJavaModule;
// import com.facebook.react.bridge.ReactMethod;
// import com.facebook.react.bridge.WritableMap;
// import com.facebook.react.modules.core.DeviceEventManagerModule;
// import com.seers.bluetooth.le.BLEControlListener;
// import com.seers.bluetooth.le.BluetoothConfig;
// import com.seers.bluetooth.le.BluetoothRequestEnable;
// import com.seers.bluetooth.le.MC200MDataReceiveListener;
// import com.seers.bluetooth.le.MC200MStatusListener;
// import com.seers.bluetooth.le.MobiCAREBluetoothLeScanner;
// import com.seers.common.IOnHandlerMessage;
// import com.seers.common.LibUtils;
// import com.seers.common.WeakRefHandler;
// import com.seers.control.MobiCAREMC200MControl;
// import com.seers.control.operation.PacketOperationConfig;
// import com.seers.protocol.mc200m.obj.ErrorCodeList;
// import com.awesomeproject.MainActivity;

// import javax.annotation.Nullable;

// public class MoabiCareModule extends ReactContextBaseJavaModule {
//     private static final String TAG = MobiCareModule.class.getSimpleName();

//     private ReactApplicationContext reactContext;
//     private WeakRefHandler handler = null;
//     private BluetoothRequestEnable mBluetoothRequestEnable = null;
//     private MobiCAREMC200MControl mMobiCAREMC200MControl = null;
//     private boolean isBluetoothON = false;
//     private int gattState = BluetoothConfig.GATT_STATE_DISCONNECTED;
//     private BluetoothGatt bluetoothGatt = null;
//     private BluetoothDevice currentDevice = null;
//     private boolean isConnectDevices = false;
//     private boolean isStreaming = false;

//     private static final int HANDLE_MSG_BT_ON = 0x01;
//     private static final int HANDLE_MSG_BLE_SCAN_START = 0x02;
//     private Handler retryHandler = new Handler(Looper.getMainLooper());
//     private static final int RETRY_DELAY_MS = 10000; // 10초 대기
//     private int retryCount = 0;
//     private static final int MAX_RETRY_COUNT = 3; // 최대 재시도 횟수
//     public MobiCareModule(ReactApplicationContext context) {
//         super(context);
//         reactContext = context;
//         new Thread(new Runnable() {
//             @Override
//             public void run() {
//                 Looper.prepare();
//                 if (handler == null) {
//                     handler = new WeakRefHandler(iOnHandlerMessage);
//                 }
//                 if (mBluetoothRequestEnable == null) {
//                     mBluetoothRequestEnable = new BluetoothRequestEnable(context, bleControlListener);
//                 }
//                 if (mMobiCAREMC200MControl == null) {
//                     mMobiCAREMC200MControl = new MobiCAREMC200MControl(context, bleControlListener,
//                             iMC200MStatusListener);
//                     mMobiCAREMC200MControl.setDebugLogWrite(true);
//                 }
//                 Looper.loop();
//             }
//         }).start();

//     }

//     private void leDeviceConnect() {
//         Log.w(TAG, "################# leDeviceConnect ");
//         if (!isBluetoothEnable()) {
//             Log.w(TAG, "################# isBluetoothEnable ");
//             // setStatusMsg(getColor(R.color.warning_color), "블루투스를 지원하지 않거나 블루투스가
//             // 꺼져있습니다.");
//             return;
//         }
//         if (mMobiCAREMC200MControl != null) {
//             Log.w(TAG, "################# mMobiCAREMC200MControl " + getCurrentDevice());
//             BluetoothDevice bluetoothDevice = getCurrentDevice();
//             boolean result = mMobiCAREMC200MControl.mobiCAREMC200MConnected(3, bluetoothDevice);
//             if (result) {
//                 try {
//                     WritableMap params = Arguments.createMap();
//                     params.putString("isConnect", "true");
//                     params.putString("deviceName", bluetoothDevice.getName());
//                     sendEvent("BluetoothConnectEvent", params);
//                 } catch (SecurityException e) {
//                     e.printStackTrace();
//                 }
//             }
//             mMobiCAREMC200MControl.stopLeScan();
//             Log.w(TAG, "result " + result);
//         }
//     }

//     private IOnHandlerMessage iOnHandlerMessage = new IOnHandlerMessage() {
//         @Override
//         public void handleMessage(Message msg) {
//             if (msg == null) {
//                 return;
//             }
//             boolean result = false;
//             int messageType = msg.what;
//             int arg1 = msg.arg1;
//             int arg2 = msg.arg2;
//             Object object = msg.obj;

//             switch (messageType) {
//                 case HANDLE_MSG_BT_ON:
//                     Log.w(TAG, "HANDLE_MSG_BT_ON 요기도 쓰나?");
//                     if (mBluetoothRequestEnable != null) {
//                         mBluetoothRequestEnable.setBluetoothEnable(true);
//                     }
//                     if (mBluetoothRequestEnable != null) {
//                         mBluetoothRequestEnable.setBluetoothEnable(true);
//                     }
//                     break;
//                 case HANDLE_MSG_BLE_SCAN_START:
//                     // leScanStart();
//                     Log.w(TAG, "HANDLE_MSG_BLE_SCAN_ST 요기도 쓰나?");
//                     if (!isBluetoothOn()) {
//                         if (mBluetoothRequestEnable != null) {
//                             mBluetoothRequestEnable.setBluetoothEnable(true);
//                         }
//                     }
//                     break;
//             }
//         }
//     };

//     private BLEControlListener bleControlListener = new BLEControlListener() {
//         @Override
//         public void onLeScanner(BluetoothDevice device, int rssi, byte[] scanRecord) {
//             try {
//                 if (device != null) {
//                     Log.w(TAG, "################# Scan DEVICE [" + device.getName() + ", device addr " + device.getAddress()
//                             + "]");
//                     addMobiCAREDevice(device);
//                     WritableMap params = Arguments.createMap();
//                     params.putString("deviceName", device.getName());
//                     params.putString("deviceAddr", device.getAddress());
//                     sendEvent("BluetoothDataEvent", params);
//                     // leScanStop();
//                 }
//             } catch (SecurityException e) {
//                 e.printStackTrace();
//             }
//         }

//         @Override
//         public void onScanFail(int error) {
//             Log.w(TAG, "스캔실패 ~~~" + error);
//             switch (error) {
//                 case MobiCAREBluetoothLeScanner.SCAN_FAILED_ALREADY_STARTED:
//                     // setStatusMsg(getColor(R.color.warning_color), "이미 검색 중입니다.");
//                     break;
//                 case MobiCAREBluetoothLeScanner.SCAN_FAILED_APPLICATION_REGISTRATION_FAILED:
//                     // setStatusMsg(getColor(R.color.warning_color), "앱이 등록되지 않아 검색을 시작하지 못했습니다.\n이
//                     // 경우 권한을 체크하는 것이 좋습니다.");
//                     break;
//                 case MobiCAREBluetoothLeScanner.SCAN_FAILED_INTERNAL_ERROR:
//                     // setStatusMsg(getColor(R.color.warning_color), "내부 오류로 인해 Bluetooth 검색을 시작하지
//                     // 못했습니다.");
//                     break;
//                 case MobiCAREBluetoothLeScanner.SCAN_FAILED_FEATURE_UNSUPPORTED:
//                     // setStatusMsg(getColor(R.color.warning_color), "Bluetooth 기능을 지원하지 않습니다.");
//                     break;
//                 case MobiCAREBluetoothLeScanner.SCAN_FAILED_OUT_OF_HARDWARE_RESOURCES:
//                     // setStatusMsg(getColor(R.color.warning_color), "하드웨어 리소스가 부족해 검색을 시작하지
//                     // 못했습니다.");
//                     break;
//                 case MobiCAREBluetoothLeScanner.SCAN_FAILED_SCANNING_TOO_FREQUENTLY:
//                     // setStatusMsg(getColor(R.color.warning_color), "검색을 너무 자주 시도해 검색을 시도하지
//                     // 못했습니다.\n잠시후 다시 시도해주세요.");
//                     break;
//             }
//         }

//         @Override
//         public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int status) {
//             Log.w(TAG, "######### onConnectionStateChange " + bluetoothGatt.toString());
//             // if(mMobiCAREDeviceAdapter == null) {
//             // return;
//             // }
//             // if(mMobiCAREDeviceAdapter.getClickPosition() < 0) {
//             // return;
//             // }
//             // setGattState(status);

//             runOnUiThread(new Runnable() {
//                 @Override
//                 public void run() {
//                     if (bluetoothGatt != null) {
//                         try {
//                             Log.w(TAG, "################# onConnectionStateChange " + status);
//                             switch (status) {
//                                 case BluetoothConfig.GATT_STATE_DISCONNECTED:
//                                     setBluetoothGatt(null);
//                                     // 밧데리제거시 0
//                                     // mMobiCAREDeviceAdapter.setBleConnChanged(getResources().getString(R.string.ble_state_disconnected));
//                                     // disConnectUIInit();
//                                     retryConnection();
//                                     break;
//                                 case BluetoothConfig.GATT_STATE_DISCONNECTING:
//                                     // clearECGData();
//                                     setBluetoothGatt(null);
//                                     // mMobiCAREDeviceAdapter.setBleConnChanged(getResources().getString(R.string.ble_state_disconnecting));
//                                     break;
//                                 case BluetoothConfig.GATT_STATE_CONNECTING:
//                                     setBluetoothGatt(null);
//                                     // mMobiCAREDeviceAdapter.setBleConnChanged(getResources().getString(R.string.ble_state_connecting));
//                                     break;
//                                 case BluetoothConfig.GATT_STATE_CONNECTED:
//                                     setBluetoothGatt(bluetoothGatt);
//                                     // mMobiCAREDeviceAdapter.setBleConnChanged(getResources().getString(R.string.ble_state_connected));

//                                     BluetoothDevice bluetoothDevice = bluetoothGatt.getDevice();
//                                     if (bluetoothDevice == null) {
//                                         Log.w(TAG, "bluetoothDevice");
//                                         return;
//                                     }
//                                     String macAddr = bluetoothDevice.getAddress();
//                                     if (macAddr == null || macAddr.isEmpty()) {
//                                         Log.w(TAG, "macAddr");
//                                         return;
//                                     }
//                                     String serialNumber = LibUtils.getSerialNumber(macAddr);
//                                     if (serialNumber == null || serialNumber.isEmpty()) {
//                                         Log.w(TAG, "serialNumber " + serialNumber);
//                                         return;
//                                     }
//                                     serialNumber = "A" + serialNumber;
//                                     Log.w(TAG, "######### onConnectionStateChange " + serialNumber);
//                                     // String connectDeviceInfo =
//                                     // String.format(getResources().getString(R.string.connected_device_info),
//                                     // macAddr, serialNumber);
//                                     // if(connectDeviceInfo != null && !connectDeviceInfo.isEmpty()) {
//                                     // if(device_connect_info != null) {
//                                     // device_connect_info.setText(connectDeviceInfo);
//                                     // }
//                                     // }
//                                     break;
//                             }
//                         } catch (SecurityException e) {
//                             e.printStackTrace();
//                         }
//                     } else {
//                         Log.w(TAG, "######### onConnectionStateChange Error" + status);
//                     }
//                 }
//             });
//         }
//         private void retryConnection() {
//             if (retryCount <= MAX_RETRY_COUNT) {
//                 retryCount++;
//                 Log.w(TAG, "Retrying connection... Attempt " + retryCount);
//                 retryHandler.postDelayed(new Runnable() {
//                     @Override
//                     public void run() {
//                         Log.w(TAG, "Retrying connection... Success " + retryCount);
//                         leDeviceConnect();
//                         //onMobiCAREStart();
//                     }
//                 }, RETRY_DELAY_MS);
//             } else {
//                 Log.e(TAG, "Max retry attempts reached. Connection failed.");
//                 WritableMap params = Arguments.createMap();
//                 params.putString("isConnect", "false");
//                 sendEvent("BluetoothConnectEvent", params);
//                 retryCount = 0; // 재시도 횟수 초기화
//             }
//         }
//         @Override
//         public void onBluetoothPowerStatus(int status) {
//             Log.w(TAG, "######### onBluetoothPowerStatus");
//             switch (status) {
//                 case BluetoothRequestEnable.BT_STATE_OFF:
//                     Log.e(TAG, "################# onBluetoothState(), BT_STATE_OFF");
//                     setBluetoothOn(false);
//                     // WeakRefHandler.sendHandler(handler, HANDLE_MSG_BT_ON, 0, 0, null, (1000L *
//                     // 7L));
//                     break;
//                 case BluetoothRequestEnable.BT_STATE_ON:
//                     Log.w(TAG, "################# onBluetoothState(), BT_STATE_ON");
//                     setBluetoothOn(true);
//                     // WeakRefHandler.sendHandler(handler, HANDLE_MSG_BLE_SCAN_START, 0, 0, null,
//                     // (1000L * 2L));
//                     break;
//                 default:
//                     break;
//             }
//         }
//     };

//     private void leScanStop() {
//         if (mMobiCAREMC200MControl != null) {
//             mMobiCAREMC200MControl.stopLeScan();
//         }
//     }

//     private MC200MStatusListener iMC200MStatusListener = new MC200MStatusListener() {
//         @Override
//         public void onBatteryStatus(int state, int mv) {
//             Log.w(TAG, "################# onBatteryStatus " + state);
//             WritableMap params = Arguments.createMap();
//             params.putString("state", String.valueOf(state));
//             sendEvent("BatteryStatusEvent", params);
//             // public static final int MC200M_BATTERY_STATE_LOW = 4; // 배터리가 얼마 남지 않았습니다.
//             // public static final int MC200M_BATTERY_STATE_EMPTY = 5; // 배터리 방전
//         }

//         @Override
//         public void onLeadStatus(int status) {
//             Log.w(TAG, "onLeadStatus " + status);
//         }

//         @Override
//         public void onHRChanged(int hr) {
//             // Log.w(TAG, "왜수집이 안되지 " + hr);
//             if (hr > 0) {
//                 // Log.w(TAG, "################# HR: " + String.valueOf(hr) + "######################");
//                 WritableMap params = Arguments.createMap();
//                 params.putString("heartRate", String.valueOf(hr));
//                 sendEvent("BluetoothHeartRateEvent", params);
//             }
//         }

//         @Override
//         public void onDeviceControlError(int type, int errorCode) {
//             Log.w(TAG, "################# 에러코드[" + errorCode + "]");
//             switch (errorCode) {
//                 case ErrorCodeList.INVALID_ADDRESS:
//                     /** 센서를 제어하기 위한 어드레스 값이 잘 못 되었습니다. */
//                     break;
//                 case ErrorCodeList.INVALID_VALUE:
//                     /** 스트리밍 or 레코딩 하기 위한 전달 값이 잘 못 되었습니다. */
//                     break;
//                 case ErrorCodeList.INVALID_OPERATION:
//                     /** 작업이 처리되지 않았습니다. */
//                     break;
//                 case ErrorCodeList.INVALID_SOURCE_ID:
//                     /** 스트리밍 or 레코딩 하기 위한 전달 값이 잘 못 되었습니다. */
//                     break;
//                 case ErrorCodeList.INVALID_MEASUREMENT_CODE:
//                     /** 측정코드를 확인해주세요. */
//                     break;
//                 case ErrorCodeList.NO_STORAGE:
//                     /** SD Card를 확인해주세요. */
//                     break;
//                 case ErrorCodeList.INVALID_FILE_SYSTEM:
//                     /** 파일 시스템이 손상되었습니다. */
//                     break;
//                 case ErrorCodeList.NO_RECORDING:
//                     /** 레코딩 중이 아닙니다. */
//                     break;
//                 case ErrorCodeList.INVALID_START_TIME:
//                     /** 전달된 시작 시간이 잘 못 되었습니다. */
//                     break;
//                 case ErrorCodeList.RECORDING_IN_PROGRESS:
//                     break;
//                 case ErrorCodeList.ENCRYPTION_FAILED:
//                     break;
//                 case ErrorCodeList.MEASUREMENT_DATA_EXIST:
//                     break;
//                 case ErrorCodeList.UNKNOWN_ERROR:
//                     /** 정의되지 않은 에러코드 */
//                     break;
//             }
//         }
//     };

//     private void leScanStart(String deviceCode) {
//         if (!isBluetoothEnable()) {
//             // setStatusMsg(getColor(R.color.warning_color), "블루투스를 지원하지 않거나 블루투스가
//             // 꺼져있습니다.");
//             return;
//         }

//         String scanMatchStr = deviceCode;
//         String statusMsg = "mobiCARE-MC200M 검색을 시작합니다.";
//         int leScanType = BLEControlListener.LE_SCAN_TYPE_SERIAL_NUMBER;

//         Log.w(TAG, "################# Status Message : " + statusMsg);
//         Log.w(TAG, "################# scan Match String : " + scanMatchStr);

//         if (mMobiCAREMC200MControl != null) {
//             Log.w(TAG, "################# startLeScan : " + leScanType + " " + scanMatchStr);
//             mMobiCAREMC200MControl.startLeScan(leScanType, scanMatchStr);
//         }
//     }

//     private void onMobiCAREStart() {
//         if (isStreaming) {
//             return;
//         }
//         Log.w(TAG, " ################# onMobiCAREStart ");
//         if (mMobiCAREMC200MControl == null) {
//             Log.w(TAG, "mMobiCAREMC200MControl ");
//             return;
//         }

//         mMobiCAREMC200MControl.startStreaming();
//         isStreaming = true;
//         Log.w(TAG, "startStreaming ");
//     }

//     private void onMobiCAREStop() {
//         Log.w(TAG, "onMobiCAREStop ");
//         if (mMobiCAREMC200MControl == null) {
//             Log.w(TAG, "mMobiCAREMC200MControl ");
//             return;
//         }

//         mMobiCAREMC200MControl.stopStreaming();
//         isStreaming = false;
//         Log.w(TAG, "stopStreaming ");
//     }

//     public boolean isBluetoothEnable() {
//         if (mBluetoothRequestEnable == null) {
//             return false;
//         }
//         if (!mBluetoothRequestEnable.isLocationEnabled()) {
//             return false;
//         }
//         if (!mBluetoothRequestEnable.isBluetoothSupport()) {
//             return false;
//         }
//         if (!mBluetoothRequestEnable.isBluetoothEnable()) {
//             setBluetoothOn(false);
//             return false;
//         }
//         return true;
//     }

//     private void addMobiCAREDevice(BluetoothDevice bluetoothDevice) {
//         if (bluetoothDevice == null) {
//             return;
//         }
//         setCurrentDevice(bluetoothDevice);
//     }

//     private void leDeviceDisconnect() {
//         if (mMobiCAREMC200MControl != null) {
//             mMobiCAREMC200MControl.stopLeScan();
//         }
//         if (mMobiCAREMC200MControl != null) {
//             mMobiCAREMC200MControl.mobiCAREMC200MDisconnected();
//         }
//     }

//     public boolean isBluetoothOn() {
//         return isBluetoothON;
//     }

//     public void setBluetoothOn(boolean btOn) {
//         this.isBluetoothON = btOn;
//     }

//     private void setBluetoothGatt(BluetoothGatt bluetoothGatt) {
//         this.bluetoothGatt = bluetoothGatt;
//     }

//     private BluetoothGatt getBluetoothGatt() {
//         return bluetoothGatt;
//     }

//     private void setGattState(int gattState) {
//         this.gattState = gattState;
//     }

//     private int getGattState() {
//         return this.gattState;
//     }

//     public BluetoothDevice getCurrentDevice() {
//         return currentDevice;
//     }

//     public void setCurrentDevice(BluetoothDevice currentDevice) {
//         this.currentDevice = currentDevice;
//     }

//     @Override
//     public String getName() {
//         return "MobiCareModule";
//     }

//     @ReactMethod
//     public void devicesScanStart(String deviceCode) {
//         leScanStart(deviceCode);
//     }

//     @ReactMethod
//     public void getHeartRate() {
//         Log.w(TAG, "getHeartRate ");

//         onMobiCAREStart();
//         //leScanStop();
//     }

//     @ReactMethod
//     public void stopHeartRate() {
//         Log.w(TAG, "stopHeartRate ");
//         isConnectDevices = false;
//         isStreaming = false;
//         onMobiCAREStop();
//         leDeviceDisconnect();
//     }
//     @ReactMethod
//     public void reconnectHeartRate() {
//         Log.w(TAG, "reconnectHeartRate ");
//         isConnectDevices = false;
//         isStreaming = false;
//         onMobiCAREStop();
//         leDeviceDisconnect();
//         new Handler().postDelayed(new Runnable() {
//             @Override
//             public void run() {
//                 leDeviceConnect();
//             }
//         }, 5000);
//     }

//     @ReactMethod
//     public void connectDevices() {
//         if (!isConnectDevices) {
//             Log.w(TAG, "connectDevices ");
//             leDeviceConnect();
//         }
//         isConnectDevices = true;
//     }

//     @ReactMethod
//     public void powerOFFDevices() {
//         isConnectDevices = false;
//         isStreaming = false;
//         Log.w(TAG, "powerOFFDevices ");
//         if (mMobiCAREMC200MControl == null) {
//             return;
//         }
//         mMobiCAREMC200MControl.onMobiCAREPowerOFF();
//         if (bluetoothGatt != null) {
//             try {
//                 Log.w(TAG, "bluetoothGatt.close ");
//                 bluetoothGatt.close();
//                 setBluetoothGatt(null);
//             } catch (SecurityException e) {
//                 e.printStackTrace();
//             }
//         }
//     }

//     @ReactMethod
//     public void disConnectDevices() {
//         leDeviceDisconnect();
//     }

//     public void sendEvent(String eventName, @Nullable WritableMap params) {
//         /**
//          * WritableMap params = Arguments.createMap();
//          * params.putString("data", "Fetched Bluetooth data");
//          * sendEvent("BluetoothDataEvent", params);
//          */
//         reactContext
//                 .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
//                 .emit(eventName, params);
//     }

// }
