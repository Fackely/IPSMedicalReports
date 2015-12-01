package com.servinte.axioma.mundo.impl.odontologia.ventaTarjetaCliente;

import com.servinte.axioma.dao.fabrica.odontologia.ventastarjeta.VentaTarjetaFabricaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.ventaTarjeta.IVentaTarjetaEmpresarialDAO;
import com.servinte.axioma.mundo.interfaz.odontologia.ventaTarjetaCliente.IVentaEmpresarialMundo;
import com.servinte.axioma.orm.VentaEmpresarial;


/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public class VentaEmpresarialMundo implements IVentaEmpresarialMundo{

	
	/**
	 * Interfaz Venta Empresaraial DAO
	 */
	private IVentaTarjetaEmpresarialDAO ventaEmpresarialDAO;
	
	

	/**
	 * Constructor
	 */
	public  VentaEmpresarialMundo(){
		ventaEmpresarialDAO=VentaTarjetaFabricaDAO.crearVentaEmpresarialDAO();
	}
	
	@Override
	public VentaEmpresarial buscarxId(Number id) {
		return ventaEmpresarialDAO.buscarxId(id);
	}
	

	@Override
	public void eliminar(VentaEmpresarial objeto) {
		ventaEmpresarialDAO.eliminar(objeto);
		
	}

	
	@Override
	public void insertar(VentaEmpresarial objeto) {
		ventaEmpresarialDAO.insertar(objeto);
		
	}

	
	
	@Override
	public void modificar(VentaEmpresarial objeto) {
		ventaEmpresarialDAO.modificar(objeto);
		
	}

}
