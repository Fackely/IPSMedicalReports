package com.princetonsa.actionform.consultaExterna;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.administracion.DtoProfesional;
import com.princetonsa.dto.consultaExterna.DtoConsultorios;
import com.princetonsa.dto.consultaExterna.DtoExcepcionesHorarioAtencion;
import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.consultaExterna.ExcepcionesHorarioAtencion;

@SuppressWarnings("serial")
public class ExcepcionesHorarioAtencionForm extends ActionForm{
	
	/**
	 * 
	 */
	private String estado;
	private String estadoAnterior;
	private ArrayList<HashMap<String, Object>>centrosAtencion = new ArrayList<HashMap<String,Object>>();
	private ArrayList<DtoProfesional> profesionales = new ArrayList<DtoProfesional>();
	private ArrayList<DtoExcepcionesHorarioAtencion> listaDtoExcepcionesha = new ArrayList<DtoExcepcionesHorarioAtencion>();
	private ArrayList<DtoConsultorios> listaDtoConsultorios = new ArrayList<DtoConsultorios>();
    private ArrayList<Usuario> listaDtoUsuario = new ArrayList<Usuario>();
    private int posicion;
	private HashMap listadoUnidadesConsulta;
	private String indicador;
	private int centroAtencion;
	private int profesionalSalud; 
	
	private int indicador1;
	boolean exito;
	//Atributos de ordenación
	private String patronOrdenar;
	private String esDescendente;
	
	//Atributos para paginar
	private String linkSiguiente;
	private int maxPageItems;
	
	
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errores=new ActionErrors();
		UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
	
		if(estado.equals("guardar") || estado.equals("actualizar"))
		{ 
	
			
					DtoExcepcionesHorarioAtencion dtoExcepciones=this.listaDtoExcepcionesha.get(posicion);
					// verifica quee el campo funcionalidad no este vacio
					if (dtoExcepciones.getDtoCentroAtencion().getConsecutivo()<=0)
					{
						estado="nuevo";
						errores.add("centroAtencion", new ActionMessage("errors.required","El Campo Centro de Atencion   "));
						
					}
					
					if (dtoExcepciones.getUnidadAgenda()<=0)
					{
						estado="nuevo";
						errores.add("UnidadAgenda", new ActionMessage("errors.required","El Campo Unidad de Agenda  "));
						
					}
					
					
					
					if (dtoExcepciones.getConsultorio()<=0)
					{
						estado="nuevo";
						errores.add("consultorio", new ActionMessage("errors.required","El Campo Consultorio  "));
						
					}
					if (getProfesionalSalud()<=0)
					{
						estado="nuevo";
						errores.add("profesional", new ActionMessage("errors.required","El Campo Profesional de la Salud   "));
						
					}
				
					if (dtoExcepciones.getFechaInicio().equals(""))
					{
						estado="nuevo";
						errores.add("fechainicio", new ActionMessage("errors.required","El Campo Fecha de Inicio  "));
						
					}
					else 
					{
						if (UtilidadFecha.esFechaMenorQueOtraReferencia(dtoExcepciones.getFechaInicio(), UtilidadFecha.getFechaActual()))	
						{
							estado="nuevo";
							errores.add("FechaInicio", new ActionMessage("errors.fechaAnteriorIgualActual","El Campo Fecha de Inicio ("+dtoExcepciones.getFechaInicio()+")", " Actual ("+UtilidadFecha.getFechaActual()+")"));
						}
					}
					
					
					
					if (dtoExcepciones.getFechaFin().equals(""))
					{
						estado="nuevo";
						errores.add("fechafinaliza", new ActionMessage("errors.required","El Campo Fecha de Finalización   "));
						
					}else 
					{
						if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(dtoExcepciones.getFechaInicio(), dtoExcepciones.getFechaFin()))	
						{
							estado="nuevo";
							errores.add("FechaInicio", new ActionMessage("errors.fechaAnteriorIgualActual","El Campo Fecha de Finalizacion("+dtoExcepciones.getFechaFin()+")", "Inicio ("+dtoExcepciones.getFechaInicio()+")"));
						}
					}
					
					if (dtoExcepciones.getHoraInicio().equals(""))
					{
						estado="nuevo";
						errores.add("horaInicio", new ActionMessage("errors.required","El Campo Hora de Inicio   "));
						
					}
					if (dtoExcepciones.getHoraFin().equals(""))
					{
						estado="nuevo";
						errores.add("horaFinaliza", new ActionMessage("errors.required","El Campo Hora Finalización  "));
						
					}
		
		
					if(!UtilidadFecha.validacionHora(dtoExcepciones.getHoraInicio()).puedoSeguir)
					{
						estado="nuevo";
						errores.add("horaFinaliza", new ActionMessage("errors.formatoHoraInvalido"," "+dtoExcepciones.getHoraInicio()));
					}
					
					if(!UtilidadFecha.validacionHora(dtoExcepciones.getHoraFin()).puedoSeguir)
					{
						estado="nuevo";
						errores.add("horaFinaliza", new ActionMessage("errors.formatoHoraInvalido"," "+dtoExcepciones.getHoraFin()));
					}
					
					if(!UtilidadFecha.esHoraMenorQueOtraReferencia(dtoExcepciones.getHoraInicio(), dtoExcepciones.getHoraFin())){
						estado="nuevo";
						errores.add("horaFinaliza", new ActionMessage("errors.horaPosteriorAOtraDeReferencia","El Campo Hora de inicio", "de finalización"));
					}
					
					//Si posición esta lleno es porque se modifica o se guarda, y las validaciones solo aplican para esos momentos
					//Realizo las validaciones por anexo 145740
					Connection con=UtilidadBD.abrirConexion();
					ArrayList<DtoExcepcionesHorarioAtencion> listadoHAValidacion=ExcepcionesHorarioAtencion.listar(con,usuario.getCodigoInstitucionInt(),this.listaDtoExcepcionesha.get(posicion).getDtoCentroAtencion().getConsecutivo(), profesionalSalud);
					try
					{
						UtilidadBD.cerrarConexion(con);
					}
					catch (Exception e) {
						Log4JManager.error("ERROR CERRANDO LA CONEXION PARA CONSULTAR EL LISTADO DE HORARIOS DE ATENCION!!! :(");
					}
					for (DtoExcepcionesHorarioAtencion dto:listadoHAValidacion)
					{
						//1. Tiene = consultorio,= rango de fechas, diferente profesional y = horario
						if(UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaInicio()).equals(UtilidadFecha.conversionFormatoFechaAAp(this.listaDtoExcepcionesha.get(posicion).getFechaInicio()))
						&&
						UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaFin()).equals(UtilidadFecha.conversionFormatoFechaAAp(this.listaDtoExcepcionesha.get(posicion).getFechaFin()))
						&&
						dto.getConsultorio()==this.listaDtoExcepcionesha.get(posicion).getConsultorio()
						&&
						dto.getHoraInicio().equals(this.listaDtoExcepcionesha.get(posicion).getHoraInicio())
						&&
						dto.getHoraFin().equals(this.listaDtoExcepcionesha.get(posicion).getHoraFin())
						)
						{
							estado="nuevo";
							errores.add("horaFinaliza", new ActionMessage("errors.notEspecific","Se cruza con una excepción registrada para el mismo consultorio, rango de fechas y rango de horas, Por favor Verifique. "));
						}
						//2. El mismo profesional,=rango de fechas, = horas y el consultorio es diferente
						if(UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaInicio()).equals(UtilidadFecha.conversionFormatoFechaAAp(this.listaDtoExcepcionesha.get(posicion).getFechaInicio()))
						&&
						UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaFin()).equals(UtilidadFecha.conversionFormatoFechaAAp(this.listaDtoExcepcionesha.get(posicion).getFechaFin()))
						&&
						dto.getHoraInicio().equals(this.listaDtoExcepcionesha.get(posicion).getHoraInicio())
						&&
						dto.getHoraFin().equals(this.listaDtoExcepcionesha.get(posicion).getHoraFin())
						&&
						dto.getConsultorio()!=this.listaDtoExcepcionesha.get(posicion).getConsultorio()
						)
						{
							estado="nuevo";
							errores.add("horaFinaliza", new ActionMessage("errors.notEspecific","Se cruza con una excepción registrada para el mismo profesional de la salud, rango de fechas y rango de horas, Por favor Verifique."));
						}
						
						//3.= horarios, = centro atencion, = unidad agenda, = conltorio, = profesional, =fechas, = horas
						if(
						UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaInicio()).equals(UtilidadFecha.conversionFormatoFechaAAp(this.listaDtoExcepcionesha.get(posicion).getFechaInicio()))
						&&
						UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaFin()).equals(UtilidadFecha.conversionFormatoFechaAAp(this.listaDtoExcepcionesha.get(posicion).getFechaFin()))
						&&
						dto.getHoraInicio().equals(this.listaDtoExcepcionesha.get(posicion).getHoraInicio())
						&&
						dto.getHoraFin().equals(this.listaDtoExcepcionesha.get(posicion).getHoraFin())
						&&
						dto.getConsultorio()==this.listaDtoExcepcionesha.get(posicion).getConsultorio()
						&&
						dto.getCentroAtencion()==this.listaDtoExcepcionesha.get(posicion).getCentroAtencion()
						&&
						dto.getUnidadAgenda()==this.listaDtoExcepcionesha.get(posicion).getUnidadAgenda()
						)
						{
							estado="nuevo";
							errores.add("horaFinaliza", new ActionMessage("errors.notEspecific","Se cruza con una excepción registrada con los mismos campos seleccionados para el nuevo ingreso, Por favor Verifique. "));
						}
					}
					
					
					
					
					
					
		}
		
		return errores;
		
	}
	
	
	
	
	public void reset()
	{
		this.estado = "";
		this.estadoAnterior = "";
		this.centrosAtencion = new  ArrayList<HashMap<String,Object>>();
		this.profesionales = new  ArrayList<DtoProfesional>();
		this.profesionalSalud = ConstantesBD.codigoNuncaValido;
		this.listaDtoExcepcionesha = new ArrayList<DtoExcepcionesHorarioAtencion>();
		this.posicion = ConstantesBD.codigoNuncaValido; 
		this.listadoUnidadesConsulta=new HashMap();
		this.listaDtoConsultorios= new ArrayList<DtoConsultorios>();
		this.centroAtencion=ConstantesBD.codigoNuncaValido;
		this.indicador = "";
		this.listaDtoUsuario=new ArrayList<Usuario>();
		this.indicador1=0;
		
		this.patronOrdenar = "";
		this.esDescendente = "";
		this.exito=false;
		this.linkSiguiente = "";
		this.maxPageItems = 0;
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
	 * @return the centrosAtencion
	 */
	public ArrayList<HashMap<String, Object>> getCentrosAtencion() {
		return centrosAtencion;
	}



	/**
	 * @param centrosAtencion the centrosAtencion to set
	 */
	public void setCentrosAtencion(
			ArrayList<HashMap<String, Object>> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}



	/**
	 * @return the listaDtoExcepcionesha
	 */
	public ArrayList<DtoExcepcionesHorarioAtencion> getListaDtoExcepcionesha() {
		return listaDtoExcepcionesha;
	}



	/**
	 * @param listaDtoExcepcionesha the listaDtoExcepcionesha to set
	 */
	public void setListaDtoExcepcionesha(
			ArrayList<DtoExcepcionesHorarioAtencion> listaDtoExcepcionesha) {
		this.listaDtoExcepcionesha = listaDtoExcepcionesha;
	}






	/**
	 * @return the posicion
	 */
	public int getPosicion() {
		return posicion;
	}



	/**
	 * @param posicion the posicion to set
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}



	/**
	 * @return the listadoUnidadesConsulta
	 */
	public HashMap getListadoUnidadesConsulta() {
		return listadoUnidadesConsulta;
	}



	/**
	 * @param listadoUnidadesConsulta the listadoUnidadesConsulta to set
	 */
	public void setListadoUnidadesConsulta(HashMap listadoUnidadesConsulta) {
		this.listadoUnidadesConsulta = listadoUnidadesConsulta;
	}



	/**
	 * @return the listaDtoConsultorios
	 */
	public ArrayList<DtoConsultorios> getListaDtoConsultorios() {
		return listaDtoConsultorios;
	}



	/**
	 * @param listaDtoConsultorios the listaDtoConsultorios to set
	 */
	public void setListaDtoConsultorios(
			ArrayList<DtoConsultorios> listaDtoConsultorios) {
		this.listaDtoConsultorios = listaDtoConsultorios;
	}



	/**
	 * @return the indicador
	 */
	public String getIndicador() {
		return indicador;
	}



	/**
	 * @param indicador the indicador to set
	 */
	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}



	/**
	 * @return the profesionales
	 */
	public ArrayList<DtoProfesional> getProfesionales() {
		return profesionales;
	}



	/**
	 * @param profesionales the profesionales to set
	 */
	public void setProfesionales(ArrayList<DtoProfesional> profesionales) {
		this.profesionales = profesionales;
	}
	
	
	public int getNumCentrosAtencion()
	{
		return this.centrosAtencion.size();
	}



	/**
	 * @return the listaDtoUsuario
	 */
	public ArrayList<Usuario> getListaDtoUsuario() {
		return listaDtoUsuario;
	}



	/**
	 * @param listaDtoUsuario the listaDtoUsuario to set
	 */
	public void setListaDtoUsuario(ArrayList<Usuario> listaDtoUsuario) {
		this.listaDtoUsuario = listaDtoUsuario;
	}



	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}



	/**
	 * @param centroAtencionP the centroAtencionP to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


	/**
	 * Método que me retorna el numero de registros existentes del listado de envio email
	 * @return
	 */
	public int getNumListadoEnvio()
	{
		int contador = 0;
		ArrayList<DtoExcepcionesHorarioAtencion> excepciones=this.listaDtoExcepcionesha;
		contador= excepciones.size();
		return contador;
	}
	
	
	/**
	 * Método para obtener el numero de registros lógico del listado de excepciones
	 * @return
	 */
	public int getNumListadoExcepcionesVista()
	{
		int contador = 0;
		
		for(DtoExcepcionesHorarioAtencion excepcion:this.listaDtoExcepcionesha)
		{
			if(!UtilidadTexto.getBoolean(excepcion.getEliminado()))
			{
				contador++;
			}
		}
		return contador;
	}



	/**
	 * @return the indicador1
	 */
	public int getIndicador1() {
		return indicador1;
	}



	/**
	 * @param indicador1 the indicador1 to set
	 */
	public void setIndicador1(int indicador1) {
		this.indicador1 = indicador1;
	}



	/**
	 * @return the estadoAnterior
	 */
	public String getEstadoAnterior() {
		return estadoAnterior;
	}



	/**
	 * @param estadoAnterior the estadoAnterior to set
	 */
	public void setEstadoAnterior(String estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
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
	 * @return the esDescendente
	 */
	public String getEsDescendente() {
		return esDescendente;
	}



	/**
	 * @param esDescendente the esDescendente to set
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}



	/**
	 * @return the exito
	 */
	public boolean isExito() {
		return exito;
	}



	/**
	 * @param exito the exito to set
	 */
	public void setExito(boolean exito) {
		this.exito = exito;
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
	 * Obtiene el valor del atributo profesionalSalud
	 *
	 * @return Retorna atributo profesionalSalud
	 */
	public int getProfesionalSalud()
	{
		return profesionalSalud;
	}




	/**
	 * Establece el valor del atributo profesionalSalud
	 *
	 * @param valor para el atributo profesionalSalud
	 */
	public void setProfesionalSalud(int profesionalSalud)
	{
		this.profesionalSalud = profesionalSalud;
	}

	/**
	 * Obtener la cantidad de profesionales
	 * @return int cantidad de profesionales
	 */
	public int getCantidadProfesionales()
	{
		return profesionales.size();
	}
	
}