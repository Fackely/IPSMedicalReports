package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;




/**
 * @author Jhony Alexande Duque A.
 * jduque@princetonsa.com
 */
public interface AperturaIngresosDao
{
	
	/**
	 * Metodo encargado de listar los ingresos de un paciente
	 * @param connection
	 * @param codigoPaciente
	 * ------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * -- consecutivo0_
	 * -- fechaIngreso1_
	 * -- horaIngreso2_
	 * -- fechaCierre3_
	 * -- horaCierrre4_
	 * -- nombreUsuario5_
	 * -- codigoIngreso6_
	 * -- codigoPaciente7_
	 * -- centroAtencion8_
	 * -- codigoCentroAtencion9_
	 * -- cuenta10_
	 */
	public HashMap cargarListadoIngresos (Connection connection,String codigoPaciente,String ingreso);
		
	/**
	 * Metodo encargado de actualizar el estado del cierre.
	 * @param connection
	 * @param datosCierre
	 * ----------------------------
	 * KEY'S DEL MAPA CIERRE
	 * ----------------------------
	 * -- activo
	 * -- codigo
	 */
	public  boolean ActualizarEstadoCierre (Connection connection,HashMap datosCierre);
	
	/**
	 * Metodo encargado de hacer una apertera de un ingreso cerrado.
	 * @param connection
	 * @param datosIngreso
	 * ------------------------------
	 * KEY'S DEL MAPA DATOSINGRESO
	 * -------------------------------
	 * -- estado
	 * -- cierreManual
	 * -- aperturaAuto
	 * -- ingreso
	 */
	public boolean ActualizarEstadoIngreso (Connection connection,HashMap datosIngreso);
	
	
}