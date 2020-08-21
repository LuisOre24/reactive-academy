package com.ore.handler;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ore.document.Curso;
import com.ore.service.ICursoService;
import com.ore.validators.RequestValidator;

import reactor.core.publisher.Mono;

@Component
public class CursoHandler {

	@Autowired
	private ICursoService service;
	
	@Autowired
	private RequestValidator validadorGeneral;
	
	
	public Mono<ServerResponse> listar(ServerRequest req){
		
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(service.listar(),Curso.class);
	}
	
	public Mono<ServerResponse> listarPorId(ServerRequest req){
		String id = req.pathVariable("id");
		return service.listarPorId(id)
				.flatMap(c -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(c))	
						)
						.switchIfEmpty(ServerResponse
								.notFound()
								.build()
						);
		
	}
	
	public Mono<ServerResponse> registrar(ServerRequest req){
		Mono<Curso> cursoMono = req.bodyToMono(Curso.class);
		
		return cursoMono
				.flatMap(validadorGeneral::validate)
				.flatMap(service::registrar)
				.flatMap(c -> ServerResponse
						.created(URI.create(req.uri().toString().concat("/").concat(c.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(c))
						);
	}
	
	public Mono<ServerResponse> actualizar(ServerRequest req){
		Mono<Curso> cursoMono = req.bodyToMono(Curso.class);
		
		return cursoMono
				.flatMap(validadorGeneral::validate)
				.flatMap(service::actualizar)
				.flatMap(c -> ServerResponse
						.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(c))
						);
	}
	
	public Mono<ServerResponse> eliminar(ServerRequest req){
		String id = req.pathVariable("id");
		
		return service.listarPorId(id)
				.flatMap(c -> service.eliminar(c.getId())
				.then(ServerResponse.noContent()
						.build()
						)
				).switchIfEmpty(ServerResponse.notFound()
						.build()
						);
				
		
	}
}
