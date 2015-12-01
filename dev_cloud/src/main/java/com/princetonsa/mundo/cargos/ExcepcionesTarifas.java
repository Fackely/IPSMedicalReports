/*
 * @(#)ExcepcionesTarifas.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.cargos;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ExcepcionesTarifasDao;

/**
 * Clase para el manejo de contratos
 * @version 1.0, Oct 20, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class ExcepcionesTarifas 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ExcepcionesTarifas.class);

	/**
	 * código de la excepción
	 */
	private int codigoExcepcion;
	
	/**
	 * Porcentaje de descuento o aumento de la tarifa
	 */
	private double porcentaje;
	
	/**
	 * Valor de ajuste de la excepción
	 */
	private double valorAjuste;
	
	/**
	 * Nueva tarifa de la excepción
	 */
	private double nuevaTarifa;
	
	/**
	 * Contrato asociado a la excepción
	 */
	private Contrato contrato;
	
	/**
	 * Servicio asociado a la excepcion 
	 */
	private InfoDatosInt servicio;
	
	/**
	 * Especialidad asociada a la excepcion
	 */
	private InfoDatosInt especialidad;
	
	/**
	 * Via de ingreso asociada a la excepcion
	 */
	private InfoDatosInt viaIngreso;
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos.
	 */
	private ExcepcionesTarifasDao excepcionDao = null;
	
	/**
	 * Creadora de la clase ExcepcionTarifa.java
	 */	
	public ExcepcionesTarifas()
	{
		this.codigoExcepcion = 0;
		this.porcentaje = 0.0;
		this.valorAjuste = 0.0;
		this.nuevaTarifa = 0.0;
		this.viaIngreso = new InfoDatosInt();
		this.especialidad = new InfoDatosInt();
		this.servicio = new InfoDatosInt();
		this.contrato = new Contrato();
		//this.nombreArchivoLogs = "";
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Creadora de la clase ExcepcionTarifa.java
	 * @param codigo
	 * @param porcentaje
	 * @param valorAjuste
	 * @param nuevaTarifa
	 * @param viaIngreso
	 * @param especialidad
	 * @param servicio
	 * @param contrato
	 * @param nombreArchivoLogs
	 */
	public ExcepcionesTarifas(int codigoExcepcion,
											double porcentaje,
											double valorAjuste,
											double nuevaTarifa,
											InfoDatosInt viaIngreso,
											InfoDatosInt especialidad,
											InfoDatosInt servicio,
											Contrato contrato)
											//String nombreArchivoLogs	)
	{
		this.codigoExcepcion = codigoExcepcion;
		this.porcentaje = porcentaje;
		this.valorAjuste = valorAjuste;
		this.nuevaTarifa = nuevaTarifa;
		this.viaIngreso = viaIngreso;
		this.especialidad = especialidad;
		this.servicio = servicio;
		this.contrato = contrato;
		//this.nombreArchivoLogs = nombreArchivoLogs;
		this.init(System.getProperty("TIPOBD"));	
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
		if( excepcionDao == null)
		{
			if(tipoBD==null)
			{
				logger.error("No esta llegando el tipo de base de datos");
				System.exit(1);
			}
			else
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
				if (myFactory != null)
				{
					excepcionDao = myFactory.getExcepcionesTarifasDao();
				}					
			}
		}
	}
	
	/**
	 * Método que inicializa todos los atributos de la forma
	 */
	public void reset() 
	{
		this.codigoExcepcion = 0;
		this.porcentaje = 0.0;
		this.valorAjuste = 0.0;
		this.nuevaTarifa = 0.0;
		this.viaIngreso = new InfoDatosInt();
		this.especialidad = new InfoDatosInt();
		this.servicio = new InfoDatosInt();
		this.contrato = new Contrato();
	}
	
	/**
	 * Inserta una excepcion validando que no esté previamente definida
	 * @param con. Connection, conexión abierta con la fuente de datos
	 * @return boolean
	 */
	public boolean existeExcepcion( Connection con, int codigoExcepcionP )
	{
		return this.excepcionDao.existeTarifaDefinida(con,this.getCodigoViaIngreso(), this.getCodigoEspecialidad(), this.getCodigoContrato(),this.getCodigoServicio(), codigoExcepcionP);
	}
	
	/**
	 * Inserta una excepcion validando que no esté previamente definida
	 * @param con. Connection, conexión abierta con la fuente de datos
	 * @return boolean
	 */
	public static boolean existeExcepcion( Connection con,  int codigoViaIngresoP, int codigoEspecialidadP, int codigoContratoP, int codigoServicioP, int codigoExcepcionP)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesTarifasDao().existeTarifaDefinida(con,codigoViaIngresoP, codigoEspecialidadP, codigoContratoP,codigoServicioP, codigoExcepcionP);
	}
	
	/**
	 * Inserta una excepción a una tarifa 
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @return boolean
	 */
	public boolean insertar(	Connection con	)
	{
		return this.excepcionDao.insertar(	con, 
															this.porcentaje, 
															this.valorAjuste, 
															this.nuevaTarifa, 
															this.servicio.getCodigo(), 
															this.especialidad.getCodigo(), 
															this.viaIngreso.getCodigo(), 
															this.contrato.getCodigo());
	}
	
	
	/**
	 * Método para cargar los datos pertinentes al resumen
	 * @param con conexión
	 * @return
	 * @throws SQLException
	 */
	public boolean cargarResumen(Connection con) 
	{
		try
		{
			ResultSetDecorator rs=excepcionDao.cargarResumen(con);
			
			if (rs.next())
			{
				//this.codigo=rs.getInt("codigo");
				this.porcentaje=rs.getDouble("porcentaje");
				this.valorAjuste=rs.getDouble("valorAjuste");
				this.nuevaTarifa=rs.getDouble("nuevaTarifa");
				this.viaIngreso= new InfoDatosInt(rs.getInt("codigoViaIngreso"), rs.getString("nombreViaIngreso"));
				this.especialidad= new InfoDatosInt(rs.getInt("codigoEspecialidad"), rs.getString("nombreEspecialidad"));
				this.servicio=new InfoDatosInt(rs.getInt("codigoServicio"), rs.getString("nombreServicio"));
				this.contrato.setCodigo(rs.getInt("codigoContrato"));
				this.contrato.setNumeroContrato(rs.getString("numeroContrato"));
				
				return true;
			}
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del resumen de  la excepcion de tarifas en el mundo "+e.toString());
			return false;
		}
	}
	
	/**
	 * Método que contiene los resultados de la búsqueda de excepciones de tarifas
	 * según los criterios dados en la búsqueda avanzada. 
	 * @param con
	 * @return
	 */
	public Collection resultadoBusquedaAvanzada(Connection con)
	{
		excepcionDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesTarifasDao();
		Collection coleccion=null;
		try
		{	
			if(	excepcionDao.busqueda	(	con,this.getCodigoContrato(), this.getCodigoViaIngreso(), this.getCodigoServicio(), 
														this.getNombreServicio(), this.getCodigoEspecialidad(), this.getNombreEspecialidad(), 
														this.getPorcentaje(), this.getValorAjuste(), this.getNuevaTarifa())==null)
				return null;
			else	
				coleccion=UtilidadBD.resultSet2Collection(excepcionDao.busqueda	(	con,this.getCodigoContrato(), this.getCodigoViaIngreso(), this.getCodigoServicio(), 
																														this.getNombreServicio(), this.getCodigoEspecialidad(), this.getNombreEspecialidad(), 
																														this.getPorcentaje(), this.getValorAjuste(), this.getNuevaTarifa()));
		}
		catch(Exception e)
		{
			logger.warn("Error resultado Busqueda Avanzada en excepciones de tarifas " +e.toString());
			coleccion=null;
		}
		return coleccion;		
	}
	
	/**
	 * Método que elimina una excepción de tarifa dado su código
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public boolean eliminar(Connection con) throws SQLException 
	{
			boolean resp=false;
			resp=excepcionDao.eliminar(con,this.getCodigoExcepcion());
			return resp;
	}
	
	/**
	 * Método que modifica  una excepción de tarifa 
	 * dentro de una transaccion
	 */
	public boolean modificarTransaccional(		Connection con, int codigoExcepcion, 
																	int codigoViaIngreso, int codigoEspecialidad, 
																	int codigoServicio, double porcentaje, 
																	double valorAjuste, double nuevaTarifa, 
																	String estado) throws SQLException 
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		boolean resp = false;
		
		if (excepcionDao == null) 
		{
			throw new SQLException(	"No se pudo inicializar la conexión con la fuente de datos (excepcionesMedicamentosDao - modificarTransaccional )");
		}
		
		//****Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;

		if (estado.equals(ConstantesBD.inicioTransaccion)) 
		{
			inicioTrans = myFactory.beginTransaction(con);
		} 
		else 
		{
			inicioTrans = true;
		}
		
		resp = excepcionDao.modificar(con,codigoExcepcion,codigoViaIngreso, codigoEspecialidad,codigoServicio, porcentaje,valorAjuste, nuevaTarifa); 
		
		if (!inicioTrans || resp == false) 
		{
		    myFactory.abortTransaction(con);
			return false;
		}
		
		return resp;
	}
	
	/**
	 * Método que carga el resumen de las excepciones que han sido modificadas
	 */
	public Collection cargarResumenModificacion(Connection con, Vector codigos)
	{
		excepcionDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesTarifasDao();
		Collection coleccion=null;
		try
		{	
			if(	excepcionDao.cargarResumenModificacion(con, codigos) == null)
				return null;
			else	
				coleccion=UtilidadBD.resultSet2Collection(excepcionDao.cargarResumenModificacion(con, codigos));
		}
		catch(Exception e)
		{
			logger.warn("Error resultado resumen excepciones de tarifas modificadas " +e.toString());
			coleccion=null;
		}
		return coleccion;		
	}
	
	/**
	 * Retorna el porcentaje de descuento o aumento de la tarifa
	 * @return
	 */
	public double getPorcentaje()
	{
		return porcentaje;
	}

	/**
	 * Asigna el porcentaje de descuento o aumento de la tarifa
	 * @param porcentaje
	 */
	public void setPorcentaje(double porcentaje)
	{
		this.porcentaje = porcentaje;
	}

	/**
	 * Retorna el valor de ajuste de la excepción
	 * @return
	 */
	public double getValorAjuste()
	{
		return valorAjuste;
	}

	/**
	 * Asigna el valor de ajuste de la excepción
	 * @param valorAjuste
	 */
	public void setValorAjuste(double valorAjuste)
	{
		this.valorAjuste = valorAjuste;
	}

	/**
	 * Retorna la nueva tarifa de la excepción
	 * @return
	 */
	public double getNuevaTarifa()
	{
		return nuevaTarifa;
	}

	/**
	 * Asigna la nueva tarifa de la excepción
	 * @param nuevaTarifa
	 */
	public void setNuevaTarifa(double nuevaTarifa)
	{
		this.nuevaTarifa = nuevaTarifa;
	}

	/**
	 * Retorna el contrato asociado a la excepción
	 * @return
	 */
	public Contrato getContrato()
	{
		return contrato;
	}

	/**
	 * Asigna el contrato asociado a la excepción
	 * @param contrato
	 */
	public void setContrato(Contrato contrato)
	{
		this.contrato = contrato;
	}
	
	/**
	 * Retorna el código del contrato asociado a la excepción
	 * @return
	 */
	public int getCodigoContrato()
	{
		return this.contrato.getCodigo();
	}

	/**
	 * Asigna el código del contrato asociado a la excepción
	 * @param codigoContrato
	 */
	public void setCodigoContrato(int codigoContrato)
	{
		this.contrato.setCodigo(codigoContrato);
	}

	/**
	 * Retorna el número del contrato asociado a la excepción
	 * @return
	 */
	public String getNumeroContrato()
	{
		return this.contrato.getNumeroContrato();
	}

	/**
	 * Asigna el número del contrato asociado a la excepción
	 * @param numeroContrato
	 */
	public void setNumeroContrato(String numeroContrato)
	{
		this.contrato.setNumeroContrato(numeroContrato);
	}

	/**
	 * Retorna el servicio asociado a la excepcion 
	 * @return
	 */
	public InfoDatosInt getServicio()
	{
		return servicio;
	}

	/**
	 * Asigna el servicio asociado a la excepcion 
	 * @param servicio
	 */
	public void setServicio(InfoDatosInt servicio)
	{
		this.servicio = servicio;
	}

	/**
	 * Retorna el código del servicio asociado a la excepción 
	 * @return
	 */
	public int getCodigoServicio()
	{
		return this.servicio.getCodigo();
	}

	/**
	 * Asigna el código del servicio asociado a la excepción 
	 * @param codigoServicio
	 */
	public void setCodigoServicio(int codigoServicio)
	{
		this.servicio.setCodigo(codigoServicio);
	}

	/**
	 * Retorna el nombre del servicio asociado a la excepción 
	 * @return
	 */
	public String getNombreServicio()
	{
		return this.servicio.getNombre();
	}

	/**
	 * Asigna el nombre del servicio asociado a la excepción 
	 * @param nombreServicio
	 */
	public void setNombreServicio(String nombreServicio)
	{
		this.servicio.setNombre(nombreServicio);
	}
	
	/**
	 * Retorna la especialidad asociada a la excepcion
	 * @return
	 */
	public InfoDatosInt getEspecialidad()
	{
		return especialidad;
	}

	/**
	 * Asigna la especialidad asociada a la excepcion
	 * @param especialidad
	 */
	public void setEspecialidad(InfoDatosInt especialidad)
	{
		this.especialidad = especialidad;
	}

	/**
	 * Retorna el código especialidad asociada a la excepcion
	 * @return
	 */
	public int getCodigoEspecialidad()
	{
		return especialidad.getCodigo();
	}

	/**
	 * Asigna el código especialidad asociada a la excepcion
	 * @param codigoEspecialidad 
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad)
	{
		this.especialidad.setCodigo(codigoEspecialidad);
	}

	/**
	 * Retorna el nombre especialidad asociada a la excepcion
	 * @return
	 */
	public String getNombreEspecialidad()
	{
		return especialidad.getNombre();
	}

	/**
	 * Asigna el nombre especialidad asociada a la excepcion
	 * @param nombreEspecialidad
	 */
	public void setNombreEspecialidad(String nombreEspecialidad)
	{
		this.especialidad.setNombre(nombreEspecialidad);
	}

	/**
	 * Retorna la via de ingreso asociada a la excepcion
	 * @return
	 */
	public InfoDatosInt getViaIngreso()
	{
		return viaIngreso;
	}

	/**
	 * Asigna la via de ingreso asociada a la excepcion
	 * @param viaIngreso
	 */
	public void setViaIngreso(InfoDatosInt viaIngreso)
	{
		this.viaIngreso = viaIngreso;
	}
	
	/**
	 * Retorna el ódigo de la Via de ingreso asociada a la excepcion
	 * @return
	 */
	public int getCodigoViaIngreso()
	{
		return viaIngreso.getCodigo();
	}

	/**
	 * Asigna el código de la Via de ingreso asociada a la excepcion
	 * @param codigoViaIngreso
	 */
	public void setCodigoViaIngreso(int codigoViaIngreso)
	{
		this.viaIngreso.setCodigo(codigoViaIngreso);
	}

	/**
	 * Retorna el nombre de la Via de ingreso asociada a la excepcion
	 * @return
	 */
	public String getNombreViaIngreso()
	{
		return viaIngreso.getNombre();
	}

	/**
	 * Asigna el nombre de la Via de ingreso asociada a la excepcion
	 * @param nombreViaIngreso
	 */
	public void setNombreViaIngreso(String nombreViaIngreso)
	{
		this.viaIngreso.setNombre(nombreViaIngreso);
	}
	
	/**
	 * @return Returns the codigoExcepcion.
	 */
	public int getCodigoExcepcion() {
		return codigoExcepcion;
	}
	/**
	 * @param codigoExcepcion The codigoExcepcion to set.
	 */
	public void setCodigoExcepcion(int codigoExcepcion) {
		this.codigoExcepcion = codigoExcepcion;
	}
}
