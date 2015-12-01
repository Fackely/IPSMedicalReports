package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.ReingresoSalidaHospiDiaDao;
import com.princetonsa.dto.manejoPaciente.DtoReingresoSalidaHospiDia;

/**
 * 
 * @author wilson
 *
 */
public class ReingresoHospitalDia 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ReingresoSalidaHospiDiaDao dao;
	
	 /**
	 * resetea los atributos del objeto
	 *
	 */
	public void reset()
	{}

	public ReingresoHospitalDia() 
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
			dao = myFactory.getReingresoSalidaHospiDiaDao();
			wasInited = (dao != null);
		}
		return wasInited;
	}
	
	
	/**
	 * Método para obtener el DAO
	 * @return
	 */
	public static ReingresoSalidaHospiDiaDao utilidadDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReingresoSalidaHospiDiaDao();
	}
	
	/**
	 * 
	 * @param con
	 * @param criteriosBusqueda key{codigoCentroAtencion, esSalida, tipoIdentificacion, numeroIdentificacion, primerNombre, primerApellido}
	 * @return
	 */
	public static HashMap listadoPacientesReingresoOSalida(Connection con, HashMap criteriosBusqueda)
	{
		return utilidadDao().listadoPacientesReingresoOSalida(con, criteriosBusqueda);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean  insertar(Connection con, DtoReingresoSalidaHospiDia dto)
	{
		return utilidadDao().insertar(con, dto);
	}
	
	/**
	 * Método que consulta el último codigo del reingreso salida hospital día por la cuenta
	 * @param con
	 * @param dto
	 * @return
	 */
	public static String obtenerUltimoCodigoReingresoSalidaHospitalDia(Connection con,DtoReingresoSalidaHospiDia dto)
	{
		return utilidadDao().obtenerUltimoCodigoReingresoSalidaHospitalDia(con, dto);
	}
	
	/**
	 * Método que realiza la eliminación de todos los registros de reingreso hospital día de una cuenta
	 * @param con
	 * @param dto
	 * @return
	 */
	public static int eliminarReingresoSalidaHospitalDiaXCuenta(Connection con,DtoReingresoSalidaHospiDia dto)
	{
		return utilidadDao().eliminarReingresoSalidaHospitalDiaXCuenta(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @param tipo
	 * @return
	 */
	public static boolean existeReingresoOSalidaActivo(Connection con, int cuenta, String tipo,int codigo)
	{
		return utilidadDao().existeReingresoOSalidaActivo(con, cuenta, tipo,codigo);
	}
}
