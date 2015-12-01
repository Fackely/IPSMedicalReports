/*
 * @(#)Tercero.java
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
import com.princetonsa.dto.administracion.DtoConceptosRetencionTercero;
import com.princetonsa.dto.cargos.DtoTercero;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Cargos.InfoDeudorTerceroDto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TerceroDao;
import com.princetonsa.dao.sqlbase.SqlBaseTerceroDao;

/**
 * Clase para el manejo de terceros
 * @version 1.0, Junio 11, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class Tercero {
	
	/**
	 * DAO utilizado por el objeto parra acceder a la fuente de datos
	 */
	private static TerceroDao terceroDao = null;

	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(Tercero.class);
	
	/** 
	 * codigo del tercero
	 */
	private int codigo;
	
	/** 
	 * numero de identificacion del tercero	 
	 */
	private String numeroIdentificacion;
	
	/** 
	 * Descripcion del tercero 
	 */
	private String descripcion;


	/** 
	 * estado del tercero 
	 */
	private boolean activo;
	
	/**
	 *  codigo de la institucion asociada al Tercero 
	 */
	private int institucion;
	
	/**
	 * Auxiliar del campo boolean activa, para poder mandar
	 * un nuevo valor diferente de true o false en la búsqueda
	 * avanzada
	 */
	private int activaAux;	

	/**
	 * 
	 */
	private int codigoTipoTercero;
	
	/**
	 * 
	 */
	private String digitoVerificacion;
	
	private String direccion;
	
	private String telefono;
	
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
				terceroDao = myFactory.getTerceroDao();
				wasInited = (terceroDao != null);
			}
			return wasInited;
	}

	/**
	 * Constructor de la clase, inicializa los atributos con los datos correspondientes. 
	 * @param codigo
 	 * @param numeroIdentificacion, String,  número de id
	 * @param descripcion, String, descripción
	 * @param institucion, institución según dado el usuarioBasico
	 */
	public Tercero( int codigo,  String numeroIdentificacion, String descripcion,boolean activo)
	{
		this.codigo=codigo;
		this.numeroIdentificacion=numeroIdentificacion;
		this.descripcion=descripcion;
		this.activo=activo;
		this.institucion= -1;
		this.direccion="";
		this.telefono="";
		this.init (System.getProperty("TIPOBD"));
	}

	/**
	 * Constructor de la clase, inicializa en vacio todos los parámetros
	 */	
	public Tercero()
	{
		this.reset();
		this.init (System.getProperty("TIPOBD"));
	}

	/**
	 * resetea los datos pertinentes al registro de terceros
	 */
	public void reset()
	{
		this.codigo=-1;
		this.numeroIdentificacion="";
		this.descripcion="";
		this.activo= false;
		this.institucion= -1;		
		this.activaAux=0;
		this.codigoTipoTercero=ConstantesBD.codigoNuncaValido;
		this.digitoVerificacion="";
		this.direccion="";
		this.telefono="";
	}	

	/**
	 * Método para insertar un tercero
	 * @param con una conexion abierta con una fuente de datos
	 * @return número de filas insertadas (1 o 0)
	 */
	public int insertarTercero(Connection con)  
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int  resp1=0;
		try{
				if (terceroDao==null)
				{
					throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (terceroDao - insertarTercero )");
				}
				//Iniciamos la transacción, si el estado es empezar
				boolean inicioTrans;
				
				//inicioTrans=myFactory.beginTransaction(con);
		
				resp1=terceroDao.insertar(con,numeroIdentificacion,descripcion,activo,institucion, this.codigoTipoTercero, this.digitoVerificacion, direccion,telefono);
			
				/*if (!inicioTrans||resp1<1  )
				{
						myFactory.abortTransaction(con);
						return -1;
				}
				else
				{
						myFactory.endTransaction(con);
				}*/
		}
		catch(SQLException e)
		{
			logger.warn("Error mundo insertarTercero" +e.toString());
			return -1;
		}
		return resp1;
	}

	/**
	 * Método para cargar los datos pertinentes al resumen
	 * @param con conexión
	 * @param  empresa
	 * @return
	 * @throws SQLException
	 */
	public boolean cargarResumen(Connection con, int codigo) 
	{
		ResultSetDecorator rs=terceroDao.cargarResumen(con,codigo);
		
		try 
		{
			if (rs.next())
			{
				this.codigo=rs.getInt("codigo");
				this.numeroIdentificacion=rs.getString("numeroIdentificacion");
				this.descripcion=rs.getString("descripcion");
				this.codigoTipoTercero=rs.getInt("tipo_tercero");
				this.digitoVerificacion=rs.getString("digito_verificacion");
				this.activo=rs.getBoolean("activo");
				this.direccion=rs.getString("direccion");
				this.telefono=rs.getString("telefono");
				
				logger.info("---------CARGAR RESUMEN -------------");
				logger.info("Codigo "+rs.getInt("codigo"));
				logger.info("Numero Id "+rs.getString("numeroIdentificacion"));
				logger.info("Tipo Tercero "+rs.getInt("tipo_tercero"));
				logger.info("Digito Verif "+rs.getString("digito_verificacion"));
				logger.info("Activo "+rs.getBoolean("activo"));
				logger.info("Direccion "+rs.getString("direccion"));
				logger.info("Telefono "+rs.getString("telefono"));
				
				
				return true;
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	
	public boolean cargarUltimoCodigo(Connection con)
	{
		try
		{
			ResultSetDecorator rs=terceroDao.cargarUltimoCodigo(con);
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
			logger.warn("Error mundo tercero" +e.toString());	
			return false;
		}
	}	

	/**
	 * Método utilizado en la funcionalidad Modificar tercero
	 * que tiene como finalidad modificar el numeroIdentificacion, 
	 * tipoIdentificacion, descripcion, tipoRetencion, activo.
	 * @param con
	 * @return
	 */
	public int modificarTercero(Connection con)  
	{
		int resp=0;
		try
		{
			resp=terceroDao.modificar(con,this.getCodigo(),this.getNumeroIdentificacion(),this.getDescripcion(), this.getCodigoTipoTercero(), this.getDigitoVerificacion(), this.getActivo(), this.direccion,this.telefono);
		}
		catch(Exception e)
		{
			logger.warn("Error mundo modificarTercero" +e.toString());
			return -1;
		}
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
	public int modificarTerceroTransaccional (Connection con, String estado) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0;
		if (estado==null)
		{
			myFactory.abortTransaction(con);
			throw new SQLException ("El estado de la transacción (tercero - modificarTerceroTransaccional ) no esta especificado");
		}
		if (terceroDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (terceroDao - modificarTerceroTransaccional )");
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
		resp1=terceroDao.modificar(con,this.getCodigo(),this.getNumeroIdentificacion(),this.getDescripcion(),this.getCodigoTipoTercero(), this.getDigitoVerificacion(),this.getActivo(), this.direccion, this.telefono);
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
	 * Método que obtiene todos los resultados de la tabla terceros
	 * para mostrarlos en el listado
	 * @param con
	 * @return
	 */
	public Collection listadoTercero(Connection con, int codigoInstitucion)
	{
		TerceroDao consulta= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTerceroDao();
		Collection coleccion=null;
		try
		{			
			coleccion=UtilidadBD.resultSet2Collection(consulta.listado(con, codigoInstitucion));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo tercero " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}

	/**
	 * Método que contiene los resultados de la búsqueda de terceros,
	 * según los criterios dados en la búsqueda avanzada. 
	 * @param con
	 * @return
	 */
	public Collection resultadoBusquedaAvanzada(Connection con, int codigoInstitucion)
	{
		TerceroDao consulta= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTerceroDao();
		Collection coleccion=null;
		
		logger.info("CODIGO BUSQUEDA TIPO TER "+codigoTipoTercero);
		try
		{	
				coleccion=UtilidadBD.resultSet2Collection(consulta.busqueda(con, codigoInstitucion, numeroIdentificacion,descripcion, codigoTipoTercero, digitoVerificacion, activaAux));
		}		
		catch(Exception e)
		{
			logger.warn("Error mundo tercero " +e.toString());
			coleccion=null;
		}
		return coleccion;		
	}

	/**
	 * Busca un tercero dado el tipo y numero de identificacion
	 * @param con
	 * @param numeroIdentificacion
	 * @param tipoIdentificacion
	 * @return
	 * @throws SQLException
	 */
	public int busquedaExistenciaTercero(Connection con, String numeroIdentificacion) 
	{
	    try
	    {
		    int codigoTercero= terceroDao.busquedaExistenciaTercero(con, numeroIdentificacion);
		    return codigoTercero;
	    }
	    catch (SQLException e)
	    {
	        logger.warn("Error mundo tercero busqueda  existencia" +e.toString());
	        return ConstantesBD.codigoNuncaValido;
	    }
	}
	
	/**
	 * Returns the activo.
	 * @return boolean
	 */
	public boolean getActivo() {
		return activo;
	}

	/**
	 * Returns the codigo.
	 * @return int
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * Returns the descripcion.
	 * @return String
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Returns the institucion.
	 * @return institucion
	 */
	public int getInstitucion() {
		return institucion;
	}


	/**
	 * Returns the numeroIdentificacion.
	 * @return String
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	
	
	
	
	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * Sets the activo.
	 * @param activo The activo to set
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	/**
	 * Sets the codigo.
	 * @param codigo The codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * Sets the descripcion.
	 * @param descripcion The descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Sets the institucion.
	 * @param institucion The institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}


	/**
	 * Sets the numeroIdentificacion.
	 * @param numeroIdentificacion The numeroIdentificacion to set
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
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
	 * @return the codigoTipoTercero
	 */
	public int getCodigoTipoTercero() {
		return codigoTipoTercero;
	}

	/**
	 * @param codigoTipoTercero the codigoTipoTercero to set
	 */
	public void setCodigoTipoTercero(int codigoTipoTercero) {
		this.codigoTipoTercero = codigoTipoTercero;
	}

	/**
	 * @return the digitoVerificacion
	 */
	public String getDigitoVerificacion() {
		return digitoVerificacion;
	}

	/**
	 * @param digitoVerificacion the digitoVerificacion to set
	 */
	public void setDigitoVerificacion(String digitoVerificacion) {
		this.digitoVerificacion = digitoVerificacion;
	}
	
	public boolean insertarConceptoRetencion(DtoConceptosRetencionTercero dto, int tercero)
	{
		return terceroDao.insertarConceptoRetencion(dto, tercero);
	}
	
	public ArrayList<DtoConceptosRetencionTercero> consultarConceptosRetTercero(int tercero) 
	{
		return terceroDao.consultarConceptosRetTercero(tercero);
	}

	public boolean modificarConceptoRetencion(DtoConceptosRetencionTercero dto)
	{
		return terceroDao.modificarConceptoRetencion(dto);
	}
	
	
	
	/**
	 * CARGAR TERCERO
	 * @param dto
	 * @return
	 */
	public static ArrayList<InfoDeudorTerceroDto> cargarTerceroArray(InfoDeudorTerceroDto dto) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTerceroDao().cargarTerceroArray(dto);
	}

	
	
}
