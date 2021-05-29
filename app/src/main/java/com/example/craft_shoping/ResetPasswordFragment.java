package com.example.craft_shoping;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResetPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetPasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText registeredemail;
    private Button reset_btn;
    private TextView goback;
    private FrameLayout parentframelayout;
    private FirebaseAuth firebaseAuth;
    private ViewGroup emailiconcontainer;
    private ImageView emailicon;
    private TextView emailicontext;
    private ProgressBar progressBar;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResetPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResetPasswordFragment newInstance(String param1, String param2) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        registeredemail = view.findViewById(R.id.forgot_password_email);
        reset_btn = view.findViewById(R.id.forgot_password_btn);
        goback = view.findViewById(R.id.forgot_go_back);
        parentframelayout=getActivity().findViewById(R.id.register_framelaout);
        firebaseAuth=FirebaseAuth.getInstance();
        emailiconcontainer=view.findViewById(R.id.forgot_password_warrning);
        emailicon=view.findViewById(R.id.forgot_password_email_icon);
        emailicontext=view.findViewById(R.id.forgot_recovery_text);
        progressBar=view.findViewById(R.id.forgot_progressBar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registeredemail.addTextChangedListener(new TextWatcher() {
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
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setFragment(new SignInFragment());
            }
        });
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransitionManager.beginDelayedTransition(emailiconcontainer);
                emailicontext.setVisibility(View.GONE);

                TransitionManager.beginDelayedTransition(emailiconcontainer);
                emailiconcontainer.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                reset_btn.setEnabled(false);
                reset_btn.setTextColor(Color.argb(50,255,255,255));
                firebaseAuth.sendPasswordResetEmail(registeredemail.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    progressBar.setVisibility(View.VISIBLE);
                                    Toast.makeText(getActivity(), "email send successfully!", Toast.LENGTH_LONG).show();

                                }else {
                                    String  error = task.getException().getMessage();
                                    TransitionManager.beginDelayedTransition(emailiconcontainer);
                                    emailicontext.setText(error);
                                    emailicontext.setTextColor(getResources().getColor(R.color.teal_700));
                                    emailicontext.setVisibility(View.VISIBLE);
                                    emailicon.setVisibility(View.VISIBLE);
                                }
                                progressBar.setVisibility(View.GONE);
                                reset_btn.setEnabled(true);
                                reset_btn.setTextColor(Color.rgb(255,255,255));
                            }
                        });
            }
        });
    }

    private void checkInputs() {
        if (TextUtils.isEmpty(registeredemail.getText())) {
            reset_btn.setEnabled(false);
            reset_btn.setTextColor(Color.argb(50, 255, 255, 255));

        } else {
            reset_btn.setEnabled(true);
            reset_btn.setTextColor(Color.rgb(255, 255, 255));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentframelayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}