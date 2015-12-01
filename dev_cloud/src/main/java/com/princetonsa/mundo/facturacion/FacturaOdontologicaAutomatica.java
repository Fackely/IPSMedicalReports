package com.princetonsa.mundo.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import util.ElementoApResource;
import util.UtilidadValidacion;

import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.dto.facturacion.DtoFacturaAutomaticaOdontologica;
import com.princetonsa.dto.facturacion.DtoParametroFacturaAutomatica;
import com.princetonsa.dto.facturacion.DtoResponsableFacturaOdontologica;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * Generacion de la Factura odontologica Automatica
 * 
 * @author Wilson Rios 
 *
 * Apr 28, 2010 - 8:42:33 AM
 */
public class FacturaOdontologicaAutomatica 
{
	/**
	 * 
	 * Metodo static para generar la factura automatica de odontologia
	 * 
	 * @param dtoParametros : 	Para este proceso es necesario tener el listado de cargos que se quieren facturar,
	 * 							además el usuario y paciente que intervienen en el proceso, el id de la session para
	 * 							hacer lock en la tabla cuentas_proceso_facturacion.
	 * 
	 * @return DtoFacturaAutomaticaOdontologica, 	cuando el proceso es exitoso carga los codigo pk de las facturas generadas
	 * 												de lo contrario carga un array de errores que contiene la descripcion de la falencia
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @throws BDException 
	 * @see
	 */
	public static DtoFacturaAutomaticaOdontologica insertar(DtoParametroFacturaAutomatica dtoParametros, Integer ingreso) throws BDException
	{
		DtoFacturaAutomaticaOdontologica dtoFacturaAutomatica= new DtoFacturaAutomaticaOdontologica();
		ArrayList<DtoResponsableFacturaOdontologica> listaResponsablesConSolicitudes= new ArrayList<DtoResponsableFacturaOdontologica>();
		dtoFacturaAutomatica.setErroresFactura(validarConsecutivosConcurrencia(dtoParametros));
		
		//CANCELAMOS EL PROCESO X ERROR
		if(dtoFacturaAutomatica.getErroresFactura()!=null)
		{
			return dtoFacturaAutomatica;
		}
		
		//AHORA CARGAMOS LOS RESPONSABLES DE LA CUENTA
		listaResponsablesConSolicitudes= FacturaOdontologica.cargarResponsables(new BigDecimal(dtoParametros.getPaciente().getCodigoCuenta()) , dtoParametros.getUsuario().getCodigoCentroAtencion(), dtoParametros.getUsuario().getCodigoInstitucionInt(), dtoParametros.getListaCargosPk());
		dtoFacturaAutomatica.setErroresFactura(validarCalculoTotales(dtoParametros, listaResponsablesConSolicitudes));
		
		//CANCELAMOS EL PROCESO X ERROR
		if(dtoFacturaAutomatica.getErroresFactura()!=null)
		{
			return dtoFacturaAutomatica;
		}
		
		//SI SALE BIEN LAS VALIDACIONES ENTONCES GENERAMOS LA FACTURA
		generarFacturaAutomatica(dtoParametros, listaResponsablesConSolicitudes, dtoFacturaAutomatica, ingreso);
		
		return dtoFacturaAutomatica;
	}

	/**
	 * 
	 * Metodo que realiza validaciones de los consecutivos y la concurrencia lock
	 * @param dtoParametros
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static ArrayList<ElementoApResource> validarConsecutivosConcurrencia(DtoParametroFacturaAutomatica dtoParametros) 
	{
		ValidacionesFacturaOdontologica validaciones= new ValidacionesFacturaOdontologica();
		
		//1. VALIDAMOS LOS CONSECUTIVOS DISPONIBLES
		if(!validaciones.existeConsecutivoFactura(dtoParametros.getUsuario().getCodigoInstitucionInt(), dtoParametros.getUsuario().getCodigoCentroAtencion()))
		{
			return validaciones.getErroresFactura();
		}
		
		//2. VALIDAMOS LOS RANGOS AUTORIZADOS DE FACTURACION
		if(!validaciones.esConsecutivoEnRangoAutorizado(dtoParametros.getUsuario().getCodigoInstitucionInt(), dtoParametros.getUsuario().getCodigoCentroAtencion()))
		{
			return validaciones.getErroresFactura();
		}
		
		//3. MANEJO DE CONCURRENCIA
		if(UtilidadValidacion.estaEnProcesofacturacion( dtoParametros.getPaciente().getCodigoPersona(), dtoParametros.getIdSession()))
		{	
			ArrayList<ElementoApResource> error= new ArrayList<ElementoApResource>();
			error.add(new ElementoApResource("error.facturacion.cuentaEnProcesoFact"));
			return error;
		}
			
		return null;
	}
	
	/**
	 * 
	 * Metodo para validar pooles o liquidacion de honorarios, vigencia contratos, topes, valor a facturar >0,  validar saldo anticipos....
	 * @param dtoParametros
	 * @param listaResponsablesConSolicitudes
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	private static ArrayList<ElementoApResource> validarCalculoTotales(	DtoParametroFacturaAutomatica dtoParametros,
																		ArrayList<DtoResponsableFacturaOdontologica> listaResponsablesConSolicitudes) 
	{
		//1. VALIDAMOS LA INFORMACION  POOLES Y LIQUIDACION DE HONORARIOS
		ValidacionesFacturaOdontologica validaciones = new ValidacionesFacturaOdontologica();
		validaciones.validacionesPoolesYLiquidacionHonorarios(listaResponsablesConSolicitudes, dtoParametros.getUsuario().getCodigoInstitucionInt());
		
		if(validaciones.getErroresFactura().size()>0)
		{
			return validaciones.getErroresFactura();
		}
		
		//2. validamos la vigencia de los contratos dependiendo del parametro general
		//si el parametro general no esta activo entonces se mostrara en los warnings los convenios
		validaciones.esContratosVigentesYTopeValido(new BigDecimal(dtoParametros.getPaciente().getCodigoCuenta()), conversionContratosVector(listaResponsablesConSolicitudes), dtoParametros.getUsuario().getCodigoInstitucionInt());
		
		if(validaciones.getErroresFactura().size()>0)
		{
			return validaciones.getErroresFactura();
		}

		//3. VALIDAMOS QUE EL TOTAL A FACTURAR SEA MAYOR QUE CERO
		validaciones.esValorCargoTotalMayorCero(listaResponsablesConSolicitudes);
		
		if(validaciones.getErroresFactura().size()>0)
		{
			return validaciones.getErroresFactura();
		}
		
		//4. REMOVEMOS LOS RESPONSABLES CON VALOR MENOR IGUAL A CERO PARA NO FACTURARLOS
		Iterator<DtoResponsableFacturaOdontologica> iterador= listaResponsablesConSolicitudes.iterator();
		
		while(iterador.hasNext())
		{
			if(iterador.next().getValorTotalNetoCargosEstadoCargado().doubleValue()<=0)
			{
				iterador.remove();
			}
		}
		
		//5. VERIFICACION DEL ANTICIPO O SALDO 
		validaciones.esValorAnticipoConvenioOSaldoPacienteCorrecto(dtoParametros.getPaciente().getCodigoPersona(), listaResponsablesConSolicitudes);
		
		if(validaciones.getErroresFactura().size()>0)
		{
			return validaciones.getErroresFactura();
		}
		
		//6. REALIZAMOS EL BLOQUEO DE LAS CUENTAS EN PROCESO DE FACTURACION
		if(!UtilidadValidacion.estaEnProcesofacturacion(dtoParametros.getPaciente().getCodigoPersona(), ""))
		{
			if(!FacturaOdontologica.empezarProcesoFacturacion(dtoParametros.getPaciente().getCodigoCuenta(), dtoParametros.getUsuario().getLoginUsuario(), dtoParametros.getIdSession()))
			{
				ArrayList<ElementoApResource> error= new ArrayList<ElementoApResource>();
				error.add(new ElementoApResource("errors.problemasBd"));
				return error;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * Metodo para que reutiliza in codigo antiguo que maneja objeto sincrono vector para obtener los contratos
	 * @param listaResponsablesConSolicitudes
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	@SuppressWarnings("unchecked")
	private static Vector conversionContratosVector(ArrayList<DtoResponsableFacturaOdontologica> listaResponsablesConSolicitudes) 
	{
		Vector contratos= new Vector();
		if(listaResponsablesConSolicitudes!=null)
		{	
			for(DtoResponsableFacturaOdontologica dto: listaResponsablesConSolicitudes)
			{
				contratos.add(dto.getContrato().getCodigo());
			}
		}	
		return contratos;
	}
	
	/**
	 * 
	 * Metodo para generar la factura automatica de odontologia
	 * @param dtoParametros
	 * @param listaResponsablesConSolicitudes
	 * @param dtoFacturaAutomatica
	 * 
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @param i 
	 * @see
	 */
	private static boolean generarFacturaAutomatica(DtoParametroFacturaAutomatica dtoParametros,
													ArrayList<DtoResponsableFacturaOdontologica> listaResponsablesConSolicitudes,
													DtoFacturaAutomaticaOdontologica dtoFacturaAutomatica, Integer ingreso) 
	{
		ValidacionesFacturaOdontologica validaciones= new ValidacionesFacturaOdontologica();
		
		//1. LLENAMOS LOS DTOS DE FACTURACION 
		ArrayList<DtoFactura> listaFacturas= FacturaOdontologica.proponerEncabezadoFactura(dtoParametros.getCon(), listaResponsablesConSolicitudes, dtoParametros.getPaciente().getCodigoPersona(), new BigDecimal(dtoParametros.getPaciente().getCodigoCuenta()), dtoParametros.getUsuario(), dtoParametros.getCodigoInstitucion());
		ArrayList<Double> listaConsecutivos= new ArrayList<Double>();
		
		//2 LLENAMOS LOS CONSECUTIVOS DE FACTURA
		for(DtoFactura dtoFactura: listaFacturas)
		{
			int tipoFuenteDatos = validaciones.obtenerTipoFuenteDatosFactura(dtoParametros.getUsuario());
			
			dtoFactura.setConsecutivoFactura(validaciones.obtenerSiguienteConsecutivoFactura(dtoParametros.getCon() , dtoParametros.getUsuario(), tipoFuenteDatos).doubleValue());
			
			validaciones.obtenerDatosParametrizacionParaFactura (dtoFactura.getHistoricoEncabezado(), tipoFuenteDatos, dtoParametros.getUsuario().getCodigoCentroAtencion());
			
			listaConsecutivos.add(dtoFactura.getConsecutivoFactura());
			
			//3.1 VALIDAMOS LOS RANGOS AUTORIZADOS DE FACTURACION
			if(!validaciones.esConsecutivoEnRangoAutorizadoDadoValor(dtoParametros.getUsuario().getCodigoInstitucionInt(), dtoParametros.getUsuario().getCodigoCentroAtencion(), new BigDecimal(dtoFactura.getConsecutivoFactura())))
			{
				ArrayList<ElementoApResource> errores;
				if(validaciones.getErroresFactura()==null)
				{
					errores=validaciones.getErroresFactura();
				}
				else
				{
					errores=new ArrayList<ElementoApResource>();
				}
				dtoFacturaAutomatica.setErroresFactura(errores);
				cancelarFacturaAutomatica(dtoParametros.getCon(), dtoParametros, dtoFacturaAutomatica, listaConsecutivos);
				return false;
			}
		}
		
		//3 INSERTAMOS LA FACTURA
		
		if(!FacturaOdontologica.insertarFacturas(dtoParametros.getCon(), listaFacturas))
		{
			return cancelarFacturaAutomatica(dtoParametros.getCon(), dtoParametros,	dtoFacturaAutomatica, listaConsecutivos);
		}
		
//		dtoParametros.setCon(UtilidadBD.abrirConexion());
//		UtilidadBD.iniciarTransaccion(dtoParametros.getCon());
		
		//ACTUALIZAR EL ACUMULADO DEL CONTRATO
		if(!FacturaOdontologica.actualizarAcumuladoContratos(dtoParametros.getCon(), listaFacturas))
		{
			return cancelarFacturaAutomatica(dtoParametros.getCon(), dtoParametros,	dtoFacturaAutomatica, listaConsecutivos);
		}
		
		//5.ACTUALIZAMOS EL ESTADO DE LA CUENTA
		if(!FacturaOdontologica.actualizarEstadoCuenta(dtoParametros.getCon(), new BigDecimal(dtoParametros.getPaciente().getCodigoCuenta())))
		{
			return cancelarFacturaAutomatica(dtoParametros.getCon(), dtoParametros, dtoFacturaAutomatica, listaConsecutivos);
		}
		
		//6.ACTUALIZAMOS A FACTURADOS EL ESTADO DE LOS CARGOS
		if(!Factura.actualizarEstadoFacturadoDetalleCargo(dtoParametros.getCon(), listaFacturas))
		{
			return cancelarFacturaAutomatica(dtoParametros.getCon(), dtoParametros,	dtoFacturaAutomatica, listaConsecutivos);
		}
		
		//7.ACTUALIZAMOS LOS ABONOS
		if(!FacturaOdontologica.insertarAbonos(dtoParametros.getCon(), listaFacturas, ingreso))
		{
			return cancelarFacturaAutomatica(dtoParametros.getCon(), dtoParametros,	dtoFacturaAutomatica, listaConsecutivos);
		}
		
		//8.ACTUALIZACION DE ANTICIPOS
		if(!FacturaOdontologica.insertarAnticipos(dtoParametros.getCon(), listaFacturas))
		{
			return cancelarFacturaAutomatica(dtoParametros.getCon(), dtoParametros, dtoFacturaAutomatica, listaConsecutivos);
		}
		
		//9.INSERTAMOS LOS HISTORICOS
		if(!Factura.insertarHistoricosSubCuentas(dtoParametros.getCon(), listaFacturas))
		{
			return cancelarFacturaAutomatica(dtoParametros.getCon(), dtoParametros,	dtoFacturaAutomatica, listaConsecutivos);
		}
		
		//10. FINALIZAMOS LOS CONSECUTIVOS DE INSTITUCION
		ValidacionesFacturaOdontologica.finalizarConsecutivos(dtoParametros.getCon(), listaConsecutivos, dtoParametros.getUsuario().getCodigoInstitucionInt());
		
		//11. eliminamos la cuenta del proceso de facturacion
		if(!Factura.eliminarCuentaProcesoFacturacion(dtoParametros.getCon(), new BigDecimal(dtoParametros.getPaciente().getCodigoCuenta()), dtoParametros.getIdSession()))
		{
			return cancelarFacturaAutomatica(dtoParametros.getCon(), dtoParametros,	dtoFacturaAutomatica, listaConsecutivos);
		}
		
		//cargamos los codigos de las facturas insertadas
		for(DtoFactura dtoFactura: listaFacturas)
		{
			dtoFacturaAutomatica.addFactura(new BigDecimal(dtoFactura.getCodigo()));
		}
		
		return true;
	}

	/**
	 * Metodo para cancelar el proceso de facturacion
	 * @param dtoParametros
	 * @param dtoFacturaAutomatica
	 * @param listaConsecutivos
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see      
	 */
	private static boolean cancelarFacturaAutomatica(Connection con, DtoParametroFacturaAutomatica dtoParametros,
														DtoFacturaAutomaticaOdontologica dtoFacturaAutomatica,
														ArrayList<Double> listaConsecutivos) 
	{
		ValidacionesFacturaOdontologica.liberarConsecutivos(con, listaConsecutivos, dtoParametros.getUsuario().getCodigoInstitucionInt());
		Factura fact= new Factura();
		
		//debemos usar una conexion diferente para cancelar la cuenta_proceso_factura 
		fact.cancelarProcesoFacturacion(con, dtoParametros.getPaciente().getCodigoCuenta(), dtoParametros.getIdSession());
		
		dtoFacturaAutomatica.addErroresFactura("error.facturacion.noGuardoFactura");
		return false;
	}
	
	
}
