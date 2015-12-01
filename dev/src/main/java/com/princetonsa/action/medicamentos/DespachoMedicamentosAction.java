/*
 * @(#)DespchoMedicamentosAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.medicamentos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
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
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ElementoApResource;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.interfaces.UtilidadBDInterfaz;
import util.inventarios.UtilidadInventarios;
import util.pdf.PdfReports;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.medicamentos.DespachoMedicamentosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.AlmacenParametros;
import com.princetonsa.mundo.inventarios.EquivalentesDeInventario;
import com.princetonsa.mundo.medicamentos.DespachoMedicamentos;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.pdf.DespachoMedicamentosPdf;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.AutorizacionesCapitacionSub;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.IEntregaMedicamentosInsumosEntSubcontratadasServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConvenioServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IAurorizacionesEntSubCapitacionServicio;


/**
 * Action, controla todas las opciones dentro del despacho de medicamentos 
 * incluyendo los posibles casos de error y los casos de flujo.
 * @version 1.0, Agosto 31, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R铆os</a>
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Arlen L&oacute;pez Correa.</a>
 */
@SuppressWarnings("unchecked")
public class DespachoMedicamentosAction extends Action  
{
	/** Objeto para manejar los logs de esta clase  */
	private Logger logger = Logger.getLogger(DespachoMedicamentosAction.class);
	
	/** Servicios  */
	IEntregaMedicamentosInsumosEntSubcontratadasServicio entregaMedicamentosInsumosEntSubcontratadasServicio
				= FacturacionServicioFabrica.crearEntregaMedicamentosInsumosEntSubcontratadasServicio();
	
	IAurorizacionesEntSubCapitacionServicio aurorizacionesEntSubCapitacionServicio
				= ManejoPacienteServicioFabrica.crearAurorizacionesEntSubCapitacionServicio();
	
	IConvenioServicio convenioServicio = FacturacionServicioFabrica.crearConvenioServicio();
	
	/** Mensajes  */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.medicamentos.DespachoMedicamentosForm");
	
		
	/**
	 * M閠odo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try {
			if (response==null); //Para evitar que salga el warning
			if(form instanceof DespachoMedicamentosForm)
			{
				try	{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}catch(SQLException e) {
					logger.warn("No se pudo abrir la conexi贸n"+e.toString());
				}

				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				DespachoMedicamentosForm despachoForm =(DespachoMedicamentosForm)form;
				String estado=despachoForm.getEstado();
				logger.info("\n\n ESTADO DESPACHO MEDICAMENTOS ACTION---->"+estado+"\n\n");				

				if(estado.equals("listarSolicitudes"))
				{
					ActionForward validacionesGenerales = validacionesAccesoUsuario(mapping, request, usuario);
					if (validacionesGenerales != null)
					{
						UtilidadBD.cerrarConexion(con);
						return validacionesGenerales ;
					}
				}   

				if(estado == null)
				{
					despachoForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());	
					logger.warn("Estado no valido dentro del flujo de Despacho Medicamentos (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("listarSolicitudes"))
				{
					despachoForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
					return this.accionListarSolicitudes(despachoForm, mapping, con, estado, usuario, paciente);
				}
				else if (estado.equals("filtrarXCentroCosto"))
				{
					return this.accionFiltrarXCentroCosto(despachoForm, mapping, con, estado, request);
				}
				else if(estado.equals("listarSolicitudesXCentroCosto"))
				{
					return this.accionListarSolicitudesXCentroCosto(despachoForm, mapping, con, estado, usuario, paciente);
				}
				else if (estado.equals("listarSolicitudesXPaciente"))
				{
					if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
					{
						logger.warn("Paciente no v醠ido (null)");			
						request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaError");
					}

					return this.accionListarSolicitudesXPaciente(despachoForm, mapping, con, estado, usuario, paciente);

				}
				else if(estado.equals("ordenarSolicitudes"))
				{
					return this.accionOrdenarSolicitudes(despachoForm, mapping, request, con, usuario);
				}
				else if(estado.equals("detalleSolicitud")) 
				{
					despachoForm.inicializarForma(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
					ActionForward validacionesDatosDosisForward= null;
					validacionesDatosDosisForward= validacionesDatosDosisConsistentes(despachoForm, mapping, con, request, usuario);	
					if(validacionesDatosDosisForward==null)
						return this.accionDetalleSolicitud(despachoForm,mapping,request, usuario,con); 
					else
						return validacionesDatosDosisForward;
				}
				else if(estado.equals("imprimirPerfilFarmacoterapia"))
				{
					return this.accionImprimirPerfilFarmacoterapia(despachoForm,mapping,request,usuario,paciente,con);
				}
				else if (estado.equals("agregar"))
				{
					return this.accionAgregarOtro(despachoForm,mapping,con);
				}
				else if (estado.equals("eliminarEleccion"))
				{
					return this.accionEliminarInsumo(despachoForm,mapping,con);
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.cerrarConexion(con);
					response.sendRedirect(despachoForm.getLinkSiguiente());
					return null;
				}
				else if (estado.equals("volver"))
				{
					despachoForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
					return this.accionListarSolicitudes(despachoForm, mapping, con, estado, usuario, paciente);
				}
				else if (estado.equals("popup"))
				{

					//se obtienen los valores del popup 'busquedaSustituto.jsp', el cual nos entrega 
					//el valor en el form de 'setCombinado' y all铆 se asignan los valores del numeroSustituto
					//y el numeroOriginal
					int aGuardar[] = new int[2];
					aGuardar[0] = despachoForm.getNumeroSustituto();
					aGuardar[1] = despachoForm.getNumeroOriginal();

					//restringe en la busqueda el numeroSustituto insertado
					despachoForm.getColeccionArticulosInsertados().add(despachoForm.getNumeroSustituto()+"");


					//Se cre贸 una nueva colecci贸n para adicionar un vector de enteros (numeroSustituto-numeroOriginal)
					despachoForm.getColeccionNumerosSustitutos().add(aGuardar);
 
					DespachoMedicamentos desp=new DespachoMedicamentos ();

					//Se envia el numeroSolicitud, numeroSustituto-numeroOriginal, y se hace una consulta para obtener
					//los medicamentos originales + medicamentos sustitutos
					Collection paso=desp.consultaGenerada(con, despachoForm.getNumeroSolicitud(), despachoForm.getColeccionNumerosSustitutos(), usuario.getCodigoInstitucionInt(), despachoForm.getNumeroOriginal());
					
					
					Iterator ite= paso.iterator(); 
					String[] codigos;
					while (ite.hasNext())
					{
						HashMap temporal;
						temporal=(HashMap)ite.next();
						if (temporal.get("codigosustitutoprincipal")!=null && !temporal.get("codigosustitutoprincipal").toString().equals("vacio"))
						{
							codigos= temporal.get("codigosustitutoprincipal").toString().split("@");
							String codsus= codigos[0];
							String codprin= codigos[1];
							Collection eq= desp.consultaEquivalentes(con, Integer.parseInt (codsus), Integer.parseInt(codprin));
					       Iterator ito= eq.iterator();
					       HashMap temEq= (HashMap)ito.next(); 
					       temporal.put("cantidadeq", temEq.get("cantidad"));
					       
						}
						
					}
					despachoForm.setCol(paso);
					
			
					UtilidadBD.cerrarConexion(con);

					despachoForm.setTermino(true);

					return null;

				}
				else if (estado.equals("eliminarSustituto"))
				{
					int aEliminar[]=new int[0],  aGuardar[]=new int[2];
					String aEliminar2="";
					aGuardar[0]=despachoForm.getNumeroSustituto() ;
					aGuardar[1]=despachoForm.getNumeroOriginal();
					DespachoMedicamentos desp=new DespachoMedicamentos (); 

					//eliminar sustituto de la vista
					Iterator it=despachoForm.getColeccionNumerosSustitutos().iterator();

					while (it.hasNext())
					{
						int temporal[];
						temporal=(int[])it.next();

						if(aGuardar[0]==temporal[0])
						{
							if(aGuardar[1]==temporal[1])
							{
								aEliminar=temporal;
							}
						}
					}

					if (aEliminar.length==2)
					{
						despachoForm.getColeccionNumerosSustitutos().remove(aEliminar);
					}
					else
					{
						//si entra a este caso es que ese valor ya esta en BD
						int validarEliminar=desp.eliminarSustitutoConDespachoCero(con,aGuardar[0], despachoForm.getNumeroSolicitud());
						if(validarEliminar<=0)
						{
							logger.warn("Error en el eliminar ");
							UtilidadBD.cerrarConexion(con);
							despachoForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
							ArrayList atributosError = new ArrayList();
							atributosError.add(" Detalle SolicitudesMedicamentos");
							request.setAttribute("codigoDescripcionError", "errors.invalid");				
							request.setAttribute("atributosError", atributosError);
							return mapping.findForward("paginaError");	
						}
					}

					//eliminar sustituto de la restricci贸n del tag
					Iterator it2=despachoForm.getColeccionArticulosInsertados().iterator();
					int contDespachoParcial=0;
					while (it2.hasNext())
					{
						String temporal;
						contDespachoParcial++;
						temporal= it2.next()+"";

						if(temporal.equals(aGuardar[0]+""))
						{
							aEliminar2=temporal;
							despachoForm.setDespachoMap("parcial_"+contDespachoParcial,"");
						}
					}

					if (aEliminar2!=null)
					{
						despachoForm.getColeccionArticulosInsertados().remove(aEliminar2);

					}

					//Se envia el numeroSolicitud, numeroSustituto-numeroOriginal, y se hace una consulta para obtener
					//los medicamentos originales + medicamentos sustitutos
					Collection paso=desp.consultaGenerada(con, despachoForm.getNumeroSolicitud(), despachoForm.getColeccionNumerosSustitutos(), usuario.getCodigoInstitucionInt(), despachoForm.getNumeroOriginal());
					despachoForm.setCol(paso);

					UtilidadBD.cerrarConexion(con);
					despachoForm.setTermino(true);
					return mapping.findForward("recargarPagina");
				}
				else if (estado.equals("mostrarPopupSustitutos"))
				{
					return this.accionMostrarPopupSustitutos(despachoForm,mapping,request,usuario,con);
				}	
				else if (estado.equals("recargar")) 
				{
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaDetalle");
				}
				else if (estado.equals("salirGuardar"))
				{
					return this.accionSalirGuardar(despachoForm,mapping,request,usuario,con);
				}

				else if (estado.equals("detalleSolicitudOtraFuncionalidad")) 
				{
					return this.accionDetalleSolicitudOtraFuncionalidad(despachoForm, mapping, request, usuario, paciente);
				}

				else if (estado.equals("imprimir"))
				{

					String nombreArchivo;
					Random r=new Random();
					nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
					DespachoMedicamentosPdf.pdfDespachoMedicamentos(ValoresPorDefecto.getFilePath() + nombreArchivo,despachoForm,usuario,paciente,con);
					UtilidadBD.cerrarConexion(con);
					request.setAttribute("nombreArchivo", nombreArchivo);
					request.setAttribute("nombreVentana", "Consulta Diagnosticos");
					return mapping.findForward("abrirPdf");
				}	
				else
				{
					despachoForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());	
					logger.warn("Estado no valido dentro del flujo de Despacho Medicamentos (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
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
	 * @param despachoForm
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionMostrarPopupSustitutos(
			DespachoMedicamentosForm forma, ActionMapping mapping,
			HttpServletRequest request, UsuarioBasico usuario, Connection con) throws SQLException {
		
		Iterator ite;
		int posColeccion;
		int codArtPrincipal = ConstantesBD.codigoNuncaValido;
		int codArtEquivalente = ConstantesBD.codigoNuncaValido;
		int cantSolArtPpal = ConstantesBD.codigoNuncaValido;
		
		// Se buscan los codigos tanto del articulo principal como del equivalente
		ite = forma.getCol().iterator();
		posColeccion = 0;
		while (ite.hasNext()) {
			HashMap colMap = (HashMap) ite.next();
			if(forma.getPosColeccion()==posColeccion){
				codArtPrincipal = Utilidades.convertirAEntero(colMap.get("codigosustitutoprincipal").toString().split("@")[1]);
				codArtEquivalente = Utilidades.convertirAEntero(colMap.get("codigosustitutoprincipal").toString().split("@")[0]);
			}
			posColeccion++;
		}
		
		// Se busca la cantidad solicitada del articulo principal
		ite = forma.getCol().iterator();
		posColeccion = 0;
		while (ite.hasNext()) {
			HashMap colMap = (HashMap) ite.next();
			if(Utilidades.convertirAEntero(colMap.get("codigo")+"") == codArtPrincipal){
				cantSolArtPpal = Utilidades.convertirAEntero(colMap.get("cantidad")+"");
			}
			posColeccion++;
		}
		
		// Se consulta la informaci髇 de los dos articulos
		HashMap mapa = EquivalentesDeInventario.consultarArticulo(con, codArtPrincipal, codArtEquivalente);
		
		// Se cargan los datos
		forma.setEquivalentesMap("articuloPrincipal", mapa.get("descArtPrincipal"));
		forma.setEquivalentesMap("solicitadoPrincipal", cantSolArtPpal);
		forma.setEquivalentesMap("articuloEquivalente", mapa.get("descArtEquivalente"));
		forma.setEquivalentesMap("solicitadoEquivalente", (cantSolArtPpal*Utilidades.convertirAEntero(mapa.get("cantidadEquivalente")+"")));
		
		Utilidades.imprimirMapa(mapa);
		
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("popupSustitutos");
	}

	
	/**
	 * 
	 * @param despachoForm
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward validacionesDatosDosisConsistentes(DespachoMedicamentosForm despachoForm, ActionMapping mapping, Connection con, HttpServletRequest request, UsuarioBasico usuario) 
	{
		if(!DespachoMedicamentos.dosisConsistentesSolicitud(con, despachoForm.getNumeroSolicitud()))
		{
			logger.warn("datos no consistentes de la solicitud");
			ArrayList atributosError = new ArrayList();
			Solicitud sol= new Solicitud();
			try 
			{
				sol.cargar(con, despachoForm.getNumeroSolicitud());
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			atributosError.add(sol.getConsecutivoOrdenesMedicas()+"");
			request.setAttribute("codigoDescripcionError", "error.despachoMed.datosDosisInconsistentes");				
			request.setAttribute("atributosError", atributosError);
			despachoForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());	
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaError");
		}
		return null;
	}

	
	/**
	 * Este m茅todo especifica las acciones a realizar en el estado
	 * listar DespachoMedicamentos
	 * @param despachoForm DespachoMedicamentosForm
	 * @param mapping Mapping para manejar la navegaci贸n
	 * @param con Conexi贸n con la fuente de datos
	 * @return ActionForward  a la p谩gina "listadoSolicitudesMedicamentos.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListarSolicitudes	(	DespachoMedicamentosForm despachoForm,
														ActionMapping mapping,
														Connection con,
														String estado,
														UsuarioBasico usuario,
														PersonaBasica paciente
														) throws SQLException 
	{
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listarSolicitudes")	;	
	}
	
	
    /**
	 * Este m茅todo especifica las acciones a realizar en el estado
	 * listar DespachoMedicamentos
	 * @param despachoForm DespachoMedicamentosForm
	 * @param mapping Mapping para manejar la navegaci贸n
	 * @param con Conexi贸n con la fuente de datos
	 * @return ActionForward  a la p谩gina "listadoSolicitudesMedicamentos.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionFiltrarXCentroCosto	(	DespachoMedicamentosForm despachoForm,
														ActionMapping mapping,
														Connection con,
														String estado,
														HttpServletRequest request
													) throws SQLException 
	{
		quitarPaciente(con, request);
		
		despachoForm.setEstado(estado);
		despachoForm.setTipoListado("centroCosto");
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("filtroArea")	;	
	}
	
	
	/**
	 * quita el paciente de la sesion postulando 
	 * @param con
	 * @param request
	 */
	private void quitarPaciente(Connection con, HttpServletRequest request)
	{
		PersonaBasica persona= new PersonaBasica() ;
		request.getSession().setAttribute("pacienteActivo",persona);
		/*
        persona.setCodigoPersona(-1);
        try 
        {
			persona.cargar(con,-1);
			persona.cargarPaciente(con, -1, "-1","-1");
		} 
        catch (SQLException e) 
        {
			e.printStackTrace();
		}
		*/
	}
	

	/**
	 * Este m茅todo especifica las acciones a realizar en el estado
	 * listar DespachoMedicamentos
	 * @param despachoForm DespachoMedicamentosForm
	 * @param mapping Mapping para manejar la navegaci贸n
	 * @param con Conexi贸n con la fuente de datos
	 * @return ActionForward  a la p谩gina "listadoSolicitudesMedicamentos.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListarSolicitudesXPaciente	(	DespachoMedicamentosForm despachoForm,ActionMapping mapping,
																Connection con,	String estado, UsuarioBasico usuario,
																PersonaBasica paciente) throws SQLException 
	{
		DespachoMedicamentos mundo = new DespachoMedicamentos();
		despachoForm.setEstado(estado);
		despachoForm.setTipoListado("paciente");
		despachoForm.setEstadoListado(estado);
		despachoForm.setCol(mundo.listadoSolicitudesMedicamentos(con, usuario.getCodigoCentroCosto(), paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt(), -1, -1,-1, -1));
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listarSolicitudes")	;	
	}	
	
	
	/**
	 * Este m茅todo especifica las acciones a realizar en el estado
	 * listar DespachoMedicamentos
	 * @param despachoForm DespachoMedicamentosForm
	 * @param mapping Mapping para manejar la navegaci贸n
	 * @param con Conexi贸n con la fuente de datos
	 * @return ActionForward  a la p谩gina "listadoSolicitudesMedicamentos.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListarSolicitudesXCentroCosto	(	DespachoMedicamentosForm despachoForm,
																ActionMapping mapping,
																Connection con,
																String estado,
																UsuarioBasico usuario,
																PersonaBasica paciente) throws SQLException 
	{
		DespachoMedicamentos mundo= new DespachoMedicamentos();
		despachoForm.setEstado(estado);
		despachoForm.setTipoListado("paciente");
		despachoForm.setEstadoListado(estado);
		despachoForm.setCol(mundo.listadoSolicitudesMedicamentos(con,  usuario.getCodigoCentroCosto(), 0, usuario.getCodigoInstitucionInt(), despachoForm.getAreaFiltro(), despachoForm.getCamaFiltro(), despachoForm.getHabitacionFiltro(), despachoForm.getPisoFiltro()));
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listarSolicitudes")	;	
	}
	
	
	/**
	 * M茅todo de ordenamiento del pager, seg煤n la columna que sea seleccionada. 
	 * @param despachoForm
	 * @param mapping
	 * @param request
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionOrdenarSolicitudes(	DespachoMedicamentosForm despachoForm,
																			ActionMapping mapping,
																			HttpServletRequest request, 
																			Connection con,
																			UsuarioBasico usuario) throws SQLException
	{
		try
		{
			despachoForm.setCol(Listado.ordenarColumna(new ArrayList(despachoForm.getCol()),despachoForm.getUltimaPropiedad(),despachoForm.getColumna()));
			despachoForm.setUltimaPropiedad(despachoForm.getColumna());
			UtilidadBD.cerrarConexion(con);
		}
		catch(Exception e)
		{
			logger.warn("Error en el listado de solicitudes medicamentos ");
			UtilidadBD.cerrarConexion(con);
			despachoForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Listado SolicitudesMedicamentos");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");		
		}
		return mapping.findForward("listarSolicitudes")	;
	}
	
	
	/**
	 * Este m閠odo especifica las acciones a realizar en el estado
	 * detalle solicitud
	 * @param despachoForm DespachoMedicamentosForm
	 * @param mapping Mapping para manejar la navegaci贸n
	 * @param con Conexi贸n con la fuente de datos
	 * @return ActionForward  a la p谩gina "detalleDespachoMedicamentos.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionDetalleSolicitud(	DespachoMedicamentosForm despachoForm, 
													ActionMapping mapping,
													HttpServletRequest request,
													UsuarioBasico usuario,
													Connection con) throws SQLException, IPSException 
	{
		
	    DespachoMedicamentos mundo= new DespachoMedicamentos();
	    despachoForm.setCol(mundo.listadoMedicamentos(con,despachoForm.getNumeroSolicitud(), usuario.getCodigoInstitucionInt(), despachoForm.getCodigoArticulo()));
	   
	    if(UtilidadTexto.isEmpty(despachoForm.getOrdenAmb().trim()))
	    {
	    	despachoForm.setEsOrdenAmb(false);
	    }else{
	    	despachoForm.setEsOrdenAmb(true);
	    }
	    
	    despachoForm.setNumeroIngresos(0);      
		Iterator iterador=despachoForm.getCol().iterator();
		for(int i=0; i<despachoForm.getColSize(); i++)
		{
			HashMap fila=(HashMap)iterador.next();
			logger.info("---->"+fila.get("cantidadeq"));
			despachoForm.setDespachoMap("cantidad_"+i, fila.get("cantidad")+"");
			//String cantidad=fila.get("cantidad")+"";
			despachoForm.setDespachoMap("despacho_total_"+i, fila.get("despacho_total")+"");
			
			if(Utilidades.convertirAEntero((fila.get("despacho_total")+""))>0)
			{
				despachoForm.setEsDespachoParcial(true);
			}
			
			String sustituto=String.valueOf(fila.get("codigosustitutoprincipal"));
			if(!sustituto.equals("vacio"))
			{
				
				String[] sutitutos=sustituto.split("@");
				despachoForm.getMapaSustitutosBD().put(sutitutos[0]+"", sutitutos[1]+"");
			}
			else
			{
				Log4JManager.info( fila.get("------ codigo")+"");
				despachoForm.getMapaSustitutosBD().put(fila.get("codigo")+"", fila.get("codigo")+"");
			}
		}

		
	    /*valores por defecto*/
	    for(int i=1; i<=despachoForm.getColSize();i++)
	    {
	       despachoForm.setDespachoMap("parcial_"+i,"0");
	    }
	    
	    listarInsumosBD(con,despachoForm, true, new HashMap());
 
	    //Se Consulta los articulos (Principales - Sustitutos) que ya han sido insertados para restringir la b煤squeda en el tag
		despachoForm.setColeccionArticulosInsertados(mundo.articulosInsertados(con,despachoForm.getNumeroSolicitud()));
		
		despachoForm.setCodigoCentroCostoSolicitante(Solicitud.obtenerCodigoCentroCostoSolicitante(con, despachoForm.getNumeroSolicitud()+""));
		
		boolean validarCargar= mundo.infoSolicitud(con, despachoForm.getNumeroSolicitud(), usuario.getCodigoInstitucionInt(), despachoForm.getCodigoArticulo());
		
		if(validarCargar)
	    {
	        llenarForm(despachoForm,mundo); 
	        PersonaBasica persona= (PersonaBasica) request.getSession().getAttribute("pacienteActivo");
	        persona.setCodigoPersona(despachoForm.getCodigoPaciente());
	        persona.cargar(con,despachoForm.getCodigoPaciente());
	        persona.cargarPaciente(con, despachoForm.getCodigoPaciente(), usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
	        
	        ActionErrors errores = new ActionErrors();
	        
			//Tarea 3237 Soporte Suba
	        /*if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, persona.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
			{
				errores.add("errors.ingresoEstadoDiferenteAbierto", new ActionMessage("errors.ingresoEstadoDiferenteAbierto"));
			}
			if(UtilidadValidacion.esCuentaValida(con, persona.getCodigoCuenta())<1)
			{
				errores.add("errors.paciente.cuentaNoValida", new ActionMessage("errors.paciente.cuentaNoValida"));
			}*/
			
			if(errores.isEmpty())
			{
				UtilidadBD.closeConnection(con);
				validarConvenioCargoOrden(despachoForm, usuario);
				return mapping.findForward("paginaDetalle");
			}
			else
			{
				saveErrors(request, errores);
				return accionListarSolicitudesXCentroCosto(despachoForm, mapping, con, "listarSolicitudesXCentroCosto", usuario, persona);
			}
	    }
	    else
	    {
	        logger.warn("N鷐ero de solicitud inv醠ido "+despachoForm.getNumeroSolicitud());
            UtilidadBD.cerrarConexion(con);
			despachoForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
			ArrayList atributosError = new ArrayList();
			atributosError.add("N鷐ero de solicitud");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");
	    }
	}
	
	
	/**
	 * 
	 * @param despachoForm
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param paciente
	 * @param con
	 * @return
	 */
	private ActionForward accionImprimirPerfilFarmacoterapia (DespachoMedicamentosForm despachoForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente, Connection con){
		
		HashMap perfilFarmacoterapia = UtilidadesHistoriaClinica.obtenerPerfilFarmacoterapia(con, paciente.getCodigoIngreso());
		Vector archivosGenerados=new Vector();
		int numArchivos=0;
		String mes="";
		
		HashMap archivosGeneradosBirt=new HashMap();
        archivosGeneradosBirt.put("numRegistros", "0");
        
        for(int  i = 0 ; i < Integer.parseInt(perfilFarmacoterapia.get("num_meses")+"") ; i++)
		{
        	// Obtenemos el mes para el cual se va a generar el reporte
        	mes = perfilFarmacoterapia.get("meses").toString().split(ConstantesBD.separadorSplit)[i];
        	
        	String nombreRptDesign = "perfilDeFarmacoterapia.rptdesign";
			
			//EstadisticasServicios mundo = new EstadisticasServicios();
			
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			//String viaIngreso="";
			//String tipoPaciente="";
			Vector v;
			
			//***************** INFORMACI脫N DEL CABEZOTE
		    DesignEngineApi comp; 
		    comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"historiaClinica/",nombreRptDesign);
		    
		    // Logo
		    comp.insertImageHeaderOfMasterPage1(1, 2, ins.getLogoReportes());
		    
		    // Nombre Instituci贸n, titulo y rango de fechas
		    comp.insertGridHeaderOfMasterPageWithName(0,0,1,2, "titulo");
		    v=new Vector();
		    v.add(ins.getRazonSocial());
		    v.add("PERFIL DE FARMACOTERAPIA - "+mes);
		    comp.insertLabelInGridOfMasterPage(0,0,v);
		    
		    // Paciente, entidad, nro Ingreso, fecha ingreso
		    comp.insertGridHeaderOfMasterPageWithName(1,0,1,4, "info1");
		    v=new Vector();
		    v.add("Paciente: "+paciente.getNombrePersona());
		    v.add("Entidad: "+paciente.getConvenioPersonaResponsable());
		    v.add("Nro Ingreso: "+paciente.getConsecutivoIngreso());
		    v.add("Fecha Ingreso: "+paciente.getFechaIngreso());
		    comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,"left");
		    
		    // Id, sexo, edad, cama
		    comp.insertGridHeaderOfMasterPageWithName(1,1,1,4, "info2");
		    v=new Vector();
		    v.add("Nro. Doc: "+paciente.getTipoIdentificacionPersona(false)+" "+paciente.getNumeroIdentificacionPersona());
		    v.add("sexo: "+paciente.getSexo());
		    v.add("Edad: "+paciente.getEdadDetallada());
		    v.add("Cama: "+paciente.getCama());
		    comp.insertLabelInGridOfMasterPageWithProperties(1,1,v,"left");
		    
		    // Fecha hora de proceso y usuario
		    comp.insertLabelInGridPpalOfFooter(0,1,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
		    comp.insertLabelInGridPpalOfFooter(0,0,"Usuario: "+usuario.getLoginUsuario());
		    //******************
			
		    //***************** NUEVA CONSULTA DEL REPORTE
		    String consultaSQL = ConsultasBirt.perfilDeFarmacoterapia(paciente.getCodigoIngreso(), mes);
		   
		   //****************** MODIFICAR CONSULTA
		    comp.obtenerComponentesDataSet("dataSet");
		   //String newquery=comp.obtenerQueryDataSet();
		   comp.modificarQueryDataSet(consultaSQL);
		   
		   logger.info("Query >>> "+consultaSQL);
		   
		   //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
		   comp.updateJDBCParameters(newPathReport);        
		   
		   if(!newPathReport.equals(""))
		    {
			   archivosGeneradosBirt.put("isOpenReport_"+i, "true");
			   archivosGeneradosBirt.put("newPathReport_"+i, newPathReport);
			   numArchivos++;
		    }
		}
        
        archivosGeneradosBirt.put("numRegistros", numArchivos);
        request.setAttribute("archivos", archivosGenerados);
	    request.setAttribute("archivosBirt", archivosGeneradosBirt);
        request.setAttribute("nombreVentana", "hola");
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("abrirNPdfBirt");
	}
	
	/**
	 * Metodo para cargar los insumos presentes en la BD, 
	 * en un HashMap.
	 * @param con, Connection conexion con la fuente de datos.
	 * @param despachoForm, DespachoMedicamentosForm.
	 */
	private void listarInsumosBD(Connection con,DespachoMedicamentosForm despachoForm, boolean resetearDespachoParcialForm, HashMap despachoParcialMap )
	{
		DespachoMedicamentos mundo= new DespachoMedicamentos();
		despachoForm.setColeccionInsumos(mundo.infoInsumos(con,despachoForm.getNumeroSolicitud()));
		PdfReports report = new PdfReports();
		String[] data;
		int pos=2,reg=0;
		String[] dbColumns =  {
	      		"codigo",
	      		"descripcion", 
				"unidad_medida", 
				"cantidad",
				"codigointerfaz"
				};
		
		Iterator iterador=despachoForm.getColeccionInsumos().iterator();
		for(int i=0; i<despachoForm.getColeccionInsumos().size();i++)
		{
			HashMap fila=(HashMap)iterador.next();
			despachoForm.setDespachoMap("esSolicitado_"+i,fila.get("es_solicitado"));
			String tempoCantidad=fila.get("cantidad_solicitada")==null?null:fila.get("cantidad_solicitada")+"";
			despachoForm.setDespachoMap("cantidadInsumo_"+i,tempoCantidad);

			if(tempoCantidad==null || tempoCantidad.equals("") || tempoCantidad.equals("0") || tempoCantidad.equals("null"))
			{
				despachoForm.setDespachoMap("cantidadNuevaInsumo_"+i, "true");
				despachoForm.setDespachoMap("nuevo_"+i, true);
			}
			else
			{
				despachoForm.setDespachoMap("cantidadNuevaInsumo_"+i, "false");
			}
		}
		
		data = report.getDataFromCollection(dbColumns,despachoForm.getColeccionInsumos());
		for(int posHash=0;posHash<despachoForm.getColeccionInsumos().size();posHash++) 
		{
			despachoForm.setDespachoMap("articulo_"+posHash,(data[reg]+"")+"-"+(data[reg+1])+"");
			despachoForm.setDespachoMap("codigointerfaz_"+posHash,(data[reg+4])+"");
			reg=reg+dbColumns.length;
			despachoForm.setDespachoMap("unidadMedida_"+posHash,data[pos]);
			despachoForm.setDespachoMap("despachoTotal_"+posHash,data[pos+1]);
			//*******valor por defecto para el consumo int (0)********//
			if(resetearDespachoParcialForm)
				despachoForm.setDespachoMap("despacho_"+posHash,"0");
			else
				despachoForm.setDespachoMap("despacho_"+posHash,despachoParcialMap.get("despacho_"+posHash).toString());
			despachoForm.setDespachoMap("tipo_"+posHash,"BD");
			despachoForm.setNumeroIngresos(posHash+1);
			pos=pos+dbColumns.length;
		}
	}
	
	/**
	 * Este m茅todo especifica las acciones a realizar en el estado
	 * salir guardar.
	 * 
	 * @param despachoForm DespachoMedicamentosForm
	 * @param request HttpServletRequest para obtener 
	 * 					datos de la session 
	 * @param mapping Mapping para manejar la navegaci贸n
	 * @param con Conexi贸n con la fuente de datos
	 * 
	 * @return ActionForward "listadoSolicitudes"
	 * @throws SQLException
	*/
	private ActionForward accionSalirGuardar(DespachoMedicamentosForm despachoForm,
											 ActionMapping mapping,
											 HttpServletRequest request,
											 UsuarioBasico usuario,
											 Connection con) throws SQLException
	{
		//se debe validar por concurrencia que la solicitud este solicitada PENDIENTE
		Solicitud solicitud= new Solicitud();	
		solicitud.cargar(con, despachoForm.getNumeroSolicitud());
		
		if(solicitud.getEstadoHistoriaClinica().getCodigo()!=ConstantesBD.codigoEstadoHCSolicitada)
		{
			UtilidadBD.closeConnection(con);
			logger.warn("Error en el estado de la solicitud n鷐ero:"+despachoForm.getNumeroSolicitud()+ " estado HC -> "+solicitud.getEstadoHistoriaClinica());
			despachoForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
			ArrayList atributosError = new ArrayList();
			atributosError.add("Despachar");
			atributosError.add("Pendiente");
			atributosError.add("Solicitada");
			request.setAttribute("codigoDescripcionError", "error.solicitudgeneral.estados");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");
		}
		
		DespachoMedicamentos mundo=new DespachoMedicamentos ();  
		llenarMundo(despachoForm, mundo, usuario);
		boolean generarCargo=false;
		boolean actualizarPyP=false; 
		
		int datosCodigosArticulos[]= new int[despachoForm.getColSize()+1];
		int datosInsumosArticulo[]=new int[despachoForm.getNumeroIngresos()]; 
		int datosInsumosCantidad[]=new int[despachoForm.getNumeroIngresos()];
		int codigoEstadoMedico=0;
		int cantidadTemporal[]=new int[despachoForm.getColSize()+1];
		
		for(int w=1; w<=despachoForm.getColSize(); w++)
		{
		    cantidadTemporal[w]=Integer.parseInt(despachoForm.getDespachoMap("parcial_"+w)+"");
		}
		Vector cantidades=new Vector();
		for(int k=0; k<despachoForm.getNumeroIngresos(); k++)
		{
			String cantidad=(String)despachoForm.getDespachoMap("cantidadInsumo_"+k);
			String cantidadNueva=(String)despachoForm.getDespachoMap("cantidadNuevaInsumo_"+k);
			String codigoArticulo= String.valueOf(despachoForm.getDespachoMap("articulo_"+k)).split("-")[0];
			
			logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			logger.info("ARTICULO: -->"+codigoArticulo+"--CANTIDAD ARTICULO:"+cantidadNueva+"--CANTIDAD NUEVA:"+cantidad);
			logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			
			if(cantidad!=null && cantidadNueva!=null)
			{
				Vector tempo=new Vector();
				try
				{
					tempo.add(new Integer(codigoArticulo));
					tempo.add(new Integer(cantidad));
					cantidades.add(tempo);
				}
				catch (NumberFormatException e)
				{
					logger.error("Error al recuperar las cantidades de los articulos "+e);
				}
			}
		}
		
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if(despachoForm.getRadioPendiente().equals("radioPendiente"))
		{
			codigoEstadoMedico=ConstantesBD.codigoEstadoHCSolicitada;
			for(int k=0; k<despachoForm.getColSize(); k++)
			{
				String cantidad= String.valueOf(despachoForm.getDespachoMap("cantidad_"+k));
				cantidad=cantidad.trim();
				if(cantidad==null)
				{
					cantidad="";
				}
			}

		}
		else if(despachoForm.getRadioFinal().equals("radioFinal") )
		{
			// en caso de que sea mezclas no se valida porque la cantidad viene en cero 
			if(UtilidadTexto.isEmpty(despachoForm.getMezclaOrdenDieta()))
			{	
				codigoEstadoMedico=ConstantesBD.codigoEstadoHCDespachada;
				
				for(int k=0; k<despachoForm.getColSize(); k++)
				{
					//String despachoInsumos=String.valueOf(despachoForm.getDespachoMap("despacho_"+k));
					//String despachoParcial=String.valueOf(despachoForm.getDespachoMap("parcial_"+(k+1)));
					//String insumo= String.valueOf(despachoForm.getDespachoMap("articulo_"+k));
					String cantidad= String.valueOf(despachoForm.getDespachoMap("cantidad_"+k));
					//String cantidadNueva= String.valueOf(despachoForm.getDespachoMap("cantidadNueva_"+k));
					//String despachoTotal= String.valueOf(despachoForm.getDespachoMap("despacho_total_"+k));
					cantidad=cantidad.trim();
					String sustituto= String.valueOf(despachoForm.getDespachoMap("sustituto_"+k));
					if(cantidad!=null && !cantidad.equals(""))
					{
						if(sustituto==null)
						{
							ActionErrors errores = new ActionErrors();
							try
							{
								if(Integer.parseInt(cantidad)<1)
								{
									errores.add("numeroNoEntero", new ActionMessage("errors.required", "La cantidad del Medicamento"));
									saveErrors(request, errores);	
									UtilidadBD.cerrarConexion(con);									
									return mapping.findForward("paginaDetalle");
								}
							}
							catch (NumberFormatException e)
							{
								errores.add("numeroNoEntero", new ActionMessage("errors.integer", "La cantidad del Medicamento"));
								saveErrors(request, errores);	
								UtilidadBD.cerrarConexion(con);									
								return mapping.findForward("paginaDetalle");
							}
						}
					}
					else
					{
						ActionErrors errores = new ActionErrors();
						errores.add("numeroNoEntero", new ActionMessage("errors.required", "La cantidad del Medicamento "+despachoForm.getCodigosArticulosMap("articulo_"+k)));
						logger.warn("entra al error de formato");
						saveErrors(request, errores);	
						UtilidadBD.cerrarConexion(con);									
						return mapping.findForward("paginaDetalle");
					}
				}	
			}
		}
		
		Log4JManager.info(despachoForm.getEsDirecto());
		Log4JManager.info(despachoForm.getCheckDirecto());
		
		if(despachoForm.getEsDirecto() == true || DespachoMedicamentos.existeDespachocConEntregaDirPac(con, despachoForm.getNumeroSolicitud()))
		{
			if(despachoForm.getRadioFinal().equals("radioFinal"))
			{	
				codigoEstadoMedico=ConstantesBD.codigoEstadoHCAdministrada;
				generarCargo=true;
				actualizarPyP=true;
			}	
		}
		
		
		if(!UtilidadTexto.isEmpty(despachoForm.getMezclaOrdenDieta()))
		{
			codigoEstadoMedico=ConstantesBD.codigoEstadoHCAdministrada;
			generarCargo=true;
		}
		
		Utilidades.imprimirMapa(despachoForm.getDespachoMap());
		
		for(int u=0; u<despachoForm.getNumeroIngresos();u++)
		{
		    try
		    {
		       String temp[]= String.valueOf(despachoForm.getDespachoMap("articulo_"+u)).split("-");
		       datosInsumosArticulo[u]=Integer.parseInt(temp[0]);
		       String despacho=despachoForm.getDespachoMap("despacho_"+u)+"";
		       if(!util.UtilidadCadena.noEsVacio(despacho))
		       {
		    	   despacho="0";
		    	   despachoForm.setDespachoMap("despacho_"+u, despacho);
		       }
		       datosInsumosCantidad[u]=Integer.parseInt(despacho);
		    }
		    catch(Exception e)
		    {
		    	logger.warn("Error en los insumos del despacho de medicamentos " +e.toString());
		    }
		}
		
		int j=0;
		int temp=0;
		for(int i=1; i<=despachoForm.getColSize(); i++)
		{
			
			try
		    {
				if(despachoForm.getDespachoMap("parcial_"+i)==null || despachoForm.getDespachoMap("parcial_"+i).toString().equals(""))
				{
					ActionErrors errores = new ActionErrors();
					errores.add("numeroNoEntero", new ActionMessage("errors.integerMayorIgualQue", "El despacho del Medicamento para el articulo "+despachoForm.getCodigosArticulosMap("articulo_"+i), "0"));
					logger.warn("entra al error de formato");
					saveErrors(request, errores);	
					UtilidadBD.cerrarConexion(con);									
					return mapping.findForward("paginaDetalle");
				}
					
				else
				{	
					temp=Integer.parseInt(despachoForm.getDespachoMap("parcial_"+i)+"");
					
					if(temp<0)
					{
						ActionErrors errores = new ActionErrors();
						errores.add("numeroNoEntero", new ActionMessage("errors.integerMayorIgualQue", "El despacho del Medicamento para el articulo "+despachoForm.getCodigosArticulosMap("articulo_"+i), "0"));
						logger.warn("entra al error de formato");
						saveErrors(request, errores);	
						UtilidadBD.cerrarConexion(con);									
						return mapping.findForward("paginaDetalle");
					}
				}
				if(despachoForm.getCodigosArticulosMap("articulo_"+i)==null || despachoForm.getCodigosArticulosMap("articulo_"+i).toString().equals(""))
				{
					logger.warn("Error cargando los codigos de los art铆culos en BD, con la solicitud n煤mero:"+despachoForm.getNumeroSolicitud());
					UtilidadBD.cerrarConexion(con);
					despachoForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
					ArrayList atributosError = new ArrayList();
					atributosError.add("Codigos Articulos");
					request.setAttribute("codigoDescripcionError", "errors.invalid");				
					request.setAttribute("atributosError", atributosError);
					return mapping.findForward("paginaError");
				}
				else
				{
					datosCodigosArticulos[j]=Integer.parseInt(despachoForm.getCodigosArticulosMap("articulo_"+i)+"");
				}
				j++;

		    }
			catch (NumberFormatException e)
	       {	
				ActionErrors errores = new ActionErrors();
				errores.add("numeroNoEntero", new ActionMessage("errors.integer", "El despacho del Medicamento para el articulo "+despachoForm.getCodigosArticulosMap("articulo_"+i)));
				logger.warn("entra al error de formato");
				saveErrors(request, errores);	
				UtilidadBD.cerrarConexion(con);									
				return mapping.findForward("paginaDetalle");
	       	}
		}
		
		
		for(int k=0; k<datosCodigosArticulos.length; k++)
		{
			String cantidad=(String)despachoForm.getDespachoMap("cantidad_"+k);
			String cantidadNueva=(String)despachoForm.getDespachoMap("cantidadNueva_"+k);
			if(cantidad!=null && cantidadNueva!=null && !cantidad.equals("null") && !cantidadNueva.equals("null"))
			{
				Vector tempo=new Vector();
				try
				{
					tempo.add(new Integer(datosCodigosArticulos[k]));
					tempo.add(new Integer(cantidad));
					cantidades.add(tempo);
				}
				catch (NumberFormatException e)
				{
					logger.error("Error al recuperar las cantidades de los articulos (Insumos)"+e);
				}
			}
		}

		
		PersonaBasica paciente= (PersonaBasica) request.getSession().getAttribute("pacienteActivo");
		mundo.setCodigoFarmacia(despachoForm.getCodigoCentroCostoSolicitado());
		mundo.setFechaSolicitud(despachoForm.getFechaSolicitud());
		mundo.setHoraSoliciutd(despachoForm.getHoraSoliciutd());
		int validar=0;
		try 
		{
			validar=mundo.insertarDespachoBasicoYDetalle(		con,
																despachoForm.getColeccionNumerosSustitutos(), 
																despachoForm.getTamanoCollectionOriginal(), 
																datosCodigosArticulos, 
																cantidades, 
																datosInsumosArticulo, 
																datosInsumosCantidad, 
																codigoEstadoMedico,
																/*despachoForm.getNumeroAutorizacion(),*/
																despachoForm.getEsDirecto(),
																generarCargo,
																usuario,
																paciente, ConstantesBD.inicioTransaccion,
																despachoForm.getMezclaOrdenDieta(),
																actualizarPyP,
																despachoForm.getRadioFinal(),
																despachoForm.getDespachoMap(),
																despachoForm.getNombrePersonaRecibe(),despachoForm.getMapaSustitutosBD());
		}
		catch(Exception e)
		{
			logger.error("se presento una excepcion en DespachoMedicamentosAction linea 840 --> "+e);
		}
		if (validar <1)
		{
			logger.warn("Error en la transacci髇 de Inserci髇 en BD, con la solicitud n鷐ero:"+despachoForm.getNumeroSolicitud());
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
			UtilidadBD.cerrarConexion(con);
			despachoForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
			ArrayList atributosError = new ArrayList();
			atributosError.add("N鷐ero de solicitud");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");
		}
		else
		{
			despachoForm.setCodigoDespacho(validar);
			
			//SE ACTUALIZA LA INFORMCION DE INTERFAZ INVENTARIOS SHAIO
            ActionForward forward;
			try 
			{
				forward = this.actualizarInformacionInterfaz(con, despachoForm, usuario, request, mapping);
				if(forward!=null)
	            	return forward;
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			//ANTES DE ACTUALIZAR LAS EXISTENCIAS DE ARTICULOS X ALMACEN SE EVALUAN LOS CAMBIOS SOBRE BD PARA NO TENER PROBLEMAS DE CONCURRENCIA
			if(!AlmacenParametros.manejaExistenciasNegativas(con, despachoForm.getCodigoCentroCostoSolicitado(), usuario.getCodigoInstitucionInt()))
	        {
				ActionErrors errores= new ActionErrors();
				//PARTE DE MEDICAMENTOS  
		    	for (int i=0; i<despachoForm.getColSize(); i ++)
		        {
		    		//EL BLOQUEO SE HACE EN EL MOMENTO DE INSERTAR EN EL SQL BASE
		    		/*
		    		ArrayList filtro=new ArrayList();
			        filtro.add(despachoForm.getCodigosArticulosMap("articulo_"+(i+1)).toString());
			        filtro.add(despachoForm.getCodigoCentroCostoSolicitado()+"");
			        UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloquearArticulosAlmacen,filtro);*/
		    		String despachoParcial=String.valueOf(despachoForm.getDespachoMap("parcial_"+(i+1)));
		    		
		    		if(Integer.parseInt(despachoParcial)>0)
		    		{	
				        if(!Articulo.articuloManejaLote(con, Integer.parseInt(despachoForm.getCodigosArticulosMap("articulo_"+(i+1)).toString()), usuario.getCodigoInstitucionInt()))
	                	{
					        int exArticulo=Integer.parseInt(UtilidadInventarios.getExistenciasXArticulo(con, Integer.parseInt(despachoForm.getCodigosArticulosMap("articulo_"+(i+1)).toString()),despachoForm.getCodigoCentroCostoSolicitado(),usuario.getCodigoInstitucionInt())+"");
				    		if(Integer.parseInt(despachoParcial)>exArticulo)
				    		{
				    			errores.add("error.inventarios.existenciasInsuficientes", 
				    						new ActionMessage("error.inventarios.existenciasInsuficientes", 
				    						despachoForm.getCodigosArticulosMap("articulo_"+(i+1)).toString(), 
			                        		exArticulo+"", 
			                        		despachoForm.getNombreCentroCostoSolicitado() ));
				    			logger.info("1 existencias--->"+exArticulo);
				    		}
	                	}
				        else
				        {
				        	if(Integer.parseInt(despachoParcial)>Integer.parseInt(despachoForm.getDespachoMap("existenciaXLote_"+(i+1))+""))
		                    {
		                        errores.add("error.inventarios.existenciasInsuficientesLote", 
		                                new ActionMessage("error.inventarios.existenciasInsuficientesLote", 
		                                							despachoForm.getCodigosArticulosMap("articulo_"+(i+1)).toString(), 
		                                							despachoForm.getDespachoMap("existenciaXLote_"+(i+1)).toString(),
		                                							despachoForm.getNombreCentroCostoSolicitado(),
		                                							despachoForm.getDespachoMap("lote_"+(i+1)).toString()));
		                        logger.info("2 existencias--->"+despachoForm.getDespachoMap("existenciaXLote_"+(i+1)));
		                    }
				        }
		    		}    
		        }
		    	//PARTE DE INSUMOS
		    	for(int k=0; k < despachoForm.getNumeroIngresos(); k++)
                {
		    		//EL BLOQUEO SE HACE EN EL MOMENTO DE INSERTAR EN EL SQL BASE
                    /*
                    ArrayList filtro=new ArrayList();
			        filtro.add((despachoForm.getDespachoMap("articulo_"+k)+"").split("-")[0]);
			        filtro.add(despachoForm.getCodigoCentroCostoSolicitado()+"");
			        UtilidadBD.bloquearRegistro(con,BloqueosConcurrencia.bloquearArticulosAlmacen,filtro);*/
		    		
		    		String despachoInsumos=String.valueOf(despachoForm.getDespachoMap("despacho_"+k));
		    		if(Integer.parseInt(despachoInsumos)>0)
		    		{
			    		if(!Articulo.articuloManejaLote(con, Integer.parseInt(String.valueOf(despachoForm.getDespachoMap("articulo_"+k)).split("-")[0]), usuario.getCodigoInstitucionInt()))
	                	{
					        int exArticulo=Integer.parseInt(UtilidadInventarios.getExistenciasXArticulo(con, Integer.parseInt((despachoForm.getDespachoMap("articulo_"+k)+"").split("-")[0]),despachoForm.getCodigoCentroCostoSolicitado(),usuario.getCodigoInstitucionInt())+"");
					        if(Integer.parseInt(despachoInsumos)>exArticulo)
				    		{
				    			errores.add("error.inventarios.existenciasInsuficientes", 
				    						new ActionMessage("error.inventarios.existenciasInsuficientes", 
				    						String.valueOf(despachoForm.getDespachoMap("articulo_"+k)).split("-")[0], 
			                        		exArticulo+"", 
			                        		despachoForm.getNombreCentroCostoSolicitado() ));
				    			logger.info("3 e3xistencias->"+exArticulo);
				    		}
	                	}
				        else
				        {
				        	if(Integer.parseInt(despachoInsumos)>Integer.parseInt(despachoForm.getDespachoMap("existenciaXLoteInsumo_"+k)+""))
		                    {
		                        errores.add("error.inventarios.existenciasInsuficientesLote", 
		                                new ActionMessage("error.inventarios.existenciasInsuficientesLote", 
		                                							String.valueOf(despachoForm.getDespachoMap("articulo_"+k)).split("-")[0], 
		                                							despachoForm.getDespachoMap("existenciaXLoteInsumo_"+k).toString(),
		                                							despachoForm.getNombreCentroCostoSolicitado(),
		                                							despachoForm.getDespachoMap("loteInsumo_"+k).toString()));
		                        logger.info("4 e3xistencias->"+despachoForm.getDespachoMap("existenciaXLoteInsumo_"+k));
		                    }
				        }
		    		}	
			    }
		    	if(!errores.isEmpty())
		    	{
		    		saveErrors(request,errores);
		    		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		    		UtilidadBD.closeConnection(con);
		    		return mapping.findForward("paginaDetalle");
		    	}
	        }
			
			//SE ACTUALIZAN LAS EXISTENCIAS DE ARTICULOS X ALMACEN
            boolean inserto=this.actualizarExistenciasArticulosAlmacen(con, despachoForm, usuario.getCodigoInstitucionInt());
            if(!inserto)
            {
                DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
                logger.warn("Error en el resumen del Despacho Medicamentos con la solicitud n煤mero:"+despachoForm.getNumeroSolicitud());
                UtilidadBD.cerrarConexion(con);
                despachoForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
                ArrayList atributosError = new ArrayList();
                atributosError.add("N煤mero de solicitud");
                request.setAttribute("codigoDescripcionError", "errors.invalid");               
                request.setAttribute("atributosError", atributosError);
                return mapping.findForward("paginaError");
            }
			
		    ///validacion para guardar sobrantes de multidosis
            if(!despachoForm.getEsDirecto()&&despachoForm.isEntidadControlaDespachoSaldoMultidosis())
            {
            	Iterator iterador=despachoForm.getCol().iterator();
	    		for(int i=0; i<despachoForm.getColSize(); i++)
	    		{
	    			HashMap fila=(HashMap)iterador.next();
	    			
	    			if(Articulo.esArticuloMultidosis(con,Utilidades.convertirAEntero(fila.get("codigo")+""))&&Utilidades.convertirADouble(despachoForm.getDespachoMap("saldodosisdespachada_"+(i+1))+"")>0)
	    			{
	    				//si tiene unidad de medida de unidosis guarda el saldo.
	    				if(!UtilidadTexto.isEmpty(fila.get("unidadmedidaunidosis")+""))
	    					mundo.actualizarDosisSobrantes(con,despachoForm.getCodigoCentroCostoSolicitante(),Utilidades.convertirAEntero(fila.get("codigo")+""),fila.get("unidadmedidaunidosis")+"",Utilidades.convertirADouble(despachoForm.getDespachoMap("saldodosisdespachada_"+(i+1))+""),false);
	    			}
	    		}
            }
            
            ///validacion para guardar sobrantes de multidosis
            if(despachoForm.getRadioFinal().equals("radioFinal")&&UtilidadTexto.isEmpty(despachoForm.getRadioPendiente())&&!despachoForm.getEsDirecto()&&despachoForm.isEntidadControlaDespachoSaldoMultidosis())
            {
	    		Iterator iterador=despachoForm.getCol().iterator();
	    		for(int i=0; i<despachoForm.getColSize(); i++)
	    		{
	    			HashMap fila=(HashMap)iterador.next();
	    			
	    			if(Articulo.esArticuloMultidosis(con,Utilidades.convertirAEntero(fila.get("codigo")+"")))
	    			{
		    			//tomando el numero de dosis(no la cantidad por dosis.) que se deben administrar.
		    			double numeroDosis=Utilidades.calculoNroDosisTotal(fila.get("tipofrecuencia")+"",fila.get("diastratamiento")+"",Utilidades.convertirAEntero(fila.get("frecuencia")+""),true);
		    			
		    			//calculando el total de dosis solicitado (cantidad_por_dosis * numero_dosis)
		    			double cantidadTotalDosisSolicitada=Utilidades.convertirADouble(fila.get("dosis")+"")*numeroDosis;
		    			
		    			//obteniendo el total despachado( sumar el despacho_total acumulado + el despacho parcial).
		    			int cantidadTotalDespachada=Utilidades.convertirAEntero(fila.get("despacho_total")+"")+Utilidades.convertirAEntero(despachoForm.getDespachoMap("parcial_"+(i+1))+"");
		    			
		    			//obteniendo el total de dosis despachado.
		    			double cantidadTotalDosisDespachada=cantidadTotalDespachada*Utilidades.convertirADouble(fila.get("cantidadunidosis")+"",true)+Utilidades.convertirADouble(fila.get("totaldespachosaldos")+"",true)+Utilidades.convertirADouble(despachoForm.getDespachoMap("saldodosisdespachada_"+(i+1))+"",true);
		    			
		    			if((cantidadTotalDosisDespachada-cantidadTotalDosisSolicitada)>0)
		    			{
		    				if(!UtilidadTexto.isEmpty(fila.get("unidadmedidaunidosis")+""))
		    					mundo.actualizarDosisSobrantes(con,despachoForm.getCodigoCentroCostoSolicitante(),Utilidades.convertirAEntero(fila.get("codigo")+""),fila.get("unidadmedidaunidosis")+"",(cantidadTotalDosisDespachada-cantidadTotalDosisSolicitada),true);
		    			}
	    			}
		        }
            }
            
            Utilidades.imprimirMapa(despachoForm.getDespachoMap());
            
            boolean valida=mundo.infoSolicitud(con,despachoForm.getNumeroSolicitud(), usuario.getCodigoInstitucionInt(), despachoForm.getCodigoArticulo());
			if(valida)
			{	
				despachoForm.setCol(mundo.listadoMedicamentos(con,despachoForm.getNumeroSolicitud(), usuario.getCodigoInstitucionInt(), despachoForm.getCodigoArticulo()));
				listarInsumosBD(con,despachoForm, false, (HashMap)despachoForm.getDespachoMap().clone());
				llenarForm(despachoForm,mundo);
                myFactory.endTransaction(con);
                this.cargarMensajesExistenciasNegativas(despachoForm, usuario.getCodigoInstitucionInt());
                this.cargarMensajesStockMaximoMinimo(despachoForm, usuario.getCodigoInstitucionInt());
				UtilidadBD.cerrarConexion(con);		
				logger.info("despachoMap-->"+despachoForm.getDespachoMap());
				
				/* Guarda el registro de entrega. Asocia el despacho a un registro de entrega *  */
				guardarRegistroEntrega(usuario, paciente, despachoForm);
				//-----------------------------------------------------------------------------------
				
				return mapping.findForward("resumenSol");
			}
			else
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
				logger.warn("Error en el resumen del Despacho Medicamentos con la solicitud n煤mero:"+despachoForm.getNumeroSolicitud());
				UtilidadBD.cerrarConexion(con);
				despachoForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
				ArrayList atributosError = new ArrayList();
				atributosError.add("N鷐ero de solicitud");
				request.setAttribute("codigoDescripcionError", "errors.invalid");				
				request.setAttribute("atributosError", atributosError);
				return mapping.findForward("paginaError");
			}    
		}
	}
	
	
	/**
	 * 
	 * @param con
	 * @param despachoForm
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 * @throws Exception 
	 */
	private ActionForward actualizarInformacionInterfaz(Connection con, DespachoMedicamentosForm despachoForm, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) throws Exception 
	{
		if(despachoForm.isInterfazCompras())
		{	
			logger.info("\n\n VA HA ACTUALIZAR LA INFORMACION DE INTERFAZ INVENTARIOS");
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			/* keys( codigosArt_, 
					 * 		cantidadADespachar_, 
					 * 		tipodespacho_, 
					 * 		proveedorCompra_, 
					 * 		almacenConsignacion_, 
					 * 		lote_, 
					 * 		fechavencimiento_, 
					 * 		manejaLoteArticulo_, 
					 * 		proveedorCatalogo_)*/
			
			//agrupamos toda la informacion de los insumos y medicamentos en un mapa unico
			HashMap mapa= new HashMap();
			int pos=0;
			for(int k=0; k<despachoForm.getNumeroIngresos(); k++)
			{
	            mapa.put("codigosArt_"+pos, despachoForm.getDespachoMap("articulo_"+k).toString().split("-")[0]);
	            mapa.put("cantidadADespachar_"+pos, despachoForm.getDespachoMap("despacho_"+k).toString());
	            if(despachoForm.getDespachoMap().containsKey("tipodespachoInsumo_"+k))
	            	mapa.put("tipodespacho_"+pos, despachoForm.getDespachoMap("tipodespachoInsumo_"+k).toString());
	            if(despachoForm.getDespachoMap().containsKey("proveedorCompraInsumo_"+k))
	            	mapa.put("proveedorCompra_"+pos, despachoForm.getDespachoMap("proveedorCompraInsumo_"+k).toString());
	            if(despachoForm.getDespachoMap().containsKey("almacenConsignacionInsumo_"+k))
	            	mapa.put("almacenConsignacion_"+pos, despachoForm.getDespachoMap("almacenConsignacionInsumo_"+k).toString());
	            if(!UtilidadTexto.isEmpty(despachoForm.getDespachoMap("loteInsumo_"+k).toString()))
	            	mapa.put("lote_"+pos, despachoForm.getDespachoMap("loteInsumo_"+k).toString());
	            else
	            	mapa.put("lote_"+pos,"");
	            if(!UtilidadTexto.isEmpty(despachoForm.getDespachoMap("fechaVencimientoLoteInsumo_"+k).toString()))
	            	mapa.put("fechavencimiento_"+pos, despachoForm.getDespachoMap("fechaVencimientoLoteInsumo_"+k).toString());
	            else
	            	mapa.put("fechavencimiento_"+pos,"");
	            if(Articulo.articuloManejaLote(con, Integer.parseInt(despachoForm.getDespachoMap("articulo_"+k).toString().split("-")[0]), usuario.getCodigoInstitucionInt()))
	            	mapa.put("manejaLoteArticulo_"+pos, "true");
	            else
	            	mapa.put("manejaLoteArticulo_"+pos, "false");
	            if(despachoForm.getDespachoMap().containsKey("proveedorCatalogoInsumo_"+k))
	            	mapa.put("proveedorCatalogo_"+pos, despachoForm.getDespachoMap("proveedorCatalogoInsumo_"+k).toString());
	            
	            mapa.put("ccsolicita_"+pos, despachoForm.getCodigoCentroCostoSolicitante());
	            mapa.put("ccejecuta_"+pos, despachoForm.getCodigoCentroCostoSolicitado());
	            
	            pos++;
			}
			
			for(int k=0; k<despachoForm.getColSize(); k++)
			{
	            mapa.put("codigosArt_"+pos, despachoForm.getCodigosArticulosMap().get("articulo_"+(k+1)).toString().split("-")[0]);
	            mapa.put("cantidadADespachar_"+pos, despachoForm.getDespachoMap("parcial_"+(k+1)).toString());
	            if(despachoForm.getDespachoMap().containsKey("tipodespacho_"+(k+1)))
	            	mapa.put("tipodespacho_"+pos, despachoForm.getDespachoMap("tipodespacho_"+(k+1)).toString());
	            if(despachoForm.getDespachoMap().containsKey("proveedorCompra_"+(k+1)))
	            	mapa.put("proveedorCompra_"+pos, despachoForm.getDespachoMap("proveedorCompra_"+(k+1)).toString());
	            if(despachoForm.getDespachoMap().containsKey("almacenConsignacion_"+(k+1)))
	            	mapa.put("almacenConsignacion_"+pos, despachoForm.getDespachoMap("almacenConsignacion_"+(k+1)).toString());
	            if(!UtilidadTexto.isEmpty(despachoForm.getDespachoMap("lote_"+(k+1)).toString()))
	            	mapa.put("lote_"+pos, despachoForm.getDespachoMap("lote_"+(k+1)).toString());
	            else
	            	mapa.put("lote_"+pos,"");
	            if(!UtilidadTexto.isEmpty(despachoForm.getDespachoMap("fechaVencimientoLote_"+(k+1)).toString()))
	            	mapa.put("fechavencimiento_"+pos, despachoForm.getDespachoMap("fechaVencimientoLote_"+(k+1)).toString());
	            else
	            	mapa.put("fechavencimiento_"+pos,"");
	            if(Articulo.articuloManejaLote(con, Integer.parseInt(despachoForm.getCodigosArticulosMap().get("articulo_"+(k+1)).toString().split("-")[0]), usuario.getCodigoInstitucionInt()))
	            	mapa.put("manejaLoteArticulo_"+pos, "true");
	            else
	            	mapa.put("manejaLoteArticulo_"+pos, "false");
	            if(despachoForm.getDespachoMap().containsKey("proveedorCatalogo_"+(k+1)))
	            	mapa.put("proveedorCatalogo_"+pos, despachoForm.getDespachoMap("proveedorCatalogo_"+(k+1)).toString());
	            
	            mapa.put("ccsolicita_"+pos, despachoForm.getCodigoCentroCostoSolicitante());
	            mapa.put("ccejecuta_"+pos, despachoForm.getCodigoCentroCostoSolicitado());
	            
	            pos++;
			}    
			
			mapa.put("numRegistros", pos);
			logger.info("MAPA INSERCION INTERFAZ-->"+mapa);
			
			ElementoApResource elem= UtilidadBDInterfaz.actualizarInterfazInventarios(con, mapa, usuario,paciente,despachoForm.getMensajesAdvertenciaMap());
			
	        if(elem!=null)
	        {
	        	UtilidadBD.abortarTransaccion(con);
	            logger.warn("Error en el Despacho Medicamentos con la solicitud n煤mero:"+despachoForm.getNumeroSolicitud());
	            UtilidadBD.closeConnection(con);
	            ActionErrors errores= new ActionErrors();
	            if(elem.getAtributosArrayList().size()==0)
	            	errores.add(elem.getLlave(), new ActionMessage(elem.getLlave()));
	            else if(elem.getAtributosArrayList().size()==1)	
	            	errores.add(elem.getLlave(), new ActionMessage(elem.getLlave(), elem.getAtributo(0).toString()));
	            else if(elem.getAtributosArrayList().size()==2)	
	            	errores.add(elem.getLlave(), new ActionMessage(elem.getLlave(), elem.getAtributo(0).toString(), elem.getAtributo(1).toString()));
	            else if(elem.getAtributosArrayList().size()==3)	
	            	errores.add(elem.getLlave(), new ActionMessage(elem.getLlave(), elem.getAtributo(0).toString(), elem.getAtributo(1).toString(), elem.getAtributo(2).toString()));
	            saveErrors(request,errores);
	            return mapping.findForward("paginaDetalle");
	        }
		}
		
		return null;
	}

	/**
     * metodo que actualiza las existencias articulos almacen 
     * @param con
     * @param despachoForm
     * @return
     */
    private boolean actualizarExistenciasArticulosAlmacen (Connection con, DespachoMedicamentosForm despachoForm, int codigoInstitucion)
    {
        boolean insertoBien= false;
        
        //actualizacion de los medicamentos
        for(int k=0; k < despachoForm.getColSize(); k++)
        {   
            try
            {
            	if(Integer.parseInt(String.valueOf(despachoForm.getDespachoMap("parcial_"+(k+1))))>0)
	    		{
	            	if(!Articulo.articuloManejaLote(con, Integer.parseInt(despachoForm.getCodigosArticulosMap("articulo_"+(k+1)).toString()), codigoInstitucion))
	            	{
		                insertoBien=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(  con, 
		                                                                                                    Integer.parseInt(despachoForm.getCodigosArticulosMap("articulo_"+(k+1)).toString()) , 
		                                                                                                    despachoForm.getCodigoCentroCostoSolicitado(), 
		                                                                                                    false, 
		                                                                                                    Integer.parseInt(String.valueOf(despachoForm.getDespachoMap("parcial_"+(k+1)))), 
		                                                                                                    codigoInstitucion, 
		                                                                                                    ConstantesBD.continuarTransaccion );
		            } 
	            	else
	            	{
	            		insertoBien=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(  con, 
	            				Integer.parseInt(despachoForm.getCodigosArticulosMap("articulo_"+(k+1)).toString()) ,  
	                            despachoForm.getCodigoCentroCostoSolicitado(), 
	                            false, 
	                            Integer.parseInt(String.valueOf(despachoForm.getDespachoMap("parcial_"+(k+1)))), 
	                            codigoInstitucion, 
	                            ConstantesBD.continuarTransaccion,
	                            despachoForm.getDespachoMap("lote_"+(k+1)).toString(),
	                            UtilidadFecha.conversionFormatoFechaABD(despachoForm.getDespachoMap("fechaVencimientoLote_"+(k+1)).toString())
	                            );
	            	}
	            	if(!insertoBien)
	                {    
	                    logger.warn("Error en el insertar el valor existencias del articulo==="+despachoForm.getCodigosArticulosMap("articulo_"+(k+1)));
	                    return false;
	                }
	    		}	
            }  
            catch(NumberFormatException e)
            {
                logger.warn("Error en el parseInt del codigo del articulo con indice ="+k +"   error-->"+e);
                return false;
            }
            catch(SQLException sqle)
            {
                logger.warn("Error en el insert del valor existencias articulos solicitud con indice ="+k +"   error-->"+sqle);
                return false;
            }
        }
        
        //actualizacion de los insumos
        for(int k=0; k < despachoForm.getNumeroIngresos(); k++)
        {
            try
            {
            	if(Integer.parseInt(String.valueOf(despachoForm.getDespachoMap("despacho_"+k)))>0)
            	{	
	            	if(!Articulo.articuloManejaLote(con, Integer.parseInt(despachoForm.getDespachoMap("articulo_"+k).toString().split("-")[0]), codigoInstitucion))
	            	{
		                insertoBien=UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(  con, 
		                                                                                                    Integer.parseInt(despachoForm.getDespachoMap("articulo_"+k).toString().split("-")[0]) , 
		                                                                                                    despachoForm.getCodigoCentroCostoSolicitado(), 
		                                                                                                    false, 
		                                                                                                    Integer.parseInt(String.valueOf(despachoForm.getDespachoMap("despacho_"+k))), 
		                                                                                                    codigoInstitucion, 
		                                                                                                    ConstantesBD.continuarTransaccion );
	            	}
	            	else
	            	{
	            		insertoBien=UtilidadInventarios.actualizarExistenciasArticuloAlmacenLoteTransaccional(  con, 
	            				Integer.parseInt(despachoForm.getDespachoMap("articulo_"+k).toString().split("-")[0]) ,  
	                            despachoForm.getCodigoCentroCostoSolicitado(), 
	                            false, 
	                            Integer.parseInt(String.valueOf(despachoForm.getDespachoMap("despacho_"+k))), 
	                            codigoInstitucion, 
	                            ConstantesBD.continuarTransaccion,
	                            despachoForm.getDespachoMap("loteInsumo_"+k).toString(),
	                            UtilidadFecha.conversionFormatoFechaABD(despachoForm.getDespachoMap("fechaVencimientoLoteInsumo_"+k).toString())
	                            );
	            	}
	                if(!insertoBien)
	                {    
	                    logger.warn("Error en el insertar el valor existencias del articulo==="+despachoForm.getDespachoMap("articulo_"+k).toString().split("-")[0]);
	                    return false;
	                }
            	}    
            }  
            catch(NumberFormatException e)
            {
                logger.warn("Error en el parseInt del codigo del articulo con indice ="+k +"   error-->"+e);
                return false;
            }
            catch(SQLException sqle)
            {
                logger.warn("Error en el insert del valor existencias articulos solicitud con indice ="+k +"   error-->"+sqle);
                return false;
            }
        }
        return true;
    }
    
    
     /**
     * metodo que carga el mapa para mostrar los mensajes con las existencias negativas
     * @param cargosForm
     * @return
     */
    private boolean cargarMensajesExistenciasNegativas (DespachoMedicamentosForm despachoForm, int codigoInstitucion)
    {
        int numeroExistencias= ConstantesBD.codigoNuncaValido;
        int centinela=0;
        int numeroregistros=0;
        String codigosArticulos="";
        String valoresExistenciasXArticulo="";
        if(AlmacenParametros.manejaExistenciasNegativas(despachoForm.getCodigoCentroCostoSolicitado(), codigoInstitucion))
        {
            //parte de medicamentos
            for(int k=0; k < despachoForm.getColSize(); k++)
            {  
                try
                {
                	
                	 Utilidades.imprimirMapa(despachoForm.getCodigosArticulosMap());
                 	if (despachoForm.getCodigosArticulosMap("articulo_"+(k+1))!=null){
                 		
 	                	if(!UtilidadTexto.isEmpty(despachoForm.getCodigosArticulosMap("articulo_"+(k+1)).toString())){
 	                		
 	                		Log4JManager.info("\n  ---------------------------------------------------------------------------- "+
 	                    			"\n pos: "+k+"" +
 	                    			"\n art: " + despachoForm.getCodigosArticulosMap("articulo_"+(k+1)).toString()+
 	                    			"\n cos: " + despachoForm.getCodigoCentroCostoSolicitado()+
 	                    			"\n ins: " + codigoInstitucion+
 	                    			"\n ------------------------------------------------------------------------------------------"
 	                    	);
 	                		
 	                		numeroExistencias=UtilidadInventarios.existenciasArticuloAlmacen(Integer.parseInt(despachoForm.getCodigosArticulosMap("articulo_"+(k+1)).toString()), despachoForm.getCodigoCentroCostoSolicitado(), codigoInstitucion);
		                    if(numeroExistencias<0)
		                    {
		                    	codigosArticulos+=despachoForm.getCodigosArticulosMap("articulo_"+(k+1)).toString()+",";
		                    	valoresExistenciasXArticulo+=numeroExistencias+",";
		                    	centinela=1;
		                        //despachoForm.setMensajesAdvertenciaMap("mensaje_"+index, "ARTICULO  ["+despachoForm.getCodigosArticulosMap("articulo_"+(k+1)).toString()+"] QUEDA CON EXISTENCIA NEGATIVA ["+numeroExistencias+"]");
		                        //index++;
		                    }
 	                	}
                 	}
                }  
                catch(NumberFormatException e)
                {
                    logger.warn("Error en el parseInt del codigo del articulo con indice ="+k +"   error-->"+e);
                    return false;
                }
            }
            //error.inventarios.articuloQuedaConExistenciaNegativa
            if(centinela>0)
            	despachoForm.setMensajesAdvertenciaMap("mensaje_0", "MEDICAMENTO(S)  ["+codigosArticulos+"] QUEDA(N) CON EXISTENCIA(S) NEGATIVA(S) ["+valoresExistenciasXArticulo+"] [bz-14]");
            
            int centinela1=0;
            codigosArticulos="";
            valoresExistenciasXArticulo="";
            
            //parte de insumos
            for(int k=0; k < despachoForm.getNumeroIngresos(); k++)
            {
                try
                {
                    numeroExistencias=UtilidadInventarios.existenciasArticuloAlmacen(Integer.parseInt(despachoForm.getDespachoMap("articulo_"+k).toString().split("-")[0]), despachoForm.getCodigoCentroCostoSolicitado(), codigoInstitucion);
                    if(numeroExistencias<0)
                    {
                    	codigosArticulos+=despachoForm.getDespachoMap("articulo_"+k)+",";
                    	valoresExistenciasXArticulo+=numeroExistencias+",";
                    	centinela1=1;
                        //despachoForm.setMensajesAdvertenciaMap("mensaje_"+index, "ARTICULO  ["+Integer.parseInt(despachoForm.getDespachoMap("articulo_"+k).toString().split("-")[0])+"] QUEDA CON EXISTENCIA NEGATIVA ["+numeroExistencias+"]");
                        //index++;
                    }
                }  
                catch(NumberFormatException e)
                {
                    logger.warn("Error en el parseInt del codigo del articulo con indice ="+k +"   error-->"+e);
                    return false;
                }
            }
            if(centinela1>0)
            {
            	if(centinela>0)
            		numeroregistros=2;
            	else
            		numeroregistros=1;
	            	//error.inventarios.articuloQuedaConExistenciaNegativa
	            	despachoForm.setMensajesAdvertenciaMap("mensaje_"+(numeroregistros-1), "INSUMO(S)  ["+codigosArticulos+"] QUEDA(N) CON EXISTENCIA(S) NEGATIVA(S) ["+valoresExistenciasXArticulo+"] [bz-14]");
            		
            }	
        }    
        despachoForm.setMensajesAdvertenciaMap("numRegistros", numeroregistros+"");
        return true;
    }
    
    /**
     * metodo que carga el mapa para mostrar los mensajes de los articulos 
     * que no cumplen con el stock maxiom - minimo y punto pedido
     * @param despachoForm
     * @return
     */
    private boolean cargarMensajesStockMaximoMinimo (DespachoMedicamentosForm despachoForm, int codigoInstitucion)
    {
        int index=Integer.parseInt(despachoForm.getMensajesAdvertenciaMap("numRegistros")+"");
        String descripcionesStockMinimoArticulos="", descripcionesStockMaximoArticulos="", descripcionesPuntosPedido="";
        
        //parte de medicamentos
        for(int k=0; k < despachoForm.getColSize(); k++)
        {   
            try
            {
                if(!UtilidadInventarios.existenciasArticuloMayorIgualStockMinimo(Utilidades.convertirAEntero(despachoForm.getCodigosArticulosMap("articulo_"+(k+1))+""), codigoInstitucion))
                    descripcionesStockMinimoArticulos+= despachoForm.getCodigosArticulosMap("articulo_"+(k+1))+"" +", ";
                if(!UtilidadInventarios.existenciasArticuloMenorIgualStockMaximo(Utilidades.convertirAEntero(despachoForm.getCodigosArticulosMap("articulo_"+(k+1))+""), codigoInstitucion))
                    descripcionesStockMaximoArticulos+= despachoForm.getCodigosArticulosMap("articulo_"+(k+1))+""+", ";
                if(!UtilidadInventarios.existenciasArticuloMayorIgualPuntoPedido(Utilidades.convertirAEntero(despachoForm.getCodigosArticulosMap("articulo_"+(k+1))+""), codigoInstitucion) )
                    descripcionesPuntosPedido+= despachoForm.getCodigosArticulosMap("articulo_"+(k+1)).toString()+", ";
            }  
            catch(NumberFormatException e)
            {
                logger.warn("Error en el parseInt del codigo del articulo con indice ="+k +"   error-->"+e);
                return false;
            }
        } 
        //error.inventarios.quedanConCantidad
        if(!descripcionesStockMinimoArticulos.equals(""))
        {    
            despachoForm.setMensajesAdvertenciaMap("mensaje_"+index, "MEDICAMENTOS [ "+descripcionesStockMinimoArticulos+"] QUEDAN CON CANTIDAD MENOR AL STOCK MINIMO [bz-16]");
            index++;
        }
        //error.inventarios.quedanConCantidad
        if(!descripcionesStockMaximoArticulos.equals(""))
        {    
            despachoForm.setMensajesAdvertenciaMap("mensaje_"+index, "MEDICAMENTOS [ "+descripcionesStockMaximoArticulos+"] QUEDAN CON CANTIDAD MAYOR AL STOCK MAXIMO [bz-16]");
            index++;
        }
        //error.inventarios.quedanConCantidad
        if(!descripcionesPuntosPedido.equals(""))
        {    
            despachoForm.setMensajesAdvertenciaMap("mensaje_"+index, "MEDICAMENTOS [ "+descripcionesPuntosPedido+"] QUEDAN CON CANTIDAD MENOR AL PUNTO PEDIDO [bz-16]");
            index++;
        }
        
        //parte de inventarios
        descripcionesStockMaximoArticulos=""; descripcionesStockMinimoArticulos=""; descripcionesPuntosPedido="";
        
        for(int k=0; k < despachoForm.getNumeroIngresos(); k++)
        {  
            try
            {
                if(!UtilidadInventarios.existenciasArticuloMayorIgualStockMinimo(Integer.parseInt(despachoForm.getDespachoMap("articulo_"+k).toString().split("-")[0]), codigoInstitucion))
                    descripcionesStockMinimoArticulos+= despachoForm.getDespachoMap("articulo_"+k).toString().split("-")[0]+", ";
                if(!UtilidadInventarios.existenciasArticuloMenorIgualStockMaximo(Integer.parseInt(despachoForm.getDespachoMap("articulo_"+k).toString().split("-")[0]), codigoInstitucion))
                    descripcionesStockMaximoArticulos+= despachoForm.getDespachoMap("articulo_"+k).toString().split("-")[0]+", ";
                if(!UtilidadInventarios.existenciasArticuloMayorIgualPuntoPedido(Integer.parseInt(despachoForm.getDespachoMap("articulo_"+k).toString().split("-")[0]), codigoInstitucion) )
                    descripcionesPuntosPedido+= despachoForm.getDespachoMap("articulo_"+k).toString().split("-")[0]+", ";
            }  
            catch(NumberFormatException e)
            {
                logger.warn("Error en el parseInt del codigo del articulo con indice ="+k +"   error-->"+e);
                return false;
            }
        } 
        //error.inventarios.quedanConCantidad
        if(!descripcionesStockMinimoArticulos.equals(""))
        {    
            despachoForm.setMensajesAdvertenciaMap("mensaje_"+index, "INSUMOS [ "+descripcionesStockMinimoArticulos+"] QUEDAN CON CANTIDAD MENOR AL STOCK MINIMO [bz-16]");
            index++;
        }
        //error.inventarios.quedanConCantidad
        if(!descripcionesStockMaximoArticulos.equals(""))
        {    
            despachoForm.setMensajesAdvertenciaMap("mensaje_"+index, "INSUMOS [ "+descripcionesStockMaximoArticulos+"] QUEDAN CON CANTIDAD MAYOR AL STOCK MAXIMO [bz-16]");
            index++;
        }
        //error.inventarios.quedanConCantidad
        if(!descripcionesPuntosPedido.equals(""))
        {    
            despachoForm.setMensajesAdvertenciaMap("mensaje_"+index, "INSUMOS [ "+descripcionesPuntosPedido+"] QUEDAN CON CANTIDAD MENOR AL PUNTO PEDIDO [bz-16]");
            index++;
        }
        despachoForm.setMensajesAdvertenciaMap("numRegistros", index+"");
        return true;
    }
    
	/**
	 * M閠odo que carga los datos pertinentes desde el 
	 * form para el mundo 
	 * @param despachoForm DespachoMedicamentosForm (forma)
	 * @param mundo DespachoMedicamentos (mundo)
	 */
	protected void llenarMundo(DespachoMedicamentosForm despachoForm, DespachoMedicamentos mundo, UsuarioBasico usuario)
	{
		mundo.setUsuario(usuario.getLoginUsuario());
		//mundo.setEsDirecto(despachoForm.getEsDirecto());
		mundo.setNumeroSolicitud(despachoForm.getNumeroSolicitud());
		mundo.setDespachoParcial(despachoForm.getDespachoParcial());
	}
	
	
	
	/**
	 * Este m茅todo especifica las acciones a realizar en el estado
	 * agregar
     * @param despachoForm DespachoMedicamentosForm
     * @param mapping para manejar la navegaci贸n
     * @param con Conexi贸n con la fuente de datos
     * @return a la p谩gina "detalleDespachoMedicamentos.jsp"
	 * @throws SQLException
	 * @throws SQLException
     */
    private ActionForward accionAgregarOtro(DespachoMedicamentosForm despachoForm, 
            								ActionMapping mapping, 
            								Connection con) throws SQLException
    {
        int k=despachoForm.getNumeroIngresos();
        String cadena="";
        
        cadena+=despachoForm.getCodigoArticulo()+"-"+despachoForm.getArticulo();
        
        despachoForm.setDespachoMap("articulo_"+k, cadena +"");
        despachoForm.setDespachoMap("unidadMedida_"+k, despachoForm.getUnidadMedida()+"");
                
        despachoForm.setDespachoMap("despachoTotal_"+k, despachoForm.getDespachoTotal()+"");
        despachoForm.setDespachoMap("tipo_"+k, "MEM");
        despachoForm.setDespachoMap("nuevo_"+k, true);
        
        despachoForm.setDespachoMap("loteInsumo_"+k, "");
        despachoForm.setDespachoMap("fechaVencimientoLoteInsumo_"+k, "");
        despachoForm.setDespachoMap("existenciaXLoteInsumo_"+k, "");
        
        despachoForm.setNumeroIngresos(k+1);
        despachoForm.setArticulo("");
        
        despachoForm.setUnidadMedida("");
        despachoForm.setDespachoTotal(0);
        
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("paginaDetalle");
    }
	
    
    /**
     * @param despachoForm DespachoMedicamentosForm
     * @param mapping ActionMapping
     * @param con Connection
     * @return ActionForward
     */
    private ActionForward accionEliminarInsumo(	DespachoMedicamentosForm despachoForm, 
						            									ActionMapping mapping, 
						            									Connection con)throws SQLException
    {
        eliminarMemoria(despachoForm);        
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("paginaDetalle");
    }
    
    
     /**
     * Metodo para eliminar el registro del 
     * HashMap.
     * @param despachoForm DespachoMedicamentosForm
     */
    protected void eliminarMemoria(DespachoMedicamentosForm despachoForm)
    {
        int k=0;
        int esUltimo=despachoForm.getNumeroIngresos()-1;
        
        while(k < despachoForm.getNumeroIngresos())
        {
            String [] dato= String.valueOf(despachoForm.getDespachoMap("articulo_"+k)).split("-");
            
            if(despachoForm.getCodigoArticulo() == Integer.parseInt(dato[0]))
            {
               /*despachoForm.setDespachoMap("eliminar_"+k,"-1");
               despachoForm.setDespachoMap("articulo_"+k, 0 +"");*/
               
              if(k == esUltimo)
              {
                despachoForm.despachoMap.remove("articulo_"+k);
                despachoForm.despachoMap.remove("unidadMedida_"+k);
                despachoForm.despachoMap.remove("despachoTotal_"+k);  
                despachoForm.despachoMap.remove("despacho_"+k);
                despachoForm.setNumeroIngresos(despachoForm.getNumeroIngresos()-1);
              }
              else
              {    
               despachoForm.despachoMap.remove("articulo_"+k);
               despachoForm.despachoMap.remove("unidadMedida_"+k);
               despachoForm.despachoMap.remove("despachoTotal_"+k);
               despachoForm.despachoMap.remove("despacho_"+k);
               despachoForm.setNumeroIngresos(despachoForm.getNumeroIngresos()-1);
               despachoForm.setDespachoMap("articulo_"+k,despachoForm.getDespachoMap("articulo_"+(k+1)));
               despachoForm.setDespachoMap("unidadMedida_"+k,despachoForm.getDespachoMap("unidadMedida_"+(k+1)));
               despachoForm.setDespachoMap("despachoTotal_"+k,despachoForm.getDespachoMap("despachoTotal_"+(k+1)));
               despachoForm.setDespachoMap("despacho_"+k,despachoForm.getDespachoMap("despacho_"+(k+1)));
              }           
            }
            
            k ++;
        }
    }
    
	/**
	 * Este m閠odo carga los datos pertinentes a la forma.
	 * si se desea llenar mas variables revisar un metodo contructor llamado: InfoSolicitud 
	 *  y el decorator de SQLBaseDespachoMedicamento.listadoSolicitudesMedicamentos.
	 *   
	 * @param depachoForm (form)
	 * @param mundo
	 */
	protected void llenarForm(DespachoMedicamentosForm despachoForm, DespachoMedicamentos mundo)
	{		
		despachoForm.setNumeroSolicitud(mundo.getNumeroSolicitud());
		despachoForm.setFechaSolicitud(UtilidadFecha.conversionFormatoFechaAAp(mundo.getFechaSolicitud()));
		despachoForm.setHoraSoliciutd(mundo.getHoraSoliciutd());
		despachoForm.setMedicoSolicitante(mundo.getMedicoSolicitante());
		//despachoForm.setNumeroAutorizacion(mundo.getNumeroAutorizacion());
		despachoForm.setEstadoMedico(mundo.getEstadoMedico());
		despachoForm.setObservacionesGenerales(mundo.getObservacionesGenerales());
		despachoForm.setOrden(mundo.getOrden());
	}	
	
	
    /**
     * metodo que realiza las validaciones de acceso a usuario
     * @param paciente
     * @param map
     * @param req
     * @param user
     * @return ActionForward
     */
    protected ActionForward validacionesAccesoUsuario(  ActionMapping map, HttpServletRequest req, UsuarioBasico user)
    {
        /* validaciones de inventarios */
        if(!UtilidadInventarios.esAlmacenUsuarioAutorizado(user.getLoginUsuario(),user.getCodigoCentroCosto(), user.getCodigoInstitucionInt()))
        {
           ActionErrors errores = new ActionErrors(); 
           errores.add("usuario no autorizado", new ActionMessage("error.inventarios.usuarioNoAutorizado",user.getCentroCosto()));
           saveErrors(req, errores);
           return map.findForward("paginaErroresActionErrors");
        }
        return null;
    }
    
    
    
    
   /**
    *  Carga el detalle d euna solicitud cuando esta funcionalidad es llamada desde otra.
    * @param despachoForm
    * @param mapping
    * @param request
    * @param usuario
    * @param paciente
    * @return ActionForward
    * @author Cristhian Murillo
    * @throws SQLException 
    * @return ActionForward
    */
    private ActionForward accionDetalleSolicitudOtraFuncionalidad(	DespachoMedicamentosForm despachoForm, ActionMapping mapping,
													HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente) throws SQLException, IPSException 
	{
    	
    	despachoForm.inicializarForma(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
    	
    	Connection con = null;
    	con = UtilidadBD.abrirConexion();
    	
    	despachoForm.setCodigoPaciente(paciente.getCodigoPersona());
    	
    	if(con == null)
		{
    		request.setAttribute("codigoDescripcionError", "errors.problemasBd");
			return mapping.findForward("paginaError");
		}
		
    	
		accionListarSolicitudesXPaciente(despachoForm, mapping, con, despachoForm.getEstado(), usuario, paciente);
		
		ActionForward validacionesDatosDosisForward= null;
		
		if(con.isClosed()) 
		{
			con = null;
	    	con = UtilidadBD.abrirConexion();
		}
		
		validacionesDatosDosisForward= validacionesDatosDosisConsistentes(despachoForm, mapping, con, request, usuario);	
		if(validacionesDatosDosisForward==null)
		{
			return this.accionDetalleSolicitud(despachoForm,mapping,request, usuario,con);
		}
		else 
		{
			return validacionesDatosDosisForward;
		}
	}
    
    
    
    
    /**
     * Guarda el registro de entrega para el despacho
     * 
     * @param usuario
     * @param paciente
     * @param forma
     * 
     * @author Cristhian Murillo
     */
    private void guardarRegistroEntrega(UsuarioBasico usuario, PersonaBasica paciente, DespachoMedicamentosForm despachoForm)
    {
    	if(despachoForm.getCodigoDespacho() >0)
    	{
    		DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacion = new  DtoAutorizacionEntSubcontratadasCapitacion();
        	crearDtoRegistroEntrega(dtoAutorizacionEntSubcontratadasCapitacion, despachoForm);
        	
    		UtilidadTransaccion.getTransaccion().begin();
    		
        	boolean save = entregaMedicamentosInsumosEntSubcontratadasServicio.guardarRegistroEntregaMedicamentoInsumoEntSub(dtoAutorizacionEntSubcontratadasCapitacion, usuario);
    		
    		if(save){
    			UtilidadTransaccion.getTransaccion().commit();
    		}
    		else{
    			UtilidadTransaccion.getTransaccion().rollback();
    		}
    		
    	}
    }
    
    
    
    /**
     * Llena los datos del dto para guardar el registro de entrega
     * @param registroEntrega
     * @param forma
     * 
     * @author Cristhian Murillo
     */
    private void crearDtoRegistroEntrega(DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacion, DespachoMedicamentosForm despachoForm)
    {
    	UtilidadTransaccion.getTransaccion().begin();
    	
    	dtoAutorizacionEntSubcontratadasCapitacion.setAutorizacion(despachoForm.getCodigoAutorizacionEntidadSubcontratada());
    	dtoAutorizacionEntSubcontratadasCapitacion.setNumeroOrden(despachoForm.getNumeroSolicitud());
    	dtoAutorizacionEntSubcontratadasCapitacion.setNumeroDespacho(despachoForm.getCodigoDespacho());
    	
    	if(despachoForm.getRadioPendiente().equals("radioPendiente")){
    		dtoAutorizacionEntSubcontratadasCapitacion.setEstado(ConstantesIntegridadDominio.acronimoEstadoPendiente);
    	}else{
    		dtoAutorizacionEntSubcontratadasCapitacion.setEstado(ConstantesIntegridadDominio.acronimoEstadoEntregado);
    	}
    	
    	/*
    	ArrayList<DtoArticulosAutorizaciones> listaArticulosSolicitados =  new ArrayList<DtoArticulosAutorizaciones>();
    	for(int i=0; i<despachoForm.getNumeroIngresos(); i++)
		{
    		DtoArticulosAutorizaciones dtoArticuloDespachado = new DtoArticulosAutorizaciones();
    		
    		dtoArticuloDespachado.setCodigoArticulo(Integer.parseInt(String.valueOf(despachoForm.getDespachoMap("articulo_"+i)).split("-")[0]));
    		dtoArticuloDespachado.setTotalUnidadesFormulacion(Integer.parseInt(String.valueOf(despachoForm.getDespachoMap("cantidadInsumo_"+i))));
    		dtoArticuloDespachado.setDespachoEntregar(Integer.parseInt(despachoForm.getDespachoMap("despacho_"+i)+""));
    		//dtoArticuloDespachado.setValorArticulo(valorArticulo?);
    		listaArticulosSolicitados.add(dtoArticuloDespachado);
		}
		*/
    	
    	dtoAutorizacionEntSubcontratadasCapitacion.setListaArticulos(validarInsumosAutorizados(despachoForm));
    	
    	
    	/*
    	 * Se comparan los articulos cargados con los de la autorizacion. 
    	 * De esta manera se puede obtener la autorizacion de entidad subcontratada Articulo
    	 */
    	ArrayList<DtoArticulosAutorizaciones> listaArticulosAutorizacion = new ArrayList<DtoArticulosAutorizaciones>();
    	listaArticulosAutorizacion = aurorizacionesEntSubCapitacionServicio.listarautorizacionesEntSubArticuPorAutoEntSub(dtoAutorizacionEntSubcontratadasCapitacion);
    	
    	for (DtoArticulosAutorizaciones insumoSolicitado : dtoAutorizacionEntSubcontratadasCapitacion.getListaArticulos()) 
    	{
    		if(!despachoForm.isTieneAutorizacionEntidadSub())
    		{
    			insumoSolicitado.setTieneAutorizacion(false); // Si no se tiene ninguna autorizaci髇 todos los articulos solicitados se registran sin autorizaci髇
    		}
    		
    		for (DtoArticulosAutorizaciones insumoAutorizado : listaArticulosAutorizacion) 
			{
				if(insumoSolicitado.getCodigoArticulo().equals(insumoAutorizado.getCodigoArticulo()))
				{
					if(insumoSolicitado.getTotalUnidadesFormulacion().equals(insumoAutorizado.getTotalUnidadesFormulacion()))
					{
						insumoSolicitado.setAutorizacionPropia(insumoAutorizado.getAutorizacionPropia());
					}
				}
			}
		}
    	
    	//dtoAutorizacionEntSubcontratadasCapitacion.setListaArticulos(listaArticulosSolicitados);
    	
    	UtilidadTransaccion.getTransaccion().commit();
    }

    
    
    
    
    /**
     * Valida si el convenio del cargo de la orden del paciente maneja Capitaci髇 Subcontratada.
     * 
     * @param despachoForm
     * @author Cristhian Murillo
     */
    private void validarConvenioCargoOrden(DespachoMedicamentosForm despachoForm, UsuarioBasico usuario)
    {
    	despachoForm.setListaAdvertencias(new ArrayList<String>());
    	boolean tieneAutorizacionCapitacionsubcontratada = true;
    	
    	UtilidadTransaccion.getTransaccion().begin();
    	
    	Convenios convenio = new Convenios();
    	try
    	{
    		convenio = convenioServicio.obtenerConvenioDetCargoPorSolicitud(despachoForm.getNumeroSolicitud(), usuario.getCodigoInstitucionInt()).get(0);
		}
    	catch (IndexOutOfBoundsException e)
    	{
    		convenio = null;
    		Log4JManager.error(e);
		}
    	
    	if(convenio != null)
    	{
    		if(convenio.getCapitacionSubcontratada() != null)
    		{
    			if(convenio.getCapitacionSubcontratada().equals(ConstantesBD.acronimoSiChar))
        		{
        			AutorizacionesEntidadesSub autorizacionesEntidadesSub; 	autorizacionesEntidadesSub = new AutorizacionesEntidadesSub(); 
        			autorizacionesEntidadesSub = aurorizacionesEntSubCapitacionServicio.obtenerAutorizacionesEntidadesSubPorId(despachoForm.getCodigoAutorizacionEntidadSubcontratada());
        			if(autorizacionesEntidadesSub != null)
        			{ 
        				HashSet<AutorizacionesCapitacionSub> setAutorizacionesCapitacionSub = new HashSet<AutorizacionesCapitacionSub>();
        				setAutorizacionesCapitacionSub.addAll(autorizacionesEntidadesSub.getAutorizacionesCapitacionSubs());
        				if( setAutorizacionesCapitacionSub.isEmpty())
        				{
        					// Si NO tiene Autorizacion de capitacu髇 se muestra un 鷑ico mensaje
        					String mensajeConcreto = fuenteMensaje.getMessage("DespachoMedicamentosForm.noAutorizacionCapitacion");
        	    			despachoForm.getListaAdvertencias().add(mensajeConcreto);
        	    			tieneAutorizacionCapitacionsubcontratada = false;
        				}
        			}
        		}
    			else{
    				tieneAutorizacionCapitacionsubcontratada = false;
    			}
    		}
    		else{
    			tieneAutorizacionCapitacionsubcontratada = false;
    		}
    	}
    	
    	despachoForm.setTieneAutorizacionEntidadSub(tieneAutorizacionCapitacionsubcontratada);
    	
    	// Si tiene Autorizaci髇 de capitaci髇 se hace la verificaci髇 por art韈ulo
    	if(tieneAutorizacionCapitacionsubcontratada)
    	{
    		validarInsumosAutorizados(despachoForm);
    	}
    	
    	UtilidadTransaccion.getTransaccion().commit();
    }
    
    
    
    /**
     * Recorre y compara los articulos autorizados con los solicitados para determinar cuales tienen autorizaci髇 de capitaci髇.
     * 
     * @param despachoForm
     * @return  ArrayList<DtoArticulosAutorizaciones>
     * 
     * @author Cristhian Murillo
     */
    private ArrayList<DtoArticulosAutorizaciones> validarInsumosAutorizados (DespachoMedicamentosForm despachoForm)
    {
    	
    	ArrayList<DtoArticulosAutorizaciones> listaArticulosSolicitados =  new ArrayList<DtoArticulosAutorizaciones>();
    	
    	// Lista Insumos Solicitados =========================
    	for(int i=0; i<despachoForm.getNumeroIngresos(); i++)
		{
    		DtoArticulosAutorizaciones dtoArticuloDespachado;
    		dtoArticuloDespachado = new DtoArticulosAutorizaciones();
    		
    		dtoArticuloDespachado.setCodigoArticulo(Integer.parseInt(String.valueOf(despachoForm.getDespachoMap("articulo_"+i)).split("-")[0]));
    		if(UtilidadTexto.isEmpty(despachoForm.getDespachoMap("cantidadInsumo_"+i)+"")){
    			dtoArticuloDespachado.setTotalUnidadesFormulacion(new Integer(0));
    		}
    		else{
    			dtoArticuloDespachado.setTotalUnidadesFormulacion(Integer.parseInt(String.valueOf(despachoForm.getDespachoMap("cantidadInsumo_"+i))));
    		}
    		dtoArticuloDespachado.setDespachoEntregar(Integer.parseInt(despachoForm.getDespachoMap("despacho_"+i)+""));
    		//dtoArticuloDespachado.setValorArticulo(valorArticulo?);
    		
    		// Para no cargar los articulos autorizados y solo los solicitados
    		if(dtoArticuloDespachado.getTotalUnidadesFormulacion() >0){
    			listaArticulosSolicitados.add(dtoArticuloDespachado);
    		}
		}
    	// ====================================================

    	// Lista medicamentos solicitados =====================
    		Iterator<HashMap<String, Object>> iterador=despachoForm.getCol().iterator();
    		while(iterador.hasNext())
    		{
    			DtoArticulosAutorizaciones dtoArticuloDespachado;
        		dtoArticuloDespachado = new DtoArticulosAutorizaciones();
    			HashMap<String, Object> mapa=iterador.next();
    			
    			dtoArticuloDespachado.setCodigoArticulo(Integer.parseInt(mapa.get("codigo")+""));
    			if(UtilidadTexto.isEmpty(mapa.get("cantidad")+"")){
    				dtoArticuloDespachado.setTotalUnidadesFormulacion(new Integer(0));
    			}
    			else{
    				dtoArticuloDespachado.setTotalUnidadesFormulacion(Integer.parseInt(String.valueOf(mapa.get("cantidad"))));
    			}
    			dtoArticuloDespachado.setDespachoEntregar(Integer.parseInt(mapa.get("despacho_total")+""));
    			
    			if(dtoArticuloDespachado.getTotalUnidadesFormulacion() > 0){
    				listaArticulosSolicitados.add(dtoArticuloDespachado);
    			}
    		}
    	// ===================================================
    	
    	// Lista Insumos Autorizados
    	ArrayList<DtoArticulosAutorizaciones> listaArticulosAutorizacion = new ArrayList<DtoArticulosAutorizaciones>();
    	DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacion = new  DtoAutorizacionEntSubcontratadasCapitacion();
    	dtoAutorizacionEntSubcontratadasCapitacion.setAutorizacion(despachoForm.getCodigoAutorizacionEntidadSubcontratada());
    	listaArticulosAutorizacion = aurorizacionesEntSubCapitacionServicio.listarautorizacionesEntSubArticuPorAutoEntSub(dtoAutorizacionEntSubcontratadasCapitacion);
    	
    	for (DtoArticulosAutorizaciones insumoSolicitado : listaArticulosSolicitados) 
    	{
    		boolean autorizado = false;
    		for (DtoArticulosAutorizaciones insumoAutorizado : listaArticulosAutorizacion) 
			{
				if(insumoSolicitado.getCodigoArticulo().equals(insumoAutorizado.getCodigoArticulo()))
				{
					if(insumoSolicitado.getTotalUnidadesFormulacion().equals(insumoAutorizado.getTotalUnidadesFormulacion()))
					{
						autorizado = true;
						insumoSolicitado.setTieneAutorizacion(true);
					}
				}
			}
    		if(!autorizado)
    		{
    			String mensajeConcreto = fuenteMensaje.getMessage("DespachoMedicamentosForm.articuloSinAutoEntSub", insumoSolicitado.getCodigoArticulo());
    			despachoForm.getListaAdvertencias().add(mensajeConcreto);
    			insumoSolicitado.setTieneAutorizacion(false);
    		}
		}
    	
    	return listaArticulosSolicitados;
    }
}