package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.RegistroAccidentesTransitoDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseRegistroAccidentesTransitoDao;
import com.princetonsa.dto.manejoPaciente.DtoRegistroAccidentesTransito;

public class PostgresqlRegistroAccidentesTransitoDao implements
		RegistroAccidentesTransitoDao
{

	/**
	 * 
	 */
	public DtoRegistroAccidentesTransito consultarRegistrosAccidentesTransito(
			Connection con, String ingreso)
	{
		return SqlBaseRegistroAccidentesTransitoDao.consultarRegistrosAccidentesTransito(con, ingreso);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public DtoRegistroAccidentesTransito consultarRegistroAccidentesTransitoLlave(Connection con, String codigo)
	{
		return SqlBaseRegistroAccidentesTransitoDao.consultarRegistrosAccidentesTransitoLlave(con, codigo);
	}
	

	/**
	 * 
	 */
	public int insertarRegistroAccidentesTransito(Connection con,
			DtoRegistroAccidentesTransito dto)
	{
		return SqlBaseRegistroAccidentesTransitoDao.insertarRegistroAccidentesTransito(con, dto);
	}

	/**
	 * 
	 */
	public boolean modificarRegistroAccidentesTransito(Connection con,
			DtoRegistroAccidentesTransito dto)
	{
		return SqlBaseRegistroAccidentesTransitoDao.modificarRegistroAccidentesTransito(con, dto);
	}

	
	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public DtoRegistroAccidentesTransito consultarRegistroAccidentesTransitoIngreso(Connection con, String ingreso)
	{
		return SqlBaseRegistroAccidentesTransitoDao.consultarRegistroAccidentesTransitoIngreso(con,ingreso);
	}
	
	/**
	 * metodo que realiza la busqueda avanzada x rangos
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 */
	public HashMap busquedaAvanzada(Connection con, HashMap criteriosBusquedaMap) 
	{
		return SqlBaseRegistroAccidentesTransitoDao.busquedaAvanzada(con, criteriosBusquedaMap); 
	}
	
	/**
	 * Método implementado para actualizar el estado del registro de accidentes de transito,
	 * si el estado es anulación se ingresa la fecha, hora y usuario anulacion
	 * @param con
	 * @param dtoReg
	 * @return
	 */
	public int actualizarEstadoRegistroAccidenteTransito(Connection con,DtoRegistroAccidentesTransito dtoReg)
	{
		return SqlBaseRegistroAccidentesTransitoDao.actualizarEstadoRegistroAccidenteTransito(con,dtoReg);
	}
	
	/**
	 * mapa de generarReporteCertificadoAtencionMedica
	 * @param con
	 * @param criteriosBusquedaMap, keys--> codigoCentroAtencion , codigoInstitucion, idIngreso
	 * @return
	 */
	public HashMap generarReporteCertificadoAtencionMedica(Connection con, HashMap criteriosBusquedaMap) 
	{
		return SqlBaseRegistroAccidentesTransitoDao.generarReporteCertificadoAtencionMedica(con, criteriosBusquedaMap);
	}
	
	/**
	 * 
	 * @param con
	 * @param criteriosBusquedaMap, keys--> codigoCentroAtencion , codigoInstitucion, idIngreso
	 * @return
	 */
	public HashMap generarReporteFUSOAT01(Connection con, HashMap criteriosBusquedaMap)
	{
		return SqlBaseRegistroAccidentesTransitoDao.generarReporteFUSOAT01(con, criteriosBusquedaMap);
	}
	
	/**
	 * Metodo encargado de consultar todos los registros de
	 * accidentes de transito  filtrandolos por el estado. 
	 * @param connection
	 * @param criterios
	 * -------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * -------------------------
	 * -- estado
	 * @return
	 * ---------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ---------------------------
	 * -- fechaAccidente0_
	 * -- departamentoAccidente1_
	 * -- ciudadAccidente2_
	 * -- lugarAccidente3_
	 * -- asegurado4_
	 * -- nombreConductor5
	 * -- nombrePropietario6
	 */
	public HashMap cargarListadoAccidentesTransito (Connection connection,HashMap criterios)
	{
		return SqlBaseRegistroAccidentesTransitoDao.cargarListadoAccidentesTransito(connection, criterios);
	}
	
	
	/**
	 * Metodo encargado de asociar un accidente de transito 
	 * a un ingreso
	 * @param connection
	 * @param ingreso
	 * @param codigoAccidente
	 * @return
	 */
	public boolean asociarAcciedente (Connection connection, String ingreso,String codigoAccidente )
	{
		return SqlBaseRegistroAccidentesTransitoDao.asociarAcciedente(connection, ingreso, codigoAccidente);
	}
	
	/**
	 * Metodo encargado de modificar la seccion de maparos
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean modificarAmparos(Connection con,DtoRegistroAccidentesTransito dto)
	{
		return SqlBaseRegistroAccidentesTransitoDao.modificarAmparos(con, dto); 
	}
	
	/**
	 * Metodo encargado de modificar los datos de la Reclamacion
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean modificarReclamacion(Connection con,DtoRegistroAccidentesTransito dto)
	{
		return SqlBaseRegistroAccidentesTransitoDao.modificarReclamacion(con, dto);
	}
}
