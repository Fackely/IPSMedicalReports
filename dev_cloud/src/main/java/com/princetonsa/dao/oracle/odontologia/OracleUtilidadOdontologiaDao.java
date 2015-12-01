package com.princetonsa.dao.oracle.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.odontologia.InfoPlanTratamiento;

import com.princetonsa.dao.sqlbase.odontologia.SqlBaseUtilidadOdontologia;
import com.princetonsa.dao.util.UtilidadOdontologiaDao;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.odontologia.DtoAntecedentesAlerta;
import com.princetonsa.dto.odontologia.DtoAntecendenteOdontologico;
import com.princetonsa.dto.odontologia.DtoComponenteIndicePlaca;
import com.princetonsa.dto.odontologia.DtoDetalleIndicePlaca;
import com.princetonsa.dto.odontologia.DtoFiltroConsultaServiciosPaciente;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
import com.princetonsa.dto.odontologia.DtoTratamientoExterno;
import com.princetonsa.dto.odontologia.DtoTratamientoInterno;

public class OracleUtilidadOdontologiaDao implements UtilidadOdontologiaDao 
{
	/**
	 * Inserta el indice de placa
	 * @param DtoIndicePlaca dto
	 * */
	public int insertarIndicePlaca(Connection con,DtoComponenteIndicePlaca dto)
	{
		return SqlBaseUtilidadOdontologia.insertarIndicePlaca(con,dto);
	}
	
	/**
	 * Consulta las interpretaciones de indice de placa, la informacion la devuelve como lenguaje de marcas (XML)
	 * */
	public String cargarInterpretacionIndicePlaca()
	{
		return SqlBaseUtilidadOdontologia.cargarInterpretacionIndicePlaca();
	}
	
	/**
	 * M&eacute;todo que carga los servicios odont&oacute;logicos. Si se envia el c&oacute;digo y tipo de servicio, 
	 * solo trae el registro correspondiente a ese servicio.
	 * 
	 * @param codigoInstitucion
	 * @param unidadConsulta
	 * @param codigoMedico
	 * @param codigoPaciente
	 * @param tipoServicio
	 * @param activos
	 * @param validaPresCont
	 * @param cambiarSerOdo
	 * @param casoBusSer
	 * @param codigoCita
	 * @param buscarPlanTratamiento
	 * @param codigoServicio
	 * @return
	 */
	public ArrayList<DtoServicioOdontologico> obtenerServicios(HashMap<String, Object> parametros)
	{
		return SqlBaseUtilidadOdontologia.obtenerServicios(parametros);
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoServicioOdontologico> obtenerServicios(DtoFiltroConsultaServiciosPaciente filtro)
	{
		return SqlBaseUtilidadOdontologia.obtenerServicios(filtro);
	}
	
	/**
	 * meodo que retorna las piezas dentales
	 * @return
	 */
	public ArrayList<InfoDatosInt> obtenerPiezasDentales()
	{
		return SqlBaseUtilidadOdontologia.obtenerPiezasDentales();
	}
	
	/**
	 * metodo que obtiene los tratamientos externos 
	 * @return
	 */
	public ArrayList<DtoTratamientoExterno> obtenerAnteOdoTratamientosExternos(int codigoPaciente)
	{
		return SqlBaseUtilidadOdontologia.obtenerAnteOdoTratamientosExternos(codigoPaciente);
	}
	
	/**
	 * metodo que obtiene el antecedente odontologico
	 * @param codigoPaciente
	 * @return
	 */
	public DtoAntecendenteOdontologico obtenerAntecedenteOdontologico(int codigoPaciente)
	{
		return SqlBaseUtilidadOdontologia.obtenerAntecedenteOdontologico(codigoPaciente);
	}
	
	/**
	 * Método implementado para cargar la informacion de antecedentes bien sea desde la valoracion o evolucion odontologica
	 * @param con
	 * @param antecedenteOdo
	 */
	public void obtenerAntecedenteOdontologicoHistorico(Connection con,DtoAntecendenteOdontologico antecedenteOdo)
	{
		SqlBaseUtilidadOdontologia.obtenerAntecedenteOdontologicoHistorico(con, antecedenteOdo);
	}

	@Override
	public ArrayList<DtoAntecendenteOdontologico> obtenerAntecedentesOdontologicos(int codPaciente, int codIngreso, String fechaInicio, String fechaFinal) {
		return SqlBaseUtilidadOdontologia.obtenerAntecedentesOdontologicos(codPaciente,codIngreso, fechaInicio, fechaFinal);
	}
	
	/**
	 * metodo que inserta un antecedente odontologico
	 * @param con
	 * @param dto
	 * @return
	 */
	public int insertarAntcedenteOdontologico(Connection con, DtoAntecendenteOdontologico dto)
	{
		return SqlBaseUtilidadOdontologia.insertarAntcedenteOdontologico(con, dto);
	}
	
	/**
	 * Método que retorna un booleano indicando si tiene o no agenda generada un profesional de la salud
	 * @param connection
	 * @param codigoProfesional
	 * @return
	 */
	public boolean consultarProfesionalTieneAgendaGenerada(Connection connection, int codigoProfesional){
		return SqlBaseUtilidadOdontologia.consultarProfesionalTieneAgendaGenerada(connection, codigoProfesional);
	}
	
	/**
	 * metodo que actualiza el antecedente odontologico
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarAntecedenteOdontologico(Connection con, DtoAntecendenteOdontologico dto)
	{
		return SqlBaseUtilidadOdontologia.actualizarAntecedenteOdontologico(con, dto);
	}

	/**
	 * metodo que actualiza Tratamiento Externo 
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarTratamientoExterno(Connection con, DtoTratamientoExterno dto)
	{
		return SqlBaseUtilidadOdontologia.actualizarTratamientoExterno(con, dto);
	}
	
	/**
	 * metodo eliminar tratamientos internos de un antecedente odontologico
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean deleteTratamientoInterno(Connection con, int codigoAnteOdon)
	{
		return SqlBaseUtilidadOdontologia.deleteTratamientoInterno(con, codigoAnteOdon);
	}
	
	/**
	 * metodo que elimina un tratamiento externo
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean deleteTratamientoExterno(Connection con, DtoTratamientoExterno dto)
	{
		return SqlBaseUtilidadOdontologia.deleteTratamientoExterno(con, dto);
	}

	/**
	 * metodo que inserta un tratamiento interno 
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean insertarTratamientoInterno(Connection con, DtoTratamientoInterno dto)
	{
		return SqlBaseUtilidadOdontologia.insertarTratamientoInterno(con, dto);
	}

	/**
	 * metodo que inserta el un tratamiento externo
	 * @param con
	 * @param dto
	 * @return
	 */
	public int insertarTratamientoExterno(Connection con, DtoTratamientoExterno dto)
	{
		return SqlBaseUtilidadOdontologia.insertarTratamientoExterno(con, dto);
	}
	
	/**
	 * metodo que obtiene el antecedente odontologico existente
	 * @param codigoPaciente
	 * @return
	 */
	public DtoAntecendenteOdontologico obtenerAntecedenteOdontExistente(int codigoAnteOdon,boolean esEvolucion)
	{
		return SqlBaseUtilidadOdontologia.obtenerAntecedenteOdontExistente(codigoAnteOdon,esEvolucion);
	}
	
	/**
	 * Método implementado para confirmar el indice de placa
	 * @param con
	 * @param campos
	 * @return
	 */
	public ResultadoBoolean confirmarIndicePlaca(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadOdontologia.confirmarIndicePlaca(con, campos);
	}
	
	/**
	 * metodo que actualiza el componente indice placa
	 * @param con
	 * @param dto
	 * @return boolean
	 */
	public boolean actualizarComponenteIndicePlaca(Connection con, DtoComponenteIndicePlaca dto)
	{
		return SqlBaseUtilidadOdontologia.actualizarComponenteIndicePlaca(con, dto);
	}
	
	/**
	 * metodo de insercion de detalle de componente indice de placa
	 * @param con
	 * @param dto
	 * @param codigoSuperficie
	 * @return int
	 */
	public int insertDetalleCompIndicePlaca(Connection con, DtoDetalleIndicePlaca dto)
	{
		return SqlBaseUtilidadOdontologia.insertDetalleCompIndicePlaca(con, dto);
	}
		
	/**
	 * metodo de eliminacion de detalle componente  indice placa
	 * @param con
	 * @param dto
	 * @return boolean
	 */
	public boolean eliminarDetalleCompIndicePlaca(Connection con, DtoDetalleIndicePlaca dto)
	{
		return SqlBaseUtilidadOdontologia.eliminarDetalleCompIndicePlaca(con, dto);
	}
		
	/**
	 * metod que actualiza el indicador del detalle del indice de placa
	 * @param con
	 * @param dto
	 * @param codigoSuperficie
	 * @return boolean
	 */
	public boolean actualizarDetalleCompIndicePlaca(Connection con, DtoDetalleIndicePlaca dto)
	{
		return SqlBaseUtilidadOdontologia.actualizarDetalleCompIndicePlaca(con, dto);
	}
		
	/**
	 * metodo que obtine el ultimo indice de placa
	 * @param connection
	 * @param codigoPaciente
	 * @return
	 */
	public DtoComponenteIndicePlaca consultarComponenteIndicePlaca(Connection connection, int plantillaIngreso,int plantillaEvolucion, int codigoPaciente)
	{
		return SqlBaseUtilidadOdontologia.consultarComponenteIndicePlaca(connection, plantillaIngreso, plantillaEvolucion, codigoPaciente);
	}
		
	/**
	 * metodo que obtiene un listadod e piezas con las repectivas combinaciones de superficies 
	 * @param connection
	 * @param codigoInstitucion
	 * @param codigoCompIndPlaca
	 * @return
	 */
	public ArrayList<DtoDetalleIndicePlaca> consultarDetalleCompIndicePlaca(
			Connection connection, 
			int codigoInstitucion, 
			int codigoCompIndPlaca)
	{
		return SqlBaseUtilidadOdontologia.consultarDetalleCompIndicePlaca(connection, codigoInstitucion, codigoCompIndPlaca);
	}

	/**
	 * Obtine el c&oacute;digo y el nombre de la superficie segun el sector, estado y la institucion
	 * @param codigoInstitucion
	 * @param activo
	 * @param sectores
	 * @return
	 */
	public InfoDatosInt obtenerSuperficieDental(int codigoInstitucion, String activo, int sector, int piezaDental)
	{
		return SqlBaseUtilidadOdontologia.obtenerSuperficieDental(codigoInstitucion, activo, sector, piezaDental);
	}
	
	/**
	 * metodo que retorna una lista de antecedentes que requieren de alerta
	 * @param codigoPaciente
	 * @return ArrayList<DtoAntecedentesAlerta>
	 */
	public ArrayList<DtoAntecedentesAlerta> obtenerAntecedentesAlerta(int codigoPaciente)
	{
		return SqlBaseUtilidadOdontologia.obtenerAntecedentesAlerta(codigoPaciente);
	}
	
	/**
	 * Método implementado para confirmar los antecedentes odontológicos
	 * @param con
	 * @param campos
	 * @return
	 */
	public ResultadoBoolean confirmarAntecedenteOdontologico(Connection con,HashMap campos)
	{
		return SqlBaseUtilidadOdontologia.confirmarAntecedenteOdontologico(con, campos);
	}
	
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
			int codigoInstitucion)
	{
		return SqlBaseUtilidadOdontologia.obtenerCondicionesTomaServicio(con, codigoTarifario, codigoServicio, codigoInstitucion);
	}
	
	/**
	 * metodo que obtiene la descripcion del servicios
	 * @param codigoTarifario
	 * @param codigoServicio
	 * @return
	 */
	public String obenerNombreServicio (int codigoServicio, int codigoInstitucion)
	{
		return SqlBaseUtilidadOdontologia.obenerNombreServicio(codigoServicio, codigoInstitucion);
	}
	
	/**
	 * Método implementado para consultar el id de la cuenta de una cita de odontologia
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public BigDecimal consultarIngresoCitaOdontologica(Connection con,BigDecimal codigoCita)
	{
		return SqlBaseUtilidadOdontologia.consultarIngresoCitaOdontologica(con, codigoCita);
	}
	
	/**
	 * Método implementado para obtener el último registro de antecedentes odontologicos del paciente
	 * @param con
	 * @param dtoAntecedente
	 * @param camposParametrizables: atributo para validar que los ultimos antecedentes encontrados deben tener campos parametrizables
	 */
	public void obtenerUltimoRegistroAntecedentesOdontologia(Connection con,DtoAntecendenteOdontologico dtoAntecedente,boolean camposParametrizables)
	{
		SqlBaseUtilidadOdontologia.obtenerUltimoRegistroAntecedentesOdontologia(con, dtoAntecedente, camposParametrizables);
	}
	
	/**
	 * Método para consultar la cita de un servicio del plan de tratamiento
	 * @param progServPlanT
	 * @return
	 */
	public ArrayList<BigDecimal> consultarCitaXProgSerPlanTrat(BigDecimal progServPlanT)
	{
		return SqlBaseUtilidadOdontologia.consultarCitaXProgSerPlanTrat(progServPlanT);
	}
	
	/**
	 * Método que verifica si un convenio de in ingreso esta relacionado a un presupuesto que está contratado
	 * @param con
	 * @param dtoSubCuenta
	 * @return
	 */
	public boolean esConvenioRelacionadoAPresupuestoOdoContratado(Connection con,DtoSubCuentas dtoSubCuenta)
	{
		return SqlBaseUtilidadOdontologia.esConvenioRelacionadoAPresupuestoOdoContratado(con, dtoSubCuenta);
	}

	@Override
	public boolean validarTipoCitaInterconsulta(int codigoCuentaActiva, int codigoCuentaAsocio) {
		return SqlBaseUtilidadOdontologia.validarTipoCitaInterconsulta(codigoCuentaActiva, codigoCuentaAsocio);
	}

	@Override
	public String obtenerNombrePieza(int pieza) {
		return SqlBaseUtilidadOdontologia.obtenerNombrePieza(pieza);
	}
	
	@Override
	public boolean pacienteConValoracionInicial(int codigoPersona, int idIngreso) {
		return SqlBaseUtilidadOdontologia.pacienteConValoracionInicial(codigoPersona,idIngreso);
	}

	@Override
	public boolean existeServicioAsignadoDeMenorOrden(int codigoCita) {
		return SqlBaseUtilidadOdontologia.existeServicioAsignadoDeMenorOrden(codigoCita);
	}

	@Override
	public String obtenerNombreSuperficie(BigDecimal superficie) {
		return SqlBaseUtilidadOdontologia.obtenerNombreSuperficie(superficie);
	}
	
	/**
	 * Metodo que obtiene el profesional asociado a la primera valoracion y plan de tratamiento del paciente ya confirmada
	 * @param codigoPersona
	 * @param idIngreso
	 * @return
	 */
	public HashMap<String, Object> obtenerProfesionalPrimeraValoracion(int codigoPersona, int idIngreso){
		return SqlBaseUtilidadOdontologia.obtenerNombreSuperficie(codigoPersona,idIngreso);
	}

	@Override
	/**
	 * 
	 */
	public ArrayList<DtoServicioOdontologico> obtenerServiciosInterconsulta( int codCuentaPaciente, int codigoInstitucion, int codCita) {
		
		return SqlBaseUtilidadOdontologia.obtenerServiciosInterconsulta(codCuentaPaciente, codigoInstitucion, codCita);
	}

	@Override
	public boolean profesionalTieneAsosiadoServicioAdd(int codigoServicio,int codigoMedico, int codigoInstitucionInt) {
		
		return SqlBaseUtilidadOdontologia.profesionalTieneAsosiadoServicioAdd(codigoServicio,codigoMedico,codigoInstitucionInt);
	}

	@Override
	public int consultarNumeroSolicitudServicio(int servicio, int codCuentaPaciente) {
		
		return SqlBaseUtilidadOdontologia.consultarNumeroSolicitudServicio(servicio, codCuentaPaciente);
	}

	@Override
	public int obtenerCodigoProgramaHallazgoPiezaProgramaSerPT(Connection con,int codigoProgramaServicioPT) 
	{
		return SqlBaseUtilidadOdontologia.obtenerCodigoProgramaHallazgoPiezaProgramaSerPT(con, codigoProgramaServicioPT);
	}

	@Override
	public ArrayList<BigDecimal> consultarCitaXProgSerPlanTratHisConf(BigDecimal codigoPkProgServ, InfoPlanTratamiento infoPlanTrata) {
		
		return SqlBaseUtilidadOdontologia.consultarCitaXProgSerPlanTratHisConf(codigoPkProgServ, infoPlanTrata);
	}

	@Override
	public boolean pacienteConPlanTratamientoEnEstado(Connection con,
			String estado, int codigoPAciente) {
		return SqlBaseUtilidadOdontologia.pacienteConPlanTratamientoEnEstado(con,estado,codigoPAciente);

	}

	
	
	/**
	 * 
	 * @param con
	 * @param estados
	 * @param codigoPAciente
	 * @return
	 */
	@Override
	public boolean pacienteConPlanTratamientoEnEstados(Connection con,ArrayList<String> estados, int codigoPAciente)
	{
		return SqlBaseUtilidadOdontologia.pacienteConPlanTratamientoEnEstados(con,estados,codigoPAciente);
	}

	
	
	/* (non-Javadoc)
	 * @see com.princetonsa.dao.util.UtilidadOdontologiaDao#obtenerServiciosXEstadoCitaXTipoCita(int, int, java.lang.String, java.lang.String, int)
	 */
	@Override
	public ArrayList<DtoServicioOdontologico> obtenerServiciosXEstadoCitaXTipoCita (int codigoPaciente, int codigoIngreso, String estadoCita, String tipoCita, int codigoCita){
		
		return SqlBaseUtilidadOdontologia.obtenerServiciosXEstadoCitaXTipoCita(codigoPaciente, codigoIngreso, estadoCita, tipoCita, codigoCita);
	}
	

}