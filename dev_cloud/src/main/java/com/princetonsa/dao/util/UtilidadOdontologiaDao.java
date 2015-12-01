package com.princetonsa.dao.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.odontologia.InfoPlanTratamiento;

import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.odontologia.DtoAntecedentesAlerta;
import com.princetonsa.dto.odontologia.DtoAntecendenteOdontologico;
import com.princetonsa.dto.odontologia.DtoComponenteIndicePlaca;
import com.princetonsa.dto.odontologia.DtoDetalleIndicePlaca;
import com.princetonsa.dto.odontologia.DtoFiltroConsultaServiciosPaciente;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
import com.princetonsa.dto.odontologia.DtoTratamientoExterno;
import com.princetonsa.dto.odontologia.DtoTratamientoInterno;

public interface UtilidadOdontologiaDao {
	
	/**
	 * Inserta el indice de placa
	 * @param DtoIndicePlaca dto
	 * */
	public int insertarIndicePlaca(Connection con,DtoComponenteIndicePlaca dto);
	
	/**
	 * Consulta las interpretaciones de indice de placa, la informacion la devuelve como lenguaje de marcas (XML)
	 * */
	public String cargarInterpretacionIndicePlaca();
	
	
	/**
	 * 
	 * M&eacute;todo que carga los servicios odont&oacute;logicos. Si se envia el c&oacute;digo y tipo de servicio, 
	 * solo trae el registro correspondiente a ese servicio.
	 * Tambien se listan los servicios que estan asociados a una cita o a un grupo de citas espec&iacute;fico
	 * 
	 * @param parametros
	 * @return
	 */
	public ArrayList<DtoServicioOdontologico> obtenerServicios(HashMap<String, Object> parametros);
	
	
	/**
	 * Método que obtiene los servicios asociados a un paciente,
	 * con un estado y tipo de cita que debe estar asociada a un ingreso espec&iacute;fico.
	 * 
	 * Si se envia el código de la cita, lo involucra en el filtro.
	 * 
	 * @param codigoPaciente
	 * @param codigoIngreso
	 * @param estadoCita
	 * @param tipoCita
	 * @param codigoCita
	 * @return
	 */
	public  ArrayList<DtoServicioOdontologico> obtenerServiciosXEstadoCitaXTipoCita (int codigoPaciente, int codigoIngreso, String estadoCita, String tipoCita, int codigoCita);
		
		
	/**
	 * meodo que retorna las piezas dentales
	 * @return
	 */
	public ArrayList<InfoDatosInt> obtenerPiezasDentales();
	
	/**
	 * metodo que obtiene los tratamientos externos 
	 * @return
	 */
	public ArrayList<DtoTratamientoExterno> obtenerAnteOdoTratamientosExternos(int codigoAnteOdo);
	
	/**
	 * metodo que obtiene el antecedente odontologico
	 * @param codigoPaciente
	 * @return
	 */
	public DtoAntecendenteOdontologico obtenerAntecedenteOdontologico(int codigoPaciente);
	
	/**
	 * Método implementado para cargar la informacion de antecedentes bien sea desde la valoracion o evolucion odontologica
	 * @param con
	 * @param antecedenteOdo
	 */
	public void obtenerAntecedenteOdontologicoHistorico(Connection con,DtoAntecendenteOdontologico antecedenteOdo);
	
	/**
	 * metodo que inserta un antecedente odontologico
	 * @param con
	 * @param dto
	 * @return
	 */
	public int insertarAntcedenteOdontologico(Connection con, DtoAntecendenteOdontologico dto);
	
	/**
	 * Método que retorna un booleano indicando si tiene o no agenda generada un profesional de la salud
	 * @param connection
	 * @param codigoProfesional
	 * @return
	 */
	public boolean consultarProfesionalTieneAgendaGenerada(Connection connection, int codigoProfesional);
	
	
	/**
	 * Metodo para consultar TODOS los Antecedentes Odontológicos Asociados a un Paciente (Version NUEVA - Agosto 2009)
	 * @param codigoPersona
	 * @param codIngreso
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public ArrayList<DtoAntecendenteOdontologico> obtenerAntecedentesOdontologicos(int codigoPersona, int codIngreso, String fechaInicial, String fechaFinal);
	
	/**
	 * metodo que actualiza el antecedente odontologico
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarAntecedenteOdontologico(Connection con, DtoAntecendenteOdontologico dto);

	/**
	 * metodo que actualiza Tratamiento Externo 
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarTratamientoExterno(Connection con, DtoTratamientoExterno dto);
	
	/**
	 * metodo eliminar tratamientos internos de un antecedente odontologico
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean deleteTratamientoInterno(Connection con, int codigoAnteOdon);
	
	/**
	 * metodo que elimina un tratamiento externo
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean deleteTratamientoExterno(Connection con, DtoTratamientoExterno dto);

	/**
	 * metodo que inserta un tratamiento interno 
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean insertarTratamientoInterno(Connection con, DtoTratamientoInterno dto);

	/**
	 * metodo que inserta el un tratamiento externo
	 * @param con
	 * @param dto
	 * @return
	 */
	public int insertarTratamientoExterno(Connection con, DtoTratamientoExterno dto);
	
	/**
	 * metodo que obtiene el antecedente odontologico existente
	 * @param esEvolucion 
	 * @param codigoPaciente
	 * @return
	 */
	public DtoAntecendenteOdontologico obtenerAntecedenteOdontExistente(int codigoAnteOdon, boolean esEvolucion);
	
	/**
	 * Método implementado para confirmar el indice de placa
	 * @param con
	 * @param campos
	 * @return
	 */
	public ResultadoBoolean confirmarIndicePlaca(Connection con,HashMap campos);
	
	/**
	 * metodo que actualiza el componente indice placa
	 * @param con
	 * @param dto
	 * @return boolean
	 */
	public boolean actualizarComponenteIndicePlaca(Connection con, DtoComponenteIndicePlaca dto);
	
	/**
	 * metodo de insercion de detalle de componente indice de placa
	 * @param con
	 * @param dto
	 * @param codigoSuperficie
	 * @return int
	 */
	public int insertDetalleCompIndicePlaca(Connection con, DtoDetalleIndicePlaca dto);
		
	/**
	 * metodo de eliminacion de detalle componente  indice placa
	 * @param con
	 * @param dto
	 * @return boolean
	 */
	public boolean eliminarDetalleCompIndicePlaca(Connection con, DtoDetalleIndicePlaca dto);
		
	/**
	 * metod que actualiza el indicador del detalle del indice de placa
	 * @param con
	 * @param dto
	 * @param codigoSuperficie
	 * @return boolean
	 */
	public boolean actualizarDetalleCompIndicePlaca(Connection con, DtoDetalleIndicePlaca dto);
		
	/**
	 * metodo que obtine el ultimo indice de placa
	 * @param connection
	 * @param codigoPaciente
	 * @return
	 */
	public DtoComponenteIndicePlaca consultarComponenteIndicePlaca(Connection connection, int plantillaIngreso,int plantillaEvolucion, int codigoPaciente);
		
	/**
	 * metodo que obtiene un listadod e piezas con las repectivas combinaciones de superficies 
	 * @param connection
	 * @param codigoInstitucion
	 * @param codigoCompIndPlaca
	 * @return
	 */
	public ArrayList<DtoDetalleIndicePlaca> consultarDetalleCompIndicePlaca(Connection connection, int codigoInstitucion, int codigoCompIndPlaca);

	/**
	 * Obtiene el c&oacute;digo y el nombre de la superficie seg&uacute;n el sector, estado y la instituci&oacute;n
	 * @param codigoInstitucion
	 * @param activo
	 * @param sector
	 * @param piezaDental
	 * @return
	 */
	public InfoDatosInt obtenerSuperficieDental(int codigoInstitucion, String activo, int sector, int piezaDental);
	
	/**
	 * metodo que retorna una lista de antecedentes que requieren de alerta
	 * @param codigoPaciente
	 * @return ArrayList<DtoAntecedentesAlerta>
	 */
	public ArrayList<DtoAntecedentesAlerta> obtenerAntecedentesAlerta(int codigoPaciente);
	
	/**
	 * Método implementado para confirmar los antecedentes odontológicos
	 * @param con
	 * @param campos
	 * @return
	 */
	public ResultadoBoolean confirmarAntecedenteOdontologico(Connection con,HashMap campos);
	
	/**
	 * metodo que obtiene la condiciones toma del servicio
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @return
	 */
	public ArrayList<InfoDatosInt> obtenerCondicionesTomaServicio(
			Connection con,
			int codigoTarifario,
			int codigoServicio, 
			int codigoInstitucion);
	
	/**
	 * metodo que obtiene la descripcion del servicios
	 * @param codigoTarifario
	 * @param codigoServicio
	 * @return
	 */
	public String obenerNombreServicio (int codigoServicio, int codigoInstitucion);
	
	/**
	 * Método implementado para consultar el id del ingreso de una cita de odontologia
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public BigDecimal consultarIngresoCitaOdontologica(Connection con,BigDecimal codigoCita);
	
	/**
	 * Método implementado para obtener el último registro de antecedentes odontologicos del paciente
	 * @param con
	 * @param dtoAntecedente
	 * @param camposParametrizables: atributo para validar que los ultimos antecedentes encontrados deben tener campos parametrizables
	 */
	public void obtenerUltimoRegistroAntecedentesOdontologia(Connection con,DtoAntecendenteOdontologico dtoAntecedente,boolean camposParametrizables);
	
	/**
	 * Método para consultar la cita de un servicio del plan de tratamiento
	 * @param progServPlanT
	 * @return
	 */
	public ArrayList<BigDecimal> consultarCitaXProgSerPlanTrat(BigDecimal progServPlanT);
	
	/**
	 * Método que verifica si un convenio de in ingreso esta relacionado a un presupuesto que está contratado
	 * @param con
	 * @param dtoSubCuenta
	 * @return
	 */
	public boolean esConvenioRelacionadoAPresupuestoOdoContratado(Connection con,DtoSubCuentas dtoSubCuenta);

	/**
	 * Metodo que verifica si el paciente tiene generada una órden médica
	 * generada previamente de tipo Interconsulta en estado Solicitada (Sin respuesta)
	 * y que no esté incluida en el plan de tratamiento .
	 * @param codigoCuentaActiva código del paciente a consultar
	 * @param codigoCuentaAsocio 
	 * @return true en caso de existir órden, false de lo contrario
	 */
	public boolean validarTipoCitaInterconsulta(int codigoCuentaActiva, int codigoCuentaAsocio);
	
	/**
	 * 
	 * @param pieza
	 * @return
	 */
	public String obtenerNombrePieza(int pieza);

	/**
	 * 
	 * @param codigoPersona
	 * @param idIngreso
	 * @return
	 */
	public boolean pacienteConValoracionInicial(int codigoPersona, int idIngreso);

	/**
	 * M&eacute;todo encargado de verificar que la cita no 
	 * tenga servicios asignados a otra cita de menor orden para la
	 * misma pieza.
	 * @param codigoCita C&oacute;digo de la cita que se desea verificar
	 * @return boolean True en caso de existir servicios de menor órden asignados
	 */
	public boolean existeServicioAsignadoDeMenorOrden(int codigoCita);

	/**
	 * 
	 * @param superficie
	 * @return
	 */
	public String obtenerNombreSuperficie(BigDecimal superficie);
	
	
	/**
	 * 
	 * @param codCuentaPaciente
	 * @param codigoInstitucion
	 * @return
	 */
	public ArrayList<DtoServicioOdontologico> obtenerServiciosInterconsulta(int codCuentaPaciente, int codigoInstitucion,int codCita);

	
	/**
	 * Metodo utilizado para validar si Un profesional Tiene Asociado un Servicio 
	 * según la parametrica de  servicios adicionales por profesional de atención Odontológica
	 * @param codigoServicio
	 * @param codigoMedico
	 * @param unidadAgenda
	 * @param codigoInstitucionInt
	 * @return
	 */
	public boolean profesionalTieneAsosiadoServicioAdd(int codigoServicio,int codigoMedico, int codigoInstitucionInt);

	
	/**
	 *  Metodo utilizado para Obtener el Numero de Solicitud de un Servicio de InterConsulta
	 * @param servicio
	 * @return
	 */
	public int consultarNumeroSolicitudServicio(int servicio, int codCuentaPaciente);

	/**
	 * 
	 * @param con
	 * @param codigoProgramaServicioPT
	 * @return
	 */
	public int obtenerCodigoProgramaHallazgoPiezaProgramaSerPT(Connection con,int codigoProgramaServicioPT);

	
	/**
	 * 
	 * @param codigoPkProgServ
	 * @param infoPlanTrata
	 * @return
	 */
	public ArrayList<BigDecimal> consultarCitaXProgSerPlanTratHisConf(BigDecimal codigoPkProgServ, InfoPlanTratamiento infoPlanTrata);

	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public ArrayList<DtoServicioOdontologico> obtenerServicios(DtoFiltroConsultaServiciosPaciente filtro);

	/**
	 * 
	 * @param con
	 * @param estado
	 * @param codigoPAciente 
	 * @return
	 */
	public boolean pacienteConPlanTratamientoEnEstado(Connection con,String estado, int codigoPAciente);

	/**
	 * 
	 * @param con
	 * @param estados
	 * @param codigoPAciente
	 * @return
	 */
	public boolean pacienteConPlanTratamientoEnEstados(Connection con,ArrayList<String> estados, int codigoPAciente);

	
	
	
}
