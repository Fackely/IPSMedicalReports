/*
 * @(#)ServiciosCamas1.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ServiciosCamas1Dao;

/**
 * Clase para el manejo de servicios camas1
 * @version 1.0, Junio 1, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class ServiciosCamas1 
{
    /**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ServiciosCamas1Dao serviciosCamas1Dao;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ServiciosCamas1.class);
	
	/**
	 * codigo del servicios_camas PK
	 */
	private int codigo;
	
	/**
	 * codigo de la cama
	 */
	private int codigoCama;
	
	/**
	 * codigo - desc del servicio
	 */
	private InfoDatosInt servicio;
	
	/**
	 * codigo - nombre del tipo de monitoreo
	 */
	private InfoDatosInt tipoMonitoreo;
	
	/**
	 * Resetea todos los atributos del objeto
	 */
	public void reset()
	{
	    this.codigo=ConstantesBD.codigoNuncaValido;
	    this.codigoCama=ConstantesBD.codigoNuncaValido;
	    this.servicio = new InfoDatosInt();
	    this.tipoMonitoreo= new InfoDatosInt();
	}
	
	/**
	 * Constructor de la clase, inicializa en vacio todos los parámetros
	 */
	public ServiciosCamas1()
	{
		reset();
		this.init (System.getProperty("TIPOBD"));
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
		    serviciosCamas1Dao = myFactory.getServiciosCamas1Dao();
			wasInited = (serviciosCamas1Dao != null);
		}
		return wasInited;
	}
	
	/**
	 * Metodo que inserta un servicioCamas1 en una transaccion, es necesario instanciar los atributos de este objeto
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	  public boolean insertarServiciosCamas1Transaccional (Connection con, String estado) throws SQLException
	  {
	      boolean elementosInsertados=false;
	      DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	      if (estado.equals(ConstantesBD.inicioTransaccion))
	      {
	          if (!myFactory.beginTransaction(con))
	          {
	              myFactory.abortTransaction(con);
	          }
	      }
	      try
	      {
	          elementosInsertados=serviciosCamas1Dao.insertarServicioCama(con, this.getCodigoCama(),this.getServicio().getCodigo(), this.getTipoMonitoreo().getCodigo());
	          
	          if (!elementosInsertados)
	          {
	              myFactory.abortTransaction(con);
	          }
	      }
	      catch (SQLException e)
	      {
	          myFactory.abortTransaction(con);
	          throw e;
	      }
	      
	      if (estado.equals(ConstantesBD.finTransaccion))
	      {
	          myFactory.endTransaction(con);
	      }
	      return elementosInsertados;
	  }
	
	/**
	 * Metodo que elimina un servicioCamas1 en una transaccion, es necesario instanciar los atributos de este objeto
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	  public boolean eliminarServiciosCamas1Transaccional (Connection con, String estado) throws SQLException
	  {
	      boolean elementosEliminados=false;
	      DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	      if (estado.equals(ConstantesBD.inicioTransaccion))
	      {
	          if (!myFactory.beginTransaction(con))
	          {
	              myFactory.abortTransaction(con);
	          }
	      }
	      try
	      {
	          elementosEliminados=serviciosCamas1Dao.eliminarServiciosCama(con, this.getCodigo());
	          
	          if (!elementosEliminados)
	          {
	              myFactory.abortTransaction(con);
	          }
	      }
	      catch (SQLException e)
	      {
	          myFactory.abortTransaction(con);
	          throw e;
	      }
	      
	      if (estado.equals(ConstantesBD.finTransaccion))
	      {
	          myFactory.endTransaction(con);
	      }
	      return elementosEliminados;
	  } 
 	  
	/**
     * Metodo para cargar los servicios - tipoMonitoreo en el caso de que sea de uci 
	 * @param con
	 * @return
	 */
	public HashMap cargarServiciosCama1CasoEsUci(Connection con)
	{
		serviciosCamas1Dao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getServiciosCamas1Dao();
		HashMap map= null;
		try
		{
		    String[] colums={"codigoServicio", "nombreServicio", "codigoTipoMonitoreo", "nombreTipoMonitoreo", "codigoTablaServiciosCama" };
			map=UtilidadBD.resultSet2HashMap(colums, serviciosCamas1Dao.detalleServiciosCama(con,this.getCodigoCama(),true), false, true).getMapa();
		}
		catch(Exception e)
		{
			logger.warn("Error mundo Hash Map ServiciosCamas1 " +e.toString());
			map=null;
		}
		
		return map;
	}
  
	/**
     * Metodo para cargar los servicios - en el caso de que NO sea de uci 
	 * @param con
	 * @return
	 */
	public HashMap cargarServicioCama1CasoNoEsUci(Connection con)
	{
		serviciosCamas1Dao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getServiciosCamas1Dao();
		HashMap map= null;
		try
		{
		    String[] colums={"codigoServicio", "nombreServicio", "codigoTablaServiciosCama" };
			map=UtilidadBD.resultSet2HashMap(colums, serviciosCamas1Dao.detalleServiciosCama(con,this.getCodigoCama(), false), false, true).getMapa();
		}
		catch(Exception e)
		{
			logger.warn("Error mundo Hash Map ServiciosCamas1 No Uci " +e.toString());
			map=null;
		}
		return map;
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
     * @return Returns the servicio.
     */
    public InfoDatosInt getServicio() {
        return servicio;
    }
    /**
     * @param servicio The servicio to set.
     */
    public void setServicio(InfoDatosInt servicio) {
        this.servicio = servicio;
    }
    /**
     * @return Returns the tipoMonitoreo.
     */
    public InfoDatosInt getTipoMonitoreo() {
        return tipoMonitoreo;
    }
    /**
     * @param tipoMonitoreo The tipoMonitoreo to set.
     */
    public void setTipoMonitoreo(InfoDatosInt tipoMonitoreo) {
        this.tipoMonitoreo = tipoMonitoreo;
    }
    /**
     * @return Returns the codigoCama.
     */
    public int getCodigoCama() {
        return codigoCama;
    }
    /**
     * @param codigoCama The codigoCama to set.
     */
    public void setCodigoCama(int codigoCama) {
        this.codigoCama = codigoCama;
    }
}
