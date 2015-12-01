package com.servinte.axioma.mundo.impl.odontologia.presupuesto;


import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.odontologia.InfoConvenioContratoPresupuesto;
import util.odontologia.InfoPresupuestoXConvenioProgramaServicio;
import util.odontologia.InfoTarifaServicioPresupuesto;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DTOCentroAtencionReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DTOEstadosReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DTOInstitucionReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DTOPacientesReportePacientesEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DtoConsolidadoPresupuestoContratadoPorEstado;
import com.princetonsa.dto.odontologia.DtoConsolidadoPresupuestoContratadoPorPromocion;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoOtroSi;
import com.princetonsa.dto.odontologia.DtoPresuOdoProgServ;
import com.princetonsa.dto.odontologia.DtoPresupuestoContratado;
import com.princetonsa.dto.odontologia.DtoPresupuestoDetalleServiciosProgramaDao;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdoConvenio;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoPresupuestoTotalConvenio;
import com.princetonsa.dto.odontologia.DtoPresupuestosOdontologicosContratados;
import com.princetonsa.dto.odontologia.DtoReporteConsultaPacienteEstadoPresupuesto;
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratados;
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratadosConPromocion;
import com.princetonsa.dto.odontologia.DtoViewPresupuesTotalesConv;
import com.princetonsa.enu.general.EnumTipoModificacion;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.CargosOdon;
import com.servinte.axioma.dao.fabrica.odontologia.presupuesto.PresupuestoFabricaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IPresupuestoContratadoDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoDAO;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.contrato.ContratoFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.presupuesto.PresupuestoFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroAtencionMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.contrato.IOtrosSiMundo;
import com.servinte.axioma.mundo.interfaz.presupuesto.IPresupuestoContratadoMundo;
import com.servinte.axioma.mundo.interfaz.presupuesto.IPresupuestoOdontologicoMundo;
import com.servinte.axioma.mundo.interfaz.presupuesto.IViewPresupuestoTotalesConvMundo;
import com.servinte.axioma.orm.ConveniosIngresoPaciente;
import com.servinte.axioma.orm.OtrosSi;
import com.servinte.axioma.orm.PresupuestoOdontologico;
import com.servinte.axioma.orm.ViewPresupuestoTotalesConv;
import com.servinte.axioma.vista.odontologia.presupuesto.PresupuestoHelperVista;

/**
 * Esta clase se encarga de implementar la l&oacute;gica de negocio del 
 * presupuesto odontol&oacute;gico.
 *
 * @author Yennifer Guerrero
 * @since  14/09/2010
 *
 */
public class PresupuestoOdontologicoMundo implements IPresupuestoOdontologicoMundo {
	
	IPresupuestoOdontologicoDAO dao;
	private static String ESTADO_PRECONTRATADO_CON_SOLICITUD="Precontratado -  Con solicitud Descuento";
	private static String ESTADO_PRECONTRATADO_SIN_SOLICITUD="Precontratado -  Sin solicitud Descuento";
	
	
	/**
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Yennifer Guerrero
	 */
	public PresupuestoOdontologicoMundo() {
		dao = PresupuestoFabricaDAO.crearPresupuestoOdontologicoDAO();
	}
	
	@Override
	public ArrayList<DtoIntegridadDominio> listarEstadoPresupuestoOdonto(String validaPresupuestoContratado){
		
		String[] listadoEstado = null;
		
		if (validaPresupuestoContratado.equals(ConstantesBD.acronimoSi)) {
			listadoEstado = new String[]{
					ConstantesIntegridadDominio.acronimoEstadoActivo,
					ConstantesIntegridadDominio.acronimoInactivo,
					ConstantesIntegridadDominio.acronimoPrecontratado,
					ConstantesIntegridadDominio.acronimoContratado};
		} else {
			listadoEstado = new String[]{
					ConstantesIntegridadDominio.acronimoEstadoActivo,
					ConstantesIntegridadDominio.acronimoInactivo};
		}
		
		Connection con=UtilidadBD.abrirConexion();
		
		ArrayList<DtoIntegridadDominio> listaEstadoPresupuesto = Utilidades.generarListadoConstantesIntegridadDominio(
				con, listadoEstado, false);
		UtilidadBD.closeConnection(con);
		
		return listaEstadoPresupuesto;
	}
	
	@Override
	public ArrayList<DtoIntegridadDominio> listarIndicativoContrato(){
		IPresupuestoContratadoMundo mundo = PresupuestoFabricaMundo.crearPresupuestoContratadoMundo();
		
		ArrayList<DtoIntegridadDominio>listadoIndicativoContrato = mundo.listarIndicativoContrato();
		
		return listadoIndicativoContrato;
	}
	
	@Override
	public ArrayList<DtoIngresosOdontologicos> obtenerPresupuestosPrecontratados(
			List<Long> presupuestos, String conSolicituDcto){
		return dao.obtenerPresupuestosPrecontratados(presupuestos, conSolicituDcto);
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
		return dao.buscarPresupuestoOdontologicoPorEstado(dto);
	}
	
	
	/**
	 * M&eacute;todo encargado de consolidar la consulta
	 * de presupuestos odontologicos contratados y retornarme el 
	 * valor de cada presupuesto
	 * 
	 * @param DtoReportePresupuestosOdontologicosContratados
	 * @return ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado>
	 * @author Diana Carolina G
	 */
	
	public ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado> consolidarConsultaPresupuestosContratados (
			DtoReportePresupuestosOdontologicosContratados dto, String tSalidaReporte){
		
		
		IPresupuestoOdontologicoDAO dao = PresupuestoFabricaDAO.crearPresupuestoOdontologicoDAO();
		ArrayList<DtoPresupuestosOdontologicosContratados> consolidado = new ArrayList<DtoPresupuestosOdontologicosContratados>();
		ArrayList<DtoPresupuestosOdontologicosContratados> listadoPresupContratados = dao.obtenerPresupuestoOdoContratado(dto, tSalidaReporte);
		ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado> listadoResultadoConsulta = new ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado>();
		List<Long> listaCodigosPresupuestos = new ArrayList<Long>();
		ICentroAtencionMundo mundoCentroAtencion = AdministracionFabricaMundo.crearCentroAtencionMundo();
		
		//ArrayList<DtoCentrosAtencion> centrosAtencion = new ArrayList<DtoCentrosAtencion>();
		
		//para asignar el valor del presupuesto a cada registro del resultado de la consulta.
		if ( !Utilidades.isEmpty(listadoPresupContratados)) {
			
			for (DtoPresupuestosOdontologicosContratados registro : listadoPresupContratados) {
				listaCodigosPresupuestos.add(registro.getCodigoPkPresupuesto());
				
				/*DtoCentrosAtencion dtoCentroAtencion = new DtoCentrosAtencion();
				dtoCentroAtencion.setConsecutivo(registro.getConsCentroAtencionContrato());
				centrosAtencion.add(dtoCentroAtencion); */
			}
			
			ArrayList<DtoCentrosAtencion> centrosAtencion = mundoCentroAtencion.obtenerCentrosAtencionPresupuestos(listaCodigosPresupuestos);
			
			IPresupuestoContratadoMundo mundo = PresupuestoFabricaMundo.crearPresupuestoContratadoMundo();
			
			consolidado = mundo.consolidarConsultaPresupuestosContratados(listadoPresupContratados);
			
			// Filtra los presupuestos que se encuentran dentro del rango de valores de contrato seleccionados
			if (!Utilidades.isEmpty(consolidado)) {
				if (dto.getValorContratoInicial() >0 &&
						dto.getValorContratoFinal() >0){
							consolidado = obtenerValoresPresupuestosContratados(dto, consolidado);
				}
			}
			//********************************
			
			
			
			listadoResultadoConsulta = ordenarResultadoConsultaPresupuestosContratados(consolidado, centrosAtencion, listaCodigosPresupuestos);
		}
		
		
		
		
		return listadoResultadoConsulta;
		
	}

	/**
	 * M&eacute;todo encargado de ordenar por indicativo de contratado 
	 * el consolidado de la consulta de Presupuesto Odontologico Contratado 
	 * @param consolidado
	 * @param centrosAtencion
	 * @param listaCodigosPresupuestos
	 * @return ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado>
	 * @author Diana Carolina G
	 */
	
	
	private ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado> ordenarResultadoConsultaPresupuestosContratados(
			ArrayList<DtoPresupuestosOdontologicosContratados> consolidado,
			ArrayList<DtoCentrosAtencion> centrosAtencion,
			List<Long> listaCodigosPresupuestos) {
		
		ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado> consolidadoPorEstado = new ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado>();
		
		for (DtoCentrosAtencion registro : centrosAtencion) {
			
			ArrayList<DtoPresupuestosOdontologicosContratados> listadoEstadoContratado = new ArrayList<DtoPresupuestosOdontologicosContratados>();
			ArrayList<DtoPresupuestosOdontologicosContratados> listadoEstadoCancelado = new ArrayList<DtoPresupuestosOdontologicosContratados>();
			ArrayList<DtoPresupuestosOdontologicosContratados> listadoEstadoSuspendido = new ArrayList<DtoPresupuestosOdontologicosContratados>();
			ArrayList<DtoPresupuestosOdontologicosContratados> listadoEstadoTerminado = new ArrayList<DtoPresupuestosOdontologicosContratados>();
			
			ArrayList<DtoPresupuestosOdontologicosContratados> listadoConsolidadoPorEstado = new ArrayList<DtoPresupuestosOdontologicosContratados>();
			
			DtoConsolidadoPresupuestoContratadoPorEstado dtoConsolidado = new DtoConsolidadoPresupuestoContratadoPorEstado();
			
			for (DtoPresupuestosOdontologicosContratados dto : consolidado) {
				
					if (dto.getConsCentroAtencionContrato() == registro.getConsecutivo()) {
					
						if (dto.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoContratadoContratado)) {
							
							listadoEstadoContratado.add(dto);
						
						}
						else if (dto.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoContratadoCancelado)) {
							
							listadoEstadoCancelado.add(dto);
						}
						else if (dto.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente)) {

							listadoEstadoSuspendido.add(dto);
						}
						else if (dto.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoContratadoTerminado)) {
							
							listadoEstadoTerminado.add(dto);
						}
					}
			}
			
			listadoConsolidadoPorEstado.addAll(listadoEstadoContratado);
			listadoConsolidadoPorEstado.addAll(listadoEstadoCancelado);
			listadoConsolidadoPorEstado.addAll(listadoEstadoSuspendido);
			listadoConsolidadoPorEstado.addAll(listadoEstadoTerminado);
			dtoConsolidado.setConsecutivoCentroAtencion(registro.getConsecutivo());
			dtoConsolidado.setListadoConsolidadoPorEstado(listadoConsolidadoPorEstado);
			
			consolidadoPorEstado.add(dtoConsolidado); 
		}
		return consolidadoPorEstado;
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
		return dao.buscarPresupuestoOdontologicoSinSolicitudDescuento();		
	}
	
	
	/**
	 * 
	 * Este Método se encarga de crear una lista ordenada
	 * de centros de atención por su respectiva institución
	 * 
	 * @param ArrayList<DTOCentroAtencionReportePacientesEstadoPresupuesto> listaCentroAtencion
	 * @retun ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto>
	 * @author, Angela Maria Aguirre
	 *
	 */
	private ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto> ordenarPresupuestoOdontologicoPorInstitucion(
			ArrayList<DTOCentroAtencionReportePacientesEstadoPresupuesto> listaCentroAtencion){
		
		ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto> listaInstitucion = 
			new ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto>();
		
		DTOInstitucionReportePacientesEstadoPresupuesto dtoInstitucion= null;
		ArrayList<DTOCentroAtencionReportePacientesEstadoPresupuesto> listaCAInstitucion = null;
		
		long totalPresupuestoInstitucion=0L;				
		
		for (int i = 0; i < listaCentroAtencion.size(); i++){
			dtoInstitucion= new DTOInstitucionReportePacientesEstadoPresupuesto();
			listaCAInstitucion = new ArrayList<DTOCentroAtencionReportePacientesEstadoPresupuesto>();
			totalPresupuestoInstitucion=0L;
			
			dtoInstitucion.setCodigoInstitucion(listaCentroAtencion.get(i).getCodigoInstitucion());
			dtoInstitucion.setNombreInstitucion(listaCentroAtencion.get(i).getNombreInstitucion());
			totalPresupuestoInstitucion = totalPresupuestoInstitucion + 
				(listaCentroAtencion.get(i)).getTotalPresupuestoCentroAtencion(); 
				
			listaCAInstitucion.add(listaCentroAtencion.get(i));
			
			int j=0;	
			for ( j = i+1; j < listaCentroAtencion.size(); j++){	
				if(listaCentroAtencion.get(i).getCodigoInstitucion() == listaCentroAtencion.get(j).getCodigoInstitucion() ){
		
					listaCAInstitucion.add(listaCentroAtencion.get(j));
					totalPresupuestoInstitucion = totalPresupuestoInstitucion + 
						(listaCentroAtencion.get(j)).getTotalPresupuestoCentroAtencion();
				}else{
					break;
				}
			}
			i = j-1;
			dtoInstitucion.setTotalPresupuestoInstitucion(totalPresupuestoInstitucion);
			dtoInstitucion.setListaCentroAtencion(listaCAInstitucion);
			listaInstitucion.add(dtoInstitucion);
		}	
		
		return listaInstitucion;
	}
	
	
	/**
	 * 
	 * Este Método se encarga de ordenar los registros de la búsqueda de
	 * pacientes por estados del presupuesto odontológico
	 * 
	 * @param DtoReporteConsultaPacienteEstadoPresupuesto
	 * @return ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto> buscarPresupuestoOdontologicoPorEstadoEstructurado(
			DtoReporteConsultaPacienteEstadoPresupuesto dto){
		
		ArrayList<DTOInstitucionReportePacientesEstadoPresupuesto> listaInstitucion = null;
		dto.setCodigoInstitucion(ConstantesBD.codigoNuncaValido);
		ArrayList<DtoReporteConsultaPacienteEstadoPresupuesto> lista = dao.buscarPresupuestoOdontologicoPorEstado(dto);
				
		if(lista!=null && lista.size()>0){
							
			ArrayList<DTOCentroAtencionReportePacientesEstadoPresupuesto> listaCentroAtencion = 
				new ArrayList<DTOCentroAtencionReportePacientesEstadoPresupuesto>();			
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto> listaEstados = 
				new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>();
			
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoActivo=new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>();
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoInactivo=new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>();
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoPrecontratadoConSolicitud=new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>();
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoPrecontratadoSinSolicitud=new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>();
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoContratadoContratado=new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>();
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoContratadoSuspendido=new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>();
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoContratadoTerminado=new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>();
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoContratadoCancelado=new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>();
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoPrecontratadoAmbos=new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>();
			DTOCentroAtencionReportePacientesEstadoPresupuesto dtoCentroAtencion=new DTOCentroAtencionReportePacientesEstadoPresupuesto();
			int codigoCentroAtencion=lista.get(0).getCodigoCentroAtencion();
			long codigoInstitucion = lista.get(0).getCodigoInstitucion();
			int penultimoRegistro=0;
			String nombreInstitucion = lista.get(0).getNombreInstitucion();
			long totalPresupuestoCA=0L;
			
			String nombreCompletoCentroAtencion = lista.get(0).getNombreCentroAtencion()+" - "+
			lista.get(0).getNombreCiudad()+" ("+ lista.get(0).getNombrePais()+") "+" - Región: "+lista.get(0).getNombreRegion();
			
			for(DtoReporteConsultaPacienteEstadoPresupuesto registro : lista){
				
				if(codigoCentroAtencion==registro.getCodigoCentroAtencion()){
					
					poblarListasEstadoPresupuestoOdontologico(
							registro,dto,listaEstadoActivo,
							listaEstadoInactivo,listaEstadoContratadoContratado,listaEstadoContratadoSuspendido,
							listaEstadoContratadoTerminado,listaEstadoContratadoCancelado,
							listaEstadoPrecontratadoSinSolicitud,listaEstadoPrecontratadoConSolicitud);
					
					totalPresupuestoCA = totalPresupuestoCA + registro.getTotalPresupuestoPorEstado();
					
				}else{					
					
					dtoCentroAtencion=new DTOCentroAtencionReportePacientesEstadoPresupuesto();
					dtoCentroAtencion.setNombreCompletoCentroAtencion(nombreCompletoCentroAtencion);
					dtoCentroAtencion.setTotalPresupuestoCentroAtencion(totalPresupuestoCA);
					dtoCentroAtencion.setCodigoInstitucion(codigoInstitucion);
					dtoCentroAtencion.setNombreInstitucion(nombreInstitucion);
					dtoCentroAtencion.setCodigoCentroAtencion(codigoCentroAtencion);
					totalPresupuestoCA = 0L;
					
					if(listaEstadoActivo!=null && listaEstadoActivo.size()>0){
						listaEstados.addAll(listaEstadoActivo);
					}
					if(listaEstadoInactivo!=null && listaEstadoInactivo.size()>0){
						listaEstados.addAll(listaEstadoInactivo);
					}
					if(listaEstadoPrecontratadoConSolicitud!=null && listaEstadoPrecontratadoConSolicitud.size()>0){
						listaEstados.addAll(listaEstadoPrecontratadoConSolicitud);
					}
					if(listaEstadoPrecontratadoSinSolicitud!=null && listaEstadoPrecontratadoSinSolicitud.size()>0){
						listaEstados.addAll(listaEstadoPrecontratadoSinSolicitud);
					}
					if(listaEstadoPrecontratadoAmbos!=null && listaEstadoPrecontratadoAmbos.size()>0){
						listaEstados.addAll(listaEstadoPrecontratadoAmbos);
					}
					if(listaEstadoContratadoContratado!=null && listaEstadoContratadoContratado.size()>0){
						listaEstados.addAll(listaEstadoContratadoContratado);
					}
					if(listaEstadoContratadoSuspendido!=null && listaEstadoContratadoSuspendido.size()>0){
						listaEstados.addAll(listaEstadoContratadoSuspendido);
					}
					if(listaEstadoContratadoTerminado!=null && listaEstadoContratadoTerminado.size()>0){
						listaEstados.addAll(listaEstadoContratadoTerminado);
					}
					if(listaEstadoContratadoCancelado!=null && listaEstadoContratadoCancelado.size()>0){
						listaEstados.addAll(listaEstadoContratadoCancelado);
					}					
					
					dtoCentroAtencion.setListaEstados(listaEstados);				
					listaCentroAtencion.add(dtoCentroAtencion);
					
					listaEstados = new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>();
					listaEstadoActivo = new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>();
					listaEstadoInactivo = new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>();
					listaEstadoContratadoContratado = new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>();
					listaEstadoContratadoSuspendido = new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>();
					listaEstadoContratadoTerminado = new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>(); 
					listaEstadoContratadoCancelado = new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>();
					listaEstadoPrecontratadoSinSolicitud = new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>();
					listaEstadoPrecontratadoConSolicitud = new ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>();
																		
					poblarListasEstadoPresupuestoOdontologico(
							registro,dto,listaEstadoActivo,
							listaEstadoInactivo,listaEstadoContratadoContratado,listaEstadoContratadoSuspendido,
							listaEstadoContratadoTerminado,listaEstadoContratadoCancelado,
							listaEstadoPrecontratadoSinSolicitud,listaEstadoPrecontratadoConSolicitud);
					
					totalPresupuestoCA = totalPresupuestoCA + registro.getTotalPresupuestoPorEstado();					
					codigoCentroAtencion = registro.getCodigoCentroAtencion();					
					codigoInstitucion = registro.getCodigoInstitucion();
					nombreInstitucion = registro.getNombreInstitucion();
					
					nombreCompletoCentroAtencion = lista.get(0).getNombreCentroAtencion()+" - "+
					lista.get(0).getNombreCiudad()+" ("+ lista.get(0).getNombrePais()+") "+" - Región: "+lista.get(0).getNombreRegion();
				}	
			}	
			
			if(listaEstados==null || listaEstados.size()<=0){
				
				if(listaEstadoActivo!=null && listaEstadoActivo.size()>0){
					listaEstados.addAll(listaEstadoActivo);
				}
				if(listaEstadoInactivo!=null && listaEstadoInactivo.size()>0){
					listaEstados.addAll(listaEstadoInactivo);
				}
				if(listaEstadoPrecontratadoConSolicitud!=null && listaEstadoPrecontratadoConSolicitud.size()>0){
					listaEstados.addAll(listaEstadoPrecontratadoConSolicitud);
				}
				if(listaEstadoPrecontratadoSinSolicitud!=null && listaEstadoPrecontratadoSinSolicitud.size()>0){
					listaEstados.addAll(listaEstadoPrecontratadoSinSolicitud);
				}
				if(listaEstadoPrecontratadoAmbos!=null && listaEstadoPrecontratadoAmbos.size()>0){
					listaEstados.addAll(listaEstadoPrecontratadoAmbos);
				}
				if(listaEstadoContratadoContratado!=null && listaEstadoContratadoContratado.size()>0){
					listaEstados.addAll(listaEstadoContratadoContratado);
				}
				if(listaEstadoContratadoSuspendido!=null && listaEstadoContratadoSuspendido.size()>0){
					listaEstados.addAll(listaEstadoContratadoSuspendido);
				}
				if(listaEstadoContratadoTerminado!=null && listaEstadoContratadoTerminado.size()>0){
					listaEstados.addAll(listaEstadoContratadoTerminado);
				}
				if(listaEstadoContratadoCancelado!=null && listaEstadoContratadoCancelado.size()>0){
					listaEstados.addAll(listaEstadoContratadoCancelado);
				}
			}
			
			if(listaCentroAtencion.size()>0){
				if(listaCentroAtencion.size()>=2){
					penultimoRegistro=2;
				}else{
					penultimoRegistro=1;
				}				
				if(listaCentroAtencion.get(listaCentroAtencion.size()-penultimoRegistro)
						.getCodigoCentroAtencion()==codigoCentroAtencion){
					
					if(listaCentroAtencion.size()>0){
						listaCentroAtencion.get(listaCentroAtencion.size()-1).getListaEstados().add(
								listaEstados.get(listaEstados.size()-1));						
					}				
				}else{
					dtoCentroAtencion=new DTOCentroAtencionReportePacientesEstadoPresupuesto();					
					
					dtoCentroAtencion.setNombreCompletoCentroAtencion(nombreCompletoCentroAtencion);
					dtoCentroAtencion.setTotalPresupuestoCentroAtencion(totalPresupuestoCA);
					dtoCentroAtencion.setCodigoInstitucion(codigoInstitucion);
					dtoCentroAtencion.setNombreInstitucion(nombreInstitucion);
					dtoCentroAtencion.setCodigoCentroAtencion(codigoCentroAtencion);					
					dtoCentroAtencion.setListaEstados(listaEstados);				
					listaCentroAtencion.add(dtoCentroAtencion);
				}
			}else{
				dtoCentroAtencion=new DTOCentroAtencionReportePacientesEstadoPresupuesto();					
				
				dtoCentroAtencion.setNombreCompletoCentroAtencion(nombreCompletoCentroAtencion);
				dtoCentroAtencion.setTotalPresupuestoCentroAtencion(totalPresupuestoCA);
				dtoCentroAtencion.setCodigoInstitucion(codigoInstitucion);
				dtoCentroAtencion.setNombreInstitucion(nombreInstitucion);
				dtoCentroAtencion.setCodigoCentroAtencion(codigoCentroAtencion);					
				dtoCentroAtencion.setListaEstados(listaEstados);				
				listaCentroAtencion.add(dtoCentroAtencion);
			}
			
			listaInstitucion =  
				ordenarPresupuestoOdontologicoPorInstitucion(listaCentroAtencion);			
		}
		return listaInstitucion;
	}
	
	
	/**
	 * 
	 * Este Método se encarga de ordenar los registros de la búsqueda de
	 * presupuestos odontológicos según el estado de éste
	 * 
	 * @param DtoReporteConsultaPacienteEstadoPresupuesto registro,
	 *		DtoReporteConsultaPacienteEstadoPresupuesto dto,
	 *		ArrayList<DTOEstadosReportePacientesEstadoPresupuesto> listaEstados,
	 *		ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoActivo,
	 *		ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoInactivo,
	 *		ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoContratadoContratado,
	 *		ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoContratadoSuspendido,
	 *		ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoContratadoTerminado,
	 *		ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoContratadoCancelado,
	 *		ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoPrecontratadoSinSolicitud,
	 *		ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoPrecontratadoConSolicitud
	 * @author, Angela Maria Aguirre
	 *
	 */
	private void poblarListasEstadoPresupuestoOdontologico(
			DtoReporteConsultaPacienteEstadoPresupuesto registro,
			DtoReporteConsultaPacienteEstadoPresupuesto dto,
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoActivo,
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoInactivo,
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoContratadoContratado,
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoContratadoSuspendido,
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoContratadoTerminado,
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoContratadoCancelado,
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoPrecontratadoSinSolicitud,
			ArrayList<DTOEstadosReportePacientesEstadoPresupuesto>listaEstadoPrecontratadoConSolicitud){
		
		DtoReporteConsultaPacienteEstadoPresupuesto dtoSubconsulta = 
				new DtoReporteConsultaPacienteEstadoPresupuesto();
		ArrayList<DtoReporteConsultaPacienteEstadoPresupuesto> listaConDescuento=
			new ArrayList<DtoReporteConsultaPacienteEstadoPresupuesto>();
		
		DTOEstadosReportePacientesEstadoPresupuesto dtoEstado = 
			new DTOEstadosReportePacientesEstadoPresupuesto();
	
		Long cantidadPresupuestoSinSolicitud=1L;
		
		if(registro.getNombreEstadoPresupuesto().equals(
				ConstantesIntegridadDominio.acronimoPrecontratado)){
			
			if( UtilidadTexto.isEmpty(dto.getEstadoPresupuesto()) || 
				(!UtilidadTexto.isEmpty(dto.getConSolicitudDcto()) && 
					dto.getConSolicitudDcto().equals(
							ConstantesIntegridadDominio.acronimoAmbos))){							
				
				dtoSubconsulta.setFechaInicial(dto.getFechaInicial());
				dtoSubconsulta.setFechaFinal(dto.getFechaFinal());
				dtoSubconsulta.setCodigoPais(dto.getCodigoPais());
				dtoSubconsulta.setCiudadDeptoPais(dto.getCiudadDeptoPais());
				dtoSubconsulta.setCodigoRegion(dto.getCodigoRegion());
				
				dtoSubconsulta.setConsecutivoCentroAtencion(registro.getCodigoCentroAtencion());
				
				dtoSubconsulta.setEdadInicial(dto.getEdadInicial());
				dtoSubconsulta.setEdadFinal(dto.getEdadFinal());
				dtoSubconsulta.setSexoPaciente(dto.getSexoPaciente());
				dtoSubconsulta.setCodigoPaqueteOdonto(dto.getCodigoPaqueteOdonto());
				dtoSubconsulta.setCodigoPrograma(dto.getCodigoPrograma());
				dtoSubconsulta.setEstadoPresupuesto(ConstantesIntegridadDominio.acronimoPrecontratado);
				dtoSubconsulta.setConSolicitudDcto(ConstantesBD.acronimoSi);
				
				
				if(dto.isEsMultiempresa()){
					dtoSubconsulta.setCodigoEmpresaInstitucion(dto.getCodigoEmpresaInstitucion());
					dtoSubconsulta.setCodigoInstitucion(ConstantesBD.codigoNuncaValidoLong);
					dtoSubconsulta.setEsMultiempresa(true);
				}else{
					dtoSubconsulta.setCodigoInstitucion(registro.getCodigoInstitucion());
					dtoSubconsulta.setCodigoEmpresaInstitucion(ConstantesBD.codigoNuncaValidoLong);
					dtoSubconsulta.setEsMultiempresa(false);
				}
				
				listaConDescuento = dao.buscarPresupuestoOdontologicoPorEstado(dtoSubconsulta);
				
				dtoEstado.setNombreEstado(ESTADO_PRECONTRATADO_SIN_SOLICITUD);		
				
				if(listaConDescuento!=null && listaConDescuento.size()>=1){
					
					cantidadPresupuestoSinSolicitud = registro.getTotalPresupuestoPorEstado()-listaConDescuento.size()+0L;
					
					if(cantidadPresupuestoSinSolicitud>0){						
						
						dtoEstado.setCantidadPresupuesto(cantidadPresupuestoSinSolicitud);
						listaEstadoPrecontratadoSinSolicitud.add(dtoEstado);
					}	
					
					dtoEstado = new DTOEstadosReportePacientesEstadoPresupuesto();
					dtoEstado.setNombreEstado(ESTADO_PRECONTRATADO_CON_SOLICITUD);
					dtoEstado.setCantidadPresupuesto(listaConDescuento.size()+0L);
					listaEstadoPrecontratadoConSolicitud.add(dtoEstado);
									
				}else{
					dtoEstado.setCantidadPresupuesto(registro.getTotalPresupuestoPorEstado()+0L);
					listaEstadoPrecontratadoSinSolicitud.add(dtoEstado);
				}								
			}
			if(!UtilidadTexto.isEmpty(dto.getEstadoPresupuesto()) &&
					!UtilidadTexto.isEmpty(dto.getConSolicitudDcto())){
				
				if(dto.getConSolicitudDcto().equals(ConstantesBD.acronimoSi)){
					dtoEstado.setNombreEstado(ESTADO_PRECONTRATADO_CON_SOLICITUD);
					dtoEstado.setCantidadPresupuesto(registro.getTotalPresupuestoPorEstado()+0L);
					listaEstadoPrecontratadoConSolicitud.add(dtoEstado);								
					
				}else{
					if(dto.getConSolicitudDcto().equals(ConstantesBD.acronimoNo)){
						dtoEstado.setNombreEstado(ESTADO_PRECONTRATADO_SIN_SOLICITUD);
						dtoEstado.setCantidadPresupuesto(registro.getTotalPresupuestoPorEstado()+0L);
						listaEstadoPrecontratadoSinSolicitud.add(dtoEstado);									
					}
				}	
			}
		}else if(registro.getNombreEstadoPresupuesto().equals(
				ConstantesIntegridadDominio.acronimoEstadoActivo)){
			
			dtoEstado.setNombreEstado((String)ValoresPorDefecto.getIntegridadDominio(
				ConstantesIntegridadDominio.acronimoEstadoActivo));	
			dtoEstado.setCantidadPresupuesto(registro.getTotalPresupuestoPorEstado()+0L);
			listaEstadoActivo.add(dtoEstado);
			
		}else if(registro.getNombreEstadoPresupuesto().equals(
				ConstantesIntegridadDominio.acronimoInactivo)){
			
			dtoEstado.setNombreEstado((String)ValoresPorDefecto.getIntegridadDominio(
					ConstantesIntegridadDominio.acronimoInactivo));	
			dtoEstado.setCantidadPresupuesto(registro.getTotalPresupuestoPorEstado()+0L);
			listaEstadoInactivo.add(dtoEstado);	
			
		}else if(registro.getNombreEstadoPresupuesto().equals(
				ConstantesIntegridadDominio.acronimoContratadoContratado)){
			
			dtoEstado.setNombreEstado((String)ValoresPorDefecto.getIntegridadDominio(
					ConstantesIntegridadDominio.acronimoContratadoContratado));
			dtoEstado.setCantidadPresupuesto(registro.getTotalPresupuestoPorEstado()+0L);
			listaEstadoContratadoContratado.add(dtoEstado);
			
		}else if(registro.getNombreEstadoPresupuesto().equals(
				ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente)){
			
			dtoEstado.setNombreEstado((String)ValoresPorDefecto.getIntegridadDominio(
					ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente));
			dtoEstado.setCantidadPresupuesto(registro.getTotalPresupuestoPorEstado()+0L);
			listaEstadoContratadoSuspendido.add(dtoEstado);
			
		}else if(registro.getNombreEstadoPresupuesto().equals(
				ConstantesIntegridadDominio.acronimoContratadoTerminado)){
			
			dtoEstado.setNombreEstado((String)ValoresPorDefecto.getIntegridadDominio(
					ConstantesIntegridadDominio.acronimoContratadoTerminado));	
			dtoEstado.setCantidadPresupuesto(registro.getTotalPresupuestoPorEstado()+0L);
			listaEstadoContratadoTerminado.add(dtoEstado);
			
		}else if(registro.getNombreEstadoPresupuesto().equals(
				ConstantesIntegridadDominio.acronimoContratadoCancelado)){
			
			dtoEstado.setNombreEstado((String)ValoresPorDefecto.getIntegridadDominio(
					ConstantesIntegridadDominio.acronimoContratadoCancelado));
			dtoEstado.setCantidadPresupuesto(registro.getTotalPresupuestoPorEstado()+0L);
			listaEstadoContratadoCancelado.add(dtoEstado);	
			
		}		 
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
		
		IPresupuestoContratadoDAO daoPresContratado= PresupuestoFabricaDAO.crearPresupuestoContratado();
		ArrayList<DTOPacientesReportePacientesEstadoPresupuesto>  lista = dao.buscarPacientesEstadoPresupuestoTotalizado(dto);
		DtoPresupuestoContratado dtoPresupContratado = null;
				
		if(lista!=null && lista.size()>0){
			IViewPresupuestoTotalesConvMundo vistaPresupuestoMundo = 
				PresupuestoFabricaMundo.crearViewPresupuestoTotalesConvMundo();
			DtoViewPresupuesTotalesConv dtoConsultaVista = null;
			
			for(DTOPacientesReportePacientesEstadoPresupuesto presupuesto :lista){
				
				if(presupuesto.getFechaNacimiento()!=null){
					presupuesto.setEdadPaciente(String.valueOf(UtilidadFecha.calcularEdad(
						UtilidadFecha.conversionFormatoFechaAAp(presupuesto.getFechaNacimiento()))));
				}
				
				dtoPresupContratado = new DtoPresupuestoContratado();
				dtoPresupContratado.setCodigoPresupuesto(presupuesto.getCodigoPresupuesto());
				List<DtoPresupuestoContratado> listaContratos = 
					daoPresContratado.obtenerContratosPresupuestoOdonto(dtoPresupContratado);
				
				if(listaContratos!=null && listaContratos.size()>0){
					Long consecutivoContrao = (listaContratos.get(
							listaContratos.size()-1)).getConsecutivoContrato();
					if(consecutivoContrao!=null && consecutivoContrao>0){
						presupuesto.setNumeroContrato(consecutivoContrao.intValue());
					}else{
						presupuesto.setNumeroContrato(ConstantesBD.codigoNuncaValido);
					}
					
				}else{
					presupuesto.setNumeroContrato(ConstantesBD.codigoNuncaValido);
				}
				
				if(dto.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoEstadoActivo)||
						dto.getEstadoPresupuesto().equals(ConstantesIntegridadDominio.acronimoInactivo)	){
					presupuesto.setValorTotalPresupuesto("");
				}else{
					dtoConsultaVista = new DtoViewPresupuesTotalesConv();
					dtoConsultaVista.setCodigoPresupuesto(presupuesto.getCodigoPresupuesto());					
					
					List<ViewPresupuestoTotalesConv>  listaVistaPresupuesto= 
						vistaPresupuestoMundo.obtenerViewPresupuesto(dtoConsultaVista);
					
					if(listaVistaPresupuesto!=null && listaVistaPresupuesto.size()>0){
						double valor = 0;
						
						for(ViewPresupuestoTotalesConv dtoVista : listaVistaPresupuesto){
							
							if(dtoVista.getId().getValorSubtotalContratado().doubleValue()>0){
								
								valor = valor + (dtoVista.getId().getValorSubtotalContratado().doubleValue());
															
							}
						}	
						if(valor>0){
							
							presupuesto.setValorTotalPresupuesto(UtilidadTexto.formatearValores(valor));
						}else{
							
							presupuesto.setValorTotalPresupuesto("");
						}
					}	
				}							
			}
		}
		
		return lista;		
	}
	
		
	
	/**
	 * M&eacute;todo encargado de obtener los valores 
	 * de los presupuestos contratados que se 
	 * encuentran dentro del rango de valor inicial
	 * y final seleccionados en el reporte de presupuestos
	 * odontologicos contratados 
	 * 
	 * @param DtoReportePresupuestosOdontologicosContratados
	 * @return ArrayList<DtoConsolidadoPresupuestoContratadoPorEstado>
	 * @author Diana Carolina G
	 */
	
	public ArrayList<DtoPresupuestosOdontologicosContratados> obtenerValoresPresupuestosContratados (DtoReportePresupuestosOdontologicosContratados dto, 
			ArrayList<DtoPresupuestosOdontologicosContratados> listadoResultado){
		
		ArrayList<DtoPresupuestosOdontologicosContratados> listadoPresupContratados = new ArrayList<DtoPresupuestosOdontologicosContratados>();
		
		for (DtoPresupuestosOdontologicosContratados registro : listadoResultado) {
			
			if (registro.getValorPresupuesto() > 0) {
				double valor = registro.getValorPresupuesto();
				
				if (valor < dto.getValorContratoFinal() && valor > dto.getValorContratoInicial()) {
					listadoPresupContratados.add(registro);
				}
			}
		}
		
		return listadoPresupContratados;
		
	}
	
	
	@Override
	public PresupuestoOdontologico buscarPresupuestoId(long codigoPk) {
		return dao.buscarPresupuestoId(codigoPk);
	}

	/**
	 * M&eacute;todo que se encarga de obtener los presupuestos odontologicos
	 * contratados
	 * @param dto
	 * @return ArrayList<DtoPresupuestosOdontologicosContratados
	 * @author Diana Carolina G&oacute;mez
	 */
	
	@Override
	public ArrayList<DtoPresupuestosOdontologicosContratados> obtenerPresupuestoOdoContratado(
			DtoReportePresupuestosOdontologicosContratados dto,
			String tSalidaReporte) {
		
		return dao.obtenerPresupuestoOdoContratado(dto, tSalidaReporte);
	}

	


	public ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> consolidarConsultaPresupuestosContratadosConPromocion (
				DtoReportePresupuestosOdontologicosContratadosConPromocion dto){
	
		

		ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> resultado = com.princetonsa.mundo.odontologia.PresupuestoOdontologico.obtenerPresupuestoOdoContratadoPromo(dto);
		ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> listaDetalles = new ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion>();
		ArrayList<Integer> consecutivosCentrosAtencion = new ArrayList<Integer>();
		ArrayList<Integer> codigosPromocion = new ArrayList<Integer>();
		BigDecimal total = new BigDecimal("00000000.00");
		BigDecimal totalPromocion = new BigDecimal("00000000.00");
		BigDecimal t = new BigDecimal("00000000.00");
		int i=0;
		// Ordenar por Promocion odontologica
		if (!Utilidades.isEmpty(resultado)) {
			
			for ( i = 0; i < resultado.size(); i++) {
				listaDetalles = new ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion>();
				totalPromocion = new BigDecimal("00000000.00");
				
				if (!consecutivosCentrosAtencion.contains(resultado
						.get(i).getConsCentroAtencionContrato())){
					codigosPromocion = new ArrayList<Integer>();
					
				}
				
				if (!codigosPromocion.contains(resultado
						.get(i).getCodigoPkPromocion())) {
					for (int j = i ; j < resultado.size(); j++) {
						if (resultado.get(i)
									.getConsCentroAtencionContrato() == resultado.get(j).getConsCentroAtencionContrato()
								&& 
								resultado.get(i).getCodigoPkPromocion() == resultado.get(j).getCodigoPkPromocion()
								&& resultado.get(i).getFechaContrato().compareTo(resultado.get(i).getFechaInicialvige())>=0 
								&& resultado.get(i).getFechaContrato().compareTo(resultado.get(i).getFechaFinalvige())<=0) {
							
							listaDetalles.add(resultado.get(j));
							totalPromocion=totalPromocion.add(resultado.get(j).getValorDescuentoProm());
							total=total.add(resultado.get(j).getValorDescuentoProm());
							if(totalPromocion.compareTo(t) > 0){
								resultado.get(j).setTotalPromo(totalPromocion);
								
							}
						}
					
					}
					consecutivosCentrosAtencion.add(resultado.get(i)
							.getConsCentroAtencionContrato());
					codigosPromocion.add(resultado.get(i)
							.getCodigoPkPromocion());
					//total=total.add(resultado.get(i).getValorDescuentoProm());
					resultado.get(i).setTotalPorPromocion(total);
					resultado.get(i)
							.setListadoConsolidadoPorPromocion(listaDetalles);
						
				}
				
			}
			
			
		}
		
		//Quita registros repetidos de codigo promocion y los registros vacios de su lista por promocion
		ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> listadefinitiva = new ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion>();
		for(DtoConsolidadoPresupuestoContratadoPorPromocion retorno:resultado){
			if (!Utilidades
					.isEmpty(retorno.getListadoConsolidadoPorPromocion())) {
				listadefinitiva.add(retorno);
				
			}
		}
		
		return listadefinitiva;
		
	}
	
	/**
	 * metodo que se encarga de ordenar por promocion odontologica para
	 * el archivo plano
	 */

	public ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> ordenarResultadoConsultaPlano(
			DtoReportePresupuestosOdontologicosContratadosConPromocion dto) {
			    		
		
		ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> resultado = com.princetonsa.mundo.odontologia.PresupuestoOdontologico.obtenerPresupuestoOdoContratadoPromo(dto);
		ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> listaDetalles = new ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion>();
		if (!Utilidades.isEmpty(resultado)) {
			listaDetalles = new ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion>();
						
				for (int i = 0; i < resultado.size(); i++) {
					if (resultado.get(i).getFechaContrato().compareTo(resultado.get(i).getFechaInicialvige())>=0 
							&& resultado.get(i).getFechaContrato().compareTo(resultado.get(i).getFechaFinalvige())<=0) {
						
						listaDetalles.add(resultado.get(i));
					}
				}
				
		}
						
		return listaDetalles;
	}
	
	
	/**
	 * Método que se encarga de calcular las cantidades por número de superficies
	 * 
	 * @param listaProgramasServicios
	 * @param utilizaProgramas
	 * @param modificadas
	 */
	public void calcularCantidadesXNumeroSuperficies(ArrayList<DtoPresuOdoProgServ> listaProgramasServicios, boolean utilizaProgramas, boolean modificadas) 
	{
		for(DtoPresuOdoProgServ dto: listaProgramasServicios)
		{
			int cantidad = 0;

			if(modificadas){
				
				cantidad= dto.getCantidad()>0?dto.getCantidad():0;
			}
			
			if(dto.getTipoModificacion()==EnumTipoModificacion.NUEVO || dto.getTipoModificacion()==EnumTipoModificacion.MODIFICADO)
			{	
				if(modificadas){
					
					dto.setCantidad(dto.calcularCantidadModificada(utilizaProgramas)+cantidad);
					
				}else{
					
					dto.setCantidad(dto.calcularCantidad(utilizaProgramas));
				}
			}
		}
	}
	
	
	
	
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
	public void calcularTarifas(ArrayList<DtoPresuOdoProgServ> listaProgramasServicios, ArrayList<InfoConvenioContratoPresupuesto> listaConvenioPresupuesto, 
			String loginUsuario, int codigoInstitucion, int codigoCuentaPaciente, boolean utilizaProgramas) throws IPSException
	{

		for(DtoPresuOdoProgServ dtoPresuServPrograma: listaProgramasServicios)
		{
			if(dtoPresuServPrograma.getTipoModificacion()==EnumTipoModificacion.NUEVO
					 || dtoPresuServPrograma.getTipoModificacion()==EnumTipoModificacion.MODIFICADO)
			{
				ArrayList<DtoPresupuestoOdoConvenio> arrayConvenio= new ArrayList<DtoPresupuestoOdoConvenio>();
				//logger.info("numero de convenios-->"+forma.getListaConvenioPresupuesto().size());
				for(InfoConvenioContratoPresupuesto infoConvenio:  listaConvenioPresupuesto)
				{
					if(infoConvenio.getActivo())
					{
						
						DtoPresupuestoOdoConvenio detalleConvenioServPrograma = obtenerConvenioProgramaServicio(
								dtoPresuServPrograma, infoConvenio.getConvenio(), infoConvenio.getContrato(),
								loginUsuario, utilizaProgramas, codigoInstitucion,
								codigoCuentaPaciente);
					
						arrayConvenio.add(detalleConvenioServPrograma);
					}
				}
				//logger.info("seteamos las tarifas finales!!!!!!!!!!");
				dtoPresuServPrograma.setListPresupuestoOdoConvenio(arrayConvenio);
			}
		}
	}

	/**
	 * Método que calcula la tarifa por cada programa / servicio
	 * 
	 * @param dtoPresuServPrograma
	 * @param infoConvenio
	 * @param loginUsuario
	 * @param fechaCalculoTarifa
	 * @param utilizaProgramas
	 * @param codigoInstitucion
	 * @param codigoCuentaPaciente
	 * @return
	 */
	public DtoPresupuestoOdoConvenio obtenerConvenioProgramaServicio(
			DtoPresuOdoProgServ dtoPresuServPrograma, InfoDatosInt convenio, InfoDatosInt contrato, String loginUsuario, 
			boolean utilizaProgramas, int codigoInstitucion, int codigoCuentaPaciente) throws IPSException{
		
		String fechaCalculoTarifa=UtilidadFecha.getFechaActual();
		
		DtoPresupuestoOdoConvenio detalleConvenioServPrograma= new DtoPresupuestoOdoConvenio();
		InfoPresupuestoXConvenioProgramaServicio infoPresupuesto= 
			CargosOdon.obtenerPresupuestoXConvenio(dtoPresuServPrograma.getServicio().getCodigo(), 
					dtoPresuServPrograma.getPrograma().getCodigo().doubleValue(), 
					dtoPresuServPrograma.getCantidad(), 
					convenio.getCodigo(), 
					contrato.getCodigo(), 
					fechaCalculoTarifa, 
					codigoInstitucion,
					new BigDecimal(codigoCuentaPaciente));
		
		detalleConvenioServPrograma.setValorUnitario(infoPresupuesto.getValorUnitarioProgramaServicioConvenio());
		detalleConvenioServPrograma.setErrorCalculoTarifa(infoPresupuesto.getErroresTotalesStr("<br>"));
		detalleConvenioServPrograma.setAdvertenciaPromocion(infoPresupuesto.getDetallePromocionDescuento().getAdvertencia());
		detalleConvenioServPrograma.setDescuentoComercialUnitario(infoPresupuesto.getValorUnitarioDctoComercial());
		detalleConvenioServPrograma.setPorcentajeHonorarioPromocion(infoPresupuesto.getDetallePromocionDescuento().getPorcentajeHonorario());
		detalleConvenioServPrograma.setValorHonorarioPromocion(infoPresupuesto.getDetallePromocionDescuento().getValorHonorario());
		detalleConvenioServPrograma.setValorDescuentoPromocion(infoPresupuesto.getDetallePromocionDescuento().getValorPromocion());
		detalleConvenioServPrograma.setPorcentajePromocion(infoPresupuesto.getDetallePromocionDescuento().getPorcentajePromocion());
		detalleConvenioServPrograma.setSerialBono(infoPresupuesto.getDetalleBonoDescuento().getSerial());
		detalleConvenioServPrograma.setValorDescuentoBono(infoPresupuesto.getDetalleBonoDescuento().getValorDctoCALCULADO());
		detalleConvenioServPrograma.setPorcentajeDctoBono(infoPresupuesto.getDetalleBonoDescuento().getPorcentajeDescuento());
		detalleConvenioServPrograma.setAdvertenciaBono(infoPresupuesto.getDetalleBonoDescuento().getAdvertencia());
		detalleConvenioServPrograma.setConvenio(convenio);
		detalleConvenioServPrograma.setContrato(contrato);
		detalleConvenioServPrograma.setDetallePromocion(infoPresupuesto.getDetallePromocionDescuento().getDetPromocion());
		
		detalleConvenioServPrograma.setSeleccionadoPorcentajeBono(infoPresupuesto.getDetalleBonoDescuento().isSeleccionadoPorcentaje());
		detalleConvenioServPrograma.setSeleccionadoPorcentajePromocion(infoPresupuesto.getDetallePromocionDescuento().isSeleccionadoPorcentaje());
		
		//logger.info("sele prom-->"+detalleConvenioServPrograma.isSeleccionadoPorcentajePromocion());
		//logger.info("seteamos las tarifas!!!!!!!!!!");
		
		//si existe valor descuento x bono y x promocion entonces se debe postular la de mayor valor
		if(detalleConvenioServPrograma.getValorDescuentoBono().doubleValue()>0 && detalleConvenioServPrograma.getValorDescuentoPromocion().doubleValue()>0)
		{
			if(detalleConvenioServPrograma.getValorDescuentoBono().doubleValue()>detalleConvenioServPrograma.getValorDescuentoPromocion().doubleValue())
			{	
				detalleConvenioServPrograma.setSeleccionadoBono(true);
				detalleConvenioServPrograma.setSeleccionadoPromocion(false);
			}
			else
			{	
				detalleConvenioServPrograma.setSeleccionadoPromocion(true);
				detalleConvenioServPrograma.setSeleccionadoBono(false);
			}	
		}
		else
		{
			detalleConvenioServPrograma.setSeleccionadoBono(detalleConvenioServPrograma.getValorDescuentoBono().doubleValue()>0);
			detalleConvenioServPrograma.setSeleccionadoPromocion(detalleConvenioServPrograma.getValorDescuentoPromocion().doubleValue()>0);
		}
		
		//logger.info("sele prom1-->"+detalleConvenioServPrograma.isSeleccionadoPorcentajePromocion());
		//DEBEMOS SETEAR LOS DETALLES DE LOS SERVICIOS DEL PROGRAMA
		if(utilizaProgramas)
		{
			detalleConvenioServPrograma.setListaDetalleServiciosPrograma(cargarListaServiciosProgramaTarifas(infoPresupuesto.getDetalleTarifasServicio(), loginUsuario, dtoPresuServPrograma.getPrograma().getCodigo()));
		}
		return detalleConvenioServPrograma;
	}
	
	
	
	/**
	 * Método que carga el detalle de las tarifas para la lista de los Programa/servicios
	 * 
	 * @param detalleTarifasServicio
	 * @param loginUsuario
	 * @param programa
	 * @return
	 */
	public ArrayList<DtoPresupuestoDetalleServiciosProgramaDao> cargarListaServiciosProgramaTarifas(
			ArrayList<InfoTarifaServicioPresupuesto> detalleTarifasServicio, String loginUsuario , Double programa) 
	{
		ArrayList<DtoPresupuestoDetalleServiciosProgramaDao> array= new ArrayList<DtoPresupuestoDetalleServiciosProgramaDao>();
		
		for(InfoTarifaServicioPresupuesto info: detalleTarifasServicio)
		{
			DtoPresupuestoDetalleServiciosProgramaDao detalle= new DtoPresupuestoDetalleServiciosProgramaDao();
			detalle.setDctoComercialUnitario(info.getValorDescuentoComercial());
			detalle.setErrorCalculoTarifa(info.getError());
			detalle.setFHU(new DtoInfoFechaUsuario(loginUsuario));
			detalle.setPorcentajeDctoBonoServicio(info.getPorcentajeDecuentoBonoUnitario());
			detalle.setPorcentajeDctoPromocionServicio(info.getPorcentajeDescuentoPromocionUnitario());
			detalle.setPrograma(programa);
			detalle.setServicio(info.getServicio());
			detalle.setValorDctoBonoServicio(info.getValorDescuentoBonoUnitario());
			detalle.setValorDctoPromocionServicio(info.getValorDescuentoPromocionUnitario());
			detalle.setValorUnitarioServicio(info.getValorTarifaUnitaria());
			detalle.setValorHonorarioDctoPromocionServicio(info.getValorHonorarioPromocion());
			detalle.setPorcentajeHonorarioDctoPromocionServicio(info.getPorcentajeHonorarioPromocion());
			detalle.setEsquemaTarifario(info.getEsquemaTarifario());
			array.add(detalle);
		}
		
		return array;
	}
	
	
	/**
	 * Método que crea el encabezado para el presupuesto
	 * 
	 * @param usuario
	 * @param paciente
	 * @param codigoPlanTratamiento
	 * @return
	 */
	public DtoPresupuestoOdontologico crearEncabezadoPresupuesto (UsuarioBasico usuario, PersonaBasica paciente, BigDecimal codigoPlanTratamiento)
	{
		
		DtoPresupuestoOdontologico dtoPresupuesto= new DtoPresupuestoOdontologico();
		dtoPresupuesto.setCentroAtencion(new InfoDatosInt(usuario.getCodigoCentroAtencion(), usuario.getCentroAtencion()));
		dtoPresupuesto.setCodigoPaciente(new InfoDatosInt(paciente.getCodigoPersona(), paciente.getNombrePersona()));
		dtoPresupuesto.setConsecutivo(new BigDecimal(0));
		dtoPresupuesto.setCuenta(new BigDecimal(paciente.getCodigoCuenta()));

		if(usuario.getEspecialidades().length>0){
			dtoPresupuesto.setEspecialidad(new InfoDatosInt(usuario.getEspecialidades()[0].getCodigo()));
		}
		
		dtoPresupuesto.setEstado(ConstantesIntegridadDominio.acronimoEstadoActivo);
		dtoPresupuesto.setFechaUsuarioGenera( new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		dtoPresupuesto.setIngreso(new BigDecimal(paciente.getCodigoIngreso()));
		dtoPresupuesto.setInstitucion(usuario.getCodigoInstitucionInt());
		dtoPresupuesto.setPlanTratamiento(codigoPlanTratamiento);
		dtoPresupuesto.setUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		 
		return  dtoPresupuesto;
		 
	}
	

	
	/**
	 * Método que se encarga de calcular los valores Totales X Convenio.
	 * 
	 * @param listaProgramasServicios
	 * @param listaConvenioPresupuesto
	 * @return
	 */
	public ArrayList<DtoPresupuestoTotalConvenio> crearValoresTotalesXConvenio (ArrayList<DtoPresuOdoProgServ> listaProgramasServicios, ArrayList<InfoConvenioContratoPresupuesto> listaConvenioPresupuesto) throws IPSException
	{
		//ACTUALIZACION DE LOS VALORES TOTALES X CONVENIO
		 ArrayList<DtoPresupuestoTotalConvenio> arrayTotales= new ArrayList<DtoPresupuestoTotalConvenio>();
		 
		 for(InfoConvenioContratoPresupuesto infoConvenio: listaConvenioPresupuesto)
		 {
			 if(infoConvenio.getActivo())
			 {
				 DtoPresupuestoTotalConvenio dtoTotalesXConvenio= new DtoPresupuestoTotalConvenio();
				
				 BigDecimal valorSubTotal= new BigDecimal(0);
				 BigDecimal valorSubTotalSinInclusiones= new BigDecimal(0);
				 for(DtoPresuOdoProgServ dtop : listaProgramasServicios)
				 {
					 for(DtoPresupuestoOdoConvenio detalleConvenioServPrograma: dtop.getListPresupuestoOdoConvenio())
					 {
						 if(infoConvenio.getConvenio().getCodigo()==detalleConvenioServPrograma.getConvenio().getCodigo())
						 {
							 //logger.info("val unitario externo-->"+detalleConvenioServPrograma.getValorUnitario()+ "  cantidad-->"+ dtop.getCantidad());
							 if(detalleConvenioServPrograma.getValorTotalConvenioXProgServ(dtop.getCantidad()).doubleValue()>0)
							 {
								 //logger.info(" antes entra!!! valorSubTotal-->"+valorSubTotal+ "    valor unitario a sumar-->"+detalleConvenioServPrograma.getValorUnitario()+" *  cantidad ->"+dtop.getCantidad());
								 valorSubTotal= valorSubTotal.add(detalleConvenioServPrograma.getValorTotalConvenioXProgServ(dtop.getCantidad()));
								 //logger.info(" despues entra!!! valorSubTotal-->"+valorSubTotal);
								 if(dtop.getInclusion()!=null && dtop.getInclusion().intValue()<=0)
								 {
									 valorSubTotalSinInclusiones=valorSubTotalSinInclusiones.add(detalleConvenioServPrograma.getValorTotalConvenioXProgServ(dtop.getCantidad()));
								 }
							 }
						 }
					 }
				 }
				 
				 dtoTotalesXConvenio.setSubTotalContratadoSinDescuentos(valorSubTotalSinInclusiones);
				 dtoTotalesXConvenio.setValorSubTotalSinContratar(valorSubTotal);
				 dtoTotalesXConvenio.setValorSubTotalContratado(new BigDecimal(0));
				 dtoTotalesXConvenio.setConvenio(infoConvenio.getConvenio());
				 dtoTotalesXConvenio.setContrato(infoConvenio.getContrato().getCodigo());
				 dtoTotalesXConvenio.setPresupuesto(new BigDecimal(0));
				 
				 arrayTotales.add(dtoTotalesXConvenio);
			 }
		 }
		 
		 return arrayTotales;
	}
	
	
	/**
	 * Método que se encarga de reunir los convenios
	 * disponibles
	 * 
	 * @param listaPresupuestos
	 * @return
	 */
	public ArrayList<DtoPresupuestoTotalConvenio> obtenerListadoConvenios (ArrayList<DtoPresupuestoOdontologico> listaPresupuestos){
		
		ArrayList<DtoPresupuestoTotalConvenio> listaTemporal = new ArrayList<DtoPresupuestoTotalConvenio>();
		
		TreeSet<Integer> treeTemp= new TreeSet<Integer>();
		
		for(DtoPresupuestoOdontologico dtop: listaPresupuestos)
		{
			for(DtoPresupuestoTotalConvenio tarifa: dtop.getListaTarifas())
			{
			
				if(!treeTemp.contains(tarifa.getConvenio().getCodigo()))
				{	
					treeTemp.add(tarifa.getConvenio().getCodigo());
					
					DtoPresupuestoTotalConvenio dto= new DtoPresupuestoTotalConvenio();
					dto.setConvenio(tarifa.getConvenio());
					dto.setContrato(tarifa.getContrato());
					dto.setPresupuesto(tarifa.getPresupuesto());
					dto.setValorSubTotalSinContratar(tarifa.getValorSubTotalSinContratar());
					dto.setValorSubTotalContratado(tarifa.getValorSubTotalContratado());
					listaTemporal.add(dto);
				}
			}
		}
		
		treeTemp=null;
		
		return listaTemporal;
	}
	
	
	/**
	 * Método que carga los convenios para la contratación
	 * del presupuesto
	 * 
	 * @param listaSumatoriaConvenios
	 * @param codigoPersona
	 * @return
	 */
	 public ArrayList<InfoConvenioContratoPresupuesto> cargarConveniosContratoPresupuesto(ArrayList<DtoPresupuestoTotalConvenio> listaSumatoriaConvenios, int codigoPersona)
	 {
		 ArrayList<InfoConvenioContratoPresupuesto> listaConvenioPresupuesto = new ArrayList<InfoConvenioContratoPresupuesto>();
		 
		 ////primero cargamos los contratos - convenios que provienen de los parametros generales
		// ArrayList<InfoConvenioContratoPresupuesto> arrayParametros= new ArrayList<InfoConvenioContratoPresupuesto>();
		 ArrayList<HashMap<String, Object>> array= ValoresPorDefecto.getConveniosAMostrarPresupuestoOdo();
		 
		 for( HashMap<String, Object> mapa: array)
		 {
			 InfoConvenioContratoPresupuesto info= new InfoConvenioContratoPresupuesto();
			 info.setActivo(true);
			 info.setContrato(new InfoDatosInt(Utilidades.convertirAEntero(mapa.get("codigoContrato")+""), mapa.get("numeroContrato")+""));
			 info.setConvenio(new InfoDatosInt(Utilidades.convertirAEntero(mapa.get("codigoConvenio")+""), mapa.get("descripcionConvenio")+""));
			 info.setAjusteServicio(mapa.get("ajusteServicio")+"");
			 
			 info.setEsParametroGeneral(true);
			 listaConvenioPresupuesto.add(info);
		 }
		 
		 ////cargamos los contratos - convenios que vienen del paciente desde la subcuenta
		 /*
		  * Cargar los convenios de la tabla convenios ingreso paciente
		  */
		
		 List<ConveniosIngresoPaciente> lisConvenoPacienteTMP=  com.princetonsa.mundo.odontologia.PresupuestoOdontologico.cargarIngresoPaciente(codigoPersona);
		 ArrayList<InfoConvenioContratoPresupuesto> arrayPaciente= PresupuestoHelperVista.transformarListConvenios(lisConvenoPacienteTMP);
		 
		// ArrayList<InfoConvenioContratoPresupuesto> arrayPaciente= ValidacionesPresupuesto.cargarConveniosContratosPacientePresupuesto(paciente.getCodigoIngreso());
		 
		 for( InfoConvenioContratoPresupuesto infoPaciente: arrayPaciente)
		 {
			 boolean puedoAdicionar=true;
			 
			 for(InfoConvenioContratoPresupuesto infoParametros: listaConvenioPresupuesto)
			 {
				 if(infoParametros.getConvenio().getCodigo()==infoPaciente.getConvenio().getCodigo() 
						 && infoParametros.getContrato().getCodigo()==infoPaciente.getContrato().getCodigo())
				 {
					 //logger.info("no se puede adicionar porque ya esta en los parametros !!!!!!!!!!!");
					 puedoAdicionar=false;
				 }
			 }
			 
			 if(puedoAdicionar)
			 {
				 listaConvenioPresupuesto.add(infoPaciente);
			 }
		 }
		 
		 /*
		  *Validar si existe convenio en ingresos 
		  */
		 for( InfoConvenioContratoPresupuesto infoConvenioContrato : arrayPaciente)
		 {
			 for ( DtoPresupuestoTotalConvenio dtoPresupuesto:  listaSumatoriaConvenios)
			 {
				 if(dtoPresupuesto.getConvenio().getCodigo()==infoConvenioContrato.getConvenio().getCodigo() 
						 && dtoPresupuesto.getContrato()==infoConvenioContrato.getContrato().getCodigo())
				 {
					dtoPresupuesto.setExisteConvenioEnIngreso(Boolean.FALSE); 
				 }
			 }
		 }
		 

		 /*
		  *Adiccionar  
		  */
		 
		 for(InfoConvenioContratoPresupuesto infoPaciente: arrayPaciente)
		 {
			 for (InfoConvenioContratoPresupuesto infoContrato: listaConvenioPresupuesto){
				 
				 if(infoContrato.getConvenio().getCodigo()==infoPaciente.getConvenio().getCodigo() 
						 && infoContrato.getContrato().getCodigo()==infoPaciente.getContrato().getCodigo())
				 {
					 
					 if(infoPaciente.getActivo())
					 {
						 infoContrato.setActivo(Boolean.TRUE);
					 }
				 }
			 }
		 }
		 
		 return listaConvenioPresupuesto;
	 }

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.presupuesto.IPresupuestoOdontologicoMundo#guardarOtroSi(com.princetonsa.dto.odontologia.DtoOtroSi)
	 */
	@Override
	public OtrosSi guardarOtroSi(DtoOtroSi dtoOtroSi) {
		
		IOtrosSiMundo otrosSiMundo = ContratoFabricaMundo.crearOtrosSiMundo();
		return otrosSiMundo.guardarOtroSi(dtoOtroSi);
	}
}
