package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.Cajas;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de Cajas 
 * 
 * @author Jorge Armando Agudelo Quintero - Luis Alejandro Echandia
 *
 */

public interface ICajasDAO {
	
	/**
	 * lista de Cajas activas por institucion y centro de atenci&oacute;n para
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
	 * lista de Cajas activas por institucion y centro de atenci&oacute;n para
	 * el cajero enviado y que esten activas en el sistema
	 * 
	 * @param usuario
	 * @param constanteBDTipoCaja
	 * @return
	 */
	public ArrayList<Cajas> listarCajasPorCajeroActivasXInstitucionXCentroAtencion 
		(UsuarioBasico usuario, int constanteBDTipoCaja);
	
	/**
	 * Retorna un listado de Cajas por institucion y por el tipo de caja (Caja Mayor - Caja Principal - Caja Recaudo)
	 * @param codigoInstitucion
	 * @param constanteBDTipoCaja
	 * @return listado de Cajas por institucion y por el tipo de caja (Caja Mayor - Caja Principal - Caja Recaudo)
	 */
	public List<Cajas> listarCajasPorInstitucionPorTipoCaja(int codigoInstitucion, int constanteBDTipoCaja);
	
	
	/**
	 * M&eacute;todo que lista las Cajas por el centro de atenci&oacute;n y 
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
