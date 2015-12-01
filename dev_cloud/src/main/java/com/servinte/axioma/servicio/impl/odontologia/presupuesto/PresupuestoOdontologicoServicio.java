package com.servinte.axioma.servicio.impl.odontologia.presupuesto;

import java.math.BigDecimal;
import java.util.ArrayList;

import util.odontologia.InfoConvenioContratoPresupuesto;
import util.odontologia.InfoTarifaServicioPresupuesto;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.odontologia.DTOInstitucionReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DTOPacientesReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DtoConsolidadoPresupuestoContratadoPorEstado;
import com.princetonsa.dto.odontologia.DtoConsolidadoPresupuestoContratadoPorPromocion;
import com.princetonsa.dto.odontologia.DtoOtroSi;
import com.princetonsa.dto.odontologia.DtoPresuOdoProgServ;
import com.princetonsa.dto.odontologia.DtoPresupuestoDetalleServiciosProgramaDao;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoPresupuestoTotalConvenio;
import com.princetonsa.dto.odontologia.DtoPresupuestosOdontologicosContratados;
import com.princetonsa.dto.odontologia.DtoReporteConsultaPacienteEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratados;
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratadosConPromocion;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.mundo.fabrica.odontologia.contrato.ContratoFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.presupuesto.PresupuestoFabricaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.contrato.IOtrosSiMundo;
import com.servinte.axioma.mundo.interfaz.presupuesto.IPresupuestoOdontologicoMundo;
import com.servinte.axioma.orm.OtrosSiExclusiones;
import com.servinte.axioma.orm.OtrosSiInclusiones;
import com.servinte.axioma.orm.PresupuestoOdontologico;
import com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoServicio;

/**
 * Esta clase se encarga de implementar la l&oacute;gica de negocio del 
 * presupuesto odontol&oacute;gico.
 *
 * @author Yennifer Guerrero
 * @since  14/09/2010
 *
 */
public class PresupuestoOdontologicoServicio implements IPresupuestoOdontologicoServicio {
	
	IPresupuestoOdontologicoMundo mundo;
	IOtrosSiMundo otrosSiMundo;
	
	
	/**
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Yennifer Guerrero
	 */
	public PresupuestoOdontologicoServicio() {
		mundo 			= PresupuestoFabricaMundo.crearPresupuestoOdontologicoMundo();
		otrosSiMundo 	= ContratoFabricaMundo.crearOtrosSiMundo();
	}
	
	@Override
	public ArrayList<DtoIntegridadDominio> listarEstadoPresupuestoOdonto(String validaPresupuestoContratado){
		return mundo.listarEstadoPresupuestoOdonto(validaPresupuestoContratado);
	}
	
	@Override
	public ArrayList<DtoIntegridadDominio> listarIndicativoContrato(){
		return mundo.listarIndicativoContrato();
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los pacientes por
	 * estados del presupuesto odontológico, según los filtros
	 * enviados por parámetro
	 * 
	 * @param DtoReporteConsultaPacienteEstadoPresupuesto
	 * @return ArrayList<DtoReporteConsultaPacienteEstadoPresupuesto>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DtoReporteConsultaPacienteEstadoPresupuesto> buscarPresupuestoOdontologicoPorEstado(
			DtoReporteConsultaPacienteEstadoPresupuesto dto){
		return mundo.buscarPresupuestoOdontologicoPorEstado(dto);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar el presupuesto odontológico, 
	 * que no esté asociado a una solicitud de descuento 
	 * 
	 * @param DtoReporteConsultaPacienteEstadoPresupuesto
	 * @return ArrayList<DtoReporteConsultaPacienteEstadoPresupuesto>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<PresupuestoOdontologico> buscarPresupuestoOdontologicoSinSolicitudDescuento(){
		return mundo.buscarPresupuestoOdontologicoSinSolicitudDescuento();
	}
	
	/**
	 * 
	 * Este Método se encarga de ordenar los registros de la búsqueda de
	 * pacientes por estados del presupuesto odontológico
	 * 
	 * @param DTOInstitucionReportePacientesEstadoPresupuesto
	 * @return ArrayList<DtoReporteConsultaPacienteEstadoPresupuesto>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto> buscarPresupuestoOdontologicoPorEstadoEstructurado(
			DtoReporteConsultaPacienteEstadoPresupuesto dto){
		return mundo.buscarPresupuestoOdontologicoPorEstadoEstructurado(dto);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los pacientes por estado del 
	 * presupuesto con su respectivo valor totalizado.
	 * 
	 * 
	 * @param DtoReporteConsultaPacienteEstadoPresupuesto dto
	 * @return ArrayList<DTOPacientesReportePacientesEstadoPresupuesto>
	 * @author, Angela Maria Aguirre
	 * 
	 *
	 */
	public ArrayList<DTOPacientesReportePacientesEstadoPresupuesto> 
		buscarPacientesEstadoPresupuestoTotalizado(DtoReporteConsultaPacienteEstadoPresupuesto dto){
		return mundo.buscarPacientesEstadoPresupuestoTotalizado(dto);
	}
	
	
	@Override
	public boolean guardarOtroSiPresupuesto(DtoOtroSi dtoOtroSi) {
		return otrosSiMundo.guardarOtroSiPresupuesto(dtoOtroSi);
	}

	@Override
	public ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado> consolidarConsultaPresupuestosContratados(
			DtoReportePresupuestosOdontologicosContratados dto,
			String tSalidaReporte) {
		
		return mundo.consolidarConsultaPresupuestosContratados(dto, tSalidaReporte);
	}


	/**
	 * M&eacute;todo que se encarga de obtener los presupuestos odontologicos
	 * contratados
	 * @param dto
	 * @author Diana Carolina G&oacute;mez 
	 */
	@Override
	public ArrayList<DtoPresupuestosOdontologicosContratados> obtenerPresupuestoOdoContratado(
			DtoReportePresupuestosOdontologicosContratados dto,
			String tSalidaReporte) {
		return mundo.obtenerPresupuestoOdoContratado(dto, tSalidaReporte);
	}

	@Override
	public ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> consolidarConsultaPresupuestosContratadosConPromocion (
			DtoReportePresupuestosOdontologicosContratadosConPromocion dto){
		return mundo.consolidarConsultaPresupuestosContratadosConPromocion(dto);
	}
	


	public ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> ordenarResultadoConsultaPlano(
			DtoReportePresupuestosOdontologicosContratadosConPromocion dto){
		return mundo.ordenarResultadoConsultaPlano(dto);
	}
	@Override
	public void persistOtrosSiExclusiones(OtrosSiExclusiones transientInstance) {
		otrosSiMundo.persistOtrosSiExclusiones(transientInstance);
	}

	@Override
	public void persistOtrosSiInclusiones(OtrosSiInclusiones transientInstance) {
		otrosSiMundo.persistOtrosSiInclusiones(transientInstance);
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoServicio#calcularCantidadesXNumeroSuperficies(java.util.ArrayList, boolean, boolean)
	 */
	public void calcularCantidadesXNumeroSuperficies(ArrayList<DtoPresuOdoProgServ> listaProgramasServicios, boolean utilizaProgramas, boolean modificadas) {

		mundo.calcularCantidadesXNumeroSuperficies(listaProgramasServicios, utilizaProgramas, modificadas);
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoServicio#cargarListaServiciosProgramaTarifas(java.util.ArrayList, java.lang.String, java.lang.Double)
	 */
	@Override
	public ArrayList<DtoPresupuestoDetalleServiciosProgramaDao> cargarListaServiciosProgramaTarifas(
			ArrayList<InfoTarifaServicioPresupuesto> detalleTarifasServicio,
			String loginUsuario, Double programa) {
		
		return mundo.cargarListaServiciosProgramaTarifas(detalleTarifasServicio, loginUsuario, programa);
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoServicio#calcularTarifas(java.util.ArrayList, java.util.ArrayList, java.lang.String, int, int, boolean)
	 */
	@Override
	public void calcularTarifas(
			ArrayList<DtoPresuOdoProgServ> listaProgramasServicios,
			ArrayList<InfoConvenioContratoPresupuesto> listaConvenioPresupuesto,
			String loginUsuario, int codigoInstitucion,
			int codigoCuentaPaciente, boolean utilizaProgramas) throws IPSException {
		
		mundo.calcularTarifas(listaProgramasServicios, listaConvenioPresupuesto, loginUsuario, codigoInstitucion, 
				codigoCuentaPaciente, utilizaProgramas);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoServicio#crearEncabezadoPresupuesto(com.princetonsa.mundo.UsuarioBasico, com.princetonsa.mundo.PersonaBasica, java.math.BigDecimal)
	 */
	@Override
	public DtoPresupuestoOdontologico crearEncabezadoPresupuesto(
			UsuarioBasico usuario, PersonaBasica paciente,
			BigDecimal codigoPlanTratamiento) {
		
		return mundo.crearEncabezadoPresupuesto(usuario, paciente, codigoPlanTratamiento);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoServicio#crearValoresTotalesXConvenio(java.util.ArrayList, java.util.ArrayList)
	 */
	@Override
	public ArrayList<DtoPresupuestoTotalConvenio> crearValoresTotalesXConvenio(
			ArrayList<DtoPresuOdoProgServ> listaProgramasServicios,
			ArrayList<InfoConvenioContratoPresupuesto> listaConvenioPresupuesto) throws IPSException{
		
		return mundo.crearValoresTotalesXConvenio(listaProgramasServicios, listaConvenioPresupuesto);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoServicio#getSumatoria(java.util.ArrayList)
	 */
	@Override
	public ArrayList<DtoPresupuestoTotalConvenio> obtenerListadoConvenios(
			ArrayList<DtoPresupuestoOdontologico> listaPresupuestos) {
		
		return mundo.obtenerListadoConvenios (listaPresupuestos);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoServicio#cargarConveniosContratoPresupuesto(java.util.ArrayList, int)
	 */
	@Override
	public ArrayList<InfoConvenioContratoPresupuesto> cargarConveniosContratoPresupuesto(
			ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios,
			int codigoPersona) {
		
		return mundo.cargarConveniosContratoPresupuesto(listaSumatoriaConvenios, codigoPersona);
	}

}
