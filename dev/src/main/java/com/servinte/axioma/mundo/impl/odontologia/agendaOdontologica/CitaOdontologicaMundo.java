package com.servinte.axioma.mundo.impl.odontologia.agendaOdontologica;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dto.odontologia.DtoFiltroReporteCambioServCitaOdonto;
import com.princetonsa.dto.odontologia.DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas;
import com.princetonsa.dto.odontologia.DtoIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoReporteIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaCambioServiciosOdontologicos;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto;
import com.servinte.axioma.dao.impl.odontologia.agendaOdontologica.CitaOdontologicaHibernateDAO;
import com.servinte.axioma.dao.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaDAO;
import com.servinte.axioma.mundo.fabrica.odontologia.agendaOdontologica.AgendaOdontologicaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaMundo;
import com.servinte.axioma.orm.CitasAsociadasAProgramada;

public class CitaOdontologicaMundo implements ICitaOdontologicaMundo {

	private ICitaOdontologicaDAO dao;

	/**
	 * M&eacute;todo constructor de la clase
	 * 
	 * @author Yennifer Guerrero
	 */
	public CitaOdontologicaMundo() {
		dao = new CitaOdontologicaHibernateDAO();
	}

	@Override
	public ArrayList<DtoIngresosOdontologicos> obtenerCitaOdontoSinValIni(
			List<Integer> ingresos, List<Integer> codigoPaciente) {
		return dao.obtenerCitaOdontoSinValIni(ingresos, codigoPaciente);
	}

	@Override
	public ArrayList<DtoIngresosOdontologicos> obtenercitaOdontoValIniNoAtendida(
			List<Integer> ingresos ) {
		return dao.obtenercitaOdontoValIniNoAtendida(ingresos);
	}

	/**
	 * Este m&eacute;todo se encarga de consultar las citas odontol&oacute;gicas
	 * que tienen una valoraci&oacute;n inicial en estado atendida.
	 * 
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	public List<Integer> obtenercitaOdontoValIniAtendida(List<Integer> ingresos) {
		return dao.obtenercitaOdontoValIniAtendida(ingresos);
	}

	@Override
	public List<Long> obtenerCodigoCitasProgramadasPacienteXTipoCita(
			int codigoPaciente, String tipoCita) {

		return dao.obtenerCodigoCitasProgramadasPacienteXTipoCita(
				codigoPaciente, tipoCita);
	}

	public ArrayList<DtoIngresosOdontologicos> obtenerCitaOdontoDiferenteDeValIni(
			List<Integer> ingresos, List<Integer> pacientesConValIni) {
		return dao.obtenerCitaOdontoDiferenteDeValIni(ingresos, pacientesConValIni);
	}

	@Override
	public boolean guardarCitaAsociadasProgramada(
			CitasAsociadasAProgramada transientInstance) {
		return dao.guardarCitaAsociadasProgramada(transientInstance);
	}

	/**
	 * Este m&eacute;todo se encarga de consultar las solicitudes de cambio de
	 * servicios de citas dependiendo de los parametros que se envian en el
	 * filtro
	 * 
	 * @return ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>
	 * 
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> consultarCambioServCitasOdonto(
			DtoFiltroReporteCambioServCitaOdonto filtroCambioServicio) {
		return dao.consultarCambioServCitasOdonto(filtroCambioServicio);
	}
	
	
	/**
	 * Este método se encarga de consultar los tiempos de espera de atención de citas odontológicas
	 * @param filtroTiempoEspera
	 * @return ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto>
	 * 
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> consultarTiempoEsperaAtencionCitasOdonto (DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas filtroTiempoEspera){
		return dao.consultarTiempoEsperaAtencionCitasOdonto(filtroTiempoEspera);
	}
	

	/**
	 * Este m&eacute;todo se encarga de consultar las solicitudes de cambio de
	 * servicio y ordenarlas por el codigo del profesional y por codigo de la
	 * cita para cargar los servicios iniciales y finales y sus valores
	 * 
	 * @return ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>
	 * 
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> consolidarInfoReporteCitas(
			DtoFiltroReporteCambioServCitaOdonto filtroCambioServicio) {

		ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> listaDetalles = new ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>();
		ICitaOdontologicaMundo mundo = AgendaOdontologicaFabricaMundo
				.crearCitaOdontologicaMundo();
		ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> resultadoCambioServicios = mundo
				.consultarCambioServCitasOdonto(filtroCambioServicio);
			
		ArrayList<Integer> codigosProfesional = new ArrayList<Integer>();
		// Ordenar por Profesional de la Salud
		if (!Utilidades.isEmpty(resultadoCambioServicios)) {

			for (int i = 0; i < resultadoCambioServicios.size(); i++) {
				listaDetalles = new ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>();

				//if (i == 0) {
					//listaDetalles.add(resultadoCambioServicios.get(0));
				//}
				if (!codigosProfesional.contains(resultadoCambioServicios
						.get(i).getCodigoProfesional())) {
					for (int j = i ; j < resultadoCambioServicios.size(); j++) {
						if (resultadoCambioServicios.get(i)
								.getCodigoProfesional() == resultadoCambioServicios.get(j).getCodigoProfesional()) {
							listaDetalles.add(resultadoCambioServicios.get(j));
						}
					}

					codigosProfesional.add(resultadoCambioServicios.get(i)
							.getCodigoProfesional());
					resultadoCambioServicios.get(i)
							.setListaCambioServicioProfesional(listaDetalles);
				}

			}

		}

		
		// QUITAR REGISTROS REPETIDOS DE CODIGO PROFESIONAL Y QUE SU LISTA DE
		// CAMBIOSERVICIO ESTE VACIA
		ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> listaDefinitiva = new ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>();

		for (DtoResultadoConsultaCambioServiciosOdontologicos retorno : resultadoCambioServicios) {
			if (!Utilidades
					.isEmpty(retorno.getListaCambioServicioProfesional())) {
				listaDefinitiva.add(retorno);
			}
		}

		// ORDENAR POR CITA ODONTOLOGICA PARA SERVICIO INICIAL FINAL Y VALORES
		// INICIAL Y FINAL
		ArrayList<Long> codigosCita = new ArrayList<Long>();
		ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> listaResultado = new ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>();

		if (!Utilidades.isEmpty(listaDefinitiva)) {

			for (int i = 0; i < listaDefinitiva.size(); i++) {
				ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> listaInterior = listaDefinitiva
						.get(i).getListaCambioServicioProfesional();
				listaResultado = new ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>();
				for (int j = 0; j < listaInterior.size(); j++) {
					if (!codigosCita.contains(listaInterior.get(j)
							.getCodigoPkCita())) {

						for (int k = listaInterior.size() - 1; k >= j; k--) {
							if (listaInterior.get(j).getCodigoPkCita() == listaInterior.get(k).getCodigoPkCita()) {
								listaInterior.get(k).setServicioInicial(listaInterior.get(j).getServicioInicial());
								listaInterior.get(k).setValorInicial(listaInterior.get(j).getValorInicial());
								listaInterior.get(k).setValorInicialCita(listaInterior.get(j).getValorInicialCita());
								listaResultado.add(listaInterior.get(k));
								break;
							}

						}
						codigosCita.add(listaInterior.get(j).getCodigoPkCita());

					}
				}
				
				listaDefinitiva.get(i).setListaCambioServicioProfesional(
						listaResultado);
				
				
			}

		}

		
		DtoResultadoConsultaCambioServiciosOdontologicos dto = new DtoResultadoConsultaCambioServiciosOdontologicos();

		// ORDENAR POR ULTIMA FECHA DE SOLICITUD
		if (!Utilidades.isEmpty(listaDefinitiva)) {

			for (int i = 0; i < listaDefinitiva.size(); i++) {
				ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> listaInterior = listaDefinitiva
						.get(i).getListaCambioServicioProfesional();
				for (int j = 0; j < listaInterior.size(); j++) {
					Date fechaj = UtilidadFecha
							.conversionFormatoFechaStringDate(listaInterior
									.get(j).getFechaSol());
					for (int k = j + 1; k < listaInterior.size(); k++) {
						Date fechak = UtilidadFecha
								.conversionFormatoFechaStringDate(listaInterior
										.get(k).getFechaSol());
						if (fechaj.compareTo(fechak) == 1) {

							dto = listaInterior.get(j);
							listaInterior.set(j, listaInterior.get(k));
							listaInterior.set(k, dto);

						}
					}
				}

			}

		}
		
		
		return listaDefinitiva;

	}
	
	
	/**
	 * Este método se encarga de consultar las citas odontologicas en el sistema
	 * y ordenar para generar el reporte de tiempos espera de atencion de citas odontológicas
	 * 
	 * @return ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto>
	 * 
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> consolidarInfoReporteTiemposEspera(
			DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas filtroTiempoEspera) {

		ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> listaDetalles = new ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto>();
		ICitaOdontologicaMundo mundo = AgendaOdontologicaFabricaMundo
				.crearCitaOdontologicaMundo();
		ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> resultadoTiempoEspera = mundo.consultarTiempoEsperaAtencionCitasOdonto(filtroTiempoEspera);
		
			
		ArrayList<Integer> consecutivosCentrosAtencion = new ArrayList<Integer>();
		ArrayList<Integer> codigosUnidadAgenda = new ArrayList<Integer>();
		
		
		// Ordenar por unidad agenda
		if (!Utilidades.isEmpty(resultadoTiempoEspera)) {

			for (int i = 0; i < resultadoTiempoEspera.size(); i++) {
				listaDetalles = new ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto>();

				if (!consecutivosCentrosAtencion.contains(resultadoTiempoEspera
						.get(i).getConsecutivoCentroAtencion())){
					codigosUnidadAgenda = new ArrayList<Integer>();
					
				}
				if(!codigosUnidadAgenda.contains(resultadoTiempoEspera
							.get(i).getCodigoUnidadAgenda())) {
						for (int j = i ; j < resultadoTiempoEspera.size(); j++) {
							if (resultadoTiempoEspera.get(i)
									.getConsecutivoCentroAtencion() == resultadoTiempoEspera.get(j).getConsecutivoCentroAtencion()
								&& resultadoTiempoEspera.get(i).getCodigoUnidadAgenda() == resultadoTiempoEspera.get(j).getCodigoUnidadAgenda()	) {
								listaDetalles.add(resultadoTiempoEspera.get(j));
							}
						}
						consecutivosCentrosAtencion.add(resultadoTiempoEspera.get(i)
								.getConsecutivoCentroAtencion());
						codigosUnidadAgenda.add(resultadoTiempoEspera.get(i)
								.getCodigoUnidadAgenda());
						resultadoTiempoEspera.get(i)
								.setListaTiempoEsperaEspecialidad(listaDetalles);
				}
					
			}
		}
		
		
		// QUITAR REGISTROS REPETIDOS DE UNIDAD AGENDA Y QUE SU LISTA
		// ESTE VACIA
		ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> listaDefinitiva = new ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto>();

		for (DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto retorno : resultadoTiempoEspera) {
			if (!Utilidades
					.isEmpty(retorno.getListaTiempoEsperaEspecialidad())) {
				listaDefinitiva.add(retorno);
			}
		}
		
		
		//TOMAR SOLO LA ULTIMA CITA ASIGNADA Y GUARDARLA
		ArrayList<Long> codigosCita = new ArrayList<Long>();
		
		if (!Utilidades.isEmpty(listaDefinitiva)) {

			for (int i = 0; i < listaDefinitiva.size(); i++) {
				listaDetalles = new ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto>();
					for (int j = listaDefinitiva.get(i).getListaTiempoEsperaEspecialidad().size()-1 ; j >= 0 ; j--) {
							if (!codigosCita.contains(listaDefinitiva
										.get(i).getListaTiempoEsperaEspecialidad().get(j).getCodigoPkCita())){
											listaDetalles.add(listaDefinitiva
													.get(i).getListaTiempoEsperaEspecialidad().get(j));
							}
							codigosCita.add(listaDefinitiva.get(i).
									getListaTiempoEsperaEspecialidad().get(j).getCodigoPkCita());
						}
						
						listaDefinitiva.get(i)
								.setListaTiempoEsperaEspecialidad(listaDetalles);
			}		
			
		}
		
		
		if (!Utilidades.isEmpty(listaDefinitiva)) {
		
			for (int i = 0; i < listaDefinitiva.size(); i++) {
				listaDetalles = new ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto>();
					for (int j = listaDefinitiva.get(i).getListaTiempoEsperaEspecialidad().size()-1 ; j >= 0 ; j--) {
							listaDetalles.add(listaDefinitiva
									.get(i).getListaTiempoEsperaEspecialidad().get(j));
						}
					listaDefinitiva.get(i).setListaTiempoEsperaEspecialidad(listaDetalles);
			}		
			
		}
		
		
		
		return listaDefinitiva;

	}
	
	/**
	 * Este método se encarga de consultar las solicitudes de cambio de
	 * servicio y ordenarlas por codigo de la cita para cargar los servicios 
	 * iniciales y finales y sus valores para generar el reporte en archivo plano
	 * 
	 * @return ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>
	 * 
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> consolidarInfoReporteCitasPlano(
			DtoFiltroReporteCambioServCitaOdonto filtroCambioServicio) {

		ICitaOdontologicaMundo mundo = AgendaOdontologicaFabricaMundo
				.crearCitaOdontologicaMundo();
		ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> resultadoCambioServicios = mundo
				.consultarCambioServCitasOdonto(filtroCambioServicio);
	
		// ORDENAR POR CITA ODONTOLOGICA PARA SERVICIO INICIAL FINAL Y VALORES
		// INICIAL Y FINAL
		ArrayList<Long> codigosCita = new ArrayList<Long>();
		ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> listaResultado = new ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>();
		DtoResultadoConsultaCambioServiciosOdontologicos dtoOrdena = new DtoResultadoConsultaCambioServiciosOdontologicos();
		
		if (!Utilidades.isEmpty(resultadoCambioServicios)) {

				for (int i = 0; i < resultadoCambioServicios.size(); i++) {
					if (!codigosCita.contains(resultadoCambioServicios.get(i)
							.getCodigoPkCita())) {
						Date fechai = UtilidadFecha
						.conversionFormatoFechaStringDate(resultadoCambioServicios
								.get(i).getFechaSol());
						for (int j = i+1; j <resultadoCambioServicios.size(); j++) {
							Date fechaj = UtilidadFecha
							.conversionFormatoFechaStringDate(resultadoCambioServicios
									.get(j).getFechaSol());
							if (resultadoCambioServicios.get(i).getCodigoPkCita() == resultadoCambioServicios.get(j).getCodigoPkCita()
									&& fechai.compareTo(fechaj) == -1) {
								resultadoCambioServicios.get(j).setServicioInicial(resultadoCambioServicios.get(i).getServicioInicial());
								resultadoCambioServicios.get(j).setValorInicial(resultadoCambioServicios.get(i).getValorInicial());
								resultadoCambioServicios.get(j).setValorInicialCita(resultadoCambioServicios.get(i).getValorInicialCita());
								dtoOrdena = resultadoCambioServicios.get(i);
								resultadoCambioServicios.set(i, resultadoCambioServicios.get(j));
								resultadoCambioServicios.set(j, dtoOrdena);
								
							}

						}
						codigosCita.add(resultadoCambioServicios.get(i).getCodigoPkCita());
						listaResultado.add(resultadoCambioServicios.get(i));
					}
				}
		}
	
		ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> listaDefinitiva = new ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>();
		DtoResultadoConsultaCambioServiciosOdontologicos dto=null;
		String [] codigoServicioInicial=null;
		String [] codigoServicioFinal=null;
		//AGREGA REGISTROS PARA LOS QUE TENGAN MAS DE UN SERVICIO INICIAL O FINAL
		if (!Utilidades.isEmpty(listaResultado)) {
			
			for (int i = 0; i < listaResultado.size(); i++) {
				
				codigoServicioInicial = listaResultado.get(i).getServicioInicial().split("\n");
				codigoServicioFinal = listaResultado.get(i).getServicioFinal().split("\n");
				
				if(codigoServicioInicial!=null&&codigoServicioInicial.length>0&&codigoServicioFinal!=null&&codigoServicioFinal.length>0){
					
					if(codigoServicioFinal.length>=codigoServicioInicial.length){
						for(int j=0;j<codigoServicioFinal.length;j++){
							if(j==0){
								listaResultado.get(i).setServicioInicial(codigoServicioInicial[0]);
								listaResultado.get(i).setServicioFinal(codigoServicioFinal[0]);
								listaDefinitiva.add(listaResultado.get(i));
							}else{
								dto=new DtoResultadoConsultaCambioServiciosOdontologicos();
								dto.setServicioFinal(codigoServicioFinal[j]);
								if(codigoServicioInicial.length>j)
									dto.setServicioInicial(codigoServicioInicial[j]);
								listaDefinitiva.add(dto);
							}
						}
					}else{
						for(int j=0;j<codigoServicioInicial.length;j++){
							if(j==0){
								listaResultado.get(i).setServicioInicial(codigoServicioInicial[0]);
								listaResultado.get(i).setServicioFinal(codigoServicioFinal[0]);
								listaDefinitiva.add(listaResultado.get(i));
							}else{
								dto=new DtoResultadoConsultaCambioServiciosOdontologicos();
								dto.setServicioInicial(codigoServicioInicial[j]);
								if(codigoServicioFinal.length>j)
									dto.setServicioFinal(codigoServicioFinal[j]);
								listaDefinitiva.add(dto);
							}
						}
					}
					
				}else{
					listaDefinitiva.add(listaResultado.get(i));
				}
				
			}
			
			
		}
		
		return listaDefinitiva;

	}
	
	/**
	 * Este método se encarga de consultar las citas odontologicas en el sistema
	 * y ordenar para generar el reporte plano de tiempos espera de atencion de citas odontológicas
	 * 
	 * @return ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>
	 * 
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> consolidarInfoReporteTiemposEsperaPlano(
			DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas filtroTiempoEspera) {

		ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> listaDetalles = new ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto>();
		ICitaOdontologicaMundo mundo = AgendaOdontologicaFabricaMundo
				.crearCitaOdontologicaMundo();
		ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> resultadoTiempoEspera = mundo.consultarTiempoEsperaAtencionCitasOdonto(filtroTiempoEspera);
		
		
		
		//TOMAR SOLO LA ULTIMA CITA ASIGNADA Y GUARDARLA
		ArrayList<Long> codigosCita = new ArrayList<Long>();
		
		if (!Utilidades.isEmpty(resultadoTiempoEspera)) {
			listaDetalles = new ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto>();
			for (int i = resultadoTiempoEspera.size()-1; i >= 0 ; i--) {
							if (!codigosCita.contains(resultadoTiempoEspera
										.get(i).getCodigoPkCita())){
											listaDetalles.add(resultadoTiempoEspera.get(i));
							}
							codigosCita.add(resultadoTiempoEspera.get(i).getCodigoPkCita());
			}
		}
		
		ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> listaDefinitiva = new ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto>();
		if (!Utilidades.isEmpty(listaDetalles)) {
			listaDefinitiva = new ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto>();
				for (int j = listaDetalles.size()-1 ; j >= 0 ; j--) {
					listaDefinitiva.add(listaDetalles.get(j));
					}
					
		}
		
		
		
		return listaDefinitiva;

	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaMundo#eliminarCitaProgramada(long)
	 */
	@Override
	public boolean eliminarCitaProgramada(long codigoCita) {
		
		return dao.eliminarCitaProgramada(codigoCita);
	}
	
	@Override
	public ArrayList<DtoIngresosOdontologicos> obtenerEstadoCitasPorPaciente(List<Integer> ingresos, DtoReporteIngresosOdontologicos filtro){
		return dao.obtenerEstadoCitasPorPaciente(ingresos, filtro);
	}
	
	@Override
	public ArrayList<DtoIngresosOdontologicos> obtenerInfoPacientesSinValIni(List<Integer> pacientes){
		return dao.obtenerInfoPacientesSinValIni(pacientes);
	}
	
	@Override
	public List<Long> obtenerCodigoCitasOdontoAtendidas(List<Integer> pacientesConValIni){
		return dao.obtenerCodigoCitasOdontoAtendidas(pacientesConValIni);
	}
	
	@Override
	public boolean actualizarIndicativoCambioEstadoCita(long codigoCita){
		return dao.actualizarIndicativoCambioEstadoCita(codigoCita);
	}
}
