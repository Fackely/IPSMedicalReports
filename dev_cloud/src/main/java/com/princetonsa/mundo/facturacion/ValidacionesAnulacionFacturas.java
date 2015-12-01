/*
 * @(#)ValidacionesAnulacionFacturas.java
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
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ResultadoInteger;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.ConstantesBDFacturacion;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.interfaces.UtilidadBDInterfaz;

import com.princetonsa.action.ComunAction;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ValidacionesAnulacionFacturasDao;
import com.princetonsa.dto.facturacion.DtoConsultaFacturasAnuladas;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.ConsecutivosCentroAtencion;

/**
 * Clase para el manejo de las validaciones de la anulacion de facturas
 * @version 1.0, Agosto 04, 2005
 */
public class ValidacionesAnulacionFacturas 
{
	Logger logger=Logger.getLogger(ValidacionesAnulacionFacturas.class);
    /**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ValidacionesAnulacionFacturasDao valDao;
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
	
		if (myFactory != null)
		{
			valDao = myFactory.getValidacionesAnulacionFacturasDao();
			wasInited = (valDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Constructor de la clase, inicializa en vacio todos los parámetros
	 */
	public ValidacionesAnulacionFacturas()
	{
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * Retorna:
	 * 0 El mapa no tiene informacion
	 * 1 Los Valores estan descuadrados
	 * 2 Los valores estan Cuadrados
	 * Metodo que valida saldos de la factura con la interfaz Contable 
	 * @param Connection con
	 * @param int codigoFactura
	 * */
	@SuppressWarnings("unchecked")
	public ResultadoInteger facturaSaldoFacturaInterfazContable(Connection con, int consecutivoFactura,String nitFactura, int codigoInstitucion)
	{
		HashMap parametros = new HashMap();
		HashMap resultado = new HashMap();
		
		if(ValoresPorDefecto.getInterfazContableFacturas(codigoInstitucion).toString().equals(ConstantesBD.acronimoSi))
		{
			UtilidadBDInterfaz interfaz = new UtilidadBDInterfaz();
			
			parametros.put("consecutivofactura", consecutivoFactura);
			parametros.put("nitfactura", nitFactura);
			parametros.put("institucion",codigoInstitucion);
			resultado = interfaz.consultarSaldosCarteraPaciente(parametros);
			logger.info("RESULTADO EN LA VALIDACION --->"+resultado+"<-");
			if (!UtilidadTexto.getBoolean(resultado.get("isError")+""))
			{
				if(Utilidades.convertirAEntero(resultado.get("numRegistros")+"")>0)
				{	
					double salmae = 0; 
					salmae=Utilidades.convertirADouble(resultado.get("SALMAE_0").toString());
					double vlrmae = 0;
					vlrmae=Utilidades.convertirADouble(resultado.get("VLRMAE_0").toString());
					
					logger.info("SALMAE ->"+salmae+"<-");
					logger.info("VLRMAE ->"+vlrmae+"<-");
					
					if(resultado.get("numRegistros").toString().equals("1") && (salmae == vlrmae ) )
					{	
						return new ResultadoInteger(ConstantesBDFacturacion.codigoEsIgualInfoSaldoInterfaz,"");
					}	
					else
					{	
						return  new ResultadoInteger(ConstantesBDFacturacion.codigoNoEsIgualInfoSaldoInterfaz,"");
					}	
				}
				else
				{	
					return  new ResultadoInteger(ConstantesBD.codigoNuncaValido,resultado.get("descError")+"");
				}	
			} 
			else 
			{
				UtilidadBD.abortarTransaccion(con);
				return new ResultadoInteger(ConstantesBD.codigoNuncaValido,resultado.get("descError")+"");
			}
			
		}
		return new ResultadoInteger(ConstantesBDFacturacion.codigoEsIgualInfoSaldoInterfaz,"");
	}
	
	/**
	 * Metodo que valida saldos de la factura con la interfaz Contable
	 * Requerimiento Anexo 657. pagina 2 
	 * @param Connection con
	 * @param int codigoFactura
	 * */
	@SuppressWarnings("unchecked")
	public ResultadoInteger esFacturaEstadoFacturaCuentasMedicasInvalida(Connection con, int codigoFactura,String numeroIdentificacion, int codigoInstitucion)
	{
		HashMap parametros = new HashMap();
		HashMap resultado = new HashMap();
		
		if(ValoresPorDefecto.getInterfazContableFacturas(codigoInstitucion).toString().equals(ConstantesBD.acronimoSi))
		{
			UtilidadBDInterfaz interfaz = new UtilidadBDInterfaz();
			
			parametros.put("factura", codigoFactura);
			parametros.put("idpaciente", numeroIdentificacion);
			parametros.put("institucion",codigoInstitucion);
			resultado = interfaz.consultarEstadoCuentasMedicas(parametros);
			
			if(UtilidadTexto.getBoolean(resultado.get("procesoExitoso")+"")){
			
				if(!resultado.get("numRegistros").toString().equals("0"))
				{
					if((resultado.get("esgfac_0").toString().equals("4") || 
							resultado.get("esgfac_0").toString().equals("50") || 
								resultado.get("esgfac_0").toString().equals("70") ||
									resultado.get("esgfac_0").toString().equals("75") || 
										resultado.get("esgfac_0").toString().equals("80") || 
											resultado.get("esgfac_0").toString().equals("85") || 
												resultado.get("esgfac_0").toString().equals("90") || 
													resultado.get("esgfac_0").toString().equals("95")))
					{
						return new ResultadoInteger(ConstantesBD.codigoNuncaValido, "ESTADO DE LA FACTURA CUENTA MEDICA INTERFAZ NO VALIDA PARA ANULAR. POR FAVOR VERIFIQUE");
					}
					else				
						return new ResultadoInteger(1, "");				
				}
			} else {
				return new ResultadoInteger(ConstantesBD.codigoNuncaValido, resultado.get("mensaje")+"");
			}
		}
		return new ResultadoInteger(1, "");
	}
	
	/**
	 * Metodo que verifica la existencia de otras cuentas abiertas, abiertasDistribuidas, asociadas, asociadasDistribuidas 
	 * asociadasFacturadaParcial, facturadaParcial de un paciente y una cuenta dado el idFactura
	 * @param con
	 * @param codigofactura
	 * @return
	 */
	public boolean existenOtrasCuentasAbiertas(Connection con, int codigoFactura)
	{
	    return valDao.existenOtrasCuentasAbiertas(con, codigoFactura);
	}
	
	/**
	 * Método que devuelve el estado de la cuenta dado su id
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static int getEstadoCuenta(Connection con, int idCuenta)
	{
	    return valDao.getEstadoCuenta(con, idCuenta);
	}
	
	/**
	 * Metodo que indica si una factura esta  en un cierre inicial de cartera
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean estaFacturaEnCierreInicialCartera(int codigoFactura)
	{
		Connection con= UtilidadBD.abrirConexion();
	    boolean returna= valDao.estaFacturaEnCierreInicialCartera(con, codigoFactura);
	    UtilidadBD.closeConnection(con);
	    return returna;
	}
	
	/**
	 * Metodo que indica si la facura pertenece a un particular o a un convenio
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean esFacturaResponsableParticular(Connection con, int codigoFactura)
	{
	    return valDao.esFacturaResponsableParticular(con, codigoFactura);
	}
	
	/**
	 * Metodo que indica si una factura pertenece a un responsable CAPITADO
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean facturaPerteneceAConvenioCapitado(Connection con, int codigoFactura)
	{
	    return valDao.facturaPerteneceAConvenioCapitado(con, codigoFactura);
	}
	
	/**
	 * Método que indica si una factura esta asociada a un pagare
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String estaFacturaAsociadaAPagare(int codigoFactura)
	{
		Connection con= UtilidadBD.abrirConexion();
	    String returna= valDao.estaFacturaAsociadaAPagare(con, codigoFactura);
	    UtilidadBD.closeConnection(con);
	    return returna;
	}
	
	/**
	 * Metodo que indica si la factura pertenece a una cuenta de cobro en caso de ser asi devuelve el
	 * numero de cuenta de cobro
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String getCuentaCobroDeFactura(int codigoFactura)
	{
		Connection con= UtilidadBD.abrirConexion();
	    String returna= valDao.getCuentaCobroDeFactura(con, codigoFactura);
	    UtilidadBD.closeConnection(con);
	    return returna;
	}
	
	/**
	 * Método que devuelve  'naturalezaAjuste - numeroAjuste' ya sea por factura o por cuenta de cobro, 
	 * toma los ajustes que estan en estado generado, es decir, los que estan pendientes de anular o aprobar,
	 * en caso de ser "" entonces es que no existen. 
	 * @param con
	 * @param codigoFactura
	 * @param cuentaCobro
	 * @return
	 */
	public String facturaTieneAjustesPendientesEnFacturaYCuentaCobro(int codigoFactura, String cuentaCobro)
	{
		Connection con= UtilidadBD.abrirConexion();
	    String returna= valDao.facturaTieneAjustesPendientesEnFacturaYCuentaCobro(con, codigoFactura, cuentaCobro);
	    UtilidadBD.closeConnection(con);
	    return returna;
	}
	
	/**
	 * Validacion de la sumatoria del movimiento en cartera = 0, es decir (ajustes_debito - ajustes_credito) = 0 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean sumatoriaMovCarteraEsCero(int codigoFactura)
	{
		Connection con= UtilidadBD.abrirConexion();
	    boolean returna= valDao.sumatoriaMovCarteraEsCero(con, codigoFactura);
	    UtilidadBD.closeConnection(con);
	    return returna;
	}
	
	/**
	 * Método que indica si una factura tiene un valor de abono
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean existeValorAbonoFactura (Connection con, int codigoFactura)
	{
	    return valDao.existeValorAbonoFactura(con, codigoFactura);
	}
	
	/**
	 * Método que indica si una factura tiene un valor bruto paciente
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean existeValorBrutoPaciente (Connection con, int codigoFactura)
	{
	    return valDao.existeValorBrutoPaciente(con, codigoFactura);
	}
	
	/**
	 * obtiene el valor de bruto paciente menos el valor de descuento
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public double getValorBrutoPacMenosValDescuento(Connection con, String codigoFactura)
	{
		return valDao.getValorBrutoPacMenosValDescuento(con, codigoFactura);
	}
	
	
	/**
	 * Método que indica si una cuenta tiene o no asocio
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public boolean cuentaTieneAsocio(Connection con, int codigoCuenta)
	{
	    return valDao.cuentaTieneAsocio(con, codigoCuenta);
	}
	
	/**
	 * Método que indica si la cuenta asociada esta en una sola factura
	 * @param con
	 * @param codigoCuenta
	 * @param codigoCuentaAsociada
	 * @return
	 */
	public boolean esCuentaAsociadaEnUnaSolaFactura(Connection con, int codigoCuenta, int codigoCuentaAsociada)
	{
	    return valDao.esCuentaAsociadaEnUnaSolaFactura(con, codigoCuenta, codigoCuentaAsociada);
	}
	
	/**
	 * Método que devuelve le codigo de la cuenta asociada dado un id de ingreso
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public Vector<String> getCodigoCuentasAsociadas(Connection con, int ingreso)
	{
	    return valDao.getCodigoCuentasAsociadas(con, ingreso);
	}
	
	/**
	 * Método que indica si una cuenta distribuida es unida, de lo contrario es independiente
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public boolean cuentaDistribuidaEsUnida(Connection con, int codigoCuenta)
	{
	    return valDao.cuentaDistribuidaEsUnida(con, codigoCuenta);
	}
	
	/**
	 * Método que devuelve le codigo de la cuenta asociada dado un id de cuenta
	 * @param con
	 * @param codigoCuenta
	 * @param valorTrueSegunBD
	 * @return
	 */
	public int getCodigoCuentaDadaFactura(Connection con, int codigoFactura)
	{
	    return valDao.getCodigoCuentaDadaFactura(con, codigoFactura);
	}
	
	/**
	 * Método que devuelve le codigo de la cuenta asociada dado un id de cuenta
	 * @param con
	 * @param codigoCuenta
	 * @param valorTrueSegunBD
	 * @return
	 */
	public int getCodigoCuentaPrincipalDadaFactura(Connection con, int codigoFactura)
	{
	    return valDao.getCodigoCuentaPrincipalDadaFactura(con, codigoFactura);
	}
	
	/**
	 * Metodo que indica si una cuenta tiene mi9nimo otra factura facturada
	 * @param con
	 * @param codigoFactura
	 * @param codigoCuenta
	 * @return
	 */
	public boolean cuentaTieneMinimoOtraFacturaFacturada (Connection con, int codigoFactura, int codigoCuenta)
	{
	    return valDao.cuentaTieneMinimoOtraFacturaFacturada(con, codigoFactura, codigoCuenta);
	}
	
	
	/**
	 * Método que evalua si existe la parametrizacion de lod motivos de anulacion
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean existenMotivosAnulacion(Connection con, int codigoInstitucion)
	{
	    return valDao.existenMotivosAnulacion(con, codigoInstitucion);
	}
	
	/**
	 * Método que devuelve le codigo de la subcuenta dado un id de cuenta
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public  int getSubCuentaDadaFactura(Connection con, int codigoFactura)
	{
	    return valDao.getSubCuentaDadaFactura(con, codigoFactura);
	}
	
	/**
	 * Metodo que verifica si el valor neto a cargo del paciente es mayor a cero o no
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean esValorNetoACargoPacienteMayorCero(String codigoFactura)
	{
		Connection con= UtilidadBD.abrirConexion();
	    boolean returna= valDao.esValorNetoACargoPacienteMayorCero(con, codigoFactura);
	    UtilidadBD.closeConnection(con);
	    return returna;
	}
	
	/**
	 * metodo que retorna el codigo del estado de pago del paciente
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public int getCodigoEstadoPagoPaciente(String codigoFactura)
	{
		Connection con= UtilidadBD.abrirConexion();
	    int returna= valDao.getCodigoEstadoPagoPaciente(con, codigoFactura);
	    UtilidadBD.closeConnection(con);
	    return returna;
	}
	
	/**
	 * Metodo con las validaciones basicas
	 * @param codigoPkFactura
	 * @param consecutivoFactura
	 * @return
	 */
	public static ActionErrors validacionesAnulacionBasicas(BigDecimal codigoPkFactura, String consecutivoFactura, UsuarioBasico usuario)
	{
		DtoFactura dto= Factura.cargarFactura(codigoPkFactura, false);
		ActionErrors errores = new ActionErrors();
		
		//1. VALIDAMOS QUE LA FACTURA NO ESTE EN ESTADO ANULADO
		if(dto.getEstadoFacturacion().getCodigo()==ConstantesBD.codigoEstadoFacturacionAnulada)
		{
			errores.add("", new ActionMessage("error.factura.estadoFacturacion", consecutivoFactura, "Anulada"));
    	}
		
		//2. VALIDAMOS QUE LA FACTURA TENGA LOS ESTADOS PACIENTE
		if(dto.getEstadoPaciente().getCodigo()!=ConstantesBD.codigoEstadoFacturacionPacientePorCobrar
				&& dto.getEstadoPaciente().getCodigo()!=ConstantesBD.codigoEstadoFacturacionPacienteConDevolucion 
				&& dto.getEstadoPaciente().getCodigo()!=ConstantesBD.codigoEstadoFacturacionPacienteSinValorPaciente)
		{	
			errores.add("No cumple con los estados de paciente", new ActionMessage("error.facturacion.estadosParaAnular"));
		}
		
		//3. VALIDAMOS QUE ESTE FACTURADA
		if(dto.getEstadoFacturacion().getCodigo()!=ConstantesBD.codigoEstadoFacturacionFacturada)
		{
	    	errores.add("No cumple con el estado de facturacion", new ActionMessage("error.facturacion.estadoFacturacion", "Facturada"));
        }
	
		//4 FACTURA CON RECIBO CAJA
		if(dto.getEstadoPaciente().getCodigo()==ConstantesBD.codigoEstadoFacturacionPacienteCancelada)
		{
			errores.add("la factura tiene un recibo de caja asociado", new ActionMessage("error.facturacion.facturaAsocioReciboCaja"));
    	}
	    
		//5. VALIDAMOS QUE NO EXISTA OTRO INGRESO ABIERTO
		Vector<String> ingresosActivos= ValidacionesFactura.obtenerIngresosAbiertosPacFactura(codigoPkFactura);
		if(ingresosActivos.size()>0)
		{
			errores.add("", new ActionMessage("error.facturacion.otroIngresoAbierto", UtilidadTexto.convertirVectorACodigosSeparadosXComas(ingresosActivos, false)));
		}
		
		//6. VERIFICAMOS QUE PODAMOS ANULAR X GLOSAS
		if(!ValidacionesFactura.puedoAnularFacturaXGlosa(codigoPkFactura))
		{
			errores.add("", new ActionMessage("error.facturacion.facturaLigadaGlosa"));
		}
		
		// LA FACTURA TIENE RECLAMACIONES EN ESTADO GENERADA O RADICADA
		if(!ValidacionesFactura.puedoAnularFacturaXReclamaciones(codigoPkFactura))
		{
			errores.add("", new ActionMessage("error.facturacion.facturaLigadaReclamacion"));
		}
		
		ValidacionesAnulacionFacturas objectValidacionesAnulacion= new ValidacionesAnulacionFacturas();
		
		//7. VERIFICAMOS QUE NO ESTE EN CIERRE INICIAL DE CARTERA
		if(objectValidacionesAnulacion.estaFacturaEnCierreInicialCartera(codigoPkFactura.intValue()))
        {
            errores.add("", new ActionMessage("error.facturacion.factEnCierreInicial", consecutivoFactura));
        }
		
		//8. VERIFICAMOS QUE NO TENGA CUENTA DE COBRO
	    String cuentaCobroTemp= ConstantesBD.codigoNuncaValido+"";
        if(!objectValidacionesAnulacion.getCuentaCobroDeFactura(codigoPkFactura.intValue()).equals(""))
        {
        	cuentaCobroTemp= objectValidacionesAnulacion.getCuentaCobroDeFactura(codigoPkFactura.intValue());
            errores.add("", new ActionMessage("error.facturacion.cuentaCobro", cuentaCobroTemp));
        }
        
        //9. VALIDAMOS QUE NO ESTE ASOCIADA A UN PAGARE
        if(!objectValidacionesAnulacion.estaFacturaAsociadaAPagare(codigoPkFactura.intValue()).equals(""))
        {
            errores.add("", new ActionMessage("error.facturacion.pagare", objectValidacionesAnulacion.estaFacturaAsociadaAPagare(codigoPkFactura.intValue())));
        }
        
        //10. VALIDAMOS QUE NO TENGA AJUSTES PENDIENTES EN FACTURA Y CUENTA COBRO
        if(!objectValidacionesAnulacion.facturaTieneAjustesPendientesEnFacturaYCuentaCobro(codigoPkFactura.intValue(), cuentaCobroTemp ).equals(""))
        {
            errores.add("", new ActionMessage("error.facturacion.facturaAjustesPendientes", objectValidacionesAnulacion.facturaTieneAjustesPendientesEnFacturaYCuentaCobro(codigoPkFactura.intValue(), cuentaCobroTemp )+""));
        }
        
        //11. VALIDAMOS QUE LA SUMATORI DE MOVIMIENTOS CARTERA SEA CERO
        if(!objectValidacionesAnulacion.sumatoriaMovCarteraEsCero(codigoPkFactura.intValue()))
        {
            errores.add("", new ActionMessage("error.facturacion.facturaConMovCartera"));
        }
		
        //12. SI REQUIERE AUTORIZAR ANULACION 
        if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValoresDefectoRequiereAutorizarAnularFacturas(usuario.getCodigoInstitucionInt())))
		{
			if(!UtilidadValidacion.facturaAprobadaAnular(codigoPkFactura.intValue()))
			{
				errores.add("descripcion",new ActionMessage("errors.notEspecific","No se encuentra aprobada la anulacion de esta factura."));
			}
		}
        
        //13. SOLAMENTE SE DEJA ANULAR FACTURAS QUE PERTENEZCAN AL MISMO CENTRO DE ATENCION DEL USUARIO
    	if(dto.getCentroAtencion().getCodigo()!=usuario.getCodigoCentroAtencion())
    	{	
    		errores.add("error.facturacion.noPuedeAnular", new ActionMessage("error.facturacion.noPuedeAnular", consecutivoFactura ,dto.getCentroAtencion().getNombre()));
     	}
        
    	//14. VALIDAMOS LOS PAGOS PARCIALES
    	if(objectValidacionesAnulacion.esValorNetoACargoPacienteMayorCero(codigoPkFactura+""))
		{
		    if(objectValidacionesAnulacion.getCodigoEstadoPagoPaciente(codigoPkFactura+"")==ConstantesBD.codigoEstadoFacturacionPacientePorCobrar)
		    {
		        if(UtilidadValidacion.existenPagosParcialesFacturaPaciente(usuario.getCodigoInstitucionInt(), ConstantesBD.codigoEstadoPagosRecaudado, codigoPkFactura+""))
		        {
		            errores.add("la factura tiene pagos de paciente registrados", new ActionMessage("error.facturacion.pagosParcialesPacRegistrados"));
		        }
		    }
		}
    	
    	//15 PARA LOS INGRESOS ODONTOLOGICOS LA CUENTA DEBE ESTAR EN ESTADO ACTIVA
    	if(esFacturaIngresoOdontologico(codigoPkFactura))
    	{
    		/*
    		 * 
    		 * Modificio por que la cuentas debe estar activa
    		 * Edgar Carvajal OJO
    		 * cambio con felipe
    		 */ 
    		if(UtilidadesHistoriaClinica.obtenerEstadoCuenta(Utilidades.convertirAEntero(dto.getCuentas().get(0)+"")).getCodigo()!=ConstantesBD.codigoEstadoCuentaActiva)
    		{
    			errores.add("descripcion",new ActionMessage("errors.notEspecific","La cuenta facturada no se encuentra en estado activo."));
    		}
    	
    	}
    	
		return errores;
	}

	
	/**
	 * Metodo para validar si la factura viene o no de un ingreso odontologico
	 * @param codigoFactura
	 * @return
	 */
	public static boolean esFacturaIngresoOdontologico(BigDecimal codigoFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesAnulacionFacturasDao().esFacturaIngresoOdontologico(codigoFactura);
	}
	
	/**
	 * 
	 * @param con
	 * @param trim
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean existeConsecutivoEnFactura(String consecutivoFactura,int codigoInstitucion) 
	{
		Connection con= UtilidadBD.abrirConexion();
		boolean returna= Factura.busquedaPorConsecutivoDianEInstitucion(con, consecutivoFactura, codigoInstitucion, "").size()>0;
		UtilidadBD.closeConnection(con);
		return returna;
	}
	
	
	/**
	 * Metodo para validar la existencia del motivo de anulacion
	 * 
	 * @param mapping
	 * @param request
	 * @param con
	 * @param user
	 */
	public ActionErrors validarMotivosAnulacion(UsuarioBasico usuario)
	{
		ValidacionesAnulacionFacturas objetoValidaciones= new ValidacionesAnulacionFacturas();
		Connection con=UtilidadBD.abrirConexion();
		ActionErrors errores = new ActionErrors();
		if(!objetoValidaciones.existenMotivosAnulacion(con, usuario.getCodigoInstitucionInt()))
		{
			logger.warn("no existe la parametrizacion de motivos de anulacion");
		    errores.add("descripcion",new ActionMessage("error.facturacion.parametrizarMotivosAnul"));
		}
		UtilidadBD.closeConnection(con);
		return errores;
	}	
	
	
	/**
	 * Metodo para validar la existencia de los consecutivos de anulacion factura, 
	 * 1. X CENTRO ATENCION
	 * 2. X INSTITUCION
	 *  
	 * @param codigoInstitucion
	 * @param centroAtencion
	 * @return
	 */
	public ActionErrors existeConsecutivoAnulacionFactura(int codigoInstitucion, int centroAtencion) 
	{
		//1. PRIMERO IDENTIFICAMOS SI EL CONSECUTIVO CENTRO DE ATENCION
		if(ValoresPorDefecto.getManejaConsecutivoFacturaPorCentroAtencionBool(codigoInstitucion))
		{
			logger.info("X CENTRO ATENCION");
			return validarConsecutivoXCentroAtencion(centroAtencion);
		}
		//2. DE LO CONTRARIO TOMAMOS EL CONSECUTIVO X INSTITUION
		else 
		{
			logger.info("X INSTITUCION");
			return validarConsecutivoXInstitucion(codigoInstitucion);
		}
	}
	
	/**
	 * @param centroAtencion
	 * @param retorna
	 * @return
	 */
	private ActionErrors validarConsecutivoXCentroAtencion(int centroAtencion) 
	{
		BigDecimal valorActualConsecutivo= ConsecutivosCentroAtencion.obtenerValorActualConsecutivo(centroAtencion, ConstantesBD.nombreConsecutivoAnulacionFactura, UtilidadFecha.getMesAnioDiaActual("anio"));
		ActionErrors errores= new ActionErrors();
		if(valorActualConsecutivo.doubleValue()<=0)
		{
			errores.add("",new ActionMessage("error.paciente.faltaDefinirConsecutivo", "Facturas por Centro de Atencion"));
		}
		return errores;
	}
	
	/**
	 * 
	 * @param codigoInstitucion
	 * @return
	 */
	private ActionErrors validarConsecutivoXInstitucion(int codigoInstitucion) 
	{
		ActionErrors errores= new ActionErrors();
		Connection con= UtilidadBD.abrirConexion();
		BigDecimal valorActualConsecutivo= new BigDecimal(Utilidades.convertirAEntero(UtilidadBD.obtenerValorActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoAnulacionFactura, codigoInstitucion)));
		UtilidadBD.closeConnection(con);
		
		if(valorActualConsecutivo.doubleValue()<=0)
		{
			errores.add("",new ActionMessage("error.paciente.faltaDefinirConsecutivo", "Facturas por Institucion"));
		}
		return errores;
	}
	
	
	/**
	 * Restricciones del reporte
	 * @param dto
	 * @return
	 */
	public static String obtenerRestriccionesReporte(DtoConsultaFacturasAnuladas dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesAnulacionFacturasDao().obtenerRestriccionesReporte(dto);
	}
	
	
	/**
	 * 
	 */
	public static ActionForward validacionesInterfaz(	Connection con, 
														String consecutivoFactura, 
														BigDecimal codigoFactura, 
														UsuarioBasico usuario,
														HttpServletRequest request,
														ActionMapping mapping,
														Logger logger,
														String consecutivoAnulacion,
														String numeroIdentificacionPaciente)
	{
		ValidacionesAnulacionFacturas objetoValidacionesAnulacion= new ValidacionesAnulacionFacturas();
		
		ResultadoInteger resultadoInteger = objetoValidacionesAnulacion.facturaSaldoFacturaInterfazContable(
											con,
											Utilidades.convertirAEntero(consecutivoFactura),
											Utilidades.obtenerNitConveniodeCodfactura(con, codigoFactura.intValue()),
											usuario.getCodigoInstitucionInt());
		
		//modifcacion por anexo 779
		if (resultadoInteger.getResultado() == ConstantesBD.codigoNuncaValido)
		{	
			UtilidadBD.abortarTransaccion(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, resultadoInteger.getDescripcion(), resultadoInteger.getDescripcion(), false);
		}	
		
		int saldoInterfaz=resultadoInteger.getResultado();
		if(saldoInterfaz != ConstantesBD.codigoNuncaValido)
		{
			if (saldoInterfaz == ConstantesBDFacturacion.codigoNoExisteInfoSaldoInterfaz)
			{
				logger.warn("(AnulacionFacturasAction) No hay datos de Saldos Facturas en la Interfaz  "+consecutivoAnulacion);
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				request.setAttribute("codigoDescripcionError", "error.facturacion.saldofacturainterfaz");				
				return mapping.findForward("paginaError");
			}
			if (saldoInterfaz == ConstantesBDFacturacion.codigoNoEsIgualInfoSaldoInterfaz)
			{
				logger.warn("(AnulacionFacturasAction) No Pasa la validacion de Saldos Facturas en la Interfaz  "+consecutivoAnulacion);
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				request.setAttribute("codigoDescripcionError", "error.facturacion.saldosfacturavalorini");				
				return mapping.findForward("paginaError");
			}
	
			resultadoInteger = objetoValidacionesAnulacion.esFacturaEstadoFacturaCuentasMedicasInvalida(
					con, 
					codigoFactura.intValue(), 
					numeroIdentificacionPaciente,
					usuario.getCodigoInstitucionInt());
			
			if (resultadoInteger.getResultado() == ConstantesBD.codigoNuncaValido)
			{
				UtilidadBD.abortarTransaccion(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, resultadoInteger.getDescripcion(), resultadoInteger.getDescripcion(), false);
			}
		}
		return null;
	}

	public static PersonaBasica cargarPaciente(Connection con, BigDecimal codigoFactura) 
	{
		PersonaBasica paciente= new PersonaBasica();
	    try 
	    {
			paciente.cargar(con, Factura.obtenerCodigoPacienteFactura(con, codigoFactura.intValue()+""));
		} 
	    catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return paciente;
	}
	
	/**
	 * 
	 * @param con
	 * @param usuario
	 * @return
	 */
	public static BigDecimal obtenerSiguienteConsecutivoAnulacionFactura(Connection con,UsuarioBasico usuario)
	{
		BigDecimal consecutivo= BigDecimal.ZERO;
		String nombreConsecutivo=ConstantesBD.nombreConsecutivoAnulacionFactura;
		
		//1. PRIMERO IDENTIFICAMOS SI EL CONSECUTIVO CENTRO DE ATENCION
		if(ValoresPorDefecto.getManejaConsecutivoFacturaPorCentroAtencionBool(usuario.getCodigoInstitucionInt()))
		{
			consecutivo= ConsecutivosCentroAtencion.incrementarConsecutivoXCentroAtencion(usuario.getCodigoCentroAtencion(), nombreConsecutivo, usuario.getCodigoInstitucionInt());
		}
		//2. DE LO CONTRARIO TOMAMOS EL CONSECUTIVO X INSTITUION
		else 
		{
			boolean consecutivoValido=false;
			consecutivo = new BigDecimal( Utilidades.convertirADouble(UtilidadBD.obtenerValorConsecutivoDisponible(nombreConsecutivo, usuario.getCodigoInstitucionInt())));
			int contIntentos=0;
			while(!consecutivoValido && contIntentos<100)
			{
				if(UtilidadesFacturacion.esConsecutvioAsignadoFactura(usuario.getCodigoInstitucionInt(),String.valueOf(consecutivo.longValue())))
				{
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, nombreConsecutivo, usuario.getCodigoInstitucionInt(), consecutivo+"", ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
					consecutivo = new BigDecimal( Utilidades.convertirADouble(UtilidadBD.obtenerValorConsecutivoDisponible(nombreConsecutivo, usuario.getCodigoInstitucionInt())) );
					contIntentos++;
				}
				else
				{
					consecutivoValido=true;
				}
			}
		}
		return consecutivo;
	}
	
}
