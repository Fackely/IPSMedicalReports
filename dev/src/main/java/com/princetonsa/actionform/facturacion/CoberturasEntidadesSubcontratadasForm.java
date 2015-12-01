package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author Angela María Angel amangel@axioma-md.com
 *
 */


import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;


import util.ResultadoBoolean;

public class CoberturasEntidadesSubcontratadasForm extends ValidatorForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4236574310935905488L;

	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(CoberturasEntidadesSubcontratadasForm.class);
	
	/**
	 * Variable para manejo de Estado
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Mapa que almacena las entidades subcobcontratadas	
	 */
	private HashMap<String, Object> entidadesSubcontratadasMap = new HashMap<String, Object>();
	
	/**
	 * Variable que almacena la entidad subcontratada seleccionada
	 */
	private int entidadSubcontratadaSel;
	
	/**
	 * Mapa que almacena los contratos parametrizados segun la entidad subcontratada
	 */
	private HashMap<String, Object> contratosXEntidadSubMap = new HashMap<String, Object>();
	
	/**
	 * Variable que almacena el contrato seleccionado
	 */
	private int contratoXentSubSel;
	
	/**
	 * Mapa que almacena las coberturas parametrizadas en la funcionalidad parametrizacion coberturas 
	 */
	private HashMap<String, Object> coberturasMap = new HashMap<String, Object>();
	
	/**
	 * Mapa para almacenar las coberturas de la entidad subcontratada
	 */
	private HashMap<String, Object> coberturasEntiSubMap = new HashMap<String, Object>();
	
	/**
	 * Mapa para almacenar las vias de ingreso
	 */
	private HashMap<String, Object> viasIngreso = new HashMap<String, Object>();
	
	/**
	 * Mapa para almacenar las excepciones dela cobertura
	 */
	private HashMap<String, Object> excepCobertura = new HashMap<String, Object>();
	
	/**
	 * Variable para almacenar la via ingreso seleccionada para un nuevo registro
	 */
	private String viaIngresoSel;
	
	/**
	 * Variable para almacenar el tipo paciente seleccionada para un nuevo registro
	 */
	private String tipoPacienteSel;
	
	/**
	 * Variable para almacenar la naturaleza seleccionada para un nuevo registro
	 */
	private String naturalezaSel;
	
	/**
	 * Mapa para almacenar los tipos paciente para la via de ingreso seleccionada
	 */
	private HashMap<String, Object> tipoPaciente = new HashMap<String, Object>();
	
	/**
	 * Mapa para almacenar la naturaleza del paciente 
	 */
	private HashMap<String, Object> naturalezaMap = new HashMap<String, Object>();
	
	/**
	 * Variable q almacena la cobertura para un nuevo registro
	 */
	private HashMap<String, Object> nuevaCoberPriMap= new HashMap<String, Object>();

	/**
	 * Variable q almacena el indice del registro a eliminar de coberturas por entidades subcontratadas
	 */
	private String registroEliminar;

	private String registroModificar;
	
	private String consecutivoExCoberSel;
	
	/**
	 * Variable q almacena el indice del registro de excepcion de cobertura para ingresar al detalle
	 */
	private String detalleExCobertura;
	
	/**
	 * Mapa que almacena las clases de inventario definidas en el sistema
	 */
	private HashMap<String, Object> claseInventario= new HashMap<String, Object>();
	
	/**
	 * Variable que almacena la clase inventario seleccionada
	 */
	private String claseInventarioSel;

	/**
	 * Mapa que almacena los grupos segun la clase de inventario seleccionada
	 */
	private HashMap<String, Object> grupoInventario= new HashMap<String, Object>();
	
	/**
	 * Variable que el grupo de inventario seleccionado
	 */
	private String grupoInventarioSel;

	/**
	 * Mapa que almacena los subgrupos segun grupo de inventario seleccionada
	 */
	private HashMap<String, Object> subGrupoInventario= new HashMap<String, Object>();
	
	/**
	 * Variable que el sub grupo de inventario seleccionado
	 */
	private String subGrupoInventarioSel;
	
	/**
	 * Mapa que almacena las naturalezas de articulos creadas en el sistema
	 */
	private HashMap<String, Object> naturalezasArticuloMap= new HashMap<String, Object>();
	
	/**
	 * Variable que la naturaleza de articulo seleccionada
	 */
	private String naturalezaArtiSel;
	
	/**
	 * Variable que almacena si un articulo es incluido o no 
	 */
	private String checkAA;	
	
	/**
	 * Variable que almacena si un servicio es incluido o no 
	 */
	private String checkAE;
	
	/**
	 * Variable que almacena si un articulo especifico es incluido o no
	 */
	private String checkAS;
	
	/**
	 * Variable que almacena si un servicio especifico es incluido o no
	 */
	private String checkSE;
	
	/**
	 * Varible para almacenar si un articulo es Pos o No Pos
	 */
	private String tipoPos;
	
	/**
	 * Varible para almacenar si un articulo es subsidiado o no
	 */
	private String posSubsidiado;
	
	/**
	 * Mapa que almacena los grupos de servicio creadas en el sistema
	 */
	private HashMap<String, Object> grupoServicioMap= new HashMap<String, Object>();
	
	/**
	 * Variable que el grupo de servicio seleccionada
	 */
	private String grupoServicioSel;
	
	/**
	 * Mapa que almacena los tipos de servicio en el sistema
	 */
	private HashMap<String, Object> tiposServicioMap= new HashMap<String, Object>();
	
	/**
	 * Variable que el tipo servicio seleccionada
	 */
	private String tipoServicioSel;
	
	/**
	 * Mapa que almacena las especialidades creadas en el sistema
	 */
	private HashMap<String, Object> especialidadesMap= new HashMap<String, Object>();
	
	/**
	 * Variable que la especialidad seleccionada
	 */
	private String especialidadSel;
	
	/**
	 * Mapa que almacena los registros de Articulos Agrupados
	 */
	private HashMap<String, Object> artiAgruMap= new HashMap<String, Object>();

	/**
	 * Mapa que almacena los registros de Servicios Agrupados
	 */
	private HashMap<String, Object> servAgruMap= new HashMap<String, Object>();
		
	/**
	 * Variables para almacenar registro a modificar de cobertura
	 */
	private String coberturaSel;
	
	private String prioridadSel;
	
	private String consecutivoCoberSel;

	private String codigoServEsp;
			
	private String codigoCupsServEsp;
	
	private String servicioDescServEsp;

	
	/**
	 * Variables para eliminar y modificar Articulos o servicios
	 */
	
	
	private String consecutivoArtiAgruSel;
	
	private String consecutivoServAgruSel;
	
	private String codigoArtiEsp;
	
	private String descArtiEsp;
	
		
	/**
	 *Variables para implementar la busqueda de servicios y articulos generica 
	 */
	private String codigosServiciosInsertados;
	
	private HashMap<String,Object> datosBusquedaServicios= new HashMap<String,Object>();
	
	private HashMap<String,Object> datosBusquedaArticulos= new HashMap<String,Object>();
	
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * Variables para la busqueda de Articulos
	 */
	private String articulo;
	private String nombreArticulo;
	
	/**
	 * Resetea algunos valores de la forma cuando
	 * se filtran los contratos de una Entidad Subcontratada
	 * @param codigoInstitucion
	 */
	public void resetBusqueda(int codigoInstitucion)
	{
		this.coberturasEntiSubMap= new HashMap<String, Object>();
		this.coberturasEntiSubMap.put("numRegistros", "0");
		this.excepCobertura= new HashMap<String, Object>();
		this.excepCobertura.put("numRegistros", "0");
	}
	
	/**
	 * Resetea los valores de la forma cuando
	 * se ingresa por la funcionalidad por Area
	 * @param codigoInstitucion
	 */
	public void reset(int codigoInstitucion)
	{
		this.estado="";
		this.entidadSubcontratadaSel=-1;
		this.contratoXentSubSel=-1;
		this.entidadesSubcontratadasMap= new HashMap<String, Object>();
		this.entidadesSubcontratadasMap.put("numregistros", "0");
		this.contratosXEntidadSubMap= new HashMap<String, Object>();
		this.contratosXEntidadSubMap.put("numRegistros", "0");
		this.coberturasMap= new HashMap<String, Object>();
		this.coberturasMap.put("numRegistros", "0");
		this.coberturasEntiSubMap= new HashMap<String, Object>();
		this.coberturasEntiSubMap.put("numRegistros", "0");
		this.viasIngreso= new HashMap<String, Object>();
		this.viasIngreso.put("numRegistros", "0");
		this.excepCobertura= new HashMap<String, Object>();
		this.excepCobertura.put("numRegistros", "0");
		this.viaIngresoSel="";
		this.tipoPaciente= new HashMap<String, Object>();
		this.tipoPaciente.put("numRegistros", "0");
		this.tipoPacienteSel="-1";
		this.naturalezaMap= new HashMap<String, Object>();
		this.naturalezaMap.put("numRegistros", "0");
		this.naturalezaSel="";
		this.nuevaCoberPriMap= new HashMap<String, Object>();
		this.nuevaCoberPriMap.put("numRegistros", "0");
		this.registroEliminar="";
		this.detalleExCobertura="";
		this.claseInventario= new HashMap<String, Object>();
		this.claseInventario.put("numRegistros", "0");
		this.claseInventarioSel="";
		this.grupoInventario= new HashMap<String, Object>();
		this.grupoInventario.put("numRegistros", "0");
		this.grupoInventarioSel="";
		this.subGrupoInventario= new HashMap<String, Object>();
		this.subGrupoInventario.put("numRegistros", "0");
		this.subGrupoInventarioSel="";
		this.naturalezasArticuloMap= new HashMap<String, Object>();
		this.naturalezasArticuloMap.put("numRegistros", "0");
		this.naturalezaArtiSel="";
		this.tipoPos="";
		this.posSubsidiado="";
		this.grupoServicioMap= new HashMap<String, Object>();
		this.grupoServicioMap.put("numRegistros", "0");
		this.grupoServicioSel="";
		this.tiposServicioMap= new HashMap<String, Object>();
		this.tiposServicioMap.put("numRegistros", "0");
		this.tipoServicioSel="";
		this.especialidadesMap= new HashMap<String, Object>();
		this.especialidadesMap.put("numRegistros", "0");
		this.especialidadSel="";
		this.checkAA="S";
		this.checkAE="S";
		this.checkAS="S";
		this.checkSE="S";
		this.codigosServiciosInsertados="";
		this.datosBusquedaServicios= new HashMap<String,Object>();
		this.datosBusquedaServicios.put("numRegistros", "0");
		this.datosBusquedaArticulos= new HashMap<String,Object>();
		this.datosBusquedaArticulos.put("numRegistros", "0");
		this.artiAgruMap= new HashMap<String, Object>();
		this.artiAgruMap.put("numRegistros", "0");
		this.servAgruMap= new HashMap<String, Object>();
		this.servAgruMap.put("numRegistros", "0");
		this.coberturaSel="";
		this.prioridadSel="";
		this.consecutivoCoberSel="";
		this.consecutivoArtiAgruSel="";
		this.consecutivoServAgruSel="";
		this.codigoServEsp="";
		this.codigoCupsServEsp="";
		this.servicioDescServEsp="";
		this.codigoArtiEsp="";
		this.descArtiEsp="";
		this.registroModificar="";
		this.consecutivoExCoberSel="";
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.articulo="";
		this.nombreArticulo="";
	}	

	
	public String getArticulo() {
		return articulo;
	}

	public void setArticulo(String articulo) {
		this.articulo = articulo;
	}

	public String getNombreArticulo() {
		return nombreArticulo;
	}

	public void setNombreArticulo(String nombreArticulo) {
		this.nombreArticulo = nombreArticulo;
	}

	public String getViaIngresoSel() {
		return viaIngresoSel;
	}


	public void setViaIngresoSel(String viaIngresoSel) {
		this.viaIngresoSel = viaIngresoSel;
	}


	public String getNaturalezaSel() {
		return naturalezaSel;
	}


	public void setNaturalezaSel(String naturalezaSel) {
		this.naturalezaSel = naturalezaSel;
	}


	public String getConsecutivoServAgruSel() {
		return consecutivoServAgruSel;
	}


	public String getRegistroModificar() {
		return registroModificar;
	}


	public void setRegistroModificar(String registroModificar) {
		this.registroModificar = registroModificar;
	}


	public String getRegistroEliminar() {
		return registroEliminar;
	}


	public void setRegistroEliminar(String registroEliminar) {
		this.registroEliminar = registroEliminar;
	}


	public String getUltimoPatron() {
		return ultimoPatron;
	}


	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}


	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	public String getConsecutivoExCoberSel() {
		return consecutivoExCoberSel;
	}


	public void setConsecutivoExCoberSel(String consecutivoExCoberSel) {
		this.consecutivoExCoberSel = consecutivoExCoberSel;
	}

	
	public String getCodigoArtiEsp() {
		return codigoArtiEsp;
	}


	public void setCodigoArtiEsp(String codigoArtiEsp) {
		this.codigoArtiEsp = codigoArtiEsp;
	}


	public String getDescArtiEsp() {
		return descArtiEsp;
	}


	public void setDescArtiEsp(String descArtiEsp) {
		this.descArtiEsp = descArtiEsp;
	}


	public String getCodigoCupsServEsp() {
		return codigoCupsServEsp;
	}


	public void setCodigoCupsServEsp(String codigoCupsServEsp) {
		this.codigoCupsServEsp = codigoCupsServEsp;
	}


	public String getServicioDescServEsp() {
		return servicioDescServEsp;
	}


	public void setServicioDescServEsp(String servicioDescServEsp) {
		this.servicioDescServEsp = servicioDescServEsp;
	}


	public String getCodigoServEsp() {
		return codigoServEsp;
	}


	public void setCodigoServEsp(String codigoServEsp) {
		this.codigoServEsp = codigoServEsp;
	}

	
	public void setConsecutivoServAgruSel(String consecutivoServAgruSel) {
		this.consecutivoServAgruSel = consecutivoServAgruSel;
	}


	public String getConsecutivoArtiAgruSel() {
		return consecutivoArtiAgruSel;
	}


	public void setConsecutivoArtiAgruSel(String consecutivoArtiAgruSel) {
		this.consecutivoArtiAgruSel = consecutivoArtiAgruSel;
	}

	
	public String getConsecutivoCoberSel() {
		return consecutivoCoberSel;
	}


	public void setConsecutivoCoberSel(String consecutivoCoberSel) {
		this.consecutivoCoberSel = consecutivoCoberSel;
	}


	public String getCoberturaSel() {
		return coberturaSel;
	}


	public void setCoberturaSel(String coberturaSel) {
		this.coberturaSel = coberturaSel;
	}


	public String getPrioridadSel() {
		return prioridadSel;
	}


	public void setPrioridadSel(String prioridadSel) {
		this.prioridadSel = prioridadSel;
	}


	public String getCheckAA() {
		return checkAA;
	}


	public void setCheckAA(String checkAA) {
		this.checkAA = checkAA;
	}


	public String getCheckAE() {
		return checkAE;
	}


	public void setCheckAE(String checkAE) {
		this.checkAE = checkAE;
	}

	public String getCheckAS() {
		return checkAS;
	}


	public void setCheckAS(String checkAS) {
		this.checkAS = checkAS;
	}


	public String getCheckSE() {
		return checkSE;
	}


	public void setCheckSE(String checkSE) {
		this.checkSE = checkSE;
	}


	public HashMap<String, Object> getArtiAgruMap() {
		return artiAgruMap;
	}


	public void setArtiAgruMap(HashMap<String, Object> artiAgruMap) {
		this.artiAgruMap = artiAgruMap;
	}
	
	public Object getArtiAgruMap(String key) {
		return artiAgruMap.get(key);
	}

	public void setArtiAgruMap(String key, Object value){
		this.artiAgruMap.put(key, value);
	}

	public HashMap<String, Object> getServAgruMap() {
		return servAgruMap;
	}


	public void setServAgruMap(HashMap<String, Object> servAgruMap) {
		this.servAgruMap = servAgruMap;
	}
	
	public Object getServAgruMap(String key) {
		return servAgruMap.get(key);
	}
	
	public void setServAgruMap(String key, Object value){
		this.servAgruMap.put(key, value);
	}

	public HashMap<String, Object> getDatosBusquedaArticulos() {
		return datosBusquedaArticulos;
	}


	public void setDatosBusquedaArticulos(
			HashMap<String, Object> datosBusquedaArticulos) {
		this.datosBusquedaArticulos = datosBusquedaArticulos;
	}

	public Object getDatosBusquedaArticulos(String key) {
		return datosBusquedaArticulos.get(key);
	}
	
	public void setDatosBusquedaArticulos(String key, Object value){
		this.datosBusquedaArticulos.put(key, value);
	}

	public HashMap<String, Object> getDatosBusquedaServicios() {
		return datosBusquedaServicios;
	}


	public void setDatosBusquedaServicios(
			HashMap<String, Object> datosBusquedaServicios) {
		this.datosBusquedaServicios = datosBusquedaServicios;
	}
	
	public Object getDatosBusquedaServicios(String key) {
		return datosBusquedaServicios.get(key);
	}
	
	public void setDatosBusquedaServicios(String key, Object value){
		this.datosBusquedaServicios.put(key, value);
	}


	public String getCodigosServiciosInsertados() {
		return codigosServiciosInsertados;
	}


	public void setCodigosServiciosInsertados(String codigosServiciosInsertados) {
		this.codigosServiciosInsertados = codigosServiciosInsertados;
	}

	public Object getEspecialidadesMap(String key) {
		return especialidadesMap.get(key);
	}
	
	public void setEspecialidadesMap(String key, Object value){
		this.especialidadesMap.put(key, value);
	}
	
	public HashMap<String, Object> getEspecialidadesMap() {
		return especialidadesMap;
	}


	public void setEspecialidadesMap(HashMap<String, Object> especialidadesMap) {
		this.especialidadesMap = especialidadesMap;
	}


	public String getEspecialidadSel() {
		return especialidadSel;
	}


	public void setEspecialidadSel(String especialidadSel) {
		this.especialidadSel = especialidadSel;
	}


	public HashMap<String, Object> getTiposServicioMap() {
		return tiposServicioMap;
	}


	public void setTiposServicioMap(HashMap<String, Object> tiposServicioMap) {
		this.tiposServicioMap = tiposServicioMap;
	}
	
	public Object getTiposServicioMap(String key) {
		return tiposServicioMap.get(key);
	}
	
	public void setTiposServicioMap(String key, Object value){
		this.tiposServicioMap.put(key, value);
	}


	public String getTipoServicioSel() {
		return tipoServicioSel;
	}


	public void setTipoServicioSel(String tipoServicioSel) {
		this.tipoServicioSel = tipoServicioSel;
	}


	public HashMap<String, Object> getGrupoServicioMap() {
		return grupoServicioMap;
	}

	public void setGrupoServicioMap(HashMap<String, Object> grupoServicioMap) {
		this.grupoServicioMap = grupoServicioMap;
	}


	public Object getGrupoSercvicioMap(String key) {
		return grupoServicioMap.get(key);
	}
	
	public void setGrupoSercvicioMap(String key, Object value){
		this.grupoServicioMap.put(key, value);
	}
	
	public String getGrupoServicioSel() {
		return grupoServicioSel;
	}

	public void setGrupoServicioSel(String grupoServicioSel) {
		this.grupoServicioSel = grupoServicioSel;
	}

	public String getPosSubsidiado() {
		return posSubsidiado;
	}

	public void setPosSubsidiado(String posSubsidiado) {
		this.posSubsidiado = posSubsidiado;
	}

	public String getTipoPos() {
		return tipoPos;
	}

	public void setTipoPos(String tipoPos) {
		this.tipoPos = tipoPos;
	}

	public HashMap<String, Object> getNaturalezasArticuloMap() {
		return naturalezasArticuloMap;
	}

	public void setNaturalezasArticuloMap(
			HashMap<String, Object> naturalezasArticuloMap) {
		this.naturalezasArticuloMap = naturalezasArticuloMap;
	}
	
	public Object getNaturalezasArticuloMap(String key) {
		return naturalezasArticuloMap.get(key);
	}
	
	public void setNaturalezasArticuloMap(String key, Object value){
		this.naturalezasArticuloMap.put(key, value);
	}

	public String getNaturalezaArtiSel() {
		return naturalezaArtiSel;
	}

	public void setNaturalezaArtiSel(String naturalezaArtiSel) {
		this.naturalezaArtiSel = naturalezaArtiSel;
	}

	public HashMap<String, Object> getSubGrupoInventario() {
		return subGrupoInventario;
	}

	public void setSubGrupoInventario(HashMap<String, Object> subGrupoInventario) {
		this.subGrupoInventario = subGrupoInventario;
	}
	
	public Object getSubGrupoInventario(String key) {
		return subGrupoInventario.get(key);
	}
	
	public void setSubGrupoInventario(String key, Object value){
		this.subGrupoInventario.put(key, value);
	}

	public String getSubGrupoInventarioSel() {
		return subGrupoInventarioSel;
	}

	public void setSubGrupoInventarioSel(String subGrupoInventarioSel) {
		this.subGrupoInventarioSel = subGrupoInventarioSel;
	}

	public HashMap<String, Object> getGrupoInventario() {
		return grupoInventario;
	}

	public void setGrupoInventario(HashMap<String, Object> grupoInventario) {
		this.grupoInventario = grupoInventario;
	}
	
	public Object getGrupoInventario(String key) {
		return grupoInventario.get(key);
	}
	
	public void setGrupoInventario(String key, Object value){
		this.grupoInventario.put(key, value);
	}

	public String getGrupoInventarioSel() {
		return grupoInventarioSel;
	}

	public void setGrupoInventarioSel(String grupoInventarioSel) {
		this.grupoInventarioSel = grupoInventarioSel;
	}

	public String getClaseInventarioSel() {
		return claseInventarioSel;
	}

	public void setClaseInventarioSel(String claseInventarioSel) {
		this.claseInventarioSel = claseInventarioSel;
	}

	public HashMap<String, Object> getClaseInventario() {
		return claseInventario;
	}


	public void setClaseInventario(HashMap<String, Object> claseInventario) {
		this.claseInventario = claseInventario;
	}

	public Object getClaseInventario(String key) {
		return claseInventario.get(key);
	}
	
	public void setClaseInventario(String key, Object value){
		this.claseInventario.put(key, value);
	}

	public String getDetalleExCobertura() {
		return detalleExCobertura;
	}


	public void setDetalleExCobertura(String detalleExCobertura) {
		this.detalleExCobertura = detalleExCobertura;
	}


	public HashMap<String, Object> getNuevaCoberPriMap() {
		return nuevaCoberPriMap;
	}


	public void setNuevaCoberPriMap(HashMap<String, Object> nuevaCoberPriMap) {
		this.nuevaCoberPriMap = nuevaCoberPriMap;
	}

	public Object getNuevaCoberPriMap(String key) {
		return nuevaCoberPriMap.get(key);
	}
	
	public void setNuevaCoberPriMap(String key, Object value){
		this.nuevaCoberPriMap.put(key, value);
	}

	public String getTipoPacienteSel() {
		return tipoPacienteSel;
	}


	public void setTipoPacienteSel(String tipoPacienteSel) {
		this.tipoPacienteSel = tipoPacienteSel;
	}


	public HashMap<String, Object> getNaturalezaMap() {
		return naturalezaMap;
	}


	public void setNaturalezaMap(HashMap<String, Object> naturalezaMap) {
		this.naturalezaMap = naturalezaMap;
	}
	
	public Object getNaturalezaMap(String key) {
		return naturalezaMap.get(key);
	}

	public void setNaturalezaMap(String key, Object value){
		this.naturalezaMap.put(key, value);
	}

	
	public HashMap<String, Object> getTipoPaciente() {
		return tipoPaciente;
	}


	public void setTipoPaciente(HashMap<String, Object> tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}
	
	public Object getTipoPaciente(String key) {
		return tipoPaciente.get(key);
	}
	
	public void setTipoPaciente(String key, Object value){
		this.tipoPaciente.put(key, value);
	}


	public HashMap<String, Object> getExcepCobertura() {
		return excepCobertura;
	}


	public void setExcepCobertura(HashMap<String, Object> excepCobertura) {
		this.excepCobertura = excepCobertura;
	}

	public Object getExcepCobertura(String key) {
		return excepCobertura.get(key);
	}
	
	public void setExcepCobertura(String key, Object value){
		this.excepCobertura.put(key, value);
	}
	public HashMap<String, Object> getViasIngreso() {
		return viasIngreso;
	}


	public void setViasIngreso(HashMap<String, Object> viasIngreso) {
		this.viasIngreso = viasIngreso;
	}
	
	public Object getViasIngreso(String key) {
		return viasIngreso.get(key);
	}

	public void setViasIngreso(String key, Object value){
		this.viasIngreso.put(key, value);
	}

	public HashMap<String, Object> getCoberturasEntiSubMap() {
		return coberturasEntiSubMap;
	}


	public void setCoberturasEntiSubMap(HashMap<String, Object> coberturasEntiSubMap) {
		this.coberturasEntiSubMap = coberturasEntiSubMap;
	}
	
	public Object getCoberturasEntiSubMap(String key) {
		return coberturasEntiSubMap.get(key);
	}

	public void setCoberturasEntiSubMap(String key, Object value){
		this.coberturasEntiSubMap.put(key, value);
	}

	public HashMap<String, Object> getCoberturasMap() {
		return coberturasMap;
	}

	public void setCoberturasMap(HashMap<String, Object> coberturasMap) {
		this.coberturasMap = coberturasMap;
	}
	
	public Object getCoberturasMap(String key) {
		return coberturasMap.get(key);
	}
	
	public void setCoberturasMap(String key, Object value){
		this.coberturasMap.put(key, value);
	}

	public HashMap<String, Object> getContratosXEntidadSubMap() {
		return contratosXEntidadSubMap;
	}

	public void setContratosXEntidadSubMap(
			HashMap<String, Object> contratosXEntidadSubMap) {
		this.contratosXEntidadSubMap = contratosXEntidadSubMap;
	}
	
	public Object getContratosXEntidadSubMap(String key){
		return contratosXEntidadSubMap.get(key);
	}
	
	public void setContratosXEntidadSubMap(String key, Object value){
		this.contratosXEntidadSubMap.put(key, value);
	}

	public int getContratoXentSubSel() {
		return contratoXentSubSel;
	}

	public void setContratoXentSubSel(int contratoXentSubSel) {
		this.contratoXentSubSel = contratoXentSubSel;
	}

	public HashMap<String, Object> getEntidadesSubcontratadasMap() {
		return entidadesSubcontratadasMap;
	}

	public void setEntidadesSubcontratadasMap(
			HashMap<String, Object> entidadesSubcontratadasMap) {
		this.entidadesSubcontratadasMap = entidadesSubcontratadasMap;
	}
	
	public Object getEntidadesSubcontratadasMap(String key){
		return entidadesSubcontratadasMap.get(key);
	}
	
	public void setEntidadesSubcontratadasMap(String key, Object value){
		this.entidadesSubcontratadasMap.put(key, value);
	}

	public int getEntidadSubcontratadaSel() {
		return entidadSubcontratadaSel;
	}

	public void setEntidadSubcontratadaSel(int entidadSubcontratadaSel) {
		this.entidadSubcontratadaSel = entidadSubcontratadaSel;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
	
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        
        if(estado.equals("nuevoRegistroCobertura") || estado.equals("nuevoRegistroEx"))
        {
        	if(this.entidadSubcontratadaSel <= 0)
        		errores.add("descripcion",new ActionMessage("errors.required","La Entidad Subcontratada "));
        	
        	if(this.contratoXentSubSel <= 0)
        		errores.add("descripcion",new ActionMessage("errors.required","El contrato "));
        }
        if(estado.equals("guardarNuevo"))
        {
        	if(this.entidadSubcontratadaSel <= 0)
        		errores.add("descripcion",new ActionMessage("errors.required","La Entidad Subcontratada "));
        	
        	if(this.contratoXentSubSel <= 0)
        		errores.add("descripcion",new ActionMessage("errors.required","El contrato "));    
        	
        	if((this.viaIngresoSel+"").equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","La Via de Ingreso "));   
           	
        	if(this.tipoPacienteSel.equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","El Tipo Paciente "));   
           	
           	if((this.naturalezaSel+"").equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","La Naturaleza "));   
           	
           	if(!errores.isEmpty())		
        	{
           		this.estado="nuevoRegistroEx";
    		}

        }
        if(estado.equals("guardarNuevoRegCobertura"))
        {
        	if((this.nuevaCoberPriMap.get("cobertura")+"").equals("-1"))
        		errores.add("descripcion",new ActionMessage("errors.required","La Cobertura "));
        	
        	if((this.nuevaCoberPriMap.get("prioridad")+"").equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","La Prioridad "));
        	
           	if(!errores.isEmpty())		
        	{
           		this.estado="nuevoRegistroCobertura";
    		}
        }
        if(estado.equals("guardarModificacionCobertura"))
        {
        	if(this.coberturaSel.equals("-1"))
        		errores.add("descripcion",new ActionMessage("errors.required","La Cobertura "));
        	
        	if(this.prioridadSel.equals(""))
        		errores.add("descripcion",new ActionMessage("errors.required","La Prioridad "));
        	
        	if(!errores.isEmpty())		
        	{
           		this.estado="modificarRegCoberturas";
    		}
        }
        if(estado.equals("modificarRegistroExCobertura"))
        {
        	logger.info("\n\nvia ingreso::"+this.viaIngresoSel+"\n\ntipo paciente::"+this.tipoPacienteSel+"\n\nnaturaleza::"+this.tipoPacienteSel);
        }
        return errores;
    }
}