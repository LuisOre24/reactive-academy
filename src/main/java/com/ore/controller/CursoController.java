package com.ore.controller;


import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.linkTo;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.methodOn;
import static reactor.function.TupleUtils.function;

import java.net.URI;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.ore.document.Curso;
import com.ore.service.ICursoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cursos")
public class CursoController {

	@Autowired
	private ICursoService service;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<Curso>>> listar() {
		Flux<Curso> fluxCurso = service.listar();
		return Mono.just(ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(fluxCurso)
				);
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Curso>> listarPorId(@PathVariable("id") String id){
		return service.listarPorId(id)
				.map(p -> ResponseEntity.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(p)
				).defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public Mono<ResponseEntity<Curso>> registrar(@RequestBody Curso obj, final ServerHttpRequest req){
		return service.registrar(obj)
				.map(p -> ResponseEntity.created(URI.create(req.getURI().toString().concat("/").concat(p.getId())))
						.body(p)
						).defaultIfEmpty(ResponseEntity.notFound().build());
	}
	 
	@PutMapping
	public Mono<ResponseEntity<Curso>> actualizar(@RequestBody Curso obj){
		return service.actualizar(obj)
				.map(p -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(p)
						);
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> eliminar(@PathVariable("id") String id){
		return service.listarPorId(id)
				.flatMap(p -> {
					return service.eliminar(p.getId())
							.then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
				})
				.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
	
	//Hateoas
		private Curso cursoHateoas;
		
		@GetMapping("/hateoas/{id}")
		public Mono<EntityModel<Curso>> listarHateoasPorId(@PathVariable("id") String id){
			Mono<Link> link1 = linkTo(methodOn(CursoController.class).listarHateoasPorId(id)).withSelfRel().toMono();
			Mono<Link> link2 = linkTo(methodOn(CursoController.class).listarHateoasPorId(id)).withSelfRel().toMono();
			
			/*return service.listarPorId(id)
					.flatMap(p -> {
						this.cursoHateoas = p;
						return link1;
					}).map(links -> {
						return EntityModel.of(this.cursoHateoas, links);
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
