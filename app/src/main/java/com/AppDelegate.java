package com;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.constants.Tags;
import com.task_1.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class AppDelegate extends Application {

    public static ProgressDialog mProgressDialog;
    public static AppDelegate mInstance;
    public static AlertDialog.Builder mAlert;
    public static DisplayMetrics displayMetrics;

    public static String checkJsonType(String data) {
        String string = "String";
        Object json = null;
        try {
            json = new JSONTokener(data).nextValue();
            if (json instanceof JSONObject) {
                string = "JSONObject";
            } else if (json instanceof JSONArray) {
                string = "JSONArray";
            }
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
//        AppDelegate.LogT("json type==" + string);
        return string;
    }

    public static boolean isLegalPassword(String pass) {
        if (pass.length() < 6 || pass.length() > 20) return false;
        if (!pass.matches(".*[A-Z].*")) return false;
        if (!pass.matches(".*[a-z].*")) return false;
        if (!pass.matches(".*[0-9].*")) return false;
//        if (!pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*)[a-zA-Z]{6,}$")) return false;
//        if (!pass.matches(".*\\d.*")) return false;
//        if (!pass.matches(".*[~!.......].*")) return false;
        return true;
    }

    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    public static boolean haveNetworkConnection(Context mContext) {
        return haveNetworkConnection(mContext, true);
    }

    public static boolean haveNetworkConnection(Context mContext, boolean showAlert) {
        boolean isConnected = false;
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        if (mContext == null) {
            return false;
        } else {
            ConnectivityManager cm = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile = true;
            }
            isConnected = haveConnectedWifi || haveConnectedMobile;
            if (isConnected) {
                return isConnected;
            } else {
                if (showAlert) {
                    AppDelegate.showToast(mContext, "Please make sure that your device is " +
                            "connected to active internet connection.");
                }
            }
            return isConnected;
        }
    }

    public static void showToast(Context mContext, String Message) {
        try {
            if (mContext != null)
                Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
            else
                Log.e("tag", "context is null at showing toast.");
        } catch (Exception e) {
            Log.e("tag", "context is null at showing toast.", e);
        }
    }

    public static void showFragmentAnimation(FragmentManager fragmentManager,
                                             Fragment fragment, int id) {
        try {
            if (fragmentManager != null && fragment != null)
                fragmentManager
                        .beginTransaction()
                        .replace(id, fragment)
                        .addToBackStack(null).commit();
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
    }

    public static void showToast(Context mContext, String Message, int type) {
        try {
            if (mContext != null)
                if (type == 0)
                    Toast.makeText(mContext, Message, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(mContext, Message, Toast.LENGTH_LONG).show();
            else
                Log.e("tag", "context is null at showing toast.");
        } catch (Exception e) {
            Log.e("tag", "context is null at showing toast.", e);
        }
    }

//    public static void callNumber(Activity mActivity, String number) {
//        try {
//            if (AppDelegate.isValidString(number)) {
//                Intent intent = new Intent(Intent.ACTION_CALL);
//                intent.setData(Uri.parse("tel:" + number));
//                mActivity.startActivity(intent);
//            }
//        } catch (Exception e) {
//            AppDelegate.LogE(e);
//        }
//    }

    public static void sendEmail(Activity mActivity, String mail_id, String subject, String extra) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", mail_id, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, extra);

        String[] hello = new String[1];
        // hello[0] = "adavies@crystalglass.ca";
        // emailIntent.putExtra(Intent.EXTRA_EMAIL, hello);

        mActivity.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    public static void showProgressDialog(Activity mContext) {
        showProgressDialog(mContext, "", "Loading...");
    }

    public static void showProgressDialog(Activity mContext, String mTitle,
                                          String mMessage) {
        AppDelegate.hideKeyBoard(mContext);
        try {
            if (mContext != null) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    return;
                }
                mProgressDialog = new ProgressDialog(mContext);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setTitle(mTitle);
                mProgressDialog.setMessage(mMessage);
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog.show();
                } else {
                    mProgressDialog.show();
                }
            } else {
                Log.d("tag", "showProgressDialog instense is null");
            }
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }

    }

    public static void hideProgressDialog(Context mContext) {
        try {
            if (mContext != null) {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                } else {
                    mProgressDialog = new ProgressDialog(mContext);
                    mProgressDialog.dismiss();
                }
            } else {
                Log.d("tag", " hide instense is null");
            }
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
    }

    public static void showFragment(FragmentActivity mContext,
                                    Fragment mFragment, int fragmentId, Bundle mBundle, String TAG) {

        if (mBundle != null && mContext != null)
            mFragment.setArguments(mBundle);
        try {
            FragmentTransaction mFragmentTransaction = mContext
                    .getSupportFragmentManager()
                    .beginTransaction();


            mFragmentTransaction.replace(fragmentId, mFragment, TAG)
                    .addToBackStack(TAG).commitAllowingStateLoss();
            hideKeyBoard(mContext);
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
    }

    /**
     * Method to Hide Soft Input Keyboard
     *
     * @param mContext
     * @param view
     */

    public static void HideKeyboardMain(Activity mContext, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            // R.id.search_img
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            //Utility.debug(1, TAG, "Exception in executing HideKeyboardMain()");
            AppDelegate.LogE(e);
        }
    }

    public static void openKeyboard(Activity mActivity, View view) {
        try {
            final InputMethodManager imm = (InputMethodManager) mActivity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
    }

    /**
     * @param TAG
     * @param Message
     * @param LogType
     */
    public static void Log(String TAG, String Message, int LogType) {
        switch (LogType) {
            // Case 1- To Show Message as Debug
            case 1:
                Log.d(TAG, Message);
                break;
            // Case 2- To Show Message as Error
            case 2:
                Log.e(TAG, Message);
                break;
            // Case 3- To Show Message as Information
            case 3:
                Log.i(TAG, Message);
                break;
            // Case 4- To Show Message as Verbose
            case 4:
                Log.v(TAG, Message);
                break;
            // Case 5- To Show Message as Assert
            case 5:
                Log.w(TAG, Message);
                break;
            // Case Default- To Show Message as System Print
            default:
                System.out.println(Message);
                break;
        }
    }

    public static void Log(String TAG, String Message) {
        AppDelegate.Log(TAG, Message, 1);
    }

    /* Function to show log for error message */
    public static void LogD(String Message) {
        AppDelegate.Log(Tags.DATE, "" + Message, 1);
    }

    /* Function to show log for error message */
    public static void LogDB(String Message) {
        AppDelegate.Log(Tags.DATABASE, "" + Message, 1);
    }

    /* Function to show log for error message */
    public static void LogE(Exception e) {
        if (e != null) {
            AppDelegate.LogE(e.getMessage());
            e.printStackTrace();
        } else {
            AppDelegate.Log(Tags.ERROR, "exception object is also null.", 2);
        }
    }

    /* Function to show log for error message */
    public static void LogE(OutOfMemoryError e) {
        if (e != null) {
            AppDelegate.LogE(e.getMessage());
            e.printStackTrace();
        } else {
            AppDelegate.Log(Tags.ERROR, "exception object is also null.", 2);
        }
    }

    /* Function to show log for error message */
    public static void LogE(String message, Exception exception) {
        if (exception != null) {
            AppDelegate.LogE("from = " + message + " => "
                    + exception.getMessage());
            exception.printStackTrace();
        } else {
            AppDelegate.Log(Tags.ERROR, "exception object is also null. at "
                    + message, 2);
        }
    }

    /**
     * Funtion to log with tag RESULT, you need to just pass the message.
     *
     * @String Message = pass your message that you want to log.
     */
    public static void LogR(String Message) {
        AppDelegate.Log(Tags.RESULT, "" + Message, 1);
    }

    /**
     * Funtion to log with tag RESULT, you need to just pass the message.
     *
     * @String Message = pass your message that you want to log.
     */
    public static void LogI(String Message) {
        AppDelegate.Log(Tags.INTERNET, "" + Message, 1);
    }

    /**
     * Funtion to log with tag ERROR, you need to just pass the message. This
     * method is used to exeception .
     *
     * @String Message = pass your message that you want to log.
     */
    public static void LogE(String Message) {
        AppDelegate.Log(Tags.ERROR, "" + Message, 2);
    }

    /**
     * Funtion to log with tag your name, you need to just pass the message.
     * This method is used to log url of your api calling.
     *
     * @String Message = pass your message that you want to log.
     */
    public static void LogB(String Message) {
        AppDelegate.Log(Tags.BHARAT, "" + Message, 1);
    }

    /**
     * Funtion to log with tag URL, you need to just pass the message. This
     * method is used to log url of your api calling.
     *
     * @String Message = pass your message that you want to log.
     */
    public static void LogU(String Message) {
        AppDelegate.Log(Tags.URL, "" + Message, 1);
    }

    /**
     * Funtion to log with tag URL_API, you need to just pass the message. This
     * method is used to log url of your api calling.
     *
     * @String Message = pass your message that you want to log.
     */
    public static void LogUA(String Message) {
        AppDelegate.Log(Tags.URL_API, "" + Message, 1);
    }

    /**
     * Funtion to log with tag URL_POST, you need to just pass the message. This
     * method is used to log post param of your api calling.
     *
     * @String Message = pass your message that you want to log.
     */
    public static void LogUP(String Message) {
        AppDelegate.Log(Tags.URL_POST, "" + Message, 1);
    }

    /**
     * Funtion to log with tag URL_RESPONSE, you need to just pass the message.
     * This method is used to log response of your api calling.
     *
     * @String Message = pass your message that you want to log.
     */
    public static void LogUR(String Message) {
        AppDelegate.Log(Tags.URL_RESPONSE, "URL_RESPONSE " + Message, 1);
    }

    /**
     * Funtion to log with tag TEST, you need to just pass the message.
     *
     * @String Message = pass your message that you want to log.
     */
    public static void LogT(String Message) {
        AppDelegate.Log(Tags.TEST, "" + Message, 1);
    }

    /**
     * Funtion to log with tag TEST, you need to just pass the message.
     *
     * @String Message = pass your message that you want to log.
     */
    public static void LogPN(String Message) {
        AppDelegate.Log(Tags.PUBNUB, "" + Message, 1);
    }

    /**
     * Funtion to log with tag TEST, you need to just pass the message.
     *
     * @String Message = pass your message that you want to log.
     */
    public static void LogCh(String Message) {
        AppDelegate.Log("check", "" + Message, 1);
    }

    public static void LogTR(String Message) {
        AppDelegate.Log(Tags.TRACKER, "" + Message, 1);
    }

    /**
     * Funtion to log with tag TEST, you need to just pass the message.
     *
     * @String Message = pass your message that you want to log.
     */
    public static void LogF(String Message) {
        AppDelegate.Log(Tags.FORMATED, "" + Message, 1);
    }

    /**
     * Funtion to log with tag TEST, you need to just pass the message.
     *
     * @String Message = pass your message that you want to log.
     */
    public static void LogFB(String Message) {
        AppDelegate.Log(Tags.FACEBOOK, "" + Message, 1);
    }

    /**
     * Funtion to log with tag TEST, you need to just pass the message.
     *
     * @String Message = pass your message that you want to log.
     */
    public static void LogS(String Message) {
        AppDelegate.Log(Tags.SERVICE, "" + Message, 1);
    }

    /**
     * Funtion to log with tag TEST, you need to just pass the message.
     *
     * @Message = pass your message that you want to log.
     * @int type = you need to pass int value to print in different color. 0 =
     * default color; 1 = fro print in exception style in red color; 2 =
     * info style in orange color;
     */
    public static void LogT(String Message, int type) {
        AppDelegate.Log(Tags.TEST, "" + Message, type);
    }

    /**
     * Funtion to log with tag PREF, you need to just pass the message.
     *
     * @String Message = pass your message that you want to log.
     */
    public static void LogP(String Message) {
        AppDelegate.Log(Tags.PREF, "" + Message, 1);
    }

    /**
     * Funtion to log with tag PREF, you need to just pass the message.
     *
     * @String Message = pass your message that you want to log.
     */
    public static void LogPU(String Message) {
        AppDelegate.Log(Tags.PUSHER, "" + Message, 1);
    }

    /**
     * Funtion to log with tag GCM, you need to just pass the message.
     *
     * @String Message = pass your message that you want to log.
     */
    public static void LogGC(String Message) {
        AppDelegate.Log(Tags.GCM, "" + Message, 1);
    }

    /**
     * Funtion to log with tag Chat, you need to just pass the message.
     *
     * @String Message = pass your message that you want to log.
     */
    public static void LogC(String Message) {
        AppDelegate.Log(Tags.CHAT, "" + Message, 1);
    }


    /**
     * Funtion to log with tag GPS, you need to just pass the message.
     *
     * @String Message = pass your message that you want to log.
     */
    public static void LogGP(String Message) {
        AppDelegate.Log(Tags.GPS, "" + Message, 1);
    }


    public static void timeoutalert(Context context) {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

            alertDialog.setTitle("Error");
            alertDialog.setMessage("Connection TimeOut");
            alertDialog.setCancelable(false);

            alertDialog.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            alertDialog.show();
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
    }

    public static boolean checkEmptyString(String xyz) {
        if (xyz == null || xyz.trim().equalsIgnoreCase(""))
            return true;
        else
            return false;
    }


    public static boolean isContainAlpha(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if (Character.isLetter(c))
                return true;
        }
        return false;
    }

    public static void hideKeyBoard(final Activity mActivity, long timeAfter) {
        if (mActivity != null)
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    AppDelegate.hideKeyBoard(mActivity);
                }
            }, timeAfter);
    }

    public static void hideKeyBoard(Activity mActivity) {
        if (mActivity == null)
            return;
        else {
            InputMethodManager inputManager = (InputMethodManager) mActivity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            // check if no view has focus:
            View v = mActivity.getCurrentFocus();
            if (v == null)
                return;

            AppDelegate.LogT("hideKeyBoard viewNot null");
            inputManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static int convertdp(Context context, int x) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(x * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    /**
     * it will true if your device having mounted SD card else false
     */
    public static boolean isSDcardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getHashKey(Context mContext) {
        String str_HashKey = null;
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                str_HashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                AppDelegate.LogT("HashKey = " + str_HashKey);
            }
        } catch (PackageManager.NameNotFoundException e) {
            AppDelegate.LogE(e);
        } catch (NoSuchAlgorithmException e) {
            AppDelegate.LogE(e);
        }
        return str_HashKey;
    }

    /*
     * Masking of an image
	 */
    public static Bitmap masking(Bitmap original, Bitmap mask) {
        original = Bitmap.createScaledBitmap(original, mask.getWidth(),
                mask.getHeight(), true);

        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(original, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);
        return result;
    }

    /* convert drawable to bitmap */
    public static Bitmap integerToBitmap(Context ctx, Integer integer) {

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inScaled = false;
        o.inJustDecodeBounds = false;
        Bitmap icon = BitmapFactory.decodeResource(ctx.getResources(), integer,
                o);
        icon.setDensity(Bitmap.DENSITY_NONE);
        return icon;
    }

    /*
     * Reduce image size
     */
    public static Bitmap Shrinkmethod(String file, int width, int height) {
        BitmapFactory.Options bitopt = new BitmapFactory.Options();
        bitopt.inJustDecodeBounds = true;
        Bitmap bit = BitmapFactory.decodeFile(file, bitopt);

        int h = (int) Math.ceil(bitopt.outHeight / (float) height);
        int w = (int) Math.ceil(bitopt.outWidth / (float) width);

        if (h > 1 || w > 1) {
            if (h > w) {
                bitopt.inSampleSize = h;

            } else {
                bitopt.inSampleSize = w;
            }
        }
        bitopt.inJustDecodeBounds = false;
        bit = BitmapFactory.decodeFile(file, bitopt);

        System.out.println("HEIGHT WIDTH:::::::" + bit.getWidth() + "::::"
                + bit.getHeight());

        return bit;

    }

    public static synchronized AppDelegate getInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = (AppDelegate) mContext.getApplicationContext();
        }
        return mInstance;
    }

    public static String getUUID(Context mContext) {
        TelephonyManager tManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        AppDelegate.LogD("getUUID = " + tManager.getDeviceId());
        return tManager.getDeviceId();
//        return "359774050367819";
    }

    public static String getBase64String(String value) {
        byte[] data;
        String str_base64 = "";
        String str = "xyzstring";
        try {
            data = str.getBytes("UTF-8");
            str_base64 = Base64.encodeToString(data, Base64.DEFAULT);
            Log.i("Base 64 ", str_base64);
        } catch (UnsupportedEncodingException e) {
            AppDelegate.LogE(e);
        }
        return str_base64;
    }

    public static boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            AppDelegate.LogE(pe);
            return false;
        }
        return true;
    }

    public static String getFormatedAddress(String str_value_1, String str_value_2) {
        String st_value = "";
        if (isValidString(str_value_1) && isValidString(str_value_2)) {
            st_value = str_value_1 + " - " + str_value_2;
        } else if (isValidString(str_value_1)) {
            st_value = str_value_1;
        } else if (isValidString(str_value_2)) {
            st_value = str_value_2;
        }
        return st_value;
    }

    public static boolean isValidString(String string) {
        if (string != null && !string.equalsIgnoreCase("null") && string.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static int getValidInt(String string) {
        if (string != null && !string.equalsIgnoreCase("null") && string.length() > 0) {
            try {
                return Integer.parseInt(string);
            } catch (Exception e) {
                AppDelegate.LogE(e);
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static boolean isValidDouble(String string) {
        try {
            if (string != null && !string.equalsIgnoreCase("null") && string.length() > 0 && Double.parseDouble(string) > 1) {
                return true;
            } else {
                AppDelegate.LogT("isValidDouble false => " + string);
                return false;
            }
        } catch (Exception e) {
            AppDelegate.LogE(e);
            return false;
        }
    }

    //
    public static void showAlert(Context mContext, String Title, String
            Message) {
        try {
            mAlert = new AlertDialog.Builder(mContext);
            mAlert.setCancelable(false);
            mAlert.setTitle(Title);
            mAlert.setMessage(Message);
            mAlert.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            mAlert.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            mAlert.show();
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
    }

    public static void showAlert(Context mContext, String Message) {
        showAlert(mContext, "", Message, "OK");
    }

    public static void showAlert(Context mContext, String Title, String
            Message, String str_button_name) {
        try {
            mAlert = new AlertDialog.Builder(mContext);
            mAlert.setCancelable(false);
            mAlert.setTitle(Title);
            mAlert.setMessage(Html.fromHtml(Message));
            mAlert.setPositiveButton(
                    str_button_name,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            mAlert.show();
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
    }

    public static void showAlert(Context mContext, String Title, String
            Message, String str_button_name, final View.OnClickListener onClickListener) {
        try {
            mAlert = new AlertDialog.Builder(mContext);
            mAlert.setCancelable(false);
            mAlert.setTitle(Title);
            mAlert.setMessage(Message);
            mAlert.setPositiveButton(
                    str_button_name,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (onClickListener != null) {
                                onClickListener.onClick(null);
                            }
                            dialog.dismiss();
                        }
                    });
            mAlert.show();
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
    }

    public static void showAlert(Context mContext, String Title, String
            Message, String str_button_name, final View.OnClickListener onClickListener, String str_button_name_1, final View.OnClickListener onClickListener1) {
        try {
            mAlert = new AlertDialog.Builder(mContext);
            mAlert.setCancelable(false);
            mAlert.setTitle(Title);
            mAlert.setMessage(Message);
            mAlert.setPositiveButton(
                    str_button_name,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (onClickListener != null) {
                                onClickListener.onClick(null);
                            }
                            dialog.dismiss();
                        }
                    });

            mAlert.setNegativeButton(
                    str_button_name_1,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (onClickListener1 != null) {
                                onClickListener1.onClick(null);
                            }
                            dialog.dismiss();
                        }
                    });
            mAlert.show();
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
    }

    public static void lanchAppIfInstalled(Context mContext,
                                           String appPackageName) {
        try {
            mContext.startActivity(mContext.getPackageManager()
                    .getLaunchIntentForPackage(appPackageName));
        } catch (ActivityNotFoundException anfe) {
            AppDelegate.LogE(anfe);
            try {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("market://details?id=" + appPackageName)));
            } catch (Exception e) {
                AppDelegate.LogE(e);
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("https://play.google.com/store/apps/details?id="
                                + appPackageName)));
            }
        } catch (Exception e) {
            AppDelegate.LogE(e);
            try {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("market://details?id=" + appPackageName)));
            } catch (Exception e1) {
                AppDelegate.LogE(e1);
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("https://play.google.com/store/apps/details?id="
                                + appPackageName)));
            }
        }
    }

    public static void openInPlayStore(Context mContext) {
        try {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mContext.getPackageName())));
        } catch (ActivityNotFoundException anfe) {
            AppDelegate.LogE(anfe);
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
        }
    }


    public static void shareApplicationUrl(Context mContext, String str_share_content) {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.app_name));
            String sAux = "\n" + str_share_content + "\n\n";
            sAux = sAux + "https://play.google.com/store/apps/details?id=" + mContext.getPackageName() + " \n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            mContext.startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
    }

    public static int dpToPix(Context mContext, int value) {
        int calculatedValue = value;
        try {
            if (mContext != null)
                calculatedValue = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, mContext.getResources().getDisplayMetrics()));
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
        return calculatedValue;

    }

    public static float pixToDP(Context mContext, int value) {
        Resources r = mContext.getResources();
        float calculatedValue = value;
        try {
            calculatedValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, r.getDisplayMetrics());
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
        return calculatedValue;

    }


    public static int getFileSize(File file) {
        try {
            return Integer.parseInt(String.valueOf(file.length() / 1024));
        } catch (Exception e) {
            AppDelegate.LogE(e);
            return 0;
        }
    }

    /**
     * reduces the size of the image
     *
     * @param image
     * @param maxSize
     * @return
     */
    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH
        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    public static void setListViewHeight(Context mContext, ListView listView, ListAdapter gridAdapter, View view) {
        try {
            if (gridAdapter == null) {
                // pre-condition
                AppDelegate.LogE("Adapter is null");
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = 80;
                listView.setLayoutParams(params);
                listView.requestLayout();
                return;
            }
            int totalHeight = 0;
            int itemCount = gridAdapter.getCount();

            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                    View.MeasureSpec.AT_MOST);
            for (int i = 0; i < itemCount; i++) {
                View listItem = gridAdapter.getView(i, null, listView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
            }
            if (view != null && view.getVisibility() == View.VISIBLE) {
                totalHeight += AppDelegate.dpToPix(mContext, 60);
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
    }

    public static void setListViewHeight(Context mContext, ListView listView, ListAdapter gridAdapter) {
        try {
            if (gridAdapter == null) {
                // pre-condition
                AppDelegate.LogE("Adapter is null");
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = 80;
                listView.setLayoutParams(params);
                listView.requestLayout();
                return;
            }
            int totalHeight = 0;
            int itemCount = gridAdapter.getCount();

            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                    View.MeasureSpec.AT_MOST);
            for (int i = 0; i < itemCount; i++) {
                View listItem = gridAdapter.getView(i, null, listView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
    }

    public static void setGridViewHeight(Context mContext, GridView listView, ListAdapter gridAdapter, int itemHeight) {
        try {
            if (gridAdapter == null) {
                // pre-condition
                AppDelegate.LogE("Adapter is null");
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                params.height = 80;
                listView.setLayoutParams(params);
                listView.requestLayout();
                return;
            }
            int totalHeight = 0;
            int itemCount = gridAdapter.getCount();
            if ((itemCount % 2) == 0) {
            } else {
                itemCount += 1;
            }
            itemCount = itemCount / 2;
            for (int i = 0; i < itemCount; i++) {
                totalHeight += AppDelegate.dpToPix(mContext, itemHeight);
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
    }

    public static void setListViewHeightNull(ListView listView) {
        try {
            int itemCount = 0;
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = itemCount;
            listView.setLayoutParams(params);
            listView.requestLayout();
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
    }

    public static String getNewFile(Context mContext) {
        File directoryFile, capturedFile;
        if (AppDelegate.isSDcardAvailable()) {
            directoryFile = new File(Environment.getExternalStorageDirectory()
                    + "/" + mContext.getResources().getString(R.string.app_name));
        } else {
            directoryFile = mContext.getDir(mContext.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        }
        if (directoryFile.exists() && directoryFile.isDirectory()
                || directoryFile.mkdirs()) {
            capturedFile = new File(directoryFile, "Image_" + System.currentTimeMillis()
                    + ".png");
            try {
                if (capturedFile.createNewFile()) {
                    AppDelegate.LogT("File created = "
                            + capturedFile.getAbsolutePath());
                    return capturedFile.getAbsolutePath();
                }
            } catch (IOException e) {
                AppDelegate.LogE(e);
            }
        }
        AppDelegate.LogE("no file created.");
        return null;
    }

    public static void openGallery(Activity mActivity, int SELECT_PICTURE) {
        mActivity.startActivityForResult(new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI), SELECT_PICTURE);
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "CropTitle", null);
        return Uri.parse(path);
    }

    public static String getFilterdUrl(String str_url) {
        if (str_url != null && str_url.length() > 0) {
            str_url = str_url.replace("[", "%5B");
            str_url = str_url.replace("@", "%40");
            str_url = str_url.replace(" ", "%20");
        }
        return str_url;
    }

    public static void showKeyboard(final Context mContext, final EditText editText) {
        if (editText != null && mContext != null) {
            editText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    editText.requestFocus();
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                }
            }, 100);
        }
    }

    public static void disableButton(View view) {
        disableButton(view, 200);
    }

    public static void disableButton(final View view, long delayTime) {
        view.setEnabled(false);
        view.setClickable(false);
        new Handler().postAtTime(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
                view.setClickable(true);
            }
        }, delayTime);
    }

    public static String getcurrenttime() {
        try {
            Date date1 = Calendar.getInstance().getTime();
            DateFormat dateFormater = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");  //("yyyy-MM-dd HH:mm:ss"); (format)Sat, 16 Jan 2016 11:55:23 GMT

            TimeZone gmtTime = TimeZone.getTimeZone("GMT+00");
            dateFormater.setTimeZone(gmtTime);
            String newgmtdate = dateFormater.format(date1);

            newgmtdate = newgmtdate.substring(0, newgmtdate.indexOf("+") != -1 ? newgmtdate.indexOf("+") : newgmtdate.length());
            Log.d("currentLocalTime", " date " + newgmtdate);

            return newgmtdate;
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
        return "";
    }


    public static int getDeviceWith(Context mContext) {
        if (displayMetrics == null)
            displayMetrics = mContext.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
//        AppDelegate.LogT("getDeviceWidth = " + width);
        return width;
    }

    public static int getDeviceHeight(Context mContext) {
        if (displayMetrics == null)
            displayMetrics = mContext.getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
//        AppDelegate.LogT("getDeviceHeight = " + height);
        return height;
    }

    public static Bitmap loadBitmap(Context context, String picName) {
        Bitmap b = null;
        FileInputStream fis;
        try {
            fis = context.openFileInput(picName);
            b = BitmapFactory.decodeStream(fis);
            fis.close();

        } catch (FileNotFoundException e) {
            AppDelegate.LogE(e);
        } catch (IOException e) {
            AppDelegate.LogE(e);
        }
        return b;
    }

    public static void saveFile(Context context, Bitmap b, String picName) {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(picName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            AppDelegate.showToast(context, "Image saved BitmapImageViewTarget called " + picName);
        } catch (FileNotFoundException e) {
            AppDelegate.LogE(e);
        } catch (IOException e) {
            AppDelegate.LogE(e);
        }
    }

    public static String getValidDatePostFixFormate(String string) {
        if (string.endsWith("1") && !string.endsWith("11"))
            return "st";
        else if (string.endsWith("2") && !string.endsWith("12"))
            return "nd";
        else if (string.endsWith("3") && !string.endsWith("13"))
            return "rd";
        else
            return "th";
    }

    public static String getDecodedStringMessage(String string) {
        if (string.contains("\\n") || string.contains("\n")) {
            string = string.replace("\\n", System.getProperty("line.separator"));
            string = string.replace("\n", System.getProperty("line.separator"));
        }
        string = string.replaceAll("%20", " ");
        string = string.replaceAll("%29", "'");
        return string;
    }

    public static String getEncodedStringMessage(String string) {
        if (string.contains(System.getProperty("line.separator"))) {
            AppDelegate.LogT("list.seperator is present");
            string = string.replaceAll(System.getProperty("line.separator"), "%5Cn");
        }
        string = string.replaceAll(" ", "%20");
        string = string.replaceAll("'", "%29");
        return string;
    }

    public static String getDateForChat(String long_date) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTimeInMillis(Long.parseLong(long_date));
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
        return new SimpleDateFormat("dd MMMM yyyy").format(calendar.getTime());
    }

    public static String getTimeForChat(String long_date) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTimeInMillis(Long.parseLong(long_date));
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
        return new SimpleDateFormat("hh:mm aa").format(calendar.getTime());
    }

    public static String getTimeForchat(Date dateObject) {
        Calendar now = Calendar.getInstance();
        Calendar then = Calendar.getInstance();
        now.setTime(new Date());
        then.setTime(dateObject);

        /*finding the system time zone and getting the difference in miliseconds*/
        TimeZone tz = TimeZone.getDefault();
        tz.getRawOffset();
        AppDelegate.Log("timeZone", "" + tz.getRawOffset());

        // Get the represented date in milliseconds
        long nowMs = now.getTimeInMillis();
        long thenMs = then.getTimeInMillis();

        // Calculate difference in milliseconds
//        long diff = nowMs - thenMs ;
        long diff = Math.abs(nowMs - (thenMs + tz.getRawOffset()));

        // Calculate difference in seconds
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        long diffDays = diff / (24 * 60 * 60 * 1000);

//        AppDelegate.LogT("getDateDifference " + then.getTime() + ", diffMinutes = " + diffMinutes + ", diffHours = " + diffHours + ", diffDays = " + diffDays);
        if (diffMinutes < 60) {
            if (diffMinutes == 1)
                return diffMinutes + " min";
            else
                return diffMinutes + " mins";
        } else if (diffHours < 24) {
            if (diffHours == 1)
                return diffHours + " hour";
            else
                return diffHours + " hours";
        } else if (diffDays < 30) {
            if (diffDays == 1)
                return diffDays + " day";
            else
                return diffDays + " days";
        } else {
            return "months";
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    public static void set_locale(Activity mActivity, String lan) {
        Log.d("test", "set_locale => " + lan);
        if (mActivity != null) {
            Locale locale = new Locale(lan);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            mActivity.getBaseContext().getResources().updateConfiguration(config, mActivity.getBaseContext().getResources().getDisplayMetrics());
        } else {
            AppDelegate.LogE("set locale => activity context is null for lan = " + lan);
        }
    }

    public static void setStictModePermission() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
    }

    public static Bitmap getBitmapFromBase64(String encodedImage) {
        Bitmap decodedByte = null;
        try {
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            AppDelegate.LogE(e);
        }
        return decodedByte;
    }


}
