package com.servinte.axioma.dao.impl.inventario;

import java.util.ArrayList;

import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.servinte.axioma.dao.interfaz.inventario.IAurorizacionesEntSubCapitacionDAO;
import com.servinte.axioma.orm.AutorizacionesEntSubArticu;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.delegate.manejoPaciente.AutorizacionesEntSubArticuDelegate;
import com.servinte.axioma.orm.delegate.manejoPaciente.AutorizacionesEntSubServiDelegate;
import com.servinte.axioma.orm.delegate.manejoPaciente.AutorizacionesEntidadesSubDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link IAurorizacionesEntSubCapitacionDAO}.
 * @author Cristhian Murillo
 */
public class AurorizacionesEntSubCapitacionHibernateDAO implements IAurorizacionesEntSubCapitacionDAO 
{
	
	private AutorizacionesEntSubArticuDelegate autorizacionesEntSubArticuDelegate;
	private AutorizacionesEntSubServiDelegate autorizacionesEntSubServiDelegate;
	private AutorizacionesEntidadesSubDelegate autorizacionesEntidadesSubDelegate;

	
	/**
	 * Constructor
	 */
	public AurorizacionesEntSubCapitacionHibernateDAO(){
		autorizacionesEntSubArticuDelegate = new AutorizacionesEntSubArticuDelegate();
		autorizacionesEntSubServiDelegate = new AutorizacionesEntSubServiDelegate();
		autorizacionesEntidadesSubDelegate = new AutorizacionesEntidadesSubDelegate();
	}
	
	
	@Override
	public ArrayList<AutorizacionesEntSubServi> listarAutorizacionesEntSubServiPorAutoEntSub(
			DtoAutorizacionEntSubcontratadasCapitacion dtoParametros) {
		return autorizacionesEntSubServiDelegate.listarAutorizacionesEntSubServiPorAutoEntSub(dtoParametros);
	}


	@Override
	public ArrayList<DtoArticulosAutorizaciones> listarautorizacionesEntSubArticuPorAutoEntSub(
			DtoAutorizacionEntSubcontratadasCapitacion dtoParametros) {
		return autorizacionesEntSubArticuDelegate.listarautorizacionesEntSubArticuPorAutoEntSub(dtoParametros);
	}


	@Override
	public AutorizacionesEntSubArticu obtenerAutorizacionesEntSubArticuPorId(long id) {
		return autorizacionesEntSubArticuDelegate.obtenerAutorizacionesEntSubArticuPorId(id);
	}


	@Override
	public AutorizacionesEntidadesSub obtenerAutorizacionesEntidadesSubPorId(long id) {
		return autorizacionesEntidadesSubDelegate.obtenerAutorizacionesEntidadesSubPorId(id);
	}


	@Override
	public void attachDirtyAutorizacionesEntSubArticu(AutorizacionesEntSubArticu instance) {
		autorizacionesEntSubArticuDelegate.attachDirtyAutorizacionesEntSubArticu(instance);	
	}


	@Override
	public ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> obtenerAutorizacionesPorEntSubPorNumeroSolicitud(
			DtoAutorizacionEntSubcontratadasCapitacion dtoEntregaMedicamentosInsumosEntSubcontratadas) {
		return autorizacionesEntidadesSubDelegate.obtenerAutorizacionesPorEntSubPorNumeroSolicitud(dtoEntregaMedicamentosInsumosEntSubcontratadas);
	}
	

}
