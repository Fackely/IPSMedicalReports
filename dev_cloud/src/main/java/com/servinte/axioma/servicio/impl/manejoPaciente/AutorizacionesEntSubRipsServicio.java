package com.servinte.axioma.servicio.impl.manejoPaciente;

import com.princetonsa.dto.manejoPaciente.DtoAutorizacionesEntSubRips;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAutorizacionesEntSubArticuloMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAutorizacionesEntSubRipsMundo;
import com.servinte.axioma.orm.AutorizacionesEntSubRips;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntSubRipsServicio;

public class AutorizacionesEntSubRipsServicio implements IAutorizacionesEntSubRipsServicio{

	IAutorizacionesEntSubRipsMundo mundo;
	
	public AutorizacionesEntSubRipsServicio(){
		mundo = ManejoPacienteFabricaMundo.crearAutorizacionesEntSubRipsMundo();
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
		return mundo.obtenerAutorizacionEntSubRipsPorEntSub(consecutivoAutorizacionEntidadSub);
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
		return mundo.guardarAutorizacionEntSubRips(autorEntSubRips);
	}
	
}
