/*
 * @(#)FormatoImpresionPresupuesto.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.mundo.presupuesto;

import java.util.HashMap;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.FormatoImpresionPresupuestoDao;


/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 11 /Ene/ 2006
 */
public class FormatoImpresionPresupuesto
{

	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(FormatoImpresionPresupuesto.class);
	
	/**
     * Constructor del objeto (Solo inicializa el acceso a la fuente de datos)
     */
    public FormatoImpresionPresupuesto()
    {
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>FormatoImpresionPresupuestoDao</code> 
	 * para acceder a la fuente de datos. 
	 */
	private FormatoImpresionPresupuestoDao formatoImpresionPresupuestoDao ;
	
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
			formatoImpresionPresupuestoDao = myFactory.getFormatoImpresionPresupuestoDao();
			wasInited = (formatoImpresionPresupuestoDao != null);
		}

		return wasInited;
	}

	
	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private String estado = "";
	
	/**
	 * Codigo del formato de impresión de presupuesto
	 */
	private int codigoFormato;
	
	/**
	 * String con el nombre del formato de impresión
	 */
	private String nombreFormato;

	/**
	 * Titulo de la impresión
	 */
	private String tituloFormato;
	
	/**
	 * Descripcion de la cantidad
	 */
	private String descripcionCantidad;
	
	/**
	 * Descripcion del valor unitario
	 */
	private String descripcionValUnitario;
	
	/**
	 * Descripcion del valor total
	 */
	private String descripcionValTotal;
	
	/**
	 * Entero que indica la prioridad de los servicios
	 */
	private int prioridadServicios;
	
	/**
	 * Entero que indica la prioridad de los articulos
	 */
	private int prioridadArticulos;
	
	/**
	 * Descripcion de la seccion de articulos
	 */
	private String descripcionSecArticulo;
	
	/**
	 * Nivel del detalle de articulos
	 */
	private String nivel;
	
	/**
	 * String con la nota de pie de pagina parametrizada
	 */
	private String piePagina;
	
	/**
	 * Almacena los datos de la consulta los traslados de area
	 */
	private HashMap mapaTrasladosArea;

    /**
    * Poscicion del mapa en la consulta de facturas
    */
    private int posicionMapa;
    
    /**
     * Nombre del grupo de servicios
     */
    private String grupoServicios;
    
    /**
     * Alamancena los formatos existentes
     */
    private HashMap mapaFormatosPrevios;
    
    /**
     * Alamacena el detalle de los servicios
     */
    private HashMap mapaDetServicios;
	
    /**
     * Almacena el detalle de los articulos
     */
    private HashMap mapaDetArticulos;
    
    /*********************************************************************
     * 						   GETTER Y SETTER							 *
    **********************************************************************/
	
	/**
	 * @return Returns the posicionMapa.
	 */
	public int getPosicionMapa()
	{
		return posicionMapa;
	}
	/**
	 * @param posicionMapa The posicionMapa to set.
	 */
	public void setPosicionMapa(int posicionMapa)
	{
		this.posicionMapa= posicionMapa;
	}
	
    /**
     * @return Retorna el estado.
     */
    public  String getEstado()
    {
        return estado;
    }
    
    /**
     * @param estado El estado a establecer.
     */
    public void setEstado(String estado)
    {
        this.estado = estado;
    }

    /**
	 * @return Returns the mapaTrasladosArea.
	 */
	public HashMap getMapaTrasladosArea()
	{
		return mapaTrasladosArea;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaTrasladosArea(String key, Object value) 
	{
		mapaTrasladosArea.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaTrasladosArea(String key) 
	{
		return mapaTrasladosArea.get(key);
	}
	
	/**
	 * @param mapaTrasladosArea The mapaTrasladosArea to set.
	 */
	public void setMapaTrasladosArea(HashMap mapaTrasladosArea)
	{
		this.mapaTrasladosArea= mapaTrasladosArea;
	}
	
	/**
	 * @return Returns the codigoFormato.
	 */
	public int getCodigoFormato()
	{
		return codigoFormato;
	}
	/**
	 * @param codigoFormato The codigoFormato to set.
	 */
	public void setCodigoFormato(int codigoFormato)
	{
		this.codigoFormato=codigoFormato;
	}
	/**
	 * @return Returns the descripcionCantidad.
	 */
	public String getDescripcionCantidad()
	{
		return descripcionCantidad;
	}
	/**
	 * @param descripcionCantidad The descripcionCantidad to set.
	 */
	public void setDescripcionCantidad(String descripcionCantidad)
	{
		this.descripcionCantidad=descripcionCantidad;
	}
	/**
	 * @return Returns the descripcionSecArticulo.
	 */
	public String getDescripcionSecArticulo()
	{
		return descripcionSecArticulo;
	}
	/**
	 * @param descripcionSecArticulo The descripcionSecArticulo to set.
	 */
	public void setDescripcionSecArticulo(String descripcionSecArticulo)
	{
		this.descripcionSecArticulo=descripcionSecArticulo;
	}
	/**
	 * @return Returns the descripcionValTotal.
	 */
	public String getDescripcionValTotal()
	{
		return descripcionValTotal;
	}
	/**
	 * @param descripcionValTotal The descripcionValTotal to set.
	 */
	public void setDescripcionValTotal(String descripcionValTotal)
	{
		this.descripcionValTotal=descripcionValTotal;
	}
	/**
	 * @return Returns the descripcionValUnitario.
	 */
	public String getDescripcionValUnitario()
	{
		return descripcionValUnitario;
	}
	/**
	 * @param descripcionValUnitario The descripcionValUnitario to set.
	 */
	public void setDescripcionValUnitario(String descripcionValUnitario)
	{
		this.descripcionValUnitario=descripcionValUnitario;
	}
	/**
	 * @return Returns the grupoServicios.
	 */
	public String getGrupoServicios()
	{
		return grupoServicios;
	}
	/**
	 * @param grupoServicios The grupoServicios to set.
	 */
	public void setGrupoServicios(String grupoServicios)
	{
		this.grupoServicios=grupoServicios;
	}
	/**
	 * @return Returns the nivel.
	 */
	public String getNivel()
	{
		return nivel;
	}
	/**
	 * @param nivel The nivel to set.
	 */
	public void setNivel(String nivel)
	{
		this.nivel=nivel;
	}
	/**
	 * @return Returns the nombreFormato.
	 */
	public String getNombreFormato()
	{
		return nombreFormato;
	}
	/**
	 * @param nombreFormato The nombreFormato to set.
	 */
	public void setNombreFormato(String nombreFormato)
	{
		this.nombreFormato=nombreFormato;
	}
	/**
	 * @return Returns the piePagina.
	 */
	public String getPiePagina()
	{
		return piePagina;
	}
	/**
	 * @param piePagina The piePagina to set.
	 */
	public void setPiePagina(String piePagina)
	{
		this.piePagina=piePagina;
	}
	/**
	 * @return Returns the prioridadArticulos.
	 */
	public int getPrioridadArticulos()
	{
		return prioridadArticulos;
	}
	/**
	 * @param prioridadArticulos The prioridadArticulos to set.
	 */
	public void setPrioridadArticulos(int prioridadArticulos)
	{
		this.prioridadArticulos=prioridadArticulos;
	}
	/**
	 * @return Returns the prioridadServicios.
	 */
	public int getPrioridadServicios()
	{
		return prioridadServicios;
	}
	/**
	 * @param prioridadServicios The prioridadServicios to set.
	 */
	public void setPrioridadServicios(int prioridadServicios)
	{
		this.prioridadServicios=prioridadServicios;
	}
	/**
	 * @return Returns the tituloFormato.
	 */
	public String getTituloFormato()
	{
		return tituloFormato;
	}
	/**
	 * @param tituloFormato The tituloFormato to set.
	 */
	public void setTituloFormato(String tituloFormato)
	{
		this.tituloFormato=tituloFormato;
	}
	
	/**
	 * @return Returns the mapaFormatosPrevios.
	 */
	public HashMap getMapaFormatosPrevios()
	{
		return mapaFormatosPrevios;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaFormatosPrevios(String key, Object value) 
	{
		mapaFormatosPrevios.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaFormatosPrevios(String key) 
	{
		return mapaFormatosPrevios.get(key);
	}
	
	/**
	 * @param mapaFormatosPrevios The mapaFormatosPrevios to set.
	 */
	public void setMapaFormatosPrevios(HashMap mapaFormatosPrevios)
	{
		this.mapaFormatosPrevios= mapaFormatosPrevios;
	}
	
	
	/**
	 * @return Returns the mapaDetServicios.
	 */
	public HashMap getMapaDetServicios()
	{
		return mapaDetServicios;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaDetServicios(String key, Object value) 
	{
		mapaDetServicios.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaDetServicios(String key) 
	{
		return mapaDetServicios.get(key);
	}
	
	/**
	 * @param mapaDetServicios The mapaDetServicios to set.
	 */
	public void setMapaDetServicios(HashMap mapaDetServicios)
	{
		this.mapaDetServicios= mapaDetServicios;
	}
	
	/**
	 * @return Returns the mapaDetArticulos.
	 */
	public HashMap getMapaDetArticulos()
	{
		return mapaDetArticulos;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaDetArticulos(String key, Object value) 
	{
		mapaDetArticulos.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaDetArticulos(String key) 
	{
		return mapaDetArticulos.get(key);
	}
	
	/**
	 * @param mapaDetArticulos The mapaDetArticulos to set.
	 */
	public void setMapaDetArticulos(HashMap mapaDetArticulos)
	{
		this.mapaDetArticulos= mapaDetArticulos;
	}
	
	/**************************************************************************************
	 *						     IMPLEMETACION DE METODOS								  *
	**************************************************************************************/
	
	/**
	 * Método para consultar los formatos de impresión 
	 * existentes para una insititución
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarFormatosExistentes (Connection con, int institucion) throws SQLException
	{
		return formatoImpresionPresupuestoDao.consultarFormatosExistentes(con, institucion);
	}
	
	
	/**
	 * Método para consultar los Grupos de Servicios existentes
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarGruposServicios (Connection con) throws SQLException
	{
		return formatoImpresionPresupuestoDao.consultarGruposServicios(con);
	}
	
	/**
	 * Método para consultar todo el formato de impresion de presupuesto con 
	 * el detalle de Servicios y el detalle de Articulos
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarFormatoImpresion (Connection con, int codigoFormato) throws SQLException
	{
		return formatoImpresionPresupuestoDao.consultarFormatoImpresion(con, codigoFormato);
	}
	
	/**
	 * Método para consultar el detalle de los servicios de un formato de impresion
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarDetServicios (Connection con, int codigoFormato) throws SQLException
	{
		return formatoImpresionPresupuestoDao.consultarDetServicios(con, codigoFormato);
	}
	
	/**
	 * Método para consultar el detalle de los articulos de un formato de impresion
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarDetAritculos (Connection con, int codigoFormato) throws SQLException
	{
		return formatoImpresionPresupuestoDao.consultarDetArticulos(con, codigoFormato);
	}
	
	/**
	 * Método para insertar un Formato de Impresión de un Presupuesto completo con el detalle
	 * de servicios y con el detalle de articulos
	 * @param con
	 * @param nombreFormato
	 * @param tituloFormato
	 * @param cantidad
	 * @param descripcionCantidad
	 * @param valorUnitario
	 * @param descripcionValUnitario
	 * @param valorTotal
	 * @param descripcionValTotal
	 * @param piePagina
	 * @param fechaHora
	 * @param institucion
	 * @param mapaDetServicios
	 * @param mapaDetArticulos
	 * @param insertarFormatoImpresionBasicoStr
	 * @param insertarDetalleServiciosStr
	 * @param insertarDetalleArticulosStr
	 * @return
	 * @throws SQLException
	 */
	public int insertarFormatoImpresion(Connection con, String nombreFormato, String tituloFormato, boolean cantidad, String descripcionCantidad, boolean valorUnitario, String descripcionValUnitario, boolean valorTotal, String descripcionValTotal, String piePagina, boolean fechaHora, int institucion, HashMap mapaDetServicios, HashMap mapaDetArticulos) throws SQLException
	{
		return formatoImpresionPresupuestoDao.insertarFormatoImpresion(con, nombreFormato, tituloFormato, cantidad, descripcionCantidad, valorUnitario, descripcionValUnitario, valorTotal, descripcionValTotal, piePagina, fechaHora, institucion, mapaDetServicios, mapaDetArticulos);
	}
	
	/**
	 * Método para eliminar por completo el formato de impresion de presupuesto por medio de tres pasos:
	 * 1. Eliminar el detalle de Servicion
	 * 2. Eliminar el detalle de Articulos
	 * 3. Eliminar el formato basico 
	 * @param con
	 * @param codigoFormato
	 * @return
	 * @throws SQLException
	 */
	public  int eliminarFormatoCompleto(Connection con, int codigoFormato) throws SQLException
	{
		return formatoImpresionPresupuestoDao.eliminarFormatoCompleto(con, codigoFormato);
	}
	/**
	 * Método para modificar un Formato de Impresión de un Presupuesto completo con el detalle
	 * de servicios y con el detalle de articulos
	 * @param con
	 * @param nombreFormato
	 * @param tituloFormato
	 * @param cantidad
	 * @param descripcionCantidad
	 * @param valorUnitario
	 * @param descripcionValUnitario
	 * @param valorTotal
	 * @param descripcionValTotal
	 * @param piePagina
	 * @param fechaHora
	 * @param institucion
	 * @param mapaDetServicios
	 * @param mapaDetArticulos
	 * @param insertarFormatoImpresionBasicoStr
	 * @param insertarDetalleServiciosStr
	 * @param insertarDetalleArticulosStr
	 * @return
	 * @throws SQLException
	 */
	public int modificarFormatoImpresion(Connection con, int codigoFormato, String nombreFormato, String tituloFormato, boolean cantidad, String descripcionCantidad, boolean valorUnitario, String descripcionValUnitario, boolean valorTotal, String descripcionValTotal, String piePagina, boolean fechaHora,  HashMap mapaDetServicios, HashMap mapaDetArticulos) throws SQLException
	{
		return formatoImpresionPresupuestoDao.modificarFormatoImpresion(con, codigoFormato, nombreFormato, tituloFormato, cantidad, descripcionCantidad, valorUnitario, descripcionValUnitario, valorTotal, descripcionValTotal, piePagina, fechaHora, mapaDetServicios, mapaDetArticulos);
	}
	/**
	 * Eliminar un grupo de servicios dado un formato y el codigo del grupo
	 * @param con
	 * @param codigoFormato
	 * @param codigoGrupo
	 * @return
	 * @throws SQLException
	 */
	public int eliminarGrupo(Connection con, int codigoFormato, int codigoGrupo)
	{
		return formatoImpresionPresupuestoDao.eliminarGrupo(con, codigoFormato, codigoGrupo);
	}
}
