/*
 * @(#)SolicitudEvolucion.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.mundo.solicitudes;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.SolicitudEvolucionDao;

/**
 *
 *	@version 1.0, May 11, 2004
 */
public class SolicitudEvolucion extends Solicitud
{
	/**
	 * C�digo de la evoluci�n
	 */
	int codigoEvolucion=0;
	
	/**
	 * Maneja los logs del m�dulo de control de Solicitudes
	 */
	private static Logger logger = Logger.getLogger(SolicitudEvolucion.class);

	/**
	 * El DAO usado por el objeto <code>SolicitudEvolucion</code> para acceder
	 * a la fuente de datos.
	 */
	private static SolicitudEvolucionDao solicitudEvolucionDao;


	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores v�lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicializaci�n fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		super.init(tipoBD);
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null)
		{
			solicitudEvolucionDao = myFactory.getSolicitudEvolucionDao();
			wasInited = (solicitudEvolucionDao != null);
		}
		return wasInited;
	}

	/**
	 * Constructor vacio del objeto Solicitud
	 */
	public SolicitudEvolucion ()
	{
		this.clean();
		this.init (System.getProperty("TIPOBD"));
	}

	/**
	 * M�todo para insertar de forma transaccional una solicitud
	 * de evoluci�n (junto a una evoluci�n normal, claramente).
	 * Maneja una transacci�n internamente
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param estado Estado de la transacci�n
	 * @return
	 * @throws SQLException
	 */
	public int insertarSolicitudEvolucion (Connection con) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		myFactory.beginTransaction(con);
		return this.insertarSolicitudEvolucionTransaccional(con, ConstantesBD.finTransaccion);
	}

	/**
	 * M�todo para insertar de forma transaccional una solicitud
	 * de evoluci�n (junto a una evoluci�n normal, claramente).
	 * Soportando definir transaccionalidad
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param estado Estado de la transacci�n
	 * @return
	 * @throws SQLException
	 */
	public int insertarSolicitudEvolucionTransaccional (Connection con, String estado) throws SQLException
	{
		int resp0=0, resp1=0;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		
		//Este insertar esta compuesto del insertar de su padre y 
		//el insertar propio, el del padre mantiene su estado
		//a menos que se seleccione finalizar transacci�n
		//caso en el cual se debe continuar
		
		if (estado.equals(ConstantesBD.finTransaccion))
		{
			resp0=super.insertarSolicitudGeneralTransaccional(con, ConstantesBD.continuarTransaccion);
		}
		else
		{
			resp0=super.insertarSolicitudGeneralTransaccional(con, estado);
		}
		
		//El insertar llena el n�mero de la solicitud autom�ticamente
		//en el padre
		
		resp1=solicitudEvolucionDao.insertarSolicitudEvolucion(con, this.getNumeroSolicitud(), this.codigoEvolucion);
		
		if (resp0<=0||resp1<=0)
		{
			myFactory.abortTransaction(con);
			throw new SQLException ("Problemas realizando la inserci�n de Solicitud de Evoluci�n");
		}
		else
		{
			if (estado.equals(ConstantesBD.finTransaccion))
			{
				myFactory.endTransaction(con);		
			}
		}
		//Devolvemos el n�mero de la solicitud de esta evoluci�n
		return resp0;
	}
	
	/**
	 * M�todo que interpreta la solicitud correspondiente a una
	 * evoluci�n
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoEvolucion
	 * @param estado Estado de la transacci�n
	 * @throws SQLException
	 */
	public void interpretarSolicitudesEvolucionDadaEvolucionTransaccional (Connection con, int codigoEvolucion, String estado) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if (estado.equals(ConstantesBD.inicioTransaccion))
		{
			myFactory.beginTransaction(con);
		}
		
		try
		{
			//No se revisa si sali� bien o mal, simplemente se llama porque
			//puede ser que este egreso no sea cobrable y no haya gen. solicitud
			solicitudEvolucionDao.interpretarSolicitudesEvolucionDadaEvolucion(con, codigoEvolucion);
		}
		catch (SQLException e) 
		{
			myFactory.abortTransaction(con);
			logger.error("Error en interpretarSolicitudesEvolucionDadaEvolucionTransaccional " + e);
			throw e;
		}
		
		if (estado.equals(ConstantesBD.finTransaccion))
		{
			myFactory.endTransaction(con);
		}
	}

	/**
	 * M�todo que interpreta las solicitudes asociadas a una valoraci�n
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoValoracion
	 * @param estado Estado de la transacci�n
	 * @throws SQLException
	 */
	public void interpretarSolicitudesEvolucionDadaValoracionTransaccional (Connection con, int codigoValoracion, String estado) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if (estado.equals(ConstantesBD.inicioTransaccion))
		{
			myFactory.beginTransaction(con);
		}
		
		try
		{
			//No se revisa si sali� bien o mal, simplemente se llama porque
			//puede ser que este egreso no sea cobrable y no haya gen. solicitud
			solicitudEvolucionDao.interpretarSolicitudesEvolucionDadaValoracion(con, codigoValoracion);
		}
		catch (SQLException e) 
		{
			myFactory.abortTransaction(con);
			logger.error("Error en interpretarSolicitudesValoracionDadaValoracionTransaccional " + e);
			throw e;
		}
		
		if (estado.equals(ConstantesBD.finTransaccion))
		{
			myFactory.endTransaction(con);
		}
	}
	
	/**
	 * M�todo que interpreta las solicitudes asociadas a una valoraci�n dado el cod de val, 
	 * evaluando tambien que el estado HC sea respondida
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @param codigoValoracion
	 * @param estado Estado de la transacci�n
	 * @throws SQLException
	 */
	public boolean interpretarSolicitudesEvolucionDadaValoracionYEstadoHCTransaccional (Connection con, int codigoValoracion, String estado)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			if (estado.equals(ConstantesBD.inicioTransaccion))
			{
				myFactory.beginTransaction(con);
			}
			int res=solicitudEvolucionDao.interpretarSolicitudesEvolucionDadaValoracionYEstadoHC(con, codigoValoracion);
			    
			if (estado.equals(ConstantesBD.finTransaccion))
			{
				myFactory.endTransaction(con);
			}
			if(res>0)
			    return true;
			
		}
		catch (SQLException e) 
		{
		    try
		    {
				myFactory.abortTransaction(con);
		    }
		    catch( SQLException e1)
		    {
		        logger.error("Error en interpretarSolicitudesValoracionDadaValoracionTransaccional " + e);
		        return false;
		    }
			return false;	
		}
		return false;
	}
	
	
	
	

	/**
	 * M�todo para limpiar este objeto
	 */
	public void clean()
	{
		super.clean();
		this.codigoEvolucion=0;
	}

	/**
	 * @return
	 */
	public int getCodigoEvolucion() {
		return codigoEvolucion;
	}

	/**
	 * @param i
	 */
	public void setCodigoEvolucion(int i) {
		codigoEvolucion = i;
	}

}
