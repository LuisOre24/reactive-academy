package com.ore.handler;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ore.document.Matricula;
import com.ore.service.IMatriculaService;

import reactor.core.publisher.Mono;

@Component
public class MatriculaHandler {

	@Autowired
	private IMatriculaService service;
	
	
	public Mono<ServerResponse> listar(ServerRequest req){
		
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(service.listar(),Matricula.class);
	}
	
	public Mono<ServerResponse> listarPorId(ServerRequest req){
		String id = req.pathVariable("id");
		return service.listarPorId(id)
				.flatMap(m -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(m))	
						)
						.switchIfEmpty(ServerResponse
								.notFound()
								.build()
						);
		
	}
	
	public Mono<ServerResponse> registrar(ServerRequest req){
		Mono<Matricula> matriculaMono = req.bodyToMono(Matricula.class);
		
		return matriculaMono
				.flatMap(service::registrar)
				.flatMap(m -> ServerResponse
						.created(URI.create(req.uri().toString().concat("/").concat(m.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(m))
						);
	}
	
	public Mono<ServerResponse> actualizar(ServerRequest req){
		Mono<Matricula> matriculaMono = req.bodyToMono(Matricula.class);
		
		return matriculaMono
				.flatMap(service::actualizar)
				.flatMap(m -> ServerResponse
						.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(m))
						);
	}
	
	public Mono<ServerResponse> eliminar(ServerRequest req){
		String id = req.pathVariable("id");
		
		return service.listarPorId(id)
				.flatMap(m -> service.eliminar(m.getId())
				.then(ServerResponse.noContent()
						.build()
						)
				).switchIfEmpty(ServerResponse.notFound()
						.build()
						);
				
		
	}
	
	
}
