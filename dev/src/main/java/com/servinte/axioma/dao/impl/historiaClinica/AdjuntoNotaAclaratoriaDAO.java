package com.servinte.axioma.dao.impl.historiaClinica;

import com.servinte.axioma.dao.interfaz.historiaClinica.IAdjuntoNotaAclaratoriaDAO;
import com.servinte.axioma.orm.AdjuntoNotaAclaratoria;
import com.servinte.axioma.orm.delegate.historiaClinica.AdjuntoNotaAclaratoriaDelegate;

/**
 * Esta clase se encarga de definir los métodos de negocio
 * para la entidad  AdjuntoNotaAclaratoria
 * @author Ricardo Ruiz
 */
public class AdjuntoNotaAclaratoriaDAO implements IAdjuntoNotaAclaratoriaDAO{
	
	private AdjuntoNotaAclaratoriaDelegate delegate;
	
	/**
	 * Constructor de la clase
	 */
	public AdjuntoNotaAclaratoriaDAO(){
		delegate=new AdjuntoNotaAclaratoriaDelegate();
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.historiaClinica.IAdjuntoNotaAclaratoriaDAO#merge(com.servinte.axioma.orm.AdjuntoNotaAclaratoria)
	 */
	@Override
	public AdjuntoNotaAclaratoria merge(
			AdjuntoNotaAclaratoria adjuntoNotaAclaratoria) {
		return delegate.merge(adjuntoNotaAclaratoria);
	}

}
