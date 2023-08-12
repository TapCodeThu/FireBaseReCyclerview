package com.example.pushdata01;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText inputId, inputName, inputEmail, inputIdJob, inputNameJob;
    private Button btnPush;
    RecyclerView recyclerView;
    UserAdapter userAdapter;
    List<User> mlistUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iNitView();
        getData();
        btnPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Id = Integer.parseInt(inputId.getText().toString().trim());
                String Name = inputName.getText().toString().trim();
                String Email = inputEmail.getText().toString().trim();
                int IdJob = Integer.parseInt(inputIdJob.getText().toString().trim());
                String NameJob = inputNameJob.getText().toString().trim();
                User user = new User(Id,Name,Email,new Job(IdJob,NameJob));

                PushData(user);
            }
        });
    }

    private void getData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                if(user != null){
                    mlistUser.add(user);
                    userAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                if(user == null || mlistUser == null || mlistUser.isEmpty()){
                    return;
                }
                for(int i = 0;i<mlistUser.size();i++){
                    if(user.getId() == mlistUser.get(i).getId()){
                        mlistUser.set(i,user);
                        break;
                    }
                }
                userAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user == null || mlistUser == null || mlistUser.isEmpty()){
                    return;
                }
                for(int i = 0;i<mlistUser.size();i++){
                    if(user.getId() == mlistUser.get(i).getId()){
                        mlistUser.remove(mlistUser.get(i));
                        break;
                    }
                }
                userAdapter.notifyDataSetChanged();


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void PushData(User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");
        String pathObject = String.valueOf(user.getId());
        ref.child(pathObject).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(MainActivity.this, "Successfully", Toast.LENGTH_SHORT).show();

            }
        });


    }
    private void openDialog(User user){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        EditText Name_dialog = dialog.findViewById(R.id.id_name_dialog);
        EditText Email_dialog = dialog.findViewById(R.id.id_email_dialog);
        Button Update_dialog = dialog.findViewById(R.id.id_update_dialog);
        Button Huy_dialog = dialog.findViewById(R.id.id_huy_dialog);
        Name_dialog.setText(user.getName());
        Email_dialog.setText(user.getEmail());
        Update_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("users");
                String newName = Name_dialog.getText().toString().trim();
                String newEmail = Email_dialog.getText().toString().trim();
                user.setName(newName);
                user.setEmail(newEmail);
                ref.child(String.valueOf(user.getId())).updateChildren(user.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(MainActivity.this, "Update successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });

            }
        });
        Huy_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



        dialog.show();


    }
    private void onClickDeleteData(User user){
        new AlertDialog.Builder(this)
                .setTitle("Thông Báo")
                .setCancelable(false)
                .setMessage("Bạn có chắc chắn muốn xóa ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference("users");
                        ref.child(String.valueOf(user.getId())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                })
                .setNegativeButton("Cancel",null)
                .show();

    }

    private void iNitView() {
        inputId = findViewById(R.id.id_Id);
        inputName = findViewById(R.id.id_Name);
        inputEmail = findViewById(R.id.id_Email);
        btnPush = findViewById(R.id.id_Push);
        inputIdJob = findViewById(R.id.id_IdJob);
        inputNameJob = findViewById(R.id.id_NameJob);
        recyclerView = findViewById(R.id.recycler_view);
        mlistUser = new ArrayList<>();
        userAdapter = new UserAdapter(this, new UserAdapter.IClickListener() {
            @Override
            public void onClickUpdate(User user) {
                openDialog(user);

            }

            @Override
            public void onClickDelete(User user) {
                onClickDeleteData(user);
            }
        },mlistUser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userAdapter);
    }
}