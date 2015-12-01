package com.servinte.axioma.mundo.impl.odontologia.tipoTarjetaCliente;

import com.princetonsa.dto.odontologia.administracion.DtoTipoTarjetaCliente;
import com.servinte.axioma.dao.fabrica.odontologia.ventastarjeta.VentaTarjetaFabricaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.tipoTarjeta.ITipoTarjetaDAO;
import com.servinte.axioma.mundo.interfaz.odontologia.tipoTarjeta.ITipoTarjetaClienteMundo;
import com.servinte.axioma.orm.TiposTarjCliente;


/**
 * Clase tipo tarjeta cliente. esta clase contiene y administra la lógica de negocio de las tarjeta cliente
 * @author Edgar Carvajal
 *
 */
public  class TipoTarjetaClienteMundo implements ITipoTarjetaClienteMundo {

	
	/**
	 * Interfaz con la persistencia
	 */
	private ITipoTarjetaDAO tarjetaDAO;

	
	/**
	 * Constructor
	 */
	public TipoTarjetaClienteMundo(){
		this.setTarjetaDAO(VentaTarjetaFabricaDAO.crearTipoTarjetaDAO());
	}
	
	@Override
	public TiposTarjCliente buscarxId(Number id) {
		
		return this.getTarjetaDAO().buscarxId(id);
	}

	@Override
	public void eliminar(TiposTarjCliente objeto) {
		this.getTarjetaDAO().eliminar(objeto);
		
	}

	@Override
	public void insertar(TiposTarjCliente objeto) {
		this.getTarjetaDAO().insertar(objeto);
		
	}

	@Override
	public void modificar(TiposTarjCliente objeto) {
		this.getTarjetaDAO().modificar(objeto);
		
	}

	public void setTarjetaDAO(ITipoTarjetaDAO tarjetaDAO) {
		this.tarjetaDAO = tarjetaDAO;
	}

	public ITipoTarjetaDAO getTarjetaDAO() {
		return tarjetaDAO;
	}

	@Override
	public DtoTipoTarjetaCliente consultarTipoTarjetaCliente(double tipoTarjeta, String claseVenta)
	{
		return VentaTarjetaFabricaDAO.crearTipoTarjetaDAO().consultarTipoTarjetaCliente(tipoTarjeta, claseVenta);
	}
}
