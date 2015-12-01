package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.util.HashMap;

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
import util.UtilidadBD;
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.inventarios.AlmacenParametrosForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.AlmacenParametros;

public class AlmacenParametrosAction extends Action {
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(AlmacenParametrosAction.class);
	
	
	/**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{

    	Connection con = null;
    	try{

    		if(response==null);

    		if(form instanceof AlmacenParametrosForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}	


    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

    			AlmacenParametrosForm forma = (AlmacenParametrosForm)form;
    			String estado = forma.getEstado();
    			logger.warn("[AlmacenParametrosAction]===>Estado: "+estado);

    			if(estado == null)
    			{
    				forma.reset();
    				logger.warn("Estado no valido dentro del Flujo de Unidad de Procedimiento (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}

    			else if(estado.equals("empezar"))
    			{    			
    				return this.accionEmpezar(forma, con, mapping, usuario.getCodigoInstitucionInt());    			
    			}
    			else if(estado.equals("detalle"))
    			{
    				this.accionDetalle(forma, con, mapping, usuario.getCodigoInstitucionInt());
    				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getAlmacenParametrosMap("numRegistros").toString()), response, request, "almacenParametros.jsp",false);
    			}
    			else if(estado.equals("guardar"))
    			{
    				return this.accionGuardar(forma, con, usuario, mapping);
    			}   		
    			else if(estado.equals("ordenar"))
    			{
    				this.accionOrdenarMapa(forma);
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("principal");
    			}
    			else if(estado.equals("redireccion"))
    			{
    				UtilidadBD.closeConnection(con);
    				response.sendRedirect(forma.getLinkSiguiente());
    				return null;    			
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
     * Primera accion del action 
     * @param AlmacenParametrosForm forma
     * @param Connection con
     * @param ActionMapping mapping
     * @param institucion
     * */
    public ActionForward accionEmpezar(AlmacenParametrosForm forma, Connection con, ActionMapping mapping,int institucion)
    {
    	forma.reset();
    	forma.inicializarCentroAtencion(institucion);
    	forma.setEstado("empezar");
    	AlmacenParametros.iniciarTableAlmacenParametros(con, institucion,2);
    	UtilidadBD.closeConnection(con);
    	return mapping.findForward("principal");    	
    }
    
    /**
     * Carga el listado de Centros de Costos (SubAlmacen)
     * @param AlmacenParametrosForm forma
     * @param Connection con
     * @param ActionMapping mapping
     * @param institucion 
     * **/
    public void accionDetalle(AlmacenParametrosForm forma, Connection con, ActionMapping mapping, int institucion)
    {
    	forma.setAlmacenParametrosMap(AlmacenParametros.consultarAlmacenParametros(con,ConstantesBD.codigoNuncaValido, Integer.parseInt(forma.getCentroAtencion()), institucion));
    	
    	for(int i=0; i<Integer.parseInt(forma.getAlmacenParametrosMap("numRegistros")+""); i++ ){
    		if(forma.getAlmacenParametrosMap("afectacostoprom_"+i).toString().isEmpty()){
    			forma.getAlmacenParametrosMap().remove("afectacostoprom_"+i);
    			forma.getAlmacenParametrosMap().put("afectacostoprom_"+i, ConstantesBD.acronimoSi);
    		}
    	}
    	
    	/*
    	 * Se cargan los tipos de área:
    	 * Directo - Indirecto - SubAlmacén
    	 * Por solicitud de tarea: 43590
    	 */
    	String tipoArea = (
    			ConstantesBD.codigoTipoAreaDirecto)+
    			ConstantesBD.separadorSplit+
    			(ConstantesBD.codigoTipoAreaIndirecto)+
    			ConstantesBD.separadorSplit+
    			(ConstantesBD.codigoTipoAreaSubalmacen);
    	forma.setCentroCostoPrincipal(Utilidades.obtenerCentrosCosto(con, institucion, tipoArea, true, Utilidades.convertirAEntero(forma.getCentroAtencion()),false));
    	logger.info("===> Centros de Costo Todos: "+forma.getCentroCostoPrincipal("numRegistros"));
    	forma.setEstado("detalle");
    	UtilidadBD.closeConnection(con);   	
    }
    
    /**
	 * 
	 * @param forma
	 */
	private void accionOrdenarMapa(AlmacenParametrosForm forma) 
	{
		String[] indices= (String[])forma.getAlmacenParametrosMap("INDICES_MAPA");
		int numReg=Integer.parseInt(forma.getAlmacenParametrosMap("numRegistros")+"");
		forma.setAlmacenParametrosMap(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getAlmacenParametrosMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setAlmacenParametrosMap("numRegistros",numReg+"");	
		forma.setAlmacenParametrosMap("INDICES_MAPA", indices);
	}	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(AlmacenParametrosForm forma, Connection con, UsuarioBasico usuario, ActionMapping mapping) 
	{
		boolean transacction=UtilidadBD.iniciarTransaccion(con);
		forma.setEstado("ninguno");
		AlmacenParametros mundo= new AlmacenParametros();
		String [] indices = (String[])forma.getAlmacenParametrosMap("INDICES_MAPA");				
		
		for(int i=0;i<Integer.parseInt(forma.getAlmacenParametrosMap("numRegistros")+"");i++)
		{
			//modificar
			if(this.existeModificacion(con,forma,mundo, Integer.parseInt(forma.getAlmacenParametrosMap("codigo_"+i)+""),i,usuario))
			{
				forma.setEstado("operacionTrue");
				transacction=AlmacenParametros.modificarAlmacenParametros(con,llenarCrearMundo(forma, i, usuario.getCodigoInstitucionInt()));				
			}			
		}
		
		if(transacction)
		{			
			UtilidadBD.finalizarTransaccion(con);
			logger.info("--->INSERTO 100% ALMACEN PARAMETROS");
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			
		}
		return this.accionResumen(con, mapping, forma, usuario);
	}
	
	
	/**
	 * Verfica la modificacion de registros
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario 
	 * @return boolean
	 */
	private boolean existeModificacion(Connection con, AlmacenParametrosForm forma, AlmacenParametros mundo,int codigo,int pos, UsuarioBasico usuario)
	{		
		HashMap temp=AlmacenParametros.consultarAlmacenParametros(con,codigo,ConstantesBD.codigoNuncaValido,usuario.getCodigoInstitucionInt());
		String[] indices= (String[])forma.getAlmacenParametrosMap("INDICES_MAPA");
		for(int i=0;i<indices.length;i++)
		{
			if(temp.containsKey(indices[i]+"0")&&forma.getAlmacenParametrosMap().containsKey(indices[i]+""+pos))
			{
				if(!((temp.get(indices[i]+"0")+"").trim().equals(forma.getAlmacenParametrosMap(indices[i]+""+pos)+"")))
				{
					Utilidades.generarLogGenerico(forma.getAlmacenParametrosMap(),temp,usuario.getNombreUsuario(),false,pos,ConstantesBD.logAlmacenParametrosCodigo,indices);					
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Llenar Crear Mundo 
	 * @param forma
	 * @param mundo
	 * @param indice
	 */
	private AlmacenParametros llenarCrearMundo(AlmacenParametrosForm forma, int indice, int institucion)
	{
		AlmacenParametros mundo= new AlmacenParametros();
		mundo.setExist_negativa(forma.getAlmacenParametrosMap("existencianegativa_"+indice).toString());
		mundo.setTipo_consignac(forma.getAlmacenParametrosMap("tipoconsignacion_"+indice).toString());
		mundo.setCodigo_interfaz(forma.getAlmacenParametrosMap("codigointerfaz_"+indice).toString());
		mundo.setPlan_especial(forma.getAlmacenParametrosMap("planespecial_"+indice).toString());
		mundo.setCentro_costo_principal(forma.getAlmacenParametrosMap("centrocostoprincipal_"+indice).toString());
		mundo.setAfectaCostoPromedio(forma.getAlmacenParametrosMap("afectacostoprom_"+indice).toString());
		
		mundo.setCodigo(Integer.parseInt(forma.getAlmacenParametrosMap("codigo_"+indice).toString()));
		mundo.setCentroAtencion(Integer.parseInt(forma.getAlmacenParametrosMap("centroatencion_"+indice).toString()));
		mundo.setInstitucion(institucion);		
		return mundo;
	}
	
	/**
	 * Muestra el listado despues de la modificacion
	 * @param Connection con
	 * @paramActionMapping mapping
	 * @paramAlmacenParametrosForm forma
	 * @paramUsuarioBasico usuario
	 * @return ActionForward
	 * */
	private ActionForward accionResumen(Connection con, ActionMapping mapping, AlmacenParametrosForm forma, UsuarioBasico usuario)
	{
		String centroAtencionActual = forma.getCentroAtencion();
		forma.reset();		
		forma.setAlmacenParametrosMap(AlmacenParametros.consultarAlmacenParametros(con,ConstantesBD.codigoNuncaValido, Integer.parseInt(centroAtencionActual), usuario.getCodigoInstitucionInt()));

		forma.inicializarCentroAtencion(usuario.getCodigoInstitucionInt());
    	forma.setCentroAtencion(centroAtencionActual);
    	logger.info("\n====>Centro de Atencion: "+centroAtencionActual);
    	
    	String tipoArea = (
    			ConstantesBD.codigoTipoAreaDirecto)+
    			ConstantesBD.separadorSplit+
    			(ConstantesBD.codigoTipoAreaIndirecto)+
    			ConstantesBD.separadorSplit+
    			(ConstantesBD.codigoTipoAreaSubalmacen);
    	
    	forma.setCentroCostoPrincipal(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), tipoArea, true, Utilidades.convertirAEntero(centroAtencionActual),false));
    	logger.info("\n====>Centros de Costo Principal: "+forma.getCentroCostoPrincipal());
    	UtilidadBD.closeConnection(con);    	
		return mapping.findForward("principal");	
	}	
}