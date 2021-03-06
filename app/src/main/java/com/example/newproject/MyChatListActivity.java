package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.Class.ChatProfile;
import com.example.newproject.Class.LocalUserInfo;
import com.example.newproject.Interface.OnMyChatClickListener;
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

public class MyChatListActivity extends Fragment implements MyChatAdapter.OnItemClickListener{
    private RecyclerView recyclerView;
    private MyChatAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<String> arrayList_key = new ArrayList<String>();
    private ArrayList<ChatProfile> arrayList = new ArrayList<ChatProfile>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState){
        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.activity_mychatlist, container, false);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(this.getResources().getDrawable(R.drawable.recycler_line));

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);

        findmychat();
        return view;
    }

    public void findmychat() {
        final String[] key = new String[1];
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
                adapter = new MyChatAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
                for(int i = 0; i < arrayList_key.size(); i++){
                    key[0] = arrayList_key.get(i);
                    documentReference[0] = db[0].collection("Users").document(key[0]);
                    documentReference[0].addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            ChatProfile chatProfile = documentSnapshot.toObject(ChatProfile.class);
                            LocalUserInfo localUserInfo = documentSnapshot.toObject(LocalUserInfo.class);
                            chatProfile.setProfile(localUserInfo.getImageurl());
                            arrayList.add(chatProfile);
                            adapter = new MyChatAdapter(arrayList, getContext());
                            recyclerView.setAdapter(adapter);
                            adapter.setOnItemClickListener(new OnMyChatClickListener() {
                                @Override
                                public void onItemClick(MyChatAdapter.MyChatViewHolder holder, View view, int position) {
                                    MyChatAdapter.MyChatViewHolder viewHolder = (MyChatAdapter.MyChatViewHolder)recyclerView.findViewHolderForAdapterPosition(position);
                                    Intent intent = new Intent(getContext(), ChatActivity.class);
                                    intent.putExtra("chat_userid", arrayList_key.get(position));
                                    startActivity(intent);
                                }
                            });
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void onItemClick(MyChatAdapter.MyChatViewHolder myChatViewHolder, View v, int position){

    }
}
