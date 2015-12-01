/*
 * Creado en  27/06/2005
 *
 */
package com.sies.dao.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;

import com.sies.dao.CuadroTurnosDao;
import com.sies.dao.sqlbase.SqlBaseCuadroTurnosDao;

/**
 * @author Karenth Yuliana Mar�n
 * 
 * Creado en 27/06/2005
 * 
 * SiEs
 * Parquesoft Manizales
 */
public class OracleCuadroTurnosDao implements CuadroTurnosDao
{
	/**
	 * Consulta el turno de un cuadro dado el codigo del turno
	 * @param con
	 * @param codigo
	 * @return
	 */
	public ResultSet consultarTurnoCuadro(Connection con, int codigo)
	{
		return SqlBaseCuadroTurnosDao.consultarTurnoCuadro(con, codigo);
	}
	
	/**
	 * M�todo que retorna la fecha de finalizaci�n del ultimo cuadro programado para esa
	 * categoria
	 * @param con
	 * @param codigoCategoria
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarFechaUltimaProg(Connection con, int codigoCategoria)
	{
		return SqlBaseCuadroTurnosDao.consultarFechaUltima(con, codigoCategoria);
	}
	
	
	/**
	 * M�todo que retorna la fecha de finalizaci�n del ultimo cuadro programado
	 * para esa enfermera
	 * @param con
	 * @param codigoEnfermera
	 * @return
	 */
	public Collection<HashMap<String, Object>> consulFechaUltEnf(Connection con, int codigoEnfermera)
	{
		return SqlBaseCuadroTurnosDao.consulFechaUltEnf(con, codigoEnfermera);
	}
	
	/**
	 * M�todo que consulta los ultimos dos turnos que tiene asignada una enfermera
	 * @param con
	 * @param codigoEnfermera
	 * @param ultimosTurnos
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarUltimosTurnos(Connection con, int codigoEnfermera, int limiteTurnos)
	{
		return SqlBaseCuadroTurnosDao.consultarUltimosTurnos(con, codigoEnfermera, limiteTurnos);
	}

	@Override
	public Collection<HashMap<String, Object>> consultarTurnos(Connection con, boolean activos)
	{
		return SqlBaseCuadroTurnosDao.consultarTurnos(con, activos);
	}

	@Override
	public Collection<HashMap<String, Object>> consultarTurnos(Connection con, boolean activos, Integer centroCosto)
	{
		return SqlBaseCuadroTurnosDao.consultarTurnos(con, activos, centroCosto);
	}

	@Override
	public int insertarCuadro(Connection con, String fechaInicio, String fechaFin, int codigoCategoria, HashMap<String, Object> restriccionesCategoria) 
	{
		return SqlBaseCuadroTurnosDao.insertarCuadro(con, fechaInicio, fechaFin, codigoCategoria, restriccionesCategoria);
	}


	/**
	 * M�todo que inserta un nuevo turno
	 */
	public int insertarTurno(Connection con, int codigoCT, String fecha, int codigoEnfermera, int codigoTurno)
	{
		return SqlBaseCuadroTurnosDao.insertarTurno(con, codigoCT, fecha, codigoEnfermera, codigoTurno );
	}

	/**
	 * Metodo que inserta un cubrimiento de turno en la base de datos
	 * @param con
	 * @param codigoCategoria
	 * @param fecha
	 * @param codigoEnfermera
	 * @param codigoTurno
	 * @return C�digo secuencial del registro ingresado, -1 en caso de error
	 */
	public int procesarCubrimientoTurno(Connection con, int codigoRegistro, int codigoCategoria, String fecha, int codigoEnfermera, int codigoTurno, boolean eliminar)
	{
		return SqlBaseCuadroTurnosDao.procesarCubrimientoTurno(con, codigoRegistro, codigoCategoria, fecha, codigoEnfermera, codigoTurno, eliminar);
	}

	/**
	 * M�todo para consultar el codigo de la asociacion de un turno en una fecha
	 */
	public Collection consultarFechaEnf(Connection con, String fecha, int codigoEnfermera)
	{
		return SqlBaseCuadroTurnosDao.consultarFechaEnf(con, fecha, codigoEnfermera);
	}
	
	/**
	 * M�todo para actualizar un turno en la BD
	 */
	public int actualizarTurno(Connection con, int codigoCuadro, int codigoTurno, int codigoCtTurmo)
	{
		return SqlBaseCuadroTurnosDao.actualizarTurno(con, codigoCuadro, codigoTurno, codigoCtTurmo);
	}

	/**
	 * M�todo que retorna el codigo del cuadro de turno que se esta ejecutando y que corresponde a una categoria espec�fica
	 */
	public Collection consultarCodigoCuadro(Connection con, int codigoCategoria, String fecha)
	{
		return SqlBaseCuadroTurnosDao.consultarCodigoCuadro(con , codigoCategoria, fecha);
	}


	/**
	 * M�todo que consulta toda la inf referente a un cuadro de turnos especifico
	 */
	public Collection<HashMap<String, Object>> consultarTurnosEnfermeras(Connection con, int codigocuadro)
	{
		
		return SqlBaseCuadroTurnosDao.consultarTurnosEnfermeras(con, codigocuadro);
	}
	
	/**
	 * M�todo que consulta la fecha maxima y minima de los turnos de una enfermera
	 */
	public Collection consultarFechasMaximaMinimaTurnosEnfermera(Connection con, int codigoEnfermera)
	{
		return SqlBaseCuadroTurnosDao.consultarFechasMaximaMinimaTurnosEnfermera(con, codigoEnfermera);
	}
	
	/**
	 * M�todo que consulta la fecha maxima y minima de los turnos de una categoria
	 */
	public Collection<HashMap<String, Object>> consultarFechasMaximaMinimaTurnosCategoria(Connection con, int codigoCategoria)
	{
		return SqlBaseCuadroTurnosDao.consultarFechasMaximaMinimaTurnosCategoria(con, codigoCategoria);
	}

	/**
	 * Consulta los turnos que tiene una enfermera entre dos fechas dadas
	 */
	public Collection<HashMap<String, Object>> consultarTurnosEnfermera(Connection con, String fechaInicial, String fechaFinal, int codigoEnfermera)
	{
		return SqlBaseCuadroTurnosDao.consultarTurnosPersonaOCategoria(con, fechaInicial, fechaFinal, codigoEnfermera, false);
	}
	
	/**
	 * M�todo que consulta los turnos de una categoria entre dos fechas dadas
	 */
	public Collection<HashMap<String, Object>> consultarTurnosCategoria(Connection con, String fechaInicial, String fechaFinal, int codigoCategoria)
	{
		return SqlBaseCuadroTurnosDao.consultarTurnosPersonaOCategoria(con, fechaInicial, fechaFinal, codigoCategoria, true);
	}

	/**
	 * M�todo que Actualiza un turno ya sea con una observacion o cambio de turno o ambos
	 */
	public int actualizarTurnoObservacion(Connection con, int ctcodigo, int codigoTurno, String observacion)
	{
		
		return SqlBaseCuadroTurnosDao.actualizarTurnoObservacion(con, ctcodigo, codigoTurno, observacion);
	}
	
	
	/**
	 * M�todo que consulta lo datos para generar el reporte
	 */
	public Collection consultarTurnosReporte(Connection con, int codigoCategoria, String fechaInicio, String fechaFin, int tipoVinculacion)
	{
		
		return SqlBaseCuadroTurnosDao.consultarTurnosReporte(con, codigoCategoria, fechaInicio, fechaFin, tipoVinculacion);
	}
	
	/**
	 * Metodo para consultar las personas que poseen alguna observacionen su turno para ser mostradas en el reporte de observaciones
	 */
	public Collection<HashMap<String, Object>> consultarTurnosReporteObPersona(Connection con, int codigoCategoria,String fechaInicio, String fechaFin)
	{
		return SqlBaseCuadroTurnosDao.consultarTurnosReporteObPersona(con, codigoCategoria, fechaInicio, fechaFin);
	}
	
	/**
	 * M�todo para consultar los turnos correspondientes al reporte de Observaciones 
	 */
	public Collection<HashMap<String, Object>> consultarTurnosReporteObs(Connection con, String fechaInicio, String fechaFin)
	{
		return SqlBaseCuadroTurnosDao.consultarTurnosReporteObs(con, fechaInicio, fechaFin);
	}
	
	/**
	 * M�todo que busca las enfermeras que pertenec�an a un cuadro en una fecha espec�fica
	 * @param con
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarPersonasCuadro(Connection con, int codigoCategoria, String fechaInicio, String fechaFin)
	{
		return SqlBaseCuadroTurnosDao.consultarPersonasCuadro(con, codigoCategoria, fechaInicio, fechaFin);
	}

	/**
	 * (Juan David)
	 * M�todo que busca las personas que pertenec�an a un cuadro en una fecha espec�fica
	 * @param con
	 * @param codigoCuadro
	 * @param fechaInicio
	 * @param fechaFin
	 * @return Listado de personas
	 */
	public Collection<HashMap<String, Object>> consultarPersonasCuadroPorCodigoCuadro(Connection con, int codigoCuadro, String fechaInicio, String fechaFin)
	{
		return SqlBaseCuadroTurnosDao.consultarPersonasCuadroPorCodigoCuadro(con, codigoCuadro, fechaInicio, fechaFin);
	}

	/**
	 * M�todo que consulta las observaciones de un turno espec�fico
	 * @param con
	 * @param codigoTurno
	 * @return Collection con las observaciones descritas
	 */
	public Collection<HashMap<String, Object>> consultarObservacionesTurno(Connection con, int codigoTurno)
	{
		return SqlBaseCuadroTurnosDao.consultarObservacionesTurno(con, codigoTurno);
	}
	
	/**
	 * M�todo que consulta el listado de los tipos de observaci�nexistentes en la BD
	 * @param con
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarTiposObservacion(Connection con)
	{
		return SqlBaseCuadroTurnosDao.consultarTiposObservacion(con);
	}

	/**
	 * M�todo que actualiza las observaciones de un turno
	 * @param con
	 * @param observacion
	 * @param tipoObservacion
	 * @param codigoTurnoObservacion
	 * @param novedad
	 * @return true si se ingres�, false de lo contrario
	 */
	public boolean ingresarActualizarObservacion(Connection con, String observacion, int tipoObservacion, int codigoTurnoObservacion, int novedad, String usuario)
	{
		return SqlBaseCuadroTurnosDao.ingresarActualizarObservacion(con, observacion, tipoObservacion, codigoTurnoObservacion, novedad, usuario);
	}

	/**
	 * M�todo para consultar el listado de personas con el que se debe generar el cuadro de turnos
	 * @param con
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarEnfermeraCategoria(Connection con, int codigoCategoria, String fechaInicio, String fechaFin)
	{
		return SqlBaseCuadroTurnosDao.consultarEnfermeraCategoria(con, codigoCategoria, fechaInicio, fechaFin);
	}

	/**
	 * M�todo para consultar las restricciones de los cuadros de turnos generados
	 * entre las fechas pasadas por par�metros y la categor�a seleccionada
	 * @param con
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoCategoria
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarRestriccionesCuadro(Connection con, String fechaInicio, String fechaFin, int codigoCategoria)
	{
		return SqlBaseCuadroTurnosDao.consultarRestriccionesCuadro(con, fechaInicio, fechaFin, codigoCategoria);
	}

	/**
	 * M�todo para Consultar el codigo del turno relacionado al cuadro de turnos
	 * @param con
	 * @param codigoCT
	 * @param fecha
	 * @param codigoPersona
	 */
	public int consultarCTCodigoTurno(Connection con, int codigoCT, String fecha, int codigoPersona)
	{
		return SqlBaseCuadroTurnosDao.consultarCTCodigoTurno(con, codigoCT, fecha, codigoPersona);
	}
	
	/**
	 * M�todo que lista los cuadros de turnos que contengan la
	 * fecha pasada por par�metros
	 * @param con
	 * @param fecha
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarCuadroTurnosDadaUnaFecha(Connection con, String fecha, Integer centroCosto)
	{
		return SqlBaseCuadroTurnosDao.consultarCuadroTurnosDadaUnaFecha(con, fecha, centroCosto);
	}

	/**
	 * M�todo que lista los cuadros de turnos que pueden ser
	 * eliminados pasada una fecha
	 * @param con
	 * @param fecha
	 * @return Listado de cuadro de turnos
	 */
	public Collection<HashMap<String, Object>> consultarCuadroTurnosEliminar(Connection con, String fecha, Integer centroCosto)
	{
		return SqlBaseCuadroTurnosDao.consultarCuadroTurnosEliminar(con, fecha, centroCosto);
	}
	
	/**
	 * M�todo que permite eliminar un cuadro de turnoas dado su c�digo
	 * Elimina todos los turnos y observaciones que est�n asociados a �l
	 * @param con
	 * @param codigoCuadroTurnos
	 * @return int
	 */
	public int eliminar(Connection con, int codigoCuadroTurnos)
	{
		return SqlBaseCuadroTurnosDao.eliminar(con, codigoCuadroTurnos);
	}

	/**
	 * M�todo que me consulta estrictamente un turno de una persona
	 * @param con
	 * @param codigoCategoria
	 * @param turnos C�digos de los turnos a Consultar
	 * @return Los datos del turno
	 */
	public Collection<HashMap<String, Object>> consultarTurnoPersona(Connection con, int codigoCategoria, String turnos)
	{
		return SqlBaseCuadroTurnosDao.consultarTurnoPersona(con, codigoCategoria, turnos);
	}

	/**
	 * M�todo para eliminar un turno espec�fico para una persona en una fecha
	 * @param con
	 * @param codigoPersonaEliminar
	 * @param fecha
	 * @return Retorna 0 en caso de error, en caso de �xito retorna el n�mero de elementos borrados de la base de datos
	 */
	public int eliminarTurno(Connection con, int codigoPersonaEliminar, String fecha)
	{
		return SqlBaseCuadroTurnosDao.eliminarTurno(con, codigoPersonaEliminar, fecha);
	}

	@Override
	public int ingresarHorasTurno(Connection con, int ctTurno, double numeroHoras)
	{
		return SqlBaseCuadroTurnosDao.ingresarHorasTurno(con, ctTurno, numeroHoras);
	}

	@Override
	public int guardarHorasFaltantes(Connection con, int codigoEnfermera, double numeroHorasFaltantes, double numeroHorasFaltantesAnteriores)
	{
		return SqlBaseCuadroTurnosDao.guardarHorasFaltantes(con, codigoEnfermera, numeroHorasFaltantes, numeroHorasFaltantesAnteriores);
	}

	@Override
	public int modificarHorasTurno(Connection con, int codigoTurno, double numeroHoras, String usuario)
	{
		return SqlBaseCuadroTurnosDao.modificarHorasTurno(con, codigoTurno, numeroHoras, usuario);
	}

	@Override
	public int eliminarHorasTurno(Connection con, int codigoTurno)
	{
		return SqlBaseCuadroTurnosDao.eliminarHorasTurno(con, codigoTurno);
	}

}
