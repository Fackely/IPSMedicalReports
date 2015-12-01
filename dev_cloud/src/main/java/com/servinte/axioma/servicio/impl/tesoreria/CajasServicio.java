package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICajasMundo;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICajasServicio;


/**
 * Implementaci&oacute;n de la interfaz {@link ICajasServicio}
 * 
 * @author Jorge Armando Agudelo Quintero - Luis Alejandro Echandia
 *
 */

public class CajasServicio implements ICajasServicio {
	
	
private ICajasMundo cajasMundo;
	
	public CajasServicio() {
		cajasMundo =  TesoreriaFabricaMundo.crearCajasMundo();
	}

	@Override
	public List<Cajas> obtenerCajasPorCajeroActivasXCentroAtencion(DtoUsuarioPersona usuario, int codigoCentroAtencion, int constanteBDTipoCaja)
	{
		return cajasMundo.obtenerCajasPorCajeroActivasXCentroAtencion(usuario, codigoCentroAtencion, constanteBDTipoCaja);
	}

	
	
	@Override
	public ArrayList<Cajas> listarCajasPorCajeroActivasXInstitucionXCentroAtencion(
			UsuarioBasico usuario, int constanteBDTipoCaja) {
		return cajasMundo.listarCajasPorCajeroActivasXInstitucionXCentroAtencion(usuario, constanteBDTipoCaja);
	}

	@Override
	public List<Cajas> listarCajasPorInstitucionPorTipoCaja(int codigoInstitucion, int constanteBDTipoCaja) {
		
		return cajasMundo.listarCajasPorInstitucionPorTipoCaja(codigoInstitucion, constanteBDTipoCaja);
	}
	
	/**
	 * M&eacute;todo que lista las Cajas por el centro de atenci&oacute;n y 
	 * por el tipo de caja (Caja Mayor - Caja Principal - Caja Recaudo)
	 *
	 * @param codigoCentroAtencion
	 * @param constanteBDTipoCaja
	 * @return lista de Cajas por el centro de institucion y por tipo de 
	 * caja (Caja Mayor - Caja Principal - Caja Recaudo)
	 */
	@Override
	public ArrayList<Cajas> listarCajasPorCentrosAtencionPorTipoCaja(int codigoCA, int constanteBDTipoCaja){
		return cajasMundo.listarCajasPorCentrosAtencionPorTipoCaja(codigoCA, constanteBDTipoCaja);
	}

	
	
	
	@Override
	public ArrayList<Cajas> listarCajasPorCajeroActivasXInstitucionXCentroAtencionTurno(
			UsuarioBasico usuario, int constanteBDTipoCaja,
			String integridadDominioEstadoTurnoExcluir) {
		return cajasMundo.listarCajasPorCajeroActivasXInstitucionXCentroAtencionTurno(usuario, constanteBDTipoCaja, integridadDominioEstadoTurnoExcluir);
	}
	
}
