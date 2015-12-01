/*
 * Created on 19/12/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.CierresInventarioDao;

/**
 * @version 1.0, 19/12/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class CierreInventarios
{
    /**
     * Anio del cierre que se esta realizando
     */
    private String anioCierre;
    /**
     * Mes del cierre que se esta realizando
     */
    private String mesCierre;
    /**
     * Observaciones del cierre. 
     */
    private String observacionesCierre;
    /**
     * boolean que indica si el cierre que se esta realizando es de cierre inicial.
     */
    private boolean cierreInicial;
    /**
     * boolean que indica si el cierre que se esta realizando es de cierre final.
     */
    private boolean cierreFinal;
    
    /**
     * Mapa movimientos de inventarios
     */
    private HashMap movimientos;
    
    /**
     * Mapa que contine el encabezado del mapa
     */
    private HashMap encabezadoCirre;
    
    
    /**
	 * DAO de este objeto, para trabajar con cierres
	 * en la fuente de datos
	 */    
    private static CierresInventarioDao cierresDao;
    /**
     * Metodo que resetea todas las variables del Mundo.
     */
    public void reset()
    {
        this.anioCierre="";
        this.mesCierre="";
        this.observacionesCierre="";
        this.cierreInicial=false;
        this.cierreFinal=false;
    }

    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	*/
	public boolean init(String tipoBD)
	{ 
	    if ( cierresDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			cierresDao= myFactory.getCierresInventarioDao();
			if( cierresDao!= null )
				return true;
		}
			return false;
	}
    
    
    /**
     * Constructor de la clase.
     */
    public CierreInventarios()
    {
        this.reset();
    	init(System.getProperty("TIPOBD"));
    }
    public boolean isCierreFinal()
    {
        return cierreFinal;
    }
    /**
     * @param cierreFinal The cierreFinal to set.
     */
    public void setCierreFinal(boolean cierreFinal)
    {
        this.cierreFinal = cierreFinal;
    }
    /**
     * @return Returns the cierreInicial.
     */
    public boolean isCierreInicial()
    {
        return cierreInicial;
    }
    /**
     * @param cierreInicial The cierreInicial to set.
     */
    public void setCierreInicial(boolean cierreInicial)
    {
        this.cierreInicial = cierreInicial;
    }
    /**
     * @return Returns the observacionesCierre.
     */
    public String getObservacionesCierre()
    {
        return observacionesCierre;
    }
    /**
     * @param observacionesCierre The observacionesCierre to set.
     */
    public void setObservacionesCierre(String observacionesCierre)
    {
        this.observacionesCierre = observacionesCierre;
    }
    /**
     * @return Returns the anioCierre.
     */
    public String getAnioCierre()
    {
        return anioCierre;
    }
    /**
     * @param anioCierre The anioCierre to set.
     */
    public void setAnioCierre(String anioCierre)
    {
        this.anioCierre = anioCierre;
    }
    /**
     * @return Returns the mesCierre.
     */
    public String getMesCierre()
    {
        return mesCierre;
    }
    /**
     * @param mesCierre The mesCierre to set.
     */
    public void setMesCierre(String mesCierre)
    {
        this.mesCierre = mesCierre;
    }

    /**
     * Metodo que carga los mivimientos por almacen de un articulo en la instucion.
     * la informacion es cargada en el mapa movimientos
     * @param con
     * @param institucion
     * @param fechaInicial
     * @param fechaFinal
     */
	public void cargarMovimientosInventarios(Connection con, int institucion, String fechaInicial, String fechaFinal)
	{
		movimientos=cierresDao.cargarMovimientosInventarios(con,institucion,fechaInicial,fechaFinal);
	}

	/**
	 * @return Retorna encabezadoCirre.
	 */
	public HashMap getEncabezadoCirre()
	{
		return encabezadoCirre;
	}

	/**
	 * @param encabezadoCirre Asigna encabezadoCirre.
	 */
	public void setEncabezadoCirre(HashMap encabezadoCirre)
	{
		this.encabezadoCirre = encabezadoCirre;
	}

	/**
	 * @return Retorna movimientos.
	 */
	public HashMap getMovimientos()
	{
		return movimientos;
	}

	/**
	 * @param movimientos Asigna movimientos.
	 */
	public void setMovimientos(HashMap movimientos)
	{
		this.movimientos = movimientos;
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public boolean generarCierre(Connection con)
	{
		return cierresDao.generarCierre(con,encabezadoCirre,movimientos);
	}

	/**
	 * 
	 * @param con
	 * @param codigoCierre
	 */
	public int eliminarCierreInventarios(Connection con, String codigoCierre)
	{
		return cierresDao.eliminarCierreInventarios(con,codigoCierre);
	}


}
