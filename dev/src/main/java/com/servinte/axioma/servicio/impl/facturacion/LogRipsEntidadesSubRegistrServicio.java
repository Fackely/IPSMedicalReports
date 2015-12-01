package com.servinte.axioma.servicio.impl.facturacion;

import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntidadesSubArchivoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntidadesSubRegistrMundo;
import com.servinte.axioma.orm.LogRipsEntidadesSubRegistr;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntidadesSubArchivoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntidadesSubRegistrServicio;

public class LogRipsEntidadesSubRegistrServicio implements ILogRipsEntidadesSubRegistrServicio{
	
	ILogRipsEntidadesSubRegistrMundo mundo;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Fabian Becerra
	 */
	public LogRipsEntidadesSubRegistrServicio(){
		mundo = FacturacionFabricaMundo.crearLogRipsEntidadesSubRegistrMundo();
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro de log del proceso de rips entidades subcontratadas
	 * por registro
	 * 
	 * @param logRipsEntSubReg log generado en el proceso
	 * @return boolean
	 * @author, Fabi�n Becerra
	 *
	 */
	public boolean guardarLogRipsEntidadesSubRegistr(LogRipsEntidadesSubRegistr logRipsEntSubReg){
		return mundo.guardarLogRipsEntidadesSubRegistr(logRipsEntSubReg);
	}

	
}
