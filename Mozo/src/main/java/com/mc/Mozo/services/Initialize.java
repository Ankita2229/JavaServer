package com.mc.Mozo.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.v1beta1.FirestoreSettings;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class Initialize {
	
	public static Firestore db;
	
	
	public Initialize() {
		// TODO Auto-generated constructor stub
	}
	
	public void startfirebase() throws IOException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		FileInputStream serviceAccount = new FileInputStream("keys/mozo-1ddd9-firebase-adminsdk-zzis3-01d104e885.json");
		
		FirebaseOptions options1 = new FirebaseOptions.Builder()
		    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
		    .build();
		FirebaseApp.initializeApp(options1);
		
		db = FirestoreClient.getFirestore();

	}
	

}
