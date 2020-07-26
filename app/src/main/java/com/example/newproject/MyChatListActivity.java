package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class MyChatListActivity extends Fragment implements MyChatAdapter.OnListItemSelectedInterface{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<String> arrayList_key = new ArrayList<String>();
    private ArrayList<ChatProfile> arrayList = new ArrayList<ChatProfile>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState){
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.activity_mychatlist, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        findmychat();
        return view;
    }

    public void findmychat() {
        final String[] key = new String[1];
        String email;
        String profile;
        final DocumentReference[] documentReference = new DocumentReference[1];
        final FirebaseFirestore[] db = {FirebaseFirestore.getInstance()};
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance("https://newproject-ab6cb-chat.firebaseio.com/");
        databaseReference = database.getReference(user.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    arrayList_key.add(dataSnapshot.getKey());
                }
                for(int i = 0; i < arrayList_key.size(); i++){
                    key[0] = arrayList_key.get(i);
                    documentReference[0] = db[0].collection("Users").document(key[0]);
                    documentReference[0].addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            ChatProfile chatProfile = documentSnapshot.toObject(ChatProfile.class);
                            arrayList.add(chatProfile);
                            adapter = new MyChatAdapter(arrayList, getContext(), MyChatListActivity.this);
                            recyclerView.setAdapter(adapter);
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemSelected(View v, int position) {
        MyChatAdapter.MyChatViewHolder viewHolder = (MyChatAdapter.MyChatViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        Intent intent = new Intent(getContext(), ChatActivity.class);
        String id = arrayList_key.get(position);      //클릭한 위치의 채팅 상대의 id
        intent.putExtra("chat_userid", id);
        startActivity(intent);
    }
}
