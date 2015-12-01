package com.princetonsa.dto.tesoreria;

import util.ConstantesBD;

/**
 * contiene los parametros para la busqueda de traslado de Abonos
 * 
 * @author Cristhian Murillo
 */
public class DtoBusTrasladoAbono extends DtoFaltanteSobrante {
	
	
	private static final long serialVersionUID = 1L;
	
	private String codigoPais;
	private String codigoCiudad;
	private long codigoRegionesCobertura;
	
	private int codigoInstituciones;
	private int codigoCentroAten;
	
	//private String codigoInstitucionesStr;
	
	private boolean parametroInstitucionMultiempresa;
	
	
	public boolean isParametroInstitucionMultiempresa() {
		return parametroInstitucionMultiempresa;
	}


	public void setParametroInstitucionMultiempresa(
			boolean parametroInstitucionMultiempresa) {
		this.parametroInstitucionMultiempresa = parametroInstitucionMultiempresa;
	}


	public DtoBusTrasladoAbono()
	{
		this.codigoPais					= "";
		this.codigoCiudad				= "";
		this.codigoRegionesCobertura	= ConstantesBD.codigoNuncaValidoLong;
		this.codigoInstituciones		= ConstantesBD.codigoNuncaValido;
		this.codigoCentroAten			= ConstantesBD.codigoNuncaValido;
		//this.codigoInstitucionesStr		= "";
		this.parametroInstitucionMultiempresa = false;
		
	}


	public String getCodigoPais() {
		return codigoPais;
	}


	public String getCodigoCiudad() {
		return codigoCiudad;
	}


	public long getCodigoRegionesCobertura() {
		return codigoRegionesCobertura;
	}


	public int getCodigoInstituciones() {
		return codigoInstituciones;
	}


	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}


	public void setCodigoCiudad(String codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}


	public void setCodigoRegionesCobertura(long codigoRegionesCobertura) {
		this.codigoRegionesCobertura = codigoRegionesCobertura;
	}


	public void setCodigoInstituciones(int codigoInstituciones) {
		this.codigoInstituciones = codigoInstituciones;
	}

/*
	public String getCodigoInstitucionesStr() {
		return codigoInstitucionesStr;
	}
*/

	public int getCodigoCentroAten() {
		return codigoCentroAten;
	}


	public void setCodigoCentroAten(int codigoCentroAten) {
		this.codigoCentroAten = codigoCentroAten;
	}

/*
	public void setCodigoInstitucionesStr(String codigoInstitucionesStr) {
		this.codigoInstitucionesStr = codigoInstitucionesStr;
	}	
*/
	
}
