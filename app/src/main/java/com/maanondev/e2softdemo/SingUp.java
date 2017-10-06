package com.maanondev.e2softdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maanondev.e2softdemo.Model.Reg;

public class SingUp extends AppCompatActivity {

    FirebaseDatabase Database;
    DatabaseReference myRef;
    AppCompatButton  reg_btn;
    EditText reg_email;
    EditText reg_password;
    EditText reg_name;
    EditText reg_dob;
    EditText reg_mob;
    String uniqueDatabase_Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        reg_email = (EditText) findViewById(R.id.reg_email);
        reg_password = (EditText) findViewById(R.id.reg_password);
        reg_name = (EditText) findViewById(R.id.reg_name);
        reg_dob = (EditText) findViewById(R.id.reg_dob);
        reg_mob = (EditText) findViewById(R.id.reg_mob);

        reg_btn= (AppCompatButton) findViewById(R.id.reg_btn);

        Database = FirebaseDatabase.getInstance();
        myRef=Database.getReference("DatabaseReg");

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String UserName = reg_email.getText().toString();
                String Password = reg_password.getText().toString();
                String Name = reg_name.getText().toString();
                String Dob = reg_dob.getText().toString();
                String Mob = reg_mob.getText().toString();


                Long DobLongValue = Long.valueOf(0);
                Long MobLongValue = Long.valueOf(0);

                try {
                    DobLongValue = Long.parseLong(Dob);
                } catch (Exception e) {
                    Log.i("TAG", e.getMessage());
                }

                try {
                    MobLongValue = Long.valueOf(0);
                } catch (Exception e) {
                    Log.i("TAG", e.getMessage());
                }

                if (UserName.length() == 0) {
                    reg_email.requestFocus();
                    reg_email.setError("FIELD CANNOT BE EMPTY");
                } else if (Password.length() == 0) {
                    reg_password.requestFocus();
                    reg_password.setError("FIELD CANNOT BE EMPTY");
                } else if (Name.length() == 0) {
                    reg_name.requestFocus();
                    reg_name.setError("FIELD CANNOT BE EMPTY");
                } else if (Dob.length() == 0) {
                    reg_dob.requestFocus();
                    reg_dob.setError("FIELD CANNOT BE EMPTY");
                } else if (Mob.length() == 0) {
                    reg_mob.requestFocus();
                    reg_mob.setError("FIELD CANNOT BE EMPTY");
                } else {

                    DataGoToServer(UserName, Password, Name, DobLongValue, MobLongValue);
                }

                finish();

               /* myRef.child(reg_email.getText().toString()).child("Password").setValue(reg_password.getText().toString());
                myRef.child(reg_email.getText().toString()).child("Name").setValue(reg_name.getText().toString());
                myRef.child(reg_email.getText().toString()).child("DOB").setValue(reg_dob.getText().toString());
                myRef.child(reg_email.getText().toString()).child("Mobile").setValue(reg_mob.getText().toString());*/

            }
        });

    }

    private void DataGoToServer(String UserName,String Password,String Name,Long Dob,Long Mob) {

        uniqueDatabase_Id=myRef.push().getKey();

        Reg reg=new Reg(UserName, Password, Name, Dob, Mob);
        myRef.child(uniqueDatabase_Id).setValue(reg);
    }
}
