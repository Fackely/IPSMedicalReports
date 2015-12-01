package com.princetonsa.dao.oracle.administracion;

import java.util.ArrayList;

import com.princetonsa.dao.administracion.ConceptosRetencionDao;
import com.princetonsa.dao.sqlbase.administracion.SqlBaseConceptosRetencionDao;
import com.princetonsa.dto.administracion.DtoConceptosRetencion;
import com.princetonsa.dto.administracion.DtoTiposRetencion;


public class OracleConceptosRetencionDao implements ConceptosRetencionDao
{
	public ArrayList<DtoTiposRetencion> consultaTiposRetencion(int institucion)
	{
		return SqlBaseConceptosRetencionDao.consultaTiposRetencion(institucion);
	}
	
	public ArrayList<DtoConceptosRetencion> consultarConceptosRetencion(int institucion,int tipoRetencion)
	{
		return SqlBaseConceptosRetencionDao.consultarConceptosRetencion(institucion,tipoRetencion);
	}
	
	public boolean insertarConceptoRetencion(int tipoRet, String codConcepto, int institucion, String desConcepto, 
			String codInterfaz, String cuentaRet, String cuentaDB, String cuentaCR, String usuario)
	{
		return SqlBaseConceptosRetencionDao.insertarConceptoRetencion(tipoRet, codConcepto, institucion, desConcepto, codInterfaz, cuentaRet, cuentaDB, cuentaCR, usuario);
	}
	
	public boolean insertarLogConceptoRetencion(int conceptoRet, int tipoRet, String codConcepto, String desConcepto, 
			String codInterfaz, String cuentaRet, String cuentaDB, String cuentaCR, String usuario)
	{
		return SqlBaseConceptosRetencionDao.insertarLogConceptoRetencion(conceptoRet, tipoRet, codConcepto, desConcepto, codInterfaz, cuentaRet, cuentaDB, cuentaCR, usuario);
	}
	
	public boolean actualizarConceptoRetencion(int tipoRet, String codConcepto, String descConcepto, 
			String codInterfaz, String cuentaRet, String cuentaDB, String cuentaCR, String usuario, int consecutivo)
	{	
		return SqlBaseConceptosRetencionDao.actualizarConceptoRetencion(tipoRet, codConcepto, descConcepto, codInterfaz, cuentaRet, cuentaDB, cuentaCR, usuario, consecutivo);
	}
	
	public int consultarDetalleConcepto(int tipoRet)
	{
		return SqlBaseConceptosRetencionDao.consultarDetalleConcepto(tipoRet);
	}
	
	public boolean eliminarConceptoRetencion(String usuario, int consecutivo)
	{	
		return SqlBaseConceptosRetencionDao.eliminarConceptoRetencion(usuario, consecutivo);
	}
	
	public int consultarVigDetConcepto(int consecutivo) 
	{
		return SqlBaseConceptosRetencionDao.consultarVigDetConcepto(consecutivo);
	}
	
	public String consultarCuentaContable(int cuenta)
	{
		return SqlBaseConceptosRetencionDao.consultarCuentaContable(cuenta);
	}
}