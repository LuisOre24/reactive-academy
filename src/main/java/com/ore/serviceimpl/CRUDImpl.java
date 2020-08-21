package com.ore.serviceimpl;

import com.ore.repo.IGenericRepo;
import com.ore.service.ICRUDService;

import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;

import com.ore.pagination.PageSupport;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public abstract class CRUDImpl<T,ID> implements ICRUDService<T,ID> {

	protected abstract IGenericRepo<T,ID> getRepo();

	@Override
	public Mono<T> registrar(T obj) {
		return getRepo().save(obj);
	}

	@Override
	public Mono<T> actualizar(T obj) {
		return getRepo().save(obj);
	}

	@Override
	public Flux<T> listar() {
		return getRepo().findAll();
	}

	@Override
	public Mono<T> listarPorId(ID id) {
		return getRepo().findById(id);
	}

	@Override
	public Mono<Void> eliminar(ID id) {
		return getRepo().deleteById(id);
	}
	

	public Mono<PageSupport<T>> listarPage(Pageable page) {
		return getRepo().findAll()
				.collectList()
				.map(list -> new PageSupport<>(
						list
						.stream()
						.skip(page.getPageNumber() * page.getPageSize())
						.limit(page.getPageSize())
						.collect(Collectors.toList()),
					page.getPageNumber(), page.getPageSize(), list.size()						
					));
	}
	
}
