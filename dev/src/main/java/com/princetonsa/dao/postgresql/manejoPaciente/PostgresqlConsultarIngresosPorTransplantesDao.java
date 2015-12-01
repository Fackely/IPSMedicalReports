package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.ConsultarIngresosPorTransplantesDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseConsultarIngresosPorTransplantesDao;




/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class PostgresqlConsultarIngresosPorTransplantesDao implements ConsultarIngresosPorTransplantesDao
{

	/**
	 * Metodo encargado de consultar los ingrsos por transplantes
	 * @param connection
	 * @param criterios
	 * -----------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * -----------------------------
	 * -- centAten0
	 * -- viaIngreso1
	 * -- estIngreso2
	 * -- tipoTransp3
	 * -- fechaIniEgre4
	 * -- fechaFinEgre5
	 * -- institucion6
	 * @return mapa
	 * -------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * centroAtencion0_,nomCentroCosto1_,
	 * idIngreso2_,consecutivo3_,
	 * fechaIngreso4_,viaIngreso5_,
	 * nomViaIngreso6_,transplante7_,
	 * paciente8_,nomPersona9_,
	 * identPaciente10_,sexoPaciente11_,
	 * fechaNacimiento12_
	 */
	public HashMap consultarIngresosTransplante (Connection connection, HashMap criterios)
	{
		return SqlBaseConsultarIngresosPorTransplantesDao.consultarIngresosTransplante(connection, criterios);
	}
	
	/**
	 * Metodo  encargado de consultar del detalle de los traslados de solicitudes
	 * @param connection
	 * @param idTraslado
	 * @return mapa
	 * -------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------
	 * -- solicitudTrasladada0_
	 * -- solicitudGenerada1
	 * -- fechaTraslado2_
	 * -- horaTraslado3_
	 */
	public HashMap consultarTrasladoSolicitudes (Connection connection,String idTraslado)
	{
		return SqlBaseConsultarIngresosPorTransplantesDao.consultarTrasladoSolicitudes(connection, idTraslado);
	}
	
	/**
	 * Metodo encargado de consultar los traslados de ingrespos a un paciente
	 * @param connection
	 * @param institucion
	 * @param tipoTransplante
	 * @param ingresoBuscar
	 * @return mapa
	 * -------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------
	 * -- consecutivo0_
	 * -- valorCampoSub1_
	 */
	public HashMap consultarTraslado (Connection connection,String institucion,String tipoTransplante, String ingresoBuscar)
	{
		return SqlBaseConsultarIngresosPorTransplantesDao.consultarTraslado(connection, institucion, tipoTransplante, ingresoBuscar);
	}
	
	
	
}