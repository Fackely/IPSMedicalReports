package com.servinte.axioma.servicio.impl.facturacion;

import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntSubInconsisArchMundo;
import com.servinte.axioma.orm.LogRipsEntSubInconsisArch;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntSubInconsisArchServicio;

public class LogRipsEntSubInconsisArchServicio implements ILogRipsEntSubInconsisArchServicio{
	
	ILogRipsEntSubInconsisArchMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Fabian Becerra
	 */
	public LogRipsEntSubInconsisArchServicio(){
		mundo = FacturacionFabricaMundo.crearLogRipsEntSubInconsisArchMundo();
	}
	
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
		return mundo.guardarLogRipsEntSubInconsistenciaArchivo(logRipsEntSubInconArch);
	}

}
