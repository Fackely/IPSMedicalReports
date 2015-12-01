package com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoFiltroReporteCambioServCitaOdonto;
import com.princetonsa.dto.odontologia.DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaCambioServiciosOdontologicos;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto;

public interface ICitaOdontologicaServicio {
	
	/**
	 * Este m&eacute;todo se encarga de consultar las solicitudes de cambio de servicio.
	 * @param filtroCambioServicio
	 * @return ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>
	 * 
	 * @author Fabian Becerra
	 * @author Wilson Gomez
	 * @author Javier Gonzalez
	 */
	public ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> consolidarInfoReporteCitas (DtoFiltroReporteCambioServCitaOdonto filtroCambioServicio);
	
	/**
	 * Este m&eacute;todo se encarga de consultar las solicitudes de cambio de servicio ordenadas para archivo plano
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
