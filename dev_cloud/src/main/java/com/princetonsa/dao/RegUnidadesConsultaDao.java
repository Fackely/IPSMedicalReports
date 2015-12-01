/*
 * Creado  17/08/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Clase para manejar
 *
 * @version 1.0, 17/08/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
public interface RegUnidadesConsultaDao
{

    /**
     * @param codigoEspecialidad 
     * @param con, Conexion a la BD
     * @param descripcion, Descripcion de la unidad de Consulta
     * @param servicio, Codigo del servicio
     * @param Activo, Para activar o inactivar las unidades de consulta 
     */
    
     int insertarRegUniCon(Connection con, 
             				String descripcion, 
             				boolean Activo,
             				int especialidad, String tiposAtencion, String color);
     
     /**
      * 
      * @param con
      * @param Codigo
      * @param Descripcion
      * @param codServicio
      * @param Activo
     * @param especialidad 
      * @param Temp
      * @return
      */
     public ResultSetDecorator consultaAvanzada(Connection con,
             							int Codigo,
             							String Descripcion, 
             							int codServicio,
             							boolean Activo,
             							int especialidad, String Temp);

    /**
     * @param con, Conexion a la BD
     * @param codigo, Codigo para buscar
     * @return Retorna un ResultSet.
     */
    public ResultSetDecorator consultaModificar(Connection con, int codigo);
    
    /**
     * 
     * @param con
     * @param codigo
     * @param descripcion
     * @param activa
     * @param especialidad 
     * @param color 
     */
    public int modificar(Connection con, int codigo, String descripcion,boolean activa, int especialidad, String color);
            
    /**
     * 
     * @param con
     * @param codigo
     * @return
     */
    public int eliminar(Connection con, int codigo);         	                        
    /**
     * 
     * @param con
     * @return
     */
    public ResultSetDecorator consultarTodo(Connection con);
    
    /**
     * 
     * @param con
     * @param servicio
     * @return
     */
    public ResultSetDecorator consultarCUPS(Connection con);

	/**
	 * @param con
	 * @param codigoUC
	 * @param i
	 * @return
	 */
	public boolean insertarDetalle(Connection con, int codigoUC, int servicio, int especialidad);

	/**
	 * @param con
	 * @param codigo
	 * @return
	 */
	public ResultSetDecorator consultaServiciosModificacion(Connection con, int codigo);

	/**
	 * @param con
	 * @param codigoT
	 * @param codigoServicio
	 */
	public int eliminarServicio(Connection con, int codigoT, int codigoServicio);

	/**
	 * @param con
	 * @param codigoT
	 * @param servicioNuevo
	 * @param servicioAntiguo
	 */
	public int modificarServico(Connection con, int codigoT, int servicioNuevo, int servicioAntiguo,int especialidad);

	/**
	 * @param con
	 * @param codigo
	 */
	public int eliminarDetalles(Connection con, int codigo);
	
	/**
	 * Método que realiza la consulta de los servicios de la unuidad de consulta
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> obtenerServiciosUnidadConsulta(Connection con,HashMap campos);
	
	/**
	 * Método que realiza la consulta de la especialidad 
	 * @param con
	 * @param campos
	 * @return
	 */
	public String obtenerEspecialidad(Connection con,HashMap campos);
	
	/**
	 * Metodo que cuenta cuanta veces esta asociada una unidad de agenda con la tabla horario de atencion
	 * @param con
	 * @param unidad_consulta
	 * @return
	 */
	public int verificarUniAgenAsoHorarioAten(Connection con, int unidad_consulta);
}
