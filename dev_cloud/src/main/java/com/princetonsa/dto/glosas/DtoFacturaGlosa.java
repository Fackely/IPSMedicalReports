/*
 * Ene 08, 2009
 */
package com.princetonsa.dto.glosas;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Data Transfer Object: 
 * Usado para el manejo de las facturas de la glosa
 * 
 * @author Sebastián Gómez R. - Felipe Pérez Granda
 *
 */
public class DtoFacturaGlosa implements Serializable
{
	private String codigo;
	private String codigoGlosa;
	private String codigoFactura;
	private String consecutivoFactura;
	private String fechaModificacion;
	private String horaModificacion;
	private UsuarioBasico usuarioModificacion;
	private int codigoContrato;
	private double valorGlosaFactura;
	private String valorGlosaFacturaStr;
	private double saldoFactura;
	private String saldoFacturaStr;
	private String fechaElaboracionFactura;
	private String numeroCuentaCobro;
	private String fechaRadicacion;
	private int institucion;
	private DtoFactura factura;
	private double porcentajeAceptado;
	private double porcentajeSoportado;
	private double valorAceptado;
	private double valorSoportado;
	
	//Totales de la factura
	private String totalFacturaStr;
	private String totalResponsableStr;
	private String totalBrutoPacienteStr;
	private String totalDescuentosStr;
	private String totalAbonosStr;
	private String totalNetoPacienteStr;
	
	/*
	 * Totales factura con tipo dato double
	 */
	private double totalFactura;
	private double totalResponsable;
	private double totalBrutoPaciente;
	private double totalDescuentos;
	private double totalAbonos;
	private double totalNetoPaciente;
	
	private String concepto;
	private String eliminar;
	private String seleccionado;
	private DtoConceptoGlosa conceptoGlosa;
	private DtoConceptoRespuesta conceptoRespuesta;
	
	private String motivo;
	
	private DtoGlosa glosa= new DtoGlosa();
	
	/**
	 * Arreglo donde se almacenan los distintos conceptos de la glosa
	 */
	private ArrayList<DtoConceptoGlosa> conceptos;
	
	/**
	 * Arreglo donde se almacena el detalle de la factura de una glosa
	 */
	private ArrayList<DtoDetalleFacturaGlosa> detalle;
	
	
	//Atributos para el manejo del flujo de facturas/detalle*********************************************
	/**
	 * Variable para manejar el estado de la seccion totales
	 */
	private boolean estadoSeccionTotales;
	
	/**
	 * Atributo usado para indicar que la factura se debe devolver
	 */
	private Boolean chequeoDevolucion;
	
	/**
	 * Atributos para saber la posicion de un detalle seleccionado
	 */
	private int  posicionDetalle;
	
	/**
	 * ATributos para saber la posicion del asocio elegido del detalle de la factura
	 */
	private int posicionAsocio;
	
	/**
	 * Atributos para saber la posicion de un concepto del detalle seleccionado
	 */
	private int posicionConcepto;
	
	/**
	 * Atributo para mostrar un mensaje de error
	 */
	private String mensajeError;
	
	//***************************************************************************************************
	
	/**
	 * Método que inicializa los atributos del DTO
	 */
	public void clean()
	{
		this.concepto="";
		this.codigo = "";
		this.codigoGlosa = "";
		this.codigoFactura = "";
		this.consecutivoFactura = "";
		this.factura= new DtoFactura();
		this.fechaModificacion = "";
		this.horaModificacion = "";
		this.usuarioModificacion = new UsuarioBasico();
		this.codigoContrato = ConstantesBD.codigoNuncaValido;
		this.valorGlosaFactura = 0;
		this.valorGlosaFacturaStr = "";
		this.saldoFactura = 0;
		this.saldoFacturaStr = "";
		this.fechaElaboracionFactura = "";
		this.numeroCuentaCobro = "";
		this.fechaRadicacion = "";
		this.conceptos = new ArrayList<DtoConceptoGlosa>();
		this.detalle = new ArrayList<DtoDetalleFacturaGlosa>();
		this.institucion=ConstantesBD.codigoNuncaValido;
		
		//Totales factura
		this.totalFacturaStr = "";
		this.totalResponsableStr = "";
		this.totalBrutoPacienteStr = "";
		this.totalDescuentosStr = "";
		this.totalAbonosStr = "";
		this.totalNetoPacienteStr = "";
		
		/*
		 * Totales factura de tipo dato double
		 */
		this.totalFactura = ConstantesBD.codigoNuncaValido;
		this.totalResponsable = ConstantesBD.codigoNuncaValido;
		this.totalBrutoPaciente = ConstantesBD.codigoNuncaValido;
		this.totalDescuentos = ConstantesBD.codigoNuncaValido;
		this.totalAbonos = ConstantesBD.codigoNuncaValido;
		this.totalNetoPaciente = ConstantesBD.codigoNuncaValido;
		
		this.estadoSeccionTotales = false;
		
		this.chequeoDevolucion = null;
		
		this.posicionDetalle = ConstantesBD.codigoNuncaValido;
		this.posicionAsocio = ConstantesBD.codigoNuncaValido;
		this.posicionConcepto = ConstantesBD.codigoNuncaValido;
		this.mensajeError = "";
		
		
		this.glosa= new DtoGlosa();
		this.porcentajeAceptado=0.0;
		this.porcentajeSoportado=0.0;
		this.valorAceptado=0.0;
		this.valorSoportado=0.0;
		this.eliminar=ConstantesBD.acronimoNo;
		this.seleccionado=ConstantesBD.acronimoSi;
		this.conceptoGlosa= new DtoConceptoGlosa();
		this.conceptoRespuesta= new DtoConceptoRespuesta();
		
		this.motivo="";
	}
	
	public DtoConceptoRespuesta getConceptoRespuesta() {
		return conceptoRespuesta;
	}

	public void setConceptoRespuesta(DtoConceptoRespuesta conceptoRespuesta) {
		this.conceptoRespuesta = conceptoRespuesta;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public DtoConceptoGlosa getConceptoGlosa() {
		return conceptoGlosa;
	}


	public String getSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(String seleccionado) {
		this.seleccionado = seleccionado;
	}

	public String getEliminar() {
		return eliminar;
	}

	public void setEliminar(String eliminar) {
		this.eliminar = eliminar;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public double getValorAceptado() {
		return valorAceptado;
	}

	public void setValorAceptado(double valorAceptado) {
		this.valorAceptado = valorAceptado;
	}

	public double getValorSoportado() {
		return valorSoportado;
	}

	public void setValorSoportado(double valorSoportado) {
		this.valorSoportado = valorSoportado;
	}

	public double getPorcentajeAceptado() {
		return porcentajeAceptado;
	}

	public void setPorcentajeAceptado(double porcentajeAceptado) {
		this.porcentajeAceptado = porcentajeAceptado;
	}

	public double getPorcentajeSoportado() {
		return porcentajeSoportado;
	}

	public void setPorcentajeSoportado(double porcentajeSoportado) {
		this.porcentajeSoportado = porcentajeSoportado;
	}

	public DtoFactura getFactura() {
		return factura;
	}

	public void setFactura(DtoFactura factura) {
		this.factura = factura;
	}

	public DtoGlosa getGlosa() {
		return glosa;
	}

	public void setGlosa(DtoGlosa glosa) {
		this.glosa = glosa;
	}

	public boolean isEstadoSeccionTotales() {
		return estadoSeccionTotales;
	}

	public void setEstadoSeccionTotales(boolean estadoSeccionTotales) {
		this.estadoSeccionTotales = estadoSeccionTotales;
	}

	public double getTotalFactura() {
		return totalFactura;
	}

	public void setTotalFactura(double totalFactura) {
		this.totalFactura = totalFactura;
	}

	public double getTotalResponsable() {
		return totalResponsable;
	}

	public void setTotalResponsable(double totalResponsable) {
		this.totalResponsable = totalResponsable;
	}

	public double getTotalBrutoPaciente() {
		return totalBrutoPaciente;
	}

	public void setTotalBrutoPaciente(double totalBrutoPaciente) {
		this.totalBrutoPaciente = totalBrutoPaciente;
	}

	public double getTotalDescuentos() {
		return totalDescuentos;
	}

	public void setTotalDescuentos(double totalDescuentos) {
		this.totalDescuentos = totalDescuentos;
	}

	public double getTotalAbonos() {
		return totalAbonos;
	}

	public void setTotalAbonos(double totalAbonos) {
		this.totalAbonos = totalAbonos;
	}

	public double getTotalNetoPaciente() {
		return totalNetoPaciente;
	}

	public void setTotalNetoPaciente(double totalNetoPaciente) {
		this.totalNetoPaciente = totalNetoPaciente;
	}
	
	public String getTotalFacturaStr() {
		return totalFacturaStr;
	}

	public void setTotalFacturaStr(String totalFacturaStr) {
		this.totalFacturaStr = totalFacturaStr;
	}

	public String getTotalResponsableStr() {
		return totalResponsableStr;
	}

	public void setTotalResponsableStr(String totalResponsableStr) {
		this.totalResponsableStr = totalResponsableStr;
	}

	public String getTotalBrutoPacienteStr() {
		return totalBrutoPacienteStr;
	}

	public void setTotalBrutoPacienteStr(String totalBrutoPacienteStr) {
		this.totalBrutoPacienteStr = totalBrutoPacienteStr;
	}

	public String getTotalDescuentosStr() {
		return totalDescuentosStr;
	}

	public void setTotalDescuentosStr(String totalDescuentosStr) {
		this.totalDescuentosStr = totalDescuentosStr;
	}

	public String getTotalAbonosStr() {
		return totalAbonosStr;
	}

	public void setTotalAbonosStr(String totalAbonosStr) {
		this.totalAbonosStr = totalAbonosStr;
	}

	public String getTotalNetoPacienteStr() {
		return totalNetoPacienteStr;
	}

	public void setTotalNetoPacienteStr(String totalNetoPacienteStr) {
		this.totalNetoPacienteStr = totalNetoPacienteStr;
	}

	/**
	 * @return the detalle
	 */
	public ArrayList<DtoDetalleFacturaGlosa> getDetalle() {
		return detalle;
	}

	/**
	 * @param detalle the detalle to set
	 */
	public void setDetalle(ArrayList<DtoDetalleFacturaGlosa> detalle) {
		this.detalle = detalle;
	}

	/**
	 * Constructor
	 */
	public DtoFacturaGlosa()
	{
		this.clean();
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codigoGlosa
	 */
	public String getCodigoGlosa() {
		return codigoGlosa;
	}

	/**
	 * @param codigoGlosa the codigoGlosa to set
	 */
	public void setCodigoGlosa(String codigoGlosa) {
		this.codigoGlosa = codigoGlosa;
	}

	/**
	 * @return the codigoFactura
	 */
	public String getCodigoFactura() {
		return codigoFactura;
	}

	/**
	 * @param codigoFactura the codigoFactura to set
	 */
	public void setCodigoFactura(String codigoFactura) {
		this.codigoFactura = codigoFactura;
	}

	/**
	 * @return the fechaModificacion
	 */
	public String getFechaModificacion() {
		return fechaModificacion;
	}

	/**
	 * @param fechaModificacion the fechaModificacion to set
	 */
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	/**
	 * @return the horaModificacion
	 */
	public String getHoraModificacion() {
		return horaModificacion;
	}

	/**
	 * @param horaModificacion the horaModificacion to set
	 */
	public void setHoraModificacion(String horaModificacion) {
		this.horaModificacion = horaModificacion;
	}

	/**
	 * @return the usuarioModificacion
	 */
	public UsuarioBasico getUsuarioModificacion() {
		return usuarioModificacion;
	}

	/**
	 * @param usuarioModificacion the usuarioModificacion to set
	 */
	public void setUsuarioModificacion(UsuarioBasico usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	/**
	 * @return the codigoContrato
	 */
	public int getCodigoContrato() {
		return codigoContrato;
	}

	/**
	 * @param codigoContrato the codigoContrato to set
	 */
	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	/**
	 * @return the valorGlosaFactura
	 */
	public double getValorGlosaFactura() {
		return valorGlosaFactura;
	}

	/**
	 * @param valorGlosaFactura the valorGlosaFactura to set
	 */
	public void setValorGlosaFactura(double valorGlosaFactura) {
		this.valorGlosaFactura = valorGlosaFactura;
	}

	/**
	 * @return the valorGlosaFacturaStr
	 */
	public String getValorGlosaFacturaStr() {
		return valorGlosaFacturaStr;
	}

	/**
	 * @param valorGlosaFacturaStr the valorGlosaFacturaStr to set
	 */
	public void setValorGlosaFacturaStr(String valorGlosaFacturaStr) {
		this.valorGlosaFacturaStr = valorGlosaFacturaStr;
	}

	/**
	 * @return the saldoFactura
	 */
	public double getSaldoFactura() {
		return saldoFactura;
	}

	/**
	 * @param saldoFactura the saldoFactura to set
	 */
	public void setSaldoFactura(double saldoFactura) {
		this.saldoFactura = saldoFactura;
	}

	/**
	 * @return the saldoFacturaStr
	 */
	public String getSaldoFacturaStr() {
		return saldoFacturaStr;
	}

	/**
	 * @param saldoFacturaStr the saldoFacturaStr to set
	 */
	public void setSaldoFacturaStr(String saldoFacturaStr) {
		this.saldoFacturaStr = saldoFacturaStr;
	}

	/**
	 * @return the fechaElaboracionFactura
	 */
	public String getFechaElaboracionFactura() {
		return fechaElaboracionFactura;
	}

	/**
	 * @param fechaElaboracionFactura the fechaElaboracionFactura to set
	 */
	public void setFechaElaboracionFactura(String fechaElaboracionFactura) {
		this.fechaElaboracionFactura = fechaElaboracionFactura;
	}

	/**
	 * @return the numeroCuentaCobro
	 */
	public String getNumeroCuentaCobro() {
		return numeroCuentaCobro;
	}

	/**
	 * @param numeroCuentaCobro the numeroCuentaCobro to set
	 */
	public void setNumeroCuentaCobro(String numeroCuentaCobro) {
		this.numeroCuentaCobro = numeroCuentaCobro;
	}

	/**
	 * @return the fechaRadicacion
	 */
	public String getFechaRadicacion() {
		return fechaRadicacion;
	}

	/**
	 * @param fechaRadicacion the fechaRadicacion to set
	 */
	public void setFechaRadicacion(String fechaRadicacion) {
		this.fechaRadicacion = fechaRadicacion;
	}

	/**
	 * @return the conceptos
	 */
	public ArrayList<DtoConceptoGlosa> getConceptos() {
		return conceptos;
	}

	/**
	 * @param conceptos the conceptos to set
	 */
	public void setConceptos(ArrayList<DtoConceptoGlosa> conceptos) {
		this.conceptos = conceptos;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @return the consecutivoFactura
	 */
	public String getConsecutivoFactura() {
		return consecutivoFactura;
	}

	/**
	 * @param consecutivoFactura the consecutivoFactura to set
	 */
	public void setConsecutivoFactura(String consecutivoFactura) {
		this.consecutivoFactura = consecutivoFactura;
	}

	public Boolean getChequeoDevolucion() {
		return chequeoDevolucion;
	}

	public void setChequeoDevolucion(Boolean chequeoDevolucion) {
		this.chequeoDevolucion = chequeoDevolucion;
	}
	
	
	/**
	 * Método para obtener el numero de registros del detalle de la factura
	 * @return
	 */
	public int getNumRegistrosDetalle()
	{
		
		return this.detalle.size();
	}
	
	/**
	 * Método para obtener el numero de registros del detalle de la factura
	 * @return
	 */
	public int getNumRegistrosDetalleSinEliminar()
	{
		int numRegistros = 0;
		
		for(DtoDetalleFacturaGlosa detalle:this.detalle)
			if(!detalle.isEliminado())
				numRegistros++;
		return numRegistros;
	}

	public int getPosicionDetalle() {
		return posicionDetalle;
	}

	public void setPosicionDetalle(int posicionDetalle) {
		this.posicionDetalle = posicionDetalle;
	}

	public int getPosicionConcepto() {
		return posicionConcepto;
	}

	public void setPosicionConcepto(int posicionConcepto) {
		this.posicionConcepto = posicionConcepto;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	
	/**
	 * Método para obtener los codigos de los detalles de la factura separados por coma
	 * @return
	 */
	public String obtenerCodigosDetalleFactura()
	{
		String codigos = "";
		
		for(DtoDetalleFacturaGlosa detalle:this.detalle)
			if(!detalle.isEliminado())
				codigos += (codigos.equals("")?"":",") + detalle.getCodigoDetalleFacturaSolicitud();
		
		return codigos;
	}

	public int getPosicionAsocio() {
		return posicionAsocio;
	}

	public void setPosicionAsocio(int posicionAsocio) {
		this.posicionAsocio = posicionAsocio;
	}
}
