/*
 * @(#)ActualizacionAutorizacion.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.mundo.facturacion;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.dao.ActualizacionAutorizacionDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.cargos.Contrato;

/**
 * Objeto que maneja la interacción entre la capa
 * de control y el acceso a la información de la 
 * Actualizacion Autorizacion,
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 24 /May/ 2005
 */
public class ActualizacionAutorizacion
{
	
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ActualizacionAutorizacion.class);
	
	/**
	 * Numero de autorizacion a actualizar
	 */
	private String numeroAutorizacion;
	
	/**
	 * Nuemro de la cuenta del paciente cargado
	 */
	private int cuenta;
	
	/**
	 * Fecha de la solicitud
	 */
	private String fechaSolicitud;
	
	/**
	 * Hora de la Solicitud
	 */
	private String horaSolicitud;
	
	/**
	 * Consecutivo Ordenes medicas de la solicitud
	 */
	private String consecutivo;
	
	/**
	 * Descripcion CUPS del Servicio de la Solicitud
	 */
	private String descripcionServicio;
	
	/**
	 * Codigo del paciente cargado en session
	 */
	private int codigoPaciente;
	
	/**
	 * Numero de solicitud a modificar
	 */
	private int numeroSolicitud;
	
	/**
	 * Código Axioma de la cirugia
	 */
	private int codigoCirugia;
    /**
     * Constructor del objeto
     * (Solo inicializa el acceso a la 
     * fuente de datos)
     */
    public ActualizacionAutorizacion()
    {
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>ActualizacionAutorizacion</code> 
	 * para acceder a la fuente de datos. 
	 */
	private ActualizacionAutorizacionDao actualizacionAutorizacionDao ;

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
			actualizacionAutorizacionDao = myFactory.getActualizacionAutorizacionDao();
			wasInited = (actualizacionAutorizacionDao != null);
		}

		return wasInited;
	}
	
	
	public void clean()
	{
		this.numeroAutorizacion = "";
		this.cuenta = 0;
		this.fechaSolicitud = "";
		this.horaSolicitud = "";
		this.consecutivo = "";
		this.descripcionServicio = "";
		this.codigoPaciente = 0;
		this.numeroSolicitud = 0;
		this.codigoCirugia = 0;
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
	 * @return Returns the numeroAutorizacion.
	 */
	public String getNumeroAutorizacion()
	{
		return numeroAutorizacion;
	}
	/**
	 * @param numeroAutorizacion The numeroAutorizacion to set.
	 */
	public void setNumeroAutorizacion(String numeroAutorizacion)
	{
		this.numeroAutorizacion= numeroAutorizacion;
	}
	
	/**
	 * @return Returns the consecutivo.
	 */
	public String getConsecutivo()
	{
		return consecutivo;
	}
	/**
	 * @param consecutivo The consecutivo to set.
	 */
	public void setConsecutivo(String consecutivo)
	{
		this.consecutivo= consecutivo;
	}
	/**
	 * @return Returns the descripcionServicio.
	 */
	public String getDescripcionServicio()
	{
		return descripcionServicio;
	}
	/**
	 * @param descripcionServicio The descripcionServicio to set.
	 */
	public void setDescripcionServicio(String descripcionServicio)
	{
		this.descripcionServicio= descripcionServicio;
	}
	/**
	 * @return Returns the fechaSolicitud.
	 */
	public String getFechaSolicitud()
	{
		return fechaSolicitud;
	}
	/**
	 * @param fechaSolicitud The fechaSolicitud to set.
	 */
	public void setFechaSolicitud(String fechaSolicitud)
	{
		this.fechaSolicitud= fechaSolicitud;
	}
	/**
	 * @return Returns the horaSolicitud.
	 */
	public String getHoraSolicitud()
	{
		return horaSolicitud;
	}
	/**
	 * @param horaSolicitud The horaSolicitud to set.
	 */
	public void setHoraSolicitud(String horaSolicitud)
	{
		this.horaSolicitud= horaSolicitud;
	}
	
	
	/**
	 * @return Returns the codigoCirugia.
	 */
	public int getCodigoCirugia() {
		return codigoCirugia;
	}
	/**
	 * @param codigoCirugia The codigoCirugia to set.
	 */
	public void setCodigoCirugia(int codigoCirugia) {
		this.codigoCirugia = codigoCirugia;
	}
	
	
	/**
	 * @return Returns the numeroSolicitud.
	 */
	public int getNumeroSolicitud()
	{
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(int numeroSolicitud)
	{
		this.numeroSolicitud= numeroSolicitud;
	}
	
	
	/**
	 * Método que carga los datos necesarios para
	 * el estado de cuenta para todas las cuentas
	 * de un paciente dado
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoPaciente Código del paciente
	 * al que se desean revisar sus cuentas
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	public HashMap cargarDatosPacienteCuenta (Connection con, int codigoPaciente) throws SQLException
	{
		actualizacionAutorizacionDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getActualizacionAutorizacionDao();
		return actualizacionAutorizacionDao.cargarDatosPacienteCuenta(con, codigoPaciente);
	}
	
	/**
	 * Actualiza los numeros de autorizacion de la admision de hospitalización según la cuenta del paciente
	 * @param con
	 * @return int
	 */
	public int modificarNumeroAutorizacionAdmisionHospitalizacionTransaccional(Connection con, String estado)
	{
	    return actualizacionAutorizacionDao.modificarNumeroAutorizacionAdmisionHospitalizacionTransaccional(con,this.numeroAutorizacion,this.cuenta, estado);
	}
	
	/**
	 * Actualiza los numeros de autorizacion de la admision de urgencias según la cuenta del paciente
	 * @param con
	 * @return int
	 */
	public int modificarNumeroAutorizacionAdmisionUrgenciasTransaccional(Connection con, String estado)
	{
	    return actualizacionAutorizacionDao.modificarNumeroAutorizacionAdmisionUrgenciasTransaccional(con, this.numeroAutorizacion, this.cuenta, estado);
	}
	
	/**
	 * Actualiza todos los numeros de Autorizacion ent todas las solicitudes
	 * asociadas a una cuenta
	 * @param con
	 * @return
	 */
	public int modificarNumeroAutorizacionTodasSolicitudesXCuentaTransaccional(Connection con, String estado)
	{
		return actualizacionAutorizacionDao.modificarNumeroAutorizacionTodasSolicitudesXCuentaTransaccional(con, this.numeroAutorizacion, this.cuenta, estado);
	}
	
	
	/**
	 * @param con
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap listadoSolicitudesOrdenes(Connection con)
	{
		actualizacionAutorizacionDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getActualizacionAutorizacionDao();
		HashMap map= null;
		try
		{
		    String[] colums={"fechaSolicitud", "horaSolicitud", "consecutivo", "descripcionServicio","numeroAutorizacion", "numeroSolicitud","tipo","esJustificado","codigoServicio"};
		    int contrato = this.cargarContratoCuenta(con);
			map=UtilidadBD.resultSet2HashMap(colums, actualizacionAutorizacionDao.cargarSolicitudesOrden(con, this.cuenta,contrato), true, true).getMapa();
		}
		catch(Exception e)
		{
			logger.warn("Error mundo Actualización de Número de Autorización" +e.toString());
			map=null;
		}
		return map;
	}
	
	/**
	 * Método implementado para cargar el contrato de la cuenta
	 * @param con
	 * @param cuenta
	 * @return
	 */
	private int cargarContratoCuenta(Connection con) 
	{
		Cuenta mundoCuenta = new Cuenta();
		Contrato mundoContrato = new Contrato();
		int contrato = 0;
		try
		{
			mundoCuenta.cargarCuenta(con,this.cuenta+"");
			//Se verifica si la cuenta está distribuida
			if(mundoCuenta.getCodigoEstadoCuenta().equals(ConstantesBD.codigoEstadoCuentaFacturadaParcial+""))
				contrato = mundoContrato.cargarCodigoContrato(con,this.cuenta,false);
			else
				contrato = mundoContrato.cargarCodigoContrato(con,this.cuenta,true);
				
			
		}
		catch(Exception e)
		{
			logger.error("Error al consultar el contrato de la cuenta en Actualizacion_Autorizaciones: "+e);
		}
		
		return contrato;
	}


	/**
	 * @param con
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap listadoMedicamentos(Connection con)
	{
		actualizacionAutorizacionDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getActualizacionAutorizacionDao();
		return actualizacionAutorizacionDao.cargarMedicamentos(con, this.cuenta);
	}
	
	/**
	 * Método para actualizar el numero de autorizacion de todas las solicitudes que NO sean de medicamentos
	 * @param con
	 * @return
	 */
	public  int modificarNumeroAutorizacionSolicitudes(Connection con) 
	{
		return actualizacionAutorizacionDao.modificarNumeroAutorizacionSolicitudes(con,this.numeroAutorizacion, this.numeroSolicitud);
	}
	
	/**
	 * Método para actualizar el numero de autorizacion de todas las solicitudes que NO sean de medicamentos
	 * @param con
	 * @return
	 */
	public static boolean modificarNumeroAutorizacionSolicitudes(Connection con, String numeroAutorizacion, int numeroSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getActualizacionAutorizacionDao().modificarNumeroAutorizacionSolicitudes(con,numeroAutorizacion, numeroSolicitud)>0;
	}
	
	/**
	 * Método para actualizar el numero de autorizacion de todas las solicitudes que SOLO sean de medicamentos
	 * @param con
	 * @return
	 */
	public int modificarNumeroAutorizacionMedicamentos(Connection con)
	{
		return actualizacionAutorizacionDao.modificarNumeroAutorizacionMedicamentos(con,this.numeroAutorizacion, this.numeroSolicitud);
	}
	
	/**
	 * Metodo para la Búsqueda Avanzada
	 * @param con
	 * @param fechaSolicitud
	 * @param consecutivo
	 * @param descripcionServicio
	 * @param numeroAutorizacion
	 * @param esServicio
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap busquedaAvanzada(Connection con,String fechaSolicitud, String horaSolicitud, String consecutivo, String descripcionServicio, String numeroAutorizacion, boolean esServicio)
	{
		actualizacionAutorizacionDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getActualizacionAutorizacionDao();
		HashMap map= null;
		try
		{
		    String[] colums={"fechaSolicitud", "horaSolicitud", "consecutivo", "descripcionServicio","numeroAutorizacion", "numeroSolicitud","tipo","esJustificado","codigoServicio"};
		    int contrato = this.cargarContratoCuenta(con);
			map=UtilidadBD.resultSet2HashMap(colums, actualizacionAutorizacionDao.busquedaAvanzada(con,this.cuenta, fechaSolicitud, horaSolicitud, consecutivo, descripcionServicio,numeroAutorizacion,contrato, esServicio), true, true).getMapa();
		}
		catch(Exception e)
		{
			logger.warn("Error mundo Actualización de Número de Autorización" +e.toString());
			map=null;
		}
		return map;
	}
	
	/**
	 * Método que modifica el numero de autorizacion de una cirugia
	 * @param con
	 * @return
	 */
	public int modificarNumeroAutorizacionCirugia(Connection con)
	{
		logger.info("codigo Cirugia=> "+codigoCirugia+", numeroAutorizacion=> "+numeroAutorizacion);
		return actualizacionAutorizacionDao.modificarNumeroAutorizacionCirugia(con,this.numeroAutorizacion,this.codigoCirugia);
	}
	
}
