package com.bridgelabz.noteservice.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExceptionResponse {
	String message;
	HttpStatus status;
	Object data;
		public ExceptionResponse(String message,Object user,HttpStatus status) 
		{
		this.message=message;
		this.data=user;
		this.status=status;
		}
}
