/*
 * @(#)HistoricoEvoluciones.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.atencion;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.HistoricoEvolucionesDao;

/**
 * Esta clase sirve como agregado de objetos <code>InfoEvolucion</code>.
 * Puede cargar el conjunto de evoluciones asociadas a una misma cuenta,
 * y eventualmente, todas las evoluciones de un paciente. 
 *
 * @version Jun 10, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class HistoricoEvoluciones {

	/**
	 * Lista con las evoluciones.
	 */
	private ArrayList evoluciones;

	/**
	 * DAO para acceder a la fuente de datos.
	 */
	private static HistoricoEvolucionesDao historicoEvolucionesDao;

	/**
	 * Crea un nuevo objeto <code>HistoricoEvoluciones</code>.
	 */
	public HistoricoEvoluciones() {
		this.evoluciones = new ArrayList();
		init(System.getProperty("TIPOBD"));
	}

	/**
	 * Crea un nuevo objeto <code>HistoricoEvoluciones</code>.
	 * @param con una conexi�n abierta con una fuente de datos
	 * @param idCuenta n�mero de identificaci�n de una cuenta
	 */
	public HistoricoEvoluciones(Connection con, int idCuenta) throws SQLException {
		this.evoluciones = new ArrayList();
		init(System.getProperty("TIPOBD"));
		cargar(con, idCuenta);
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores v�lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicializaci�n fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD) {

		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null) {
			historicoEvolucionesDao = myFactory.getHistoricoEvolucionesDao();
			wasInited = (historicoEvolucionesDao != null);
		}

		return wasInited;

	}
	
	/**
	 * M�todo para obtener una instancia del DAO de forma est�tica
	 * @return
	 */
	private static HistoricoEvolucionesDao historicoEvoluciones()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHistoricoEvolucionesDao();
	}
	
	/**
	 * M�todo que verifica si una cuenta tiene evoluciones
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static boolean existeHistoricoEvoluciones(Connection con,int idCuenta)
	{
		return historicoEvoluciones().existeHistoricoEvoluciones(con, idCuenta);
	}

	/**
	 * Retorna la lista con las evoluciones.
	 * @return la lista con las evoluciones
	 */
	public ArrayList getEvoluciones() {
		return evoluciones;
	}

	/**
	 * Agrega un nuevo objeto <code>InfoEvolucion</code> a la lista de evoluciones.
	 * @return <b>true</b> si se pudo agregar una nueva evoluci�n, <b>false</b> si no
	 */
	public boolean add(InfoEvolucion evolucion) {
		return evoluciones.add(evolucion);
	}

	/**
	 * Retorna un objeto <code>InfoEvolucion</code>, dado su �ndice dentro de la lista.
	 * @param i �ndice del objeto que se quiere recuperar
	 * @return el objeto <code>InfoEvolucion</code> solicitado, o null si el �ndice
	 * proporcionado no existe.
	 */
	public InfoEvolucion get(int i) {

		InfoEvolucion info = null;

		try {
			info = (InfoEvolucion) evoluciones.get(i);
		}	catch (IndexOutOfBoundsException e) {}

		finally {
			return info;
		}

	}

	/**
	 * Retorna el n�mero de evoluciones en la lista.
	 * @return el n�mero de evoluciones en la lista
	 */
	public int size() {
		return evoluciones.size();
	}

	/**
	 * Carga un conjunto de evoluciones desde la fuente de datos,
	 * dada la cuenta y la cuenta asociada a la que pertenecen.
	 * 
	 * @param con conexi�n abierta con la fuente de datos
	 * @param idCuenta n�mero de la cuenta cuyas evoluciones se 
	 * desean cargar
	 * @param idCuentaAsociada n�mero de la cuenta y su asociada
	 * cuyas evoluciones se desean cargar
	 * @return
	 */
	public int cargarEvolucionesCuentaYAsocio(Connection con, int idCuenta, int idCuentaAsociada) throws SQLException
	{
		ResultSetDecorator rs = historicoEvolucionesDao.cargarEvolucionesCuentaYAsocio(con, idCuenta, idCuentaAsociada);

		while (rs.next()) {

			add(
				new InfoEvolucion(
					rs.getInt("codigo"),
					rs.getString("fechaEvolucion"),
					rs.getString("horaEvolucion"),
					rs.getString("centroCosto"),
					rs.getString("nombreMedico")
				)
			);

		}

		return size();

	}

	/**
	 * Carga, desde la fuente de datos, un conjunto de evoluciones
	 * asociadas a una misma cuenta.
	 * 
	 * @param con una conexi�n abierta con una fuente de datos
	 * @param idCuenta n�mero de identificaci�n de una cuenta
	 * @return n�mero de evoluciones cargadas
	 */
	public int cargar(Connection con, int idCuenta) throws SQLException {

		ResultSetDecorator rs = historicoEvolucionesDao.cargar(con, idCuenta);

		while (rs.next()) {

			add(
				new InfoEvolucion(
					rs.getInt("codigo"),
					rs.getString("fechaEvolucion"),
					rs.getString("horaEvolucion"),
					rs.getString("centroCosto"),
					rs.getString("nombreMedico")
				)
			);

		}

		return size();

	}


	/**
	 * Carga un conjunto de evoluciones (dado su codigo de tipo, hospitalizacion
	 * urgencias ... definidos en la clase Evolucion)desde la fuente de datos,
	 * dado el paciente al que pertenecen.
	 * 
	 * @param con conexi�n abierta con la fuente de datos
	 * @param codigoTipoIdentificacionPaciente C�digo del tipo de identificaci�n
	 * del paciente al que se quieren sacar las evoluciones
	 * @param numeroIdentificacionPaciente N�mero de identificaci�n
	 * del paciente al que se quieren sacar las evoluciones 
	 * @param codigoTipoEvolucion C�digo del tipo de evoluci�n 
	 * @return n�mero de evoluciones cargadas
	 */
	public int cargar(Connection con, String codigoTipoIdentificacionPaciente, String numeroIdentificacionPaciente, int codigoTipoEvolucion) throws SQLException
	{
		ResultSetDecorator rs = historicoEvolucionesDao.cargar(con, codigoTipoIdentificacionPaciente, numeroIdentificacionPaciente, codigoTipoEvolucion);

		while (rs.next()) 
		{

			add(
				new InfoEvolucion(
					rs.getInt("codigo"),
					rs.getString("fechaEvolucion"),
					rs.getString("horaEvolucion"),
					rs.getString("centroCosto"),
					rs.getString("nombreMedico")
				)
			);

		}

		return size();

	}

	/**
	 * Carga un conjunto de evoluciones desde la fuente de datos,
	 * dado el paciente al que pertenecen.
	 * 
	 * @param con conexi�n abierta con la fuente de datos
	 * @param codigoTipoIdentificacionPaciente C�digo del tipo de identificaci�n
	 * del paciente al que se quieren sacar las evoluciones
	 * @param numeroIdentificacionPaciente N�mero de identificaci�n
	 * del paciente al que se quieren sacar las evoluciones 
	 * @return n�mero de evoluciones cargadas
	 */
	public int cargar(Connection con, String codigoTipoIdentificacionPaciente, String numeroIdentificacionPaciente) throws SQLException
	{
		ResultSetDecorator rs = historicoEvolucionesDao.cargar(con, codigoTipoIdentificacionPaciente, numeroIdentificacionPaciente);

		while (rs.next()) 
		{

			add(
				new InfoEvolucion(
					rs.getInt("codigo"),
					rs.getString("fechaEvolucion"),
					rs.getString("horaEvolucion"),
					rs.getString("centroCosto"),
					rs.getString("nombreMedico")
				)
			);

		}

		return size();

	}


}