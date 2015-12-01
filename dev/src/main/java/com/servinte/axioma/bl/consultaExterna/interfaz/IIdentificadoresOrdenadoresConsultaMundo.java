/**
 * 
 */
package com.servinte.axioma.bl.consultaExterna.interfaz;

import java.util.List;

import com.servinte.axioma.dto.administracion.ProfesionalSaludDto;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.dto.consultaExterna.GrupoOrdenadoresConsultaExternaDto;
import com.servinte.axioma.dto.consultaExterna.OrdenadoresConsultaExternaPlanoDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author jeilones
 * @created 2/11/2012
 *
 */
public interface IIdentificadoresOrdenadoresConsultaMundo {

	/**
	 * Consulta las unidades de consulta que estan asociadas a un centro de atencion especifico 
	 * 
	 * @param codigoCentroAtencion
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 20/11/2012
	 */
	List<DtoUnidadesConsulta> consultarUnidadAgenda(Integer codigoCentroAtencion) throws IPSException;
	
	/**
	 * Consulta los diferentes servicios agrupados por grupo servicio ordenados y verifica si tienen tarifa
	 * 
	 * @param esquemaTarifarioServicios
	 *  
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 *  
	 * @created 8/11/2012
	 */
	List<ServicioAutorizacionOrdenDto> verificarTarifasServicios(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto, int esquemaTarifarioServicios)throws IPSException;
	/**
	 * Consulta los diferentes articulos agrupados por clase de inventario ordenados y verifica si tienen tarifa
	 * 
	 * @param grupoOrdenadoresConsultaExternaDto
	 * @param esquemaTarifarioArticulos
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 9/11/2012
	 */
	List<MedicamentoInsumoAutorizacionOrdenDto> verificarTarifasArticulos(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto, int esquemaTarifarioArticulos)throws IPSException;
	/**
	 * Consulta para el reporte de ordenadores de consulta externa, sirve para PDF y EXCEL
	 * 
	 * @param grupoOrdenadoresConsultaExternaDto
	 * @param esquemaTarifarioArticulos
	 * @param esquemaTarifarioServicios
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 2/11/2012
	 */
	List<ProfesionalSaludDto> consultarIdentificadorOrdenadoresConsulta(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto,int esquemaTarifarioArticulos,int esquemaTarifarioServicios) throws IPSException;
	
	/**
 	 * Consulta para el reporte de ordenadores de consulta externa, sirve para ARCHIVO PLANO
 	 * 
	 * @param grupoOrdenadoresConsultaExternaPlanoDto
	 * @param esquemaTarifarioArticulos
	 * @param esquemaTarifarioServicios
	 * 
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 2/11/2012
	 */
	List<OrdenadoresConsultaExternaPlanoDto> consultarIdentificadorOrdenadoresConsultaPlano(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaPlanoDto,int esquemaTarifarioArticulos, int esquemaTarifarioServicios) throws IPSException;
	
	/**
	 * Consulta para el reporte de ordenadores de consulta externa, para grupos de servicio y clases de inventario sirve para PDF y EXCEL
	 * 
	 * @param grupoOrdenadoresConsultaExternaDto
	 * @param esquemaTarifarioArticulos
	 * @param esquemaTarifarioServicios
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 19/11/2012
	 */
	GrupoOrdenadoresConsultaExternaDto consultarIdentificadorOrdenadoresConsultaGrupoClase(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto,int esquemaTarifarioArticulos,int esquemaTarifarioServicios)throws IPSException;
	
	/**
	 * Consulta para el reporte de ordenadores de consulta externa para archivo plano
	 * 
	 * @param grupoOrdenadoresConsultaExternaDto
	 * @param esquemaTarifarioArticulos
	 * @param esquemaTarifarioServicios
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 20/11/2012
	 */
	List<OrdenadoresConsultaExternaPlanoDto> consultarIdentificadorOrdenadoresConsultaGrupoClasePlano(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto,int esquemaTarifarioArticulos,int esquemaTarifarioServicios)throws IPSException;
}
