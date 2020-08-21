package com.ore.controller;

import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;
import static reactor.function.TupleUtils.function;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ore.document.Matricula;
import com.ore.service.IMatriculaService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {

	@Autowired
	private IMatriculaService service;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<Matricula>>> listar(){
		Flux<Matricula> fluxMatricula = service.listar();
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(fluxMatricula)
				);
	}
		
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Matricula>> listarPorId(@PathVariable("id") String id){
		return service.listarPorId(id)
				.map(p -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(p)
						//).defaultIfEmpty(ResponseEntity.notFound().build());
						).defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	
	}
	
	@PostMapping
	public Mono<ResponseEntity<Matricula>> registrar(@RequestBody Matricula obj, final ServerHttpRequest req){
		return service.registrar(obj)
				.map(p -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(p)
						);

	}
	
	@PutMapping
	public Mono<ResponseEntity<Matricula>> actualizar(@RequestBody Matricula obj){
		return service.actualizar(obj)
				.map(p -> ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(p)
				);
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> eliminar(@PathVariable("id") String id){
		return service.listarPorId(id)
				.flatMap(p->{
					return service.eliminar(p.getId())
							.then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
				}).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
	
	//Hateoas
			private Matricula matriculaHateoas;
			
			@GetMapping("/hateoas/{id}")
			public Mono<EntityModel<Matricula>> listarHateoasPorId(@PathVariable("id") String id){
				Mono<Link> link1 = linkTo(methodOn(MatriculaController.class).listarHateoasPorId(id)).withSelfRel().toMono();
				Mono<Link> link2 = linkTo(methodOn(MatriculaController.class).listarHateoasPorId(id)).withSelfRel().toMono();
				
				/*return service.listarPorId(id)
						.flatMap(p -> {
							this.matriculaHateoas = p;
							return link1;
						}).map(links -> {
							return EntityModel.of(this.matriculaHateoas, links);
						});*/
				
				//CON MAP DENTRO DE UN MONO //PRACTICA INTERMEDIA
				/*return service.listarPorId(id).flatMap(p -> {
					return link1.map(links -> EntityModel.of(p, links));
				});*/
				
				//CON ZIP WITH //PRACTICA IDEAL
				//return service.listarPorId(id).zipWith(link1, (p, links) -> EntityModel.of(p, links));
				
				//CON MULTIPLE LINKS
				return link1.zipWith(link2)
						.map(function((left, rigth) -> Links.of(left, rigth)))
						.zipWith(service.listarPorId(id), (links, p) -> EntityModel.of(p, links));
				
			}
	
}
