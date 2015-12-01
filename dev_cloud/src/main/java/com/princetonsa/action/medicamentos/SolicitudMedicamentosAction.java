/*
 * Creado en 2/09/2004
 *
 */
package com.princetonsa.action.medicamentos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
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
import util.LogsAxioma;
import util.ObjetoReferencia;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadN2T;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.inventarios.UtilidadInventarios;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.medicamentos.SolicitudMedicamentosForm;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.Medico;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Egreso;
import com.princetonsa.mundo.inventarios.FarmaciaCentroCosto;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;
import com.princetonsa.mundo.ordenesmedicas.OrdenMedica;
import com.princetonsa.mundo.ordenesmedicas.OrdenesAmbulatorias;
import com.princetonsa.mundo.solicitudes.ArticuloSolicitudMedicamentos;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudMedicamentos;
import com.princetonsa.mundo.solicitudes.ValidacionesSolicitud;
import com.princetonsa.pdf.SolicitudMedicamentosPdf;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.bl.inventario.facade.InventarioFacade;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.bl.ordenes.facade.OrdenesFacade;
import com.servinte.axioma.dto.capitacion.DtoAutorizacionCapitacionOrdenAmbulatoria;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.facturacion.ConvenioDto;
import com.servinte.axioma.dto.facturacion.MontoCobroDto;
import com.servinte.axioma.dto.inventario.ClaseInventarioDto;
import com.servinte.axioma.dto.manejoPaciente.AnulacionAutorizacionSolicitudDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionPorOrdenDto;
import com.servinte.axioma.dto.manejoPaciente.DatosPacienteAutorizacionDto;
import com.servinte.axioma.dto.manejoPaciente.InfoSubCuentaDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.generadorReporte.ordenes.solicitudMedicamentos.GeneradorReporteSolicitudMedicamentos;
import com.servinte.axioma.generadorReporte.ordenes.solicitudMedicamentos.ImpresionMedicamentoInsumoSolicitadoDto;
import com.servinte.axioma.generadorReporte.ordenes.solicitudMedicamentos.ImpresionSolicitudMedicamentosDto;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IAutorizacionCapitacionOrdenesAmbulatoriasMundo;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Medicos;
import com.servinte.axioma.orm.delegate.administracion.CentroCostosDelegate;
import com.servinte.axioma.orm.delegate.facturacion.convenio.ContratosDelegate;

/**
 * @author Juan David Ramírez López
 * Princeton S.A.
 */
public class SolicitudMedicamentosAction extends Action
{
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(SolicitudMedicamentosAction.class);
	
	MessageResources messageResource = MessageResources.getMessageResources("com.servinte.mensajes.medicamentos.SolicitudMedicamentosForm");
	
	private static final String KEY_SESSION_INSTITUCION = "institucionBasica";
	
	private static final String FORWARD_DETALLE_SOLICITUD = "detalleSolicitud";
								
	private static final String FORWARD_RESUMEN_INSERCION = "resumenInsercion";
	
	private static final String SOLICITUD_ANULADA = "Anulada";
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	@SuppressWarnings("deprecation")
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws SQLException
	{
		Connection con = null;
		try {
			if(form instanceof SolicitudMedicamentosForm)
			{

				SolicitudMedicamentos mundo=new SolicitudMedicamentos();
				SolicitudMedicamentosForm forma=(SolicitudMedicamentosForm)form;

				con = UtilidadBD.abrirConexion();	
				String estado=forma.getEstado();
				logger.info("\n\n\n Estado Sol Medicamentos--->"+estado+"\n\n\n");
				UsuarioBasico usuario= obtenerUsuario(con, forma, request);

				try {
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
				}
				catch (Exception e){
					Log4JManager.error(e);
				}
				
				if(forma.getMaxPageItems() == 0){
					forma.setMaxPageItems(10);
				}
				
				forma.setMostrarValidacionArticulos(new ResultadoBoolean(false));
				forma.setMostrarImprimirAutorizacion(false);
				forma.setListaNombresReportes(new ArrayList<String>());

				forma.setNombreArchivoGenerado("");

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de Despacho Medicamentos (null) ");
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Estado no valido dentro del flujo de Despacho Medicamentos (null) ", "errors.estadoInvalido", true);
				}

				if(estado.equals("consultarTipoListado"))
				{
					return this.accionConsultarTipoListado(con, forma, mundo, mapping, usuario);
				}
				if(estado.equals("consultarDefinido") && SolicitudMedicamentosForm.listadoPorArea == forma.getTipoListadoSolicitudes())
				{
					return this.accionConsultarDefinidoPorArea(con, forma, mundo, mapping, usuario);
				}
				else if(estado.equals("consultarDefinido") && SolicitudMedicamentosForm.listadoPorPaciente == forma.getTipoListadoSolicitudes())
				{
					forma.setEstado("consultar");
					estado=forma.getEstado();
				}

				PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				ValidacionesSolicitud validacion=new ValidacionesSolicitud(con, forma.getNumeroSolicitud(), usuario, paciente);

				int codigoCuentaPaciente=paciente.getCodigoCuenta();

				if(forma.getTipoListadoSolicitudes()==SolicitudMedicamentosForm.listadoPorPaciente || estado.equals("empezar") || estado.equals("insertarOrdenAmbulatoria"))
				{
					ActionForward validacionesAccesoForward= this.validacionesAcceso(estado, con, paciente, usuario, mapping, request, forma, codigoCuentaPaciente);
					if(validacionesAccesoForward!=null)
					{
						return validacionesAccesoForward;
					}
				}
				if(estado.equals("empezar"))
				{
					request.getSession().removeAttribute("JUSTIFICACION");

					ActionForward validacionesAccesoForward= this.validacionesAcceso(estado, con, paciente, usuario, mapping, request, forma, codigoCuentaPaciente);
					if (validacionesAccesoForward != null)
					{
						UtilidadBD.cerrarConexion(con);
						return validacionesAccesoForward ;
					}
					else
					{
						forma.resetAmbulatorias();
						forma.setHiddens("");
						return this.accionEmpezar(forma, usuario, paciente,mapping, con, request, mundo);
					}
				}
				if(estado.equals("empezarContinuar"))
				{
					if(forma.isIndicativoOrdenAmbulatoria())
					{
						this.accionCargarDatosOrdenAmbulatoria(con,forma,mundo,usuario,paciente);

						if(forma.isCentroCostoNoCorresponde())//FIXME Validacion DCU 65-V 1.55
						{	ActionErrors errores=new ActionErrors();
						errores.add("centroCostoNoCorres", new ActionMessage("errors.notEspecific",messageResource.getMessage("solicitudMedicamentosForm.centroCostoNoCorresponde")));
						saveErrors(request, errores);
						}
					}
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				if(estado.equals("corregir"))
				{
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				if(estado.equals("corregirModificar"))
				{
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("detalleSolicitud");
				}
				if(estado.equals("modificarMotivo"))
				{
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("detalleSolicitud");
				}
				else if(estado.equals("consultar"))
				{
					return this.accionConsultar(con, forma, mundo, paciente, mapping);
				}
				else if(estado.equals("guardar"))
				{
					return this.accionGuardar(mundo, con, forma, paciente, usuario, mapping, request);
				}
				else if(estado.equals("eliminar"))
				{
					return this.accionEliminar(con, forma, mapping);
				}
				else if(estado.equals("eliminarMedicamentoDiferenteDosificacion"))
				{
					return this.accionEliminarMedicamentoDiferenteDosificacion(con, forma, mapping);
				}
				else if (estado.equals("imprimir") || estado.equals("imprimirConsulta"))
				{
					//evaluamos si es media carta o no
					/*
					 * Se remueve por MT 5750, dice el DCU 65 que el formato es uno solo para cualquier institucion
					 * */
					//if(UtilidadTexto.getBoolean(ValoresPorDefecto.getImpresionMediaCarta(usuario.getCodigoInstitucionInt())))
						//return this.accionImprimirMediaCarta(con, forma, usuario, request, paciente, mapping);
					//else
						return this.accionImprimir(con, forma, mundo, usuario, paciente, request, mapping);
				}
				else if(estado.equals("imprimirnopos"))
				{
					return this.accionImprimirNoPos(forma, mundo, paciente, usuario, request, con, mapping);
				}
				else if(estado.equals("detalle")) 
				{
					return this.accionDetalle(forma, request, mundo, usuario, con, mapping, paciente);
				}
				else if(estado.equals("modificar")) 
				{
					return this.accionModificar(con, mundo, forma, usuario, mapping, paciente, validacion, request);
				}
				else if(estado.equals("modificarJustificacion"))
				{
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("detalleSolicitud");
				}
				else if(estado.equals("modificarEliminar"))
				{
					return this.accionModificarEliminar(con, forma, mapping);
				}
				else if(estado.equals("suspender"))
				{
					return this.accionSuspender(con, forma, mapping);
				}
				else if(estado.equals("guardarModificacion"))
				{
					return this.accionGuardarModificacion(mundo, forma, usuario, response, mapping, con, paciente, request);
				}
				else if(estado.equals("anular"))
				{
					return this.accionAnular(mundo, forma, paciente, usuario, con, mapping);
				}
				else if(estado.equals("anularSolicitud"))
				{
					return this.accionAnularSolicitud(forma, con, mundo, mapping, usuario, request, paciente);
				}
				else if(estado.equals("ordenar"))
				{
					return this.accionOrdenar(mundo, forma, con, paciente, mapping);
				}	
				else if(estado.equals("justificarMedicamento"))
				{
					return this.accionJustificarMedicamento(forma,request,mapping,con,paciente,usuario, mundo);
				}
				else if(estado.equals("insertarOrdenAmbulatoria"))
				{
					return this.accionEmpezar(forma, usuario, paciente,mapping, con, request, mundo);
				}
				else if(estado.equals("historicoMedicamentos"))
				{
					metodoHistoricoMedicamentos(con,false, forma, paciente,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("historicoSolicitud");
				}
				else if(estado.equals("reformulacionUltimaOrden"))
				{
					metodoUltimaOrden(con,forma,paciente,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("asignarMedicamentoDiferenteDosificacion"))
				{
					return this.accionAsignarMedicamentoDiferenteDosificacion(forma, mapping, con);
				}
				else if(estado.equals(""))
				{
					String paginaSiguiente=request.getParameter("paginaSiguiente");
					UtilidadBD.cerrarConexion(con);
					if(paginaSiguiente==null)
					{
						forma.setEstado("consultar");
						return mapping.findForward("listado");
					}
					if(paginaSiguiente.equals(""))
					{
						forma.setEstado("consultar");
						return mapping.findForward("listado");
					}
					try
					{
						response.sendRedirect(paginaSiguiente);
					}
					catch (IOException e1)
					{
						logger.error("Error redireccionando la página "+e1);
						forma.setEstado("consultar");
						return mapping.findForward("listado");
					}
				}
				else if(estado.equals("imprimirAutorizacion")){

					/*********Pendiente ****************/
					int codigoInstitucion = usuario.getCodigoInstitucionInt();
					forma.setFormatoImpAutorizacion(ValoresPorDefecto.getFormatoImpresionAutorEntidadSub(codigoInstitucion));
					if(forma.getFormatoImpAutorizacion().equals(ConstantesIntegridadDominio.acronimoFormatoImpresionVersalles))
					{

					}
					else{
						/*********Pendiente ****************/

					}



				}
				else if(estado.equals("inicializarNomArch")){
					forma.setNombreArchivoGenerado("");
					return mapping.findForward("principal");
				}
				else
				{
					UtilidadBD.cerrarConexion(con);
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
			}
			return null;
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
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarMedicamentoDiferenteDosificacion(Connection con, SolicitudMedicamentosForm forma,ActionMapping mapping) 
	{
		logger.info("\n\n**********************************************************accionEliminarMedicamentoDiferenteDosificacion************************************************************");
		
		forma.setMedicamento("asignadoMismoArticuloDifDosificacion_"+forma.getCodigoEliminado(), ConstantesBD.acronimoNo);
		
		forma.getMedicamentosDiferenteDosificacion().remove("MDD_via_"+forma.getCodigoEliminado());
		forma.getMedicamentosDiferenteDosificacion().remove("MDD_esPos_"+forma.getCodigoEliminado());
		forma.getMedicamentosDiferenteDosificacion().remove("MDD_codigoNaturaleza_"+forma.getCodigoEliminado());
		forma.getMedicamentosDiferenteDosificacion().remove("MDD_frecuencia_"+forma.getCodigoEliminado());
		forma.getMedicamentosDiferenteDosificacion().remove("MDD_cantidad_"+forma.getCodigoEliminado());
		forma.getMedicamentosDiferenteDosificacion().remove("MDD_cod_"+forma.getCodigoEliminado());
		forma.getMedicamentosDiferenteDosificacion().remove("MDD_es_pos_"+forma.getCodigoEliminado());
		forma.getMedicamentosDiferenteDosificacion().remove("MDD_observaciones_"+forma.getCodigoEliminado());
		forma.getMedicamentosDiferenteDosificacion().remove("MDD_dosis_"+forma.getCodigoEliminado());
		forma.getMedicamentosDiferenteDosificacion().remove("MDD_diastratamiento_"+forma.getCodigoEliminado());
		forma.getMedicamentosDiferenteDosificacion().remove("MDD_existencias_"+forma.getCodigoEliminado());
		
		String[] codigosV= forma.getMedicamentosDiferenteDosificacion("MDD_codigos").toString().split("-");
		String nuevosCodigos="";
		for(int w=0; w<codigosV.length; w++)
		{
			if(!codigosV[w].equals(forma.getCodigoEliminado()+""))
			{
				if(UtilidadTexto.isEmpty(nuevosCodigos))
					nuevosCodigos=codigosV[w];
				else
					nuevosCodigos+="-"+codigosV[w];
			}
		}
		
		forma.setMedicamentosDiferenteDosificacion("MDD_codigos", nuevosCodigos);
		
		int numRegistros= Integer.parseInt(forma.getMedicamentosDiferenteDosificacion("numRegistros")+"");
		numRegistros--;
		logger.info(numRegistros);
		
		forma.setMedicamentosDiferenteDosificacion("numRegistros", numRegistros);
		
		Utilidades.imprimirMapa(forma.getMedicamentosDiferenteDosificacion());
		
		logger.info("**finnnnnnnnnnnnnnnnnnnnnnnnnnnn accionEliminarMedicamentoDiferenteDosificacion**************************************************************************************\n\n");
		UtilidadBD.closeConnection(con);
        return mapping.findForward("principal");
	}

	/**
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionAsignarMedicamentoDiferenteDosificacion(SolicitudMedicamentosForm forma, ActionMapping mapping,Connection con) 
	{
		logger.info("\n\n**********************************************************accionAsignarMedicamentoDiferenteDosificacion************************************************************");
		
		Utilidades.imprimirMapa(forma.getMedicamentos());
		forma.setMedicamento("asignadoMismoArticuloDifDosificacion_"+forma.getCodArticuloSelDiferenteDosificacion(), ConstantesBD.acronimoSi);
		
		forma.setMedicamentosDiferenteDosificacion("MDD_via_"+forma.getCodArticuloSelDiferenteDosificacion(), "");
		forma.setMedicamentosDiferenteDosificacion("MDD_esPos_"+forma.getCodArticuloSelDiferenteDosificacion(), forma.getMedicamento("esPos_"+forma.getCodArticuloSelDiferenteDosificacion()));
		forma.setMedicamentosDiferenteDosificacion("MDD_codigoNaturaleza_"+forma.getCodArticuloSelDiferenteDosificacion(), forma.getMedicamento("codigoNaturaleza_"+forma.getCodArticuloSelDiferenteDosificacion()));
		forma.setMedicamentosDiferenteDosificacion("MDD_unidosis_"+forma.getCodArticuloSelDiferenteDosificacion(), "");
		forma.setMedicamentosDiferenteDosificacion("MDD_frecuencia_"+forma.getCodArticuloSelDiferenteDosificacion(), "");
		forma.setMedicamentosDiferenteDosificacion("MDD_cantidad_"+forma.getCodArticuloSelDiferenteDosificacion(), "");
		forma.setMedicamentosDiferenteDosificacion("MDD_cod_"+forma.getCodArticuloSelDiferenteDosificacion(), forma.getMedicamento("cod_"+forma.getCodArticuloSelDiferenteDosificacion()));
		forma.setMedicamentosDiferenteDosificacion("MDD_es_pos_"+forma.getCodArticuloSelDiferenteDosificacion(), forma.getMedicamento("es_pos_"+forma.getCodArticuloSelDiferenteDosificacion()));
		forma.setMedicamentosDiferenteDosificacion("MDD_observaciones_"+forma.getCodArticuloSelDiferenteDosificacion(), "");
		forma.setMedicamentosDiferenteDosificacion("MDD_dosis_"+forma.getCodArticuloSelDiferenteDosificacion(), "");
		forma.setMedicamentosDiferenteDosificacion("MDD_diastratamiento_"+forma.getCodArticuloSelDiferenteDosificacion(), forma.getMedicamento("diastratamiento_"+forma.getCodArticuloSelDiferenteDosificacion()));
		forma.setMedicamentosDiferenteDosificacion("MDD_existencias_"+forma.getCodArticuloSelDiferenteDosificacion(), forma.getMedicamento("existencias_"+forma.getCodArticuloSelDiferenteDosificacion()));
		
		if(UtilidadTexto.isEmpty(forma.getMedicamentosDiferenteDosificacion("MDD_codigos")+""))
			forma.setMedicamentosDiferenteDosificacion("MDD_codigos", forma.getCodArticuloSelDiferenteDosificacion());
		else
			forma.setMedicamentosDiferenteDosificacion("MDD_codigos", forma.getMedicamentosDiferenteDosificacion("MDD_codigos")+"-"+forma.getCodArticuloSelDiferenteDosificacion());
		
		int numRegistros= forma.getMedicamentosDiferenteDosificacion("MDD_codigos").toString().split("-").length;
		forma.setMedicamentosDiferenteDosificacion("numRegistros", numRegistros);
		
		Utilidades.imprimirMapa(forma.getMedicamentosDiferenteDosificacion());
		
		logger.info("**finnnnnnnnnnnnnnnnnnnnnnnnnnnn accionAsignarMedicamentoDiferenteDosificacion**************************************************************************************\n\n");
		UtilidadBD.closeConnection(con);
        return mapping.findForward("principal");
	}


	/**
	 * 
	 * @param mundo
	 * @param forma
	 * @param con
	 * @param paciente
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ActionForward accionOrdenar(SolicitudMedicamentos mundo, SolicitudMedicamentosForm forma, Connection con, PersonaBasica paciente, ActionMapping mapping) 
	{
		//------------------
		mundo.resetCompleto(); 
		Collection colTempo=null;
		if(SolicitudMedicamentosForm.listadoPorPaciente==forma.getTipoListadoSolicitudes())
		{
			if(paciente.getExisteAsocio())
			{
				int cuentaAsociada=UtilidadValidacion.tieneCuentaAsociada(con, paciente.getCodigoIngreso());
				colTempo=mundo.consultarSolicitudesMedicamento(con,cuentaAsociada, "", "", -1,-1,-1,-1);
				if(paciente.getCodigoCuenta()!=0)
				{
					colTempo.addAll(mundo.consultarSolicitudesMedicamento(con,paciente.getCodigoCuenta(), "", "", -1,-1,-1,-1));
				}
				else
				{
					forma.setMostrarMensajeAsocio(true);
				}
			}
			else
			{
				colTempo=mundo.consultarSolicitudesMedicamento(con, paciente.getCodigoCuenta(), "","",-1,-1,-1,-1);
			}
		}
		else if(SolicitudMedicamentosForm.listadoPorArea==forma.getTipoListadoSolicitudes())
		{
			colTempo=mundo.consultarSolicitudesMedicamento(con, -1, forma.getFechaInicialFiltro(), forma.getFechaFinalFiltro(), forma.getAreaFiltro(), forma.getPisoFiltro(), forma.getHabitacionFiltro(), forma.getCamaFiltro());
		}
		forma.setColeccion(colTempo);
		//-----------

		
	    this.accionOrdenar(forma);
	    UtilidadBD.closeConnection(con);
	    return mapping.findForward("principalModificacion");
	}

	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mundo
	 * @param mapping
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionAnularSolicitud(SolicitudMedicamentosForm forma, Connection con, SolicitudMedicamentos mundo,
			ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request, PersonaBasica paciente) throws SQLException 
	{
		Solicitud solicitud	= null;
		ActionMessages errores = new ActionMessages();
		try{
			//se debe validar por concurrencia que la solicitud este solicitada PENDIENTE
			solicitud= new Solicitud();
			solicitud.cargar(con, forma.getNumeroSolicitud());
		
		
			if(solicitud.getEstadoHistoriaClinica().getCodigo()!=ConstantesBD.codigoEstadoHCSolicitada)
			{
				UtilidadBD.closeConnection(con);
				logger.warn("Error en el estado de la solicitud número:"+forma.getNumeroSolicitud()+ " estado HC -> "+solicitud.getEstadoHistoriaClinica());
				forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
				ArrayList atributosError = new ArrayList();
				atributosError.add("Anular");
				atributosError.add("Pendiente");
				atributosError.add("Solicitada");
				request.setAttribute("codigoDescripcionError", "error.solicitudgeneral.estados");				
				request.setAttribute("atributosError", atributosError);
				return mapping.findForward("paginaError");
			}
			
			mundo.setNumeroSolicitud(forma.getNumeroSolicitud());
			mundo.setMotivoAnulacion(forma.getMotivoAnulacion());
			mundo.setCodigoMedicoAnulacion(usuario.getCodigoPersona());
			
			//en este punto se valida lo de pyp
			if(Utilidades.esSolicitudPYP(con, forma.getNumeroSolicitud()))
			{
				 String codActProg=Utilidades.obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(con,forma.getNumeroSolicitud());
			     if(Integer.parseInt(Utilidades.obtenerCodigoOrdenAmbulatoriaActividad(con, forma.getNumeroSolicitud()+""))>0)
			     {
			    	 Utilidades.actualizarEstadoActividadProgramaPypPaciente(con,codActProg,ConstantesBD.codigoEstadoProgramaPYPProgramado,usuario.getLoginUsuario(),"");
			    	 Utilidades.asignarSolicitudToActividadPYP(con,Utilidades.obtenerCodigoOrdenAmbulatoriaActividad(con,forma.getNumeroSolicitud()+""),forma.getNumeroSolicitud()+"");
			     }
			     else
			     {
			    	 Utilidades.actualizarEstadoActividadProgramaPypPaciente(con,codActProg,ConstantesBD.codigoEstadoProgramaPYPCancelado,usuario.getLoginUsuario(),"SOLICITUD CANCELADA");
			     }
			}
			// esta parte no va dentro de pyp, solo es requerido
			String codigoOrden=Utilidades.obtenerCodigoOrdenAmbulatoria(con, forma.getNumeroSolicitud()+"");
	        if(Integer.parseInt(codigoOrden)>0)
	    	{
	        	OrdenesAmbulatorias.actualizarEstadoOrdenAmbulatoria(con,codigoOrden,ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente);
	        	HashMap campos=new HashMap();
	        	campos.put("estadoOrden",ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente);
	        	campos.put("numeroSolicitud",ConstantesBD.codigoNuncaValido);
	        	campos.put("numeroOrden",codigoOrden);
	        	OrdenesAmbulatorias.actualizarSolicitudEnOrdenAmbulatoria(con,campos);
	    	}
			
	        /**INICIO ANULACION SOLICITUD Y AUTORIZACION----------------------------------------------*/
	        //int resultado = mundo.anularSolicitudTransaccional(con,ConstantesBD.finTransaccion);
		
			//Validaciones para la solicitud que se anula, si esta asociada a una autorizacion
			cargarInfoParaAnulacionAutorizacion(mundo,usuario);
			/**FIN ANULACION SOLICITUD Y AUTORIZACION-------------------------------------------------------*/
			
			
			/**
	         * Se inserta el registro de alerta para registro de enfermería MT-3438
	         */
			if (/*resultado > 0 && */Integer.parseInt(codigoOrden) < 1 && 
					UtilidadValidacion.esMedico(usuario).equals("")) {
				OrdenMedica ordenMedica = new OrdenMedica();
				ordenMedica.registrarAlertaOrdenMedica(con, 
						ConstantesBD.seccionMedicamentos, 
						new Long(paciente.getCodigoCuenta()), usuario.getLoginUsuario());
			}
		
			UtilidadBD.cerrarConexion(con);
			
		}catch(IPSException ipse){
			UtilidadBD.abortarTransaccion(con);
			//MT6013 Se agrega sentencia ipse.getParamsMsg()
			errores.add("ERROR Negocio", new ActionMessage(ipse.getErrorCode().toString(),ipse.getParamsMsg()));
			if(!errores.isEmpty()){
				saveErrors(request, errores);
			}
			return mapping.findForward("listado");
		}
		catch(Exception e){
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(e);
			errores.add("ERROR no Controlado", new ActionMessage("errors.notEspecific", e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		
		return mapping.findForward("listado");
	}

	/**
	 * 
	 * @param mundo
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param con
	 * @param mapping
	 * @return
	 */
	private ActionForward accionAnular(SolicitudMedicamentos mundo, SolicitudMedicamentosForm forma, PersonaBasica paciente, UsuarioBasico usuario, Connection con, ActionMapping mapping) 
	{
		mundo.setNumeroSolicitud(forma.getNumeroSolicitud());
		mundo.cargarSolicitudMedicamentos(con, usuario.getCodigoInstitucionInt());
		llenarForma(mundo, forma, paciente, usuario, new SolicitudMedicamentos());
		
		llenarFormaMedicamentos(con, mundo, forma, usuario);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("anularSolicitud");
	}


	/**
	 * 
	 * @param mundo
	 * @param forma
	 * @param usuario
	 * @param response
	 * @param mapping
	 * @param con
	 * @param paciente
	 * @param request
	 * @return
	 * @throws IPSException 
	 * @throws SQLException 
	 */
	private ActionForward accionGuardarModificacion(SolicitudMedicamentos mundo, SolicitudMedicamentosForm forma, UsuarioBasico usuario, HttpServletResponse response, ActionMapping mapping, Connection con, PersonaBasica paciente, HttpServletRequest request) throws IPSException, SQLException 
	{
		boolean fechaMod=false;
		boolean horaMod=false;		
		//boolean autMod=false;
		boolean cenCosMod=false;
		boolean urgMod=false;
		boolean obsMod=false;
		boolean artMod = false;
		mundo.setNumeroSolicitud(forma.getNumeroSolicitud());
		mundo.cargarSolicitudMedicamentos(con, usuario.getCodigoInstitucionInt());
		String consOrdenAmbula=null;
		boolean seModificaArticulo=false;
		OrdenesFacade ordenesFacade = new OrdenesFacade(); 
		ActionErrors errores = new ActionErrors();
		List<String> listaEstados = new ArrayList<String>();
		List<AutorizacionPorOrdenDto> listaAutorizacionesPorOrden = new ArrayList<AutorizacionPorOrdenDto>();
		ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade(); 
		ConvenioDto convenioDto = null;
		ContratoDto contratoDto = null;
		ContratosDelegate contratosDelegate = new ContratosDelegate();
		Contratos contratos = new Contratos();
		CentrosCosto centroCosto = new CentrosCosto();
		CentroCostosDelegate centroCostoDelegate = new CentroCostosDelegate();
		OrdenAutorizacionDto ordenAutorizacionDto = null;
		List<MedicamentoInsumoAutorizacionOrdenDto> listaArticulosPorAutorizar = null;
		MedicamentoInsumoAutorizacionOrdenDto medicamentoInsumoAutorizacionOrdenDto = null;
		ClaseInventarioDto claseInventarioDto	= null;
		InventarioFacade inventarioFacade 		= new InventarioFacade();
		List<OrdenAutorizacionDto> listaOrdenesAutorizar	= null;
		DatosPacienteAutorizacionDto datosPacienteAutorizar = null;
		MontoCobroDto montoCobroAutorizacion = null;
		AutorizacionCapitacionDto autorizacionCapitacionDto = null;
		List<AutorizacionCapitacionDto> listaAutorizacionCapitacion = null;
		AnulacionAutorizacionSolicitudDto anulacionAutorizacionSolicitudDto = null; 
		boolean esManual=false;
		listaEstados.add(ConstantesIntegridadDominio.acronimoAutorizado);
		String articuloEliminado = null;
		
		//Valido si se elimino todos los medicamentos
		if (forma.getNumeroElementos()!=0){
		//Verifico si la solicitud tiene asociada una orden ambulatoria
		consOrdenAmbula = ordenesFacade.existeOrdenAmbAsociadaSolicitud(forma.getNumeroSolicitud());
		if (consOrdenAmbula==null){
				
		String logInicial=	"\n            ====INFORMACIÓN ORIGINAL==== " +
					"\n*  Número Órden Médica	[" + mundo.getConsecutivoOrdenesMedicas() +"] ";

		if(!mundo.getFechaSolicitud().equals(forma.getFecha()))
		{
			logInicial+=
					"\n*  Fecha Solicitud		[" + mundo.getFechaSolicitud() +"] ";
			fechaMod=true;
		}
		if(!mundo.getHoraSolicitud().equals(forma.getHora()))
		{
			logInicial+=
					"\n*  Hora Solicitud		[" + mundo.getHoraSolicitud() +"] ";
			horaMod=true;
		}
		
		// quitar de aqui la verificacion del numero de autorizacion
		/*
		if(!mundo.getNumeroAutorizacion().equals(forma.getNumeroAutorizacion()))
		{
			logInicial+=
					"\n*  Número Autorización	[" + mundo.getNumeroAutorizacion() +"] ";
			autMod=true;
		}
		*/
		
		if(mundo.getCentroCostoSolicitado().getCodigo()!=forma.getCentroCostoSolicitado())
		{
			logInicial+=
				"\n*  Farmacia				[" + mundo.getCentroCostoSolicitado().getCodigo()+" - "+mundo.getCentroCostoSolicitado().getNombre() +"] ";
			cenCosMod=true;
		}
		if(mundo.getUrgente()!=forma.getUrgente())
		{
			logInicial+=
					"\n*  Urgente				[" + mundo.getUrgente() +"] ";
			urgMod=true;
		}
		if(!forma.getObservacionesGeneralesNuevas().equals(""))
		{
			logInicial+=
					"\n*  Observaciones				[" + mundo.getObservacionesGenerales() +"] ";
			obsMod=true;
		}
		
		ObjetoReferencia log=new ObjetoReferencia();
		
		logger.info("NUMERO SOLICITUD--------------------------->"+forma.getNumeroSolicitud());
		mundo.setNumeroSolicitud(forma.getNumeroSolicitud());
		
			articuloEliminado=(String)forma.getMedicamento("codigosEliminados");
		
			/*
			 * 3496-2
			 */
			listaAutorizacionesPorOrden = ordenesFacade.obtenerAutorizacionCapitacion(ConstantesBD.claseOrdenOrdenMedica, ConstantesBD.codigoTipoSolicitudMedicamentos, 
					Long.valueOf(forma.getNumeroSolicitud()), listaEstados);
		
			if (listaAutorizacionesPorOrden!=null && !listaAutorizacionesPorOrden.isEmpty()){
		
				//Valido si la orden tiene n autorizaciones asociadas
				if (listaAutorizacionesPorOrden.size() > 1){ 
		
					//Para las ordenes que tiene n autorizaciones asociadas verifico que todas las autorizaciones sean automaticas
					for (AutorizacionPorOrdenDto autorizacionPorOrden : listaAutorizacionesPorOrden) {
						if(autorizacionPorOrden.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoManual)){
							esManual=true;
							break;
						}
					}
				}
			}
			
			if (articuloEliminado!=null 
					&& !articuloEliminado.isEmpty()){
				seModificaArticulo=true;
			}
			
			
			if (!esManual){
				
				int eliminados=mundo.eliminarArticulosYSolSubCuenta(con,(String)forma.getMedicamento("codigosEliminados"), log, usuario.getCodigoInstitucionInt());
				
				String codigosSuspendidos=(String)forma.getMedicamento("codigosSuspendidos");
				
				//---------Se carga la solicitud porque estaba vaciá al momento de suspender -----//
				//mundo.setNumeroSolicitud(forma.getNumeroSolicitud());
				//mundo.cargarSolicitudMedicamentos(con, usuario.getCodigoInstitucionInt());
				
				llenarSuspensiones(con, codigosSuspendidos, forma, usuario, log, mundo);
				
				forma.setNumeroElementos(forma.getNumeroElementos()-eliminados);
				
		SolicitudMedicamentos solicitudTemporal=new SolicitudMedicamentos(mundo);
		mundo.resetMedicamentos();
		
		llenarMundo(con,mundo, forma, paciente, usuario);
		String codigos=forma.getMedicamento("codigos")+"";
		if(codigos.indexOf("-")!=-1)
		{
			String[] insertados=codigos.split("-");
			for(int i=0; i<forma.getNumeroElementos(); i++)
			{
				ArticuloSolicitudMedicamentos articuloAnterior=solicitudTemporal.getArticuloSolicitud(Integer.parseInt(insertados[i]));
				ArticuloSolicitudMedicamentos articuloTemporal=llenarMedicamento(Integer.parseInt(insertados[i]), forma, articuloAnterior, usuario);
						
						/*
						 * MT 3496-2
						 */
						if (articuloAnterior != null){
							if (articuloAnterior.getCantidadSolicitada() != articuloTemporal.getCantidadSolicitada()){
								seModificaArticulo=true;
							}
							if (!articuloAnterior.getDiasTratamiento().equals(articuloTemporal.getDiasTratamiento())){
								seModificaArticulo=true;
							}
							if(articuloAnterior.getDosis().equals(articuloTemporal.getDosis())){
								seModificaArticulo=true;
							}
							if (!articuloAnterior.getTipoFrecuencia().equals(articuloTemporal.getTipoFrecuencia()) 
									|| articuloAnterior.getFrecuencia() != articuloTemporal.getFrecuencia()){
								seModificaArticulo=true;
							}
							if (!articuloAnterior.getObservaciones().equals(articuloTemporal.getObservaciones())){
								seModificaArticulo=true;
							}
						}else{
							seModificaArticulo=true;
						}
							
				if(articuloTemporal.getArtPrincipal()==0)
				{
					mundo.adicionarArticuloSolicitud(articuloTemporal);
					artMod = true;
				}
			}
		}
		else
		{
			
					ArticuloSolicitudMedicamentos articuloAnt=solicitudTemporal.getArticuloSolicitud(Integer.parseInt(codigos));
					ArticuloSolicitudMedicamentos articuloTemporal=llenarMedicamento(Integer.parseInt(codigos), forma, articuloAnt, usuario);
					
					/*
					 * MT 3496-2
					 */
					if (articuloAnt != null){
						if (articuloAnt.getCantidadSolicitada() != articuloTemporal.getCantidadSolicitada()){
							seModificaArticulo=true;
						}
						if (!articuloAnt.getDiasTratamiento().equals(articuloTemporal.getDiasTratamiento())){
							seModificaArticulo=true;
						}
						/*
						 * debe evaluar si es diferente
						 * 
						 * jeilones
						 */
						if(!articuloAnt.getDosis().equals(articuloTemporal.getDosis())){
							seModificaArticulo=true;
						}
						if (!articuloAnt.getTipoFrecuencia().equals(articuloTemporal.getTipoFrecuencia()) 
								|| articuloAnt.getFrecuencia() != articuloTemporal.getFrecuencia()){
							seModificaArticulo=true;
						}
						if (!articuloAnt.getObservaciones().equals(articuloTemporal.getObservaciones())){
							seModificaArticulo=true;
						}
					}else{
						seModificaArticulo=true;
					}
						
					if(articuloTemporal.getArtPrincipal()==0)
					{
						mundo.adicionarArticuloSolicitud(articuloTemporal);
						artMod = true;
					}
					
					
					ArticuloSolicitudMedicamentos articuloAnterior=mundo.getArticuloSolicitud(Integer.parseInt(codigos));
					
					mundo.adicionarArticuloSolicitud(llenarMedicamento(Integer.parseInt(codigos), forma, articuloAnterior, usuario));
					artMod = true;
		}
		
		
		if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna||paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios)
		{
			HashMap<String, Object> articulos=new HashMap<String, Object>();
			for(int i=0; i<mundo.getArticulos().size(); i++)
			{
				articulos.put("articulo_"+i, ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(i)).getArticulo());
			}
			articulos.put("numRegistros",mundo.getArticulos().size()+"");
			
			int numDiasControl=Utilidades.convertirAEntero(ValoresPorDefecto.getNumeroDiasControlMedicamentosOrdenados(usuario.getCodigoInstitucionInt())+"");
			if(numDiasControl>0)
			{
				if(!forma.isGenerarOrdenArticulosConfirmada())
				{
					forma.setArticulosConfirmacion(Utilidades.consultarArticulosSolicitadosUltimosXDias(con,numDiasControl,articulos,paciente.getCodigoPersona()));
					HashMap<String,Object> mapaArticulos=forma.getArticulosConfirmacion();
					if(Utilidades.convertirAEntero(mapaArticulos.get("numRegistros")+"")>1)
					{
						String cadenaArticulos="";
						for(int i=0;i<Utilidades.convertirAEntero(mapaArticulos.get("numRegistros")+"");i++)
						{
							if(i>0)
								cadenaArticulos+=",";
							cadenaArticulos+=mapaArticulos.get("articulo_"+i);
							logger.info("\n ordenes medicamentos "+mapaArticulos.get("articulo_"+i));
						}
						
						forma.setMostrarValidacionArticulos(new ResultadoBoolean(true,"EL paciente presenta ordenes vigentes para el o los Medicamento(s) [<b>"+cadenaArticulos+"</b>], desea generar la orden de todas formas?"));
						UtilidadBD.closeConnection(con);
						return mapping.findForward("detalleSolicitud");
					}
				}
			}
		}
		
		mundo.modificarSolicitudMedicamentosYSolSubcuenta(con, log, usuario.getCodigoInstitucionInt(), usuario, paciente, forma,request);
		
		mundo.cargarSolicitudMedicamentos(con, usuario.getCodigoInstitucionInt());
		
		/**
         * Se inserta el registro de alerta para registro de enfermería MT-3438
         */
		
		
		if((cenCosMod || urgMod || obsMod || artMod || eliminados > 0 || 
				!codigosSuspendidos.equals("")) && 
				UtilidadValidacion.esMedico(usuario).equals("")) {
			OrdenMedica ordenMedica = new OrdenMedica();
			ordenMedica.registrarAlertaOrdenMedica(con, 
					ConstantesBD.seccionMedicamentos, 
					new Long(paciente.getCodigoCuenta()), usuario.getLoginUsuario());
		}
		/****/
		
//		fue requerida la confirmacion, y requiere guardar el log.
		if(forma.isGenerarOrdenArticulosConfirmada())
		{
			Utilidades.generarLogConfirmacionOrdenAmbSolMed(con,mundo.getNumeroSolicitud()+"",usuario.getLoginUsuario(),ConstantesBD.acronimoSi,forma.getArticulosConfirmacion());
		}
		
		
		forma.setGenerarOrdenArticulosConfirmada(false);
		
		
		String logModificado=
					"\n            ====INFORMACION DESPUES DE LA MODIFICACION==== " +
					"\n*  Número Órden Médica [" + mundo.getConsecutivoOrdenesMedicas() +"] ";
		
		if(fechaMod)
		{
			logModificado+=
					"\n*  Fecha Solicitud		[" + mundo.getFechaSolicitud() +"] ";
		}
		if(horaMod)
		{
			logModificado+=
					"\n*  Hora Solicitud		[" + mundo.getHoraSolicitud() +"] ";
		}
		/*
		if(autMod)
		{
			logModificado+=
					"\n*  Número Autorización	[" + mundo.getNumeroAutorizacion() +"] ";
		}
		*/
		if(cenCosMod)
		{
			logModificado+=
					"\n*  Farmacia				[" + mundo.getCentroCostoSolicitado().getCodigo()+" - "+mundo.getCentroCostoSolicitado().getNombre() +"] ";
		}
		if(urgMod)
		{
			logModificado+=
					"\n*  Urgente				[" + mundo.getUrgente() +"] ";
		}
				if(obsMod)
				{
					logModificado+=
							"\n*  Observaciones			[" + mundo.getObservacionesGenerales() +"] ";
				}
				
				log.setStringReferencia(logInicial+logModificado+log.getStringReferencia());
				
				LogsAxioma.enviarLog(ConstantesBD.logSolicitudMedimcanetosCodigo, log.getStringReferencia(), ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
				
				llenarForma(mundo, forma, paciente, usuario, new SolicitudMedicamentos());
				llenarFormaMedicamentos(con, mundo, forma, usuario);
				
				String paginaSiguiente=request.getParameter("paginaSiguiente");
			    UtilidadBD.closeConnection(con);
			    
		
				/*
				 * MT 3496-2
				 * Verifico se se modifico o ingreso un nuevo articulo
				 */
				if (seModificaArticulo){
					//Envio la información para validar la autorización
					convenioDto = new ConvenioDto();
					contratoDto = new ContratoDto();
					ordenAutorizacionDto = new OrdenAutorizacionDto();
					medicamentoInsumoAutorizacionOrdenDto = new MedicamentoInsumoAutorizacionOrdenDto();
					listaArticulosPorAutorizar	= new ArrayList<MedicamentoInsumoAutorizacionOrdenDto>();
					claseInventarioDto = new ClaseInventarioDto();
					listaOrdenesAutorizar = new ArrayList<OrdenAutorizacionDto>();
					datosPacienteAutorizar = new DatosPacienteAutorizacionDto();
					montoCobroAutorizacion = new MontoCobroDto();
					autorizacionCapitacionDto = new AutorizacionCapitacionDto();
					listaAutorizacionCapitacion = new ArrayList<AutorizacionCapitacionDto>();
					anulacionAutorizacionSolicitudDto = new AnulacionAutorizacionSolicitudDto();
					
					//Envio la información del convenio/contrato
					HibernateUtil.beginTransaction();
					contratos = contratosDelegate.findById(paciente.getCodigoContrato());
					contratoDto.setNumero(contratos.getNumeroContrato());
					centroCosto = centroCostoDelegate.findById(forma.getCentroCostoSolicitado());
					HibernateUtil.endTransaction();
					convenioDto.setCodigo(paciente.getCodigoConvenio());
					convenioDto.setNombre(paciente.getConvenioPersonaResponsable()); 
					
					//MT6112 se agregan datos de manejo de presupuesto para que realice la validación
					
					DtoSubCuentas dtoSubCuenta = null;
					if(forma.getInfoCoberturaArticulo()!=null&&forma.getInfoCoberturaArticulo().get(0).getDtoSubCuenta()!=null)
					{
						dtoSubCuenta = forma.getInfoCoberturaArticulo().get(0).getDtoSubCuenta();
						if(dtoSubCuenta.getDatosConvenio().getManejaPreupuestoCapitacion()!=null &&
								dtoSubCuenta.getDatosConvenio().getManejaPreupuestoCapitacion().equals(ConstantesBD.acronimoSiChar+"")){
							convenioDto.setConvenioManejaPresupuesto(true);
						}else{
							convenioDto.setConvenioManejaPresupuesto(false);
						}
						
					}
					else{
						convenioDto.setConvenioManejaPresupuesto(false);
					}
						
				
					//Fin MT
					contratoDto.setConvenio(convenioDto);
					contratoDto.setCodigo(paciente.getCodigoContrato());
					
					//Envio la información de la orden
					ordenAutorizacionDto.setCodigoOrden(Utilidades.convertirALong(forma.getNumeroSolicitud()+""));
					ordenAutorizacionDto.setContrato(contratoDto);
					ordenAutorizacionDto.setCodigoCentroCostoEjecuta(forma.getCentroCostoSolicitado());
					ordenAutorizacionDto.setTipoEntidadEjecuta(centroCosto.getTipoEntidadEjecuta());
					ordenAutorizacionDto.setEsPyp(forma.isSolPYP());
					ordenAutorizacionDto.setCodigoViaIngreso(paciente.getCodigoUltimaViaIngreso());
					ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenOrdenMedica);
					ordenAutorizacionDto.setTipoOrden(ConstantesBD.codigoTipoSolicitudMedicamentos);
					
					//Envio la información del articulo
					listaArticulosPorAutorizar 	= ordenesFacade.obtenerMedicamentosInsumosPorSolicitud(forma.getNumeroSolicitud());
					for(MedicamentoInsumoAutorizacionOrdenDto articulo: listaArticulosPorAutorizar){
						articulo.setAutorizar(true);
						claseInventarioDto	= inventarioFacade.obtenerClaseInventarioPorSubGrupo(articulo.getSubGrupoInventario());
						if(claseInventarioDto!=null){
							articulo.setClaseInventario(claseInventarioDto.getCodigo());
							articulo.setGrupoInventario(claseInventarioDto.getCodigoGrupo());
						}
					}
					
					if(forma.getUrgente()){
						ordenAutorizacionDto.setEsUrgente(true);
					}else{
						ordenAutorizacionDto.setEsUrgente(false);
					}
					ordenAutorizacionDto.setMedicamentosInsumosPorAutorizar(listaArticulosPorAutorizar);
					listaOrdenesAutorizar.add(ordenAutorizacionDto);
					
					//Envio la información del paciente
					InfoSubCuentaDto dtoDatosSubCuenta = manejoPacienteFacade.consultarInfoSubCuentaPorIngresoPorConvenio(paciente.getCodigoIngreso(), paciente.getCodigoConvenio());
					datosPacienteAutorizar.setCodigoPaciente(paciente.getCodigoPersona());
					datosPacienteAutorizar.setCodigoIngresoPaciente(paciente.getCodigoIngreso());
					datosPacienteAutorizar.setTipoPaciente(paciente.getCodigoTipoPaciente());
					datosPacienteAutorizar.setClasificacionSocieconomica(dtoDatosSubCuenta.getCodigoEstratoSocial());
					datosPacienteAutorizar.setTipoAfiliado(String.valueOf(dtoDatosSubCuenta.getCodigoTipoAfiliado()));
					datosPacienteAutorizar.setNaturalezaPaciente(dtoDatosSubCuenta.getCodigoNaturaleza());
					datosPacienteAutorizar.setCodConvenioCuenta(paciente.getCodigoConvenio());
					datosPacienteAutorizar.setCuentaAbierta(true);
					datosPacienteAutorizar.setCuentaManejaMontos(dtoDatosSubCuenta.isSubCuentaManejaMontos());
					datosPacienteAutorizar.setPorcentajeMontoCuenta(dtoDatosSubCuenta.getSubCuentaPorcentajeMonto());
					
					//Envio la información del monto
					montoCobroAutorizacion	= new MontoCobroDto();
					montoCobroAutorizacion.setCantidadMonto(dtoDatosSubCuenta.getMontoCobro().getCantidadMonto());
					montoCobroAutorizacion.setCodDetalleMonto(dtoDatosSubCuenta.getMontoCobro().getCodDetalleMonto());
					montoCobroAutorizacion.setPorcentajeMonto(dtoDatosSubCuenta.getMontoCobro().getPorcentajeMonto());
					montoCobroAutorizacion.setTipoDetalleMonto(dtoDatosSubCuenta.getMontoCobro().getTipoDetalleMonto());
					montoCobroAutorizacion.setTipoMonto(dtoDatosSubCuenta.getMontoCobro().getTipoMonto());
					montoCobroAutorizacion.setValorMonto(dtoDatosSubCuenta.getMontoCobro().getValorMonto());
					
					//Envio la información al dto general
					autorizacionCapitacionDto.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
					autorizacionCapitacionDto.setLoginUsuario(usuario.getLoginUsuario());
					autorizacionCapitacionDto.setCodigoPersonaUsuario(usuario.getCodigoPersona());
					autorizacionCapitacionDto.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAutomatica);
					autorizacionCapitacionDto.setCentroAtencion(usuario.getCodigoCentroAtencion());
					autorizacionCapitacionDto.setDatosPacienteAutorizar(datosPacienteAutorizar);
					autorizacionCapitacionDto.setMontoCobroAutorizacion(montoCobroAutorizacion);
					autorizacionCapitacionDto.setOrdenesAutorizar(listaOrdenesAutorizar);
		    		
					//Verifico si la solicitud tiene una autorización de capitación asociada
					listaAutorizacionesPorOrden = ordenesFacade.obtenerAutorizacionCapitacion(ConstantesBD.claseOrdenOrdenMedica, ConstantesBD.codigoTipoSolicitudMedicamentos, 
							Long.valueOf(forma.getNumeroSolicitud()), listaEstados);
					
					
					if (listaAutorizacionesPorOrden!=null && !listaAutorizacionesPorOrden.isEmpty()){
						
						//Valido si la orden tiene n autorizaciones asociadas
						if (listaAutorizacionesPorOrden.size() > 1){ 
							
							//Para las ordenes que tiene n autorizaciones asociadas verifico que todas las autorizaciones sean automaticas
							for (AutorizacionPorOrdenDto autorizacionPorOrden : listaAutorizacionesPorOrden) {
								if(autorizacionPorOrden.getTipoAutorizacion().equals(ConstantesIntegridadDominio.acronimoManual)){
									esManual=true;
									break;
								}
							}
							
							
							if (esManual){
								//Si se encuentro una autorización manual se cancela el proceso de modificación y de autorización
								errores.add("", new ActionMessage("errors.autorizacion.autorizacionManual"));
								 if(!errores.isEmpty()){
									saveErrors(request, errores);
								 }
								 return mapping.findForward("detalleSolicitud");
							}else{//Si no hay autorizaciones manuales se continua con el proceso de autorización
								
								//Realizo el proceso de anular el cual anula las autorizaciones asociadas a la orden y resta el valor 
								//de lo autorizado en la temporal del acumulado del cierre
								boolean procesoAnulacionExitoso=false;
								Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
								String horaActual = UtilidadFecha.getHoraActual();
								anulacionAutorizacionSolicitudDto.setMotivoAnulacion("Modificación de la orden de medicamentos");
								anulacionAutorizacionSolicitudDto.setFechaAnulacion(fechaActual);
								anulacionAutorizacionSolicitudDto.setHoraAnulacion(horaActual);
								anulacionAutorizacionSolicitudDto.setLoginUsuarioAnulacion(usuario.getLoginUsuario());
								
								procesoAnulacionExitoso = ordenesFacade.procesoAnulacionAutorizacion(ConstantesBD.claseOrdenOrdenMedica, ConstantesBD.codigoTipoSolicitudMedicamentos,
										listaAutorizacionesPorOrden, anulacionAutorizacionSolicitudDto, Integer.parseInt(usuario.getCodigoInstitucion()));
								
								//Verifico si el proceso de anulación es exitoso
								if (procesoAnulacionExitoso){
									//Genero la autorización de capitación subcontratada
									listaAutorizacionCapitacion = manejoPacienteFacade.verificarGenerarAutorizacionCapitacion(autorizacionCapitacionDto);
									
									if(listaAutorizacionCapitacion!=null && !listaAutorizacionCapitacion.isEmpty())
									{	//Se adiciona mensaje para los servicio que no se autorizaron
										manejoPacienteFacade.obtenerMensajesError(listaAutorizacionCapitacion, errores);
									}
									
									if(!errores.isEmpty()){
										saveErrors(request, errores);
									 }
									
								}else{
									 errores.add("", new ActionMessage("errors.autorizacion.noSeAnulaOrden"));
									 if(!errores.isEmpty()){
										saveErrors(request, errores);
									 }
								}
							}
							
						}else{//Si solo tiene una autorización asociada
							
							//Realizo el proceso de anular el cual anula las autorizaciones asociadas a la orden y resta el valor 
							//de lo autorizado en la temporal del acumulado del cierre
							boolean procesoAnulacionExitoso=false;
							Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
							String horaActual = UtilidadFecha.getHoraActual();
							anulacionAutorizacionSolicitudDto.setMotivoAnulacion("Modificación de la orden de medicamentos");
							anulacionAutorizacionSolicitudDto.setFechaAnulacion(fechaActual);
							anulacionAutorizacionSolicitudDto.setHoraAnulacion(horaActual);
							anulacionAutorizacionSolicitudDto.setLoginUsuarioAnulacion(usuario.getLoginUsuario());
							
							procesoAnulacionExitoso = ordenesFacade.procesoAnulacionAutorizacion(ConstantesBD.claseOrdenOrdenMedica, ConstantesBD.codigoTipoSolicitudMedicamentos,
									listaAutorizacionesPorOrden, anulacionAutorizacionSolicitudDto, Integer.parseInt(usuario.getCodigoInstitucion()));
							
							//Verifico si el proceso de anulación es exitoso
							if (procesoAnulacionExitoso){
								
								//Genero la autorización de capitación subcontratada
								listaAutorizacionCapitacion = manejoPacienteFacade.verificarGenerarAutorizacionCapitacion(autorizacionCapitacionDto);
								
								if(listaAutorizacionCapitacion!=null && !listaAutorizacionCapitacion.isEmpty())
								{	//Se adiciona mensaje para los servicio que no se autorizaron
									manejoPacienteFacade.obtenerMensajesError(listaAutorizacionCapitacion, errores);
								}
								
								if(!errores.isEmpty()){
									saveErrors(request, errores);
								 }
								
							}else{
								 errores.add("", new ActionMessage("errors.autorizacion.noSeAnulaOrden"));
								 if(!errores.isEmpty()){
									saveErrors(request, errores);
								 }
		}
		
						}
		
						}else{ //La orden no tiene asociada autorizaciones de capita o esta se encuentra en estado anulada
							//Proceso que se encarga de verificar si se generara o asociara autorizacion para la orden.
							listaAutorizacionCapitacion = manejoPacienteFacade.verificarGenerarAutorizacionCapitacion(autorizacionCapitacionDto);
		
							if(listaAutorizacionCapitacion!=null && !listaAutorizacionCapitacion.isEmpty())
							{	//Se adiciona mensaje para los servicio que no se autorizaron
								manejoPacienteFacade.obtenerMensajesError(listaAutorizacionCapitacion, errores);
							}
		
							if(!errores.isEmpty()){
								saveErrors(request, errores);
							 }
						}
				}
		try
		{
			if(paginaSiguiente!=null)
			{
				if(!paginaSiguiente.equals(""))
				{
					response.sendRedirect(paginaSiguiente);
				}
				else
				{
					forma.setEstado("consultar");
					return mapping.findForward("listado");
				}
			}
			else
			{
				forma.setEstado("consultar");
				return mapping.findForward("listado");
			}
				
		}
		catch (IOException e1)
		{
			logger.error("Error redireccionando la página "+e1);
			return null;
		}

				
			}else{ //Tiene una autorización Manual
				errores.add("", new ActionMessage("errors.autorizacion.canceladoPorAutoManual"));
				if(!errores.isEmpty()){
					saveErrors(request, errores);
				 }
				 return mapping.findForward("detalleSolicitud");
			}
				
		}else{//Si tiene asociada una orden ambulatoria se cancela proceso de modificación de la orden
			
			 errores.add("", new ActionMessage("errors.autorizacion.solicitudAsociadaOrdenAmb", consOrdenAmbula));
			 if(!errores.isEmpty()){
				saveErrors(request, errores);
			 }
			 return mapping.findForward("detalleSolicitud");
		}
		
		}else{ //Si se elimino todos los medicamentos se debe mostrar la pagina del detalle e indicar al usuario
			// que debe ingresar un medicamento. MT 5944
			 errores.add("", new ActionMessage("errors.autorizacion.eliminaTodosArticulosSolicitud "));
			 if(!errores.isEmpty()){
				saveErrors(request, errores);
			 }
			 return mapping.findForward("detalleSolicitud");
		}
		return mapping.findForward("resumenModificacion");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionSuspender(Connection con, SolicitudMedicamentosForm forma, ActionMapping mapping) 
	{
		UtilidadBD.closeConnection(con);
		//String codigos=forma.getMedicamento("codigos")+"";
		String codigosSuspendidos=(String)forma.getMedicamento("codigosSuspendidos");
		//boolean primero=false;
		int codigoSuspendido=forma.getCodigoEliminado();
		if(!codigosSuspendidos.equals(""))
		{
			codigosSuspendidos+="-"+codigoSuspendido;
		}
		else
		{
			codigosSuspendidos=codigoSuspendido+"";
		}
		forma.setMedicamento("codigosSuspendidos",codigosSuspendidos);
		return mapping.findForward("detalleSolicitud");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionModificarEliminar(Connection con, SolicitudMedicamentosForm forma, ActionMapping mapping) 
	{
		UtilidadBD.closeConnection(con);
		String codigos=forma.getMedicamento("codigos")+"";
		String codigosEliminados=(String)forma.getMedicamento("codigosEliminados");
		boolean primero=false;
		int codigoEliminado=forma.getCodigoEliminado();
		if(codigosEliminados!=null)
		{
			codigosEliminados+="-"+codigoEliminado;
		}
		else
		{
			codigosEliminados=codigoEliminado+"";
		}
		forma.setMedicamento("codigosEliminados",codigosEliminados);
		if(codigos.indexOf("-")!=-1)
		{
			String[] codigosTemp=codigos.split("-");
			codigos="";
			for(int j=0; j<codigosTemp.length; j++)
			{
				if(codigoEliminado!=Integer.parseInt(codigosTemp[j]))
				{
					if(!primero)
					{
						codigos=codigosTemp[j];
						primero=true;
					}
					else
					{
						codigos+="-"+codigosTemp[j];
					}
				}
			}
			forma.setMedicamento("codigos",codigos);
		}
		else
		{
			forma.setMedicamento("codigos","0");
		}
		forma.setNumeroElementos(forma.getNumeroElementos()-1);
		return mapping.findForward("detalleSolicitud");
	}

	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param paciente
	 * @return
	 */
	private ActionForward accionModificar(Connection con, SolicitudMedicamentos mundo, SolicitudMedicamentosForm forma, UsuarioBasico usuario, ActionMapping mapping, PersonaBasica paciente, ValidacionesSolicitud validacion,HttpServletRequest request) 
	{

		ActionMessages mensajeInformativo = new ActionMessages();
		mundo.setNumeroSolicitud(forma.getNumeroSolicitud());
		mundo.cargarSolicitudMedicamentos(con, usuario.getCodigoInstitucionInt());
		//forma.setColeccionInsumos(mundo.cargarInsumos(con));
		llenarForma(mundo, forma, paciente, usuario, new SolicitudMedicamentos());
		llenarFormaMedicamentos(con, mundo, forma, usuario);
		
		/**
		 * Inc. 443
		 * No se debe permitir modificar una orden que ya tenga despachos
		 * @author Diana Ruiz
		 * @since 10/08/2011 
		 */
		forma.setTieneDespacho(validacion.tieneDespachos());
		if(UtilidadValidacion.esEnfermera(usuario).equals(""))
		{
			forma.setPuedoModificar(false);
		}
		else
		{
			forma.setPuedoModificar(validacion.puedoAnularModificarSolicitudMedicamentos().isTrue());
		}
		forma.setPuedoSuspender(validacion.puedoSuspenderSolicitudMedicamentos().isTrue());
		if(mundo.getEstadoHistoriaClinica().getCodigo() != ConstantesBD.codigoEstadoHCSolicitada &&
				mundo.getEstadoHistoriaClinica().getCodigo() != ConstantesBD.codigoEstadoHCDespachada)	{
			//Se comenta porque según documento no aplica esta la validación
			//if(!Utilidades.hayDespachosEnSolicitud(con, mundo.getNumeroSolicitud())) {
			forma.setPuedoSuspender(false);
			//}
		}
        
		// Mt 6495 Mensaje informativo para medicamentos no pos con una solicitud asociada
		if (mundo.getEstadoHistoriaClinica().getCodigo() == ConstantesBD.codigoEstadoHCSolicitada
				&& !forma.getJustificacionModifica().isEmpty()
				&& !forma.isTieneDespacho()) {
			   
			  mensajeInformativo.add("",new ActionMessage("errors.notEspecific","El artículo "+
			                         forma.getJustificacionModifica()+
			                         " Es No Pos y tiene una Justificación asociada. No puede ser modificada"));
			 
			  saveErrors(request, mensajeInformativo);
			  
		}

		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleSolicitud");
	}

	/**
	 * 
	 * @param forma
	 * @param request
	 * @param mundo
	 * @param usuario
	 * @param con
	 * @param mapping
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionDetalle(SolicitudMedicamentosForm forma, HttpServletRequest request, SolicitudMedicamentos mundo, UsuarioBasico usuario, Connection con, ActionMapping mapping, PersonaBasica paciente) throws SQLException 
	{
		if(forma.getTipoListadoSolicitudes()==SolicitudMedicamentosForm.listadoPorArea)
		{
			int codigoPersona=Integer.parseInt(request.getParameter("codigoPersona"));
	        PersonaBasica persona=(PersonaBasica) request.getSession().getAttribute("pacienteActivo");
	        persona.cargarPaciente(con, codigoPersona, usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
	        persona.cargar(con, codigoPersona);
	        persona.cargarPaciente2(con, codigoPersona, usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");

			// Código necesario para registrar este paciente como Observer
			Observable observable = (Observable) request.getSession().getServletContext().getAttribute("observable");
			if (observable != null) 
			{
				persona.setObservable(observable);
				// Si ya lo habíamos añadido, la siguiente línea no hace nada
				observable.addObserver(persona);
			}
		}
		

		/**
		 * Cambio por responder cirugia ANEXO-395
		 */
		/*
		 * la validacion fue solicitada en el anexo 395
		 * per se quita por la tarea 754 del xplanner 2008
		 * @author Carlos Mario

		if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.ingresoEstadoDiferente", "errors.ingresoEstadoDiferenteAbierto", true);
		}
		if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta())<1)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.cuentaNoValida", "errors.paciente.cuentaNoValida", true);
		}
		*/
		
		
		mundo.setNumeroSolicitud(forma.getNumeroSolicitud());
		
		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		logger.info("<<Numero Solicitud: " + forma.getNumeroSolicitud());
		logger.info("***************************************************************************************");
		logger.info("PASO POR ACA 111111111111111111111111111111111111111111111111111111111111111111111111111");
		logger.info("***************************************************************************************");
		
		mundo.cargarSolicitudMedicamentos(con, usuario.getCodigoInstitucionInt());
		logger.info("***************************************************************************************");
		logger.info("PASO POR ACA 22222");
		logger.info("***************************************************************************************");
		forma.setColeccionInsumos(mundo.cargaInsumosFarmacia(con,forma.getNumeroSolicitud()));
		logger.info("***************************************************************************************");
		logger.info("PASO POR ACA 33333333");
		logger.info("***************************************************************************************");
		boolean tempoResumenAtenciones=forma.isResumenAtencion();
		logger.info("***************************************************************************************");
		logger.info("ANTES DE LLENAR FORMA");
		logger.info("***************************************************************************************");
		llenarForma(mundo, forma, paciente, usuario, new SolicitudMedicamentos());
		logger.info("***************************************************************************************");
		logger.info("DESPUES DE LLENAR FORMA");
		logger.info("***************************************************************************************");
		logger.info("***************************************************************************************");
		logger.info("ANTES DE LLENAR FORMA MEDICAMENTOS");
		logger.info("***************************************************************************************");
		llenarFormaMedicamentos(con, mundo, forma, usuario);
		logger.info("***************************************************************************************");
		logger.info("DESPUES DE LLENAR FORMA MEDICAMENTOS");
		logger.info("***************************************************************************************");
						
		/** validación para la tarea oid=10741 **/
		if((forma.getCodigoTipoRegimen()!='P'&&forma.getCodigoTipoRegimen()!='O')&&
		   (mundo.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCDespachada||
			mundo.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCAdministrada)&&
			UtilidadValidacion.esMedicoTratante(con,usuario,paciente).equals(""))
		{
			
			forma.setEsPosibleJustificar('S');
		}
		else
		{
			forma.setEsPosibleJustificar('N');
		}
		UtilidadBD.cerrarConexion(con);
		forma.setResumenAtencion(tempoResumenAtenciones);
		return mapping.findForward("detalleSolicitud");
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
	private ActionForward accionImprimirNoPos(SolicitudMedicamentosForm forma, SolicitudMedicamentos mundo, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request, Connection con, ActionMapping mapping) 
	{
		String nombreArchivo;
    	Random r=new Random();
    	nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
    	int articulonopos=forma.getCodigoArticuloAImprimir();
    	mundo.setNumeroSolicitud(forma.getNumeroSolicitud());
    	mundo.cargarSolicitudMedicamentos(con,usuario.getCodigoInstitucionInt());
		llenarForma(mundo, forma, paciente, usuario, new SolicitudMedicamentos());
		llenarFormaMedicamentos(con, mundo, forma, usuario);
    	SolicitudMedicamentosPdf.pdfSolicitudMedicamentosNoPos(ValoresPorDefecto.getFilePath() + nombreArchivo, mundo, usuario,paciente,articulonopos,con);
    	request.setAttribute("nombreArchivo", nombreArchivo);
        request.setAttribute("nombreVentana", "Consulta Diagnósticos");
        UtilidadBD.closeConnection(con);
        return mapping.findForward("abrirPdf");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, SolicitudMedicamentosForm forma, SolicitudMedicamentos mundo, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping) 
	{
		if (forma.getCheckCE().equals("S"))
		{
			
		DesignEngineApi comp;        
		InstitucionBasica institucionBasica = new InstitucionBasica();
        institucionBasica.cargarXConvenio(usuario.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
        
        UsuarioBasico usuarioGeneraSolicitud = new UsuarioBasico();
    	String login=Solicitud.obtenerLoginMedicoSolicita(con, forma.getNumeroSolicitud(), usuario.getCodigoInstitucionInt());
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
        
    	String reporte="";
    	if (forma.getCheckCE().equals("S"))
    	{
    	reporte="solicitudMedicamentosCE.rptdesign";
        if(!UtilidadTexto.isEmpty(usuarioGeneraSolicitud.getFirmaDigital()))
    	reporte="solicitudMedicamentosFirmaDigitalCE.rptdesign";	
    	}
    			
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
        v.add(institucionBasica.getDireccion());
        v.add("Tels. "+institucionBasica.getTelefono());
        comp.insertGridHeaderOfMasterPage(0,1,1,v.size());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        //en la segunda celda se coloca el logo de la institucion
        comp.insertImageHeaderOfMasterPage1(0, 2, institucionBasica.getLogoReportes());
        
        if(!UtilidadTexto.isEmpty(usuarioGeneraSolicitud.getFirmaDigital()))
			comp.insertImageBodyPage(0, 0, usuarioGeneraSolicitud.getPathFirmaDigital(), "grillaFirmaDigital");
        
        comp.insertLabelBodyPage(0, 0, institucionBasica.getPieHistoriaClinica(), "piehiscli");
		
        //comp.insertGridHeaderOfMasterPage(0,1,1,4);
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        if(!newPathReport.equals(""))
        {
            request.setAttribute("isOpenReport", "true");
            request.setAttribute("newPathReport", newPathReport);
        }            
        comp.updateJDBCParameters(newPathReport);
        
        UtilidadBD.closeConnection(con);
        
        if(forma.getEstado().equals("imprimirConsulta"))
        	return mapping.findForward("detalleSolicitud");
        
		return mapping.findForward("resumenInsercion");
		}
		
    	mundo.setNumeroSolicitud(forma.getNumeroSolicitud());
    	if (mundo.getControlEspecial() == null)	{
    		mundo.setControlEspecial(forma.getCheckCE());
    	}
    	
    	SolicitudMedicamentos mundoMDD = new SolicitudMedicamentos();
    	
    	if(forma.getNumeroOrdenMedicaMedDiferenteDosificacion() > 0) {	
    		mundoMDD= (SolicitudMedicamentos)mundo.clone();
    		mundoMDD.setNumeroSolicitud(forma.getNumeroSolicitudMedDiferenteDosificacion());
    		
    	}
	
		//hermorhu - MT6628
		//Generacion del nuevo reporte
		generarReporteSolicitudMedicamentos(con, request, forma, paciente, usuario, mundo, mundoMDD);
	
        UtilidadBD.closeConnection(con);
        
        if(forma.getEstado().equals("imprimirConsulta")) {
        	return mapping.findForward(FORWARD_DETALLE_SOLICITUD);
        } else {	
//        	if (forma.getEstado().equals("imprimir"))
        	return mapping.findForward(FORWARD_RESUMEN_INSERCION);
        }	

	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param paciente
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ActionForward accionImprimirMediaCarta(	Connection con,
													SolicitudMedicamentosForm forma, UsuarioBasico usuario,
													HttpServletRequest request, PersonaBasica paciente,
													ActionMapping mapping) 
	{
		DesignEngineApi comp;        
		InstitucionBasica institucionBasica = new InstitucionBasica();
        institucionBasica.cargarXConvenio(usuario.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
        
        UsuarioBasico usuarioGeneraSolicitud = new UsuarioBasico();
    	String login=Solicitud.obtenerLoginMedicoSolicita(con, forma.getNumeroSolicitud(), usuario.getCodigoInstitucionInt());
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
        
    	String reporte="";
    	if (forma.getCheckCE().equals("S"))
    	{
    	reporte="solicitudMedicamentosMediaCartaCE.rptdesign";
        if(!UtilidadTexto.isEmpty(usuarioGeneraSolicitud.getFirmaDigital()))
    	reporte="solicitudMedicamentosMediaCartaFirmaDigitalCE.rptdesign";	
    	}
    	else {
    	reporte="solicitudMedicamentosMediaCarta.rptdesign";
    	if(!UtilidadTexto.isEmpty(usuarioGeneraSolicitud.getFirmaDigital()))
			reporte="solicitudMedicamentosMediaCartaFirmaDigital.rptdesign";}
		
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
        v.add(institucionBasica.getDireccion());
        v.add("Tels. "+institucionBasica.getTelefono());
        comp.insertGridHeaderOfMasterPage(0,1,1,v.size());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        //en la segunda celda se coloca el logo de la institucion
        comp.insertImageHeaderOfMasterPage1(0, 2, institucionBasica.getLogoReportes());
        
        if(!UtilidadTexto.isEmpty(usuarioGeneraSolicitud.getFirmaDigital()))
			comp.insertImageBodyPage(0, 0, usuarioGeneraSolicitud.getPathFirmaDigital(), "grillaFirmaDigital");
        
        comp.insertLabelBodyPage(0, 0, institucionBasica.getPieHistoriaClinica(), "piehiscli");
		
        //comp.insertGridHeaderOfMasterPage(0,1,1,4);
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        if(!newPathReport.equals(""))
        {
            request.setAttribute("isOpenReport", "true");
            request.setAttribute("newPathReport", newPathReport);
        }            
        comp.updateJDBCParameters(newPathReport);
        
        UtilidadBD.closeConnection(con);
        
        if(forma.getEstado().equals("imprimirConsulta"))
        	return mapping.findForward("detalleSolicitud");
        
		return mapping.findForward("resumenInsercion");
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminar(Connection con, SolicitudMedicamentosForm forma, ActionMapping mapping) 
	{
		String codigos=forma.getMedicamento("codigos")+"";
		boolean primero=false;
		int codigoEliminado=forma.getCodigoEliminado();
		if(codigos.indexOf("-")!=-1)
		{
			String[] codigosTemp=codigos.split("-");
			codigos="";
			for(int j=0; j<codigosTemp.length; j++)
			{
				if(codigoEliminado!=Integer.parseInt(codigosTemp[j]))
				{
					if(!primero)
					{
						codigos=codigosTemp[j];
						primero=true;
					}
					else
					{
						codigos+="-"+codigosTemp[j];
					}
				}
			}
			forma.setMedicamento("codigos",codigos);
			
			// si es reformulacion y se borrar un articulo inactivo, se verifica si quedan más articulos inactivos
			if (forma.isEsreform())
			{
			codigos=forma.getMedicamento("codigos")+"";
			codigosTemp=codigos.split("-");
			forma.setEsreform(false);
			for(int k=0; k<codigosTemp.length; k++)
			{
				if(forma.getMedicamento("estado_"+codigosTemp[k])!=null)
				{
					if (forma.getMedicamento("estado_"+codigosTemp[k]).equals("0"))
					{
						forma.setEsreform(true);
					}
				}
			
			}
			}
		}
		else
		{
			forma.setMedicamento("codigos","0");
		}
		
		forma.setNumeroElementos(forma.getNumeroElementos()-1);
		
		if(UtilidadTexto.getBoolean(forma.getMedicamento("asignadoMismoArticuloDifDosificacion_"+forma.getCodigoEliminado())+""))
		{
			return this.accionEliminarMedicamentoDiferenteDosificacion(con, forma, mapping);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * 
	 * @param mundo
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "deprecation" })
	private ActionForward accionGuardar(SolicitudMedicamentos mundo, Connection con, SolicitudMedicamentosForm forma,
			PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) throws Exception 
	{
		ActionErrors errores = new ActionErrors();
		try {
			//--------Validacion Ingreso Cuidados Especiales Anexo 637--------//
			int aux=Utilidades.verificarValoracionIngresoCuidadosEspeciales(con, paciente.getCodigoIngreso());


			if(aux==3)
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente es de cuidados especiales pero no tiene valoracion inicial", "error.solicitudgeneral.tieneValoracionInicialCuidadosEspeciales", true);
			}
			//------------------------------------------------------------------//
			mundo.resetMedicamentos();
			llenarMundo(con,mundo, forma, paciente, usuario);

			String codigos=forma.getMedicamento("codigos")+"";
			if(codigos.indexOf("-")!=-1)
			{
				String[] insertados=codigos.split("-");
				for(int i=0; i<forma.getNumeroElementos(); i++)
				{
					if (forma.isEsreform())
					{
					if(forma.getMedicamento("estado_"+insertados[i])==null)
					{
						mundo.adicionarArticuloSolicitud(llenarMedicamento(Integer.parseInt(insertados[i]), forma, null, usuario));
				    }
					else {
						if(forma.getMedicamento("estado_"+insertados[i]).equals("1"))
						{
							mundo.adicionarArticuloSolicitud(llenarMedicamento(Integer.parseInt(insertados[i]), forma, null, usuario));
						}
					}
				    }
					else{				
					mundo.adicionarArticuloSolicitud(llenarMedicamento(Integer.parseInt(insertados[i]), forma, null, usuario));
					}
					}
			}
			else
			{		
			mundo.adicionarArticuloSolicitud(llenarMedicamento(Integer.parseInt(codigos), forma, null, usuario));
			}

			//si el tipo de orden ambulatoria es de articulos y consulta externa hacer la validacion de articulos pedidos con dias en otra orden.
			//((ArticuloSolicitudMedicamentos)articulos.elementAt(i)).getArticulo()
			if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna 
					||paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios)
			{
				logger.info("\n\n\n entro  a validacion de articulo orden ambulatoria");
				HashMap<String, Object> articulos=new HashMap<String, Object>();
				for(int i=0; i<mundo.getArticulos().size(); i++)
				{
					articulos.put("articulo_"+i, ((ArticuloSolicitudMedicamentos)mundo.getArticulos().elementAt(i)).getArticulo());
				}
				articulos.put("numRegistros",mundo.getArticulos().size()+"");

				int numDiasControl=Utilidades.convertirAEntero(ValoresPorDefecto.getNumeroDiasControlMedicamentosOrdenados(usuario.getCodigoInstitucionInt())+"");
				if(numDiasControl>0)
				{
					if(!forma.isGenerarOrdenArticulosConfirmada())
					{
						forma.setArticulosConfirmacion(Utilidades.consultarArticulosSolicitadosUltimosXDias(con,numDiasControl,articulos,paciente.getCodigoPersona()));
						HashMap<String,Object> mapaArticulos=forma.getArticulosConfirmacion();
						if(Utilidades.convertirAEntero(mapaArticulos.get("numRegistros")+"")>1)
						{
							String cadenaArticulos="";
							for(int i=0;i<Utilidades.convertirAEntero(mapaArticulos.get("numRegistros")+"");i++)
							{
								if(i>0)
									cadenaArticulos+=",";
								cadenaArticulos+=mapaArticulos.get("articulo_"+i);
								logger.info("\n ordenes medicamentos "+mapaArticulos.get("articulo_"+i));
							}
							forma.setMostrarValidacionArticulos(new ResultadoBoolean(true,"EL paciente presenta ordenes vigentes para el o los Medicamento(s) [<b>"+cadenaArticulos+"</b>], desea generar la orden de todas formas?"));
							UtilidadBD.closeConnection(con);
							return mapping.findForward("principal");
						}
					}
				}
			}

			//		VALIDAR PARAMETRO GENERAL REGISTRO EVOLUCIONES PARA ORDENES -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
			String  parametroEvoluciones = Solicitud.consultarParametroEvoluciones(con); 
			boolean validacionViaIngreso=false;

			if(parametroEvoluciones.equals(ConstantesBD.acronimoSi))
			{
				int codigoCuenta = paciente.getCodigoCuenta();

				if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
				{
					logger.info("ES VIA INGRESO HOSPITALIZACION");
					validacionViaIngreso=true;
				}
				else
					if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias && 
							UtilidadValidacion.tieneValoracionConConductaCamaObservacion(con, codigoCuenta))
					{
						logger.info("ES VIA INGRESO URGENCIAS EN OBSERVACION");
						validacionViaIngreso=true;
					}

				// PREGUNTO SI PASA EL FILTRO DE LA VIA INGRESO
				if(validacionViaIngreso == true)
				{
					String fechaValoracionInicial = UtilidadesOrdenesMedicas.consultarFechaValoracionInicial(con, paciente.getCodigoCuenta());
					logger.info(".......... FECHA VALORACION INICIAL ----->"+fechaValoracionInicial);
					logger.info(".......... FECHA SOLICITUD--------- ----->"+UtilidadFecha.conversionFormatoFechaABD(forma.getFecha()));
					if(! fechaValoracionInicial.equals(UtilidadFecha.conversionFormatoFechaABD(forma.getFecha())))
					{
						// PERMITO GENERAR LA SOLICITUD SIN VALIDAR AUE EXISTAN LAS EVOLUCIONES
						boolean evoluciones = UtilidadValidacion.tieneEvolucionesParaElDia(con, paciente.getCodigoCuenta(), UtilidadFecha.conversionFormatoFechaABD(forma.getFecha()));

						if(evoluciones == false)
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Registrar Evolucion al Paciente antes de Continuar", "error.solicitudgeneral.validarEvolucion", true);
					}
				}
			}

			//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-

			UtilidadBD.iniciarTransaccion(con);
			logger.info("\n\n\n va a generar la solicitud"+forma.getMedicamentos());
			String codigoCentroCostoPrincipal = Utilidades.obtenerCentroCostoPrincipal(con, forma.getCentroCostoSolicitado()+"", usuario.getCodigoInstitucion());
			logger.info("\n\n\n\n CENTRO DE COSTO -------> : "+forma.getCentroCostoSolicitado()+"\n\n");

			logger.info("\n=====>Obtenemos el Centro  de Costo Principal de la Farmacia "+forma.getCentroCostoSolicitado()+": "+codigoCentroCostoPrincipal);

			int indicador=mundo.ingresarSolicitudMedicamentos(con, usuario.getCodigoInstitucionInt(), usuario, paciente, forma, codigoCentroCostoPrincipal, request);

			if(indicador<0)
			{
				logger.info("\n\n\n [ABORTA EL PROCESO POR VALIDACION MEDICO ESPECIALISTA] \n\n\n");
				UtilidadBD.abortarTransaccion(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "la solicitud de medicamentos", "errors.problemasBd", true);
			}

			/**
             * Se inserta el registro de alerta para registro de enfermería MT-3438
             */
			if(UtilidadValidacion.esMedico(usuario).equals("") &&
					(!UtilidadTexto.isEmpty(forma.getGeneraAlertaEnfermeria()) && 
					forma.getGeneraAlertaEnfermeria().equals(ConstantesBD.acronimoSi) || forma.isSolPYP())) {
				OrdenMedica ordenMedica = new OrdenMedica();
				ordenMedica.registrarAlertaOrdenMedica(con, 
					ConstantesBD.seccionMedicamentos, 
					new Long(paciente.getCodigoCuenta()), usuario.getLoginUsuario());
			}
			forma.setGeneraAlertaEnfermeria(ConstantesBD.acronimoNo);
			/****/
			
			///INSERTAMOS LOS MEDICAMENTOS CON DIFERENTE DOSIFICACION
			SolicitudMedicamentos mundoClonado= (SolicitudMedicamentos)mundo.clone();
			mundoClonado.setNumeroSolicitud(0);
			mundoClonado.setArticulos(new Vector());

			String codigosMDD=forma.getMedicamentosDiferenteDosificacion("MDD_codigos")+"";

			if(!UtilidadTexto.isEmpty(codigosMDD)){
				mundoClonado= accionGuardarMedicamentosDiferenteDosificacion(mundoClonado, con, forma, paciente, usuario, mapping, request);
			}

			if(mundoClonado==null)
			{
				logger.info("\n\n\n [ABORTA EL PROCESO POR CARGA DEL MUNDO CLONADO] \n\n\n");
				UtilidadBD.abortarTransaccion(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "la solicitud de medicamentos", "errors.problemasBd", true);
			}

			if(!mundo.cargarSolicitudMedicamentos(con,usuario.getCodigoInstitucionInt()))
			{
				logger.info("\n\n\n [ABORTA EL PROCESO POR CARGA DEL MUNDO CLONADO] \n\n\n");
				UtilidadBD.abortarTransaccion(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "la solicitud de medicamentos", "errors.problemasBd", true);
			}

			logger.info("\n*********numeroSolicitud->"+mundo.getNumeroSolicitud());

			//		fue requerida la confirmacion, y requiere guardar el log.
			if(forma.isGenerarOrdenArticulosConfirmada())
			{
				Utilidades.generarLogConfirmacionOrdenAmbSolMed(con,mundo.getNumeroSolicitud()+"",usuario.getLoginUsuario(),ConstantesBD.acronimoSi,forma.getArticulosConfirmacion());
			}

			llenarForma(mundo, forma, paciente, usuario, mundoClonado);
			llenarFormaMedicamentos(con, mundo, forma, usuario);
			llenarFormaMedicamentosDiferenteDosificacion(con, mundoClonado, forma, usuario);

			if(!UtilidadTexto.isEmpty(codigosMDD))
			{	
				Vector<String> vectorNumeroSolicitudes= new Vector<String>();
				vectorNumeroSolicitudes.add(forma.getNumeroSolicitud()+"");
				vectorNumeroSolicitudes.add(forma.getNumeroSolicitudMedDiferenteDosificacion()+"");
				if(!Solicitud.actualizarNumeroOrdenMedDiferenteDosificacion(con, vectorNumeroSolicitudes))
				{
					logger.info("\n\n\n [ABORTA EL PROCESO POR CARGA DEL MUNDO CLONADO] \n\n\n");
					UtilidadBD.abortarTransaccion(con);
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "la solicitud de medicamentos", "errors.problemasBd", true);
				}
			}	

			forma.setGenerarOrdenArticulosConfirmada(false);		
			forma.setEstado("resumen");
			logger.info("\n\n\nINSERTO EL 100% DE LA SOLICITUD DE MEDICAMENTOS........\n\n\n");
			UtilidadBD.finalizarTransaccion(con);


			//Se agrega funcionaliad por versión  1.52 del CU 65 Solicitud de Medicamentos 
			int numeroSolicitud=mundo.getNumeroSolicitud();
			//Date fechaSolicitud = UtilidadFecha.conversionFormatoFechaStringDate(mundo.getFechaSolicitud());
			DtoDiagnostico dtoDiagnostico = null;
			dtoDiagnostico = mundo.getDtoDiagnostico();

			forma.setNumeroSolicitud(numeroSolicitud);
			forma.setFecha(mundo.getFechaSolicitud());
			forma.setDtoDiagnostico(dtoDiagnostico);

			//Se captura la excepcion para no bloquear el flujo
			try{
				//VALIDACIONES PARA GENERACION DE AUTORIZACION O ASOCIACION CON LA/LAS NUEVAS SOLICITUDES
				cargarInfoVerificarGeneracionAutorizacion(con, forma, usuario, paciente, errores);
				saveErrors(request, errores);
			}catch (IPSException e) {
				Log4JManager.error(e);
				ActionMessages mensajeError = new ActionMessages();
				mensajeError.add("", new ActionMessage(String.valueOf(e.getErrorCode())));
				saveErrors(request, mensajeError);
			}
			
		} catch (Exception e) {
			HibernateUtil.abortTransaction();
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		} 
		return mapping.findForward("resumenInsercion");
	}

	/**
	 * 
	 * @param mundo
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private SolicitudMedicamentos accionGuardarMedicamentosDiferenteDosificacion(SolicitudMedicamentos mundo, Connection con, SolicitudMedicamentosForm forma, PersonaBasica paciente, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)  throws IPSException
	{
		mundo.setArticulos(new Vector());
		llenarMundo(con,mundo, forma, paciente, usuario);
		String codigos=forma.getMedicamentosDiferenteDosificacion("MDD_codigos")+"";
		
		if(!UtilidadTexto.isEmpty(codigos))
		{	
			if(codigos.indexOf("-")!=-1)
			{
				String[] insertados=codigos.split("-");
				for(int i=0; i<insertados.length; i++)
				{
					mundo.adicionarArticuloSolicitud(llenarMedicamentoDiferenteDosificacion(Integer.parseInt(insertados[i]), forma, null, usuario));
				}
			}
			else
			{
				mundo.adicionarArticuloSolicitud(llenarMedicamentoDiferenteDosificacion(Integer.parseInt(codigos), forma, null, usuario));
			}
			
			logger.info("\n\n\n va a generar la solicitud"+forma.getMedicamentos());
			String codigoCentroCostoPrincipal = Utilidades.obtenerCentroCostoPrincipal(con, forma.getCentroCostoSolicitado()+"", usuario.getCodigoInstitucion());
			logger.info("\n=====>Obtenemos el Centro  de Costo Principal de la Farmacia "+forma.getCentroCostoSolicitado()+": "+codigoCentroCostoPrincipal);
			
			if(mundo.ingresarSolicitudMedicamentos(con, usuario.getCodigoInstitucionInt(), usuario, paciente, forma, codigoCentroCostoPrincipal, request)<0)
				return null;
			
			
			logger.info("\n***********numeroSolicitudMDD-->"+mundo.getNumeroSolicitud());
		}
		return mundo;
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionConsultar(Connection con, SolicitudMedicamentosForm forma, SolicitudMedicamentos mundo, PersonaBasica paciente, ActionMapping mapping) 
	{
		logger.info("*******************ENTRÓ A ACCION CONSULTAR*******************************************");
		/*forma.reset();*/
		mundo.resetCompleto();
		Collection colTempo=null;
		Collection colMedicamentos=null;
		boolean cuentaAsocio=false;
		
		if(SolicitudMedicamentosForm.listadoPorPaciente==forma.getTipoListadoSolicitudes())
		{
			if(paciente.getExisteAsocio())
			{
				
				if(paciente.getCodigoCuentaAsocio()>0){
					colTempo=mundo.consultarSolicitudesMedicamento(con,paciente.getCodigoCuentaAsocio(), "", "", -1, -1, -1, -1);
					//MT 4477 Se adiciona validacion para que muestre el detalle de los medicamentos cuando tiene solicitudes con cuenta de asocio
					colMedicamentos=mundo.consultarListadoMedicamentosPaciente(con, paciente.getCodigoCuentaAsocio());
					cuentaAsocio=true;
				}
				if(paciente.getCodigoCuenta()!=0)
				{
					//se deja de esta manera porque no estaba mostrando la info completa para asocios SUBA - INMACULADA
					if(colTempo == null)
						colTempo=mundo.consultarSolicitudesMedicamento(con,paciente.getCodigoCuenta(), "", "", -1,-1,-1,-1);
					else
						colTempo.addAll(mundo.consultarSolicitudesMedicamento(con,paciente.getCodigoCuenta(), "", "", -1,-1,-1,-1));
					//Tarea 2070. Se documentaron las dos lineas anteriores
					//colTempo=mundo.consultarSolicitudesMedicamento(con,cuentaAsociada, "", "", -1, -1, -1, -1);
				}
				else
				{
					forma.setMostrarMensajeAsocio(true);
				}
			}
			else
			{
				colTempo=mundo.consultarSolicitudesMedicamento(con, paciente.getCodigoCuenta(), "", "", -1, -1, -1, -1);
			}
		}
		else if(SolicitudMedicamentosForm.listadoPorArea==forma.getTipoListadoSolicitudes())
		{
			colTempo=mundo.consultarSolicitudesMedicamento(con,  0, forma.getFechaInicialFiltro(), forma.getFechaFinalFiltro(), forma.getAreaFiltro(), forma.getPisoFiltro(), forma.getHabitacionFiltro(), forma.getCamaFiltro());
		}
		
		//MT 4477 Se adiciona validacion para que muestre el detalle de los medicamentos cuando tiene solicitudes con cuenta de asocio
		if(cuentaAsocio){
			colMedicamentos.addAll(mundo.consultarListadoMedicamentosPaciente(con, paciente.getCodigoCuenta()));
		}else{
			colMedicamentos=mundo.consultarListadoMedicamentosPaciente(con, paciente.getCodigoCuenta());
		}
		forma.setMedicamentosSolicitadosPaciente(colMedicamentos);
		
		forma.setColeccion(colTempo);
				
		if(colTempo!=null && !colTempo.isEmpty())
		{
			Iterator iterador = colTempo.iterator();
			int cont = 0;
			while(iterador.hasNext())
			{
				HashMap elemento = (HashMap)iterador.next();
				logger.info("Orden N° "+cont+": "+elemento.get("orden"));
				cont++;
			}
		}
		logger.info("*******************FINALIZÓ A ACCION CONSULTAR*******************************************");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principalModificacion");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @return
	 */	
	private ActionForward accionConsultarTipoListado(Connection con, SolicitudMedicamentosForm forma, SolicitudMedicamentos mundo, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//forma.resetAmbulatorias();
		forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		mundo.resetCompleto();
		forma.setTipoListadoSolicitudes(SolicitudMedicamentosForm.listadoNoDefinido);
		forma.setEstado("consultarDefinido");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("definirTipoListado");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @return
	 */
	private ActionForward accionConsultarDefinidoPorArea(Connection con, SolicitudMedicamentosForm forma, SolicitudMedicamentos mundo, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//forma.resetAmbulatorias();
		forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		forma.setCentroCostoSolicitado(-1);
		mundo.resetCompleto();
		forma.setEstado("consultar");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("consultarCentroCosto");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param request
	 * @return
	 */
	private UsuarioBasico obtenerUsuario(Connection con, SolicitudMedicamentosForm forma, HttpServletRequest request) 
	{
		UsuarioBasico usuario=new UsuarioBasico();
		if(forma.isIndicativoOrdenAmbulatoria())
		{
			int institucion=((UsuarioBasico)request.getSession().getAttribute("usuarioBasico")).getCodigoInstitucionInt();
			String infoSol=OrdenesAmbulatorias.obtenerInfoServicioProcOrdenAmbulatoria(con,Utilidades.obtenerCodigoOrdenAmbulatoria(con,forma.getOrdenAmbulatoria(),institucion), institucion);
			if(!infoSol.trim().equals(""))
			{
				String[] vectorInfoSol=infoSol.split(ConstantesBD.separadorSplit);
				try 
				{
					usuario.cargarUsuarioBasico(con,vectorInfoSol[3]);
				} 
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
			}
			else
			{
				usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			}
		}
		else
		{
			usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		}
		
		if(usuario.getCodigoInstitucionInt()==0)
			usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		
		return usuario;
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param paciente 
	 * @param usuario 
	 */
    @SuppressWarnings({ "static-access", "rawtypes", "unchecked" })
	private void accionCargarDatosOrdenAmbulatoria(Connection con, SolicitudMedicamentosForm forma, SolicitudMedicamentos mundo, UsuarioBasico usuario, PersonaBasica paciente) throws SQLException
    {
    	try{
	    	logger.info("***********************************************************************************");
	    	String codigoOA=Utilidades.obtenerCodigoOrdenAmbulatoria(con,forma.getOrdenAmbulatoria(),usuario.getCodigoInstitucionInt());
	    	HashMap mapaArticulosOrden=Utilidades.consultarDetalleOrdenAmbulatoriaArticulos(con,codigoOA);
	    	forma.setFecha(UtilidadFecha.getFechaActual());
	    	forma.setHora(UtilidadFecha.getHoraActual());
	    	forma.setCentroCostoSolicitante(OrdenesAmbulatorias.obtenerCentroCostoSolicitante(con,codigoOA));
	    	for(int i=0;i<Integer.parseInt(mapaArticulosOrden.get("numRegistros")+"");i++)
	    	{
	    		ArticuloSolicitudMedicamentos articulo=new ArticuloSolicitudMedicamentos();
	    		articulo.setArticulo(Integer.parseInt(mapaArticulosOrden.get("articulo_"+i)+""));
	    		articulo.setCantidadSolicitada(Integer.parseInt(mapaArticulosOrden.get("cantidad_"+i)+""));
	    		if(UtilidadTexto.getBoolean((mapaArticulosOrden.get("medicamento_"+i)+"")))
	    		{
	    			articulo.setObservaciones(mapaArticulosOrden.get("observaciones_"+i)+"");
	    			articulo.setDosis(mapaArticulosOrden.get("dosis_"+i)+"");
	    			articulo.setFrecuencia(Integer.parseInt(mapaArticulosOrden.get("frecuencia_"+i)+""));
	    			//articulo.setFrecuencia(mapaArticulosOrden.get("frecuencia_"+i)+"");
	    			articulo.setTipoFrecuencia(mapaArticulosOrden.get("tipofrecuencia_"+i)+"");
	    			articulo.setVia(mapaArticulosOrden.get("nomvia_"+i)+"");
	    			articulo.setEsPos((mapaArticulosOrden.get("espos_"+i)+"").equals("")?"":(mapaArticulosOrden.get("espos_"+i)+""));
	    			articulo.setUnidosis(mapaArticulosOrden.get("unidosis_"+i).toString());
	    			articulo.setDiasTratamiento(mapaArticulosOrden.get("duraciontratamiento_"+i).toString());
	    		}
	        	mundo.getArticulos().add(articulo);
	    	}
	    	logger.info("ACTION : "+mundo.getArticulos());
	    	mundo.setFechaSolicitud(UtilidadFecha.getFechaActual());
	    	mundo.setHoraSolicitud(UtilidadFecha.getHoraActual());
			mundo.setCentroCostoSolicitado(new InfoDatosInt(forma.getCentroCostoSolicitado()));
			mundo.setCentroCostoSolicitante(new InfoDatosInt(forma.getCentroCostoSolicitante()));
			String infoSol=OrdenesAmbulatorias.obtenerInfoServicioProcOrdenAmbulatoria(con,Utilidades.obtenerCodigoOrdenAmbulatoria(con,forma.getOrdenAmbulatoria(),usuario.getCodigoInstitucionInt()), usuario.getCodigoInstitucionInt());
			
			if(!infoSol.trim().equals(""))
			{
				//urgente - observaciones - centro_atencion_solicita - usuario_solicita - especialidad_solicita - servicio - finalidad
				String[] vectorInfoSol=infoSol.split(ConstantesBD.separadorSplit);
				mundo.setUrgente(UtilidadTexto.getBoolean(vectorInfoSol[0]));
				mundo.setObservacionesGenerales(vectorInfoSol[1]);
				mundo.setEspecialidadSolicitante(new InfoDatosInt(Integer.parseInt(vectorInfoSol[4])));
			}
			
			
			//Se consulta el Diagn&oacute;stico asociado a la orden ambulatoria
			//para ser guardado en la solicitud de medicamentos
			int codigoOrdenAmbulatoria = Integer.parseInt(codigoOA);
			DtoDiagnostico dtoDiagnostico = OrdenesAmbulatorias.consultarDiagnosticoOrdenAmbulatoria(con, codigoOrdenAmbulatoria);
			mundo.setDtoDiagnostico(dtoDiagnostico);
			
		 //------------------------------------------------------------------------------
	        //Validación Centro costo cuando existe autorización asociada a Orden RQF 02-0025-DCU 65 v 1.55 
	       
			HibernateUtil.beginTransaction();
			IAutorizacionCapitacionOrdenesAmbulatoriasMundo autorizacionCapitacionOrdenesAmbulatoriasMundo=CapitacionFabricaMundo.crearAutorizacionCapitacionOrdenesAmbulatoriasMundo();
			DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizCapitaOrdenAmbu=new DtoAutorizacionCapitacionOrdenAmbulatoria();
			ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria> listAutCapiOrdenAmbu=new ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria>();
			dtoAutorizCapitaOrdenAmbu.getDtoOrdenesAmbulatorias().setNumeroOrden(forma.getOrdenAmbulatoria());
			//dtoAutorizCapitaOrdenAmbu = autorizacionCapitacionOrdenesAmbulatoriasMundo.existeAutorizacionOrdenAmbul(dtoAutorizCapitaOrdenAmbu);
			listAutCapiOrdenAmbu = autorizacionCapitacionOrdenesAmbulatoriasMundo.existeAutorizacionesOrdenAmbul(dtoAutorizCapitaOrdenAmbu);
			//FIXME probar
			
			for (DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutoCapiOrdAmb : listAutCapiOrdenAmbu) {
				
				if(forma.getCentroCostoSolicitado()==dtoAutoCapiOrdAmb.getCodigoCentrosCostoSolicitadoAutoriz())
				{
					FarmaciaCentroCosto farmaciaCentroCosto=new FarmaciaCentroCosto();
			        HashMap farmaciaMap=new HashMap();
			        farmaciaCentroCosto.setCentroCosto(dtoAutoCapiOrdAmb.getCodigoCentrosCostoSolicitadoPaciente());
			        farmaciaMap = farmaciaCentroCosto.consultar(con, farmaciaCentroCosto);
			        
			        if(!farmaciaMap.isEmpty())
			        {
			        	for(int i=0;i<=Utilidades.convertirAEntero(farmaciaMap.get("numRegistros").toString());i++)
			        	{
			        		Integer valor= Integer.parseInt(farmaciaMap.get("codigodet_"+i).toString());
			        		if(valor.equals(dtoAutoCapiOrdAmb.getCodigoCentrosCostoSolicitadoAutoriz())){
			        			forma.setCentroCostoNoCorresponde(false);			        			
			        			break;
			        		}else{
			        			forma.setCentroCostoNoCorresponde(true);
			        	}
			        }
			        	if(!forma.isCentroCostoNoCorresponde()){
			        		break;
			        	}
			        }
				}else{
					forma.setCentroCostoNoCorresponde(true);
				}
			}
	        //------------------------------------------------------------------------------		
			
			llenarForma(mundo, forma, paciente, usuario, new SolicitudMedicamentos());
			llenarFormaMedicamentos(con, mundo, forma, usuario);
			HibernateUtil.endTransaction();
    	}catch (Exception e) {
			Log4JManager.error("ERROR accionCargarDatosOrdenAmbulatoria",e);
			HibernateUtil.abortTransaction();
		}
	}


	/**
     * estado donde se listan los almacenes 
     * @param forma
     * @param usuario
	 * @param paciente 
     * @param mapping
     * @param con
     * @param req
     * @return
     * @throws SQLException
     */
	private ActionForward accionEmpezar (   SolicitudMedicamentosForm forma, 
                                            UsuarioBasico usuario, 
                                            PersonaBasica paciente, ActionMapping mapping, 
                                            Connection con, 
                                            HttpServletRequest req,
                                            SolicitudMedicamentos mundo)throws SQLException
    {
        //Limpiamos lo que venga del form
    	boolean postularArticulo = UtilidadTexto.getBoolean(req.getParameter("postularArticulo"));
        forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
        forma.resetCollection();
        mundo.resetCompleto();
        forma.setEsOrdenAmbulatoria(true);
        
        if(UtilidadTexto.getBoolean(req.getParameter("solPYP")))
        	forma.setSolPYP(true);
        else
        	forma.setSolPYP(false);
        forma.setPostularArticulo(postularArticulo);
        //@todo quitar el comentatio cuando se tenga a estructura  de las advertencias
        //cargosForm.resetMensajes();
        UsuarioBasico usuarioEnAplicacion = Utilidades.getUsuarioBasicoSesion(req.getSession());
    	
        
//      VALIDAR PARAMETRO GENERAL REGISTRO EVOLUCIONES PARA ORDENES -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
		Solicitud solicitud = new Solicitud();
		String  parametroEvoluciones = Solicitud.consultarParametroEvoluciones(con); 
		boolean validacionViaIngreso=false;
		
		if(parametroEvoluciones.equals(ConstantesBD.acronimoSi))
		{
			logger.info("<<<<<<< PARAMETRO EVOLUCIONES ->>>>>>"+parametroEvoluciones+" | ESTA ACTIVADA | ");
			
			logger.info("FECHAAAAAAAA->"+UtilidadFecha.conversionFormatoFechaABD(forma.getFecha())+"<-");
			
			int codigoCuenta = paciente.getCodigoCuenta();
			
			if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
			{
				logger.info("ES VIA INGRESO HOSPITALIZACION");
				validacionViaIngreso=true;
			}
			else
				if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias && 
						UtilidadValidacion.tieneValoracionConConductaCamaObservacion(con, codigoCuenta))
				{
					logger.info("ES VIA INGRESO URGENCIAS EN OBSERVACION");
					validacionViaIngreso=true;
				}
			
			// PREGUNTO SI PASA EL FILTRO DE LA VIA INGRESO
			if(validacionViaIngreso == true)
			{
				String fechaValoracionInicial = UtilidadesOrdenesMedicas.consultarFechaValoracionInicial(con, paciente.getCodigoCuenta());
				logger.info(".......... FECHA VALORACION INICIAL ----->"+fechaValoracionInicial);
				logger.info(".......... FECHA SOLICITUD--------- ----->"+UtilidadFecha.conversionFormatoFechaABD(forma.getFecha()));
				if(! fechaValoracionInicial.equals(UtilidadFecha.conversionFormatoFechaABD(forma.getFecha())))
				{
					// PERMITO GENERAR LA SOLICITUD SIN VALIDAR AUE EXISTAN LAS EVOLUCIONES
					boolean evoluciones = UtilidadValidacion.tieneEvolucionesParaElDia(con, paciente.getCodigoCuenta(), UtilidadFecha.conversionFormatoFechaABD(forma.getFecha()));
					
					if(evoluciones == false)
					{
						ActionErrors errores = new ActionErrors(); 
		            	errores.add("Registrar Evolucion al Paciente antes de Continuar", new ActionMessage("error.solicitudgeneral.validarEvolucion"));
		            	saveErrors(req, errores);
		                UtilidadBD.cerrarConexion(con);
		                return mapping.findForward("paginaErroresActionErrors");
					}
						
				}
			}
		}
		
		//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
        
        
        
        
        
        // CAPTURO EL NUMERO DE CONVENIOS PLAN ESPECIAL POR INGRESO
        forma.setNumConveniosPlanEspecial(mundo.conveniosPlanEspecial(con, paciente.getCodigoIngreso()));
        
        logger.info("Cantidad Convenios Plan Especial por Ingreso -> "+forma.getNumConveniosPlanEspecial()+" >> ingreso >> "+paciente.getCodigoIngreso());
        
        boolean planEspecial;
        // PREGUNTO SI LOS CONVENIOS SON MAYORES A CERO PARA MOSTRAR LOS ALMACENES
        if(forma.getNumConveniosPlanEspecial()>0)
        {
        	logger.info("SI TIENE CONVENIOS PLAN ESPECIAL");
        	planEspecial=true;
        	logger.info("Numero de Almacenes Parametrizados como Plan Especial --->"+UtilidadInventarios.existeAlmacenPlanEspecial(con, usuario.getCodigoCentroAtencion())+" >> centro de atencion >> "+usuario.getCodigoCentroAtencion());
        	if(UtilidadInventarios.existeAlmacenPlanEspecial(con, usuario.getCodigoCentroAtencion()) < 1)
    	    {
        		ActionErrors errores = new ActionErrors(); 
            	errores.add("No hay definido almacenes Plan Especial", new ActionMessage("error.inventarios.parametrosAlmacenPlanEspecial"));
            	saveErrors(req, errores);
                UtilidadBD.cerrarConexion(con);
                return mapping.findForward("paginaErroresActionErrors");
    	    }
        }
        else
        {
        	logger.info("NO TIENE CONVENIOS PLAN ESPECIAL");
        	planEspecial=false;
        }	
     
        forma.setListadoAlmacenesMap(UtilidadInventarios.listadoAlmacenes(
															    			usuarioEnAplicacion.getCodigoInstitucionInt(), 
															    			paciente.getCodigoArea(),
															                ValoresPorDefecto.getCodigoTransSoliPacientes(usuarioEnAplicacion.getCodigoInstitucionInt(),true),
															                "", 
															                planEspecial));
        
        forma.setRequeridoComentarios(UtilidadTexto.getBoolean(ValoresPorDefecto.getRequeridoComentariosSolicitar(usuario.getCodigoInstitucionInt())));
        
        
        // si es cero entonces sacar el error
        if(forma.getListadoAlmacenesMap("numRegistros").equals("0"))
        {
            //error.inventarios.noExistenAlmacenesPermitidos
            ActionErrors errores = new ActionErrors();     
            //errores.add("no hay almacenes permitidos", new ActionMessage("error.inventarios.noExistenAlmacenesPermitidos","SOLICITUDES DE ARTICULOS"));
            errores.add("falta parametrizar el usuario", new ActionMessage("error.inventarios.centroCostoSinAlmacen",paciente.getArea()));
            
            saveErrors(req, errores);
            if(!con.isClosed())
            	UtilidadBD.cerrarConexion(con);
            return mapping.findForward("paginaErroresActionErrors");

        }
        //si el numero de registros es uno entonces pasar a la pagina siguiente
        else if(forma.getListadoAlmacenesMap("numRegistros").equals("1"))
        {
            //@todo hacer cambios
            forma.setCentroCostoSolicitado(Integer.parseInt(forma.getListadoAlmacenesMap("codigo_0").toString()));
            forma.setNombreCentroCostoSolicitado(forma.getListadoAlmacenesMap("nombre_0").toString());
            forma.setEstado("empezar");
            if(forma.isIndicativoOrdenAmbulatoria())
        	{
        		this.accionCargarDatosOrdenAmbulatoria(con,forma,mundo,usuario,paciente);
        		
				if(forma.isCentroCostoNoCorresponde()) {	
					ActionErrors errores=new ActionErrors();
					errores.add("centroCostoNoCorres", new ActionMessage("errors.notEspecific",messageResource.getMessage("solicitudMedicamentosForm.centroCostoNoCorresponde")));
					saveErrors(req, errores);
				}
        	}
            UtilidadBD.cerrarConexion(con);
            return mapping.findForward("principal");
        }
        else
        {
            UtilidadBD.cerrarConexion(con);
            return mapping.findForward("listadoAlmacenes");
        }
    }
    
	/**
	 * Adición Sebastián
	 * Método para justificar un medicamento en la opción de consultar/modificar, por los siguientes casos:
	 * 1) sustitución
	 * 2) cambio de tipo de régimen en la cuenta
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param logger2
	 */
	private ActionForward accionJustificarMedicamento(SolicitudMedicamentosForm forma, HttpServletRequest request, ActionMapping mapping, Connection con, PersonaBasica paciente, UsuarioBasico usuario, SolicitudMedicamentos mundo) 
	{
		String numeroStr=forma.getJustificacion("numero")+"";
		String solicitudStr=forma.getJustificacion("solicitud")+"";
		String articuloStr=forma.getJustificacion("articulo")+"";
		
		Articulo articulo=new Articulo();
		
		try
		{
			if(numeroStr!=null)
			{
				int cont=Integer.parseInt(numeroStr);
				int solicitud=Integer.parseInt(solicitudStr);
				int art=Integer.parseInt(articuloStr);
				
				
				for(int i=0;i<cont;i++)
				{
					String codigo=forma.getJustificacion("codigo_"+i)+"";
					String descripcion=forma.getJustificacion("descripcion_"+i)+"";
					
					if(!descripcion.equals(""))
						articulo.insertarAtributoJustificacion(con,solicitud,
								art,Integer.parseInt(codigo),descripcion);
				}
				
				//vuelvo y cargo todo
				mundo.setNumeroSolicitud(forma.getNumeroSolicitud());
				mundo.cargarSolicitudMedicamentos(con, usuario.getCodigoInstitucionInt());	
				forma.setColeccionInsumos(mundo.cargaInsumosFarmacia(con,forma.getNumeroSolicitud()));
				boolean tempoResumenAtenciones=forma.isResumenAtencion();
				forma.setResumenAtencion(tempoResumenAtenciones);
				llenarForma(mundo, forma, paciente, usuario, new SolicitudMedicamentos());
				llenarFormaMedicamentos(con, mundo, forma, usuario);
				
				/** validación para la tarea oid=10741 **/
				if((forma.getCodigoTipoRegimen()!='P'||forma.getCodigoTipoRegimen()!='O')&&
				   (mundo.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCDespachada||
					mundo.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCAdministrada)&&
					UtilidadValidacion.esMedicoTratante(con,usuario,paciente).equals(""))
				{
					
					forma.setEsPosibleJustificar('S');
				}
				else
				{
					forma.setEsPosibleJustificar('N');
				}
				
				UtilidadBD.cerrarConexion(con);
				
				return 	mapping.findForward("recargarPager");
			}
			else
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error transformando datos", "errors.problemasDatos", true);
			}
		}
		catch(Exception e)
		{
			logger.error("Error en accionJustificarMedicamento de SolicitudesMedicamentosAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error tranformando datos", "errors.problemasDatos", true);
		}
		
	}

	/**
	 * Método para organizar los codigos de los artículos suspendidos junto a su respectivo motivo
	 * separados por "@@@@"
	 * @param con
	 * @param codigosSuspendidos
	 * @param forma
	 * @param usuario
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void llenarSuspensiones(Connection con, String codigosSuspendidos, SolicitudMedicamentosForm forma, UsuarioBasico usuario, ObjetoReferencia logSuspendidos, SolicitudMedicamentos mundo)
	{
		if(!codigosSuspendidos.equals(""))
		{
			String[] codigosMotivos=codigosSuspendidos.split("-");
			ArrayList codigosMotivosASuspender = new ArrayList();
			ArrayList codigosMotivosAModificarSuspension = new ArrayList();
			ArticuloSolicitudMedicamentos articuloSolicitudMedicamentos = null;
			for(int i=0; i<codigosMotivos.length; i++)
			{
				if(forma.getMedicamento("motivo_"+codigosMotivos[i])!=null)
				{
					if(!forma.getMedicamento("motivo_"+codigosMotivos[i]).equals(""))
					{
						String modificado=forma.getMedicamento("motivoModificado_"+codigosMotivos[i])+"";
						boolean motivoModificado=false;
						if(modificado!=null)
						{
							if(!modificado.equals(""))
							{
								motivoModificado=UtilidadTexto.getBoolean(forma.getMedicamento("motivoModificado_"+codigosMotivos[i])+"");
							}
						}
						
						/*
						 * MT 6014 
						 */
						
						Integer codArticuloSuspendido = Integer.parseInt(codigosMotivos[i]);
						articuloSolicitudMedicamentos = new ArticuloSolicitudMedicamentos();
						articuloSolicitudMedicamentos = mundo.getArticuloSolicitud(codArticuloSuspendido);
						
						if (articuloSolicitudMedicamentos!=null){
						if(!mundo.getArticuloSolicitud(Integer.parseInt(codigosMotivos[i])).getSuspendido())
						{
							codigosMotivosASuspender.add(codigosMotivos[i]+"@@@@"+UtilidadCadena.cargarObservaciones(forma.getMedicamento("motivo_"+codigosMotivos[i])+"",null, usuario)+"@@@@"+forma.getMedicamento("motivo_"+codigosMotivos[i]));
						}
						else if(motivoModificado)
						{
							codigosMotivosAModificarSuspension.add(codigosMotivos[i]+"@@@@"+UtilidadCadena.cargarObservaciones(forma.getMedicamento("motivo_"+codigosMotivos[i])+"",null, usuario)+"@@@@"+forma.getMedicamento("motivo_"+codigosMotivos[i]));
						}
					}
				}
			}
			}
			String[] tempo = new String[codigosMotivosASuspender.size()];
			for(int i=0; i<codigosMotivosASuspender.size(); i++)
			{
				tempo[i]=(String)codigosMotivosASuspender.get(i);
			}
			mundo.suspenderMedicamento(con, tempo, usuario.getCodigoPersona(), logSuspendidos);
			tempo = new String[codigosMotivosAModificarSuspension.size()];
			for(int i=0; i<codigosMotivosAModificarSuspension.size(); i++)
			{
				tempo[i]=(String)codigosMotivosAModificarSuspension.get(i);
			}
			mundo.modificarMotivosSuspension(con, tempo);
		}
	}

	/**
	 * 
	 * @param con
	 * @param mundo
	 * @param forma
	 * @param usuario
	 */
	@SuppressWarnings("rawtypes")
	private void llenarFormaMedicamentosDiferenteDosificacion(Connection con,SolicitudMedicamentos mundo, SolicitudMedicamentosForm forma,UsuarioBasico usuario) 
	{
		Vector articulos=mundo.getArticulos();
		int numElementos=articulos.size();
		ArticuloSolicitudMedicamentos articulo;
		String codigos="";
		String codigosSuspendidos="";
		if(numElementos>0)
		{
			for(int i=0; i<articulos.size(); i++)
			{
				articulo=(ArticuloSolicitudMedicamentos)articulos.elementAt(i);
				if(i!=0)
				{
					codigos+="-"+articulo.getArticulo();
					if(articulo.getSuspendido())
					{
						codigosSuspendidos+="-"+articulo.getArticulo();
					}
				}
				else
				{
					codigos=articulo.getArticulo()+"";
					if(articulo.getSuspendido())
					{
						codigosSuspendidos=articulo.getArticulo()+"";
					}
				}
				
				Articulo art=new Articulo();
				art.cargarArticulo(con, articulo.getArticulo());
				Vector cod=art.getVectorMedicamentoCompleto();
				forma.setMedicamentosDiferenteDosificacion("MDD_cod_"+articulo.getArticulo(),articulo.getArticulo()+ConstantesBD.separadorSplit+cod);
				forma.setMedicamentosDiferenteDosificacion("MDD_dosis_"+articulo.getArticulo(),articulo.getDosis());
				forma.setMedicamentosDiferenteDosificacion("MDD_unidosis_"+articulo.getArticulo(),articulo.getUnidosis());
				forma.setMedicamentosDiferenteDosificacion("MDD_frecuencia_"+articulo.getArticulo(),articulo.getFrecuencia()+"");
				forma.setMedicamentosDiferenteDosificacion("MDD_tipoFrecuencia_"+articulo.getArticulo(),articulo.getTipoFrecuencia());
				forma.setMedicamentosDiferenteDosificacion("MDD_cantidad_"+articulo.getArticulo(),articulo.getCantidadSolicitada()+"");
				forma.setMedicamentosDiferenteDosificacion("MDD_via_"+articulo.getArticulo(),articulo.getVia());
				forma.setMedicamentosDiferenteDosificacion("MDD_artPrincipal_"+articulo.getArticulo(),articulo.getArtPrincipal()+"");
				forma.setMedicamentosDiferenteDosificacion("MDD_diastratamiento_"+articulo.getArticulo(),articulo.getDiasTratamiento());
				forma.setMedicamentosDiferenteDosificacion("MDD_codigoNaturaleza_"+articulo.getArticulo(),art.getNaturaleza());
				
				if(!UtilidadTexto.getBoolean(articulo.getEsPos()))
				{
					forma.setMedicamentosDiferenteDosificacion("MDD_es_pos_"+articulo.getArticulo(), "false");
				}
				else if(articulo.getEsPos().equals("t"))
				{
					forma.setMedicamentosDiferenteDosificacion("MDD_es_pos_"+articulo.getArticulo(), "true");
				}
				else
				{
					forma.setMedicamentosDiferenteDosificacion("MDD_es_pos_"+articulo.getArticulo(), "");
				}
				forma.setMedicamentosDiferenteDosificacion("MDD_observaciones_"+articulo.getArticulo(),articulo.getObservaciones());
				forma.setMedicamentosDiferenteDosificacion("MDD_motivo_"+articulo.getArticulo(),articulo.getMotivoSuspension());
			}
			forma.setMedicamentosDiferenteDosificacion("numRegistros", articulos.size());
			forma.setMedicamentosDiferenteDosificacion("MDD_codigos",codigos);
			forma.setMedicamentosDiferenteDosificacion("MDD_codigosSuspendidos",codigosSuspendidos);
			
		}
		
	}
	
	
	/**
	 * Metodo que genera un String con los códigos de los articulos separado por '-'
	 * para introducirlo en el hashmap de la forma
	 * @param con
	 * @param mundo
	 * @param forma
	 */
	@SuppressWarnings("rawtypes")
	public void llenarFormaMedicamentos(Connection con, SolicitudMedicamentos mundo, SolicitudMedicamentosForm forma, UsuarioBasico usuario)
	{
		
		logger.info("****************************************************");
		Vector articulos=mundo.getArticulos();
		int numElementos=articulos.size();
		ArticuloSolicitudMedicamentos articulo;
		String codigos="";
		String codigosSuspendidos="";
		String artsolconsul="";
		String justificaciones="";
		if(numElementos>0)
		{
			Vector codigosNombresJustificacion=Utilidades.buscarCodigosNombresJustificaciones(usuario.getCodigoInstitucionInt(), false, true);
			for(int i=0; i<articulos.size(); i++)
			{
				articulo=(ArticuloSolicitudMedicamentos)articulos.elementAt(i);
				artsolconsul+=ConstantesBD.separadorSplit+" "+articulo.getArticulo()+" ";
			if(i!=0)
				{
					codigos+="-"+articulo.getArticulo();
					if(articulo.getSuspendido())
					{
						codigosSuspendidos+="-"+articulo.getArticulo();
					}
				}
				else
				{
					codigos=articulo.getArticulo()+"";
					if(articulo.getSuspendido())
					{
						codigosSuspendidos=articulo.getArticulo()+"";
					}
				}
				
				Articulo art=new Articulo();
				art.cargarArticulo(con, articulo.getArticulo());
				Vector cod=art.getVectorMedicamentoCompleto();
				forma.setMedicamento("cod_"+articulo.getArticulo(),articulo.getArticulo()+ConstantesBD.separadorSplit+cod);
				forma.setMedicamento("dosis_"+articulo.getArticulo(),articulo.getDosis());
				logger.info("Unidosis Antes >>>>>>>"+articulo.getUnidosis());
				//forma.setMedicamento("unidosis_"+articulo.getArticulo(),Utilidades.obtenerUnidadMedidadUnidosisArticulo(con, articulo.getUnidosis()));
				forma.setMedicamento("unidosis_"+articulo.getArticulo(), articulo.getUnidosis());

				logger.info("Unidosis >>>>>>>"+forma.getMedicamento("unidosis_"+articulo.getArticulo()));
				logger.info("Unidosis l >>>>>>>"+forma.getMedicamento("unidosisl_"+articulo.getArticulo()));
				

				forma.setMedicamento("frecuencia_"+articulo.getArticulo(),articulo.getFrecuencia()+"");
				forma.setMedicamento("tipoFrecuencia_"+articulo.getArticulo(),articulo.getTipoFrecuencia());
				forma.setMedicamento("cantidad_"+articulo.getArticulo(),articulo.getCantidadSolicitada()+"");
				forma.setMedicamento("via_"+articulo.getArticulo(),articulo.getVia());
				forma.setMedicamento("artPrincipal_"+articulo.getArticulo(),articulo.getArtPrincipal()+"");
				forma.setMedicamento("diastratamiento_"+articulo.getArticulo(),articulo.getDiasTratamiento());
				forma.setMedicamento("codigoNaturaleza_"+articulo.getArticulo(),art.getNaturaleza());

				forma.setMedicamento("tipofrecuencia_"+articulo.getArticulo(),articulo.getTipoFrecuencia());
				
				
				if(!UtilidadTexto.getBoolean(articulo.getEsPos()))
				{
					boolean estaJustificado=true;
					//Valida si en realidad tiene una Justificacion NoPos
					Integer tieneJustificacion=FormatoJustArtNopos.consultarCodigoJustificacionSolicitud(con, 
							                                                            forma.getNumeroSolicitud(), 
							                                                            articulo.getArticulo());
					if(tieneJustificacion != null){
					    forma.getJustificacionMap().put("justModifica_"+articulo.getArticulo(), articulo.getArticulo());
					    justificaciones += articulo.getArticulo() + " , ";
					    forma.setJustificacionModifica(justificaciones);
				    }
					for(int i1=0; i1<codigosNombresJustificacion.size();i1++)
					{
						Vector atributo=(Vector)codigosNombresJustificacion.elementAt(i1);
						int codigo=((Integer)atributo.elementAt(0)).intValue();
						String tempoJustificacion=(String)articulo.getJustificacion(codigo+"");
						if(tempoJustificacion!=null)
						{
							if(tempoJustificacion.equals(""))
							{
								try
								{
									atributo.elementAt(2);
									estaJustificado=false;
								}
								catch(ArrayIndexOutOfBoundsException e)
								{/*Para que no me saque la excepcion*/}
							}
							forma.setMedicamento("just"+codigo+"_"+articulo.getArticulo(),tempoJustificacion);
						}
						else
						{
							forma.setMedicamento("just"+codigo+"_"+articulo.getArticulo(),"");
							try
							{
								atributo.elementAt(2);
								estaJustificado=false;
							}
							catch(ArrayIndexOutOfBoundsException e)
							{/*Para que no me saque la excepcion*/}
						}
					}
					forma.setMedicamento("es_pos_"+articulo.getArticulo(), "false");
					if(estaJustificado)
					{
						forma.setMedicamento("justificacion_"+articulo.getArticulo(), "justificado");
					}
				}
				else if(articulo.getEsPos().equals("t"))
				{
					forma.setMedicamento("es_pos_"+articulo.getArticulo(), "true");
				}
				else
				{
					forma.setMedicamento("es_pos_"+articulo.getArticulo(), "");
				}
				forma.setMedicamento("observaciones_"+articulo.getArticulo(),articulo.getObservaciones());
				forma.setMedicamento("motivo_"+articulo.getArticulo(),articulo.getMotivoSuspension());
			}
			forma.setMedicamento("codigos",codigos);
			forma.setMedicamento("codigosSuspendidos",codigosSuspendidos);
			
			logger.info("\n\n\n\n [Es orden ambulatoria]->"+forma.isIndicativoOrdenAmbulatoria()+"\n\n\n\n");
			if (forma.getEstado().equals("modificar"))
				forma.setArtConsultaNP(artsolconsul);
			
		}
	}

	/**
	 * Método para llenar la forma con los datos cargados en el mundo
	 * @param mundo
	 * @param forma
	 */
	public void llenarForma(SolicitudMedicamentos mundo, SolicitudMedicamentosForm forma, PersonaBasica paciente, UsuarioBasico usuario, SolicitudMedicamentos mundoMDD)
	{
		forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		forma.setCodigoTipoRegimen(paciente.getCodigoTipoRegimen());
		forma.setNumeroSolicitud(mundo.getNumeroSolicitud());
		forma.setNumeroSolicitudMedDiferenteDosificacion(mundoMDD.getNumeroSolicitud());
		forma.setArticulos(mundo.getArticulos());
		forma.setArticulosDiferenteDosificacion(mundoMDD.getArticulos());
		
		forma.setNumeroSolicitud(mundo.getNumeroSolicitud());
		//forma.setNumeroAutorizacion(mundo.getNumeroAutorizacion());
		forma.setCentroCostoSolicitado(mundo.getCentroCostoSolicitado().getCodigo());
		forma.setCentroCostoSolicitante(mundo.getCentroCostoSolicitante().getCodigo());
		forma.setFecha(mundo.getFechaSolicitud());
		forma.setHora(mundo.getHoraSolicitud());
		forma.setNumeroElementos(mundo.getArticulos().size());
		forma.setObservacionesGenerales(mundo.getObservacionesGenerales());
		forma.setUrgente(mundo.getUrgente());
		forma.setNumeroOrdenMedica(mundo.getConsecutivoOrdenesMedicas());
		forma.setNumeroOrdenMedicaMedDiferenteDosificacion(mundoMDD.getConsecutivoOrdenesMedicas());
		forma.setMotivoAnulacion(mundo.getMotivoAnulacion());
		if(mundo.getControlEspecial() == null || mundo.getControlEspecial().equals(""))
			mundo.setControlEspecial(ConstantesBD.acronimoNo);
		forma.setCheckCE(mundo.getControlEspecial());

		//Se llena la forma con los datos del DtoDiagnostico asociado a la orden ambulatoria
		if(mundo.getDtoDiagnostico()!=null && mundo.getDtoDiagnostico().getAcronimoDiagnostico()!=null &&  !UtilidadTexto.isEmpty(mundo.getDtoDiagnostico().getAcronimoDiagnostico()))
		{
			forma.setAcronimoDiagnostico(mundo.getDtoDiagnostico().getAcronimoDiagnostico());
			forma.setTipoCieDiagnostico(mundo.getDtoDiagnostico().getTipoCieDiagnostico());
		}
		else
		{
			forma.setAcronimoDiagnostico("");
			forma.setTipoCieDiagnostico("");
		}
		
		
	}

	/**
	 * 
	 * @param index
	 * @param forma
	 * @param articuloAnterior
	 * @param usuario
	 * @return
	 */
	private ArticuloSolicitudMedicamentos llenarMedicamentoDiferenteDosificacion(int index, SolicitudMedicamentosForm forma, ArticuloSolicitudMedicamentos articuloAnterior, UsuarioBasico usuario) 
	{
		String tempo;
		ArticuloSolicitudMedicamentos articulo=new ArticuloSolicitudMedicamentos();
		boolean validar=true;
		boolean modificado=false;
		if(articuloAnterior==null)
		{
			articulo.setModificado(false);
			articuloAnterior=new ArticuloSolicitudMedicamentos();
			validar=false;
		}
		else
		{
			String logArticuloAnterior= "" +

					"\n*  Dosis			" + articuloAnterior.getDosis() +
					"\n*  Cod Unidosis	" + articuloAnterior.getUnidosis() +
					"\n*  Vía				" + articuloAnterior.getVia() +
					"\n*  Días Tratamiento	" + articuloAnterior.getDiasTratamiento() +
					"\n*  Cantidad Sol.			" + articuloAnterior.getCantidadSolicitada()+
					"\n*  Frecuencia			" + articuloAnterior.getFrecuencia()+
					"\n*  Tipo Frecuencia	" + articuloAnterior.getTipoFrecuencia();
			logArticuloAnterior+=
					"\n*  Observaciones		" + articuloAnterior.getObservaciones();
			articulo.setLogModificacion(logArticuloAnterior);
		}
		
		String medicamentoCompleto=(String)forma.getMedicamentosDiferenteDosificacion("MDD_cod_"+index);
		articulo.setArticulo(Integer.parseInt(medicamentoCompleto.split(ConstantesBD.separadorTags)[0]));
		String articuloEsPos=(String)forma.getMedicamentosDiferenteDosificacion("MDD_es_pos_"+index).toString();
		
		if(articuloEsPos!=null)
		{
			articulo.setEsPos(articuloEsPos);
		}
		else
		{
			articulo.setEsPos("");
		}
		
		if(validar && !articulo.getEsPos().equals(articuloAnterior.getEsPos()))
		{
			modificado=true;
		}
		
		tempo=forma.getMedicamentosDiferenteDosificacion("MDD_dosis_"+index)+"";
		articulo.setDosis(tempo);
		if(validar && !articulo.getDosis().equals(articuloAnterior.getDosis()))
		{
			modificado=true;
		}

		logger.info("valor de unidosis >> "+forma.getMedicamentosDiferenteDosificacion("MDD_unidosis_"+index)+"");
		tempo=forma.getMedicamentosDiferenteDosificacion("MDD_unidosis_"+index)+"";
		articulo.setUnidosis(tempo);
		if(validar && !articulo.getUnidosis().equals(articuloAnterior.getUnidosis()))
		{
			modificado=true;
		}
		
		tempo=forma.getMedicamentosDiferenteDosificacion("MDD_via_"+index)+"";
		articulo.setVia(tempo);
		if(validar && !articulo.getVia().equals(articuloAnterior.getVia()))
		{
			modificado=true;
		}

		tempo=(String)forma.getMedicamentosDiferenteDosificacion("MDD_frecuencia_"+index);
		if(tempo!=null && !tempo.equals(""))
		{
			articulo.setFrecuencia(Integer.parseInt(tempo));
			//articulo.setFrecuencia(tempo);
		}
		if(validar && articulo.getFrecuencia()!=articuloAnterior.getFrecuencia())
		{
			modificado=true;
		}

		tempo=forma.getMedicamentosDiferenteDosificacion("MDD_tipoFrecuencia_"+index)+"";
		articulo.setTipoFrecuencia(tempo);
		if(validar && !articulo.getTipoFrecuencia().equals(articuloAnterior.getTipoFrecuencia()))
		{
			modificado=true;
		}

		tempo=forma.getMedicamentosDiferenteDosificacion("MDD_diastratamiento_"+index)+"";
		articulo.setDiasTratamiento(tempo);
		if(validar && !articulo.getDiasTratamiento().equals(articuloAnterior.getDiasTratamiento()))
		{
			modificado=true;
		}
		
		tempo=(String)forma.getMedicamentosDiferenteDosificacion("MDD_cantidad_"+index);
		if(tempo!=null && !tempo.equals(""))
		{
			articulo.setCantidadSolicitada(Integer.parseInt(tempo));
		}
		else
		{
			articulo.setCantidadSolicitada(0);
		}
		if(validar && articulo.getCantidadSolicitada()!=articuloAnterior.getCantidadSolicitada())
		{
			modificado=true;
		}

		tempo=forma.getMedicamentosDiferenteDosificacion("MDD_observaciones_"+index)+"";
		articulo.setObservaciones(tempo);
		if(validar && !articulo.getObservaciones().equals(articuloAnterior.getObservaciones()))
		{
			modificado=true;
		}
		
		tempo=(String)forma.getMedicamentosDiferenteDosificacion("MDD_artPrincipal_"+index);
		if(tempo!=null && !tempo.equals(""))
		{
			articulo.setArtPrincipal(Integer.parseInt(tempo));
		}
		else
		{
			articulo.setArtPrincipal(0);
		}
		articulo.setModificado(modificado);
		return articulo;
	}
	
	/**
	 * Método para tomar los datos ingresados en el <class>HashMap</class>
	 * y con ellos generar un vector en el mundo el cual tiene
	 * objetos de tipo <code>ArticuloSolicitudMedicamentos</code> con cada
	 * uno de los medicamentos de la solicitud
	 * @param index
	 * @param forma
	 * @param articuloAnterior
	 */
	@SuppressWarnings("rawtypes")
	private ArticuloSolicitudMedicamentos llenarMedicamento(int index, SolicitudMedicamentosForm forma, ArticuloSolicitudMedicamentos articuloAnterior, UsuarioBasico usuario)
	{

		logger.info("aaaaaaaaaaaaaaaaaaWWWWWWWWWWWWWWWWWWWWWWWWWWWW");
		logger.info("Index " + index);

		logger.info("valor de unidosis >> "+forma.getMedicamento("unidosis_"+index)+"");
		logger.info("valor de unidosis >> "+forma.getMedicamento(index+"_unidosis")+"");
		logger.info("valor de unidosis >> "+forma.getMedicamento(index+"_unidosis_0")+"");
		
		logger.info("valor de unidosis >> "+forma.getMedicamento(index+"_indicespos_6")+"");
		
		
		
		String tempo="";
		ArticuloSolicitudMedicamentos articulo=new ArticuloSolicitudMedicamentos();
		Vector nombresJustificacion=Utilidades.buscarCodigosNombresJustificaciones(usuario.getCodigoInstitucionInt(), false, true);
		boolean validar=true;
		boolean modificado=false;

		

		if(articuloAnterior==null)
		{
			logger.info("<<Nulo");
			articulo.setModificado(false);
			articuloAnterior=new ArticuloSolicitudMedicamentos();
			validar=false;
		}
		else
		{
			String logArticuloAnterior= "" +

					"\n*  Dosis			" + articuloAnterior.getDosis() +
					"\n*  Cod Unidosis	" + articuloAnterior.getUnidosis() +
					"\n*  Vía				" + articuloAnterior.getVia() +
					"\n*  Días Tratamiento	" + articuloAnterior.getDiasTratamiento() +
					"\n*  Cantidad Sol.			" + articuloAnterior.getCantidadSolicitada()+
					"\n*  Frecuencia			" + articuloAnterior.getFrecuencia()+
					"\n*  Tipo Frecuencia	" + articuloAnterior.getTipoFrecuencia();
			if(articuloAnterior.getEsPos().equals("f"))
			{
				for(int i1=0; i1<nombresJustificacion.size(); i1++)
				{
					Vector vector=(Vector)nombresJustificacion.elementAt(i1);
					logArticuloAnterior+="\n*\t"+vector.elementAt(1)+"\t"+articuloAnterior.getJustificacion(vector.elementAt(0)+"");
				}
			}
			logArticuloAnterior+=
					"\n*  Observaciones		" + articuloAnterior.getObservaciones();
			articulo.setLogModificacion(logArticuloAnterior);

			logger.info("logArticuloAnterior: " + logArticuloAnterior);
		}
		
		String medicamentoCompleto=(String)forma.getMedicamento("cod_"+index);
		articulo.setArticulo(Integer.parseInt(medicamentoCompleto.split(ConstantesBD.separadorTags)[0]));
		//logger.info("MEDICAMENTO COMPLETO>>>>>>>>>>>>>>"+medicamentoCompleto);
		//logger.info("ARTICULO>>>>>>>>>>>>>>>>"+articulo.getArticulo());
		//logger.info("ARTICULO ES POOOOSSS>>>>>>>>>>>>>>>"+forma.getMedicamento("es_pos_"+index).toString());

		String articuloEsPos=(String)forma.getMedicamento("es_pos_"+index).toString();
		
		if(articuloEsPos!=null)
		{
			articulo.setEsPos(articuloEsPos);
		}
		else
		{
			articulo.setEsPos("");
		}

		
		Utilidades.imprimirMapa(forma.getMedicamentos());
		
		
		/*logger.info("INDEEEEEEEEXXXXXXXXXX>>>>>>>>>>>>>"+index);
		Utilidades.imprimirMapa(forma.getMedicamento());
		if(forma.getMedicamento().containsKey("es_pos_"+index) && (String)forma.getMedicamento("es_pos_"+index).toString()!=null)
		{
			articulo.setEsPos((String)forma.getMedicamento("es_pos_"+index).toString());
		}
		else
		{
			articulo.setEsPos("");
		}*/
		
		if(validar && !articulo.getEsPos().equals(articuloAnterior.getEsPos()))
		{
			modificado=true;
		}
		
		tempo=forma.getMedicamento("dosis_"+index)+"";
		articulo.setDosis(tempo);
		if(validar && !articulo.getDosis().equals(articuloAnterior.getDosis()))
		{
			modificado=true;
		}

		logger.info("aaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaaaaaaaaaaaaa");
		logger.info("Indice: " + index);
		
		logger.info("valor de unidosis >> "+forma.getMedicamento("unidosis_"+index)+"");
		logger.info("valor de unidosis >> "+forma.getMedicamento(index+"_unidosis")+"");
		
		tempo=forma.getMedicamento("unidosis_"+index)+"";
		articulo.setUnidosis(tempo);
		if(validar && !articulo.getUnidosis().equals(articuloAnterior.getUnidosis()))
		{
			modificado=true;
		}
		
		tempo=forma.getMedicamento("via_"+index)+"";
		articulo.setVia(tempo);
		if(validar && !articulo.getVia().equals(articuloAnterior.getVia()))
		{
			modificado=true;
		}

		tempo=(String)forma.getMedicamento("frecuencia_"+index);
		if(tempo!=null && !tempo.equals(""))
		{
			articulo.setFrecuencia(Integer.parseInt(tempo));
			//articulo.setFrecuencia(tempo);
		}
		if(validar && articulo.getFrecuencia()!=articuloAnterior.getFrecuencia())
		{
			modificado=true;
		}

		tempo=forma.getMedicamento("tipoFrecuencia_"+index)+"";
		articulo.setTipoFrecuencia(tempo);
		if(validar && !articulo.getTipoFrecuencia().equals(articuloAnterior.getTipoFrecuencia()))
		{
			modificado=true;
		}

		tempo=forma.getMedicamento("diastratamiento_"+index)+"";
		articulo.setDiasTratamiento(tempo);
		if(validar && !articulo.getDiasTratamiento().equals(articuloAnterior.getDiasTratamiento()))
		{
			modificado=true;
		}
		
		tempo=(String)forma.getMedicamento("cantidad_"+index);
		if(tempo!=null && !tempo.equals(""))
		{
			articulo.setCantidadSolicitada(Integer.parseInt(tempo));
		}
		else
		{
			articulo.setCantidadSolicitada(0);
		}
		if(validar && articulo.getCantidadSolicitada()!=articuloAnterior.getCantidadSolicitada())
		{
			modificado=true;
		}

		tempo=forma.getMedicamento("observaciones_"+index)+"";
		articulo.setObservaciones(tempo);
		if(validar && !articulo.getObservaciones().equals(articuloAnterior.getObservaciones()))
		{
			modificado=true;
		}

		for(int i1=0; i1<nombresJustificacion.size(); i1++)
		{
			Vector atrtibuto=(Vector)nombresJustificacion.elementAt(i1);
			tempo=(String)forma.getMedicamento("just"+atrtibuto.elementAt(0)+"_"+index);
			articulo.setJustificacion(atrtibuto.elementAt(0)+"", tempo);
			modificado=true;
		}
		
		tempo=(String)forma.getMedicamento("artPrincipal_"+index);
		if(tempo!=null && !tempo.equals(""))
		{
			articulo.setArtPrincipal(Integer.parseInt(tempo));
		}
		else
		{
			articulo.setArtPrincipal(0);
		}
		articulo.setModificado(modificado);
		return articulo;
	}

	/**
	 * Copiar los datos del form al mundo
	 * @param con 
	 * @param mundo
	 * @param forma
	 */
	private void llenarMundo(Connection con, SolicitudMedicamentos mundo, SolicitudMedicamentosForm forma, PersonaBasica paciente, UsuarioBasico usuario)
	{
		
		mundo.setNumeroSolicitud(forma.getNumeroSolicitud());
		mundo.setFechaSolicitud(forma.getFecha());
		mundo.setHoraSolicitud(forma.getHora());
		mundo.setTipoSolicitud(new InfoDatosInt(ConstantesBD.codigoTipoSolicitudMedicamentos));
		//mundo.setNumeroAutorizacion(forma.getNumeroAutorizacion());
		mundo.setEspecialidadSolicitada(new InfoDatosInt(0));
		mundo.setEspecialidadSolicitante(new InfoDatosInt(0));
		mundo.setOcupacionSolicitado(new InfoDatosInt(usuario.getCodigoOcupacionMedica()));
		mundo.setSolPYP(forma.isSolPYP());
		   mundo.setControlEspecial(forma.getCheckCE());
		if(forma.isIndicativoOrdenAmbulatoria())
			mundo.setOrdenAmbulatoria(Utilidades.obtenerCodigoOrdenAmbulatoria(con,forma.getOrdenAmbulatoria(),usuario.getCodigoInstitucionInt()));
		else
			mundo.setOrdenAmbulatoria("");
		mundo.setLoginMedico(usuario.getLoginUsuario());
		mundo.setControlEspecial(forma.getCheckCE());
		/* Cuando la vía de ingreso del paciente es consulta externo o ambularios se guarda el centro de costo
		parametrizado en valores por defecto para estos dos centros de costo */
/*
 * oid=19325
		if (paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoConsultaExterna) 
		{
			String centroCosto1=util.ValoresPorDefecto.getCentroCostoConsultaExterna(usuario.getCodigoInstitucionInt());
			mundo.setCentroCostoSolicitante(new InfoDatosInt(Integer.parseInt(centroCosto1)));
		}
		else if (paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoAmbulatorios)
		{
			String centroCosto2=util.ValoresPorDefecto.getCentroCostoAmbulatorios(usuario.getCodigoInstitucionInt());
			mundo.setCentroCostoSolicitante(new InfoDatosInt(Integer.parseInt(centroCosto2)));
		}
		else
		{
			mundo.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea()));
		}
*/
		if(forma.isIndicativoOrdenAmbulatoria())
			mundo.setCentroCostoSolicitante(new InfoDatosInt(forma.getCentroCostoSolicitante()));
		else
			mundo.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea()));
/**/					
		mundo.setCentroCostoSolicitado(new InfoDatosInt(forma.getCentroCostoSolicitado()));
		mundo.setCodigoMedicoSolicitante(usuario.getCodigoPersona());
		//consecutivo ordenes médicas
		mundo.setCodigoCuenta(paciente.getCodigoCuenta());
		mundo.setCobrable(true);
		mundo.setUrgente(forma.getUrgente());
		mundo.setEstadoHistoriaClinica(new InfoDatosInt(ConstantesBD.codigoEstadoHCSolicitada));
		/*String tempo=forma.getObservacionesGeneralesNuevas();
		if(!tempo.equals(""))
		{*/
			//mundo.setObservacionesGenerales(UtilidadTexto.agregarTextoAObservacion(forma.getObservacionesGenerales(), null, usuario, false));
			mundo.setObservacionesGenerales(forma.getObservacionesGenerales());
		//}
		mundo.setDatosMedico(UtilidadTexto.agregarTextoAObservacion(null, null, usuario, false));
		
		//Se agrega cambio por versión 1.52 del Anexo Solicitud de Medicamentos 65
		//Guardar último Diagnóstico y Tipo de CIE del Paciente 
		//@author Diana Carolina G
		DtoDiagnostico dtoDiagnostico = null;
		
		if(forma.isIndicativoOrdenAmbulatoria()){
			dtoDiagnostico = new DtoDiagnostico();
			if((!UtilidadTexto.isEmpty(forma.getAcronimoDiagnostico())) && (!UtilidadTexto.isEmpty(forma.getTipoCieDiagnostico())))
			{
				dtoDiagnostico = new DtoDiagnostico();
				dtoDiagnostico.setAcronimoDiagnostico(forma.getAcronimoDiagnostico());
				dtoDiagnostico.setTipoCieDiagnostico(forma.getTipoCieDiagnostico());
			}
			else{
				dtoDiagnostico.setAcronimoDiagnostico(null);
				dtoDiagnostico.setTipoCieDiagnostico(null);
			}
			
		}else{
			dtoDiagnostico = Utilidades.getDiagnosticoPacienteCuenta(paciente.getCodigoCuenta());
		}	
	  	mundo.setDtoDiagnostico(dtoDiagnostico);
	}
	
    /**
     * Procedimiento que ordena los datos del resumen segun la columna seleccionada
     * @param documentoForm
     * 
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void accionOrdenar(SolicitudMedicamentosForm forma)
    {        
        try
        {       	
            forma.setColeccion(Listado.ordenarColumna(new ArrayList(forma.getColeccion()), forma.getUltimaPropiedad(),forma.getColumna()));
            forma.setUltimaPropiedad(forma.getColumna());
        }
        catch (Exception e)
        {
            logger.warn("Error en el listado de documentos");
            e.printStackTrace();
        }
         
    }
    
    
   /** 
    * Método que se encarga de realizar las validaciones comunes
	* a todos los estados / tipos en la solicitud general. Valida que
	* el usuario sea válido, que sea profesional de la salud y que
	* haya al menos un paciente cargado
	*
	* @param map Mapping de la aplicación
	* @param req Request de Http
	* @param paciente paciente cargado
	* @param medico medico / usuario que intenta acceder a la
	* funcionalidad
	* @return
	*/
	@SuppressWarnings("deprecation")
	private ActionForward validacionesComunes(ActionMapping map, HttpServletRequest req, PersonaBasica paciente, UsuarioBasico medico, Connection con, SolicitudMedicamentosForm forma ) throws Exception
	{
		logger.info("\n\n\n\nHACIENDO VALIDACIONES COMUNES \n\n\n\n");
		logger.info("--->"+paciente.getCodigoAdmision());
		if (paciente.getCodigoCuenta()<1)
		{
				return ComunAction.accionSalirCasoError(map, req, con, logger, "El paciente no tiene la cuenta abierta", "errors.paciente.cuentaNoAbierta", true);
		}
		//Para los casos dif. a Adm de hosp y Urgencias hay
		//que validar que no tenga egreso médico. Caso
		//de urgencias maneja semi-egreso
		else if (paciente.getCodigoAdmision()>0)
		{
			if(forma.getTipoListadoSolicitudes()!=SolicitudMedicamentosForm.listadoPorPaciente && forma.getTipoListadoSolicitudes()!=SolicitudMedicamentosForm.listadoPorArea)
			{	
				//Nueva valición tarea 44656 permitir ordenes estado cuenta asociada
				if (UtilidadValidacion.tieneEgreso(con, paciente.getCodigoCuenta()) 
						&& !UtilidadValidacion.puedoCrearCuentaAsocio(con,paciente.getCodigoIngreso()))
				//if (UtilidadValidacion.tieneEgreso(con, paciente.getCodigoCuenta()) && forma.isIndicativoOrdenAmbulatoria()==false)
				{

					logger.info("******************************************************");
					logger.info("cuenta-->"+paciente.getCodigoCuenta());
					logger.info("estado cuenta--->"+UtilidadesHistoriaClinica.obtenerEstadoCuenta(con, paciente.getCodigoCuenta()).getCodigo());
					logger.info("******************************************************");
					
					
					if(UtilidadesHistoriaClinica.obtenerEstadoCuenta(con, paciente.getCodigoCuenta()).getCodigo()!=ConstantesBD.codigoEstadoCuentaAsociada)
					{
						//si es permitido para urgencias.
						if(paciente.getCodigoUltimaViaIngreso()!=ConstantesBD.codigoViaIngresoUrgencias)
						{
							return ComunAction.accionSalirCasoError(map, req, con, logger, "El paciente es de urg/hosp pero ya tiene egreso médico", "error.solicitudgeneral.tieneOrdenSalida", true);
						}
					}	
				}
				else if (!UtilidadValidacion.tieneValoraciones(con, paciente.getCodigoCuenta()))
				{
					//xplanner 55400
					//if(!forma.isIndicativoOrdenAmbulatoria())
					{
						return ComunAction.accionSalirCasoError(map, req, con, logger, "El paciente es de urg/hosp pero no tiene valoracion inicial", "error.solicitudgeneral.tieneValoracionInicial", true);
					}
					/*else
					{
						if(!forma.isIndicativoOrdenAmbulatoria())
						{
							return ComunAction.accionSalirCasoError(map, req, con, logger, "El paciente es de urg/hosp pero no tiene valoracion inicial", "error.solicitudgeneral.tieneValoracionInicial", true);
						}
						else
						{
							if(paciente.getCodigoUltimaViaIngreso() != ConstantesBD.codigoViaIngresoHospitalizacion && paciente.getCodigoUltimaViaIngreso() != ConstantesBD.codigoViaIngresoUrgencias)
							{
								return ComunAction.accionSalirCasoError(map, req, con, logger, "El paciente es de urg/hosp pero no tiene valoracion inicial", "error.solicitudgeneral.tieneValoracionInicial", true);
							}
						}
					}*/
				}
			}	
		}
		else if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion || paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
		{
			int codigoCuentaPaciente= paciente.getCodigoCuenta();
			
			logger.info("******************************************************");
			logger.info("cuenta-->"+paciente.getCodigoCuenta());
			logger.info("estado cuenta--->"+UtilidadesHistoriaClinica.obtenerEstadoCuenta(con, paciente.getCodigoCuenta()).getCodigo());
			logger.info("******************************************************");
			
			if(UtilidadesHistoriaClinica.obtenerEstadoCuenta(con, paciente.getCodigoCuenta()).getCodigo()!=ConstantesBD.codigoEstadoCuentaAsociada)
			{
				/*
				 * No es cama de observación
				 */
				if(!UtilidadValidacion.tieneValoracionConConductaCamaObservacion(con, codigoCuentaPaciente))
				{
					if(UtilidadValidacion.tieneEgreso(con, codigoCuentaPaciente) && forma.isIndicativoOrdenAmbulatoria()==false)
					{
						/*
						 * Si no es hospitalizar en piso, muestra error de egreso
						 */
						int[] conductas={ConstantesBD.codigoConductaSeguirHospitalizarPiso};
						if(!UtilidadValidacion.validacionConductaASeguir(con, codigoCuentaPaciente, conductas))
						{
							return ComunAction.accionSalirCasoError(map, req, con, logger, "Opción disponible solo para pacientes con Admisión Abierta", "errors.paciente.noAdmisionEgreso", true);
						}
					}
				}
				/*
				 * Si es conducta a seguir cama de observación
				 */
				else
				{
					/*
					 * Validar si tiene egreso
					 * Si lo tiene, debe validar el destino a
					 * la salida que sea diferente de hospitalización
					 * De lo contrario muestra error de egreso
					 */
					if(UtilidadValidacion.tieneEgreso(con, codigoCuentaPaciente) && forma.isIndicativoOrdenAmbulatoria()==false)
					{
						Egreso egreso=new Egreso();
						egreso.cargarEgresoGeneral(con, codigoCuentaPaciente);
						if((egreso.getDestinoSalida().getCodigo()!=ConstantesBD.codigoDestinoSalidaHospitalizacion))
						{
							return ComunAction.accionSalirCasoError(map, req, con, logger, "Opción disponible solo para pacientes con Admisión Abierta", "errors.paciente.noAdmisionEgreso", true);
						}
					}
				}
			}	
		}

		return null;
	}
    
    
	/**
	 * 
	 * @param estado
	 * @param con
	 * @param paciente
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param codigoCuentaPaciente
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	private ActionForward validacionesAcceso(	String estado, Connection con, PersonaBasica paciente, UsuarioBasico usuario, 
												ActionMapping mapping, HttpServletRequest request, SolicitudMedicamentosForm forma,
												int codigoCuentaPaciente) throws SQLException 
	{
		boolean esConsulta = false;
		if(forma.getTipoListadoSolicitudes()==SolicitudMedicamentosForm.listadoPorPaciente)
			esConsulta = true;
		
		forma.setCodigoTipoRegimen(paciente.getCodigoTipoRegimen());
		if(paciente.getCodigoPersona()<1)
		{
		    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
		}
		else if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
		}
		else if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), usuario.getLoginUsuario())  )
        {
            return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
        }
		ActionForward validacionesGenerales=null;
		try
		{
			validacionesGenerales = this.validacionesComunes(mapping, request, paciente, usuario, con, forma);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (validacionesGenerales != null)
		{
			UtilidadBD.closeConnection(con);
			return validacionesGenerales ;
		}
		else if(!forma.isResumenAtencion())
		{
			if(codigoCuentaPaciente==0 && !paciente.getExisteAsocio())
			{
			    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente sin cuenta Abierta", "errors.paciente.cuentaNoAbierta", true);
			}
			else if(!UtilidadValidacion.esMedicoTratante(con, usuario, paciente).equals("") && !UtilidadValidacion.esAdjuntoCuenta(con, codigoCuentaPaciente, usuario.getLoginUsuario()) && !UtilidadValidacion.esViaIngresoConsultaExterna(con, codigoCuentaPaciente) && (paciente.getCodigoUltimaViaIngreso()!=ConstantesBD.codigoViaIngresoAmbulatorios) && (forma.getEstado().equals("empezar")))
			{
				if(UtilidadCadena.noEsVacio(UtilidadValidacion.esEnfermera(usuario)) && !UtilidadValidacion.esProfesionalSalud(usuario))
				{
					String mensaje = UtilidadValidacion.esMedicoTratante(con, usuario, paciente);
					//se verifica si el error es por no definir ocupación médico especialista y general
					if(mensaje.equals("errors.noOcupacionMedica"))
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No se definió ocupación médica", mensaje, true);
					else
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Médico no es tratante ni Adjunto", "error.validacionessolicitud.medicoNoTratanteNiAdjunto", true);
				}
			}
			else if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion || paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
			{
				if(!UtilidadValidacion.tieneValoraciones(con, codigoCuentaPaciente))
				{
					if(paciente.getCodigoUltimaViaIngreso() != ConstantesBD.codigoViaIngresoHospitalizacion && paciente.getCodigoUltimaViaIngreso() != ConstantesBD.codigoViaIngresoUrgencias)
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no tiene Valoración Inicial", "error.solicitudgeneral.tieneValoracionInicial", true);
					}
				}
				

				logger.info("******************************************************");
				logger.info("cuenta-->"+paciente.getCodigoCuenta());
				logger.info("estado cuenta--->"+UtilidadesHistoriaClinica.obtenerEstadoCuenta(con, paciente.getCodigoCuenta()).getCodigo());
				logger.info("******************************************************");
				
				if(UtilidadesHistoriaClinica.obtenerEstadoCuenta(con, paciente.getCodigoCuenta()).getCodigo()!=ConstantesBD.codigoEstadoCuentaAsociada)
				{
					/*
					 * No es cama de observación
					 */
					if(!UtilidadValidacion.tieneValoracionConConductaCamaObservacion(con, codigoCuentaPaciente))
					{
						if(!estado.equals("consultar") && !estado.equals("detalle"))
						{	
							if(UtilidadValidacion.tieneEgreso(con, codigoCuentaPaciente) && forma.isIndicativoOrdenAmbulatoria()==false)
							{
								/*
								 * Si no es hospitalizar en piso, muestra error de egreso
								 */
								int[] conductas={ConstantesBD.codigoConductaSeguirHospitalizarPiso};
								if((!UtilidadValidacion.validacionConductaASeguir(con, codigoCuentaPaciente, conductas))&&!forma.isResumenAtencion())
								{
									//para urgencias si es posible.
									if(paciente.getCodigoUltimaViaIngreso()!=ConstantesBD.codigoViaIngresoUrgencias)
									{
										return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Opción disponible solo para pacientes con Admisión Abierta", "errors.paciente.noAdmisionEgreso", true);
									}
								}
							}
						}	
					}
					/*
					 * Si es conducta a seguir cama de observación
					 */
					else
					{
						/*
						 * Validar si tiene egreso
						 * Si lo tiene, debe validar el destino a
						 * la salida que sea diferente de hospitalización
						 * De lo contrario muestra error de egreso
						 */
						if(!estado.equals("consultar") && !estado.equals("detalle"))
						{
							if(UtilidadValidacion.tieneEgreso(con, codigoCuentaPaciente) && forma.isIndicativoOrdenAmbulatoria()==false)
							{
								Egreso egreso=new Egreso();
								egreso.cargarEgresoGeneral(con, codigoCuentaPaciente);
								if((egreso.getDestinoSalida().getCodigo()!=ConstantesBD.codigoDestinoSalidaHospitalizacion)&&!forma.isResumenAtencion())
								{
									return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Opción disponible solo para pacientes con Admisión Abierta", "errors.paciente.noAdmisionEgreso", true);
								}
							}
						}	
					}
				}	
			}
			else if(UtilidadValidacion.esViaIngresoConsultaExterna(con, codigoCuentaPaciente))
			{
				/*
				 * Esta validación ya no aplica
				if(!UtilidadValidacion.tieneCitaRespondida(con, usuario.getCodigoPersona(), paciente.getCodigoPersona()))
				{
				    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no tiene citas atendidas por el medico que solicita", "error.cita.noRespondida", true);
				}*/
			}
			
		}
        
		
		if(UtilidadTexto.isEmpty(ValoresPorDefecto.getNumDiasTratamientoMedicamentos(usuario.getCodigoInstitucionInt()))&&!esConsulta)
        {
            ActionErrors errores=new ActionErrors();
            errores.add("error.parametrosGenerales.faltaDefinirParametro", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro", "<Número de días Tratamiento en solicitudes de medicamentos>"));
            logger.warn("entra al error de [error.parametrosGenerales.faltaDefinirParametro] ");
            saveErrors(request, errores);
            UtilidadBD.closeConnection(con);
            return mapping.findForward("paginaErroresActionErrors");
        }
		
		if(!esConsulta)
		{
	        if(ValoresPorDefecto.getCodigoTransSoliPacientes(usuario.getCodigoInstitucionInt(), true).trim().equals(""))
	        {
	            ActionErrors errores=new ActionErrors();
	            errores.add("error.inventarios.sinDefinirTipoTransaccion", new ActionMessage("error.inventarios.sinDefinirTipoTransaccion", "SOLICITUDES PACIENTE"));
	            logger.warn("entra al error de [error.inventarios.sinDefinirTipoTransaccion] ");
	            saveErrors(request, errores);
	            UtilidadBD.closeConnection(con);
	            return mapping.findForward("paginaErroresActionErrors");
	        }
	        else
	        {    
	            //validacion de las transacciones validas por centro de consto
	            Vector restricciones= new Vector();
	            restricciones.add(0, ValoresPorDefecto.getCodigoTransSoliPacientes(usuario.getCodigoInstitucionInt(), true));
	            HashMap transaccionesValidasCCMap=UtilidadInventarios.transaccionesValidasCentroCosto( usuario.getCodigoInstitucionInt(), paciente.getCodigoArea(), restricciones, true);
	            if(Integer.parseInt(transaccionesValidasCCMap.get("numRegistros").toString())<=0) 
	            {
	                ActionErrors errores=new ActionErrors();
	                errores.add("error.inventarios.transaccionNoValidaCentroCosto", new ActionMessage("error.inventarios.transaccionNoValidaCentroCosto", paciente.getArea()));
	                logger.warn("entra al error de [error.inventarios.transaccionNoValidaCentroCosto] Area-->"+paciente.getCodigoArea());
	                saveErrors(request, errores);
	                UtilidadBD.closeConnection(con);
	                return mapping.findForward("paginaErroresActionErrors");
	            }
	        } 
		}
        /*********************************************************************************************************************/
		/** Se verifica que el ingreso no sea realizado atraves de entidades subcontratadas*/
		if (paciente.esIngresoEntidadSubcontratada())
		{
			request.setAttribute("codigoDescripcionError", "error.ingresoEntidadSubContratada");
			return mapping.findForward("paginaError");
		}
		/*********************************************************************************************************************/
		return null;
	}
	
	//**************************************************************************************************************************
	
	/** 
	 * Metodo para listar las solicitudes historicas con sus medicamentos
	 * @param Connection con
	 * @param boolean esUltimaSolicitud
	 * @param SolicitudMedicamentosForm forma
	 * @param PersonaBasica paciente
	 * @param UsuarioBasico usuario
	 * */
	private void metodoHistoricoMedicamentos(
			Connection con, 
			boolean esUltimaSolicitud, 
			SolicitudMedicamentosForm forma,
			PersonaBasica paciente,
			UsuarioBasico usuario)
	{
		SolicitudMedicamentos mundo = new SolicitudMedicamentos();
		Utilidades.imprimirMapa(forma.getMedicamentos());		
		
		//Consulta el listado
		forma.setListadoHistoricoSolicitudMap(mundo.consultarHistorialSolicitudes(
				con,
				esUltimaSolicitud, 
				paciente.getCodigoPersona()+"", 
				paciente.getCodigoIngreso()+"",
				paciente.getCodigoUltimaViaIngreso()+"",
				forma.getCentroCostoSolicitado()+"",
				usuario.getCodigoInstitucionInt()));				
	}
	
	//**************************************************************************************************************************	
	
	/**
	 * Carga la ultima solicitud de medicamentos o la indicada dentro del mapa
	 * @param Connection con
	 * @param SolicitudMedicamentosForm forma
	 * @param PersonaBasica paciente
	 * @param UsuarioBasico usuario
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void metodoUltimaOrden(Connection con,SolicitudMedicamentosForm forma,PersonaBasica paciente,UsuarioBasico usuario)
	{
		int index = ConstantesBD.codigoNuncaValido;
		
		if(forma.getListadoHistoricoSolicitudMap("indexCargar").toString().equals(ConstantesBD.codigoNuncaValido+""))
		{
			SolicitudMedicamentos mundo = new SolicitudMedicamentos();
			
			//Consulta la ultima solicitud de medicamentos/Insumos
			forma.setListadoHistoricoSolicitudMap(mundo.consultarHistorialSolicitudes(
					con,
					true, 
					paciente.getCodigoPersona()+"", 
					paciente.getCodigoIngreso()+"",
					paciente.getCodigoUltimaViaIngreso()+"",
					forma.getCentroCostoSolicitado()+"",
					usuario.getCodigoInstitucionInt()));
			
			if(!forma.getListadoHistoricoSolicitudMap("numRegistros").toString().equals("0"))
			{
				index = 0;
				forma.setListadoHistoricoSolicitudMap("indicativoMensajeUltimaOrdenNula",ConstantesBD.acronimoNo);
			}
			else
			{
				forma.setListadoHistoricoSolicitudMap("indicativoMensajeUltimaOrdenNula",ConstantesBD.acronimoSi);
				return ;
			}
			
		}
		else
		{
			index = Integer.parseInt(forma.getListadoHistoricoSolicitudMap("indexCargar").toString());			
		}
		
		
		String codigos=forma.getMedicamento("codigos")+"";
		boolean primero = false;
		
		if(codigos.indexOf("-")==-1 && codigos.equals("0"))
		{
			primero = true;
			codigos = "";
		} 
				
		String nuevoCodigos = "";		
		
		HashMap medicamentos = (HashMap)forma.getListadoHistoricoSolicitudMap("medicamentos_"+index);		
		boolean paso=false;
		for(int i = 0; i < Integer.parseInt(medicamentos.get("numRegistros").toString()); i++)
		{
			//Valida que los medicamentos no se encuentren repetidos y el indicativo de suspendido			
			if(!validarExistenciaCodigo(forma,medicamentos.get("cod_articulo_"+i).toString()) 
					&& medicamentos.get("indicativo_"+i).toString().equals(""))
			{
				if(medicamentos.get("esmedicamento_"+i).toString().equals(ConstantesBD.acronimoNo))
				{
					forma.getMedicamentos().put("cod_"+medicamentos.get("cod_articulo_"+i),medicamentos.get("cod_articulo_"+i)+ConstantesBD.separadorSplit+"["+medicamentos.get("articulo_"+i)+"]");						
					forma.getMedicamentos().put("es_pos_"+medicamentos.get("cod_articulo_"+i),UtilidadTexto.getBoolean(medicamentos.get("es_pos_"+i).toString()));
					forma.getMedicamentos().put("existencias_"+medicamentos.get("cod_articulo_"+i),medicamentos.get("existencias_"+i));
					forma.getMedicamentos().put("cantidad_"+medicamentos.get("cod_articulo_"+i),medicamentos.get("cantidad_"+i).toString());
					forma.getMedicamentos().put("esPos_"+medicamentos.get("cod_articulo_"+i),UtilidadTexto.getBoolean(medicamentos.get("es_pos_"+i).toString())+"");
					forma.getMedicamentos().put("codigoNaturaleza_"+medicamentos.get("cod_articulo_"+i),medicamentos.get("codigonaturaleza_"+i).toString());
				}
				else
				{
					forma.getMedicamentos().put("cod_"+medicamentos.get("cod_articulo_"+i),medicamentos.get("cod_articulo_"+i)+ConstantesBD.separadorSplit+"["+medicamentos.get("articulo_"+i)+"]");						
					forma.getMedicamentos().put("es_pos_"+medicamentos.get("cod_articulo_"+i),UtilidadTexto.getBoolean(medicamentos.get("es_pos_"+i).toString()));
					forma.getMedicamentos().put("tipoFrecuencia_"+medicamentos.get("cod_articulo_"+i),medicamentos.get("tipo_frecuencia_"+i).toString());
					forma.getMedicamentos().put("existencias_"+medicamentos.get("cod_articulo_"+i),medicamentos.get("existencias_"+i));
					forma.getMedicamentos().put("cantidad_"+medicamentos.get("cod_articulo_"+i),medicamentos.get("cantidad_"+i).toString());
					forma.getMedicamentos().put("observaciones_"+medicamentos.get("cod_articulo_"+i),medicamentos.get("observaciones_"+i).toString());
					forma.getMedicamentos().put("esPos_"+medicamentos.get("cod_articulo_"+i),UtilidadTexto.getBoolean(medicamentos.get("es_pos_"+i).toString())+"");
					forma.getMedicamentos().put("via_"+medicamentos.get("cod_articulo_"+i),medicamentos.get("via_"+i).toString());
					forma.getMedicamentos().put("dosis_"+medicamentos.get("cod_articulo_"+i),medicamentos.get("dosis_"+i).toString());						
					forma.getMedicamentos().put("unidosis_"+medicamentos.get("cod_articulo_"+i),medicamentos.get("unidosis_"+i).toString());
					forma.getMedicamentos().put("diastratamiento_"+medicamentos.get("cod_articulo_"+i),ValoresPorDefecto.getNumDiasTratamientoMedicamentos(usuario.getCodigoInstitucionInt()));
					forma.getMedicamentos().put("codigoNaturaleza_"+medicamentos.get("cod_articulo_"+i),medicamentos.get("codigonaturaleza_"+i).toString());
					forma.getMedicamentos().put("frecuencia_"+medicamentos.get("cod_articulo_"+i),medicamentos.get("frecuencia_"+i).toString());
					forma.getMedicamentos().put("estado_"+medicamentos.get("cod_articulo_"+i),medicamentos.get("estado_"+i).toString());
					forma.getMedicamentos().put("conce_"+medicamentos.get("cod_articulo_"+i),medicamentos.get("conce_"+i).toString());
					paso=true; 
					if (!medicamentos.get("estado_"+i).toString().equals("0") && !medicamentos.get("estado_"+i).toString().equals("1"))
					{
						if ((Boolean) medicamentos.get("estado_"+i))
						{ 
							forma.getMedicamentos().put("estado_"+medicamentos.get("cod_articulo_"+i),"1");
						} 
						else
						{ 
							forma.getMedicamentos().put("estado_"+medicamentos.get("cod_articulo_"+i),"0");
							forma.setEsreform(true);
						} 
						
					}
					if (medicamentos.get("estado_"+i).toString().equals("0"))
					{
						forma.setEsreform(true);
					}
					forma.setMedicamento("asignadoMismoArticuloDifDosificacion_"+medicamentos.get("cod_articulo_"+i), ConstantesBD.acronimoNo);
				}
				
				forma.setNumeroElementos(forma.getNumeroElementos()+1);
				
				if(primero)
				{
					nuevoCodigos +=	medicamentos.get("cod_articulo_"+i).toString();
					primero = false;
				}
				else				
					nuevoCodigos +=	"-"+medicamentos.get("cod_articulo_"+i).toString();
			}	
		}
		
		if (!paso)
		{
			for(int i = 0; i < Integer.parseInt(medicamentos.get("numRegistros").toString()); i++)
			{
			if(medicamentos.get("esmedicamento_"+i).toString().equals(ConstantesBD.acronimoSi))
			{
				if (!medicamentos.get("estado_"+i).toString().equals("0") && !medicamentos.get("estado_"+i).toString().equals("1"))
				{
					if ((Boolean) medicamentos.get("estado_"+i))
					{
						forma.getMedicamentos().put("estado_"+medicamentos.get("cod_articulo_"+i),"1");
					}
					else
					{
						forma.getMedicamentos().put("estado_"+medicamentos.get("cod_articulo_"+i),"0");
						forma.setEsreform(true);
					}
					
				}
					
				if (forma.getMedicamentos().get("estado_"+medicamentos.get("cod_articulo_"+i)).toString().equals("0"))
				{
					forma.setEsreform(true);
				}
				
			}
			}
		}
		forma.setMedicamento("codigos",codigos+nuevoCodigos);
		Utilidades.imprimirMapa(forma.getMedicamentos());			
	}
	
	//**************************************************************************************************************************
	
	/**
	 * Valida la existencia de un codigo en el mapa de medicamentos
	 * @param SolicitudMedicamentosForm forma
	 * @param String codigo
	 * */
	public boolean validarExistenciaCodigo(SolicitudMedicamentosForm forma,String codigo)
	{
		String [] resultado = forma.getMedicamento("codigos").toString().split("-");
		
		for(int i = 0 ; i < resultado.length; i ++)
		{
			if(!resultado[i].equals("") )
			{
				if(resultado[i].toString().equals(codigo))
					return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Método que se encarga de validar si se generar autorizacion para el servicio de la orden.
	 * DCU 65 - Solicitud de Medicamentos v2.2
	 * MT 4681
	 * 
	 * @author wilgomcr
	 * @param con
	 * @param citaForm
	 * @param usuario
	 * @param paciente
	 * @param listaDtoAdministrarSolicitudesAutorizar
	 * @param errores
	 * @throws IPSException
	 */
	private void cargarInfoVerificarGeneracionAutorizacion(Connection con,SolicitudMedicamentosForm forma,
			UsuarioBasico usuario, PersonaBasica paciente, ActionErrors errores)throws IPSException
	{
		DtoSubCuentas dtoSubCuenta = null;
		AutorizacionCapitacionDto autorizacionCapitacionDto = null;
		MontoCobroDto montoCobroAutorizacion				= null;
		DatosPacienteAutorizacionDto datosPacienteAutorizar = null;
		OrdenAutorizacionDto ordenAutorizacionDto	= null; 
		List<OrdenAutorizacionDto> listaOrdenesAutorizar	= null;
		List<MedicamentoInsumoAutorizacionOrdenDto> listaArticulosPorAutorizar = null;
		ContratoDto contratoDto = null;
		ConvenioDto convenioDto = null;
		ManejoPacienteFacade manejoPacienteFacade = null;
		OrdenesFacade ordenesFacade	= null;
		InventarioFacade inventarioFacade 		= null;
		ClaseInventarioDto claseInventarioDto	= null; 
		List<AutorizacionCapitacionDto> listaAutorizacionCapitacion = null;
		try{
			UtilidadBD.iniciarTransaccion(con);
			listaOrdenesAutorizar 	= new ArrayList<OrdenAutorizacionDto>();
			ordenesFacade			= new OrdenesFacade();
			inventarioFacade		= new InventarioFacade();
			
			if(forma.getInfoCoberturaArticulo()!=null && !forma.getInfoCoberturaArticulo().isEmpty()){
			
				dtoSubCuenta = forma.getInfoCoberturaArticulo().get(0).getDtoSubCuenta();
				
				convenioDto = new ConvenioDto();
				convenioDto.setCodigo(dtoSubCuenta.getConvenio().getCodigo());
				convenioDto.setNombre(dtoSubCuenta.getConvenio().getNombre());
				if(dtoSubCuenta.getDatosConvenio().getManejaPreupuestoCapitacion()!=null &&
						dtoSubCuenta.getDatosConvenio().getManejaPreupuestoCapitacion().equals(ConstantesBD.acronimoSiChar+"")){
					convenioDto.setConvenioManejaPresupuesto(true);
				}else{
					convenioDto.setConvenioManejaPresupuesto(false);
				}
				contratoDto = new ContratoDto();
				contratoDto.setConvenio(convenioDto);
				contratoDto.setCodigo(dtoSubCuenta.getContrato());
				contratoDto.setNumero(dtoSubCuenta.getNumeroContrato());
			
				ordenAutorizacionDto = new OrdenAutorizacionDto();
				ordenAutorizacionDto.setCodigoOrden(Utilidades.convertirALong(forma.getNumeroSolicitud()+""));
				ordenAutorizacionDto.setConsecutivoOrden(dtoSubCuenta.getSolicitudesSubcuenta().get(0).getConsecutivoSolicitud());
				ordenAutorizacionDto.setContrato(contratoDto);
				ordenAutorizacionDto.setCodigoCentroCostoEjecuta(forma.getCentroCostoSolicitado());
				ordenAutorizacionDto.setEsPyp(forma.isSolPYP());
				ordenAutorizacionDto.setCodigoViaIngreso(paciente.getCodigoUltimaViaIngreso());
	 			
				if(forma.isIndicativoOrdenAmbulatoria()){
					ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenOrdenAmbulatoria);
					String codigoOA=Utilidades.obtenerCodigoOrdenAmbulatoria(con,forma.getOrdenAmbulatoria(),usuario.getCodigoInstitucionInt());
					ordenAutorizacionDto.setOtroCodigoOrden(Utilidades.convertirALong(codigoOA));
				}else{
					ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenOrdenMedica);
	 			}
				ordenAutorizacionDto.setTipoOrden(ConstantesBD.codigoTipoSolicitudMedicamentos);
				listaArticulosPorAutorizar	= null;
				listaArticulosPorAutorizar = ordenesFacade.obtenerMedicamentosInsumosPorAutorizar(forma.getNumeroSolicitud());
				for(MedicamentoInsumoAutorizacionOrdenDto articulo: listaArticulosPorAutorizar){
					articulo.setAutorizar(true);
					claseInventarioDto	= inventarioFacade.obtenerClaseInventarioPorSubGrupo(articulo.getSubGrupoInventario());
					if(claseInventarioDto!=null){
						articulo.setClaseInventario(claseInventarioDto.getCodigo());
						articulo.setGrupoInventario(claseInventarioDto.getCodigoGrupo());
					}
				}
				if(forma.getUrgente()){
					ordenAutorizacionDto.setEsUrgente(true);
				}else{
					ordenAutorizacionDto.setEsUrgente(false);
				}
				ordenAutorizacionDto.setMedicamentosInsumosPorAutorizar(listaArticulosPorAutorizar);
				listaOrdenesAutorizar.add(ordenAutorizacionDto);
				
				boolean manejaMonto = UtilidadTexto.isEmpty(dtoSubCuenta.getMontoCobro()) || dtoSubCuenta.getMontoCobro() == 0 ? false : true;
								
				datosPacienteAutorizar	= new DatosPacienteAutorizacionDto();
				datosPacienteAutorizar.setCodigoPaciente(dtoSubCuenta.getCodigoPaciente());
				datosPacienteAutorizar.setCodigoIngresoPaciente(dtoSubCuenta.getIngreso());
				datosPacienteAutorizar.setTipoPaciente(dtoSubCuenta.getTipoPaciente());
				datosPacienteAutorizar.setClasificacionSocieconomica(dtoSubCuenta.getClasificacionSocioEconomica());
				datosPacienteAutorizar.setTipoAfiliado(dtoSubCuenta.getTipoAfiliado());
				datosPacienteAutorizar.setNaturalezaPaciente(dtoSubCuenta.getNaturalezaPaciente());
				datosPacienteAutorizar.setCodConvenioCuenta(paciente.getCodigoConvenio());
				datosPacienteAutorizar.setCuentaAbierta(true);
				datosPacienteAutorizar.setCuentaManejaMontos(manejaMonto);
				datosPacienteAutorizar.setPorcentajeMontoCuenta(dtoSubCuenta.getPorcentajeMontoCobro());
								
				montoCobroAutorizacion	= new MontoCobroDto();
				montoCobroAutorizacion.setCantidadMonto(dtoSubCuenta.getCantidadMontoGeneral());
				montoCobroAutorizacion.setCodDetalleMonto(dtoSubCuenta.getMontoCobro());
				montoCobroAutorizacion.setPorcentajeMonto(dtoSubCuenta.getPorcentajeMontoGeneral());
				montoCobroAutorizacion.setTipoDetalleMonto(dtoSubCuenta.getTipoDetalleMonto());
				montoCobroAutorizacion.setTipoMonto(dtoSubCuenta.getTipoMonto());
				montoCobroAutorizacion.setValorMonto(dtoSubCuenta.getValorMontoGeneral());
									
				autorizacionCapitacionDto = new AutorizacionCapitacionDto();
				autorizacionCapitacionDto.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
				autorizacionCapitacionDto.setLoginUsuario(usuario.getLoginUsuario());
				autorizacionCapitacionDto.setCodigoPersonaUsuario(usuario.getCodigoPersona());
				autorizacionCapitacionDto.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAutomatica);
				autorizacionCapitacionDto.setCentroAtencion(usuario.getCodigoCentroAtencion());
				autorizacionCapitacionDto.setDatosPacienteAutorizar(datosPacienteAutorizar);
				autorizacionCapitacionDto.setMontoCobroAutorizacion(montoCobroAutorizacion);
				autorizacionCapitacionDto.setOrdenesAutorizar(listaOrdenesAutorizar);
									
				//Proceso que se encarga de verificar si se generara o asociara autorizacion para la orden.
				manejoPacienteFacade = new ManejoPacienteFacade();
				listaAutorizacionCapitacion = manejoPacienteFacade.verificarGenerarAutorizacionCapitacion(autorizacionCapitacionDto);
								}
								
			if(listaAutorizacionCapitacion!=null && !listaAutorizacionCapitacion.isEmpty())
			{	//Se adiciona mensaje para los servicio que no se autorizaron
				manejoPacienteFacade.obtenerMensajesError(listaAutorizacionCapitacion, errores);
			}
			UtilidadBD.finalizarTransaccion(con);
		}catch (IPSException ipsme) {
			UtilidadBD.abortarTransaccion(con);
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
								
	/**
	 * Metodo que se encarga de enviar los datos necesarios para validar la anulacion de la
	 * autorizacion
	 * 
	 * @param mundoMedicamento
	 * @param usuario
	 * @throws IPSException
	 */
	public void cargarInfoParaAnulacionAutorizacion(SolicitudMedicamentos mundoMedicamento,
			UsuarioBasico usuario)throws IPSException
	{
		AnulacionAutorizacionSolicitudDto anulacionDto				= null;
		ManejoPacienteFacade manejoPacienteFacade		= null;
		Medicos medicos	= null;
		try{
			anulacionDto= new AnulacionAutorizacionSolicitudDto();
			anulacionDto.setMotivoAnulacion(mundoMedicamento.getMotivoAnulacion());
			anulacionDto.setFechaAnulacion(UtilidadFecha.getFechaActualTipoBD());
			anulacionDto.setHoraAnulacion(UtilidadFecha.getHoraActual());
			medicos		= new Medicos(); 
			medicos.setCodigoMedico(usuario.getCodigoPersona());
			anulacionDto.setMedicoAnulacion(medicos);
			anulacionDto.setLoginUsuarioAnulacion(usuario.getLoginUsuario());
			anulacionDto.setNumeroSolicitud(mundoMedicamento.getNumeroSolicitud());
			
			manejoPacienteFacade = new ManejoPacienteFacade();
			manejoPacienteFacade.validarAnulacionAutorizacionCapitaSolictud(anulacionDto, 
					ConstantesBD.claseOrdenOrdenMedica,ConstantesBD.codigoTipoSolicitudMedicamentos,null,usuario.getCodigoInstitucionInt());
			
		}catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	
	/**
	 * Metodo encargado de generar reporte de la solicitud de medicamentos
	 * 
	 * @param con
	 * @param request
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param solicitudMedicamentos
	 * @param solicitudMedicamentosDD
	 * @author hermorhu
	 * @created 08-03-2013
	 */
	@SuppressWarnings("unchecked")
	public void generarReporteSolicitudMedicamentos(Connection con, HttpServletRequest request, SolicitudMedicamentosForm forma, PersonaBasica paciente, UsuarioBasico usuario,  SolicitudMedicamentos solicitudMedicamentos,  SolicitudMedicamentos solicitudMedicamentosDD) {
		
		GeneradorReporteSolicitudMedicamentos generadorReporteSolicitudMedicamentos = null;
		JasperPrint reporteOriginal = null;
		
		ImpresionSolicitudMedicamentosDto impresionSolicitudMedicamentosDto = null;
		ImpresionMedicamentoInsumoSolicitadoDto impresionMedicamentoInsumoSolicitadoDto = null;
		
		Articulo articulo = null;
		ArticuloSolicitudMedicamentos articuloSolicitudMedicamentos = null;
		
		try {
			
			InstitucionBasica institucion = (InstitucionBasica) request.getSession().getAttribute(KEY_SESSION_INSTITUCION);
			
			solicitudMedicamentos.cargarSolicitudMedicamentos(con, usuario.getCodigoInstitucionInt());
			paciente.cargarEmpresaRegimen(con, paciente.getCodigoPersona(), paciente.getCodigoTipoRegimen()+"");
			
			if(solicitudMedicamentosDD.getNumeroSolicitud() > 0) { 
				solicitudMedicamentosDD.cargarSolicitudMedicamentos(con, usuario.getCodigoInstitucionInt());
			}
			
			impresionSolicitudMedicamentosDto = new ImpresionSolicitudMedicamentosDto();
			
			impresionSolicitudMedicamentosDto.setRutaLogo("../" + institucion.getLogoJsp());
			impresionSolicitudMedicamentosDto.setUbicacionLogo(institucion.getUbicacionLogo());
			
			impresionSolicitudMedicamentosDto.setRazonSocial(institucion.getRazonSocialInstitucionCompleta());
			impresionSolicitudMedicamentosDto.setNit(institucion.getNit());
			impresionSolicitudMedicamentosDto.setDireccion(institucion.getDireccion());
			impresionSolicitudMedicamentosDto.setTelefono(institucion.getTelefono());
			/**
			 * MT 6762
			 * 
			 * Se cambia el nombre del paciente para que salga segun el caso de uso.
			 * Se cambia el nombre de la cama por el codigo de la cama tal y como lo dice el caso de uso
			 */
			if(paciente.getSegundoApellido()==null){
				paciente.setSegundoApellido("");
			}
			if(paciente.getSegundoNombre()==null){
				paciente.setSegundoNombre("");
			}
			impresionSolicitudMedicamentosDto.setPaciente(paciente.getPrimerApellido()+" "+paciente.getSegundoApellido()+" "+paciente.getPrimerNombre()+" "+ paciente.getSegundoNombre());
			if(!paciente.getCama().equals("")){
				impresionSolicitudMedicamentosDto.setCama(", Cama: "+paciente.getCama().substring(paciente.getCama().lastIndexOf("  ")).trim());
			}else{
				impresionSolicitudMedicamentosDto.setCama("");
			}
			
		
			
			/**
			 * FIn MT 6762
			 */
			impresionSolicitudMedicamentosDto.setDocumentoIdentificacion(paciente.getCodigoTipoIdentificacionPersona() +" "+ paciente.getNumeroIdentificacionPersona());
			impresionSolicitudMedicamentosDto.setEdad(paciente.getEdadDetallada());
			impresionSolicitudMedicamentosDto.setTelefonoPaciente(paciente.getTelefonoFijo() != null ? paciente.getTelefonoFijo() : "");
			
			if(paciente.getUltimaViaIngreso().contains("-")){
				impresionSolicitudMedicamentosDto.setViaIngreso(paciente.getUltimaViaIngreso().substring(0, paciente.getUltimaViaIngreso().lastIndexOf("-")));
			}else{
				impresionSolicitudMedicamentosDto.setViaIngreso(paciente.getUltimaViaIngreso());
			}
			
			
		
			impresionSolicitudMedicamentosDto.setArea(solicitudMedicamentos.getCentroCostoSolicitante().getNombre());
			impresionSolicitudMedicamentosDto.setResponsable(paciente.getConvenioPersonaResponsable());
			impresionSolicitudMedicamentosDto.setCentroSolicitante(usuario.getCentroAtencion());
			impresionSolicitudMedicamentosDto.setFarmacia(solicitudMedicamentos.getCentroCostoSolicitado().getNombre());
			impresionSolicitudMedicamentosDto.setFechaHoraOrden(solicitudMedicamentos.getFechaSolicitud()+" "+solicitudMedicamentos.getHoraSolicitud());
			impresionSolicitudMedicamentosDto.setOrden(String.valueOf(solicitudMedicamentos.getConsecutivoOrdenesMedicas()));
			impresionSolicitudMedicamentosDto.setIngreso(paciente.getConsecutivoIngreso());
			
			if(solicitudMedicamentos.getEstadoHistoriaClinica().getNombre().trim().equals(SOLICITUD_ANULADA)) {
				String datosMedicoAnulacion = solicitudMedicamentos.consultarDatosMedicoAnulacion(con, solicitudMedicamentos.getCodigoMedicoAnulacion());
				impresionSolicitudMedicamentosDto.setEsAnulada(true);
				impresionSolicitudMedicamentosDto.setMotivoAnulacion(solicitudMedicamentos.getMotivoAnulacion() + " " + solicitudMedicamentos.getFechaAnulacion() + " " + solicitudMedicamentos.getHoraAnulacion() + " " + datosMedicoAnulacion);
			} else {
				impresionSolicitudMedicamentosDto.setEsAnulada(false);
			}
			
			List<ImpresionMedicamentoInsumoSolicitadoDto> listaMedicamentosInsumosSolicitud = new ArrayList<ImpresionMedicamentoInsumoSolicitadoDto>();
			
			if(solicitudMedicamentos.getArticulos().size() > 0 || solicitudMedicamentosDD.getArticulos().size() > 0) {
			
				Iterator<ArticuloSolicitudMedicamentos> it1 = solicitudMedicamentos.getArticulos().iterator();
				
				while (it1.hasNext()) {
					articuloSolicitudMedicamentos = it1.next();
					articulo = new Articulo();
					
					articulo.cargarArticulo(con, articuloSolicitudMedicamentos.getArticulo());
					
					impresionMedicamentoInsumoSolicitadoDto = new ImpresionMedicamentoInsumoSolicitadoDto();
					
					impresionMedicamentoInsumoSolicitadoDto.setArticulo(articulo.getCodigo());
					impresionMedicamentoInsumoSolicitadoDto.setMedicamento(articulo.getDescripcion().trim()+" CONC. "+articulo.getConcentracion().trim()+" FF. "+articulo.getNomFormaFarmaceutica().trim()+" UM. "+articulo.getNomUnidadMedida().trim());
					
					String dosis = "";
					if(articuloSolicitudMedicamentos.getDosis() != null && !articuloSolicitudMedicamentos.getDosis().equals("null")) {
						dosis += articuloSolicitudMedicamentos.getDosis();
					}		
					if(articuloSolicitudMedicamentos.getUnidosis() != null && !articuloSolicitudMedicamentos.getUnidosis().equals("null")) {
						dosis += Utilidades.obtenerUnidadMedidadUnidosisArticulo(con, articuloSolicitudMedicamentos.getUnidosis());
					}
					impresionMedicamentoInsumoSolicitadoDto.setDosis(dosis);
					
					impresionMedicamentoInsumoSolicitadoDto.setFrecuencia("Cada "+ articuloSolicitudMedicamentos.getFrecuencia() + " " + articuloSolicitudMedicamentos.getTipoFrecuencia());
					impresionMedicamentoInsumoSolicitadoDto.setVia(articuloSolicitudMedicamentos.getVia());
					impresionMedicamentoInsumoSolicitadoDto.setDiasTratamiento(articuloSolicitudMedicamentos.getDiasTratamiento());
					impresionMedicamentoInsumoSolicitadoDto.setCantidad(articuloSolicitudMedicamentos.getCantidadSolicitada()+" ("+UtilidadN2T.convertirLetras(articuloSolicitudMedicamentos.getCantidadSolicitada())+")");
					impresionMedicamentoInsumoSolicitadoDto.setObservaciones(articuloSolicitudMedicamentos.getObservaciones());
					impresionMedicamentoInsumoSolicitadoDto.setPos(articuloSolicitudMedicamentos.getEsPos().equals(ConstantesBD.valorTrueEnString) ? "SI" : "NO");
				
					impresionMedicamentoInsumoSolicitadoDto.setEsMedicamento(articulo.getEsMedicamento());
					
					listaMedicamentosInsumosSolicitud.add(impresionMedicamentoInsumoSolicitadoDto);
				}
				
				Iterator<ArticuloSolicitudMedicamentos> it2 = solicitudMedicamentosDD.getArticulos().iterator();
				
				while (it2.hasNext()) {
					articuloSolicitudMedicamentos = it2.next();
					articulo = new Articulo();
					
					articulo.cargarArticulo(con, articuloSolicitudMedicamentos.getArticulo());
					
					impresionMedicamentoInsumoSolicitadoDto = new ImpresionMedicamentoInsumoSolicitadoDto();
					
					impresionMedicamentoInsumoSolicitadoDto.setArticulo(articulo.getCodigo());
					impresionMedicamentoInsumoSolicitadoDto.setMedicamento(articulo.getDescripcion().trim()+" CONC. "+articulo.getConcentracion().trim()+" FF. "+articulo.getNomFormaFarmaceutica().trim()+" UM. "+articulo.getNomUnidadMedida().trim());
					
					String dosis = "";
					if(articuloSolicitudMedicamentos.getDosis() != null && !articuloSolicitudMedicamentos.getDosis().equals("null")) {
						dosis += articuloSolicitudMedicamentos.getDosis();
					}		
					if(articuloSolicitudMedicamentos.getUnidosis() != null && !articuloSolicitudMedicamentos.getUnidosis().equals("null")) {
						dosis += Utilidades.obtenerUnidadMedidadUnidosisArticulo(con, articuloSolicitudMedicamentos.getUnidosis());
					}
					impresionMedicamentoInsumoSolicitadoDto.setDosis(dosis);
					
					impresionMedicamentoInsumoSolicitadoDto.setFrecuencia("Cada "+ articuloSolicitudMedicamentos.getFrecuencia() + " " + articuloSolicitudMedicamentos.getTipoFrecuencia());
					impresionMedicamentoInsumoSolicitadoDto.setVia(articuloSolicitudMedicamentos.getVia());
					impresionMedicamentoInsumoSolicitadoDto.setDiasTratamiento(articuloSolicitudMedicamentos.getDiasTratamiento());
					impresionMedicamentoInsumoSolicitadoDto.setCantidad(articuloSolicitudMedicamentos.getCantidadSolicitada()+" ("+UtilidadN2T.convertirLetras(articuloSolicitudMedicamentos.getCantidadSolicitada())+")");
					impresionMedicamentoInsumoSolicitadoDto.setObservaciones(articuloSolicitudMedicamentos.getObservaciones());
					impresionMedicamentoInsumoSolicitadoDto.setPos(articuloSolicitudMedicamentos.getEsPos().equals(ConstantesBD.valorTrueEnString) ? "SI" : "NO");
				 
					impresionMedicamentoInsumoSolicitadoDto.setEsMedicamento(articulo.getEsMedicamento());
					
					listaMedicamentosInsumosSolicitud.add(impresionMedicamentoInsumoSolicitadoDto);
				}
			}
			
			impresionSolicitudMedicamentosDto.setListaMedicamentosInsumosSolicitados(listaMedicamentosInsumosSolicitud);
			
			impresionSolicitudMedicamentosDto.setFirmaDigital("../" + System.getProperty("ADJUNTOS") + System.getProperty("FIRMADIGITAL") + System.getProperty("file.separator") + Medico.obtenerFirmaDigitalMedico(solicitudMedicamentos.getCodigoMedicoSolicitante()));
			
			//FIXME 
			String[] datosMedicos = solicitudMedicamentos.getDatosMedico().split("\\d{2}:\\d{2}\n");
			
			if(datosMedicos.length>1)
				impresionSolicitudMedicamentosDto.setDatosMedico(datosMedicos[1]);
			else
				impresionSolicitudMedicamentosDto.setDatosMedico(solicitudMedicamentos.getDatosMedico());
			
			impresionSolicitudMedicamentosDto.setUsuario(usuario.getNombreUsuario() + " (" + usuario.getLoginUsuario() + ")");
			impresionSolicitudMedicamentosDto.setFechaEntrega(UtilidadFecha.getFechaActual(con));
			impresionSolicitudMedicamentosDto.setHoraEntrega(UtilidadFecha.getHoraActual(con));
			
			//Se genera el reporte de la solicitud de medicamentos
			generadorReporteSolicitudMedicamentos = new GeneradorReporteSolicitudMedicamentos(impresionSolicitudMedicamentosDto);
			reporteOriginal = generadorReporteSolicitudMedicamentos.generarReporte();
			
			//Se envia a la jsp el nombre del archivo generado para que lo muestre 
			forma.setNombreArchivoGenerado(generadorReporteSolicitudMedicamentos.exportarReportePDF(reporteOriginal, "FormatoReporteSolicitudMedicamentos"));
		
		} catch(Exception e) {
			Log4JManager.error(e.getMessage(), e);
		}
			
	}
}
	