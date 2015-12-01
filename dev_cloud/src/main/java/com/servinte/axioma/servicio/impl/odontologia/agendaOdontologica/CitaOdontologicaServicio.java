package com.servinte.axioma.servicio.impl.odontologia.agendaOdontologica;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoFiltroReporteCambioServCitaOdonto;
import com.princetonsa.dto.odontologia.DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaCambioServiciosOdontologicos;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto;
import com.servinte.axioma.mundo.fabrica.odontologia.agendaOdontologica.AgendaOdontologicaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaMundo;
import com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaServicio;

public class CitaOdontologicaServicio implements ICitaOdontologicaServicio {
	
	private ICitaOdontologicaMundo mundo;
	
	public CitaOdontologicaServicio() {
		mundo = AgendaOdontologicaFabricaMundo.crearCitaOdontologicaMundo();
	}
	
	/**
	 * Este m&eacute;todo se encarga de consultar las solicitudes de cambio de servicio.
	 * @param filtroCambioServicio
	 * @return ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>
	 * 
	 * @author Fabian Becerra
	 * @author Wilson Gomez
	 * @author Javier Gonzalez
	 */
	public ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> consolidarInfoReporteCitas (DtoFiltroReporteCambioServCitaOdonto filtroCambioServicio){
		return mundo.consolidarInfoReporteCitas(filtroCambioServicio);
	}
	
	/**
	 * Este m&eacute;todo se encarga de consultar las solicitudes de cambio de servicio.
	 * @param filtroCambioServicio
	 * @return ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>
	 * 
	 * @author Fabian Becerra
	 * @author Wilson Gomez
	 * @author Javier Gonzalez
	 */
	public ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> consolidarInfoReporteCitasPlano (DtoFiltroReporteCambioServCitaOdonto filtroCambioServicio){
		return mundo.consolidarInfoReporteCitasPlano(filtroCambioServicio);
	}
	
	
	/**
	 * Este método se encarga de consultar los tiempos de espera de atención de citas odontológicas
	 * @param filtroTiempoEspera
	 * @return ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto>
	 * 
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> consolidarInfoReporteTiemposEspera (DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas filtroTiempoEspera){
		return mundo.consolidarInfoReporteTiemposEspera(filtroTiempoEspera);
	}
	
	/**
	 * Este método se encarga de consultar las citas odontologicas en el sistema
	 * y ordenar para generar el reporte plano de tiempos espera de atencion de citas odontológicas
	 * @param filtroTiempoEspera
	 * @return ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos>
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> consolidarInfoReporteTiemposEsperaPlano(
			DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas filtroTiempoEspera) {
		return mundo.consolidarInfoReporteTiemposEsperaPlano(filtroTiempoEspera);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaServicio#eliminarCitaProgramada(long)
	 */
	@Override
	public boolean eliminarCitaProgramada(long codigoCita) {
		
		return mundo.eliminarCitaProgramada(codigoCita);
	}
	
	/**
	 * Este mètodo se encarga de encontrar una cita odontológica por medio
	 * de su codigo y actualizar su indicativo de cambio de estado.
	 *
	 * @param codigoCita
	 * @return
	 *
	 * @autor Yennifer Guerrero
	 */
	public boolean actualizarIndicativoCambioEstadoCita(long codigoCita){
		return mundo.actualizarIndicativoCambioEstadoCita(codigoCita);
	}
}
