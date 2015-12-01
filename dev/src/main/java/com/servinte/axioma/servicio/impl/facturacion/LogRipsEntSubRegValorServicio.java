package com.servinte.axioma.servicio.impl.facturacion;

import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntSubInconsisCampMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntSubRegValorMundo;
import com.servinte.axioma.orm.LogRipsEntSubRegValor;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntSubRegValorServicio;

public class LogRipsEntSubRegValorServicio implements ILogRipsEntSubRegValorServicio{
	
	ILogRipsEntSubRegValorMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Fabian Becerra
	 */
	public LogRipsEntSubRegValorServicio(){
		mundo = FacturacionFabricaMundo.crearLogRipsEntSubRegValorMundo();
	}
	
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
		return mundo.guardarLogRipsEntSubRegValores(logRipsEntSubRegVal);
	}


}
