package com.ore.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ore.repo.IGenericRepo;
import com.ore.repo.IMatriculaRepo;
import com.ore.document.Matricula;
import com.ore.service.IMatriculaService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MatriculaServiceImpl extends CRUDImpl<Matricula,String> implements IMatriculaService{

	@Autowired 
	private IMatriculaRepo repo;
	
	@Override
	protected IGenericRepo<Matricula,String> getRepo(){
		return repo;
	}
	
	@Override
	public Mono<Matricula> registrar(Matricula obj) {
		return repo.save(obj);
	}

	@Override
	public Mono<Matricula> actualizar(Matricula obj) {
		return repo.save(obj);
	}

	@Override
	public Flux<Matricula> listar() {
		return repo.findAll();
	}

	@Override
	public Mono<Matricula> listarPorId(String id) {
		return repo.findById(id);
	}

	@Override
	public Mono<Void> eliminar(String id) {
		return repo.deleteById(id);
	}


}
