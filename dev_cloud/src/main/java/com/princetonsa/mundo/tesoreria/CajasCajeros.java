/*
 * Created on 15/09/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.mundo.tesoreria;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadValidacion;

import com.princetonsa.dao.CajasCajerosDao;
import com.princetonsa.dao.DaoFactory;

/**
 * @author artotor
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CajasCajeros 
{
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(CajasCajeros.class);
	
	/**
	 * DAO de este objeto, para trabajar con Cajas Cajeros
	 * en la fuente de datos
	 */    
    private static CajasCajerosDao cajasCajerosDao;
	
	/**
	 * Mapa paara manejar las cajas definidas en el sistema
	 */
	private HashMap mapaCajasCajeros;
	
    /**
     * Centro de atención que se selecciona, cuando empieza
     * la funcionalidad por defecto se selecciona el centro de atención del usuario  
     */
    private int centroAtencion;
	
	
	/**
     * Método que limpia este objeto
     */
    public void reset()
    {
    	this.mapaCajasCajeros = new HashMap ();
    	this.centroAtencion=ConstantesBD.codigoNuncaValido;
    }

    ///////datos modificables///////////////////////
    public boolean activo;
    
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( cajasCajerosDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			cajasCajerosDao= myFactory.getCajasCajerosDao();
			if( cajasCajerosDao!= null )
				return true;
		}
		return false;
	}
	
	public CajasCajeros()
	{
		this.reset();
		init(System.getProperty("TIPOBD"));
	}
	/**
	 * @return Returns the mapaCajasCajeros.
	 */
	public HashMap getMapaCajasCajeros() {
		return mapaCajasCajeros;
	}
	/**
	 * @param mapaCajasCajeros The mapaCajasCajeros to set.
	 */
	public void setMapaCajasCajeros(HashMap mapaCajasCajeros) {
		this.mapaCajasCajeros = mapaCajasCajeros;
	}


	/**
	 * @param con
	 * @param codigoInstitucionInt
	 */
	public int cargarInformacion(Connection con, int codigoInstitucionInt)
	{
		ResultSetDecorator rs=cajasCajerosDao.cargarInformacion(con,codigoInstitucionInt, this.centroAtencion);
		try
		{
			int i=0;
			while(rs.next())
			{
				mapaCajasCajeros.put("consecutivocaja_"+i,rs.getString("consecutivocaja"));
				mapaCajasCajeros.put("codigocaja_"+i,rs.getString("codigocaja"));
				mapaCajasCajeros.put("caja_"+i,rs.getString("descripcioncaja"));
				mapaCajasCajeros.put("logincajero_"+i,rs.getString("loginusuario"));
				mapaCajasCajeros.put("cajero_"+i,rs.getString("nombreusuario"));
				mapaCajasCajeros.put("activo_"+i,rs.getBoolean("activo")+"");
				mapaCajasCajeros.put("institucion_"+i,rs.getBoolean("institucion")+"");
				if(UtilidadValidacion.cajaCajeroUtilizadaEnRecibosCaja(con,rs.getInt("consecutivocaja"),rs.getString("loginusuario")) || UtilidadValidacion.cajaCajeroConTurno(con,rs.getInt("consecutivocaja"),rs.getString("loginusuario")))
				{
					mapaCajasCajeros.put("existerelacion_"+i,true+"");
				}
				else
				{
					mapaCajasCajeros.put("existerelacion_"+i,false+"");
				}
				mapaCajasCajeros.put("tiporegistro_"+i,"BD");
				i++;
			}
			return i;
		}
		catch(SQLException e)
		{
			logger.error("Error consultando la informacion en la BD [CajasCajaeros.java]"+e+"\n\n");
			e.printStackTrace();
		}
		return 0;
	}


	/**
	 * @param con
	 * @param i
	 * @param string
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, int consecutivoCaja, String loginUsuario)
	{
		return cajasCajerosDao.eliminarRegistro(con,consecutivoCaja,loginUsuario);
	}


	/**
	 * @param con
	 * @param i
	 * @param string
	 * @param boolean1
	 * @return
	 */
	public boolean existeModificacion(Connection con, int consecutivoCaja, String loginUsuario, boolean tempActivo) 
	{
		if(cajasCajerosDao.existeModificacion(con,consecutivoCaja,loginUsuario,tempActivo))
		{
			this.activo=!tempActivo;
			return true;
		}
		return false;
	}
	/**
	 * @return Returns the activo.
	 */
	public boolean isActivo() {
		return activo;
	}
	/**
	 * @param activo The activo to set.
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	/**
	 * @param con
	 * @param i
	 * @param string
	 * @param boolean1
	 * @return
	 */
	public boolean modificarRegistro(Connection con, int consecutivoCaja, String loginUsuario, boolean tempActivo)
	{
		return cajasCajerosDao.modificarRegistro(con,consecutivoCaja,loginUsuario,tempActivo);
	}

	/**
	 * @param con
	 * @param i
	 * @param string
	 * @param boolean1
	 * @param codigoInstitucionInt
	 * @return
	 */
	public boolean insertarRegistro(Connection con, int caja, String loginUsuario, boolean activo, int codigoInstitucionInt) 
	{
		return cajasCajerosDao.insertarRegistro(con,caja,loginUsuario,activo,codigoInstitucionInt);
	}

	/**
	 * @return Retorna the centroAtencion.
	 */
	public int getCentroAtencion()
	{
		return centroAtencion;
	}

	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(int centroAtencion)
	{
		this.centroAtencion = centroAtencion;
	}
}
