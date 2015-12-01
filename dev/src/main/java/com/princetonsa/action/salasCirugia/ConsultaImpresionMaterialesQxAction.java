package com.princetonsa.action.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.salas.UtilidadesSalas;

import com.princetonsa.actionform.salasCirugia.ConsultaImpresionMaterialesQxForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.salasCirugia.ConsultaImpresionMaterialesQx;
import com.princetonsa.pdf.ConsultaImpresionConsumoMaterialesPdf;
import com.servinte.axioma.fwk.exception.IPSException;


/**
 * @author Luis Gabriel Chavez Salazar
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Consulta Materiales Quirúrgicos
 */
public class ConsultaImpresionMaterialesQxAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ConsultaImpresionMaterialesQxAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
													{
		Connection con = null;
		try{
			if (response==null); //Para evitar que salga el warning
			if(form instanceof ConsultaImpresionMaterialesQxForm)
			{
				//OBJETOS A USAR ****************************************************************
				ConsultaImpresionMaterialesQxForm forma =(ConsultaImpresionMaterialesQxForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");


				con = UtilidadBD.abrirConexion();

				ConsultaImpresionMaterialesQx mundo=new ConsultaImpresionMaterialesQx();

				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				String estado=forma.getEstado(); 
				logger.info("[------->[ESTADO CONSULTA/IMPRESION CONSUMO MATERIALES]<-------]-->"+estado);
				ActionErrors errores = new ActionErrors();

				//*************************************************************************************

				if(estado == null)
				{
					forma.reset();	
					logger.warn("Estado no valido dentro del flujo de Materiales Quirúgicos (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				//*******ESTADOS DE INGRESAR/MODIFICAR MATERIALES QUIRURGICOS**********************
				/*****************************************************
				 * Inicio de la funcionalidad 
				 *****************************************************/
				else if (estado.equals("empezar"))
				{
					forma.reset();
					UtilidadBD.closeConnection(con);
					return accionEmpezar(forma,mapping,paciente,usuario,request);
				}
				/*****************************************************
				 * Inicio del flujo consulta por paciente:
				 * se valida el paciente en sesion, 
				 * se consultan los ingresos del paciente  
				 *****************************************************/
				else if (estado.equals("paciente"))
				{
					//Validaciones
					errores = validarPacienteCargado(con, forma, usuario, paciente, request, errores);
					if(!errores.isEmpty())
					{
						saveErrors(request,errores);	
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");	
					}

					HashMap criterios=new HashMap();
					criterios.put("paciente", paciente.getCodigoPersona());
					criterios.put("centroatencion", usuario.getCodigoCentroAtencion());
					forma.setIngresosPacientes(mundo.consultaIngresosPaciente(con, criterios));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paciente");
				}
				/*****************************************************
				 * Estado para el ordenamiento del listado de ingresos  
				 *****************************************************/
				else
					if (estado.equals("ordenar"))
					{
						accionOrdenarMapa(forma);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paciente");
					}
				/*****************************************************
				 * incio del flujo de consutla pro rango 
				 *****************************************************/
					else if (estado.equals("rango"))
					{
						forma.reset();
						forma.getFiltros().put("centroAtencion",usuario.getCodigoCentroAtencion());
						forma.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion()+"");
						forma.setCentroAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(con, usuario.getCodigoInstitucionInt(),""));
						forma.setCentrosCosto(UtilidadesManejoPaciente.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaDirecto+"", true, usuario.getCodigoCentroAtencion()));
						UtilidadBD.closeConnection(con);
						return mapping.findForward("rango");
					}
				/*****************************************************
				 * Estado para el control del pager  
				 *****************************************************/
					else if (estado.equals("redireccion"))
					{
						UtilidadBD.closeConnection(con);
						response.sendRedirect(forma.getLinkSiguiente());
						return null;
					}
				/*****************************************************
				 * Estado que carga el detalle de ingresos, 
				 * consulta las solicitudes cirugia de un ingreso-cuenta-centro atencion  
				 *****************************************************/
					else if(estado.equals("cargarDetalle"))
					{
						HashMap criterios=new HashMap();
						forma.setEstadoVolverListado("paciente");
						criterios.put("paciente", paciente.getCodigoPersona());
						criterios.put("centroatencion", usuario.getCodigoCentroAtencion());
						criterios.put("cuenta", forma.getIngresosPacientes().get("cuenta_"+forma.getIndex()));
						criterios.put("ingreso", forma.getIngresosPacientes().get("ingreso_"+forma.getIndex()));
						forma.setSolicitudesQx(mundo.cargarSolicitudesCx(con,criterios,true));
						forma.setEsRango(false);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("detalleSolicitudesCx");	
					}
				/*****************************************************
				 * Estado que ordena el listado de solicitudes cirugia  
				 *****************************************************/
					else
						if (estado.equals("ordenar1"))
						{
							accionOrdenarMapa1(forma);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("detalleSolicitudesCx");
						}
				/****************************************************************************************
				 * Estado que carga la informacion del consumo de materiales segun una solicitud cirugia 
				 * verifica si el consumo es por acto o por cirugia para proceder a la consulta y 
				 * redireccionar el flujo. 
				 ****************************************************************************************/
						else if(estado.equals("cargarDetalle1"))
						{
							HashMap criterios=new HashMap();
							//Se asigna el estado de volver
							forma.setEstadoVolver("recargarDetalle");
							criterios.put("solicitud", forma.getSolicitudesQx("numero_solicitud_"+forma.getIndex1()));
							boolean acto=UtilidadesSalas.esSolicitudCirugiaPorActo(con, Integer.parseInt(criterios.get("solicitud").toString()));
							if (acto)
							{
								forma.setConsumoM(mundo.cargarConsumoMaterialesCirugias(con,criterios, true));
								forma.setConsumoServiciosM((HashMap)forma.getConsumoM().get("mapaCirugias"));
								forma.getConsumoServiciosM().put("solicitud", Integer.parseInt(criterios.get("solicitud").toString()));
								forma.getConsumoServiciosM().put("orden",Utilidades.convertirAEntero(forma.getSolicitudesQx("consecutivo_"+forma.getIndex1()).toString()));//cambio no mostrar No solicitud si no No orden
								forma.getConsumoServiciosM().put("farmacia", UtilidadesSalas.obtenerNombreFarmaciaConsumoMaterialesQx(con, Integer.parseInt(criterios.get("solicitud").toString())));
								forma.getConsumoM().put("farmacia", forma.getConsumoServiciosM().get("farmacia"));
								UtilidadBD.closeConnection(con);
								return mapping.findForward("detalleConsumoMateriales");
							}
							else
							{
								HashMap mapa=new HashMap();
								forma.setConsumoM(mundo.cargarConsumoMaterialesCirugias(con, criterios, false));
								forma.setConsumoServiciosM((HashMap)forma.getConsumoM().get("mapaCirugias"));
								forma.getConsumoServiciosM().put("solicitud", Integer.parseInt(criterios.get("solicitud").toString()));
								forma.getConsumoServiciosM().put("orden",Utilidades.convertirAEntero(forma.getSolicitudesQx("consecutivo_"+forma.getIndex1()).toString()));//cambio no mostrar No solicitud si no No orden
								forma.getConsumoServiciosM().put("farmacia", UtilidadesSalas.obtenerNombreFarmaciaConsumoMaterialesQx(con, Integer.parseInt(criterios.get("solicitud").toString())));
								forma.getConsumoM().put("farmacia", forma.getConsumoServiciosM().get("farmacia"));
								UtilidadBD.closeConnection(con);
								return mapping.findForward("detalleConsumoMaterialesPorcirugia");	
							}

						}
				/****************************************************************************************
				 * Estado que carga la informacion del consumo de materiales segun una solicitud cirugia
				 * como es invocado desde el flujo por rango entonces se carga el paciente en sesion y luego 
				 * verifica si el consumo es por acto o por cirugia para proceder a la consulta y 
				 * redireccionar el flujo. 
				 ****************************************************************************************/
						else if(estado.equals("cargarDetalleRango1"))
						{
							int	codpaciente=Integer.parseInt(forma.getSolicitudesQx("codigopac_"+forma.getIndex1()).toString());

							paciente.setCodigoPersona(codpaciente);
							paciente.cargar(con, codpaciente);
							paciente.cargarPaciente(con, codpaciente, usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");

							HashMap criterios=new HashMap();
							criterios.put("solicitud", forma.getSolicitudesQx("numero_solicitud_"+forma.getIndex1()));

							//Se asigna el estado de volver
							forma.setEstadoVolver("recargarDetalle");


							boolean acto=UtilidadesSalas.esSolicitudCirugiaPorActo(con, Integer.parseInt(criterios.get("solicitud").toString()));
							if (acto)
							{
								forma.setConsumoM(mundo.cargarConsumoMaterialesCirugias(con,criterios, true));
								forma.setConsumoServiciosM((HashMap)forma.getConsumoM().get("mapaCirugias"));
								forma.getConsumoServiciosM().put("solicitud", Integer.parseInt(criterios.get("solicitud").toString()));
								forma.getConsumoServiciosM().put("orden",Utilidades.convertirAEntero(forma.getSolicitudesQx("consecutivo_"+forma.getIndex1()).toString()));//cambio no mostrar No solicitud si no No orden
								forma.getConsumoServiciosM().put("farmacia", UtilidadesSalas.obtenerNombreFarmaciaConsumoMaterialesQx(con, Integer.parseInt(criterios.get("solicitud").toString())));
								forma.getConsumoM().put("farmacia", forma.getConsumoServiciosM().get("farmacia"));
								UtilidadBD.closeConnection(con);
								return mapping.findForward("detalleConsumoMateriales");
							}
							else
							{
								HashMap mapa=new HashMap();
								forma.setConsumoM(mundo.cargarConsumoMaterialesCirugias(con, criterios, false));
								forma.setConsumoServiciosM((HashMap)forma.getConsumoM().get("mapaCirugias"));
								forma.getConsumoServiciosM().put("solicitud", Integer.parseInt(criterios.get("solicitud").toString()));
								forma.getConsumoServiciosM().put("orden",Utilidades.convertirAEntero(forma.getSolicitudesQx("consecutivo_"+forma.getIndex1()).toString()));//cambio no mostrar No solicitud si no No orden
								forma.getConsumoServiciosM().put("farmacia", UtilidadesSalas.obtenerNombreFarmaciaConsumoMaterialesQx(con, Integer.parseInt(criterios.get("solicitud").toString())));
								forma.getConsumoM().put("farmacia", forma.getConsumoServiciosM().get("farmacia"));
								UtilidadBD.closeConnection(con);
								return mapping.findForward("detalleConsumoMaterialesPorcirugia");	
							}

						}
				/*********************************************************************
				 * Estado que regenera el contenido del select de centros de costo  
				 **********************************************************************/
						else
							if(estado.equals("cargarCentrosCosto"))
							{
								forma.setCentrosCosto(UtilidadesManejoPaciente.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaDirecto+"", true, Utilidades.convertirAEntero(forma.getFiltros().get("centroAtencion").toString())));
								UtilidadBD.closeConnection(con);
								return mapping.findForward("rango");
							}
				/*****************************************************
				 * Estado que carga el detalle de ingresos, desde el flujo de consulta por rango 
				 * consulta las solicitudes cirugia de un centro atencion-centro costo fecha incial - fecha final  
				 *****************************************************/
							else if(estado.equals("cargarDetalleRango"))
							{
								forma.getFiltros().put("fechaInicial", forma.getFechaInicial());
								forma.getFiltros().put("fechaFinal", forma.getFechaFinal());
								forma.setEstadoVolverListado("rango");
								//Utilidades.imprimirMapa(forma.getFiltros());
								forma.setSolicitudesQx(mundo.cargarSolicitudesCx(con,forma.getFiltros(),false));
								//Utilidades.imprimirMapa(forma.getSolicitudesQx());
								forma.setEsRango(true);
								UtilidadBD.closeConnection(con);
								return mapping.findForward("detalleSolicitudesCx");	
							}
				//Estado usado para volver a la pagina del listado de solicitudes
							else if(estado.equals("recargarDetalle"))
							{
								UtilidadBD.closeConnection(con);
								return mapping.findForward("detalleSolicitudesCx");
							}
				/*********************************************************************
				 * Estado que verifica los parametros de imprision y genera el reporte.  
				 **********************************************************************/
							else if (estado.equals("imprimir"))
							{
								return imprimirReporte(con,forma,paciente,usuario,mapping, request);
							}

			}	
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;	 
	}

	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param request 
	 * @param mapping 
	 */
    private ActionForward imprimirReporte(Connection con, ConsultaImpresionMaterialesQxForm forma, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) throws IPSException {
    	
    	
    	HashMap criterios=new HashMap();
		criterios.put("solicitud", forma.getSolicitudesQx("numero_solicitud_"+forma.getIndex1()));
		forma.getConsumoM().put("noSolicitud", forma.getSolicitudesQx("numero_solicitud_"+forma.getIndex1()));
		boolean acto=UtilidadesSalas.esSolicitudCirugiaPorActo(con, Integer.parseInt(criterios.get("solicitud").toString()));
		String nombreArchivo="";
		Random r= new Random();
		nombreArchivo= "/consumoMateriales"+r.nextInt()+".pdf";
		
		for(int h=0;h<Integer.parseInt(forma.getConsumoM().get("numRegistros").toString());h++)
			forma.getConsumoM().put("band_"+h, ConstantesBD.acronimoNo);
		
		
		/******************************************
		 * OPCIONES DE IMPRESION
		 ******************************************/
		
		logger.info("\n\n\n\n\n [opciones de Impresion ] \n Costo:"+forma.getImpCosto()+"\n Tarifa:"+forma.getImpTarifa()+"\n Especiales:"+forma.getImpMaterialesEsp()+"\n\n\n\n\n");
		
		criterios.put("costo", forma.getImpCosto());
		criterios.put("tarifa", forma.getImpTarifa());
		criterios.put("materiales", forma.getImpMaterialesEsp());
		
		Utilidades.imprimirMapa(criterios);
		
		logger.info("\n\n\n\n\n archvio "+ValoresPorDefecto.getFilePath()+nombreArchivo);
    	if (acto)
    	{
    		/****************************************************************
    		 * Se genera el reporte con diseño segun consumo por acto. "itext"
    		 ***************************************************************/
    	
    		ConsultaImpresionConsumoMaterialesPdf.imprimirListadoActo(con, ValoresPorDefecto.getFilePath()+nombreArchivo, forma.getConsumoServiciosM(), paciente, usuario, forma.getConsumoM(), criterios, request);
    		UtilidadBD.closeConnection(con);
    		request.setAttribute("nombreArchivo", nombreArchivo);
    		request.setAttribute("nombreVentana", "Consulta Ajustes");
    		return mapping.findForward("abrirPdf");
    		
    	}
    	else
    	{
    		/****************************************************************
    		 * Se genera el reporte con diseño segun consumo por cirugia. "itext"
    		 ***************************************************************/
    		
    		Utilidades.imprimirMapa(forma.getConsumoM());
    		Utilidades.imprimirMapa(forma.getConsumoServiciosM());
    		
    		ConsultaImpresionConsumoMaterialesPdf.imprimirListadoCirugia(con, ValoresPorDefecto.getFilePath()+nombreArchivo, forma.getConsumoServiciosM(), paciente, usuario, forma.getConsumoM(), criterios, request);
    		UtilidadBD.closeConnection(con);
    		request.setAttribute("nombreArchivo", nombreArchivo);
    		request.setAttribute("nombreVentana", "Consulta Ajustes");
    		return mapping.findForward("abrirPdf");
    	}
    	
		
	}


	/**
	 * metodo encargado del ordenamiento del mapa ingresos
	 * @param forma
	 */

	public static void accionOrdenarMapa(ConsultaImpresionMaterialesQxForm forma)
	{
		String [] indicesMap = {"estado_",
								"via_ingreso_",
								"codigo_paciente_",
								"centro_atencion_",
								"consecutivo_" ,
								"fecha_ingreso_",
								"estado_cuenta_",
								"fecha_egreso_"
								};
		
		int numReg = Integer.parseInt(forma.getIngresosPacientes().get("numRegistros").toString());
		forma.setIngresosPacientes(Listado.ordenarMapa(indicesMap, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getIngresosPacientes(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.getIngresosPacientes().put("numRegistros",numReg);
		forma.getIngresosPacientes().put("INDICES_MAPA",indicesMap);
	}

    /**
	 * metodo encargado del ordenamiento del mapa solicitudes cirugia
	 * @param forma
	 */

	public static void accionOrdenarMapa1(ConsultaImpresionMaterialesQxForm forma)
	{
		String [] indicesMap = {"fecha_solicitud_",
								"numero_solicitud_",
								"centro_costo_",
								"estadohistoria_",
								"estadoing_",
								"tipoid_0",
								"centroatencion_2",
								"fechasol_2",
								"tipoid_",
								"centroatencion_",
								"fechasol_",
								"ingreso_1",
								"nombrepac_0",
								"ingreso_",
								"nombrepac_",
								"consecutivo_",
								"codigopac_"
								};
		
	    int numReg = Integer.parseInt(forma.getSolicitudesQx().get("numRegistros").toString());
		forma.setSolicitudesQx(Listado.ordenarMapa(indicesMap, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getSolicitudesQx(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.getSolicitudesQx().put("numRegistros",numReg);
		forma.getSolicitudesQx().put("INDICES_MAPA",indicesMap);
	}	
	
	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezar(ConsultaImpresionMaterialesQxForm forma, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) {
		
		
		return mapping.findForward("principal");
	}
	/**
     * Función que valida si el paciente se encuentra cargado
     * @param con
     * @param forma
     * @param usuario
     * @param paciente
     * @param request
     * @param errores
     * @return
     */
    private ActionErrors validarPacienteCargado(Connection con, ConsultaImpresionMaterialesQxForm forma, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionErrors errores) {
    	if(paciente.getCodigoPersona()<=0)
			errores.add("Paciente Cargado", new ActionMessage("errors.required","Paciente Cargado en Sesion"));
    	return errores;
	}

}
