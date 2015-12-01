package com.servinte.axioma.mundo.interfaz.odontologia.agendaOdontologica;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.odontologia.DtoFiltroReporteCambioServCitaOdonto;
import com.princetonsa.dto.odontologia.DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas;
import com.princetonsa.dto.odontologia.DtoIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoReporteIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaCambioServiciosOdontologicos;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto;
import com.servinte.axioma.orm.CitasAsociadasAProgramada;

public interface ICitaOdontologicaMundo {
	
	/**
	 * Este m&eacute;todo se encarga de consultar las citas odontol&oacute;gicas
	 * que no tienen una valoraci&oacute;n inicial.
	 * @param ingresos
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 * @param codigoPaciente 
	 */
	public ArrayList<DtoIngresosOdontologicos> obtenerCitaOdontoSinValIni(List<Integer> ingresos, List<Integer> codigoPaciente);
	
	/**
	 * Este m&eacute;todo se encarga de consultar las citas odontol&oacute;gicas
	 * que tienen una valoraci&oacute;n inicial pero que su estado es diferente 
	 * a atendida.
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DtoIngresosOdontologicos> obtenercitaOdontoValIniNoAtendida(List<Integer> ingresos);
	
	/**
	 * Este m&eacute;todo se encarga de consultar las citas odontol&oacute;gicas
	 * que tienen una valoraci&oacute;n inicial en estado atendida.
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	public List<Integer> obtenercitaOdontoValIniAtendida(List<Integer> ingresos);

	
	/**
	 * M&eacute;todo que devuelve un listado con los c&oacute;digos de las citas de un paciente
	 * en estado programada y de un tipo de cita espec&iacute;fico. 
	 * 
	 * @param codigoPaciente
	 * @param tipoCita
	 * @return
	 */
	public List<Long> obtenerCodigoCitasProgramadasPacienteXTipoCita (int codigoPaciente, String tipoCita);
	
	/**
	 * Este m&eacute;todo se encarga de consultar las citas odontol&oacute;gicas
	 * cuyo tipo es diferente a valoracion inicial.
	 * @param ingresos
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 * @param pacientesConValIni 
	 */
	public ArrayList<DtoIngresosOdontologicos> obtenerCitaOdontoDiferenteDeValIni(List<Integer> ingresos, List<Integer> pacientesConValIni);
	
	
	
	/**
	 * Guarda la entidad enviada
	 * @param CitasAsociadasAProgramada
	 * @return boolean
	 * @author Cristhian Murillo
	 */
	public boolean guardarCitaAsociadasProgramada(CitasAsociadasAProgramada transientInstance);
	
	/**
	 * Este m&eacute;todo se encarga de consultar las solicitudes de cambio de servicio.
	 * @param filtroCambioServicio
	 * @return ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>
	 * 
	 * @author Fabian Becerra
	 * @author Wilson Gomez
	 * @author Javier Gonzalez
	 */
	public ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> consultarCambioServCitasOdonto(DtoFiltroReporteCambioServCitaOdonto filtroCambioServicio);
	
	/**
	 * Este m&eacute;todo se encarga de ordenar las solicitudes de cambio
	 * de servicio por instituciones y citas odontologicas para no repetir
	 * datos en el reporte
	 * @param filtroCambioServicio
	 * @return ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>
	 * 
	 * @author Fabian Becerra
	 * @author Wilson Gomez
	 * @author Javier Gonzalez
	 */
	public ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> consolidarInfoReporteCitas (DtoFiltroReporteCambioServCitaOdonto filtroCambioServicio);
	
	/**
	 * Este m&eacute;todo se encarga de ordenar las solicitudes de cambio
	 * de servicio para el reporte generado en archivo plano
	 * @param filtroCambioServicio
	 * @return ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>
	 * 
	 * @author Fabian Becerra
	 * @author Wilson Gomez
	 * @author Javier Gonzalez
	 */
	public ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> consolidarInfoReporteCitasPlano (DtoFiltroReporteCambioServCitaOdonto filtroCambioServicio);
	
	/**
	 * Este m�todo se encarga de consultar los tiempos de espera de atenci�n de citas odontol�gicas
	 * @param filtroTiempoEspera
	 * @return ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto>
	 * 
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> consultarTiempoEsperaAtencionCitasOdonto (DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas filtroTiempoEspera);
	
	/**
	 * Este m�todo se encarga de consultar los tiempos de espera de atenci�n de citas odontol�gicas y ordenarlos por unidad agenda
	 * @param filtroTiempoEspera
	 * @return ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto>
	 * 
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> consolidarInfoReporteTiemposEspera (DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas filtroTiempoEspera);
	
	/**
	 * Este m�todo se encarga de consultar las citas odontologicas en el sistema
	 * y ordenar para generar el reporte plano de tiempos espera de atencion de citas odontol�gicas
	 * @param filtroTiempoEspera
	 * @return ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> consolidarInfoReporteTiemposEsperaPlano(
			DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas filtroTiempoEspera) ;
	
	
	/**
	 * M�todo que se encarga de eliminar el registro de la cita programada
	 * y sus servicios asociados al c�digo de cita
	 * 
	 * @param codigoCita
	 * @return
	 */
	public boolean eliminarCitaProgramada (long codigoCita);
	
	/**
	 * Este m�todo se encarga de obtener un listado con el n�mero de citas por estado y la 
	 * fecha m�s reciente de la cita, para los pacientes cuyo ingreso se encuentra dentro de la lista
	 * que llega por par�metro.
	 *
	 * @param ingresos
	 * @param filtro
	 * @return
	 *
	 * @autor Yennifer Guerrero
	 */
	public ArrayList<DtoIngresosOdontologicos> obtenerEstadoCitasPorPaciente(List<Integer> ingresos, DtoReporteIngresosOdontologicos filtro);
	
	/**
	 * Este m�todo se encarga de obtener la informaci�n de los pacientes 
	 * sin valoraciones iniciales seg�n la lista de pacientes que llega por par�metro. 
	 *
	 * @param pacientes
	 * @return
	 *
	 * @autor Yennifer Guerrero
	 */
	public ArrayList<DtoIngresosOdontologicos> obtenerInfoPacientesSinValIni(List<Integer> pacientes);
	
	/**
	 * 
	 * Este m�todo se encarga de obtener el c�digo de las citas de valoraci�n 
	 * inicial en estado atendida cuyo registro es el
	 * m�s actual por paciente.
	 * @param pacientesConValIni 
	 *
	 * @return
	 *
	 * @autor Yennifer Guerrero
	 */
	public List<Long> obtenerCodigoCitasOdontoAtendidas(List<Integer> pacientesConValIni);
	
	/**
	 * Este m�todo se encarga de encontrar una cita odontol�gica por medio
	 * de su codigo y actualizar su indicativo de cambio de estado.
	 *
	 * @param codigoCita
	 * @return
	 *
	 * @autor Yennifer Guerrero
	 */
	public boolean actualizarIndicativoCambioEstadoCita(long codigoCita);
}
