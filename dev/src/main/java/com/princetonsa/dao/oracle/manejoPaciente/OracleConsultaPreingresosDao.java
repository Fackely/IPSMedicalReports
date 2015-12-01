package com.princetonsa.dao.oracle.manejoPaciente;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.ConsultaPreingresosDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseConsultaPreingresosDao;







/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class OracleConsultaPreingresosDao implements ConsultaPreingresosDao
{
	
	/**
	 * Metodo encargado de consultar el listado 
	 * de preingresos
	 * @author Jhony Duque A.
	 * @param connection
	 * @param criterios
	 * --------------------------
	 * 	KEY'S DEL MAPA CRITERIOS
	 * --------------------------
	 * -- centroAten0 --> Opcional
	 * -- fechaIni1 --> Opcional
	 * -- fechaFin2 --> Opcional
	 * -- estados3 --> Opcional
	 * -- usuario4 --> Opcional
	 * -- institucion5 --> Requerido
	 * @return  HashMap
	 * -------------------------------
	 * KEY'S DEL MAPA RSULTADO
	 * -------------------------------
	 * -- centroAtencion0_
	 * -- nombreCentroAtencion1_
	 * -- id2_
	 * -- consecutivo3_
	 * -- fechaPreingresoPen4_
	 * -- horaPreingresoPen5_
	 * -- usuarioPreingresoPen6_
	 * -- fechaPreingresoGen7_
	 * -- horaPreingresoGen8_
	 * -- usuarioPreingresoGen9_
	 * -- estado10_
	 * -- tipoIdent11_
	 * -- identPac12_
	 * -- nombrePac13_
	 * -- codigoPac14_
	 */
	public HashMap cargarListadoPreingresos (Connection connection, HashMap criterios )
	{
		return SqlBaseConsultaPreingresosDao.cargarListadoPreingresos(connection, criterios);
	}
	
	/**
	 * Metodo encargado de obtener los usuarios de preingresos
	 * @param connection
	 * @param criterios
	 * --------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------
	 * -- estados3 --> Requerido
	 * -- institucion5 --> Requerido
	 */
	public ArrayList<HashMap<String, Object>> obtenerUsuariosPreingreso (Connection connection, HashMap criterios)
	{
		return SqlBaseConsultaPreingresosDao.obtenerUsuariosPreingreso(connection, criterios);
	}
	
	
	/**
	 * Metodo encargado de cargar el detalle
	 * del preingresos
	 * @param connection
	 * @param criterios
	 * -------------------------------
	 * KEY'S DE MAPA CRITERIOS
	 * -------------------------------
	 * -- institucion5 --> Requerido
	 * -- ingreso8 --> Requerido
	 */
	public HashMap cargarDetallePreingreso (Connection connection, HashMap criterios )
	{
		return SqlBaseConsultaPreingresosDao.cargarDetallePreingreso(connection, criterios);
	}
	
	
}