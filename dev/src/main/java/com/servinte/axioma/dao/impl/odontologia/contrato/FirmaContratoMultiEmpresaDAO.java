package com.servinte.axioma.dao.impl.odontologia.contrato;

import java.util.List;
import com.servinte.axioma.dao.interfaz.odontologia.contrato.IFirmaContratoMultiEmpresaDAO;
import com.servinte.axioma.orm.FirmasContOtrsiempr;
import com.servinte.axioma.orm.delegate.odontologia.contrato.FirmasContratoMultiEmpDelegate;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public class FirmaContratoMultiEmpresaDAO  implements  IFirmaContratoMultiEmpresaDAO{


	
	
	private FirmasContratoMultiEmpDelegate delegate;
	
	
	/**
	 * 
	 */
	public FirmaContratoMultiEmpresaDAO()
	{
		delegate= new FirmasContratoMultiEmpDelegate();
	}


	@Override
	public List<FirmasContOtrsiempr> cargarFirmasPorInstucion(FirmasContOtrsiempr firmas)
	{
		return delegate.cargarFirmasPorInstucion(firmas);
	}


	@Override
	public FirmasContOtrsiempr buscarxId(Number id) {
		
		return delegate.findById(id.longValue());
	}


	@Override
	public void eliminar(FirmasContOtrsiempr objeto) 
	{
		delegate.delete(objeto);
	}


	@Override
	public void insertar(FirmasContOtrsiempr objeto) 
	{
		delegate.attachDirty(objeto);
		
	}


	@Override
	public void modificar(FirmasContOtrsiempr objeto) {
		delegate.attachDirty(objeto);
	}
	
	
	
	
}
