package com.example.craft_shoping;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static boolean disableCloseBtn = false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView alreadyhaveaccount;
    private FrameLayout parentframelayout;
    private EditText sign_up_email;
    private EditText sign_up_full_name;
    private EditText sign_up_password;
    private EditText confirm_password;
    private ImageButton sign_up_close_btn;
    private Button sign_up_btn;
    private ProgressBar sign_up_progress_Bar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        parentframelayout = getActivity().findViewById(R.id.register_framelaout);
        alreadyhaveaccount = view.findViewById(R.id.already_account);


        sign_up_email = view.findViewById(R.id.sign_up_email);
        sign_up_full_name = view.findViewById(R.id.sign_up_full_name);
        sign_up_password = view.findViewById(R.id.sign_up_password);
        confirm_password = view.findViewById(R.id.sign_up_confirm_password);

        sign_up_close_btn = view.findViewById(R.id.sign_up_close_btn);
        sign_up_btn = view.findViewById(R.id.ok_btn);

        sign_up_progress_Bar = view.findViewById(R.id.sign_up_progress_Bar);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (disableCloseBtn) {
            sign_up_close_btn.setVisibility(View.GONE);
        } else {
            sign_up_close_btn.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyhaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });
        sign_up_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });

        sign_up_email.addTextChangedListener(new TextWatcher() {
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
        sign_up_full_name.addTextChangedListener(new TextWatcher() {
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
        sign_up_password.addTextChangedListener(new TextWatcher() {
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
        confirm_password.addTextChangedListener(new TextWatcher() {
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

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                checkEmailandPassword();
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentframelayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkinputs() {
        if (!TextUtils.isEmpty(sign_up_email.getText())) {
            if (!TextUtils.isEmpty(sign_up_full_name.getText())) {
                if (!TextUtils.isEmpty(sign_up_password.getText()) && sign_up_password.length() >= 8) {
                    if (!TextUtils.isEmpty(confirm_password.getText())) {
                        sign_up_btn.setEnabled(true);
                        sign_up_btn.setTextColor(Color.rgb(255, 255, 255));

                    } else {
                        sign_up_btn.setEnabled(false);
                        sign_up_btn.setTextColor(Color.argb(50f, 255, 255, 255));


                    }

                } else {
                    sign_up_btn.setEnabled(false);
                    sign_up_btn.setTextColor(Color.argb(50f, 255, 255, 255));
                }

            } else {
                sign_up_btn.setEnabled(false);
                sign_up_btn.setTextColor(Color.argb(50f, 255, 255, 255));
            }

        } else {
            sign_up_btn.setEnabled(false);
            sign_up_btn.setTextColor(Color.argb(50f, 255, 255, 255));

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkEmailandPassword() {
        Drawable customerroricon = getResources().getDrawable(R.mipmap.warning);
        customerroricon.setBounds(0, 0, customerroricon.getIntrinsicWidth(), customerroricon.getIntrinsicHeight());

        if (sign_up_email.getText().toString().matches(emailPattern)) {
            if (sign_up_password.getText().toString().equals(confirm_password.getText().toString())) {

                sign_up_progress_Bar.setVisibility(View.VISIBLE);
                sign_up_btn.setEnabled(false);
                sign_up_btn.setTextColor(Color.argb(50, 255, 255, 255));

                firebaseAuth.createUserWithEmailAndPassword(sign_up_email.getText().toString(), sign_up_password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Map<String, Object> userdata = new HashMap<>();
                                    userdata.put("full_name", sign_up_full_name.getText().toString());
                                    userdata.put("email", sign_up_email.getText().toString());
                                    userdata.put("profile", "");
                                    firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                            .set(userdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                CollectionReference userDataReference = firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");

                                                //Maps

                                                Map<String, Object> wishlistMap = new HashMap<>();
                                                wishlistMap.put("list_size", (long) 0);

                                                Map<String, Object> ratingsMap = new HashMap<>();
                                                ratingsMap.put("list_size", (long) 0);

                                                Map<String, Object> cartMap = new HashMap<>();
                                                cartMap.put("list_size", (long) 0);

                                                Map<String, Object> myAddressesMap = new HashMap<>();
                                                myAddressesMap.put("list_size", (long) 0);

                                                Map<String, Object> notificationsMap = new HashMap<>();
                                                notificationsMap.put("list_size", (long) 0);

                                                //////maps


                                                List<String> documentNames = new ArrayList<>();
                                                documentNames.add("MY_WISHLIST");
                                                documentNames.add("MY_RATINGS");
                                                documentNames.add("MY_CART");
                                                documentNames.add("MY_ADDRESSES");
                                                documentNames.add("MY_NOTIFICATIONS");

                                                List<Map<String, Object>> documentFields = new ArrayList<>();
                                                documentFields.add(wishlistMap);
                                                documentFields.add(ratingsMap);
                                                documentFields.add(cartMap);
                                                documentFields.add(myAddressesMap);
                                                documentFields.add(notificationsMap);

                                                for (int x = 0; x < documentNames.size(); x++) {
                                                    int finalX = x;
                                                    userDataReference.document(documentNames.get(x))
                                                            .set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                if (finalX == documentNames.size() - 1) {
                                                                    mainIntent();
                                                                }
                                                            } else {
                                                                sign_up_progress_Bar.setVisibility(View.INVISIBLE);
                                                                sign_up_btn.setEnabled(true);
                                                                sign_up_btn.setTextColor(Color.rgb(255, 255, 255));
                                                                String error = task.getException().getMessage();
                                                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            } else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    sign_up_progress_Bar.setVisibility(View.INVISIBLE);
                                    sign_up_btn.setEnabled(true);
                                    sign_up_btn.setTextColor(Color.rgb(255, 255, 255));
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            } else {
                confirm_password.setError("Password doesn't matched!", customerroricon);

            }

        } else {
            sign_up_email.setError("Invalid Email!", customerroricon);

        }
    }

    private void mainIntent() {
        if (disableCloseBtn) {
            disableCloseBtn = false;
        } else {
            Intent mainIntent = new Intent(getActivity(), MainActivity.class);
            startActivity(mainIntent);
        }
        getActivity().finish();


    }
}