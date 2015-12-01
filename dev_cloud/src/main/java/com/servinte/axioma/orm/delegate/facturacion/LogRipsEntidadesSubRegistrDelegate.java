package com.servinte.axioma.orm.delegate.facturacion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.LogRipsEntidadesSubRegistr;
import com.servinte.axioma.orm.LogRipsEntidadesSubRegistrHome;

public class LogRipsEntidadesSubRegistrDelegate extends LogRipsEntidadesSubRegistrHome{
	
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
	public boolean guardarLogRipsEntidadesSubRegistr(LogRipsEntidadesSubRegistr logRipsEntSubReg){
		boolean save = true;					
		try{
			super.attachDirty(logRipsEntSubReg);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro de " +
					"log de rips entidades subcontratadas por registro: ",e);
		}				
		return save;				
	}

}
