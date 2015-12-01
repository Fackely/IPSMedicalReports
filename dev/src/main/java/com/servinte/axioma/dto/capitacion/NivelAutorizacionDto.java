package com.servinte.axioma.dto.capitacion;

import java.io.Serializable;
import java.util.List;

import com.servinte.axioma.dto.manejoPaciente.ViaIngresoDto;

/**
 * Dto para guardar los niveles de autorizaci�n de un usuario
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:01 p.m.
 */
public class NivelAutorizacionDto implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3280075578893432462L;
	
	/**
	 * Atributo que representa el codigoPK del nivel de autorizaci�n
	 */
	private int codigo;
	
	/**
	 * Atributo que representa la descripci�n del nivel de autorizaci�n
	 */
	private String descripcion;
	
	/**
	 * Atributo que representa la via de ingreso parametrizada para el nivel de autorizaci�n
	 */
	private ViaIngresoDto viaIngreso;
	
	/**
	 * Atributo que representa las prioridades parametrizadas para el nivel de autorizaci�n
	 */
	private List<Integer> prioridades;

	public NivelAutorizacionDto(){

	}
	
	/**
	 * Constructor utilizado en los named query de consulta de niveles 
	 * de autorizaci�n del usuario
	 * 
	 * @param codigoPk
	 * @param descripcion
	 * @param codigoViaIngreso
	 * @param nombreViaIngreso
	 */
	public NivelAutorizacionDto(int codigoPk, String descripcion, int codigoViaIngreso, String nombreViaIngreso){
		this.codigo=codigoPk;
		this.descripcion=descripcion;
		this.viaIngreso= new ViaIngresoDto(codigoViaIngreso, nombreViaIngreso);
	}


	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}


	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}


	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}


	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	/**
	 * @return the viaIngreso
	 */
	public ViaIngresoDto getViaIngreso() {
		return viaIngreso;
	}


	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(ViaIngresoDto viaIngreso) {
		this.viaIngreso = viaIngreso;
	}


	/**
	 * @return the prioridades
	 */
	public List<Integer> getPrioridades() {
		return prioridades;
	}


	/**
	 * @param prioridades the prioridades to set
	 */
	public void setPrioridades(List<Integer> prioridades) {
		this.prioridades = prioridades;
	}

	
}