/*
 * @(#)ConsultaFacturas.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.mundo;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TrasladoCamasDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Objeto que maneja la interacción entre la capa
 * de control y el acceso a la información de la 
 * Traslado de Camas
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 06 /Jul/ 2005
 */
public class TrasladoCamas
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(TrasladoCamas.class);
	
	/**
	 * Numero de la cuenta del paciente cargado
	 */
	private int cuenta;
	
	/**
	 * String de la via de Ingreso
	 */
	private String viaIngreso;
	
	/**
	 * Codigo del paciente cargado en session
	 */
	private int codigoPaciente;
	
	/**
	 * Responsable de la cuenta
	 */
	private String convenio;
	
	
	/**
	 * Entero para la institucion del usuario cargado en session
	 */
	private int institucion;
	
	/**
	 * Entro con el numero de la ultima cama asiganda al paciente
	 */
	private int nuevaCama;
	
	/**
	 * String para la fecha del Traslado cuando se consulta por fecha
	 */
	private String fechaTraslado;
	
	
	
	////////////////////////////DATOS INSERTADOS WILSON
	
	/**
	 * fecha de admision del paciente a Hospitalizacion,
	 * o en su defecto la fecha de traslado a nueva cama
	 */
	private String fechaAsignacion;
	
	/**
	 * hora de admision del paciente a Hospitalizacion,
	 * o en su defecto la hora de traslado a nueva cama
	 */
	private String horaAsignacion;
	
	/**
	 * convenio del paciente
	 */
	private InfoDatosInt convenioPaciente;
	
	/**
	 * fecha finalizacion del uso de la cama
	 */
	private String fechaFinalizacion;
	
	/**
	 * hora finalizacion del uso de la cama
	 */
	private String horaFinalizacion;
	
	/**
	 * codigo de la nuevaCama
	 */
	private int codigoNuevaCama;
	
	/**
	 * codigo de la camaAntigua
	 */
	private int codigoCamaAntigua;
	
	
	/**
	 * reset de los atributos del objeto
	 *
	 */
	public void resetMundo()
	{
	    this.fechaAsignacion="";
	    this.horaAsignacion="";
	    this.convenioPaciente= new InfoDatosInt();
	    this.fechaFinalizacion="";
	    this.horaFinalizacion="";
	    this.codigoNuevaCama=ConstantesBD.codigoNuncaValido;
	    this.codigoCamaAntigua= ConstantesBD.codigoNuncaValido;
	    
	    this.institucion= ConstantesBD.codigoNuncaValido;
	 	this.codigoPaciente=	ConstantesBD.codigoNuncaValido;;
		this.cuenta=ConstantesBD.codigoNuncaValido;
	}
	
	/////////////////////////////////////////////////////////////////////////
	
	
    /**
     * Constructor del objeto
     * (Solo inicializa el acceso a la 
     * fuente de datos)
     */
    public TrasladoCamas()
    {
        this.resetMundo();
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>trasladoCamasDao</code> 
	 * para acceder a la fuente de datos. 
	 */
	private TrasladoCamasDao trasladoCamasDao ;

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
			trasladoCamasDao = myFactory.getTrasladoCamasDao();
			wasInited = (trasladoCamasDao != null);
		}

		return wasInited;
	}
	
	/**
	 * @return Returns the fechaTraslado.
	 */
	public String getFechaTraslado()
	{
		return fechaTraslado;
	}
	/**
	 * @param fechaTraslado The fechaTraslado to set.
	 */
	public void setFechaTraslado(String fechaTraslado)
	{
		this.fechaTraslado= fechaTraslado;
	}
	/**
	 * @return Returns the nuevaCama.
	 */
	public int getNuevaCama()
	{
		return nuevaCama;
	}
	/**
	 * @param nuevaCama The nuevaCama to set.
	 */
	public void setNuevaCama(int nuevaCama)
	{
		this.nuevaCama= nuevaCama;
	}
	/**
	 * @return Returns the institucion.
	 */
	public int getInstitucion()
	{
		return institucion;
	}
	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(int institucion)
	{
		this.institucion= institucion;
	}
	/**
	 * @return Returns the codigoPaciente.
	 */
	public int getCodigoPaciente()
	{
		return codigoPaciente;
	}
	/**
	 * @param codigoPaciente The codigoPaciente to set.
	 */
	public void setCodigoPaciente(int codigoPaciente)
	{
		this.codigoPaciente= codigoPaciente;
	}
	
	/**
	 * @return Returns the convenio.
	 */
	public String getConvenio()
	{
		return convenio;
	}
	/**
	 * @param convenio The convenio to set.
	 */
	public void setConvenio(String convenio)
	{
		this.convenio= convenio;
	}
	/**
	 * @return Returns the cuenta.
	 */
	public int getCuenta()
	{
		return cuenta;
	}
	/**
	 * @param cuenta The cuenta to set.
	 */
	public void setCuenta(int cuenta)
	{
		this.cuenta= cuenta;
	}
	
	/**
	 * @return Returns the viaIngreso.
	 */
	public String getViaIngreso()
	{
		return viaIngreso;
	}
	/**
	 * @param viaIngreso The viaIngreso to set.
	 */
	public void setViaIngreso(String viaIngreso)
	{
		this.viaIngreso= viaIngreso;
	}
	
	/**
	 * Método que carga la cama actual dado el Codigo del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 * @throws SQLException
	 */
	public HashMap cargarCamaActualPaciente (Connection con, int codigoPaciente) 
	{
		trasladoCamasDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTrasladoCamasDao();
		HashMap map= null;
		try
		{
		    String[] colums={"codigo","habitacion","nombre_habitacion","tipo_habitacion","piso", "cama", "descripcionCama", "tipoUsuario", "centroCosto", "esUci","fecha_traslado","hora_traslado","tipo_monitoreo"};
		    		
			map=UtilidadBD.resultSet2HashMap(colums, trasladoCamasDao.cargarCamaActualPaciente(con, codigoPaciente), true, true).getMapa();
		}
		catch(Exception e)
		{
			logger.warn("Error mundo TrasladoCamas.java Cargando la cama actual Por Paciente" +e.toString());
			map=null;
		}
		return map;
	}
	
	
	/**
	 * Método que carga la cama reservada dado el Codigo del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 * @throws SQLException
	 */
	public HashMap cargarCamaReservadaPaciente (Connection con, int codigoPaciente, int centroAtencion) 
	{
		HashMap map= null;
		try
		{
		   map=UtilidadesManejoPaciente.consultarCamaReservada(con, codigoPaciente+"", centroAtencion+"");
		}
		catch(Exception e)
		{
			logger.warn("Error mundo TrasladoCamas.java Cargando la cama Reservada del Paciente" +e.toString());
			map=null;
		}
		return map;
	}
	
	/**
	 * Método para la insercion de los datos de la nueva cama asiganda al paciente
	 * @param con
	 * @param fecha
	 * @param hora
	 * @param nuevaCama
	 * @param nuevaHabitacion
	 * @param camaAntigua
	 * @param habitacionAntigua
	 * @param institucion
	 * @param fechaGrabacion
	 * @param horaGrabacion
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	public int insertarTrasladoCamaPaciente(Connection con, String fecha_asignacion, String hora_asignacion, int codigoNuevaCama,  int codigoCamaAntigua, int institucion, String usuario, int codigoPaciente, int cuenta, int convenio, String observacion) throws SQLException 
	{
		return trasladoCamasDao.insertarTrasladoCamaPaciente(con, fecha_asignacion, hora_asignacion, codigoNuevaCama, codigoCamaAntigua, institucion, usuario, codigoPaciente, cuenta, convenio, observacion);
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
	  public int insertarTrasladoCamaTransaccional (Connection con, String loginUsuario, String estado) 
	  {
	      int numElementosInsertados=0;
	      if (estado.equals(ConstantesBD.inicioTransaccion))
	      {
	          if (!UtilidadBD.iniciarTransaccion(con))
	          {
	              UtilidadBD.abortarTransaccion(con);
	          }
	      }
	      try
	      {
	          numElementosInsertados=trasladoCamasDao.insertarTrasladoCama(	con, 
	                  																								this.getFechaAsignacion(), 
	                  																								this.getHoraAsignacion(), 
	                  																								this.getInstitucion(), 
	                  																								loginUsuario, 
	                  																								this.getCodigoPaciente(), 
	                  																								this.getConvenioPaciente().getCodigo(), 
	                  																								this.getFechaFinalizacion(), 
	                  																								this.getHoraFinalizacion(), 
	                  																								this.getCuenta(), 
	                  																								this.getCodigoNuevaCama(), 
	                  																								this.codigoCamaAntigua);
	          if (numElementosInsertados<=0)
	          {
	              UtilidadBD.abortarTransaccion(con);
	          }
	      }
	      catch (SQLException e)
	      {
	          UtilidadBD.abortarTransaccion(con);
	          numElementosInsertados = 0;
	          logger.error("Error al insertar el traslado de cama " + e);
	      }
	      
	      if (estado.equals(ConstantesBD.finTransaccion))
	      {
	          UtilidadBD.finalizarTransaccion(con);
	      }
	      return numElementosInsertados;
	  }
	
  
	/**
	 * Método que actualiza la fecha y hora de finalizacion de la estancia de un paciente en una cama
	 * @param con
	 * @param codigoCuenta
	 * @param fechaFinalizacion
	 * @param horaFinalizacion
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public boolean actualizarFechaHoraFinalizacion (Connection con, int codigoCuenta, String fechaFinalizacion, String horaFinalizacion, String estado,String observaciones) throws SQLException
	{
	    boolean actualizo=false;
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
	        actualizo=trasladoCamasDao.actualizarFechaHoraFinalizacion(con, codigoCuenta, fechaFinalizacion, horaFinalizacion,observaciones);
	        if (!actualizo)
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
	    return actualizo;
	}
	
	/**
	 * Metodo que actualiza la fecha y hora de finalizacion de ocupacion de una cama
	 * @param con
	 * @param codigoCuenta
	 * @param fechaFinalizacion
	 * @param horaFinalizacion
	 * @return
	 * @throws SQLException
	 */
	public boolean actualizarFechaHoraFinalizacionNoTransaccional (Connection con, int codigoCuenta, String fechaFinalizacion, String horaFinalizacion,String observaciones) throws SQLException
	{
		boolean actualizo=trasladoCamasDao.actualizarFechaHoraFinalizacion(con, codigoCuenta, fechaFinalizacion, horaFinalizacion,observaciones);
		return actualizo;
	}
	
	/**
	 * Mètodo que carga los traslados por centro de costo. Por defecto carga Todos
	 * @param con
	 * @param codigoCentroCosto
	 * @return
	 * @throws SQLException
	 */
	public HashMap cargarTrasladosArea (Connection con, int codigoCentroCosto) throws SQLException
	{
		trasladoCamasDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTrasladoCamasDao();
		HashMap map= null;
		try
		{
		    String[] colums={"habitacion", "cama", "descripcionCama", "paciente", "tipoId", "fechaIngreso", "responsable","centroCosto", "codigoPaciente","piso","tipo_habitacion"};
			map=UtilidadBD.resultSet2HashMap(colums, trasladoCamasDao.cargarTrasladosArea(con, codigoCentroCosto), true, true).getMapa();
		}
		catch(Exception e)
		{
			logger.warn("Error mundo TrasladoCamas.java Cargando los traslados de cama por centro de costo" +e.toString());
			map=null;
		}
		return map;
	}
	
	
	/**
	 * Mètodo que carga los traslados por centro de costo. Por defecto carga Todos
	 * @param con
	 * @param codigoCentroCosto
	 * @return
	 * @throws SQLException
	 */
	public HashMap cargarTrasladosPaciente (Connection con, int codigoPaciente) throws SQLException
	{
		trasladoCamasDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTrasladoCamasDao();
		HashMap map= null;
		try
		{
		    String[] colums={"fechaHoraTraslado","habitacion", "cama", "descripcionCama", "tipoUsuario","piso","tipo_habitacion", "codigoTraslado"};
			map=UtilidadBD.resultSet2HashMap(colums, trasladoCamasDao.cargarTrasladosPaciente(con, codigoPaciente), true, true).getMapa();
		}
		catch(Exception e)
		{
			logger.warn("Error mundo TrasladoCamas.java Cargando los traslados de cama por paciente " +e.toString());
			map=null;
		}
		return map;
	}
	
	/**
	 * Carga los traslados de camas que se realizaron en un ingreso especifico
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public HashMap cargarTrasladosPacientePorCuenta(Connection con, int cuenta)throws IPSException {
		HashMap map = null;
		try
		{
			String[] colums={"fechaHoraTraslado","habitacion", "cama", "descripcionCama", "tipoUsuario","piso","tipo_habitacion", "codigoTraslado"};
			ResultSetDecorator rsd = com.princetonsa.dao.sqlbase.SqlBaseTrasladoCamasDao.cargarTrasladosPacientePorCuenta(con, cuenta);
			map=UtilidadBD.resultSet2HashMap(colums, rsd, true, true).getMapa();
		}
		catch (SQLException e)
		{
			logger.error(e);
			throw new IPSException(e);
		}
		return map;
	}
	
	
	/**
	 * Método que cargar el detalle de un traslado cuando se consultan por paciente
	 * @param codigoTraslado
	 * @return
	 * @throws SQLException-
	 */
	public HashMap cargarDetalleTrasladoPaciente (Connection con, int codigoTraslado) throws SQLException
	{
		trasladoCamasDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTrasladoCamasDao();
		HashMap map= null;
		try
		{
		    String[] colums={
		    	"fechaHoraTraslado", 
		    	"nuevaHabitacion",
		    	"nuevaCama", 
		    	"pisoNuevaCama",
		    	"tipoHabitacionNuevaCama",
		    	"descripcionNuevaCama", 
		    	"tipoUsuarioNuevaCama", 
		    	"centroCostoNuevaCama", 
		    	"esUciNuevaCama", 
		    	"habitacionAntigua", 
		    	"camaAntigua", 
		    	"pisoCamaAntigua",
		    	"tipoHabitacionCamaAntigua",
		    	"descripcionCamaAntigua", 
		    	"tipoUsuarioCamaAntigua", 
		    	"centroCostoCamaAntigua", 
		    	"esUciCamaAntigua", 
		    	"fechaGrabacion", 
		    	"usuario",
		    	"paciente",
		    	"tipoId",
		    	"sexo",
		    	"fechaIngreso",
		    	"fechaEgreso",
		    	"responsable"
		    };
			map=UtilidadBD.resultSet2HashMap(colums, trasladoCamasDao.cargarDetalleTrasladoPaciente(con, codigoTraslado), true, true).getMapa();
		}
		catch(Exception e)
		{
			logger.warn("Error mundo TrasladoCamas.java Cargando el detalle de un traslado de cama por paciente" +e.toString());
			map=null;
		}
		return map;
	}
	
	
	
	/**
	 * Método que carga los ingresos anteriores de un paciente dado su codigo
	 * @param con
	 * @param codigoPaciente
	 * @return
	 * @throws SQLException
	 */
	public HashMap cargarIngresosAnteriores (Connection con, int codigoPaciente) throws SQLException
	{
		trasladoCamasDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTrasladoCamasDao();
		HashMap map= null;
		try
		{
		    String[] colums={"centro_atencion","cuenta","estadoCuenta","codigoviaIngreso","viaIngreso", "fechaHoraIngreso", "fechaHoraEgreso", "especialidad"};
			map=UtilidadBD.resultSet2HashMap(colums, trasladoCamasDao.cargarIngresosAnteriores(con, codigoPaciente), true, true).getMapa();
		}
		catch(Exception e)
		{
			logger.warn("Error mundo TrasladoCamas.java Cargando los ingresos anteriores de un Paciente" +e.toString());
			map=null;
		}
		return map;
	}
	
	/**
	 * Método que carga los traslados segun una fecha dada. Por defecto carga los que tengan fecha del sistema
	 * @param con
	 * @param fechaTraslado
	 * @param centroAtencion
	 * @return
	 * @throws SQLException
	 */
	public HashMap cargarTrasladosFecha (Connection con, String fechaTraslado,int centroAtencion) throws SQLException
	{
		trasladoCamasDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTrasladoCamasDao();
		HashMap map= null;
		try
		{
		    String[] colums={
	    		"hora",
	    		"habitacionActual",
	    		"pisoCamaActual",
	    		"tipoHabitacionCamaActual",
	    		"camaActual",
	    		"descripcionCamaActual",
	    		"paciente",
	    		"tipoId",
	    		"fechaIngreso",
	    		"responsable",
	    		"habitacionAnterior",
	    		"pisoCamaAnterior",
	    		"tipoHabitacionCamaAnterior",
	    		"camaAnterior",
	    		"descripcionCamaAnterior",
	    		"fechaTraslado",
	    		"codigoPaciente", 
	    		"codigoTraslado"
	    	};
			map=UtilidadBD.resultSet2HashMap(colums, trasladoCamasDao.cargarTrasladosFecha(con, fechaTraslado,centroAtencion), true, true).getMapa();
		}
		catch(Exception e)
		{
			logger.warn("Error mundo TrasladoCamas.java Cargando los traslados de cama por fecha de traslado" +e.toString());
			map=null;
		}
		return map;
	}

	/**
	 * Método para actualizar el estado de una cama 
	 * @param con
	 * @param estadoCama
	 * @param codigoCama
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public int modificarEstadoCama (Connection con,int estadoCama,int codigoCama, int institucion) throws SQLException
	{
		return trasladoCamasDao.modificarEstadoCama(con,estadoCama,codigoCama,institucion); 
	}
	
	
	/**
	 * Método que cargar el detalle de un traslado Anterior cuando se consultan por paciente
	 * @param con
	 * @param cuenta
	 * @return
	 * @throws SQLException
	 */
	public HashMap cargarDetalleTrasladoAnteriorPaciente (Connection con, int cuenta) throws SQLException
	{
		trasladoCamasDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTrasladoCamasDao();
		HashMap map= null;
		try
		{
		    String[] colums={"fechaHoraTraslado", "nuevaHabitacion", "nuevaCama","pisoNuevaCama","tipoHabitacionNuevaCama", "descripcionNuevaCama", "tipoUsuarioNuevaCama", "centroCostoNuevaCama", "esUciNuevaCama", "habitacionAntigua", "camaAntigua", "pisoCamaAntigua","tipoHabitacionCamaAntigua","descripcionCamaAntigua", "tipoUsuarioCamaAntigua", "centroCostoCamaAntigua", "esUciCamaAntigua", "fechaGrabacion", "usuario","paciente","tipoId","sexo","fechaIngreso","fechaEgreso","responsable"};
			map=UtilidadBD.resultSet2HashMap(colums, trasladoCamasDao.cargarDetalleTrasladoAnteriorPaciente(con, cuenta), true, true).getMapa();
			
			logger.info("nu,REgistros======> "+map.get("numRegistros"));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo TrasladoCamas.java Cargando el detalle de un traslado Anterior de un paciente" +e.toString());
			map=null;
		}
		return map;
	}
	
	
	/**
	 *Metodo encargdo de cambiar el estado de la cama,
	 *este devuelve true para indicar operacion exitosa
	 *de lo contrario devuelve false.
	 *El HashMap aprametros contiene las siguientes key's
	 *--------------------------------------------------
	 *--nuevoEstadoCama --> Requerido
	 *--institucion --> Requerido
	 *--codigoCama --> Requerido
	 *--codigoPaciente --> Requerido
	 *--estadoCama --> Requerido
	 *-------------------------------------------------- 
	 * @param connection
	 * @param parametros
	 * @return
	 */
	public  boolean cambiarEstaReserva (Connection connection,HashMap parametros)
	{
	
		 return trasladoCamasDao.cambiarEstaReserva(connection, parametros);
	}
	
    /**
     * @return Returns the fechaAsignacion.
     */
    public String getFechaAsignacion() {
        return fechaAsignacion;
    }
    /**
     * @param fechaAsignacion The fechaAsignacion to set.
     */
    public void setFechaAsignacion(String fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }
    /**
     * @return Returns the horaAsignacion.
     */
    public String getHoraAsignacion() {
        return horaAsignacion;
    }
    /**
     * @param horaAsignacion The horaAsignacion to set.
     */
    public void setHoraAsignacion(String horaAsignacion) {
        this.horaAsignacion = horaAsignacion;
    }
    /**
     * @return Returns the convenioPaciente.
     */
    public InfoDatosInt getConvenioPaciente() {
        return convenioPaciente;
    }
    /**
     * @param convenioPaciente The convenioPaciente to set.
     */
    public void setConvenioPaciente(InfoDatosInt convenioPaciente) {
        this.convenioPaciente = convenioPaciente;
    }
    /**
     * @return Returns the horaFinalizacion.
     */
    public String getHoraFinalizacion() {
        return horaFinalizacion;
    }
    /**
     * @param horaFinalizacion The horaFinalizacion to set.
     */
    public void setHoraFinalizacion(String horaFinalizacion) {
        this.horaFinalizacion = horaFinalizacion;
    }
    /**
     * @return Returns the fechaFinalizacion.
     */
    public String getFechaFinalizacion() {
        return fechaFinalizacion;
    }
    /**
     * @param fechaFinalizacion The fechaFinalizacion to set.
     */
    public void setFechaFinalizacion(String fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }
    /**
     * @return Returns the codigoCamaAntigua.
     */
    public int getCodigoCamaAntigua() {
        return codigoCamaAntigua;
    }
    /**
     * @param codigoCamaAntigua The codigoCamaAntigua to set.
     */
    public void setCodigoCamaAntigua(int codigoCamaAntigua) {
        this.codigoCamaAntigua = codigoCamaAntigua;
    }
    /**
     * @return Returns the codigoNuevaCama.
     */
    public int getCodigoNuevaCama() {
        return codigoNuevaCama;
    }
    /**
     * @param codigoNuevaCama The codigoNuevaCama to set.
     */
    public void setCodigoNuevaCama(int codigoNuevaCama) {
        this.codigoNuevaCama = codigoNuevaCama;
    }
}
