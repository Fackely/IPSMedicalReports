package com.servinte.axioma.mundo.impl.facturacion;

import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntSubInconsisArchDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntSubInconsisArchMundo;
import com.servinte.axioma.orm.LogRipsEntSubInconsisArch;

public class LogRipsEntSubInconsisArchMundo implements ILogRipsEntSubInconsisArchMundo{
	
	ILogRipsEntSubInconsisArchDAO dao;
	
	public LogRipsEntSubInconsisArchMundo(){
		dao = FacturacionFabricaDAO.crearLogRipsEntiSubInconsisArchiDAO();
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
		return dao.guardarLogRipsEntSubInconsistenciaArchivo(logRipsEntSubInconArch);
	}
	
	
	
}
