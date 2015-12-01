/**
 * 
 */
package com.servinte.axioma.bl.capitacion.impl;

import java.util.List;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;

import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.servinte.axioma.bl.capitacion.interfaz.ICargueUsuariosMundo;
import com.servinte.axioma.delegate.capitacion.CargueUsuariosDelegate;
import com.servinte.axioma.dto.capitacion.CargueDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * @author jeilones
 * @created 4/10/2012
 *
 */
public class CargueUsuariosMundo implements ICargueUsuariosMundo{

	/**
	 * 
	 * @author jeilones
	 * @created 4/10/2012
	 */
	public CargueUsuariosMundo() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.capitacion.interfaz.ICargueUsuariosMundo#consultarCargueUsuarios(com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados)
	 */
	@Override
	public List<DtoUsuariosCapitados> consultarCargueUsuarios(
			boolean requiereTransaccion,DtoUsuariosCapitados filtrosUsuario) throws IPSException {
		try{
			if (requiereTransaccion) {
				HibernateUtil.beginTransaction();
			}
			
			CargueUsuariosDelegate cargueUsuariosDelegate=new CargueUsuariosDelegate();
			
			return cargueUsuariosDelegate.consultarCargueUsuarios(filtrosUsuario);
			
		}catch (IPSException ipsme) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			throw ipsme;
		} catch (Exception e) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}finally{
			if (requiereTransaccion) {
				HibernateUtil.endTransaction();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.capitacion.interfaz.ICargueUsuariosMundo#consultarGrupoFamiliar(com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados)
	 */
	@Override
	public List<DtoUsuariosCapitados> consultarGrupoFamiliar(
			boolean requiereTransaccion,DtoUsuariosCapitados usuarioCapitado) throws IPSException {
		try{
			if (requiereTransaccion) {
				HibernateUtil.beginTransaction();
			}
			
			CargueUsuariosDelegate cargueUsuariosDelegate=new CargueUsuariosDelegate();
			
			return cargueUsuariosDelegate.consultarGrupoFamiliar(usuarioCapitado);
			
		}catch (IPSException ipsme) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			throw ipsme;
		} catch (Exception e) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}finally{
			if (requiereTransaccion) {
				HibernateUtil.endTransaction();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.capitacion.interfaz.ICargueUsuariosMundo#consultarHistoricoCargue(com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados)
	 */
	@Override
	public List<CargueDto> consultarHistoricoCargue(
			boolean requiereTransaccion,DtoUsuariosCapitados usuarioCapitado) throws IPSException {
		try{
			if (requiereTransaccion) {
				HibernateUtil.beginTransaction();
			}
			
			CargueUsuariosDelegate cargueUsuariosDelegate=new CargueUsuariosDelegate();
			
			return cargueUsuariosDelegate.consultarHistoricoCargue(usuarioCapitado);
			
		}catch (IPSException ipsme) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			throw ipsme;
		} catch (Exception e) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}finally{
			if (requiereTransaccion) {
				HibernateUtil.endTransaction();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.capitacion.interfaz.ICargueUsuariosMundo#consultarDetalleUsuario(boolean, com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados)
	 */
	@Override
	public DtoUsuariosCapitados consultarDetalleUsuario(
			boolean requiereTransaccion, DtoUsuariosCapitados usuarioCapitado)
			throws IPSException {
		try{
			if (requiereTransaccion) {
				HibernateUtil.beginTransaction();
			}
			
			CargueUsuariosDelegate cargueUsuariosDelegate=new CargueUsuariosDelegate();
			DtoUsuariosCapitados dtoUsuariosCapitados=cargueUsuariosDelegate.consultarDetalleUsuario(usuarioCapitado);
			
			if(dtoUsuariosCapitados.getTipoAfiliado()!=null&&(dtoUsuariosCapitados.getTipoAfiliado()+"").equals(ConstantesBD.codigoTipoAfiliadoCotizante)){
				dtoUsuariosCapitados.setGrupoFamiliar(cargueUsuariosDelegate.consultarGrupoFamiliar(dtoUsuariosCapitados));
			}
			List<CargueDto>historicoCargues=cargueUsuariosDelegate.consultarHistoricoCargue(dtoUsuariosCapitados);
			if(!historicoCargues.isEmpty()){
				dtoUsuariosCapitados.setCargueActualDto(historicoCargues.get(0));
				historicoCargues.remove(0);
			}
			dtoUsuariosCapitados.setHistoricoCargues(historicoCargues);
			
			return dtoUsuariosCapitados; 
			
		}catch (IPSException ipsme) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			throw ipsme;
		} catch (Exception e) {
			if (requiereTransaccion) {
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}finally{
			if (requiereTransaccion) {
				HibernateUtil.endTransaction();
			}
		}
	}

}
