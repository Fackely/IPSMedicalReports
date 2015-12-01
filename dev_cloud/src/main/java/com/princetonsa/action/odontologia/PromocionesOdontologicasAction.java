package com.princetonsa.action.odontologia;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
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

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.LogReporteArchivoPlano;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.ReportesArchivoPlano.EmunReporteArchivoPlano;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.odontologia.PromocionesOdontologicasForm;
import com.princetonsa.dto.facturacion.DtoEmpresasInstitucion;
import com.princetonsa.dto.manejoPaciente.DtoCategoriaAtencion;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.manejoPaciente.DtoRegionesCobertura;
import com.princetonsa.dto.odontologia.DtoDetCaPromocionesOdo;
import com.princetonsa.dto.odontologia.DtoDetConvPromocionesOdo;
import com.princetonsa.dto.odontologia.DtoDetPromocionOdo;
import com.princetonsa.dto.odontologia.DtoPromocionesOdontologicas;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.princetonsa.mundo.manejoPaciente.CategoriaAtencion;
import com.princetonsa.mundo.manejoPaciente.RegionesCobertura;
import com.princetonsa.mundo.odontologia.DetCaPromocionesOdo;
import com.princetonsa.mundo.odontologia.DetConvPromocionesOdo;
import com.princetonsa.mundo.odontologia.DetPromocionesOdontolgicas;
import com.princetonsa.mundo.odontologia.PromocionesOdontologicas;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;


/**
 * ANEXO 822 PROMOCIONES ODONTOLOGICAS
 * 
 * @author axioma
 * 
 */
public class PromocionesOdontologicasAction extends Action {
	/**
	 * 
	 */
	private Logger logger = Logger.getLogger(PromocionesOdontologicasAction.class);

	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		if (form instanceof PromocionesOdontologicasForm) {
			
			PromocionesOdontologicasForm forma = (PromocionesOdontologicasForm) form;
			UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
			InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			ActionErrors errores = new ActionErrors();

			logger.info("\n\n\n\n\n\n\n");

			logger.info(" UTILIZA INSTITUCION ----*>" + forma.getUtilizaInst());
			logger.info("\n\n\n\n\n\n\n");
			logger.info("\n\n\n\n\n\n");
			logger.info("\t\t\t\t ESTADO--------------->>>>>>"	+ forma.getEstado());
			logger.info("\n\n\n\n\n\n");

			if (forma.getEstado().equals("empezar")) {
				return accionEmpezar(mapping, forma, usuario);
			} else if (forma.getEstado().equals("consulta")) {
				return accionEmpezarConsulta(forma, usuario, mapping, institucionBasica);
			} else if (forma.getEstado().equals("nuevo")) {
				return accionNuevo(mapping, forma);
			} else if (forma.getEstado().equals("guardar")) {
				return accionGuardar(mapping, forma, usuario);
			} else if (forma.getEstado().equals("guardarModificar")) {
				return accionguardarModificar(mapping, forma, usuario);
			} else if (forma.getEstado().equals("modificar")) {
				return accionModificar(mapping, forma);
			} else if (forma.getEstado().equals("eliminar")) {
				return accionEliminar(forma, mapping, usuario, request);
			}
			// ----------> Empezar Detalle -----------
			else if (forma.getEstado().equals("empezarDetalle")) {
				return accionEmpezarDetalle(mapping, forma, usuario,institucionBasica);
			} else if (forma.getEstado().equals("ordenar")) 
			{
				return accionOrdenar(mapping, forma, usuario);
			} else if (forma.getEstado().equals("cargarPrograma")) {
				return mapping.findForward("empezarDetalle");
			}
			// -----> cargar Centros de atencion
			else if (forma.getEstado().equals("cargarCentroAtencion")) {
				return accionCargarCentroAtencion(mapping, forma, usuario);
			} else if (forma.getEstado().equals("adaptarCentros")) {
				return accionAdaptarCentros(mapping, forma);
			} else if (forma.getEstado().equals("eliminarCentroAtencion")) {
				return accionEliminarCentros(mapping, forma);
			} else if (forma.getEstado().equals("adaptarConvenios")) {
				return accionAdaptarConvenios(mapping, forma);
			}
			// << CARGAR PAISES POR AJAX>>
			else if (forma.getEstado().equals("cargarPaisXCiudad")) {
				accionCargarPaisXCiudad(forma, response);
			}
			// <<>
			else if (forma.getEstado().equals("eliminarConvenios")) {
				return accionEliminarConvenios(mapping, forma);
			}
			/***
			 * 
			 * Guardando el Detalle
			 */
			else if (forma.getEstado().equals("guardarDetalle")) 
			{
				return accionGuardarDetalle(mapping, request, forma, usuario,	institucionBasica);
			} 
			else if (forma.getEstado().equals("consutaAvanzada")) 
			{
				return busquedaAvanzadaPromociones(forma, mapping, errores,	request, usuario);
			}
			// << CARGAR CENTRO DE ATENCION POR AJAX>>
			else if (forma.getEstado().equals("cargarCentro")) {
				cargarCentroAtencion(forma, response);
			} else if (forma.getEstado().equals("cargarCentroXEmpresa")) {
				cargarCentroAtencion(forma, response);
			}
			// <<Recarga >>
			else if (forma.getEstado().equals("generarReporte")) {
				return accionGenerarReporte(mapping, request, forma, usuario,
						institucionBasica);
			}

		}// if
		return null;
	}

	
	
	
	
	/**
	 * ESTE METODO HACE:
	 * 
	 *  1. CREA EL PDF DE LA CONSULTA.
	 *  2. GUARDA EL LOG DE LA CONSULTA
	 * 
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param usuario
	 * @param institucionBasica
	 * @return
	 */
	private ActionForward accionGenerarReporte(ActionMapping mapping,HttpServletRequest request, PromocionesOdontologicasForm forma, UsuarioBasico usuario, InstitucionBasica ins) 
	{
		
		String tmpRutaImpresion ="";
		
		/**
		 * CREAR EL REPORTE CON BIRT 
		 */
		if (forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion)) 
		{
						Vector v = new Vector();
				
						HashMap result = new HashMap();
				
						String nombreRptDesign = "PromocionesOdontologicas.rptdesign";
				
						// ***************** INFORMACIÓN DEL CABEZOTE
						DesignEngineApi comp = new DesignEngineApi(ParamsBirtApplication.getReportsPath()+ "odontologia/", nombreRptDesign);
						
						tmpRutaImpresion=ParamsBirtApplication.getReportsPath()+ "odontologia/"+nombreRptDesign;
						// Logo
						Connection con = UtilidadBD.abrirConexion();
						comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
						logger.info("xxxxxxxxxx--->"+ ins.getLogoReportes());
						comp.insertGridHeaderOfMasterPage(0, 1, 1, 4);
						v.add(ins.getRazonSocial());
						if (Utilidades.convertirAEntero(ins.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
							v.add(Utilidades.getDescripcionTipoIdentificacion(con, ins.getTipoIdentificacion())+ ". "+ ins.getNit()+ " - "
											+ ins.getDigitoVerificacion());
						else
							v.add(Utilidades.getDescripcionTipoIdentificacion(con, ins.getTipoIdentificacion())	+ ". " + ins.getNit());
							
							v.add(ins.getDireccion());
							v.add("Tels. " + ins.getTelefono());
							comp.insertLabelInGridOfMasterPage(0, 1, v);
							UtilidadBD.cerrarObjetosPersistencia(null, null, con);
					
							// Información del reporte
							comp.insertLabelInGridPpalOfHeader(1, 0, "PROMOCIONES ODONTOLÓGICAS");
					
							// Información del reporte
							comp.insertLabelInGridPpalOfHeader(	2, 0,"Fecha de Generación Inicial:  "+ forma.getDtoPromocion().getFechaGeneracionInicial() +" -  Fecha de Generación Final: "+forma.getDtoPromocion().getFechaGeneracionFinal() );
					
							comp.insertarUsuarioImprimio(usuario.getNombreUsuario());
							
							// Reemplazar consulta
							
							Boolean tmpParametroUtilizaProgramas = definirTipoReporteAPlicaProgramaServicio(forma);
							String newquery =PromocionesOdontologicas.consultaAvanzadaPromocionesReportes(forma.getDtoPromocion(), tmpParametroUtilizaProgramas);
							
							logger.info("CONSULTA DEL REPORTE DEL BIRT newquery birt------>"+newquery+"\n\n\n\n");
							
						
						comp.obtenerComponentesDataSet("promocion");
						comp.modificarQueryDataSet(newquery);
						
				
						// debemos arreglar los alias para que funcione oracle, debido a que
						// este los convierte a MAYUSCULAS
						comp.lowerAliasDataSet();
						String newPathReport = comp.saveReport1(false);
						comp.updateJDBCParameters(newPathReport);
				
						result.put("descripcion", newPathReport);
						result.put("resultado", true);
						result.put("urlArchivoPlano", "");
						result.put("pathArchivoPlano", "");
						
						 
				        if(!newPathReport.equals(""))
				        {
				        	request.setAttribute("isOpenReport", "true");
							request.setAttribute("newPathReport",result.get("descripcion").toString());
				        }
							
						
						
		}
		/**
		 * REPORTE ARCHIVO PLANO
		 */
		else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
		{
			tmpRutaImpresion=forma.getRutaArchivoAbsoluta();
		}
		
		
		/**
		 * GUARDAR LOG DEL REPORTE 
		 */
		forma.getDtoPromocion().setFechaModificada(UtilidadFecha.getFechaActual());
		forma.getDtoPromocion().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getDtoPromocion().setHoraModificada(UtilidadFecha.getHoraActual());
		forma.getDtoPromocion().setInstitucion(usuario.getCodigoInstitucionInt());
		PromocionesOdontologicas.guardarLogConsulta(forma.getDtoPromocion(),forma.getTipoReporte(),tmpRutaImpresion);
		
		return mapping.findForward("paginaPrincipal");
	}

	
	
	
	
	
	/**
	 * METODO PARA DEFINIR EL TIPO DE REPORTE
	 * 
	 * @param forma
	 * @return
	 */
	private Boolean definirTipoReporteAPlicaProgramaServicio(PromocionesOdontologicasForm forma) 
	{
		Boolean tmpParametroUtilizaProgramas=Boolean.TRUE;
		if(forma.getTipodeRelacion().equals("Programa"))
		{
			tmpParametroUtilizaProgramas=Boolean.TRUE;
		}
		else if( forma.getTipodeRelacion().equals("Servicio")) 
		{
			tmpParametroUtilizaProgramas=Boolean.FALSE;
		}
		return tmpParametroUtilizaProgramas;
	}

	
	
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionEliminarCentros(ActionMapping mapping, 	PromocionesOdontologicasForm forma) 
	{
		forma.setMensajeCentro("");
	
		if (forma.getListaCaPromociones().get(forma.getPosArrayCentro()).isEstadoDb()) 
		{
			forma.getListaCaPromociones().get(forma.getPosArrayCentro()).setVisible(false);
		} 
		else 
		{
			forma.getListaCaPromociones().remove(forma.getPosArrayCentro());
		}

		return mapping.findForward("listaCentroAtencion");
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionEliminarConvenios(ActionMapping mapping,PromocionesOdontologicasForm forma) 
	{
		forma.setMensajeConvenio("");
		
		if (forma.getListaDetConvenios().get(forma.getPosArrayConvenio()).isEstadoDb()) 
		{
			forma.getListaDetConvenios().get(forma.getPosArrayConvenio()).setVisible(false);
		} 
		else 
		{
			forma.getListaDetConvenios().remove(forma.getPosArrayConvenio());
		}

		return mapping.findForward("listaConveniosPromocion");
	}

	
	
	
	/**
	 * ACCION EMPEZAR CONSULTA
	 * CARGA TODA LA ESTRUCTURA PARA EMPEZAR CON LA BUSQUEDA DE PROMOCIONES
	 * 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezarConsulta(PromocionesOdontologicasForm forma, UsuarioBasico usuario,	ActionMapping mapping, InstitucionBasica institucionBasica) {
		
		forma.reset();
		forma.setUtilizaInst(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt())); 
		accionDefinirParametroUtilizacionProgramasServicios(forma, usuario);
		this.cargarPais(forma);
		this.cargarEstructuras(forma, usuario, institucionBasica);
		this.cargarCentroPais(forma, usuario, institucionBasica);
		//this.cargarEspecialidadOdontologica(forma);
		this.cargarInstitucionEmpresa(forma, usuario);
		
		/**
		 * 
		 */
		String codigoCiudad= institucionBasica.getCodigoCiudad()+"-"+institucionBasica.getCodigoDepto();
		forma.getDtoDetPromocion().setCiudadCentroAtencion(codigoCiudad); 
		
		
		return mapping.findForward("paginaPrincipal");
	}

	
	/**
	 * ACCION DEFINE EL PARAMERO PARA APLICAR SERVICIOS O PROGRAMAS
	 * @param forma
	 * @param usuario
	 */
	private void accionDefinirParametroUtilizacionProgramasServicios(PromocionesOdontologicasForm forma, UsuarioBasico usuario) 
	{
		if (ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()).equals(	ConstantesBD.acronimoSi)) 
		{
			forma.setTipodeRelacion("Programa");
		}
		else 
		{
			forma.setTipodeRelacion("Servicio");
		}
	}

	
	
	/**
	 * CARGAR EL CENTRO Y EL PAIS
	 * 
	 * @param forma
	 * @param usuario
	 * @param institucionBasica
	 */
	private void cargarCentroPais(PromocionesOdontologicasForm forma,	UsuarioBasico usuario, InstitucionBasica institucionBasica) {
	
		forma.getCentroAtencion().setNombre(usuario.getCentroAtencion());
		forma.getCentroAtencion().setCodigo(usuario.getCodigoCentroAtencion());
		
		DtoCentrosAtencion dto = new DtoCentrosAtencion();
		dto.setConsecutivo(usuario.getCodigoCentroAtencion());
		dto.setActivo(Boolean.TRUE);
		
		ArrayList<DtoCentrosAtencion>  tmpLista=UtilidadesManejoPaciente.obtenerCentrosAtencion(dto);
		/**
		 * CARGA EL NOMBRE Y EL CODIGO DEL PAIS 
		 */
	
		if(tmpLista.size()>0)
		{
			forma.getPais().setNombre(tmpLista.get(0).getNombrePais());
			forma.setTmpCodigoPais(tmpLista.get(0).getPais());
			forma.getPais().setCodigo(tmpLista.get(0).getPais());	
		}
	}
	
	

	/**
	 * ACCION EMPEZAR GUARDAR DETALLE
	 * 
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param usuario
	 * @param institucionBasica
	 * @return
	 */
	private ActionForward accionGuardarDetalle(ActionMapping mapping,HttpServletRequest request, PromocionesOdontologicasForm forma,
												UsuarioBasico usuario, InstitucionBasica institucionBasica) 
	{
	
		
		ActionErrors errores = new ActionErrors();
		// ---
		this.validarEdad(errores, forma);
		this.valorPromocionPorcentaje(errores, forma);
		this.porcentajeHonorarios(errores, forma);
		this.accionValidarProgramaServicio(forma, usuario, errores);
		

		if (errores.isEmpty()) {

			//SON EXCLUYENTES
			if (forma.getDtoDetPromocion().getRegionCentroAtencion() > 0) 
			{
				forma.getDtoDetPromocion().setPaisCentroAtencion(institucionBasica.getCodigoPais());
			}
			else
			{
				this.arreglarFormatoCiudad(forma);
				forma.getDtoDetPromocion().setCiudadCentroAtencion(forma.getTmpCodigoCiudad());
				forma.getDtoDetPromocion().setDeptoCentroAtencion(forma.getTmpCodigoDepartamento());
				forma.getDtoDetPromocion().setPaisCentroAtencion(forma.getTmpCodigoPais());
				
			}
			

			/*
			 * GUARDAR Y/O MODIFICAR DETALLE PROMOCION ODONTOLOGICA
			 */
			int codigoDetalle = 0;

			
			if (forma.getDtoDetPromocion().getCodigoPk() != 0) 
			{
				
				codigoDetalle = forma.getDtoDetPromocion().getCodigoPk();

				forma.getDtoDetPromocion().setFechaModificada(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				forma.getDtoDetPromocion().setUsuarioModifica(usuario.getLoginUsuario());
				forma.getDtoDetPromocion().setHoraModificada(UtilidadFecha.getHoraActual());
				DetPromocionesOdontolgicas.modificar(forma.getDtoDetPromocion());
				forma.getDtoDetPromocion().setDetPromocionOdo(codigoDetalle);
				
				DetPromocionesOdontolgicas.guardarLog(forma.getDtoDetPromocion());

			} 
			else 
			{
				forma.getDtoDetPromocion().setPromocionOdontologia(forma.getDtoPromocion().getCodigoPk());
				forma.getDtoDetPromocion().setFechaModificada(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				forma.getDtoDetPromocion().setUsuarioModifica(usuario.getLoginUsuario());
				forma.getDtoDetPromocion().setHoraModificada(UtilidadFecha.getHoraActual());
				codigoDetalle = DetPromocionesOdontolgicas.guardar(forma.getDtoDetPromocion());
				forma.getDtoDetPromocion().setDetPromocionOdo(codigoDetalle);
				DetPromocionesOdontolgicas.guardarLog(forma.getDtoDetPromocion());
			}

			if (codigoDetalle != 0) 
			{
				// GUARDAR CENTRO DE ATENCION
				accionGuardarCentroAtencionConvenios(forma, usuario,	codigoDetalle);
			}

			forma.setEstado("resumenDetalle");
			forma.setEsConsulta(Boolean.TRUE);
		} 
		else 
		{
			saveErrors(request, errores);
			forma.setEstado("mostrarErroresGuardar");
			forma.setEsConsulta(Boolean.FALSE);
		}
		//ORGANIZAR EL DTO CUIDAD 
		forma.getDtoDetPromocion().setCiudadCentroAtencion(forma.getTmpCodigoCiudad()+"-"+forma.getTmpCodigoDepartamento()+"-"+forma.getTmpCodigoPais());
		
		return mapping.findForward("empezarDetalle");
	}





	
	/**
	 * Metodo para validar el programa o servicio
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 * @param usuario
	 * @param errores
	 */
	private void accionValidarProgramaServicio(
			PromocionesOdontologicasForm forma, UsuarioBasico usuario,
			ActionErrors errores) {
		if (ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()).equals(	ConstantesBD.acronimoSi))
		{
			if(forma.getDtoDetPromocion().getProgramaOdontologico().getCodigo().doubleValue()<=0)
			{
				errores.add("", new ActionMessage("errors.required","Programa"));
			}
		}
		else 
		{
			if( forma.getDtoDetPromocion().getServicio().getCodigo()<=ConstantesBD.codigoNuncaValido)
			{
				errores.add("", new ActionMessage("errors.required","Servicio"));
			}
		}
	}

	
	
	
	/**
	 * ACCION GUARDAR LOS CENTROS DE ATENCION Y LOS CONVENIOS
	 * 
	 * @param forma
	 * @param usuario
	 * @param codigoDetalle
	 */
	private void accionGuardarCentroAtencionConvenios(PromocionesOdontologicasForm forma, UsuarioBasico usuario,int codigoDetalle) {
		/**
		 * ACCION MODIFICAR Y GUARDAR CENTRO DE ATENCION
		 */
		accionModifcarGuardarCentrosAtencion(forma, usuario, codigoDetalle);

		/**
		 * GUARDAR y/o MODIFICAR CONVENIOS
		 */
		accionGuardarConvenios(forma, usuario, codigoDetalle);
	}
	
	

	/**
	 * ACCION MODIFICAR Y/O GUARDAR CENTROS DE ATENCION
	 * 
	 * @param forma
	 * @param usuario
	 * @param codigoDetalle
	 */
	private void accionModifcarGuardarCentrosAtencion(PromocionesOdontologicasForm forma, UsuarioBasico usuario,	int codigoDetalle) {

		if (forma.getListaCaPromociones().size() > 0) {
			/**
			 * INACTIVAR TODOS LOS REGISTRO DE LA BASE DE DATOS
			 */
			accionModificarPromocionAplicaTodoCentroAtencion(forma,
					codigoDetalle, usuario);

			for (DtoDetCaPromocionesOdo dtoCa : forma.getListaCaPromociones()) {
				// insertar base de datos New Dto
				if ((dtoCa.isVisible()) && (dtoCa.isEstadoDb() == false)) {
					accionGuardarCentroAtencion(usuario, codigoDetalle, dtoCa);
				} 
				else if ((dtoCa.isVisible() == false) && (dtoCa.isEstadoDb())) 
				{
					logger.info("\n\n\n\n");
					logger.info(" INACTIVAR CA CODIGO--->"
							+ dtoCa.getCodigoPk());
					logger.info("\n\n\n\n");
					accionInactivarCentroAtencionBD(usuario, dtoCa);
				} else if ((dtoCa.isVisible() == true) && (dtoCa.isEstadoDb())
						&& (dtoCa.getActivo().equals(ConstantesBD.acronimoSi))) {
					logger.info("\n\n\n\n");
					logger.info(" ACTIVAR CA CODIGO--->" + dtoCa.getCodigoPk());
					logger.info("\n\n\n\n");
					accionActivarCentroAtencionBD(usuario, dtoCa);
				}

			}
		}

		/**
		 * VERIFICAR EXISTENCIA DE DET CENTRO DE ATENCION} SI NO EXISTE INSERTAR
		 * UN DETALLE CON CENTRO ATENCION =NULL , QUE SIGNIFICA QUE APLICA PARA
		 * TODOS LOS CENTROS DE ATENCION
		 */
		else {
			// GUARDAR CENTRO DE ATENCION PARA APLICAR PARA TODOS
			accionGuardarAplicaTodosCentroAtencion(usuario, codigoDetalle);
		}
	}

	
	
	
	/**
	 * Modicar promocion
	 * 
	 * @param forma
	 */
	private void accionModificarPromocionAplicaTodoCentroAtencion(PromocionesOdontologicasForm forma, int codigoDetalle, UsuarioBasico usuario) 
	{
		boolean promocionAplicaTodos = Boolean.TRUE;
		for (DtoDetCaPromocionesOdo dtoCa : forma.getListaCaPromociones()) {
			logger.info(" Objeto--Visible-->" + dtoCa.isVisible()
					+ "  BaseDatos-->" + dtoCa.isEstadoDb() + "   ACTIVO-->"
					+ dtoCa.getActivo());
			if ((dtoCa.isVisible()) && (dtoCa.isEstadoDb() == true)
					&& (dtoCa.getActivo().equals(ConstantesBD.acronimoSi))) {
				logger.info(" VISIBLE Y BASE DE DATOS== FALSE ");
				promocionAplicaTodos = Boolean.FALSE;
			} else if ((dtoCa.isVisible()) && (dtoCa.isEstadoDb() == false)) {
				logger.info("VISIBLE Y NO ES BASE DE DATOS== FALSE ");
				promocionAplicaTodos = Boolean.FALSE;
			}
		}
		/**
		 * BUSCAR EL OBJETO PARA MODIFICARLO DEBE EXISTE SOLO UN REGISTRO CON
		 * CENTRO DE ATENCION EN NULL: EN OTRO CASO ES UN ERROR
		 * 
		 */
		DtoDetCaPromocionesOdo dtoDetCentro = new DtoDetCaPromocionesOdo();
		dtoDetCentro.setDetPromocionOdo(codigoDetalle);
		ArrayList<DtoDetCaPromocionesOdo> tmplistaCa = DetCaPromocionesOdo
				.cargarCentroAtencionNUll(dtoDetCentro);

		if (promocionAplicaTodos) {
			logger.info("\n\n\n\n\n\n");
			logger
					.info("**************************** PROMOCIONES APLICA PARA TODOS ***********************************");
			logger.info("\n\n\n\n\n\n");

			if (tmplistaCa.size() > 0) {
				logger
						.info("\n\n\n APLICA PARA TODOS EXISTEN REGISTRO  ACTIVAR  ******************************************************************");
				dtoDetCentro.setCodigoPk(tmplistaCa.get(0).getCodigoPk());
				accionActivarCentroAtencionBD(usuario, dtoDetCentro);

			} else {
				logger
						.info(" APLICA PARA TODOS NO EXISTE REGISTRO ACTIVAR *****************************************************************");
				accionGuardarCentroAtencion(usuario, codigoDetalle,
						dtoDetCentro);
			}
		} else {
			logger.info("\n\n\n\n\n\n");
			logger
					.info("************************** NO APLICA PARA TODOS **********************************************");
			logger.info("\n\n\n\n\n\n");

			if (tmplistaCa.size() > 0) {
				logger
						.info(" NO APLICA PARA TODOS EXISTE REGISTRO INACTIVAR *****************************************************************");
				dtoDetCentro.setCodigoPk(tmplistaCa.get(0).getCodigoPk());
				accionInactivarCentroAtencionBD(usuario, dtoDetCentro);
			}
		}
	}

	
	
	
	/**
	 * ACCION GUARDAR CENTROS DE ATENCION BD
	 * 
	 * @param usuario
	 * @param codigoDetalle
	 * @param dtoCa
	 */
	private void accionGuardarCentroAtencion(UsuarioBasico usuario,
			int codigoDetalle, DtoDetCaPromocionesOdo dtoCa) {
		logger.info("\n\n\nGuardando Nuevo Objeto  CENTRO DE ATENCION  \n\n");
		logger.info("codigoooooooooooooooooo*-------------" + codigoDetalle);

		dtoCa.setDetPromocionOdo(codigoDetalle);
		dtoCa.setActivo(ConstantesBD.acronimoSi);
		dtoCa.setFechaModifica(UtilidadFecha.getFechaActual());
		dtoCa.setHoraModifica(UtilidadFecha.getHoraActual());
		dtoCa.setUsuarioModifica(usuario.getLoginUsuario());
		DetCaPromocionesOdo.guardar(dtoCa);
	}
	
	
	

	/**
	 * ACCION INACTIVAR CENTROS ATENCION EN LA BD
	 * 
	 * @param usuario
	 * @param dtoCa
	 */
	private void accionInactivarCentroAtencionBD(UsuarioBasico usuario,
			DtoDetCaPromocionesOdo dtoCa) {
		logger.info("\n\n\n\n Modificando Objetos CENTRO ATENCION \n\n");
		dtoCa.setActivo(ConstantesBD.acronimoNo);
		dtoCa.setUsuarioInactivacion(usuario.getLoginUsuario());
		dtoCa.setHoraInactivacion(UtilidadFecha.getHoraActual());
		dtoCa.setFechaInactivacion(UtilidadFecha.getFechaActual());
		dtoCa.setUsuarioModifica(usuario.getLoginUsuario());
		dtoCa.setFechaModifica(UtilidadFecha.getFechaActual());
		dtoCa.setHoraModifica(UtilidadFecha.getHoraActual());
		DetCaPromocionesOdo.modificar(dtoCa);
	}
	
	
	

	/**
	 * ACCION ACTIVAR CENTRO DE ATENCION BD
	 */
	private void accionActivarCentroAtencionBD(UsuarioBasico usuario,
			DtoDetCaPromocionesOdo dtoCa) {
		logger.info("\n\n\n\n Modificando Objetos CENTRO ATENCION \n\n");
		dtoCa.setActivo(ConstantesBD.acronimoSi);
		dtoCa.setUsuarioInactivacion(usuario.getLoginUsuario());
		dtoCa.setHoraInactivacion(UtilidadFecha.getHoraActual());
		dtoCa.setFechaInactivacion(UtilidadFecha.getFechaActual());
		dtoCa.setUsuarioModifica(usuario.getLoginUsuario());
		dtoCa.setFechaModifica(UtilidadFecha.getFechaActual());
		dtoCa.setHoraModifica(UtilidadFecha.getHoraActual());
		DetCaPromocionesOdo.modificar(dtoCa);
	}

	
	
	/**
	 * GUARDA LOS CONVENIOS
	 * 
	 * @param forma
	 * @param usuario
	 * @param codigoDetalle
	 */
	private void accionGuardarConvenios(PromocionesOdontologicasForm forma,
			UsuarioBasico usuario, int codigoDetalle) {
		for (DtoDetConvPromocionesOdo dtoConv : forma.getListaDetConvenios()) {
			if ((dtoConv.isVisible()) && (dtoConv.isEstadoDb() == false)) {
				logger.info("\n\n\n\n  Guardando Convenios");
				dtoConv.setDetPromocionOdo(codigoDetalle);
				dtoConv.setActivo(ConstantesBD.acronimoSi);
				dtoConv.setFechaModifica(UtilidadFecha.getFechaActual());
				dtoConv.setHoraModifica(UtilidadFecha.getHoraActual());
				dtoConv.setUsuarioModifica(usuario.getLoginUsuario());

				DetConvPromocionesOdo.guardar(dtoConv);
			}

			else if ((dtoConv.isVisible() == false) && (dtoConv.isEstadoDb())) {
				logger.info("\n\n\n Modificando convenios Convenios \n");

				dtoConv.setActivo(ConstantesBD.acronimoNo);
				dtoConv.setUsuarioInactivacion(usuario.getLoginUsuario());
				dtoConv.setHoraInactivacion(UtilidadFecha.getHoraActual());
				dtoConv.setFechaInactivacion(UtilidadFecha.getFechaActual());
				DetConvPromocionesOdo.modificar(dtoConv);
			} else if ((dtoConv.isVisible() == true) && (dtoConv.isEstadoDb())) {
				dtoConv.setActivo(ConstantesBD.acronimoSi);
				dtoConv.setUsuarioInactivacion(usuario.getLoginUsuario());
				dtoConv.setHoraInactivacion(UtilidadFecha.getHoraActual());
				dtoConv.setFechaInactivacion(UtilidadFecha.getFechaActual());
				DetConvPromocionesOdo.modificar(dtoConv);
			}
		}
	}

	
	
	
	/**
	 * ACCION GUARDAR CENTRO DE ATENCION. APLICA PARA TODOS CENTROS DE ATENCION
	 * 
	 * @param usuario
	 * @param codigoDetalle
	 */
	private void accionGuardarAplicaTodosCentroAtencion(UsuarioBasico usuario,
			int codigoDetalle) {
		DtoDetCaPromocionesOdo newDto = new DtoDetCaPromocionesOdo();
		newDto.setDetPromocionOdo(codigoDetalle);
		ArrayList<DtoDetCaPromocionesOdo> newLisCA = DetCaPromocionesOdo
				.cargar(newDto);

		if (newLisCA.size() > 0) {
			logger.info("\n\n\n\n ERROR NO DEBEN EXISTIR REGISTROS \n\n\n");
		} else {
			logger
					.info(" \n\n\n\n\n GUARDAR-- APLICA PARA TODOS LOS CENTROS DE ATENCION  \n\n\n\n");
			newDto.setActivo(ConstantesBD.acronimoSi);
			newDto.setUsuarioModifica(usuario.getLoginUsuario());
			newDto.setFechaModifica(UtilidadFecha.getFechaActual());
			newDto.setHoraModifica(UtilidadFecha.getHoraActual());
			DetCaPromocionesOdo.guardar(newDto);
		}
	}

	
	
	
	
	
	/**
	 * ACCION ELIMINAR Y INACTIVAR CONVENIOS. FUNCIONA PARA LAS LISTA QUE SE
	 * ENCUENTRAN EN MEMORIA
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionAdaptarConvenios(ActionMapping mapping,
			PromocionesOdontologicasForm forma) {
		// Adaptamos el convenio a la lista
		DtoDetConvPromocionesOdo dtoDetConv = new DtoDetConvPromocionesOdo();
		dtoDetConv.getConvenio().setCodigo(forma.getDtoConvPromociones().getConvenio().getCodigo());
		dtoDetConv.getConvenio().setNombre(forma.getDtoConvPromociones().getConvenio().getNombre());

		/*
		 * Atributo de validacion
		 */
		boolean bandera = Boolean.TRUE;
		
		/*
		 *Mensaje de Presentacion  
		 */
		forma.setMensajeConvenio("");

		/*
		 * Filtrar el convenio si aplica para todos o no. 
		 */
		if (0 < dtoDetConv.getConvenio().getCodigo()) 
		{
			
			for (DtoDetConvPromocionesOdo dtoConv : forma.getListaDetConvenios()) 
			{
				if(dtoConv.getConvenio().getCodigo()==0) // si ya seleccion aplica para todos 
				{
					forma.setMensajeConvenio(" La Promocion aplica para todos "); // Mensaje de presentacion
					return mapping.findForward("listaConveniosPromocion"); 
				}
				
				bandera = accionValidarSiExistenConveniosRepetidos(forma,bandera, dtoConv); 
			}
			
			if (bandera) {
				forma.getListaDetConvenios().add(dtoDetConv); //adaptar a la lista de convenios 
			}
		}
		/*
		 *Si aplica Para todos
		 *Validar si el convenio aplica para todos  
		 */
		else if(dtoDetConv.getConvenio().getCodigo()==0)
		{
			accionValidarSiExistenConvenios(forma, dtoDetConv, bandera);
		}
		
		
		return mapping.findForward("listaConveniosPromocion");
	}





	/**
	 * Metodo que sirve para verficar que no existan convenios repetidos
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 * @param bandera
	 * @param dtoConv
	 * @return
	 */
	private boolean accionValidarSiExistenConveniosRepetidos(
			PromocionesOdontologicasForm forma, boolean bandera,
			DtoDetConvPromocionesOdo dtoConv) {
		if (dtoConv.getConvenio().getCodigo() == forma.getDtoConvPromociones().getConvenio().getCodigo()) 
		{
			if (dtoConv.isEstadoDb()) 
			{
				
				if (dtoConv.isVisible()) 
				{
					bandera = Boolean.FALSE;
					forma.setMensajeConvenio(" El Convenio Ya Existe");
					
				} 
				else 
				{
					bandera = Boolean.FALSE;
					dtoConv.setVisible(Boolean.TRUE);
				}
				
			} 
			else 
			{
				bandera = Boolean.FALSE;
				forma.setMensajeConvenio(" El Convenio Ya Existe");
			}
		}
		return bandera;
	}


	/**
	 * Accion Validar si Existe convenios 
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 * @param dtoDetConv
	 * @param bandera
	 */
	private void accionValidarSiExistenConvenios(
													PromocionesOdontologicasForm forma,
													DtoDetConvPromocionesOdo dtoDetConv, 
													boolean bandera) 
	{
	
		for (DtoDetConvPromocionesOdo dtoConv : forma.getListaDetConvenios()) 
		{
			if(dtoConv.isVisible())
			{
				bandera=Boolean.FALSE;
			}
		}
		
		if(bandera)
		{
			forma.getListaDetConvenios().add(dtoDetConv);
		}
		else
		{
			forma.setMensajeConvenio("Existen Convenios");
		}
		
	}
	
	
	

	/**
	 * ACCION INACTIVAR Y ELIMINAR CENTRO DE ATENCION FUNCIONA PARA LAS LISTA
	 * QUE SE ENCUENTRAN EN MEMORIA
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionAdaptarCentros(ActionMapping mapping,PromocionesOdontologicasForm forma) 
	{
		DtoDetCaPromocionesOdo dtoDetCa = new DtoDetCaPromocionesOdo();
		dtoDetCa.getCentroAtencion().setCodigo(forma.getDtoCaPromociones().getCentroAtencion().getCodigo());
		dtoDetCa.getCentroAtencion().setNombre(forma.getDtoCaPromociones().getCentroAtencion().getNombre());
		boolean bandera = Boolean.TRUE;

		forma.setMensajeCentro("");

		if (0 < dtoDetCa.getCentroAtencion().getCodigo()) {
			for (DtoDetCaPromocionesOdo dto : forma.getListaCaPromociones()) {
				if (dto.getCentroAtencion().getCodigo() == dtoDetCa
						.getCentroAtencion().getCodigo()) {
					if (dto.isEstadoDb()) {
						if (dto.isVisible()
								&& dto.getActivo().equals(
										ConstantesBD.acronimoSi)) {
							bandera = Boolean.FALSE;
							forma
									.setMensajeCentro("El Centro de Atencion Ya Existe");
						} else {
							bandera = Boolean.FALSE;// CAMBIAR EL ESTADO A
													// VISIBLE
							dto.setVisible(Boolean.TRUE);
							dto.setActivo(ConstantesBD.acronimoSi);
						}
					} else {
						bandera = Boolean.FALSE;
						forma
								.setMensajeCentro(" El Centro de Atencion ya Existe");
					}
				}
			}

			if (bandera) {
				dtoDetCa.setVisible(Boolean.TRUE);
				dtoDetCa.setActivo(ConstantesBD.acronimoSi);
				forma.getListaCaPromociones().add(dtoDetCa);
			}

		}

		return mapping.findForward("listaCentroAtencion");
	}

	
	
	
	/**
	 * CARGAR CENTROS DE ATENCION
	 * POR TRES TIPO DE CLASIFICACION
	 * 1. Region centro atencion 
	 * 2. Cuidad Centro Atencion
	 * 3. Categoria Centro Atencion 
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCargarCentroAtencion(ActionMapping mapping,
			PromocionesOdontologicasForm forma, UsuarioBasico usuario) {

		logger.info("\n\n\n\n\n\n\n\n\n\n\n CIUDAD"+ forma.getDtoDetPromocion().getCiudadCentroAtencion());
		logger.info("**********************************************************************************");
		
		DtoCentrosAtencion dto = new DtoCentrosAtencion();
		dto.setActivo(Boolean.TRUE);
		
		forma.setListaCentrosAtencion(new ArrayList<DtoCentrosAtencion>());
		
		
		/*
		 * REGION CENTRO DE ATENCION
		 */
		if (forma.getDtoDetPromocion().getRegionCentroAtencion() > 0) 
		{
			dto.setRegionCobertura(forma.getDtoDetPromocion().getRegionCentroAtencion());
			
			if(forma.getDtoDetPromocion().getCategoriaCentroAtencion()>0)
			{
				dto.setCategoriaAtencion(forma.getDtoDetPromocion().getCategoriaCentroAtencion());
			}
			
			forma.setListaCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(dto));
		}
		
		/*
		 * CUIDAD CENTRO DE ATENCION
		 */
		if (!UtilidadTexto.isEmpty(forma.getDtoDetPromocion().getCiudadCentroAtencion())) 
		{
			
			if(forma.getDtoDetPromocion().getCategoriaCentroAtencion()>0)
			{
				dto.setCategoriaAtencion(forma.getDtoDetPromocion().getCategoriaCentroAtencion());
			}
			
			arreglarFormatoCiudad(forma); // arreglar
			dto.setCiudad(forma.getTmpCodigoCiudad());
			dto.setDepartamento(forma.getTmpCodigoDepartamento());
			dto.setPais(forma.getTmpCodigoPais());
			
			forma.setListaCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(dto));
		}
		/*
		 * CATEGORIA CENTRO ATENCION
		 */
		if(forma.getDtoDetPromocion().getCategoriaCentroAtencion()>0)
		{	
			arreglarFormatoCiudad(forma); // arreglar
			dto.setCiudad(forma.getTmpCodigoCiudad());
			dto.setDepartamento(forma.getTmpCodigoDepartamento());
			dto.setPais(forma.getTmpCodigoPais());
			if (forma.getDtoDetPromocion().getRegionCentroAtencion() > 0) 
			{
				dto.setRegionCobertura(forma.getDtoDetPromocion().getRegionCentroAtencion());
			}
			
			forma.setListaCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(dto));
		}
		
		// cambiarEstadoCentroAtencion(forma, usuario); //borrar
		forma.setEstado("cargarCentroAtencion");
		return mapping.findForward("empezarDetalle");
	}

	
	
	
	
	
	
	/**
	 * 
	 * @param forma
	 */
	private void validarCargaCentroAtencion(PromocionesOdontologicasForm forma) {

		DtoCentrosAtencion dto = new DtoCentrosAtencion();


		if (forma.getDtoDetPromocion().getRegionCentroAtencion() > 0) 
		{
			
			if(forma.getDtoDetPromocion().getCategoriaCentroAtencion()>0)
			{
				dto.setCategoriaAtencion(forma.getDtoDetPromocion().getCategoriaCentroAtencion());
			}
			dto.setRegionCobertura(forma.getDtoDetPromocion().getRegionCentroAtencion());
			forma.setListaCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(dto));
			
		} 
		else if (UtilidadTexto.isEmpty(forma.getDtoDetPromocion().getCiudadCentroAtencion())) 
		{
			if(forma.getDtoDetPromocion().getCategoriaCentroAtencion()>0)
			{
				dto.setCategoriaAtencion(forma.getDtoDetPromocion().getCategoriaCentroAtencion());
			}
			
			
			dto.setCiudad(forma.getTmpCodigoCiudad());
			dto.setDepartamento(forma.getTmpCodigoDepartamento());
			dto.setPais(forma.getTmpCodigoPais());
			
			dto.setActivo(Boolean.TRUE);
			forma.setListaCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(dto));
		}
	}

	
	
	


	// ////<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ENCABEZADO DE  PROMOCION <<<<<<<<<>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>><
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(ActionMapping mapping,	PromocionesOdontologicasForm forma, UsuarioBasico usuario) 
	{
		logger.info("Guardar Datos de la Promocion");
		forma.getDtoPromocion().setFechaModificada(	UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		forma.getDtoPromocion().setHoraModificada(UtilidadFecha.getHoraActual());
		forma.getDtoPromocion().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getDtoPromocion().setFechaGeneracion(UtilidadFecha.getFechaActual());
		forma.getDtoPromocion().setInstitucion(usuario.getCodigoInstitucionInt());
		
		double tmpcodigoPkPromcion = PromocionesOdontologicas.guardar(forma.getDtoPromocion());
		forma.getDtoPromocion().setPromocionOdontologica((int)tmpcodigoPkPromcion);
		PromocionesOdontologicas.guardarLog(forma.getDtoPromocion());
		forma.reset();
		forma.setListaPromociones(PromocionesOdontologicas.cargar(forma.getDtoPromocion()));
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionguardarModificar(ActionMapping mapping,	PromocionesOdontologicasForm forma, UsuarioBasico usuario) {
		
		logger.info("\n\n\n MODIFICANDO PROMOCION ODONTOLOGICA \n");
		PromocionesOdontologicas.modificar(forma.getDtoPromocion());
		

		forma.getDtoPromocion().setPromocionOdontologica(forma.getDtoPromocion().getCodigoPk());
		

		PromocionesOdontologicas.guardarLog(forma.getDtoPromocion());
		forma.reset();
		forma.setListaPromociones(PromocionesOdontologicas.cargar(forma.getDtoPromocion()));
		forma.setEstado("resumen");
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionModificar(ActionMapping mapping,
			PromocionesOdontologicasForm forma) {
		forma.setDtoPromocion(forma.getListaPromociones().get(
				forma.getPosArray()));
		return mapping.findForward("paginaPrincipal");
	}

	
	
	
		
	/**
	 * ACCION ELIMINAR
	 * 
	 * @param forma
	 */
	private ActionForward accionEliminar(PromocionesOdontologicasForm forma,ActionMapping mapping, UsuarioBasico usuario,
			HttpServletRequest request) 
	
	{
		DtoPromocionesOdontologicas dtoEliminar = new DtoPromocionesOdontologicas();
		logger.info("\n\n\n\n");
		dtoEliminar.setCodigoPk(forma.getListaPromociones().get(forma.getPosArray()).getCodigoPk());

		DtoDetPromocionOdo dto = new DtoDetPromocionOdo();
		dto.setPromocionOdontologia(dtoEliminar.getCodigoPk());

		ActionErrors errores = new ActionErrors();

		if (DetPromocionesOdontolgicas.cargar(dto).size() > 0) 
		{
			logger.info("EN CUENTRA DETALLE");
			errores.add("",	new ActionMessage("errors.notEspecific","No se Permite Eliminar Encabezado De La Promocion, Tiene Detalle  "));
			saveErrors(request, errores);
			return accionEmpezar(mapping, forma, usuario);
		} 
		else 
		{
			logger.info("NO ENCUENTRA DETALLE");
			PromocionesOdontologicas.eliminar(dtoEliminar);
			forma.setDtoPromocion(forma.getListaPromociones().get(forma.getPosArray()));
			forma.getDtoPromocion().setPromocionOdontologica(forma.getDtoPromocion().getCodigoPk());
			PromocionesOdontologicas.guardarLog(forma.getDtoPromocion());
		}
		return accionEmpezar(mapping, forma, usuario);
	}
	
	
	

	/**
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionNuevo(ActionMapping mapping,
			PromocionesOdontologicasForm forma) {
		forma.reset();
		forma.setEstado("nuevo");
		forma.setListaPromociones(PromocionesOdontologicas.cargar(forma
				.getDtoPromocion()));
		return mapping.findForward("paginaPrincipal");
	}

	
	
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping,	PromocionesOdontologicasForm forma, UsuarioBasico usuario) 
	{
		forma.reset();
		accionDefinirParametroUtilizacionProgramasServicios(forma, usuario);
		forma.setListaPromociones(PromocionesOdontologicas.cargar(forma.getDtoPromocion()));
		return mapping.findForward("paginaPrincipal");
	}


	
	
	// <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<-----------------DETALLE DE PROMOCION------>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<
	/**
	 * 
	 */
	private ActionForward accionEmpezarDetalle(ActionMapping mapping,PromocionesOdontologicasForm forma, UsuarioBasico usuario,	InstitucionBasica institucionBasica) 
	{
		
	

		forma.setDtoPromocion(forma.getListaPromociones().get(forma.getPosArray()));
		DtoDetPromocionOdo dtoDet = new DtoDetPromocionOdo();
		dtoDet.setPromocionOdontologia(forma.getDtoPromocion().getCodigoPk());
		forma.setDtoDetPromocion(new DtoDetPromocionOdo());// LIMPIAR
		forma.setDtoDetPromocion(DetPromocionesOdontolgicas.cargarObjeto(dtoDet));

		if (forma.getDtoDetPromocion().getCodigoPk() > 0) 
		{
		
			// Cargamos los Centros de atencion
			DtoDetCaPromocionesOdo dtoCa = new DtoDetCaPromocionesOdo();
			dtoCa.setDetPromocionOdo(forma.getDtoDetPromocion().getCodigoPk());
			forma.setListaCaPromociones(DetCaPromocionesOdo.cargar(dtoCa));
			
			// Cargamos los Convenios
			DtoDetConvPromocionesOdo dtoConv = new DtoDetConvPromocionesOdo();
			dtoConv.setDetPromocionOdo(forma.getDtoDetPromocion().getCodigoPk());
			ArrayList<DtoDetConvPromocionesOdo>  listTmp=DetConvPromocionesOdo.cargar(dtoConv);
		
			if(listTmp.size()>0)
			{
				forma.setListaDetConvenios(listTmp);
			}
			/*else
			{
				DtoDetConvPromocionesOdo dtoDetConv = new DtoDetConvPromocionesOdo();
				dtoDetConv.getConvenio().setCodigo(0);
				dtoDetConv.getConvenio().setNombre("Todos");
				forma.getListaDetConvenios().add(dtoDetConv);
			}*/

			this.cargarEstructuras(forma, usuario, institucionBasica);
			this.validarCargaCentroAtencion(forma);

		}// FIN SI
		else 
		{
			logger.info("No Existen datos");
			forma.getDtoDetPromocion().setEstadoCivil("");//limpiar
			this.cargarEstructuras(forma, usuario, institucionBasica);
		}

		String listado = "";

		
		/*
		 * NO SE PUEDEN REPERTIR PROGRAMAS
		 */
		if (forma.getListaDetPromociones().size() > 0) {
			for (int w = 0; w < forma.getListaDetPromociones().size(); w++) 
			{
					if (w > 0) 
					{
						listado += ",";
					}
					
					if (forma.getTipodeRelacion().equals("Programa")) 
					{
						
						listado += forma.getListaDetPromociones().get(w).getProgramaOdontologico().getCodigo()+ "";
					}
					else 
					{
						listado += forma.getListaDetPromociones().get(w).getServicio().getCodigo()+ "";
					}
			}
		}
		
		
		forma.setCodigos(listado);

		
		logger.info("Estado----->" + forma.getEstado());
		logger.info("EmpezarDetalle");
		return mapping.findForward("empezarDetalle");
	}

	
	
	
	
	
	
	
	
	/*------------Cargar>
	 * CARGAR TODAS LAS LISTAS
	 * @param forma
	 * @param usuario
	 * @param institucionBasica
	 */
	private void cargarEstructuras(PromocionesOdontologicasForm forma,	UsuarioBasico usuario, InstitucionBasica institucionBasica) {

		logger.info("Cargar Estructura-------------------------------------_>");

		// if(forma.getDtoDetPromocion().get)

		// Cargamos las Regiones
		DtoRegionesCobertura dtoRegiones = new DtoRegionesCobertura();
		dtoRegiones.setInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
		forma.setArrayRegiones(RegionesCobertura.cargar(dtoRegiones));

		// cargamos las Categorias
		DtoCategoriaAtencion dtoCategorias = new DtoCategoriaAtencion();
		dtoCategorias.setInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
		forma.setArrayCategorias(CategoriaAtencion.cargar(dtoCategorias));

		// --------> Pais
		Connection con = UtilidadBD.abrirConexion();
		logger.info("Objeto con Info ");
		logger.info("institucion-----------" + institucionBasica.getPais());
		logger.info(Utilidades.obtenerCiudadesXPais(con, institucionBasica.getCodigoPais()));

		// Cargamos los convenios
		
		forma.setCiudadesMap(Utilidades.obtenerCiudadesXPais(con,	institucionBasica.getCodigoPais()));
		forma.setListConvenios(Utilidades.obtenerConvenios("",""/* tipoContrato */,	true/* verificarVencimientoContrato */,	""/* fechaReferencia */,true/* activo */,ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico,"", ConstantesBD.acronimoSi /* * Maneja * Promociones*/));
		
		forma.setEstadosCiviles(Utilidades.consultarEstadosCiviles(con));
		forma.setOcupaciones(Utilidades.consultarOcupaciones(con));
		UtilidadBD.closeConnection(con);
	}

	
	
	// ------------------------------>Ordenar---------------------
	/**
	 * ORDENAR GENERICO
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionOrdenar(ActionMapping mapping,
			PromocionesOdontologicasForm forma, UsuarioBasico usuario) {

		logger.info("patron->" + forma.getPatronOrdenar());
		logger.info("des -->" + forma.getEsDescendente());

		boolean ordenamiento = false;

		if (forma.getEsDescendente().equals(
				forma.getPatronOrdenar() + "descendente")) {

			ordenamiento = true;
		}

		SortGenerico sortG = new SortGenerico(forma.getPatronOrdenar(),
				ordenamiento);

		Collections.sort(forma.getListaPromociones(), sortG);
		return mapping.findForward("paginaPrincipal");

	}

	
	
	/*
	 * -<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< VALIDACIONES DETALLE PROMOCIONES ODONTOLOGICAS  >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><<<
	 */
	
	
	
	
	/**
	 * ACCION VALIDAR EDAD FINAL Y FINAL
	 * @param errores
	 * @param forma
	 */
	private void validarEdad(ActionErrors errores,	PromocionesOdontologicasForm forma) 
	{
		//
		if ( 	(forma.getDtoDetPromocion().getEdadInicial() > 1) && (forma.getDtoDetPromocion().getEdadFinal() == 0)	) 
		{
			errores.add("", new ActionMessage("errors.required","La Edad Final"));
		}
		
		//
		if (forma.getDtoDetPromocion().getEdadInicial() > forma.getDtoDetPromocion().getEdadFinal()) 
		{
			errores.add("", new ActionMessage("errors.notEspecific", "La Edad Final "+forma.getDtoDetPromocion().getEdadFinal()+" Debe ser Mayor Que La Edad Inicial "+ forma.getDtoDetPromocion().getEdadInicial()));
		}
		
		
		// 
		if( (forma.getDtoDetPromocion().getEdadFinal()>0)  && (forma.getDtoDetPromocion().getEdadInicial()==0) )
		{
			errores.add("", new ActionMessage("errors.notEspecific", " La Edad Inicial No Puede Ser Cero"));
		}
	}

	
	
	/**
	 * 
	 * @param errores
	 * @param forma
	 */
	private void porcentajeHonorarios(ActionErrors errores,	PromocionesOdontologicasForm forma) 
	{
		
		if (forma.getDtoDetPromocion().getPorcentajeHonorarios() > 0) 
		{
			if (forma.getDtoDetPromocion().getPorcentajeHonorarios() > 100) 
			{
				errores.add("", new ActionMessage("errors.notEspecific", " El Porcentaje  Honorario debe estar entre 1 - 100%"));
			}
		}
	}


	
	/**
	 * 
	 * @param errores
	 * @param forma
	 */
	private void valorPromocionPorcentaje(ActionErrors errores,		PromocionesOdontologicasForm forma) 
	{
	
		
		if ((forma.getDtoDetPromocion().getPorcentajeDescuento() <= 0) && (forma.getDtoDetPromocion().getValorDescuento() <= 0)) 
		{
			errores.add("", new ActionMessage("errors.required"," El Valor de Descuento ó el Porcentaje, (Son Excluyentes) "));
		}

		
		if (forma.getDtoDetPromocion().getPorcentajeDescuento() > 0) 
		{
			if (forma.getDtoDetPromocion().getPorcentajeDescuento() > 100) 
			{
				errores.add("", new ActionMessage("errors.notEspecific", " El Porcentaje Descuento debe estar entre 1 - 100% "));
				
			}
		}
		
	}

	
	
	
	
	
	
	
	
	/**
	 * <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>><
	 */
	
	/**
	 * ACCION CARGAR PAISES
	 */
	void cargarPais(PromocionesOdontologicasForm forma) {
		forma.setListaPaises(Utilidades.obtenerPaises());
	}

	
	
	
	/**
	 * CARGAR LAS CIUDADES DEL PAIS RETORNA UN XML CON LAS CIUDADES
	 * 
	 * @param forma
	 * @param response
	 * @return
	 */
	private void accionCargarPaisXCiudad(PromocionesOdontologicasForm forma,	HttpServletResponse response) 
	{
		
		String resultado = "<respuesta>";
		String codigoPais = "";
		/**
		 * CARGAR CIUDADES DE UN PAIS
		 */
		logger.info("CODIGO PAIS---------------" + forma.getTmpCodigoPais());

		forma.setCiudadesMap(Utilidades.obtenerCiudadesXPais(forma.getTmpCodigoPais()));
		
		ArrayList<HashMap<String, Object>> arregloAux = new ArrayList<HashMap<String, Object>>();
		arregloAux = forma.getCiudadesMap();

		
		/**
		 * NOTA.
		 * SEGUN EL DIESEÑO DE TABLAS,
		 * ES NECESARIO UNIR EL CODIGO DE LA CUIDAD CON EL CODIGO DEL DEPARTAMENTO
		 *
		 */

		for (int i = 0; i < arregloAux.size(); i++) {
			HashMap elemento = (HashMap) arregloAux.get(i);
			resultado += "" + "<ciudad>" + 
								"<codigo-ciudad>"+ elemento.get("codigoCiudad") + "</codigo-ciudad>"
								+"<nombre-ciudad>" + elemento.get("nombreCiudad") +"</nombre-ciudad>" + 
								"<codigo-departamento>"+elemento.get("codigoDepartamento")+ "</codigo-departamento>"+ 
								"<nombre-departamento>"+ elemento.get("nombreDepartamento")	+ "</nombre-departamento>" 
							+"</ciudad>";
		}
		resultado += "</respuesta>";
		

		// **********SE GENERA RESPUESTA PARA AJAX EN
		// XML**********************************************
		try {
				response.setContentType("text/xml");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(resultado);
			}
			catch (IOException e) 
			{
				logger.error("Error al enviar respuesta AJAX : " + e);
			}
	}

	
	
	/**
	 * CARGAR CENTRO DE ATENCION POR AJAX RETORNA UN XML CON LOS CENTROS DE
	 * ATENCION
	 * 
	 * @param forma
	 * @param response
	 */
	void cargarCentroAtencion(PromocionesOdontologicasForm forma,HttpServletResponse response) {
	
		logger.info("\n\n\n\n\n\n\n\n---------------------------------------------------------------------->");
		logger.info("CARGAR CENTROS DE ATENCION  \n\n");


		
		
		
		
		DtoCentrosAtencion dto = new DtoCentrosAtencion();
			
		arreglarFormatoCiudad(forma);
		dto.setCiudad(forma.getTmpCodigoCiudad());
		dto.setDepartamento(forma.getTmpCodigoDepartamento());
		dto.setPais(forma.getTmpCodigoPais());
		dto.setRegionCobertura(forma.getDtoDetPromocion().getRegionCentroAtencion());
		dto.setActivo(Boolean.TRUE);

		if (forma.getDtoEmpresa().getCodigo().doubleValue() > 0) 
		{
			dto.setEmpresaInstitucion(forma.getDtoEmpresa().getCodigo().doubleValue());
		}

		logBorrar(forma);

		forma.setListaCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(dto));
		
		

		String resultado = "<respuesta>";	
		
		for (DtoCentrosAtencion dtoCentro : forma.getListaCentrosAtencion()) 
		{
			resultado += "<centro>" + "<codigo-centro>"
					+ dtoCentro.getConsecutivo() + "</codigo-centro>"
					+ "<nombre-centro>" + dtoCentro.getDescripcion()
					+ "</nombre-centro>" + "</centro>";
		}
		resultado += "</respuesta>";
		logger.info(resultado + "\n\n\n\n\n");
		
		try {
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(resultado);
		} 
		catch (IOException e) 
		{
			logger.error("Error al enviar respuesta AJAX : " + e);
		}
	}

	private void logBorrar(PromocionesOdontologicasForm forma) {
		logger.info("TMP CODIGO PAIS*-----------> " + forma.getTmpCodigoPais());
		logger.info(" CODIGO CIUDAD *----------->"+ forma.getDtoDetPromocion().getCiudadCentroAtencion());
		logger.info("codigo ciduda "+forma.getTmpCodigoCiudad()+" codigo Depar"+forma.getTmpCodigoDepartamento());
		logger.info("REGION COBERTURA-------> "+ forma.getDtoDetPromocion().getRegionCentroAtencion());
	}

	
	
	/**
	 * BUSQUEDAD AVANZADA PROMOCIONES
	 * 
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward busquedaAvanzadaPromociones(	PromocionesOdontologicasForm forma, ActionMapping mapping,
			ActionErrors errores, HttpServletRequest request, UsuarioBasico usuario) {
		/**
		 * VALIDACION DE FECHAS
		 */
		if (!validarFechasConsultaAvanzadaPromociones(forma, errores, request)) {
			return mapping.findForward("paginaPrincipal");
		}
		/**
		 * VALIDAR EDADES
		 */
		//this.validarEdad(errores, forma); // TODO FALTA VALIDAR 
		
		/**
		 * REALIZAR LAS CONSULTAS
		 */
		this.consultaAvanzadaPromociones(forma,usuario);
		
		return mapping.findForward("paginaPrincipal");
	}

	
	
	/**
	 * CONSULTA AVANZADA PROMOCIONES ODONTOLOGICAS
	 * 
	 * @param forma
	 */
	private void consultaAvanzadaPromociones(PromocionesOdontologicasForm forma, UsuarioBasico usuario) {
		
		
		logger.info("********************************************************************************************************************");
		arreglarFormatoCiudad(forma);
		forma.getDtoPromocion().setDtoDetPromociones(forma.getDtoDetPromocion());
		forma.getDtoPromocion().getDtoDetPromociones().setCiudadCentroAtencion(forma.getTmpCodigoCiudad());
		forma.getDtoPromocion().getDtoDetPromociones().setDeptoCentroAtencion(forma.getTmpCodigoDepartamento());
		forma.getDtoPromocion().getDtoDetPromociones().setPaisCentroAtencion(forma.getTmpCodigoPais());
		forma.getDtoPromocion().getDtoDetPromociones().setDtoDetCaPromociones(forma.getDtoCaPromociones());
		forma.setListaPromociones(new ArrayList<DtoPromocionesOdontologicas>());
		forma.setListaPromociones(PromocionesOdontologicas.consultaAvanzadaPromociones(forma.getDtoPromocion()));
		logger.info("********************************************************************************************************************");
		if( forma.getListaPromociones().size()>0)
		{
			StringBuilder generarReporte= new StringBuilder();
			armarReporteArchivoPlano(forma, usuario, generarReporte);
			accionConstruirRerporteArchivoPlano(forma, generarReporte);
		}
		
		/*
		forma.getDtoPromocion().setFechaModificada(UtilidadFecha.getFechaActual());
		forma.getDtoPromocion().setUsuarioModifica(usuario.getLoginUsuario());
		forma.getDtoPromocion().setHoraModificada(UtilidadFecha.getHoraActual());
		forma.getDtoPromocion().setInstitucion(usuario.getCodigoInstitucionInt()));
		PromocionesOdontologicas.guardarLogConsulta(forma.getDtoPromocion());
		*/
		
		logger.info("\n\n\n\n\n");
	}

	
	
	/**
	 * arreglarFormatoCiudad
	 * @param forma
	 * @return
	 */
	private void arreglarFormatoCiudad(PromocionesOdontologicasForm forma) {
		
		String[] tmpCodigoCuidadDepartamento = forma.getDtoDetPromocion().getCiudadCentroAtencion().split("-"); // Mala Practica
	
		/**
		 * limpira
		 */
		forma.setTmpCodigoCiudad("");
		forma.setTmpCodigoDepartamento("");
		
		if (tmpCodigoCuidadDepartamento.length ==2){
			forma.setTmpCodigoCiudad(tmpCodigoCuidadDepartamento[0]);
			forma.setTmpCodigoDepartamento(tmpCodigoCuidadDepartamento[1]);
		}else
		if (tmpCodigoCuidadDepartamento.length ==3) 
		{
			forma.setTmpCodigoCiudad(tmpCodigoCuidadDepartamento[0]);
			forma.setTmpCodigoDepartamento(tmpCodigoCuidadDepartamento[1]);
			forma.setTmpCodigoPais(tmpCodigoCuidadDepartamento[2]);
		}
	}

	
	
	/**
	 * METODO PARA ARMAR UN ARCHIVO PLANO
	 * 
	 * armarReporteArchivoPlano
	 * @param forma
	 * @param usuario
	 * @param generarReporte
	 */
	private void armarReporteArchivoPlano(PromocionesOdontologicasForm forma,	UsuarioBasico usuario, StringBuilder generarReporte) 
	{
		generarReporte.append("Características Promociones");
		generarReporte.append("\n");
		generarReporte.append("Fecha de Generación Inicial:,"+forma.getDtoPromocion().getFechaGeneracionInicial()+",Fecha de Generación Final:,"+forma.getDtoPromocion().getFechaGeneracionFinal());
		generarReporte.append("\n");
		
		for(DtoPromocionesOdontologicas  dto: forma.getListaPromociones())
		{
			generarReporte.append("PROMOCION ODONTOLOGICA");
			generarReporte.append("\n");
			generarReporte.append("Nombre Promoción:,"+dto.getNombre()+",");
			generarReporte.append("\n");
			generarReporte.append("Fecha Generación:,"+dto.getFechaGeneracion());
			generarReporte.append("\n");
			generarReporte.append("Fecha Final Vigencia :,"+dto.getFechaFinalVigencia()+",");
			generarReporte.append("Fecha Inicial Vigencia :,"+dto.getFechaInicialVigencia()+",");
			generarReporte.append("\n");
			generarReporte.append("Hora Inicial :,"+dto.getHoraInicialVigencia()+",");
			generarReporte.append("Hora Final :,"+dto.getHoraFinalVigencia()+",");
			generarReporte.append("\n");
			generarReporte.append("INFORMACION GENERAL");
			generarReporte.append("\n");
			generarReporte.append("Región Centro Atención:,"+dto.getDtoDetPromociones().getNombreRegion()+",");
			generarReporte.append("Ciudad Centro Atención:,"+dto.getDtoDetPromociones().getNombreCiudad()+",");
			generarReporte.append("\n");
			generarReporte.append("Categoría Centro Atención:,"+dto.getDtoDetPromociones().getNombreCategoria()+",");
			generarReporte.append("\n");
			generarReporte.append("CENTRO ATENCION");
			generarReporte.append("\n");
			generarReporte.append("Nombre Centro");
			generarReporte.append("\n");
			
			
			
			for( DtoDetCaPromocionesOdo dtoCa : dto.getDtoDetPromociones().getListDetCa())
			{
				generarReporte.append(dtoCa.getCentroAtencion().getNombre());
				generarReporte.append("\n");
			}
			generarReporte.append("CONVENIO");
			generarReporte.append("\n");
			generarReporte.append("Nombre Convenio");
			generarReporte.append("\n");
			
			for(DtoDetConvPromocionesOdo dtoCon : dto.getDtoDetPromociones().getListDetConv())
			{
				generarReporte.append(dtoCon.getConvenio().getNombre());
				generarReporte.append("\n");
			}
			
			generarReporte.append("Programa Odontológico:,"+dto.getDtoDetPromociones().getProgramaOdontologico().getNombre()+"," );
			generarReporte.append("\n");
			generarReporte.append("INFORMACION PACIENTE");
			generarReporte.append("\n");
			generarReporte.append("Edad Inicial:,"+dto.getDtoDetPromociones().getEdadInicial()+",");
			generarReporte.append("Edad Final:,"+dto.getDtoDetPromociones().getEdadFinal()+",");
			generarReporte.append("Sexo:,"+dto.getDtoDetPromociones().getNombreSexo()+",");
			generarReporte.append("Estado Civil:,"+dto.getDtoDetPromociones().getNombreEstadoCivil()+",");
			generarReporte.append("Numero Hijos:, "+dto.getDtoDetPromociones().getNroHijos()+",");
			generarReporte.append("\n");
			generarReporte.append("Ocupación:, "+dto.getDtoDetPromociones().getNombreOcupacion()+",");
			generarReporte.append("\n");
			generarReporte.append("INFORMACION PROMOCION");
			generarReporte.append("\n");
			generarReporte.append("Porcentaje Descuento Promoción:,"+dto.getDtoDetPromociones().getPorcentajeDescuento()+",");
			generarReporte.append("Valor Descuento Promoción:,"+dto.getDtoDetPromociones().getValorDescuento()+",");
			generarReporte.append("\n");
			generarReporte.append("Porcentaje Honorarios:,"+dto.getDtoDetPromociones().getPorcentajeHonorarios()+",");
			generarReporte.append("Valor Honorario:,"+dto.getDtoDetPromociones().getValorHonorario()+",");
			generarReporte.append("\n");
			generarReporte.append("\n");
			generarReporte.append("\n");
			
			
		}
		generarReporte.append("\n");
		generarReporte.append("Usuario:,"+usuario.getNombreUsuario());
		generarReporte.append("\n");
		generarReporte.append("Fecha Generación:,"+UtilidadFecha.getFechaActual());
		generarReporte.append("\n");
		generarReporte.append("Hora:,"+UtilidadFecha.getHoraActual());
		
		generarReporte.append("\n");
		
	}
	
	
	
	/**
	 *ACCION CONSTRUIR REPORTE ARCHIVO PLANO 
	 *UTILIZA LA UTILIDAD LogReporteArchivoPlano
	 * @param forma
	 * @param generarReporte
	 */
	private void accionConstruirRerporteArchivoPlano(PromocionesOdontologicasForm forma, StringBuilder generarReporte) 
	{
		LogReporteArchivoPlano objLogArchivoPlano = new LogReporteArchivoPlano(EmunReporteArchivoPlano.promocionOdontologica.toString());
		objLogArchivoPlano.generaArchivoPlano(generarReporte);
		forma.setRutaArchivoRelativa("../upload/"+objLogArchivoPlano.getNombreArchivo()+LogReporteArchivoPlano.PUNTO_ZIP);
		forma.setRutaArchivoAbsoluta(objLogArchivoPlano.getRuta()+objLogArchivoPlano.getNombreArchivo()+LogReporteArchivoPlano.PUNTO_ZIP);
		forma.setNombreArchivo(LogReporteArchivoPlano.PUNTO_ZIP);
	}

	
	
	
	/**
	 * VALIDAR FECHAS CONSULTA AVANZADA PROMOCIONES
	 * 
	 * @param forma
	 * @param errores
	 * @param request
	 */
	private boolean validarFechasConsultaAvanzadaPromociones(PromocionesOdontologicasForm forma, ActionErrors errores,	HttpServletRequest request) 
	{

		if (UtilidadTexto.isEmpty(forma.getDtoPromocion().getFechaInicialVigencia()) || UtilidadTexto.isEmpty(forma.getDtoPromocion().getFechaFinalVigencia())) 
		{
				errores.add("", new ActionMessage("errors.notEspecific",	" La Fecha Inicial Vigencia y La Fecha Final Vigencia Son Requeridas "));
				saveErrors(request, errores);
				return Boolean.FALSE;
		} 
		else 
		{
			
			if(  (!UtilidadFecha.esFechaValidaSegunAp(forma.getDtoPromocion().getFechaInicialVigencia()))  && (!UtilidadFecha.esFechaValidaSegunAp(forma.getDtoPromocion().getFechaInicialVigencia())) )
			{
				errores.add("", new ActionMessage("errors.notEspecific",	" Formato de Fecha Invalidado "));
				saveErrors(request, errores);
				return Boolean.FALSE;
			}
			else
			{
				
				
				if(!UtilidadFecha.esFechaValidaSegunAp(forma.getDtoPromocion().getFechaInicialVigencia()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", forma.getDtoPromocion().getFechaInicialVigencia()));
					saveErrors(request, errores);
					return Boolean.FALSE;
				}
				
				
				if(!UtilidadFecha.esFechaValidaSegunAp(forma.getDtoPromocion().getFechaFinalVigencia()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", forma.getDtoPromocion().getFechaFinalVigencia()));
					saveErrors(request, errores);
					return Boolean.FALSE;
				}
				
				
				if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getDtoPromocion().getFechaInicialVigencia(), forma.getDtoPromocion().getFechaFinalVigencia())) 
				{
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", " Inicial Vigencia "+ forma.getDtoPromocion().getFechaInicialVigencia(), "final Vigencia"+ forma.getDtoPromocion().getFechaFinalVigencia()));
					saveErrors(request, errores);
					return Boolean.FALSE;
				}

				if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma	.getDtoPromocion().getFechaInicialVigencia(), UtilidadFecha.getFechaActual())) 
				{
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", " Inicial Vigencia "+ forma.getDtoPromocion().getFechaInicialVigencia(), "Actual "+ UtilidadFecha.getFechaActual()));
					saveErrors(request, errores);
					return Boolean.FALSE;
				}
			
			}
		}
		
		//*
		if ( !UtilidadTexto.isEmpty(forma.getDtoPromocion().getFechaGeneracionInicial() ))  
		{
				
		
			if( !UtilidadFecha.esFechaValidaSegunAp(forma.getDtoPromocion().getFechaGeneracionFinal()) ) 
			{
				errores.add("", new ActionMessage("errors.notEspecific",	" Formato de Fecha Invalidado "));
				saveErrors(request, errores);
				return Boolean.FALSE;
			}
			
			if(!UtilidadFecha.esFechaValidaSegunAp(forma.getDtoPromocion().getFechaGeneracionInicial()))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", forma.getDtoPromocion().getFechaGeneracionInicial()));
				saveErrors(request, errores);
				return Boolean.FALSE;
			}
				
				
			if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getDtoPromocion().getFechaGeneracionInicial(), forma.getDtoPromocion().getFechaGeneracionFinal())) 
			{
				errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Generación Inicial "+ forma.getDtoPromocion().getFechaGeneracionInicial(), "  Generación Final "+ forma.getDtoPromocion().getFechaGeneracionFinal()));
				saveErrors(request, errores);
				return Boolean.FALSE;
			}
				
			if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma	.getDtoPromocion().getFechaGeneracionInicial(), UtilidadFecha.getFechaActual())) 
			{
				errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", " Generación Inicial "+ forma.getDtoPromocion().getFechaGeneracionInicial(), " Generación Actual "+ UtilidadFecha.getFechaActual()));
				saveErrors(request, errores);
				return Boolean.FALSE;
			}
			
		}// iniciales

		
		
		
		
		if(! UtilidadTexto.isEmpty(forma.getDtoPromocion().getFechaGeneracionFinal()) )
		{

			if(  !UtilidadFecha.esFechaValidaSegunAp(forma.getDtoPromocion().getFechaGeneracionFinal())  ) 
			{
				errores.add("", new ActionMessage("errors.notEspecific",	" Formato de Fecha Invalidado "));
				saveErrors(request, errores);
				return Boolean.FALSE;
			}
			
			
			if(!UtilidadFecha.esFechaValidaSegunAp(forma.getDtoPromocion().getFechaGeneracionFinal()))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", forma.getDtoPromocion().getFechaGeneracionFinal()));
				saveErrors(request, errores);
				return Boolean.FALSE;
			}
			
			
			if ( !UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getDtoPromocion().getFechaGeneracionFinal() , UtilidadFecha.getFechaActual()) ) 
			{
				errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", " Generación Final   "+ forma.getDtoPromocion().getFechaInicialVigencia(), "Actual "+ UtilidadFecha.getFechaActual()));
				saveErrors(request, errores);
				return Boolean.FALSE;
			}
			
			
		}//final
		
	
		return Boolean.TRUE;
	}

	
	
	
	
//	/**
//	 * 
//	 * @param forma
//	 */
//	private void cargarEspecialidadOdontologica(PromocionesOdontologicasForm forma) 
//	{
//		Connection con = UtilidadBD.abrirConexion();
//		forma.setEspecialidadesOdontologia(Utilidades.obtenerEspecialidadesEnArray(con,	ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica+""));
//		UtilidadBD.cerrarObjetosPersistencia(null, null, con);
//	}

	
	
	
	/**
	 * ACCION CARGAR EMPRESA INSTITUCION 
	 * @param forma
	 * @param usuario
	 */
	private void cargarInstitucionEmpresa(PromocionesOdontologicasForm forma,UsuarioBasico usuario) {
		DtoEmpresasInstitucion dto = new DtoEmpresasInstitucion();
		dto.setInstitucion(usuario.getCodigoInstitucionInt());
		
		forma.setListaInstitucionEmpresa(ParametrizacionInstitucion.listaInstitucionEmpresa(dto));
		
	}

}