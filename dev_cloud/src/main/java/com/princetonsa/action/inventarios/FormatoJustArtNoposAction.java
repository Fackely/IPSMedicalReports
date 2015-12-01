/**
 * 
 */
package com.princetonsa.action.inventarios;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

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
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.inventarios.UtilidadInventarios;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.inventarios.FormatoJustArtNoposForm;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.antecedentes.AntecedentesMedicamentos;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;
import com.princetonsa.mundo.solicitudes.SolicitudMedicamentos;
import com.princetonsa.pdf.SolicitudMedicamentosPdf;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.servicio.formatoJust.ComportamientoCampo;

/**
 * @author axioma
 *
 */
public class FormatoJustArtNoposAction extends Action {

	/**
	 * logger 
	 * */
	static Logger logger = Logger.getLogger(AjustesXInventarioFisicoAction.class);
	
	/**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{




    	logger.info("===> Entré al Action !!!");

    	Connection con = null;
    	try{

    		if(response==null);
    		if(form instanceof FormatoJustArtNoposForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("paginaError");
    			}
    			FormatoJustArtNoposForm forma = (FormatoJustArtNoposForm) form;
    			FormatoJustArtNopos mundo = new FormatoJustArtNopos();

    			String estado = forma.getEstado();
    			ActionErrors errores = new ActionErrors();
    			logger.info("\n\n ESTADO FORMATO JUSTIFICACION ARTICULOS NO POS ---->"+estado+"\n\n");
    			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
    			UsuarioBasico usuario  = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			if(paciente==null)
    				errores.add("Paciente", new ActionMessage("errors.required","Paciente"));

    			if(estado == null)
    			{
    				forma.reset();
    				logger.warn("Estado no valido dentro del Flujo de Unidad de Procedimiento (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezar"))
    			{    			
     				return this.accionEmpezar(forma, con, mapping, usuario, paciente, request, response);
    			}
    			else if(estado.equals("empezarValidarJustificacion"))
    			{    			
    				return this.accionValidarJustificacion(forma, con, mapping, usuario, paciente, request, response);
    			}
    			else if(estado.equals("asociarjustificacion"))
    			{    			
    				return this.asociarJustificacion(forma, con, mapping, usuario, paciente, request, response);
    			}
    			else if (estado.equals("subirarchivo"))
    			{
    				return this.accionSubirArchivo(con, forma, mapping, estado);

    			}
    			else if(estado.equals("cargarInfoMedicamentosPos"))
    			{
    				return this.accionCargarInfoMedicamentosPos(forma, con, mapping, usuario, paciente,response);
    			}
    			else if(estado.equals("cargarInfoIngresos"))
    			{
    				return this.accionCargarInfoIngresos(forma, con, mapping, usuario, paciente,response);
    			}
    			else if (estado.equals("cargarDatosSustitutos"))
    			{
    				return accioncargarDatosSustitutos(con,forma,response);
    			}
    			else if (estado.equals("guardar"))
    			{
    				return accionguardarjustificacion(forma, con, mapping,request);
    			}
    			else if (estado.equals("mostrarSubCuentas"))
    			{
    				return this.accionMostrarSubCuentas(forma,mapping,con,usuario, paciente,request,response);
    			}
    			else
    				if(estado.equals("cargarArt"))
    				{
    					return this.accionconsultarM(forma, con, mapping, usuario);
    				}
    				else
    					if(estado.equals("empezarotravez"))
    					{
    						FormatoJustArtNopos fjan=new FormatoJustArtNopos();
    						
    						if(request.getSession().getAttribute(forma.getMedicamentoNoPos()+"MAPAJUSART_NORMAL")!=null){
    							forma.setFormularioMap((HashMap) request.getSession().getAttribute(forma.getMedicamentoNoPos()+"MAPAJUSART_NORMAL"));
    						}
    						
    						if(request.getSession().getAttribute(forma.getMedicamentoNoPos()+"MAPA_MED_NO_POS_NORMAL")!=null){
    							forma.setMedicamentosNoPos((HashMap) request.getSession().getAttribute(forma.getMedicamentoNoPos()+"MAPA_MED_NO_POS_NORMAL"));
    						}
    						if(request.getSession().getAttribute(forma.getMedicamentoNoPos()+"MAPA_MED_POS_NORMAL")!=null){
    							forma.setMedicamentosPos((HashMap) request.getSession().getAttribute(forma.getMedicamentoNoPos()+"MAPA_MED_POS_NORMAL"));
    						}
    						if(request.getSession().getAttribute(forma.getMedicamentoNoPos()+"MAPA_SUS_NO_POS_NORMAL")!=null){
    							forma.setSustitutosNoPos((HashMap) request.getSession().getAttribute(forma.getMedicamentoNoPos()+"MAPA_SUS_NO_POS_NORMAL"));
    						}
    						if(request.getSession().getAttribute(forma.getMedicamentoNoPos()+"MAPA_DIAG_DEF_NORMAL")!=null){
    							forma.setDiagnosticosDefinitivos((HashMap) request.getSession().getAttribute(forma.getMedicamentoNoPos()+"MAPA_DIAG_DEF_NORMAL"));
    						}
    						if(request.getSession().getAttribute(forma.getMedicamentoNoPos()+"MAPA_DIAG_PRESUNTIVOS_NORMAL")!=null){
    							forma.setDiagnosticosPresuntivos((HashMap) request.getSession().getAttribute(forma.getMedicamentoNoPos()+"MAPA_DIAG_PRESUNTIVOS_NORMAL"));
    						}
    						if(request.getSession().getAttribute(forma.getMedicamentoNoPos()+"MAPA_INF_ART_PRINT")!=null){
    							forma.setInformacionArtPrin((HashMap) request.getSession().getAttribute(forma.getMedicamentoNoPos()+"MAPA_INF_ART_PRINT"));
    						}else{
    							forma.setInformacionArtPrin(new HashMap());
    						}
    						if(request.getSession().getAttribute(forma.getMedicamentoNoPos()+"MAPA_FORMA_FCONC")!=null){
    							forma.setFormaFconcMap((HashMap) request.getSession().getAttribute(forma.getMedicamentoNoPos()+"MAPA_FORMA_FCONC"));
    						}else{
    							forma.setFormaFconcMap(new HashMap());
    						}
    						
    						fjan.setIndicesformulariomap(Listado.obtenerKeysMapa(forma.getFormularioMap()));
    						fjan.setIndicesnoposmap(Listado.obtenerKeysMapa(forma.getMedicamentosNoPos()));
    						fjan.setIndicesposmap(Listado.obtenerKeysMapa(forma.getMedicamentosPos()));
    						fjan.setIndicessustimap(Listado.obtenerKeysMapa(forma.getSustitutosNoPos()));
    						fjan.setIndicesdiagmap(Listado.obtenerKeysMapa((HashMap)forma.getDiagnosticosDefinitivos()));
    						fjan.setIndicesdiag1map(Listado.obtenerKeysMapa((HashMap)forma.getDiagnosticosPresuntivos()));

    						return this.accionEmpezarCon(forma, con, mapping, usuario, paciente, request);
    					}
    					else
    						if(estado.equals("continuar"))
    						{
    							//logger.info("\n\n\n\n\n\n cadena diagnosticos complicacion >>>"+forma.getDiagnosticoComplicacion_1());
    							//logger.info("\n\n\n\n\n\n mapa dx definitivos >>>"+forma.getDiagnosticosDefinitivos());
    							UtilidadBD.closeConnection(con);
    							return mapping.findForward("principal");
    						}
    						else
    							if(estado.equals("ingresar"))
    							{
    								UtilidadBD.closeConnection(con);
    								request.getSession().removeAttribute(forma.getMedicamentoNoPos()+"MAPAJUSART");
    								return mapping.findForward("ingresar");			
    							}
    							else
    								if(estado.equals("consultarModificar"))
    								{
    									UtilidadBD.closeConnection(con);
    									request.getSession().removeAttribute(forma.getMedicamentoNoPos()+"MAPAJUSART");
    									return mapping.findForward("consultarModificar");
    								}
    								else
    									if(estado.equals("ingresarXPaciente"))
    									{
    										//Validaciones
    										errores = validarPacienteCargado(con, forma, usuario, paciente, request, errores);
    										errores = validarProfesionalDeLaSalud(con, forma, usuario, request, errores);
    										if(!errores.isEmpty())
    										{
    											saveErrors(request,errores);	
    											UtilidadBD.closeConnection(con);
    											return mapping.findForward("ingresar");	
    										}
    										return this.accionCargarIngresarProPaciente(forma, con, mapping, usuario, paciente, request);
    									}
    									else
    										if(estado.equals("ingresarXRangos"))
    										{
    											return this.accionIngresarRango(forma, con, mapping, usuario, paciente, request,response);
    										}
    										else
    											if(estado.equals("funcionalidad"))
    											{
    												UtilidadBD.closeConnection(con);
    												return mapping.findForward("menu");
    											}
    											else
    												if(estado.equals("guardar1"))
    												{
    													return this.accionGuardarJustificacionIngresar(forma,con,mapping,usuario,paciente,request);
    												}
    												else
    													if(estado.equals("cargarCentrosCosto"))
    													{
    														return this.accionCargarCentrosCosto(forma,con,mapping,usuario,paciente,request,response);
    													}
    													else
    														/*------------------------------
    														 * 		ESTADO > ordenarIngresarXPaciente
				 -------------------------------*/
    														if (estado.equals("ordenarIngresarXPaciente"))
    														{
    															accionOrdenarIngresarXPaciente(forma);
    															UtilidadBD.closeConnection(con);
    															return mapping.findForward("ingresarXPaciente");
    														}
    														else
    															/*------------------------------
    															 * 		ESTADO > ordenarIngresarXPacientedet
				 -------------------------------*/
    															if (estado.equals("ordenarIngresarXPacientedet"))
    															{
    																accionOrdenarIngresarXPacientedet(forma);
    																UtilidadBD.closeConnection(con);
    																return mapping.findForward("detsolicitudes");
    															}  
    															else
    																if(estado.equals("ordenarIngresarXRangodet"))
    																{
    																	accionOrdenarIngresarXRangodet(forma);
    																	UtilidadBD.closeConnection(con);
    																	return mapping.findForward("detsolicitudesRango");
    																}
    																else 
    																	if(estado.equals("ordenarConsultarModificarXRangodet"))
    																	{
    																		ordenarConsultarModificarXRangodet(forma);
    																		UtilidadBD.closeConnection(con);
    																		return mapping.findForward("detconsultarmodificarRango");
    																	}
    																	else 
    																		if(estado.equals("ordenarConsultarModificarXRangodetcon"))
    																		{
    																			ordenarConsultarModificarXRangodetcon(forma);
    																			UtilidadBD.closeConnection(con);
    																			return mapping.findForward("detconsultarmodificarRangocon");
    																		}
    																		else 
    																			if(estado.equals("ordenarConsultarModificarXPaciente"))
    																			{
    																				ordenarConsultarModificarXPaxienteDet(forma);
    																				UtilidadBD.closeConnection(con);
    																				return mapping.findForward("detconsultarmodificarpac");
    																			}
    																			else
    																				/*------------------------------
    																				 * 		ESTADO > ordenarIngresarXPaciente
				 -------------------------------*/
    																				if (estado.equals("agregarNuevo"))
    																				{
    																					return accionAdicionarArticulo(forma,con,mapping,usuario);
    																				}
    																				else
    																					/*------------------------------
    																					 * 		ESTADO > ordenarIngresarXPaciente
				 -------------------------------*/
    																					if (estado.equals("agregarNuevoc"))
    																					{
    																						return accionAdicionarArticuloc(forma,con,mapping,usuario);
    																					}
    																					else if(estado.equals("eliminarArticulo"))
    																					{
    																						return this.accionEliminarArticulo(forma,con,mapping, usuario);
    																					}
    																					else if(estado.equals("eliminarArticuloc"))
    																					{
    																						return this.accionEliminarArticuloc(forma,con,mapping, usuario);
    																					}    		
    																					else if(estado.equals("consultarRangos"))
    																					{
    																						return this.accionConsutarRangos(forma,con,mapping,usuario,paciente,request);
    																					}
    																					else if (estado.equals("consultarModificarRangos"))
    																					{
    																						return this.accionConsutarModificarRangos(forma,con,mapping,usuario,paciente,request);
    																					}
    																					else if (estado.equals("recargarConsultarModificarRangos"))
    																					{
    																						return this.accionRecargarConsutarModificarRangos(forma,con,mapping,usuario,paciente,request);
    																					}
    																					else if(estado.equals("cargarPacJus"))
    																					{
    																						int	codpaciente=Utilidades.convertirAEntero(forma.getIngresosArticulos().get("codpaciente_"+forma.getIndexMap()).toString());

    																						paciente.setCodigoPersona(codpaciente);
    																						paciente.cargar(con, codpaciente);
    																						paciente.cargarPaciente(con, codpaciente, usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");

    																						//se cargan los datos en la forma en el formato en el que el metodo los busca
    																						forma.setEmisor("ingresar");
    																						forma.setCodigoSolicitud(""+forma.getIngresosArticulos().get("solicitud_"+forma.getIndexMap()));
    																						forma.setConvenio(""+forma.getIngresosArticulos().get("codigo_convenio_"+forma.getIndexMap()));
    																						forma.setCentroCosto(""+forma.getIngresosArticulos().get("codigo_centro_costo_"+forma.getIndexMap()));
    																						forma.setMedicamentoNoPos(""+forma.getIngresosArticulos().get("codigo_art_"+forma.getIndexMap()));
    																						forma.setIngreso(""+forma.getIngresosArticulos().get("ingreso_"+forma.getIndexMap()));
    																						forma.setSubCuenta(""+forma.getIngresosArticulos().get("subcuenta_"+forma.getIndexMap()));
    																						forma.setCuenta(""+forma.getIngresosArticulos().get("codigocuenta_"+forma.getIndexMap()));
    																						forma.setSubcuentascantidad(""+forma.getIngresosArticulos().get("cantidad_"+forma.getIndexMap()));
    																						// se coloca esta bandera para que al volver a la jsp se entere que tiene que levantar el popup del formulario
    																						
    																						
    																						forma.setEsInsumoRango(UtilidadesFacturacion.tipoMedicamento(Integer.valueOf(forma.getMedicamentoNoPos()))>0);
    																						
    																						
    																						forma.setCapa("irpopup");
    																						UtilidadBD.closeConnection(con);
    																						return mapping.findForward("detsolicitudesRango");

    																					}

    																					else if(estado.equals("cargarPacJus1"))	{
    																						int	codpaciente=Utilidades.convertirAEntero(forma.getIngresosArticulos().get("codpaciente_"+forma.getIndexMap()).toString());

    																						paciente.setCodigoPersona(codpaciente);
    																						paciente.cargar(con, codpaciente);
    																						paciente.cargarPaciente(con, codpaciente, usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");

    																						//se cargan los datos en la forma en el formato en el que el metodo los busca
    																						forma.setEmisor("modificar");

    																						//		        logger.info("mapa a la hora del llamado al formulario >>>>>"+forma.getIngresosArticulos());
    																						logger.info("index >>>"+forma.getIndexMap());

    																						forma.setCodigoSolicitud(""+forma.getIngresosArticulos().get("solicitud_"+forma.getIndexMap()));
    																						forma.setConvenio(""+forma.getIngresosArticulos().get("codigo_convenio_"+forma.getIndexMap()));
    																						forma.setCentroCosto(""+forma.getIngresosArticulos().get("codigo_centro_costo_"+forma.getIndexMap()));
    																						forma.setMedicamentoNoPos(""+forma.getIngresosArticulos().get("codigo_art_"+forma.getIndexMap()));
    																						forma.setIngreso(""+forma.getIngresosArticulos().get("ingreso_"+forma.getIndexMap()));
    																						forma.setSubCuenta(""+forma.getIngresosArticulos().get("subcuenta_"+forma.getIndexMap()));
    																						forma.setCuenta(""+forma.getIngresosArticulos().get("codigocuenta_"+forma.getIndexMap()));
    																						forma.setSubcuentascantidad(""+forma.getIngresosArticulos().get("cantidad_"+forma.getIndexMap()));
    																						// se coloca esta bandera para que al volver a la jsp se entere que tiene que levantar el popup del formulario
    																						
    																						forma.setEsInsumoRango(UtilidadesFacturacion.tipoMedicamento(Integer.valueOf(forma.getMedicamentoNoPos()))>0);
    																						
    																						forma.setCapa("irpopup");
    																						UtilidadBD.closeConnection(con);
    																						return mapping.findForward("detconsultarmodificarRango");
    																					}


    																					else
    																						if(estado.equals("consultarmodificarXPaciente"))
    																						{
    																							//Validaciones
    																							errores = validarPacienteCargado(con, forma, usuario, paciente, request, errores);
    																							//errores = validarProfesionalDeLaSalud(con, forma, usuario, request, errores);
    																							if(!errores.isEmpty())
    																							{
    																								saveErrors(request,errores);	
    																								UtilidadBD.closeConnection(con);
    																								return mapping.findForward("consultarModificar");	
    																							}
    																							return this.accionCargarconsultarMosificarProPaciente(forma, con, mapping, usuario, paciente, request);

    																						}
    																						else 
    																							if(estado.equals("cargarlistadojustificaciones"))
    																							{
    																								return this.accionCargarInfoJus(forma, con, mapping, usuario, paciente,response);
    																							}
    																							else
    																								if(estado.equals("consultarmodificarXRangos"))
    																								{
    																									return this.accionConsultarModificarRango(forma,con,mapping,usuario,paciente,request, response);
    																								}
    																								else if(estado.equals("imprimirListado"))
    																								{
    																									this.generarReporte(con, forma, mapping, request, usuario, paciente);
    																									UtilidadBD.closeConnection(con);
    																									return mapping.findForward("detsolicitudesRango");
    																								}
    																								else if (estado.equals("imprimir"))
    																								{
    																									UtilidadBD.closeConnection(con);
    																									return mapping.findForward("imprimir");
    																								}
    																								else if (estado.equals("imprimirConsultarModificarJusRangoDetallado"))
    																								{
    																									this.generarReporteConsultarModificarRangoDetallado(con, forma, mapping, request, usuario, paciente);
    																									UtilidadBD.closeConnection(con);
    																									return mapping.findForward("detconsultarmodificarRango");
    																								}
    																								else if (estado.equals("imprimirConsultarModificarJusRangoDetalladoCon"))
    																								{
    																									this.generarReporteConsultarModificarRangoDetalladoCon(con, forma, mapping, request, usuario, paciente, mundo);
    																									UtilidadBD.closeConnection(con);
    																									return mapping.findForward("detconsultarmodificarRangocon");
    																								}
    																								else if (estado.equals("consultarJustificacionHistorica")){
    																									return accionConsultarJustificacionHistorica(con, forma, mapping, request, usuario, paciente, mundo);
    																								}
    																								else if (estado.equals("imprimirConsultarJustificacionHistorica")){
    																									return accionImprimirConsultarJustificacionHistorica(con, forma, mapping, request, usuario, paciente);
    																								}else if(estado.equals("accionCampo")){
    																									return ComportamientoCampo.accionCampo(con, forma, mapping, request, usuario, paciente);
    																								}


    		}
    	}catch (Exception e) {
    		Log4JManager.error(e);
    		e.printStackTrace();
    	}
    	finally{
    		UtilidadBD.closeConnection(con);
    	}
    	return null;
}


	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @param con
	 * @param mapping
	 * @return 
	 */
    private ActionForward accionImprimirConsultarJustificacionHistorica(Connection con, FormatoJustArtNoposForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente) {
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
    	SolicitudMedicamentos mundo = new SolicitudMedicamentos();
    	mundo.setNumeroSolicitud(Utilidades.convertirAEntero(forma.getCodigoSolicitud()+""));
    	mundo.cargarSolicitudMedicamentos(con,usuario.getCodigoInstitucionInt());
    	SolicitudMedicamentosPdf.pdfSolicitudMedicamentosNoPos(ValoresPorDefecto.getFilePath() + nombreArchivo, mundo, usuario,paciente,Utilidades.convertirAEntero(forma.getMedicamentoNoPos()+""),con);
    	request.setAttribute("nombreArchivo", nombreArchivo);
        request.setAttribute("nombreVentana", "");
        UtilidadBD.closeConnection(con);
        return mapping.findForward("abrirPdf");
	}




    /**
     * Consultar una justificación histórica
     * @param con
     * @param forma
     * @param mapping
     * @param request
     * @param usuario
     * @param paciente
     * @param mundo
     * @return
     */
	private ActionForward accionConsultarJustificacionHistorica(Connection con, FormatoJustArtNoposForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario,  PersonaBasica paciente, FormatoJustArtNopos mundo) 
	{
		forma.setJustificacionHistorica(mundo.consultarJustificacionHistorica(con, forma.getMedicamentoNoPos(), forma.getCodigoSolicitud()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("justHistorica");
	}









	private ActionForward generarReporteConsultarModificarRangoDetalladoCon(Connection con, FormatoJustArtNoposForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente, FormatoJustArtNopos mundo) {

//logger.warn("\n\n");
//logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    	String tipoBD = System.getProperty("TIPOBD");
	    int cual = 0;
	    //String nombreTabla = "", dataSetTmp = "";
	    
    	String nombreRptDesign = "consultaJustificacionNoPosRangoConsolidado.rptdesign";
    	InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
    	//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v = new Vector();
        v.add(ins.getRazonSocial());

        if(Utilidades.convertirAEntero(ins.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit()+" - "+ins.getDigitoVerificacion());
        else
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit());
        
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        String parame="Parámetros de generación del listado \n";
        String encabezado="";
        
    	comp.obtenerComponentesDataSet("RompimientoCentroCostoCon");
        
        
        if (forma.getFiltros().get("tipoRompimiento").toString().equals("piso_")) {
        	logger.info("\n\n[dataSet Piso \n\n");
        	cual = 1;
       	}
        else
        	if (forma.getFiltros().get("tipoRompimiento").toString().equals("centro_costo_"))
        	{
        		logger.info("\n\n[dataSet Centro Costo \n\n");
        		cual = 0;

        	}
        	else
        		if (forma.getFiltros().get("tipoRompimiento").toString().equals("estadojus_"))
        		{
        			logger.info("\n\n[dataSet Estado \n\n");
        			cual = 2;
        		}
        
       //logger.info("consulta1 >>>"+comp.obtenerQueryDataSet());
       
       String newquery="";
       
      	String[] viapac=forma.getFiltros().get("viaIngreso").toString().split(ConstantesBD.separadorSplit);
		String consulta="";
		String articulos=forma.getArticulosMap().get("codigosArticulos").toString();

		newquery=comp.obtenerQueryDataSet().replaceAll("ValoresPorDefecto.getValorFalseParaConsultas", String.valueOf(ValoresPorDefecto.getValorFalseParaConsultas()));
		comp.modificarQueryDataSet(newquery);
		
		if(!viapac[0].toString().equals("-1")) {
			consulta=" c.via_ingreso="+viapac[0]+" " ;
			newquery=comp.obtenerQueryDataSet().replaceAll("9=9", consulta);
			comp.modificarQueryDataSet(newquery);
			parame+=" [Via Ingreso / Tipo Paciente :"+forma.getIngresosArticulos().get("viaingresotipopac_0")+"] ";
		} 

		/*if(!viapac[0].toString().equals("-1") && !viapac[1].toString().equals(""))
		{
		consulta=" c.tipo_paciente='"+viapac[1]+"' " ;
		newquery=comp.obtenerQueryDataSet().replaceAll("2=2", consulta);
		comp.modificarQueryDataSet(newquery);
		parame+=" [Tipo Paciente:"+viapac[1]+"] ";
		}*/
	
		if(!forma.getFiltros().get("centrocostoImp").toString().equals("")) {
			consulta=" c.area="+forma.getFiltros().get("centrocostoImp")+" " ;
			newquery=comp.obtenerQueryDataSet().replaceAll("2=2", consulta);
			comp.modificarQueryDataSet(newquery);
			parame+=" [Area:"+forma.getFiltros().get("centrocostoImp")+"] ";
		}

		if(!forma.getFiltros().get("convenioImp").toString().equals("")) {
			consulta=" sc.convenio="+forma.getFiltros().get("convenioImp")+" " ;
			newquery=comp.obtenerQueryDataSet().replaceAll("3=3", consulta);
			comp.modificarQueryDataSet(newquery);
			parame+=" [Convenio:"+forma.getFiltros().get("convenioImp")+"] ";
		}

		if(!articulos.equals("")) {
			consulta=" jpa.articulo IN ("+ConstantesBD.codigoNuncaValido+","+articulos+""+ConstantesBD.codigoNuncaValido+") " ;
			newquery=comp.obtenerQueryDataSet().replaceAll("4=4", consulta);
			comp.modificarQueryDataSet(newquery);
			parame+=" [Articulo (s):"+articulos+"] ";
		}


		if(!forma.getFiltros().get("estadojus").equals("-1")) {
			consulta=" jar.estado="+forma.getFiltros().get("estadojus"); 
			newquery=comp.obtenerQueryDataSet().replaceAll("5=5", consulta);
			comp.modificarQueryDataSet(newquery);
			parame+=" [Estado (s):"+forma.getFiltros().get("estadojus")+"] ";
		}
	

		if(!forma.getFiltros().get("piso").equals("")) {
			//MT6527 se el elimina el and ya que se repite en la consulta
			consulta="  getcodigopisocuenta(c.id,c.via_ingreso)="+forma.getFiltros().get("piso")+" " ;
			newquery=comp.obtenerQueryDataSet().replaceAll("6=6", consulta);
			comp.modificarQueryDataSet(newquery);
			parame+=" [Piso (s):"+forma.getFiltros().get("piso")+"] ";
		}
	
		if(!forma.getFiltros().get("profesional").equals("")) {
			//MT6527 se el elimina el and ya que se repite en la consulta
			consulta=" jaf.profesional_responsable ="+forma.getFiltros().get("profesional")+" " ;
			newquery=comp.obtenerQueryDataSet().replaceAll("7=7", consulta);
			comp.modificarQueryDataSet(newquery);
			parame+=" [Profesional (s):"+forma.getFiltros().get("profesional")+"] ";
		}

		String filtrofijo="";
	    if(tipoBD.equals("POSTGRESQL")){
	    	 filtrofijo=" jas.fecha BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal())+"' " ;
	    }else{
	    	 filtrofijo="  to_date(jas.fecha) BETWEEN to_date('"+forma.getFechaInicial()+"','DD/MM/YY') AND to_date('"+forma.getFechaFinal()+"','DD/MM/YY') " ;
	    }
	    
		newquery=comp.obtenerQueryDataSet().replaceAll("8=8", filtrofijo);
		comp.modificarQueryDataSet(newquery);
		parame+=" [Rango de Fechas:"+forma.getFechaInicial()+"-"+forma.getFechaFinal()+"] ";
	
		filtrofijo=" i.centro_atencion="+forma.getFiltros().get("centroAtencionImp")+" " +
				" and c.estado_cuenta!="+ConstantesBD.codigoEstadoCuentaCerrada+" and sc.nro_prioridad=1"+
				" and s.estado_historia_clinica!="+ConstantesBD.codigoEstadoHCAnulada+" "  ;

		newquery=comp.obtenerQueryDataSet().replaceAll("1=2", filtrofijo);
		

		//comp.modificarQueryDataSet(newquery);
		
		//reemplazar cadena desde el sqlbase
		comp.modificarQueryDataSet(mundo.consultaCompletaConsolidado(cual, newquery));
	

		parame+=" [Centro de Atención:"+forma.getFiltros().get("centroAtencion")+"] ";
		encabezado+="Centro de Atención: "+forma.getFiltros().get("centroAtencion")+"\n";
	
		if(!forma.getFiltros().get("piso").equals(""))
			encabezado+="Piso: "+forma.getFiltros().get("piso")+"\n";
		else
			encabezado+="Piso:\n";
	
		if(!forma.getFiltros().get("centrocostoImp").toString().equals(""))
			encabezado+="Centro de Costo:"+forma.getFiltros().get("centrocosto");
		else
			encabezado+="Centro de Costo:\n";
	
		if(!forma.getFiltros().get("estadojus").equals("-1"))
			encabezado+="Estado: "+forma.getFiltros().get("estadojus")+"\n";
		else
			encabezado+="Estado: \n";
	
	
	    comp.insertLabelInGridPpalOfHeader(3,0, parame);
	    comp.insertLabelInGridPpalOfHeader(5,0, encabezado);
	    comp.insertLabelInGridPpalOfHeader(5,1, UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual())+" - "+UtilidadFecha.getHoraActual());
    
    
	    //logger.info("consulta modificada >><"+comp.obtenerQueryDataSet());
	    //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
	    comp.updateJDBCParameters(newPathReport);
        
	    if(!newPathReport.equals("")) {
	    	request.setAttribute("isOpenReport", "true");
	    	request.setAttribute("newPathReport", newPathReport);
        }
       
		logger.info("\n Tipo Rompimiento " + forma.getFiltros().get("tipoRompimiento"));
	    comp.obtenerComponentesDataSet("RompimientoCentroCostoCon");
	    
	    UtilidadBD.closeConnection(con);
		return mapping.findForward("detconsultarmodificarRangocon");
	}


    /**
     * @param con
     * @param forma
     * @param mapping
     * @param request
     * @param usuario
     * @param paciente
     * @return     */
	private ActionForward generarReporteConsultarModificarRangoDetallado(Connection con, FormatoJustArtNoposForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente) {
    	String nombreRptDesign = "consultaJustificacionNoPosRangoDetallado.rptdesign";
		
    	String tipoBD = System.getProperty("TIPOBD");
    	
    	InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        String parame="Parámetros de generación del listado \n";

        
        //Valida data set a utilizar. 
        
        if (forma.getFiltros().get("tipoRompimiento").toString().equals("piso_"))
        	{
        	logger.info("\n\n[dataSet Piso \n\n");
        	
        	if ((forma.getFiltros().get("convenio")+"").equals("") || (forma.getFiltros().get("tiporegimen")+"").split("-")[0].equals(ConstantesBD.codigoTipoRegimenParticular+"") ){
        		comp.obtenerComponentesDataSet("RompimientoPiso");
        	    comp.modificarQueryDataSet(ConsultasBirt.consultarRompimientoPiso());
        	}else{
    			comp.obtenerComponentesDataSet("RompimientoPiso1");
    		    comp.modificarQueryDataSet(ConsultasBirt.consultarRompimientoPiso1());
        	}
        	}
        else
        	if (forma.getFiltros().get("tipoRompimiento").toString().equals("centro_costo_"))
        	{
        		logger.info("\n\n[dataSet Centro Costo \n\n");
        		
        		if ((forma.getFiltros().get("convenio")+"").equals("") || (forma.getFiltros().get("tiporegimen")+"").split("-")[0].equals(ConstantesBD.codigoTipoRegimenParticular+"") ){
        		 	comp.obtenerComponentesDataSet("RompimientoCentroCosto");
        		 	comp.modificarQueryDataSet(ConsultasBirt.consultarRompimientoCentroCosto());
        		}else{
        			comp.obtenerComponentesDataSet("RompimientoCentroCosto1");
        			comp.modificarQueryDataSet(ConsultasBirt.consultarRompimientoCentroCosto1());
        		}
        	}
        	else
        		if (forma.getFiltros().get("tipoRompimiento").toString().equals("estadojus_"))
        		{
        			logger.info("\n\n[dataSet Estado \n\n");
        			
        			if ((forma.getFiltros().get("convenio")+"").equals("") || (forma.getFiltros().get("tiporegimen")+"").split("-")[0].equals(ConstantesBD.codigoTipoRegimenParticular+"") ){
        				comp.obtenerComponentesDataSet("RompimientoEstadoJus");
        				comp.modificarQueryDataSet(ConsultasBirt.consultarRompimientoEstadoJus());
        			}else{
        				comp.obtenerComponentesDataSet("RompimientoEstadoJus1");
        				comp.modificarQueryDataSet(ConsultasBirt.consultarRompimientoEstadoJus1());
        			}
        		}
        
       logger.info("consulta1 >>>"+comp.obtenerQueryDataSet());
       
       String newquery="";
       
      	String[] viapac=forma.getFiltros().get("viaIngreso").toString().split(ConstantesBD.separadorSplit);
		String consulta="";
		String articulos=forma.getArticulosMap().get("codigosArticulos").toString();
		
	if(!viapac[0].toString().equals("-1"))
		{
		consulta=" c.via_ingreso="+viapac[0]+" " ;
		newquery=comp.obtenerQueryDataSet().replaceAll("9=9", consulta);
		comp.modificarQueryDataSet(newquery);
		parame+=" [Via Ingreso / Tipo Paciente :"+forma.getIngresosArticulos().get("viaingresotipopac_0")+"] ";
		} 
	/*if(!viapac[0].toString().equals("-1") && !viapac[1].toString().equals(""))
		{
		consulta=" c.tipo_paciente='"+viapac[1]+"' " ;
		newquery=comp.obtenerQueryDataSet().replaceAll("2=2", consulta);
		comp.modificarQueryDataSet(newquery);
		parame+=" [Tipo Paciente:"+viapac[1]+"] ";
		}*/
	if(!forma.getFiltros().get("centrocostoImp").toString().equals(""))
		{
		consulta=" c.area="+forma.getFiltros().get("centrocostoImp")+" " ;
		newquery=comp.obtenerQueryDataSet().replaceAll("2=2", consulta);
		comp.modificarQueryDataSet(newquery);
		parame+=" [Area:"+forma.getFiltros().get("centrocostoImp")+"] ";
		}
	if(!forma.getFiltros().get("convenioImp").toString().equals(""))
		{
		consulta=" sc.convenio="+forma.getFiltros().get("convenioImp")+" " ;
		newquery=comp.obtenerQueryDataSet().replaceAll("3=3", consulta);
		comp.modificarQueryDataSet(newquery);
		parame+=" [Convenio:"+forma.getFiltros().get("convenioImp")+"] ";
		}
	if(!articulos.equals(""))
		{
		consulta=" jpa.articulo IN ("+ConstantesBD.codigoNuncaValido+","+articulos+""+ConstantesBD.codigoNuncaValido+") " ;
		newquery=comp.obtenerQueryDataSet().replaceAll("4=4", consulta);
		comp.modificarQueryDataSet(newquery);
		parame+=" [Articulo (s):"+articulos+"] ";
		}

	if(!forma.getFiltros().get("estadojus").equals("-1"))
	{
		//MT6527 se adiciona las commillas simples ya que el condicional es un texto
	consulta=" jar.estado="+"'"+forma.getFiltros().get("estadojus")+"'"; 
	newquery=comp.obtenerQueryDataSet().replaceAll("5=5", consulta);
	comp.modificarQueryDataSet(newquery);
	parame+=" [Estado (s):"+forma.getFiltros().get("estadojus")+"] ";
	}
	
	if(!forma.getFiltros().get("piso").equals(""))
	{
		//MT6527 se el elimina el and ya que se repite en la consulta	
	consulta="  getcodigopisocuenta(c.id,c.via_ingreso)="+forma.getFiltros().get("piso")+" " ;
	newquery=comp.obtenerQueryDataSet().replaceAll("6=6", consulta);
	comp.modificarQueryDataSet(newquery);
	parame+=" [Piso (s):"+forma.getFiltros().get("piso")+"] ";
	}
	
	if(!forma.getFiltros().get("profesional").equals(""))
	{
		//MT6527 se el elimina el and ya que se repite en la consulta
	consulta=" jaf.profesional_responsable ="+forma.getFiltros().get("profesional")+" " ;
	newquery=comp.obtenerQueryDataSet().replaceAll("7=7", consulta);
	comp.modificarQueryDataSet(newquery);
	parame+=" [Profesional (s):"+forma.getFiltros().get("profesional")+"] ";
	}

	String filtrofijo="";
    if(tipoBD.equals("POSTGRESQL")){
    	 filtrofijo=" jas.fecha BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal())+"' " ;
    }else{
    	 filtrofijo="  to_date(jas.fecha) BETWEEN to_date('"+forma.getFechaInicial()+"','DD/MM/YY') AND to_date('"+forma.getFechaFinal()+"','DD/MM/YY') " ;
    }
    
	
    
	 
	newquery=comp.obtenerQueryDataSet().replaceAll("8=8", filtrofijo);
	comp.modificarQueryDataSet(newquery);
	parame+=" [Rango de Fechas:"+forma.getFechaInicial()+"-"+forma.getFechaFinal()+"]  ";
	
	if(tipoBD.equals("POSTGRESQL")){
	
	filtrofijo=" i.centro_atencion="+forma.getFiltros().get("centroAtencionImp")+" " +
				" and c.estado_cuenta <> "+ConstantesBD.codigoEstadoCuentaCerrada+" and sc.nro_prioridad=1"+
				" and s.estado_historia_clinica <> "+ConstantesBD.codigoEstadoHCAnulada+" "  ;
	}else{
		filtrofijo=" i.centro_atencion="+forma.getFiltros().get("centroAtencionImp")+" " +
				" and c.estado_cuenta!="+ConstantesBD.codigoEstadoCuentaCerrada+" and sc.nro_prioridad=1"+
				" and s.estado_historia_clinica!="+ConstantesBD.codigoEstadoHCAnulada+" "  ;
	}

	newquery=comp.obtenerQueryDataSet().replaceAll("1=2", filtrofijo);
	comp.modificarQueryDataSet(newquery);
	parame+=" [Centro de Atención:"+forma.getFiltros().get("centroAtencion")+"] ";

    
	
	String encabezado="Centro de Atención: "+forma.getFiltros().get("centroAtencion")+"\n";
	
	if(!forma.getFiltros().get("piso").equals(""))
		encabezado+="Piso: "+forma.getFiltros().get("piso")+"\n";
	else
		encabezado+="Piso:\n";
	
	if(!forma.getFiltros().get("centrocostoImp").toString().equals(""))
		//MT6527 se agrega un salto de linea
		encabezado+="Centro de Costo: "+forma.getFiltros().get("centrocosto")+"\n";
	else
		encabezado+="Centro de Costo:\n";
	
	if(!forma.getFiltros().get("estadojus").equals("-1"))
		encabezado+="Estado: "+forma.getFiltros().get("estadojus")+"\n";
	else
		encabezado+="Estado: \n";
	if(!forma.getFiltros().get("convenio").equals(""))
		encabezado+="Convenio: "+forma.getFiltros().get("convenio")+"\n";
	else
		encabezado+="Convenio: \n";	
	
    comp.insertLabelInGridPpalOfHeader(3,0, parame);
    comp.insertLabelInGridPpalOfHeader(5,0, encabezado);
    comp.insertLabelInGridPpalOfHeader(5,1, UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual())+" - "+UtilidadFecha.getHoraActual());
    
   logger.info("consulta modificada >><"+comp.obtenerQueryDataSet());
       //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
       comp.updateJDBCParameters(newPathReport);
        
       if(!newPathReport.equals(""))
        {
    	   request.setAttribute("isOpenReport", "true");
           request.setAttribute("newPathReport", newPathReport);
        }
       
    UtilidadBD.closeConnection(con);
	return mapping.findForward("detconsultarmodificarRango");
		
	}





	private void ordenarConsultarModificarXPaxienteDet(FormatoJustArtNoposForm forma) {
    	String [] indices={
    			"codigo_articulo_",
    			"profesional_",
    			"articulo_",
    			"nojus_", 
    			"fechajus_" ,
    			"subcuentascantidad_" ,
    			"subcuenta_" ,
    			"fecha_" ,
    			"solicitud_" ,
    			"codigo_convenio_" ,
    			"codigo_centro_costo_" ,
    			"ingreso_",
    			"tipo_jus_" 
    		}; 
    	
    	int numReg = Utilidades.convertirAEntero(forma.getIngresosArticulos().get("numRegistros")+"");
    		forma.setIngresosArticulos(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getIngresosArticulos(),numReg));
    		forma.setUltimoPatron(forma.getPatronOrdenar());
    		forma.setIngresosArticulos("numRegistros",numReg+"");
    		forma.setIngresosArticulos("INDICES_MAPA",indices);
		
	}





	private ActionForward generarReporte(Connection con, FormatoJustArtNoposForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente) 
	{
       	
    	String nombreRptDesign = "ListadoJusArtPendiente.rptdesign";
		
    	InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        String parame="Parámetros de generación del listado \n";
        
        comp.obtenerComponentesDataSet("ListadoJusArtPendiente");
        
       String valida1="";
       
		if (Utilidades.convertirAEntero(forma.getTipoCodigo())==1)
		{
			valida1+="	jpa.articulo as codigo_art " ;
		}
		else
			if(Utilidades.convertirAEntero(forma.getTipoCodigo())==2)
			{
				valida1+="	art.codigo_interfaz as codigo_art " ;
			}
		else
			if(Utilidades.convertirAEntero(forma.getTipoCodigo())==3)
			{
				valida1+="coalesce(jpa.articulo || '',' ') || ' - ' || coalesce(art.codigo_interfaz || '', ' ') as codigo_art" ;
			}
       
       logger.info("consulta1 >>>"+comp.obtenerQueryDataSet());
       
       String newquery=comp.obtenerQueryDataSet().replaceAll("'1' AS uno ", valida1);
       comp.modificarQueryDataSet(newquery);
       
		String[] viapac=forma.getFiltros().get("viaIngreso").toString().split(ConstantesBD.separadorSplit);
		String consulta="";
		String articulos=forma.getArticulosMap().get("codigosArticulos").toString();
       
	if(!viapac[0].toString().equals("-1"))
		{
		consulta=" c.via_ingreso="+viapac[0]+" " ;
		newquery=comp.obtenerQueryDataSet().replaceAll("1=1", consulta);
		comp.modificarQueryDataSet(newquery);
		parame+=" [Via Ingreso / Tipo Paciente :"+forma.getIngresosArticulos().get("viaingresotipopac_0")+"] ";
		} 
	/*if(!viapac[0].toString().equals("-1") && !viapac[1].toString().equals(""))
		{
		consulta=" c.tipo_paciente='"+viapac[1]+"' " ;
		newquery=comp.obtenerQueryDataSet().replaceAll("2=2", consulta);
		comp.modificarQueryDataSet(newquery);
		parame+=" [Tipo Paciente:"+viapac[1]+"] ";
		}*/
	if(!forma.getFiltros().get("centrocosto").toString().equals(""))
		{
		consulta=" c.area="+forma.getFiltros().get("centrocosto")+" " ;
		newquery=comp.obtenerQueryDataSet().replaceAll("3=3", consulta);
		comp.modificarQueryDataSet(newquery);
		parame+=" [Area:"+forma.getFiltros().get("centrocosto")+"] ";
		}
	if(!forma.getFiltros().get("convenio").toString().equals(""))
		{
		consulta=" sc.convenio="+forma.getFiltros().get("convenio")+" " ;
		newquery=comp.obtenerQueryDataSet().replaceAll("4=4", consulta);
		comp.modificarQueryDataSet(newquery);
		parame+=" [Convenio:"+forma.getFiltros().get("convenio")+"] ";
		}
	if(!articulos.equals(""))
		{
		consulta=" jpa.articulo IN ("+ConstantesBD.codigoNuncaValido+","+articulos+""+ConstantesBD.codigoNuncaValido+") " ;
		newquery=comp.obtenerQueryDataSet().replaceAll("5=5", consulta);
		comp.modificarQueryDataSet(newquery);
		parame+=" [Articulo (s):"+articulos+"] ";
		}
	
	
	String tipoBD = System.getProperty("TIPOBD");
	
	
	String filtrofijo="";
    if(tipoBD.equals("POSTGRESQL")){
    	 filtrofijo=" s.fecha_solicitud BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaFinal())+"' " ;
    }else{
    	 filtrofijo="  to_date(s.fecha_solicitud) BETWEEN to_date('"+forma.getFechaInicial()+"','DD/MM/YY') AND to_date('"+forma.getFechaFinal()+"','DD/MM/YY') " ;
    }
	
	
	
	
	
	
	
	newquery=comp.obtenerQueryDataSet().replaceAll("7=7", filtrofijo);
	comp.modificarQueryDataSet(newquery);
	parame+=" [Rango de Fechas:"+forma.getFechaInicial()+"-"+forma.getFechaFinal()+"] ";
	Utilidades.imprimirMapa(forma.getFiltros());
	
	filtrofijo=" i.centro_atencion="+forma.getFiltros().get("centroAtencionCod")+" " ;
	newquery=comp.obtenerQueryDataSet().replaceAll("8=8", filtrofijo);
	comp.modificarQueryDataSet(newquery);
	parame+=" [Centro de Atención:"+forma.getFiltros().get("centroAtencion")+"] ";

    
    comp.insertLabelInGridPpalOfHeader(3,0, parame);
   logger.info("consulta modificada >><"+newquery);
       //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
       comp.updateJDBCParameters(newPathReport);
        
       if(!newPathReport.equals(""))
        {
    	   request.setAttribute("isOpenReport", "true");
           request.setAttribute("newPathReport", newPathReport);
        }
       
    UtilidadBD.closeConnection(con);
	return mapping.findForward("detsolicitudesRango");
	
	}
    
    
    
/**
 * 
 * @param forma
 * @param con
 * @param mapping
 * @param usuario
 * @param paciente
 * @param request
 * @param response
 * @return
 */
    
    private ActionForward accionConsultarModificarRango(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, HttpServletResponse response) {
		
    	forma.resetanexo();
    	forma.setFiltros("centroAtencion",usuario.getCodigoCentroAtencion());
    	forma.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion()+"");
    	forma.setCodigoViaIngreso(ConstantesBD.codigoNuncaValido+"");
    	forma.setCentroAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(con, usuario.getCodigoInstitucionInt(),""));
    	forma.setViaIngresoPaciente(Utilidades.obtenerViasIngresoTipoPaciente(con));
    	forma.setCentrosCosto(UtilidadesManejoPaciente.obtenerCentrosCostoViaingreso(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaDirecto+"", true, 0));
    	forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", false));
    	
    	HashMap mapa=new HashMap();
    	
    	mapa.put("institucion", usuario.getCodigoInstitucionInt());
    	
    	forma.setPisos(UtilidadesManejoPaciente.obtenerPisos(con, mapa));
    	forma.setProfesionales(UtilidadesManejoPaciente.obtenerProfesionales(con, usuario.getCodigoInstitucionInt(), true, false, "",""));
    	forma.setCargarsi(1);
    	accionCargarCentrosCosto(forma, con, mapping, usuario, paciente, request, response);
    	
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("consultarmodificarXRangos");
   	}

    /**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @param request
     * @return
     */
    private ActionForward accionRecargarConsutarModificarRangos(
			FormatoJustArtNoposForm forma, Connection con,
			ActionMapping mapping, UsuarioBasico usuario,
			PersonaBasica paciente, HttpServletRequest request) 
    {
		forma.setFiltros((HashMap)forma.getFiltrosRecargar().clone());
		forma.setEstado("consultarModificarRangos");
		return accionConsutarModificarRangos(forma, con, mapping, usuario, paciente, request);
	}
    
    
	private ActionForward accionConsutarModificarRangos(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) {
		
		ActionErrors errors=new ActionErrors();
		//Validacion rango fechas
		forma.setFiltrosRecargar((HashMap)forma.getFiltros().clone());
		
		if(!forma.getFechaInicial().equals("")&&!forma.getFechaFinal().equals("")) {
			if(UtilidadFecha.validarFecha(forma.getFechaInicial())&&UtilidadFecha.validarFecha(forma.getFechaFinal())) {
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaInicial(),UtilidadFecha.getFechaActual()))
					errors.add("Fecha inicial mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","inicial","actual"));
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaFinal(),UtilidadFecha.getFechaActual()))
					errors.add("Fecha final mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","final","actual"));
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getFechaFinal(),forma.getFechaInicial()))
					errors.add("Fecha inicial mayor a la fecha final",new ActionMessage("errors.fechaAnteriorIgualActual","final","inicial"));
				else if(UtilidadFecha.numeroMesesEntreFechas(forma.getFechaInicial(),forma.getFechaFinal(),true)>3)
					errors.add("Rango de fechas > a 3 meses",new ActionMessage("errors.rangoMayorTresMeses","para consulta de justificaciones"));
			}
			else {
				if(!UtilidadFecha.validarFecha(forma.getFechaInicial()))
					errors.add("Fecha Inicial inválida",new ActionMessage("errors.formatoFechaInvalido","inicial"));
				if(!UtilidadFecha.validarFecha(forma.getFechaFinal()))
					errors.add("Fecha Final inválida",new ActionMessage("errors.formatoFechaInvalido","final"));
			}
		}

		else {
			if(forma.getFechaInicial().equals(""))
				errors.add("fecha Inicial requerida",new ActionMessage("errors.required","La Fecha Inicial"));
			else if(!UtilidadFecha.validarFecha(forma.getFechaInicial()))
				errors.add("Fecha Inicial inválida",new ActionMessage("errors.formatoFechaInvalido","inicial"));
			else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaInicial(),UtilidadFecha.getFechaActual()))
				errors.add("Fecha inicial mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","inicial","actual"));
			
			if(forma.getFechaFinal().equals(""))
				errors.add("fecha Final requerida",new ActionMessage("errors.required","La Fecha Final"));
			else if(!UtilidadFecha.validarFecha(forma.getFechaFinal()))
				errors.add("Fecha Final inválida",new ActionMessage("errors.formatoFechaInvalido","final"));
			else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaFinal(),UtilidadFecha.getFechaActual()))
				errors.add("Fecha final mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","final","actual"));
		}			
			
		//validacion centro de atencion, tipo de rompimiento y tipo de reporte
		if (!forma.getFiltros().containsKey("tipoReporte") || forma.getFiltros().get("tipoReporte").toString().equals("") || forma.getFiltros().get("tipoReporte").toString().equals(null))
			errors.add("Tipo Reporte requerido",new ActionMessage("errors.required","El tipo de Reporte"));
		
		if (!forma.getFiltros().containsKey("tipoRompimiento") || forma.getFiltros().get("tipoRompimiento").toString().equals("") || forma.getFiltros().get("tipoRompimiento").toString().equals(null))
			errors.add("Tipo Rompimiento requerido",new ActionMessage("errors.required","El tipo de Rompimiento"));
		
		if (!forma.getFiltros().containsKey("centroAtencion") || forma.getFiltros().get("centroAtencion").toString().equals("") || forma.getFiltros().get("centroAtencion").toString().equals(null))
			errors.add("Centro de Atención requerido",new ActionMessage("errors.required","El Centro de Atención"));
			
		if(errors.isEmpty()) {
			if (forma.getFiltros().get("tipoReporte").toString().equals("0")) {
				FormatoJustArtNopos fjan=new FormatoJustArtNopos();
				logger.info("filtros >>>>>"+forma.getFiltros());
				forma.setIngresosArticulos(fjan.cargarInfoIngresoConsultarModificarRango(con, forma.getIngresosArticulos(), forma.getFiltros(), forma.getArticulosMap().get("codigosArticulos").toString(),	forma.getFechaInicial(), forma.getFechaFinal(), 0));
				forma.setFiltros("fechahora", UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual());
				
				//String [] indices=Listado.obtenerKeysMapa(forma.getIngresosArticulos());
				String [] indices={
									"codigo_centro_costo_",
									"centro_costo_",
									"codigo_convenio_",
									"convenio_", 
									"solicitud_" ,
									"fecha_" ,
									"articulo_" ,
									"ingreso_" ,
									"subcuenta_" ,
									"subcuentacantidad_" ,
									"codviaingreso_" ,
									"codtipopaciente_" ,
									"viaingresotipopac_" ,
									"fechahora_" ,
									"noingreso_" ,
									"valor_",
									"cantidadcargada_" ,
									"tipoid_" ,
									"nombrepac_" ,
									"codigoart_" ,
									"codigocuenta_" ,
									"codpaciente_" ,
									"cama_" ,
									"nojus_" ,
									"cantotorden_" ,
									"cantotdespacho_" ,
									"cantotadmin_" ,
									"tiempotratamiento_" ,
									"cantotconv_" ,
									"precioventot_" ,	
									"costounitario_" ,
									"preciocostot_" ,
									"codigo_art_" ,
									"piso_",
									"estadojus_",
									"codigomed_",
									"control_",
									"codigo_articulo_principal_",
									"desc_articulo_principal_"
								}; 
				
				forma.setPatronOrdenar("convenio_");
				int numReg = Utilidades.convertirAEntero(forma.getIngresosArticulos().get("numRegistros")+"");

				forma.setIngresosArticulos(Listado.ordenarMapaRompimiento(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getIngresosArticulos(), forma.getFiltros().get("tipoRompimiento").toString()));
				forma.setUltimoPatron(forma.getPatronOrdenar());
				forma.setIngresosArticulos("numRegistros",numReg+"");
				forma.setIngresosArticulos("INDICES_MAPA",indices);
				forma.setFiltros("centroAtencionImp",forma.getFiltros().get("centroAtencion").toString());
				forma.setFiltros("centrocostoImp", forma.getFiltros().get("centrocosto").toString());
				forma.setFiltros("convenioImp", forma.getFiltros().get("convenio").toString());
				forma.setFiltros("tiporegimen", "");

				if (!forma.getFiltros().get("convenio").toString().equals(""))
					forma.setFiltros("tiporegimen", Utilidades.obtenerTipoRegimenConvenio(con, forma.getFiltros().get("convenio").toString()));
				
				forma.setFiltros("centroAtencion", Utilidades.obtenerNombreCentroAtencion(con,(Utilidades.convertirAEntero(forma.getFiltros().get("centroAtencion").toString()))));
				//	forma.setFiltros("piso", Utilidades.obtenerNombreCentroAtencion(con,(Utilidades.convertirAEntero(forma.getFiltros().get("piso").toString()))));
				
				if(!forma.getFiltros().get("centrocosto").toString().equals("")) {
					forma.setFiltros("centrocosto", Utilidades.obtenerNombreCentroCosto(con,(Utilidades.convertirAEntero(forma.getFiltros().get("centrocosto").toString())), usuario.getCodigoInstitucionInt()));
				}

				//forma.setFiltros("estadojus", Utilidades.obtenerNombreCentroAtencion(con,(Utilidades.convertirAEntero(forma.getFiltros().get("estadojus").toString()))));
				if(!forma.getFiltros().get("convenio").toString().equals("")) {
					forma.setFiltros("convenio", Utilidades.obtenerNombreConvenioOriginal(con,(Utilidades.convertirAEntero(forma.getFiltros().get("convenio").toString()))));
				}
				
				UtilidadBD.closeConnection(con);
				return mapping.findForward("detconsultarmodificarRango");
			}

			else if (forma.getFiltros().get("tipoReporte").toString().equals("1"))
			{
				FormatoJustArtNopos fjan=new FormatoJustArtNopos();
				
				logger.info("filtros >>>>>"+forma.getFiltros());
				
				forma.setIngresosArticulos(fjan.cargarInfoIngresoConsultarModificarRangocon(con, 
											forma.getIngresosArticulos(), 
											forma.getFiltros(), 
											forma.getArticulosMap().get("codigosArticulos").toString(), 
											forma.getFechaInicial(), 
											forma.getFechaFinal(),
											0));
				
				forma.setFiltros("fechahora", UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual());
				
				//String [] indices=Listado.obtenerKeysMapa(forma.getIngresosArticulos());
				
				String [] indices={
									"codigo_centro_costo_",
									"centro_costo_",
									"codigo_convenio_",
									"convenio_", 
									"solicitud_" ,
									"fecha_" ,
									"articulo_" ,
									"ingreso_" ,
									"subcuenta_" ,
									"subcuentacantidad_" ,
									"codviaingreso_" ,
									"codtipopaciente_" ,
									"viaingresotipopac_" ,
									"fechahora_" ,
									"noingreso_" ,
									"valor_",
									"cantidadcargada_" ,
									"tipoid_" ,
									"nombrepac_" ,
									"codigoart_" ,
									"codigocuenta_" ,
									"codpaciente_" ,
									"cama_" ,
									"nojus_" ,
									"cantotorden_" ,
									"cantotdespacho_" ,
									"cantotadmin_" ,
									"tiempotratamiento_" ,
									"cantotconv_" ,
									"precioventot_" ,	
									"costounitario_" ,
									"preciocostot_" ,
									"codigo_art_" ,
									"piso_",
									"estadojus_",
									"cantsol_",
									"codigomed_",
									"control_"
								}; 
				
				forma.setPatronOrdenar("articulo_");
				forma.setUltimoPatron("codigomed_");
				int numReg = Utilidades.convertirAEntero(forma.getIngresosArticulos().get("numRegistros")+"");
				forma.setIngresosArticulos(Listado.ordenarMapaRompimiento(indices, 
																		  forma.getPatronOrdenar(), 
																		  forma.getUltimoPatron(), 
																		  forma.getIngresosArticulos(), 
																		  forma.getFiltros().get("tipoRompimiento").toString()));
				forma.setUltimoPatron(forma.getPatronOrdenar());
				forma.setIngresosArticulos("numRegistros",numReg+"");
				forma.setIngresosArticulos("INDICES_MAPA",indices);
				forma.setFiltros("centroAtencionImp",forma.getFiltros().get("centroAtencion").toString());
				forma.setFiltros("centrocostoImp", forma.getFiltros().get("centrocosto").toString());
				forma.setFiltros("convenioImp", forma.getFiltros().get("convenio").toString());
				
				forma.setFiltros("centroAtencion", Utilidades.obtenerNombreCentroAtencion(con,(Utilidades.convertirAEntero(forma.getFiltros().get("centroAtencion").toString()))));
				//	forma.setFiltros("piso", Utilidades.obtenerNombreCentroAtencion(con,(Utilidades.convertirAEntero(forma.getFiltros().get("piso").toString()))));
				if(!forma.getFiltros().get("centrocosto").toString().equals(""))
				{
					forma.setFiltros("centrocosto", Utilidades.obtenerNombreCentroCosto(con,(Utilidades.convertirAEntero(forma.getFiltros().get("centrocosto").toString())), usuario.getCodigoInstitucionInt()));
				}
				//forma.setFiltros("estadojus", Utilidades.obtenerNombreCentroAtencion(con,(Utilidades.convertirAEntero(forma.getFiltros().get("estadojus").toString()))));
				if(!forma.getFiltros().get("convenio").toString().equals(""))
				{
					forma.setFiltros("convenio", Utilidades.obtenerNombreConvenioOriginal(con,(Utilidades.convertirAEntero(forma.getFiltros().get("convenio").toString()))));
				}
				UtilidadBD.closeConnection(con);	
			return mapping.findForward("detconsultarmodificarRangocon");	
			}
		}
		else {
			forma.setEstado("consultarmodificarXRangos");
			saveErrors(request,errors);	
			UtilidadBD.closeConnection(con);
			return mapping.findForward("consultarmodificarXRangos");
		}
		return null;
	}

	
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @param request 
     * @return
     */
	private ActionForward accionConsutarRangos(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) {
		
		ActionErrors errors=new ActionErrors();
		forma.setIngresosArticulos(new HashMap());
			
			//Validacion rango fechas
			if(!forma.getFechaInicial().equals("")&&!forma.getFechaFinal().equals(""))
			{
				if(UtilidadFecha.validarFecha(forma.getFechaInicial())&&UtilidadFecha.validarFecha(forma.getFechaFinal()))
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaInicial(),UtilidadFecha.getFechaActual()))
						errors.add("Fecha inicial mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","inicial","actual"));
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaFinal(),UtilidadFecha.getFechaActual()))
						errors.add("Fecha final mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","final","actual"));
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getFechaFinal(),forma.getFechaInicial()))
						errors.add("Fecha inicial mayor a la fecha final",new ActionMessage("errors.fechaAnteriorIgualActual","final","inicial"));
					else if(UtilidadFecha.numeroMesesEntreFechas(forma.getFechaInicial(),forma.getFechaFinal(),true)>3)
						errors.add("Rango de fechas > a 3 meses",new ActionMessage("errors.rangoMayorTresMeses","para consulta de justificaciones"));
				}
				else
				{
					if(!UtilidadFecha.validarFecha(forma.getFechaInicial()))
						errors.add("Fecha Inicial inválida",new ActionMessage("errors.formatoFechaInvalido","inicial"));
					if(!UtilidadFecha.validarFecha(forma.getFechaFinal()))
						errors.add("Fecha Final inválida",new ActionMessage("errors.formatoFechaInvalido","final"));
					
				}
			}
			else
			{
				if(forma.getFechaInicial().equals(""))
					errors.add("fecha Inicial requerida",new ActionMessage("errors.required","La Fecha Inicial"));
				else if(!UtilidadFecha.validarFecha(forma.getFechaInicial()))
					errors.add("Fecha Inicial inválida",new ActionMessage("errors.formatoFechaInvalido","inicial"));
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaInicial(),UtilidadFecha.getFechaActual()))
					errors.add("Fecha inicial mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","inicial","actual"));
				
				if(forma.getFechaFinal().equals(""))
					errors.add("fecha Final requerida",new ActionMessage("errors.required","La Fecha Final"));
				else if(!UtilidadFecha.validarFecha(forma.getFechaFinal()))
					errors.add("Fecha Final inválida",new ActionMessage("errors.formatoFechaInvalido","final"));
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaFinal(),UtilidadFecha.getFechaActual()))
					errors.add("Fecha final mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","final","actual"));
			}			
	
	if(errors.isEmpty())
		{
		FormatoJustArtNopos fjan=new FormatoJustArtNopos();
		
		//logger.info("\n\n\n tipo codigo >>>>"+forma.getTipoCodigo());
		
	
		forma.setIngresosArticulos(fjan.cargarInfoIngresoRango(con, 
									forma.getIngresosArticulos(), 
									forma.getFiltros(), 
									forma.getArticulosMap().get("codigosArticulos").toString(), 
									forma.getFechaInicial(), 
									forma.getFechaFinal(),
									Utilidades.convertirAEntero(forma.getTipoCodigo().toString())));
		
		//logger.info("Mapaa ingresos por rangos >>>>"+forma.getIngresosArticulos());
		
		if(forma.getIngresosArticulos().get("numRegistros")==null){
			forma.getIngresosArticulos().put("numRegistros", 0);
		}
		
		
		
		forma.setFiltros("centroAtencionCod", forma.getFiltros().get("centroAtencion"));
		forma.setFiltros("centroAtencion", Utilidades.obtenerNombreCentroAtencion(con,(Utilidades.convertirAEntero(forma.getFiltros().get("centroAtencion").toString()))));
		//	forma.setFiltros("piso", Utilidades.obtenerNombreCentroAtencion(con,(Utilidades.convertirAEntero(forma.getFiltros().get("piso").toString()))));
		if(!forma.getFiltros().get("centrocosto").toString().equals(""))
		{
		forma.setFiltros("centrocosto", Utilidades.obtenerNombreCentroCosto(con,(Utilidades.convertirAEntero(forma.getFiltros().get("centrocosto").toString())), usuario.getCodigoInstitucionInt()));
		}
		//forma.setFiltros("estadojus", Utilidades.obtenerNombreCentroAtencion(con,(Utilidades.convertirAEntero(forma.getFiltros().get("estadojus").toString()))));
		if(!forma.getFiltros().get("convenio").toString().equals(""))
		{
		forma.setFiltros("convenio", Utilidades.obtenerNombreConvenioOriginal(con,(Utilidades.convertirAEntero(forma.getFiltros().get("convenio").toString()))));
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detsolicitudesRango");
		}
		else
		{
			saveErrors(request,errors);	
			UtilidadBD.closeConnection(con);
			return mapping.findForward("ingresarXRangos");
		}
	}



	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarArticulo(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		
		HashMap mapa = forma.getArticulosMap();
		String codigosArticulos = forma.getArticulosMap().get("codigosArticulos").toString();
		
		codigosArticulos = codigosArticulos.replaceAll(mapa.get("codigo_"+forma.getIndexMap())+",", "");
		
		eliminarRegistroMapaArticulos(mapa, forma.getIndexMap());
		
		forma.getArticulosMap().put("codigosArticulos", codigosArticulos);
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("ingresarXRangos");
	}

	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarArticuloc(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
		
		HashMap mapa = forma.getArticulosMap();
		String codigosArticulos = forma.getArticulosMap().get("codigosArticulos").toString();
		
		codigosArticulos = codigosArticulos.replaceAll(mapa.get("codigo_"+forma.getIndexMap())+",", "");
		
		eliminarRegistroMapaArticulos(mapa, forma.getIndexMap());
		
		forma.getArticulosMap().put("codigosArticulos", codigosArticulos);
		
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("consultarmodificarXRangos");
	}

	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionAdicionarArticulo(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
	
		HashMap mapa = forma.getArticulosMap();
    	String codigosArticulos = forma.getArticulosMap().get("codigosArticulos").toString();
    	int pos=Utilidades.convertirAEntero(forma.getArticulosMap().get("numRegistros")+"");
    	
     	mapa.put("codigo_"+pos,forma.getCodigoArticulo());
     	mapa.put("descripcion_"+pos,forma.getDescripcionArticulo());
    	mapa.put("numRegistros", (pos+1)+"");
    	
		codigosArticulos += forma.getArticulosMap().get("codigo_"+pos).toString() + ",";
		forma.getArticulosMap().put("codigosArticulos", codigosArticulos);
		
		forma.setDescripcionArticulo("");
		
		UtilidadBD.closeConnection(con);		
        return mapping.findForward("ingresarXRangos");
    	    	
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionAdicionarArticuloc(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
	
		HashMap mapa = forma.getArticulosMap();
    	String codigosArticulos = forma.getArticulosMap().get("codigosArticulos").toString();
    	int pos=Utilidades.convertirAEntero(forma.getArticulosMap().get("numRegistros")+"");
    	
     	mapa.put("codigo_"+pos,forma.getCodigoArticulo());
     	mapa.put("descripcion_"+pos,forma.getDescripcionArticulo());
    	mapa.put("numRegistros", (pos+1)+"");
    	
		codigosArticulos += forma.getArticulosMap().get("codigo_"+pos).toString() + ",";
		forma.getArticulosMap().put("codigosArticulos", codigosArticulos);
		
		forma.setDescripcionArticulo("");
		
		UtilidadBD.closeConnection(con);		
        return mapping.findForward("consultarmodificarXRangos");
    	    	
	}
	
    
    /**
     * 
     * @param mapa
     * @param pos
     * @return
     */
	private HashMap eliminarRegistroMapaArticulos(HashMap mapa, int pos){
    	int aux=pos+1;
		
    	for(int x=pos; x<Utilidades.convertirAEntero(mapa.get("numRegistros").toString()); x++)
		{
    		
    		mapa.put("codigo_"+x, mapa.get("codigo_"+aux));
    		mapa.put("descripcion_"+x, mapa.get("descripcion_"+aux));
    		aux++;
		}
    	aux = Utilidades.convertirAEntero(mapa.get("numRegistros").toString());
    	mapa.remove("codigo_"+aux);
    	mapa.remove("descripcion_"+aux);
    	mapa.put("numRegistros", aux-1);
    	
    	return mapa;
    }
    
    /**
     * 
     * @param mapa
     * @return
     */
	private HashMap crearCadenaConComas(HashMap mapa){
    	String cadena = "";
    	int x;
    	for(x=0; x<Utilidades.convertirAEntero(mapa.get("numRegistros").toString())-1; x++){
    		cadena += mapa.get("codigo_pk_"+x);
    		cadena += ",";
    	}
    	cadena += mapa.get("codigo_pk_"+x);
    	mapa.put("cadenaCodigos", cadena);
    	return mapa;
    }
    
/**
 * 
 * @param forma
 */  
private void accionOrdenarIngresarXPacientedet(FormatoJustArtNoposForm forma) {
	String[] indices = {"codigo_centro_costo_", "centro_costo_", "codigo_convenio_", "convenio_", "solicitud_", "fecha_", "codigo_articulo_", "articulo_", "ingreso_", "subcuenta_",""};
		int numReg = Utilidades.convertirAEntero(forma.getIngresosArticulos().get("numRegistros")+"");
		forma.setIngresosArticulos(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getIngresosArticulos(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setIngresosArticulos("numRegistros",numReg+"");
		forma.setIngresosArticulos("INDICES_MAPA",indices);
	} 


private void accionOrdenarIngresarXRangodet(FormatoJustArtNoposForm forma) {
	String[] indices = {"codigo_convenio_", 
						"codpaciente_", 
						"codigo_centro_costo_", 
						"ingreso_", 
						"subcuenta_", 
						"codigocuenta_", 
						"viaingresotipopac_", 
						"noingreso_",
						"fechahora_", 
						"centro_costo_",
						"convenio_", 
						"solicitud_", 
						"fecha_", 
						"tipoid_", 
						"nombrepac_", 
						"codigo_art_", 
						"articulo_", 
						"cantidad_", 
						"valor_",
						"codigoart_",
						"subcuentacantidad_",
						"codviaingreso_",
						"codtipopaciente_",
						""};
	
	int numReg = Utilidades.convertirAEntero(forma.getIngresosArticulos().get("numRegistros")+"");
		forma.setIngresosArticulos(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getIngresosArticulos(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setIngresosArticulos("numRegistros",numReg+"");
		forma.setIngresosArticulos("INDICES_MAPA",indices);
        

	} 


/**
 * 
 * @param forma
 */
private void ordenarConsultarModificarXRangodet(FormatoJustArtNoposForm forma) {
	String [] indices={
			"codigo_centro_costo_",
			"centro_costo_",
			"codigo_convenio_",
			"convenio_", 
			"solicitud_" ,
			"fecha_" ,
			"articulo_" ,
			"ingreso_" ,
			"subcuenta_" ,
			"subcuentacantidad_" ,
			"codviaingreso_" ,
			"codtipopaciente_" ,
			"viaingresotipopac_" ,
			"fechahora_" ,
			"noingreso_" ,
			"valor_",
			"cantidadcargada_" ,
			"tipoid_" ,
			"nombrepac_" ,
			"codigoart_" ,
			"codigocuenta_" ,
			"codpaciente_" ,
			"cama_" ,
			"nojus_" ,
			"cantotorden_" ,
			"cantotdespacho_" ,
			"cantotadmin_" ,
			"tiempotratamiento_" ,
			"cantotconv_" ,
			"precioventot_" ,	
			"costounitario_" ,
			"preciocostot_ " ,
			"codigo_art_" ,
			"piso_",
			"estadojus_",
			"control_",
			"codigo_articulo_principal_",
			"desc_articulo_principal_"
		}; 
	
	int numReg = Utilidades.convertirAEntero(forma.getIngresosArticulos().get("numRegistros")+"");
		forma.setIngresosArticulos(Listado.ordenarMapaRompimiento(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getIngresosArticulos(), forma.getFiltros().get("tipoRompimiento").toString()));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setIngresosArticulos("numRegistros",numReg+"");
		forma.setIngresosArticulos("INDICES_MAPA",indices);
        

	} 


/**
 * 
 * @param forma
 */
private void ordenarConsultarModificarXRangodetcon(FormatoJustArtNoposForm forma) {
	String [] indices={
			"codigo_centro_costo_",
			"centro_costo_",
			"codigo_convenio_",
			"convenio_", 
			"solicitud_" ,
			"fecha_" ,
			"articulo_" ,
			"ingreso_" ,
			"subcuenta_" ,
			"subcuentacantidad_" ,
			"codviaingreso_" ,
			"codtipopaciente_" ,
			"viaingresotipopac_" ,
			"fechahora_" ,
			"noingreso_" ,
			"valor_",
			"cantidadcargada_" ,
			"tipoid_" ,
			"nombrepac_" ,
			"codigoart_" ,
			"codigocuenta_" ,
			"codpaciente_" ,
			"cama_" ,
			"nojus_" ,
			"cantotorden_" ,
			"cantotdespacho_" ,
			"cantotadmin_" ,
			"tiempotratamiento_" ,
			"cantotconv_" ,
			"precioventot_" ,	
			"costounitario_" ,
			"preciocostot_" ,
			"codigo_art_" ,
			"piso_",
			"estadojus_",
			"cantsol_",
			"codigomed_",
			"control_"
		}; 
	
	int numReg = Utilidades.convertirAEntero(forma.getIngresosArticulos().get("numRegistros")+"");
		forma.setIngresosArticulos(Listado.ordenarMapaRompimiento(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getIngresosArticulos(), forma.getFiltros().get("tipoRompimiento").toString()));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setIngresosArticulos("numRegistros",numReg+"");
		forma.setIngresosArticulos("INDICES_MAPA",indices);
        

	} 






/**
 * 
 * @param forma
 */  
private void accionOrdenarIngresarXPaciente(FormatoJustArtNoposForm forma) {
		String[] indices = {"centro_atencion_", "nom_centro_atencion_", "via_ingreso_", "num_ingreso_", "fecha_ingreso_", "fecha_egreso_", "estado_ingreso_", "nom_estado_ingreso_", "num_cuenta_", "nom_estado_cuenta_", "estado_cuenta_","capa_",""};
		int numReg = Utilidades.convertirAEntero(forma.getIngresos().get("numRegistros")+"");
		forma.setIngresos(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getIngresos(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setIngresos("numRegistros",numReg+"");
		forma.setIngresos("INDICES_MAPA",indices);
	} 
    
    
private ActionForward accionCargarCentrosCosto(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, HttpServletResponse response) {
	
	String resultado = "<respuesta>" +
	"<infoid>" +
		"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
		"<id-select>centrocosto</id-select>" +
		"<id-arreglo>centro-costo</id-arreglo>" +
		"<activar-seleccione>"+ConstantesBD.acronimoSi+"</activar-seleccione>" ; //nombre de la etiqueta de cada elemento
		if(forma.getCargarsi()==1)
		{
			resultado+="<id-select>piso</id-select>" +
						"<id-arreglo>piso</id-arreglo>" ; //nombre de la etiqueta de cada elemento
		}
	resultado+="</infoid>";
	
// aqui iteramos el arraylist de centros de costo para hacer el filtro por centros de atencion y via ingreso
	String select="";
	
	
	for(int i=0;i<forma.getCentrosCosto().size();i++)
	{
		if(forma.getCentrosCosto().get(i).get("codcentroatencion").toString().equals(""+forma.getCodigoCentroAtencion())	)
		{
			if(forma.getCodigoViaIngreso().equals(ConstantesBD.codigoNuncaValido+""))
			{
				if (select.indexOf(forma.getCentrosCosto().get(i).get("codigo")+"")<0)
				{
				select+=forma.getCentrosCosto().get(i).get("codigo")+" , ";
				resultado += "<centro-costo>";
				resultado += "<codigo>"+forma.getCentrosCosto().get(i).get("codigo")+"</codigo>";
				resultado += "<descripcion>"+forma.getCentrosCosto().get(i).get("nomcentrocosto")+"</descripcion>";
				resultado += "</centro-costo>";
				}
			}
			else 
				{
					if(forma.getCentrosCosto().get(i).get("viaingtipopac").toString().equals(forma.getCodigoViaIngreso()))
					{
						resultado += "<centro-costo>";
						resultado += "<codigo>"+forma.getCentrosCosto().get(i).get("codigo")+"</codigo>";
						resultado += "<descripcion>"+forma.getCentrosCosto().get(i).get("nomcentrocosto")+"</descripcion>";
						resultado += "</centro-costo>";
					}
				}
		}
	}
	
if (forma.getCargarsi()==1)
{
	for(int i=0;i<forma.getPisos().size();i++)
	{
		if(forma.getPisos().get(i).get("idcentroatencion").toString().equals(""+forma.getCodigoCentroAtencion()+"")	)
		{		//MT6527 cambia el codigopiso por codigo para el filtro con piso
				resultado += "<piso>";
				resultado += "<codigo>"+forma.getPisos().get(i).get("codigo")+"</codigo>";
				resultado += "<descripcion>"+forma.getPisos().get(i).get("nombre")+"</descripcion>";
				resultado += "</piso>";
		}
	}
}
	
resultado += "</respuesta>";
	UtilidadBD.closeConnection(con);
	//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
	try
	{
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(resultado);
   }
	catch(IOException e)
	{
		logger.error("Error al enviar respuesta AJAX en accionFormatoJustArtNoPos: "+e);
	}
	
return null;
	
	}




private ActionForward accionIngresarRango(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, HttpServletResponse response) {
		
	forma.resetanexo();
	forma.setFiltros("centroAtencion",usuario.getCodigoCentroAtencion());
	forma.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion()+"");
	forma.setCodigoViaIngreso(ConstantesBD.codigoNuncaValido+"");
	
	forma.setCentroAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(con, usuario.getCodigoInstitucionInt(),""));
	forma.setViaIngresoPaciente(Utilidades.obtenerViasIngresoTipoPaciente(con));
	forma.setCentrosCosto(UtilidadesManejoPaciente.obtenerCentrosCostoViaingreso(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaDirecto+"", false, 0));
	forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", true));
	accionCargarCentrosCosto(forma, con, mapping, usuario, paciente, request, response);
	UtilidadBD.closeConnection(con);
	return mapping.findForward("ingresarXRangos");
	}




/**
 * Metodo que convierte el mapa y coloca el identificador de articulo codArt_key
 * @param mapa
 * @param indices
 * @param articulo
 * 
 */    
 private HashMap convierteMapa (HashMap mapa, String [] indices, int articulo)
 {
	 HashMap mapaC=new HashMap();

	 for(int x=0;x<indices.length;x++)
	 {
	 	mapaC.put(articulo+"_"+indices[x],mapa.get(indices[x]));
	 }
	 
	 return mapaC;
 
 }
    
    
/**
 * 
 * @param forma
 * @param con
 * @param mapping
 * @param usuario
 * @param paciente
 * @param request
 * @return
 */    
 
 	private ActionForward accionGuardarJustificacionIngresar(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) {

 		FormatoJustArtNopos fjan = new FormatoJustArtNopos ();


 		// SE ADICIONA EL IDENTIFICADOR DE ARTICULO A LAS LLAVES DE LOS MAPAS PARA QUE EL METODO FUNCIONE
 		//se Obtienen las llaves de los mapas

 		fjan.setIndicesformulariomap(Listado.obtenerKeysMapa(forma.getFormularioMap()));
 		fjan.setIndicesnoposmap(Listado.obtenerKeysMapa(forma.getMedicamentosNoPos()));
 		fjan.setIndicesposmap(Listado.obtenerKeysMapa(forma.getMedicamentosPos()));
 		fjan.setIndicessustimap(Listado.obtenerKeysMapa(forma.getSustitutosNoPos()));
 		fjan.setIndicesdiagmap(Listado.obtenerKeysMapa((HashMap)forma.getDiagnosticosDefinitivos()));
 		fjan.setIndicesdiag1map(Listado.obtenerKeysMapa((HashMap)forma.getDiagnosticosPresuntivos()));

 		forma.setFormularioMap(convierteMapa(forma.getFormularioMap(), fjan.getIndicesformulariomap(), Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString())));
 		forma.setMedicamentosNoPos(convierteMapa(forma.getMedicamentosNoPos(), fjan.getIndicesnoposmap(), Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString())));
 		forma.setMedicamentosPos(convierteMapa(forma.getMedicamentosPos(), fjan.getIndicesposmap(), Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString())));
 		forma.setSustitutosNoPos(convierteMapa(forma.getSustitutosNoPos(), fjan.getIndicessustimap(), Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString())));
 		forma.setDiagnosticosDefinitivos(convierteMapa((HashMap)forma.getDiagnosticosDefinitivos(), fjan.getIndicesdiagmap(), Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString())));
 		forma.setDiagnosticosPresuntivos(convierteMapa((HashMap)forma.getDiagnosticosPresuntivos(), fjan.getIndicesdiag1map(), Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString())));

 		//se incluye en los mapas los datos en la representacion requerida por el metodo asi como si se estuviera haciendo el proceso de form a form
 		logger.info("\n\n\n [index sustitutos] \n\n\n"+forma.getIndexSelectSustitutos());
	
 		forma.getMedicamentosPos().put(forma.getMedicamentoNoPos()+"_articulo", forma.getMedicamentoPos());
 		forma.getSustitutosNoPos().put(forma.getMedicamentoNoPos()+"_indexSelectSustitutos", forma.getIndexSelectSustitutos());
 		forma.getFormularioMap().put(forma.getMedicamentoNoPos()+"__archivo", forma.getArchivo());
 		forma.getDiagnosticosDefinitivos().put(forma.getMedicamentoNoPos()+"_complicacion", forma.getDiagnosticoComplicacion_1());
 		forma.getDiagnosticosDefinitivos().put(forma.getMedicamentoNoPos()+"_numdiagnosticos", forma.getNumDiagnosticosDefinitivos());

		logger.info("respuestaClinicaParaclinica: " + forma.getMedicamentoNoPos()+"_respuestaClinicaParaclinica");
		logger.info("respuesta desde la forma" + forma.getRespuestaClinicaParaclinica());
		logger.info("resumen: " + forma.getMedicamentoNoPos()+"_observacionesResumen");
		logger.info("resumen 2: " + forma.getObservacionesResumen());

 		//forma.getFormularioMap().put(forma.getMedicamentoNoPos()+"_respuestaClinicaParaclinica", forma.getRespuestaClinicaParaclinica());
 		forma.getFormularioMap().put(forma.getMedicamentoNoPos()+"_observacionesResumen", forma.getObservacionesResumen());
 		forma.getFormularioMap().put(forma.getMedicamentoNoPos()+"_respuestaClinicaParaclinica", forma.getRespuestaClinicaParaclinica());
 		
 		logger.info("-----------------------------------------");
 		logger.info("" + forma.getObservacionesResumen());
 		logger.info("-----------------------------------------");
 		logger.info("" + UtilidadTexto.observacionAHTML(forma.getObservacionesResumen()));
 		logger.info("-----------------------------------------");
 		logger.info("" + UtilidadTexto.deshacerCodificacionHTML(forma.getObservacionesResumen()));
	

 		// ******** RESPONSABLES DE LA JUSTIFICACIÓN.
 		HashMap responsables = UtilidadJustificacionPendienteArtServ.obtenerResponsablesJusPendiente(con, Utilidades.convertirAEntero(forma.getCodigoSolicitud().toString()), Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString()), true);

 		
 		// primer responsable
 		if (responsables.get("subcuenta_0")!=null)
 			forma.setFormularioMap(forma.getMedicamentoNoPos()+"_subcuenta", responsables.get("subcuenta_0"));
		//forma.setMedicamentosNoPos(forma.getMedicamentoNoPos()+"_cantidad_0", responsables.get("cantidad_0"));
	
		
		
		
		logger.info("Cantidad del Responsable: " + responsables.get("cantidad_0"));
		logger.info("Subcuenta del Responsable: " + responsables.get("subcuenta_0"));
		logger.info("NumRegistros Responsables: " + responsables.get("numRegistros"));


		// %%%%%%%%%%%%%%%%% METODO PARA ADICIONAR JUSTIFICACION NO POS %%%%%%%%%%%%%%%%%%%%%
		//EN ESTE CASO SE INSERTA LA CANTIDAD, DOSIFICACION Y TIEMPO TRATAMIENTO -> DATOS OBTENIDOS POR TECLADO "NO REQUERIDOS"
		//la frecuencia.. campo16 estaba en 0 como int

	
		logger.info("<< nnnnnnnnnnnnnnnnnnnNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN");
		Utilidades.imprimirMapa(forma.getMedicamentosNoPos());
	
		logger.info("<< mmmmmmmmmmmmmmmmmMMMMMMMMMMMMMMMMMMMMMMMMMMM");
		Utilidades.imprimirMapa(forma.getMedicamentosPos());
	
	
		logger.info("<< Tiempo Tratamiento: " + forma.getMedicamentosNoPos(forma.getMedicamentoNoPos()+"_tiempotratamiento_0").toString());
	
		UtilidadBD.iniciarTransaccion(con);
	
		int u = fjan.insertarJustificacion(	con, 
								Utilidades.convertirAEntero(forma.getCodigoSolicitud().toString()), 
								ConstantesBD.codigoNuncaValido,
								forma.getFormularioMap(), 
								forma.getMedicamentosNoPos(), 
								forma.getMedicamentosPos(), 
								forma.getSustitutosNoPos(), 
								(HashMap)forma.getDiagnosticosDefinitivos(), 
								Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString()), 
								usuario.getCodigoInstitucionInt(), 
								"", 
								ConstantesBD.continuarTransaccion, 
								Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString()), 
								"", 
								"0", 
								"", 
								0, 
								"", 
								"", 
								Utilidades.convertirADouble(forma.getMedicamentosNoPos(forma.getMedicamentoNoPos()+"_cantidad_0").toString()),  
								forma.getMedicamentosNoPos(forma.getMedicamentoNoPos()+"_tiempotratamiento_0").toString(), 
								usuario.getLoginUsuario()
								);

		// SE INSERTAN SI EXISTEN LOS REGISTROS DE LOS OTROS RESPONSABLES.

		//String subcuentas[] = forma.getSubcuentascantidad().split(", ");
		//logger.info("subcuentas cantidad >>>"+forma.getSubcuentascantidad());

		int h=0;

		//Utilidades.imprimirMapa(responsables);
		logger.info("Ya se ingreso la justificación - Estado Inserccion Justificacion: " + u);
	
		if(u > 0) {
			// Ingresamos el registro para otro responsable si lo tiene
			for(int j=1; j<Utilidades.convertirAEntero(responsables.get("numRegistros").toString()); j++) {
				logger.info("------ "+j);
				if(!fjan.ingresarResponsable(con, forma.getCodigoSolicitud().toString(), Utilidades.convertirAEntero(responsables.get("subcuenta_"+j)+""), Utilidades.convertirAEntero(responsables.get("cantidad_"+j)+""), Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString())))
					h++;
				if(h==1)
					UtilidadBD.abortarTransaccion(con);
			}

			/*for(int j=1; j<subcuentas.length; j++) {
				if(!fjan.ingresarResponsable(con,   forma.getCodigoSolicitud().toString(), 
												Utilidades.convertirAEntero(subcuentas[j].split(ConstantesBD.separadorSplit)[0]),
												Utilidades.convertirAEntero(subcuentas[j].split(ConstantesBD.separadorSplit)[1]),
												Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString())))
				{
					h++;
					if(h==1)
						UtilidadBD.abortarTransaccion(con);
				}
			}*/
		
			logger.info("se elimina el pendiente");
			// %%%%%%%%%%%	SE ELIMINA EL REGISTRO DE LA TABLA DE JUSTIFICACIONES PENDIENTES	%%%%%%%%%%%%%
			if(!UtilidadJustificacionPendienteArtServ.eliminarJusNoposPendiente(con, Utilidades.convertirAEntero(forma.getCodigoSolicitud().toString()), Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString()), true))
			{
				h++;
				if(h==1) {
					UtilidadBD.abortarTransaccion(con);
					logger.info("aborto 1");
				}	
			}
		}

		else {
			h++;
			if(h==1) {
				UtilidadBD.abortarTransaccion(con);
				logger.info("aborto 2");
			}	
		}

		if(h==0){
			UtilidadBD.finalizarTransaccion(con);	
		}
		UtilidadBD.closeConnection(con);
	
		logger.info("codigoSolicitud despues de guardar: "+forma.getCodigoSolicitud());
		return mapping.findForward("principal");
 	}

 	
/**
 * 
 * @param forma
 * @param con
 * @param mapping
 * @param usuario
 * @param paciente
 * @param response
 * @return
 */
private ActionForward accionCargarInfoJus(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletResponse response) {

	FormatoJustArtNopos fjan=new FormatoJustArtNopos();
	forma.setIngresosArticulos(new HashMap());
	forma.getIngresosArticulos().put("numRegistros", 0);
	forma.setIngresosArticulos(fjan.cargarInfoIngresoJus(con, forma.getIngresosArticulos(),forma.getIngreso(), forma.getCuenta()));
	UtilidadBD.closeConnection(con);		
//logger.info("mapa >>>"+forma.getIngresosArticulos());
	UtilidadBD.closeConnection(con);
	return mapping.findForward("detconsultarmodificarpac");
		
	}


/**
 * 
 * @param forma
 * @param con
 * @param mapping
 * @param usuario
 * @param paciente
 * @param response
 * @return
 */
private ActionForward accionCargarInfoIngresos(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletResponse response) {

	FormatoJustArtNopos fjan=new FormatoJustArtNopos();
	forma.setIngresosArticulos(new HashMap());
	forma.getIngresosArticulos().put("numRegistros", 0);
	forma.setIngresosArticulos(fjan.cargarInfoIngreso(con, forma.getIngresosArticulos(),forma.getIngreso(), forma.getCuenta()));
	UtilidadBD.closeConnection(con);		
//logger.info("mapa >>>"+forma.getIngresosArticulos());
	UtilidadBD.closeConnection(con);
	return mapping.findForward("detsolicitudes");
		
	}

/**
 * 
 * @param forma
 * @param con
 * @param mapping
 * @param usuario
 * @param paciente
 * @param request
 * @return
 */
private ActionForward accionCargarconsultarMosificarProPaciente(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) {
	forma.resetanexo();
	//Se Cargan los ingresos - cuenta
	forma.setIngresos(UtilidadesHistoriaClinica.consultarIngresosXPaciente(con, paciente.getCodigoPersona()));
	UtilidadBD.closeConnection(con);
	return mapping.findForward("consultarmodificarXPaciente");
}


/**
 * 
 * @param forma
 * @param con
 * @param mapping
 * @param usuario
 * @param paciente
 * @param request
 * @return
 */    
	private ActionForward accionCargarIngresarProPaciente(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) {
		forma.resetanexo();
		//Se Cargan los ingresos - cuenta
		forma.setIngresos(UtilidadesHistoriaClinica.consultarIngresosXPaciente(con, paciente.getCodigoPersona()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ingresarXPaciente");
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
    private ActionErrors validarPacienteCargado(Connection con, FormatoJustArtNoposForm forma, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionErrors errores) {
    	if(paciente.getCodigoPersona()<=0)
			errores.add("Paciente Cargado", new ActionMessage("errors.required","Paciente Cargado"));
    	return errores;
	}

	/**
     * Función que valida que el usuario que ingresa sea un profesional de la salud
     * @param con
     * @param forma
     * @param usuario
     * @param request
     * @param errores
     * @return
     */
	private ActionErrors validarProfesionalDeLaSalud(Connection con, FormatoJustArtNoposForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionErrors errores) {
		if(!UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, true))
			errores.add("Validación Ocupación Medico Especialista", new ActionMessage("errors.required","Validación Ocupación Medico Especialista"));
		return errores;
	}


	/**
	 * 
	 */
	private ActionForward accionconsultarM(FormatoJustArtNoposForm medicamentosForm, Connection con, ActionMapping mapping, UsuarioBasico usuario)
	{
		AntecedentesMedicamentos medicamentos = new AntecedentesMedicamentos();
		medicamentosForm.setFormaFconcMap(medicamentos.consultaFormaConc(con, Utilidades.convertirAEntero(medicamentosForm.getInformacionArtPrin("codigoArticulo").toString())));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
    
    
    
    
    /**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param request 
     * @param usuario
     * @param paciente
     * @param request
     * @return
     */
	private ActionForward accionguardarjustificacion(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, HttpServletRequest request) {
		
		//logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		//logger.info("\n\n\n VALORES DE LA FORMA EN EL FORMULARIO JUSTIFICACION \n\n\n");
		FormatoJustArtNopos fjan= new FormatoJustArtNopos();
		
		forma.setObservacionesResumen(UtilidadTexto.observacionAHTML(forma.getObservacionesResumen().replace("'", "")+ "") +"");
		forma.setRespuestaClinicaParaclinica(UtilidadTexto.observacionAHTML(forma.getRespuestaClinicaParaclinica().replace("'", "")+ "") +"");
		
		
		/* alejo los deshabilite 
		logger.info("999999999999999999999999999999999999999999999999999999999999");
		logger.info("----------------------------------------- Resumen ");
		logger.info("" + forma.getObservacionesResumen());
		logger.info("-----------------------------------------");
		logger.info("" + UtilidadTexto.observacionAHTML(forma.getObservacionesResumen()));
		logger.info("-----------------------------------------");
		logger.info("" + UtilidadTexto.deshacerCodificacionHTML(UtilidadTexto.observacionAHTML(forma.getObservacionesResumen())));
		logger.info("999999999999999999999999999999999999999999999999999999999999");
		logger.info("----------------------------------------- Respuesta");
		logger.info("" + forma.getRespuestaClinicaParaclinica());
		logger.info("-----------------------------------------");
		logger.info("" + UtilidadTexto.observacionAHTML(forma.getRespuestaClinicaParaclinica()));
		logger.info("-----------------------------------------");
		logger.info("" + UtilidadTexto.deshacerCodificacionHTML(UtilidadTexto.observacionAHTML(forma.getRespuestaClinicaParaclinica())));
		
		
		/*
	   	logger.info("\n\n\n <<< sustituto no pos >>>"+forma.getSustitutosNoPos().get("dosificacion_"+forma.getIndexSelectSustitutos()));
	   	logger.info("\n\n\n <<< medicamento pos utilizado >>> "+forma.getMedicamentoPos());
	   logger.info("\n\n <<<<< MAPA DE FORMULARIO PARA VER CAMPOS QUE CAMBIAN O SE INSERTAN >>>>> \n\n"+forma.getFormularioMap());
	   logger.info("\n\n\n <<< datos medicamento pos utilizado >>>");
	   Utilidades.imprimirMapa(forma.getMedicamentosPos());
	   logger.info("\n\n\n <<< Mapa diagnosticos definitivos >>>");
	   Utilidades.imprimirMapa((HashMap)forma.getDiagnosticosDefinitivos());
	   logger.info("\n\n\n <<< diagnosticos complicacion >>>>"+forma.getDiagnosticoComplicacion_1());
	   logger.info("\n\n\n <<< Mapa diagnosticos presuntivos >>>");
	   Utilidades.imprimirMapa((HashMap)forma.getDiagnosticosPresuntivos());
	   logger.info("\n\n\n <<< Index sustitutos >>>"+forma.getIndexSelectSustitutos());
	   */

 		//DESCRIBIR EL FORMATO EN QUE LLEGAN LOS DATOS DEL FORMULARIO PARA UN MEJOR ENTENDIMIENTO DE COMO SE DEBE HACER LA ADICION
	   
		fjan.setIndicesformulariomap(Listado.obtenerKeysMapa(forma.getFormularioMap()));
		
		//Se eliminan las comillas ingresadas en los Strings
		Set<String> mapaMedicamentos = forma.getMedicamentosNoPos().keySet();
		for (String key : mapaMedicamentos) {
			String cadenaSinComilla = "";
			if(forma.getMedicamentosNoPos().get(key) instanceof String) {
				cadenaSinComilla = ((String)forma.getMedicamentosNoPos().get(key)).replace("'", "");
				forma.getMedicamentosNoPos().put(key, cadenaSinComilla);
			}
		}
		
		fjan.setIndicesnoposmap(Listado.obtenerKeysMapa(forma.getMedicamentosNoPos()));
		
		fjan.setIndicesposmap(Listado.obtenerKeysMapa(forma.getMedicamentosPos()));
		
		fjan.setIndicessustimap(Listado.obtenerKeysMapa(forma.getSustitutosNoPos()));
		fjan.setIndicesdiagmap(Listado.obtenerKeysMapa((HashMap)forma.getDiagnosticosDefinitivos()));
		fjan.setIndicesdiag1map(Listado.obtenerKeysMapa((HashMap)forma.getDiagnosticosPresuntivos()));
		
 		HashMap mapaConvertido=convierteMapa(forma.getFormularioMap(), fjan.getIndicesformulariomap(), Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString()));
 		mapaConvertido.put(Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString())+"_observacionesResumen", forma.getObservacionesResumen());
 		mapaConvertido.put(Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString())+"_respuestaClinicaParaclinica", forma.getRespuestaClinicaParaclinica());
 				
 				
		request.getSession().setAttribute(forma.getMedicamentoNoPos()+"MAPAJUSART", mapaConvertido);
		request.getSession().setAttribute(forma.getMedicamentoNoPos()+"MAPAJUSART_NORMAL", forma.getFormularioMap());
		request.getSession().setAttribute("MAPASECJUSART", forma.getMapaSecciones());
		
		request.getSession().setAttribute(forma.getMedicamentoNoPos()+"MAPA_MED_NO_POS_NORMAL", forma.getMedicamentosNoPos());
		request.getSession().setAttribute(forma.getMedicamentoNoPos()+"MAPA_MED_POS_NORMAL", forma.getMedicamentosPos());
		request.getSession().setAttribute(forma.getMedicamentoNoPos()+"MAPA_SUS_NO_POS_NORMAL", forma.getSustitutosNoPos());
		request.getSession().setAttribute(forma.getMedicamentoNoPos()+"MAPA_DIAG_DEF_NORMAL", forma.getDiagnosticosDefinitivos());
		request.getSession().setAttribute(forma.getMedicamentoNoPos()+"MAPA_DIAG_PRESUNTIVOS_NORMAL", forma.getDiagnosticosPresuntivos());
		request.getSession().setAttribute(forma.getMedicamentoNoPos()+"MAPA_INF_ART_PRINT", forma.getInformacionArtPrin());
		request.getSession().setAttribute(forma.getMedicamentoNoPos()+"MAPA_FORMA_FCONC", forma.getFormaFconcMap());
		
		request.getSession().setAttribute(forma.getMedicamentoNoPos()+"MAPA_MED_NO_POS", convierteMapa(forma.getMedicamentosNoPos(), fjan.getIndicesnoposmap(), Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString())));
		request.getSession().setAttribute(forma.getMedicamentoNoPos()+"MAPA_MED_POS", convierteMapa(forma.getMedicamentosPos(), fjan.getIndicesposmap(), Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString())));
		request.getSession().setAttribute(forma.getMedicamentoNoPos()+"MAPA_SUS_NO_POS", convierteMapa(forma.getSustitutosNoPos(), fjan.getIndicessustimap(), Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString())));
		request.getSession().setAttribute(forma.getMedicamentoNoPos()+"MAPA_DIAG_DEF", convierteMapa((HashMap)forma.getDiagnosticosDefinitivos(), fjan.getIndicesdiagmap(), Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString())));
		request.getSession().setAttribute(forma.getMedicamentoNoPos()+"MAPA_DIAG_PRESUNTIVOS", convierteMapa((HashMap)forma.getDiagnosticosPresuntivos(), fjan.getIndicesdiag1map(), Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString())));
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("mensajefin");
   }





/**
    * 
    * @param con
    * @param forma
    * @param response
    * @return
    */ 
    private ActionForward accioncargarDatosSustitutos(Connection con, FormatoJustArtNoposForm forma, HttpServletResponse response) {

	logger.info("posicion >>"+forma.getIndexSelectSustitutos());
	logger.info("mapa >>>"+forma.getSustitutosNoPos());
	
		String contenido = forma.getSustitutosNoPos().get("dosificacion_"+forma.getIndexSelectSustitutos()).toString() ;
		String contenido1 = forma.getSustitutosNoPos().get("ffconcentracion_"+forma.getIndexSelectSustitutos()).toString();
		String contenido2 = forma.getSustitutosNoPos().get("dosisdiaria_"+forma.getIndexSelectSustitutos()).toString();
		String contenido3 = forma.getSustitutosNoPos().get("tiempotratamiento_"+forma.getIndexSelectSustitutos()).toString();
		String contenido4 = forma.getSustitutosNoPos().get("grupoterapeutico_"+forma.getIndexSelectSustitutos()).toString();
		String contenido5 = forma.getSustitutosNoPos().get("numdossisequiv_"+forma.getIndexSelectSustitutos()).toString();
		
		
		
	logger.info("ffconcentracion de la forma >>>>"+forma.getSustitutosNoPos().get("ffconcentracion_"+forma.getIndexSelectSustitutos()).toString());
	logger.info("Indice del Mapa: " + forma.getIndexMap());
	logger.info("Indice Select Sustitutos : " + forma.getIndexSelectSustitutos());
	logger.info("No Justificacion: " + forma.getNojustificacion() );
		
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoInnerHtml</sufijo>" +
				"<id-div>divGrupoUvr</id-div>" + //id del div a modificar
				"<contenido>"+contenido+"</contenido>" + //tabla
				"<id-div>divGrupoUvr1</id-div>" + //id del div a modificar
				"<contenido>"+contenido1+"</contenido>" + //tabla
				"<id-div>divGrupoUvr2</id-div>" + //id del div a modificar
				"<contenido>"+contenido2+"</contenido>" + //tabla
				"<id-div>divGrupoUvr3</id-div>" + //id del div a modificar
				"<contenido>"+contenido3+"</contenido>" + //tabla
				"<id-div>divGrupoUvr4</id-div>" + //id del div a modificar
				"<contenido>"+contenido4+"</contenido>" + //tabla
				"<id-div>divGrupoUvr5</id-div>" + //id del div a modificar
				"<contenido>"+contenido5+"</contenido>" + //tabla
			"</infoid>"+
			"</respuesta>";
	
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFormatoJustArtNoPos: "+e);
		}
		return null;
	
	}






/**
 * 
 * @param forma
 * @param con
 * @param mapping
 * @param usuario
 * @param paciente
 * @return
 */
    private ActionForward accionCargarInfoMedicamentosPos(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletResponse response) {

    	logger.info("><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		logger.info("cargando info del POS.");

		FormatoJustArtNopos fjan=new FormatoJustArtNopos();

		String contenido = "";
		String contenido1 = "";
		String contenido2 = "";
		String contenido3 = "";
		String contenido4 = "";
		String contenido5 = "";
		
		String datos[]=forma.getMedicamentoPos().split(ConstantesBD.separadorSplit);
		
		if (!forma.getMedicamentoPos().toString().equals(ConstantesBD.codigoNuncaValido+"")&&datos.length>1)
		{
			forma.setMedicamentosPos(fjan.cargarInfoMedicamentosPos(con, forma.getMedicamentosPos(), forma.getMedicamentoPos()));
			logger.info("Indice del Mapa: " + forma.getIndexMap());
			logger.info("Indice Select Sustitutos : " + forma.getIndexSelectSustitutos());
			logger.info("No Justificacion: " + forma.getNojustificacion() );
			logger.info("Unidosis : " + forma.getMedicamentosPos().get("unidosis") );

			
			if(forma.getMedicamentosPos().get("unidosis").toString().equals(ConstantesBD.codigoNuncaValido)) {
				contenido = forma.getMedicamentosPos().get("dosificacion").toString() + 
	 			forma.getMedicamentosPos().get("frecuencia").toString() +
	 			forma.getMedicamentosPos().get("tipofrecuencia").toString();
			}

			else {
				contenido = forma.getMedicamentosPos().get("dosificacion").toString() + " " +
				Utilidades.obtenerUnidadMedidadUnidosisArticulo(con, forma.getMedicamentosPos().get("unidosis").toString()) + " cada " +				
	 			forma.getMedicamentosPos().get("frecuencia").toString() +
	 			forma.getMedicamentosPos().get("tipofrecuencia").toString();
			}
 			//forma.getMedicamentosPos().get("unidosis").toString() + " cada " + 

			
		 	contenido1 = forma.getMedicamentosPos().get("ffconcentracion").toString();
		 	contenido2 = forma.getMedicamentosPos().get("dosisdiaria").toString();
		 	contenido3 = forma.getMedicamentosPos().get("cantidad").toString();
			contenido4 = forma.getMedicamentosPos().get("tiempotratamiento").toString();
			contenido5 = forma.getMedicamentosPos().get("respuestaclinica").toString();

			logger.info("---------------------------------------------------" );
			logger.info("0 " + forma.getMedicamentosPos().get("respuestaclinica").toString());
			logger.info("---------------------------------------------------" );
			logger.info("1 " + UtilidadTexto.deshacerCodificacionHTML(forma.getMedicamentosPos().get("respuestaclinica").toString()));
			logger.info("---------------------------------------------------" );
			logger.info("2 " + UtilidadTexto.observacionAHTML(forma.getMedicamentosPos().get("respuestaclinica").toString()));
			
			forma.setRespuestaClinicaParaclinica(forma.getMedicamentosPos().get("respuestaclinica").toString());
			
		}else{
			forma.getMedicamentosPos().put("dosificacion", "");
			forma.getMedicamentosPos().put("frecuencia", "");
			forma.getMedicamentosPos().put("ffarma", "");
			forma.getMedicamentosPos().put("concentracion", "");
			forma.getMedicamentosPos().put("dosisdiaria", "");
			forma.getMedicamentosPos().put("cantidad", "");
			forma.getMedicamentosPos().put("tiempotratamiento", "");
			forma.getMedicamentosPos().put("dosis", "");
			forma.getMedicamentosPos().put("tipofrecuencia", "");
			forma.getMedicamentosPos().put("ffconcentracion", "");
		}
		fjan.setIndicesposmap(Listado.obtenerKeysMapa(forma.getMedicamentosPos()));
		
		//UtilidadBD.closeConnection(con);		
	
		logger.info("mapa >>>"+forma.getMedicamentosPos());
		
		//logger.info("caontenido 1 ffconcentracion de la variable"+contenido1 );
		//logger.info("contenido >>>"+contenido);
		
		String resultado = "<respuesta>" +
		"<infoid>" +
			"<sufijo>ajaxBusquedaTipoInnerHtml</sufijo>" +
			"<id-div>divPos1</id-div>" + //id del div a modificar
			"<contenido>"+contenido+"</contenido>" + //tabla
			"<id-div>divPos2</id-div>" + //id del div a modificar
			"<contenido>"+contenido1+"</contenido>" + //tabla
			"<id-div>divPos3</id-div>" + //id del div a modificar
			"<contenido>"+contenido2+"</contenido>" + //tabla
			"<id-div>divPos4</id-div>" + //id del div a modificar
			"<contenido>"+contenido3+"</contenido>" + //tabla
			"<id-div>divPos5</id-div>" + //id del div a modificar
			"<contenido>"+contenido4+"</contenido>" + //tabla
			/*"<id-div>divPos6</id-div>" + //id del div a modificar
			"<contenido>"+contenido5+"</contenido>" + //tabla*/
		"</infoid>"+
		"</respuesta>";

	//*************	SE GENERA RESPUESTA PARA AJAX EN XML	**********************************************
		try {
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
		}
		catch(IOException e) {
			logger.error("Error al enviar respuesta AJAX en accionFormatoJustArtNoPos: "+e);
		}
		return mapping.findForward("principal");
    		
	}



	/**
     * 
     * @param connection
     * @param forma
     * @param mapping
     * @param estado
     * @return
     */
	public ActionForward accionSubirArchivo(Connection connection, FormatoJustArtNoposForm forma, ActionMapping mapping,String estado)
	{				
		
	     //logger.info("\n\n************************************nentro a accionSubirArchibo********************************");
		//logger.info("****************  VALOR FORMA"+forma.getConsentimientoInfMap());
		forma.setEstado(estado);
		UtilidadBD.closeConnection(connection);
		
		return mapping.findForward("archivo");
	}

    
	
	private ActionForward accionEmpezarCon(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) {

		InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
		FormatoJustArtNopos fjan=new FormatoJustArtNopos();
		fjan.setEmisor(forma.getEmisor());
		forma.setCapa("N");
		forma.setRespuestaClinicaParaclinica("");
		forma.setObservacionesResumen("");
		//forma.setFormaFconcMap(new HashMap());
		//forma.setInformacionArtPrin(new HashMap());
		//forma.reset();
		
		//Preguntar de donde es llamado el formato de justificacion de articulos no pos, con el fin  
		//de definir de donde se cargan los datos del articulo no pos.	
		if(forma.getEmisor().equals("solicitud"))
			{
			//Cargamos el objeto con los parametros que llegan del llamado "parametros del articulo no pos"
			fjan.setMedicamentoNoPos(forma.getMedicamentoNoPos());
			fjan.setDosis(forma.getDosis());
			fjan.setUnidosis(forma.getUnidosis());
			fjan.setFrecuencia(forma.getFrecuencia());
			fjan.setTipoFrecuencia(forma.getTipoFrecuencia());
			fjan.setTotalUnidades(forma.getTotalUnidades());
			fjan.setTiempoTratamiento(forma.getTiempoTratamiento());
			}
		
		if(forma.getSustitutosNoPos()!=null&&forma.getSustitutosNoPos().get("numRegistros")!=null&&Utilidades.convertirAEntero(forma.getSustitutosNoPos().get("numRegistros").toString())>0){
			forma.setExisteSustituto(true);
		}else{
			forma.setExisteSustituto(false);
		}
		
		try{
			ComportamientoCampo.cargarValoresDefectoAccionesCampo(con, forma, mapping, request);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		UtilidadBD.closeConnection(con);
	return mapping.findForward("cargar");
	
	}
	
    /**
     * Método encargado de realizar la acción de empezar
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @param request
     * @param response
     * @return ActionForward
     * @throws Exception 
     */
	private ActionForward accionEmpezar(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		forma.setConsecutivoJustActual(Utilidades.convertirAEntero(UtilidadBD.obtenerValorActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoJustificacionNOPOSArticulos, usuario.getCodigoInstitucionInt())));
		logger.info("Número actual de la justificación = " + forma.getConsecutivoJustActual());
		logger.info("codigo solicitud en empezar = " + forma.getCodigoSolicitud());
		logger.info("codigo Orden = " + forma.getCodigoOrden());
		logger.info("Ingreso: "+ forma.getIngreso());
			
		logger.info("===> num solicitud salvado = " + forma.getNumSolicitudSalvado());
		logger.info("===> codigo articulo salvado = " + forma.getCodigoArticuloSalvado());
		logger.info("===> emisor = " + forma.getEmisor());
		String tipoFormato="";
		//valores utilizados por felipe para la just no pos desde histo clinica
		/*if(UtilidadCadena.noEsVacio(forma.getCodigoArticuloSalvado()+"") && (UtilidadCadena.noEsVacio(forma.getNumSolicitudSalvado()+"")) ) {
			forma.setCodigoSolicitud(forma.getNumSolicitudSalvado());
			forma.setMedicamentoNoPos(forma.getCodigoArticuloSalvado());
		}*/
		
		InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
		FormatoJustArtNopos fjan = new FormatoJustArtNopos();
		fjan.setEmisor(forma.getEmisor());
		forma.setCapa(ConstantesBD.acronimoNo);
		forma.setRespuestaClinicaParaclinica("");
		forma.setObservacionesResumen("");
		forma.setFormaFconcMap(new HashMap());
		forma.setInformacionArtPrin(new HashMap());
		forma.setIndexSelectSustitutos(0);
		forma.setPermitirModificarTiempoTratamientoJustificacionNopos(ValoresPorDefecto.getPermitirModificarTiempoTratamientoJustificacionNopos(usuario.getCodigoInstitucionInt()));
		
		logger.info("xxxxXXX Frecuencia: " + fjan.getFrecuencia());
		
		/*
		 * Solución Tarea 45708
		 * Vamos a consultar la información de se informa al paciente
		 */
		HashMap informacionSegundoRadioButton = new HashMap();
		if(forma.getNumSolicitudSalvado().equals("") && forma.getCodigoOrden()==ConstantesBD.codigoNuncaValido){
			logger.info("===> el numero de solicitud está en blanco y no existe Orden Ambulatoria!!!");

		}else{
			
			if(forma.isProvieneOrdenAmbulatoria()){
				informacionSegundoRadioButton = ValoresPorDefecto.utilizaMedicamentosTratamientoPaciente(con, forma.getCodigoOrden()+"",forma.getCodigoArticuloSalvado(),true);
			}else{
				informacionSegundoRadioButton = ValoresPorDefecto.utilizaMedicamentosTratamientoPaciente(con, forma.getNumSolicitudSalvado(),forma.getCodigoArticuloSalvado(),false);
		}
		
			/*
			logger.info("===> *************************************************************************************");
			logger.info("===> El mapa de la informacion del segundo radio button es: "+informacionSegundoRadioButton);
			logger.info("===> *************************************************************************************");
			*/
			int numRegistros = Integer.parseInt(informacionSegundoRadioButton.get("numRegistros")+""), f=0;
			String etiqueta="", valor="";
			
			if(numRegistros == 0)
			{
				logger.info("===> No existe información para esta orden ambulatoria o solicitud :(");
			}
			else if(numRegistros > 0)
			{
				/*
				 * Cómo la llave que necesitamos mostrar en la jsp es: 
				 * Se han utilizado medicamentos POS en el tratamiento del paciente?
				 * Hacemos el filtro por ese identificador
				 */
				for(f=0;f<numRegistros;f++ )
				{
					etiqueta=informacionSegundoRadioButton.get("etiqueta_campo_"+f)+"";
					if(etiqueta.equals("Se han utilizado medicamentos POS en el tratamiento del paciente?"))
					{
						valor = informacionSegundoRadioButton.get("valor_"+f)+"";
						//logger.info("===> Pude entrar Felipe !!! :D");
						logger.info("===> "+etiqueta+" = "+valor);
						//logger.info("===> ");
						forma.setSeHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente(valor);
					}
					
				}
				
			}
		}
		
		//forma.reset();
		
			//Preguntar de donde es llamado el formato de justificacion de articulos no pos, con el fin  
			//de definir de donde se cargan los datos del articulo no pos.	
		if(forma.getEmisor().equals("solicitud"))
		{
			logger.info("ES INSUMO - "+forma.isEsInsumo());
			
			logger.info("22222222222222222222222222222222222 Emisor Solicitud");
			logger.info("1111111111111111111111 - Forma");
			logger.info("Dosis: " + forma.getDosis());
			logger.info("Unidosis: " + forma.getUnidosis());
			logger.info("Frecuencia: " + forma.getFrecuencia());
			logger.info("Tipo Frecuencia: " + forma.getTipoFrecuencia());
			
			logger.info("1111111111111111111111 - Mundo ");
			logger.info("Dosis: " + fjan.getDosis());
			logger.info("Unidosis: " + fjan.getUnidosis());
			logger.info("Unidosisl: " + fjan.getUnidosisl());
			logger.info("Frecuencia: " + fjan.getFrecuencia());
			logger.info("Tipo Frecuencia: " + fjan.getTipoFrecuencia());

			//String[] valores = {"", ""};
			//valores[1] = Integer.toString(ConstantesBD.codigoNuncaValido);
			
			String[] valores = {};
			if(!forma.isEsInsumo())
				valores = forma.getDosis().split("@@");
			int prueba = valores.length;
		
			//Cargamos el objeto con los parametros que llegan del llamado "parametros del articulo no pos"
			fjan.setMedicamentoNoPos(forma.getMedicamentoNoPos());


			//toco hacer la sigte validacion ya ke no se puede cambiar el valor de la unidosis por indicaciones
			//de los jefes
			
			//sino viene desde mezclas
			if(prueba == 1) {
				logger.info("Viene de otra parte diferente a mezclas!");
				fjan.setDosis(forma.getDosis());
				fjan.setUnidosisl(Utilidades.obtenerUnidadMedidadUnidosisArticulo(con, forma.getUnidosis()+""));
				fjan.setUnidosis(forma.getUnidosis());
			}
			else {
				
				if(!forma.isEsInsumo()){
					
					logger.info("Viene desde mezclas!");
					logger.info("Valores [0]: " + valores[0]);
					logger.info("Valores [1]: " + valores[1]);
					
					fjan.setDosis(valores[0]);
					fjan.setUnidosisl(valores[1]);
					//fjan.setUnidosis(valores[1]);
				}	
			}
			
			fjan.setFrecuencia(forma.getFrecuencia());
			fjan.setTipoFrecuencia(forma.getTipoFrecuencia());
			fjan.setTotalUnidades(forma.getTotalUnidades());
			fjan.setTiempoTratamiento(forma.getTiempoTratamiento());
			//metodos para cargar formulario y datos del formulario
			
			forma.setMedicamentosNoPos(fjan.cargarMedicamentosNoPos(con, fjan, paciente));
			
			Articulo articulo=new Articulo();
			articulo.cargarArticulo(con, Integer.parseInt(forma.getMedicamentoNoPos()));
			HashMap codigoCUM=articulo.getCumMap();
			forma.getMedicamentosNoPos().put("numeroExpediente",codigoCUM.get("numero_expediente"));
			forma.getMedicamentosNoPos().put("consecutivoPresentacionComercial",codigoCUM.get("cons_present_comercial"));
			
			fjan.setMedicamentosNoPos(forma.getMedicamentosNoPos());
			logger.info("obtenerFormularioParametrizado 1 ");
			forma.setFormularioMap(fjan.obtenerFormularioParametrizado(con,fjan,paciente,usuario));
			
			forma.setMapaSecciones((HashMap)forma.getFormularioMap().get("mapasecciones"));
			
    		//Evaluamos la cobertura del articulo
			/*Modificación realizada a través de la Tarea 43414. Donde se envia el convenio y la subcuenta
			desde el infoResponsableCobertura se modifico para enviarle el del la cadena de String subCuentaConvenio
			donde: --->[0] es la subcuenta y --->[1] es el convenio
			
			infoResponsableCobertura = Cobertura.validacionCoberturaArticulo(con, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), Utilidades.convertirAEntero(forma.getMedicamentoNoPos()), Utilidades.convertirAEntero(usuario.getCodigoInstitucion()), false);
			forma.setFormularioMap("cobertura", UtilidadesFacturacion.consultarNombreConvenio(con,infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo()));
			forma.setFormularioMap("subcuenta", infoResponsableCobertura.getDtoSubCuenta().getSubCuenta());*/
			
			String[] subCuentaConvenio = new String [2];
			subCuentaConvenio = fjan.requiereJustificacionArticulo(con, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoCuenta()+"");
			forma.setFormularioMap("cobertura", UtilidadesFacturacion.consultarNombreConvenio(con, Utilidades.convertirAEntero(subCuentaConvenio[1]+"")));
			forma.setFormularioMap("subcuenta", subCuentaConvenio[0]+"");
			
			logger.info("====>SUB CUENTA: "+subCuentaConvenio[0]);
    		logger.info("====>CONVENIO: "+subCuentaConvenio[1]);
			
    		//Evaluamos si el convenio que cubre el servicio requiere de justificación de articulo
//    		if (UtilidadesFacturacion.requiereJustificacioArt(con, Utilidades.convertirAEntero(subCuentaConvenio[1]+""), Utilidades.convertirAEntero(fjan.getMedicamentoNoPos()+"")) )
//    		{
    			//obtenemos el nombre del tipo de regimen segun el codigo del convenio
    			forma.setFormularioMap("tiporegimen",UtilidadesFacturacion.consultarNombreTipoRegimen(con, Utilidades.convertirAEntero(subCuentaConvenio[1]+""))+"");
    			
    			if (!UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, true)){
					logger.info("NO JUSTIFICACION VALIDACION PROFESIONAL DE LA SALUD NO REQUIERE");
					forma.setNojustificacion("1");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("nojustificacion");
				}
    			
    			/*if(!forma.isSeVaAAsociarJustificacion()){
	    			if(UtilidadInventarios.validarTiempoTratamiento(con, Utilidades.convertirAEntero(fjan.getMedicamentoNoPos()+""), fjan.getUnidosis()+"", fjan.getTipoFrecuencia(), paciente))
	        		{
	    				if (!UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, true)){
	    					logger.info("NO JUSTIFICACION VALIDACION PROFESIONAL DE LA SALUD NO REQUIERE");
	    					forma.setNojustificacion("1");
	    					UtilidadBD.closeConnection(con);
	    					return mapping.findForward("nojustificacion");
	    				}
	    			}
	    			else{
	    				logger.info("NO JUSTIFICACION VALIDACION TIEMPO TRATAMIENTO NO REQUIERE");
	    				forma.setNojustificacion("2");
	    				UtilidadBD.closeConnection(con);
	    				return mapping.findForward("nojustificacion");
	    			}
    			}*/
//    		}
//    		else
//    		{
//    			logger.info("NO JUSTIFICACION VALIDACION COBERTURA NO REQUIERE");
//    			forma.setNojustificacion("3");
//    			UtilidadBD.closeConnection(con);
//    			return mapping.findForward("nojustificacion");
//    		}
    		
			/////////******************************POR ACA DESDE SOLICITUDES
    		forma.setMedicamentosPos(fjan.cargarMedicamentosPos(con,fjan,paciente));
			
    		HashMap medicamentosPosMap=forma.getMedicamentosPos();
    		
    		if(forma.getMedicamentoPos()==null&&medicamentosPosMap.get("numRegistros")!=null){
    			int numReg=Integer.parseInt(medicamentosPosMap.get("numRegistros").toString());
    			if(numReg==1){
    				String medicamentoPos=medicamentosPosMap.get("codigo_"+0)+ConstantesBD.separadorSplit+medicamentosPosMap.get("numerosolicitud_"+0)+ConstantesBD.separadorSplit+ConstantesBD.acronimoNo+ConstantesBD.separadorSplit+medicamentosPosMap.get("esantecedente_"+0)+ConstantesBD.separadorSplit+medicamentosPosMap.get("nombre_"+0);
    				forma.setMedicamentoPos(medicamentoPos);
    				accionCargarInfoMedicamentosPos(forma, con, mapping, usuario, paciente, response);
    			}
    		}else{
    			accionCargarInfoMedicamentosPos(forma, con, mapping, usuario, paciente, response);
    		}
    		
			forma.setSustitutosNoPos(fjan.cargarSustitutosNoPos(con,fjan,paciente));
			
			forma.setObservacionesResumen(fjan.consultarObservacionesResumenNoPOS(con, fjan.getCodigoSolicitud()+"",fjan.getMedicamentoNoPos(),false));
			
			preCargarDiagnosticos(con, paciente.getCodigoCuenta(), paciente.getCodigoIngreso(), forma, request);
			
			fjan.setIndicesformulariomap(Listado.obtenerKeysMapa(forma.getFormularioMap()));
			
			fjan.setIndicesnoposmap(Listado.obtenerKeysMapa(forma.getMedicamentosNoPos()));
			fjan.setIndicesposmap(Listado.obtenerKeysMapa(forma.getMedicamentosPos()));
			fjan.setIndicessustimap(Listado.obtenerKeysMapa(forma.getSustitutosNoPos()));
			fjan.setIndicesdiagmap(Listado.obtenerKeysMapa((HashMap)forma.getDiagnosticosDefinitivos()));
			fjan.setIndicesdiag1map(Listado.obtenerKeysMapa((HashMap)forma.getDiagnosticosPresuntivos()));
//			forma.setDiagnosticoComplicacion_1("");
//			forma.setDiagnosticosDefinitivos(new HashMap());
//			forma.setNumDiagnosticosDefinitivos(Utilidades.convertirAEntero(aux.get("numDiagnosticosDefinitivos").toString()));
		}
		
		
		else if(forma.getEmisor().equals("modificar")) {

			forma.setCapa(ConstantesBD.acronimoSi);
//			se carga el numero de solicitud enviado desde el emisor
			fjan.setCodigoSolicitud(forma.getCodigoSolicitud());
//			se carga el codigo del articulo enviado desde el emisor			
			fjan.setMedicamentoNoPos(forma.getMedicamentoNoPos());
			
			Integer tipoMediacmento = UtilidadesFacturacion.tipoMedicamento(Integer.valueOf(forma.getMedicamentoNoPos()));
//			se carga el nuemro de subcuenta enviado desde el emisor para hacer la consulta de la cantidad y el estado 
			fjan.setSubcuenta(forma.getSubCuenta());
			
			forma.setMedicamentosNoPos(fjan.cargarMedicamentosNoPos(con, fjan, paciente));
			
			tipoFormato=String.valueOf(forma.getMedicamentosNoPos().get("grupoterapeutico_0"));
			
			if(forma.getMedicamentosNoPos()!=null&&forma.getMedicamentosNoPos().containsKey("codigojus_0")&&
					forma.getMedicamentosNoPos().get("codigojus_0")!=null){
				fjan.setCodigoJustificacion(forma.getMedicamentosNoPos().get("codigojus_0").toString());
			}
			
			//cargar el convenio de alguna forma
			
//			metodos para cargar formulario y datos del formulario
			logger.info("obtenerFormularioParametrizado 2 ");
			forma.setFormularioMap(fjan.obtenerFormularioParametrizado(con,fjan,paciente,usuario));
			
			
				forma.getFormularioMap().put("tipoMedicamento", tipoMediacmento.toString());
			
			
			if(forma.getFormularioMap().get("esOrdenAmbulatoria")!= null){
				forma.setProvieneOrdenAmbulatoria(UtilidadTexto.getBoolean(forma.getFormularioMap().get("esOrdenAmbulatoria")));
			}else{
				forma.setProvieneOrdenAmbulatoria(false);
			}
			
			
			fjan.setFormularioMap(forma.getFormularioMap());
			forma.setFormularioMap("subcuenta", fjan.getSubcuenta());
			forma.setFormularioMap("cobertura", UtilidadesFacturacion.consultarNombreConvenio(con,Utilidades.convertirAEntero(forma.getConvenio())));
			forma.setFormularioMap("tiporegimen",UtilidadesFacturacion.consultarNombreTipoRegimen(con, Utilidades.convertirAEntero(forma.getConvenio().toString()))+"");
			
			//se validan los roles de nuevo, para restringir si es necesario la modificacion del formulario
			boolean estadomod=Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), 758);
			boolean formulariomod=Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), 757);
			boolean formulariocon=Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), 759);
			
			logger.info("\n\n\n\n validacion de roles >>>>>>>> modificar estados >>>>"+estadomod);
			logger.info("\n\n\n\n validacion de roles >>>>>>>> modificar form >>>>"+formulariomod);
			logger.info("\n\n\n\n validacion de roles >>>>>>>> consultar >>>>"+formulariocon);
			
			forma.setFormularioMap("modificaEstado", estadomod+"");
			forma.setFormularioMap("modificaForm", formulariomod+"");
			forma.setFormularioMap("consultaForm", formulariocon+"");
			
			//metodo para cargar los datos medicamentos no pos
			//forma.setMedicamentosNoPos(fjan.cargarMedicamentosNoPos(con, fjan, paciente));
			
			Articulo articulo=new Articulo();
			articulo.cargarArticulo(con, Integer.parseInt(forma.getMedicamentoNoPos()));
			HashMap codigoCUM=articulo.getCumMap();
			forma.getMedicamentosNoPos().put("numeroExpediente",codigoCUM.get("numero_expediente"));
			forma.getMedicamentosNoPos().put("consecutivoPresentacionComercial",codigoCUM.get("cons_present_comercial"));
			
			fjan.setMedicamentosNoPos(forma.getMedicamentosNoPos());
			//metodo para cargar los datos medicamentos pos	
			forma.setMedicamentosPos(fjan.cargarMedicamentosPos(con,fjan,paciente));

			if(forma.getMedicamentosPos().get("respuestaclinica")!=null){
				forma.setRespuestaClinicaParaclinica((String) forma.getMedicamentosPos().get("respuestaclinica"));
			}else{
				forma.setRespuestaClinicaParaclinica("");
			}
			
			if(forma.getMedicamentosPos().containsKey("codigoparam")&&forma.getMedicamentosPos().get("codigoparam")!=null)
			{
                if (!forma.getMedicamentosPos().get("codigoparam").toString().trim().equals(""))
                {
				HashMap medicamentosPosMap=forma.getMedicamentosPos();
				//medicamentosPosMap.get("codigoparam")+ConstantesBD.separadorSplit+medicamentosPosMap.get("numerosolicitudparam")+ConstantesBD.separadorSplit+ConstantesBD.acronimoSi
				String medicamentoPos=medicamentosPosMap.get("codigoparam")+ConstantesBD.separadorSplit+medicamentosPosMap.get("numerosolicitudparam")+ConstantesBD.separadorSplit+ConstantesBD.acronimoSi;
				forma.setMedicamentoPos(medicamentoPos);
			     }
                }
			
			//metodo para cargar los diagnosticos
			HashMap aux=fjan.consultarDiagnosticos(con, Utilidades.convertirAEntero(fjan.getMedicamentosNoPos("codigojustificacion_0").toString()));
			forma.setDiagnosticoComplicacion_1(aux.get("diagnosticoComplicacion").toString());
			forma.setDiagnosticosDefinitivos((HashMap)aux.get("diagnosticosDefinitivos"));
			forma.setNumDiagnosticosDefinitivos(Utilidades.convertirAEntero(aux.get("numDiagnosticosDefinitivos").toString()));
			//metodo para cargar los datos fijos de la tabla justificacion_art_fijo
			forma.setSustitutosNoPos(fjan.cargarSustitutosNoPos(con,fjan,paciente));
			forma.setObservacionesResumen(fjan.consultarObservacionesResumenNoPOS(con, fjan.getCodigoSolicitud()+"",fjan.getMedicamentoNoPos(),false));
			
			forma.setMapaSecciones((HashMap)forma.getFormularioMap().get("mapasecciones"));
			
			
			}
		else if (forma.getEmisor().equals("consultar"))
		{
			forma.setCapa(ConstantesBD.acronimoSi);
//			se carga el numero de solicitud enviado desde el emisor
			logger.info("codigo de la solicitud desde la forma: "+forma.getCodigoSolicitud());
			fjan.setCodigoSolicitud(forma.getCodigoSolicitud());
			//se carga el numero de la orden ambulatoria 			
			logger.info("codigo de la orden ambulatoria desde la forma: "+forma.getCodigoOrden());
			fjan.setCodigoOrden(String.valueOf(forma.getCodigoOrden()));
			//se carga el origen
			fjan.setProvieneOrdenAmbulatoria(forma.isProvieneOrdenAmbulatoria());
//			se carga el codigo del articulo enviado desde el emisor			
			fjan.setMedicamentoNoPos(forma.getMedicamentoNoPos());
//			se carga el nuemro de subcuenta enviado desde el emisor para hacer la consulta de la cantidad y el estado 
			fjan.setSubcuenta(forma.getSubCuenta());
			fjan.setConvenio(forma.getConvenio());
			
			
			
			logger.info("[subcuenta Parametro]"+forma.getSubCuenta());
//			metodos para cargar formulario y datos del formulario
			logger.info("obtenerFormularioParametrizado 3 ");
			
			forma.setMedicamentosNoPos(fjan.cargarMedicamentosNoPos(con, fjan, paciente));
			
			if(forma.getMedicamentosNoPos()!=null&&forma.getMedicamentosNoPos().containsKey("codigojus_0")&&
					forma.getMedicamentosNoPos().get("codigojus_0")!=null){
				fjan.setCodigoJustificacion(forma.getMedicamentosNoPos().get("codigojus_0").toString());
			}
			
			forma.setFormularioMap(fjan.obtenerFormularioParametrizado(con,fjan,paciente,usuario));
			
			if(forma.getFormularioMap().get("esOrdenAmbulatoria")!= null){
				forma.setProvieneOrdenAmbulatoria(UtilidadTexto.getBoolean(forma.getFormularioMap().get("esOrdenAmbulatoria")));
			}else{
				forma.setProvieneOrdenAmbulatoria(false);
			}
			
			fjan.setFormularioMap(forma.getFormularioMap());
			forma.setFormularioMap("subcuenta", fjan.getSubcuenta());
			forma.setFormularioMap("cobertura", UtilidadesFacturacion.consultarNombreConvenio(con,Utilidades.convertirAEntero(forma.getConvenio())));
			forma.setFormularioMap("tiporegimen",UtilidadesFacturacion.consultarNombreTipoRegimen(con, Utilidades.convertirAEntero(forma.getConvenio().toString()))+"");
			
			logger.info("\n\n\n [convenio ]"+forma.getFormularioMap().get("cobertura"));
			
			//metodo para cargar los datos medicamentos no pos
			//forma.setMedicamentosNoPos(fjan.cargarMedicamentosNoPos(con, fjan, paciente));
			
			Articulo articulo=new Articulo();
			articulo.cargarArticulo(con, Integer.parseInt(forma.getMedicamentoNoPos()));
			HashMap codigoCUM=articulo.getCumMap();
			forma.getMedicamentosNoPos().put("numeroExpediente",codigoCUM.get("numero_expediente"));
			forma.getMedicamentosNoPos().put("consecutivoPresentacionComercial",codigoCUM.get("cons_present_comercial"));
			
			fjan.setMedicamentosNoPos(forma.getMedicamentosNoPos());
			//metodo para cargar los datos medicamentos pos	
			forma.setMedicamentosPos(fjan.cargarMedicamentosPos(con,fjan,paciente));
			
			if(forma.getMedicamentosPos().get("respuestaclinica")!=null){
				forma.setRespuestaClinicaParaclinica((String) forma.getMedicamentosPos().get("respuestaclinica"));
			}else{
				forma.setRespuestaClinicaParaclinica("");
			}
			
			if(forma.getMedicamentosPos().containsKey("codigoparam")&&forma.getMedicamentosPos().get("codigoparam")!=null
					&&!forma.getMedicamentosPos().get("codigoparam").toString().trim().equals("")){
				HashMap medicamentosPosMap=forma.getMedicamentosPos();
				//medicamentosPosMap.get("codigoparam")+ConstantesBD.separadorSplit+medicamentosPosMap.get("numerosolicitudparam")+ConstantesBD.separadorSplit+ConstantesBD.acronimoSi
				String medicamentoPos=medicamentosPosMap.get("codigoparam")+ConstantesBD.separadorSplit+medicamentosPosMap.get("numerosolicitudparam")+ConstantesBD.separadorSplit+ConstantesBD.acronimoSi;
				forma.setMedicamentoPos(medicamentoPos);
			}
			
			//metodo para cargar los diagnosticos
			HashMap aux=fjan.consultarDiagnosticos(con, Utilidades.convertirAEntero(fjan.getMedicamentosNoPos("codigojustificacion_0").toString()));
			forma.setDiagnosticoComplicacion_1(aux.get("diagnosticoComplicacion").toString());
			forma.setDiagnosticosDefinitivos((HashMap)aux.get("diagnosticosDefinitivos"));
			forma.setNumDiagnosticosDefinitivos(Utilidades.convertirAEntero(aux.get("numDiagnosticosDefinitivos").toString()));
			//metodo para cargar los datos fijos de la tabla justificacion_art_fijo
			forma.setSustitutosNoPos(fjan.cargarSustitutosNoPos(con,fjan,paciente));
			
			forma.setObservacionesResumen(fjan.consultarObservacionesResumenNoPOS(con, fjan.isProvieneOrdenAmbulatoria() ? fjan.getCodigoOrden()+"" : fjan.getCodigoSolicitud()+"",fjan.getMedicamentoNoPos(),fjan.isProvieneOrdenAmbulatoria()));
			
			forma.setMapaSecciones((HashMap)forma.getFormularioMap().get("mapasecciones"));
			
		}else
			if(forma.getEmisor().equals("pendiente"))
			{
				fjan.setMedicamentoNoPos(forma.getMedicamentoNoPos());
		  		//Evaluamos la cobertura del articulo
	    		infoResponsableCobertura = Cobertura.validacionCoberturaArticulo(con, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), Utilidades.convertirAEntero(forma.getMedicamentoNoPos()), Utilidades.convertirAEntero(usuario.getCodigoInstitucion()), false);
	    		//Evaluamos si el convenio que cubre el servicio requiere de justificación de articulo
	    		if (UtilidadesFacturacion.requiereJustificacioArt(con, infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo(),Utilidades.convertirAEntero(fjan.getMedicamentoNoPos())))
	    		{
	    			//obtenemos el nombre del tipo de regimen segun el codigo del convenio
	    			forma.setFormularioMap("tiporegimen",UtilidadesFacturacion.consultarNombreTipoRegimen(con, infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo())+"");
	    			
	    				if (UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, true)){
	    					logger.info("NO JUSTIFICACION VALIDACION PROFECIONAL DE LA SALUD NO REQUIERE");
	    					forma.setNojustificacion("1");
	    					UtilidadBD.closeConnection(con);
	    					return mapping.findForward("nojustificacion");
	    				}
	    		}
	    		else
	    		{
	    			logger.info("NO JUSTIFICACION VALIDACION COBERTURA NO REQUIERE");
	    			forma.setNojustificacion("3");
	    			UtilidadBD.closeConnection(con);
	    			return mapping.findForward("nojustificacion");
	    		}
	    	}
			else
				if(forma.getEmisor().equals("ingresar"))
				{

					forma.resetformulario();
			
					/*if (UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, true)){
						logger.info("NO JUSTIFICACION VALIDACION PROFECIONAL DE LA SALUD NO REQUIERE");
						forma.setNojustificacion("1");
						return mapping.findForward("nojustificacion");
					}*/
					fjan.setMedicamentoNoPos(forma.getMedicamentoNoPos());
					fjan.setCentroCosto(forma.getCentroCosto());
					fjan.setConvenio(forma.getConvenio());
					fjan.setCodigoSolicitud(forma.getCodigoSolicitud());
					fjan.setIngreso(forma.getIngreso());
					fjan.setSubcuenta(forma.getSubCuenta());
					fjan.setCuenta(forma.getCuenta());
					//metodos para cargar formulario y datos del formulario
					forma.setMedicamentosNoPos(fjan.cargarMedicamentosNoPos(con, fjan, paciente));
					
					Articulo articulo=new Articulo();
					articulo.cargarArticulo(con, Integer.parseInt(forma.getMedicamentoNoPos()));
					HashMap codigoCUM=articulo.getCumMap();
					forma.getMedicamentosNoPos().put("numeroExpediente",codigoCUM.get("numero_expediente"));
					forma.getMedicamentosNoPos().put("consecutivoPresentacionComercial",codigoCUM.get("cons_present_comercial"));
					
					fjan.setMedicamentosNoPos(forma.getMedicamentosNoPos());
					logger.info("obtenerFormularioParametrizado 4 ");
					
					if(forma.getMedicamentosNoPos()!=null&&forma.getMedicamentosNoPos().containsKey("codigojus_0")&&
							forma.getMedicamentosNoPos().get("codigojus_0")!=null){
						fjan.setCodigoJustificacion(forma.getMedicamentosNoPos().get("codigojus_0").toString());
					}
					
					forma.setFormularioMap(fjan.obtenerFormularioParametrizado(con,fjan,paciente,usuario));
					forma.setMapaSecciones((HashMap)forma.getFormularioMap().get("mapasecciones"));
					//Evaluamos la cobertura del articulo
		    		forma.setFormularioMap("cobertura", UtilidadesFacturacion.consultarNombreConvenio(con, Utilidades.convertirAEntero(fjan.getConvenio().toString())));
					forma.setFormularioMap("subcuenta", fjan.getSubcuenta());
					forma.setFormularioMap("tiporegimen",UtilidadesFacturacion.consultarNombreTipoRegimen(con,  Utilidades.convertirAEntero(fjan.getConvenio().toString())));
					
					
					forma.setSustitutosNoPos(fjan.cargarSustitutosNoPos(con,fjan,paciente));
					forma.setObservacionesResumen(fjan.consultarObservacionesResumenNoPOS(con, fjan.getCodigoSolicitud()+"",fjan.getMedicamentoNoPos(),false));
					
					forma.setMedicamentosPos(fjan.cargarMedicamentosPos(con,fjan,paciente));
					if(forma.getMedicamentosPos().get("respuestaclinica")!=null){
						forma.setRespuestaClinicaParaclinica((String) forma.getMedicamentosPos().get("respuestaclinica"));
					}else{
						forma.setRespuestaClinicaParaclinica("");
					}
					
					if(forma.getMedicamentosPos().containsKey("codigoparam")&&forma.getMedicamentosPos().get("codigoparam")!=null
							&&!forma.getMedicamentosPos().get("codigoparam").toString().trim().equals("")){
						HashMap medicamentosPosMap=forma.getMedicamentosPos();
						//medicamentosPosMap.get("codigoparam")+ConstantesBD.separadorSplit+medicamentosPosMap.get("numerosolicitudparam")+ConstantesBD.separadorSplit+ConstantesBD.acronimoSi
						String medicamentoPos=medicamentosPosMap.get("codigoparam")+ConstantesBD.separadorSplit+medicamentosPosMap.get("numerosolicitudparam")+ConstantesBD.separadorSplit+ConstantesBD.acronimoSi;
						forma.setMedicamentoPos(medicamentoPos);
					}/*else{
					
						if(){
							//medicamentosPosMap.get("numRegistros")
							//medicamentosPosMap.get("codigo_"+x)+ConstantesBD.separadorSplit+medicamentosPosMap.get("numerosolicitud_"+x)+ConstantesBD.separadorSplit+ConstantesBD.acronimoNo+ConstantesBD.separadorSplit+medicamentosPosMap.get("esantecedente_"+x)+ConstantesBD.separadorSplit+medicamentosPosMap.get("nombre_"+x)
							//medicamentosPosMap.get("codigoparam")+ConstantesBD.separadorSplit+medicamentosPosMap.get("numerosolicitudparam")+ConstantesBD.separadorSplit+ConstantesBD.acronimoSi
						}
					}*/
					
					
					preCargarDiagnosticos(con, paciente.getCodigoCuenta(), paciente.getCodigoIngreso(), forma, request);
					
					fjan.setIndicesformulariomap(Listado.obtenerKeysMapa(forma.getFormularioMap()));
					fjan.setIndicesnoposmap(Listado.obtenerKeysMapa(forma.getMedicamentosNoPos()));
					fjan.setIndicesposmap(Listado.obtenerKeysMapa(forma.getMedicamentosPos()));
					fjan.setIndicessustimap(Listado.obtenerKeysMapa(forma.getSustitutosNoPos()));
					fjan.setIndicesdiagmap(Listado.obtenerKeysMapa((HashMap)forma.getDiagnosticosDefinitivos()));
					fjan.setIndicesdiag1map(Listado.obtenerKeysMapa((HashMap)forma.getDiagnosticosPresuntivos()));
					    					
					
				}
		
		if(forma.getSustitutosNoPos()!=null&&forma.getSustitutosNoPos().get("numRegistros")!=null&&Utilidades.convertirAEntero(forma.getSustitutosNoPos().get("numRegistros").toString())>0){
			forma.setExisteSustituto(true);
		}else{
			forma.setExisteSustituto(false);
		}
		
		
		//tipoFormato
		forma.getFormularioMap().put("tipoFormato", tipoFormato);
		
		try{
			ComportamientoCampo.cargarValoresDefectoAccionesCampo(con, forma, mapping, request);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		Boolean flag = false;
		for(int t=0; t< Integer.parseInt(( forma.getFormularioMap().get("numRegistros_"+ConstantesBD.JusSeccionMedicamentosNopos))+"");t++){
			
			
			if(forma.getFormularioMap().get("tipo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString().equals("RADI")&&UtilidadTexto.getBoolean(forma.getFormularioMap().get("mostrar_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t))){
				
				
				for(int x=0; x< Integer.parseInt(forma.getFormularioMap().get("numRegistros_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t))+"");x++)
				{
					if(forma.getFormularioMap().get("campopadre_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString()+"_"+x).toString().equals(forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString()))	
					{
						String idCh="chpac_"+forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString()+"_"+x;


						String idHd1="hddPosReg_"+forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString()+"_"+x;
						String idHd2="hddPosCam_"+forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString()+"_"+x;
						String idHd3="hddPosSeccion_"+forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString()+"_"+x;
						String idHd4="hddIdCampo_"+forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString()+"_"+x;


						String styleIdMapa = "valorcampo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t;
						String mapa="formularioMap("+styleIdMapa+")";
						if(forma.getFormularioMap().get(styleIdMapa)!=null && !String.valueOf(forma.getFormularioMap().get(styleIdMapa)).equals("") ){
							
							flag = true;
						}
						String value=forma.getFormularioMap().get("etiqueta_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString()+"_"+x).toString();

						String funcion="revisarRadio('"+t+"','"+ConstantesBD.JusSeccionMedicamentosNopos+"','"+forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString()+"','"+x+"','"+idCh+"');";
						if(UtilidadTexto.getBoolean(forma.getFormularioMap().get("tieneaccion_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t))){
							funcion+="cambiarEstado('accionCampo');";
						}
					}
				}
			}
			
	}
		
		
		
		
		
		
		Integer contador = 0;
		List<String> valoresRadio= (List<String>) forma.getFormularioMap().get("valoresRadioInsumos");
		for(int t=0; t< Integer.parseInt(( forma.getFormularioMap().get("numRegistros_"+ConstantesBD.JusSeccionMedicamentosNopos))+"");t++){
			
			
			if(forma.getFormularioMap().get("tipo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString().equals("RADI")&&UtilidadTexto.getBoolean(forma.getFormularioMap().get("mostrar_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t))){
				
				
				for(int x=0; x< Integer.parseInt(forma.getFormularioMap().get("numRegistros_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t))+"");x++)
				{
					if(forma.getFormularioMap().get("campopadre_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString()+"_"+x).toString().equals(forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString()))	
					{
						String idCh="chpac_"+forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString()+"_"+x;


						String idHd1="hddPosReg_"+forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString()+"_"+x;
						String idHd2="hddPosCam_"+forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString()+"_"+x;
						String idHd3="hddPosSeccion_"+forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString()+"_"+x;
						String idHd4="hddIdCampo_"+forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString()+"_"+x;


						String styleIdMapa = "valorcampo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t;
						String mapa="formularioMap("+styleIdMapa+")";

						
						String value=forma.getFormularioMap().get("etiqueta_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString()+"_"+x).toString();
						if(forma.getFormularioMap().get(styleIdMapa).toString().equals("")){

							if(t>0 &&  t % 2 == 0){
								contador++;
							}
							if(!flag){	

								if(valoresRadio!=null ){
								if(value.equals("Si") && contador<valoresRadio.size() && valoresRadio.get(contador).equals("S")){
									forma.getFormularioMap().put(styleIdMapa, "Si");
								}else if(value.equals("No") && contador<valoresRadio.size() && valoresRadio.get(contador).equals("N")){
									forma.getFormularioMap().put(styleIdMapa, "No");
								}
								}

							}
						}

						String funcion="revisarRadio('"+t+"','"+ConstantesBD.JusSeccionMedicamentosNopos+"','"+forma.getFormularioMap().get("campo_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t).toString()+"','"+x+"','"+idCh+"');";
						if(UtilidadTexto.getBoolean(forma.getFormularioMap().get("tieneaccion_"+ConstantesBD.JusSeccionMedicamentosNopos+"_"+t))){
							funcion+="cambiarEstado('accionCampo');";
						}
					}
				}
			}
			
	}
		
		forma.setCodigoOrden(ConstantesBD.codigoNuncaValido);
		
		
		if (forma.getImprimir()!=1){		
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
		else {
			forma.setImprimir(0);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("imprimir");
		}
	}

	
	  /**
     * Método encargado de validar medicamento no pos
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @param request
     * @param response
     * @return ActionForward
     * @throws Exception 
     */

	private ActionForward accionValidarJustificacion(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		

		Integer b = (Integer) request.getSession().getAttribute("RESETASOCIOJUSTIFICACION");
		if(b != null){
		forma.resetAsocioJustificacion();
		request.getSession().removeAttribute("RESETASOCIOJUSTIFICACION");
		}
		forma.setSeVaAAsociarJustificacion(false);
		//Mt 4568 - 4394 Se agrega parametro dosificacion y frecuencia medicamento NoPos
		if(UtilidadInventarios.validarTiempoTratamiento(con, Utilidades.convertirAEntero(forma.getMedicamentoNoPos()+""), forma.getUnidosis()+"", forma.getDosis()+"", forma.getTipoFrecuencia(), forma.getFrecuencia()+"", paciente)){
			return accionEmpezar(forma, con, mapping, usuario, paciente, request, response);
		}else{ 
			return mapping.findForward("asignarJustificacion");
		}
	}
    		
	  /**
     * Método encargado de validar asociar una justificacion NoPos
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @param request
     * @param response
     * @return ActionForward
     * @throws Exception 
     */
			
public ActionForward asociarJustificacion(FormatoJustArtNoposForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, HttpServletResponse response) throws Exception
{
	
	//Mt 4568 - 4394 Solucion Metodo que asocia una justificacion anterior a la solicitud  
	Integer codigoAsocioJustificacion = 0;
    codigoAsocioJustificacion = UtilidadInventarios.obtenerUltimaJustificacion(con, Utilidades.convertirAEntero(forma.getMedicamentoNoPos()+""), forma.getUnidosis()+"", forma.getDosis()+"", forma.getTipoFrecuencia(), forma.getFrecuencia()+"", paciente);
    forma.getAsocioMapas().put(Utilidades.convertirAEntero(forma.getMedicamentoNoPos().toString())+"_codigoAsocio", codigoAsocioJustificacion);
	request.getSession().setAttribute("MAPASOCIOJUSTIFICACION", forma.getAsocioMapas());
	forma.setSeVaAAsociarJustificacion(true);
	
	return mapping.findForward("asignarJustificacion");
}

	
	private ActionForward accionMostrarSubCuentas(FormatoJustArtNoposForm forma, ActionMapping mapping,
			Connection con,UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request,
			HttpServletResponse response)throws Exception
    {
		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< accionMostrarSubCuentas ");
		logger.info("\n  getCodigoSolicitud -->"+forma.getCodigoSolicitud());
		logger.info("\n  getCodigoOrden -->"+forma.getCodigoOrden());
		logger.info("getMedicamentoNoPos -->"+forma.getMedicamentoNoPos());
		
		/*
		 * Solución Tarea 45708
		 * Se crearon 2 nuevos atributos los cuales almacenarán los valores de codigo de articulo y numero de solicitud
		 * de ésta manera no se pierden cuando se hace un reset a subcuentas
		 */
		forma.setNumSolicitudSalvado(forma.getCodigoSolicitud());
		forma.setCodigoArticuloSalvado(forma.getMedicamentoNoPos());
		
		forma.setJustificacionHistorica(FormatoJustArtNopos.consultarJustificacionHistorica(con, forma.getMedicamentoNoPos(), forma.getCodigoSolicitud()));
		logger.info("Justificacion Historica");
		Utilidades.imprimirMapa(forma.getJustificacionHistorica());
		
		forma.resetSubCuentas();
		
		if(forma.isProvieneOrdenAmbulatoria())
			forma.setSubCuentasMap(FormatoJustArtNopos.SubCuentas(con, forma.getCodigoOrden(), Utilidades.convertirAEntero(forma.getMedicamentoNoPos()), true,true));
		else
			forma.setSubCuentasMap(FormatoJustArtNopos.SubCuentas(con, Utilidades.convertirAEntero(forma.getCodigoSolicitud().toString()), Utilidades.convertirAEntero(forma.getMedicamentoNoPos()), true,false));

		UtilidadBD.closeConnection(con);
		
		if(!forma.getSubCuentasMap().isEmpty() && forma.getSubCuentasMap().size()==1){
			return accionEmpezar(forma, con, mapping, usuario, paciente, request, response);
		}		
		
		logger.info("\n  getCodigoSolicitud -->"+forma.getCodigoSolicitud());
		logger.info("\n  getCodigoOrden -->"+forma.getCodigoOrden());
		logger.info("getMedicamentoNoPos -->"+forma.getMedicamentoNoPos());
		
		return mapping.findForward("selectSubCuenta");
    }




	/**
	 * Este método precarga los diagnosticos de la evolución (Diagnosticos
	 * sugeridos) en la forma de Evolucion (EvolucionForm), utilizando la
	 * clase AuxiliarDiagnosticos
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param numeroCuenta Número de la cuenta a la que pertenece esta
	 * evolución
	 * @param ingreso 
	 * @param bean Objeto de tipo FormatoJustServNoposForm, donde se almaceran los
	 * diagnosticos
	 * @param request Objeto request donde se van a poner el número de
	 * diagnosticos presuntivos y definitivos que se cargaron
	 * @throws SQLException
	 */
	public void preCargarDiagnosticos (Connection con, int numeroCuenta, int codigoIngreso, FormatoJustArtNoposForm bean, HttpServletRequest request) throws SQLException
	{
		// ************ CARGAR DIAGNOSTICOS ************
		bean.setDiagnosticosDefinitivos(new HashMap());
		ArrayList<Diagnostico> diagnosticos = new ArrayList<Diagnostico>();
		diagnosticos = UtilidadesHistoriaClinica.obtenerUltimosDiagnosticoIngreso(con, codigoIngreso, true);
		
		//-- Dx Principal
		for(int d=0; d<diagnosticos.size(); d++){
			if(diagnosticos.get(d).isComplicacion()){

				bean.setDiagnosticoComplicacion_1(diagnosticos.get(d).getAcronimo()+ConstantesBD.separadorSplit+diagnosticos.get(d).getTipoCIE()+ConstantesBD.separadorSplit+diagnosticos.get(d).getNombre());
			}
		}

		//-- Dx Complicacion
		for(int d=0; d<diagnosticos.size(); d++){
			if(diagnosticos.get(d).isPrincipal()){
				bean.setDiagnosticoDefinitivo("principal", diagnosticos.get(d).getAcronimo()+ConstantesBD.separadorSplit+diagnosticos.get(d).getTipoCIE()+ConstantesBD.separadorSplit+diagnosticos.get(d).getNombre());
			}
		}
		
		//-- Dx Relacionados
		int numDx=0;
		String dxSeleccionados="'"+ConstantesBD.codigoNuncaValido+"'";
		for(int d=0; d<diagnosticos.size(); d++){
			if(!diagnosticos.get(d).isPrincipal() && !diagnosticos.get(d).isComplicacion()){
				bean.setDiagnosticoDefinitivo("relacionado_" +numDx, diagnosticos.get(d).getAcronimo()+ConstantesBD.separadorSplit+diagnosticos.get(d).getTipoCIE()+ConstantesBD.separadorSplit+diagnosticos.get(d).getNombre());
				bean.setDiagnosticoDefinitivo("checkbox_"+numDx, "true");
				dxSeleccionados += ", '"+diagnosticos.get(d).getAcronimo()+"'";
				numDx++;
			}
		}
		bean.setDiagnosticoDefinitivo("dxSeleccionados", dxSeleccionados);
		bean.setNumDiagnosticosDefinitivos(numDx);
		
		// **********************************************
	}
}
