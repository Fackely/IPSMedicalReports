package com.servinte.axioma.dao.impl.historiaClinica;

import java.util.List;

import com.servinte.axioma.dao.interfaz.historiaClinica.INotaAclaratoriaDAO;
import com.servinte.axioma.dto.manejoPaciente.DtoNotaAclaratoria;
import com.servinte.axioma.orm.NotaAclaratoria;
import com.servinte.axioma.orm.delegate.historiaClinica.NotaAclaratoriaDelegate;

/**
 * Esta clase se encarga de definir los métodos de negocio
 * para la entidad  NotaAclaratoria
 * @author Ricardo Ruiz
 */
public class NotaAclaratoriaDAO implements INotaAclaratoriaDAO{
	
	private NotaAclaratoriaDelegate delegate;
	
	public NotaAclaratoriaDAO(){
		delegate=new NotaAclaratoriaDelegate();
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.historiaClinica.INotaAclaratoriaDAO#buscarNotasAclaratoriasPorIngreso(int)
	 */
	@Override
	public List<DtoNotaAclaratoria> buscarNotasAclaratoriasPorIngreso(
			int codigoIngreso) {
		return delegate.buscarNotasAclaratoriasPorIngreso(codigoIngreso);
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.historiaClinica.INotaAclaratoriaDAO#merge(com.servinte.axioma.orm.NotaAclaratoria)
	 */
	@Override
	public NotaAclaratoria merge(NotaAclaratoria notaAclaratoria) {
		return delegate.merge(notaAclaratoria);
	}

}
