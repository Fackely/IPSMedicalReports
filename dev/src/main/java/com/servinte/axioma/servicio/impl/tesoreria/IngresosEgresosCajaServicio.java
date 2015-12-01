package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.comun.DtoResultado;
import com.princetonsa.dto.tesoreria.DtoConsultaTrasladoAbonoPAciente;
import com.princetonsa.dto.tesoreria.DtoGuardarTrasladoAbonoPaciente;
import com.princetonsa.dto.tesoreria.DtoInfoIngresoTrasladoAbonoPaciente;
import com.princetonsa.dto.tesoreria.DtoValidacionesTrasladoAbonoPaciente;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ILocalizacionMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ITiposIdentificacionMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IIngresosMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IPacientesMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITrasladosAbonoMundo;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.RegionesCobertura;
import com.servinte.axioma.orm.TiposIdentificacion;
import com.servinte.axioma.servicio.interfaz.tesoreria.IIngresosEgresosCajaServicioServicio;


/**
 * Implementaci&oacute;n de la interfaz {@link IIngresosEgresosCajaServicioServicio}
 * 
 * @author Cristhian Murillo
 *
 */
public class IngresosEgresosCajaServicio  implements IIngresosEgresosCajaServicioServicio{

	private ITiposIdentificacionMundo tiposIdentificacionMundo;
	private IPacientesMundo pacientesMundo;
	private ITrasladosAbonoMundo trasladosAbonoMundo;
	private IIngresosMundo ingresosMundo;
	private ILocalizacionMundo localizacionMundo;
	
	
	public IngresosEgresosCajaServicio()
	{
		tiposIdentificacionMundo	= AdministracionFabricaMundo.crearTiposIdentificacionMundo();
		pacientesMundo				= ManejoPacienteFabricaMundo.crearPacientesMundo();
		trasladosAbonoMundo			= TesoreriaFabricaMundo.crearTrasladosAbonoMundo();
		ingresosMundo				= ManejoPacienteFabricaMundo.crearIngresosMundo();
		localizacionMundo			= AdministracionFabricaMundo.crearloILocalizacionMundo();
		
	}
	
	
	@Override
	public ArrayList<TiposIdentificacion> listarTiposIdentificacionPorTipo(	String[] listaIntegridadDominio) {
		return tiposIdentificacionMundo.listarTiposIdentificacionPorTipo(listaIntegridadDominio);
	}


	@Override
	public DtoPersonas obtenerPaciente(String identificacionBuscar, String acronimoTipoIdentificacion) {
		return pacientesMundo.obtenerPaciente(identificacionBuscar, acronimoTipoIdentificacion);
	}


	@Override
	public DtoValidacionesTrasladoAbonoPaciente validarPacienteParaTrasladoAbono(DtoPersonas paciente) {
		return trasladosAbonoMundo.validarPacienteParaTrasladoAbono(paciente);
	}


	@Override
	public List<DtoInfoIngresoTrasladoAbonoPaciente> obtenerIngresosParaTrasladoPorPaciente(int codPaciente, boolean listarPorIngreso) {
		return ingresosMundo.obtenerIngresosParaTrasladoPorPaciente(codPaciente, listarPorIngreso);
	}


	@Override
	public DtoValidacionesTrasladoAbonoPaciente validarPacienteParaTrasladoAbonoDestino(DtoPersonas paciente) {
		return trasladosAbonoMundo.validarPacienteParaTrasladoAbonoDestino(paciente);
	}


	@Override
	public DtoInfoIngresoTrasladoAbonoPaciente obtenerUltimoIngresoAbiertoPaciente(int codPaciente, boolean parametroManejoEspecialInstiOdontologicas) {
		return ingresosMundo.obtenerUltimoIngresoAbiertoPaciente(codPaciente, parametroManejoEspecialInstiOdontologicas);
	}


	@Override
	public DtoResultado guardarTrasladoAbono(DtoGuardarTrasladoAbonoPaciente dtoGuardarTrasladoAbonoPaciente) {
		return trasladosAbonoMundo.guardarTrasladoAbono(dtoGuardarTrasladoAbonoPaciente);
	}

	
	@Override
	public ArrayList<Ciudades> listarCiudades() {
		return localizacionMundo.listarCiudades();
	}


	@Override
	public ArrayList<Ciudades> listarCiudadesPorPais(String codigoPais) {
		return localizacionMundo.listarCiudadesPorPais(codigoPais);
	}


	@Override
	public ArrayList<Paises> listarPaises() {
		return localizacionMundo.listarPaises();
	}


	@Override
	public ArrayList<RegionesCobertura> listarRegionesCoberturaActivas() {
		return localizacionMundo.listarRegionesCoberturaActivas();
	}


	@Override
	public ArrayList<Instituciones> listarInstituciones() {
		return localizacionMundo.listarInstituciones();
	}


	@Override
	public List<DtoConsultaTrasladoAbonoPAciente> obtenerDetallesTrasladoAbonos(
			DtoConsultaTrasladoAbonoPAciente dtoConsulta) {
		return trasladosAbonoMundo.obtenerDetallesTrasladoAbonos(dtoConsulta);
	}

	
	
}
