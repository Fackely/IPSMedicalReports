/*
 * Creado en 2/08/2004
 *
 */
package com.princetonsa.action.cargos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.RespuestaValidacion;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.cargos.ActivacionCargosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.ActivacionCargos;
import com.princetonsa.mundo.resumenAtenciones.EstadoCuenta;

/**
 * @author Juan David Ramírez López
 *
 * Princeton S.A.
 */
public class ActivacionCargosAction extends Action
{
	/**
	 * Manejador de logs
	 */
	private Logger logger=Logger.getLogger(ActivacionCargosAction.class);
    
	/**
	 * Mantener el estado del cargo (Activo ó Inactivo)
	 */
	private boolean activTempo;
	
	/**
	 *Instancia del Mundo de activación e inactivación de cargos
	 */
	private ActivacionCargos mundo=new ActivacionCargos();
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * @throws IOException
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws SQLException, IOException 
			{

		Connection con=null;
		PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

		if(paciente.getCodigoPersona()<=0)
		{
			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			return mapping.findForward("paginaError");
		}

		if(form instanceof ActivacionCargosForm)
		{
			try{
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión "+e);
				}
				ActivacionCargosForm forma=(ActivacionCargosForm)form;
				String estado=forma.getEstado();

				logger.warn("estado ActivacionCArgosAction:  "+estado);

				/**
				 * Validar concurrencia
				 * Si ya está en proceso de facturación, no debe dejar entrar
				 **/
				if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
				/**
				 * Validar concurrencia
				 * Si ya está en proceso de distribucion, no debe dejar entrar
				 **/
				if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), usuario.getLoginUsuario()) )
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
				}

				/*************************ACTIVACION/INACTIVACION CARGOS***********************************/
				if(estado.equals("empezar"))
				{
					return accionEmpezar(con,forma,mapping,request,paciente,usuario);

				}
				//***********ESTADOS RELACIONADOS CON EL LISTADO DE SOLICITUDES**************************
				else if(estado.equals("listadoSolicitudes"))
				{
					return accionListadoSolicitudes(con,forma,mapping,usuario);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar(con,forma,mapping);
				}
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,forma,response,mapping,request);
				}
				else if (estado.equals("volverListadoSolicitudes"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				//****************************************************************************************
				//*********ESTADOS RELACIONADOS CON EL DETALLE DE LA SOLICITUD**************************
				else if (estado.equals("detalleSolicitud"))
				{
					return accionDetalleSolicitud(con,forma,mapping);
				}
				else if (estado.equals("guardarSolicitud"))
				{
					return accionGuardarSolicitud(con,forma,mapping,request,usuario,paciente);
				}
				//Estados relacionados con el flujo de cirugias
				else if (estado.equals("detalleAsocio"))
				{
					return accionDetalleAsocio(con,forma,mapping);
				}
				else if (estado.equals("volverDetalleCirugia"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleCirugia");
				}
				//****************************************************************************************
				/*******************************************************************************************/
				/**************************CONSULTA INACTIVACION/ACITVACION**********************************/
				else if (estado.equals("consultar"))
				{
					return accionConsultar(con,forma,mapping,paciente,request,usuario);
				}
				else if (estado.equals("listadoInactivaciones"))
				{
					return accionListadoInactivaciones(con,forma,mapping,paciente,usuario);
				}
				else if (estado.equals("ordenarConsulta"))
				{
					return accionOrdenarConsulta(con,forma,mapping);
				}
				/*******************************************************************************************/

				UtilidadBD.closeConnection(con);
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
			}catch (Exception e) {
				Log4JManager.error(e);
			}
			finally{
				UtilidadBD.closeConnection(con);
			}
			return mapping.findForward("paginaError");
		}
		else
		{
			return null;
		}
			}

	
	/**
	 * Método que realiza la ordenacion de las inactivaciones
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarConsulta(Connection con, ActivacionCargosForm forma, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices = {
				"orden_",
				"codigoServicio_",
				"nombreServicio_",
				"nombreAsocio_",
				"fecha_",
				"hora_",
				"activacion_",
				"paquetizado_",
				"motivo_",
				"valor_",
				"cantidad_",
				"usuario_"
			};

		
		//Se pasa la fecha a formato BD
		for(int i=0;i<forma.getNumConsulta();i++)
			forma.setConsulta("fecha_"+i,UtilidadFecha.conversionFormatoFechaABD(forma.getConsulta("fecha_"+i).toString()));
		
		forma.setConsulta(Listado.ordenarMapa(indices,
				forma.getIndice(),
				forma.getUltimoIndice(),
				forma.getConsulta(),
				forma.getNumConsulta()));
		
		///Se pasa la fecha a formato Aplicacion
		for(int i=0;i<forma.getNumConsulta();i++)
			forma.setConsulta("fecha_"+i,UtilidadFecha.conversionFormatoFechaAAp(forma.getConsulta("fecha_"+i).toString()));
		
		
		forma.setConsulta("numRegistros",forma.getNumConsulta()+"");
		
		forma.setUltimoIndice(forma.getIndice());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("consultar");
	}


	/**
	 * Método que realiza la consulta de las inactivaciones/activaciones del convenio de un ingreso
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param paciente 
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionListadoInactivaciones(Connection con, ActivacionCargosForm forma, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario) 
	{
		forma.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
		
		//Se consulta el listado de las inactivaciones
		forma.setConsulta(mundo.consultarInactivacionesCargos(con, paciente.getCodigoIngreso()+"", forma.getCodigoConvenio()));
		forma.setNumConsulta(Integer.parseInt(forma.getConsulta("numRegistros").toString()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("consultar");
	}


	/**
	 * Método implementado para iniciar la consulta de la inactivación/activacion de cargo
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request 
	 * @param paciente 
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionConsultar(Connection con, ActivacionCargosForm forma, ActionMapping mapping, PersonaBasica paciente, HttpServletRequest request, UsuarioBasico usuario) 
	{
		//************SE VERIFICA SI HAY UN INGRESO CARGADO EN SESIÓN***********************
		if(paciente.getCodigoIngreso()<=0)
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noIngresoSesion", "errors.paciente.noIngresoSesion", true);
		//***********************************************************************************
		forma.reset();
		mundo.init();
		//**********************SE CARGAN LOS CONVENIOS DEL INGRESO******************************************************
		EstadoCuenta mundoEstadoCuenta = new EstadoCuenta();
		forma.setListadoConvenios(mundoEstadoCuenta.cargarTodosConvenioIngreso(con, paciente.getCodigoIngreso()+""));
		
		//Si solo había un convenio se pasa directo al listado de solicitudes
		if(forma.getListadoConvenios().size()==1)
		{
			HashMap<String,Object> elemento = (HashMap<String,Object>)forma.getListadoConvenios().get(0);
			forma.setCodigoConvenio(elemento.get("codigoConvenio").toString());
			forma.setNombreConvenio(elemento.get("nombreConvenio").toString());
			forma.setIdSubCuenta(elemento.get("idSubCuenta").toString());
			
			
			return accionListadoInactivaciones(con, forma, mapping, paciente, usuario);
			
		}
		//***************************************************************************************************
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoConvenios");
	}


	/**
	 * Método implementado para ir al detalle de cargos de un asocio de cirugia
	 * @param con
	 * @param forma 
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleAsocio(Connection con, ActivacionCargosForm forma, ActionMapping mapping) 
	{
		//se cambia el estado
		forma.setEstado("detalleSolicitud");
		
		//Se toma la cirugia y el asocio elegido
		forma.setCodigoServicioCx(forma.getCirugia("codigoCirugia_"+forma.getPosCirugia()).toString());
		forma.setTipoAsocio(forma.getCirugia("codigoAsocio_"+forma.getPosCirugia()).toString());
		forma.setTipoServicioAsocio(forma.getCirugia("tipoServicioAsocio_"+forma.getPosCirugia()).toString());
		forma.setConsecutivoAsocio(forma.getCirugia("consecutivoAsocio_"+forma.getPosCirugia()).toString());
		
		//Se consulta el detalle del cargo asocio
		forma.setDetalleSolicitud(
			mundo.detallarSolicitud(
				con, 
				forma.getIdSubCuenta(), 
				forma.getNumeroSolicitud(), 
				forma.getTipoSolicitud(), 
				forma.getCodigoServicioCx(), 
				forma.getTipoAsocio(), 
				false,
				forma.getTipoServicioAsocio(),
				forma.getConsecutivoAsocio(),
				ConstantesBD.acronimoNo)
			);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleServicio");
	}
	
	/**
	 * Método para guardar los cambios de la inactivacion
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request 
	 * @param usuario 
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionGuardarSolicitud(Connection con, ActivacionCargosForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		ActionErrors errores = new ActionErrors();
		boolean exito = true;
		
		
		//************VALIDACIONES ANTES DE GUARDAR**************************************************
		errores = validacionesGuardarSolicitud(forma,errores);
		//*******************************************************************************************
		if(!errores.isEmpty())
		{
			forma.setEstado("detalleSolicitud");
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("detalleServicio");
		}
		
		//******************SE REALIZA EL PROCESO DE ACTIVACION/INACTIVACION X CARGO************************
		UtilidadBD.iniciarTransaccion(con);
		exito = procesoActivacionInactivacionDetalle(con,forma.getDetalleSolicitud(),forma,usuario,paciente);
		
		if(!exito)
		{
			forma.setEstado("detalleSolicitud");
			UtilidadBD.abortarTransaccion(con);
			errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA INACTIVACION/ACTIVACION DE CARGOS"));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("detalleServicio");
		}
		
		if(forma.isTieneServicioPortatil())
			exito = procesoActivacionInactivacionDetalle(con, forma.getDetalleSolicitudPortatil(), forma, usuario, paciente); 
		//******************************************************************
		
		
		
		
		//Se carga de nuevo el detalle de la solicitud
		forma.setDetalleSolicitud(
			mundo.detallarSolicitud(
				con, 
				forma.getIdSubCuenta(),
				forma.getNumeroSolicitud(), 
				forma.getTipoSolicitud(),
				forma.getCodigoServicioCx(),
				forma.getTipoAsocio(),
				true,
				forma.getTipoServicioAsocio(),
				forma.getConsecutivoAsocio(),
				ConstantesBD.acronimoNo
				)
			);
		
		if(forma.isTieneServicioPortatil())
			forma.setDetalleSolicitudPortatil(
					mundo.detallarSolicitud(
						con, 
						forma.getIdSubCuenta(),
						forma.getNumeroSolicitud(), 
						forma.getTipoSolicitud(),
						forma.getCodigoServicioCx(),
						forma.getTipoAsocio(),
						true,
						forma.getTipoServicioAsocio(),
						forma.getConsecutivoAsocio(),
						ConstantesBD.acronimoSi
						)
					);
		
		
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleServicio");
	}

	/**
	 * Método que realiza el proceso de activacion e inactivacion de cargso
	 * @param con
	 * @param detalleSolicitud
	 * @param usuario 
	 * @param forma 
	 * @param paciente 
	 * @param string 
	 * @return
	 */
	private boolean procesoActivacionInactivacionDetalle(Connection con,HashMap<String, Object> detalleSolicitud, ActivacionCargosForm forma, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		boolean existeOtroCargo = false, exito = true;
		String codigoCargo = "", codigoOtroCargo = "", codSolSubcuenta = "", codSolSubcuentaOtro = "";
		String codigoConvenio = "", codigoServicio = "", codigoAsocio = "", motivo = "", paquetizado = "";
		int codigoEstadoCargo = 0, cantidadActual = 0, cantidadNueva = 0, cantidadOtroCargo = 0, numRegistros = 0, numDetalle = 0; 
		double valorUnitario = 0;
		
		numRegistros = Integer.parseInt(detalleSolicitud.get("numRegistros").toString());
		//Iteracion de la agrupacion de cargos (paquetizados y no paquetizados)
		for(int i=0;i<numRegistros;i++)
		{
			numDetalle = Integer.parseInt(detalleSolicitud.get("numRegistros_"+i).toString());
			
			//Iteracion de los cargos de un grupo 
			for(int j=0;j<numDetalle;j++)
			{
				//Se verifica cual ha sido el registro donde se ingresó cantidad
				if(!detalleSolicitud.get("cantidadNueva_"+i+"_"+j).toString().equals(""))
				{
					//1) Se toma la informacion correspondiente a ese cargo-----------------------------------------------------
					cantidadActual = Integer.parseInt(detalleSolicitud.get("cantidad_"+i+"_"+j).toString());
					cantidadNueva = Integer.parseInt(detalleSolicitud.get("cantidadNueva_"+i+"_"+j).toString());
					codigoCargo = detalleSolicitud.get("codigo_"+i+"_"+j).toString();
					codSolSubcuenta = detalleSolicitud.get("codSolSubcuenta_"+i+"_"+j).toString();
					codigoEstadoCargo = Integer.parseInt(detalleSolicitud.get("codigoEstado_"+i+"_"+j).toString());
				
					valorUnitario = Double.parseDouble(detalleSolicitud.get("valorUnitario_"+i+"_"+j).toString());
					codigoConvenio = detalleSolicitud.get("codigoConvenio_"+i+"_"+j).toString();
					motivo = detalleSolicitud.get("motivo_"+i+"_"+j).toString();
					codigoServicio = detalleSolicitud.get("servicio_"+i+"_"+j).toString();
					if(detalleSolicitud.get("codigoAsocio_"+i+"_"+j)!=null)
					{
						codigoAsocio = detalleSolicitud.get("codigoAsocio_"+i+"_"+j).toString();
					}
					paquetizado = detalleSolicitud.get("paquetizado_"+i+"_"+j).toString();
					
					//2) Se verifica si existe otro cargo---------------------------------------------------------------------
					if(numDetalle>1)
					{
						existeOtroCargo = true;
						//Si existe otro cargo, se saca el código de ese otro cargo y su cantidad
						for(int k=0;k<numDetalle;k++)
						{
							if(!detalleSolicitud.get("codigo_"+i+"_"+k).toString().equals(codigoCargo))
							{
								codigoOtroCargo = detalleSolicitud.get("codigo_"+i+"_"+k).toString();
								cantidadOtroCargo = Integer.parseInt(detalleSolicitud.get("cantidad_"+i+"_"+k).toString());
								codSolSubcuentaOtro = detalleSolicitud.get("codSolSubcuenta_"+i+"_"+k).toString();
								
							}
						}
					}
					else
					{
						existeOtroCargo = false;
						codigoOtroCargo = "";
						cantidadOtroCargo = 0;
						codSolSubcuentaOtro = "";
						
					}
					
					//3) Se hace llamado a la inactivacion/activacion cargos -----------------------------------------------
					if(!mundo.activarInactivarCargo(
						con, 
						codigoCargo, 
						codSolSubcuenta, 
						codigoEstadoCargo, 
						cantidadActual, 
						cantidadNueva, 
						valorUnitario, 
						usuario.getLoginUsuario(), 
						forma.getNumeroSolicitud(), 
						forma.getTipoSolicitud(), 
						paciente.getCodigoIngreso()+"", 
						codigoConvenio, 
						motivo, 
						codigoServicio, 
						codigoAsocio, 
						paquetizado,
						existeOtroCargo, 
						codigoOtroCargo, 
						codSolSubcuentaOtro, 
						cantidadOtroCargo,
						forma.getTipoServicioAsocio(),
						forma.getConsecutivoAsocio()).isTrue())
						exito = false;
				}
			}
		}	
		
		
		return exito;
	}


	/**
	 * Método que realiza las validaciones antes de realizar acitvacin/inactivacion de cargos
	 * @param forma
	 * @param errores
	 * @return
	 */
	private ActionErrors validacionesGuardarSolicitud(ActivacionCargosForm forma, ActionErrors errores) 
	{
		boolean ingreso = false; //para verificar si se ingresó cantidad para activar/inactivar
		boolean hayInactivacionesValidas = false; //verifica si hay cargos que se puedan inactivar
		
		int numRegistros = Integer.parseInt(forma.getDetalleSolicitud("numRegistros").toString());
		for(int i=0;i<numRegistros;i++)
		{
			int numDetalle = Integer.parseInt(forma.getDetalleSolicitud("numRegistros_"+i).toString());
			
			String mensaje = "";
			Utilidades.imprimirMapa(forma.getDetalleSolicitud());
			String ordenPaquete = "(No es paquetizada)";
			if(forma.getDetalleSolicitud("ordenPaquete_"+i+"_0")!=null)
			{
				ordenPaquete = forma.getDetalleSolicitud("ordenPaquete_"+i+"_0").toString();
			}
			boolean paquetizado = UtilidadTexto.getBoolean(forma.getDetalleSolicitud("paquetizado_"+i+"_0").toString());
			
			for(int j=0;j<numDetalle;j++)
			{
				String tipoDistribucion = forma.getDetalleSolicitud("tipoDistribucion_"+i+"_"+j).toString(); 
				
				//Se verifica solo si el tipo de distribucion es por cantidad
				if(!tipoDistribucion.equals(ConstantesIntegridadDominio.acronimoTipoDistribucionPorcentual)&&
						!tipoDistribucion.equals(ConstantesIntegridadDominio.acronimoTipodistribucionMonto))
				{
					hayInactivacionesValidas = true;
					
					if(!forma.getDetalleSolicitud("cantidadNueva_"+i+"_"+j).toString().equals(""))
					{
						ingreso = true;
						int cantidadActual = Integer.parseInt(forma.getDetalleSolicitud("cantidad_"+i+"_"+j).toString());
						int cantidadNueva = 0;
						
						//Se verifica que la cantidad sea numérica------------------------------------------
						try
						{
							cantidadNueva = Integer.parseInt(forma.getDetalleSolicitud("cantidadNueva_"+i+"_"+j).toString());
						}
						catch(Exception e)
						{
							mensaje = "La cantidad a Activar/Inactivar"+(paquetizado?" del cargo asociado al paquete N° "+ordenPaquete:"");
							errores.add("",new ActionMessage("errors.integer",mensaje));
							cantidadNueva = 0;
						}
						
						//La cantidad nueva debe ser un número mayor que 0
						if(cantidadNueva<=0)
							errores.add("",new ActionMessage("errors.integerMayorQue","La cantidad de Activación/Inactivación","0"));
						
						//Se verifica que la cantidad inrgesada sea menor o igual de la actual----------------------------------------------
						if(cantidadNueva>cantidadActual)
						{
							mensaje = "La cantidad a Activar/Inactivar"+(paquetizado?" del cargo asociado al paquete N° "+ordenPaquete:"");
							errores.add("",new ActionMessage("errors.integerMenorIgualQue",mensaje,"la cantidad actual del cargo"));
						}
						
						//Se verifica que el motivo de activación/inactivacion se haya ingresado----------------------------------------------
						if(forma.getDetalleSolicitud("motivo_"+i+"_"+j).toString().equals(""))
						{
							mensaje = "El motivo"+(paquetizado?" del cargo asociado al paquete N° "+ordenPaquete:"");
							errores.add("", new ActionMessage("errors.required",mensaje));
						}
					}
				}
			}
		}
		
		//Se verifica que se haya ingresado cantidad a inactivar/activar
		if(!ingreso&&hayInactivacionesValidas&&!forma.isTieneServicioPortatil())
		{
			if(numRegistros==1)
				errores.add("",new ActionMessage("errors.required","La cantidad de Activación/Inactivación"));
			else
				errores.add("",new ActionMessage("errors.notEspecific","Es requerido ingresar como mínimo una cantidad a activar/inactivar en los cargos de la solicitud"));
			
		}
		
		//***********************VALIDACIONES CUANDO ES SERVICIO PORTATIL***********************************************
		if(forma.isTieneServicioPortatil())
		{
			numRegistros = Integer.parseInt(forma.getDetalleSolicitudPortatil("numRegistros").toString());
			for(int i=0;i<numRegistros;i++)
			{
				int numDetalle = Integer.parseInt(forma.getDetalleSolicitudPortatil("numRegistros_"+i).toString());
				
				String mensaje = "";
				String ordenPaquete = "(No es paquetizada)";
				if(forma.getDetalleSolicitud("ordenPaquete_"+i+"_0")!=null)
				{
					ordenPaquete = forma.getDetalleSolicitud("ordenPaquete_"+i+"_0").toString();
				}
				boolean paquetizado = UtilidadTexto.getBoolean(forma.getDetalleSolicitudPortatil("paquetizado_"+i+"_0").toString());
				
				for(int j=0;j<numDetalle;j++)
				{
					String tipoDistribucion = forma.getDetalleSolicitudPortatil("tipoDistribucion_"+i+"_"+j).toString(); 
					
					//Se verifica solo si el tipo de distribucion es por cantidad
					if(!tipoDistribucion.equals(ConstantesIntegridadDominio.acronimoTipoDistribucionPorcentual)&&
							!tipoDistribucion.equals(ConstantesIntegridadDominio.acronimoTipodistribucionMonto))
					{
						hayInactivacionesValidas = true;
						
						if(!forma.getDetalleSolicitudPortatil("cantidadNueva_"+i+"_"+j).toString().equals(""))
						{
							ingreso = true;
							int cantidadActual = Integer.parseInt(forma.getDetalleSolicitudPortatil("cantidad_"+i+"_"+j).toString());
							int cantidadNueva = 0;
							
							//Se verifica que la cantidad sea numérica------------------------------------------
							try
							{
								cantidadNueva = Integer.parseInt(forma.getDetalleSolicitudPortatil("cantidadNueva_"+i+"_"+j).toString());
							}
							catch(Exception e)
							{
								mensaje = "La cantidad a Activar/Inactivar"+(paquetizado?" del cargo asociado al paquete N° "+ordenPaquete:"");
								errores.add("",new ActionMessage("errors.integer",mensaje));
								cantidadNueva = 0;
							}
							
							//La cantidad nueva debe ser un número mayor que 0
							if(cantidadNueva<=0)
								errores.add("",new ActionMessage("errors.integerMayorQue","La cantidad de Activación/Inactivación del portatil","0"));
							
							//Se verifica que la cantidad inrgesada sea menor o igual de la actual----------------------------------------------
							if(cantidadNueva>cantidadActual)
							{
								mensaje = "La cantidad a Activar/Inactivar"+(paquetizado?" del cargo asociado al paquete N° "+ordenPaquete:"");
								errores.add("",new ActionMessage("errors.integerMenorIgualQue",mensaje,"la cantidad actual del cargo"));
							}
							
							//Se verifica que el motivo de activación/inactivacion se haya ingresado----------------------------------------------
							if(forma.getDetalleSolicitudPortatil("motivo_"+i+"_"+j).toString().equals(""))
							{
								mensaje = "El motivo"+(paquetizado?" del cargo asociado al paquete N° "+ordenPaquete:"");
								errores.add("", new ActionMessage("errors.required",mensaje));
							}
						}
					}
				}
			}
			
			if(!ingreso&&hayInactivacionesValidas)
				errores.add("",new ActionMessage("errors.notEspecific","Es requerido ingresar como mínimo una cantidad a activar/inactivar en los cargos de la solicitud"));
		}
		//******************************************************************************************************************
		
		return errores;
	}

	/**
	 * Método que me lleva al detalle 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleSolicitud(Connection con, ActivacionCargosForm forma, ActionMapping mapping) 
	{
		//Se toma la informacion de la solicitud elegida*********************************
		forma.setNumeroSolicitud(forma.getListadoSolicitudes("numeroSolicitud_"+forma.getPosSolicitud()).toString());
		forma.setOrden(forma.getListadoSolicitudes("orden_"+forma.getPosSolicitud()).toString());
		forma.setTipoSolicitud(Integer.parseInt(forma.getListadoSolicitudes("codigoTipo_"+forma.getPosSolicitud()).toString()));
		forma.setTieneServicioPortatil(Utilidades.convertirAEntero(forma.getListadoSolicitudes("codigoPortatil_"+forma.getPosSolicitud()).toString())!=ConstantesBD.codigoNuncaValido?true:false);
		
		//Si el tipo de solicitud es CIRUGIA todavía no se puede ir al detalle del cargo, se debe antes listar las cirugias y sus asocios
		if(forma.getTipoSolicitud()==ConstantesBD.codigoTipoSolicitudCirugia)
		{
			forma.setCirugia(mundo.consultarDetalleCargosCirugia(con, forma.getNumeroSolicitud(), forma.getIdSubCuenta()));
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("detalleCirugia");
		}
		//Si es un servicio diferente de cirugías entonces si se listan sus cargos
		else
		{
			//Estas variables solo aplican para cirugias entonces deben estar vacías
			forma.setCodigoServicioCx("");
			forma.setTipoAsocio("");
			forma.setTipoServicioAsocio("");
			forma.setConsecutivoAsocio("");
			
			//Se carga el detalle del cargo de la solicitud
			forma.setDetalleSolicitud(mundo.detallarSolicitud(con, forma.getIdSubCuenta(), forma.getNumeroSolicitud(), forma.getTipoSolicitud(),"","",false,"","",ConstantesBD.acronimoNo));
			
			if(forma.isTieneServicioPortatil())
				forma.setDetalleSolicitudPortatil(mundo.detallarSolicitud(con, forma.getIdSubCuenta(), forma.getNumeroSolicitud(), forma.getTipoSolicitud(),"","",false,"","",ConstantesBD.acronimoSi));
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("detalleServicio");
		}
	}

	/**
	 * Método que realiza la paginación del listado de solicitudes por convenio
	 * @param con
	 * @param forma
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, ActivacionCargosForm forma, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
		    UtilidadBD.cerrarConexion(con);
			response.sendRedirect(forma.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de ActivacionCargosAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en ActivacionCargosAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Método que realiza la ordenacion de las solicitudes del convenio
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, ActivacionCargosForm forma, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices = {
				"numeroSolicitud_",
				"codigoPortatil_",
				"orden_",
				"nombreTipo_",
				"codigoTipo_",
				"centroCostoSolicitante_",
				"centroCostoSolicitado_",
				"estadoMedico_",
				"fecha_"
			};

		
		//Se pasa la fecha a formato BD
		for(int i=0;i<forma.getNumSolicitudes();i++)
			forma.setListadoSolicitudes("fecha_"+i,UtilidadFecha.conversionFormatoFechaABD(forma.getListadoSolicitudes("fecha_"+i).toString()));
		
		forma.setListadoSolicitudes(Listado.ordenarMapa(indices,
				forma.getIndice(),
				forma.getUltimoIndice(),
				forma.getListadoSolicitudes(),
				forma.getNumSolicitudes()));
		
		///Se pasa la fecha a formato Aplicacion
		for(int i=0;i<forma.getNumSolicitudes();i++)
			forma.setListadoSolicitudes("fecha_"+i,UtilidadFecha.conversionFormatoFechaAAp(forma.getListadoSolicitudes("fecha_"+i).toString()));
		
		
		forma.setListadoSolicitudes("numRegistros",forma.getNumSolicitudes()+"");
		
		forma.setUltimoIndice(forma.getIndice());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para listar las solicitudes de un convenio del ingreso del paciente
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionListadoSolicitudes(Connection con, ActivacionCargosForm forma, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.setListadoSolicitudes(mundo.listar(con, forma.getIdSubCuenta()));
		
		
		forma.setNumSolicitudes(Integer.parseInt(forma.getListadoSolicitudes("numRegistros").toString()));
		
		forma.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método que inicia el flujo dela funcionalidad de Inactivacion/Activacion Cargo
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ActivacionCargosForm forma, ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente, UsuarioBasico usuario) 
	{
		//*********************VALIDACIONES INICIALES****************************************************************
		RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(con, paciente);
		
		if(!resp.puedoSeguir)
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, resp.textoRespuesta, resp.textoRespuesta, true);
		
		//****************************************************************************************************************
		forma.reset();
		mundo.init();
		//**********************SE CARGAN LOS CONVENIOS DEL INGRESO******************************************************
		EstadoCuenta mundoEstadoCuenta = new EstadoCuenta();
		forma.setListadoConvenios(mundoEstadoCuenta.cargarTodosConvenioIngreso(con, paciente.getCodigoIngreso()+""));
		
		//Si solo había un convenio se pasa directo al listado de solicitudes
		if(forma.getListadoConvenios().size()==1)
		{
			HashMap<String,Object> elemento = (HashMap<String,Object>)forma.getListadoConvenios().get(0);
			forma.setNombreConvenio(elemento.get("nombreConvenio").toString());
			forma.setIdSubCuenta(elemento.get("idSubCuenta").toString());
			forma.setEstado("listadoSolicitudes");
			
			return accionListadoSolicitudes(con, forma, mapping,usuario);
			
		}
		//***************************************************************************************************
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoConvenios");
		
	}







}
