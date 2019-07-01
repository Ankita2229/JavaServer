package com.mc.Mozo.services;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.GeoPoint;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;

import com.mc.Mozo.model.Profile;
import com.mc.Mozo.model.User_Wrapper;

import io.grpc.StatusRuntimeException;

@Service
public class Repo {
	 @Autowired Initialize firebase;
	 
	 public void saveUser(String data) throws Exception {
		 JSONObject object;
			try {
				object = (JSONObject) new JSONParser().parse(data);
				String id = object.get("id").toString();
				String user = object.get("user").toString();
				
				ObjectMapper mapper1 = new ObjectMapper();
				Profile p = mapper1.readValue(user, Profile.class);
				
				//Get latitude and longitude
				double lat = Double.valueOf(p.getLatitude());
				double lng = Double.valueOf(p.getLongitude());
				
				//Create GeoPoint and update it to the database
				GeoPoint geoPoint = new GeoPoint(lat, lng);
				Map<String, Object> docData1 = new HashMap<>();
				docData1.put("glocation", geoPoint);
				ApiFuture<WriteResult> f = firebase.db.collection("User_Location").document(id).set(docData1);
				
				Map<String, Object> docData = new HashMap<>();
				docData.put("id", id);

				p.setCreated_at(new Date());

				ApiFuture<WriteResult> future = firebase.db.collection("User").document(id).set(p);
				System.out.println("Update time : " + future.get().getUpdateTime());
								
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 }
	 
	 public void saveUsername(String data) throws Exception {
		 JSONObject object;
			try {
				object = (JSONObject) new JSONParser().parse(data);
				String id = object.get("id").toString();
				Map<String, Object> docData = new HashMap<>();
				docData.put("id", id);
				String username = object.get("username").toString();
				ApiFuture<WriteResult> future = firebase.db.collection("Username").document(username).set(docData);
				System.out.println("Update time : " + future.get().getUpdateTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 }
	 
	 public String checkUsername(String username) throws Exception {		
		 JSONObject object;
		 	object = (JSONObject) new JSONParser().parse(username);
			String uname = object.get("username").toString();
			DocumentReference ref = firebase.db.collection("Username").document(uname);
			ApiFuture<DocumentSnapshot> future = ref.get();
			DocumentSnapshot document = future.get();	
			if(document.exists()) {
				// username exists
				object.put("result", "true");
				String message = object.toString();			
				return message;
			}else {
				object.put("result", "false");
				String message = object.toString();	
				return message;
			}
	 }

	 public Profile getData(String id) throws Exception {
		 JSONObject obj1;
		 obj1 = (JSONObject) new JSONParser().parse(id);
		 String id1 = obj1.get("id").toString();
		 //System.out.println(id1);
		 Profile p = new Profile();
		 ObjectMapper mapper1 = new ObjectMapper();
		 mapper1.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).
		 	configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		 
		 DocumentReference docRef = firebase.db.collection("User").document(id1);
		 ApiFuture<DocumentSnapshot> future = docRef.get();
		 DocumentSnapshot document = future.get();
		 if (document.exists()) 
		 {
			  mapper1.writeValue(new File("Temp/user.json"), document.getData());
			  p = mapper1.readValue(new File("Temp/user.json"), Profile.class);
		 } 
		 else 
		 {
			 System.out.println("No such document!");
		 }
		 return p;
	 }	 
	 
	 //********************************************
	 //To retrive multiple user profiles
	 //Pagination
	 //*******pagenumber update thing is left
	 //Is no more needed
	 //Only explore and nearby needed
	 
	 public List<Profile> getProfiles(String query_data) throws Exception{ 
		 	//INPUT FORMAT: {"field" : "gender", "value":"Female"}
		 	JSONObject obj1;
		 	obj1 = (JSONObject) new JSONParser().parse(query_data);
			String field = obj1.get("field").toString();
			String value = obj1.get("value").toString();
		 	
			Profile user_data = new Profile();
			List<Profile> user_profile_list = new ArrayList<>();
			//create to json 
			ObjectMapper obj = new ObjectMapper();
			obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			
			//Query
			CollectionReference users = firebase.db.collection("User");
			Query firstPage = users.whereEqualTo(field, value).limit(10);
			 
			while(true){
				//For pagination
				// Wait for the results of the API call, waiting for a maximum of 30 seconds for a result.
				ApiFuture<QuerySnapshot> future_2 = firstPage.get();
				List<QueryDocumentSnapshot> docs3 = future_2.get(30, TimeUnit.SECONDS).getDocuments();
					
				for(DocumentSnapshot d : docs3) {	
					String user_id = d.getId();
					obj1.put("id", user_id);
					String data = obj1.toJSONString();
					getData(data);
					user_data = obj.readValue(new File("Temp/user.json"), Profile.class);
					user_profile_list.add(user_data);	
				}
				if(docs3.size()>=1) 
				{
					QueryDocumentSnapshot lastDoc = docs3.get(docs3.size() - 1);
					firstPage = firstPage.startAfter(lastDoc);
				}
				else
				{
					break;
				}
			}
			return user_profile_list;	 	 
		}

	 //EXPLORE QUERY
	 public List<User_Wrapper> Explore(String query_data) throws Exception{ 
		 	
		 //INPUT - user doc in json with id field
		 	JSONObject obj1;
		 	obj1 = (JSONObject) new JSONParser().parse(query_data);
		 	
			String id = obj1.get("id").toString();
			String user = obj1.get("user").toString();
			int count = 0;
			
			//user passed is in p
			ObjectMapper mapper1 = new ObjectMapper();
			Profile p = mapper1.readValue(user, Profile.class);
			
			
			String interested_gender = p.getInterested_gender();
			String min_age = p.getMin_age();
			String max_age = p.getMax_age();
			String pagination = p.getPagination();

			int page = Integer.parseInt(pagination);
		 	
			Profile user_data = new Profile();
			
			List<Profile> user_profile_list = new ArrayList<>();
			List<User_Wrapper> user_wrapper = new ArrayList<>();
			

			ObjectMapper obj = new ObjectMapper();
			obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			
			//Query
			CollectionReference users = firebase.db.collection("User");
			Query firstPage = users.
					whereGreaterThanOrEqualTo("age", min_age).
					whereLessThanOrEqualTo("age", max_age).
					whereEqualTo("gender", interested_gender).
					orderBy("age").
					offset((page-1)*2).
					limit(10);
			 
			while(true){
				//For pagination
				// Wait for the results of the API call, waiting for a maximum of 30 seconds for a result.
				ApiFuture<QuerySnapshot> future_2 = firstPage.get();
				List<QueryDocumentSnapshot> docs3 = future_2.get(30, TimeUnit.SECONDS).getDocuments();
				//user_wrapper.clear();
				for(DocumentSnapshot d : docs3) {
					User_Wrapper temp_user_wrapper = new User_Wrapper();
					String user_id = d.getId();
					obj1.put("id", user_id);
					String data = obj1.toJSONString();
					getData(data);
					
					user_data = obj.readValue(new File("Temp/user.json"), Profile.class);
					
					String status_1 = user_data.getStatus();
					if(status_1.equals("3"))
					{
						continue;
					}
					
					
					temp_user_wrapper.setId(user_id);
					temp_user_wrapper.setUser(user_data);
	
					User_Wrapper u = mapper1.readValue(data, User_Wrapper.class);
					user_wrapper.add(u);
					
				}
				if(docs3.size()>=1) 
				{
					QueryDocumentSnapshot lastDoc = docs3.get(docs3.size() - 1);
					firstPage = users.whereGreaterThanOrEqualTo("age", min_age).
								whereLessThanOrEqualTo("age", max_age).
								whereEqualTo("gender", interested_gender).
								orderBy("age").
								limit(10).startAfter(lastDoc);
					
					page = page + 1;
					
					//get current date when update is performed
					SimpleDateFormat sfd = new SimpleDateFormat("MM-dd-yyyy");
					String last_active = sfd.format(new Date().getTime());
					pagination = String.valueOf(page);
					
					ApiFuture<WriteResult> f1 = firebase.db.collection("User").document(id).update("pagination", pagination, "last_active", last_active);
				}
				else 
				{
					
					break;
				}
			}
			return user_wrapper;
		}
	 
	 
	 //Nearby Query
	 public List<User_Wrapper> Nearby(String query_data) throws Exception{
		 
		 JSONObject object;
		 object = (JSONObject) new JSONParser().parse(query_data);
		 String id = object.get("id").toString();
		 String user = object.get("user").toString();
		 Map<String, Object> docData = new HashMap<>();
		 docData.put("id", id);
			
		 	//Get user_id which are within given range of miles provided by current user
		 	//user passed is in p
			ObjectMapper mapper1 = new ObjectMapper();
			Profile p = mapper1.readValue(user, Profile.class);
			List<User_Wrapper> user_wrapper = new ArrayList<>();
			String interested_gender = p.getInterested_gender();
			String max_range = p.getMax_range();
		
			//1 mile to degrees
			DocumentReference docRef = firebase.db.collection("User_Location").document(id);
			ApiFuture<DocumentSnapshot> future = docRef.get();
			DocumentSnapshot document = future.get();
			GeoPoint location = document.getGeoPoint("glocation");
		 	
		 	double lat = location.getLatitude();
		 	double lng = location.getLongitude();
		 	
			double max_r = Double.parseDouble(max_range);
			
			//LAT-LNG-NEARBY
			// ~1 mile of lat and lon in degrees
		    double latitude = 0.0144927536231884;
		    double longitude = 0.0181818181818182;

		    double lowerLat = lat - (latitude * max_r);
		    double lowerLng = lng - (longitude * max_r);

		    double greaterLat = lat + (latitude * max_r);
		    double greaterLng = lng + (longitude * max_r);

		    //geopoint
		    GeoPoint g_min = new GeoPoint(lowerLat, lowerLng);
		    GeoPoint g_max = new GeoPoint(greaterLat, greaterLng);
		    
		 	
			Profile user_data = new Profile();
			List<Profile> user_profile_list = new ArrayList<>();
			List<User_Wrapper> user_wrapper_list = new ArrayList<>();
			List<String> user_list = new ArrayList<>();

			//To get user data
			ObjectMapper obj = new ObjectMapper();
			obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			
			//Query
			try {
			CollectionReference users_loc = firebase.db.collection("User_Location");
			Query firstPage = users_loc.
					whereGreaterThanOrEqualTo("glocation", g_min).
					whereLessThanOrEqualTo("glocation", g_max).
					//whereEqualTo("gender", interested_gender).
					//orderBy("age").
					limit(5);

			
			ApiFuture<QuerySnapshot> future_2 = firstPage.get();
			List<QueryDocumentSnapshot> docs3 = future_2.get(30, TimeUnit.SECONDS).getDocuments();
			for(DocumentSnapshot d : docs3) {	
				String user_id = d.getId();
				user_list.add(user_id);	
			}
			}catch(StatusRuntimeException e) {
				e.printStackTrace();
			}
		System.out.println(user_list);
		for(String u_id : user_list)
		{
			DocumentReference dRef = firebase.db.collection("User").document(u_id);
			ApiFuture<DocumentSnapshot> f = dRef.get();
			DocumentSnapshot doc = f.get();
			//System.out.println(doc.exists());
			if(doc.exists() && !id.equals(doc.getId()))
			{
				object.put("id", u_id);
				String data1 = object.toJSONString();
				getData(data1);
				User_Wrapper u1 = new User_Wrapper();
				User_Wrapper u = mapper1.readValue(data1, User_Wrapper.class);
				user_data = obj.readValue(new File("Temp/user.json"), Profile.class);
				String status_1 = user_data.getStatus();
				
				String g = user_data.getGender();
				if(status_1.equals("3") || !interested_gender.equals(g))
				{
					continue;
				}
				user_wrapper_list.add(u);
			}
		}
		return user_wrapper_list;	 
	 }
	 
	 //Find Match
	 public String findMatch(String param) throws Exception {
				JSONObject object;
				try {
					object = (JSONObject) new JSONParser().parse(param);
					String id1 = object.get("id1").toString();
					Map<String, Object> docData = new HashMap<>();
					docData.put("id", id1);
					
					String id2 = object.get("id2").toString();
					
					 DocumentReference docRef = firebase.db.collection("Liked_by").document(id1).collection("person").document(id2);
					 ApiFuture<DocumentSnapshot> future = docRef.get();
					 DocumentSnapshot document = future.get();
					 if (document.exists()) 
					 {
						 System.out.println("Hi!! You found me!");
						 //Now add both in match collection
						 
						 //DocumentReference doc_id1 = firebase.db.collection("Matches").document(id1).collection("person_match").document(id2);
						 Map<String, Object> doc_id1_data = new HashMap<>();
						 Date date_match = new Date();
						 doc_id1_data.put("timestamp", date_match);
						 
						 //Adding id2 to id1
						 ApiFuture<WriteResult> f_id1 = firebase.db.collection("Matches").document(id1).collection("person_match").document(id2).set(doc_id1_data);
						 
						 //Adding id1 to id2
						 ApiFuture<WriteResult> f_id2 = firebase.db.collection("Matches").document(id2).collection("person_match").document(id1).set(doc_id1_data);
						 
						 //Remove id2 from id1 person-subcoleection of id1
						 ApiFuture<WriteResult> writeResult = firebase.db.collection("Liked_by").document(id1).collection("person").document(id2).delete();
		
					 } 
					 else 
					 {	
						 Map<String, Object> doc_data = new HashMap<>();
						 Date liked_date = new Date();
						 doc_data.put("timestamp", liked_date);
						 DocumentReference ref1 = firebase.db.collection("Liked_by").document(id2).collection("person").document(id1);
						 ApiFuture<WriteResult> f1 = ref1.set(doc_data);
					 }
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
	 	
	 	//Get Likes
		public List<Profile> getLikes(String param) throws Exception {
			// TODO Auto-generated method stub
			JSONObject obj1;
			obj1 = (JSONObject) new JSONParser().parse(param);
			String id1 = obj1.get("id1").toString();
			
			CollectionReference users = firebase.db.collection("Liked_by").document(id1).collection("person");
			Profile like_data = new Profile();
			List<Profile> user_profile_list = new ArrayList<>();
			ObjectMapper obj = new ObjectMapper();
			obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
	
			ApiFuture<QuerySnapshot> future_2 = users.get();
			List<QueryDocumentSnapshot> docs3 = future_2.get().getDocuments();
			for(DocumentSnapshot d : docs3) {	
				String user_id = d.getId().toString();
				
				obj1.put("id", user_id);
				String data = obj1.toJSONString();
				getData(data);
				
				like_data = obj.readValue(new File("Temp/user.json"), Profile.class);
				user_profile_list.add(like_data);	
			}
			return user_profile_list;
		}

		//Get Matches
		public List<Profile> getMatches(String param) throws Exception{
			// TODO Auto-generated method stub
			JSONObject obj1;
			obj1 = (JSONObject) new JSONParser().parse(param);
			String id1 = obj1.get("id1").toString();
						
			CollectionReference users = firebase.db.collection("Matches").document(id1).collection("person_match");
			Profile match_data = new Profile();
			List<Profile> user_profile_list = new ArrayList<>();
			ObjectMapper obj = new ObjectMapper();
			obj.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
	
			ApiFuture<QuerySnapshot> future_2 = users.get();
			List<QueryDocumentSnapshot> docs3 = future_2.get().getDocuments();
			for(DocumentSnapshot d : docs3) {	
				String user_id = d.getId().toString();
				
				obj1.put("id", user_id);
				String data = obj1.toJSONString();
				getData(data);
				
				match_data = obj.readValue(new File("Temp/user.json"), Profile.class);
				user_profile_list.add(match_data);
			}
			return user_profile_list;
		}
	
}
