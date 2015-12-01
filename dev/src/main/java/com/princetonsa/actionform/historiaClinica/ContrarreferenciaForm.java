package com.princetonsa.actionform.historiaClinica;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import java.util.ArrayList;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;


public class ContrarreferenciaForm extends ValidatorForm 
{
	/**
	 * Variable para manejar el estadp de la funcionalidad
	 */
	private String estado="";
	
	
	//// Inicio de la declaracion de variables para la Contarreferencia
	
	private String institucionOrigen;
	
	private String institucionDestino;
	
	private String fechaRemision;
	
	private String horaRemision;
	
	private String hallazgosClinicos;
	
	private HashMap resultadosProcedimientos;
	
	private HashMap resultadosProcedimientosEliminados;
	
	private String tratamientosInstaurados;
	
	private String recomendaciones;
	
	private String examenFisico;
	
	private String relacionAnexos;
	
	private int profesionalResponde;
	
	private String usuarioFinaliza;
	
	private String horaFinaliza;
	
	private String fechaFinaliza;
	
	
	/**
	 * 
	 */
	private int indice;
	
	
	/**
	 * Mapa de la contrarreferenia
	 */
	private HashMap contrarreferenciaMap;
	
	
	/**
	 * clon de contrarreferenciamap al momento de cargalos de la bd,
	 * utilizado para verificar si existieron modificaciones y para
	 * crear el log tipo archivo
	 */
	
	/*private HashMap contrarreferenciaEliminadosMap;*/
	
	
	/**
	 * para la navegacion del pager, cuando se ingresa
	 * un registro nuevo.
	 */
	
	private String linkSiguiente;
	
	/**
	 *  Para la navegacion del pager, cuando se ingresa
	 *  un registro nuevo.
	 */
	private int maxPageItems;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	
	/**
	 *  para controlar la página actual
     *  del pager.
	 */
	
	 private int offset;
	 
	 
	/**
	 * Posicion del registro que se eliminara
	 */
	    
	 private int posEliminar;
	 
	 
	 
	 
	 //////// Datos a tomar de la referencia - Contrarreferencia por Paciente
	 
	 private int referencia;
	 
	 private String fechaReferencia;
	 
	 private String horaReferencia;
	 
	 private String tipoAtencion;
	 
	 private String institucionDestinoReferencia;
	 
	 
	 
	 //////// Datos a tomar de la referencia - Contrarreferencia por Area
	 
	 private String solicitud;
	 
	 private int paciente;
	 
	 private HashMap codigoCentros;
	 
	 
	 private int centrosCosto;
	 
	 /*private int centrosCosto;*/
	 
	 
	 ///// Otros Funcionamiento
	 
	 private ArrayList tiposId=new ArrayList();
	 
	 private ArrayList profesionalesSalud=new ArrayList();
	 
	 private String estadoRegistro;
	 
	 private int ingreso;
	 
	 private int numeroReferenciaContra;
	 
	 private HashMap opcionesImpresionMap;	 
	 
	 
	 	 
	 //********ATRIBUTOS DIAGNÓSTICOS PROCEDIMIENTOS***************************
	/**
	 * Objeto que almacena los diagnósticos 
	 */
	private HashMap diagnosticos = new HashMap();
	/**
	 * Número de diagnósticos del procedimientos
	 */
	private int numDiagnosticos;
	
	
	//************************************************************************
	
	
	private HashMap conducta = new HashMap();
	
	
	/////********** Resultados Procedimientos **************////////////////
	
	private String tipoReferencia;
	
	private String procedimientosInsertados;
	
	private boolean seccionProcedimientosSolicitados;
	
	private HashMap institucionesSirc = new HashMap();
	
	private ArrayList tiposIdentificacion = new ArrayList();
	
	private HashMap conductasSeguir;
	
	private String seccionConductas;
	
	
	
	/**
	 * Consecutivo Disponible para la Contrarreferencia
	 */
	private int consecutivoInt;
	private String consecutivoAnio;
	
	private String estadoReferencia;
	
	
	private ArrayList centros;
	
	
	private int indexSeleccionado;
	
	
	private String nombreInstitucionOrigen;
	
	private String anioConsecutivoPuntoAtencion;
	private String consecutivoPuntoAtencion;
	
	private boolean esModificacion;
	
	
	private String botonVolver;
	
	
	/**
	 * Reset de la forma
	 *
	 */
	public void reset()
	{
		
		this.esModificacion=false;
		this.anioConsecutivoPuntoAtencion="";
		this.consecutivoPuntoAtencion="";
		this.institucionOrigen="";
		this.institucionDestino="";
		this.fechaRemision=UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual());
		this.horaRemision=UtilidadFecha.getHoraActual();
		this.hallazgosClinicos="";
		this.resultadosProcedimientos=new HashMap();
		this.resultadosProcedimientos.put("numRegistros", "0");
		this.resultadosProcedimientosEliminados=new HashMap();
		this.resultadosProcedimientosEliminados.put("numRegistros", "0");
		this.tratamientosInstaurados="";
		this.recomendaciones="";
		this.examenFisico="";
		this.relacionAnexos="";
		this.profesionalResponde=ConstantesBD.codigoNuncaValido;
		this.usuarioFinaliza="";
		this.horaFinaliza="";
		this.fechaFinaliza="";	
		
		this.referencia=ConstantesBD.codigoNuncaValido;
		this.fechaReferencia="";
		this.horaReferencia="";
		this.tipoAtencion="";
		this.institucionDestinoReferencia="";
		
		this.solicitud="";
		this.paciente=ConstantesBD.codigoNuncaValido;
		this.codigoCentros=new HashMap();
		/*this.centrosCosto=new ArrayList();*/
		
		this.contrarreferenciaMap=new HashMap();
		this.contrarreferenciaMap.put("numRegistros", "0");
		linkSiguiente="";
       	this.maxPageItems=10;
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	this.offset=0;
    	this.posEliminar=ConstantesBD.codigoNuncaValido;
    	
    	this.estadoRegistro=ConstantesIntegridadDominio.acronimoEstadoPendiente;
    	this.ingreso=ConstantesBD.codigoNuncaValido;
    	this.numeroReferenciaContra=ConstantesBD.codigoNuncaValido;
		this.opcionesImpresionMap=new HashMap();
    	
		this.diagnosticos = new HashMap();
		this.numDiagnosticos = 0;
		
		this.tipoReferencia="";
		this.procedimientosInsertados="";
		
		this.seccionProcedimientosSolicitados = false;
		
		this.institucionesSirc= new HashMap();
		this.conducta= new HashMap();
		
		this.tiposIdentificacion = new ArrayList();
		
		this.indice=ConstantesBD.codigoNuncaValido;
		
		this.tipoReferencia=ConstantesIntegridadDominio.acronimoExterna;
		
		this.conductasSeguir=new HashMap();
		this.conductasSeguir.put("numRegistros", "0");
		this.seccionConductas="";
		
		this.consecutivoInt = 0;
		this.consecutivoAnio = "";
		
		
		this.estadoReferencia="";
		
		this.centrosCosto=ConstantesBD.codigoNuncaValido;
		
		this.centros=new ArrayList();
		
		this.indexSeleccionado=ConstantesBD.codigoNuncaValido;
		
		this.nombreInstitucionOrigen="";
		
		this.botonVolver="";
	}

	
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if((this.estado.equals("guardar"))&&(this.getEstadoRegistro().equals(ConstantesIntegridadDominio.acronimoEstadoFinalizado)))
		{
			
			if(UtilidadTexto.isEmpty(this.getInstitucionOrigen()))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Institucion Origen es Requerida "));
			}
			if(UtilidadTexto.isEmpty(this.getFechaRemision()))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Fecha de Remision es Requerida "));
				boolean centinelaErrorFechas=false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaRemision()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Remision "+this.getFechaRemision()));
					centinelaErrorFechas=true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaRemision(), UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Remision "+this.getFechaRemision(), "Actual "+UtilidadFecha.getFechaActual()));
					}
				}
			}
			if(UtilidadTexto.isEmpty(this.getHoraRemision()))
			{
				errores.add("codigo", new ActionMessage("errors.required","La Hora de Remision "));
				
			}
			if(this.getHallazgosClinicos().equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","Los Hallazgos Clinicos "));
				
			}
			if(this.getExamenFisico().equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","Examen Fisico "));
				
			}
			if(this.getDiagnosticos("principal").toString().equals(""))
			{	
				errores.add("diagnostico principal es requerido", new ActionMessage("errors.required","El diagnóstico principal "));
			}
			if(this.getTratamientosInstaurados().equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","Tratamientos Instaurados "));
				
			}
			if(this.getRecomendaciones().equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","Recomendaciones "));
				
			}
			int seleccionados=0;
			for(int i=0;i<(Integer.parseInt(conductasSeguir.get("numRegistros")+""));i++)
			{
				if((conductasSeguir.get("activo_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
				{
					seleccionados++;
				}
			}
			if(seleccionados==0)
			{
				errores.add("codigo", new ActionMessage("errors.required","Conducta Seguir"));
			}
			//this.getRelacionAnexos().equals("");
						
		}
		if(estado.equals("guardar"))
		{
			if(this.getHallazgosClinicos().length()>4000)
			{
				errores.add("codigo", new ActionMessage("errors.maxlength","Hallazgos Clinicos","1000"));
			}
			if(this.getRecomendaciones().length()>1000)
			{
				errores.add("codigo", new ActionMessage("errors.maxlength","Recomendaciones","1000"));
			}
			if(this.getTratamientosInstaurados().length()>1000)
			{
				errores.add("codigo", new ActionMessage("errors.maxlength","Tratamientos Instaurados","1000"));
			}
			if(this.getRelacionAnexos().length()>1000)
			{
				errores.add("codigo", new ActionMessage("errors.maxlength","Relacion Anexos","1000"));
			}
			if(this.getExamenFisico().length()>1000)
			{
				errores.add("codigo", new ActionMessage("errors.maxlength","Examen Fisico","1000"));
			}
		}
		return errores;
	}
	
	
			
	/**
	 * 
	 * @return
	 */
	public String getEstado() {
		return estado;
	}


	
	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	
	
	/**
	 * 
	 * @return
	 */
	public String getRecomendaciones() {
		return recomendaciones;
	}


	
	/**
	 * 
	 * @param recomendaciones
	 */
	public void setRecomendaciones(String recomendaciones) {
		this.recomendaciones = recomendaciones;
	}


	
	
	/**
	 * 
	 * @return
	 */
	public HashMap getContrarreferenciaMap() {
		return contrarreferenciaMap;
	}


	
	/**
	 * 
	 * @param contrarreferenciaMap
	 */
	public void setContrarreferenciaMap(HashMap contrarreferenciaMap) {
		this.contrarreferenciaMap = contrarreferenciaMap;
	}


	
	
	/**
	 * 
	 * @return
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}


	
	/**
	 * 
	 * @param linkSiguiente
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}


	
	/**
	 * 
	 * @return
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}


	
	/**
	 * 
	 * @param maxPageItems
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}


	
	/**
	 * 
	 * @return
	 */
	public int getOffset() {
		return offset;
	}


	
	/**
	 * 
	 * @param offset
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}


	
	/**
	 * 
	 * @return
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	
	/**
	 * 
	 * @param patronOrdenar
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	
	/**
	 * 
	 * @return
	 */
	public int getPosEliminar() {
		return posEliminar;
	}


	
	/**
	 * 
	 * @param posEliminar
	 */
	public void setPosEliminar(int posEliminar) {
		this.posEliminar = posEliminar;
	}


	
	/**
	 * 
	 * @return
	 */
	public int getReferencia() {
		return referencia;
	}


	
	/**
	 * 
	 * @param referencia
	 */
	public void setReferencia(int referencia) {
		this.referencia = referencia;
	}


	
	
	/**
	 * 
	 * @return
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}


	
	/**
	 * 
	 * @param ultimoPatron
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}


	
	/**
	 * 
	 * @return
	 */
	public int getPaciente() {
		return paciente;
	}


	
	/**
	 * 
	 * @param paciente
	 */
	public void setPaciente(int paciente) {
		this.paciente = paciente;
	}


	
	/**
	 * 
	 * @return
	 */
	public String getSolicitud() {
		return solicitud;
	}


	
	/**
	 * 
	 * @param solicitud
	 */
	public void setSolicitud(String solicitud) {
		this.solicitud = solicitud;
	}
	
	
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getContrarreferenciaMap(String key) {
		return contrarreferenciaMap.get(key);
	}

	
	
	/**
	 * 
	 * @param key
	 * @param value
	 */	
	public void setContrarreferenciaMap(String key,Object value) {
		this.contrarreferenciaMap.put(key, value);
	}


	
		
	/**
	 * 
	 * @return
	 */
	public String getExamenFisico() {
		return examenFisico;
	}


	
	/**
	 * 
	 * @param examenFisico
	 */
	public void setExamenFisico(String examenFisico) {
		this.examenFisico = examenFisico;
	}


	
	/**
	 * 
	 * @return
	 */
	public String getFechaFinaliza() {
		return fechaFinaliza;
	}


	
	/**
	 * 
	 * @param fechaFinaliza
	 */
	public void setFechaFinaliza(String fechaFinaliza) {
		this.fechaFinaliza = fechaFinaliza;
	}


	
	/**
	 * 
	 * @return
	 */
	public String getFechaReferencia() {
		return fechaReferencia;
	}


	
	/**
	 * 
	 * @param fechaReferencia
	 */
	public void setFechaReferencia(String fechaReferencia) {
		this.fechaReferencia = fechaReferencia;
	}


	
	/**
	 * 
	 * @return
	 */
	public String getFechaRemision() {
		return fechaRemision;
	}


	
	/**
	 * 
	 * @param fechaRemision
	 */
	public void setFechaRemision(String fechaRemision) {
		this.fechaRemision = fechaRemision;
	}


	
	/**
	 * 
	 * @return
	 */
	public String getHallazgosClinicos() {
		return hallazgosClinicos;
	}


	
	/**
	 * 
	 * @param hallazgosClinicos
	 */
	public void setHallazgosClinicos(String hallazgosClinicos) {
		this.hallazgosClinicos = hallazgosClinicos;
	}


	
	/**
	 * 
	 * @return
	 */
	public String getHoraFinaliza() {
		return horaFinaliza;
	}


	
	/**
	 * 
	 * @param horaFinaliza
	 */
	public void setHoraFinaliza(String horaFinaliza) {
		this.horaFinaliza = horaFinaliza;
	}


	
	/**
	 * 
	 * @return
	 */
	public String getHoraReferencia() {
		return horaReferencia;
	}


	
	/**
	 * 
	 * @param horaReferencia
	 */
	public void setHoraReferencia(String horaReferencia) {
		this.horaReferencia = horaReferencia;
	}


	
	/**
	 * 
	 * @return
	 */
	public String getHoraRemision() {
		return horaRemision;
	}


	
	/**
	 * 
	 * @param horaRemision
	 */
	public void setHoraRemision(String horaRemision) {
		this.horaRemision = horaRemision;
	}


	
	/**
	 * 
	 * @return
	 */
	public String getInstitucionDestino() {
		return institucionDestino;
	}


	
	/**
	 * 
	 * @param institucionDestino
	 */
	public void setInstitucionDestino(String institucionDestino) {
		this.institucionDestino = institucionDestino;
	}


	
	/**
	 * 
	 * @return
	 */
	public String getInstitucionDestinoReferencia() {
		return institucionDestinoReferencia;
	}



	/**
	 * 
	 * @param institucionDestinoReferencia
	 */
	public void setInstitucionDestinoReferencia(String institucionDestinoReferencia) {
		this.institucionDestinoReferencia = institucionDestinoReferencia;
	}


	
	/**
	 * 
	 * @return
	 */
	public String getInstitucionOrigen() {
		return institucionOrigen;
	}


	
	/**
	 * 
	 * @param institucionOrigen
	 */
	public void setInstitucionOrigen(String institucionOrigen) {
		this.institucionOrigen = institucionOrigen;
	}


	

	
	/**
	 * 
	 * @return
	 */
	public int getProfesionalResponde() {
		return profesionalResponde;
	}


	
	/**
	 * 
	 * @param profesionalResponde
	 */
	public void setProfesionalResponde(int profesionalResponde) {
		this.profesionalResponde = profesionalResponde;
	}



	/**
	 * 
	 * @return
	 */
	public String getRelacionAnexos() {
		return relacionAnexos;
	}


	
	/**
	 * 
	 * @param relacionAnexos
	 */
	public void setRelacionAnexos(String relacionAnexos) {
		this.relacionAnexos = relacionAnexos;
	}


			
	/**
	 * 
	 * @return
	 */
	public String getTipoAtencion() {
		return tipoAtencion;
	}


	
	/**
	 * 
	 * @param tipoAtencion
	 */
	public void setTipoAtencion(String tipoAtencion) {
		this.tipoAtencion = tipoAtencion;
	}


	
	/**
	 * 
	 * @return
	 */
	public String getTratamientosInstaurados() {
		return tratamientosInstaurados;
	}


	
	/**
	 * 
	 * @param tratamientosInstaurados
	 */
	public void setTratamientosInstaurados(String tratamientosInstaurados) {
		this.tratamientosInstaurados = tratamientosInstaurados;
	}


	
	/**
	 * 
	 * @return
	 */
	public String getUsuarioFinaliza() {
		return usuarioFinaliza;
	}


	
	/**
	 * 
	 * @param usuarioFinaliza
	 */
	public void setUsuarioFinaliza(String usuarioFinaliza) {
		this.usuarioFinaliza = usuarioFinaliza;
	}


	
	/**
	 * 
	 * @return
	 */
	public HashMap getCodigoCentros() {
		return codigoCentros;
	}


	
	/**
	 * 
	 * @param codigoCentros
	 */
	public void setCodigoCentros(HashMap codigoCentros) {
		this.codigoCentros = codigoCentros;
	}


	
	/**
	 * 
	 * @return
	 */
	public ArrayList getProfesionalesSalud() {
		return profesionalesSalud;
	}


	
	/**
	 * 
	 * @param profesionalesSalud
	 */
	public void setProfesionalesSalud(ArrayList profesionalesSalud) {
		this.profesionalesSalud = profesionalesSalud;
	}


	
	/**
	 * 
	 * @return
	 */
	public ArrayList getTiposId() {
		return tiposId;
	}


	
	/**
	 * 
	 * @param tiposId
	 */
	public void setTiposId(ArrayList tiposId) {
		this.tiposId = tiposId;
	}


	
	/**
	 * 
	 * @return
	 */
	public String getEstadoRegistro() {
		return estadoRegistro;
	}


	
	/**
	 * 
	 * @param estadoRegistro
	 */
	public void setEstadoRegistro(String estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}


	
	/**
	 * 
	 * @return
	 */
	public int getIngreso() {
		return ingreso;
	}


	
	/**
	 * 
	 * @param ingreso
	 */
	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}


	
	/**
	 * 
	 * @return
	 */
	public int getNumeroReferenciaContra() {
		return numeroReferenciaContra;
	}


	
	/**
	 * 
	 * @param numeroReferenciaContra
	 */
	public void setNumeroReferenciaContra(int numeroReferenciaContra) {
		this.numeroReferenciaContra = numeroReferenciaContra;
	}


	
	/**
	 * 
	 * @return
	 */
	public HashMap getOpcionesImpresionMap() {
		return opcionesImpresionMap;
	}


	
	/**
	 * 
	 * @param opcionesImpresionMap
	 */
	public void setOpcionesImpresionMap(HashMap opcionesImpresionMap) {
		this.opcionesImpresionMap = opcionesImpresionMap;
	}


	
		
	/**
	 * 
	 * @return
	 */
	/*public ArrayList getCentrosCosto() {
		return centrosCosto;
	}*/


	
	/**
	 * 
	 * @param centrosCosto
	 */
	/*public void setCentrosCosto(ArrayList centrosCosto) {
		this.centrosCosto = centrosCosto;
	}*/
	
		
	/**
	 * @return Returns the diagnosticos.
	 */
	public HashMap getDiagnosticos() {
		return diagnosticos;
	}
	/**
	 * @param diagnosticos The diagnosticos to set.
	 */
	public void setDiagnosticos(HashMap diagnosticos) {
		this.diagnosticos = diagnosticos;
	}
	/**
	 * @return Retorna un elemento del mapa diagnosticos.
	 */
	public Object getDiagnosticos(String key) {
		return diagnosticos.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa diagnosticos.
	 * 
	 */
	public void setDiagnosticos(String key,Object obj) {
		this.diagnosticos.put(key,obj);
	}
	
	/**
	 * @return Returns the numDiagnosticos.
	 */
	public int getNumDiagnosticos() {
		return numDiagnosticos;
	}
	/**
	 * @param numDiagnosticos The numDiagnosticos to set.
	 */
	public void setNumDiagnosticos(int numDiagnosticos) {
		this.numDiagnosticos = numDiagnosticos;
	}


	/**
	 * 
	 * @return
	 */
	public String getProcedimientosInsertados() {
		return procedimientosInsertados;
	}


	/**
	 * 
	 * @param procedimientosInsertados
	 */
	public void setProcedimientosInsertados(String procedimientosInsertados) {
		this.procedimientosInsertados = procedimientosInsertados;
	}


	
	/**
	 * 
	 * @return
	 */
	public String getTipoReferencia() {
		return tipoReferencia;
	}


	
	/**
	 * 
	 * @param tipoReferencia
	 */
	public void setTipoReferencia(String tipoReferencia) {
		this.tipoReferencia = tipoReferencia;
	}


	
	/**
	 * 
	 * @return
	 */
	public HashMap getResultadosProcedimientos() {
		return resultadosProcedimientos;
	}


	
	/**
	 * 
	 * @param resultadosProcedimientos
	 */
	public void setResultadosProcedimientos(HashMap resultadosProcedimientos) {
		this.resultadosProcedimientos = resultadosProcedimientos;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Object getResultadosProcedimientos(String key) {
		return resultadosProcedimientos.get(key);
	}


	
	/**
	 * 
	 * @param resultadosProcedimientos
	 */
	public void setResultadosProcedimientos(String key,Object value) 
	{
		this.resultadosProcedimientos.put(key, value);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public boolean isSeccionProcedimientosSolicitados() {
		return seccionProcedimientosSolicitados;
	}

	
	
	/**
	 * 
	 * @param seccionProcedimientosSolicitados
	 */
	public void setSeccionProcedimientosSolicitados(
			boolean seccionProcedimientosSolicitados) {
		this.seccionProcedimientosSolicitados = seccionProcedimientosSolicitados;
	}


	
	/**
	 * 
	 * @return
	 */
	public HashMap getInstitucionesSirc() {
		return institucionesSirc;
	}


	
	/**
	 * 
	 * @param institucionesSirc
	 */
	public void setInstitucionesSirc(HashMap institucionesSirc) {
		this.institucionesSirc = institucionesSirc;
	}


	
	/**
	 * 
	 * @return
	 */
	public ArrayList getTiposIdentificacion() {
		return tiposIdentificacion;
	}


	
	/**
	 * 
	 * @param tiposIdentificacion
	 */
	public void setTiposIdentificacion(ArrayList tiposIdentificacion) {
		this.tiposIdentificacion = tiposIdentificacion;
	}


	
	/**
	 * 
	 * @return
	 */
	public HashMap getConducta() {
		return conducta;
	}


	
	/**
	 * 
	 * @param conducta
	 */
	public void setConducta(HashMap conducta) {
		this.conducta = conducta;
	}


	/**
	 * 
	 * @return
	 */
	public int getIndice() {
		return indice;
	}


	/**
	 * 
	 * @param indice
	 */
	public void setIndice(int indice) {
		this.indice = indice;
	}


	/**
	 * 
	 * @return
	 */
	public HashMap getConductasSeguir() {
		return conductasSeguir;
	}


	/**
	 * 
	 * @param conductasSeguir
	 */
	public void setConductasSeguir(HashMap conductasSeguir) {
		this.conductasSeguir = conductasSeguir;
	}
	
	
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getConductasSeguir(String key) {
		return conductasSeguir.get(key);
	}


	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setConductasSeguir(String key,Object value) {
		this.conductasSeguir.put(key, value);
	}


	/**
	 * 
	 * @return
	 */
	public String getSeccionConductas() {
		return seccionConductas;
	}


	/**
	 * 
	 * @param seccionConductas
	 */
	public void setSeccionConductas(String seccionConductas) {
		this.seccionConductas = seccionConductas;
	}


	
	/**
	 * 
	 * @return
	 */
	public String getConsecutivoAnio() {
		return consecutivoAnio;
	}


	
	/**
	 * 
	 * @param consecutivoAnio
	 */
	public void setConsecutivoAnio(String consecutivoAnio) {
		this.consecutivoAnio = consecutivoAnio;
	}


	
	/**
	 * 
	 * @return
	 */
	public int getConsecutivoInt() {
		return consecutivoInt;
	}


	
	/**
	 * 
	 * @param consecutivoInt
	 */
	public void setConsecutivoInt(int consecutivoInt) {
		this.consecutivoInt = consecutivoInt;
	}


	
	/**
	 * 
	 * @return
	 */
	public String getEstadoReferencia() {
		return estadoReferencia;
	}


	
	/**
	 * 
	 * @param estadoReferencia
	 */
	public void setEstadoReferencia(String estadoReferencia) {
		this.estadoReferencia = estadoReferencia;
	}


	
	

	
	/**
	 * 
	 * @return
	 */
	public int getCentrosCosto() {
		return centrosCosto;
	}


	
	/**
	 * 
	 * @param centrosCosto
	 */
	public void setCentrosCosto(int centrosCosto) {
		this.centrosCosto = centrosCosto;
	}


	
	/**
	 * 
	 * @return
	 */
	public ArrayList getCentros() {
		return centros;
	}


	
	/**
	 * 
	 * @param centros
	 */
	public void setCentros(ArrayList centros) {
		this.centros = centros;
	}


	
	/**
	 * 
	 * @return
	 */
	public int getIndexSeleccionado() {
		return indexSeleccionado;
	}


	
	/**
	 * 
	 * @param indexSeleccionado
	 */
	public void setIndexSeleccionado(int indexSeleccionado) {
		this.indexSeleccionado = indexSeleccionado;
	}



	public String getNombreInstitucionOrigen() {
		return nombreInstitucionOrigen;
	}



	public void setNombreInstitucionOrigen(String nombreinstitucionorigen) {
		this.nombreInstitucionOrigen = nombreinstitucionorigen;
	}



	public String getAnioConsecutivoPuntoAtencion() {
		return anioConsecutivoPuntoAtencion;
	}



	public void setAnioConsecutivoPuntoAtencion(String anioConsecutivoPuntoAtencion) {
		this.anioConsecutivoPuntoAtencion = anioConsecutivoPuntoAtencion;
	}
	
	

	public String getConsecutivoPuntoAtencion() {
		return consecutivoPuntoAtencion;
	}



	public void setConsecutivoPuntoAtencion(String consecutivoPuntoAtencion) {
		this.consecutivoPuntoAtencion = consecutivoPuntoAtencion;
	}



	public boolean isEsModificacion() {
		return esModificacion;
	}



	public void setEsModificacion(boolean esModificacion) {
		this.esModificacion = esModificacion;
	}



	public HashMap getResultadosProcedimientosEliminados() {
		return resultadosProcedimientosEliminados;
	}



	public void setResultadosProcedimientosEliminados(
			HashMap resultadosProcedimientosEliminados) {
		this.resultadosProcedimientosEliminados = resultadosProcedimientosEliminados;
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public Object getResultadosProcedimientosEliminados(String key) {
		return resultadosProcedimientosEliminados.get(key);
	}


	
	/**
	 * 
	 * @param resultadosProcedimientos
	 */
	public void setResultadosProcedimientosEliminados(String key,Object value) 
	{
		this.resultadosProcedimientosEliminados.put(key, value);
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
	
	
	
}
	
	
