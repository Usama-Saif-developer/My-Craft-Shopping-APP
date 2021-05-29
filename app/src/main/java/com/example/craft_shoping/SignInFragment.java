package com.example.craft_shoping;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static boolean onResetPasswordFragment = false;
    private TextView donthaveAnAccount;
    private FrameLayout parentframelayout;

    // public static boolean onResetPaaawordFragment=false;
    private EditText sign_in_email;
    private EditText sign_in_password;

    private TextView sign_in_forgot_password;

    private ProgressBar sign_in_progress_bar;


    private ImageButton sign_in_close_btn;
    private Button sign_in_btn;

    private FirebaseAuth firebaseAuth;

    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";
    public static boolean disableCloseBtn = false;

    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        donthaveAnAccount = view.findViewById(R.id.dont_have_an_account);
        parentframelayout = getActivity().findViewById(R.id.register_framelaout);

        sign_in_forgot_password = view.findViewById(R.id.sign_in_forgot_password);
        sign_in_email = view.findViewById(R.id.sign_in_email);
        sign_in_password = view.findViewById(R.id.sign_in_password);

        sign_in_progress_bar = view.findViewById(R.id.sign_in_progress_bar);

        sign_in_close_btn = view.findViewById(R.id.sign_in_close_btn);
        sign_in_btn = view.findViewById(R.id.cancel_btn);

        firebaseAuth = FirebaseAuth.getInstance();

        if (disableCloseBtn) {
            sign_in_close_btn.setVisibility(View.GONE);
        } else {
            sign_in_close_btn.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        donthaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }
        });
        sign_in_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });
        sign_in_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetPasswordFragment = true;
                setFragment(new ResetPasswordFragment());
            }
        });
        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });
        sign_in_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sign_in_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailAndPassword();
            }
        });
    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentframelayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(sign_in_email.getText())) {
            if (!TextUtils.isEmpty(sign_in_password.getText())) {
                sign_in_btn.setEnabled(true);
                sign_in_btn.setTextColor(Color.rgb(255, 255, 255));

            } else {
                sign_in_btn.setEnabled(false);
                sign_in_btn.setTextColor(Color.argb(50, 255, 255, 255));
            }

        } else {
            sign_in_btn.setEnabled(false);
            sign_in_btn.setTextColor(Color.argb(50, 255, 255, 255));

        }
    }

    private void checkEmailAndPassword() {
        if (sign_in_email.getText().toString().matches(emailpattern)) {
            if (sign_in_password.length() >= 8) {
                sign_in_progress_bar.setVisibility(View.VISIBLE);
                sign_in_btn.setEnabled(false);
                sign_in_btn.setTextColor(Color.argb(50, 255, 255, 255));


                firebaseAuth.signInWithEmailAndPassword(sign_in_email.getText().toString(), sign_in_password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    mainIntent();
                                } else {
                                    sign_in_progress_bar.setVisibility(View.INVISIBLE);
                                    sign_in_btn.setEnabled(true);
                                    sign_in_btn.setTextColor(Color.rgb(255, 255, 255));

                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            } else {
                Toast.makeText(getActivity(), "Incorrect email or password!", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getActivity(), "Incorrect email or password!", Toast.LENGTH_SHORT).show();

        }
    }

    private void mainIntent() {
        if (disableCloseBtn){
            disableCloseBtn=false;
        }else {
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
        }
        getActivity().finish();

    }
}