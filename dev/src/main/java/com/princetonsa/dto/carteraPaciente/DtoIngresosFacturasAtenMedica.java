package com.princetonsa.dto.carteraPaciente;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

public class DtoIngresosFacturasAtenMedica implements Serializable{
    	
	private String codigoIngreso;
	private String consecutivoIngreso;
	private String codDatosFinanciacion;
	private String numFactura;
	private String fechaFactura;
	private String tipoDocumentoCartera;
	private String nomPaciente;
	private String codCentroAtencion;
	private String nomCentroAtencion;
	private String estadoDocCartera;
	private String codGarantia;
	private String numPagare;
	private String anioConsecPagare;
	private String saldo;
	private String valorDocGarantia;
	private DtoDeudoresDatosFinan deudor;
	private DtoDeudoresDatosFinan coDeudor;
	private  ArrayList<DtoDetApliPagosCarteraPac> detalleAplicacion;
	private int cantidadCuotasPagas;
	private int numCuotasFinanciacion;
	private int diaporcuotaFinanciacion;
	private String fechaInicioFinanciacion;
	
	
	public DtoIngresosFacturasAtenMedica()
	{
	  this.reset();	
	}
	
	public void reset()
	{
		this.codigoIngreso=ConstantesBD.codigoNuncaValido+"";
		this.consecutivoIngreso=ConstantesBD.codigoNuncaValido+"";
		this.numFactura=ConstantesBD.codigoNuncaValido+"";
		this.codDatosFinanciacion=new String("");
		this.tipoDocumentoCartera=new String("");
		this.nomPaciente=new String("nombrepaciente");
		this.codCentroAtencion=ConstantesBD.codigoNuncaValido+"";
		this.nomCentroAtencion=new String("");
		this.estadoDocCartera=new String("");
		this.numPagare=ConstantesBD.codigoNuncaValido+"";
		this.anioConsecPagare=new String("");
		this.codGarantia=ConstantesBD.codigoNuncaValido+"";
		this.saldo=ConstantesBD.codigoNuncaValido+"";
		this.detalleAplicacion= new ArrayList<DtoDetApliPagosCarteraPac>();
		this.cantidadCuotasPagas=ConstantesBD.codigoNuncaValido;
		this.deudor = new DtoDeudoresDatosFinan();
		this.coDeudor = new DtoDeudoresDatosFinan();
		this.numCuotasFinanciacion=ConstantesBD.codigoNuncaValido;
		this.diaporcuotaFinanciacion=ConstantesBD.codigoNuncaValido;
		this.fechaInicioFinanciacion=new String("");
		this.fechaFactura=new String("");
		this.valorDocGarantia=new String("");
		
	}
	
	
	/**
	 * @return the codigoIngreso
	 */
	public String getCodigoIngreso() {
		return codigoIngreso;
	}
	/**
	 * @param codigoIngreso the codigoIngreso to set
	 */
	public void setCodigoIngreso(String codigoIngreso) {
		this.codigoIngreso = codigoIngreso;
	}
	/**
	 * @return the consecutivoIngreso
	 */
	public String getConsecutivoIngreso() {
		return consecutivoIngreso;
	}
	/**
	 * @param consecutivoIngreso the consecutivoIngreso to set
	 */
	public void setConsecutivoIngreso(String consecutivoIngreso) {
		this.consecutivoIngreso = consecutivoIngreso;
	}
	/**
	 * @return the numFactura
	 */
	public String getNumFactura() {
		return numFactura;
	}
	/**
	 * @param numFactura the numFactura to set
	 */
	public void setNumFactura(String numFactura) {
		this.numFactura = numFactura;
	}
	/**
	 * @return the tipoDocumentoCartera
	 */
	public String getTipoDocumentoCartera() {
		return tipoDocumentoCartera;
	}
	/**
	 * @param tipoDocumentoCartera the tipoDocumentoCartera to set
	 */
	public void setTipoDocumentoCartera(String tipoDocumentoCartera) {
		this.tipoDocumentoCartera = tipoDocumentoCartera;
	}
	/**
	 * @return the centroAtencion
	 */
	public String getCodCentroAtencion() {
		return codCentroAtencion;
	}
	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCodCentroAtencion(String centroAtencion) {
		this.codCentroAtencion = centroAtencion;
	}
	/**
	 * @return the estadoDocCartera
	 */
	public String getEstadoDocCartera() {
		return estadoDocCartera;
	}
	/**
	 * @param estadoDocCartera the estadoDocCartera to set
	 */
	public void setEstadoDocCartera(String estadoDocCartera) {
		this.estadoDocCartera = estadoDocCartera;
	}
	/**
	 * @return the codGarantia
	 */
	public String getCodGarantia() {
		return codGarantia;
	}
	/**
	 * @param codGarantia the codGarantia to set
	 */
	public void setCodGarantia(String codGarantia) {
		this.codGarantia = codGarantia;
	}
	/**
	 * @return the saldo
	 */
	public String getSaldo() {
		return saldo;
	}
	/**
	 * @param saldo the saldo to set
	 */
	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}

	/**
	 * @return the nomCentroAtencion
	 */
	public String getNomCentroAtencion() {
		return nomCentroAtencion;
	}

	/**
	 * @param nomCentroAtencion the nomCentroAtencion to set
	 */
	public void setNomCentroAtencion(String nomCentroAtencion) {
		this.nomCentroAtencion = nomCentroAtencion;
	}

	/**
	 * @return the detalleAplicacion
	 */
	public ArrayList<DtoDetApliPagosCarteraPac> getDetalleAplicacion() {
		return detalleAplicacion;
	}

	/**
	 * @param detalleAplicacion the detalleAplicacion to set
	 */
	public void setDetalleAplicacion(
			ArrayList<DtoDetApliPagosCarteraPac> detalleAplicacion) {
		this.detalleAplicacion = detalleAplicacion;
	}

	/**
	 * @return the numPagare
	 */
	public String getNumPagare() {
		return numPagare;
	}

	/**
	 * @param numPagare the numPagare to set
	 */
	public void setNumPagare(String numPagare) {
		this.numPagare = numPagare;
	}

	/**
	 * @return the anioConsecPagare
	 */
	public String getAnioConsecPagare() {
		return anioConsecPagare;
	}

	/**
	 * @param anioConsecPagare the anioConsecPagare to set
	 */
	public void setAnioConsecPagare(String anioConsecPagare) {
		this.anioConsecPagare = anioConsecPagare;
	}

	/**
	 * @return the cantidadCuotas
	 */
	public int getCantidadCuotasPagas() {
		return cantidadCuotasPagas;
	}

	/**
	 * @param cantidadCuotas the cantidadCuotas to set
	 */
	public void setCantidadCuotasPagas(int cantidadCuotas) {
		this.cantidadCuotasPagas = cantidadCuotas;
	}

	/**
	 * @return the deudor
	 */
	public DtoDeudoresDatosFinan getDeudor() {
		return deudor;
	}

	/**
	 * @param deudor the deudor to set
	 */
	public void setDeudor(DtoDeudoresDatosFinan deudor) {
		this.deudor = deudor;
	}

	/**
	 * @return the nomPaciente
	 */
	public String getNomPaciente() {
		return nomPaciente;
	}

	/**
	 * @param nomPaciente the nomPaciente to set
	 */
	public void setNomPaciente(String nomPaciente) {
		this.nomPaciente = nomPaciente;
	}

	/**
	 * @return the numCuotasFinanciacion
	 */
	public int getNumCuotasFinanciacion() {
		return numCuotasFinanciacion;
	}

	/**
	 * @param numCuotasFinanciacion the numCuotasFinanciacion to set
	 */
	public void setNumCuotasFinanciacion(int numCuotasFinanciacion) {
		this.numCuotasFinanciacion = numCuotasFinanciacion;
	}

	/**
	 * @return the diaporcuotaFinanciacion
	 */
	public int getDiaporcuotaFinanciacion() {
		return diaporcuotaFinanciacion;
	}

	/**
	 * @param diaporcuotaFinanciacion the diaporcuotaFinanciacion to set
	 */
	public void setDiaporcuotaFinanciacion(int diaporcuotaFinanciacion) {
		this.diaporcuotaFinanciacion = diaporcuotaFinanciacion;
	}

	/**
	 * @return the fechaInicioFinanciacion
	 */
	public String getFechaInicioFinanciacion() {
		return fechaInicioFinanciacion;
	}

	/**
	 * @param fechaInicioFinanciacion the fechaInicioFinanciacion to set
	 */
	public void setFechaInicioFinanciacion(String fechaInicioFinanciacion) {
		this.fechaInicioFinanciacion = fechaInicioFinanciacion;
	}

	/**
	 * @return the fechaFactura
	 */
	public String getFechaFactura() {
		return fechaFactura;
	}

	/**
	 * @param fechaFactura the fechaFactura to set
	 */
	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}

	/**
	 * @return the codDatosFinanciacion
	 */
	public String getCodDatosFinanciacion() {
		return codDatosFinanciacion;
	}

	/**
	 * @param codDatosFinanciacion the codDatosFinanciacion to set
	 */
	public void setCodDatosFinanciacion(String codDatosFinanciacion) {
		this.codDatosFinanciacion = codDatosFinanciacion;
	}

	/**
	 * @return the coDeudor
	 */
	public DtoDeudoresDatosFinan getCoDeudor() {
		return coDeudor;
	}

	/**
	 * @param coDeudor the coDeudor to set
	 */
	public void setCoDeudor(DtoDeudoresDatosFinan coDeudor) {
		this.coDeudor = coDeudor;
	}

	/**
	 * @return the valorDocGarantia
	 */
	public String getValorDocGarantia() {
		return valorDocGarantia;
	}

	/**
	 * @param valorDocGarantia the valorDocGarantia to set
	 */
	public void setValorDocGarantia(String valorDocGarantia) {
		this.valorDocGarantia = valorDocGarantia;
	}

	

	
}
