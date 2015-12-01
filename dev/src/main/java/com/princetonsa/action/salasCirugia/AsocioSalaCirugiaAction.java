package com.princetonsa.action.salasCirugia;

import java.sql.Connection;
import java.util.ArrayList;
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
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.UtilidadFecha;
import com.princetonsa.actionform.salasCirugia.AsocioSalaCirugiaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.salasCirugia.AsocioSalaCirugia;

/**
 * Clase para el manejo del workflow de la funcionalidad
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 */

public class AsocioSalaCirugiaAction extends Action
{
	/**
	 * Objeto para manejar el log de la clase
	 * */	
	private Logger logger = Logger.getLogger(AsocioSalaCirugiaAction.class);
	
	/**
	 * Metodo execute del action
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @return ActionForward
	 * */
	public ActionForward execute(ActionMapping mapping, 
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response) throws Exception
								 {

		Connection con = null;
		try{
			if(response==null);
			if(form instanceof AsocioSalaCirugiaForm)		 
			{

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","errors.problemasBd");
					return mapping.findForward("paginaError");				 
				}

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				AsocioSalaCirugiaForm forma = (AsocioSalaCirugiaForm)form;
				String estado = forma.getEstado();
				ActionErrors errores = new ActionErrors();

				logger.warn("n\n\nEl estado en AsocioSalaCirugiaAction es------->"+estado+"\n");		 

				if (estado == null)
				{				 
					forma.reset();
					logger.warn("Estado no Valido dentro del flujo de Coberturas (null)");
					request.setAttribute("CodigDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginError");			  
				}		 
				else if(estado.equals("empezar"))
				{				 
					inicializar(con,forma,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("nuevo"))
				{
					UtilidadBD.closeConnection(con);
					errores = accionNuevo(forma,errores);

					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						return mapping.findForward("principal");
					}

					return UtilidadSesion.redireccionar("", ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getAsocioMap("numRegistros").toString()), response, request, "asociosSalaCirugia.jsp",true);				 		 
				}
				else if(estado.equals("ordenar"))
				{
					UtilidadBD.closeConnection(con);
					accionOrdenarMapa(forma);
					return mapping.findForward("principal");				 
				}
				else if(estado.equals("eliminarCampo"))
				{				 
					accionEliminarCampo(con,forma, request, response, mapping,usuario.getCodigoInstitucionInt());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardar"))
				{				 
					accionGuardarRegistros(con, forma, mapping, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if (estado.equals("redireccion"))
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
	 * Inicializa los atributos de la forma para la funcionalidad
	 * @param Connection con
	 * @param UsuarioBasico usuario
	 * */
	public void inicializar(Connection con,AsocioSalaCirugiaForm forma,UsuarioBasico usuario)
	{
		forma.reset();
		forma.setTipoServicioArray(AsocioSalaCirugia.consultaTiposServicios(con));
		forma.setAsocioEliminadoMap("numRegistros","0");
		forma.setCentrosCosto(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaDirecto+"", false, ConstantesBD.codigoNuncaValido, false));

		forma.setAsocioMap(AsocioSalaCirugia.consultaAsocioSalaCirugia(con, usuario.getCodigoInstitucionInt()));	
		
		int numRegistros = Integer.parseInt(forma.getAsocioMap("numRegistros").toString());
		for(int i = 0; i< numRegistros; i++)
			forma.setAsocioMap("esUsado_"+i,false);		
	}
		
	
	/**
	 * Genera un Nuevo Registro en el HashMap de AsociosMap
	 *@param forma
	 *@param HttpServletRequest request
	 *@param ActionErrors errores
	 * */
	private ActionErrors accionNuevo(AsocioSalaCirugiaForm forma,ActionErrors errores)
	{	
		for(int i = 0; i<Integer.parseInt(forma.getAsocioMap("numRegistros").toString()); i++)
		{
			if(forma.getAsocioMap("estabd_"+i).toString().equals(ConstantesBD.acronimoNo))
			{
				errores.add("descripcion",new ActionMessage("errors.notEspecific","Ya existe un Registro Nuevo, favor guardar información antes de Ingresar uno Nuevo."));				
				return errores;
			}				
		}
		
		
		int pos = Integer.parseInt(forma.getAsocioMap("numRegistros")+"");
		forma.setAsocioMap("codigoasocio_"+pos,"");		
		forma.setAsocioMap("nombreasocio_"+pos,"");
		forma.setAsocioMap("tiposervicio_"+pos,ConstantesBD.codigoNuncaValido+"");		
		forma.setAsocioMap("participacir_"+pos,ConstantesBD.acronimoNo);
		forma.setAsocioMap("estabd_"+pos,ConstantesBD.acronimoNo);		
		forma.setAsocioMap("esUsado_"+pos,"false");
		forma.setAsocioMap("numRegistros",(pos+1)+"");
		
		return errores;
	}	
	
	
	/**
	 * Ordenar el Mapa 
	 * @param forma
	 * */
	private void accionOrdenarMapa(AsocioSalaCirugiaForm forma)
	{
		String[] indices = (String[])forma.getAsocioMap("INDICES_MAP");
		int numReg = Integer.parseInt(forma.getAsocioMap("numRegistros")+"");
		forma.setAsocioMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUtilmoPatron(), forma.getAsocioMap(), numReg));
		forma.setUtilmoPatron(forma.getPatronOrdenar());
		forma.setAsocioMap("numRegistros",numReg+"");
		forma.setAsocioMap("INDICES_MAP",indices);		
	}
	
	
	/**
	 * Elimina un Registro del HashMap de Asocio y Adicciona el Registro Eliminado en el HashMap de Asocios Eliminados
	 * @param forma
	 * @param request
	 * @param response
	 * @param mapping
	 * @return ActionForward
	 */
	private void accionEliminarCampo(
			Connection con,
			AsocioSalaCirugiaForm forma, 
			HttpServletRequest request, 
			HttpServletResponse response, 
			ActionMapping mapping,			
			int codigoInstitucion) 
	{		
		int ultimaPosMapa=(Integer.parseInt(forma.getAsocioMap("numRegistros")+"")-1);
		forma.setMensajesList(new ArrayList());
		
		if( (forma.getAsocioMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi)
				&& UtilidadTexto.getBoolean(UtilidadValidacion.esAsocioSalaCirugiaUsado(con,forma.getAsocioMap("codigoasocio_"+forma.getIndexEliminado()).toString(),codigoInstitucion)+""))
		{			
			forma.setAsocioMap("esUsado_"+forma.getIndexEliminado(),true);
			//se almacena la informacion de los mensajes de error
			forma.getMensajesList().add("El Codigo del registro Nro. "+(forma.getIndexEliminado()+1)+" esta siendo referenciado por otra funcionalidad y no puede ser Eliminado.");
		}
		else
			forma.setAsocioMap("esUsado_"+forma.getIndexEliminado(),false);
		
		
		if(!UtilidadTexto.getBoolean(forma.getAsocioMap("esUsado_"+forma.getIndexEliminado()).toString()))
		{
		
			int numRegMapEliminados=Integer.parseInt(forma.getAsocioEliminadoMap("numRegistros")+"");			
			
			//poner la informacion en el otro mapa.
			String[] indices= (String[])forma.getAsocioMap("INDICES_MAP");		
			
			for(int i=0;i<indices.length;i++)
			{ 
				//solo pasar al mapa los registros que son de BD
				if((forma.getAsocioMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
				{
					forma.setAsocioEliminadoMap(indices[i]+""+numRegMapEliminados, forma.getAsocioMap(indices[i]+""+forma.getIndexEliminado()));
				}
			}		
			
			if((forma.getAsocioMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
			{					
				forma.setAsocioEliminadoMap("numRegistros", (numRegMapEliminados+1));
			}
			
			//acomodar los registros del mapa en su nueva posicion
			for(int i=forma.getIndexEliminado();i<ultimaPosMapa;i++)
			{
				for(int j=0;j<indices.length;j++)
				{
					forma.setAsocioMap(indices[j]+""+i,forma.getAsocioMap(indices[j]+""+(i+1)));
				}
			}
			
			//ahora eliminamos el ultimo registro del mapa.
			for(int j=0;j<indices.length;j++)
			{
				forma.getAsocioMap().remove(indices[j]+""+ultimaPosMapa);
			}
			
			//ahora actualizamos el numero de registros en el mapa.
			forma.setAsocioMap("numRegistros",ultimaPosMapa);
		}		
	}
	
		
	
	/**
	 * Guarda, Modifica o Elimina un Registro en Asocios
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return ActionForward
	 * */
	private void accionGuardarRegistros(Connection con, AsocioSalaCirugiaForm forma, ActionMapping mapping,UsuarioBasico usuario)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(con);
		String estadoEliminar="ninguno";
		HashMap parametros = new HashMap();
		
		
		logger.info("valor mapa asocio GUARDAR >>  "+forma.getAsocioMap());
		
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getAsocioEliminadoMap("numRegistros")+"");i++)
		{
			if(AsocioSalaCirugia.eliminaAsocioSalaCirugia(con,usuario.getCodigoInstitucionInt(),forma.getAsocioEliminadoMap("codigoasocio_"+i).toString().trim()))			
			{				
				 this.generarlog(forma, new HashMap(), usuario, true, i);
				 estadoEliminar="operacionTrue";
				 transacction=true;						 
			}
		}		
		
		for(int i=0;i<Integer.parseInt(forma.getAsocioMap("numRegistros")+"");i++)
		{			
			//modificar			
			if((forma.getAsocioMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi)&&
					this.existeModificacion(con,forma,forma.getAsocioMap("codigoasocioold_"+i).toString(),usuario.getCodigoInstitucionInt(),i,usuario))			{					
				parametros.put("codigoasocio",forma.getAsocioMap("codigoasocio_"+i));
				parametros.put("nombreasocio",forma.getAsocioMap("nombreasocio_"+i));
				parametros.put("tiposservicio",forma.getAsocioMap("tiposservicio_"+i));
				if(forma.getAsocioMap().containsKey("ccejecuta_"+i))
					parametros.put("ccejecuta",forma.getAsocioMap("ccejecuta_"+i));
				parametros.put("participacir",forma.getAsocioMap("participacir_"+i));
				
				parametros.put("codigoasocioold",forma.getAsocioMap("codigoasocioold_"+i));
				parametros.put("institucion",usuario.getCodigoInstitucionInt());
				
				transacction = AsocioSalaCirugia.actualizarAsocioSalaCirugia(con, parametros);
				estadoEliminar="operacionTrue";
			}
			
			//insertar
			else if((forma.getAsocioMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				parametros.put("institucion",usuario.getCodigoInstitucionInt());
				parametros.put("codigoasocio",forma.getAsocioMap("codigoasocio_"+i));
				parametros.put("nombreasocio",forma.getAsocioMap("nombreasocio_"+i));
				parametros.put("tiposservicio",forma.getAsocioMap("tiposservicio_"+i));
				if(forma.getAsocioMap().containsKey("ccejecuta_"+i))
					parametros.put("ccejecuta",forma.getAsocioMap("ccejecuta_"+i));
				parametros.put("participacir",forma.getAsocioMap("participacir_"+i));
				
				parametros.put("usuariomodifica",usuario.getLoginUsuario());
				parametros.put("fechamodifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				parametros.put("horamodifica",UtilidadFecha.getHoraActual());
				
				transacction = AsocioSalaCirugia.insertarAsocioSalaCirugia(con, parametros);				
				estadoEliminar="operacionTrue";
			}			
		}
		
		if(transacction)
		{
			forma.setEstado(estadoEliminar);
			UtilidadBD.finalizarTransaccion(con);			
			logger.info("----->INSERTO 100% ASOCIOS SALA CIRUGIA");
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
		
		this.inicializar(con, forma, usuario);	
	}
	
	
	
	/**
	 * verifica si el registro se ha modificado
	 * @param con
	 * @param forma	
	 * @param int codigoAsocio
	 * @param int institucion
	 * @param int pos
	 * @param UsuarioBasico usuario
	 * */
	private boolean existeModificacion(Connection con,AsocioSalaCirugiaForm forma,String codigoAsocio, int institucion, int pos, UsuarioBasico usuario)
	{
		HashMap temp = AsocioSalaCirugia.consultaAsocioSalaCirugia(con, institucion,codigoAsocio);
		String[] indices = (String[])forma.getAsocioMap("INDICES_MAP");		
				
		for(int i=0;i<indices.length;i++)
		{		
			if(temp.containsKey(indices[i]+"0")&&forma.getAsocioMap().containsKey(indices[i]+pos))
			{				
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getAsocioMap(indices[i]+pos)).toString().trim())))
				{
					this.generarlog(forma, temp, usuario, false, pos);
					return true;
				}				
			}
		}		
		return false;		
	}	
	
	
	
	/**
	 * General el Documento Log 
	 * @param forma
	 * @param HashMap temp
	 * @param UsuarioBasico usuario
	 * @param isEliminacion
	 * @param pos
	 * */
	private void generarlog(AsocioSalaCirugiaForm forma, HashMap mapaAsocioTemp, UsuarioBasico usuario, boolean isEliminacion ,int pos )
	{
		String log = "";
		int tipoLog=0;
		String[] indices= (String[])forma.getAsocioMap("INDICES_MAP");
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(forma.getAsocioEliminadoMap(indices[i]+""+pos)+""):forma.getAsocioEliminadoMap(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(mapaAsocioTemp.get(indices[i]+"0")+""):mapaAsocioTemp.get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(forma.getAsocioMap(indices[i]+""+pos)+""):forma.getAsocioMap(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(ConstantesBD.logAsociosSalaCirugiaCodigo,log,tipoLog,usuario.getLoginUsuario());		
	}	
}