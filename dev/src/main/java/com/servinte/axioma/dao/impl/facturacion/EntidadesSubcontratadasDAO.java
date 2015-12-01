package com.servinte.axioma.dao.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.servinte.axioma.dao.interfaz.facturacion.IEntidadesSubcontratadasDAO;
import com.servinte.axioma.orm.EntidadesSubcontratadas;
import com.servinte.axioma.orm.UsuariosEntidadSub;
import com.servinte.axioma.orm.delegate.facturacion.EntidadesSubcontratadasDelegate;
import com.servinte.axioma.orm.delegate.facturacion.convenio.UsuariosEntidadSubDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link IEntidadesSubcontratadasDAO}.
 * @author Cristhian Murillo
 */
public class EntidadesSubcontratadasDAO implements IEntidadesSubcontratadasDAO 
{
	
	private EntidadesSubcontratadasDelegate entidadesSubcontratadasDelegate;
	private UsuariosEntidadSubDelegate usuariosEntidadSubDelegate;
	
	
	
	public EntidadesSubcontratadasDAO() {
		entidadesSubcontratadasDelegate = new EntidadesSubcontratadasDelegate();
		usuariosEntidadSubDelegate		= new UsuariosEntidadSubDelegate();
	}
	
	
	@Override
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubXCentroCostoActivo(int codCentroCosto) {
		return entidadesSubcontratadasDelegate.listarEntidadesSubXCentroCostoActivo(codCentroCosto);
	}


	@Override
	public ArrayList<UsuariosEntidadSub> buscarUsuariosEntidadSubPorUsuarioEntidad(
			String login, long entidadSub) {
		return usuariosEntidadSubDelegate.buscarUsuariosEntidadSubPorUsuarioEntidad(login, entidadSub);
	}


	@Override
	public EntidadesSubcontratadas obtenerEntidadesSubcontratadasporId(long id) {
		return entidadesSubcontratadasDelegate.obtenerEntidadesSubcontratadasporId(id);
	}


	@Override
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubXCentroCostoActivoContratoVigente(int codCentroCosto){
		return entidadesSubcontratadasDelegate.listarEntidadesSubXCentroCostoActivoContratoVigente(codCentroCosto);
	}


	@Override
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubXViaIngreso(DtoEntidadSubcontratada parametros) {
		return entidadesSubcontratadasDelegate.listarEntidadesSubXViaIngreso(parametros);
	}
	
	/**
	 * Retorna las EntidadesSubcontratadas activas en el sistema
	 * @return ArrayList<DtoEntidadSubcontratada>
	 * 
	 * @author Fabián Becerra
	 */
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubActivasEnSistema(){
		return entidadesSubcontratadasDelegate.listarEntidadesSubActivasEnSistema();
	}


	@Override
	public DtoEntidadSubcontratada listarEntidadesSubXId(Long codigoPk){
		return entidadesSubcontratadasDelegate.listarEntidadesSubXId(codigoPk);
	}


}
