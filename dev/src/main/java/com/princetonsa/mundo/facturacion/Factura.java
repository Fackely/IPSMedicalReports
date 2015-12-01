package com.princetonsa.mundo.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;
import util.ResultadoInteger;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.FacturaDao;
import com.princetonsa.dto.facturacion.DtoAsociosDetalleFactura;
import com.princetonsa.dto.facturacion.DtoDetalleFactura;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.dto.facturacion.DtoPaquetizacionDetalleFactura;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.TopesFacturacion;
import com.princetonsa.mundo.parametrizacion.RegistroDiagnosticos;
import com.servinte.axioma.dto.facturacion.DtoInfoCobroPaciente;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.HistoricoEncabezado;

/**
 * 
 * @author wilson
 *
 */
public class Factura 
{
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(Factura.class);
	
	/**
	 * DAO de este objeto,
	 */
    private FacturaDao facturaDao;
	
    /**
     * Método que limpia este objeto
     */
    public void reset()
    {
    	
    }
    
    /**
     * Constructor vacío de esta clase
     */
    public Factura()
    {
        this.reset();
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( facturaDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			facturaDao= myFactory.getFacturaDao();
			if( facturaDao!= null )
				return true;
		}
		return false;
	}
    
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap obtenerSubCuentasAFacturar(Connection con, String idIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().obtenerSubCuentasAFacturar(con, idIngreso);
	}
	
	/**
	 * metodo para obtener el cargo total a facturar x responsable
	 * @param con
	 * @param cuentas
	 * @param subCuenta
	 * @return
	 */
	public static double obtenerValorCargoTotalAFacturarXSubCuenta(Connection con, Vector cuentas, double subCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().obtenerValorCargoTotalAFacturarXSubCuenta(con, cuentas, subCuenta);
	}
	
	/**
	 * metodo que propone la factura y sus detalles 
	 * @param con
	 * @param cuentas
	 * @param subCuentas
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	public static ArrayList<DtoFactura> proponerFacturas(	Connection con,
															Vector cuentas,	
															HashMap subCuentas,
															UsuarioBasico usuario, 
															PersonaBasica paciente) throws IPSException
	{
		ArrayList<DtoFactura> facturas= new ArrayList<DtoFactura>();
		//se crean facturas x subcuenta responsable seleccionado
		String prefijoFactura="";
		double rangoInicial=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		double rangoFinal=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		String resolucion="";
		
		//si no es multiempresa entonces cargamos la informacion de la institucion
		if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt())))
		{	
			ParametrizacionInstitucion institucion= new ParametrizacionInstitucion();
			institucion.cargar(con,usuario.getCodigoInstitucionInt());
			rangoInicial=ConstantesBD.codigoNuncaValidoDoubleNegativo;
			rangoFinal=ConstantesBD.codigoNuncaValidoDoubleNegativo;
			prefijoFactura=institucion.getPrefijoFacturas();
			resolucion=institucion.getResolucion();
			
			try
			{
				rangoInicial=Double.parseDouble(institucion.getRangoInicialFactura()+"");
				rangoFinal=Double.parseDouble(institucion.getRangoFinalFactura()+"");
			}
			catch (Exception e) 
			{
				rangoInicial=ConstantesBD.codigoNuncaValidoDoubleNegativo;
				rangoFinal=ConstantesBD.codigoNuncaValidoDoubleNegativo;
			}
		}
		
		for(int w=0; w<Integer.parseInt(subCuentas.get("numRegistros").toString()); w++)
		{	
			if(	subCuentas.get("seleccionado_"+w).toString().equals(ConstantesBD.acronimoSi))
			{
				int codigoViaIngreso=Cuenta.obtenerCodigoViaIngresoCuenta(con, cuentas.get(0).toString());
				
				double entidadSubcontratada=ConstantesBD.codigoNuncaValidoDoubleNegativo;
				double empresaInstitucion=ConstantesBD.codigoNuncaValidoDoubleNegativo;
				double pacienteEntidadSubcontratada=ConstantesBD.codigoNuncaValidoDoubleNegativo;
				
				if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt())))
				{
					empresaInstitucion= Double.parseDouble(subCuentas.get("empresainstitucion_"+w).toString());
					
					//SE CARGA LA INFORMACION DE LA EMPRESA INSTITUCION
					HashMap mapaBusqueda= new HashMap();
					mapaBusqueda.put("codigo1_", empresaInstitucion+"");
					HashMap mapaEmpresaInstitucion= ParametrizacionInstitucion.consultarEmpresas(con, mapaBusqueda);
					resolucion= mapaEmpresaInstitucion.get("resolucion11_0")+"";
					prefijoFactura= mapaEmpresaInstitucion.get("prefijo12_0")+"";
					
					try
					{
						rangoInicial=Double.parseDouble(mapaEmpresaInstitucion.get("rango_inicial13_0").toString());
						rangoFinal=Double.parseDouble(mapaEmpresaInstitucion.get("rango_final14_0").toString());
					}
					catch (Exception e) 
					{
						rangoInicial=ConstantesBD.codigoNuncaValidoDoubleNegativo;
						rangoFinal=ConstantesBD.codigoNuncaValidoDoubleNegativo;
					}
				}
				
				//estos datos son de entidades subcontratadas que es diferente de los de empresa institucion
				entidadSubcontratada= Double.parseDouble(subCuentas.get("entidadsubcontratada_"+w).toString());
				pacienteEntidadSubcontratada= Double.parseDouble(subCuentas.get("pacienteentidadsubcontratada_"+w).toString());
				
				//cargamos la informacion de los topes de facturacion
				String fechaVigenciaTope=Cuenta.obtenerFechaVigenciaTopeCuenta(cuentas.get(0).toString());
				Log4JManager.info("tiporegimen_-->"+subCuentas.get("tiporegimen_"+w));
				Log4JManager.info("clasificacionsocioeconomica_-->"+subCuentas.get("clasificacionsocioeconomica_"+w));
				Log4JManager.info("tipomonto_-->"+subCuentas.get("tipomonto_"+w));
				int topeFacturacion=TopesFacturacion.cargarTopeParaFacturacion(con, subCuentas.get("tiporegimen_"+w).toString(), Integer.parseInt(subCuentas.get("clasificacionsocioeconomica_"+w).toString()), Integer.parseInt(subCuentas.get("tipomonto_"+w).toString()), fechaVigenciaTope);
				
				DtoFactura dtoFactura= proponerFactura(	con, 
														cuentas, 
														Integer.parseInt(subCuentas.get("codigoconvenio_"+w).toString()),
														Integer.parseInt(subCuentas.get("contrato_"+w).toString()),	
														subCuentas.get("nombreresponsable_"+w).toString(),
														codigoViaIngreso,
														Utilidades.obtenerNombreViaIngreso(con, codigoViaIngreso),
														Utilidades.convertirAEntero(subCuentas.get("montocobro_"+w)+""),
														Utilidades.convertirAEntero(subCuentas.get("tipomonto_"+w)+""),
														subCuentas.get("nombretipomonto_"+w).toString(),
														subCuentas.get("tipoafiliado_"+w).toString(),
														Double.parseDouble(subCuentas.get("subcuenta_"+w).toString()), 
														Integer.parseInt(subCuentas.get("clasificacionsocioeconomica_"+w).toString()),
														subCuentas.get("nomclassocioeconomica_"+w).toString(),
														Cuenta.obtenerCentroAtencionCuenta(con, cuentas.get(0).toString()), 
														Double.parseDouble(subCuentas.get("valorcargototal_"+w).toString()), 
														Double.parseDouble(subCuentas.get("valorbrutopaciente_"+w).toString()), 
														Double.parseDouble(subCuentas.get("valorconvenio_"+w).toString()),
														subCuentas.get("tiporegimen_"+w).toString(),
														subCuentas.get("nombretiporegimen_"+w).toString(),
														UtilidadTexto.getBoolean(subCuentas.get("pactieneexcepcionnaturaleza_"+w).toString()),
														UtilidadTexto.getBoolean(subCuentas.get("esparticular_"+w).toString()),
														prefijoFactura,
														Integer.parseInt(subCuentas.get("codigoformatoimpresion_"+w).toString()),
														subCuentas.get("nombreformatoimpresion_"+w).toString(),
														usuario, 
														paciente,
														UtilidadTexto.getBoolean(subCuentas.get("validarinfovenezuela_"+w).toString()),
														Double.parseDouble(subCuentas.get("valormontoautorizadovenezuela_"+w).toString()),
														ConstantesBD.codigoNuncaValidoDoubleNegativo/*valorFaltaAsignarVenezuela*/,
														entidadSubcontratada,
														empresaInstitucion,
														pacienteEntidadSubcontratada,
														resolucion,
														rangoInicial,
														rangoFinal,
														Double.parseDouble(subCuentas.get("valorliquidadopaciente_"+w).toString()),
														topeFacturacion,
														subCuentas.get("fechavigenciamontocobro_"+w).toString(),
														fechaVigenciaTope,
														Utilidades.convertirAEntero(subCuentas.get("centroatencionduenio_"+w)+""),
														(DtoInfoCobroPaciente)subCuentas.get("calculoValorPaciente_"+w));
				
				facturas.add(dtoFactura);
			}
		}	
		return facturas;
	}
	
	
	/**
	 * metodo para proponer la factura basica a generar
	 * @param con
	 * @param cuentas
	 * @param codigoConvenio
	 * @param nombreConvenio
	 * @param codigoViaIngreso
	 * @param nombreViaIngreso
	 * @param montoCobro
	 * @param codigoTipoMonto
	 * @param nombreTipoMonto
	 * @param porcentajeMonto
	 * @param valorMonto
	 * @param subCuenta
	 * @param codigoEstratoSocial
	 * @parma nombreEstratoSocial
	 * @param centroAtencion
	 * @param valorTotal
	 * @param valorBrutoPaciente
	 * @param valorConvenio
	 * @param acronimoTipoRegimen
	 * @param nombreTiporegimen
	 * @param pacienteTieneExcepcionNaturaleza
	 * @param esParticular
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	public static DtoFactura proponerFactura(	Connection con, 
												Vector cuentas, 
												int codigoConvenio, 
												int codigoContrato,
												String nombreConvenio,
												int codigoViaIngreso,
												String nombreViaIngreso,
												Integer montoCobro, 
												int codigoTipoMonto,
												String nombreTipoMonto,
												String tipoAfiliado,
												double subCuenta, 
												int codigoEstratoSocial,
												String nombreEstratoSocial,
												int centroAtencion, 
												double valorTotal, 
												double valorBrutoPaciente, 
												double valorConvenio,
												String acronimoTipoRegimen,
												String nombreTipoRegimen,
												boolean pacienteTieneExcepcionNaturaleza,
												boolean esParticular,
												String prefijoFactura,
												int codigoFormatoFactura,
												String nombreFormatoFactura,
												UsuarioBasico usuario, 
												PersonaBasica paciente,
												boolean validarInfoVenezuela,
												double valorMontoAutorizadoVenezuela,
												double valorFaltaAsignarVenezuela,
												double entidadSubcontratada,
												double empresaInstitucion,
												double pacienteEntidadSubcontratada,
												String resolucion,
												double rangoInicialFactura,
												double rangoFinalFactura,
												double valorLiquidadoPaciente,
												int topeFacturacion,
												String fechaVigenciaInicialMontoCobro,
												String fechaVigenciaInicialTopeFacturacion,
												int centroAtencionDuenio,
												DtoInfoCobroPaciente inforCalculoValorPaciente)
	{
		//para estas vias de ingreso cargamos el dx para hacer el calculo de los topes
		String diagnosticoEgresoAcronimoTipoCie="";
		if(codigoViaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion || codigoViaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
		{
			diagnosticoEgresoAcronimoTipoCie= RegistroDiagnosticos.cargarDiagnosticoHospUrg(con, Integer.parseInt(cuentas.get(0).toString()));
		}
		
		DtoFactura facturaDto= new DtoFactura(	ConstantesBD.codigoNuncaValido /*codigo*/,  	
												"" /*fecha*/, 
												"" /*hora*/, 
												new InfoDatosInt(ConstantesBD.codigoEstadoFacturacionFacturada) /*estadoFacturacion*/, 
												new InfoDatosInt(ConstantesBD.codigoEstadoFacturacionPacientePorCobrar) /*estadoPaciente*/, 
												usuario.getLoginUsuario(),  
												ConstantesBD.codigoNuncaValidoDoubleNegativo /*consecutivoFactura*/, 
												prefijoFactura /*prefijoFactura*/, 
												usuario.getCodigoInstitucionInt(), 
												0 /*valorPagos SE GUARDA EN CERO*/, 
												0 /*ajustesCredito SE GUARDA EN CERO*/, 
												0 /*ajustesDebito SE GUARDA EN CERO*/, 
												ConstantesBD.codigoNuncaValidoDoubleNegativo /*valorAbonos SE MODIFICA CON ABONOS*/, 
												valorTotal, 
												valorBrutoPaciente /*valorNetoPaciente SE MODIFICA CON ABONOS*/, 
												valorConvenio /*SE MODIFICA CON ABONOS*/, 
												valorConvenio /*valorCartera ES EL MISMO VALOR CONVENIO*/, 
												valorBrutoPaciente /*valorBrutoPaciente SE MODIFICA EN ABONOS*/, 
												ConstantesBD.codigoNuncaValidoDoubleNegativo /*valorDescuentoPaciente SE MODIFICA EN ABONOS*/, 
												ConstantesBD.codigoNuncaValidoDoubleNegativo /*numeroCuentaCobro NULL*/, 
												true /*tipoFacturaSistema*/, 
												paciente.getCodigoPersona() /*codigoPaciente*/, 
												ConstantesBD.codigoNuncaValido /*codigoResonsableParticular*/, 
												cuentas, 
												new InfoDatosInt(codigoConvenio, nombreConvenio), 
												new InfoDatosInt(codigoViaIngreso, nombreViaIngreso), 
												montoCobro, 
												subCuenta, 
												false /*facturaCerrada*/, 
												tipoAfiliado /*tipoAfiliado*/, 
												new InfoDatosInt(codigoEstratoSocial, nombreEstratoSocial), 
												new InfoDatosInt(codigoTipoMonto, nombreTipoMonto) /*tipoMonto*/, 
												ConstantesBD.codigoNuncaValidoDoubleNegativo /*cuentaCobroCapitacion NULL*/, 
												new InfoDatosInt(centroAtencion), 
												"" /*tipoComprobante*/, 
												ConstantesBD.codigoNuncaValidoDouble /*nroComprobante*/, 
												new ArrayList<DtoDetalleFactura>()/*ArrayList<DtoDetalleFactura> detallesFactura*/,
												new ArrayList<DtoDetalleFactura>()/*ArrayList<DtoDetalleFactura> detallesFacturaExcentas*/,
												diagnosticoEgresoAcronimoTipoCie /*diagnosticoEgresoAcronimoTipoCie*/,
												new InfoDatos(acronimoTipoRegimen, nombreTipoRegimen),
												pacienteTieneExcepcionNaturaleza,
												esParticular,
												false /*valorBrutoPacienteModificadoXEvento MODIFICADO X TOPES*/, 
												false /*valorBrutoPacienteModificadoXAnioCalendario MODIFICADO X TOPES*/, 
												ConstantesBD.codigoNuncaValidoDoubleNegativo /*valorAFavorConvenio MODIFICADO X TOPES*/,
												new InfoDatosInt(codigoFormatoFactura, nombreFormatoFactura),
												resolucion,
												rangoInicialFactura,
												rangoFinalFactura,
												validarInfoVenezuela,
												valorMontoAutorizadoVenezuela,
												valorFaltaAsignarVenezuela,
												entidadSubcontratada,
												empresaInstitucion,
												pacienteEntidadSubcontratada,
												valorLiquidadoPaciente,
												false /*valorAFavorConvenioModificado MODIFICADO X TOPES*/,
												topeFacturacion,
												fechaVigenciaInicialMontoCobro,
												fechaVigenciaInicialTopeFacturacion,
												new InfoDatosInt(centroAtencionDuenio,""),
												0 /*valorAnticipos*/,
												inforCalculoValorPaciente);
		
		facturaDto.setContrato(new InfoDatosInt(codigoContrato));
		
		
		facturaDto.setHistoricoEncabezado(new HistoricoEncabezado());
		ValidacionesFacturaOdontologica validaciones= new ValidacionesFacturaOdontologica();
		int tipoFuenteDatos = validaciones.obtenerTipoFuenteDatosFactura(usuario);
		validaciones.obtenerDatosParametrizacionParaFactura(facturaDto.getHistoricoEncabezado(), tipoFuenteDatos, usuario.getCodigoCentroAtencion());
		
		
		ArrayList<DtoDetalleFactura> detallesFactura= proponerDetalleFacturaDesdeCargos(con, facturaDto, ConstantesBD.codigoEstadoFCargada);
		ArrayList<DtoDetalleFactura> detallesFacturaExcenta= proponerDetalleFacturaDesdeCargos(con, facturaDto, ConstantesBD.codigoEstadoFExento);
		
		facturaDto.setDetallesFactura(detallesFactura);
		facturaDto.setDetallesFacturaExcentas(detallesFacturaExcenta);
		
		return facturaDto;
	}
	
	/**
	 * Método que inicializa el proceso de facturación
	 * Inserta en la tabla 
	 * @param con
	 * @param idCuenta
	 * @param loginUsuario
	 * @return true si se inició correctamente el proceso de facturación
	 */
	public boolean empezarProcesoFacturacion(Connection con, int idCuenta, String loginUsuario, String idSesion)
	{
		Cuenta cuenta=new Cuenta();
		try
		{
			int resultado=facturaDao.empezarProcesoFacturacionTransaccional(con, idCuenta, loginUsuario, ConstantesBD.inicioTransaccion, idSesion);
			if(resultado==0)
			{
				UtilidadBD.abortarTransaccion(con);
				return false;
			}
			return cuenta.cambiarEstadoCuentaTransaccional(con, ConstantesBD.codigoEstadoCuentaProcesoFacturacion, idCuenta, ConstantesBD.finTransaccion)>0 && resultado>0;
		}
		catch (SQLException e)
		{
			logger.error("Error cambiando el estado de la cuenta "+idCuenta);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Método para terminar el proceso de facturación
	 * @param con
	 * @param idCuenta
	 * @return true si se finalizó correctamente
	 */
	public boolean finalizarProcesoFacturacionTransaccional(Connection con, int idCuenta, String estado, String idSesion)
	{
		return facturaDao.finalizarProcesoFacturacionTransaccional(con, idCuenta, estado, idSesion)>0;
	}
	
	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @param idSesion
	 * @return
	 */
	public static boolean eliminarCuentaProcesoFacturacion(Connection con, BigDecimal cuenta, String idSesion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().eliminarCuentaProcesoFacturacion(con, cuenta, idSesion);
	}
	
	/**
	 * Método para cancelar el proceso de facturación (transaccional)
	 * @param con
	 * @param idCuenta
	 * @return true si se canceló el proceso correctamente
	 */
	public boolean cancelarProcesoFacturacion(Connection con, int idCuenta, String idSesion)
	{
		try
		{
			if(con==null || con.isClosed())
			{
			con=UtilidadBD.abrirConexion();
			}
		} catch (SQLException e1)
		{
			logger.warn("No se pudo realizar la conexión (Factura)"+e1.toString());
		}
		try
		{
			int resultado=facturaDao.cancelarProcesoFacturacionTransaccional(con, idCuenta, ConstantesBD.inicioTransaccion, idSesion);
			if(resultado==0)
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
				return false;
			}
			return facturaDao.finalizarProcesoFacturacionTransaccional(con, idCuenta, ConstantesBD.finTransaccion, idSesion)>0 && resultado>0;
		}
		catch (SQLException e)
		{
			logger.error("Error abortando la transacción al cancelar el proceso de facturación "+e);
			return false;
		}
	}
	
	
	/**
	 * 
	 * @param con
	 * @param dtoFactura
	 * @return
	 */
	public static ArrayList<DtoDetalleFactura> proponerDetalleFacturaDesdeCargos(Connection con, DtoFactura dtoFactura, int estadoCargo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().proponerDetalleFacturaDesdeCargos(con, dtoFactura, estadoCargo);
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoFactura
	 * @return
	 */
	public static ResultadoInteger insertar(Connection con, DtoFactura dtoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().insertar(con, dtoFactura);
	}
	
	/**
	 * metodo para cargar los detalles de la prefactura
	 * @param con
	 * @param dtoFactura
	 * @return
	 */
	public static HashMap proponerPreFactura(Connection con, DtoFactura dtoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().proponerPreFactura(con, dtoFactura);
	}
	
	/**
	 *  metodo para la actualización del estado de facturacion y el estado del paciente de una factura dada
	 * @param con
	 * @param estadoFacturacion
	 * @param estadoPaciente
	 * @param codigoFactura
	 * @return
	 * @throws SQLException
	 */
	public static boolean actualizarEstadosFactura(Connection con, int estadoFacturacion, int estadoPaciente, int codigoFactura)
	{
	     return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().actualizarEstadosFactura(con, estadoFacturacion, estadoPaciente, codigoFactura);
	}
	
	/**
	 * obtiene el codigo - nombre del centro de atencion de una factura dada
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 */
	public static InfoDatosInt obtenerCentroAtencionFactura( Connection con, String consecutivoFactura, int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().obtenerCentroAtencionFactura(con, consecutivoFactura, codigoInstitucion);
	}
	
	/**
	 * obtiene el codigo - nombre del centro de atencion de una factura dada
	 * @param codigoFactura
	 * @return
	 */
	public static InfoDatosInt obtenerCentroAtencionFactura( Connection con, int codigoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().obtenerCentroAtencionFactura(con, codigoFactura);
	}
	
	/**
	 * Método que consulta el número de cuenta de cobro teniendo como referencia el
	 * consecutivo de la factura
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 * @return numero de cuenta de cobro
	 */
	public static double obtenerCuentaCobro(Connection con, int consecutivoFactura, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().obtenerCuentaCobro(con, consecutivoFactura,institucion);
	}
	
	/**
	 * Método que consulta el número de cuenta de cobro teniendo como referencia el codigo de la factura
	 * @param con
	 * @param codigoFactura
	 * @return numero de cuenta de cobro
	 */
	public static double obtenerCuentaCobro(Connection con, int codigoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().obtenerCuentaCobro(con, codigoFactura);
	}
	
	
	/**
	 * Metodo que realiza la busqueda de una factura por su consecutivo y cod de institucion
	 * @param con
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Collection busquedaPorConsecutivoDianEInstitucion(Connection con, String consecutivoFactura, int codigoInstitucion, String restricciones)
	{
	    Collection coleccion= null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().busquedaPorConsecutivoDianEInstitucion(con, consecutivoFactura, codigoInstitucion, restricciones));
		}
		catch(Exception e)
		{
			coleccion= null;
		}
		return coleccion;
	}
	
	/**
	 * obtiene el valor neto paciente
	 * @param con
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 */
	public static double obtenerValorNetoPaciente(Connection con, String consecutivoFactura, int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().obtenerValorNetoPaciente(con, consecutivoFactura, codigoInstitucion);
	}

	/**
	 * Método que cancela todos los procesos de facturación en proceso
	 * @param con Conexión con la BD
	 * @return numero de cancelaciones
	 */
	public static int cancelarTodosLosProcesosDeFacturacion(Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().cancelarTodosLosProcesosDeFacturacion(con);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static int obtenerContratoFactura(Connection con, int codigoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().obtenerContratoFactura(con, codigoFactura);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @param cargarDetalles
	 * @return
	 */
	public static DtoFactura cargarFactura( Connection con, String codigoFactura, boolean cargarDetalles)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().cargarFactura(con, codigoFactura, cargarDetalles);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @param cargarDetalles
	 * @return
	 */
	public static DtoFactura cargarFactura( BigDecimal codigoFactura, boolean cargarDetalles)
	{
		Connection con= UtilidadBD.abrirConexion();
		DtoFactura dto= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().cargarFactura(con, codigoFactura+"", cargarDetalles);
		UtilidadBD.closeConnection(con);
		return dto;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static double obtenerValorConvenioFactura(Connection con, String codigoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().obtenerValorConvenioFactura(con, codigoFactura);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static int obtenerCodigoPacienteFactura(Connection con, String codigoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().obtenerCodigoPacienteFactura(con, codigoFactura);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static String obtenerIdIngresoFactura( Connection con, String codigoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().obtenerIdIngresoFactura(con, codigoFactura);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static int obtenerFormatoFacturaXCodigoFact(Connection con, String codigoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().obtenerFormatoFacturaXCodigoFact(con, codigoFactura);
	}
	
	/**
	 * actualiza el numero de la cuenta de cobro  en la factura
	 * @param con
	 * @param numeroCuentaCobroCapitada
	 * @param codigoFactura
	 * @return
	 */
	public static boolean updateNumeroCuentaCobroCapitadaEnFactura(Connection con, String numeroCuentaCobroCapitada, String codigoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().updateNumeroCuentaCobroCapitadaEnFactura(con, numeroCuentaCobroCapitada, codigoFactura);
	}
	
	/**
	 * Método usado para desasignar el numero de cuenta de cobro de una factura
	 * en el caso de que se haya hehco una inactivación de factura
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static int desasignarCuentaCobro(Connection con,String codigoFactura,int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().desasignarCuentaCobro(con,codigoFactura,institucion);
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static boolean insertarHistoricosSubCuentas(Connection con, ArrayList<DtoFactura> facturas)
	{
		boolean retornado=true;
		for(int w=0; w<facturas.size(); w++)
		{
			if(!insertarHistoricoSubCuenta(con, facturas.get(w).getSubCuenta(), facturas.get(w).getCodigo()))
				retornado=false;
		}
		return retornado;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static boolean insertarHistoricoSubCuenta(Connection con, double subCuenta, double codigoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().insertarHistoricoSubCuenta(con, subCuenta, codigoFactura);
	}

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @param restricciones
	 * @return
	 */
	public static Collection busquedaPorCodigo(Connection con, int codigoFactura,  String restricciones) 
	{
		Collection coleccion= null;
		try
		{
			coleccion=UtilidadBD.resultSet2Collection(DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().busquedaPorCodigo(con, codigoFactura, restricciones));
		}
		catch(Exception e)
		{
			coleccion= null;
		}
		return coleccion;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static ArrayList<DtoAsociosDetalleFactura> cargarAsociosDetalleFactura(int codigoDetalle)
	{
		Connection con=UtilidadBD.abrirConexion();
		ArrayList<DtoAsociosDetalleFactura> array= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().cargarAsociosDetalleFactura(con, codigoDetalle);
		UtilidadBD.closeConnection(con);
		return array;
	}


	
	
	public static boolean actualizarConsecutivoFactura(Connection con, int codigoFactura,String consecutivo)
	{
	    if(UtilidadTexto.isEmpty(consecutivo))
	    	return false;
	    
	    return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().actualizarConsecutivoFactura(con, codigoFactura,consecutivo);
	    
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	public static boolean actualizarEstadoFacturadoDetalleCargo(Connection con, ArrayList<DtoFactura> listaFacturas) 
	{
		for(DtoFactura dtoFactura: listaFacturas)
		{	
			for(int x=0; x<dtoFactura.getDetallesFactura().size(); x++)
			{	
				DtoDetalleFactura dtoDetalle= dtoFactura.getDetallesFactura().get(x);
				if(!Cargos.actualizarEstadoFacturadoDetalleCargo(con, dtoFactura.getSubCuenta(), dtoDetalle.getNumeroSolicitud(), dtoDetalle.getCodigoServicioOArticulo(),ConstantesBD.codigoNuncaValido, ConstantesBD.acronimoSi/*estadoFacturado*/, dtoDetalle.getEsServicio(),dtoFactura.getCodigo()))
				{
					//logger.info("no actualizo a facturado el cargo de la solicitud "+dtoDetalle.getNumeroSolicitud());
					return false;
				}
				for(int y=0; y<dtoDetalle.getAsociosDetalleFactura().size(); y++)
				{	
					DtoAsociosDetalleFactura dtoAsociosDetalleFactura= dtoDetalle.getAsociosDetalleFactura().get(y);
					if(!Cargos.actualizarEstadoFacturadoDetalleCargo(con, dtoFactura.getSubCuenta(), dtoDetalle.getNumeroSolicitud(), dtoDetalle.getCodigoServicioOArticulo(), dtoAsociosDetalleFactura.getCodigoServicioAsocio(), ConstantesBD.acronimoSi/*estadoFacturado*/, true,dtoFactura.getCodigo()))
					{
						//logger.info("no actualizo a facturado el cargo de la solicitud "+dtoDetalle.getNumeroSolicitud());
						return false;
					}
				}
				for(int y=0; y<dtoDetalle.getPaquetizacionDetalleFactura().size(); y++)
				{	
					DtoPaquetizacionDetalleFactura dtoPaquetizacion= dtoDetalle.getPaquetizacionDetalleFactura().get(y);
					if(!Cargos.actualizarEstadoFacturadoDetalleCargo(con, dtoFactura.getSubCuenta(), dtoPaquetizacion.getSolicitud(), dtoPaquetizacion.getCodigoServicioOArticulo(),ConstantesBD.codigoNuncaValido, ConstantesBD.acronimoSi/*estadoFacturado*/, dtoPaquetizacion.getEsServicio(),dtoFactura.getCodigo()))
					{
						//logger.info("no actualizo a facturado el cargo de la solicitud "+dtoDetalle.getNumeroSolicitud());
						return false;
					}
				}
			}
			
			//ANEXO 777--------------
			for(int x=0; x<dtoFactura.getDetallesFacturaExcentas().size(); x++)
			{
				DtoDetalleFactura dtoDetalle= dtoFactura.getDetallesFacturaExcentas().get(x);
				if(!Cargos.actualizarEstadoFacturadoDetalleCargo(con, dtoFactura.getSubCuenta(), dtoDetalle.getNumeroSolicitud(), dtoDetalle.getCodigoServicioOArticulo(),ConstantesBD.codigoNuncaValido, ConstantesBD.acronimoSi/*estadoFacturado*/, dtoDetalle.getEsServicio(),dtoFactura.getCodigo()))
				{
					//logger.info("no actualizo a facturado el cargo de la solicitud "+dtoDetalle.getNumeroSolicitud());
					return false;
				}
			}
			//-----------------------
		}	
		return true;
	}
	
	
	

	
	/**
	 * Se valida si la solicitud tiene una autorizacion asociada
	 * @param con
	 * @param numeroFactura
	 * @return si tiene  autorizados
	 */
	public static  Boolean  tieneSolicitudesSinAutorizar(Connection con, Integer numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturaDao().tieneSolicitudesSinAutorizar(con, numeroSolicitud);
	}
}
