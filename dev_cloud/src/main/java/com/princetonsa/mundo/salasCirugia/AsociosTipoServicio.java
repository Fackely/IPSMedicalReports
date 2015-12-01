/*
 * @(#)Asocios>TipoServicio.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 */
package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;
import util.UtilidadBD;

import com.princetonsa.dao.AsociosTipoServicioDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Clase para el manejo de asocios tipo servicio
 * @version 1.0, Sep 20, 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class AsociosTipoServicio 
{
    /**
	 * DAO utilizado por el objeto parra acceder a la fuente de datos
	 */
	private static AsociosTipoServicioDao asoDao = null;

	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(AsociosTipoServicio.class);
	
	/**
	 * codigo del asocio x tipo servicio
	 */
	private int codigo;
	
	/**
	 * codigo - nombre del esquema tarifario particular
	 */
	private InfoDatosInt esquemaTarifario;
	
	/**
	 * indica si el esquema tarifario es general (TODOS - TODOS ISS - TODOS SOAT)
	 */
	private boolean esEsquemaTarifarioGeneral;
	
	/**
	 * acronimo - nombre del tipo servicio
	 */
	private InfoDatos tipoServicio;
	
	/**
	 * codigoTipoAsocio - acronimoTipoServicioAsocio - nombreTipoServicioAsocio 
	 */
	private InfoDatos tipoAsocio;
	
	/**
	 * codigo -descripcion del servicio
	 */
	private InfoDatosInt servicio;
	
	/**
	 * true, false, vacio= no realizar busqueda
	 */
	private String activo;
	
	/**
	 * reset de los atributos
	 *
	 */
	public void reset()
	{
	    this.codigo= ConstantesBD.codigoNuncaValido;
	    this.esquemaTarifario= new InfoDatosInt();
	    this.esEsquemaTarifarioGeneral= false;
	    this.tipoServicio= new InfoDatos();
	    this.tipoAsocio= new InfoDatos();
	    this.servicio= new InfoDatosInt();
	    this.activo= "";
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
			asoDao = myFactory.getAsociosTipoServicioDao();
			wasInited = (asoDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Constructor vacio de la clase
	 *
	 */
	public AsociosTipoServicio()
	{
	    this.reset();
	    this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * (Utilizado para ingresar - Modificar - Eliminar )Método que obtiene todos los resultados de los asocios tipo x servicio
	 * para mostrarlos en el listado en un HashMap y poder ingresar otros o modificarlos.
	 * @param con
	 * @return
	 */
	public HashMap listadoMapaAsociosTipoServicio(Connection con, int codigoInstitucion)
	{
		asoDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAsociosTipoServicioDao();
		HashMap map= null;
		try
		{
		    String[] colums={	"codigo",
		            					"codigoEsquemaTarifarioBoolEsGeneral",
		            					"nombreEsquemaTarifario",
		            					"acronimoTipoServicio",
		            					"nombreTipoServicio",
		            					"codigoTipoAsocio",
		            					"acronimoTipoServicioAsocio",
		            					"nombreTipoServicioAsocio",
		            					"codigoServicio",
		            					"descripcionServicio",
		            					"activo",
		            					"estaBD", 
		            					"esEliminada" 
		            				};
			map=UtilidadBD.resultSet2HashMap(colums, asoDao.listadoAsociosTipoServicio(con, codigoInstitucion), true, true).getMapa();
		}
		catch(Exception e)
		{
			logger.warn("Error mundo Hash Map  listadoMapaAsociosTipoServicio " +e.toString());
			map=null;
		}
		return map;
	}
	
	/**
	 * Inserta un asocio x tipo servicio dentro de una transaccion
	 * @param con
	 * @param codigoInstitucion
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public boolean insertarAsocioTipoServicioTransaccional (Connection con, int codigoInstitucion, String estado) throws SQLException
	{
	    boolean insertado=false;
	    boolean yaExisteRegistro=false;
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
	    	// en caso de sea activo entonces  toca validar, de lo contrario lo inserta
	    	if(this.getActivoBoolean())
	    	{	
		    	yaExisteRegistro= asoDao.existeRegistroAsocioTipoServicio(con, this.getEsquemaTarifario().getCodigo(), this.getEsEsquemaTarifarioGeneral(), this.getTipoServicio().getAcronimo(), this.getTipoAsocio().getCodigo(), this.getServicio().getCodigo(), this.getActivoBoolean(), codigoInstitucion, false, -1);
		        if(yaExisteRegistro)
		        {
		            myFactory.abortTransaction(con);
		            return false;
		        }
	    	}    
	        insertado=asoDao.insertar(con, this.getEsquemaTarifario().getCodigo(), this.getEsEsquemaTarifarioGeneral(), this.getTipoServicio().getAcronimo(), this.getTipoAsocio().getCodigo(), this.getServicio().getCodigo(), this.getActivoBoolean(), codigoInstitucion);
	        if (!insertado)
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
	    return insertado;
	}
	
	/**
	 * metodo que modifica un asocio x tipo servicio en una transaccion
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */ 
	public boolean modificarAsocioTipoSertvicioTransaccional (Connection con, String estado, int codigoInstitucion) throws SQLException
	{
	    boolean insertados=false;
	    boolean yaExisteRegistro=false;
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
	    	//en caso de sea activo entonces  toca validar, de lo contrario lo inserta
	    	if(this.getActivoBoolean())
	    	{	
		        yaExisteRegistro= asoDao.existeRegistroAsocioTipoServicio(con, this.getEsquemaTarifario().getCodigo(), this.getEsEsquemaTarifarioGeneral(), this.getTipoServicio().getAcronimo(), this.getTipoAsocio().getCodigo(), this.getServicio().getCodigo(), this.getActivoBoolean(), codigoInstitucion, true, this.getCodigo());
		        if(yaExisteRegistro)
		        {
		            myFactory.abortTransaction(con);
		            return false;
		        }
	    	}    
	        insertados=asoDao.modificar(con,this.getCodigo(), this.getServicio().getCodigo(), this.getActivoBoolean()); 
	        if (!insertados)
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
	    return insertados;
	}
	
	/**
	 * Método que termina la transaccion
	 * @param con
	 * @throws SQLException
	 */
	public void terminarTransaccion(Connection con) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    myFactory.endTransaction(con);
	}
	
	  /**
     * @return Returns the activo.
     */
    public boolean getActivoBoolean() 
    {
        if(this.getActivo().trim().equals("true"))
            return true;
        else
            return false;
    }
    /**
     * @return Returns the activo.
     */
    public String getActivo() {
        return activo;
    }
    /**
     * @param activo The activo to set.
     */
    public void setActivo(String activo) {
        this.activo = activo;
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
     * @return Returns the tipoServicio.
     */
    public InfoDatos getTipoServicio() {
        return tipoServicio;
    }
    /**
     * @param tipoServicio The tipoServicio to set.
     */
    public void setTipoServicio(InfoDatos tipoServicio) {
        this.tipoServicio = tipoServicio;
    }
    /**
     * @return Returns the tipoAsocio.
     */
    public InfoDatos getTipoAsocio() {
        return tipoAsocio;
    }
    /**
     * @param tipoAsocio The tipoAsocio to set.
     */
    public void setTipoAsocio(InfoDatos tipoAsocio) {
        this.tipoAsocio = tipoAsocio;
    }
    /**
     * @return Returns the esEsquemaTarifarioGeneral.
     */
    public boolean getEsEsquemaTarifarioGeneral() {
        return esEsquemaTarifarioGeneral;
    }
    /**
     * @param esEsquemaTarifarioGeneral The esEsquemaTarifarioGeneral to set.
     */
    public void setEsEsquemaTarifarioGeneral(boolean esEsquemaTarifarioGeneral) {
        this.esEsquemaTarifarioGeneral = esEsquemaTarifarioGeneral;
    }
    /**
     * @return Returns the esquemaTarifario.
     */
    public InfoDatosInt getEsquemaTarifario() {
        return esquemaTarifario;
    }
    /**
     * @param esquemaTarifario The esquemaTarifario to set.
     */
    public void setEsquemaTarifario(InfoDatosInt esquemaTarifario) {
        this.esquemaTarifario = esquemaTarifario;
    }
}
