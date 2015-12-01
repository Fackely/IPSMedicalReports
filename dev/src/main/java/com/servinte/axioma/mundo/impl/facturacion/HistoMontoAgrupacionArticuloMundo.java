package com.servinte.axioma.mundo.impl.facturacion;

import java.sql.Connection;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.HistoMontoAgrupacionArticuloDAO;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoMontoAgrupacionArticuloMundo;
import com.servinte.axioma.orm.HistoDetalleMonto;
import com.servinte.axioma.persistencia.UtilidadPersistencia;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 13/09/2010
 */
public class HistoMontoAgrupacionArticuloMundo implements
		IHistoMontoAgrupacionArticuloMundo {
	
	HistoMontoAgrupacionArticuloDAO dao;
	
	public HistoMontoAgrupacionArticuloMundo(){
		dao = DaoFactory.getDaoFactory(
				System.getProperty("TIPOBD")).getHistoMontoAgrupacionArticuloDAO();
	}
	
	/**
	 * 
	 * Este Método se encarga de insertar un registro del histórico de
	 * una agrupación de artículos del detalle de un monto de cobro
	 * 
	 * @param Connection conn, DTOHistoMontoAgrupacionArticulo dto, int TIPO_BD
	 * @return int	
	 * @author, Angela Maria Aguirre
	 *
	 */	
	public int insertarHistoMontoAgrupacionArticulo(DTOBusquedaMontoAgrupacionArticulo registro, 
			HistoDetalleMonto histoDetalle){
		
		Connection conn = UtilidadPersistencia.getPersistencia().obtenerConexion();
		int tipoBD = DaoFactory.getConstanteTipoBD(System.getProperty("TIPOBD"));
		int filasAfectadas = dao.insertarHistoMontoAgrupacionArticulo(conn, registro, histoDetalle, tipoBD);		
		
		return filasAfectadas;
	}

}
