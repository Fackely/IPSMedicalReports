/**
 * 
 */
package com.servinte.axioma.dao.impl.inventario;

import com.princetonsa.dto.inventario.DTOEsqTarProcedimientoContrato;
import com.servinte.axioma.dao.interfaz.inventario.IEsqTarProcedimientoContratoDAO;
import com.servinte.axioma.orm.delegate.inventario.EsqTarProcedimientoContratoDelegate;


public class EsqTarProcedimientoContratoHibernateDAO implements
		IEsqTarProcedimientoContratoDAO {
	
	
	EsqTarProcedimientoContratoDelegate delegate;
	
	public EsqTarProcedimientoContratoHibernateDAO(){
		delegate = new EsqTarProcedimientoContratoDelegate();
	}
	
	
	public DTOEsqTarProcedimientoContrato buscarEsquematarifarioPorContrato(DTOEsqTarProcedimientoContrato dto){
		return delegate.buscarEsquematarifarioPorContrato(dto);
	}
	

}
