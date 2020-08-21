package com.ore;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.ore.handler.CursoHandler;
import com.ore.handler.EstudianteHandler;
import com.ore.handler.MatriculaHandler;

@Configuration
public class RouterConfig {

	@Bean
	public RouterFunction<ServerResponse> routeCursos(CursoHandler handler){
		return route(GET("/v1/cursos"),handler::listar)           //request -> handler.listar(request)
				.andRoute(GET("/v1/cursos/{id}"), handler::listarPorId)
				.andRoute(POST("/v1/cursos"), handler::registrar)
				.andRoute(PUT("/v1/cursos"), handler::actualizar)
				.andRoute(DELETE("/v1/cursos/{id}"), handler::eliminar);
	}
	
	@Bean
	public RouterFunction<ServerResponse> routeEstudiantes(EstudianteHandler handler){
		return route(GET("/v1/estudiantes"),handler::listar)           //request -> handler.listar(request)
				.andRoute(GET("/v1/estudiantes/{id}"), handler::listarPorId)
				.andRoute(POST("/v1/estudiantes"), handler::registrar)
				.andRoute(PUT("/v1/estudiantes"), handler::actualizar)
				.andRoute(DELETE("/v1/estudiantes/{id}"), handler::eliminar);
	}
	
	@Bean
	public RouterFunction<ServerResponse> routeMatriculas(MatriculaHandler handler){
		return route(GET("/v1/matriculas"),handler::listar)           //request -> handler.listar(request)
				.andRoute(GET("/v1/matriculas/{id}"), handler::listarPorId)
				.andRoute(POST("/v1/matriculas"), handler::registrar)
				.andRoute(PUT("/v1/matriculas"), handler::actualizar)
				.andRoute(DELETE("/v1/matriculas/{id}"), handler::eliminar);
	}
	
}
