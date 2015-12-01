package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;

import com.princetonsa.dao.facturacion.HistoMontoAgrupacionArticuloDAO;
import com.princetonsa.dao.sqlbase.facturacion.SQLBaseHistoMontoAgrupacionArticuloDAO;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoAgrupacionArticulo;
import com.servinte.axioma.orm.HistoDetalleMonto;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 13/09/2010
 */
public class OracleHistoMontoAgrupacionArticulo implements
		HistoMontoAgrupacionArticuloDAO {

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
	public int insertarHistoMontoAgrupacionArticulo(Connection conn,DTOBusquedaMontoAgrupacionArticulo registro, 
			HistoDetalleMonto histoDetalle, int TIPO_BD){
		
		return SQLBaseHistoMontoAgrupacionArticuloDAO.insertarHistoMontoAgrupacionArticulo(conn, registro, histoDetalle, TIPO_BD);
	}

}
