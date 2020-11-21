package com.example.fakebook;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterFragment extends Fragment implements View.OnClickListener {
    EditText edtregisteremail,edtregisterpassword, edtregisterpasswordagain;
    NavController navController;
    Button btntrolai;
    Button btndangki;
    FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        edtregisteremail = getActivity().findViewById(R.id.edtregisteremail);
        edtregisterpassword=getActivity().findViewById(R.id.edtregisterpassword);
        edtregisterpasswordagain =getActivity().findViewById(R.id.edtregisterpasswordagain);
        btntrolai = getActivity().findViewById(R.id.btntrolaifromdky);
        btndangki = getActivity().findViewById(R.id.btnregister);
        btntrolai.setOnClickListener(this);
        btndangki.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.btnregister:
                dangki();
                break;
            case R.id.btntrolaifromdky:
                navController.navigate(R.id.action_registerFragment_to_loginFragment);
                break;
        }

    }
    public void dangki()
    {
        String email = edtregisteremail.getText().toString();
        String pass1 = edtregisterpassword.getText().toString();
        String pass2 = edtregisterpasswordagain.getText().toString();
        if(!email.isEmpty() && !pass1.isEmpty() && !pass2.isEmpty()) {
            if (pass2.equals(pass1) == false) {
                Toast.makeText(getActivity(), "Mật khẩu không khớp", Toast.LENGTH_LONG).show();
            } else {
                mAuth.createUserWithEmailAndPassword(email, pass1)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Đăng kí thành công", Toast.LENGTH_LONG).show();
                                    Intent isetupfirst = new Intent(getActivity(),SetupFirstActivity.class);
                                    startActivity(isetupfirst);
                                } else {
                                    Toast.makeText(getActivity(), "Email không hợp lệ hoặc đã được đăng kí", Toast.LENGTH_LONG).show();

                                }
                            }
                        });

            }
        }
        else
        {
            Toast.makeText(getActivity(),"Vui lòng nhập đủ thông tin",Toast.LENGTH_LONG).show();
        }
    }
}

