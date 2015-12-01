/*
 * @(#)AnulacionFacturas.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.interfaces.ConstantesBDInterfaz;
import util.interfaces.UtilidadBDInterfaz;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.AnulacionFacturasDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.dto.interfaz.DtoInterfazAbonos;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.cargos.PagosPaciente;
import com.princetonsa.mundo.manejoPaciente.AperturaIngresos;

/**
 * Clase para el manejo de la anulacion de facturas
 * @version 1.0, Agosto 22, 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class AnulacionFacturas 
{
    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(AnulacionFacturas.class);
    
	/**
	 * DAO de este objeto, para trabajar con Servicio en
	 * la fuente de datos
	 */
    private static AnulacionFacturasDao anulacionDao;
	
    
    /**
	 * constructor vacio
	 */
	public AnulacionFacturas()
	{
	    this.reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * codigo de anulacion= codigo factura
	 */
	private int codigoAnulacion;
	
	/**
	 * consecutivo de anulacion
	 */
	private String consecutivoAnulacion;
	
	/**
	 * fecha de anulacion
	 */
	private String fechaAnulacion;
	
	/**
	 * hora de anulacion
	 */
	private String horaAnulacion;
	
	/**
	 * login del usuario que anula
	 */
	private String loginUsuario;
	
	/**
	 * ( codigo-nombre ) estado de la cuenta
	 */
	private InfoDatosInt estadoCuenta;
	
	/**
	 * ( codigo-nombre ) estado de la cuenta Madre
	 */
	private InfoDatosInt estadoCuentaMadre;
	
	/**
	 * (codigo-descripcion del motivo de anulacion)
	 */
	private InfoDatosInt motivoAnulacion;
	
	/**
	 * codigo del ingreso paciente asociado a la factura
	 */
	private int ingresoPaciente;
	
	/**
	 * observaciones adicionales al motivo de anulacion
	 */
	private String observaciones;
	
	/**
	 * resetea los atributos del objeto
	 *
	 */
	public void reset()
	{
	    this.codigoAnulacion=ConstantesBD.codigoNuncaValido;
	    this.consecutivoAnulacion="";
	    this.fechaAnulacion="";
	    this.horaAnulacion="";
	    this.loginUsuario="";
	    this.estadoCuenta= new InfoDatosInt();
	    this.estadoCuentaMadre= new InfoDatosInt();
	    this.motivoAnulacion= new InfoDatosInt();
	    this.observaciones="";
	    this.ingresoPaciente=ConstantesBD.codigoNuncaValido;
	}
    
	/**
	 * Método que carga el codigo del movimiento abono
	 * @param codigoFactura
	 * @param con
	 * @return
	 */
	public static int cargarCodigoMovimientoAbono(int codigoFactura, Connection con)
	{
	    return AbonosYDescuentos.cargarCodigoPKMovimientosAbono(con, ConstantesBD.tipoMovimientoAbonoFacturacion, codigoFactura);
	}
	
	/**
	 * Método que carga el codigo del movimiento abono
	 * @param codigoFactura
	 * @param con
	 * @return
	 */
	public static int cargarCodigoMovimientoAbonoReserva(int codigoFactura, Connection con)
	{
	    return AbonosYDescuentos.cargarCodigoPKMovimientosAbono(con, ConstantesBD.tipoMovimientoAbonoEntradaReservaAbono, codigoFactura);
	}
	
	
	/**
	 * Método que actualiza en una transaccion los movimientos de abonos para el tipo anulacion
	 * @param codigoFactura
	 * @param estadoTransaccion
	 * @param con
	 * @param codigoMovimientoAbono
	 * @return
	 */
	public static boolean actualizacionAbonosTransaccional(	int codigoFactura,
														String estadoTransaccion,				
														Connection con, int codigoMovimientoAbono, int codigoCentroAtencion)
	{
	    return AbonosYDescuentos.insertarClonMovimientoAbonosDadoCodigo(con, ConstantesBD.tipoMovimientoAbonoAnulacionFactura, codigoMovimientoAbono, codigoCentroAtencion)>0;
	}
	
	
	/**
	 * Método que actualiza en una transaccion los movimientos de abonos para el tipo anulacion
	 * @param codigoFactura
	 * @param estadoTransaccion
	 * @param con
	 * @param codigoMovimientoAbono
	 * @return
	 */
	public static boolean actualizacionAbonosReservaTransaccional(	int codigoFactura,
																	String estadoTransaccion,				
																	Connection con, int codigoMovimientoAbono, int codigoCentroAtencion)
	{
	    return AbonosYDescuentos.insertarClonMovimientoAbonosDadoCodigo(con, ConstantesBD.tipoMovimientoAbonoSalidaReservaAbono, codigoMovimientoAbono, codigoCentroAtencion)>0;
	}
	
	/**
	 * Método para actualizar la información de anulación en los pagos paciente
	 * @param codigoFactura
	 * @param consecutivoFactura
	 * @param estadoTransaccion
	 * @param con
	 * @param objetoValidacionesAnulacion
	 * @param user
	 * @return
	 */
	public boolean actualizacionTopesTransaccional (	int codigoFactura,
														String consecutivoFactura,
														String estadoTransaccion,
														Connection con, 
														ValidacionesAnulacionFacturas objetoValidacionesAnulacion,
														UsuarioBasico user,
														String consecutivoAnulacion)
	{
	    if(objetoValidacionesAnulacion.existeValorBrutoPaciente(con, codigoFactura) && objetoValidacionesAnulacion.getValorBrutoPacMenosValDescuento(con, codigoFactura+"")>0)
	    {
	        PagosPaciente objetoPagosPaciente= new PagosPaciente();
	        String anulado="Anulacion No. "+consecutivoAnulacion;
	        try
	        {
	            if(objetoPagosPaciente.actuallizarAnulacionPagosPacienteDadoFacturaTransaccional(con, anulado, user.getLoginUsuario(), user.getCodigoInstitucionInt(), ConstantesBD.codigoOrigenPagoInterno, consecutivoFactura, estadoTransaccion))
	            {
	                logger.info("RETORNA TRUE EN TOPES!!!!!!!!!!!!!!!!!!");
	                return true;
	            }
	            else
	            {
	                return false;
	            }
	        }
	        catch (SQLException sqle)
	        {
	            logger.warn("No se pudo actualizacionTopes para el cod de factura: "+codigoFactura+" error--->"+sqle);
		        return false;
	        }
	    }
	    //si entra a ca tiene que retornar true;
	    return true;
	}
	
	/**
	 * Método para la actualizacion del estado de la cuenta dependiendo del caso de facturacion presentado con antelacion
	 * @param con
	 * @param codigoFactura
	 * @param objetoValidacionesAnulacion
	 * @param estadoTransaccion
	 * @return
	 * @throws SQLException 
	 */
	public boolean actualizacionEstadoCuentaTransaccional(	Connection con, 
						        							int codigoFactura,
						        							ValidacionesAnulacionFacturas objetoValidacionesAnulacion,
						        							int codigoEstadoCuenta,
						        							String estadoTransaccion) throws SQLException 
	{
	    int codigoCuenta= objetoValidacionesAnulacion.getCodigoCuentaDadaFactura(con, codigoFactura);
	    Cuenta objetoCuenta= new Cuenta();
	    return objetoCuenta.cambiarEstadoCuentaTransaccional(con, codigoEstadoCuenta, codigoCuenta, estadoTransaccion)>0;
	}

	/**
	 * Metodo para insertar la anulacion de facturas
	 * @param con
	 * @param codigoFactura
	 * @param consecutivoAnulacion
	 * @param motivoAnulacion
	 * @param observaciones
	 * @param loginUsuario
	 * @param codigoInstitucion
	 * @param contabilizado
	 * @return
	 * @throws SQLException
	 */
	public  static boolean  insertarAnulacionFacturaTransaccional(		Connection con,
																		int codigoFactura,	
																		String consecutivoAnulacion,
																		int motivoAnulacion, 
																		String observaciones,
																		String loginUsuario,
																		int codigoInstitucion,
																		String contabilizado,
																		String estado
																	) throws SQLException
	{
	    boolean insertado=false;
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    if (estado.equals(ConstantesBD.inicioTransaccion))
	    {
	        if (!myFactory.beginTransaction(con))
	        {
	            myFactory.abortTransaction(con);
	        }
	    }
	    try
	    {
	        insertado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAnulacionFacturasDao().insertarAnulacionFactura(con, codigoFactura, consecutivoAnulacion, motivoAnulacion, observaciones, loginUsuario, codigoInstitucion, contabilizado);
	        if (!insertado)
	        {
	           myFactory.abortTransaction(con);
	        }
	    }
	    catch (SQLException e)
	    {
	        myFactory.abortTransaction(con);
	        throw e;
	    }
	    if (estado.equals(ConstantesBD.finTransaccion))
	    {
	        myFactory.endTransaction(con);
	    }
	    return insertado; 
	}
	
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( anulacionDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			anulacionDao= myFactory.getAnulacionFacturasDao();
			if( anulacionDao!= null )
				return true;
		}
		return false;
	}
	
	/**
	 * Método para cargar los datos pertinentes al resumen
	 * @param con conexión
	 * @param  empresa
	 * @return
	 * @throws SQLException
	 */
	public boolean detalleAnulacion(Connection con, int codigoAnulacionFactura)
	{
		try
		{
			ResultSetDecorator rs=anulacionDao.detalleAnulacionFactura(con, codigoAnulacionFactura);
			
			if (rs.next())
			{
				this.codigoAnulacion=rs.getInt("codigo");
				this.consecutivoAnulacion= rs.getString("consecutivoAnulacion");
				this.fechaAnulacion=rs.getString("fechaAnulacion");
				this.horaAnulacion=rs.getString("horaAnulacion");
				this.loginUsuario= rs.getString("loginUsuario");
				this.estadoCuenta= new InfoDatosInt(rs.getInt("codigoEstadoCuenta"), rs.getString("nombreEstadoCuenta"));
				this.estadoCuentaMadre= new InfoDatosInt(rs.getInt("codigoEstadoCuentaMadre"), rs.getString("nombreEstadoCuentaMadre"));
				this.motivoAnulacion= new InfoDatosInt(rs.getInt("codigoMotivoAnulacion"),"", rs.getString("descripcionMotivoAnulacion"));
				this.observaciones= rs.getString("observacionesAnulacion");
				this.ingresoPaciente=rs.getInt("ingresopaciente");
				
				return true;
			}
			else
				return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en detalleAnulacion de AnulacionFacturas: "+e);
			return false;
		}
	
	}
	
	/**
	 * Metodo que carga el Listado de anulación factura
	 * @param con
	 * @param empresaInstitucion 
	 * @param codigoFacturaAnulacion
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public Collection listadoAnulacionFactura(	Connection con, int codigoInstitucion, 
												String consecutivoFactura, String consecutivoAnulacion,
												String fechaInicialAnulacion, String fechaFinalAnulacion,
												String loginUsuarioAnulacion, 
												int codigoCentroAtencion, String empresaInstitucion)
	{
	    Collection<?> coleccion=null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(anulacionDao.listadoAnulacionFactura(con, codigoInstitucion, consecutivoFactura, 
			        																			consecutivoAnulacion, fechaInicialAnulacion, fechaFinalAnulacion, loginUsuarioAnulacion, codigoCentroAtencion,empresaInstitucion));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo anulacion facturas " +e.toString());
			coleccion=null;
		}
		return coleccion;
	}
	
	/**
	 * Actualizo el valor acumulado del contrato
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String actualizarValorAcumuladoContrato(Connection con, BigDecimal codigoFactura)
	{
		int codigoContratoFactura=Factura.obtenerContratoFactura(con, codigoFactura.intValue());
	    if(codigoContratoFactura<1)
	    {
	        return "error.factura.yaEstaAnulada";
        }
	    else
	    {
	    	double valorConvenio=Factura.obtenerValorConvenioFactura(con, codigoFactura+"");
	    	valorConvenio=valorConvenio*-1;
	    	if(Contrato.actualizarValorAcumulado(con, codigoContratoFactura, valorConvenio)<=0)
		    {
		    	return "error.factura.yaEstaAnulada";
    		}
	    }
	    return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @param usuario
	 * @param ingreso
	 * @param numeroIdentificacionPaciente
	 * @return
	 */
	public static String insertarInterfazContable(Connection con, BigDecimal codigoFactura, UsuarioBasico usuario, int ingreso, String numeroIdentificacionPaciente)
	{
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazContableFacturas(usuario.getCodigoInstitucionInt())))
		{
		    ArrayList<DtoFactura> facturaArray= new ArrayList<DtoFactura>();
		    facturaArray.add(Factura.cargarFactura(con, codigoFactura+"", false/*cargarDetalles*/));
		    // lo que habia antes del consecutivo ingreso era objetoCuenta.getCodigoIngreso()
		    ResultadoBoolean resultadoBoolean= UtilidadBDInterfaz.insertarInterfazContableFactura(con, facturaArray, usuario, Utilidades.obtenerConsecutivoIngreso(con, ingreso+"")+"", numeroIdentificacionPaciente, true);
			
	    	if (!resultadoBoolean.isTrue())
	    	{	
	    		return resultadoBoolean.getDescripcion();
	    	}	
		}
		return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	public static String insertarInterfazTesoreria(Connection con, BigDecimal codigoFactura, UsuarioBasico usuario, PersonaBasica paciente)
	{
		if(	UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazPaciente(usuario.getCodigoInstitucionInt())) || 
		    	UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazAbonosTesoreria(usuario.getCodigoInstitucionInt())))
		{
	    	DtoFactura factura=Factura.cargarFactura(con, codigoFactura+"", false/*cargarDetalles*/);
	    	DtoInterfazAbonos dtoInterfazAbonos= new DtoInterfazAbonos();
	    	dtoInterfazAbonos.setCodigoPaciente(paciente.getCodigoPersona()+"");
			dtoInterfazAbonos.setEstadoRegistro(ConstantesBDInterfaz.codigoEstadoPacienteNoProcesado+"");
			dtoInterfazAbonos.setInstitucion(usuario.getCodigoInstitucionInt());
			dtoInterfazAbonos.setNumeroDocumento(factura.getConsecutivoFactura()+"");
			dtoInterfazAbonos.setSigno("+");
			dtoInterfazAbonos.setTipoMov(ConstantesBDInterfaz.codigoTipoMovimientoAnularfactura+"");
			dtoInterfazAbonos.setValor(factura.getValorAbonos()+"");
			dtoInterfazAbonos.setTipoIdentificacion(paciente.getCodigoTipoIdentificacionPersona());
			dtoInterfazAbonos.setNumIdentificacion(paciente.getNumeroIdentificacionPersona());
			
			//modificado por anexo 779
	    	ResultadoBoolean resultadoBoolean=UtilidadBDInterfaz.insertarInterfazTesoreria(usuario, dtoInterfazAbonos);
	    	
	    	if (!resultadoBoolean.isTrue())
	    	{	
	    		return resultadoBoolean.getDescripcion();
	    	}	
	 	}
		return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static boolean estaFacturaAnulada(Connection con, int codigoFactura) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAnulacionFacturasDao().estaFacturaAnulada(con, codigoFactura);
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static boolean actualizarEstadoDetalleCargo(Connection con, String codigoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAnulacionFacturasDao().actualizarEstadoDetalleCargo(con, codigoFactura);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @param usuario 
	 * @return
	 */
	public static boolean revertirAbonos(Connection con, BigDecimal codigoFactura, int codigoCentroAtencion)
	{
		ValidacionesAnulacionFacturas objetoValidacionesAnulacion= new ValidacionesAnulacionFacturas();
		 //6. VERIFICAMOS LA EXISTENCIA DE LOS ABONOS
	    if(objetoValidacionesAnulacion.existeValorAbonoFactura(con, codigoFactura.intValue()))
	    {    
	    	int codigoMovimientoAbono=AnulacionFacturas.cargarCodigoMovimientoAbono(codigoFactura.intValue(), con);
		    if(codigoMovimientoAbono<=0)
		    {
		    	return false;
		    }
		    else
		    {
		        if(!actualizacionAbonosTransaccional(codigoFactura.intValue(), ConstantesBD.continuarTransaccion, con, codigoMovimientoAbono, codigoCentroAtencion))
		        {
		            return false;
		        }
		    }
		    
		    ///verificamos si existe tipoMovimientoAbonoEntradaReservaAbono para odontologia
		    codigoMovimientoAbono=AnulacionFacturas.cargarCodigoMovimientoAbonoReserva(codigoFactura.intValue(), con);
		    if(codigoMovimientoAbono>0)
		    {	
		        if(!actualizacionAbonosReservaTransaccional(codigoFactura.intValue(), ConstantesBD.continuarTransaccion, con, codigoMovimientoAbono, codigoCentroAtencion))
		        {
		            return false;
		        }
		    }
	    }
	    return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @param cerrarCuenta
	 * @param mostrarAdvertenciaIngresos
	 * @param codigoIngreso
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String actualizarEstadoCuentaIngreso(	Connection con, 
														BigDecimal codigoFactura, 
														String consecutivoFactura,
														int cerrarCuenta, 
														boolean mostrarAdvertenciaIngresos,
														int codigoIngreso,
														UsuarioBasico usuario)
	{
		ValidacionesAnulacionFacturas objetoValidacionesAnulacion= new ValidacionesAnulacionFacturas();
		Cuenta objetoCuenta = new Cuenta();
		String estadoIngreso="";
	    int estadoCuenta=ConstantesBD.codigoNuncaValido;
	    int estadoCuentaAsocio=ConstantesBD.codigoNuncaValido;
	    boolean cuentaTieneOtraFacturaFacturada=objetoValidacionesAnulacion.cuentaTieneMinimoOtraFacturaFacturada(con, codigoFactura.intValue(), objetoValidacionesAnulacion.getCodigoCuentaDadaFactura(con, codigoFactura.intValue()));
	    IngresoGeneral objetoIngresoGeneral= new IngresoGeneral();
	    
	    //Se setea el codigo de ingreso para ser utilizado en el flujo siguiente
	    objetoCuenta.setCodigoIngreso(codigoIngreso);
	    
	    if(cerrarCuenta==1 && !mostrarAdvertenciaIngresos &&!cuentaTieneOtraFacturaFacturada)
	    {
	    	estadoIngreso=ConstantesIntegridadDominio.acronimoEstadoCerrado;
	    	estadoCuenta=ConstantesBD.codigoEstadoCuentaCerrada;
	    	estadoCuentaAsocio=ConstantesBD.codigoEstadoCuentaCerrada;
	    }
	    else
	    {
	    	if(cuentaTieneOtraFacturaFacturada)
	    	{
	    		estadoCuenta=ConstantesBD.codigoEstadoCuentaFacturadaParcial;
	    		estadoCuentaAsocio=ConstantesBD.codigoEstadoCuentaFacturadaParcial;
	    		estadoIngreso= (!mostrarAdvertenciaIngresos)?ConstantesIntegridadDominio.acronimoEstadoAbierto:ConstantesIntegridadDominio.acronimoEstadoCerrado;
	    	}
	    	else
	    	{
	    		estadoCuenta=ConstantesBD.codigoEstadoCuentaActiva;
	    		estadoCuentaAsocio=ConstantesBD.codigoEstadoCuentaAsociada;
	    		estadoIngreso=(!mostrarAdvertenciaIngresos)?ConstantesIntegridadDominio.acronimoEstadoAbierto:ConstantesIntegridadDominio.acronimoEstadoCerrado;
	    	}
	    }
	    
	    int codigoCuentaPpal=objetoValidacionesAnulacion.getCodigoCuentaPrincipalDadaFactura(con, codigoFactura.intValue());
	    Vector<String> vectorCuentasAsociadas= objetoValidacionesAnulacion.getCodigoCuentasAsociadas(con, codigoIngreso);
	    
	    //si la cuenta queda activa o facturada parcial entonces reversamos el egreso
	    if(estadoCuenta==ConstantesBD.codigoEstadoCuentaActiva || estadoCuenta==ConstantesBD.codigoEstadoCuentaFacturadaParcial)
	    {
	    	try 
	    	{
				if(!objetoIngresoGeneral.reversarFechaHoraEgresoTransaccional(con, codigoIngreso, ConstantesBD.continuarTransaccion))
				{
					return "error.facturacion.actualizacionIngreso";
				}
			} 
	    	catch (SQLException e) 
	    	{
	    		return "error.facturacion.actualizacionIngreso";
			}
	    }
	    else if(estadoCuenta==ConstantesBD.codigoEstadoCuentaCerrada)
	    {
	    	 if(objetoCuenta.registrarCierreCuenta(con, codigoCuentaPpal, usuario.getLoginUsuario(), "Cuenta Cerrada en el proceso de Anulacion de la Factura "+consecutivoFactura, ConstantesBD.continuarTransaccion)<=0)
	    	 {
	    		 return "error.facturacion.anulacionEstadoCuenta";
	    	 }
	    	 if(vectorCuentasAsociadas.size()>0)
	    	 {
	    		 for(int w=0; w<vectorCuentasAsociadas.size();w++)
	    		 {	 
		    		 if(objetoCuenta.registrarCierreCuenta(con, Integer.parseInt(vectorCuentasAsociadas.get(w)), usuario.getLoginUsuario(), "Cuenta Cerrada en el proceso de Anulacion de la Factura "+consecutivoFactura, ConstantesBD.continuarTransaccion)<=0)
			    	 {
			    		 return "error.facturacion.anulacionEstadoCuenta";
			    	 } 
	    		 }	 
	    	 }
	    	 try 
	    	 {
	    		 if(objetoIngresoGeneral.modificarIngresoTransaccional(con, objetoCuenta.getCodigoIngreso()+"", ConstantesBD.continuarTransaccion)<=0)
				 {
				    return "error.facturacion.actualizacionIngreso";
				 }
	    	 } 
	    	 catch (SQLException e) 
	    	 {
	    		 return "error.facturacion.actualizacionIngreso";
	    	 }
	    }
	    
	    //finalmente cambiamos los estados
	    try 
	    {
			if(objetoCuenta.cambiarEstadoCuentaTransaccional(con, estadoCuenta, codigoCuentaPpal, ConstantesBD.continuarTransaccion)<=0)
			{
				return "error.facturacion.anulacionEstadoCuenta";
			}
		} 
	    catch (SQLException e) 
	    {
	    	return "error.facturacion.anulacionEstadoCuenta";
		}
	    if(vectorCuentasAsociadas.size()>0)
	    {
	    	for(int w=0; w<vectorCuentasAsociadas.size();w++)
   		 	{
			    try 
			    {
					if(objetoCuenta.cambiarEstadoCuentaTransaccional(con, estadoCuentaAsocio,  Integer.parseInt(vectorCuentasAsociadas.get(w)), ConstantesBD.continuarTransaccion)<=0)
					{
					    return "error.facturacion.anulacionEstadoCuenta";
					}
				} 
			    catch (NumberFormatException e) 
			    {
			    	return "error.facturacion.anulacionEstadoCuenta";
				} 
			    catch (SQLException e) 
			    {
			    	return "error.facturacion.anulacionEstadoCuenta";
				}
   		 	}    
	    }
	    
	    if(!IngresoGeneral.actualizarEstadoIngreso(con, objetoCuenta.getCodigoIngreso()+"", estadoIngreso, usuario.getLoginUsuario()))
	    {
    	    return "error.facturacion.anulacionEstadoCuenta";
	    }
	    
	    // Identificar el ingreso con apertura generada automaticamente
	    if(estadoIngreso.equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
    	{
			HashMap datosIngreso = new HashMap();
			datosIngreso.put("estado", ConstantesIntegridadDominio.acronimoEstadoAbierto);
			datosIngreso.put("ingreso", objetoCuenta.getCodigoIngreso());
			datosIngreso.put("aperturaAuto", ConstantesBD.acronimoSi);
			datosIngreso.put("usuarioModifica", usuario.getLoginUsuario());
			
			if(!AperturaIngresos.ActualizarEstadoIngreso(con, datosIngreso))
		    {
		        return "error.facturacion.anulacionEstadoCuenta";
		    }
    	}
	    
	    return "";
	}
	
	/**
	 * metodo para revertir los anticipos
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static boolean revertirAnticipos(Connection con, BigDecimal codigoFactura)
	{
		DtoFactura dto= Factura.cargarFactura(codigoFactura, false);
		BigDecimal valorAnticipo= new BigDecimal(dto.getValorAnticipos());
		int contrato=UtilidadesFacturacion.obtenerContratoSubCuenta(con, dto.getSubCuenta()+"");
		
		if(valorAnticipo.doubleValue()>0)
		{	
			if(!Contrato.modificarValorAnticipoReservadoPresupuesto(con, contrato, valorAnticipo))
			{	
				return  false;
			}
			if(!Contrato.modificarValorAnticipoUtilizadoFactura(con, contrato, valorAnticipo.multiply(new BigDecimal(-1))))
			{	
				return false;
			}
		}
		return true;
	}
	
	
    /**
     * @return Returns the codigoAnulacion.
     */
    public int getCodigoAnulacion() {
        return codigoAnulacion;
    }
    /**
     * @param codigoAnulacion The codigoAnulacion to set.
     */
    public void setCodigoAnulacion(int codigoAnulacion) {
        this.codigoAnulacion = codigoAnulacion;
    }
    /**
     * @return Returns the estadoCuenta.
     */
    public InfoDatosInt getEstadoCuenta() {
        return estadoCuenta;
    }
    /**
     * @param estadoCuenta The estadoCuenta to set.
     */
    public void setEstadoCuenta(InfoDatosInt estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
    }
    /**
     * @return Returns the fechaAnulacion.
     */
    public String getFechaAnulacion() {
        return fechaAnulacion;
    }
    /**
     * @param fechaAnulacion The fechaAnulacion to set.
     */
    public void setFechaAnulacion(String fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }
    /**
     * @return Returns the horaAnulacion.
     */
    public String getHoraAnulacion() {
        return horaAnulacion;
    }
    /**
     * @param horaAnulacion The horaAnulacion to set.
     */
    public void setHoraAnulacion(String horaAnulacion) {
        this.horaAnulacion = horaAnulacion;
    }
    /**
     * @return Returns the loginUsuario.
     */
    public String getLoginUsuario() {
        return loginUsuario;
    }
    /**
     * @param loginUsuario The loginUsuario to set.
     */
    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }
    /**
     * @return Returns the motivoAnulacion.
     */
    public InfoDatosInt getMotivoAnulacion() {
        return motivoAnulacion;
    }
    /**
     * @param motivoAnulacion The motivoAnulacion to set.
     */
    public void setMotivoAnulacion(InfoDatosInt motivoAnulacion) {
        this.motivoAnulacion = motivoAnulacion;
    }
    /**
     * @return Returns the observaciones.
     */
    public String getObservaciones() {
        return observaciones;
    }
    /**
     * @param observaciones The observaciones to set.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    /**
     * @return Returns the consecutivoAnulacion.
     */
    public String getConsecutivoAnulacion() {
        return consecutivoAnulacion;
    }
    /**
     * @param consecutivoAnulacion The consecutivoAnulacion to set.
     */
    public void setConsecutivoAnulacion(String consecutivoAnulacion) {
        this.consecutivoAnulacion = consecutivoAnulacion;
    }

	/**
	 * @return the estadoCuentaMadre
	 */
	public InfoDatosInt getEstadoCuentaMadre() {
		return estadoCuentaMadre;
	}

	/**
	 * @param estadoCuentaMadre the estadoCuentaMadre to set
	 */
	public void setEstadoCuentaMadre(InfoDatosInt estadoCuentaMadre) {
		this.estadoCuentaMadre = estadoCuentaMadre;
	}

	/**
	 * @return the ingresoPaciente
	 */
	public int getIngresoPaciente() {
		return ingresoPaciente;
	}

	/**
	 * @param ingresoPaciente the ingresoPaciente to set
	 */
	public void setIngresoPaciente(int ingresoPaciente) {
		this.ingresoPaciente = ingresoPaciente;
	}

}
