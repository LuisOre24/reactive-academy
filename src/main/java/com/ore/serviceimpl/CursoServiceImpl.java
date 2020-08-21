package com.ore.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ore.repo.ICursoRepo;
import com.ore.repo.IGenericRepo;
import com.ore.document.Curso;
import com.ore.service.ICursoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CursoServiceImpl extends CRUDImpl<Curso,String> implements ICursoService{

	@Autowired
	private ICursoRepo repo;
	
	@Override
	protected IGenericRepo<Curso,String> getRepo(){
		return repo;
	}
	
	@Override
	public Mono<Curso> registrar(Curso obj) {
		return repo.save(obj);
	}
	
	@Override
	public Mono<Curso> actualizar(Curso obj) {
		return repo.save(obj);
	}

	@Override
	public Flux<Curso> listar() {
		return repo.findAll();
	}

	@Override
	public Mono<Curso> listarPorId(String id) {
		return repo.findById(id);
	}

	@Override
	public Mono<Void> eliminar(String id) {
		return repo.deleteById(id);
	}



	
	
}
