package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;







/**
 * @author Jhony Alexande Duque A.
 * jduque@princetonsa.com
 */
public interface LiberacionCamasIngresosCerradosDao
{

	/**
	 * Metodo encargado de consultar los ingresos
	 * @param connection
	 * @param criterios
	 * ---------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------
	 * -- centroAtencion6 --> Requerido
	 * @return mapa
	 * -------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * --fechaIngreso0_
	 * -- id1_
	 * -- consecutivo2_
	 * -- fechaCierre3_
	 * -- codigoCama4_
	 * -- nombreCama5_
	 */
	public HashMap consultaIngresos (Connection connection, HashMap criterios);

	/**
	 * Metodo encargado de consultar el detalle de 
	 * l afuncionalidad liberacion camas ingresos cerrados
	 * @param connection
	 * @param criterios
	 * ---------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------
	 * -- centroAtencion11 --> Requerido
	 * @return mapa
	 * -------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * -- id1_
	 * -- consecutivo2_
	 * -- viaIngreso3_
	 * -- area4_
	 * -- nombreCama5_
	 * -- tipoPaciente6_
	 * -- numeroCuenta7_
	 * -- fechaCierre8_
	 * -- facturas9_
	 * -- fechaFacturas10_
	 * -- centroAtencion11
	 */
	public HashMap consultaDetalle (Connection connection, HashMap criterios);
	
	
}