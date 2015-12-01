/*
 * @(#)TiposConvenioAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.action.facturacion;

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
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturacion.TiposConvenioForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.TiposConvenio;


/**
 * Clase para el manejo del workflow de la funcionalidad
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 */

public class TiposConvenioAction extends Action
{
	/**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(TiposConvenioAction.class);
    
    
    private String[] indices={"codigo_", "descripcion_", "acronimotiporegimen_", "codigocuentacontable_", "estabd_", "descripcioncuentacontable_", "codigoantesmod_", "asegatec_", "codigoclasificacion_", "rubro_", "ccvalconv_","ccvalpac_","ccdespac_","cccxccap_","ccutilxing_", "ccperdxing_", "ccdebglorec_", "cccreglorec_", "ccdebgloace_", "cccregloace_"  }; 
    
    /**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con=null;
    	try{
    		if (response==null);
    		if(form instanceof TiposConvenioForm)
    		{

    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{
    				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}

    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico"); 
    			TiposConvenioForm forma =(TiposConvenioForm)form;
    			String estado=forma.getEstado();
    			logger.warn("\n\n\nEl estado en Tipos convenio es------->"+estado+"\n");

    			if(estado == null)
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de Consultorios (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else if(estado.equals("empezar")){
    				return this.accionEmpezar(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("nuevo"))
    			{
    				this.accionNuevo(forma);
    				UtilidadBD.closeConnection(con);
    				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getTiposConvenioMap("numRegistros").toString()), response, request, "tiposConvenio.jsp",true);
    			}
    			else if(estado.equals("eliminar"))
    			{
    				return this.accionEliminar(con, forma, mapping, usuario, response, request );
    			}
    			else if(estado.equals("guardar"))
    			{
    				//guardamos en BD
    				return this.accionGuardar(con,forma,mapping,usuario);
    			}
    			else if(estado.equals("resumen"))
    			{
    				return this.accionResumen(forma, mapping, con, usuario);
    			}
    			else if(estado.equals("ordenar"))
    			{
    				this.accionOrdenarMapa(forma);
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("principal");
    			}
    			else if (estado.equals("redireccion"))
    			{
    				UtilidadBD.closeConnection(con);
    				response.sendRedirect(forma.getLinkSiguiente());
    				return null;
    			}
    			else if (estado.equals("eliminarRubro"))  
    			{
    				accionEliminarRubro(mapping, forma);
    				return this.accionEmpezar(forma, mapping, con, usuario);
    			}
    			else if (estado.equals("cargarCuentasContables"))  
    			{
    				return mapping.findForward("cuentasContables");
    			}
    			else
    			{
    				forma.reset(); 
    				logger.warn("Estado no valido dentro del flujo de Consultorios -> "+estado);
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
     * MEtodo para eliminar el rubro Presupuestal de un registro de la lista. 
     * @param mapping
     * @param forma
     */
    private void accionEliminarRubro(ActionMapping mapping, TiposConvenioForm forma) 
    {
    	TiposConvenio mundo = new TiposConvenio();
    	mundo.eliminarRubro(forma.getCodigoEliminar());
	}

	/**
     * 
     * @param forma
     */
    private void accionOrdenarMapa(TiposConvenioForm forma) 
    {
    	int numReg=Integer.parseInt(forma.getTiposConvenioMap("numRegistros")+"");
		forma.setTiposConvenioMap(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getTiposConvenioMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setTiposConvenioMap("numRegistros",numReg+"");	
	}

	/**
     * @param con
     * @param
     */
    private ActionForward accionGuardar(Connection con, TiposConvenioForm forma,ActionMapping mapping,UsuarioBasico usuario)
    {
    	boolean transacction = UtilidadBD.iniciarTransaccion(con);
		TiposConvenio mundo = new TiposConvenio();
		
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getTiposConvenioEliminadosMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarTiposConvenio(con,(forma.getTiposConvenioEliminadosMap("codigoantesmod_"+i)+"").toString().trim(),usuario.getCodigoInstitucionInt()))			
			{				
				 transacction=true;						 
			}
		}
		for(int i=0;i<Integer.parseInt(forma.getTiposConvenioMap("numRegistros")+"");i++)
		{			
			//modificar			
			if((forma.getTiposConvenioMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
			{	
				if(this.existeModificacion(con,forma,mundo,forma.getTiposConvenioMap("codigoantesmod_"+i).toString(),i,usuario))
				{
					transacction = mundo.modificarTiposConvenio(con,llenarCrearMundo(forma,i,usuario), forma.getTiposConvenioMap("codigoantesmod_"+i).toString());
				}	
			}
			
			//insertar
			if((forma.getTiposConvenioMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				transacction = mundo.insertarTiposConvenio(con, llenarCrearMundo(forma,i,usuario));
			}			
		}
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(con);			
			logger.info("----->INSERTO 100% TIPOS DE CONVENIO");
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
		
		return this.accionResumen(forma, mapping, con, usuario);
		
    }
    
    /**
     * 
     * @param con
     * @param forma
     * @param mapping
     * @return
     */
    private ActionForward accionEliminar(Connection con, TiposConvenioForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request) 
    {
    	logger.info("indice-->"+forma.getIndice());
    	boolean puedoEliminar= true;
    	if(forma.getTiposConvenioMap("estabd_"+forma.getIndice()).toString().equals(ConstantesBD.acronimoSi))
    	{	
    		puedoEliminar= true; //ACA TOCA HACER UN METODO PARA EVALUAR SI SE PUEDE ELIMINAR O NO DE LA BD DE DATOS DEPENDIENDO DE QUE SE ESTE USANDO EN EL CONVENIO
		}
    	int ultimaPosMapa=(Integer.parseInt(forma.getTiposConvenioMap("numRegistros")+"")-1);
    	if(puedoEliminar)
    	{
    		int numRegMapEliminados=Integer.parseInt(forma.getTiposConvenioEliminadosMap("numRegistros")+"");
    		
    		//poner la informacion en el otro mapa.
    		for(int i=0;i<indices.length;i++)
    		{
    			//solo pasar al mapa los registros que son de BD
    			if((forma.getTiposConvenioMap("estabd_"+forma.getIndice())+"").trim().equals(ConstantesBD.acronimoSi))
    			{
    				forma.setTiposConvenioEliminadosMap(indices[i]+""+numRegMapEliminados, forma.getTiposConvenioMap(indices[i]+""+forma.getIndice()));
    			}
    		}
    		if((forma.getTiposConvenioMap("estabd_"+forma.getIndice())+"").trim().equals(ConstantesBD.acronimoSi))
    		{
    			forma.setTiposConvenioEliminadosMap("numRegistros", (numRegMapEliminados+1));
    		}
    		
    		//acomodar los registros del mapa en su nueva posicion
    		for(int i=forma.getIndice();i<ultimaPosMapa;i++)
    		{
    			for(int j=0;j<indices.length;j++)
    			{
    				forma.setTiposConvenioMap(indices[j]+""+i,forma.getTiposConvenioMap(indices[j]+""+(i+1)));
    			}
    		}
    		
    		//ahora eliminamos el ultimo registro del mapa.
    		for(int j=0;j<indices.length;j++)
    		{
    			forma.getTiposConvenioMap().remove(indices[j]+""+ultimaPosMapa);
    		}
    		
    		//ahora actualizamos el numero de registros en el mapa.
    		forma.setTiposConvenioMap("numRegistros",ultimaPosMapa);
    	}
    	UtilidadBD.closeConnection(con);
    	return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getTiposConvenioMap("numRegistros").toString()), response, request, "tiposConvenio.jsp",forma.getIndice()==ultimaPosMapa);
	}

	/**
     * 
     * @param forma
     */
    private void accionNuevo(TiposConvenioForm forma) 
    {
    	int pos=Integer.parseInt(forma.getTiposConvenioMap("numRegistros")+"");
		forma.setTiposConvenioMap("codigo_"+pos,"");
		forma.setTiposConvenioMap("descripcion_"+pos,"");
		forma.setTiposConvenioMap("acronimotiporegimen_"+pos,"");
		forma.setTiposConvenioMap("codigocuentacontable_"+pos,"");
		forma.setTiposConvenioMap("estabd_"+pos, ConstantesBD.acronimoNo);
		forma.setTiposConvenioMap("descripcioncuentacontable_"+pos, "");
		forma.setTiposConvenioMap("codigoantesmod_"+pos,"");
		forma.setTiposConvenioMap("codigoclasificacion_"+pos, "");
		//forma.setTiposConvenioMap("asegatec_"+pos, "");
		forma.setTiposConvenioMap("asegatec_"+pos, ConstantesBD.acronimoNo);
		
		forma.setTiposConvenioMap("rubro_"+pos, "");
		forma.setTiposConvenioMap("numRegistros", (pos+1)+"");
	}

	/**
     * 
     * @param forma
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
	private ActionForward accionEmpezar(TiposConvenioForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset();
		TiposConvenio mundo= new TiposConvenio();
		forma.setTiposConvenioMap(mundo.consultarTiposConvenio(con, usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Unicializa un objeto Cobertura con los datos.
	 * @param forma
	 * @param indice
	 * */
	private TiposConvenio llenarCrearMundo(TiposConvenioForm forma, int indice, UsuarioBasico usuario)
	{
		TiposConvenio mundo = new TiposConvenio();		
		mundo.setCodigo(forma.getTiposConvenioMap().get("codigo_"+indice).toString());
		mundo.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		mundo.setDescripcion(forma.getTiposConvenioMap().get("descripcion_"+indice).toString());
		mundo.setAcronimoTipoRegimen(forma.getTiposConvenioMap().get("acronimotiporegimen_"+indice).toString());
		if(UtilidadTexto.isEmpty(forma.getTiposConvenioMap().get("codigocuentacontable_"+indice)+""))
		{
			mundo.setCodigoCuentaContable(-1);
		}
		else
		{
			mundo.setCodigoCuentaContable(Integer.parseInt(forma.getTiposConvenioMap().get("codigocuentacontable_"+indice).toString()));
		}	
		mundo.setUsuarioModifica(usuario.getLoginUsuario());
		mundo.setCodigoClasificacion(Integer.parseInt(forma.getTiposConvenioMap("codigoclasificacion_"+indice).toString()));
		mundo.setAseguradoraAtEv(forma.getTiposConvenioMap("asegatec_"+indice).toString());
		
		if(UtilidadTexto.isEmpty(forma.getTiposConvenioMap().get("rubro_"+indice)+""))
		{
			mundo.setRubroPresupuestal(ConstantesBD.codigoNuncaValidoDoubleNegativo);
		}
		else
		{
			mundo.setRubroPresupuestal(Utilidades.convertirADouble(forma.getTiposConvenioMap().get("rubro_"+indice).toString()));
		}
		
		if(UtilidadTexto.isEmpty(forma.getTiposConvenioMap().get("ccvalconv_"+indice)+""))
			mundo.setCuentaValConvenio(ConstantesBD.codigoNuncaValido);
		else
			mundo.setCuentaValConvenio(Utilidades.convertirAEntero(forma.getTiposConvenioMap().get("ccvalconv_"+indice).toString()));
		
		if(UtilidadTexto.isEmpty(forma.getTiposConvenioMap().get("ccvalpac_"+indice)+""))
			mundo.setCuentaValPaciente(ConstantesBD.codigoNuncaValido);
		else
			mundo.setCuentaValPaciente(Utilidades.convertirAEntero(forma.getTiposConvenioMap().get("ccvalpac_"+indice).toString()));
		
		if(UtilidadTexto.isEmpty(forma.getTiposConvenioMap().get("ccdespac_"+indice)+""))
			mundo.setCuentaDescPaciente(ConstantesBD.codigoNuncaValido);
		else
			mundo.setCuentaDescPaciente(Utilidades.convertirAEntero(forma.getTiposConvenioMap().get("ccdespac_"+indice).toString()));
		
		if(UtilidadTexto.isEmpty(forma.getTiposConvenioMap().get("cccxccap_"+indice)+""))
			mundo.setCuentaCxcCapitacion(ConstantesBD.codigoNuncaValido);
		else
			mundo.setCuentaCxcCapitacion(Utilidades.convertirAEntero(forma.getTiposConvenioMap().get("cccxccap_"+indice).toString()));
		
		if(UtilidadTexto.isEmpty(forma.getTiposConvenioMap().get("ccutilxing_"+indice)+""))
			mundo.setCuentaContableUtil(ConstantesBD.codigoNuncaValido);
		else
			mundo.setCuentaContableUtil(Utilidades.convertirAEntero(forma.getTiposConvenioMap().get("ccutilxing_"+indice).toString()));
		
		if(UtilidadTexto.isEmpty(forma.getTiposConvenioMap().get("ccperdxing_"+indice)+""))
			mundo.setCuentaContablePerd(ConstantesBD.codigoNuncaValido);
		else
			mundo.setCuentaContablePerd(Utilidades.convertirAEntero(forma.getTiposConvenioMap().get("ccperdxing_"+indice).toString()));
		
		if(UtilidadTexto.isEmpty(forma.getTiposConvenioMap().get("ccdebglorec_"+indice)+""))
			mundo.setCuentaDebitoGlsRecibida(ConstantesBD.codigoNuncaValido);
		else
			mundo.setCuentaDebitoGlsRecibida(Utilidades.convertirAEntero(forma.getTiposConvenioMap().get("ccdebglorec_"+indice).toString()));
		
		if(UtilidadTexto.isEmpty(forma.getTiposConvenioMap().get("cccreglorec_"+indice)+""))
			mundo.setCuentaCreditoGlsRecibida(ConstantesBD.codigoNuncaValido);
		else
			mundo.setCuentaCreditoGlsRecibida(Utilidades.convertirAEntero(forma.getTiposConvenioMap().get("cccreglorec_"+indice).toString()));
		
		if(UtilidadTexto.isEmpty(forma.getTiposConvenioMap().get("ccdebgloace_"+indice)+""))
			mundo.setCuentaDebitoGlsAceptada(ConstantesBD.codigoNuncaValido);
		else
			mundo.setCuentaDebitoGlsAceptada(Utilidades.convertirAEntero(forma.getTiposConvenioMap().get("ccdebgloace_"+indice).toString()));
		
		if(UtilidadTexto.isEmpty(forma.getTiposConvenioMap().get("cccregloace_"+indice)+""))
			mundo.setCuentaCreditoGlsAceptada(ConstantesBD.codigoNuncaValido);
		else
			mundo.setCuentaCreditoGlsAceptada(Utilidades.convertirAEntero(forma.getTiposConvenioMap().get("cccregloace_"+indice).toString()));
		
		return mundo;		
	}
	
	/**
	 * verifica si el registro se ha modificado
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param String codigoCobertura
	 * @param int institucion
	 * @param int pos
	 * */
	private boolean existeModificacion(Connection con,TiposConvenioForm forma,TiposConvenio mundo, String codigo, int pos, UsuarioBasico usuario)
	{
		HashMap temp = mundo.consultarTiposConvenioEspecifico(con,usuario.getCodigoInstitucionInt(), codigo);
		Utilidades.imprimirMapa(forma.getTiposConvenioMap());	
		for(int i=0;i<indices.length;i++)
		{		
			if(temp.containsKey(indices[i]+"0")&&forma.getTiposConvenioMap().containsKey(indices[i]+pos))
			{				
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getTiposConvenioMap(indices[i]+pos)).toString().trim())))
				{
					logger.info("retorna true");
					return true;
				}				
			}
		}		
		logger.info("retorna false");
		return false;
		
	}
	
	
	/**
     * 
     * @param forma
     * @param mapping
     * @param con
     * @param usuario
     * @return
     */
	private ActionForward accionResumen(TiposConvenioForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) 
	{
		forma.reset();
		forma.setEstado("resumen");
		TiposConvenio mundo= new TiposConvenio();
		forma.setTiposConvenioMap(mundo.consultarTiposConvenio(con, usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param isEliminacion
	 * @param pos
	 */
	private void generarLog(TiposConvenioForm forma, TiposConvenio mundo, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getTiposConvenioEliminadosMap(indices[i]+""+pos)+""):forma.getTiposConvenioEliminadosMap(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(mundo.getTiposConvenioMap().get(indices[i]+"0")+""):mundo.getTiposConvenioMap().get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getTiposConvenioMap(indices[i]+""+pos)+""):forma.getTiposConvenioMap(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		
		LogsAxioma.enviarLog(ConstantesBD.logTiposConvenioCodigo,log,tipoLog,usuario.getLoginUsuario());
	}
	
}