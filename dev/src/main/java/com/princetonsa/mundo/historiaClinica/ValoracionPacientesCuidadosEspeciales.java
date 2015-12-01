package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.ValoracionPacientesCuidadosEspecialesDao;

/**
 * mundo de Motivos Satisfaccion Insatisfaccion
 * @author axioma
 *
 */
public class ValoracionPacientesCuidadosEspeciales
{
	private static Logger logger = Logger.getLogger(ValoracionPacientesCuidadosEspeciales.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ValoracionPacientesCuidadosEspecialesDao valoracionPacientesCuidadosEspecialesDao;
	
	/**
	 * Codigo de la Institución
	 */
	private String institucion;
	
	/**
	 * Login Usuario
	 */
	private String usuario;
	
	/**
	 * Mapa de Motivos de satisfaccion e insatisfacion
	 */
	private HashMap valoracionPacientesCuidadosEspecialesMap;
	
	/**
	 * Tipo de monitoreo
	 */
	private int tipoMonitoreo;
	
	/**
	 *
	 */
	public ValoracionPacientesCuidadosEspeciales()
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
		this.usuario="";
		this.valoracionPacientesCuidadosEspecialesMap=new HashMap();
		this.valoracionPacientesCuidadosEspecialesMap.put("numRegistros", "0");
		this.tipoMonitoreo=ConstantesBD.codigoNuncaValido;
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
			valoracionPacientesCuidadosEspecialesDao = myFactory.getValoracionPacientesCuidadosEspecialesDao();
			wasInited = (valoracionPacientesCuidadosEspecialesDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultar(Connection con, ValoracionPacientesCuidadosEspeciales mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValoracionPacientesCuidadosEspecialesDao().consultar(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarTiposMonitoreo(Connection con, ValoracionPacientesCuidadosEspeciales mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValoracionPacientesCuidadosEspecialesDao().consultarTiposMonitoreo(con, mundo);
	}

	/**
	 * @return the valoracionPacientesCuidadosEspecialesDao
	 */
	public static ValoracionPacientesCuidadosEspecialesDao getValoracionPacientesCuidadosEspecialesDao() {
		return valoracionPacientesCuidadosEspecialesDao;
	}

	/**
	 * @param valoracionPacientesCuidadosEspecialesDao the valoracionPacientesCuidadosEspecialesDao to set
	 */
	public static void setValoracionPacientesCuidadosEspecialesDao(
			ValoracionPacientesCuidadosEspecialesDao valoracionPacientesCuidadosEspecialesDao) {
		ValoracionPacientesCuidadosEspeciales.valoracionPacientesCuidadosEspecialesDao = valoracionPacientesCuidadosEspecialesDao;
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
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the valoracionPacientesCuidadosEspecialesMap
	 */
	public HashMap getValoracionPacientesCuidadosEspecialesMap() {
		return valoracionPacientesCuidadosEspecialesMap;
	}

	/**
	 * @param valoracionPacientesCuidadosEspecialesMap the valoracionPacientesCuidadosEspecialesMap to set
	 */
	public void setValoracionPacientesCuidadosEspecialesMap(
			HashMap valoracionPacientesCuidadosEspecialesMap) {
		this.valoracionPacientesCuidadosEspecialesMap = valoracionPacientesCuidadosEspecialesMap;
	}

	/**
	 * @return the tipoMonitoreo
	 */
	public int getTipoMonitoreo() {
		return tipoMonitoreo;
	}

	/**
	 * @param tipoMonitoreo the tipoMonitoreo to set
	 */
	public void setTipoMonitoreo(int tipoMonitoreo) {
		this.tipoMonitoreo = tipoMonitoreo;
	}
	
	
}	