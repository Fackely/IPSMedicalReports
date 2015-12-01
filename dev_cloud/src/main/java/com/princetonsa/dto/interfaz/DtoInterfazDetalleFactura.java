package com.princetonsa.dto.interfaz;

import java.io.Serializable;

import util.ConstantesBD;
import util.UtilidadFecha;

/**
 * 
 * @author wilson
 *
 */
public class DtoInterfazDetalleFactura implements Serializable
{
	/**
	 * 
	 */
	int tipoMovimiento;
	
	/**
	 * 
	 */
	String documento;
	
	/**
	 * 
	 */
	String ingreso;
	
	/**
	 * 
	 */
	String centroCostoPaciente;
	
	/**
	 * 
	 */
	String centroCostoEjecuta;
	
	/**
	 * 
	 */
	String conceptoFactura;
	
	/**
	 * 
	 */
	double consecutivoConsumo;
	
	/**
	 * 
	 */
	int codigoViaIngreso;
	
	/**
	 * 
	 */
	String medicoOrdena;
	
	/**
	 * 
	 */
	String medicoEjecuta;
	
	/**
	 * 
	 */
	String fechaConsumo;
	
	/**
	 * 
	 */
	String horaConsumo;
	
	/**
	 * 
	 */
	String cups;
	
	/**
	 * 
	 */
	String paquete;
	
	/**
	 * 
	 */
	int cantidad;
	
	/**
	 * 
	 */
	String auxiliar;
	
	/**
	 * 
	 */
	double valorConsumoUnitario;
	
	/**
	 * 
	 */
	double valorConsumoTotal;
	
	/**
	 * 
	 */
	double valorDescuentoTotalConsumo;
	
	/**
	 * 
	 */
	double valorNetoTotalConsumo;
	
	/**
	 * 
	 */
	String terceroEntidad;
	
	/**
	 * 
	 */
	String terceroPaciente;
	
	/**
	 * 
	 */
	int codigoConvenio;
	
	/**
	 * 
	 */
	String estadoRegistro;
	
	/**
	 * 
	 */
	String contabilizado;
	
	/**
	 * 
	 */
	int codigoInstitucion;

	/**
	 * 
	 */
	int centroCostoPlanEspecialConvenio;
	
	/**
	 * 
	 */
	private String consecutivo;
	
	/**
	 * 
	 */
	private String viaIngreso;
	
	/**
	 * 
	 */
	private String fechaRegistro;
	
	/**
	 * 
	 */
	private String horaRegistro;
	
	/**
	 * 
	 */
	private String fechaModifica;
	
	/**
	 * 
	 */
	private String horaModifica;
	
	/**
	 * 
	 */
	private String indicativoContabilizado;
	
	/**
	 * 
	 */
	private String indicativoCobrable;
	
	/**
	 * 
	 */
	private String codigoInterfazConvenio="";
	
	/**
	 * @return the codigoInterfazConvenio
	 */
	public String getCodigoInterfazConvenio() {
		return codigoInterfazConvenio;
	}

	/**
	 * @param codigoInterfazConvenio the codigoInterfazConvenio to set
	 */
	public void setCodigoInterfazConvenio(String codigoInterfazConvenio) {
		this.codigoInterfazConvenio = codigoInterfazConvenio;
	}

	public DtoInterfazDetalleFactura() {
		super();
		this.tipoMovimiento = ConstantesBD.codigoNuncaValido;
		this.documento = "";
		this.ingreso = "";
		this.centroCostoPaciente = "";
		this.centroCostoEjecuta = "";
		this.conceptoFactura = "";
		this.consecutivoConsumo = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.codigoViaIngreso = ConstantesBD.codigoNuncaValido;
		this.medicoOrdena = "";
		this.medicoEjecuta = "";
		this.fechaConsumo = "";
		this.horaConsumo = "";
		this.cups = "";
		this.paquete = "";
		this.cantidad = ConstantesBD.codigoNuncaValido;
		this.auxiliar = "";
		this.valorConsumoUnitario = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorConsumoTotal = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorDescuentoTotalConsumo = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.valorNetoTotalConsumo = ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.terceroEntidad = "";
		this.terceroPaciente = "";
		this.codigoConvenio = ConstantesBD.codigoNuncaValido;
		this.estadoRegistro = "";
		this.contabilizado = "";
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		this.centroCostoPlanEspecialConvenio=ConstantesBD.codigoNuncaValido;
		this.consecutivo="";
		this.viaIngreso="";
		this.fechaRegistro="";
		this.horaRegistro="";
		this.fechaModifica="";
		this.horaModifica="";
		this.indicativoContabilizado="";
		this.indicativoCobrable="";
		this.codigoInterfazConvenio="";
	}
	
	/**
	 * @return the fechaRegistro
	 */
	public String getFechaRegistro() {
		return fechaRegistro;
	}


	/**
	 * @param fechaRegistro the fechaRegistro to set
	 */
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}


	/**
	 * @return the horaRegistro
	 */
	public String getHoraRegistro() {
		return horaRegistro;
	}


	/**
	 * @param horaRegistro the horaRegistro to set
	 */
	public void setHoraRegistro(String horaRegistro) {
		this.horaRegistro = horaRegistro;
	}


	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}


	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}


	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}


	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}


	/**
	 * @return the indicativoContabilizado
	 */
	public String getIndicativoContabilizado() {
		return indicativoContabilizado;
	}


	/**
	 * @param indicativoContabilizado the indicativoContabilizado to set
	 */
	public void setIndicativoContabilizado(String indicativoContabilizado) {
		this.indicativoContabilizado = indicativoContabilizado;
	}


	/**
	 * @return the indicativoCobrable
	 */
	public String getIndicativoCobrable() {
		return indicativoCobrable;
	}


	/**
	 * @param indicativoCobrable the indicativoCobrable to set
	 */
	public void setIndicativoCobrable(String indicativoCobrable) {
		this.indicativoCobrable = indicativoCobrable;
	}

	/**
	 * @return the viaIngreso
	 */
	public String getViaIngreso() {
		return viaIngreso;
	}


	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(String viaIngreso) {
		this.viaIngreso = viaIngreso;
	}


	/**
	 * 
	 * @param tipoMovimiento
	 * @param documento
	 * @param ingreso
	 * @param centroCostoPaciente
	 * @param centroCostoEjecuta
	 * @param conceptoFactura
	 * @param consecutivoConsumo
	 * @param codigoViaIngreso
	 * @param medicoOrdena
	 * @param medicoEjecuta
	 * @param fechaConsumo
	 * @param horaConsumo
	 * @param cups
	 * @param paquete
	 * @param cantidad
	 * @param auxiliar
	 * @param valorConsumoUnitario
	 * @param valorConsumoTotal
	 * @param valorDescuentoTotalConsumo
	 * @param valorNetoTotalConsumo
	 * @param terceroEntidad
	 * @param terceroPaciente
	 * @param codigoConvenio
	 * @param estadoRegistro
	 * @param fechaRegistro
	 * @param horaRegistro
	 * @param contabilizado
	 * @param codigoInstitucion
	 * @param centroCostoPlanEspecialConvenio
	 */
	public DtoInterfazDetalleFactura(int tipoMovimiento, String documento, String ingreso, String centroCostoPaciente, String centroCostoEjecuta, String conceptoFactura, double consecutivoConsumo, int codigoViaIngreso, String medicoOrdena, String medicoEjecuta, String fechaConsumo, String horaConsumo, String cups, String paquete, int cantidad, String auxiliar, double valorConsumoUnitario, double valorConsumoTotal, double valorDescuentoTotalConsumo, double valorNetoTotalConsumo, String terceroEntidad, String terceroPaciente, int codigoConvenio, String estadoRegistro, String contabilizado, int codigoInstitucion, int centroCostoPlanEspecialConvenio) {
		super();
		this.tipoMovimiento = tipoMovimiento;
		this.documento = documento;
		this.ingreso = ingreso;
		this.centroCostoPaciente = centroCostoPaciente;
		this.centroCostoEjecuta = centroCostoEjecuta;
		this.conceptoFactura = conceptoFactura;
		this.consecutivoConsumo = consecutivoConsumo;
		this.codigoViaIngreso = codigoViaIngreso;
		this.medicoOrdena = medicoOrdena;
		this.medicoEjecuta = medicoEjecuta;
		this.fechaConsumo = fechaConsumo;
		this.horaConsumo = horaConsumo;
		this.cups = cups;
		this.paquete = paquete;
		this.cantidad = cantidad;
		this.auxiliar = auxiliar;
		this.valorConsumoUnitario = valorConsumoUnitario;
		this.valorConsumoTotal = valorConsumoTotal;
		this.valorDescuentoTotalConsumo = valorDescuentoTotalConsumo;
		this.valorNetoTotalConsumo = valorNetoTotalConsumo;
		this.terceroEntidad = terceroEntidad;
		this.terceroPaciente = terceroPaciente;
		this.codigoConvenio = codigoConvenio;
		this.estadoRegistro = estadoRegistro;
		this.contabilizado = contabilizado;
		this.codigoInstitucion = codigoInstitucion;
		this.centroCostoPlanEspecialConvenio=centroCostoPlanEspecialConvenio;
	}
	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}


	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}
	/**
	 * @return the auxiliar
	 */
	public String getAuxiliar() {
		return auxiliar;
	}

	/**
	 * @param auxiliar the auxiliar to set
	 */
	public void setAuxiliar(String auxiliar) {
		this.auxiliar = auxiliar;
	}

	/**
	 * @return the cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the centroCostoPaciente
	 */
	public String getCentroCostoPaciente() {
		return centroCostoPaciente;
	}

	/**
	 * @param centroCostoPaciente the centroCostoPaciente to set
	 */
	public void setCentroCostoPaciente(String centroCostoPaciente) {
		this.centroCostoPaciente = centroCostoPaciente;
	}

	/**
	 * @return the centroCostoEjecuta
	 */
	public String getCentroCostoEjecuta() {
		return centroCostoEjecuta;
	}

	/**
	 * @param centroCostoEjecuta the centroCostoEjecuta to set
	 */
	public void setCentroCostoEjecuta(String centroCostoEjecuta) {
		this.centroCostoEjecuta = centroCostoEjecuta;
	}

	/**
	 * @return the codigoConvenio
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * @param codigoConvenio the codigoConvenio to set
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * @return the codigoViaIngreso
	 */
	public int getCodigoViaIngreso() {
		return codigoViaIngreso;
	}

	/**
	 * @param codigoViaIngreso the codigoViaIngreso to set
	 */
	public void setCodigoViaIngreso(int codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}

	/**
	 * @return the conceptoFactura
	 */
	public String getConceptoFactura() {
		return conceptoFactura;
	}

	/**
	 * @param conceptoFactura the conceptoFactura to set
	 */
	public void setConceptoFactura(String conceptoFactura) {
		this.conceptoFactura = conceptoFactura;
	}

	/**
	 * @return the consecutivoConsumo
	 */
	public double getConsecutivoConsumo() {
		return consecutivoConsumo;
	}

	/**
	 * @param consecutivoConsumo the consecutivoConsumo to set
	 */
	public void setConsecutivoConsumo(double consecutivoConsumo) {
		this.consecutivoConsumo = consecutivoConsumo;
	}

	/**
	 * @return the contabilizado
	 */
	public String getContabilizado() {
		return contabilizado;
	}

	/**
	 * @param contabilizado the contabilizado to set
	 */
	public void setContabilizado(String contabilizado) {
		this.contabilizado = contabilizado;
	}

	/**
	 * @return the cups
	 */
	public String getCups() {
		return cups;
	}

	/**
	 * @param cups the cups to set
	 */
	public void setCups(String cups) {
		this.cups = cups;
	}

	/**
	 * @return the documento
	 */
	public String getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(String documento) {
		this.documento = documento;
	}

	/**
	 * @return the estadoRegistro
	 */
	public String getEstadoRegistro() {
		return estadoRegistro;
	}

	/**
	 * @param estadoRegistro the estadoRegistro to set
	 */
	public void setEstadoRegistro(String estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}

	/**
	 * @return the fechaConsumo
	 */
	public String getFechaConsumo() {
		return fechaConsumo;
	}

	/**
	 * @return the fechaConsumo
	 */
	public String getFechaConsumoFormatoShaio() 
	{
		if(UtilidadFecha.esFechaValidaSegunAp(this.getFechaConsumo()))
			this.fechaConsumo=UtilidadFecha.conversionFormatoFechaABD(this.fechaConsumo);
		return fechaConsumo.replaceAll("-", "");
	}
	
	/**
	 * @param fechaConsumo the fechaConsumo to set
	 */
	public void setFechaConsumo(String fechaConsumo) {
		this.fechaConsumo = fechaConsumo;
	}

	/**
	 * @return the horaConsumo
	 */
	public String getHoraConsumo() {
		return horaConsumo;
	}

	/**
	 * @return the horaConsumo
	 */
	public String getHoraConsumoFormatoShaio() {
		return horaConsumo.replaceAll(":", "");
	}
	
	/**
	 * @param horaConsumo the horaConsumo to set
	 */
	public void setHoraConsumo(String horaConsumo) {
		this.horaConsumo = horaConsumo;
	}

	/**
	 * @return the ingreso
	 */
	public String getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(String ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the medicoEjecuta
	 */
	public String getMedicoEjecuta() {
		return medicoEjecuta;
	}

	/**
	 * @param medicoEjecuta the medicoEjecuta to set
	 */
	public void setMedicoEjecuta(String medicoEjecuta) {
		this.medicoEjecuta = medicoEjecuta;
	}

	/**
	 * @return the medicoOrdena
	 */
	public String getMedicoOrdena() {
		return medicoOrdena;
	}

	/**
	 * @param medicoOrdena the medicoOrdena to set
	 */
	public void setMedicoOrdena(String medicoOrdena) {
		this.medicoOrdena = medicoOrdena;
	}

	/**
	 * @return the paquete
	 */
	public String getPaquete() {
		return paquete;
	}

	/**
	 * @param paquete the paquete to set
	 */
	public void setPaquete(String paquete) {
		this.paquete = paquete;
	}

	/**
	 * @return the terceroEntidad
	 */
	public String getTerceroEntidad() {
		return terceroEntidad;
	}

	/**
	 * @param terceroEntidad the terceroEntidad to set
	 */
	public void setTerceroEntidad(String terceroEntidad) {
		this.terceroEntidad = terceroEntidad;
	}

	/**
	 * @return the terceroPaciente
	 */
	public String getTerceroPaciente() {
		return terceroPaciente;
	}

	/**
	 * @param terceroPaciente the terceroPaciente to set
	 */
	public void setTerceroPaciente(String terceroPaciente) {
		this.terceroPaciente = terceroPaciente;
	}

	/**
	 * @return the tipoMovimiento
	 */
	public int getTipoMovimiento() {
		return tipoMovimiento;
	}

	/**
	 * @param tipoMovimiento the tipoMovimiento to set
	 */
	public void setTipoMovimiento(int tipoMovimiento) {
		this.tipoMovimiento = tipoMovimiento;
	}

	/**
	 * @return the valorConsumoTotal
	 */
	public double getValorConsumoTotal() {
		return valorConsumoTotal;
	}

	/**
	 * @param valorConsumoTotal the valorConsumoTotal to set
	 */
	public void setValorConsumoTotal(double valorConsumoTotal) {
		this.valorConsumoTotal = valorConsumoTotal;
	}

	/**
	 * @return the valorConsumoUnitario
	 */
	public double getValorConsumoUnitario() {
		return valorConsumoUnitario;
	}

	/**
	 * @param valorConsumoUnitario the valorConsumoUnitario to set
	 */
	public void setValorConsumoUnitario(double valorConsumoUnitario) {
		this.valorConsumoUnitario = valorConsumoUnitario;
	}

	/**
	 * @return the valorDescuentoTotalConsumo
	 */
	public double getValorDescuentoTotalConsumo() {
		return valorDescuentoTotalConsumo;
	}

	/**
	 * @param valorDescuentoTotalConsumo the valorDescuentoTotalConsumo to set
	 */
	public void setValorDescuentoTotalConsumo(double valorDescuentoTotalConsumo) {
		this.valorDescuentoTotalConsumo = valorDescuentoTotalConsumo;
	}

	/**
	 * @return the valorNetoTotalConsumo
	 */
	public double getValorNetoTotalConsumo() {
		return valorNetoTotalConsumo;
	}

	/**
	 * @param valorNetoTotalConsumo the valorNetoTotalConsumo to set
	 */
	public void setValorNetoTotalConsumo(double valorNetoTotalConsumo) {
		this.valorNetoTotalConsumo = valorNetoTotalConsumo;
	}

	public int getCentroCostoPlanEspecialConvenio() {
		return centroCostoPlanEspecialConvenio;
	}

	public void setCentroCostoPlanEspecialConvenio(
			int centroCostoPlanEspecialConvenio) {
		this.centroCostoPlanEspecialConvenio = centroCostoPlanEspecialConvenio;
	}
	
	
	
}
