package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * DTO relacionado a la tabla historiaclinica.superficie_dental
 * @author Juan David Ram&iacute;rez
 * @since 2010-05-12
 */
public class DtoSuperficieDental  implements Serializable , Cloneable{

	/**
	 * Veris&oacute;n serial
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Llave primaria
	 */
	private double codigo;
	
	/**
	 * Nombre de la superficie
	 */
	private String nombre;
	
	/**
	 * Instituci&oacute;n relacionada a la superficie
	 */
	private int institucion;
	
	/**
	 * Activo en el sistema
	 */
	private int activo;
	
	/**
	 * 
	 */
	public DtoSuperficieDental() {
		this.reset();
	}
	
	/**
	 * 
	 */
	public void reset(){
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.nombre="";
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.activo= ConstantesBD.codigoNuncaValido;
	}
	
	
	/**
	 * 
	 * @param codigo
	 * @param nombre
	 * @param institucion
	 * @param activo
	 */
	public DtoSuperficieDental(double codigo, String nombre, int institucion,
			int activo)
	{
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.institucion = institucion;
		this.activo = activo;
	}
	/**
	 * @return the codigo
	 */
	public double getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(double codigo) {
		this.codigo = codigo;
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
	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	/**
	 * @return the activo
	 */
	public int getActivo() {
		return activo;
	}
	/**
	 * @param activo the activo to set
	 */
	public void setActivo(int activo) {
		this.activo = activo;
	}
	
	@Override
	protected DtoSuperficieDental clone() throws CloneNotSupportedException
	{
		return (DtoSuperficieDental)super.clone();
	}
	
}
