/*
 * Marzo 21 del 2007
 */
package com.princetonsa.action.salasCirugia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

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

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.salasCirugia.HojaGastosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.salasCirugia.HojaGastos;

/**
 * @author Sebastián Gómez 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Parametrización de hoja de Gastos
 */
public class HojaGastosAction extends Action {
	
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(HojaGastosAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
													{
		Connection con=null;
		try{
			if (response==null); //Para evitar que salga el warning
			if(form instanceof HojaGastosForm)
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

				//OBJETOS A USAR
				HojaGastosForm hojaForm =(HojaGastosForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=hojaForm.getEstado(); 
				logger.warn("estado HojaGastosAction-->"+estado);


				if(estado == null)
				{
					hojaForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Hoja Gastos (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				//********Estados relacionados con el listado de paquetes de materiales quirúrgicos********
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,hojaForm,mapping,usuario);
				}
				else if (estado.equals("nuevoPaquete"))
				{
					return accionNuevoPaquete(con,hojaForm,response,request);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar(con,hojaForm,mapping);
				}
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,hojaForm,mapping,request,response);
				}
				else if (estado.equals("eliminarPaquete"))
				{
					return accionEliminarPaquete(con,hojaForm,request,response);
				}
				else if (estado.equals("guardarPaquete"))
				{
					return accionGuardarPaquete(con,hojaForm,mapping,request,usuario);
				}
				//******************************************************************************************
				//********Estados relacionados con el detalle de un paquete*********************************
				else if (estado.equals("detallePaquete"))
				{
					return accionDetallePaquete(con,hojaForm,mapping);
				}
				else if (estado.equals("ingresarNuevoArticulo"))
				{
					return accionIngresarNuevoArticulo(con,hojaForm,mapping);
				}	
				else if (estado.equals("eliminarArticulo"))
				{
					return accionEliminarArticulo(con,hojaForm,mapping);
				}
				else if (estado.equals("guardarArticulos"))
				{
					return accionGuardarArticulos(con,hojaForm,mapping,usuario,request);
				}
				//*******************************************************************************************
				//*******Estados relacionados con la funcionalidad procedimientos x paquetes quirúrgicos*******
				else if (estado.equals("empezarProcedimientos"))
				{
					return accionEmpezarProcedimientos(con,hojaForm,usuario,mapping);
				}
				else if (estado.equals("detalleArticulosPaquete"))
				{
					return accionDetallePaquete(con, hojaForm, mapping);
				}
				else if (estado.equals("detalleProcedimientos"))
				{
					return accionDetalleProcedimientos(con,hojaForm,mapping);
				}
				else if (estado.equals("ingresarNuevoServicio"))
				{
					return accionIngresarNuevoServicio(con,hojaForm,mapping);
				}
				else if (estado.equals("eliminarServicio"))
				{
					return accionEliminarServicio(con,hojaForm,mapping);
				}
				else if (estado.equals("guardarProcedimientos"))
				{
					return accionGuardarProcedimientos(con,hojaForm,mapping,usuario,request);
				}
				//**********************************************************************************************
				else
				{
					hojaForm.reset();
					logger.warn("Estado no valido dentro del flujo de HojaGastosAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
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
	 * Método que se usa para ingresar/modificar/eliminar los servicios de un paquete material quirúrgico
	 * @param con
	 * @param hojaForm
	 * @param mapping
	 * @param usuario 
	 * @param request 
	 * @return
	 */
	private ActionForward accionGuardarProcedimientos(Connection con, HojaGastosForm hojaForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		ActionErrors errores = HojaGastos.guardarServicios(con, hojaForm.getMapaServicios(),hojaForm.getNumServicios(), usuario, hojaForm.getConsecutivo());
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			hojaForm.setEstado("detalleProcedimientos");
		}
		else
			return accionDetalleProcedimientos(con, hojaForm, mapping);
		
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método que elimina un servicio del listado de servicios de un paquete de materiales quirúrgicos
	 * @param con
	 * @param hojaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarServicio(Connection con, HojaGastosForm hojaForm, ActionMapping mapping) 
	{
		int pos = hojaForm.getPos();
		String codigosServiciosInsertados = hojaForm.getCodigosServiciosInsertados();
		String codigoServicios = hojaForm.getMapaServicios("codigo_"+pos).toString();
		//se quita del listado de servicios insertados
		codigosServiciosInsertados = codigosServiciosInsertados.replaceAll(codigoServicios+",", "");
		hojaForm.setCodigosServiciosInsertados(codigosServiciosInsertados);
		
		//se verifica si el registro existe en la base de datos
		if(UtilidadTexto.getBoolean(hojaForm.getMapaServicios("existeBd_"+pos).toString()))
			//se cambia el valor del atributo eliminar en true
			hojaForm.setMapaServicios("eliminar_"+pos, "true");
		else
		{
			int numServicios = hojaForm.getNumServicios() - 1;
			//como no existe en la base de datos se puede borrar del mapa
			for(int i=pos;i<numServicios;i++)
			{
				hojaForm.setMapaServicios("codigo_"+pos, hojaForm.getMapaServicios("codigo_"+(pos+1)));
				hojaForm.setMapaServicios("descripcionServicio_"+pos, hojaForm.getMapaServicios("descripcionServicio_"+(pos+1)));
				hojaForm.setMapaServicios("existeBd_"+pos, hojaForm.getMapaServicios("existeBd_"+(pos+1)));
				hojaForm.setMapaServicios("eliminar_"+pos, hojaForm.getMapaServicios("eliminar_"+(pos+1)));
			}
			
			hojaForm.getMapaServicios().remove("codigo_"+numServicios);
			hojaForm.getMapaServicios().remove("descripcionServicio_"+numServicios);
			hojaForm.getMapaServicios().remove("existeBd_"+numServicios);
			hojaForm.getMapaServicios().remove("eliminar_"+numServicios);
			
			hojaForm.setNumServicios(numServicios);
		}
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para ingresar un nuevo servicio del paquete material quirúrgico
	 * @param con
	 * @param hojaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionIngresarNuevoServicio(Connection con, HojaGastosForm hojaForm, ActionMapping mapping) 
	{
		int pos = hojaForm.getNumServicios() - 1;
		String codigosServiciosInsertados = hojaForm.getCodigosServiciosInsertados();
		String codigoServicio = hojaForm.getMapaServicios("codigo_"+pos).toString() ;
		
		//se borra informacion innecesario proveniente de la busqueda genérica de servicios
		hojaForm.getMapaServicios().remove("fueEliminadoServicio_"+pos);
		hojaForm.getMapaServicios().remove("codigoCups_"+pos);
		hojaForm.getMapaServicios().remove("esPos_"+pos);
		hojaForm.getMapaServicios().remove("finalidad_"+pos);
		hojaForm.getMapaServicios().remove("cantidad_"+pos);
		hojaForm.getMapaServicios().remove("urgente_"+pos);
		
		//se adiciona informacion de control
		hojaForm.setMapaServicios("existeBd_"+pos, "false");
		hojaForm.setMapaServicios("eliminar_"+pos, "false");
		
		//se registra el servicio como insertado
		codigosServiciosInsertados += codigoServicio + ",";
		hojaForm.setCodigosServiciosInsertados(codigosServiciosInsertados);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método que muestra el detalle de los servicios de un paquete material quirúrgico
	 * @param con
	 * @param hojaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleProcedimientos(Connection con, HojaGastosForm hojaForm, ActionMapping mapping) 
	{
		String[] datosPaquete = new String[3];
		
		//Si el campo posPaquete viene como codigoNuncaValido quiere decir que se está intentando entrar al detalle de un paquete
		//que aun no tiene procedimientos asociados, de lo contrario, se está intentando entrar al detalle de un paquete que ya tiene
		//procedimientos asociados.
		if(hojaForm.getPosPaquete()!=ConstantesBD.codigoNuncaValido)
			hojaForm.setConsecutivo(hojaForm.getListado("consecutivo_"+hojaForm.getPosPaquete()).toString());
		else
		{
			datosPaquete = hojaForm.getNuevoPaquete().split(ConstantesBD.separadorSplit);
			hojaForm.setConsecutivo(datosPaquete[2]);
		}
		
		//Se consultan los servicios del paquete
		hojaForm.setMapaServicios(HojaGastos.consultarServiciosXConsecutivo(con, hojaForm.getConsecutivo()));
		hojaForm.setNumServicios(Integer.parseInt(hojaForm.getMapaServicios("numRegistros").toString()));
		String codigosServiciosInsertados = "";
		for(int i=0;i<hojaForm.getNumServicios();i++)
			codigosServiciosInsertados += hojaForm.getMapaServicios("codigo_"+i) + ",";
		hojaForm.setCodigosServiciosInsertados(codigosServiciosInsertados);
		
		//Se adicionan los datos del paquete dependendiendo de donde viene la informacion
		if(hojaForm.getPosPaquete()!=ConstantesBD.codigoNuncaValido)
		{
			hojaForm.setMapaServicios("codigoPaquete",hojaForm.getListado("codigo_"+hojaForm.getPosPaquete()).toString());
			hojaForm.setMapaServicios("descripcionPaquete",hojaForm.getListado("descripcion_"+hojaForm.getPosPaquete()).toString());
		}
		else
		{
			hojaForm.setMapaServicios("codigoPaquete",datosPaquete[0]);
			hojaForm.setMapaServicios("descripcionPaquete",datosPaquete[1]);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método que consulta los paquetes que tengan procedimientos definidos
	 * @param con
	 * @param hojaForm
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezarProcedimientos(Connection con, HojaGastosForm hojaForm, UsuarioBasico usuario, ActionMapping mapping) 
	{
		hojaForm.reset();
		
		hojaForm.setListado(HojaGastos.consultarPaquetesMaterialesQx(con, usuario.getCodigoInstitucionInt(), true, false));
		hojaForm.setNumRegistros(Integer.parseInt(hojaForm.getListado("numRegistros").toString()));
		
		//Se cargan los paquetes que no tienen procedimientos asociados para selección
		hojaForm.setSeleccion(HojaGastos.consultarPaquetesMaterialesQx(con, usuario.getCodigoInstitucionInt(), false, true));
		
		//Se toma el maxPagetems
		hojaForm.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listado");
	}

	/**
	 * Método que carga el detalle de los artículos de una paquete
	 * @param con
	 * @param hojaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetallePaquete(Connection con, HojaGastosForm hojaForm, ActionMapping mapping) 
	{
		String[] datosPaquete = new String[3];
		
		//Se toma el consecutivo del paquete
		/**
		 * Nota * Si el estado es detalleArticulosPaquete el flujo es de la funcionalidad de Procedimientos x Paquetes Qx,
		 * delo contrario es flujo de la funcioanlidad de Paquetes Materiales Qx.
		 */
		if(hojaForm.getEstado().equals("detalleArticulosPaquete"))
		{
			//Si posPaquete tiene un valor diferente a código nunca válido, quiere decir que se desea ver el detalle de articulos
			//de un paquete que ya tiene procedimientos asociados, de lo contrario es el detalle de un paquete nuevo que todavía no
			//tiene procedimientos asociados
			if(hojaForm.getPosPaquete()!=ConstantesBD.codigoNuncaValido)
				hojaForm.setConsecutivo(hojaForm.getListado("consecutivo_"+hojaForm.getPosPaquete()).toString());
			else
			{
				datosPaquete = hojaForm.getNuevoPaquete().split(ConstantesBD.separadorSplit);
				hojaForm.setConsecutivo(datosPaquete[2]);
			}
				
		}
		else
			hojaForm.setConsecutivo(hojaForm.getListado("consecutivo_"+hojaForm.getPosPaquete()).toString());
		
		hojaForm.setMapaArticulos(HojaGastos.consultarArticulosXConsecutivo(con, hojaForm.getConsecutivo()));
		hojaForm.setNumArticulos(Integer.parseInt(hojaForm.getMapaArticulos("numRegistros").toString()));
		
		if(hojaForm.getEstado().equals("detalleArticulosPaquete"))
		{
			if(hojaForm.getPosPaquete()!=ConstantesBD.codigoNuncaValido)
			{
				//Se asignan los datos básicos del paquete
				hojaForm.setMapaArticulos("codigoPaquete", hojaForm.getListado("codigo_"+hojaForm.getPosPaquete()).toString());
				hojaForm.setMapaArticulos("descripcionPaquete", hojaForm.getListado("descripcion_"+hojaForm.getPosPaquete()).toString());
			}
			else
			{
				//Se asignan los datos básicos del paquete
				hojaForm.setMapaArticulos("codigoPaquete", datosPaquete[0]);
				hojaForm.setMapaArticulos("descripcionPaquete", datosPaquete[1]);
			}
			
		}
		
		UtilidadBD.closeConnection(con);
		if(hojaForm.getEstado().equals("detalleArticulosPaquete"))
			return mapping.findForward("detalleArticulos");
		else
			return mapping.findForward("principal");
	}

	/**
	 * Método que adiciona/modifica/elimina paquetes de materiales quirurgicos
	 * @param con
	 * @param hojaForm
	 * @param mapping
	 * @param request
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionGuardarPaquete(Connection con, HojaGastosForm hojaForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) 
	{
		ActionErrors errores = new ActionErrors();
		
		//*************VALIDACIONES***************************************************
		//1) Validación de requeridos o caracteres especiales
		for(int i=0;i<hojaForm.getNumRegistros();i++)
		{
			//Campo codigo
			if(hojaForm.getListado("codigo_"+i).toString().equals(""))
				errores.add("",new ActionMessage("errors.required","El código del registro Nro "+(i+1)));
			else if(UtilidadCadena.tieneCaracteresEspecialesGeneral(hojaForm.getListado("codigo_"+i).toString()))
				errores.add("",new ActionMessage("errors.caracteresInvalidos","El código del registro Nro "+(i+1)));
			
			//Campo descripcion
			if(hojaForm.getListado("descripcion_"+i).toString().equals(""))
				errores.add("",new ActionMessage("errors.required","La descripción del registro Nro "+(i+1)));
			else if(UtilidadCadena.tieneCaracteresEspecialesGeneral(hojaForm.getListado("descripcion_"+i).toString()))
				errores.add("",new ActionMessage("errors.caracteresInvalidos","La descripción del registro Nro "+(i+1)));
			
		}
		
		//2) Validación de códigos repetidos
		errores = Listado.validacionValoresRepetidos(errores, hojaForm.getListado(), hojaForm.getNumRegistros(), "codigo_", "códigos");
		//****************************************************************************
		
		
		if(errores.isEmpty())
		{
			//Se llama el método del mundo que guarda los registros
			errores = HojaGastos.guardarPaquetes(
				con, 
				hojaForm.getListado(), 
				hojaForm.getListadoEliminacion(), 
				hojaForm.getNumRegistros(), 
				hojaForm.getNumRegistrosEliminacion(), 
				usuario);
		}
		
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			hojaForm.setEstado("empezar");
		}
		else
		{
			//Se consulta el listado de los paquetes materiales quirurgicos actuales
			hojaForm.setListado(HojaGastos.consultarPaquetesMaterialesQx(con, usuario.getCodigoInstitucionInt(),false, false));
			hojaForm.setNumRegistros(Integer.parseInt(hojaForm.getListado("numRegistros").toString()));
			
			hojaForm.setListadoEliminacion(new HashMap<String, Object>());
			hojaForm.setListadoEliminacion("numRegistros", "0");
			hojaForm.setNumRegistrosEliminacion(0);
			
			hojaForm.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listado");
	}

	/**
	 * Método que realiza la eliminación de un paquete
	 * @param con
	 * @param hojaForm
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionEliminarPaquete(Connection con, HojaGastosForm hojaForm, HttpServletRequest request, HttpServletResponse response) 
	{
		int ultimaPosMapa=hojaForm.getNumRegistros()-1;
		
		//poner la informacion en el otro mapa.
		String[] indices=hojaForm.getListado("indices").toString().split(",");
		//solo pasar al mapa los registros que son de BD
		if(!hojaForm.getListado("consecutivo_"+hojaForm.getPosPaquete()).toString().equals(""))
		{
			for(int i=0;i<indices.length;i++)
				hojaForm.setListadoEliminacion(indices[i]+""+hojaForm.getNumRegistrosEliminacion(), hojaForm.getListado(indices[i]+""+hojaForm.getPosPaquete()));
			hojaForm.setListadoEliminacion("numRegistros", (hojaForm.getNumRegistrosEliminacion()+1)+"");
			hojaForm.setNumRegistrosEliminacion(hojaForm.getNumRegistrosEliminacion()+1);
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=hojaForm.getPosPaquete();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				hojaForm.setListado(indices[j]+""+i,hojaForm.getListado(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			hojaForm.getListado().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamo el numero de registros en el mapa.
		hojaForm.setListado("numRegistros",ultimaPosMapa);
		hojaForm.setNumRegistros(ultimaPosMapa);
		
		UtilidadBD.closeConnection(con);
		return UtilidadSesion.redireccionar(hojaForm.getLinkSiguiente(),hojaForm.getMaxPageItems(),ultimaPosMapa, response, request, "listadoPaquetesMaterialesQx.jsp",hojaForm.getPosPaquete()==ultimaPosMapa);

	}

	/**
	 * Método que realiza la paginacion del listado de paquetes
	 * @param con
	 * @param hojaForm
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, HojaGastosForm hojaForm, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) 
	{
		try
		{
			
		    UtilidadBD.cerrarConexion(con);
			response.sendRedirect(hojaForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de HojaGastosAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en HojaGastosAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Método implementado para realizar la ordenacion de registros del listado de paquetes
	 * @param con
	 * @param hojaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, HojaGastosForm hojaForm, ActionMapping mapping) 
	{
		String listadoIndices = hojaForm.getListado("indices").toString();
		String[] indices=listadoIndices.split(",");
		
		hojaForm.setListado(Listado.ordenarMapa(
			indices,
			hojaForm.getIndice(),
			hojaForm.getUltimoIndice(),
			hojaForm.getListado(),
			hojaForm.getNumRegistros()));
		
		hojaForm.setUltimoIndice(hojaForm.getIndice());
		hojaForm.setListado("numRegistros",hojaForm.getNumRegistros()+"");
		hojaForm.setListado("indices",listadoIndices);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listado");
	}

	/**
	 * Método implementado para añadir un nuevo paquete a la lista de paquetes
	 * @param con
	 * @param hojaForm
	 * @param request 
	 * @param response 
	 * @return
	 */
	private ActionForward accionNuevoPaquete(Connection con, HojaGastosForm hojaForm, HttpServletResponse response, HttpServletRequest request) 
	{
		int pos = hojaForm.getNumRegistros();
		
		hojaForm.setListado("consecutivo_"+pos, "");
		hojaForm.setListado("codigo_"+pos, "");
		hojaForm.setListado("descripcion_"+pos, "");
		hojaForm.setListado("puedoEliminar_"+pos, ConstantesBD.acronimoSi);
		
		pos++;
		hojaForm.setListado("numRegistros", pos+"");
		hojaForm.setNumRegistros(pos);
		
		UtilidadBD.closeConnection(con);
		return UtilidadSesion.redireccionar(hojaForm.getLinkSiguiente(),hojaForm.getMaxPageItems(),pos, response, request, "listadoPaquetesMaterialesQx.jsp",true);
	}

	/**
	 * Método implementado para guardar los artículos de un paquete de materiales quirúrgicos
	 * @param con
	 * @param hojaForm
	 * @param mapping
	 * @param usuario
	 * @param request 
	 * @return
	 */
	private ActionForward accionGuardarArticulos(Connection con, HojaGastosForm hojaForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		
		ActionErrors errores = HojaGastos.guardarArticulos(con, hojaForm.getMapaArticulos(),hojaForm.getNumArticulos(), usuario, hojaForm.getConsecutivo());
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			hojaForm.setEstado("detallePaquete");
		}
		else
		{
			//Se toma el consecutivo del paquete
			hojaForm.setConsecutivo(hojaForm.getListado("consecutivo_"+hojaForm.getPosPaquete()).toString());
			hojaForm.setMapaArticulos(HojaGastos.consultarArticulosXConsecutivo(con, hojaForm.getConsecutivo()));
			hojaForm.setNumArticulos(Integer.parseInt(hojaForm.getMapaArticulos("numRegistros").toString()));
		}
		
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	
	
	
	

	/**
	 * Método implementado para eliminar un articulo de la hoja de gastos
	 * @param con
	 * @param hojaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarArticulo(Connection con, HojaGastosForm hojaForm, ActionMapping mapping) 
	{
		int pos = hojaForm.getPos();
		String codigosArticulosInsertados = hojaForm.getCodigosArticulosInsertados();
		String codigoArticulo = hojaForm.getMapaArticulos("codigoArticulo_"+pos).toString();
		//se quita del listado de servicios insertados
		codigosArticulosInsertados = codigosArticulosInsertados.replaceAll(codigoArticulo+",", "");
		hojaForm.setCodigosArticulosInsertados(codigosArticulosInsertados);
		
		//se verifica si el registro existe en la base de datos
		if(!hojaForm.getMapaArticulos("consecutivo_"+pos).toString().equals(""))
			//se cambia el valor del atributo eliminar en true
			hojaForm.setMapaArticulos("eliminar_"+pos, "true");
		else
		{
			int numArticulos = hojaForm.getNumArticulos() - 1;
			//como no existe en la base de datos se puede borrar del mapa
			for(int i=pos;i<numArticulos;i++)
			{
				hojaForm.setMapaArticulos("consecutivo_"+pos, hojaForm.getMapaArticulos("consecutivo_"+(pos+1)));
				hojaForm.setMapaArticulos("codigoArticulo_"+pos, hojaForm.getMapaArticulos("codigoArticulo_"+(pos+1)));
				hojaForm.setMapaArticulos("descripcionArticulo_"+pos, hojaForm.getMapaArticulos("descripcionArticulo_"+(pos+1)));
				hojaForm.setMapaArticulos("unidadMedidaArticulo_"+pos, hojaForm.getMapaArticulos("unidadMedidaArticulo_"+(pos+1)));
				hojaForm.setMapaArticulos("cantidad_"+pos, hojaForm.getMapaArticulos("cantidad_"+(pos+1)));
				hojaForm.setMapaArticulos("cantidadOriginal_"+pos, hojaForm.getMapaArticulos("cantidadOriginal_"+(pos+1)));
				hojaForm.setMapaArticulos("eliminar_"+pos, hojaForm.getMapaArticulos("eliminar_"+(pos+1)));
			}
			
			hojaForm.getMapaArticulos().remove("consecutivo_"+numArticulos);
			hojaForm.getMapaArticulos().remove("codigoArticulo_"+numArticulos);
			hojaForm.getMapaArticulos().remove("descripcionArticulo_"+numArticulos);
			hojaForm.getMapaArticulos().remove("unidadMedidaArticulo_"+numArticulos);
			hojaForm.getMapaArticulos().remove("cantidad_"+numArticulos);
			hojaForm.getMapaArticulos().remove("cantidadOriginal_"+numArticulos);
			hojaForm.getMapaArticulos().remove("eliminar_"+numArticulos);
			
			hojaForm.setNumArticulos(numArticulos);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	

	/**
	 * Método implementado para ingresar un nuevo articulo a la parametrizacion de hoja de gastos
	 * @param con
	 * @param hojaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionIngresarNuevoArticulo(Connection con, HojaGastosForm hojaForm, ActionMapping mapping) 
	{
		int pos = hojaForm.getNumArticulos() ;
		String codigosArticulosInsertados = hojaForm.getCodigosArticulosInsertados();
		
		//se borra informacion innecesario proveniente de la busqueda genérica de articulos
		hojaForm.getMapaArticulos().remove("fueEliminadoArticulo_"+pos);
		hojaForm.getMapaArticulos().remove("cantidadDespachadaArticulo_"+pos);
		hojaForm.getMapaArticulos().remove("autorizacionArticulo_"+pos);
		hojaForm.getMapaArticulos().remove("tipoPosArticulo_"+pos);
		hojaForm.getMapaArticulos().remove("existenciaXAlmacen_"+pos);
		hojaForm.getMapaArticulos().remove("lote_"+pos);
		hojaForm.getMapaArticulos().remove("existenciaXLote_"+pos);
		hojaForm.getMapaArticulos().remove("fueVencimientoLote_"+pos);
		hojaForm.getMapaArticulos().remove("valorUnitarioOriginal_"+pos);
		hojaForm.getMapaArticulos().remove("valorUnitario_"+pos);
		hojaForm.getMapaArticulos().remove("existeTarifa_"+pos);
		
		//se adiciona informacion de control
		hojaForm.setMapaArticulos("consecutivo_"+pos, "");
		hojaForm.setMapaArticulos("cantidadOriginal_"+pos, "");
		hojaForm.setMapaArticulos("eliminar_"+pos, "false");
		
		
		//se registra el articulo como insertado
		codigosArticulosInsertados += hojaForm.getMapaArticulos("codigoArticulo_"+pos).toString() + ",";
		hojaForm.setCodigosArticulosInsertados(codigosArticulosInsertados);
		
		//se actualiza tamaño del mapa
		pos++;
		hojaForm.setNumArticulos(pos);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	

	/**
	 * Método que inicia el flujo de la funcionalidad de hoja de gastos
	 * @param con
	 * @param hojaForm
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, HojaGastosForm hojaForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		hojaForm.reset();
		
		//Se consulta el listado de los paquetes materiales quirurgicos actuales
		hojaForm.setListado(HojaGastos.consultarPaquetesMaterialesQx(con, usuario.getCodigoInstitucionInt(),false,false));
		hojaForm.setNumRegistros(Integer.parseInt(hojaForm.getListado("numRegistros").toString()));
		
		hojaForm.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listado");
	}

}
