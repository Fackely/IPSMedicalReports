package com.servinte.axioma.servicio.interfaz.odontologia.presupuesto;

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
import com.servinte.axioma.orm.OtrosSiExclusiones;
import com.servinte.axioma.orm.OtrosSiInclusiones;
import com.servinte.axioma.orm.PresupuestoOdontologico;

/**
 * Esta clase se encarga de definir los métodos de negocio para 
 * el presupuesto odontológico
 *
 * @author Yennifer Guerrero
 */
public interface IPresupuestoOdontologicoServicio {
	
	/**
	 * Este m&eacute;todo se encarga de listar los estados que tiene
	 * un presupuesto odontol&oacute;gico.
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoIntegridadDominio> listarEstadoPresupuestoOdonto(String validaPresupuestoContratado);
	
	/**
	 * Este m&eacute;todo se encarga de listar los indicativos de
	 * contrato para los presupuestos en estado contratado.
	 *
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoIntegridadDominio> listarIndicativoContrato();
	
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
			DtoReporteConsultaPacienteEstadoPresupuesto dto);
	
	
	/**
	 * M&eacute;todo que se encarga de consolidar la consulta
	 * de los presupuestos odontologicos contratados
	 * @param dto
	 * @author Diana Carolina G&oacute;mez
	 */
	public ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado> consolidarConsultaPresupuestosContratados (
			DtoReportePresupuestosOdontologicosContratados dto, String tSalidaReporte );
	
	
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
	public ArrayList<PresupuestoOdontologico> buscarPresupuestoOdontologicoSinSolicitudDescuento();
	
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
			DtoReporteConsultaPacienteEstadoPresupuesto dto);
	
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
		buscarPacientesEstadoPresupuestoTotalizado(DtoReporteConsultaPacienteEstadoPresupuesto dto);
	
	
	/**
	 * M&eacute;todo que se encarga de obtener los presupuestos odontologicos
	 * contratados
	 * @param dto
	 * @return ArrayList<DtoPresupuestosOdontologicosContratados>
	 * @author Diana Carolina G&oacute;mez
	 */
	public ArrayList<DtoPresupuestosOdontologicosContratados> obtenerPresupuestoOdoContratado(DtoReportePresupuestosOdontologicosContratados dto, String tSalidaReporte);
	
	/**
	 * M&eacute;todo que se encarga de consolidar la consulta
	 * de los presupuestos odontologicos contratados
	 * @param dto
	 * @author Javier Gonz&aacute;lez
	 */
	
	public ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> consolidarConsultaPresupuestosContratadosConPromocion (
			DtoReportePresupuestosOdontologicosContratadosConPromocion dto);

	
	/**
	 * Realiza las validaciones para guardar el Otro Si del presupuesto enviado.
	 * Estas validaciones consisten en determinar y asignar el consecutivo 
	 * correspondiente al registro.
	 * 
	 * @autor Cristhian Murillo
	 * @param DtoOtroSi
	 * @return boolean
	 */
	public boolean guardarOtroSiPresupuesto(DtoOtroSi dtoOtroSi);
	

	
	
	public ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> ordenarResultadoConsultaPlano(
			DtoReportePresupuestosOdontologicosContratadosConPromocion dto);
	
	
	
	/**
	 * Implementacion del método persist
	 */
	public void persistOtrosSiInclusiones(OtrosSiInclusiones transientInstance);
	
	
	
	/**
	 * Implementacion del método persist
	 */
	public void persistOtrosSiExclusiones(OtrosSiExclusiones transientInstance);
	
	
	/**
	 * Método que se encarga de calcular las cantidades por número de superficies
	 * 
	 * @param listaProgramasServicios
	 * @param utilizaProgramas
	 * @param modificadas
	 */
	public void calcularCantidadesXNumeroSuperficies(ArrayList<DtoPresuOdoProgServ> listaProgramasServicios, boolean utilizaProgramas, boolean modificadas);
	
	
	
	/**
	 * 
	 * Método que calcula las tarias para los programas/servicios según los convenios
	 * 
	 * @param listaProgramasServicios
	 * @param listaConvenioPresupuesto
	 * @param loginUsuario
	 * @param codigoInstitucion
	 * @param codigoCuentaPaciente
	 * @param utilizaProgramas
	 */
	public void calcularTarifas (ArrayList<DtoPresuOdoProgServ> listaProgramasServicios, ArrayList<InfoConvenioContratoPresupuesto> listaConvenioPresupuesto, 
			String loginUsuario, int codigoInstitucion, int codigoCuentaPaciente, boolean utilizaProgramas) throws IPSException;
	
	/**
	 * Método que carga el detalle de las tarifas para la lista de los Programa/servicios
	 * 
	 * @param detalleTarifasServicio
	 * @param loginUsuario
	 * @param programa
	 * @return
	 */
	public ArrayList<DtoPresupuestoDetalleServiciosProgramaDao> cargarListaServiciosProgramaTarifas(ArrayList<InfoTarifaServicioPresupuesto> detalleTarifasServicio, String loginUsuario , Double programa);
	
	
	/**
	 * Método que crea el encabezado para el presupuesto
	 * 
	 * @param usuario
	 * @param paciente
	 * @param codigoPlanTratamiento
	 * @return
	 */
	public DtoPresupuestoOdontologico crearEncabezadoPresupuesto (UsuarioBasico usuario, PersonaBasica paciente, BigDecimal codigoPlanTratamiento);
	
	
	/**
	 * Método que se encarga de calcular los valores Totales X Convenio.
	 * 
	 * @param listaProgramasServicios
	 * @param listaConvenioPresupuesto
	 * @return
	 */
	public ArrayList<DtoPresupuestoTotalConvenio> crearValoresTotalesXConvenio (ArrayList<DtoPresuOdoProgServ> listaProgramasServicios, ArrayList<InfoConvenioContratoPresupuesto> listaConvenioPresupuesto) throws IPSException;
	
	
	
	/**
	 * Método que se encarga de reunir los convenios
	 * disponibles
	 * 
	 * @param listaPresupuestos
	 * @return
	 */
	public ArrayList<DtoPresupuestoTotalConvenio> obtenerListadoConvenios (ArrayList<DtoPresupuestoOdontologico> listaPresupuestos);
	
	
	/**
	 * Método que carga los convenios para la contratación
	 * del presupuesto
	 * 
	 * @param listaSumatoriaConvenios
	 * @param codigoPersona
	 * @return
	 */
	 public ArrayList<InfoConvenioContratoPresupuesto> cargarConveniosContratoPresupuesto(ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios, int codigoPersona);
	 
}
