package com.servinte.axioma.mundo.impl.manejoPaciente;

import com.princetonsa.dto.manejoPaciente.DtoAutorizacionesEntSubRips;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IAutorizacionesEntSubRipsDAO;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAutorizacionesEntSubRipsMundo;
import com.servinte.axioma.orm.AutorizacionesEntSubRips;

public class AutorizacionesEntSubRipsMundo implements IAutorizacionesEntSubRipsMundo{

	IAutorizacionesEntSubRipsDAO dao;
	
	public AutorizacionesEntSubRipsMundo(){
		dao = ManejoPacienteDAOFabrica.crearAutorizacionesEntSubRipsDAO();
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
		return dao.obtenerAutorizacionEntSubRipsPorEntSub(consecutivoAutorizacionEntidadSub);
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
		return dao.guardarAutorizacionEntSubRips(autorEntSubRips);
	}
	
	
}
