package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.Cajas;

/**
 * Define la l&oacute;gica de negocio relacionada con las Cajas
 * 
 * @author Jorge Armando Agudelo Quintero - Luis Alejandro Echandia
 * @see com.servinte.axioma.mundo.impl.tesoreria.CajasMundo
 */

public interface ICajasMundo {

	/**
	 * lista de Cajas activas por centro de atenci&oacute;n para
	 * el cajero enviado y que esten activas en el sistema y para ese cajero
	 * 
	 * @autor Jorge Armando Agudelo - Luis Alejandro Echandia
	 * @param usuario
	 * @param codigoCentroAtencion
	 * @param constanteBDTipoCaja
	 * @return List<{@link Cajas}>
	 */

	public List<Cajas> obtenerCajasPorCajeroActivasXCentroAtencion(
			DtoUsuarioPersona usuario, int codigoCentroAtencion, int constanteBDTipoCaja);
	
	
	/**
	 * Retorna un listado de Cajas relacionados en la entidad cajas por institucion para el cajero enviado
	 * 
	 * @param usuario
	 * @param constanteBDTipoCaja
	 * @return
	 */
	public ArrayList<Cajas> listarCajasPorCajeroActivasXInstitucionXCentroAtencion (UsuarioBasico usuario, int constanteBDTipoCaja);
	
	
	/**
	 * Retorna un listado de Cajas por institucion y por el tipo de caja (Caja Mayor - Caja Principal - Caja Recaudo)
	 * @param codigoInstitucion
	 * @param constanteBDTipoCaja
	 * @return listado de Cajas por institucion y por el tipo de caja (Caja Mayor - Caja Principal - Caja Recaudo)
	 */
	public List<Cajas> listarCajasPorInstitucionPorTipoCaja(int codigoInstitucion, int constanteBDTipoCaja);
	
	/**
	 * M&eacute;todo que lista las Cajas por el centro de atención y 
	 * por el tipo de caja (Caja Mayor - Caja Principal - Caja Recaudo)
	 *
	 * @param codigoCentroAtencion
	 * @param constanteBDTipoCaja
	 * @return lista de Cajas por el centro de institucion y por tipo de 
	 * caja (Caja Mayor - Caja Principal - Caja Recaudo)
	 */
	public ArrayList<Cajas> listarCajasPorCentrosAtencionPorTipoCaja(int codigoCA, int constanteBDTipoCaja);
	
	
	
	/**
	 * lista de Cajas activas por institucion y centro de atenci&oacute;n para el cajero enviado y 
	 * que esten activas en el sistema y para ese cajero.
	 * El campo integridadDominioEstadoTurnoExcluir se utiliza para EXCLUIR las cajas que tengan 
	 * ese estado.
	 * @param usuario
	 * @param constanteBDTipoCaja
	 * @param integridadDominioEstadoTurnoExcluir
	 * @return ArrayList<Cajas>
	 */
	public ArrayList<Cajas> listarCajasPorCajeroActivasXInstitucionXCentroAtencionTurno(
			UsuarioBasico usuario, int constanteBDTipoCaja, String integridadDominioEstadoTurnoExcluir);
}
