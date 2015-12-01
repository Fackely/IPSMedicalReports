package com.servinte.axioma.servicio.impl.facturacion;

import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntidadesSubArchivoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntidadesSubcontratadasMundo;
import com.servinte.axioma.orm.LogRipsEntidadesSubArchivo;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntidadesSubArchivoServicio;

public class LogRipsEntidadesSubArchivoServicio implements ILogRipsEntidadesSubArchivoServicio{

	ILogRipsEntidadesSubArchivoMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Fabian Becerra
	 */
	public LogRipsEntidadesSubArchivoServicio(){
		mundo = FacturacionFabricaMundo.crearLogRipsEntidadesSubArchivoMundo();
	}
	
	
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
		return mundo.guardarLogRipsEntidadesSubArchivo(logRipsEntSubArchivo);
	}
	
}
