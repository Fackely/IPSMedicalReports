/*
 * Creado en  27/06/2005
 *
 */
package com.sies.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Karenth Yuliana Marín
 * 
 * Creado en 27/06/2005
 * 
 * SiEs
 * Parquesoft Manizales
 */
public interface CuadroTurnosDao
{
	/**
	 * Consulta el turno de un cuadro dado el codigo del turno
	 * @param con
	 * @param codigo
	 * @return
	 */
	public ResultSet consultarTurnoCuadro(Connection con, int codigo);
	
	/**
	 * Metodo que retorna la fecha de finalizacion de la ultima programacion
	 * de cuadros de turnos para esa categoria
	 * @param con
	 * @param codigoCategoria
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarFechaUltimaProg(Connection con, int codigoCategoria);
	
	/**
	 *  Metodo que retorna la fecha de finalización del ultimo cuadro programado
	 * para esa enfermera
	 * @param con
	 * @param codigoEnfermera
	 * @return
	 */
	public Collection<HashMap<String, Object>> consulFechaUltEnf(Connection con, int codigoEnfermera);
	
	/**
	 * Metodo que consulta los ultimos dos turnos que tiene asignada una enfermera
	 * @param con
	 * @param codigoEnfermera
	 * @param limiteTurnos Cuantos turnos anteriores
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarUltimosTurnos(Connection con, int codigoEnfermera, int limiteTurnos);
	
	/**
	 * Método que retorna todos los datos de todos los turnos posibles en el
	 * cuadro de turnos
	 * @param con
	 * @param activos True para indicar que liste únicamente los turnos activos
	 * @return Listado de turnos existentes en BD
	 */
	public Collection<HashMap<String, Object>> consultarTurnos(Connection con, boolean activos);

	/**
	 * Método que retorna todos los datos de todos los turnos posibles en el
	 * cuadro de turnos
	 * @param con
	 * @param activos True para indicar que liste únicamente los turnos activos
	 * @param centroCosto Código del centro de costo por el cual se desea filtrar
	 * @return Listado de turnos existentes en BD
	 */
	public Collection<HashMap<String, Object>> consultarTurnos(Connection con, boolean activos, Integer centroCosto);

	/**
	 * Metodo para insertar en la base de datos un nuevo turno
	 * @param con
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoCategoria
	 * @param restriccionesCategoria
	 * @return
	 */
	public int insertarCuadro(Connection con, String fechaInicio, String fechaFin, int codigoCategoria, HashMap<String, Object> restriccionesCategoria);
	
	/**
	 * Metodo que inserta un nuevo turno en la base de datos
	 * @param con
	 * @param codigoCT
	 * @param fecha
	 * @param codigoEnfermera
	 * @param codigoTurno
	 * @return
	 */
	public int insertarTurno(Connection con, int codigoCT, String fecha, int codigoEnfermera, int codigoTurno);
	
	/**
	 * Metodo que inserta un cubrimiento de turno en la base de datos
	 * @param con
	 * @param codigoRegistro
	 * @param codigoCategoria
	 * @param fecha
	 * @param codigoEnfermera
	 * @param codigoTurno
	 * @param eliminar
	 * @return Código secuencial del registro ingresado, -1 en caso de error
	 */
	public int procesarCubrimientoTurno(Connection con, int codigoRegistro, int codigoCategoria, String fecha, int codigoEnfermera, int codigoTurno, boolean eliminar);

	/**
	 * metodo para cosultar el codigo de la asociacion de turno en una fecha
	 * @param con
	 * @param fecha
	 * @param codigoEnfermera
	 * @return
	 */
	public Collection consultarFechaEnf(Connection con, String fecha, int codigoEnfermera);
	
	/**
	 * Metodo para actualizar un turno en la base de datos
	 * @param con
	 * @param codigoCuadro
	 * @param codigoTurno
	 * @param codigoCtTurmo
	 * @return
	 */
	public int actualizarTurno( Connection con, int codigoCuadro, int codigoTurno, int codigoCtTurmo );
	
	/**
	 * Método que retorna el codigo del cuadro de turno que se esta ejecutando y que corresponde a una categoria específica
	 * @param con
	 * @param codigoCategoria
	 * @param fecha
	 * @return
	 */
	public Collection consultarCodigoCuadro(Connection con, int codigoCategoria, String fecha);
	
	/**
	 * Método que consulta toda la inf referente a un cuadro de turnos especifico
	 * @param con
	 * @param codigocuadro
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarTurnosEnfermeras(Connection con, int codigocuadro);
	
	/**
	 * Método que consulta la fecha maxima y mánima de los turnos de una enfermera
	 * @param con
	 * @param codigoEnfermera
	 * @return
	 */
	public Collection consultarFechasMaximaMinimaTurnosEnfermera(Connection con, int codigoEnfermera);
	
	/**
	 * Método que consulta la fecha máxima y mínima de los turnos de una categoria
	 * @param con
	 * @param codigoEnfermera
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarFechasMaximaMinimaTurnosCategoria(Connection con, int codigoCategoria);

	/**
	 * Consulta los turnos de una enfermera entre dos fechas dadas
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoEnfermera
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarTurnosEnfermera(Connection con, String fechaInicial, String fechaFinal, int codigoEnfermera);

	/**
	 * Método que consulta los turnos de una categoría entre dos fechas dadas
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoEnfermera
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarTurnosCategoria(Connection con, String fechaInicial, String fechaFinal, int codigoCategoria);

	/**
	 * Metodo que Actualiza un turno ya sea con una observacion o cambio de turno o ambos
	 * @param con
	 * @param ctcodigo
	 * @param codigoTurno
	 * @param observacion
	 * @return
	 */
	public  int actualizarTurnoObservacion(Connection con, int ctcodigo, int codigoTurno, String observacion);
	
	/**
	 * Método que consulta lo datos para generar el reporte
	 * @param con
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @param tipoVinculacion
	 * @return
	 */
	public Collection consultarTurnosReporte(Connection con, int codigoCategoria, String fechaInicio, String fechaFin, int tipoVinculacion);
	
	/**
	 * Metodo para consultarlas personas que poseen observaciones en sus turnos y ser mostradas en el reporte de observaciones
	 * @param con
	 * @param codigoCategoria
	 */
	public Collection<HashMap<String, Object>> consultarTurnosReporteObPersona(Connection con, int codigoCategoria,String fechaInicio, String fechaFin);
	
	/**
	 * Metodo para consultar los turnos correspondientes al reporte de Observaciones 
	 * @param con
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarTurnosReporteObs(Connection con, String fechaInicio, String fechaFin);

	/**
	 * Método que busca las personas que pertenecían a un cuadro en una fecha específica
	 * @param con
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarPersonasCuadro(Connection con, int codigoCategoria, String fechaInicio, String fechaFin);

	/**
	 * (Juan David)
	 * Método que busca las personas que pertenecían a un cuadro en una fecha específica
	 * @param con
	 * @param codigoCuadro
	 * @param fechaInicio
	 * @param fechaFin
	 * @return Listado de personas
	 */
	public Collection<HashMap<String, Object>> consultarPersonasCuadroPorCodigoCuadro(Connection con, int codigoCuadro, String fechaInicio, String fechaFin);
	/**
	 * Método que consulta las observaciones de un turno específico
	 * @param con
	 * @param codigoTurno
	 * @return Collection con las observaciones descritas
	 */
	public Collection<HashMap<String, Object>> consultarObservacionesTurno(Connection con, int codigoTurno);

	/**
	 * Método que consulta el listado de los tipos de observaciónexistentes en la BD
	 * @param con
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarTiposObservacion(Connection con);

	/**
	 * Método que actualiza las observaciones de un turno
	 * @param con
	 * @param tipoObservacion
	 * @param codigoTurnoObservacion
	 * @param novedad
	 * @param usuario
	 * @param observacion2
	 * @return true si se ingresó, false de lo contrario
	 */
	public boolean ingresarActualizarObservacion(Connection con, String observacion, int tipoObservacion, int codigoTurnoObservacion, int novedad, String usuario);

	/**
	 * Método para consultar el listado de personas con el que se debe generar el cuadro de turnos
	 * @param con
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarEnfermeraCategoria(Connection con, int codigoCategoria, String fechaInicio, String fechaFin);

	/**
	 * Método para consultar las restricciones de los cuadros de turnos generados
	 * entre las fechas pasadas por parámetros y la categoría seleccionada
	 * @param con
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoCategoria
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarRestriccionesCuadro(Connection con, String fechaInicio, String fechaFin, int codigoCategoria);

	/**
	 * Método para Consultar el codigo del turno relacionado al cuadro de turnos
	 * @param con
	 * @param codigoCT
	 * @param fecha
	 * @param codigoPersona
	 */
	public int consultarCTCodigoTurno(Connection con, int codigoCT, String fecha, int codigoPersona);

	/**
	 * Método que lista los cuadros de turnos que contengan la
	 * fecha pasada por parámetros
	 * @param con
	 * @param fecha
	 * @param centroCosto Centro de Costo del usuario
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarCuadroTurnosDadaUnaFecha(Connection con, String fecha, Integer centroCosto);

	/**
	 * Método que lista los cuadros de turnos que pueden ser
	 * eliminados pasada una fecha
	 * @param con
	 * @param fecha
	 * @param centroCosto Centro de costo del usuario
	 * @return Listado de cuadro de turnos
	 */
	public Collection<HashMap<String, Object>> consultarCuadroTurnosEliminar(Connection con, String fecha, Integer centroCosto);

	/**
	 * Método que permite eliminar un cuadro de turnoas dado su código
	 * Elimina todos los turnos y observaciones que estén asociados a él
	 * @param con
	 * @param codigoCuadroTurnos
	 * @return int
	 */
	public int eliminar(Connection con, int codigoCuadroTurnos);

	/**
	 * Método que me consulta estrictamente un turno de una persona
	 * @param con
	 * @param codigoCategoria
	 * @param turnos
	 * @return Los datos del turno
	 */
	public Collection<HashMap<String, Object>> consultarTurnoPersona(Connection con, int codigoCategoria, String turnos);

	/**
	 * Método para eliminar un turno específico para una persona en una fecha
	 * @param con
	 * @param codigoPersonaEliminar
	 * @param fecha
	 * @return Retorna 0 en caso de error, en caso de éxito retorna el número de elementos borrados de la base de datos
	 */
	public int eliminarTurno(Connection con, int codigoPersonaEliminar, String fecha);
	
	/**
	 * Método para ingrementar o elíminar el número de horas trabajadas por una persona
	 * en un turno específico sin necesidad de crear un turno nuevo
	 * @param con
	 * @param ctTurno
	 * @param numeroHoras
	 * @return retorna el número de modificaciones en BD
	 */
	public int ingresarHorasTurno(Connection con, int ctTurno, double numeroHoras);

	/**
	 * Método para almacenar el numero de horas faltantes del mes para cada persona
	 * @param con
	 * @param codigoEnfermera
	 * @param numeroHorasFaltantes
	 * @param numeroHorasFaltantesAnteriores
	 * @return numero de registros almacenados en BD
	 */
	public int guardarHorasFaltantes(Connection con, int codigoEnfermera, double numeroHorasFaltantes, double numeroHorasFaltantesAnteriores);
	
	/**
	 * Método que almacena las horas adicionales al turno en BD
	 * En caso de que ya existan son adicionadas a las horas existentes
	 * @param con
	 * @param codigoTurno
	 * @param numeroHoras
	 * @param usuario
	 * @return Número de registros insertados o modificados en BD, -1 en caso de error
	 */
	public int modificarHorasTurno(Connection con, int codigoTurno, double numeroHoras, String usuario);

	/**
	 * Método que eliminar las horas adicionales de un turno en BD
	 * @param con Conexión con la BD
	 * @param codigoTurno Código del turno que se desea eliminar
	 * @return Número de registros modificados en BD, -1 en caso de error
	 */
	public int eliminarHorasTurno(Connection con, int codigoTurno);

}
