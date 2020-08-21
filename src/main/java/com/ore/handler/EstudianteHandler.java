package com.ore.handler;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ore.document.Estudiante;
import com.ore.service.IEstudianteService;

import reactor.core.publisher.Mono;

@Component
public class EstudianteHandler {

	@Autowired
	private IEstudianteService service;
	
	
	public Mono<ServerResponse> listar(ServerRequest req){
		
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(service.listar(),Estudiante.class);
	}
	
	public Mono<ServerResponse> listarPorId(ServerRequest req){
		String id = req.pathVariable("id");
		return service.listarPorId(id)
				.flatMap(e -> ServerResponse.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(e))	
						)
						.switchIfEmpty(ServerResponse
								.notFound()
								.build()
						);
		
	}
	
	public Mono<ServerResponse> registrar(ServerRequest req){
		Mono<Estudiante> estudianteMono = req.bodyToMono(Estudiante.class);
		
		return estudianteMono
				.flatMap(service::registrar)
				.flatMap(e -> ServerResponse
						.created(URI.create(req.uri().toString().concat("/").concat(e.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(e))
						);
	}
	
	public Mono<ServerResponse> actualizar(ServerRequest req){
		Mono<Estudiante> estudianteMono = req.bodyToMono(Estudiante.class);
		
		return estudianteMono
				.flatMap(service::actualizar)
				.flatMap(e -> ServerResponse
						.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(fromValue(e))
						);
	}
	
	public Mono<ServerResponse> eliminar(ServerRequest req){
		String id = req.pathVariable("id");
		
		return service.listarPorId(id)
				.flatMap(e -> service.eliminar(e.getId())
				.then(ServerResponse.noContent()
						.build()
						)
				).switchIfEmpty(ServerResponse.notFound()
						.build()
						);
				
		
	}
	
}
