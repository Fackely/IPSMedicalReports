/*
 * Creado en  27/06/2005
 *
 */
package com.sies.mundo;

import java.sql.Connection;
import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import util.UtilidadFecha;
import util.UtilidadTexto;

import com.sies.dao.CuadroTurnosDao;
import com.sies.dao.SiEsFactory;

/**
 * @author Karenth Yuliana Marín
 * 
 * Creado en 27/06/2005
 * 
 * SiEs Parquesoft Manizales
 */
public class CuadroTurnos
{
	/**
	 * @return Returns the codigoCtTurno.
	 */
	public int getCodigoCtTurno()
	{
		return codigoCtTurno;
	}

	/**
	 * Variable de Clase para la conexión con el DAO
	 */
	private static CuadroTurnosDao cuadroTurnosDao;

	static
	{
		if (cuadroTurnosDao == null)
		{
			cuadroTurnosDao = SiEsFactory.getDaoFactory().getCuadroTurnosDao();
		}
	}

	public CuadroTurnos()
	{
		clean();
	}

	public void clean()
	{
		codigoTurno = 0;
		codigoCT = 0;
		simboloTurno = "";
		codigoEnfermera = 0;
		codigoCategoria = 0;
		fechaInicio = "";
		fechaFin = "";
		observacion = "";
		fechaPr = "";
	}

	/**
	 * Para manejar el codigo del turno que se va a asignar
	 */
	private int codigoTurno;

	/**
	 * Para manejar el codigo del cuadro de turno
	 */
	private int codigoCT;

	/**
	 * Para manejar el codigo de una asignacion de un turnos a una enfermera en
	 * una fecha especifica
	 */
	private int codigoCtTurno;

	/**
	 * Para manejar el simbolo del turno
	 */
	private String simboloTurno;

	/**
	 * Para manejar el codigo de la enfermera
	 */
	private int codigoEnfermera;

	/**
	 * para manejar el codigo de la categoria
	 */
	private int codigoCategoria;

	/**
	 * Para manejar la fecha de inicio de la programacion del cuadro
	 */
	private String fechaInicio;

	/**
	 * Para manejar la fecha de finalizacion de la programación
	 */
	private String fechaFin;

	/**
	 * Manipula el nombre del tipo de observacion
	 */
	private String nombreObservacion;
	
	/**
	 * observacion que se hace al cuadro de turnos
	 */
	private String observacion;

	/**
	 * Fecha que se le esta programando a la enfermera
	 */
	private String fechaPr;

	/**
	 * @return Returns the fechaPr.
	 */
	public String getFechaPr()
	{
		return fechaPr;
	}

	/**
	 * @param fechaPr
	 *            The fechaPr to set.
	 */
	public void setFechaPr(String fechaPr)
	{
		this.fechaPr = fechaPr;
	}

	/**
	 * @return Returns the codigoTurno.
	 */
	public int getCodigoTurno()
	{
		return codigoTurno;
	}

	/**
	 * @param codigoTurno
	 *            The codigoTurno to set.
	 */
	public void setCodigoTurno(int codigoTurno)
	{
		this.codigoTurno = codigoTurno;
	}

	/**
	 * @return Returns the codigoCT.
	 */
	public int getCodigoCT()
	{
		return codigoCT;
	}

	/**
	 * @param codigoCT
	 *            The codigoCT to set.
	 */
	public void setCodigoCT(int codigoCT)
	{
		this.codigoCT = codigoCT;
	}

	/**
	 * @return Returns the codigoCategoria.
	 */
	public int getCodigoCategoria()
	{
		return codigoCategoria;
	}

	/**
	 * @param codigoCategoria
	 *            The codigoCategoria to set.
	 */
	public void setCodigoCategoria(int codigoCategoria)
	{
		this.codigoCategoria = codigoCategoria;
	}

	/**
	 * @return Returns the codigoEnfermera.
	 */
	public int getCodigoEnfermera()
	{
		return codigoEnfermera;
	}

	/**
	 * @param codigoEnfermera
	 *            The codigoEnfermera to set.
	 */
	public void setCodigoEnfermera(int codigoEnfermera)
	{
		this.codigoEnfermera = codigoEnfermera;
	}

	/**
	 * @return Returns the simboloTurno.
	 */
	public String getSimboloTurno()
	{
		return simboloTurno;
	}

	/**
	 * @param simboloTurno
	 *            The simboloTurno to set.
	 */
	public void setSimboloTurno(String simboloTurno)
	{
		this.simboloTurno = simboloTurno;
	}

	/**
	 * @return Returns the fechaFin.
	 */
	public String getFechaFin()
	{
		return fechaFin;
	}

	/**
	 * @param fechaFin
	 *            The fechaFin to set.
	 */
	public void setFechaFin(String fechaFin)
	{
		this.fechaFin = fechaFin;
	}

	/**
	 * @return Returns the fechaIncio.
	 */
	public String getFechaInicio()
	{
		return fechaInicio;
	}

	/**
	 * @param fechaIncio
	 *            The fechaIncio to set.
	 */
	public void setFechaInicio(String fechaInicio)
	{
		this.fechaInicio = fechaInicio;
	}

	public String getNombreObservacion()
	{
		return nombreObservacion;
	}

	public void setNombreObservacion(String nombreObservacion) 
	{
		this.nombreObservacion = nombreObservacion;
	}
	
	/**
	 * @return Returns the observacion.
	 */
	public String getObservacion()
	{
		return observacion;
	}

	/**
	 * @param observacion
	 *            The observacion to set.
	 */
	public void setObservacion(String observacion)
	{
		this.observacion = observacion;
	}

	/**
	 * Metodo que retorna la fecha de finalización de la última programación de
	 * cuadro de turnos realizada a esa categoria
	 * 
	 * @param con
	 * @param codigoCategoria
	 * @return
	 */
	public String consultarFechaUltimaProg(Connection con, int codigoCategoria)
	{
		Iterator iterador = cuadroTurnosDao.consultarFechaUltimaProg(con,
				codigoCategoria).iterator();
		if (iterador.hasNext())
		{
			HashMap<String, Object> fila = (HashMap<String, Object>) iterador.next();
			Date fechaBd = (Date) fila.get("fechafin");
			//System.out.println("Fecha bd:" + fechaBd);
			if (fechaBd != null)
				return fechaBd.toString();
			else
				return null;
		}
		return null;
	}

	/**
	 * Metodo que retorna la fecha de finalización del ultimo cuadro programado
	 * para esa enfermera
	 * 
	 * @param con
	 * @param codigoEnfermera
	 * @return
	 */
	public String consulFechaUltEnf(Connection con, int codigoEnfermera)
	{
		Iterator<HashMap<String, Object>> iterador = cuadroTurnosDao.consulFechaUltEnf(con,
				codigoEnfermera).iterator();
		if (iterador.hasNext())
		{
			HashMap<String, Object> fila = iterador.next();
			Date fechaBd=null;
			if(!UtilidadTexto.isEmpty(fila.get("fechafin")+""))
				fechaBd = (Date) fila.get("fechafin");
			//System.out.println("Fecha bd:" + fechaBd);
			if (fechaBd != null)
				return fechaBd.toString();
			else
				return null;
		}
		return null;
	}

	/**
	 * Metodo que consulta los ultimos dos turnos que tiene asignada una
	 * enfermera
	 * 
	 * @param con
	 * @param codigoEnfermera
	 * @param limiteTurnos Cuantos turnos anteriores
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarUltimosTurnos(Connection con, int codigoEnfermera, int limiteTurnos)
	{
		return cuadroTurnosDao.consultarUltimosTurnos(con, codigoEnfermera, limiteTurnos);
	}

	/**
	 * Método que retorna todos los datos de todos los turnos posibles en el
	 * cuadro de turnos
	 * @param con
	 * @param activos True para indicar que liste únicamente los turnos activos
	 * @return Listado de turnos existentes en BD
	 */
	public Collection<HashMap<String, Object>> consultarTurnos(Connection con, boolean activos)
	{
		return cuadroTurnosDao.consultarTurnos(con, activos);
	}

	/**
	 * Método que retorna todos los datos de todos los turnos posibles en el
	 * cuadro de turnos
	 * @param con
	 * @param activos True para indicar que liste únicamente los turnos activos
	 * @param centroCosto Código del centro de costo por el cual se desea filtrar
	 * @return Listado de turnos existentes en BD
	 */
	public Collection<HashMap<String, Object>> consultarTurnos(Connection con, boolean activos, Integer centroCosto)
	{
		return cuadroTurnosDao.consultarTurnos(con, activos, centroCosto);
	}

	/**
	 * Metodo que inserta en la base de datos un nuevo cuadro de turnos
	 * @param con
	 * @param restriccionesCategoria
	 * @return
	 */
	public int insertarCuadro(Connection con, HashMap<String, Object> restriccionesCategoria)
	{
		codigoCT=cuadroTurnosDao.insertarCuadro(con, fechaInicio, fechaFin, codigoCategoria, restriccionesCategoria);
		return codigoCT;
	}

	/**
	 * Metodo que permite insertar un nuevo turno en la base de datos
	 * @param con
	 * @param codigoCT
	 * @param fecha
	 * @param codigoEnfermera
	 * @param codigoTurno
	 * @return
	 */
	public int insertarTurno(Connection con, int codigoCT, String fecha, int codigoEnfermera, int codigoTurno)
	{
		return cuadroTurnosDao.insertarTurno(con, codigoCT, fecha, codigoEnfermera, codigoTurno);
	}

	/**
	 * Método que inserta un cubrimiento de turno en la base de datos
	 * @param con
	 * @param codigoRegistro
	 * @param codigoCategoria
	 * @param fecha
	 * @param codigoEnfermera
	 * @param codigoTurno
	 * @return Código secuencial del registro ingresado, -1 en caso de error
	 */
	public int insertarCubrimientoTurno(Connection con, int codigoRegistro, int codigoCategoria, String fecha, int codigoEnfermera, int codigoTurno)
	{
		return cuadroTurnosDao.procesarCubrimientoTurno(con, codigoRegistro, codigoCategoria, fecha, codigoEnfermera, codigoTurno, false);
	}

	public int borrarCubrimientoTurno(Connection con, int codigoRegistro)
	{
		return cuadroTurnosDao.procesarCubrimientoTurno(con, codigoRegistro, 0, null, 0, 0, true);
	}

	/**
	 * Metodo que consulta las fecha que ha trabajado una enfermera
	 * 
	 * @param con
	 * @param fecha
	 * @param codigoEnfermera
	 * @return
	 */
	public Collection consultarFechaEnf(Connection con, String fecha,
			int codigoEnfermera)
	{
		return cuadroTurnosDao.consultarFechaEnf(con, fecha, codigoEnfermera);
	}

	/**
	 * Metodo que actualiza un turno de una persona
	 * @param con
	 * @param codigoCT
	 * @param codigoTurno
	 * @param codigoCtTurno
	 * @return
	 */
	public int actualizarTurno(Connection con, int codigoCT, int codigoTurno, int codigoCtTurno)
	{
		return cuadroTurnosDao.actualizarTurno(con, codigoCT, codigoTurno, codigoCtTurno);
	}

	/**
	 * Metodo que guarda los turnos de las enfermeras
	 * 
	 * @param con
	 * @param codigoTurno Códigodel turno que se desea insertar
	 * @param codigoEnfermera Codigo de la enfermera a la cual se le va a ingresar el turno
	 * @param fecha
	 * @return
	 */
	public int guardarTurno(Connection con, int codigoTurno, int codigoEnfermera, String fecha)
	{
		Collection enfermeraFecha;

		enfermeraFecha = consultarFechaEnf(con, fecha, codigoEnfermera);

		if (enfermeraFecha.size() == 0)
		{
			return insertarTurno(con, codigoCT, fecha, codigoEnfermera, codigoTurno);
		}
		else
		{
			Iterator iterador = enfermeraFecha.iterator();

			while (iterador.hasNext())
			{
				HashMap<String, Object> fila = (HashMap<String, Object>) iterador.next();
				this.codigoCtTurno = ((Integer) fila.get("codigo")).intValue();
			}
			return actualizarTurno(con, codigoCT, codigoTurno, codigoCtTurno);
		}

	}

	/**
	 * (Juan David)
	 * Metodo que consulta el codigo de cuadro de turnos para una
	 * categoría y una fecha específica
	 * @param con
	 * @param codigoCategoria
	 * @param fecha
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarCodigoCuadro(Connection con, int codigoCategoria, String fecha)
	{
		return cuadroTurnosDao.consultarCodigoCuadro(con, codigoCategoria, fecha);
	}

	/**
	 * Metodo que consulta toda la inf referente a un cuadro de turnos
	 * especifico
	 * 
	 * @param con
	 * @param codigocuadro
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarTurnosEnfermeras(Connection con, int codigocuadro)
	{
		return cuadroTurnosDao.consultarTurnosEnfermeras(con, codigocuadro);
	}

	/**
	 * Metodo que consulta los turnos de una enfermera entre dos fechas
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoEnfermera
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarTurnosEnfermera(Connection con, String fechaInicial, String fechaFinal, int codigoEnfermera)
	{
		return cuadroTurnosDao.consultarTurnosEnfermera(con,
				UtilidadFecha.conversionFormatoFechaABD(fechaInicial),
				UtilidadFecha.conversionFormatoFechaABD(fechaFinal), codigoEnfermera);
	}

	/**
	 * Metodo que consulta la fecha maxima y minima de los turnos de una
	 * enfermera
	 * 
	 * @param con
	 * @param codigoEnfermera
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarFechasMaximaMinimaTurnosEnfermera(
			Connection con, int codigoEnfermera)
	{
		return cuadroTurnosDao.consultarFechasMaximaMinimaTurnosEnfermera(con,
				codigoEnfermera);
	}

	/**
	 * Metodo que consulta la fecha maxima y minima de los turnos de una
	 * categoria
	 * 
	 * @param con
	 * @param codigoEnfermera
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarFechasMaximaMinimaTurnosCategoria(
			Connection con, int codigoCategoria)
	{
		return cuadroTurnosDao.consultarFechasMaximaMinimaTurnosCategoria(con,
				codigoCategoria);
	}

	/**
	 * Método que consulta los turnos de una categoria entre dos fechas
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoCategoria
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarTurnosCategoria(Connection con,
			String fechaInicial, String fechaFinal, int codigoCategoria)
	{
		return cuadroTurnosDao.consultarTurnosCategoria(con, UtilidadFecha
				.conversionFormatoFechaABD(fechaInicial), UtilidadFecha
				.conversionFormatoFechaABD(fechaFinal), codigoCategoria);
	}

	/**
	 * Método que consulta lo datos para generar el reporte
	 * @param con
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @param tipoVinculacion
	 * @return
	 */
	public Collection consultarTurnosReporte(Connection con, int codigoCategoria, String fechaInicio, String fechaFin, int tipoVinculacion)
	{
		return cuadroTurnosDao.consultarTurnosReporte(con, codigoCategoria, fechaInicio, fechaFin, tipoVinculacion);
	}

	/**
	 * Andrés Arias López
	 * Metodo para consultar las observaciones que posee una persona y ser mostradas en el reporte de observaciones
	 */
	public Collection<HashMap<String, Object>> consultarTurnosReporteObPersona(Connection con, int codigoCategoria, String fechaInicio, String fechaFin)
	{
		return cuadroTurnosDao.consultarTurnosReporteObPersona(con, codigoCategoria, fechaInicio, fechaFin);
	}
	
	/**
	 * (Juan David)
	 * Método para consultar los turnos correspondientes al reporte de
	 * Observaciones
	 * @param con
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarTurnosReporteObs(Connection con,String fechaInicio, String fechaFin)
	{
		return cuadroTurnosDao.consultarTurnosReporteObs(con, fechaInicio, fechaFin);
	}

	/**
	 * Método que busca las personas que pertenecían a un cuadro en una fecha específica
	 * @param con
	 * @param codigoCuadro
	 * @param fechaInicio
	 * @param fechaFin
	 * @return Listado de personas que pertenecen al cuadro de turnos de acuerdo a la
	 * categoría y a las fechas pasadas por parámetro
	 */
	public Collection<HashMap<String, Object>> consultarPersonasCuadro(Connection con, int codigoCategoria, String fechaInicio, String fechaFin)
	{
		return cuadroTurnosDao.consultarPersonasCuadro(con, codigoCategoria, fechaInicio, fechaFin);
	}

	/**
	 * (Juan David)
	 * Método que busca las personas que pertenecían a un cuadro en una fecha específica
	 * @param con
	 * @param codigoCuadro
	 * @param fechaInicio
	 * @param fechaFin
	 * @return Listado de personas
	 */
	public Collection<HashMap<String, Object>> consultarPersonasCuadroPorCodigoCuadro(Connection con, int codigoCuadro, String fechaInicio, String fechaFin)
	{
		return cuadroTurnosDao.consultarPersonasCuadroPorCodigoCuadro(con, codigoCuadro, fechaInicio, fechaFin);
	}

	/**
	 * Método que consulta las observaciones de un turno específico
	 * @param con
	 * @param codigoTurno
	 * @return Collection con las observaciones descritas
	 */
	public Collection<HashMap<String, Object>> consultarObservacionesTurno(Connection con, int codigoTurno)
	{
		return cuadroTurnosDao.consultarObservacionesTurno(con, codigoTurno);
	}

	/**
	 * Método que consulta el listado de los tipos de observaciónexistentes en la BD
	 * @param con
	 * @return Collection con el listado de los tipos de observación
	 */
	public Collection<HashMap<String, Object>> consultarTiposObservacion(Connection con)
	{
		return cuadroTurnosDao.consultarTiposObservacion(con);
	}

	/**
	 * Método que actualiza las observaciones de un turno
	 * @param con
	 * @param observacion
	 * @param tipoObservacion
	 * @param codigoTurnoObservacion
	 * @param novedad
	 * @param usuario
	 * @return true si se ingresó, false de lo contrario
	 */
	public boolean ingresarActualizarObservacion(Connection con, String observacion, int tipoObservacion, int codigoTurnoObservacion, int novedad, String usuario)
	{
		return cuadroTurnosDao.ingresarActualizarObservacion(con, observacion, tipoObservacion, codigoTurnoObservacion, novedad, usuario);
	}

	/**
	 * Método para consultar el listado de personas con el que se debe generar el cuadro de turnos
	 * @param con
	 * @param codigoCategoria
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarEnfermeraCategoria(Connection con, int codigoCategoria, String fechaInicio, String fechaFin)
	{
		return cuadroTurnosDao.consultarEnfermeraCategoria(con, codigoCategoria, fechaInicio, fechaFin);
	}

	/**
	 * Método para consultar las restricciones de los cuadros de turnos generados
	 * entre las fechas pasadas por parámetros y la categoría seleccionada
	 * @param con
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoCategoria
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarRestriccionesCuadro(Connection con, String fechaInicio, String fechaFin, int codigoCategoria)
	{
		return cuadroTurnosDao.consultarRestriccionesCuadro(con, fechaInicio, fechaFin, codigoCategoria);
	}

	public int consultarCTCodigoTurno(Connection con, int codigoCT, String fecha, int codigoPersona)
	{
		return cuadroTurnosDao.consultarCTCodigoTurno(con, codigoCT, fecha, codigoPersona);
	}
	
	/**
	 * Método que lista los cuadros de turnos que contengan la
	 * fecha pasada por parámetros
	 * @param con
	 * @param fecha
	 * @param centroCosto Centrode Costo del usuario
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarCuadroTurnosDadaUnaFecha(Connection con, String fecha, Integer centroCosto)
	{
		return cuadroTurnosDao.consultarCuadroTurnosDadaUnaFecha(con, fecha, centroCosto);
	}

	/**
	 * Método que lista los cuadros de turnos que pueden ser
	 * eliminados pasada una fecha
	 * @param con
	 * @param fecha
	 * @param centroCosto Centro de costo del usuario
	 * @return Listado de cuadro de turnos
	 */
	public Collection<HashMap<String, Object>> consultarCuadroTurnosEliminar(Connection con, String fecha, Integer centroCosto)
	{
		return cuadroTurnosDao.consultarCuadroTurnosEliminar(con, fecha, centroCosto);
	}
	/**
	 * Método que permite eliminar un cuadro de turnoas dado su código
	 * Elimina todos los turnos y observaciones que estén asociados a él
	 * @param con
	 * @param codigoCuadroTurnos
	 */
	public int eliminar(Connection con, int codigoCuadroTurnos)
	{
		return cuadroTurnosDao.eliminar(con, codigoCuadroTurnos);
	}
	
	/**
	 * Método que me consulta estrictamente un turno de una persona
	 * @param con
	 * @param codigoCategoria
	 * @param turnos Códigos de los turnos a Consultar
	 * @return Los datos del turno
	 */
	public Collection<HashMap<String, Object>> consultarTurnoPersona(Connection con, int codigoCategoria, String turnos)
	{
		return cuadroTurnosDao.consultarTurnoPersona(con, codigoCategoria, turnos);
	}

	/**
	 * Método para eliminar un turno específico para una persona en una fecha
	 * @param con
	 * @param codigoPersonaEliminar
	 * @param fecha
	 * @return Retorna 0 en caso de error, en caso de éxito retorna el número de elementos borrados de la base de datos
	 */
	public int eliminarTurno(Connection con, int codigoPersonaEliminar, String fecha)
	{
		return cuadroTurnosDao.eliminarTurno(con, codigoPersonaEliminar, fecha);
	}

	/**
	 * Método para almacenar el numero de horas faltantes del mes para cada persona
	 * @param con
	 * @param codigoEnfermera
	 * @param numeroHorasFaltantes
	 * @param numeroHorasFaltantesAnteriores
	 * @return numero de registros almacenados en BD
	 */
	public int guardarHorasFaltantes(Connection con, int codigoEnfermera, double numeroHorasFaltantes, double numeroHorasFaltantesAnteriores)
	{
		return cuadroTurnosDao.guardarHorasFaltantes(con, codigoEnfermera, numeroHorasFaltantes, numeroHorasFaltantesAnteriores);
	}
	
	/**
	 * Método que almacena las horas adicionales al turno en BD
	 * En caso de que ya existan son adicionadas a las horas existentes
	 * @param con
	 * @param codigoTurno
	 * @param numeroHoras
	 * @param usuario
	 * @return Número de registros insertados o modificados en BD, -1 en caso de error
	 */
	public int modificarHorasTurno(Connection con, int codigoTurno, double numeroHoras, String usuario)
	{
		return cuadroTurnosDao.modificarHorasTurno(con, codigoTurno, numeroHoras, usuario);
	}

	/**
	 * Método que eliminar las horas adicionales de un turno en BD
	 * @param con Conexión con la BD
	 * @param codigoTurno Código del turno que se desea eliminar
	 * @return Número de registros modificados en BD, -1 en caso de error
	 */
	public int eliminarHorasTurno(Connection con, int codigoTurno)
	{
		return cuadroTurnosDao.eliminarHorasTurno(con, codigoTurno);
	}


}
