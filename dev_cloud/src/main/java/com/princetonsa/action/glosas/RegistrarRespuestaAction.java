package com.princetonsa.action.glosas;

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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.actionform.glosas.RegistrarRespuestaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.RegistrarRespuesta;

/**
 * @author Diego Bedoya
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Registrar Respuesta (GLOSAS)
 */
public class RegistrarRespuestaAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private Logger logger = Logger.getLogger(RegistrarRespuestaAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * @author Diego Bedoya
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response ) throws Exception
		{

		Connection con=null;
		try{

			if (response==null); //Para evitar que salga el warning
			if(form instanceof RegistrarRespuestaForm)
			{

				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				//se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//se obtiene el paciente cargado en sesion.
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				//se obtiene la institucion
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				//se instancia la forma
				RegistrarRespuestaForm forma = (RegistrarRespuestaForm)form;		

				//se instancia el mundo
				RegistrarRespuesta mundo = new RegistrarRespuesta();

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				//se instancia la variable para manejar los errores.
				ActionErrors errores=new ActionErrors();

				forma.setMensaje(new ResultadoBoolean(false));

				logger.info("\n\n***************************************************************************");
				logger.info(" 	  EL ESTADO DE REGISTRAR RESPUESTA (GLOSAS) ES ====>> "+estado);
				logger.info("\n***************************************************************************");


				// ESTADO --> NULL
				if(estado == null)
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);

					return mapping.findForward("paginaError");
				}			
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezarArea(forma, mundo, con, mapping, usuario, request);					   			
				}
				else if(estado.equals("buscarGlosa"))
				{
					return this.accionBuscarGlosa(forma, mundo, con, mapping, usuario);					   			
				}
				else if(estado.equals("busquedaAvanzada"))
				{
					return this.accionBusquedaAvanzada(forma, mundo, con, mapping, usuario);					   			
				}
				else if(estado.equals("mostrarRespuesta"))
				{
					return this.accionMostrarRespuesta(forma, mundo, con, mapping, usuario);					   			
				}
				else if(estado.equals("listarFacturasGlosa"))
				{
					return this.accionListarFacturasGlosa(forma, mundo, con, mapping, usuario);					   			
				}
				else if(estado.equals("seleccionarFacturas"))
				{
					return this.accionSeleccionarFacturas(forma, mundo, con, mapping, usuario, request);					   			
				}
				else if(estado.equals("recargarPrincipal"))
				{
					return this.accionRecargarPrincipal(forma, mundo, con, mapping, usuario);					   			
				}
				else if(estado.equals("guardarRespuesta"))
				{
					return this.accionGuardarRespuesta(forma, mundo, con, mapping, usuario);					   			
				}
				else if(estado.equals("detalleFac"))
				{
					return this.accionDetalleFacturaRespuesta(forma, mundo, con, mapping, usuario);
				}			
				else if(estado.equals("guardarDetFac"))
				{
					return this.accionGuardarDetFac(forma, mundo, con, mapping, usuario, errores, request);					   			
				}
				else if(estado.equals("guardarDetFacExt"))
				{
					return this.accionGuardarDetFacExt(forma, mundo, con, mapping, usuario);					   			
				}
				else if(estado.equals("asociosSolicitud"))
				{
					return this.accionAsociosSolicitud(forma, mundo, con, mapping, usuario);					   			
				}
				else if(estado.equals("cerrarPopUpAsocios"))
				{
					return this.accionCerrarPopUpAsocios(forma, mundo, con, mapping, usuario);					   			
				}
				else if(estado.equals("eliminar"))
				{
					return this.accionEliminarFactura(forma, mundo, con, mapping, usuario);					   			
				}
				else if(estado.equals("filtrarFacturasGlosas"))
				{
					return this.accionFiltrarFacturasGlosas(forma, mundo, con, mapping, usuario);					   			
				}
				else if(estado.equals("volver"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleRespGloFac");
				}
				else if(estado.equals("volverListadoFacturas"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("registrarRespuesta");					   			
				}
				else if(estado.equals("adicionarFacturas"))
				{
					if((forma.getInformacionRespuesta("glosasis")+"").equals(""))
						forma.setAdicionarFacturas(false);
					else
						forma.setAdicionarFacturas(true);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("registrarRespuesta");	
				}
				else if(estado.equals("imprimirRespuesta"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("registrarRespuesta");	
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
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionFiltrarFacturasGlosas(RegistrarRespuestaForm forma, RegistrarRespuesta mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) {
				
		forma.setFacturasGlosaMap(mundo.consultaFacturasPorGlosa(con, forma.getInformacionRespuesta("codglosa")+"", forma.getFacturaFiltro(), forma.getFechaElabFiltro()));
		forma.setFacturaFiltro("");
		forma.setFechaElabFiltro("");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoFacturasGlosa");
	}

	/**
	 * Metodo que muestra los asocios por solicitud en un PopUp
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionAsociosSolicitud(RegistrarRespuestaForm forma,RegistrarRespuesta mundo, Connection con, ActionMapping mapping,UsuarioBasico usuario) 
	{
		for(int i=0;i<(Utilidades.convertirAEntero(forma.getAsociosSolicitudesMap("numRegistros")+""));i++)
		{
			forma.setAsociosSolicitudesMap("codconceptor_"+i, forma.getConceptoSol());
		}

		
		if(Utilidades.convertirAEntero(forma.getConceptoSol().split(ConstantesBD.separadorSplit)[1]) == (ConstantesBD.codigoConceptosCarteraDebito))
			forma.setNaturalezaSol("D");
		else if(Utilidades.convertirAEntero(forma.getConceptoSol().split(ConstantesBD.separadorSplit)[1]) == (ConstantesBD.codigoConceptosCarteraCredito))
			forma.setNaturalezaSol("C");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("asociosSolicitud");
	}
	
	/**
	 * Metodo que cierra el pop up asocios y mantiene en memoria los datos
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCerrarPopUpAsocios(RegistrarRespuestaForm forma,RegistrarRespuesta mundo, Connection con, ActionMapping mapping,UsuarioBasico usuario) 
	{
		UtilidadBD.closeConnection(con);
		return mapping.findForward("asociosSolicitud");
	}
	
	/**
	 * Metodo que carga las solicitudes servicio/articulo por la factura seleccionada
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionDetalleFacturaRespuesta(RegistrarRespuestaForm forma,RegistrarRespuesta mundo, Connection con, ActionMapping mapping,UsuarioBasico usuario) 
	{
		forma.setRespuestaDetalleMap(mundo.consultaRespuestaDetalleGlosa(con, forma.getInformacionRespuesta("codglosa")+""));
		if(UtilidadTexto.getBoolean(forma.getRespuestaDetalleMap("sistema_"+forma.getPosicionFac())))
		{
			forma.setSolicitudesFacturaMap(mundo.consultaSolicitudesDetalleFactura(con, forma.getRespuestaDetalleMap("codaudi_"+forma.getPosicionFac())+""));
			forma.setAsociosSolicitudesMap(mundo.consultaAsociosDetalleFactura(con, forma.getRespuestaDetalleMap("codaudi_"+forma.getPosicionFac())+""));			
		}
		else
		{
			forma.setDetalleFacturaExternaMap(mundo.consultaDetalleFacturasExternas(con, forma.getRespuestaDetalleMap("codaudi_"+forma.getPosicionFac())+""));
		}
		forma.setConceptosRespuestaMap(mundo.consultaConceptosRespuesta(con, ConstantesIntegridadDominio.acronimoTipoGlosaRespuesta));

		int n= Utilidades.convertirAEntero(forma.getSolicitudesFacturaMap("numRegistros")+"");
		
		for(int i=0; i<n;i++)
		{
			if(Utilidades.convertirAEntero(forma.getSolicitudesFacturaMap("tieneajuste_"+i)+"") > 0)
			{
				forma.setSolicitudesFacturaMap("modificable_"+i, 0);
			}
		}
				
		
		UtilidadBD.closeConnection(con);
		if(UtilidadTexto.getBoolean(forma.getRespuestaDetalleMap("sistema_"+forma.getPosicionFac())))
			return mapping.findForward("detalleRespGloFac");
		return mapping.findForward("detalleRespGloFacExterna");
	}
	
	/**
	 * Metodo que marca un registro como eliminado en el mapa de detalle facturas
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarFactura(RegistrarRespuestaForm forma,RegistrarRespuesta mundo, Connection con, ActionMapping mapping,UsuarioBasico usuario) 
	{
		int cont=0;
		for(int i=0; i< Utilidades.convertirAEntero(forma.getRespuestaDetalleMap("numRegistros")+"");i++)
		{
			if(i != Utilidades.convertirAEntero(forma.getPosicionFac()) && (forma.getRespuestaDetalleMap("eliminar_"+i)+"").equals(ConstantesBD.acronimoSi))
				cont++;
		}	
			
		if(cont < (Utilidades.convertirAEntero(forma.getRespuestaDetalleMap("numRegistros")+"")))
		{			
			forma.setRespuestaDetalleMap("eliminar_"+forma.getPosicionFac(), ConstantesBD.acronimoNo);
			forma.setMensaje(new ResultadoBoolean(true,"Es requerido el ingreso de por lo menos una Factura para la respuesta."));
		}
		else
			forma.setRespuestaDetalleMap("eliminar_"+forma.getPosicionFac(), ConstantesBD.acronimoSi);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("registrarRespuesta");
	}
		
	/**
	 * Metodo para empezar la funcionalidad
	 * y cargar formulario de busqueda
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarArea(RegistrarRespuestaForm forma, RegistrarRespuesta mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errores=new ActionErrors();
		forma.reset(usuario.getCodigoInstitucionInt());
		
		//***************SE TOMA Y SE VALIDA EL CONSECUTIVO DE RESPUESTA**********************************************************
		String valorConsecutivoRespuestaGlosa=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoRespuestaGlosas, usuario.getCodigoInstitucionInt());
				
		if(!UtilidadCadena.noEsVacio(valorConsecutivoRespuestaGlosa) || valorConsecutivoRespuestaGlosa.equals("-1"))
			errores.add("Falta consecutivo disponible",new ActionMessage("error.glosas.faltaDefinirConsecutivo","el registro de Respuesta"));
		else
		{
			try
			{
				Integer.parseInt(valorConsecutivoRespuestaGlosa);
			}
			catch(Exception e)
			{
				logger.error("Error en validacionConsecutivoDisponibleIngreso:  "+e);
				errores.add("Consecutivo no es entero", new ActionMessage("errors.integer","el consecutivo de la Respuesta Glosa"));
			}
		}
		
		UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoRespuestaGlosas, usuario.getCodigoInstitucionInt(), valorConsecutivoRespuestaGlosa, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
		
		//Si hay algun error con el consecutivo de respuesta se debe manejar el error
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			return mapping.findForward("paginaErroresActionErrors");
		}		
		//***********************************************************************************************************************
			
		UtilidadBD.closeConnection(con);
		return mapping.findForward("registrarRespuesta");
	}
	
	/**
	 * Metodo que busca la glosa y su respuesta por Glosa sistema
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBuscarGlosa(RegistrarRespuestaForm forma, RegistrarRespuesta mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		ActionErrors errors = new ActionErrors();		
		forma.setAdicionarFacturas(false);
		//Se resetean las variables de la busqueda compartida (Avanzada, PorGlosa)
		forma.resetPorBusqueda(usuario.getCodigoInstitucionInt());
		
		//Se carga un mapa encabezado Glosa con datos basicos de la glosa
		forma.setEncabezadoGlosaMap(mundo.consultaEncabezadoGlosa(con, forma.getInformacionRespuesta("glosasis")+"", "Registrar"));
				
		//Si se encontro glosa se carga la forma
		if(Utilidades.convertirAEntero((forma.getEncabezadoGlosaMap("numRegistros")+""))>0)
		{
			forma.setInformacionRespuesta("fechareg", forma.getEncabezadoGlosaMap("fechaRegistroGlosa")+"");
			forma.setInformacionRespuesta("valor", forma.getEncabezadoGlosaMap("valorGlosa")+"");
			forma.setInformacionRespuesta("nomconvenio", forma.getEncabezadoGlosaMap("nombreConvenio")+"");
			forma.setInformacionRespuesta("contrato", forma.getEncabezadoGlosaMap("contrato")+"");
			forma.setInformacionRespuesta("glosaent", forma.getEncabezadoGlosaMap("glosaEntidad")+"");
			forma.setInformacionRespuesta("fechanot", forma.getEncabezadoGlosaMap("fechaNotificacion")+"");
			forma.setInformacionRespuesta("codglosa", forma.getEncabezadoGlosaMap("codigo")+"");
			forma.setInformacionRespuesta("indicativoglosa", forma.getEncabezadoGlosaMap("indicativoglosa")+"");
			
			forma.setInformacionRespuesta("respuesta", forma.getEncabezadoGlosaMap("respuesta")+"");
			if(!(forma.getEncabezadoGlosaMap("conciliacion")+"").equals(""))
				forma.setConciliacion(forma.getEncabezadoGlosaMap("conciliacion")+"");
			forma.setInformacionRespuesta("observresp", forma.getEncabezadoGlosaMap("observacionesresp")+"");
			forma.setInformacionRespuesta("codrespuesta", forma.getEncabezadoGlosaMap("codigoresp")+"");
			forma.setInformacionRespuesta("fecharadresp", forma.getEncabezadoGlosaMap("fecharadresp")+"");

						
			if((forma.getInformacionRespuesta("codrespuesta")+"").equals(""))
				forma.setInformacionRespuesta("fecharesp", UtilidadFecha.getFechaActual());
			else
				forma.setInformacionRespuesta("fecharesp", forma.getEncabezadoGlosaMap("fecharesp")+"");
			
			//Se carga la respuesta asociada a la glosa encontrada si la hay, junto con las facturas de la respuesta
			forma.setRespuestaDetalleMap(mundo.consultaRespuestaDetalleGlosa(con, forma.getInformacionRespuesta("codglosa")+""));
			
			if(Utilidades.convertirAEntero(forma.getRespuestaDetalleMap("numRegistros")+"") > 0)
				forma.setAdicionarFacturas(true);
			
			for(int i=0;i<Utilidades.convertirADouble(forma.getRespuestaDetalleMap()+"");i++)
			{
				forma.setRespuestaDetalleMap("mostrarLink_"+i, "S");
			}
			
			//Se carga el mapa con las facturas segun la glosa que se listo en pantalla
			forma.setFacturasGlosaMap(mundo.consultaFacturasPorGlosa(con, forma.getInformacionRespuesta("codglosa")+"", "", ""));
			
			for(int i=0;i<Utilidades.convertirAEntero(forma.getFacturasGlosaMap("numRegistros")+"");i++)
			{
				for(int j=0;j<Utilidades.convertirAEntero(forma.getRespuestaDetalleMap("numRegistros")+"");j++)
				{
					if((forma.getRespuestaDetalleMap("codfactura_"+j)+"").equals(forma.getFacturasGlosaMap("codfactura_"+i)+""))
						forma.setFacturasGlosaMap("seleccionado_"+i, ConstantesBD.acronimoSi);
				}
			}
		}
		else
		{
			forma.setInformacionRespuesta("glosasis", "");
			errors.add("descripcion",new ActionMessage("prompt.generico","No se encontraron registros para su selecccion."));
		}
			
		forma.setMostrarLink(false);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("registrarRespuesta");
	}
	
	/**
	 * 
	 * @param facturasGlosaMap
	 * @param respuestaDetalleMap
	 * @return
	 */
	private HashMap marcarFacturasSeleccionadas(HashMap facturasGlosaMap, HashMap respuestaDetalleMap) {
		logger.info("Pasa 1");
		for(int i=0; i<Utilidades.convertirAEntero(facturasGlosaMap.get("numRegistros")+""); i++){
			logger.info("Pasa 2");
			for(int j=0; j<Utilidades.convertirAEntero(respuestaDetalleMap.get("numRegistros")+""); j++){
				logger.info("Pasa 3");
				if(facturasGlosaMap.get("codfactura_"+i).toString().equals(respuestaDetalleMap.get("codglosa_"+j)+"")){
					facturasGlosaMap.put("seleccionado_"+i, ConstantesBD.acronimoSi);
					logger.info("Pasa 4");
				}
			}
		}
		return facturasGlosaMap;
	}


	/**
	 * Metodo para empezar la busqueda Avanzada de Respuestas
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBusquedaAvanzada(RegistrarRespuestaForm forma, RegistrarRespuesta mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.setAdicionarFacturas(false);
		//Validaciones para mostrar los convenios
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarUsuarioGlosa(usuario.getCodigoInstitucionInt())))
			forma.setArregloConvenios(UtilidadesFacturacion.obtenerConvenioPorUsuario(con, usuario.getLoginUsuario(), 
				ConstantesIntegridadDominio.acronimoTipoUsuarioGlosa, false));
		else
			forma.setArregloConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", false));
		forma.setConvenioBus("-1");
			
		UtilidadBD.closeConnection(con);
		return mapping.findForward("registrarRespuesta");
	}
	
	/**
	 * Metodo para mostrar la respuesta consultada
	 * y cargar formulario de busqueda
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionMostrarRespuesta(RegistrarRespuestaForm forma, RegistrarRespuesta mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//Se resetean las variables que comparte las busquedas (Avanzada, PorGlosa)
		forma.resetPorBusqueda(usuario.getCodigoInstitucionInt());
		//Se cargan las facturas que tiene asociada la respuesta
		if(!(forma.getInformacionRespuesta("codrespuesta")+"").equals(""))
			forma.setRespuestaDetalleMap(mundo.consultaFacturasRespuestaGlosa(con, forma.getInformacionRespuesta("codrespuesta")+""));
		
		//Se carga el mapa con las facturas segun la glosa que se listo en pantalla
		forma.setFacturasGlosaMap(mundo.consultaFacturasPorGlosa(con, forma.getInformacionRespuesta("codglosa")+"", "", ""));
		
		for(int i=0;i<Utilidades.convertirAEntero(forma.getFacturasGlosaMap("numRegistros")+"");i++)
		{
			for(int j=0;j<Utilidades.convertirAEntero(forma.getRespuestaDetalleMap("numRegistros")+"");j++)
			{
				if((forma.getRespuestaDetalleMap("codaudi_"+j)+"").equals(forma.getFacturasGlosaMap("codaudi_"+i)+""))
					forma.setFacturasGlosaMap("seleccionado_"+i, ConstantesBD.acronimoSi);
			}
		}
		forma.setMostrarLink(false);
		forma.setAdicionarFacturas(true);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("registrarRespuesta");
	}
	
	/**
	 * Metodo para abrir y consultar el pop up de facturas de la glosa
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionListarFacturasGlosa(RegistrarRespuestaForm forma, RegistrarRespuesta mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//Se carga el mapa con las facturas segun la glosa que se listo en pantalla
		forma.setFacturasGlosaMap(mundo.consultaFacturasPorGlosa(con, forma.getInformacionRespuesta("codglosa")+"", "", ""));
						
		for(int i=0;i<Utilidades.convertirAEntero(forma.getFacturasGlosaMap("numRegistros")+"");i++)
		{
			for(int j=0;j<Utilidades.convertirAEntero(forma.getRespuestaDetalleMap("numRegistros")+"");j++)
			{
				if((forma.getRespuestaDetalleMap("codfactura_"+j)+"").equals(forma.getFacturasGlosaMap("codfactura_"+i)))
					forma.setFacturasGlosaMap("mostrar_"+j, ConstantesBD.acronimoNo);
				else
					forma.setFacturasGlosaMap("mostrar_"+j, ConstantesBD.acronimoSi);
			}
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoFacturasGlosa");
	}
	
	/**
	 * Metodo para cargar las facturas seleccionadas
	 * en el mapa facturas respuesta
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionSeleccionarFacturas(RegistrarRespuestaForm forma, RegistrarRespuesta mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errors = new ActionErrors();
		int numRegResp=Utilidades.convertirAEntero(forma.getRespuestaDetalleMap("numRegistros")+"");
		for(int i=0;i<Utilidades.convertirAEntero(forma.getFacturasGlosaMap("numRegistros")+"");i++)
		{			
			if((forma.getFacturasGlosaMap("seleccionado_"+i)+"").equals(ConstantesBD.acronimoSi))
			{				
				for(int j=0;j<Utilidades.convertirAEntero(forma.getRespuestaDetalleMap("numRegistros")+"");j++)
				{
					
					if(Utilidades.convertirAEntero(forma.getRespuestaDetalleMap("codfactura_"+j)+"") == Utilidades.convertirAEntero(forma.getFacturasGlosaMap("codfactura_"+i)+""))
						errors.add("descripcion",new ActionMessage("prompt.generico","La Factura ya Fue Seleccionada."));				
				}
				
				if(!errors.isEmpty())
				{
					UtilidadBD.closeConnection(con);
					forma.setEstado("listarFacturasGlosa");
					saveErrors(request,errors);
				}
				else
				{	
					forma.setRespuestaDetalleMap("codresp_"+numRegResp, forma.getInformacionRespuesta("codrespuesta")+"");
					forma.setRespuestaDetalleMap("respuesta_"+numRegResp, forma.getInformacionRespuesta("respuesta")+"");
					forma.setRespuestaDetalleMap("codglosa_"+numRegResp, forma.getFacturasGlosaMap("codglosa_"+i)+"");
					forma.setRespuestaDetalleMap("factura_"+numRegResp, forma.getFacturasGlosaMap("factura_"+i)+"");
					forma.setRespuestaDetalleMap("codfactura_"+numRegResp, forma.getFacturasGlosaMap("codfactura_"+i)+"");
					forma.setRespuestaDetalleMap("fechaelab_"+numRegResp, forma.getFacturasGlosaMap("fechaelabora_"+i)+"");
					forma.setRespuestaDetalleMap("cuentacobro_"+numRegResp, forma.getFacturasGlosaMap("cxc_"+i)+"");
					forma.setRespuestaDetalleMap("fecharadicacion_"+numRegResp, forma.getFacturasGlosaMap("fecharad_"+i)+"");
					forma.setRespuestaDetalleMap("saldofactura_"+numRegResp, forma.getFacturasGlosaMap("saldofactura_"+i)+"");
					forma.setRespuestaDetalleMap("conceptos_"+numRegResp, forma.getFacturasGlosaMap("conceptos_"+i)+"");
					forma.setRespuestaDetalleMap("valorglosa_"+numRegResp, forma.getFacturasGlosaMap("valorglosafac_"+i)+"");
					forma.setRespuestaDetalleMap("codaudi_"+numRegResp, forma.getFacturasGlosaMap("codaudi_"+i)+"");
					forma.setRespuestaDetalleMap("sistema_"+numRegResp, forma.getFacturasGlosaMap("sistema_"+i)+"");
					forma.setRespuestaDetalleMap("esconsulta_"+numRegResp, ConstantesBD.acronimoNo);
					forma.setRespuestaDetalleMap("eliminar_"+numRegResp, ConstantesBD.acronimoNo);
					numRegResp++;
					forma.setRespuestaDetalleMap("numRegistros", numRegResp);
					
					forma.setAdicionarFacturas(true);
				}
			}
		}	
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoFacturasGlosa");
	}
	
	/**
	 * Metodo que recarga la pagina principal
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionRecargarPrincipal(RegistrarRespuestaForm forma, RegistrarRespuesta mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		UtilidadBD.closeConnection(con);
		return mapping.findForward("registrarRespuesta");
	}
	
	/**
	 * Metodo que almacena o actualiza la respuesta con sus facturas
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarRespuesta(RegistrarRespuestaForm forma, RegistrarRespuesta mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		String operacion="";
		//validacion para saber si ya existe la respuesta para actualizarla o insertar una nueva
		if(!(forma.getInformacionRespuesta("codrespuesta")+"").equals(""))
			operacion="ACTUALIZAR";
		else
			operacion="INSERTAR";
		
		forma.setInformacionRespuesta("conciliacion", forma.getConciliacion());
		
		//se envia a guardar el encabezado de la Respuesta con sus detalles
		//mapa encabezadoGlosa que contiene el encabezado de la respuesta si la hay
		//mapa respuestaDetalle que contiene el detalle de las facturas de la Respuesta
				
		if(Utilidades.convertirAEntero(forma.getRespuestaDetalleMap("numRegistros")+"") > 0)
		{				
			if(mundo.guardarRespuestaConDetalleGlosa(con, forma.getInformacionRespuesta(), forma.getRespuestaDetalleMap(), operacion, usuario))
			{
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
				forma.setMostrarLink(true);
			}
			else
				forma.setMensaje(new ResultadoBoolean(true,"La Respuesta no se actualizó satisfactoriamente."));
		}
		else
			forma.setMensaje(new ResultadoBoolean(true,"Es requerido el ingreso de por lo menos una Factura para la respuesta."));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("registrarRespuesta");
	}
	
	/**
	 * Metodo que guarda el Detalle de Respuesta de Glosa Factura
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param errores 
	 * @param request 
	 * @return
	 */
	private ActionForward accionGuardarDetFac(RegistrarRespuestaForm forma,RegistrarRespuesta mundo, Connection con, ActionMapping mapping,UsuarioBasico usuario, ActionErrors errores, HttpServletRequest request) 
	{
		double valorFactura=0.0;
		
		for (int i=0; i<Utilidades.convertirAEntero(forma.getSolicitudesFacturaMap().get("numRegistros")+"");i++){

			valorFactura+=Utilidades.convertirADouble(forma.getSolicitudesFacturaMap("valorresp_"+i)+"");			
		}
		for (int i=0; i<Utilidades.convertirAEntero(forma.getSolicitudesFacturaMap().get("numRegistros")+"");i++){
			// Validar Concepto
			if(forma.getSolicitudesFacturaMap().get("codconceptor_"+i).equals("") || forma.getSolicitudesFacturaMap().get("codconceptor_"+i).equals(ConstantesBD.codigoNuncaValido+""))
				errores.add("", new ActionMessage("errors.required","Concepto Respuesta en la Solictud "+forma.getSolicitudesFacturaMap().get("solicitud_"+i)));
			
			// Validar Cantidad
			if(forma.getSolicitudesFacturaMap().get("cantidadresp_"+i).equals(""))
				errores.add("", new ActionMessage("errors.required","Cantidad Respuesta en la Solictud "+forma.getSolicitudesFacturaMap().get("solicitud_"+i)));
			
			// Validar Valor
			if(forma.getSolicitudesFacturaMap().get("valorresp_"+i).equals(""))
				errores.add("", new ActionMessage("errors.required","Valor Respuesta en la Solictud "+forma.getSolicitudesFacturaMap().get("solicitud_"+i)));
			
			if(errores.isEmpty())
			{			
				if(valorFactura != Utilidades.convertirAEntero(forma.getValorTotalRta()+""))
				{
					forma.setEsMenor(1);
				}	
			}
		}		
		
		saveErrors(request,errores);
		
		if(!errores.isEmpty()){

			UtilidadBD.closeConnection(con);
			if(UtilidadTexto.getBoolean(forma.getRespuestaDetalleMap("sistema_"+forma.getPosicionFac())))
				return mapping.findForward("detalleRespGloFac");
			return mapping.findForward("detalleRespGloFacExterna");

		} 
		else {

			String generaAjusteResp=ValoresPorDefecto.getGenerarAjusteAutoRegRespuesta(usuario.getCodigoInstitucionInt());
			String generaAjusteRespConc=ValoresPorDefecto.getGenerarAjusteAutoRegRespuesConciliacion(usuario.getCodigoInstitucionInt());
			String generaAjuste="";
			
			
			if(forma.getConciliacion().equals(ConstantesBD.acronimoSi))
			{
				if(generaAjusteRespConc.equals(ConstantesBD.acronimoSi))
					generaAjuste=ConstantesBD.acronimoSi;
				else
					generaAjuste=ConstantesBD.acronimoNo;
			}
			else
			{
				if(generaAjusteResp.equals(ConstantesBD.acronimoSi))
					generaAjuste=ConstantesBD.acronimoSi;
				else
					generaAjuste=ConstantesBD.acronimoNo;
			}			
			
			if(mundo.guardarDetalleFactura(con, forma.getSolicitudesFacturaMap(), forma.getAsociosSolicitudesMap(), usuario, generaAjuste))
			{
				if(forma.getEsMenor() == 0)
					forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
				else
					forma.setMensaje(new ResultadoBoolean(true,"El Total de Respuesta no es Igual al Total de la Glosa. \nOperaciones Realizadas con Exito"));
			}
			else
				forma.setMensaje(new ResultadoBoolean(true,"La Respuesta no se actualizó satisfactoriamente."));
			forma.setEsMenor(0);
			return this.accionDetalleFacturaRespuesta(forma, mundo, con, mapping, usuario);
		}
	}
	
	/**
	 * MEtodo que valida el guardar del detalle Factura Interna
	 * @param forma
	 * @return
	 */
	public ActionErrors validarDetalleFacturaInterna(RegistrarRespuestaForm forma)
	{
		ActionErrors errors = new ActionErrors();
		
		for(int i=0;i<Utilidades.convertirAEntero(forma.getSolicitudesFacturaMap("numRegistros")+"");i++)
		{
			if((forma.getSolicitudesFacturaMap("codconceptor_"+i)+"").equals("-1"))
				errors.add("descripcion",new ActionMessage("errors.required","El Concepto respuesta de la Solicitud "+forma.getSolicitudesFacturaMap("solicitud_"+i)+" del Concepto Glosa "+forma.getSolicitudesFacturaMap("descconcepto_"+i)));
			if(!(forma.getSolicitudesFacturaMap("cantidadresp_"+i)+"").equals(""))
				errors.add("descripcion",new ActionMessage("errors.required","La Cantidad de Respuesta de la Solicitud "+forma.getSolicitudesFacturaMap("solicitud_"+i)+" del Concepto Glosa "+forma.getSolicitudesFacturaMap("descconcepto_"+i)));
			if(Utilidades.convertirADouble(forma.getSolicitudesFacturaMap("valorresp_"+i)+"")<=0)
				errors.add("descripcion",new ActionMessage("errors.required","El Valor de Respuesta de la Solicitud "+forma.getSolicitudesFacturaMap("solicitud_"+i)+" del Concepto Glosa "+forma.getSolicitudesFacturaMap("descconcepto_"+i)));
		}
		
		return errors;
	}
	
	/**
	 * Metodo que guarda el Detalle de Respuesta de Glosa Factura Externa
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarDetFacExt(RegistrarRespuestaForm forma,RegistrarRespuesta mundo, Connection con, ActionMapping mapping,UsuarioBasico usuario) 
	{
		if(mundo.guardarDetalleFacturaExt(con, forma.getDetalleFacturaExternaMap(), usuario))
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		else
			forma.setMensaje(new ResultadoBoolean(true,"La Respuesta no se actualizó satisfactoriamente."));
		return this.accionDetalleFacturaRespuesta(forma, mundo, con, mapping, usuario);
	}
}