package com.princetonsa.dao.enfermeria;

import java.sql.Connection;
import java.util.HashMap;



/**
 * @version 1.0
 * @fecha 06/01/09
 * @author Jhony Alexander Duque A. y Diego Fernando Bedoya Castaño
 *
 */
public interface ProgramacionAreaPacienteDao
{
	/**
	 * Consulta las Areas por Via de Ingreso segun Centro de Atencion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> listaAreas (Connection con, int centroAtencion);
			
	/**
	 * Consulta los Pisos segun Centro de Atencion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> listaPisos (Connection con, int centroAtencion);
	
	/**
	 * Consulta las Habitaciones segun Piso
	 * @param con
	 * @param Piso
	 * @return
	 */
	public HashMap<String, Object> listaHabitaciones (Connection con, int piso);
	
	/**
	 * Consulta por Area de los Pacientes
	 * @param con
	 * @param fechaIniProg
	 * @param fechaFinProg
	 * @param area
	 * @param piso
	 * @param habitacion
	 * @param checkProg
	 * @param horaIniProg
	 * @param horaFinProg
	 * @return
	 */
	public HashMap<String, Object> ConsultaPacientes (Connection con, String fechaIniProg, String fechaFinProg, String area, String piso, String habitacion, int checkProg, String horaIniProg, String horaFinProg, int remite);
	
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
	public HashMap consultarListadoOrdenesMedicas (Connection connection,HashMap criterios, String remite);
	
	
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
	public int insertarEncabezadoProgramacion (Connection connection,HashMap datos);
	
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
	public int insertarDetalleProgramacion (Connection connection,HashMap datos);
	
	/**
	 * Metodo encargado de consultar los datos de la
	 * programacion de administracion de medicamentos.
	 * @param connection
	 * @param codigoAdmin
	 * @return
	 */
	public HashMap consultarProgramacionAdmin (Connection connection,int codigoAdmin,String validaFecha);
	
	
	
	/**
	 * Metodo encargado de actualizar el estado del
	 * detalle de la programacion
	 * @param connection
	 * @param activo
	 * @param codigo
	 * @return
	 */
	public boolean actualizarEstadoProgramacionAdmin (Connection connection,String activo,int codigo);
	
	
}