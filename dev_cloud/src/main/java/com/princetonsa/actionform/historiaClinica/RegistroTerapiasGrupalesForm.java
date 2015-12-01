/*
 * Oct 22, 2007
 * Proyect axioma
 * Paquete com.princetonsa.actionform.historiaClinica
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.actionform.historiaClinica;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.UsuarioBasico;

import util.ConstantesBD;
import util.RespuestaValidacion;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class RegistroTerapiasGrupalesForm extends ValidatorForm 
{
	
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Mapa que contiene los pacientes posibles para hosptal dia.
	 */
	private HashMap<String, Object> paciente;
	
	/**
	 * Mapa que contiene los servicios.
	 */
	private HashMap<String, Object> servicios;
	

	/**
	 * 
	 */
	private String informacionRips;
	
	/**
	 * 
	 */
	private String observaciones;
	
	/**
	 * 
	 */
	private String fechaSolicitud;
	
	/**
	 * 
	 */
	private String horaSolicitud;
	
	/**
	 * Bandera que indica si se debe validar la informacion de rips.
	 */
	private boolean validarRips;
	
	/**
	 *  
	 */
	private String tipoServicio;
	
	/**
	 * Codigo de la causa externa de la consulta.
	 */
	private int causaExternaConsulta;
	
	/**
	 * 
	 */
	private String finalidadConsulta;
	
	/**
	 * 
	 */
	private int finalidadProcedimiento;
	
	/**
	 * 
	 */
	private int numDiagnosticos;
	
	/**
	 * 
	 */
	private HashMap diagnosticos;
	
	/**
	 * 
	 */
	private String tipoDiagnosticoPrincipal;
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String, Object>> finalidadesProcedimientos;
	
	/**
	 * 
	 */
	private String selTodas;
	
	/**
	 * 
	 */
	private int sexoServicio;
	
	/**
	 * 
	 */
	private String centroAtencion;
	
	/**
	 * 
	 */
	private String centroCosto;
	
	/**
	 * Fecha de Consulta y Fecha de Procedimiento
	 * */
	private String fechaCP;
	
	/**
     * Se guarda la información necesaria para guardar la justificacion de cada servicio No POS
     */
    private HashMap justificacionesServicios;
    
    /**
     * Variable para mantener el boton en la forma de la justificacion nopos
     */
    private String botonjus;
    
    /**
     * Medico que realiza que registra la terapia Grupal (profesional que reponde) 
     */
    private UsuarioBasico profesionalResponde;
    
    /**
     * Espcialidad seleccionada del Médico que registra la Terapia Grupal
     */
    private  String especialidadProfResponde;
	
	/**
	 * 
	 *
	 */
	public void reset(UsuarioBasico usuario) 
	{
		this.paciente=new HashMap<String, Object>();
		this.servicios=new HashMap<String, Object>();
		this.servicios.put("numRegistros", "0");
		this.diagnosticos=new HashMap<String, Object>();
		this.diagnosticos.put("numRegistros", "0");
		this.informacionRips="false";
		this.observaciones="";
		this.profesionalResponde=new UsuarioBasico();
		this.profesionalResponde=usuario;
		this.especialidadProfResponde = "";
		this.fechaSolicitud=UtilidadFecha.getFechaActual();
		this.horaSolicitud=UtilidadFecha.getHoraActual();
		this.validarRips=UtilidadTexto.getBoolean(ValoresPorDefecto.getEntidadManejaRips(usuario.getCodigoInstitucionInt())+"");
		this.tipoServicio="";
		this.causaExternaConsulta=ConstantesBD.codigoNuncaValido;
		this.finalidadConsulta="";
		this.finalidadProcedimiento=ConstantesBD.codigoNuncaValido;
		this.numDiagnosticos=0;
		this.tipoDiagnosticoPrincipal="";
		this.finalidadesProcedimientos=new ArrayList<HashMap<String, Object>>();
		this.selTodas="false";
		this.sexoServicio=ConstantesBD.codigoNuncaValido;
		this.centroAtencion=usuario.getCodigoCentroAtencion()+"";
		this.centroCosto="";
		this.fechaCP = UtilidadFecha.getFechaActual();
		this.justificacionesServicios=new HashMap();
		this.justificacionesServicios.put("numRegistros", 0);
		this.botonjus="N";
	}


	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores=new ActionErrors();
		if(this.estado.equals("guardar"))
		{
			boolean fechaCorrecta=true;
			boolean horaCorrecta=true;
			if(this.fechaSolicitud.trim().equals(""))
			{
				errores.add("fechaSolicitud",new ActionMessage("errors.required","La fecha de Solicitud"));
				fechaCorrecta=false;
			}
			else
			{
				if(!UtilidadFecha.validarFecha(this.fechaSolicitud))
				{
					errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido",this.fechaSolicitud));
					fechaCorrecta=false;
				}
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaSolicitud,UtilidadFecha.getFechaActual()))
				{
					errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","Fecha ("+this.fechaSolicitud+")","Actual ("+UtilidadFecha.getFechaActual()+")"));
					fechaCorrecta=false;
				}
			}
			if(this.horaSolicitud.trim().equals(""))
			{
				errores.add("horaCambio",new ActionMessage("errors.required","La Hora de Solicitud"));
				fechaCorrecta=false;
			}
			else 
			{
				RespuestaValidacion val=UtilidadFecha.validacionHora(this.horaSolicitud);
				if(!val.puedoSeguir)
				{
					errores.add("error en la hora",new ActionMessage("error.errorEnBlanco",val.textoRespuesta+""));
					fechaCorrecta=false;
				}
			}
			if(fechaCorrecta&&horaCorrecta)
			{
				if(UtilidadTexto.isEmpty(this.getServicios("codigoServicio_0")+""))
				{
					errores.add("",new ActionMessage("errors.required","El Servicio de la terapia"));
				}
				
				//validaciones rips procedimientos
				if(this.tipoServicio.equals(ConstantesBD.codigoServicioProcedimiento+"")&&this.validarRips)
				{
					if(this.finalidadProcedimiento<=0)
					{
						errores.add("",new ActionMessage("errors.required","La Finalidad del Procedimiento"));
					}
															
					//Validación de la fecha de Procedimiento
					if(this.fechaCP.toString().equals(""))						
						errores.add("descripcion",new ActionMessage("errors.required","Fecha de Procedimiento "));				
					else
					{
						//Validación del formato de la fecha de Procedimiento				
						if(!this.fechaCP.toString().equals("") 
								&& !UtilidadFecha.validarFecha(this.fechaCP))				
							errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," de Procedimiento "+this.fechaCP));
					}				
				}
				//validaciones rips interconsultas.
				else if(this.tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+"")&&this.validarRips)
				{
					if(this.causaExternaConsulta<=0)
					{
						errores.add("",new ActionMessage("errors.required","La Causa Externa"));
					}
					if(UtilidadTexto.isEmpty(this.finalidadConsulta))
					{
						errores.add("",new ActionMessage("errors.required","La Finalidad de la Consulta"));
					}
					if(this.diagnosticos.containsKey("principal"))
					{
						if(UtilidadTexto.isEmpty(this.diagnosticos.get("principal")+""))
						{
							errores.add("",new ActionMessage("errors.required","El Diagnostico Principal"));
						}
					}
					else
					{
						errores.add("",new ActionMessage("errors.required","El Diagnostico Principal"));						
					}
					if(UtilidadTexto.isEmpty(tipoDiagnosticoPrincipal))
					{
						errores.add("",new ActionMessage("errors.required","El Tipo de Diagnostico"));
					}
					
					//Validación de la fecha de Consulta
					if(this.fechaCP.toString().equals(""))				
						errores.add("descripcion",new ActionMessage("errors.required","Fecha de Consulta "));					
					else
					{
						//Validación del formato de la fecha de Consulta				
						if(!this.fechaCP.toString().equals("") 
								&& !UtilidadFecha.validarFecha(this.fechaCP))				
							errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," de Consulta "+this.fechaCP));
					}
				}
				
				int pacientesSeleccionados = 0;
				for(int i=0;i<Utilidades.convertirAEntero(paciente.get("numRegistros")+"");i++)
				{
					if(UtilidadTexto.getBoolean(paciente.get("seleccionado_"+i)+""))
					{						
						pacientesSeleccionados++;						
						i =  Utilidades.convertirAEntero(paciente.get("numRegistros")+"");					
					}
				}
				if(pacientesSeleccionados<=0)
				{
					errores.add("error.errorEnBlanco",new ActionMessage("error.errorEnBlanco","Es requerido la seleccion de un paciente."));
				}
				
				
				if(this.especialidadProfResponde.equals(""))
				{
					errores.add("descripcion",new ActionMessage("errors.required"," La especialidad de Profesional que Responde"));
				}
				
				//SE VERIFICA SI SE HA INGRESADO JUSTIFICACIÒN NO POS PARA EL SERVICIO EN CASO DE QUE LO REQUIERA
            	if (this.getServicios("justificar_0").toString().equals("true")){
                	if(!this.getJustificacionesServicios().containsKey("0_servicio"))
                		errores.add("No se ha diligenciado el formato de justificación No POS", new ActionMessage("errors.required","Justificación No POS para el servicio "+this.getServicios("descripcionServicio_0")));
                }
			}
		}
		return errores;
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
	 * @return the paciente
	 */
	public HashMap<String, Object> getPaciente() {
		return paciente;
	}


	/**
	 * @param paciente the paciente to set
	 */
	public void setPaciente(HashMap<String, Object> paciente) {
		this.paciente = paciente;
	}


	/**
	 * @return the paciente
	 */
	public Object getPaciente(String key) 
	{
		return paciente.get(key);
	}


	/**
	 * @param paciente the paciente to set
	 */
	public void setPaciente(String key, Object value) 
	{
		this.paciente.put(key, value);
	}


	/**
	 * @return the servicios
	 */
	public HashMap<String, Object> getServicios() {
		return servicios;
	}


	/**
	 * @param servicios the servicios to set
	 */
	public void setServicios(HashMap<String, Object> servicios) {
		this.servicios = servicios;
	}
	
	/**
	 * @return the servicios
	 */
	public Object getServicios(String key) 
	{
		return servicios.get(key);
	}


	/**
	 * @param servicios the servicios to set
	 */
	public void setServicios(String key,Object value) 
	{
		this.servicios.put(key, value);
	}


	/**
	 * @return the informacionRips
	 */
	public String getInformacionRips() {
		return informacionRips;
	}


	/**
	 * @param informacionRips the informacionRips to set
	 */
	public void setInformacionRips(String informacionRips) {
		this.informacionRips = informacionRips;
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
	 * @return the fechaSolicitud
	 */
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}


	/**
	 * @param fechaSolicitud the fechaSolicitud to set
	 */
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}


	/**
	 * @return the horaSolicitud
	 */
	public String getHoraSolicitud() {
		return horaSolicitud;
	}


	/**
	 * @param horaSolicitud the horaSolicitud to set
	 */
	public void setHoraSolicitud(String horaSolicitud) {
		this.horaSolicitud = horaSolicitud;
	}


	/**
	 * @return the validarRips
	 */
	public boolean isValidarRips() {
		return validarRips;
	}


	/**
	 * @param validarRips the validarRips to set
	 */
	public void setValidarRips(boolean validarRips) {
		this.validarRips = validarRips;
	}


	/**
	 * @return the tipoServicio
	 */
	public String getTipoServicio() {
		return tipoServicio;
	}


	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}


	/**
	 * @return the causaExternaConsulta
	 */
	public int getCausaExternaConsulta() {
		return causaExternaConsulta;
	}


	/**
	 * @param causaExternaConsulta the causaExternaConsulta to set
	 */
	public void setCausaExternaConsulta(int causaExternaConsulta) {
		this.causaExternaConsulta = causaExternaConsulta;
	}


	/**
	 * @return the finalidadConsulta
	 */
	public String getFinalidadConsulta() {
		return finalidadConsulta;
	}


	/**
	 * @param finalidadConsulta the finalidadConsulta to set
	 */
	public void setFinalidadConsulta(String finalidadConsulta) {
		this.finalidadConsulta = finalidadConsulta;
	}


	/**
	 * @return the finalidadProcedimiento
	 */
	public int getFinalidadProcedimiento() {
		return finalidadProcedimiento;
	}


	/**
	 * @param finalidadProcedimiento the finalidadProcedimiento to set
	 */
	public void setFinalidadProcedimiento(int finalidadProcedimiento) {
		this.finalidadProcedimiento = finalidadProcedimiento;
	}


	/**
	 * @return the diagnosticos
	 */
	public HashMap getDiagnosticos() {
		return diagnosticos;
	}


	/**
	 * @param diagnosticos the diagnosticos to set
	 */
	public void setDiagnosticos(HashMap diagnosticos) {
		this.diagnosticos = diagnosticos;
	}


	/**
	 * @return the diagnosticos
	 */
	public Object getDiagnosticos(String key) 
	{
		return diagnosticos.get(key);
	}


	/**
	 * @param diagnosticos the diagnosticos to set
	 */
	public void setDiagnosticos(String key,Object value) 
	{
		this.diagnosticos.put(key, value);
	}

	/**
	 * @return the numDiagnosticos
	 */
	public int getNumDiagnosticos() {
		return numDiagnosticos;
	}


	/**
	 * @param numDiagnosticos the numDiagnosticos to set
	 */
	public void setNumDiagnosticos(int numDiagnosticos) {
		this.numDiagnosticos = numDiagnosticos;
	}


	/**
	 * @return the codigoTipoDiagnosticoPrincipal
	 */
	public String getTipoDiagnosticoPrincipal() {
		return tipoDiagnosticoPrincipal;
	}


	/**
	 * @param codigoTipoDiagnosticoPrincipal the codigoTipoDiagnosticoPrincipal to set
	 */
	public void setTipoDiagnosticoPrincipal(String tipoDiagnosticoPrincipal) {
		this.tipoDiagnosticoPrincipal = tipoDiagnosticoPrincipal;
	}


	/**
	 * @return the finalidadesProcedimientos
	 */
	public ArrayList<HashMap<String, Object>> getFinalidadesProcedimientos() {
		return finalidadesProcedimientos;
	}


	/**
	 * @param finalidadesProcedimientos the finalidadesProcedimientos to set
	 */
	public void setFinalidadesProcedimientos(ArrayList<HashMap<String, Object>> finalidadesProcedimientos) 
	{
		this.finalidadesProcedimientos = finalidadesProcedimientos;
	}


	public String getSelTodas() {
		return selTodas;
	}


	public void setSelTodas(String selTodas) {
		this.selTodas = selTodas;
	}


	public int getSexoServicio() {
		return sexoServicio;
	}


	public void setSexoServicio(int sexoServicio) {
		this.sexoServicio = sexoServicio;
	}


	public String getCentroAtencion() {
		return centroAtencion;
	}


	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


	public String getCentroCosto() {
		return centroCosto;
	}


	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}


	/**
	 * @return the fechaCP
	 */
	public String getFechaCP() {
		return fechaCP;
	}


	/**
	 * @param fechaCP the fechaCP to set
	 */
	public void setFechaCP(String fechaCP) {
		this.fechaCP = fechaCP;
	}
	
	/**
	 * @return the justificacionesServicios
	 */
	public HashMap getJustificacionesServicios() {
		return justificacionesServicios;
	}

	/**
	 * @param justificacionesServicios the justificacionesServicios to set
	 */
	public void setJustificacionesServicios(HashMap justificacionesServicios) {
		this.justificacionesServicios = justificacionesServicios;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getJustificacionesServicios(String key) {
		return justificacionesServicios.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param obj
	 */
	public void setJustificacionesServicios(String key, Object obj) {
		this.justificacionesServicios.put(key, obj);
	}


	public String getBotonjus() {
		return botonjus;
	}


	public void setBotonjus(String botonjus) {
		this.botonjus = botonjus;
	}


	/**
	 * @return the profesionalResponde
	 */
	public UsuarioBasico getProfesionalResponde() {
		return profesionalResponde;
	}


	/**
	 * @param profesionalResponde the profesionalResponde to set
	 */
	public void setProfesionalResponde(UsuarioBasico profesionalResponde) {
		this.profesionalResponde = profesionalResponde;
	}


	/**
	 * @return the especialidadProfResponde
	 */
	public String getEspecialidadProfResponde() {
		return especialidadProfResponde;
	}


	/**
	 * @param especialidadProfResponde the especialidadProfResponde to set
	 */
	public void setEspecialidadProfResponde(String especialidadProfResponde) {
		this.especialidadProfResponde = especialidadProfResponde;
	}
}
