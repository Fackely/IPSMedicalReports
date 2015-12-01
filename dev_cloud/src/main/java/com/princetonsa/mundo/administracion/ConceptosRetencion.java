package com.princetonsa.mundo.administracion;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.administracion.ConceptosRetencionDao;
import com.princetonsa.dao.facturacion.CentrosCostoEntidadesSubcontratadasDao;
import com.princetonsa.dao.sqlbase.administracion.SqlBaseConceptosRetencionDao;
import com.princetonsa.dto.administracion.DtoConceptosRetencion;
import com.princetonsa.dto.administracion.DtoTiposRetencion;


public class ConceptosRetencion
{
	Logger logger = Logger.getLogger(ConceptosRetencion.class);

	private static ConceptosRetencionDao getConceptosRetencionDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConceptosRetencionDao();
	}
	
	public static ArrayList<DtoTiposRetencion> consultarTiposRetencion(int institucion) {
		return getConceptosRetencionDao().consultaTiposRetencion(institucion);
	}

	public static ArrayList<DtoConceptosRetencion> consultarConceptosRetencion(int institucion, int tipoRetencion) {
		return getConceptosRetencionDao().consultarConceptosRetencion(institucion,tipoRetencion);
	}
	
	public static boolean insertarConceptoRetencion(int tipoRet, String codConcepto, int institucion, String desConcepto, 
			String codInterfaz, String cuentaRet, String cuentaDB, String cuentaCR,  String usuario)
	{
		return getConceptosRetencionDao().insertarConceptoRetencion(tipoRet, codConcepto, institucion, desConcepto, codInterfaz, cuentaRet, cuentaDB, cuentaCR, usuario);
	}
	
	public static boolean insertarLogConceptoRetencion(int conceptoRet, int tipoRet, String codConcepto, String desConcepto, 
			String codInterfaz, String cuentaRet, String cuentaDB, String cuentaCR, String usuario)
	{
		return getConceptosRetencionDao().insertarLogConceptoRetencion(conceptoRet, tipoRet, codConcepto, desConcepto, codInterfaz, cuentaRet, cuentaDB, cuentaCR, usuario);
	}
	
	public static boolean actualizarConceptoRetencion(int tipoRet, String codConcepto, String descConcepto, 
			String codInterfaz, String cuentaRet, String cuentaDB, String cuentaCR, String usuario, int consecutivo)
	{	
		return getConceptosRetencionDao().actualizarConceptoRetencion(tipoRet, codConcepto, descConcepto, codInterfaz, cuentaRet, cuentaDB, cuentaCR, usuario, consecutivo);
	}

	public int consultarDetalleConcepto(int tipoRet) {
		return getConceptosRetencionDao().consultarDetalleConcepto(tipoRet);
	}
	
	public boolean eliminarConceptoRetencion(String usuario, int consecutivo)
	{	
		return getConceptosRetencionDao().eliminarConceptoRetencion(usuario, consecutivo);
	}
	
	public int consultarVigDetConcepto(int consecutivo) 
	{
		return getConceptosRetencionDao().consultarVigDetConcepto(consecutivo);
	}

	public String consultarCuentaContable(int cuenta) 
	{
		return getConceptosRetencionDao().consultarCuentaContable(cuenta);
	}
}