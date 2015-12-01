/*
 * Created on 31-ago-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.SustitutosInventariosDao;

/**
 * @author armando
 * Clase encargada de manejar la interacción del
 * usuario con los datos almacenados previamente
 * en la fuente de datos
 * Princeton 31-ago-2004
 */
public class SustitutosInventarios {
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static SustitutosInventariosDao susInventariosDao;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(SustitutosInventarios.class);
	
	/**
	 * Código del artículo principal
	 */
	private int codPrincipal;
	/**
	 * Código del artículo sustituto
	 */
	private int codSustituto;
	/**
	 * Codigo de sustituto a modificar
	 */
	private int codSustitutoOld;
	

    /**
     * @return Retorna el codPrincipal.
     */
    public int getCodPrincipal() {
        return codPrincipal;
    }
    /**
     * @param codPrincipal Asigna el codPrincipal.
     */
    public void setCodPrincipal(int codPrincipal) {
        this.codPrincipal = codPrincipal;
    }
    /**
     * @return Retorna el codSustituto.
     */
    public int getCodSustituto() {
        return codSustituto;
    }
    /**
     * @param codSustituto Asigna el codSustituto.
     */
    public void setCodSustituto(int codSustituto) {
        this.codSustituto = codSustituto;
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
				susInventariosDao = myFactory.getSustitutosInventariosDao();
				wasInited = (susInventariosDao != null);
			}
			return wasInited;
	}
    
    public void reset()
    {
        this.codPrincipal=-1;
        this.codSustituto=-1;
    }
	/**
	 * Constructor de la clase, inicializa en vacio todos los parámetros.
	 */
    public SustitutosInventarios()
    {
    	    reset();
    		this.init (System.getProperty("TIPOBD"));
    }
    /**
     * Contructor de la clace dando valor del articulo principal.
     * @param codigoPrincipal, Codigo de articulo principal.
     */
    public SustitutosInventarios(int codigoPrincipal)
    {
        this.codPrincipal=codigoPrincipal;
	    this.init (System.getProperty("TIPOBD"));
    }
    
    /**
     * Contructor de la clace dando valores iniciales a sus atributos.
     * @param codigoPrincipal, Codigo de articulo principal.
     * @param codigoSustituto, Codigo de articulo Sustituto.
     */
    public SustitutosInventarios(int codigoPrincipal,int codigoSustituto)
    {
        this.codPrincipal=codigoPrincipal;
        this.codSustituto=codigoSustituto;
	    this.init (System.getProperty("TIPOBD"));
    }
    /**
     * Contructor de la clace dando valores iniciales a sus atributos.
     * @param codigoPrincipal, Codigo de articulo principal.
     * @param codigoSustituto, Codigo de articulo Sustituto.
     */
    public SustitutosInventarios(int codigoPrincipal,int codigoSustituto,int codigoSustitutoOld)
    {
        this.codPrincipal=codigoPrincipal;
        this.codSustituto=codigoSustituto;
        this.codSustitutoOld=codigoSustitutoOld;
	    this.init (System.getProperty("TIPOBD"));
    }
    /**
     * Metodo que inserta un registro en la tabla Sustitutos Inventario
     * @param con,Conexion
     * @param codigoPrincipal,<b>int</b>, Codigo del articulo Principal.
     * @param codigoSustituto,<b>int</b>, Codigo del articulo Sustituto.
     * @return -1 si se produjo un error.
     */
    public int insertarSustitutosInventarios(Connection con)
    {
        return susInventariosDao.insertarSustitutoInventario(con,this.codPrincipal,this.codSustituto);
    }
    /**
     * Metodo que modifica un registro en la tabla Sustitutos Inventario,
     * solo se pueden modificar los articulos sustitutos de un determinado
     * aticulo principal.
     * @param con,Conexion
     * @param codigoPrincipal,<b>int</b>, Codigo del articulo Principal a modificar.
     * @param codigoSustituto,<b>int</b>, Codigo del nuevo articulo Sustituto.
     * @return -1 si se produjo un error.
     */
    public int modificarSustitutosInventarios(Connection con)
    {
        return susInventariosDao.modificarSustitutoInventario(con,this.codPrincipal,this.codSustituto,this.codSustitutoOld);
    }
    /**
     * Metodo que elimina un registro en la tabla Sustitutos Inventario,
     * solo se pueden eliminar los articulos sustitutos de un determinado
     * aticulo principal.
     * @param con,Conexion
     * @param codigoPrincipal,<b>int</b>, Codigo del articulo Principal.
     * @param codigoSustituto,<b>int</b>, Codigo del articulo Sustituto que sera eliminado.
     * @return -1 si se produjo un error.
     */
    public int eliminarSustitutosInventarios(Connection con)
    {
        return susInventariosDao.eliminarSustitutoInventario(con,this.codPrincipal,this.codSustituto);
    }
    /**
     * Metodo que realiza la consulta de un registro en la tabla Sustitutos Inventario,
     * @param con,Conexion
     * @param codigoPrincipal,<b>int</b>, Codigo del articulo Principal.
     * @return Collection con los datos
     */
    public Collection consultarSustitutosInventarios(Connection con,int codigoPrincipal)
    {
        return susInventariosDao.consultarSustitutoInventario(con,codigoPrincipal);
    }
    /**
     * @return Retorna el codSustitutoOld.
     */
    public int getCodSustitutoOld() {
        return codSustitutoOld;
    }
    /**
     * @param codSustitutoOld Asigna el codSustitutoOld.
     */
    public void setCodSustitutoOld(int codSustitutoOld) {
        this.codSustitutoOld = codSustitutoOld;
    }
    /**
     * Metodo que me retorna un boolean informado si ya existe el registro.
     * @param con, Conexion
     * @param codPrincipal2
     * @param codSustituto2
     * @return true or false
     */
    public boolean existeSustitutos(Connection con, int codigoPrincipal, int codigoSustituto) {
	    ResultSetDecorator rs;
        try 
        {
            rs=susInventariosDao.consultarSustitutoInventarioEspecifico(con,codigoPrincipal,codigoSustituto);
            return rs.next();
        } catch (SQLException e) {
            logger.warn("error realizando la verificacion"+e.toString());
        }
        return true;
    }
}
