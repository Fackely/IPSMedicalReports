package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;





/**
 * @author Jhony Alexande Duque A.
 * jduque@princetonsa.com
 */
public interface ConsultarIngresosPorTransplantesDao
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
	public HashMap consultarIngresosTransplante (Connection connection, HashMap criterios);
	
	
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
	public HashMap consultarTrasladoSolicitudes (Connection connection,String idTraslado);
	
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
	public HashMap consultarTraslado (Connection connection,String institucion,String tipoTransplante, String ingresoBuscar);
	
	
}