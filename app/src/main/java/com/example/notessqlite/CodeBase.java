package com.example.notessqlite;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

public class CodeBase {
    public static void showToast(Context context,String message,int image){
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.toast_message,null);
        TextView toastTextView = layout.findViewById(R.id.toastMessage);
        Typeface fontFamily = ResourcesCompat.getFont(context,R.font.indie_flower);
        ImageView toastImageView = layout.findViewById(R.id.toast_image);

        toastImageView.setImageResource(image);

        toastTextView.setTypeface(fontFamily);
        toastTextView.setText(message);

        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();


    }
}
