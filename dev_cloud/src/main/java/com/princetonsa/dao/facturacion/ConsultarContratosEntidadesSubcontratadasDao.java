package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.facturacion.DtoContratoEntidadSub;

/**
 * 
 * @author Julio Hernandez jfhernandez@axioma-md.com
 *
 */

public interface ConsultarContratosEntidadesSubcontratadasDao
{
	public ArrayList obtenerEntidades(Connection con);
	
	public ArrayList obtenerClaseInventarios(Connection con);
	
	public ArrayList obtenerEsquemas(Connection con);
	
	public ArrayList obtenerGruposServicio(Connection con);
	
	public ArrayList obtenerEsquemasProcedimientos(Connection con);
	
	public HashMap consultaContratos(Connection con, HashMap encabezado, HashMap inventarios, HashMap servicios);
	
	public HashMap consultaEsquemasInventarios(Connection con);
	
	public HashMap consultaEsquemasServicios(Connection con);
	
	/**
	 * 
	 * Este Método se encarga de consultar el tipo de de tarifa 
	 * manejado en el contrato de una entidad subcontratada, a través
	 * de la entidad subcontratada y el número de contrato
	 * 
	 * @param Connection con, DtoContratoEntidadSub dto
	 * @return DtoContratoEntidadSub
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DtoContratoEntidadSub consultarTipoTarifaEntidadSubcontratada(Connection con, DtoContratoEntidadSub dto);
	
}