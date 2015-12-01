package com.servinte.axioma.servicio.interfaz.facturacion;

import com.servinte.axioma.orm.LogRipsEntSubRegValor;

public interface ILogRipsEntSubRegValorServicio {

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
	public boolean guardarLogRipsEntSubRegValores(LogRipsEntSubRegValor logRipsEntSubRegVal);
	
}
