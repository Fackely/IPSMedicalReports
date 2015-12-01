package com.servinte.axioma.servicio.interfaz.facturacion;

import com.servinte.axioma.orm.LogRipsEntSubInconsisCamp;

public interface ILogRipsEntSubInconsisCampServicio {

	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de log del proceso de rips entidades subcontratadas
	 * inconsistencias de campos
	 * 
	 * @param logRipsEntSubInconCamp log inconsistencia de campo generada en el proceso
	 * @return boolean
	 * @author, Fabián Becerra
	 *
	 */
	public boolean guardarLogRipsEntSubInconsistenciaCampo(LogRipsEntSubInconsisCamp logRipsEntSubInconCamp);
}
