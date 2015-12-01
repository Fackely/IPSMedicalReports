/*
 * Ene 06, 2008
 */
package com.princetonsa.dto.glosas;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Data Transfer Object: 
 * Usado para el manejo de las glosas
 * 
 * @author Sebastián Gómez R.
 *
 */
public class DtoGlosa implements Serializable, Comparable<DtoGlosa>
{
	private String codigo;
	private double valorGlosa;
	private String valorGlosaStr;
	private String glosaSistema;
	private String fechaRegistroGlosa;
	private InfoDatosInt convenio;
	private String glosaEntidad;
	private String fechaNotificacion;
	private String observaciones;
	private UsuarioBasico usuarioGlosa;
	private UsuarioBasico usuarioAuditor;
	private String fechaAuditoria;
	private String horaAuditoria;
	private String estado;
	private String fechaModificacion;
	private String horaModificacion;
	private UsuarioBasico usuarioModificacion;
	private int codigoInstitucion;
	private int codigoContrato;
	private String consecutivoContrato;
	
	//***********Indicativo para conocer si la glosa se encuentra en estado Glosa o Preglosa
	private String indicativoFueAuditada;
	
	//******************Atributos para el manejo de la vista de las glosas********************************
	private int posicionFactura; //Posicion actual de la factura seleccionada
	private ArrayList<HashMap<String, Object>> conceptos = new ArrayList<HashMap<String,Object>>();
	private DtoConceptoGlosa conceptoSeleccionado; //se almacena el concepto seleccionado
	private boolean devolucionActiva; //atributo para saber si se activa la devolucion
	private boolean esResumen; //para saber si la glosa es solo resumen o es editable
	
	//PARAMETROS BUSQUEDA DE SOLICITUDES
	private String numeroSolicitudBusqueda;
	private String codigoServicioBusqueda;
	private String descripcionServicioBusqueda;
	private String codigoArticuloBusqueda;
	private String descripcionArticuloBusqueda;
	private String tarifarioOficialBusqueda;
	private String seleccionTodosBusqueda;
	private HashMap<String, Object> busquedaSolicitudes = new HashMap<String, Object>();
	//*******************************************************************************************************
	
	
	private DtoRespuestaGlosa dtoRespuestaGlosa;
	private ArrayList<DtoRespuestaFacturaGlosa> respuestasFacturas;
	
	// **** Cadenas SQL Para la Impresion de reportes *****
	private String sqlImpresionDetalleRespuesta;
	//*****************************************************
	
	
	/**
	 * Arreglo donde se almacenan las facturas de una glosa
	 */
	private ArrayList<DtoFacturaGlosa> facturas;
	
	/**
	 * 
	 */
	private String keyOrdenar;
	
	/**
	 * 
	 */
	private int diasInResp;
	
	private String tipoConvenio;
	
	private String descripcionTConvenio;
	
	private String fechaAprobacion;
	
	/**
	 * Constructor
	 */
	public DtoGlosa()
	{
		this.clean();
	}
	
	/**
	 * Método que limpia los datos del dto
	 */
	public void clean()
	{
		this.codigo = "";
		this.glosaSistema = "";
		this.fechaRegistroGlosa = "";
		this.convenio = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.observaciones = "";
		this.fechaAuditoria = "";
		this.horaAuditoria = "";
		this.usuarioAuditor = new UsuarioBasico();
		this.usuarioGlosa = new UsuarioBasico();
		this.fechaModificacion = "";
		this.horaModificacion = "";
		this.usuarioModificacion = new UsuarioBasico();
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		this.estado = "";
		
		this.glosaEntidad = "";
		this.fechaNotificacion= "";
		
		this.valorGlosa = 0;
		this.valorGlosaStr = "";
		
		this.facturas = new ArrayList<DtoFacturaGlosa>();		
		this.posicionFactura = 0;
		this.conceptos = new ArrayList<HashMap<String,Object>>();
		this.conceptoSeleccionado = new DtoConceptoGlosa();
		this.devolucionActiva = false;
		this.esResumen = false;
		
		
		//PARÁMETROS BUSQUEDA SOLICITUDES
		this.numeroSolicitudBusqueda = "";
		this.codigoServicioBusqueda = "";
		this.descripcionServicioBusqueda = "";
		this.codigoArticuloBusqueda = "";
		this.descripcionArticuloBusqueda = "";
		this.tarifarioOficialBusqueda = "";
		this.seleccionTodosBusqueda = ConstantesBD.acronimoNo;
		this.busquedaSolicitudes = new HashMap<String, Object>();
				
		// RESPUESTA DE LA GLOSA
		this.dtoRespuestaGlosa = new DtoRespuestaGlosa();
		this.codigoContrato=ConstantesBD.codigoNuncaValido;
		this.consecutivoContrato="";
		
		this.dtoRespuestaGlosa = new DtoRespuestaGlosa();
		this.respuestasFacturas = new ArrayList<DtoRespuestaFacturaGlosa>();
		this.sqlImpresionDetalleRespuesta="";
		
		this.indicativoFueAuditada="";
		this.keyOrdenar="";
		
		this.diasInResp = ConstantesBD.codigoNuncaValido;
		this.tipoConvenio=ConstantesBD.codigoNuncaValido+"";
		this.descripcionTConvenio="";
		
		this.fechaAprobacion="";
	}

	public String getRespuestaConsecutivo (){
		return dtoRespuestaGlosa.getRespuestaConsecutivo();
	}
	
	public String getFechaRespuesta (){
		return dtoRespuestaGlosa.getFechaRespuesta();
	}
	
	public double getValorRespuesta (){
		return dtoRespuestaGlosa.getValorRespuesta();
	}
	
	public String getEstadoRespuesta (){
		return dtoRespuestaGlosa.getEstadoRespuesta();
	}
	
	public void setEstadoRespuesta(String estadoRespuesta){
		this.dtoRespuestaGlosa.setEstadoRespuesta(estadoRespuesta);
	}
	
	/**
	 * @return the facturas
	 */
	public ArrayList<DtoFacturaGlosa> getFacturas() {
		return facturas;
	}

	/**
	 * @param facturas the facturas to set
	 */
	public void setFacturas(ArrayList<DtoFacturaGlosa> facturas) {
		this.facturas = facturas;
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
	 * @return the fechaAuditoria
	 */
	public String getFechaAuditoria() {
		return fechaAuditoria;
	}

	/**
	 * @param fechaAuditoria the fechaAuditoria to set
	 */
	public void setFechaAuditoria(String fechaAuditoria) {
		this.fechaAuditoria = fechaAuditoria;
	}

	/**
	 * @return the horaAuditoria
	 */
	public String getHoraAuditoria() {
		return horaAuditoria;
	}

	/**
	 * @param horaAuditoria the horaAuditoria to set
	 */
	public void setHoraAuditoria(String horaAuditoria) {
		this.horaAuditoria = horaAuditoria;
	}

	/**
	 * @return the usuarioAuditor
	 */
	public UsuarioBasico getUsuarioAuditor() {
		return usuarioAuditor;
	}

	/**
	 * @param usuarioAuditor the usuarioAuditor to set
	 */
	public void setUsuarioAuditor(UsuarioBasico usuarioAuditor) {
		this.usuarioAuditor = usuarioAuditor;
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
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	


	/**
	 * @return the glosaEntidad
	 */
	public String getGlosaEntidad() {
		return glosaEntidad;
	}

	/**
	 * @param glosaEntidad the glosaEntidad to set
	 */
	public void setGlosaEntidad(String glosaEntidad) {
		this.glosaEntidad = glosaEntidad;
	}

	/**Sol.
	 * @return the fechaNotificacion
	 */
	public String getFechaNotificacion() {
		return fechaNotificacion;
	}

	/**
	 * @param fechaNotificacion the fechaNotificacion to set
	 */
	public void setFechaNotificacion(String fechaNotificacion) {
		this.fechaNotificacion = fechaNotificacion;
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
	 * @return the usuarioGlosa
	 */
	public UsuarioBasico getUsuarioGlosa() {
		return usuarioGlosa;
	}

	/**
	 * @param usuarioGlosa the usuarioGlosa to set
	 */
	public void setUsuarioGlosa(UsuarioBasico usuarioGlosa) {
		this.usuarioGlosa = usuarioGlosa;
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
	 * @return the glosaSistema
	 */
	public String getGlosaSistema() {
		return glosaSistema;
	}

	/**
	 * @param glosaSistema the glosaSistema to set
	 */
	public void setGlosaSistema(String glosaSistema) {
		this.glosaSistema = glosaSistema;
	}

	/**
	 * @return the fechaRegistroGlosa
	 */
	public String getFechaRegistroGlosa() {
		return fechaRegistroGlosa;
	}

	/**
	 * @param fechaRegistroGlosa the fechaRegistroGlosa to set
	 */
	public void setFechaRegistroGlosa(String fechaRegistroGlosa) {
		this.fechaRegistroGlosa = fechaRegistroGlosa;
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
	 * @return the convenio
	 */
	public int getCodigoConvenio() {
		return convenio.getCodigo();
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setCodigoConvenio(int convenio) {
		this.convenio.setCodigo(convenio);
	}
	
	/**
	 * @return the convenio
	 */
	public String getNombreConvenio() {
		return convenio.getNombre();
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setNombreConvenio(String convenio) {
		this.convenio.setNombre(convenio);
	}

	public int getPosicionFactura() {
		return posicionFactura;
	}

	public void setPosicionFactura(int posicionFactura) {
		this.posicionFactura = posicionFactura;
	}

	public ArrayList<HashMap<String, Object>> getConceptos() {
		return conceptos;
	}

	public void setConceptos(ArrayList<HashMap<String, Object>> conceptos) {
		this.conceptos = conceptos;
	}
	
	/**
	 * Método para saber cuantos conceptos hay parametrzados
	 * @return
	 */
	public int getNumConceptos()
	{
		return this.conceptos.size();
	}

	public DtoConceptoGlosa getConceptoSeleccionado() {
		return conceptoSeleccionado;
	}

	public void setConceptoSeleccionado(DtoConceptoGlosa conceptoSeleccionado) {
		this.conceptoSeleccionado = conceptoSeleccionado;
	}

	public boolean isDevolucionActiva() {
		return devolucionActiva;
	}

	public void setDevolucionActiva(boolean devolucionActiva) {
		this.devolucionActiva = devolucionActiva;
	}

	public String getNumeroSolicitudBusqueda() {
		return numeroSolicitudBusqueda;
	}

	public void setNumeroSolicitudBusqueda(String numeroSolicitudBusqueda) {
		this.numeroSolicitudBusqueda = numeroSolicitudBusqueda;
	}

	public String getCodigoServicioBusqueda() {
		return codigoServicioBusqueda;
	}

	public void setCodigoServicioBusqueda(String codigoServicioBusqueda) {
		this.codigoServicioBusqueda = codigoServicioBusqueda;
	}

	public String getDescripcionServicioBusqueda() {
		return descripcionServicioBusqueda;
	}

	public void setDescripcionServicioBusqueda(String descripcionServicioBusqueda) {
		this.descripcionServicioBusqueda = descripcionServicioBusqueda;
	}

	public String getCodigoArticuloBusqueda() {
		return codigoArticuloBusqueda;
	}

	public void setCodigoArticuloBusqueda(String codigoArticuloBusqueda) {
		this.codigoArticuloBusqueda = codigoArticuloBusqueda;
	}

	public String getDescripcionArticuloBusqueda() {
		return descripcionArticuloBusqueda;
	}

	public void setDescripcionArticuloBusqueda(String descripcionArticuloBusqueda) {
		this.descripcionArticuloBusqueda = descripcionArticuloBusqueda;
	}

	public HashMap<String, Object> getBusquedaSolicitudes() {
		return busquedaSolicitudes;
	}

	public void setBusquedaSolicitudes(HashMap<String, Object> busquedaSolicitudes) {
		this.busquedaSolicitudes = busquedaSolicitudes;
	}
	
	public Object getBusquedaSolicitudes(String key) {
		return busquedaSolicitudes.get(key);
	}

	public void setBusquedaSolicitudes(String key,Object obj) {
		this.busquedaSolicitudes.put(key,obj);
	}
	
	/**
	 * Método para obtener el numero de registros de la busqueda de solicitudes
	 * @return
	 */
	public int getNumBusquedaSolicitudes()
	{
		return Utilidades.convertirAEntero(this.getBusquedaSolicitudes("numRegistros").toString(), true);
	}
	
	/**
	 * Método para asignar el tamaño del numero de registros de la busqueda de solicitudes
	 * @param numRegistros
	 */
	public void setNumBusquedaSolicitudes(int numRegistros)
	{
		this.setBusquedaSolicitudes("numRegistros", numRegistros);
	}

	public String getTarifarioOficialBusqueda() {
		return tarifarioOficialBusqueda;
	}

	public void setTarifarioOficialBusqueda(String tarifarioOficialBusqueda) {
		this.tarifarioOficialBusqueda = tarifarioOficialBusqueda;
	}

	public String getSeleccionTodosBusqueda() {
		return seleccionTodosBusqueda;
	}

	public void setSeleccionTodosBusqueda(String seleccionTodosBusqueda) {
		this.seleccionTodosBusqueda = seleccionTodosBusqueda;
	}

	/**
	 * @return the esResumen
	 */
	public boolean isEsResumen() {
		return esResumen;
	}

	/**
	 * @param esResumen the esResumen to set
	 */
	public void setEsResumen(boolean esResumen) {
		this.esResumen = esResumen;
	}

	/**
	 * @return the dtoRespuestaGlosa
	 */
	public DtoRespuestaGlosa getDtoRespuestaGlosa() {
		return dtoRespuestaGlosa;
	}

	/**
	 * @param dtoRespuestaGlosa the dtoRespuestaGlosa to set
	 */
	public void setDtoRespuestaGlosa(DtoRespuestaGlosa dtoRespuestaGlosa) {
		this.dtoRespuestaGlosa = dtoRespuestaGlosa;
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
	 * @return the consecutivoContrato
	 */
	public String getConsecutivoContrato() {
		return consecutivoContrato;
	}

	/**
	 * @param consecutivoContrato the consecutivoContrato to set
	 */
	public void setConsecutivoContrato(String consecutivoContrato) {
		this.consecutivoContrato = consecutivoContrato;
	}

	/**
	 * @return the respuestasFacturas
	 */
	public ArrayList<DtoRespuestaFacturaGlosa> getRespuestasFacturas() {
		return respuestasFacturas;
	}
	
	/**
	 * @return the respuestasFacturas
	 */
	public DtoRespuestaFacturaGlosa getRespuestasFacturas(int posArray) {
		return respuestasFacturas.get(posArray);
	}

	/**
	 * @param respuestasFacturas the respuestasFacturas to set
	 */
	public void setRespuestasFacturas(
			ArrayList<DtoRespuestaFacturaGlosa> respuestasFacturas) {
		this.respuestasFacturas = respuestasFacturas;
	}

	/**
	 * @return the sqlImpresionDetalleRespuesta
	 */
	public String getSqlImpresionDetalleRespuesta() {
		return sqlImpresionDetalleRespuesta;
	}

	/**
	 * @param sqlImpresionDetalleRespuesta the sqlImpresionDetalleRespuesta to set
	 */
	public void setSqlImpresionDetalleRespuesta(String sqlImpresionDetalleRespuesta) {
		this.sqlImpresionDetalleRespuesta = sqlImpresionDetalleRespuesta;
	}

	/**
	 * @return the indicativoFueAuditada
	 */
	public String getIndicativoFueAuditada() {
		return indicativoFueAuditada;
	}

	/**
	 * @param indicativoFueAuditada the indicativoFueAuditada to set
	 */
	public void setIndicativoFueAuditada(String indicativoFueAuditada) {
		this.indicativoFueAuditada = indicativoFueAuditada;
	}

	@Override
	public int compareTo(DtoGlosa o) 
	{
		if(UtilidadTexto.isEmpty(this.keyOrdenar) || this.keyOrdenar.equals("convenio_"))
			return this.getNombreConvenio().compareTo(o.getNombreConvenio());
		else if(this.keyOrdenar.equals("contrato_"))
			return this.getConsecutivoContrato().compareTo(o.getConsecutivoContrato());
		else if(this.keyOrdenar.equals("glosaentidad_"))
			return this.getGlosaEntidad().compareTo(o.getGlosaEntidad());
		else if(this.keyOrdenar.equals("fechanoti_"))
			return this.getFechaNotificacion().compareTo(o.getFechaNotificacion());
		else if(this.keyOrdenar.equals("glosasistema_"))
			return this.getGlosaSistema().compareTo(o.getGlosaSistema());
		else if(this.keyOrdenar.equals("indicativofueauditada_"))
			return this.getIndicativoFueAuditada().compareTo(o.getIndicativoFueAuditada());
		else if(this.keyOrdenar.equals("estadoglosa_"))
			return this.getEstado().compareTo(o.getEstado());
		else if(this.keyOrdenar.equals("valorglosa_"))
			return new Double(this.getValorGlosa()).compareTo(new Double(o.getValorGlosa()));
		else if(this.keyOrdenar.equals("respuesta_"))
			return this.getDtoRespuestaGlosa().getRespuestaConsecutivo().compareTo(o.getDtoRespuestaGlosa().getRespuestaConsecutivo());
		else if(this.keyOrdenar.equals("diassinrta_"))
			return new Integer(UtilidadFecha.numeroDiasEntreFechas(UtilidadFecha.conversionFormatoFechaAAp(this.getFechaNotificacion()),UtilidadFecha.getFechaActual())).compareTo(new Integer(UtilidadFecha.numeroDiasEntreFechas(UtilidadFecha.conversionFormatoFechaAAp(o.getFechaNotificacion()),UtilidadFecha.getFechaActual())));
		
		else if(this.keyOrdenar.equals("convenio_contrario"))
			return o.getNombreConvenio().compareTo(this.getNombreConvenio());
		else if(this.keyOrdenar.equals("contrato_contrario"))
			return o.getConsecutivoContrato().compareTo(this.getConsecutivoContrato());
		else if(this.keyOrdenar.equals("glosaentidad_contrario"))
			return o.getGlosaEntidad().compareTo(this.getGlosaEntidad());
		else if(this.keyOrdenar.equals("fechanoti_contrario"))
			return o.getFechaNotificacion().compareTo(this.getFechaNotificacion());
		else if(this.keyOrdenar.equals("glosasistema_contrario"))
			return o.getGlosaSistema().compareTo(this.getGlosaSistema());
		else if(this.keyOrdenar.equals("indicativofueauditada_contrario"))
			return o.getIndicativoFueAuditada().compareTo(this.getIndicativoFueAuditada());
		else if(this.keyOrdenar.equals("estadoglosa_contrario"))
			return o.getEstado().compareTo(this.getEstado());
		else if(this.keyOrdenar.equals("valorglosa_contrario"))
			return new Double(o.getValorGlosa()).compareTo(new Double(this.getValorGlosa()));
		else if(this.keyOrdenar.equals("respuesta_contrario"))
			return o.getDtoRespuestaGlosa().getRespuestaConsecutivo().compareTo(this.getDtoRespuestaGlosa().getRespuestaConsecutivo());
		else if(this.keyOrdenar.equals("diassinrta_contrario"))
			return new Integer(UtilidadFecha.numeroDiasEntreFechas(UtilidadFecha.conversionFormatoFechaAAp(o.getFechaNotificacion()),UtilidadFecha.getFechaActual())).compareTo(new Integer(UtilidadFecha.numeroDiasEntreFechas(UtilidadFecha.conversionFormatoFechaAAp(this.getFechaNotificacion()),UtilidadFecha.getFechaActual())));
		else
			return this.getNombreConvenio().compareTo(o.getNombreConvenio());
	}

	/**
	 * @return the keyOrdenar
	 */
	public String getKeyOrdenar() {
		return keyOrdenar;
	}

	/**
	 * @param keyOrdenar the keyOrdenar to set
	 */
	public void setKeyOrdenar(String keyOrdenar) {
		this.keyOrdenar = keyOrdenar;
	}

	/**
	 * @return the diasInResp
	 */
	public int getDiasInResp() {
		return diasInResp;
	}

	/**
	 * @param diasInResp the diasInResp to set
	 */
	public void setDiasInResp(int diasInResp) {
		this.diasInResp = diasInResp;
	}

	public String getDescripcionTConvenio() {
		return descripcionTConvenio;
	}

	public void setDescripcionTConvenio(String descripcionTConvenio) {
		this.descripcionTConvenio = descripcionTConvenio;
	}

	/**
	 * @return the fechaAprobacion
	 */
	public String getFechaAprobacion() {
		return fechaAprobacion;
	}

	/**
	 * @param fechaAprobacion the fechaAprobacion to set
	 */
	public void setFechaAprobacion(String fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}

	public String getTipoConvenio() {
		return tipoConvenio;
	}

	public void setTipoConvenio(String tipoConvenio) {
		this.tipoConvenio = tipoConvenio;
	}
	
	
	
}
