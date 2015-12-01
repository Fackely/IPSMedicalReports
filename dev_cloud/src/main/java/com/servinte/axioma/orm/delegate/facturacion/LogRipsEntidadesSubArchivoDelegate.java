package com.servinte.axioma.orm.delegate.facturacion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.LogRipsEntidadesSubArchivo;
import com.servinte.axioma.orm.LogRipsEntidadesSubArchivoHome;

public class LogRipsEntidadesSubArchivoDelegate extends LogRipsEntidadesSubArchivoHome{
	
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
	public boolean guardarLogRipsEntidadesSubArchivo(LogRipsEntidadesSubArchivo logRipsEntSubArchivo){
		boolean save = true;					
		try{
			super.attachDirty(logRipsEntSubArchivo);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar/actualizar el registro de " +
					"log de archivo rips entidades subcontratadas: ",e);
		}				
		return save;				
	}


}
