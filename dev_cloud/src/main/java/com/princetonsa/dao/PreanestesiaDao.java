/*
 * Creado en Oct 26, 2005
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

/**
 * Interface para el manejo de la preanestesia realizada en una cirugía
 * 
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public interface PreanestesiaDao
{
	
	/**
	 * Método para insertar la valoración de preanestesia
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
	 * Método para insertar el encabezado del exámen de laboratorio de preanestesia, que se utiliza para
	 * los exámes parametrizados por institución así como los ingresados.
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
	 * Método para insertar el detalle del examen de laboratorio parametrizado en la preanestesia
	 * @param con una conexion abierta con una fuente de datos
	 * @param valExamenLabPre
	 * @param examenLabPreInst
	 * @return valExamenLabPre
	 */
	public int insertarExamenLabParametrizado (Connection con, int valExamenLabPre, int  examenLabPreInst);
	
	/**
	 * Método para insertar el detalle del otro exámen de laboratorio en la preanestesia
	 * @param con una conexion abierta con una fuente de datos
	 * @param valExamenLabPre
	 * @param nombreOtro
	 * @return valExamenLabPre
	 */
	public int insertarExamenLabOtro (Connection con, int valExamenLabPre, String  nombreOtro);
	
	/**
	 * Método para insertar el documento adjunto del exámen de laboratorio
	 * @param con una conexion abierta con una fuente de datos
	 * @param codValExamenLab
	 * @param docRealAdj
	 * @param docGeneradoAdj
	 * @return codValExamenLab
	 */
	public int insertarAdjuntoExamenLabPreanestesia (Connection con, int codValExamenLab, String  docRealAdj, String docGeneradoAdj);
	
	/**
	 * Método para consultar los tipos aplicables para la Preanestesia según la institución
	 * @param con
	 * @param institucion
	 * @param peticionQx
	 * @param nroConsulta
	 * @return Collection
	 */
	public Collection consultarTipoParametrizado (Connection con, int institucion, int peticionQx, int nroConsulta);
	
	/**
	 * Metodo para consultar los examenes de laboratorio de la preanestesia de acuerdo a la institución y la petición de cirugía
	 * @param con
	 * @param institucion
	 * @param peticionQx
	 * @return
	 */
	public HashMap consultarExamenesLaboratorio(Connection con, int institucion, int peticionQx);
	
	 /**
     * Metodo para consultar la preanestesia asociada a la petición generada 
     * @param con
     * @param peticionQx
     * @return Collection con los datos de la Preanestesia
     */
	public Collection cargarPreanestesia (Connection con, int peticionQx);
	
    /**
     * Metodo para consultar y cargar la información de la sección Exámenes de Laboratorio
     * @param con
     * @param peticionQx
     * @return Collection -> Con la información de los exámenes de laboratorio
     */
  public Collection cargarExamenesLabPre(Connection con, int peticionQx); 
  
  /**
   * Metodo para consultar y cargar la información de la sección de conclusiones
   * @param con
   * @param valPreanestesia
   * @return Collection -> Con la información de las conclusiones
   */
  public Collection cargarConclusiones(Connection con, int valPreanestesia); 
    
    /**
	 * Método para consultar el código del encabezado del exámen de laboratorio  cuando es parametrizado (Sobrecargado)
	 * @param con -> conexion
	 * @param peticionCirugia
	 * @param examLabInst
	 * @return codigo
	 */
	public int consultaCodigoEncabezadoExamenLab (Connection con, int peticionCirugia, int examenLabPreInst);
	
	/**
	 * Método para consultar el código del encabezado del exámen de laboratorio cuando es otro (Sobrecargado)
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
	 * Método para insertar una conclusión parametrizada de la preanestesia
	 * @param con una conexion abierta con una fuente de datos
	 * @param valPreanestesia
	 * @param concluPreInst
	 * @param valorConclusion
	 * @return valPreanestesia
	 */
	public int insertarConclusionPreanestesia (Connection con, int valPreanestesia, int  concluPreInst, String valorConclusion);
    
    /**
     * Metodo para consultar y cargar la información de la sección Exámenes Fisicos
     * @param con
     * @param peticionQx
     * @return Collection -> Con la información de los exámenes de laboratorio
     */
    public Collection cargarExamenesFisicos(Connection con, int peticionQx);
    
    /**
     * Metodo para consultar y cargar la información de la sección Exámenes Fisicos de tipo TextArea
     * @param con
     * @param peticionQx
     * @return Collection -> Con la información de los exámenes de laboratorio
     */
    public Collection cargarExamenesFisicosArea(Connection con, int peticionQx);
    
	 /**
     * Metodo para consultar la información general de la petición de cirugía 
     * @param con
     * @param nroPeticion
     * @return Collection con los datos generales de la Petición
     */
	public Collection cargarInfoPeticion (Connection con, int nroPeticion);
	
	 /**
     * Metodo para obtener el último codigo de los exámenes de laboratorio parametrizados para la institución
     * en la preanestesia  
     * @param con
     * @param institucion
     * @return int -> Ultimo codigo del exámen de laboratorio parametrizado
     */
	public int obtenerUltimoExamenLabPar (Connection con, int institucion);
	
}
