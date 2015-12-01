package com.princetonsa.action.parametrizacion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.Administracion.UtilidadesAdministracion;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.parametrizacion.CentrosCostoXUnidadConsultaForm;
import com.princetonsa.mundo.CentrosCostoXUnidadConsulta;
import com.princetonsa.mundo.UsuarioBasico;

public class CentrosCostoXUnidadConsultaAction extends Action
{
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger = Logger.getLogger(CentrosCostoXUnidadConsultaAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, 
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response ) throws Exception
								{
		Connection con = null;
		try {
			if (response==null); //Para evitar que salga el warning
			if(form instanceof CentrosCostoXUnidadConsultaForm)
			{
				CentrosCostoXUnidadConsultaForm forma=(CentrosCostoXUnidadConsultaForm)form;
				String estado=forma.getEstado();

				con = UtilidadBD.abrirConexion();
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				logger.warn("estado (CentrosCostoXUnidadConsultaAction) -->"+estado);

				if(estado.equalsIgnoreCase("empezar"))
				{
					return accionEmpezar(con, forma, mapping, request, usuario);
				}
				if(estado.equalsIgnoreCase("busqueda"))
				{
					forma.setUltimaPropiedad("");
					forma.setPropiedad("");
					return accionBusqueda(con, forma, mapping, request, usuario);
				}
				if(estado.equalsIgnoreCase("nuevo"))
				{
					forma.setUltimaPropiedad("");
					forma.setPropiedad("");
					//forma.getCentrosCostosUniAgen().clear();
					cerrarConexion(con);
					return mapping.findForward("principal");
				}
				if(estado.equalsIgnoreCase("guardar"))
				{
					forma.setUltimaPropiedad("");
					forma.setPropiedad("");
					logger.info("HOLAAAAAAAAAAAAAAAAAAA!!!");
					return accionGuardar(con, forma, mapping, request, usuario);
				}
				if(estado.equalsIgnoreCase("eliminar"))
				{
					return accionEliminar(con, forma, mapping);
				}
				if(estado.equalsIgnoreCase("ordenar"))
				{
					cerrarConexion(con);
					return accionOrdenar(forma, mapping);
				}
				if(estado.equalsIgnoreCase("activarModificar"))
				{
					return accionActivarModificar(con,forma,response);
				}
				if(estado.equalsIgnoreCase("filtraUnidadAgenda"))
				{
					return accionFiltrarUnidadAgenda(con, forma, usuario, response); 
				}

			}
			else
			{
				logger.error("Froma invalida. Revise el struts-config");
				return ComunAction.accionSalirCasoError(mapping, request, null, logger, "Forma Inválida", "errors.formaTipoInvalido", true);
			}
			return ComunAction.accionSalirCasoError(mapping, request, null, logger, "La accion específicada no esta definida", "errors.estadoInvalido", true);
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
     }

	/**
	 * Método implementado para activar la modificacion de un registro
	 * de la funcionalidad centros de costo x unidad de agenda
	 * @param con
	 * @param forma
	 * @param response
	 * @return
	 */
	private ActionForward accionActivarModificar(Connection con,
			CentrosCostoXUnidadConsultaForm forma, HttpServletResponse response) 
	{
		///Se consultan las salas
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoInnerHtml</sufijo>" +
				"<id-div>tdCentroCosto_"+forma.getIndiceModificado()+"</id-div>" +
				"<ind-reemplazo>"+ConstantesBD.acronimoSi+"</ind-reemplazo>" ; //variable que indica si se deben hacer reemplazos de % y @
			
		
		forma.setMapa("modificar_activado_"+forma.getIndiceModificado(), ConstantesBD.acronimoSi);
		
		/**
		 * Comentario: como la respuesta XML no soporta etiquetas de HTML, entonces se debe manejar el caracter % = < y @ = >
		 * manando el ind-reemplazo en S para que el ajaxGenérico haga el reemplazo
		 */
		
		// Anexo 810 - Cambios Funcionalidades Consulta Externa EntidadesSubcontratadas
		//-----------------------------------
		/*UsuarioBasico usuario = new UsuarioBasico();
		ArrayList<HashMap<String,Object>> array = new ArrayList<HashMap<String,Object>>();
		array.clear();
		if(Utilidades.convertirAEntero(forma.getMapa("unidad_consulta_"+forma.getIndiceModificado()).toString()) !=ConstantesBD.codigoNuncaValido)
		{
			forma.setListadoCentrosCosto(UtilidadesAdministracion.obtenerCentroCosto(con,
					usuario.getCodigoInstitucion(),
					forma.getCodigoCentroAtencion(),
					ConstantesIntegridadDominio.acronimoInterna));
		}*/
		//array.iterator();
		//-----------------------------------
		
		Iterator iterador = forma.getListadoCentrosCosto().iterator();
		String contenido = "%select name=\"mapa(centro_costo_"+forma.getIndiceModificado()+")\" @ " ;
		while(iterador.hasNext())
		{
			HashMap fila = (HashMap)iterador.next();
			contenido += "%option value=\""+fila.get("codigo")+"\" ";
			
			if(fila.get("codigo").toString().equals(forma.getMapa("centro_costo_"+forma.getIndiceModificado())+""))
				contenido += " selected ";
			
			contenido += "@"+UtilidadTexto.cambiarCaracteresEspeciales(fila.get("id")+"")+" - "+
					UtilidadTexto.cambiarCaracteresEspeciales(fila.get("nombre")+"")+"%/option@";
			
			//contenido += "@"+UtilidadTexto.cambiarCaracteresEspeciales(fila.get("codigo_nombre")+"")+"%/option@";
			//contenido += "@"+fila.get("codigo_nombre")+"%/option@";
		}
		contenido += "%/select@";
		
		resultado += "<contenido>"+contenido+"</contenido>";
		resultado += "</infoid></respuesta>";
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			//response.setCharacterEncoding(arg0)
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionActivarModificar: "+e);
		}
		return null;
	}

	/**
	 * Método para ordenar por columnas
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(CentrosCostoXUnidadConsultaForm forma, ActionMapping mapping)
	{
		String[] indices={"unidad_consulta_", "nombre_unidad_consulta_", "centro_costo_", "nombre_centro_costo_"};
		int numRegistros=(Integer)forma.getMapa().get("numRegistros");
		HashMap mapa=Listado.ordenarMapa(indices, forma.getPropiedad(), forma.getUltimaPropiedad(), forma.getMapa(), numRegistros);
		mapa.put("numRegistros", numRegistros);
		forma.setMapa(mapa);
		if(!forma.getUltimaPropiedad().equalsIgnoreCase(forma.getPropiedad()))
		{
			forma.setUltimaPropiedad(forma.getPropiedad());
		}
		else
		{
			forma.setUltimaPropiedad("");
		}
		return mapping.findForward("principal");
	}

	/**
	 * Método para eliminar un registro
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminar(Connection con, CentrosCostoXUnidadConsultaForm forma, ActionMapping mapping)
	{
		HashMap mapa=forma.getMapa();
		int numRegistros=(Integer)mapa.get("numRegistros");
		for(int i=forma.getIndiceEliminado(); i<numRegistros-1; i++)
		{
			int unidadConsulta=Integer.parseInt(mapa.get("unidad_consulta_"+(i+1))+"");
			int centroCosto=Integer.parseInt(mapa.get("centro_costo_"+(i+1))+"");
			String nombreUnidadConsulta=(String)mapa.get("nombre_unidad_consulta_"+(i+1));
			String nombreCentroCosto=(String)mapa.get("nombre_centro_costo_"+(i+1));
			String modificarActivado = (String)mapa.get("modificar_activado_"+(i+1));
			mapa.put("unidad_consulta_"+i, unidadConsulta);
			mapa.put("centro_costo_"+i, centroCosto);
			mapa.put("nombre_unidad_consulta_"+i, nombreUnidadConsulta);
			mapa.put("nombre_centro_costo_"+i, nombreCentroCosto);
			mapa.put("modificar_activado_"+i, modificarActivado);
		}
		mapa.put("unidad_consulta_"+(numRegistros-1), null);
		mapa.put("centro_costo_"+(numRegistros-1), null);
		mapa.put("nombre_unidad_consulta_"+(numRegistros-1), null);
		mapa.put("nombre_centro_costo_"+(numRegistros-1), null);
		mapa.put("modificar_activado_"+(numRegistros-1), null);
		mapa.put("numRegistros", numRegistros-1);
		forma.setIndiceEliminado(0);
		cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método para almacenar los registros en la BD
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return
	 */
	 @SuppressWarnings("unused")
	private ActionForward accionGuardar(Connection con, CentrosCostoXUnidadConsultaForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario)
	{
		HashMap mapa=forma.getMapa();
		HashMap mapaBD=CentrosCostoXUnidadConsulta.consultarRegistros(con, usuario.getCodigoInstitucionInt(), forma.getCentroAtencion());
		int numRegistros=(Integer)mapa.get("numRegistros");
		logger.info("numRegistros--> "+numRegistros);
		int numRegistrosBD=Integer.parseInt(mapaBD.get("numRegistros")+"");
		logger.info("numRegistrosBD--> "+numRegistrosBD);
		Vector elementos=new Vector();
		for(int i=0; i<numRegistros; i++)
		{
			int unidadConsulta=Integer.parseInt(mapa.get("unidad_consulta_"+i)+"");
			logger.info("unidadConsulta--> "+unidadConsulta);
			int centroCosto=Integer.parseInt(mapa.get("centro_costo_"+i)+"");
			logger.info("centroCosto--> "+centroCosto);
			String modificarActivo = mapa.get("modificar_activado_"+i)+"";
			logger.info("modificarActivo--> "+modificarActivo);
			Vector elemento=new Vector();
			elemento.add(centroCosto);
			elemento.add(unidadConsulta);
			elementos.add(elemento);
			logger.info("elemento--> "+elemento);
		}
		Iterator<HashMap> centrosAtencion=forma.getListadoCentrosAtencion().iterator();
		String nombreCentroAtencion="";
		logger.info("nombreCentroAtencion");
		while(centrosAtencion.hasNext())
		{
			HashMap centroAt=centrosAtencion.next();
			logger.info("centroAt--->"+centroAt);
			Integer centroAtencion=Utilidades.convertirAEntero(centroAt.get("consecutivo")+"");
			logger.info("centroAtencion--->"+centroAtencion);
			
			if( centroAtencion==forma.getCentroAtencion())
			{
				nombreCentroAtencion=(String)centroAt.get("nombre");
				logger.info("nombreCentroAtencion--->"+nombreCentroAtencion);
				break;
			}
		}
		
		String log="\n            ====INFORMACIÓN ELIMINADA===== ";
		log+="\n *  Centro Atención [ "+forma.getCodigoCentroAtencion().trim()+" - "+nombreCentroAtencion+" ]\n";
		
		boolean eliminados=false;
		for(int i=0; i<numRegistrosBD; i++)
		{
			int unidadConsulta=Integer.parseInt(mapaBD.get("unidad_consulta_"+i)+"");
			int centroCosto=Integer.parseInt(mapaBD.get("centro_costo_"+i)+"");
			Vector elemento=new Vector();
			elemento.add(centroCosto);
			elemento.add(unidadConsulta);
			if(!elementos.contains(elemento))
			{
				eliminados=true;
				log += "\n *  Unidad Consulta [ "+mapaBD.get("unidad_consulta_"+i)+" - "+mapaBD.get("nombre_unidad_consulta_"+i)+" ]" ;
				log += "\n *  Centro Costo    [ "+mapaBD.get("centro_costo_"+i)+" - "+mapaBD.get("nombre_centro_costo_"+i)+" ]\n" ;
			}
		}
		
		LogsAxioma.enviarLog(ConstantesBD.logCentroCostoXUnidadesConsultaCodigo, log, ConstantesBD.tipoRegistroLogEliminacion, usuario.getLoginUsuario());
		CentrosCostoXUnidadConsulta mundo=new CentrosCostoXUnidadConsulta();
		mundo.setCentroAtencion(forma.getCentroAtencion());
		mundo.setRegistros(elementos); 
		mundo.guardar(con);
		cerrarConexion(con);
		
		//******se inactiva de nuevo el campo modificar_activado**********+
		for(int i=0; i<numRegistros; i++)
			forma.setMapa("modificar_activado_"+i, ConstantesBD.acronimoNo);
		
		return mapping.findForward("principal");
	}

	/**
	 * Buscar los centros de costo que pertenecen al centro de atención seleccionado
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBusqueda(Connection con, CentrosCostoXUnidadConsultaForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario)
	{
		//Si se seleccionó un centro de atención se realiza el proceso de consultar las estructuras
		if(forma.getCentroAtencion()>0)
		{
		
			Collection col=CentrosCostoXUnidadConsulta.consultarListados(con, 2, 0, 0);
			if(col==null)
			{
				return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.problemasBd", "errors.problemasBd", true);
			}
			forma.setListadoUnidadesConsulta(col);
			col=CentrosCostoXUnidadConsulta.consultarListados(con, 1, usuario.getCodigoInstitucionInt(), forma.getCentroAtencion());
			if(col==null)
			{
				return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.problemasBd", "errors.problemasBd", true);
			}
			HashMap mapa=CentrosCostoXUnidadConsulta.consultarRegistros(con, usuario.getCodigoInstitucionInt(), forma.getCentroAtencion());
			forma.setListadoCentrosCosto(col);
			if(mapa==null)
			{
				return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.problemasBd", "errors.problemasBd", true);
			}
			mapa.put("numRegistros", Integer.parseInt((String)mapa.get("numRegistros")));
			forma.setMapa(mapa);
		}
		cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Manejo de la accion empezar
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, CentrosCostoXUnidadConsultaForm forma, ActionMapping mapping, HttpServletRequest request,UsuarioBasico usuario)
	{
		forma.reset();
		
		Collection col=CentrosCostoXUnidadConsulta.consultarListados(con, 3, usuario.getCodigoInstitucionInt(), 0);
		if(col==null)
		{
			return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.problemasBd", "errors.problemasBd", true);
		}
		forma.setListadoCentrosAtencion(col);
		
		/*
		forma.getListadoCentrosCosto().clear();
		if(Utilidades.convertirAEntero(forma.getIndexCodUniAgen())>0)
		{
			forma.setListadoCentrosCosto(UtilidadesAdministracion.obtenerCentroCosto(con,
					usuario.getCodigoInstitucion(),
					forma.getCodigoCentroAtencion(),
					ConstantesIntegridadDominio.acronimoInterna));
		}
		logger.info("despues ++ listado centro costos >>> "+forma.getListadoCentrosCosto().size());
		logger.info("listado centro costos >>> "+forma.getListadoCentrosCosto());
		*/
		cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método pàra cerrar la conexión con la BD 
	 * @param con
	 */
	private void cerrarConexion(Connection con)
	{
		try
		{
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.error("Error cerrando la conexión "+e);
		}
	}
	
	
	/**
	 * Método implementado para realizar el filtro de unidades de agenda
	 * @param con
	 * @param cargosForm
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltrarUnidadAgenda(Connection con, CentrosCostoXUnidadConsultaForm forma, UsuarioBasico usuario, HttpServletResponse response) 
	{
		String propiedad = "";
		//Se consultan los Centros Costos
		forma.getCentrosCostosUniAgen().clear();
		if(Utilidades.convertirAEntero(forma.getIndexCodUniAgen()) !=ConstantesBD.codigoNuncaValido)
		{
			logger.info("entra a consultar los centros de costos ");
			logger.info("centro de costo >>> "+forma.getCodigoCentroAtencion());
			forma.setCentrosCostosUniAgen(UtilidadesAdministracion.obtenerCentroCosto(con,
					usuario.getCodigoInstitucion(),
					forma.getCodigoCentroAtencion(),
					ConstantesIntegridadDominio.acronimoInterna));
		}
		
		propiedad="mapa(centro_costo_"+forma.getMapa().get("numRegistros")+")";
		
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>centro_costo</id-select>" +
				"<id-arreglo>centrosCostosUniAgen</id-arreglo>" + //nombre de la etiqueta de cada elemento
			"</infoid>";
		
		Iterator iter = forma.getCentrosCostosUniAgen().iterator();
		while(iter.hasNext())
		{
			HashMap elemento = (HashMap) iter.next();
			resultado += "<centrosCostosUniAgen>";
				resultado += "<codigo>"+elemento.get("codigo")+"</codigo>";
				resultado += "<descripcion>"+elemento.get("id")+" - "+elemento.get("nombre")+"</descripcion>";
			resultado += "</centrosCostosUniAgen>";
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
			logger.error("Error al enviar respuesta AJAX en accionFiltrarUnidadAgenda: "+e);
		}
		return null;
	}
}
