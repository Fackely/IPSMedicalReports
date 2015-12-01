/*
 * @(#)FormatoImpresionPresupuestoAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action.presupuesto;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpSession;
import com.princetonsa.dao.DaoFactory;
import org.apache.struts.action.Action;
import com.princetonsa.action.ComunAction;
import org.apache.struts.action.ActionForm;
import com.princetonsa.mundo.UsuarioBasico;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.mundo.presupuesto.FormatoImpresionPresupuesto;
import com.princetonsa.actionform.presupuesto.FormatoImpresionPresupuestoForm;

/**
 * Clase encargada del control de la funcionalidad de Formato de Impresion de Presupuesto

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 11 /Ene/ 2006
 */
public class FormatoImpresionPresupuestoAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(FormatoImpresionPresupuestoAction.class);
	boolean esNuevo=false;

	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if(form instanceof FormatoImpresionPresupuestoForm)
			{


				/**Intentamos abrir una conexion con la fuente de datos**/ 
				con = openDBConnection(con); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				FormatoImpresionPresupuestoForm forma=(FormatoImpresionPresupuestoForm)form;

				HttpSession session = request.getSession();
				UsuarioBasico usuario= (UsuarioBasico)session.getAttribute("usuarioBasico");
				FormatoImpresionPresupuesto mundo =new FormatoImpresionPresupuesto();
				String estado = forma.getEstado();
				logger.warn("[FormatoImpresionPresupuestoAction] estado->"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de FormatoImpresionPresupuestoAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(mapping, forma, con, usuario, mundo);
				}
				else if(estado.equals("modificar"))
				{
					return this.accionModificar(forma, mapping, con, mundo);
				}
				else if(estado.equals("nuevo"))
				{
					return this.accionNuevo(mapping, forma, con, mundo, usuario);
				}
				else if(estado.equals("eliminarGrupo"))
				{
					return this.accionEliminarDelMapa(forma, mapping, con, mundo);
				}
				else if(estado.equals("recargarBusqueda"))
				{
					return this.accionAgregarGrupo(forma, mapping, con, mundo);
				}
				else if(estado.equals("guardarModificacion"))
				{
					return this.accionGuardarModificacion(forma, request, mapping, con, mundo, usuario);
				}
				else if(estado.equals("guardarNuevo"))
				{
					return this.accionGuardarNuevo(forma, request, mapping, con, mundo, usuario);
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de Formato de Impresion de Presupuesto");
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
	 * Acción de empezar el flujo de la funcionalidad solo cargando los formatos de
	 * impresión de presupuestos existentes
	 * @param mapping
	 * @param forma
	 * @param con
	 * @param usuario
	 * @param mundo
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, FormatoImpresionPresupuestoForm forma, Connection con, UsuarioBasico usuario, FormatoImpresionPresupuesto mundo) throws Exception
	{
		forma.reset();
		forma.setMapaFormatosPrevios(mundo.consultarFormatosExistentes(con, usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaSeleccion");
	}
	
	
	/**
	 * Accion para generar un formato de impresion de presupuesto Nuevo
	 * @param mapping
	 * @param forma
	 * @param con
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionNuevo(ActionMapping mapping, FormatoImpresionPresupuestoForm forma, Connection con, FormatoImpresionPresupuesto mundo, UsuarioBasico usuario) throws Exception
	{
		forma.reset();
		esNuevo=true;
		forma.setMapaFormatosPrevios(mundo.consultarFormatosExistentes(con, usuario.getCodigoInstitucionInt()));
		forma.setMapaGruposServicios(mundo.consultarGruposServicios(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}
	
	
	/**
	 * Accion para agregar un grupo nuevo al detalle de servicios tomando en cuenta que el grupo no exista
	 * en caso de existir ya no lo adiciona.
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param mundo
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionAgregarGrupo(FormatoImpresionPresupuestoForm forma, ActionMapping mapping, Connection con, FormatoImpresionPresupuesto mundo) throws SQLException
	{
		int codigoFormato=forma.getCodigoFormato();
		forma.setMapaGruposServicios(mundo.consultarGruposServicios(con));
		//forma.setMapaFormatoImpresionBasico(mundo.consultarFormatoImpresion(con, codigoFormato));
		//forma.setMapaDetArticulos(mundo.consultarDetAritculos(con, codigoFormato));
		boolean existeGrupo=false;
		String nombreGrupo="", temporalGrupo="";
		int index=0, indicador=0;
		if(forma.getCodigoGrupo()!=0)
		{
			nombreGrupo=Utilidades.getNombreGrupoServicios(con, forma.getCodigoGrupo());
			if(forma.getMapaDetServicios().size()/7==0 && indicador==0)
			{
				forma.getMapaDetServicios().put("grupo_"+index, nombreGrupo);
				forma.getMapaDetServicios().put("valoresdetalle_"+index, "false");
				forma.getMapaDetServicios().put("subtotalgrupo_"+index, "false");
				forma.getMapaDetServicios().put("detalle_"+index, "false");
				forma.getMapaDetServicios().put("prioridad_"+index, "");
				forma.getMapaDetServicios().put("codigoformato_"+index, codigoFormato+"");
				forma.getMapaDetServicios().put("codigogrupo_"+index, forma.getCodigoGrupo()+"");
				forma.getMapaDetServicios().put("numRegistros", index+1+"");
				indicador=1;
			}
			else
			{
				
				index=forma.getMapaDetServicios().size()/7;
				for (int k = 0 ; k < index ; k++)
				{
					temporalGrupo=forma.getMapaDetServicios("grupo_"+k)+"";
					if(temporalGrupo.equals(nombreGrupo))
					{
						existeGrupo=true;
					}
				}
				if(!existeGrupo)
				{
					forma.getMapaDetServicios().put("grupo_"+index, nombreGrupo);
					forma.getMapaDetServicios().put("valoresdetalle_"+index, "false");
					forma.getMapaDetServicios().put("subtotalgrupo_"+index, "false");
					forma.getMapaDetServicios().put("detalle_"+index, "false");
					forma.getMapaDetServicios().put("prioridad_"+index, "");
					forma.getMapaDetServicios().put("codigoformato_"+index, codigoFormato+"");
					forma.getMapaDetServicios().put("codigogrupo_"+index, forma.getCodigoGrupo()+"");
					index=index+1;
					forma.setMapaDetServicios("numRegistros", index+"");
				}
			}
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * Accion para modificar un formato de impresion de presupuesto
	 * seleccionado de los ya existentes
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param mundo
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionModificar(FormatoImpresionPresupuestoForm forma, ActionMapping mapping, Connection con, FormatoImpresionPresupuesto mundo) throws SQLException
	{
		int codigoFormato=forma.getCodigoFormato();
		forma.setMapaGruposServicios(mundo.consultarGruposServicios(con));
		forma.setMapaDetServicios(mundo.consultarDetServicios(con, codigoFormato));
		forma.setMapaFormatoImpresionBasico(mundo.consultarFormatoImpresion(con, codigoFormato));
		forma.setMapaDetArticulos(mundo.consultarDetAritculos(con, codigoFormato));
		forma.setCantidad(UtilidadTexto.getBoolean(forma.getMapaFormatoImpresionBasico("cantidad_0")+""));
		forma.setValorUnitario(UtilidadTexto.getBoolean(forma.getMapaFormatoImpresionBasico("valorunitario_0")+""));
		forma.setValorTotal(UtilidadTexto.getBoolean(forma.getMapaFormatoImpresionBasico("valortotal_0")+""));
		/**Mapas Auxiliares para el LOG**/
		forma.setMapaAuxBasico(mundo.consultarFormatoImpresion(con, codigoFormato));
		forma.setMapaAuxDetArt(mundo.consultarDetAritculos(con, codigoFormato));
		forma.setMapaAuxDetServ(mundo.consultarDetServicios(con, codigoFormato));
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	
	/**
	 * Accion para guardar la modificacion de un formato de Impresion 
	 * de presupuesto existente
	 * @param forma
	 * @param request
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param mundo
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGuardarModificacion(FormatoImpresionPresupuestoForm forma, HttpServletRequest request, ActionMapping mapping, Connection con, FormatoImpresionPresupuesto mundo, UsuarioBasico usuario) throws SQLException
	{	
		if(!forma.isCantidad())
		{
			forma.setDescripcionCantidad("");
		}
		if(!forma.isValorUnitario())
		{
			forma.setDescripcionValUnitario("");
		}
		if(!forma.isValorTotal())
		{
			forma.setDescripcionValTotal("");
		}
		int guardarMod=mundo.modificarFormatoImpresion(con, forma.getCodigoFormato(), forma.getNombreFormato(), forma.getTituloFormato(), forma.isCantidad(), forma.getDescripcionCantidad(), forma.isValorUnitario(), forma.getDescripcionValUnitario(), forma.isValorTotal(), forma.getDescripcionValTotal(), forma.getPiePagina(),forma.isFechaHora(), forma.getMapaDetServicios(), forma.getMapaDetArticulos());
		if(guardarMod<=0)
		{
			return ComunAction.accionSalirCasoError(mapping, request,con,logger, "no se guardo modificacion","error.manejoPaciente.formatoImpresionPresupuesto.noSeGuardoModificacion", true);
		}
		else
		{
			forma.setMensajeExitoso("La Modificación del Formato de Impresión de Presupuesto: "+forma.getNombreFormato().toUpperCase()+" fue exitosa.");
			/**Despues de guardar en la base de datos generamos el log**/
	        generarLogModificacion(forma,usuario);
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaSeleccion");		
	}
	
	
	/**
	 * Accion para guardar un Nuevo formato de impresion de presupuesto
	 * @param forma
	 * @param request
	 * @param mapping
	 * @param con
	 * @param mundo
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGuardarNuevo(FormatoImpresionPresupuestoForm forma, HttpServletRequest request, ActionMapping mapping, Connection con, FormatoImpresionPresupuesto mundo, UsuarioBasico usuario) throws SQLException
	{
		int guardarNuevo=mundo.insertarFormatoImpresion(con, forma.getNombreFormato(), forma.getTituloFormato(), forma.isCantidad(), forma.getDescripcionCantidad(), forma.isValorUnitario(), forma.getDescripcionValUnitario(), forma.isValorTotal(), forma.getDescripcionValTotal(), forma.getPiePagina(),forma.isFechaHora(), usuario.getCodigoInstitucionInt(), forma.getMapaDetServicios(), forma.getMapaDetArticulos());
		if(guardarNuevo<=0)
		{
			return ComunAction.accionSalirCasoError(mapping, request,con,logger, "no se guardo modificacion","error.manejoPaciente.formatoImpresionPresupuesto.noSeGuardoModificacion", true);
		}
		else
		{
			forma.setMensajeExitoso("El nuevo Formato de Impresión de Presupuesto se guardó exitosamente.");
			forma.setMapaFormatosPrevios(mundo.consultarFormatosExistentes(con, usuario.getCodigoInstitucionInt()));
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaSeleccion");		
	}
		
	
	/**
	 * Método para generar el log de base de datos mostrando los dato antes y despues de la modificación
	 * @param forma
	 * @param usuario
	 * @throws SQLException
	 */
	public void generarLogModificacion(FormatoImpresionPresupuestoForm forma, UsuarioBasico usuario)  throws SQLException
	{
		
		String log="\n            ===============INFORMACION ORIGINAL DEL FORMATO DE IMPRESION DE PRESUPUESTO===============             " +
		"\n*  Nombre Formato [" +forma.getMapaAuxBasico("nombreformato_0") +"] "+
		"\n*  Titulo Formato ["+forma.getMapaAuxBasico("tituloformato_0")+"] " +
		"\n*  Cantidad ["+forma.getMapaAuxBasico("cantidad_0")+"] " +
		"\n*  Descripción Cantidad ["+forma.getMapaAuxBasico("desccantidad_0")+"] " +
		"\n*  Valor Unitario ["+forma.getMapaAuxBasico("valorunitario_0")+"] " +
		"\n*  Descripción Valor Unitario ["+forma.getMapaAuxBasico("descvalunitario_0")+"] "+ 
		"\n*  Valor Total ["+forma.getMapaAuxBasico("valortotal_0")+"] " +
		"\n*  Valor Unitario ["+forma.getMapaAuxBasico("descvaltotal_0")+"] " ;
		
		/**Detalle de Servicios**/
		log+="\n ******* DETALLE DE SERVICIOS ******* \n";
		for(int k = 0; k<Integer.parseInt(forma.getMapaAuxDetServ("numRegistros")+""); k++)
		{
			
			log+= "\n*  Grupo Servicios ["+forma.getMapaAuxDetServ("grupo_"+k)+"] " +
				 "\n*  Prioridad ["+forma.getMapaAuxDetServ("prioridad_"+k)+"] " +
				 "\n*  Mostrar Detalle de Servicios? ["+forma.getMapaAuxDetServ("detalle_"+k)+"] " +
				 "\n*  Mostrar Valores Detalle Servicio? ["+forma.getMapaAuxDetServ("valoresdetalle_"+k)+"] " +
				 "\n*  Mostrar Subtotal del Grupo? ["+forma.getMapaAuxDetServ("subtotalgrupo_"+k)+"] "  ;
		}
		
		/**Detalle De Articulos**/
		log+="\n ******* DETALLE DE ARTICULOS ******* \n" +
			 "\n*  Prioridad ["+forma.getMapaAuxDetArt("prioridad_0")+"] " +
			 "\n*  Descripción Sección Articulos ["+forma.getMapaAuxDetArt("descseccionarticulo_0")+"] " +
			 "\n*  Mostrar Valores Detalle Articulo? ["+forma.getMapaAuxDetArt("detarticulo_0")+"] " +
			 "\n*  Nivel ["+forma.getMapaAuxDetServ("nivel_0")+"] " +
			 "\n*  Mostrar Detalle Articulo? ["+forma.getMapaAuxDetArt("detallenivel_0")+"] " +  
			 "\n*  Mostrar Valores Detalle Articulo? ["+forma.getMapaAuxDetArt("valoresdetalle_0")+"] "  +
			 "\n*  Mostrar Subtotal Articulo? ["+forma.getMapaAuxDetArt("subtotalnivel_0")+"] "  ;
	
		log+="\n*  Nota de Pie de Página ["+forma.getMapaAuxBasico("notapiepagina_0")+"] " ;
		log+="\n* Imprimir fecha y hora de la impresión en pie de Página ? ["+forma.getMapaAuxBasico("fechahora_0")+"] \n" ;
		
		
		log+="\n            ===============INFORMACION DESPUES DE LA MODIFICACION DEL FORMATO DE IMPRESION DE PRESUPUESTO===============             " +
		"\n*  Nombre Formato [" +forma.getMapaFormatoImpresionBasico("nombreformato_0") +"] "+
		"\n*  Titulo Formato ["+forma.getTituloFormato()+"] " +
		"\n*  Cantidad ["+forma.isCantidad()+"] " +
		"\n*  Descripción Cantidad ["+forma.getDescripcionCantidad()+"] " +
		"\n*  Valor Unitario ["+forma.isValorUnitario()+"] " +
		"\n*  Descripción Valor Unitario ["+forma.getDescripcionValUnitario()+"] "+ 
		"\n*  Valor Total ["+forma.isValorTotal()+"] " +
		"\n*  Valor Unitario ["+forma.getDescripcionValTotal()+"] " ;
		
		/**Detalle de Servicios**/
		log+="\n ******* DETALLE DE SERVICIOS ******* \n";
		for(int k = 0; k<Integer.parseInt(forma.getMapaDetServicios("numRegistros")+""); k++)
		{
			
		   log+= "\n*  Grupo Servicios ["+forma.getMapaDetServicios("grupo_"+k)+"] " +
				 "\n*  Prioridad ["+forma.getMapaDetServicios("prioridad_"+k)+"] " +
				 "\n*  Mostrar Detalle de Servicios? ["+forma.getMapaDetServicios("detalle_"+k)+"] " +
				 "\n*  Mostrar Valores Detalle Servicio? ["+forma.getMapaDetServicios("valoresdetalle_"+k)+"] " +
				 "\n*  Mostrar Subtotal del Grupo? ["+forma.getMapaDetServicios("subtotalgrupo_"+k)+"] "  ;
		}
		
		/**Detalle De Articulos**/
		log+="\n ******* DETALLE DE ARTICULOS ******* \n" +
			 "\n*  Prioridad ["+forma.getMapaDetArticulos("prioridad_0")+"] " +
			 "\n*  Descripción Sección Articulos ["+forma.getMapaDetArticulos("descseccionarticulo_0")+"] " +
			 "\n*  Mostrar Valores Detalle Articulo? ["+forma.getMapaDetArticulos("detarticulo_0")+"] " +
			 "\n*  Nivel ["+forma.getMapaDetArticulos("nivel_0")+"] " +
			 "\n*  Mostrar Detalle Articulo? ["+forma.getMapaDetArticulos("detallenivel_0")+"] " +  
			 "\n*  Mostrar Valores Detalle Articulo? ["+forma.getMapaDetArticulos("valoresdetalle_0")+"] "  +
			 "\n*  Mostrar Subtotal Articulo? ["+forma.getMapaDetArticulos("subtotalnivel_0")+"] "  ;
	
		log+="\n*  Nota de Pie de Página ["+forma.getMapaFormatoImpresionBasico("notapiepagina_0")+"] " ;
		log+="\n* Imprimir fecha y hora de la impresión en pie de Página ? ["+forma.getMapaFormatoImpresionBasico("fechahora_0")+"] " ;
		
		
		log+="\n==========================================================================================================\n\n\n " ;	
		
		LogsAxioma.enviarLog(ConstantesBD.logFormatoImpresionPresupuestoCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
	}
	
	
	private ActionForward accionEliminarDelMapa(FormatoImpresionPresupuestoForm forma, ActionMapping mapping, Connection con, FormatoImpresionPresupuesto mundo) throws SQLException
	{
	    int tamanioMapaAntesEliminar=Integer.parseInt(forma.getMapaDetServicios("numRegistros")+"");
	    forma.getMapaDetServicios().remove("grupo_"+forma.getPosicionMapa());
	    forma.getMapaDetServicios().remove("valoresdetalle_"+forma.getPosicionMapa());
	    forma.getMapaDetServicios().remove("subtotalgrupo_"+forma.getPosicionMapa());
	    forma.getMapaDetServicios().remove("prioridad_"+forma.getPosicionMapa());
	    forma.getMapaDetServicios().remove("codigoformato_"+forma.getPosicionMapa());
	    forma.getMapaDetServicios().remove("codigogrupo_"+forma.getPosicionMapa());
	    forma.setMapaDetServicios("numRegistros",tamanioMapaAntesEliminar-1+"");
	    
	    /**El siguiente cod, lo que verifica, es que si eliminan un elemento del mapa y atrás de el existen más datos entonces
	     * se debe correr el index del hash Map de los que estan después del eliminado una posición menos para que existe 
	     * concordancia entre el size real del mapa y el index de elementos **/
	    int indexEliminado= forma.getPosicionMapa();
	    if(tamanioMapaAntesEliminar>indexEliminado)
	    {
	        for(int k=indexEliminado; k<tamanioMapaAntesEliminar; k++)
	        {
	        	forma.setMapaDetServicios("grupo_"+k, forma.getMapaDetServicios("grupo_"+(k+1)));
	        	forma.setMapaDetServicios("valoresdetalle_"+k, forma.getMapaDetServicios("valoresdetalle_"+(k+1)));
	        	forma.setMapaDetServicios("subtotalgrupo_"+k, forma.getMapaDetServicios("subtotalgrupo_"+(k+1)));
	        	forma.setMapaDetServicios("prioridad_"+k, forma.getMapaDetServicios("prioridad_"+(k+1)));
	        	forma.setMapaDetServicios("codigoformato_"+k, forma.getMapaDetServicios("codigoformato_"+(k+1)));
	        	forma.setMapaDetServicios("codigogrupo_"+k, forma.getMapaDetServicios("codigogrupo_"+(k+1)));
	        }
	        /**Removemos la ultima posicion del mapa que quedo vacias despues de mover los otros registros**/
	        forma.getMapaDetServicios().remove("grupo_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaDetServicios().remove("valoresdetalle_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaDetServicios().remove("subtotalgrupo_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaDetServicios().remove("prioridad_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaDetServicios().remove("codigoformato_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaDetServicios().remove("codigogrupo_"+(tamanioMapaAntesEliminar-1));
	    }
	    
	    UtilidadBD.closeConnection(con);
	    return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * Abrir la conceccion con la Base de Datos
	 * @param con
	 * @return
	 */
	 public Connection openDBConnection(Connection con)
		{
			if(con != null)
			{
				return con;
			}
			try
			{
				String tipoBD = System.getProperty("TIPOBD");
				DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
				con = myFactory.getConnection();
			}
			catch(Exception e)
			{
				logger.warn("Problemas con la base de datos al abrir la conexion "+e.toString());
				return null;
			}
		
			return con;
		}
	 
	 	/**
		 * Tomar el usuario basico cargado en session
		 * @param session
		 * @return
		 */
		 private UsuarioBasico getUsuarioBasicoSesion(HttpSession session)
		 {
			UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
		    if(usuario == null)
		    {
				logger.warn("El usuario no esta cargado (null)");
		    }
		    return usuario;
		 }
}