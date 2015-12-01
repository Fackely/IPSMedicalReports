package com.princetonsa.dto.capitacion;

import java.io.Serializable;
import java.util.ArrayList;

import com.princetonsa.dto.administracion.DtoPersonas;

import util.ConstantesBD;

/**
 *  Clase para 
 * @author Camilo Gómez
 *
 */
public class DtoInconsistenciasArchivoPlano implements Serializable, Comparable<DtoInconsistenciasArchivoPlano>{

	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6122988334625082516L;


	/**
	 * Atributo que almacena el tipo de inconsistencia
	 */
	private String tipoInconsistencia;
	
	
	/**
	 * Atributo que almacena el campo donde se generó la inconsistencia
	 */
	private String campo;
	
	/**
	 * Atributo que almacena el número de registro del archivo donde
	 * se generó la inconsistencia
	 */
	private Integer numeroFila;

	/**
	 * Atributo que almacena el número de inconsistencias en el archivo
	 */
	private int numeroInconsistencias;
	
	
	/**
	 * Atributo que almacena la persona del archivo que tienen la inconsistencia
	 */
	DtoPersonas personaArchivo;
	/**
	 * Atributo que almacena la lista de personas de la BD que tienen la inconsistencia
	 */
	private ArrayList<DtoPersonas> listaPersonas;
		
	/**
	 * Atributo que almacena la descripción de la inconsistencia
	 */
	private String descripcion;
	
	/**
	 * Atributo que almacena el nombre la inconsistencia
	 */
	private String nombreInconsistencia;

	public DtoInconsistenciasArchivoPlano()	{
		
	}
	
	public DtoInconsistenciasArchivoPlano(String tipoInconsistencia, String campo, int numeroRegistro)
	{
		this.tipoInconsistencia		=tipoInconsistencia;
		this.campo					=campo;
		this.numeroFila				=numeroRegistro;
	}
	
	public DtoInconsistenciasArchivoPlano(String tipoInconsistencia, DtoPersonas personaArchivo, ArrayList<DtoPersonas> listaPersonas, int numeroRegistro)
	{
		this.tipoInconsistencia		=tipoInconsistencia;
		this.numeroFila				=numeroRegistro;
		this.personaArchivo			=personaArchivo;
		this.listaPersonas			=listaPersonas;
	}
	
	public DtoInconsistenciasArchivoPlano(String tipoInconsistencia, DtoPersonas personaArchivo, int numeroRegistro)
	{
		this.tipoInconsistencia		=tipoInconsistencia;
		this.numeroFila				=numeroRegistro;
		this.personaArchivo			=personaArchivo;
	}
	
	
	public void reset ()
	{
		this.tipoInconsistencia		="";
		this.campo					="";
		this.numeroFila				=ConstantesBD.codigoNuncaValido;
		this.numeroInconsistencias	=ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo tipoInconsistencia
	 * 
	 * @param  valor para el atributo tipoInconsistencia 
	 */
	public void setTipoInconsistencia(String tipoInconsistencia) {
		this.tipoInconsistencia = tipoInconsistencia;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo tipoInconsistencia
	 * 
	 * @return  Retorna la variable tipoInconsistencia
	 */
	public String getTipoInconsistencia() {
		return tipoInconsistencia;
	}

	
	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo campo
	 * 
	 * @param  valor para el atributo campo 
	 */
	public void setCampo(String campo) {
		this.campo = campo;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo campo
	 * 
	 * @return  Retorna la variable campo
	 */
	public String getCampo() {
		return campo;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo numeroFila
	 * 
	 * @param  valor para el atributo numeroFila 
	 */
	public void setNumeroFila(Integer numeroFila) {
		this.numeroFila = numeroFila;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo numeroFila
	 * 
	 * @return  Retorna la variable numeroFila
	 */
	public Integer getNumeroFila() {
		return numeroFila;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo numeroinconsistencias
	 * 
	 * @param numeroInconsistencias
	 */
	public void setNumeroInconsistencias(int numeroInconsistencias) {
		this.numeroInconsistencias = numeroInconsistencias;
	}

	/**
	 * Método que se encarga de obtener el numero de inconsistencias
	 * @return numeroInconsistencias
	 */
	public int getNumeroInconsistencias() {
		return numeroInconsistencias;
	}

	/**
	 * @return the listaPersonas
	 */
	public ArrayList<DtoPersonas> getListaPersonas() {
		return listaPersonas;
	}

	/**
	 * @param listaPersonas the listaPersonas to set
	 */
	public void setListaPersonas(ArrayList<DtoPersonas> listaPersonas) {
		this.listaPersonas = listaPersonas;
	}

	/**
	 * @return the personaArchivo
	 */
	public DtoPersonas getPersonaArchivo() {
		return personaArchivo;
	}

	/**
	 * @param personaArchivo the personaArchivo to set
	 */
	public void setPersonaArchivo(DtoPersonas personaArchivo) {
		this.personaArchivo = personaArchivo;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the nombreInconsistencia
	 */
	public String getNombreInconsistencia() {
		return nombreInconsistencia;
	}

	/**
	 * @param nombreInconsistencia the nombreInconsistencia to set
	 */
	public void setNombreInconsistencia(String nombreInconsistencia) {
		this.nombreInconsistencia = nombreInconsistencia;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 0;
	    hash += (this.campo != null ? this.campo.hashCode() : 0);
	    return hash;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	

	public int compareTo(DtoInconsistenciasArchivoPlano o) {
		if(this.numeroFila.compareTo(o.numeroFila) == 0){
        	if(this.campo.compareTo(o.campo) == 0){
        		return this.campo.compareTo(o.campo);
        	}else{
        		return this.campo.compareTo(o.campo);
        	}
        }   
		return this.numeroFila.compareTo(o.numeroFila);
	}
	
}
