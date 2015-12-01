package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dto.manejoPaciente.DtoRegistroEventosCatastroficos;

public interface RegistroEventosCatastroficosDao
{

	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public abstract DtoRegistroEventosCatastroficos consultarRegistroEventoCatastroficoIngreso(Connection con, int codigoIngreso,String codigoEvento);

	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public abstract boolean insertarRegistroAccidentesTransito(Connection con, DtoRegistroEventosCatastroficos dto, int institucion);

	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public abstract boolean modificarRegistroAccidentesTransito(Connection con, DtoRegistroEventosCatastroficos dto, int institucion);
	
	/**
	 * Método implementado para realizar la actualizacion del estado de un evento catastrofico
	 * @param con
	 * @param dtoCatastrofico
	 * @return
	 */
	public abstract int actualizarEstadoRegistroEventoCatastrofico(Connection con,DtoRegistroEventosCatastroficos dtoCatastrofico);
	
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
	public HashMap cargarListadoEventosCatastroficos (Connection connection,HashMap criterios);
	
	/**
	 * Metodo encargado de asociar un evento catastrofico
	 * a un ingreso
	 * @param connection
	 * @param ingreso
	 * @param codigoEvento
	 * @return
	 */
	public boolean asociarEvento (Connection connection, String ingreso,String codigoEvento );

	/**
	 * Metodo encargado de actualizar los valores de 
	 * la seccion de Amparos
	 * @param con
	 * @param dto
	 * @param institucion
	 * @return
	 */
	public abstract boolean actualizarAmparos(Connection con, DtoRegistroEventosCatastroficos dto, int institucion);

	/**
	 * Metodo encargado de actualizar los valores de 
	 * la seccion de Datos de la Reclamacion
	 * @param con
	 * @param dto
	 * @param institucion
	 * @return
	 */
	public abstract boolean actualizarDatosReclamacion(Connection con, DtoRegistroEventosCatastroficos dto, int institucion);

	/**
	 * Metodo para actualizar los Servicios reclamados
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarServiciosReclamados(Connection con,DtoRegistroEventosCatastroficos dto);
}