package com.servinte.axioma.bl.consultaExterna.facade;

import java.util.List;

import com.servinte.axioma.bl.consultaExterna.impl.CatalogoConsultaExternaMundo;
import com.servinte.axioma.bl.consultaExterna.impl.CitasMundo;
import com.servinte.axioma.bl.consultaExterna.impl.IdentificadoresOrdenadoresConsultaMundo;
import com.servinte.axioma.bl.consultaExterna.interfaz.ICatalogoConsultaExternaMundo;
import com.servinte.axioma.bl.consultaExterna.interfaz.ICitasMundo;
import com.servinte.axioma.bl.consultaExterna.interfaz.IIdentificadoresOrdenadoresConsultaMundo;
import com.servinte.axioma.bl.facturacion.impl.ConvenioContratoMundo;
import com.servinte.axioma.bl.facturacion.interfaz.IConvenioContratoMundo;
import com.servinte.axioma.dto.administracion.ProfesionalSaludDto;
import com.servinte.axioma.dto.consultaExterna.CitaDto;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.dto.consultaExterna.GrupoOrdenadoresConsultaExternaDto;
import com.servinte.axioma.dto.consultaExterna.OrdenadoresConsultaExternaPlanoDto;
import com.servinte.axioma.dto.consultaExterna.ValorEstandarOrdenadoresDto;
import com.servinte.axioma.dto.facturacion.ConvenioDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Clase Fachada que provee todos los servicios de logica de negocio del modulo de
 * Consulta externa a todos los Action de la Capa Web
 * 
 * @author ginsotfu
 * @version 1.0
 * @created 26/10/2012
 */
public class ConsultaExternaFacade {
	/**
	 * Servicio que obtiene los tipos de orden segun el filtro de 
	 * busqueda seleccionados por el usuario (Servicios, medicamentos)
	 * @param tipoOrden
	 * @return
	 * @throws IPSException
	 * @author ginsotfu
	 */
	public List<ValorEstandarOrdenadoresDto> obtenerParametrica(int tipoOrden) throws IPSException{
		ICatalogoConsultaExternaMundo mundoCatalogoConsultaExterna = new CatalogoConsultaExternaMundo();
		return mundoCatalogoConsultaExterna.obtenerParametrica(tipoOrden);
	}
	/**
	 * Servicio que obtiene la consulta de unidad de agenda
	 * 
	 * @param 
	 * @return
	 * @throws IPSException
	 * @author ginsotfu
	 */
	
	public List<DtoUnidadesConsulta> consultarUnidadAgenda() throws IPSException{
		ICatalogoConsultaExternaMundo mundoCatalogoConsultaExterna = new CatalogoConsultaExternaMundo();
		return mundoCatalogoConsultaExterna.consultarUnidadAgenda();
	}
	
	/**
	 * Servicio que guarda parametrica nueva
	 * 
	 * @param valor
	 * @return
	 * @throws IPSException
	 * @author ginsotfu
	 */
	
	public void ingresarOrden(ValorEstandarOrdenadoresDto valor ) throws IPSException{
		ICatalogoConsultaExternaMundo mundoCatalogoConsultaExterna = new CatalogoConsultaExternaMundo();
		mundoCatalogoConsultaExterna.ingresarOrden(valor);
	}
	/**
	 * Servicio que modifica parametrica 
	 * 
	 * @param valor
	 * @return
	 * @throws IPSException
	 * @author ginsotfu
	 */
	public void modificarOrden(ValorEstandarOrdenadoresDto valor ) throws IPSException{
		ICatalogoConsultaExternaMundo mundoCatalogoConsultaExterna = new CatalogoConsultaExternaMundo();
		mundoCatalogoConsultaExterna.modificarOrden(valor);
	}
	/**
	 * Servicio que elimina parametrica 
	 * 
	 * @param valor
	 * @return
	 * @throws IPSException
	 * @author ginsotfu
	 */
	
	public void eliminarOrden(ValorEstandarOrdenadoresDto valor)throws IPSException{
		ICatalogoConsultaExternaMundo mundoCatalogoConsultaExterna = new CatalogoConsultaExternaMundo();
		mundoCatalogoConsultaExterna.eliminarOrden(valor);
	}
	
	/**
	 * Consulta las citas que han sido atendidas por el un profesional X a un paciente Y
	 * 
	 * @param loginUsuario
	 * @param codigoIngreso
	 * @param codigoPaciente
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 24/10/2012
	 */
	public List<CitaDto> consultarCitasAtentidas(String loginUsuario,int codigoIngreso,int codigoPaciente)
			throws IPSException {
		ICitasMundo citasMundo=new CitasMundo();
		return citasMundo.consultarCitasAtentidas(loginUsuario, codigoIngreso, codigoPaciente);
	}

	/**
	 * Consulta las unidades de consulta que estan asociadas a un centro de atencion especifico 
	 * 
	 * @param codigoCentroAtencion
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 20/11/2012
	 */
	public List<DtoUnidadesConsulta> consultarUnidadAgenda(Integer codigoCentroAtencion) throws IPSException{
		IIdentificadoresOrdenadoresConsultaMundo ordenadoresConsultaMundo=new IdentificadoresOrdenadoresConsultaMundo();
		return ordenadoresConsultaMundo.consultarUnidadAgenda(codigoCentroAtencion);
	}
	
	/**
	 * Consulta los diferentes servicios agrupados por grupo servicio ordenados y verifica si tienen tarifa
	 * 
	 * @param grupoOrdenadoresConsultaExternaDto 
	 * @param esquemaTarifarioArticulos 
	 *  
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * 
	 * @created 8/11/2012
	 */
	public List<ServicioAutorizacionOrdenDto> verificarTarifasServicios(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto, int esquemaTarifario)throws IPSException{
		IIdentificadoresOrdenadoresConsultaMundo ordenadoresConsultaMundo=new IdentificadoresOrdenadoresConsultaMundo();
		return ordenadoresConsultaMundo.verificarTarifasServicios(grupoOrdenadoresConsultaExternaDto,esquemaTarifario);
	}
	/**
	 * Consulta los diferentes articulos agrupados por clase de inventario ordenados y verifica si tienen tarifa
	 * 
	 * @param grupoOrdenadoresConsultaExternaDto
	 * @param esquemaTarifario
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 9/11/2012
	 */
	public List<MedicamentoInsumoAutorizacionOrdenDto> verificarTarifasArticulos(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto, int esquemaTarifario)throws IPSException{
		IIdentificadoresOrdenadoresConsultaMundo ordenadoresConsultaMundo=new IdentificadoresOrdenadoresConsultaMundo();
		return ordenadoresConsultaMundo.verificarTarifasArticulos(grupoOrdenadoresConsultaExternaDto,esquemaTarifario);
	}
	
	/**
	 * Consulta para el reporte de ordenadores de consulta externa, sirve para PDF y EXCEL
	 * 
	 * @param grupoOrdenadoresConsultaExternaDto
	 * @param esquemaTarifarioArticulos
	 * @param esquemaTarifarioServicios
	 * 
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @param esquemaTarifarioServicios 
	 * @created 2/11/2012
	 */
	public List<ProfesionalSaludDto> consultarIdentificadorOrdenadoresConsulta(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto,int esquemaTarifarioArticulos, int esquemaTarifarioServicios)throws IPSException{
		IIdentificadoresOrdenadoresConsultaMundo ordenadoresConsultaMundo=new IdentificadoresOrdenadoresConsultaMundo();
		return ordenadoresConsultaMundo.consultarIdentificadorOrdenadoresConsulta(grupoOrdenadoresConsultaExternaDto,esquemaTarifarioArticulos,esquemaTarifarioServicios);
	}
	
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
	public List<OrdenadoresConsultaExternaPlanoDto> consultarIdentificadorOrdenadoresConsultaPlano(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaPlanoDto,int esquemaTarifarioArticulos, int esquemaTarifarioServicios)throws IPSException{
		IIdentificadoresOrdenadoresConsultaMundo ordenadoresConsultaMundo=new IdentificadoresOrdenadoresConsultaMundo();
		return ordenadoresConsultaMundo.consultarIdentificadorOrdenadoresConsultaPlano(grupoOrdenadoresConsultaExternaPlanoDto, esquemaTarifarioArticulos, esquemaTarifarioServicios);
	}
	
	/**
	 * Servicio que obtiene la lista de todos convenios parametrizados
	 * en el sistema que cumplan con los parámetros de búsqueda
	 * 
	 * @param codigoInstitucion
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 14/11/2012
	 */
	public List<ConvenioDto> consultarTodosConveniosPorInstitucion(int codigoInstitucion) throws IPSException{
		IConvenioContratoMundo convenioContratoMundo=new ConvenioContratoMundo();
		return convenioContratoMundo.consultarTodosConveniosPorInstitucion(codigoInstitucion);
	}
	
	/**
	 * Consulta todos los profesionales que han atendido citas
	 * 
	 * @param codigoInsitucion
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 15/11/2012
	 */
	public List<ProfesionalSaludDto> consultarProfesionalesAtiendenCitas(int codigoInsitucion) throws IPSException{
		ICitasMundo citasMundo=new CitasMundo();
		return citasMundo.consultarProfesionalesAtiendenCitas(codigoInsitucion);
	}
	
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
	public GrupoOrdenadoresConsultaExternaDto consultarIdentificadorOrdenadoresConsultaGrupoClase(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto,int esquemaTarifarioArticulos,int esquemaTarifarioServicios)throws IPSException{
		IIdentificadoresOrdenadoresConsultaMundo ordenadoresConsultaMundo=new IdentificadoresOrdenadoresConsultaMundo();
		return ordenadoresConsultaMundo.consultarIdentificadorOrdenadoresConsultaGrupoClase(grupoOrdenadoresConsultaExternaDto, esquemaTarifarioArticulos, esquemaTarifarioServicios);
	}
	
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
	public List<OrdenadoresConsultaExternaPlanoDto> consultarIdentificadorOrdenadoresConsultaGrupoClasePlano(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto,int esquemaTarifarioArticulos,int esquemaTarifarioServicios)throws IPSException{
		IIdentificadoresOrdenadoresConsultaMundo ordenadoresConsultaMundo=new IdentificadoresOrdenadoresConsultaMundo();
		return ordenadoresConsultaMundo.consultarIdentificadorOrdenadoresConsultaGrupoClasePlano(grupoOrdenadoresConsultaExternaDto, esquemaTarifarioArticulos, esquemaTarifarioServicios);
	}
	
	/**
	 * Consulta para realizar validacion de parametrica servicios
	 * 
	 * @param codigoParametrica
	 * @param codigoGrupoServicio
	 * @param codigoUnidadAgenda
	 * @return
	 * @throws IPSException
	 * @author ginsotfu
	 */
	
	public boolean consultarValidacionServicio(Integer codigoParametrica, int codigoGrupoServicio, int codigoUnidadAgenda)throws IPSException{
		ICatalogoConsultaExternaMundo mundoCatalogoConsultaExterna = new CatalogoConsultaExternaMundo();
		return mundoCatalogoConsultaExterna.consultarValidacionServicio(codigoParametrica, codigoGrupoServicio, codigoUnidadAgenda);
	}
	
	/**
	 * Consulta para realizar validacion de parametrica medicamentos
	 * 
	 * @param codigoParametrica
	 * @param codigoClaseInventario
	 * @param codigoUnidadAgenda
	 * @return
	 * @throws IPSException
	 * @author ginsotfu
	 */
	
	public boolean consultarValidacionMedicamento(Integer codigoParametrica, int codigoClaseInventario, int codigoUnidadAgenda)throws IPSException{
		ICatalogoConsultaExternaMundo mundoCatalogoConsultaExterna = new CatalogoConsultaExternaMundo();
		return mundoCatalogoConsultaExterna.consultarValidacionMedicamento(codigoParametrica, codigoClaseInventario, codigoUnidadAgenda);
	}
}
