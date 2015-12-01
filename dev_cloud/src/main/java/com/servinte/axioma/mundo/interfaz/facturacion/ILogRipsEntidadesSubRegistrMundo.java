package com.servinte.axioma.mundo.interfaz.facturacion;

import com.servinte.axioma.orm.LogRipsEntidadesSubRegistr;

public interface ILogRipsEntidadesSubRegistrMundo {
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de log del proceso de rips entidades subcontratadas
	 * por registro
	 * 
	 * @param logRipsEntSubReg log generado en el proceso
	 * @return boolean
	 * @author, Fabián Becerra
	 *
	 */
	public boolean guardarLogRipsEntidadesSubRegistr(LogRipsEntidadesSubRegistr logRipsEntSubReg);

}
