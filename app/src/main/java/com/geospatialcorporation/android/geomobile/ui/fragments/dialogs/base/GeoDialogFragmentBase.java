package com.geospatialcorporation.android.geomobile.ui.fragments.dialogs.base;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.geospatialcorporation.android.geomobile.library.DI.Analytics.Interfaces.IGeoAnalytics;

public class GeoDialogFragmentBase extends DialogFragment {
    //region Getters & Setters
    public Context getContext() {
        if(mContext == null) {
            mContext = getActivity();
        }

        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    //endregion
    protected Context mContext;

    protected IGeoAnalytics mAnalytics;

    protected AlertDialog.Builder getDialogBuilder(){
        return new AlertDialog.Builder(getContext());
    }

    protected View getDialogView(int layoutId){
        LayoutInflater inflater = LayoutInflater.from(getContext());

        return inflater.inflate(layoutId, null);
    }

    protected void Toaster(String message){
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    public void close() {
        getDialog().cancel();
    }

    public void showKeyboard(final EditText editText){
        (new Handler()).postDelayed(new Runnable() {

            public void run() {

                editText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                editText.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));

            }
        }, 200);
    }

    public void hideKeyBoard(EditText editText){
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
