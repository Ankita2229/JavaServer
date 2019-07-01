package com.mc.Mozo.services;

import java.util.List;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mc.Mozo.model.Profile;
import com.mc.Mozo.model.User_Wrapper;

@Path("/api")
@Component
@Produces(value = { MediaType.APPLICATION_JSON })
public class Endpoint {

	@Autowired Repo repo;

	@POST
	@Path("/saveUser")
	public Response saveUser(String data) throws Exception {
		repo.saveUser(data);
		return Response.ok("data posted..").build();
	}
	
	@POST
	@Path("/get")
	public Response getData(String id) throws Exception {
		Profile data = repo.getData(id);
		return Response.ok(data).build();
	}
	
	@POST
	@Path("/checkUsername")
	public Response checkUsername(String username) throws Exception {
		String data = repo.checkUsername(username);
		return Response.ok(data).build();
	}
	
	@POST
	@Path("/saveUsername")
	public Response saveUsername(String data) throws Exception {
		//System.out.println(data);
		repo.saveUsername(data);
		return Response.ok("data posted..").build();
	}
	
	@POST
	@Path("/getUserProfiles")
	public Response getProfiles(String param) throws Exception {
		List<Profile> data1 = repo.getProfiles(param);
		return Response.ok(data1).build();
	}
	
	@POST
	@Path("/Explore")
	public Response Explore(String param) throws Exception {
		List<User_Wrapper> data1 = repo.Explore(param);
		return Response.ok(data1).build();
	}
	
	@POST
	@Path("/getNearby")
	public Response Nearby(String param) throws Exception{
		List<User_Wrapper> data1 = repo.Nearby(param);
		return Response.ok(data1).build();
	}

	@POST
	@Path("/findMatch")
	public void findMatch(String param) throws Exception {
		String data1 = repo.findMatch(param);
	}
	
	@POST
	@Path("/getLikes")
	public Response getLikes(String param) throws Exception {
		List<Profile> data1 = repo.getLikes(param);
		return Response.ok(data1).build();
	}
	
	@POST
	@Path("/getMatches")
	public Response getMatches(String param) throws Exception {
		List<Profile> data1 = repo.getMatches(param);
		return Response.ok(data1).build();
	}
	
}
