package com.princetonsa.dao.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import util.InfoDatosDouble;
import util.odontologia.InfoConvenioContratoPresupuesto;
import util.odontologia.InfoPresupuestoPrecontratado;

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


public interface PresupuestoOdontologicoDao 
{

/*
 * PRESUPUESTO
 */

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardarPresupuesto(DtoPresupuestoOdontologico dto , Connection con);

	/**
	 * 
	 * @param dto
	 * @param estadosPresupuesto 
	 * @return
	 */
	public ArrayList<DtoPresupuestoOdontologico> cargarPresupuesto( Connection con, 		DtoPresupuestoOdontologico dto, ArrayList<String> estadosPresupuesto,boolean estadoNotIn);

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean modificarPresupuesto(DtoPresupuestoOdontologico dto , Connection con , ArrayList<String> arrayEstados);

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean eliminarPresupuesto(DtoPresupuestoOdontologico dto);

/*------------------------------------------------------
 *  TOTALES DEL LOS CONVENIOS DEL PRESUPUESTO
 * **************************************************
 */

	/**
  * 
  */
	public ArrayList<DtoPresupuestoTotalConvenio> cargarTotalesPresupuetoConvenio(DtoPresupuestoTotalConvenio dto );

	
/*------------------------------------------------------------------------------------------------------------------------------------
 * METODOS PARA El DETALLE PRESUPUESTO ODONTOLOGICOS PROGRAMA - SERVICIOS
 * **********************************************************************************************************************************
 */
	
	
	public  ArrayList<DtoPresuOdoProgServ> cargarPresupuestoOdoProgServ ( DtoPresuOdoProgServ dto, Connection con, DtoPresupuestoPiezas dtoPieza);
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  double guardarPresupuestoOdoProgramaServicio(DtoPresuOdoProgServ dto , Connection con);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  boolean modificarPresupuestoProgramaServicio( DtoPresuOdoProgServ dto, Connection con );
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  boolean eliminarPresupuestoProgramaServicio( DtoPresuOdoProgServ dto , Connection con);
	
 
		
/*---------------------------------------------------------------------------------------------------------------
 * PRESUPUESTO ODONTOLOGIA CONVENIO 
 * *****************************************************************************************************************************
 * 
 */
	/**	
	 * 
	 */
	public  double guardarPresupuestoOdoConvenio(DtoPresupuestoOdoConvenio dto , Connection con);
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  ArrayList<DtoPresupuestoOdoConvenio> cargarPresupuestoConvenio ( DtoPresupuestoOdoConvenio dto, Connection con);
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  boolean modificarPresupuestoConvenio( DtoPresupuestoOdoConvenio dto  , Connection con );
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  boolean eliminarPresupuestoOdoConvenio( DtoPresupuestoOdoConvenio  dto , Connection con);
	
	
	
	
/*
 *-------------------------------------------------------------------------------------------------------------------------------
 * PRESUPUESTO PIEZAS ODONTOLOGICAS 
 * ******************************************************************************************************************************
 */
	/**
	 * 
	 */
	public  double guardarPresupuestoPiezas(DtoPresupuestoPiezas  dto , Connection con);
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  ArrayList<DtoPresupuestoPiezas> cargarPresupuestoPieza ( DtoPresupuestoPiezas dto, boolean inactivarNoPendientesPlanTratamiento, boolean utilizaProgramas);
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  boolean modificarPresupuestoPieza( DtoPresupuestoPiezas dto );
	/**	
	 * 
	 * @param dto
	 * @return
	 */
	public  boolean eliminarPresupuestoPieza( DtoPresupuestoPiezas  dto , Connection con ) ;

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  ArrayList<DtoLogPresupuestoOdontologico> cargarLogPresupuesto ( DtoLogPresupuestoOdontologico dto);
	
	
	/***
	 * 
	 * 
	 * 
	 */
	public  ArrayList<DtoPresupuestoOdontologicoDescuento> cargarPresupuestoDescuentos ( DtoPresupuestoOdontologicoDescuento dto, ArrayList<String> listaEstados);
	
	/***
	 * 
	 * 
	 * 
	 */
	public  double guardarPresupuestoDescuento(DtoPresupuestoOdontologicoDescuento dto, Connection con);
	
	/**
	 *
	 * 
	 * 
	 */
	
	public  boolean eliminarPresupuestoDescuento( DtoPresupuestoOdontologicoDescuento  dto);

	/**
	 * 
	 * @param piezaOPCIONAL
	 * @param superficieOPCIONAL
	 * @param hallazgoREQUERIDO
	 * @param utilizaProgramas 
	 * @param codigoPkProgramaServicio 
	 * @param codigoPkPresupuesto 
	 * @return
	 * 
	 */
		public BigDecimal existeProgramaServicioPlanTratamientoEnPresupuesto(int piezaOPCIONAL, int superficieOPCIONAL, int hallazgoREQUERIDO, BigDecimal codigoPkProgramaServicio, boolean utilizaProgramas, BigDecimal codigoPkPresupuesto, String seccion);
	
	/**
	 * 
	 * 
	 */
	
	public boolean aplicaPromocion(int contrato);
	
	/**
	 * 
	 * 
	 * 
	 */
	
	public  int existeProgramaServicioPresupuestoEnPlanTratamiento(int piezaOPCIONAL, int superficieOPCIONAL, int hallazgoREQUERIDO,BigDecimal codigoPkProgramaServicio, boolean utilizaProgramas, BigDecimal codigoPkPlan, String estadoOPCIONAL);
	
	/**
	 * 
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public ArrayList<InfoConvenioContratoPresupuesto> obtenerConveniosContratosPresupuesto(BigDecimal codigoPkPresupuesto);
	
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
	public  String consultaReporteDetalle(int codigoPresupuesto, String tipoRelacion) ;
	
	/**
	 * 
	 * 
	 */
	public  String consultaReporteContratar(String tipoRelacion , int codigoPresupuesto);
	 /**
	  * 
	  * 
	  * 
	  */
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
																boolean eliminarFilaProg);

	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @param idSesionOPCIONAL
	 * @param igualSession 
	 * @return
	 */
	public DtoConcurrenciaPresupuesto estaEnProcesoPresupuesto(Connection con,int cuenta, String idSesionOPCIONAL, boolean igualSession);

	/**
	 * 
	 * @param idCuenta
	 * @param loginUsuario
	 * @param idSesion
	 * @param esFlujoInclusiones
	 * @return
	 */
	public boolean empezarBloqueoPresupuesto(int idCuenta, String loginUsuario,
			String idSesion, boolean esFlujoInclusiones);

	/**
	 * 
	 * @param con
	 * @return
	 */
	public int cancelarTodosLosProcesosDePresupuesto(Connection con);

	/**
	 * 
	 * @param idCuenta
	 * @param idSesion
	 * @return
	 */
	public boolean cancelarProcesoPresupuesto(int idCuenta, String idSesion);
	
	/**
	 * Método par aobtener el codigo del presupuesto de un plan de tratamiento
	 * por un estado específico
	 * @param codigoPlanTratamiento
	 * @param estado
	 * @return
	 */
	public BigDecimal consultarCodigoPresupuestoXPlanTratamiento(BigDecimal codigoPlanTratamiento,String estado);
	
	
	
	/*
	 *METODOS PARA LA TABLA PRESU_ODON_CONV_DET_SER_PROG 
	 */
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  ArrayList<DtoPresupuestoDetalleServiciosProgramaDao> cargarPresupuestoDetalleServicosPrograma(DtoPresupuestoDetalleServiciosProgramaDao dto );
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double insertPresupuestoDetalleServiciosProgramaDao(DtoPresupuestoDetalleServiciosProgramaDao dto, Connection con);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  boolean eliminarPresupuestoDetalleServiciosProgramaDao(DtoPresupuestoDetalleServiciosProgramaDao dto, Connection con );
	
	/**
	 * 
	 * @param codigoPresupuesto
	 * @return
	 */
	public BigDecimal obtenerTotalPresupuestoParaDescuento(BigDecimal codigoPresupuesto);

	/**
	 * 
	 * @param codigoPresupuesto
	 * @return
	 */
	public BigDecimal obtenerTotalPresupuestoSinDctoOdon(BigDecimal codigoPresupuesto);
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean modificarDescuentoPresupuesto(Connection con, DtoPresupuestoOdontologicoDescuento dto);
	
	/**
	 * 
	 * @param con
	 * @param fhuModifica
	 * @param estado
	 * @param codigoPk
	 * @return
	 */
	public boolean cambiarEstadoDescuentoPresupuesto(Connection con, DtoInfoFechaUsuario fhuModifica, String estado, BigDecimal codigoPk);
	
	/**
	 * 
	 * @param con
	 * @param codigoPkPresupuesto
	 * @param loginUsuario
	 * @param codigoMotivo
	 * @return
	 */
	public boolean anularDescuentoPresupuesto(Connection con, BigDecimal codigoPkPresupuesto, String loginUsuario, double codigoMotivo);
	
	/**
	 * 
	 * @param ingreso
	 * @return
	 */
	public ArrayList<InfoPresupuestoPrecontratado> cargarPresupuestosPresontratados(int ingreso, BigDecimal presupuestoNotIn);
	
	/**
	 * 
	 * @param con
	 * @param codigoPk
	 * @param estadoNuevo
	 * @return
	 */
	public boolean cambiarEstadoPresupuesto(Connection con, BigDecimal codigoPk, String estadoNuevo);
	
	/**
	 * 
	 * @param con
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public boolean modificarIndicativoContratadoPresupuesto(Connection con, BigDecimal codigoPkPresupuesto);

	/**
	 * Verifica si el paciente tiene presupuesto filtrado por los estados pasados por parámetros
	 * @param codigoPaciente
	 * @param estados
	 */
	public boolean existePresupuestoXPaciente(int codigoPaciente, ArrayList<String> estados);
	
	/**
	 * 
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public boolean reversarAnticiposPresupuestoContratado(Connection con, BigDecimal codigoPkPresupuesto);
	
	/**
	 * 
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public double obtenerPorcentajeDctoOdonContratadoPresupuesto(BigDecimal codigoPkPresupuesto);
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @param utilizaProgramas
	 * @return
	 */
	public BigDecimal insertarPlanTtoPresupuesto(Connection con, DtoPlanTratamientoPresupuesto dto, boolean utilizaProgramas);
	
	/**
	 * 
	 * @param con
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public boolean eliminarPlanTtoPresupuesto(Connection con, BigDecimal codigoPkPresupuesto);

	/**
	 * 
	 * @param con 
	 * @param codigoPkProgramaServicioPlanTto
	 * @param detPlanTratamiento
	 * @param programa
	 * @param servicio
	 * @param codigoPkPresupuesto 
	 * @return
	 */
	public boolean actualizarPlanTtoPresupuestoProgServ(Connection con, BigDecimal codigoPkProgramaServicioPlanTto,BigDecimal detPlanTratamiento, InfoDatosDouble programa,int servicio, BigDecimal codigoPkPresupuesto);

	/**
	 * Método que modifica los bonos por convenio
	 * 
	 * @param con
	 * @param dtoConvenio
	 * @return
	 */
	public boolean modificarBonos(Connection con, DtoPresupuestoOdoConvenio dtoConvenio);

	/**
	 * 
	 * @param con
	 * @param dtoConvenio
	 * @return
	 */
	public boolean modificarPromociones(Connection con, DtoPresupuestoOdoConvenio dtoConvenio);
	
	/**
	 * metodo para cargar las citas odontologicas que se deben cancelar para poder cancelar un presupuesto
	 * @param codigoPaciente
	 * @return
	 */
	public ArrayList<DtoCitasPresupuestoOdo> cargarCitasPresupuestoOdontologico(int codigoPaciente);

	/**
	 * 
	 * @param utilizaProgramas
	 * @param codigoPKPresupuesto
	 * @return
	 */
	public boolean puedoTerminarPresupuesto(boolean utilizaProgramas,BigDecimal codigoPKPresupuesto);
	
	/**
	 * 
	 * @param codigoPkPlanTratamiento
	 * @return
	 */
	public double obtenerDctoOdontologicoPresupuestoXPlan(BigDecimal codigoPkPlanTratamiento);
	
	/**
	 * CARGAR DTO LOG PRESUPUESTO 
	 * @author Edgar Carvajal Ruiz
	 * @param dto
	 * @return
	 */
	public DtoLogPrespuesto cargarDtoLogPresupuesto(DtoLogPrespuesto dto);
	
	
	/**
	 * METODO QUE GUARDA LA CLAUSULA DE CONTRATO 
	 * NOTA SE COLOCO DE UN MUCHOS CON  PRESUPUESTO CONTRATADO PARA LA FLEXIBILIDAD ->PERO SU FUNCIONAMIENTO DE UNO A UNO 
	 * TAMBIEN EN INSTITUCION SE PUEDE COLOCAR MUCHOS CONTRATOS ODONTOLOGICOS
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param con
	 * @param dto
	 * @return
	 */
	public  boolean guardarContratoPresupuestoClausula(Connection con, DtoPresuContratoOdoImp dto);

	/**
	 * Metodo para consultar el valor del descuento que tiene un Presupuesto
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public BigDecimal obtenerValorDctoOdontologicoPresupuesto(BigDecimal codigoPkPresupuesto);

	
	/**
	 * 
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public BigDecimal obtenerTotalPresupuestoConDctoOdon(BigDecimal codigoPkPresupuesto);
	
	
	
	/**
	 *	Metodo que cargar el Valor del Descuento para el Presupuesto.
	 *	Recibe un el codigo_pk del Presupuesto y retorna el Valor de Descuento 
	 * @author Edgar Carvajal Ruiz
	 * @param codigoPkPresupuesto
	 */
	public  BigDecimal cargarValorPresupuesto( BigDecimal codigoPkPresupuesto );
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param paciente
	 * @param viaIngreso
	 * @param institucion
	 * @return
	 */
	public List<DtoHistoricoIngresoPresupuesto> listaIngresoHistoricosPresupuesto(final  int paciente , final int viaIngreso , final int institucion );

	public ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> obtenerPresupuestoOdoContratadoPromo(
			DtoReportePresupuestosOdontologicosContratadosConPromocion dto);

	
	/**
	 * Método que devuelve el valor inicial contratado en el presupuesto
	 * 
	 * @param codigoPresupuesto
	 * @return valor inicial del presupuesto
	 */
	public BigDecimal obtenerValorContratadoInicialPresupuesto(	long codigoPresupuesto);
	
}	