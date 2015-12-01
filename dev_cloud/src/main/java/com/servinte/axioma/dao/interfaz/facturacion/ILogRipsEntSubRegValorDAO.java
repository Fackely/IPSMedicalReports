package com.servinte.axioma.dao.interfaz.facturacion;

import com.servinte.axioma.orm.LogRipsEntSubRegValor;

public interface ILogRipsEntSubRegValorDAO {
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro de log del proceso de rips entidades subcontratadas
	 * valores de los campos leidos
	 * 
	 * @param logRipsEntSubRegVal valores y campos leidos en el proceso
	 * @return boolean
	 * @author, Fabi�n Becerra
	 *
	 */
	public boolean guardarLogRipsEntSubRegValores(LogRipsEntSubRegValor logRipsEntSubRegVal);

}
