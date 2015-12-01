package com.servinte.axioma.mundo.impl.facturacion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.MontoArticuloEspecificoDAO;
import com.princetonsa.dto.facturacion.DTOBusquedaMontoArticuloEspecifico;
import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IMontoArticuloEspecificoDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.IMontoArticuloEspecificoMundo;
import com.servinte.axioma.persistencia.UtilidadPersistencia;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class MontoArticuloEspecificoMundo implements
		IMontoArticuloEspecificoMundo {
	
	IMontoArticuloEspecificoDAO dao;
	MontoArticuloEspecificoDAO conexionDAO;
	
	public MontoArticuloEspecificoMundo(){
		dao=FacturacionFabricaDAO.crearMontoArticuloEspecificoDAO();
		
		conexionDAO = DaoFactory.getDaoFactory(
				System.getProperty("TIPOBD")).getMontoArticuloEspecificoDAO();
	}
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de un artículo específico de montos de cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarArticuloEspecifico(int id){
		return dao.eliminarArticuloEspecifico(id);
	}
	
	/**
	 * 
	 * Este Método se encarga de buscar un registro de
	 * de artículos específicos del detalle de un monto de cobro
	 * 
	 * @param DTOBusquedaMontoArticuloEspecifico dto
	 * @return ArrayList<DTOBusquedaMontoArticuloEspecifico> lista	
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOBusquedaMontoArticuloEspecifico> buscarMontoArticuloEspecifico(
			DTOBusquedaMontoArticuloEspecifico dto){
		Connection conn = UtilidadPersistencia.getPersistencia().obtenerConexion();
		ArrayList<DTOBusquedaMontoArticuloEspecifico> lista = conexionDAO.buscarMontoArticuloEspecifico(conn, dto);
		return lista;
	}

}
