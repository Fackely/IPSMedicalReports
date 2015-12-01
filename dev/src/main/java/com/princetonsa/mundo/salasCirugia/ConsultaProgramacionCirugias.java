package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.EventosAdversosDao;
import com.princetonsa.dao.manejoPaciente.MotivosSatisfaccionInsatisfaccionDao;
import com.princetonsa.dao.salasCirugia.ConsultaProgramacionCirugiasDao;

/**
 * mundo de Motivos Satisfaccion Insatisfaccion
 * @author axioma
 *
 */
public class ConsultaProgramacionCirugias
{
	private static Logger logger = Logger.getLogger(ConsultaProgramacionCirugias.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ConsultaProgramacionCirugiasDao consultaProgramacionCirugiasDao;
	
	/**
	 * Codigo de la Institución
	 */
	private String institucion;
	
	/**
	 * Codigo Paciente
	 */
	private int paciente;
	
	/**
	 * Codigo Peticion
	 */
	private int peticion;
	
	/**
	 * Codigo Solicitud
	 */
	private int solicitud;
	
	/**
	 * Mapa con los filtros de la busqueda por rangos
	 */
	private HashMap filtrosMap;
	
	/**
	 * Numero de pedido
	 */
	private int pedido;
	
	/**
	 *
	 */
	public ConsultaProgramacionCirugias()
	{
		this.reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 *
	 */
	public void reset() 
	{
		this.institucion="";
		this.paciente=ConstantesBD.codigoNuncaValido;
		this.peticion=ConstantesBD.codigoNuncaValido;
		this.solicitud=ConstantesBD.codigoNuncaValido;
		this.pedido=ConstantesBD.codigoNuncaValido;
		this.filtrosMap=new HashMap();
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
			consultaProgramacionCirugiasDao = myFactory.getConsultaProgramacionCirugiasDao();
			wasInited = (consultaProgramacionCirugiasDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarXPaciente(Connection con, ConsultaProgramacionCirugias mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaProgramacionCirugiasDao().consultarXPaciente(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarServiciosXPeticion(Connection con, ConsultaProgramacionCirugias mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaProgramacionCirugiasDao().consultarServiciosXPeticion(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarProfesionalesXPeticion(Connection con, ConsultaProgramacionCirugias mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaProgramacionCirugiasDao().consultarProfesionalesXPeticion(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarIngreso(Connection con, ConsultaProgramacionCirugias mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaProgramacionCirugiasDao().consultarIngreso(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarMaterialesEspeciales(Connection con, ConsultaProgramacionCirugias mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaProgramacionCirugiasDao().consultarMaterialesEspeciales(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarXRango(Connection con, ConsultaProgramacionCirugias mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaProgramacionCirugiasDao().consultarXRango(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarPedidos(Connection con, ConsultaProgramacionCirugias mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaProgramacionCirugiasDao().consultarPedidos(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarArticulosPedido(Connection con, ConsultaProgramacionCirugias mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaProgramacionCirugiasDao().consultarArticulosPedido(con, mundo);
	}
	
	/**
	 * @return the logger
	 */
	public static Logger getLogger() {
		return logger;
	}
	/**
	 * @return the institucion
	 */
	public String getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the paciente
	 */
	public int getPaciente() {
		return paciente;
	}

	/**
	 * @param paciente the paciente to set
	 */
	public void setPaciente(int paciente) {
		this.paciente = paciente;
	}

	/**
	 * @return the peticion
	 */
	public int getPeticion() {
		return peticion;
	}

	/**
	 * @param peticion the peticion to set
	 */
	public void setPeticion(int peticion) {
		this.peticion = peticion;
	}

	/**
	 * @return the solicitud
	 */
	public int getSolicitud() {
		return solicitud;
	}

	/**
	 * @param solicitud the solicitud to set
	 */
	public void setSolicitud(int solicitud) {
		this.solicitud = solicitud;
	}

	/**
	 * @return the filtrosMap
	 */
	public HashMap getFiltrosMap() {
		return filtrosMap;
	}

	/**
	 * @param filtrosMap the filtrosMap to set
	 */
	public void setFiltrosMap(HashMap filtrosMap) {
		this.filtrosMap = filtrosMap;
	}

	public int getPedido() {
		return pedido;
	}

	public void setPedido(int pedido) {
		this.pedido = pedido;
	}


}	