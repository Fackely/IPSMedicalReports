package com.servinte.axioma.dao.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dao.interfaz.tesoreria.ICajasDAO;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.delegate.tesoreria.CajasDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link ICajasDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero - Luis Alejandro Echandia
 * @see CajasDelegate.
 */

public class CajasHibernateDAO implements ICajasDAO{

	private CajasDelegate cajasDaoImp;

	public CajasHibernateDAO() {
		cajasDaoImp  = new CajasDelegate();
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.ICajasDAO#obtenerCajasPorCajeroActivasXCentroAtencion(com.servinte.axioma.orm.Usuarios, int, int)
	 */
	@Override
	public List<Cajas> obtenerCajasPorCajeroActivasXCentroAtencion(
			DtoUsuarioPersona usuario, int codigoCentroAtencion, int constanteBDTipoCaja) {
		
		return cajasDaoImp.obtenerCajasPorCajeroActivasXCentroAtencion(usuario, codigoCentroAtencion, constanteBDTipoCaja);
	}

	
	@Override
	public ArrayList<Cajas> listarCajasPorCajeroActivasXInstitucionXCentroAtencion(
			UsuarioBasico usuario, int constanteBDTipoCaja) {

		return cajasDaoImp.listarCajasPorCajeroActivasXInstitucionXCentroAtencion(usuario, constanteBDTipoCaja);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.ICajasDAO#listarCajasPorInstitucionPorTipoCaja(int, int)
	 */
	@Override
	public List<Cajas> listarCajasPorInstitucionPorTipoCaja(int codigoInstitucion, int constanteBDTipoCaja) {
	
		return cajasDaoImp.listarCajasPorInstitucionPorTipoCaja(codigoInstitucion, constanteBDTipoCaja);
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
	public ArrayList<Cajas> listarCajasPorCentrosAtencionPorTipoCaja(int codigoCA, int constanteBDTipoCaja){
		return cajasDaoImp.listarCajasPorCentrosAtencionPorTipoCaja(codigoCA, constanteBDTipoCaja);
	}


	
	
	@Override
	public ArrayList<Cajas> listarCajasPorCajeroActivasXInstitucionXCentroAtencionTurno(
			UsuarioBasico usuario, int constanteBDTipoCaja,
			String integridadDominioEstadoTurnoExcluir) {
		return cajasDaoImp.listarCajasPorCajeroActivasXInstitucionXCentroAtencionTurno(usuario, constanteBDTipoCaja, integridadDominioEstadoTurnoExcluir);
	}
}
