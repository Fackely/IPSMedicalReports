/*
 * @(#)MotivoAnulacionFacturas.java
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.InfoDatosInt;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.MotivoAnulacionFacturasDao;

/**
 * Clase para registrar los motivos de anulacion  de
 * las facturas
 *
 * @version 1.0, 19/11/2004
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class MotivoAnulacionFacturas
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static MotivoAnulacionFacturasDao motivoAnulacionFacturasDao;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(MotivoAnulacionFacturas.class);
    
	/** 
	 * Codigo - descripcion - Activo/Inactivo Motivo de anulacion
	 */
	private InfoDatosInt motivosAnulacion;
	
	/**
	 * Código de la institución a la cual pertenece
	 */
	private int codigoInstitucion;
	
    /**
     * limpiar e inicializar atributos de la clase
     *
     */
    public void reset ()
    {
        this.motivosAnulacion= new InfoDatosInt();
        this.codigoInstitucion=-1;
    }
    
	 /**
	  * Constructor vacio de la clase
	  *
	  */
	 public  MotivoAnulacionFacturas ()
	 {
	     this.reset (); 
	     this.init (System.getProperty("TIPOBD"));
	 }
	 
	 /**
	  * Constructor con todos los campos
	  * @param codigo
	  * @param descripcion
	  * @param activo
	  * @param codigoInstitucion
	  */
	 public MotivoAnulacionFacturas( int codigo, String descripcion, boolean activo, int codigoInstitucion)
	 {
	     this.motivosAnulacion = new InfoDatosInt(codigo, descripcion, activo);
	     this.codigoInstitucion=codigoInstitucion;
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
			motivoAnulacionFacturasDao = myFactory.getMotivoAnulacionFacturasDao();
			wasInited = (motivoAnulacionFacturasDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Método que obtiene todos los resultados de la tabla motivos_anulacion_facturas
	 * para mostrarlos en el listado y poder ingresar otros o modificarlos.
	 * @param con
	 * @return
	 */
	public HashMap listadoMotivos(Connection con)
	{
		motivoAnulacionFacturasDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivoAnulacionFacturasDao();
		HashMap map= null;
		try
		{
		    String[] colums={"codigo", "descripcion", "activo", "puedoborrar"};
			map=UtilidadBD.resultSet2HashMap(colums, motivoAnulacionFacturasDao.cargarMotivos(con, this.codigoInstitucion), true, true).getMapa();
		}
		catch(Exception e)
		{
			logger.warn("Error mundo  motivo de anulacion facturas" +e.toString());
			map=null;
		}
		return map;
	}
	
	/**
	 * Método que obtiene todos los resultados de la tabla motivos_anul_fact
	 * para mostrarlos en el consultar y en el resumen.
	 * @param con
	 * @return
	 */
	public Collection listadoMotivosCollection(Connection con)
	{
		motivoAnulacionFacturasDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivoAnulacionFacturasDao();
		Collection coleccion=null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(motivoAnulacionFacturasDao.cargarMotivos(con, this.codigoInstitucion));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo listadoMotivosCollection motivos anulacion de facturas " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}
	
	
	/**
	 * Inserta un motivo de anulacion de factura
	 * @param con
	 * @return
	 */
	public int insertarMotivosAnulacionFacturas(Connection con) 
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    int resp1=0;
	    try
	    {
			if (motivoAnulacionFacturasDao==null)
				throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (MotivoAnulacionFacturasDao - insertar )");
			//Iniciamos la transacción, si el estado es empezar
			boolean inicioTrans;
			inicioTrans=myFactory.beginTransaction(con);
			resp1=motivoAnulacionFacturasDao.insertar(con,this.getDescripcionMotivo(),this.getActivoMotivo(),this.getCodigoInstitucion());
			if (!inicioTrans||resp1<1  )
			{
			    myFactory.abortTransaction(con);
				return -1;
			}
			else
			{
			    myFactory.endTransaction(con);
			}
	    }	
		catch (SQLException e) 
		{
		    resp1=0;
        }	
		return resp1;
	}
	
	/**
	 * Método para modificar los motivios de anulacion de una factura
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int modificarMotivosAnulacionFacturas(Connection con) throws SQLException 
	{
			int resp=0;
			resp=motivoAnulacionFacturasDao.modificar(con,this.getDescripcionMotivo(), this.getActivoMotivo(), this.getCodigoMotivoAnulacion());
			return resp;
	}
	
	/**
	 * Retorna el código del motivo de anulacion facturas
	 * @return
	 */
	public int getCodigoMotivoAnulacion(){
		return motivosAnulacion.getCodigo();
	}
	/**
	 * Asigna el código del motivo anulacion facturas
	 * @param codigoMotivo
	*/
	public void setCodigoMotivoAnulacion(int codigoMotivo){
		this.motivosAnulacion.setCodigo(codigoMotivo);
	}
	/**
	 * Retorna la descripcion del motivo de anulacion
	 * @return
	 */
	public String  getDescripcionMotivo(){
		return motivosAnulacion.getDescripcion();
	}
	/**
	 * Asigna la descripcion del motivo de anulacion
	 * @param descripcionMotivo
	*/
	public void setDescripcionMotivoAnulacion(String descripcionMotivo){
		this.motivosAnulacion.setDescripcion(descripcionMotivo);
	}
	/**
	 * Retorna boolean activo/inactivo  motivo de anulacion de facturas
	 * @return
	 */
	public boolean  getActivoMotivo(){
		return motivosAnulacion.isActivo();
	}
	/**
	 * Asigna boolean activo/inactivo del motivo anulacion de facturas
	 * @param activoMotivo
	*/
	public void setActivoMotivo(boolean activoMotivo){
		this.motivosAnulacion.setActivo(activoMotivo);
	}
	/**
     * @return Returns the codigoInstitucion.
     */
    public int getCodigoInstitucion() {
        return codigoInstitucion;
    }
    /**
     * @param codigoInstitucion The codigoInstitucion to set.
     */
    public void setCodigoInstitucion(int codigoInstitucion) {
        this.codigoInstitucion = codigoInstitucion;
    }
    /**
     * @return Returns the motivosAnulacion.
     */
    public InfoDatosInt getMotivosAnulacion() {
        return motivosAnulacion;
    }
    /**
     * @param motivosAnulacion The motivosAnulacion to set.
     */
    public void setMotivosAnulacion(InfoDatosInt motivosAnulacion) {
        this.motivosAnulacion = motivosAnulacion;
    }
}
