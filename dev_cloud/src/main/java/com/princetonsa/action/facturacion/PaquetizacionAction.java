/*
 * Jun 15, 2007
 * Proyect axioma
 * Paquete com.princetonsa.action.facturacion
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.action.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.action.ComunAction;
import com.princetonsa.action.cartera.AprobacionPagosCarteraAction;
import com.princetonsa.actionform.facturacion.PaquetizacionForm;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.facturacion.ComponentesPaquetes;
import com.princetonsa.mundo.facturacion.EntidadesSubContratadas;
import com.princetonsa.mundo.facturacion.Paquetizacion;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class PaquetizacionAction extends Action 
{
	/**
     * manejador de los logs de la clase
     */
    private Logger logger=Logger.getLogger(AprobacionPagosCarteraAction.class);

    
    //////indices mapa paquetes responsable: codpaquetizacion_pos | subcuenta_pos | solicitud_pos | codpaqueteconvenio_pos | convenio_pos | contrato_pos | codpaquete_pos | nompaquete_pos | codviaingreso_pos | nomviaingreso_pos | fechainicialven_pos | fechafinalven_pos | serviciopaquete_pos | numrespaquete_pos | numresfacpaquete_pos | componentesServicios_  | componentesAgurpacionServicios_ | componentesArticulos_ | componentesAgurpacionArticulos_ | centrocosto_pos | ccsolicitafiltro_pos | ccejecutafiltro_pos | fechainisolfiltro_pos| fechafinsolfiltro_pos | facturado_pos | tiporegistro_pos
    
    
    //////indices mapa paquete servicio: codigo_codserv_codpaq | codpaquetizacion_codserv_codpaq | solicitud_codserv_codpaq | servicio_codserv_codpaq | articulo_codserv_codpaq | cantidad_codserv_codpaq | serviciocx_codserv_codpaq | tipoasocio_codserv_codpaq | nomtipoasocio_codserv_codpaq
   
    
    //////indicesAgrupacionArticulos={"codigo_","paquete_","clase_","nomclase_","grupo_","nomgrupo_","subgrupo_","nomsubgrupo_","naturaleza_","nomnaturaleza_","cantidad_","tiporegistro_"};

	//////indicesArticulos={"codigo_","paquete_","codigoArticulo_","descripcionArticulo_","cantidad_","tiporegistro_"};

	//////indicesAgrupacionServicio={"codigo_","paquete_","gruposervicio_","acronimogruposervicio_","descgruposervicio_","tiposervicio_","nomtiposervicio_","especialidad_","nomespecialidad_","cantidad_","tiporegistro_"};
	
	//////indicesServicios={"codigo_","paquete_","codigoServicio_","descripcionServicio_","principal_","cantidad_","tiporegistro_"};
    /**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
													        {
		Connection con = null;	
		try{
			if(form instanceof PaquetizacionForm)
			{

				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				PaquetizacionForm forma=(PaquetizacionForm)form;
				Paquetizacion mundo=new Paquetizacion ();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente=Utilidades.getPersonaBasicaSesion(request.getSession());

				String estado=forma.getEstado();
				logger.warn("estado--->"+estado);
				if(estado == null)
				{
					logger.warn("Estado no valido ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}	
				if(paciente.getCodigoPersona()<1)
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no Cargado", "errors.paciente.noCargado", true);
				}
				forma.setCuentaProcesoDistribucion(UtilidadValidacion.ingresoEstaEnProcesoDistribucion(con,paciente.getCodigoIngreso(),usuario.getLoginUsuario()));
				logger.info("===>Cuenta En Procesco Distribución 1: "+forma.isCuentaProcesoDistribucion());


				if(estado.equals("empezar") )
				{
					if (paciente.getCodigoCuenta()==0)
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente sin cuenta", "errors.paciente.cuentaNoAbierta", true) ;
					}
					/**
					 * Validar concurrencia
					 * Si ya está en proceso de facturación, no debe dejar entrar
					 **/
					if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
					}

					/*
				else if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), usuario.getLoginUsuario()) )
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
				}
					 */

					InfoDatosInt estadoCuenta=UtilidadesHistoriaClinica.obtenerEstadoCuenta(con, paciente.getCodigoCuenta());
					if(estadoCuenta.getCodigo()!=ConstantesBD.codigoEstadoCuentaActiva&&estadoCuenta.getCodigo()!=ConstantesBD.codigoEstadoCuentaAsociada&&estadoCuenta.getCodigo()!=ConstantesBD.codigoEstadoCuentaFacturadaParcial)
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "cuenta estado diferente", "La cuenta se encuentra en estado "+estadoCuenta.getNombre(), false);
					}

					InfoDatosInt centroAtencionCuenta=UtilidadesHistoriaClinica.obtenerCentroAtencionCuenta(con,paciente.getCodigoCuenta());
					if(centroAtencionCuenta.getCodigo()!=usuario.getCodigoCentroAtencion())
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "cuenta estado diferente", "LA CUENTA NO PERTENCE AL CENTRO DE ATENCION DEL USUARIO.", false);
					}
					InfoDatosString estadoIngreso=UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso());
					if(!estadoIngreso.getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto)&&!estadoIngreso.getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoCerrado))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Ingreso estado diferente", "El Ingreso se encuentra en estado "+estadoIngreso.getNombre()+" .", false);
					}

					if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarEgresoAdministrativoPaquetizar(usuario.getCodigoInstitucionInt())))
					{
						if(!UtilidadValidacion.tieneEgreso(con,paciente.getCodigoCuenta()))
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no tiene Egreso Administrativo", "El paciente no tiene Egreso Administrativo", false);

					}

					return this.accionEmpezar(con,forma,mundo,paciente,usuario,mapping);
				}
				else if(estado.equals("cargarInfo"))
				{
					forma.setMensajeProceso(new ResultadoBoolean(false,""));
					forma.setNumPaquetesMemoria(0);
					forma.setServiciosPaquetes(new HashMap<String, Object>());
					forma.setServiciosPaquetes("numRegistros", "0");
					if(forma.getIndexResponSeleccionado()>=0)
						return this.accionCargarInfo(con,forma,mundo,mapping,paciente,usuario);
					else
						return this.accionEmpezar(con,forma,mundo,paciente,usuario,mapping);
				}
				else if(estado.equals("adicionaPaquete"))
				{
					return this.accionAdicionarPaquete(con,forma,mapping);
				}
				else if(estado.equals("liquidacionAutomatica"))
				{
					forma.setMensajeProceso(new ResultadoBoolean(false,""));
					this.accionLiquidacionAutomatica(con,forma,mundo);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("eliminarPaquete"))
				{
					forma.setMensajeProceso(new ResultadoBoolean(false,""));
					if((forma.getPaquetesResponsables().get("tiporegistro_"+ forma.getIndicePaqueteLiquidacionAuto())+"").trim().equals("MEM"))
					{
						forma.setNumPaquetesMemoria(forma.getNumPaquetesMemoria()-1);
					}
					String[] indices={"codpaquetizacion_","subcuenta_","solicitud_","codpaqueteconvenio_","convenio_","contrato_","codpaquete_","nompaquete_","codviaingreso_","nomviaingreso_","fechainicialven_","fechafinalven_","serviciopaquete_","nomserviciopaquete_","numrespaquete_","numresfacpaquete_","componentesServicios_","componentesAgurpacionServicios_","componentesArticulos_","componentesAgurpacionArticulos_","centrocosto_","ccsolicitafiltro_","ccejecutafiltro_","fechainisolfiltro_","fechafinsolfiltro_","facturado_","tiporegistro_"};
					Utilidades.eliminarRegistroMapaGenerico(forma.getPaquetesResponsables(), forma.getPaquetesResponsablesEliminados(), forma.getIndicePaqueteLiquidacionAuto(), indices, "numRegistros", "tiporegistro_", "BD", false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("asignarEventoPaquete"))
				{
					forma.setMensajeProceso(new ResultadoBoolean(false,""));
					this.accionAsignarEventoPaquete(forma,mundo);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardar"))
				{
					/**
					 * Validar concurrencia
					 * Si ya está en proceso de distribucion, no debe dejar entrar
					 **/
					if(UtilidadValidacion.ingresoEstaEnProcesoDistribucion(con,paciente.getCodigoIngreso(),usuario.getLoginUsuario()) )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.ingresoEnProcesoDistribucion", "error.facturacion.ingresoEnProcesoDistribucion", true);
					}

					//validaciones
					ActionErrors errores=new ActionErrors();
					for(int i=0;i<convertirEntero(forma.getPaquetesResponsables("numRegistros")+"");i++)
					{
						ArrayList<String> serviciosPrincipales=this.obtenerServiciosPrincipales((HashMap)forma.getPaquetesResponsables("componentesServicios_"+i));
						//si tiene servicios principales hacer la validacion.
						if(serviciosPrincipales.size()>0)
						{
							int cantidad=0;
							//recorrer todos los servicios principlaes, y buscarlos en los que se han distribuido para el responsable. sumando su cantidad, si cantidad es > 0 quiere decir que sí lo contiene.
							for(int j=0;j<serviciosPrincipales.size();j++)
							{
								for(int k=0;k<forma.getResponsableCuenta().getSolicitudesSubcuenta().size();k++)
								{
									String servicio=serviciosPrincipales.get(j);
									if(serviciosPrincipales.get(j).equals((forma.getResponsableCuenta().getSolicitudesSubcuenta().get(k)).getServicio().getCodigo())||(serviciosPrincipales.get(j).equals((forma.getResponsableCuenta().getSolicitudesSubcuenta().get(k)).getServicioCX().getCodigo())))
									{
										servicio=(forma.getResponsableCuenta().getSolicitudesSubcuenta().get(k)).getServicio().getCodigo();
									}

									String servicioCXTempo=(forma.getResponsableCuenta().getSolicitudesSubcuenta().get(k)).getServicioCX().getCodigo();
									String tipoAsocioTempo=(forma.getResponsableCuenta().getSolicitudesSubcuenta().get(k)).getTipoAsocio().getCodigo()>0?(forma.getResponsableCuenta().getSolicitudesSubcuenta().get(k)).getTipoAsocio().getCodigo()+"":"-1";
									int detcxhonorarios=(forma.getResponsableCuenta().getSolicitudesSubcuenta().get(k)).getDetcxhonorarios();
									int detasicxsalasmat=(forma.getResponsableCuenta().getSolicitudesSubcuenta().get(k)).getDetasicxsalasmat();

									servicioCXTempo=UtilidadTexto.isEmpty(servicioCXTempo)?"-1":servicioCXTempo;


									cantidad+=convertirEntero(forma.getServiciosPaquetes("cantidad_"+k+"_"+servicio+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+forma.getPaquetesResponsables("codpaquetizacion_"+i)+"_"+detcxhonorarios+"_"+detasicxsalasmat)+"");
								}
							}
							if(cantidad<=0&&!UtilidadTexto.getBoolean(forma.getPaquetesResponsables("facturado_"+i)+""))
							{
								errores.add("", new ActionMessage("error.paquetizacion.paqueteSinServicioPrincipal",forma.getPaquetesResponsables("nompaquete_"+i)+""));

							}
						}
					}
					if(!errores.isEmpty())
					{
						saveErrors(request, errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}
					this.accionGuardarPaquetizacion(con,forma,mundo,usuario,paciente);

					forma.setNumPaquetesMemoria(0);
					forma.setServiciosPaquetes(new HashMap<String, Object>());
					forma.setServiciosPaquetes("numRegistros", "0");
					forma.setPaquetesResponsables(new HashMap<String, Object>());
					forma.setPaquetesResponsables("numRegistros", "0");
					forma.setPaquetesResponsablesEliminados(new HashMap<String, Object>());
					forma.getPaquetesResponsablesEliminados().put("numRegistros", "0");
					return this.accionCargarInfo(con,forma,mundo,mapping,paciente, usuario);
				}
				else if(estado.equals("liquidacionPaquetes"))
				{
					forma.setMensajeProceso(new ResultadoBoolean(false,""));
					return this.accionLiqudacionPaquetes(con,forma,mundo,mapping);
				}
				else if(estado.equals("generarReporte"))
				{                
					forma.setMensajeProceso(new ResultadoBoolean(false,""));
					this.generarReporte(con, forma, mapping, request, usuario,paciente);                
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("generarReporteResumido"))
				{                
					forma.setMensajeProceso(new ResultadoBoolean(false,""));
					this.generarReporteResumido(con, forma, mapping, request, usuario,paciente);                
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("consultar"))
				{
					forma.reset();
					forma.setIngresos(mundo.consultarIngresos(con, paciente.getCodigoPersona(),false));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("consultarPaquetizacion");
				}
				else if(estado.equals("cargarIngreso"))
				{
					forma.setMensajeProceso(new ResultadoBoolean(false,""));
					return this.accionCargarInfoIngresoConsulta(con,forma,mundo,mapping,paciente,usuario);			
				}
				else
				{
					logger.warn("Estado no valido ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form dePaquetizacionForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
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
	 * @param mundo
	 * @param mapping
	 * @param paciente 
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionCargarInfoIngresoConsulta(Connection con, PaquetizacionForm forma, Paquetizacion mundo, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario) throws IPSException 
	{
		forma.setResponsables(UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con,Utilidades.convertirAEntero(forma.getIngresos().get("idingreso_"+forma.getIndiceIngresoSeleccionado())+"",false),true,new String[0],false, "" /*subCuenta*/,ConstantesBD.codigoNuncaValido /*Vía de ingreso*/));
		
		if(forma.getResponsables().size()==1)
		{
			//posturar el unico responsable.
			forma.setIndexResponSeleccionado(0);
			return this.accionCargarInfo(con, forma, mundo, mapping,paciente, usuario);
		}
		
		//tarea  id=22376
		
		/*****************************************************************************************************************/
		ArrayList<String> aux = new ArrayList<String>();
        //se valida que el paciente es de entidad subcontratada
		//logger.info("\n VOY A ENTRAR A ENTIDAD  CONSULTAR!!!");
	    /*****************************************************************************************************************/

		if (IngresoGeneral.esIngresoComoEntidadSubContratada(con, paciente.getCodigoIngreso()).equals(ConstantesBD.acronimoSi))
	    {
	    //	logger.info("\n YA ENTRE A ENTIDAD CONSULTAR!!!");
	    	aux.add("Ingreso de paciente en entidad subcontratada "+EntidadesSubContratadas.getDescripcionEntidadSubXIngreso(con, paciente.getCodigoIngreso()+""));
	    	forma.setMensajes(aux);
	    	//logger.info("\n VOY A SALIR DE ENTIDAD CONSULTAR!!!");
	    }
		 
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @return
	 */
	private ActionForward accionCargarInfoConsulta(Connection con, PaquetizacionForm forma, Paquetizacion mundo, ActionMapping mapping) 
	{
		//cargamos el responsable seleccionado
		forma.setResponsableCuenta(forma.getResponsables().get(forma.getIndexResponSeleccionado()));
		
		//cargamos las solicitudes(serv-arti) del responsable
		forma.getResponsableCuenta().setSolicitudesSubcuenta(mundo.obtenerSolicitudesSubCuentaPaquetizar(con,forma.getResponsableCuenta().getSubCuenta()));
		
		//cargamos los paquetes que ya tiene el responsable
		forma.setPaquetesResponsables(mundo.consultarPaquetesAsocioadosResponsableSubcuenta(con,forma.getResponsableCuenta().getSubCuenta()));

		///cargamos los servicios, de cada paquete, toca cargarlos a un mapa aparte para poder modificarlos desde el jsp,
		///ya que no se puden enviar objetos, por etiquietas html, no se puede manejar un mapa entre otro mapa, y modificarlo.
		//////cargarlos bien en el mapa
		/////////asignar los servicios.
		if(Integer.parseInt(forma.getPaquetesResponsables().get("numRegistros")+"")>0)
		{
			String[] codigoPaquetizaciones=new String[Integer.parseInt(forma.getPaquetesResponsables().get("numRegistros")+"")];
			for(int i=0;i<codigoPaquetizaciones.length;i++)
			{
				codigoPaquetizaciones[i]=forma.getPaquetesResponsables().get("codpaquetizacion_"+i)+"";
			}
			
			HashMap mapaSerTemp=mundo.consultarServiciosPaquetes(con,codigoPaquetizaciones);
			for(int i=0;i<forma.getResponsableCuenta().getSolicitudesSubcuenta().size();i++)
			{
				DtoSolicitudesSubCuenta dtoTemp=forma.getResponsableCuenta().getSolicitudesSubcuenta().get(i);
				String servArti=dtoTemp.getServicio().getCodigo().trim().equals("")?dtoTemp.getArticulo().getCodigo():dtoTemp.getServicio().getCodigo();
				String servicioCXTempo=dtoTemp.getServicioCX().getCodigo();
				String tipoAsocioTempo=dtoTemp.getTipoAsocio().getCodigo()>0?dtoTemp.getTipoAsocio().getCodigo()+"":"-1";
				int detcxhonorarios=dtoTemp.getDetcxhonorarios();
				int detasicxsalasmat=dtoTemp.getDetasicxsalasmat();
				servicioCXTempo=UtilidadTexto.isEmpty(servicioCXTempo)?"-1":servicioCXTempo;
				for(int j=0;j<codigoPaquetizaciones.length;j++)
				{
					//String valor=""
					String indice=servArti+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+codigoPaquetizaciones[j]+"_"+detcxhonorarios+"_"+detasicxsalasmat;
					forma.setServiciosPaquetes("codigo_"+i+"_"+indice,mapaSerTemp.get("codigo_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
					forma.setServiciosPaquetes("codpaquetizacion_"+i+"_"+indice,mapaSerTemp.get("codpaquetizacion_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
					forma.setServiciosPaquetes("solicitud_"+i+"_"+indice,mapaSerTemp.get("solicitud_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
					forma.setServiciosPaquetes("servicio_"+i+"_"+indice,mapaSerTemp.get("servicio_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
					forma.setServiciosPaquetes("articulo_"+i+"_"+indice,mapaSerTemp.get("articulo_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
					forma.setServiciosPaquetes("cantidad_"+i+"_"+indice,convertirEntero(mapaSerTemp.get("cantidad_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"")+"");
					forma.setServiciosPaquetes("principal_"+i+"_"+indice,mapaSerTemp.get("principal_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
					forma.setServiciosPaquetes("serviciocx_"+i+"_"+indice,mapaSerTemp.get("serviciocx_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
					forma.setServiciosPaquetes("detcxhonorarios_"+i+"_"+indice,mapaSerTemp.get("detcxhonorarios_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
					forma.setServiciosPaquetes("detasicxsalasmat_"+i+"_"+indice,mapaSerTemp.get("detasicxsalasmat_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
					forma.setServiciosPaquetes("tipoasocio_"+i+"_"+indice,mapaSerTemp.get("tipoasocio_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
					forma.setServiciosPaquetes("nomtipoasocio_"+i+"_"+indice,mapaSerTemp.get("nomtipoasocio_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
				}
			}
			forma.setServiciosPaquetes("numRegistros",forma.getResponsableCuenta().getSolicitudesSubcuenta().size());
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}



	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuarioActual
	 * @param paciente 
	 * @return
	 */
	private ActionForward generarReporte(Connection con, PaquetizacionForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual, PersonaBasica paciente) 
	{
		String nombreRptDesign = "Paquetizacion.rptdesign";
		
		InstitucionBasica ins= new InstitucionBasica();
		ins.cargarXConvenio(usuarioActual.getCodigoInstitucionInt(), forma.getResponsableCuenta().getConvenio().getCodigo());
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+"  -  "+ins.getNit());
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0, 1, v);
        comp.insertLabelInGridPpalOfHeader(1, 0, "PAQUETIZACIONES");
        comp.insertLabelInGridPpalOfHeader(2, 0, "Fecha Impresión: "+UtilidadFecha.getFechaActual(con)+" - "+UtilidadFecha.getHoraActual(con));
        
        //***************INICIO SE INCLUYO ESTA PARTE PARA MIRAR COMO UTILIZABAN LAS CONSULTAS**********************//
        comp.obtenerComponentesDataSet("ConsumosEvento");
        String newQuery = comp.obtenerQueryDataSet();
        logger.info("===>Consulta Consumos Evento: "+newQuery);
        
        comp.obtenerComponentesDataSet("DatosPaciente");
        newQuery = comp.obtenerQueryDataSet();
        logger.info("===>Consulta Datos Paciente: "+newQuery);
        
        comp.obtenerComponentesDataSet("PaquetesProcesados");
        newQuery = comp.obtenerQueryDataSet();
        logger.info("===>Consulta Paquetes Procesados: "+newQuery);
        
        logger.info("===>Ingreso: "+paciente.getCodigoIngreso());
        logger.info("===>Sub Cuenta: "+forma.getResponsableCuenta().getSubCuenta());
        //***************FIN SE INCLUYO ESTA PARTE PARA MIRAR COMO UTILIZABAN LAS CONSULTAS**********************//
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        
        // se mandan los parámetros al reporte
        newPathReport += "&cuenta="+paciente.getCodigoCuenta()+
        				 "&subCuenta="+Utilidades.convertirAEntero(forma.getResponsableCuenta().getSubCuenta());
        	
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        
		UtilidadBD.closeConnection(con);
		return mapping.findForward("liquidacionPaquetes");
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuarioActual
	 * @param paciente 
	 * @return
	 */
	private ActionForward generarReporteResumido(Connection con, PaquetizacionForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual, PersonaBasica paciente) 
	{
		String nombreRptDesign = "PaquetizacionResumido.rptdesign";
		
		InstitucionBasica ins= new InstitucionBasica();
		ins.cargarXConvenio(usuarioActual.getCodigoInstitucionInt(), forma.getResponsableCuenta().getConvenio().getCodigo());
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(2,1, "                  Paquetizaciones.");
        comp.insertLabelInGridPpalOfHeader(2,0, "Fecha Impresion: "+UtilidadFecha.getFechaActual(con)+"   "+UtilidadFecha.getHoraActual(con));
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        // se mandan los parámetros al reporte
        newPathReport += "&subCuenta="+Utilidades.convertirAEntero(forma.getResponsableCuenta().getSubCuenta());
        	
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("liquidacionPaquetes");
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @return
	 */
	private ActionForward accionLiqudacionPaquetes(Connection con, PaquetizacionForm forma, Paquetizacion mundo, ActionMapping mapping) 
	{
		forma.setLiquidacionPaquetes(mundo.consultarLiquidacionPaquete(con,forma.getResponsableCuenta().getSubCuenta()));
		double valorTotal=0,valorTotalAsignado=0,valorTotalEvento=0;
		for(int i=0;i<convertirEntero(forma.getLiquidacionPaquetes().get("numRegistros")+"");i++)
		{
			valorTotal=valorTotal+Utilidades.convertirADouble(forma.getLiquidacionPaquetes().get("valorpaquete_"+i)+"",true);
			valorTotalAsignado=valorTotalAsignado+Utilidades.convertirADouble(forma.getLiquidacionPaquetes().get("valorpaqueteasignado_"+i)+"",true);
			valorTotalEvento=valorTotalEvento+Utilidades.convertirADouble(forma.getLiquidacionPaquetes().get("valorevento_"+i)+"",true);
			forma.getLiquidacionPaquetes().put("diferencia_"+i, (Utilidades.convertirADouble(forma.getLiquidacionPaquetes().get("valorpaquete_"+i)+"",true)-Utilidades.convertirADouble(forma.getLiquidacionPaquetes().get("valorpaqueteasignado_"+i)+"",true)));
		}
		forma.getLiquidacionPaquetes().put("valortotalpaquete", valorTotal+"");
		forma.getLiquidacionPaquetes().put("valortotalasignado", valorTotalAsignado+"");
		forma.getLiquidacionPaquetes().put("valortotalevento", valorTotalEvento+"");
		forma.getLiquidacionPaquetes().put("totalcuenta", (valorTotal+valorTotalEvento));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("liquidacionPaquetes");
	}

	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param paciente 
	 */
	private void accionGuardarPaquetizacion(Connection con, PaquetizacionForm forma, Paquetizacion mundo, UsuarioBasico usuario, PersonaBasica paciente) throws IPSException 
	{
		logger.info("1. EMPIEZA EL PROCESO DE PAQUETIZACION.");
		//marcar como paquetizados, los que se guardan, y marcar como despaquetizados, los que se eliminan., se puede hacer en el metodo de eliminar paquetizacion.
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		for(int i=0;i<convertirEntero(forma.getPaquetesResponsablesEliminados().get("numRegistros")+"");i++)
		{
			transaccion=mundo.eliminarPaquetizacion(con,forma.getPaquetesResponsablesEliminados().get("codpaquetizacion_"+i)+"",forma.getPaquetesResponsablesEliminados().get("solicitud_"+i)+"",true);
		}
		logger.info("2. SE ELIMINO LA PAQUETIZACION ANTERIOR.");
		if(transaccion)
		{
			logger.info("3. ACTUALIZANDO LAS CANTIDAD DET CARGOS.");
			//actualizar la cantidad evento de las solicitudes del paquete.
			for(int j=0;j<forma.getResponsableCuenta().getSolicitudesSubcuenta().size();j++)
			{
				DtoSolicitudesSubCuenta solSubcuentas=forma.getResponsableCuenta().getSolicitudesSubcuenta().get(j);
				if(!UtilidadTexto.getBoolean(solSubcuentas.getFacturado()))
				{
					String servArti=solSubcuentas.getServicio().getCodigo().trim().equals("")?solSubcuentas.getArticulo().getCodigo():solSubcuentas.getServicio().getCodigo();
					String servicioCXTempo=solSubcuentas.getServicioCX().getCodigo();
					String tipoAsocioTempo=solSubcuentas.getTipoAsocio().getCodigo()>0?solSubcuentas.getTipoAsocio().getCodigo()+"":"-1";
					servicioCXTempo=UtilidadTexto.isEmpty(servicioCXTempo)?"-1":servicioCXTempo;
					int detcxhonorarios=solSubcuentas.getDetcxhonorarios();
					int detasicxsalasmat=solSubcuentas.getDetasicxsalasmat();

					int evento=convertirEntero(forma.getServiciosPaquetes("evento_"+j+"_"+servArti+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+detcxhonorarios+"_"+detasicxsalasmat)+"");
					logger.info("servicio --->"+solSubcuentas.getServicio().getCodigo());
					logger.info("servicio_cx --->"+solSubcuentas.getServicioCX().getCodigo());
					logger.info("tipo_asocio --->"+solSubcuentas.getTipoAsocio().getCodigo());
					logger.info("articulo --->"+solSubcuentas.getArticulo().getCodigo());
					logger.info("codigo_det_cargo --->"+solSubcuentas.getArticulo().getCodigo());
					logger.info("cantidad evento -->"+evento);
					if(solSubcuentas.getServicio().getCodigo().trim().equals(""))
					{
						mundo.actualizarCantidadDetCargo(con,evento,forma.getResponsableCuenta().getSubCuenta(), solSubcuentas.getNumeroSolicitud(),solSubcuentas.getArticulo().getCodigo(),ConstantesBD.codigoNuncaValido,false,ConstantesBD.acronimoNo);
					}
					else
					{
						if(!UtilidadTexto.isEmpty(solSubcuentas.getServicioCX().getCodigo()))//en caso de cirugia
						{
							mundo.actualizarCantidadDetCargoServicioCx(con,evento,forma.getResponsableCuenta().getSubCuenta(),solSubcuentas.getNumeroSolicitud(),solSubcuentas.getServicio().getCodigo(),solSubcuentas.getServicioCX().getCodigo(),solSubcuentas.getTipoAsocio().getCodigo(),ConstantesBD.codigoNuncaValido,ConstantesBD.acronimoNo,solSubcuentas.getDetcxhonorarios(),solSubcuentas.getDetasicxsalasmat());
						}
						else
						{
							mundo.actualizarCantidadDetCargo(con,evento,forma.getResponsableCuenta().getSubCuenta(),solSubcuentas.getNumeroSolicitud(),solSubcuentas.getServicio().getCodigo(),ConstantesBD.codigoNuncaValido,true,ConstantesBD.acronimoNo);
						}
					}
				}
			}
			
			logger.info("4. SE EMPIEZAN A GUARDAR LOS PAQUETES. NUMERO DE PAQUETES: "+convertirEntero(forma.getPaquetesResponsables("numRegistros")+""));

			for(int i=0;i<convertirEntero(forma.getPaquetesResponsables("numRegistros")+"");i++)
			{

				logger.info("5. SI ES PAQUETE DE MEMORIA SE GENERA LA SOLICITUD.");
				
				if(!UtilidadTexto.getBoolean(forma.getPaquetesResponsables().get("facturado_"+i)+""))
				{
					//si es tipo registro memoria, generar la solicitud.
					if((forma.getPaquetesResponsables().get("tiporegistro_"+i)+"").trim().equals("MEM"))
					{
						logger.info("6. GENERANDO LA SOLICITUD, VERIFICAR SI TIENE CANTIDAD ASIGNADA.");
						int asignados=0;
						for(int j=0;j<forma.getResponsableCuenta().getSolicitudesSubcuenta().size();j++)
						{
							DtoSolicitudesSubCuenta solSubcuentas=forma.getResponsableCuenta().getSolicitudesSubcuenta().get(j);
							String servArti=solSubcuentas.getServicio().getCodigo().trim().equals("")?solSubcuentas.getArticulo().getCodigo():solSubcuentas.getServicio().getCodigo();
							String servicioCXTempo=solSubcuentas.getServicioCX().getCodigo();
							String tipoAsocioTempo=solSubcuentas.getTipoAsocio().getCodigo()>0?solSubcuentas.getTipoAsocio().getCodigo()+"":"-1";
							int detcxhonorarios=solSubcuentas.getDetcxhonorarios();
							int detasicxsalasmat=solSubcuentas.getDetasicxsalasmat();
							servicioCXTempo=UtilidadTexto.isEmpty(servicioCXTempo)?"-1":servicioCXTempo;
							if(forma.getServiciosPaquetes().containsKey("cantidad_"+j+"_"+servArti+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+forma.getPaquetesResponsables("codpaquetizacion_"+i)+"_"+detcxhonorarios+"_"+detasicxsalasmat))
							{
								int valor=convertirEntero(forma.getServiciosPaquetes("cantidad_"+j+"_"+servArti+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+forma.getPaquetesResponsables("codpaquetizacion_"+i)+"_"+detcxhonorarios+"_"+detasicxsalasmat)+"");
								if(valor>0)
								{
									asignados++;
								}
							}
						}
						logger.info("7. TIENE SERVICIOS/ARTICULOS ASOCIADOS "+(asignados>0));
						if(asignados>0)
						{
							int consecutivoSol=this.generarSoliciutud(con,forma,paciente,i,forma.getPaquetesResponsables("serviciopaquete_"+i)+"", usuario);
							logger.info("8. SOLICITUD GENERADA "+consecutivoSol);
							if(consecutivoSol<=0)
							{
								transaccion=false;
								i=convertirEntero(forma.getPaquetesResponsables("numRegistros")+"");
							}
							else
							{
								logger.info("9. CONTINUAL EL PROCESO DE PAQUETIZACION.");
															
								HashMap vo=new HashMap();
								vo.put("subcuenta", forma.getPaquetesResponsables("subcuenta_"+i)+"");
								vo.put("institucion", usuario.getCodigoInstitucion());
								vo.put("numerosolicitud", consecutivoSol+"");
								vo.put("codpaquete", forma.getPaquetesResponsables("codpaqueteconvenio_"+i)+"");
								vo.put("centrocosto", forma.getPaquetesResponsables("centrocosto_"+i)+"");
								vo.put("ccsolicitafiltro", forma.getPaquetesResponsables("ccsolicitafiltro_"+i)+"");
								vo.put("ccejecutafiltro", forma.getPaquetesResponsables("ccejecutafiltro_"+i)+"");
								vo.put("fechainisolfiltro", forma.getPaquetesResponsables("fechainisolfiltro_"+i)+"");
								vo.put("fechafinsolfiltro", forma.getPaquetesResponsables("fechafinsolfiltro_"+i)+"");
								vo.put("servicio", forma.getPaquetesResponsables("serviciopaquete_"+i)+"");
								vo.put("usuario", usuario.getLoginUsuario());
								vo.put("fecha", UtilidadFecha.getFechaActual());
								vo.put("hora", UtilidadFecha.getHoraActual());
								
								int codigoPaquetizacion=ConstantesBD.codigoNuncaValido;
								//solo se puede guardar si se le asignaron componentes.
								if(asignados>0)
									codigoPaquetizacion=mundo.insertarEncabezadoPaquetizacion(con,vo);
								if(codigoPaquetizacion<=0)
								{
									if(asignados>0)
										transaccion=false;
		
									i=convertirEntero(forma.getPaquetesResponsables("numRegistros")+"");
								}
								else
								{
									HashMap detalle=new HashMap();
									detalle.put("numRegistros", "0");
									detalle.put("subcuenta", forma.getPaquetesResponsables("subcuenta_"+i)+"");
									detalle.put("numerosolicitud", consecutivoSol+"");
									asignados=0;
									for(int j=0;j<forma.getResponsableCuenta().getSolicitudesSubcuenta().size();j++)
									{
										DtoSolicitudesSubCuenta solSubcuentas=forma.getResponsableCuenta().getSolicitudesSubcuenta().get(j);
										String servArti=solSubcuentas.getServicio().getCodigo().trim().equals("")?solSubcuentas.getArticulo().getCodigo():solSubcuentas.getServicio().getCodigo();
										String servicioCXTempo=solSubcuentas.getServicioCX().getCodigo();
										String tipoAsocioTempo=solSubcuentas.getTipoAsocio().getCodigo()>0?solSubcuentas.getTipoAsocio().getCodigo()+"":"-1";
										servicioCXTempo=UtilidadTexto.isEmpty(servicioCXTempo)?"-1":servicioCXTempo;
										int detcxhonorarios=solSubcuentas.getDetcxhonorarios();
										int detasicxsalasmat=solSubcuentas.getDetasicxsalasmat();

										if(forma.getServiciosPaquetes().containsKey("cantidad_"+j+"_"+servArti+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+forma.getPaquetesResponsables("codpaquetizacion_"+i) +"_"+detcxhonorarios+"_"+detasicxsalasmat))
										{
											int valor=convertirEntero(forma.getServiciosPaquetes("cantidad_"+j+"_"+servArti+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+forma.getPaquetesResponsables("codpaquetizacion_"+i)+"_"+detcxhonorarios+"_"+detasicxsalasmat)+"");
											if(valor>0)
											{
												detalle.put("codpaquetizacion_"+asignados, codigoPaquetizacion);
												detalle.put("numerosolicitud_"+asignados, solSubcuentas.getNumeroSolicitud());
												detalle.put("servicio_"+asignados,solSubcuentas.getServicio().getCodigo());
												detalle.put("articulo_"+asignados,solSubcuentas.getArticulo().getCodigo());
												detalle.put("cantidad_"+asignados, valor);
	
												String servPrincipal[]=(forma.getServiciosPaquetes("principal_"+forma.getPaquetesResponsables("codpaquetizacion_"+i)+"")+"").split("_");
												logger.info("servPrincipal[2]--->"+servPrincipal[2]);
												logger.info("solSubcuentas.getServicioCX().getCodigo()--->"+solSubcuentas.getServicioCX().getCodigo());
												logger.info("servPrincipal[3]--->"+servPrincipal[3]);
												logger.info("solSubcuentas.getTipoAsocio().getCodigo()--->"+solSubcuentas.getTipoAsocio().getCodigo());
												/*
												 * 				
												 int detcxhonorarios=dtoTemp.getDetcxhonorarios();
												 int detasicxsalasmat=dtoTemp.getDetasicxsalasmat();
												 +"_"+detcxhonorarios+"_"+detasicxsalasmat

												 */

												boolean valSerCX=servPrincipal[2].equals(solSubcuentas.getServicioCX().getCodigo()+"")||servPrincipal[2].equals("-1");
												boolean valTipoAsocio=servPrincipal[3].equals(solSubcuentas.getTipoAsocio().getCodigo()+"")||servPrincipal[3].equals("-1");
												boolean valCxhon=servPrincipal[4].equals(solSubcuentas.getDetcxhonorarios()+"")||servPrincipal[4].equals("-1");
												boolean valAsCX=servPrincipal[5].equals(solSubcuentas.getDetasicxsalasmat()+"")||servPrincipal[5].equals("-1");
												if(solSubcuentas.getServicio().getCodigo().equals(servPrincipal[1])&&solSubcuentas.getNumeroSolicitud().equals(servPrincipal[0])&&valSerCX&&valTipoAsocio&&valCxhon&&valAsCX)
													detalle.put("principal_"+asignados,"S");
												else
													detalle.put("principal_"+asignados,"N");
												
												detalle.put("serviciocx_"+asignados, solSubcuentas.getServicioCX().getCodigo());
												detalle.put("tipoasocio_"+asignados,solSubcuentas.getTipoAsocio().getCodigo());
												detalle.put("detcxhonorarios_"+asignados,solSubcuentas.getDetcxhonorarios());
												detalle.put("detasicxsalasmat_"+asignados,solSubcuentas.getDetasicxsalasmat());
												detalle.put("usuario", usuario.getLoginUsuario());
												detalle.put("fecha", UtilidadFecha.getFechaActual());
												detalle.put("hora", UtilidadFecha.getHoraActual());
												asignados++;
											}
										}
									}
									if(asignados>0)
									{
										detalle.put("numRegistros",asignados+"");
										logger.info("\n\n\nMAPA A INSERTAR EN EL DETALLE\n\n\n");
										transaccion=mundo.guardarDetallePaquete(con,detalle);
									}
								}
							}
						}
					}
					//si no modificar los datos.
					//eliminar solo el detalle del paquete y generar nuevamente el detalle.
					else
					{
						logger.info("MODIFICANDO EL PAQUETE.................");
						HashMap detalle=new HashMap();
						detalle.put("numRegistros", "0");
						detalle.put("subcuenta", forma.getPaquetesResponsables("subcuenta_"+i)+"");
						detalle.put("numerosolicitud", forma.getPaquetesResponsables("solicitud_"+i)+""+"");
						int asignados=0;
						for(int j=0;j<forma.getResponsableCuenta().getSolicitudesSubcuenta().size();j++)
						{
							DtoSolicitudesSubCuenta solSubcuentas=forma.getResponsableCuenta().getSolicitudesSubcuenta().get(j);
							String servArti=solSubcuentas.getServicio().getCodigo().trim().equals("")?solSubcuentas.getArticulo().getCodigo():solSubcuentas.getServicio().getCodigo();
							
							String servicioCXTempo=solSubcuentas.getServicioCX().getCodigo();
							String tipoAsocioTempo=solSubcuentas.getTipoAsocio().getCodigo()>0?solSubcuentas.getTipoAsocio().getCodigo()+"":"-1";
							servicioCXTempo=UtilidadTexto.isEmpty(servicioCXTempo)?"-1":servicioCXTempo;
							int detcxhonorarios=solSubcuentas.getDetcxhonorarios();
							int detasicxsalasmat=solSubcuentas.getDetasicxsalasmat();
							
							if(forma.getServiciosPaquetes().containsKey("cantidad_"+j+"_"+servArti+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+forma.getPaquetesResponsables("codpaquetizacion_"+i)+"_"+detcxhonorarios+"_"+detasicxsalasmat))
							{
								int valor=convertirEntero(forma.getServiciosPaquetes("cantidad_"+j+"_"+servArti+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+forma.getPaquetesResponsables("codpaquetizacion_"+i)+"_"+detcxhonorarios+"_"+detasicxsalasmat)+"");
								if(valor>0)
								{
									asignados++;
								}
							}
						}
						//actualizar la distribucion de solicitudes como deben quedar actualmente.
						if(asignados>0)
						{
							///eliminar paquetizacion vieja.
							if(transaccion)
								transaccion=mundo.eliminarPaquetizacion(con,forma.getPaquetesResponsables("codpaquetizacion_"+i)+"",forma.getPaquetesResponsables("solicitud_"+i)+"",false);
		
							asignados=0;
							
							for(int j=0;j<forma.getResponsableCuenta().getSolicitudesSubcuenta().size();j++)
							{
								DtoSolicitudesSubCuenta solSubcuentas=forma.getResponsableCuenta().getSolicitudesSubcuenta().get(j);
								String servArti=solSubcuentas.getServicio().getCodigo().trim().equals("")?solSubcuentas.getArticulo().getCodigo():solSubcuentas.getServicio().getCodigo();
								String servicioCXTempo=solSubcuentas.getServicioCX().getCodigo();
								String tipoAsocioTempo=solSubcuentas.getTipoAsocio().getCodigo()>0?solSubcuentas.getTipoAsocio().getCodigo()+"":"-1";
								servicioCXTempo=UtilidadTexto.isEmpty(servicioCXTempo)?"-1":servicioCXTempo;
								int detcxhonorarios=solSubcuentas.getDetcxhonorarios();
								int detasicxsalasmat=solSubcuentas.getDetasicxsalasmat();
								
								if(forma.getServiciosPaquetes().containsKey("cantidad_"+j+"_"+servArti+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+forma.getPaquetesResponsables("codpaquetizacion_"+i)+"_"+detcxhonorarios+"_"+detasicxsalasmat))
								{
									int valor=convertirEntero(forma.getServiciosPaquetes("cantidad_"+j+"_"+servArti+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+forma.getPaquetesResponsables("codpaquetizacion_"+i)+"_"+detcxhonorarios+"_"+detasicxsalasmat)+"");
									if(valor>0)
									{
										
										detalle.put("codpaquetizacion_"+asignados, forma.getPaquetesResponsables("codpaquetizacion_"+i));
										detalle.put("numerosolicitud_"+asignados, solSubcuentas.getNumeroSolicitud());
										detalle.put("servicio_"+asignados,solSubcuentas.getServicio().getCodigo());
										detalle.put("articulo_"+asignados,solSubcuentas.getArticulo().getCodigo());
										detalle.put("cantidad_"+asignados, valor);
										
										String servPrincipal[]=(forma.getServiciosPaquetes("principal_"+forma.getPaquetesResponsables("codpaquetizacion_"+i)+"")+"").split("_");
										logger.info("servPrincipal[2]--->"+servPrincipal[2]);
										logger.info("solSubcuentas.getServicioCX().getCodigo()--->"+solSubcuentas.getServicioCX().getCodigo());
										logger.info("servPrincipal[3]--->"+servPrincipal[3]);
										logger.info("solSubcuentas.getTipoAsocio().getCodigo()--->"+solSubcuentas.getTipoAsocio().getCodigo());
										boolean valSerCX=servPrincipal[2].equals(solSubcuentas.getServicioCX().getCodigo()+"")||servPrincipal[2].equals("-1");
										boolean valTipoAsocio=servPrincipal[3].equals(solSubcuentas.getTipoAsocio().getCodigo()+"")||servPrincipal[3].equals("-1");
										boolean valCxhon=servPrincipal[4].equals(solSubcuentas.getDetcxhonorarios()+"")||servPrincipal[4].equals("-1");
										boolean valAsCX=servPrincipal[5].equals(solSubcuentas.getDetasicxsalasmat()+"")||servPrincipal[5].equals("-1");

										if(solSubcuentas.getServicio().getCodigo().equals(servPrincipal[1])&&solSubcuentas.getNumeroSolicitud().equals(servPrincipal[0])&&valSerCX&&valTipoAsocio&&valCxhon&&valAsCX)
											detalle.put("principal_"+asignados,"S");
										else
											detalle.put("principal_"+asignados,"N");
										
										detalle.put("serviciocx_"+asignados, solSubcuentas.getServicioCX().getCodigo());
										detalle.put("tipoasocio_"+asignados,solSubcuentas.getTipoAsocio().getCodigo());
										detalle.put("detcxhonorarios_"+asignados, solSubcuentas.getDetcxhonorarios());
										detalle.put("detasicxsalasmat_"+asignados,solSubcuentas.getDetasicxsalasmat());

										
										detalle.put("usuario", usuario.getLoginUsuario());
										detalle.put("fecha", UtilidadFecha.getFechaActual());
										detalle.put("hora", UtilidadFecha.getHoraActual());
		
										asignados++;
									}
								}
								
							}
							detalle.put("numRegistros",asignados+"");
							transaccion=mundo.guardarDetallePaquete(con,detalle);
						}
						else
						{
							///eliminar paquetizacion vieja.
							if(transaccion)
							{
								transaccion=mundo.eliminarPaquetizacion(con,forma.getPaquetesResponsables("codpaquetizacion_"+i)+"",forma.getPaquetesResponsables("solicitud_"+i)+"",true);
								Solicitud sol=new Solicitud();
								sol.setNumeroSolicitud(Utilidades.convertirAEntero(forma.getPaquetesResponsables("solicitud_"+i)+""));
								sol.setMotivoAnulacion("SE ELIMINO LA PAQUETIZACION");
								sol.setCodigoMedicoAnulacion(usuario.getCodigoPersona());
	
								try 
								{
									sol.anularSolicitudTransaccional(con, ConstantesBD.continuarTransaccion);
								} 
								catch (SQLException e) 
								{
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
		if(transaccion)
		{	
			
			logger.info("ACA DEBE DE SACAR EL MENSAJE DE ADVERTENCIA >>>>>");
			forma.setMensajeProceso(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}	
		else
		{	
			logger.info("ACA DEBE SACAR ELL MENSAJE DE QUE NO REALIZO EL PROCESO >>>>>> ");
			forma.setMensajeProceso(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}	
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param paciente 
	 * @param i
	 * @param servicioPaquete 
	 * @return
	 */
	private int generarSoliciutud(Connection con, PaquetizacionForm forma, PersonaBasica paciente, int i, String servicioPaquete, UsuarioBasico usuario) throws IPSException 
	{
		int consecutivoSol=ConstantesBD.codigoNuncaValido;
		Solicitud sol=new Solicitud();
		sol.clean();
		try 
		{
			sol.setFechaSolicitud(UtilidadFecha.getFechaActual());
			sol.setHoraSolicitud(UtilidadFecha.getHoraActual());
			sol.setTipoSolicitud(new InfoDatosInt(ConstantesBD.codigoTipoSolicitudPaquetes));
			sol.setCobrable(true);
			//sol.setNumeroAutorizacion("");
			sol.setEspecialidadSolicitada(new InfoDatosInt(ConstantesBD.codigoNuncaValido));
			sol.setOcupacionSolicitado(new InfoDatosInt(ConstantesBD.codigoNuncaValido));
			sol.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea()));
			sol.setCentroCostoSolicitado(new InfoDatosInt(convertirEntero(forma.getPaquetesResponsables("centrocosto_"+i)+"")));
			sol.setCodigoMedicoSolicitante(ConstantesBD.codigoNuncaValido);
			sol.setCodigoCuenta(paciente.getCodigoCuenta());
			sol.setVaAEpicrisis(false);
			sol.setUrgente(false);
			sol.setEstadoHistoriaClinica(new InfoDatosInt(ConstantesBD.codigoEstadoHCInterpretada));
			sol.setDatosMedico("");
			sol.setSolPYP(false);
			consecutivoSol=sol.insertarSolicitudTransaccional(con, "continuar");

			int conSolSubcuenta=ConstantesBD.codigoNuncaValido;
			///si se inserto la solicitud, insertar el registro en solicitudes sub cuenta
			if(consecutivoSol>0)
				conSolSubcuenta=this.insertarSolicitudSubCuenta(con,consecutivoSol,paciente.getCodigoCuenta(),Integer.parseInt(forma.getResponsableCuenta().getSubCuenta()),servicioPaquete,"",1,ConstantesBD.acronimoSi,usuario);
			
			//generar el cargo
			if(consecutivoSol>0&&conSolSubcuenta>0)
			{
				Cargos cargoServicio= new Cargos();
				cargoServicio.generarCargoServicio(			con, 
															false, 
															false, 
															consecutivoSol, 
															Integer.parseInt(Utilidades.obtenerViaIngresoCuenta(con,paciente.getCodigoCuenta()+"")), 
															forma.getResponsableCuenta().getContrato()/*codigoContrato*/, 
															Cuenta.obtenerTipoComplejidad(con, paciente.getCodigoCuenta()+"")/*codigoTipoComplejidad*/, 
															usuario.getCodigoInstitucionInt(), 
															"" /*observ*/, 
															usuario.getLoginUsuario(), 
															forma.getResponsableCuenta().getSubCuentaDouble(), 
															Double.parseDouble(conSolSubcuenta+""), 
															ConstantesBD.acronimoSi/*cubierto*/, 
															ConstantesBD.codigoNuncaValidoDouble/*codigoPadrePaquetes*/, 
															ConstantesBD.acronimoNo/*paquetizado*/, 
															1/*cantidadCargada*/, 
															forma.getInfoCobertura().getRequiereAutorizacionStr(),
															ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad /*tipoDistribucion*/,
															ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/, 
															Integer.parseInt(servicioPaquete) /*codigoServicioOPCIONAL*/, 
															ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
															forma.getResponsableCuenta().getConvenio().getCodigo() /*codigoConvenioOPCIONAL*/, 
															forma.getResponsableCuenta().getEsquemaTarifarioServiciosPpalOoriginal(con,forma.getResponsableCuenta().getSubCuenta(),forma.getResponsableCuenta().getContrato(),Integer.parseInt(servicioPaquete),"", Cargos.obtenerCentroAtencionCentroCostoSolicitadoCargo(sol.getCentroCostoSolicitado().getCodigo())) /*codigoEsquemaTarifarioOPCIONAL*/, 
															ConstantesBD.codigoTipoSolicitudPaquetes /*codigoTipoSolicitudOPCIONAL*/, 
															convertirEntero(forma.getPaquetesResponsables("centrocosto_"+i)+""),
															"",/* "" -- numeroAutorizacion*/
															-1 /*porcentajeDescuentoOPCIONAL*/,
															-1 /*double valorUnitarioDescuentoOPCIONAL*/,
															true /*esRegistroNuevo*/,
															true /*insertarRegistro*/,
															""/*esPortatil*/,
															false,"",
															0 /*porcentajeDctoPromocionServicio*/, 
															BigDecimal.ZERO /*valorDescuentoPromocionServicio*/, 
															0 /*porcentajeHonorarioPromocionServicio*/, 
															BigDecimal.ZERO /*valorHonorarioPromocionServicio*/,
															
															0/*programa*/,
															0/*porcentajeDctoBono*/,
															BigDecimal.ZERO/*valorDescuentoBono*/, 
															0/*porcentajeDctoOdontologico*/, 
															BigDecimal.ZERO/*valorDescuentoOdontologico*/,
															0 /*detallePaqueteOdonConvenio*/);
				
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return consecutivoSol;
	}


	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoCuenta
	 * @param codigoResponsable
	 * @param usuario 
	 * @param  
	 * @param temporalCodigoServicio
	 * @param string
	 * @param temporalCantidadServicio
	 * @return
	 */
	private int insertarSolicitudSubCuenta(Connection con, int numeroSolicitud, int codigoCuenta, int codigoResponsable, String codigoServicio, String codigoArticulo, int cantidad, String cubierto, UsuarioBasico usuario) throws IPSException 
	{
		DtoSolicitudesSubCuenta dto=new DtoSolicitudesSubCuenta();
		dto.setCuenta(codigoCuenta+"");
		dto.setSubCuenta(codigoResponsable+"");
		dto.setNumeroSolicitud(numeroSolicitud+"");
		dto.setServicio(new InfoDatosString(codigoServicio));
		dto.setArticulo(new InfoDatosString(codigoArticulo));
		dto.setCantidad(cantidad+"");
		dto.setCubierto(cubierto);
		dto.setTipoSolicitud(new InfoDatosInt(ConstantesBD.codigoTipoSolicitudPaquetes));
		dto.setPaquetizada(ConstantesBD.acronimoNo);
		dto.setTipoDistribucion(new InfoDatosString(ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad));
		dto.setUsuarioModifica(usuario.getLoginUsuario());
		int codSolSub=Solicitud.insertarSolicitudSubCuenta(con, dto, "continuar");
		return codSolSub;
	}
	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 */
	private void accionAsignarEventoPaquete(PaquetizacionForm forma, Paquetizacion mundo) 
	{
		for(int i=0;i<forma.getResponsableCuenta().getSolicitudesSubcuenta().size();i++)
		{
			//asignando lo que queda por evento al paquete que se esta liqudando.
			DtoSolicitudesSubCuenta solSubcuentas=forma.getResponsableCuenta().getSolicitudesSubcuenta().get(i);
			boolean repartido=false;
			if(!solSubcuentas.getTipoDistribucion().getCodigo().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad))
			{
				repartido=solSubcuentas.getNumResponsablesMismoArticulo()>1||solSubcuentas.getNumResponsablesMismoServicio()>1;
			}
			if(solSubcuentas.getNumResponsablesFacturadosMismoArticulo()==0&&solSubcuentas.getNumResponsablesFacturadosMismoServicio()==0&&!repartido)
			{
				String servArti=solSubcuentas.getServicio().getCodigo().trim().equals("")?solSubcuentas.getArticulo().getCodigo():solSubcuentas.getServicio().getCodigo();
				String servicioCXTempo=solSubcuentas.getServicioCX().getCodigo();
				String tipoAsocioTempo=solSubcuentas.getTipoAsocio().getCodigo()>0?solSubcuentas.getTipoAsocio().getCodigo()+"":"-1";
				int detcxhonorarios=solSubcuentas.getDetcxhonorarios();
				int detasicxsalasmat=solSubcuentas.getDetasicxsalasmat();

				servicioCXTempo=UtilidadTexto.isEmpty(servicioCXTempo)?"-1":servicioCXTempo;
				
				int cantidadEvento=this.obtenerCantidadEvento(forma,solSubcuentas,i,servArti,servicioCXTempo,tipoAsocioTempo,detcxhonorarios,detasicxsalasmat);
				forma.setServiciosPaquetes("cantidad_"+i+"_"+servArti+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+forma.getPaquetesResponsables("codpaquetizacion_"+forma.getIndicePaqueteLiquidacionAuto())+"_"+detcxhonorarios+"_"+detasicxsalasmat,cantidadEvento);
			}
		}
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 */
	private void accionLiquidacionAutomatica(Connection con, PaquetizacionForm forma, Paquetizacion mundo) 
	{
		HashMap componentesServicios=(HashMap)forma.getPaquetesResponsables("componentesServicios_"+forma.getIndicePaqueteLiquidacionAuto());
		HashMap componentesAgurpacionServicios=(HashMap)forma.getPaquetesResponsables("componentesAgurpacionServicios_"+forma.getIndicePaqueteLiquidacionAuto());
		HashMap componentesArticulos=(HashMap)forma.getPaquetesResponsables("componentesArticulos_"+forma.getIndicePaqueteLiquidacionAuto());
		HashMap componentesAgurpacionArticulos=(HashMap)forma.getPaquetesResponsables("componentesAgurpacionArticulos_"+forma.getIndicePaqueteLiquidacionAuto());

		///inciializar la cantidad disponeble para empeza a hacer la distribucion.
		this.inicializarCantidadesDisponibles(componentesServicios,componentesAgurpacionServicios,componentesArticulos,componentesAgurpacionArticulos);

		
		for(int i=0;i<forma.getResponsableCuenta().getSolicitudesSubcuenta().size();i++)
		{
			//asignando lo que queda por evento al paquete que se esta liqudando.
			DtoSolicitudesSubCuenta solSubcuentas=forma.getResponsableCuenta().getSolicitudesSubcuenta().get(i);
			boolean repartido=false;
			if(!solSubcuentas.getTipoDistribucion().getCodigo().equals(ConstantesIntegridadDominio.acronimoTipoDistribucionCantidad))
			{
				repartido=solSubcuentas.getNumResponsablesMismoArticulo()>1||solSubcuentas.getNumResponsablesMismoServicio()>1;
			}
			if(solSubcuentas.getNumResponsablesFacturadosMismoArticulo()==0&&solSubcuentas.getNumResponsablesFacturadosMismoServicio()==0&&!repartido)
			{
				if(asignarSolSubCuentaToPaquete(forma,solSubcuentas,componentesServicios))
				{
					String servArti=solSubcuentas.getServicio().getCodigo().trim().equals("")?solSubcuentas.getArticulo().getCodigo():solSubcuentas.getServicio().getCodigo();
					String servicioCXTempo=solSubcuentas.getServicioCX().getCodigo();
					String tipoAsocioTempo=solSubcuentas.getTipoAsocio().getCodigo()>0?solSubcuentas.getTipoAsocio().getCodigo()+"":"-1";
					int detcxhonorarios=solSubcuentas.getDetcxhonorarios();
					int detasicxsalasmat=solSubcuentas.getDetasicxsalasmat();

					servicioCXTempo=UtilidadTexto.isEmpty(servicioCXTempo)?"-1":servicioCXTempo;
					
					String servicioCirugia="";
					//para el caso de se asocio, se debe analizar al servicio de su cirugia
					if(solSubcuentas.getTipoSolicitud().getCodigo()==ConstantesBD.codigoTipoSolicitudCirugia&&!solSubcuentas.getServicioCX().getCodigo().trim().equals(""))
					{
						servicioCirugia=solSubcuentas.getServicioCX().getCodigo();
					}
					
					int cantidadEvento=this.obtenerCantidadEvento(forma,solSubcuentas,i,servArti,servicioCXTempo,tipoAsocioTempo,detcxhonorarios,detasicxsalasmat);
					int cantidadAsignar=0;
					
					//si el paquete no tiene componentes parametrizadas, se asigna todo lo que tiene por evento.
					if(Integer.parseInt(componentesServicios.get("numRegistros")+"")==0&&Integer.parseInt(componentesAgurpacionServicios.get("numRegistros")+"")==0&&Integer.parseInt(componentesArticulos.get("numRegistros")+"")==0&&Integer.parseInt(componentesAgurpacionArticulos.get("numRegistros")+"")==0)
					{
						cantidadAsignar=cantidadEvento;
					}
					/////si el paquete tiene asignado algun componente, toca hacer la validaciones de componentes
					else
					{
						///validaciones para servicio
						if(!solSubcuentas.getServicio().getCodigo().trim().equals(""))
						{
							//bandrea que indica si encontro el servicio en los componentes especificos
							boolean estaSE=false;
							//validar primer en los servicios específicos.
							for(int se=0;se<Integer.parseInt(componentesServicios.get("numRegistros")+"");se++)
							{
								String servicioCompara=servicioCirugia.trim().equals("")?servArti:servicioCirugia;
								if(servicioCompara.trim().equals((componentesServicios.get("codigoServicio_"+se)+"").trim()))
								{
									estaSE=true;
	
									String cantidadDisponible=componentesServicios.get("cantidaddisponible_"+se)+"";
									if(UtilidadTexto.isEmpty(cantidadDisponible))
									{
										cantidadAsignar=cantidadEvento;
									}
									else
									{
										if(cantidadEvento>Integer.parseInt(cantidadDisponible))
											cantidadAsignar=Integer.parseInt(cantidadDisponible);
										else
											cantidadAsignar=cantidadEvento;
	
										//si no es cirugia entonces se resta la cantidad disponible
										if(servicioCirugia.trim().equals(""))
											componentesServicios.put("cantidaddisponible_"+se,(Integer.parseInt(cantidadDisponible)-cantidadAsignar)+"");
										
									}
								}
								
							}
							if(!estaSE)
							{
								//cargar los parametros por el cual se filtrar el componente al cual se le adicionara el servicio.
								String servicioCompara=servicioCirugia.trim().equals("")?servArti:servicioCirugia;
								HashMap paramServicio=mundo.obtenerParametrosServicio(con,servicioCompara);
								
								int indiceApto=this.encontrarComponenteApropiadaServicio(componentesAgurpacionServicios,paramServicio);
								if(indiceApto==ConstantesBD.codigoNuncaValido)
								{
									cantidadAsignar=0;
								}
								else
								{
									String cantidadDisponible=componentesAgurpacionServicios.get("cantidaddisponible_"+indiceApto)+"";
									if(UtilidadTexto.isEmpty(cantidadDisponible))
									{
										cantidadAsignar=cantidadEvento;
									}
									else
									{
										if(cantidadEvento>Integer.parseInt(cantidadDisponible))
											cantidadAsignar=Integer.parseInt(cantidadDisponible);
										else
											cantidadAsignar=cantidadEvento;
	
										if(servicioCirugia.trim().equals(""))
											componentesAgurpacionServicios.put("cantidaddisponible_"+indiceApto,(Integer.parseInt(cantidadDisponible)-cantidadAsignar)+"");
									}
								}
							}
						}
						/////validaciones para articulos.
						else
						{
							//bandrea que indica si encontro el articulo en los componentes especificos
							boolean estaSE=false;
	
							//validar primer en los articulos específicos.
							for(int se=0;se<Integer.parseInt(componentesArticulos.get("numRegistros")+"");se++)
							{
								if(servArti.trim().equals((componentesArticulos.get("codigoArticulo_"+se)+"").trim()))
								{
									estaSE=true;
									String cantidadDisponible=componentesArticulos.get("cantidaddisponible_"+se)+"";
	
	
									if(UtilidadTexto.isEmpty(cantidadDisponible))
									{
										cantidadAsignar=cantidadEvento;
									}
									else
									{
										if(cantidadEvento>Integer.parseInt(cantidadDisponible))
											cantidadAsignar=Integer.parseInt(cantidadDisponible);
										else
											cantidadAsignar=cantidadEvento;
										
										componentesArticulos.put("cantidaddisponible_"+se,(Integer.parseInt(cantidadDisponible)-cantidadAsignar)+"");
									}
								}
								
							}
							if(!estaSE)
							{
								//cargar los parametros por el cual se filtrar el componente al cual se le adicionara el articulo
								HashMap paramArticulo=mundo.obtenerParametrosArticulos(con,servArti);
								int indiceApto=this.encontrarComponenteApropiadaArticulo(componentesAgurpacionArticulos,paramArticulo);
								if(indiceApto==ConstantesBD.codigoNuncaValido)
								{
									cantidadAsignar=0;
								}
								else
								{
									String cantidadDisponible=componentesAgurpacionArticulos.get("cantidaddisponible_"+indiceApto)+"";
									if(UtilidadTexto.isEmpty(cantidadDisponible))
									{
										cantidadAsignar=cantidadEvento;
									}
									else
									{
										if(cantidadEvento>Integer.parseInt(cantidadDisponible))
											cantidadAsignar=Integer.parseInt(cantidadDisponible);
										else
											cantidadAsignar=cantidadEvento;
	
										componentesAgurpacionArticulos.put("cantidaddisponible_"+indiceApto,(Integer.parseInt(cantidadDisponible)-cantidadAsignar)+"");
									}
								}
							}
						}
					}
					forma.setServiciosPaquetes("cantidad_"+i+"_"+servArti+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+forma.getPaquetesResponsables("codpaquetizacion_"+forma.getIndicePaqueteLiquidacionAuto())+"_"+detcxhonorarios+"_"+detasicxsalasmat,cantidadAsignar);
				}
			}
		}
	}

	/**
	 * 
	 * @param componentesAgurpacionArticulos
	 * @param paramArticulo
	 * @return  
	 */
	private int encontrarComponenteApropiadaArticulo(HashMap componentes, HashMap paramArticulo) 
	{
		boolean encontreSG=false, encontreG=false,encontreC=false,encontreN=false;
		int indice=ConstantesBD.codigoNuncaValido;

		logger.info("\n\n\n\n\n");
		for(int j=0;j<convertirEntero(componentes.get("numRegistros")+"");j++)
		{
			if(UtilidadTexto.isEmpty(componentes.get("cantidad_"+j)+"")||this.convertirEntero(componentes.get("cantidaddisponible_"+j)+"")>0)
			{
				if(!(UtilidadTexto.isEmpty((componentes.get("grupoespecial_"+j)+"").trim())))
				{
					for(int k=0;k<convertirEntero(paramArticulo.get("numRegistros")+"");k++)
					{	
						logger.info("------->"+UtilidadTexto.isEmpty(componentes.get("cantidad_"+j)+""));
						logger.info("------->"+UtilidadTexto.isEmpty(componentes.get("cantidaddisponible_"+j)+""));
						logger.info("---componentes----->"+componentes.get("grupoespecial_"+j));
						logger.info("---paramArticulo----->"+paramArticulo.get("grupoespecial_"+k));
						logger.info("------>"+componentes.get("cantidaddisponible_"+j));
						logger.info("------>"+this.convertirEntero(componentes.get("cantidaddisponible_"+j)+""));
						logger.info("------aaaaaaaaa---------");
						if(((componentes.get("grupoespecial_"+j)+"").trim().equals((paramArticulo.get("grupoespecial_"+k)+"").trim())))
						{
							return j;
						}
					}
				}
			}	
		}
		logger.info("indice -->"+indice);
		logger.info("\n\n\n\n\n");
		
		//si no lo encuentra en grupo especial.
		for(int j=0;j<convertirEntero(componentes.get("numRegistros")+"");j++)
		{
			//verificar que tenga cantidad disponible
			if(UtilidadTexto.isEmpty(componentes.get("cantidad_"+j)+"")||this.convertirEntero(componentes.get("cantidaddisponible_"+j)+"")>0)
			{
				if(UtilidadTexto.isEmpty((componentes.get("grupoespecial_"+j)+"").trim()))
				{
					if((componentes.get("subgrupo_"+j)+"").trim().equals((paramArticulo.get("subgrupo_0")+"").trim()))
					{
						//si es la primera vez que encuentro subgrupo, iniciliazar las otras banderas
						if(!encontreSG)
						{
							encontreG=false;
							encontreC=false;
							encontreN=false;
						}
						if(indice==ConstantesBD.codigoNuncaValido)
						{
							indice=j;
						}
						if((componentes.get("grupo_"+j)+"").trim().equals((paramArticulo.get("grupo_0")+"").trim()))
						{
							//si es la primiera vez que encunetro grupo, inicilizar las otras banderas.
							if(!encontreG)
							{
								encontreC=false;
								encontreN=false;
							}
							if(indice==ConstantesBD.codigoNuncaValido)
							{
								indice=j;
							}
							if((componentes.get("clase_"+j)+"").trim().equals((paramArticulo.get("clase_0")+"").trim()))
							{
								//si es la primiera vez que encuentr clas, inicilizar las otras banderas.
								if(!encontreC)
									encontreN=false;
								if(indice==ConstantesBD.codigoNuncaValido)
								{
									indice=j;
								}
								if((componentes.get("naturaleza_"+j)+"").trim().equals((paramArticulo.get("naturaleza_0")+"").trim()))
								{
									indice=j;
									return indice;
								}
								if(UtilidadTexto.isEmpty(componentes.get("naturaleza_"+j)+""))
								{
									indice=j;
								}
								encontreC=true;
							}
							else if(!encontreC&&UtilidadTexto.isEmpty(componentes.get("clase_"+j)+""))
							{
								if((componentes.get("naturaleza_"+j)+"").trim().equals((paramArticulo.get("naturaleza_0")+"").trim()))
								{
									indice=j;
									encontreN=true;
								}
								if(!encontreN&&UtilidadTexto.isEmpty(componentes.get("naturaleza_"+j)+""))
								{
									indice=j;
								}
							}
							encontreG=true;
						}
						else if(!encontreG&&UtilidadTexto.isEmpty(componentes.get("grupo_"+j)+""))
						{
							if((componentes.get("clase_"+j)+"").trim().equals((paramArticulo.get("clase_0")+"").trim()))
							{
								if(indice==ConstantesBD.codigoNuncaValido)
								{
									indice=j;
								}
								if((componentes.get("naturaleza_"+j)+"").trim().equals((paramArticulo.get("naturaleza_0")+"").trim()))
								{
									indice=j;
									encontreN=true;
								}
								if(!encontreN&&UtilidadTexto.isEmpty(componentes.get("naturaleza_"+j)+""))
								{
									indice=j;
								}
								encontreC=true;
							}
							else if(!encontreC&&UtilidadTexto.isEmpty(componentes.get("clase_"+j)+""))
							{
								if((componentes.get("naturaleza_"+j)+"").trim().equals((paramArticulo.get("naturaleza_0")+"").trim()))
								{
									indice=j;
									encontreN=true;
								}
								if(!encontreN&&UtilidadTexto.isEmpty(componentes.get("naturaleza_"+j)+""))
								{
									indice=j;
								}
							}
							
						}
						encontreSG=true;
					}
					else if(!encontreSG&&UtilidadTexto.isEmpty(componentes.get("subgrupo_"+j)+""))
					{
						/*
						if(indice==ConstantesBD.codigoNuncaValido)
						{
							indice=j;
						}
						*/
						if((componentes.get("grupo_"+j)+"").trim().equals((paramArticulo.get("grupo_0")+"").trim()))
						{
							//si es la primiera vez que encunetro grupo, inicilizar las otras banderas.
							if(!encontreG)
							{
								encontreC=false;
								encontreN=false;
							}
							if(indice==ConstantesBD.codigoNuncaValido)
							{
								indice=j;
							}
							if((componentes.get("clase_"+j)+"").trim().equals((paramArticulo.get("clase_0")+"").trim()))
							{
								//si es la primiera vez que encuentr clas, inicilizar las otras banderas.
								if(!encontreC)
									encontreN=false;
								if(indice==ConstantesBD.codigoNuncaValido)
								{
									indice=j;
								}
								if((componentes.get("naturaleza_"+j)+"").trim().equals((paramArticulo.get("naturaleza_0")+"").trim()))
								{
									indice=j;
									return indice;
								}
								if(UtilidadTexto.isEmpty(componentes.get("naturaleza_"+j)+""))
								{
									indice=j;
								}
								encontreC=true;
							}
							else if(!encontreC&&UtilidadTexto.isEmpty(componentes.get("clase_"+j)+""))
							{
								if((componentes.get("naturaleza_"+j)+"").trim().equals((paramArticulo.get("naturaleza_0")+"").trim()))
								{
									indice=j;
									encontreN=true;
								}
								if(!encontreN&&UtilidadTexto.isEmpty(componentes.get("naturaleza_"+j)+""))
								{
									indice=j;
								}
							}
							encontreG=true;
						}
						else if(!encontreG&&UtilidadTexto.isEmpty(componentes.get("grupo_"+j)+""))
						{
							if((componentes.get("clase_"+j)+"").trim().equals((paramArticulo.get("clase_0")+"").trim()))
							{
								if(indice==ConstantesBD.codigoNuncaValido)
								{
									indice=j;
								}
								if((componentes.get("naturaleza_"+j)+"").trim().equals((paramArticulo.get("naturaleza_0")+"").trim()))
								{
									indice=j;
									encontreN=true;
								}
								if(UtilidadTexto.isEmpty(componentes.get("naturaleza_"+j)+""))
								{
									indice=j;
								}
								encontreC=true;
							}
							else if(!encontreC&&UtilidadTexto.isEmpty(componentes.get("clase_"+j)+""))
							{
								if((componentes.get("naturaleza_"+j)+"").trim().equals((paramArticulo.get("naturaleza_0")+"").trim()))
								{
									indice=j;
									encontreN=true;
								}
								if(!encontreN&&UtilidadTexto.isEmpty(componentes.get("naturaleza_"+j)+""))
								{
									indice=j;
								}
							}
						}
					}
				}
			}
		}
		return indice;
	}
	

	/**
	 * Metodo que analiza las componentes que tiene el paquete en agrupacion de servicios, y retorna el indice apropiado.
	 * @param componentesServicios
	 * @param componentesAgurpacionServicios
	 * @param componentesArticulos
	 * @param componentesAgurpacionArticulos
	 * @param paramServicio 
	 * @return
	 */
	private int encontrarComponenteApropiadaServicio(HashMap componentes, HashMap paramServicio) 
	{
		boolean encontreE=false, encontreTS=false,encontreG=false;
		int indice=ConstantesBD.codigoNuncaValido;
		for(int j=0;j<convertirEntero(componentes.get("numRegistros")+"");j++)
		{
			//verificar que tenga cantidad disponible
			if(UtilidadTexto.isEmpty(componentes.get("cantidad_"+j)+"")||this.convertirEntero(componentes.get("cantidaddisponible_"+j)+"")>0)
			{
				if((componentes.get("especialidad_"+j)+"").trim().equals((paramServicio.get("especialidad")+"").trim()))
				{
					//si es la primera vez que encuentro 
					if(!encontreE)
					{
						encontreTS=false;
						encontreG=false;
					}
					if(indice==ConstantesBD.codigoNuncaValido)
					{
						indice=j;
					}

					if((componentes.get("tiposervicio_"+j)+"").trim().equals((paramServicio.get("tipoServicio")+"").trim()))
					{
						//si es la primiera vez que encunetro
						if(!encontreTS)
						{
							encontreG=false;
						}
						if(indice==ConstantesBD.codigoNuncaValido)
						{
							indice=j;
						}
						if((componentes.get("gruposervicio_"+j)+"").trim().equals((paramServicio.get("grupoServicio")+"").trim()))
						{
							indice=j;
							return indice;
						}
						else if(UtilidadTexto.isEmpty(componentes.get("gruposervicio_"+j)+""))
						{
							indice=j;
						}
						encontreTS=true;
					}
					else if(!encontreTS&&UtilidadTexto.isEmpty(componentes.get("tiposervicio_"+j)+""))
					{
						if((componentes.get("gruposervicio_"+j)+"").trim().equals((paramServicio.get("grupoServicio")+"").trim()))
						{
							indice=j;
							encontreG=true;
						}
						if(!encontreG&&UtilidadTexto.isEmpty(componentes.get("gruposervicio_"+j)+""))
						{
							indice=j;
						}
					}
					encontreE=true;
				}
				else if(!encontreE&&UtilidadTexto.isEmpty(componentes.get("especialidad_"+j)+""))
				{

					/*
					if(indice==ConstantesBD.codigoNuncaValido)
					{
						indice=j;
					}
					*/
					if((componentes.get("tiposervicio_"+j)+"").trim().equals((paramServicio.get("tipoServicio")+"").trim()))
					{

						//si es la primiera vez que encunetro
						if(!encontreTS)
						{
							encontreG=false;
						}
						if(indice==ConstantesBD.codigoNuncaValido)
						{
							indice=j;
						}
						if((componentes.get("gruposervicio_"+j)+"").trim().equals((paramServicio.get("grupoServicio")+"").trim()))
						{
							indice=j;
							return indice;
						}
						else if(UtilidadTexto.isEmpty(componentes.get("gruposervicio_"+j)+""))
						{
							indice=j;
						}
						encontreTS=true;
					}
					else if(!encontreTS&&UtilidadTexto.isEmpty(componentes.get("tiposervicio_"+j)+""))
					{

						if((componentes.get("gruposervicio_"+j)+"").trim().equals((paramServicio.get("grupoServicio")+"").trim()))
						{
							indice=j;
							encontreG=true;
						}
						if(!encontreG&&UtilidadTexto.isEmpty(componentes.get("gruposervicio_"+j)+""))
						{
							indice=j;
						}
					}
					//encontreE=false;
				}
			}
		}
		return indice;
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	private int convertirEntero(String valor) 
	{
		if(UtilidadTexto.isEmpty(valor))
			return 0;
		else
			return Integer.parseInt(valor);
	}

	/**
	 * Metod que me indica si puedo asignar un registro a un paquete despues de aplicar los filtros.
	 * @param forma
	 * @param solSubcuentas
	 * @param servicios 
	 * @return
	 */
	private boolean asignarSolSubCuentaToPaquete(PaquetizacionForm forma, DtoSolicitudesSubCuenta solSubcuentas, HashMap servicios) 
	{
		String fIncial=forma.getPaquetesResponsables("fechainisolfiltro_"+forma.getIndicePaqueteLiquidacionAuto())+"";
		String fFinal=forma.getPaquetesResponsables("fechafinsolfiltro_"+forma.getIndicePaqueteLiquidacionAuto())+"";
		String ccEjecuta=forma.getPaquetesResponsables("ccejecutafiltro_"+forma.getIndicePaqueteLiquidacionAuto())+"";
		String ccSolicita=forma.getPaquetesResponsables("ccsolicitafiltro_"+forma.getIndicePaqueteLiquidacionAuto())+"";
		
		
		boolean asignar=true;
		
		/////validar filtro rango de las solitudes.
		if(!fIncial.trim().equals("")&&!fFinal.trim().equals(""))
		{
			if(UtilidadFecha.validarFechaRango(fIncial, fFinal, solSubcuentas.getFechaSolicitud()))
			{
				asignar=true;
			}
			else
			{
				asignar=false;
			}
		}
		
		///validar que la solicitud cumpa con el fintro de centro de costo que ejecuta
		if(asignar && !ccEjecuta.trim().equals(""))
		{
			if(Integer.parseInt(ccEjecuta)==solSubcuentas.getCentroCostoEjecuta().getCodigo())
			{
				asignar=true;
			}
			else
			{
				asignar=false;
			}
		}
		
		///validar que la solicitud cumpla con el filtro de centro de costo que solita.
		if(asignar && !ccSolicita.trim().equals(""))
		{
			if(Integer.parseInt(ccSolicita)==solSubcuentas.getCentroCostoSolicita().getCodigo())
			{
				asignar=true;
			}
			else
			{
				asignar=false;
			}
		}
		//si no cumple validacion de filtros, mirar si es principal.
		if(!asignar)
		{
			ArrayList<String> serviciosPrincipales=this.obtenerServiciosPrincipales(servicios);
			if(Integer.parseInt(servicios.get("numRegistros")+"")>0)
			{
				//si tiene servicios principales, verificar que se encuentre minimo uno en los servicios de la solicitud.
				for(int j=0;j<serviciosPrincipales.size();j++)
				{
					if(serviciosPrincipales.get(j).trim().equals(solSubcuentas.getServicio().getCodigo())||serviciosPrincipales.get(j).trim().equals(solSubcuentas.getServicioCX().getCodigo()))
					{
						asignar=true;
						j=serviciosPrincipales.size();
					}
				}
			}
		}
		return asignar;
	}

	/**
	 * Metoto que inicializa las cantidades disponebles de canda componente.
	 * @param componentesServicios
	 * @param componentesAgurpacionServicios
	 * @param componentesArticulos
	 * @param componentesAgurpacionArticulos
	 */
	private void inicializarCantidadesDisponibles(HashMap componentesServicios, HashMap componentesAgurpacionServicios, HashMap componentesArticulos, HashMap componentesAgurpacionArticulos) 
	{
		for(int i=0;i<Integer.parseInt(componentesServicios.get("numRegistros")+"");i++)
		{
			componentesServicios.put("cantidaddisponible_"+i, componentesServicios.get("cantidad_"+i));
		}
		for(int i=0;i<Integer.parseInt(componentesAgurpacionServicios.get("numRegistros")+"");i++)
		{
			componentesAgurpacionServicios.put("cantidaddisponible_"+i, componentesAgurpacionServicios.get("cantidad_"+i));
		}
		for(int i=0;i<Integer.parseInt(componentesArticulos.get("numRegistros")+"");i++)
		{
			componentesArticulos.put("cantidaddisponible_"+i, componentesArticulos.get("cantidad_"+i));
		}
		for(int i=0;i<Integer.parseInt(componentesAgurpacionArticulos.get("numRegistros")+"");i++)
		{
			componentesAgurpacionArticulos.put("cantidaddisponible_"+i, componentesAgurpacionArticulos.get("cantidad_"+i));
		}
	}

	/**
	 * Metodo que retorna la cantidad por evento, que aun queda en un servicio-articulo.
	 * @param forma
	 * @param i 
	 * @param solSubcuentas 
	 * @param servArti 
	 * @param tipoAsocioTempo 
	 * @param servicioCXTempo 
	 * @param detasicxsalasmat 
	 * @param detcxhonorarios 
	 * @return
	 */
	private int obtenerCantidadEvento(PaquetizacionForm forma, DtoSolicitudesSubCuenta solSubcuentas, int i, String servArti, String servicioCXTempo, String tipoAsocioTempo, int detcxhonorarios, int detasicxsalasmat) 
	{
		int cantidad=0;
		int cantidadActual=0;
		try
		{
			cantidad=Integer.parseInt(forma.getServiciosPaquetes("evento_"+i+"_"+servArti+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+detcxhonorarios+"_"+detasicxsalasmat)+"");
	
		}
		catch (Exception e) 
		{
			cantidad=0;
		}
		try
		{
			cantidadActual=Integer.parseInt(forma.getServiciosPaquetes("cantidad_"+i+"_"+servArti+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+forma.getPaquetesResponsables("codpaquetizacion_"+forma.getIndicePaqueteLiquidacionAuto())+"_"+detcxhonorarios+"_"+detasicxsalasmat)+"");
		}
		catch (Exception e) 
		{
			cantidadActual=0;
		}
		//sumar las quetenia antes asignadas el paquete a la cantidad que se obtuvo del evento.
		cantidad=cantidad+cantidadActual;
		if(cantidad<0)
			cantidad=0;
		return cantidad;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 */
	private ActionForward accionAdicionarPaquete(Connection con, PaquetizacionForm forma, ActionMapping mapping) 
	{
		if(forma.getNumPaquetesMemoria()>1)
		{
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
		int posInsertar=Integer.parseInt(forma.getPaquetesResponsables("numRegistros")+"");
		forma.setPaquetesResponsables("codpaquetizacion_"+posInsertar, ConstantesBD.codigoNuncaValido+"");
		forma.setPaquetesResponsables("subcuenta_"+posInsertar,forma.getResponsableCuenta().getSubCuenta());
		forma.setPaquetesResponsables("solicitud_"+posInsertar, ConstantesBD.codigoNuncaValido+"");
		forma.setPaquetesResponsables("codpaqueteconvenio_"+posInsertar,forma.getPaquetesValidosConvenio("codigo_"+forma.getIndicePaqueteSeleccionado()));
		forma.setPaquetesResponsables("convenio_"+posInsertar,forma.getPaquetesValidosConvenio("convenio_"+forma.getIndicePaqueteSeleccionado()));
		forma.setPaquetesResponsables("contrato_"+posInsertar,forma.getPaquetesValidosConvenio("contrato_"+forma.getIndicePaqueteSeleccionado()));
		forma.setPaquetesResponsables("codpaquete_"+posInsertar,forma.getPaquetesValidosConvenio("paquete_"+forma.getIndicePaqueteSeleccionado()));
		forma.setPaquetesResponsables("nompaquete_"+posInsertar,forma.getPaquetesValidosConvenio("descpaquete_"+forma.getIndicePaqueteSeleccionado()));
		forma.setPaquetesResponsables("codviaingreso_"+posInsertar,forma.getPaquetesValidosConvenio("viaingreso_"+forma.getIndicePaqueteSeleccionado()));
		forma.setPaquetesResponsables("nomviaingreso_"+posInsertar,forma.getPaquetesValidosConvenio("nomviaingreso_"+forma.getIndicePaqueteSeleccionado()));
		forma.setPaquetesResponsables("fechainicialven_"+posInsertar,forma.getPaquetesValidosConvenio("fechainicialvenc_"+forma.getIndicePaqueteSeleccionado()));
		forma.setPaquetesResponsables("fechafinalven_"+posInsertar,forma.getPaquetesValidosConvenio("fechafinalvenc_"+forma.getIndicePaqueteSeleccionado()));
		forma.setPaquetesResponsables("serviciopaquete_"+posInsertar,forma.getPaquetesValidosConvenio("codserviciopaquete_"+forma.getIndicePaqueteSeleccionado()));
		forma.setPaquetesResponsables("nomserviciopaquete_"+posInsertar,forma.getPaquetesValidosConvenio("nomserviciopaquete_"+forma.getIndicePaqueteSeleccionado()));
		forma.setPaquetesResponsables("numrespaquete_"+posInsertar,"1");
		forma.setPaquetesResponsables("numresfacpaquete_"+posInsertar,"0");
		forma.setPaquetesResponsables("componentesServicios_"+posInsertar,forma.getPaquetesValidosConvenio("componentesServicios_"+forma.getIndicePaqueteSeleccionado()));
		forma.setPaquetesResponsables("componentesAgurpacionServicios_"+posInsertar,forma.getPaquetesValidosConvenio("componentesAgurpacionServicios_"+forma.getIndicePaqueteSeleccionado()));
		forma.setPaquetesResponsables("componentesArticulos_"+posInsertar,forma.getPaquetesValidosConvenio("componentesArticulos_"+forma.getIndicePaqueteSeleccionado()));
		forma.setPaquetesResponsables("componentesAgurpacionArticulos_"+posInsertar,forma.getPaquetesValidosConvenio("componentesAgurpacionArticulos_"+forma.getIndicePaqueteSeleccionado()));
		forma.setPaquetesResponsables("centrocosto_"+posInsertar,forma.getCentroCosto());
		forma.setPaquetesResponsables("ccsolicitafiltro_"+posInsertar,forma.getCentroCostoSolicita());
		forma.setPaquetesResponsables("ccejecutafiltro_"+posInsertar,forma.getCentroCostoEjecuta());
		forma.setPaquetesResponsables("fechainisolfiltro_"+posInsertar,forma.getFechaInicial());
		forma.setPaquetesResponsables("fechafinsolfiltro_"+posInsertar,forma.getFechaFinal());
		forma.setPaquetesResponsables("facturado_"+posInsertar,ConstantesBD.acronimoNo);
		forma.setPaquetesResponsables("tiporegistro_"+posInsertar,"MEM");
		forma.setPaquetesResponsables("numRegistros",(posInsertar+1)+"");
		
		for(int j=0;j<forma.getResponsableCuenta().getSolicitudesSubcuenta().size();j++)
		{
			DtoSolicitudesSubCuenta solSubcuentas=forma.getResponsableCuenta().getSolicitudesSubcuenta().get(j);
			String servArti=solSubcuentas.getServicio().getCodigo().trim().equals("")?solSubcuentas.getArticulo().getCodigo():solSubcuentas.getServicio().getCodigo();
			String servicioCXTempo=solSubcuentas.getServicioCX().getCodigo();
			String tipoAsocioTempo=solSubcuentas.getTipoAsocio().getCodigo()>0?solSubcuentas.getTipoAsocio().getCodigo()+"":"-1";
			servicioCXTempo=UtilidadTexto.isEmpty(servicioCXTempo)?"-1":servicioCXTempo;
			int detcxhonorarios=solSubcuentas.getDetcxhonorarios();
			int detasicxsalasmat=solSubcuentas.getDetasicxsalasmat();

			forma.setServiciosPaquetes("cantidad_"+j+"_"+servArti+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+forma.getPaquetesResponsables("codpaquetizacion_"+posInsertar)+"_"+detcxhonorarios+"_"+detasicxsalasmat,"0");
			forma.setServiciosPaquetes("principal_"+j+"_"+servArti+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+forma.getPaquetesResponsables("codpaquetizacion_"+posInsertar)+"_"+detcxhonorarios+"_"+detasicxsalasmat,"");
		}


		
		forma.setNumPaquetesMemoria(1);

		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");

	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente 
	 * @param usuario 
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, PaquetizacionForm forma, Paquetizacion mundo, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping) throws IPSException 
	{
		forma.reset();				 
		forma.setResponsables(UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con,paciente.getCodigoIngreso(),true,new String[0],false,"" /*subCuenta*/,ConstantesBD.codigoNuncaValido /*Via de ingreso*/));
		forma.setCuentaProcesoDistribucion(UtilidadValidacion.ingresoEstaEnProcesoDistribucion(con,paciente.getCodigoIngreso(),usuario.getLoginUsuario()));
		logger.info("===>Cuenta En Procesco Distribución 2: "+forma.isCuentaProcesoDistribucion());
		/*****************************************************************************************************************/
		ArrayList<String> aux = new ArrayList<String>();
        //se valida que el paciente es de entidad subcontratada
		logger.info("\n VOY A ENTRAR A ENTIDAD !!!");
	    if (IngresoGeneral.esIngresoComoEntidadSubContratada(con, paciente.getCodigoIngreso()).equals(ConstantesBD.acronimoSi))
	    {
	    	logger.info("\n YA ENTRE A ENTIDAD !!!");
	    	aux.add("Ingreso de paciente en entidad subcontratada "+EntidadesSubContratadas.getDescripcionEntidadSubXIngreso(con, paciente.getCodigoIngreso()+""));
	    	forma.setMensajes(aux);
	    	logger.info("\n VOY A SALIR DE ENTIDAD !!!");
	    }
	    /*****************************************************************************************************************/
		if(forma.getResponsables().size()==1)
		{
			//posturar el unico responsable.
			forma.setIndexResponSeleccionado(0);
			return this.accionCargarInfo(con, forma, mundo, mapping,paciente, usuario);
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionCargarInfo(Connection con, PaquetizacionForm forma, Paquetizacion mundo, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario) throws IPSException 
	{
		//cargamos el responsable seleccionado
		forma.setResponsableCuenta(forma.getResponsables().get(forma.getIndexResponSeleccionado()));
		
		//cargamos las solicitudes(serv-arti) del responsable
		forma.getResponsableCuenta().setSolicitudesSubcuenta(mundo.obtenerSolicitudesSubCuentaPaquetizar(con,forma.getResponsableCuenta().getSubCuenta()));
		
		//cargamos los paquetes que ya tiene el responsable
		forma.setPaquetesResponsables(mundo.consultarPaquetesAsocioadosResponsableSubcuenta(con,forma.getResponsableCuenta().getSubCuenta()));

		///cargamos los servicios, de cada paquete, toca cargarlos a un mapa aparte para poder modificarlos desde el jsp,
		///ya que no se puden enviar objetos, por etiquietas html, no se puede manejar un mapa entre otro mapa, y modificarlo.
		//////cargarlos bien en el mapa
		/////////asignar los servicios.
		if(Integer.parseInt(forma.getPaquetesResponsables().get("numRegistros")+"")>0)
		{
			String[] codigoPaquetizaciones=new String[Integer.parseInt(forma.getPaquetesResponsables().get("numRegistros")+"")];
			for(int i=0;i<codigoPaquetizaciones.length;i++)
			{
				codigoPaquetizaciones[i]=forma.getPaquetesResponsables().get("codpaquetizacion_"+i)+"";
			}
			
			HashMap mapaSerTemp=mundo.consultarServiciosPaquetes(con,codigoPaquetizaciones);
			for(int i=0;i<forma.getResponsableCuenta().getSolicitudesSubcuenta().size();i++)
			{
				DtoSolicitudesSubCuenta dtoTemp=forma.getResponsableCuenta().getSolicitudesSubcuenta().get(i);
				String servArti=dtoTemp.getServicio().getCodigo().trim().equals("")?dtoTemp.getArticulo().getCodigo():dtoTemp.getServicio().getCodigo();
				String servicioCXTempo=dtoTemp.getServicioCX().getCodigo();
				String tipoAsocioTempo=dtoTemp.getTipoAsocio().getCodigo()>0?dtoTemp.getTipoAsocio().getCodigo()+"":"-1";
				servicioCXTempo=UtilidadTexto.isEmpty(servicioCXTempo)?"-1":servicioCXTempo;
				int detcxhonorarios=dtoTemp.getDetcxhonorarios();
				int detasicxsalasmat=dtoTemp.getDetasicxsalasmat();
				
				logger.info("---->"+detcxhonorarios+"*******"+detasicxsalasmat);
				for(int j=0;j<codigoPaquetizaciones.length;j++)
				{
					//String valor=""
					String indice=servArti+"_"+servicioCXTempo+"_"+tipoAsocioTempo+"_"+codigoPaquetizaciones[j]+"_"+detcxhonorarios+"_"+detasicxsalasmat;
					
					if(UtilidadTexto.getBoolean(mapaSerTemp.get("principal_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+""))
					{
						String servicioCXTemp=mapaSerTemp.get("serviciocx_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"";
						String tipoAsocioTemp=mapaSerTemp.get("tipoasocio_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"";
						servicioCXTemp=UtilidadTexto.isEmpty(servicioCXTemp)?"-1":servicioCXTemp;
						tipoAsocioTemp=(UtilidadTexto.isEmpty(tipoAsocioTemp)||Utilidades.convertirAEntero(tipoAsocioTemp)<=0)?"-1":tipoAsocioTemp;
						forma.setServiciosPaquetes("principal_"+codigoPaquetizaciones[j],mapaSerTemp.get("solicitud_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"_"+mapaSerTemp.get("servicio_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"_"+servicioCXTemp+"_"+tipoAsocioTemp+"_"+detcxhonorarios+"_"+detasicxsalasmat);
					}
					forma.setServiciosPaquetes("codigo_"+i+"_"+indice,mapaSerTemp.get("codigo_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
					forma.setServiciosPaquetes("codpaquetizacion_"+i+"_"+indice,mapaSerTemp.get("codpaquetizacion_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
					forma.setServiciosPaquetes("solicitud_"+i+"_"+indice,mapaSerTemp.get("solicitud_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
					forma.setServiciosPaquetes("servicio_"+i+"_"+indice,mapaSerTemp.get("servicio_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
					forma.setServiciosPaquetes("articulo_"+i+"_"+indice,mapaSerTemp.get("articulo_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
					forma.setServiciosPaquetes("cantidad_"+i+"_"+indice,convertirEntero(mapaSerTemp.get("cantidad_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"")+"");
					forma.setServiciosPaquetes("serviciocx_"+i+"_"+indice,mapaSerTemp.get("serviciocx_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
					forma.setServiciosPaquetes("tipoasocio_"+i+"_"+indice,mapaSerTemp.get("tipoasocio_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
					forma.setServiciosPaquetes("nomtipoasocio_"+i+"_"+indice,mapaSerTemp.get("nomtipoasocio_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
					forma.setServiciosPaquetes("detcxhonorarios_"+i+"_"+indice,mapaSerTemp.get("detasicxsalasmat_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
					forma.setServiciosPaquetes("detasicxsalasmat_"+i+"_"+indice,mapaSerTemp.get("detasicxsalasmat_"+dtoTemp.getNumeroSolicitud()+"_"+indice)+"");
				}
			}
			forma.setServiciosPaquetes("numRegistros",forma.getResponsableCuenta().getSolicitudesSubcuenta().size());
			
		}
		
		forma.setPaquetesValidosConvenio(this.obtenerPaquetesValidosConvenio(con,forma,mundo,paciente, usuario));
		
		forma.setMostrarMensaje(new ResultadoBoolean(UtilidadValidacion.responsableTieneServicioEstado(con,forma.getResponsableCuenta().getSubCuenta(),ConstantesBD.codigoEstadoFPendiente),"El responsable tiene solicitudes con cargos en estado Pendiente."));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Metodo que retorna el mapa de paquetes validos seleccionado para el convenio, aplicando todas las validaciones.
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @return
	 */
	private HashMap<String, Object> obtenerPaquetesValidosConvenio(Connection con, PaquetizacionForm forma, Paquetizacion mundo, PersonaBasica paciente, UsuarioBasico usuario) throws IPSException 
	{
		
		//si el paciente no tiene egreso el filtro se hace con la fecha actual, si tiene egreso se toma la fecha de egreso
		String fechaFiltro=UtilidadFecha.getFechaActual();
		try 
		{	String fechaTemp = "";
			if(UtilidadValidacion.existeEgresoCompleto(con, paciente.getCodigoCuenta()))
				fechaTemp=Utilidades.obtenerFechaEgreso(paciente.getCodigoCuenta()+"");
			
			if(!UtilidadTexto.isEmpty(fechaTemp))
				fechaFiltro = fechaTemp;
		} 
		catch (SQLException e) 
		{
			fechaFiltro=UtilidadFecha.getFechaActual();			
		}
		
		//cargar los paquetes validos para el convenio-responsable seleccionado
		//indices--->  codigo_,institucion_,convenio_,contrato_,paquete_,descpaquete_,codserviciopaquete_,codigogruposer_,nomserviciopaquete_,viaingreso_,fechainicialvenc_,fechafinalvenc_
		HashMap<String, Object> paquetesValidos=new HashMap<String, Object>();
		int contadorPaquetesValidos=0;
		
		HashMap mapaPaquetesValidosFiltro=UtilidadesFacturacion.obtenerPaquetesValidosResponsable(con,forma.getResponsableCuenta().getConvenio().getCodigo(),Utilidades.obtenerViaIngresoCuenta(con,paciente.getCodigoCuenta()+""),fechaFiltro);
		
		//validaciones adicionales.
		ComponentesPaquetes mundoPaquetes=new ComponentesPaquetes();
		for(int i=0;i<Integer.parseInt(mapaPaquetesValidosFiltro.get("numRegistros")+"");i++)
		{
			HashMap servicios=mundoPaquetes.consultarServiciosPaquete(con,mapaPaquetesValidosFiltro.get("paquete_"+i)+"", Integer.parseInt(mapaPaquetesValidosFiltro.get("institucion_"+i)+""));
			//validar que el si tiene servicio principal, este entre los servicios del responsable.
			ArrayList<String> serviciosPrincipales=this.obtenerServiciosPrincipales(servicios);
			boolean valido=true;
			if(Integer.parseInt(servicios.get("numRegistros")+"")>0)
			{
				//si tiene servicios principales, verificar que se encuentre minimo uno en los servicios de la solicitud.
				if(serviciosPrincipales.size()>0)
					valido=false;
				for(int j=0;j<serviciosPrincipales.size();j++)
				{
					for(int k=0;k<forma.getResponsableCuenta().getSolicitudesSubcuenta().size();k++)
					{
						DtoSolicitudesSubCuenta solTemporal=forma.getResponsableCuenta().getSolicitudesSubcuenta().get(k);
						if(serviciosPrincipales.get(j).trim().equals(solTemporal.getServicio().getCodigo())||serviciosPrincipales.get(j).trim().equals(solTemporal.getServicioCX().getCodigo()))
						{
							//lo encontro en los principales, entonces hace valido=true, y sale de los ciclos para poderlos asignar.
							valido=true;
							k=forma.getResponsableCuenta().getSolicitudesSubcuenta().size();
							j=serviciosPrincipales.size();
						}
					}
				}
			}
			logger.info("paquete "+mapaPaquetesValidosFiltro.get("paquete_"+i)+" Valido -->"+valido);
			String servicioPaq=mapaPaquetesValidosFiltro.get("codserviciopaquete_"+i)+"";

			//Se modifico según la Tarea 49987 
			//forma.setInfoCobertura(Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", Integer.parseInt(Utilidades.obtenerViaIngresoCuenta(con,paciente.getCodigoCuenta()+"")), paciente.getCodigoTipoPaciente(), Integer.parseInt(servicioPaq), Integer.parseInt( mapaPaquetesValidosFiltro.get("institucion_"+i)+""), false, "" /*subCuentaCoberturaOPCIONAL*/).getInfoCobertura());
			forma.setInfoCobertura(Cobertura.validacionCoberturaServicioDadoResponsable(con, forma.getResponsableCuenta(), Integer.parseInt(Utilidades.obtenerViaIngresoCuenta(con,paciente.getCodigoCuenta()+"")), paciente.getCodigoTipoPaciente(), Integer.parseInt(servicioPaq), Integer.parseInt( mapaPaquetesValidosFiltro.get("institucion_"+i)+"")));
			
			valido=forma.getInfoCobertura().getIncluido();
			logger.info("Validado cobertura: "+valido);
			//como el paquete que estamos analizando es valido lo asignamos y consultamos los demas componentes del paquete.
			if(valido)
			{
				paquetesValidos.put("codigo_"+contadorPaquetesValidos, mapaPaquetesValidosFiltro.get("codigo_"+i));
				paquetesValidos.put("institucion_"+contadorPaquetesValidos, mapaPaquetesValidosFiltro.get("institucion_"+i));
				paquetesValidos.put("convenio_"+contadorPaquetesValidos, mapaPaquetesValidosFiltro.get("convenio_"+i));
				paquetesValidos.put("contrato_"+contadorPaquetesValidos, mapaPaquetesValidosFiltro.get("contrato_"+i));
				paquetesValidos.put("paquete_"+contadorPaquetesValidos, mapaPaquetesValidosFiltro.get("paquete_"+i));
				paquetesValidos.put("descpaquete_"+contadorPaquetesValidos, mapaPaquetesValidosFiltro.get("descpaquete_"+i));
				paquetesValidos.put("codserviciopaquete_"+contadorPaquetesValidos, servicioPaq);
				paquetesValidos.put("tarifaserviciopaquete_"+contadorPaquetesValidos,UtilidadesFacturacion.obtenerTarfiaServicio(con,forma.getResponsableCuenta().getEsquemaTarifarioServiciosPpalOoriginal(con,forma.getResponsableCuenta().getSubCuenta(),forma.getResponsableCuenta().getContrato(),Integer.parseInt(servicioPaq),"",  usuario.getCodigoCentroAtencion()),Integer.parseInt(servicioPaq), /*segun Armando es la fecha actual entonces la podemos enviar vacia*/ ""));
				paquetesValidos.put("codigogruposer_"+contadorPaquetesValidos, mapaPaquetesValidosFiltro.get("codigogruposer_"+i));
				paquetesValidos.put("nomserviciopaquete_"+contadorPaquetesValidos, mapaPaquetesValidosFiltro.get("nomserviciopaquete_"+i));
				paquetesValidos.put("viaingreso_"+contadorPaquetesValidos, mapaPaquetesValidosFiltro.get("viaingreso_"+i));
				paquetesValidos.put("fechainicialvenc_"+contadorPaquetesValidos, mapaPaquetesValidosFiltro.get("fechainicialvenc_"+i));
				paquetesValidos.put("fechafinalvenc_"+contadorPaquetesValidos, mapaPaquetesValidosFiltro.get("fechafinalvenc_"+i));
				paquetesValidos.put("componentesServicios_"+contadorPaquetesValidos, servicios);
				paquetesValidos.put("componentesAgurpacionServicios_"+contadorPaquetesValidos,mundoPaquetes.consultarAgrupacionServiciosPaquete(con,mapaPaquetesValidosFiltro.get("paquete_"+i)+"", Integer.parseInt(mapaPaquetesValidosFiltro.get("institucion_"+i)+"")));
				paquetesValidos.put("componentesArticulos_"+contadorPaquetesValidos, mundoPaquetes.consultarArticulosPaquete(con,mapaPaquetesValidosFiltro.get("paquete_"+i)+"", Integer.parseInt(mapaPaquetesValidosFiltro.get("institucion_"+i)+"")));
				paquetesValidos.put("componentesAgurpacionArticulos_"+contadorPaquetesValidos, mundoPaquetes.consultarAgrupacionArticulosPaquete(con,mapaPaquetesValidosFiltro.get("paquete_"+i)+"", Integer.parseInt(mapaPaquetesValidosFiltro.get("institucion_"+i)+"")));
				contadorPaquetesValidos++;
			}
		}
		
		
		paquetesValidos.put("numRegistros", contadorPaquetesValidos+"");
		
		return paquetesValidos;
	}

	/**
	 * Metodo que retorna los servicios porncipales de los componentes de servicios.
	 * @param servicios
	 * @return
	 */
	private ArrayList<String> obtenerServiciosPrincipales(HashMap servicios) 
	{
		ArrayList<String> serviciosPrincipales=new ArrayList<String>();
		for(int j=0;j<Integer.parseInt(servicios.get("numRegistros")+"");j++)
		{
			if(UtilidadTexto.getBoolean(servicios.get("principal_"+j)+""))
			{
				serviciosPrincipales.add(servicios.get("codigoServicio_"+j)+"");
			}
		}
		return serviciosPrincipales;
	}

}
