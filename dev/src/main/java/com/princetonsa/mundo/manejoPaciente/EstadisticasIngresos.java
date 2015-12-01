package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.EventosAdversosDao;
import com.princetonsa.dao.manejoPaciente.CalidadAtencionDao;
import com.princetonsa.dao.manejoPaciente.EstadisticasIngresosDao;
import com.princetonsa.dao.manejoPaciente.EstadisticasServiciosDao;
import com.princetonsa.dao.manejoPaciente.MotivosSatisfaccionInsatisfaccionDao;

/**
 * mundo de Estadisticas Ingresos
 * @author axioma
 *
 */
public class EstadisticasIngresos
{
	private static Logger logger = Logger.getLogger(EstadisticasServicios.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static EstadisticasIngresosDao estadisticasIngresosDao;

	/**
	 * Login Usuario
	 */
	private String usuario;
	
	/**
	 * 
	 */
	private HashMap filtrosMap;
	
	/**
	 * 
	 */
	private String viasDeIngreso;
	
	/**
	 * 
	 */
	private String tiposPaciente;
	
	/**
	 * 
	 */
	private HashMap dxEgreso;
	
	/**
	 * 
	 */
	private int institucion;
	
	/**
	 *
	 */
	public EstadisticasIngresos()
	{
		this.reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 *
	 */
	public void reset() 
	{
		this.usuario="";
		this.filtrosMap=new HashMap();
		this.dxEgreso=new HashMap();
		this.institucion=ConstantesBD.codigoNuncaValido;
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
			estadisticasIngresosDao = myFactory.getEstadisticasIngresosDao();
			wasInited = (estadisticasIngresosDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearWhereIngresosXConvenio(Connection con, EstadisticasIngresos mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEstadisticasIngresosDao().crearWhereIngresosXConvenio(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearWhereReingresos(Connection con, EstadisticasIngresos mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEstadisticasIngresosDao().crearWhereReingresos(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearWhereTotalReingresosXConvenio(Connection con, EstadisticasIngresos mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEstadisticasIngresosDao().crearWhereTotalReingresosXConvenio(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearConsultaAtencionXRangoEdad(Connection con, EstadisticasIngresos mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEstadisticasIngresosDao().crearConsultaAtencionXRangoEdad(con, mundo);
	}

	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static HashMap consultarAtencionXEmpresaYConvenio(Connection con, EstadisticasIngresos mundo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEstadisticasIngresosDao().consultarAtencionXEmpresaYConvenio(con, mundo);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static int consultarEgresosMes(Connection con, String anioMes, HashMap filtros)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEstadisticasIngresosDao().consultarEgresosMes(con, anioMes, filtros);
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

	/**
	 * @return the filtrosMap
	 */
	public Object getFiltrosMap(String llave) {
		return filtrosMap.get(llave);
	}

	/**
	 * @param filtrosMap the filtrosMap to set
	 */
	public void setFiltrosMap(String llave, Object obj) {
		this.filtrosMap.put(llave, obj);
	}

	/**
	 * @return the viasDeIngreso
	 */
	public String getViasDeIngreso() {
		return viasDeIngreso;
	}

	/**
	 * @param viasDeIngreso the viasDeIngreso to set
	 */
	public void setViasDeIngreso(String viasDeIngreso) {
		this.viasDeIngreso = viasDeIngreso;
	}

	/**
	 * @return the tiposPaciente
	 */
	public String getTiposPaciente() {
		return tiposPaciente;
	}

	/**
	 * @param tiposPaciente the tiposPaciente to set
	 */
	public void setTiposPaciente(String tiposPaciente) {
		this.tiposPaciente = tiposPaciente;
	}

	/**
	 * @return the dxEgreso
	 */
	public HashMap getDxEgreso() {
		return dxEgreso;
	}

	/**
	 * @param dxEgreso the dxEgreso to set
	 */
	public void setDxEgreso(HashMap dxEgreso) {
		this.dxEgreso = dxEgreso;
	}
	
	/**
	 * @param dxEgreso the dxEgreso to set
	 */
	public void setDxEgreso(String llave, Object obj) {
		this.dxEgreso.put(llave, obj);
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	
	
	
}	