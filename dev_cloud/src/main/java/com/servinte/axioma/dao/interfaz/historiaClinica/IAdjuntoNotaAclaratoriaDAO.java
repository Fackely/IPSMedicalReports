package com.servinte.axioma.dao.interfaz.historiaClinica;

import com.servinte.axioma.orm.AdjuntoNotaAclaratoria;

/**
 * Esta interfaz se encarga de definir los m�todos de negocio
 * para la entidad  AdjuntoNotaAclaratoria
 * @author Ricardo Ruiz
 */
public interface IAdjuntoNotaAclaratoriaDAO {

	/**
	 * M�todo que permite sincronizar con la BD el Adjunto de la Nota Aclaratoria
	 * @param adjuntoNotaAclaratoria
	 * @return
	 * @author Ricardo Ruiz
	 */
	public AdjuntoNotaAclaratoria merge(AdjuntoNotaAclaratoria adjuntoNotaAclaratoria);
}
