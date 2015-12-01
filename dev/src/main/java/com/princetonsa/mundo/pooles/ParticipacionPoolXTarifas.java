/*
 * @(#)ParticipacionPoolXTarifas.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.pooles;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ParticipacionPoolXTarifasDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase para el manejo de la participación de pool X tarifas
 * @version 1.0, Junio 11, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class ParticipacionPoolXTarifas 
{
	/**
	 * DAO utilizado por el objeto parra acceder a la fuente de datos
	 */
	private static ParticipacionPoolXTarifasDao poolXTarDao = null;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ParticipacionPoolXTarifas.class);
	
	/**
	 * Código - Descripción del Pool
	 */
	private InfoDatosInt pool;
	
	private String convenio;
	
	private String convenioOld;
	
	/**
	 * Código - Nombre del esquema Tarifario
	 */
	private InfoDatosInt esquemaTarifario;
	
	/**
	 * % participación
	 */
	private double porcentajeParticipacion;
	
	
	/**
	 * 
	 */
	
	private double valorParticipacion;

	/**
	 * Cuenta contable por pagar al Pool
	 */
	private int cuentaPool;
	
	/**
	 * Cuenta contable por ingresos del pool
	 */
	private int cuentaInstitucion;
	
	/**
	 * 
	 */
	private int cuentaInstitucionAnterior;
	
	/**
	 * institucion
	 */
	private int institucion;
	
	/**
	 *usuario 
	 */
	private String usuario;
	
	public int getInstitucion() {
		return institucion;
	}


	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}


	


	/**
	 * Resetea todos los valores de este mundo
	 *
	 */
	public void reset()
	{
	    this.pool= new InfoDatosInt();
	    this.esquemaTarifario= new InfoDatosInt();
	    this.porcentajeParticipacion=0;
	    this.cuentaPool=ConstantesBD.codigoNuncaValido;
	    this.cuentaInstitucion=ConstantesBD.codigoNuncaValido;
	    this.cuentaInstitucionAnterior=ConstantesBD.codigoNuncaValido;
	    this.institucion=ConstantesBD.codigoNuncaValido;
	    this.usuario = "";
	    this.convenio = "";
	    this.convenioOld ="";
	    this.valorParticipacion=0;
	    
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
				poolXTarDao = myFactory.getParticipacionPoolXTarifaDao();
				wasInited = (poolXTarDao != null);
			}
			return wasInited;
	}
	
	/**
	 * Constructor con todos los atributos del objeto
	 * @param codigoPool
	 * @param descripcionPool
	 * @param codigoEsquemaTarifario
	 * @param nombreEsquemaTarifario
	 * @param porcentajeParicipacion
	 * @param cuentaPool
	 * @param cuentaInstitucion
	 */
	public ParticipacionPoolXTarifas(	int codigoPool, String descripcionPool, 
	        											int codigoEsquemaTarifario, String nombreEsquemaTarifario, 
	        											double porcentajeParicipacion, int cuentaPool, 
	        											int cuentaInstitucion, int cuentaInstitucionAnterior )
	{
	    this.pool = new InfoDatosInt(codigoPool,"", descripcionPool);
	    this.esquemaTarifario= new InfoDatosInt(codigoEsquemaTarifario, nombreEsquemaTarifario);
	    this.porcentajeParticipacion= porcentajeParicipacion;
	    this.cuentaPool= cuentaPool;
	    this.cuentaInstitucion= cuentaInstitucion;
	    this.cuentaInstitucionAnterior= cuentaInstitucionAnterior;
	    this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * Constructor vacio de la clase
	 *
	 */
	public ParticipacionPoolXTarifas()
	{
	    this.reset();
	    this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * (Utilizado solo para consulta - Búsqueda Avanzada) 
	 * Método que obtiene todos los resultados de la 
	 * participación de un pool por tarifas dado el cod del pool
	 * para mostrarlos en el listado en forma de collection
	 * @param con
	 * @return
	 */
	public Collection listadoParticipacionPoolXTarifas(Connection con)
	{
		poolXTarDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getParticipacionPoolXTarifaDao();
		Collection coleccion=null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(poolXTarDao.listadoParticipacionPoolXTarifa(con, this.getCodigoPool(),this.getInstitucion()));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo listado participacion pool X tarifa " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}
	
	/**
	 * (Utilizado para ingresar - Modificar - Eliminar )Método que obtiene todos los resultados de la participación de un pool x tarifas
	 * dado el cod del pool para mostrarlos en el listado en un HashMap y poder ingresar otros o modificarlos.
	 * @param con
	 * @return
	 */
	public HashMap listadoMapaParticipacionPoolXTarifas(Connection con)
	{
		poolXTarDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getParticipacionPoolXTarifaDao();
		HashMap map= null;
		try
		{
			//construccion de la Lista
		    String[] colums={"codigoEsquemaTarifario", "nombreEsquemaTarifario", "porcentajeParticipacion", "cuentaPool", "cuentaInstitucion", "cuentaInstitucionAnterior" ,"valorParticipacion",  "estaBD", "esEliminada" };
			map=UtilidadBD.resultSet2HashMap(colums, poolXTarDao.listadoParticipacionPoolXTarifa(con, this.getCodigoPool(),this.getInstitucion()), true, true).getMapa();
			map.put("esBusquedaAvanzada", "f");
		}
		catch(Exception e)
		{
			logger.warn("Error mundo Hash Map requisitos Paciente " +e.toString());
			map=null;
		}
		
		return map;
	}
	
	
	/**
	 * (Utilizado para ingresar - Modificar - Eliminar )Método que obtiene todos los resultados de la participación de un pool x tarifas
	 * dado el cod del pool para mostrarlos en el listado en un HashMap y poder ingresar otros o modificarlos.
	 * @param con
	 * @return
	 */
	public HashMap listadoMapaParticipacionPoolXConvenio(Connection con)
	{
		poolXTarDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getParticipacionPoolXTarifaDao();
		logger.info("entro a listadoMapaParticipacionPoolXConvenio");
		logger.info("institucion "+this.getInstitucion());
		logger.info("pool "+this.getCodigoPool());
		HashMap map= new HashMap();
		try
		{
		 
			
		    map.put("institucion", this.getInstitucion());
		    map.put("pool", this.getCodigoPool());
		    if (!this.getConvenio().equals(""))
		    	 map.put("convenio", this.getConvenio());
		    logger.info("el hashMap "+map);
		    map=poolXTarDao.consultarParticipacionPoolesXConvenio(con, map);
			map.put("esBusquedaAvanzada", "f");
		}
		catch(Exception e)
		{
			logger.warn("Error mundo Hash Map requisitos Paciente " +e.toString());
			map=null;
		}
		  logger.info("el hashMap al salir de listadoMapaParticipacionPoolXConvenio "+map);
		return map;
	}
	
	/**
	 * Mètodo que permite insertar una participación Pool X Tarifas. 
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
  public int insertarParticipacionPoolXTarifasTransaccional (Connection con, String estado) throws SQLException
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
          numElementosInsertados=poolXTarDao.insertar(con, this.getCodigoPool(), this.getCodigoEsquemaTarifario(), this.getPorcentajeParticipacion(), this.getCuentaPool(), this.getCuentaInstitucion(), this.getCuentaInstitucionAnterior(),this.getInstitucion(),this.valorParticipacion, this.getUsuario() );
          
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
	 * Mètodo que permite insertar una participación Pool X Convenio. 
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
public boolean insertarParticipacionPoolXConvenioTransaccional (Connection con, String estado) throws SQLException
{
    boolean numElementosInsertados=false;
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
    	HashMap parametros = new HashMap ();
   	 
    	parametros.put("codigoConvenio", this.getConvenio());
   	 	parametros.put("porcentajeParticipacion", this.getPorcentajeParticipacion());
   	 	parametros.put("cuentaPool", this.getCuentaPool());
   	 	parametros.put("cuentaInstitucion", this.getCuentaInstitucion());
   	 	parametros.put("usuario", this.getUsuario());
   	 	parametros.put("cuentaInstitucionAnterior", this.getCuentaInstitucionAnterior());
   	 	parametros.put("codigoPool", this.getCodigoPool());
   	 	parametros.put("institucion", this.getInstitucion());
   	 	parametros.put("valorParticipacion",this.getValorParticipacion());
   	 	
   	 	
        numElementosInsertados=poolXTarDao.insertarPoolXConvenio(con, parametros);
        
        if (numElementosInsertados!=true)
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
	 * Mètodo que permite modificar una participación Pool X Tarifas. 
	 * Recibe como parámetro
	 * el nivel de transaccionalidad con que se insertarán
	 * estos req, y el nuevo esquemaTarifario 
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param estado Estado de esta operación dentro de la
	 * transacción
	 * @return
	 * @throws SQLException
	 */
	public int modificarParticipacionPoolXTarifasTransaccional (Connection con, String estado, int codigoEsquemaTarifarioNuevo) throws SQLException
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
	        numElementosInsertados=poolXTarDao.modificar(con, codigoEsquemaTarifarioNuevo, this.getPorcentajeParticipacion(), this.getCuentaPool(), this.getCuentaInstitucion(), this.getCodigoPool(), this.getCodigoEsquemaTarifario(), this.getCuentaInstitucionAnterior(),this.getInstitucion(),this.getValorParticipacion(), this.usuario);
	       logger.info(" Valor Participacion------------------------------------------------------------------------------------------------------>"+this.getValorParticipacion());
	        
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
	
	
	
	public boolean modificarParticipacionPoolXconvenioTransaccional (Connection con, String estado) throws SQLException
	{
	    boolean numElementosInsertados=false;
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
	    	HashMap parametros = new HashMap ();
	 
	    	parametros.put("codigoConvenio", this.getConvenio());
	    	parametros.put("codigoConvenioOld", this.getConvenioOld());
	   	 	parametros.put("porcentajeParticipacion", this.getPorcentajeParticipacion());
	   	 	parametros.put("cuentaPool", this.getCuentaPool());
	   	 	parametros.put("cuentaInstitucion", this.getCuentaInstitucion());
	   	 	parametros.put("usuario", this.getUsuario());
	   	 	parametros.put("cuentaInstitucionAnterior", this.getCuentaInstitucionAnterior());
	   	 	parametros.put("codigoPool", this.getCodigoPool());
	   	 	parametros.put("institucion", this.getInstitucion());
	   	 	parametros.put("valorParticipacion",this.getValorParticipacion());
	   	 	
	   	    numElementosInsertados=poolXTarDao.ModificaConvenio(con, parametros);
	       
	        
	        if (numElementosInsertados!=true)
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
	 * Mètodo que permite eliminar una participación Pool X Tarifas. 
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
	public int eliminarParticipacionPoolXTarifasTransaccional (Connection con, String estado) throws SQLException
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
	        numElementosInsertados=poolXTarDao.eliminar(con, this.getCodigoPool(), this.getCodigoEsquemaTarifario(),this.getInstitucion());
	        
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
	 * Mètodo que permite eliminar una participación Pool X Convenio. 
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
	public int eliminarParticipacionPoolXConvenioTransaccional (Connection con, String estado) throws SQLException
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
	        numElementosInsertados=poolXTarDao.eliminarXConvenio(con, this.getCodigoPool(), Integer.parseInt(this.getConvenio()),this.getInstitucion());
	        
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
	 * (Utilizado para Consulta) Método que obtiene todos los resultados de la búsqueda avanzada
	 * de la participación de pool por Tarifas 
	 * para mostrarlos en el listado
	 * @param con
	 * @return
	 */
	public Collection busquedaAvanzadaPoolXTarifas(Connection con)
	{
		poolXTarDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getParticipacionPoolXTarifaDao();
		Collection coleccion=null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(poolXTarDao.busquedaAvanzadaPoolXTarifa(con, this.getCodigoPool(), this.getCodigoEsquemaTarifario(), this.getPorcentajeParticipacion(),this.getInstitucion()));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo búsqueda avanzada participacion pool X tarifa " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}
	
	/**
	 * (Utilizado para ingresar - Modificar - Eliminar )Método que obtiene los resultados de 
	 * la búsqueda avanzada para la participación de un pool x tarifas
	 * 
	 * @param con
	 * @return
	 */
	public HashMap busquedaAvanzadaIngresoPoolXTarifas(Connection con)
	{
		poolXTarDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getParticipacionPoolXTarifaDao();
		HashMap map= null;
		try
		{
		    String[] colums={"codigoEsquemaTarifario", "nombreEsquemaTarifario", "porcentajeParticipacion", "cuentaPool", "cuentaInstitucion", "cuentaInstitucionAnterior",  "estaBD", "esEliminada" };
			map=UtilidadBD.resultSet2HashMap(colums, poolXTarDao.busquedaAvanzadaPoolXTarifa(con, this.getCodigoPool(), this.getCodigoEsquemaTarifario(), this.getPorcentajeParticipacion(),this.getInstitucion()), true, true).getMapa();
			map.put("esBusquedaAvanzada", "t");
		}
		catch(Exception e)
		{
			logger.warn("Error mundo Hash Map pool X Tarifas " +e.toString());
			map=null;
		}
		
		return map;
	}
	
	
	/**
	 * (Utilizado para ingresar - Modificar - Eliminar ) Método que busca los codigos 
	 * de los esquemas tarifarios del pool que no han sido
	 * consultados en la búsqueda avanzada y son necesarios para la comparación de los
	 * mapas ya que el cod del esq hace parte del primary key, y este se valida en la forma.
	 * 
	 * @param con
	 * @return
	 */
	public HashMap datosNoCargadosConBusquedaAvanzada(Connection con)
	{
		poolXTarDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getParticipacionPoolXTarifaDao();
		HashMap map= null;
		try
		{
		    String[] colums={"codigoEsquemaTarifario"};
			map=UtilidadBD.resultSet2HashMap(colums, poolXTarDao.datosNoCargadosConBusquedaAvanzada(con, this.getCodigoPool(), this.getCodigoEsquemaTarifario(), this.getPorcentajeParticipacion()), true, true).getMapa();
		}
		catch(Exception e)
		{
			logger.warn("Error mundo Hash Map pool X Tarifas " +e.toString());
			map=null;
		}
		
		return map;
	}
	
	
    /**
     * @return Returns the cuentaInstitucion.
     */
    public int getCuentaInstitucion() {
        return cuentaInstitucion;
    }
    /**
     * @param cuentaInstitucion The cuentaInstitucion to set.
     */
    public void setCuentaInstitucion(int cuentaInstitucion) {
        this.cuentaInstitucion = cuentaInstitucion;
    }
    /**
     * @return Returns the cuentaPool.
     */
    public int getCuentaPool() {
        return cuentaPool;
    }
    /**
     * @param cuentaPool The cuentaPool to set.
     */
    public void setCuentaPool(int cuentaPool) {
        this.cuentaPool = cuentaPool;
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
    /**
     * @return Returns the pool.
     */
    public InfoDatosInt getPool() {
        return pool;
    }
    /**
     * @param pool The pool to set.
     */
    public void setPool(InfoDatosInt pool) {
        this.pool = pool;
    }
    /**
     * @return Returns the porcentajeParticipacion.
     */
    public double getPorcentajeParticipacion() {
        return porcentajeParticipacion;
    }
    /**
     * @param porcentajeParticipacion The porcentajeParticipacion to set.
     */
    public void setPorcentajeParticipacion(double porcentajeParticipacion) {
        this.porcentajeParticipacion = porcentajeParticipacion;
    }
	/**
	 * Retorna el código del pool
	 * @return
	 */
	public int  getCodigoPool(){
		return pool.getCodigo();
	}
	/**
	 * Asigna el código del pool
	 * @param codPool
	*/
	public void setCodigoPool(int codPool){
		this.pool.setCodigo(codPool);
	}
	/**
	 * Retorna la descripcion del pool
	 * @return
	 */
	public String  getDescripcionPool(){
		return pool.getDescripcion();
	}
	/**
	 * Asigna la descripción del pool
	 * @param descPool
	*/
	public void setDescripcionPool(String  descPool){
		this.pool.setDescripcion(descPool);
	}
	/**
	 * Retorna el código del esquema Tarifario
	 * @return
	 */
	public int  getCodigoEsquemaTarifario(){
		return this.esquemaTarifario.getCodigo();
	}
	/**
	 * Asigna el código del esquema tarifario
	 * @param cod
	*/
	public void setCodigoEsquemaTarifario(int cod){
		this.esquemaTarifario.setCodigo(cod);
	}
	/**
	 * Retorna el nombre del esquema Tarifario
	 * @return
	 */
	public String  getNombreEsquemaTarifario(){
		return this.esquemaTarifario.getNombre();
	}
	/**
	 * Asigna el nombre del esquema Tarifario
	 * @param nombre
	*/
	public void setNombreEsquemaTarifario(String  nombre){
		this.esquemaTarifario.setNombre(nombre);
	}

	/**
	 * Método que consulta un porcentaje de participación para un
	 * pool y un esquema tarifario específico
	 * @param con
	 * @param pool
	 * @param esquemaTarifario
	 */
	public static double consultarParticipacionPoolXTarifa(Connection con, int pool, int esquemaTarifario, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getParticipacionPoolXTarifaDao().consultarParticipacionPoolXTarifa(con, pool, esquemaTarifario,institucion);
	}


	/**
	 * @return the cuentaInstitucionAnterior
	 */
	public int getCuentaInstitucionAnterior() {
		return cuentaInstitucionAnterior;
	}


	/**
	 * @param cuentaInstitucionAnterior the cuentaInstitucionAnterior to set
	 */
	public void setCuentaInstitucionAnterior(int cuentaInstitucionAnterior) {
		this.cuentaInstitucionAnterior = cuentaInstitucionAnterior;
	}


	public String getUsuario() {
		return usuario;
	}


	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}


	public String getConvenio() {
		return convenio;
	}


	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}


	public String getConvenioOld() {
		return convenioOld;
	}


	public void setConvenioOld(String convenioOld) {
		this.convenioOld = convenioOld;
	}


	public void setValorParticipacion(double valorParticipacion) {
		this.valorParticipacion = valorParticipacion;
	}


	public double getValorParticipacion() {
		return valorParticipacion;
	}
	
}
