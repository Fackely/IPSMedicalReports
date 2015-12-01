/**
 * 
 */
package com.princetonsa.dto.interfaz;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * @author Cristhian Murillo
 */
public class DtoCuentasInterfazEmpresasInsti implements Serializable
{
	
	/** Serialización  *  */
	private static final long serialVersionUID = 1L;

	/** Llave primaria  *  */
	private long codigoPk;
	
	/** Empresa Insticucion  *  */
	private long empresasInstitucion;
	
	/** Razon social de la Empresa-Institución  *  */
	private String nombreEmpresasInstitucion;
	
	/** Cuenta  *  */
	private long cuentaContable;
	
	/** Código de la Institución  *  */
	private int institucion;
	
	/** Razon social de la Institución  *  */
	private String nombreInstitucion;
	
	/**
	 * Constructor
	 */
	public DtoCuentasInterfazEmpresasInsti(){
		this.codigoPk				= ConstantesBD.codigoNuncaValidoLong;
		this.empresasInstitucion	= ConstantesBD.codigoNuncaValidoLong;
		this.cuentaContable			= ConstantesBD.codigoNuncaValidoLong;
		this.institucion			= ConstantesBD.codigoNuncaValido;
		this.nombreInstitucion		= "";
		this.nombreEmpresasInstitucion = "";
	}

	public long getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	public long getEmpresasInstitucion() {
		return empresasInstitucion;
	}

	public void setEmpresasInstitucion(long empresasInstitucion) {
		this.empresasInstitucion = empresasInstitucion;
	}

	public long getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(long cuentaContable) {
		this.cuentaContable = cuentaContable;
	}

	public String getNombreInstitucion() {
		return nombreInstitucion;
	}

	public void setNombreInstitucion(String nombreInstitucion) {
		this.nombreInstitucion = nombreInstitucion;
	}

	public String getNombreEmpresasInstitucion() {
		return nombreEmpresasInstitucion;
	}

	public void setNombreEmpresasInstitucion(String nombreEmpresasInstitucion) {
		this.nombreEmpresasInstitucion = nombreEmpresasInstitucion;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	

}
	

