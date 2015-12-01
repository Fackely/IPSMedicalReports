/*
 * @(#)Camas1.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.Camas1Dao;
import com.princetonsa.dao.DaoFactory;

/**
 * Clase para el manejo de camas1
 * @version 1.0, Junio 1, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class Camas1 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static Camas1Dao camas1Dao;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(Camas1.class);
	
	/**
	 * codigo  (axioma) de la cama
	 */
	private int codigo;
	
	/**
	 * Maneja el numero de habitacion
	 */
	private int habitacion;
	
	/**
	 * Nombre de la habitacion
	 */
	private String nombreHabitacion;
	
	/**
	 * Nombre del piso
	 */
	private String nombrePiso;
	
	/**
	 * Piso de la habitacion
	 */
	private String piso;
	
	/**
	 * Tipo de habitacion
	 */
	private String tipoHabitacion;
	
	
	/**
	 * Numero de la cama perteneciente a una habitacion
	 */
	private String numeroCama;
	
	/**
	 * descripcion de la cama perteneciente a una habitacion
	 */
	private String descripcionCama;

	/**
	 * codigo - nombre del estado de la cama
	 */
	private InfoDatosInt estadoCama;
	
	/**
	 * true false es uci (unidad cuidados intensivos)
	 */
	private boolean esUci;
	
	/**
	 * Cod de la institucion
	 */
	private int codigoInstitucion;
		
	/**
	 * cod - nombre de los tipo de usuario 
	 */
	private InfoDatosInt tipoUsuarioCama;
	
	/**
	 * codigo - nombre del centro de costo
	 */
	private InfoDatosInt centroCosto;
	
	/**
	 * Boolean que indica si se puede modificar el estado de la cama
	 */
	private boolean puedoModificarEstadoCama;
	
	/**
	 * Centro Atencion 
	 */
	private int centroAtencion;
	
	private String asignableAdmision="";
	
	public String getAsignableAdmision() {
		return asignableAdmision;
	}

	public void setAsignableAdmision(String asignableAdmision) {
		this.asignableAdmision = asignableAdmision;
	}

	/**
	 * Resetea todos los atributos del objeto
	 */
	public void reset()
	{
	    this.codigo=ConstantesBD.codigoNuncaValido;
	    this.habitacion=ConstantesBD.codigoNuncaValido;
	    this.nombreHabitacion = "";
	    this.piso = "";
	    this.nombrePiso = "";
	    this.tipoHabitacion = "";
	    this.numeroCama= "";
	    this.descripcionCama="";
	    this.estadoCama= new InfoDatosInt();
	    this.esUci= false;
	    this.codigoInstitucion= ConstantesBD.codigoNuncaValido;
	    this.tipoUsuarioCama= new InfoDatosInt();
	    this.centroCosto= new InfoDatosInt();
	    this.puedoModificarEstadoCama=false;
	    this.centroAtencion = 0;
	}
	
	/**
	 * Constructor de la clase, inicializa en vacio todos los parámetros
	 */
	public Camas1()
	{
		reset();
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
		    camas1Dao = myFactory.getCamas1Dao();
			wasInited = (camas1Dao != null);
		}
		return wasInited;
	}
	
	/**
	 * Método que obtiene todo el listado de camas1
	 * @param con
	 * @return
	 */
	public Collection listadoCamas1(Connection con)
	{
		camas1Dao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCamas1Dao();
		Collection coleccion=null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(camas1Dao.listadoCamas1(con, this.getCodigoInstitucion(),this.getCentroAtencion()));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo camas1 listadoCamas1 " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}
	
	/**
	 * 
	 * @param codigoInstitucion
	 * @param codigoCentroAtencion
	 * @return
	 */
	public static HashMap<Object, Object> listadoCamas1(int codigoInstitucion, int codigoCentroAtencion)
	{
		Connection con= UtilidadBD.abrirConexion();
		camas1Dao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCamas1Dao();
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		mapa.put("numRegistros", "0");
		try
		{
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(camas1Dao.listadoCamas1(con,codigoInstitucion, codigoCentroAtencion)));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			mapa= new HashMap<Object, Object>();
			mapa.put("numRegistros","0");
		}
		UtilidadBD.closeConnection(con);
		return mapa;
	}
	
	
	/**
	 * Carga el detalle de una cama1 dado el codigo (axioma)  de la cama 
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public boolean detalleCama1(Connection con) throws SQLException
	{
		ResultSetDecorator rs=camas1Dao.detalleCama1(con, this.getCodigo());
		
		if (rs.next())
		{
		    this.codigo=rs.getInt("codigo");
			this.habitacion= rs.getInt("habitacion");
			this.nombreHabitacion= rs.getString("nombreHabitacion");
			this.piso = rs.getString("piso");
			this.nombrePiso = rs.getString("nombrePiso");
			this.tipoHabitacion = rs.getString("tipoHabitacion");
			this.numeroCama=rs.getString("cama");
			this.descripcionCama=rs.getString("descripcionCama");
			this.estadoCama.setCodigo(rs.getInt("codigoEstado"));
			this.estadoCama.setNombre(rs.getString("nombreEstado"));
			this.puedoModificarEstadoCama= rs.getBoolean("esModificableEstado");
			this.tipoUsuarioCama.setCodigo(rs.getInt("codigoTipoUsurioCama"));
			this.tipoUsuarioCama.setNombre(rs.getString("nombreTipoUsuarioCama"));
			this.centroCosto.setCodigo(rs.getInt("codigoCentroCosto"));
			this.centroCosto.setNombre(rs.getString("nombreCentroCosto"));
			this.esUci=rs.getBoolean("esUciBool");
			this.centroAtencion = rs.getInt("centro_atencion");
			this.asignableAdmision= rs.getString("asignable_admision");
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Metodo que inserta una cama1 en una transaccion, es necesario instanciar los atributos de este objeto
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	  public boolean insertarCama1Transaccional (Connection con, String estado) throws SQLException
	  {
	      boolean elementosInsertados=false;
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
	    	  //MT 5309-metodo consulta sequencia cama y envia como parametro a insertar Cama1
	    	  this.codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_camas1");
	    	  elementosInsertados=camas1Dao.insertarCama1(con, this.codigo, this.getHabitacion(), this.getNumeroCama(), this.getDescripcionCama(), this.getEstadoCama().getCodigo(), this.getEsUci(), this.getCodigoInstitucion(), this.getTipoUsuarioCama().getCodigo(), this.getCentroCosto().getCodigo());
	          
	          if (!elementosInsertados)
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
	      return elementosInsertados;
	  }
	
  	/**
  	 * Metodo transaccional para  modificar camas1
  	 * @param con
  	 * @param estado
  	 * @return
  	 * @throws SQLException
  	 */
 	public boolean modificarCamas1Transaccional (Connection con, String estado) throws SQLException
	{
	    boolean elementosInsertados=false;
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
	        elementosInsertados=camas1Dao.modificarCama1(con,this.getHabitacion()+"", this.getDescripcionCama(), this.getEstadoCama().getCodigo(), this.getTipoUsuarioCama().getCodigo(), this.getCentroCosto().getCodigo(), this.getEsUci(), this.getCodigo(),this.getAsignableAdmision());
	        
	        if (!elementosInsertados)
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
	    return elementosInsertados;
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
	 * Método que contiene los resultados de la búsqueda de camas1,
	 * según los criterios dados en la búsqueda avanzada. 
	 * @param con
	 * @return
	 */
	public Collection busquedaAvanzada(Connection con, int esUciInt, String codigoServicio, String descripcionServicio)
	{
		logger.info("\n  busquedaAvanzada codigoServicio -->"+codigoServicio);
		/**********************************************************************
		 *  modifcado por tarea 56042
		 */
			int codigoSerTemp= ConstantesBD.codigoNuncaValido;
			if (UtilidadCadena.noEsVacio(codigoServicio))
				codigoSerTemp=Utilidades.obtenerCodigoServicio(con, codigoServicio, ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(this.getCodigoInstitucion()));
			logger.info("\n  busquedaAvanzada codigoServicio -->"+codigoSerTemp);
		/*
		 * 
		 *********************************************************************/
	  
		camas1Dao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCamas1Dao();
		Collection coleccion=null;
		try
		{	
		    coleccion=UtilidadBD.resultSet2Collection( camas1Dao.busquedaAvanzadaCama1(	con, 
				this.getHabitacion(), 
				this.getPiso(),
				this.getTipoHabitacion(),
				this.getNumeroCama(), 
				this.getDescripcionCama(), 
				this.getEstadoCama().getCodigo(), 
				this.getTipoUsuarioCama().getCodigo(), 
				esUciInt, codigoSerTemp,descripcionServicio, 
				this.getCodigoInstitucion(),
				this.getCentroCosto().getCodigo(),
				this.getCentroAtencion(),
				this.asignableAdmision));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo empresa " +e.toString());
			coleccion=null;
		}
		return coleccion;		
	}
	
	/**
	 * retorna el ultimo codigo insertado en la tabla camas1
	 * @param con
	 * @return
	 */
	public int retornarUltimoCodigoCamaInsertada(Connection con)
	{
		try
		{
			ResultSetDecorator rs=camas1Dao.cargarUltimaCodigoCamaInsertado(con);
			if(rs.next())
			{    
			    this.codigo=rs.getInt("codigo");
				return rs.getInt("codigo");
			}	
			else
				return ConstantesBD.codigoNuncaValido;
		}catch(SQLException e)
		{
			logger.warn("Error mundo camas1: " +e.toString());	
			return ConstantesBD.codigoNuncaValido;
		}
	}	
	
	/**
	 * Método implementado para verificar si ya existe una cama
	 * con la misma habitacion, numero de cama, institucion en el mismo
	 * de centro de atención.
	 * @param con
	 * @param habitacion
	 * @param numeroCama
	 * @param institucion
	 * @param centrocosto
	 * @return
	 */
	public static boolean existeCamaEnCentroAtencion(Connection con,int habitacion,String numeroCama,int institucion,int centroCosto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCamas1Dao().existeCamaEnCentroAtencion(con,habitacion,numeroCama,institucion,centroCosto);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static int obtenerCamaDadaCuenta(Connection con, String codigoCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCamas1Dao().obtenerCamaDadaCuenta(con, codigoCuenta);
	}
	
	
	/**
	 * Método que termina la transaccion
	 * @param con
	 * @throws SQLException
	 */
	public void abortarTransaccion(Connection con) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    myFactory.abortTransaction(con);
	}
	
    /**
     * @return Returns the centroCosto.
     */
    public InfoDatosInt getCentroCosto() {
        return centroCosto;
    }
    /**
     * @param centroCosto The centroCosto to set.
     */
    public void setCentroCosto(InfoDatosInt centroCosto) {
        this.centroCosto = centroCosto;
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
     * @return Returns the estadoCama.
     */
    public InfoDatosInt getEstadoCama() {
        return estadoCama;
    }
    /**
     * @param estadoCama The estadoCama to set.
     */
    public void setEstadoCama(InfoDatosInt estadoCama) {
        this.estadoCama = estadoCama;
    }
    /**
     * @return Returns the esUci.
     */
    public boolean getEsUci() {
        return esUci;
    }
    /**
     * @param esUci The esUci to set.
     */
    public void setEsUci(boolean esUci) {
        this.esUci = esUci;
    }
    /**
     * @return Returns the habitacion.
     */
    public int getHabitacion() {
        return habitacion;
    }
    /**
     * @param habitacion The habitacion to set.
     */
    public void setHabitacion(int habitacion) {
        this.habitacion = habitacion;
    }
    /**
     * @return Returns the tipoUsuarioCama.
     */
    public InfoDatosInt getTipoUsuarioCama() {
        return tipoUsuarioCama;
    }
    /**
     * @param tipoUsuario The tipoUsuario to set.
     */
    public void setTipoUsuarioCama(InfoDatosInt tipoUsuario) {
        this.tipoUsuarioCama = tipoUsuario;
    }
    /**
     * @return Returns the descripcionCama.
     */
    public String getDescripcionCama() {
        return descripcionCama;
    }
    /**
     * @param descripcionCama The descripcionCama to set.
     */
    public void setDescripcionCama(String descripcionCama) {
        this.descripcionCama = descripcionCama;
    }
    /**
     * @return Returns the numeroCama.
     */
    public String getNumeroCama() {
        return numeroCama;
    }
    /**
     * @param numeroCama The numeroCama to set.
     */
    public void setNumeroCama(String numeroCama) {
        this.numeroCama = numeroCama;
    }
    /**
     * @return Returns the puedoModificarEstadoCama.
     */
    public boolean getPuedoModificarEstadoCama() {
        return puedoModificarEstadoCama;
    }
    /**
     * @param puedoModificarEstadoCama The puedoModificarEstadoCama to set.
     */
    public void setPuedoModificarEstadoCama(boolean puedoModificarEstadoCama) {
        this.puedoModificarEstadoCama = puedoModificarEstadoCama;
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
	 * @return Returns the centroAtencion.
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the piso
	 */
	public String getPiso() {
		return piso;
	}

	/**
	 * @param piso the piso to set
	 */
	public void setPiso(String piso) {
		this.piso = piso;
	}

	/**
	 * @return the tipoHabitacion
	 */
	public String getTipoHabitacion() {
		return tipoHabitacion;
	}

	/**
	 * @param tipoHabitacion the tipoHabitacion to set
	 */
	public void setTipoHabitacion(String tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}

	/**
	 * @return the nombreHabitacion
	 */
	public String getNombreHabitacion() {
		return nombreHabitacion;
	}

	/**
	 * @param nombreHabitacion the nombreHabitacion to set
	 */
	public void setNombreHabitacion(String nombreHabitacion) {
		this.nombreHabitacion = nombreHabitacion;
	}

	/**
	 * @return the nombrePiso
	 */
	public String getNombrePiso() {
		return nombrePiso;
	}

	/**
	 * @param nombrePiso the nombrePiso to set
	 */
	public void setNombrePiso(String nombrePiso) {
		this.nombrePiso = nombrePiso;
	}

	
}
