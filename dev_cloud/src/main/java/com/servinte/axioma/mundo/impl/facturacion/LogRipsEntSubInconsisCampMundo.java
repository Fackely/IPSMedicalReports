package com.servinte.axioma.mundo.impl.facturacion;

import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntSubInconsisCampDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntSubInconsisCampMundo;
import com.servinte.axioma.orm.LogRipsEntSubInconsisCamp;

public class LogRipsEntSubInconsisCampMundo implements ILogRipsEntSubInconsisCampMundo{

	ILogRipsEntSubInconsisCampDAO dao;
	
	public LogRipsEntSubInconsisCampMundo(){
		dao = FacturacionFabricaDAO.crearLogRipsEntSubInconsisCampDAO();
	}
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro de log del proceso de rips entidades subcontratadas
	 * inconsistencias de campos
	 * 
	 * @param logRipsEntSubInconCamp log inconsistencia de campo generada en el proceso
	 * @return boolean
	 * @author, Fabi�n Becerra
	 *
	 */
	public boolean guardarLogRipsEntSubInconsistenciaCampo(LogRipsEntSubInconsisCamp logRipsEntSubInconCamp){
		return dao.guardarLogRipsEntSubInconsistenciaCampo(logRipsEntSubInconCamp);
	}
	
}
