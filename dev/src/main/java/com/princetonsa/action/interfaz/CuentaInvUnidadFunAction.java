/*
 * Abr 16, 2007
 */
package com.princetonsa.action.interfaz;

import java.io.IOException;
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
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.interfaz.CuentaInvUnidadFunForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.interfaz.CuentaInvUnidadFun;

/**
 * 
 * @author sgomez
 *	 Action usado para controlar los procesos de cuentas inventario
 *	por unidad funcional
 */
public class CuentaInvUnidadFunAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(CuentaInvUnidadFunAction.class);
	
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
			if(form instanceof CuentaInvUnidadFunForm)
			{
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				//OBJETOS A USAR
				CuentaInvUnidadFunForm cuentaForm =(CuentaInvUnidadFunForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=cuentaForm.getEstado(); 
				logger.warn("estado CuentaInvUnidadFunAction-->"+estado);


				if(estado == null)
				{
					cuentaForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Cuenta Inventario X Unidad Funcional (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
				//**************ESTADOS RELACIONADOS CON LAS CLASES************************************
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,cuentaForm,mapping,usuario);
				}
				else if (estado.equals("cargarClases"))
				{
					return accionCargarClases(con,cuentaForm,mapping,usuario);
				}
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,cuentaForm,response);
				}
				else if (estado.equals("ordenarClases"))
				{
					return accionOrdenarClases(con,cuentaForm,mapping);
				}
				else if (estado.equals("guardarClases"))
				{
					return accionGuardarClases(con,cuentaForm,mapping,usuario,request);
				}
				//****************************************************************************************
				//**************ESTADOS RELACIONADOS CON LOS GRUPOS****************************************
				else if (estado.equals("cargarGrupos"))
				{
					return accionCargarGrupos(con,cuentaForm,usuario,request,response);
				}
				else if (estado.equals("ordenarGrupos"))
				{
					return accionOrdenarGrupos(con,cuentaForm,mapping);
				}
				else if (estado.equals("guardarGrupos"))
				{
					return accionGuardarGrupos(con,cuentaForm,mapping,usuario,request,response);
				}
				//******************************************************************************************
				//**************ESTADOS RELACIONADOS CON LOS SUBGRUPOS****************************************
				else if (estado.equals("cargarSubgrupos"))
				{
					return accionCargarSubgrupos(con,cuentaForm,usuario,request,response);
				}
				else if (estado.equals("ordenarSubgrupos"))
				{
					return accionOrdenarSubgrupos(con,cuentaForm,mapping);
				}
				else if (estado.equals("guardarSubgrupos"))
				{
					return accionGuardarSubgrupos(con,cuentaForm,mapping,usuario,request,response);
				}
				//******************************************************************************************
				else
				{
					cuentaForm.reset();
					logger.warn("Estado no valido dentro del flujo de Cuenta Inventario X Unidad Funcional (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
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
	 * Método implementado para guardar los subgrupos de un grupo seleccionado
	 * @param con
	 * @param cuentaForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionGuardarSubgrupos(Connection con, CuentaInvUnidadFunForm cuentaForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request, HttpServletResponse response) 
	{
		int resp = 0;
		
		//Se instancia mundo CuentaInvUnidaDfun
		CuentaInvUnidadFun cuentaInv = new CuentaInvUnidadFun();
		
		//Se inicia transacción
		UtilidadBD.iniciarTransaccion(con);
		
		//Se llena el mundo
		cuentaInv.setUnidadFuncional(cuentaForm.getAcronimoUnidadFuncional());
		cuentaInv.setClase(cuentaForm.getClase());
		cuentaInv.setGrupo(cuentaForm.getGrupo());
		cuentaInv.setInstitucion(usuario.getCodigoInstitucionInt());
		
		for(int i=0;i<cuentaForm.getNumSubgrupos();i++)
		{
			//Se toma la informacion
			cuentaInv.setCuentaCosto(Integer.parseInt(
				cuentaForm.getSubgrupos("codigoCuentaCosto_"+i).toString().equals("")?"0":cuentaForm.getSubgrupos("codigoCuentaCosto_"+i).toString()
				)
			);
			cuentaInv.setCodigo(Integer.parseInt(cuentaForm.getSubgrupos("codigo_"+i).toString()));
			cuentaInv.setSubgrupo(Integer.parseInt(cuentaForm.getSubgrupos("codigoSubgrupo_"+i).toString()));
			
			//Se verifica si ya existía en la base de datos
			if(UtilidadTexto.getBoolean(cuentaForm.getSubgrupos("existeBd_"+i).toString()))
			{
				//**********SE REALIZA MODIFICACION*************************************
				resp = cuentaInv.modificarSubgrupo(con);
				
				if(resp>0)
					this.generarLog(cuentaForm,i,usuario,"subgrupo");
				//***********************************************************************
			}
			else
			{
				//*********SE REALIZA INSERCIÓN******************************************
				resp = cuentaInv.insertarSubgrupo(con);
				//***********************************************************************
			}
			
			
			if(resp<=0)
				i = cuentaForm.getNumSubgrupos();
		}
		
		if(resp>0)
		{
			UtilidadBD.finalizarTransaccion(con);
			//se conservan datos de la forma-------------------------------------
			this.resetearForm(cuentaForm);
			
			cuentaForm.setEstado("guardarSubgrupos");
			return accionCargarSubgrupos(con, cuentaForm, usuario,request,response);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			ActionErrors errores = new ActionErrors();
			errores.add("Error procesando la informacion",new ActionMessage("errors.noSeGraboInformacion","DE SUBGRUPOS INVENTARIO"));
			saveErrors(request, errores);
			cuentaForm.setEstado("cargarSubgrupos");
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaSubgrupo");
	}


	/**
	 * Método implementado para realizar la ordenacion del listado de subgrupos del grupo seleccionado
	 * @param con
	 * @param cuentaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarSubgrupos(Connection con, CuentaInvUnidadFunForm cuentaForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices = {
				"codigo_",
				"codigoSubgrupo_",
				"codigoGrupo_",
				"codigoClase_",
				"nombre_",
				"codigoCuentaCosto_",
				"cuentaCosto_",
				"existeBd_"
			};

		cuentaForm.setSubgrupos(Listado.ordenarMapa(indices,
				cuentaForm.getIndice(),
				cuentaForm.getUltimoIndice(),
				cuentaForm.getSubgrupos(),
				cuentaForm.getNumSubgrupos()));
		
		cuentaForm.setSubgrupos("numRegistros",cuentaForm.getNumSubgrupos()+"");
		
		cuentaForm.setUltimoIndice(cuentaForm.getIndice());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaSubgrupo");
	}


	/**
	 * Método que carga los subgrupos del grupo y clase seleccionados
	 * @param con
	 * @param cuentaForm
	 * @param usuario
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionCargarSubgrupos(Connection con, CuentaInvUnidadFunForm cuentaForm, UsuarioBasico usuario, HttpServletRequest request, HttpServletResponse response) 
	{
		//Se toma informacion de la clase elegida
		if(cuentaForm.getGrupos("codigo_"+cuentaForm.getPosGrupo())!=null&&
				cuentaForm.getGrupos("nombre_"+cuentaForm.getPosGrupo())!=null)
		{
			cuentaForm.setGrupo(Integer.parseInt(cuentaForm.getGrupos("codigo_"+cuentaForm.getPosGrupo()).toString()));
			cuentaForm.setNombreGrupo(cuentaForm.getGrupos("nombre_"+cuentaForm.getPosGrupo()).toString());
		}
		
		//Se instancia objeto CuentaInvUnidadFun
		CuentaInvUnidadFun cuentaInv = new CuentaInvUnidadFun();
		
		//Se asigna la informacion
		cuentaInv.setClase(cuentaForm.getClase());
		cuentaInv.setGrupo(cuentaForm.getGrupo());
		cuentaInv.setUnidadFuncional(cuentaForm.getAcronimoUnidadFuncional());
		cuentaInv.setInstitucion(usuario.getCodigoInstitucionInt());
		
		cuentaForm.setSubgrupos(cuentaInv.consultarSubgrupos(con));
		cuentaForm.setNumSubgrupos(Integer.parseInt(cuentaForm.getSubgrupos("numRegistros").toString()));
		cuentaForm.setSubgruposHistorico(new HashMap(cuentaForm.getSubgrupos()));
		
		//Se inicializa el registro de ultima columan ordenada
		cuentaForm.setUltimoIndice("");
		
		UtilidadBD.closeConnection(con);
		cuentaForm.setOffset(0);
		return UtilidadSesion.redireccionar("subgrupoCtaInvUnidadFun.jsp",cuentaForm.getMaxPageItems(),cuentaForm.getNumSubgrupos(), response, request, "subgrupoCtaInvUnidadFun.jsp",false);
	}

	/**
	 * Método implementado para guardar los grupos
	 * @param con
	 * @param cuentaForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param response 
	 * @return
	 */
	private ActionForward accionGuardarGrupos(Connection con, CuentaInvUnidadFunForm cuentaForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request, HttpServletResponse response) 
	{
		int resp = 0;
		
		//Se instancia mundo CuentaInvUnidaDfun
		CuentaInvUnidadFun cuentaInv = new CuentaInvUnidadFun();
		
		//Se inicia transacción
		UtilidadBD.iniciarTransaccion(con);
		
		//Se llena el mundo
		cuentaInv.setUnidadFuncional(cuentaForm.getAcronimoUnidadFuncional());
		cuentaInv.setClase(cuentaForm.getClase());
		cuentaInv.setInstitucion(usuario.getCodigoInstitucionInt());
		
		for(int i=0;i<cuentaForm.getNumGrupos();i++)
		{
			//Se toma la informacion
			cuentaInv.setCuentaCosto(Integer.parseInt(
				cuentaForm.getGrupos("codigoCuentaCosto_"+i).toString().equals("")?"0":cuentaForm.getGrupos("codigoCuentaCosto_"+i).toString()
				)
			);
			cuentaInv.setGrupo(Integer.parseInt(cuentaForm.getGrupos("codigo_"+i).toString()));
			
			//Se verifica si ya existía en la base de datos
			if(UtilidadTexto.getBoolean(cuentaForm.getGrupos("existeBd_"+i).toString()))
			{
				//**********SE REALIZA MODIFICACION*************************************
				resp = cuentaInv.modificarGrupo(con);
				
				if(resp>0)
					this.generarLog(cuentaForm,i,usuario,"grupo");
				//***********************************************************************
			}
			else
			{
				//*********SE REALIZA INSERCIÓN******************************************
				resp = cuentaInv.insertarGrupo(con);
				//***********************************************************************
			}
			
			
			if(resp<=0)
				i = cuentaForm.getNumGrupos();
		}
		
		if(resp>0)
		{
			UtilidadBD.finalizarTransaccion(con);
			//se conservan datos de la forma-------------------------------------
			this.resetearForm(cuentaForm);
			
			cuentaForm.setEstado("guardarGrupos");
			return accionCargarGrupos(con, cuentaForm, usuario,request,response);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			ActionErrors errores = new ActionErrors();
			errores.add("Error procesando la informacion",new ActionMessage("errors.noSeGraboInformacion","DE GRUPOS INVENTARIO"));
			saveErrors(request, errores);
			cuentaForm.setEstado("cargarGrupos");
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaGrupo");
	}

	/**
	 * Método implementado para resetear la forma conservando los valores principales
	 * @param cuentaForm
	 */
	private void resetearForm(CuentaInvUnidadFunForm cuentaForm) 
	{
		int maxPageItems = cuentaForm.getMaxPageItems();
		HashMap unidadesFun = cuentaForm.getUnidadesFun();
		int numUnidadesFun = cuentaForm.getNumUnidadesFun();
		String unidadFuncional = cuentaForm.getUnidadFuncional();
		int clase = cuentaForm.getClase();
		String nombreClase = cuentaForm.getNombreClase();
		int grupo = cuentaForm.getGrupo();
		String nombreGrupo = cuentaForm.getNombreGrupo();
		
		cuentaForm.reset();
		
		cuentaForm.setUnidadFuncional(unidadFuncional);
		cuentaForm.setUnidadesFun(unidadesFun);
		cuentaForm.setNumUnidadesFun(numUnidadesFun);
		cuentaForm.setMaxPageItems(maxPageItems);
		cuentaForm.setClase(clase);
		cuentaForm.setNombreClase(nombreClase);
		cuentaForm.setGrupo(grupo);
		cuentaForm.setNombreGrupo(nombreGrupo);
		
	}

	/**
	 * Método implementado para la ordenacion de los grupos de una clase específica
	 * @param con
	 * @param cuentaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarGrupos(Connection con, CuentaInvUnidadFunForm cuentaForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices = {
				"codigo_",
				"codigoClase_",
				"nombre_",
				"codigoCuentaCosto_",
				"cuentaCosto_",
				"existeBd_"
			};

		cuentaForm.setGrupos(Listado.ordenarMapa(indices,
				cuentaForm.getIndice(),
				cuentaForm.getUltimoIndice(),
				cuentaForm.getGrupos(),
				cuentaForm.getNumGrupos()));
		
		cuentaForm.setGrupos("numRegistros",cuentaForm.getNumGrupos()+"");
		
		cuentaForm.setUltimoIndice(cuentaForm.getIndice());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaGrupo");
	}

	/**
	 * Método implementado para cargar los grupos de una clase específica
	 * @param con
	 * @param cuentaForm
	 * @param usuario
	 * @param response 
	 * @param request 
	 * @return
	 */
	private ActionForward accionCargarGrupos(Connection con, CuentaInvUnidadFunForm cuentaForm, UsuarioBasico usuario, HttpServletRequest request, HttpServletResponse response) 
	{
		//Se toma informacion de la clase elegida
		if(cuentaForm.getClases("codigo_"+cuentaForm.getPosClase())!=null&&
			cuentaForm.getClases("nombre_"+cuentaForm.getPosClase())!=null)
		{
			cuentaForm.setClase(Integer.parseInt(cuentaForm.getClases("codigo_"+cuentaForm.getPosClase()).toString()));
			cuentaForm.setNombreClase(cuentaForm.getClases("nombre_"+cuentaForm.getPosClase()).toString());
		}
		
		//Se instancia objeto CuentaInvUnidadFun
		CuentaInvUnidadFun cuentaInv = new CuentaInvUnidadFun();
		
		//Se asigna la informacion
		cuentaInv.setClase(cuentaForm.getClase());
		cuentaInv.setUnidadFuncional(cuentaForm.getAcronimoUnidadFuncional());
		cuentaInv.setInstitucion(usuario.getCodigoInstitucionInt());
		
		cuentaForm.setGrupos(cuentaInv.consultarGrupos(con));
		cuentaForm.setNumGrupos(Integer.parseInt(cuentaForm.getGrupos("numRegistros").toString()));
		cuentaForm.setGruposHistorico(new HashMap(cuentaForm.getGrupos()));
		
		//Se inicializa el registro de ultima columan ordenada
		cuentaForm.setUltimoIndice("");
		
		UtilidadBD.closeConnection(con);
		cuentaForm.setOffset(0);
		return UtilidadSesion.redireccionar("grupoCtaInvUnidadFun.jsp",cuentaForm.getMaxPageItems(),cuentaForm.getNumGrupos(), response, request, "grupoCtaInvUnidadFun.jsp",false);
	}

	/**
	 * Método implementado para guardar las clases x unidad funcional
	 * @param con
	 * @param cuentaForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarClases(Connection con, CuentaInvUnidadFunForm cuentaForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		int resp = 0;
		
		//Se instancia mundo CuentaInvUnidaDfun
		CuentaInvUnidadFun cuentaInv = new CuentaInvUnidadFun();
		
		//Se inicia transacción
		UtilidadBD.iniciarTransaccion(con);
		
		//Se llena el mundo
		cuentaInv.setUnidadFuncional(cuentaForm.getAcronimoUnidadFuncional());
		cuentaInv.setInstitucion(usuario.getCodigoInstitucionInt());
		
		for(int i=0;i<cuentaForm.getNumClases();i++)
		{
			//Se toma la informacion
			cuentaInv.setCuentaCosto(Integer.parseInt(
				cuentaForm.getClases("codigoCuentaCosto_"+i).toString().equals("")?"0":cuentaForm.getClases("codigoCuentaCosto_"+i).toString()
				)
			);
			cuentaInv.setClase(Integer.parseInt(cuentaForm.getClases("codigo_"+i).toString()));
			
			//Se verifica si ya existía en la base de datos
			if(UtilidadTexto.getBoolean(cuentaForm.getClases("existeBd_"+i).toString()))
			{
				//**********SE REALIZA MODIFICACION*************************************
				resp = cuentaInv.modificarClase(con);
				
				if(resp>0)
					this.generarLog(cuentaForm,i,usuario,"clase");
				//***********************************************************************
			}
			else
			{
				//*********SE REALIZA INSERCIÓN******************************************
				resp = cuentaInv.insertarClase(con);
				//***********************************************************************
			}
			
			
			if(resp<=0)
				i = cuentaForm.getNumClases();
		}
		
		if(resp>0)
		{
			UtilidadBD.finalizarTransaccion(con);
			//se conservan datos de la forma-------------------------------------
			this.resetearForm(cuentaForm);
			
			cuentaForm.setEstado("guardarClases");
			return accionCargarClases(con, cuentaForm, mapping, usuario);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			ActionErrors errores = new ActionErrors();
			errores.add("Error procesando la informacion",new ActionMessage("errors.noSeGraboInformacion","DE CLASES INVENTARIO"));
			saveErrors(request, errores);
			cuentaForm.setEstado("cargarClases");
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaClase");
	}

	/**
	 * Método implementado para generar el log tipo archivo
	 * @param cuentaForm
	 * @param pos
	 * @param usuario
	 * @param tipo
	 */
	private void generarLog(CuentaInvUnidadFunForm cuentaForm, int pos, UsuarioBasico usuario, String tipo) 
	{
		HashMap historico = new HashMap();
		String log = "";
		boolean fueModificado = false;
		
		if(tipo.equals("clase"))
		{
			//Se toma información histórica*********************************************************************
			for(int i=0;i<Integer.parseInt(cuentaForm.getClasesHistorico().get("numRegistros").toString());i++)
			{
				if(cuentaForm.getClases("codigo_"+pos).toString().equals(cuentaForm.getClasesHistorico().get("codigo_"+i).toString()))
				{
					historico.put("codigo", cuentaForm.getClasesHistorico().get("codigo_"+i));
					historico.put("nombre", cuentaForm.getClasesHistorico().get("nombre_"+i));
					historico.put("codigoCuentaCosto", cuentaForm.getClasesHistorico().get("codigoCuentaCosto_"+i));
					historico.put("cuentaCosto", cuentaForm.getClasesHistorico().get("cuentaCosto_"+i));
				}
			}
			//****************************************************************************************************************
			
			//Se verifica si el registro fue modificado**************************************************************
			
			if(!historico.get("cuentaCosto").toString().equals(cuentaForm.getClases("cuentaCosto_"+pos).toString()))
			{
				fueModificado = true;
				
				//se edita el log*********************************************************************
				log = 		 "\n   ============INFORMACION ORIGINAL CLASE INVENTARIO=========== " +
				 "\n*  Unidad Funcional ["+cuentaForm.getNombreUnidadFuncional()+"] " +
				 "\n*  Clase ["+historico.get("nombre")+"] " +
				 "\n*  Cuenta de Costo ["+historico.get("cuentaCosto")+"] " +
				 "\n*  Institución ["+usuario.getInstitucion()+"] ";
				
				 log+="\n   ===========INFORMACION MODIFICADA CLASE INVENTARIO========== 	" +
				 "\n*  Unidad Funcional ["+cuentaForm.getNombreUnidadFuncional()+"] " +
				 "\n*  Clase [" +cuentaForm.getClases("nombre_"+pos)+""+"] "+
				 "\n*  Cuenta de Costo ["+cuentaForm.getClases("cuentaCosto_"+pos)+"] " +
				 "\n*  Institución ["+usuario.getInstitucion()+"] ";
				 
				 log+="\n========================================================\n\n\n ";
			}
		}
		else if (tipo.equals("grupo"))
		{
			//Se toma información histórica*********************************************************************
			for(int i=0;i<Integer.parseInt(cuentaForm.getGruposHistorico().get("numRegistros").toString());i++)
			{
				if(cuentaForm.getGrupos("codigo_"+pos).toString().equals(cuentaForm.getGruposHistorico().get("codigo_"+i).toString()))
				{
					historico.put("codigo", cuentaForm.getGruposHistorico().get("codigo_"+i));
					historico.put("nombre", cuentaForm.getGruposHistorico().get("nombre_"+i));
					historico.put("codigoCuentaCosto", cuentaForm.getGruposHistorico().get("codigoCuentaCosto_"+i));
					historico.put("cuentaCosto", cuentaForm.getGruposHistorico().get("cuentaCosto_"+i));
				}
			}
			//****************************************************************************************************************
			
			//Se verifica si el registro fue modificado**************************************************************
			
			if(!historico.get("cuentaCosto").toString().equals(cuentaForm.getGrupos("cuentaCosto_"+pos).toString()))
			{
				fueModificado = true;
				
				//se edita el log*********************************************************************
				log = 		 "\n   ============INFORMACION ORIGINAL GRUPO INVENTARIO=========== " +
				 "\n*  Unidad Funcional ["+cuentaForm.getNombreUnidadFuncional()+"] " +
				 "\n*  Clase ["+cuentaForm.getNombreClase()+"] " +
				 "\n*  Grupo ["+historico.get("nombre")+"] " +
				 "\n*  Cuenta de Costo ["+historico.get("cuentaCosto")+"] " +
				 "\n*  Institución ["+usuario.getInstitucion()+"] ";
				
				 log+="\n   ===========INFORMACION MODIFICADA GRUPO INVENTARIO========== 	" +
				 "\n*  Unidad Funcional ["+cuentaForm.getNombreUnidadFuncional()+"] " +
				 "\n*  Clase ["+cuentaForm.getNombreClase()+"] " +
				 "\n*  Grupo [" +cuentaForm.getGrupos("nombre_"+pos)+""+"] "+
				 "\n*  Cuenta de Costo ["+cuentaForm.getGrupos("cuentaCosto_"+pos)+"] " +
				 "\n*  Institución ["+usuario.getInstitucion()+"] ";
				 
				 log+="\n========================================================\n\n\n ";
			}
		}
		else if (tipo.equals("subgrupo"))
		{
			//Se toma información histórica*********************************************************************
			for(int i=0;i<Integer.parseInt(cuentaForm.getSubgruposHistorico().get("numRegistros").toString());i++)
			{
				if(cuentaForm.getSubgrupos("codigo_"+pos).toString().equals(cuentaForm.getSubgruposHistorico().get("codigo_"+i).toString()))
				{
					historico.put("codigo", cuentaForm.getSubgruposHistorico().get("codigo_"+i));
					historico.put("nombre", cuentaForm.getSubgruposHistorico().get("nombre_"+i));
					historico.put("codigoCuentaCosto", cuentaForm.getSubgruposHistorico().get("codigoCuentaCosto_"+i));
					historico.put("cuentaCosto", cuentaForm.getSubgruposHistorico().get("cuentaCosto_"+i));
				}
			}
			//****************************************************************************************************************
			
			//Se verifica si el registro fue modificado**************************************************************
			
			if(!historico.get("cuentaCosto").toString().equals(cuentaForm.getSubgrupos("cuentaCosto_"+pos).toString()))
			{
				fueModificado = true;
				
				//se edita el log*********************************************************************
				log = 		 "\n   ============INFORMACION ORIGINAL SUBGRUPO INVENTARIO=========== " +
				 "\n*  Unidad Funcional ["+cuentaForm.getNombreUnidadFuncional()+"] " +
				 "\n*  Clase ["+cuentaForm.getNombreClase()+"] " +
				 "\n*  Grupo ["+cuentaForm.getNombreGrupo()+"] " +
				 "\n*  Subgrupo ["+historico.get("nombre")+"] " +
				 "\n*  Cuenta de Costo ["+historico.get("cuentaCosto")+"] " +
				 "\n*  Institución ["+usuario.getInstitucion()+"] ";
				
				 log+="\n   ===========INFORMACION MODIFICADA SUBGRUPO INVENTARIO========== 	" +
				 "\n*  Unidad Funcional ["+cuentaForm.getNombreUnidadFuncional()+"] " +
				 "\n*  Clase ["+cuentaForm.getNombreClase()+"] " +
				 "\n*  Grupo [" +cuentaForm.getNombreGrupo()+""+"] "+
				 "\n*  Subgrupo [" +cuentaForm.getSubgrupos("nombre_"+pos)+""+"] "+
				 "\n*  Cuenta de Costo ["+cuentaForm.getSubgrupos("cuentaCosto_"+pos)+"] " +
				 "\n*  Institución ["+usuario.getInstitucion()+"] ";
				 
				 log+="\n========================================================\n\n\n ";
			}
		}
			
		
		if(fueModificado)
			LogsAxioma.enviarLog(ConstantesBD.logCuentasInventarioUnidadFuncionalCodigo,log,ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario());
		
	}

	/**
	 * Método implementado para efectuar la ordenacion de clases
	 * @param con
	 * @param cuentaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenarClases(Connection con, CuentaInvUnidadFunForm cuentaForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices = {
				"codigo_",
				"nombre_",
				"codigoCuentaCosto_",
				"cuentaCosto_",
				"existeBd_"
			};

		cuentaForm.setClases(Listado.ordenarMapa(indices,
				cuentaForm.getIndice(),
				cuentaForm.getUltimoIndice(),
				cuentaForm.getClases(),
				cuentaForm.getNumClases()));
		
		cuentaForm.setClases("numRegistros",cuentaForm.getNumClases()+"");
		
		cuentaForm.setUltimoIndice(cuentaForm.getIndice());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaClase");
	}

	/**
	 * Método implementado para efectuar la paginacion de los listados
	 * @param con
	 * @param cuentaForm
	 * @param response
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, CuentaInvUnidadFunForm cuentaForm, HttpServletResponse response) 
	{
		try 
		{
			UtilidadBD.closeConnection(con);
			response.sendRedirect(cuentaForm.getLinkSiguiente());
		} catch (IOException e) 
		{
			logger.error("Error realizando la redireccion del listado de registros:" +e);
		}
		return null;
	}

	/**
	 * Método implementado para cargar las clases de la unidad funcional seleccionada
	 * @param con
	 * @param cuentaForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCargarClases(Connection con, CuentaInvUnidadFunForm cuentaForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//Se instancia mundo
		CuentaInvUnidadFun cuentaInv = new CuentaInvUnidadFun();
		
		//Se carga el mundo
		cuentaInv.setUnidadFuncional(cuentaForm.getAcronimoUnidadFuncional());
		cuentaInv.setInstitucion(usuario.getCodigoInstitucionInt());
		
		//se consultan las clases
		cuentaForm.setClases(cuentaInv.consultarClases(con));
		
		cuentaForm.setNumClases(Integer.parseInt(cuentaForm.getClases("numRegistros").toString()));
		//se almacenan las clases iniciales como historico
		cuentaForm.setClasesHistorico(new HashMap(cuentaForm.getClases()));
		
		//Se inicializa el registro de ultima columan ordenada
		cuentaForm.setUltimoIndice("");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaClase");
	}

	/**
	 * Método implementado para iniciar el flujo
	 * @param con
	 * @param cuentaForm
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, CuentaInvUnidadFunForm cuentaForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		cuentaForm.reset();
		cuentaForm.setEstado("empezar");
		//Se toma el maxPageItems*****************************************************************
		try
		{
			cuentaForm.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
		}
		catch(Exception e)
		{
			logger.warn("El maxPageItems no está parametrizado: "+e);
			cuentaForm.setMaxPageItems(10);
		}
		//Se consultan las unidades funcionales****************************************************
		cuentaForm.setUnidadesFun(Utilidades.consultarUnidadesFuncionales(con, usuario.getCodigoInstitucionInt()));
		cuentaForm.setNumUnidadesFun(Integer.parseInt(cuentaForm.getUnidadesFun("numRegistros").toString()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaClase");
	}
}
