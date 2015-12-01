package com.princetonsa.dao.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import util.InfoDatosDouble;

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
public interface ProcesosAutomaticosOdontologiaDao {
	
	/**
	 * 
	 * @param dto
	 * @param con
	 * @return
	 */
	public  double guardarLogProcCitas(DtoLogProcAutoCitas dto, Connection con);
	
	/**
	 * 
	 * @param dto
	 * @param con
	 * @return
	 */
	public  double guardarLogAutoServCita(DtoLogProcAutoServCita dto, Connection con);
	
	/**
	 * 
	 * @param dto
	 * @param tiempo
	 * @param controlaAbonosPorIngreso Valor por defecto para verificar si controla abonos por ingreso
	 * @return
	 */
	public  ArrayList<DtoLogProcAutoCitas> cargarCitasDisponibles(DtoLogProcAutoCitas dto, String tiempo, ArrayList<String> estados, boolean controlaAbonosPorIngreso);
		
	/**
	 * 
	 * @param codigosPk
	 * @param estado
	 * @param con
	 * @return
	 */
	public  boolean modificarCita(ArrayList<Integer> codigosPk, String estado, Connection con);
	
	/**
	 * 
	 * 
	 */
	public  ArrayList<InfoDatosDouble> cargarProcAutoServCita(DtoServicioCitaOdontologica dtoWhere);
	
	/**
	 * 
	 * @param dto
	 * @param con
	 * @return
	 */
	public  double guardarLogProcAutoFact(DtoLogProcAutoFact dto, Connection con);

	/**
	 * 
	 * @param dto
	 * @param con
	 * @return
	 */
	public double guardarProcAutoEstados(DtoLogProcAutoEstados dto , Connection con);
	
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
	public boolean cerrarIngresosOdontologicos(Connection con, int institucion);
	
}
