/*
 * @(#)CIE.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;

import util.UtilidadBD;

import com.princetonsa.dao.CIEDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Clase para el manejo de la vigencia de diagnósticos
 * @version 1.0, Agosto 17, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class CIE 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static CIEDao cieDao;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(CIE.class);
		
	/**
	 * Código CIE asignado por el sistema
	 */
	private int codigo;
		
	/**
	 * Código Real  del CIE para el user (Tipo CIE) 
	 */
	private String codigoReal;
	
	/**
	 * Fecha desde la cual estará vigente el CIE
	 */
	private String vigencia;
	
	/**
	 * Constructor de la clase, inicializa los atributos con los datos correspondientes.
	 * @param codigo, int, código del convenio
	 ** @param codigoReal, String, para capturar el tipo de CIE
	 * @param vigencia, String, para ingresarl la fecha desde la cuál estará vigente el CIE
	 */
	public CIE(	int codigo,	
						String codigoReal,
						String vigencia)
	{
			this.codigo=codigo;
			this.codigoReal=codigoReal;
			this.vigencia=vigencia;
			this.init (System.getProperty("TIPOBD"));
	}											

	/**
	 * Constructor de la clase, inicializa en vacio todos los parámetros
	 */		
	public CIE()
	{
		reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * resetea los datos pertinentes a la vigencia del diagnóstico
	 */
	public void reset()
	{
		this.codigo = -1;
		this.codigoReal="";
		this.vigencia="";
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
				cieDao = myFactory.getCIEDao();
				wasInited = (cieDao != null);
			}
			return wasInited;
	}
	
	/**
	 * Método para insertar una vigencia de diagnóstico
	 * @param con una conexion abierta con una fuente de datos
	 * @return número de filas insertadas (1 o 0)
	 * @throws SQLException
	 */
	public int insertarCIE(Connection con) throws SQLException 
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int  resp1=0;
			
		if (cieDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (CIEDao - insertarCIE )");
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);

		resp1=cieDao.insertar(con,codigoReal,vigencia);
		
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
	 * @param  codigo CIE
	 * @return
	 * @throws SQLException
	 */
	public boolean cargarResumen(Connection con, int codigo) throws SQLException
	{
		ResultSetDecorator rs=cieDao.cargarResumen(con,codigo);
		
		if (rs.next())
		{
			this.codigo=rs.getInt("codigo");
			this.codigoReal=rs.getString("codigoReal");
			this.vigencia=rs.getString("vigencia");
			
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Método que  Verifica la existencia de un tipo de CIE, ya que no pueden estar duplicados
	 * @param con conexión
	 * @param  codigo CIE
	 * @return
	 * @throws SQLException
	 */
	public boolean existeTipoCIE(Connection con,  String codigoReal) throws SQLException
	{
		ResultSetDecorator rs=cieDao.existeTipoCIE(con, codigoReal);
		
		if (rs.next())
		{
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Método que  Verifica la existencia de una fecha inicio vigencia de CIE, ya que no pueden estar duplicados
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public boolean existeFechaInicioVigenciaCIE(Connection con, String vigencia) throws SQLException
	{
		ResultSetDecorator rs=cieDao.existeFechaInicioVigenciaCIE(con,vigencia);
		
		if (rs.next())
		{
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Método utilizado en la funcionalidad Modificar vigencia diagnóstico
	 * que tiene como finalidad modificar la fecha de vigencia. 
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int modificarCIE(Connection con) throws SQLException 
	{
			int resp=0;
			resp=cieDao.modificar(con,this.getCodigo(),this.getVigencia());
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
	public int modificarCIETransaccional (Connection con, String estado) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp1=0;
		if (estado==null)
		{
		    myFactory.abortTransaction(con);
			throw new SQLException ("El estado de la transacción (CIE - modificarCIETransaccional ) no esta especificado");
		}
		if (cieDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (CIEDao - modificarCIETransaccional )");
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
		resp1=cieDao.modificar(con,this.getCodigo(),this.getVigencia());
		
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
	 * Borra una vigencia de diagnóstco según su código 
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int borrarCIE(Connection con) throws SQLException 
	{
		int resp=0;
		resp=cieDao.borrarCIE(con,this.getCodigo());
		return resp;
	}
	

	/**
	 * Método que obtiene todos los resultados de la tabla tipos_cie
	 * para mostrarlos en el listado
	 * @param con
	 * @return
	 */
	public Collection listadoCIE(Connection con)
	{
		cieDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCIEDao();
		Collection coleccion=null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(cieDao.listado(con));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo CIE " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}

	/**
	 * @return Returns the codigo.
	 */
	public int getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return Returns the codigoReal.
	 */
	public String getCodigoReal() {
		return codigoReal;
	}
	/**
	 * @param codigoReal The codigoReal to set.
	 */
	public void setCodigoReal(String codigoReal) {
		this.codigoReal = codigoReal;
	}
	/**
	 * @return Returns the vigencia.
	 */
	public String getVigencia() {
		return vigencia;
	}
	/**
	 * @param vigencia The vigencia to set.
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}
}
