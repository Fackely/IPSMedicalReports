package com.servinte.axioma.servicio.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.comun.DtoResultado;
import com.princetonsa.dto.tesoreria.DtoConsultaTrasladoAbonoPAciente;
import com.princetonsa.dto.tesoreria.DtoGuardarTrasladoAbonoPaciente;
import com.princetonsa.dto.tesoreria.DtoInfoIngresoTrasladoAbonoPaciente;
import com.princetonsa.dto.tesoreria.DtoValidacionesTrasladoAbonoPaciente;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.RegionesCobertura;
import com.servinte.axioma.orm.TiposIdentificacion;


/**
 * Servicio que le delega al negocio las operaciones relacionados con 
 * los ingresos y los egresos que tiene una caja 
 * 
 * @author Cristhian Murillo
 * @see com.servinte.axioma.servicio.impl.tesoreria.IIngresosEgresosCajaServicioServicio
 */
public interface IIngresosEgresosCajaServicioServicio {
	
	
	/**
	 * Lista por tipo de tipos de identificacion
	 */
	public ArrayList<TiposIdentificacion> listarTiposIdentificacionPorTipo(String[] listaIntegridadDominio);
	
	
	/**
	 * Retorna una persona por su numero de identificacion y tipo
	 * @param identificacionBuscar
	 * @param acronimoTipoIdentificacion
	 * @return {@link DtoPersonas}
	 */
	public DtoPersonas obtenerPaciente(String identificacionBuscar, String acronimoTipoIdentificacion);
	
	
	/**
	 * Valida so un paciente es valido para realizar un traslado de abonos.
	 * Retorna un DTO con el resultado de los estados de als valiadciones del paciente
	 * 
	 * @param paciente
	 * @return {@link DtoValidacionesTrasladoAbonoPaciente}
	 */
	public DtoValidacionesTrasladoAbonoPaciente validarPacienteParaTrasladoAbono(DtoPersonas paciente);
	
	
	/**
	 * Retorna los ingresos de un paciente.
	 * El parametro listarPorIngreso define si se debe mostrar detallado cada uno de los 
	 * ingresos con su valor o si se debe listar el totalizado de estos para el paciente dado
	 * 
	 * @param codPaciente
	 * @param listarPorIngreso
	 * @return
	 */
	public List<DtoInfoIngresoTrasladoAbonoPaciente> obtenerIngresosParaTrasladoPorPaciente (int codPaciente, boolean listarPorIngreso);
	
	
	/**
	 * Valida so un paciente es valido para realizar un traslado de abonos.
	 * Retorna una lista de errores en el caso de que existan;
	 * 
	 * @param paciente
	 * @return
	 */
	public DtoValidacionesTrasladoAbonoPaciente validarPacienteParaTrasladoAbonoDestino(DtoPersonas paciente);
	
	
	
	/**
	 * Retorna el ultimo ingreso en estado abierto del paciente
	 * Este metodo debe llamarse luego de validar luego que el paciente tenga ingresos 
	 * en estado abierto @see (List<Ingresos> obtenerIngresosPacientePorEstado (int codPaciente, String[] listaEstadosIngreso)).
	 * El valor de abonoDisponible retornado es el valor totalizado de todos los ingresos.
	 * Si el paciente no tiene un centro de atencion duenio asociado no se traeran registros de este.
	 * 
	 * @param codPaciente
	 * @param parametroManejoEspecialInstiOdontologicas
	 * @return
	 */
	public DtoInfoIngresoTrasladoAbonoPaciente obtenerUltimoIngresoAbiertoPaciente (int codPaciente, boolean parametroManejoEspecialInstiOdontologicas);
	
	
	/**
	 * Guarda un traslado de abonos
	 * 
	 * @param paciente
	 * @return
	 */
	public DtoResultado guardarTrasladoAbono(DtoGuardarTrasladoAbonoPaciente dtoGuardarTrasladoAbonoPaciente);
	
	
	
	/**
	 * Retorna los detalles  de un traslado de abonos enviando como parametro el codigo del traslado
	 * @param dtoConsulta
	 */
	public List<DtoConsultaTrasladoAbonoPAciente> obtenerDetallesTrasladoAbonos(DtoConsultaTrasladoAbonoPAciente dtoConsulta);
	
	
	
	
	/* LOCALIZACION */
	/**
	 * Lista todos los Paises
	 * @return
	 */
	public ArrayList<Paises> listarPaises ();
	
	/**
	 * Lista todas las Ciudades
	 * @return
	 */
	public ArrayList<Ciudades> listarCiudades ();
	
	/**
	 * Lista todas las Ciudades de un país
	 * @return
	 */
	public ArrayList<Ciudades> listarCiudadesPorPais (String codigoPais);
	
	/**
	 * Lista todas las Regiones de Cobertura
	 * @return
	 */
	public ArrayList<RegionesCobertura> listarRegionesCoberturaActivas();
	
	/**
	 * Lista todas las Instituciones
	 * @return
	 */
	public ArrayList<Instituciones> listarInstituciones ();
	
	
}
