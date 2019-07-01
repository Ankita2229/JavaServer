package com.mc.Mozo.model;

import java.util.ArrayList;
import java.util.Date;

//import java.util.Date;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
"name",
"username",
"gender",
"email",
"dob",
"age",
"status",
"last_active",
"min_age",
"latitude",
"latitude",
"max_age",
"max_range",
"interested_gender",
"pagination",
"que",
"image_url",
"created_at"
})

public class Profile {
	
	@JsonProperty("name")
	private String name;
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("gender")
	private String gender;
	
	@JsonProperty("email")
	private String email;
	
	@JsonProperty("dob")
	private String dob;
		
	@JsonProperty("age")
	private String age;
	
	@JsonProperty("status")
	private String status;

	@JsonProperty("latitude")
	private String latitude;
	
	@JsonProperty("longitude")
	private String longitude;
	
	@JsonProperty("last_active")
	private String last_active;
	
	@JsonProperty("min_age")
	private String min_age;
	
	@JsonProperty("max_age")
	private String max_age;
	
	@JsonProperty("max_range")
	private String max_range;
	
	@JsonProperty("interested_gender")
	private String interested_gender;
	
	@JsonProperty("pagination")
	private String pagination;
	
	@JsonProperty("created_at")
	private Date created_at;
	
	@JsonProperty("que")
	private ArrayList<String> que;
	
	@JsonProperty("image_url")
	private ArrayList<String> image_url;

	public ArrayList<String> getImage_url() {
		return image_url;
	}

	public void setImage_url(ArrayList<String> image_url) {
		this.image_url = image_url;
	}
	
	public ArrayList<String> getQue() {
		return que;
	}

	public void setQue(ArrayList<String> que) {
		this.que = que;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreated_at()
	{
		return created_at;
	}
	
	public void setCreated_at(Date created_at)
	{
		this.created_at = created_at;
	}
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLast_active() {
		return last_active;
	}

	public void setLast_active(String last_active) {
		this.last_active = last_active;
	}

	public String getMin_age() {
		return min_age;
	}

	public void setMin_age(String min_age) {
		this.min_age = min_age;
	}

	public String getMax_age() {
		return max_age;
	}

	public void setMax_age(String max_age) {
		this.max_age = max_age;
	}

	public String getMax_range() {
		return max_range;
	}

	public void setMax_range(String max_range) {
		this.max_range = max_range;
	}

	public String getInterested_gender() {
		return interested_gender;
	}

	public void setInterested_gender(String interested_gender) {
		this.interested_gender = interested_gender;
	}

	public String getPagination() {
		return pagination;
	}

	public void setPagination(String pagination) {
		this.pagination = pagination;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
}

