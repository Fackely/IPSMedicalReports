package com.princetonsa.actionform.tramiteReferencia;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.UsuarioBasico;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.Utilidades;

import java.util.HashMap;

public class TramiteReferenciaForm extends ValidatorForm
{
	Logger logger = Logger.getLogger(TramiteReferenciaForm.class);
	
	//-----------------------Atributos
	/**
	 * HashMap de listado de tramites  
	 * */	
	private HashMap listarTramiteMap;
	
	/**
	 * HashMap de consulta de tramites de Referencia  
	 * */	
	private HashMap referenciaMap;
	
	/**
	 * String estado del Action
	 * */
	private String estado;
	
	/**
	 * String Codigo del Tramite
	 * */
	private String indexTramiteMap;
	
	/**
	 * HashMap Servicios de la Referencia 
	 * */
	private HashMap serviciosReferencia;
	
	/**
	 * HashMap centro de Atencion
	 * */
	private HashMap centroAtencionMap;
	
	/**
	 * String Index de Centro de Atencion
	 * */
	private String indexCentroAtencion;
	
	
	//********************************** SECCION INSTITUCIONES DE REFERENCIA	
	/**
	 * Boolean seccionInstitucionReferencia
	 * */
	private boolean seccionInstitucionReferencia;
	  
	//********************************** FIN SECCION INSTITUCIONES DE REFERENCIA
		
	
	//********************************** SECCION DATOS HISTORIA CLINICA
	/**
	 * HashMap de Otros Datps Historia Clinica 
	 * */
	private HashMap otrosHistoriaClinicaMap;	
	
	/**
	 * HashMap de Signos Vitales
	 * */
	private HashMap signosVitalesMap;
	
	/**
	 * HashMap Resultados
	 * */
	private HashMap resultadosMap;
	
	/**
	 * HashMap Diagnosticos
	 * */
	private HashMap diagnosticoMap;	
	
	/**
	 * Boolean seccionInstitucionReferencia
	 * */
	private boolean seccionHistoriaClinica;
	  
	//********************************** FIN SECCION DATOS HISTORIA CLINICA
	
	
	//********************************** SECCION TRAMITE
	/**
	 * HashMap Tramite 
	 * */
	private HashMap tramiteMap;	
	
	/**
	 * HashMap instituciones tramite
	 * */
	private HashMap institucionTramiteMap;
	
	/**
	 * HashMap Translado paciente
	 * */
	private HashMap trasladoPacienteMap;
	
	/**
	 * HashMap Historial Traslado Paciente
	 * */
	private HashMap trasladoPacienteHistorialMap;
			
	/**
	 * Boolean seccionInstitucionReferencia
	 * */
	private boolean seccionTramite;
	
	/**
	 * String almance los codigod de las instituciones SIRC que han sido insertadas desde la busqueda
	 * */
	private String codigosInstitucionesInsertados;	
	
	/**
	 * Index de posicion del mapa de Translado Paciente
	 * */
	private String indexInstTrasladoPacienteMap;
	  
	//********************************** FIN SECCION TRAMITE
	
	//********************************** SECCION SERVICIOS SIRC
	/**
	 * HashMap de Detalle Tramite
	 * */
	private HashMap serviciosInstitucionReferenciaMap;
	
	/**
	 * HashMap del historial de servicios institucion historial
	 * */
	private HashMap serviciosInstitucionHistorialMap;
	
	/**
	 * Index de posicion del mapa institucion tramite para el detalle
	 * */
	private String indexInstitucionTramiteMap;
	
	/**
	 * tipo de Red de la Institucion tramite para el datalle
	 * */
	private String tipoRedInstitucionTramiteMap;
	
	/**
	 * HashMap motivos Sirc
	 * */
	private HashMap motivosSirc;
	
	/**
	 * HasMap ciudades
	 * */
	private ArrayList ciudades;
	
	//********************************** FIN SECCION SERVICIOS SIRC
	
	//********************************** VALORES DEL TRASLADO PACIENTE
	/**
	 * codigo de la institucion a mostrar 
	 * */
	private String codigoInst;
	
	/**
	 * nombre de la institucion a mostrar 
	 * */
	private String nombreInst;
	
	/**
	 * Estado de la Base de Datos del Traslado de Paciente
	 * */
	private String estabdTraslado;
	
	/**
	 * Estado de Editar o no el Traslado del Paciente
	 * */
	private String checkTraslado;
	
	/**
	 * Estado del Popup de Traslado Paciente
	 * */
	private String estadoPopupTraslado;
	
	/**
	 * Codigos de Instituciones Traslado Paciente Insertados
	 * */
	private String codigosInsTrasladosInsertados;
	
	//********************************** FIN VALORES TRASLADO
	
	//********************************** VALORES DEL RESUMEN 
	/**
	 * Id del ingreso del paciente 
	 * */
	private int idIngresoPaciente;
	//********************************** FIN VALORES DEL RESUMEN  

	
	//********************************** VALORES PARA EL PAGER
	
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
	
	//************************************FIN VALORES DE PAGER	
	
	/**
	 * 
	 */
	private String botonVolver;
	
	//-----------------------Fin Atributos
	
	
	//-----------------------Metodos
	/**
     * Validate the properties that have been set from this HTTP request, and
     * return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no recorded
     * error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
    */
	public ActionErrors validate (ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores = new ActionErrors();	
		
		//estado de volver a tramitar, proviene de servicios institucion SIRC 
		if(estado.equals("volverTramitar"))
		{	
			int numRegistros = Integer.parseInt(this.institucionTramiteMap.get("numRegistrosServicios_"+this.indexInstitucionTramiteMap).toString());			
			
			if(this.institucionTramiteMap.get("departamentoreferir_"+this.indexInstitucionTramiteMap).toString().equals(" "+ConstantesBD.separadorSplit+" "+ConstantesBD.separadorSplit+" "))
				errores.add("descripcion",new ActionMessage("errors.required","La Ciudad del Tramite "));
			
			if(this.institucionTramiteMap.get("paisreferir_"+this.indexInstitucionTramiteMap).toString().equals(" - "))
				errores.add("descripcion",new ActionMessage("errors.required","El pais del Tramite "));
			
			for(int i=0; i<numRegistros; i++)
			{
				if(this.institucionTramiteMap.get("editar_"+this.indexInstitucionTramiteMap+"_"+i+"").toString().equals(ConstantesBD.acronimoSi))
				{
					if(this.institucionTramiteMap.get("activo_"+this.indexInstitucionTramiteMap+"_"+i).toString().equals(ConstantesBD.acronimoSi))
					{
						if(this.institucionTramiteMap.get("estado_"+this.indexInstitucionTramiteMap+"_"+i).toString().equals(ConstantesBD.codigoNuncaValido+""))
							errores.add("descripcion",new ActionMessage("errors.required","El Estado del Servicio SIRC "+(i+1)));
						
						if((this.institucionTramiteMap.get("estado_"+this.indexInstitucionTramiteMap+"_"+i).toString().equals(ConstantesIntegridadDominio.acronimoEstadoCancelado)) || (this.institucionTramiteMap.get("estado_"+this.indexInstitucionTramiteMap+"_"+i).toString().equals(ConstantesIntegridadDominio.acronimoEstadoNegado)))
						{
							if(this.institucionTramiteMap.get("motivo_"+this.indexInstitucionTramiteMap+"_"+i).toString().equals(ConstantesBD.codigoNuncaValido+""))
								errores.add("descripcion",new ActionMessage("errors.required","El Motivo del Servicio SIRC "+(i+1)));
						}	
						
						if(this.institucionTramiteMap.get("funcionariocontactado_"+this.indexInstitucionTramiteMap+"_"+i).toString().equals(""))
							errores.add("descripcion",new ActionMessage("errors.required","El Funcionario Contactado del Servicio SIRC "+(i+1)));						
						
						if(this.institucionTramiteMap.get("cargo_"+this.indexInstitucionTramiteMap+"_"+i).toString().equals(""))
							errores.add("descripcion",new ActionMessage("errors.required","El Cargo del Funcionario Contactado del Servicio SIRC "+(i+1)));						
												
						
						if((!this.institucionTramiteMap.get("fechatramite_"+this.indexInstitucionTramiteMap+"_"+i).toString().equals("")) && (!this.institucionTramiteMap.get("horatramite_"+this.indexInstitucionTramiteMap+"_"+i).toString().equals("")))
						{
							if(!UtilidadFecha.validarFecha(this.institucionTramiteMap.get("fechatramite_"+this.indexInstitucionTramiteMap+"_"+i).toString()))
								errores.add("descripcion",new ActionMessage("errors.invalid",this.institucionTramiteMap.get("fechatramite_"+this.indexInstitucionTramiteMap+"_"+i).toString()+"  "+this.institucionTramiteMap.get("horatramite_"+this.indexInstitucionTramiteMap+"_"+i).toString()+" En el Servicio SIRC "+(i+1)));
							
							if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),this.institucionTramiteMap.get("fechatramite_"+this.indexInstitucionTramiteMap+"_"+i).toString(),this.institucionTramiteMap.get("horatramite_"+this.indexInstitucionTramiteMap+"_"+i).toString()).isTrue())
								errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.institucionTramiteMap.get("fechatramite_"+this.indexInstitucionTramiteMap+"_"+i).toString()+"  "+this.institucionTramiteMap.get("horatramite_"+this.indexInstitucionTramiteMap+"_"+i).toString()+" En el Servicio SIRC "+(i+1),UtilidadFecha.getFechaActual()+" "+UtilidadFecha.getHoraActual()));
						}
						else
							errores.add("descripcion",new ActionMessage("errors.required","La Fecha en el  Servicio SIRC "+(i+1)));
							
						
						if((this.institucionTramiteMap.get("tipored_"+this.getIndexInstitucionTramiteMap()).toString().equals(ConstantesIntegridadDominio.acronimoRN)) && (this.institucionTramiteMap.get("estado_"+this.indexInstitucionTramiteMap+"_"+i).toString().equals(ConstantesIntegridadDominio.acronimoEstadoAceptado)))
						{
							if(this.institucionTramiteMap.get("numeroverificacion_"+this.indexInstitucionTramiteMap+"_"+i).toString().equals(""))
								errores.add("descripcion",new ActionMessage("errors.required","El Numero de Verificacion del Servicio SIRC "+(i+1)));								
						}	
					}
				}
			}			
		}		
		return errores;
	}	

 
	 /**
	 * Inicializa los atributos de la clase
	 * */
	public void reset()
	{
		this.listarTramiteMap = new HashMap();
		this.referenciaMap = new HashMap();
		this.otrosHistoriaClinicaMap = new HashMap();
		this.signosVitalesMap = new HashMap();
		this.resultadosMap = new HashMap();
		this.diagnosticoMap = new HashMap();
		this.institucionTramiteMap = new HashMap();		
		this.serviciosInstitucionReferenciaMap = new HashMap();
		this.trasladoPacienteMap = new HashMap();	    
		this.trasladoPacienteMap.put("numRegistros","0");
		this.motivosSirc = new HashMap();
		this.idIngresoPaciente = ConstantesBD.codigoNuncaValido;		
		
		this.tramiteMap = new HashMap();
		this.estado = "";
		this.ultimoPatron ="";
		this.patronOrdenar = "";
		this.linkSiguiente = "";
		this.seccionHistoriaClinica=false;
		this.seccionInstitucionReferencia=false;
		this.seccionTramite=false;
		this.botonVolver="";
	}

	
	/**
	 * Inicializa los atributos para cargar nuevamente
	 * */
	public void iniciar()
	{
		this.seccionHistoriaClinica=false;
		this.seccionInstitucionReferencia=false;
		this.seccionTramite=false;
	}
	
	
	/**
	 * Inicializa los centros de Atencion 
	 * @param int institucion
	 * */
	public void iniciarCentroAtencion(int codigoInstitucion)
	{	
		this.centroAtencionMap =Utilidades.obtenerCentrosAtencion(codigoInstitucion);		
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
	 * @return the listarTramiteMap
	 */
	public HashMap getListarTramiteMap() {
		return listarTramiteMap;
	}


	/**
	 * @param listarTramiteMap the listarTramiteMap to set
	 */
	public void setListarTramiteMap(HashMap listarTramiteMap) {
		this.listarTramiteMap = listarTramiteMap;
	}
	
	/**
	 * @param listarTramiteMap the listarTramiteMap to set
	 */
	public void updateListarTramiteMap(HashMap listarTramiteMap) {
		this.listarTramiteMap.clear();
		this.listarTramiteMap = listarTramiteMap;
	}
	
	/**
	 * @return the listarTramiteMap
	 */
	public Object getListarTramiteMap(String key) {
		return listarTramiteMap.get(key);
	}


	/**
	 * @param listarTramiteMap the listarTramiteMap to set
	 */
	public void setListarTramiteMap(String key, Object value) {
		this.listarTramiteMap.put(key, value);
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
	 * @return the indexTramiteMap
	 */
	public String getIndexTramiteMap() {
		return indexTramiteMap;
	}


	/**
	 * @param indexTramiteMap the indexTramiteMap to set
	 */
	public void setIndexTramiteMap(String indexTramiteMap) {
		this.indexTramiteMap = indexTramiteMap;
	}


	/**
	 * @return the seccionInstitucionReferencia
	 */
	public boolean isSeccionInstitucionReferencia() {
		return seccionInstitucionReferencia;
	}


	/**
	 * @param seccionInstitucionReferencia the seccionInstitucionReferencia to set
	 */
	public void setSeccionInstitucionReferencia(boolean seccionInstitucionReferencia) {
		this.seccionInstitucionReferencia = seccionInstitucionReferencia;
	}


	/**
	 * @return the seccionHistoriaClinica
	 */
	public boolean isSeccionHistoriaClinica() {
		return seccionHistoriaClinica;
	}


	/**
	 * @param seccionHistoriaClinica the seccionHistoriaClinica to set
	 */
	public void setSeccionHistoriaClinica(boolean seccionHistoriaClinica) {
		this.seccionHistoriaClinica = seccionHistoriaClinica;
	}


	/**
	 * @return the seccionTramite
	 */
	public boolean isSeccionTramite() {
		return seccionTramite;
	}


	/**
	 * @param seccionTramite the seccionTramite to set
	 */
	public void setSeccionTramite(boolean seccionTramite) {
		this.seccionTramite = seccionTramite;
	}	
	
	/**
	 * @return the TramiteReferenciaMap
	 */
	public HashMap getReferenciaMap() {
		return referenciaMap;
	}


	/**
	 * @param TramiteMap the listarTramiteMap to set
	 */
	public void setReferenciaMap(HashMap referenciaMap) {
		this.referenciaMap = referenciaMap;
	}
	
	/**
	 * @param TramiteMap the listarTramiteMap to set
	 */
	public void updateReferenciaMap(HashMap referenciaMap) {
		this.referenciaMap.clear();
		this.referenciaMap = referenciaMap;
	}
	
	/**
	 * @return the TramiteMap
	 */
	public Object getReferenciaMap(String key) {
		return referenciaMap.get(key);
	}


	/**
	 * @param TramiteMap the listarTramiteMap to set
	 */
	public void setReferenciaMap(String key, Object value) {
		this.referenciaMap.put(key, value);
	}


	/**
	 * @return the diagnosticoMap
	 */
	public HashMap getDiagnosticoMap() {
		return diagnosticoMap;
	}


	/**
	 * @param diagnosticoMap the diagnosticoMap to set
	 */
	public void setDiagnosticoMap(HashMap diagnosticoMap) {
		this.diagnosticoMap = diagnosticoMap;
	}
	
	/**
	 * @return the diagnosticoMap
	 */
	public Object getDiagnosticoMap(String key) {
		return diagnosticoMap.get(key);
	}


	/**
	 * @param diagnosticoMap the diagnosticoMap to set
	 */
	public void setDiagnosticoMap(String key, Object value) {
		this.diagnosticoMap.put(key, value);
	}



	/**
	 * @return the resultadosMap
	 */
	public HashMap getResultadosMap() {
		return resultadosMap;
	}


	/**
	 * @param resultadosMap the resultadosMap to set
	 */
	public void setResultadosMap(HashMap resultadosMap) {
		this.resultadosMap = resultadosMap;
	}
	
	/**
	 * @return the resultadosMap
	 */
	public Object getResultadosMap(String key) {
		return resultadosMap.get(key);
	}


	/**
	 * @param resultadosMap the resultadosMap to set
	 */
	public void setResultadosMap(String key, Object value) {
		this.resultadosMap.put(key, value);
	}



	/**
	 * @return the signosVitalesMap
	 */
	public HashMap getSignosVitalesMap() {
		return signosVitalesMap;
	}


	/**
	 * @param signosVitalesMap the signosVitalesMap to set
	 */
	public void setSignosVitalesMap(HashMap signosVitalesMap) {
		this.signosVitalesMap = signosVitalesMap;
	}
	
	/**
	 * @return the signosVitalesMap
	 */
	public Object getSignosVitalesMap(String key) {
		return signosVitalesMap.get(key);
	}


	/**
	 * @param signosVitalesMap the signosVitalesMap to set
	 */
	public void setSignosVitalesMap(String key, Object value) {
		this.signosVitalesMap.put(key, value);
	}


	/**
	 * @return the otrosHistoriaClinicaMap
	 */
	public HashMap getOtrosHistoriaClinicaMap() {
		return otrosHistoriaClinicaMap;
	}


	/**
	 * @param otrosHistoriaClinicaMap the otrosHistoriaClinicaMap to set
	 */
	public void setOtrosHistoriaClinicaMap(HashMap otrosHistoriaClinicaMap) {
		this.otrosHistoriaClinicaMap = otrosHistoriaClinicaMap;
	}
	
	/**
	 * @return the otrosHistoriaClinicaMap
	 */
	public Object getOtrosHistoriaClinicaMap(String key) {
		return otrosHistoriaClinicaMap.get(key);
	}


	/**
	 * @param otrosHistoriaClinicaMap the otrosHistoriaClinicaMap to set
	 */
	public void setOtrosHistoriaClinicaMap(String key, Object value) {
		this.otrosHistoriaClinicaMap.put(key, value);
	}

	/**
	 * @return the tramiteMap
	 */
	public HashMap getTramiteMap() {
		return tramiteMap;
	}


	/**
	 * @param tramiteMap the tramiteMap to set
	 */
	public void setTramiteMap(HashMap tramiteMap) {
		this.tramiteMap = tramiteMap;
	}
	
	/**
	 * @return the tramiteMap
	 */
	public Object getTramiteMap(String key) {
		return tramiteMap.get(key);
	}


	/**
	 * @param tramiteMap the tramiteMap to set
	 */
	public void setTramiteMap(String key, Object value) {
		this.tramiteMap.put(key, value);
	}


	/**
	 * @return the institucionTramiteMap
	 */
	public HashMap getInstitucionTramiteMap() {
		return institucionTramiteMap;
	}


	/**
	 * @param institucionTramiteMap the institucionTramiteMap to set
	 */
	public void setInstitucionTramiteMap(HashMap institucionTramiteMap) {
		this.institucionTramiteMap = institucionTramiteMap;
	}
	
	/**
	 * @return the institucionTramiteMap
	 */
	public Object getInstitucionTramiteMap(String key){
		return institucionTramiteMap.get(key);
	}


	/**
	 * @param institucionTramiteMap the institucionTramiteMap to set
	 */
	public void setInstitucionTramiteMap(String key, Object value) {
		this.institucionTramiteMap.put(key, value);
	}


	/**
	 * @return the codigosInstitucionesInsertados
	 */
	public String getCodigosInstitucionesInsertados() {
		return codigosInstitucionesInsertados;
	}


	/**
	 * @param codigosInstitucionesInsertados the codigosInstitucionesInsertados to set
	 */
	public void setCodigosInstitucionesInsertados(
			String codigosInstitucionesInsertados) {
		this.codigosInstitucionesInsertados = codigosInstitucionesInsertados;
	}



	/**
	 * @return the tramiteDetalleMap
	 */
	public HashMap getServiciosInstitucionReferenciaMap() {
		return serviciosInstitucionReferenciaMap;
	}


	/**
	 * @param tramiteDetalleMap the tramiteDetalleMap to set
	 */
	public void setServiciosInstitucionReferenciaMap(HashMap serviciosInstitucionReferenciaMap) {
		this.serviciosInstitucionReferenciaMap = serviciosInstitucionReferenciaMap;
	}
	
	/**
	 * @return the tramiteDetalleMap
	 */
	public Object getServiciosInstitucionReferenciaMap(String key) {
		return serviciosInstitucionReferenciaMap.get(key);
	}


	/**
	 * @param tramiteDetalleMap the tramiteDetalleMap to set
	 */
	public void setServiciosInstitucionReferenciaMap(String key, Object value) {
		this.serviciosInstitucionReferenciaMap.put(key,value);
	}	
	//-----------------------Fin Metodos


	/**
	 * @return the indexInstitucionTramiteMap
	 */
	public String getIndexInstitucionTramiteMap() {
		return indexInstitucionTramiteMap;
	}


	/**
	 * @param indexInstitucionTramiteMap the indexInstitucionTramiteMap to set
	 */
	public void setIndexInstitucionTramiteMap(String indexInstitucionTramiteMap) {
		this.indexInstitucionTramiteMap = indexInstitucionTramiteMap;
	}


	/**
	 * @return the serviciosReferencia
	 */
	public HashMap getServiciosReferencia() {
		return serviciosReferencia;
	}


	/**
	 * @param serviciosReferencia the serviciosReferencia to set
	 */
	public void setServiciosReferencia(HashMap serviciosReferencia) {
		this.serviciosReferencia = serviciosReferencia;
	}
	
	/**
	 * @return the serviciosReferencia
	 */
	public Object getServiciosReferencia(String key) {
		return serviciosReferencia.get(key);
	}


	/**
	 * @param serviciosReferencia the serviciosReferencia to set
	 */
	public void setServiciosReferencia(String key, Object value) {
		this.serviciosReferencia.put(key, value);
	}


	/**
	 * @return the motivosSirc
	 */
	public HashMap getMotivosSirc() {
		return motivosSirc;
	}


	/**
	 * @param motivosSirc the motivosSirc to set
	 */
	public void setMotivosSirc(HashMap motivosSirc) {
		this.motivosSirc = motivosSirc;
	}
	
	/**
	 * @return the motivosSirc
	 */
	public Object getMotivosSirc(String key) {
		return motivosSirc.get(key);
	}


	/**
	 * @param motivosSirc the motivosSirc to set
	 */
	public void setMotivosSirc(String key, Object value) {
		this.motivosSirc.put(key, value);
	}


	/**
	 * @return the ciudades
	 */
	public ArrayList getCiudades() {
		return ciudades;
	}


	/**
	 * @param ciudades the ciudades to set
	 */
	public void setCiudades(ArrayList ciudades) {
		this.ciudades = ciudades;
	}


	/**
	 * @return the serviciosInstitucionHistorialMap
	 */
	public HashMap getServiciosInstitucionHistorialMap() {
		return serviciosInstitucionHistorialMap;
	}


	/**
	 * @param serviciosInstitucionHistorialMap the serviciosInstitucionHistorialMap to set
	 */
	public void setServiciosInstitucionHistorialMap(
			HashMap serviciosInstitucionHistorialMap) {
		this.serviciosInstitucionHistorialMap = serviciosInstitucionHistorialMap;
	}
	
	/**
	 * @return the serviciosInstitucionHistorialMap
	 */
	public Object getServiciosInstitucionHistorialMap(String key) {
		return serviciosInstitucionHistorialMap.get(key);
	}


	/**
	 * @param serviciosInstitucionHistorialMap the serviciosInstitucionHistorialMap to set
	 */
	public void setServiciosInstitucionHistorialMap(String key, Object value) {
		this.serviciosInstitucionHistorialMap.put(key, value);
	}


	/**
	 * @return the tipoRedInstitucionTramiteMap
	 */
	public String getTipoRedInstitucionTramiteMap() {
		return tipoRedInstitucionTramiteMap;
	}


	/**
	 * @param tipoRedInstitucionTramiteMap the tipoRedInstitucionTramiteMap to set
	 */
	public void setTipoRedInstitucionTramiteMap(String tipoRedInstitucionTramiteMap) {
		this.tipoRedInstitucionTramiteMap = tipoRedInstitucionTramiteMap;
	}


	/**
	 * @return the idIngresoPaciente
	 */
	public int getIdIngresoPaciente() {
		return idIngresoPaciente;
	}


	/**
	 * @param idIngresoPaciente the idIngresoPaciente to set
	 */
	public void setIdIngresoPaciente(int idIngresoPaciente) {
		this.idIngresoPaciente = idIngresoPaciente;
	}


	/**
	 * @return the trasladoPacienteMap
	 */
	public HashMap getTrasladoPacienteMap() {
		return trasladoPacienteMap;
	}


	/**
	 * @param trasladoPacienteMap the trasladoPacienteMap to set
	 */
	public void setTrasladoPacienteMap(HashMap trasladoPacienteMap) {
		this.trasladoPacienteMap = trasladoPacienteMap;
	}
	
	/**
	 * @return the trasladoPacienteMap
	 */
	public Object getTrasladoPacienteMap(String key) {
		return trasladoPacienteMap.get(key);
	}


	/**
	 * @param trasladoPacienteMap the trasladoPacienteMap to set
	 */
	public void setTrasladoPacienteMap(String key, Object value) {
		this.trasladoPacienteMap.put(key, value);
	}


	/**
	 * @return the indexInstTrasladoPacienteMap
	 */
	public String getIndexInstTrasladoPacienteMap() {
		return indexInstTrasladoPacienteMap;
	}


	/**
	 * @param indexInstTrasladoPacienteMap the indexInstTrasladoPacienteMap to set
	 */
	public void setIndexInstTrasladoPacienteMap(String indexInstTrasladoPacienteMap) {
		this.indexInstTrasladoPacienteMap = indexInstTrasladoPacienteMap;
	}


	/**
	 * @return the trasladoPacienteHistorialMap
	 */
	public HashMap getTrasladoPacienteHistorialMap() {
		return trasladoPacienteHistorialMap;
	}


	/**
	 * @param trasladoPacienteHistorialMap the trasladoPacienteHistorialMap to set
	 */
	public void setTrasladoPacienteHistorialMap(HashMap trasladoPacienteHistorialMap) {
		this.trasladoPacienteHistorialMap = trasladoPacienteHistorialMap;
	}
	
	/**
	 * @return the trasladoPacienteHistorialMap
	 */
	public Object getTrasladoPacienteHistorialMap(String key) {
		return trasladoPacienteHistorialMap.get(key);
	}


	/**
	 * @param trasladoPacienteHistorialMap the trasladoPacienteHistorialMap to set
	 */
	public void setTrasladoPacienteHistorialMap(String key, Object value) {
		this.trasladoPacienteHistorialMap.put(key, value);
	}


	/**
	 * @return the codigoInst
	 */
	public String getCodigoInst() {
		return codigoInst;
	}


	/**
	 * @param codigoInst the codigoInst to set
	 */
	public void setCodigoInst(String codigoInst) {
		this.codigoInst = codigoInst;
	}


	/**
	 * @return the nombreInst
	 */
	public String getNombreInst() {
		return nombreInst;
	}


	/**
	 * @param nombreInst the nombreInst to set
	 */
	public void setNombreInst(String nombreInst) {
		this.nombreInst = nombreInst;
	}


	/**
	 * @return the estabdTraslado
	 */
	public String getEstabdTraslado() {
		return estabdTraslado;
	}


	/**
	 * @param estabdTraslado the estabdTraslado to set
	 */
	public void setEstabdTraslado(String estabdTraslado) {
		this.estabdTraslado = estabdTraslado;
	}


	/**
	 * @return the checkTraslado
	 */
	public String getCheckTraslado() {
		return checkTraslado;
	}


	/**
	 * @param checkTraslado the checkTraslado to set
	 */
	public void setCheckTraslado(String checkTraslado) {
		this.checkTraslado = checkTraslado;
	}


	/**
	 * @return the estadoPopupTraslado
	 */
	public String getEstadoPopupTraslado() {
		return estadoPopupTraslado;
	}


	/**
	 * @param estadoPopupTraslado the estadoPopupTraslado to set
	 */
	public void setEstadoPopupTraslado(String estadoPopupTraslado) {
		this.estadoPopupTraslado = estadoPopupTraslado;
	}


	/**
	 * @return the codigosInsTrasladosInsertados
	 */
	public String getCodigosInsTrasladosInsertados() {
		return codigosInsTrasladosInsertados;
	}


	/**
	 * @param codigosInsTrasladosInsertados the codigosInsTrasladosInsertados to set
	 */
	public void setCodigosInsTrasladosInsertados(
			String codigosInsTrasladosInsertados) {
		this.codigosInsTrasladosInsertados = codigosInsTrasladosInsertados;
	}


	/**
	 * @return the botonVolver
	 */
	public String getBotonVolver() {
		return botonVolver;
	}


	/**
	 * @param botonVolver the botonVolver to set
	 */
	public void setBotonVolver(String botonVolver) {
		this.botonVolver = botonVolver;
	}


	/**
	 * @return the centroAtencionMap
	 */
	public HashMap getCentroAtencionMap() {
		return centroAtencionMap;
	}


	/**
	 * @param centroAtencionMap the centroAtencionMap to set
	 */
	public void setCentroAtencionMap(HashMap centroAtencionMap) {
		this.centroAtencionMap = centroAtencionMap;
	}
	
	/**
	 * @return the centroAtencionMap
	 */
	public Object getCentroAtencionMap(String key) {
		return centroAtencionMap.get(key);
	}


	/**
	 * @param centroAtencionMap the centroAtencionMap to set
	 */
	public void setCentroAtencionMap(String key, Object value) {
		this.centroAtencionMap.put(key, value);
	}


	/**
	 * @return the indexCentroAtencion
	 */
	public String getIndexCentroAtencion() {
		return indexCentroAtencion;
	}

	/**
	 * @param indexCentroAtencion the indexCentroAtencion to set
	 */
	public void setIndexCentroAtencion(String indexCentroAtencion) {
		this.indexCentroAtencion = indexCentroAtencion;
	}
}