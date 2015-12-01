
/*
 * Creado   7/12/2004
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

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.GeneracionExcepcionesFarmaciaDao;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Clase para el manejo de la información general de
 * generación de excepciones farmacia, información básica para tener en 
 * cuenta en diferentes módulos/funcionalidades.
 *
 * @version 1.0, 7/12/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 */
public class GeneracionExcepcionesFarmacia 
{
    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(GeneracionExcepcionesFarmacia.class);
	
	 /**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static GeneracionExcepcionesFarmaciaDao generacionDao;
	
	/**
	 * id de la cuenta
	 */
	private int idCuenta;
	
	/**
	 * # de solicitud
	 */
	private int numeroSolicitud;
	
	/**
	 * Código - Nombre Área
	 */
	private InfoDatosInt area;
	
	/**
	 * Código - Descripción del artículo
	 */
	private InfoDatosInt articulo;
	
	/**
	 * % No cubierto
	 */
	private double porcentajeNoCubierto;
	
	 /**
	  * limpiar e inicializar atributos de la clase
	  */
	public void reset ()
	{
	   this.idCuenta=0;
	   this.area= new InfoDatosInt();
	   this.articulo= new InfoDatosInt();
	   this.porcentajeNoCubierto=-1;
	   this.numeroSolicitud=0;
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
			    generacionDao = myFactory.getGeneracionExcepcionesFarmaciaDao();
				wasInited = (generacionDao != null);
			}
			return wasInited;
	}
 
 /**
  * Constructor de la clase
  *
  */
 public GeneracionExcepcionesFarmacia ()
 {
     this.reset ();  
     this.init (System.getProperty("TIPOBD"));
 }
 
 
 /**
  * Carga el Listado de las solicitudes  de la cuenta cuando son de
  * farmacia y != de centro_costo_externo, estado_med = Despachada - Admin,
  * estado_fact= pendiente - cargada 
  * @param con, Connection, conexión abierta con una fuente de datos
  * @param idCuenta, int, id de la cuenta
  * @param tipoConsulta, boolean true realiza la consulta con todos los porcentajes
  * 								false realiza la consulta con los porcentajes diferentes de cero 
  * @return ResulSet list
  * @see com.princetonsa.dao.SqlBaseGeneracionExcepcionesFarmaciaDao#listadoSolicitudesFarmacia(java.sql.Connection,int,boolean)
  */
 public HashMap listadoSolicitudesFarmacia(Connection con, int idCuenta)
 {
    HashMap mapa = null;
  
    try 
    {
     String[] colums={
	            "numeroSolicitud",
	            "orden",
	            "codigoArea", 
	            "nombreArea", 
	            "codigoArticulo",
	            "descripcionArticulo",
	            "porcentajeNoCubiertoGenerado",
	            "esNuloEnBD"
	            };
     mapa = UtilidadBD.resultSet2HashMap(colums,generacionDao.listadoSolicitudesFarmacia(con,idCuenta,true),true,true).getMapa();
	} 
    catch (Exception e) 
    {
		logger.warn("Error mundo MedicosXPool con la cuenta->"+idCuenta+" "+e.toString());
		mapa = null;
	}
    
    return mapa;
     
 }

 /**
  * Carga el Listado de las solicitudes  de la cuenta cuando son de
  * farmacia y != de centro_costo_externo, estado_med = Despachada - Admin,
  * estado_fact= pendiente - cargada 
  * @param con, Connection, conexión abierta con una fuente de datos
  * @param idCuenta, int, id de la cuenta
  * @param tipoConsulta, boolean true realiza la consulta con todos los porcentajes
  * 								false realiza la consulta con los porcentajes diferentes de cero 
  * @return ResulSet list
  * @see com.princetonsa.dao.SqlBaseGeneracionExcepcionesFarmaciaDao#listadoSolicitudesFarmacia(java.sql.Connection,int,boolean)
  */
 public HashMap listadoSolicitudesFarmaciaGeneradas(Connection con, int idCuenta)
 {
    HashMap mapa = null;
  
    try 
    {
     String[] colums={
	            "numeroSolicitud",
	            "orden",
	            "codigoArea", 
	            "nombreArea", 
	            "codigoArticulo",
	            "descripcionArticulo",
	            "porcentajeNoCubiertoGenerado",
	            "fecha",
	            "usuario"
	            };
     mapa = UtilidadBD.resultSet2HashMap(colums,generacionDao.listadoSolicitudesFarmacia(con,idCuenta,false),true,true).getMapa();
	} 
    catch (Exception e) 
    {
		logger.warn("Error mundo MedicosXPool con la cuenta->"+idCuenta+" "+e.toString());
		mapa = null;
	}
    
    return mapa;
     
 }

	/**
	 * (utilizado en la consulta- Collection) Método que obtiene todos los resultados de la búsqueda avanzada
	 * de la generación de excepciones farmacia 
	 * @param con
	 * @param boolean esBusquedaPorCodigo
	 * @return Collection
	 */
	public Collection busquedaGenExcepcionesFarmaciaCollection(Connection con, boolean esBusquedaPorCodigo)
	{
	    generacionDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionExcepcionesFarmaciaDao();
		Collection coleccion=null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(generacionDao.busqueda(con, this.getIdCuenta(),this.getCodigoArea(), this.getDescripcionArticulo(),esBusquedaPorCodigo,this.porcentajeNoCubierto ));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo búsqueda avanzada Collection  generación de excepciones farmacia " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}
	
	/**
	 * (Utilizado para ingresar - Modificar  )Método que obtiene los resultados de 
	 * la búsqueda avanzada para la generación de excepciones farmacia
	 * 
	 * @param con
	 * @param boolean esBusquedaPorCodigo
	 * @return HashMap
	 */
	public HashMap busquedaGenExcepcionesFarmaciaHashMap(Connection con, boolean esBusquedaPorCodigo)
	{
		generacionDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionExcepcionesFarmaciaDao();
		HashMap map= null;
		try
		{
		    String[] colums={
		            "numeroSolicitud",
		            "orden",
		            "codigoArea", 
		            "nombreArea", 
		            "codigoArticulo",
		            "descripcionArticulo",
		            "porcentajeNoCubiertoGenerado",
		            "esNuloEnBD"
		            };
			map=UtilidadBD.resultSet2HashMap(colums, generacionDao.busqueda(con, this.getIdCuenta(),this.getCodigoArea(), this.getDescripcionArticulo(),esBusquedaPorCodigo,this.porcentajeNoCubierto ), true,true).getMapa();
		}
		catch(Exception e)
		{
			logger.warn("Error mundo Búsqueda avanzadaHash Map generación excepciones farmacia " +e.toString());
			map=null;
		}
		
		return map;
	}
	
	/**
	 * Metodo para la consulta de excepciones de farmacia
	 * @param con
	 * @param codigoConvenio
	 * @param codigoCentroCosto
	 * @param codigoArticulo
	 * @return
	 */
	public ResultSetDecorator consultaExcepcionesFarmacia(Connection con, 
														        int codigoConvenio,
														        int codigoCentroCosto,
														        int codigoArticulo,
														        boolean existeExcepcion,
														        boolean consultaExcepcion)
	{
		generacionDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionExcepcionesFarmaciaDao();
		ResultSetDecorator rs= null;
		try
		{
		   
			rs= generacionDao.consultaExcepcionesFarmacia(con, codigoConvenio,codigoCentroCosto, codigoArticulo,existeExcepcion,consultaExcepcion);
		}
		catch(Exception e)
		{
			logger.warn("Error mundo consultaExcepcionesFarmacia Map generación excepciones farmacia " +e.toString());
			rs=null;
		}
		
		return rs;
	}
 
 /**
  * Metodo para consultar la generación de excepciones de farmacia
  * segun los parametros enviados.
  * @param con, Conecction con la fuente de datos
  * @param codigoPaciente, Código de la cuenta activa
  * @return col, Collection con la consulta
  */
 public Collection consultaExcepcionFarmaciaLinks (Connection con, int codigoPaciente)
 {
     Collection col = null;
     
     try
     {
         col = UtilidadBD.resultSet2Collection(generacionDao.linksConsultaGeneracionExcepcionesFarmacia(con,codigoPaciente));
     }
     catch(Exception e)
     {
         logger.warn("Error mundo GeneracionExcepcionesFarmacia con el paciente "+codigoPaciente+" "+e.toString());
 		 col = null;   
     }
     
     return col;
 }
 
	/**
	 * Mètodo que permite insertar una generación de Excepción de Farmacia 
	 * Recibe como parámetro
	 * el nivel de transaccionalidad con que se insertarán
	 * estos req 
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param estado Estado de esta operación dentro de la
	 * transacción
	 * @param login del usuario
	 * @return
	 * @throws SQLException
	 */
	public int insertarGenExcepcionesFarmaciaTransaccional (Connection con, String estado, String loginUsuario) throws SQLException
	{
	   int numElementosInsertados=0;
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
	       numElementosInsertados= generacionDao.insertar(con, this.getNumeroSolicitud(), this.getCodigoArticulo(), this.getPorcentajeNoCubierto(), loginUsuario);
	       
	       if (numElementosInsertados<=0)
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
	   return numElementosInsertados;
	}
 
	 /**
	 * Mètodo que permite modificar una generación de excepciones farmacia 
	 * Recibe como parámetro
	 * el nivel de transaccionalidad con que se insertarán
	 * estos req
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param estado Estado de esta operación dentro de la
	 * transacción
	 * @param loginUsuario
	 * @return
	 * @throws SQLException
	 */
	public int modificarGenExcepcionesFarmaciaTransaccional (Connection con, String estado, String loginUsuario) throws SQLException
	{
	    int numElementosInsertados=0;
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
	        numElementosInsertados=generacionDao.modificar(con,this.getNumeroSolicitud(), this.getCodigoArticulo(), this.getPorcentajeNoCubierto(), loginUsuario );
	        
	        if (numElementosInsertados<=0)
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
	    return numElementosInsertados;
	}
	
	 
	 /**
	 * Mètodo que permite eliminar una generación de exce. farmacia
	 * Recibe como parámetro
	 * el nivel de transaccionalidad con que se insertarán
	 * estos req 
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param estado Estado de esta operación dentro de la
	 * transacción
	 * @return
	 * @throws SQLException
	 */
	public int eliminarGenExcepcionesFarmaciaTransaccional (Connection con, String estado) throws SQLException
	{
	    int numElementosInsertados=0;
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
	        numElementosInsertados=generacionDao.eliminar(con, this.getNumeroSolicitud(), this.getCodigoArticulo());
	        
	        if (numElementosInsertados<=0)
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
	    return numElementosInsertados;
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
     * @return Returns the idCuenta.
     */
    public int getIdCuenta() {
        return idCuenta;
    }
    /**
     * @param idCuenta The idCuenta to set.
     */
    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }
    /**
     * @return Returns the area.
     */
    public InfoDatosInt getArea() {
        return area;
    }
    /**
     * @param area The area to set.
     */
    public void setArea(InfoDatosInt area) {
        this.area = area;
    }
    /**
	 * Retorna el código del área
	 * @return
	 */
	public int  getCodigoArea(){
		return this.area.getCodigo();
	}
	/**
	 * Asigna el código del área
	 * @param cod
	*/
	public void setCodigoArea(int cod){
		this.area.setCodigo(cod);
	}
	/**
	 * Retorna el nombre del área
	 * @return
	 */
	public String  getNombreArea(){
		return this.area.getNombre();
	}
	/**
	 * Asigna el nombre del área
	 * @param nombre
	*/
	public void setNombreArea(String  nombre){
		this.area.setNombre(nombre);
	}
    /**
	 * Retorna el código del articulo
	 * @return
	 */
	public int  getCodigoArticulo(){
		return this.articulo.getCodigo();
	}
	/**
	 * Asigna el código del articulo
	 * @param cod
	*/
	public void setCodigoArticulo(int cod){
		this.articulo.setCodigo(cod);
	}
	/**
	 * Retorna el Descripcion del articulo
	 * @return
	 */
	public String  getDescripcionArticulo(){
		return this.articulo.getDescripcion();
	}
	/**
	 * Asigna el Descripcion del articulo
	 * @param Descripcion
	*/
	public void setDescripcionArticulo(String  Descripcion){
		this.articulo.setDescripcion(Descripcion);
	}

    /**
     * @return Returns the articulo.
     */
    public InfoDatosInt getArticulo() {
        return articulo;
    }
    /**
     * @param articulo The articulo to set.
     */
    public void setArticulo(InfoDatosInt articulo) {
        this.articulo = articulo;
    }
    /**
     * @return Returns the porcentajeNoCubierto.
     */
    public double getPorcentajeNoCubierto() {
        return porcentajeNoCubierto;
    }
    /**
     * @param porcentajeNoCubierto The porcentajeNoCubierto to set.
     */
    public void setPorcentajeNoCubierto(double porcentajeNoCubierto) {
        this.porcentajeNoCubierto = porcentajeNoCubierto;
    }
    /**
     * @return Returns the numeroSolicitud.
     */
    public int getNumeroSolicitud() {
        return numeroSolicitud;
    }
    /**
     * @param numeroSolicitud The numeroSolicitud to set.
     */
    public void setNumeroSolicitud(int numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }
}
