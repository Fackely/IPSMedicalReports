package com.servinte.axioma.mundo.interfaz.facturacion;

import com.servinte.axioma.orm.LogRipsEntidadesSubArchivo;

public interface ILogRipsEntidadesSubArchivoMundo {
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro de log del proceso por achivo de rips entidades subcontratadas
	 * 
	 * @param logRipsEntSubArchivo log generado en el proceso
	 * @return boolean
	 * @author, Fabi�n Becerra
	 *
	 */
	public boolean guardarLogRipsEntidadesSubArchivo(LogRipsEntidadesSubArchivo logRipsEntSubArchivo);

}
