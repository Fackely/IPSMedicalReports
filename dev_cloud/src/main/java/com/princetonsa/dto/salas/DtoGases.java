package com.princetonsa.dto.salas;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * 
 * @author wilson
 *
 */
public class DtoGases implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private int codigo;
	
	/**
	 * 
	 */
	private String nombre;
	
	/**
	 * 
	 */
	private int orden;
	
	/**
	 * 
	 */
	private boolean obligatorio;
	
	/**
	 * 
	 */
	private String convencion;
	
	/**
	 * 
	 */
	private int cantidadMinLitros;
	
	
	/**
	 * 
	 */
	private int cantidadMaxLitros;
	
	/**
	 * 
	 */
	private boolean llevaFio2;
	
	/**
	 * 
	 */
	private boolean llevaGasAnestesico;

	/**
	 * 
	 */
	private DtoColor dtoColor;
	
	/**
	 * 
	 * @param codigo
	 * @param nombre
	 * @param orden
	 * @param obligatorio
	 * @param convencion
	 * @param cantidadMinLitros
	 * @param cantidadMaxLitros
	 * @param llevaFio2
	 * @param llevaGasAnestesico
	 */
	public DtoGases(int codigo, String nombre, int orden, boolean obligatorio, String convencion, int cantidadMinLitros, int cantidadMaxLitros, boolean llevaFio2, boolean llevaGasAnestesico, DtoColor dtocolor) 
	{
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.orden = orden;
		this.obligatorio = obligatorio;
		this.convencion = convencion;
		this.cantidadMinLitros = cantidadMinLitros;
		this.cantidadMaxLitros = cantidadMaxLitros;
		this.llevaFio2 = llevaFio2;
		this.llevaGasAnestesico = llevaGasAnestesico;
		this.dtoColor= dtocolor;
	}

	/**
	 * 
	 *
	 */
	public DtoGases() 
	{
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.nombre = "";
		this.orden = ConstantesBD.codigoNuncaValido;
		this.obligatorio = false;
		this.convencion = "";
		this.cantidadMinLitros = ConstantesBD.codigoNuncaValido;
		this.cantidadMaxLitros = ConstantesBD.codigoNuncaValido;
		this.llevaFio2 = false;
		this.llevaGasAnestesico = false;
		this.dtoColor= new DtoColor();
	}

	/**
	 * @return the cantidadMaxLitros
	 */
	public int getCantidadMaxLitros() {
		return cantidadMaxLitros;
	}

	/**
	 * @param cantidadMaxLitros the cantidadMaxLitros to set
	 */
	public void setCantidadMaxLitros(int cantidadMaxLitros) {
		this.cantidadMaxLitros = cantidadMaxLitros;
	}

	/**
	 * @return the cantidadMinLitros
	 */
	public int getCantidadMinLitros() {
		return cantidadMinLitros;
	}

	/**
	 * @param cantidadMinLitros the cantidadMinLitros to set
	 */
	public void setCantidadMinLitros(int cantidadMinLitros) {
		this.cantidadMinLitros = cantidadMinLitros;
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
	 * @return the convencion
	 */
	public String getConvencion() {
		return convencion;
	}

	/**
	 * @param convencion the convencion to set
	 */
	public void setConvencion(String convencion) {
		this.convencion = convencion;
	}

	/**
	 * @return the llevaFio2
	 */
	public boolean getLlevaFio2() {
		return llevaFio2;
	}

	/**
	 * @param llevaFio2 the llevaFio2 to set
	 */
	public void setLlevaFio2(boolean llevaFio2) {
		this.llevaFio2 = llevaFio2;
	}

	/**
	 * @return the llevaGasAnestesico
	 */
	public boolean getLlevaGasAnestesico() {
		return llevaGasAnestesico;
	}

	/**
	 * @param llevaGasAnestesico the llevaGasAnestesico to set
	 */
	public void setLlevaGasAnestesico(boolean llevaGasAnestesico) {
		this.llevaGasAnestesico = llevaGasAnestesico;
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
	 * @return the obligatorio
	 */
	public boolean getObligatorio() {
		return obligatorio;
	}

	/**
	 * @param obligatorio the obligatorio to set
	 */
	public void setObligatorio(boolean obligatorio) {
		this.obligatorio = obligatorio;
	}

	/**
	 * @return the orden
	 */
	public int getOrden() {
		return orden;
	}

	/**
	 * @param orden the orden to set
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}

	/**
	 * @return the dtoColor
	 */
	public DtoColor getDtoColor() {
		return dtoColor;
	}

	/**
	 * @param dtoColor the dtoColor to set
	 */
	public void setDtoColor(DtoColor dtoColor) {
		this.dtoColor = dtoColor;
	}
	
	
}
