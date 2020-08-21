package com.ore.document;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "matricula")
public class Matricula {

	@Id
	private String id;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Field(name = "FECHA_MATRICULA")
	private LocalDateTime fechaMatricula;
	
	@Field(name = "ESTUDIANTE")
	private Estudiante estudiante;
	
	@Field(name = "CURSO")
	private List<Curso> curso;
	
	@Field(name = "ESTADO")
	private boolean estado;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public LocalDateTime getFechaMatricula() {
		return fechaMatricula;
	}
	public void setFechaMatricula(LocalDateTime fechaMatricula) {
		this.fechaMatricula = fechaMatricula;
	}
	public Estudiante getEstudiante() {
		return estudiante;
	}
	public void setEstudiante(Estudiante estudiante) {
		this.estudiante = estudiante;
	}
	public List<Curso> getCurso() {
		return curso;
	}
	public void setCurso(List<Curso> curso) {
		this.curso = curso;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	
}
