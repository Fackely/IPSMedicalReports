/*
 * RegistroPooles.java 
 * Autor		:  jarloc
 * Creado el	:  01-dic-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.mundo.pooles;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosStr;
import util.UtilidadBD;

import com.princetonsa.actionform.pooles.PoolesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.PoolesDao;


/**
 * descripcion de esta clase
 *
 * @version 1.0, 16-dic-2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
public class Pooles {

    
    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(PoolesForm.class);
	
    /**
	  * objeto de acceso a la fuente de datos
	  */
	 private static PoolesDao objectDao;
	 
	/**
	 * codigo de la institucion
	 */
	 private int institucion;
	 
	 /**
	  * codigo del pool 
	  */
	 private int codigo;
	 
	 /**
	  * nit del tercero responsable
	  */
	 private int nitResponsable;
	 
	 /**
	  * nombre del tercero responsable 
	  */
	 private String nombreResponsable; 
	 
	 /**
	  * descripcion del pool
	  */
	 private String descripcion;
	 
	 /**
	  * indica si el pool esta activo o inactivo
	  */
	 private int activo;
	 
	 /**
	  * almacena la cantidad de dias de vencimiento de una factura
	  */
	 private int diasVencFactura;

	 /**
	  * alamacena la cuenta contable
	  */
	 private int cuentaXpagar;
	 
	 
	 /**
	  * limpiar e inicializar atributos de la clase
	  *
	  */ 
   public void reset ()
   {
  	 this.institucion = 0;
  	 this.codigo = 0;
  	 this.nitResponsable = 0;
  	 this.nombreResponsable = ""; 
  	 this.descripcion = "";
  	 this.activo = 0;  	
  	 this.cuentaXpagar = ConstantesBD.codigoNuncaValido;
  	 this.diasVencFactura = ConstantesBD.codigoNuncaValido;
   }

   /**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
   public void init(String tipoBD)
   {
 		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

 		if (myFactory != null){
 			objectDao = myFactory.getPoolesDao();
 		}
	 }
    
   /**
    * Constructor de la clase
    *
    */
   public Pooles()
   {
    this.reset ();
    this.init(System.getProperty("TIPOBD"));
   }
   
   
   /**
	 * Metodo que realiza la consulta de uno ó varios registros de pooles.
	 * @param con, Connection con la fuente de datos.
	 * @param institucion, Codigo de la institución.	 
	 * @return map, HashMap con el resultado.
	 * @see com.princetonsa.dao.sqlbase.SqlBasePoolesDao#consultarRegPooles(java.sql.Connection,int)
	 */
public HashMap consultarRegPooles (Connection con,int institucion)
{
   objectDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPoolesDao();
   HashMap map= null;
    try 
	    {
        String[] colums={
		            "codigo", 
		            "descripcion", 
		            "tercero", 
		            "activo",
		            "nit",
		            "nombreResponsable",
		            "diasVencimiento",
		            "cuentaXPagar",
		            "descCuentaXPagar"
		            };
        map = UtilidadBD.resultSet2HashMap(colums,objectDao.consultaPooles(con,institucion),false,true).getMapa();
		} 
	    catch (Exception e) 
	    {
			logger.warn("Error mundo consultarRegPooles con el codigo institucion->"+institucion+" "+e.toString());
			map = null;
		}
	    
    return map;
}


/**
 * Metodo que realiza la consulta de pooles.
 * @param con, Connection con la fuente de datos.
 * @param institucion, Codigo de la institución.	 
 * @return col, Collection con el resultado de la consulta
 * @see com.princetonsa.dao.sqlbase.SqlBasePoolesDao#consultaPooles(java.sql.Connection,int)
 */
public Collection consultaPooles (Connection con,int institucion)
{
	objectDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPoolesDao();
	Collection col= null;
	try 
	    {
	      col = UtilidadBD.resultSet2Collection(objectDao.consultaPooles(con,institucion));
		} 
	    catch (Exception e) 
	    {
			logger.warn("Error mundo consultarRegPooles con el codigo institucion->"+institucion+" "+e.toString());
			col = null;
		}
	    
	return col;
}


/**
 * Metodo que realiza la consulta de uno ó varios registros de pooles.
 * @param con, Connection con la fuente de datos.
 * @return map, HashMap con el resultado.
 * @see com.princetonsa.dao.sqlbase.SqlBasePoolesDao#consultarTerceros(java.sql.Connection)
 */
public HashMap consultarTerceros (Connection con)
{
    objectDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPoolesDao();
    HashMap map= null;
    try 
	    {
	    String[] colums={
	            	"codigo",
		            "numeroIdentificacion", 
		            "tipoIdentificacion", 
		            "descripcion", 
		            "tipoRetencion",
		            "institucion",
		            "activo"
		            };
	    map = UtilidadBD.resultSet2HashMap(colums,objectDao.consultaTerceros(con),true,true).getMapa();
		} 
	    catch (Exception e) 
	    {
			logger.warn("Error mundo consultarTerceros "+e.toString());
			map = null;
		}
	    
	return map;
}

/**
 * Metodo que realiza la consulta para verificar si el pool
 * se encuentra relacionado con la funcionalidad de MedicosXPool 
 * @param con, Connection con la fuente de datos.
 * @param codigoPool, int codigo del Pool
 * @return boolean, true si hay relación, false de lo contrario
 * @see com.princetonsa.dao.SqlBasePoolesDao#existePoolToMedicosXPool(java.sql.Connection)
 */
public boolean existeRelacionPoolToMedicosXPool (Connection con, int codigoPool)
{
    boolean hayRelacion = false;
    ResultSetDecorator rs = null;
    
    objectDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPoolesDao();
    
    try 
    {
       rs = objectDao.existePoolToMedicosXPool(con,codigoPool);
       if(rs.next())
           hayRelacion = true;
	} 
    catch (Exception e) 
    {
		logger.warn("Error mundo existeRelacionPoolToMedicosXPool "+e.toString());
		hayRelacion = false;
	}
    
    return hayRelacion;
}

/**
 * Metodo para eliminar, modificar e insertar por medio de transacción
 * @param diasVencFactura 
 * @param con, Connection con la fuente de datos
 * @param descripcionPool, String
 * @param codigoResponsable, int 
 * @param activo, int 
 * @param codigoPool, int 
 * @param esInsertar, boolen
 * @param esModificar, boolean
 * @param esEliminar, boolean
 * @return boolean, true efectivo en cualquiera de los casos, false de lo contrario
 * @throws SQLException
 */
public boolean insertarModificarEliminarTransaccion (Connection con,
															        String descripcionPool,
															        int codigoResponsable,
															        int activo,
															        int codigoPool,
															        boolean esInsertar,
															        boolean esModificar,
															        boolean esEliminar) throws SQLException
{
    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

    boolean siInserto = false,sielimino = false,siModifico = false;
    
    if (objectDao == null) 
	{
		throw new SQLException(	"No se pudo inicializar la conexión con la fuente de datos (PoolesDao - insertarModificarEliminarTransaccion )");
	}
    
//  ****Iniciamos la transacción, si el estado es empezar
	boolean inicioTrans;

	if (objectDao.equals(ConstantesBD.inicioTransaccion)) 
	{
		inicioTrans = myFactory.beginTransaction(con);
	} 
	else 
	{
		inicioTrans = true;
	}
    
	if(esInsertar)
	{logger.info("cuentaXpagar:::"+cuentaXpagar);
	    siInserto =  objectDao.insertarPool(con,descripcionPool,codigoResponsable,activo, this.diasVencFactura, this.cuentaXpagar);
	    if (!inicioTrans || !siInserto) 
		{
	        myFactory.abortTransaction(con);
			return false;
		}
		
		return siInserto;
	}
	
	if(esEliminar)
	{
	    sielimino =  objectDao.eliminar(con,codigoPool);
	    if (!inicioTrans || !sielimino) 
		{
	        myFactory.abortTransaction(con);
			return false;
		}
		
		return sielimino;
	}
	
	if(esModificar)
	{
	    siModifico =  objectDao.modificar(con,descripcionPool,activo,codigoResponsable,codigoPool, this.diasVencFactura, this.cuentaXpagar);

	    if (!inicioTrans || !siModifico) 
		{
	        myFactory.abortTransaction(con);
			return false;
		}
		
		return siModifico;
	}
	
	myFactory.endTransaction(con);
	return false;    
}
	

/**
 * Metodo que realiza la consulta avanzada de uno ó varios registros de pooles.
 * para ingresar, modificar e ingresar
 * @param con, Connection con la fuente de datos.
 * @param descripcion, String con el nombre del pool
 * @param nombreResponsable, String con el nombre del responsable
 * @param nit, String con el número de identificación
 * @param activo, int 1 si es activo, 0 de lo contrario
 * @return map, HashMap con el resultado.
 * @see com.princetonsa.dao.sqlbase.SqlBasePoolesDao#consultaAvanzadaPooles(java.sql.Connection,String,String,String,int)
 */
	public HashMap consultaAvanzadaPooles (Connection con, String descripcion, String nombreResponsable, String nit, int activo)
	{
	objectDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPoolesDao();
	HashMap map= null;
	try 
	    {
	    String[] colums={
				            "codigo", 
				            "descripcion", 
				            "tercero", 
				            "activo",
				            "nit",
				            "nombreResponsable"				            
		            	};
	    map = UtilidadBD.resultSet2HashMap(colums,objectDao.consultaPoolesAvanzada(con,descripcion,nombreResponsable,nit,activo),false,true).getMapa();
		} 
	    catch (Exception e) 
	    {
			logger.warn(e+" Error mundo consultaAvanzadaPooles "+e.toString());
			map = null;
		}
	    
	return map;
}
	
	/**
	 * Metodo que realiza la busqueda avanzada de uno ó varios registros de pooles.
	 * para la Consulta
	 * @param con, Connection con la fuente de datos.
	 * @param descripcion, String con el nombre del pool
	 * @param nombreResponsable, String con el nombre del responsable
	 * @param nit, String con el número de identificación
	 * @param activo, int 1 si es activo, 0 de lo contrario
	 * @return col, Collection con el resultado.
	 * @see com.princetonsa.dao.sqlbase.SqlBasePoolesDao#consultaAvanzadaPoolesConsultar(java.sql.Connection,String,String,String,int)
	 */
	public Collection consultaAvanzadaPoolesConsultar (Connection con, String descripcion, String nombreResponsable, String nit, int activo)
	{
		objectDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPoolesDao();
		Collection col= null;
		try 
		    {
		      col = UtilidadBD.resultSet2Collection(objectDao.consultaPoolesAvanzada(con,descripcion,nombreResponsable,nit,activo));
			} 
		    catch (Exception e) 
		    {
				logger.warn(e+" Error mundo consultaAvanzadaPooles "+e.toString());
				col = null;
			}
		    
		return col;
	}
	
	
	
	/**
	 * 
	 * @param institucion
	 * @param activo
	 * @return
	 */
	public static ArrayList<InfoDatosStr> cargarPooles(final int institucion, boolean activo){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPoolesDao().cargarPooles(institucion, activo);
	}
	
	
    /**
     * @return Retorna activo.
     */
    public int getActivo() {
        return activo;
    }
    /**
     * @param activo Asigna activo.
     */
    public void setActivo(int activo) {
        this.activo = activo;
    }
    /**
     * @return Retorna codigo.
     */
    public int getCodigo() {
        return codigo;
    }
    /**
     * @param codigo Asigna codigo.
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    /**
     * @return Retorna descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * @param descripcion Asigna descripcion.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * @return Retorna institucion.
     */
    public int getInstitucion() {
        return institucion;
    }
    /**
     * @param institucion Asigna institucion.
     */
    public void setInstitucion(int institucion) {
        this.institucion = institucion;
    }
    /**
     * @return Retorna nitResponsable.
     */
    public int getNitResponsable() {
        return nitResponsable;
    }
    /**
     * @param nitResponsable Asigna nitResponsable.
     */
    public void setNitResponsable(int nitResponsable) {
        this.nitResponsable = nitResponsable;
    }
    /**
     * @return Retorna nombreResponsable.
     */
    public String getNombreResponsable() {
        return nombreResponsable;
    }

	/**
	 * @return the diasVencFactura
	 */
	public int getDiasVencFactura() {
		return diasVencFactura;
	}

	/**
	 * @param diasVencFactura the diasVencFactura to set
	 */
	public void setDiasVencFactura(int diasVencFactura) {
		this.diasVencFactura = diasVencFactura;
	}

	/**
	 * @param cuentaXpagar the cuentaXpagar to set
	 */
	public void setCuentaXpagar(int cuentaXpagar) {
		this.cuentaXpagar = cuentaXpagar;
	}

	/**
	 * @return the cuentaXpagar
	 */
	public int getCuentaXpagar() {
		return cuentaXpagar;
	}    
   
}
