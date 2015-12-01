package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.Utilidades;

import com.princetonsa.dto.facturacion.DTOEstanciaViaIngCentroCosto;
import com.princetonsa.dto.manejoPaciente.DtoCentroCostosVista;
import com.princetonsa.mundo.facturacion.EntidadesSubContratadas;

/**
 * 
 * @author Jhony Alexander Duque A.
 * 02/01/2008
 *
 */


public class EntidadesSubContratadasForm extends ValidatorForm
{
	
	
	
	/**
	 *  lista para la instancia
	 */
	private List<DTOEstanciaViaIngCentroCosto> listaEstanciaviaIngCentroCosto;
	/**
	 *  DTO Estancia via ingreso centro costo 
	 */
	private DTOEstanciaViaIngCentroCosto dtoEstanciaViaIngresoCentroCosto;
	
	/**
	 *  Lista de Centro de costos
	 */
	private  List<DtoCentroCostosVista> listaCentroCosto;
	
	/**
	 *  Atributo para almacena  el centro  costo
	 */
	private String centroCosto;
	
	/**
	 * Atributo que almacena el c&oacute;digo
	 * del centro de costo
	 */
	private int codCentroCosto;
	
	private int indiceEliminarViasCentros;
	
	
	private String nombreTemporalTercero;
	
	private String codigoTemporalTercero;
	
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase censocamasform
	 */
	Logger logger = Logger.getLogger(EntidadesSubContratadasForm.class);
	
	String [] indices = EntidadesSubContratadas.indices;
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	
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
	 * String usuarios activos
	 * */
	private String usuarios;
	
	private HashMap<String, Object> usuariosSelMap= new HashMap<String, Object> ();
	
	private HashMap<String, Object> usuariosMap=new HashMap<String, Object> ();
	
	private String indiceElimUsu;
	
	/**
	 * >Int dias de vencimiento de la factura 
	 * */
	private int diasVenFact;
	
	private boolean activo;
	/**
	 * String codigo cuenta contable
	 * */
	private String cuentaCxp;
	
	private String valorMapa;
	/**
	 * String Link Siguiente
	 * */
	private String linkSiguiente;	
	
	private HashMap<String, Object> usuariosEntidadSub=new HashMap<String, Object> ();
	
	/*-----------------------------------------------
	 * 				FIN ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
	
	/*-----------------------------------------------
	 * 				ATRIBUTOS DE LA FORMA
	 ------------------------------------------------*/
	/**
	 * Coleccion principal donde se almacenaran los datos de entidades
	 */
	private HashMap entidadesSubContratadas;
	
	/**
	 * copia de los datos originales
	 */
	private HashMap entidadesSubcontratadasClone;
	
	/**
	 * Controla mediande el manejo de estados
	 */ 
	private String estado;

	private HashMap terceros;
	
	private Collection resultados;
	/**
	 * indica si la operacion se realizo con exito o no.
	 */
	private boolean operacionTrue;
	
	/**
	 * almacena los esquemas tarifarios de servicios 
	 */
	private ArrayList esqTarServ;
	
	/**
	 * almacena los esquemas tarifarios de inventarios
	 */
	private ArrayList esqTarInv;
	
	/**
	 *  indica cual index se va a eliminar
	 */
	private String indexEliminado;
	
	private HashMap detalle;
	
	private String indexDestino;
	
	private HashMap detalleClone;
	
	private HashMap detalleEliminado;
	
	/**
	 * Datos de la busqueda avanzada
	 */
	private HashMap criteriosBusqueda;
	
	/**
	 * resultado de la busqueda avanzada
	 */
	private HashMap resultBusqueda;
	
	/**
	 *Encargado de almacenar las entidades que son borradas 
	 */
	private HashMap resultBusquedaEliminado;
	
//	Se eliminan estos atributos por incidencia Mantis 1221
//	/**
//	 * se utiliza para almacenar los centros atencion activos en el sistema
//	 */
//	private HashMap mapCentrosAtencion;
//	private String centroAtencionCub;
	
	/**
	 *Indica el origen de un llamado
	 */
	private String origen;
	
	private boolean unRegistro;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);


	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
	
	private HashMap mapViasIngreso;
	private String viasIngreso;
	private String estancia;
	
	/*-----------------------------------------------
	 * 				FIN ATRIBUTOS DE LA FORMA
	 ------------------------------------------------*/
	
	
	/*-----------------------------------------------
	 * 		 	METODOS SETTERS AND GETTERS
	 ------------------------------------------------*/
	
	
	
	
	
	
	
	
	
	public String getOrigen() {
		return origen;
	}


	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(String usuarios) {
		this.usuarios = usuarios;
	}
		

	public HashMap<String, Object> getUsuariosSelMap() {
		return usuariosSelMap;
	}

	public void setUsuariosSelMap(HashMap<String, Object> usuariosSelMap) {
		this.usuariosSelMap = usuariosSelMap;
	}

	public Object getUsuariosSelMap(String key) {
		return usuariosSelMap.get(key);
	}
	
	public void setUsuariosSelMap(String key, Object value){
		this.usuariosSelMap.put(key, value);
	}
	
	
	
	public HashMap<String, Object> getUsuariosEntidadSub() {
		return usuariosEntidadSub;
	}

	public void setUsuariosEntidadSub(HashMap<String, Object> usuariosEntidadSub) {
		this.usuariosEntidadSub = usuariosEntidadSub;
	}


	public Object getUsuariosEntidadSub(String key) {
		return usuariosEntidadSub.get(key);
	}
	
	public void setUsuariosEntidadSub(String key, Object value){
		this.usuariosEntidadSub.put(key, value);
	}
	
	
	public HashMap<String, Object> getUsuariosMap() {
		return usuariosMap;
	}

	public void setUsuariosMap(HashMap<String, Object> usuariosMap) {
		this.usuariosMap = usuariosMap;
	}
	
	public Object getUsuariosMap(String key) {
		return usuariosMap.get(key);
	}
	
	public void setUsuariosMap(String key, Object value){
		this.usuariosMap.put(key, value);
	}

	public String getIndiceElimUsu() {
		return indiceElimUsu;
	}

	public void setIndiceElimUsu(String indiceElimUsu) {
		this.indiceElimUsu = indiceElimUsu;
	}

	public int getDiasVenFact() {
		return diasVenFact;
	}

	public void setDiasVenFact(int diasVenFact) {
		this.diasVenFact = diasVenFact;
	}

	public String getCuentaCxp() {
		return cuentaCxp;
	}

	public void setCuentaCxp(String cuentaCxp) {
		this.cuentaCxp = cuentaCxp;
	}

	

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public HashMap getCriteriosBusqueda() {
		return criteriosBusqueda;
	}

	public void setCriteriosBusqueda(HashMap criteriosBusqueda) {
		this.criteriosBusqueda = criteriosBusqueda;
	}

	public Object getCriteriosBusqueda(String key) {
		return criteriosBusqueda.get(key);
	}

	public void setCriteriosBusqueda(String key, Object value) {
		this.criteriosBusqueda.put(key, value);
	}
	
	
	public HashMap getDetalleEliminado() {
		return detalleEliminado;
	}

	public void setDetalleEliminado(HashMap detalleEliminado) {
		this.detalleEliminado = detalleEliminado;
	}
	
	public Object getDetalleEliminado(String key) {
		return detalleEliminado.get(key);
	}

	public void setDetalleEliminado(String key, Object value) {
		this.detalleEliminado.put(key, value);
	}

	public HashMap getDetalleClone() {
		return detalleClone;
	}

	public void setDetalleClone(HashMap detalleClone) {
		this.detalleClone = detalleClone;
	}

	public String getIndexDestino() {
		return indexDestino;
	}

	public void setIndexDestino(String indexDestino) {
		this.indexDestino = indexDestino;
	}

	public HashMap getDetalle() {
		return detalle;
	}

	public void setDetalle(HashMap detalle) {
		this.detalle = detalle;
	}

	
	
	public Object getDetalle(String key) {
		return detalle.get(key);
	}

	public void setDetalle(String key, Object value) {
		this.detalle.put(key, value);
	}
	
	
	public ArrayList getEsqTarInv() {
		return esqTarInv;
	}

	public void setEsqTarInv(ArrayList esqTarInv) {
		this.esqTarInv = esqTarInv;
	}

	public ArrayList getEsqTarServ() {
		return esqTarServ;
	}

	public void setEsqTarServ(ArrayList esqTarServ) {
		this.esqTarServ = esqTarServ;
	}

	public HashMap getTerceros() {
		return terceros;
	}

	public void setTerceros(HashMap terceros) {
		this.terceros = terceros;
	}
	
	public Object getTerceros(String key) {
		return terceros.get(key);
	}

	public void setTerceros(String key,Object value) {
		this.terceros = terceros;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public HashMap getEntidadesSubContratadas() {
		return entidadesSubContratadas;
	}

	public void setEntidadesSubContratadas(HashMap entidadesSubContratadas) {
		this.entidadesSubContratadas = entidadesSubContratadas;
	}
	
	
	public Object getEntidadesSubContratadas(String key) {
		return entidadesSubContratadas.get(key);
	}

	public void setEntidadesSubContratadas(String key, Object value) {
		this.entidadesSubContratadas.put(key, value);
	}
	
	

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public String getUltimoPatron() {
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}
	
	public Collection getResultados() {
		return resultados;
	}

	public void setResultados(Collection resultados) {
		this.resultados = resultados;
	}
	
	
	public void resetTerceros ()
	{
		this.terceros = new HashMap ();
		this.resultados = new ArrayList ();
	}

	public boolean isOperacionTrue() {
		return operacionTrue;
	}

	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}

	public String getIndexEliminado() {
		return indexEliminado;
	}

	public void setIndexEliminado(String indexEliminado) {
		this.indexEliminado = indexEliminado;
	}
	
	
	public HashMap getResultBusqueda() {
		return resultBusqueda;
	}

	public void setResultBusqueda(HashMap resultBusqueda) {
		this.resultBusqueda = resultBusqueda;
	}
	
	
	public Object getResultBusqueda(String key) {
		return resultBusqueda.get(key);
	}

	public void setResultBusqueda(String key, Object value) {
		this.resultBusqueda.put(key, value);
	}
	
	

	public HashMap getResultBusquedaEliminado() {
		return resultBusquedaEliminado;
	}

	public void setResultBusquedaEliminado(HashMap resultBusquedaEliminado) {
		this.resultBusquedaEliminado = resultBusquedaEliminado;
	}
	
	

	public Object getResultBusquedaEliminado(String key) {
		return resultBusquedaEliminado.get(key);
	}

	public void setResultBusquedaEliminado(String key,Object value) {
		this.resultBusquedaEliminado.put(key, value);
	}
	
	

	public HashMap getEntidadesSubcontratadasClone() {
		return entidadesSubcontratadasClone;
	}

	public void setEntidadesSubcontratadasClone(HashMap entidadesSubcontratadasClone) {
		this.entidadesSubcontratadasClone = entidadesSubcontratadasClone;
	}

	

	public Object getEntidadesSubcontratadasClone(String key) {
		return entidadesSubcontratadasClone.get(key);
	}

	public void setEntidadesSubcontratadasClone(String key, Object value) {
		this.entidadesSubcontratadasClone.put(key, value);
	}

	
	
	/*-----------------------------------------------
	 * 		 	FIN METODOS SETTERS AND GETTERS
	 ------------------------------------------------*/
	
	/*-----------------------------------------------
	 * 		 	METODOS ADICIONALES
	 ------------------------------------------------*/
	
	public void reset ()
	{
		this.entidadesSubContratadas = new HashMap ();
		this.setEntidadesSubContratadas("numRegistros", 0);
		this.setEntidadesSubContratadas(indices[10], "N");
		this.setEntidadesSubContratadas("seccionInsertar", "S");
		this.setOperacionTrue(false);
		this.setIndexDestino("");
		this.setIndexEliminado("");
		this.origen = "";
		this.cuentaCxp="";
		this.usuariosMap=new HashMap<String,Object>();
		this.usuariosMap.put("numRegistros", "0");
		this.usuariosSelMap=new HashMap<String,Object>();
		this.usuariosSelMap.put("numRegistros", "0");
		this.usuariosEntidadSub=new HashMap<String,Object>();
		this.usuariosEntidadSub.put("numRegistros", "0");
		
		this.indiceElimUsu="";
		this.activo=false;
		this.diasVenFact=0;
		this.usuarios="";
		
//		this.mapCentrosAtencion = new HashMap();
//		this.centroAtencionCub = "";
		
		this.unRegistro=false;
		this.mapViasIngreso=new HashMap();
		this.viasIngreso="";
		this.estancia="";
		this.dtoEstanciaViaIngresoCentroCosto = new DTOEstanciaViaIngCentroCosto();
		this.listaEstanciaviaIngCentroCosto = new ArrayList<DTOEstanciaViaIngCentroCosto>();
		this.listaCentroCosto = new ArrayList<DtoCentroCostosVista>();
		this.codCentroCosto=ConstantesBD.codigoNuncaValido;
		this.nombreTemporalTercero="";
		this.codigoTemporalTercero="";
	}

	
	public void resetDetalle ()
	{
		this.detalle = new HashMap();
		this.setDetalle("numRegistros", 0);
		this.detalleClone = new HashMap ();
		this.detalleEliminado = new HashMap ();
		this.setDetalleEliminado("numRegistros", 0);
		this.esqTarInv = new ArrayList ();
		this.esqTarServ = new ArrayList ();
		
//		this.mapCentrosAtencion = new HashMap();
//		this.centroAtencionCub = "";
		
		this.mapViasIngreso=new HashMap();
		this.viasIngreso="";
		this.estancia="";
		this.codCentroCosto=ConstantesBD.codigoNuncaValido;
	}
	
	public void resetBusqueda ()
	{		
		this.criteriosBusqueda = new HashMap ();
		this.setCriteriosBusqueda("numRegistros", 0);
		this.setCriteriosBusqueda("seccionBusquedaAvanzada", "S");
		this.setCriteriosBusqueda(indices[1], "");
		this.setCriteriosBusqueda(indices[3], "");
		this.setCriteriosBusqueda(indices[4], "");
		this.resultBusqueda = new HashMap ();
		this.setResultBusqueda("numRegistros", 0);
		this.resultBusquedaEliminado = new HashMap ();
		this.setResultBusquedaEliminado("numRegistros", 0);
		this.usuarios="";
		
//		this.mapCentrosAtencion = new HashMap();
//		this.centroAtencionCub = "";
		
		this.mapViasIngreso=new HashMap();
		this.viasIngreso="";
		this.estancia="";
		this.codCentroCosto=ConstantesBD.codigoNuncaValido;
	}
	
	public void resetBusq ()
	{
		this.resultBusqueda = new HashMap ();
		this.setResultBusqueda("numRegistros", 0);
		this.resultBusquedaEliminado = new HashMap ();
		this.setResultBusquedaEliminado("numRegistros", 0);
		this.entidadesSubContratadas = new HashMap ();
		this.setEntidadesSubContratadas("numRegistros", 0);
		this.setEntidadesSubContratadas("seccionInsertar", "N");
		this.setEntidadesSubContratadas(indices[10], "N");
		this.setOperacionTrue(false);
		this.entidadesSubcontratadasClone = new HashMap ();
		this.setEntidadesSubcontratadasClone("numRegistros", 0);
		
//		this.mapCentrosAtencion = new HashMap();
//		this.centroAtencionCub = "";
		
		this.mapViasIngreso=new HashMap();
		this.viasIngreso="";
		this.estancia="";
		this.codCentroCosto=ConstantesBD.codigoNuncaValido;
	}

	public void resetPager ()
	{
		this.linkSiguiente = "";
		this.ultimoPatron = "";
		this.patronOrdenar = "";
	}

	/*-----------------------------------------------
	 * 		 	FIN METODOS ADICIONALES
	 ------------------------------------------------*/
	
	
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		errores=super.validate(mapping, request);
		
		HashMap entidades = new HashMap ();
		
		
		
		
		if (estado.equals("guardar"))
		{
			/*------------------------------------------------------------
			 * En esta seccion se valida que los datos no vengan vacios
			 ----------------------------------------------------------------*/
			//codigo
			if(this.getEntidadesSubContratadas(indices[1]).toString().trim().equals(""))
			{
				errores.add("descripcion",new ActionMessage("errors.required","El Código "));
			}
			//Si se ingresó codigo se verifica que no esté repetido (Si no existe en la base de datos)
			else 
			{
				boolean ingresado = false;
				for(int i=0;i<Utilidades.convertirAEntero(this.resultBusqueda.get("numRegistros")+"");i++)
				{
					if(this.resultBusqueda.get(indices[22]+i).toString().equals(this.getEntidadesSubContratadas(indices[1]).toString())&&
						!this.resultBusqueda.get(indices[25]+i).toString().equals(this.getEntidadesSubContratadas(indices[0])+""))
					{
						ingresado = true;
					}
				}
				if(ingresado)
				{
					errores.add("descripcion",new ActionMessage("errors.yaExiste","El Código: "+this.getEntidadesSubContratadas(indices[1])));
				}
			}
			//razon  social
			if(this.getEntidadesSubContratadas(indices[3]).toString().trim().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Razón Social "));
			//numero identificacion
			if(this.getEntidadesSubContratadas(indices[4]).toString().trim().equals(""))
			{
				errores.add("descripcion",new ActionMessage("errors.required","El Número de Identificación "));
			}
			//codigo minsalud
			if(this.getEntidadesSubContratadas(indices[5]).toString().trim().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","El Código Minsalud "));
			if((this.diasVenFact>=360) || (this.diasVenFact<0))
			{
				errores.add("descripcion",new ActionMessage("errors.range","los dias de vencimiento de la factura","0","360"));
			}
//			Se eliminan estos atributos por incidencia Mantis 1221
//			if(this.getCentroAtencionCub().equals(ConstantesBD.codigoNuncaValido+""))
//			{
//				errores.add("centAtencion",new ActionMessage("errors.required","El Centro de Atención cubierto "));
//			}
			
			if(this.getEntidadesSubContratadas(indices[35]).toString().trim().equals(""))
			{
				errores.add("descripcion",new ActionMessage("errors.required","Permite Estancia de Pacientes "));
			}
		
			if(this.getEntidadesSubContratadas() != null && this.getEntidadesSubContratadas().get("estancia35_0") != null){	
		       String estancia= this.getEntidadesSubContratadas().get("estancia35_0").toString();
			   if(estancia.equals(ConstantesBD.acronimoSi)){
				   request.setAttribute("mostrar", "block");
			   }
			   else{
				   request.setAttribute("mostrar", "");
			   }
			}
		}
		
		return errores;
	}

//	/**
//	 * @return the mapCentrosAtencion
//	 */
//	public HashMap getMapCentrosAtencion() {
//		return mapCentrosAtencion;
//	}
//
//	/**
//	 * @param mapCentrosAtencion the mapCentrosAtencion to set
//	 */
//	public void setMapCentrosAtencion(HashMap mapCentrosAtencion) {
//		this.mapCentrosAtencion = mapCentrosAtencion;
//	}
//
//	/**
//	 * @return the centroAtencionCub
//	 */
//	public String getCentroAtencionCub() {
//		return centroAtencionCub;
//	}
//
//	/**
//	 * @param centroAtencionCub the centroAtencionCub to set
//	 */
//	public void setCentroAtencionCub(String centroAtencionCub) {
//		this.centroAtencionCub = centroAtencionCub;
//	}

	public boolean isUnRegistro() {
		return unRegistro;
	}

	public void setUnRegistro(boolean unRegistro) {
		this.unRegistro = unRegistro;
	}


	public String getViasIngreso() {
		return viasIngreso;
	}

	public void setViasIngreso(String viasIngreso) {
		this.viasIngreso = viasIngreso;
	}

	public HashMap getMapViasIngreso() {
		return mapViasIngreso;
	}

	public void setMapViasIngreso(HashMap mapViasIngreso) {
		this.mapViasIngreso = mapViasIngreso;
	}

	public void setEstancia(String estancia) {
		this.estancia = estancia;
	}

	public String getEstancia() {
		return estancia;
	}

	/**
	 * @return Retorna el atributo listaEstanciaviaIngCentroCosto
	 */
	public List<DTOEstanciaViaIngCentroCosto> getListaEstanciaviaIngCentroCosto() {
		return listaEstanciaviaIngCentroCosto;
	}

	/**
	 * @param listaEstanciaviaIngCentroCosto Asigna el atributo listaEstanciaviaIngCentroCosto
	 */
	public void setListaEstanciaviaIngCentroCosto(
			List<DTOEstanciaViaIngCentroCosto> listaEstanciaviaIngCentroCosto) {
		this.listaEstanciaviaIngCentroCosto = listaEstanciaviaIngCentroCosto;
	}

	/**
	 * @return Retorna el atributo dtoEstanciaViaIngresoCentroCosto
	 */
	public DTOEstanciaViaIngCentroCosto getDtoEstanciaViaIngresoCentroCosto() {
		return dtoEstanciaViaIngresoCentroCosto;
	}

	/**
	 * @param dtoEstanciaViaIngresoCentroCosto Asigna el atributo dtoEstanciaViaIngresoCentroCosto
	 */
	public void setDtoEstanciaViaIngresoCentroCosto(
			DTOEstanciaViaIngCentroCosto dtoEstanciaViaIngresoCentroCosto) {
		this.dtoEstanciaViaIngresoCentroCosto = dtoEstanciaViaIngresoCentroCosto;
	}

	/**
	 * @return Retorna el atributo listaCentroCosto
	 */
	public List<DtoCentroCostosVista> getListaCentroCosto() {
		return listaCentroCosto;
	}

	/**
	 * @param listaCentroCosto Asigna el atributo listaCentroCosto
	 */
	public void setListaCentroCosto(List<DtoCentroCostosVista> listaCentroCosto) {
		this.listaCentroCosto = listaCentroCosto;
	}

	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}

	public String getCentroCosto() {
		return centroCosto;
	}

	public void setIndiceEliminarViasCentros(int indiceEliminarViasCentros) {
		this.indiceEliminarViasCentros = indiceEliminarViasCentros;
	}

	public int getIndiceEliminarViasCentros() {
		return indiceEliminarViasCentros;
	}

	public void setCodCentroCosto(int codCentroCosto) {
		this.codCentroCosto = codCentroCosto;
	}

	public int getCodCentroCosto() {
		return codCentroCosto;
	}

	public void setNombreTemporalTercero(String nombreTemporalTercero) {
		this.nombreTemporalTercero = nombreTemporalTercero;
	}

	public String getNombreTemporalTercero() {
		return nombreTemporalTercero;
	}

	public void setCodigoTemporalTercero(String codigoTemporalTercero) {
		this.codigoTemporalTercero = codigoTemporalTercero;
	}

	public String getCodigoTemporalTercero() {
		return codigoTemporalTercero;
	}
	
	
}