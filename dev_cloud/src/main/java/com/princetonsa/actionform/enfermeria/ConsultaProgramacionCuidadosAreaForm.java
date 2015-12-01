package com.princetonsa.actionform.enfermeria;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.enfermeria.DtoCuidadosEnfermeria;
import com.princetonsa.dto.enfermeria.DtoFrecuenciaCuidadoEnferia;

import util.ConstantesBD;
import util.UtilidadFecha;

public class ConsultaProgramacionCuidadosAreaForm extends ValidatorForm {

	private String estado;
    private String fechaProg;
	private String horaProg;
	private String areaFiltro;
	private String pisoFiltro;
	private String habitacionFiltro;
	private HashMap mapaListaPacientes;
	private ArrayList<DtoFrecuenciaCuidadoEnferia> cuidados;
	private ArrayList<DtoCuidadosEnfermeria> programacionCuidados;
	private ArrayList<HashMap<String,Object>> arrayTipoFrecuencias;
	private int indicePaciente;
	private String centroAtencion;
	private String descripcionPiso;
	private String descripcionArea;
	private String descripcionHabitacion;

	
	
	/**
	 * Metodo constructor por defecto
	 */
 public ConsultaProgramacionCuidadosAreaForm(){
		
        this.reset(); 
		this.programacionCuidados = new ArrayList<DtoCuidadosEnfermeria>(); 
 }
	

 
 /**
  * Metodo que reinicia los valores de los atributos 
  * Los valores de fecha y hora (fechaProg - horaProg) les pone la fecha y la hora del sistema
  */
	public void reset(){
		this.estado=new String("");
		this.fechaProg=UtilidadFecha.getFechaActual();
		this.horaProg= UtilidadFecha.getHoraActual();
		this.areaFiltro=new String("");
		this.pisoFiltro=new String("");
		this.habitacionFiltro= new String("");
		this.centroAtencion=new String("");
		this.mapaListaPacientes=new HashMap();
		this.mapaListaPacientes.put("numRegistros", "0");
		this.indicePaciente=ConstantesBD.codigoNuncaValido;
		this.cuidados = new ArrayList<DtoFrecuenciaCuidadoEnferia>();
		this.arrayTipoFrecuencias = new ArrayList<HashMap<String,Object>>();
		this.descripcionPiso=new String("");
		this.descripcionArea=new String("");
		this.descripcionHabitacion=new String("");
	
	
		
	}
	
	/**
	 * 
	 * Metodo para retornar el atributo estado (estado).... retorna un String
 	 */
	public final String getEstado() {
		return estado;
	}

	/**
	 * 
	 * Metodo para asignar un valor al atributo estado (estado)...recibe como parametro un String (estado)
	 */
	public final void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * Metodo que retorna el atributo fecha (fechaProg) .... retorna un String
	 * 
	 */
	public final String getFechaProg() {
		return fechaProg;
	}
	
	/**
	 * Metodo para asignar un valor al atributo fecha (FechaProg) ...recibe como parametro un String (FechaProg)
	 * 
	 */

	public final void setFechaProg(String fechaProg) {
		this.fechaProg = fechaProg;
	}
	
	/**
	 * Metodo que retorna el atributo hora ( horaProg) .... retorna un String
	 *
	 */

	public final String getHoraProg() {
		return horaProg;
	}

    /**
     * Metodo para asignar un valor al atributo hora (horaProg)	...recibe como parametro un String (horaProg)
     * @param horaProg
     */
	
	public final void setHoraProg(String horaProg) {
		this.horaProg = horaProg;
	}
	
	/**
	 * Metodo que retorna el atributo area (areaFiltro) .... retorna un String
	 * 
	 */

	public String getAreaFiltro() {
		return areaFiltro;
	}
	
	/**
	 * Metodo que asigna un valor al atributo area (areFiltro) ...recibe como parametro un String (areaFiltro)
	 * @param areaFiltro
	 */

	public void setAreaFiltro(String areaFiltro) {
		this.areaFiltro = areaFiltro;
	}

	/**
	 *Metodo que retorna el atributo piso (pisoFiltro) ...retorna un String
	 * @return
	 */
	public String getPisoFiltro() {
		return pisoFiltro;
	}

	/**
	 * Metodo que asigna un valor al atributo piso (pisoFiltro)...recibe como parametro un String (pisoFiltro)
	 * @param pisoFiltro
	 */
	public void setPisoFiltro(String pisoFiltro) {
		this.pisoFiltro = pisoFiltro;
	}
/**
 * Metodo que retorna el atributo habitacion (habitacionFiltro) ....retorna un String
 * @return
 */
	public String getHabitacionFiltro() {
		return habitacionFiltro;
	}
/**
 * Metodo que asigna un valor al atributo habitacion (habitacionFiltro)....recibe como parametro un String (habitacionFiltro)
 * @param habitacionFiltro
 */
	public void setHabitacionFiltro(String habitacionFiltro) {
		this.habitacionFiltro = habitacionFiltro;
	}

/**
 * 
 * @return
 */

public HashMap getMapaListaPacientes() {
	return mapaListaPacientes;
}

/**
 * 
 * @param mapaListaPacientes
 */
	
	public void setMapaListaPacientes(HashMap mapaListaPacientes) {
		this.mapaListaPacientes = mapaListaPacientes;
	}


/**
 * 
 * @return
 */
	public int getIndicePaciente() {
		return indicePaciente;
	}


/**
 * 
 * @param indicePaciente
 */
	public void setIndicePaciente(int indicePaciente) {
		this.indicePaciente = indicePaciente;
	}
	
	public void setCuidados(ArrayList<DtoFrecuenciaCuidadoEnferia> cuidados) {
		this.cuidados = cuidados;
	}



	public ArrayList<DtoFrecuenciaCuidadoEnferia> getCuidados() {
		return cuidados;
	}



	public ArrayList<HashMap<String, Object>> getArrayTipoFrecuencias() {
		return arrayTipoFrecuencias;
	}



	public void setArrayTipoFrecuencias(
			ArrayList<HashMap<String, Object>> arrayTipoFrecuencias) {
		this.arrayTipoFrecuencias = arrayTipoFrecuencias;
	}



	public String getCentroAtencion() {
		return centroAtencion;
	}



	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}



	/**
	 * @return the programacionCuidados
	 */
	public ArrayList<DtoCuidadosEnfermeria> getProgramacionCuidados() {
		return programacionCuidados;
	}



	/**
	 * @param programacionCuidados the programacionCuidados to set
	 */
	public void setProgramacionCuidados(
			ArrayList<DtoCuidadosEnfermeria> programacionCuidados) {
		this.programacionCuidados = programacionCuidados;
	}



	/**
	 * @return the descripcionPiso
	 */
	public String getDescripcionPiso() {
		return descripcionPiso;
	}



	/**
	 * @param descripcionPiso the descripcionPiso to set
	 */
	public void setDescripcionPiso(String descripcionPiso) {
		this.descripcionPiso = descripcionPiso;
	}



	/**
	 * @return the descripcionArea
	 */
	public String getDescripcionArea() {
		return descripcionArea;
	}



	/**
	 * @param descripcionArea the descripcionArea to set
	 */
	public void setDescripcionArea(String descripcionArea) {
		this.descripcionArea = descripcionArea;
	}



	/**
	 * @return the descripcionHabitacion
	 */
	public String getDescripcionHabitacion() {
		return descripcionHabitacion;
	}



	/**
	 * @param descripcionHabitacion the descripcionHabitacion to set
	 */
	public void setDescripcionHabitacion(String descripcionHabitacion) {
		this.descripcionHabitacion = descripcionHabitacion;
	}


}


