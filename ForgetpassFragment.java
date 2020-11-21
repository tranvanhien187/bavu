package com.example.fakebook;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ForgetpassFragment extends Fragment implements View.OnClickListener, FirebaseAuth.AuthStateListener {
    TextView txtxacnhan;
    Button btntrolai;
    EditText edtemailforget;
    FirebaseAuth firebaseAuth;
    NavController navController;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_forgetpass, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtxacnhan = getActivity().findViewById(R.id.txtxacnhan);
        btntrolai = getActivity().findViewById(R.id.btntrolaifromqmk);
        edtemailforget = getActivity().findViewById(R.id.edtemailf);

        txtxacnhan.setOnClickListener(this);
        btntrolai.setOnClickListener(this);

        navController = Navigation.findNavController(view);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.txtxacnhan:
                String xnemail = edtemailforget.getText().toString();
                boolean check = KiemtraEmail(xnemail);
                if (check) {

                    firebaseAuth.sendPasswordResetEmail(xnemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("KIEMTRA", "@");
                                Toast.makeText(getActivity(), "Xác nhận thành công! Vui lòng thay đổi mật khẩu trong email", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Email chưa được đăng kí!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "Email không hợp lệ!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btntrolaifromqmk:
                navController.navigate(R.id.action_forgetpassFragment2_to_loginFragment);
                break;
        }
    }
    public boolean KiemtraEmail(String email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }



    @Override
        public void onAuthStateChanged (@NonNull FirebaseAuth firebaseAuth){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

        } else {

        }
    }
}
