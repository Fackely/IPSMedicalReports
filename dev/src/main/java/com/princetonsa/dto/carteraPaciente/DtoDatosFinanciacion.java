package com.princetonsa.dto.carteraPaciente;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.ConstantesBD;
import util.UtilidadFecha;

/**
 * 
 * @author Vícto Gómez L.
 *
 */
public class DtoDatosFinanciacion implements Serializable
{
	private int codigoPk;
	private int detallePagoRC;
	private int detalleConceptoRC;
	private DtoDocumentosGarantia documentoGarantia;
	private DtoDeudoresDatosFinan deudor;
	private DtoDeudoresDatosFinan codeudor;
	private int ingreso;
	private int codigoPacienteFac;
	private String consecutivo;
	private String anioConsecutivo;
	private String nombreCentroAtenDocGaran;
	private String tipoDocumento;
	private int codigoFactura;
	private String fechaModifica;
	private String horaModifica;
	private String usuarioModifica;
	private String fechaInicio;
	private int diasPorCuota;
	private String observaciones;
	private String nomDeudor;
	private String idDeudor;
	private int nroCoutas;	
	private int diasVencimiento;
	private ArrayList<DtoCuotasDatosFinanciacion> cuotasDatosFinan;
	private BigDecimal valorForPago;
	private String posicionRango;
	private String valorSaldo;
	private double codigo;
	
	// Atributos Validacion
	private int maxNroCuotasFinan;
	private int maxNroDiasFinanCuo;
	private String isNuevoDoc;
	private String procesoExito;
	private String procesoExitosoCuo;
	
	
	///atributos para propips para el registro de saldos iniciales de cartera paciente.
	private String consecutivoFactura;
	private String fechaElaboracionFactura;
	private double valorConvenio;
	private double valorPaciente;
	
	private double valorTotalDocumento;
	
	//variable para almacenar el codigo del documento de garantia asociado.
	private int codigoDocumentoGarantia;
	
	
	private String saldoInicialCartera;
	
	public DtoDatosFinanciacion()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.detallePagoRC = ConstantesBD.codigoNuncaValido;
		this.detalleConceptoRC = ConstantesBD.codigoNuncaValido;
		this.deudor = new DtoDeudoresDatosFinan();
		this.codeudor = new DtoDeudoresDatosFinan();
		this.ingreso = ConstantesBD.codigoNuncaValido;
		this.codigoPacienteFac = ConstantesBD.codigoNuncaValido;
		this.consecutivo = "";
		this.anioConsecutivo = "";
		this.tipoDocumento = "";
		this.codigoFactura = ConstantesBD.codigoNuncaValido;
		this.fechaModifica = "";
		this.horaModifica = "" ;
		this.usuarioModifica = "";
		this.fechaInicio = "";
		this.diasPorCuota = ConstantesBD.codigoNuncaValido;
		this.observaciones = "";
		this.nroCoutas = ConstantesBD.codigoNuncaValido; 
		this.nomDeudor="";
		this.idDeudor="";
		this.isNuevoDoc = "";
		this.cuotasDatosFinan = new ArrayList<DtoCuotasDatosFinanciacion>();
		this.valorForPago = new BigDecimal(0.0);
		this.valorTotalDocumento = 0;
		this.documentoGarantia= new DtoDocumentosGarantia();
		this.codigo=0;
		this.codigoDocumentoGarantia=ConstantesBD.codigoNuncaValido;
		//this.maxNroCuotasFinan = ConstantesBD.codigoNuncaValido;
		//this.maxNroDiasFinanCuo = ConstantesBD.codigoNuncaValido;
		
		
		
		///atributos para propips para el registro de saldos iniciales de cartera paciente.
		this.consecutivoFactura="";
		this.fechaElaboracionFactura="";
		this.valorConvenio=0;
		this.valorPaciente=0;
		this.saldoInicialCartera="N";
	}

	/**
	 * @return the saldoInicialCartera
	 */
	public String getSaldoInicialCartera() {
		return saldoInicialCartera;
	}

	/**
	 * @param saldoInicialCartera the saldoInicialCartera to set
	 */
	public void setSaldoInicialCartera(String saldoInicialCartera) {
		this.saldoInicialCartera = saldoInicialCartera;
	}

	/**
	 * @return the codigoDocumentoGarantia
	 */
	public int getCodigoDocumentoGarantia() {
		return codigoDocumentoGarantia;
	}

	/**
	 * @param codigoDocumentoGarantia the codigoDocumentoGarantia to set
	 */
	public void setCodigoDocumentoGarantia(int codigoDocumentoGarantia) {
		this.codigoDocumentoGarantia = codigoDocumentoGarantia;
	}

	public double getValorTotalDocumento() {
		return valorTotalDocumento;
	}

	public void setValorTotalDocumento(double valorTotalDocumento) {
		this.valorTotalDocumento = valorTotalDocumento;
	}

	public String getConsecutivoFactura() {
		return consecutivoFactura;
	}

	public void setConsecutivoFactura(String consecutivoFactura) {
		this.consecutivoFactura = consecutivoFactura;
	}

	public String getFechaElaboracionFactura() {
		return fechaElaboracionFactura;
	}

	public void setFechaElaboracionFactura(String fechaElaboracionFactura) {
		this.fechaElaboracionFactura = fechaElaboracionFactura;
	}

	public double getValorConvenio() {
		return valorConvenio;
	}

	public void setValorConvenio(double valorConvenio) {
		this.valorConvenio = valorConvenio;
	}

	public double getValorPaciente() {
		return valorPaciente;
	}

	public void setValorPaciente(double valorPaciente) {
		this.valorPaciente = valorPaciente;
	}

	public DtoDocumentosGarantia getDocumentoGarantia() {
		return documentoGarantia;
	}

	public void setDocumentoGarantia(DtoDocumentosGarantia documentoGarantia) {
		this.documentoGarantia = documentoGarantia;
	}

	public String getIdDeudor() {
		return idDeudor;
	}

	public void setIdDeudor(String idDeudor) {
		this.idDeudor = idDeudor;
	}

	public String getNomDeudor() {
		return nomDeudor;
	}

	public void setNomDeudor(String nomDeudor) {
		this.nomDeudor = nomDeudor;
		this.diasVencimiento = ConstantesBD.codigoNuncaValido;
		this.cuotasDatosFinan = new ArrayList<DtoCuotasDatosFinanciacion>(); 
		// Atributos validacion
		this.maxNroCuotasFinan = ConstantesBD.codigoNuncaValido;
		this.maxNroDiasFinanCuo = ConstantesBD.codigoNuncaValido;
		this.isNuevoDoc = "";
		this.procesoExito = ConstantesBD.acronimoNo;
		this.procesoExitosoCuo = ConstantesBD.acronimoNo;
		this.posicionRango = "";
		this.nombreCentroAtenDocGaran = "";
		this.valorSaldo = "";
	}

	
	/**
	 * Calcula el valor de dias de vencimiento
	 * @param String fechaInicial
	 * @param String fechaFinal
	 * @param int dias
	 * */
	public static int calcularDiasVencimiento(String fechaInicial,String fechaCorte,int dias)
	{
		int valor = ConstantesBD.codigoNuncaValido;
		String fecha = UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaABD(fechaInicial),dias,true);
		
		if(UtilidadFecha.compararFechas(fecha,"00:00",fechaCorte,"00:00").isTrue())
			return 0;
		else
			UtilidadFecha.numeroDiasEntreFechas(fecha,fechaCorte);
		
		return valor;
	}
	
	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the detallePagoRC
	 */
	public int getDetallePagoRC() {
		return detallePagoRC;
	}

	/**
	 * @param detallePagoRC the detallePagoRC to set
	 */
	public void setDetallePagoRC(int detallePagoRC) {
		this.detallePagoRC = detallePagoRC;
	}

	/**
	 * @return the detalleConceptoRC
	 */
	public int getDetalleConceptoRC() {
		return detalleConceptoRC;
	}

	/**
	 * @param detalleConceptoRC the detalleConceptoRC to set
	 */
	public void setDetalleConceptoRC(int detalleConceptoRC) {
		this.detalleConceptoRC = detalleConceptoRC;
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
	 * @return the codeudor
	 */
	public DtoDeudoresDatosFinan getCodeudor() {
		return codeudor;
	}

	/**
	 * @param codeudor the codeudor to set
	 */
	public void setCodeudor(DtoDeudoresDatosFinan codeudor) {
		this.codeudor = codeudor;
	}

	/**
	 * @return the ingreso
	 */
	public int getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
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
	 * @return the anioConsecutivo
	 */
	public String getAnioConsecutivo() {
		return anioConsecutivo;
	}

	/**
	 * @param anioConsecutivo the anioConsecutivo to set
	 */
	public void setAnioConsecutivo(String anioConsecutivo) {
		this.anioConsecutivo = anioConsecutivo;
	}

	/**
	 * @return the tipoDocumento
	 */
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento the tipoDocumento to set
	 */
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return the codigoFactura
	 */
	public int getCodigoFactura() {
		return codigoFactura;
	}

	/**
	 * @param codigoFactura the codigoFactura to set
	 */
	public void setCodigoFactura(int codigoFactura) {
		this.codigoFactura = codigoFactura;
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
	 * @return the fechaInicio
	 */
	public String getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * @param fechaInicio the fechaInicio to set
	 */
	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * @return the diasPorCuota
	 */
	public int getDiasPorCuota() {
		return diasPorCuota;
	}

	/**
	 * @param diasPorCuota the diasPorCuota to set
	 */
	public void setDiasPorCuota(int diasPorCuota) {
		this.diasPorCuota = diasPorCuota;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the nroCoutas
	 */
	public int getNroCoutas() {
		return nroCoutas;
	}

	/**
	 * @param nroCoutas the nroCoutas to set
	 */
	public void setNroCoutas(int nroCoutas) {
		this.nroCoutas = nroCoutas;
	}

	public int getDiasVencimiento() {
		return diasVencimiento;
	}

	public void setDiasVencimiento(int diasVencimiento) {
		this.diasVencimiento = diasVencimiento;
	}

	/**
	 * @return the maxNroCuotasFinan
	 */
	public int getMaxNroCuotasFinan() {
		return maxNroCuotasFinan;
	}

	/**
	 * @param maxNroCuotasFinan the maxNroCuotasFinan to set
	 */
	public void setMaxNroCuotasFinan(int maxNroCuotasFinan) {
		this.maxNroCuotasFinan = maxNroCuotasFinan;
	}

	/**
	 * @return the maxNroDiasFinanCuo
	 */
	public int getMaxNroDiasFinanCuo() {
		return maxNroDiasFinanCuo;
	}

	/**
	 * @param maxNroDiasFinanCuo the maxNroDiasFinanCuo to set
	 */
	public void setMaxNroDiasFinanCuo(int maxNroDiasFinanCuo) {
		this.maxNroDiasFinanCuo = maxNroDiasFinanCuo;
	}

	/**
	 * @return the cuotasDatosFinan
	 */
	public ArrayList<DtoCuotasDatosFinanciacion> getCuotasDatosFinan() {
		return cuotasDatosFinan;
	}

	/**
	 * @param cuotasDatosFinan the cuotasDatosFinan to set
	 */
	public void setCuotasDatosFinan(
			ArrayList<DtoCuotasDatosFinanciacion> cuotasDatosFinan) {
		this.cuotasDatosFinan = cuotasDatosFinan;
	}

	/**
	 * @return the isNuevoDoc
	 */
	public String getIsNuevoDoc() {
		return isNuevoDoc;
	}

	/**
	 * @param isNuevoDoc the isNuevoDoc to set
	 */
	public void setIsNuevoDoc(String isNuevoDoc) {
		this.isNuevoDoc = isNuevoDoc;
	}

	/**
	 * @return the procesoExito
	 */
	public String getProcesoExito() {
		return procesoExito;
	}

	/**
	 * @param procesoExito the procesoExito to set
	 */
	public void setProcesoExito(String procesoExito) {
		this.procesoExito = procesoExito;
	}

	/**
	 * @return the procesoExitosoCuo
	 */
	public String getProcesoExitosoCuo() {
		return procesoExitosoCuo;
	}

	/**
	 * @param procesoExitosoCuo the procesoExitosoCuo to set
	 */
	public void setProcesoExitosoCuo(String procesoExitosoCuo) {
		this.procesoExitosoCuo = procesoExitosoCuo;
	}

	/**
	 * @return the codigoPacienteFac
	 */
	public int getCodigoPacienteFac() {
		return codigoPacienteFac;
	}

	/**
	 * @param codigoPacienteFac the codigoPacienteFac to set
	 */
	public void setCodigoPacienteFac(int codigoPacienteFac) {
		this.codigoPacienteFac = codigoPacienteFac;
	}
	
	public String getPosicionRango() {
		return posicionRango;
	}

	public void setPosicionRango(String posicionRango) {
		this.posicionRango = posicionRango;
	}

	public String getNombreCentroAtenDocGaran() {
		return nombreCentroAtenDocGaran;
	}

	public void setNombreCentroAtenDocGaran(String nombreCentroAtenDocGaran) {
		this.nombreCentroAtenDocGaran = nombreCentroAtenDocGaran;
	}

	public String getValorSaldo() {
		return valorSaldo;
	}

	public void setValorSaldo(String valorSaldo) {
		this.valorSaldo = valorSaldo;
	}

	/**
	 * @return the valorForPago
	 */
	public BigDecimal getValorForPago() 
	{
		this.valorForPago = new BigDecimal(0);
		for(int i=0;i<this.cuotasDatosFinan.size();i++)
			this.valorForPago = this.valorForPago.add(this.cuotasDatosFinan.get(i).getValorCuota());
		return this.valorForPago;
	}

	public double getCodigo() {
		return codigo;
	}

	public void setCodigo(double codigo) {
		this.codigo = codigo;
	}
}
