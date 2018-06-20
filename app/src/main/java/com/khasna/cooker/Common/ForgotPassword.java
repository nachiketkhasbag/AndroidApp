package com.khasna.cooker.Common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.khasna.cooker.R;

/**
 * Created by nachiket on 4/3/2017.
 */

public class ForgotPassword extends AppCompatActivity {

    private DebugClass debugClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        final Button resetPasswordButton = (Button)findViewById(R.id.resetPasswordButton);   // FragmentResetPasswordChef button listener
        final TextView emailTextView = (TextView)findViewById(R.id.email);   // FragmentResetPasswordChef button listener

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String errorCode = ValidateInput();

                if (errorCode.compareTo("VALID_INPUT") == 0)
                {
                    // Write your code here
//                    debugClass.DebugPrint("InForgotPassword", errorCode);
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.sendPasswordResetEmail(emailTextView.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(),"Reset email sent to " + emailTextView.getText().toString() ,
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),"Error" + task.getException(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else
                {
                    // Start the Signup activity
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            }
        });
    }

    public String ValidateInput()
    {
        EditText emailID = (EditText)findViewById(R.id.email); // Textfield email

        String validateEmail = emailID.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if ((!validateEmail.matches(emailPattern)) ||
                validateEmail.isEmpty() )
        {
            return "INVALID_EMAIL";
        }

        return "VALID_INPUT";
    }
}
