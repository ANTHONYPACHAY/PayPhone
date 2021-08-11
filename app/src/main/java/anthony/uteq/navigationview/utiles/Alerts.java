package anthony.uteq.navigationview.utiles;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/*import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialog;
import com.bestsoft32.tt_fancy_gif_dialog_lib.TTFancyGifDialogListener;*/
import com.bumptech.glide.Glide;

import anthony.uteq.navigationview.R;
//import cn.pedant.SweetAlert.SweetAlertDialog;

public class Alerts {
    private static Dialog mLoadingDialog;

    public static void MessageToast(Context ctx, String message){
        Toast toast= Toast.makeText(ctx, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void LoadingDialog(Context context) {
        // Get the view

        mLoadingDialog = new Dialog(context);
        //mLoadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mLoadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = LayoutInflater.from(context).inflate(R.layout.loading, null);

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.container_loading);
        ImageView gifView = view.findViewById(R.id.git_loading);
        Glide.with(context).load(R.drawable.rueda).into(gifView);


        mLoadingDialog.setContentView(view);


        //dialog.show();

        /*mLoadingDialog = new Dialog(context);
        // Setting the return key is invalid
        mLoadingDialog.setCancelable(false);

        mLoadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));*/
    }

    public static void showLoading(){
        if (mLoadingDialog!=null) {
            mLoadingDialog.show();
        }else{
            MyLogs.error("No se ha ininicalizado el swal");
        }
    }

    public static void closeLoading(){
        if (mLoadingDialog!=null) {
            mLoadingDialog.dismiss();
            mLoadingDialog=null;
        }
    }

    /*public static void loading(Activity ctx){
        new TTFancyGifDialog.Builder(ctx)
                .setTitle(String.valueOf(R.string.loading_tittle))
                .setMessage(String.valueOf(R.string.loading_description))
                .setPositiveBtnText("Ok")
                .setPositiveBtnBackground("#22b573")
                .setGifResource(R.drawable.gatito)      //pass your gif, png or jpg
                .isCancellable(true)
                .OnPositiveClicked(new TTFancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        Toast.makeText(ctx,"Ok",Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
    }*/

    /*public static SweetAlertDialog swal_loading(Context ctx){
        SweetAlertDialog pDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#4285F4"));
        pDialog.setTitleText("Por favor, espere.");
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
    }
    public static SweetAlertDialog swal_all(Context ctx, int status, String tittle, String info) {
        int state = 0;
        switch (state){
            case 1:
                state =  SweetAlertDialog.NORMAL_TYPE;
            case 2:
                state =  SweetAlertDialog.SUCCESS_TYPE;
            case 3:
                state =  SweetAlertDialog.WARNING_TYPE;
            case 4:
                state =  SweetAlertDialog.ERROR_TYPE;
        };
        SweetAlertDialog swal =  new SweetAlertDialog(ctx, state)
                .setTitleText(tittle)
                .setContentText(info)
                .setConfirmText("Aceptar");
        swal.show();
        return swal;
    }*/
}
