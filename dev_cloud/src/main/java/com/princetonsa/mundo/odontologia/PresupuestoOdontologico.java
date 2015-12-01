package com.princetonsa.mundo.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.Utilidades;
import util.odontologia.InfoConvenioContratoPresupuesto;
import util.odontologia.InfoPermisosPresupuesto;
import util.odontologia.InfoPresupuestoPrecontratado;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
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
import com.princetonsa.dto.odontologia.DtoPresupuestoTotalesContratadoPrecontratado;
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratadosConPromocion;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.ConveniosIngresoPaciente;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConveniosIngresoPacienteServicio;

/**
 * 
 * @author axioma
 *
 */
public class PresupuestoOdontologico 
{
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardarPresupuesto(DtoPresupuestoOdontologico dto , Connection con){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().guardarPresupuesto(dto, con);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoPresupuestoOdontologico> cargarPresupuesto(
			DtoPresupuestoOdontologico dto){
		Connection con= UtilidadBD.abrirConexion();
		ArrayList<DtoPresupuestoOdontologico> cargarPresupuesto = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarPresupuesto(con, dto, new ArrayList<String>(),Boolean.FALSE);
		UtilidadBD.cerrarObjetosPersistencia(null, null, con);
		return cargarPresupuesto;
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoPresupuestoOdontologico> cargarPresupuesto(
			DtoPresupuestoOdontologico dto, ArrayList<String> estadosPresupuesto){
		Connection con= UtilidadBD.abrirConexion();
		ArrayList<DtoPresupuestoOdontologico> cargarPresupuesto = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarPresupuesto(con,dto, estadosPresupuesto, Boolean.FALSE);
		UtilidadBD.cerrarObjetosPersistencia(null,null, con);
		return cargarPresupuesto;
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoPresupuestoOdontologico> cargarPresupuesto(DtoPresupuestoOdontologico dto, ArrayList<String> estadosPresupuesto, boolean estadoNotIn){
		Connection con= UtilidadBD.abrirConexion();
		ArrayList<DtoPresupuestoOdontologico> cargarPresupuesto =DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarPresupuesto(con,dto, estadosPresupuesto, Boolean.FALSE);
		UtilidadBD.cerrarObjetosPersistencia(null, null , con);
		return cargarPresupuesto;
	}

	/**
	 * 
	 * @param con
	 * @param dto
	 * @param estadosPresupuesto
	 * @param estadoNotIn
	 * @return
	 */
	public static ArrayList<DtoPresupuestoOdontologico> cargarPresupuesto(Connection con, DtoPresupuestoOdontologico dto, ArrayList<String> estadosPresupuesto, boolean estadoNotIn){

		ArrayList<DtoPresupuestoOdontologico> cargarPresupuesto =DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarPresupuesto(con,dto, estadosPresupuesto, estadoNotIn);

		return cargarPresupuesto;
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoPresupuestoOdontologico> cargarPresupuesto(Connection con, DtoPresupuestoOdontologico dto, ArrayList<String> estadosPresupuesto){
		ArrayList<DtoPresupuestoOdontologico> cargarPresupuesto =DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarPresupuesto(con,dto, estadosPresupuesto, Boolean.FALSE);
		return cargarPresupuesto;
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static DtoPresupuestoOdontologico cargarPresupuestoContratado(BigDecimal ingreso)
	{
		DtoPresupuestoOdontologico dtoWhere= new DtoPresupuestoOdontologico();
		dtoWhere.setIngreso(ingreso);
		dtoWhere.setEstado(ConstantesIntegridadDominio.acronimoContratadoContratado);
		ArrayList<DtoPresupuestoOdontologico> arrayPresupuesto= PresupuestoOdontologico.cargarPresupuesto(dtoWhere);
		if(arrayPresupuesto.size()>0)
			return arrayPresupuesto.get(0);
		return null;
	}

	/**
	 * 
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public static BigDecimal obtenerValorTotalContratadoPresupuesto(BigDecimal codigoPkPresupuesto)
	{ 
		BigDecimal total= new BigDecimal(0);
		DtoPresupuestoTotalConvenio dto= new DtoPresupuestoTotalConvenio();
		dto.setPresupuesto(codigoPkPresupuesto);
		ArrayList<DtoPresupuestoTotalConvenio> array= cargarTotalesPresupuetoConvenio(dto);
		for(DtoPresupuestoTotalConvenio resultado: array)
		{
			total=total.add(resultado.getValorSubTotalContratado());
		}
		return total;
	}


	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean modificarPresupuesto(DtoPresupuestoOdontologico dto , Connection con){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().modificarPresupuesto(dto,con , new ArrayList<String>());
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean modificarPresupuesto(DtoPresupuestoOdontologico dto , Connection con, ArrayList<String> arrayEstados){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().modificarPresupuesto(dto,con , arrayEstados);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean eliminarPresupuesto(DtoPresupuestoOdontologico dto){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().eliminarPresupuesto(dto);
	}

	/*------------------------------------------------------
	 *  TOTALES DEL LOS CONVENIOS DEL PRESUPUESTO
	 * **************************************************
	 */

	/**
	 * 
	 */
	public static ArrayList<DtoPresupuestoTotalConvenio> cargarTotalesPresupuetoConvenio(DtoPresupuestoTotalConvenio dto){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarTotalesPresupuetoConvenio(dto);
	}

	/**
	 * 
	 */
	public static ArrayList<DtoPresupuestoTotalConvenio> cargarTotalesPresupuetoConvenio(BigDecimal presupuesto)
	{
		DtoPresupuestoTotalConvenio dto= new DtoPresupuestoTotalConvenio();
		dto.setPresupuesto(presupuesto);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarTotalesPresupuetoConvenio(dto);
	}

	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static BigDecimal obtenerValorContratadoInicialPresupuesto (long codigoPresupuesto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().obtenerValorContratadoInicialPresupuesto(codigoPresupuesto);
	}
	/*------------------------------------------------------------------------------------------------------------------------------------
	 * METODOS PARA El DETALLE PRESUPUESTO ODONTOLOGICOS PROGRAMA - SERVICIOS
	 * **********************************************************************************************************************************
	 */

	/**
	 * 
	 */
	public  static ArrayList<DtoPresuOdoProgServ> cargarPresupuestoOdoProgServ ( DtoPresuOdoProgServ dto, Connection con, DtoPresupuestoPiezas dtoPieza){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarPresupuestoOdoProgServ(dto, con, dtoPieza);
	}

	/**
	 * 
	 * @param dto
	 * @param con
	 * @return
	 */
	public  static ArrayList<DtoPresuOdoProgServ> cargarPresupuestoOdoProgServ ( DtoPresuOdoProgServ dto, DtoPresupuestoPiezas dtoPieza){
		Connection con=UtilidadBD.abrirConexion();
		ArrayList<DtoPresuOdoProgServ> array= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarPresupuestoOdoProgServ(dto, con, dtoPieza);
		UtilidadBD.closeConnection(con);
		return array;
	}


	/**
	 * 
	 * @param dto
	 * @param con
	 * @return
	 */
	public  static ArrayList<DtoPresuOdoProgServ> cargarPresupuestoOdoProgServ (BigDecimal codigoPkPresupuesto)
	{
		DtoPresuOdoProgServ dto= new DtoPresuOdoProgServ();
		dto.setPresupuesto(codigoPkPresupuesto);
		Connection con=UtilidadBD.abrirConexion();
		ArrayList<DtoPresuOdoProgServ> array= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarPresupuestoOdoProgServ(dto, con, null);
		UtilidadBD.closeConnection(con);
		return array;
	}


	/**
	 * 
	 * @param codigoPk
	 * @param programaServicio
	 * @param utilizaProgramas
	 * @return
	 */
	public static DtoPresuOdoProgServ obtenerPresupuestoOdoProgServ(BigDecimal codigoPkPresupuesto, BigDecimal programaServicio, boolean utilizaProgramas, DtoPresupuestoPiezas dtoPieza)
	{
		Connection con= UtilidadBD.abrirConexion();
		DtoPresuOdoProgServ dto= obtenerPresupuestoOdoProgServ(codigoPkPresupuesto, programaServicio, utilizaProgramas, con, dtoPieza);
		UtilidadBD.closeConnection(con);
		return dto;
	}

	/**
	 * 
	 * @param codigoPk
	 * @param programaServicio
	 * @param utilizaProgramas
	 * @return
	 */
	public static DtoPresuOdoProgServ obtenerPresupuestoOdoProgServ(BigDecimal codigoPkPresupuesto, BigDecimal programaServicio, boolean utilizaProgramas, Connection con, DtoPresupuestoPiezas dtoPieza)
	{
		ArrayList<DtoPresuOdoProgServ> arrayList= new ArrayList<DtoPresuOdoProgServ>();
		DtoPresuOdoProgServ dto= new DtoPresuOdoProgServ();
		dto.setPresupuesto(codigoPkPresupuesto);
		if(utilizaProgramas)
			dto.setPrograma(new InfoDatosDouble(programaServicio.doubleValue(), ""));
		else
			dto.setServicio(new InfoDatosInt(programaServicio.intValue()));

		arrayList= cargarPresupuestoOdoProgServ(dto, con, dtoPieza);
		if(arrayList.size()>0)
			return arrayList.get(0);
		return null;
	}


	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  double guardarPresupuestoOdoProgramaServicio(DtoPresuOdoProgServ dto , Connection con){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().guardarPresupuestoOdoProgramaServicio(dto , con);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  static boolean modificarPresupuestoProgramaServicio( DtoPresuOdoProgServ dto, Connection con ){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().modificarPresupuestoProgramaServicio(dto, con);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  static boolean actualizarCantidadProgramaServicioPresupuesto(BigDecimal codigoPk, int cantidad, String login, Connection con )
	{
		DtoPresuOdoProgServ dto= new DtoPresuOdoProgServ();
		dto.setCodigoPk(codigoPk);
		dto.setCantidad(cantidad);
		dto.setUsuarioModifica(new DtoInfoFechaUsuario(login));
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().modificarPresupuestoProgramaServicio(dto, con);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  boolean eliminarPresupuestoProgramaServicio( DtoPresuOdoProgServ dto  , Connection con){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().eliminarPresupuestoProgramaServicio(dto , con);
	}
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  boolean eliminarPresupuestoProgramaServicio(BigDecimal codigoPk , Connection con){
		DtoPresuOdoProgServ dto= new DtoPresuOdoProgServ();
		dto.setCodigoPk(codigoPk);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().eliminarPresupuestoProgramaServicio(dto , con);
	}


	/*---------------------------------------------------------------------------------------------------------------
	 * PRESUPUESTO ODONTOLOGIA CONVENIO 
	 * *****************************************************************************************************************************
	 * 
	 */
	/**	
	 * 
	 */
	public  static double guardarPresupuestoOdoConvenio(DtoPresupuestoOdoConvenio dto , Connection con){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().guardarPresupuestoOdoConvenio(dto, con);
	}
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoPresupuestoOdoConvenio> cargarPresupuestoConvenio ( DtoPresupuestoOdoConvenio dto, Connection con){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarPresupuestoConvenio(dto, con);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoPresupuestoOdoConvenio> cargarPresupuestoConvenio ( DtoPresupuestoOdoConvenio dto){ 
		Connection con= UtilidadBD.abrirConexion();
		ArrayList<DtoPresupuestoOdoConvenio> array= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarPresupuestoConvenio(dto, con);
		UtilidadBD.closeConnection(con);
		return array;
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoPresupuestoOdoConvenio> cargarPresupuestoConvenio (BigDecimal presupuestoOdoProgServ)
	{
		DtoPresupuestoOdoConvenio dto= new DtoPresupuestoOdoConvenio();
		dto.setPresupuestoOdoProgServ(presupuestoOdoProgServ);
		Connection con= UtilidadBD.abrirConexion();
		ArrayList<DtoPresupuestoOdoConvenio> array= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarPresupuestoConvenio(dto, con);
		UtilidadBD.closeConnection(con);
		return array;
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  static boolean modificarPresupuestoConvenio( DtoPresupuestoOdoConvenio dto , Connection con ){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().modificarPresupuestoConvenio(dto , con);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  boolean eliminarPresupuestoOdoConvenio( DtoPresupuestoOdoConvenio  dto , Connection con){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().eliminarPresupuestoOdoConvenio(dto, con);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  boolean eliminarPresupuestoOdoConvenioXProgServ( BigDecimal codigoProgServPresupuesto , Connection con)
	{
		DtoPresupuestoOdoConvenio  dto= new DtoPresupuestoOdoConvenio();
		dto.setPresupuestoOdoProgServ(codigoProgServPresupuesto);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().eliminarPresupuestoOdoConvenio(dto, con);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  boolean eliminarPresupuestoOdoConvenioXCodigoPk( BigDecimal codigoPk , Connection con)
	{
		DtoPresupuestoOdoConvenio  dto= new DtoPresupuestoOdoConvenio();
		dto.setCodigoPK(codigoPk);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().eliminarPresupuestoOdoConvenio(dto, con);
	}
	
	/*
	 *-------------------------------------------------------------------------------------------------------------------------------
	 * PRESUPUESTO PIEZAS ODONTOLOGICAS 
	 * ******************************************************************************************************************************
	 */

	/**
	 * 
	 */
	public static  double guardarPresupuestoPiezas(DtoPresupuestoPiezas  dto , Connection con){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().guardarPresupuestoPiezas(dto , con);
	}
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  static  ArrayList<DtoPresupuestoPiezas> cargarPresupuestoPieza ( DtoPresupuestoPiezas dto, boolean inactivarNoPendientesPlanTratamiento, boolean utilizaProgramas){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarPresupuestoPieza(dto, inactivarNoPendientesPlanTratamiento, utilizaProgramas);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  static  ArrayList<DtoPresupuestoPiezas> cargarPresupuestoPieza ( BigDecimal presupuestoOdoProgServ, boolean inactivarNoPendientesPlanTratamiento, boolean utilizaProgramas){
		DtoPresupuestoPiezas dto= new DtoPresupuestoPiezas();
		dto.setPresupuestoOdoProgServ(presupuestoOdoProgServ);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarPresupuestoPieza(dto, inactivarNoPendientesPlanTratamiento, utilizaProgramas);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  boolean modificarPresupuestoPieza( DtoPresupuestoPiezas dto ){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().modificarPresupuestoPieza(dto);
	}
	/**	
	 * 
	 * @param dto
	 * @return
	 */
	public static  boolean eliminarPresupuestoPieza( DtoPresupuestoPiezas  dto , Connection con){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().eliminarPresupuestoPieza(dto , con);
	}

	/**	
	 * 
	 * @param dto
	 * @return
	 */
	public static  boolean eliminarPresupuestoPiezaXProgServ(BigDecimal codigoProgServPresupuesto, Connection con)
	{
		DtoPresupuestoPiezas  dto= new DtoPresupuestoPiezas();
		dto.setPresupuestoOdoProgServ(codigoProgServPresupuesto);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().eliminarPresupuestoPieza(dto , con);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  ArrayList<DtoLogPresupuestoOdontologico> cargarLogPresupuesto ( DtoLogPresupuestoOdontologico dto){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarLogPresupuesto(dto);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  static ArrayList<DtoPresupuestoOdontologicoDescuento> cargarPresupuestoDescuentos ( DtoPresupuestoOdontologicoDescuento dto, ArrayList<String> listaEstados){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarPresupuestoDescuentos(dto, listaEstados);
	}

	/***
	 * 
	 * 
	 * 
	 */
	public static double guardarPresupuestoDescuento(DtoPresupuestoOdontologicoDescuento dto , Connection con){

		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().guardarPresupuestoDescuento(dto,con);	
	}
	/**
	 * 
	 * 
	 */

	public  static boolean eliminarPresupuestoDescuento( DtoPresupuestoOdontologicoDescuento  dto){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().eliminarPresupuestoDescuento(dto);
	}

	/**
	 * metodo que verifica 
	 * @param codigo
	 * @param codigo2
	 * @param hallazgoREQUERIDO
	 * @param codigoPkPresupuesto 
	 * @return
	 */
	public static boolean existeProgramaServicioPlanTratamientoEnPresupuesto(int piezaOPCIONAL, int superficieOPCIONAL, int hallazgoREQUERIDO, BigDecimal codigoPkProgramaServicio, boolean utilizaProgramas, BigDecimal codigoPkPresupuesto, String seccion )
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().existeProgramaServicioPlanTratamientoEnPresupuesto(piezaOPCIONAL, superficieOPCIONAL, hallazgoREQUERIDO, codigoPkProgramaServicio, utilizaProgramas, codigoPkPresupuesto, seccion).doubleValue()>0;
	}


	//*************************************** CALCULO DESCUENTO PARA CONTRATAR ***********************************************************

	/**
	 * 
	 */
	public static boolean aplicaPromocion(int contrato) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().aplicaPromocion(contrato);
	}

	/**
	 * 
	 * @param piezaOPCIONAL
	 * @param superficieOPCIONAL
	 * @param hallazgoREQUERIDO
	 * @param codigoPkProgramaServicio
	 * @param utilizaProgramas
	 * @param codigoPkPlan
	 * @return
	 */
	public static int existeProgramaServicioPresupuestoEnPlanTratamiento(
			int piezaOPCIONAL, int superficieOPCIONAL, int hallazgoREQUERIDO,
			BigDecimal codigoPkProgramaServicio, boolean utilizaProgramas,
			BigDecimal codigoPkPlan, String estadoOPCIONAL) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().existeProgramaServicioPresupuestoEnPlanTratamiento(piezaOPCIONAL, superficieOPCIONAL, hallazgoREQUERIDO, codigoPkProgramaServicio, utilizaProgramas, codigoPkPlan, estadoOPCIONAL);
	}

	/**
	 * 
	 * @param tipoRelacion
	 * @param codigoPresupuesto
	 * @return
	 */
	public  static String consultaReporteContratar(String tipoRelacion,
			int codigoPresupuesto) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().consultaReporteContratar(tipoRelacion, codigoPresupuesto);
	}
	
	/**
	 * 
	 * @param codigoPresupuesto
	 * @param tipoRelacion
	 * @return
	 */
	public static String consultaReporteDetalle(int codigoPresupuesto,
			String tipoRelacion) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().consultaReporteDetalle(codigoPresupuesto, tipoRelacion);
	}

	/**
	 * 
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public static ArrayList<InfoConvenioContratoPresupuesto> obtenerConveniosContratosPresupuesto(BigDecimal codigoPkPresupuesto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().obtenerConveniosContratosPresupuesto(codigoPkPresupuesto);
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
	public static boolean eliminarProgramaServicioPresupuestoCascada(	Connection con, 
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
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().eliminarProgramaServicioPresupuestoCascada(con, codigoPkPresupuestoREQUERIDO, programaOservicioREQUERIDO, utilizaProgramas, piezaDentalOPCIONAL, hallazgoREQUERIDO, superficieOPCIONAL, seccionREQUERIDA, loginUsuario, eliminarFilaProg);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPresupuestoOdoProgServ
	 * @return
	 */
	public static DtoPresupuestoOdoConvenio obtenerPresupuestoConvenioContratado(Connection con, BigDecimal codigoPresupuestoOdoProgServ)
	{
		DtoPresupuestoOdoConvenio dtoConvenio= new DtoPresupuestoOdoConvenio();
		dtoConvenio.setPresupuestoOdoProgServ(codigoPresupuestoOdoProgServ);
		dtoConvenio.setContratado(ConstantesBD.acronimoSi);
		ArrayList<DtoPresupuestoOdoConvenio> array= PresupuestoOdontologico.cargarPresupuestoConvenio(dtoConvenio, con);
		if(array.size()>0)
			return array.get(0);
		return null;
	}

	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param loginUsuario
	 * @return
	 */
	public static DtoConcurrenciaPresupuesto estaEnProcesoPresupuesto(Connection con, int cuenta, String idSesionOPCIONAL, boolean igualSession)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().estaEnProcesoPresupuesto(con, cuenta, idSesionOPCIONAL, igualSession);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param loginUsuario
	 * @return
	 */
	public static DtoConcurrenciaPresupuesto estaEnProcesoPresupuesto(int cuenta, String idSesionOPCIONAL, boolean igualSession)
	{
		Connection con=UtilidadBD.abrirConexion();
		DtoConcurrenciaPresupuesto dto= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().estaEnProcesoPresupuesto(con, cuenta, idSesionOPCIONAL, igualSession);
		UtilidadBD.closeConnection(con);
		return dto;
	}


	/**
	 * 
	 * @param idCuenta
	 * @param loginUsuario
	 * @param idSesion
	 * @return
	 */
	public static boolean empezarBloqueoPresupuesto(	int idCuenta,
			String loginUsuario, 
			String idSesion,
			boolean esFlujoInclusiones
	)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().empezarBloqueoPresupuesto(idCuenta, loginUsuario, idSesion, esFlujoInclusiones);
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public static int cancelarTodosLosProcesosDePresupuesto(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cancelarTodosLosProcesosDePresupuesto(con);
	}

	/**
	 * 
	 * @param idCuenta
	 * @param idSesion
	 * @return
	 */
	public static boolean cancelarProcesoPresupuesto(int idCuenta,String idSesion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cancelarProcesoPresupuesto(idCuenta, idSesion);
	}

	
	/**
	 * Metodo para cargar los permisos sobre la actividades que realiza un usuario frente al presupuesto
	 * Recibe le login del usuario y retorna un objeto {@link InfoPermisosPresupuesto } 
	 * @param loginUsuario
	 * @return
	 */
	public static  InfoPermisosPresupuesto cargarPermisosPresupuesto(String loginUsuario)
	{
		InfoPermisosPresupuesto info= new InfoPermisosPresupuesto();
		info.setCancelar(Utilidades.tieneRolFuncionalidad(loginUsuario, ConstantesBD.codigoFuncionalidadCancelarPresupuesto));
		info.setContratar(Utilidades.tieneRolFuncionalidad(loginUsuario, ConstantesBD.codigoFuncionalidadContratarPresupuesto));
		info.setCopiar(Utilidades.tieneRolFuncionalidad(loginUsuario, ConstantesBD.codigoFuncionalidadCopiarPresupuesto));
		info.setModificar(Utilidades.tieneRolFuncionalidad(loginUsuario, ConstantesBD.codigoFuncionaildadModificarPresupuesto));
		info.setReactivar(Utilidades.tieneRolFuncionalidad(loginUsuario, ConstantesBD.codigoFuncionalidadReactivarPresupuesto));
		info.setPreContratar(Utilidades.tieneRolFuncionalidad(loginUsuario, ConstantesBD.codigoFuncionalidadPrecontratadoPresupuesto));
		info.setSuspender(Utilidades.tieneRolFuncionalidad(loginUsuario, ConstantesBD.codigoFuncionalidadSuspenderPresupuesto));
		
	
		return info;
	}

	/**
	 * Método par aobtener el codigo del presupuesto de un plan de tratamiento
	 * por un estado específico
	 * @param codigoPlanTratamiento
	 * @param estado
	 * @return
	 */
	public static BigDecimal consultarCodigoPresupuestoXPlanTratamiento(BigDecimal codigoPlanTratamiento,String estado)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().consultarCodigoPresupuestoXPlanTratamiento(codigoPlanTratamiento, estado);
	}



	/**
	 * 
	 * @param dto
	 * @return
	 */

	public static  ArrayList<DtoPresupuestoDetalleServiciosProgramaDao> cargarPresupuestoDetalleServicosPrograma(DtoPresupuestoDetalleServiciosProgramaDao dto ){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarPresupuestoDetalleServicosPrograma(dto);
	}



	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double insertPresupuestoDetalleServiciosProgramaDao(DtoPresupuestoDetalleServiciosProgramaDao dto , Connection con){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().insertPresupuestoDetalleServiciosProgramaDao(dto, con);
	}


	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static   boolean eliminarPresupuestoDetalleServiciosProgramaDao(DtoPresupuestoDetalleServiciosProgramaDao dto ){
		Connection con= UtilidadBD.abrirConexion();
		boolean retorna= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().eliminarPresupuestoDetalleServiciosProgramaDao(dto, con);
		UtilidadBD.closeConnection(con);
		return retorna;
	}

	/**
	 * 
	 * @param codigoPresupuesto
	 * @return
	 */
	public static BigDecimal obtenerTotalPresupuestoParaDescuento(BigDecimal codigoPresupuesto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().obtenerTotalPresupuestoParaDescuento(codigoPresupuesto);
	}

	/**
	 * Cargar presupuesto Contratado
	 * @param codigoPresupuesto
	 * @return
	 */
	public static DtoPresupuestoTotalesContratadoPrecontratado obtenerTotalesContratadoPrecontratado(BigDecimal codigoPresupuesto)
	{
		DtoPresupuestoTotalesContratadoPrecontratado dtoTotales= new DtoPresupuestoTotalesContratadoPrecontratado();
		
		/*
		 * dto para consulta descuentos, se buscar por codigo presupuesto
		 */
		DtoPresupuestoOdontologicoDescuento dtoDcto= new DtoPresupuestoOdontologicoDescuento();
		dtoDcto.setPresupuesto(codigoPresupuesto);

		/*
		 * Carga la lista de descuentos
		 */
		ArrayList<DtoPresupuestoOdontologicoDescuento> lista= cargarPresupuestoDescuentos(dtoDcto, new ArrayList<String>());
		
		/*
		 * Existe un Solo descuento Comercial 
		 */
		if(lista.size()>0)
		{	
			dtoTotales.setCodigoPkDcto(lista.get(0).getCodigo());
			dtoTotales.setEstadoDescuento(lista.get(0).getEstado());
	
			if(dtoTotales.getEstadoDescuento().equals(ConstantesIntegridadDominio.acronimoAutorizado) 
					|| dtoTotales.getEstadoDescuento().equals(ConstantesIntegridadDominio.acronimoContratado1))
			{
				dtoTotales.setDescuento(lista.get(0).getValorDescuento());
			}
			else
			{
				dtoTotales.setDescuentoNoAutorizadoNoContratado(lista.get(0).getValorDescuento());
			}
		}
	
		/*
		 * Para Aplicar descuentos Comercial
		 */
		dtoTotales.setTotalPresupuestoParaDescuento(obtenerTotalPresupuestoParaDescuento(codigoPresupuesto));
		/*
		 * No se le resta descuento comercial 
		 */
		dtoTotales.setTotalPresupuesto(obtenerTotalPresupuestoSinDctoOdon(codigoPresupuesto));

		return dtoTotales;
	}

	/**
	 * 
	 * @param codigoPresupuesto
	 * @return
	 */
	public static BigDecimal obtenerTotalPresupuestoSinDctoOdon(BigDecimal codigoPresupuesto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().obtenerTotalPresupuestoSinDctoOdon(codigoPresupuesto);
	}

	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean modificarDescuentoPresupuesto(Connection con, DtoPresupuestoOdontologicoDescuento dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().modificarDescuentoPresupuesto(con, dto);
	}

	/**
	 * 
	 * @param con
	 * @param fhuModifica
	 * @param estado
	 * @param codigoPk
	 * @return
	 */
	public static boolean cambiarEstadoDescuentoPresupuesto(Connection con, DtoInfoFechaUsuario fhuModifica, String estado, BigDecimal codigoPk)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cambiarEstadoDescuentoPresupuesto(con, fhuModifica, estado, codigoPk);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPkPresupuesto
	 * @param loginUsuario
	 * @param codigoMotivo
	 * @return
	 */
	public static boolean anularDescuentoPresupuesto(Connection con, BigDecimal codigoPkPresupuesto, String loginUsuario, double codigoMotivo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().anularDescuentoPresupuesto(con, codigoPkPresupuesto, loginUsuario, codigoMotivo);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPkPresupuesto
	 * @param loginUsuario
	 * @param codigoMotivo
	 * @return
	 */
	public static boolean anularDescuentoPresupuestoPrecontratadoYCambioEstadoActivo(Connection con, ArrayList<InfoPresupuestoPrecontratado> listaPresupuestoPrecontratados, String loginUsuario, double codigoMotivo)
	{
		for(InfoPresupuestoPrecontratado info: listaPresupuestoPrecontratados)
		{	
			if(!anularDescuentoPresupuesto(con, info.getCodigoPkPresupuesto(), loginUsuario, codigoMotivo))
			{	
				return false;
			}
			if(!cambiarEstadoPresupuesto(con, info.getCodigoPkPresupuesto(), ConstantesIntegridadDominio.acronimoEstadoActivo))
			{
				return false;
			}
			if(!modificarIndicativoContratadoPresupuesto(con, info.getCodigoPkPresupuesto()))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param con
	 * @param codigoPk
	 * @param estadoNuevo
	 * @return
	 */
	public static boolean cambiarEstadoPresupuesto(Connection con, BigDecimal codigoPk, String estadoNuevo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cambiarEstadoPresupuesto(con, codigoPk, estadoNuevo);
	}

	/**
	 * 
	 * @param ingreso
	 * @return
	 */
	public static ArrayList<InfoPresupuestoPrecontratado> cargarPresupuestosPresontratados(int ingreso, BigDecimal presupuestoNotIn)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarPresupuestosPresontratados(ingreso, presupuestoNotIn);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public static boolean modificarIndicativoContratadoPresupuesto(Connection con, BigDecimal codigoPkPresupuesto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().modificarIndicativoContratadoPresupuesto(con, codigoPkPresupuesto);
	}

	/**
	 * Verifica si el paciente tiene presupuesto filtrado por los estados pasados por parámetros
	 * @param codigoPaciente
	 * @param estados
	 */
	public static boolean existePresupuestoXPaciente(int codigoPaciente, ArrayList<String> estados) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().existePresupuestoXPaciente(codigoPaciente, estados);
	}

	/**
	 * 
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public static boolean reversarAnticiposPresupuestoContratado(Connection con, BigDecimal codigoPkPresupuesto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().reversarAnticiposPresupuestoContratado(con, codigoPkPresupuesto);
	}

	/**
	 * 
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public static double obtenerPorcentajeDctoOdonContratadoPresupuesto(BigDecimal codigoPkPresupuesto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().obtenerPorcentajeDctoOdonContratadoPresupuesto(codigoPkPresupuesto);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @param utilizaProgramas
	 * @return
	 */
	public static BigDecimal insertarPlanTtoPresupuesto(Connection con, DtoPlanTratamientoPresupuesto dto, boolean utilizaProgramas)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().insertarPlanTtoPresupuesto(con, dto, utilizaProgramas);
	}

	/**
	 * 
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public static BigDecimal obtenerCodigoPlanTratamientoPresupuesto(BigDecimal codigoPkPresupuesto) 
	{
		BigDecimal plan= BigDecimal.ZERO;
		DtoPresupuestoOdontologico dtoWhere= new DtoPresupuestoOdontologico();
		dtoWhere.setCodigoPK(codigoPkPresupuesto);
		ArrayList<DtoPresupuestoOdontologico> presupuesto= cargarPresupuesto(dtoWhere);
		if(presupuesto.size()>0)
		{
			plan= presupuesto.get(0).getPlanTratamiento();
		}
		return plan;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public static boolean eliminarPlanTtoPresupuesto(Connection con,BigDecimal codigoPkPresupuesto) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().eliminarPlanTtoPresupuesto(con, codigoPkPresupuesto);
	}

	/**
	 * 
	 * @param bigDecimal
	 * @param detPlanTratamiento
	 * @param programa
	 * @param servicio
	 * @return
	 */
	public static boolean actualizarPlanTtoPresupuestoProgServ(Connection con,BigDecimal codigoPkProgramaServicioPlanTto, BigDecimal detPlanTratamiento,InfoDatosDouble programa, int servicio, BigDecimal codigoPkPresupuesto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().actualizarPlanTtoPresupuestoProgServ(con, codigoPkProgramaServicioPlanTto, detPlanTratamiento, programa, servicio, codigoPkPresupuesto);
	}

	/**
	 * Método que modifica los bonos por convenio
	 * 
	 * @param con
	 * @param dtoConvenio
	 * @return
	 */
	public static boolean modificarBonos(	Connection con, 
			DtoPresupuestoOdoConvenio dtoConvenio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().modificarBonos( con, dtoConvenio);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPKPresupuestoConvenio
	 * @param advertenciaPromocion
	 * @param valorDescuentoPromocion
	 * @param porcentajePromocion
	 * @param valorHonorarioPromocion
	 * @param porcentajeHonorarioPromocion
	 */
	public static boolean modificarPromociones(	Connection con,	
			DtoPresupuestoOdoConvenio dtoConvenio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().modificarPromociones(	con, dtoConvenio);
	}
	
	/**
	 * metodo para cargar las citas odontologicas que se deben cancelar para poder cancelar un presupuesto
	 * @param codigoPaciente
	 * @return
	 */
	public static ArrayList<DtoCitasPresupuestoOdo> cargarCitasPresupuestoOdontologico(int codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarCitasPresupuestoOdontologico(codigoPaciente);
	}

	/**
	 * 
	 * @param con
	 * @param listaCitasPresupuesto
	 * @param motivoCancelacionCita
	 * @param usuario
	 * @param codigoPersona
	 * @return
	 */
	public static boolean cancelarCitasPresupuesto(	Connection con,
													ArrayList<DtoCitasPresupuestoOdo> listaCitasPresupuesto,
													int motivoCancelacionCita, 
													UsuarioBasico usuario, 
													int codigoPaciente,
													Integer ingreso) throws IPSException 
	{
		boolean retorna=true;
		for(DtoCitasPresupuestoOdo dto: listaCitasPresupuesto)
		{	
			if(dto.getAcronimoEstado().equals(ConstantesIntegridadDominio.acronimoReservado) || dto.getAcronimoEstado().equals(ConstantesIntegridadDominio.acronimoAsignado))
			{
				DtoCitaOdontologica dtoCita= AtencionCitasOdontologia.cargarDatosCita(dto.getCodigo(), usuario);
				/*
				 * TODO
				 *Nota cambio temporal verificar el funcionamiento  
				 */
				retorna= CitaOdontologica.cancelarCita(con, dtoCita, usuario, codigoPaciente, ConstantesBD.acronimoSi, motivoCancelacionCita, false/*iniciarFinalizarTransaccion*/, ingreso, ConstantesIntegridadDominio.acronimoCancelado ).isTrue();
				if(!retorna)
				{
					break;
				}
			}
			else if(dto.getAcronimoEstado().equals(ConstantesIntegridadDominio.acronimoProgramado) || dto.getAcronimoEstado().equals(ConstantesIntegridadDominio.acronimoAreprogramar))
			{
				retorna= CitaOdontologica.cambiarEstadoCita(con, dto.getCodigo(), ConstantesIntegridadDominio.acronimoEstadoCancelado, usuario.getLoginUsuario());
				if(!retorna)
				{
					break;
				}
			}
		}
		return retorna;
	}

	/**
	 * Metodo que verifica que los programas - servicios que contrate en el presupuesto estén en estado difererente a pendiente, 
	 * contratado y al menos uno en estado terminado
	 * para poder terminar el presupuesto 
	 * @param equals
	 * @param codigoPK
	 * @return
	 */
	public static boolean puedoTerminarPresupuesto(boolean utilizaProgramas, BigDecimal codigoPKPresupuesto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().puedoTerminarPresupuesto(utilizaProgramas, codigoPKPresupuesto);
	}
	
	/**
	 * 
	 * @param codigoPkPlanTratamiento
	 * @return
	 */
	public static double obtenerDctoOdontologicoPresupuestoXPlan(BigDecimal codigoPkPlanTratamiento)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().obtenerDctoOdontologicoPresupuestoXPlan(codigoPkPlanTratamiento);
	}
	
	/**
	 * CARGAR DTO LOG PRESUPUESTO 
	 * @author Edgar Carvajal Ruiz
	 * @param dto
	 * @return
	 */
	public static  DtoLogPrespuesto cargarDtoLogPresupuesto(DtoLogPrespuesto dto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarDtoLogPresupuesto(dto);
	}
	
	/**
	 * 
	 * Metodo para eliminar toda la estructura
	 * @param utilizaProgramas
	 * @param con
	 * @param codigoPkProgServPresu
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static boolean eliminarCascadaProgramaServPresupuesto(	boolean utilizaProgramas, 
																	Connection con, 
																	BigDecimal codigoPkProgServPresu,
																	BigDecimal codigoPkPresupuesto)
	{
		//comenzamos la eliminacion desde las piezas
		if(!PresupuestoOdontologico.eliminarPresupuestoPiezaXProgServ(codigoPkProgServPresu, con))
		{
			Log4JManager.error("NO ELIMINA -->"+codigoPkProgServPresu);
			return false;
		}
		//eliminamos los servicios de los programas
		DtoPresupuestoOdoConvenio dtoConv= new DtoPresupuestoOdoConvenio();
		dtoConv.setPresupuestoOdoProgServ(codigoPkProgServPresu);
		ArrayList<DtoPresupuestoOdoConvenio> array= PresupuestoOdontologico.cargarPresupuestoConvenio(dtoConv, con);
		if(utilizaProgramas)
		{
			for(DtoPresupuestoOdoConvenio e: array)
			{
				DtoPresupuestoDetalleServiciosProgramaDao dto= new DtoPresupuestoDetalleServiciosProgramaDao();
				dto.setPresupuestoOdoConvenio(e.getCodigoPK());
				if(!PresupuestoOdontologico.eliminarPresupuestoDetalleServiciosProgramaDao(dto))
				{
					Log4JManager.error("NO ELIMINA -->"+codigoPkProgServPresu);
					return false;
				}
			}
		}
		
		//eliminamos convenios
		if(!PresupuestoOdontologico.eliminarPresupuestoOdoConvenioXProgServ(codigoPkProgServPresu, con))
		{
			Log4JManager.error("NO ELIMINA -->"+codigoPkProgServPresu);
			return false;
		}
		if(!PresupuestoOdontologico.eliminarPresupuestoProgramaServicio(codigoPkProgServPresu, con))
		{
			Log4JManager.error("NO ELIMINA -->"+codigoPkProgServPresu);
			return false;
		}
		
		if(!PresupuestoOdontologico.eliminarPlanTtoPresupuesto(con, codigoPkPresupuesto))
		{
			Log4JManager.error("NO plan tto Presupuesto");
			return false;
		}
		
		//eliminamos la informacion de n superficies
		if(!NumeroSuperficiesPresupuesto.eliminarXPresupuesto(con, codigoPkPresupuesto))
		{
			Log4JManager.error("NO elimina superficies Presupuesto");
			return false;
		}
		
		Log4JManager.info("100% elimino!!!!!!!--->"+codigoPkProgServPresu);
		return true;
	}
	
	
	
	
	
	
	
	/**
	 * METODO QUE GUARDA LA CLAUSULA DE CONTRATO NOTA SE COLOCO DE UN MUCHOS CON PRESUPUESTO 
	 * CONTRATADO PARA LA FLEXIBILIDAD ->PERO SU FUNCIONAMIENTO DE UNO A UNO 
	 * TAMBIEN EN INSTITUCION SE PUEDE COLOCAR MUCHOS CONTRATOS ODONTOLOGICOS
	 * @author Edgar Carvajal Ruiz
	 * @param con
	 * @param dto
	 * @return
	 */
	public static  boolean guardarContratoPresupuestoClausula(Connection con,DtoPresuContratoOdoImp dto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().guardarContratoPresupuestoClausula(con, dto);
	}
	
	
	
	
	/**
	 * Metodo para consultar el valor del descuento que tiene un Presupuesto
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public static BigDecimal obtenerValorDctoOdontologicoPresupuesto(BigDecimal codigoPkPresupuesto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().obtenerValorDctoOdontologicoPresupuesto(codigoPkPresupuesto);
	}
	
	
	/**
	 * Método para obtener el Valor Total de Presupuesto con Descuento cuando el Presupuesto esta contratado
	 * @param codigoPkPresupuesto
	 * @return
	 */
	public static BigDecimal obtenerTotalPresupuestoConDctoOdon(BigDecimal codigoPkPresupuesto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().obtenerTotalPresupuestoConDctoOdon(codigoPkPresupuesto);
	}
	

	
	/**
	 *	Metodo que cargar el Valor del Descuento para el Presupuesto.
	 *	Recibe un el codigo_pk del Presupuesto y retorna el Valor de Descuento 
	 * @author Edgar Carvajal Ruiz
	 * @param codigoPkPresupuesto
	 */
	public static BigDecimal cargarValorPresupuesto(BigDecimal codigoPkPresupuesto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().cargarValorPresupuesto(codigoPkPresupuesto);
	}
	
	
	/**
	 * Metodo que lista los ingreso de un paciente con presuesto asociado
	 * Recibe el codigo del paciente, la via de ingreso y la institucion
	 * @author Edgar Carvajal Ruiz
	 * @param paciente
	 * @param viaIngreso
	 * @param institucion
	 * @return
	 */
	public static  List<DtoHistoricoIngresoPresupuesto> listaIngresoHistoricosPresupuesto(final int paciente ,final  int viaIngreso ,final  int institucion )
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().listaIngresoHistoricosPresupuesto(paciente, viaIngreso, institucion);
	}
	

	
	/**
	 * Cargar ingreso paciente
	 * @param codigoPaciente
	 * @return
	 */
	public static List<ConveniosIngresoPaciente> cargarIngresoPaciente(int codigoPaciente){
	

		UtilidadTransaccion.getTransaccion().begin();
		IConveniosIngresoPacienteServicio servicioConvenio =FacturacionServicioFabrica.crearConvenioIngresoPaciente();
		List<ConveniosIngresoPaciente> listConvenioPaciEntidad =  servicioConvenio.obtenerConveniosIngresoPacientePorEstado(codigoPaciente, ConstantesBD.acronimoSiChar);		 
		
		try 
		{
			for(ConveniosIngresoPaciente dto: listConvenioPaciEntidad){
				
				if( dto.getContratos()!=null && dto.getContratos().getConvenios()!=null )
				{
					dto.getContratos().getCodigo();
					dto.getContratos().getConvenios().getCodigo();
					dto.getContratos().getConvenios().getNombre();
					dto.getContratos().getNumeroContrato();
				}
			}
			
		}catch (Exception e) {
			
			Log4JManager.error(e);
		}
		
		UtilidadTransaccion.getTransaccion().commit(); 
		
		return listConvenioPaciEntidad;
	}

	public static ArrayList<DtoConsolidadoPresupuestoContratadoPorPromocion> obtenerPresupuestoOdoContratadoPromo(
			DtoReportePresupuestosOdontologicosContratadosConPromocion dto) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoOdontologicoDao().obtenerPresupuestoOdoContratadoPromo(dto);
	}
	
}