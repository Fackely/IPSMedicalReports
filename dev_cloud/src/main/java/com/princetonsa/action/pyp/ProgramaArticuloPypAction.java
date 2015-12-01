/*
 * Creado el Aug 8, 2006
 * por Julian Montoya
 */
package com.princetonsa.action.pyp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;

import com.princetonsa.actionform.pyp.ProgramaArticuloPypForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.pyp.ProgramaArticuloPyp;

public class ProgramaArticuloPypAction extends Action {

	
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ProgramaArticuloPypAction.class);
	

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
			if(form instanceof ProgramaArticuloPypForm)
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				ProgramaArticuloPypForm forma =(ProgramaArticuloPypForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=forma.getEstado();  

				logger.warn("En ProgramaArticuloPypAction El Estado [" + estado + "] \n\n");	


				if(estado == null)
				{
					forma.reset();	
					logger.warn("Estado no valido dentro de ProgramaArticuloPypAction (NULL) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con, mapping,  forma, request, usuario); 
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con, mapping,  forma, request, usuario); 
				}
				else if (estado.equals("eliminar"))
				{
					return accionEliminar(con, mapping,  forma, request, usuario); 
				}
				else if (estado.equals("listar"))
				{
					return accionListar(con, mapping,  forma, request, usuario); 
				}
				else if (estado.equals("insertarFila")) //-- Para Insertar una Nueva Fila en el mapa.
				{
					return insertarFila(con, forma, mapping);
				}
				else if(estado.equals("empezarViasIngreso"))
				{
					ProgramaArticuloPyp mundo = new ProgramaArticuloPyp();
					forma.setActividad(forma.getMapa("cod_act_reg_"+forma.getIndex())+"");
					mundo.cargarViasIngreso(con,forma.getPrograma(),forma.getActividad(),usuario.getCodigoInstitucion());
					forma.setViasIngreso((HashMap)mundo.getViasIngreso());
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaViaIngreso");
				}
				else if(estado.equals("nuevaViaIngreso"))
				{
					this.nuevaViaIngreso(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaViaIngreso");
				}

				else if(estado.equals("eliminarViaIngreso"))
				{
					this.eliminarViaIngreso(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaViaIngreso");
				}

				else if(estado.equals("guardarViaIngreso"))
				{
					ProgramaArticuloPyp mundo = new ProgramaArticuloPyp();
					this.accionGuardarViasIngreso(con,forma,mundo,usuario);
					forma.resetMapas();
					mundo.setViasIngreso(new HashMap());
					mundo.cargarViasIngreso(con,forma.getPrograma(),forma.getActividad(),usuario.getCodigoInstitucion());
					forma.setViasIngreso((HashMap)mundo.getViasIngreso());
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaViaIngreso");
				}
				else if(estado.equals("empezarGuposEtareos"))
				{
					ProgramaArticuloPyp mundo = new ProgramaArticuloPyp();
					forma.setActividad(forma.getMapa("cod_act_reg_"+forma.getIndex())+"");
					if(!forma.getRegimenGrupoEtareo().trim().equals(""))
					{
						mundo.cargarGruposEtareos(con,forma.getPrograma(),forma.getActividad(),usuario.getCodigoInstitucion(),forma.getRegimenGrupoEtareo());
						forma.setGruposEtareos((HashMap)mundo.getGruposEtareos());
					}
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaGruposEtareos");
				}
				else if(estado.equals("nuevoGrupoEtareo"))
				{
					this.nuevGrupoEtareo(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaGruposEtareos");
				}

				else if(estado.equals("eliminarGrupoEtareo"))
				{
					this.eliminarGrupoEtareo(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaGruposEtareos");
				}
				else if(estado.equals("guardarGrupoEtareo"))
				{
					ProgramaArticuloPyp mundo = new ProgramaArticuloPyp();
					this.accionGuardarGruposEtareos(con,forma,mundo,usuario);
					forma.resetMapas();
					mundo.setGruposEtareos(new HashMap());
					mundo.cargarGruposEtareos(con,forma.getPrograma(),forma.getActividad(),usuario.getCodigoInstitucion(),forma.getRegimenGrupoEtareo());
					forma.setGruposEtareos((HashMap)mundo.getGruposEtareos());
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaGruposEtareos");
				}
				else if (estado.equals("eliminarFila")) //-- Para Eliminar una Nueva Fila en el mapa.
				{
					return eliminarFila(con, mapping, forma);
				}
				else if (estado.equals("redireccion"))  //--Estado para mantener los datos del pager
				{			    
					UtilidadBD.cerrarConexion(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de ProgramaArticuloPypAction (null) ");
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


	private void accionGuardarGruposEtareos(Connection con, ProgramaArticuloPypForm forma, ProgramaArticuloPyp mundo, UsuarioBasico usuario)
	{
		int numReg=Integer.parseInt(forma.getGruposEtareos().get("numRegistros")+"");
		int regEliminados=Integer.parseInt(forma.getGruposEtareosEliminados().get("numRegistros")+"");
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		//se encuentra entre la transaccion
		{
			//eliminar.
			for(int el=0;el<regEliminados;el++)
			{
				if(enTransaccion)
				{
					enTransaccion=mundo.eliminarRegistroGrupoEtareo(con,forma.getPrograma(),forma.getActividad(),usuario.getCodigoInstitucion(),forma.getRegimenGrupoEtareo(),forma.getGruposEtareosEliminados("grupoetareo_"+el)+"");
				}
				else
				{
					logger.error("ERROR ELIMINANDO GRUPO ETAREO");
					el=regEliminados;
				}
			}
			
			//insertar modificar.
	        for(int k=0;k<numReg;k++)
	        {
	        	if(enTransaccion)
	        	{
		        	//modificar
		        	if((forma.getGruposEtareos("tiporegistro_"+k)+"").equalsIgnoreCase("BD")&&enTransaccion)
		        	{
	        			enTransaccion=mundo.modificarRegistroGrupoEtareo(con,forma.getPrograma(),forma.getActividad(),usuario.getCodigoInstitucion(),forma.getRegimenGrupoEtareo(),forma.getGruposEtareos("grupoetareo_"+k)+"",forma.getGruposEtareos("frecuencia_"+k)+"",forma.getGruposEtareos("tipofrecuencia_"+k)+"");
		        	}
		        	//insertar
		        	if((forma.getGruposEtareos("tiporegistro_"+k)+"").equalsIgnoreCase("MEM")&& enTransaccion)
		        	{
		        		enTransaccion=mundo.insertarRegistroGrupoEtareo(con,forma.getPrograma(),forma.getActividad(),usuario.getCodigoInstitucion(),forma.getRegimenGrupoEtareo(),forma.getGruposEtareos("grupoetareo_"+k)+"",forma.getGruposEtareos("frecuencia_"+k)+"",forma.getGruposEtareos("tipofrecuencia_"+k)+"");
		        	}
	        	}
	        	else
	        	{
	        		logger.error("error modificando - insertando  GRUPO ETAREO");
	        		k=numReg;
	        	}
	        }
		}
		if(enTransaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
	}


	private void eliminarGrupoEtareo(ProgramaArticuloPypForm forma)
	{
		int pos=forma.getRegEliminar();
		int numReg=Integer.parseInt(forma.getGruposEtareos().get("numRegistros")+"");
		int ultimoRegistro=numReg-1;
		int regEliminados=Integer.parseInt(forma.getGruposEtareosEliminados().get("numRegistros")+"");
		String indices[]={"grupoetareo_","frecuencia_","tiporegistro_"};
		if((forma.getGruposEtareos("tiporegistro_"+pos)+"").equalsIgnoreCase("BD"))
		{
			for(int ind=0;ind<indices.length;ind++)
			{
				forma.setGruposEtareosEliminados(indices[ind]+""+regEliminados,forma.getGruposEtareos(indices[ind]+""+pos));
			}
			forma.setGruposEtareosEliminados("numRegistros",(regEliminados+1)+"");
		}
		for(int i=pos;i<ultimoRegistro;i++)
		{
			for(int ind=0;ind<indices.length;ind++)
			{
				forma.setGruposEtareos(indices[ind]+""+i,forma.getGruposEtareos(indices[ind]+""+(i+1)));
			}
		}
		for(int ind=0;ind<indices.length;ind++)
		{
			forma.getGruposEtareos().remove(indices[ind]+""+ultimoRegistro);
		}
		forma.setGruposEtareos("numRegistros",ultimoRegistro);
	}


	private void nuevGrupoEtareo(ProgramaArticuloPypForm forma)
	{
		int numReg=Integer.parseInt(forma.getGruposEtareos().get("numRegistros")+"");
		forma.setGruposEtareos("grupoetareo_"+numReg,"");
		forma.setGruposEtareos("frecuencia_"+numReg,"");
		forma.setGruposEtareos("tiporegistro_"+numReg,"MEM");
		forma.setGruposEtareos("numRegistros",(numReg+1)+"");
	}


	private void accionGuardarViasIngreso(Connection con, ProgramaArticuloPypForm forma, ProgramaArticuloPyp mundo, UsuarioBasico usuario)
	{

		int numReg=Integer.parseInt(forma.getViasIngreso().get("numRegistros")+"");
		int regEliminados=Integer.parseInt(forma.getViasIngresoEliminados().get("numRegistros")+"");
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		//se encuentra entre la transaccion
		{
			//eliminar.
			for(int el=0;el<regEliminados;el++)
			{
				if(enTransaccion)
				{
					enTransaccion=mundo.eliminarRegistroViaIngreso(con,forma.getPrograma(),forma.getActividad(),usuario.getCodigoInstitucion(),forma.getViasIngresoEliminados("viaingreso_"+el)+"",forma.getViasIngresoEliminados("ocupacion_"+el)+"");
				}
				else
				{
					logger.error("ERROR ELIMINANDO LAS VIAS DE INGRESO");
					el=regEliminados;
				}
			}
			
			//insertar modificar.
	        for(int k=0;k<numReg;k++)
	        {
	        	if(enTransaccion)
	        	{
		        	//modificar
		        	if((forma.getViasIngreso("tiporegistro_"+k)+"").equalsIgnoreCase("BD")&&enTransaccion)
		        	{
	        			enTransaccion=mundo.modificarRegistroViaIngreso(con,forma.getPrograma(),forma.getActividad(),usuario.getCodigoInstitucion(),forma.getViasIngreso("viaingreso_"+k)+"",forma.getViasIngreso("ocupacion_"+k)+"",UtilidadTexto.getBoolean(forma.getViasIngreso("solicitar_"+k)+""),UtilidadTexto.getBoolean(forma.getViasIngreso("programar_"+k)+""),UtilidadTexto.getBoolean(forma.getViasIngreso("ejecutar_"+k)+""));
		        	}
		        	//insertar
		        	if((forma.getViasIngreso("tiporegistro_"+k)+"").equalsIgnoreCase("MEM")&& enTransaccion)
		        	{
		        		enTransaccion=mundo.insertarRegistroViaIngreso(con,forma.getPrograma(),forma.getActividad(),usuario.getCodigoInstitucion(),forma.getViasIngreso("viaingreso_"+k)+"",forma.getViasIngreso("ocupacion_"+k)+"",UtilidadTexto.getBoolean(forma.getViasIngreso("solicitar_"+k)+""),UtilidadTexto.getBoolean(forma.getViasIngreso("programar_"+k)+""),UtilidadTexto.getBoolean(forma.getViasIngreso("ejecutar_"+k)+""));
		        	}
	        	}
	        	else
	        	{
	        		logger.error("error modificando - insertando LAS VIAS DE INGRESO");
	        		k=numReg;
	        	}
	        }	
	        		
		}
		if(enTransaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
	}


	private void eliminarViaIngreso(ProgramaArticuloPypForm forma)
	{
		int pos=forma.getRegEliminar();
		int numReg=Integer.parseInt(forma.getViasIngreso().get("numRegistros")+"");
		int ultimoRegistro=numReg-1;
		int regEliminados=Integer.parseInt(forma.getViasIngresoEliminados().get("numRegistros")+"");
		String indices[]={"viaingreso_","ocupacion_","solicitar_","programar_","ejecutar_","tiporegistro_"};
		if((forma.getViasIngreso("tiporegistro_"+pos)+"").equalsIgnoreCase("BD"))
		{
			for(int ind=0;ind<indices.length;ind++)
			{
				forma.setViasIngresoEliminados(indices[ind]+""+regEliminados,forma.getViasIngreso(indices[ind]+""+pos));
			}
			forma.setViasIngresoEliminados("numRegistros",(regEliminados+1)+"");
		}
		for(int i=pos;i<ultimoRegistro;i++)
		{
			for(int ind=0;ind<indices.length;ind++)
			{
				forma.setViasIngreso(indices[ind]+""+i,forma.getViasIngreso(indices[ind]+""+(i+1)));
			}
		}
		for(int ind=0;ind<indices.length;ind++)
		{
			forma.getViasIngreso().remove(indices[ind]+""+ultimoRegistro);
		}
		forma.setViasIngreso("numRegistros",ultimoRegistro);
	}


	private void nuevaViaIngreso(ProgramaArticuloPypForm forma)
	{
		int numReg=Integer.parseInt(forma.getViasIngreso().get("numRegistros")+"");
		forma.setViasIngreso("viaingreso_"+numReg,"");
		forma.setViasIngreso("ocupacion_"+numReg,"-1");
		forma.setViasIngreso("solicitar_"+numReg,"false");
		forma.setViasIngreso("programar_"+numReg,"false");
		forma.setViasIngreso("ejecutar_"+numReg,"false");
		forma.setViasIngreso("tiporegistro_"+numReg,"MEM");
		forma.setViasIngreso("numRegistros",(numReg+1)+"");
	}


	/**
	 * Metodo para insertar una fila de nuevos.
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 * @throws SQLException
	 */
	private ActionForward insertarFila(Connection con, ProgramaArticuloPypForm forma, ActionMapping mapping) throws SQLException
	{
		if ( UtilidadCadena.noEsVacio(forma.getMapa("nroRegistrosNv")+"") )
		{
			forma.setMapa("nroRegistrosNv", Integer.parseInt(forma.getMapa("nroRegistrosNv")+"")+1 +"");
		}
		else
		{
			forma.setMapa("nroRegistrosNv", "1");
		}
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Metodo para eliminar las filas nuevas ..
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 * @throws SQLException
	 */
	private ActionForward eliminarFila(Connection con, ActionMapping mapping, ProgramaArticuloPypForm forma) throws SQLException 
	{
		int indice = 0, nroRows = 0;

		
		if ( UtilidadCadena.noEsVacio(forma.getMapa("nroFilaEliminar")+"") )
		{
			indice = forma.getNroArticuloEliminado();
			nroRows = Integer.parseInt(forma.getMapa("nroRegistrosNv")+""); 
			boolean encontro = false;
			int k = 0;	

			
			HashMap mp  = forma.getMapa();
			
			for (int i = 0; i < nroRows; i++)
			{
				if ( i == indice )
				{
					mp.remove("cod_art_nv_"+ i);
					mp.remove("act_art_nv_"+ i);
					encontro = true;
				}
				else
				{
					mp.put("cod_art_nv_"+ k, forma.getMapa("cod_art_nv_"+ i)+"");
					mp.put("act_art_nv_"+ k, forma.getMapa("act_art_nv_"+ i)+"");
					k++;	
				}
			}  
			

			forma.setMapa(mp);
			
			//-- Si encontro el indice puede Actualizar el Contador
			if ( encontro )
			{
				forma.setNroArticuloEliminado(0);
				forma.setMapa("nroRegistrosNv", Integer.parseInt(forma.getMapa("nroRegistrosNv")+"")-1 +"");
			}	
		}

		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}



	/**
	 * Metodo para listar los Articulos de un programa especifico.
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionListar(Connection con, ActionMapping mapping, ProgramaArticuloPypForm forma, HttpServletRequest request, UsuarioBasico usuario) throws SQLException
	{
		ProgramaArticuloPyp mundo = new ProgramaArticuloPyp();
		
		String progAux = "";
		progAux = forma.getPrograma();
		forma.reset();
		forma.setPrograma(progAux);			


		//-- Cargar Los Programas.
		HashMap mp = new HashMap();
		mp.put("nroConsulta", "1");
		mp.put("codigoInstitucion", "" + usuario.getCodigoInstitucionInt());
		forma.setMapa( mundo.consultarInformacion(con, mp) );
		forma.setMapa("nroRegistrosNv","0");

		//-- Cargar Los articulos para llenar los select <html>
		mp.clear(); 
		mp.put("nroConsulta", "2");
		mp.put("codigoInstitucion", "" + usuario.getCodigoInstitucionInt());
		mp.put("programa", "" + forma.getPrograma());
		
		mp = mundo.consultarInformacion(con, mp);
		forma.setMapa( "nroRegistrosAct", mp.get("numRegistros")+"" );
		mp.remove("numRegistros");
		forma.getMapa().putAll(mp);

		//-- Cargar Los Articulos seleccionados de un programa especifico 
		mp.clear(); 
		mp.put("nroConsulta", "3");
		mp.put("codigoInstitucion", "" + usuario.getCodigoInstitucionInt());
		mp.put("programa", forma.getPrograma());
		
		mp = mundo.consultarInformacion(con, mp);
		forma.setMapa( "nroRegistrosReg", mp.get("numRegistros")+"" );
		mp.remove("numRegistros");
		forma.getMapa().putAll(mp);

		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
		
	}

	/**
	 * Metodo para eliminar una Articulo de un Programa de Salud PYP Especifico. 
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionEliminar(Connection con, ActionMapping mapping, ProgramaArticuloPypForm forma, HttpServletRequest request, UsuarioBasico usuario) throws SQLException
	{
		ProgramaArticuloPyp mundo = new ProgramaArticuloPyp(); 
		int res = mundo.eliminarActividadesCentroAtencion( con, forma, usuario );
		return accionListar(con, mapping, forma, request, usuario);
	}

	/**
	 * Metodo para Guardar Modificar Los Articulos por Programa PYP
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGuardar(Connection con, ActionMapping mapping, ProgramaArticuloPypForm forma, HttpServletRequest request, UsuarioBasico usuario) throws SQLException 
	{
		ProgramaArticuloPyp mundo = new ProgramaArticuloPyp();
		int res = mundo.insertarArticulosPrograma( con, forma.getMapa(), forma.getPrograma(),forma.getNombrePrograma(), usuario );
		return accionListar(con, mapping, forma, request, usuario);
	}


	/**
	 * Metodo para iniciar el Flujo de la funcionalidad.
	 * @param con
	 * @param mapping 
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, ProgramaArticuloPypForm forma, HttpServletRequest request, UsuarioBasico usuario) throws SQLException
	{
		ProgramaArticuloPyp mundo = new ProgramaArticuloPyp();

		//-- Limpiar la Forma.
		forma.reset();
		
		//-- Cargar Los Programas.
		HashMap mp = new HashMap();
		mp.put("nroConsulta", "1");
		mp.put("codigoInstitucion", "" + usuario.getCodigoInstitucionInt());
		
		forma.setMapa( mundo.consultarInformacion(con, mp) );
		forma.setMapa("nroRegistrosNv","0");
		

		//-- Cargar Las Actividades para Llenar Los Select
		mp.clear(); 
		mp.put("nroConsulta", "2");
		mp.put("codigoInstitucion", "" + usuario.getCodigoInstitucionInt());
		mp.put("programa", "" + forma.getPrograma());
		
		mp = mundo.consultarInformacion(con, mp);
		forma.setMapa( "nroRegistrosAct", mp.get("numRegistros")+"" );
		mp.remove("numRegistros");
		forma.getMapa().putAll(mp);

		
		//--
		UtilidadBD.cerrarConexion(con);
		return  mapping.findForward("principal");
	}
	

}
