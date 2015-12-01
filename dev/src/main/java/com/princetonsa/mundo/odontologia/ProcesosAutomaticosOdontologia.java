package com.princetonsa.mundo.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import util.InfoDatosDouble;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoLogProcAutoCitas;
import com.princetonsa.dto.odontologia.DtoLogProcAutoEstados;
import com.princetonsa.dto.odontologia.DtoLogProcAutoFact;
import com.princetonsa.dto.odontologia.DtoLogProcAutoServCita;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;


/**
 * 
 * @author axioma
 *
 */
public class ProcesosAutomaticosOdontologia 
{
	/**
	 * 
	 * @param dto
	 * @param con
	 * @return
	 */
	public static double guardarLogAutoServCita(DtoLogProcAutoServCita dto ,Connection con){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProcesosAutomaticosOdontologiaDao().guardarLogAutoServCita(dto, con);
	}
	
	/**
	 * 
	 * @param dto
	 * @param con
	 * @return
	 */
	public static double guardarLogProcCitas(DtoLogProcAutoCitas dto ,Connection con){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProcesosAutomaticosOdontologiaDao().guardarLogProcCitas(dto, con);
	}
	
	/**
	 * 
	 * @param dto
	 * @param tiempo
	 * @param controlaAbonosPorIngreso Indica si controla abonos por ingreso
	 * @return
	 */
	public static ArrayList<DtoLogProcAutoCitas> cargarCitasDisponibles(DtoLogProcAutoCitas dto, String tiempo,ArrayList<String> estados, boolean controlaAbonosPorIngreso){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProcesosAutomaticosOdontologiaDao().cargarCitasDisponibles(dto, tiempo, estados, controlaAbonosPorIngreso);
	}
	
	/**
	 * @param codigosPk
	 * @param estado
	 * @param con
	 * @return
	 */

	public static boolean modificarCita(ArrayList<Integer> codigosPk, String estado, Connection con){
			return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProcesosAutomaticosOdontologiaDao().modificarCita(codigosPk, estado, con);
	}
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<InfoDatosDouble> cargarProcAutoServCita(
			DtoServicioCitaOdontologica dtoWhere) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProcesosAutomaticosOdontologiaDao().cargarProcAutoServCita(dtoWhere);
	}
	
	/**
	 * 
	 * @param dto
	 * @param con
	 * @return
	 */

	public static double guardarLogProcAutoFact(DtoLogProcAutoFact dto, Connection con) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProcesosAutomaticosOdontologiaDao().guardarLogProcAutoFact(dto, con);
	}

	/**
	 * 
	 * @param dto
	 * @param con
	 * @return
	 */
	public static  double guardarProcAutoEstados(DtoLogProcAutoEstados dto,
			Connection con) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProcesosAutomaticosOdontologiaDao().guardarProcAutoEstados(dto, con);
	}
	
	/**
	 * 
	 * Metodo para cerrar los ingresos
	 * @param con
	 * @param institucion
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static boolean cerrarIngresosOdontologicos(Connection con, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProcesosAutomaticosOdontologiaDao().cerrarIngresosOdontologicos(con, institucion);
	}
	
	
}
