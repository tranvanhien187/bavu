package com.example.fakebook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupFirstActivity extends AppCompatActivity {
    private CircleImageView setupImage;
    private EditText edtNickName,edtNgay,edtThang,edtNam;
    private RadioGroup radioGroup;
    private Button btnSubmit;
    private RadioButton rdoMale,rdoFemale;
    private boolean isMale=true;
    Uri mainimageURI = null;
    FirebaseFirestore firebaseFirestore;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_first);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseStorage= FirebaseStorage.getInstance();
        storageReference=FirebaseStorage.getInstance().getReference();
        mAuth=FirebaseAuth.getInstance();
        setWidget();
        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SetupFirstActivity.this, "ABC", Toast.LENGTH_LONG).show();
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SetupFirstActivity.this);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName=edtNickName.getText().toString();
                String ngay=edtNgay.getText().toString();
                String thang=edtThang.getText().toString();
                String nam=edtNam.getText().toString();
                if(TextUtils.isEmpty(nickName)||TextUtils.isEmpty(ngay)||TextUtils.isEmpty(thang)||TextUtils.isEmpty(nam)||mainimageURI==null){
                    Toast.makeText(SetupFirstActivity.this, "ngu", Toast.LENGTH_SHORT).show();
                }
                else{
                    String emailTmp=mAuth.getCurrentUser().getEmail();
                    final String email=emailTmp.substring(0,emailTmp.length()-"@gmail.com".length());
                    String fileName=email+"@ava";
                    StorageReference filePath=storageReference.child("ava_image").child(fileName+".jpg");
                    filePath.putFile(mainimageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(SetupFirstActivity.this, "put image success !", Toast.LENGTH_SHORT).show();
                                    Map<String,Object> userMap=new HashMap<>();
                                    userMap.put("name",nickName);
                                    userMap.put("isMale",isMale);
                                    userMap.put("dateOfBirth",ngay+"/"+thang+"/"+nam);
                                    userMap.put("uId",mAuth.getUid());
                                    userMap.put("ava",uri.toString());
                                    firebaseFirestore.collection("Users").document(email)
                                            .set(userMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(SetupFirstActivity.this, "good :v", Toast.LENGTH_SHORT).show();
//                                    Intent mainIntent=new Intent(SetupFirstActivity.this,MainActivity.class);
//                                    startActivity(mainIntent);
//                                    finish();
                                                }
                                            });
                                }
                            });
                        }
                    });

                }
            }
        });
        rdoMale.setOnCheckedChangeListener(listenerRadio);
        rdoFemale.setOnCheckedChangeListener(listenerRadio);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainimageURI = result.getUri();
                setupImage.setImageURI(mainimageURI);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    void setWidget(){
        rdoFemale=findViewById(R.id.rdo_female);
        rdoMale=findViewById(R.id.rdo_male);
        btnSubmit=findViewById(R.id.btn_submit);
        setupImage = findViewById(R.id.setup_avatar);
        edtNickName=findViewById(R.id.edt_nickname);
        edtNgay=findViewById(R.id.edt_ngay);
        edtThang=findViewById(R.id.edt_thang);
        edtNam=findViewById(R.id.edt_nam);
        radioGroup=findViewById(R.id.rdo_group);
    }
    CompoundButton.OnCheckedChangeListener listenerRadio=new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                Toast.makeText(SetupFirstActivity.this,buttonView.getText().toString(), Toast.LENGTH_SHORT).show();
                if(buttonView.getText().toString().equals("Nam")){
                    isMale=true;
                }
                else{
                    isMale=false;
                }
            }
        }
    };
}
