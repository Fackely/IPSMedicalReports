/*
 * Ene 06, 2008
 */
package com.princetonsa.dto.glosas;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.mundo.glosas.Glosas;


import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;

/**
 * Data Transfer Object: 
 * Usado para el manejo del detalle de una glosa
 * 
 * @author Sebastián Gómez R. - Felipe Pérez Grabda
 *
 */
public class DtoDetalleFacturaGlosa implements Serializable
{
	/* *******************
	 * Atributos de Logger
	 * *******************/
	public static Logger logger = Logger.getLogger(DtoDetalleFacturaGlosa.class);
	
	private String codigo;
	private String codigoGlosa;
	private String codigoAuditoria;
	private String pool;
	private String codigoMedico;
	private String esCirugia;
	private String codigoDetalleFacturaSolicitud;
	private String numeroSolicitud;
	private String consecutivoOrden;
	private InfoDatosString servicioArticulo;
	private InfoDatosInt tipoSolicitud;
	private boolean esServicio;
	private Integer cantidad;
	private double valor;
	private String valorStr;
	private Integer cantidadGlosa;
	private double valorGlosa;
	private String valorGlosaStr;
	private boolean esArticulo;
	private DtoFactura factura= new DtoFactura();
	private String ajuste;
	private String centroCosto;
	
	//Arreglo donde se registran los conceptos de un detalle de la glosa
	private ArrayList<DtoConceptoGlosa> conceptos;
	private DtoConceptoGlosa concepto;
	
	//Arreglo donde se registran los asocios de un detalle de la glosa
	private ArrayList<DtoDetalleAsociosGlosa> asocios;
	
	//Atributos para el control de los procesos de un detalle*************
	private boolean eliminado;
	private String motivo;
	
	/**
	 * Constructor
	 */
	public DtoDetalleFacturaGlosa()
	{
		this.clean();
	}
	
	
	/**
	 * Método que limpia los datos del detalle de la glosa
	 */
	public void clean()
	{
		this.codigo = "";
		this.codigoGlosa = "";
		this.codigoAuditoria="";
		this.codigoDetalleFacturaSolicitud = "";
		this.pool="";
		this.codigoMedico="";
		this.esCirugia="";
		this.numeroSolicitud = "";
		this.consecutivoOrden = "";
		this.servicioArticulo = new InfoDatosString();
		this.tipoSolicitud = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.servicioArticulo.setCodigo(""); //codigo del servicio o artículo
		this.servicioArticulo.setDescripcion(""); //descripcion del servicio o artículo
		this.esServicio = false;
		this.esArticulo = false;
		this.cantidad = null;
		this.valor = 0;
		this.valorStr = "";
		this.cantidadGlosa = null;
		this.valorGlosa = 0;
		this.valorGlosaStr = "";
		this.concepto = new DtoConceptoGlosa();
		this.conceptos = new ArrayList<DtoConceptoGlosa>();
		this.asocios = new ArrayList<DtoDetalleAsociosGlosa>();
		this.factura= new DtoFactura();
		this.eliminado = false;
		this.ajuste="";
		this.motivo="";
		this.centroCosto="";
	}


	public String getCentroCosto() {
		return centroCosto;
	}


	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}


	public String getMotivo() {
		return motivo;
	}


	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}


	public String getAjuste() {
		return ajuste;
	}


	public void setAjuste(String ajuste) {
		this.ajuste = ajuste;
	}


	public String getPool() {
		return pool;
	}


	public void setPool(String pool) {
		this.pool = pool;
	}


	public String getCodigoMedico() {
		return codigoMedico;
	}


	public void setCodigoMedico(String codigoMedico) {
		this.codigoMedico = codigoMedico;
	}


	public String getEsCirugia() {
		return esCirugia;
	}


	public void setEsCirugia(String esCirugia) {
		this.esCirugia = esCirugia;
	}


	public DtoFactura getFactura() {
		return factura;
	}


	public void setFactura(DtoFactura factura) {
		this.factura = factura;
	}


	public String getCodigoAuditoria() {
		return codigoAuditoria;
	}


	public void setCodigoAuditoria(String codigoAuditoria) {
		this.codigoAuditoria = codigoAuditoria;
	}


	public DtoConceptoGlosa getConcepto() {
		return concepto;
	}


	public void setConcepto(DtoConceptoGlosa concepto) {
		this.concepto = concepto;
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
	 * @return the codigoDetalleFacturaSolicitud
	 */
	public String getCodigoDetalleFacturaSolicitud() {
		return codigoDetalleFacturaSolicitud;
	}


	/**
	 * @param codigoDetalleFacturaSolicitud the codigoDetalleFacturaSolicitud to set
	 */
	public void setCodigoDetalleFacturaSolicitud(
			String codigoDetalleFacturaSolicitud) {
		this.codigoDetalleFacturaSolicitud = codigoDetalleFacturaSolicitud;
	}


	/**
	 * @return the numeroSolicitud
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}


	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}


	/**
	 * @return the consecutivoOrden
	 */
	public String getConsecutivoOrden() {
		return consecutivoOrden;
	}


	/**
	 * @param consecutivoOrden the consecutivoOrden to set
	 */
	public void setConsecutivoOrden(String consecutivoOrden) {
		this.consecutivoOrden = consecutivoOrden;
	}

	
	/**
	 * @return the servicioArticulo
	 */
	public InfoDatosString getServicioArticulo() {
		return servicioArticulo;
	}
	
	/**
	 * @param servicioArticulo the servicioArticulo to set
	 */
	public void setServicioArticulo(InfoDatosString servicioArticulo) {
		this.servicioArticulo = servicioArticulo;
	}


	/**
	 * @param servicioArticulo the servicioArticulo to set
	 */
	public void setCodigoServicioArticulo(String servicioArticulo) {
		this.servicioArticulo.setCodigo( servicioArticulo);
	}
	
	/**
	 * @return the servicioArticulo
	 */
	public String getCodigoServicioArticulo() {
		return servicioArticulo.getCodigo();
	}
	
	/**
	 * @param servicioArticulo the servicioArticulo to set
	 */
	public void setDescripcionServicioArticulo(String servicioArticulo) {
		this.servicioArticulo.setDescripcion( servicioArticulo);
	}
	
	/**
	 * @return the servicioArticulo
	 */
	public String getDescripcionServicioArticulo() {
		return servicioArticulo.getDescripcion();
	}


	


	/**
	 * @return the esServicio
	 */
	public boolean isEsServicio() {
		return esServicio;
	}


	/**
	 * @param esServicio the esServicio to set
	 */
	public void setEsServicio(boolean esServicio) {
		this.esServicio = esServicio;
	}


	/**
	 * @return the cantidad
	 */
	public Integer getCantidad() {
		return cantidad;
	}


	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}


	/**
	 * @return the valor
	 */
	public double getValor() {
		return valor;
	}


	/**
	 * @param valor the valor to set
	 */
	public void setValor(double valor) {
		this.valor = valor;
	}


	/**
	 * @return the valorStr
	 */
	public String getValorStr() {
		return valorStr;
	}


	/**
	 * @param valorStr the valorStr to set
	 */
	public void setValorStr(String valorStr) {
		this.valorStr = valorStr;
	}


	/**
	 * @return the cantidadPreglosa
	 */
	public Integer getCantidadGlosa() {
		return cantidadGlosa;
	}


	/**
	 * @param cantidadPreglosa the cantidadPreglosa to set
	 */
	public void setCantidadGlosa(Integer cantidadGlosa) {
		this.cantidadGlosa = cantidadGlosa;
	}


	


	/**
	 * @return the valorGlosa
	 */
	public double getValorGlosa() {
		return valorGlosa;
	}


	/**
	 * @param valorGlosa the valorGlosa to set
	 */
	public void setValorGlosa(double valorGlosa) {
		this.valorGlosa = valorGlosa;
	}


	/**
	 * @return the valorGlosaStr
	 */
	public String getValorGlosaStr() {
		return valorGlosaStr;
	}


	/**
	 * @param valorGlosaStr the valorGlosaStr to set
	 */
	public void setValorGlosaStr(String valorGlosaStr) {
		this.valorGlosaStr = valorGlosaStr;
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


	public ArrayList<DtoDetalleAsociosGlosa> getAsocios() {
		return asocios;
	}


	public void setAsocios(ArrayList<DtoDetalleAsociosGlosa> asocios) {
		this.asocios = asocios;
	}


	public boolean isEliminado() {
		return eliminado;
	}


	public void setEliminado(boolean eliminado) {
		this.eliminado = eliminado;
	}
	
	/**
	 * Método para saber si un detalle tiene conceptos ya almacenados en la base de datos
	 * @return
	 */
	public boolean tieneConceptosEnBD()
	{
		boolean tieneConceptosBD = false;
		
		for(DtoConceptoGlosa concepto:this.conceptos)
			//Si ya existe un codigo de secuencia almacenado del concepto quiere decir que ya está registrado en base de datos
			if(!concepto.getCodigo().equals(""))
				tieneConceptosBD = true;
		
		return tieneConceptosBD;
	}
	
	/**
	 * Método para verificar si un detalle de factura de glosa tiene asocios en la base de datos relacionado
	 * @return
	 */
	public boolean tieneAsociosEnBD()
	{
		boolean tieneAsociosBD = false;
		
		for(DtoDetalleAsociosGlosa asocio:this.asocios)
			//Si ya existe un codigo de secuencia almacenado del asocio quiere decir que ya está registrado en base de datos
			if(!asocio.getCodigo().equals(""))
				tieneAsociosBD = true;
		
		return tieneAsociosBD;
	}
	
	/**
	 * Método para saber cual es el tipo concepto que se debe aplicar para
	 * seleccionar los tipos de concepto por detalle
	 * @return
	 */
	public String getObtenerTipoConceptoAplicable()
	{
		String tipo = "";
		
		for(DtoConceptoGlosa concepto:this.conceptos)
		{
			logger.info("concepto: "+concepto.getDescripcion());
			logger.info("eliminado? : "+concepto.isEliminado());
			if(!concepto.isEliminado())
				tipo=concepto.getTipo();
		}
		logger.info("El tipo encontrado fue: "+tipo);
		return tipo;
	}


	public InfoDatosInt getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(InfoDatosInt tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	public void setCodigoTipoSolicitud(int tipoSolicitud) {
		this.tipoSolicitud.setCodigo(tipoSolicitud);
	}
	
	public int getCodigoTipoSolicitud() {
		return tipoSolicitud.getCodigo();
	}
	
	public void setNombreTipoSolicitud(String nombreTipoSolicitud)
	{
		this.tipoSolicitud.setNombre(nombreTipoSolicitud);
	}
	
	public String getNombreTipoSolicitud()
	{
		return this.tipoSolicitud.getNombre();
	}
	
	/**
	 * Método para saber cuantos asocios tiene el detalle
	 * @return
	 */
	public int getNumRegistrosAsocios()
	{
		return this.asocios.size();
	}

	/**
	 * @return the esArticulo
	 */
	public boolean isEsArticulo() {
		return esArticulo;
	}

	/**
	 * @param esArticulo the esArticulo to set
	 */
	public void setEsArticulo(boolean esArticulo) {
		this.esArticulo = esArticulo;
	}	
}
