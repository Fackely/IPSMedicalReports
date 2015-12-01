package com.servinte.axioma.servicio.interfaz.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoServicios;
import com.servinte.axioma.orm.Servicios;

public interface IServiciosServicio {

	/**
	 * Obtener un servicio por su id
	 * @param id
	 * @return Servicios
	 */
	public Servicios obtenerServicioPorId(int id);
	
	/**
	 * 
	 * Este Método se encarga de consultar el tipo, especialidad y grupo de
	 * un servicio para comparar con la agrupación de servicios
	 * 
	 * @return DtoServicios
	 * @author, Fabian Becerra
	 *
	 */
	public DtoServicios obtenerTipoEspecialidadGrupoServicioPorID(int codigoServicio);
	
	/**
	 * 
	 * Este Método se encarga de consultar los servicios dependiendo
	 * del tipo tarifario oficial
	 * 
	 * @return ArrayList<DtoServicios>
	 * @author, Fabian Becerra
	 *
	 */
	public ArrayList<DtoServicios> obtenerServiciosXTipoTarifarioOficial(int codigoServicio,int codigoTarifarioOficial);
}
