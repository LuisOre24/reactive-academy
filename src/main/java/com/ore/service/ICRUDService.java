package com.ore.service;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.data.domain.Pageable;

import com.ore.pagination.PageSupport;

public interface ICRUDService<T,ID> {

	Mono<T> registrar(T obj);
	Mono<T> actualizar(T obj);
	Flux<T> listar();
	Mono<T> listarPorId(ID id);
	Mono<Void> eliminar(ID id);
	Mono<PageSupport<T>> listarPage(Pageable page);
	
}
