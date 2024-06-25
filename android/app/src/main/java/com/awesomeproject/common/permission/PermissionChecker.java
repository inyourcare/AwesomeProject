// package com.awesomeproject.common.permission;

// import android.Manifest;
// import android.content.Context;
// import android.content.pm.PackageManager;
// import android.os.Build;

// import androidx.core.content.ContextCompat;

// import com.awesomeproject.common.Config;
// import com.karumi.dexter.Dexter;
// import com.karumi.dexter.MultiplePermissionsReport;
// import com.karumi.dexter.PermissionToken;
// import com.karumi.dexter.listener.DexterError;
// import com.karumi.dexter.listener.PermissionRequest;
// import com.karumi.dexter.listener.PermissionRequestErrorListener;
// import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

// import java.util.ArrayList;
// import java.util.List;

// public class PermissionChecker {
//     private static final String TAG = PermissionChecker.class.getSimpleName();

//     private static PermissionChecker mPermissionChecker = null;

//     private Context mContext = null;
//     private IPermissionCheckListener _cb = null;

//     public static PermissionChecker getInstance(Context context, IPermissionCheckListener _cb) {
//         if (mPermissionChecker == null) {
//             mPermissionChecker = new PermissionChecker(context, _cb);
//         }

//         return mPermissionChecker;
//     }

//     public static PermissionChecker getInstance() {
//         return mPermissionChecker;
//     }

//     private PermissionChecker(Context context, IPermissionCheckListener _cb) {
//         mContext = context;
//         this._cb = _cb;

//         init();
//     }

//     public void setPermissionCheckListener(IPermissionCheckListener _cb) {
//         this._cb = _cb;
//     }

//     private void init() {

//     }

//     public void deInit() {
//         _cb = null;
//         mContext = null;
//     }

//     public void reqPermission() {
//         int osVersion = Build.VERSION.SDK_INT;

//         ArrayList<String> permissions = new ArrayList<>();
//         if (osVersion >= Build.VERSION_CODES.S) {
//             permissions.add(Manifest.permission.BLUETOOTH_SCAN);
//             permissions.add(Manifest.permission.BLUETOOTH_CONNECT);
//             permissions.add(Manifest.permission.ACTIVITY_RECOGNITION);
//             //permissions.add(Manifest.permission.SCHEDULE_EXACT_ALARM);
//             if (osVersion >= Build.VERSION_CODES.TIRAMISU) {
//                 permissions.add(Manifest.permission.POST_NOTIFICATIONS);
//                 permissions.add(Manifest.permission.USE_EXACT_ALARM);
//             }
//             permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
//             permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);

//         } else {
//             permissions.add(Manifest.permission.BLUETOOTH);
//             permissions.add(Manifest.permission.BLUETOOTH_ADMIN);
//             permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
//             permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
//             permissions.add(Manifest.permission.ACTIVITY_RECOGNITION);
//             // permissions.add(Manifest.permission.SCHEDULE_EXACT_ALARM);
            
//         }

//         Dexter.withContext(mContext)
//                 .withPermissions(permissions)
//                 .withListener(new MultiplePermissionsListener() {
//                     @Override
//                     public void onPermissionsChecked(MultiplePermissionsReport report) {
//                         if (report.areAllPermissionsGranted()) {
//                             if (_cb != null) {
//                                 _cb.permissionRequestResult(IPermissionCheckListener.PERMISSION_GRANTED);
//                             }
//                         } else {
//                             if (_cb != null) {
//                                 _cb.permissionRequestResult(IPermissionCheckListener.PERMISSION_ANY_DENIED);
//                             }
//                         }
//                     }

//                     @Override
//                     public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
//                             PermissionToken token) {
//                         token.continuePermissionRequest();
//                     }
//                 })
//                 .withErrorListener(new PermissionRequestErrorListener() {
//                     @Override
//                     public void onError(DexterError error) {
//                         if (_cb != null) {
//                             _cb.permissionRequestResult(IPermissionCheckListener.PERMISSION_REQUEST_ERROR);
//                         }
//                     }
//                 })
//                 .onSameThread()
//                 .check();
//     }

//     public int isPermissionChecked() {
//         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//             if (ContextCompat.checkSelfPermission(mContext,
//                     Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
//                     ContextCompat.checkSelfPermission(mContext,
//                             Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//                 return Config.PERMISSION_DENIED;
//             }
//         } else {
//             if (ContextCompat
//                     .checkSelfPermission(mContext, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
//                     ||
//                     ContextCompat.checkSelfPermission(mContext,
//                             Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
//                     ||
//                     ContextCompat.checkSelfPermission(mContext,
//                             Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//                     ||
//                     ContextCompat.checkSelfPermission(mContext,
//                             Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                 return Config.PERMISSION_DENIED;
//             }
//         }

//         return Config.PERMISSION_GRANTED;
//     }
// }
