package com.princetonsa.action.inventarios;

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

import com.princetonsa.actionform.inventarios.SustitutosNoPosForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.SustitutosNoPos;


public class SustitutosNoPosAction extends Action
{
	Logger logger = Logger.getLogger(SustitutosNoPosAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{
		Connection con = null;
		try{
			if(response==null);
			if(form instanceof SustitutosNoPosForm)
			{

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{	
					request.setAttribute("CodigoDescripcionError","errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				SustitutosNoPosForm forma = (SustitutosNoPosForm)form;
				String estado = forma.getEstado();

				ActionErrors errores=new ActionErrors();

				forma.setMensaje(new ResultadoBoolean(false));

				logger.info("\n\n ESTADO SUSTITUTOS NO POS---->"+estado+"\n\n");

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				if(estado == null)
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					//logger.warn("Estado no valido dentro del Flujo de Unidad de Procedimiento (null)");
					request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(forma, con, mapping, usuario);

				}
				else if(estado.equals("cargarSus"))
				{
					return this.accionConsultarSus(con, mapping, forma);

				}
				else if(estado.equals("guardarNuevo"))
				{
					return this.accionguardarNuevo(forma, con, mapping, usuario);
				}
				else if(estado.equals("cargarNueSus"))
				{
					return this.accionValidarSus(request, errores, con,mapping, forma);
				}
				else if(estado.equals("eliminarRegistro"))
				{
					return this.accionEliminarSus(con,mapping,forma,usuario);
				}
				else if(estado.equals("modificarRegistro"))
				{
					return this.accionModificarSus(con,mapping,forma,usuario);
				}
				else if(estado.equals("guardarModificacion"))
				{
					return this.accionGuardarModificacion(con,mapping,forma,usuario);
				}
				else if(estado.equals("insertar"))
				{
					return this.accionInsertar(con,mapping,forma,usuario);
				}
				else
					/*------------------------------
					 * 		ESTADO ==> ORDENAR
				-------------------------------*/
					if (estado.equals("ordenar"))
					{
						accionOrdenarMapa(forma);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("SustitutosNoPos");
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
     */    
    private void accionOrdenarMapa(SustitutosNoPosForm forma) {
    	
    	String[] indices = (String[])forma.getResultadosMap("INDICES");
		int numReg = Integer.parseInt(forma.getResultadosMap("numRegistros")+"");
		forma.setResultadosMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getResultadosMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setResultadosMap("numRegistros",numReg+"");
		forma.setResultadosMap("INDICES",indices);
    }
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezar(SustitutosNoPosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
			
		UtilidadBD.closeConnection(con);
		return mapping.findForward("SustitutosNoPos");
		
	}
	
	/**
	 * Metodo utilizado para la consulta de articulos sustitutos
	 * @param con
	 * @param mapping
	 * @param sustitutosNoPosForm
	 * @return
	 */
	private ActionForward accionConsultarSus(Connection con, ActionMapping mapping, SustitutosNoPosForm forma) {
		forma.setResultadosMap(SustitutosNoPos.consultaSus(con,Integer.parseInt(forma.getInformacionArtPrin("codigoArticulo").toString())));
		forma.setResultadosCGMap(SustitutosNoPos.consultaCG(con,Integer.parseInt(forma.getInformacionArtPrin("codigoArticulo").toString())));
		
		logger.info("valor del mapa >> "+forma.getResultadosCGMap());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("SustitutosNoPos");
	}
	
	/**
	 * Metodo utilizado para la validacion de articulos sustitutos
	 * @param con
	 * @param mapping
	 * @param sustitutosNoPosForm
	 * @return
	 */
	private ActionForward accionValidarSus(HttpServletRequest request,ActionErrors errores,Connection con, ActionMapping mapping, SustitutosNoPosForm forma) {
		
		if(!SustitutosNoPos.validarSus(forma.getResultadosMap(),forma.getInformacionArtPrin("codigoArticuloSus").toString()))
		{
			errores.add("descripcion",new ActionMessage("error.capitacion.yaExisteCodigo"," Articulo con Codigo "+forma.getInformacionArtPrin("codigoArticuloSus").toString()));
			saveErrors(request,errores);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("SustitutosNoPos");
	}
	
	/**
	 * Metodo utilizado para la eliminacion de articulos sustitutos
	 * @param con
	 * @param mapping
	 * @param sustitutosNoPosForm
	 * @return
	 */
	private ActionForward accionEliminarSus(Connection con, ActionMapping mapping, SustitutosNoPosForm forma, UsuarioBasico usuario){
		String [] indicesMap={"articuloPpal_",
								"tiempoTratamiento_",
								"articuloSustituto_",
								"dosisDiaria_",
								"numDosisEquivalentes_",
								"nombreArtSus_",
								"dosificacion_",
								"umedida_",
								"ffarma_",
								"concentracion_"};
		
		if(!forma.getIndexMap().equals(""))
    	{
			int pos = Integer.parseInt(forma.getIndexMap());			
						
			if(SustitutosNoPos.eliminar(con, forma.getResultadosMap("articuloSustituto_"+forma.getIndexMap()).toString()))
        	{
    			Utilidades.generarLogGenerico(forma.getResultadosMap(), null, usuario.getLoginUsuario(), true,pos,ConstantesBD.logSustitutosNoPosCodigo, indicesMap);
    			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
        	}
        	
			forma.setResultadosMap(SustitutosNoPos.consultaSus(con, Integer.parseInt(forma.getInformacionArtPrin("codigoArticulo").toString())));
    	 	UtilidadBD.closeConnection(con);
        	return mapping.findForward("SustitutosNoPos");
    	}
		forma.setResultadosMap(SustitutosNoPos.consultaSus(con, Integer.parseInt(forma.getInformacionArtPrin("codigoArticulo").toString())));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("SustitutosNoPos");
	}
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionModificarSus(Connection con, ActionMapping mapping, SustitutosNoPosForm forma, UsuarioBasico usuario) {
		if(!forma.getIndexMap().equals(""))
    	{
    		for(int w=0; w<Integer.parseInt(forma.getResultadosMap().get("numRegistros").toString());w++)
    		{
    			if(Integer.parseInt(forma.getIndexMap().toString()) == w)
    			{
    				forma.setArticuloSustituto(forma.getResultadosMap().get("articuloSustituto_"+w).toString());
    				forma.setTiempoTratamiento(forma.getResultadosMap().get("tiempoTratamiento_"+w).toString());
    				forma.setNomModSus(forma.getResultadosMap().get("nombreArtSus_"+w).toString());
    				forma.setDosificacion(forma.getResultadosMap().get("dosificacion_"+w).toString());
    				forma.setDosisDiaria(forma.getResultadosMap().get("dosisDiaria_"+w).toString());
    				forma.setNumDosisEquivalentes(forma.getResultadosMap().get("numDosisEquivalentes_"+w).toString());
    				UtilidadBD.closeConnection(con);
    		    	return mapping.findForward("SustitutosNoPos");
    			}
    		}
    	}
		UtilidadBD.closeConnection(con);
    	return mapping.findForward("SustitutosNoPos");
	}
	
	/**
     * 
     * @param forma
     * @param mundo
     * @param usuario 
     */
	private void llenarMundo (SustitutosNoPosForm forma, SustitutosNoPos mundo, UsuarioBasico usuario, String operacion){
		
		mundo.setArticuloPrincipal(forma.getInformacionArtPrin("codigoArticulo").toString());
		mundo.setArticuloSustituto(forma.getArticuloSustituto());		
		mundo.setNomModSus(forma.getNomModSus());
		
		if(operacion.equals("nuevo"))
		{
			mundo.setTiempoTratamiento(forma.getInformacionArtPrin("tiempoTratamientoNuevo").toString());
			mundo.setDosificacion(forma.getInformacionArtPrin("dosificacionNuevo").toString());
			mundo.setDosisDiaria(forma.getInformacionArtPrin("dosisDiariaNuevo").toString());
			mundo.setNumDosisEquivalentes(forma.getInformacionArtPrin("numDosisEquivalentesNuevo").toString());
		}
		else
		{
			mundo.setTiempoTratamiento(forma.getTiempoTratamiento().toString());
			mundo.setDosificacion(forma.getDosificacion().toString());
			mundo.setDosisDiaria(forma.getDosisDiaria().toString());
			mundo.setNumDosisEquivalentes(forma.getNumDosisEquivalentes().toString());
		}
			
		
		mundo.setIndexMap(forma.getIndexMap());
		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
		mundo.setUsuarioModifica(usuario.getLoginUsuario());
		mundo.setResultadosMap(forma.getResultadosMap());
		mundo.setCodNueArtSus(forma.getInformacionArtPrin("codigoArticuloSus").toString());		
	}
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionGuardarModificacion(Connection con, ActionMapping mapping, SustitutosNoPosForm forma, UsuarioBasico usuario){
		String [] indicesMap={"articuloPpal_",
								"tiempoTratamiento_",
								"articuloSustituto_",
								"dosisDiaria_",
								"numDosisEquivalentes_",
								"dosificacion_",
								"ffarma_",
								"umedida_",
								"concentracion_",
								"puedoELiminar_"};
		
		int pos = Integer.parseInt(forma.getIndexMap().toString());
		forma.setMensaje(new ResultadoBoolean(false,""));
		SustitutosNoPos mundo = new SustitutosNoPos();
		
		HashMap<String, Object> mapaM = new HashMap<String, Object> ();
		mapaM.put("articuloPpal_"+forma.getIndexMap(), forma.getInformacionArtPrin("codigoArticulo"));
		mapaM.put("articuloSustituto_"+forma.getIndexMap(), forma.getArticuloSustituto());
		mapaM.put("dosificacion_"+forma.getIndexMap(), forma.getDosificacion());
		mapaM.put("dosisDiaria_"+forma.getIndexMap(), forma.getDosisDiaria());
		mapaM.put("tiempoTratamiento_"+forma.getIndexMap(), forma.getTiempoTratamiento());
		mapaM.put("numDosisEquivalentes_"+forma.getIndexMap(), forma.getNumDosisEquivalentes());
		mapaM.put("ffarma_"+forma.getIndexMap(), forma.getResultadosMap("ffarma_"+forma.getIndexMap()));
		mapaM.put("umedida_"+forma.getIndexMap(), forma.getResultadosMap("umedida_"+forma.getIndexMap()));
		mapaM.put("concentracion_"+forma.getIndexMap(), forma.getResultadosMap("concentracion_"+forma.getIndexMap()));
		mapaM.put("INDICES",indicesMap);
		
		HashMap<String, Object> mapaN = new HashMap<String, Object> ();
		mapaN.put("articuloPpal_0", forma.getInformacionArtPrin("codigoArticulo"));
		mapaN.put("articuloSustituto_0", forma.getResultadosMap("articuloSustituto_"+forma.getIndexMap()));
		mapaN.put("dosificacion_0", forma.getResultadosMap("dosificacion_"+forma.getIndexMap()));
		mapaN.put("dosisDiaria_0", forma.getResultadosMap("dosisDiaria_"+forma.getIndexMap()));
		mapaN.put("tiempoTratamiento_0", forma.getResultadosMap("tiempoTratamiento_"+forma.getIndexMap()));
		mapaN.put("numDosisEquivalentes_0", forma.getResultadosMap("numDosisEquivalentes_"+forma.getIndexMap()));
		mapaN.put("ffarma_0", forma.getResultadosMap("ffarma_"+forma.getIndexMap()));
		mapaN.put("umedida_0", forma.getResultadosMap("umedida_"+forma.getIndexMap()));
		mapaN.put("concentracion_0", forma.getResultadosMap("concentracion_"+forma.getIndexMap()));
		mapaN.put("INDICES",indicesMap);
		
		llenarMundo(forma, mundo, usuario, "modificar");
		mundo.modificar(con, mundo);
		Utilidades.generarLogGenerico(mapaM, mapaN, usuario.getLoginUsuario(), false,pos,ConstantesBD.logSustitutosNoPosCodigo, indicesMap);
		
		forma.setResultadosMap(SustitutosNoPos.consultaSus(con, Integer.parseInt(forma.getInformacionArtPrin("codigoArticulo").toString())));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("SustitutosNoPos");
	}
	
	/**
	 * 
	 */
	private ActionForward accionInsertar(Connection con, ActionMapping mapping, SustitutosNoPosForm forma, UsuarioBasico usuario){
		SustitutosNoPos mundo = new SustitutosNoPos();
		llenarMundo(forma, mundo, usuario, "nuevo");
		if(SustitutosNoPos.insertar(con, mundo))
		{
			forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		}
		forma.setResultadosMap(SustitutosNoPos.consultaSus(con, Integer.parseInt(forma.getInformacionArtPrin("codigoArticulo").toString())));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("SustitutosNoPos");
	}
	
	/**
	 * 
	 */
	private ActionForward accionguardarNuevo(SustitutosNoPosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario)
	{
		forma.setInformacionArtPrin("codigoArticuloSus","");
		forma.setInformacionArtPrin("descripcionArticuloSus","");
		forma.setInformacionArtPrin("dosificacionNuevo","");
		forma.setInformacionArtPrin("dosisDiariaNuevo","");
		forma.setInformacionArtPrin("tiempoTratamientoNuevo","");
		forma.setInformacionArtPrin("numDosisEquivalentesNuevo","");
		
		UtilidadBD.closeConnection(con);		
    	return mapping.findForward("SustitutosNoPos");
	}
}