
/*
 * Creado   19/11/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import util.ConstantesBD;
import util.InfoDatos;
import util.RespuestaHashMap;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.RequisitosPacienteDao;

/**
 * Clase para manejar informaci&oacute;n correspondiente 
 * a requisitos de paciente o radicacion para cada convenio, .
 *
 * @version 1.0, 19/11/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class RequisitosPacientesXConvenio 
{
    /**
     * convenio que se encuente en el sistema
     * (C&oacute;digo y descripci&oacute;n)
     */
    private InfoDatos convenio;
    
    /**
     * almacena requiisitos previamente parametrizados.
     */
    private InfoDatos requisito;
    
    private InfoDatos viaIngreso;
    
	/**
	 * DAO usado por RequisitosPacienteXConvenio para acceder a la fuente de datos.
	 */
	private RequisitosPacienteDao requisitosPacienteDao;

    
    /**
     * limpiar e inicializar atributos de la clase
     *
     */
    public void reset ()
    {
        this.convenio = new InfoDatos ();
        this.requisito = new InfoDatos ();
        this.viaIngreso = new InfoDatos ();
		this.init(System.getProperty("TIPOBD"));
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

		if (myFactory != null) {
			requisitosPacienteDao = myFactory.getRequisitosPacienteDao();
			wasInited = (requisitosPacienteDao != null);
		}

		return wasInited;

	}

   /**
    * Constructor de la clase
    *
    */
   public RequisitosPacientesXConvenio ()
   {
       this.reset ();       
   }
   
	/**
	 * Mètodo que permite insertar un nuevo requisito
	 * exigido por un convenio. 
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
   public int insertarRequisitoPacienteConvenio (Connection con) throws SQLException
   {
       return requisitosPacienteDao.insertarRequisitoPacienteConvenio (con, this.requisito.getCodigo(), this.convenio.getCodigo(), this.viaIngreso.getCodigo()); 
   }
   
	/**
	 * Mètodo que permite insertar un nuevo requisito
	 * exigido por un convenio. 
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	  public int insertarRequisitoRadicacionConvenio (Connection con) throws SQLException
	  {
	      return requisitosPacienteDao.insertarRequisitoRadicacionConvenio (con, this.requisito.getCodigo(), this.convenio.getCodigo()); 
	  }
   
	/**
	 * Mètodo que permite insertar un nuevo requisito
	 * exigido por un convenio. Recibe como parámetro
	 * el nivel de transaccionalidad con que se insertarán
	 * estos convenios 
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param estado Estado de esta operación dentro de la
	 * transacción
	 * @return
	 * @throws SQLException
	 */
  public int insertarRequisitoPacienteConvenioTransaccional (Connection con, String estado) throws SQLException
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
          numElementosInsertados=requisitosPacienteDao.insertarRequisitoPacienteConvenio (con, this.requisito.getCodigo(), this.convenio.getCodigo(),this.viaIngreso.getCodigo());
          if (numElementosInsertados<=0)
          {
              myFactory.abortTransaction(con);
          }
      }
      catch (SQLException e)
      {
          myFactory.abortTransaction(con);
      }
      if (estado.equals(ConstantesBD.finTransaccion))
      {
          myFactory.endTransaction(con);
      }
      return numElementosInsertados;
  }
   
	/**
	 * Mètodo que permite insertar un nuevo requisito radicacion
	 * exigido por un convenio. Recibe como parámetro
	 * el nivel de transaccionalidad con que se insertarán
	 * estos convenios 
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param estado Estado de esta operación dentro de la
	 * transacción
	 * @return
	 * @throws SQLException
	 */
	public int insertarRequisitoRadicacionConvenioTransaccional (Connection con, String estado) throws SQLException
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
	        numElementosInsertados=requisitosPacienteDao.insertarRequisitoRadicacionConvenio (con, this.requisito.getCodigo(), this.convenio.getCodigo());
	        if (numElementosInsertados<=0)
	        {
	            myFactory.abortTransaction(con);
	        }
	    }
	    catch (SQLException e)
	    {
	        myFactory.abortTransaction(con);
	    }
	    if (estado.equals(ConstantesBD.finTransaccion))
	    {
	        myFactory.endTransaction(con);
	    }
	    return numElementosInsertados;
	}
  
	/**
	 * Método que permite eliminar un requisito exigido
	 * por un convenio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoRequisitoPaciente Código del requisito
	 * @param codigoConvenio Código del convenio
	 * @return
	 * @throws SQLException
	 */
	public int eliminarRequisitoPacienteConvenio (Connection con) throws SQLException
	{
	    return requisitosPacienteDao.eliminarRequisitoPacienteConvenio (con, this.requisito.getCodigo(), this.convenio.getCodigo(), this.viaIngreso.getCodigo());
	}

	/**
	 * Método que permite eliminar un requisito radicacion exigido
	 * por un convenio
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoRequisitoRadicacion Código del requisito
	 * @param codigoConvenio Código del convenio
	 * @return
	 * @throws SQLException
	 */
	public int eliminarRequisitoRadicacionConvenio (Connection con) throws SQLException
	{
	    return requisitosPacienteDao.eliminarRequisitoRadicacionConvenio (con, this.requisito.getCodigo(), this.convenio.getCodigo());
	}
	
	/**
	 * Método que permite eliminar un requisito exigido
	 * por un convenio. Recibe como parámetro
	 * el nivel de transaccionalidad con que se insertarán
	 * estos convenios 
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param estado Estado de esta operación dentro de la
	 * transacción
	 * @param codigoRequisitoPaciente Código del requisito
	 * @param codigoConvenio Código del convenio
	 * @return
	 * @throws SQLException
	 */
	
	  public int eliminarRequisitoPacienteConvenioTransaccional (Connection con, String estado) throws SQLException
	  {
	      int numElementosEliminados=0;
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
	          numElementosEliminados=requisitosPacienteDao.eliminarRequisitoPacienteConvenio (con, this.requisito.getCodigo(), this.convenio.getCodigo(),this.viaIngreso.getCodigo());
	          if (numElementosEliminados<=0)
	          {
	              myFactory.abortTransaction(con);
	          }
	      }
	      catch (SQLException e)
	      {
	          myFactory.abortTransaction(con);
	      }
	      if (estado.equals(ConstantesBD.finTransaccion))
	      {
	          myFactory.endTransaction(con);
	      }
	      return numElementosEliminados;
	  }
	  
	/**
	 * Método que permite eliminar un requisito radicacion exigido
	 * por un convenio. Recibe como parámetro
	 * el nivel de transaccionalidad con que se insertarán
	 * estos convenios 
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param estado Estado de esta operación dentro de la
	 * transacción
	 * @param codigoRequisitoRadicacion Código del requisito
	 * @param codigoConvenio Código del convenio
	 * @return
	 * @throws SQLException
	 */
	  public int eliminarRequisitoRadicacionConvenioTransaccional (Connection con, String estado) throws SQLException
	  {
	      int numElementosEliminados=0;
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
	          numElementosEliminados=requisitosPacienteDao.eliminarRequisitoRadicacionConvenio (con, this.requisito.getCodigo(), this.convenio.getCodigo());
	          if (numElementosEliminados<=0)
	          {
	              myFactory.abortTransaction(con);
	          }
	      }
	      catch (SQLException e)
	      {
	          myFactory.abortTransaction(con);
	      }
	      if (estado.equals(ConstantesBD.finTransaccion))
	      {
	          myFactory.endTransaction(con);
	      }
	      return numElementosEliminados;
	  }
	  
	/**
	 * Método que consulta todos los requisitos de paciente
	 * para un convenio y devuelve el resultado en una
	 * colección
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoConvenio Código del convenio 
	 * @return
	 * @throws SQLException
	 */
	public Collection consultarRequisitoPacienteConvenioCollection(Connection con) throws SQLException
	{
	    return UtilidadBD.resultSet2Collection(requisitosPacienteDao.consultarRequisitoPacienteConvenio(con, this.convenio.getCodigo())) ;
	}

	/**
	 * Método que consulta todos los requisitos radicacion
	 * para un convenio y devuelve el resultado en una
	 * colección
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoConvenio Código del convenio 
	 * @return
	 * @throws SQLException
	 */
	public Collection consultarRequisitoRadicacionConvenioCollection(Connection con) throws SQLException
	{
	    return UtilidadBD.resultSet2Collection(requisitosPacienteDao.consultarRequisitoRadicacionConvenio(con, this.convenio.getCodigo())) ;
	}
	
	/**
	 * Método que actualiza un HashMap con los
	 * requisitos de paciente para un convenio
	 * 
	 * @param mapaAntiguo
	 * @param con
	 * @throws SQLException
	 */
	public void actualizarHashMapRequisitoPacienteConvenio(RespuestaHashMap mapaAntiguo, Connection con) throws SQLException
	{
	    String nombreColumnas[]={"codigoRequisito", "requisito","tipoRequisito", "codigoConvenio", "convenio", "vieneBD", "eliminar", "viaIngreso"};
	    
	    mapaAntiguo.setTamanio(UtilidadBD.resultSet2HashMap(mapaAntiguo, nombreColumnas, requisitosPacienteDao.consultarRequisitoPacienteConvenio(con, this.convenio.getCodigo()), false, true)) ;
	}

	/**
	 * Método que actualiza un HashMap con los
	 * requisitos radicacion para un convenio
	 * 
	 * @param mapaAntiguo
	 * @param con
	 * @throws SQLException
	 */
	public void actualizarHashMapRequisitoRadicacionConvenio(RespuestaHashMap mapaAntiguo, Connection con) throws SQLException
	{
	    String nombreColumnas[]={"codigoRequisito", "requisito","tipoRequisito", "codigoConvenio", "convenio", "vieneBD", "eliminar"};
	    
	    mapaAntiguo.setTamanio(UtilidadBD.resultSet2HashMap(mapaAntiguo, nombreColumnas, requisitosPacienteDao.consultarRequisitoRadicacionConvenio(con, this.convenio.getCodigo()), false, true)) ;
	}
	
	/**
	 * Método que actualiza un hash map con todos los 
	 * requisitos no usados por un convenio. Devuelve
	 * el número de elementos encontrados. NO actualiza
	 * el tamaño en el objeto RespuestaHashMap
	 * 
	 * @param mapaAntiguo mapa a actualizar con los
	 * datos encontrados
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public int actualizarHashMapRequisitosNoUtilizadosPorConvenio (RespuestaHashMap mapaAntiguo, Connection con, String codigoInstitucion) throws SQLException
	{
	    String nombreColumnas[]={"codigoReqNoUsado", "reqNoUsado", "tipoReqNoUsado"};
	    
	    return UtilidadBD.resultSet2HashMap(mapaAntiguo, nombreColumnas, requisitosPacienteDao.obtenerRequisitosNoUtilizadosPorConvenio(con, this.convenio.getCodigo(), codigoInstitucion), false, true ) ;
	}

	/**
	 * Método que actualiza un hash map con todos los 
	 * requisitos no usados por un convenio. Devuelve
	 * el número de elementos encontrados. NO actualiza
	 * el tamaño en el objeto RespuestaHashMap
	 * 
	 * @param mapaAntiguo mapa a actualizar con los
	 * datos encontrados
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public int actualizarHashMapRequisitosRadicacionNoUtilizadosPorConvenio (RespuestaHashMap mapaAntiguo, Connection con, String codigoInstitucion) throws SQLException
	{
	    String nombreColumnas[]={"codigoReqNoUsado", "reqNoUsado", "tipoReqNoUsado"};
	    
	    return UtilidadBD.resultSet2HashMap(mapaAntiguo, nombreColumnas, requisitosPacienteDao.obtenerRequisitosRadicacionNoUtilizadosPorConvenio(con, this.convenio.getCodigo(), codigoInstitucion), false, true ) ;
	}
	
    /**
     * @return Retorna convenio.
     */
    public InfoDatos getConvenio() {
        return convenio;
    }
    /**
     * @param convenio Asigna convenio.
     */
    public void setConvenio(InfoDatos convenio) {
        this.convenio = convenio;
    }
    /**
     * @return Retorna requisito.
     */
    public InfoDatos getRequisito() {
        return requisito;
    }
    /**
     * @param requisito Asigna requisito.
     */
    public void setRequisito(InfoDatos requisito) {
        this.requisito = requisito;
    }
    
    /**
	 * Método que consulta los requisitos del paciente por convenio segun el tipo
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> cargarRequisitosPacienteXConvenio(Connection con,int codigoConvenio, String tipoRequisito,boolean activo, int institucion,int codigoViaIngreso)
	{
		HashMap campos = new HashMap();
		campos.put("codigoConvenio",codigoConvenio+"");
		campos.put("tipoRequisito", tipoRequisito);
		campos.put("institucion", institucion);
		campos.put("activo", activo);
		campos.put("codigoViaIngreso", codigoViaIngreso);
		return requisitoDao().cargarRequisitosPacienteXConvenio(con, campos);
	}
	
	/**
	 * Obtener el DAO de RequisitosPacienteDao
	 * @return
	 */
	public static RequisitosPacienteDao requisitoDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRequisitosPacienteDao();
	}

	/**
	 * @return the viaIngreso
	 */
	public InfoDatos getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(InfoDatos viaIngreso) {
		this.viaIngreso = viaIngreso;
	}
}
