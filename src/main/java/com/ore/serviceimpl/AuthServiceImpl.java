package com.ore.serviceimpl;

import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl {

	public boolean tieneAcceso(String path) {
		//SecurityContext getUser
		return true;
	}
}
