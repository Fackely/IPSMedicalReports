package com.princetonsa.dto.interfaz;

import java.io.Serializable;

import util.UtilidadFecha;

/**
 * 
 * @author wilson
 *
 */
public class DtoInterfazFactura implements Serializable
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
	String fechaDocumento;
	
	/**
	 * 
	 */
	String horaDocumento;
	
	/**
	 * 
	 */
	String fechaVencimiento;
	
	/**
	 * 
	 */
	String terceroPaciente;
	
	/**
	 * 
	 */
	String terceroEntidad;
	
	/**
	 * 
	 */
	int codigoConvenio;
	
	/**
	 * 
	 */
	int numeroDiasVencimientoConvenio;
	
	/**
	 * 
	 */
	double valorPacienteConDescuento;
	
	/**
	 * 
	 */
	double valorEntidadConDescuento;
	
	/**
	 * 
	 */
	double valorAbonosPacienteAplicados;
	
	/**
	 * 
	 */
	double valorDescuentosPaciente;
	
	/**
	 * 
	 */
	double valorDescuentoFactura;
	
	/**
	 * 
	 */
	double valorTotalFacturaConDescuento;
	
	/**
	 * 
	 */
	String cuentaConvenio;
	
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
	int indicativoRadicado;
	
	/**
	 * 
	 */
	double venTec;
	
	/**
	 * 
	 */
	String usuario;
	
	
	/**
	 * 
	 * @param tipoMovimiento
	 * @param documento
	 * @param ingreso
	 * @param fechaDocumento
	 * @param horaDocumento
	 * @param fechaVencimiento
	 * @param terceroPaciente
	 * @param terceroEntidad
	 * @param codigoConvenio
	 * @param numeroDiasVencimientoConvenio
	 * @param valorPacienteConDescuento
	 * @param valorEntidadConDescuento
	 * @param valorAbonosPacienteAplicados
	 * @param valorDescuentosPaciente
	 * @param valorDescuentoFactura
	 * @param valorTotalFacturaConDescuento
	 * @param cuentaConvenio
	 * @param estadoRegistro
	 * @param contabilizado
	 * @param usuario 
	 */
	public DtoInterfazFactura(int tipoMovimiento, String documento, String ingreso, String fechaDocumento, String horaDocumento, String fechaVencimiento, String terceroPaciente, String terceroEntidad, int codigoConvenio, int numeroDiasVencimientoConvenio, double valorPacienteConDescuento, double valorEntidadConDescuento, double valorAbonosPacienteAplicados, double valorDescuentosPaciente, double valorDescuentoFactura, double valorTotalFacturaConDescuento, String cuentaConvenio, String estadoRegistro, String contabilizado, int codigoInstitucion, int indicaticoRadicado, double venTec, String usuario) {
		super();
		this.tipoMovimiento = tipoMovimiento;
		this.documento = documento;
		this.ingreso = ingreso;
		this.fechaDocumento = fechaDocumento;
		this.horaDocumento = horaDocumento;
		this.fechaVencimiento = fechaVencimiento;
		this.terceroPaciente = terceroPaciente;
		this.terceroEntidad = terceroEntidad;
		this.codigoConvenio = codigoConvenio;
		this.numeroDiasVencimientoConvenio = numeroDiasVencimientoConvenio;
		this.valorPacienteConDescuento = valorPacienteConDescuento;
		this.valorEntidadConDescuento = valorEntidadConDescuento;
		this.valorAbonosPacienteAplicados = valorAbonosPacienteAplicados;
		this.valorDescuentosPaciente = valorDescuentosPaciente;
		this.valorDescuentoFactura = valorDescuentoFactura;
		this.valorTotalFacturaConDescuento = valorTotalFacturaConDescuento;
		this.cuentaConvenio = cuentaConvenio;
		this.estadoRegistro = estadoRegistro;
		this.contabilizado = contabilizado;
		this.codigoInstitucion=codigoInstitucion;
		this.indicativoRadicado=indicaticoRadicado;
		this.venTec=venTec;
		this.usuario=usuario;
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
	 * @return the cuentaConvenio
	 */
	public String getCuentaConvenio() {
		return cuentaConvenio;
	}

	/**
	 * @param cuentaConvenio the cuentaConvenio to set
	 */
	public void setCuentaConvenio(String cuentaConvenio) {
		this.cuentaConvenio = cuentaConvenio;
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
	 * @return the fechaDocumento
	 */
	public String getFechaDocumento() {
		return fechaDocumento;
	}

	/**
	 * @return the fechaDocumento
	 */
	public String getFechaDocumentoFormatoShaio() 
	{
		if(UtilidadFecha.esFechaValidaSegunAp(fechaDocumento))
			this.fechaDocumento=UtilidadFecha.conversionFormatoFechaABD(fechaDocumento);
		return fechaDocumento.replaceAll("-", "");
	}
	
	
	/**
	 * @param fechaDocumento the fechaDocumento to set
	 */
	public void setFechaDocumento(String fechaDocumento) {
		this.fechaDocumento = fechaDocumento;
	}

	/**
	 * @return the fechaVencimiento
	 */
	public String getFechaVencimientoFormatoShaio() 
	{
		if(UtilidadFecha.esFechaValidaSegunAp(fechaVencimiento))
			this.fechaVencimiento=UtilidadFecha.conversionFormatoFechaABD(fechaVencimiento);
		return fechaVencimiento.replaceAll("-", "");
	}

	/**
	 * @param fechaVencimiento the fechaVencimiento to set
	 */
	public void setFechaVencimiento(String fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	/**
	 * @return the horaDocumento
	 */
	public String getHoraDocumento() {
		return horaDocumento;
	}

	/**
	 * @return the horaDocumento
	 */
	public String getHoraDocumentoFormatoShaio() {
		return horaDocumento.replaceAll(":", "");
	}
	
	/**
	 * @param horaDocumento the horaDocumento to set
	 */
	public void setHoraDocumento(String horaDocumento) {
		this.horaDocumento = horaDocumento;
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
	 * @return the numeroDiasVencimientoConvenio
	 */
	public int getNumeroDiasVencimientoConvenio() {
		return numeroDiasVencimientoConvenio;
	}

	/**
	 * @param numeroDiasVencimientoConvenio the numeroDiasVencimientoConvenio to set
	 */
	public void setNumeroDiasVencimientoConvenio(int numeroDiasVencimientoConvenio) {
		this.numeroDiasVencimientoConvenio = numeroDiasVencimientoConvenio;
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
	 * @return the valorAbonosPacienteAplicados
	 */
	public double getValorAbonosPacienteAplicados() {
		return valorAbonosPacienteAplicados;
	}

	/**
	 * @param valorAbonosPacienteAplicados the valorAbonosPacienteAplicados to set
	 */
	public void setValorAbonosPacienteAplicados(double valorAbonosPacienteAplicados) {
		this.valorAbonosPacienteAplicados = valorAbonosPacienteAplicados;
	}

	/**
	 * @return the valorDescuentoFactura
	 */
	public double getValorDescuentoFactura() {
		return valorDescuentoFactura;
	}

	/**
	 * @param valorDescuentoFactura the valorDescuentoFactura to set
	 */
	public void setValorDescuentoFactura(double valorDescuentoFactura) {
		this.valorDescuentoFactura = valorDescuentoFactura;
	}

	/**
	 * @return the valorDescuentosPaciente
	 */
	public double getValorDescuentosPaciente() {
		return valorDescuentosPaciente;
	}

	/**
	 * @param valorDescuentosPaciente the valorDescuentosPaciente to set
	 */
	public void setValorDescuentosPaciente(double valorDescuentosPaciente) {
		this.valorDescuentosPaciente = valorDescuentosPaciente;
	}

	/**
	 * @return the valorEntidadConDescuento
	 */
	public double getValorEntidadConDescuento() {
		return valorEntidadConDescuento;
	}

	/**
	 * @param valorEntidadConDescuento the valorEntidadConDescuento to set
	 */
	public void setValorEntidadConDescuento(double valorEntidadConDescuento) {
		this.valorEntidadConDescuento = valorEntidadConDescuento;
	}

	/**
	 * @return the valorPacienteConDescuento
	 */
	public double getValorPacienteConDescuento() {
		return valorPacienteConDescuento;
	}

	/**
	 * @param valorPacienteConDescuento the valorPacienteConDescuento to set
	 */
	public void setValorPacienteConDescuento(double valorPacienteConDescuento) {
		this.valorPacienteConDescuento = valorPacienteConDescuento;
	}

	/**
	 * @return the valorTotalFacturaConDescuento
	 */
	public double getValorTotalFacturaConDescuento() {
		return valorTotalFacturaConDescuento;
	}

	/**
	 * @param valorTotalFacturaConDescuento the valorTotalFacturaConDescuento to set
	 */
	public void setValorTotalFacturaConDescuento(
			double valorTotalFacturaConDescuento) {
		this.valorTotalFacturaConDescuento = valorTotalFacturaConDescuento;
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
	 * @return the indicativoRadicado
	 */
	public int getIndicativoRadicado() {
		return indicativoRadicado;
	}

	/**
	 * @param indicativoRadicado the indicativoRadicado to set
	 */
	public void setIndicativoRadicado(int indicativoRadicado) {
		this.indicativoRadicado = indicativoRadicado;
	}

	/**
	 * @return the venTec
	 */
	public double getVenTec() {
		return venTec;
	}

	/**
	 * @param venTec the venTec to set
	 */
	public void setVenTec(double venTec) {
		this.venTec = venTec;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	
	
	
}
