package com.bridgelabz.noteservice.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Response {
	
	String message;
	int status;
	Object data;
		public Response(String message,Object user,int status) 
		{
		this.message=message;
		this.status=status;
		this.data=user;
		}
	
	
	

}