package com.servinte.axioma.servicio.interfaz.facturacion;

import com.servinte.axioma.orm.LogRipsEntidadesSubArchivo;

public interface ILogRipsEntidadesSubArchivoServicio {

	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de log del proceso por achivo de rips entidades subcontratadas
	 * 
	 * @param logRipsEntSubArchivo log generado en el proceso
	 * @return boolean
	 * @author, Fabián Becerra
	 *
	 */
	public boolean guardarLogRipsEntidadesSubArchivo(LogRipsEntidadesSubArchivo logRipsEntSubArchivo);
	
}
