package com.ore.service;

import com.ore.document.Usuario;
import com.ore.security.User;

import reactor.core.publisher.Mono;

public interface IUsuarioService extends ICRUDService<Usuario, String>{
	
	Mono<User> buscarPorUsuario(String usuario);
}
