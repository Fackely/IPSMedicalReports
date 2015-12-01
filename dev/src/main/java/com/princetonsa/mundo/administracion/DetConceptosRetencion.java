package com.princetonsa.mundo.administracion;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.administracion.DetConceptosRetencionDao;
import com.princetonsa.dao.administracion.EspecialidadesDao;
import com.princetonsa.dao.interfaz.ConsultaInterfazSistema1EDao;
import com.princetonsa.dto.administracion.DtoDetConceptosRetencion;
import com.princetonsa.dto.administracion.DtoDetVigConRet;
import com.princetonsa.dto.administracion.DtoDetVigConRetencion;


public class DetConceptosRetencion
{
	Logger logger = Logger.getLogger(ConceptosRetencion.class);
	
	private static DetConceptosRetencionDao detConceptosRetencion;
	
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
			detConceptosRetencion = myFactory.getDetConceptosRetencion();
			wasInited = (detConceptosRetencion != null);
		}
		return wasInited;
	}
	
	public static DetConceptosRetencionDao getDetConceptosRetencionDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetConceptosRetencion();
	}
	
	public static ArrayList<DtoDetConceptosRetencion> consultaDetConceptosRetencion()
	{
		return getDetConceptosRetencionDao().consultaDetConceptosRetencion();
	}
	
	public static boolean insertarDetConceptosRetencion(DtoDetConceptosRetencion dto)
	{
		return getDetConceptosRetencionDao().insertarDetConceptosRetencion(dto);
	}
	
	public static boolean validarExisteDetConceptosRetencion(DtoDetConceptosRetencion dto)
	{
		return getDetConceptosRetencionDao().validarExisteDetConceptosRetencion(dto);
	}
	
	public static boolean validarDetalleXConcepto(String consecutivo)
	{
		return getDetConceptosRetencionDao().validarDetalleXConcepto(consecutivo);
	}
	
	public static ArrayList<DtoDetVigConRetencion> consultarDetVigenciaConceptosRetencion(String consecutivo)
	{
		return getDetConceptosRetencionDao().consultarDetVigenciaConceptosRetencion(consecutivo);
	}
	
	public static boolean insertarDetVigConRetencion(DtoDetVigConRetencion dto)
	{
		return getDetConceptosRetencionDao().insertarDetVigConRetencion(dto);
	}
	
	public static ArrayList<DtoDetVigConRet> consultarDetRetXGrupoServicio(String consecutivo)
	{
		return getDetConceptosRetencionDao().consultarDetRetXGrupoServicio(consecutivo);
	}
	
	public static boolean insertarDetRetXGrupoServicio(DtoDetVigConRet dto)
	{
		return getDetConceptosRetencionDao().insertarDetRetXGrupoServicio(dto);
	}
	
	public static boolean inactivarDetRetXGrupoServicio(DtoDetVigConRet dto)
	{
		return getDetConceptosRetencionDao().inactivarDetRetXGrupoServicio(dto);
	}
	
	public static ArrayList<DtoDetVigConRet> consultarDetRetXClaseInv(String consecutivo)
	{
		return getDetConceptosRetencionDao().consultarDetRetXClaseInv(consecutivo);
	}
	
	public static boolean insertarDetRetXClaseInv (DtoDetVigConRet dto)
	{
		return getDetConceptosRetencionDao().insertarDetRetXClaseInv(dto);
	}
	
	public static boolean inactivarDetRetXClaseInv(DtoDetVigConRet dto)
	{
		return getDetConceptosRetencionDao().inactivarDetRetXClaseInv(dto);
	}
	
	public static ArrayList<DtoDetVigConRet> consultarDetRetXConceptos(String consecutivo)
	{
		return getDetConceptosRetencionDao().consultarDetRetXConceptos(consecutivo);
	}
	
	public static boolean insertarDetRetXConcepto(DtoDetVigConRet dto)
	{
		return getDetConceptosRetencionDao().insertarDetRetXConcepto(dto);
	}
	
	public static boolean inactivarDetRetXConcepto(DtoDetVigConRet dto)
	{
		return getDetConceptosRetencionDao().inactivarDetRetXConcepto(dto);
	}
	
	public static boolean detPoseeDetalles(String consecutivo)
	{
		return getDetConceptosRetencionDao().detPoseeDetalles(consecutivo);
	}
	
	public static boolean inactivarDetVigConRetencion(DtoDetVigConRetencion dto)
	{
		return getDetConceptosRetencionDao().inactivarDetVigConRetencion(dto);
	}
	
	public static boolean ingresarLog(DtoDetVigConRetencion dto)
	{
		return getDetConceptosRetencionDao().ingresarLog(dto);
	}
	
	public static boolean actualizarGrupo(int consecutivoNuevo, int consecutivoViejo)
	{
		return  getDetConceptosRetencionDao().actualizarGrupo(consecutivoNuevo,consecutivoViejo);
	}
	
	public static boolean actualizarClase(int consecutivoNuevo, int consecutivoViejo)
	{
		return  getDetConceptosRetencionDao().actualizarClase(consecutivoNuevo,consecutivoViejo);
	}
	
	public static boolean actualizarCfv(int consecutivoNuevo, int consecutivoViejo)
	{
		return  getDetConceptosRetencionDao().actualizarCfv(consecutivoNuevo,consecutivoViejo);
	}
	
	public static boolean poseeDetalles(int consecutivo)
	{
		return getDetConceptosRetencionDao().poseeDetalles(consecutivo);
	}
}