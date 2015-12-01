/*
 * Created on 2/12/2005
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
import com.princetonsa.dao.inventarios.ArticulosPuntoPedidoDao;

/**
 * @version 1.0, 2/12/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class ArticulosPuntoPedido
{

    /**
	 * Mapa para manejar los articulos
	 */
	private HashMap articulos;
	
    /**
     * Porcentaje Alerta para realizar la busqueda
     */
    private String porcentajeAleta;
	/**
	 * DAO de este objeto, para trabajar con ConceptosPagoCartera
	 * en la fuente de datos
	 */    
    private static ArticulosPuntoPedidoDao articulosDao;
	
	public void reset ()
	{
	    this.articulos=new HashMap();
	    this.articulos.put("numRegistros", "0");
	    this.porcentajeAleta="0.0";
	}
	
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	*/
	public boolean init(String tipoBD)
	{
		if ( articulosDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			articulosDao= myFactory.getArticulosPuntoPedidoDao();
			if( articulosDao!= null )
				return true;
		}
			return false;
	}
	
	public ArticulosPuntoPedido ()
	{
	    this.reset();
	    init(System.getProperty("TIPOBD"));
	}
    /**
     * @return Returns the articulos.
     */
    public HashMap getArticulos()
    {
        return articulos;
    }
    /**
     * @param articulos The articulos to set.
     */
    public void setArticulos(HashMap articulos)
    {
        this.articulos = articulos;
    }
    /**
     * @return Returns the porcentajeAleta.
     */
    public String getPorcentajeAleta()
    {
        return porcentajeAleta;
    }
    /**
     * @param porcentajeAleta The porcentajeAleta to set.
     */
    public void setPorcentajeAleta(String porcentajeAleta)
    {
        this.porcentajeAleta = porcentajeAleta;
    }

    
    /**
     * @param con
     * @param institucion
     * @param porcentajeAleta2
     */
    public void consultarArticulosGeneral(Connection con, String porcAlerta, int institucion)
    {
        this.articulos=articulosDao.consultarArticulosGeneral(con,porcAlerta,institucion);
    }

    
    /**
     * @param con
     * @param porcentajeAleta2
     * @param codBusqueda
     * @param desBusqueda
     */
    public void consultarArticulosAvanzada(Connection con, String porcAlerta, String codBusqueda, String desBusqueda, int institucion)
    {
        this.articulos=articulosDao.consultarArticulosAvanzada(con,porcAlerta,codBusqueda,desBusqueda,institucion);
    }
}
