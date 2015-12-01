/**
 * 
 */
package com.servinte.axioma.dao.impl.inventario;

import com.princetonsa.dto.inventario.DTOEsqTarInventarioContrato;
import com.servinte.axioma.dao.interfaz.inventario.IEsqTarInventariosContratoDAO;
import com.servinte.axioma.orm.delegate.inventario.EsqTarInventariosContratoDelegate;


public class EsqTarInventariosContratoHibernateDAO implements
		IEsqTarInventariosContratoDAO {
	
	
	EsqTarInventariosContratoDelegate delegate;
	
	public EsqTarInventariosContratoHibernateDAO(){
		delegate = new EsqTarInventariosContratoDelegate();
	}
	
	
	public DTOEsqTarInventarioContrato buscarEsquematarifarioPorContrato(DTOEsqTarInventarioContrato dto){
		return delegate.buscarEsquematarifarioPorContrato(dto);
	}
	

}
