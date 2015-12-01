package com.servinte.axioma.orm.delegate.facturacion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.LogRipsEntSubInconsisArch;
import com.servinte.axioma.orm.LogRipsEntSubInconsisArchHome;

public class LogRipsEntSubInconsisArchDelegate extends LogRipsEntSubInconsisArchHome{
	
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
	public boolean guardarLogRipsEntSubInconsistenciaArchivo(LogRipsEntSubInconsisArch logRipsEntSubInconArch){
		boolean save = true;					
		try{
			super.attachDirty(logRipsEntSubInconArch);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar/actualizar el registro de " +
					"log de rips entidades subcontratadas inconsistencia de archivo: ",e);
		}				
		return save;				
	}

}
