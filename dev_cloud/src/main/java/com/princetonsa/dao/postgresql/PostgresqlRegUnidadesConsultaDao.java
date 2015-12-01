/*
 * Creado  17/08/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.RegUnidadesConsultaDao;
import com.princetonsa.dao.sqlbase.SqlBaseRegUnidadesConsultaDao;

/**
 * Clase para manejar
 *
 * @version 1.0, 17/08/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
public class PostgresqlRegUnidadesConsultaDao implements RegUnidadesConsultaDao
{
	
	/**
     * String que almacena el Query de insercion.
     */
    private static String insertarRegUniConStr="insert into unidades_consulta(codigo,descripcion,activa,especialidad,tipo_atencion,color) values(nextval('seq_unidades_consulta'),?,?,?,?,?)";

    /**
     * 
     */
	public int insertarRegUniCon(Connection con, String descripcion,boolean activo,int especialidad, String tiposAtencion, String color)
    {
     return SqlBaseRegUnidadesConsultaDao.insertarRegUniCon(con,descripcion,activo,especialidad,insertarRegUniConStr,DaoFactory.POSTGRESQL, tiposAtencion, color);
    }
	/**
	 * @param con
	 * @param codigoUC
	 * @param i
	 * @return
	 */
	public boolean insertarDetalle(Connection con, int codigoUC, int servicio, int especializacion)
	{
		return SqlBaseRegUnidadesConsultaDao.insertarDetalle(con,codigoUC,servicio,especializacion);
	}
    /**
     * 
     */
	public ResultSetDecorator consultaAvanzada(Connection con,int Codigo, String Descripcion, int codServicio, boolean Activo,int especialidad, String Temp)
    {
        return SqlBaseRegUnidadesConsultaDao.consultaAvanzada(con, Codigo, Descripcion, codServicio, Activo, especialidad,Temp,DaoFactory.POSTGRESQL);
    }
    /**
     * 
     */
    public ResultSetDecorator consultarTodo(Connection con)
    {
        return SqlBaseRegUnidadesConsultaDao.consultarTodo(con);
    }
    /**
     * 
     */
    public ResultSetDecorator consultaModificar(Connection con, int codigo)
    {
        return SqlBaseRegUnidadesConsultaDao.consultaModificar(con, codigo);
    }
    /**
     * 
     */
    public int modificar(Connection con, int codigo,String descripcion,boolean activa, int especialidad, String color)
    {
        int resp = SqlBaseRegUnidadesConsultaDao.modificar(con,codigo,descripcion,activa, especialidad, color);
        return resp;
    }
    /**
     * 
     */
    public int eliminar(Connection con, int codigo)
    {
        return SqlBaseRegUnidadesConsultaDao.eliminar(con, codigo);
    }
    
    /**
     * 
     */
    public ResultSetDecorator consultarCUPS(Connection con)
    {
        return SqlBaseRegUnidadesConsultaDao.consultarCUPS(con);
    }
    
	/**
	 * @param con
	 * @param codigo
	 * @return
	 */
	public ResultSetDecorator consultaServiciosModificacion(Connection con, int codigo)
	{
		return SqlBaseRegUnidadesConsultaDao.consultaServiciosModificacion(con,codigo);
	}
	
	/**
	 * @param con
	 * @param codigoT
	 * @param codigoServicio
	 */
	public int eliminarServicio(Connection con, int codigoT, int codigoServicio)
	{
		return SqlBaseRegUnidadesConsultaDao.eliminarServicio(con,codigoT,codigoServicio);
	}
	
	/**
	 * @param con
	 * @param codigoT
	 * @param servicioNuevo
	 * @param servicioAntiguo
	 */
	public int modificarServico(Connection con, int codigoT, int servicioNuevo, int servicioAntiguo, int especializacion)
	{
		return SqlBaseRegUnidadesConsultaDao.modificarServico(con,codigoT,servicioNuevo,servicioAntiguo,especializacion);
	}
	
	/**
	 * @param con
	 * @param codigo
	 */
	public int eliminarDetalles(Connection con, int codigo)
	{
		return SqlBaseRegUnidadesConsultaDao.eliminarDetalles(con,codigo);
	}
	
	/**
	 * Método que realiza la consulta de los servicios de la unuidad de consulta
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> obtenerServiciosUnidadConsulta(Connection con,HashMap campos)
	{
		return SqlBaseRegUnidadesConsultaDao.obtenerServiciosUnidadConsulta(con, campos);
	}
	
	/**
	 * Método que realiza la consulta de la especialidad 
	 * @param con
	 * @param campos
	 * @return
	 */
	public String obtenerEspecialidad(Connection con,HashMap campos)
	{
		return SqlBaseRegUnidadesConsultaDao.obtenerEspecialidad(con, campos);
	}
	
	/**
	 * Metodo que cuenta cuanta veces esta asociada una unidad de agenda con la tabla horario de atencion
	 * @param con
	 * @param unidad_consulta
	 * @return
	 */
	public int verificarUniAgenAsoHorarioAten(Connection con, int unidad_consulta)
	{
		return SqlBaseRegUnidadesConsultaDao.verificarUniAgenAsoHorarioAten(con, unidad_consulta);
	}
}
