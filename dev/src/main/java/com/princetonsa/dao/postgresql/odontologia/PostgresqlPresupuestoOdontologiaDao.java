package com.princetonsa.dao.postgresql.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import util.InfoDatosDouble;
import util.odontologia.InfoConvenioContratoPresupuesto;
import util.odontologia.InfoPresupuestoPrecontratado;

import com.princetonsa.dao.odontologia.PresupuestoOdontologicoDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBasePresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoCitasPresupuestoOdo;
import com.princetonsa.dto.odontologia.DtoConcurrenciaPresupuesto;
import com.princetonsa.dto.odontologia.DtoConsolidadoPresupuestoContratadoPorPromocion;
import com.princetonsa.dto.odontologia.DtoHistoricoIngresoPresupuesto;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoLogPrespuesto;
import com.princetonsa.dto.odontologia.DtoLogPresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoPresupuesto;
import com.princetonsa.dto.odontologia.DtoPresuContratoOdoImp;
import com.princetonsa.dto.odontologia.DtoPresuOdoProgServ;
import com.princetonsa.dto.odontologia.DtoPresupuestoDetalleServiciosProgramaDao;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdoConvenio;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento;
import com.princetonsa.dto.odontologia.DtoPresupuestoPiezas;
import com.princetonsa.dto.odontologia.DtoPresupuestoTotalConvenio;
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratadosConPromocion;

public class PostgresqlPresupuestoOdontologiaDao  implements PresupuestoOdontologicoDao{

	@Override
	public ArrayList<DtoPresupuestoOdoConvenio> cargarPresupuestoConvenio(
			DtoPresupuestoOdoConvenio dto, Connection con) {
		
		return SqlBasePresupuestoOdontologico.cargarPresupuestoConvenio(dto, con);
	}

	@Override
	public ArrayList<DtoPresuOdoProgServ> cargarPresupuestoOdoProgServ(
			DtoPresuOdoProgServ dto, Connection con, DtoPresupuestoPiezas dtoPieza) {
		
		return SqlBasePresupuestoOdontologico.cargarPresupuestoOdoProgServ(dto, con, dtoPieza);
	}

	@Override
	public ArrayList<DtoPresupuestoPiezas> cargarPresupuestoPieza(
			DtoPresupuestoPiezas dto, boolean inactivarNoPendientesPlanTratamiento, boolean utilizaProgramas) {
		
		return SqlBasePresupuestoOdontologico.cargarPresupuestoPieza(dto, inactivarNoPendientesPlanTratamiento, utilizaProgramas);
		}

	
	@Override
	public ArrayList<DtoPresupuestoOdontologico> cargarPresupuesto( Connection con, 
			DtoPresupuestoOdontologico dto, ArrayList<String> estadosPresupuesto, boolean estadoNotIn) {
		
		return SqlBasePresupuestoOdontologico.cargarPresupuesto(con, dto, estadosPresupuesto, estadoNotIn);
	}

	@Override
	public ArrayList<DtoPresupuestoTotalConvenio> cargarTotalesPresupuetoConvenio(
			DtoPresupuestoTotalConvenio dto) {
		
		return SqlBasePresupuestoOdontologico.cargarTotalesPresupuetoConvenio(dto);
	}

	@Override
	public boolean eliminarPresupuesto(DtoPresupuestoOdontologico dto) {
		
		return SqlBasePresupuestoOdontologico.eliminarPresupuesto(dto);
	}

	@Override
	public boolean eliminarPresupuestoOdoConvenio(DtoPresupuestoOdoConvenio dto , Connection con) {
		
		return SqlBasePresupuestoOdontologico.eliminarPresupuestoOdoConvenio(dto , con);
	}

	@Override
	public boolean eliminarPresupuestoPieza(DtoPresupuestoPiezas dto , Connection con) {
		
		return SqlBasePresupuestoOdontologico.eliminarPresupuestoPieza(dto , con);
	}

	@Override
	public boolean eliminarPresupuestoProgramaServicio(DtoPresuOdoProgServ dto , Connection con) {
		
		return SqlBasePresupuestoOdontologico.eliminarPresupuestoProgramaServicio(dto , con);
	}

	@Override
	public double guardarPresupuesto(DtoPresupuestoOdontologico dto , Connection con ) {
		
		 return SqlBasePresupuestoOdontologico.guardarPresupuesto(dto, con);
	}

	@Override
	public double guardarPresupuestoOdoConvenio(DtoPresupuestoOdoConvenio dto ,  Connection con) {
		
		return SqlBasePresupuestoOdontologico.guardarPresupuestoOdoConvenio(dto , con);
	}

	@Override
	public double guardarPresupuestoOdoProgramaServicio(DtoPresuOdoProgServ dto ,  Connection con) {
		
		return SqlBasePresupuestoOdontologico.guardarPresupuestoOdoProgramaServicio(dto , con);
	}

	@Override
	public double guardarPresupuestoPiezas(DtoPresupuestoPiezas dto ,  Connection con) {
		
		return SqlBasePresupuestoOdontologico.guardarPresupuestoPiezas(dto , con);
	}

	@Override
	public boolean modificarPresupuesto(DtoPresupuestoOdontologico dto , Connection con ,  ArrayList<String> arrayEstados) {
		
		return SqlBasePresupuestoOdontologico.modificarPresupuesto(dto , con , arrayEstados);
	}

	@Override
	public boolean modificarPresupuestoConvenio(DtoPresupuestoOdoConvenio dto  , Connection con) {
		
		return SqlBasePresupuestoOdontologico.modificarPresupuestoConvenio(dto , con);
	}

	@Override
	public boolean modificarPresupuestoPieza(DtoPresupuestoPiezas dto) {
		
		return SqlBasePresupuestoOdontologico.modificarPresupuestoPieza(dto);
	}

	@Override
	public boolean modificarPresupuestoProgramaServicio(DtoPresuOdoProgServ dto, Connection con) {
		
		return SqlBasePresupuestoOdontologico.modificarPresupuestoProgramaServicio(dto, con);
	}
	
	@Override
	public ArrayList<DtoLogPresupuestoOdontologico> cargarLogPresupuesto(
			DtoLogPresupuestoOdontologico dto) {
		
		return SqlBasePresupuestoOdontologico.cargarLogPresupuesto(dto);
	}

	@Override
	public ArrayList<DtoPresupuestoOdontologicoDescuento> cargarPresupuestoDescuentos(
			DtoPresupuestoOdontologicoDescuento dto, ArrayList<String> listaEstados) {
		
		return SqlBasePresupuestoOdontologico.cargarPresupuestoDescuentos(dto, listaEstados);
	}

	
	@Override
	public boolean eliminarPresupuestoDescuento(
			DtoPresupuestoOdontologicoDescuento dto) {
		
		return SqlBasePresupuestoOdontologico.eliminarPresupuestoDescuento(dto);
	}

	


	
	@Override
	public double guardarPresupuestoDescuento(
			DtoPresupuestoOdontologicoDescuento dto , Connection con) {
		
		return SqlBasePresupuestoOdontologico.guardarPresupuestoDescuento(dto , con);
	}
	
	@Override
	public boolean aplicaPromocion(int contrato) {
		
		return SqlBasePresupuestoOdontologico.aplicaPromocion(contrato);
	}

	@Override
	public BigDecimal existeProgramaServicioPlanTratamientoEnPresupuesto(
			int piezaOPCIONAL, int superficieOPCIONAL, int hallazgoREQUERIDO,
			BigDecimal codigoPkProgramaServicio, boolean utilizaProgramas, BigDecimal codigoPkPresupuesto, String seccion)
	{
		
		return SqlBasePresupuestoOdontologico.existeProgramaServicioPlanTratamientoEnPresupuesto(piezaOPCIONAL,superficieOPCIONAL,hallazgoREQUERIDO,
				codigoPkProgramaServicio, utilizaProgramas, codigoPkPresupuesto, seccion);
	}
	
	
	@Override
	public int existeProgramaServicioPresupuestoEnPlanTratamiento(
			int piezaOPCIONAL, int superficieOPCIONAL, int hallazgoREQUERIDO,
			BigDecimal codigoPkProgramaServicio, boolean utilizaProgramas,
			BigDecimal codigoPkPlan, String estadoOPCIONAL) {
		
	   return SqlBasePresupuestoOdontologico.existeProgramaServicioPresupuestoEnPlanTratamiento(piezaOPCIONAL, superficieOPCIONAL, hallazgoREQUERIDO, codigoPkProgramaServicio, utilizaProgramas, codigoPkPlan, estadoOPCIONAL);
	}
	
	@Override
	public String consultaReporteContratar(String tipoRelacion,
			int codigoPresupuesto) {
		
		return SqlBasePresupuestoOdontologico.consultaReporteContratar(tipoRelacion, codigoPresupuesto);
	}

	@Override
	public String consultaReporteDetalle(int codigoPresupuesto,
			String tipoRelacion) {
		
		return SqlBasePresupuestoOdontologico.consultaReporteDetalle(codigoPresupuesto, tipoRelacion);
	}

	@Override
	public ArrayList<InfoConvenioContratoPresupuesto> obtenerConveniosContratosPresupuesto(
			BigDecimal codigoPkPresupuesto)
	{
		
		return SqlBasePresupuestoOdontologico.obtenerConveniosContratosPresupuesto(codigoPkPresupuesto);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPkPresupuestoREQUERIDO
	 * @param programaOservicioREQUERIDO
	 * @param utilizaProgramas
	 * @param piezaDentalOPCIONAL
	 * @param hallazgoREQUERIDO
	 * @param superficieOPCIONAL
	 * @return
	 */
	public boolean eliminarProgramaServicioPresupuestoCascada(	Connection con, 
																BigDecimal codigoPkPresupuestoREQUERIDO,  
																int programaOservicioREQUERIDO, 
																boolean utilizaProgramas, 
																int piezaDentalOPCIONAL, 
																int hallazgoREQUERIDO, 
																int superficieOPCIONAL,
																String seccionREQUERIDA,
																String loginUsuario,
																boolean eliminarFilaProg)
	{
		return SqlBasePresupuestoOdontologico.eliminarProgramaServicioPresupuestoCascada(con, codigoPkPresupuestoREQUERIDO, programaOservicioREQUERIDO, utilizaProgramas, piezaDentalOPCIONAL, hallazgoREQUERIDO, superficieOPCIONAL, seccionREQUERIDA, loginUsuario, eliminarFilaProg);
	}
	
	@Override
	public DtoConcurrenciaPresupuesto estaEnProcesoPresupuesto(Connection con,
			int cuenta, String idSesionOPCIONAL, boolean igualSession)
	{
		return SqlBasePresupuestoOdontologico.estaEnProcesoPresupuesto(con, cuenta, idSesionOPCIONAL, igualSession);
	}

	@Override
	public boolean empezarBloqueoPresupuesto(int idCuenta, String loginUsuario,
			String idSesion, boolean esFlujoInclusiones)
	{
		return SqlBasePresupuestoOdontologico.empezarBloqueoPresupuesto(idCuenta, loginUsuario, idSesion, esFlujoInclusiones);
	}

	@Override
	public int cancelarTodosLosProcesosDePresupuesto(Connection con)
	{
		return SqlBasePresupuestoOdontologico.cancelarTodosLosProcesosDePresupuesto(con);
	}
	
	@Override
	public boolean cancelarProcesoPresupuesto(int idCuenta, String idSesion)
	{
		return SqlBasePresupuestoOdontologico.cancelarProcesoPresupuesto(idCuenta, idSesion);
	}
	
	/**
	 * Método par aobtener el codigo del presupuesto de un plan de tratamiento
	 * por un estado específico
	 * @param codigoPlanTratamiento
	 * @param estado
	 * @return
	 */
	@Override
	public BigDecimal consultarCodigoPresupuestoXPlanTratamiento(BigDecimal codigoPlanTratamiento,String estado)
	{
		return SqlBasePresupuestoOdontologico.consultarCodigoPresupuestoXPlanTratamiento(codigoPlanTratamiento, estado);
	}
	
	
	@Override
	public ArrayList<DtoPresupuestoDetalleServiciosProgramaDao> cargarPresupuestoDetalleServicosPrograma(
			DtoPresupuestoDetalleServiciosProgramaDao dto) {
		
		return SqlBasePresupuestoOdontologico.cargarPresupuestoDetalleServicosPrograma(dto);
	}

	@Override
	public boolean eliminarPresupuestoDetalleServiciosProgramaDao(
			DtoPresupuestoDetalleServiciosProgramaDao dto, Connection con) {
		
		return SqlBasePresupuestoOdontologico.eliminarPresupuestoDetalleServiciosProgramaDao(dto, con);
	}

	@Override
	public double insertPresupuestoDetalleServiciosProgramaDao(
			DtoPresupuestoDetalleServiciosProgramaDao dto , Connection con) {
		
		return SqlBasePresupuestoOdontologico.insertPresupuestoDetalleServiciosProgramaDao(dto , con);
	}
	
	/**
	 * 
	 * @param codigoPresupuesto
	 * @return
	 */
	public BigDecimal obtenerTotalPresupuestoParaDescuento(BigDecimal codigoPresupuesto)
	{
		return SqlBasePresupuestoOdontologico.obtenerTotalPresupuestoParaDescuento(codigoPresupuesto);
	}
	
	/**
	 * 
	 * @param codigoPresupuesto
	 * @return
	 */
	public BigDecimal obtenerTotalPresupuestoSinDctoOdon(BigDecimal codigoPresupuesto)
	{
		return SqlBasePresupuestoOdontologico.obtenerTotalPresupuestoSinDctoOdon(codigoPresupuesto);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean modificarDescuentoPresupuesto(Connection con, DtoPresupuestoOdontologicoDescuento dto)
	{
		return SqlBasePresupuestoOdontologico.modificarDescuentoPresupuesto(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param fhuModifica
	 * @param estado
	 * @param codigoPk
	 * @return
	 */
	public boolean cambiarEstadoDescuentoPresupuesto(Connection con, DtoInfoFechaUsuario fhuModifica, String estado, BigDecimal codigoPk)
	{
		return SqlBasePresupuestoOdontologico.cambiarEstadoDescuentoPresupuesto(con, fhuModifica, estado, codigoPk);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPkPresupuesto
	 * @param loginUsuario
	 * @param codigoMotivo
	 * @return
	 */
	public boolean anularDescuentoPresupuesto(Connection con, BigDecimal codigoPkPresupuesto, String loginUsuario, double codigoMotivo)
	{
		return SqlBasePresupuestoOdontologico.anularDescuentoPresupuesto(con, codigoPkPresupuesto, loginUsuario, codigoMotivo);
	}
	
	@Override
	public ArrayList<InfoPresupuestoPrecontratado> cargarPresupuestosPresontratados(
			int ingreso, BigDecimal presupuestoNotIn) {
		return SqlBasePresupuestoOdontologico.cargarPresupuestosPresontratados(ingreso, presupuestoNotIn);
	}
	
	@Override
	public boolean cambiarEstadoPresupuesto(Connection con,
			BigDecimal codigoPk, String estadoNuevo) {
		return SqlBasePresupuestoOdontologico.cambiarEstadoPresupuesto(con, codigoPk, estadoNuevo);
	}
	
	@Override
	public boolean modificarIndicativoContratadoPresupuesto(Connection con,
			BigDecimal codigoPkPresupuesto) {
		return SqlBasePresupuestoOdontologico.modificarIndicativoContratadoPresupuesto(con, codigoPkPresupuesto);
	}
	
	@Override
	public boolean existePresupuestoXPaciente(int codigoPaciente, ArrayList<String> estados) {
		return SqlBasePresupuestoOdontologico.existePresupuestoXPaciente(codigoPaciente, estados);
	}

	@Override
	public boolean reversarAnticiposPresupuestoContratado(Connection con,
			BigDecimal codigoPkPresupuesto) {
		return SqlBasePresupuestoOdontologico.reversarAnticiposPresupuestoContratado(con, codigoPkPresupuesto);
	}

	@Override
	public double obtenerPorcentajeDctoOdonContratadoPresupuesto(
			BigDecimal codigoPkPresupuesto) 
	{
		return SqlBasePresupuestoOdontologico.obtenerPorcentajeDctoOdonContratadoPresupuesto(codigoPkPresupuesto);
	}
	
	@Override
	public BigDecimal insertarPlanTtoPresupuesto(Connection con,
			DtoPlanTratamientoPresupuesto dto, boolean utilizaProgramas) {
		return SqlBasePresupuestoOdontologico.insertarPlanTtoPresupuesto(con, dto, utilizaProgramas);
	}
	
	@Override
	public boolean eliminarPlanTtoPresupuesto(Connection con,
			BigDecimal codigoPkPresupuesto) {
		return SqlBasePresupuestoOdontologico.eliminarPlanTtoPresupuesto(con, codigoPkPresupuesto);
	}
	
	@Override
	public boolean actualizarPlanTtoPresupuestoProgServ(Connection con, BigDecimal codigoPkProgramaServicioPlanTto,
			BigDecimal detPlanTratamiento, InfoDatosDouble programa,
			int servicio, BigDecimal codigoPkPresupuesto) 
	{
		return SqlBasePresupuestoOdontologico.actualizarPlanTtoPresupuestoProgServ(con,codigoPkProgramaServicioPlanTto,
				detPlanTratamiento, programa,servicio, codigoPkPresupuesto);
	}
	
	@Override
	public boolean modificarBonos(Connection con,DtoPresupuestoOdoConvenio dtoConvenio) {
		return SqlBasePresupuestoOdontologico.modificarBonos( con, dtoConvenio);
	}

	@Override
	public boolean modificarPromociones(Connection con,
			DtoPresupuestoOdoConvenio dtoConvenio) {
		return SqlBasePresupuestoOdontologico.modificarPromociones(	con, dtoConvenio);
	}

	@Override
	public ArrayList<DtoCitasPresupuestoOdo> cargarCitasPresupuestoOdontologico(
			int codigoPaciente) {
		return SqlBasePresupuestoOdontologico.cargarCitasPresupuestoOdontologico(codigoPaciente);
	}
	
	@Override
	public boolean puedoTerminarPresupuesto(boolean utilizaProgramas,
			BigDecimal codigoPKPresupuesto) {
		return SqlBasePresupuestoOdontologico.puedoTerminarPresupuesto(utilizaProgramas, codigoPKPresupuesto);
	}

	@Override
	public double obtenerDctoOdontologicoPresupuestoXPlan(
			BigDecimal codigoPkPlanTratamiento) {
		return SqlBasePresupuestoOdontologico.obtenerDctoOdontologicoPresupuestoXPlan(codigoPkPlanTratamiento);
	}

	@Override
	public DtoLogPrespuesto cargarDtoLogPresupuesto(DtoLogPrespuesto dto) {
	
		return SqlBasePresupuestoOdontologico.cargarDtoLogPresupuesto(dto);
	}

	
	@Override
	public boolean guardarContratoPresupuestoClausula(Connection con,DtoPresuContratoOdoImp dto) 
	{
		return SqlBasePresupuestoOdontologico.guardarContratoPresupuestoClausula(con, dto);
	}

	@Override
	public BigDecimal obtenerTotalPresupuestoConDctoOdon(BigDecimal codigoPkPresupuesto) {
		
		return SqlBasePresupuestoOdontologico.obtenerTotalPresupuestoConDctoOdon(codigoPkPresupuesto);
	}

	@Override
	public BigDecimal obtenerValorDctoOdontologicoPresupuesto(BigDecimal codigoPkPresupuesto) {
		
		return SqlBasePresupuestoOdontologico.obtenerValorDctoOdontologicoPresupuesto(codigoPkPresupuesto);
	}

	@Override
	public BigDecimal cargarValorPresupuesto(BigDecimal codigoPkPresupuesto) {
		
		return SqlBasePresupuestoOdontologico.cargarValorPresupuesto(codigoPkPresupuesto);
	}
	
	
	
	@Override
	public List<DtoHistoricoIngresoPresupuesto> listaIngresoHistoricosPresupuesto(
																				final int paciente,
																				final int viaIngreso, 
																				final int institucion) {
		return  SqlBasePresupuestoOdontologico.listaIngresoHistoricosPresupuesto(paciente, viaIngreso, institucion);
	}

	@Override
	public ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> obtenerPresupuestoOdoContratadoPromo(
			DtoReportePresupuestosOdontologicosContratadosConPromocion dto) {
		return  SqlBasePresupuestoOdontologico.obtenerPresupuestoOdoContratadoPromo(dto);
	}
	
	
	/* (non-Javadoc)
	 * @see com.princetonsa.dao.odontologia.PresupuestoOdontologicoDao#obtenerValorContratadoInicialPresupuesto(long)
	 */
	@Override
	public BigDecimal obtenerValorContratadoInicialPresupuesto(long codigoPresupuesto) {
		
		return  SqlBasePresupuestoOdontologico.obtenerValorContratadoInicialPresupuesto(codigoPresupuesto);
	}
	
}
