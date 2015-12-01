package com.servinte.axioma.mundo.interfaz.historiaClinica;

import com.servinte.axioma.orm.AdjuntoNotaAclaratoria;

/**
 * Esta interface se encarga de definir los métodos de negocio
 * para la entidad  AdjuntoNotaAclaratoria
 * @author Ricardo Ruiz
 */
public interface IAdjuntoNotaAclaratoriaMundo {

	/**
	 * Método que permite sincronizar con la BD el Adjunto de la Nota Aclaratoria
	 * @param adjuntoNotaAclaratoria
	 * @return
	 * @author Ricardo Ruiz
	 */
	public AdjuntoNotaAclaratoria merge(AdjuntoNotaAclaratoria adjuntoNotaAclaratoria);
}
