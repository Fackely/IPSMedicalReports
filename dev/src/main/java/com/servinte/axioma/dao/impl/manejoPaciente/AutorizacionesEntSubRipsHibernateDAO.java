package com.servinte.axioma.dao.impl.manejoPaciente;

import com.princetonsa.dao.manejoPaciente.AutorizacionesEntidadesSubcontratadasDao;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacionesEntSubRips;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEntSubRipsDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEntidadesSubDAO;
import com.servinte.axioma.orm.AutorizacionesEntSubRips;
import com.servinte.axioma.orm.delegate.manejoPaciente.AutorizacionesEntSubArticuDelegate;
import com.servinte.axioma.orm.delegate.manejoPaciente.AutorizacionesEntSubRipsDelegate;

public class AutorizacionesEntSubRipsHibernateDAO implements IAutorizacionesEntSubRipsDAO{

	AutorizacionesEntSubRipsDelegate delegate;
	
	public AutorizacionesEntSubRipsHibernateDAO(){
		delegate = new AutorizacionesEntSubRipsDelegate();
	}
	
	/**
	 * Consulta si la autorizaci�n enviada como par�metro ya ha sido procesada
	 * con �xito por la funcionalidad Rips Entidades Subcontratadas
	 * @param consecutivoAutorizacionEntidadSub
	 * @return DtoAutorizacionesEntSubRips
	 * 
	 * @author Fabi�n Becerra
	 */
	public DtoAutorizacionesEntSubRips obtenerAutorizacionEntSubRipsPorEntSub(long consecutivoAutorizacionEntidadSub){
		return delegate.obtenerAutorizacionEntSubRipsPorEntSub(consecutivoAutorizacionEntidadSub);
	}
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro autorizacion entidad subcontratada que fue procesada
	 * por la funcionalidad procesar rips entidades subcontratadas
	 * 
	 * @param autorEntSubRips Autorizacion de Entidad Subcontratada Procesada por Rips
	 * @return boolean
	 * @author, Fabi�n Becerra
	 *
	 */
	public boolean guardarAutorizacionEntSubRips(AutorizacionesEntSubRips autorEntSubRips){
		return delegate.guardarAutorizacionEntSubRips(autorEntSubRips);
	}
	
	
}
