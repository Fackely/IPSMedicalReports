package com.princetonsa.action.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

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
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.salasCirugia.DevolucionPedidoQxForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.salasCirugia.DevolucionPedidoQx;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class DevolucionPedidoQxAction extends Action
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(DevolucionPedidoQxAction.class);

	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
		if (form instanceof DevolucionPedidoQxForm) 
		{
			
		    //Abrimos la conexion con la fuente de Datos 
			con = util.UtilidadBD.abrirConexion();
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			DevolucionPedidoQxForm forma = (DevolucionPedidoQxForm) form;
			DevolucionPedidoQx mundo = new DevolucionPedidoQx();
			HttpSession session = request.getSession();
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
			PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
			String estado = forma.getEstado();
			logger.warn("[DevolucionPedidoQxForm]--->Estado: "+estado);
			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo estado is NULL");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("empezar"))
			{	
				forma.setPacienteDummy(false);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("paciente"))
			{
				return this.accionPaciente(con, forma, request, paciente, mundo, usuario, mapping);
			}
			else if(estado.equals("rangos"))
			{
				return this.accionRangos(con, forma, request, paciente, mundo, usuario, mapping);
			}
			else if(estado.equals("ordenar"))
			{
				return this.ordenarListado(con, forma, mapping);
			}
			else if(estado.equals("volverPrincipal"))
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("detallePeticion"))
			{
				return this.accionDetallePeticion(con, forma, request, paciente, mundo, usuario, mapping);
			}
			else if(estado.equals("cargarAlmacen"))
			{
				return this.accionCargarAlmacen(con, forma, request, paciente, mundo, usuario, mapping);
			}
			else if(estado.equals("buscarRangos"))
			{
				return this.accionBuscarRangos(con, forma, request, paciente, mundo, usuario, mapping);
			}
			else if(estado.equals("volverRangos"))
			{
				return this.accionRangos(con, forma, request, paciente, mundo, usuario, mapping);
			}
			else if(estado.equals("ordenarRango"))
			{
				return this.ordenarListadoRangos(con, forma, mapping);
			}
			else if (estado.equals("cargarPaciente"))
			{
				return this.cargarPaciente(con, forma, mundo, usuario, paciente, request, mapping);
			}
			else if (estado.equals("devolver"))
			{
				return this.accionDevolver(con, forma, mundo, usuario, paciente, request, mapping);
			}
			else if (estado.equals("volverDetalle"))
			{
				return this.accionVolverDetalle(con, forma, mundo, usuario, paciente, request, mapping);
			}
			else if (estado.equals("aceptarAlmacenes"))
			{
				return this.accionAceptarAlmacenes(con, forma, mundo, usuario, paciente, request, mapping);
			}
			else if (estado.equals("imprimir"))
			{
				return this.accionImprimir(con, forma, mundo, usuario, paciente, request, mapping);
			}
			//ESTADO UTILIZADO PARA EL PAGER
			else if (estado.equals("redireccion")) 
			{			    
			    UtilidadBD.cerrarConexion(con);
			    forma.getLinkSiguiente();
			    response.sendRedirect(forma.getLinkSiguiente());
			    return null;
			}
			else
			{
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
		}
		else
		{
			logger.error("El form no es compatible con el form de DevolucionPedidoQxForm");
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
	 * Metodo que guarda la cantidad a devolver de cada uno de los
	 * almacenes para el articulo de consumo que se le realiza la
	 * distribucion de pedido
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionAceptarAlmacenes(Connection con, DevolucionPedidoQxForm forma, DevolucionPedidoQx mundo, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping)
	{
		logger.info("===>Posicion del Articulo: "+forma.getPosicionArticulo());
		int numRegistrosAlmacenes = Utilidades.convertirAEntero(((HashMap)forma.getAlmacenes(forma.getPosicionArticulo()+"")).get("numRegistros")+"");
		logger.info("===>Numero de Registros: "+numRegistrosAlmacenes);
		//Recorremos el numero de almacenes del articulo y guardamos su valor a devolver
		for(int i=0; i<numRegistrosAlmacenes; i++)
		{
			((HashMap)forma.getAlmacenes(forma.getPosicionArticulo()+"")).put("cantidaddevolver_"+i, forma.getAlmacenes("cantidaddevolver_"+i));
			logger.info("===>Cantidad Devolver: "+((HashMap)forma.getAlmacenes(forma.getPosicionArticulo()+"")).get("cantidaddevolver_"+i));
			//Reseteamos el valor temporal cantidaddevolver del mapa almacenes con la intencion de que no lo postule en los otros articulos
			forma.setAlmacenes("cantidaddevolver_"+i, "");
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("almacenes");
	}

	/**
	 * Metodo que devuelve a la pagina anterior al detalle de
	 * la solicitud de cirugia
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionVolverDetalle(Connection con, DevolucionPedidoQxForm forma, DevolucionPedidoQx mundo, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping)
	{
		//Se valida si la funcionalidad es por paciente o por rangos
		if(forma.isEsPorPaciente())
		{

			//Se valida si el mapa tuvo mas de un registro con el fin de devolverlo a la estructura anterior
			if(Utilidades.convertirAEntero(forma.getListadoPeticiones("numRegistros")+"") == 1)
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else
				return this.accionPaciente(con, forma, request, paciente, mundo, usuario, mapping);
		}
		else
		{
			//Se valida si el mapa tuvo mas de un registro con el fin de devolverlo a la estructura anterior
			if(Utilidades.convertirAEntero(forma.getListadoPeticiones("numRegistros")+"") == 1)
				return this.accionRangos(con, forma, request, paciente, mundo, usuario, mapping);
			else
				return this.accionBuscarRangos(con, forma, request, paciente, mundo, usuario, mapping);
		}
	}
	
	/**
	 * Metodo implementado para validar los errores en el 
	 * detalle de la solicitud de cirugia a la que se le
	 * pretende dar una devolucion de pedido qx
	 * @param con
	 * @param forma
	 * @return  
	 */
	private ActionErrors validarDevolverPedido(Connection con, DevolucionPedidoQxForm forma)
	{
		ActionErrors errores = new ActionErrors();
		
		if(forma.getMotivoDevolucionSeleccionado().trim().equals("") || forma.getMotivoDevolucionSeleccionado().trim().equals("null"))
			errores.add("Motivo Devolución", new ActionMessage("errors.required","El Motivo de Devolución "));
		
		return errores;
	}
	
	/**
	 * Metodo que permite devolver el pedido de la solicitud de cirugia
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDevolver(Connection con, DevolucionPedidoQxForm forma, DevolucionPedidoQx mundo, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping)
	{
		ActionErrors errores = new ActionErrors();
		ActionErrors erroresDistribucion = new ActionErrors();
		String codigoArticulo = "", pedido = "", cantidadADevolver = "", lote = "", fechaVencimiento = "";
		boolean devolver = false;
		int cantidadNetaDevolver = 0;
		//Validamos los datos requeridos en el detalle de la solicitud de devolucion de pedido
		errores = this.validarDevolverPedido(con, forma);
		//Realizar el split del nombre del motivo de devolucion
		if(UtilidadCadena.noEsVacio(forma.getMotivoDevolucionSeleccionado()))
		{
			String motivo[];
			motivo = forma.getMotivoDevolucionSeleccionado().split(ConstantesBD.separadorSplit);
			forma.setMotivoDevolucionSeleccionado(motivo[0]);
			forma.setNombreMotivoDevolucionSeleccionado(motivo[1]);
		}
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			return mapping.findForward("detallePaciente");
		}
		else
		{
			UtilidadBD.iniciarTransaccion(con);
			int codigoDevolucion = ConstantesBD.codigoNuncaValido;
			//Recorremos el mapa de los articulos para saber a cual articulo se le realizo una devolucion
			for(int i=0; i<Utilidades.convertirAEntero(forma.getDetallePeticionArticulos("numRegistros")+""); i++)
			{
				//Reseteamos la cantidad neta a devolver
				cantidadNetaDevolver = 0;
				if(codigoDevolucion <= 0)
				{
					//Insertamos la devolucion
					codigoDevolucion = mundo.devolucionPedidoQx(con, forma, usuario);
					if(codigoDevolucion <= 0)
						UtilidadBD.abortarTransaccion(con);
				}
				logger.info("===>Cantidad a Devolver para el Articulo "+i+": "+forma.getDetallePeticionArticulos("cantidaddevolver_"+i));
				//Validamos si al articulo se realizo una devolucion de pedido Qx. Es decir si el indice "cantidaddevolver" no es vacio
				if(UtilidadCadena.noEsVacio(forma.getDetallePeticionArticulos("cantidaddevolver_"+i)+""))
				{
					codigoArticulo = forma.getDetallePeticionArticulos("codigoarticulo_"+i)+"";
					//Validamos si el articulo determinado se le realizo la distribucion de la devolucion
					//en caso tal de que no se valida que es requerido la distribucion en los almacenes del articulo
					if(forma.getAlmacenes().containsKey(i+""))
					{
						//Se saca el mapa interno de cada uno los articulos, el cual referencia a los almacenes de este. Lo llenamos en un
						//mapa temporal para facilitar su manipulacion
						HashMap tempAlmacen = new HashMap ();
						tempAlmacen.putAll((HashMap)forma.getAlmacenes(i+""));
						//Recorremos el mapa tempAlmacen de cada uno de los articulos con el fin de determinar la cantidad y la farmacia a la
						//cual se realizo la distribucion de devolucion del pedido Qx
						int numRegistrosAlmacen = Utilidades.convertirAEntero(tempAlmacen.get("numRegistros")+"");
						for(int j=0; j<numRegistrosAlmacen; j++)
						{
							if(UtilidadCadena.noEsVacio(tempAlmacen.get("cantidaddevolver_"+j)+"") 
									|| (!UtilidadCadena.noEsVacio(tempAlmacen.get("cantidaddevolver_"+j)+"") && Integer.parseInt(tempAlmacen.get("numRegistros")+"") == 1))
							{
								pedido = tempAlmacen.get("numeropedido_"+j)+"";
								cantidadADevolver = UtilidadCadena.noEsVacio(tempAlmacen.get("cantidaddevolver_"+j)+"") ? tempAlmacen.get("cantidaddevolver_"+j)+"" : forma.getDetallePeticionArticulos("cantidaddevolver_"+i)+"" ;
								lote = tempAlmacen.get("lote_"+j)+"";
								fechaVencimiento = tempAlmacen.get("fechavencimiento_"+j)+"";
								devolver = mundo.devolverDetallePedidoQx(con, forma, usuario, codigoArticulo, pedido, cantidadADevolver, lote, fechaVencimiento, codigoDevolucion);
								if(devolver)
									cantidadNetaDevolver = cantidadNetaDevolver + Utilidades.convertirAEntero(cantidadADevolver);
							}
						}
						//Llenamos la cantidad real a devolver; la cual se refiere a la sumatoria declarada en la distribucion por almacen
						forma.setDetallePeticionArticulos("cantidaddevolver_"+i, cantidadNetaDevolver);
					}
					else
						erroresDistribucion.add("Distribución Almacen", new ActionMessage("errors.required","La Distribución de la Devolución para el Articulo "+forma.getDetallePeticionArticulos("codigoarticulo_"+i)));
				}
			}
			if(!devolver)
			{
				forma.setCodigosDevolucion("");
				UtilidadBD.abortarTransaccion(con);
			}
			else
			{
				logger.info("====>Codigo de la Devolucion: "+codigoDevolucion);
				forma.setCodigosDevolucion(codigoDevolucion+"");
				UtilidadBD.finalizarTransaccion(con);
			}
			
			if(!erroresDistribucion.isEmpty())
			{
				//Llenamos el motivo de devolucion con el codigo y el nombre que hay actual con la intencion de que no haga reset
				forma.setMotivoDevolucionSeleccionado(forma.getMotivoDevolucionSeleccionado()+ConstantesBD.separadorSplit+forma.getNombreMotivoDevolucionSeleccionado());
				saveErrors(request, erroresDistribucion);
				UtilidadBD.abortarTransaccion(con);
				return mapping.findForward("detallePaciente");
			}
			else
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("resumenPaciente");
			}
		}
	}

	/**
	 * Metodo que carga el paciente en sesion
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward cargarPaciente(Connection con, DevolucionPedidoQxForm forma, DevolucionPedidoQx mundo, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping)
	{
		paciente.setCodigoPersona(Utilidades.convertirAEntero(forma.getListadoPeticiones("codigopaciente_"+forma.getPosicion())+""));
		UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detallePaciente");
	}

	/**
	 * Metodo que ejecuta la pagina principal de
	 * criterios de busqueda para la consulta por
	 * rangosaccionPaciente
	 * @param con
	 * @param forma
	 * @param request 
	 * @param paciente 
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionRangos(Connection con, DevolucionPedidoQxForm forma, HttpServletRequest request, PersonaBasica paciente, DevolucionPedidoQx mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.reset();
		forma.setEsPorPaciente(false);
		//Cargamos el select de centros de costo que ejecuta y el que devuelve
		forma.setCentroCosto(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaDirecto+"", true, usuario.getCodigoCentroAtencion(),false));
		//Cargamos el select que devuelve con el centro de costo del usuario
		forma.setCodigoCentroCostoDevuelve(usuario.getCodigoCentroCosto()+"");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("buscarRangos");
	}

	/**
	 * Metodo que ejecuta la consulta y las validaciones
	 * por paciente. Consulta las solicitudes de cirugia
	 * de la cuenta cargada en sesion
	 * @param con
	 * @param forma
	 * @param request 
	 * @param paciente 
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionPaciente(Connection con, DevolucionPedidoQxForm forma, HttpServletRequest request, PersonaBasica paciente, DevolucionPedidoQx mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.reset();
		forma.setEsPorPaciente(true);
		if(paciente.getCodigoPersona()<1)
		    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noCargado", "errors.paciente.noCargado", true);
		else if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta())<1)
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.cuentaNoValida", "errors.paciente.cuentaNoValida", true);
		else if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto) && !UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoCerrado))
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.salasCirugia.estadoCuenta", "error.salasCirugia.estadoCuenta", true);
		else
		{
			
			forma.setListadoPeticiones(mundo.listadoPeticiones(con, paciente.getCodigoIngreso(),usuario.getCodigoInstitucionInt()));	
			
			if(Utilidades.convertirAEntero(forma.getListadoPeticiones("numRegistros")+"") == 1)
			{
				//Llenamos la posicion en cero cuando existe solo un registro
				forma.setPosicion(0);
				return this.accionDetallePeticion(con, forma, request, paciente, mundo, usuario, mapping);
			}
			else
			{	
				forma.setPedidosVarios(true);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("listadoPaciente");
			}
		}
	}
	
	/**
	 * Metodo que permite ordenar por un patron dado el criterio seleccionado
	 * @param con 
	 * @param forma
	 * @param mapping 
	 * @return
	 */
	private ActionForward ordenarListado(Connection con, DevolucionPedidoQxForm forma, ActionMapping mapping)
	{	
		String[] indices={
        		"peticion_",
        		"fechapeticion_",
        		"horapeticion_",
				"profesionalsolicita_",
        		"ordenmedica_",
        		"estadomedico_"
			};
		
		int numReg = Integer.parseInt(forma.getListadoPeticiones("numRegistros")+"");
		forma.setListadoPeticiones(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getListadoPeticiones(), numReg));
		forma.setListadoPeticiones("numRegistros", numReg+"");	
		
		
		forma.setUltimoPatron(forma.getPatronOrdenar());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoPaciente");
	}
	
	/**
	 * Metodo que permite ordenar por un patron dado el criterio seleccionado
	 * @param con 
	 * @param forma
	 * @param mapping 
	 * @return
	 */
	private ActionForward ordenarListadoRangos(Connection con, DevolucionPedidoQxForm forma, ActionMapping mapping)
	{
		String[] indices={
			        		"peticion_",
			        		"fechapeticion_",
			        		"horapeticion_",
			        		"idpaciente_",
			        		"nombrepaciente_",
			        		"profesionalsolicita_",
							"ordenmedica_",
			        		"estadomedico_"
        				};
		int numReg = Integer.parseInt(forma.getListadoPeticiones("numRegistros")+"");
		forma.setListadoPeticiones(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getListadoPeticiones(), numReg));
		forma.setListadoPeticiones("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoRangos");
	}
	
	/**
	 * Metodo que permite ir al detalle de la peticion
	 * de cirugia seleccionada 
	 * @param con
	 * @param forma
	 * @param request
	 * @param paciente
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetallePeticion(Connection con, DevolucionPedidoQxForm forma, HttpServletRequest request, PersonaBasica paciente, DevolucionPedidoQx mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		logger.info("====>Posicion Seleccionada: "+forma.getPosicion());
		int numeroPeticion = Utilidades.convertirAEntero(forma.getListadoPeticiones("peticion_"+forma.getPosicion())+"");
		String validacionPeticion = mundo.validarPeticionDevolucionesPendientes(con, numeroPeticion); 
		
		if(!validacionPeticion.equals("") && !validacionPeticion.equals(ConstantesBD.acronimoNo))
		    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.salasCirugia.peticionConDevoluciones", "error.salasCirugia.peticionConDevoluciones", true);
		else
		{
			//llenamos el mapa de los servicios de la peticion
			forma.setDetallePeticionServicios(mundo.detallePeticion(con, numeroPeticion));		
			//Llenamos el mapa de los articulos consumidos de la cirugia
			forma.setDetallePeticionArticulos(mundo.detallePeticionArticulos(con, numeroPeticion));
			//Llenamos el select con los motivos de devolucion
			forma.setMotivoDevolucion(Utilidades.obtenerMotivosDevolucionInventarios(con, usuario.getCodigoInstitucionInt()));

			int numRegistrosArticulos = Integer.parseInt(forma.getDetallePeticionArticulos("numRegistros")+"");
			
			for(int i=0 ; i<numRegistrosArticulos ; i++){
				
				int codigoArticulo = Integer.parseInt(forma.getDetallePeticionArticulos("codigoarticulo_"+i)+"");

					logger.info("===>Entro a cargar el mapa Almacen");
					HashMap<String,Object> almacen = new HashMap<String,Object>();
					almacen.putAll(mundo.consultarAlmacenes(con, numeroPeticion, codigoArticulo));

					forma.setDetallePeticionArticulos("numalmacenes_"+i,almacen.get("numRegistros")+"");
					
					if(Integer.parseInt(almacen.get("numRegistros")+"") == 1){
						forma.setDetallePeticionArticulos("codigoalmacen_"+i, almacen.get("codigoalmacen_0"));
						forma.setDetallePeticionArticulos("numeropedido_"+i, almacen.get("numeropedido_0"));
						forma.setDetallePeticionArticulos("nombrealmacen_"+i, almacen.get("nombrealmacen_0"));	
						
						if(!forma.getAlmacenes().containsKey(forma.getPosicionArticulo()+"")){
							almacen.put("cantidaddevolver_0","");
						 	forma.setAlmacenes(i+"", almacen);
						 }
					}
			}
			
			if(!forma.isEsPorPaciente())
				this.cargarPaciente(con, forma, mundo, usuario, paciente, request, mapping);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detallePaciente");
	}
	
	/**
	 * Metodo utilizado para cargar los datos del 
	 * pop-up de almacenes
	 * @param con
	 * @param forma
	 * @param request
	 * @param paciente
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionCargarAlmacen(Connection con, DevolucionPedidoQxForm forma, HttpServletRequest request, PersonaBasica paciente, DevolucionPedidoQx mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		logger.info("====>Posicion Articulo Seleccionada: "+forma.getPosicionArticulo());
		int numeroPeticion = Utilidades.convertirAEntero(forma.getListadoPeticiones("peticion_"+forma.getPosicion())+"");
		int codigoArticulo = Utilidades.convertirAEntero(forma.getDetallePeticionArticulos("codigoarticulo_"+forma.getPosicionArticulo())+"");
		
		//Se llena el mapa de almacenes con otro mapa por dentro con la intencion de tener un mapa por articulo siempre y cuando la llave no exista
		if(!forma.getAlmacenes().containsKey(forma.getPosicionArticulo()+""))
		{	
			logger.info("===>Entro a cargar el mapa Almacen");
			HashMap<String,Object> almacen = new HashMap<String,Object>();
			almacen.putAll(mundo.consultarAlmacenes(con, numeroPeticion, codigoArticulo));
		 	forma.setAlmacenes(forma.getPosicionArticulo()+"", almacen);
		}
		
		int numRegistrosAlmacenes = Utilidades.convertirAEntero(((HashMap)forma.getAlmacenes(forma.getPosicionArticulo()+"")).get("numRegistros")+"");
		logger.info("===>Numero de Registros: "+numRegistrosAlmacenes);
		//Recorremos el numero de almacenes del articulo y guardamos su valor a devolver
		for(int i=0; i<numRegistrosAlmacenes; i++)
		{
			if(UtilidadCadena.noEsVacio(((HashMap)forma.getAlmacenes(forma.getPosicionArticulo()+"")).get("cantidaddevolver_"+i)+""))
			{
				forma.setAlmacenes("cantidaddevolver_"+i, ((HashMap)forma.getAlmacenes(forma.getPosicionArticulo()+"")).get("cantidaddevolver_"+i));
			}
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("almacenes");
	}
	
	/**
	 * Metodo que consulta todas las solicitudes de cirugias
	 * segun un criterio dado
	 * @param con
	 * @param forma
	 * @param request
	 * @param paciente
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionBuscarRangos(Connection con, DevolucionPedidoQxForm forma, HttpServletRequest request, PersonaBasica paciente, DevolucionPedidoQx mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.setListadoPeticiones(mundo.listadoPeticionesPorRangos(con, forma,usuario.getCodigoCentroAtencion()));
		
		if(Utilidades.convertirAEntero(forma.getListadoPeticiones("numRegistros")+"") == 1)
		{
			//Llenamos la posicion en cero cuando existe solo un registro
			forma.setPosicion(0);
			return this.accionDetallePeticion(con, forma, request, paciente, mundo, usuario, mapping);
		}
		else
		{
			UtilidadBD.closeConnection(con);
			return mapping.findForward("listadoRangos");
		}
	}
	
	/**
	 * Metodo que ejecuta la impresion en PDF del detalle de
	 * la devolucion del pedido qx
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, DevolucionPedidoQxForm forma, DevolucionPedidoQx mundo, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionMapping mapping)
	{
		String nombreRptDesign = "DevolucionPedidoQx.rptdesign";
		String condiciones = "";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		String edadPaciente = UtilidadFecha.calcularEdadDetallada(forma.getListadoPeticiones("fechanacimiento_"+forma.getPosicion())+"", UtilidadFecha.getFechaActual()+"");
		String estado = "";
		if(UtilidadCadena.noEsVacio(forma.getCodigosDevolucion()))
			estado = mundo.consultarEstadoDevolucion(con, forma.getCodigosDevolucion());
		//Informacion del Cabezote
        DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"salasCirugia/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v = new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        v = new Vector();
        v.add("Devolución Nro. "+forma.getCodigosDevolucion());
        v.add("Fecha Devolución: "+UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual());
        v.add("Estado Devolución: "+estado);
        v.add("Paciente: "+forma.getListadoPeticiones("nombrepaciente_"+forma.getPosicion())+"");
        v.add("Identificación: "+forma.getListadoPeticiones("idpaciente_"+forma.getPosicion())+"");
        v.add("Centro de Costo que Devuelve: "+forma.getListadoPeticiones("nombrecentrocostosolicita_"+forma.getPosicion())+"");
        v.add("Usuario que Devuelve: "+usuario.getLoginUsuario());
        v.add("Motivo Devolución: "+forma.getNombreMotivoDevolucionSeleccionado());
        v.add("Observaciones: "+forma.getObservaciones());
        comp.insertLabelInGridOfMasterPageWithProperties(4,0,v,DesignChoiceConstants.TEXT_ALIGN_LEFT);
        comp.obtenerComponentesDataSet("DevolucionPedidoQx");
        //Realizamos el filtro por las devoluciones generadas
        condiciones = "dp.codigo = "+forma.getCodigosDevolucion()+" ";
        String newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
        logger.info("Consulta en el BIRT con Condiciones: "+newQuery);
        //Se modifica el query
        comp.modificarQueryDataSet(newQuery);
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
		return mapping.findForward("resumenPaciente");
	}
	
}