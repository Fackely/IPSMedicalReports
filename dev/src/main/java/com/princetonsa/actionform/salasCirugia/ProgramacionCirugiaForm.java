/*
 * Creado el 21-nov-2005
 * por Julian Montoya
 */
package com.princetonsa.actionform.salasCirugia;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;


/**
 * @author Julian Montoya
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */
public class ProgramacionCirugiaForm extends ValidatorForm {
	
	
	/**
	 * Estado para el manejo del flujo de la funcionalidad
	 */
	private String estado;

	/**
	 * Variable que guarda el estado para saber a que 
	 * pagina debe retornar una vez guarde la programación 
	 */
	private String estadoAnterior;
	
	/**
	 * Numero de la peticion
	 */
	private int numeroPeticion;
	
	/**
	 * Fecha de la Peticion 
	 */	
	private String fechaPeticion;
	
	/**
	 * Hora Peticion
	 * */
	private String horaPeticion;
	
	/**
	 *  Fecha estimada de Cirugia
	 */
	private String fechaEstimadaCirugia;
	
	/**
	 *  Nombre del Medico que solicita 
	 */
	private String solicitante;
	
	/**
	 * Duracion Aproximada de la cirugia
	 */
	private String duracion;

	/**
	 * Para Saber por cual de los campos ordenar 
	 */
	private String patronOrdenar;
	
	/**
	 * variable para ordenar el ultimo patron por el que se ordeno
	 */
	private String ultimoPatronOrdenar;
	
	/**
	 * Numero del Servicio inicial (consecutivo)
	 */
	private int nroIniServicio;

	/**
	 * Numero del Servicio Final (consecutivo)
	 */
	private int nroFinServicio;
	
	
	/**
	 * Fecha inicial peticion del servicio
	 */
	private String fechaIniPeticion;
	
	/**
	 * Fecha final de peticion del servicio 
	 */
	private String fechaFinPeticion;
	
	/**
	 * Fecha Inicial de Cirugia 
	 */
	private String fechaIniCirugia;

	/**
	 * Fecha Inicial de Cirugia 
	 */
	private String fechaFinCirugia;
	
	/**
	 * Fecha para manejar la navegacion
	 */
	private String fechaProgramacion;
	
	/**
	 * Para Almacenar la Hora de Inicio de Programacion
	 */
	private String horaInicioProgramacion;

	/**
	 * Para Almacenar la Hora Final de Programacion
	 */
	private String horaFinProgramacion;
	
	/**
	 * Variable para almacenar el codigo
	 * de la sala donde se va a programar 
	 */
	private int nroSala;


	/**
	 * Medico que solicita la peticion 
	 */
	private int profesional;
	
	/**
	 * Variable para Saber el estado de la inserción
	 */
	private int estadoInsercion;
	
	/**
	 * Variable para saber si se esta programando reprogramando
	 */
	private boolean esProgramacion;
	
	
	/**
	 * Variable para almacenar la informacion de peticion de cirugia
	 */
	private Collection informacionPeticion;

	/**
	 * Variable para almacenar el codigo y nombre de las salas
	 */
	private Collection listaSalas;

	/**
	 * Variable para almacenar la petiones 
	 */
	private Collection listaPeticiones;

	/**
	 * Variable para almacenar la petiones 
	 */
	private Collection listaServicios;
	
	/**
	 * Mapa para almacenar los servicios (de petion o de ordenes)
	 *  
	 */
	private HashMap mapaServicios;

	/**
	 * Mapa para almacenar las peticiones.
	 *  
	 */
	private HashMap mapaPeticiones;
	
	/**
	 * Colección para almacenar las horas ya programadas  
	 */
	private Collection  listaSalasProgramadas;
	
	/**
	 * String para almacenar el estado de la  peticion en la busqueda avanzada 
	 * de reprogramacion de cirugias.  
	 */
	private String estadoPeticion;
	
	/**
	 * Variable para manejar el nro de filas de la grilla deonde se 
	 * muestra el cronograma.
	 */
	private int nroFilasGrilla;
	
	/**
	 * Para Postular el tipo de anestesia registrado en la preanestesia.
	 */
	private int tipoAnestesiaPreanestesia;
	
    
    /**
     * almacena el ultimo indice por el 
     * cual se ordeno el HashMap
     */
    private String ultimoPatron;

    /**
     * Varible para almacenar el centro de atencion
     */
    private int centroAtencion;
    
    /**
     * Indicador de Permisos para la funcionalidad de Pedido Quirurgico
     * */
    private String indicadorPermisoFunc;
    
    /**
     * Motivo de la cancelacion 
     */
    private String motivoCancelacion;
    

	
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();

		if( estado.equals("salir") || estado.equals("empezar") || estado.equals("reprogramacion")  )
		{
			if(!UtilidadFecha.validarFecha(this.getFechaProgramacion()))
			{
				errores.add("FechaProgramacion", new ActionMessage("errors.formatoFechaInvalido", this.getFechaProgramacion()));							
			}
		}	
		
		if(estado.equals("cancelarProgramacion"))
		{
			if(this.motivoCancelacion.equals("") || this.motivoCancelacion.equals("null"))
			{
				errores.add("Motivo Cancelación", new ActionMessage("errors.required", "Motivo Cancelación"));
			}
			
		}
	
		return errores;
	}
	
	/**
	 * Metodo para limpiar la informacion
	 * del Form
	 */
	public void reset()
	{
		this.informacionPeticion = null;
		this.listaSalas = null;
		this.listaPeticiones = null;
		this.listaServicios = null;
		this.listaSalasProgramadas = null;
		
		this.fechaPeticion = "";
		this.horaPeticion = "";
		
		this.fechaEstimadaCirugia = "";
		this.solicitante = "";
		this.duracion = "";
		this.estadoPeticion = "";
		this.mapaServicios = new HashMap();
		
		this.nroSala = -1;
		this.tipoAnestesiaPreanestesia = 0;
		this.patronOrdenar = "";
		this.ultimoPatronOrdenar= "";
		
		this.centroAtencion = 0;
		
		this.indicadorPermisoFunc ="";		
		
		this.motivoCancelacion="";
	}
	
	/**
	 * @return Retorna estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param Asigna estado.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Retorna informacionPeticion.
	 */
	public Collection getInformacionPeticion() {
		return informacionPeticion;
	}
	/**
	 * @param Asigna informacionPeticion.
	 */
	public void setInformacionPeticion(Collection informacionPeticion) {
		this.informacionPeticion = informacionPeticion;
	}
	/**
	 * @return Retorna fechaEstimadaCirugia.
	 */
	public String getFechaEstimadaCirugia() {
		return fechaEstimadaCirugia;
	}
	/**
	 * @param Asigna fechaEstimadaCirugia.
	 */
	public void setFechaEstimadaCirugia(String fechaEstimadaCirugia) {
		this.fechaEstimadaCirugia = fechaEstimadaCirugia;
	}
	/**
	 * @return Retorna fechaPeticion.
	 */
	public String getFechaPeticion() {
		return fechaPeticion;
	}
	/**
	 * @param Asigna fechaPeticion.
	 */
	public void setFechaPeticion(String fechaPeticion) {
		this.fechaPeticion = fechaPeticion;
	}
	/**
	 * @return Retorna numeroPeticion.
	 */
	public int getNumeroPeticion() {
		return numeroPeticion;
	}
	/**
	 * @param Asigna numeroPeticion.
	 */
	public void setNumeroPeticion(int numeroPeticion) {
		this.numeroPeticion = numeroPeticion;
	}
	/**
	 * @return Retorna solicitante.
	 */
	public String getSolicitante() {
		return solicitante;
	}
	/**
	 * @param Asigna solicitante.
	 */
	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}
	/**
	 * @return Retorna duracion.
	 */
	public String getDuracion() {
		return duracion;
	}
	/**
	 * @param Asigna duracion.
	 */
	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}
	/**
	 * @return Retorna listaSalas.
	 */
	public Collection getListaSalas() {
		return listaSalas;
	}
	/**
	 * @param Asigna listaSalas.
	 */
	public void setListaSalas(Collection listaSalas) {
		this.listaSalas = listaSalas;
	}
	/**
	 * @return Retorna listaPeticiones.
	 */
	public Collection getListaPeticiones() {
		return listaPeticiones;
	}
	/**
	 * @param Asigna listaPeticiones.
	 */
	public void setListaPeticiones(Collection listaPeticiones) {
		this.listaPeticiones = listaPeticiones;
	}
	/**
	 * @return Retorna listaServicios.
	 */
	public Collection getListaServicios() {
		return listaServicios;
	}
	/**
	 * @param Asigna listaServicios.
	 */
	public void setListaServicios(Collection listaServicios) {
		this.listaServicios = listaServicios;
	}
	/**
	 * @return Retorna patronOrdenar.
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}
	/**
	 * @param Asigna patronOrdenar.
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}
	/**
	 * @return Retorna ultimoPatronOrdenar.
	 */
	public String getUltimoPatronOrdenar() {
		return ultimoPatronOrdenar;
	}
	/**
	 * @param Asigna ultimoPatronOrdenar.
	 */
	public void setUltimoPatronOrdenar(String ultimoPatronOrdenar) {
		this.ultimoPatronOrdenar = ultimoPatronOrdenar;
	}
	/**
	 * @return Retorna fechaFinCirugia.
	 */
	public String getFechaFinCirugia() {
		return fechaFinCirugia;
	}
	/**
	 * @param Asigna fechaFinCirugia.
	 */
	public void setFechaFinCirugia(String fechaFinCirugia) {
		this.fechaFinCirugia = fechaFinCirugia;
	}
	/**
	 * @return Retorna fechaFinPeticion.
	 */
	public String getFechaFinPeticion() {
		return fechaFinPeticion;
	}
	/**
	 * @param Asigna fechaFinPeticion.
	 */
	public void setFechaFinPeticion(String fechaFinPeticion) {
		this.fechaFinPeticion = fechaFinPeticion;
	}
	/**
	 * @return Retorna fechaIniCirugia.
	 */
	public String getFechaIniCirugia() {
		return fechaIniCirugia;
	}
	/**
	 * @param Asigna fechaIniCirugia.
	 */
	public void setFechaIniCirugia(String fechaIniCirugia) {
		this.fechaIniCirugia = fechaIniCirugia;
	}
	/**
	 * @return Retorna fechaIniPeticion.
	 */
	public String getFechaIniPeticion() {
		return fechaIniPeticion;
	}
	/**
	 * @param Asigna fechaIniPeticion.
	 */
	public void setFechaIniPeticion(String fechaIniPeticion) {
		this.fechaIniPeticion = fechaIniPeticion;
	}
	/**
	 * @return Retorna nroFinServicio.
	 */
	public int getNroFinServicio() {
		return nroFinServicio;
	}
	/**
	 * @param Asigna nroFinServicio.
	 */
	public void setNroFinServicio(int nroFinServicio) {
		this.nroFinServicio = nroFinServicio;
	}
	/**
	 * @return Retorna nroIniServicio.
	 */
	public int getNroIniServicio() {
		return nroIniServicio;
	}
	/**
	 * @param Asigna nroIniServicio.
	 */
	public void setNroIniServicio(int nroIniServicio) {
		this.nroIniServicio = nroIniServicio;
	}
	/**
	 * @return Retorna profesional.
	 */
	public int getProfesional() {
		return profesional;
	}
	/**
	 * @param Asigna profesional.
	 */
	public void setProfesional(int profesional) {
		this.profesional = profesional;
	}
	/**
	 * @return Retorna mapaServicios.
	 */
	public HashMap getMapaServicios() {
		return mapaServicios;
	}
	/**
	 * @param Asigna mapaServicios.
	 */
	public void setMapaServicios(HashMap mapaServicios) {
		this.mapaServicios = mapaServicios;
	}
	
	/**
	 * @return Retorna un elemento dentro del mapaServicios.
	 */
	public Object getMapaServicios(String key) {
		return mapaServicios.get(key);
	}
	
	/**
	 * @param Asigna mapaServicios.
	 */
	public void setMapaServicios(String key, Object elemento) {
		this.mapaServicios.put(key,elemento);
	}

	/**
	 * @return Retorna fechaAux.
	 */
	public String getFechaProgramacion() {
		return fechaProgramacion;
	}
	/**
	 * @param Asigna fechaAux.
	 */
	public void setFechaProgramacion(String fechaProgramacion) {
		this.fechaProgramacion = fechaProgramacion;
	}
	/**
	 * @return Retorna horaFinProgramacion.
	 */
	public String getHoraFinProgramacion() {
		return horaFinProgramacion;
	}
	/**
	 * @param Asigna horaFinProgramacion.
	 */
	public void setHoraFinProgramacion(String horaFinProgramacion) {
		this.horaFinProgramacion = horaFinProgramacion;
	}
	/**
	 * @return Retorna horaInicioProgramacion.
	 */
	public String getHoraInicioProgramacion() {
		return horaInicioProgramacion;
	}
	/**
	 * @param Asigna horaInicioProgramacion.
	 */
	public void setHoraInicioProgramacion(String horaInicioProgramacion) {
		this.horaInicioProgramacion = horaInicioProgramacion;
	}
	/**
	 * @return Retorna nroSala.
	 */
	public int getNroSala() {
		return nroSala;
	}
	/**
	 * @param Asigna nroSala.
	 */
	public void setNroSala(int nroSala) {
		this.nroSala = nroSala;
	}
	
	
	/**
	 * @return Retorna listaSalasProgramadas.
	 */
	public Collection getListaSalasProgramadas() {
		return listaSalasProgramadas;
	}
	/**
	 * @param Asigna listaSalasProgramadas.
	 */
	public void setListaSalasProgramadas(Collection listaSalasProgramadas) {
		this.listaSalasProgramadas = listaSalasProgramadas;
	}
	/**
	 * @return Retorna estadoAnterior.
	 */
	public String getEstadoAnterior() {
		return estadoAnterior;
	}
	/**
	 * @param Asigna estadoAnterior.
	 */
	public void setEstadoAnterior(String estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}
	/**
	 * @return Retorna estadoInsercion.
	 */
	public int getEstadoInsercion() {
		return estadoInsercion;
	}
	/**
	 * @param Asigna estadoInsercion.
	 */
	public void setEstadoInsercion(int estadoInsercion) {
		this.estadoInsercion = estadoInsercion;
	}
	
	
	/**
	 * @return Retorna estadoPeticion.
	 */
	public String getEstadoPeticion() {
		return estadoPeticion;
	}
	/**
	 * @param Asigna estadoPeticion.
	 */
	public void setEstadoPeticion(String estadoPeticion) {
		this.estadoPeticion = estadoPeticion;
	}
	/**
	 * @return Retorna esProgramacion.
	 */
	public boolean getEsProgramacion() {
		return esProgramacion;
	}
	/**
	 * @param Asigna esProgramacion.
	 */
	public void setEsProgramacion(boolean esProgramacion) {
		this.esProgramacion = esProgramacion;
	}
	/**
	 * @return Retorna nroFilasGrilla.
	 */
	public int getNroFilasGrilla() {
		return nroFilasGrilla;
	}
	/**
	 * @param Asigna nroFilasGrilla.
	 */
	public void setNroFilasGrilla(int nroFilasGrilla) {
		this.nroFilasGrilla = nroFilasGrilla;
	}

	/**
	 * @return Retorna tipoAnestesiaPreanestesia.
	 */
	public int getTipoAnestesiaPreanestesia() {
		return tipoAnestesiaPreanestesia;
	}

	/**
	 * @param Asigna tipoAnestesiaPreanestesia.
	 */
	public void setTipoAnestesiaPreanestesia(int tipoAnestesiaPreanestesia) {
		this.tipoAnestesiaPreanestesia = tipoAnestesiaPreanestesia;
	}

	/**
	 * @return Retorna mapaPeticiones.
	 */
	public HashMap getMapaPeticiones() {
		return mapaPeticiones;
	}

	/**
	 * @param Asigna mapaPeticiones.
	 */
	public void setMapaPeticiones(HashMap mapaPeticiones) {
		this.mapaPeticiones = mapaPeticiones;
	}
	
	/**
	 * @return Retorna un elemento dentro del mapaServicios.
	 */
	public Object getMapaPeticiones(String key) {
		return mapaPeticiones.get(key);
	}
	
	/**
	 * @param Asigna mapaServicios.
	 */
	public void setMapaPeticiones(String key, Object elemento) {
		this.mapaPeticiones.put(key,elemento);
	}

	/**
	 * @return Retorna ultimoPatron.
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param Asigna ultimoPatron.
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	public int getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the indicadorPermisoFunc
	 */
	public String getIndicadorPermisoFunc() {
		return indicadorPermisoFunc;
	}

	/**
	 * @param indicadorPermisoFunc the indicadorPermisoFunc to set
	 */
	public void setIndicadorPermisoFunc(String indicadorPermisoFunc) {
		this.indicadorPermisoFunc = indicadorPermisoFunc;
	}

	/**
	 * @return the horaPeticion
	 */
	public String getHoraPeticion() {
		return horaPeticion;
	}

	/**
	 * @param horaPeticion the horaPeticion to set
	 */
	public void setHoraPeticion(String horaPeticion) {
		this.horaPeticion = horaPeticion;
	}

	/**
	 * @return the motivoCancelacion
	 */
	public String getMotivoCancelacion() {
		return motivoCancelacion;
	}

	/**
	 * @param motivoCancelacion the motivoCancelacion to set
	 */
	public void setMotivoCancelacion(String motivoCancelacion) {
		this.motivoCancelacion = motivoCancelacion;
	}

}