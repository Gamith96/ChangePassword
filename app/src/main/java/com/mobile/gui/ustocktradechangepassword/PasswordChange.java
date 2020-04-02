package com.mobile.gui.ustocktradechangepassword;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.gui.ustocktradechangepassword.Common.Common;
import com.mobile.gui.ustocktradechangepassword.Model.User;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class PasswordChange extends AppCompatActivity {

    Button btnChange;
    String phone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);



        btnChange = (Button)findViewById(R.id.btnChange);
        final MaterialEditText edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        final MaterialEditText edtNewPassword = (MaterialEditText)findViewById(R.id.edtNewPassword);
        final MaterialEditText edtRepeatPassword = (MaterialEditText)findViewById(R.id.edtRepeatPassword);


        //init Firebase

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference user = database.getReference("User");



        btnChange.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                final ProgressDialog mDialog = new ProgressDialog(PasswordChange.this);
                mDialog.setMessage("Please wait...");
                mDialog.show();

                //check old password
             if(edtPassword.getText().toString().equals(Common.currentUser.getPassword()))
                {



                    //Check new password with re-entering password
                    if(edtNewPassword.getText().toString().equals(edtRepeatPassword.getText().toString())){

                        Map<String,Object> passwordUpdate = new HashMap<>();
                        passwordUpdate.put("Password",edtNewPassword.getText().toString());

                        //Make Update
                        DatabaseReference user = FirebaseDatabase.getInstance().getReference("User");
                        Bundle b = getIntent().getExtras();
                        if(b != null)
                            phone = b.getString("phone");

                        user.child(phone)

                                .updateChildren(passwordUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Intent intent = new Intent(PasswordChange.this,Home.class);
                                        startActivity(intent);

                                       mDialog.dismiss();
                                        Toast.makeText(PasswordChange.this, "Password was updated", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(PasswordChange.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                    else
                    {
                        mDialog.dismiss();
                        Toast.makeText(PasswordChange.this, "New Passwords dosen't match", Toast.LENGTH_SHORT).show();
                    }

                }else{
                 mDialog.dismiss();
                    Toast.makeText(PasswordChange.this, "Incorrect Old Password", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}
