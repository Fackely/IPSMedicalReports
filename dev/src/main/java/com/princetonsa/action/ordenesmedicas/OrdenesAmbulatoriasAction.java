
/*
 * @author Jorge Armando Osorio Velasquez.
 * @author Wilson Rios.
 */
package com.princetonsa.action.ordenesmedicas;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.consultaExterna.UtilidadesConsultaExterna;
import util.facturacion.InfoResponsableCobertura;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.reportes.ConsultasBirt;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.ordenesmedicas.OrdenesAmbulatoriasForm;
import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionPaciente;
import com.princetonsa.dto.manejoPaciente.DTOReporteEstandarAutorizacionServiciosArticulos;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.manejoPaciente.DtoGeneralReporteArticulosAutorizados;
import com.princetonsa.dto.manejoPaciente.DtoGeneralReporteServiciosAutorizados;
import com.princetonsa.dto.ordenes.DtoOrdenesAmbulatorias;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.ClasificacionSocioEconomica;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.ordenesmedicas.OrdenesAmbulatorias;
import com.princetonsa.mundo.solicitudes.SolicitudConsultaExterna;
import com.princetonsa.pdf.OrdenesAmbulatoriasArticulosPdf;
import com.princetonsa.pdf.OrdenesAmbulatoriasServiciosPdf;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.bl.consultaExterna.facade.ConsultaExternaFacade;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.bl.ordenes.facade.OrdenesFacade;
import com.servinte.axioma.dto.capitacion.DtoAutorizacionCapitacionOrdenAmbulatoria;
import com.servinte.axioma.dto.consultaExterna.CitaDto;
import com.servinte.axioma.dto.manejoPaciente.AnulacionAutorizacionSolicitudDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionPorOrdenDto;
import com.servinte.axioma.dto.manejoPaciente.DtoValidacionGeneracionAutorizacionCapitada;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatoEstandar.formatoAutorizacionMedicamentosInsumos.GeneradorReporteFormatoEstandarAutorArticulos;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatoEstandar.formatoAutorizacionServicios.GeneradorReporteFormatoEstandarAutorservicio;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionMedicamentosInsumos.GeneradorReporteFormatoCapitacionAutorArticulos;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionServicios.GeneradorReporteFormatoCapitacionAutorservicio;
import com.servinte.axioma.generadorReporte.ordenes.ordenesAmbulatorias.GeneradorReporteOrdenesAmbulatorias;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.fabrica.ordenes.OrdenesFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IMedicosMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IAutorizacionCapitacionOrdenesAmbulatoriasMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ITiposAfiliadoMundo;
import com.servinte.axioma.mundo.interfaz.ordenes.IOrdenesAmbulatoriasMundo;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Medicos;
import com.servinte.axioma.orm.delegate.facturacion.convenio.ContratosDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * 
 * @author Jorge Armando Osorio Velasquez.
 * @author Wilson Rios.
 */
public class OrdenesAmbulatoriasAction extends Action 
{

	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(OrdenesAmbulatoriasAction.class);
	
	private static final String MOTIVO_ANULACION = "Modificación de la orden ambulatoria de medicamentos";
	
	MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.ordenes.OrdenesAmbulatoriasForm");
	
	public static final String KEY_DETALLE_MEDICAMENTOS = "detalleMedicamentos_";
	public static final String KEY_DETALLE_INSUMO = "detalleInsumos_";
	
	/**
	 * Método execute del action
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ActionForward execute(	ActionMapping mapping, 	
							        ActionForm form, 
							        HttpServletRequest request, 
							        HttpServletResponse response) throws Exception
							        {
		Connection con = null;
		ActionErrors errores = new ActionErrors();
		try {
			if(form instanceof OrdenesAmbulatoriasForm)
			{
				con=UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				
				OrdenesAmbulatoriasForm forma=(OrdenesAmbulatoriasForm)form;
				OrdenesAmbulatorias mundo=new OrdenesAmbulatorias();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				InstitucionBasica institucionActual = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				if(paciente==null || paciente.getCodigoPersona()<1)
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no cargado", "errors.paciente.noCargado", true);

				String estado = forma.getEstado();

				/**
				* Tipo Modificacion: Segun incidencia MT6636
				* Autor: Jesús Darío Ríos
				* usuario: jesrioro
				* Fecha: 13/03/2013
				* Descripcion: 	se  asigna  el valor "" a los atributos NombreArchivoGeneradoOriginal
				* 				y  NombreArchivoGeneradoCopia  para  imprimir desde el detalleOrdenServicio
				* 				y detalleOrdenArticulo.  
				**/
				forma.setNombreArchivoGeneradoOriginal("");
				forma.setNombreArchivoGeneradoCopia("");
				//fin MT6636
				
				logger.info("estado [OrdenesAmbulatoriasAction.java]!!!!!!!!!!-->"+estado);

				forma.setCodigoPersona(paciente.getCodigoPersona());
				forma.setInstitucion(usuario.getCodigoInstitucionInt());

				forma.setMostrarValidacionArticulos(new ResultadoBoolean(false));
				forma.setProcesoExitosoAnulacion(false);
				
				//si tiene permiso para imprimir hc
				
				if(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), ConstantesBD.permisoImprimirDetalleItemHC)){
					forma.setTienePermisoImprimirDetalleItemHC(true); 
				}

				//Se hace esta validación para el caso de autorizaciones de servicios dado que utiliza una la misma jsp para el resumen y para el detalle
				if (estado != null && !estado.equals("imprimirAutorizacion")){
					forma.setListaNombresReportes(new ArrayList<String>());
				}
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de OrdenesAmbulatoriasAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");

				}
				else if (estado.equals("empezar"))
				{

					forma.reset();
					String numDiasEgres=ValoresPorDefecto.getNumDiasEgresoOrdenesAmbulatorias(usuario.getCodigoInstitucionInt());
					if(!UtilidadCadena.noEsVacio(numDiasEgres) || numDiasEgres.equals("-1"))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Falta definir el Número de días de egreso para generar ordenes ambulatorias.", "Falta definir el Número de días de egreso para generar ordenes ambulatorias.", false);
					}
					this.accionEmpezar(request,con,forma,mundo,usuario,paciente);
					return mapping.findForward("principal");

				}
				else if(estado.equals("continuar"))
				{
					this.accionEmpezar(request,con,forma,mundo,usuario,paciente);
					return mapping.findForward("principal");
				}
				else if(estado.equals("verTodas"))
				{
					
					forma.reset();
					forma.setEsTodas(true);
					//Se verifica si se abre de sólo consulta de acuerdo al parámetro
					String esConsulta = request.getParameter("esConsulta");
					if(UtilidadTexto.getBoolean(esConsulta)){
						forma.setEsConsulta(true);
					}else{
						forma.setEsConsulta(false);
					}
					this.accionVerTodas(con,forma,mundo,usuario,paciente);
					this.cargarCuentaSolicitaPaciente(con,forma,mundo,paciente,usuario);
					request.getSession().setAttribute("ocultarEncabezadoOrden",null);
					return mapping.findForward("principal");

				}
				
				else if(estado.equals("verTodasRemoto"))
				{
					forma.resetOrdenesRemoto();

					forma.setEsTodas(true);
					//Se verifica si se abre de sólo consulta de acuerdo al parámetro
					String esConsulta = request.getParameter("esConsulta");
					if(UtilidadTexto.getBoolean(esConsulta))
						forma.setEsConsulta(true);
					else
						forma.setEsConsulta(false);


					this.accionVerTodas(con,forma,mundo,usuario,paciente);
					this.cargarCuentaSolicitaPaciente(con,forma,mundo,paciente,usuario);
					request.getSession().setAttribute("ocultarEncabezadoOrden",null);
					return mapping.findForward("principal");

				}
				else if(estado.equals("verXIngreso"))
				{
					forma.reset();

					forma.setEsTodas(true);
					//Se verifica si se abre de sólo consulta de acuerdo al parámetro
					String esConsulta = request.getParameter("esConsulta");
					if(UtilidadTexto.getBoolean(esConsulta))
						forma.setEsConsulta(true);
					else
						forma.setEsConsulta(false);

					forma.setIdIngreso(Utilidades.convertirAEntero(request.getParameter("idIngreso")));

					this.accionVerTodas(con,forma,mundo,usuario,paciente);
					this.cargarCuentaSolicitaPaciente(con,forma,mundo,paciente,usuario);
					request.getSession().setAttribute("ocultarEncabezadoOrden",null);
					//MT6636 flujo alterno adicional
					//return mapping.findForward("ordenes");
					return mapping.findForward("principal");

				}
				else if(estado.equals("detalleOrden"))
				{
					return accionDetalleOrden(con,forma,mundo,mapping,paciente,usuario, request);
				}
				else if(estado.equals("continuarDetalleOrdenArticulo"))
				{
					return mapping.findForward("detalleArticulo");
				}
				else if(estado.equals("guardarOrden"))
				{
					request.getSession().removeAttribute("MAPAJUS");

					forma.setEstadoOrden(ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente+"");
					forma.setCentroAtencion(usuario.getCodigoCentroAtencion()+"");
					//si el tipo de orden ambulatoria es de articulos y consulta externa hacer la validacion de articulos pedidos con dias en otra orden.
					if(Integer.parseInt(forma.getTipoOrden())==ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos&&(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna||paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios))
					{	
						logger.info("\n\n\n\nentra a articulos!!!!!!!!!!\n\n\n\n");
						int numDiasControl=Utilidades.convertirAEntero(ValoresPorDefecto.getNumeroDiasControlMedicamentosOrdenados(usuario.getCodigoInstitucionInt())+"");
						if(numDiasControl>0)
						{
							if(!forma.isGenerarOrdenArticulosConfirmada())
							{
								forma.setArticulosConfirmacion(Utilidades.consultarArticulosSolicitadosUltimosXDias(con,numDiasControl,forma.getArticulos(),paciente.getCodigoPersona()));
								HashMap<String, Object> mapaArticulos = forma.getArticulosConfirmacion();
								HashMap<String, Object> mapaArticulosPrincipal = mundo.consultarOrdenesAmbulatoriasPacientePrincipal(con, paciente.getCodigoPersona(), usuario.getCodigoInstitucion(), ConstantesBD.codigoNuncaValido);
								if(Utilidades.convertirAEntero(mapaArticulos.get("numRegistros")+"")>0)
								{
									String cadenaArticulos="";
									boolean mostrarMensaje = false;
									int diferenciaDiasFecha = 0;
									for(int i=0;i<Utilidades.convertirAEntero(mapaArticulos.get("numRegistros")+"");i++)
									{
										for(int j=0;j<Utilidades.convertirAEntero(mapaArticulosPrincipal.get("numRegistros")+""); j++)
										{
											HashMap<String, Object> articulos = mundo.cargarDetalleOrdenArticulosValidacion(con, mapaArticulosPrincipal.get("codigo_"+j)+"");
											for(int k=0; k<Utilidades.convertirAEntero(articulos.get("numRegistros")+""); k++)
											{
												if(Integer.parseInt(mapaArticulos.get("articulo_"+i).toString())==Integer.parseInt(articulos.get("articulo_"+k).toString()) && Utilidades.convertirAEntero(mapaArticulosPrincipal.get("estado_"+j)+"")!=ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada)
												{
													logger.info("\n ordenes ambulatorias "+i);
													diferenciaDiasFecha = UtilidadFecha.numeroDiasEntreFechas(mapaArticulosPrincipal.get("fecha_"+j).toString(), UtilidadFecha.conversionFormatoFechaAAp(mapaArticulos.get("fecha_"+i).toString()));
													if(diferenciaDiasFecha <= numDiasControl)
														mostrarMensaje = true;
												}
											}
										}
										if(i>0)
											cadenaArticulos+=",";
										cadenaArticulos+=mapaArticulos.get("articulo_"+i);
									}
									if(mostrarMensaje)
									{
										forma.resetMensaje();
										forma.setMostrarValidacionArticulos(new ResultadoBoolean(true,"El paciente presenta ordenes vigentes para el o los Medicamento(s) [<b>"+cadenaArticulos+"</b>], desea generar la orden de todas formas?"));
										return mapping.findForward("nuevoOrdenArticulos");
									}
								}
							}
						}
					}
					cargarMundoDesdeForm(forma,mundo,usuario,paciente,request);
					//se obtiene el consecutivo disponible tarea xplaner 32673.
					String consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoOrdenesAmbulatorias,  usuario.getCodigoInstitucionInt());
					mundo.setConsecutivosOrdenesInsertadas(new Vector<String>());
					logger.info("\n\n\n\nentra a servicios!!!!!!!!!!\n\n\n\n");
					/*
					 * se asocia a la ultima cuenta, y al centro de costo de esa cuenta.
				//Evalua los ingresos del paciente y toma el ultimo*/
					/*boolean resultado=paciente.cargarTodosIngresos(con,paciente.getCodigoPersona());
				//el listado de ingreso esta ordenados de mayor a menor
				// esto no aplica en todos los casos
				if(resultado)
				{	
					mundo.setIdIngresoPaciente(Integer.parseInt(paciente.getListadoTodosIngresosPaciente().get(0).getIngreso()));
					logger.info("\n\nla ultima cuenta del paciente ingresado"+paciente.getListadoTodosIngresosPaciente().get(0).getIngreso());
				}
				else{
					logger.info("Paciente sin ingresos previos, la orden no se asocia a ningún ingreso");
				}*/
					String[] vector=mundo.guardarOrdenAmbulatoria(con,true,usuario,paciente,request, errores);					
					forma.setConsecutivosOrdenesInsertadas(consecutivo);
					forma.setConsecutivosOrdenesInsertadas1(mundo.getConsecutivosOrdenesInsertadas());

					String codigoOrden=vector[0];
					forma.setNumeroOrden(vector[1]);
					forma.setCodigoOrden(Utilidades.convertirAEntero(codigoOrden));

					//fue requerida la confirmacion, y requiere guardar el log.
					if(forma.isGenerarOrdenArticulosConfirmada())
					{
						Utilidades.generarLogConfirmacionOrdenAmbSolMed(con,codigoOrden,usuario.getLoginUsuario(),ConstantesBD.acronimoNo,forma.getArticulosConfirmacion());
					}

					forma.setGenerarOrdenArticulosConfirmada(false);
					//UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoOrdenesAmbulatorias, usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);

					/**MT 2275 Cuando se generan varios servicios al mismo tiempo en una orden Ambulatoria se debe 
					 * imprimir cuando se encuentre en el resumen -Camilo Gómez*/
					if(Integer.parseInt(forma.getTipoOrden())==ConstantesBD.codigoTipoOrdenAmbulatoriaServicios)
					{
						forma.resetNumerosOrdenesServicio();
						if(mundo.getConsecutivosOrdenesInsertadas().size()>1)
						{	
							ArrayList<String>listaOrdenesAmbulServicios=new ArrayList<String>();
							for(int i=0;i<mundo.getConsecutivosOrdenesInsertadas().size();i++)
								listaOrdenesAmbulServicios.add(mundo.getConsecutivosOrdenesInsertadas().get(i));
							forma.setListaNumOrdenServicios(listaOrdenesAmbulServicios);
						}
					}

					return this.accionResumen(con, codigoOrden, forma,mundo,usuario,mapping, errores, request);
				}
				else if(estado.equals("ingresarResultado"))
				{
					this.accioningresarResultado(con,forma,mundo,usuario);
					forma.reset();
					this.accionEmpezar(request,con,forma,mundo,usuario,paciente);
					return mapping.findForward("principal");
				}
				else if(estado.equals("adicionarJus"))
				{
					/*
					 * Organizar el mapa de justificacionesServicios con los key que hacen falta
					 * exactamente como son guardados en la BD MT 1203
					 */
					if(request.getSession().getAttribute("MAPAJUSSERV")!=null&&request.getSession().getAttribute("MAPAJUSSERVFORM")!=null)
					{
						HashMap justificacion = new HashMap();
						HashMap justificacionForma = new HashMap();
	    		    	justificacion= (HashMap)request.getSession().getAttribute("MAPAJUSSERV");
	    		    	justificacionForma = (HashMap)request.getSession().getAttribute("MAPAJUSSERVFORM");
	    		    	
	    		    	for(int i=0;i<forma.getNumeroFilasMapaCasoServicios();i++)
	    		    	{
	    		    		if((justificacion.get("0_servicio")+"").equals(forma.getJustificacionesServicios(i+"_servicio")+""))
	    		    		{
	    		    			forma.getJustificacionesServicios().put(i+"_mapasecciones", justificacion.get("0_mapasecciones"));
	    		    			
	    		    			HashMap seccionesMap=(HashMap) justificacion.get("0_mapasecciones");
	    		    			int numRegistrosSecciones=Integer.parseInt(seccionesMap.get("numRegistros").toString());
	    		    			for(int j=0;j<numRegistrosSecciones;j++){
	    		    				
	    		    				forma.getJustificacionesServicios().put(i+"_numRegistrosXSec_"+seccionesMap.get("codigo_"+j), justificacion.get("0_numRegistrosXSec_"+seccionesMap.get("codigo_"+j).toString()));
	    		    				
	    		    				int numRegistrosXSeccion=Integer.parseInt(justificacion.get("0_numRegistrosXSec_"+seccionesMap.get("codigo_"+j).toString()).toString());;
	    		    				String codigoSeccion=seccionesMap.get("codigo_"+j).toString();
	    		    				for(int k=0;k<numRegistrosXSeccion;k++){
	    		    					
	    		    					forma.getJustificacionesServicios().put(i+"_tipo_"+codigoSeccion+"_"+k, justificacion.get("0_tipo_"+codigoSeccion+"_"+k));
	    		    					forma.getJustificacionesServicios().put(i+"_numhijos_"+seccionesMap.get("codigo_"+j).toString()+"_"+k, justificacion.get("0_numhijos_"+seccionesMap.get("codigo_"+j).toString()+"_"+k));
	    		    					
	    		    					if(justificacion.get("0_tipo_"+codigoSeccion+"_"+k).toString().equals("CHEC")){
	    		    						for (int h=0; h<Utilidades.convertirAEntero(justificacion.get("0_numhijos_"+seccionesMap.get("codigo_"+j).toString()+"_"+k).toString()); h++)
    		    							{
	    		    							forma.getJustificacionesServicios().put(i+"_valorcampohijo_"+seccionesMap.get("codigo_"+j).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h, justificacion.get("0_valorcampohijo_"+seccionesMap.get("codigo_"+j).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h));
    		    								forma.getJustificacionesServicios().put(i+"_etiguetaseccion_"+seccionesMap.get("codigo_"+j).toString(), justificacion.get("0_etiguetaseccion_"+seccionesMap.get("codigo_"+j).toString()));
    		    								forma.getJustificacionesServicios().put(i+"_codigocampohijo_"+seccionesMap.get("codigo_"+j).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h, justificacion.get("0_codigocampohijo_"+seccionesMap.get("codigo_"+j).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h));
    		    								forma.getJustificacionesServicios().put(i+"_etiquetacampohijo_"+seccionesMap.get("codigo_"+j).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h, justificacion.get("0_etiquetacampohijo_"+seccionesMap.get("codigo_"+j).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h));
    		    								forma.getJustificacionesServicios().put(i+"_codigoparamjuscampohijo_"+seccionesMap.get("codigo_"+j).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h, justificacion.get("0_codigoparamjuscampohijo_"+seccionesMap.get("codigo_"+j).toString()+"_"+ConstantesBD.JusOrdenCampoRiesgo+"_"+h));
    		    							}
	    		    					}else
	    		    					{
	    		    						forma.getJustificacionesServicios().put(i+"_valorcampo_"+codigoSeccion+"_"+k, justificacion.get("0_valorcampo_"+codigoSeccion+"_"+k));
	    		    						forma.getJustificacionesServicios().put(i+"_etiquetaseccion_"+codigoSeccion+"_"+k, justificacion.get("0_etiquetaseccion_"+codigoSeccion+"_"+k));
    		    							forma.getJustificacionesServicios().put(i+"_codigocampo_"+codigoSeccion+"_"+k, justificacion.get("0_codigocampo_"+codigoSeccion+"_"+k));
    		    							forma.getJustificacionesServicios().put(i+"_etiquetacampo_"+codigoSeccion+"_"+k, justificacion.get("0_etiquetacampo_"+codigoSeccion+"_"+k));
    		    							forma.getJustificacionesServicios().put(i+"_codigoparametrizacion_"+codigoSeccion+"_"+k, justificacion.get("0_codigoparametrizacion_"+codigoSeccion+"_"+k));
	    		    					}
	    		    				}
	    		    			}
	    		    			forma.getJustificacionesServicios().put(i+"_mapajustservform", justificacionForma);
	    		    		}
	    		    	}
					}
					
					request.getSession().setAttribute("MAPAJUS", forma.getJustificacionesServicios());
					return mapping.findForward("nuevoOrdenServicios");
				}
				else if(estado.equals("anularOrden"))
				{
					
					forma.setProcesoExitosoAnulacion(false);
					this.accionAnular(con,forma,usuario,errores,request);
					/*
					 * MT 6004
					 */
					if(Integer.parseInt(forma.getTipoOrden())==ConstantesBD.codigoTipoOrdenAmbulatoriaServicios)
						{
							return mapping.findForward("detalleServicio");
						}else {
							return mapping.findForward("detalleArticulo");
						}
					/*else{*/
					/*	forma.reset();
					this.accionEmpezar(request,con,forma,mundo,usuario,paciente);
						return mapping.findForward("principal");*/
					//}
				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenar(forma);
					return mapping.findForward("principal");
				}
				else if(estado.equals("nuevo"))
				{
					return this.accionNuevo(con, forma, mapping, request, usuario);
				}
				else if(estado.equals("ingresarOrden"))
				{
					forma.resetCheck();
					forma.resetMapaArticulos();
					forma.resetMapaServicios();
					this.cargarCuentaSolicitaPaciente(con, forma, mundo, paciente,usuario);
					return this.accionIngresarOrden(forma, mapping,usuario);
				}
				else if(estado.equals("ingresarNuevoServicio"))
				{
					/*request.getSession().removeAttribute("MAPAJUSSERV");
					request.getSession().removeAttribute("MAPAJUSART");*/
					return this.accionIngresarServicio(forma, mapping);
				}
				else if(estado.equals("ingresarNuevoArticulo"))
				{
					request.removeAttribute("JUSTIFICACION");
					/*request.getSession().removeAttribute("MAPAJUSSERV");
					request.getSession().removeAttribute("MAPAJUSART");*/
					return this.accionIngresarArticulo(forma, mapping);
				}
				else if(estado.equals("ingresarNuevoArticuloDetalle"))
				{
					return this.accionIngresarArticuloDetalle(forma, mapping);
				}
				else if(estado.equals("guardarSolicitar"))
				{
					//entra a guardar la solicitud solo cuando se genera una sola solicitud y se carga en la forma de ordenes
					//este caso solo pasa cuando son servicios de tipo consulta y se genera la cita, no la solicitud.
					if(!forma.getNumeroSolicitud().trim().equals(""))
						this.accionGuardarSolicitar(con,forma,mundo,usuario);
					this.accionEmpezar(request,con,forma,mundo,usuario,paciente);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardarModOrdenArticulos"))
				{
					return this.accionGuardarModOrdenArticulos(con, forma, mundo, response, mapping, request, usuario, paciente, errores);
				}
				else if(estado.equals("guardarModificacionesDetalleOrdenArticulos"))
				{
					return this.accionGuardarModificacionesDetalleOrdenArticulos(con, forma, mundo, response, mapping, request, usuario);
				}			
				else if (estado.equals("imprimirOrdenArticulos") || estado.equals("imprimirOrdenArticulosResumen"))
				{
					//evaluamos si es media carta o no y si la impresionde la orden ambulatoria es de medicamentos de control o no
					if (forma.getArticulos("controlespecial_0").toString().equals(ConstantesBD.acronimoNo))
					{
						if(UtilidadTexto.getBoolean(ValoresPorDefecto.getImpresionMediaCarta(usuario.getCodigoInstitucionInt())))
							return this.accionImprimirArticuloMediaCarta(con, forma, usuario, request, paciente, mapping, institucionActual);
						else
							return this.accionImprimirArticuloCartaCompleta(con, forma, usuario, mapping, request, mundo, paciente,institucionActual);
					}
					else if (forma.getArticulos("controlespecial_0").toString().equals(ConstantesBD.acronimoSi))
					{
						boolean mediaCarta=false;
						if(UtilidadTexto.getBoolean(ValoresPorDefecto.getImpresionMediaCarta(usuario.getCodigoInstitucionInt())))
							mediaCarta=true;
						else
							mediaCarta=false;

						return this.accionImprimirArticuloControlCartaCompleta(con, forma, usuario, mapping, request, mundo, paciente, mediaCarta);

					}
				}
				else if (estado.equals("imprimirOrdenServicios") || estado.equals("imprimirOrdenServiciosDesdeResumenNuevo"))
				{
					return this.imprimirOrdenesDetalle(forma, usuario, mapping, request, mundo, paciente, institucionActual);

					//evaluamos si es media carta o no
					/*if(UtilidadTexto.getBoolean(ValoresPorDefecto.getImpresionMediaCarta(usuario.getCodigoInstitucionInt())))
					return this.accionImprimirServicioMediaCarta(con, forma, usuario, request, paciente, mapping, institucionActual);
				else
					return this.accionImprimirServicioCartaCompleta(con, forma, usuario, mapping, request, mundo, paciente, institucionActual);*/
				}
				else if(estado.equals("validarCentroCosto"))
				{
					return this.accionValidarCentroCosto(con, forma, mundo, response, mapping, request, usuario);
				}
				else if(estado.equals("tipoEntidadEjecuta"))
				{
					return this.accionTipoEntidadEjecuta(con, forma, mundo, response, mapping, request, usuario);
				}
				else if(estado.equals("generarSolInterCon"))
				{
					return this.accionGenerarSolInterCon(con, forma, mundo, response, mapping, request, usuario, paciente);
				}
				else if (estado.equals("checkearTodo"))
				{				
					return this.checkearTodoImpresion(forma, mapping);//------------------------>checkAll
				}			
				else if (estado.equals("redireccion"))// estado para mantener los datos del pager
				{			    
					forma.getLinkSiguiente();
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("imprimirListadoOrdenes"))
				{
					return this.imprimirOrdenesSeleccionadas(con, forma, usuario, mapping, request, mundo, paciente, institucionActual);
				}else if(estado.equals("imprimirListadoOrdenesDetalle"))
				{
					return this.imprimirOrdenesDetalle(forma, usuario, mapping, request, mundo, paciente, institucionActual);
				}
				else if (estado.equals("imprimirAutorizacion"))
				{
					//this.accionImprimirAutorizacion(con, forma,usuario,paciente, errores, request);
					if(Integer.parseInt(forma.getTipoOrden())==ConstantesBD.codigoTipoOrdenAmbulatoriaServicios){
						return mapping.findForward("resumenOrdenServicios");
					}else{
						return mapping.findForward("detalleArticulo");
					}
				}
				else
				{
					logger.info("entra!!!!");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de OrdenesAmbulatoriasForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
			return null;
		}catch (BDException e) {
			Log4JManager.error(e);
			errores.add("ERROR BD", new ActionMessage(e.getErrorCode().toString(),e.getParamsMsg()));
			saveErrors(request, errores);
		}catch(IPSException ipse){
			Log4JManager.error(ipse);
			errores.add("ERROR Negocio", new ActionMessage(ipse.getErrorCode().toString(),ipse.getParamsMsg()));
			saveErrors(request, errores);
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("ERROR no Controlado", new ActionMessage("errors.notEspecific", e.getMessage()));
			saveErrors(request, errores);
		}finally{
			UtilidadBD.closeConnection(con);
		}
		
		return mapping.findForward("principal");
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param response
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionGenerarSolInterCon(Connection con, OrdenesAmbulatoriasForm forma, OrdenesAmbulatorias mundo, HttpServletResponse response, ActionMapping mapping, 	HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente) throws SQLException
	{	
		Cargos cargos = new Cargos();
		cargos.setPyp(forma.isPyp());
		boolean resultado = false;
		int resultado1 = 0;
		
		
		//se genera la solicitud--------------------------------------------------

		SolicitudConsultaExterna lsce_solicitud = new SolicitudConsultaExterna();


		logger.info("\n\nESPECIALIDAD  SOLICITANTE------->"+forma.getEspecialidad()+"\n\nCONSECUTIVO ORDEN----->"+forma.getNumeroOrden());
		logger.info("\n\n ES URGENTE ---> "+forma.isUrgente());
		lsce_solicitud.setTipoSolicitud(new InfoDatosInt(ConstantesBD.codigoTipoSolicitudCita) );
		lsce_solicitud.setEspecialidadSolicitante(new InfoDatosInt(Utilidades.convertirAEntero(forma.getEspecialidad())));
		lsce_solicitud.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea()));
		lsce_solicitud.setCentroCostoSolicitado(new InfoDatosInt(Utilidades.convertirAEntero(forma.getCentroCostoSel())));
		lsce_solicitud.setConsecutivoOrdenesMedicas(Utilidades.convertirAEntero(forma.getNumeroOrden()));
		lsce_solicitud.setCodigoCuenta(paciente.getCodigoCuenta());
		lsce_solicitud.setCobrable(true);
		lsce_solicitud.setVaAEpicrisis(false);
		lsce_solicitud.setUrgente(forma.isUrgente());
		lsce_solicitud.setEstadoHistoriaClinica(new InfoDatosInt(ConstantesBD.codigoEstadoHCSolicitada));
		lsce_solicitud.setTieneCita(false);
		lsce_solicitud.setFechaSolicitud(UtilidadFecha.conversionFormatoFechaAAp(new Date() ) );
		lsce_solicitud.setHoraSolicitud(UtilidadFecha.conversionFormatoHoraAAp(new Date() ) );
				
		lsce_solicitud.setCodigoServicioSolicitado(Integer.parseInt(forma.getServicio()));
		
		lsce_solicitud.setOcupacionSolicitado(new InfoDatosInt(usuario.getCodigoOcupacionMedica()) );
		lsce_solicitud.setSolPYP(forma.isPyp());
		
		//Cambio Version 1.50 Anexo Solicitud de Procedimientos
		//@author Diana Carolina G
		//Se consulta el Diagn&oacute;stico asociado a la orden ambulatoria
		//para ser guardado en la solicitud de consulta externa
		//************************************************************************/
		int codigoOrdenAmbulatoria = forma.getCodigoOrden(); 
		DtoDiagnostico dtoDiagnostico = OrdenesAmbulatorias.consultarDiagnosticoOrdenAmbulatoria(con, codigoOrdenAmbulatoria);
		lsce_solicitud.setDtoDiagnostico(dtoDiagnostico);
		//************************************************************************/
		
		
		lsce_solicitud.insertarSolicitudConsultaExternaTransaccional(con, ConstantesBD.continuarTransaccion);
		
		if(lsce_solicitud.getNumeroSolicitud()>0)
			resultado = true;
						
		HashMap codigoOrden=new HashMap();
		
		codigoOrden= mundo.consultarCodigoOrdenAmb(forma.getNumeroOrden()+"");
		
		logger.info("\n\nCODIGO ORDEN----->"+codigoOrden);
		
		if(resultado)
		{
			HashMap vo=new HashMap();
			vo.put("estadoOrden",ConstantesBD.codigoEstadoOrdenAmbulatoriaSolicitada+"");
			vo.put("numeroSolicitud",lsce_solicitud.getNumeroSolicitud()+"");
			vo.put("numeroOrden",codigoOrden.get("codigo"));
			vo.put("usuario",usuario.getLoginUsuario());
			//esta consulta solo aplica para ordenes de tipo consulta, ya que en la generacion de la cita no se hace esta confimacion.
			if(OrdenesAmbulatorias.confirmarOrdenAmbulatoria(con,vo)<=0)
				resultado= false;
		}
		
		if(resultado)
		{
			//Se genera el cargo
			resultado1 = 1;
			//cargo.setEsCita(false); //se dice que no es cita para que pueda generar cargo pendiente en caso de que no haya tarifa
			//cargo.setServicio(Integer.parseInt(codigoActividad));
			//String[] erroresCargo=cargo.generarCargoTransaccional(con,lsce_solicitud.getNumeroSolicitud(),ConstantesBD.codigoCentroCostoConsultaExterna,paciente.getCodigoContrato(),paciente.getEstaContratoVencido(),paciente.getCodigoUltimaViaIngreso(),ConstantesBD.codigoTipoSolicitudCita,usuario.getLoginUsuario(),1,0,Integer.parseInt(codigoActividad),false,"",//aqui van las observaciones cuando se requieranConstantesBD.continuarTransaccion,false,/*utilizarValorTarifaOpcional*/-1/*valorTarifaOpcional*/);
			
			if(!cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
																				usuario, 
																				paciente, 
																				false/*dejarPendiente*/, 
																				lsce_solicitud.getNumeroSolicitud(), 
																				ConstantesBD.codigoTipoSolicitudCita /*codigoTipoSolicitudOPCIONAL*/, 
																				paciente.getCodigoCuenta(), 
																				ConstantesBD.codigoCentroCostoConsultaExterna/*codigoCentroCostoEjecutaOPCIONAL*/, 
																				Integer.parseInt(forma.getServicio())/*codigoServicioOPCIONAL*/, 
																				1/*cantidadServicioOPCIONAL*/, 
																				ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
																				ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
																				/*"" -- numeroAutorizacionOPCIONAL*/
																				""/*esPotatil*/,false,"",
																				"" /*subCuentaCoberturaOPCIONAL*/
																			))
			{
				resultado1 = 0;
			}
			
			
			if(resultado1 != 0)
			{
				forma.setMensaje(new ResultadoBoolean(true,"Se Genera Solicitud Numero "+lsce_solicitud.getNumeroSolicitud()+" de Consulta para Entidad Subcontratada, esta Solicitud debe ser Autorizada. Por favor Verifique"));
			}
			else
			{
				forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
			}			
		}
		
		mundo.consultarOrdenesAmbulatoriasPaciente(con,paciente.getCodigoPersona(),usuario.getCodigoInstitucion(),ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente, ConstantesBD.codigoNuncaValido);
		forma.setOrdenes((HashMap)mundo.getOrdenes().clone());
		forma.setEsPreingreso(true);
		// ACA  Validar y Generar Autorización Población Capitada
		return mapping.findForward("detalleServicio");
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param response
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ActionForward accionTipoEntidadEjecuta(Connection con, OrdenesAmbulatoriasForm forma, OrdenesAmbulatorias mundo, HttpServletResponse response, ActionMapping mapping, 	HttpServletRequest request, UsuarioBasico usuario)
	{	
		ActionErrors errores= new ActionErrors();
		for(int i=0;i<(Utilidades.convertirAEntero(forma.getCentrosCostoMap("numRegistros")+""));i++)
		{
			if((forma.getCentrosCostoMap("centroCosto_"+i)+"").equals(forma.getCentroCostoSel()))
			{
				forma.setTipoEntidadEjecuta((forma.getCentrosCostoMap("tipoEntidadEjecuta_"+i)+""));
				if(forma.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoInterna)){
					if(!UtilidadesConsultaExterna.existeAgendaXCentroCostoXServicio(con, Integer.parseInt(forma.getCentroCostoSel()), 
																					Integer.parseInt(forma.getServicios("codigoServicio").toString()), 
																					UtilidadFecha.getFechaActual(con))){
						forma.setBotonGenerarSolicitud(false);
						errores.add("No hay agenda creada para el centro de costo y a las unidades de agenda del servicio", 
								new ActionMessage("errors.notEspecific",messageResource.getMessage("ordenesAmbulatorias.noAgendaCentroCostoXServicio", new Object[]{forma.getCentrosCostoMap().get("nombre_"+i)})));
						saveErrors(request, errores);
					}
				}
				else{
					forma.setBotonGenerarSolicitud(true);
				}
				break;
			}
		}
		return mapping.findForward("validarCentroCosto");
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	private ActionForward accionValidarCentroCosto(Connection con, OrdenesAmbulatoriasForm forma, OrdenesAmbulatorias mundo, HttpServletResponse response, ActionMapping mapping, 	HttpServletRequest request, UsuarioBasico usuario)
	{	
		
		forma.setCentroCostoSel("");
		ActionErrors errores=new ActionErrors();
		try {
			forma.setServicios(mundo.consultarServiciosOrdenAmbulatoria(con, forma.getNumeroOrden(), usuario.getCodigoInstitucionInt()));

			for(int i=0;i<(Utilidades.convertirAEntero(forma.getServicios("numRegistros")+""));i++)
			{
				if(forma.getServicio().equals((forma.getServicios("codigoServicio_"+i)+"")))
				{
					forma.setServicios("codigoServicio", (forma.getServicios("codigoServicio_"+i)+""));
					forma.setServicios("descripcionServicio", (forma.getServicios("descripcionServicio_"+i)+""));
					forma.setServicios("codigoCups", (forma.getServicios("codigoCups_"+i)+""));
					forma.setServicios("esPos", (forma.getServicios("esPos_"+i)+""));
					forma.setServicios("grupoServicio", (forma.getServicios("grupoServicio_"+i)+""));	
				}
			}

			forma.setCentrosCostoMap(mundo.consultaCentrosCostoXUnidadAgendaServ(con, Utilidades.convertirAEntero(forma.getServicios("codigoServicio")+"")));

			int tamanoMapaCenCosGrupoSer=Utilidades.convertirAEntero(forma.getCentrosCostoMap("numRegistros").toString());
			if(tamanoMapaCenCosGrupoSer == 0){
				forma.setBotonGenerarSolicitud(false);
				forma.setCentroCostoSel("");
				errores.add("No hay centros de costo asociados a las unidades de agenda del servicio", 
						new ActionMessage("errors.notEspecific",messageResource.getMessage("ordenesAmbulatorias.noCentroCostoXUnidadAgenda")));
				saveErrors(request, errores);
				return mapping.findForward("validarCentroCosto");
			}
			else{
				if(tamanoMapaCenCosGrupoSer==1){
					forma.setCentroCostoSel(forma.getCentrosCostoMap().get("centroCosto_"+0).toString());
					forma.setTipoEntidadEjecuta((forma.getCentrosCostoMap().get("tipoEntidadEjecuta_"+0).toString()));
					if(forma.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoInterna)){
						if(!UtilidadesConsultaExterna.existeAgendaXCentroCostoXServicio(con, Integer.parseInt(forma.getCentroCostoSel()), 
								Integer.parseInt(forma.getServicios("codigoServicio").toString()), 
								UtilidadFecha.getFechaActual(con))){
							forma.setBotonGenerarSolicitud(false);
							errores.add("No hay agenda creada para el centro de costo y a las unidades de agenda del servicio", 
									new ActionMessage("errors.notEspecific",messageResource.getMessage("ordenesAmbulatorias.noAgendaCentroCostoXServicio", new Object[]{forma.getCentrosCostoMap().get("nombre_"+0)})));
							saveErrors(request, errores);
							return mapping.findForward("validarCentroCosto");
						}
					}
				}
			}
			//------------------------------------------------------------------------------------------------------------
			/*ICentroCostoMundo centroCostoMundo=AdministracionFabricaMundo.crearCentroCostoMundo();
		ArrayList<DtoCentroCosto> listaCentroCostoGrupoSer=new ArrayList<DtoCentroCosto>(); 
		listaCentroCostoGrupoSer = centroCostoMundo.listaCentroCostoGrupoServicio(Utilidades.convertirAEntero(forma.getServicios("grupoServicio").toString()));*/

			HibernateUtil.beginTransaction();
			//Validar si existe Autorización asociada a la Orden Ambulatoria		
			IAutorizacionCapitacionOrdenesAmbulatoriasMundo autorizacionCapitacionOrdenesAmbulatoriasMundo=CapitacionFabricaMundo.crearAutorizacionCapitacionOrdenesAmbulatoriasMundo();
			ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria> listaAutorizaciones = new ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria>();
			DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizCapitaOrdenAmbu=new DtoAutorizacionCapitacionOrdenAmbulatoria();
			dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().setNumeroOrden(forma.getNumeroOrden());
			//FIXME retorna lista de las ordenes autorizadas previamente
			listaAutorizaciones = autorizacionCapitacionOrdenesAmbulatoriasMundo.existeAutorizacionesOrdenAmbul(dtoAutorizCapitaOrdenAmbu);
																				 
			/*dtoAutorizCapitaOrdenAmbu = new DtoAutorizacionCapitacionOrdenAmbulatoria();//NO OLVIDAR QUITAR ESTAS LINEAS
			dtoAutorizCapitaOrdenAmbu.setCentroAtencionCorresponde(true);*/
			
			forma.setBotonGenerarSolicitud(false);
			if(listaAutorizaciones!=null && !listaAutorizaciones.isEmpty())
			{
				for(DtoAutorizacionCapitacionOrdenAmbulatoria autorizCapitaAmbul: listaAutorizaciones)
				{
					if( !autorizCapitaAmbul.isCentroAtencionCorresponde())
					{
						forma.setCentroCostoSel("");
						HashMap mapaErrorCentroAtencion=new HashMap();
						forma.setCentrosCostoMap(mapaErrorCentroAtencion);
						errores.add("centroCostoNoCorres", new ActionMessage("errors.notEspecific",messageResource.getMessage("ordenesAmbulatorias.centroCostoNoCorresponde")));
						saveErrors(request, errores);
						return mapping.findForward("validarCentroCosto");
					}else{
						//				dtoAutorizCapitaOrdenAmbu.setCentrosCostoSolicitadoAutoriz(dtoAutorizCapitaOrdenAmbu.getCodigoCentrosCostoSolicitadoAutoriz()+" "+dtoAutorizCapitaOrdenAmbu.getCentrosCostoSolicitadoAutoriz());
						//				dtoAutorizCapitaOrdenAmbu.setCentroAtencionAutoriz(dtoAutorizCapitaOrdenAmbu.getCodigoCentroAtencionAutoriz()+" "+dtoAutorizCapitaOrdenAmbu.getCentroAtencionAutoriz());
						forma.setCentroCostoCorrespondeAutorizacion(true);
	
						/*dtoAutorizCapitaOrdenAmbu.setCentrosCostoSolicitadoAutoriz(0+" "+"Centro de Costo");
					dtoAutorizCapitaOrdenAmbu.setCentroAtencionAutoriz(1+" "+"Centro de Atencion");*/
						HashMap mapaCentroAtencion=new HashMap();
						mapaCentroAtencion.put("numRegistros", 1);
						mapaCentroAtencion.put("centroCosto_"+0, autorizCapitaAmbul.getCodigoCentrosCostoSolicitadoAutoriz());
						mapaCentroAtencion.put("nombre_"+0, autorizCapitaAmbul.getCentrosCostoSolicitadoAutoriz());//--Carga el Centro de Costo
						mapaCentroAtencion.put("centroAtencion_"+0, autorizCapitaAmbul.getCentroAtencionAutoriz());//--Carga el Centro de Atención
						mapaCentroAtencion.put("tipoEntidadEjecuta_"+0,autorizCapitaAmbul.getTipoEntidadCentroCostoSolicitadoAutoriz());
						forma.setCentrosCostoMap(mapaCentroAtencion);
						forma.setTipoEntidadEjecuta((forma.getCentrosCostoMap("tipoEntidadEjecuta_"+0)+""));
						forma.setCentroCostoSel(forma.getCentrosCostoMap().get("centroCosto_"+0).toString());
						if(forma.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoInterna)){
							if(!UtilidadesConsultaExterna.existeAgendaXCentroCostoXServicio(con, Integer.parseInt(forma.getCentroCostoSel()), 
									Integer.parseInt(forma.getServicios("codigoServicio").toString()), 
									UtilidadFecha.getFechaActual(con))){
								errores.add("No hay agenda creada para el centro de costo y a las unidades de agenda del servicio", 
										new ActionMessage("errors.notEspecific",messageResource.getMessage("ordenesAmbulatorias.noAgendaCentroCostoXServicio", new Object[]{forma.getCentrosCostoMap().get("nombre_"+0)})));
								saveErrors(request, errores);
								return mapping.findForward("validarCentroCosto");
							}
						}
						forma.setBotonGenerarSolicitud(true);
					}
				}
			}else{//Si no existe autorizacion debe cargar los centro de costo de la orden como estaba y mostrar el boton 'generar solicitud'
				forma.setBotonGenerarSolicitud(true);
			}
			//------------------------------------------------------------------------------------------------------------
			HibernateUtil.endTransaction();
		} catch (Exception e) {
			Log4JManager.error("Validar centro costo: " + e);
			HibernateUtil.abortTransaction();
		}
		return mapping.findForward("validarCentroCosto");
	}

	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param mundo
	 * @param paciente
	 * @param institucionActual 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ActionForward accionImprimirArticuloCartaCompleta(Connection con, OrdenesAmbulatoriasForm forma, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, OrdenesAmbulatorias mundo, PersonaBasica paciente, InstitucionBasica institucionActual) 
	{
		Vector archivosGenerados = new Vector();
		
		String nombreArchivo;
    	
    	UsuarioBasico usuarioGeneraSolicitud= new UsuarioBasico();
    	String login=Utilidades.getLoginUsuarioSolicitaOrdenAmbulatoria(con, forma.getNumeroOrden(), usuario.getCodigoInstitucionInt());
    	if(!login.equals(""))
    	{	
    		try 
    		{
				usuarioGeneraSolicitud.cargarUsuarioBasico(con, login);
			} 
    		catch (SQLException e) 
    		{
				e.printStackTrace();
			}
    	}
    	
    	//************ Copia
    	Random r=new Random();
    	nombreArchivo="/OrdenesAmbulatoriasArticulos-" + r.nextInt()  +".pdf";
    	try
    	{
    	    OrdenesAmbulatoriasArticulosPdf.pdf(con, ValoresPorDefecto.getFilePath() + nombreArchivo, forma, usuarioGeneraSolicitud, paciente, institucionActual, "Copia");
    	    archivosGenerados.add(nombreArchivo);
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error de tamaño generando PDF de Ordenes Ambulatorias Artículos", "errors.pdfSuperoMemoria", true);
    	}
    	
    	//************* Original
    	r=new Random();
    	nombreArchivo="/OrdenesAmbulatoriasArticulos-" + r.nextInt()  +".pdf";
    	try
    	{
    	    OrdenesAmbulatoriasArticulosPdf.pdf(con, ValoresPorDefecto.getFilePath() + nombreArchivo, forma, usuarioGeneraSolicitud, paciente, institucionActual, "Original");
    	    archivosGenerados.add(nombreArchivo);
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error de tamaño generando PDF de Ordenes Ambulatorias Artículos", "errors.pdfSuperoMemoria", true);
    	}

        //request.setAttribute("nombreArchivo", nombreArchivo);
        request.setAttribute("nombreVentana1", "Orden Ambulatoria de Artículos");
        request.setAttribute("archivos", archivosGenerados);
        return mapping.findForward("abrirNPdf");
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param paciente
	 * @param mapping
	 * @param institucionActual 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionImprimirArticuloMediaCarta(Connection con, OrdenesAmbulatoriasForm forma, UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente, ActionMapping mapping, InstitucionBasica institucionActual) 
	{
		HashMap archivosGeneradosBirt=new HashMap();
		String newPathReport="";
		
		//********** Copia
		newPathReport = imprimirArticuloMediaCarta(con, forma, usuario, request, paciente, mapping, institucionActual, "Copia");
		archivosGeneradosBirt.put("isOpenReport_0", "true");
    	archivosGeneradosBirt.put("newPathReport_0", newPathReport);
		
    	//********** Original
    	newPathReport = imprimirArticuloMediaCarta(con, forma, usuario, request, paciente, mapping, institucionActual, "Original");
		archivosGeneradosBirt.put("isOpenReport_1", "true");
    	archivosGeneradosBirt.put("newPathReport_1", newPathReport);
    	
        archivosGeneradosBirt.put("numRegistros", "2");
        
        request.setAttribute("archivosBirt", archivosGeneradosBirt);
        request.setAttribute("archivos", new Vector());
        request.setAttribute("nombreVentana1", "");
        
        UtilidadBD.closeConnection(con);
        return mapping.findForward("abrirNPdfBirt");
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param paciente
	 * @param mapping
	 * @param institucionActual 
	 * @param tipoImpresion 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String imprimirArticuloMediaCarta(Connection con, OrdenesAmbulatoriasForm forma, UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente, ActionMapping mapping, InstitucionBasica institucionActual, String tipoImpresion){
		logger.info("\n\n\n accionImprimirArticuloMediaCarta--->"+forma.getEstado());
		DesignEngineApi comp;        
		InstitucionBasica institucionBasica = new InstitucionBasica();
		logger.info("Convenio Paciente - "+paciente.getCodigoConvenio());
		if(Utilidades.convertirAEntero(paciente.getCodigoConvenio()+"")>0)
			institucionBasica.cargarXConvenio(con, usuario.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
		else
			institucionBasica = institucionActual;
		
		UsuarioBasico usuarioGeneraSolicitud= new UsuarioBasico();
		String login=Utilidades.getLoginUsuarioSolicitaOrdenAmbulatoria(con, forma.getNumeroOrden(), usuario.getCodigoInstitucionInt());
		if(!login.equals(""))
		{	
			try 
			{
				usuarioGeneraSolicitud.cargarUsuarioBasico(con, login);
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		
		String reporte="ordenAmbulatoriaArticuloMediaCarta.rptdesign";
		if(!UtilidadTexto.isEmpty(usuarioGeneraSolicitud.getFirmaDigital()))
			reporte="ordenAmbulatoriaArticuloMediaCartaFirmaDigital.rptdesign";
		
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"ordenes/",reporte);
		
		//en la primera celda va el nombre de la institucion y el cod min salud
		Vector v=new Vector();
		v.add(institucionBasica.getRazonSocial());
		if(Utilidades.convertirAEntero(institucionBasica.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
			v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucionBasica.getTipoIdentificacion())+". "+institucionBasica.getNit()+" - "+institucionBasica.getDigitoVerificacion());
		else
			v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucionBasica.getTipoIdentificacion())+". "+institucionBasica.getNit());
		//Con la intención de Estandarizar los Reportes
		//v.add(institucionBasica.getCodMinsalud());
		//v.add(institucionBasica.getDireccion());
		/*---------------Modificacion por tarea 77564*/
		InfoDatosInt centroAten=UtilidadesHistoriaClinica.obtenerCentroAtencionCuenta(con, paciente.getCodigoCuenta());
		HashMap criterios = new HashMap ();
		criterios.put("consecutivo", centroAten.getCodigo());
		HashMap tmp=Utilidades.obtenerDatosCentroAtencion(con, criterios);
		logger.info("\n centro aten -->"+centroAten);
		if (UtilidadCadena.noEsVacio(tmp.get("direccion")+""))
			v.add(tmp.get("direccion")+"");        				
		else
			v.add(institucionBasica.getDireccion());
		/*-----------------------------------------------------------*/
		
		v.add("Tels. "+institucionBasica.getTelefono());
		comp.insertGridHeaderOfMasterPage(0,1,1,v.size());
		comp.insertLabelInGridOfMasterPage(0,1,v);
		
		//en la segunda celda se coloca el logo de la institucion
		comp.insertImageHeaderOfMasterPage1(0, 2, institucionBasica.getLogoReportes());
		
		if(!UtilidadTexto.isEmpty(usuarioGeneraSolicitud.getFirmaDigital()))
			comp.insertImageBodyPage(0, 0, usuarioGeneraSolicitud.getPathFirmaDigital(), "grillaFirmaDigital");
		
		comp.insertLabelBodyPage(0, 0, institucionBasica.getPieHistoriaClinica(), "piehiscli");
		
		
		comp.obtenerComponentesDataSet("insumos");
		comp.modificarQueryDataSet(comp.obtenerQueryDataSet().replaceAll("valorFalseParaConsultas", ValoresPorDefecto.getValorFalseParaConsultas()));
		
		comp.obtenerComponentesDataSet("medicamentos");
		comp.modificarQueryDataSet(comp.obtenerQueryDataSet().replaceAll("valorTrueParaConsultas", ValoresPorDefecto.getValorTrueParaConsultas()));
		
		//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
		comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
		newPathReport += "&usuario="+usuario.getLoginUsuario()+"&tipoimp="+tipoImpresion+"&consecutivo_orden="+forma.getNumeroOrden()+"&institucion="+usuario.getCodigoInstitucion();
        if(!newPathReport.equals(""))
        {
            request.setAttribute("isOpenReport", "true");
            request.setAttribute("newPathReport", newPathReport);
        }            
        
        if(forma.getEstado().equals("imprimirOrdenArticulosResumen"))
        {
        	request.setAttribute("esResumen", "true");
        }
        
        return newPathReport;
	}

	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param institucionActual 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private ActionForward accionImprimirServicioMediaCarta(Connection con, OrdenesAmbulatoriasForm forma, UsuarioBasico usuario,  HttpServletRequest request, PersonaBasica paciente, ActionMapping mapping, InstitucionBasica institucionActual) 
	{
		HashMap archivosGeneradosBirt=new HashMap();
		String newPathReport="";
		
		//********** Copia
		newPathReport = imprimirServicioMediaCarta(con, forma, usuario, request, paciente, mapping, institucionActual, "Copia");
		archivosGeneradosBirt.put("isOpenReport_0", "true");
    	archivosGeneradosBirt.put("newPathReport_0", newPathReport);
		
    	//********** Original
    	newPathReport = imprimirServicioMediaCarta(con, forma, usuario, request, paciente, mapping, institucionActual, "Original");
		archivosGeneradosBirt.put("isOpenReport_1", "true");
    	archivosGeneradosBirt.put("newPathReport_1", newPathReport);
    	
        archivosGeneradosBirt.put("numRegistros", "2");
        
        request.setAttribute("archivosBirt", archivosGeneradosBirt);
        request.setAttribute("archivos", new Vector());
        request.setAttribute("nombreVentana1", "");
        
        UtilidadBD.closeConnection(con);
        return mapping.findForward("abrirNPdfBirt");
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param institucionActual 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String imprimirServicioMediaCarta(Connection con, OrdenesAmbulatoriasForm forma, UsuarioBasico usuario,  HttpServletRequest request, PersonaBasica paciente, ActionMapping mapping, InstitucionBasica institucionActual, String tipoImpresion) 
	{
DesignEngineApi comp;
		
		InstitucionBasica institucionBasica= new InstitucionBasica();
        if(Utilidades.convertirAEntero(paciente.getCodigoConvenio()+"")>0)
        	institucionBasica.cargarXConvenio(con, usuario.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
        else
        	institucionBasica = institucionActual;
		
        UsuarioBasico usuarioGeneraSolicitud = new UsuarioBasico();
    	String login=Utilidades.getLoginUsuarioSolicitaOrdenAmbulatoria(con, forma.getNumeroOrden(), usuario.getCodigoInstitucionInt());
    	if(!login.equals(""))
    	{	
    		try 
    		{
				usuarioGeneraSolicitud.cargarUsuarioBasico(con, login);
			} 
    		catch (SQLException e) 
    		{
				e.printStackTrace();
			}
    	}
        
    	String reporte="ordenAmbulatoriaServiciosMediaCarta.rptdesign";
    	if(!UtilidadTexto.isEmpty(usuarioGeneraSolicitud.getFirmaDigital()))
			reporte="ordenAmbulatoriaServiciosMediaCartaFirmaDigital.rptdesign";
    	
    	comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"ordenes/",reporte);
        
    	//en la primera celda va el nombre de la institucion y el cod min salud
    	Vector v=new Vector();
        v.add(institucionBasica.getRazonSocial());
        if(Utilidades.convertirAEntero(institucionBasica.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucionBasica.getTipoIdentificacion())+". "+institucionBasica.getNit()+" - "+institucionBasica.getDigitoVerificacion());
        else
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucionBasica.getTipoIdentificacion())+". "+institucionBasica.getNit());
        //Con la intención de Estandarizar los Reportes
        //v.add(institucionBasica.getCodMinsalud());
        
       // v.add(institucionBasica.getDireccion());
        
        /*---------------Modificacion por tarea 77564*/
        InfoDatosInt centroAten=UtilidadesHistoriaClinica.obtenerCentroAtencionCuenta(con, paciente.getCodigoCuenta());
	     HashMap criterios = new HashMap ();
	     criterios.put("consecutivo", centroAten.getCodigo());
	     HashMap tmp=Utilidades.obtenerDatosCentroAtencion(con, criterios);
	     logger.info("\n centro aten -->"+centroAten);
       	if (UtilidadCadena.noEsVacio(tmp.get("direccion")+""))
       		v.add(tmp.get("direccion")+"");        				
       	else
       		v.add(institucionBasica.getDireccion());
       /*-----------------------------------------------------------*/
       
        
        
        v.add("Tels. "+institucionBasica.getTelefono());
        comp.insertGridHeaderOfMasterPage(0,1,1,v.size());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        //en la segunda celda se coloca el logo de la institucion
        comp.insertImageHeaderOfMasterPage1(0, 2, institucionBasica.getLogoReportes());
        
        if(!UtilidadTexto.isEmpty(usuarioGeneraSolicitud.getFirmaDigital()))
			comp.insertImageBodyPage(0, 0, usuarioGeneraSolicitud.getPathFirmaDigital(), "grillaFirmaDigital");
        
        comp.insertLabelBodyPage(0, 0, institucionBasica.getPieHistoriaClinica(), "piehiscli");
		
        
        
           
        
        if(forma.getConsecutivosOrdenesInsertadas1().size()<=0)
        	forma.getConsecutivosOrdenesInsertadas1().add(forma.getNumeroOrden());
        
        comp.obtenerComponentesDataSet("servicios");
        String newQuery= ConsultasBirt.modificarConsultaOrdenesAmbServicios("", forma.getConsecutivosOrdenesInsertadas1(), usuario.getCodigoInstitucionInt());
        //comp.modificarQueryDataSet(newQuery);
        
        comp.obtenerComponentesDataSet("encabezado");            
        String oldQuery=comp.obtenerQueryDataSet();
        newQuery= oldQuery.replace("remplaceXNumerosOrden", UtilidadTexto.convertirVectorACodigosSeparadosXComas(forma.getConsecutivosOrdenesInsertadas1(), false));
        comp.modificarQueryDataSet(newQuery);
        
        
      
    	//comp.insertGridHeaderOfMasterPage(0,1,1,4);
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
		newPathReport+="&usuario="+usuario.getLoginUsuario()+"&tipoimp="+tipoImpresion+"&consecutivo_orden="+forma.getNumeroOrden()+"&institucion="+usuario.getCodigoInstitucion();
        if(!newPathReport.equals(""))
        {
            request.setAttribute("isOpenReport", "true");
            request.setAttribute("newPathReport", newPathReport);
        }            
        
        return newPathReport;
	}

	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param mundo 
	 * @param paciente 
	 * @param institucionActual 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private ActionForward accionImprimirServicioCartaCompleta(Connection con, OrdenesAmbulatoriasForm forma, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, OrdenesAmbulatorias mundo, PersonaBasica paciente, InstitucionBasica institucionActual) 
	{
		String nombreArchivo;
    	Vector archivosGenerados = new Vector();
		
    	UsuarioBasico usuarioGeneraSolicitud= new UsuarioBasico();
    	String login = Utilidades.getLoginUsuarioSolicitaOrdenAmbulatoria(con, forma.getNumeroOrden(), usuario.getCodigoInstitucionInt());
    	if(!login.equals(""))
    	{
    		try 
    		{
				usuarioGeneraSolicitud.cargarUsuarioBasico(con, login);
			} 
    		catch (SQLException e) 
    		{
				e.printStackTrace();
			}
    	}	
    	
    	
    	if(!forma.getNumeroOrden().trim().equals(""))
    	{
    		if(forma.getEstado().equals("imprimirOrdenServicios"))
    			forma.setServicios(mundo.consultarServiciosOrdenAmbulatoria(con,forma.getNumeroOrden(), usuario.getCodigoInstitucionInt()));
    	}
    	
    	// Copia
    	Random r = new Random();
    	/*nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
    	logger.info("num Orden->"+forma.getNumeroOrden());
    	try
    	{
    	    OrdenesAmbulatoriasServiciosPdf.pdf(ValoresPorDefecto.getFilePath() + nombreArchivo, forma, usuarioGeneraSolicitud, paciente, institucionActual, "Copia");
    	    archivosGenerados.add(nombreArchivo);
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error de tamaño generando PDF de Ordenes Ambulatorias Servicios", "errors.pdfSuperoMemoria", true);
    	}
    	
    	// Original
    	r = new Random();*/
    	nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
    	try
    	{
    	    OrdenesAmbulatoriasServiciosPdf.pdf(con, ValoresPorDefecto.getFilePath() + nombreArchivo, forma, usuarioGeneraSolicitud, paciente, institucionActual, "Original");
    	    archivosGenerados.add(nombreArchivo);
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error de tamaño generando PDF de Ordenes Ambulatorias Servicios", "errors.pdfSuperoMemoria", true);
    	}

        //request.setAttribute("nombreArchivo", nombreArchivo);
        request.setAttribute("nombreVentana", "Orden Ambulatoria de Servicios");
        request.setAttribute("archivos", archivosGenerados);
        return mapping.findForward("abrirNPdf");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ActionForward accionNuevo(Connection con, OrdenesAmbulatoriasForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario)
	{
		ActionErrors errores = new ActionErrors();
		if(!UtilidadValidacion.pacienteTieneIngresoAbierto(con, forma.getCodigoPersona()))
		{
			String ultimaCuenta = String.valueOf(Utilidades.obtenerIdUltimaCuenta(con, forma.getCodigoPersona()));
			String fechaEgreso = "";
			int ultimaViaIngreso = Utilidades.convertirAEntero(Utilidades.obtenerViaIngresoCuenta(con, ultimaCuenta)+"");
			
			if(ultimaViaIngreso==ConstantesBD.codigoViaIngresoAmbulatorios || ultimaViaIngreso==ConstantesBD.codigoViaIngresoConsultaExterna)
				fechaEgreso=UtilidadValidacion.obtenerFechaFacturaCuenta(con, forma.getCodigoPersona());
			else
				fechaEgreso=UtilidadValidacion.obtenerFechaEgresoUltimoIngresoPaciente(con, forma.getCodigoPersona());
			
			String numDiasEgres=ValoresPorDefecto.getNumDiasEgresoOrdenesAmbulatorias(usuario.getCodigoInstitucionInt());
			if(!UtilidadTexto.isEmpty(numDiasEgres))
			{
				int numDias=UtilidadFecha.numeroDiasEntreFechas(fechaEgreso,UtilidadFecha.getFechaActual());
				if(Integer.parseInt(numDiasEgres)<numDias)
				{
					errores.add("", new ActionMessage("error.ordenAmbulatoria.pacienteSinAtencionEnNDias",numDiasEgres));
					saveErrors(request, errores);
	                UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaErroresActionErrors"); 
				}
			}
		}
		forma.resetNuevo();
		return mapping.findForward("nuevo");
	}

	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param response
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param paciente
	 * @param errores
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	private ActionForward accionGuardarModOrdenArticulos(Connection con,OrdenesAmbulatoriasForm forma, OrdenesAmbulatorias mundo,HttpServletResponse response, ActionMapping mapping,HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente, ActionErrors errores) throws IPSException {
		ManejoPacienteFacade manejoPacienteFacade = null;
		OrdenesFacade ordenesFacade = null;
		List<AutorizacionCapitacionDto> listaAutorizacionesCapitacion = null;
		List<AutorizacionPorOrdenDto> listaAutorizacionesPorOrdenExistentes = null;
		boolean transaccion = false;
		int contratoConvenio = ConstantesBD.codigoNuncaValido;
		boolean esModificacion = false;
		
		forma.setProcesoExitoso(false);
			
		try{
			//verifica halla existido modificacion en articulos 
			for(int i=0 ; i < Integer.parseInt(forma.getArticulos("numRegistros").toString()) ; i++){
				if((!UtilidadTexto.getBoolean(forma.getArticulos("estabd_"+i).toString()))
						|| (UtilidadTexto.getBoolean(forma.getArticulos("fueEliminadoArticulo_"+i).toString())) 
						|| (forma.getArticulosModificacion().get("duraciontratamiento_"+i) != null && forma.getArticulos("duraciontratamiento_"+i) != null && !forma.getArticulos("duraciontratamiento_"+i).toString().equals(forma.getArticulosModificacion().get("duraciontratamiento_"+i).toString()))
						|| (forma.getArticulosModificacion().get("tipofrecuencia_"+i) != null && forma.getArticulos("tipofrecuencia_"+i) != null && !forma.getArticulos("tipofrecuencia_"+i).toString().equals(forma.getArticulosModificacion().get("tipofrecuencia_"+i).toString()))
						|| (forma.getArticulosModificacion().get("cantidad_"+i) != null && forma.getArticulos("cantidad_"+i) != null && !forma.getArticulos("cantidad_"+i).toString().equals(forma.getArticulosModificacion().get("cantidad_"+i).toString()))		
						|| (forma.getArticulosModificacion().get("observaciones_"+i) != null && forma.getArticulos("observaciones_"+i) != null && !forma.getArticulos("observaciones_"+i).toString().equals(forma.getArticulosModificacion().get("observaciones_"+i).toString()))		
						|| (forma.getArticulosModificacion().get("dosis_"+i) != null && forma.getArticulos("dosis_"+i)!= null && !forma.getArticulos("dosis_"+i).toString().equals(forma.getArticulosModificacion().get("dosis_"+i).toString()))	
						|| (forma.getArticulosModificacion().get("frecuencia_"+i) != null && forma.getArticulos("frecuencia_"+i) != null && !forma.getArticulos("frecuencia_"+i).toString().equals(forma.getArticulosModificacion().get("frecuencia_"+i).toString()))
						|| (forma.getArticulosModificacion().get("unidosis_"+i) != null && forma.getArticulos("unidosis_"+i) != null && !forma.getArticulos("unidosis_"+i).toString().equals(forma.getArticulosModificacion().get("unidosis_"+i).toString()))){
					esModificacion = true;
					break;
		}
	}
	
			if(esModificacion){	
	
				String codigoOrden = Utilidades.obtenerCodigoOrdenAmbulatoria(con,forma.getNumeroOrden(), usuario.getCodigoInstitucionInt());
			
				manejoPacienteFacade = new ManejoPacienteFacade();
				ordenesFacade = new OrdenesFacade();
				
				//valida que existan ordenes con Autorizaciones en estado Autorizada	
				List<String> estados = new ArrayList<String>();
				estados.add(ConstantesIntegridadDominio.acronimoAutorizado);
				listaAutorizacionesPorOrdenExistentes = ordenesFacade.obtenerAutorizacionCapitacion(ConstantesBD.claseOrdenOrdenAmbulatoria, ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos, Long.parseLong(codigoOrden), estados);
				
				//Si algun articulo trae contratoconvenio ese se utiliza para todos en la modificacion
				for(int i=0 ; i < Integer.parseInt(forma.getArticulos("numRegistros").toString()) ; i++){
					if(forma.getArticulos("contratoconvenio_"+i) != null && !forma.getArticulos("contratoconvenio_"+i).toString().isEmpty()){
						contratoConvenio = Integer.parseInt(forma.getArticulos("contratoconvenio_"+i).toString());
						break;
					}
				}
			
				//Se setea cubierto 'Si' y contratoConvenio a los articulos nuevos en espera del nuevo desarrollo
				int codigoViaIngreso= Cuenta.obtenerCodigoViaIngresoCuenta(con, forma.getCuentaSolicitante()+"");
				String tipoPaciente = UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(forma.getCuentaSolicitante()+"").getAcronimo();
				for(int i=0 ; i < Integer.parseInt(forma.getArticulos("numRegistros").toString()) ; i++){
					if( !UtilidadTexto.getBoolean(forma.getArticulos("fueEliminadoArticulo_"+i)+"")
							//&& !UtilidadTexto.getBoolean(forma.getArticulos("estabd_"+i)+"")
							){
						
							/*
							 * MT 5880, debe evaluar la cobertura para todos los articulos 
							 * segun DCU 437 v1.3 - PROCESO GENERAL, DE LA VALIDACIÓN DE COBERTURA DE SERVICIOS ARTÍCULOS
							 * incluyendo los que existen desde la creacion de la solicitud como los que se agregan en la modificacion
							 * 
							 * jeilones
							 */
							InfoResponsableCobertura infoResponsableCobertura= Cobertura.validacionCoberturaArticulo(con, paciente.getCodigoIngreso()+"", codigoViaIngreso,tipoPaciente,Integer.parseInt(forma.getArticulos("articulo_"+i).toString()) , usuario.getCodigoInstitucionInt(),forma.isPyp());
							forma.getArticulos().put("cubierto_"+i, infoResponsableCobertura.getInfoCobertura().getIncluidoStr());
							forma.getArticulos().put("contrato_convenio_"+i, infoResponsableCobertura.getDtoSubCuenta().getContrato());
							//forma.getArticulos().put("cubierto_"+i, ConstantesBD.acronimoSi);
							//forma.getArticulos().put("contrato_convenio_"+i, contratoConvenio);
					}
				}
	
				cargarMundoDesdeForm(forma,mundo,usuario,paciente,request);
				
				List<DtoValidacionGeneracionAutorizacionCapitada> listaValidacionGeneracionAutorizacionCapitada = new ArrayList<DtoValidacionGeneracionAutorizacionCapitada>();
	
				//Si no tiene asociada Autorizacion de Capitacion Subcontratada 
				if(listaAutorizacionesPorOrdenExistentes == null || listaAutorizacionesPorOrdenExistentes.isEmpty()){
					
					transaccion = mundo.updateOrdenAmbulatoriaArticulos(con, forma.getArticulos(), codigoOrden);
						
					if(transaccion){
						listaValidacionGeneracionAutorizacionCapitada = this.cargarDatosValidacionGeneracionAutorizacionCapitada(forma, con, mundo, paciente, Integer.parseInt(codigoOrden), contratoConvenio, usuario);
						listaAutorizacionesCapitacion = mundo.generarAutorizacionCapitacion(paciente, con, usuario, request, listaValidacionGeneracionAutorizacionCapitada);
					}	
						
				}
				//Si tiene asociada autorizacion de capitacion subcontratada
				else {
					
					boolean sinAutEntSubContratada = true;
					
					for(AutorizacionPorOrdenDto autorizacionPorOrden : listaAutorizacionesPorOrdenExistentes){
						//Si la autorizacion de entidad subcontratada es autorizada
						if(autorizacionPorOrden.getConsecutivoAutorizacion() != null && !autorizacionPorOrden.getConsecutivoAutorizacion().isEmpty()){
							errores.add("", new ActionMessage("errors.autorizacion.ordenAmbEntidadSubContratadaAsoc"));
							sinAutEntSubContratada = false;
							break;
						}
					}		
					
					if(sinAutEntSubContratada){
						
						transaccion = mundo.updateOrdenAmbulatoriaArticulos(con, forma.getArticulos(), codigoOrden);
						
						if(transaccion){
						
							AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto= new AnulacionAutorizacionSolicitudDto();
							anulacionAutorizacionDto.setMotivoAnulacion(MOTIVO_ANULACION);
							anulacionAutorizacionDto.setFechaAnulacion(UtilidadFecha.getFechaActualTipoBD());
							anulacionAutorizacionDto.setHoraAnulacion(UtilidadFecha.getHoraActual());
							anulacionAutorizacionDto.setLoginUsuarioAnulacion(usuario.getLoginUsuario());
							
							boolean anulacionExitosa = false;
							anulacionExitosa = ordenesFacade.procesoAnulacionAutorizacion(ConstantesBD.claseOrdenOrdenAmbulatoria, ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos, listaAutorizacionesPorOrdenExistentes, anulacionAutorizacionDto, usuario.getCodigoInstitucionInt());
	
							if(anulacionExitosa){
								listaValidacionGeneracionAutorizacionCapitada = this.cargarDatosValidacionGeneracionAutorizacionCapitada(forma, con, mundo, paciente, Integer.parseInt(codigoOrden), contratoConvenio, usuario);
								listaAutorizacionesCapitacion = mundo.generarAutorizacionCapitacion(paciente, con, usuario, request, listaValidacionGeneracionAutorizacionCapitada);
							} else {
								errores.add("", new ActionMessage("errors.autorizacion.noSeAnulaOrden"));
							}	
						}
					}
					//Si tiene Autorizacion de Entidad Subcontratada
					else{
						saveErrors(request, errores);
						return this.accionDetalleOrden(con, forma, mundo, mapping, paciente,usuario, request);
					}		
				}
					
				if(listaAutorizacionesCapitacion!=null && !listaAutorizacionesCapitacion.isEmpty()){//Se adiciona mensaje para los articulos que no se autorizaron
					manejoPacienteFacade.obtenerMensajesError(listaAutorizacionesCapitacion, errores);
				}
	
				if(transaccion){	
					forma.setProcesoExitoso(true);
				}else {
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en el actualizar las Ordenes Ambulatorias Artículos", "error.ordenAmbulatoria.actualizarOrden", true);
				}
				
			}
			
		}catch (IPSException ipse) {
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(ipse.getMessage(), ipse);
			throw ipse;
		}
		catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
		
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		
		return this.accionDetalleOrden(con, forma, mundo, mapping, paciente,usuario, request);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param response
	 * @return
	 */
	private ActionForward accionGuardarModificacionesDetalleOrdenArticulos(Connection con, OrdenesAmbulatoriasForm forma, OrdenesAmbulatorias mundo, HttpServletResponse response, ActionMapping mapping, 	HttpServletRequest request, UsuarioBasico usuario)
			{
		try {
				response.sendRedirect("../solicitarMedicamentosDummy/solicitarMedicamentosDummy.do?estado=insertarOrdenAmbulatoria&ordenAmbulatoria="+forma.getNumeroOrden()+"&indicativoOrdenAmbulatoria=true&solPYP="+forma.isPyp()+"&acronimoDiagnostico="+forma.getAcronimoDiagnostico()+"&tipoCieDiagnostico="+forma.getTipoCieDiagnostico());
				//response.sendRedirect("../solicitarMedicamentosDummy/solicitarMedicamentosDummy.do?estado=insertarOrdenAmbulatoria&ordenAmbulatoria="+forma.getNumeroOrden()+"&indicativoOrdenAmbulatoria=true&solPYP="+forma.isPyp());
		} catch (IOException e) {
				logger.error("error->"+e.toString());
			}
			return null;
		}

	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ActionForward accionResumen(Connection con, String codigoOrden, OrdenesAmbulatoriasForm forma, OrdenesAmbulatorias mundo, UsuarioBasico usuario,  ActionMapping mapping, ActionErrors errores, HttpServletRequest request) 
	{
		
		if(Integer.parseInt(forma.getTipoOrden())==ConstantesBD.codigoTipoOrdenAmbulatoriaServicios)
		{
			/*if(mundo.getDtoValidaciones() != null && !mundo.getDtoValidaciones().isEmpty()){
				List<String> validacionServicios= new ArrayList<String>();
				ArrayList<DtoValidacionGeneracionAutorizacionCapitada> dtoValidacionesServicios = new ArrayList<DtoValidacionGeneracionAutorizacionCapitada>();
				for(DtoValidacionGeneracionAutorizacionCapitada dtoValidacion:mundo.getDtoValidaciones()){
					if(dtoValidacion.getListaAdvertencias() != null && !dtoValidacion.getListaAdvertencias().isEmpty()){
						validacionServicios.add(dtoValidacion.getListaAdvertencias().get(0));
					}
					else{
						if(dtoValidacion.getProcesoAutorizacion() != null && dtoValidacion.getProcesoAutorizacion().getListaAutorizaciones() != null
								&& !dtoValidacion.getProcesoAutorizacion().getListaAutorizaciones().isEmpty()){
							dtoValidacionesServicios.add(dtoValidacion);
						} 
					}
				}
				if(!validacionServicios.isEmpty()){
					 errores.add("Servicios No autorizados",
								new ActionMessage("prompt.generico", messageResource.getMessage(
										"ordenesAmbulatorias.mensajeServiciosAutorizarManual")));
					 for(String mensaje:validacionServicios){
						 errores.add("Servicio No autorizado",
									new ActionMessage("prompt.generico", mensaje));
					 }
					saveErrors(request, errores);
					forma.setPendienteAutorizacion(true);//
				}
				if(!dtoValidacionesServicios.isEmpty()){
					 forma.setListaValidacionesAutorizaciones(dtoValidacionesServicios);
					 forma.setImprimirAutorizacion(true);
				}
				else{
					 forma.setImprimirAutorizacion(false);
				}
			}*/
			saveErrors(request, errores);
			forma.setPendienteAutorizacion(true);
			return mapping.findForward("resumenOrdenServicios"); 
		}
		else
		{
			/*if(mundo.getDtoValidaciones() != null && !mundo.getDtoValidaciones().isEmpty()){
				DtoValidacionGeneracionAutorizacionCapitada dtoValidacion=mundo.getDtoValidaciones().get(0);
				List<String> validacionArticulos=new ArrayList<String>();
				ArrayList<DtoValidacionGeneracionAutorizacionCapitada> dtoValidacionesArticulos = new ArrayList<DtoValidacionGeneracionAutorizacionCapitada>();
				if(dtoValidacion.getListaAdvertencias() != null && !dtoValidacion.getListaAdvertencias().isEmpty()){
					for(String advertencia:dtoValidacion.getListaAdvertencias()){
						validacionArticulos.add(advertencia);
					}
				}
				else{
					if(dtoValidacion.getProcesoAutorizacion() != null && dtoValidacion.getProcesoAutorizacion().getListaAutorizaciones() != null
							&& !dtoValidacion.getProcesoAutorizacion().getListaAutorizaciones().isEmpty()){
						dtoValidacionesArticulos.add(dtoValidacion);
					} 
				}
				if(!validacionArticulos.isEmpty()){
					 errores.add("Articulos No autorizados",
								new ActionMessage("prompt.generico", messageResource.getMessage(
										"ordenesAmbulatorias.mensajeArticulosAutorizarManual")));
					 for(String mensaje:validacionArticulos){
						 errores.add("Articulo No autorizado",
									new ActionMessage("prompt.generico", mensaje));
					 }
					 saveErrors(request, errores);
					 forma.setPendienteAutorizacion(true);//
				}
				if(!dtoValidacionesArticulos.isEmpty()){
					 forma.setListaValidacionesAutorizaciones(dtoValidacionesArticulos);
					 forma.setImprimirAutorizacion(true);
				}
				else{
					 forma.setImprimirAutorizacion(false);
				}
			}*/
			saveErrors(request, errores);
			forma.setPendienteAutorizacion(true);
			cargarResumenArticulos(con, codigoOrden, forma, mundo, usuario);
			return mapping.findForward("detalleArticulo");
		}
	}

	/**
	 * 
	 * @param codigoOrden
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	@SuppressWarnings("rawtypes")
	private void cargarResumenArticulos(Connection con, String codigoOrden, OrdenesAmbulatoriasForm forma, OrdenesAmbulatorias mundo, UsuarioBasico usuario)
	{
		mundo.consultarOrdenesAmbulatoriasXCodigoOrden(con, codigoOrden, usuario.getCodigoInstitucion());
		forma.setOrdenes((HashMap)mundo.getOrdenes().clone());
		//ell indice va ha ser cero
		forma.setIndex("0");
		this.cargarInformaionGenerarToForm(con,forma,mundo);
		mundo.cargarDetalleOrdenArticulos(con,codigoOrden);
		forma.setArticulos((HashMap)mundo.getArticulos().clone());
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	@SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	private void accionGuardarSolicitar(Connection con, OrdenesAmbulatoriasForm forma, OrdenesAmbulatorias mundo, UsuarioBasico usuario) 
	{
		String codigoOrden=Utilidades.obtenerCodigoOrdenAmbulatoria(con,forma.getNumeroOrden(),usuario.getCodigoInstitucionInt());
		HashMap vo=new HashMap();
		vo.put("estadoOrden",ConstantesBD.codigoEstadoOrdenAmbulatoriaSolicitada+"");
		vo.put("numeroSolicitud",forma.getNumeroSolicitud()+"");
		vo.put("numeroOrden",codigoOrden+"");
		vo.put("usuario",usuario.getLoginUsuario());
		
		//Actualiza el estado de la orden
		mundo.actualizarSolicitudEnOrdenAmbulatoria(con,vo);	
		
		if(forma.isPyp())
		{
			mundo.actualizarEstadoActividadProgramaPYPPAcienteNumOrden(con,codigoOrden,ConstantesBD.codigoEstadoProgramaPYPSolicitado,usuario.getLoginUsuario(),"");
			Utilidades.asignarSolicitudToActividadPYP(con,codigoOrden,forma.getNumeroSolicitud());
		}
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionIngresarOrden(OrdenesAmbulatoriasForm forma, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//por la funcionalidad ordenes ambulatorias no son de pyp.
		forma.setPyp(false);
		if(forma.getTipoOrden().equals(ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos+""))
		{
		    return mapping.findForward("nuevoOrdenArticulos");
		}
		else if(forma.getTipoOrden().equals(ConstantesBD.codigoTipoOrdenAmbulatoriaServicios+""))
		{
			forma.setManejoProgramacionSalas(ValoresPorDefecto.getManejoProgramacionSalasSolicitudesDyt(usuario.getCodigoInstitucionInt()));
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//se adiciona por tarea 2567
			forma.setMostrarMensaje(ValoresPorDefecto.getManejoProgramacionSalasSolicitudesDyt(usuario.getCodigoInstitucionInt()));
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			return mapping.findForward("nuevoOrdenServicios");
		}
		return null;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionIngresarServicio(OrdenesAmbulatoriasForm forma, ActionMapping mapping) 
	{
		forma.setServicios("numRegistros", forma.getNumeroFilasMapaCasoServicios()+"");
		return mapping.findForward("nuevoOrdenServicios");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionIngresarArticulo(OrdenesAmbulatoriasForm forma, ActionMapping mapping) 
	{
		forma.setArticulos("numRegistros", forma.getNumeroFilasMapa()+"");
		return mapping.findForward("nuevoOrdenArticulos");
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionIngresarArticuloDetalle(OrdenesAmbulatoriasForm forma, ActionMapping mapping) 
	{
		forma.setArticulos("numRegistros", forma.getNumeroFilasMapa()+"");
		return mapping.findForward("detalleArticulo");
	}
	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenar(OrdenesAmbulatoriasForm forma) 
	{
		int numReg=Integer.parseInt(forma.getOrdenes("numRegistros")==null?"0":forma.getOrdenes("numRegistros")+"");
		String[] indices={"codigo_","numero_","fecha_","fechabd_","hora_","descripcion_","cantidad_","servicio_","tipoorden_","nomtipoorden_","urgente_","centrosolicita_","profesional_","especialidad_","observaciones_","consultaexterna_","estado_","descestado_","pyp_","numsolicitud_","consesolicitud_", "nombrecentrocostosolicita_","otros_",OrdenesAmbulatoriasAction.KEY_DETALLE_MEDICAMENTOS,OrdenesAmbulatoriasAction.KEY_DETALLE_INSUMO};
		forma.setOrdenes(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getOrdenes(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setOrdenes("numRegistros",numReg+"");
	}


	/**
	 * Método que se encarga de anular la orden ambulatoria
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	@SuppressWarnings({ "deprecation" })
	private void accionAnular(Connection con, OrdenesAmbulatoriasForm forma, UsuarioBasico usuario,
			ActionErrors errores, HttpServletRequest request)throws IPSException 
	{
		try{
			/**ANULACION ORDEN AMBULATORIA Y AUTORIZACION---------------*/
			cargarInfoParaAnulacionAutorizacion(forma, usuario,errores);
			forma.setProcesoExitosoAnulacion(true);
			
		}catch(IPSException ipse){
			Log4JManager.error(ipse);
			errores.add("ERROR Negocio", new ActionMessage(ipse.getErrorCode().toString(),ipse.getParamsMsg()));
		}
		catch(Exception e){
			Log4JManager.error(e);
			errores.add("ERROR no Controlado", new ActionMessage("errors.notEspecific", e.getMessage()));
		}finally{
			if(!errores.isEmpty()){
				saveErrors(request, errores);
			}
		}
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void accioningresarResultado(Connection con, OrdenesAmbulatoriasForm forma, OrdenesAmbulatorias mundo, UsuarioBasico usuario)
	{
		HashMap vo=new HashMap();
		vo.put("codigo",forma.getOrdenes("codigo_"+forma.getIndex()));
		vo.put("fecha",UtilidadFecha.getFechaActual());
		vo.put("hora",UtilidadFecha.getHoraActual());
		vo.put("usuario",usuario.getLoginUsuario());
		vo.put("resultado",forma.getResultado());
		if(mundo.responderOrdenAmbulatorio(con,vo) && UtilidadTexto.getBoolean(forma.getOrdenes("pyp_"+forma.getIndex())+""))
		{
			mundo.actualizarEstadoActividadProgramaPYPPAcienteNumOrden(con,forma.getOrdenes("codigo_"+forma.getIndex())+"",ConstantesBD.codigoEstadoProgramaPYPCancelado,usuario.getLoginUsuario(),forma.getResultado());
		}
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
	@SuppressWarnings({ "rawtypes", "deprecation" })
	private ActionForward accionDetalleOrden(Connection con, OrdenesAmbulatoriasForm forma, OrdenesAmbulatorias mundo, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) 
	{
		forma.setMensaje(new ResultadoBoolean(false));
		forma.setConsecutivosOrdenesInsertadas1(new Vector<String>());
		forma.setEsPreingreso(Utilidades.esPreingresoNormal(con,paciente.getCodigoIngreso()));
		forma.setErrorOrdenCaduca(0);
		logger.info("VARIABLE CUANDO ES UN PREINGRESO>>>>>>>>>>>>>>>>"+forma.isEsPreingreso());
		logger.info("--->"+forma.getOrdenes("ccsolicita_"+forma.getIndex())+"<--");
		logger.info("--->"+forma.getOrdenes("cuenta_"+forma.getIndex())+"<--");
		logger.info("--->"+forma.getOrdenes("ingreso_"+forma.getIndex())+"<--");
		logger.info("--->"+forma.getOrdenes("codigo_"+forma.getIndex())+"<--");
		this.cargarInformaionGenerarToForm(con,forma,mundo);
		int numeroDiasentreFechaSolyActual=0;
		int numMaxDiasGenSolServ=0;

		forma.setCentroCostoSolicitante(Utilidades.convertirAEntero(forma.getOrdenes("ccsolicita_"+forma.getIndex())+""));
		forma.setCuentaSolicitante(Utilidades.convertirAEntero(forma.getOrdenes("cuenta_"+forma.getIndex())+""));
		forma.setIdIngreso(Utilidades.convertirAEntero(forma.getOrdenes("ingreso_"+forma.getIndex())+""));
		forma.setCodigoOrden(Utilidades.convertirAEntero(forma.getOrdenes("codigo_"+forma.getIndex())+""));
		/**
		* Tipo Modificacion: Segun incidencia MT6636 ADICIONAL
		* Autor: Jesús Darío Ríos
		* usuario: jesrioro
		* Fecha: 13/03/2013
		* Descripcion: 	Se  realiza  la  validacion de  esta  variable  para  flujo  de
		* 				manejo del paciente/autorizaciones/administrarAutorizacionCapitacion/administrarAutorizacionCapitacionPaciente.jsp
		* 				ya  que  generaba  un error al  mostrar la orden ambulatoria.   
		***/
		if(forma.getArticulosModificacion()!=null){
			forma.getArticulosModificacion().clear();
		}
		//MT6636--->ADICIONAL
		
		if(Integer.parseInt(forma.getTipoOrden())==ConstantesBD.codigoTipoOrdenAmbulatoriaServicios)
		{
			numeroDiasentreFechaSolyActual= UtilidadFecha.numeroDiasEntreFechas(UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaOrden()), UtilidadFecha.getFechaActual());
			numMaxDiasGenSolServ= Utilidades.convertirAEntero(ValoresPorDefecto.getNumeroMaximoDiasGenOrdenesAmbServicios(usuario.getCodigoInstitucionInt()));
			
			logger.info("\n\nnumeroDiasentreFechaSolyActual"+numeroDiasentreFechaSolyActual+"\n\nnumMaxDiasGenSolServ"+numMaxDiasGenSolServ);
								
			if(numeroDiasentreFechaSolyActual > numMaxDiasGenSolServ)
			{	
				forma.setErrorOrdenCaduca(numeroDiasentreFechaSolyActual);	
			}
			try
			{	
				forma.setActivarBotonGenSolOrdAmbulatoria(ValoresPorDefecto.getActivarBotonGenerarSolicitudOrdenAmbulatora(usuario.getCodigoInstitucionInt()));
				forma.setExisteEgreso(UtilidadValidacion.existeEgresoCompleto(con, paciente.getCodigoCuenta()));
				logger.info("===>Existe Egreso en Servicios: "+forma.isExisteEgreso());
				return mapping.findForward("detalleServicio");
			}
			catch (SQLException e) 
			{
				logger.error("ERROR OBTENIENDO EL EGRESO PARA SERVICIOS");
				e.printStackTrace();
				return mapping.findForward("detalleServicio");
			}
		}
		else
		{
			try
			{	
				
				//----------------------------------------------------------------------------------------------------------
				//existe Autorización asociada a Orden Ambulatoria RQF 02-0025 Autorizaciones Capitación Subcontratada
				ActionErrors errores=new ActionErrors();
				HibernateUtil.beginTransaction();
				IAutorizacionCapitacionOrdenesAmbulatoriasMundo autorizacionCapitacionOrdenesAmbulatoriasMundo=CapitacionFabricaMundo.crearAutorizacionCapitacionOrdenesAmbulatoriasMundo();
				ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria> listaAutorizaciones = new ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria>();
				DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizCapitOrdenAmbulatoria=new DtoAutorizacionCapitacionOrdenAmbulatoria();
				dtoAutorizCapitOrdenAmbulatoria.getDtoOrdenesAmbulatorias().setNumeroOrden(forma.getNumeroOrden());
				//FIXME retorna lista de las ordenes autorizadas previamente
				listaAutorizaciones = autorizacionCapitacionOrdenesAmbulatoriasMundo.existeAutorizacionesOrdenAmbul(dtoAutorizCapitOrdenAmbulatoria);
				
				if(listaAutorizaciones!=null && !listaAutorizaciones.isEmpty())
				{
					for(DtoAutorizacionCapitacionOrdenAmbulatoria autorizCapitaAmbul:listaAutorizaciones)
					{
						if(autorizCapitaAmbul.getEstadoArticulo().equals(ConstantesIntegridadDominio.acronimoEstadoEntregado))
						{//Existe autorización de medicamentos con estado Entregado entonces no se puede generar solicitud
							forma.setActivarBotonGenSolOrdAmbulatoria(ConstantesBD.acronimoNoChar+"");
							errores.add("", new ActionMessage(messageResource.getMessage("ordenesAmbulatorias.ordenAmbConEntregaMedicamentos")));
							saveErrors(request, errores);
							return mapping.findForward("detalleArticulo");
						}
					}
				}//----------------------------------------------------------------------------------------------------------
				
				mundo.cargarDetalleOrdenArticulos(con,forma.getOrdenes("codigo_"+forma.getIndex())+"");
				forma.setArticulos((HashMap)mundo.getArticulos().clone());
				forma.setArticulosModificacion((HashMap<String, Object>)mundo.getArticulos().clone());
				forma.setNumeroFilasMapa(Integer.parseInt(forma.getArticulos("numRegistros").toString()));
				forma.setExisteEgreso(UtilidadValidacion.existeEgresoCompleto(con, paciente.getCodigoCuenta()));
				logger.info("===>Existe Egreso en Articulos: "+forma.isExisteEgreso());
				forma.setActivarBotonGenSolOrdAmbulatoria(ValoresPorDefecto.getActivarBotonGenerarSolicitudOrdenAmbulatora(usuario.getCodigoInstitucionInt()));
				HibernateUtil.endTransaction();
				return mapping.findForward("detalleArticulo");
			}
			catch (SQLException e)
			{
				Log4JManager.error("ERROR OBTENIENDO EL EGRESO PARA ARTICULOS");
				HibernateUtil.abortTransaction();
				return mapping.findForward("detalleArticulo");
			}
		}
	}
	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param paciente 
	 * @param request 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void cargarMundoDesdeForm(OrdenesAmbulatoriasForm forma,OrdenesAmbulatorias mundo, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request)
	{
		mundo.setNumeroOrden(forma.getNumeroOrden());
		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
		mundo.setCodigoPaciente(paciente.getCodigoPersona()+"");
		mundo.setTipoOrden(forma.getTipoOrden());
		mundo.setPyp(forma.isPyp());
		mundo.setUrgente(forma.isUrgente());
		mundo.setCentroAtencion(forma.getCentroAtencion());
		mundo.setProfesional(forma.getProfesional());
		mundo.setLoginUsuario(usuario.getLoginUsuario());
		mundo.setEspecialidad(forma.getEspecialidad());
		mundo.setFechaOrden(forma.getFechaOrden());
		mundo.setHora(forma.getHora());
		mundo.setObservaciones(forma.getObservaciones());
		mundo.setEstadoOrden(forma.getEstadoOrden());
		mundo.setConsultaExterna(forma.isConsultaExterna());
		mundo.setNumeroSolicitud(forma.getNumeroSolicitud());
		mundo.setUsuarioConfirma(forma.getUsuarioConfirma());
		mundo.setFechaConfirma(forma.getUsuarioConfirma());
		mundo.setHoraConfirma(forma.getHoraConfirma());
		//esto solo aplica cuando es solicitud tipo servicio, sino se cargar en ""
		//mundo.setServicio(forma.getServicio());
		mundo.setTipoServicio(forma.getTipoServicio());
		mundo.setServicios(forma.getServicios());
		
		//mundo.setFinalidadServicio(forma.getFinalidadServicio());
		//mundo.setCantidad(forma.getCantidad());
		//solo para articulos, sino carga el mapa vacio.
		mundo.setArticulos(forma.getArticulos());
		//cambio en el documento 919, se debe asociar como centro de costo que solicita, el centro de costo de la ultima cuenta del paciente.
		//mundo.setCentroCostoSolicita(usuario.getCodigoCentroCosto()+"");
		mundo.setOtros(forma.getOtros());
		mundo.setControlEspecial(forma.getCheckCE());
		
		// *************************** JUSTIFICACIÓN NO POS *************************
		//Servicios
		//HashMap mapaJus=(HashMap) request.getSession().getAttribute("MAPAJUSSERV");
		//if(mapaJus!=null){
		  mundo.setJustificacionesServicios(forma.getJustificacionesServicios());
		  
		  HashMap mapaJus=new HashMap();
			int numReg=Utilidades.convertirAEntero(mundo.getServicios().get("numRegistros")==null||(mundo.getServicios().get("numRegistros")+"").equals("")?"0":mundo.getServicios().get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				//mapaJus.put(mundo.getServicios().get("codigo_"+i)+"_pendiente",forma.getJustificacionMap().get(mundo.getServicios().get("codigo_"+i)+"_pendiente"));
				//mapaJus.put(mundo.getServicios().get("codigo_"+i)+"_mapasecciones",request.getSession().getAttribute("MAPASECJUSSERV"));
				mapaJus.put(mundo.getServicios().get("codigo_"+i)+"_mapajustificacion",request.getSession().getAttribute(mundo.getServicios().get("codigo_"+i)+"MAPAJUSSERV"));
			}
			
			mundo.setJustificacionesServicios(mapaJus);
		/*	request.getSession().removeAttribute("MAPAJUSSERV");
		}else{
			mundo.setJustificacionesServicios(forma.getJustificacionesServicios());
		}*/
		//Articulos
		/*mapaJus=(HashMap) request.getSession().getAttribute("MAPAJUSART");
		if(mapaJus!=null){
			
			*/
		mapaJus=new HashMap();
			numReg=Utilidades.convertirAEntero(mundo.getArticulos("numRegistros")==null||(mundo.getArticulos("numRegistros")+"").equals("")?"0":mundo.getArticulos("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				mapaJus.put(mundo.getArticulos("articulo_"+i)+"_pendiente",forma.getJustificacionMap().get(mundo.getArticulos("articulo_"+i)+"_pendiente"));
				mapaJus.put(mundo.getArticulos("articulo_"+i)+"_mapasecciones",request.getSession().getAttribute("MAPASECJUSART"));
				mapaJus.put(mundo.getArticulos("articulo_"+i)+"_mapajustificacion",request.getSession().getAttribute(mundo.getArticulos("articulo_"+i)+"MAPAJUSART"));
				
				mapaJus.put(mundo.getArticulos("articulo_"+i)+"_sevaasociar",forma.getJustificacionMap().get(mundo.getArticulos("articulo_"+i)+"_sevaasociar"));
			}
			
			mundo.setJustificacionMap(mapaJus);
			//request.getSession().removeAttribute("MAPAJUSART");
		/*}else{
			mundo.setJustificacionMap(forma.getJustificacionMap());
		}*/
		
		mundo.setMedicamentosPosMap(forma.getMedicamentosPosMap());
		mundo.setMedicamentosNoPosMap(forma.getMedicamentosNoPosMap());
		mundo.setSustitutosNoPosMap(forma.getSustitutosNoPosMap());
		mundo.setDiagnosticosDefinitivos(forma.getDiagnosticosDefinitivos());
		// *************************** FIN JUSTIFICACIÓN NO POS *********************
		
		mundo.setCentroCostoSolicita(forma.getCentroCostoSolicitante()+"");
		mundo.setIdIngresoPaciente(forma.getIdIngreso());
		mundo.setCuentaPaciente(forma.getCuentaSolicitante());
		
		if(forma.getCodigoCitaSeleccionada()!=null&&!forma.getCodigoCitaSeleccionada().trim().isEmpty()){
			mundo.setCodigoCitaAsociada(Integer.parseInt(forma.getCodigoCitaSeleccionada()));
		}
	}

	/**
	 * 
	 * @param con 
	 * @param forma
	 */
	private void cargarInformaionGenerarToForm(Connection con, OrdenesAmbulatoriasForm forma, OrdenesAmbulatorias mundo) 
	{
		forma.setTipoOrden(forma.getOrdenes("tipoorden_"+forma.getIndex())+"");
		forma.setUrgente(UtilidadTexto.getBoolean(forma.getOrdenes("urgente_"+forma.getIndex())+""));
		forma.setCentroAtencion(forma.getOrdenes("centrosolicita_"+forma.getIndex())+"");
		forma.setProfesional(forma.getOrdenes("profesional_"+forma.getIndex())+"");
		forma.setEspecialidad(forma.getOrdenes("especialidad_"+forma.getIndex())+"");
		forma.setNumeroOrden(forma.getOrdenes("numero_"+forma.getIndex())+"");
		forma.setFechaOrden(forma.getOrdenes("fecha_"+forma.getIndex())+"");
		forma.setHora(forma.getOrdenes("hora_"+forma.getIndex())+"");
		forma.setObservaciones(forma.getOrdenes("observaciones_"+forma.getIndex())+"");
		forma.setOtros(forma.getOrdenes("otros_"+forma.getIndex())+"");
		forma.setEstadoOrden(forma.getOrdenes("estado_"+forma.getIndex())+"");
		forma.setPyp(UtilidadTexto.getBoolean(forma.getOrdenes("pyp_"+forma.getIndex())+""));
		forma.setCantidad(forma.getOrdenes("cantidad_"+forma.getIndex())+"");
		//forma.setSeleccionado(forma.getOrdenes("seleccionado_"+forma.getIndex()));
		forma.setResultado(mundo.consultarResultadoOrdenesAmbulatorias(con, forma.getNumeroOrden()));
		
		if(Utilidades.convertirAEntero(forma.getTipoOrden())==ConstantesBD.codigoTipoOrdenAmbulatoriaServicios
				&& !forma.getOrdenes("cantidad_"+forma.getIndex()).toString().equals("") )
		{
			
			forma.setServicio(forma.getOrdenes("servicio_"+forma.getIndex())+"");
			forma.setTipoServicio(Utilidades.obtenerTipoServicio(con,forma.getServicio()));
			forma.setCantidad(forma.getOrdenes("cantidad_"+forma.getIndex())+"");
		}
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param paciente
	 * @throws IPSException 
	 */
	@SuppressWarnings({ "rawtypes" })
	private void accionVerTodas(Connection con, OrdenesAmbulatoriasForm forma, OrdenesAmbulatorias mundo, UsuarioBasico usuario, PersonaBasica paciente) throws IPSException 
	{
		
		try {
			mundo.consultarOrdenesAmbulatoriasPaciente(con,paciente.getCodigoPersona(),usuario.getCodigoInstitucion(),ConstantesBD.codigoNuncaValido, forma.getIdIngreso());
			forma.setOrdenes((HashMap)mundo.getOrdenes().clone());
			
			/**
			 * @author javrammo
			 * MT 3851
			 */
			cargarDetalleArticulosOrdenesAmbulatorias(con, forma, usuario, mundo);
			/**
			 * Fin 3851
			 */
			
			int tamanio = Integer.parseInt(forma.getOrdenes().get("numRegistros")+"");
			String[] vector = new String[tamanio];
			for(int i=0;i<tamanio;i++)
			{
				vector[i]="false";
			}
			forma.setSeleccionados(vector);
			forma.setAll("false");
			
			forma.setEsPreingreso(true);			
		}catch (IPSException e) {
			Log4JManager.error(e.getMessage(),e);
			throw e;
		} 
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}

	}

	/**
	 * 
	 * @param request 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param paciente
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes"})
	private void accionEmpezar(HttpServletRequest request, Connection con, OrdenesAmbulatoriasForm forma, OrdenesAmbulatorias mundo, UsuarioBasico usuario, PersonaBasica paciente) throws IPSException 
	{
		
		try {
			
			mundo.consultarOrdenesAmbulatoriasPaciente(con,paciente.getCodigoPersona(),usuario.getCodigoInstitucion(),ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente, ConstantesBD.codigoNuncaValido);
			forma.setOrdenes((HashMap)mundo.getOrdenes().clone());
			int tamanio = Integer.parseInt(forma.getOrdenes().get("numRegistros")+"");
			
			/**
			 * @author javrammo
			 * MT 3851
			 */
			cargarDetalleArticulosOrdenesAmbulatorias(con, forma, usuario, mundo);
			/**
			 * fin MT 3851
			 */
			String[] vector = new String[tamanio];
			for(int i=0;i<tamanio;i++)
			{
				vector[i]="false";
			}
			forma.setSeleccionados(vector);
			forma.setAll("false");
			
			this.cargarCuentaSolicitaPaciente(con,forma,mundo,paciente,usuario);
			
			ActionMessages errores=this.consultarCitasAtendidas(forma,mundo,usuario,paciente);
			
			Boolean provieneAtencionCita=UtilidadTexto.getBoolean(request.getParameter("provieneAtencionCitaRequest"));
			
			forma.setProvieneAtencionCita(provieneAtencionCita.booleanValue());
			
			if(provieneAtencionCita.booleanValue()){	
				forma.setCodigoCitaSeleccionada(request.getParameter("codigoCitaSeleccionadaRequest"));
			}
			
			if(UtilidadTexto.getBoolean(request.getParameter("quitarEncabezados")))
			{
				forma.setQuitarEncabezados(true);
			}
		
			if(!errores.isEmpty()){
				saveErrors(request, errores);
			}
		}catch (IPSException e) {
			Log4JManager.error(e.getMessage(),e);
			throw e;
		}  
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
		

	}
	
	/**
	 * Metodo que carga los detalles de los articulos de una orden ambulatoria para ser mostradas mediante un 
	 * tooltip. MT 3851
	 * @author javrammo
	 */
	private void cargarDetalleArticulosOrdenesAmbulatorias(Connection con, OrdenesAmbulatoriasForm forma, UsuarioBasico usuario, OrdenesAmbulatorias mundo) throws IPSException {

		try {
		    int codigoInstitucion=usuario.getCodigoInstitucionInt();
		    int tamanio = Integer.parseInt(forma.getOrdenes().get("numRegistros")+"");
		    
			if(tamanio > 0 && forma.getOrdenes() != null){
				for (int i = 0; i < tamanio; i++) {
					//Inicializo el mapa: Si es de servicios no va a tener una coleccion con el detalle
					forma.getOrdenes().put(OrdenesAmbulatoriasAction.KEY_DETALLE_MEDICAMENTOS+i, null);
					forma.getOrdenes().put(OrdenesAmbulatoriasAction.KEY_DETALLE_INSUMO+i, null);
					
					Object objTipoOrden = forma.getOrdenes().get("tipoorden_"+i);
					Integer tipoOrden = null;
					if(objTipoOrden instanceof Number){
						tipoOrden = ((Number) objTipoOrden).intValue();
					}
					
					if(tipoOrden!= null && tipoOrden == ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos){
						//Si la orden ambulatoria es de tipo Articulos
						Object objIdOrden = forma.getOrdenes().get("codigo_"+i);
						Integer idOrden = null;
						if(objIdOrden instanceof Number){
							idOrden = ((Number) objIdOrden).intValue();
						}
						
						forma.getOrdenes().put(OrdenesAmbulatoriasAction.KEY_DETALLE_MEDICAMENTOS+i, 
								mundo.detalleToolTipArticulosOrdenAmbulatoria(con,idOrden, true, codigoInstitucion));
						forma.getOrdenes().put(OrdenesAmbulatoriasAction.KEY_DETALLE_INSUMO+i, 
								mundo.detalleToolTipArticulosOrdenAmbulatoria(con,idOrden, false, codigoInstitucion));					
					}
				}
			}			
		}catch (IPSException e) {
			Log4JManager.error(e.getMessage(),e);
			throw e;
		} 
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}


	/**
	 * 
	 * @param con
	 * @param forma 
	 * @param mundo 
	 * @param paciente
	 */
	private void cargarCuentaSolicitaPaciente(Connection con,OrdenesAmbulatoriasForm forma, OrdenesAmbulatorias mundo, PersonaBasica paciente, UsuarioBasico usuario) 
	{
		//si el paciente tiene un ingreso cargado, car la cuenta activa si no la tiene cargar la cuenta asociada.
		forma.setCuentaSolicitante(ConstantesBD.codigoNuncaValido);
    	if(paciente.getCodigoIngreso()>0)
    	{
    		forma.setCuentaSolicitante(paciente.getCodigoCuenta()>0?paciente.getCodigoCuenta():paciente.getCodigoCuentaAsocio());
    	}
    	//si el paciente no tiene ingreso cargado, o no tiene la cuenta. se debe cargar la ultima cuenta del paciente, que no este en estado cerrada.
    	forma.setCuentaSolicitante(mundo.otenerUltimaCuentaPacienteValidaParaOrden(con,paciente.getCodigoPersona()));
		Cuenta cuenta=new Cuenta();
		logger.info("cuenta --->"+forma.getCuentaSolicitante());
		cuenta.cargarCuenta(con, forma.getCuentaSolicitante()+"");
		logger.info("------------>area:  "+cuenta.getCodigoArea());
		logger.info("------------>ingreso:  "+cuenta.getCodigoIngreso());
		//forma.setCentroCostoSolicitante(cuenta.getCodigoArea());
		forma.setCentroCostoSolicitante(usuario.getCodigoCentroCosto());//MT 3340 nota 10263 El centro de costo= Centro de costo solicitante de la orden ambulatoria
		forma.setIdIngreso(cuenta.getCodigoIngreso());
	}


	/**
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param mundo
	 * @param paciente
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionImprimirArticuloControlCartaCompleta(Connection con, OrdenesAmbulatoriasForm forma, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, OrdenesAmbulatorias mundo, PersonaBasica paciente, boolean mediaCarta) 
	{
		String nombreRptDesign2="";
	 	
	 	if(mediaCarta)
	 		nombreRptDesign2 = "prescripcionAmbulatoriaControlEspecialMediaCarta.rptdesign";
	 	else
	 		nombreRptDesign2 = "prescripcionAmbulatoriaControlEspecial.rptdesign"; 	
	 		
	 	InstitucionBasica ins = (InstitucionBasica) request.getSession().getAttribute("institucionBasica");
		Vector v;
	
		// ***************** INFORMACIÓN DEL CABEZOTE
		DesignEngineApi comp;
		comp = new DesignEngineApi(ParamsBirtApplication.getReportsPath()+ "ordenes/", nombreRptDesign2);

		// Logo
		comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());

		// Nombre Institución, titulo y rango de fechas
		comp.insertGridHeaderOfMasterPageWithName(0, 1, 1, 2, "titulo");
		
		v = new Vector();
		v.add(ins.getRazonSocial()+"\nNIT: "+ins.getNit()+"\nDIR: "+ins.getDireccion()+"\nTEL: "+ins.getTelefono()+"\nRECETARIO OFICIAL MEDICAMENTOS DE CONTROL ESPECIAL\nPRESCRIPCION AMBULATORIA DE MEDICAMENTOS");
			
		comp.insertLabelInGridOfMasterPage(0, 1, v);
		
		UsuarioBasico usuarioGeneraSolicitud = new UsuarioBasico();
    	String login=Utilidades.getLoginUsuarioSolicitaOrdenAmbulatoria(con, forma.getNumeroOrden(), usuario.getCodigoInstitucionInt());
    	if(!login.equals(""))
    	{	
    		try 
    		{
				usuarioGeneraSolicitud.cargarUsuarioBasico(con, login);
			} 
    		catch (SQLException e) 
    		{
				logger.info("ERROR: "+e);
			}
    	}
		
		// Fecha hora de proceso y usuario
		comp.insertLabelInGridPpalOfFooter(0, 0, "Fecha: "+ UtilidadFecha.getFechaActual() + " Hora:"+ UtilidadFecha.getHoraActual());
		comp.insertLabelInGridPpalOfFooter(0, 1, "Usuario:"+ usuario.getLoginUsuario());
		// ****************** FIN INFORMACIÓN DEL CABEZOTE"

		comp.obtenerComponentesDataSet("dataSet");
		comp.modificarQueryDataSet(ConsultasBirt.impresionInfoPacienteMedicamentosControlEspecial(paciente.getCodigoPersona()));
		
		comp.obtenerComponentesDataSet("dataSetOrden");
		comp.modificarQueryDataSet(ConsultasBirt.impresionInfoOrden(forma.getNumeroOrden()));
		
		comp.obtenerComponentesDataSet("dataSetMedico");
		comp.modificarQueryDataSet(ConsultasBirt.impresionInfoMedico(forma.getProfesional()));
		
		comp.insertImageBodyPage(0, 1, usuario.getPathFirmaDigital(), "firmaMedico");
		
		//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport2 = comp.saveReport1(false);
		
		comp.updateJDBCParameters(newPathReport2);
		
		newPathReport2 += 	"&nroHistoriaClinica="+ paciente.getHistoriaClinica()+
							"&nroOrden="+ forma.getNumeroOrden()+
							"&fecha="+UtilidadFecha.getFechaActual()+
							"&hora="+UtilidadFecha.getHoraActual();

		if (!newPathReport2.equals("")) {
			
			request.setAttribute("isOpenReport2", "true");
			request.setAttribute("newPathReport2", newPathReport2);
		}
		return mapping.findForward("detalleArticulo");
	}
	
	/**
	 * Método que se encarga de agrupar las ordenes segun caracteristicas equitativas
	 * (=tipo,=fecha,=usuario,=centroAtencion,=estado) 
	 * y realizar la impresion de las ordenes seleccionadas por Hoja
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param mundo
	 * @param paciente
	 * @param institucionActual
	 * @return
	 * @author Camilo Gómez
	 */
	@SuppressWarnings("deprecation")
	private ActionForward imprimirOrdenesSeleccionadas(Connection con,OrdenesAmbulatoriasForm forma,UsuarioBasico usuario, ActionMapping mapping,
			HttpServletRequest request,OrdenesAmbulatorias mundo,PersonaBasica paciente,InstitucionBasica institucionActual)
	{
		ActionErrors errores = new ActionErrors();		
		try {
			int tamanioRegistros=Utilidades.convertirAEntero(forma.getOrdenes().get("numRegistros")+"");
			//cuando se le de imprimir
			ArrayList<String>numOrdenes=new ArrayList<String>();
			for(int i=0;i<tamanioRegistros;i++)
			{
				if(forma.getOrdenes().get("numerocheck_"+i)!=null && forma.getOrdenes().get("numerocheck_"+i).equals("true"))
				{
					numOrdenes.add(forma.getOrdenes("numero_"+i)+"");
				}		
			}
			if(Utilidades.isEmpty(numOrdenes))
			{
				errores.add("seleccionarOrden", new ActionMessage("error.ordenAmbulatoria.seleccionarOrdenImpresion"));
				saveErrors(request, errores);            
				return mapping.findForward("principal");
			}
			/*******************************************************************************************/
			/**·······················CONSULTA DE ORDENES AMBULATORIAS A IMPRIMIR······················*/
			/*******************************************************************************************/
			DtoOrdenesAmbulatorias parametros =new DtoOrdenesAmbulatorias();
			ArrayList<DtoOrdenesAmbulatorias> resultadoConsulta =new ArrayList<DtoOrdenesAmbulatorias>();
			ArrayList<String>consecutivosOrdenes=new ArrayList<String>();
			/*consecutivosOrdenes.add("126494");
			consecutivosOrdenes.add("126534");
			consecutivosOrdenes.add("126594");
			consecutivosOrdenes.add("126614");
			consecutivosOrdenes.add("126634");
			consecutivosOrdenes.add("126674");
			consecutivosOrdenes.add("126694");
			consecutivosOrdenes.add("126714");*/
			for (String cambioLista : numOrdenes) {
				consecutivosOrdenes.add(cambioLista);
			}


			IOrdenesAmbulatoriasMundo ordenesAmbulatoriasMundo=OrdenesFabricaMundo.crearOrdenesAmbulatoriasMundo();				
			parametros.setParametrosOrdenesAmbulatorias(consecutivosOrdenes);
			parametros.setInstitucion(institucionActual.getCodigoInstitucionBasica());
			//Consulta ordenes a imprimir
			resultadoConsulta = ordenesAmbulatoriasMundo.obtenerOrdenesAmbulatorias(parametros);

			//IUsuariosMundo usuariosMundo=AdministracionFabricaMundo.crearUsuariosMundo();
			IMedicosMundo medicosMundo = AdministracionFabricaMundo.crearMedicosMundo();
			ArrayList<String>especialidadesMedico=new ArrayList<String>();

			ArrayList<DtoOrdenesAmbulatorias>listaArtiMedicamentos	=new ArrayList<DtoOrdenesAmbulatorias>();
			ArrayList<DtoOrdenesAmbulatorias>listaArtiInsumos		=new ArrayList<DtoOrdenesAmbulatorias>();
			ArrayList<DtoOrdenesAmbulatorias>listaArtiOtros			=new ArrayList<DtoOrdenesAmbulatorias>();

			ArrayList<DtoOrdenesAmbulatorias>listaServiOrdenes	=new ArrayList<DtoOrdenesAmbulatorias>();
			ArrayList<DtoOrdenesAmbulatorias>listaServiOtros	=new ArrayList<DtoOrdenesAmbulatorias>();

			DtoOrdenesAmbulatorias dtoFinalReporte=new DtoOrdenesAmbulatorias();
			ArrayList<String> grupoCaracteristica=new ArrayList<String>();
			ArrayList<String>listaOrdenObservacion=new ArrayList<String>();
			ArrayList<String>listaOrdenOtros=new ArrayList<String>();//

			//System.out.println("---------- SIN ORDENAR ------------");
			//pintaLista(resultadoConsulta);
			//System.out.println("---------- ORDEN DEFINIDO ------------");		
			Collections.sort(resultadoConsulta);
			//pintaLista(resultadoConsulta);

			//String prescripcionValida="- Esta Prescripción es válida por 72 Horas";//-->Aplica solo para medicamentos		
			//int contAnuladas=0;
			int contAnuladasSup=0;//>Se inicializa en el ciclo interno en el caso de anuladas para comparar que no se repita cuando tengan agrupacion igual

			for (DtoOrdenesAmbulatorias listaResul : resultadoConsulta) 
			{	boolean controlado=false;
		  	   String caracteristicaAgrupa=listaResul.getTipoOrden()+"-"+listaResul.getFechaOrden()+"-"+listaResul.getLoginMedico()+"-"+listaResul.getCentroAtencion()+"-"+listaResul.getEstadoOrden();
			   listaResul.setCaracteristicaAgrupa(caracteristicaAgrupa);

				String observaTemp="";
				listaServiOrdenes=new ArrayList<DtoOrdenesAmbulatorias>();
				listaServiOtros=new ArrayList<DtoOrdenesAmbulatorias>();
				listaArtiMedicamentos=new ArrayList<DtoOrdenesAmbulatorias>();
				listaArtiInsumos=new ArrayList<DtoOrdenesAmbulatorias>();
				listaArtiOtros=new ArrayList<DtoOrdenesAmbulatorias>();

				if(listaResul.getEstadoOrden()==ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada)
					listaResul.setCaracteristicaAgrupa(listaResul.getCaracteristicaAgrupa()+"-Anulada_"+contAnuladasSup++);

				if(!grupoCaracteristica.contains(listaResul.getCaracteristicaAgrupa()))
				{
					int contAnuladasInf=0;//>Se inicializa en el ciclo interno en el caso de anuladas para comparar que no se repita cuando tengan agrupacion igual 

					if(listaResul.getTipoOrden().equals(messageResource.getMessage("ordenesAmbulatorias.tipoServicio")))
					{//SERVICIOS	

						for(DtoOrdenesAmbulatorias listaResulServi : resultadoConsulta)
						{
							String caracteristicaAgrupaServicios=listaResulServi.getTipoOrden()+"-"+listaResulServi.getFechaOrden()+"-"+listaResulServi.getLoginMedico()+"-"+listaResulServi.getCentroAtencion()+"-"+listaResulServi.getEstadoOrden();
							listaResulServi.setCaracteristicaAgrupa(caracteristicaAgrupaServicios);

							if(listaResul.getEstadoOrden()==ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada)
								listaResulServi.setCaracteristicaAgrupa(listaResulServi.getCaracteristicaAgrupa()+"-Anulada_"+contAnuladasInf++);

							if(listaResul.getCaracteristicaAgrupa().equals(listaResulServi.getCaracteristicaAgrupa()))
							{
								if(listaResulServi.getCodigoServicio()!=null)
								{//Si ingresaron algun servicio parametrizado
									//String tmp =																										

									/*
									 * MT  1656
									 * Se debe mostrar indicativo de orden pyp
									 * Diana Ruiz
									 */

									if (listaResulServi.isPyp())										
										listaResulServi.setNumeroOrden(listaResulServi.getNumeroOrden()+" (pyp)");								

									listaResulServi.setNombreservicio(listaResulServi.getCodigoPropietario()+" - "+listaResulServi.getNombreservicio());
									//tmp = "";

									if(UtilidadTexto.getBoolean(listaResulServi.isEsPos()))
									{	listaResulServi.setEsPosStr("SI");								
									}
									else
									{	listaResulServi.setEsPosStr("NO");								
									}

									if(UtilidadTexto.getBoolean(listaResulServi.isUrgente()))
									{	listaResulServi.setUrgenteStr("Prioritario");								
									}
									else
									{	listaResulServi.setUrgenteStr("");								
									}

									listaServiOrdenes.add(listaResulServi);		//----->Se agrega a la lista

									if(!UtilidadTexto.isEmpty(listaResulServi.getOtros())&&!listaOrdenOtros.contains(listaResulServi.getNumeroOrden()))
									{	listaServiOtros.add(listaResulServi);	//----->Se agrega a la lista Otros
									listaOrdenOtros.add(listaResulServi.getNumeroOrden());
									}

								}else
								{//Si solo ingresaron otros y no cargaron ningun servicio parametrizado
									if(!UtilidadTexto.isEmpty(listaResulServi.getOtros())&&!listaOrdenOtros.contains(listaResulServi.getNumeroOrden()))
									{	listaServiOtros.add(listaResulServi);	//----->Se agrega a la lista Otros
									listaOrdenOtros.add(listaResulServi.getNumeroOrden());
									}
								}

								if(!UtilidadTexto.isEmpty(listaResulServi.getObservaciones())&&!listaOrdenObservacion.contains(listaResulServi.getNumeroOrden()))
								{	observaTemp =observaTemp+"\n Orden "+listaResulServi.getNumeroOrden()+" - "+listaResulServi.getObservaciones();
								listaOrdenObservacion.add(listaResulServi.getNumeroOrden());
								}

							
							}
						}						

						DtoOrdenesAmbulatorias dto =new DtoOrdenesAmbulatorias();

						String fechaNac=UtilidadFecha.conversionFormatoFechaAAp(listaResul.getFechaNacimiento());

						String pacienteImp=	listaResul.getPrimerNombre()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoNombre())?"":listaResul.getSegundoNombre())+" "+
						listaResul.getPrimerApellido()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoApellido())?"":listaResul.getSegundoApellido())+", "+
						listaResul.getTipoId()+" "+listaResul.getNumeroId()+", Edad: "+UtilidadFecha.calcularEdad(fechaNac)+" años, Tel: "+
						(UtilidadTexto.isEmpty(listaResul.getTelefonoPersona())?"":listaResul.getTelefonoPersona());

						if(listaResul.getEstadoOrden()==ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada)
						{	
							dto.setTitulo(messageResource.getMessage("ordenesAmbulatorias.tituloServicioAnulada"));
							//listaResul.setCaracteristicaAgrupa(listaResul.getCaracteristicaAgrupa()+"-Anulada_"+contAnuladas++);
						}
						else
						{	dto.setTitulo(messageResource.getMessage("ordenesAmbulatorias.tituloServicio"));
						}

						listaResul.setEspecialidad("");
						especialidadesMedico=medicosMundo.obtenerEspecialidadesMedico(listaResul.getLoginMedico());
						for(String espe:especialidadesMedico)
						{
							listaResul.setEspecialidad(listaResul.getEspecialidad()+", "+espe);
						}
						dto.setCaracteristicaAgrupa(listaResul.getCaracteristicaAgrupa());//---------
						dto.setPaciente(pacienteImp);
						dto.setFechaOrden(listaResul.getFechaOrden());							
						dto.setConsecutivoOrden(listaResul.getNumeroOrden());
						dto.setHora(listaResul.getHora());
						dto.setCentroAtencion(listaResul.getCentroAtencion());
						dto.setIngreso(listaResul.getIngreso());
						dto.setCuenta(listaResul.getCuenta());
						dto.setProfesional(listaResul.getPrimerNombreMedico()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoNombreMedico())?"":listaResul.getSegundoNombreMedico()) 
								+" "+listaResul.getPrimerApellidoMedico()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoApellidoMedico())?"":listaResul.getSegundoApellidoMedico())
								+" - "+listaResul.getRegistroMedico()
								+" "+listaResul.getEspecialidad());
						//dto.setEspecialidad(listaResul.getEspecialidad());
						dto.setTipoAfiliado(listaResul.getTipoAfiliado());
						dto.setCategoria(listaResul.getCategoria());
						dto.setConvenio(UtilidadTexto.isEmpty(listaResul.getConvenio())?"":listaResul.getConvenio());
						dto.setListaServiciosOrdenes(listaServiOrdenes);
						dto.setListaServiciosOtros(listaServiOtros);
						dto.setObservaciones(observaTemp);
						dto.setFirmaDigitalMedico(UtilidadTexto.isEmpty(listaResul.getFirmaDigitalMedico())?"":listaResul.getFirmaDigitalMedico());
						if(!UtilidadTexto.isEmpty(dto.getFirmaDigitalMedico()))
						{
							//dto.setFirmaDigital(ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+listaResul.getFirmaDigitalMedico());
							dto.setFirmaDigital("../../upload"+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+listaResul.getFirmaDigitalMedico());
						}

						if(listaResul.getEstadoOrden()==ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada)
						{
							String fechaAnulacion=UtilidadFecha.conversionFormatoFechaAAp(listaResul.getFechaAnulacion());
							dto.setMotivoAnulacion(listaResul.getMotivoAnulacion()+" - "+fechaAnulacion+" - "+listaResul.getHora()+" - "+
									listaResul.getPrimerNombreAnulacion()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoNombreAnulacion())?"":listaResul.getSegundoNombreAnulacion())
									+" "+listaResul.getPrimerApellidoAnulacion()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoApellidoAnulacion())?"":listaResul.getSegundoApellidoAnulacion()));
							dto.setHayAnulacion(true);
						}else
						{
							dto.setHayAnulacion(false);
						}
						dtoFinalReporte.getListaServicios().add(dto);   //adiciona cuando cambia de caracteristica ordenes

						grupoCaracteristica.add(listaResul.getCaracteristicaAgrupa());//carcateristica

					}else if(listaResul.getTipoOrden().equals(messageResource.getMessage("ordenesAmbulatorias.tipoArticulo")))
					{//ARTICULOS
						 boolean esControl=false;
						for(DtoOrdenesAmbulatorias listaResulArti : resultadoConsulta)
						{
							String caracteristicaAgrupaArticulos=listaResulArti.getTipoOrden()+"-"+listaResulArti.getFechaOrden()+"-"+listaResulArti.getLoginMedico()+"-"+listaResulArti.getCentroAtencion()+"-"+listaResulArti.getEstadoOrden();
							listaResulArti.setCaracteristicaAgrupa(caracteristicaAgrupaArticulos);

							if(listaResul.getEstadoOrden()==ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada)
								listaResulArti.setCaracteristicaAgrupa(listaResulArti.getCaracteristicaAgrupa()+"-Anulada_"+contAnuladasInf++);

							if(listaResul.getCaracteristicaAgrupa().equals(listaResulArti.getCaracteristicaAgrupa()))
							{	
								if(listaResulArti.getCodigoArticulo()!=null)	
								{//Si ingresaron algun medicamento o insumo parametrizado

									if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(parametros.getInstitucion()).equals(ConstantesIntegridadDominio.acronimoInterfaz))
									{
										listaResulArti.setCodigoArticulo(Utilidades.convertirAEntero(listaResulArti.getCodigoInterfaz()));
									}
									listaResulArti.setNombreArticulo(listaResulArti.getCodigoArticulo()+" - "+ listaResulArti.getNombreArticulo());


									/*
									 * MT 1656
									 * Se debe mostrar indicativo de orden pyp
									 * Diana Ruiz
									 */								

									if (listaResulArti.isPyp())										
										listaResulArti.setNumeroOrden(listaResulArti.getNumeroOrden()+" (pyp)");								

									if(listaResulArti.isMedicamento())
									{	

										/*
										 * MT 1869
										 * Mostrar la concentración del medicamento y la palabra cada en la frecuencia.
										 * Diana Ruiz
										 */	
											String m="";
											m= UtilidadTexto.convertNumberToLetter(listaResulArti.getCantidadArticulo().toString());
											listaResulArti.setCantidadArticuloLetras(m);
											if (listaResulArti.getControlEspecial().equals('S'))
											{ esControl=true;}
											else { esControl=false;}
										listaResulArti.setNombreArticulo(listaResulArti.getNombreArticulo()+" Conc: "+listaResulArti.getConcentracion());										
										listaResulArti.setDosis(listaResulArti.getDosis()+" "+listaResulArti.getUnidadMedidaDosis().trim()+ " - " +listaResulArti.getCantidadUnidadMedidaDosis());
										listaResulArti.setTipoFrecuencia("Cada "+listaResulArti.getFrecuencia()+" "+listaResulArti.getTipoFrecuencia());									
										listaArtiMedicamentos.add(listaResulArti);
									}else
									{	listaArtiInsumos.add(listaResulArti);//----->Se agrega a la lista								
									}

									if(!UtilidadTexto.isEmpty(listaResulArti.getOtros())&&!listaOrdenOtros.contains(listaResulArti.getNumeroOrden()))
									{	listaArtiOtros.add(listaResulArti);	//----->Se agrega a la lista Otros
									listaOrdenOtros.add(listaResulArti.getNumeroOrden());
									}

								}else
								{//Si solo ingresaron otros y no cargaron ningun medicamento o insumo parametrizado
									if(!UtilidadTexto.isEmpty(listaResulArti.getOtros())&&!listaOrdenOtros.contains(listaResulArti.getNumeroOrden()))
									{	listaArtiOtros.add(listaResulArti);	//----->Se agrega a la lista Otros
									listaOrdenOtros.add(listaResulArti.getNumeroOrden());
									}
								}

								if(!UtilidadTexto.isEmpty(listaResulArti.getObservaciones())&&!listaOrdenObservacion.contains(listaResulArti.getNumeroOrden()))
								{	observaTemp =observaTemp+"\n Orden "+listaResulArti.getNumeroOrden()+" - "+listaResulArti.getObservaciones();				
								listaOrdenObservacion.add(listaResulArti.getNumeroOrden());
								}

								if(UtilidadTexto.isEmpty(listaResulArti.getObservacionesMedicamentos()))
									listaResulArti.setObservacionesMedicamentos("");

								if(listaResulArti.getEstadoOrden()==ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada)
								{	break;								
								}
							}
						}

						DtoOrdenesAmbulatorias dto =new DtoOrdenesAmbulatorias();
						String fechaNac=UtilidadFecha.conversionFormatoFechaAAp(listaResul.getFechaNacimiento());

						String pacienteImp=	listaResul.getPrimerNombre()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoNombre())?"":listaResul.getSegundoNombre())+""+
						listaResul.getPrimerApellido()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoApellido())?"":listaResul.getSegundoApellido())+", "+
						listaResul.getTipoId()+" "+listaResul.getNumeroId()+", Edad: "+UtilidadFecha.calcularEdad(fechaNac)+" años, Tel: "+
						(UtilidadTexto.isEmpty(listaResul.getTelefonoPersona())?"":listaResul.getTelefonoPersona());

						if(listaResul.getEstadoOrden()==ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada)
						{	
							
							dto.setTituloCE(messageResource.getMessage("ordenesAmbulatorias.tituloArticuloCEAnulada"));
							dto.setTitulo(messageResource.getMessage("ordenesAmbulatorias.tituloArticuloAnulada"));
							//listaResul.setCaracteristicaAgrupa(listaResul.getCaracteristicaAgrupa()+"-Anulada_"+contAnuladas++);
						}
						else
						{	
							dto.setTituloCE(messageResource.getMessage("ordenesAmbulatorias.tituloArticuloCE"));
							dto.setTitulo(messageResource.getMessage("ordenesAmbulatorias.tituloArticulo"));
														
						}
						
						listaResul.setEspecialidad("");
						especialidadesMedico=medicosMundo.obtenerEspecialidadesMedico(listaResul.getLoginMedico());
						for(String espe:especialidadesMedico)
						{
							listaResul.setEspecialidad(listaResul.getEspecialidad()+", "+espe);
						}							
						dto.setCaracteristicaAgrupa(listaResul.getCaracteristicaAgrupa());//---------
						dto.setPaciente(pacienteImp);
						dto.setSexo(listaResul.getSexo());
						dto.setDirPaciente(listaResul.getDirPaciente());
						dto.setMun(listaResul.getMun());
						dto.setDpto(listaResul.getDpto());
						dto.setHistoria(listaResul.getHistoria());
						dto.setFechaOrden(listaResul.getFechaOrden());
						dto.setConsecutivoOrden(listaResul.getNumeroOrden());
						dto.setHora(listaResul.getHora());
						dto.setCentroAtencion(listaResul.getCentroAtencion());
						dto.setIngreso(listaResul.getIngreso());
						dto.setCuenta(listaResul.getCuenta());
						if (esControl)
						{dto.setControlEspecial('S');}
						esControl=false;
						dto.setProfesional(listaResul.getPrimerNombreMedico()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoNombreMedico())?"":listaResul.getSegundoNombreMedico()) 
								+" "+listaResul.getPrimerApellidoMedico()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoApellidoMedico())?"":listaResul.getSegundoApellidoMedico())
								+" - "+listaResul.getRegistroMedico()
								+""+listaResul.getEspecialidad()+"\n"+messageResource.getMessage("ordenesAmbulatorias.prescripcionMedicamento"));
						dto.setProfesionalCE(listaResul.getPrimerNombreMedico()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoNombreMedico())?"":listaResul.getSegundoNombreMedico()) 
								+" "+listaResul.getPrimerApellidoMedico()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoApellidoMedico())?"":listaResul.getSegundoApellidoMedico())
								+", "+listaResul.getTipoIdMedico()+" "+listaResul.getNumeroIdMedico()+" - "+listaResul.getRegistroMedico()
								+""+listaResul.getEspecialidad()+"\n"+messageResource.getMessage("ordenesAmbulatorias.prescripcionMedicamento"));
						//dto.setEspecialidad(listaResul.getEspecialidad());
						dto.setTipoAfiliado(listaResul.getTipoAfiliado());
						dto.setCategoria(listaResul.getCategoria());
						dto.setConvenio(UtilidadTexto.isEmpty(listaResul.getConvenio())?"":listaResul.getConvenio());
						dto.setListaArticulosMedicamentos(listaArtiMedicamentos);
						dto.setListaArticulosInsumos(listaArtiInsumos);
						dto.setListaArticulosOtros(listaArtiOtros);
						dto.setObservaciones(observaTemp);
						dto.setFirmaDigitalMedico(UtilidadTexto.isEmpty(listaResul.getFirmaDigitalMedico())?"":listaResul.getFirmaDigitalMedico());
						if(!UtilidadTexto.isEmpty(dto.getFirmaDigitalMedico()))
						{
							//dto.setFirmaDigital(ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+listaResul.getFirmaDigitalMedico());
							dto.setFirmaDigital("../../upload"+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+listaResul.getFirmaDigitalMedico());
						}

						if(listaResul.getEstadoOrden()==ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada)
						{
							String fechaAnulacion=UtilidadFecha.conversionFormatoFechaAAp(listaResul.getFechaAnulacion());
							dto.setMotivoAnulacion(listaResul.getMotivoAnulacion()+" - "+fechaAnulacion+" - "+listaResul.getHora()+" - "+
									listaResul.getPrimerNombreAnulacion()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoNombreAnulacion())?"":listaResul.getSegundoNombreAnulacion())
									+" "+listaResul.getPrimerApellidoAnulacion()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoApellidoAnulacion())?"":listaResul.getSegundoApellidoAnulacion()));
							dto.setHayAnulacion(true);
						}else
						{
							dto.setHayAnulacion(false);
						}
						dtoFinalReporte.getListaArticulos().add(dto);   //adiciona cuando cambia de caracteristica ordenes

						grupoCaracteristica.add(listaResul.getCaracteristicaAgrupa());//carcateristica

					}
					dtoFinalReporte.setRazonSocial(listaResul.getRazonSocial());
					dtoFinalReporte.setNit("NIT - "+listaResul.getNit());
					dtoFinalReporte.setActividadEconomica(listaResul.getActividadEconomica());
					dtoFinalReporte.setDireccion("Dir:  "+listaResul.getDireccion() +"  -  Tel: "+listaResul.getTelefono());
					dtoFinalReporte.setUsuario(usuario.getNombreUsuario()+" ("+usuario.getLoginUsuario()+")");
				}	
			}			
			/************************************************************************************/

			String nombreArchivoCopia="";
			String nombreArchivoOriginal="";
			// ArrayList <String>nombresArchivos=new ArrayList<String>();

			dtoFinalReporte.setUbicacionLogo(institucionActual.getUbicacionLogo());
			String rutalogo = institucionActual.getLogoJsp(); 
			dtoFinalReporte.setRutaLogo(rutalogo);

			String reporteMediaCarta = ValoresPorDefecto.getImpresionMediaCarta(
					usuario.getCodigoInstitucionInt());

			if(UtilidadTexto.isEmpty(reporteMediaCarta))
				reporteMediaCarta=ConstantesBD.acronimoNo;
			dtoFinalReporte.setFormatoMediaCarta(reporteMediaCarta);

			GeneradorReporteOrdenesAmbulatorias generadorReporte=null;
			JasperPrint reporteCopia=null;
			JasperPrint reporteOriginal=null;

			//-----------Original
			dtoFinalReporte.setTipoImpresion(messageResource.getMessage("ordenesAmbulatorias.impresionOriginal"));
			generadorReporte = new GeneradorReporteOrdenesAmbulatorias(dtoFinalReporte);
			reporteOriginal = generadorReporte.generarReporte();


			nombreArchivoOriginal = generadorReporte.exportarReportePDF(reporteOriginal, "OrdenesAmbulatoriasOriginal");
			//JasperViewer.viewReport(reporteOriginal, false);
			dtoFinalReporte.setNombreArchivoGenerado(nombreArchivoOriginal);
			forma.setNombreArchivoGeneradoOriginal(nombreArchivoOriginal);
			//nombresArchivos.add(nombreArchivoOriginal);

			//------------Copia
			dtoFinalReporte.setTipoImpresion(messageResource.getMessage("ordenesAmbulatorias.impresionCopia"));
			generadorReporte = new GeneradorReporteOrdenesAmbulatorias(dtoFinalReporte);
			reporteCopia = generadorReporte.generarReporte();

			nombreArchivoCopia= generadorReporte.exportarReportePDF(reporteCopia, "OrdenesAmbulatoriasCopia");
			dtoFinalReporte.setNombreArchivoGenerado(nombreArchivoCopia);
			forma.setNombreArchivoGeneradoCopia(nombreArchivoCopia);
			//nombresArchivos.add(nombreArchivoCopia);

			//forma.setListaNombreArchivos(nombresArchivos);
			/************************************************************************************/
			UtilidadTransaccion.getTransaccion().commit();
		} catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			Log4JManager.error("imprimirOrdenesSeleccionadas : " + e);
			errores.add("seleccionarOrden", new ActionMessage("errors.transaccion"));
			saveErrors(request, errores);       
			return mapping.findForward("paginaError");
		}	
		return mapping.findForward("principal");		
	}
	
	
	
	
	/**Imprime la lista de Ordenes Ambulatorias sin ordenar y ordenada por compareTo del dtoOrdenesAmbulatorias
	 * y por el toString implicito en el dto
	 * @author Camilo Gómez
	 */
	/*@SuppressWarnings("rawtypes")
	public static void pintaLista(List lista) {
		for (int i = 0; i < lista.size(); i++) {
			System.out.println(lista.get(i));
		}
	}*/
	
	
	/**
	 * Metodo que se encarga de checkear todos los checkBox para impresion sin entrar al detalle 
	 * 
	 * @param forma
	 * @param usuario
	 * @author Camilo Gómez
	 */
	@SuppressWarnings("unchecked")
	private ActionForward checkearTodoImpresion(OrdenesAmbulatoriasForm forma,ActionMapping mapping)
	{
		forma.setNombreArchivoGeneradoOriginal("");
		forma.setNombreArchivoGeneradoCopia("");
		
		int j=Integer.parseInt(forma.getOrdenes().get("numRegistros").toString());
		String select[]=new String[j];			
		for(int i=0 ;i<j;i++)
		{
			if(forma.getAll().equals("true"))
			{
				forma.getOrdenes().put("numerocheck_"+i, "true");
				select[i]="true";
			}else{
				forma.getOrdenes().put("numerocheck_"+i, "false");
				select[i]="false";
			}					
		}
		forma.setSeleccionados(select);
				
		
		for(int i=0; i<j;i++)
		{
			forma.getOrdenes().get("numero_"+i);
		}
		return mapping.findForward("principal");
	}
	
	/**
	 * Método que se encarga imprimir las ordenes ambulatorias 
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param mundo
	 * @param paciente
	 * @param institucionActual
	 * @return
	 * @author Luis Alejandro Castellanos Oviedo
	 */
	@SuppressWarnings({"deprecation" })
	private ActionForward imprimirOrdenesDetalle(OrdenesAmbulatoriasForm forma,UsuarioBasico usuario, ActionMapping mapping,
			HttpServletRequest request,OrdenesAmbulatorias mundo,PersonaBasica paciente,InstitucionBasica institucionActual)
	{
		ActionErrors errores = new ActionErrors();		
		try {
			int tamanioRegistros=Utilidades.convertirAEntero(forma.getOrdenes().get("numRegistros")+"");
			//cuando se le de imprimir
			ArrayList<String>numOrdenes=new ArrayList<String>();

			/*******************************************************************************************/
			/***CONSULTA DE ORDENES AMBULATORIAS A IMPRIMIR**/
			/*******************************************************************************************/
			DtoOrdenesAmbulatorias parametros =new DtoOrdenesAmbulatorias();
			ArrayList<DtoOrdenesAmbulatorias> resultadoConsulta =new ArrayList<DtoOrdenesAmbulatorias>();
			ArrayList<String>consecutivosOrdenes=new ArrayList<String>();

			/**MT 2275 Cuando se generan varios servicios al mismo tiempo en una orden Ambulatoria se debe 
			 * imprimir cuando se encuentre en el resumen*/
			if(forma.getListaNumOrdenServicios()!=null&&!forma.getListaNumOrdenServicios().isEmpty())
			{
				for (String numOrdeServi : forma.getListaNumOrdenServicios())
					consecutivosOrdenes.add(numOrdeServi);
				forma.resetNumerosOrdenesServicio();
			}else
			{
				consecutivosOrdenes.add(forma.getNumeroOrden());
			}

			HibernateUtil.beginTransaction();
			IOrdenesAmbulatoriasMundo ordenesAmbulatoriasMundo=OrdenesFabricaMundo.crearOrdenesAmbulatoriasMundo();				
			parametros.setParametrosOrdenesAmbulatorias(consecutivosOrdenes);
			parametros.setInstitucion(institucionActual.getCodigoInstitucionBasica());
			//Consulta ordenes a imprimir
			resultadoConsulta = ordenesAmbulatoriasMundo.obtenerOrdenesAmbulatorias(parametros);
			//IUsuariosMundo usuariosMundo=AdministracionFabricaMundo.crearUsuariosMundo();
			IMedicosMundo medicosMundo = AdministracionFabricaMundo.crearMedicosMundo();
			ArrayList<String>especialidadesMedico=new ArrayList<String>();

			ArrayList<DtoOrdenesAmbulatorias>listaArtiMedicamentos	=new ArrayList<DtoOrdenesAmbulatorias>();
			ArrayList<DtoOrdenesAmbulatorias>listaArtiInsumos		=new ArrayList<DtoOrdenesAmbulatorias>();
			ArrayList<DtoOrdenesAmbulatorias>listaArtiOtros			=new ArrayList<DtoOrdenesAmbulatorias>();

			ArrayList<DtoOrdenesAmbulatorias>listaServiOrdenes	=new ArrayList<DtoOrdenesAmbulatorias>();
			ArrayList<DtoOrdenesAmbulatorias>listaServiOtros	=new ArrayList<DtoOrdenesAmbulatorias>();

			DtoOrdenesAmbulatorias dtoFinalReporte=new DtoOrdenesAmbulatorias();
			ArrayList<String> grupoCaracteristica=new ArrayList<String>();
			ArrayList<String>listaOrdenObservacion=new ArrayList<String>();
			ArrayList<String>listaOrdenOtros=new ArrayList<String>();

			//System.out.println("---------- SIN ORDENAR ------------");
			//pintaLista(resultadoConsulta);
			//System.out.println("---------- ORDEN DEFINIDO ------------");		
			Collections.sort(resultadoConsulta);
			//pintaLista(resultadoConsulta);

			//String prescripcionValida="- Esta Prescripción es válida por 72 Horas";//-->Aplica solo para medicamentos		
			//int contAnuladas=0;
			int contAnuladasSup=0;//>Se inicializa en el ciclo interno en el caso de anuladas para comparar que no se repita cuando tengan agrupacion igual

			for (DtoOrdenesAmbulatorias listaResul : resultadoConsulta) 
			{	
				String caracteristicaAgrupa=listaResul.getTipoOrden()+"-"+listaResul.getFechaOrden()+"-"+listaResul.getLoginMedico()+"-"+listaResul.getCentroAtencion()+"-"+listaResul.getEstadoOrden();
				listaResul.setCaracteristicaAgrupa(caracteristicaAgrupa);

				String observaTemp="";
				listaServiOrdenes=new ArrayList<DtoOrdenesAmbulatorias>();
				listaServiOtros=new ArrayList<DtoOrdenesAmbulatorias>();
				listaArtiMedicamentos=new ArrayList<DtoOrdenesAmbulatorias>();
				listaArtiInsumos=new ArrayList<DtoOrdenesAmbulatorias>();
				listaArtiOtros=new ArrayList<DtoOrdenesAmbulatorias>();

				if(listaResul.getEstadoOrden()==ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada)
					listaResul.setCaracteristicaAgrupa(listaResul.getCaracteristicaAgrupa()+"-Anulada_"+contAnuladasSup++);

				if(!grupoCaracteristica.contains(listaResul.getCaracteristicaAgrupa()))
				{
					int contAnuladasInf=0;//>Se inicializa en el ciclo interno en el caso de anuladas para comparar que no se repita cuando tengan agrupacion igual 

					if(listaResul.getTipoOrden().equals(messageResource.getMessage("ordenesAmbulatorias.tipoServicio")))
					{//SERVICIOS	

						for(DtoOrdenesAmbulatorias listaResulServi : resultadoConsulta)
						{
							String caracteristicaAgrupaServicios=listaResulServi.getTipoOrden()+"-"+listaResulServi.getFechaOrden()+"-"+listaResulServi.getLoginMedico()+"-"+listaResulServi.getCentroAtencion()+"-"+listaResulServi.getEstadoOrden();
							listaResulServi.setCaracteristicaAgrupa(caracteristicaAgrupaServicios);

							if(listaResul.getEstadoOrden()==ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada)
								listaResulServi.setCaracteristicaAgrupa(listaResulServi.getCaracteristicaAgrupa()+"-Anulada_"+contAnuladasInf++);

							if(listaResul.getCaracteristicaAgrupa().equals(listaResulServi.getCaracteristicaAgrupa()))
							{
								if(listaResulServi.getCodigoServicio()!=null)
								{//Si ingresaron algun servicio parametrizado
									
									/*
									 ** MT 1656
									 * Se debe mostrar indicativo de orden pyp
									 * Diana Ruiz
									 */
									if (listaResulServi.isPyp())										
										listaResulServi.setNumeroOrden(listaResulServi.getNumeroOrden()+" (pyp)");	

									String descripcionServi=listaResulServi.getNombreservicio();
									/**FIXME DCU 307*******************************************/
					            	// validacion descripcion servicio cuando proceso -GENERACION AUTORIZACION- queda en pendiente (Servicio+Acronimo)
									if(forma.isPendienteAutorizacion())
										descripcionServi=validacionImpresionDescripcionServicio(Integer.parseInt(listaResulServi.getNumeroOrden()),
				     						listaResulServi.getCodigoServicio(), request, true);
					     			/******************************************************************/
				     				listaResulServi.setNombreservicio(listaResulServi.getCodigoPropietario()+" - "+descripcionServi);
				     				

									if(UtilidadTexto.getBoolean(listaResulServi.isEsPos()))
									{	listaResulServi.setEsPosStr("SI");								
									}
									else
									{	listaResulServi.setEsPosStr("NO");								
									}

									if(UtilidadTexto.getBoolean(listaResulServi.isUrgente()))
									{	listaResulServi.setUrgenteStr("Prioritario");								
									}
									else
									{	listaResulServi.setUrgenteStr("");								
									}

									listaServiOrdenes.add(listaResulServi);		//----->Se agrega a la lista

									if(!UtilidadTexto.isEmpty(listaResulServi.getOtros())&&!listaOrdenOtros.contains(listaResulServi.getNumeroOrden()))
									{	listaServiOtros.add(listaResulServi);	//----->Se agrega a la lista Otros
									listaOrdenOtros.add(listaResulServi.getNumeroOrden());
									}

								}else
								{//Si solo ingresaron otros y no cargaron ningun servicio parametrizado
									if(!UtilidadTexto.isEmpty(listaResulServi.getOtros())&&!listaOrdenOtros.contains(listaResulServi.getNumeroOrden()))
									{	listaServiOtros.add(listaResulServi);	//----->Se agrega a la lista Otros							
									listaOrdenOtros.add(listaResulServi.getNumeroOrden());
									}
								}

								if(!UtilidadTexto.isEmpty(listaResulServi.getObservaciones())&&!listaOrdenObservacion.contains(listaResulServi.getNumeroOrden()))
								{	observaTemp =observaTemp+"\n Orden "+listaResulServi.getNumeroOrden()+" - "+listaResulServi.getObservaciones();
								listaOrdenObservacion.add(listaResulServi.getNumeroOrden());
								}

								
							}
						}						

						DtoOrdenesAmbulatorias dto =new DtoOrdenesAmbulatorias();

						String fechaNac=UtilidadFecha.conversionFormatoFechaAAp(listaResul.getFechaNacimiento());

						String pacienteImp=	listaResul.getPrimerNombre()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoNombre())?"":listaResul.getSegundoNombre())+" "+
						listaResul.getPrimerApellido()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoApellido())?"":listaResul.getSegundoApellido())+", "+
						listaResul.getTipoId()+" "+listaResul.getNumeroId()+", Edad: "+UtilidadFecha.calcularEdad(fechaNac)+" años, Tel: "+
						(UtilidadTexto.isEmpty(listaResul.getTelefonoPersona())?"":listaResul.getTelefonoPersona());

						if(listaResul.getEstadoOrden()==ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada)
						{	
							dto.setTitulo(messageResource.getMessage("ordenesAmbulatorias.tituloServicioAnulada"));
							//listaResul.setCaracteristicaAgrupa(listaResul.getCaracteristicaAgrupa()+"-Anulada_"+contAnuladas++);
						}
						else
						{	dto.setTitulo(messageResource.getMessage("ordenesAmbulatorias.tituloServicio"));
						}

						listaResul.setEspecialidad("");
						especialidadesMedico=medicosMundo.obtenerEspecialidadesMedico(listaResul.getLoginMedico());
						for(String espe:especialidadesMedico)
						{
							listaResul.setEspecialidad(listaResul.getEspecialidad()+", "+espe);
						}
						dto.setCaracteristicaAgrupa(listaResul.getCaracteristicaAgrupa());//---------
						dto.setPaciente(pacienteImp);
						dto.setFechaOrden(listaResul.getFechaOrden());							
						dto.setCentroAtencion(listaResul.getCentroAtencion());
						dto.setIngreso(listaResul.getIngreso());
						dto.setCuenta(listaResul.getCuenta());
						dto.setProfesional(listaResul.getPrimerNombreMedico()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoNombreMedico())?"":listaResul.getSegundoNombreMedico()) 
								+" "+listaResul.getPrimerApellidoMedico()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoApellidoMedico())?"":listaResul.getSegundoApellidoMedico())
								+" - "+listaResul.getRegistroMedico()
								+" "+listaResul.getEspecialidad());
						//dto.setEspecialidad(listaResul.getEspecialidad());
						dto.setTipoAfiliado(listaResul.getTipoAfiliado());
						dto.setCategoria(listaResul.getCategoria());
						dto.setConvenio(UtilidadTexto.isEmpty(listaResul.getConvenio())?"":listaResul.getConvenio());
						dto.setListaServiciosOrdenes(listaServiOrdenes);
						dto.setListaServiciosOtros(listaServiOtros);
						dto.setObservaciones(observaTemp);
						dto.setFirmaDigitalMedico(UtilidadTexto.isEmpty(listaResul.getFirmaDigitalMedico())?"":listaResul.getFirmaDigitalMedico());
						if(!UtilidadTexto.isEmpty(dto.getFirmaDigitalMedico()))
						{
							//dto.setFirmaDigital(ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+listaResul.getFirmaDigitalMedico());
							dto.setFirmaDigital("../../upload"+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+listaResul.getFirmaDigitalMedico());
						}

						if(listaResul.getEstadoOrden()==ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada)
						{
							String fechaAnulacion=UtilidadFecha.conversionFormatoFechaAAp(listaResul.getFechaAnulacion());
							dto.setMotivoAnulacion(listaResul.getMotivoAnulacion()+" - "+fechaAnulacion+" - "+listaResul.getHora()+" - "+
									listaResul.getPrimerNombreAnulacion()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoNombreAnulacion())?"":listaResul.getSegundoNombreAnulacion())
									+" "+listaResul.getPrimerApellidoAnulacion()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoApellidoAnulacion())?"":listaResul.getSegundoApellidoAnulacion()));
							dto.setHayAnulacion(true);
						}else
						{
							dto.setHayAnulacion(false);
						}
						dtoFinalReporte.getListaServicios().add(dto);   //adiciona cuando cambia de caracteristica ordenes

						grupoCaracteristica.add(listaResul.getCaracteristicaAgrupa());//carcateristica

					}else if(listaResul.getTipoOrden().equals(messageResource.getMessage("ordenesAmbulatorias.tipoArticulo")))
					{//ARTICULOS

						for(DtoOrdenesAmbulatorias listaResulArti : resultadoConsulta)
						{
							String caracteristicaAgrupaArticulos=listaResulArti.getTipoOrden()+"-"+listaResulArti.getFechaOrden()+"-"+listaResulArti.getLoginMedico()+"-"+listaResulArti.getCentroAtencion()+"-"+listaResulArti.getEstadoOrden();
							listaResulArti.setCaracteristicaAgrupa(caracteristicaAgrupaArticulos);

							if(listaResul.getEstadoOrden()==ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada)
								listaResulArti.setCaracteristicaAgrupa(listaResulArti.getCaracteristicaAgrupa()+"-Anulada_"+contAnuladasInf++);

							if(listaResul.getCaracteristicaAgrupa().equals(listaResulArti.getCaracteristicaAgrupa()))
							{	
								if(listaResulArti.getCodigoArticulo()!=null)	
								{//Si ingresaron algun medicamento o insumo parametrizado
									
									/*
									 * MT 1656
									 * Se debe mostrar indicativo de orden pyp
									 * Diana Ruiz
									 */								

									if (listaResulArti.isPyp())										
										listaResulArti.setNumeroOrden(listaResulArti.getNumeroOrden()+" (pyp)");	


									if(ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulos(parametros.getInstitucion()).equals(ConstantesIntegridadDominio.acronimoInterfaz))
									{
										listaResulArti.setCodigoArticulo(Utilidades.convertirAEntero(listaResulArti.getCodigoInterfaz()));
									}
									listaResulArti.setNombreArticulo(listaResulArti.getCodigoArticulo()+" - "+ listaResulArti.getNombreArticulo());

									if(listaResulArti.isMedicamento())
									{	

										/*
										 * MT 1869
										 * Mostrar la concentración del medicamento y la palabra cada en la frecuencia.
										 * Diana Ruiz
										 */		
										String m="";
										m= UtilidadTexto.convertNumberToLetter(listaResulArti.getCantidadArticulo().toString());
										listaResulArti.setCantidadArticuloLetras(m);
										listaResulArti.setNombreArticulo(listaResulArti.getNombreArticulo()+" Conc: "+listaResulArti.getConcentracion());										
										listaResulArti.setDosis(listaResulArti.getDosis()+" "+listaResulArti.getUnidadMedidaDosis().trim()+ " - " +listaResulArti.getCantidadUnidadMedidaDosis());
										listaResulArti.setTipoFrecuencia("Cada "+listaResulArti.getFrecuencia()+" "+listaResulArti.getTipoFrecuencia());									
										listaArtiMedicamentos.add(listaResulArti);										
									}else
									{	listaArtiInsumos.add(listaResulArti);//----->Se agrega a la lista								
									}

									if(!UtilidadTexto.isEmpty(listaResulArti.getOtros())&&!listaOrdenOtros.contains(listaResulArti.getNumeroOrden()))
									{	listaArtiOtros.add(listaResulArti);	//----->Se agrega a la lista Otros
									listaOrdenOtros.add(listaResulArti.getNumeroOrden());
									}

								}else
								{//Si solo ingresaron otros y no cargaron ningun medicamento o insumo parametrizado
									if(!UtilidadTexto.isEmpty(listaResulArti.getOtros())&&!listaOrdenOtros.contains(listaResulArti.getNumeroOrden()))
									{	listaArtiOtros.add(listaResulArti);	//----->Se agrega a la lista Otros
									listaOrdenOtros.add(listaResulArti.getNumeroOrden());
									}
								}

								if(!UtilidadTexto.isEmpty(listaResulArti.getObservaciones())&&!listaOrdenObservacion.contains(listaResulArti.getNumeroOrden()))
								{	observaTemp =observaTemp+"\n Orden "+listaResulArti.getNumeroOrden()+" - "+listaResulArti.getObservaciones();
								listaOrdenObservacion.add(listaResulArti.getNumeroOrden());
								}

								if(UtilidadTexto.isEmpty(listaResulArti.getObservacionesMedicamentos()))
									listaResulArti.setObservacionesMedicamentos("");

								if(listaResulArti.getEstadoOrden()==ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada)
								{	break;								
								}
							}
						}

						DtoOrdenesAmbulatorias dto =new DtoOrdenesAmbulatorias();
						String fechaNac=UtilidadFecha.conversionFormatoFechaAAp(listaResul.getFechaNacimiento());

						String pacienteImp=	listaResul.getPrimerNombre()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoNombre())?"":listaResul.getSegundoNombre())+" "+
						listaResul.getPrimerApellido()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoApellido())?"":listaResul.getSegundoApellido())+", "+
						listaResul.getTipoId()+" "+listaResul.getNumeroId()+", Edad: "+UtilidadFecha.calcularEdad(fechaNac)+" años, Tel: "+
						(UtilidadTexto.isEmpty(listaResul.getTelefonoPersona())?"":listaResul.getTelefonoPersona());

						if(listaResul.getEstadoOrden()==ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada)
						{	
							dto.setTitulo(messageResource.getMessage("ordenesAmbulatorias.tituloArticuloAnulada"));
							dto.setTituloCE(messageResource.getMessage("ordenesAmbulatorias.tituloArticuloCEAnulada"));
							//listaResul.setCaracteristicaAgrupa(listaResul.getCaracteristicaAgrupa()+"-Anulada_"+contAnuladas++);
						}
						else
						{
						dto.setTituloCE(messageResource.getMessage("ordenesAmbulatorias.tituloArticuloCE"));
						dto.setTitulo(messageResource.getMessage("ordenesAmbulatorias.tituloArticulo"));
													
						}

						listaResul.setEspecialidad("");
						especialidadesMedico=medicosMundo.obtenerEspecialidadesMedico(listaResul.getLoginMedico());
						for(String espe:especialidadesMedico)
						{
							listaResul.setEspecialidad(listaResul.getEspecialidad()+", "+espe);
						}							
						dto.setCaracteristicaAgrupa(listaResul.getCaracteristicaAgrupa());//---------
						dto.setPaciente(pacienteImp);
						dto.setSexo(listaResul.getSexo());
						dto.setDirPaciente(listaResul.getDirPaciente());
						dto.setMun(listaResul.getMun());
						dto.setDpto(listaResul.getDpto());
						dto.setHistoria(listaResul.getHistoria());
						dto.setFechaOrden(listaResul.getFechaOrden());
						dto.setConsecutivoOrden(listaResul.getNumeroOrden());
						dto.setHora(listaResul.getHora());
						dto.setCentroAtencion(listaResul.getCentroAtencion());
						dto.setIngreso(listaResul.getIngreso());
						dto.setCuenta(listaResul.getCuenta());
						dto.setProfesionalCE(listaResul.getPrimerNombreMedico()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoNombreMedico())?"":listaResul.getSegundoNombreMedico()) 
								+" "+listaResul.getPrimerApellidoMedico()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoApellidoMedico())?"":listaResul.getSegundoApellidoMedico())
								+", "+listaResul.getTipoIdMedico()+" "+listaResul.getNumeroIdMedico()+" - "+listaResul.getRegistroMedico()
								+""+listaResul.getEspecialidad()+"\n"+messageResource.getMessage("ordenesAmbulatorias.prescripcionMedicamento"));
						
						dto.setProfesional(listaResul.getPrimerNombreMedico()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoNombreMedico())?"":listaResul.getSegundoNombreMedico()) 
								+" "+listaResul.getPrimerApellidoMedico()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoApellidoMedico())?"":listaResul.getSegundoApellidoMedico())
								+" - "+listaResul.getRegistroMedico()
								+""+listaResul.getEspecialidad()+"\n"+messageResource.getMessage("ordenesAmbulatorias.prescripcionMedicamento"));
						//dto.setEspecialidad(listaResul.getEspecialidad());
						dto.setTipoAfiliado(listaResul.getTipoAfiliado());
						dto.setCategoria(listaResul.getCategoria());
						dto.setConvenio(UtilidadTexto.isEmpty(listaResul.getConvenio())?"":listaResul.getConvenio());
						dto.setListaArticulosMedicamentos(listaArtiMedicamentos);
						dto.setListaArticulosInsumos(listaArtiInsumos);
						dto.setListaArticulosOtros(listaArtiOtros);
						dto.setObservaciones(observaTemp);
						dto.setFirmaDigitalMedico(UtilidadTexto.isEmpty(listaResul.getFirmaDigitalMedico())?"":listaResul.getFirmaDigitalMedico());
						if(!UtilidadTexto.isEmpty(dto.getFirmaDigitalMedico()))
						{
							//dto.setFirmaDigital(ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+listaResul.getFirmaDigitalMedico());
							dto.setFirmaDigital("../../upload"+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+listaResul.getFirmaDigitalMedico());
						}

						if(listaResul.getEstadoOrden()==ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada)
						{
							String fechaAnulacion=UtilidadFecha.conversionFormatoFechaAAp(listaResul.getFechaAnulacion());
							dto.setMotivoAnulacion(listaResul.getMotivoAnulacion()+" - "+fechaAnulacion+" - "+listaResul.getHora()+" - "+
									listaResul.getPrimerNombreAnulacion()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoNombreAnulacion())?"":listaResul.getSegundoNombreAnulacion())
									+" "+listaResul.getPrimerApellidoAnulacion()+" "+(UtilidadTexto.isEmpty(listaResul.getSegundoApellidoAnulacion())?"":listaResul.getSegundoApellidoAnulacion()));
							dto.setHayAnulacion(true);
						}else
						{
							dto.setHayAnulacion(false);
						}
						dtoFinalReporte.getListaArticulos().add(dto);   //adiciona cuando cambia de caracteristica ordenes

						grupoCaracteristica.add(listaResul.getCaracteristicaAgrupa());//carcateristica

					}
					dtoFinalReporte.setRazonSocial(listaResul.getRazonSocial());
					dtoFinalReporte.setNit("NIT - "+listaResul.getNit());
					dtoFinalReporte.setActividadEconomica(listaResul.getActividadEconomica());
					dtoFinalReporte.setDireccion("Dir:  "+listaResul.getDireccion() +"  -  Tel: "+listaResul.getTelefono());
					dtoFinalReporte.setUsuario(usuario.getNombreUsuario()+" ("+usuario.getLoginUsuario()+")");
				}	
			}			
			/************************************************************************************/

			String nombreArchivoCopia="";
			String nombreArchivoOriginal="";
			// ArrayList <String>nombresArchivos=new ArrayList<String>();

			dtoFinalReporte.setUbicacionLogo(institucionActual.getUbicacionLogo());
			String rutalogo = institucionActual.getLogoJsp(); 
			dtoFinalReporte.setRutaLogo(rutalogo);

			String reporteMediaCarta = ValoresPorDefecto.getImpresionMediaCarta(
					usuario.getCodigoInstitucionInt());

			if(UtilidadTexto.isEmpty(reporteMediaCarta))
				reporteMediaCarta=ConstantesBD.acronimoNo;
			dtoFinalReporte.setFormatoMediaCarta(reporteMediaCarta);

			GeneradorReporteOrdenesAmbulatorias generadorReporte=null;
			JasperPrint reporteCopia=null;
			JasperPrint reporteOriginal=null;

			//-----------Original
			dtoFinalReporte.setTipoImpresion(messageResource.getMessage("ordenesAmbulatorias.impresionOriginal"));
			generadorReporte = new GeneradorReporteOrdenesAmbulatorias(dtoFinalReporte);
			reporteOriginal = generadorReporte.generarReporte();


			nombreArchivoOriginal = generadorReporte.exportarReportePDF(reporteOriginal, "OrdenesAmbulatoriasOriginal");
			//JasperViewer.viewReport(reporteOriginal, false);
			dtoFinalReporte.setNombreArchivoGenerado(nombreArchivoOriginal);
			forma.setNombreArchivoGeneradoOriginal(nombreArchivoOriginal);
			//nombresArchivos.add(nombreArchivoOriginal);

			//------------Copia
			dtoFinalReporte.setTipoImpresion(messageResource.getMessage("ordenesAmbulatorias.impresionCopia"));
			generadorReporte = new GeneradorReporteOrdenesAmbulatorias(dtoFinalReporte);
			reporteCopia = generadorReporte.generarReporte();

			nombreArchivoCopia= generadorReporte.exportarReportePDF(reporteCopia, "OrdenesAmbulatoriasCopia");
			dtoFinalReporte.setNombreArchivoGenerado(nombreArchivoCopia);
			forma.setNombreArchivoGeneradoCopia(nombreArchivoCopia);
			//nombresArchivos.add(nombreArchivoCopia);
			HibernateUtil.endTransaction();
			//forma.setListaNombreArchivos(nombresArchivos);
			
			/**
			* Tipo Modificacion: Segun incidencia MT6636
			* Autor: Jesús Darío Ríos
			* usuario: jesrioro
			* Fecha: 13/03/2013
			* Descripcion: 	Cambio en la  navegacion para que no recargue el popup del detalle de la
			* 				orden ambulatoria  
			**/
			
				if(Integer.parseInt(forma.getTipoOrden())==ConstantesBD.codigoTipoOrdenAmbulatoriaServicios){
					return mapping.findForward("detalleServicio");
				}else{
					return mapping.findForward("detalleArticulo");		
				}
			/************************************************************************************/
		} catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error("imprimirOrdenesSeleccionadas : " + e);
			errores.add("seleccionarOrden", new ActionMessage("errors.transaccion"));
			saveErrors(request, errores);
			return mapping.findForward("paginaError");
		}	
				
	}

	
	/**
	 * M&eacute;todo encargado de generar el reporte de la o las 
     * autorizaciones
	 * @param Connection con, OrdenesAmbulatoriasForm forma, UsuarioBasico usuario, PersonaBasica paciente 
			HttpServletRequest request 
	 * @author Ricardo Ruiz
	 */

	@SuppressWarnings({ "deprecation", "unused" })
	private void accionImprimirAutorizacion(Connection con, OrdenesAmbulatoriasForm forma, UsuarioBasico usuario,
						PersonaBasica paciente, ActionErrors errores, HttpServletRequest request) {
			String tipoFormatoImpresion = ValoresPorDefecto.getFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
			if(!UtilidadTexto.isEmpty(tipoFormatoImpresion))
			{		
				if(tipoFormatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionEstandar)){
	 				generarReporteAutorizacionFormatoEstandar(forma, usuario, paciente, request);
	 			
	 			}else if(tipoFormatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionVersalles)){
	 				generarReporteAutorizacionFormatoVersalles(con, forma, usuario, paciente, request);
	 			}
			}
			else{
				errores.add("Formato no Definifo",
						new ActionMessage("errors.notEspecific", messageResource.getMessage(
								"ordenesAmbulatorias.formatoNoDefinido")));
				saveErrors(request, errores);
			}
	}	
	
	/**
	 * M&eacute;todo encargado de generar el reporte en formato Estandar de la o las 
     * autorizaciones
	 * @param OrdenesAmbulatoriasForm forma, UsuarioBasico usuario, PersonaBasica paciente 
			HttpServletRequest request 
	 * @author Ricardo Ruiz
	 */

	private void generarReporteAutorizacionFormatoEstandar(OrdenesAmbulatoriasForm forma, UsuarioBasico usuario,PersonaBasica paciente, HttpServletRequest request) {
		
			String nombreReporte="AUTORIZACION ORDENES MEDICAS";
			String nombreArchivo ="";
			DTOReporteEstandarAutorizacionServiciosArticulos dtoReporte = 
				new DTOReporteEstandarAutorizacionServiciosArticulos();
			
			ArrayList<String> listaNombresReportes = new ArrayList<String>();
				
	    	InstitucionBasica institucion = (
		        		InstitucionBasica)request.getSession().getAttribute("institucionBasica");
						 			     			
			String nombrePaciente = paciente.getPrimerNombre() + " " + 
				paciente.getSegundoNombre() + " " + paciente.getPrimerApellido()+
				" " + paciente.getSegundoApellido();
				 			     			
			String reporteMediaCarta = ValoresPorDefecto.getImpresionMediaCarta(
					institucion.getCodigoInstitucionBasica());
			
			String infoParametroGeneral = ValoresPorDefecto.getEncFormatoImpresionAutorEntidadSub(
					institucion.getCodigoInstitucionBasica());
			
			String infoPiePagina=ValoresPorDefecto.getPiePagFormatoImpresionAutorEntidadSub(
					institucion.getCodigoInstitucionBasica());
			
			if(UtilidadTexto.isEmpty(reporteMediaCarta)){
				reporteMediaCarta=ConstantesBD.acronimoNo;
			}
				
			dtoReporte.setNombrePaciente(nombrePaciente);
			dtoReporte.setTipoDocPaciente(paciente.getCodigoTipoIdentificacionPersona());
			dtoReporte.setNumeroDocPaciente(paciente.getNumeroIdentificacionPersona());
			dtoReporte.setFormatoMediaCarta(reporteMediaCarta);
			dtoReporte.setInfoParametroGeneral(infoParametroGeneral);
			dtoReporte.setInfoPiePagina(infoPiePagina);
			dtoReporte.setEntidadAutoriza(usuario.getInstitucion());
			dtoReporte.setUsuarioAutoriza(usuario.getLoginUsuario());
			dtoReporte.setRutaLogo(institucion.getLogoJsp());
			
			for(DtoValidacionGeneracionAutorizacionCapitada dtoValidacion:forma.getListaValidacionesAutorizaciones()){
				if(Integer.parseInt(forma.getTipoOrden())==ConstantesBD.codigoTipoOrdenAmbulatoriaServicios){
					dtoReporte.setTipoContrato(dtoValidacion.getTipoContrato());	 			     			
					dtoReporte.setEntidadSubcontratada(dtoValidacion.getProcesoAutorizacion().getDtoEntidadSubcontratada().getRazonSocial());
					dtoReporte.setDireccionEntidadSub(dtoValidacion.getProcesoAutorizacion().getDtoEntidadSubcontratada().getDireccion());
					dtoReporte.setTelefonoEntidadSub(dtoValidacion.getProcesoAutorizacion().getDtoEntidadSubcontratada().getTelefono());
				}
				else if(Integer.parseInt(forma.getTipoOrden())==ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos){
					dtoReporte.setTipoContrato(dtoValidacion.getProcesoAutorizacion().getDtoSolicitud().getConvenioResponsable().getTiposContrato().getNombre());	 			     			
					dtoReporte.setEntidadSubcontratada(dtoValidacion.getDtoentidadSubcontratada().getRazonSocial());
					dtoReporte.setDireccionEntidadSub(dtoValidacion.getDtoentidadSubcontratada().getDireccion());
					dtoReporte.setTelefonoEntidadSub(dtoValidacion.getDtoentidadSubcontratada().getTelefono());
				}
				for(DTOAutorEntidadSubcontratadaCapitacion autorizacion : dtoValidacion.getProcesoAutorizacion().getListaAutorizaciones()){
					
					dtoReporte.setNumeroAutorizacion(autorizacion.getConsecutivoAutorizacion()+"");
					dtoReporte.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaAAp(autorizacion.getFechaAutorizacion()));	 			     				
					dtoReporte.setFechaVencimiento(UtilidadFecha.conversionFormatoFechaAAp(autorizacion.getFechaVencimiento()));
					dtoReporte.setObservaciones(forma.getObservaciones());
					
					if(!UtilidadTexto.isEmpty(autorizacion.getEstado())){
						
						String estado = (String)ValoresPorDefecto.getIntegridadDominio(
								autorizacion.getEstado());	 			     					
						dtoReporte.setEstadoAutorizacion(estado);	 
					} 			     				
					if(Integer.parseInt(forma.getTipoOrden())==ConstantesBD.codigoTipoOrdenAmbulatoriaServicios){
						dtoReporte.setListaServiciosAutorizados(autorizacion.getListaServicios());
						GeneradorReporteFormatoEstandarAutorservicio generadorReporteServicios = 
							new GeneradorReporteFormatoEstandarAutorservicio(dtoReporte);
						
						JasperPrint reporte = generadorReporteServicios.generarReporte();
						nombreArchivo = generadorReporteServicios.exportarReportePDF(reporte, nombreReporte);
					}
					else if(Integer.parseInt(forma.getTipoOrden())==ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos){
						dtoReporte.setListaArticulosAutorizados(autorizacion.getListaArticulos());
						GeneradorReporteFormatoEstandarAutorArticulos generadorReporteArticulos = 
							new GeneradorReporteFormatoEstandarAutorArticulos(dtoReporte);
						JasperPrint reporte = generadorReporteArticulos.generarReporte();
						nombreArchivo = generadorReporteArticulos.exportarReportePDF(reporte, nombreReporte);
					}					
					listaNombresReportes.add(nombreArchivo);	 			     				
				}
			}
			if(listaNombresReportes!=null && listaNombresReportes.size()>0){
				forma.setListaNombresReportes(listaNombresReportes);
			}	
		
	}
	
	/**
	 * M&eacute;todo encargado de generar el reporte en formato Versalles de la o las 
     * autorizaciones
	 * @param Connection con, OrdenesAmbulatoriasForm forma, UsuarioBasico usuario, PersonaBasica paciente 
			HttpServletRequest request 
	 * @author Ricardo Ruiz
	 */
	private void generarReporteAutorizacionFormatoVersalles(Connection con, OrdenesAmbulatoriasForm forma, UsuarioBasico usuario,PersonaBasica paciente, HttpServletRequest request) {
    	
    	String nombreReporte="AUTORIZACION ORDENES MEDICAS";
		String nombreArchivo ="";
		ArrayList<String> listaNombresReportes = new ArrayList<String>();		
		if(Integer.parseInt(forma.getTipoOrden())==ConstantesBD.codigoTipoOrdenAmbulatoriaServicios){
			DtoGeneralReporteServiciosAutorizados dtoReporteServicios = new DtoGeneralReporteServiciosAutorizados();
			GeneradorReporteFormatoCapitacionAutorservicio generadorReporteServicios = 
				new GeneradorReporteFormatoCapitacionAutorservicio(dtoReporteServicios);
			DTOReporteAutorizacionSeccionPaciente dtoPaciente = new DTOReporteAutorizacionSeccionPaciente();
			DTOReporteAutorizacionSeccionAutorizacion dtoAutorizacion = new DTOReporteAutorizacionSeccionAutorizacion();
			
			//**********Datos Comunes para todas las autorizaciones que llegan
			Cuenta cuenta= new Cuenta();
		    cuenta.cargarCuenta(con, paciente.getCodigoCuenta()+"");
		    
		    InstitucionBasica institucion = (
	        		InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		    
		    String nombrePaciente = paciente.getPrimerNombre() + " " + 
			paciente.getSegundoNombre() + " " + paciente.getPrimerApellido()+
			" " + paciente.getSegundoApellido();
			 			     			
			String reporteMediaCarta = ValoresPorDefecto.getImpresionMediaCarta(
					usuario.getCodigoInstitucionInt());
			
			String infoEncabezado = ValoresPorDefecto.getEncFormatoImpresionAutorEntidadSub(
					usuario.getCodigoInstitucionInt());
			
			String infoPiePagina=ValoresPorDefecto.getPiePagFormatoImpresionAutorEntidadSub(
					usuario.getCodigoInstitucionInt());
			
			if(UtilidadTexto.isEmpty(reporteMediaCarta)){
				reporteMediaCarta=ConstantesBD.acronimoNo;
			}	
			
			dtoPaciente.setNombrePaciente(nombrePaciente);
			dtoPaciente.setTipoDocPaciente(paciente.getCodigoTipoIdentificacionPersona());
			dtoPaciente.setNumeroDocPaciente(paciente.getNumeroIdentificacionPersona());
			dtoPaciente.setTipoAfiliado(cuenta.getTipoAfiliado());
			dtoPaciente.setEdadPaciente(String.valueOf(paciente.getEdad()));
			dtoPaciente.setCategoriaSocioEconomica(cuenta.getEstrato());
			dtoPaciente.setRecobro(ConstantesBD.acronimoNo);
			dtoAutorizacion.setEntidadAutoriza(usuario.getInstitucion());
			dtoAutorizacion.setUsuarioAutoriza(usuario.getLoginUsuario());
			
			dtoReporteServicios.setDtoPaciente(dtoPaciente);
			dtoReporteServicios.setDatosEncabezado(infoEncabezado);
			dtoReporteServicios.setDatosPie(infoPiePagina);
			dtoReporteServicios.setTipoReporteMediaCarta(reporteMediaCarta);
			dtoReporteServicios.setRutaLogo(institucion.getLogoJsp());
			dtoReporteServicios.setUbicacionLogo(institucion.getUbicacionLogo());
					
			//**********Datos Comunes para todas las autorizaciones que llegan
			for(DtoValidacionGeneracionAutorizacionCapitada dtoValidacion:forma.getListaValidacionesAutorizaciones()){
				dtoPaciente.setTipoContratoPaciente(dtoValidacion.getTipoContrato());	
				dtoPaciente.setConvenioPaciente(dtoValidacion.getDescripcionConvenioResponsable());
				
				try {
					String tipoAfiliado = "";
					String clasificacionSE = "";
					if(!UtilidadTexto.isEmpty(dtoValidacion.getPaciente().getTipoAfiliado()) &&
							UtilidadTexto.isEmpty(dtoPaciente.getTipoAfiliado())){
						ITiposAfiliadoMundo tiposAfiliadoMundo=ManejoPacienteFabricaMundo.crearTiposAfiliadoMundo();
						tipoAfiliado = tiposAfiliadoMundo.obtenerDescripcionTipoAfiliado(dtoValidacion.getPaciente().getTipoAfiliado().charAt(0));
						dtoPaciente.setTipoAfiliado(tipoAfiliado);
					}
					
					if(!UtilidadTexto.isEmpty(dtoValidacion.getPaciente().getClasificacionSE()) && 
							UtilidadTexto.isEmpty(dtoPaciente.getCategoriaSocioEconomica())){
						ClasificacionSocioEconomica mundoClasificacionSE=new ClasificacionSocioEconomica();
						mundoClasificacionSE.cargarResumen(con, Integer.parseInt(dtoValidacion.getPaciente().getClasificacionSE()));
						clasificacionSE = mundoClasificacionSE.getDescripcion();
						dtoPaciente.setCategoriaSocioEconomica(clasificacionSE);
					}
				}catch (SQLException e) {
					Log4JManager.warning("\n Error obteniendo datos del tipo afiliado y clasificacion SE: \n\n "+e);
				}
				
				if (dtoValidacion.getProcesoAutorizacion().getValorMonto()!=null){
					dtoPaciente.setMontoCobro("$"+dtoValidacion.getProcesoAutorizacion().getValorMonto());
				}else if(dtoValidacion.getProcesoAutorizacion().getPorcentajeMonto()!=null){
					dtoPaciente.setMontoCobro(dtoValidacion.getProcesoAutorizacion().getPorcentajeMonto()+"%");
				}else{
					dtoPaciente.setMontoCobro("");
				}
				
				dtoAutorizacion.setEntidadSub(dtoValidacion.getProcesoAutorizacion().getDtoEntidadSubcontratada().getRazonSocial());
				dtoAutorizacion.setDireccionEntidadSub(dtoValidacion.getProcesoAutorizacion().getDtoEntidadSubcontratada().getDireccion());
				dtoAutorizacion.setTelefonoEntidadSub(dtoValidacion.getProcesoAutorizacion().getDtoEntidadSubcontratada().getTelefono());
				//Por cada autorizacion
				for(DTOAutorEntidadSubcontratadaCapitacion autorizacion: dtoValidacion.getProcesoAutorizacion().getListaAutorizaciones()){
					dtoAutorizacion.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaAAp(autorizacion.getFechaAutorizacion()));
					dtoAutorizacion.setFechaVencimiento(UtilidadFecha.conversionFormatoFechaAAp(autorizacion.getFechaVencimiento()));
					if(!UtilidadTexto.isEmpty(autorizacion.getEstado())){
						
						String estado = (String)ValoresPorDefecto.getIntegridadDominio(
								autorizacion.getEstado());	 			     					
						dtoAutorizacion.setEstadoAutorizacion(estado);	 
					}
					
					dtoAutorizacion.setObservaciones(forma.getObservaciones());
					dtoAutorizacion.setNumeroAutorizacion(autorizacion.getConsecutivoAutorizacion());
					
					dtoReporteServicios.setDtoAutorizacion(dtoAutorizacion);
					dtoReporteServicios.setListaServicios(autorizacion.getListaServicios());
					
					JasperPrint reporte = generadorReporteServicios.generarReporte();
					nombreArchivo = generadorReporteServicios.exportarReportePDF(reporte, nombreReporte);
					
					listaNombresReportes.add(nombreArchivo);
					
//					dtoAutorizacion.setIndicadorPrioridad("");
					dtoAutorizacion.setObservaciones("");
				}
			}
		}
		else if(Integer.parseInt(forma.getTipoOrden())==ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos){
			DtoGeneralReporteArticulosAutorizados dtoReporteArticulos = new DtoGeneralReporteArticulosAutorizados();
			GeneradorReporteFormatoCapitacionAutorArticulos generadorReporteArticulos = 
				new GeneradorReporteFormatoCapitacionAutorArticulos(dtoReporteArticulos);
			DTOReporteAutorizacionSeccionPaciente dtoPaciente = new DTOReporteAutorizacionSeccionPaciente();
			DTOReporteAutorizacionSeccionAutorizacion dtoAutorizacion = new DTOReporteAutorizacionSeccionAutorizacion();
			
			//**********Datos Comunes para todas las autorizaciones que llegan
			Cuenta cuenta= new Cuenta();
		    cuenta.cargarCuenta(con, paciente.getCodigoCuenta()+"");
		    
		    InstitucionBasica institucion = (
	        		InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		    
		    String nombrePaciente = paciente.getPrimerNombre() + " " + 
			paciente.getSegundoNombre() + " " + paciente.getPrimerApellido()+
			" " + paciente.getSegundoApellido();
			 			     			
			String reporteMediaCarta = ValoresPorDefecto.getImpresionMediaCarta(
					usuario.getCodigoInstitucionInt());
			
			String infoEncabezado = ValoresPorDefecto.getEncFormatoImpresionAutorEntidadSub(
					usuario.getCodigoInstitucionInt());
			
			String infoPiePagina=ValoresPorDefecto.getPiePagFormatoImpresionAutorEntidadSub(
					usuario.getCodigoInstitucionInt());
			
			if(UtilidadTexto.isEmpty(reporteMediaCarta)){
				reporteMediaCarta=ConstantesBD.acronimoNo;
			}	
			
			dtoPaciente.setNombrePaciente(nombrePaciente);
			dtoPaciente.setTipoDocPaciente(paciente.getCodigoTipoIdentificacionPersona());
			dtoPaciente.setNumeroDocPaciente(paciente.getNumeroIdentificacionPersona());
			dtoPaciente.setTipoAfiliado(cuenta.getTipoAfiliado());
			dtoPaciente.setEdadPaciente(String.valueOf(paciente.getEdad()));
			dtoPaciente.setCategoriaSocioEconomica(cuenta.getEstrato());
			dtoPaciente.setRecobro(ConstantesBD.acronimoNo);
			dtoAutorizacion.setEntidadAutoriza(usuario.getInstitucion());
			dtoAutorizacion.setUsuarioAutoriza(usuario.getLoginUsuario());
			
			dtoReporteArticulos.setDtoPaciente(dtoPaciente);
			dtoReporteArticulos.setDatosEncabezado(infoEncabezado);
			dtoReporteArticulos.setDatosPie(infoPiePagina);
			dtoReporteArticulos.setTipoReporteMediaCarta(reporteMediaCarta);
			dtoReporteArticulos.setRutaLogo(institucion.getLogoJsp());
			dtoReporteArticulos.setUbicacionLogo(institucion.getUbicacionLogo());
					
			//**********Datos Comunes para todas las autorizaciones que llegan
			for(DtoValidacionGeneracionAutorizacionCapitada dtoValidacion:forma.getListaValidacionesAutorizaciones()){
				dtoPaciente.setTipoContratoPaciente(dtoValidacion.getProcesoAutorizacion().getDtoSolicitud().getConvenioResponsable().getTiposContrato().getNombre());	
				dtoPaciente.setConvenioPaciente(dtoValidacion.getProcesoAutorizacion().getDtoSolicitud().getConvenioResponsable().getNombre());
				
				try {
					String tipoAfiliado = "";
					String clasificacionSE = "";
					if(!UtilidadTexto.isEmpty(dtoValidacion.getPaciente().getTipoAfiliado()) &&
							UtilidadTexto.isEmpty(dtoPaciente.getTipoAfiliado())){
						ITiposAfiliadoMundo tiposAfiliadoMundo=ManejoPacienteFabricaMundo.crearTiposAfiliadoMundo();
						tipoAfiliado = tiposAfiliadoMundo.obtenerDescripcionTipoAfiliado(dtoValidacion.getPaciente().getTipoAfiliado().charAt(0));
						dtoPaciente.setTipoAfiliado(tipoAfiliado);
					}
					
					if(!UtilidadTexto.isEmpty(dtoValidacion.getPaciente().getClasificacionSE()) && 
							UtilidadTexto.isEmpty(dtoPaciente.getCategoriaSocioEconomica())){
						ClasificacionSocioEconomica mundoClasificacionSE=new ClasificacionSocioEconomica();
						mundoClasificacionSE.cargarResumen(con, Integer.parseInt(dtoValidacion.getPaciente().getClasificacionSE()));
						clasificacionSE = mundoClasificacionSE.getDescripcion();
						dtoPaciente.setCategoriaSocioEconomica(clasificacionSE);
					}
				}catch (SQLException e) {
					Log4JManager.warning("\n Error obteniendo datos del tipo afiliado y clasificacion SE: \n\n "+e);
				}
				
				if (dtoValidacion.getProcesoAutorizacion().getValorMonto()!=null){
					dtoPaciente.setMontoCobro("$"+dtoValidacion.getProcesoAutorizacion().getValorMonto());
				}else if(dtoValidacion.getProcesoAutorizacion().getPorcentajeMonto()!=null){
					dtoPaciente.setMontoCobro(dtoValidacion.getProcesoAutorizacion().getPorcentajeMonto()+"%");
				}else{
					dtoPaciente.setMontoCobro("");
				}
				
				dtoAutorizacion.setEntidadSub(dtoValidacion.getDtoentidadSubcontratada().getRazonSocial());
				dtoAutorizacion.setDireccionEntidadSub(dtoValidacion.getDtoentidadSubcontratada().getDireccion());
				dtoAutorizacion.setTelefonoEntidadSub(dtoValidacion.getDtoentidadSubcontratada().getTelefono());
				//Por cada autorizacion
				for(DTOAutorEntidadSubcontratadaCapitacion autorizacion: dtoValidacion.getProcesoAutorizacion().getListaAutorizaciones()){
					dtoAutorizacion.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaAAp(autorizacion.getFechaAutorizacion()));
					dtoAutorizacion.setFechaVencimiento(UtilidadFecha.conversionFormatoFechaAAp(autorizacion.getFechaVencimiento()));
					if(!UtilidadTexto.isEmpty(autorizacion.getEstado())){
						
						String estado = (String)ValoresPorDefecto.getIntegridadDominio(
								autorizacion.getEstado());	 			     					
						dtoAutorizacion.setEstadoAutorizacion(estado);	 
					}
					
					dtoAutorizacion.setObservaciones(forma.getObservaciones());
					dtoAutorizacion.setNumeroAutorizacion(autorizacion.getConsecutivoAutorizacion());
					
					dtoReporteArticulos.setDtoAutorizacion(dtoAutorizacion);
					dtoReporteArticulos.setListaArticulos(autorizacion.getListaArticulos());
					
					JasperPrint reporte = generadorReporteArticulos.generarReporte();
					nombreArchivo = generadorReporteArticulos.exportarReportePDF(reporte, nombreReporte);
					
					listaNombresReportes.add(nombreArchivo);
					
//					dtoAutorizacion.setIndicadorPrioridad("");
					dtoAutorizacion.setObservaciones("");
				}
			}
		}
		if(listaNombresReportes!=null && listaNombresReportes.size()>0){
			forma.setListaNombresReportes(listaNombresReportes);
		}	
	}
	
	/**
	 * Método que se encarga de validar la descripción del servicio pendiente por autorizar si falló en :
	 * La generación de autorizacion (DCU-1106) mostrar SERVICIO + ACRONIMO de dias tramite del grupo de servicio
	 * En el no de consecutivos disponibles y niveles (DCU-1115), mostrar SERVICIO + DIAS de tramite del grupo de servicios
	 * 
	 *  Cambio DCU 307 RQF-02 -0025 Autorizaciones Capitación Subcontratada
	 *  
	 *  @author Camilo Gómez
	 */	
	private String validacionImpresionDescripcionServicio(int solicitud,int servicio,
			HttpServletRequest request,boolean procesoGeneracion)
	{
		IAutorizacionCapitacionOrdenesAmbulatoriasMundo autorizacionCapitacionOrdenesAmbulatoriasMundo=CapitacionFabricaMundo.crearAutorizacionCapitacionOrdenesAmbulatoriasMundo();
		DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizacionCapitacionOrdenAmbulatoria=new DtoAutorizacionCapitacionOrdenAmbulatoria();
		
		try{
			//Paso la validacion de contrato=Capitado y capitacionSubcontratada='S' entonces 
			//aplica para autorizacion Capita Orden Ambulatoria
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");			
				
				DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizCapitaOrdenAmbu=new DtoAutorizacionCapitacionOrdenAmbulatoria(); 
				dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().setNumeroOrden(solicitud+"");
				dtoAutorizCapitaOrdenAmbu =  consultarExisteAutorizacionCapitaOrdenAmbulatoria(dtoAutorizCapitaOrdenAmbu);
				
				dtoAutorizacionCapitacionOrdenAmbulatoria.setCodigoServicioAutorizar(servicio);//solicitudGenerada.getCodigoServicioCitaAutorizar());
				dtoAutorizacionCapitacionOrdenAmbulatoria.setNumeroSolicitudAutorizar(Integer.parseInt(dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().getNumeroOrden()));//solicitudGenerada.getNumeroSolicitudCitaAutorizar());
				dtoAutorizacionCapitacionOrdenAmbulatoria.setOrdenAmbulatoria(true);
				
				if(procesoGeneracion)
					dtoAutorizacionCapitacionOrdenAmbulatoria.setProcesoGeneracionAutoriz(true);//indica si viene desde las validaciones de -Generacion Autorizacion- se debe agregar a la descripcion, el ACRONIMO
				else
					dtoAutorizacionCapitacionOrdenAmbulatoria.setProcesoGeneracionAutoriz(false);//indica si viene desde las validaciones de -Consecutivos y Nivel- se debe agregar a la descripcion, los DIAS
			
			autorizacionCapitacionOrdenesAmbulatoriasMundo.validarDescripcionServicio(dtoAutorizacionCapitacionOrdenAmbulatoria, ins);
			
		} catch (Exception e) {
			Log4JManager.error("No existe la Orden Ambulatoria: " + e);
		}
		
		return dtoAutorizacionCapitacionOrdenAmbulatoria.getNombreServicioAutorizar();
	}
	
	
	/**
	 * consulta el codigo de la Orden Ambulatoria de acuerdo a su consecutivo
	 * @param dtoAutorizCapitaOrdenAmbu
	 * @return DtoAutorizacionCapitacionOrdenAmbulatoria
	 * 
	 */
	private DtoAutorizacionCapitacionOrdenAmbulatoria consultarExisteAutorizacionCapitaOrdenAmbulatoria(DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizCapitaOrdenAmbu)
	{
		/* Se obtiene el código de la orden a partir del consecutivo mientras se hace la consulta y despues se deja todo como venia */
		try {
			IOrdenesAmbulatoriasMundo ordenesAmbulatoriasMundo	=OrdenesFabricaMundo.crearOrdenesAmbulatoriasMundo();		
			ArrayList<com.servinte.axioma.orm.OrdenesAmbulatorias> listaOrdenesAmbulatorias = new  ArrayList<com.servinte.axioma.orm.OrdenesAmbulatorias>(); 
			DtoOrdenesAmbulatorias dtoOrdenesAmbulatorias = new DtoOrdenesAmbulatorias();
			dtoOrdenesAmbulatorias.setConsecutivoOrden(dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().getNumeroOrden());
			listaOrdenesAmbulatorias = ordenesAmbulatoriasMundo.buscarPorParametros(dtoOrdenesAmbulatorias);
			dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().setNumeroOrden(listaOrdenesAmbulatorias.get(0).getCodigo()+"");
			
		} catch (Exception e) {
			Log4JManager.error("Error obteniendo el codigo de la orden Ambulatoria de acuerdo al consecutivo (" +
					dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().getNumeroOrden()+"): error--> " + e);
		}
		return dtoAutorizCapitaOrdenAmbu;
	}
	

	/**
	 * Metodo que se encarga de enviar los datos necesarios para validar la anulacion de la
	 * autorizacion
	 * 
	 * @param ordenForm
	 * @param usuario
	 * @throws IPSException
	 */
	public void cargarInfoParaAnulacionAutorizacion(OrdenesAmbulatoriasForm ordenForm,
			UsuarioBasico usuario, ActionErrors errores)throws IPSException
	{
		List<AutorizacionCapitacionDto> listaAutorizacionCapitacion = null;
		AnulacionAutorizacionSolicitudDto anulacionDto	= null;
		ManejoPacienteFacade manejoPacienteFacade		= null;
		Medicos medicos	= null;
		try{
			anulacionDto= new AnulacionAutorizacionSolicitudDto();
			anulacionDto.setMotivoAnulacion(ordenForm.getMotivoAnulacion());
			anulacionDto.setFechaAnulacion(UtilidadFecha.getFechaActualTipoBD());
			anulacionDto.setHoraAnulacion(UtilidadFecha.getHoraActual());
			medicos		= new Medicos(); 
			medicos.setCodigoMedico(usuario.getCodigoPersona());
			anulacionDto.setMedicoAnulacion(medicos);
			anulacionDto.setLoginUsuarioAnulacion(usuario.getLoginUsuario());
			anulacionDto.setCodigoOrdenAmbulatoria(Utilidades.convertirALong(ordenForm.getCodigoOrden()+""));
			anulacionDto.setPyP(UtilidadTexto.getBoolean(ordenForm.getOrdenes("pyp_"+ordenForm.getIndex())+""));
			int tipoOrdenAmbulatoria= ConstantesBD.codigoNuncaValido;
			if(Integer.parseInt(ordenForm.getTipoOrden())==ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos){
				tipoOrdenAmbulatoria=ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos;
			}else{
				tipoOrdenAmbulatoria=ConstantesBD.codigoTipoOrdenAmbulatoriaServicios;
			}
			
			manejoPacienteFacade = new ManejoPacienteFacade();
			listaAutorizacionCapitacion = manejoPacienteFacade.validarAnulacionAutorizacionCapitaSolictud(anulacionDto, 
					ConstantesBD.claseOrdenOrdenAmbulatoria,tipoOrdenAmbulatoria,null,usuario.getCodigoInstitucionInt());
			
			if(listaAutorizacionCapitacion!=null && !listaAutorizacionCapitacion.isEmpty())
			{	//Se adiciona mensaje para los servicio que no se autorizaron
				manejoPacienteFacade.obtenerMensajesError(listaAutorizacionCapitacion, errores);
			}
			
		}catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	/**
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @author jeilones
	 * @param usuario 
	 * @created 23/10/2012
	 */
	private ActionMessages consultarCitasAtendidas(OrdenesAmbulatoriasForm forma,
			OrdenesAmbulatorias mundo, UsuarioBasico usuario, PersonaBasica paciente) {
		ActionMessages errores=new ActionMessages();
		ConsultaExternaFacade consultaExternaFacade=new ConsultaExternaFacade();
		try {
			List<CitaDto>listaCitasAtendidas=consultaExternaFacade.consultarCitasAtentidas(usuario.getLoginUsuario(), forma.getIdIngreso(),paciente.getCodigoPersona());
			forma.setListaCitasAtendidas(listaCitasAtendidas);
			
			forma.setCodigoCitaSeleccionada(ConstantesBD.codigoNuncaValido+"");
			
			if(!listaCitasAtendidas.isEmpty()){
				forma.setCodigoCitaSeleccionada(listaCitasAtendidas.get(0).getCodigo()+"");
			}
		} catch (IPSException ipse) {
			errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
		}
		return errores;
	}
	
	/**
	 * @param forma
	 * @param con
	 * @param mundo
	 * @param paciente
	 * @param codigoOrden
	 * @param contratoConvenio
	 * @param usuario
	 * @return
	 * @author hermorhu
	 */
	@SuppressWarnings("static-access")
	private List<DtoValidacionGeneracionAutorizacionCapitada> cargarDatosValidacionGeneracionAutorizacionCapitada(OrdenesAmbulatoriasForm forma, Connection con, OrdenesAmbulatorias mundo, PersonaBasica paciente, int codigoOrden, int contratoConvenio, UsuarioBasico usuario){
		List<DtoValidacionGeneracionAutorizacionCapitada> listaValidacionGeneracionAutorizacionCapitada = new ArrayList<DtoValidacionGeneracionAutorizacionCapitada>(); 
		
		DtoValidacionGeneracionAutorizacionCapitada dtoValidacionGeneraAutorizacion = new DtoValidacionGeneracionAutorizacionCapitada();
		dtoValidacionGeneraAutorizacion.setOrdenAmbulatoria(codigoOrden);
		dtoValidacionGeneraAutorizacion.setConsecutivoOrden(forma.getNumeroOrden());
		dtoValidacionGeneraAutorizacion.setContratoConvenioResponsable(contratoConvenio);

		ContratosDelegate contratosDelegate = new ContratosDelegate();
		Contratos contrato = null;
		contrato = contratosDelegate.findById(contratoConvenio);
		dtoValidacionGeneraAutorizacion.setConvenio(contrato.getConvenios().getCodigo());
				
		DtoDiagnostico dtoDiagnostico=new DtoDiagnostico();
		dtoDiagnostico=mundo.consultarDiagnosticoOrdenAmbulatoria(con, Integer.parseInt(forma.getNumeroOrden()));
		dtoValidacionGeneraAutorizacion.setTipoOrden(ConstantesIntegridadDominio.acronimoTipoOrdenambulatoria);
		dtoValidacionGeneraAutorizacion.setPaciente(paciente);
		dtoValidacionGeneraAutorizacion.getCentrosCostoSolicitante().setCodigo(forma.getCentroCostoSolicitante());
		dtoValidacionGeneraAutorizacion.setAcronimoDiagnostico(dtoDiagnostico.getAcronimoDiagnostico());
		dtoValidacionGeneraAutorizacion.setOrdenMedicamentoUrgente(forma.isUrgente());	
		
		cargarCuentaSolicitaPaciente(con,forma,mundo,paciente,usuario);
		dtoValidacionGeneraAutorizacion.setCodIngreso(forma.getIdIngreso());
		
		listaValidacionGeneracionAutorizacionCapitada.add(dtoValidacionGeneraAutorizacion);
		
		return listaValidacionGeneracionAutorizacionCapitada;
	}
	
}
