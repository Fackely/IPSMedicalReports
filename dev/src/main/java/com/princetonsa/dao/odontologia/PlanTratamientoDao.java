package com.princetonsa.dao.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

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

import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.odontologia.InfoDetallePlanTramiento;
import util.odontologia.InfoHallazgoSuperficie;
import util.odontologia.InfoIngresoPlanTratamiento;
import util.odontologia.InfoNumSuperficiesPresupuesto;
import util.odontologia.InfoPlanTratamiento;
import util.odontologia.InfoProgramaServicioPlan;
import util.odontologia.InfoServicios;

/**
 * 
 * @author axioma
 * 
 */
public interface PlanTratamientoDao {
	/**
	 * 
	 * @param ingreso
	 * @param estados
	 * @return
	 */
	public BigDecimal obtenerUltimoCodigoPlanTratamiento(int ingreso,
			ArrayList<String> estados, String porConfirmarOPCIONAL);

	/**
	 * 
	 * @param codigoPkPlanTratamiento
	 * @param seccion
	 * @return
	 */
	public ArrayList<InfoDatosInt> obtenerPiezas(
			BigDecimal codigoPkPlanTratamiento, String seccion,
			String porConfirmarOPCIONAL);

	/**
	 * 
	 * @param codigoPkPlanTratamiento
	 * @param pieza
	 * @param seccion
	 * @param estadosProgramasOservicios
	 * @param utilizaProgramas
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficies(
			BigDecimal codigoPkPlanTratamiento, int pieza, String seccion,
			ArrayList<String> estadosProgramasOservicios,
			boolean utilizaProgramas, String porConfirmar,
			boolean cargaServicios, BigDecimal codigoPkPresupuesto , int institucion);

	/**
	 * 
	 * @param detallePlanTratamiento
	 * @param estadosProgramasOservicios
	 * @param utilizaProgramas
	 * @return
	 */
	public ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServicios(
			BigDecimal detallePlanTratamiento,
			ArrayList<String> estadosProgramasOservicios,
			boolean utilizaProgramas, String porConfirmar,
			boolean cargaServicios,
			int institucion);

	/**
	 * 
	 * @param codigoProgramas
	 * @param codigoDetalle
	 * @param codigosAsignadosPresupuesto
	 * @param CodigoHallazgo
	 * @return
	 */
	public ArrayList<InfoProgramaServicioPlan> obtenerProgramasServiciosHallazgosPlanTramiento(
			double codigoHallazgo, String codigoProgramas,
			BigDecimal codigoDetalle, boolean utilizaProgramas,
			double codigoServicio, BigDecimal presupuesto,
			String codigosAsignadosPresupuesto, ArrayList<InfoNumSuperficiesPresupuesto> superficies);

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardarProgramasServicio(DtoProgramasServiciosPlanT dto,
			Connection con);

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public ArrayList<DtoProgramasServiciosPlanT> cargarProgramasServiciosPlanT(
			DtoProgramasServiciosPlanT dto);

	/**
	 * 
	 * @param dto
	 * @param con
	 * @param estadosAEliminar
	 * @param utilizaProgramas
	 * @return
	 */
	public boolean inactivarProgramasServicios(DtoProgramasServiciosPlanT dto,
			Connection con, ArrayList<String> estadosAEliminar,
			boolean utilizaProgramas);

	/**
	 * 
	 * @param codigoPkPlanTratamiento
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public ArrayList<InfoHallazgoSuperficie> obtenerHallazgosBOCA(
			BigDecimal codigoPkPlanTratamiento, int hallazgoOPCIONAL,
			String seccion, ArrayList<String> estadosProgramasOservicios,
			boolean utilizaProgramas, String porConfirmar,
			boolean cargaServicios, 
			BigDecimal codigoPkPresupuesto,
			int institucion);

	/**
	 * 
	 * @param codigoPrograma
	 * @param codigoDetPlan
	 * @return
	 */
	public int cargarOrdenServicio(int codigoPrograma,
			BigDecimal codigoDetPlan, Connection con, boolean inclusion);

	/**
	 * @param DtoDetallePlanTratamiento
	 *            dto
	 * */
	public double guardarDetPlanTratamiento(Connection con,
			DtoDetallePlanTratamiento dto);

	/**
	 * @param DtoPlanTratamientoOdo
	 *            dto
	 * */
	public double guardarPlanTratamiento(Connection con,
			DtoPlanTratamientoOdo dto);

	/**
	 * @param DtoPlanTratamientoOdo
	 *            dto
	 * */
	public double guardarOdontograma(Connection con, DtoOdontograma dto);

	/**
	 * @param DtoPlanTratamientoOdo
	 *            dto
	 * */
	public double guardarLogPlanTratamiento(Connection con,
			DtoLogPlanTratamiento dto);

	/**
	 * @param DtoLogDetPlanTratamiento
	 *            dto
	 * */
	public double guardarLogDetPlanTratamiento(Connection con,
			DtoLogDetPlanTratamiento dto);

	/**
	 * @param DtoLogProgServPlant
	 *            dto
	 * */
	public double guardarLogProgramasServiciosPlanT(Connection con,
			DtoLogProgServPlant dto);

	/**
		  * 
		  * 
		  */

	public double guardarDetalle(DtoDetallePlanTratamiento dto, Connection con);

	/**
		  * 
		  * 
		  */
	public boolean modicarEstadosDetalleProgServ(
			DtoProgramasServiciosPlanT dto, Connection con);

	/**
		  * 
		  * 
		  */
	public boolean modificar(DtoPlanTratamientoOdo dtoWhere,
			DtoPlanTratamientoOdo dtoNuevo, Connection con);

	/**
	  * 
	  * 
	  * 
	  */
	public String cargarMigradoPlanTratamiento(BigDecimal codigo);

	/**
		  * 
		  * 
		  * 
		  */
	public String cargarEstadoPlanTratamiento(BigDecimal codigo);

	/**
	 * Consulta el plan de tratamiento, recibe como parametro un
	 * DtoPlanTratamiento, evalua los campos llenos del dto y con estos realiza
	 * los filtros. El Dto debe tener el campo institucion lleno
	 * 
	 * @param Connection con
	 * @param DtoPlanTratamientoOdo parametros
	 * */
	public DtoPlanTratamientoOdo consultarPlanTratamiento(
			DtoPlanTratamientoOdo parametros);

	/**
	 * Consultar todos los planes de tratamiento del paciente sin importar
	 * ingreso.
	 * @param codigoPaciente Código del paciente
	 * @param institucion Institución a la cual pertenece el paciente
	 * @return {@link DtoPlanTratamientoOdo} con los datos del último plan de tratamiento
	 */
	public DtoPlanTratamientoOdo consultarPlanTratamientoPaciente(int codigoPaciente, int institucion);

	/**
	 * Consulta el plan de tratamiento, recibe como parametro un
	 * DtoPlanTratamiento, evalua los campos llenos del dto y con estos realiza
	 * los filtros. El Dto debe tener el campo institucion lleno
	 * 
	 * @param Connection
	 *            con
	 * @param DtoPlanTratamientoOdo
	 *            parametros
	 * */
	public DtoPlanTratamientoOdo consultarPlanTratamientoHistConf(
			DtoPlanTratamientoOdo parametros);

	/**
	 * Obtiene los hallazgos
	 * 
	 * @return
	 */
	public ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficies(
			DtoDetallePlanTratamiento parametros);

	/**
	 * Consulta los programas y servicios del detalle del plan de tratamiento
	 */
	public ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServicios(
			DtoProgramasServiciosPlanT parametros);

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public String cargarFechaLogPlant(DtoPlanTratamientoOdo dto);

	/**
	 * Obtiene el programa parametrizado por defecto para un hallazgo
	 * 
	 * @param DtoProgramasServiciosPlanT
	 * @return
	 */
	public InfoProgramaServicioPlan obtenerProgramaServicioParamHallazgo(
			DtoProgramasServiciosPlanT parametros);

	/**
	 * Carga los servicios de un programa Plant Tratamiento
	 * @return
	 */
	public ArrayList<InfoServicios> cargarServiciosDeProgramasPlanT(
			DtoProgramasServiciosPlanT parametros);

	/**
	 * se consulta los los codigos del plan de tratamiento segun el ArrayList de
	 * estado y el codigo del paciente
	 * 
	 * @param estados
	 * @return
	 */
	public ArrayList<BigDecimal> obtenerCodigoPlanTratamiento(
			int codigoPaciente, ArrayList<String> estados, String porConfirmar);

	/**
	 * 
	 * @param codigoPkPlanTratamiento
	 * @param seccion
	 * @param porConfirmar
	 * @param activo
	 * @return
	 */
	public ArrayList<InfoDatosInt> obtenerPiezas(
			BigDecimal codigoPkPlanTratamiento, String seccion,
			String porConfirmar, String activo);

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
			boolean utilizaProgramas, String porConfirmar, String activo);

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
			BigDecimal codigoPkPlanTratamiento, int pieza, String seccion,
			ArrayList<String> estadosProgramasOservicios,
			boolean utilizaProgramas, String porConfirmar, String activo);

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
			BigDecimal codigoPkPlanTratamiento, int hallazgoOPCIONAL,
			String seccion, ArrayList<String> estadosProgramasOservicios,
			boolean utilizaProgramas, String porConfirmar, String activo);

	/**
	 * 
	 * @param codPlanTratamiento
	 * @return
	 */
	public DtoLogPlanTratamiento consultarLogTratamiento(int codPlanTratamiento);

	/**
	 * 
	 * @param dto
	 * @param codigoPlanTratamiento
	 * @return
	 */
	public ArrayList<InfoDatosInt> obtenerPiezasInclusionesGarantias(
			DtoProgramasServiciosPlanT dto, 
			BigDecimal codigoPlanTratamiento,
			int institucion);

	/**
		  * 
		  * 
		  */
	public ArrayList<DtoLogPlanTratamiento> cargarLogs(
			DtoLogPlanTratamiento dtoWhere);

	/**
		  * 
		  * 
		  */
	public ArrayList<DtoLogProgServPlant> cargarLogProgramas(
			DtoLogProgServPlant dtoWhere);

	/**
	 * Carga las Superficies del Diente
	 * 
	 * @return
	 */
	public ArrayList<DtoSectorSuperficieCuadrante> cargarSuperficiesDiente(int institucion);

	/**
	 * Actualiza el estado activo del detalle del plan de tratamiento
	 * 
	 * @param Connection
	 *            con
	 * @param DtoProgramasServiciosPlanT
	 *            dto
	 */
	public boolean actualizarActivoDetallePlanTrat(Connection con,
			DtoDetallePlanTratamiento dto);

	/**
	 * Actualiza el estado activo de los programas del plan de tratamiento
	 * 
	 * @param Connection
	 *            con
	 * @param DtoProgramasServiciosPlanT
	 *            dto
	 */
	public boolean actualizarActivoProgServPlanTr(Connection con,
			DtoProgramasServiciosPlanT dto);

	/**
	 * CARGA EL CODIGO DEL PLAN DE TRATAMIENTO x ingreso y codigo de cita
	 * 
	 * @param ingreso
	 * @param estados
	 * @param codigoCita
	 * @return
	 */
	public int obtenerUltimoCodigoPlanTratamientoXIngresoXCita(int ingreso,
			ArrayList<String> estados, int codigoCita);

	/**
	 * 
	 * @param parametros
	 * @param estadosPlan
	 * @return
	 */
	public ArrayList<DtoPlanTratamientoOdo> consultarPlanTratamiento(
			Connection con, DtoPlanTratamientoOdo parametros,
			ArrayList<String> estadosPlan);

	/**
	 * Modificar el estado del plan de tratamiento
	 * 
	 * @param dto
	 * @param con
	 * @return
	 */
	public boolean modificarPlan(int institucion, Connection con,
			boolean utilizaProgramas);

	/**
	 * 
	 * @param dto
	 * @param con
	 * @return
	 */
	public boolean inactivarPlan(DtoPlanTratamientoOdo dto, Connection con);

	/**
	 * metodo que inserta el his_conf_plan_tratamiento
	 * 
	 * @param con
	 * @param valoracion
	 * @param evolucion
	 * @param codigoCita
	 * @param codigoPlanTratamiento
	 * @return
	 */
	public boolean insertHisConfPlanTratamiento(Connection con, int valoracion,
			int evolucion, int codigoCita, int codigoPlanTratamiento);

	/**
	 * metodo que inserta his_conf_det_plan_t
	 * 
	 * @param con
	 * @param valoracion
	 * @param evolucion
	 * @param codigoCita
	 * @param codigoPlanTratamiento
	 * @return
	 */
	public boolean insertHisConfDetallePlanTratamiento(Connection con,
			int valoracion, int evolucion, int codigoCita,
			int codigoPlanTratamiento);

	/**
	 * metodo que inserta his_conf_prog_serv_plan_t
	 * 
	 * @param con
	 * @param valoracion
	 * @param evolucion
	 * @param codigoCita
	 * @param codigoProgServPlanTrat
	 * @return
	 */
	public boolean insertHisConfProgServPlanTratamiento(Connection con,
			int valoracion, int evolucion, int codigoCita, int codigoDetPlanTrat);

	/**
	 * 
	 * @param con
	 * @param codigoPlanTrat
	 * @param valoracion
	 * @param evolucion
	 * @param usuario
	 * @return
	 */
	public boolean confirmarPlanTratamiento(Connection con, int codigoPlanTrat,
			int valoracion, int evolucion, String usuario);

	/**
	 * 
	 * @param con
	 * @param codigoPlanTrat
	 * @param valoracion
	 * @param evolucion
	 * @param usuario
	 * @return
	 */
	public boolean confirmarDetallePlanTratamiento(Connection con,
			int codigoPlanTrat, int valoracion, int evolucion, String usuario);

	/**
	 * 
	 * @param con
	 * @param codigoDetPlanTrat
	 * @param valoracion
	 * @param evolucion
	 * @param usuario
	 * @return
	 */
	public boolean confirmarProgServPlanTratamiento(Connection con,
			int codigoDetPlanTrat, int valoracion, int evolucion, String usuario);

	/**
	 * Actualiza informacion en detalla de plan de tratamiento
	 * 
	 * @param Connection
	 *            con
	 * @param DtoDetallePlanTratamiento
	 *            dto
	 * */
	public boolean actualizarDetPlanTratamiento(Connection con,
			DtoDetallePlanTratamiento dto);

	/**
	 * Obtiene los hallazgos para las seccion de Otros y Boca
	 * 
	 * @return
	 */
	public ArrayList<InfoDetallePlanTramiento> obtenerHallazgosSuperficiesSeccionOtrayBoca(
			DtoDetallePlanTratamiento parametros);

	/**
	 * obtiene el listado de programas o servicios
	 * 
	 * @param DtoProgramasServiciosPlanT
	 *            parametros
	 * */
	public ArrayList<InfoProgramaServicioPlan> obtenerListadoProgramasServicio(
			DtoProgramasServiciosPlanT parametros);

	/**
	 * Carga la informacion de un programa/servicio especifico
	 * 
	 * @param DtoProgramasServiciosPlanT
	 *            parametros
	 * */
	public InfoProgramaServicioPlan obtenerInfoProgramaServicios(
			DtoProgramasServiciosPlanT parametros);

	/**
	 * 
	 * @param codPkDetallePlanT
	 * @param codPrograma
	 * @param codServicio
	 * @return
	 */
	public int obtenerCodPkLogServPlanT(int codPkDetallePlanT, int codPrograma,
			int codServicio);

	/**
	 * metodo de actualizacion de estados programas servicios de otra
	 * evoluciones del plan de tratamietno
	 * 
	 * @param con
	 * @param casoActualizacion
	 * @param estado
	 * @param aplica
	 * @param usuario
	 * @param codigoDetPlanTrat
	 * @param codigoPrograma
	 * @param codigoServicio
	 * @param porConfirmar
	 * @return boolean
	 */
	public boolean actualizacionEstadosPSOtrasEvoluciones(Connection con,
			char casoActualizacion, String estado, String aplica,
			String estPrograma, String usuario, int codigoDetPlanTrat,
			int codigoPrograma, int codigoServicio, int motivo, int codigoCita,
			int valoracion, int evolucion, String porConfirmar);

	/**
	 * Método que carga el encabezado del historial de ocnfirmacion del plan de
	 * tratamiento
	 * @deprecated
	 * @param con
	 * @param infoPlanTrat
	 */
	public void consultarHisConfPlanTratamiento(Connection con,
			InfoPlanTratamiento infoPlanTrat);

	/**
	 * CARGA LAS PIEZAS del historial de confirmacion
	 * 
	 * @param codigoPkPlanTratamiento
	 * @return
	 */
	public ArrayList<InfoDatosInt> obtenerPiezasHisConf(Connection con,
			InfoPlanTratamiento infoPlanTrat, String seccion);

	/**
	 * Obtiene los hallazgos
	 * 
	 * @return
	 */
	public ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesHisConf(
			Connection con, DtoDetallePlanTratamiento parametros);

	/**
	 * 
	 * Consulta los programas y servicios del detalle del plan de tratamiento
	 * Mirando las tablas de historial de confirmacion
	 */
	public ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServiciosHisConf(
			Connection con, DtoProgramasServiciosPlanT parametros);

	/**
	 * Obtiene los hallazgos para las seccion de Otros y Boca
	 * 
	 * @return
	 */
	public ArrayList<InfoDetallePlanTramiento> obtenerHallazgosSuperficiesSeccionOtrayBocaHisConf(
			Connection con, DtoDetallePlanTratamiento parametros);

	/**
	 * verificar se el o los servicios del programa, alguno de ellos se
	 * encuentra agendado
	 * 
	 * @param detPlanTratamiento
	 * @param codigoPrograma
	 * @param codigoServicio
	 * @param codigoCita
	 * @return
	 */
	public boolean verificarServiciosAgendados(int detPlanTratamiento,
			int codigoPrograma, int codigoServicio, int codigoCita);

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean guardarServArtIncPlant(Connection con,
			DtoServArtIncCitaOdo dto);

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public ArrayList<DtoServArtIncCitaOdo> cargarServArtIncPlanT(
			DtoServArtIncCitaOdo dto, BigDecimal codigoDetallePlanTratamiento,
			int cargarServArtIncPlanT);

	/**
	 * 
	 * @param codigoDetallePlanTratamiento
	 * @param utilizaProgramas
	 * @return
	 */
	public String obtenerEstadoProgramaServicioPlanTratamiento(
			BigDecimal codigoDetallePlanTratamiento, boolean utilizaProgramas,
			double codigoPkProgramaOServicio);

	/**
	 * 
	 * @param listapkProgramasServiciosPlant
	 * @return
	 */
	public boolean validarCargos(
			ArrayList<String> listapkProgramasServiciosPlant);

	/**
	 * 
	 * @param codigoPrograma
	 * @return
	 */
	public ArrayList<InfoServicios> cargarServiciosParamPrograma(
			int codigoPrograma, String codigoTarifarioServ);

	/**
	 * 
	 * @param planTratamiento
	 * @param programa
	 * @param piezaDental
	 * @param superficie
	 * @param hallazgo
	 * @return
	 */
	public ArrayList<InfoServicios> cargarServiciosDeProgramasPlanT(
			BigDecimal planTratamiento, Double programa, Integer piezaDental,
			Integer superficie, Integer hallazgo, int institucion);

	/**
	 * 
	 * @param planTratamiento
	 * @param pieza
	 * @param hallazgo
	 * @param superficie
	 * @param seccion
	 * @return
	 */
	public BigDecimal obtenerDetPlanTratamiento(BigDecimal planTratamiento,
			BigDecimal pieza, BigDecimal hallazgo, BigDecimal superficie,
			String seccion);

	/**
	 * Método para cargar el odontograma de valorción
	 * @param codigoPk
	 * @param esValoracion
	 * @return DtoOdontograma
	 */
	public DtoOdontograma cargarOdontograma(BigDecimal codigoPk, boolean esValoracion);
	
	/**
	 * 
	 * Método que verifica si el servicio se encuentra asignado a otra cita
	 * 
	 * @param con
	 * @param codigoCita
	 * @param codigoProgHallPieza
	 * @param servicio
	 * @param codigoPaciente
	 * @return true en caso de estar asociado,m false de lo contrario
	 */
	public boolean estaServicioAsociadoAOtraCita(Connection con, int codigoCita, int codigoProgHallPieza, int servicio, int codigoPaciente);

	/**
	 * Método para actualizar un programa ( Inclusion )
	 * @param con
	 * @param dtoPrograma
	 * @return
	 */
	public boolean actualizarInclusionProgServPlanTr(Connection con,DtoProgramasServiciosPlanT dtoPrograma);

	/**
	 * 
	 * @param con
	 * @param codigoPkPlanTratamiento
	 * @param utilizaProgramas
	 * @return
	 */
	public boolean existenProgramasServiciosContratadosNoTerminados(Connection con, BigDecimal codigoPkPlanTratamiento, boolean utilizaProgramas);

	/**
	 * 
	 * @param cuenta
	 * @return
	 */
	public InfoDatosDouble obtenerPlanTratamientoXCuenta(BigDecimal cuenta);

	
	

	/**
	 * Lista los programas seg&uacute;n los par&aacute;metros dados
	 * @param DtoProgramasServiciosPlanT dtoBusqueda Dto en el cual se encapsulan los par&acute;metros de b&uacute;squeda
	 * @return {@link ArrayList}<{@link InfoProgramaServicioPlan}> Lista los programas/servicios encontrados con los parámetros dados
	 */
	public ArrayList<InfoProgramaServicioPlan> listarProgramaServicioParamHallazgo(DtoProgramasServiciosPlanT dtoBusqueda);
	
	/**
	 * Verifica la existencia de almenos una pieza activa
	 * @param codigoPkPlanTratamiento C&oacute;digo del plan de tratamiento
	 * @param piezaDental C&oacute;digo de la pieza dental
	 * @param seccion 
	 * @return true en caso de tener elementos activos
	 */
	public boolean tieneAlgunaSuperficieActiva(BigDecimal codigoPkPlanTratamiento, int piezaDental, String seccion);
	
	
	/**
	 * CARGAR LOS HALLAZGOS DE LA SUPERFICIE 
	 * RETORNA UN OBJETO INFOHALLAZGO SUPERFICIE (Utilizado para la construccion Grafica del plan de Tratamiento)
	 * @param dtoPlanTratamiento
	 * @param dtoDetallePlanT
	 * @param dtoProgramasServicios
	 * @param estadosProgramasOservicios
	 * @param utilizaProgramas
	 * @param porConfirmar
	 * @param cargaServicios
	 * @param codigoPkPresupuesto
	 * @param institucion
	 * @return
	 */
	public ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesDTO(DtoPlanTratamientoOdo dtoPlanTratamiento , DtoDetallePlanTratamiento dtoDetallePlanT,  DtoProgramasServiciosPlanT dtoProgramasServicios , ArrayList<String> estadosProgramasOservicios, boolean utilizaProgramas, String porConfirmar, boolean cargaServicios , BigDecimal codigoPkPresupuesto  ,int institucion);

	
	
	/**
	 * CARGA UNA LISTA DE HISTORICOS DEL PLAN DE TRATAMIENTO
	 * @param dto
	 * @return
	 */
	public ArrayList<DtoPlanTratamientoOdo> consultarPlanTratamientoHistorico(DtoPlanTratamientoOdo dto);
	
	
	/**
	 *OBTENER PIEZAS DEL DETALLE PLAN TRATAMIENTO HISTORICO 
	 * @param dtoDetallePlan
	 * @return
	 */
	public ArrayList<InfoDatosInt> obtenerPiezasHistorico(DtoDetallePlanTratamiento dtoDetallePlan);
	
	
	
	/**
	 * OBTENER LAS PIEZAS Y SUPERFICIES DE LOS HISTORICOS DEL PLAN DE TRATAMIENTO
	 * @author Edgar Carvajal
	 * @param dtoPlanTratamiento
	 * @param dtoDetallePlan
	 * @param utilizaProgramas
	 * @param cargaServicios
	 * @return
	 */
	public ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesHistoricos(DtoPlanTratamientoOdo dtoPlanTratamiento, DtoDetallePlanTratamiento dtoDetallePlan, boolean utilizaProgramas, DtoProgramasServiciosPlanT dtoProgramaServicios , int institucion );
	
	
	
	/**
	 * METODO QUE OBTIENE  LOS HALLAZGOS EN BOCA HISTORICOS DEL PLAN DE TRATAMIENTO
	 * @author Edgar Carvajal
	 * @param dtoPlanTratamiento
	 * @param dtoDetallePlan
	 * @param utilizaProgramas
	 * @return
	 */
	public  ArrayList<InfoHallazgoSuperficie> obtenerHallazgosBOCAHistoricos(DtoPlanTratamientoOdo dtoPlanTratamiento , DtoDetallePlanTratamiento dtoDetallePlan ,  boolean utilizaProgramas, DtoProgramasServiciosPlanT dtoProgramasServicios, int institucion);
	
	
	/**
	 * OBTENER LAS PIEZAS INCLUSION GARANTIA HISTORICOS PLAN DE TRATAMIENTO
	 * @author Edgar Carvajal
	 * @param dto
	 * @param codigoPlanTratamiento
	 * @return
	 */
	public  ArrayList<InfoDatosInt> obtenerPiezasInclusionesGarantiasHistorico(DtoProgramasServiciosPlanT dto, BigDecimal codigoPlanTratamiento  );
	
	
	
	/**
	 * 
	 * @param dtoPlanTratamiento
	 * @param dtoDetallePlanT
	 * @param dtoProgramasServicios
	 * @param utilizaProgramas
	 * @return
	 */
	public ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesDTOHistoricos(DtoPlanTratamientoOdo dtoPlanTratamiento , DtoDetallePlanTratamiento dtoDetallePlanT,  DtoProgramasServiciosPlanT dtoProgramasServicios,  boolean utilizaProgramas , int institucion);

	/**
	 * Elimina todas las relaciones entre programas de N superficies
	 * @param con Conexi&oacute;n con la BD
	 * @param planTratamiento C&oacute;digo del plan de tratamiento para el cual se van a eliminar las relaciones
	 * @param seccion Secci&oacute;n a la cual pertenecen los las relaciones a eliminar
	 * @return retorna true en caso de &eacute;xito, false en caso de error
	 */
	public boolean eliminarRelacionSuperficiesProgramas(Connection con, int planTratamiento, String seccion);

	
	/**
	 * Guarda las relaciones de los programas con las superficies
	 * @param con Conexi&oacute;n con la base de datos.
	 * @param listaEncabezados {@link ArrayList}<{@link DtoProgHallazgoPieza}>} Listado con las relaciones a guardar,
	 * los encabezados son de tipo {@link DtoProgHallazgoPieza} y los detalles de las superficies
	 * son de tipo {@link DtoSuperficiesPorPrograma}.
	 * @author Juan David Ram&iacute;rez
	 * @since 2010-05-11
	 */
	public ArrayList<DtoProgHallazgoPieza> guardarRelacionesProgSuperficies(Connection con, ArrayList<DtoProgHallazgoPieza> listaEncabezados);

	/**
	 * Consulta un registro específico del plan de tratamiento con los parámetros
	 * pasados a través del DTO.
	 * @param dto {@link DtoDetallePlanTratamiento} DTO con los par&aacute;metros de b&uacute;squeda
	 * @param con {@link Connection} conexi&oaute;n con la BD
	 * @param buscarTerminado TODO
	 * @author Juan David Ram&iacute;rez.
	 * @since 2010-05-11 
	 */
	public boolean consultarDetPlanTratamiento(DtoDetallePlanTratamiento dto, Connection con, boolean buscarTerminado);

	/**
	 * Obtener el n&uacute;mero de superficies que fueron relacionadas en un programa de N superficies.
	 * @param hallazgo C&oacute;digo del hallazgo.
	 * @param programa C&oacute;digo del programa buscado.
	 * @param pieza C&oacute;digo de la pieza dental a la cual se le vincul&oacute; el hallazgo. 
	 * @param superficie C&oacute;digo de la superficie evaluada.
	 * @param codigoPlanTratamiento C&oacute;digo del plan de tratamiento evaluado
	 * @return N&uacute;mero de superficies relacionadas, si no existe relaci&oacute;n en BD
	 * indica que es un diente, por lo tanto retorna 0
	 * @author Juan David Ram&uacute;rez
	 * @since 2010-05-14
	 */
	public int consultarNumeroSuperficiesPorPrograma(int hallazgo, int programa, int pieza, int superficie, int codigoPlanTratamiento);
	
	
	
	
	
	
	/**
	 * CARGA EL INGRESO CON EL RESPECTIVO PLAN DE TRATAMIENTO
	 * @author Edgar Carvajal
	 * @param infoDto
	 * @return
	 */
	public  ArrayList<InfoIngresoPlanTratamiento> cargarIngresosPlanTratamiento(InfoIngresoPlanTratamiento infoDto );
	
	
	
	
	
	/**
	 * 
	 * @param codigoPresupuesto
	 * @return
	 */
	public  String  cargarNombreUsuarioModificaPlanTratamiento(BigDecimal codigoPresupuesto );
	
	
	
	
	/**
	 * MODIFICAR EL DETALLE DEL PLAN DE TRATAMIENTO
	 * METODO QUE MODIFICA EL PLAN DE TRATAMIENTO PARA REALIZAR LA TRAZABILIDAD DEL ODONTOGRAMA
	 * @author Edgar Carvajal
	 * @param dto
	 * @return
	 */
	public boolean   modificarDetallePlanTratamiento (DtoDetallePlanTratamiento dto, Connection con);
	
	
	
	/**
	 * CARGA LAS PIEZAS  DE LOS HISTORICOS DEL PLAN DE TRATAMIENTO
	 * @author Edgar Carvajal Ruiz
	 * @param dtoPlan
	 * @param dtoDet
	 * @return
	 */
	public  ArrayList<InfoDatosInt> obtenerPiezasHistoricoPlanTratamiento(DtoPlanTratamientoOdo dtoPlan, DtoDetallePlanTratamiento dtoDet);
	
	
	
	/**
	 * OBTENER LISTA DE HALLAZGOS SUPERFICIE HISTORICOS PLAN 
	 * BUSCAR LOS HALLAZGOS DE UNA SUPEFICIE EL  LOS HIISTORICOS DEL DETALLE DEL PLAN DE TRATAMIENTO
	 * @author Edgar Carvajal Ruiz
	 * @param dtoDetalle
	 * @return
	 */
	public  ArrayList<InfoHallazgoSuperficie> obtenerHallazgosSuperficiesHistoricos(DtoDetallePlanTratamiento dtoDetalle);
	
	
	
	
	
	/**
	 * ESTE ALGORITMO ES TOMADO DEL  METODO obtenerProgramasOServicios(DtoProgramasServiciosPlanT).
	 * SIRVE PARA CARGAR PROGRAMAS Y SERVICIOS DEL HISTORICO DEL PLAN DE TRATAMIENTO
	 * @author Edgar Carvajal Ruiz
	 * @param dtoProgramaServicios
	 * @return
	 */
	public  ArrayList<InfoProgramaServicioPlan> obtenerProgramasOServiciosHistoricos(DtoProgramasServiciosPlanT dtoProgramaServicios);
	
	
	
	/**
	 * METODO PARA CARGAR HALLAGOS SUPERFICIES SECCION OTROS Y BOCA
	 *  
	 * @author Edgar Carvajal Ruiz
	 * @param parametros
	 * @return
	 */
	public  ArrayList<InfoDetallePlanTramiento> obtenerHallazgosSuperficiesSeccionOtrayBocaHistoricos(DtoDetallePlanTratamiento parametros);

	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoOdongrama
	 * @param dtoPlanTrata
	 * @param institucion
	 * @return
	 */
	public  ArrayList<InfoProgramaServicioPlan> consultarProximaCitaProgramasServicios(DtoOdontograma dtoOdongrama , DtoPlanTratamientoOdo dtoPlanTrata ,int institucion );
	
	
	
	
	/**
	 * CARGAR MAXIMO CODIGO PK DEL LOG PLANTRATAMIENTO
	 * @author Edgar Carvajal Ruiz
	 * @param dtoPlan
	 * @return
	 */
	public  BigDecimal cargarMaximoCodigoLogPlantratamiento(DtoPlanTratamientoOdo dtoPlan, Connection con );
	
	
	
	
	/**
	 * Carga el último plan de tratamiento del paciente
	 * 
	 * @param codigoPaciente
	 * @return
	 */
	public BigDecimal obtenerUltimoCodigoPlanTratamiento (int codigoPaciente);
	
	 /**
	 * 
	 * Método para obtener el ultimo codigo pk de la cita de un programa servicio del plan tratamiento dado el codigo_pk de la tabla programas servicios plan t
	 * También verifica si el programa o servicio Excluido tiene motivo
	 *  
	 * @param codigoPkProgServPlanT
	 * @param fechaFormatoAppNOREQUERIDA
	 * @param estado
	 * 
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public BigDecimal obtenerCodigoPkUltimaCitaProgServPlanT(BigDecimal codigoPkProgServPlanT, String fechaFormatoAppNOREQUERIDA, ArrayList<String> estado);
	
	
	
	/**
	 * Método que se encarga de consultar si existen registros de detalle
	 * plan tratamiento asociados a una cita específica y que no tienen asociada
	 * una clasificación
	 * 
	 * 
	 * @param codigoCita
	 * @return
	 */
	public boolean existeDetPlanTratamientoSinClasificacion (int codigoCita);
	
	
	/**
	 * Método que obtiene el codigo de los detalles de plan de tratamiento asociados a un
	 * programa Hallazgo pieza específico.
	 * 
	 * @param programaHallazgoPieza
	 * @return
	 */
	public int obtenerDetPlanTratamientoXProgramaHallazgoPieza(int programaHallazgoPieza);
	
}