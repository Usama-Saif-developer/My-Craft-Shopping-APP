package com.example.craft_shoping;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePasswordFragment extends Fragment {

    private EditText OldPassword, newPassword, confirmedNewPassword;
    private Button updatepasswordBtn;
    private String email;
    private Dialog loadingDialog;

    public UpdatePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_password, container, false);

        OldPassword = view.findViewById(R.id.old_password);
        newPassword = view.findViewById(R.id.new_password);
        confirmedNewPassword = view.findViewById(R.id.confirm_new_password);
        updatepasswordBtn = view.findViewById(R.id.update_password_btn);

        ///loading Dailog//////
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dailog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ///loading Dailog//////

        email = getArguments().getString("Email");

        OldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("NewApi")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        confirmedNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkinputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        updatepasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailandPassword();
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkinputs() {
        if (!TextUtils.isEmpty(OldPassword.getText()) && OldPassword.length() >= 8) {
            if (!TextUtils.isEmpty(newPassword.getText()) && newPassword.length() >= 8) {
                if (!TextUtils.isEmpty(confirmedNewPassword.getText()) && confirmedNewPassword.length() >= 8) {
                    updatepasswordBtn.setEnabled(true);
                    updatepasswordBtn.setTextColor(Color.rgb(255, 255, 255));
                } else {
                    updatepasswordBtn.setEnabled(false);
                    updatepasswordBtn.setTextColor(Color.argb(50f, 255, 255, 255));
                }
            } else {
                updatepasswordBtn.setEnabled(false);
                updatepasswordBtn.setTextColor(Color.argb(50f, 255, 255, 255));
            }
        } else {
            updatepasswordBtn.setEnabled(false);
            updatepasswordBtn.setTextColor(Color.argb(50f, 255, 255, 255));
        }
    }

    private void checkEmailandPassword() {

        Drawable customerroricon = getResources().getDrawable(R.mipmap.warning);
        customerroricon.setBounds(0, 0, customerroricon.getIntrinsicWidth(), customerroricon.getIntrinsicHeight());

        if (newPassword.getText().toString().equals(confirmedNewPassword.getText().toString())) {

            loadingDialog.show();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            AuthCredential credential = EmailAuthProvider
                    .getCredential(email, OldPassword.getText().toString());
            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(newPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            OldPassword.setText(null);
                                            newPassword.setText(null);
                                            confirmedNewPassword.setText(null);
                                            getActivity().finish();
                                            Toast.makeText(getContext(), "Password Updated Successfylly!", Toast.LENGTH_SHORT).show();
                                        }else{
                                            String error=task.getException().getMessage();
                                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                        }
                                        loadingDialog.dismiss();
                                    }
                                });
                            }else {
                                loadingDialog.dismiss();
                                String error=task.getException().getMessage();
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
        } else {
            confirmedNewPassword.setError("Invalid Email!", customerroricon);

        }
    }
}