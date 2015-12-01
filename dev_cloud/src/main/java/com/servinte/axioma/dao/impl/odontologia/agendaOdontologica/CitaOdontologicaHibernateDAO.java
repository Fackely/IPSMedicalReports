package com.servinte.axioma.dao.impl.odontologia.agendaOdontologica;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.odontologia.DtoFiltroReporteCambioServCitaOdonto;
import com.princetonsa.dto.odontologia.DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas;
import com.princetonsa.dto.odontologia.DtoIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoReporteIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaCambioServiciosOdontologicos;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto;
import com.servinte.axioma.dao.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaDAO;
import com.servinte.axioma.mundo.impl.odontologia.agendaOdontologica.CitasAsociadasAProgramadaDelegate;
import com.servinte.axioma.orm.CitasAsociadasAProgramada;
import com.servinte.axioma.orm.CitasOdontologicas;
import com.servinte.axioma.orm.delegate.odontologia.CitaOdontologicaDelegate;

/**
 * Esta clase se encarga de 
 * @author Angela Aguirre
 */
public class CitaOdontologicaHibernateDAO implements ICitaOdontologicaDAO {
	
	private CitaOdontologicaDelegate delegate;
	private CitasAsociadasAProgramadaDelegate citasAsociadasAProgramadaDelegate;
	
	
	/**
	 * M&eacute;todo constructor de la clase 
	 * @author Yennifer Guerrero
	 */
	public CitaOdontologicaHibernateDAO() 
	{
		delegate = new CitaOdontologicaDelegate();
		citasAsociadasAProgramadaDelegate = new CitasAsociadasAProgramadaDelegate();
	}
	
	@Override
	public ArrayList<DtoIngresosOdontologicos> obtenerCitaOdontoSinValIni(List<Integer> ingresos, List<Integer> codigoPaciente){
		return delegate.obtenerCitaOdontoSinValIni(ingresos, codigoPaciente);
	}
	
	@Override
	public ArrayList<DtoIngresosOdontologicos> obtenercitaOdontoValIniNoAtendida(List<Integer> ingresos){
		return delegate.obtenercitaOdontoValIniNoAtendida(ingresos);
	}
	
	/**
	 * Este m&eacute;todo se encarga de consultar las citas odontol&oacute;gicas
	 * que tienen una valoraci&oacute;n inicial en estado atendida.
	 * @return
	 * 
	 * @author Yennifer Guerrero
	 */
	public List<Integer> obtenercitaOdontoValIniAtendida(List<Integer> ingresos){
		return delegate.obtenercitaOdontoValIniAtendida(ingresos);
	}

	
	@Override
	public List<Long> obtenerCodigoCitasProgramadasPacienteXTipoCita(int codigoPaciente, String tipoCita ) {
		
		return delegate.obtenerCodigoCitasProgramadasPacienteXTipoCita(codigoPaciente, tipoCita);
	}
	
	@Override
	public ArrayList<DtoIngresosOdontologicos> obtenerCitaOdontoDiferenteDeValIni(List<Integer> ingresos, List<Integer> pacientesConValIni){
		return delegate.obtenerCitaOdontoDiferenteDeValIni(ingresos, pacientesConValIni);
	}

	
	@Override
	public boolean guardarCitaAsociadasProgramada(CitasAsociadasAProgramada transientInstance) {
		return citasAsociadasAProgramadaDelegate.guardarCitaAsociadasProgramada(transientInstance);
	}

	
	@Override
	public CitasOdontologicas buscarCitaOdontologicas(long codigoCita) {
		return delegate.findById(codigoCita);
	}
	
	@Override
	public  ArrayList<DtoResultadoConsultaCambioServiciosOdontologicos> consultarCambioServCitasOdonto(DtoFiltroReporteCambioServCitaOdonto dto){
		return delegate.consultarCambioServCitasOdonto(dto);
	}
	
	@Override
	public ArrayList<DtoResultadoConsultaReporteTiempoEsperaAtencionCitasOdonto> consultarTiempoEsperaAtencionCitasOdonto (DtoFiltroReporteTiempoEsperaAtencionCitasOdontologicas dto){
		return delegate.consultarTiempoEsperaAtencionCitasOdonto(dto);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.agendaOdontologica.ICitaOdontologicaDAO#eliminarCitaProgramada(long)
	 */
	@Override
	public boolean eliminarCitaProgramada(long codigoCita) {
		
		return delegate.eliminarCitaProgramada(codigoCita);
	}
	
	@Override
	public ArrayList<DtoIngresosOdontologicos> obtenerEstadoCitasPorPaciente(List<Integer> ingresos, DtoReporteIngresosOdontologicos filtro){
		return delegate.obtenerEstadoCitasPorPaciente(ingresos, filtro);
	}
	
	@Override
	public ArrayList<DtoIngresosOdontologicos> obtenerInfoPacientesSinValIni(List<Integer> pacientes){
		return delegate.obtenerInfoPacientesSinValIni(pacientes);
	}
	
	@Override
	public List<Long> obtenerCodigoCitasOdontoAtendidas(List<Integer> pacientesConValIni){
		return delegate.obtenerCodigoCitasOdontoAtendidas(pacientesConValIni);
	}
	
	@Override
	public boolean actualizarIndicativoCambioEstadoCita(long codigoCita){
		return delegate.actualizarIndicativoCambioEstadoCita(codigoCita);
	}
}
