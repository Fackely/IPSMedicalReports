package com.princetonsa.action.historiaClinica;

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
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.historiaClinica.TiposTratamientosOdontologicosForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.TiposTratamientosOdontologicos;

public class TiposTratamientosOdontologicosAction extends Action
{
	Logger logger = Logger.getLogger(TiposTratamientosOdontologicosAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{
		Connection con = null;
		try {
			if(response==null);
			if(form instanceof TiposTratamientosOdontologicosForm)
			{
				con = UtilidadBD.abrirConexion();

				if(con == null)
				{	
					request.setAttribute("CodigoDescripcionError","errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				TiposTratamientosOdontologicosForm forma = (TiposTratamientosOdontologicosForm)form;
				String estado = forma.getEstado();

				ActionErrors errores=new ActionErrors();

				logger.info("\n\n ESTADO TIPOS TRATAMIENTOS ODONTOLOGICOS---->"+estado+"\n\n");

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				if(estado == null)
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(forma, con, mapping, usuario);   			
				}			
				else if(estado.equals("eliminarRegistro"))
				{
					return this.accionEliminarTipoT(con,mapping,forma,usuario);
				}
				else if(estado.equals("modificarRegistro"))
				{
					return this.accionModificarTipoT(con,mapping,forma,usuario);
				}
				else if(estado.equals("guardarModificacion"))
				{
					return this.accionGuardarModificacion(con,mapping,forma,usuario);
				}
				else if(estado.equals("insertar"))
				{
					return this.accionInsertar(con,mapping,forma,usuario);
				}
				else if(estado.equals("guardarNuevo"))
				{
					return this.accionGuardarNuevo(con,mapping,forma,usuario);
				}
				else
					/*------------------------------
					 * 		ESTADO ==> ORDENAR
				-------------------------------*/
					if (estado.equals("ordenar"))
					{
						accionOrdenarMapa(forma);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("TiposTratamientosOdontologicos");
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
     * @param forma
     */    
    private void accionOrdenarMapa(TiposTratamientosOdontologicosForm forma) {
    	
    	String[] indices = (String[])forma.getTiposTMap("INDICES");
		int numReg = Integer.parseInt(forma.getTiposTMap("numRegistros")+"");
		forma.setTiposTMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getTiposTMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setTiposTMap("numRegistros",numReg+"");
		forma.setTiposTMap("INDICES",indices);
    }
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezar(TiposTratamientosOdontologicosForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		forma.setTiposTMap(TiposTratamientosOdontologicos.consultaTiposT(con, usuario.getCodigoInstitucionInt()));
			
		UtilidadBD.closeConnection(con);
		return mapping.findForward("TiposTratamientosOdontologicos");
		
	}
	
	/**
	 * Metodo utilizado para la eliminacion de tipos de tratamientos
	 * @param con
	 * @param mapping
	 * @param sustitutosNoPosForm
	 * @return
	 */
	private ActionForward accionEliminarTipoT(Connection con, ActionMapping mapping, TiposTratamientosOdontologicosForm forma, UsuarioBasico usuario){
		String [] indicesMap={"codigo_","nombre_","activo_"};
		
		if(!forma.getIndexMap().equals(""))
    	{
			int pos = Integer.parseInt(forma.getIndexMap());			
						
			if(TiposTratamientosOdontologicos.eliminar(con, forma.getTiposTMap("codigo_"+forma.getIndexMap()).toString()))
        	{
    			Utilidades.generarLogGenerico(forma.getTiposTMap(), null, usuario.getLoginUsuario(), true,pos,ConstantesBD.logTiposTratamientosOdontologicosCodigo, indicesMap);
        	}
        	
			forma.setTiposTMap(TiposTratamientosOdontologicos.consultaTiposT(con, usuario.getCodigoInstitucionInt()));
    	 	UtilidadBD.closeConnection(con);
        	return mapping.findForward("TiposTratamientosOdontologicos");
    	}
		forma.setTiposTMap(TiposTratamientosOdontologicos.consultaTiposT(con, usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("TiposTratamientosOdontologicos");
	}
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionModificarTipoT(Connection con, ActionMapping mapping, TiposTratamientosOdontologicosForm forma, UsuarioBasico usuario) {
		if(!forma.getIndexMap().equals(""))
    	{
    		for(int w=0; w<Integer.parseInt(forma.getTiposTMap().get("numRegistros").toString());w++)
    		{
    			if(Integer.parseInt(forma.getIndexMap().toString()) == w)
    			{
    				forma.setCodigo(forma.getTiposTMap().get("codigo_"+w).toString());
    				forma.setDescripcion(forma.getTiposTMap().get("nombre_"+w).toString());
    				
    				if(UtilidadTexto.getBoolean(forma.getTiposTMap().get("activo_"+w)))
    					forma.setActivo("1");
    				else
    					forma.setActivo("0");
    				
    				UtilidadBD.closeConnection(con);
    		    	return mapping.findForward("TiposTratamientosOdontologicos");
    			}
    		}
    	}
		UtilidadBD.closeConnection(con);
    	return mapping.findForward("TiposTratamientosOdontologicos");
	}
	
	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @return
     */
	private ActionForward accionGuardarModificacion(Connection con, ActionMapping mapping, TiposTratamientosOdontologicosForm forma, UsuarioBasico usuario){
		String [] indicesMap={"codigo_","nombre_","activo_"};
		int pos = Integer.parseInt(forma.getIndexMap().toString());
		TiposTratamientosOdontologicos mundo = new TiposTratamientosOdontologicos();
		
		HashMap<String, Object> mapaM = new HashMap<String, Object> ();
		mapaM.put("codigo_"+forma.getIndexMap(), forma.getCodigo());
		mapaM.put("nombre_"+forma.getIndexMap(), forma.getDescripcion());
		mapaM.put("activo_"+forma.getIndexMap(), forma.getActivo());
		mapaM.put("INDICES",indicesMap);
		
		HashMap<String, Object> mapaN = new HashMap<String, Object> ();
		mapaN.put("codigo_0", forma.getTiposTMap("codigo_"+forma.getIndexMap()));
		mapaN.put("nombre_0", forma.getTiposTMap("nombre_"+forma.getIndexMap()));
		mapaN.put("activo_0", forma.getTiposTMap("activo_"+forma.getIndexMap()));
		mapaN.put("INDICES",indicesMap);
		
		llenarmundo(forma, mundo, usuario, "modificar");
		mundo.modificar(con, mundo);
		Utilidades.generarLogGenerico(mapaM, mapaN, usuario.getLoginUsuario(), false,pos,ConstantesBD.logTiposTratamientosOdontologicosCodigo, indicesMap);
		forma.setTiposTMap(TiposTratamientosOdontologicos.consultaTiposT(con, usuario.getCodigoInstitucionInt()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("TiposTratamientosOdontologicos");
	}
	
	public void llenarmundo(TiposTratamientosOdontologicosForm forma, TiposTratamientosOdontologicos mundo, UsuarioBasico usuario, String estadol)
	{
		mundo.setCodigo(forma.getCodigo());
		mundo.setDescripcion(forma.getDescripcion());
		mundo.setActivo(forma.getActivo());
		mundo.setInstitucion(usuario.getCodigoInstitucion());
		if(estadol.equals("modificar"))
			mundo.setCodigoAux(forma.getTiposTMap().get("codigo_"+forma.getIndexMap()).toString());
	}
	
	/**
	 * 
	 */
	private ActionForward accionInsertar(Connection con, ActionMapping mapping, TiposTratamientosOdontologicosForm forma, UsuarioBasico usuario){
		TiposTratamientosOdontologicos mundo = new TiposTratamientosOdontologicos();
		llenarmundo(forma, mundo, usuario, "insertar");
		TiposTratamientosOdontologicos.insertar(con, mundo);
		forma.setTiposTMap(TiposTratamientosOdontologicos.consultaTiposT(con, usuario.getCodigoInstitucionInt()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("TiposTratamientosOdontologicos");
	}
	
	/**
	 * 
	 */
	private ActionForward accionGuardarNuevo(Connection con, ActionMapping mapping, TiposTratamientosOdontologicosForm forma, UsuarioBasico usuario){
		forma.setCodigo("");
		forma.setDescripcion("");
		forma.setTiposTMap(TiposTratamientosOdontologicos.consultaTiposT(con, usuario.getCodigoInstitucionInt()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("TiposTratamientosOdontologicos");
	}
}