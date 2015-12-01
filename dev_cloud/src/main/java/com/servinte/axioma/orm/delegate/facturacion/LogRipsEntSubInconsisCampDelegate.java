package com.servinte.axioma.orm.delegate.facturacion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.LogRipsEntSubInconsisCamp;
import com.servinte.axioma.orm.LogRipsEntSubInconsisCampHome;

public class LogRipsEntSubInconsisCampDelegate extends LogRipsEntSubInconsisCampHome{
	
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
	public boolean guardarLogRipsEntSubInconsistenciaCampo(LogRipsEntSubInconsisCamp logRipsEntSubInconCamp){
		boolean save = true;					
		try{
			super.attachDirty(logRipsEntSubInconCamp);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro de " +
					"log de rips entidades subcontratadas inconsistencia de campos: ",e);
		}				
		return save;				
	}

}
