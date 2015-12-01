package com.servinte.axioma.orm.delegate.facturacion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.LogRipsEntSubInconsisArch;
import com.servinte.axioma.orm.LogRipsEntSubRegValor;
import com.servinte.axioma.orm.LogRipsEntSubRegValorHome;

public class LogRipsEntSubRegValorDelegate extends LogRipsEntSubRegValorHome{
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de log del proceso de rips entidades subcontratadas
	 * valores de los campos leidos
	 * 
	 * @param logRipsEntSubRegVal valores y campos leidos en el proceso
	 * @return boolean
	 * @author, Fabián Becerra
	 *
	 */
	public boolean guardarLogRipsEntSubRegValores(LogRipsEntSubRegValor logRipsEntSubRegVal){
		boolean save = true;					
		try{
			super.attachDirty(logRipsEntSubRegVal);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar/actualizar el registro de " +
					"log rips valores de los registros leidos: ",e);
		}				
		return save;				
	}

}
