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
	 * Consulta si la autorización enviada como parámetro ya ha sido procesada
	 * con éxito por la funcionalidad Rips Entidades Subcontratadas
	 * @param consecutivoAutorizacionEntidadSub
	 * @return DtoAutorizacionesEntSubRips
	 * 
	 * @author Fabián Becerra
	 */
	public DtoAutorizacionesEntSubRips obtenerAutorizacionEntSubRipsPorEntSub(long consecutivoAutorizacionEntidadSub){
		return mundo.obtenerAutorizacionEntSubRipsPorEntSub(consecutivoAutorizacionEntidadSub);
	}
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro autorizacion entidad subcontratada que fue procesada
	 * por la funcionalidad procesar rips entidades subcontratadas
	 * 
	 * @param autorEntSubRips Autorizacion de Entidad Subcontratada Procesada por Rips
	 * @return boolean
	 * @author, Fabián Becerra
	 *
	 */
	public boolean guardarAutorizacionEntSubRips(AutorizacionesEntSubRips autorEntSubRips){
		return mundo.guardarAutorizacionEntSubRips(autorEntSubRips);
	}
	
}
