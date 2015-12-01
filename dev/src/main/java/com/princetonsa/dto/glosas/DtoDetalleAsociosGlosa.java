/*
 * Ene 06, 2008
 */
package com.princetonsa.dto.glosas;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.Utilidades;

/**
 * Data Transfer Object: 
 * Usado para el manejo del detalle de asocios de una glosa
 * 
 * @author Sebastián Gómez R. - Felipe Pérez Granda
 *
 */
public class DtoDetalleAsociosGlosa implements Serializable
{
	private String codigo;
	private String codigoAsocioDetalleFactura;
	private String codigoDetalleGlosa;
	private String tipoAsocio;
	private InfoDatosInt asocio;
	private InfoDatosInt profesional;
	private Integer codigoServicioAsocio;
	private String codigoPropietarioServicioAsocio;
	private Integer cantidad;
	private double valor;
	private String valorStr;
	private Integer cantidadGlosa;
	private double valorGlosa;
	private String valorGlosaStr;
	private String servicioAsocio;
	private DtoDetalleFacturaGlosa detalleFacturaGlosa= new DtoDetalleFacturaGlosa();
	private String codigoMedico;
	private String poolAsocio;
	private String consecutivoAsoDetFact;
	private String porcentajePool;
	private String motivo;
	private DtoConceptoRespuesta conceptoResp;
	private DtoConceptoGlosa conceptoGlosa;
	
	private ArrayList<DtoConceptoGlosa> conceptos;
	
	/**
	 * Constructor
	 */
	public DtoDetalleAsociosGlosa()
	{
		this.clean();
	}
	
	/**
	 * Método que limpia los datos del DTO
	 */
	public void clean()
	{
		this.codigo = "";
		this.codigoAsocioDetalleFactura = "";
		this.codigoDetalleGlosa = "";
		this.asocio = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.profesional = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.codigoServicioAsocio = null;
		this.codigoPropietarioServicioAsocio = "";
		this.cantidad = null;
		this.valor = 0;
		this.valorStr = "";
		this.cantidadGlosa = null;
		this.valorGlosa = 0;
		this.valorGlosaStr = "";
		this.tipoAsocio="";
		this.servicioAsocio="";
		this.detalleFacturaGlosa= new DtoDetalleFacturaGlosa();
		this.consecutivoAsoDetFact="";
		this.codigoMedico="";
		this.poolAsocio="";
		this.porcentajePool="";
		this.conceptos = new ArrayList<DtoConceptoGlosa>();
		this.motivo="";
		this.conceptoGlosa= new DtoConceptoGlosa();
		this.conceptoResp= new DtoConceptoRespuesta();
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public DtoConceptoRespuesta getConceptoResp() {
		return conceptoResp;
	}

	public void setConceptoResp(DtoConceptoRespuesta conceptoResp) {
		this.conceptoResp = conceptoResp;
	}

	public DtoConceptoGlosa getConceptoGlosa() {
		return conceptoGlosa;
	}

	public void setConceptoGlosa(DtoConceptoGlosa conceptoGlosa) {
		this.conceptoGlosa = conceptoGlosa;
	}

	public String getPorcentajePool() {
		return porcentajePool;
	}

	public void setPorcentajePool(String porcentajePool) {
		this.porcentajePool = porcentajePool;
	}

	public String getConsecutivoAsoDetFact() {
		return consecutivoAsoDetFact;
	}

	public void setConsecutivoAsoDetFact(String consecutivoAsoDetFact) {
		this.consecutivoAsoDetFact = consecutivoAsoDetFact;
	}

	public String getServicioAsocio() {
		return servicioAsocio;
	}

	public void setServicioAsocio(String servicioAsocio) {
		this.servicioAsocio = servicioAsocio;
	}

	public DtoDetalleFacturaGlosa getDetalleFacturaGlosa() {
		return detalleFacturaGlosa;
	}

	public void setDetalleFacturaGlosa(DtoDetalleFacturaGlosa detalleFacturaGlosa) {
		this.detalleFacturaGlosa = detalleFacturaGlosa;
	}

	public String getCodigoMedico() {
		return codigoMedico;
	}

	public void setCodigoMedico(String codigoMedico) {
		this.codigoMedico = codigoMedico;
	}

	public String getPoolAsocio() {
		return poolAsocio;
	}

	public void setPoolAsocio(String poolAsocio) {
		this.poolAsocio = poolAsocio;
	}

	public String getTipoAsocio() {
		return tipoAsocio;
	}

	public void setTipoAsocio(String tipoAsocio) {
		this.tipoAsocio = tipoAsocio;
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
	 * @return the codigoAsocioDetalleFactura
	 */
	public String getCodigoAsocioDetalleFactura() {
		return codigoAsocioDetalleFactura;
	}

	/**
	 * @param codigoAsocioDetalleFactura the codigoAsocioDetalleFactura to set
	 */
	public void setCodigoAsocioDetalleFactura(String codigoAsocioDetalleFactura) {
		this.codigoAsocioDetalleFactura = codigoAsocioDetalleFactura;
	}

	/**
	 * @return the codigoDetalleGlosa
	 */
	public String getCodigoDetalleGlosa() {
		return codigoDetalleGlosa;
	}

	/**
	 * @param codigoDetalleGlosa the codigoDetalleGlosa to set
	 */
	public void setCodigoDetalleGlosa(String codigoDetalleGlosa) {
		this.codigoDetalleGlosa = codigoDetalleGlosa;
	}

	/**
	 * @return the asocio
	 */
	public InfoDatosInt getAsocio() {
		return asocio;
	}

	/**
	 * @param asocio the asocio to set
	 */
	public void setAsocio(InfoDatosInt asocio) {
		this.asocio = asocio;
	}
	
	/**
	 * @return the asocio
	 */
	public int getCodigoAsocio() {
		return asocio.getCodigo();
	}

	/**
	 * @param asocio the asocio to set
	 */
	public void setCodigoAsocio(int asocio) {
		this.asocio.setCodigo(asocio);
	}
	
	/**
	 * @return the asocio
	 */
	public String getNombreAsocio() {
		return asocio.getNombre();
	}

	/**
	 * @param asocio the asocio to set
	 */
	public void setNombreAsocio(String asocio) {
		this.asocio.setNombre(asocio);
	}

	/**
	 * @return the profesional
	 */
	public InfoDatosInt getProfesional() {
		return profesional;
	}

	/**
	 * @param profesional the profesional to set
	 */
	public void setProfesional(InfoDatosInt profesional) {
		this.profesional = profesional;
	}
	
	/**
	 * @return the profesional
	 */
	public int getCodigoProfesional() {
		return profesional.getCodigo();
	}

	/**
	 * @param profesional the profesional to set
	 */
	public void setCodigoProfesional(int profesional) {
		this.profesional.setCodigo(profesional);
	}
	
	/**
	 * @return the profesional
	 */
	public String getNombreProfesional() {
		return profesional.getNombre();
	}

	/**
	 * @param profesional the profesional to set
	 */
	public void setNombreProfesional(String profesional) {
		this.profesional.setNombre(profesional);
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
	 * @return the cantidadGlosa
	 */
	public Integer getCantidadGlosa() {
		return cantidadGlosa;
	}

	/**
	 * @param cantidadGlosa the cantidadGlosa to set
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

	/**
	 * @return the codigoServicioAsocio
	 */
	public Integer getCodigoServicioAsocio() {
		return codigoServicioAsocio;
	}

	/**
	 * @param codigoServicioAsocio the codigoServicioAsocio to set
	 */
	public void setCodigoServicioAsocio(Integer codigoServicioAsocio) {
		this.codigoServicioAsocio = codigoServicioAsocio;
	}

	/**
	 * @return the codigoPropietarioServicioAsocio
	 */
	public String getCodigoPropietarioServicioAsocio() {
		return codigoPropietarioServicioAsocio;
	}

	/**
	 * @param codigoPropietarioServicioAsocio the codigoPropietarioServicioAsocio to set
	 */
	public void setCodigoPropietarioServicioAsocio(
			String codigoPropietarioServicioAsocio) {
		this.codigoPropietarioServicioAsocio = codigoPropietarioServicioAsocio;
	}
	
	/**
	 * Método para saber si un detalle de asocios tiene conceptos ya almacenados en la base de datos
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
	 * Método para saber cual es el tipo concepto que se debe aplicar para
	 * seleccionar los tipos de concepto por detalle
	 * @return
	 */
	public String getObtenerTipoConceptoAplicable()
	{
		String tipo = "";
		
		for(DtoConceptoGlosa concepto:this.conceptos)
			if(!concepto.isEliminado())
				tipo=concepto.getTipo();
		
		return tipo;
	}
	
	/**
	 * Método para saber cuantos conceptos definitivos tiene el asocio
	 * @return
	 */
	public int getNumConceptosDefinitivos()
	{
		int numConceptos = 0;
		for(DtoConceptoGlosa concepto:this.conceptos)
			if(!concepto.isEliminado())
				numConceptos++;
		return numConceptos;
	}
	
	
}
