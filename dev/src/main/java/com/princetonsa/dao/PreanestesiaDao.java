/*
 * Creado en Oct 26, 2005
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

/**
 * Interface para el manejo de la preanestesia realizada en una cirug?a
 * 
 * @author Andr?s Mauricio Ruiz V?lez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public interface PreanestesiaDao
{
	
	/**
	 * M?todo para insertar la valoraci?n de preanestesia
	 * @param con una conexion abierta con una fuente de datos
	 * @param peticionCirugia
	 * @param fechaPreanestesia
	 * @param horaPreanestesia
	 * @param datosMedico
	 * @param tipoAnestesia 
	 * @param observacionesGrales
	 * @param login
	 * @return peticionCirugia
	 */
	public int insertarValoracionPreanestesia (Connection con, int peticionCirugia, String fechaPreanestesia, String horaPreanestesia, 
																					String datosMedico, int tipoAnestesia, String observacionesGrales, String login);
	
	/**
	 * M?todo para insertar el encabezado del ex?men de laboratorio de preanestesia, que se utiliza para
	 * los ex?mes parametrizados por instituci?n as? como los ingresados.
	 * @param con una conexion abierta con una fuente de datos
	 * @param peticionQx
	 * @param resultados
	 * @param observaciones
	 * @param esInsertar
	 * @param examenLabPreInst
	 * @param descripcionOtro
	 * @param datosMedico
	 * @return codEncaExamenLab
	 */
	public int insertarEncabezadoExamenLab (Connection con, int peticionQx, String resultados, String observaciones, boolean esInsertar, int examenLabPreInst, String descripcionOtro, String datosMedico);
	
	/**
	 * M?todo para insertar el detalle del examen de laboratorio parametrizado en la preanestesia
	 * @param con una conexion abierta con una fuente de datos
	 * @param valExamenLabPre
	 * @param examenLabPreInst
	 * @return valExamenLabPre
	 */
	public int insertarExamenLabParametrizado (Connection con, int valExamenLabPre, int  examenLabPreInst);
	
	/**
	 * M?todo para insertar el detalle del otro ex?men de laboratorio en la preanestesia
	 * @param con una conexion abierta con una fuente de datos
	 * @param valExamenLabPre
	 * @param nombreOtro
	 * @return valExamenLabPre
	 */
	public int insertarExamenLabOtro (Connection con, int valExamenLabPre, String  nombreOtro);
	
	/**
	 * M?todo para insertar el documento adjunto del ex?men de laboratorio
	 * @param con una conexion abierta con una fuente de datos
	 * @param codValExamenLab
	 * @param docRealAdj
	 * @param docGeneradoAdj
	 * @return codValExamenLab
	 */
	public int insertarAdjuntoExamenLabPreanestesia (Connection con, int codValExamenLab, String  docRealAdj, String docGeneradoAdj);
	
	/**
	 * M?todo para consultar los tipos aplicables para la Preanestesia seg?n la instituci?n
	 * @param con
	 * @param institucion
	 * @param peticionQx
	 * @param nroConsulta
	 * @return Collection
	 */
	public Collection consultarTipoParametrizado (Connection con, int institucion, int peticionQx, int nroConsulta);
	
	/**
	 * Metodo para consultar los examenes de laboratorio de la preanestesia de acuerdo a la instituci?n y la petici?n de cirug?a
	 * @param con
	 * @param institucion
	 * @param peticionQx
	 * @return
	 */
	public HashMap consultarExamenesLaboratorio(Connection con, int institucion, int peticionQx);
	
	 /**
     * Metodo para consultar la preanestesia asociada a la petici?n generada 
     * @param con
     * @param peticionQx
     * @return Collection con los datos de la Preanestesia
     */
	public Collection cargarPreanestesia (Connection con, int peticionQx);
	
    /**
     * Metodo para consultar y cargar la informaci?n de la secci?n Ex?menes de Laboratorio
     * @param con
     * @param peticionQx
     * @return Collection -> Con la informaci?n de los ex?menes de laboratorio
     */
  public Collection cargarExamenesLabPre(Connection con, int peticionQx); 
  
  /**
   * Metodo para consultar y cargar la informaci?n de la secci?n de conclusiones
   * @param con
   * @param valPreanestesia
   * @return Collection -> Con la informaci?n de las conclusiones
   */
  public Collection cargarConclusiones(Connection con, int valPreanestesia); 
    
    /**
	 * M?todo para consultar el c?digo del encabezado del ex?men de laboratorio  cuando es parametrizado (Sobrecargado)
	 * @param con -> conexion
	 * @param peticionCirugia
	 * @param examLabInst
	 * @return codigo
	 */
	public int consultaCodigoEncabezadoExamenLab (Connection con, int peticionCirugia, int examenLabPreInst);
	
	/**
	 * M?todo para consultar el c?digo del encabezado del ex?men de laboratorio cuando es otro (Sobrecargado)
	 * @param con -> conexion
	 * @param peticionCirugia
	 * @param descripcionOtro
	 * @return codigo
	 */
	public int consultaCodigoEncabezadoExamenLab (Connection con, int peticionCirugia, String descripcionOtro);
    
    
    /**
     * Metodo para insertar los examenes fisicos 
     * @param con
     * @param tablaDestino : indicador para saber sobre cual tabla se inserta
     * @param codigoOtro
     * @param val_preanestesia
     * @param nombreOtro
     * @param valor
     * @return
     */
    public int  insertarExamenFisico(Connection con, int tablaDestino, int codigoOtro, int val_preanestesia, String nombreOtro, String  valor);
    
     /**
	 * M?todo para insertar una conclusi?n parametrizada de la preanestesia
	 * @param con una conexion abierta con una fuente de datos
	 * @param valPreanestesia
	 * @param concluPreInst
	 * @param valorConclusion
	 * @return valPreanestesia
	 */
	public int insertarConclusionPreanestesia (Connection con, int valPreanestesia, int  concluPreInst, String valorConclusion);
    
    /**
     * Metodo para consultar y cargar la informaci?n de la secci?n Ex?menes Fisicos
     * @param con
     * @param peticionQx
     * @return Collection -> Con la informaci?n de los ex?menes de laboratorio
     */
    public Collection cargarExamenesFisicos(Connection con, int peticionQx);
    
    /**
     * Metodo para consultar y cargar la informaci?n de la secci?n Ex?menes Fisicos de tipo TextArea
     * @param con
     * @param peticionQx
     * @return Collection -> Con la informaci?n de los ex?menes de laboratorio
     */
    public Collection cargarExamenesFisicosArea(Connection con, int peticionQx);
    
	 /**
     * Metodo para consultar la informaci?n general de la petici?n de cirug?a 
     * @param con
     * @param nroPeticion
     * @return Collection con los datos generales de la Petici?n
     */
	public Collection cargarInfoPeticion (Connection con, int nroPeticion);
	
	 /**
     * Metodo para obtener el ?ltimo codigo de los ex?menes de laboratorio parametrizados para la instituci?n
     * en la preanestesia  
     * @param con
     * @param institucion
     * @return int -> Ultimo codigo del ex?men de laboratorio parametrizado
     */
	public int obtenerUltimoExamenLabPar (Connection con, int institucion);
	
}
