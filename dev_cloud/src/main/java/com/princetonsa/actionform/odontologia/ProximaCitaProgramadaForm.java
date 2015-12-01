
package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.odontologia.InfoServicios;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.odontologia.DtoProcesoCitaProgramada;
import com.princetonsa.dto.odontologia.DtoProximaCita;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;

/**
 * Form que contiene los datos espec&iacute;ficos para la funcionalidad 
 * Proceso Próxima Cita - Anexo 1120
 * 
 * Adem&aacute;s maneja el proceso de validaci&oacute;n de errores de datos de entrada.
 *
 * @author Jorge Armando Agudelo Quintero
 */
public class ProximaCitaProgramadaForm extends ActionForm{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo utilizado para el control del flujo de la aplicaci&oacute;n
	 */
	private String estado;
	
	/**
	 * Arreglo con los servicios disponibles a programar
	 */
	private ArrayList<DtoServicioOdontologico> servicios;

	/**
	 * Fecha de la próxima cita programada
	 */
	private Date fechaProximaCita;
	
	/**
	 * Atributo que indica cual es el código de la funcionalidad que origina el 
	 * llamado al proceso centralizado de Próxima Cita Programada
	 */
	private int codigoFuncionalidad;
	
	
	/**
	 * Objeto que contiene toda la información necesaria para realizar
	 * el proceso de programación de la Próxima Cita desde cualquier
	 * funcionalidad.
	 */
	private DtoProcesoCitaProgramada dtoProcesoCitaProgramada;
	
	/**
	 * Objeto que contiene la información de la cita que se va a programar.
	 */
	private DtoProximaCita dtoProximaCita;
	
	/**
	 * Atributo que indica si la Institución maneja programas odontológicos.
	 */
	private boolean utilizaInstitucionProgramasOdontologicos;
	
	/**
	 * Atributo con el múltiplo en minutos para generar una cita 
	 * odontológica.
	 */
	private String multiploMinutosGeneracionCita;

	/**
	 * Duración total de los servicios asociados a la cita programada
	 */
	private int totalMinutosDuracion;
	
	/**
	 * Tamaño del listado que contiene los Programas / Servicios 
	 * que se pueden asociar a la cita programada
	 */
	private int sizeProgramasServicios;
	
	/**
	 * Paciente al cual se le va a asociar la cita programada
	 */
	private DtoPersonas datosPaciente;
	
	/**
	 * Atributo que indica si se debe mostrar
	 * el resumen de la cita programada
	 */
	private boolean resumen;
	
	/**
	 * Constructor
	 */
	public ProximaCitaProgramadaForm(){
		
		reset();
	}
	
	
	
	
	/**
	 * Método que reinicia los valores de los atributos de la forma.
	 */
	public void reset (){
		
		estado = "";
		fechaProximaCita = UtilidadFecha.getFechaActualTipoBD();
		//codigoFuncionalidad = ConstantesBD.codigoNuncaValido;
		dtoProcesoCitaProgramada = new DtoProcesoCitaProgramada();
		dtoProximaCita =  new DtoProximaCita();
		utilizaInstitucionProgramasOdontologicos = false;
		multiploMinutosGeneracionCita = "";
		totalMinutosDuracion = 0;
		sizeProgramasServicios = 0;
		resumen = false;
	}
	
	/**
	 * M&eacute;todo encargado de procesar los errores presentados
	 * @param forma
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

		ActionErrors errores = new ActionErrors();
		
		MessageResources mensages = MessageResources.getMessageResources("com.servinte.mensajes.odontologia.ProximaCitaProgramadaForm");
		String mensaje = "";
	
		if (UtilidadTexto.isEmpty(getEstado())) {
			
			errores.add("estado invalido", new ActionMessage("errors.estadoInvalido"));
			return errores;
		
		}
//		else if (estado.equals("totalizarDuracionCita") || estado.equals("activarProgramaServicio")){
//			
//			ArrayList<InfoServicios> servicios = getDtoProximaCita().getTodosServicios();
//			
//			if(getDtoProximaCita().isExistenServiciosAsociados()){
//
//				if (servicios!=null){
//					
//					for (InfoServicios servicio : servicios) {
//						
//						if(servicio.getServicio().isActivo() && servicio.getDuracionCita()<=0){
//							
//							mensaje = mensages.getMessage("ProximaCitaProgramadaForm.error.definirDuracionServicio");
//							
//							errores.add("Requerido Duración", new ActionMessage("errors.notEspecific", mensaje));
//						}
//					}
//				}
//			}
//			
//			mapping.findForward("totalizarDuracionCita");
//			
//		}
		else if ("guardarProximaCita".equals(getEstado())){
			
			if(getFechaProximaCita()!=null){
				
				if(getFechaProximaCita().getTime() < UtilidadFecha.getFechaActualTipoBD().getTime()){
					
					mensaje = mensages.getMessage("ProximaCitaProgramadaForm.error.fechaMayorFechaActual");
					
					errores.add("Fecha Mayor Fecha Actual", new ActionMessage("errors.notEspecific", mensaje));
				}
			}else{
				
				mensaje = mensages.getMessage("ProximaCitaProgramadaForm.error.definirFechaProximaCita");
				
				errores.add("Definir Fecha", new ActionMessage("errors.notEspecific", mensaje));
			}
			
			ArrayList<InfoServicios> servicios = getDtoProximaCita().getTodosServicios();
			
			if(getDtoProximaCita().isExistenServiciosAsociados()){

				if (servicios!=null){
					
					for (InfoServicios servicio : servicios) {
						
						if(servicio.getServicio().isActivo()){
							
							if(servicio.getDuracionCita()<=0){
								
								mensaje = mensages.getMessage("ProximaCitaProgramadaForm.error.definirDuracionServicio");
								errores.add("Requerido Duración", new ActionMessage("errors.notEspecific", mensaje));
							
							}else if(servicio.getDuracionCita() % Utilidades.convertirAEntero(getMultiploMinutosGeneracionCita()) !=0){
							
								mensaje = mensages.getMessage("ProximaCitaProgramadaForm.error.duracionDebeSerMultiplo", getMultiploMinutosGeneracionCita());
								errores.add("Valor debe ser Múltiplo", new ActionMessage("errors.notEspecific", mensaje));
							}
						}
					}
				}
			}else{
				
				mensaje = mensages.getMessage("ProximaCitaProgramadaForm.error.asociarServicioACita");
				
				errores.add("Asociar Servicio Cita", new ActionMessage("errors.notEspecific", mensaje));
			}
		}
		
		return errores;
	}



	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}



	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}



	/**
	 * @param servicios the servicios to set
	 */
	public void setServicios(ArrayList<DtoServicioOdontologico> servicios) {
		this.servicios = servicios;
	}



	/**
	 * @return the servicios
	 */
	public ArrayList<DtoServicioOdontologico> getServicios() {
		return servicios;
	}


	/**
	 * @param fechaProximaCita the fechaProximaCita to set
	 */
	public void setFechaProximaCita(Date fechaProximaCita) {
		this.fechaProximaCita = fechaProximaCita;
	}


	/**
	 * @return the fechaProximaCita
	 */
	public Date getFechaProximaCita() {
		return fechaProximaCita;
	}


	/**
	 * @param codigoFuncionalidad the codigoFuncionalidad to set
	 */
	public void setCodigoFuncionalidad(int codigoFuncionalidad) {
		this.codigoFuncionalidad = codigoFuncionalidad;
	}


	/**
	 * @return the codigoFuncionalidad
	 */
	public int getCodigoFuncionalidad() {
		return codigoFuncionalidad;
	}


	/**
	 * @param dtoProcesoCitaProgramada the dtoProcesoCitaProgramada to set
	 */
	public void setDtoProcesoCitaProgramada(DtoProcesoCitaProgramada dtoProcesoCitaProgramada) {
		this.dtoProcesoCitaProgramada = dtoProcesoCitaProgramada;
	}


	/**
	 * @return the dtoProcesoCitaProgramada
	 */
	public DtoProcesoCitaProgramada getDtoProcesoCitaProgramada() {
		return dtoProcesoCitaProgramada;
	}


	/**
	 * @param dtoProximaCita the dtoProximaCita to set
	 */
	public void setDtoProximaCita(DtoProximaCita dtoProximaCita) {
		this.dtoProximaCita = dtoProximaCita;
	}


	/**
	 * @return the dtoProximaCita
	 */
	public DtoProximaCita getDtoProximaCita() {
		return dtoProximaCita;
	}


	/**
	 * @param utilizaInstitucionProgramasOdontologicos the utilizaInstitucionProgramasOdontologicos to set
	 */
	public void setUtilizaInstitucionProgramasOdontologicos(
			boolean utilizaInstitucionProgramasOdontologicos) {
		this.utilizaInstitucionProgramasOdontologicos = utilizaInstitucionProgramasOdontologicos;
	}


	/**
	 * @return the utilizaInstitucionProgramasOdontologicos
	 */
	public boolean isUtilizaInstitucionProgramasOdontologicos() {
		return utilizaInstitucionProgramasOdontologicos;
	}


	/**
	 * @param multiploMinutosGeneracionCita the multiploMinutosGeneracionCita to set
	 */
	public void setMultiploMinutosGeneracionCita(
			String multiploMinutosGeneracionCita) {
		this.multiploMinutosGeneracionCita = multiploMinutosGeneracionCita;
	}


	/**
	 * @return the multiploMinutosGeneracionCita
	 */
	public String getMultiploMinutosGeneracionCita() {
		return multiploMinutosGeneracionCita;
	}

	/**
	 * @param totalMinutosDuracion the totalMinutosDuracion to set
	 */
	public void setTotalMinutosDuracion(int totalMinutosDuracion) {
		this.totalMinutosDuracion = totalMinutosDuracion;
	}

	/**
	 * @return the totalMinutosDuracion
	 */
	public int getTotalMinutosDuracion() {
		return totalMinutosDuracion;
	}

	/**
	 * @param sizeProgramasServicios the sizeProgramasServicios to set
	 */
	public void setSizeProgramasServicios(int sizeProgramasServicios) {
		this.sizeProgramasServicios = sizeProgramasServicios;
	}

	/**
	 * @return the sizeProgramasServicios
	 */
	public int getSizeProgramasServicios() {
		return sizeProgramasServicios;
	}

	/**
	 * @param datosPaciente the datosPaciente to set
	 */
	public void setDatosPaciente(DtoPersonas datosPaciente) {
		this.datosPaciente = datosPaciente;
	}

	/**
	 * @return the datosPaciente
	 */
	public DtoPersonas getDatosPaciente() {
		return datosPaciente;
	}

	/**
	 * @param resumen the resumen to set
	 */
	public void setResumen(boolean resumen) {
		this.resumen = resumen;
	}

	/**
	 * @return the resumen
	 */
	public boolean isResumen() {
		return resumen;
	}
}
