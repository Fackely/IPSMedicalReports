/*
 * Created on 09/05/2006
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Lenguaje		:Java
 *
 */
package com.princetonsa.mundo.agenda;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ReasignarAgendaDao;

/**
 * 
 * @author artotor
 *
 */
public class ReasignarAgenda 
{
	
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(ReasignarAgenda.class);
	/**
	 * DAO de este objeto, para trabajar con Cajas
	 * en la fuente de datos
	 */    
    private static ReasignarAgendaDao reasignarAgendaDao;
	/**
	 * Fecha inicial para realizar la busqueda de las agendas.
	 */
	private String fechaInicial;
	/**
	 * Fecha final para realizar la busqueda de las agendas.
	 */
	private String fechaFinal;
	/**
	 * codigo del profesional para realizar la busqueda de la agenda
	 */
	private int codigoProfesional;
	/**
	 * codigo de la unidad de consulta pra realizar la busqueda.
	 */
	private int codigoUnidadConsulta;
	/**
	 * mapa para manejar el listado de las agendas. 
	 */
	private HashMap agendas;
	
	/**
	 * 
	 */
	private String centroAtencion;
	 /**
     * Usuario que genera la reasignacion, usado en la consulgta del log.
     */
    private String usuarioLog;
    /**
 	 * mapa para manejar el listado de las agendas. 
 	 */
 	private HashMap logs;
 	
 	/**
 	 * 
 	 */
 	private String centrosAtencion;
 	
 	/**
 	 * 
 	 */
 	private String unidadesAgenda;
 	
	/**
	 * Constructor de la clase.
	 *
	 */
	public ReasignarAgenda()
	{
		this.reset();
	    init(System.getProperty("TIPOBD"));
	}
	

	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	*/
	public boolean init(String tipoBD)
	{
		if ( reasignarAgendaDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			reasignarAgendaDao= myFactory.getReasignarAgendaDao();
			if( reasignarAgendaDao!= null )
				return true;
		}
		return false;
	}
	
	/**
	 * Método para obtener el DAO de reasignar agenda de manera estática
	 * @return
	 */
	public static ReasignarAgendaDao reasignarAgenda()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReasignarAgendaDao();
	}
	

	/**
     * Metodo que resetea los atributos de la clase
     */
    public void reset() 
    {
    	this.fechaInicial="";
    	this.fechaFinal="";
    	this.codigoProfesional=ConstantesBD.codigoNuncaValido;
    	this.codigoUnidadConsulta=ConstantesBD.codigoNuncaValido;
    	this.agendas=new HashMap();
    	this.usuarioLog="";
    	this.logs=new HashMap();
    	this.centroAtencion="";
    	this.unidadesAgenda="";
    	this.centrosAtencion="";
    }
    public int getCodigoProfesional() 
    {
		return codigoProfesional;
	}

	public void setCodigoProfesional(int codigoProfesional) 
	{
		this.codigoProfesional = codigoProfesional;
	}

	public int getCodigoUnidadConsulta() 
	{
		return codigoUnidadConsulta;
	}

	public void setCodigoUnidadConsulta(int codigoUnidadConsulta) 
	{
		this.codigoUnidadConsulta = codigoUnidadConsulta;
	}

	public String getFechaFinal() 
	{
		return fechaFinal;
	}

	public void setFechaFinal(String fechaFinal) 
	{
		this.fechaFinal = fechaFinal;
	}

	public String getFechaInicial() 
	{
		return fechaInicial;
	}

	public void setFechaInicial(String fechaInicial) 
	{
		this.fechaInicial = fechaInicial;
	}
	public HashMap getAgendas() {
		return agendas;
	}

	public void setAgendas(HashMap agendas) {
		this.agendas = agendas;
	}
	
	public Object getAgendas(String key) {
		return agendas.get(key);
	}

	public void setAgendas(String key,Object value) {
		this.agendas.put(key,value);
	}

	

	
	/**
	 * Metodo que realiza la consulta de las agendas.
	 * @param con
	 */
	public void ejecutarBusquedaAgendas(Connection con) 
	{
		HashMap vo=new HashMap();
		vo.put("fechaInicial",this.getFechaInicial());
		vo.put("fechaFinal",this.getFechaFinal());
		vo.put("profesional",this.getCodigoProfesional()+"");
		vo.put("unidadConsulta",this.getCodigoUnidadConsulta()+"");
		vo.put("centroAtencion",this.centroAtencion);
		vo.put("centrosAtencion", this.centrosAtencion);
		vo.put("unidadesAgenda", this.unidadesAgenda);
		this.setAgendas(reasignarAgendaDao.buscarAgendas(con,vo));
	}


	public HashMap consultarCitasAgenda(Connection con, int codigoAgenda) 
	{
		return reasignarAgendaDao.consultarCitasAgenda(con,codigoAgenda);
	}


	/**
	 * metodo que reasigna la agenda a otro profesional.
	 * @param con
	 * @param codigoAgenda
	 * @param nuevoProfesional
	 * @return
	 */
	public boolean reasignarProfesiona(Connection con, int codigoAgenda, int nuevoProfesional) 
	{
		return reasignarAgendaDao.reasignarProfesiona(con,codigoAgenda,nuevoProfesional);
	}


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarLogReasignacionAgenda(Connection con, HashMap vo) 
	{
		return reasignarAgendaDao.insertarLogReasignacionAgenda(con,vo);
	}


	public String getUsuarioLog() {
		return usuarioLog;
	}


	public void setUsuarioLog(String usuarioLog) {
		this.usuarioLog = usuarioLog;
	}


	public HashMap getLogs() {
		return logs;
	}


	public void setLogs(HashMap logs) {
		this.logs = logs;
	}


	/**
	 * 
	 * @param con
	 */
	public void ejecutarBusquedalogs(Connection con) 
	{
		HashMap vo=new HashMap();
		vo.put("fechaInicial",this.getFechaInicial());
		vo.put("fechaFinal",this.getFechaFinal());
		vo.put("profesional",this.getCodigoProfesional()+"");
		vo.put("usuario",this.getUsuarioLog()+"");
		vo.put("centroAtencion",this.getCentroAtencion()+"");
		vo.put("centrosAtencion", this.centrosAtencion);
		this.setLogs(reasignarAgendaDao.ejecutarBusquedalogs(con,vo));
	}
	
	/**
	 * Método para verificar si un profesional pertenece a una unidad de agenda
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean perteneceProfesionalAUnidadAgenda(Connection con,int codigoProfesional,int codigoUnidadAgenda)
	{
		HashMap campos = new HashMap();
		campos.put("codigoProfesional", codigoProfesional);
		campos.put("codigoUnidadAgenda", codigoUnidadAgenda);
		return reasignarAgenda().perteneceProfesionalAUnidadAgenda(con, campos);
	}
	
	/**
	 * Método para obtener los profesionales que aplican para una unidad de agenda específica
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerProfesionalesUnidadAgenda(Connection con,int codigoUnidadAgenda)
	{
		HashMap campos = new HashMap();
		campos.put("codigoUnidadAgenda", codigoUnidadAgenda);
		return reasignarAgenda().obtenerProfesionalesUnidadAgenda(con, campos);
	}


	public String getCentroAtencion() {
		return centroAtencion;
	}


	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


	/**
	 * @return the centrosAtencion
	 */
	public String getCentrosAtencion() {
		return centrosAtencion;
	}


	/**
	 * @param centrosAtencion the centrosAtencion to set
	 */
	public void setCentrosAtencion(String centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}


	/**
	 * @return the unidadesAgenda
	 */
	public String getUnidadesAgenda() {
		return unidadesAgenda;
	}


	/**
	 * @param unidadesAgenda the unidadesAgenda to set
	 */
	public void setUnidadesAgenda(String unidadesAgenda) {
		this.unidadesAgenda = unidadesAgenda;
	}
	
	
}
