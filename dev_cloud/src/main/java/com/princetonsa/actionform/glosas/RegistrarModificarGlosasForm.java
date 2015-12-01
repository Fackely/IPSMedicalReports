package com.princetonsa.actionform.glosas;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;
import util.UtilidadFecha;

import com.princetonsa.dto.glosas.DtoGlosa;
import com.princetonsa.dto.glosas.DtoObsFacturaGlosas;

/**
 * 
 * @author Angela María Angel amangel@princetonsa.com
 *
 */

public class RegistrarModificarGlosasForm extends ValidatorForm
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(RegistrarModificarGlosasForm.class);
	
	/**
	 * Variable para manejo de Estado
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Variable para almacenar los Convenios
	 */
	private HashMap conveniosMap;
	
	/**
	 * Variable para almacenar el codigo del convenio
	 */
	private String convenio;
	
	/**
	 * Variable para almacenar el codigo del convenio Principal
	 */
	private String convenioP;
	
	/**
	 * Variable que almacena el numero de mapas en el arraylist
	 */
	private int numMapas;
	
	/**
	 * Variable que almacena el numero de mapas en el arraylist Principal
	 */
	private int numMapasP;
	
	/**
	 * Arreglo para almacenar los Convenios
	 */
	private ArrayList<HashMap<String, Object>> arregloConvenios = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Arreglo para almacenar los Convenios de la Pagina Principal
	 */
	private ArrayList<HashMap<String, Object>> arregloConveniosPrincipal = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Mapa para los valores de la consulta de la GLosa
	 */
	private HashMap informacionGlosa;
	
	/**
	 * Variable para modificar la glosa entidad del registro de la Busqueda
	 */
	private String glosaEntidad;
	
	/**
	 * Variable para modificar la fecha notificacion del registro de la Busqueda
	 */
	private String fechaNotificacion;
	
	/**
	 * Variable para modificar el valor del registro de la Busqueda
	 */
	private String valor;
	
	private String valorF;
	
	/**
	 * Variable para modificar las observaciones de la Busqueda
	 */
	private String observaciones;

	/**
	 * Variable para el manejo de la fecha registro de la Glosa
	 */
	private String fechaRegistro;
	
	/**
	 * Variable para validar actualizacion de Glosa Insert o Update
	 */
	private String banderaGuardar;
	
	/**
	 * Mapa para el manejo del detalle de la Glosa
	 */
	private HashMap mapaDetalleGlosa = new HashMap();
	
	/**
	 * Variable para almacenar la factura a buscar
	 */
	private String facturaABuscar;
	
	/**
	 * Variable para almacenar el codigo de la factura
	 */
	private String codFactura;
	
	/**
	 * Mapa donde se almacena el historico de la Factura
	 */
	private HashMap historicoMap;
	
	/**
	 * Mapa para almacenar unica Factura
	 */
	private HashMap unicaFacturaMap;
	
	/**
	 * Variable que almacena la posicion a eliminar del mapa detalle Glosa
	 */
	private String posicion = "";
	
	/**
	 * Mapa para almacenar el detalle de la Factura
	 */
	private HashMap consultaDetFacMap = new HashMap();
	
	/**
	 * Variable para almacenar el saldo de la factura
	 */
	private String saldofact;
	
	/**
	 * Arreglo para almacenar los Contratos
	 */
	private ArrayList<HashMap<String, Object>> arregloContratos = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Variable para consultar contratos
	 */	
	private int codigoContrato;
	
	/**
	 * Mapa para almacenar todos los contratos por factura del detalle glosa
	 */
	private HashMap contratosGlosaMap;
	
	/**
	 * Mapa para iterar los conceptos de una Factura Glosa y sus detalles
	 */
	private HashMap conceptosDetalleFacturaMap;
	
	/**
	 * Mapa que almacena todos los conceptos parametrizados
	 */
	private HashMap conceptosParamMap;
	
	/**
	 * Variable pára el concepto que selecciono
	 */
	private String conceptoSel;
	
	/**
	 * Variable para almacenar la posicion de la factura que se selecciono
	 */
	private String posicionFac;
	
	/**
	 * Variable para almacenar el consecutivo factura seleccionado
	 */
	private String factura;
	
	/**
	 * Variable para validar si se muestra el link detalle factura o no
	 */
	private boolean mostrarLink;

	/**
	 * Variable para almacenar el codigo del convenio
	 */
	private String indicativoglosa;
	
	/**
	 * Variable para almacenar el codigo del conveio seleccionado
	 */
	private String codConvenio;
	
	private HashMap arregloConvMap;
	
	private String codigoFac;
	
    /**
     * Almacena los diagnosticos adicionados
     * */
    private String conceptosAdd [];
	
	//################## NUEVO INGRESAR DETALLE GLOSA POR FACTURA ##########################//

	/**
	 * VAriables para el manejo del ordenamiento y paginacion
	 */
	private String linkSiguiente;
	
	/**
	 * Link Siguiente para el pager pagina Solicitudes
	 */
	private String linkSiguienteSolicitudes;
	
	/**
	 * Link Siguiente para el pager pagina Asocios
	 */
	private String linkSiguienteAsocios;
	
	
	private String indice;
	private String ultimoIndice;
	private int maxPageItems;
	private String patronOrdenar;
	
	/**
	 * Arreglo que contiene los conceptos de devolucion parametrizados por institucion
	 */
	private ArrayList<HashMap<String, Object>> conceptosDevolucion = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Mapa que maneja el listado de las facturas
	 */
	private HashMap<String, Object> listadoFacturas = new HashMap<String, Object>();
	
	/**
	 * Atributos que almacena el consecutivo de la glosa
	 */
	private String codigoAuditoria;
	
	/**
	 * Atributo DTO que maneja toda la informacion de la glosa
	 */
	private DtoGlosa glosa;
	
	private float sumatoriaValorGlosa;
	
	
	/**
	 * 
	 */
	private String observacionesDetalle;
	
	/**
	 * 
	 */
	private ArrayList<DtoObsFacturaGlosas> observacionesFacturaGlosas;
	
	//##############################FIN#####################################################//
	
	/**
	 * Resetea los valores de la forma cuando
	 * se ingresa por la funcionalidad por Area
	 * @param codigoInstitucion
	 */
	public void reset(int codigoInstitucion)
	{
		this.estado="";
		this.convenio="";
		this.convenioP="";
		this.conveniosMap = new HashMap();
		this.conveniosMap.put("numRegistros", "0");
		this.arregloConvenios = new ArrayList<HashMap<String,Object>>();
		this.arregloConveniosPrincipal = new ArrayList<HashMap<String,Object>>();
		this.informacionGlosa=new HashMap();
		this.informacionGlosa.put("numRegistros", "0");
		this.numMapas=0;
		this.numMapasP=0;
		this.glosaEntidad="";
		this.fechaNotificacion="";
		this.valor="";
		this.observaciones="";	
		this.fechaRegistro=UtilidadFecha.getFechaActual();
		this.banderaGuardar="";
		this.mapaDetalleGlosa=new HashMap();
		this.mapaDetalleGlosa.put("numRegistros", "0");
		this.facturaABuscar="";
		this.codFactura="";
		this.historicoMap=new HashMap();
		this.historicoMap.put("numRegistros", "0");
		this.unicaFacturaMap=new HashMap();
		this.unicaFacturaMap.put("numRegistros", "0");
		this.posicion="";
		this.consultaDetFacMap= new HashMap();
		this.consultaDetFacMap.put("numRegistros", "0");
		this.saldofact="";
		this.valorF="";
		this.arregloContratos = new ArrayList<HashMap<String,Object>>();
		this.codigoContrato=0;
		this.contratosGlosaMap = new HashMap();
		this.contratosGlosaMap.put("numRegistros", "0");
		this.conceptosDetalleFacturaMap = new HashMap();
		this.conceptosDetalleFacturaMap.put("numRegistros", "0");
		this.conceptosParamMap = new HashMap();
		this.conceptosParamMap.put("numRegistros", "0");
		this.conceptoSel="";
		this.posicionFac="";
		this.factura="";
		this.mostrarLink=false;
		this.listadoFacturas = new HashMap<String, Object>();
		this.glosa = new DtoGlosa();
		this.codigoAuditoria = "";
		this.conceptosDevolucion = new ArrayList<HashMap<String,Object>>();
		this.linkSiguiente="";
		this.linkSiguienteSolicitudes = "";
		this.linkSiguienteAsocios = "";
		this.indice = "";
		this.ultimoIndice = "";
		this.patronOrdenar="";
		this.maxPageItems = 10;
		this.indicativoglosa="";
		this.codConvenio="";
		this.arregloConvMap= new HashMap();
		this.arregloConvMap.put("numRegistros", "0");
		this.codigoFac="";
		this.sumatoriaValorGlosa=0;
		this.conceptosAdd = new String[100];
		this.observacionesDetalle="";
		this.observacionesFacturaGlosas=new ArrayList<DtoObsFacturaGlosas>();
	}


	
	public String[] getConceptosAdd() {
		return conceptosAdd;
	}



	public void setConceptosAdd(String[] conceptosAdd) {
		this.conceptosAdd = conceptosAdd;
	}



	public float getSumatoriaValorGlosa() {
		return sumatoriaValorGlosa;
	}



	public void setSumatoriaValorGlosa(float sumatoriaValorGlosa) {
		this.sumatoriaValorGlosa = sumatoriaValorGlosa;
	}



	public HashMap getArregloConvMap() {
		return arregloConvMap;
	}

	public void setArregloConvMap(HashMap arregloConvMap) {
		this.arregloConvMap = arregloConvMap;
	}

	public Object getArregloConvMap(String key){
		return this.arregloConvMap.get(key);
	}
	
	public void setArregloConvMap(String key,Object value){
		this.arregloConvMap.put(key, value);
	}
	
	public String getConvenioP() {
		return convenioP;
	}



	public void setConvenioP(String convenioP) {
		this.convenioP = convenioP;
	}



	public String getCodConvenio() {
		return codConvenio;
	}



	public void setCodConvenio(String codConvenio) {
		this.codConvenio = codConvenio;
	}



	public String getIndicativoglosa() {
		return indicativoglosa;
	}



	public void setIndicativoglosa(String indicativoglosa) {
		this.indicativoglosa = indicativoglosa;
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
	 * @return the mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
		
	/**
	 * @return the conveniosMap
	 */
	public HashMap getConveniosMap() {
		return conveniosMap;
	}
	
	/**
	 * @param conveniosMap the conveniosMap to set
	 */
	public void setConveniosMap(HashMap conveniosMap) {
		this.conveniosMap = conveniosMap;
	}

	public Object getConveniosMap(String key) {
		return conveniosMap.get(key);
	}

	public void setConveniosMap(String key, Object value) {
		this.conveniosMap.put(key, value);
	}

	/**
	 * @return the convenio
	 */
	public String getConvenio() {
		return convenio;
	}
	
	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
		
	/**
	 * @return the arregloConvenios
	 */
	public ArrayList<HashMap<String, Object>> getArregloConvenios() {
		return arregloConvenios;
	}
	/**
	 * @param arregloConvenios the arregloConvenios to set
	 */
	public void setArregloConvenios(
			ArrayList<HashMap<String, Object>> arregloConvenios) {
		this.arregloConvenios = arregloConvenios;
	}

	/**
	 * @return the informacionGlosa
	 */
	public HashMap getInformacionGlosa() {
		return informacionGlosa;
	}

	/**
	 * @param informacionGlosa the informacionGlosa to set
	 */
	public void setInformacionGlosa(HashMap informacionGlosa) {
		this.informacionGlosa = informacionGlosa;
	}

	public Object getInformacionGlosa(String key) {
		return informacionGlosa.get(key);
	}

	public void setInformacionGlosa(String key, Object value) {
		this.informacionGlosa.put(key, value);
	}	

	/**
	 * @return the arregloConveniosPrincipal
	 */
	public ArrayList<HashMap<String, Object>> getArregloConveniosPrincipal() {
		return arregloConveniosPrincipal;
	}

	/**
	 * @param arregloConveniosPrincipal the arregloConveniosPrincipal to set
	 */
	public void setArregloConveniosPrincipal(
			ArrayList<HashMap<String, Object>> arregloConveniosPrincipal) {
		this.arregloConveniosPrincipal = arregloConveniosPrincipal;
	}
		
	public int getNumMapas()
	{
		return arregloConvenios.size();
	}
	
	public int getNumMapasP()
	{
		return arregloConveniosPrincipal.size();
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

	/**
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
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}


	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
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
	 * @return the banderaGuardar
	 */
	public String getBanderaGuardar() {
		return banderaGuardar;
	}

	/**
	 * @param banderaGuardar the banderaGuardar to set
	 */
	public void setBanderaGuardar(String banderaGuardar) {
		this.banderaGuardar = banderaGuardar;
	}

	/**
	 * @return the detalleGlosa
	 */
	public HashMap getMapaDetalleGlosa() {
		return mapaDetalleGlosa;
	}

	/**
	 * @param detalleGlosa the detalleGlosa to set
	 */
	public void setMapaDetalleGlosa(HashMap detalleGlosa) {
		this.mapaDetalleGlosa = detalleGlosa;
	}
	
	public Object getMapaDetalleGlosa(String key) {
		return mapaDetalleGlosa.get(key);
	}

	public void setMapaDetalleGlosa(String key, Object value) {
		this.mapaDetalleGlosa.put(key, value);
	}
	
	public Object getCodFacturaUrl(String key)
	{
		if(this.mapaDetalleGlosa.isEmpty())
			return "";
		
		return this.mapaDetalleGlosa.get("codfactura_0"); 
	}
	
	public void setCodFacturaUrl(String value)
	{
		if(this.mapaDetalleGlosa.isEmpty())
			this.mapaDetalleGlosa = new HashMap();
		
		this.codigoFac=value;
		this.mapaDetalleGlosa.put("codfactura_0",value);
	}
	
	public void setFacturaUrl(String value)
	{		
		if(this.mapaDetalleGlosa.isEmpty())
			this.mapaDetalleGlosa = new HashMap();
		
		this.mapaDetalleGlosa.put("factura_0", value);		
	}
	
	public Object getFacturaUrl(String key)
	{
		if(this.mapaDetalleGlosa.isEmpty())
			return "";
		
		return this.mapaDetalleGlosa.get("factura_0"); 
	}
	
	public void setCodGlosaUrl(String value)
	{
		if(this.mapaDetalleGlosa.isEmpty())
			this.mapaDetalleGlosa = new HashMap();
		
		this.mapaDetalleGlosa.put("codglosa_0", value);
	}
	
	public Object getCodGlosaUrl(String key)
	{
		if(this.mapaDetalleGlosa.isEmpty())
			return "";
		
		return this.mapaDetalleGlosa.get("codglosa_0"); 
	}
		
	public void setCodigoAudiUrl(String value)
	{
		if(this.mapaDetalleGlosa.isEmpty())
			this.mapaDetalleGlosa = new HashMap();
		
		this.mapaDetalleGlosa.put("codigoaudi_0", value);
	}
	
	public Object getCodigoAudiUrl(String key)
	{
		if(this.mapaDetalleGlosa.isEmpty())
			return "";
		
		return this.mapaDetalleGlosa.get("codigoaudi_0"); 
	}
	

	/**
	 * @return the facturaABuscar
	 */
	public String getFacturaABuscar() {
		return facturaABuscar;
	}

	/**
	 * @param facturaABuscar the facturaABuscar to set
	 */
	public void setFacturaABuscar(String facturaABuscar) {
		this.facturaABuscar = facturaABuscar;
	}

	/**
	 * @return the codFactura
	 */
	public String getCodFactura() {
		return codFactura;
	}

	/**
	 * @param codFactura the codFactura to set
	 */
	public void setCodFactura(String codFactura) {
		this.codFactura = codFactura;
	}

	/**
	 * @return the historicoMap
	 */
	public HashMap getHistoricoMap() {
		return historicoMap;
	}

	/**
	 * @param historicoMap the historicoMap to set
	 */
	public void setHistoricoMap(HashMap historicoMap) {
		this.historicoMap = historicoMap;
	}
	
	public Object getHistoricoMap(String key) {
		return historicoMap.get(key);
	}

	public void setHistoricoMap(String key, Object value) {
		this.historicoMap.put(key, value);
	}

	/**
	 * @return the unicaFacturaMap
	 */
	public HashMap getUnicaFacturaMap() {
		return unicaFacturaMap;
	}

	/**
	 * @param unicaFacturaMap the unicaFacturaMap to set
	 */
	public void setUnicaFacturaMap(HashMap unicaFacturaMap) {
		this.unicaFacturaMap = unicaFacturaMap;
	}
	
	public Object getUnicaFacturaMap(String key)
	{
		return this.unicaFacturaMap.get(key);
	}
	
	public void setUnicaFacturaMap(String key, Object value)
	{
		this.unicaFacturaMap.put(key, value);
	}	

	/**
	 * @return the posicion
	 */
	public String getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion the posicion to set
	 */
	public void setPosicion(String posicion) {
		this.posicion = posicion;
	}

	/**
	 * @return the consultaDetFacMap
	 */
	public HashMap getConsultaDetFacMap() {
		return consultaDetFacMap;
	}

	/**
	 * @param consultaDetFacMap the consultaDetFacMap to set
	 */
	public void setConsultaDetFacMap(HashMap consultaDetFacMap) {
		this.consultaDetFacMap = consultaDetFacMap;
	}
	
	public Object getConsultaDetFacMap(String key)
	{
		return this.consultaDetFacMap.get(key);
	}
	
	public void setConsultarDetFacMap(String key, Object value)
	{
		this.consultaDetFacMap.put(key, value);
	}
	
	/**
	 * @return the saldofact
	 */
	public String getSaldofact() {
		return saldofact;
	}

	/**
	 * @param saldofact the saldofact to set
	 */
	public void setSaldofact(String saldofact) {
		this.saldofact = saldofact;
	}

	/**
	 * @return the valorF
	 */
	public String getValorF() {
		return valorF;
	}

	/**
	 * @param valorF the valorF to set
	 */
	public void setValorF(String valorF) {
		this.valorF = valorF;
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
	 * @return the arregloContratos
	 */
	public ArrayList<HashMap<String, Object>> getArregloContratos() {
		return arregloContratos;
	}

	/**
	 * @param arregloContratos the arregloContratos to set
	 */
	public void setArregloContratos(
			ArrayList<HashMap<String, Object>> arregloContratos) {
		this.arregloContratos = arregloContratos;
	}
	
	/**
	 * @return the contratosGlosaMap
	 */
	public HashMap getContratosGlosaMap() {
		return contratosGlosaMap;
	}

	/**
	 * @param contratosGlosaMap the contratosGlosaMap to set
	 */
	public void setContratosGlosaMap(HashMap contratosGlosaMap) {
		this.contratosGlosaMap = contratosGlosaMap;
	}
	
	public Object getContratosGlosaMap(String key){
		return this.contratosGlosaMap.get(key);
	}
	
	public void setContratosGlosasMap(String key, Object value){
		this.contratosGlosaMap.put(key, value);
	}
	
	/**
	 * @return the conceptosDetalleFacturaMap
	 */
	public HashMap getConceptosDetalleFacturaMap() {
		return conceptosDetalleFacturaMap;
	}

	/**
	 * @param conceptosDetalleFacturaMap the conceptosDetalleFacturaMap to set
	 */
	public void setConceptosDetalleFacturaMap(HashMap conceptosDetalleFacturaMap) {
		this.conceptosDetalleFacturaMap = conceptosDetalleFacturaMap;
	}
	
	public Object getConceptosDetalleFacturaMap(String key)
	{
		return this.conceptosDetalleFacturaMap.get(key);
	}
	
	public void setConceptosDetalleFacturaMap(String key, Object value)
	{
		this.conceptosDetalleFacturaMap.put(key, value);
	}
	
	/**
	 * @return the conceptosParamMap
	 */
	public HashMap getConceptosParamMap() {
		return conceptosParamMap;
	}

	/**
	 * @param conceptosParamMap the conceptosParamMap to set
	 */
	public void setConceptosParamMap(HashMap conceptosParamMap) {
		this.conceptosParamMap = conceptosParamMap;
	}
	
	public Object getConceptosParamMap(String key)
	{
		return this.conceptosParamMap.get(key);
	}
	
	public void setConceptosParamMap(String key, Object value)
	{
		this.conceptosParamMap.put(key, value);
	}
	
	/**
	 * @return the conceptoSel
	 */
	public String getConceptoSel() {
		return conceptoSel;
	}

	/**
	 * @param conceptoSel the conceptoSel to set
	 */
	public void setConceptoSel(String conceptoSel) {
		this.conceptoSel = conceptoSel;
	}
	
	/**
	 * @return the posicionFac
	 */
	public String getPosicionFac() {
		return posicionFac;
	}

	/**
	 * @param posicionFac the posicionFac to set
	 */
	public void setPosicionFac(String posicionFac) {
		this.posicionFac = posicionFac;
	}

	/**
	 * @return the factura
	 */
	public String getFactura() {
		return factura;
	}

	/**
	 * @param factura the factura to set
	 */
	public void setFactura(String factura) {
		this.factura = factura;
	}

	/**
	 * @return the mostrarLink
	 */
	public boolean isMostrarLink() {
		return mostrarLink;
	}

	/**
	 * @param mostrarLink the mostrarLink to set
	 */
	public void setMostrarLink(boolean mostrarLink) {
		this.mostrarLink = mostrarLink;
	}	

	
	//################## NUEVO INGRESAR DETALLE GLOSA POR FACTURA ##########################//

	public ArrayList<HashMap<String, Object>> getConceptosDevolucion() {
		return conceptosDevolucion;
	}

	public void setConceptosDevolucion(
			ArrayList<HashMap<String, Object>> conceptosDevolucion) {
		this.conceptosDevolucion = conceptosDevolucion;
	}
	
	/**
	 * @return the codigoAuditoria
	 */
	public String getCodigoAuditoria() {
		return codigoAuditoria;
	}

	/**
	 * @param codigoAuditoria the codigoAuditoria to set
	 */
	public void setCodigoAuditoria(String codigoAuditoria) {
		this.codigoAuditoria = codigoAuditoria;
	}
	
	/**
	 * @return the listadoFacturas
	 */
	public HashMap<String, Object> getListadoFacturas() {
		return listadoFacturas;
	}

	/**
	 * @param listadoFacturas the listadoFacturas to set
	 */
	public void setListadoFacturas(HashMap<String, Object> listadoFacturas) {
		this.listadoFacturas = listadoFacturas;
	}
	
	/**
	 * @return the listadoFacturas
	 */
	public Object getListadoFacturas(String key) {
		return listadoFacturas.get(key);
	}

	/**
	 * @param listadoFacturas the listadoFacturas to set
	 */
	public void setListadoFacturas(String key,Object obj) {
		this.listadoFacturas.put(key, obj);
	}
	
	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the indice
	 */
	public String getIndice() {
		return indice;
	}

	/**
	 * @param indice the indice to set
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}

	/**
	 * @return the ultimoIndice
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}

	/**
	 * @param ultimoIndice the ultimoIndice to set
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}

	/**
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}



	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}



	/**
	 * @return the glosa
	 */
	public DtoGlosa getGlosa() {
		return glosa;
	}

	/**
	 * @param glosa the glosa to set
	 */
	public void setGlosa(DtoGlosa glosa) {
		this.glosa = glosa;
	}
	
	//####################################### FIN ########################################//
	



	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);  

        if(this.estado.equals("guardar"))
        {
        	if(this.convenioP.equals("-1"))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","El convenio"));
        	}
        	if(this.glosaEntidad.equals(""))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","La Glosa Entidad"));
        	}        	
        	if(this.valor.equals(""))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","El Valor de la Glosa"));
        	}
        	if(this.valor.equals("0"))
        	{
        		errores.add("descripcion",new ActionMessage("prompt.generico","El Valor de la Glosa debe ser mayor a cero."));
        	}
        	if(this.fechaNotificacion.equals(""))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","La Fecha de Notificacion de la Glosa"));
        	}
        	else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(this.fechaNotificacion), UtilidadFecha.getFechaActual()))
        	{
        		errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.fechaNotificacion+"",UtilidadFecha.getFechaActual()));
        	}
        }  
        
	    return errores;     
    }



	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}



	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}



	public String getCodigoFac() {
		return codigoFac;
	}



	public void setCodigoFac(String codigoFac) {
		this.codigoFac = codigoFac;
	}



	/**
	 * @return the linkSiguienteSolicitudes
	 */
	public String getLinkSiguienteSolicitudes() {
		return linkSiguienteSolicitudes;
	}



	/**
	 * @param linkSiguienteSolicitudes the linkSiguienteSolicitudes to set
	 */
	public void setLinkSiguienteSolicitudes(String linkSiguienteSolicitudes) {
		this.linkSiguienteSolicitudes = linkSiguienteSolicitudes;
	}



	/**
	 * @return the linkSiguienteAsocios
	 */
	public String getLinkSiguienteAsocios() {
		return linkSiguienteAsocios;
	}



	/**
	 * @param linkSiguienteAsocios the linkSiguienteAsocios to set
	 */
	public void setLinkSiguienteAsocios(String linkSiguienteAsocios) {
		this.linkSiguienteAsocios = linkSiguienteAsocios;
	}



	public String getObservacionesDetalle() {
		return observacionesDetalle;
	}



	public void setObservacionesDetalle(String observacionesDetalle) {
		this.observacionesDetalle = observacionesDetalle;
	}





	public ArrayList<DtoObsFacturaGlosas> getObservacionesFacturaGlosas() {
		return observacionesFacturaGlosas;
	}



	public void setObservacionesFacturaGlosas(
			ArrayList<DtoObsFacturaGlosas> observacionesFacturaGlosas) {
		this.observacionesFacturaGlosas = observacionesFacturaGlosas;
	}	
	
	
}