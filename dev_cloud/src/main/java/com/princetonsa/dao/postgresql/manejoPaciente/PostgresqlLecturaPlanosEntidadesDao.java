/*
 * Enero 2, 2008
 */
package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.LecturaPlanosEntidadesDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseLecturaPlanosEntidadesDao;
import com.princetonsa.dto.manejoPaciente.DtoLogLecturaPlanosEntidades;

/**
 * @author Sebastián Gómez 
 *
 * Clase que maneja los métodos propìos de Postgres para el acceso a la fuente
 * de datos en la funcionalidad Lectura Planos Entidades Subcontratadas
 */
public class PostgresqlLecturaPlanosEntidadesDao implements LecturaPlanosEntidadesDao 
{
	/**
	 * Método que consulta el codigo minsalud de una entidad subcontratada
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public String consultarCodMinSaludEntidadSubcontratada(Connection con,String consecutivo)
	{
		return SqlBaseLecturaPlanosEntidadesDao.consultarCodMinSaludEntidadSubcontratada(con, consecutivo);
	}
	
	/**
	 * Método implementado para realizar la inserción en el archivo AF
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insercionRegistroArchivo(Connection con,HashMap campos)
	{
		return SqlBaseLecturaPlanosEntidadesDao.insercionRegistroArchivo(con, campos);
	}
	
	/**
	 * Método que verifica si existe factura en la tabla de AF
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public boolean existeFacturaEnAF(Connection con,String numeroFactura,String usuario,int codigoCentroAtencion)
	{
		return SqlBaseLecturaPlanosEntidadesDao.existeFacturaEnAF(con, numeroFactura,usuario,codigoCentroAtencion);
	}
	
	/**
	 * Método que actualiza el indicatico inconsistencia en una factura de la tabla AF
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public int actualizarFacturaComoInconsistencia(Connection con,String numeroFactura,String usuario,int codigoCentroAtencion)
	{
		return SqlBaseLecturaPlanosEntidadesDao.actualizarFacturaComoInconsistencia(con, numeroFactura,usuario,codigoCentroAtencion);
	}
	
	/**
	 * Método para consultar las facturas de un paciente
	 * @param con
	 * @param tipoIdentificacion
	 * @param numeroIdentificacion
	 * @return
	 */
	public ArrayList<String> consultarFacturasPaciente(Connection con,String tipoIdentificacion,String numeroIdentificacion,String usuario,int codigoCentroAtencion)
	{
		return SqlBaseLecturaPlanosEntidadesDao.consultarFacturasPaciente(con, tipoIdentificacion, numeroIdentificacion,usuario,codigoCentroAtencion);
	}
	
	/**
	 * Método que elimina los registros Rips cargados en base de datos
	 * @param con
	 */
	public void eliminarRegistrosRips(Connection con,String usuario,int codigoCentroAtencion)
	{
		SqlBaseLecturaPlanosEntidadesDao.eliminarRegistrosRips(con,usuario,codigoCentroAtencion);
	}
	
	/**
	 * Método implementado para cargar el listado de facturas
	 */
	public HashMap<String, Object> cargarListadoFacturas(Connection con,String usuario,int codigoCentroAtencion)
	{
		return SqlBaseLecturaPlanosEntidadesDao.cargarListadoFacturas(con,usuario,codigoCentroAtencion);
	}
	
	/**
	 * Método que realiza la consulta de los pacientes de una factura
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public HashMap<String, Object> consultarPacientesFactura(Connection con,String numeroFactura,String usuario,int codigoCentroAtencion)
	{
		return SqlBaseLecturaPlanosEntidadesDao.consultarPacientesFactura(con, numeroFactura,usuario,codigoCentroAtencion);
	}
	
	/**
	 * Consultar información AH
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> consultarInformacionArchivo(Connection con,HashMap campos)
	{
		return SqlBaseLecturaPlanosEntidadesDao.consultarInformacionArchivo(con, campos);
	}
	
	/**
	 * Método que consulta el consecutivo del registro del paciente en entidad subcontratada
	 * que concuerda con los datos encontrados en los archivos RIPS de la factura
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> getDatosRegPacEntidadSubcontratada(Connection con,HashMap campos)
	{
		return SqlBaseLecturaPlanosEntidadesDao.getDatosRegPacEntidadSubcontratada(con, campos);
	}
	/**
	 * Método implementado para consultar el codigo del servicio de un cargo de servicios
	 * @param con
	 * @param campos
	 * @return
	 */
	public int consultarServicioCargo(Connection con,HashMap campos)
	{
		return SqlBaseLecturaPlanosEntidadesDao.consultarServicioCargo(con, campos);
	}
	
	/**
	 * Método que verifica si es válida la autorizacion del servicio
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean esValidoAutorizacionesServicio(Connection con,HashMap campos)
	{
		return SqlBaseLecturaPlanosEntidadesDao.esValidoAutorizacionesServicio(con, campos);
	}
	
	/**
	 * Método para consultar el artículo del cargo
	 * @param con
	 * @param valor
	 * @param parametro
	 * @return
	 */
	public int consultarArticuloCargo(Connection con,String valor,String parametro)
	{
		return SqlBaseLecturaPlanosEntidadesDao.consultarArticuloCargo(con, valor, parametro);
	}
	
	/**
	 * Método implementado para actualizar el detalle del registro de la entidad subcontratada
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarDetalleRegEntidadSubcontratada(Connection con,HashMap campos)
	{
		return SqlBaseLecturaPlanosEntidadesDao.actualizarDetalleRegEntidadSubcontratada(con, campos);
	}
	
	/**
	 * Método implementado para insertar el log de ejecucion del proceso de lectura de planos
	 * @param con
	 * @param logLectura
	 * @return
	 */
	public int insertarLogEjecucionProceso(Connection con,DtoLogLecturaPlanosEntidades logLectura)
	{
		return SqlBaseLecturaPlanosEntidadesDao.insertarLogEjecucionProceso(con, logLectura);
	}
	
	/**
	 * Método para consultar la cantidad de un servicio
	 * @param con
	 * @param campos
	 * @return
	 */
	public int consultarCantidadServicio(Connection con,HashMap campos)
	{
		return SqlBaseLecturaPlanosEntidadesDao.consultarCantidadServicio(con, campos);
	}
	
	/**
	 * Método implementado para finalizar 
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean finalizarRegistroPacienteEntidadSubcontratada(Connection con,HashMap campos)
	{
		return SqlBaseLecturaPlanosEntidadesDao.finalizarRegistroPacienteEntidadSubcontratada(con, campos);
	}
}
