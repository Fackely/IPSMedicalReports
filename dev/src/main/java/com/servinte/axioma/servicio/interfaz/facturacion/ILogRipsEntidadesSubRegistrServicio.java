package com.servinte.axioma.servicio.interfaz.facturacion;

import com.servinte.axioma.orm.LogRipsEntidadesSubRegistr;

public interface ILogRipsEntidadesSubRegistrServicio {

	/**
	 * 
	 * Este M?todo se encarga de insertar en la base de datos
	 * un registro de log del proceso de rips entidades subcontratadas
	 * por registro
	 * 
	 * @param logRipsEntSubReg log generado en el proceso
	 * @return boolean
	 * @author, Fabi?n Becerra
	 *
	 */
	public boolean guardarLogRipsEntidadesSubRegistr(LogRipsEntidadesSubRegistr logRipsEntSubReg);
	
}
