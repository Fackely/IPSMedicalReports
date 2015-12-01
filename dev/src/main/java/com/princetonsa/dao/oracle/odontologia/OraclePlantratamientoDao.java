package com.princetonsa.dao.oracle.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.odontologia.InfoDetallePlanTramiento;
import util.odontologia.InfoHallazgoSuperficie;
import util.odontologia.InfoIngresoPlanTratamiento;
import util.odontologia.InfoNumSuperficiesPresupuesto;
import util.odontologia.InfoPlanTratamiento;
import util.odontologia.InfoProgramaServicioPlan;
import util.odontologia.InfoServicios;

import com.princetonsa.dao.odontologia.PlanTratamientoDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBasePlanTratamientoDao;
import com.princetonsa.dto.odontologia.DtoDetallePlanTratamiento;
import com.princetonsa.dto.odontologia.DtoLogDetPlanTratamiento;
import com.princetonsa.dto.odontologia.DtoLogPlanTratamiento;
import com.princetonsa.dto.odontologia.DtoLogProgServPlant;
import com.princetonsa.dto.odontologia.DtoOdontograma;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.dto.odontologia.DtoProgHallazgoPieza;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosPlanT;
import com.princetonsa.dto.odontologia.DtoSectorSuperficieCuadrante;
import com.princetonsa.dto.odontologia.DtoServArtIncCitaOdo;

public class OraclePlantratamientoDao implements PlanTratamientoDao{

	@Override
	public ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficies(
			BigDecimal codigoPkPlanTratamiento, int pieza, String seccion,
			ArrayList<String> estadosProgramasOservicios,
			boolean utilizaProgramas,String porConfirmar, 
			boolean cargaServicios, 
			BigDecimal codigoPkPresupuesto
			, int institucion) {
		return SqlBasePlanTratamientoDao.obtenerHallazgosSuperficies(codigoPkPlanTratamiento, pieza, seccion, estadosProgramasOservicios, utilizaProgramas,porConfirmar,cargaServicios, codigoPkPresupuesto , institucion);
	}

	@Override
	public ArrayList<InfoDatosInt> obtenerPiezas(
			BigDecimal codigoPkPlanTratamiento, String seccion, String porConfirmarOPCIONAL) {
		
		return SqlBasePlanTratamientoDao.obtenerPiezas(codigoPkPlanTratamiento, seccion, porConfirmarOPCIONAL);
	}

	@Override
	public ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServicios(
			BigDecimal detallePlanTratamiento,
			ArrayList<String> estadosProgramasOservicios,
			boolean utilizaProgramas,String porConfirmar, boolean cargaServicios , int institucion) {
		
		return SqlBasePlanTratamientoDao.obtenerProgramasOServicios(detallePlanTratamiento,estadosProgramasOservicios, utilizaProgramas,porConfirmar ,cargaServicios,   institucion);
	}

	@Override
	public BigDecimal obtenerUltimoCodigoPlanTratamiento(int ingreso,
			ArrayList<String> estados, String porConfirmarOPCIONAL) {
		
		return SqlBasePlanTratamientoDao.obtenerUltimoCodigoPlanTratamiento(ingreso, estados, porConfirmarOPCIONAL);
		
	}
	
	public ArrayList<InfoProgramaServicioPlan> obtenerProgramasServiciosHallazgosPlanTramiento(double codigoHallazgo, String codigoProgramas, BigDecimal codigoDetalle , boolean utilizaProgramas, double codigoServicio, BigDecimal presupuesto, String codigosAsignadosPresupuesto, ArrayList<InfoNumSuperficiesPresupuesto> superficies ) {
		
		return SqlBasePlanTratamientoDao.obtenerProgramasServiciosHallazgosPlanTramiento(codigoHallazgo, codigoProgramas, codigoDetalle , utilizaProgramas, codigoServicio, presupuesto, codigosAsignadosPresupuesto, superficies);
	}

	@Override
	public ArrayList<DtoProgramasServiciosPlanT> cargarProgramasServiciosPlanT(
			DtoProgramasServiciosPlanT dto) {
		
		return SqlBasePlanTratamientoDao.cargarProgramasServiciosPlanT(dto);
	}

	@Override
	public boolean inactivarProgramasServicios(DtoProgramasServiciosPlanT dto, Connection con, ArrayList<String> estadosAEliminar, boolean utilizaProgramas)
	{
		
		return SqlBasePlanTratamientoDao.inactivarProgramasServicios(dto, con, estadosAEliminar, utilizaProgramas);
	}

	@Override
	public double guardarProgramasServicio(DtoProgramasServiciosPlanT dto , Connection con )
	  {
		
		return SqlBasePlanTratamientoDao.guardarProgramasServicio(dto , con);
	}

	@Override
	public ArrayList<InfoHallazgoSuperficie> obtenerHallazgosBOCA(
			BigDecimal codigoPkPlanTratamiento, int hallazgoOPCIONAL,
			String seccion, ArrayList<String> estadosProgramasOservicios,
			boolean utilizaProgramas, 
			String porConfirmar, 
			boolean cargaServicios, 
			BigDecimal codigoPkPresupuesto , 
			int institucion) 
	{
		
		return SqlBasePlanTratamientoDao.obtenerHallazgosBOCA(codigoPkPlanTratamiento, hallazgoOPCIONAL, seccion, estadosProgramasOservicios, utilizaProgramas, porConfirmar, cargaServicios, codigoPkPresupuesto , institucion);
	}

	@Override
	public int cargarOrdenServicio(int codigoPrograma, BigDecimal codigoDetPlan , Connection con, boolean inclusion) {
		
		return SqlBasePlanTratamientoDao.cargarOrdenServicio(codigoPrograma, codigoDetPlan ,con, inclusion);
	}


	
	/**
	  * @param DtoDetallePlanTratamiento dto
	  * */
	 public double guardarDetPlanTratamiento(Connection con,DtoDetallePlanTratamiento dto){
		return  SqlBasePlanTratamientoDao.guardarDetPlanTratamiento(con,dto); 
	 }
	 
	 /**
	  * @param DtoPlanTratamientoOdo dto
	  * */
	 public double guardarPlanTratamiento(Connection con,DtoPlanTratamientoOdo dto){
		 return SqlBasePlanTratamientoDao.guardarPlanTratamiento(con,dto);
	 }
	 
	 /**
	  * @param DtoPlanTratamientoOdo dto
	  * */
	 public double guardarOdontograma(Connection con,DtoOdontograma dto){
		 return SqlBasePlanTratamientoDao.guardarOdontograma(con,dto);
	 }
	 
	 /**
	  * @param DtoPlanTratamientoOdo dto
	  * */
	 public double guardarLogPlanTratamiento(Connection con,DtoLogPlanTratamiento dto){
		 return SqlBasePlanTratamientoDao.guardarLogPlanTratamiento(con,dto);
	 }
	 
	 /**
	  * @deprecated
	  * @param DtoLogDetPlanTratamiento dto
	  * */
	 public double guardarLogDetPlanTratamiento(Connection con,DtoLogDetPlanTratamiento dto){
		 return SqlBasePlanTratamientoDao.guardarLogDetPlanTratamiento(con,dto);
	 }
	 
	 /**
	  * @deprecated
	  * @param DtoLogProgServPlant dto
	  * */
	 public double guardarLogProgramasServiciosPlanT(Connection con,DtoLogProgServPlant dto){
		 return SqlBasePlanTratamientoDao.guardarLogProgramasServiciosPlanT(con,dto);
	 }

	@Override
	public double guardarDetalle(DtoDetallePlanTratamiento dto , Connection con) {
		
		return SqlBasePlanTratamientoDao.guardarDetalle(dto , con);
	}

	@Override
	public boolean modicarEstadosDetalleProgServ(DtoProgramasServiciosPlanT dto, Connection con) {
		
		return SqlBasePlanTratamientoDao.modicarEstadosDetalleProgServ(dto, con);
	}

	@Override
	public boolean modificar(DtoPlanTratamientoOdo dtoWhere,
			DtoPlanTratamientoOdo dtoNuevo , Connection con) {
		
		return SqlBasePlanTratamientoDao.modificar(dtoWhere, dtoNuevo , con);
	}

	@Override
	public String cargarMigradoPlanTratamiento(BigDecimal codigo) {
		
		return SqlBasePlanTratamientoDao.cargarMigradoPlanTratamiento(codigo);
	}

	@Override
	public String cargarEstadoPlanTratamiento(BigDecimal codigo) {
		
		return SqlBasePlanTratamientoDao.cargarEstadoPlanTratamiento(codigo);
	}
	
	@Override
	public DtoPlanTratamientoOdo consultarPlanTratamiento(DtoPlanTratamientoOdo parametros)
	{
		
		return SqlBasePlanTratamientoDao.consultarPlanTratamiento(parametros);
	}
	
	@Override
	public DtoPlanTratamientoOdo consultarPlanTratamientoHistConf(DtoPlanTratamientoOdo parametros)
	{
		
		return SqlBasePlanTratamientoDao.consultarPlanTratamientoHistConf(parametros);
	}
	
	/**
	  * Obtiene los hallazgos 
	  * @return
	  */
	public ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficies(DtoDetallePlanTratamiento parametros)
	{
		return SqlBasePlanTratamientoDao.obtenerHallazgosSuperficies(parametros);
	}
	
	/** 
	 * Consulta los programas y servicios del detalle del plan de tratamiento
	*/
	public ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServicios(DtoProgramasServiciosPlanT parametros)
	{
		return SqlBasePlanTratamientoDao.obtenerProgramasOServicios(parametros);
	}

	@Override
	public String cargarFechaLogPlant(DtoPlanTratamientoOdo dto) {
		
		return SqlBasePlanTratamientoDao.cargarFechaLogPlant(dto);
	}
	
	/**
	  * Obtiene el programa parametrizado por defecto para un hallazgo
	  * @param DtoProgramasServiciosPlanT
	  * @return
	  */
	 public InfoProgramaServicioPlan obtenerProgramaServicioParamHallazgo(DtoProgramasServiciosPlanT parametros)
	 {
		 return SqlBasePlanTratamientoDao.obtenerProgramaServicioParamHallazgo(parametros);
	 }

	 @Override
	 public ArrayList<InfoServicios> cargarServiciosDeProgramasPlanT(DtoProgramasServiciosPlanT parametros)
	 {
		 return SqlBasePlanTratamientoDao.cargarServiciosDeProgramasPlanT(parametros);
	 }
	
	@Override
	 public ArrayList<BigDecimal> obtenerCodigoPlanTratamiento (int codigoPaciente, ArrayList<String> estados, String porConfirmar)
	 {
		return SqlBasePlanTratamientoDao.obtenerCodigoPlanTratamiento(codigoPaciente, estados, porConfirmar);
	 }
	
	@Override
	 public ArrayList<InfoDatosInt> obtenerPiezas(BigDecimal codigoPkPlanTratamiento, String seccion, String porConfirmar, String activo)
	 {
		return SqlBasePlanTratamientoDao.obtenerPiezas(codigoPkPlanTratamiento, seccion, porConfirmar, activo);
	 }
	
	@Override
	/**
	  * 
	  * @param bigDecimal
	  * @param estadosProgramasOservicios
	  * @param utilizaProgramas
	  * @param porConfirmar
	  * @param activo 
	  * @return
	  */
	 public ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServicios(
			BigDecimal detallePlanTratamiento, 
			ArrayList<String> estadosProgramasOservicios, 
			boolean utilizaProgramas, 
			String porConfirmar,
			String activo)
	{
		return SqlBasePlanTratamientoDao.obtenerProgramasOServicios(detallePlanTratamiento, estadosProgramasOservicios, utilizaProgramas, porConfirmar, activo);
	}
	
	@Override
	 /**
	  * 
	  * @param codigoPkPlanTratamiento
	  * @param pieza
	  * @param seccion
	  * @param estadosProgramasOservicios
	  * @param utilizaProgramas
	  * @param porConfirmar
	  * @param activo
	  * @return
	  */
	 public ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficies(
			 BigDecimal codigoPkPlanTratamiento, 
			 int pieza, 
			 String seccion, 
			 ArrayList<String> estadosProgramasOservicios, 
			 boolean utilizaProgramas, 
			 String porConfirmar,
			 String activo)
	 {
		return SqlBasePlanTratamientoDao.obtenerHallazgosSuperficies(codigoPkPlanTratamiento, pieza, seccion, estadosProgramasOservicios, utilizaProgramas, porConfirmar, activo);
	 }
	
	@Override
	/**
	  * 
	  * @param codigoPkPlanTratamiento
	  * @param hallazgoOPCIONAL
	  * @param seccion
	  * @param estadosProgramasOservicios
	  * @param utilizaProgramas
	  * @param porConfirmar
	  * @param activo
	  * @return
	  */
	 public ArrayList<InfoHallazgoSuperficie> obtenerHallazgosBOCA(
			 BigDecimal codigoPkPlanTratamiento, 
			 int hallazgoOPCIONAL, 
			 String seccion, 
			 ArrayList<String> estadosProgramasOservicios, 
			 boolean utilizaProgramas, 
			 String porConfirmar,
			 String activo)
	 {
		return SqlBasePlanTratamientoDao.obtenerHallazgosBOCA(codigoPkPlanTratamiento, hallazgoOPCIONAL, seccion, estadosProgramasOservicios, utilizaProgramas, porConfirmar, activo);
	 }

	@Override
	public DtoLogPlanTratamiento consultarLogTratamiento(int codPlanTratamiento) {
		
		return SqlBasePlanTratamientoDao.consultarLogTratamiento(codPlanTratamiento);
	}
	

	
	 public   ArrayList<InfoDatosInt> obtenerPiezasInclusionesGarantias(DtoProgramasServiciosPlanT dto, BigDecimal codigoPlanTratamiento , int institucion){
			 return SqlBasePlanTratamientoDao.obtenerPiezasInclusionesGarantias(dto, codigoPlanTratamiento , institucion);
	 }



	@Override
	public ArrayList<DtoLogPlanTratamiento> cargarLogs(
			DtoLogPlanTratamiento dtoWhere) {
		return SqlBasePlanTratamientoDao.cargarLogs(dtoWhere);
	}
	
	@Override
	public ArrayList<DtoLogProgServPlant> cargarLogProgramas(
			DtoLogProgServPlant dtoWhere) {
		return SqlBasePlanTratamientoDao.cargarLogProgramas(dtoWhere);
	}



	
	 
	 /**
	  * Carga las Superficies del Diente
	  * @return
	  */
	 public ArrayList<DtoSectorSuperficieCuadrante> cargarSuperficiesDiente(int institucion)
	 {
		 return SqlBasePlanTratamientoDao.cargarSuperficiesDiente(institucion);
	 }
	 
	 
	/**
	 * Actualiza el estado activo del detalle del plan de tratamiento
	 * @param Connection con
	 * @param DtoProgramasServiciosPlanT dto
	 */
	public boolean actualizarActivoDetallePlanTrat(Connection con,DtoDetallePlanTratamiento dto)
	{
		return SqlBasePlanTratamientoDao.actualizarActivoDetallePlanTrat(con, dto);
	}
	 
	/**
	 * Actualiza el estado activo de los programas del plan de tratamiento
	 * @param Connection con
	 * @param DtoProgramasServiciosPlanT dto
	 */
	public boolean actualizarActivoProgServPlanTr(Connection con,DtoProgramasServiciosPlanT dto)
	{
		return SqlBasePlanTratamientoDao.actualizarActivoProgServPlanTr(con, dto);
	}

	@Override
	public ArrayList<DtoPlanTratamientoOdo> consultarPlanTratamiento(Connection con,
			DtoPlanTratamientoOdo parametros, ArrayList<String> estadosPlan) {
		return SqlBasePlanTratamientoDao.consultarPlanTratamiento(con, parametros, estadosPlan);
	}

	@Override
	public boolean modificarPlan(int institucion, Connection con, boolean utilizaProgramas) {
		return SqlBasePlanTratamientoDao.modificarPlan(institucion, con, utilizaProgramas);
	}

	@Override
	public boolean inactivarPlan(DtoPlanTratamientoOdo dto, Connection con) {
		return SqlBasePlanTratamientoDao.inactivarPlan(dto, con);
	} 

	/**
	  * CARGA EL CODIGO DEL PLAN DE TRATAMIENTO x ingreso y codigo de cita
	  * @param ingreso
	  * @param estados
	  * @param codigoCita
	  * @return
	  */
	 public int obtenerUltimoCodigoPlanTratamientoXIngresoXCita ( int ingreso, ArrayList<String> estados, int codigoCita){
		 return SqlBasePlanTratamientoDao.obtenerUltimoCodigoPlanTratamientoXIngresoXCita(ingreso, estados, codigoCita);
	 }
	
	/**
	 * metodo que inserta el his_conf_plan_tratamiento
	 * @param con
	 * @param valoracion
	 * @param evolucion
	 * @param codigoCita
	 * @param codigoPlanTratamiento
	 * @return
	 */
	public boolean insertHisConfPlanTratamiento(Connection con, int valoracion, int evolucion, int codigoCita, int codigoPlanTratamiento)
	{
		return SqlBasePlanTratamientoDao.insertHisConfPlanTratamiento(con, valoracion, evolucion, codigoCita, codigoPlanTratamiento);
	}
	
	/**
	 * metodo que inserta his_conf_det_plan_t
	 * @param con
	 * @param valoracion
	 * @param evolucion
	 * @param codigoCita
	 * @param codigoPlanTratamiento
	 * @return
	 */
	public boolean insertHisConfDetallePlanTratamiento(Connection con, int valoracion, int evolucion, int codigoCita, int codigoPlanTratamiento)
	{
		return SqlBasePlanTratamientoDao.insertHisConfDetallePlanTratamiento(con, valoracion, evolucion, codigoCita, codigoPlanTratamiento);
	}

	/**
	 * metodo que inserta his_conf_prog_serv_plan_t
	 * @param con
	 * @param valoracion
	 * @param evolucion 
	 * @param codigoCita
	 * @param codigoProgServPlanTrat
	 * @return
	 */
	public boolean insertHisConfProgServPlanTratamiento(Connection con, int valoracion, int evolucion, int codigoCita, int codigoDetPlanTrat)
	{
		return SqlBasePlanTratamientoDao.insertHisConfProgServPlanTratamiento(con, valoracion, evolucion, codigoCita, codigoDetPlanTrat);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPlanTrat
	 * @param valoracion
	 * @param evolucion
	 * @param usuario
	 * @return
	 */
	public boolean confirmarPlanTratamiento(Connection con, int codigoPlanTrat, int valoracion, int evolucion, String usuario)
	{
		return SqlBasePlanTratamientoDao.confirmarPlanTratamiento(con, codigoPlanTrat, valoracion, evolucion, usuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPlanTrat
	 * @param valoracion
	 * @param evolucion
	 * @param usuario
	 * @return
	 */
	public boolean confirmarDetallePlanTratamiento(Connection con, int codigoPlanTrat, int valoracion, int evolucion, String usuario)
	{
		return SqlBasePlanTratamientoDao.confirmarDetallePlanTratamiento(con, codigoPlanTrat, valoracion, evolucion, usuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoDetPlanTrat
	 * @param valoracion
	 * @param evolucion
	 * @param usuario
	 * @return
	 */
	public boolean confirmarProgServPlanTratamiento(Connection con, int codigoDetPlanTrat, int valoracion, int evolucion, String usuario)
	{
		return SqlBasePlanTratamientoDao.confirmarProgServPlanTratamiento(con, codigoDetPlanTrat, valoracion, evolucion, usuario);
	}
	
	 /**
	  * Actualiza informacion en detalla de plan de tratamiento
	  * @param Connection con
	  * @param DtoDetallePlanTratamiento dto
	  * */
	 public boolean actualizarDetPlanTratamiento(Connection con,DtoDetallePlanTratamiento dto)
	 {
		 return SqlBasePlanTratamientoDao.actualizarDetPlanTratamiento(con, dto);
	 }
	 
	/**
	 * Obtiene los hallazgos para las seccion de Otros y Boca
	 * @return
	 */
	public ArrayList<InfoDetallePlanTramiento> obtenerHallazgosSuperficiesSeccionOtrayBoca(DtoDetallePlanTratamiento parametros)
	{
		return SqlBasePlanTratamientoDao.obtenerHallazgosSuperficiesSeccionOtrayBoca(parametros);
	}
	
	/**
	  * obtiene el listado de programas o servicios
	  * @param DtoProgramasServiciosPlanT parametros
	  * */
	 public ArrayList<InfoProgramaServicioPlan> obtenerListadoProgramasServicio(DtoProgramasServiciosPlanT parametros)
	 {
		 return SqlBasePlanTratamientoDao.obtenerListadoProgramasServicio(parametros);
	 }
	 
	 /**
	  * Carga la informacion de un programa/servicio especifico
	  * @param DtoProgramasServiciosPlanT parametros
	  * */
	 public InfoProgramaServicioPlan obtenerInfoProgramaServicios(DtoProgramasServiciosPlanT parametros)
	 {
		 return SqlBasePlanTratamientoDao.obtenerInfoProgramaServicios(parametros);
	 }

	@Override
	public int obtenerCodPkLogServPlanT(int codPkDetallePlanT, int codPrograma,	int codServicio) {
		
		return SqlBasePlanTratamientoDao.obtenerCodPkLogServPlanT(codPkDetallePlanT, codPrograma, codServicio);
	}
	 
	 /**
	  * metodo de actualizacion de estados programas servicios de otra evoluciones del plan de tratamietno
	  * @param con
	  * @param casoActualizacion
	  * @param estado
	  * @param aplica
	  * @param usuario
	  * @param codigoDetPlanTrat
	  * @param codigoPrograma
	  * @param codigoServicio
	  * @return boolean
	  */
	public boolean actualizacionEstadosPSOtrasEvoluciones(
			Connection con, 
			char casoActualizacion, 
			String estado, 
			String aplica,
			String estPrograma,
			String usuario,
			int codigoDetPlanTrat,
			int codigoPrograma,
			int codigoServicio,
			int motivo,
			int codigoCita,
			int valoracion,
			int evolucion,
			String porConfirmar)
	{
		return SqlBasePlanTratamientoDao.actualizacionEstadosPSOtrasEvoluciones(con, casoActualizacion, estado, aplica, estPrograma, usuario, codigoDetPlanTrat, codigoPrograma, codigoServicio, motivo, codigoCita, valoracion, evolucion, porConfirmar);
	}
	
	/**
	 * Método que carga el encabezado del historial de ocnfirmacion del plan de tratamiento
	 * @param con
	 * @param infoPlanTrat
	 */
	public void consultarHisConfPlanTratamiento(Connection con,InfoPlanTratamiento infoPlanTrat)
	{
		SqlBasePlanTratamientoDao.consultarHisConfPlanTratamiento(con, infoPlanTrat);
	}
	
	/**
	  * CARGA LAS PIEZAS del historial de confirmacion
	  * @param codigoPkPlanTratamiento
	  * @return
	  */
	 public ArrayList<InfoDatosInt> obtenerPiezasHisConf(Connection con,InfoPlanTratamiento infoPlanTrat, String seccion)
	 {
		 return SqlBasePlanTratamientoDao.obtenerPiezasHisConf(con, infoPlanTrat, seccion);
	 }
	 
	 /**
	  * Obtiene los hallazgos 
	  * @return
	  */
	 public ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesHisConf(Connection con,DtoDetallePlanTratamiento parametros)
	 {
		 return SqlBasePlanTratamientoDao.obtenerHallazgosSuperficiesHisConf(con, parametros);
	 }
	 
	 /**
		
	  * Consulta los programas y servicios del detalle del plan de tratamiento
	  * Mirando las tablas de historial de confirmacion
	  */
	 public ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServiciosHisConf(Connection con,DtoProgramasServiciosPlanT parametros)
	 {
		 return SqlBasePlanTratamientoDao.obtenerProgramasOServiciosHisConf(con, parametros);
	 }
	 
	 /**
	  * Obtiene los hallazgos para las seccion de Otros y Boca
	  * @return
	  */
	 public ArrayList<InfoDetallePlanTramiento> obtenerHallazgosSuperficiesSeccionOtrayBocaHisConf(Connection con,DtoDetallePlanTratamiento parametros)
	 {
		 return SqlBasePlanTratamientoDao.obtenerHallazgosSuperficiesSeccionOtrayBocaHisConf(con, parametros);
	 }
	 
	 /**
	 * verificar se el o los servicios del programa, alguno de ellos se encuentra agendado
	 * @param detPlanTratamiento
	 * @param codigoPrograma
	 * @param codigoServicio
	 * @return
	 */
	public boolean verificarServiciosAgendados(int detPlanTratamiento, int codigoPrograma, int codigoServicio, int codigoCita)
	{
		return SqlBasePlanTratamientoDao.verificarServiciosAgendados(detPlanTratamiento, codigoPrograma, codigoServicio, codigoCita);
	}

	@Override
	public boolean guardarServArtIncPlant(Connection con,DtoServArtIncCitaOdo dto) {
		
		return SqlBasePlanTratamientoDao.guardarServArtIncPlant(con,dto);
	}

	@Override
	public  ArrayList<DtoServArtIncCitaOdo> cargarServArtIncPlanT(DtoServArtIncCitaOdo dto, BigDecimal codigoDetallePlan,int institucion)
	{
		
		return SqlBasePlanTratamientoDao.cargarServArtIncPlanT(dto, codigoDetallePlan,institucion);
	}
	
	/**
	  * 
	  * @param codigoDetallePlanTratamiento
	  * @param utilizaProgramas
	  * @return
	  */
	 public String obtenerEstadoProgramaServicioPlanTratamiento(BigDecimal codigoDetallePlanTratamiento, boolean utilizaProgramas, double codigoPkProgramaOServicio)
	 {
		 return SqlBasePlanTratamientoDao.obtenerEstadoProgramaServicioPlanTratamiento(codigoDetallePlanTratamiento, utilizaProgramas, codigoPkProgramaOServicio);
	 }
	 
	 @Override
	 public  boolean validarCargos( ArrayList<String> listapkProgramasServiciosPlant){
		 return SqlBasePlanTratamientoDao.validarCargos(listapkProgramasServiciosPlant);
	 }

	 @Override
	 public ArrayList<InfoServicios> cargarServiciosParamPrograma(int codigoPrograma, String codigoTarifarioServ) {
		 
		 return SqlBasePlanTratamientoDao.cargarServiciosParamPrograma(codigoPrograma, codigoTarifarioServ);
	 }

	 @Override
	 public ArrayList<InfoServicios> cargarServiciosDeProgramasPlanT(
			 BigDecimal planTratamiento, Double programa,
			 Integer piezaDental, Integer superficie, Integer hallazgo, int institucion) {
		 return SqlBasePlanTratamientoDao.cargarServiciosDeProgramasPlanT(planTratamiento, programa, piezaDental, superficie, hallazgo, institucion);
	 }

	 @Override
	 public BigDecimal obtenerDetPlanTratamiento(BigDecimal planTratamiento,BigDecimal pieza, BigDecimal hallazgo, BigDecimal superficie, String seccion) 
	 {
		 return SqlBasePlanTratamientoDao.obtenerDetPlanTratamiento(planTratamiento,pieza,hallazgo,superficie, seccion);
	 }

	@Override
	public DtoOdontograma cargarOdontograma(BigDecimal codigoPk, boolean esValoracion) {
		 return SqlBasePlanTratamientoDao.cargarOdontograma(codigoPk, esValoracion);
	}

	@Override
	public boolean actualizarInclusionProgServPlanTr(Connection con,DtoProgramasServiciosPlanT dtoPrograma) {
		
		return SqlBasePlanTratamientoDao.actualizarInclusionProgServPlanTr(con, dtoPrograma);
	}

	@Override
	public boolean estaServicioAsociadoAOtraCita(Connection con, int codigoCita, int codigoProgHallPieza,int servicio, int codigoPaciente)
	{
		return SqlBasePlanTratamientoDao.estaServicioAsociadoAOtraCita(con, codigoCita, codigoProgHallPieza,servicio, codigoPaciente);
	}

	@Override
	public boolean existenProgramasServiciosContratadosNoTerminados(
			Connection con, BigDecimal codigoPkPlanTratamiento,
			boolean utilizaProgramas) {
		return SqlBasePlanTratamientoDao.existenProgramasServiciosContratadosNoTerminados(con, codigoPkPlanTratamiento, utilizaProgramas);
	}

	@Override
	public InfoDatosDouble obtenerPlanTratamientoXCuenta(BigDecimal cuenta) 
	{
		return SqlBasePlanTratamientoDao.obtenerPlanTratamientoXCuenta(cuenta);
	}
	
 
  @Override
	public ArrayList<InfoProgramaServicioPlan> listarProgramaServicioParamHallazgo(
			DtoProgramasServiciosPlanT dtoBusqueda)
	{
		return SqlBasePlanTratamientoDao.listarProgramaServicioParamHallazgo(dtoBusqueda);
	}
	 
	@Override
	public boolean tieneAlgunaSuperficieActiva(BigDecimal codigoPkPlanTratamiento, int piezaDental, String seccion)
	{
		return SqlBasePlanTratamientoDao.tieneAlgunaSuperficieActiva(codigoPkPlanTratamiento, piezaDental, seccion);
	}

	@Override
	public ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesDTO(
			DtoPlanTratamientoOdo dtoPlanTratamiento,
			DtoDetallePlanTratamiento dtoDetallePlanT,
			DtoProgramasServiciosPlanT dtoProgramasServicios,
			ArrayList<String> estadosProgramasOservicios,
			boolean utilizaProgramas, String porConfirmar,
			boolean cargaServicios, BigDecimal codigoPkPresupuesto , int institucion) 
			{
		return SqlBasePlanTratamientoDao.obtenerHallazgosSuperficiesDTO(dtoPlanTratamiento, dtoDetallePlanT, dtoProgramasServicios, estadosProgramasOservicios, utilizaProgramas, porConfirmar, cargaServicios, codigoPkPresupuesto , institucion);
	}

	
	
	
	@Override
	public ArrayList<DtoPlanTratamientoOdo> consultarPlanTratamientoHistorico(DtoPlanTratamientoOdo dto)
	{
		return SqlBasePlanTratamientoDao.consultarPlanTratamientoHistorico(dto);
	}
	
	
	

	@Override
	public ArrayList<InfoDatosInt> obtenerPiezasHistorico(	DtoDetallePlanTratamiento dtoDetallePlan) 
	{
		return SqlBasePlanTratamientoDao.obtenerPiezasHistorico(dtoDetallePlan);
	}
	
	
	@Override
	public ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesHistoricos(
																					DtoPlanTratamientoOdo dtoPlanTratamiento,
																					DtoDetallePlanTratamiento dtoDetallePlan, boolean utilizaProgramas, DtoProgramasServiciosPlanT dtoProgramaServicios, int institucion) 
	{
		
		return SqlBasePlanTratamientoDao.obtenerHallazgosSuperficiesHistoricos(dtoPlanTratamiento, dtoDetallePlan, utilizaProgramas, dtoProgramaServicios ,institucion );
	}
	

	@Override
	public ArrayList<InfoHallazgoSuperficie> obtenerHallazgosBOCAHistoricos(
																			DtoPlanTratamientoOdo dtoPlanTratamiento,
																			DtoDetallePlanTratamiento dtoDetallePlan, boolean utilizaProgramas, DtoProgramasServiciosPlanT dtoProgramaServicios , int institucion ) 
	{
		
		return SqlBasePlanTratamientoDao.obtenerHallazgosBOCAHistoricos(dtoPlanTratamiento, dtoDetallePlan, utilizaProgramas,  dtoProgramaServicios , institucion);
	}
	
	
	@Override
	public ArrayList<InfoDatosInt> obtenerPiezasInclusionesGarantiasHistorico(
			DtoProgramasServiciosPlanT dto, BigDecimal codigoPlanTratamiento) {
		return SqlBasePlanTratamientoDao.obtenerPiezasInclusionesGarantiasHistorico(dto, codigoPlanTratamiento);
	}
	
	
	
	@Override
	public ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesDTOHistoricos(
			DtoPlanTratamientoOdo dtoPlanTratamiento,
			DtoDetallePlanTratamiento dtoDetallePlanT,
			DtoProgramasServiciosPlanT dtoProgramasServicios,
			boolean utilizaProgramas,
			int institucion) {
		return SqlBasePlanTratamientoDao.obtenerHallazgosSuperficiesDTOHistoricos(dtoPlanTratamiento, dtoDetallePlanT, dtoProgramasServicios, utilizaProgramas, institucion);
	}

	@Override
	public boolean eliminarRelacionSuperficiesProgramas(Connection con, int planTratamiento, String seccion)
	{
		return SqlBasePlanTratamientoDao.eliminarRelacionSuperficiesProgramas(con, planTratamiento, seccion);
	}

	@Override
	public ArrayList<DtoProgHallazgoPieza> guardarRelacionesProgSuperficies(Connection con, ArrayList<DtoProgHallazgoPieza> listaEncabezados)
	{
		return SqlBasePlanTratamientoDao.guardarRelacionesProgSuperficies(con, listaEncabezados);
	}

	@Override
	public boolean consultarDetPlanTratamiento(DtoDetallePlanTratamiento dto, Connection con, boolean buscarTerminado)
	{
		return SqlBasePlanTratamientoDao.consultarDetPlanTratamiento(dto, con, buscarTerminado);
	}

	@Override
	public int consultarNumeroSuperficiesPorPrograma(int hallazgo, int programa, int pieza, int superficie, int codigoPlanTratamiento)
	{
		return SqlBasePlanTratamientoDao.consultarNumeroSuperficiesPorPrograma(hallazgo, programa, pieza, superficie, codigoPlanTratamiento);
	}
	
	
	@Override
	public ArrayList<InfoIngresoPlanTratamiento> cargarIngresosPlanTratamiento(
			InfoIngresoPlanTratamiento infoDto) {
		return SqlBasePlanTratamientoDao.cargarIngresosPlanTratamiento(infoDto);
	}

	@Override
	public String cargarNombreUsuarioModificaPlanTratamiento(
			BigDecimal codigoPresupuesto) {
		// 
		return SqlBasePlanTratamientoDao.cargarNombreUsuarioModificaPlanTratamiento(codigoPresupuesto);
	}
	
	
	@Override
	public boolean modificarDetallePlanTratamiento(
			DtoDetallePlanTratamiento dto, Connection con) {
		
		return SqlBasePlanTratamientoDao.modificarDetallePlanTratamiento(dto, con);
	}
	
	
	@Override
	public ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesHistoricos(
			DtoDetallePlanTratamiento dtoDetalle) {
		return SqlBasePlanTratamientoDao.obtenerHallazgosSuperficiesHistoricos(dtoDetalle);
	}


	@Override
	public ArrayList<InfoDatosInt> obtenerPiezasHistoricoPlanTratamiento(
			DtoPlanTratamientoOdo dtoPlan, DtoDetallePlanTratamiento dtoDet) {
		
		return SqlBasePlanTratamientoDao.obtenerPiezasHistoricoPlanTratamiento(dtoPlan, dtoDet);
	}


	@Override
	public ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServiciosHistoricos(
			DtoProgramasServiciosPlanT dtoProgramaServicios) {
	
		return SqlBasePlanTratamientoDao.obtenerProgramasOServiciosHistoricos(dtoProgramaServicios);
	}

	@Override
	public ArrayList<InfoDetallePlanTramiento> obtenerHallazgosSuperficiesSeccionOtrayBocaHistoricos(
			DtoDetallePlanTratamiento parametros) {
		
		return SqlBasePlanTratamientoDao.obtenerHallazgosSuperficiesSeccionOtrayBocaHistoricos(parametros);
	}
	
	
	@Override
	public ArrayList<InfoProgramaServicioPlan> consultarProximaCitaProgramasServicios(
											DtoOdontograma dtoOdongrama, 
											DtoPlanTratamientoOdo dtoPlanTrata,
											int institucion) 
	{
		return SqlBasePlanTratamientoDao.consultarProximaCitaProgramasServicios(dtoOdongrama, dtoPlanTrata, institucion);
	}

	
	
	@Override
	public BigDecimal cargarMaximoCodigoLogPlantratamiento(
			DtoPlanTratamientoOdo dtoPlan,
			Connection con) 
	{
		return SqlBasePlanTratamientoDao.cargarMaximoCodigoLogPlantratamiento(dtoPlan, con);
	}

	
	
	@Override
	public BigDecimal obtenerUltimoCodigoPlanTratamiento(int codigoPaciente) {
		return SqlBasePlanTratamientoDao.obtenerUltimoCodigoPlanTratamiento(codigoPaciente);
	}
	
	@Override
	public DtoPlanTratamientoOdo consultarPlanTratamientoPaciente(int codigoPaciente, int institucion)
	{
		return SqlBasePlanTratamientoDao.consultarPlanTratamientoPaciente(codigoPaciente, institucion);
	}


	/* (non-Javadoc)
	 * @see com.princetonsa.dao.odontologia.PlanTratamientoDao#obtenerCodigoPkUltimaCitaProgServPlanT(java.math.BigDecimal, java.lang.String, java.util.ArrayList)
	 */
	@Override
	public BigDecimal obtenerCodigoPkUltimaCitaProgServPlanT(BigDecimal codigoPkProgServPlanT,
			String fechaFormatoAppNOREQUERIDA, ArrayList<String> estado) {
		
		return SqlBasePlanTratamientoDao.obtenerCodigoPkUltimaCitaProgServPlanT(codigoPkProgServPlanT, fechaFormatoAppNOREQUERIDA, estado);
	}

	/* (non-Javadoc)
	 * @see com.princetonsa.dao.odontologia.PlanTratamientoDao#existeDetPlanTratamientoSinClasificacion(int)
	 */
	@Override
	public boolean existeDetPlanTratamientoSinClasificacion(int codigoCita) {
		
		return SqlBasePlanTratamientoDao.existeDetPlanTratamientoSinClasificacion(codigoCita);
	}

	/* (non-Javadoc)
	 * @see com.princetonsa.dao.odontologia.PlanTratamientoDao#obtenerDetPlanTratamientoXProgramaHallazgoPieza(int)
	 */
	@Override
	public int obtenerDetPlanTratamientoXProgramaHallazgoPieza(int programaHallazgoPieza) {
		
		return SqlBasePlanTratamientoDao.obtenerDetPlanTratamientoXProgramaHallazgoPieza(programaHallazgoPieza);
	}
}
