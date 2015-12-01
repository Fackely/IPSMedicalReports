/*
 * @(#)ValidacionesFactura.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 
 *
 */
package com.princetonsa.mundo.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ElementoApResource;
import util.InfoDatosInt;
import util.RangosConsecutivos;
import util.ResultadoBoolean;
import util.ResultadoString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.interfaces.UtilidadBDInterfaz;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ValidacionesFacturaDao;
import com.princetonsa.dao.sqlbase.facturacion.DtoVerficacionDerechos;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.cargos.GeneracionCargosPendientes;
import com.princetonsa.mundo.cargos.PagosPaciente;
import com.princetonsa.mundo.cargos.TopesFacturacion;
import com.princetonsa.mundo.historiaClinica.Epicrisis1;
import com.princetonsa.mundo.manejoPaciente.ViasIngreso;
import com.princetonsa.mundo.parametrizacion.ExcepcionesNaturalezaPaciente;
import com.princetonsa.mundo.salasCirugia.DevolucionPedidoQx;
import com.princetonsa.mundo.tesoreria.Arqueos;
import com.servinte.axioma.dto.facturacion.DtoInfoCobroPaciente;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.ICalculoValorCobrarPaciente;

/**
 * objeto que carga todos los posibles errores de la generacion de la factura
 * @author wilson
 *
 */
public class ValidacionesFactura 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ValidacionesFactura.class);
	
	/**
	 * 
	 */
	private ArrayList<ElementoApResource> erroresFactura;
	
	/**
	 * DAO de este objeto, para trabajar con Servicios en
	 * la fuente de datos
	 */
	private static ValidacionesFacturaDao validacionesFacturaDao;

	/**
	 * 
	 */
	private ArrayList<ElementoApResource> warningsFactura;
	
	/**
	 * simulacion de los pagos paciente
	 * para evaluar que no supere los topes
	 */
	@SuppressWarnings("unchecked")
	private Hashtable pagosPacienteMEM;
	
	/**
	 * consutructor
	 *
	 */
	public ValidacionesFactura() 
	{
		this.init(System.getProperty("TIPOBD"));
		this.clean();
	}

	/**
	 * Mï¿½todo que limpia los atributos de esta clase
	 *
	 */
	@SuppressWarnings("unchecked")
	public void clean ()
	{
		this.erroresFactura=new ArrayList<ElementoApResource>();
		this.warningsFactura=new ArrayList<ElementoApResource>();
		this.pagosPacienteMEM= new Hashtable();
		this.pagosPacienteMEM.put("numRegistros", 0);
	}
	
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores vï¿½lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( validacionesFacturaDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			validacionesFacturaDao= myFactory.getValidacionesFacturaDao();
			if( validacionesFacturaDao!= null )
				return true;
		}
		return false;
	}
	
	/**
		this.clean();
	 * metodo que verifica:DEBE EXISTIR UN INGRESO (abierto-cerrado) CON UNA CUENTA VALIDA (Activa, Facturada Parcial, Asociada (Incompleta cuenta_final=null)
	 * @param con
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param logger
	 * @return
	 */
	public static ActionForward esIngresoCuentaValidoCargado(Connection con, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, Logger logger)
	{
		if( usuario == null )
		{
		    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No existe el usuario", "errors.usuario.noCargado", true);
		}
		if(paciente.getCodigoPersona()<1 || paciente.getCodigoTipoIdentificacionPersona().equals("") )
		{
		    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no Cargado", "errors.paciente.noCargado", true);
		}
		if(paciente.getCodigoIngreso()<1)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No Existe un Ingreso Cargado", "errors.paciente.noIngresoSesion", true); 
		}
		int idCuenta = 0 ;
		Cuenta cuenta = new Cuenta();
		if(paciente.getExisteAsocio() && paciente.getCodigoCuenta() <=0)
		{
			idCuenta =  Integer.parseInt(cuenta.cargarCuentaUrgenciasAsociada(con,paciente.getCodigoIngreso())); 			
		}
		else
		{
			idCuenta = paciente.getCodigoCuenta();
		}
		if(UtilidadValidacion.esCuentaValida(con, idCuenta)<1)
		{
			if(esCuentaValidaProcesoFacturacion(con, idCuenta)<1)
			{	
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La Cuenta No Es Valida", "errors.paciente.cuentaNoValida", true);
			}	
		}	
		return null;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static int esCuentaValidaProcesoFacturacion(Connection con, int codigoCuenta) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().esCuentaValidaProcesoFacturacion(con, codigoCuenta);
	}
	
	
	/**
	 * 
	 * @param paciente
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Vector obtenerCuentasAValidar(Connection con, PersonaBasica paciente) 
	{
		Vector vectorCuentas= new Vector();
		
		for(int i=0; i<paciente.getCuentasPacienteArray().size(); i++)
			vectorCuentas.add(paciente.getCuentasPacienteArray(i).getCodigoCuenta());		

		return vectorCuentas;
		
		/*
		if(paciente.getCodigoCuenta()>0)
		{	
			vectorCuentas.add(paciente.getCodigoCuenta());
		}
		if(paciente.getExisteAsocio() && paciente.getCodigoCuentaAsocio()>0)
		{
			vectorCuentas.add(paciente.getCodigoCuentaAsocio());
		}
		return vectorCuentas;*/
	}
	
	/**
	 * 
	 * @param con
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean esConsecutivoFacturaDentroRangoInstitucion (Connection con, UsuarioBasico usuario)
	{
		Vector rangos= esRangoFacturaInstitucionValido(con, usuario, true);
		if(!rangos.isEmpty() && esConsecutivoFacturacionValido(con, usuario, true))
		{
			logger.info("rangos");
			double codigoPosibleSiguienteFactura= Utilidades.convertirADouble(UtilidadBD.obtenerValorActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoFacturas, usuario.getCodigoInstitucionInt()));
			logger.info("codigoPosibleSiguienteFactura->"+codigoPosibleSiguienteFactura);
			if (codigoPosibleSiguienteFactura< Double.parseDouble(rangos.get(0).toString()) || codigoPosibleSiguienteFactura> Double.parseDouble(rangos.get(1).toString()))
			{
				this.setErrorFactura("error.facturacion.rangoLimite", ",EL CONSECUTIVO DISPONIBLE ES "+codigoPosibleSiguienteFactura+ " Y DEBE ESTAR ENTRE "+rangos.get(0).toString()+" - "+rangos.get(1).toString());
				logger.info("raNgo limite");
				return false;
			}
		}
		else
		{
			logger.info("rango o consecutivo no valido");
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param usuario
	 * @return
	 */
	public boolean existeConsecutivoInterfaz(Connection con, UsuarioBasico usuario)
	{
		ResultadoString resultadoString=UtilidadBDInterfaz.obtenerConsecutivoFacturas(usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario(), false);
		String consecutivo=resultadoString.getResultado();
		
		logger.info("CONSECUTIVO INTERFAZ------->"+consecutivo);
		if(UtilidadTexto.isEmpty(consecutivo) || Utilidades.convertirADouble(consecutivo, true)<=0)
		{
			ElementoApResource elem=new ElementoApResource("error.facturacion.consecutivoInterfaz");
			this.erroresFactura.add(elem);
			logger.info("consecutivo interfaz no valido");
			return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param empresasInstitucion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean sonConsecutivosFacturasMultiempresaDentroRango( Connection con, Vector empresasInstitucion)
	{
		boolean retorna=true;
		for(int w=0; w<empresasInstitucion.size(); w++)
		{
			if(!esConsecutivoFacturaMultiempresaDentroRango(con, Integer.parseInt(empresasInstitucion.get(w)+"")))
				retorna=false;
		}
		return retorna;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param empresaInstitucion
	 * @return
	 */
	public boolean esConsecutivoFacturaMultiempresaDentroRango( Connection con, double empresaInstitucion )
	{
		Vector<String> rangos= new Vector<String>();
		rangos=esRangoFacturaMultiempresaValido(con, empresaInstitucion, true);
		
		if(rangos!=null)
		{
			double codigoPosibleSiguienteFactura= obtenerSiguientePosibleNumeroFacturaMultiempresa(con, empresaInstitucion);
			logger.info("codigoPosibleSiguienteFacturaMultiempresa->"+codigoPosibleSiguienteFactura);
			if (codigoPosibleSiguienteFactura< Double.parseDouble(rangos.get(0)) || codigoPosibleSiguienteFactura> Double.parseDouble(rangos.get(1)))
			{
				this.setErrorFactura("error.facturacion.rangoLimite", "DE LA EMPRESA-INSTITUCION "+rangos.get(2));
				logger.info("rango limite");
				return false;
			}
		}
		else
		{
			logger.info("rango o consecutivo no valido");
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param empresaInstitucion
	 * @param incremento
	 * @return
	 */
	public static boolean incrementarConsecutivoFacturaMultiempresa(Connection con, double empresaInstitucion,int incremento)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().incrementarConsecutivoFacturaMultiempresa(con, empresaInstitucion, incremento);
	}
	
	/**
	 * metodo que evalua que el rango de factura de una institucion sea valido
	 * @param con
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Vector esRangoFacturaInstitucionValido(Connection con, UsuarioBasico usuario, boolean asignarErrores)
	{
		ParametrizacionInstitucion institucion= new ParametrizacionInstitucion();
		if(institucion.cargar(con,usuario.getCodigoInstitucionInt()))
		{
			logger.info("rango Inicial->"+institucion.getRangoInicialFactura()+" rango final->"+institucion.getRangoFinalFactura());
			if(UtilidadTexto.isNumber(institucion.getRangoInicialFactura()))
			{
				if(Integer.parseInt(institucion.getRangoInicialFactura())<1)
				{	
					if(asignarErrores)
						this.setErrorFactura("error.facturacion.rangoNoValido", "INICIAL");
					return new Vector();
				}	
			}
			else
			{
				if(asignarErrores)
					this.setErrorFactura("error.facturacion.rangoNoValido", "INICIAL");
				return new Vector();
			}
			
			if(UtilidadTexto.isNumber(institucion.getRangoFinalFactura()))
			{
				if(Integer.parseInt(institucion.getRangoFinalFactura())<1)
				{	
					if(asignarErrores)
						this.setErrorFactura("error.facturacion.rangoNoValido", "FINAL");
					return new Vector();
				}	
			}
			else
			{
				if(asignarErrores)
					this.setErrorFactura("error.facturacion.rangoNoValido", "FINAL");
				return new Vector();
			}
		}
		Vector rango= new Vector();
		rango.add(institucion.getRangoInicialFactura());
		rango.add(institucion.getRangoFinalFactura());
		return rango;
	}
	
	/**
	 * 
	 * @param con
	 * @param empresaInstitucion
	 * @param asignarErrores
	 */
	private Vector<String> esRangoFacturaMultiempresaValido(Connection con, double empresaInstitucion, boolean asignarErrores)
	{
		boolean esValido=true;
		Vector<String> rangosMultiempresa= obtenerRangoInicialFinalFacturaMultiempresa(con, empresaInstitucion);
		
		if(rangosMultiempresa.size()>0)
		{
			logger.info("rango Inicial->"+rangosMultiempresa.get(0)+" rango final->"+rangosMultiempresa.get(1)+ " desc empresa->"+rangosMultiempresa.get(2));
			if(Double.parseDouble(rangosMultiempresa.get(0))<1)
			{	
				if(asignarErrores)
				{	
					this.setErrorFactura("error.facturacion.rangoNoValido", "INICIAL DE LA EMPRESA-INSTITUCION "+rangosMultiempresa.get(2));
					esValido=false;
				}	
			}
			if(Double.parseDouble(rangosMultiempresa.get(1))<1)
			{	
				if(asignarErrores)
				{	
					this.setErrorFactura("error.facturacion.rangoNoValido", "FINAL DE LA EMPRESA-INSTITUCION "+rangosMultiempresa.get(2));
					esValido=false;
				}	
			}
		}
		else
		{
			if(asignarErrores)
			{	
				this.setErrorFactura("error.facturacion.empresaInstitucion", empresaInstitucion+"");
				esValido=false;
			}	
		}
		
		if(!esValido)
			return null;
		
		return rangosMultiempresa;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param empresaInstitucion
	 * @return
	 */
	public static Vector<String> obtenerRangoInicialFinalFacturaMultiempresa( Connection con, double empresaInstitucion )
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().obtenerRangoInicialFinalFacturaMultiempresa(con, empresaInstitucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param empresaInstitucion
	 * @return
	 */
	public static RangosConsecutivos obtenerRangoInicialFinalFacturaMultiempresa( double empresaInstitucion )
	{
		RangosConsecutivos rango= null;
		Connection con= UtilidadBD.abrirConexion();
		Vector<String> rangosMultiempresa= obtenerRangoInicialFinalFacturaMultiempresa(con, empresaInstitucion);
		UtilidadBD.closeConnection(con);
		
		if(rangosMultiempresa.size()>0)
		{
			if(Double.parseDouble(rangosMultiempresa.get(0))>=0 && Double.parseDouble(rangosMultiempresa.get(1))>0)
			{
				rango= new RangosConsecutivos( new BigDecimal(rangosMultiempresa.get(0)), new BigDecimal(rangosMultiempresa.get(1)) );
			}
		}
		return rango;
	}
	
	/**
	 * metodo que evalua si un consecutivo de factura es valido
	 * @param con
	 * @param usuario
	 * @return
	 */
	private boolean esConsecutivoFacturacionValido(Connection con, UsuarioBasico usuario, boolean asignarErrores)
	{
		int consecutivoFacturacion=Utilidades.convertirAEntero(UtilidadBD.obtenerValorActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoFacturas, usuario.getCodigoInstitucionInt()));
		
		if(consecutivoFacturacion<1)
		{
			//Verificaion que el consecutivo de facturacion ya halla sido parametrizado
			if(asignarErrores)
			{	
				ElementoApResource elem=new ElementoApResource("error.facturacion.consecutivoNoDisponible");
				this.erroresFactura.add(elem);
			}	
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param empresaInstitucion
	 * @param codigoInstitucion
	 * @return
	 */
	public double obtenerSiguientePosibleNumeroFacturaMultiempresa(Connection con, double empresaInstitucion)
	{
		return validacionesFacturaDao.obtenerSiguientePosibleNumeroFacturaMultiempresa(con, empresaInstitucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param empresaInstitucion
	 * @param codigoInstitucion
	 * @return
	 */
	public BigDecimal obtenerSiguientePosibleNumeroFacturaMultiempresa(double empresaInstitucion)
	{
		Connection con = UtilidadBD.abrirConexion();
		double consecutivo= validacionesFacturaDao.obtenerSiguientePosibleNumeroFacturaMultiempresa(con, empresaInstitucion);
		UtilidadBD.closeConnection(con);
		return new BigDecimal(consecutivo);
	}
	
	/**
	 * 
	 * @param con
	 * @param tipoFacturacion
	 * @param cuentas
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean esEgresoPacienteValido( Connection con, int tipoFacturacion, Vector cuentas, int institucion ) throws IPSException
	{
		for(int w=0; w<cuentas.size(); w++)
		{	
			int codigoViaIngreso= Cuenta.obtenerCodigoViaIngresoCuenta(con, cuentas.get(w).toString());
			if(tipoFacturacion==ConstantesBD.codigoTipoFacturacionPacienteContinuaProcesoAtencion)
			{
				logger.info("codigoTipoFacturacionPacienteContinuaProcesoAtencion no se realiza validacion del egreso");
			}
			else if(tipoFacturacion==ConstantesBD.codigoTipoFacturacionPacienteConAtencionFinalizada)
			{
				///XPLANNER 2008- 59362- 
				
				if(codigoViaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
				{
					try 
					{
						int codigoConsultaSeguir=UtilidadesHistoriaClinica.obtenerCodigoConductaValoracionUrgenciasCuenta(con, cuentas.get(w).toString());
						logger.info("conducta a seguir urgnecias-->"+codigoConsultaSeguir);
						if(!UtilidadValidacion.existeEgresoMedico(con, Integer.parseInt(cuentas.get(w).toString()))
							////para este caso de conducta a seguir no pueden generar evoluciones por ende no es posible generar el egreso mï¿½dico desde la evol	
							&& codigoConsultaSeguir!=ConstantesBD.codigoConductaSeguirSalidaSinObservacion 
							&& codigoConsultaSeguir!=ConstantesBD.codigoConductaSeguirRemitirMayorComplejidad)
						{	
							logger.info("no existe egreso medico!!!! nombreViaIngreso->URGENCIAS");
							Vector atributosVector= new Vector();
							atributosVector.add("MEDICO");
							atributosVector.add("URGENCIAS");
							this.setErrorFactura("error.facturacion.egresoIncompleto", atributosVector);
							return false;
						}
						else if(!UtilidadValidacion.existeEgresoCompleto(con, Integer.parseInt(cuentas.get(w).toString()))
								////para este caso de conducta a seguir no pueden generar evoluciones por ende no es posible generar el egreso mï¿½dico desde la evol	
								&& 
								(codigoConsultaSeguir==ConstantesBD.codigoConductaSeguirSalidaSinObservacion 
								|| codigoConsultaSeguir==ConstantesBD.codigoConductaSeguirRemitirMayorComplejidad))
						{
							Vector atributosVector= new Vector();
							atributosVector.add("AUTOMATICO");
							atributosVector.add("URGENCIAS");
							this.setErrorFactura("error.facturacion.egresoIncompleto", atributosVector);
							return false;
						}
					} 
					catch (NumberFormatException e) 
					{
						e.printStackTrace();
					}
					catch (SQLException e) {
						logger.info("ERROR "+e);
					}
				}
				else if(codigoViaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
				{
					if(UtilidadTexto.getBoolean(ValoresPorDefecto.getLiberarCamaHospitalizacionDespuesFacturar(institucion))
						|| UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(con, cuentas.get(w).toString()).getAcronimo().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
					{
						try 
						{
							if(!UtilidadValidacion.existeEgresoMedico(con, Integer.parseInt(cuentas.get(w).toString())))
							{	
								logger.info("no existe egreso medico!!!! nombreViaIngreso->HOSPITALIZACION");
								Vector atributosVector= new Vector();
								atributosVector.add("MEDICO");
								atributosVector.add("HOSPITALIZACION");
								this.setErrorFactura("error.facturacion.egresoIncompleto", atributosVector);
								return false;
							}	
						} 
						catch (NumberFormatException e) 
						{
							e.printStackTrace();
						}
					}
					else
					{
						try 
						{
							if(!UtilidadValidacion.existeEgresoCompleto(con, Integer.parseInt(cuentas.get(w).toString())))
							{	
								logger.info("no existe egreso completo!!!! nombreViaIngreso->HOSPITALIZACION");
								Vector atributosVector= new Vector();
								atributosVector.add("ADMINISTRATIVO");
								atributosVector.add("HOSPITALIZACION");
								this.setErrorFactura("error.facturacion.egresoIncompleto", atributosVector);
								return false;
							}	
						} 
						catch (NumberFormatException e) 
						{
							e.printStackTrace();
						} 
						catch (SQLException e) 
						{
							e.printStackTrace();
						}
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean existeVerificacionDerechosConvenioIngreso( Connection con, String idIngreso, int codigoConvenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().existeVerificacionDerechosConvenioIngreso(con, idIngreso, codigoConvenio);
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param subCuentas
	 * @param cuentas
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean esVerificacionDerechosValida( Connection con, String idIngreso, HashMap subCuentas, Vector cuentas) throws IPSException
	{
		boolean valido=true;
		logger.info("\n\n\nVERIFICACION DE DERECHOS!!!!!!\n\n\n");
		//primero evaluamos que la via de ingreso  tenga verificacion de derechos en S
		for(int w=0; w<cuentas.size();w++)
		{
			logger.info("Existe verificacion derechos via ingreso? cuenta->"+cuentas.get(w).toString());
			if(ViasIngreso.existeVerificacionDerechosViaIngreso(con, Cuenta.obtenerCodigoViaIngresoCuenta(con, cuentas.get(w).toString())))
			{
				logger.info("existe!!!!!");
				//como existe para la via de ingreso entonces evaluamos para los tipos de regimen
				for(int w1=0; w1<Integer.parseInt(subCuentas.get("numRegistros").toString()); w1++)
				{	
					if(subCuentas.get("seleccionado_"+w1).toString().equals(ConstantesBD.acronimoSi))
					{	
						logger.info("convenio existe verificacion derechos --> codConvenio="+subCuentas.get("codigoconvenio_"+w1).toString());
						if(Convenio.existeVerificacionDerechosConvenio(con, Integer.parseInt(subCuentas.get("codigoconvenio_"+w1).toString())))
						{
							logger.info("EOXISTE VERIFICACION DE DERECHOS TANTO PARA LA CUENTA COMO PARA EL CONVENIO!!!!!!!!");
							logger.info("como en ambas existe entonces se debe verificar que exista el registro en verificacion de derechos en estado aceptado - aprobado");
							//como en ambas existe entonces se debe verificar que exista el registro en verificacion de derechos en estado aceptado - aprobado
							if(!existeVerificacionDerechosConvenioIngreso(con, idIngreso, Integer.parseInt(subCuentas.get("codigoconvenio_"+w1).toString())))
							{
								logger.info("error.facturacion.verificacionDerechos");
								this.setErrorFactura("error.facturacion.verificacionDerechos", subCuentas.get("nombreresponsable_"+w1).toString());
								valido=false;
							}
							else
							{
								logger.info("existe no hay error");
							}
						}
					}	
				}	
			}
		}
		return valido;	
	}
	
	/**
	 * meotodo que evalua que este activo la interfaz shaio y valida que cada convenio seleccionado
	 * tenga su correspondiente codigo de interfaz
	 * @param con
	 * @param subCuentasVector
	 * @param institucion
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean esConvenioInterfazValido(Connection con, HashMap subCuentasMap, int institucion) 
	{
		boolean valido=true;
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazContableFacturas(institucion)))
		{
			for(int w=0; w<Integer.parseInt(subCuentasMap.get("numRegistros").toString()); w++)
			{	
				if(subCuentasMap.get("seleccionado_"+w).toString().equals(ConstantesBD.acronimoSi))
				{
					String convenioInterfaz=Utilidades.obtenerCodigoInterfazConvenioDeCodigo(Utilidades.convertirAEntero(subCuentasMap.get("codigoconvenio_"+w)+""));
					
					if(UtilidadTexto.isEmpty(convenioInterfaz))
					{
						Vector atributosVector= new Vector();
						atributosVector.add("Se tiene activa la interfaz Contable Facturas, por ï¿½sta razï¿½n el -Cï¿½digo Interfaz Convenio- del responsable "+subCuentasMap.get("nombreresponsable_"+w).toString());
						this.setErrorFactura("errors.required", atributosVector);
						valido=false;
					}
				}	
			}
		}
		return valido;
	}

	
	
	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param cuentas
	 * @param subCuentas
	 * @param tipoFacturacion
	 */
	@SuppressWarnings("unchecked")
	public void obtenerSolicitudesEstadosInvalidos (Connection con, UsuarioBasico usuario, Vector cuentas, Vector subCuentas, int tipoFacturacion) 
	{
		//primero obtenemos las solicitudes que tienen cargo pendiente, reutilizamsos la consulta de geberacion de cargos pendientes
		HashMap mapaSolicitudesPendientesXSubCuenta= GeneracionCargosPendientes.obtenerSolicitudesCargoPendiente(con, cuentas, subCuentas);
		
		for(int w=0; w<Integer.parseInt(mapaSolicitudesPendientesXSubCuenta.get("numRegistros").toString()); w++)
		{
			if( mapaSolicitudesPendientesXSubCuenta.get("codigotiposolicitud_"+w).toString().equals(ConstantesBD.codigoTipoSolicitudMedicamentos)
					&& mapaSolicitudesPendientesXSubCuenta.get("codigoestadomedico_"+w).toString().equals(ConstantesBD.codigoEstadoHCSolicitada))
			{
				Vector atributosVector= new Vector();
				atributosVector.add(mapaSolicitudesPendientesXSubCuenta.get("nombretiposolicitud_"+w).toString());
				atributosVector.add(mapaSolicitudesPendientesXSubCuenta.get("consecutivoordenesmedicas_"+w).toString()+" del convenio "+mapaSolicitudesPendientesXSubCuenta.get("nombreconvenio_"+w).toString());
				//La solicitud de {0} No. {1} se encuentra en estado medico SOLICITADA (CREADA), debe estar administrada. [bf-66]
				this.setErrorFactura("error.facturacion.medicamentosSolicitada", atributosVector);
			}	
			else if(mapaSolicitudesPendientesXSubCuenta.get("codigotiposolicitud_"+w).toString().equals(ConstantesBD.codigoTipoSolicitudMedicamentos)
					&& mapaSolicitudesPendientesXSubCuenta.get("codigoestadomedico_"+w).toString().equals(ConstantesBD.codigoEstadoHCDespachada))
			{
				Vector atributosVector= new Vector();
				atributosVector.add(mapaSolicitudesPendientesXSubCuenta.get("nombretiposolicitud_"+w).toString());
				atributosVector.add(mapaSolicitudesPendientesXSubCuenta.get("consecutivoordenesmedicas_"+w).toString()+" del convenio "+mapaSolicitudesPendientesXSubCuenta.get("nombreconvenio_"+w).toString());
				//La solicitud de {0} No. {1} se encuentra en estado medico DESPACHADA, debe estar administrada. [bf-67]
				this.setErrorFactura("error.facturacion.medicamentosDespachada", atributosVector);
			}
			else
			{
				Vector atributosVector= new Vector();
				atributosVector.add(mapaSolicitudesPendientesXSubCuenta.get("nombretiposolicitud_"+w).toString());
				atributosVector.add(mapaSolicitudesPendientesXSubCuenta.get("consecutivoordenesmedicas_"+w).toString()+" del convenio "+mapaSolicitudesPendientesXSubCuenta.get("nombreconvenio_"+w).toString());
				//La Solicitud de {0} No. {1} se encuentra en estado de facturaciï¿½n PENDIENTE. Debe estar Cargada [bf-69]
				this.setErrorFactura("error.facturacion.evolucionEstadoPendiente", atributosVector);
			}
		}
		
		if(tipoFacturacion==ConstantesBD.codigoTipoFacturacionPacienteConAtencionFinalizada)
		{
			//xplanner [id=19832]
			//verificamos que todas las solicitudes de cx esten liquidadas
			HashMap mapaSolicitudeCxSinLiquidar= obtenerSolicitudesCxSinLiquidacion(con, cuentas, subCuentas);
			for(int w=0; w<Integer.parseInt(mapaSolicitudeCxSinLiquidar.get("numRegistros").toString()); w++)
			{
				Vector atributosVector= new Vector();
				atributosVector.add(mapaSolicitudeCxSinLiquidar.get("consecutivoordenesmedicas_"+w).toString()+" del convenio "+mapaSolicitudeCxSinLiquidar.get("nombreconvenio_"+w).toString());
				//La solicitud de Cx No. {1} no esta liquidada, por favor revisar. [bf-63]
				this.setErrorFactura("error.solicitudCx.noLiquidada", atributosVector);
			}
		}	
		
		
//		solo se valida los estados de hc para el tipo de facturacion paciente con atencion finalizada
		//segun Oscar esto aplica para ambos flujos
		if(tipoFacturacion==ConstantesBD.codigoTipoFacturacionPacienteConAtencionFinalizada)
		{
			boolean validarInterpretadas=UtilidadTexto.getBoolean(util.ValoresPorDefecto.getValidarEstadoSolicitudesInterpretadas(usuario.getCodigoInstitucionInt()));
			
			logger.info("\n\nvalidarInterpretadas---->"+validarInterpretadas+"\n\n");
			
			//PARTE DE SERVICIOS
			
			if(validarInterpretadas)
			{
				HashMap mapaSolicitudesInvalidas= obtenerSolicitudesHCInvalidas(con, cuentas, subCuentas, validarInterpretadas);
					
				for(int w=0; w<Integer.parseInt(mapaSolicitudesInvalidas.get("numRegistros").toString()); w++)
				{
					int estadomedico= Utilidades.convertirAEntero(mapaSolicitudesInvalidas.get("codigoestadomedico_"+w)+"");
					int codigotiposolicitud = Utilidades.convertirAEntero(mapaSolicitudesInvalidas.get("codigotiposolicitud_"+w)+"");
					
					if(estadomedico==ConstantesBD.codigoEstadoHCRespondida)
					{
						Vector atributosVector= new Vector();
						atributosVector.add(mapaSolicitudesInvalidas.get("nombretiposolicitud_"+w).toString());
						atributosVector.add(mapaSolicitudesInvalidas.get("consecutivoordenesmedicas_"+w).toString()+" del convenio "+mapaSolicitudesInvalidas.get("nombreconvenio_"+w).toString());
						//La solicitud de {0} No. {1} se encuentra en estado medico RESPONDIDA, debe estar interpretada. [bf-63]
						this.setErrorFactura("error.facturacion.procInterConsultaNoInterpretada", atributosVector);
					}
					else if(estadomedico==ConstantesBD.codigoEstadoHCSolicitada)
					{
						Vector atributosVector= new Vector();
						atributosVector.add(mapaSolicitudesInvalidas.get("nombretiposolicitud_"+w).toString());
						atributosVector.add(mapaSolicitudesInvalidas.get("consecutivoordenesmedicas_"+w).toString()+" del convenio "+mapaSolicitudesInvalidas.get("nombreconvenio_"+w).toString());
						if((codigotiposolicitud==ConstantesBD.codigoTipoSolicitudMedicamentos)||(codigotiposolicitud==ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos))
							this.setErrorFactura("error.facturacion.medicamentosSolicitada", atributosVector);
						else
							//La solicitud de {0} No. {1} se encuentra en estado medico SOLICITADA (CREADA), debe estar interpretada o anulada. [bf-64]
							this.setErrorFactura("error.facturacion.procInterConsultaSolcitada", atributosVector);
					}
				}
			}
			else
			{
				//PARTE DE SERVICIOS
				HashMap mapaSolicitudesInvalidas= obtenerSolicitudesHCInvalidas(con, cuentas, subCuentas, validarInterpretadas);
					
				for(int w=0; w<Integer.parseInt(mapaSolicitudesInvalidas.get("numRegistros").toString()); w++)
				{
					int estadomedico= Utilidades.convertirAEntero(mapaSolicitudesInvalidas.get("codigoestadomedico_"+w)+"");
					int codigotiposolicitud = Utilidades.convertirAEntero(mapaSolicitudesInvalidas.get("codigotiposolicitud_"+w)+"");
					
					if(estadomedico==ConstantesBD.codigoEstadoHCSolicitada && !ValidacionesFactura.generaCargoEnSolicitud(con, Utilidades.convertirAEntero( mapaSolicitudesInvalidas.get("numerosolicitud_"+w)+"")))
					{
						Vector atributosVector= new Vector();
						atributosVector.add(mapaSolicitudesInvalidas.get("nombretiposolicitud_"+w).toString());
						atributosVector.add(mapaSolicitudesInvalidas.get("consecutivoordenesmedicas_"+w).toString()+" del convenio "+mapaSolicitudesInvalidas.get("nombreconvenio_"+w).toString());
						if((codigotiposolicitud==ConstantesBD.codigoTipoSolicitudMedicamentos)||(codigotiposolicitud==ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos))
							this.setErrorFactura("error.facturacion.medicamentosSolicitada", atributosVector);
						else
							//La solicitud de {0} No. {1} se encuentra en estado medico SOLICITADA (CREADA), debe estar interpretada o anulada. [bf-64]
							this.setErrorFactura("error.facturacion.procInterConsultaSolcitada", atributosVector);
					}
				}
			}
		}	
		
		//hermorhu - MT6827
		//Validaciones solo se deben cumplir para pacientes con atencion finalizada
		if(tipoFacturacion==ConstantesBD.codigoTipoFacturacionPacienteConAtencionFinalizada) {
		
			//PARTE DE MEDICAMENTOS
			//LOS ESTADOS DE HC DE LOS MEDICAMENTOS APLICA EN AMBOS FLUJOS DEL TIPO DE FACTURACION YA QUE ESTOS ESTADOS NO DEBEN TENER CARGO
			//ES DECIR, SE VALIDA EL HC QUE EN REALIDAD VERIFICA EL ESTADO DE FACTURACION
			HashMap mapaSolicitudesInvalidas= obtenerSolicitudesHCInvalidasMedicamentos(con, cuentas, subCuentas);
			
			for(int w=0; w<Integer.parseInt(mapaSolicitudesInvalidas.get("numRegistros").toString()); w++)
			{
				
				int estadomedico= Utilidades.convertirAEntero(mapaSolicitudesInvalidas.get("codigoestadomedico_"+w)+"");
				
				int codigotiposolicitud = Utilidades.convertirAEntero(mapaSolicitudesInvalidas.get("codigotiposolicitud_"+w)+"");
				
				if(estadomedico==ConstantesBD.codigoEstadoHCSolicitada)
				{
					Vector atributosVector= new Vector();
					atributosVector.add(mapaSolicitudesInvalidas.get("nombretiposolicitud_"+w).toString());
					atributosVector.add(mapaSolicitudesInvalidas.get("consecutivoordenesmedicas_"+w).toString()+" del convenio "+mapaSolicitudesInvalidas.get("nombreconvenio_"+w).toString());
					if((codigotiposolicitud==ConstantesBD.codigoTipoSolicitudMedicamentos)||(codigotiposolicitud==ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos))
						this.setErrorFactura("error.facturacion.medicamentosSolicitada", atributosVector);
					else
						//La solicitud de {0} No. {1} se encuentra en estado medico SOLICITADA (CREADA), debe estar interpretada o anulada. [bf-64]
						this.setErrorFactura("error.facturacion.procInterConsultaSolcitada", atributosVector);
				}
				else if(estadomedico==ConstantesBD.codigoEstadoHCDespachada)
				{
					Vector atributosVector= new Vector();
					atributosVector.add(mapaSolicitudesInvalidas.get("nombretiposolicitud_"+w).toString());
					atributosVector.add(mapaSolicitudesInvalidas.get("consecutivoordenesmedicas_"+w).toString()+" del convenio "+mapaSolicitudesInvalidas.get("nombreconvenio_"+w).toString());
					//La solicitud de {0} No. {1} se encuentra en estado medico SOLICITADA (CREADA), debe estar interpretada o anulada. [bf-64]
					this.setErrorFactura("error.facturacion.medicamentosDespachada", atributosVector);
				}
			}
		
		}
		
	}
	
	/**
	 * metodo que obtiene el listado de solicitudes de interconsulta - procedimientos - evoluciones que tienen asociado
	 * un servicio que requiere interpretacion y la solicitud tiene estado respondida
	 * @param con
	 * @param cuentas
	 * @param subCuentas
	 * @param tipoFacturacion 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap obtenerSolicitudesHCInvalidas( Connection con, Vector cuentas, Vector subCuentas, boolean validarInterpretadas)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().obtenerSolicitudesHCInvalidas(con, cuentas, subCuentas, validarInterpretadas);
	}	

	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param subCuentas
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap obtenerSolicitudesHCInvalidasMedicamentos( Connection con, Vector cuentas, Vector subCuentas)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().obtenerSolicitudesHCInvalidasMedicamentos(con, cuentas, subCuentas);
	}

	/**
	 * Metodo que obtiene las solicitudes de cx que no estan liquidadas
	 * @param con
	 * @param cuentas
	 * @param subCuentas
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap obtenerSolicitudesCxSinLiquidacion( Connection con, Vector cuentas, Vector subCuentas)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().obtenerSolicitudesCxSinLiquidacion(con, cuentas, subCuentas);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param subCuentas
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean existenSolicitudesNoFacturadas( Connection con, Vector cuentas, Vector subCuentas)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().existenSolicitudesNoFacturadas(con, cuentas, subCuentas);
	}
	
	/**
	 * Validar cierre de notas enfermeria para generar la factura
	 * @param Connection con 
	 * @param Vector cuentas
	 * */
	public boolean validarCierreNotasEnfermeriaGenerarFactura(Connection con, PersonaBasica paciente)
	{	
		//Validar si se hace requerido que las notas de enfermeria esten cerradas				
		//primero evaluamos que la via de ingreso tenga Cierre de Notas de Enfermeria en S
		/**
		 * Validacion cambiada por tarea id=64697 (xplanner3)
		 */
		if(ViasIngreso.existeVerificacionCierreNotasEnferViaIngreso(con, paciente.getCodigoUltimaViaIngreso()))
		{
			if(!UtilidadValidacion.esCerradaNotasEnfermeria(con,paciente.getCodigoCuenta()) && 
					(paciente.getCodigoUltimaViaIngreso()!=ConstantesBD.codigoViaIngresoUrgencias 
							|| (paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias && 
								UtilidadValidacion.estaEnCamaObservacion(con, paciente.getCodigoCuenta()
							   ))
					)
				)
			{
				this.setErrorFactura("error.facturacion.cierreNotasEnfer", "");
				return false;
			}
		}
					
		
		return true;
	}
	
	
	/**
	 * Validar la finalizacion de la epicrisis  para generar la factura
	 * @param Connection con 
	 * @param Vector cuentas
	 * */
	@SuppressWarnings("rawtypes")
	public boolean validarCierreEpicrisisFinaGenerarFactura(Connection con,  Vector cuentas, int ingreso) throws IPSException
	{	
		boolean indicador = false;
		
		//Validar si se hace requerido que las notas de enfermeria esten cerradas				
		//primero evaluamos que la via de ingreso tenga Cierre de Notas de Enfermeria en S
		for(int w=0; w<cuentas.size() && !indicador;w++)
		{
			logger.info("Existe verificaciï¿½n Cierre de Epicrisis via ingreso? cuenta->"+cuentas.get(w).toString());
			if(ViasIngreso.existeVerificacionEpicrisisFinalizadaViaIngreso(con, Cuenta.obtenerCodigoViaIngresoCuenta(con, cuentas.get(w).toString())))
			{
				indicador = true;				
			}
		}
		
		if(indicador)
		{
			if(!Epicrisis1.existeFinalizacionEpicrisis(con, ingreso))
			{			
				this.setErrorFactura("error.facturacion.finaliEpicrisis", "");
				return false;					
			}
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param subCuentas
	 * @param facturado
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Vector obtenerContratosSubCuentas(Connection con, String idIngreso, Vector subCuentas, String facturado)
	{
		return validacionesFacturaDao.obtenerContratosSubCuentas(con, idIngreso, subCuentas, facturado);
	}
	
	/**
	 * metodo que evalua la vigencia de los contratos
	 * @param con
	 * @param idIngreso
	 * @param subCuentas
	 * @param cuentas
	 */
	@SuppressWarnings("unchecked")
	public boolean esContratosVigentesYTopeValido(Connection con, String idIngreso, Vector subCuentas, Vector cuentas, UsuarioBasico usuario, Vector subCuentasSinInfoAdicVenezuela)
	{
		String validarContratosVencidos=ValoresPorDefecto.getValidarContratosVencidos(usuario.getCodigoInstitucionInt());
		boolean validarContratosBool=true;
		
		if(UtilidadTexto.isEmpty(validarContratosVencidos))
		{
			ElementoApResource elem=new ElementoApResource("error.facturacion.parametroContratosVencidos");
			this.erroresFactura.add(elem);
			return false;
		}
		else
			validarContratosBool=UtilidadTexto.getBoolean(validarContratosVencidos);
		
		Vector contratos= obtenerContratosSubCuentas(con, idIngreso, subCuentas, "" /*facturado*/);
		
		for(int w=0; w<cuentas.size(); w++)
		{
			HashMap contratosVencidosMap= Contrato.obtenerContratosVencidosXCuenta(con, cuentas.get(w).toString(), contratos);
			
			for(int w1=0; w1<Integer.parseInt(contratosVencidosMap.get("numRegistros").toString()); w1++)
			{
				Vector atributosVector= new Vector();
				atributosVector.add(contratosVencidosMap.get("nombreconvenio_"+w1).toString()+" de la cuenta Nro "+cuentas.get(w));
				atributosVector.add(contratosVencidosMap.get("fechainicial_"+w1).toString());
				atributosVector.add(contratosVencidosMap.get("fechafinal_"+w1).toString());
				//EL CONTRATO DEL CONVENIO {0} A FACTURAR SE ENCUENTRA VENCIDO. Fecha Inicial: {1} Fecha Final: {2}. [bf-19]
				if(validarContratosBool)
					this.setErrorFactura("error.facturacion.convenioVencidoConFechas", atributosVector);
				else
					this.setWarningsFactura("error.facturacion.convenioVencidoConFechas", atributosVector);
			}		
		}
		
		if(subCuentasSinInfoAdicVenezuela.size()>0)
			contratos= obtenerContratosSubCuentas(con, idIngreso, subCuentasSinInfoAdicVenezuela, "" /*facturado*/);
		
		if(contratos.size()>0)
		{	
			HashMap contratosTopeCompleto= Contrato.obtenerContratosTopesCompletos(con, contratos, "");
			
			for(int w=0; w<Integer.parseInt(contratosTopeCompleto.get("numRegistros").toString()); w++)
			{
				Vector atributosVector= new Vector();
				atributosVector.add(contratosTopeCompleto.get("nombreconvenio_"+w).toString());
				atributosVector.add(UtilidadTexto.formatearValores(contratosTopeCompleto.get("valor_"+w).toString()));
				atributosVector.add(UtilidadTexto.formatearValores(contratosTopeCompleto.get("acumulado_"+w).toString()));
				
				if(validarContratosBool)
					this.setErrorFactura("error.facturacion.convenioValorCompletoConValores", atributosVector);
				else
					this.setWarningsFactura("error.facturacion.convenioValorCompletoConValores", atributosVector);
			}
		}	
		return true;
	}
	
	/**
	 * metodo apra obtener las solicitudes q no tienen pool
	 * @param con
	 * @param cuentas
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap obtenerSolicitudesSinPool(Connection con, Vector cuentas, Vector subCuentas)
	{
		return validacionesFacturaDao.obtenerSolicitudesSinPool(con, cuentas, subCuentas);
	}
	
	/**
	 * Mï¿½todo que busca las solicitudes cuyos mï¿½dicos no esten en ningï¿½n pool 
	 * @param con
	 * @param cuentas
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap obtenerSolicitudesSinMedicoEnPool (Connection con, Vector cuentas, Vector subCuentas)
	{
		return validacionesFacturaDao.obtenerSolicitudesSinMedicoEnPool(con, cuentas, subCuentas);
	}
	
	/**
	 * Mï¿½todo que busca las solicitudes de cirugï¿½a las cuales no tienen pool asignado
     * @param con Conexiï¿½n con la fuente de datos
	 * @param cuentas 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap obtenerSolicitudesCxSinPool(Connection con, Vector cuentas, Vector subCuentas)
	{
		return validacionesFacturaDao.obtenerSolicitudesCxSinPool(con, cuentas, subCuentas);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 */
	@SuppressWarnings("unchecked")
	public void esPoolValidoXSolicitud (Connection con, Vector cuentas, Vector subCuentas, int institucion) 
	{
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValoresDefectoValidarPoolesFact(institucion)))
		{	
			HashMap mapa=obtenerSolicitudesSinPool(con, cuentas, subCuentas);
			
			for(int w=0; w<Integer.parseInt(mapa.get("numRegistros").toString()); w++)
			{
				Vector atributosVector= new Vector();
				atributosVector.add(mapa.get("numerosolicitud_"+w).toString());
				atributosVector.add(mapa.get("nombretiposolicitud_"+w).toString());
				atributosVector.add(mapa.get("nombremedico_"+w).toString());
				this.setErrorFactura("error.facturacion.solicitudSinPool", atributosVector);
			}
			
			mapa=obtenerSolicitudesSinMedicoEnPool(con, cuentas, subCuentas);
			for(int w=0; w<Integer.parseInt(mapa.get("numRegistros").toString()); w++)
			{
				Vector atributosVector= new Vector();
				atributosVector.add(mapa.get("nombremedico_"+w).toString());
				atributosVector.add(mapa.get("numerosolicitud_"+w).toString());
				this.setErrorFactura("error.facturacion.solicitudMedicoNoPool", atributosVector);
			}
	
			mapa=obtenerSolicitudesCxSinPool(con, cuentas, subCuentas);
			for(int w=0; w<Integer.parseInt(mapa.get("numRegistros").toString()); w++)
			{
				Vector atributosVector= new Vector();
				atributosVector.add(mapa.get("orden_"+w).toString());
				atributosVector.add(mapa.get("nombreservicio_"+w).toString());
				atributosVector.add(mapa.get("asocio_"+w).toString());
				this.setErrorFactura("error.facturacion.solicitudCirugiaNoPool", atributosVector);
			}
		}	
	}
	
	/**
	 * el metodo devuelve el mapa subCuentas pero cuando el valor es cero entonces lo coloca como seleccionado_w como N para 
	 * NO tener en cuenta una factura con valor cero (0), agrega tambien key valorcargototal_w
	 * @param con
	 * @param cuentas
	 * @param subCuentas
	 */
	@SuppressWarnings("unchecked")
	public HashMap esValorCargoTotalMayorCero(Connection con, Vector cuentas, HashMap subCuentas)
	{
		ArrayList<ElementoApResource> erroresOWarningsTemp= new ArrayList<ElementoApResource>();
		boolean todosSubcuentasTotalCero=true;
		for(int w=0; w<Integer.parseInt(subCuentas.get("numRegistros").toString()); w++)
		{	
			if(subCuentas.get("seleccionado_"+w).toString().equals(ConstantesBD.acronimoSi))
			{	
				double total= Factura.obtenerValorCargoTotalAFacturarXSubCuenta(con, cuentas, Double.parseDouble(subCuentas.get("subcuenta_"+w).toString()));
				if(total<1)
				{
					ElementoApResource elem=new ElementoApResource("error.facturacion.totalCuentaCero");
					elem.agregarAtributo(subCuentas.get("nombreresponsable_"+w).toString());
					erroresOWarningsTemp.add(elem);
					//LO COLOCAMOS COMO NO SELECCIONADO PARA QUE NO GENERE FACTURA YA QUE ES CERO
					subCuentas.put("seleccionado_"+w, ConstantesBD.acronimoNo);
				}
				else
				{
					todosSubcuentasTotalCero=false;
				}
				subCuentas.put("valorcargototal_"+w, total+"");
			}
			else
			{
				subCuentas.put("valorcargototal_"+w, "0");
			}
		}
		
		logger.info("\n\nSON TODOS LAS SUBCUENTAS TOTAL CERO-->"+todosSubcuentasTotalCero+"\n\n");
		
		if(todosSubcuentasTotalCero)
		{
			//entonces agregamos el error
			for(int w=0; w<erroresOWarningsTemp.size(); w++)
				this.erroresFactura.add(erroresOWarningsTemp.get(w));
		}
		else
		{
			//entonces agregamos el warning
			for(int w=0; w<erroresOWarningsTemp.size(); w++)
				this.warningsFactura.add(erroresOWarningsTemp.get(w));
		}
		return subCuentas;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param subCuentas
	 * @param facturado
	 * @return
	 */
	public static InfoDatosInt obtenerNaturalezaPacienteSubCuenta(Connection con, String idIngreso, double subCuenta, String facturado)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().obtenerNaturalezaPacienteSubCuenta(con, idIngreso, subCuenta, facturado);
	}
	
	/**
	 * metodo quee verifica la naturaleza del paciente x subcuenta  y evalua si tiene excepcion, en el caso de que sea Si indica
	 * que no se le debe cobrar ningun valor al paciente y que todo el valor de los cargos le corresponde al subCuenta responsable
	 * @param con
	 * @param subCuentas
	 * @param paciente
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap obtenerNaturalezaPaciente(Connection con, HashMap subCuentas, PersonaBasica paciente)
	{
		for(int w=0; w<Integer.parseInt(subCuentas.get("numRegistros").toString()); w++)
		{
			//solo para responsables <> particular
			if(subCuentas.get("seleccionado_"+w).toString().equals(ConstantesBD.acronimoSi)
				&& subCuentas.get("esparticular_"+w).toString().equals(ConstantesBD.acronimoNo))
			{	
				logger.info("\n\nFFFFFFFFFFFFFFFFFFFFFFFF obtenerNaturalezaPaciente-->");
				int naturalezaSubCuenta=obtenerNaturalezaPacienteSubCuenta(con, paciente.getCodigoIngreso()+"", Double.parseDouble(subCuentas.get("subcuenta_"+w).toString()), ""/*facturado*/).getCodigo();
				logger.info("nat->"+naturalezaSubCuenta);
				if(naturalezaSubCuenta>0)
				{
					//como existe entonces se verifica
					ExcepcionesNaturalezaPaciente excepciones=new ExcepcionesNaturalezaPaciente();
					ResultadoBoolean resExcepcionesNat=excepciones.buscarExcepcionPorNaturaleza(con, subCuentas.get("tiporegimen_"+w).toString(), naturalezaSubCuenta);
					logger.info("resExcepcionesNat "+resExcepcionesNat.isTrue()+" desc->"+resExcepcionesNat.getDescripcion());
					if(resExcepcionesNat.getDescripcion()!=null && !resExcepcionesNat.getDescripcion().equals(""))
					{
						this.setErrorFactura("error.facturacion.faltaParametrizarExcepcionNaturalezaConvenio", subCuentas.get("nombreresponsable_"+w).toString());
					}
					else
					{
						if(resExcepcionesNat.isTrue())
						{
							//ACTUALIZAMOS EL INDICATIVO pacientetieneexcepcionnaturaleza
							subCuentas.put("pactieneexcepcionnaturaleza_"+w, ConstantesBD.acronimoSi);
						}
					}
				}
			}
		}
		return subCuentas;
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuentas
	 */
	@SuppressWarnings("unchecked")
	public HashMap obtenerValorPacienteYConvenioSinAplicarTopes(Connection con, HashMap subCuentas, String idIngreso)
	{
		try {
			for(int w=0; w<Integer.parseInt(subCuentas.get("numRegistros").toString()); w++)
			{	
				if(subCuentas.get("seleccionado_"+w).toString().equals(ConstantesBD.acronimoSi))
				{	
					double valorBrutoPac=0;

					//primero verificamos la cuota de verificacion, si existe la asignamos de una
					DtoVerficacionDerechos cuotaVerificacion= this.obtenerValorCuotaVerificacion(con, idIngreso, Integer.parseInt(subCuentas.get("codigoconvenio_"+w).toString()));
					DtoInfoCobroPaciente calculo=new DtoInfoCobroPaciente();
					if(cuotaVerificacion.getValor()>=0||cuotaVerificacion.getPorcetaje()>=0)
					{
						if(cuotaVerificacion.getValor()>=0)
						{
							logger.info("VA HA ASIGNAR VALOR POR CUOTA DE VERIFICACION");
							double valorCuota=cuotaVerificacion.getValor();
							double valorCargoTotal=Double.parseDouble(subCuentas.get("valorcargototal_"+w).toString());
							if(cuotaVerificacion.getValor()>valorCargoTotal)
								valorCuota=valorCargoTotal;

							subCuentas.put("valorbrutopaciente_"+w, valorCuota+"");
							subCuentas.put("valorconvenio_"+w, (valorCargoTotal-valorCuota)+"");
							logger.info("valor convenio->"+subCuentas.get("valorconvenio_"+w));
							logger.info("valor PAC->"+subCuentas.get("valorbrutopaciente_"+w));
							this.setWarningsFactura("error.facturacion.valorVerificacionDerechos", subCuentas.get("nombreresponsable_"+w).toString());
						}
						else
						{
							logger.info("VA HA ASIGNAR VALOR POR CUOTA DE VERIFICACION");
							double valorCargoTotal=Double.parseDouble(subCuentas.get("valorcargototal_"+w).toString());
							double valorCuota=valorCargoTotal*cuotaVerificacion.getPorcetaje()/100;
							if(cuotaVerificacion.getValor()>valorCargoTotal)
								valorCuota=valorCargoTotal;

							subCuentas.put("valorbrutopaciente_"+w, valorCuota+"");
							subCuentas.put("valorconvenio_"+w, (valorCargoTotal-valorCuota)+"");
							logger.info("valor convenio->"+subCuentas.get("valorconvenio_"+w));
							logger.info("valor PAC->"+subCuentas.get("valorbrutopaciente_"+w));
							this.setWarningsFactura("error.facturacion.valorVerificacionDerechos", subCuentas.get("nombreresponsable_"+w).toString());
						}
					}
					else
					{	
						if(subCuentas.get("esparticular_"+w).toString().equals(ConstantesBD.acronimoSi))
						{
							// Si el tipo de rï¿½gimen es particular, todo el valor de la factura le corresponde al paciente, 
							// el valor del convenio queda en 0 (No hay convenio)
							subCuentas.put("valorbrutopaciente_"+w, subCuentas.get("valorcargototal_"+w));
							subCuentas.put("valorconvenio_"+w, "0");
							logger.info("valor bruto pac ->"+subCuentas.get("valorbrutopaciente_"+w));
							logger.info("valor convenio->"+subCuentas.get("valorconvenio_"+w));
						}
						//si no es particular entonces se verifica la excepcion
						else
						{
							//si tiene excepcion entonces el valor paciente es cero y se le cobra la totalidad al convenio
							if(subCuentas.get("pactieneexcepcionnaturaleza_"+w).toString().equals(ConstantesBD.acronimoSi))
							{
								subCuentas.put("valorbrutopaciente_"+w, "0");
								subCuentas.put("valorconvenio_"+w, subCuentas.get("valorcargototal_"+w));
								logger.info("valor bruto pac ->"+subCuentas.get("valorbrutopaciente_"+w));
								logger.info("valor convenio->"+subCuentas.get("valorconvenio_"+w));
							}
							//de lo contrario se busca x monto
							else
							{
								ICalculoValorCobrarPaciente calculoServicio=FacturacionServicioFabrica.crearCalculoValorCobrarPaciente();
								calculo=calculoServicio.valorCobrarAPaciente(Utilidades.convertirAEntero(subCuentas.get("subcuenta_"+w)+""));
								//Calculo del valor del paciente y de la subcuenta
								//Actualmente el porcentaje y el valor definidos en el monto de cobro son excluyentes, dependiendo de cual estï¿½ definido
								//en el sistema se calcula el valor bruto del paciente y el valor total del subcuenta
								if(calculo.isGenerado())
								{
									valorBrutoPac=calculo.getValorCargoPaciente().doubleValue();
									logger.info("valor bruto pac sin aprox->"+valorBrutoPac);
									subCuentas.put("valorbrutopaciente_"+w, UtilidadTexto.formatearValores(valorBrutoPac+"","#"));
									logger.info("valor bruto pac aprox->"+subCuentas.get("valorbrutopaciente_"+w));
									subCuentas.put("valorconvenio_"+w, Double.parseDouble(subCuentas.get("valorcargototal_"+w).toString()) - Double.parseDouble(subCuentas.get("valorbrutopaciente_"+w).toString()));
									logger.info("valor convenio->"+subCuentas.get("valorconvenio_"+w));
								}
								else
								{
									subCuentas.put("valorbrutopaciente_"+w, "0");
									logger.info("valor bruto pac->"+subCuentas.get("valorbrutopaciente_"+w));
									subCuentas.put("valorconvenio_"+w, subCuentas.get("valorcargototal_"+w).toString());
									logger.info("valor convenio->"+subCuentas.get("valorconvenio_"+w));
								}

							}
						}

					}
					subCuentas.put("calculoValorPaciente_"+w, calculo);
				}	
			}
			UtilidadTransaccion.getTransaccion().commit();
			logger.info("\n\n MAPA SUBCUENTAS->"+subCuentas);
		} catch (Exception e) {
			Log4JManager.error("obtenerValorPacienteYConvenioSinAplicarTopes : " + e);
			UtilidadTransaccion.getTransaccion().rollback();
		}
		return subCuentas;
	}
	
	/**
	 * 
	 * @param usuario
	 * @return
	 */
	public boolean esParametroGeneralTopesValido(UsuarioBasico usuario)
	{
		String manejoTopesXDiag= ValoresPorDefecto.getManejoTopesPaciente(usuario.getCodigoInstitucionInt());
		if(UtilidadTexto.isEmpty(manejoTopesXDiag))
		{	
			this.setErrorFactura("error.parametrosGenerales.faltaDefinirParametro", "Tipo de Manejo Topes del Paciente");
			return false;
		}
		return true;
	}
	
	/**
	 *  
	 * @param con
	 * @param institucion
	 * @param codigoIngreso
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean facturarReingresosIndependientes(Connection con, int institucion, int codigoIngreso) {
		int reingreso = UtilidadesManejoPaciente.obtenerReingreso(con, codigoIngreso);
		
		if (reingreso != ConstantesBD.codigoNuncaValido){
			if(ValoresPorDefecto.getPermitirFacturarReingresosIndependientes(institucion).toString().equals(ConstantesBD.acronimoNo)){
				Vector vector = new Vector();
				vector.add(IngresoGeneral.getConsecutivoIngreso(con, codigoIngreso+""));
				vector.add(IngresoGeneral.getConsecutivoIngreso(con,reingreso+""));
				//NO SE PERMITE FACTURAR REINGRESOS INDEPENDIENTES (Ingreso Actual {0} / Ingreso Asociado {1}) [bf-88]
				this.setErrorFactura("error.facturacion.reingresosIndependientes", vector);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param facturasPropuestas
	 * @param usuario
	 * @param paciente
	 * @param tipoFacturacion
	 * @return
	 */
	public ArrayList<DtoFactura> recalcularValorPacienteXPYP(Connection con, ArrayList<DtoFactura> facturasPropuestas, UsuarioBasico usuario, PersonaBasica paciente, int tipoFacturacion)
	{
		logger.info("\n\n**************************************ENTRAMOS A RECALCULAR pyp *********************************************");
		for(int w=0; w<facturasPropuestas.size(); w++)
		{	
			logger.info("factura propuesta ->"+w);
			if(	!facturasPropuestas.get(w).getEsParticular() && !facturasPropuestas.get(w).getValidarInfoVenezuela())
			{
				if(existenUnicamenteSolicitudesPYP(con, facturasPropuestas.get(w).getCuentas(), facturasPropuestas.get(w).getSubCuenta()))
				{
					logger.info("TODAS LAS SOLICITUDES SON DE PYP, ESO QUIERE DECIR QUE EL PACIENTE PAGA 0 PESOS!!!!!!!!!!!!!!!!!!");
					DtoFactura dtoFacturaMod= facturasPropuestas.get(w);
					dtoFacturaMod.setValorBrutoPac(0);
					dtoFacturaMod.setValorConvenio(dtoFacturaMod.getValorTotal());
					facturasPropuestas.set(w, dtoFacturaMod);
				}	
			}
		}
		return facturasPropuestas;
	}
	
	/**
	 * metodo que evalua si todas las solicitudes son de pyp para no cobrarle nada al paciente
	 * @param con
	 * @param cuentas
	 * @param subCuenta
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean existenUnicamenteSolicitudesPYP(Connection con, Vector cuentas, double subCuenta) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().existenUnicamenteSolicitudesPYP(con, cuentas, subCuenta);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param facturasPropuestas
	 * @param usuario
	 * @param paciente
	 * @param tipoFacturacion
	 * @return
	 */
	public ArrayList<DtoFactura> recalcularValorPacienteXTopes(Connection con, ArrayList<DtoFactura> facturasPropuestas, UsuarioBasico usuario, PersonaBasica paciente, int tipoFacturacion)
	{
		logger.info("\n\n**************************************ENTRAMOS A RECALCULAR TOPES *********************************************");
		for(int w=0; w<facturasPropuestas.size(); w++)
		{	
			logger.info("factura propuesta ->"+w);
			if(	!facturasPropuestas.get(w).getEsParticular()
				&& !facturasPropuestas.get(w).getPacienteTieneExcepcionNaturaleza()
				&& !facturasPropuestas.get(w).getValidarInfoVenezuela())
			{
				facturasPropuestas.set(w, aplicarTopesXEventoYCalendario(con,facturasPropuestas.get(w), paciente, tipoFacturacion, usuario));
			}
		}
		return facturasPropuestas;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param facturasPropuestas
	 * @param usuario
	 * @param paciente
	 * @param tipoFacturacion
	 * @return
	 */
	public ArrayList<DtoFactura> recalcularValorPacienteXParametroAsigValPacienteValAbonos(Connection con, ArrayList<DtoFactura> facturasPropuestas) throws IPSException
	{
		for(int w=0; w<facturasPropuestas.size(); w++)
		{	
			//lo seteamos aca para reutilizarlo en abonos sin que lo modifiquen,
			DtoFactura dtoFactura= facturasPropuestas.get(w);
			dtoFactura.setValorBrutoPacSinModParamConvenio(dtoFactura.getValorBrutoPac());
			
			if(	!facturasPropuestas.get(w).getValidarInfoVenezuela())
			{
				//xplanner 61514 -- al entrar inmediatamente a esta pagina se debe validar que el campo 
				//"Valor paciente" sea igual al campo "abono aplicado" 
				//siempre y cuando el parametro del Convenio: Asignar Valor Paciente con el Valor de los Abonos = Si 
				//permitiendo modificar el campo valor paciente.
				
				//Evalua el indicador de Asignar en la factura como valor del paciente el valor de abono 
				Convenio convenio = new Convenio();
				convenio.cargarResumen(con,dtoFactura.getConvenio().getCodigo());
				
				if(UtilidadTexto.getBoolean(convenio.getAsignarFactValorPacValorAbono()))
				{
					if(!dtoFactura.getValorBrutoPacienteModificadoXAnioCalendario() && !dtoFactura.getValorBrutoPacienteModificadoXEvento())
					{
						dtoFactura.setValorConvenio(dtoFactura.getValorConvenio()+dtoFactura.getValorBrutoPac());
						dtoFactura.setValorBrutoPac(0); //el abono aplicado en un inicio va ha ser cero
						
					}	
				}
			}
			
			facturasPropuestas.set(w, dtoFactura);
			
		}
		return facturasPropuestas;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param dtoFactura
	 * @param paciente
	 * @param tipoFacturacion
	 * @return
	 */
	private DtoFactura aplicarTopesXEventoYCalendario(Connection con, DtoFactura dtoFactura, PersonaBasica paciente, int tipoFacturacion, UsuarioBasico usuario)
	{
		
		boolean manejoTopesPacienteXDiagnostico=UtilidadTexto.getBoolean(ValoresPorDefecto.getManejoTopesPaciente(usuario.getCodigoInstitucionInt()));
		//primero obtenemos el tope x evento
		TopesFacturacion topes= new TopesFacturacion();
		PagosPaciente pagosPaciente=new PagosPaciente();
		
		if(topes.cargarResumen(con, dtoFactura.getTopeFacturacion()))
		{	
			//evaluamos el codigo via ingreso para determinar q dx se usa
			if(dtoFactura.getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoUrgencias
				|| dtoFactura.getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoHospitalizacion)
			{
				double valorBrutoPacienteEvento=0;
				double valorBrutoPacienteAnio=0;
				boolean recalculadoXEvento=false;
				boolean recalculadoXAnio=false;
				
				if(!UtilidadTexto.isEmpty(dtoFactura.getDiagnosticoEgresoAcronimoTipoCie()))
				{
					if(topes.getTopeEvento()>0 && tipoFacturacion==ConstantesBD.codigoTipoFacturacionPacienteConAtencionFinalizada)
					{
						pagosPaciente.cargarAcumuladosXDx(con, paciente.getCodigoPersona(), dtoFactura.getDiagnosticoAcronimo(), dtoFactura.getDiagnosticoTipoCie(), dtoFactura.getTipoMonto().getCodigo(), dtoFactura.getTipoRegimenAcronimo());
						double valorAcumuladoMEM=0; 
						//si es por diagnostico el filtro del acumulado se hace por dx de lo contrario se toma el total
						if(manejoTopesPacienteXDiagnostico)
							valorAcumuladoMEM=obtenerValorAcumuladoXDiagMEM(dtoFactura.getDiagnosticoAcronimo(), dtoFactura.getDiagnosticoTipoCie()+"");
						else
							valorAcumuladoMEM=obtenerValorAcumuladoXTotalMEM();
						
						double valorAcumuladoPaciente=0;
						//para el parametro en N quiere decir que no se tiene en cuenta el acumulado en BD solo importa el actual
						if(manejoTopesPacienteXDiagnostico)
							valorAcumuladoPaciente=pagosPaciente.getPagosAcumuladosXDiagnostico()+ valorAcumuladoMEM;
						else
							valorAcumuladoPaciente=valorAcumuladoMEM;
						double valorAcumuladoTotal=dtoFactura.getValorBrutoPac()+valorAcumuladoPaciente;
						
						//en el caso de que el valor acumulado del paciente sea mayor igual al valor del tope x evento, se asigna al valor del paciente cero
						if(valorAcumuladoPaciente>=topes.getTopeEvento())
						{
							valorBrutoPacienteEvento=0;
							recalculadoXEvento=true;
						}
						//si el valor acumulado del paciente es menor que el valor del tope x evento:
						else
						{
							//si el valor del paciente que se esta facturando mas el valor acumulado sea mayor que el tope x evento,
							//se cobrara al paciente el valor correspondiente a la diferencia entre el tope x evento menos el valor acumulado
							if(valorAcumuladoTotal>topes.getTopeEvento())
							{
								valorBrutoPacienteEvento=topes.getTopeEvento()-valorAcumuladoPaciente;
								recalculadoXEvento=true;
							}
						}
					}
				}	
				if(topes.getTopeAnioCalendario()>0)
				{
					pagosPaciente.cargarAcumuladosTotalAnio(con, paciente.getCodigoPersona(), dtoFactura.getTipoMonto().getCodigo(), dtoFactura.getTipoRegimenAcronimo());
					double valorAcumuladoMEM= obtenerValorAcumuladoXTotalMEM();
					double valorAcumuladoPaciente=pagosPaciente.getPagosAcumuladosTotalAnio()+ valorAcumuladoMEM;
					double valorAcumuladoTotal=dtoFactura.getValorBrutoPac()+valorAcumuladoPaciente;
					
					//en el caso de que el valor acumulado del paciente sea mayor igual al valor del tope x calendario, se asigna al valor del paciente cero
					if(valorAcumuladoPaciente>=topes.getTopeAnioCalendario())
					{
						valorBrutoPacienteAnio=0;
						recalculadoXAnio=true;
					}
					//si el valor acumulado del paciente es menor que el valor del tope x evento:
					else
					{
						//si el valor del paciente que se esta facturando mas el valor acumulado sea mayor que el tope x evento,
						//se cobrara al paciente el valor correspondiente a la diferencia entre el tope x evento menos el valor acumulado
						if(valorAcumuladoTotal>topes.getTopeAnioCalendario())
						{
							valorBrutoPacienteAnio=topes.getTopeAnioCalendario()-valorAcumuladoPaciente;
							recalculadoXAnio=true;
						}
					}
				}
				
				if(recalculadoXEvento && recalculadoXAnio)
				{
					//obtenemos el menor valor bruto paciente para asignarlo al objeto de factura 
					if(valorBrutoPacienteAnio<=valorBrutoPacienteEvento)
					{	
						dtoFactura.setValorBrutoPac(valorBrutoPacienteAnio);
						dtoFactura.setValorBrutoPacienteModificadoXAnioCalendario(true);
					}	
					else
					{	
						dtoFactura.setValorBrutoPac(valorBrutoPacienteEvento);
						dtoFactura.setValorBrutoPacienteModificadoXEvento(true);
					}	
					dtoFactura.setValorConvenio(dtoFactura.getValorTotal()-dtoFactura.getValorBrutoPac());
					
				}
				
				else if(recalculadoXEvento)
				{
					dtoFactura.setValorBrutoPac(valorBrutoPacienteEvento);
					dtoFactura.setValorBrutoPacienteModificadoXEvento(true);
					dtoFactura.setValorConvenio(dtoFactura.getValorTotal()-dtoFactura.getValorBrutoPac());
				}
				else if(recalculadoXAnio)
				{
					dtoFactura.setValorBrutoPac(valorBrutoPacienteAnio);
					dtoFactura.setValorBrutoPacienteModificadoXAnioCalendario(true);
					dtoFactura.setValorConvenio(dtoFactura.getValorTotal()-dtoFactura.getValorBrutoPac());
				}
				
			
				//simulamos la insercion del pago en un Hashtable
				insertarPagoPacienteHashTable(dtoFactura.getDiagnosticoAcronimo(), dtoFactura.getDiagnosticoTipoCie(), dtoFactura.getValorBrutoPac());
			}
			else if(dtoFactura.getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoAmbulatorios
					|| dtoFactura.getViaIngreso().getCodigo()==ConstantesBD.codigoViaIngresoConsultaExterna)
			{
				//en este punto cargamos los diagnosticos del detalle de factura
				double valorARestarTotal=0;
				//en este punto debemos saber que procentaje del valor le toca al paciente y cuanto al convenio para poder sacar el valor x servicio 
				//regla de tres
				double porcentajePaciente=  dtoFactura.getValorBrutoPac() * 100 / dtoFactura.getValorTotal();
				
				for(int w=0; w<dtoFactura.getDetallesFactura().size(); w++)
				{
					double valorARestarValorBrutoPacEvento=0;
					double valorARestarValorBrutoPacAnio=0;
					boolean recalculadoXEvento=false;
					boolean recalculadoXAnio=false;
					
					if(!UtilidadTexto.isEmpty(dtoFactura.getDetallesFactura().get(w).getDiagnosticoAcronimoTipoCie()))
					{	
						if(topes.getTopeEvento()>0 && tipoFacturacion==ConstantesBD.codigoTipoFacturacionPacienteConAtencionFinalizada)
						{	
							pagosPaciente.cargarAcumuladosXDx(con, paciente.getCodigoPersona(), dtoFactura.getDetallesFactura().get(w).getDiagnosticoAcronimo(), dtoFactura.getDetallesFactura().get(w).getDiagnosticoTipoCie(), dtoFactura.getTipoMonto().getCodigo(), dtoFactura.getTipoRegimenAcronimo());
							double valorAcumuladoMEM=0;
							
							if(manejoTopesPacienteXDiagnostico)
								valorAcumuladoMEM=obtenerValorAcumuladoXDiagMEM(dtoFactura.getDetallesFactura().get(w).getDiagnosticoAcronimo(), dtoFactura.getDetallesFactura().get(w).getDiagnosticoTipoCie()+"");
							else
								valorAcumuladoMEM=obtenerValorAcumuladoXTotalMEM();
							
							double valorAcumuladoPaciente=0;
							//para el parametro en N quiere decir que no se tiene en cuenta el acumulado en BD solo importa el actual
							if(manejoTopesPacienteXDiagnostico)
								valorAcumuladoPaciente=pagosPaciente.getPagosAcumuladosXDiagnostico()+ valorAcumuladoMEM;
							else
								valorAcumuladoPaciente=valorAcumuladoMEM;
							double valorServicioSinTope=((dtoFactura.getDetallesFactura().get(w).getValorTotal()*porcentajePaciente)/100);
							double valorAcumuladoTotal=valorServicioSinTope+valorAcumuladoPaciente;
							
							//en el caso de que el valor acumulado del paciente sea mayor igual al valor del tope x evento, se asigna al valor del paciente cero
							if(valorAcumuladoPaciente>=topes.getTopeEvento())
							{
								valorARestarValorBrutoPacEvento+=valorServicioSinTope;
								recalculadoXEvento=true;
							}
							//si el valor acumulado del paciente es menor que el valor del tope x evento:
							else
							{
								//si el valor del paciente que se esta facturando mas el valor acumulado sea mayor que el tope x evento,
								//se cobrara al paciente el valor correspondiente a la diferencia entre el tope x evento menos el valor acumulado
								if(valorAcumuladoTotal>topes.getTopeEvento())
								{
									valorARestarValorBrutoPacEvento+=valorAcumuladoTotal-topes.getTopeEvento();
									recalculadoXEvento=true;
								}
							}
							
						}
					}	
					if(topes.getTopeAnioCalendario()>0)
					{	
						pagosPaciente.cargarAcumuladosTotalAnio(con, paciente.getCodigoPersona(),dtoFactura.getTipoMonto().getCodigo(), dtoFactura.getTipoRegimenAcronimo());
						double valorAcumuladoMEM= obtenerValorAcumuladoXTotalMEM();
						double valorAcumuladoPaciente=pagosPaciente.getPagosAcumuladosTotalAnio()+ valorAcumuladoMEM;
						double valorServicioSinTope=((dtoFactura.getDetallesFactura().get(w).getValorTotal()*porcentajePaciente)/100);
						double valorAcumuladoTotal=valorServicioSinTope+valorAcumuladoPaciente;
							
						//en el caso de que el valor acumulado del paciente sea mayor igual al valor del tope x , se asigna al valor del paciente cero
						if(valorAcumuladoPaciente>=topes.getTopeAnioCalendario())
						{
							valorARestarValorBrutoPacAnio+=valorServicioSinTope;
							recalculadoXAnio=true;
						}
						//si el valor acumulado del paciente es menor que el valor del tope x :
						else
						{
							//si el valor del paciente que se esta facturando mas el valor acumulado sea mayor que el tope x ,
							//se cobrara al paciente el valor correspondiente a la diferencia entre el tope x  menos el valor acumulado
							if(valorAcumuladoTotal>topes.getTopeAnioCalendario())
							{
								valorARestarValorBrutoPacAnio+=valorAcumuladoTotal-topes.getTopeAnioCalendario();
								recalculadoXAnio=true;
							}
						}
					}
				
					double valorARestarSeleccionado=0;
					
					if(recalculadoXEvento && recalculadoXAnio)
					{
						//obtenemos el valor a restar mas alto para asignarlo
						if(valorARestarValorBrutoPacAnio>=valorARestarValorBrutoPacEvento)
						{	
							valorARestarSeleccionado=valorARestarValorBrutoPacAnio;
							dtoFactura.setValorBrutoPacienteModificadoXAnioCalendario(true);
						}	
						else
						{	
							valorARestarSeleccionado=valorARestarValorBrutoPacEvento;
							dtoFactura.setValorBrutoPacienteModificadoXEvento(true);
						}	
						
						valorARestarTotal+=valorARestarSeleccionado;
					}
					else if(recalculadoXEvento)
					{
						valorARestarSeleccionado=valorARestarValorBrutoPacEvento;
						dtoFactura.setValorBrutoPacienteModificadoXEvento(true);
						valorARestarTotal+=valorARestarSeleccionado;
					}
					else if(recalculadoXAnio)
					{
						valorARestarSeleccionado=valorARestarValorBrutoPacAnio;
						dtoFactura.setValorBrutoPacienteModificadoXAnioCalendario(true);
						valorARestarTotal+=valorARestarSeleccionado;
					}
					
					//simulamos la insercion del pago en un Hashtable
					double valorServicioSinTope=((dtoFactura.getDetallesFactura().get(w).getValorTotal()*porcentajePaciente)/100);
					insertarPagoPacienteHashTable(dtoFactura.getDiagnosticoAcronimo(), dtoFactura.getDiagnosticoTipoCie(), valorServicioSinTope-valorARestarSeleccionado);
					
				}//for	
				
				//ya teniendo el valor a restar al valorBrutopaciente
				dtoFactura.setValorBrutoPac(dtoFactura.getValorBrutoPac()-valorARestarTotal);
				dtoFactura.setValorConvenio(dtoFactura.getValorTotal()-dtoFactura.getValorBrutoPac());
			}
		}
		else
		{
			logger.info("NO existen topes x evento!!!");
		}
		
		//para el caso en el cual el valor del paciente es mayor que el valor de los cargos se debe evaluar parametros generales
		if(dtoFactura.getValorBrutoPac()>dtoFactura.getValorTotal())
		{
			boolean asignarValorPacienteCargos=UtilidadTexto.getBoolean(ValoresPorDefecto.getAsignarValorPacienteValorCargos(usuario.getCodigoInstitucionInt()));
			if(asignarValorPacienteCargos)
			{	
				dtoFactura.setValorBrutoPac(dtoFactura.getValorTotal());
				dtoFactura.setValorConvenio(0);
			}
			else
			{
				dtoFactura.setValorAFavorConvenio(dtoFactura.getValorBrutoPac()-dtoFactura.getValorTotal());
				dtoFactura.setValorAFavorConvenioModificado(true);
				dtoFactura.setValorConvenio(0);
			}
		}	
		
		return dtoFactura;
	}
	

	/**
	 * 
	 * @param indiceArrayFactura
	 * @param diagnosticoEgresoAcronimoTipoCie
	 * @return
	 */
	private double obtenerValorAcumuladoXDiagMEM(String diagnosticoAcronimo, String diagnosticoTipoCie) 
	{
		double valor=0;
		for(int w=0; w<Integer.parseInt(this.pagosPacienteMEM.get("numRegistros").toString()); w++)
		{
			if(this.pagosPacienteMEM.get("acronimoDiagnostico_"+w).toString().equals(diagnosticoAcronimo)
				&& this.pagosPacienteMEM.get("tipoCie_"+w).toString().equals(diagnosticoTipoCie))
			{
				valor+=Double.parseDouble(this.pagosPacienteMEM.get("valorPago_"+w).toString());
			}
		}
		return valor;
	}

	/**
	 * 
	 * @param indiceArrayFactura
	 * @param diagnosticoEgresoAcronimoTipoCie
	 * @return
	 */
	private double obtenerValorAcumuladoXTotalMEM() 
	{
		double valor=0;
		for(int w=0; w<Integer.parseInt(this.pagosPacienteMEM.get("numRegistros").toString()); w++)
		{
				valor+=Double.parseDouble(this.pagosPacienteMEM.get("valorPago_"+w).toString());
		}
		return valor;
	}
	
	
	/**
	 * simulacion de 
	 * @param acronimoDiagnostico
	 * @param tipoCie
	 * @param valorPago
	 */
	@SuppressWarnings("unchecked")
	private void insertarPagoPacienteHashTable(	String acronimoDiagnostico, 
												int tipoCie, 
												double valorPago 
											  )
	{
		int numReg= Integer.parseInt(this.pagosPacienteMEM.get("numRegistros").toString());
		this.pagosPacienteMEM.put("acronimoDiagnostico_"+numReg, acronimoDiagnostico+"");
		this.pagosPacienteMEM.put("tipoCie_"+numReg, tipoCie+"");
		this.pagosPacienteMEM.put("valorPago_"+numReg, valorPago+"");
		numReg++;
		this.pagosPacienteMEM.put("numRegistros", numReg+"");
		
		logger.info("***********HASHTABLE PAGOS -->"+this.pagosPacienteMEM);
	}
	
	/**
	 * 
	 * @param con
	 * @param abonosYdescuentos
	 */
	@SuppressWarnings("unchecked")
	public void validacionesAbonosYDescuentosVlrPacienteAbonos (Connection con, HashMap abonosYdescuentos)
	{
		//el campo total paciente debe ser mayor igual a cero y menor igual al total a facturar
		for(int w=0; w<Integer.parseInt(abonosYdescuentos.get("numRegistros").toString());w++)
		{	
			logger.info("w->"+w+ "abonosYdescuentos.get(vlrPaciente_+w).toString()->"+abonosYdescuentos.get("vlrPaciente_"+w).toString());
			if(!UtilidadTexto.isNumber(abonosYdescuentos.get("vlrPaciente_"+w).toString()))
			{
				logger.info("valor paciente no number");
				Vector atributosVector= new Vector();
				atributosVector.add("El Valor del Paciente");
				atributosVector.add("0 y menor igual al total a facturar");
				//{0} debe ser mayor igual que {1}. [aa-47.3]
				this.setErrorFactura("errors.debeSerNumeroMayorIgual", atributosVector);
			}
			else
			{
				if(Double.parseDouble(abonosYdescuentos.get("vlrPaciente_"+w).toString())>Double.parseDouble(abonosYdescuentos.get("totalFacturar_"+w).toString()))
				{
					//se debe validar el valor del paciente cuando no existe un valor a favor del convenio, porque cuando existe favor convenio siempre va ha sobrepasar el total
					//a facturar
					if(abonosYdescuentos.get("vlrFavorConvenio_"+w).toString().equals("0"))
					{
						logger.info("valor paciente mayor al total a facturar");
						Vector atributosVector= new Vector();
						atributosVector.add("El Valor del Paciente");
						atributosVector.add("0 y menor igual al total a facturar");
						//{0} debe ser mayor igual que {1}. [aa-47.3]
						this.setErrorFactura("errors.debeSerNumeroMayorIgual", atributosVector);
					}	
				}
			}
		}
		logger.info("size errorees->"+this.getErroresFactura().size());
		//como todo esta bien podemos evaluar el dcto del paciente , abonos
		if(this.erroresFactura.size()<1)
		{	
			for(int w=0; w<Integer.parseInt(abonosYdescuentos.get("numRegistros").toString());w++)
			{
				if(UtilidadTexto.isNumber(abonosYdescuentos.get("dctoValor_"+w).toString()))
				{
					//validar como campo numerico entero positivo, mayor igual q cero y menor igual valor paciente
					double dctoValor= Double.parseDouble(abonosYdescuentos.get("dctoValor_"+w).toString());
					double valorPaciente= Double.parseDouble(abonosYdescuentos.get("vlrPaciente_"+w).toString());
					if(dctoValor<0 || dctoValor>valorPaciente)
					{
						Vector atributosVector= new Vector();
						atributosVector.add("El Valor del Descuento");
						atributosVector.add("0 y menor igual al valor paciente");
						//{0} debe ser mayor igual que {1}. [aa-47.3]
						this.setErrorFactura("errors.debeSerNumeroMayorIgual", atributosVector);
					}
				}
			}
			
			//numerico, entero, positivo, >=0 y <= valorAbonoDisponible
			double valorAbonosAcumulado=0;
			boolean existeErrorAbono=false;
			for(int w=0; w<Integer.parseInt(abonosYdescuentos.get("numRegistros").toString());w++)
			{
				if(UtilidadTexto.isNumber(abonosYdescuentos.get("abonoAplicado_"+w).toString()))
				{
					if(Double.parseDouble(abonosYdescuentos.get("abonoAplicado_"+w).toString())<0)
					{
						Vector atributosVector= new Vector();
						atributosVector.add("El Valor del Abono");
						atributosVector.add("0");
						//{0} debe ser mayor igual que {1}. [aa-47.3]
						this.setErrorFactura("errors.debeSerNumeroMayorIgual", atributosVector);
						existeErrorAbono=true;
					}
					valorAbonosAcumulado+=Double.parseDouble(abonosYdescuentos.get("abonoAplicado_"+w).toString());
				}
			}
			if(!existeErrorAbono)
			{
				HashMap encabezado=	((HashMap) abonosYdescuentos.get("encabezado"));
				double valorAbonoDisponible= Double.parseDouble(encabezado.get("abonoDisponible").toString());
				if(valorAbonosAcumulado>valorAbonoDisponible)
				{
					//error.facturacion.valorExcedidoAbonosAplicados
					Vector atributosVector= new Vector();
					atributosVector.add(UtilidadTexto.formatearValores(valorAbonosAcumulado));
					atributosVector.add(UtilidadTexto.formatearValores(valorAbonoDisponible));
					this.setErrorFactura("error.facturacion.valorExcedidoAbonosAplicados", atributosVector);
					//SE EXCEDE EL VALOR DE LOS ABONOS APLICADOS {0} CON EL ABONO DISPONIBLE {1}. Por favor verifique.
				}
			}
		}
	}	
	
	/**
	 * 
	 * @param con
	 * @param abonosYdescuentos
	 */
	@SuppressWarnings("unchecked")
	public void validacionesAbonosYDescuentosValorContrato (Connection con, HashMap abonosYdescuentos, PersonaBasica paciente, UsuarioBasico usuario) throws IPSException
	{
		//en este punto todos los valores del paciente deben ser mayor q cero
		boolean validarContratosVencidos=UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarContratosVencidos(usuario.getCodigoInstitucionInt()));
		for(int w=0; w<Integer.parseInt(abonosYdescuentos.get("numRegistros").toString());w++)
		{
			//se debe tomar los valores VALOR CONTRATO Y VALOR ACUMULADO del contrato y la diferencia entre estos dos debe ser mayor igual 
			//que el valor a cargo del convenio
			Vector subCuenta= new Vector();
			subCuenta.add(abonosYdescuentos.get("idSubcuenta_"+w).toString());
			Vector contrato=obtenerContratosSubCuentas(con, paciente.getCodigoIngreso()+"", subCuenta, "" /*facturado*/);
			if(subCuenta.size()==1)
			{
				Contrato objectContrato= new Contrato();
				objectContrato.cargar(con, contrato.get(0).toString());
				
				double diferencia= objectContrato.getValorContrato()-objectContrato.getValorAcumulado();
				double vlrConvenio= Double.parseDouble(abonosYdescuentos.get("vlrConvenio_"+w).toString());
				
				if(objectContrato.getValorContrato()>0)
				{	
					if(vlrConvenio>0 && vlrConvenio>diferencia)
					{
						Vector atributosVector= new Vector();
						atributosVector.add(abonosYdescuentos.get("nomConvenio_"+w).toString());
						atributosVector.add(UtilidadTexto.formatearValores(vlrConvenio));
						atributosVector.add(UtilidadTexto.formatearValores(objectContrato.getValorContrato()));
						atributosVector.add(UtilidadTexto.formatearValores(objectContrato.getValorAcumulado()));
						//SE EXCEDE EL VALOR DEL CONTRATO PARA EL CONVENIO {0} A FACTURAR. Valor Convenio {1}, Total Contrato {2}, Total Acumulado Anterior {3}.
						
						if(validarContratosVencidos)
							this.setErrorFactura("error.facturacion.valorExcedidoContrato", atributosVector);
						else
							this.setWarningsFactura("error.facturacion.valorExcedidoContrato", atributosVector);
					}
				}	
			}
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param existenConceptos
	 */
	@SuppressWarnings("unchecked")
	public void validacionesReciboCajaAutoMatico(Connection con, UsuarioBasico usuario, boolean existenConceptos)
	{
		Arqueos mundoArqueos= new Arqueos();
		if(UtilidadValidacion.funcionalidadADibujarNoEntradaDependencias(con, usuario.getLoginUsuario(), ConstantesBD.codigoFuncionalidadRecibosCaja).equals(""))
		{
			ElementoApResource elem=new ElementoApResource("error.recibosCaja.usuarioNoAutorizado");
			this.warningsFactura.add(elem);
		}
		if(!existenConceptos)
		{
			ElementoApResource elem=new ElementoApResource("error.recibosCaja.noDefinidoConcepto");
			this.warningsFactura.add(elem);
		}
		if(usuario.getCodigoCaja()==ConstantesBD.codigoNuncaValido)
		{
			if(Utilidades.numCajasAsociadasUsuarioXcentroAtencion(usuario.getLoginUsuario(), usuario.getCodigoCentroAtencion()+"")<=0)
			{	
				ElementoApResource elem=new ElementoApResource("error.recibosCaja.usuarioNoASociadoCaja");
				this.warningsFactura.add(elem);
			}	
		}	
		//se evalua que no exista previamente el cierre caja
		else if(mundoArqueos.existeCierreCaja(con, usuario.getCodigoInstitucionInt(), UtilidadFecha.getFechaActual(), usuario.getLoginUsuario(), usuario.getConsecutivoCaja()+""))
		{
			Vector atributosError= new Vector();
			atributosError.add(usuario.getLoginUsuario());
			atributosError.add(usuario.getConsecutivoCaja());
			atributosError.add(UtilidadFecha.getFechaActual());
			this.setWarningsFactura("error.arqueos.yaExisteCierreCaja", atributosError);
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public DtoVerficacionDerechos obtenerValorCuotaVerificacion(Connection con, String idIngreso, int codigoConvenio)
	{
		return validacionesFacturaDao.obtenerValorCuotaVerificacion(con, idIngreso, codigoConvenio);
	}
	
	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param cuentas
	 */
	@SuppressWarnings("unchecked")
	public void validacionCentroAtencion(Connection con, UsuarioBasico usuario, Vector cuentas)
	{
		int codigoCentroAtencion=Cuenta.obtenerCentroAtencionCuenta(con, cuentas.get(0).toString());
		if(codigoCentroAtencion!=usuario.getCodigoCentroAtencion())
		{
			this.setErrorFactura("error.facturacion.centroAtencionInvelido", Utilidades.obtenerNombreCentroAtencion(con, codigoCentroAtencion));
		}
	}
	
	/**
	 * Mï¿½todo que valida los estados de los accidentes de transito
	 * @param con
	 * @param pac
	 * @return
	 */
	public void validacionAccidentesTransito(Connection con, PersonaBasica paciente, int tipoFacturacion)
	{
		if(tipoFacturacion==ConstantesBD.codigoTipoFacturacionPacienteConAtencionFinalizada)
		{	
			if(UtilidadValidacion.esAccidenteTransito(con, paciente.getCodigoIngreso()))
			{	
				if(!UtilidadValidacion.esAccidenteTransitoEstadoDado(con, paciente.getCodigoIngreso(),  ConstantesIntegridadDominio.acronimoEstadoProcesado))
					this.setErrorFactura("error.facturacion.accidenteTransito", "");
			}
		}	
	}
	
	/**
	 * Mï¿½todo que valida los estados de los eventos catastroficos
	 * @param con
	 * @param pac
	 * @return
	 */
	public void validacionEventosCatastroficos(Connection con, PersonaBasica paciente, int tipoFacturacion)
	{
		if(tipoFacturacion==ConstantesBD.codigoTipoFacturacionPacienteConAtencionFinalizada)
		{
			if(UtilidadValidacion.esEventoCatastrofico(con, paciente.getCodigoIngreso()))
			{	
				if(!UtilidadValidacion.esEventoCatastroficoEstadoDado(con, paciente.getCodigoIngreso(),  ConstantesIntegridadDominio.acronimoEstadoFinalizado))
					this.setErrorFactura("error.facturacion.eventoCatastrofico", "");
			}
		}	
	}
	
	/**
	 * Mï¿½todo que valida que la cuenta del paciente no se encuentre en proceso de asocio
	 * @param con
	 * @param paciente
	 */
	public void validacionProcesoAsocio(Connection con, PersonaBasica paciente) 
	{
		if(UtilidadValidacion.puedoCrearCuentaAsocio(con, paciente.getCodigoIngreso()))
			this.setErrorFactura("error.facturacion.ingresoEnProcesoAsocio", "");
	}
	
	/**
	 * @param numeroFactura
	 */
	public static boolean facturaTieneAjustesPendientes(int numeroFactura)
	{
		boolean respuesta=true;
		Connection con= UtilidadBD.abrirConexion();
		respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().facturaTieneAjustesPendientes(con,numeroFactura);
		UtilidadBD.closeConnection(con);
		return respuesta;
	}
	
	/**
	 * Metod que verifica si una factura tiene pagos pendientes.
	 * @param v
	 * @return
	 */
	public static boolean facturaTienePagosPendientes(int numeroFactura)
	{
		Connection con=UtilidadBD.abrirConexion();
		boolean respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().facturaTienePagosPendientes(con,numeroFactura);
		UtilidadBD.closeConnection(con);
		return respuesta;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public static boolean facturaTieneSaldoPendiente(Connection con, int numeroFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().facturaTieneSaldoPendiente(con,numeroFactura);
	}
	
	/**
	 * 
	 * @param codigoFactura
	 * @param codigo
	 * @return
	 */
	public static boolean facturaTieneAjustesPendientesDiferentesAjusteActual(int codigoFactura, double codigoAjuste) 
	{
		Connection con=UtilidadBD.abrirConexion();
		boolean respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().facturaTieneAjustesPendientesDiferentesAjusteActual(con,codigoFactura,codigoAjuste);
		UtilidadBD.closeConnection(con);
		return respuesta;
	}
	
	/**
	 * Aplicando ajustes.
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public static boolean facturaTieneSaldoPendiente(int numeroFactura) 
	{
		Connection con=UtilidadBD.abrirConexion();
		boolean respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().facturaTieneSaldoPendiente(con, numeroFactura);
		UtilidadBD.closeConnection(con);
		return respuesta;
	}
	
	/**
	 * @param numeroFactura
	 * @return
	 */
	public static boolean esFacturaCerrada(int numeroFactura) 
	{
		Connection con=UtilidadBD.abrirConexion();
		boolean respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().esFacturaCerrada(con, numeroFactura);
		UtilidadBD.closeConnection(con);
		return respuesta;
	}
	
	/**
	 * @param numeroFactura
	 * @return
	 */
	public static boolean esFacturaExterna(int numeroFactura) 
	{
		Connection con=UtilidadBD.abrirConexion();
		boolean respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().esFacturaExterna(con, numeroFactura);
		UtilidadBD.closeConnection(con);
		return respuesta;
	}
	
	/**
	 * Metodo para consultar si una factura tiene ajustes
	 * aprobados o anulados, en un rango de fecha.	 
	 * @param numeroFactura int 
	 * @param fecha String 
	 * @return array ArrayList
	 * @author jarloc
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList facturaAjustesAprobadosAnulados(int numeroFactura,String fecha)
	{
		Connection con=UtilidadBD.abrirConexion();
		ArrayList array=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().facturaAjustesAprobadosAnulados(con,numeroFactura,fecha);
		UtilidadBD.closeConnection(con);
		return array;
	}
	
	/**
	 * (Simulacion errors ApplicationResources.properties)
	 */
	@SuppressWarnings("unchecked")
	private void setErrorFactura (String llaveError, Vector atributosVector)
	{
		ElementoApResource elem=new ElementoApResource(llaveError);
		for(int i=0;i<atributosVector.size();i++)
			elem.agregarAtributo(atributosVector.get(i).toString());
		this.erroresFactura.add(elem);
	}

 	/**
  	 * Metodo para consultar si una factura existe en BD.
  	 * @param con Connection, conexiï¿½n con la fuente de datos
  	 * @param codigoFact in, cï¿½digo de la factura
  	 * @return int, 0 si no existe
  	 * @author jarloc
  	 */
  	public static int existeCodigoFactura (Connection con, int codigoFact)
  	{
  	    return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().existeCodigoFactura(con,codigoFact);
  	}
	
 	/**
	 * Consultar el consecutivo de la primera factura
	 * generada por el sistema
	 * @param con Connection, conexiï¿½n con la fuente de datos	 
	 * @return int, -1 si hay error
	 * @see com.princetonsa.dao.SqlBase.SqlBaseFacturaBORRAMEDao#primeraFacturaGeneradaSistema(Connection)
	 * @author jarloc
	 */
	public static int primeraFacturaGeneradaSistema (Connection con)
	{
	    return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().primeraFacturaGeneradaSistema(con);
	}

	/**
	 * 
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public static boolean facturaTieneAjustesPendientes(Connection con,int numeroFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().facturaTieneAjustesPendientes(con,numeroFactura);
	}
	

	/**
	 * 
	 * @param con
	 * @param codFactura
	 * @return
	 */
	public static boolean facturaTieneCastigoCartera(Connection con,int numeroFactura) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().facturaTieneCastigoCartera(con,numeroFactura);
	}

	/**
	 * Metodo implementado para consultar los pagos
	 * realizados a una factura, que no se encuentren
	 * en estado pendiente y que esten en un rango de
	 * fecha 
	 * @param con Connection
	 * @param numeroFactura int
	 * @param fecha String 
	 * @return array ArrayList
	 * @author jarloc
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList facturaTienePagos (int numeroFactura, String fecha)
	{		
		Connection con=UtilidadBD.abrirConexion();
		ArrayList array = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().facturaTienePagos(con,numeroFactura,fecha);
		UtilidadBD.closeConnection(con);			
		return array;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static boolean viaIngresoPermiteCorteFactura(Connection con, int codigoCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().viaIngresoPermiteCorteFactura(con, codigoCuenta);
	}

	/**
	 * Metodo que evalua los posibles errores de las solicitudes PYP, hasta el momento solo se verifica que las solicitudes 
	 * esten cubiertas por subcuenta
	 * @param con
	 * @param cuentas
	 * @parma subCuentas
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void analisisSolicitudesPYP(Connection con, Vector cuentas, Vector subCuentas)
	{
		Vector consecutivosVector= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().analisisSolicitudesPYP(con, cuentas, subCuentas);
		
		if(consecutivosVector.size()>0)
		{
			String cuentasStr= UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false);
			String consecutivosStr= UtilidadTexto.convertirVectorACodigosSeparadosXComas(consecutivosVector, false);
			Vector atributosVector= new Vector();
			atributosVector.add(cuentasStr);
			atributosVector.add(consecutivosStr);
			this.setWarningsFactura("error.facturacion.pyp.solNoCubiertasXConvenio", atributosVector);
		}
	}

	/**
	 * 
	 * @param con
	 * @param subCuentasMap
	 */
	@SuppressWarnings("unchecked")
	public void esPacienteCapitadoVigente(Connection con, HashMap<Object, Object> subCuentasMap, int codigoPersona, int ingreso)
	{
		for(int w=0; w<Integer.parseInt(subCuentasMap.get("numRegistros").toString()); w++)
		{
			if(subCuentasMap.get("seleccionado_"+w).toString().equals(ConstantesBD.acronimoSi))
			{	
				if(UtilidadTexto.getBoolean(subCuentasMap.get("escapitado_"+w)+""))
				{
					//sabiendo que es capitado entonces evaluamos la vigencia
					if(!DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().esPacienteCapitadoVigente(con, codigoPersona, Integer.parseInt(subCuentasMap.get("contrato_"+w)+""), ingreso))
					{
						Vector atributosVector= new Vector();
						atributosVector.add(subCuentasMap.get("nombreresponsable_"+w));
						this.setErrorFactura("error.facturacion.capitadoVencido", atributosVector);
					}
				}
			}
		}	
	}
	
	
	/**
	 * metodo que saca el warning de las validaciones de venezuela
	 * @param facturas
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoFactura> validacionInformacionVenezuela(ArrayList<DtoFactura> facturas) 
	{
		for(int w=0; w <facturas.size(); w++)
		{
			if(facturas.get(w).getValidarInfoVenezuela())
			{
				if(facturas.get(w).getValorMontoAutorizadoVenezuela()>facturas.get(w).getValorTotal())
				{
					//al responsable xxxx le hace falta por asignar yyy. verificar
					Vector atributoVector= new Vector();
					atributoVector.add(facturas.get(w).getConvenio().getNombre());
					atributoVector.add(UtilidadTexto.formatearValores(facturas.get(w).getValorMontoAutorizadoVenezuela()-facturas.get(w).getValorTotal()));
					this.setWarningsFactura("error.facturacion.montoAutorizadoVenezuela", atributoVector);
					
					//asignamos el valor a cargo del convenio y del paciente
					facturas.get(w).setValorConvenio(facturas.get(w).getValorTotal());
					facturas.get(w).setValorBrutoPac(0);
					facturas.get(w).setValorFaltaAsignarVenezuela(facturas.get(w).getValorMontoAutorizadoVenezuela()-facturas.get(w).getValorTotal());
				}
				else 
				{
					facturas.get(w).setValorConvenio(facturas.get(w).getValorMontoAutorizadoVenezuela());
					facturas.get(w).setValorBrutoPac(facturas.get(w).getValorTotal()-facturas.get(w).getValorMontoAutorizadoVenezuela());
					facturas.get(w).setValorFaltaAsignarVenezuela(0);
				}
			}
		}
		return facturas;
	}
	
	/**
	 * metodo que evalua la existencia del porcentaje o valor de pool, 
	 * ACLARACION: EN EL CASO DE CX Y PAQUETES SOLO SE EVALUA EL PORCENTAJE POOL PARA LOS COMPONENTES Y ASOCIOS, DESDE EL SQL PARA ESTOS CASOS EN LA PROPIA SOLICITUD VIENEN EN CERO CUANDO NO EXISTEN
	 * @param con
	 * @param facturas
	 */
	@SuppressWarnings("unchecked")
	public void existePorcentajeOValorPoolXSolicitud(Connection con, ArrayList<DtoFactura> facturas, int institucion) 
	{
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValoresDefectoValidarPoolesFact(institucion)))
		{
			for(int w=0; w <facturas.size(); w++)
			{
				HashMap mapaConvenioPool= new HashMap();
				int contador=0;
				for(int x=0; x<facturas.get(w).getDetallesFactura().size(); x++)
				{
					//evaluamos si el valor y el porcentaje es menor que cero
					if( (facturas.get(w).getDetallesFactura().get(x).getPorcentajePool()<0)
						&& (facturas.get(w).getDetallesFactura().get(x).getValorPool()<0))
					{	
						if(facturas.get(w).getDetallesFactura().get(x).getPool()>0)
						{	
							//esto siempre va ha depender del tipo de solicitud
							if(	facturas.get(w).getDetallesFactura().get(x).getCodigoTipoSolicitud()!=ConstantesBD.codigoTipoSolicitudPaquetes
								&& facturas.get(w).getDetallesFactura().get(x).getCodigoTipoSolicitud()!=ConstantesBD.codigoTipoSolicitudCirugia
								&& facturas.get(w).getDetallesFactura().get(x).getCodigoTipoSolicitud()!=ConstantesBD.codigoTipoSolicitudEstancia
								&& facturas.get(w).getDetallesFactura().get(x).getCodigoTipoSolicitud()!=ConstantesBD.codigoTipoSolicitudMedicamentos
								&& facturas.get(w).getDetallesFactura().get(x).getCodigoTipoSolicitud()!=ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos)
							{
								//no existe entonces lo debemos agregar como error
								if(!existePoolEnMapa(mapaConvenioPool, facturas.get(w).getDetallesFactura().get(x).getPool()+"", contador))
								{	
									//logger.info("\n ------------1 -------------------- codigo pool-->"+facturas.get(w).getDetallesFactura().get(x).getPool()+"  numero solicitud-->"+facturas.get(w).getDetallesFactura().get(x).getNumeroSolicitud()+"  tipo  solicitud-->"+facturas.get(w).getDetallesFactura().get(x).getCodigoTipoSolicitud());
									mapaConvenioPool.put("pool_"+contador, Utilidades.getNombrePool(con, facturas.get(w).getDetallesFactura().get(x).getPool()));
									mapaConvenioPool.put("codigopool_"+contador, facturas.get(w).getDetallesFactura().get(x).getPool());
									contador++;
								}
							}
						}	
					}
					//continuamos con los asocios de cx
					for(int y=0; y<facturas.get(w).getDetallesFactura().get(x).getAsociosDetalleFactura().size(); y++)
					{
						logger.info("asocio pool-->"+facturas.get(w).getDetallesFactura().get(x).getAsociosDetalleFactura().get(y).getPool());
						logger.info("porcentaje pool-->"+facturas.get(w).getDetallesFactura().get(x).getAsociosDetalleFactura().get(y).getPorcentajePool());
						logger.info("valor pool-->"+facturas.get(w).getDetallesFactura().get(x).getAsociosDetalleFactura().get(y).getValorPool());
						if(facturas.get(w).getDetallesFactura().get(x).getAsociosDetalleFactura().get(y).getPorcentajePool()<0
							&& facturas.get(w).getDetallesFactura().get(x).getAsociosDetalleFactura().get(y).getValorPool()<0)
						{
							if(facturas.get(w).getDetallesFactura().get(x).getAsociosDetalleFactura().get(y).getPool()>0)
							{	
								//no existe entonces lo debemos agregar como error
								if(!existePoolEnMapa(mapaConvenioPool, facturas.get(w).getDetallesFactura().get(x).getAsociosDetalleFactura().get(y).getPool()+"", contador))
								{	
									//logger.info("\n ------------2 --------------------");
									mapaConvenioPool.put("pool_"+contador, Utilidades.getNombrePool(con, facturas.get(w).getDetallesFactura().get(x).getAsociosDetalleFactura().get(y).getPool()));
									mapaConvenioPool.put("codigopool_"+contador, facturas.get(w).getDetallesFactura().get(x).getAsociosDetalleFactura().get(y).getPool());
									contador++;
								}
							}	
						}	
					}
				}
				
				//logger.info("\n el contador es -->"+contador);
				
				if(contador>0)
				{	
					//finalmente tenemos que agrgar un unico error por responsable
					String nombreConvenio="";
					if(!facturas.get(w).getEsParticular())
					{	
						nombreConvenio=facturas.get(w).getConvenio().getNombre();
					}
					else
					{
						nombreConvenio="PARTICULARES";
					}
					String poolesStr="";
					for(int z=0; z<contador; z++)
					{
						poolesStr+=mapaConvenioPool.get("pool_"+z)+",";
					}
					this.setErrorFactura("errors.notEspecific", "Falta parametrizaciï¿½n de porcentaje o valor pooles para el convenio "+nombreConvenio+" y pooles "+poolesStr+". Por favor verifique");
				}	
			}
		}	
	}
	
	
	/**
	 * 
	 * @param mapa
	 * @param codigoPool
	 * @param contador
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean existePoolEnMapa(HashMap mapa, String codigoPool, int contador)
	{
		for(int w=0; w<contador; w++)
		{
			if(codigoPool.equals(mapa.get("codigopool_"+w).toString()))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param conveniosMap
	 */
	@SuppressWarnings("unchecked")
	public void validacionResponsableRepetido1(HashMap conveniosMap) 
	{
		for(int w=0; w<Integer.parseInt(conveniosMap.get("numRegistros").toString()); w++)
		{
			//recorremos los que podemos facturar
			if(UtilidadTexto.getBoolean(conveniosMap.get("puedofacturar_"+w).toString()))
			{
				if(existeRepetido(w,conveniosMap))
				{
					Vector atributosVector= new Vector();
					atributosVector.add(conveniosMap.get("nombreresponsable_"+w));
					this.setErrorFactura("error.facturacion.convenioRepetido", atributosVector);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param w
	 * @param conveniosMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean existeRepetido(int posAComparar, HashMap conveniosMap) 
	{
		String codigoConvenioAComparar= conveniosMap.get("codigoconvenio_"+posAComparar)+"";
		logger.info("convenio a comparar="+codigoConvenioAComparar);
		for(int w=posAComparar+1; w<Integer.parseInt(conveniosMap.get("numRegistros").toString()); w++)
		{
			//recorremos los que podemos facturar
			if(UtilidadTexto.getBoolean(conveniosMap.get("puedofacturar_"+w).toString()))
			{
				if(conveniosMap.get("codigoconvenio_"+w).toString().equals(codigoConvenioAComparar))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param subCuentas
	 */
	@SuppressWarnings("unchecked")
	public void validacionesAutorizacionesYCobertura(Connection con, String idIngreso, Vector subCuentas, Vector cuentas) throws IPSException
	{
		logger.info("\n\n******validacionesAutorizacionesYCobertura***************************************************************");
		
		for(int w=0; w<subCuentas.size(); w++)
		{
			Vector subCuentaUnica= new Vector();
			subCuentaUnica.add(subCuentas.get(w));
			int contrato=0; 
			try
			{
				contrato=Integer.parseInt(obtenerContratosSubCuentas(con, idIngreso, subCuentaUnica, "" /*facturado*/).get(0)+"");
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			if(Contrato.manejaCobertura(con, contrato))
			{
				logger.info("CONTRATO-->"+contrato+" MANEJA COBERTURA");
				if(Contrato.requiereAutorizacionXNoCobertura(con, contrato))
				{
					String solicitudes=obtenerSolicitudesSinAutorizacion(con, cuentas, Double.parseDouble(subCuentas.get(w)+""), true);
					if(!UtilidadTexto.isEmpty(solicitudes))
					{	
						logger.info("REQUIERE AUTORIZACION!!!!1");
						Vector atributosVector= new Vector();
						atributosVector.add(solicitudes);
						Contrato contrato1= new Contrato();
						contrato1.cargar(con, contrato+"");
						atributosVector.add(Utilidades.obtenerNombreConvenioOriginal(con, contrato1.getCodigoConvenio()));
						this.setErrorFactura("error.facturacion.sinAutorizacion1", atributosVector);
					}
					
					solicitudes=obtenerSolicitudesSinAutorizacion(con, cuentas, Double.parseDouble(subCuentas.get(w)+""), false);
					if(!UtilidadTexto.isEmpty(solicitudes))
					{
						logger.info("REQUIERE AUTORIZACION!!!!2");
						Vector atributosVector= new Vector();
						atributosVector.add(solicitudes);
						Contrato contrato1= new Contrato();
						contrato1.cargar(con, contrato+"");
						atributosVector.add(Utilidades.obtenerNombreConvenioOriginal(con, contrato1.getCodigoConvenio()));
						this.setErrorFactura("error.facturacion.sinAutorizacion2", atributosVector);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param consecutivoOrdenMedica
	 */
	@SuppressWarnings("unchecked")
	public void validacionesConsumosMaterialesCx(Connection con, ArrayList<DtoFactura> arrayFactura)
	{
		for(int w=0; w<arrayFactura.size(); w++)
		{	
			for(int x=0; x<arrayFactura.get(w).getDetallesFactura().size();x++)
			{	
				if(arrayFactura.get(w).getDetallesFactura().get(x).getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudCirugia)
				{	
					HashMap<Object, Object> mapa= DevolucionPedidoQx.detalleSolicitudArticulosStatic(con, arrayFactura.get(w).getDetallesFactura().get(x).getNumeroSolicitud(), true);
					//el mapa devuelve codigoarticulo, descripcionarticulo, unidadmedida, cantidaddespachada, cantidadconsumo, cantidaddevuelta, diferencia
					for(int y=0; y<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");y++)
					{	
						int diferencia= Utilidades.convertirAEntero(mapa.get("diferencia_"+y)+"");
						if( diferencia!=0 )
						{
							//La Cx Nro.{0} contiene pedido(s) Qx con articulos que NO cumplen con la sgt formula CONSUMO TOTAL + DEVOLUCIONES TOTALES = DESPACHOS TOTALES, verificar despacho versus consumo. [bf-103]
							logger.info("CONSUMOS PENDIENTES!!!!!");
							Vector atributosVector= new Vector();
							atributosVector.add(Utilidades.getConsecutivoOrdenesMedicas(con, arrayFactura.get(w).getDetallesFactura().get(x).getNumeroSolicitud()));
							atributosVector.add(UtilidadTexto.convertirVectorACodigosSeparadosXComas(DevolucionPedidoQx.obtenerPedidosPeticionesQx(con, arrayFactura.get(w).getDetallesFactura().get(x).getNumeroSolicitud()) ,false));
							this.setErrorFactura("error.facturacion.consumosMateriales", atributosVector);
							//simulo break
							y=Utilidades.convertirAEntero(mapa.get("numRegistros")+"");
						}
					}
				}	
			}	
		}
	}
	
	/**
	 * Valida Si existen solicitudes sin Autorizaciones
	 * @param con
	 * @param facturas
	 */
	public void  validarSolicitudesSinAutorizaciones(Connection con,ArrayList<DtoFactura>  facturas){
		Boolean  faltanAutorizaciones= new Boolean (true);
		String convenio = "";
		
		//Se recorren todas las facturas que se porponen a ser facturadas
		for (int i = 0; i < facturas.size() && faltanAutorizaciones; i++) {

			//Se recorreo por cada factura sus detalles de factura que tiene cada una su respectiva solicitud a validar
			for (int j = 0; j < facturas.get(i).getDetallesFactura().size() && faltanAutorizaciones; j++) {
				
				//se valida si tiene una autorización asociada
				faltanAutorizaciones=Factura.tieneSolicitudesSinAutorizar(con,facturas.get(i).getDetallesFactura().get(j).getNumeroSolicitud());
				
				//si no tiene  una autorizacion asociada entonces se obtienen los datos del convenio 
				if(!faltanAutorizaciones){
					
					//se obtiene el codigo y el nombre del convenio que no tiene una autorizacion para la solicitud consultada
					convenio =facturas.get(i).getConvenio().getCodigo()+" "+facturas.get(i).getConvenio().getNombre();
				}
			}
		}
		
		//si se encontro que no todas las solicitudes tengan una autorizacion asociada entonces se adiciona un mensaje error 
		if(!faltanAutorizaciones){
			this.setErrorFactura("errors.notEspecific", "Para facturar el Convenio "+convenio+"," +
					" deben estar Autorizados todos los Servicios y Medicamentos / " +
			"Insumos. Por favor verifique. Proceso Cancelado.");
		}
	}
	
	
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param conveniosSeleccionadosList
	 */
	public void existeTipoLiquidacionPoolXConvenios(Connection con,
			ArrayList<InfoDatosInt> conveniosSeleccionadosList, int institucion)
	{
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValoresDefectoValidarPoolesFact(institucion)))
		{	
			String error="";
			for(InfoDatosInt info: conveniosSeleccionadosList)
			{
				if(UtilidadTexto.isEmpty(Convenio.obtenerTipoLiquidacionPool(info.getCodigo())))
				{
					error+=" - "+info.getNombre();
				}
			}
			if(!UtilidadTexto.isEmpty(error))
			{
				this.setErrorFactura("errors.notEspecific", "Debe ingresar el Tipo de Liquidaciï¿½n Pool X Convenio de: "+error);
			}
		}	
	}
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param subCuentasSeleccionadasVector
	 * @param codigoInstitucionInt
	 */
	@SuppressWarnings("unchecked")
	public void existeTipoLiquidacionXMedicoResponde(	Connection con,
														Vector cuentas, 
														Vector subCuentasSeleccionadasVector,
														int codigoInstitucionInt) 
	{
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValoresDefectoValidarPoolesFact(codigoInstitucionInt)))
		{	
			ArrayList<String> lista=obtenerMedicoRespondeSinTipoLiquidacion(con, cuentas, subCuentasSeleccionadasVector);
			for(String m: lista)
			{
				this.setErrorFactura("errors.notEspecific", "Debe ingresar el Tipo de Liquidaciï¿½n para el mï¿½dico "+m);
			}
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param subCuentasSeleccionadasVector
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<String> obtenerMedicoRespondeSinTipoLiquidacion(	Connection con,
																Vector cuentas, 
																Vector subCuentasSeleccionadasVector) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().obtenerMedicoRespondeSinTipoLiquidacion(con, cuentas,subCuentasSeleccionadasVector);
	}

	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param subCuenta
	 * @param cubierta
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String obtenerSolicitudesSinAutorizacion( Connection con, Vector cuentas, double subCuenta, boolean cubierta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().obtenerSolicitudesSinAutorizacion(con, cuentas, subCuenta, cubierta);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 * @return
	 */
	public static Vector<String> obtenerCuentasActivaPacFactura(Connection con, String consecutivoFactura, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().obtenerCuentasActivaPacFactura(con, consecutivoFactura, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 * @return
	 */
	public static Vector<String> obtenerIngresosAbiertosPacFactura(BigDecimal codigoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().obtenerIngresosAbiertosPacFactura(codigoFactura);
	}
	
	/**
	 * 
	 * @param con
	 * @param conseuctivoFactura
	 * @param institucion
	 * @return
	 */
	public static boolean puedoAnularFacturaXGlosa(BigDecimal codigoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().puedoAnularFacturaXGlosa(codigoFactura);
	}
	
	/**
	 * 
	 * @param codigoFactura
	 * @return
	 */
	public static boolean puedoAnularFacturaXReclamaciones(BigDecimal codigoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().puedoAnularFacturaXReclamaciones(codigoFactura);
	}
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean generaCargoEnSolicitud(Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().generaCargoEnSolicitud(con, numeroSolicitud);
	}
	
	
	/**
	 * con
	 */
	public static boolean validarPreglosaRespondida (Connection con, String numeroFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesFacturaDao().validarPreglosaRespondida(con,numeroFactura);
	}
	
	
	/**
	 * 
	 * @param llaveError
	 * @param atrib
	 */
	@SuppressWarnings("unchecked")
	public void setErrorFactura(String llaveError, String atrib)
	{
		Vector atributosVector= new Vector();
		atributosVector.add(atrib);
		this.setErrorFactura(llaveError, atributosVector);
	}
	
	/**
	 * (Simulacion errors AppliccationsResources.properties)
	 */
	@SuppressWarnings("unchecked")
	private void setWarningsFactura (String llaveError, Vector atributosVector)
	{
		ElementoApResource elem=new ElementoApResource(llaveError);
		for(int i=0;i<atributosVector.size();i++)
			elem.agregarAtributo(atributosVector.get(i).toString());
		this.warningsFactura.add(elem);
	}

	/**
	 * 
	 * @param llaveError
	 * @param atrib
	 */
	@SuppressWarnings("unchecked")
	private void setWarningsFactura(String llaveError, String atrib)
	{
		Vector atributosVector= new Vector();
		atributosVector.add(atrib);
		this.setWarningsFactura(llaveError, atributosVector);
	}
	
	/**
	 * @return the erroresFactura
	 */
	public ArrayList<ElementoApResource> getErroresFactura() {
		return erroresFactura;
	}

	/**
	 * @param erroresFactura the erroresFactura to set
	 */
	public void setErroresFactura(ArrayList<ElementoApResource> erroresFactura) {
		this.erroresFactura = erroresFactura;
	}

	/**
	 * @return the warningsFactura
	 */
	public ArrayList<ElementoApResource> getWarningsFactura() {
		return warningsFactura;
	}

	/**
	 * @return the warningsFactura
	 */
	public ElementoApResource getWarningFactura(int pos) {
		return warningsFactura.get(pos);
	}
	
	/**
	 * @param warningsFactura the warningsFactura to set
	 */
	public void setWarningsFactura(ArrayList<ElementoApResource> warningsFactura) {
		this.warningsFactura = warningsFactura;
	}

	/**
	 * @return the pagosPacienteMEM
	 */
	@SuppressWarnings("unchecked")
	public Hashtable getPagosPacienteMEM() {
		return pagosPacienteMEM;
	}

	/**
	 * @param pagosPacienteMEM the pagosPacienteMEM to set
	 */
	@SuppressWarnings("unchecked")
	public void setPagosPacienteMEM(Hashtable pagosPacienteMEM) {
		this.pagosPacienteMEM = pagosPacienteMEM;
	}

	

}
