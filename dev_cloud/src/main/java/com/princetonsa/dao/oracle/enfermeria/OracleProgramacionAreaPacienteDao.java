package com.princetonsa.dao.oracle.enfermeria;

import java.sql.Connection;
import java.util.HashMap;
import com.princetonsa.dao.enfermeria.ProgramacionAreaPacienteDao;
import com.princetonsa.dao.sqlbase.enfermeria.SqlBaseProgramacionAreaPacienteDao;



/**
 * @version 1.0
 * @fecha 06/01/09
 * @author Jhony Alexander Duque A. y Diego Fernando Bedoya Castaño
 *
 */
public class OracleProgramacionAreaPacienteDao implements ProgramacionAreaPacienteDao
{
	/**
	 * Consulta las Areas por Via de Ingreso segun centro de Atencion
	 */
	public HashMap<String, Object> listaAreas (Connection con, int centroAtencion)
	{
		return SqlBaseProgramacionAreaPacienteDao.listaAreas(con, centroAtencion);
	}
	
	/**
	 * Consulta los Pisos segun centro de Atencion
	 */
	public HashMap<String, Object> listaPisos (Connection con, int centroAtencion)
	{
		return SqlBaseProgramacionAreaPacienteDao.listaPisos(con, centroAtencion);
	}
	
	/**
	 * Consulta las Habitaciones segun piso
	 */
	public HashMap<String, Object> listaHabitaciones (Connection con, int piso)
	{
		return SqlBaseProgramacionAreaPacienteDao.listaHabitaciones(con, piso);
	}
	
	/**
	 * Consulta por Area de los Pacientes
	 */
	public HashMap<String, Object> ConsultaPacientes (Connection con, String fechaIniProg, String fechaFinProg, String area, String piso, String habitacion, int checkProg, String horaIniProg, String horaFinProg, int remite)
	{
		return SqlBaseProgramacionAreaPacienteDao.ConsultaPacientes(con, fechaIniProg, fechaFinProg, area, piso, habitacion, checkProg, horaIniProg, horaFinProg, remite);
	}
	
	/**
	 * Metodo encargado de Consultar el listado de ordenes medicas
	 * de un paciente.
	 * @param connection
	 * @param criterios
	 * ---------------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------------
	 *  cuenta6 --> Requerido
	 * idPaciente7 --> Requerido
	 * @return HashMap
	 * ---------------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ---------------------------------------
	 * numeroOrden0_,medicamento1_,presentacion2_,
	 * dosis3_,via4_,frecuencia5_
	 * 
	 */
	public HashMap consultarListadoOrdenesMedicas (Connection connection,HashMap criterios, String remite)
	{
		return SqlBaseProgramacionAreaPacienteDao.consultarListadoOrdenesMedicas(connection,criterios,remite);
	}
	
	/**
	 * Metodo encargado de insertar el encabezado de
	 * la programacion
	 * @param connection
	 * @param datos
	 * -----------------------------------
	 * KEY'S DEL MAPA DATOS
	 * -----------------------------------
	 * numeroSolicitud10_ --> Requerido
	 * articulo11_ --> Requerido
	 * fechaProg6_ --> Requerido
	 * horaProg7_ --> Requerido
	 * usuarioProg9_ --> Requerido
	 * institucion12 --> Requerido
	 * observ8_ --> Requerido
	 * @return
	 */
	public int insertarEncabezadoProgramacion (Connection connection,HashMap datos)
	{
		return SqlBaseProgramacionAreaPacienteDao.insertarEncabezadoProgramacion(connection, datos);
	}
	
	/**
	 * Metodo encargado de insertar los datos del detalle
	 * de la programacion
	 * @param connection
	 * @param datos
	 * ---------------------------
	 * KEY'S DEL MAPA DATOS
	 * ---------------------------
	 * codigoProg13 --> Requerido
	 * fechaProg14 --> Requerido
	 * horaProg15 --> Requerido
	 * activo16 --> Requerido
	 * @return
	 */
	public int insertarDetalleProgramacion (Connection connection,HashMap datos)
	{
		return SqlBaseProgramacionAreaPacienteDao.insertarDetalleProgramacion(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de consultar los datos de la
	 * programacion de administracion de medicamentos.
	 * @param connection
	 * @param codigoAdmin
	 * @return
	 */
	public HashMap consultarProgramacionAdmin (Connection connection,int codigoAdmin,String validaFecha)
	{
		return SqlBaseProgramacionAreaPacienteDao.consultarProgramacionAdmin(connection, codigoAdmin,validaFecha);
	}
	
	
	/**
	 * Metodo encargado de actualizar el estado del
	 * detalle de la programacion
	 * @param connection
	 * @param activo
	 * @param codigo
	 * @return
	 */
	public boolean actualizarEstadoProgramacionAdmin (Connection connection,String activo,int codigo)
	{
		return SqlBaseProgramacionAreaPacienteDao.actualizarEstadoProgramacionAdmin(connection, activo, codigo);
	}
}