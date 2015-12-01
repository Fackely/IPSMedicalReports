package com.servinte.axioma.mundo.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ICajasDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICajasMundo;
import com.servinte.axioma.orm.Cajas;

/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con las Cajas
 * 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see ICajasMundo
 */

public class CajasMundo implements ICajasMundo {


	private ICajasDAO cajasDAO;
	
	public CajasMundo() {
		inicializar();
	}

	private void inicializar() {
		cajasDAO = TesoreriaFabricaDAO.crearCajasDAO();
	}

	
	@Override
	public List<Cajas> obtenerCajasPorCajeroActivasXCentroAtencion(DtoUsuarioPersona usuario, int codigoCentroAtencion, int constanteBDTipoCaja) {
		
		List<Cajas> listadoCajasActivas = cajasDAO.obtenerCajasPorCajeroActivasXCentroAtencion(usuario, codigoCentroAtencion, constanteBDTipoCaja);
	
		return listadoCajasActivas;
	}

	
	
	@Override
	public ArrayList<Cajas> listarCajasPorCajeroActivasXInstitucionXCentroAtencion(
			UsuarioBasico usuario, int constanteBDTipoCaja) {
		ArrayList<Cajas> listacajas = cajasDAO.listarCajasPorCajeroActivasXInstitucionXCentroAtencion(usuario, constanteBDTipoCaja);
		return listacajas;
	}

	@Override
	public List<Cajas> listarCajasPorInstitucionPorTipoCaja(int codigoInstitucion, int constanteBDTipoCaja) {
		
		List<Cajas> listadoCajas = cajasDAO.listarCajasPorInstitucionPorTipoCaja(codigoInstitucion, constanteBDTipoCaja);
		
		return listadoCajas;
	}
	
	/**
	 * M&eacute;todo que lista las Cajas por el centro de atención y 
	 * por el tipo de caja (Caja Mayor - Caja Principal - Caja Recaudo)
	 *
	 * @param codigoCentroAtencion
	 * @param constanteBDTipoCaja
	 * @return lista de Cajas por el centro de institucion y por tipo de 
	 * caja (Caja Mayor - Caja Principal - Caja Recaudo)
	 */
	@Override
	public ArrayList<Cajas> listarCajasPorCentrosAtencionPorTipoCaja(int codigoCA, int constanteBDTipoCaja){
		return cajasDAO.listarCajasPorCentrosAtencionPorTipoCaja(codigoCA, constanteBDTipoCaja);
	}

	
	
	
	@Override
	public ArrayList<Cajas> listarCajasPorCajeroActivasXInstitucionXCentroAtencionTurno(
			UsuarioBasico usuario, int constanteBDTipoCaja,
			String integridadDominioEstadoTurnoExcluir) {
		return cajasDAO.listarCajasPorCajeroActivasXInstitucionXCentroAtencionTurno(usuario, constanteBDTipoCaja, integridadDominioEstadoTurnoExcluir);
	}
	
	
	
	
}
