package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.RegistroEventosCatastroficosDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseRegistroEventosCatastroficosDao;
import com.princetonsa.dto.manejoPaciente.DtoRegistroEventosCatastroficos;

public class PostgresqlRegistroEventosCatastroficosDao implements
		RegistroEventosCatastroficosDao
{

	/**
	 * 
	 */
	public DtoRegistroEventosCatastroficos consultarRegistroEventoCatastroficoIngreso(Connection con, int codigoIngreso,String codigoEvento)
	{
		return SqlBaseRegistroEventosCatastroficosDao.consultarRegistroEventoCatastroficoIngreso(con,codigoIngreso,codigoEvento);
	}

	/**
	 * 
	 */
	public boolean insertarRegistroAccidentesTransito(Connection con, DtoRegistroEventosCatastroficos dto, int institucion)
	{
		return SqlBaseRegistroEventosCatastroficosDao.insertarRegistroAccidentesTransito(con, dto, institucion);
	}

	/**
	 * 
	 */
	public boolean modificarRegistroAccidentesTransito(Connection con, DtoRegistroEventosCatastroficos dto, int institucion)
	{
		return SqlBaseRegistroEventosCatastroficosDao.modificarRegistroAccidentesTransito(con, dto, institucion);
	}
	
	/**
	 * Método implementado para realizar la actualizacion del estado de un evento catastrofico
	 * @param con
	 * @param dtoCatastrofico
	 * @return
	 */
	public int actualizarEstadoRegistroEventoCatastrofico(Connection con,DtoRegistroEventosCatastroficos dtoCatastrofico)
	{
		return SqlBaseRegistroEventosCatastroficosDao.actualizarEstadoRegistroEventoCatastrofico(con, dtoCatastrofico);
		
	}
	
	
	/**
	 * Metodo encargado de consultar todos los registros de
	 * eventos catastroficos en estado finalizado 
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
	 * -- fechaEvento0_
	 * -- departamentoEvento1_
	 * -- ciudadEvento2_
	 * -- direccionEvento3_
	 * -- tipoEvento4_
	 * -- codigo5_
	 */
	public HashMap cargarListadoEventosCatastroficos (Connection connection,HashMap criterios)
	{
		return SqlBaseRegistroEventosCatastroficosDao.cargarListadoEventosCatastroficos(connection, criterios);
	}
	
	/**
	 * Metodo encargado de asociar un evento catastrofico
	 * a un ingreso
	 * @param connection
	 * @param ingreso
	 * @param codigoEvento
	 * @return
	 */
	public  boolean asociarEvento (Connection connection, String ingreso,String codigoEvento )
	{
		return SqlBaseRegistroEventosCatastroficosDao.asociarEvento(connection, ingreso, codigoEvento);	
	}
	
	/**
	 * Metodo encargado de actualizar los valores de 
	 * la seccion de Amparos
	 * @param con
	 * @param dto
	 * @param institucion
	 * @return
	 */
	public boolean actualizarAmparos(Connection con, DtoRegistroEventosCatastroficos dto, int institucion)
	{
		return SqlBaseRegistroEventosCatastroficosDao.actualizarAmparos(con, dto, institucion);	
	}
	
	/**
	 * Metodo encargado de actualizar los valores de 
	 * la seccion Datos de la Reclamacion
	 * @param con
	 * @param dto
	 * @param institucion
	 * @return
	 */
	public boolean actualizarDatosReclamacion(Connection con, DtoRegistroEventosCatastroficos dto, int institucion)
	{
		return SqlBaseRegistroEventosCatastroficosDao.actualizarDatosReclamacion(con, dto, institucion);	
	}
	
	/**
	 * Metodo para actualizar los Servicios reclamados
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarServiciosReclamados(Connection con,DtoRegistroEventosCatastroficos dto)
	{
		return SqlBaseRegistroEventosCatastroficosDao.actualizarServiciosReclamados(con, dto);
	}
}