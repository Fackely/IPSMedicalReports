package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

/**
 * 
 * @author wilson
 *
 */
public class DtoFURPRO implements Serializable 
{
	/**
	 * 
	 */
	public String[] nombresCampos=	{"",
									/*1*/"Nro. Radicado Anterior",
									/*2*/"RG",
									/*3*/"Numero factura o Numero cuenta cobro",
									/*4*/"codigo de habilitacion del prestador de servicios de salud",
									/*5*/"primer apellido victima",
									/*6*/"segundo apellido victima",
									/*7*/"primer nombre victima",
									/*8*/"segundo nombre victima",
									/*9*/"Tipo documento de identidad de la Víctima",
									/*10*/"numero documento victima",
									/*11*/"fecha de nacimiento de la victima",
									/*12*/"sexo de la victima",
									/*13*/"direccion de residencia de la victima",
									/*14*/"codigo del departamento de residencia de la victima",
									/*15*/"codigo del municipio de residencia de la victima",
									/*16*/"telefono de la victima",
									/*17*/"condicion de afiliacion al sgsss de la victima",
									/*18*/"regimen al cual se encuentra afiliado la victima",
									/*19*/"codigo de la eps o eoc la cual se encuentra afiliada la victima",
									/*20*/"naturaleza del evento",
									/*21*/"descripcion de otro evento",
									/*22*/"direccion ocurrencia del evento",
									/*23*/"fecha del evento",
									/*24*/"codigo del departamento de ocurrencia del evento",
									/*25*/"codigo del municipio de ocurrencia del evento",
									/*26*/"zona donde ocurrio el evento",
									/*27*/"decripcion breve del evento",
									/*28*/"codigo del diagnostico principal",
									/*29*/"codigo del diagnostico asociado 1",
									/*30*/"codigo del diagnostico asociado 2",
									/*31*/"codigo del diagnostico asociado 3",
									/*32*/"codigo del diagnostico asociado 4",
									/*33*/"descripcion de la protesis o servicio prestado",
									/*34*/"valor reclamado por protesis",
									/*35*/"valor reclamado por adaptacion de protesis",
									/*36*/"valor reclamado por rehabilitacion",
									/*37*/"valor total reclamado",
									/*38*/"total folios",
									};
	
	/**
	 * 
	 */
	private boolean esDesplazado;
	
	/**
	 * 
	 */
	private String idCuenta;

	/**
	 * 
	 */
	private String nombreViaIngreso;

	/**
	 * 
	 */
	private String idFactura;

	/**
	 * 
	 */
	private String idCuentaCobro;

	/**
	 * 
	 */
	private boolean esInstitucionPublica;
	
	
	
	
	
	private String nroRadicadoAnterior;
	private String rg;
	private String nroFactura_nroCxC;
	private String codHabilitacionPrestadorServ;
	private String primerApellidoVictima;
	private String segundoApellidoVictima;
	private String primerNombreVictima;
	private String segundoNombreVictima;
	private String tipoDocIdVictima;
	private String numDocVictima;
	private String fechaNacVictima;
	private String sexoVictima;
	private String dirResidenciaVictima;
	private String codDeptoResidenciaVictima;
	private String codMunicipioResidenciaVictima;
	private String telVictima;
	private String SGSSS;
	private String regimenVictima;
	private String codEPS_EOC;
	private String naturalezaEvento;
	private String descOtroEvento;
	private String dirOcurrenciaVictima;
	private String fechaEvento;
	private String codDeptoOcurrenciaEvento;
	private String codMunicipioOcurrioEvento;
	private String zonaEvento;
	private String descBreveEvento;
	private String codDxPpal;
	private String codDxAsociado1;
	private String codDxAsociado2;
	private String codDxAsociado3;
	private String codDxAsociado4;
	private String descProtesisOServ;
	private String valorReclamadoXprotesis;
	private String valorReclamadoXadaptacionProtesis;
	private String valorReclamadoXrehabilitacion;
	private String valorTotalReclamado;
	private String totalFolios;
	
	/**
	 * 
	 */
	public DtoFURPRO() 
	{
		super();
		this.esDesplazado=false;
		this.idCuenta="";
		this.nombreViaIngreso="";
		this.idFactura="";
		this.idCuentaCobro="";
		this.esInstitucionPublica=false;
		this.nroRadicadoAnterior="";
		this.rg="";
		this.nroFactura_nroCxC="";
		this.codHabilitacionPrestadorServ="";
		this.primerApellidoVictima="";
		this.segundoApellidoVictima="";
		this.primerNombreVictima="";
		this.segundoNombreVictima="";
		this.tipoDocIdVictima="";
		this.numDocVictima="";
		this.fechaNacVictima="";
		this.sexoVictima="";
		this.dirResidenciaVictima="";
		this.codDeptoResidenciaVictima="";
		this.codMunicipioResidenciaVictima="";
		this.telVictima="";
		this.SGSSS="";
		this.regimenVictima="";
		this.codEPS_EOC="";
		this.naturalezaEvento="";
		this.descOtroEvento="";
		this.dirOcurrenciaVictima="";
		this.fechaEvento="";
		this.codDeptoOcurrenciaEvento="";
		this.codMunicipioOcurrioEvento="";
		this.zonaEvento="";
		this.descBreveEvento="";
		this.codDxPpal="";
		this.codDxAsociado1="";
		this.codDxAsociado2="";
		this.codDxAsociado3="";
		this.codDxAsociado4="";
		this.descProtesisOServ="";
		this.valorReclamadoXprotesis="";
		this.valorReclamadoXadaptacionProtesis="";
		this.valorReclamadoXrehabilitacion="";
		this.valorTotalReclamado="";
		this.totalFolios="";
	}

	public String[] getNombresCampos() {
		return nombresCampos;
	}

	public void setNombresCampos(String[] nombresCampos) {
		this.nombresCampos = nombresCampos;
	}

	public String getNroRadicadoAnterior() {
		return nroRadicadoAnterior;
	}

	public void setNroRadicadoAnterior(String nroRadicadoAnterior) {
		this.nroRadicadoAnterior = nroRadicadoAnterior;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getNroFactura_nroCxC() {
		return nroFactura_nroCxC;
	}

	public void setNroFactura_nroCxC(String nroFactura_nroCxC) {
		this.nroFactura_nroCxC = nroFactura_nroCxC;
	}

	public String getCodHabilitacionPrestadorServ() {
		return codHabilitacionPrestadorServ;
	}

	public void setCodHabilitacionPrestadorServ(String codHabilitacionPrestadorServ) {
		this.codHabilitacionPrestadorServ = codHabilitacionPrestadorServ;
	}

	public String getPrimerApellidoVictima() {
		return primerApellidoVictima;
	}

	public void setPrimerApellidoVictima(String primerApellidoVictima) {
		this.primerApellidoVictima = primerApellidoVictima;
	}

	public String getSegundoApellidoVictima() {
		return segundoApellidoVictima;
	}

	public void setSegundoApellidoVictima(String segundoApellidoVictima) {
		this.segundoApellidoVictima = segundoApellidoVictima;
	}

	public String getPrimerNombreVictima() {
		return primerNombreVictima;
	}

	public void setPrimerNombreVictima(String primerNombreVictima) {
		this.primerNombreVictima = primerNombreVictima;
	}

	public String getSegundoNombreVictima() {
		return segundoNombreVictima;
	}

	public void setSegundoNombreVictima(String segundoNombreVictima) {
		this.segundoNombreVictima = segundoNombreVictima;
	}

	public String getTipoDocIdVictima() {
		return tipoDocIdVictima;
	}

	public void setTipoDocIdVictima(String tipoDocIdVictima) {
		this.tipoDocIdVictima = tipoDocIdVictima;
	}

	public String getNumDocVictima() {
		return numDocVictima;
	}

	public void setNumDocVictima(String numDocVictima) {
		this.numDocVictima = numDocVictima;
	}

	public String getFechaNacVictima() {
		return fechaNacVictima;
	}

	public void setFechaNacVictima(String fechaNacVictima) {
		this.fechaNacVictima = fechaNacVictima;
	}

	public String getSexoVictima() {
		return sexoVictima;
	}

	public void setSexoVictima(String sexoVictima) {
		this.sexoVictima = sexoVictima;
	}

	public String getDirResidenciaVictima() {
		return dirResidenciaVictima;
	}

	public void setDirResidenciaVictima(String dirResidenciaVictima) {
		this.dirResidenciaVictima = dirResidenciaVictima;
	}

	public String getCodDeptoResidenciaVictima() {
		return codDeptoResidenciaVictima;
	}

	public void setCodDeptoResidenciaVictima(String codDeptoResidenciaVictima) {
		this.codDeptoResidenciaVictima = codDeptoResidenciaVictima;
	}

	public String getCodMunicipioResidenciaVictima() {
		return codMunicipioResidenciaVictima;
	}

	public void setCodMunicipioResidenciaVictima(
			String codMunicipioResidenciaVictima) {
		this.codMunicipioResidenciaVictima = codMunicipioResidenciaVictima;
	}

	public String getTelVictima() {
		return telVictima;
	}

	public void setTelVictima(String telVictima) {
		this.telVictima = telVictima;
	}

	public String getSGSSS() {
		return SGSSS;
	}

	public void setSGSSS(String sGSSS) {
		SGSSS = sGSSS;
	}

	public String getRegimenVictima() {
		return regimenVictima;
	}

	public void setRegimenVictima(String regimenVictima) {
		this.regimenVictima = regimenVictima;
	}

	public String getCodEPS_EOC() {
		return codEPS_EOC;
	}

	public void setCodEPS_EOC(String codEPS_EOC) {
		this.codEPS_EOC = codEPS_EOC;
	}

	public String getNaturalezaEvento() {
		return naturalezaEvento;
	}

	public void setNaturalezaEvento(String naturalezaEvento) {
		this.naturalezaEvento = naturalezaEvento;
	}

	public String getDescOtroEvento() {
		return descOtroEvento;
	}

	public void setDescOtroEvento(String descOtroEvento) {
		this.descOtroEvento = descOtroEvento;
	}

	public String getDirOcurrenciaVictima() {
		return dirOcurrenciaVictima;
	}

	public void setDirOcurrenciaVictima(String dirOcurrenciaVictima) {
		this.dirOcurrenciaVictima = dirOcurrenciaVictima;
	}

	public String getFechaEvento() {
		return fechaEvento;
	}

	public void setFechaEvento(String fechaEvento) {
		this.fechaEvento = fechaEvento;
	}

	public String getCodDeptoOcurrenciaEvento() {
		return codDeptoOcurrenciaEvento;
	}

	public void setCodDeptoOcurrenciaEvento(String codDeptoOcurrenciaEvento) {
		this.codDeptoOcurrenciaEvento = codDeptoOcurrenciaEvento;
	}

	public String getCodMunicipioOcurrioEvento() {
		return codMunicipioOcurrioEvento;
	}

	public void setCodMunicipioOcurrioEvento(String codMunicipioOcurrioEvento) {
		this.codMunicipioOcurrioEvento = codMunicipioOcurrioEvento;
	}

	public String getZonaEvento() {
		return zonaEvento;
	}

	public void setZonaEvento(String zonaEvento) {
		this.zonaEvento = zonaEvento;
	}

	public String getDescBreveEvento() {
		return descBreveEvento;
	}

	public void setDescBreveEvento(String descBreveEvento) {
		this.descBreveEvento = descBreveEvento;
	}

	public String getCodDxPpal() {
		return codDxPpal;
	}

	public void setCodDxPpal(String codDxPpal) {
		this.codDxPpal = codDxPpal;
	}

	public String getCodDxAsociado1() {
		return codDxAsociado1;
	}

	public void setCodDxAsociado1(String codDxAsociado1) {
		this.codDxAsociado1 = codDxAsociado1;
	}

	public String getCodDxAsociado2() {
		return codDxAsociado2;
	}

	public void setCodDxAsociado2(String codDxAsociado2) {
		this.codDxAsociado2 = codDxAsociado2;
	}

	public String getCodDxAsociado3() {
		return codDxAsociado3;
	}

	public void setCodDxAsociado3(String codDxAsociado3) {
		this.codDxAsociado3 = codDxAsociado3;
	}

	public String getCodDxAsociado4() {
		return codDxAsociado4;
	}

	public void setCodDxAsociado4(String codDxAsociado4) {
		this.codDxAsociado4 = codDxAsociado4;
	}

	public String getDescProtesisOServ() {
		return descProtesisOServ;
	}

	public void setDescProtesisOServ(String descProtesisOServ) {
		this.descProtesisOServ = descProtesisOServ;
	}

	public String getValorReclamadoXprotesis() {
		return valorReclamadoXprotesis;
	}

	public void setValorReclamadoXprotesis(String valorReclamadoXprotesis) {
		this.valorReclamadoXprotesis = valorReclamadoXprotesis;
	}

	public String getValorReclamadoXadaptacionProtesis() {
		return valorReclamadoXadaptacionProtesis;
	}

	public void setValorReclamadoXadaptacionProtesis(
			String valorReclamadoXadaptacionProtesis) {
		this.valorReclamadoXadaptacionProtesis = valorReclamadoXadaptacionProtesis;
	}

	public String getValorReclamadoXrehabilitacion() {
		return valorReclamadoXrehabilitacion;
	}

	public void setValorReclamadoXrehabilitacion(
			String valorReclamadoXrehabilitacion) {
		this.valorReclamadoXrehabilitacion = valorReclamadoXrehabilitacion;
	}

	public String getValorTotalReclamado() {
		return valorTotalReclamado;
	}

	public void setValorTotalReclamado(String valorTotalReclamado) {
		this.valorTotalReclamado = valorTotalReclamado;
	}

	public String getTotalFolios() {
		return totalFolios;
	}

	public void setTotalFolios(String totalFolios) {
		this.totalFolios = totalFolios;
	}

	public boolean isEsDesplazado() {
		return esDesplazado;
	}

	public void setEsDesplazado(boolean esDesplazado) {
		this.esDesplazado = esDesplazado;
	}

	public String getIdCuenta() {
		return idCuenta;
	}

	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}

	public String getNombreViaIngreso() {
		return nombreViaIngreso;
	}

	public void setNombreViaIngreso(String nombreViaIngreso) {
		this.nombreViaIngreso = nombreViaIngreso;
	}

	public String getIdFactura() {
		return idFactura;
	}

	public void setIdFactura(String idFactura) {
		this.idFactura = idFactura;
	}

	public String getIdCuentaCobro() {
		return idCuentaCobro;
	}

	public void setIdCuentaCobro(String idCuentaCobro) {
		this.idCuentaCobro = idCuentaCobro;
	}

	public boolean isEsInstitucionPublica() {
		return esInstitucionPublica;
	}

	public void setEsInstitucionPublica(boolean esInstitucionPublica) {
		this.esInstitucionPublica = esInstitucionPublica;
	}

	
	
	
}
