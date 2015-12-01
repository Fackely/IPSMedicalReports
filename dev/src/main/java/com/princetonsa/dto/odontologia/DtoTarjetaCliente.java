package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadFecha;

/**
 * 
 * @author axioma
 *
 */
public class DtoTarjetaCliente implements Serializable 
{
	
	/*
	 * Atributos del Sistema
	 */
	private String codigoTipoTarj;
	private int institucion;
	private String nombre;
	private String aliado; //OJO
	private int consecutivoSerial;
	private String esModificableConsecutivoSerial;
	private InfoDatosInt convenio;
	private InfoDatosInt servicioPersonal;
	private InfoDatosInt servicioEmpresarial;
	private InfoDatosInt servicioFamiliar;
	private int numBeneficiariosFam;
	private String horaModificada;
	private String fechaModificada;
	private String usuarioModifica; 
	private Double codigoPk;

	/**
	 * Constructor
	 */
	public DtoTarjetaCliente(){
		clean();
	}
	
	
	
	/**
	 * 
	 */
	public void clean() 
	{
		this.codigoTipoTarj = "";
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.nombre = "";
		this.aliado = "";
		this.convenio = new InfoDatosInt();
		this.consecutivoSerial= ConstantesBD.codigoNuncaValido;
		this.esModificableConsecutivoSerial=ConstantesBD.acronimoSi;
		//this.diasVigencia = ConstantesBD.codigoNuncaValido;
		this.setServicioEmpresarial(new InfoDatosInt());
		this.setServicioFamiliar(new InfoDatosInt());
		this.setServicioPersonal(new InfoDatosInt());
		this.numBeneficiariosFam=ConstantesBD.codigoNuncaValido;
		this.horaModificada = "";
		this.fechaModificada = "";
		this.usuarioModifica = "";
		this.codigoPk = ConstantesBD.codigoNuncaValidoDoubleNegativo;
	}




	
	/**
	 * @return the fechaModificada
	 */
	public String getFechaModificadaFormatoBD() {
		return UtilidadFecha.validarFecha(this.fechaModificada)?UtilidadFecha.conversionFormatoFechaABD(this.fechaModificada):"";
	}

	/**
	 * @return the codigoTipoTarj
	 */
	public String getCodigoTipoTarj() {
		return codigoTipoTarj;
	}

	/**
	 * @param codigoTipoTarj the codigoTipoTarj to set
	 */
	public void setCodigoTipoTarj(String codigoTipoTarj) {
		this.codigoTipoTarj = codigoTipoTarj;
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
	 * @return the aliado
	 */
	public String getAliado() {
		return aliado;
	}

	/**
	 * @param aliado the aliado to set
	 */
	public void setAliado(String aliado) {
		this.aliado = aliado;
	}

	/**
	 * @return the convenio
	 */
	public InfoDatosInt getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(InfoDatosInt convenio) {
		this.convenio = convenio;
	}

	
	

	/**
	 * @return the horaModificada
	 */
	public String getHoraModificada() {
		return horaModificada;
	}

	/**
	 * @param horaModificada the horaModificada to set
	 */
	public void setHoraModificada(String horaModificada) {
		this.horaModificada = horaModificada;
	}

	/**
	 * @return the fechaModificada
	 */
	public String getFechaModificada() {
		return fechaModificada;
	}

	
	
	
	/**
	 * @param fechaModificada the fechaModificada to set
	 */
	public void setFechaModificada(String fechaModificada) {
		this.fechaModificada = fechaModificada;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFechaModificaBD() {
		return UtilidadFecha.conversionFormatoFechaABD(this.fechaModificada);	 
	}
	
	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the codigoPk
	 */
	public Double getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(Double codigoPk) {
		this.codigoPk = codigoPk;
	}

 /**
  * 
  * @param numBeneficiariosFam
  */

	public void setNumBeneficiariosFam(int numBeneficiariosFam) {
		this.numBeneficiariosFam = numBeneficiariosFam;
	}

/**
 * 
 * @return
 */

	public int getNumBeneficiariosFam() {
		return numBeneficiariosFam;
	}



	public void setServicioPersonal(InfoDatosInt servicioPersonal) {
		this.servicioPersonal = servicioPersonal;
	}



	public InfoDatosInt getServicioPersonal() {
		return servicioPersonal;
	}



	public void setServicioEmpresarial(InfoDatosInt servicioEmpresarial) {
		this.servicioEmpresarial = servicioEmpresarial;
	}



	public InfoDatosInt getServicioEmpresarial() {
		return servicioEmpresarial;
	}



	public void setServicioFamiliar(InfoDatosInt servicioFamiliar) {
		this.servicioFamiliar = servicioFamiliar;
	}



	public InfoDatosInt getServicioFamiliar() {
		return servicioFamiliar;
	}



	public int getConsecutivoSerial() {
		return consecutivoSerial;
	}



	public void setConsecutivoSerial(int consecutivoSerial) {
		this.consecutivoSerial = consecutivoSerial;
	}



	public String getEsModificableConsecutivoSerial() {
		return esModificableConsecutivoSerial;
	}



	public void setEsModificableConsecutivoSerial(
			String esModificableConsecutivoSerial) {
		this.esModificableConsecutivoSerial = esModificableConsecutivoSerial;
	}



	


	



	
}