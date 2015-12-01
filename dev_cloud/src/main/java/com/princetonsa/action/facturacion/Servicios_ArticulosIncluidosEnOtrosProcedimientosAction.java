package com.princetonsa.action.facturacion;

//ojorevisar el valor de la cantidad en el servicio incluido al momento de adicionar 


import java.sql.Connection;
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
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.actionform.facturacion.Servicios_ArticulosIncluidosEnOtrosProcedimientosForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.Servicios_ArticulosIncluidosEnOtrosProcedimientos;

/**
 * @author Juan Alejandro Cardona
 * @e-mail: jacardona@princetonsa.com
 * @date: Septiembre de 2008
 */

public class Servicios_ArticulosIncluidosEnOtrosProcedimientosAction extends Action {

	Logger logger = Logger.getLogger(Servicios_ArticulosIncluidosEnOtrosProcedimientosAction.class);	//Objeto para manejar los logs de esta clase
	

	/**	 * Método execute del Action	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception	{

		Connection con = null;
		try{
			if (form instanceof Servicios_ArticulosIncluidosEnOtrosProcedimientosForm) {

				con = util.UtilidadBD.abrirConexion();		//Abrimos la conexion con la fuente de Datos

				Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma = (Servicios_ArticulosIncluidosEnOtrosProcedimientosForm) form;
				Servicios_ArticulosIncluidosEnOtrosProcedimientos mundo = new Servicios_ArticulosIncluidosEnOtrosProcedimientos();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				forma.setMensaje(new ResultadoBoolean(false));

				String estado = forma.getEstado();
				logger.warn("[Servicios_ArticulosIncluidosEnOtrosProcedimientosForm]--->Estado: "+estado);

				if(con == null)	{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				else {
					if(estado == null) {
						logger.warn("Estado no valido dentro del flujo estado is NULL");
						request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaError");
					}
					else {

						if(estado.equals("empezar")) {
							return accionEmpezar(con, forma, mundo, usuario, mapping, request);
						}

						else if (estado.equals("redireccion")) { //ESTADO UTILIZADO PARA EL PAGER			    
							UtilidadBD.cerrarConexion(con);
							forma.getLinkSiguiente();
							response.sendRedirect(forma.getLinkSiguiente());
							return null;
						}

						else if(estado.equals("ordenar")) {
							return accionOrdenar(con, forma, mapping);
						}

						else if(estado.equals("nuevoSPpal") || estado.equals("cargarServicio")) {
							UtilidadBD.closeConnection(con);
							return mapping.findForward("nuevo");
						}
						//carga los datos de un servicio ppal seleccionado en el listado
						else if(estado.equals("cargarServiPpal"))	{
							return accionCargarServiPpal(con, forma, mundo, usuario, mapping, request);
						}
						//carga los datos de un servicio includo seleccionado en el listado de un ppal
						else if(estado.equals("cargarServiInclu"))	{
							return accionCargarServiInclu(con, forma, mundo, usuario, mapping, request);
						}

						else if(estado.equals("adicionarServInclu")) {
							return accionAdicionarServInclu(forma,con,mapping, usuario);
						}
						else if(estado.equals("eliminarServInclu")) {
							return accionEliminarServInclu(forma, con, mapping, usuario, mundo, request);
						}
						else if(estado.equals("guardarInsertar")) {
							return accionGuardar(con, forma, mundo, usuario, mapping, request);
						}

						//adiciona un arituclo al listado de sevicios ppales
						else if(estado.equals("adicionarArticulo")) {
							return accionAdicionarArticulo(forma,con,mapping, usuario);
						}
						//elimina un arituclo al listado de sevicios ppales
						else if(estado.equals("eliminarArticulo")) {
							return accionEliminarArticulo(forma, con, mapping, usuario, mundo, request);
						}
						//carga los datos para modificar un servi ppal					
						else if(estado.equals("modificarServiPpal")) {
							return accionCargarModificar(con, forma, mundo, usuario, mapping, request);
						}
						//guarda los datos de la modificacion un servi ppal					
						else if(estado.equals("guardarModificar")) {
							return accionGuardarModificar(con, forma, mundo, usuario, mapping, request);
						}

						// de aqui pa bajo

						//llamado a la interface para agregar nuevo articulo a un servicio incluido
						else if(estado.equals("nuevoArtServInclu")) {
							UtilidadBD.closeConnection(con);
							return mapping.findForward("nuevoartservinclu");
						}

						//adiciona un arituclo al listado de sevicios incluidos
						else if(estado.equals("adicionarArtServinclu")) {
							return accionAdicionarArtServinclu(forma,con,mapping, usuario);
						}

						//elimina un arituclo al listado de sevicios ppales
						else if(estado.equals("eliminarArtServinclu")) {
							return accionEliminarArtServinclu(forma, con, mapping, usuario, mundo, request);
						}

						//carga los datos para modificar un servi incluido					
						else if(estado.equals("modificartServiInclu")) {
							return accionCargarModificarServinclu(con, forma, mundo, usuario, mapping, request);
						}

						//guarda los datos de la modificacion un servi incluido					
						else if(estado.equals("guardarModificartServinclu")) {
							return accionGuardarModificartServinclu(con, forma, mundo, usuario, mapping, request);
						}
						// guarda nuevos articulos para los servicios incluidos al momento de agregar un serv ppal
						else if(estado.equals("guardarInsertartServinclu")) {
							return accionGuardartServinclu(con, forma, mundo, usuario, mapping, request);
						}



						else {
							request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
							UtilidadBD.cerrarConexion(con);
							return mapping.findForward("paginaError");
						}
					}
				}
			}
			else {
				logger.error("El form no es compatible con el form de Servicios_ArticulosIncluidosEnOtrosProcedimientosForm");
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
	
	/**	 * Método que valida la información requerido de los
	 * servicios y articulos incluidos dentro de un servicio principal	 */
	private ActionErrors validarGuardar(Connection con, Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma)
	{
		ActionErrors errores = new ActionErrors();
		//Validaciones al momento de guardar de un registro nuevo
		if(forma.getEstado().equals("guardarInsertar") || forma.getEstado().equals("guardarModificar"))
		{
			//Recorremos el numRegistros del Mapa de Servicio Incluidos con el fin de ver que cantidades vienen vacias
			for(int i=0; i<Utilidades.convertirAEntero(forma.getHasMapaServiIncluServiPpal("numRegistros")+""); i++)
			{
				if((forma.getHasMapaServiIncluServiPpal("cantservinclu_"+i)+"").trim().equals("") || (forma.getHasMapaServiIncluServiPpal("cantservinclu_"+i)+"").trim().equals("null"))
					errores.add("cantidadServicioIncluido", new ActionMessage("errors.required", "La Cantidad del Servicio Incluido "+forma.getHasMapaServiIncluServiPpal("codservinclutarifario_"+i)+" "));
			}
			
			//Recorremos el numRegistros del Mapa de Articulos Incluidos con el fin de ver que cantidades vienen vacias
			for(int i=0; i<Utilidades.convertirAEntero(forma.getHasMapaArtServiPpal("numRegistros")+""); i++)
			{
				if((forma.getHasMapaArtServiPpal("cantartinclu_"+i)+"").trim().equals("") || (forma.getHasMapaArtServiPpal("cantartinclu_"+i)+"").trim().equals("null"))
					errores.add("cantidadArticuloIncluido", new ActionMessage("errors.required", "La Cantidad del Artículo Incluido "+forma.getHasMapaArtServiPpal("codartinclu_"+i)+" "));
			}
		}

		//Validacion para los metodos de agregar y actualizar
		if(forma.getEstado().equals("guardarInsertartServinclu") || forma.getEstado().equals("guardarModificartServinclu")) {
			//Recorremos el numRegistros del Mapa de Articulos Incluidos con el fin de ver que cantidades vienen vacias
			for(int i=0; i<Utilidades.convertirAEntero(forma.getHasMapaArtServiInclu("numRegistros")+""); i++)
			{
				if((forma.getHasMapaArtServiInclu("cantartincluserv_"+i)+"").trim().equals("") || (forma.getHasMapaArtServiInclu("cantartincluserv_"+i)+"").trim().equals("null"))
					errores.add("cantidadArticuloIncluido", new ActionMessage("errors.required", "La Cantidad del Artículo Incluido "+forma.getHasMapaArtServiInclu("codartinclu_"+i)+" "));
			}
		}
		return errores;
	}
	
	
	
	/** Método que carga el servicio principal con sus respectivos servicios incluidos, articulos incluidos 
	 * para realizar la respectiva modificación
	 * @param con, forma, mundo, usuario, mapping, request
	 * @return	 */
	private ActionForward accionCargarModificar(Connection con, Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma, Servicios_ArticulosIncluidosEnOtrosProcedimientos mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		int institucion = usuario.getCodigoInstitucionInt();
		int centroAtencion = usuario.getCodigoCentroAtencion();
		
		
		//obtener el centro de costo
		forma.setFarmaciasMap(Utilidades.obtenerCentrosCosto(institucion,ConstantesBD.codigoTipoAreaSubalmacen+"", true, centroAtencion, false));		
		
		this.accionCargarServiPpal(con, forma, mundo, usuario, mapping, request);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("modificar");
	}

	
	
	/** Método que guarda las modificaciones de los servicios incluidos, 
	 * incluyendo los articulos, servicios y servicio principal
	 * @param forma, mundo, usuario, mapping, request
	 * @return	 */
	private ActionForward accionGuardarModificar(Connection con, Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma, Servicios_ArticulosIncluidosEnOtrosProcedimientos mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores = new ActionErrors();
		errores = this.validarGuardar(con, forma);
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("modificar");
		}
		else
		{
			boolean enTransaccion = false;
			int numResgitrosMapaServiciosIncluidos = Utilidades.convertirAEntero(forma.getHasMapaServiIncluServiPpal("numRegistros")+"");
			int numResgitrosMapaArticulosIncluidos = Utilidades.convertirAEntero(forma.getHasMapaArtServiPpal("numRegistros")+"");
			int codigoProcedimientoIncluido = Utilidades.convertirAEntero(forma.getHasMapaServiArtiIncluOtroProc("codigosys_"+forma.getPosicion())+"");

			logger.info("=====================");
			logger.info("=====================");
			logger.info("===>Código Servicio Principal: "+codigoProcedimientoIncluido);
			
			UtilidadBD.iniciarTransaccion(con);
			//Modificación del Servicio Principal por Activo o no?
			enTransaccion = mundo.actualizarServicioPrincipal(con, forma, usuario);
			if(enTransaccion)
			{
				//Se inserta segundo los servicios incluidos dentro del principal y se evalua si se adiciono correctamente
				if(numResgitrosMapaServiciosIncluidos > 0)
				{
					for(int i=0; i<numResgitrosMapaServiciosIncluidos; i++)
					{
						//Validamos si se debe hacer un UPDATE o un INSERT validando la llave del mapa basedatos
						if((forma.getHasMapaServiIncluServiPpal("basedatos_"+i)+"").equals(ConstantesBD.acronimoSi))
							enTransaccion = mundo.actualizarServiciosIncluidosEnServicioPrincipal(con, Utilidades.convertirAEntero(forma.getHasMapaServiIncluServiPpal("codconservinclu_"+i)+""), Utilidades.convertirAEntero(forma.getHasMapaServiIncluServiPpal("centrocostoseleccionado_"+i)+""), Utilidades.convertirAEntero(forma.getHasMapaServiIncluServiPpal("cantservinclu_"+i)+""));
						else
							enTransaccion = mundo.insertarServiciosIncluidosEnServicioPrincipal(con, codigoProcedimientoIncluido+"", forma.getHasMapaServiIncluServiPpal("codservinclu_"+i)+"", forma.getHasMapaServiIncluServiPpal("centrocostoseleccionado_"+i)+"", forma.getHasMapaServiIncluServiPpal("cantservinclu_"+i)+"");
						
						//Validamos si la adicion del servicio incluido fue satisfactorio o no 
						if(!enTransaccion)
							i = numResgitrosMapaServiciosIncluidos;
					}
				}
				
				//Se inserta segundo los articulos incluidos dentro del principal y se evalua si se adiciono correctamente
				if(numResgitrosMapaArticulosIncluidos > 0)
				{
					for(int j=0; j<numResgitrosMapaArticulosIncluidos; j++)
					{
						//Validamos si se debe hacer un UPDATE o un INSERT validando la llave del mapa basedatos
						if((forma.getHasMapaArtServiPpal("basedatos_"+j)+"").equals(ConstantesBD.acronimoSi))
							enTransaccion = mundo.actualizarArticulosIncluidosEnServicioPrincipal(con, Utilidades.convertirAEntero(forma.getHasMapaArtServiPpal("codconartinclu_"+j)+""), Utilidades.convertirAEntero(forma.getHasMapaArtServiPpal("farmaciaartinclu_"+j)+""), Utilidades.convertirAEntero(forma.getHasMapaArtServiPpal("cantartinclu_"+j)+""));
						else
							enTransaccion = mundo.insertarArticulosIncluidosEnServicioPrincipal(con, codigoProcedimientoIncluido+"", forma.getHasMapaArtServiPpal("codartinclu_"+j)+"", forma.getHasMapaArtServiPpal("farmaciaartinclu_"+j)+"", forma.getHasMapaArtServiPpal("cantartinclu_"+j)+"");
							
						//Validamos si la adicion del articulo incluido fue satisfactorio o no 
						if(!enTransaccion)
							j = numResgitrosMapaArticulosIncluidos;
					}
				}
				
				if(enTransaccion)
				{
					UtilidadBD.finalizarTransaccion(con);
					this.accionEmpezar(con, forma, mundo, usuario, mapping, request);
					forma.setMensaje(new ResultadoBoolean(true,"OPERACIÓN REALIZADA CON ÉXITO"));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else
				{
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("modificar");
				}
			}
			else
			{
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("modificar");
			}
		}
	}

	/** Método que guarda los servicios incluidos, para lo cual se debe ingresar
	 * primero el servicio principal y luego los articulos y servicios incluidos
	 * Además ingresando los articulos incluidos de los servicios incluidos
	 * @param con, forma, mundo, usuario, mapping, request
	 * @return	 */
	private ActionForward accionGuardar(Connection con, Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma, Servicios_ArticulosIncluidosEnOtrosProcedimientos mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{		
		ActionErrors errores = new ActionErrors();
		errores = this.validarGuardar(con, forma);
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			forma.setExitoso(false);
//			forma.setEstado("nuevoSPpal");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("nuevo");
		}
		else
		{
			boolean enTransaccion = false;
			int numResgitrosMapaServiciosIncluidos = Utilidades.convertirAEntero(forma.getHasMapaServiIncluServiPpal("numRegistros")+"");
			int numResgitrosMapaArticulosIncluidos = Utilidades.convertirAEntero(forma.getHasMapaArtServiPpal("numRegistros")+"");
			int codigoProcedimientoIncluido;
			UtilidadBD.iniciarTransaccion(con);
			//Se inserta primero el servicio principal y se evalua si se adiciono correctamente
			enTransaccion = mundo.insertarServicios_ArticulosIncluidosEnOtrosProcedimientos(con, forma, usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt());
			if(enTransaccion)
			{
				codigoProcedimientoIncluido = Utilidades.obtenerCodigoProcedimientoPrincipalIncluidos(con, forma.getCodigoServicio());
				logger.info("==========>");
				logger.info("==========>estado: " + forma.getEstado());
				logger.info("===>Código Procedimiento Incluido: "+codigoProcedimientoIncluido);
				
				
				//Se inserta segundo los servicios incluidos dentro del principal y se evalua si se adiciono correctamente
				if(numResgitrosMapaServiciosIncluidos > 0)
				{
					for(int i=0; i<numResgitrosMapaServiciosIncluidos; i++)
					{
						logger.info("==========>Valor centro de costo: " + forma.getHasMapaServiIncluServiPpal("centrocostoseleccionado_"+i));

						enTransaccion =	mundo.insertarServiciosIncluidosEnServicioPrincipal(con, codigoProcedimientoIncluido+"", forma.getHasMapaServiIncluServiPpal("codservinclu_"+i)+"", forma.getHasMapaServiIncluServiPpal("centrocostoseleccionado_"+i)+"", forma.getHasMapaServiIncluServiPpal("cantservinclu_"+i)+"");
						//Validamos si la adicion del servicio incluido fue satisfactorio o no 
						if(!enTransaccion)
							i = numResgitrosMapaServiciosIncluidos;
					}
				}
				
				//Se inserta tercero los articulos incluidos dentro del principal y se evalua si se adiciono correctamente
				if(numResgitrosMapaArticulosIncluidos > 0)
				{
					for(int j=0; j<numResgitrosMapaArticulosIncluidos; j++)
					{
						enTransaccion = mundo.insertarArticulosIncluidosEnServicioPrincipal(con, codigoProcedimientoIncluido+"", forma.getHasMapaArtServiPpal("codartinclu_"+j)+"", forma.getHasMapaArtServiPpal("farmaciaartinclu_"+j)+"", forma.getHasMapaArtServiPpal("cantartinclu_"+j)+"");
						//Validamos si la adicion del articulo incluido fue satisfactorio o no 
						if(!enTransaccion)
							j = numResgitrosMapaArticulosIncluidos;
					}
				}
				
				if(enTransaccion)
				{
					UtilidadBD.finalizarTransaccion(con);
					//this.accionEmpezar(con, forma, mundo, usuario, mapping, request);
					forma.setMensaje(new ResultadoBoolean(true,"OPERACIÓN REALIZADA CON ÉXITO"));
					forma.setExitoso(true);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("nuevo");
				}
				else
				{
					forma.setExitoso(false);
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("nuevo");
				}
			}
			else
			{
				forma.setExitoso(false);
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("nuevo");
			}
		}
	}

	/** Método inicial de la funcionalidad, la cual se encarga de cargar en un mapa los resultados arrojados
	 * por la consulta de Servicios Articulos Incluidos en Otros Procedimientos
	 * @param (con, forma, mundo, usuario, mapping, request)
	 * @return	 */
	private ActionForward accionEmpezar(Connection con, Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma, Servicios_ArticulosIncluidosEnOtrosProcedimientos mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) {
		forma.reset();
		String codigosPpales = "";		// codigos principales
		String codigosAllInclu = "";	// todos los codigos de los incluidos
		
		String tarifarioServicio = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());

		//se consultan los valores de los servi ppales 
		forma.setHasMapaServiArtiIncluOtroProc(mundo.consultarServicios_ArticulosIncluidosEnOtrosProcedimientos(con, usuario.getCodigoInstitucionInt(), tarifarioServicio));

		int cantRegistros = Utilidades.convertirAEntero(forma.getHasMapaServiArtiIncluOtroProc("numRegistros")+"");

		if(cantRegistros > 0) {
			//se carga los codigos de los ServiPpales utilizados
			for(int i=0; i<cantRegistros; i++)
				codigosPpales += forma.getHasMapaServiArtiIncluOtroProc("codservippal_"+i)+", ";

			
			//se consultan los valores de todos los servi incluidos almacenados en el sistema 
			forma.setHasMapaCodigosTmp(mundo.consultarServIncluUsados(con, 0, 0));
			// cantidad de servicios incluidos
			int cantRegServinclu = Utilidades.convertirAEntero(forma.getHasMapaCodigosTmp("numRegistros")+"");
			if(cantRegServinclu > 0) {
				//se cargan todos los codigos de los ServiIncluidos ya que no pueden ser tenidos en cuenta como ppales 
				for(int i=0; i<cantRegServinclu; i++)
					codigosAllInclu += forma.getHasMapaCodigosTmp("codservinclu_"+i) + ", ";
			}
		}

		forma.setHasMapaCodigosTmp("codigosPpales", codigosPpales);
		forma.setHasMapaCodigosTmp("codigosTodosInclu", codigosAllInclu);
		forma.setHasMapaCodigosTmp("codigosIncluSelec", "");

		forma.setFarmaciasMap(Utilidades.obtenerCentrosCosto(usuario.getCodigoInstitucionInt(),ConstantesBD.codigoTipoAreaSubalmacen+"", true,usuario.getCodigoCentroAtencion(), false));		
		logger.info("====> Codigos Principales Utilizados: "+ forma.getHasMapaServiArtiIncluOtroProc("codServiUtilizados"));

		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	
	/** * Método que ordena el listado mostrado en la vista según el criterio de ordenamiento establecido
	 * @param (con, forma, mapping)
	 * @return	 */
	private ActionForward accionOrdenar(Connection con, Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma, ActionMapping mapping) {
		String[] indices={
				"codigoserviciotarifa_",
				"nameservi_",
				"nameespecialidad_",
				"activo_",
				"espos_"
			};
		int numReg = Integer.parseInt(forma.getHasMapaServiArtiIncluOtroProc("numRegistros")+"");
		forma.setHasMapaServiArtiIncluOtroProc(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getHasMapaServiArtiIncluOtroProc(), numReg));
		forma.setHasMapaServiArtiIncluOtroProc("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());

		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	
	/**	 * Método que carga en una nueva pagina los datos del Servicio Principal el cual válida si el 
	 * ConceptoGeneral está siendo utilizado en otra parte, con el fin de permitir modificar los campos correspondientes
	 * @param (con, forma, mundo, usuario, mapping, request)
	 * @return	 */
	private ActionForward accionCargarServiPpal(Connection con, Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma, Servicios_ArticulosIncluidosEnOtrosProcedimientos mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) {

		int codigoSys = Utilidades.convertirAEntero(forma.getHasMapaServiArtiIncluOtroProc("codigosys_"+forma.getPosicion())+"");
		String codigosAllInclu = "", codigosArtPpal="";
		String tarifarioServicio = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());

		logger.info("======================");
		logger.info("=====> Cargar Modificar");
		logger.info("===>Posición Seleccionada: "+forma.getPosicion());
		logger.info("===>Código Servicio Principal: "+codigoSys);

		
		//Llenamos los HashMap de Articulos Serv Ppal y Servicios Incluidos en el Ppal
		forma.setHasMapaArtServiPpal(mundo.cargarArtIncluServiPpal(con, codigoSys));
		forma.setHasMapaServiIncluServiPpal(mundo.cargarServiIncluServiPpal(con, codigoSys, Utilidades.convertirAEntero(tarifarioServicio),usuario));

		forma.setHasMapaCodigosTmp("codigosTodosInclu", "");	//todos los servicios incluidos del sistema
		forma.setHasMapaCodigosTmp("codigosIncluSelec", "");	//los servicios incluidos seleccionados para un serv ppal
		
		forma.setHasMapaCodigosTmp("codArtPpalTodos", "");	//todos los articulos de un principal
		forma.setHasMapaCodigosTmp("codArtPpalSelec", "");	//los articulos seleccionados para un serv ppal

		int numServiIncluPpal = Utilidades.convertirAEntero(forma.getHasMapaServiIncluServiPpal("numRegistros")+"");
		int numArticulosPpal = Utilidades.convertirAEntero(forma.getHasMapaArtServiPpal("numRegistros")+"");

		//si el ppal tiene servicios incluidos se cargan los codigos de los mismos para que no se seleccionen nuevamente
		if(numServiIncluPpal > 0) {
			for(int i=0; i<numServiIncluPpal; i++)
				codigosAllInclu += forma.getHasMapaServiIncluServiPpal("codservinclu_"+i)+ ", ";
		}

		//si el ppal tiene articulos incluidos se cargan los codigos de los mismos para que no se seleccionen nuevamente
		if(numArticulosPpal > 0) {
			for(int i=0; i<numArticulosPpal ; i++)
				codigosArtPpal += forma.getHasMapaArtServiPpal("codartinclu_"+i)+ ", ";
		}
		
		
		forma.setHasMapaCodigosTmp("codigosTodosInclu", codigosAllInclu);
		forma.setHasMapaCodigosTmp("codArtPpalTodos", codigosArtPpal);	//todos los articulos de un principal
		
		/**
		 * Determino si el servicio principal es de tipo atencion odontologica
		 */
		forma.setEsServicioOdontologico(forma.getHasMapaServiArtiIncluOtroProc("atencionodontologica_"+forma.getPosicion())+"");
		logger.info("\n\n\n\nEL TIPO DE ATENCION DEL SERVICIO ES----->"+forma.getEsServicioOdontologico()+"\n\n\n");
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalle");
	}

	
	/**	 * Método que carga en una nueva pagina los datos del Servicio Incluido  
	 * @param (con, forma, mundo, usuario, mapping, request)
	 * @return	 */
	private ActionForward accionCargarServiInclu(Connection con, Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma, Servicios_ArticulosIncluidosEnOtrosProcedimientos mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
	{
		String codigosArtInclu = "";
		logger.info("===>Posición Servicio De Donde Vengo: " + forma.getPosicion());
		logger.info("===>Posición Servicio Seleccionado: " + forma.getPosServicioSeleccionado());

		int codigoSys = Utilidades.convertirAEntero(forma.getHasMapaServiIncluServiPpal("codconservinclu_"+forma.getPosServicioSeleccionado())+"");
		logger.info("===>Código Servicio Incluido: "+codigoSys);
		
		// Llenamos los HashMap de Articulos de los Serv Incluidos 
		forma.setHasMapaArtServiInclu(mundo.cargarArtIncluServiInclu(con, codigoSys));
		
		
		
		//recorrer lo mismo y llenar los datos de los articulos
		
		

		forma.setHasMapaCodigosTmp("codArtIncluTodos", "");	//todos los articulos de un principal
		forma.setHasMapaCodigosTmp("codArtIncluSelec", "");	//los articulos seleccionados para un serv ppal

		int numArticulosInclu = Utilidades.convertirAEntero(forma.getHasMapaArtServiInclu("numRegistros")+"");

		//si el ppal tiene articulos incluidos se cargan los codigos de los mismos para que no se seleccionen nuevamente
		if(numArticulosInclu > 0) {
			for(int i=0; i<numArticulosInclu ; i++)
				codigosArtInclu += forma.getHasMapaArtServiInclu("codartincluserv_"+i)+ ", ";
		}
		
		
		forma.setHasMapaCodigosTmp("codArtIncluTodos", codigosArtInclu);	//todos los articulos de un principal
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleinclu");
	}
	

	/** Metodo para adicionar un Servicio Incluido a uno Principal 
	 * @param forma, con, mapping, usuario
	 * @return	 */
	private ActionForward accionAdicionarServInclu(Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario)
	{
		int pos = Integer.parseInt(forma.getHasMapaServiIncluServiPpal("numRegistros")+"");
		String tarifarioServicio = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt());
		String codigosServicios = "";

		//Validarlo para cuando se va a modificar o para cuando se va a registrar uno nuevo
		if(forma.getEsModificado().equals(ConstantesBD.acronimoSi))
		{
			//Si viene para una modificacion. Toca recorrer el mapa y meter los codigos de los servicios en esta llave del mapa
			for(int i=0; i<Utilidades.convertirAEntero(forma.getHasMapaServiIncluServiPpal(""+i)+""); i++)
				codigosServicios += forma.getHasMapaServiIncluServiPpal("codservinclu_"+i).toString() + ",";
				
			forma.setHasMapaServiIncluServiPpal("codigosServicios", codigosServicios);
		}
		else
			codigosServicios = forma.getHasMapaServiIncluServiPpal().get("codigosServicios").toString();

		int grupoServicio = Utilidades.obtenerGrupoServicio(con, Utilidades.convertirAEntero(forma.getCodServInclu()));
		
		forma.setHasMapaServiIncluServiPpal("codservinclu_"+pos, forma.getCodServInclu());
		forma.setHasMapaServiIncluServiPpal("codservinclutarifario_"+pos, Utilidades.obtenerCodigoPropietarioServicio(con, forma.getCodServInclu(), Utilidades.convertirAEntero(tarifarioServicio)));
		forma.setHasMapaServiIncluServiPpal("descripservinclu_"+pos, forma.getDescripServInclu());
		forma.setHasMapaServiIncluServiPpal("especialidadservinclu_"+pos, forma.getEspecialidadServInclu());
		forma.setHasMapaServiIncluServiPpal("centrocostoservinclu_"+pos, UtilidadesFacturacion.consultarCentrosCostoGrupoServicio(con, grupoServicio, usuario.getCodigoCentroAtencion(), true,false));
		forma.setHasMapaServiIncluServiPpal("posservinclu_"+pos, forma.getEsPosServInclu());

		forma.setHasMapaServiIncluServiPpal("cantservinclu_"+pos, "");
		forma.setHasMapaServiIncluServiPpal("basedatos_"+pos, ConstantesBD.acronimoNo);
		forma.setHasMapaServiIncluServiPpal("numRegistros", (pos+1)+"");
		
		codigosServicios += forma.getHasMapaServiIncluServiPpal("codservinclu_"+pos).toString() + ",";
		forma.setHasMapaServiIncluServiPpal("codigosServicios", codigosServicios);
		forma.resetServiciosIncluidos();

		logger.info("==========>");
		logger.info("===>Codigo de ServInclu Fin: "+forma.getHasMapaServiIncluServiPpal("codservinclu_"+pos).toString());
		logger.info("===>Codigo de ServItrifa Fin: "+forma.getHasMapaServiIncluServiPpal("codservinclutarifario_"+pos).toString());
		
		logger.info("===>Es Modificado: "+forma.getEsModificado());
		if(forma.getEsModificado().equals(ConstantesBD.acronimoSi))
		{
			UtilidadBD.closeConnection(con);		
	        return mapping.findForward("modificar");
		}
		else
		{
			UtilidadBD.closeConnection(con);		
	        return mapping.findForward("nuevo");
		}
	}
	

/** Metodo para adicionar un articulo a un servicio principal 
 * @param forma, con, mapping, usuario
 * @return */
	private ActionForward accionAdicionarArticulo(Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario)
	{
		int pos = Integer.parseInt(forma.getHasMapaArtServiPpal("numRegistros")+"");
		String codigosArticulos = "";
		
		//Validarlo para cuando se va a modificar o para cuando se va a registrar uno nuevo
		if(forma.getEsModificado().equals(ConstantesBD.acronimoSi))
		{
			//Si viene para una modificacion. Toca recorrer el mapa y meter los codigos de los servicios en esta llave del mapa
			for(int i=0; i<Utilidades.convertirAEntero(forma.getHasMapaArtServiPpal(""+i)+""); i++)
				codigosArticulos += forma.getHasMapaArtServiPpal("codartinclu_"+i).toString() + ",";
				
			forma.setHasMapaArtServiPpal("codigosArticulos", codigosArticulos);
		}
		else
			codigosArticulos = forma.getHasMapaArtServiPpal("codigosArticulos").toString();
		
		logger.info("===>"+forma.getHasMapaArtServiPpal("codigosArticulos"));
		
		forma.setHasMapaArtServiPpal("codartinclu_"+pos, forma.getCodartincluppal());
		forma.setHasMapaArtServiPpal("descripartinclu_"+pos, forma.getDescripartservppal());
		forma.setHasMapaArtServiPpal("unidadartinclu_"+pos, forma.getUnidadArtPpal());
		forma.setHasMapaArtServiPpal("posartinclu_"+pos, forma.getEsPosArtPpal());
		forma.setHasMapaArtServiPpal("cantartinclu_"+pos, "");
		forma.setHasMapaArtServiPpal("basedatos_"+pos, ConstantesBD.acronimoNo);
		forma.setHasMapaArtServiPpal("numRegistros", (pos+1)+"");
		
		codigosArticulos += forma.getHasMapaArtServiPpal("codartinclu_"+pos).toString() + ",";
		forma.setHasMapaArtServiPpal("codigosArticulos", codigosArticulos);
		forma.resetArticulosIncluidos();
		
		logger.info("===>Es Modificado: "+forma.getEsModificado());
		if(forma.getEsModificado().equals(ConstantesBD.acronimoSi))
		{
			UtilidadBD.closeConnection(con);		
	        return mapping.findForward("modificar");
		}
		else
		{
			UtilidadBD.closeConnection(con);		
	        return mapping.findForward("nuevo");
		}
 	}

	
	/** Metodo para eliminar de la lista un Servicio Incluido  de un serv ppal
	 * @param mundo, request, forma, con, mapping, usuario
	 * @return	 */
	private ActionForward accionEliminarServInclu(Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, Servicios_ArticulosIncluidosEnOtrosProcedimientos mundo, HttpServletRequest request)
	{
		HashMap mapa = forma.getHasMapaServiIncluServiPpal();
		String codigosServicios = "";
		boolean enTransaccion = false;
		logger.info("===>Posición Seleccionada: "+forma.getIndexMapInclu());
		
		//Validarlo para cuando se va a modificar o para cuando se va a registrar uno nuevo
		if(forma.getEsModificado().equals(ConstantesBD.acronimoSi))
		{
			logger.info("===>Entro por el Modificar");
			//Si viene para una modificacion. Toca recorrer el mapa y meter los codigos de los servicios en esta llave del mapa
			for(int i=0; i<Utilidades.convertirAEntero(forma.getHasMapaServiIncluServiPpal(""+i)+""); i++)
				codigosServicios += forma.getHasMapaServiIncluServiPpal("codservinclu_"+i).toString() + ",";
			
			forma.setHasMapaServiIncluServiPpal("codigosServicios", codigosServicios);
		}
		else {
			codigosServicios = forma.getHasMapaServiIncluServiPpal().get("codigosServicios").toString();
		}
		
		logger.info("===>Codigos Servicios Antes: "+codigosServicios);
		logger.info("===>Valor a Reemplazar: "+mapa.get("codservinclu_"+forma.getIndexMapInclu())+",");
		codigosServicios = codigosServicios.replaceAll(mapa.get("codservinclu_"+forma.getIndexMapInclu())+",", "");
		//
		forma.setHasMapaCodigosTmp("codigosIncluSelec", forma.getHasMapaCodigosTmp().get("codigosIncluSelec").toString().replaceAll(mapa.get("codservinclu_"+forma.getIndexMapInclu())+",", ""));
		logger.info("===>Codigos Servicio Despues: " + codigosServicios);
		
		
		//Se evalua si esta en la BD o no
		if ((mapa.get("basedatos_"+forma.getIndexMapInclu())+"").equals(ConstantesBD.acronimoNo))	
			eliminarRegistroMapaServInclu(mapa, Integer.parseInt(forma.getIndexMapInclu().toString()));
		else
		{
			UtilidadBD.iniciarTransaccion(con);
			enTransaccion = mundo.eliminarServicioIncluido(con, Utilidades.convertirAEntero(forma.getHasMapaServiIncluServiPpal("codconservinclu_"+forma.getIndexMapInclu())+""));
			if(enTransaccion)
			{
				UtilidadBD.finalizarTransaccion(con);
				this.accionCargarModificar(con, forma, mundo, usuario, mapping, request);
			}
			else
				UtilidadBD.abortarTransaccion(con);
		}
			
		forma.getHasMapaServiIncluServiPpal().put("codigosServicios", codigosServicios);
		
		logger.info("===>Es Modificado: "+forma.getEsModificado());
		if(forma.getEsModificado().equals(ConstantesBD.acronimoSi))
		{
			UtilidadBD.closeConnection(con);		
	        return mapping.findForward("modificar");
		}
		else
		{
			UtilidadBD.closeConnection(con);		
	        return mapping.findForward("nuevo");
		}
	}

	
	private ActionForward accionEliminarArticulo(Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, Servicios_ArticulosIncluidosEnOtrosProcedimientos mundo, HttpServletRequest request)
	{
		HashMap mapa = forma.getHasMapaArtServiPpal();
		String codigosArticulos = "";
		boolean enTransaccion = false;
		logger.info("===>Posición Seleccionada: "+forma.getIndexMap());
		//Validarlo para cuando se va a modificar o para cuando se va a registrar uno nuevo
		if(forma.getEsModificado().equals(ConstantesBD.acronimoSi))
		{
			logger.info("===>Entro por el Modificar");
			//Si viene para una modificacion. Toca recorrer el mapa y meter los codigos de los servicios en esta llave del mapa
			for(int i=0; i<Utilidades.convertirAEntero(forma.getHasMapaArtServiPpal(""+i)+""); i++)
				codigosArticulos += forma.getHasMapaArtServiPpal("codartinclu_"+i).toString() + ",";
			
			forma.setHasMapaArtServiPpal("codigosArticulos", codigosArticulos);
		}
		else
			codigosArticulos = forma.getHasMapaArtServiPpal().get("codigosArticulos").toString();
		
		logger.info("===>Codigos Articulos Antes: " + codigosArticulos);
		logger.info("===>Valor a Reemplazar: "+mapa.get("codartinclu_"+forma.getIndexMap())+",");
		codigosArticulos = codigosArticulos.replaceAll(mapa.get("codartinclu_"+forma.getIndexMap())+",", "");
		forma.setHasMapaCodigosTmp("codArtPpalSelec", forma.getHasMapaCodigosTmp().get("codArtPpalSelec").toString().replaceAll(mapa.get("codartinclu_"+forma.getIndexMap())+",", ""));
		logger.info("===>Codigos Articulos Despues: "+codigosArticulos);
		
		//Se evalua si esta en la BD o no
		if ((mapa.get("basedatos_"+forma.getIndexMap())+"").equals(ConstantesBD.acronimoNo))	
			eliminarRegistroMapaArticulos(mapa, Integer.parseInt(forma.getIndexMap().toString()));
		else
		{
			UtilidadBD.iniciarTransaccion(con);
			enTransaccion = mundo.eliminarArticuloIncluido(con, Utilidades.convertirAEntero(forma.getHasMapaArtServiPpal("codconartinclu_"+forma.getIndexMap())+""));
			if(enTransaccion)
			{
				UtilidadBD.finalizarTransaccion(con);
				this.accionCargarModificar(con, forma, mundo, usuario, mapping, request);
			}
			else
				UtilidadBD.abortarTransaccion(con);
		}
			
		forma.getHasMapaArtServiPpal().put("codigosArticulos", codigosArticulos);
		
		logger.info("===>Es Modificado: "+forma.getEsModificado());
		if(forma.getEsModificado().equals(ConstantesBD.acronimoSi))
		{
			UtilidadBD.closeConnection(con);		
		        return mapping.findForward("modificar");
		}
		else
		{
			UtilidadBD.closeConnection(con);		
		        return mapping.findForward("nuevo");
		}
	}

	
	/** Metodo para ordenar los datos del hashmap al momento de eliminar un dato
	 * @param mapa, pos
	 * @return	 */
	private HashMap eliminarRegistroMapaArticulos(HashMap mapa, int pos)
	{
		int aux=pos+1;
	
		for(int x=pos; x<Integer.parseInt(mapa.get("numRegistros").toString()); x++)
		{
			mapa.put("codservppal_"+x, mapa.get("codservppal_"+aux));
			mapa.put("descripartinclu_"+x, mapa.get("descripartinclu_"+aux));
			mapa.put("cantartinclu_"+x, mapa.get("cantartinclu_"+aux));
			mapa.put("basedatos_"+x, mapa.get("basedatos_"+aux));
			mapa.put("posartinclu_"+x, mapa.get("posartinclu_"+aux));
			mapa.put("unidadartinclu_"+x, mapa.get("unidadartinclu_"+aux));
			mapa.put("codartinclu_"+x, mapa.get("codartinclu_"+aux));

			//mapa.put("farmaciaartinclu_"+x, mapa.get("farmaciaartinclu_"+aux));
			//mapa.put("unidadartinclu_"+x, mapa.get("codconartinclu_"+aux));
			//mapa.put("unidadartinclu_"+x, mapa.get("codigointerfazartinclu_"+aux));
			aux++;
		}
		aux = Integer.parseInt(mapa.get("numRegistros").toString());

		mapa.remove("codservppal_"+aux);
		mapa.remove("codartinclu_"+aux);
		mapa.remove("unidadartinclu_"+aux);
		mapa.remove("descripartinclu_"+aux);
		mapa.remove("cantartinclu_"+aux);
		mapa.remove("basedatos_"+aux);
		mapa.remove("posartinclu_"+aux);
		mapa.put("numRegistros", aux-1);
		
		return mapa;
	}	

	
	/** Metodo para ordenar los datos del hashmap al momento de eliminar un dato
	 * @param mapa, pos
	 * @return	 */
	private HashMap eliminarRegistroMapaServInclu(HashMap mapa, int pos)
	{
		int aux=pos+1;
	
		for(int x=pos; x<Integer.parseInt(mapa.get("numRegistros").toString()); x++)
		{
			mapa.put("codservinclu_"+x, mapa.get("codservinclu_"+aux));
			mapa.put("codservinclutarifario_"+x, mapa.get("codservinclutarifario_"+aux));
			mapa.put("descripservinclu_"+x, mapa.get("descripservinclu_"+aux));
			mapa.put("especialidadservinclu_"+x, mapa.get("especialidadservinclu_"+aux));
			mapa.put("posservinclu_"+x, mapa.get("posservinclu_"+aux));
			mapa.put("cantservinclu_"+x, mapa.get("cantservinclu_"+aux));
			mapa.put("basedatos_"+x, mapa.get("basedatos_"+aux));
			mapa.put("centrocostoservinclu_"+x, mapa.get("centrocostoservinclu_"+aux));
			aux++;
		}
		aux = Integer.parseInt(mapa.get("numRegistros").toString());
		mapa.remove("codservinclu_"+aux);
		mapa.remove("codservinclutarifario_"+aux);
		mapa.remove("descripservinclu_"+aux);
		mapa.remove("especialidadservinclu_"+aux);
		mapa.remove("posservinclu_"+aux);
		mapa.remove("cantservinclu_"+aux);
		mapa.remove("basedatos_"+aux);
		mapa.remove("centrocostoservinclu_"+aux);
		mapa.put("numRegistros", aux-1);
		
		return mapa;
	}
	
	
	
	/** Metodo para adicionar un articulo a un servicio incluido 
	 * @param forma, con, mapping, usuario
	 * @return */
		private ActionForward accionAdicionarArtienServinclu(Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario)
		{
			int pos = Integer.parseInt(forma.getHasMapaArtServiInclu("numRegistros")+"");
			String codigosArticulos = "";
			
			//Validarlo para cuando se va a modificar o para cuando se va a registrar uno nuevo
			if(forma.getEsModificado().equals(ConstantesBD.acronimoSi))
			{
				//Si viene para una modificacion. Toca recorrer el mapa y meter los codigos de los servicios en esta llave del mapa
				for(int i=0; i<Utilidades.convertirAEntero(forma.getHasMapaArtServiInclu(""+i)+""); i++)
					codigosArticulos += forma.getHasMapaArtServiInclu("codartincluserv_"+i).toString() + ",";
					
				forma.setHasMapaArtServiInclu("codigosArticulos", codigosArticulos);
			}
			else
				codigosArticulos = forma.getHasMapaArtServiInclu("codigosArticulos").toString();

			logger.info("===>"+forma.getHasMapaArtServiInclu("codigosArticulos"));


			forma.setHasMapaArtServiInclu("posartincluserv_"+pos, forma.getEsPosArtServinclu());
			forma.setHasMapaArtServiInclu("codartincluserv_"+pos, forma.getCodartservinclu());
			forma.setHasMapaArtServiInclu("descripartincluserv_"+pos, forma.getDescripartservinclu());
			forma.setHasMapaArtServiInclu("cantartincluserv_"+pos, "");
			forma.setHasMapaArtServiInclu("unidadartincluserv_"+pos, forma.getUnidadArtServinclu());
			forma.setHasMapaArtServiInclu("basedatos_"+pos, ConstantesBD.acronimoNo);
			forma.setHasMapaArtServiInclu("numRegistros", (pos+1)+"");
			
			codigosArticulos += forma.getHasMapaArtServiInclu("codartincluserv_"+pos).toString() + ",";
			
			
			forma.setHasMapaArtServiInclu("codigosArticulos", codigosArticulos);
			forma.resetArtincluServinclu();
			
			
			logger.info("===>Es Modificado: "+forma.getEsModificado());
			if(forma.getEsModificado().equals(ConstantesBD.acronimoSi))
			{
				UtilidadBD.closeConnection(con);		
		        return mapping.findForward("modificar");
			}
			else
			{
				UtilidadBD.closeConnection(con);		
		        return mapping.findForward("nuevo");
			}
	 	}
	

		
		
// DE ACA PA BAJO SON LOS DE LOS ARTICULOS EN LOS SERVICIOS INCLUIDOS	
	

		/** Metodo para adicionar un articulo a un servicio incluido
		 * @param forma, con, mapping, usuario
		 * @return */
			private ActionForward accionAdicionarArtServinclu(Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario)
			{
				int pos = Integer.parseInt(forma.getHasMapaArtServiInclu("numRegistros")+"");
				String codigosArticulos = "";

				logger.info("==============");
				
				//Validarlo para cuando se va a modificar o para cuando se va a registrar uno nuevo
				if(forma.getEsModificado().equals(ConstantesBD.acronimoSi))
				{
					logger.info("Es Una modificacion");
					
					//Si viene para una modificacion. Toca recorrer el mapa y meter los codigos de los servicios en esta llave del mapa
					for(int i=0; i<Utilidades.convertirAEntero(forma.getHasMapaArtServiPpal(""+i)+""); i++)
						codigosArticulos += forma.getHasMapaArtServiInclu("codartincluserv_"+i).toString() + ",";
						
					forma.setHasMapaArtServiInclu("codigosArticulos", codigosArticulos);
				}
				else {
					logger.info("Es Una adicion");

					codigosArticulos = forma.getHasMapaArtServiInclu("codigosArticulos").toString();
				}
				
				logger.info("===>"+forma.getHasMapaArtServiInclu("codigosArticulos"));

				forma.setHasMapaArtServiInclu("codartincluserv_"+pos, forma.getCodartservinclu());
				forma.setHasMapaArtServiInclu("descripartincluserv_"+pos, forma.getDescripartservinclu());
				forma.setHasMapaArtServiInclu("unidadartincluserv_"+pos, forma.getUnidadArtServinclu());
				forma.setHasMapaArtServiInclu("posartincluserv_"+pos, forma.getEsPosArtServinclu());
				forma.setHasMapaArtServiInclu("cantartincluserv_"+pos, "");
				forma.setHasMapaArtServiInclu("basedatos_"+pos, ConstantesBD.acronimoNo);
				forma.setHasMapaArtServiInclu("numRegistros", (pos+1)+"");
				
				codigosArticulos += forma.getHasMapaArtServiInclu("codartincluserv_"+pos).toString() + ",";
				forma.setHasMapaArtServiInclu("codigosArticulos", codigosArticulos);

				forma.resetArtincluServinclu();
				
//				logger.info("*************MAPA MAPA ARTIUCLOS DE SERVICIOS INCLUIDOS ****************");
//				Utilidades.imprimirMapa(forma.getHasMapaArtServiInclu());
				
				logger.info("===>Es Modificado: "+forma.getEsModificado());
				if(forma.getEsModificado().equals(ConstantesBD.acronimoSi))
				{
					UtilidadBD.closeConnection(con);		
			        return mapping.findForward("modificartservinclu");
				}
				else
				{
					UtilidadBD.closeConnection(con);
			        return mapping.findForward("nuevoartservinclu");
				}
		 	}


			private ActionForward accionEliminarArtServinclu(Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, Servicios_ArticulosIncluidosEnOtrosProcedimientos mundo, HttpServletRequest request)
			{
				HashMap mapa = forma.getHasMapaArtServiInclu();
				String codigosArticulos = "";
				boolean enTransaccion = false;
				logger.info("===>Posición Seleccionada: "+forma.getIndexMap());
				//Validarlo para cuando se va a modificar o para cuando se va a registrar uno nuevo
				if(forma.getEsModificado().equals(ConstantesBD.acronimoSi))
				{
					logger.info("===>Entro por el Modificar de Articulos Incluidos");

					//Si viene para una modificacion. Toca recorrer el mapa y meter los codigos de los servicios en esta llave del mapa
					for(int i=0; i<Utilidades.convertirAEntero(forma.getHasMapaArtServiInclu(""+i)+""); i++)
						codigosArticulos += forma.getHasMapaArtServiInclu("codartincluserv_"+i).toString() + ",";
					
					forma.setHasMapaArtServiInclu("codigosArticulos", codigosArticulos);
				}
				else
					codigosArticulos = forma.getHasMapaArtServiInclu().get("codigosArticulos").toString();
				
				logger.info("===>Codigos Articulos Antes: " + codigosArticulos);
				logger.info("===>Valor a Reemplazar: "+mapa.get("codartincluserv_"+forma.getIndexMap())+",");
				codigosArticulos = codigosArticulos.replaceAll(mapa.get("codartincluserv_"+forma.getIndexMap())+",", "");
				logger.info("===>Codigos Articulos Despues: "+codigosArticulos);
				
				//Se evalua si esta en la BD o no
				if ((mapa.get("basedatos_"+forma.getIndexMap())+"").equals(ConstantesBD.acronimoNo))	
					eliminarRegistroMapaArtServinclu(mapa, Integer.parseInt(forma.getIndexMap().toString()));
				else
				{
					UtilidadBD.iniciarTransaccion(con);
					enTransaccion = mundo.eliminarArticuloIncluidoServinclu(con, Utilidades.convertirAEntero(forma.getHasMapaArtServiInclu("codconartincluser_"+forma.getIndexMap())+""));
					if(enTransaccion)
					{
						UtilidadBD.finalizarTransaccion(con);
						this.accionCargarModificarServinclu(con, forma, mundo, usuario, mapping, request);
					}
					else
						UtilidadBD.abortarTransaccion(con);
				}
					
				forma.getHasMapaArtServiInclu().put("codigosArticulos", codigosArticulos);
				
				logger.info("===>Es Modificado: "+forma.getEsModificado());

		//mirar esta redireccion
				if(forma.getEsModificado().equals(ConstantesBD.acronimoSi))
				{
					UtilidadBD.closeConnection(con);		
				        return mapping.findForward("modificartservinclu");
				}
				else
				{
					UtilidadBD.closeConnection(con);		
				        return mapping.findForward("nuevoartservinclu");
				}
			}

			/** Metodo para ordenar los datos del hashmap al momento de eliminar un dato
			 * @param mapa, pos
			 * @return	 */
			private HashMap eliminarRegistroMapaArtServinclu(HashMap mapa, int pos)
			{
				int aux=pos+1;
			
				for(int x=pos; x<Integer.parseInt(mapa.get("numRegistros").toString()); x++)
				{
					mapa.put("basedatos_"+x, mapa.get("basedatos_"+aux));
					mapa.put("descripartincluserv_"+x, mapa.get("descripartincluserv_"+aux));
					mapa.put("unidadartincluserv_"+x, mapa.get("unidadartincluserv_"+aux));
					mapa.put("cantartincluserv_"+x, mapa.get("cantartincluserv_"+aux));
					mapa.put("posartincluserv_"+x, mapa.get("posartincluserv_"+aux));
					mapa.put("codartincluserv_"+x, mapa.get("codartincluserv_"+aux));
					mapa.put("codservinclu_"+x, mapa.get("codservinclu_"+aux));

					//mapa.put("farmaciaartinclu_"+x, mapa.get("farmaciaartinclu_"+aux));
					//mapa.put("unidadartinclu_"+x, mapa.get("codconartinclu_"+aux));
					//mapa.put("unidadartinclu_"+x, mapa.get("codigointerfazartinclu_"+aux));
					aux++;
				}
				aux = Integer.parseInt(mapa.get("numRegistros").toString());

				mapa.remove("codservinclu_"+aux);
				mapa.remove("codartincluserv_"+aux);
				mapa.remove("unidadartincluserv_"+aux);
				mapa.remove("descripartincluserv_"+aux);
				mapa.remove("cantartincluserv_"+aux);
				mapa.remove("basedatos_"+aux);
				mapa.remove("posartincluserv_"+aux);
				mapa.put("numRegistros", aux-1);
				
				return mapa;
			}	




			/** Método que carga el servicio incluido con sus respectivos
			 * articulos incluidos para realizar la respectiva modificación
			 * @param con, forma, mundo, usuario, mapping, request
			 * @return		 */
			private ActionForward accionCargarModificarServinclu(Connection con, Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma, Servicios_ArticulosIncluidosEnOtrosProcedimientos mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
			{
				int institucion = usuario.getCodigoInstitucionInt();
				int centroAtencion = usuario.getCodigoCentroAtencion();
				
				//obtener el centro de cosot	
//				forma.setFarmaciasMap(Utilidades.obtenerCentrosCosto(institucion,ConstantesBD.codigoTipoAreaSubalmacen+"", true, centroAtencion, false));		
//				Utilidades.imprimirMapa(forma.getFarmaciasMap());
				
				this.accionCargarServiInclu(con, forma, mundo, usuario, mapping, request);
//				logger.info("=============>Datos del Mapa Servicio Incluido En modificar");
//				Utilidades.imprimirMapa(forma.getHasMapaServiIncluServiPpal());
				UtilidadBD.closeConnection(con);
				return mapping.findForward("modificartservinclu");
			}
		

			
			
			
			
			
			/**	 * Método que guarda las modificaciones en los servicios incluidos de los articulos
			 * @param forma, mundo, usuario
			 * @param mapping
			 * @param request
			 * @return	 */
			private ActionForward accionGuardarModificartServinclu(Connection con, Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma, Servicios_ArticulosIncluidosEnOtrosProcedimientos mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
			{
				ActionErrors errores = new ActionErrors();
				errores = this.validarGuardar(con, forma);
				
				if(!errores.isEmpty())
				{
					saveErrors(request, errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("modificartservinclu");
				}
				else
				{
					boolean enTransaccion = false;
					int numResgitrosMapaArticulosIncluidos = Utilidades.convertirAEntero(forma.getHasMapaArtServiInclu("numRegistros")+"");
					
//--------------------------					
					logger.info("======>Posicion: "+forma.getPosServicioSeleccionado());
					
					//revisar este codigo que debe apuntar al pk del servicio incluido seleccionado 
					int codigoProcedimientoIncluido = Utilidades.convertirAEntero(forma.getHasMapaServiIncluServiPpal("codconservinclu_"+forma.getPosServicioSeleccionado())+"");

					logger.info("==================");
					logger.info("==================");
					
					logger.info("===>Código Servicio Incluido: "+codigoProcedimientoIncluido);
					UtilidadBD.iniciarTransaccion(con);

					Utilidades.imprimirMapa(forma.getHasMapaArtServiInclu());
					
					//Se inserta segundo los articulos incluidos dentro del principal y se evalua si se adiciono correctamente
					if(numResgitrosMapaArticulosIncluidos > 0)
					{
						for(int j=0; j<numResgitrosMapaArticulosIncluidos; j++)
						{
							
							logger.info("===>"+forma.getHasMapaArtServiInclu("basedatos_"+j));
							//Validamos si se debe hacer un UPDATE o un INSERT validando la llave del mapa basedatos
							if((forma.getHasMapaArtServiInclu("basedatos_"+j)+"").equals(ConstantesBD.acronimoSi)){
								
								logger.info("==================");
								logger.info("SE ACTUALIZARA UN ARTICULO DE UN SERVINCLU");
								logger.info("Code Sistema: "+forma.getHasMapaArtServiInclu("codconartincluser_"+j));
								logger.info("Farmacia: "+ forma.getHasMapaArtServiInclu("farmaciaartincluserv_"+j));
								logger.info("Cantidad: "+ forma.getHasMapaArtServiInclu("cantartincluserv_"+j));
								
								enTransaccion = mundo.actualizarArticulosIncluidosEnServicioIncluido(con, Utilidades.convertirAEntero(forma.getHasMapaArtServiInclu("codconartincluser_"+j)+""), Utilidades.convertirAEntero(forma.getHasMapaArtServiInclu("farmaciaartincluserv_"+j)+""), Utilidades.convertirAEntero(forma.getHasMapaArtServiInclu("cantartincluserv_"+j)+""));
							}
							else {
								logger.info("==================");
								logger.info("SE INSERTARA UN NUEVO ARTICULO A UN SERVINCLU");
								logger.info("Code Servinclu: "+codigoProcedimientoIncluido);
								logger.info("CodeArticulo: "+ forma.getHasMapaArtServiInclu("codartincluserv_"+j));
								logger.info("Farmacia: "+ forma.getHasMapaArtServiInclu("farmaciaartincluserv_"+j));
								logger.info("Cantidad: "+ forma.getHasMapaArtServiInclu("cantartincluserv_"+j));
								enTransaccion = mundo.insertarArticulosIncluidosEnServicioIncluido(con, codigoProcedimientoIncluido+"", forma.getHasMapaArtServiInclu("codartincluserv_"+j)+"", forma.getHasMapaArtServiInclu("farmaciaartincluserv_"+j)+"", forma.getHasMapaArtServiInclu("cantartincluserv_"+j)+"");
								
							}
								
							//Validamos si la adicion del articulo incluido fue satisfactorio o no 
							if(!enTransaccion)
								j = numResgitrosMapaArticulosIncluidos;
						}
					}

					if(enTransaccion)
					{
						UtilidadBD.finalizarTransaccion(con);
						this.accionEmpezar(con, forma, mundo, usuario, mapping, request);
						forma.setMensaje(new ResultadoBoolean(true,"OPERACIÓN REALIZADA CON ÉXITO"));
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}
					else
					{
						UtilidadBD.abortarTransaccion(con);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("modificartservinclu");
					}
				}
			}




			/** Método que guarda los atiruclo incluidos, en un servinclu para lo cual se debe ingresar
			 * primero el servicio principal y luego los servicios incluidos
			 * @param con, forma, mundo, usuario, mapping, request
			 * @return	 */
			private ActionForward accionGuardartServinclu(Connection con, Servicios_ArticulosIncluidosEnOtrosProcedimientosForm forma, Servicios_ArticulosIncluidosEnOtrosProcedimientos mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
			{		
				ActionErrors errores = new ActionErrors();
				errores = this.validarGuardar(con, forma);
				
				if(!errores.isEmpty())
				{
					saveErrors(request, errores);
					forma.setExitoso(false);
//					forma.setEstado("nuevoSPpal");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("nuevoartservinclu");
				}
				else
				{
					boolean enTransaccion = false;
					int numResgitrosMapaArticulosIncluidos = Utilidades.convertirAEntero(forma.getHasMapaArtServiInclu("numRegistros")+ "");
					
					//Se insertan los articulos incluidos dentro del incluido y se evalua si se adiciono correctamente
					if(numResgitrosMapaArticulosIncluidos > 0)
					{
						int codigoServiPpal;
						int codigoServiInclu;
						UtilidadBD.iniciarTransaccion(con);
	
	
						codigoServiPpal = Utilidades.obtenerCodigoProcedimientoPrincipalIncluidos(con, forma.getCodigoServicio());
						logger.info("====>Código del Servicio Incluido: "+forma.getHasMapaServiIncluServiPpal("codservinclu_"+forma.getPosServicioSeleccionado()));
						
						codigoServiInclu = Utilidades.obtenerCodigoServicioIncluido(con, codigoServiPpal+"", forma.getHasMapaServiIncluServiPpal("codservinclu_"+forma.getPosServicioSeleccionado())+"");
						
						logger.info("===>Código Procedimiento Principal: "+codigoServiPpal);
						logger.info("===>Código Procedimiento Incluido: "+codigoServiInclu);
						
						for(int j=0; j<numResgitrosMapaArticulosIncluidos; j++)
						{
							enTransaccion = mundo.insertarArticulosIncluidosEnServicioIncluido(con, codigoServiInclu+"", forma.getHasMapaArtServiInclu("codartincluserv_"+j)+"", forma.getHasMapaArtServiInclu("farmaciaartincluserv_"+j)+"", forma.getHasMapaArtServiInclu("cantartincluserv_"+j)+"");
							
							//Validamos si la adicion del articulo incluido fue satisfactorio o no 
							if(!enTransaccion)
								j = numResgitrosMapaArticulosIncluidos;
						}
					}
					
					if(enTransaccion)
					{
						UtilidadBD.finalizarTransaccion(con);
						//this.accionEmpezar(con, forma, mundo, usuario, mapping, request);
						forma.setEstado("guardarInsertar");
						forma.setMensaje(new ResultadoBoolean(true,"OPERACIÓN REALIZADA CON ÉXITO"));
						forma.setExitoso(true);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("nuevo");
					}
					else
					{
						forma.setExitoso(false);
						UtilidadBD.abortarTransaccion(con);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("nuevoartservinclu");
					}
				}
			}

}