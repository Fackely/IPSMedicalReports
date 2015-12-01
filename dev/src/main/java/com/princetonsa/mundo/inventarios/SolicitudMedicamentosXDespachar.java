package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.EventosAdversosDao;
import com.princetonsa.dao.inventarios.SolicitudMedicamentosXDespacharDao;
import com.princetonsa.dao.manejoPaciente.CalidadAtencionDao;
import com.princetonsa.dao.manejoPaciente.EstadisticasIngresosDao;
import com.princetonsa.dao.manejoPaciente.EstadisticasServiciosDao;
import com.princetonsa.dao.manejoPaciente.MotivosSatisfaccionInsatisfaccionDao;

/**
 * mundo de Solicitud Medicamentos por despachar
 * @author axioma
 *
 */
public class SolicitudMedicamentosXDespachar
{
	private static Logger logger = Logger.getLogger(SolicitudMedicamentosXDespachar.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static SolicitudMedicamentosXDespacharDao solicitudMedicamentosXDespacharDao;
	
	/**
	 *
	 */
	public SolicitudMedicamentosXDespachar()
	{
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
			solicitudMedicamentosXDespacharDao = myFactory.getSolicitudMedicamentosXDespacharDao();
			wasInited = (solicitudMedicamentosXDespacharDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearConsultaMedicamentosXDespachar(Connection con, HashMap filtros)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudMedicamentosXDespacharDao().crearConsultaMedicamentosXDespachar(con, filtros);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearConsultaMedicamentosXEntregar(Connection con, HashMap filtros)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudMedicamentosXDespacharDao().crearConsultaMedicamentosXEntregar(con, filtros);
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static String crearConsultaMedicamentosDespachadosXPaciente(Connection con, HashMap filtros)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudMedicamentosXDespacharDao().crearConsultaMedicamentosDespachadosXPaciente(con, filtros);
	}
	
	/**
	 * Método encargado de Obtener el codigo de un centro de atención dado su nombre
	 * @author Felipe Pérez Granda
	 * @param con
	 * @param nombreCentroAtencion
	 * @return int codigoCentroAtencion
	 */
	public static int obtenerCodigoCentroAtencion(Connection con, String nombreCentroAtencion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSolicitudMedicamentosXDespacharDao().obtenerCodigoCentroAtencion(con, 
				nombreCentroAtencion);
	}

}	