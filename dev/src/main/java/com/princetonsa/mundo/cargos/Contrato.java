/*
 * @(#)Contrato.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
 package com.princetonsa.mundo.cargos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dao.ContratoDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.facturacion.DtoControlAnticiposContrato;
import com.princetonsa.dto.facturacion.DtoLogControlAnticipoContrato;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;


/**
 * Clase para el manejo de contratos
 * @version 1.0, Abril 30, 2004
 */
public class Contrato
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ContratoDao contratoDao;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(Contrato.class);
		
	/**
	 * codigo axioma del contrato
	 */
	private int codigo;

	/**
	 * codigo del convenio al cual corresponde el contrato
	 */
	private int codigoConvenio; 
	
	/**
	 * N�mero del contrato
	 */
	private String numeroContrato;
	
	
	/**
	 * Fecha inicial del contrato
	 */
	private String fechaInicial;
	
	/**
	 * Fecha final del contrato
	 */
	private String  fechaFinal;
	
	/**
	 * Tipo de contrato (Capitado / Normal) 
	 */
	//private int tipoContrato;

	/**
	 * Para la busqueda
	 * Contiene el nombre del convenio (tabla : convenios - campo: nombre)
	 */
	private String nombreConvenio; 
	
	/**
	 * Para la busqueda
	 * Contiene el nombre del tipo de contrato
	 * (table : tipos_contrato - campo: nombre)
	 */																
	//private String nombreTipoContrato; 

	/**
	 * Contiene el valor true=fechaInicial, false=fechaFinal
	 * para la busqueda por rangos de fechas
	 */
	private String eleccionFechaBusqueda;
	
	/**
	 * Valor del contrato
	 */
	private double valorContrato;
	
	/**
	 * Valor Acumulado
	 */
	private double valorAcumulado;
	

	private boolean estaVencido;
	
	/**
	 * codigo del tipo de contrato
	 */
	private String codigoTipoContrato="";
	
	/**
	 * upc
	 */
	private String upc;
	
	/**
	 * cod tipo pago
	 */
	private String codigoTipoPago;
	
	/**
	 * nombre tipo pago
	 */
	private String nombreTipoPago;
	
	
	/**
	 * % pyp
	 */
	private String pyp;
	
	/**
	 * contrato secretaria
	 */
	private String contratoSecretaria;
	
	/**
	 * nivel de atencion mapa
	 */
	@SuppressWarnings("unchecked")
	private HashMap nivelAtencionMap;
	
	/**
	 * nivel de atencion (busqueda)
	 */
	private String busquedaNivelAtencion;
	
	/**
	 * 
	 */
	private String porcentajeUpc;
	
	/**
	 * 
	 */
	private String base;
	
	/**
	 * 
	 */
	private String nombreBase;
	
	private String fechaFirmaContrato;
	private String diaLimiteRadicacion;
	private String diasRadicacion;
	private String controlaAnticipos;
	private String manejaCobertura;
	private String sinContrato;
	
	
	/**
	 * 
	 */
	private String requiereautonocobertura;
	
	/**
	 * 
	 */
	private String validarAbonoAtencionOdo;
	
	/**
	 * 
	 */
	private String pacientePagaAtencion;
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private HashMap esquemasInventario;
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private HashMap esquemasProcedimientos;
	
	/**
	 * 
	 */
	private String esquemaTarProcedimiento;
	
	/**
	 * 
	 */
	private String esquemaTarInventario;
	
	
	private String frmObservaciones;	//tarea 66618
	/**
	 * 
	 * 
	 * 
	 */
	
	private String tipoConvenio;
	
	private DtoControlAnticiposContrato dtoControlAnticipo;
	
	/**
	 * reset de los atributos para capitacion
	 *
	 */
	private DtoLogControlAnticipoContrato dtoLogControlAnticipo;
	
	/**
	 * 
	 */
	private boolean manejaTarifasXCA;
	
	
	/**
	 * Carga los datos para mostrarlos en el resumen
	 */
	private final static String cargarDatosContrato= 	"SELECT " +
			"c.codigo ||'' AS codigo, " +	
			"c.convenio AS convenio, " +
			"to_char(c.fecha_inicial,'yyyy-mm-dd') AS fechaInicial, " +
			"to_char(c.fecha_final,'yyyy-mm-dd') AS fechaFinal, " +
			"c.numero_contrato AS numeroContrato, " +
			"c.valor AS valorContrato, " +
			"c.acumulado AS valorAcumulado, " +
			"to_char(c.fecha_firma,'yyyy-mm-dd') AS fechaFirmaContrato, " +
			"c.limite_radicacion AS diaLimiteRadicacion, " +
			"c.dias_radicacion AS diasRadicacion, " +
			"c.controla_anticipos AS controlaAnticipos, " +
			"c.maneja_cobertura AS manejaCobertura, " +
			"CASE WHEN c.tipo_pago IS NULL THEN '' ELSE c.tipo_pago ||'' END AS codigoTipoPago, " +
			"CASE WHEN c.upc IS NULL THEN '' ELSE c.upc ||'' END AS upc," +
			"CASE WHEN c.porcentaje_pyp IS NULL THEN '' ELSE c.porcentaje_pyp ||'' END AS porcentaje_pyp, " +
			"CASE WHEN c.contrato_secretaria IS NULL THEN '' ELSE c.contrato_secretaria END AS contrato_secretaria, " +
			"co.tipo_contrato AS codigoTipoContrato, " +
			"CASE WHEN c.porcentaje_upc IS NULL THEN '' ELSE c.porcentaje_upc ||'' END AS porcentaje_upc, " +
			"CASE WHEN c.base IS NULL THEN '' ELSE c.base ||'' END AS codigoBase, " +
			"CASE WHEN c.base IS NULL THEN '' ELSE getNombreTipoContrato(c.base) end as nombreBase, " +
			"c.req_auto_no_cobertura as requiereautonocobertura, " +
			"c.sin_contrato as sinContrato, " +
			"c.observaciones as frmObservaciones, " +
			"c.paciente_paga_atencion, " +
			"c.validar_abono_atencion_odo," +
			"co.tipo_atencion AS tipoatencionconvenio, " +
			"c.maneja_tarifas_x_ca as maneja_tarifas_x_ca " +
			"FROM " +
			"facturacion.contratos c, facturacion.convenios co WHERE co.codigo=c.convenio AND c.codigo= ? ";
	
	
    /**
	 * 
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void resetCapitacion()
	{
		this.upc="";
		this.codigoTipoPago="";
		this.pyp="";
		this.contratoSecretaria="";
		this.nivelAtencionMap= new HashMap();
		this.nombreTipoPago="";
		this.busquedaNivelAtencion="";
		this.porcentajeUpc="";
		this.base="";
		this.nombreBase="";
	}
	
	/**
	 * resetea los datos pertinentes al registro de empresa
	 */
	@SuppressWarnings("unchecked")
	public void reset()
	{
		this.codigo=0;
		this.codigoConvenio=0;
		this.fechaFinal="";
		this.fechaInicial="";
		this.numeroContrato="";
		
		this.nombreConvenio="";
		this.eleccionFechaBusqueda="";
		this.valorAcumulado=ConstantesBD.codigoNuncaValido;
		this.valorContrato=ConstantesBD.codigoNuncaValido;
		this.estaVencido=false;
		
		this.fechaFirmaContrato="";
		this.diaLimiteRadicacion="";
		this.diasRadicacion="";
		this.controlaAnticipos="";
		this.manejaCobertura="";
		this.requiereautonocobertura="";
		this.pacientePagaAtencion="";
		this.validarAbonoAtencionOdo="";
		this.esquemasInventario=new HashMap<String,Object>();
		this.esquemasInventario.put("numRegistros", "0");
		
		this.esquemasProcedimientos=new HashMap<String, Object>();
		this.esquemasProcedimientos.put("numRegistros", "0");
		this.sinContrato="";
		
		this.esquemaTarInventario="";
		this.esquemaTarProcedimiento="";
	
		this.frmObservaciones = ""; // tarea 66618
		
		this.dtoControlAnticipo = new DtoControlAnticiposContrato();
		
		this.dtoLogControlAnticipo = new DtoLogControlAnticipoContrato();
		
		this.tipoConvenio="";
		this.manejaTarifasXCA= false;
		
		this.resetCapitacion();
	}	


	void LLenarDtoLogControlAnticipo(){
		
		this.getDtoLogControlAnticipo().setControlAnticipoContrato(this.getDtoControlAnticipo().getCodigo());
		this.getDtoLogControlAnticipo().setFechaModifica(UtilidadFecha.getFechaActual());
		this.getDtoLogControlAnticipo().setEliminado(ConstantesBD.acronimoNo);
		this.getDtoLogControlAnticipo().setHoraModifica(UtilidadFecha.getHoraActual());
		this.getDtoLogControlAnticipo().setNumeroMaximoPaciente(this.getDtoControlAnticipo().getNumeroMaximoPaciente());
		this.getDtoLogControlAnticipo().setNumeroPacientesAtendidos(this.getDtoControlAnticipo().getNumeroPacientesAtendidos());
		this.getDtoLogControlAnticipo().setNumeroPacientesXAtender(this.getDtoControlAnticipo().getNumeroPacientesXAtender());
		this.getDtoLogControlAnticipo().setNumeroTotalPacientes(this.getDtoControlAnticipo().getNumeroTotalPacientes());
		this.getDtoLogControlAnticipo().setRequiereAnticipo(this.getDtoControlAnticipo().getRequiereAnticipo());
		this.getDtoLogControlAnticipo().setUsuarioModifica(this.getDtoControlAnticipo().getUsuarioModifica());
		this.getDtoLogControlAnticipo().setValorAnticipoContratadoConvenio(this.getDtoControlAnticipo().getValorAnticipoContratadoConvenio());
		this.getDtoLogControlAnticipo().setValorAnticipoDisponible(this.getDtoControlAnticipo().getValorAnticipoDisponible());
		this.getDtoLogControlAnticipo().setValorAnticipoRecibidoConvenio(this.getDtoControlAnticipo().getValorAnticipoRecibidoConvenio());
		this.getDtoLogControlAnticipo().setValorAnticipoReservadoConvenio(this.getDtoControlAnticipo().getValorAnticipoReservadoConvenio());
		this.getDtoLogControlAnticipo().setValorAnticipoUtilizado(this.getDtoControlAnticipo().getValorAnticipoUtilizado());
	}
	
	/**
	 * Constructor de la clase, inicializa en vacio todos los par�metros
	 */
	public Contrato()
	{
		this.init (System.getProperty("TIPOBD"));
		reset();
	}

	/**
	 * Metodo para insertar un contrato
	 * @param con una conexion abierta con una fuente de datos
	 * @return numero de filas insertadas (1 o 0)
	 * @throws SQLException
	 */
	public ArrayList<DtoLogControlAnticipoContrato> obtenerListalog(DtoLogControlAnticipoContrato dtoWhere)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getLogControlAnticipoContratoDao().cargar(dtoWhere);
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int insertarContrato(Connection con) throws SQLException 
	{
		int  resp1=0;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if (contratoDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexion con la fuente de datos (ContratoDao - insertarContrato )");
		}
		//Iniciamos la transaccion, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);

		resp1=contratoDao.insertar(	con,this.codigoConvenio, this.numeroContrato, this.fechaInicial, this.fechaFinal, 
		        					this.valorContrato, this.valorAcumulado, this.fechaFirmaContrato,
		        					this.diaLimiteRadicacion, this.diasRadicacion, this.controlaAnticipos, this.manejaCobertura,
		        					this.codigoTipoPago,
									this.upc,
									this.pyp,
									this.contratoSecretaria,
									this.nivelAtencionMap,
									this.porcentajeUpc,
									this.base,
									this.requiereautonocobertura,
									this.sinContrato,
									this.frmObservaciones,
									this.pacientePagaAtencion,
									this.validarAbonoAtencionOdo,
									this.manejaTarifasXCA
								  );
		
		
		logger.info("resp 1 *******************************************************"+resp1);
		this.getDtoControlAnticipo().setContrato(resp1);
		
	    double resp2 = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getControlAnticipoContratoDao().guardar(this.dtoControlAnticipo, con);
		
		if (!inicioTrans||resp1<1 || resp2<1 )
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
	}
	

	// devuelve true si puede insertar
	public boolean puedoInsertarContrato(	Connection con, 
											int convenio, 
											String fechaInicial,
											String fechaFinal,
											String estadoInsertaOModifica,
											String fechaInicialAntigua,
											String fechaFinalAntigua
											) 
	{
		return contratoDao.puedoInsertarContrato(con,convenio,fechaInicial,fechaFinal,estadoInsertaOModifica, fechaInicialAntigua, fechaFinalAntigua);
	}
	
	/**
	 * Metodo para cargar los datos pertinentes al resumen
	 */
	public boolean cargar(Connection con, String codigoContratoStr) throws IPSException
	{
		boolean resputa=false;
		PreparedStatement pst=null;
		ResultSet rs=null;
	 	
		try	{
	 		logger.info("############## Inicio cargar");
			pst=con.prepareStatement(cargarDatosContrato, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, Integer.parseInt(codigoContratoStr));
			rs=pst.executeQuery();
			
			if (rs.next())
			{
				this.codigo=Integer.parseInt(codigoContratoStr+"");
				this.codigoConvenio=rs.getInt("convenio");
				this.fechaInicial=rs.getString("fechaInicial");
				this.fechaFinal=rs.getString("fechaFinal");
				this.numeroContrato=rs.getString("numeroContrato");
				this.valorContrato=rs.getDouble("valorContrato");
				this.valorAcumulado=rs.getDouble("valorAcumulado");
				this.fechaFirmaContrato=rs.getString("fechaFirmaContrato")==null?"":rs.getString("fechaFirmaContrato");
				this.diaLimiteRadicacion=rs.getString("diaLimiteRadicacion")==null?"":rs.getString("diaLimiteRadicacion");
				this.diasRadicacion=rs.getString("diasRadicacion")==null?"":rs.getString("diasRadicacion");
				this.controlaAnticipos=rs.getString("controlaAnticipos");
				this.manejaCobertura=rs.getString("manejaCobertura");
				this.sinContrato=rs.getString("sinContrato");
				this.frmObservaciones = rs.getString("frmObservaciones")==null?"":rs.getString("frmObservaciones");
				
				//parte capitacion 
				this.codigoTipoContrato=rs.getString("codigoTipoContrato")==null?"":rs.getString("codigoTipoContrato");
				this.codigoTipoPago=rs.getString("codigoTipoPago")==null?"":rs.getString("codigoTipoPago");
				this.upc= rs.getString("upc")==null?"":rs.getString("upc");
				this.pyp= rs.getString("porcentaje_pyp")==null?"":rs.getString("porcentaje_pyp");
				this.contratoSecretaria= rs.getString("contrato_secretaria")==null?"":rs.getString("contrato_secretaria");
				cargarNivelesContratos(con, this.getCodigo()+"");
				
				this.porcentajeUpc=rs.getString("porcentaje_upc")==null?"":rs.getString("porcentaje_upc");
				this.base=rs.getString("codigoBase")==null?"":rs.getString("codigoBase");
				this.nombreBase=rs.getString("nombreBase")==null?"":rs.getString("nombreBase");
				
				this.requiereautonocobertura=rs.getString("requiereautonocobertura")==null?"":rs.getString("requiereautonocobertura");
				this.pacientePagaAtencion=rs.getString("paciente_paga_atencion")==null?"":rs.getString("paciente_paga_atencion");
				this.validarAbonoAtencionOdo = rs.getString("validar_abono_atencion_odo")==null?"":rs.getString("validar_abono_atencion_odo");
				
				this.tipoConvenio=rs.getString("tipoatencionconvenio")==null?"":rs.getString("tipoatencionconvenio");
				this.manejaTarifasXCA= UtilidadTexto.getBoolean(rs.getString("maneja_tarifas_x_ca"));
				
				this.esquemasInventario=contratoDao.cargarEsquemasTarifarioInventarios(con,codigoContratoStr);
				this.esquemasProcedimientos=contratoDao.cargarEsquemasTarifarioProcedimientos(con,codigoContratoStr);
				resputa=true;
			}
			DtoControlAnticiposContrato dtoWhere = new DtoControlAnticiposContrato();
			dtoWhere.setContrato(this.getCodigo());
			ArrayList<DtoControlAnticiposContrato> lista=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getControlAnticipoContratoDao().cargar(con, dtoWhere);
			if(lista.size() > 0)
			{
				this.setDtoControlAnticipo(lista.get(0));
			}
			else
			{	
				this.setDtoControlAnticipo(new DtoControlAnticiposContrato());
			}	
		}
	 	catch(SQLException sqe){
			Log4JManager.error(sqe);
		}
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch(Exception e){
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				Log4JManager.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		Log4JManager.info("############## Fin cargar");
	 	return resputa;
	}

	/**
	 * Metodo utilizado en la funcionalidad Modificar contrato
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public int modificarContrato(Connection con) throws SQLException 
	{
		int resp=0;
		resp=contratoDao.modificar(	con,this.getCodigo(),this.getNumeroContrato(),this.getFechaInicial(),this.getFechaFinal(),
				this.getValorContrato(), this.getValorAcumulado(), this.fechaFirmaContrato,
				this.diaLimiteRadicacion, this.diasRadicacion, this.controlaAnticipos, this.manejaCobertura,
				this.codigoTipoPago,
				this.upc,
				this.pyp,
				this.contratoSecretaria,
				this.nivelAtencionMap,
				this.porcentajeUpc,
				this.base,
				this.requiereautonocobertura,
				this.sinContrato,
				this.frmObservaciones,
				this.pacientePagaAtencion,
				this.validarAbonoAtencionOdo,
				this.manejaTarifasXCA);

		DtoControlAnticiposContrato dtoWhere = new DtoControlAnticiposContrato();
		dtoWhere.setContrato(this.getCodigo());
		dtoWhere.setCodigo(this.getDtoControlAnticipo().getCodigo());
		boolean resp2=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getControlAnticipoContratoDao().modificar(this.getDtoControlAnticipo(), dtoWhere);
		if(Contrato.obtenerControlAnticipos(con, this.getCodigo()).size()>0)
		{
			this.LLenarDtoLogControlAnticipo();
			double resp3=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getLogControlAnticipoContratoDao().guardar(this.getDtoLogControlAnticipo());
		}	
		return resp;
	}
	
	/**
	 * Metodo para eliminar un contratp dado su codigo
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int eliminarContrato(Connection con) throws SQLException 
	{
		int resp=0;
		resp=contratoDao.eliminarContrato(con,this.getCodigo());
		return resp;
	}
	

	/**
	 * Metodo que obtiene todos los resultados de la tabla contratos
	 * para mostrarlos en el listado
	 * @param con
	 * @return
	 */
	public Collection listadoConvenio(Connection con, int codigoInstitucion)
	{
		ContratoDao consulta= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao();
		Collection coleccion=null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(consulta.listado(con, codigoInstitucion));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo contrato " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}

	/**
	 * Metodo que contiene los resultados de la buqueda de contratos,
	 * segun los criterios dados en la busqueda avanzada. 
	 * @param con
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection resultadoBusquedaAvanzada(Connection con, int codigoInstitucion, String manejaTarifasXCA)
	{
		ContratoDao consulta= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao();
		Collection coleccion=null;
		try
		{	
				coleccion=UtilidadBD.resultSet2Collection(consulta.busqueda(	con,
																				this.nombreConvenio,
																				this.numeroContrato,
																				this.eleccionFechaBusqueda,
																				this.fechaInicial,
																				this.fechaFinal,
																				codigoInstitucion,
																				this.valorContrato,
																				this.valorAcumulado,
																				this.nombreTipoPago,
																				this.upc,
																				this.pyp,
																				this.contratoSecretaria,
																				this.busquedaNivelAtencion,
																				this.porcentajeUpc,
																				this.base,
																				this.requiereautonocobertura, 
																				this.sinContrato,
																				this.esquemaTarInventario,
																				this.esquemaTarProcedimiento,
																				this.frmObservaciones,
																				manejaTarifasXCA
																				));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo contrato " +e.toString());
			coleccion=null;
		}
		return coleccion;		
	}
	
	
	 /**
	 * Metodo que permite que actualiza unicamente el valor acumulado del contrato, 
	 * dado su codigo y el valoAcumAsumar - este valor se actualiza en la generacion
	 * de las facturas con el VrContrato 
	 * 
	 * @param con Conexion con la fuente de datos
	 * @param codigo, cod del contrato
	 * @param valorAcumASumar
	 * @return
	 */
	public static int actualizarValorAcumulado(Connection con, int codigo, double valorAcumASumar)
	{
	    return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().actualizarValorAcumulado(con, codigo, valorAcumASumar);
	}
	
	/**
	 * Adiciones Sebasti�n
	 * Metodo que carga el codigo del contrato de una subcuenta o cuenta
	 * @param con
	 * @param id
	 * @param opcion si es true es cuenta y si es false es subcuenta
	 * @return
	 */
	public int cargarCodigoContrato(Connection con,int id,boolean opcion){
		return contratoDao.cargarCodigoContrato(con,id,opcion);
	}
	
	/**
	 * Adicion Sebastian
	 * Metodo que carga los datos del contrato a partir del id de la cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 * devuelve true si la carga fue existosa, de lo contrario false
	 */
	public boolean cargarContratoPorSubCuenta(Connection con,double subCuenta)
	{
		HashMap datosContrato=contratoDao.cargarContratoPorSubCuenta(con,subCuenta);
		int numRegistros=Integer.parseInt(datosContrato.get("numRegistros")+"");
		if(numRegistros>0)
		{
			this.setCodigo(Integer.parseInt(datosContrato.get("codigo_0")+""));
			this.setEstaVencido(UtilidadTexto.getBoolean(datosContrato.get("esta_vencido_0")+""));
			this.setNumeroContrato(datosContrato.get("numero_contrato_0")+"");
			//queda pendiente a�adir los dem�s atributos del contrato
			//solo habr�a que hacer adiciones en el SqlBase y en la consulta
			return true;
		}
		else
			return false;
	}

	/**
	 * carga los niveles
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public void cargarNivelesContratos(Connection con, String codigoContratoStr)
	{
		this.setNivelAtencionMap(contratoDao.cargarNivelesContratos(con, codigoContratoStr));
	}
	
	/**
	 * obtiene el contrato de una cuenta dada
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static int getContratoCuenta(Connection con, String idCuenta)
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		contratoDao = myFactory.getContratoDao();
		return contratoDao.getContratoCuenta(con, idCuenta);
	}
	
	
	/**
	 * metodo que obiene los cpntratos vencidos dado la cuenta y sus contratos
	 * @param con
	 * @param cuenta
	 * @param contratos
	 * @return
	 */
	public static HashMap obtenerContratosVencidosXCuenta (Connection con, String cuenta, Vector contratos)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().obtenerContratosVencidosXCuenta(con, cuenta, contratos);
	}
	
	/**
	 * 
	 * @param con
	 * @param contratos
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap obtenerContratosVencidos(Connection con, Vector contratos) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().obtenerContratosVencidos(con, contratos);
	}
	
	/**
	 * 
	 * @param con
	 * @param contratos
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap obtenerContratosVencidos(Vector contratos) 
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap mapa=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().obtenerContratosVencidos(con, contratos);
		UtilidadBD.closeConnection(con);
		return mapa;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param contratos
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap obtenerContratosTopesCompletos (Connection con, Vector contratos, String cuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().obtenerContratosTopesCompletos(con, contratos, cuenta);
	}
	
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores v�lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicializaci�n fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
	
		if (myFactory != null)
		{
			contratoDao = myFactory.getContratoDao();
			wasInited = (contratoDao != null);
		}
		return wasInited;
	}

	/**
	 * metodo que valida que no exista el mismo numero de Contrato para el convenio cuando las fechas se traslapen
	 * @param con
	 * @param convenioAInsertar
	 * @param numeroContratoAInsertar
	 * @param fechaInicialAInsertar
	 * @param fechaFinalAInsertar
	 * @param codigoContratoCuandoEsModificacion
	 * @return
	 */
	public HashMap validacionNumeroContrato(Connection con, String convenioAInsertar, String numeroContratoAInsertar, String fechaInicialAInsertar, String fechaFinalAInsertar, String codigoContratoCuandoEsModificacion)
	{
		return contratoDao.validacionNumeroContrato(con, convenioAInsertar, numeroContratoAInsertar, fechaInicialAInsertar, fechaFinalAInsertar, codigoContratoCuandoEsModificacion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public static boolean estaContratoVencido(Connection con, int codigoContrato)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().estaContratoVencido(con, codigoContrato);
	}
	
	/**
	 * metodo que indica si un conterato maneja o no cobertura
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public static boolean manejaCobertura(Connection con, int codigoContrato) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().manejaCobertura(con, codigoContrato);
	}
	
	/**
	 * metodo que indica si un conterato maneja o no cobertura
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public static boolean manejaCobertura(int codigoContrato) throws IPSException
	{
		Connection con=UtilidadBD.abrirConexion();
		boolean a= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().manejaCobertura(con, codigoContrato);
		UtilidadBD.closeConnection(con);
		return a;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public static boolean requiereAutorizacionXNoCobertura(Connection con, int codigoContrato)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().requiereAutorizacionXNoCobertura(con, codigoContrato);
	}
	
	/**
	 * 
	 * @param con
	 * @param contrato
	 * @param valorAnticipoPresupuesto
	 * @return
	 */
	public static boolean modificarValorAnticipoReservadoPresupuesto(Connection con, int contrato, BigDecimal valorAnticipoPresupuesto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getControlAnticipoContratoDao().modificarValorAnticipoReservadoPresupuesto(con, contrato, valorAnticipoPresupuesto);
	}
	
	/**
	 * 
	 * @param con
	 * @param contrato
	 * @param valorAnticipo
	 * @return
	 */
	public static boolean modificarValorAnticipoUtilizadoFactura(Connection con,int contrato, BigDecimal valorAnticipo) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getControlAnticipoContratoDao().modificarValorAnticipoUtilizadoFactura(con, contrato, valorAnticipo);
	}
	
	/**
	 *  Retorna  codigo axioma del contrato
	 * @return
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 *  Retorna codigo del convenio al cual corresponde el contrato
	 * @return
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 *  Retorna la fecha final del contrato
	 * @return
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 *  Retorna la fecha inicial del contrato
	 * @return
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/** 
	 * Retorna el n�mero del contrato
	 * @return
	 */
	public String getNumeroContrato() {
		return numeroContrato;
	}
	
	/** 
	 * Asigna  codigo axioma del contrato
	 * @param i
	 */
	public void setCodigo(int i) {
		codigo = i;
	}

	/**
	 * Asigna codigo del convenio al cual corresponde el contrato
	 * @param i
	 */
	public void setCodigoConvenio(int i) {
		codigoConvenio = i;
	}

	/**
	 * Asigna la fecha final del contrato
	 * @param string
	 */
	public void setFechaFinal(String string) {
		fechaFinal = string;
	}

	/**
	 * Asigna la fecha inicial del contrato
	 * @param string
	 */
	public void setFechaInicial(String string) {
		fechaInicial = string;
	}

	/**
	 * Asigna el n�mero del contrato
	 * @param string
	 */
	public void setNumeroContrato(String string) {
		numeroContrato = string;
	}

	/**
	 * Para la busqueda, 
	 * Contiene el nombre del convenio 
	 * (tabla : convenios - campo: nombre)
	 * @return
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}

	/**
	 * Para la busqueda
	 * Contiene el nombre del tipo de contrato
	 * (table : tipos_contrato - campo: nombre)
	 * @return
	 */
/*	public String getNombreTipoContrato() {
		return nombreTipoContrato;
	}*/

	/**
	 *  Para la busqueda, 
	 * Contiene el nombre del convenio 
	 * (tabla : convenios - campo: nombre)
	 * @param string
	 */
	public void setNombreConvenio(String string) {
		nombreConvenio = string;
	}


	/**
	 * Para la busqueda
	 * Contiene el nombre del tipo de contrato
	 * (table : tipos_contrato - campo: nombre)
	 * @param string
	 */
	/*public void setNombreTipoContrato(String string) {
		nombreTipoContrato = string;
	}*/

	/**
	 * retorna el valor true=fechaInicial, false=fechaFinal
	 * para la busqueda por rangos de fechas
	 */
	public String getEleccionFechaBusqueda() {
		return eleccionFechaBusqueda;
	}

	/**
	 * asigna el valor true=fechaInicial, false=fechaFinal
	 * para la busqueda por rangos de fechas
	 */
	public void setEleccionFechaBusqueda(String string) {
		eleccionFechaBusqueda = string;
	}

    /**
     * @return Returns the valorAcumulado.
     */
    public double getValorAcumulado() {
        return valorAcumulado;
    }
    /**
     * @param valorAcumulado The valorAcumulado to set.
     */
    public void setValorAcumulado(double valorAcumulado) {
        this.valorAcumulado = valorAcumulado;
    }
    /**
     * @return Returns the valorContrato.
     */
    public double getValorContrato() {
        return valorContrato;
    }
    /**
     * @param valorContrato The valorContrato to set.
     */
    public void setValorContrato(double valorContrato) {
        this.valorContrato = valorContrato;
    }
	public boolean isEstaVencido() {
		return estaVencido;
	}
	public void setEstaVencido(boolean estaVencido) {
		this.estaVencido = estaVencido;
	}

	/**
	 * @return Returns the codigoTipoContrato.
	 */
	public String getCodigoTipoContrato() {
		return codigoTipoContrato;
	}

	/**
	 * @param codigoTipoContrato The codigoTipoContrato to set.
	 */
	public void setCodigoTipoContrato(String codigoTipoContrato) {
		this.codigoTipoContrato = codigoTipoContrato;
	}

	/**
	 * @return Returns the codigoTipoPago.
	 */
	public String getCodigoTipoPago() {
		return codigoTipoPago;
	}

	/**
	 * @param codigoTipoPago The codigoTipoPago to set.
	 */
	public void setCodigoTipoPago(String codigoTipoPago) {
		this.codigoTipoPago = codigoTipoPago;
	}

	/**
	 * @return Returns the contratoSecretaria.
	 */
	public String getContratoSecretaria() {
		return contratoSecretaria;
	}

	/**
	 * @param contratoSecretaria The contratoSecretaria to set.
	 */
	public void setContratoSecretaria(String contratoSecretaria) {
		this.contratoSecretaria = contratoSecretaria;
	}

	/**
	 * @return Returns the nivelAtencionMap.
	 */
	public HashMap getNivelAtencionMap() {
		return nivelAtencionMap;
	}

	/**
	 * @param nivelAtencionMap The nivelAtencionMap to set.
	 */
	public void setNivelAtencionMap(HashMap nivelAtencionMap) {
		this.nivelAtencionMap = nivelAtencionMap;
	}

	/**
	 * @return Returns the pyp.
	 */
	public String getPyp() {
		return pyp;
	}

	/**
	 * @param pyp The pyp to set.
	 */
	public void setPyp(String pyp) {
		this.pyp = pyp;
	}

	/**
	 * @return Returns the upc.
	 */
	public String getUpc() {
		return upc;
	}

	/**
	 * @param upc The upc to set.
	 */
	public void setUpc(String upc) {
		this.upc = upc;
	}
	/**
	 * @return Returns the nivelAtencionMap.
	 */
	public String getNivelAtencionMap(String key) {
		return nivelAtencionMap.get(key).toString();
	}
	/**
	 * @param nivelAtencionMap The nivelAtencionMap to set.
	 */
	public void setNivelAtencionMap(String key, String valor) {
		this.nivelAtencionMap.put(key, valor);
	}
	/**
	 * @param nivelAtencionMap The nivelAtencionMap to set.
	 */
	public int getNumeroRegistrosNivelAtencionMap() 
	{
		if(this.getNivelAtencionMap().containsKey("numRegistros"))
			return Integer.parseInt(this.getNivelAtencionMap("numRegistros"));
		else
			return 0;
	}

	/**
	 * @return Returns the busquedaNivelAtencion.
	 */
	public String getBusquedaNivelAtencion() {
		return busquedaNivelAtencion;
	}

	/**
	 * @param busquedaNivelAtencion The busquedaNivelAtencion to set.
	 */
	public void setBusquedaNivelAtencion(String busquedaNivelAtencion) {
		this.busquedaNivelAtencion = busquedaNivelAtencion;
	}

	/**
	 * @return Returns the nombreTipoPago.
	 */
	public String getNombreTipoPago() {
		return nombreTipoPago;
	}

	/**
	 * @param nombreTipoPago The nombreTipoPago to set.
	 */
	public void setNombreTipoPago(String nombreTipoPago) {
		this.nombreTipoPago = nombreTipoPago;
	}

	/**
	 * @return Returns the base.
	 */
	public String getBase() {
		return base;
	}

	/**
	 * @param base The base to set.
	 */
	public void setBase(String base) {
		this.base = base;
	}

	/**
	 * @return Returns the nombreBase.
	 */
	public String getNombreBase() {
		return nombreBase;
	}

	/**
	 * @param nombreBase The nombreBase to set.
	 */
	public void setNombreBase(String nombreBase) {
		this.nombreBase = nombreBase;
	}

	/**
	 * @return Returns the porcentajeUpc.
	 */
	public String getPorcentajeUpc() {
		return porcentajeUpc;
	}

	/**
	 * @param porcentajeUpc The porcentajeUpc to set.
	 */
	public void setPorcentajeUpc(String porcentajeUpc) {
		this.porcentajeUpc = porcentajeUpc;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getControlaAnticipos() {
		return controlaAnticipos;
	}
	
	/**
	 * 
	 * @param controlaAnticipos
	 */
	public void setControlaAnticipos(String controlaAnticipos) {
		this.controlaAnticipos = controlaAnticipos;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDiaLimiteRadicacion() {
		return diaLimiteRadicacion;
	}
	
	/**
	 * 
	 * @param diaLimiteRadicacion
	 */
	public void setDiaLimiteRadicacion(String diaLimiteRadicacion) {
		this.diaLimiteRadicacion = diaLimiteRadicacion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDiasRadicacion() {
		return diasRadicacion;
	}
	
	/**
	 * 
	 * @param diasRadicacion
	 */
	public void setDiasRadicacion(String diasRadicacion) {
		this.diasRadicacion = diasRadicacion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFechaFirmaContrato() {
		return fechaFirmaContrato;
	}
	
	/**
	 * 
	 * @param fechaFirmaContrato
	 */
	public void setFechaFirmaContrato(String fechaFirmaContrato) {
		this.fechaFirmaContrato = fechaFirmaContrato;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getManejaCobertura() {
		return manejaCobertura;
	}
	
	/**
	 * 
	 * @param manejaCobertura
	 */
	public void setManejaCobertura(String manejaCobertura) {
		this.manejaCobertura = manejaCobertura;
	}

	/**
	 * 
	 * @param con
	 * @param contrato
	 * @return
	 */
	public static boolean acumuladoMenorValorContrato(Connection con, int contrato) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().acumuladoMenorValorContrato(con, contrato);
	}

	/**
	 * 
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public static String obtenerNumeroContrato(Connection con,int codigoContrato)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().obtenerNumeroContrato(con, codigoContrato);
	}

	/**
	 * 
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public static String obtenerNumeroContrato(int codigoContrato)
	{
		Connection con= UtilidadBD.abrirConexion();
		String num= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().obtenerNumeroContrato(con, codigoContrato);
		UtilidadBD.closeConnection(con);
		return num;
	}
	
	/**
	 * 
	 * @param codigoContrato
	 * @return
	 */
	public static ArrayList<DtoControlAnticiposContrato> obtenerControlAnticipos(Connection con, int codigoContrato)
	{
		ArrayList<DtoControlAnticiposContrato> array = null;
		DtoControlAnticiposContrato dto= new DtoControlAnticiposContrato();
		dto.setContrato(codigoContrato);
		
		try{ 
			array= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getControlAnticipoContratoDao().cargar(con, dto);
		} catch (Exception e) {
			Log4JManager.error(e);
		}
		
		return array;
	}
	
	
	/**
	 * @return the requiereautonocobertura
	 */
	public String getRequiereAutorizacionNoCobertura() {
		return requiereautonocobertura;
	}

	/**
	 * @param requiereautonocobertura the requiereautonocobertura to set
	 */
	public void setRequiereAutorizacionNoCobertura(
			String requiereautonocobertura) {
		this.requiereautonocobertura = requiereautonocobertura;
	}

	@SuppressWarnings("unchecked")
	public HashMap getEsquemasInventario() {
		return esquemasInventario;
	}

	@SuppressWarnings("unchecked")
	public void setEsquemasInventario(HashMap esquemasInventario) {
		this.esquemasInventario = esquemasInventario;
	}

	@SuppressWarnings("unchecked")
	public HashMap getEsquemasProcedimientos() {
		return esquemasProcedimientos;
	}

	@SuppressWarnings("unchecked")
	public void setEsquemasProcedimientos(HashMap esquemasProcedimientos) {
		this.esquemasProcedimientos = esquemasProcedimientos;
	}

	/**
	 * 
	 * @param con
	 * @param codigoEsquema
	 * @param esInventario
	 * @return
	 */
	public boolean eliminarEsquema(Connection con, String codigoEsquema,boolean esInventario) 
	{
		return contratoDao.eliminarEsquema(con,codigoEsquema,esInventario);
	}

	/**
	 * 
	 * @param con
	 * @param codigoEsquema
	 * @return
	 */
	public HashMap consultarEsquemaInventarioLLave(Connection con, String codigoEsquema) 
	{
		return contratoDao.consultarEsquemaInventarioLLave(con,codigoEsquema);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEsquema
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap consultarEsquemaProcedimientoLLave(Connection con, String codigoEsquema) 
	{
		return contratoDao.consultarEsquemaProcedimientoLLave(con,codigoEsquema);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 */
	@SuppressWarnings("unchecked")
	public boolean modificarEsquemasInventario(Connection con, HashMap vo) 
	{
		return contratoDao.modificarEsquemasInventario(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean insertarEsquemasInventario(Connection con, HashMap vo) 
	{
		return contratoDao.insertarEsquemasInventario(con,vo);

	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 */
	public boolean modificarEsquemasProcedimientos(Connection con, HashMap vo) 
	{
		return contratoDao.modificarEsquemasProcedimientos(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarEsquemasProcedimientos(Connection con, HashMap vo) 
	{
		return contratoDao.insertarEsquemasProcedimientos(con,vo);
	}

	/**
	 * 
	 * @return
	 */
	public String getSinContrato() {
		return sinContrato;
	}

	/**
	 * 
	 * @param sinContrato
	 */
	public void setSinContrato(String sinContrato) {
		this.sinContrato = sinContrato;
	}

	public String getEsquemaTarInventario() {
		return esquemaTarInventario;
	}

	public void setEsquemaTarInventario(String esquemaTarInventario) {
		this.esquemaTarInventario = esquemaTarInventario;
	}

	public String getEsquemaTarProcedimiento() {
		return esquemaTarProcedimiento;
	}

	public void setEsquemaTarProcedimiento(String esquemaTarProcedimiento) {
		this.esquemaTarProcedimiento = esquemaTarProcedimiento;
	}

	/**
	 * 
	 * @param con
	 * @param fechaEvaluarVigencia
	 * @return
	 */
	public static HashMap obtenerEsquemasTarifariosInventariosVigentes(Connection con,int codigoIngreso, String fechaEvaluarVigencia) 
	{
		String fecha=fechaEvaluarVigencia;
		if(UtilidadTexto.isEmpty(fecha))
			fecha=UtilidadFecha.getFechaActual(con);
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().obtenerEsquemasTarifariosInventariosVigentes(con,codigoIngreso,fecha);
		
	}

	/**
	 * 
	 * @param con
	 * @param fechaEvaluarVigencia
	 * @return
	 */
	public static HashMap obtenerEsquemasTarifariosProcedimientosVigentes(Connection con,int codigoIngreso, String fechaEvaluarVigencia) 
	{
		String fecha=fechaEvaluarVigencia;
		if(UtilidadTexto.isEmpty(fecha))
			fecha=UtilidadFecha.getFechaActual(con);
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().obtenerEsquemasTarifariosProcedimientosVigentes(con,codigoIngreso,fecha);
	}
	
	/**
	 * 
	 * @param con
	 * @param fechaEvaluarVigencia
	 * @return
	 */
	/*
	public static HashMap obtenerEsquemasTarifariosInventariosVigentes(Connection con,String contrato, String fechaEvaluarVigencia) 
	{
		String fecha=fechaEvaluarVigencia;
		if(UtilidadTexto.isEmpty(fecha))
			fecha=UtilidadFecha.getFechaActual(con);
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().obtenerEsquemasTarifariosInventariosVigentes(con,contrato,fecha);
		
	}
	*/

	/**
	 * 
	 * @param con
	 * @param fechaEvaluarVigencia
	 * @return
	 */
	/*
	public static HashMap obtenerEsquemasTarifariosProcedimientosVigentes(Connection con,String contrato, String fechaEvaluarVigencia) 
	{
		String fecha=fechaEvaluarVigencia;
		if(UtilidadTexto.isEmpty(fecha))
			fecha=UtilidadFecha.getFechaActual(con);
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().obtenerEsquemasTarifariosProcedimientosVigentes(con,contrato,fecha);
	}
	 */
	/**
	 * 
	 * @param con
	 * @param contrato
	 * @param fechaEvaluarVigencia
	 * @return
	 */
	public static HashMap obtenerEsquemasTarifariosInventariosVigentesFecha(Connection con, String contrato,  String fechaEvaluarVigencia) 
	{
		String fecha=fechaEvaluarVigencia;
		if(UtilidadTexto.isEmpty(fecha))
			fecha=UtilidadFecha.getFechaActual(con);
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().obtenerEsquemasTarifariosInventariosVigentesFecha(con,contrato,fecha);
	}

	/**
	 * 
	 * @param con
	 * @param contrato
	 * @param fechaEvaluarVigencia
	 * @return
	 */
	public static HashMap obtenerEsquemasTarifariosProcedimientosVigentesFecha(Connection con, String contrato,  String fechaEvaluarVigencia) 
	{
		String fecha=fechaEvaluarVigencia;
		if(UtilidadTexto.isEmpty(fecha))
			fecha=UtilidadFecha.getFechaActual(con);
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().obtenerEsquemasTarifariosProcedimientosVigentesFecha(con,contrato,fecha);
	}


	
	
	/**	 frmObservaciones	 */
	public String getFrmObservaciones() {		return frmObservaciones;	}
	public void setFrmObservaciones(String frmObservaciones) {	this.frmObservaciones = frmObservaciones;	}

	/**
	 * @return the requiereautonocobertura
	 */
	public String getRequiereautonocobertura() {
		return requiereautonocobertura;
	}

	/**
	 * @param requiereautonocobertura the requiereautonocobertura to set
	 */
	public void setRequiereautonocobertura(String requiereautonocobertura) {
		this.requiereautonocobertura = requiereautonocobertura;
	}

	/**
	 * @return the dtoControlAnticipo
	 */
	public DtoControlAnticiposContrato getDtoControlAnticipo() {
		return dtoControlAnticipo;
	}

	/**
	 * @param dtoControlAnticipo the dtoControlAnticipo to set
	 */
	public void setDtoControlAnticipo(DtoControlAnticiposContrato dtoControlAnticipo) {
		this.dtoControlAnticipo = dtoControlAnticipo;
	}

	/**
	 * @return the dtoLogControlAnticipo
	 */
	public DtoLogControlAnticipoContrato getDtoLogControlAnticipo() {
		return dtoLogControlAnticipo;
	}

	/**
	 * @param dtoLogControlAnticipo the dtoLogControlAnticipo to set
	 */
	public void setDtoLogControlAnticipo(
			DtoLogControlAnticipoContrato dtoLogControlAnticipo) {
		this.dtoLogControlAnticipo = dtoLogControlAnticipo;
	}

	public String getValidarAbonoAtencionOdo() {
		return validarAbonoAtencionOdo;
	}

	public void setValidarAbonoAtencionOdo(String validarAbonoAtencionOdo) {
		this.validarAbonoAtencionOdo = validarAbonoAtencionOdo;
	}

	public String getPacientePagaAtencion() {
		return pacientePagaAtencion;
	}

	public void setPacientePagaAtencion(String pacientePagaAtencion) {
		this.pacientePagaAtencion = pacientePagaAtencion;
	}
	
	/**
	 * 	
	 * @param codigoContrato
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean pacientePagaAtencion( int codigoContrato)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().pacientePagaAtencion(codigoContrato);
	}
		
	/**
	 * 
	 * @param codigoContrato
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean pacienteValidaBonoAtenOdo( int codigoContrato)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().pacienteValidaBonoAtenOdo(codigoContrato);
	}
	
	/**
	 * 	
	 * @param codigoContrato
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean controlaAnticipos( int codigoContrato)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getContratoDao().controlaAnticipos(codigoContrato);
	}

	public String getTipoConvenio() {
		return tipoConvenio;
	}

	public void setTipoConvenio(String tipoConvenio) {
		this.tipoConvenio = tipoConvenio;
	}

	/**
	 * @return the manejaTarifasXCA
	 */
	public boolean isManejaTarifasXCA() {
		return manejaTarifasXCA;
	}

	/**
	 * @param manejaTarifasXCA the manejaTarifasXCA to set
	 */
	public void setManejaTarifasXCA(boolean manejaTarifasXCA) {
		this.manejaTarifasXCA = manejaTarifasXCA;
	}
}
