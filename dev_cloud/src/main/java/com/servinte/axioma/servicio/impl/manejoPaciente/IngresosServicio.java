package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.actionform.odontologia.AgendaOdontologicaForm;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.manejoPaciente.DtoInformacionBasicaIngresoPaciente;
import com.princetonsa.dto.odontologia.DtoReporteIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaIngresosOdonto;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
import com.princetonsa.dto.tesoreria.DtoInfoIngresoTrasladoAbonoPaciente;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IIngresosMundo;
import com.servinte.axioma.orm.Ingresos;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IIngresosServicio;

public class IngresosServicio implements IIngresosServicio {
	
	IIngresosMundo mundo;
	
	public IngresosServicio() {
		mundo = ManejoPacienteFabricaMundo.crearIngresosMundo();
	}
	
	@Override
	public List<Integer> consultarIngresosOdontoEstadoAbierto(
			DtoReporteIngresosOdontologicos dto){
		return mundo.consultarIngresosOdontoEstadoAbierto(dto);
	}
	
	
	@Override
	public List<Ingresos> obtenerIngresosPacientePorEstado(int codPaciente,
			String[] listaEstadosIngreso) {
		return mundo.obtenerIngresosPacientePorEstado(codPaciente, listaEstadosIngreso);
	}

	@Override
	public ArrayList<DtoResultadoConsultaIngresosOdonto> obtenerConsolidadoReporte (ArrayList<DtoResultadoConsultaIngresosOdonto> listadoIngresosOdonto, 
			List<Integer> ingresosconsulta,  DtoReporteIngresosOdontologicos filtroIngresos){
		return mundo.obtenerConsolidadoReporte(listadoIngresosOdonto, ingresosconsulta, filtroIngresos);
	}

	
	@Override
	public DtoInformacionBasicaIngresoPaciente construirDtoInformacionBasicaIngresoPaciente(
			AgendaOdontologicaForm forma, UsuarioBasico usuario) {
		return mundo.construirDtoInformacionBasicaIngresoPaciente(forma, usuario);
	}
	
	
	@Override
	public Ingresos crearIngresos(
			DtoInformacionBasicaIngresoPaciente dtoInfoBasica) {
		return mundo.crearIngresos(dtoInfoBasica);
	}

	
	@Override
	public DtoInformacionBasicaIngresoPaciente construirDtoInformacionBasicaIngresoPaciente(
			DtoServicioOdontologico servicio, UsuarioBasico usuario) {
		return mundo.construirDtoInformacionBasicaIngresoPaciente(servicio, usuario);
	}
	
	@Override
	public ArrayList<DtoResultadoConsultaIngresosOdonto> ordenarInfoReporteAmbos(
			ArrayList<DtoCentrosAtencion> listadoCentroAtencionIngreso, 
			ArrayList<DtoResultadoConsultaIngresosOdonto> listadoIngresosOdonto){
		return mundo.ordenarInfoReporteAmbos(listadoCentroAtencionIngreso, listadoIngresosOdonto);
	}

	
	@Override
	public DtoInfoIngresoTrasladoAbonoPaciente obtenerUltimoIngresoPaciente(int codPaciente) {
		return mundo.obtenerUltimoIngresoPaciente(codPaciente);
	}
	
	@Override
	public ArrayList<DtoResultadoConsultaIngresosOdonto> obtenerConsolidadoCitasPorPaciente(DtoReporteIngresosOdontologicos filtro,
			List<Integer> ingresos, ArrayList<DtoCentrosAtencion> listadoCentrosAtencionIngreso){
		 return mundo.obtenerConsolidadoCitasPorPaciente(filtro, ingresos, listadoCentrosAtencionIngreso);
	}
}
