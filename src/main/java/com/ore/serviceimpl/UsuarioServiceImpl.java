package com.ore.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ore.document.Usuario;
import com.ore.repo.IGenericRepo;
import com.ore.repo.IRolRepo;
import com.ore.repo.IUsuarioRepo;
import com.ore.security.User;
import com.ore.service.IUsuarioService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Clase S8
@Service
public class UsuarioServiceImpl extends CRUDImpl<Usuario, String> implements IUsuarioService{

	@Autowired
	private IUsuarioRepo repo;
	
	@Autowired
	private IRolRepo rolRepo;
	
	@Override
	protected IGenericRepo<Usuario, String> getRepo() {
		return repo; 
	}
	
	@Override
	public Mono<User> buscarPorUsuario(String usuario) {
		Mono<Usuario> monoUsuario = repo.findOneByUsuario(usuario);
		
		List<String> roles = new ArrayList<String>();
		
		return monoUsuario.flatMap(u -> {
			return Flux.fromIterable(u.getRoles())
					.flatMap(rol -> {
						return rolRepo.findById(rol.getId())
								.map(r -> {
									roles.add(r.getNombre());
									return r;
								});
					}).collectList().flatMap(list -> {
						u.setRoles(list);
						return Mono.just(u);
					});
		})	
		.flatMap(u -> {			
			return Mono.just(new User(u.getUsuario(), u.getClave(), u.getEstado(), roles));
		});
	}

	
}
