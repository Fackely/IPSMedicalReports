/*
 * Creado en Aug 9, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.action.pyp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.pyp.CalificacionesXCumpliMetasForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.pyp.CalificacionesXCumpliMetas;

/**
 * Clase que controla la funcionalidad de calificaciones por
 * cumplimiento de metas
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales) 
 * @version Aug 9, 2006
 */
public class CalificacionesXCumpliMetasAction extends Action
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(CalificacionesXCumpliMetasAction.class);
	
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

			if (form instanceof CalificacionesXCumpliMetasForm)
			{
				con=UtilidadBD.abrirConexion();

				UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				CalificacionesXCumpliMetasForm forma=(CalificacionesXCumpliMetasForm)form;
				String estado=forma.getEstado();

				logger.warn("Estado CalificacionesXCumpliMetasAction [" + estado + "]");

				if(estado == null)
				{
					forma.reset();	
					logger.warn("Estado no valido dentro del flujo de Calificaciones por Cumplimiento Metas (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				//---Estado que muestra la página de inicio para seleccionar el tipo de régimen------//
				else if(estado.equals("empezar"))
				{
					return accionEmpezar(con, mapping, forma);
				}
				//---Se consultan las calificaciones ingresadas para el tipo de régimen
				else if (estado.equals("consultarCalificacionesXRegimen"))
				{
					return accionConsultarCalificacionesXRegimen (con, mapping, forma, usuario.getCodigoInstitucionInt());
				}
				//----Se guardan las calificaciones por cumplimiento de metas agregadas ----//
				else if (estado.equals("guardar"))
				{
					forma.setPatronOrdenar("");
					forma.setUltimoPatron("");
					return this.accionGuardar(con, mapping, forma, usuario);
				}
				//---Se agrega una nueva calificación al mapa ----------//
				else if (estado.equals("agregarNuevaCalificacion"))
				{
					return accionAgregarCalificacion(con, forma, request, response);
				}
				//----Se elimina una calificación del mapa o de la base de datos --------//
				else if (estado.equals("eliminarCalificacion"))
				{
					return accionEliminar (con, mapping, forma, usuario);
				}
				//-Ordenar el listado de calificaciones
				else if (estado.equals("ordenar"))   
				{			    
					return accionOrdenar(forma, mapping, con);
				}
				else if (estado.equals("redireccion"))// estado para mantener los datos del pager
				{			    
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

			}//if
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;	
}

	/**
	 * Método que resetea el form para iniciar la consulta, modificación y eliminación
	 * de las calificaciones por cumplimiento de metas
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, CalificacionesXCumpliMetasForm forma)
	{
		//----Limpiamos lo que venga del form
		forma.reset();
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");		
	}
	
	/**
	 * Método que consulta las calificaciones por cumplimiento de metas ingresadas para el 
	 * régimen seleccionado y la institución actual
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param codigoInstitucion
	 * @return
	 */
	private ActionForward accionConsultarCalificacionesXRegimen(Connection con, ActionMapping mapping, CalificacionesXCumpliMetasForm forma, int codigoInstitucion)
	{
		CalificacionesXCumpliMetas mundoCalificaciones=new CalificacionesXCumpliMetas();
		
		//Se obtiene el número de registros por página que se tiene parametrizado
		String numItems=ValoresPorDefecto.getMaxPageItems(codigoInstitucion);
		if(numItems==null || numItems.trim().equals(""))
		{
			numItems="20";
		}
		forma.setMaxPageItems( Integer.parseInt(numItems));		
		
		//---------Se consultan las calificaciones ingresadas para el tipo de régimen institución -------//
		forma.setMapaCumpliMetas(mundoCalificaciones.consultarCalificacionesXRegimen(con, forma.getTipoRegimen(), codigoInstitucion));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Método que agrega un nuevo registro de calificación por cumplimiento de metas
	 * al mapa
	 * @param con
	 * @param forma
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionAgregarCalificacion(Connection con, CalificacionesXCumpliMetasForm forma, HttpServletRequest request, HttpServletResponse response)
	{
		int numRegistros=0;
		
		if(UtilidadCadena.noEsVacio(forma.getMapaCumpliMetas("numRegistros")+""))
		{
			numRegistros=Integer.parseInt(forma.getMapaCumpliMetas("numRegistros")+"");
		}
		
		//------------Se agrega al mapa el nuevo registro ------------------------//
		forma.setMapaCumpliMetas("codigo_"+numRegistros, "");
		forma.setMapaCumpliMetas("meta_"+numRegistros, "");
		forma.setMapaCumpliMetas("rango_inicial_"+numRegistros, "");
		forma.setMapaCumpliMetas("rango_final_"+numRegistros, "");
		forma.setMapaCumpliMetas("tipo_calificacion_"+numRegistros, "-1");
		forma.setMapaCumpliMetas("activo_"+numRegistros, ValoresPorDefecto.getValorTrueParaConsultas());
		forma.setMapaCumpliMetas("esta_grabado_"+numRegistros, "0");
		forma.setPatronOrdenar("");
		forma.setUltimoPatron("");
		
		numRegistros++;
		
		forma.setMapaCumpliMetas("numRegistros", numRegistros+"");
		forma.setNumRegistros(numRegistros);
		
		return this.redireccion(con,forma,response,request,"calificacionXCumpliMetas.jsp");  		
	}
	
	/**
	 * Método que elimina el registro seleccionado del mapa, en el caso que ya 
	 * esté grabado se borra de la base de datos sino simplemente del mapa 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminar(Connection con, ActionMapping mapping, CalificacionesXCumpliMetasForm forma, UsuarioBasico usuario)
	{
		int indiceEliminado = forma.getIndiceRegEliminar();

		/*Se verifica si el registro está grabado para borrarlo de la base de datos, 
		sino se borra el registro del mapa*/ 
		if (Integer.parseInt(forma.getMapaCumpliMetas("esta_grabado_"+indiceEliminado)+"")==1)
		{
			CalificacionesXCumpliMetas mundo = new CalificacionesXCumpliMetas();
			int codigoCalificacion = Integer.parseInt(forma.getMapaCumpliMetas("codigo_"+indiceEliminado)+"");
			
			//----------Se elimina la calificación por cumplimiento ---------//
			try
			{
				if (mundo.eliminarCalificacionXCumplimiento (con, codigoCalificacion))
				{
				//-----------------------------------------------GENERACION DEL LOG AL ELIMINAR UNIDAD DE MEDIDA --------------------------------------------------//
				StringBuffer log=new StringBuffer();
				log.append("\n========ELIMINACIÓN DE CALIFICACIONES POR CUMPLIMIENTO DE METAS==========");
				
				String meta=forma.getMapaCumpliMetas("meta_"+indiceEliminado)+"";					
				String rangoInicial=forma.getMapaCumpliMetas("rango_inicial_"+indiceEliminado)+"";	
				String rangoFinal=forma.getMapaCumpliMetas("rango_final_"+indiceEliminado)+"";	
				String tipoCalificacion=forma.getMapaCumpliMetas("tipo_calificacion_"+indiceEliminado)+"";	
				String activo=forma.getMapaCumpliMetas("activo_"+indiceEliminado)+"";
				
				log.append("\n NOMBRE DEL RÉGIMEN :"+forma.getNombreTipoRegimen());
				log.append("\n META :"+meta);
				log.append("\n RANGO INICIAL :"+rangoInicial);
				log.append("\n RANGO FINAL :"+rangoFinal);
				log.append("\n TIPO CALIFICACIÓN :"+Utilidades.obtenerNombreTipoCalificacionPyP(con, Integer.parseInt(tipoCalificacion)));
				
				if(UtilidadTexto.getBoolean(activo))
				{
					log.append("\n ACTIVO : [SI]");
				}
				else
				{
					log.append("\n ACTIVO : [NO]");
				}
				
				log.append("\n======================================================================");
				   //-Generar el log 
				LogsAxioma.enviarLog(ConstantesBD.logCalificacionXCumpliMetasCodigo, log.toString(), ConstantesBD.tipoRegistroLogEliminacion, usuario.getLoginUsuario());
				
				//-------Se elimina el registro del mapa -----------//
				forma.eliminarRegistroMapa ();
				}
			}
		catch (SQLException e)
			{
			logger.warn("Error en la eliminación de la calificación (CalificacionesXCumpliMetasAction)" +e.toString());
			}
		}
		//----------Cuando el registro no ha sido guardado se borra del mapa ----------------//
		else
			{
				forma.eliminarRegistroMapa ();
			}
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
	}
	
	/**
	 * Método que guarda o actualiza las calificaciones de cumplimieto por metas
	 * registradas en un tipo de régimen y una institución
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, ActionMapping mapping, CalificacionesXCumpliMetasForm forma, UsuarioBasico usuario)
	{
		CalificacionesXCumpliMetas mundo = new CalificacionesXCumpliMetas();
		
		//Utilidades.imprimirMapa(forma.getMapaCumpliMetas());
		
		//-----------Se pasa el mapa del form al mundo -----------//
		mundo.setMapaCumpliMetas(forma.getMapaCumpliMetas());
		
		//----------Se pasa el valor del tipo régimen al mundo ----------//
		mundo.setTipoRegimen(forma.getTipoRegimen());
		mundo.setNombreTipoRegimen(forma.getNombreTipoRegimen());
		
		//-----------Se insertan los registros agregados al mapa --------------------//
		try
		{
			mundo.insertarModificarCalificacionsXCumplimiento (con, usuario);
		} 
		catch (SQLException e)
		{
			logger.warn("Error en la inserción/modificación de la calificación (CalificacionesXCumpliMetasAction) " +e.toString());
		}
		
		return accionConsultarCalificacionesXRegimen(con, mapping, forma, usuario.getCodigoInstitucionInt());
	}
	
	/**
	 * Método que ordena el mapa que el listado de calificaciones por
	 * cumplimiento de metas
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionOrdenar(CalificacionesXCumpliMetasForm forma, ActionMapping mapping, Connection con)
	{
		String[] indices = {
				"codigo_",
				"meta_",
				"metaant_",
				"rango_inicial_",
				"rango_inicialant_",
				"rango_final_",
				"rango_finalant_",
				"tipo_calificacion_",
				"tipo_calificacionant_",
				"activo_",
				"activoant_",
				"esta_grabado_",

		};

		int num = Integer.parseInt(forma.getMapaCumpliMetas("numRegistros")+"");
		
		forma.setMapaCumpliMetas(Listado.ordenarMapa(indices,
									forma.getPatronOrdenar(),
									forma.getUltimoPatron(),
									forma.getMapaCumpliMetas(),
									num ));
        
        forma.getMapaCumpliMetas().put("numRegistros", num+"");
        forma.setUltimoPatron(forma.getPatronOrdenar());
        UtilidadBD.closeConnection(con);
        return mapping.findForward("principal");
	}
	
	/**
     * Metodo implementado para posicionarse en la ultima
     * pagina del pager.
     * @param con, Connection con la fuente de datos.
     * @param forma CalificacionesXCumpliMetasForm
     * @param response HttpServletResponse
     * @param request HttpServletRequest
     * @param String enlace
     * @return null
     */
    public ActionForward redireccion (Connection con,
            														CalificacionesXCumpliMetasForm forma,
            														HttpServletResponse response,
            														HttpServletRequest request, String enlace)
    {
    	forma.setOffset(((int)((forma.getNumRegistros()-1)/forma.getMaxPageItems()))*forma.getMaxPageItems());
        if(request.getParameter("ultimaPage")==null)
        {
           if(forma.getNumRegistros() > (forma.getOffset()+forma.getMaxPageItems()))
               forma.setOffset(((int)(forma.getNumRegistros()/forma.getMaxPageItems()))*forma.getMaxPageItems());
            try 
            {
                response.sendRedirect(enlace+"?pager.offset="+forma.getOffset());
            } catch (IOException e) 
            {
                
                e.printStackTrace();
            }
        }
        else
        {    
            String ultimaPagina=request.getParameter("ultimaPage");
            String tempOffset="offset=";
            int posOffSet=ultimaPagina.indexOf(tempOffset)+tempOffset.length();
            if(forma.getNumRegistros()>(forma.getOffset()+forma.getMaxPageItems()))
                forma.setOffset(forma.getOffset()+forma.getMaxPageItems());
            try 
            {
                response.sendRedirect(ultimaPagina.substring(0,posOffSet)+forma.getOffset());
            } 
            catch (IOException e) 
            {
                
                e.printStackTrace();
            }
        }
       try {
		UtilidadBD.cerrarConexion(con);
		} catch (SQLException e) {
			// @todo Auto-generated catch block
			e.printStackTrace();
		}
       return null;
    }

}
