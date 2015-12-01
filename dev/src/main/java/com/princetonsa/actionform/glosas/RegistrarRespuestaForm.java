package com.princetonsa.actionform.glosas;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;

/**
 * @author Diego Bedoya
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 * Registrar Respuesta (GLOSAS)
 */
public class RegistrarRespuestaForm extends ValidatorForm
{	
	/**
	 * Para manejo de Logs
	 */
	private Logger logger = Logger.getLogger(RegistrarRespuestaForm.class);
	/*-----------------------------------------------
	 * 				ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * String Link Siguiente
	 * */
	private String linkSiguiente;
	
	/**
	 * Link Siguiente para el pager pagina Solicitudes
	 */
	private String linkSiguienteSolicitudes;
	
	/**
	 * Link Siguiente para el pager pagina Asocios
	 */
	private String linkSiguienteAsocios;
	
	/*-----------------------------------------------
	 * 				FIN ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
	
	/**
	 * estado del formulario
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Atributo para las observaciones de la respuesta
	 */
	private String observaciones;
	
	/**
	 * Atributo para la Glosa sistema
	 */
	private String glosa;
	
	/**
	 * Atributo para la respuesta de la glosa
	 */
	private String respuesta;
	
	/**
	 * Atributo para la fecha de la respuesta
	 */
	private String fechaRespuesta;
	
	/**
	 * Atributo para la conciliacion de la Respuesta Glosa
	 */
	private String conciliacion=ConstantesBD.acronimoNo;
	
	/**
	 * Atributo para manejar la fecha registro glosa
	 */
	private String fechaRegistroGlosa;
	
	/**
	 * Atributo para el valor de la glosa
	 */
	private String valor;
	
	/**
	 * Atributo para manejar el convenio asociado a la forma
	 */
	private String convenio;
	
	/**
	 * Atributo para el convenio de la Busqueda
	 */
	private String convenioBus;
	
	/**
	 * Atributo para manejar el contrato asociado a la glosa
	 */
	private String contrato;
	
	/**
	 * Atributo para manejar la Glosa Entidad
	 */
	private String glosaEntidad;
	
	/**
	 * Atributo para manejar la fecha de notificacion de la glosa
	 */
	private String fechaNotificacion;
	
	/**
	 * Mapa que almacena el encabezado de la glosa
	 */
	private HashMap encabezadoGlosaMap;	
	
	/**
	 * Mapa para la respuesta y el detalle de la misma
	 */
	private HashMap respuestaDetalleMap;
	
	/**
	 * Arreglo para almacenar los Convenios
	 */
	private ArrayList<HashMap<String, Object>> arregloConvenios = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Mapa Informacion de la Respuesta por Busqueda
	 */
	private HashMap informacionRespuesta;
	
	/**
	 * Mapa para las facturas por glosa
	 */
	private HashMap facturasGlosaMap;
	
	/**
	 * Atributo para validar si mostrar o no el link de detalle Factura
	 */
	private boolean mostrarLink;
	
	/**
	 * Atributo para almacenar la posicion en el mapa de la Factura Externa seleccionada para consultar detalle
	 */
	private String posicionFac;
	
	/**
	 * Atributo para almacenar el motivo de detalle Fatura Externa
	 */
	private String motivo;
	
	/**
	 * Atributo para almacenar el valor de la Respuesta
	 */
	private String valorRta;
	
	/**
	 * Atributo para almacenar la Cantidad de la Respuesta
	 */
	private String cantidadRta;
	
	/**
	 * Mapa de las solicitudes por factura glosa
	 */
	private HashMap solicitudesFacturaMap;
	
	/**
	 * Mapa para los conceptos activos en axioma
	 */
	private HashMap conceptosRespuestaMap;

	/**
	 * Variable para alamacenar el concepto seleccionado del pop up
	 */
	private String conceptoSel;
	
	/**
	 * Mapa detalle factura Externa
	 */
	private HashMap detalleFacturaExternaMap;
	
	/**
	 * Mapa para los Asocios de todas las solicitudes Factura Glosa
	 */
	private HashMap asociosSolicitudesMap;
	
	/**
	 * Variable para almacenar la posicion de solicitud que selecciono
	 */
	private String posicionSolicitud;
	
	/**
	 * Variable para almcenar el parametro de busqueda para facturas
	 */
	private String fechaElabFiltro;
	private String facturaFiltro;
	
	/**
	 * Variable para validar si se pueden adicionar Facturas  
	 */
	private boolean adicionarFacturas;
	
	private float sumatoriaValorGlosa;
	
	private int esMenor;
	
	private double valorTotalRta;
	
	private String conceptoSol;
	
	private String naturalezaSol;
	
	/**
	 * Resetea los valores de la forma cuando
	 * se ingresa por la funcionalidad por Area
	 * @param codigoInstitucion
	 */
	public void reset(int codigoInstitucion)
	{
		this.observaciones="";
		this.glosa="";
		this.respuesta="";
		this.fechaRespuesta="";
		this.conciliacion=ConstantesBD.acronimoNo;
		this.fechaRegistroGlosa="";
		this.valor="";
		this.convenio="";
		this.contrato="";
		this.glosaEntidad="";
		this.fechaNotificacion="";
		this.encabezadoGlosaMap=new HashMap();
		this.encabezadoGlosaMap.put("numRegistros", "0");
		this.respuestaDetalleMap=new HashMap();
		this.respuestaDetalleMap.put("numRegistros", "0");
		this.arregloConvenios = new ArrayList<HashMap<String,Object>>();
		this.convenioBus="";
		this.informacionRespuesta = new HashMap();
		this.informacionRespuesta.put("numRegistros", "0");
		this.facturasGlosaMap=new HashMap();
		this.facturasGlosaMap.put("numRegistros", "0");
		this.mostrarLink=false;
		this.posicionFac="";
		this.motivo="";
		this.valorRta="";
		this.cantidadRta="";
		this.solicitudesFacturaMap=new HashMap();
		this.solicitudesFacturaMap.put("numRegistros", "0");
		this.conceptosRespuestaMap=new HashMap();
		this.conceptosRespuestaMap.put("numRegistros", "0");
		this.conceptoSel="";
		this.detalleFacturaExternaMap=new HashMap();
		this.detalleFacturaExternaMap.put("numRegistros", "0");
		this.asociosSolicitudesMap=new HashMap();
		this.asociosSolicitudesMap.put("numRegistros", "0");
		this.posicionSolicitud="";
		this.facturaFiltro="";
		this.fechaElabFiltro="";
		this.adicionarFacturas=false;
		this.sumatoriaValorGlosa=0;
		this.esMenor=0;
		this.valorTotalRta=0.0;
		this.conceptoSol="";
		this.naturalezaSol="";
	}
	
	

	public String getNaturalezaSol() {
		return naturalezaSol;
	}



	public void setNaturalezaSol(String naturalezaSol) {
		this.naturalezaSol = naturalezaSol;
	}



	public String getConceptoSol() {
		return conceptoSol;
	}



	public void setConceptoSol(String conceptoSol) {
		this.conceptoSol = conceptoSol;
	}



	public double getValorTotalRta() {
		return valorTotalRta;
	}



	public void setValorTotalRta(double valorTotalRta) {
		this.valorTotalRta = valorTotalRta;
	}



	public int getEsMenor() {
		return esMenor;
	}



	public void setEsMenor(int esMenor) {
		this.esMenor = esMenor;
	}



	public float getSumatoriaValorGlosa() {
		return sumatoriaValorGlosa;
	}



	public void setSumatoriaValorGlosa(float sumatoriaValorGlosa) {
		this.sumatoriaValorGlosa = sumatoriaValorGlosa;
	}



	public String getFacturaFiltro() {
		return facturaFiltro;
	}


	public void setFacturaFiltro(String facturaFiltro) {
		this.facturaFiltro = facturaFiltro;
	}


	public boolean isAdicionarFacturas() {
		return adicionarFacturas;
	}


	public void setAdicionarFacturas(boolean adicionarFacturas) {
		this.adicionarFacturas = adicionarFacturas;
	}


	public String getFechaElabFiltro() {
		return fechaElabFiltro;
	}

	public void setFechaElabFiltro(String fechaElabFiltro) {
		this.fechaElabFiltro = fechaElabFiltro;
	}

	/**
	 * Resetea las variables solo dependiendo de la busqueda
	 * @param codigoInstitucion
	 */
	public void resetPorBusqueda(int codigoInstitucion)
	{
		this.respuestaDetalleMap=new HashMap();
		this.respuestaDetalleMap.put("numRegistros", "0");
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
	
	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
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
	 * @return the glosa
	 */
	public String getGlosa() {
		return glosa;
	}

	/**
	 * @param glosa the glosa to set
	 */
	public void setGlosa(String glosa) {
		this.glosa = glosa;
	}

	/**
	 * @return the respuesta
	 */
	public String getRespuesta() {
		return respuesta;
	}

	/**
	 * @param respuesta the respuesta to set
	 */
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	/**
	 * @return the fechaRespuesta
	 */
	public String getFechaRespuesta() {
		return fechaRespuesta;
	}

	/**
	 * @param fechaRespuesta the fechaRespuesta to set
	 */
	public void setFechaRespuesta(String fechaRespuesta) {
		this.fechaRespuesta = fechaRespuesta;
	}

	/**
	 * @return the conciliacion
	 */
	public String getConciliacion() {
		return conciliacion;
	}

	/**
	 * @param conciliacion the conciliacion to set
	 */
	public void setConciliacion(String conciliacion) {
		this.conciliacion = conciliacion;
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
	 * @return the contrato
	 */
	public String getContrato() {
		return contrato;
	}

	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(String contrato) {
		this.contrato = contrato;
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
	 * @return the encabezadoGlosaMap
	 */
	public HashMap getEncabezadoGlosaMap() {
		return encabezadoGlosaMap;
	}

	/**
	 * @param encabezadoGlosaMap the encabezadoGlosaMap to set
	 */
	public void setEncabezadoGlosaMap(HashMap encabezadoGlosaMap) {
		this.encabezadoGlosaMap = encabezadoGlosaMap;
	}
	
	public Object getEncabezadoGlosaMap(String key){
		return this.encabezadoGlosaMap.get(key);
	}
	
	public void setEncabezadoGlosaMap(String key, Object value){
		this.encabezadoGlosaMap.put(key, value);
	}

	/**
	 * @return the respuestaDetalleMap
	 */
	public HashMap getRespuestaDetalleMap() {
		return respuestaDetalleMap;
	}

	/**
	 * @param respuestaDetalleMap the respuestaDetalleMap to set
	 */
	public void setRespuestaDetalleMap(HashMap respuestaDetalleMap) {
		this.respuestaDetalleMap = respuestaDetalleMap;
	}
	
	public Object getRespuestaDetalleMap(String key){
		return this.respuestaDetalleMap.get(key);
	}
	
	public void setRespuestaDetalleMap(String key, Object value){
		this.respuestaDetalleMap.put(key, value);
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
	 * @return the convenioBus
	 */
	public String getConvenioBus() {
		return convenioBus;
	}

	/**
	 * @param convenioBus the convenioBus to set
	 */
	public void setConvenioBus(String convenioBus) {
		this.convenioBus = convenioBus;
	}

	/**
	 * @return the informacionRespuesta
	 */
	public HashMap getInformacionRespuesta() {
		return informacionRespuesta;
	}

	/**
	 * @param informacionRespuesta the informacionRespuesta to set
	 */
	public void setInformacionRespuesta(HashMap informacionRespuesta) {
		this.informacionRespuesta = informacionRespuesta;
	}
	
	public Object getInformacionRespuesta(String key){
		return this.informacionRespuesta.get(key);
	}
	
	public void setInformacionRespuesta(String key, Object value){
		this.informacionRespuesta.put(key, value);
	}

	public HashMap getFacturasGlosaMap() {
		return facturasGlosaMap;
	}

	public void setFacturasGlosaMap(HashMap facturasGlosaMap) {
		this.facturasGlosaMap = facturasGlosaMap;
	}
	
	public Object getFacturasGlosaMap(String key){
		return this.facturasGlosaMap.get(key);
	}
	
	public void setFacturasGlosaMap(String key, Object value){
		this.facturasGlosaMap.put(key, value);
	}

	public boolean isMostrarLink() {
		return mostrarLink;
	}

	public void setMostrarLink(boolean mostrarLink) {
		this.mostrarLink = mostrarLink;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getValorRta() {
		return valorRta;
	}

	public void setValorRta(String valorRta) {
		this.valorRta = valorRta;
	}

	public String getPosicionFac() {
		return posicionFac;
	}

	public void setPosicionFac(String posicionFac) {
		this.posicionFac = posicionFac;
	}

	public String getCantidadRta() {
		return cantidadRta;
	}

	public void setCantidadRta(String cantidadRta) {
		this.cantidadRta = cantidadRta;
	}

	public HashMap getSolicitudesFacturaMap() {
		return solicitudesFacturaMap;
	}

	public void setSolicitudesFacturaMap(HashMap solicitudesFacturaMap) {
		this.solicitudesFacturaMap = solicitudesFacturaMap;
	}
	
	public Object getSolicitudesFacturaMap(String key){
		return this.solicitudesFacturaMap.get(key);
	}
	
	public void setSolicitudesFacturaMap(String key, Object value){
		this.solicitudesFacturaMap.put(key, value);
	}

	public HashMap getConceptosRespuestaMap() {
		return conceptosRespuestaMap;
	}

	public void setConceptosRespuestaMap(HashMap conceptosRespuestaMap) {
		this.conceptosRespuestaMap = conceptosRespuestaMap;
	}
	
	public Object getConceptosRespuestaMap(String key){
		return this.conceptosRespuestaMap.get(key);
	}
	
	public void setConceptosRespuestaMap(String key, Object value){
		this.conceptosRespuestaMap.put(key, value);
	}

	public String getConceptoSel() {
		return conceptoSel;
	}

	public void setConceptoSel(String conceptoSel) {
		this.conceptoSel = conceptoSel;
	}

	public HashMap getDetalleFacturaExternaMap() {
		return detalleFacturaExternaMap;
	}

	public void setDetalleFacturaExternaMap(HashMap detalleFacturaExternaMap) {
		this.detalleFacturaExternaMap = detalleFacturaExternaMap;
	}
	
	public Object getDetalleFacturaExternaMap(String key){
		return this.detalleFacturaExternaMap.get(key);
	}
	
	public void setDetalleFacturaExternaMap(String key, Object value){
		this.detalleFacturaExternaMap.put(key, value);
	}

	public HashMap getAsociosSolicitudesMap() {
		return asociosSolicitudesMap;
	}

	public void setAsociosSolicitudesMap(HashMap asociosSolicitudesMap) {
		this.asociosSolicitudesMap = asociosSolicitudesMap;
	}
	
	public Object getAsociosSolicitudesMap(String key){
		return this.asociosSolicitudesMap.get(key);
	}
	
	public void setAsociosSolicitudesMap(String key, Object value){
		this.asociosSolicitudesMap.put(key, value);
	}

	public String getPosicionSolicitud() {
		return posicionSolicitud;
	}

	public void setPosicionSolicitud(String posicionSolicitud) {
		this.posicionSolicitud = posicionSolicitud;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
                
        if(this.getEstado().equals("buscarGlosa"))
        {        	
        	if((this.getInformacionRespuesta("glosasis").equals("")))
        		errores.add("descripcion",new ActionMessage("errors.required","El consecutivo de Glosa "));
        }
		return errores;
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
	
	
}