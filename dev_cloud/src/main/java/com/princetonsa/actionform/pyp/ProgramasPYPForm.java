/*
 * Ago 13, 2006
 */
package com.princetonsa.actionform.pyp;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.pyp.DtoObservacionProgramaPYP;

import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * @author Sebastián Gómez 
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 * Programas de Promoción y Prevención  
 */
public class ProgramasPYPForm extends ValidatorForm 
{
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	private boolean soloConsulta;
	
	/**
	 * Mé indica si estoy trabajando con la cuenta activa del paciente
	 */
	private boolean cuentaActiva;
	
	/**
	 * Indica si el convenio es PYP
	 */
	private boolean esConvenioPYP;
	
	/**
	 * Listado de los codigos convenios pyp del paciente separados por ,
	 */
	private String listadoConvenios;
	
	/**
	 * Listado de los codigos tipo regimen de los convenios pyp del paciente separados por ,
	 */
	private String listadoTiposRegimen;
	
	/**
	 * Objeto que almacena los programas del paciente
	 */
	private HashMap programas = new HashMap();
	
	/**
	 * Numero de filas del mapa programas
	 */
	private int numProgramas;
	
	/**
	 * posicion de un registro dentro del mapa programas
	 */
	private int posicion;
	
	/**
	 * Objeto qeu almacena el listado de actividades
	 */
	private HashMap actividades = new HashMap();
	
	/**
	 * Número de registros del mapa actividades
	 */
	private int numActividades;
	
	/**
	 * Objeto que almacena el listado de articulos
	 */
	private HashMap articulos = new HashMap();
	
	/**
	 * Número de registros del mapa articulos
	 */
	private int numArticulos;
	
	/**
	 * posicion de un registro dentro del mapa actividades o articulos
	 */
	private int posicionActividad;
	
	/**
	 * Objeto donde se almacena el histórico
	 */
	private HashMap historico = new HashMap();
	
	/**
	 * Número de registros del mapa historico
	 */
	private int numHistorico;
	/**
	 * Acronimo del tipo de actividad
	 */
	private String tipoActividad;
	
	/**
	 * Indica si dentro del listado de programas
	 * hay programas existentes
	 */
	private boolean programasExistentes;
	
	/**
	 * Centro Atencion PYP del paciente
	 */
	private String centroAtencionPaciente;
	
	/**
	 * Morivo de la cancelacion
	 */
	private String motivoCancelacion;
	
	/**
	 * Número de la solicitud asignado a la actividad
	 */
	private String numeroSolicitud;
	
	/**
	 * Indicador que se usa para confirmar la ejecuacion de una actividad
	 * de consulta
	 */
	private boolean confirmarEjecucion;
	
	/**
	 * Número de roden ambulatoria asignada a la actividad
	 */
	private String numeroOrden; 
	
	private boolean ocultarCabezote;
	//campos para mantener en memoria el consecutivo del programa con el cua se está trabajando
	private String consecutivoPrograma;
	private String consecutivoActividad;
	
	//**********ATRIBUTOS DESTINADOS A LA PROGRAMACION********************
	
	private String fechaProgramacion;
	private String horaProgramacion;
	private String institucion;
	/**
	 * Para actividades de servicios
	 */
	private String codigoMedico;
	private String especialidadOcupacion;
	private String finalidadServicio;
	
	/**
	 * Para actividaddes de articulos
	 */
	private String dosis;
	private String unidosis;
	private String via;
	private String frecuencia;
	private String tipoFrecuencia;
	private String diasTratamiento;
	private String cantidad;
	private String observaciones;
	private boolean medicamento;
	//********************************************************************
	
	//*********ATRIBUTOS DESTINADOS AL USO DE PYP EN LA VALORACION DE CONSULTA EXTERNA************
	
	private String numSolConsulta;
	private String unificarPYP;
	private boolean consultaExterna;
	
	
	private String tipoServicioCE;
	private String diagnosticoCE;
	private String valorFichaCE;
	private String finalidadCE;
	private String causaExternaCE;
	private String respuestaCE;
	private String finalidadServicioCE;
	private String especialidadCE;
	private String codigoMedicoCE;
	private String institucionCE;
	//*********************************************************************************************
	
	//*************ATRIBUTOS PARA LOS CAMBIOS EN ENTIDADES SUBCONTRATADAS
	private String codigoServicio;
	private String nombreServicio;
	private ArrayList<HashMap<String, Object>> centrosCosto;
	private boolean validarCC;
	private String centroCostoSeleccionado;
	private String nroSolicitud;
	//****************************************************************************************
	
	//**********ATRIBUTOS PARA TRABAJAR CUANDO LA CUENTA DEL PACIENTE SE ENCUENTRA CERRADA PERO HA TENIDO UNA ULTIMA ATENCIÓN FACTURADA CON UN CONVENIO PYP
	private HashMap ultimaCuentaAsociada;
	private boolean pacienteCuentaCerrada;
	
	private ArrayList<DtoObservacionProgramaPYP> observacionesProgramaPYP;
	
	private DtoObservacionProgramaPYP nuevaObservacionPYP;
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.cuentaActiva = false;
		this.esConvenioPYP = false;
		this.listadoConvenios = "";
		this.listadoTiposRegimen = "";
		
		this.programas = new HashMap();
		this.numProgramas = 0;
		this.posicion = 0;
		this.programasExistentes = false;
		this.centroAtencionPaciente = "";
		
		this.actividades = new HashMap();
		this.numActividades = 0;
		this.articulos = new HashMap();
		this.numArticulos = 0;
		this.posicionActividad = 0;
		this.tipoActividad = "";
		
		this.historico = new HashMap();
		this.numHistorico = 0;
		
		this.motivoCancelacion = "";
		
		this.numeroSolicitud = "";
		this.numeroOrden = "";
		
		this.ocultarCabezote = false;
		
		this.consecutivoPrograma = "";
		this.consecutivoActividad = "";
		
		this.soloConsulta = false;
		
		this.confirmarEjecucion = false;
		
		this.resetProgramacion();
		
		//atributos especialies de la valoracion consulta externa
		this.numSolConsulta = "";
		this.unificarPYP = "";
		this.consultaExterna = false;
		this.tipoServicioCE = "";
		
		//Atributos de 
		this.codigoServicio="";
		this.nombreServicio="";
		this.centrosCosto = new ArrayList<HashMap<String,Object>>();
		this.validarCC=false;
		this.centroCostoSeleccionado="";
		
		this.resetConsultaExterna();
		
		this.ultimaCuentaAsociada=new HashMap();
		this.pacienteCuentaCerrada=false;
	
		this.observacionesProgramaPYP=new ArrayList<DtoObservacionProgramaPYP>();
		this.nuevaObservacionPYP=new DtoObservacionProgramaPYP();
	}
	
	public void resetConsultaExterna()
	{
		this.diagnosticoCE = "";
		this.valorFichaCE = "";
		this.finalidadCE = "";
		this.causaExternaCE = "";
		this.respuestaCE = "";
		this.finalidadServicioCE = "";
		this.especialidadCE = "";
		this.codigoMedicoCE = "";
		this.institucionCE = "";
	}
	
	public void resetProgramacion()
	{
		this.fechaProgramacion = "";
		this.horaProgramacion = "";
		this.institucion = "";
		
		this.codigoMedico = "";
		this.especialidadOcupacion = "";
		this.finalidadServicio = "";
		
		this.dosis = "";
		this.unidosis = "";
		this.frecuencia = "";
		this.tipoFrecuencia = "";
		this.via = "";
		this.cantidad = "";
		this.diasTratamiento = "";
		this.observaciones = "";
		this.medicamento = false;
		this.codigoServicio="";
		this.nombreServicio="";
		this.centrosCosto = new ArrayList<HashMap<String,Object>>();
		this.validarCC=false;
		this.centroCostoSeleccionado="";
		
	}
	
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
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		if(this.estado.equals("guardarProgramas"))
		{
			request.getSession().setAttribute("ocultarEncabezadoOrden",null);
			for(int i=0;i<this.numProgramas;i++)
			{
				String fecha = this.getProgramas("fecha_final_"+i).toString();
				String fechaInicial = this.getProgramas("fecha_inicial_"+i).toString();
				if(!fecha.equals(""))
				{
					String mensaje = "";
					if(!UtilidadFecha.validarFecha(fecha))
						errores.add("Fecha invalida",new ActionMessage("errors.formatoFechaInvalido",mensaje));
					else 
					{
						mensaje = "final del programa "+this.getProgramas("nombre_programa_"+i);
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fecha,UtilidadFecha.getFechaActual()))
							errores.add("Fecha final > a fecha Actual",new ActionMessage("errors.fechaPosteriorIgualActual",mensaje,"actual"));
						mensaje = "inicial del programa "+this.getProgramas("nombre_programa_"+i);
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaInicial,fecha))
							errores.add("Fecha inicial > a fecha Final",new ActionMessage("errors.fechaPosteriorIgualActual",mensaje,"final"));
							
					}
						
					
					mensaje = "El motivo de finalización del programa "+this.getProgramas("nombre_programa_"+i);
					String motivo = this.getProgramas("motivo_finalizacion_"+i).toString();
					if(motivo.equals(""))
						errores.add("motivo finalizacion es requerido",new ActionMessage("errors.required",mensaje));
						
				}
			}
			
			if(!errores.isEmpty())
				this.estado = "empezar";
		}
		return errores;
	}

	

	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return Returns the cuentaActiva.
	 */
	public boolean isCuentaActiva() {
		return cuentaActiva;
	}

	/**
	 * @param cuentaActiva The cuentaActiva to set.
	 */
	public void setCuentaActiva(boolean cuentaActiva) {
		this.cuentaActiva = cuentaActiva;
	}

	/**
	 * @return Returns the numProgramas.
	 */
	public int getNumProgramas() {
		return numProgramas;
	}

	/**
	 * @param numProgramas The numProgramas to set.
	 */
	public void setNumProgramas(int numProgramas) {
		this.numProgramas = numProgramas;
	}

	/**
	 * @return Returns the programas.
	 */
	public HashMap getProgramas() {
		return programas;
	}

	/**
	 * @param programas The programas to set.
	 */
	public void setProgramas(HashMap programas) {
		this.programas = programas;
	}
	
	/**
	 * @return Retorna un elemento del mapa programas.
	 */
	public Object getProgramas(String key) {
		return programas.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa programas.
	 */
	public void setProgramas(String key,Object obj) {
		this.programas.put(key,obj);
	}

	/**
	 * @return Returns the posicion.
	 */
	public int getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion The posicion to set.
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	/**
	 * @return Returns the programasExistentes.
	 */
	public boolean isProgramasExistentes() {
		return programasExistentes;
	}

	/**
	 * @param programasExistentes The programasExistentes to set.
	 */
	public void setProgramasExistentes(boolean programasExistentes) {
		this.programasExistentes = programasExistentes;
	}

	/**
	 * @return Returns the centroAtencionPaciente.
	 */
	public String getCentroAtencionPaciente() {
		return centroAtencionPaciente;
	}

	/**
	 * @param centroAtencionPaciente The centroAtencionPaciente to set.
	 */
	public void setCentroAtencionPaciente(String centroAtencionPaciente) {
		this.centroAtencionPaciente = centroAtencionPaciente;
	}

	/**
	 * @return Returns the esConvenioPYP.
	 */
	public boolean isEsConvenioPYP() {
		return esConvenioPYP;
	}

	/**
	 * @param esConvenioPYP The esConvenioPYP to set.
	 */
	public void setEsConvenioPYP(boolean esConvenioPYP) {
		this.esConvenioPYP = esConvenioPYP;
	}

	/**
	 * @return Retorna elemento del mapa actividades.
	 */
	public Object getActividades(String key) {
		return actividades.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa actividades.
	 */
	public void setActividades(String key, Object obj) 
	{
		this.actividades.put(key,obj);
	}
	
	/**
	 * @return Returns the actividades.
	 */
	public HashMap getActividades() {
		return actividades;
	}

	/**
	 * @param actividades The actividades to set.
	 */
	public void setActividades(HashMap actividades) {
		this.actividades = actividades;
	}

	/**
	 * @return Returns the articulos.
	 */
	public HashMap getArticulos() {
		return articulos;
	}

	/**
	 * @param articulos The articulos to set.
	 */
	public void setArticulos(HashMap articulos) {
		this.articulos = articulos;
	}
	
	/**
	 * @return Retorna un elemento del mapa articulos.
	 */
	public Object getArticulos(String key) 
	{
		return articulos.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa articulos.
	 */
	public void setArticulos(String key,Object obj) {
		this.articulos.put(key,obj);
	}

	/**
	 * @return Returns the numActividades.
	 */
	public int getNumActividades() {
		return numActividades;
	}

	/**
	 * @param numActividades The numActividades to set.
	 */
	public void setNumActividades(int numActividades) {
		this.numActividades = numActividades;
	}

	/**
	 * @return Returns the numArticulos.
	 */
	public int getNumArticulos() {
		return numArticulos;
	}

	/**
	 * @param numArticulos The numArticulos to set.
	 */
	public void setNumArticulos(int numArticulos) {
		this.numArticulos = numArticulos;
	}

	/**
	 * @return Returns the posicionActividad.
	 */
	public int getPosicionActividad() {
		return posicionActividad;
	}

	/**
	 * @param posicionActividad The posicionActividad to set.
	 */
	public void setPosicionActividad(int posicionActividad) {
		this.posicionActividad = posicionActividad;
	}


	/**
	 * @return Returns the tipoActividad.
	 */
	public String getTipoActividad() {
		return tipoActividad;
	}

	/**
	 * @param tipoActividad The tipoActividad to set.
	 */
	public void setTipoActividad(String tipoActividad) {
		this.tipoActividad = tipoActividad;
	}

	/**
	 * @return Returns the historico.
	 */
	public HashMap getHistorico() {
		return historico;
	}

	/**
	 * @param historico The historico to set.
	 */
	public void setHistorico(HashMap historico) {
		this.historico = historico;
	}
	
	/**
	 * @return Retorna un elemento del mapa historico.
	 */
	public Object getHistorico(String key) {
		return historico.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa historico.
	 */
	public void setHistorico(String key,Object obj) {
		this.historico.put(key,obj);
	}

	/**
	 * @return Returns the numHistorico.
	 */
	public int getNumHistorico() {
		return numHistorico;
	}

	/**
	 * @param numHistorico The numHistorico to set.
	 */
	public void setNumHistorico(int numHistorico) {
		this.numHistorico = numHistorico;
	}

	/**
	 * @return Returns the motivoCancelacion.
	 */
	public String getMotivoCancelacion() {
		return motivoCancelacion;
	}

	/**
	 * @param motivoCancelacion The motivoCancelacion to set.
	 */
	public void setMotivoCancelacion(String motivoCancelacion) {
		this.motivoCancelacion = motivoCancelacion;
	}

	/**
	 * @return Returns the numeroOrden.
	 */
	public String getNumeroOrden() {
		return numeroOrden;
	}

	/**
	 * @param numeroOrden The numeroOrden to set.
	 */
	public void setNumeroOrden(String numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	/**
	 * @return Returns the numeroSolicitud.
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return Returns the consecutivoActividad.
	 */
	public String getConsecutivoActividad() {
		return consecutivoActividad;
	}

	/**
	 * @param consecutivoActividad The consecutivoActividad to set.
	 */
	public void setConsecutivoActividad(String consecutivoActividad) {
		this.consecutivoActividad = consecutivoActividad;
	}

	/**
	 * @return Returns the consecutivoPrograma.
	 */
	public String getConsecutivoPrograma() {
		return consecutivoPrograma;
	}

	/**
	 * @param consecutivoPrograma The consecutivoPrograma to set.
	 */
	public void setConsecutivoPrograma(String consecutivoPrograma) {
		this.consecutivoPrograma = consecutivoPrograma;
	}

	/**
	 * @return Returns the cantidad.
	 */
	public String getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad The cantidad to set.
	 */
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return Returns the dosis.
	 */
	public String getDosis() {
		return dosis;
	}

	/**
	 * @param dosis The dosis to set.
	 */
	public void setDosis(String dosis) {
		this.dosis = dosis;
	}

	/**
	 * @return Returns the especialidadOcupacion.
	 */
	public String getEspecialidadOcupacion() {
		return especialidadOcupacion;
	}

	/**
	 * @param especialidadOcupacion The especialidadOcupacion to set.
	 */
	public void setEspecialidadOcupacion(String especialidadOcupacion) {
		this.especialidadOcupacion = especialidadOcupacion;
	}

	/**
	 * @return Returns the finalidadServicio.
	 */
	public String getFinalidadServicio() {
		return finalidadServicio;
	}

	/**
	 * @param finalidadServicio The finalidadServicio to set.
	 */
	public void setFinalidadServicio(String finalidadServicio) {
		this.finalidadServicio = finalidadServicio;
	}

	/**
	 * @return Returns the frecuencia.
	 */
	public String getFrecuencia() {
		return frecuencia;
	}

	/**
	 * @param frecuencia The frecuencia to set.
	 */
	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}

	/**
	 * @return Returns the observaciones.
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones The observaciones to set.
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return Returns the tipoFrecuencia.
	 */
	public String getTipoFrecuencia() {
		return tipoFrecuencia;
	}

	/**
	 * @param tipoFrecuencia The tipoFrecuencia to set.
	 */
	public void setTipoFrecuencia(String tipoFrecuencia) {
		this.tipoFrecuencia = tipoFrecuencia;
	}

	/**
	 * @return Returns the via.
	 */
	public String getVia() {
		return via;
	}

	/**
	 * @param via The via to set.
	 */
	public void setVia(String via) {
		this.via = via;
	}

	/**
	 * @return Returns the fechaProgramacion.
	 */
	public String getFechaProgramacion() {
		return fechaProgramacion;
	}

	/**
	 * @param fechaProgramacion The fechaProgramacion to set.
	 */
	public void setFechaProgramacion(String fechaProgramacion) {
		this.fechaProgramacion = fechaProgramacion;
	}

	/**
	 * @return Returns the horaProgramacion.
	 */
	public String getHoraProgramacion() {
		return horaProgramacion;
	}

	/**
	 * @param horaProgramacion The horaProgramacion to set.
	 */
	public void setHoraProgramacion(String horaProgramacion) {
		this.horaProgramacion = horaProgramacion;
	}

	/**
	 * @return Returns the codigoMedico.
	 */
	public String getCodigoMedico() {
		return codigoMedico;
	}

	/**
	 * @param codigoMedico The codigoMedico to set.
	 */
	public void setCodigoMedico(String codigoMedico) {
		this.codigoMedico = codigoMedico;
	}

	/**
	 * @return Returns the institucion.
	 */
	public String getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return Returns the consultaExterna.
	 */
	public boolean isConsultaExterna() {
		return consultaExterna;
	}

	/**
	 * @param consultaExterna The consultaExterna to set.
	 */
	public void setConsultaExterna(boolean consultaExterna) {
		this.consultaExterna = consultaExterna;
	}

	/**
	 * @return Returns the numSolConsulta.
	 */
	public String getNumSolConsulta() {
		return numSolConsulta;
	}

	/**
	 * @param numSolConsulta The numSolConsulta to set.
	 */
	public void setNumSolConsulta(String numSolConsulta) {
		this.numSolConsulta = numSolConsulta;
	}

	/**
	 * @return Returns the unificarPYP.
	 */
	public String getUnificarPYP() {
		return unificarPYP;
	}

	/**
	 * @param unificarPYP The unificarPYP to set.
	 */
	public void setUnificarPYP(String unificarPYP) {
		this.unificarPYP = unificarPYP;
	}

	

	/**
	 * @return Returns the tipoServicioCE.
	 */
	public String getTipoServicioCE() {
		return tipoServicioCE;
	}

	/**
	 * @param tipoServicioCE The tipoServicioCE to set.
	 */
	public void setTipoServicioCE(String tipoServicioCE) {
		this.tipoServicioCE = tipoServicioCE;
	}

	/**
	 * @return Returns the causaExternaCE.
	 */
	public String getCausaExternaCE() {
		return causaExternaCE;
	}

	/**
	 * @param causaExternaCE The causaExternaCE to set.
	 */
	public void setCausaExternaCE(String causaExternaCE) {
		this.causaExternaCE = causaExternaCE;
	}

	/**
	 * @return Returns the diagnosticoCE.
	 */
	public String getDiagnosticoCE() {
		return diagnosticoCE;
	}

	/**
	 * @param diagnosticoCE The diagnosticoCE to set.
	 */
	public void setDiagnosticoCE(String diagnosticoCE) {
		this.diagnosticoCE = diagnosticoCE;
	}

	/**
	 * @return Returns the finalidadCE.
	 */
	public String getFinalidadCE() {
		return finalidadCE;
	}

	/**
	 * @param finalidadCE The finalidadCE to set.
	 */
	public void setFinalidadCE(String finalidadCE) {
		this.finalidadCE = finalidadCE;
	}

	/**
	 * @return Returns the respuestaCE.
	 */
	public String getRespuestaCE() {
		return respuestaCE;
	}

	/**
	 * @param respuestaCE The respuestaCE to set.
	 */
	public void setRespuestaCE(String respuestaCE) {
		this.respuestaCE = respuestaCE;
	}

	/**
	 * @return Returns the soloConsulta.
	 */
	public boolean isSoloConsulta() {
		return soloConsulta;
	}

	/**
	 * @param soloConsulta The soloConsulta to set.
	 */
	public void setSoloConsulta(boolean soloConsulta) {
		this.soloConsulta = soloConsulta;
	}

	/**
	 * @return Returns the especialidadCE.
	 */
	public String getEspecialidadCE() {
		return especialidadCE;
	}

	/**
	 * @param especialidadCE The especialidadCE to set.
	 */
	public void setEspecialidadCE(String especialidadCE) {
		this.especialidadCE = especialidadCE;
	}

	/**
	 * @return Returns the finalidadServicioCE.
	 */
	public String getFinalidadServicioCE() {
		return finalidadServicioCE;
	}

	/**
	 * @param finalidadServicioCE The finalidadServicioCE to set.
	 */
	public void setFinalidadServicioCE(String finalidadServicioCE) {
		this.finalidadServicioCE = finalidadServicioCE;
	}

	/**
	 * @return Returns the codigoMedicoCE.
	 */
	public String getCodigoMedicoCE() {
		return codigoMedicoCE;
	}

	/**
	 * @param codigoMedicoCE The codigoMedicoCE to set.
	 */
	public void setCodigoMedicoCE(String codigoMedicoCE) {
		this.codigoMedicoCE = codigoMedicoCE;
	}

	/**
	 * @return Returns the institucionCE.
	 */
	public String getInstitucionCE() {
		return institucionCE;
	}

	/**
	 * @param institucionCE The institucionCE to set.
	 */
	public void setInstitucionCE(String institucionCE) {
		this.institucionCE = institucionCE;
	}

	/**
	 * @return Returns the ocultarCabezote.
	 */
	public boolean isOcultarCabezote() {
		return ocultarCabezote;
	}

	/**
	 * @param ocultarCabezote The ocultarCabezote to set.
	 */
	public void setOcultarCabezote(boolean ocultarCabezote) {
		this.ocultarCabezote = ocultarCabezote;
	}

	/**
	 * @return Returns the confirmarEjecucion.
	 */
	public boolean isConfirmarEjecucion() {
		return confirmarEjecucion;
	}

	/**
	 * @param confirmarEjecucion The confirmarEjecucion to set.
	 */
	public void setConfirmarEjecucion(boolean confirmarEjecucion) {
		this.confirmarEjecucion = confirmarEjecucion;
	}

	/**
	 * @return the valorFichaCE
	 */
	public String getValorFichaCE() {
		return valorFichaCE;
	}

	/**
	 * @param valorFichaCE the valorFichaCE to set
	 */
	public void setValorFichaCE(String valorFichaCE) {
		this.valorFichaCE = valorFichaCE;
	}

	/**
	 * @return the unidosis
	 */
	public String getUnidosis() {
		return unidosis;
	}

	/**
	 * @param unidosis the unidosis to set
	 */
	public void setUnidosis(String unidosis) {
		this.unidosis = unidosis;
	}

	/**
	 * @return the diasTratamiento
	 */
	public String getDiasTratamiento() {
		return diasTratamiento;
	}

	/**
	 * @param diasTratamiento the diasTratamiento to set
	 */
	public void setDiasTratamiento(String diasTratamiento) {
		this.diasTratamiento = diasTratamiento;
	}

	/**
	 * @return the medicamento
	 */
	public boolean isMedicamento() {
		return medicamento;
	}

	/**
	 * @param medicamento the medicamento to set
	 */
	public void setMedicamento(boolean medicamento) {
		this.medicamento = medicamento;
	}

	/**
	 * @return the listadoConvenios
	 */
	public String getListadoConvenios() {
		return listadoConvenios;
	}

	/**
	 * @param listadoConvenios the listadoConvenios to set
	 */
	public void setListadoConvenios(String listadoConvenios) {
		this.listadoConvenios = listadoConvenios;
	}

	/**
	 * @return the listadoTiposRegimen
	 */
	public String getListadoTiposRegimen() {
		return listadoTiposRegimen;
	}

	/**
	 * @param listadoTiposRegimen the listadoTiposRegimen to set
	 */
	public void setListadoTiposRegimen(String listadoTiposRegimen) {
		this.listadoTiposRegimen = listadoTiposRegimen;
	}

	public String getCodigoServicio() {
		return codigoServicio;
	}

	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	public ArrayList<HashMap<String, Object>> getCentrosCosto() {
		return centrosCosto;
	}

	public void setCentrosCosto(ArrayList<HashMap<String, Object>> centrosCosto) {
		this.centrosCosto = centrosCosto;
	}

	public boolean isValidarCC() {
		return validarCC;
	}

	public void setValidarCC(boolean validarCC) {
		this.validarCC = validarCC;
	}

	public String getCentroCostoSeleccionado() {
		return centroCostoSeleccionado;
	}

	public void setCentroCostoSeleccionado(String centroCostoSeleccionado) {
		this.centroCostoSeleccionado = centroCostoSeleccionado;
	}

	public String getNombreServicio() {
		return nombreServicio;
	}

	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	/**
	 * @return the ultimaCuentaAsociada
	 */
	public HashMap getUltimaCuentaAsociada() {
		return ultimaCuentaAsociada;
	}

	/**
	 * @param ultimaCuentaAsociada the ultimaCuentaAsociada to set
	 */
	public void setUltimaCuentaAsociada(HashMap ultimaCuentaAsociada) {
		this.ultimaCuentaAsociada = ultimaCuentaAsociada;
	}

	/**
	 * @return the pacienteCuentaCerrada
	 */
	public boolean isPacienteCuentaCerrada() {
		return pacienteCuentaCerrada;
	}

	/**
	 * @param pacienteCuentaCerrada the pacienteCuentaCerrada to set
	 */
	public void setPacienteCuentaCerrada(boolean pacienteCuentaCerrada) {
		this.pacienteCuentaCerrada = pacienteCuentaCerrada;
	}

	/**
	 * @return the observacionesProgramaPYP
	 */
	public ArrayList<DtoObservacionProgramaPYP> getObservacionesProgramaPYP() {
		return observacionesProgramaPYP;
	}

	/**
	 * @param observacionesProgramaPYP the observacionesProgramaPYP to set
	 */
	public void setObservacionesProgramaPYP(
			ArrayList<DtoObservacionProgramaPYP> observacionesProgramaPYP) {
		this.observacionesProgramaPYP = observacionesProgramaPYP;
	}

	/**
	 * @return the nuevaObservacionPYP
	 */
	public DtoObservacionProgramaPYP getNuevaObservacionPYP() {
		return nuevaObservacionPYP;
	}

	/**
	 * @param nuevaObservacionPYP the nuevaObservacionPYP to set
	 */
	public void setNuevaObservacionPYP(DtoObservacionProgramaPYP nuevaObservacionPYP) {
		this.nuevaObservacionPYP = nuevaObservacionPYP;
	}
	
	
}
