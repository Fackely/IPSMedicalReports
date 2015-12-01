/*
 * @(#)AnulacionFacturasAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.AnulacionFacturasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.facturacion.DtoConsultaFacturasAnuladas;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.facturacion.AnulacionFacturas;
import com.princetonsa.mundo.facturacion.Factura;
import com.princetonsa.mundo.facturacion.ValidacionesAnulacionFacturas;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 *   Action, controla todas las opciones dentro del registro y consulta de
 *   anulacion de facturas, incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @version 1.0, Agosto 8, 2005
 * @author wrios
 */
public class AnulacionFacturasAction extends Action
{
    /**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(AnulacionFacturasAction.class);
	
	/**
	 * Parametros de generacion consult ha mostrar en la impresion
	 */
	private String parametrosGeneracionConsulta;
	
	/**
	 * Metodo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response ) throws Exception
	{
		Connection con = null;
		try {
			if (response==null); //Para evitar que salga el warning
			if(form instanceof AnulacionFacturasForm)
			{
				con= UtilidadBD.abrirConexion();	
				AnulacionFacturasForm forma =(AnulacionFacturasForm)form;
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				
				logger.info("\n\n****************************************************************************************************" +
						"\nESTADO ANULACION FACTURA: "+forma.getEstado()+
						"\ncerrar cuenta->"+forma.getCerrarCuenta()+
						"\n************************************************************************************************************\n\n");
				
				ActionForward forward= validacionesConsecutivosDisponibles(mapping, request, con, usuario);
				if(forward!=null)
				{
					this.accionEmpezar(forma,mapping, con, usuario);
					return forward;
				}
			    if(forma.getEstado() == null)
				{
					return accionError(mapping, request, con, forma);
				}
				else if (forma.getEstado().equals("empezar"))
				{
				    return this.accionEmpezar(forma,mapping, con, usuario);
				}
				else if (forma.getEstado().equals("cargarListadoFacturasMismoConsecutivo"))
				{
					return accionCargarListadoFacturasMismoConsecutivo(mapping, con, forma, usuario, request);
				}
				else if(forma.getEstado().equals("cargarFacturaSeleccionadaListado"))
				{
					return accionCargarFacturaSeleccionadaListado(mapping, request, con, forma,usuario);
				}
				else if (forma.getEstado().equals("anular"))
				{
				    return this.accionAnularTransaccional(forma, usuario, mapping, request, con);
				}
				else if(forma.getEstado().equals("detalleAnulacion"))
				{
				    return this.accionDetalleAnulacion(forma, usuario, mapping, con);
				}
				else if(forma.getEstado().equals("empezarConsultar"))
				{
				    return this.accionEmpezarConsultar(forma, mapping, con);
				}
				else if(forma.getEstado().equals("consultarAnulaciones"))
				{
				    return this.accionListarAnulaciones(forma, mapping, con, usuario);
				}
				else if(forma.getEstado().equals("ordenar"))
				{
					return this.accionOrdenar(forma,mapping,request,con);
				}
				else if(forma.getEstado().equals("volverAListar"))
				{
				    return this.accionVolverAListarAnulaciones(forma, mapping, con, usuario); 
				}
				else if(forma.getEstado().equals("generarReporteListado"))
	            {              
	                return accionGenerarReporteListado(mapping, request, con, forma, usuario);
	            }
				else if(forma.getEstado().equals("generarReporteDetalle"))
	            {                
	                return accionGenerarReporteDetalle(mapping, request, con, forma,	usuario);
	            }
				return this.accionError(mapping, request, con, forma);
			}			
			return null;
		} catch (Exception e) {
			logger.error("Error en la anulación de la factura. ");
			e.printStackTrace();
			return null;
		}finally{
			UtilidadBD.closeConnection(con);
		}	
	}

	/**
	 * @param mapping
	 * @param request
	 * @param con
	 * @param usuario
	 */
	private ActionForward validacionesConsecutivosDisponibles(	ActionMapping mapping,
																HttpServletRequest request, 
																Connection con, 
																UsuarioBasico usuario) 
	{
		ValidacionesAnulacionFacturas validaciones= new ValidacionesAnulacionFacturas();
		ActionErrors errores= validaciones.validarMotivosAnulacion(usuario);
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
		errores= validaciones.existeConsecutivoAnulacionFactura(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
		return null;
	}

	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * empezar.
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionEmpezar( 	AnulacionFacturasForm forma, 
											ActionMapping mapping, 
											Connection con, 
											UsuarioBasico usuario)
	{
		//Limpiamos lo que venga del form
		forma.reset(true);
		forma.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");		
	}
	
	/**
	 * Si existen n facturas con el mismo consecutivo entonces debemos mostrar un popup para que sea
	 * seleccionada
	 * @param mapping
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	private ActionForward accionCargarListadoFacturasMismoConsecutivo(	ActionMapping mapping,
																		Connection con, 
																		AnulacionFacturasForm forma, 
																		UsuarioBasico usuario,
																		HttpServletRequest request) 
	{
		forma.reset(false);
		forma.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		forma.setDescEntidadSubcontratada("");
		
		//1. DEBEMOS CARGAR EL LISTADO DE FACTURAS QUE POSIBLEMENTE TENGA EL MISMO CONSECUTIVO
		forma.setFacturasMismoConsecutivo(UtilidadesFacturacion.consultarFacturasMismoConsecutivo(new BigDecimal(forma.getConsecutivoFactura().trim()),usuario.getCodigoInstitucionInt()));
		if(forma.getFacturasMismoConsecutivo().size()>1)
		{
			util.UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}

		//de lo contrario solo existe un consecutivo entonces lo enviamos a cargar
		forma.setIndiceConsecutivoCargar(0);
		return accionCargarFacturaSeleccionadaListado(mapping, request, con, forma, usuario);
	}
	
	/**
	 * @param mapping
	 * @param request
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCargarFacturaSeleccionadaListado(	ActionMapping mapping,
																	HttpServletRequest request, 
																	Connection con,
																	AnulacionFacturasForm forma, 
																	UsuarioBasico usuario) 
	{
		forma.setCodigoFactura(forma.getFacturasMismoConsecutivo().get(forma.getIndiceConsecutivoCargar()).getCodigoFactura());
		forma.setEsFacturaOdontologica(ValidacionesAnulacionFacturas.esFacturaIngresoOdontologico(forma.getCodigoFactura()));
		
		ActionErrors errores = ValidacionesAnulacionFacturas.validacionesAnulacionBasicas(forma.getCodigoFactura(), forma.getConsecutivoFactura(), usuario);
		if(!errores.isEmpty())
    	{
			saveErrors(request, errores);
		    UtilidadBD.closeConnection(con);
		    return mapping.findForward("principal");
    	}
		
		return this.accionResultadoBusqueda(forma, usuario, mapping,request, con);
	}
	
	/**
	 * Metodo que realiza el resultado de la busqueda
	 * @param forma
	 * @param user
	 * @param mapping
	 * @param request
	 * @param con
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionResultadoBusqueda (	AnulacionFacturasForm forma, 
													UsuarioBasico user,					
													ActionMapping mapping, 
													HttpServletRequest request,
													Connection con) 
	{
		forma.setMostrarAdvertenciaIngresos(false);
		forma.setCol(Factura.busquedaPorCodigo(con, forma.getCodigoFactura().intValue(), ""));
	        	
    	//mostramos la advertencia de los ingresos
    	Iterator it=forma.getCol().iterator();
    	while(it.hasNext())
		{
    		HashMap factura=(HashMap)it.next();
    		logger.info("codigopaciente->"+factura.get("codigopaciente").toString());
    		forma.setMostrarAdvertenciaIngresos(IngresoGeneral.existeIngresoAbierto(con, factura.get("codigopaciente").toString(), Factura.obtenerIdIngresoFactura(con, factura.get("codigo")+"")));
    		forma.setDescEntidadSubcontratada(factura.get("descentidadsubcontratada")+"");
		}
		
	    forma.setMostrarInformacion(true);
	    util.UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	
	/**
	 * Metodo centralizado y transaccional para la actualizacion de todos los datos concernientes a la 
	 * anulacion de facturas
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionAnularTransaccional(	AnulacionFacturasForm forma, 
        												UsuarioBasico usuario,					
														ActionMapping mapping, 
														HttpServletRequest request,
														Connection con) throws SQLException
	{
		logger.info("forma.getCerrarCuenta() entrada metodo-------------------->"+forma.getCerrarCuenta());
		
		//OBJETOS 
		ValidacionesAnulacionFacturas objetoValidacionesAnulacion= new ValidacionesAnulacionFacturas();
	    AnulacionFacturas mundoAnulacionFacturas= new AnulacionFacturas();
	    Cuenta objetoCuenta = new Cuenta();
	    PersonaBasica paciente= ValidacionesAnulacionFacturas.cargarPaciente(con, forma.getCodigoFactura());
	    
	    //1.INICIAMOS LA TRANSACCION
		UtilidadBD.iniciarTransaccion(con);
	    
	    //2. VALIDACIONES
		if(AnulacionFacturas.estaFacturaAnulada(con, forma.getCodigoFactura().intValue()))
	    {
	    	logger.warn("(AnulacionFacturasAction) la factura ya esta anulada "+forma.getCodigoFactura().intValue());
			UtilidadBD.closeConnection(con);
			request.setAttribute("codigoDescripcionError", "error.factura.yaEstaAnulada");				
			return mapping.findForward("paginaError");
	    }
		
		//3. HACEMOS VALIDACIONES INTERFAZ CONTABLE
		ActionForward forward= ValidacionesAnulacionFacturas.validacionesInterfaz(con, forma.getConsecutivoFactura(), forma.getCodigoFactura(), usuario, request, mapping, logger, forma.getConsecutivoAnulacion(), paciente.getNumeroIdentificacionPersona());
		if(forward!=null)
		{
			//si es diferente de null entonces ya abortaron y cerraron la transaccion
			return forward;
		}
		
	    //4. OBTENEMOS EL CONSECUTIVO DE ANULACION
	    String consecutivoAInsertar= ValidacionesAnulacionFacturas.obtenerSiguienteConsecutivoAnulacionFactura(con, usuario)+"";

	    //5. CARGAMOS LA CUENTA QUE SE FACTURO
	    objetoCuenta.cargarCuenta(con, ""+objetoValidacionesAnulacion.getCodigoCuentaDadaFactura(con, forma.getCodigoFactura().intValue()));
	    
	    //6. REVERTIMOS LOS ABONOS EN CASO DE QUE EXISTAN
	    if(!AnulacionFacturas.revertirAbonos(con, forma.getCodigoFactura(), usuario.getCodigoCentroAtencion()))
	    {
	    	String descripcionError="error en el actualizar los abonos (AnulacionFacturasAction)";
	    	String error = "error.facturacion.movAbonos";
    		return abortarFacturacion(mapping, request, con, descripcionError, error, usuario,consecutivoAInsertar,true);	    	
	    }
	    
	    //7. REVERTIMOS LOS ANTICIPOS
	    if(!AnulacionFacturas.revertirAnticipos(con, forma.getCodigoFactura()))
	    {
	    	String descripcionError="error en el actualizar los anticipos (AnulacionFacturasAction)";
	    	String error = "error.facturacion.movAnticipos";
    		return abortarFacturacion(mapping, request, con, descripcionError, error, usuario,consecutivoAInsertar,true);	
	    	
	    }
	    
	    //8.ACTUALIZAMOS LOS TOPES
	    if(!forma.isEsFacturaOdontologica())
	    {	
	    	mundoAnulacionFacturas.actualizacionTopesTransaccional(forma.getCodigoFactura().intValue(), forma.getConsecutivoFactura(), ConstantesBD.continuarTransaccion, con, objetoValidacionesAnulacion, usuario, consecutivoAInsertar);
	    }	
	    
	    //9. SE ACTUALIZA LA INFORMACION DE LA CUENTA Y EL INGRESO
	    if(!forma.isEsFacturaOdontologica())
	    {	
	    	String error=AnulacionFacturas.actualizarEstadoCuentaIngreso(con, forma.getCodigoFactura(), forma.getConsecutivoFactura(), forma.getCerrarCuenta(), forma.getMostrarAdvertenciaIngresos(), objetoCuenta.getCodigoIngreso(), usuario);
	    	if(!UtilidadTexto.isEmpty(error))
	    	{
	    		String descripcionError="error en el actualizar los estados de ingreso/cuenta (AnulacionFacturasAction)";
	    		return abortarFacturacion(mapping, request, con, descripcionError, error, usuario,consecutivoAInsertar,true);
	    	}
	    }	
	    
	    //10. INSERTO LA ANULACION 
	    if(!AnulacionFacturas.insertarAnulacionFacturaTransaccional(con, forma.getCodigoFactura().intValue(), consecutivoAInsertar, forma.getMotivoAnulacion(), forma.getObservaciones(), usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt(), "", ConstantesBD.continuarTransaccion))
        {
	    	String descripcionError="error en el actualizar la tabla de anulaciones_facturas (AnulacionFacturasAction)";
	    	String error = "error.factura.yaEstaAnulada";
    		return abortarFacturacion(mapping, request, con, descripcionError, error, usuario,consecutivoAInsertar,true);
    	    
        }
	   
	    //12. VALIDACIONES BASICAS 
	    ActionErrors errores=ValidacionesAnulacionFacturas.validacionesAnulacionBasicas(forma.getCodigoFactura(), forma.getConsecutivoFactura(), usuario);
	    if(!errores.isEmpty())
	    {
	    	 saveErrors(request, errores);
	    	 
	    	 UtilidadBD.abortarTransaccion(con);
	 		 UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoAnulacionFactura, usuario.getCodigoInstitucionInt(),
	 				consecutivoAInsertar, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
	    	 
	    	 UtilidadBD.closeConnection(con);	   	        
	    	 return mapping.findForward("paginaErroresActionErrors");    	 
	    }
	    
	    //13. VALIDACIONES CONCURRENCIA BLOQUEO
	    ArrayList filtro=new ArrayList();
        filtro.add(forma.getCodigoFactura()+"");
	    DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).bloquearRegistroActualizacion(con,BloqueosConcurrencia.bloqueoFacturaDeterminada,filtro);
	    
	    //14. ACTUALIZO EL ESTADO DE LA FACTURA
	    if(!Factura.actualizarEstadosFactura(con, ConstantesBD.codigoEstadoFacturacionAnulada, ConstantesBD.codigoEstadoFacturacionPacienteAnulada, forma.getCodigoFactura().intValue()))
	    {
	    	String descripcionError="error en el actualizar los estados de factura y paciente de anulacion de facturas (AnulacionFacturasAction)";
	    	String error = "error.facturacion.actualizacionEstados";
    		return abortarFacturacion(mapping, request, con, descripcionError, error, usuario,consecutivoAInsertar,true);
    	    
	    }
	    
	    //15. ACTUALIZAMOS EL VALOR ACUMULADO DEL CONTRATO 
	    if(!forma.isEsFacturaOdontologica())
	    {	
	    	String error=AnulacionFacturas.actualizarValorAcumuladoContrato(con, forma.getCodigoFactura());
	    	if(!UtilidadTexto.isEmpty(error))
	    	{
	    		String descripcionError="error en el actualizar valor acumulado contrato (AnulacionFacturasAction)";
		    	return abortarFacturacion(mapping, request, con, descripcionError, error, usuario,consecutivoAInsertar,true);
		    	
	    	}
	    }
	    
	    //16. ACTUALIZAMOS EL CENTINELA DE FACTURADO DE LOS DETALLES DE CARGO
	    if(!AnulacionFacturas.actualizarEstadoDetalleCargo(con, forma.getCodigoFactura()+""))
	    {
	    	logger.info("no actualizo los detalles del cargo a no facturados");
	    	String descripcionError="error en el actualizar la tabla de anulaciones_facturas (AnulacionFacturasAction)";
	    	String error = "error.factura.yaEstaAnulada";
    		return abortarFacturacion(mapping, request, con, descripcionError, error, usuario,consecutivoAInsertar,true);
	    	
	    }
	    
	    //17. ACTUALIZAMOS LA SUBCUENTA A NO FACTURADA
	    if(!Cargos.actualizarEstadoFacturadoSubCuenta(con, objetoValidacionesAnulacion.getSubCuentaDadaFactura(con, forma.getCodigoFactura().intValue()) , ConstantesBD.acronimoNo))
		{
	    	logger.info("no actualizo los el estado de la sub cuenta de la factura"+forma.getCodigoFactura());
	    	String descripcionError="error en el actualizar la tabla de anulaciones_facturas (AnulacionFacturasAction)";
	    	String error = "error.factura.yaEstaAnulada";
    		return abortarFacturacion(mapping, request, con, descripcionError, error, usuario,consecutivoAInsertar,true);
	    	
		}
		
	    //18. ACTUALIZAMOS LAS SUBCUENTAS DE ESE INGRESO DONDE EL TOTAL A FACTURADO FUE CERO, ES DECIR QUEDARON SIN FACTURA
	    if(!Cargos.actualizarEstadoFacturadoSubCuentasTotalCero(con, objetoCuenta.getCodigoIngreso()))
	    {
	    	logger.info("no actualizo los el estado de la sub cuenta de la facturas que tienen total cero "+forma.getCodigoFactura());
	    	String descripcionError="error en el actualizar la tabla de anulaciones_facturas (AnulacionFacturasAction)";
	    	String error = "error.factura.yaEstaAnulada";
    		return abortarFacturacion(mapping, request, con, descripcionError, error, usuario,consecutivoAInsertar,true);
	    }
	    	
	    //19. INSERTAMOS LA INFORMACION DE LA INTERFAZ CONTABLE
	    String error=AnulacionFacturas.insertarInterfazContable(con, forma.getCodigoFactura(), usuario, objetoCuenta.getCodigoIngreso(), paciente.getNumeroIdentificacionPersona());
	    if(!error.isEmpty())
	    {	    	    	
	    	return abortarFacturacion(mapping, request, con, error, error, usuario,consecutivoAInsertar,false);	    	
	    }
	    
	    //20. INSERTAMOS LA INFORMACION DE LA INTERFAZ TESORERIA
	    error= AnulacionFacturas.insertarInterfazTesoreria(con, forma.getCodigoFactura(), usuario, paciente);
	    if(!error.isEmpty())
		{	    	
	    	return abortarFacturacion(mapping, request, con, error, error, usuario,consecutivoAInsertar,false);
		}	
	    
	    //11. ACTUALIZAMOS EL CONSECUTIVO DISPONIBLE 
	    if(!UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoAnulacionFactura, usuario.getCodigoInstitucionInt(),
	    		consecutivoAInsertar, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi))
        {
		   String descripcionError="error en el actualizar el consecutivo de anulacion de facturas (AnulacionFacturasAction)";
	       String errorConsecutivo = "error.facturacion.registroConsecutivoAnul";
    	   return abortarFacturacion(mapping, request, con, descripcionError, errorConsecutivo, usuario,consecutivoAInsertar,true);
    	    
        }
	    
	    UtilidadBD.finalizarTransaccion(con);
	    //UtilidadBD.abortarTransaccion(con);
	    logger.info("INSERTADO 100%");
	    forma.setEstado("detalleAnulacion");
	    this.setObservable(con, objetoValidacionesAnulacion.getCodigoCuentaPrincipalDadaFactura(con, forma.getCodigoFactura().intValue()));
	    return accionDetalleAnulacion(forma, usuario, mapping, con);
	}

	/**
	 * 
	 * Este Método se encarga de finalizar el consecutivo, si se debe abortar la transacción
	 * 
	 * @param ActionMapping mapping,HttpServletRequest request, 
	 *		  Connection con, String descripcionError, String error,
	 *		  UsuarioBasico usuario, String consecutivoAInsertar
	 * @return 	ActionForward
	 * @author, Angela Maria Aguirre
	 * 
	 */
	private ActionForward abortarFacturacion(ActionMapping mapping,HttpServletRequest request, 
			Connection con, String descripcionError, String error, UsuarioBasico usuario,
			String consecutivoAInsertar, boolean valorBoolean) {
		
		UtilidadBD.abortarTransaccion(con);
		UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoAnulacionFactura, usuario.getCodigoInstitucionInt(),
				consecutivoAInsertar, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
		return ComunAction.accionSalirCasoError(mapping, request, con, logger, descripcionError, error, valorBoolean);
		
	}
	
	/**
	 * @param mapping
	 * @param request
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGenerarReporteDetalle(	ActionMapping mapping,
														HttpServletRequest request, 
														Connection con,
														AnulacionFacturasForm forma, 
														UsuarioBasico usuario) 
	{
		this.generarReporte("detalle", con, forma, usuario, request);                
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleAnulacion");
	}

	/**
	 * @param mapping
	 * @param request
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGenerarReporteListado(	ActionMapping mapping,
														HttpServletRequest request, 
														Connection con,
														AnulacionFacturasForm forma, 
														UsuarioBasico usuario) 
	{
		this.generarReporte("listado", con, forma, usuario, request);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaConsulta");
	}
	
	
	/**
	 * Accion para cargar el detalle de una anulacion
	 * @param forma
	 * @param user
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionDetalleAnulacion (	AnulacionFacturasForm forma, 
													UsuarioBasico user,					
													ActionMapping mapping, 
													Connection con)
	{
	    forma.setCol(Factura.busquedaPorCodigo(con,forma.getCodigoFactura().intValue(), ""));
	    AnulacionFacturas objetoAnulacion= new AnulacionFacturas();
	    objetoAnulacion.detalleAnulacion(con, forma.getCodigoFactura().intValue());
	    forma.reset(false);
	    this.llenarForm(forma, objetoAnulacion);
	    util.UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleAnulacion");
	}
	
	/**
	 * metodo que llena los atributos del objeto anulacion en el bean form
	 * @param forma
	 * @param mundoAnulacion
	 */
	private void llenarForm(AnulacionFacturasForm forma, AnulacionFacturas mundoAnulacion)
	{
	    forma.setCodigoFactura(new BigDecimal(mundoAnulacion.getCodigoAnulacion()));
	    forma.setConsecutivoAnulacion(mundoAnulacion.getConsecutivoAnulacion());
	    forma.setHoraAnulacion(mundoAnulacion.getHoraAnulacion());
	    forma.setFechaAnulacion(mundoAnulacion.getFechaAnulacion());
	    forma.setLoginUsuario(mundoAnulacion.getLoginUsuario());
	    forma.setCodigoEstadoCuenta(mundoAnulacion.getEstadoCuenta().getCodigo());
	    forma.setNombreEstadoCuenta(mundoAnulacion.getEstadoCuenta().getNombre());
	    forma.setCodigoEstadoCuentaMadre(mundoAnulacion.getEstadoCuentaMadre().getCodigo());
	    forma.setNombreEstadoCuentaMadre(mundoAnulacion.getEstadoCuentaMadre().getNombre());
	    forma.setMotivoAnulacion(mundoAnulacion.getMotivoAnulacion().getCodigo());
	    forma.setDescripcionMotivoAnulacion(mundoAnulacion.getMotivoAnulacion().getDescripcion());
	    forma.setObservaciones(mundoAnulacion.getObservaciones());
	    forma.setIngresoPaciente(mundoAnulacion.getIngresoPaciente());
	    
	}
	
	/**
	 * Este metodo especifica las acciones a realizar en el estado 
	 * empezarConsultar.
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionEmpezarConsultar( 	AnulacionFacturasForm forma, 
													ActionMapping mapping, 
													Connection con)
	{
		//Limpiamos lo que venga del form
		forma.reset(true);
		util.UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaConsulta");		
	}
	
	
	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * listar anulaciones
	 */
	private ActionForward accionListarAnulaciones	(	AnulacionFacturasForm forma, 
														ActionMapping mapping,
														Connection con,
														UsuarioBasico usuario)  
	{
		AnulacionFacturas mundoAnulacion= new AnulacionFacturas();
		logger.info("\n empresa institucion->"+forma.getEmpresaInstitucion());
		parametrosGeneracionConsulta="Parametros de la generacion de la Consulta: ";
		
		if(!forma.getFechaInicialAnulacion().equals(""))
		{	
			parametrosGeneracionConsulta+=" Fecha Inicial Anulacion: "+forma.getFechaInicialAnulacion();
		}
		if(!forma.getFechaFinalAnulacion().equals(""))
		{	
			parametrosGeneracionConsulta+=" Fecha Final Anulacion: "+forma.getFechaFinalAnulacion();
		}
		if(!forma.getConsecutivoFactura().trim().equals(""))
		{	
		    forma.setEsBusquedaXConsecutivoFactura(true);
		    parametrosGeneracionConsulta+=" No Factura: "+forma.getConsecutivoFactura();
		}    
		if(!forma.getConsecutivoAnulacion().trim().equals(""))
		{	
		    forma.setEsBusquedaXConsecutivoAnulacion(true);
		    parametrosGeneracionConsulta+=" No. Anulacion: "+forma.getConsecutivoAnulacion();
		}    
		if(!forma.getLoginUsuario().trim().equals(""))
		{	
			forma.setEsBusquedaXLoginUsuarioAnulacion(true);
			parametrosGeneracionConsulta+=" Usuario: "+forma.getLoginUsuario();
		}
		if(forma.getCodigoCentroAtencion()>0)
		{
			parametrosGeneracionConsulta+=" Centro de Atencion: "+Utilidades.obtenerNombreCentroAtencion(con,forma.getCodigoCentroAtencion());
		}
		if(!UtilidadTexto.isEmpty(forma.getEmpresaInstitucion()) && UtilidadTexto.isNumber(forma.getEmpresaInstitucion()))
		{
			if(Utilidades.convertirADouble(forma.getEmpresaInstitucion())>0)
				parametrosGeneracionConsulta+=" Empresa Institucion: "+Utilidades.obtenerRazonSocialEmpresaInstitucion(con,forma.getEmpresaInstitucion());
		}
		
		logger.info("\n\nES BUSQUEDA POR consecutivo factura===> "+forma.getEsBusquedaXConsecutivoFactura()+" consecutivo anul==>"+forma.getEsBusquedaXConsecutivoAnulacion());
		
		forma.setCol(mundoAnulacion.listadoAnulacionFactura(	con, Integer.parseInt(usuario.getCodigoInstitucion()), 
																		forma.getConsecutivoFactura(), 
																		forma.getConsecutivoAnulacion(), 
																		forma.getFechaInicialAnulacion(), 
																		forma.getFechaFinalAnulacion(),
																		forma.getLoginUsuario(),
																		forma.getCodigoCentroAtencion(),
																		forma.getEmpresaInstitucion()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaConsulta");	
	}	
	
	/**
	 * Este metodo especifica las acciones a realizar en el estado
	 * volverAlistar anulaciones
	 */
	private ActionForward accionVolverAListarAnulaciones	(	AnulacionFacturasForm forma, 
																							ActionMapping mapping,
																							Connection con,
																							UsuarioBasico usuario)  
	{
		AnulacionFacturas mundoAnulacion= new AnulacionFacturas();
		
		logger.info("\n\n1111111ES BUSQUEDA POR consecutivo factura===> "+forma.getEsBusquedaXConsecutivoFactura()+" consecutivo anul==>"+forma.getEsBusquedaXConsecutivoAnulacion());
		
		if(!forma.getEsBusquedaXConsecutivoFactura())
		    forma.setConsecutivoFactura("");
		if(!forma.getEsBusquedaXConsecutivoAnulacion())
		    forma.setConsecutivoAnulacion("");
		if(!forma.getEsBusquedaXLoginUsuarioAnulacion())
			forma.setLoginUsuario("");
		forma.setCol(mundoAnulacion.listadoAnulacionFactura(	con, Integer.parseInt(usuario.getCodigoInstitucion()), 
		        																						forma.getConsecutivoFactura(), 
		        																						forma.getConsecutivoAnulacion(), 
		        																						forma.getFechaInicialAnulacion(), 
		        																						forma.getFechaFinalAnulacion(),
		        																						forma.getLoginUsuario(),
		        																						forma.getCodigoCentroAtencion(),forma.getEmpresaInstitucion()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaConsulta")	;	
	}	
	
	/**
	 * Metodo que ordena las columnas de la anulacion
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param con
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionOrdenar(	AnulacionFacturasForm forma,
															ActionMapping mapping,
															HttpServletRequest request, 
															Connection con) 
    {
	    try
	    {
	        forma.setCol(Listado.ordenarColumna(new ArrayList(forma.getCol()),forma.getUltimaPropiedad(),forma.getColumna()));
	        forma.setUltimaPropiedad(forma.getColumna());
	        UtilidadBD.closeConnection(con);
	    }
	    catch(Exception e)
	    {
	        logger.warn("Error en el listado de anulaciones facturas ");
	        UtilidadBD.closeConnection(con);
	        forma.reset(true);
	        ArrayList atributosError = new ArrayList();
	        atributosError.add(" Listado anulaciones");
	        request.setAttribute("codigoDescripcionError", "errors.invalid");				
	        request.setAttribute("atributosError", atributosError);
	        return mapping.findForward("paginaError");		
	    }
	    forma.setEstado("consultarAnulaciones");
	    return mapping.findForward("paginaConsulta")	;
    }
	
	
	/**
	 * Metodo para hacer que el paciente
	 * pueda ser visto por todos los usuario en la aplicacion
	 * @param paciente
	 */
	private void setObservable(Connection con, int codigoCuenta) 
	{
	    Cuenta c= new Cuenta();
	    c.cargarCuenta(con, codigoCuenta+"");
	    ObservableBD observable = (ObservableBD)getServlet().getServletContext().getAttribute("observable"); 
		if (observable != null) {
			synchronized (observable) {
				observable.setChanged();
				observable.notifyObservers(new Integer(c.getPaciente().getCodigoPersona()));
			}
		}
	}
	
	 /**
     * Metodo para generar los reportes
     * @param tipoReporte
     * @param con
     * @param forma
     * @param usuario
     * @param request
     */
	@SuppressWarnings("unchecked")
	private void generarReporte(String tipoReporte,
								Connection con, 
								AnulacionFacturasForm forma,
								UsuarioBasico usuario,
								HttpServletRequest request) 
    {
        DesignEngineApi comp;        
        if(tipoReporte.equals("listado"))
        {
        	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
        	comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/","AnulacionFacturas.rptdesign");
            comp.insertImageHeaderOfMasterPage1(0, 0, institucionBasica.getLogoReportes());
            comp.insertGridHeaderOfMasterPage(0,1,1,4);
            Vector v=new Vector();
            v.add(institucionBasica.getRazonSocial());
            v.add(institucionBasica.getTipoIdentificacion()+"         "+institucionBasica.getNit());     
            if(!institucionBasica.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
                v.add("Actividad Economica: "+institucionBasica.getActividadEconomica());
            v.add(institucionBasica.getDireccion()+"          "+institucionBasica.getTelefono());
            comp.insertLabelInGridOfMasterPage(0,1,v);
            comp.insertLabelInGridPpalOfHeader(1,0, parametrosGeneracionConsulta);
            comp.obtenerComponentesDataSet("listadoAnulacionFacturas");            
            String oldQuery=comp.obtenerQueryDataSet();
            logger.info("Query original del design->"+oldQuery);
            String newQuery=oldQuery+fragmentWhereInReport(forma, usuario.getCodigoInstitucionInt()); 
            logger.info("Nueva Consulta->"+newQuery);
            comp.modificarQueryDataSet(newQuery);
            //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
            comp.lowerAliasDataSet();
            String newPathReport = comp.saveReport1(false);
            if(!newPathReport.equals(""))
            {
                request.setAttribute("isOpenReport", "true");
                request.setAttribute("newPathReport", newPathReport);
            }            
            comp.updateJDBCParameters(newPathReport);
        }
        else if(tipoReporte.equals("detalle"))
        {
        	InstitucionBasica institucionBasica = new InstitucionBasica();
        	logger.info("\n\n codigo factura->"+forma.getCodigoFactura().intValue()+"\n\n");
        	int codigoConvenio=Utilidades.obtenerConvenioFactura(con, forma.getCodigoFactura().intValue()).getCodigo();
        	institucionBasica.cargarXConvenio(usuario.getCodigoInstitucionInt(), codigoConvenio);
        	
            comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/","DetalleAnulacionFactura.rptdesign");
            comp.obtenerComponentesDataSet("detalleAnulacion");
            comp.insertImageHeaderOfMasterPage1(0, 0, institucionBasica.getLogoReportes());
            comp.insertGridHeaderOfMasterPage(0,1,1,4);
            Vector v=new Vector();
            v.add(institucionBasica.getRazonSocial());
            v.add(institucionBasica.getTipoIdentificacion()+"             "+institucionBasica.getNit());  
            if(!institucionBasica.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
                v.add("Actividad Economica: "+institucionBasica.getActividadEconomica());
            v.add(institucionBasica.getDireccion()+"              "+institucionBasica.getTelefono());
            comp.insertLabelInGridOfMasterPage(0,1,v);
            //comp.insertLabelInGridPpalOfHeader(1,0,ins.getEncabezado());
            //comp.insertLabelInGridPpalOfFooter(1,0,ins.getPie());
            //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
            comp.lowerAliasDataSet();
            String newPathReport = comp.saveReport1(false);
            if(!newPathReport.equals(""))
            {
                request.setAttribute("isOpenReport", "true");
                request.setAttribute("newPathReport", newPathReport);
            }            
//          por ultimo se modifica la conexion a BD
            comp.updateJDBCParameters(newPathReport);
        }
            
    }
	
	/**
	 * fragmento del where para armar el data set del rptdegign
	 * @param AnulacionFacturasForm forma
	 */
	private String fragmentWhereInReport(AnulacionFacturasForm forma, int codigoInstitucion)
	{
		logger.info("\n\n CODIGO FACTURA-->"+forma.getCodigoFactura()+"\n\n");
		
		DtoConsultaFacturasAnuladas dto= new DtoConsultaFacturasAnuladas();
		dto.setInstitucion(codigoInstitucion);
		
		if(!forma.getConsecutivoFactura().trim().equals(""))
        {
			dto.setConsecutivoFactura(new BigDecimal(Utilidades.convertirADouble(forma.getConsecutivoFactura())));
        }    
        if(!forma.getConsecutivoAnulacion().trim().equals(""))
        {
        	dto.setConsecutivoFactura(new BigDecimal(forma.getConsecutivoAnulacion()));
        }
        if(!forma.getFechaFinalAnulacion().equals(""))
        {
        	dto.setFechaFinalAnulacion(UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinalAnulacion()));
        	dto.setFechaInicialAnulacion(UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicialAnulacion()));
        }
        if(!forma.getLoginUsuario().trim().equals(""))
        {	
        	dto.setLoginUsuario(forma.getLoginUsuario());
        }
        if(forma.getCodigoCentroAtencion()>0)
        {
        	dto.setCentroAtencion(forma.getCodigoCentroAtencion());
        }
        if(UtilidadTexto.isNumber(forma.getEmpresaInstitucion()))
        {
        	if(Utilidades.convertirADouble(forma.getEmpresaInstitucion())>0)
        	{
        		dto.setEmpresaInstitucion(Utilidades.convertirAEntero(forma.getEmpresaInstitucion()));
        	}
        }
        
        return ValidacionesAnulacionFacturas.obtenerRestriccionesReporte(dto);
	}
	
	
	/**
	 * Accion para cuando no se carga un estado valido
	 * @param mapping
	 * @param request
	 * @param con
	 * @param forma
	 * @return
	 */
	private ActionForward accionError(	ActionMapping mapping,
										HttpServletRequest request, 
										Connection con,
										AnulacionFacturasForm forma) 
	{
		forma.reset(true);	
		logger.warn("Estado no valido dentro del flujo de consulta y registro de anulacion de facturas (null) ");
		request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaError");
	}

}
