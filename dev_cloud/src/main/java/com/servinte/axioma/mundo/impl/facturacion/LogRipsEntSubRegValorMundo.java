package com.servinte.axioma.mundo.impl.facturacion;

import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntSubInconsisCampDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntSubRegValorDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntSubRegValorMundo;
import com.servinte.axioma.orm.LogRipsEntSubRegValor;

public class LogRipsEntSubRegValorMundo implements ILogRipsEntSubRegValorMundo{
	
	ILogRipsEntSubRegValorDAO dao;
	
	public LogRipsEntSubRegValorMundo(){
		dao = FacturacionFabricaDAO.crearLogRipsEntSubRegValorDAO();
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
		return dao.guardarLogRipsEntSubRegValores(logRipsEntSubRegVal);
	}

}
