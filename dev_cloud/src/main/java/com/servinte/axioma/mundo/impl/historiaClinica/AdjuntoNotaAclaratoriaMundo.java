package com.servinte.axioma.mundo.impl.historiaClinica;

import com.servinte.axioma.dao.impl.historiaClinica.AdjuntoNotaAclaratoriaDAO;
import com.servinte.axioma.mundo.interfaz.historiaClinica.IAdjuntoNotaAclaratoriaMundo;
import com.servinte.axioma.orm.AdjuntoNotaAclaratoria;

/**
 * Esta clase se encarga de definir los métodos de negocio
 * para la entidad  AdjuntoNotaAclaratoria
 * @author Ricardo Ruiz
 */
public class AdjuntoNotaAclaratoriaMundo implements IAdjuntoNotaAclaratoriaMundo{
	
	private AdjuntoNotaAclaratoriaDAO dao;
	
	/**
	 * Constructor de la clase
	 */
	public AdjuntoNotaAclaratoriaMundo(){
		dao = new AdjuntoNotaAclaratoriaDAO();
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.historiaClinica.IAdjuntoNotaAclaratoriaMundo#merge(com.servinte.axioma.orm.AdjuntoNotaAclaratoria)
	 */
	@Override
	public AdjuntoNotaAclaratoria merge(
			AdjuntoNotaAclaratoria adjuntoNotaAclaratoria) {
		return dao.merge(adjuntoNotaAclaratoria);
	}

}
