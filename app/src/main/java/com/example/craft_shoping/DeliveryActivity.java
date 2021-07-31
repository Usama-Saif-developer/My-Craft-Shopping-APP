package com.example.craft_shoping;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DeliveryActivity extends AppCompatActivity {

    public static final int SELECT_ADDRESS = 0;
    public static CartAdapter cartAdapter;
    public static List<CartitemModel> cartitemModelList;
    public static TextView totalAmount;
    public static Dialog loadingDialog;
    public static boolean fromCart;
    public static String order_id;
    public static boolean getQtyIDS = true;
    private RecyclerView deliveryRecyclerview;
    private Button changeoraddnewaddressbtn;
    private TextView fullname;
    private String name, mobileNo;
    private TextView fullAddress;
    private TextView pincode;
    private Button PaymentBtn;
    private String paymentMethod = "COD";
    private Dialog paymentMethodDialog;
    private TextView codTitle;
    private View divider;
    private ImageButton jazzcashbtn;
    private ImageButton codbtn;
    private ImageButton continueShoppingBtn;
    private ConstraintLayout orderConfirmationlayout;
    private TextView orderId;
    private boolean successResponse = false;
    private FirebaseFirestore firebaseFirestore;
//    public static boolean codOrderConfirmation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Toolbar CODE IS hERE
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Delivery");

        changeoraddnewaddressbtn = findViewById(R.id.change_or_add_address_btn);
        totalAmount = findViewById(R.id.product_total_amount);
        fullname = findViewById(R.id.fullname);
        fullAddress = findViewById(R.id.address);
        pincode = findViewById(R.id.pincode);
        PaymentBtn = findViewById(R.id.delivery_next_btn);
        continueShoppingBtn = findViewById(R.id.continue_shopping_btn);
        orderId = findViewById(R.id.order_id_at_confirmation);
        orderConfirmationlayout = findViewById(R.id.order_confirmation_layout);

        ///loading Dailog//////
        loadingDialog = new Dialog(DeliveryActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dailog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ///loading Dailog//////

        ///payment Dailog//////
        paymentMethodDialog = new Dialog(DeliveryActivity.this);
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.setCancelable(true);
        paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        jazzcashbtn = paymentMethodDialog.findViewById(R.id.jazzcash_btn);
        codbtn = paymentMethodDialog.findViewById(R.id.cod_btn);
        codTitle = paymentMethodDialog.findViewById(R.id.cod_btn_title);
        divider = paymentMethodDialog.findViewById(R.id.divider_payment);
        ///payment Dailog//////
        firebaseFirestore = FirebaseFirestore.getInstance();
        getQtyIDS = true;
        order_id = UUID.randomUUID().toString().substring(0, 28);

        //recyclerview code here
        deliveryRecyclerview = findViewById(R.id.delivery_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        deliveryRecyclerview.setLayoutManager(layoutManager);

        cartAdapter = new CartAdapter(cartitemModelList, totalAmount, false);
        deliveryRecyclerview.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        changeoraddnewaddressbtn.setVisibility(View.VISIBLE);
        changeoraddnewaddressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQtyIDS = false;
                Intent myaddressesIntent = new Intent(DeliveryActivity.this, MyAddressesActivity.class);
                myaddressesIntent.putExtra("MODE", SELECT_ADDRESS);
                startActivity(myaddressesIntent);
            }
        });

        PaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean allProductAvailable = true;
                for (CartitemModel cartitemModel : cartitemModelList) {
                    if (cartitemModel.isQtyError()) {
                        allProductAvailable = false;
                        break;
                    }
                    if (cartitemModel.getType() == CartitemModel.CART_ITEM) {
                        if (!cartitemModel.isCOD()) {
                            codbtn.setEnabled(false);
                            codbtn.setAlpha(0.5f);
                            codTitle.setAlpha(0.5f);
                            divider.setVisibility(View.GONE);
                            break;
                        } else {
                            codbtn.setEnabled(true);
                            codbtn.setAlpha(1f);
                            codTitle.setAlpha(1f);
                            divider.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if (allProductAvailable) {
                    paymentMethodDialog.show();
                }
            }
        });

        codbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMethod = "COD";
                placeOrderDetails();
            }
        });

        jazzcashbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                paymentMethod = "JAZZCASH";
//                placeOrderDetails();
                jazzcash();
            }
        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Check that it is the SecondActivity with an OK result
//        if (requestCode == 0 && resultCode == RESULT_OK) {
//            // Get String data from Intent
//            String ResponseCode = data.getStringExtra("pp_ResponseCode");
//            System.out.println("DateFn: ResponseCode:" + ResponseCode);
//            if (ResponseCode.equals("000")) {
//                Toast.makeText(getApplicationContext(), "Payment Success", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(getApplicationContext(), "Payment Failed", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
        ///accsss quantity
        if (getQtyIDS) {
            loadingDialog.show();
            for (int x = 0; x < cartitemModelList.size() - 1; x++) {
                for (int y = 0; y < cartitemModelList.get(x).getProductQuantity(); y++) {
                    String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);
                    Map<String, Object> timestamp = new HashMap<>();
                    timestamp.put("time", FieldValue.serverTimestamp());
                    int finalX = x;
                    int finalY = y;
                    firebaseFirestore.collection("PRODUCTS").document(cartitemModelList.get(x).getProductID()).collection("QUANTITY").document(quantityDocumentName).set(timestamp)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        cartitemModelList.get(finalX).getQtyIDs().add(quantityDocumentName);
                                        if (finalY + 1 == cartitemModelList.get(finalX).getProductQuantity()) {
                                            firebaseFirestore.collection("PRODUCTS").document(cartitemModelList.get(finalX).getProductID()).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(cartitemModelList.get(finalX).getStockQuantity()).get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                List<String> serverQuantity = new ArrayList<>();
                                                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                    serverQuantity.add(queryDocumentSnapshot.getId());
                                                                }
                                                                long availableQty = 0;
                                                                boolean noLongerAvailable = true;
                                                                for (String qtyId : cartitemModelList.get(finalX).getQtyIDs()) {
                                                                    cartitemModelList.get(finalX).setQtyError(false);
                                                                    if (!serverQuantity.contains(qtyId)) {
                                                                        if (noLongerAvailable) {
                                                                            cartitemModelList.get(finalX).setInStock(false);
                                                                        } else {
                                                                            cartitemModelList.get(finalX).setQtyError(true);
                                                                            cartitemModelList.get(finalX).setMaxQuantity(availableQty);
                                                                            Toast.makeText(DeliveryActivity.this, "Sorry,All products may not be Available in required Quantity...!", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    } else {
                                                                        availableQty++;
                                                                        noLongerAvailable = false;
                                                                    }
                                                                }
                                                                cartAdapter.notifyDataSetChanged();
                                                            } else {
                                                                String error = task.getException().getMessage();
                                                                Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                                            }
                                                            loadingDialog.dismiss();
                                                        }
                                                    });
                                        }
                                    } else {
                                        loadingDialog.dismiss();
                                        String error = task.getException().getMessage();
                                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
            }
        } else {
            getQtyIDS = true;
        }

        ///accsss quantity
        name = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getName();
        mobileNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getMobileNo();
        if (DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateMobileNo().equals("")) {
            fullname.setText(name + " - " + mobileNo);
        }else{
            fullname.setText(name + " - " + mobileNo+" or "+DBqueries.addressesModelList.get(DBqueries.selectedAddress).getAlternateMobileNo());
        }
        String flatNo = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getFlatNo();
        String locality = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLocality();
        String landmark = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getLandmark();
        String city = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getCity();
        String state = DBqueries.addressesModelList.get(DBqueries.selectedAddress).getState();
        if (landmark.equals("")){
            fullAddress.setText(flatNo+" "+locality+" "+city+" "+state);
        }else {
            fullAddress.setText(flatNo+" "+locality+" "+landmark+" "+city+" "+state);
        }
        pincode.setText(DBqueries.addressesModelList.get(DBqueries.selectedAddress).getPincode());
//        if (codOrderConfirmation){
//            showConfirmationLayout();
//        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadingDialog.dismiss();
        if (getQtyIDS) {
            for (int x = 0; x < cartitemModelList.size() - 1; x++) {
                if (!successResponse) {
                    for (String qtyID : cartitemModelList.get(x).getQtyIDs()) {
                        int finalX = x;
                        firebaseFirestore.collection("PRODUCTS").document(cartitemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (qtyID.equals(cartitemModelList.get(finalX).getQtyIDs().get(cartitemModelList.get(finalX).getQtyIDs().size() - 1))) {
                                            cartitemModelList.get(finalX).getQtyIDs().clear();
                                        }
                                    }
                                });
                    }
                } else {
                    cartitemModelList.get(x).getQtyIDs().clear();
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (successResponse) {
            finish();
            return;
        }
        super.onBackPressed();
    }

    private void showConfirmationLayout() {
        successResponse = true;
//        codOrderConfirmation = false;
        getQtyIDS = false;
        for (int x = 0; x < cartitemModelList.size() - 1; x++) {
            for (String qtyID : cartitemModelList.get(x).getQtyIDs()) {
                firebaseFirestore.collection("PRODUCTS").document(cartitemModelList.get(x).getProductID()).collection("QUANTITY").document(qtyID).update("user_ID", FirebaseAuth.getInstance().getUid());
            }
        }
        if (MainActivity.mainActivity != null) {
            MainActivity.mainActivity.finish();
            MainActivity.mainActivity = null;
            MainActivity.showCart = false;
        } else {
            MainActivity.resetMainActivity = true;
        }
        if (ProductDetailActivity.productDetailActivity != null) {
            ProductDetailActivity.productDetailActivity.finish();
            ProductDetailActivity.productDetailActivity = null;
        }

        ///SMS Api HALF CODE WILL BE THERE

        ///SMS Api HALF CODE WILL BE THERE
        String customer_id = FirebaseAuth.getInstance().getUid();
        String finalAmount = DeliveryActivity.totalAmount.getText().toString().substring(3, DeliveryActivity.totalAmount.getText().length() - 2);
        orderConfirmationlayout.setVisibility(View.VISIBLE);
        orderId.setText("Order ID :" + order_id);

        if (fromCart) {
            loadingDialog.show();
            Map<String, Object> updateCartlist = new HashMap<>();
            long cartListSize = 0;
            List<Integer> indexList = new ArrayList<>();
            for (int x = 0; x < DBqueries.cartList.size(); x++) {
                if (!cartitemModelList.get(x).isInStock()) {
                    updateCartlist.put("product_ID_" + cartListSize, cartitemModelList.get(x).getProductID());
                    cartListSize++;
                } else {
                    indexList.add(x);
                }
            }
            updateCartlist.put("list_size", cartListSize);
            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_CART")
                    .set(updateCartlist).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        for (int x = 0; x < indexList.size(); x++) {
                            DBqueries.cartList.remove(indexList.get(x).intValue());
                            DBqueries.cartitemModelList.remove(indexList.get(x).intValue());
                            DBqueries.cartitemModelList.remove(DBqueries.cartitemModelList.size() - 1);
                        }
                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismiss();
                }
            });
        }
        Toast.makeText(DeliveryActivity.this, "Thank you! your order placed ", Toast.LENGTH_SHORT).show();
        Toast.makeText(DeliveryActivity.this, "you paid Amount Rs. " + finalAmount + "/-", Toast.LENGTH_SHORT).show();
        continueShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void placeOrderDetails() {
        String userID = FirebaseAuth.getInstance().getUid();
        loadingDialog.show();
        for (CartitemModel cartitemModel : cartitemModelList) {
            if (cartitemModel.getType() == CartitemModel.CART_ITEM) {
                Map<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("ORDER ID", order_id);
                orderDetails.put("Product Id", cartitemModel.getProductID());
                orderDetails.put("Product Image", cartitemModel.getProductimage());
                orderDetails.put("Product Title", cartitemModel.getProductTitle());
                orderDetails.put("User Id", userID);
                orderDetails.put("Product Quantity", cartitemModel.getProductQuantity());
                if (cartitemModel.getCuttedPrice() != null) {
                    orderDetails.put("Cutted Price", cartitemModel.getCuttedPrice());
                } else {
                    orderDetails.put("Cutted Price", " ");
                }
                orderDetails.put("Product Price", cartitemModel.getProductPrice());
                if (cartitemModel.getSelectedCoupenId() != null) {
                    orderDetails.put("Coupen Id", cartitemModel.getSelectedCoupenId());
                } else {
                    orderDetails.put("Coupen Id", " ");
                }
                if (cartitemModel.getDiscountedPrice() != null) {
                    orderDetails.put("Discounted Price", cartitemModel.getDiscountedPrice());
                } else {
                    orderDetails.put("Discounted Price", "");
                }
                orderDetails.put("Ordered date", FieldValue.serverTimestamp());
                orderDetails.put("Packed date", FieldValue.serverTimestamp());
                orderDetails.put("Shipped date", FieldValue.serverTimestamp());
                orderDetails.put("Delivered date", FieldValue.serverTimestamp());
                orderDetails.put("Cancelled date", FieldValue.serverTimestamp());
                orderDetails.put("Order Status", "Ordered");
                orderDetails.put("Payment Method", paymentMethod);
                orderDetails.put("Address", fullAddress.getText());
                orderDetails.put("FullName", fullname.getText());
                orderDetails.put("Pincode", pincode.getText());
                orderDetails.put("Free Coupens", cartitemModel.getFreeCoupens());
                orderDetails.put("Delivery Price", cartitemModelList.get(cartitemModelList.size() - 1).getDeliveryPrice());
                orderDetails.put("Cancellation requested", false);

                firebaseFirestore.collection("ORDERS").document(order_id).collection("OrderItems").document(cartitemModel.getProductID())
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            String error = task.getException().getMessage();
                            Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Map<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("Total Items", cartitemModel.getTotalItems());
                orderDetails.put("Total Items Price", cartitemModel.getTotalItemPrice());
                orderDetails.put("Delivery Price", cartitemModel.getDeliveryPrice());
                orderDetails.put("Total Amount", cartitemModel.getTotalAmount());
                orderDetails.put("Saved Amount", cartitemModel.getSavedAmount());
                orderDetails.put("Payment Status", "Not Paid");
                orderDetails.put("Order Status", "Cancelled");
                firebaseFirestore.collection("ORDERS").document(order_id)
                        .set(orderDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (paymentMethod.equals("COD")) {
                                COD();
                            }
//                            else {
//                                jazzcash();
//                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(DeliveryActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private void jazzcash() {
        getQtyIDS = false;
        paymentMethodDialog.dismiss();
        loadingDialog.show();
        Toast.makeText(DeliveryActivity.this, "JazzCash Under Developed", Toast.LENGTH_SHORT).show();
//                Intent paymentIntent=new Intent(DeliveryActivity.this,PaymentActivity.class);
//                startActivity(paymentIntent);
        loadingDialog.dismiss();
    }

    private void COD() {
        getQtyIDS = false;
        paymentMethodDialog.dismiss();
//                ////OTP  VERIFICATION CODE
//                Intent otpIntent=new Intent(DeliveryActivity.this,OTPVerificationActivity.class);
//                otpIntent.putExtra("mobileNo",mobileNo.substring(0,11));
//                startActivity(otpIntent);
//                ////OTP  VERIFICATION CODE
        loadingDialog.show();
        if (ContextCompat.checkSelfPermission(DeliveryActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DeliveryActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }

        ///code for OTP
        Map<String, Object> updateStatus = new HashMap<>();
        updateStatus.put("Order Status", "Ordered");
        firebaseFirestore.collection("ORDERS").document(order_id).update(updateStatus)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> userOrder = new HashMap<>();
                            userOrder.put("order_id", order_id);
                            userOrder.put("time", FieldValue.serverTimestamp());
                            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_ORDERS").document(order_id).set(userOrder)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(DeliveryActivity.this, "Failed to update user order", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(DeliveryActivity.this, "Order Camcelled !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        ///code for OTP
        showConfirmationLayout();
        loadingDialog.dismiss();
    }
}