package com.servinte.axioma.generadorReporte.odontologia.dtoReportes;

import java.io.Serializable;

/**
 * Esta clase se encarga de recibir los datos que se crean en el generador del formato pdf de plantillas
 * odontológicas para organizar en un nuevo subreporte los detalles de las secciones mejorando
 * el ordenamiento de este formato
 * 
 * @author Fabian Becerra
 *
 */
public class DtoFormatoPlantillaOrdenarColumnasPDF implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo que almacena el tamano de la seccion o numero de columnas
	 */
	private String tamanoSeccion;
	
	/**
	 * Atributo que almacena la informacion en la columna 1 del reporte
	 */
	private String columnauno;
	
	/**
	 * Atributo que almacena la informacion en la columna 2 del reporte 
	 */
	private String columnados;
	
	/**
	 * Atributo que almacena la informacion en la columna 3 del reporte 
	 */
	private String columnatres;
	
	/**
	 * Atributo que almacena la informacion en la columna 4 del reporte 
	 */
	private String columnacuatro;
	
	/**
	 * Atributo que almacena la informacion en la columna 5 del reporte 
	 */
	private String columnacinco;
	/**
	 * Atributo que almacena la informacion en la columna 6 del reporte 
	 */
	private String columnaseis;
	/**
	 * Atributo que almacena la informacion en la columna 7 del reporte 
	 */
	private String columnasiete;
	/**
	 * Atributo que almacena la informacion en la columna 8 del reporte 
	 */
	private String columnaocho;
	
	/**
	 * Atributo que almacena la informacion en la columna unica del reporte 
	 */
	private String columnaunica;
	
	/**
	 * Atributo que almacena la informacion cuando son solo dos columnas (columna uno) en el reporte 
	 */
	private String columnadobleuno;
	
	/**
	 * Atributo que almacena la informacion cuando son solo dos columnas (columna dos) en el reporte 
	 */
	private String columnadobledos;
	
	/**
	 * Atributo que almacena la informacion cuando son solo tres columnas (columna uno) en el reporte 
	 */
	private String columnatripleuno;
	/**
	 * Atributo que almacena la informacion cuando son solo tres columnas (columna dos) en el reporte 
	 */
	private String columnatripledos;
	/**
	 * Atributo que almacena la informacion cuando son solo tres columnas (columna tres) en el reporte 
	 */
	private String columnatripletres;
	
public void setColumnauno(String columnauno) {
		this.columnauno = columnauno;
	}
	public String getColumnauno() {
		return columnauno;
	}
	public void setColumnados(String columnados) {
		this.columnados = columnados;
	}
	public String getColumnados() {
		return columnados;
	}
	public void setTamanoSeccion(String tamanoSeccion) {
		this.tamanoSeccion = tamanoSeccion;
	}
	public String getTamanoSeccion() {
		return tamanoSeccion;
	}
	public void setColumnatres(String columnatres) {
		this.columnatres = columnatres;
	}
	public String getColumnatres() {
		return columnatres;
	}
	public void setColumnacuatro(String columnacuatro) {
		this.columnacuatro = columnacuatro;
	}
	public String getColumnacuatro() {
		return columnacuatro;
	}
	public void setColumnacinco(String columnacinco) {
		this.columnacinco = columnacinco;
	}
	public String getColumnacinco() {
		return columnacinco;
	}
	public void setColumnaseis(String columnaseis) {
		this.columnaseis = columnaseis;
	}
	public String getColumnaseis() {
		return columnaseis;
	}
	public void setColumnasiete(String columnasiete) {
		this.columnasiete = columnasiete;
	}
	public String getColumnasiete() {
		return columnasiete;
	}
	public void setColumnaocho(String columnaocho) {
		this.columnaocho = columnaocho;
	}
	public String getColumnaocho() {
		return columnaocho;
	}
	public void setColumnaunica(String columnaunica) {
		this.columnaunica = columnaunica;
	}
	public String getColumnaunica() {
		return columnaunica;
	}
	public void setColumnadobleuno(String columnadobleuno) {
		this.columnadobleuno = columnadobleuno;
	}
	public String getColumnadobleuno() {
		return columnadobleuno;
	}
	public void setColumnadobledos(String columnadobledos) {
		this.columnadobledos = columnadobledos;
	}
	public String getColumnadobledos() {
		return columnadobledos;
	}
	public void setColumnatripleuno(String columnatripleuno) {
		this.columnatripleuno = columnatripleuno;
	}
	public String getColumnatripleuno() {
		return columnatripleuno;
	}
	public void setColumnatripledos(String columnatripledos) {
		this.columnatripledos = columnatripledos;
	}
	public String getColumnatripledos() {
		return columnatripledos;
	}
	public void setColumnatripletres(String columnatripletres) {
		this.columnatripletres = columnatripletres;
	}
	public String getColumnatripletres() {
		return columnatripletres;
	}



}
