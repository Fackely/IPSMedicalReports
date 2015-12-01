package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

import util.ConstantesBD;
import util.InfoDatosString;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public class DtoCentroCostosVista implements Serializable{
	
	
	private static final long serialVersionUID = 3751382995058535226L;
	
	private int codigo;
	private String identificador;
	private String codigoInterfaz;
	private String nombre;
	private InfoDatosString unidadFuncional;
	private int  codigoCentroAtencion;
	private int nroPrioridad;
	private Integer tipoArea;
	
	
	
	public void clean()
	{
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.codigoInterfaz = "";
		this.identificador = "";
		this.nombre = "";
		this.unidadFuncional = new InfoDatosString("","");
		this.codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
		this.setNroPrioridad(ConstantesBD.codigoNuncaValido);
		this.tipoArea = null;
	}
	
	
	
	/**
	 * Constructor
	 */
	public DtoCentroCostosVista()
	{
		this.clean();
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
	 * @return the identificador
	 */
	public String getIdentificador() {
		return identificador;
	}

	/**
	 * @param identificador the identificador to set
	 */
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	/**
	 * @return the codigoInterfaz
	 */
	public String getCodigoInterfaz() {
		return codigoInterfaz;
	}

	/**
	 * @param codigoInterfaz the codigoInterfaz to set
	 */
	public void setCodigoInterfaz(String codigoInterfaz) {
		this.codigoInterfaz = codigoInterfaz;
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
	 * @return the unidadFuncional
	 */
	public InfoDatosString getUnidadFuncional() {
		return unidadFuncional;
	}

	/**
	 * @param unidadFuncional the unidadFuncional to set
	 */
	public void setUnidadFuncional(InfoDatosString unidadFuncional) {
		this.unidadFuncional = unidadFuncional;
	}

	/**
	 * @return the codigoCentroAtencion
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion the codigoCentroAtencion to set
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}


	public void setNroPrioridad(int nroPrioridad) {
		this.nroPrioridad = nroPrioridad;
	}


	public int getNroPrioridad() {
		return nroPrioridad;
	}



	/**
	 * @return valor de tipoArea
	 */
	public Integer getTipoArea() {
		return tipoArea;
	}



	/**
	 * @param tipoArea el tipoArea para asignar
	 */
	public void setTipoArea(Integer tipoArea) {
		this.tipoArea = tipoArea;
	}
	
	

	
}
