/*
 * @(#)ClasificacionSocioEconomica.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.cargos;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;

import util.InfoDatos;
import util.UtilidadBD;

import com.princetonsa.dao.ClasificacionSocioEconomicaDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Mundo para el manejo de las clasificaciones sociales
 * @version 1.0, Junio 30, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */

public class ClasificacionSocioEconomica
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ClasificacionSocioEconomicaDao clasificacionDao;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ClasificacionSocioEconomica.class);
	
	/**
	 * Código del estrato
	 */
	private int codigo;

	/** 
	 * Descripcion de la clasificación socioEconómica 
	 */
	private String descripcion;

	/**
	 * Campo para capturar el acronimo y nombre del 
	 *  tipo de régimen previamente creado en el sistema
	 */
	private InfoDatos tipoRegimen;


	/**
	 * Dice si la clasificación socioEconómica está activa 
	 * en el sistema o no.
	 */		
	private boolean activa;

	/**
	 * Auxiliar del campo boolean activa, para poder mandar
	 * un nuevo valor diferente de true o false en la búsqueda
	 * avanzada
	 */
	private int activaAux;
	
	/**
	 * Código de la institución a la que pertenece el usuario
	 */
	private int institucion;


	public ClasificacionSocioEconomica(	int codigo,	
																	String descripcion,
																	boolean activa)
	{
		this.codigo=codigo;
		this.descripcion=descripcion;
		this.tipoRegimen= new InfoDatos();
		this.activa=activa;	
		this.init (System.getProperty("TIPOBD"));																	
	}

	/**
	 * Constructor de la clase, inicializa en vacio todos los parámetros
	 */		
	public ClasificacionSocioEconomica()
	{
		reset();
		this.init (System.getProperty("TIPOBD"));
	}

	/**
	 * resetea los datos pertinentes 
	 */
	public void reset()
	{
		this.codigo=0;
		this.descripcion="";
		this.tipoRegimen= new InfoDatos();
		this.activa= false;
		this.activaAux=0;		
	}	

	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
			boolean wasInited = false;
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
	
			if (myFactory != null)
			{
				clasificacionDao = myFactory.getClasificacionSocioEconomicaDao();
				wasInited = (clasificacionDao != null);
			}
			return wasInited;
	}

	/**
	 * Método para insertar una clasificacion socioeconomica
	 * @param con una conexion abierta con una fuente de datos
	 * @return número de filas insertadas (1 o 0)
	 * @throws SQLException
	 */
	public int insertarClasificacion(Connection con, int institucion) throws SQLException 
	{
		int  resp1=0;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if (clasificacionDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (ClasificacionSocioEconomicaDao - insertarClasificacion... )");
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);

		resp1=clasificacionDao.insertar(con,descripcion,tipoRegimen.getAcronimo(),activa,institucion);
	
		if (!inicioTrans||resp1<1  )
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
	}

	/**
	 * Método para cargar los datos pertinentes al resumen
	 * @param con conexión
	 * @param  codigo calsificacion 
	 * @return
	 * @throws SQLException
	 */
	public boolean cargarResumen(Connection con, int codigo) throws SQLException
	{
		ResultSetDecorator rs=clasificacionDao.cargarResumen(con,codigo);
		
		if (rs.next())
		{
			this.codigo=rs.getInt("codigo");
			this.descripcion=rs.getString("descripcion");
			this.tipoRegimen= new InfoDatos(rs.getString("acronimoTipoRegimen"), rs.getString("nombreTipoRegimen"));
			this.activa= rs.getBoolean("activa");
			
			return true;
		}
		else
			return false;
	}

	public boolean cargarUltimoCodigo(Connection con)
	{
		try
		{
			ResultSetDecorator rs=clasificacionDao.cargarUltimoCodigo(con);
			if(rs.next())
			{
				this.codigo=rs.getInt("codigo");
				return true;
			}
			else
			{
				return false;
			}
		}catch(SQLException e)
		{
			logger.warn("Error mundo clasificacion" +e.toString());	
			return false;
		}
	}	

	/**
	 * Método utilizado en la funcionalidad Modificar estrato
	 * que tiene como finalidad modificar el estado activo y la descripcion
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int modificarClasificacion(Connection con) throws SQLException 
	{
			int resp=0;
			resp=clasificacionDao.modificar(con,this.getCodigo(),this.getDescripcion(),this.getAcronimoTipoRegimen(),this.getActiva());
			return resp;
	}

	/**
	 * Actualiza los datos del objeto en una fuente de datos, reutilizando una conexión existente,
	 * con la información presente en los atributos del objeto.
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param estado estado de la transaccion (empezar, continuar, finalizar)
	 * @return número de filas insertadas (1 o 0)
	 */
	public int modificarClasificacionTransaccional (Connection con, String estado) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0;
		if (estado==null)
		{
		    myFactory.abortTransaction(con);
			throw new SQLException ("El estado de la transacción (ClasificacionSocioEco.. - modificarClasificacionTransaccional ) no esta especificado");
		}
		if (clasificacionDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (clasificacionDao - modificarClasificacionTransaccional )");
		}
				//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		if (estado.equals("empezar"))
		{
			inicioTrans=myFactory.beginTransaction(con);
		}
		else
		{
			inicioTrans=true;
		}
		resp1=clasificacionDao.modificar(con,this.getCodigo(),this.getDescripcion(),this.getAcronimoTipoRegimen(),this.getActiva());
		if (!inicioTrans||resp1<1)
		{
		    myFactory.abortTransaction(con);
		}
		else
		{
			if (estado.equals("finalizar"))
			{
			    myFactory.endTransaction(con);
			}
		}
		return resp1;
	}

	/**
	 * Método que obtiene todos los resultados de la tabla estratos_sociales
	 * para mostrarlos en el listado
	 * @param con
	 * @param , int, institucion, Codigo de la institucion a la que pertenece el usuario para filtrar la consulta
	 * @return
	 */
	public Collection listadoClasificacion(Connection con, int institucion)
	{
		ClasificacionSocioEconomicaDao consulta= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getClasificacionSocioEconomicaDao();
		Collection coleccion=null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(consulta.listado(con,institucion));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo estrato " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}

	/**
	 * Método que contiene los resultados de la búsqueda de los estratos,
	 * según los criterios dados en la búsqueda avanzada. 
	 * @param con
	 * @param  int, codigo de la institucion para filtrar la busqueda de acuerdo al usuario logueado
	 * @return
	 */
	public Collection resultadoBusquedaAvanzada(Connection con, int institucion)
	{
		ClasificacionSocioEconomicaDao consulta= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getClasificacionSocioEconomicaDao();
		Collection coleccion=null;
		try
		{	
				coleccion=UtilidadBD.resultSet2Collection(consulta.busqueda(	con,
																														codigo,
																														descripcion,
																														tipoRegimen.getNombre(),
																														activaAux,
																														institucion));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo estrato " +e.toString());
			coleccion=null;
		}
		return coleccion;		
	}



	/**
	 * Retorna activo/inactivo 
	 * @return
	 */
	public boolean getActiva() {
		return activa;
	}

	/**
	 * Asigna activo/inactivo 
	 * @param b
	 */
	public void setActiva(boolean b) {
		activa = b;
	}

	/**
	 * Returns the tipoRegimen
	 * @return
	 */
	public InfoDatos getTipoRegimen() {
		return tipoRegimen;
	}

	/**
	 * Retorna el acrónimo del tipo de regimen
	 * @return
	 */
	public String getAcronimoTipoRegimen(){
		return tipoRegimen.getAcronimo();
	}

	/**
	 * Retorna el nombre del tipo de regimen
	 * @return
	 */
	public String  getNombreTipoRegimen(){
		return tipoRegimen.getNombre();
	}

	/**
	 * Sets the tipoRegimen
	 * @param s1
	 */
	public void setTipoRegimen(InfoDatos s1) {
		tipoRegimen = s1;
	}
	
	/**
	 * Asigna el acronimo del tipo de regimen
	 * @param acronimoTipoRegimen
	*/
	public void setAcronimoTipoRegimen(String acronimoTipoRegimen){
		this.tipoRegimen.setAcronimo(acronimoTipoRegimen);
	}

	/**
	 * Retorna el Auxiliar del campo boolean activa, para poder mandar
	 * un nuevo valor diferente de true o false en la búsqueda
	 * avanzada
	 */
	public int getActivaAux() {
		return activaAux;
	}

	/**
	 * Asigna el Auxiliar del campo boolean activa, para poder mandar
	 * un nuevo valor diferente de true o false en la búsqueda
	 * avanzada
	 */
	public void setActivaAux(int i) {
		activaAux = i;
	}

	/**
	 * Retorna la descripción del estrato
	 * @return
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Asigna la descripcion del estrato
	 * @param string
	 */
	public void setDescripcion(String string) {
		descripcion = string;
	}

	/**
	 * Retorna el código del estrato
	 * @return
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * Asigna el código del estrato
	 * @param i
	 */
	public void setCodigo(int i) {
		codigo = i;
	}


	/**
	 * @return Returns the institucion.
	 */
	public int getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
}
