/**
 * 
 */
package com.servinte.axioma.bl.consultaExterna.impl;

import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.bl.consultaExterna.interfaz.IIdentificadoresOrdenadoresConsultaMundo;
import com.servinte.axioma.delegate.consultaExterna.IdentificadoresOrdenadoresConsultaDelegate;
import com.servinte.axioma.dto.administracion.ProfesionalSaludDto;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.dto.consultaExterna.GrupoOrdenadoresConsultaExternaDto;
import com.servinte.axioma.dto.consultaExterna.OrdenadoresConsultaExternaPlanoDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * @author jeilones
 * @created 2/11/2012
 *
 */
public class IdentificadoresOrdenadoresConsultaMundo implements
		IIdentificadoresOrdenadoresConsultaMundo {

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.consultaExterna.interfaz.IIdentificadoresOrdenadoresConsultaMundo#consultarUnidadAgenda(java.lang.Integer)
	 */
	public List<DtoUnidadesConsulta> consultarUnidadAgenda(Integer codigoCentroAtencion) throws IPSException{
		List<DtoUnidadesConsulta>unidadesConsulta=new ArrayList<DtoUnidadesConsulta>(0);
		try{
			HibernateUtil.beginTransaction();
			IdentificadoresOrdenadoresConsultaDelegate identificadoresOrdenadoresConsultaDelegate= new IdentificadoresOrdenadoresConsultaDelegate();
			unidadesConsulta=identificadoresOrdenadoresConsultaDelegate.consultarUnidadAgenda(codigoCentroAtencion);
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
		return unidadesConsulta;
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.consultaExterna.interfaz.IIdentificadoresOrdenadoresConsultaMundo#verificarTarifasServicios(com.servinte.axioma.dto.consultaExterna.OrdenadoresConsultaExternaDto, int)
	 */
	@Override
	public List<ServicioAutorizacionOrdenDto> verificarTarifasServicios(
			GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto,
			int esquemaTarifarioServicios) throws IPSException {
		List<ServicioAutorizacionOrdenDto>servicios=new ArrayList<ServicioAutorizacionOrdenDto>(0);
		try{
			HibernateUtil.beginTransaction();
			IdentificadoresOrdenadoresConsultaDelegate identificadoresOrdenadoresConsultaDelegate= new IdentificadoresOrdenadoresConsultaDelegate();
			servicios=identificadoresOrdenadoresConsultaDelegate.verificarTarifasServicios(grupoOrdenadoresConsultaExternaDto, esquemaTarifarioServicios);
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
		return servicios;
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.consultaExterna.interfaz.IIdentificadoresOrdenadoresConsultaMundo#verificarTarifasArticulos(com.servinte.axioma.dto.consultaExterna.OrdenadoresConsultaExternaDto, int)
	 */
	@Override
	public List<MedicamentoInsumoAutorizacionOrdenDto> verificarTarifasArticulos(
			GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto,
			int esquemaTarifarioArticulos) throws IPSException {
		List<MedicamentoInsumoAutorizacionOrdenDto>articulos=new ArrayList<MedicamentoInsumoAutorizacionOrdenDto>(0);
		try{
			HibernateUtil.beginTransaction();
			IdentificadoresOrdenadoresConsultaDelegate identificadoresOrdenadoresConsultaDelegate= new IdentificadoresOrdenadoresConsultaDelegate();
			articulos=identificadoresOrdenadoresConsultaDelegate.verificarTarifasArticulos(grupoOrdenadoresConsultaExternaDto, esquemaTarifarioArticulos);
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
		return articulos;
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.consultaExterna.interfaz.IIdentificadoresOrdenadoresConsultaMundo#consultarIdentificadorOrdenadoresConsulta(com.servinte.axioma.dto.consultaExterna.OrdenadoresConsultaExternaDto)
	 */
	@Override
	public List<ProfesionalSaludDto> consultarIdentificadorOrdenadoresConsulta(
			GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto, int esquemaTarifarioArticulos,int esquemaTarifarioServicios) throws IPSException{
		List<ProfesionalSaludDto>profesionales=new ArrayList<ProfesionalSaludDto>(0);
		try{
			HibernateUtil.beginTransaction();
			IdentificadoresOrdenadoresConsultaDelegate identificadoresOrdenadoresConsultaDelegate= new IdentificadoresOrdenadoresConsultaDelegate();
			profesionales=identificadoresOrdenadoresConsultaDelegate.consultarIdentificadorOrdenadoresConsulta(grupoOrdenadoresConsultaExternaDto, esquemaTarifarioArticulos,esquemaTarifarioServicios);
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
		return profesionales;
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.consultaExterna.interfaz.IIdentificadoresOrdenadoresConsultaMundo#consultarIdentificadorOrdenadoresConsultaPlano(com.servinte.axioma.dto.consultaExterna.OrdenadoresConsultaExternaPlanoDto)
	 */
	@Override
	public List<OrdenadoresConsultaExternaPlanoDto> consultarIdentificadorOrdenadoresConsultaPlano(
			GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaPlanoDto,int esquemaTarifarioArticulos, int esquemaTarifarioServicios) throws IPSException{
		List<OrdenadoresConsultaExternaPlanoDto>ordenadores=new ArrayList<OrdenadoresConsultaExternaPlanoDto>(0);
		try{
			HibernateUtil.beginTransaction();
			IdentificadoresOrdenadoresConsultaDelegate identificadoresOrdenadoresConsultaDelegate= new IdentificadoresOrdenadoresConsultaDelegate();
			ordenadores=identificadoresOrdenadoresConsultaDelegate.consultarIdentificadorOrdenadoresConsultaPlano(grupoOrdenadoresConsultaExternaPlanoDto, esquemaTarifarioArticulos,esquemaTarifarioServicios);
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
		return ordenadores;
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.consultaExterna.interfaz.IIdentificadoresOrdenadoresConsultaMundo#consultarIdentificadorOrdenadoresConsultaGrupoClase(com.servinte.axioma.dto.consultaExterna.GrupoOrdenadoresConsultaExternaDto, int, int)
	 */
	@Override
	public GrupoOrdenadoresConsultaExternaDto consultarIdentificadorOrdenadoresConsultaGrupoClase(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto,int esquemaTarifarioArticulos,int esquemaTarifarioServicios)throws IPSException{
		try{
			HibernateUtil.beginTransaction();
			IdentificadoresOrdenadoresConsultaDelegate identificadoresOrdenadoresConsultaDelegate= new IdentificadoresOrdenadoresConsultaDelegate();
			grupoOrdenadoresConsultaExternaDto=identificadoresOrdenadoresConsultaDelegate.consultarIdentificadorOrdenadoresConsultaGrupoClase(grupoOrdenadoresConsultaExternaDto, esquemaTarifarioArticulos,esquemaTarifarioServicios);
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
		return grupoOrdenadoresConsultaExternaDto;
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.consultaExterna.interfaz.IIdentificadoresOrdenadoresConsultaMundo#consultarIdentificadorOrdenadoresConsultaGrupoClasePlano(com.servinte.axioma.dto.consultaExterna.GrupoOrdenadoresConsultaExternaDto, int, int)
	 */
	@Override
	public List<OrdenadoresConsultaExternaPlanoDto> consultarIdentificadorOrdenadoresConsultaGrupoClasePlano(
			GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto,
			int esquemaTarifarioArticulos, int esquemaTarifarioServicios)
			throws IPSException {
		List<OrdenadoresConsultaExternaPlanoDto>ordenadores=new ArrayList<OrdenadoresConsultaExternaPlanoDto>(0);
		try{
			HibernateUtil.beginTransaction();
			IdentificadoresOrdenadoresConsultaDelegate identificadoresOrdenadoresConsultaDelegate= new IdentificadoresOrdenadoresConsultaDelegate();
			ordenadores=identificadoresOrdenadoresConsultaDelegate.consultarIdentificadorOrdenadoresConsultaGrupoClasePlano(grupoOrdenadoresConsultaExternaDto, esquemaTarifarioArticulos, esquemaTarifarioServicios);
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
		return ordenadores;
	}

}
