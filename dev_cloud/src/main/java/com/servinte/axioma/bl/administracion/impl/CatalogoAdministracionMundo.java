package com.servinte.axioma.bl.administracion.impl;

import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.bl.administracion.interfaz.ICatalogoAdministracionMundo;
import com.servinte.axioma.delegate.administracion.CatalogoAdministracionDelegate;
import com.servinte.axioma.dto.administracion.DtoSexo;
import com.servinte.axioma.dto.administracion.EspecialidadDto;
import com.servinte.axioma.dto.administracion.FuncionalidadDto;
import com.servinte.axioma.dto.administracion.TipoAfiliadoDto;
import com.servinte.axioma.dto.administracion.TipoIdentificacionDto;
import com.servinte.axioma.dto.ordenes.ProfesionalEspecialidadesDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Clase que implementa los servicios de Negocio correspondientes a los
 * catalogos o parámetricas del modulo de Administración
 * 
 * @author ricruico
 * @version 1.0
 * @created 22-ago-2012 02:23:59 p.m.
 */
public class CatalogoAdministracionMundo implements ICatalogoAdministracionMundo{

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.administracion.interfaz.ICatalogoAdministracionMundo#consultarEspecialidadesValidas()
	 */
	@Override
	public List<EspecialidadDto> consultarEspecialidadesValidas()
			throws IPSException {
		List<EspecialidadDto> listaEspecialidades=null;
		try{
			HibernateUtil.beginTransaction();
			CatalogoAdministracionDelegate delegate = new CatalogoAdministracionDelegate();
			listaEspecialidades=delegate.consultarEspecialidadesValidas();
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return listaEspecialidades;
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.administracion.interfaz.ICatalogoAdministracionMundo#consultarTiposIdentificacion()
	 */
	@Override
	public List<TipoIdentificacionDto> consultarTiposIdentificacion()
			throws IPSException {
		List<TipoIdentificacionDto> listaTiposIdentificacion=null;
		try{
			HibernateUtil.beginTransaction();
			CatalogoAdministracionDelegate delegate = new CatalogoAdministracionDelegate();
			listaTiposIdentificacion=delegate.consultarTiposIdentificacion();
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return listaTiposIdentificacion;
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.administracion.interfaz.ICatalogoAdministracionMundo#consultarTiposAfiliado()
	 */
	@Override
	public List<TipoAfiliadoDto> consultarTiposAfiliado()
			throws IPSException {
		List<TipoAfiliadoDto> listaTiposAfiliado=null;
		try{
			HibernateUtil.beginTransaction();
			CatalogoAdministracionDelegate delegate = new CatalogoAdministracionDelegate();
			listaTiposAfiliado=delegate.consultarTiposAfiliado();
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return listaTiposAfiliado;
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.administracion.interfaz.ICatalogoAdministracionMundo#consultarFuncionalidades(int)
	 */
	@Override
	public List<FuncionalidadDto> consultarFuncionalidades(
			int codigoFuncionalidadPadre) throws IPSException {
		List<FuncionalidadDto> listaFuncionalidades=null;
		try{
			HibernateUtil.beginTransaction();
			CatalogoAdministracionDelegate delegate = new CatalogoAdministracionDelegate();
			listaFuncionalidades=delegate.consultarFuncionalidades(codigoFuncionalidadPadre);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return listaFuncionalidades;
	}
	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.administracion.interfaz.ICatalogoAdministracionMundo#existeParametrizacionEntidadSubcontratadaPorCentroCosto(int)
	 */
	@Override
	public boolean existeParametrizacionEntidadSubcontratadaPorCentroCosto(
			int codigoEntidad) throws IPSException {
		boolean existeParametrizacion=false;
		try{
			HibernateUtil.beginTransaction();
			CatalogoAdministracionDelegate delegate = new CatalogoAdministracionDelegate();
			existeParametrizacion=delegate.existeParametrizacionEntidadSubcontratadaPorCentroCosto(codigoEntidad);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return existeParametrizacion;
	}
	
	@Override
	public List<DtoSexo> consultarSexos() throws IPSException {
		List<DtoSexo> sexos = null; 
		try 
		{
			HibernateUtil.beginTransaction();
			CatalogoAdministracionDelegate delegate = new CatalogoAdministracionDelegate();
			sexos = delegate.consultarSexos();
			HibernateUtil.endTransaction();
		}
		catch (Exception e) 
		{
			HibernateUtil.abortTransaction();
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return sexos;
	}

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.administracion.interfaz.ICatalogoAdministracionMundo#consultarProfesionalesEspecialidades()
	 */
	@Override
	public List<ProfesionalEspecialidadesDto> consultarProfesionalesEspecialidades(int codigoInstitucion) throws IPSException {
		List<ProfesionalEspecialidadesDto> listProfesionalEspecialidades = null;
		ProfesionalEspecialidadesDto profesionalEspecialidades = null;
		List<Object[]> listProfesionales = null;
		ArrayList<EspecialidadDto> listEspecialidadesProfesional = null;
		
		try {
			HibernateUtil.beginTransaction();
			CatalogoAdministracionDelegate delegate = new CatalogoAdministracionDelegate();

			listProfesionales = delegate.consultarDatosProfesionales(codigoInstitucion);
			
			if(listProfesionales != null) {
				listProfesionalEspecialidades = new ArrayList<ProfesionalEspecialidadesDto>();	

				for(Object[]  profesional: listProfesionales) {
					profesionalEspecialidades = new ProfesionalEspecialidadesDto();
					profesionalEspecialidades.setCodigoProfesional((Integer)profesional[0]);
					profesionalEspecialidades.setNombreProfesional((String)profesional[1]);
					
					listEspecialidadesProfesional = (ArrayList<EspecialidadDto>)delegate.consultarEspecialidadesXProfesional(profesionalEspecialidades.getCodigoProfesional());
					if(listEspecialidadesProfesional != null) {
						profesionalEspecialidades.setEspecialidades(listEspecialidadesProfesional);
					}
					
					listProfesionalEspecialidades.add(profesionalEspecialidades);
				}
			}
			
			HibernateUtil.endTransaction();
			
		} 
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		
		return listProfesionalEspecialidades;
	}
	
	

}
