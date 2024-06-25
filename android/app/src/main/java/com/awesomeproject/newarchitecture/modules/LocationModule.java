// package com.awesomeproject.newarchitecture.modules;

// import android.Manifest;
// import android.content.pm.PackageManager;
// import android.location.Location;
// import android.os.Handler;
// import android.os.Looper;
// import android.hardware.Sensor;
// import android.hardware.SensorEvent;
// import android.hardware.SensorEventListener;
// import android.hardware.SensorManager;
// import android.util.Log;

// import androidx.core.app.ActivityCompat;
// import androidx.core.content.ContextCompat;

// import com.facebook.react.bridge.Arguments;
// import com.facebook.react.bridge.ReactApplicationContext;
// import com.facebook.react.bridge.ReactContextBaseJavaModule;
// import com.facebook.react.bridge.ReactMethod;
// import com.facebook.react.bridge.WritableMap;
// import com.facebook.react.modules.core.DeviceEventManagerModule;
// import com.facebook.react.modules.core.PermissionListener;
// import com.google.android.gms.location.FusedLocationProviderClient;
// import com.google.android.gms.location.LocationCallback;
// import com.google.android.gms.location.LocationRequest;
// import com.google.android.gms.location.LocationResult;
// import com.google.android.gms.location.LocationServices;
// import com.awesomeproject.common.SpeedCalculator;

// import java.util.concurrent.atomic.AtomicBoolean;
// import java.util.concurrent.atomic.AtomicInteger;
// import java.util.concurrent.atomic.AtomicLong;
// import java.util.concurrent.atomic.AtomicReference;

// public class LocationModule extends ReactContextBaseJavaModule implements SensorEventListener, PermissionListener {
//     private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

//     private FusedLocationProviderClient fusedLocationClient;
//     private LocationRequest locationRequest;
//     private AtomicReference<Location> lastLocation = new AtomicReference<>(null);
//     // private float lastBearing;
//     private LocationCallback locationCallback;
//     private AtomicReference<Float> totalDistance = new AtomicReference<>(0f);
//     private AtomicInteger totalSteps = new AtomicInteger(0);
//     // private AtomicReference<ArrayList<Float>> sensorDataBufferRef = new AtomicReference<>(new ArrayList<>());
//     private AtomicLong lastStepUpdateTime = new AtomicLong(0);
//     private AtomicLong lastSpeedUpdateTime = new AtomicLong(0);
//     private final long AVERAGE_STRIDE_UPDATE_THRESHOLD = 3000; // 3초
//     private final long ROTATION_UPDATE_THRESHOLD = 8000; // 4초
//     private final float DEFAULT_STRIDE = 0.7f; // 4초
//     // private final int SENSOR_BUFFER_CAPACITY = 3000; // 버퍼에 저장할 최대 데이터 수
//     private AtomicLong averageStrideUpdateTime = new AtomicLong(0);
//     private AtomicReference<Float> averageStride = new AtomicReference<>(DEFAULT_STRIDE);
//     private AtomicReference<Float> averageSpeed = new AtomicReference<>(0.0f);
//     private AtomicInteger periodStep = new AtomicInteger(0);
//     private AtomicInteger periodAccelStep = new AtomicInteger(0);
//     private AtomicInteger tempSteps = new AtomicInteger(0);
//     private AtomicReference<Float> periodDistance = new AtomicReference<>(0f);
//     private AtomicReference<Float> tempDistance = new AtomicReference<>(0f);
//     private final long STEP_UPDATE_THRESHOLD = 3000;
//     private SensorManager sensorManager;
//     private Sensor stepDetectorSensor;
//     private Sensor accelerometerSensor;
//     private Sensor rotationSensor;
//     private AtomicBoolean initialOrientationSet = new AtomicBoolean(false);
//     private AtomicBoolean isWalk = new AtomicBoolean(false);
//     ////////////////////////////////////////////////////////////////////
//     private int accelRingCounter = 0;
//     // private int velRingCounter = 0;
//     private long lastStepTimeNs = 0;
//     // private float oldVelocityEstimate = 0f;
//     // private final float STEP_THRESHOLD = 1f;
//     private final long STEP_DELAY_NS = 200000000;
//     // private final float ROTATE_THRESHOLD = 250000000;
//     private final int ACCEL_RING_SIZE = 500;
//     // private final int VEL_RING_SIZE = 100;
//     private float[] accelRingX = new float[ACCEL_RING_SIZE];
//     private float[] accelRingY = new float[ACCEL_RING_SIZE];
//     private float[] accelRingZ = new float[ACCEL_RING_SIZE];
//     // private float[] velRing = new float[VEL_RING_SIZE];
//     private float[] recentValues = new float[3];
//     // private int index = 0;
//     private float[] lastRotationMatrix = new float[9];

//     private AtomicReference<Float> rotationAngle = new AtomicReference<>(0.0f);
//     private static final float TARGET_ANGLE = 100; // 목표 각도 (도)
//     private AtomicLong rotationUpdateTime = new AtomicLong(0);
//     private static final float NS2S = 1.0f / 1000000000.0f;
//     private SpeedCalculator speedCalculator = new SpeedCalculator();
//     private AtomicBoolean haveStepSensor = new AtomicBoolean(false);
//     private Handler noStepHandler = new Handler(Looper.getMainLooper());
//     private Runnable noStepRunnable = new Runnable() {
//         @Override
//         public void run() {
//             if ((System.currentTimeMillis() - lastStepUpdateTime.get()) > STEP_UPDATE_THRESHOLD) {
//                 totalDistance.set(totalDistance.get() + averageStride.get() * periodStep.get());
//                 totalSteps.set(totalSteps.get() + periodStep.get());
//                 averageSpeed.set(0f);
//                 averageStride.set(DEFAULT_STRIDE);
//                 tempSteps.set(0);
//                 periodStep.set(0);
//                 periodAccelStep.set(0);
//                 lastLocation.set(null);
//                 isWalk.set(false);

//                 averageStrideUpdateTime.set(System.currentTimeMillis());
//                 lastSpeedUpdateTime.set(System.currentTimeMillis());
//                 lastStepUpdateTime.set(System.currentTimeMillis());
//             }
//             // Log.w("로케이션로그 isWalk noStep", Boolean.toString(isWalk.get()));
//             // 주기적으로 검사를 계속하기 위해 자기 자신을 다시 스케줄링
//             noStepHandler.postDelayed(this, 3000); // 예를 들어, 매 5초마다 검사
//         }
//     };
//     private void calculateAverageStride() {
//         long currentTime = System.currentTimeMillis();
//         long speedUpdateTime = currentTime - lastSpeedUpdateTime.get();

//         double speed = speedCalculator.addSpeed((tempDistance.get() / speedUpdateTime * 1000 * 60 * 60) / 1000);
//         if (speed >= 15.0) {
//             speed = 15;
//         }

//         averageSpeed.set((float) speed);
//         lastSpeedUpdateTime.set(System.currentTimeMillis());
//         tempDistance.set(0f);

//         long periodTime = currentTime - averageStrideUpdateTime.get();
        
//         long stepIntervalTimeFormStepSensor = 0;
//         long stepIntervalTimeFormAccelSensor = 0;
//         if(periodAccelStep.get() > 0) {
//             stepIntervalTimeFormAccelSensor = periodTime / periodAccelStep.get();
//         }
//         if (periodStep.get() > 0) {
//             stepIntervalTimeFormStepSensor = periodTime / periodStep.get();
//         }
//         if (stepIntervalTimeFormAccelSensor > 0 && stepIntervalTimeFormAccelSensor <= 500) {
//             periodStep.set(periodAccelStep.get());
//         }
//         // Log.w("로케이션로그 stepIntervalTimeFormAccelSensor", stepIntervalTimeFormAccelSensor + " " + periodAccelStep.get());
//         // Log.w("로케이션로그 stepIntervalTimeFormStepSensor", stepIntervalTimeFormStepSensor + " " + periodStep.get());
//         //if (periodTime > AVERAGE_STRIDE_UPDATE_THRESHOLD) {
//             float stride = 0f;
//             if (periodStep.get() > 0) {
//                 stride = periodDistance.get() / periodStep.get();
//             }
//             // Log.w("로케이션로그 stride", periodDistance.get() + " " + periodStep.get() + " " + stride + " " + averageStride.get() + " " + speed);
//             if (stride >= 0.3 && stride <= 2) {
//                 totalSteps.set(totalSteps.get() + periodStep.get());
//                 totalDistance.set(totalDistance.get() + stride * periodStep.get());
//                 averageStride.set(stride);
//                 averageStrideUpdateTime.set(System.currentTimeMillis());
//             } else {
//                 totalSteps.set(totalSteps.get() + periodStep.get());
//                 totalDistance.set(totalDistance.get() + DEFAULT_STRIDE * periodStep.get());
//             }

//             periodStep.set(0);
//             periodAccelStep.set(0);
//             periodDistance.set(0f);
//             tempSteps.set(0);
//             lastStepUpdateTime.set(System.currentTimeMillis());
            
//         //}
//     }

//     private Handler dataSendHandler = new Handler(Looper.getMainLooper());
//     private Runnable dataSendRunnable = new Runnable() {
//         @Override
//         public void run() {
//             WritableMap params = Arguments.createMap();
//             params.putInt("step", totalSteps.get() + tempSteps.get());
//             params.putDouble("distance", Math.ceil(totalDistance.get() + averageStride.get() * tempSteps.get()));
//             params.putDouble("speed", averageSpeed.get());
//             // params.putDouble("speed", 5.4f);
//             getReactApplicationContext()
//                     .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
//                     .emit("exerciseInfo", params);

//             dataSendHandler.postDelayed(this, 2000); // 2초 후에 다시 실행
//         }
//     };

//     public LocationModule(ReactApplicationContext reactContext) {
//         super(reactContext);

//         fusedLocationClient = LocationServices.getFusedLocationProviderClient(reactContext);
//         createLocationRequest();
//         createLocationCallback();

//         sensorManager = (SensorManager)reactContext.getSystemService(reactContext.SENSOR_SERVICE);
//         stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
//         accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//         rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
//     }

//     private void createLocationRequest() {
//         locationRequest = LocationRequest.create();
//         locationRequest.setInterval(3500);
//         locationRequest.setFastestInterval(3500);
//         locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//     }

//     @Override
//     public void onAccuracyChanged(Sensor sensor, int accuracy) {
//     }
//     @Override
//     public void onSensorChanged(SensorEvent event) {
//         if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//             if (event != null) {
//                 updateAccel(
//                         event.timestamp,
//                         event.values[0],
//                         event.values[1],
//                         event.values[2]
//                 );
//             }
//         }
//         if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
//             // Log.e("로케이션로그", "periodStep" + totalSteps);
//             if (isWalk.get() == false) {
//                 tempSteps.set(5);
//                 periodStep.set(5);
//             }
//             isWalk.set(true);
//             tempSteps.set(tempSteps.get() + event.values.length);
//             periodStep.set(periodStep.get() + event.values.length);
//             lastStepUpdateTime.set(System.currentTimeMillis());
//         }

//         if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
//             float[] rotationMatrix = new float[9];
//             SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);

//             if (!initialOrientationSet.get()) {
//                 System.arraycopy(rotationMatrix, 0, lastRotationMatrix, 0, rotationMatrix.length);
//                 initialOrientationSet.set(true);
//             } else {
//                 float[] orientationAngles = new float[3];
//                 SensorManager.getOrientation(rotationMatrix, orientationAngles);
//                 float[] initialOrientationAngles = new float[3];
//                 SensorManager.getOrientation(lastRotationMatrix, initialOrientationAngles);

//                 float deltaAngle = Math.abs((float) Math.toDegrees(orientationAngles[0] - initialOrientationAngles[0]));

//                 long periodTime = System.currentTimeMillis() - rotationUpdateTime.get();
//                 if (periodTime > ROTATION_UPDATE_THRESHOLD && deltaAngle < 180 && deltaAngle > 135) {
//                     // Log.w("로케이션로그 ROTATION", "deltaAngle true " + deltaAngle);
//                     lastLocation.set(null);
//                     totalDistance.set(totalDistance.get() + averageStride.get() * periodStep.get());
//                     totalSteps.set(totalSteps.get() + periodStep.get());
//                     periodDistance.set(0f);
//                     periodStep.set(0);
//                     periodAccelStep.set(0);
//                     tempSteps.set(0);
//                     averageStride.set(DEFAULT_STRIDE);

//                     averageStrideUpdateTime.set(System.currentTimeMillis());
//                     lastStepUpdateTime.set(System.currentTimeMillis());

//                     System.arraycopy(rotationMatrix, 0, lastRotationMatrix, 0, rotationMatrix.length);
//                     rotationUpdateTime.set(System.currentTimeMillis());
//                 } else {
//                     // Log.w("로케이션로그 ROTATION", "deltaAngle false " + deltaAngle);
//                 }
//             }
//         }
//     }

//     private void updateAccel(long timeNs, float x, float y, float z) {
//         float[] currentAccel = new float[3];
//         currentAccel[0] = x;
//         currentAccel[1] = y;
//         currentAccel[2] = z;

//         // First step is to update our guess of where the global z vector is.
//         accelRingCounter++;
//         accelRingX[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[0];
//         accelRingY[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[1];
//         accelRingZ[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[2];

//         float[] worldZ = new float[3];
//         worldZ[0] = sumOfFloatArray(accelRingX) / Math.min(accelRingCounter, ACCEL_RING_SIZE);
//         worldZ[1] = sumOfFloatArray(accelRingY) / Math.min(accelRingCounter, ACCEL_RING_SIZE);
//         worldZ[2] = sumOfFloatArray(accelRingZ) / Math.min(accelRingCounter, ACCEL_RING_SIZE);

//         float normalizationFactor = norm(worldZ);
//         worldZ[0] /= normalizationFactor;
//         worldZ[1] /= normalizationFactor;
//         worldZ[2] /= normalizationFactor;

//         // Next step is to figure out the component of the current acceleration
//         // in the direction of world_z and subtract gravity's contribution
//         float currentZ = dot(worldZ, currentAccel) - normalizationFactor;

//         // velRingCounter++;
//         //velRing[velRingCounter % VEL_RING_SIZE] = currentZ;

//         recentValues[0] = recentValues[1];
//         recentValues[1] = recentValues[2];
//         recentValues[2] = currentZ;
//         //float velocityEstimate = sumOfFloatArray(velRing);
//         // Log.w("로케이션로그", "속도" + velocityEstimate);
//         // 현재 값이 이전 값과 다음 값보다 크면 피크 간주
//         if ((Math.abs(recentValues[0] - recentValues[1]) > 0.1) && (Math.abs(recentValues[1] - recentValues[2]) > 0.1) && recentValues[0] < recentValues[1] && recentValues[1] > recentValues[2] && timeNs - lastStepTimeNs > STEP_DELAY_NS) {
//             if(haveStepSensor.get() == false) {
//                 isWalk.set(true);
//                 periodStep.set(periodStep.get() + 1);
//                 tempSteps.set(tempSteps.get() + 1);
//                 lastStepTimeNs = timeNs;
//                 lastStepUpdateTime.set(System.currentTimeMillis());    
//             } else {
//                 // Log.w("로케이션로그", "periodAccelStep " + periodAccelStep.get());
//                 periodAccelStep.set(periodAccelStep.get() + 1);
//                 lastStepTimeNs = timeNs;
//             }
            
//         }
//     }
//     public static float sumOfFloatArray(float[] numbers) {
//         float sum = 0;
//         for (float number : numbers) {
//             sum += number;
//         }
//         return sum;
//     }
//     public static float norm(float[] vector) {
//         return (float) Math.sqrt(vector[0] * vector[0] + vector[1] * vector[1] + vector[2] * vector[2]);
//     }
//     private float dot(float[] a, float[] b) {
//         // 두 벡터 a와 b의 내적을 계산합니다.
//         // 두 배열의 길이가 같다고 가정합니다.
//         float result = 0.0f;
//         for (int i = 0; i < a.length; i++) {
//             result += a[i] * b[i];
//         }
//         return result;
//     }
//     private void createLocationCallback() {
//         locationCallback = new LocationCallback() {
//             @Override
//             public void onLocationResult(LocationResult locationResult) {
//                 if (locationResult == null) {
//                     return;
//                 }
//                if (!isWalk.get()) {
//                    // Log.w("로케이션로그 isWalk location", isWalk.get() + " ");
//                    averageStride.set(DEFAULT_STRIDE);
//                    lastLocation.set(null);

//                    averageStrideUpdateTime.set(System.currentTimeMillis());
//                    lastSpeedUpdateTime.set(System.currentTimeMillis());
//                    lastStepUpdateTime.set(System.currentTimeMillis());
//                    return;
//                }
//                 //Log.w("로케이션로그 start", "location start");
//                 for (Location location : locationResult.getLocations()) {
//                     // Log.w("로케이션로그 location", location.getLatitude() + " " + location.getLongitude());
//                     if (location == null) {
//                         continue;
//                     }
//                     if (lastLocation.get() == null) {
//                         lastLocation.set(location);
//                         totalDistance.set(totalDistance.get() + averageStride.get() * periodStep.get());
//                         totalSteps.set(totalSteps.get() + periodStep.get());
//                         periodDistance.set(0f);
//                         periodStep.set(0);
//                         periodAccelStep.set(0);
//                         tempDistance.set(0f);
//                         tempSteps.set(0);
//                         averageStride.set(DEFAULT_STRIDE);

//                         averageStrideUpdateTime.set(System.currentTimeMillis());
//                         lastSpeedUpdateTime.set(System.currentTimeMillis());
//                         lastStepUpdateTime.set(System.currentTimeMillis());
//                         continue;
//                     }
//                     float distance = lastLocation.get().distanceTo(location);

//                     tempDistance.set(tempDistance.get() + distance);
//                     periodDistance.set(periodDistance.get() + distance);

//                     lastLocation.set(location);
//                     calculateAverageStride();
//                 }
//             }
//         };
//     }
//     @Override
//     public String getName() {
//         return "LocationModule";
//     }

//     @ReactMethod
//     public void startUpdates() {

//         if (ContextCompat.checkSelfPermission(getReactApplicationContext(),
//                 Manifest.permission.ACCESS_FINE_LOCATION)
//                 == PackageManager.PERMISSION_GRANTED) {
//             fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
//         } else {
//             ActivityCompat.requestPermissions(getCurrentActivity(),
//                     new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                     PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//         }

//         sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
//         sensorManager.registerListener(this, rotationSensor, SensorManager.SENSOR_DELAY_NORMAL);

//         boolean isStepSensorRegistered = sensorManager.registerListener(this, stepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
//         haveStepSensor.set(isStepSensorRegistered);

//         if (!isStepSensorRegistered) {
//             // Sensor was not successfully registered
//             Log.e("LocationModule", "Step detector sensor could not be registered.");
//         }

//         lastStepUpdateTime.set(System.currentTimeMillis());
//         averageStrideUpdateTime.set(System.currentTimeMillis());
//         dataSendHandler.postDelayed(dataSendRunnable, 2000);
//         noStepHandler.postDelayed(noStepRunnable, 15000);
//     }

//     @ReactMethod
//     public void stopUpdates() {
//         haveStepSensor.set(false);
//         fusedLocationClient.removeLocationUpdates(locationCallback);
//         sensorManager.unregisterListener(this);
//         noStepHandler.removeCallbacks(noStepRunnable);
//         dataSendHandler.removeCallbacks(dataSendRunnable);

//         totalDistance.set(0f);
//         lastLocation.set(null);
//         totalSteps.set(0);
//         tempSteps.set(0);
//         tempDistance.set(0f);
//         lastStepUpdateTime.set(0);
//         lastSpeedUpdateTime.set(0);
//         averageStrideUpdateTime.set(0);
//         averageStride.set(0f);
//         averageSpeed.set(0f);
//         periodStep.set(0);
//         periodAccelStep.set(0);
//         periodDistance.set(0f);
//         // sensorDataBufferRef.set(new ArrayList<>());
//     }

//     @Override
//     public boolean onRequestPermissionsResult(int i, String[] strings, int[] ints) {
//         if (i == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
//             if (ints.length > 0 && ints[0] == PackageManager.PERMISSION_GRANTED) {
//                 startUpdates();
//             }
//             return true;
//         }
//         return false;
//     }
// }
