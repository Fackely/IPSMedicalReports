/**
 * 
 */
package com.servinte.axioma.bl.facturacion.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.facturacion.InfoCobertura;

import com.princetonsa.mundo.facturacion.Cobertura;
import com.servinte.axioma.bl.facturacion.interfaz.ICoberturaMundo;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * Clase que implementa los servicios de Negocio correspondientes a la 
 * lógica asociada a las Coberturas
 * 
 * @author diego
 *
 */
public class CoberturaMundo implements ICoberturaMundo {

	/**
	 * 
	 */
	public CoberturaMundo() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.facturacion.interfaz.ICoberturaMundo#validacionCoberturaServicioEntidadSub(int, int, java.lang.String, int, int, int)
	 */
	@Override
	public InfoCobertura validacionCoberturaServicioEntidadSub(
			long codigoContrato, int codigoViaIngreso, String tipoPaciente,
			int codigoServicio, Integer codigoNaturalezaPaciente,
			int codigoInstitucion) throws IPSException {
		
		InfoCobertura infoCoberturaDto = null;
		Connection con = null;
		int codigoNaturaleza = ConstantesBD.codigoNuncaValido;
		
		try {
			
			con = UtilidadBD.abrirConexion();
			
			if (codigoNaturalezaPaciente != null) {
				codigoNaturaleza = codigoNaturalezaPaciente;
			}
			
			infoCoberturaDto = Cobertura.validacionCoberturaServicioEntidadSub(
					con, codigoContrato, codigoViaIngreso, tipoPaciente, 
					codigoServicio, codigoNaturaleza, codigoInstitucion);
			
			UtilidadBD.cerrarConexion(con);
			
		} catch (SQLException e) {
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		} catch (Exception e) {
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return infoCoberturaDto;
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.facturacion.interfaz.ICoberturaMundo#validacionCoberturaArticuloEntidadSub(int, int, java.lang.String, int, int, int)
	 */
	@Override
	public InfoCobertura validacionCoberturaArticuloEntidadSub(
			long codigoContrato, int codigoViaIngreso, String tipoPaciente,
			int codigoArticulo, Integer codigoNaturalezaPaciente,
			int codigoInstitucion) throws IPSException {
		
		InfoCobertura infoCoberturaDto = null;
		Connection con = null;
		int codigoNaturaleza = ConstantesBD.codigoNuncaValido;
		
		try {
			
			con = UtilidadBD.abrirConexion();
			
			if (codigoNaturalezaPaciente != null) {
				codigoNaturaleza = codigoNaturalezaPaciente;
			}
			
			infoCoberturaDto = Cobertura.validacionCoberturaArticuloEntidadSub(
					con, codigoContrato, codigoViaIngreso, tipoPaciente, 
					codigoArticulo, codigoNaturaleza, codigoInstitucion);
			
			UtilidadBD.cerrarConexion(con);
			
		} catch (SQLException e) {
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		} catch (Exception e) {
			UtilidadBD.cerrarObjetosPersistencia(null, null, con);
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return infoCoberturaDto;
	}

}
