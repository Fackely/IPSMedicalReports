package com.servinte.axioma.servicio.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoServicios;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IServiciosMundo;
import com.servinte.axioma.orm.Servicios;
import com.servinte.axioma.servicio.interfaz.facturacion.IServiciosServicio;

public class ServiciosServicio implements IServiciosServicio{
	
	
	IServiciosMundo mundo;
	
	public ServiciosServicio(){
		mundo=FacturacionFabricaMundo.crearServiciosMundo();
	}
	
	/**
	 * Obtener un servicio por su id
	 * @param id
	 * @return Servicios
	 */
	public Servicios obtenerServicioPorId(int id){
		return mundo.obtenerServicioPorId(id);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar el tipo, especialidad y grupo de
	 * un servicio para comparar con la agrupación de servicios
	 * 
	 * @return DtoServicios
	 * @author, Fabian Becerra
	 *
	 */
	public DtoServicios obtenerTipoEspecialidadGrupoServicioPorID(int codigoServicio){
		return mundo.obtenerTipoEspecialidadGrupoServicioPorID(codigoServicio);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los servicios dependiendo
	 * del tipo tarifario oficial
	 * 
	 * @return ArrayList<DtoServicios>
	 * @author, Fabian Becerra
	 *
	 */
	public ArrayList<DtoServicios> obtenerServiciosXTipoTarifarioOficial(int codigoServicio,int codigoTarifarioOficial){
		return mundo.obtenerServiciosXTipoTarifarioOficial(codigoServicio,codigoTarifarioOficial);
	}

}
