package com.princetonsa.action.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.historiaClinica.ParametrizacionComponentesForm;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoCampoParametrizable;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.ParametrizacionComponentes;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.servinte.axioma.bl.historiaClinica.facade.HistoriaClinicaFacade;
import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoParametrizabDto;
import com.servinte.axioma.dto.historiaClinica.PlantillaComponenteDto;
import com.servinte.axioma.fwk.exception.IPSException;

public class ParametrizacionComponentesAction extends Action{
	/**
	 * 
	 */
	Logger logger =Logger.getLogger(ParametrizacionComponentesAction.class);
	
	/**
	 * 
	 */
	private static String[] indicesEscalas={"escala_","orden_","mostrar_","tiporegistro_"};
	
	private static String[] indicesOpciones={"opcion_","valor_","tiporegistro_"};
	
	private static String[] indicesSecciones={"codigoseccion_","nombreseccion_","columnaseccion_","ordenseccion_","tiposeccion_","sexoseccion_","edadinicial_","edadfinal_","indicativorestriccion_","tiporegistro_"};
	
	private static String[] indicesCampos={"codigocampo_","nombrecampo_","etiqueta_","tipocampo_","tamanocampo_","signocampo_","unidadcampo_","valorpredeterminado_","valorminimo_","valormaximo_","numerodecimales_","unicofila_","columnasocupadas_","ordencampo_","requeridocampo_","","tiporegistro_", "generaralerta_"};
	
	private static final String KEY_ERROR_NO_ESPECIFIC="errors.notEspecific";
	
	/**
	 * M�todo excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try {
			if (form instanceof ParametrizacionComponentesForm) 
			{
				ParametrizacionComponentesForm forma=(ParametrizacionComponentesForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con = UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				ParametrizacionComponentes mundo=new ParametrizacionComponentes();
				
				ActionErrors errores = new ActionErrors();

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptosCarteraAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset();
					forma.resetMensaje();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("volverPrincipal"))
				{
					forma.resetMensaje();
					forma.resetSeccion();
					forma.setMapaSignosVitales(mundo.consultarSignosVitales(con));
					forma.setMapaConsultaSecciones(mundo.consultaSecciones(con, forma.getComponente()));
					forma.setMapaConsultaEscalas(mundo.consultaEscalas(con, forma.getComponente()));
					forma.setMapaConsultaCampos(mundo.consultaCampos(con, forma.getComponente()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("consultarParametrizacion"))
				{
					//forma.reset();
					forma.resetMensaje();
					forma.resetProcesoExitoso();
					
					forma.setMapaSignosVitales(mundo.consultarSignosVitales(con));
					forma.setMapaConsultaSecciones(mundo.consultaSecciones(con, forma.getComponente()));
					forma.setMapaConsultaEscalas(mundo.consultaEscalas(con, forma.getComponente()));
					forma.setMapaConsultaCampos(mundo.consultaCampos(con, forma.getComponente()));
					
					//Curvas de Crecimiento
					HistoriaClinicaFacade historiaClinicaFacade = new HistoriaClinicaFacade();

					forma.setListaGraficaCurvasSeleccionadas(historiaClinicaFacade.consultarCurvasComponente(Integer.parseInt(forma.getComponente())));
					forma.setListaGraficaCurvas(historiaClinicaFacade.consultarCurvasDisponiblesComponente(Integer.parseInt(forma.getComponente())));
										
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("parametrizar"))
				{
					//forma.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("seleccion");
				}
				else if(estado.equals("adicionSeccion"))
				{
					//forma.reset();
					forma.resetMensaje();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("seccion");
				}
				else if(estado.equals("nuevaSeccion"))
				{
					//forma.reset();
					forma.resetMensaje();
					Utilidades.nuevoRegistroMapaGenerico(forma.getMapaSecciones(),indicesSecciones,"numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("seccion");
				}
				else if(estado.equals("nuevoCampo"))
				{
					//forma.reset();
					forma.resetMensaje();

					//Se resetea el campo mapaValoresGenerados x tarea 7742
					forma.setValoresAsociar("");
					forma.setMapaValoresGenerados(new HashMap());

					forma.setMapaOpciones(mundo.consultarOpciones(con, forma.getMapaCampos("codigopkcampo_0")+""));
					Utilidades.nuevoRegistroMapaGenerico(forma.getMapaCampos(),indicesCampos,"numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("campos");
				}
				else if(estado.equals("adicionEscala"))
				{
					//forma.reset();
					forma.resetMensaje();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("escalas");
				}
				else if(estado.equals("adicionCampos"))
				{
					//forma.reset();
					forma.resetMensaje();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("campos");
				}
				else if(estado.equals("nuevaEscala"))
				{
					//forma.reset();
					forma.resetMensaje();
					Utilidades.nuevoRegistroMapaGenerico(forma.getMapaEscalas(),indicesEscalas,"numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("escalas");
				}
				else if(estado.equals("guardarEscala"))
				{
					//forma.reset();
					this.accionGuardarEscala(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("escalas");
				}
				else if(estado.equals("guardarSeccion"))
				{
					//forma.reset();
					errores = validacionesSeccion(con, forma, mundo);
					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("seccion");
					}
					this.accionGuardarSeccion(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("seccion");
				}
				else if(estado.equals("guardarCampo"))
				{
					//forma.reset();
					errores = validacionesCampos(con, forma, mundo);
					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("campos");
					}
					this.accionGuardarCampo(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("campos");
				}
				else if(estado.equals("ingresoOpciones"))
				{
					//forma.reset();
					if (!forma.isPrimeraVezModificarOpciones())
					{
						forma.setMapaOpciones(mundo.consultarOpciones(con, forma.getMapaCampos("codigopkcampo_0")+""));
						forma.setPrimeraVezModificarOpciones(true);
					}

					UtilidadBD.closeConnection(con);
					return mapping.findForward("opciones");
				}
				else if(estado.equals("nuevaOpcion"))
				{
					//forma.reset();
					Utilidades.nuevoRegistroMapaGenerico(forma.getMapaOpciones(),indicesOpciones,"numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("opciones");
				}
				else if(estado.equals("guardarOpcion"))
				{
					//forma.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("opciones");
				}
				else if(estado.equals("detalleSeccion"))
				{
					//forma.reset();
					forma.resetMensaje();
					logger.info("\n\n\n\n\n [CodigoSeccion] >>>"+forma.getMapaConsultaSecciones("codigopkseccion_"+forma.getIndiceSeccion())+"");
					forma.setCodigoSeccion(forma.getMapaConsultaSecciones("codigopkseccion_"+forma.getIndiceSeccion())+"");
					forma.setMapaDetalleSeccion(mundo.consultaDetalleSeccion(con, forma.getMapaConsultaSecciones("codigopkseccion_"+forma.getIndiceSeccion())+""));
					forma.setMapaConsultaCamposSeccion(mundo.consultaCamposSeccion(con, forma.getMapaConsultaSecciones("codigopkseccion_"+forma.getIndiceSeccion())+""));
					forma.setMapaConsultaSecciones(mundo.consultaSubseccion(con, forma.getMapaConsultaSecciones("codigopkseccion_"+forma.getIndiceSeccion())+""));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleSeccion");
				}
				else if(estado.equals("detalleSeccion1"))
				{
					forma.resetSeccion();
					forma.resetMensaje();
					logger.info("\n\n\n\n\n<<< [CodigoSeccion] >>>"+forma.getCodigoSeccion());
					forma.setMapaDetalleSeccion(mundo.consultaDetalleSeccion(con, forma.getCodigoSeccion()));
					forma.setMapaConsultaCamposSeccion(mundo.consultaCamposSeccion(con, forma.getCodigoSeccion()));
					forma.setMapaConsultaSecciones(mundo.consultaSubseccion(con, forma.getCodigoSeccion()));

					//Agregado por tarea 6547
					forma.setPrimeraVezModificarValores(false);

					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleSeccion");
				}
				else if(estado.equals("cargarModificarSeccion"))
				{
					//forma.reset();
					forma.resetMensaje();
					forma.setMapaSecciones(mundo.consultaDetalleSeccion(con, forma.getMapaConsultaSecciones("codigopkseccion_"+forma.getIndiceSeccion())+""));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("seccion");
				}
				else if(estado.equals("cargarModificarSeccion1"))
				{
					//forma.reset();
					forma.resetMensaje();
					forma.setMapaSecciones(mundo.consultaDetalleSeccion(con, forma.getMapaConsultaSecciones("codigopkseccion_"+forma.getIndiceSeccion())+""));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("seccion");
				}			
				else if(estado.equals("detalleEscala"))
				{
					//forma.reset();
					forma.resetMensaje();
					forma.setMapaDetalleEscala(mundo.consultaDetalleEscala(con, forma.getSeccion()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleEscala");
				}
				else if(estado.equals("detalleCampo"))
				{
					//forma.reset();
					forma.resetMensaje();
					forma.setMapaDetalleCampo(mundo.consultaDetalleCampo(con, forma.getSeccion()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleCampo");
				}
				else if(estado.equals("modificarEscala"))
				{
					//forma.reset();
					this.accionModificarEscala(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleEscala");
				}
				else if(estado.equals("modificarSeccion"))
				{
					//forma.reset();
					this.accionModificarSeccion(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("seccion");
				}
				else if(estado.equals("modificarCampo"))
				{
					//forma.reset();
					this.accionModificarCampo(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("campos");
				}
				else if(estado.equals("cargarModificarCampoParam"))
				{
					//forma.reset();
					forma.resetMensaje();
					forma.setMapaCampos(mundo.consultaDetalleCampo(con, forma.getMapaConsultaCamposSeccion("codigopkcampo_"+forma.getIndexSeccionNivel2())+""));

					//Tambien consulto las opciones apra este campo, de modo que no saque la validacion que impide modificar - tarea 5303
					forma.setMapaOpciones(mundo.consultarOpciones(con, forma.getMapaCampos("codigopkcampo_0")+""));
					Utilidades.imprimirMapa(forma.getMapaOpciones());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("campos");
				}
				else if(estado.equals("consultaOpciones"))
				{
					//forma.reset();
					forma.setMapaOpciones(mundo.consultarOpciones(con, forma.getMapaDetalleCampo("codigopkcampo")+""));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("opciones");
				}
				else if(estado.equals("modificarOpcion"))
				{
					//forma.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("opciones");
				}
				else if(estado.equals("guardarSignos"))
				{
					this.accionModificarSignos(con, forma, mundo, usuario);
					forma.setMapaSignosVitales(mundo.consultarSignosVitales(con));
					forma.setMapaConsultaSecciones(mundo.consultaSecciones(con, forma.getComponente()));
					forma.setMapaConsultaEscalas(mundo.consultaEscalas(con, forma.getComponente()));
					forma.setMapaConsultaCampos(mundo.consultaCampos(con, forma.getComponente()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("eliminarSeccionParam"))
				{
					this.accionEliminarSeccionParam(con, forma, mundo, usuario, mapping);
					if(forma.getIndexNivel().equals("nivel1"))
					{

						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");

					}
					else
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("detalleSeccion");

					}
				}
				else if(estado.equals("eliminarCampoParam"))
				{
					this.accionEliminarCampoParam(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleSeccion");
				}
				else if(estado.equals("eliminarEscalaParam"))
				{
					this.accionEliminarEscalaParam(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("eliminarSeccion"))
				{
					//eliminar Secciones
					Utilidades.eliminarRegistroMapaGenerico(forma.getMapaSecciones(),forma.getMapaSecciones(),forma.getIndiceSeccionEliminar(),indicesSecciones,"numRegistros","tiporegistro_","BD",false);
					forma.setIndiceSeccionEliminar(ConstantesBD.codigoNuncaValido);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("seccion");
				}
				else if(estado.equals("eliminarEscala"))
				{
					//eliminar Escalas
					Utilidades.eliminarRegistroMapaGenerico(forma.getMapaEscalas(),forma.getMapaEscalas(),forma.getIndiceEscalaEliminar(),indicesEscalas,"numRegistros","tiporegistro_","BD",false);
					forma.setIndiceSeccionEliminar(ConstantesBD.codigoNuncaValido);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("escalas");
				}
				else if(estado.equals("eliminarCampos"))
				{
					//eliminar Escalas
					Utilidades.eliminarRegistroMapaGenerico(forma.getMapaCampos(),forma.getMapaCampos(),forma.getIndiceCampoEliminar(),indicesCampos,"numRegistros","tiporegistro_","BD",false);
					forma.setIndiceCampoEliminar(ConstantesBD.codigoNuncaValido);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("campos");
				}
				else if(estado.equals("eliminarOpciones"))
				{
					//eliminar Escalas
					Utilidades.eliminarRegistroMapaGenerico(forma.getMapaOpciones(),forma.getMapaOpciones(),forma.getIndiceOpcionesEliminar(),indicesOpciones,"numRegistros","tiporegistro_","BD",false);
					forma.setIndiceOpcionesEliminar(ConstantesBD.codigoNuncaValido);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("opciones");
				}
				//--------------------------------------------------------------
				//Estados Campos Tipo Formula 
				else if(estado.equals("cargarFormulaCampoParam"))
				{
					/*if (!(request.getParameter("esOperacionExitosa")+"").equals("") && !(request.getParameter("esOperacionExitosa")+"").equals("null"))
					 forma.setEsOperacionExitosa(request.getParameter("esOperacionExitosa").toString());*/				 
					metodoCargarFormulaCampoParam(con,forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("formula");
				}
				else if(estado.equals("elimarCaracter"))
				{				 
					metodoEliminarFormulaCampoParam(con,forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("formula");
				}
				else if(estado.equals("guardarDeclaracion"))
				{
					metodoGuardarDeclaracion(con,forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("formula");
				}
				else if(estado.equals("restringirCamposParam"))
				{
					metodoRestringirCamposParam(con,forma,true);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("campos");
				}
				else if(estado.equals("ingresoSeccionesAsoc"))
				{
					//return accionIngresarSeccionesAsociadas(con, forma, mundo, mapping, request);
					return accionModificarSeccionesAsociadas(con, forma, mundo, mapping, usuario, request);
				}
				else if(estado.equals("asociarSeccion"))
				{
					return accionSubirSeccionesAsociadas(con,forma,mapping,request,1);
				}
				else if(estado.equals("ingresoValoresAsoc"))
				{

					return accionModificarValoresModificados(con, forma, mundo, mapping, usuario, request);
					/*UtilidadBD.closeConnection(con);
				 return mapping.findForward("valoresAsociados");*/
				}
				else if(estado.equals("adicionarValores"))
				{
					return accionAdicionarValoresAsociados(con,forma,mapping,request,1);
				}
				else if(estado.equals("guardarAsociadas"))
				{
					//forma.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("seccionesAsociadas");
				}
				else if(estado.equals("guardarValores"))
				{
					//forma.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("valoresAsociados");
				}
				else if(estado.equals("modificarOrden"))
				{
					this.modificarOrdenCampos(con, forma, mundo, usuario);
					forma.setMapaConsultaCamposSeccion(mundo.consultaCamposSeccion(con, forma.getCodigoSeccion()));
					forma.setMapaConsultaSecciones(mundo.consultaSubseccion(con, forma.getCodigoSeccion()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleSeccion"); 
				}
				else if(estado.equals("modificarOrdenSeccion"))
				{
					this.modificarOrdenSeccion(con, forma, mundo, usuario);
					forma.setMapaSignosVitales(mundo.consultarSignosVitales(con));
					forma.setMapaConsultaSecciones(mundo.consultaSecciones(con, forma.getComponente()));
					forma.setMapaConsultaEscalas(mundo.consultaEscalas(con, forma.getComponente()));
					forma.setMapaConsultaCampos(mundo.consultaCampos(con, forma.getComponente()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("vistaPreviaComp"))
				{
					logger.info("Componente >> " + forma.getComponente());
					metodoCargarComponentePreview(con, forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("vistaPreviaComponente");
				}
				else if(estado.equals("agregarGraficaCurvas")){
					
					return accionAgregarGraficaCurvas(forma, mapping, request);
				}
				else if(estado.equals("guardarCurvas")){
					
					return accionGuardarCurva(forma, mapping, request);
				} 
				else if(estado.equals("eliminarCurvaComponente")){
					
					return accionEliminarCurvaComponente(forma, mapping, request);
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de PARAMETRIZACION COMPONENTES ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}		
			}
			else
			{
				logger.error("El form no es compatible con el form de ParametrizacionComponentesForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}
	
	

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void modificarOrdenSeccion(Connection con, ParametrizacionComponentesForm forma, ParametrizacionComponentes mundo, UsuarioBasico usuario) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		for(int i=0;i<Utilidades.convertirAEntero(forma.getMapaConsultaSecciones("numRegistros")+"");i++)
		{
			
			logger.info("ENTRO POR ACA >>>>> "+forma.getMapaConsultaSecciones("codigopkseccion_"+i));
			logger.info("VIENE POR ACA >>>>>  "+forma.getMapaConsultaSecciones("ordenseccion_"+i));
			//modificar
			HashMap vo=new HashMap();
			vo.put("codigo",forma.getMapaConsultaSecciones("codigopkseccion_"+i));
			vo.put("orden",forma.getMapaConsultaSecciones("ordenseccion_"+i));
			transaccion=mundo.modificarOrdenSecciones(con, vo);
		}
			
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void modificarOrdenCampos(Connection con, ParametrizacionComponentesForm forma, ParametrizacionComponentes mundo, UsuarioBasico usuario) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		for(int i=0;i<Utilidades.convertirAEntero(forma.getMapaConsultaCamposSeccion("numRegistros")+"");i++)
		{
			//modificar
			HashMap vo=new HashMap();
			vo.put("codigo",forma.getMapaConsultaCamposSeccion("codigopkcampo_"+i));
			vo.put("orden",forma.getMapaConsultaCamposSeccion("ordencampo_"+i));
			transaccion=mundo.modificarOrdenCampos(con, vo);
		}
		
		
			
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param band
	 * @return
	 */
	private ActionForward accionAdicionarValoresAsociados(Connection con, ParametrizacionComponentesForm forma, ActionMapping mapping, HttpServletRequest request, int band) 
	{
		
		forma.setNumValoresGenerados(forma.getNumValoresGenerados()+1);
		
		forma.setMapaValoresGenerados("codvalor_"+forma.getNumValoresGenerados()+"_"+forma.getIndexOpciones(), forma.getNumValoresGenerados());
		forma.setMapaValoresGenerados("checkcc_"+forma.getNumValoresGenerados()+"_"+forma.getIndexOpciones(), "1");
		forma.setMapaValoresGenerados("valor_"+forma.getNumValoresGenerados()+"_"+forma.getIndexOpciones(), forma.getValoresAsociar());
		forma.setMapaValoresGenerados("usado_"+forma.getNumValoresGenerados()+"_"+forma.getIndexOpciones(), ConstantesBD.acronimoNo);
		forma.setMapaValoresGenerados("numRegistros", forma.getNumValoresGenerados());
		
		/*if(band==1)
			forma.setEstado("nuevo");
		else
			forma.setEstado("modificar");*/
		
		forma.setValoresAsociar("");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("valoresAsociados");
		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param band
	 * @return
	 */
	private ActionForward accionSubirSeccionesAsociadas(Connection con, ParametrizacionComponentesForm forma, ActionMapping mapping, HttpServletRequest request, int band) 
	{
		String nombre="";
		for(int i=0;i<Integer.parseInt(forma.getMapaSeccionesAsociadas().get("numRegistros")+"");i++)
		{
			if(forma.getSeccionesAsociadas().equals(forma.getMapaSeccionesAsociadas().get("codigocomsec_"+i)+""))
			{
				nombre=forma.getMapaSeccionesAsociadas().get("descripcion_"+i)+"";
				forma.setMapaSeccionesAsociadas("selec_"+i, ConstantesBD.acronimoSi);
			}
		}
		forma.setNumSeccionesGeneradas(forma.getNumSeccionesGeneradas()+1);
		
		forma.setMapaSeccionesGeneradas("codseccion_"+forma.getNumSeccionesGeneradas(), forma.getSeccionesAsociadas());
		forma.setMapaSeccionesGeneradas("checkcc_"+forma.getNumSeccionesGeneradas(), "1");
		forma.setMapaSeccionesGeneradas("seccion_"+forma.getNumSeccionesGeneradas(), nombre);
		forma.setMapaSeccionesGeneradas("usado_"+forma.getNumSeccionesGeneradas(), ConstantesBD.acronimoNo);
		forma.setMapaSeccionesGeneradas("numRegistros", forma.getNumSeccionesGeneradas());
		
		/*if(band==1)
			forma.setEstado("nuevo");
		else
			forma.setEstado("modificar");*/
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("seccionesAsociadas");
		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionModificarValoresModificados(Connection con, ParametrizacionComponentesForm forma, ParametrizacionComponentes mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		
		
		if(!forma.isPrimeraVezModificarValores()
				|| forma.getValorIndexPrimeraVez()!=Utilidades.convertirAEntero(forma.getIndexOpciones()))
		{
			forma.setValorIndexPrimeraVez(Utilidades.convertirAEntero(forma.getIndexOpciones()));
			
			forma.setMapaValoresPrevioAsocio(mundo.consultarValoresGeneradas(con, forma.getMapaOpciones("codigopk_"+forma.getIndexOpciones())+""));
			int registros=0;
			if(!forma.getIndexOpciones().equals(""))
			{
				for(int i=0;i<Integer.parseInt(forma.getMapaValoresPrevioAsocio("numRegistros")+"");i++)
				{
					registros++;
					//Adiciono la posicion del elemento al que pertenece
					forma.setMapaValoresGenerados("checkcc_"+registros+"_"+forma.getIndexOpciones(), "1");
					forma.setMapaValoresGenerados("valor_"+registros+"_"+forma.getIndexOpciones(), forma.getMapaValoresPrevioAsocio("valor_"+i));
				}
				forma.setMapaValoresGenerados("numRegistros", registros);
				forma.setNumValoresGenerados(registros);
			}
			
			Utilidades.imprimirMapa(forma.getMapaValoresGenerados());
			forma.setPrimeraVezModificarValores(true);
		}
		UtilidadBD.closeConnection(con);
		
		return mapping.findForward("valoresAsociados");
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionModificarSeccionesAsociadas(Connection con, ParametrizacionComponentesForm forma, ParametrizacionComponentes mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		forma.setMapaSeccionesAsociadas(mundo.consultarSeccionesAsociadas(con, forma.getComponente()));
		forma.setMapaSeccionesPrevioAsocio(mundo.consultarSeccionesGeneradas(con, forma.getMapaOpciones("codigopk_"+forma.getIndexOpciones())+""));
		int registros=0;
		if(!forma.getIndexOpciones().equals(""))
		{
			for(int i=0;i<Integer.parseInt(forma.getMapaSeccionesPrevioAsocio("numRegistros")+"");i++)
			{
				registros++;
				forma.setMapaSeccionesGeneradas("checkcc_"+registros, "1");
				forma.setMapaSeccionesGeneradas("seccion_"+registros, forma.getMapaSeccionesPrevioAsocio("seccion_"+i));
				forma.setMapaSeccionesGeneradas("codseccion_"+registros, forma.getMapaSeccionesPrevioAsocio("codseccion_"+i));
				for(int z=0;z<Integer.parseInt(forma.getMapaSeccionesAsociadas("numRegistros")+"");z++)
				{
					if((forma.getMapaSeccionesAsociadas("codigocomsec_"+z)+"").equals(forma.getMapaSeccionesGeneradas("codseccion_"+registros)+""))
					{
						forma.setMapaSeccionesAsociadas("selec_"+z, ConstantesBD.acronimoSi);
					}
				}
			}
			forma.setMapaSeccionesGeneradas("numRegistros", registros);
			forma.setNumSeccionesGeneradas(registros);
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("seccionesAsociadas");
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionIngresarSeccionesAsociadas(Connection con, ParametrizacionComponentesForm forma, ParametrizacionComponentes mundo, ActionMapping mapping, HttpServletRequest request) 
	{
		forma.setMapaSeccionesAsociadas(mundo.consultarSeccionesAsociadas(con, forma.getComponente()));
		forma.setMapaSeccionesGeneradas(mundo.consultarSeccionesGeneradas(con, forma.getMapaOpciones("codigopk_"+forma.getIndexOpciones())+""));
		
		String nombre="";
		
		logger.info("Num Asoc. >>>>>>>>>>"+forma.getMapaSeccionesAsociadas().get("numRegistros"));
		logger.info("Num Gener. >>>>>>>>>>"+forma.getMapaSeccionesGeneradas("numRegistros"));
		
		for(int i=0;i<Integer.parseInt(forma.getMapaSeccionesAsociadas().get("numRegistros")+"");i++)
		{
			
			for(int j=0;j<(Integer.parseInt(forma.getMapaSeccionesGeneradas("numRegistros")+"")+1);j++)
			{
				
				logger.info("cod Asoc. >>>>>>>>>>"+forma.getMapaSeccionesAsociadas().get("codigocomsec_"+i));
				logger.info("cod Gener. >>>>>>>>>>"+forma.getMapaSeccionesGeneradas("codcc_"+j));
				if((forma.getMapaSeccionesAsociadas().get("codigocomsec_"+i)+"").equals(forma.getMapaSeccionesGeneradas("codseccion_"+j)+""))
				{
					nombre=forma.getMapaSeccionesAsociadas().get("descripcion_"+i)+"";
					forma.setMapaSeccionesAsociadas("selec_"+i, ConstantesBD.acronimoSi);
				}
			}	
		}
		/*forma.setNumSeccionesGeneradas(forma.getNumSeccionesGeneradas()+1);
		
		forma.setMapaSeccionesGeneradas("codcc_"+forma.getNumSeccionesGeneradas(), forma.getSeccionesAsociadas());
		forma.setMapaSeccionesGeneradas("checkcc_"+forma.getNumSeccionesGeneradas(), "1");
		forma.setMapaSeccionesGeneradas("centro_"+forma.getNumSeccionesGeneradas(), nombre);
		forma.setMapaSeccionesGeneradas("usado_"+forma.getNumSeccionesGeneradas(), ConstantesBD.acronimoNo);
		forma.setMapaSeccionesGeneradas("numRegistros", forma.getNumSeccionesGeneradas());
		
		/*if(band==1)
			forma.setEstado("nuevo");
		else
			forma.setEstado("modificar");*/
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("seccionesAsociadas");
		
	}
	
	
	 /**
	  * 
	  * @param con
	  * @param forma
	  */	
	 private void metodoCargarFormulaCampoParam(Connection con,ParametrizacionComponentesForm forma)
	 {	
		 //Carga la informaci�n de la formula
		 String formula = "";
		 forma.setFormulaMap(new HashMap());
		 forma.setFormulaMap("numRegistros","0");
		 forma.setFormulaMap("formula","");		 
		 String codigoPk = "";
		 int pos = ConstantesBD.codigoNuncaValido;
		 
		 ParametrizacionComponentes mundo= new ParametrizacionComponentes();
		 cargarCamposFormula(con,forma);				 
		 formula= mundo.consultarFormula(con, forma.getMapaCampos("codigopkcampo_"+forma.getIndexCampo())+"");
		 logger.info("valor de la formula >> "+formula);
		 
		 if(!formula.equals(""))
		 {
			 String [] formulaArray = formula.split(ConstantesBD.separadorSplit);			 
			 
			 for(int i=0; i<formulaArray.length; i++)
			 {
				forma.setFormulaMap("orden_"+i,i);
				forma.setFormulaMap("valor_"+i,formulaArray[i]);				
				forma.setFormulaMap("activo_"+i,ConstantesBD.acronimoSi);
				forma.setFormulaMap("tipo_"+i,tipoCaracterFormula(formulaArray[i]).toString());				
				
				if(forma.getFormulaMap("tipo_"+i).toString().equals("campos"))	
				{
					//logger.info("valor de la key >> "+formulaArray[i].replace("__",""));
					codigoPk = formulaArray[i].replace("__","");
					pos = getPosCampoFormula(forma,codigoPk);
										
					if(pos >= 0)					
						forma.setFormulaMap("descripcion_"+i,forma.getMapaCamposFormula().get("nombre_"+pos));					
					else
						forma.setFormulaMap("descripcion_"+i,"error");			
				}
				else if(forma.getFormulaMap("tipo_"+i).toString().equals("otrasConstantes"))
				{
					if(formulaArray[i].toString().equals("__"+ConstantesCamposParametrizables.edadPacienteDias+"__"))
						forma.setFormulaMap("descripcion_"+i, "Edad Pac. Dias");
					else if(formulaArray[i].toString().equals("__"+ConstantesCamposParametrizables.edadPacienteMeses+"__"))
						forma.setFormulaMap("descripcion_"+i, "Edad Pac. Meses");
					else if(formulaArray[i].toString().equals("__"+ConstantesCamposParametrizables.edadPacienteAnios+"__"))
						forma.setFormulaMap("descripcion_"+i, "Edad Pac. A�os");
				}
				else
					forma.setFormulaMap("descripcion_"+i,formulaArray[i]);				
			 }			
			 
			 forma.setFormulaMap("numRegistros",formulaArray.length);
		 }	
	 }
	 
	 /**
	  * 
	  * @param forma
	  * @param codigoPk
	  * @return
	  */
	 public int getPosCampoFormula(ParametrizacionComponentesForm forma, String codigoPk)
	 {
		 for(int i = 0; i < Integer.parseInt(forma.getMapaCamposFormula("numRegistros").toString()); i++)
			 if(forma.getMapaCamposFormula("codigopk_"+i).toString().equals(codigoPk))
				 return i;
		 
		 return ConstantesBD.codigoNuncaValido;
	 }
	
	
	 /**
	  * 
	  * @param con 
	 * @param forma
	  */
	 public void cargarCamposFormula(Connection con, ParametrizacionComponentesForm forma)
	 {
		 HashMap respuesta = new HashMap();
		 respuesta.put("numRegistros","0");
		 ParametrizacionComponentes mundo = new ParametrizacionComponentes();
		 		 
		 /*if(!forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo())).getCodigoPK().equals(""))		 
			 forma.setListCamposFormula(forma.getListCampoTemporal());		 
		 else
		 {			 
			 if(forma.getIndexNivel().equals("2"))
			 {		
				 forma.setListCamposFormula(forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).
				 	getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getCampos());						 
			 }
			 else if(forma.getIndexNivel().equals("3"))
			 {			 
				 forma.setListCamposFormula(forma.getPlantillaDto().getSeccionesFijasPos(Integer.parseInt(forma.getIndexSeccionFija())).
				 	getElementoSeccionPos(Integer.parseInt(forma.getIndexElemento())).getSeccionesPos(Integer.parseInt(forma.getIndexSeccionNivel2())).getCampos());						 				 
			 }			
		 }*/
		 
		 forma.setMapaCamposFormula(mundo.consultarCamposFormula(con, forma.getMapaDetalleSeccion("codigopkseccion_0")+""));
		 
	 }
	 
	 
	 /**
	  * 
	  * @param caracter
	  * @return
	  */ 
	 private String tipoCaracterFormula(String caracter)
	 {		 
		 
		 if(caracter.trim().equals("-") || 
			 caracter.trim().equals("+") || 
			 	caracter.trim().equals("/") || 
			 		caracter.trim().equals("*") || 
			 			caracter.trim().equals("^"))
			 return "operadores";
		 else if(caracter.trim().equals("(") || 
				 caracter.trim().equals(")"))
			 return "signos";
		 else if(caracter.trim().equals("__"+ConstantesCamposParametrizables.edadPacienteDias+"__") ||
				 caracter.trim().equals("__"+ConstantesCamposParametrizables.edadPacienteMeses+"__") ||
				 	caracter.trim().equals("__"+ConstantesCamposParametrizables.edadPacienteAnios+"__"))
			 return "otrasConstantes";
		 else if(caracter.startsWith("__") && 
				 caracter.toString().endsWith("__"))
			 return "campos";
		 else	 
			 return "constantes";			 
		
	 }
	 
	 
	 /**
	  * 
	  * @param con
	  * @param forma
	  */ 
	 private void metodoEliminarFormulaCampoParam(Connection con,ParametrizacionComponentesForm forma)
	 {					 
		 HashMap nuevaFormula = new HashMap(); 
		 int cont = 0; 
		 
		 //Ordenar el mapa
		 for(int i=0; i<Integer.parseInt(forma.getFormulaMap("numRegistros").toString()); i++)
		 {
			 nuevaFormula.put("orden_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("orden_"+i));
			 nuevaFormula.put("valor_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("valor_"+i));
			 nuevaFormula.put("activo_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("activo_"+i));
			 nuevaFormula.put("tipo_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("tipo_"+i));
			 nuevaFormula.put("descripcion_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("descripcion_"+i));
		 }
		 		 
		 for(int i=0; i<Integer.parseInt(forma.getFormulaMap("numRegistros").toString()); i++)
		 {
			 if(forma.getFormulaMap("activo_"+i).toString().equals(ConstantesBD.acronimoSi))
			 {				
				 nuevaFormula.put("orden_"+cont,cont);
				 nuevaFormula.put("valor_"+cont,forma.getFormulaMap("valor_"+i));
				 nuevaFormula.put("activo_"+cont,ConstantesBD.acronimoSi);
				 nuevaFormula.put("tipo_"+cont,forma.getFormulaMap("tipo_"+i));
				 nuevaFormula.put("descripcion_"+cont,forma.getFormulaMap("descripcion_"+i));
				 cont++;				
			 }
		 }
		 
		 nuevaFormula.put("formula","");		 
		 forma.setFormulaMap(nuevaFormula);
		 forma.setFormulaMap("numRegistros",cont);
		 
		 //Utilidades.imprimirMapa(forma.getFormulaMap());
	 }
	 
	 
	 /**
	  * 
	  * @param con
	  * @param forma
	  */
	 private void metodoGuardarDeclaracion(Connection con,ParametrizacionComponentesForm forma)
	 {
		 String formulaComprobacion = "";
		 String formula = "";
		 HashMap nuevaFormula = new HashMap();
		 boolean indicador = false;
		 Random rando = new Random();
		 int numRegistros = Integer.parseInt(forma.getFormulaMap("numRegistros").toString());
		 
		 //Ordenar el mapa
		 for(int i=0; i < numRegistros; i++)
		 {
			 nuevaFormula.put("orden_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("orden_"+i));
			 nuevaFormula.put("valor_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("valor_"+i));
			 nuevaFormula.put("activo_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("activo_"+i));
			 nuevaFormula.put("tipo_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("tipo_"+i));
			 nuevaFormula.put("descripcion_"+forma.getFormulaMap("orden_"+i),forma.getFormulaMap("descripcion_"+i));
		 }		 
		 		 
		 //Utilidades.imprimirMapa(nuevaFormula);
		 
		 //Recorre la formula parametrizada		 
		 for(int i = 0 ; i < numRegistros; i++)
		 {
			 indicador = false;
			 
			 //Validacion para cuando se de este caso = ConstanteCampo o CampoCampo o ConstanteConstante o CampoConstante
			 if(i>0 && (tipoCaracterFormula(nuevaFormula.get("valor_"+(i-1)).toString()).equals("campos") 
					 		|| tipoCaracterFormula(nuevaFormula.get("valor_"+(i-1)).toString()).equals("constantes")
					 		|| tipoCaracterFormula(nuevaFormula.get("valor_"+(i-1)).toString()).equals("otrasConstantes"))
					 			&& (tipoCaracterFormula(nuevaFormula.get("valor_"+i).toString()).equals("campos") 
					 					|| tipoCaracterFormula(nuevaFormula.get("valor_"+i).toString()).equals("constantes")
					 					|| tipoCaracterFormula(nuevaFormula.get("valor_"+i).toString()).equals("otrasConstantes")))
				 {
				 	logger.info("Validacion formula. Error No. 1. ConstanteCampo o CampoCampo o ConstanteConstante o CampoConstante");
				 	formulaComprobacion+="*/*/*";
				 	indicador = true;
				 }			 			 
			 
			 if(!indicador)
			 {				 
				 if(tipoCaracterFormula(nuevaFormula.get("valor_"+i).toString()).equals("campos")||tipoCaracterFormula(nuevaFormula.get("valor_"+i).toString()).equals("otrasConstantes"))
				 {
					 formulaComprobacion+=Math.abs(rando.nextInt(1000))+"";
				 }
				 else
					 formulaComprobacion+=nuevaFormula.get("valor_"+i).toString();
			 }
			 
			 formula += nuevaFormula.get("valor_"+i).toString()+ConstantesBD.separadorSplit;			 
		 } 
		 
		 nuevaFormula.put("formula",formula);		 
		 forma.setFormulaMap(nuevaFormula);
		 forma.setFormulaMap("numRegistros",numRegistros);
		 forma.setFormulaComprobar(formulaComprobacion);
		 logger.info("valor de la formula >> "+formula+" >> "+formulaComprobacion);
		 forma.setEstado("respuestaFormulaValidada");	
	 }
	 
	 
	 
	 /**
	  * 
	  * @param con
	  * @param forma
	  * @param esLLamadoPopUp
	  */
	 private void metodoRestringirCamposParam(Connection con,ParametrizacionComponentesForm forma,boolean esLLamadoPopUp)
		{
			 //Carga la informaci�n de la formula		 
			 DtoCampoParametrizable campoParam = new DtoCampoParametrizable();	
			 String [] formulaArray; 
			 
			 //Esta lleno solo cuando se va a modificar el campo que posee la formula
			 /*if(esLLamadoPopUp && !forma.getIndexCampo().equals(""))
				 logger.info("valor de la formula >> "+forma.getListCampoTemporalPos(Integer.parseInt(forma.getIndexCampo())).getFormulaCompleta());
			 
			 
			 //Inicializa todos los campos en Usado Formula 
			 for(int ca=0; ca<forma.getListCampoTemporal().size(); ca++)
				 forma.getListCampoTemporal().get(ca).setUsadoFormula("","", false);			 
			 
			 for(int ca=0; ca<forma.getListCampoTemporal().size(); ca++)
			 {
				campoParam = forma.getListCampoTemporal().get(ca);				
				 
				if(!campoParam.getFormulaCompleta().equals(""))
				{
					formulaArray = campoParam.getFormulaCompleta().split(ConstantesBD.separadorSplit);
				 
					for(int i=0; i<formulaArray.length; i++)
					{
						if(tipoCaracterFormula(formulaArray[i]).equals("campos"))
						{						
							forma.getListCampoTemporalCodigoPk(formulaArray[i].replace("__","")).setUsadoFormula(campoParam.getCodigoPK(),campoParam.getCodigo()+" - "+campoParam.getNombre(),true);							
						}
					}
				}
		 	}*/
			 //Actualiza el estado dependiendo si es la modificacion de un campo o la creacion de uno nuevo
			 if(esLLamadoPopUp && !forma.getIndexCampo().equals(""))
			 { 
				logger.info("entra mod>>>>>>>>>>>>>"+forma.getMapaCampos("codigopkcampo_"+forma.getIndexCampo()));
				 	
			 	if(!(forma.getMapaCampos("codigopkcampo_"+forma.getIndexCampo())+"").equals("")&&!(forma.getMapaCampos("codigopkcampo_"+forma.getIndexCampo())+"").equals("null")){
			 		logger.info("Estado: cargarModificarCampoParam");
			 		forma.setEstado("cargarModificarCampoParam");
			 	} else {
			 		logger.info("adicionCampos");
			 		forma.setEstado("adicionCampos");
			 	}
			 }
		}
	 
	 
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @return
	 */
	private ActionErrors validacionesSeccion(Connection con, ParametrizacionComponentesForm forma, ParametrizacionComponentes mundo) 
	{
		ActionErrors errores = new ActionErrors();
		
		
		int codigoComponente= mundo.consultarCodigoComponete(con, forma.getComponente());
		
		forma.setMapaSeccionesExistentes(mundo.consultarSeccionesExitentes(con, codigoComponente+""));
		
		
		logger.info("numRegistrosEx>>>>>>>>>>>"+forma.getMapaSeccionesExistentes("numRegistros"));
		
		logger.info("numRegistros>>>>>>>>>>>"+forma.getMapaSecciones("numRegistros"));
		
		for(int j=0; j<Utilidades.convertirAEntero(forma.getMapaSecciones("numRegistros")+"");j++)
		{
			for(int i=0; i<Utilidades.convertirAEntero(forma.getMapaSeccionesExistentes("numRegistros")+"");i++)
			{
				
				logger.info("mostrarMod>>>>>>>>>>>>>"+forma.getMapaSeccionesExistentes("mostrar_modificacion_"+i));
				
				if((forma.getMapaSeccionesExistentes("mostrar_modificacion_"+i)+"").equals(ConstantesBD.acronimoSi))
				{
					
					logger.info("codigoEx>>>>>>>>>>>>>"+forma.getMapaSeccionesExistentes("codigo_"+i));
					
					logger.info("codigo>>>>>>>>>>>>>"+forma.getMapaSecciones("codigoseccion_"+j));
					
					
					logger.info("descripcionEx>>>>>>>>>>>>>"+forma.getMapaSeccionesExistentes("descripcion_"+i));
					
					if(forma.getMapaSecciones("codigoseccion_"+j).equals(forma.getMapaSeccionesExistentes("codigo_"+i)+""))
					{
						errores.add("descripcion",new ActionMessage("errors.notEspecific","Ya existe una seccion creada con el codigo "+forma.getMapaSeccionesExistentes("codigo_"+i)+", por favor verifique."));
					}
					if(!forma.getMapaSeccionesExistentes("descripcion_"+i).equals(""))
					{
						
						logger.info("descripcionEx>>>>>>>>>>>>>"+forma.getMapaSeccionesExistentes("descripcion_"+i));
						
						logger.info("descripcion>>>>>>>>>>>>>"+forma.getMapaSecciones("nombreseccion_"+j));
						
						if(forma.getMapaSecciones("nombreseccion_"+j).equals(forma.getMapaSeccionesExistentes("descripcion_"+i)+""))
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Ya existe una seccion creada con el nombre "+forma.getMapaSeccionesExistentes("descripcion_"+i)+", por favor verifique."));
						}
						
					}
					
				}
				
				
			}
		}
		
		return errores;
	}

	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @return
	 */
	private ActionErrors validacionesCampos(Connection con, ParametrizacionComponentesForm forma, ParametrizacionComponentes mundo) 
	{
		ActionErrors errores = new ActionErrors();
		
		
		int codigoComponente= mundo.consultarCodigoComponete(con, forma.getComponente());
		
		forma.setMapaCamposExistentes(mundo.consultarCamposExitentes(con, codigoComponente+""));
		
		
		logger.info("numRegistrosEx>>>>>>>>>>>"+forma.getMapaCamposExistentes("numRegistros"));
		
		logger.info("numRegistros>>>>>>>>>>>"+forma.getMapaCampos("numRegistros"));
		
		for(int j=0; j<Utilidades.convertirAEntero(forma.getMapaCampos("numRegistros")+"");j++)
		{
			for(int i=0; i<Utilidades.convertirAEntero(forma.getMapaCamposExistentes("numRegistros")+"");i++)
			{
				
				logger.info("mostrarMod>>>>>>>>>>>>>"+forma.getMapaCamposExistentes("mostrarmodificacion_"+i));
				
				if((forma.getMapaCamposExistentes("mostrarmodificacion_"+i)+"").equals(ConstantesBD.acronimoSi))
				{
					
					logger.info("codigoEx>>>>>>>>>>>>>"+forma.getMapaCamposExistentes("codigo_"+i));
					
					logger.info("codigo>>>>>>>>>>>>>"+forma.getMapaCampos("codigocampo_"+j));
					
					
					logger.info("descripcionEx>>>>>>>>>>>>>"+forma.getMapaCamposExistentes("nombre_"+i));
					
					if(forma.getMapaCampos("codigocampo_"+j).equals(forma.getMapaCamposExistentes("codigo_"+i)+""))
					{
						errores.add("descripcion",new ActionMessage("errors.notEspecific","Ya existe un campo creado con el codigo "+forma.getMapaCamposExistentes("codigo_"+i)+", por favor verifique."));
					}
					if(!forma.getMapaCamposExistentes("nombre_"+i).equals(""))
					{
						
						logger.info("descripcionEx>>>>>>>>>>>>>"+forma.getMapaCamposExistentes("nombre_"+i));
						
						logger.info("descripcion>>>>>>>>>>>>>"+forma.getMapaCampos("nombrecampo_"+j));
						
						if(forma.getMapaCampos("nombrecampo_"+j).equals(forma.getMapaCamposExistentes("nombre_"+i)+""))
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Ya existe un campo creado con el nombre "+forma.getMapaCamposExistentes("nombre_"+i)+", por favor verifique."));
						}
						
					}
				}
				
			}
		}
		
		return errores;
	}
	
	
	

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 */
	private void accionEliminarSeccionParam(Connection con, ParametrizacionComponentesForm forma, ParametrizacionComponentes mundo, UsuarioBasico usuario, ActionMapping mapping) 
	{
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		if(forma.getIndexNivel().equals("nivel1"))
		{
			
			int numRegistros= Utilidades.convertirAEntero(forma.getMapaConsultaSecciones("numRegistros")+"");
			
			for(int i=0; i<numRegistros; i++)
			{
			
				//Modificar Mostrar Modificacion
				HashMap vo=new HashMap();
				vo.put("codigopk",forma.getMapaConsultaSecciones("codigopkseccion_"+forma.getIndiceSeccion()));
				vo.put("mostrar",ConstantesBD.acronimoSi);
				vo.put("mostrar_modificacion",ConstantesBD.acronimoNo);
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				transaccion=mundo.modificarMostrarSeccion(con, vo);
				
			}
			
			forma.setMapaConsultaSecciones(mundo.consultaSecciones(con, forma.getComponente()));
			
			
			
		}
		if(forma.getIndexNivel().equals("nivel2"))
		{
			
			int numRegistros= Utilidades.convertirAEntero(forma.getMapaConsultaSecciones("numRegistros")+"");
			
			for(int i=0; i<numRegistros; i++)
			{
			
				//Modificar Mostrar Modificacion
				HashMap vo=new HashMap();
				vo.put("codigopk",forma.getMapaConsultaSecciones("codigopkseccion_"+forma.getIndiceSeccionEliminar()));
				vo.put("mostrar",ConstantesBD.acronimoSi);
				vo.put("mostrar_modificacion",ConstantesBD.acronimoNo);
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				transaccion=mundo.modificarMostrarSeccion(con, vo);
				
				
			}
			
			Utilidades.eliminarRegistroMapaGenerico(forma.getMapaConsultaSecciones(),forma.getMapaConsultaSecciones(),forma.getIndiceCampo(),indicesCampos,"numRegistros","tiporegistro_","BD",false);
			
		}
		
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
		
		
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionEliminarEscalaParam(Connection con, ParametrizacionComponentesForm forma, ParametrizacionComponentes mundo, UsuarioBasico usuario) 
	{
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		int numRegistros= Utilidades.convertirAEntero(forma.getMapaConsultaEscalas("numRegistros")+"");
		
		for(int i=0; i<numRegistros; i++)
		{
			
			//Modificar Mostrar Modificacion
			HashMap vo=new HashMap();
			vo.put("componente",forma.getMapaConsultaEscalas("codigocomponente_"+forma.getIndiceEscala()));
			vo.put("mostrar",ConstantesBD.acronimoSi);
			vo.put("mostrar_modificacion",ConstantesBD.acronimoNo);
			vo.put("escala",forma.getMapaConsultaEscalas("codigopkescala_"+forma.getIndiceEscala()));
			transaccion=mundo.modificarMostrarEscala(con, vo);
		
		}
		
		forma.setMapaConsultaEscalas(mundo.consultaEscalas(con, forma.getComponente()));
		
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionEliminarCampoParam(Connection con, ParametrizacionComponentesForm forma, ParametrizacionComponentes mundo, UsuarioBasico usuario) 
	{
		
		
		int numRegistros= Utilidades.convertirAEntero(forma.getMapaConsultaCamposSeccion("numRegistros")+"");
		
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		
		
		for(int i=0; i<numRegistros; i++)
		{
			
			HashMap vo=new HashMap();
			vo.put("codigopk",forma.getMapaConsultaCamposSeccion("codigopkcampo_"+forma.getIndiceCampo()));
			vo.put("mostrar",ConstantesBD.acronimoSi);
			vo.put("mostrar_modificacion",ConstantesBD.acronimoNo);
			vo.put("usuario_modifica",usuario.getLoginUsuario());
			transaccion=mundo.modificarMostrarCampos(con, vo);
			
		
		}	
		
		Utilidades.eliminarRegistroMapaGenerico(forma.getMapaConsultaCamposSeccion(),forma.getMapaConsultaCamposSeccion(),forma.getIndiceCampo(),indicesCampos,"numRegistros","tiporegistro_","BD",false);
		
		
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
		
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionModificarSignos(Connection con, ParametrizacionComponentesForm forma, ParametrizacionComponentes mundo, UsuarioBasico usuario) 
	{
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		
		for(int i=0;i<Utilidades.convertirAEntero(forma.getMapaSignosVitales("numRegistros")+"");i++)
		{
			//modificar
			HashMap vo=new HashMap();
			vo.put("codigo",forma.getMapaSignosVitales("codigosigno_"+i));
			vo.put("orden",forma.getMapaSignosVitales("ordensigno_"+i));
			vo.put("visible",forma.getMapaSignosVitales("activosigno_"+i));
			transaccion=mundo.modificarSignos(con, vo);
		}
		
		for(int i=0;i<Utilidades.convertirAEntero(forma.getMapaConsultaSecciones("numRegistros")+"");i++)
		{
			//modificar
			HashMap vo=new HashMap();
			vo.put("codigo",forma.getMapaConsultaSecciones("codigopkseccion_"+i));
			vo.put("orden",forma.getMapaConsultaSecciones("ordenseccion_"+i));
			transaccion=mundo.modificarOrdenSecciones(con, vo);
		}
			
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionModificarEscala(Connection con, ParametrizacionComponentesForm forma, ParametrizacionComponentes mundo, UsuarioBasico usuario) 
	{
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		//modificar
		//transaccion=mundo.modificarEscala(con, (forma.getMapaDetalleEscala("componente")+""), (forma.getMapaDetalleEscala("escala")+""), (forma.getMapaDetalleEscala("ordenescala")+""), (forma.getMapaDetalleEscala("mostrarescala")+""), (forma.getMapaDetalleEscala("escalamod")+""));
		
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
	}

		

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarCampo(Connection con, ParametrizacionComponentesForm forma, ParametrizacionComponentes mundo, UsuarioBasico usuario) 
	{
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		int codigoComponente= mundo.consultarCodigoComponete(con, forma.getComponente());
		
		int numRegistros= Utilidades.convertirAEntero(forma.getMapaCampos("numRegistros")+"");
		
		int codigoSeccion=ConstantesBD.codigoNuncaValido;
		
		if(codigoComponente>0)
		{	
			
			codigoSeccion= Utilidades.convertirAEntero(forma.getMapaDetalleSeccion("codigopkseccion_0")+"");
			
			
			if(codigoSeccion>0)
			{
				int codigoCompSeccion=mundo.consultarCodigoCompSeccion(con, codigoComponente, codigoSeccion);
				
				if(codigoCompSeccion>0)
				{
					
					for(int i=0; i<numRegistros; i++)
					{
					
						int tipoCampo= Utilidades.convertirAEntero(forma.getMapaCampos("tipocampo_"+i)+"");
						
						//insertar
						HashMap vo=new HashMap();
						vo.put("codigo",forma.getMapaCampos("codigocampo_"+i));
						vo.put("nombre",forma.getMapaCampos("nombrecampo_"+i));
						vo.put("etiqueta",forma.getMapaCampos("etiqueta_"+i));
						vo.put("tipo",forma.getMapaCampos("tipocampo_"+i));
						vo.put("tamanio",forma.getMapaCampos("tamanocampo_"+i));
						vo.put("signo",forma.getMapaCampos("signocampo_"+i));
						vo.put("unidad",forma.getMapaCampos("unidadcampo_"+i));
						vo.put("valor_predeterminado",forma.getMapaCampos("valorpredeterminado_"+i));
						vo.put("maximo",forma.getMapaCampos("valormaximo_"+i));
						vo.put("minimo",forma.getMapaCampos("valorminimo_"+i));
						vo.put("decimales",forma.getMapaCampos("numerodecimales_"+i));
						vo.put("columnas_ocupadas",forma.getMapaCampos("columnasocupadas_"+i));
						vo.put("orden",forma.getMapaCampos("ordencampo_"+i));
						vo.put("unico_fila",forma.getMapaCampos("unicofila_"+i));
						vo.put("requerido",forma.getMapaCampos("requeridocampo_"+i));
						vo.put("formula",forma.getMapaCampos("formula_"+i));
						vo.put("alerta",forma.getMapaCampos("generaralerta_"+i));
						
						if((tipoCampo==ConstantesCamposParametrizables.tipoCampoCaracter)||(tipoCampo==ConstantesCamposParametrizables.tipoCampoNumericoEntero)||(tipoCampo==ConstantesCamposParametrizables.tipoCampoNumericoDecimal)||(tipoCampo==ConstantesCamposParametrizables.tipoCampoFecha)||(tipoCampo==ConstantesCamposParametrizables.tipoCampoHora)||(tipoCampo==ConstantesCamposParametrizables.tipoCampoFormula))
						{
							vo.put("tipo_html",ConstantesCamposParametrizables.campoTipoText);
						}
						else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoAreaTexto)
						{
							vo.put("tipo_html",ConstantesCamposParametrizables.campoTipoTextArea);
						}
						else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoChequeo)
						{
							String tipoCampoChequeo=forma.getMapaCampos("excluyente_"+i)+"";
							vo.put("tipo_html",tipoCampoChequeo);
						}
						else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoSeleccion)
						{
							vo.put("tipo_html",ConstantesCamposParametrizables.campoTipoSelect);
						}
						else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoArchivo)
						{
							vo.put("tipo_html",ConstantesCamposParametrizables.campoTipoARCH);
						}
						else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoTextoPredeterminado)
						{
							vo.put("tipo_html",ConstantesCamposParametrizables.campoTipoLabel);
						}
					
						vo.put("mostrar",ConstantesBD.acronimoSi);
						vo.put("mostrar_modificacion",ConstantesBD.acronimoSi);
						vo.put("institucion",usuario.getCodigoInstitucion());
						vo.put("usuario_modifica",usuario.getLoginUsuario());
						int codigoCampo=mundo.insertarCampo(con, vo);
						if(codigoCampo>0)
						{
							for(int j=0;j<Integer.parseInt(forma.getMapaOpciones("numRegistros")+"");j++)
							{
								//modificar
								vo=new HashMap();
								vo.put("campo_parametrizable",codigoCampo);
								vo.put("opcion",forma.getMapaOpciones("opcion_"+j));
								vo.put("valor",forma.getMapaOpciones("valor_"+j));
								vo.put("institucion",usuario.getCodigoInstitucion());
								vo.put("usuario_modifica",usuario.getLoginUsuario());
								int codigoOpcion=mundo.insertarOpciones(con, vo);
								if(codigoOpcion>0)
								{
									for(int k=1;k<=Utilidades.convertirAEntero(forma.getMapaSeccionesGeneradas("numRegistros")+"");k++)
									{
										if((forma.getMapaSeccionesGeneradas("checkcc_"+k)+"").equals("1"))
										{	
											vo=new HashMap();
											vo.put("codigo_opcion", codigoOpcion);
											vo.put("componente_seccion", forma.getMapaSeccionesGeneradas("codseccion_"+k));
											vo.put("mostrar_modificacion", ConstantesBD.acronimoSi);
											vo.put("usuario_modifica", usuario.getLoginUsuario());
											vo.put("fecha_modifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
											vo.put("hora_modifica", UtilidadFecha.getHoraActual());
											transaccion=mundo.insertarSeccionesAsocidas(con, vo);
										}	
									}
									for(int t=1;t<=Utilidades.convertirAEntero(forma.getMapaValoresGenerados("numRegistros")+"");t++)
									{
										for(int val=0;val<=Utilidades.convertirAEntero(forma.getMapaValoresGenerados("numRegistros")+"");val++)
										{
											if(forma.getMapaValoresGenerados().containsKey("checkcc_"+t+"_"+val)&&j==val)
											{	
												if (forma.getMapaValoresGenerados("checkcc_"+t+"_"+val).equals("1"))
												{
													vo=new HashMap();
													vo.put("codigo_opcion", codigoOpcion);
													vo.put("valor", forma.getMapaValoresGenerados("valor_"+t+"_"+val));
													vo.put("mostrar_modificacion", ConstantesBD.acronimoSi);
													vo.put("usuario_modifica", usuario.getLoginUsuario());
													vo.put("fecha_modifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
													vo.put("hora_modifica", UtilidadFecha.getHoraActual());
													transaccion=mundo.insertarValoresAsociados(con, vo);
												}
											}
										}
									}
								}
							}
							transaccion=mundo.insertarCompCamposSec(con, codigoCompSeccion, codigoCampo);
						}
						else
						{
							transaccion=false;
						}
					}	
					
				}
				else
				{
					transaccion=false;
				}
			}
			
		}
		else
		{
			transaccion=false;
		}
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
			forma.getMapaCampos().put("numRegistros", "0");
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarEscala(Connection con, ParametrizacionComponentesForm forma, ParametrizacionComponentes mundo, UsuarioBasico usuario) 
	{
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		int codigoComponente= mundo.consultarCodigoComponete(con, forma.getComponente());
		
		///////
		//forma.setMapaEscalaModificar(mundo.consultaEscalasModificar(con, forma.getComponente()));
		
		if(codigoComponente<0)
		{
			
			codigoComponente=mundo.insertarComponente(con, forma.getComponente(), usuario.getCodigoInstitucion(), usuario.getLoginUsuario());
			
		}
		
		if(codigoComponente>0)
		{	
			
			for(int i=0;i<Integer.parseInt(forma.getMapaEscalas("numRegistros")+"");i++)
			{
				
					
				logger.info(">>>>>Esc>>>"+forma.getMapaEscalas("escala_"+i));
				
				logger.info(">>>>>Info>>>"+mundo.consultaEscalasModificar(con, codigoComponente, forma.getMapaEscalas("escala_"+i)+""));
				
				if(mundo.consultaEscalasModificar(con, codigoComponente, forma.getMapaEscalas("escala_"+i)+""))
				{
					
					HashMap vo= new HashMap();
					vo.put("escala", forma.getMapaEscalas("escala_"+i));
					vo.put("componente", codigoComponente);
					vo.put("mostrar_modificacion", ConstantesBD.acronimoSi);
					transaccion=mundo.modificarEscala(con, vo);
					
				}
				else
				{	//insertar
					HashMap vo=new HashMap();
					vo.put("escala",forma.getMapaEscalas("escala_"+i));
					vo.put("componente",codigoComponente);
					vo.put("orden",forma.getMapaEscalas("orden_"+i));
					vo.put("mostrar",ConstantesBD.acronimoSi);
					vo.put("mostrar_modificacion",ConstantesBD.acronimoSi);
					transaccion=mundo.insertarEscala(con, vo);
				}	
			}
		}
		else
		{
			transaccion=false;
		}
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
	}

	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionModificarCampo(Connection con, ParametrizacionComponentesForm forma, ParametrizacionComponentes mundo, UsuarioBasico usuario) 
	{
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		int numRegistros= Utilidades.convertirAEntero(forma.getMapaCampos("numRegistros")+"");
		
		logger.info("888NUMERO DE REGISTROS--->"+numRegistros);
		
		for(int i=0; i<numRegistros; i++)
		{
			logger.info("indice i ="+ i);
			if((forma.getMapaCampos("nombrecampo_"+i)+"").equals(forma.getMapaCampos("nombreantiguo_"+i)+"")&&(forma.getMapaCampos("tipocampo_"+i)+"").equals(forma.getMapaCampos("tipoantiguo_"+i)+"")&&(forma.getMapaCampos("etiqueta_"+i)+"").equals(forma.getMapaCampos("etiquetaantigua_"+i)+"")&&(forma.getMapaCampos("unidadcampo_"+i)+"").equals(forma.getMapaCampos("unidadantigua_"+i)+""))
			{
				logger.info("if 1");
				int tipoCampo= Utilidades.convertirAEntero(forma.getMapaCampos("tipocampo_"+i)+"");
				
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getMapaCampos("codigocampo_"+i));
				vo.put("codigopk",forma.getMapaCampos("codigopkcampo_"+i));
				vo.put("nombre",forma.getMapaCampos("nombrecampo_"+i));
				vo.put("etiqueta",forma.getMapaCampos("etiqueta_"+i));
				vo.put("tipo",forma.getMapaCampos("tipocampo_"+i));
				vo.put("tamanio",forma.getMapaCampos("tamanocampo_"+i));
				vo.put("signo",forma.getMapaCampos("signocampo_"+i));
				vo.put("unidad",forma.getMapaCampos("unidadcampo_"+i));
				vo.put("valor_predeterminado",forma.getMapaCampos("valorpredeterminado_"+i));
				vo.put("maximo",forma.getMapaCampos("maximo_"+i));
				vo.put("minimo",forma.getMapaCampos("minimo_"+i));
				vo.put("decimales",forma.getMapaCampos("decimales_"+i));
				vo.put("columnas_ocupadas",forma.getMapaCampos("columnasocupadas_"+i));
				vo.put("orden",forma.getMapaCampos("ordencampo_"+i));
				vo.put("unico_fila",forma.getMapaCampos("unicofila_"+i));
				vo.put("requerido",forma.getMapaCampos("requeridocampo_"+i));
				vo.put("formula",forma.getFormulaMap("formula"));
				vo.put("alerta",forma.getMapaCampos("generaralerta_"+i));
				
				logger.info("mapaCampos(excluyente_"+i+"):  "+forma.getMapaCampos("excluyente_"+i));
			
				
				if((tipoCampo==ConstantesCamposParametrizables.tipoCampoCaracter)||(tipoCampo==ConstantesCamposParametrizables.tipoCampoNumericoEntero)||(tipoCampo==ConstantesCamposParametrizables.tipoCampoNumericoDecimal)||(tipoCampo==ConstantesCamposParametrizables.tipoCampoFecha)||(tipoCampo==ConstantesCamposParametrizables.tipoCampoHora)||(tipoCampo==ConstantesCamposParametrizables.tipoCampoFormula))
				{
					vo.put("tipo_html",ConstantesCamposParametrizables.campoTipoText);
				}
				else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoAreaTexto)
				{
					vo.put("tipo_html",ConstantesCamposParametrizables.campoTipoTextArea);
				}
				else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoChequeo)
				{
					vo.put("tipo_html",forma.getMapaCampos("excluyente_"+i).toString());
				}
				else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoSeleccion)
				{
					vo.put("tipo_html",ConstantesCamposParametrizables.campoTipoSelect);
				}
				else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoArchivo)
				{
					vo.put("tipo_html",ConstantesCamposParametrizables.campoTipoARCH);
				}
				else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoTextoPredeterminado)
				{
					vo.put("tipo_html",ConstantesCamposParametrizables.campoTipoLabel);
				}
				vo.put("mostrar",ConstantesBD.acronimoSi);
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				
				logger.info("VA A INSERTAR CAMPO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! --");
				Utilidades.imprimirMapa(vo);
				
				transaccion=mundo.modificarCampo(con, vo);
				
				logger.info("INSERTO CAMPO............................................");
				
				logger.info("NUMERO REGISTRO MAPA OPCIONES--->"+forma.getMapaOpciones("numRegistros"));
				
				Utilidades.imprimirMapa(forma.getMapaOpciones());
				
				double codigoOpcion=ConstantesBD.codigoNuncaValidoDouble;
				
				for(int j=0;j<Integer.parseInt(forma.getMapaOpciones("numRegistros")+"");j++)
				{
					logger.info("modificar");
					//modificar
					vo=new HashMap();
					vo.put("campo_parametrizable",forma.getMapaOpciones("campoparametrizable_"+j));
					vo.put("codigo_pk",forma.getMapaOpciones("codigopk_"+j));
					vo.put("opcion",forma.getMapaOpciones("opcion_"+j));
					vo.put("valor",forma.getMapaOpciones("valor_"+j));
					vo.put("institucion",usuario.getCodigoInstitucion());
					vo.put("usuario_modifica",usuario.getLoginUsuario());
					if(UtilidadTexto.isEmpty(vo.get("codigo_pk")+""))
					{
						vo.put("campo_parametrizable",forma.getMapaCampos("codigopkcampo_"+i));
						codigoOpcion = mundo.insertarOpciones(con, vo); 
						
						
						//Cambio TAREA 143221
						mundo.eliminarValoresAsociados(con, codigoOpcion);				
						if(codigoOpcion>0)
						{
							for(int t=1;t<=Utilidades.convertirAEntero(forma.getMapaValoresGenerados("numRegistros")+"");t++)
							{
								for(int val=0;val<=Utilidades.convertirAEntero(forma.getMapaValoresGenerados("numRegistros")+"");val++)
								{
									if(forma.getMapaValoresGenerados().containsKey("checkcc_"+t+"_"+val))
									{	

										if (forma.getMapaValoresGenerados("checkcc_"+t+"_"+val).equals("1")&&j==val)
										{
											vo=new HashMap();
											vo.put("codigo_opcion", codigoOpcion);
											vo.put("valor", forma.getMapaValoresGenerados("valor_"+t+"_"+val));
											vo.put("mostrar_modificacion", ConstantesBD.acronimoSi);
											vo.put("usuario_modifica", usuario.getLoginUsuario());
											vo.put("fecha_modifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
											vo.put("hora_modifica", UtilidadFecha.getHoraActual());
											transaccion=mundo.insertarValoresAsociados(con, vo);
										}
									}
								}
							}
						}
						//Fin Cambio TAREA 143221
					}
					else
					{
						transaccion=mundo.modificarOpciones(con, vo);
						codigoOpcion=Utilidades.convertirADouble(vo.get("codigo_pk")+"");
						
						
						//Cambio TAREA 143221
						mundo.eliminarValoresAsociados(con, codigoOpcion);				
						if(codigoOpcion>0)
						{
							for(int t=1;t<=Utilidades.convertirAEntero(forma.getMapaValoresGenerados("numRegistros")+"");t++)
							{
								for(int val=0;val<=Utilidades.convertirAEntero(forma.getMapaValoresGenerados("numRegistros")+"");val++)
								{
									if(forma.getMapaValoresGenerados().containsKey("checkcc_"+t+"_"+val))
									{	

										if (forma.getMapaValoresGenerados("checkcc_"+t+"_"+val).equals("1")&&j==val)
										{
											vo=new HashMap();
											vo.put("codigo_opcion", codigoOpcion);
											vo.put("valor", forma.getMapaValoresGenerados("valor_"+t+"_"+val));
											vo.put("mostrar_modificacion", ConstantesBD.acronimoSi);
											vo.put("usuario_modifica", usuario.getLoginUsuario());
											vo.put("fecha_modifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
											vo.put("hora_modifica", UtilidadFecha.getHoraActual());
											transaccion=mundo.insertarValoresAsociados(con, vo);
										}
									}
								}
							}
						}
					

						//FIN CAMBIO TAREA 143221	
					}
						
				}
			}
			else
			{
				logger.info("else");
				HashMap vo=new HashMap();
				
				vo.put("codigopk",forma.getMapaCampos("codigopkcampo_"+i));
				vo.put("mostrar",ConstantesBD.acronimoSi);
				vo.put("mostrar_modificacion",ConstantesBD.acronimoNo);
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				transaccion=mundo.modificarMostrarCampos(con, vo);
				
				
				int codigoComponente= mundo.consultarCodigoComponete(con, forma.getComponente());
				
				int codigoSeccion=ConstantesBD.codigoNuncaValido;
				
				if(codigoComponente>0)
				{	
					
					codigoSeccion= Utilidades.convertirAEntero(forma.getMapaDetalleSeccion("codigopkseccion_0")+"");
					
					
					if(codigoSeccion>0)
					{
						int codigoCompSeccion= mundo.consultarCodigoCompSeccion(con, codigoComponente, codigoSeccion);
						
						if(codigoCompSeccion>0)
						{
							
							int tipoCampo= Utilidades.convertirAEntero(forma.getMapaCampos("tipocampo_"+i)+"");
							
							//insertar
							vo=new HashMap();
							vo.put("codigo",forma.getMapaCampos("codigocampo_"+i));
							vo.put("nombre",forma.getMapaCampos("nombrecampo_"+i));
							vo.put("etiqueta",forma.getMapaCampos("etiqueta_"+i));
							vo.put("tipo",forma.getMapaCampos("tipocampo_"+i));
							vo.put("tamanio",forma.getMapaCampos("tamanocampo_"+i));
							vo.put("signo",forma.getMapaCampos("signocampo_"+i));
							vo.put("unidad",forma.getMapaCampos("unidadcampo_"+i));
							vo.put("valor_predeterminado",forma.getMapaCampos("valorpredeterminado_"+i));
							vo.put("maximo",forma.getMapaCampos("valormaximo_"+i));
							vo.put("minimo",forma.getMapaCampos("valorminimo_"+i));
							vo.put("decimales",forma.getMapaCampos("numerodecimales_"+i));
							vo.put("columnas_ocupadas",forma.getMapaCampos("columnasocupadas_"+i));
							vo.put("orden",forma.getMapaCampos("ordencampo_"+i));
							vo.put("unico_fila",forma.getMapaCampos("unicofila_"+i));
							vo.put("requerido",forma.getMapaCampos("requeridocampo_"+i));
							vo.put("formula",forma.getMapaCampos("formula"));
							vo.put("alerta",forma.getMapaCampos("generaralerta_"+i));
							
							if((tipoCampo==ConstantesCamposParametrizables.tipoCampoCaracter)||(tipoCampo==ConstantesCamposParametrizables.tipoCampoNumericoEntero)||(tipoCampo==ConstantesCamposParametrizables.tipoCampoNumericoDecimal)||(tipoCampo==ConstantesCamposParametrizables.tipoCampoFecha)||(tipoCampo==ConstantesCamposParametrizables.tipoCampoHora)||(tipoCampo==ConstantesCamposParametrizables.tipoCampoFormula))
							{
								vo.put("tipo_html",ConstantesCamposParametrizables.campoTipoText);
							}
							else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoAreaTexto)
							{
								vo.put("tipo_html",ConstantesCamposParametrizables.campoTipoTextArea);
							}
							else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoChequeo)
							{
								vo.put("tipo_html",ConstantesCamposParametrizables.campoTipoCheckBox);
							}
							else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoSeleccion)
							{
								vo.put("tipo_html",ConstantesCamposParametrizables.campoTipoSelect);
							}
							else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoArchivo)
							{
								vo.put("tipo_html",ConstantesCamposParametrizables.campoTipoARCH);
							}
							else if(tipoCampo==ConstantesCamposParametrizables.tipoCampoTextoPredeterminado)
							{
								vo.put("tipo_html",ConstantesCamposParametrizables.campoTipoLabel);
							}
							vo.put("mostrar",ConstantesBD.acronimoSi);
							vo.put("mostrar_modificacion",ConstantesBD.acronimoSi);
							vo.put("institucion",usuario.getCodigoInstitucion());
							vo.put("usuario_modifica",usuario.getLoginUsuario());
							int codigoCampo=mundo.insertarCampo(con, vo);
							if(codigoCampo>0)
							{
								for(int j=0;j<Integer.parseInt(forma.getMapaOpciones("numRegistros")+"");j++)
								{
									
									logger.info("modifivkarrrrr");
									//modificar
									vo=new HashMap();
									vo.put("campo_parametrizable",codigoCampo);
									vo.put("opcion",forma.getMapaOpciones("opcion_"+j));
									vo.put("valor",forma.getMapaOpciones("valor_"+j));
									vo.put("institucion",usuario.getCodigoInstitucion());
									vo.put("usuario_modifica",usuario.getLoginUsuario());
									int codigoOpcion=mundo.insertarOpciones(con, vo);
									if(codigoOpcion>0)
									{
										for(int k=1;k<=Utilidades.convertirAEntero(forma.getMapaSeccionesGeneradas("numRegistros")+"");k++)
										{
											if((forma.getMapaSeccionesGeneradas("checkcc_"+k)+"").equals("1"))
											{	
												vo=new HashMap();
												vo.put("codigo_opcion", codigoOpcion);
												vo.put("componente_seccion", forma.getMapaSeccionesGeneradas("codseccion_"+k));
												vo.put("mostrar_modificacion", ConstantesBD.acronimoSi);
												vo.put("usuario_modifica", usuario.getLoginUsuario());
												vo.put("fecha_modifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
												vo.put("hora_modifica", UtilidadFecha.getHoraActual());
												transaccion=mundo.insertarSeccionesAsocidas(con, vo);
											}	
										}
										

										for(int t=1;t<=Utilidades.convertirAEntero(forma.getMapaValoresGenerados("numRegistros")+"");t++)
										{
											for(int val=0;val<=Utilidades.convertirAEntero(forma.getMapaValoresGenerados("numRegistros")+"");val++)
											{
												if(forma.getMapaValoresGenerados().containsKey("checkcc_"+t+"_"+val))
												{	
													
													mundo.eliminarValoresAsociados(con, codigoCampo);
													
													if (forma.getMapaValoresGenerados("checkcc_"+t+"_"+val).equals("1")&&j==val)
													{
														vo=new HashMap();
														vo.put("codigo_opcion", codigoOpcion);
														vo.put("valor", forma.getMapaValoresGenerados("valor_"+t+"_"+val));
														vo.put("mostrar_modificacion", ConstantesBD.acronimoSi);
														vo.put("usuario_modifica", usuario.getLoginUsuario());
														vo.put("fecha_modifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
														vo.put("hora_modifica", UtilidadFecha.getHoraActual());
														transaccion=mundo.insertarValoresAsociados(con, vo);
													}
												}
											}
										}
									}
								}
								transaccion=mundo.insertarCompCamposSec(con, codigoCompSeccion, codigoCampo);
							}
							else
							{
								transaccion=false;
							}
							
						}
						else
						{
							transaccion=false;
						}
					}
					
				}
				else
				{
					transaccion=false;
				}
				
			}
			
		}	
				
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
	}
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionModificarSeccion(Connection con, ParametrizacionComponentesForm forma, ParametrizacionComponentes mundo, UsuarioBasico usuario) 
	{
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		int numRegistros= Utilidades.convertirAEntero(forma.getMapaSecciones("numRegistros")+"");
		
		int codigoComponente= mundo.consultarCodigoComponete(con, forma.getComponente());
		
		
		for(int i=0; i<numRegistros; i++)
		{
			
			if(Utilidades.convertirAEntero(forma.getMapaSecciones("sexoseccion_"+i)+"")==ConstantesBD.codigoSexoTodos)
			{
				forma.setMapaSecciones("sexoseccion_"+i, "");
			}
			if((forma.getMapaSecciones("nombreseccion_"+i)+"").trim().equals(forma.getMapaSecciones("nombreseccionantiguo_"+i)+"")&&(forma.getMapaSecciones("tiposeccion_"+i)+"").trim().equals(forma.getMapaSecciones("tiposeccionantiguo_"+i)+""))
			{
				
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getMapaSecciones("codigoseccion_"+i));
				vo.put("codigopk",forma.getMapaSecciones("codigopkseccion_"+i));
				vo.put("columnas_seccion",forma.getMapaSecciones("columnaseccion_"+i));
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("sexo_seccion", forma.getMapaSecciones("sexoseccion_"+i));
				vo.put("edad_inicial", forma.getMapaSecciones("edadinicial_"+i));
				vo.put("edad_final", forma.getMapaSecciones("edadfinal_"+i));
				vo.put("indicativo_restriccion", forma.getMapaSecciones("indicativorestriccion_"+i));
				transaccion=mundo.modificarSeccion(con, vo);
				
			}
			else	
			{	
				
				if(forma.getIndexNivel().equals("1"))
				{
				
					//Modificar Mostrar Modificacion
					HashMap vo=new HashMap();
					
					vo.put("codigopk",forma.getMapaSecciones("codigopkseccion_"+i));
					vo.put("mostrar",ConstantesBD.acronimoSi);
					vo.put("mostrar_modificacion",ConstantesBD.acronimoNo);
					vo.put("usuario_modifica",usuario.getLoginUsuario());
					transaccion=mundo.modificarMostrarSeccion(con, vo);
					
					
					vo=new HashMap();
					vo.put("codigo",forma.getMapaSecciones("codigoseccion_"+i));
					vo.put("descripcion",forma.getMapaSecciones("nombreseccion_"+i));
					vo.put("orden",forma.getMapaSecciones("orden_"+i));
					vo.put("columnas_seccion",forma.getMapaSecciones("columnaseccion_"+i));
					vo.put("seccion_padre","");
					vo.put("mostrar",ConstantesBD.acronimoSi);
					vo.put("mostrar_modificacion",ConstantesBD.acronimoSi);
					vo.put("tipo",forma.getMapaSecciones("tiposeccion_"+i));
					vo.put("institucion",usuario.getCodigoInstitucion());
					vo.put("usuario_modifica",usuario.getLoginUsuario());
					vo.put("sexo_seccion", forma.getMapaSecciones("sexoseccion_"+i));
					vo.put("edad_inicial", forma.getMapaSecciones("edadinicial_"+i));
					vo.put("edad_final", forma.getMapaSecciones("edadfinal_"+i));
					vo.put("indicativo_restriccion", forma.getMapaSecciones("indicativorestriccion_"+i));
					int codigoSeccion=mundo.insertarSeccion(con, vo);
					
					if(codigoSeccion>0)
					{
						int codigoCompSeccion=mundo.insertarCompSeccion(con, codigoComponente, codigoSeccion);
						if(codigoCompSeccion>0)
						{
							transaccion=true;
						}
						else
						{
							transaccion=false;
						}
					}
				
				}
				else
				{
					
					//Modificar Mostrar Modificacion
					HashMap vo=new HashMap();
					
					vo.put("codigopk",forma.getMapaSecciones("codigopkseccion_"+i));
					vo.put("mostrar",ConstantesBD.acronimoSi);
					vo.put("mostrar_modificacion",ConstantesBD.acronimoNo);
					vo.put("usuario_modifica",usuario.getLoginUsuario());
					transaccion=mundo.modificarMostrarSeccion(con, vo);
					
					
					vo=new HashMap();
					vo.put("codigo",forma.getMapaSecciones("codigoseccion_"+i));
					vo.put("descripcion",forma.getMapaSecciones("nombreseccion_"+i));
					vo.put("orden",forma.getMapaSecciones("ordenseccion:_"+i));
					vo.put("columnas_seccion",forma.getMapaSecciones("columnaseccion_"+i));
					vo.put("seccion_padre",forma.getMapaSecciones("seccionpadre_"+i));
					vo.put("mostrar",ConstantesBD.acronimoSi);
					vo.put("mostrar_modificacion",ConstantesBD.acronimoSi);
					vo.put("tipo",forma.getMapaSecciones("tiposeccion_"+i));
					vo.put("institucion",usuario.getCodigoInstitucion());
					vo.put("usuario_modifica",usuario.getLoginUsuario());
					vo.put("sexo_seccion", forma.getMapaSecciones("sexoseccion_"+i));
					vo.put("edad_inicial", forma.getMapaSecciones("edadinicial_"+i));
					vo.put("edad_final", forma.getMapaSecciones("edadfinal_"+i));
					vo.put("indicativo_restriccion", forma.getMapaSecciones("indicativorestriccion_"+i));
					int codigoSeccion=mundo.insertarSeccion(con, vo);
					
					if(codigoSeccion>0)
					{
						int codigoCompSeccion=mundo.insertarCompSeccion(con, codigoComponente, codigoSeccion);
						if(codigoCompSeccion>0)
						{
							transaccion=true;
						}
						else
						{
							transaccion=false;
						}
					}
					
				}
			
					
			}
			
		}	
			
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarSeccion(Connection con, ParametrizacionComponentesForm forma, ParametrizacionComponentes mundo, UsuarioBasico usuario) 
	{
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		int numRegistros= Utilidades.convertirAEntero(forma.getMapaSecciones("numRegistros")+"");
		
		int codigoComponente= mundo.consultarCodigoComponete(con, forma.getComponente());
		
		if(forma.getIndexNivel().equals("1"))
		{
			
			int ordenSeccion=Utilidades.convertirAEntero(forma.getMapaConsultaSecciones("numRegistros")+"");
			if(ordenSeccion==ConstantesBD.codigoNuncaValido)
			{
				ordenSeccion=0;
			}
			int ordenEscalas=Utilidades.convertirAEntero(forma.getMapaConsultaEscalas("numRegistros")+"");
			if(ordenEscalas==ConstantesBD.codigoNuncaValido)
			{
				ordenEscalas=0;
			}
			
			
			if(codigoComponente<0)
			{
			
				codigoComponente=mundo.insertarComponente(con, forma.getComponente(), usuario.getCodigoInstitucion(), usuario.getLoginUsuario());
				
			}
			
			if(codigoComponente>0)
			{	
				
				for(int i=0; i<numRegistros; i++)
				{
					
					int orden=Utilidades.convertirAEntero(forma.getMapaSecciones("orden_"+i)+"");
					if(orden==ConstantesBD.codigoNuncaValido)
					{
						orden=0;
					}
					
					int ordenFinal=orden+ordenSeccion+ordenEscalas;
					if(Utilidades.convertirAEntero(forma.getMapaSecciones("sexoseccion_"+i)+"")==ConstantesBD.codigoSexoTodos)
					{
						forma.setMapaSecciones("sexoseccion_"+i, "");
					}
					//insertar
					HashMap vo=new HashMap();
					vo.put("codigo",forma.getMapaSecciones("codigoseccion_"+i));
					vo.put("descripcion",forma.getMapaSecciones("nombreseccion_"+i));
					vo.put("orden",ordenFinal);
					vo.put("columnas_seccion",forma.getMapaSecciones("columnaseccion_"+i));
					vo.put("seccion_padre","");
					vo.put("mostrar",ConstantesBD.acronimoSi);
					vo.put("mostrar_modificacion",ConstantesBD.acronimoSi);
					vo.put("tipo",forma.getMapaSecciones("tiposeccion_"+i));
					vo.put("institucion",usuario.getCodigoInstitucion());
					vo.put("usuario_modifica",usuario.getLoginUsuario());
					vo.put("sexo_seccion", forma.getMapaSecciones("sexoseccion_"+i));
					vo.put("edad_inicial", forma.getMapaSecciones("edadinicial_"+i));
					vo.put("edad_final", forma.getMapaSecciones("edadfinal_"+i));
					vo.put("indicativo_restriccion", forma.getMapaSecciones("indicativorestriccion_"+i));
					int codigoSeccion=mundo.insertarSeccion(con, vo);
					
					if(codigoSeccion>0)
					{
						int codigoCompSeccion=mundo.insertarCompSeccion(con, codigoComponente, codigoSeccion);
						if(codigoCompSeccion>0)
						{
							transaccion=true;
						}
						else
						{
							transaccion=false;
						}
					}
					
				}
				
			}
			else
			{
				transaccion=false;
			}
			
		}
		if(forma.getIndexNivel().equals("2"))
		{
			
			if(codigoComponente>0)
			{	
				
				for(int i=0; i<numRegistros; i++)
				{
					
					if(Utilidades.convertirAEntero(forma.getMapaSecciones("sexoseccion_"+i)+"")==ConstantesBD.codigoSexoTodos)
					{
						forma.setMapaSecciones("sexoseccion_"+i, "");
					}
					//insertar
					HashMap vo=new HashMap();
					vo.put("codigo",forma.getMapaSecciones("codigoseccion_"+i));
					vo.put("descripcion",forma.getMapaSecciones("nombreseccion_"+i));
					vo.put("orden",forma.getMapaSecciones("orden_"+i));
					vo.put("columnas_seccion",forma.getMapaSecciones("columnaseccion_"+i));
					vo.put("seccion_padre",forma.getMapaDetalleSeccion("codigopkseccion_0"));
					vo.put("mostrar",ConstantesBD.acronimoSi);
					vo.put("mostrar_modificacion",ConstantesBD.acronimoSi);
					vo.put("tipo",forma.getMapaSecciones("tiposeccion_"+i));
					vo.put("institucion",usuario.getCodigoInstitucion());
					vo.put("usuario_modifica",usuario.getLoginUsuario());
					vo.put("sexo_seccion", forma.getMapaSecciones("sexoseccion_"+i));
					vo.put("edad_inicial", forma.getMapaSecciones("edadinicial_"+i));
					vo.put("edad_final", forma.getMapaSecciones("edadfinal_"+i));
					int codigoSeccion=mundo.insertarSeccion(con, vo);
					
					if(codigoSeccion>0)
					{
						int codigoCompSeccion=mundo.insertarCompSeccion(con, codigoComponente, codigoSeccion);
						if(codigoCompSeccion>0)
						{
							transaccion=true;
						}
						else
						{
							transaccion=false;
						}
					}
					
				}
				
			}
			else
			{
				transaccion=false;
			}
			
			
		}
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
			forma.getMapaSecciones().put("numRegistros", "0");
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
	}
	
	
	/**
	 * Metodo para cargar la vista previa de un Componente
	 * @param con
	 * @param forma
	 */
	private void metodoCargarComponentePreview(Connection con,ParametrizacionComponentesForm forma) {
		
		HashMap campos = new HashMap();
		forma.resetComponentePreview();
		campos.put("tipoComponente", forma.getComponente());		
		forma.setComponentePreview(Plantillas.cargarComponenteGenericoPreview(campos));	
	
	}

	/**
	 * Metodo encargado de eliminar una Curva de Crecimiento paramatrizadas del Componente 
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return
	 * @author hermorhu
	 * @created 09-Oct-2012 
	 */
	private ActionForward accionEliminarCurvaComponente(ParametrizacionComponentesForm forma, ActionMapping mapping, HttpServletRequest request) {
		
		forma.resetProcesoExitoso();
		
		PlantillaComponenteDto plantillaComponente = forma.getListaGraficaCurvasSeleccionadas().get(Integer.parseInt(forma.getIndexCurvaEliminada()));
		CurvaCrecimientoParametrizabDto curvaParametrizable = forma.getListaGraficaCurvasSeleccionadas().get(Integer.parseInt(forma.getIndexCurvaEliminada())).getPlantillaCurvaCrecimiento();
		
		forma.getListaGraficaCurvas().add(curvaParametrizable);
		
		if(plantillaComponente.getId() != null){
			forma.getListaGraficaCurvasEliminadas().add(plantillaComponente);
		}
		
		forma.getListaGraficaCurvasSeleccionadas().remove(plantillaComponente);
		
		return mapping.findForward("principal");
		
	}
	
	/**
	 * Metodo encargado de Guardar los cambios hechos en el componente de Curvas de Crecimiento
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return
	 * @author hermorhu
	 * @created 09-Oct-2012 
	 */
	private ActionForward accionGuardarCurva(ParametrizacionComponentesForm forma, ActionMapping mapping, HttpServletRequest request) {
		
		ActionMessages errores = new ActionMessages();
		HistoriaClinicaFacade historiaClinicaFacade = new HistoriaClinicaFacade();
		
		try {
			historiaClinicaFacade.asociarCurvasAComponente(forma.getListaGraficaCurvasSeleccionadas(), Integer.parseInt(forma.getComponente()));
			
			historiaClinicaFacade.eliminarCurvasComponente(forma.getListaGraficaCurvasEliminadas(), Integer.parseInt(forma.getComponente()));
			
			forma.resetCurvasCrecimiento();
		
			forma.setListaGraficaCurvasSeleccionadas(historiaClinicaFacade.consultarCurvasComponente(Integer.parseInt(forma.getComponente())));
			forma.setListaGraficaCurvas(historiaClinicaFacade.consultarCurvasDisponiblesComponente(Integer.parseInt(forma.getComponente())));
		
			forma.setProcesoExitoso(true);
			
		}catch (IPSException ipse) {
			Log4JManager.error(ipse);
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}catch(Exception e){
			Log4JManager.error(e);
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
		}
			
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}

	return mapping.findForward("principal");
	}
	
	/**
	 * Metodo encargado de agregar las Curvas de Crecimiento parametrizadas al componente
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return
	 * @author hermorhu
	 * @created 09-Oct-2012 
	 */
	private ActionForward accionAgregarGraficaCurvas(ParametrizacionComponentesForm forma, ActionMapping mapping, HttpServletRequest request) {

		forma.resetProcesoExitoso();
		
		//Recorre la lista de Curvas para remover la Curva de Crecimiento de la lista a escoger y se agrega en la Lista de escogidos
		for(CurvaCrecimientoParametrizabDto curvaCrecimiento : forma.getListaGraficaCurvas()){
			if(Integer.parseInt(forma.getGraficaCurvas()) == curvaCrecimiento.getId()){
				forma.getListaGraficaCurvas().remove(curvaCrecimiento);
			
				PlantillaComponenteDto plantillaComponente = new PlantillaComponenteDto();
				plantillaComponente.setPlantillaCurvaCrecimiento(curvaCrecimiento);
				plantillaComponente.setActivo(true);
				plantillaComponente.setId(null);
				forma.getListaGraficaCurvasSeleccionadas().add(plantillaComponente);
			
				break;
			}
		}
		return mapping.findForward("principal");
	}
	
}
