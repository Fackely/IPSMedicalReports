package com.servinte.axioma.mundo.impl.facturacion;

import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntidadesSubArchivoDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntidadesSubArchivoMundo;
import com.servinte.axioma.orm.LogRipsEntidadesSubArchivo;

public class LogRipsEntidadesSubArchivoMundo implements ILogRipsEntidadesSubArchivoMundo{
	
	ILogRipsEntidadesSubArchivoDAO dao;
	
	public LogRipsEntidadesSubArchivoMundo(){
		dao = FacturacionFabricaDAO.crearLogRipsEntidadesSubArchiDAO();
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
		return dao.guardarLogRipsEntidadesSubArchivo(logRipsEntSubArchivo);
	}
	
	

}
