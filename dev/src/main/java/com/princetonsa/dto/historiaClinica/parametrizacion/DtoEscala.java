/*
 * Mayo 6, 2008
 */
package com.princetonsa.dto.historiaClinica.parametrizacion;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;


/**
 * Data Transfer Object: Escala Parametrizable
 * @author Sebastián Gómez R.
 *
 */
public class DtoEscala extends DtoElementoParam implements Serializable
{
	
	private boolean observacionesRequeridas;
	private String observaciones;
	
	/**
	 * Arreglo que contiene los factores de prediccion de la escala
	 */
	private ArrayList<DtoEscalaFactorPrediccion> factoresPrediccion;
	
	/**
	 * Arreglo que almacena los históricos de la escala seleccionada
	 */
	private ArrayList<DtoEscala> historicos;
	
	/**
	 * Campos que almacen la fecha/hora de registro de la escala
	 */
	private String fecha;
	private String hora;
	/**
	 * Campo que almacena el responsable que registró la escala
	 */
	private String nombreResponsable;
	
	/**
	 * Variable para Campo para validar el registro de la escala
	 */
	private String requerida;
	
	/**
	 * 
	 */
	private String nombre;
	
	
	/**
	 * Resetea los datos del DTO
	 *
	 */
	public void clean()
	{
		super.clean();
		this.observacionesRequeridas = false;
		this.observaciones = "";
		
		this.factoresPrediccion = new ArrayList<DtoEscalaFactorPrediccion>();
		
		this.historicos = new ArrayList<DtoEscala>();
		
		this.fecha = "";
		this.hora = "";
		this.nombreResponsable = "";
		this.requerida=ConstantesBD.acronimoNo;
		this.nombre="";
	}
	
	/**
	 * Constructor
	 *
	 */
	public DtoEscala()
	{
		this.clean();
	}

	

	

	/**
	 * @return the observacionesRequeridas
	 */
	public boolean isObservacionesRequeridas() {
		return observacionesRequeridas;
	}

	/**
	 * @param observacionesRequeridas the observacionesRequeridas to set
	 */
	public void setObservacionesRequeridas(boolean observacionesRequeridas) {
		this.observacionesRequeridas = observacionesRequeridas;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	
	/**
	 * @return the factoresPrediccion
	 */
	public ArrayList<DtoEscalaFactorPrediccion> getFactoresPrediccion() {
		return factoresPrediccion;
	}

	/**
	 * @param factoresPrediccion the factoresPrediccion to set
	 */
	public void setFactoresPrediccion(
			ArrayList<DtoEscalaFactorPrediccion> factoresPrediccion) {
		this.factoresPrediccion = factoresPrediccion;
	}

	
	/**
	 * @return the historicos
	 */
	public ArrayList<DtoEscala> getHistoricos() {
		return historicos;
	}

	/**
	 * @param historicos the historicos to set
	 */
	public void setHistoricos(ArrayList<DtoEscala> historicos) {
		this.historicos = historicos;
	}

	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the hora
	 */
	public String getHora() {
		return hora;
	}

	/**
	 * @param hora the hora to set
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}

	/**
	 * @return the nombreResponsable
	 */
	public String getNombreResponsable() {
		return nombreResponsable;
	}

	/**
	 * @param nombreResponsable the nombreResponsable to set
	 */
	public void setNombreResponsable(String nombreResponsable) {
		this.nombreResponsable = nombreResponsable;
	}
	
	
	/**
	 * Metodo para obtener el valor mínimo de un factor de prediccion
	 * @return
	 */
	public double getRangoMinimoFactorPrediccion()
	{
		double rangoMinimo = this.getRangoMaximoFactorPrediccion();
		for(DtoEscalaFactorPrediccion factor:this.factoresPrediccion)
			if(factor.getValorInicial()<rangoMinimo)
				rangoMinimo = factor.getValorInicial();
		
		return rangoMinimo;
	}
	
	/**
	 * Método para obtener el valor máximo de un factor de prediccion
	 * @return
	 */
	public double getRangoMaximoFactorPrediccion()
	{
		double rangoMaximo = 0;
		for(DtoEscalaFactorPrediccion factor:this.factoresPrediccion)
			if(factor.getValorFinal()>rangoMaximo)
				rangoMaximo = factor.getValorFinal();
		
		return rangoMaximo;
	}

	/**
	 * @return the requerida
	 */
	public String getRequerida() {
		return requerida;
	}

	/**
	 * @param requerida the requerida to set
	 */
	public void setRequerida(String requerida) {
		this.requerida = requerida;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	
}
