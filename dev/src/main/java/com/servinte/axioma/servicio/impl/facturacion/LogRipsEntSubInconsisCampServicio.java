package com.servinte.axioma.servicio.impl.facturacion;

import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntSubInconsisCampMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntidadesSubcontratadasMundo;
import com.servinte.axioma.orm.LogRipsEntSubInconsisCamp;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntSubInconsisCampServicio;

public class LogRipsEntSubInconsisCampServicio implements ILogRipsEntSubInconsisCampServicio{

	ILogRipsEntSubInconsisCampMundo mundo;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Fabian Becerra
	 */
	public LogRipsEntSubInconsisCampServicio(){
		mundo = FacturacionFabricaMundo.crearLogRipsEntSubInconsisCampMundo();
	}
	
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
		return mundo.guardarLogRipsEntSubInconsistenciaCampo(logRipsEntSubInconCamp);
	}
	
}
