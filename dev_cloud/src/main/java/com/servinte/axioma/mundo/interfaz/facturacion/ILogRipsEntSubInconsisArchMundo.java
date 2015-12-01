package com.servinte.axioma.mundo.interfaz.facturacion;

import com.servinte.axioma.orm.LogRipsEntSubInconsisArch;

public interface ILogRipsEntSubInconsisArchMundo {

	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de log del proceso de rips entidades subcontratadas
	 * inconsistencias de archivos
	 * 
	 * @param logRipsEntSubInconArch log inconsistencia de archivo generada en el proceso
	 * @return boolean
	 * @author, Fabián Becerra
	 *
	 */
	public boolean guardarLogRipsEntSubInconsistenciaArchivo(LogRipsEntSubInconsisArch logRipsEntSubInconArch);
	
}
