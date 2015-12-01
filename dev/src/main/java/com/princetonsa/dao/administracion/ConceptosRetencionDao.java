package com.princetonsa.dao.administracion;

import java.util.ArrayList;

import com.princetonsa.dao.sqlbase.administracion.SqlBaseConceptosRetencionDao;
import com.princetonsa.dto.administracion.DtoConceptosRetencion;
import com.princetonsa.dto.administracion.DtoDetVigConRet;
import com.princetonsa.dto.administracion.DtoTiposRetencion;

public interface ConceptosRetencionDao
{
	ArrayList<DtoTiposRetencion> consultaTiposRetencion(int institucion);

	ArrayList<DtoConceptosRetencion> consultarConceptosRetencion(int institucion,int tipoRetencion);
	
	boolean insertarConceptoRetencion(int tipoRet, String codConcepto, int institucion, String desConcepto, 
			String codInterfaz, String cuentaRet, String cuentaDB, String cuentaCR, String usuario);
	
	boolean insertarLogConceptoRetencion(int conceptoRet, int tipoRet, String codConcepto, String desConcepto, 
			String codInterfaz, String cuentaRet, String cuentaDB, String cuentaCR, String usuario);
	
	boolean actualizarConceptoRetencion(int tipoRet, String codConcepto, String descConcepto, 
			String codInterfaz, String cuentaRet, String cuentaDB, String cuentaCR, String usuario, int consecutivo);

	int consultarDetalleConcepto(int tipoRet);
	
	boolean eliminarConceptoRetencion(String usuario, int consecutivo);
	
	int consultarVigDetConcepto(int consecutivo);

	String consultarCuentaContable(int cuenta);
}