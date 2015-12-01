/*
 * Created on Feb 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dto.historiaClinica.DtoClasificacionesTriage;
import com.princetonsa.dto.historiaClinica.DtoTriage;


/**
 * @author Sebastián Gómez Rivilas
 *
 * Princeton S.A.
 */
public interface TriageDao
{
    
    /**
     * Inserta los signos vitales de una entrada triage
     * @param con
     * @param codigo
     * @param consecutivo_triage
     * @param valor
     * @return
     */
    public int insertarSignosVitalesTriage(Connection con,int codigo,String consecutivo,String valor,String estado);
    
    /**
     * Inserta un triage
     * @param con
     * @param datosTriage
     * @param estado
     * @return
     */
    public int insertarTriage(Connection con,HashMap datosTriage,String estado);
    
    /**
     * Obtener consecutivo
     * @param con
     * @return
     */
    public int obtenerConsecutivo(Connection con);
    
    /**
     * Cargar el resumen de la inserción de Trage
     * @param con
     * @param consecutivo
     * @return
     */
    public Collection resumenTriage(Connection con,String consecutivo);
    
    /**
     * Función que me permite cargar el detalle de la selección de un triage
     * @param con
     * @param consecutivo
     * @param consecutivo_fecha
     * @return
     */
    public Collection cargarTriage(Connection con,String consecutivo,String consecutivo_fecha);
    /**
	 * Función que actualiza las observaciones generales de un triage específico
	 * @param con
	 * @param consecutivo
	 * @param consecutivo_fecha
	 * @param observacion
	 * @return
	 */
	public int actualizarTriage(Connection con,String consecutivo,String consecutivo_fecha,String observacion, String noRespondioLLamado);
	
	/**
	 * Función que carga los datos del reporte del triage
	 * @param con
	 * @param fechainicial
	 * @param fechafinal
	 * @param usuario
	 * @param responsable
	 * @param centroAtencion
	 * @return
	 */
	public Collection reporteTriage(Connection con,String fechainicial,String fechafinal,String usuario,String responsable,int centroAtencion);
	
	/**
	 * Metodo para buscar un triage por medio de busqueda avanzada
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoCalificacion
	 * @param codigoDestino
	 * @param admision
	 * @param codigoSala
	 * @param centroAtencion
	 * @return
	 */
	public Collection buscarTriage(Connection con, String fechaInicial, String fechaFinal, int codigoCalificacion, int codigoDestino, String admision, int codigoSala, int centroAtencion );
	
	/**
	 * Método implementado para actualizar el estado del paciente que estuvo
	 * registrado para Triage cambiando su estado a atendido e ingresando su consecutivo triage
	 * respectivo
	 * @param con
	 * @param codigo
	 * @param consecutivoTriage
	 * @return
	 */
	public int actualizarPacienteParaTriage(Connection con,String codigo,String consecutivoTriage);
	
	/**
	 * Método implementado para cargar los signos vitales del Triage
	 * @param con
	 * @param consecutivo
	 * @param consecutivo_fecha
	 * @return
	 */
	public HashMap cargarSignosVitalesTriage(Connection con,String consecutivo,String consecutivo_fecha);
	
	/**
	 * Método que consulta los datos del triage
	 * @param con
	 * @param campos
	 * @return
	 */
	public DtoTriage obtenerDatosTriage(Connection con,HashMap campos);

	/**
	 * 
	 * @param codigoCuenta
	 * @return
	 */
	public DtoTriage consultarInfoResumenTriagePorCuenta(int codigoCuenta);

	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoClasificacionesTriage> consultarClasificacionesTriage();

	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public Collection buscarTriage(Connection con, String tipoID,String numeroID);
}
