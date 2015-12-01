package com.princetonsa.action.facturasVarias;

import java.sql.Connection;

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

import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturasVarias.GeneracionModificacionAjustesFacturasVariasForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturasVarias.GeneracionModificacionAjustesFacturasVarias;



/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */
 
public class GeneracionModificacionAjustesFacturasVariasAction extends Action 
{

	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
		Logger logger = Logger.getLogger(GeneracionModificacionAjustesFacturasVariasAction.class);
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
      //-----Se instancian los indices del mundo----------------------------------------------
		String [] indicesFacturasVarias = GeneracionModificacionAjustesFacturasVarias.indicesFacturasVarias;
		String [] indicesAjustesFacturasVarias = GeneracionModificacionAjustesFacturasVarias.indicesAjustesFacturasVarias;
		String [] indicesCriterios = GeneracionModificacionAjustesFacturasVarias.indicesCriteriosBusqueda;
	//--------------------------------------------------------------------------------------
		
	/*----------------------------------------------------------------------------------
	 * 						METODO EXECUTE DEL ACTION
	 ----------------------------------------------------------------------------------*/
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form, HttpServletRequest request,
								 HttpServletResponse response) throws Exception
	{
		Connection connection = null;
		try{
		if (form instanceof GeneracionModificacionAjustesFacturasVariasForm) 
		 {
						
			
			//se habre la conexion a la BD
			connection = UtilidadBD.abrirConexion();
			
			
			//se verifica si la conexion esta nula
			if (connection == null)
			{
				// de ser asi se envia a una pagina de error. 
				request.setAttribute("CodigoDescripcionError","erros.problemasBd");
				return mapping.findForward("paginaError");
			}
			
			//se obtiene el usuario cargado en sesion.
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			//se obtiene el paciente cargado en sesion.
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			
			//optenemos el valor de la forma.
			GeneracionModificacionAjustesFacturasVariasForm forma = (GeneracionModificacionAjustesFacturasVariasForm) form;
			
			//optenemos el estado que contiene la forma.
			String estado = forma.getEstado();
			
			ActionErrors errores = new ActionErrors();
			
			//se instancia el mundo
			
			GeneracionModificacionAjustesFacturasVarias mundo = new GeneracionModificacionAjustesFacturasVarias();
			
			logger.info("\n\n***************************************************************************");
			logger.info(" 	           EL ESTADO DE GeneracionModificacionAjustesFacturasVariasForm ES ====>> "+forma.getEstado());
			logger.info("\n***************************************************************************");
			
			/*-------------------------------------------------------------------
			 * 	ESTADOS PARA LA GENERACION MODIFICACION AJUSTES FACTURAS VARIAS
			 -------------------------------------------------------------------*/
			
			
			/*----------------------------------------------
			 * ESTADO =================>>>>>  NULL
			 ---------------------------------------------*/
			if (estado == null)
			{
				//resetiamos la forma.
				forma.reset();
			
				//se saca un log con el siguiente texto.
				logger.warn("Estado no Valido dentro del Flujo de Generacion modificacion ajustes facturas varias (null)");
				//se asigana un error a mostar por ser un estado invalido
				request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
				//se cierra la conexion con la BD
				UtilidadBD.cerrarConexion(connection);
				
				//se retorna el error al forward paginaError.
				return mapping.findForward("paginaError");
			}
			/*----------------------------------------------
			 * ESTADO =================>>>>>  INICIAL
			 ---------------------------------------------*/
			else
				if (estado.equals("inicial"))
				{ 
					//se inicializan los valores de la forma.
					forma.reset();
					forma.resetAjsutes();
					forma.resetFactura();
					//se carga el concepto
					mundo.cargarConcepto(connection, forma, usuario);
					
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("principal");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  INITBUSQUEDA
				 ---------------------------------------------*/
				else
					if (estado.equals("initBusqueda"))
					{ 
						//se inicializan los valores de la forma.
						forma.reset();
						UtilidadBD.cerrarConexion(connection);
						return mapping.findForward("listado");
					}
					/*----------------------------------------------
					 * ESTADO =================>>>>>  BUSCAR
					 ---------------------------------------------*/
					else
						if (estado.equals("buscar"))
						{ 
							//se inicializan los valores de la forma.
							forma.resetBusqueda();
							errores= validarCriterios(connection, forma);
							if(!errores.isEmpty())
							{
								saveErrors(request, errores);
								forma.setEstado("initBusqueda");
								return mapping.findForward("listado");
								
							}	
							
							
							forma.getFiltrosBusqueda().put("codigoDeudor", forma.getCodigoDeudor());
							forma.getFiltrosBusqueda().put("descripDeudor", forma.getDescripDeudor());
							forma.getFiltrosBusqueda().put("tipoDeudor", forma.getTipoDeudor());
				
							forma.setListado(mundo.consultarAjustesFacturasVarias(connection, forma.getFiltrosBusqueda(), usuario, false));
					
							if (Integer.parseInt(forma.getListado("numRegistros")+"")==1)
							{
								forma.setIndex("0");
								mundo.initGenModAjustesFacturasVarias(connection, forma, usuario);
								UtilidadBD.cerrarConexion(connection);
								return mapping.findForward("principal");
							}
							
							UtilidadBD.cerrarConexion(connection);
					
							return mapping.findForward("listado");
						}
						/*----------------------------------------------
						 * ESTADO =================>>>>>  CARGARAJUSTE
						 ---------------------------------------------*/
						else
							if (estado.equals("cargarAjuste"))
							{ 
							
								mundo.initGenModAjustesFacturasVarias(connection, forma, usuario); 
								UtilidadBD.cerrarConexion(connection);
								return mapping.findForward("principal");
							}
							/*----------------------------------------------
							 * ESTADO =================>>>>>  CARGARCONCEPTO	
							 ---------------------------------------------*/
							else
								if (estado.equals("cargarconcepto"))
								{ 
									if (!forma.getMapaAjustes(indicesAjustesFacturasVarias[2]+"0").toString().equals(""))
										errores= validarConsecutivos(connection, forma, usuario, mundo);
									
									if(!errores.isEmpty())
									{
										saveErrors(request, errores);
										return ComunAction.accionSalirCasoError(mapping, request, connection,logger, "Falta definir el consecutivo de Ajuste "+ValoresPorDefecto.getIntegridadDominio(forma.getMapaAjustes(indicesAjustesFacturasVarias[2]+"0")+"")+" facturas Varias.","Falta definir el consecutivo de Ajuste "+ValoresPorDefecto.getIntegridadDominio(forma.getMapaAjustes(indicesAjustesFacturasVarias[2]+"0")+"")+" facturas Varias. Por favor verifique", false);
										
									}	
								
									mundo.cargarConcepto(connection, forma, usuario);
									UtilidadBD.cerrarConexion(connection);
									return mapping.findForward("principal");
								}
								/*----------------------------------------------
								 * ESTADO =================>>>>>  CARGARFACTURA
								 ---------------------------------------------*/
								else
									if (estado.equals("cargarFactura"))
									{ 
										
										forma.setMapaInfoFac(mundo.cargarFactura(connection, usuario, forma.getMapaInfoFac(indicesFacturasVarias[13]+"0")+""));
										forma.resetAjsutes();
										if (Utilidades.convertirAEntero(forma.getMapaInfoFac("numRegistros")+"")<1)
										{
											errores.add("",new ActionMessage("prompt.generico","Probablemente la factura no existe o no se encuentra aprobada"));
											saveErrors(request, errores);
										}
										UtilidadBD.cerrarConexion(connection);
										return mapping.findForward("principal");
									}
									/*----------------------------------------------
									 * ESTADO =================>>>>>  GUARDAR	
									 ---------------------------------------------*/
									else
										if (estado.equals("guardar"))
										{ 
											return mundo.guardar(connection, forma, mapping,usuario);
										}
										else /*----------------------------------------------
											 * ESTADO =================>>>>>  REDIRECCION
											 ---------------------------------------------*/
											if (estado.equals("redireccion"))
											{
												UtilidadBD.closeConnection(connection);
												response.sendRedirect(forma.getLinkSiguiente());
												return null;
											}
											else/*----------------------------------------------
												 * ESTADO =================>>>>>  ORDENAR
												 ---------------------------------------------*/
												if (estado.equals("ordenar"))//estado encargado de ordenar el HashMap del censo.
												{
													forma.setListado(mundo.accionOrdenarMapa(forma.getListado(), forma));
													UtilidadBD.closeConnection(connection);
													return mapping.findForward("listado");	
											
												}
			
		 }
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(connection);
		}
		return super.execute(mapping, form, request, response);
	}
	
	
	
	/**
	 * Metodo encargado de validar
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param mundo
	 * @return
	 */
	public ActionErrors validarConsecutivos (Connection connection,GeneracionModificacionAjustesFacturasVariasForm forma,UsuarioBasico usuario, GeneracionModificacionAjustesFacturasVarias mundo)
	{
		//----se obtiene el valor del consecutivo, dependiendo de si es un ajuste debito o credito----------------------
		String nomConsec=mundo.obtenerNombreConsecutivoAjustesFacturasVarias(usuario, forma.getMapaAjustes(indicesAjustesFacturasVarias[2]+"0")+"");
		String consecutivo=UtilidadBD.obtenerValorActualTablaConsecutivos(connection, nomConsec, usuario.getCodigoInstitucionInt());
		//-------------------------------------------------------------------------------------------------------------
		
		ActionErrors errores = new ActionErrors();
		if (Utilidades.convertirAEntero(consecutivo)<0)
			errores.add("",new ActionMessage("errors.noExiste","El consecutivo de facturas varias"));
		return errores;	
	}
	
	
	public ActionErrors validarCriterios (Connection connection, GeneracionModificacionAjustesFacturasVariasForm forma)
	{
		ActionErrors errores = new ActionErrors();
		logger.info("\n entre a validarCriterios "+forma.getFiltrosBusqueda(indicesCriterios[5])+"--"+forma.getFiltrosBusqueda(indicesCriterios[6])+"");
	
		
		
		//se evalua como requerido el deudor
		if (!(forma.getFiltrosBusqueda(indicesCriterios[5])+"").equals("") && !(forma.getFiltrosBusqueda(indicesCriterios[5])+"").equals("null"))
			if ((forma.getFiltrosBusqueda(indicesCriterios[6])+"").equals("") || (forma.getFiltrosBusqueda(indicesCriterios[6])+"").equals("null"))
				errores.add("descripcion",new ActionMessage("errors.required","El deudor"));
				
		//se valida que la fecha inicial no venga vacia
		if (!(forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("") && !(forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("null"))
		{
			//se valida que la fecha final si este
			if ((forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("") || (forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("null"))
				errores.add("descripcion",new ActionMessage("errors.required","La fecha Final"));
		
			//se valida que la fecha incial sea menor o igual a la actual
			if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFiltrosBusqueda(indicesCriterios[2])+"",UtilidadFecha.getFechaActual()))
				errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", forma.getFiltrosBusqueda(indicesCriterios[2])+"", "Actual "+UtilidadFecha.getFechaActual()));
		}
			//se valida que la fecha final no venga vacia
			if (!(forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("") && !(forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("null"))
			{	
				//se valida que la fecha inicial si este
				if ((forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("") || (forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("null"))
					errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial"));
				//se valida que la fecha final sea mayor o igual  a la fecha inicial
				if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFiltrosBusqueda(indicesCriterios[2])+"",forma.getFiltrosBusqueda(indicesCriterios[3])+""))
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", forma.getFiltrosBusqueda(indicesCriterios[3])+"", "Inicial "+forma.getFiltrosBusqueda(indicesCriterios[2])+""));
				else//se valida que la fecha final sea menor o igual a la fecha del sistema
					if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFiltrosBusqueda(indicesCriterios[3])+"",UtilidadFecha.getFechaActual()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", forma.getFiltrosBusqueda(indicesCriterios[2])+"", "Actual "+UtilidadFecha.getFechaActual()));
				
			}
			
			// se pregunta si las fechas vienen sin errores; de ser asi se evalua
			//si la cantidad de dias entre fechas supera 180.
			if (!(forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("") && !(forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("null") && 
				!(forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("") && !(forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("null"))	
				if(UtilidadFecha.numeroMesesEntreFechasExacta(forma.getFiltrosBusqueda(indicesCriterios[2])+"", forma.getFiltrosBusqueda(indicesCriterios[3])+"")>6)
					errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual","El rango de dias entre fechas", "180 dias"));
			
			//si la cantidad de dias entre fechas supera 180.
			if (!(forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("") && !(forma.getFiltrosBusqueda(indicesCriterios[2])+"").equals("null") && 
				!(forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("") && !(forma.getFiltrosBusqueda(indicesCriterios[3])+"").equals("null"))	
				if(UtilidadFecha.numeroMesesEntreFechasExacta(forma.getFiltrosBusqueda(indicesCriterios[2])+"", forma.getFiltrosBusqueda(indicesCriterios[3])+"")>6)
					errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual","El Rango de Días entre Fechas", "180 días"));
			if(
					UtilidadTexto.isEmpty(forma.getFiltrosBusqueda(indicesCriterios[0])+"")&&
					UtilidadTexto.isEmpty(forma.getFiltrosBusqueda(indicesCriterios[1])+"")&&
					UtilidadTexto.isEmpty(forma.getFiltrosBusqueda(indicesCriterios[2])+"")&&
					UtilidadTexto.isEmpty(forma.getFiltrosBusqueda(indicesCriterios[3])+"")&&
					UtilidadTexto.isEmpty(forma.getCodigoDeudor())&&
					UtilidadTexto.isEmpty(forma.getDescripDeudor())&&
					UtilidadTexto.isEmpty(forma.getTipoDeudor())
				)
			{
				errores.add("", new ActionMessage("errors.minimoCampos","1 campo", "la consulta"));
				
			}
			
			
			
		return errores;
	}
	
	
}