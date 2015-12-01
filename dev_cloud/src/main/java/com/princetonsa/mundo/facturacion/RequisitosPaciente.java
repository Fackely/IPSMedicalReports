/*
 * @(#)RequisitosPaciente.java
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
import com.princetonsa.dao.RequisitosPacienteDao;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Clase para registrar los tipos de requisitos
 * para la atenci&oacute;n de pacientes o los 
 * requisitos de radicacion
 *
 * @version 1.0, 19/11/2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class RequisitosPaciente 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static RequisitosPacienteDao reqDao;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(RequisitosPaciente.class);
    
	/** 
	 * Codigo - descripcion - Activo/Inactivo Requisito Paciente 
	 */
	private InfoDatosInt requisitoPaciente;
	
	/**
	 * Tipo de requisito
	 */
	private String tipoRequisito;
	
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
        this.requisitoPaciente= new InfoDatosInt();
        this.codigoInstitucion=-1;
        this.tipoRequisito = "";
    }
    
	 /**
	  * Constructor vacio de la clase
	  *
	  */
	 public  RequisitosPaciente ()
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
	 public RequisitosPaciente( int codigo, String descripcion, boolean activo, int codigoInstitucion)
	 {
	     this.requisitoPaciente = new InfoDatosInt(codigo, descripcion, activo);
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
			reqDao = myFactory.getRequisitosPacienteDao();
			wasInited = (reqDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Método que obtiene todos los resultados de la tabla requisitos_paciente
	 * para mostrarlos en el listado y poder ingresar otros o modificarlos.
	 * @param con
	 * @return
	 */
	public HashMap listadoRequisitos(Connection con)
	{
		reqDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRequisitosPacienteDao();
		HashMap map= null;
		try
		{
			map=UtilidadBD.cargarValueObject(new ResultSetDecorator(reqDao.cargarRequisitos(con, this.codigoInstitucion)));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo requisitos Paciente " +e.toString());
			map=null;
		}
		return map;
	}
	
	/**
	 * Método que obtiene todos los resultados de la tabla requisitos_radicacion
	 * para mostrarlos en el listado y poder ingresar otros o modificarlos.
	 * @param con
	 * @return
	 */
	public HashMap listadoRequisitosRadicacion(Connection con)
	{
		reqDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRequisitosPacienteDao();
		HashMap map= null;
		try
		{
			map=UtilidadBD.cargarValueObject(new ResultSetDecorator(reqDao.cargarRequisitosRadicacion(con, this.codigoInstitucion)));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo requisitos Paciente (cargar requisitos radicacion)" +e.toString());
			map=null;
		}
		return map;
	}
	
	
	/**
	 * Método que obtiene todos los resultados de la tabla requisitos_paciente
	 * para mostrarlos en el consultar y en el resumen.
	 * @param con
	 * @return
	 */
	public Collection listadoRequisitosCollection(Connection con)
	{
		reqDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRequisitosPacienteDao();
		Collection coleccion=null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(reqDao.cargarRequisitos(con, this.codigoInstitucion));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo listadoRequisitosCollection requisitos paciente " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}
	
	/**
	 * Método que obtiene todos los resultados de la tabla requisitos_radicacion
	 * para mostrarlos en el consultar y en el resumen.
	 * @param con
	 * @return
	 */
	public Collection listadoRequisitosRadicacionCollection(Connection con)
	{
		reqDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRequisitosPacienteDao();
		Collection coleccion=null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(reqDao.cargarRequisitosRadicacion(con, this.codigoInstitucion));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo listadoRequisitosRadicacionCollection requisitos paciente " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}
	
	
	/**
	 * Inserta un requisito de un paciente
	 * @param con
	 * @return
	 */
	public int insertarRequisitosPaciente(Connection con) 
	{
	    int resp1=0;
		boolean inicioTrans;
		inicioTrans=UtilidadBD.iniciarTransaccion(con);
		resp1=reqDao.insertar(con,this.getDescripcionRequisito(),this.getTipoRequisito(),this.getActivoRequisito(),this.getCodigoInstitucion());
		if (!inicioTrans||resp1<1  )
		{
			UtilidadBD.abortarTransaccion(con);
			return -1;
		}
		else
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		return resp1;
	}
	
	/**
	 * Inserta un requisito de radicacion
	 * @param con
	 * @return
	 */
	public int insertarRequisitosRadicacion(Connection con) 
	{
	    int resp1=0;
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		inicioTrans=UtilidadBD.iniciarTransaccion(con);
		resp1=reqDao.insertarRequisitosRadicacion(con,this.getDescripcionRequisito(),this.getTipoRequisito(),this.getActivoRequisito(),this.getCodigoInstitucion());
		if (!inicioTrans||resp1<1  )
		{
			UtilidadBD.abortarTransaccion(con);
			return -1;
		}
		else
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		return resp1;
	}
	
	/**
	 * Método para modificar los requisitos de un paciente
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int modificarRequisitosPaciente(Connection con) throws SQLException 
	{
			int resp=0;
			resp=reqDao.modificar(con,this.getDescripcionRequisito(), this.getTipoRequisito(),this.getActivoRequisito(), this.getCodigoRequisito());
			return resp;
	}
	
	/**
	 * Método para modificar los requisitos de radicacion
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int modificarRequisitosRadicacion(Connection con) throws SQLException 
	{
			int resp=0;
			resp=reqDao.modificarRequisitosRadicacion(con,this.getDescripcionRequisito(),this.getTipoRequisito(), this.getActivoRequisito(), this.getCodigoRequisito());
			return resp;
	}
	
	
	/**
	 * Retorna el código del requisito
	 * @return
	 */
	public int getCodigoRequisito(){
		return requisitoPaciente.getCodigo();
	}
	/**
	 * Asigna el código del requisito
	 * @param codigoRequisito
	*/
	public void setCodigoRequisito(int codigoReqisito){
		this.requisitoPaciente.setCodigo(codigoReqisito);
	}
	/**
	 * Retorna la descripcion del requisito
	 * @return
	 */
	public String  getDescripcionRequisito(){
		return requisitoPaciente.getDescripcion();
	}
	/**
	 * Asigna la descripcion del requisito
	 * @param descripcionRequisito
	*/
	public void setDescripcionRequisito(String descripcionRequisito){
		this.requisitoPaciente.setDescripcion(descripcionRequisito);
	}
	/**
	 * Retorna boolean activo/inactivo  requisito
	 * @return
	 */
	public boolean  getActivoRequisito(){
		return requisitoPaciente.isActivo();
	}
	/**
	 * Asigna boolean activo/inactivo del requisito
	 * @param activoRequisito
	*/
	public void setActivoRequisito(boolean activoRequisito){
		this.requisitoPaciente.setActivo(activoRequisito);
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
     * @return Returns the requisitoPaciente.
     */
    public InfoDatosInt getRequisitoPaciente() {
        return requisitoPaciente;
    }
    /**
     * @param requisitoPaciente The requisitoPaciente to set.
     */
    public void setRequisitoPaciente(InfoDatosInt requisitoPaciente) {
        this.requisitoPaciente = requisitoPaciente;
    }

	/**
	 * @return the tipoRequisito
	 */
	public String getTipoRequisito() {
		return tipoRequisito;
	}

	/**
	 * @param tipoRequisito the tipoRequisito to set
	 */
	public void setTipoRequisito(String tipoRequisito) {
		this.tipoRequisito = tipoRequisito;
	}
}
