package com.ore.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ore.repo.IEstudianteRepo;
import com.ore.repo.IGenericRepo;
import com.ore.document.Estudiante;
import com.ore.service.IEstudianteService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EstudianteServiceImpl extends CRUDImpl<Estudiante,String> implements IEstudianteService{

	@Autowired
	private IEstudianteRepo repo;
	
	@Override
	protected IGenericRepo<Estudiante,String> getRepo(){
		return repo;
	}

	@Override
	public Mono<Estudiante> registrar(Estudiante obj) {
		return repo.save(obj);
	}

	@Override
	public Mono<Estudiante> actualizar(Estudiante obj) {
		return repo.save(obj);
	}

	@Override
	public Flux<Estudiante> listar() {
		
		return repo.findAll();
	}

	@Override
	public Mono<Estudiante> listarPorId(String id) {
		return repo.findById(id);
	}

	@Override
	public Mono<Void> eliminar(String id) {
		return repo.deleteById(id);
	}

}
