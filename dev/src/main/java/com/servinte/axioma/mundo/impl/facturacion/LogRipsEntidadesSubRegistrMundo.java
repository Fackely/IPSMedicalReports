package com.servinte.axioma.mundo.impl.facturacion;

import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntidadesSubRegistrDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntidadesSubRegistrMundo;
import com.servinte.axioma.orm.LogRipsEntidadesSubRegistr;

public class LogRipsEntidadesSubRegistrMundo implements ILogRipsEntidadesSubRegistrMundo{

	ILogRipsEntidadesSubRegistrDAO dao;
	
	public LogRipsEntidadesSubRegistrMundo(){
		dao = FacturacionFabricaDAO.crearLogRipsEntidadesSubRegistrDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de log del proceso de rips entidades subcontratadas
	 * por registro
	 * 
	 * @param logRipsEntSubReg log generado en el proceso
	 * @return boolean
	 * @author, Fabián Becerra
	 *
	 */
	public boolean guardarLogRipsEntidadesSubRegistr(LogRipsEntidadesSubRegistr logRipsEntSubReg){
		return dao.guardarLogRipsEntidadesSubRegistr(logRipsEntSubReg);
	}
}
