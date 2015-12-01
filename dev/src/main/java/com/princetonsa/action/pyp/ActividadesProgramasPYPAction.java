/*
 *@author Jorge Armando Osorio Velasquez. 
 */
package com.princetonsa.action.pyp;

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
import org.apache.struts.upload.FormFile;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;

import com.princetonsa.actionform.pyp.ActividadesProgramasPYPForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.pyp.ActividadesProgramasPYP;

/**
 * 
 * @author Jorge Armando Osorio Velasquez.
 *
 */
public class ActividadesProgramasPYPAction extends Action 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ActividadesProgramasPYPAction.class);
	
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
													        {
		Connection con = null;
		try{
			if(form instanceof ActividadesProgramasPYPForm)
			{


				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion(); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				ActividadesProgramasPYPForm forma=(ActividadesProgramasPYPForm)form;
				ActividadesProgramasPYP mundo=new ActividadesProgramasPYP();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				String estado = forma.getEstado();
				logger.warn("estado [ActividadesProgramasPYPAction.java]-->"+estado);
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptosCarteraAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			
				else if (estado.equals("empezar"))
				{
					forma.reset();
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarPrograma"))
				{
					if(!forma.getPrograma().trim().equals(""))
						this.accionCargarActivadesProgramasPYP(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("nuevo"))
				{
					this.accionCargarActivadesProgramasPYP(con,forma,mundo,usuario);
					forma.setActivo(true);
					forma.setPermitirEjecutar(false);
					forma.setRequerido(false);
					forma.setActividad("");
					forma.setTipoServicio("");
					forma.setNaturalezaServicio("");
					forma.setEsModificacion(false);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("detallePrograma");
				}
				else if(estado.equals("modificar"))
				{
					mundo.reset();
					mundo.consultarActivadProgramaPYP(con,forma.getPrograma().split(ConstantesBD.separadorSplit)[0],forma.getActividad(),usuario.getCodigoInstitucionInt());
					forma.reset();
					this.cargarForma(con,forma,mundo);
					UtilidadBD.cerrarConexion(con);
					forma.setEsModificacion(true);
					return mapping.findForward("detallePrograma");
				}	
				else if(estado.equals("eliminarDiagnostico"))
				{
					this.accionEliminarDiagnostico(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("detallePrograma");
				}
				else if(estado.equals("guardarActividad"))
				{
					forma.setInstitucion(usuario.getCodigoInstitucionInt());
					forma.setDescArchivo(forma.getArchivo().getFileName());
					//verirficar si ya existe la relacion programa - actividad
					if(UtilidadValidacion.existeRelacionProgramaActividadPYP(con,usuario.getCodigoInstitucionInt(),forma.getPrograma().split(ConstantesBD.separadorSplit)[0],forma.getActividad()))
					{
						forma.setEsModificacion(false);
						ActionErrors errores=new ActionErrors();
						errores.add("ACTIVIDAD REQUERIDO", new ActionMessage("error.pyp.actividadesProgramaPYP.ActividadYaParametrizada",forma.getPrograma().split(ConstantesBD.separadorSplit)[1]));
						saveErrors(request,errores);
						forma.setEstado("nuevo");
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("detallePrograma");
					}

					//continuar con el proceso de insercion
					this.cargarMundo(forma,mundo);
					boolean exito=mundo.insertarActividadPrograma(con);
					if(!forma.getArchivo().getFileName().trim().equals(""))
						UtilidadFileUpload.guadarArchivo((FormFile)forma.getArchivo());
					mundo.reset();
					mundo.consultarActivadProgramaPYP(con,forma.getPrograma().split(ConstantesBD.separadorSplit)[0],forma.getActividad(),usuario.getCodigoInstitucionInt());
					forma.reset();
					this.cargarForma(con,forma,mundo);
					forma.setEstado("modificar");
					UtilidadBD.cerrarConexion(con);
					forma.setEsModificacion(true);
					if(exito)
						forma.setProcesoExitoso(ConstantesBD.acronimoSi);
					return mapping.findForward("detallePrograma");
				}
				else if(estado.equals("modificarActividad"))
				{
					mundo.reset();
					mundo.consultarActivadProgramaPYP(con,forma.getPrograma().split(ConstantesBD.separadorSplit)[0],forma.getActividad(),usuario.getCodigoInstitucionInt());
					this.generarLog(forma,mundo,usuario,false,1, 0);
					forma.setInstitucion(usuario.getCodigoInstitucionInt());
					if(!forma.getArchivo().getFileName().trim().equals(""))
						forma.setDescArchivo(forma.getArchivo().getFileName());

					this.cargarMundo(forma,mundo);
					boolean exito=mundo.modifcarActividadPrograma(con);
					if(!forma.getArchivo().getFileName().trim().equals(""))
						UtilidadFileUpload.guadarArchivo((FormFile)forma.getArchivo());
					mundo.reset();
					mundo.consultarActivadProgramaPYP(con,forma.getPrograma().split(ConstantesBD.separadorSplit)[0],forma.getActividad(),usuario.getCodigoInstitucionInt());
					forma.reset();
					this.cargarForma(con,forma,mundo);
					forma.setEstado("modificar");
					UtilidadBD.cerrarConexion(con);
					forma.setEsModificacion(true);
					if(exito)
						forma.setProcesoExitoso(ConstantesBD.acronimoSi);
					return mapping.findForward("detallePrograma");
				}
				else if(estado.equals("eliminarActividad"))
				{
					this.generarLog(forma,mundo,usuario,true,1, 0);
					boolean exito=mundo.eliminarActividad(con,forma.getCodigo());
					forma.reset();
					mundo.reset();
					forma.setEsModificacion(false);
					UtilidadBD.cerrarConexion(con);
					if(exito)
						forma.setProcesoExitoso(ConstantesBD.acronimoSi);
					return mapping.findForward("principal");
				}
				else if(estado.equals("empezarViasIngreso"))
				{
					forma.setEsModificacion(true);
					mundo.cargarViasIngreso(con,forma.getCodigo());
					forma.setViasIngreso((HashMap)mundo.getViasIngreso());
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaViaIngreso");
				}
				else if(estado.equals("nuevaViaIngreso"))
				{
					forma.setEsModificacion(true);
					this.nuevaViaIngreso(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaViaIngreso");
				}
				else if(estado.equals("eliminarViaIngreso"))
				{
					forma.setEsModificacion(true);
					this.eliminarViaIngreso(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaViaIngreso");
				}
				else if(estado.equals("guardarViaIngreso"))
				{
					forma.setEsModificacion(true);
					this.accionGuardarViasIngreso(con,forma,mundo,usuario);
					forma.resetMapas();
					mundo.setViasIngreso(new HashMap());
					mundo.cargarViasIngreso(con,forma.getCodigo());
					forma.setViasIngreso((HashMap)mundo.getViasIngreso());
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaViaIngreso");
				}
				else if(estado.equals("empezarGuposEtareos"))
				{
					forma.setEsModificacion(true);
					if(!forma.getRegimenGrupoEtareo().trim().equals(""))
					{
						mundo.cargarGruposEtareos(con,forma.getCodigo(),forma.getRegimenGrupoEtareo());
						forma.setGruposEtareos((HashMap)mundo.getGruposEtareos());
					}
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaGruposEtareos");
				}
				else if(estado.equals("nuevoGrupoEtareo"))
				{
					forma.setEsModificacion(true);
					this.nuevGrupoEtareo(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaGruposEtareos");
				}
				else if(estado.equals("eliminarGrupoEtareo"))
				{
					forma.setEsModificacion(true);
					this.eliminarGrupoEtareo(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaGruposEtareos");
				}
				else if(estado.equals("guardarGrupoEtareo"))
				{
					this.accionGuardarGruposEtareos(con,forma,mundo,usuario);
					forma.resetMapas();
					mundo.setGruposEtareos(new HashMap());
					mundo.cargarGruposEtareos(con,forma.getCodigo(),forma.getRegimenGrupoEtareo());
					forma.setGruposEtareos((HashMap)mundo.getGruposEtareos());
					UtilidadBD.cerrarConexion(con);
					//forma.setRegimenGrupoEtareo("");
					forma.setEsModificacion(true);
					return mapping.findForward("paginaGruposEtareos");
				}
				else if(estado.equals("empezarMetas"))
				{
					forma.setEsModificacion(true);
					mundo.cargarMetas(con,forma.getCodigo());
					forma.setMetas((HashMap)mundo.getMetas());
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaMetasCumplimiento");
				}
				else if(estado.equals("nuevaMeta"))
				{
					forma.setEsModificacion(true);
					this.nuevaMeta(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaMetasCumplimiento");
				}
				else if(estado.equals("eliminarMeta"))
				{
					forma.setEsModificacion(true);
					this.eliminarMeta(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaMetasCumplimiento");
				}
				else if(estado.equals("guardarMeta"))
				{
					this.accionGuardarMeta(con,forma,mundo,usuario);
					forma.resetMapas();
					mundo.setMetas(new HashMap());
					mundo.cargarMetas(con,forma.getCodigo());
					forma.setMetas((HashMap)mundo.getMetas());
					UtilidadBD.cerrarConexion(con);
					//forma.setRegimenGrupoEtareo("");
					forma.setEsModificacion(true);
					return mapping.findForward("paginaMetasCumplimiento");
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			logger.error("El form no es compatible con el form de ProgramasSaludPYPForm");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
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
	private void accionEliminarDiagnostico(ActividadesProgramasPYPForm forma) 
	{
		forma.setDiagnosticos("eliminado_"+forma.getDiagEliminar(),"true");
	}

	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param esEliminacion
	 * @param tipoLogGenerar Tipo de log: 1-actividades 2-viasingreso 3-gruposetareos 4-metas
	 * @param posMapa solo se tiene en cuenta cuando el cambio se hace sobre registros de mapas.
	 */
	private void generarLog(ActividadesProgramasPYPForm forma, ActividadesProgramasPYP mundo, UsuarioBasico usuario, boolean esEliminacion, int tipoLogGenerar, int posMapa) 
	{
		String log = "";
		int tipoLog=0;
		if(tipoLogGenerar==1)
		{
			if(esEliminacion)
			{
				log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
				 "\n*  Programa [" +forma.getPrograma()+"] "+
				 "\n*  Actividad ["+forma.getActividad()+"] " +
				 "\n*  Finalidad ["+(forma.getFinalidadConsulta().trim().equals("")?forma.getFinalidadServicio():forma.getFinalidadServicio())+"] " +
				 "\n*  Embarazo ["+forma.isEmbarazo()+"] "+
				 "\n*  Semanas Gestacion ["+forma.getSemanasGestacion()+"] " +
				 "\n*  Requerido ["+forma.isRequerido()+"] " +
				 "\n*  Permitir Ejecutar ["+forma.isPermitirEjecutar()+"] " +
				 "\n*  Activo ["+forma.isActivo()+"] " +
				 "\n*  Archivo ["+forma.getDescArchivo()+"] "+
				 "\n========================================================\n\n\n ";
				tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
				 
			}
			else
			{
				log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
				 "\n*  Programa [" +forma.getPrograma()+"] "+
				 "\n*  Actividad ["+forma.getActividad()+"] " +
				 "\n*  Finalidad ["+(forma.getFinalidadConsulta().trim().equals("")?forma.getFinalidadServicio():forma.getFinalidadServicio())+"] " +
				 "\n*  Embarazo ["+forma.isEmbarazo()+"] "+
				 "\n*  Semanas Gestacion ["+forma.getSemanasGestacion()+"] " +
				 "\n*  Requerido ["+forma.isRequerido()+"] " +
				 "\n*  Permitir Ejecutar ["+forma.isPermitirEjecutar()+"] " +
				 "\n*  Activo ["+forma.isActivo()+"] " +
				 "\n*  Archivo ["+forma.getDescArchivo()+"] "+
				 "\n   ===========INFORMACION MODIFICADA========== 	" +
				 "\n*  Programa [" +mundo.getPrograma()+"] "+
				 "\n*  Finalidad ["+(mundo.getFinalidadConsulta().trim().equals("")?mundo.getFinalidadServicio():mundo.getFinalidadServicio())+"] " +
				 "\n*  Actividad ["+mundo.getActividad()+"] " +
				 "\n*  Embarazo ["+mundo.isEmbarazo()+"] "+
				 "\n*  Semanas Gestacion ["+mundo.getSemanasGestacion()+"] " +
				 "\n*  Requerido ["+mundo.isRequerido()+"] " +
				 "\n*  Activo ["+mundo.isActivo()+"] " +
				 "\n*  Archivo ["+mundo.getDescArchivo()+"] "+
				 "\n========================================================\n\n\n ";
				tipoLog=ConstantesBD.tipoRegistroLogModificacion;
			}
		}
		if(tipoLogGenerar==2)
		{
			if(esEliminacion)
			{
				log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
				 "\n*  Programa [" +forma.getPrograma()+"] "+
				 "\n*  Actividad ["+forma.getActividad()+"] " +
				 "\n*  Via Ingreso [" +forma.getViasIngreso("viaingreso_"+posMapa)+"] "+
				 "\n*  Ocupacion ["+forma.getViasIngreso("ocupacion_"+posMapa)+"] " +
				 "\n*  solicitar [" +forma.getViasIngreso("solicitar_"+posMapa)+"] "+
				 "\n*  Programar ["+forma.getViasIngreso("programar_"+posMapa)+"] " +
				 "\n*  Ejecutar ["+forma.getViasIngreso("ejecutar_"+posMapa)+"] "+
				 "\n========================================================\n\n\n ";
				tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
				
			}
			else
			{
				log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
				 "\n   ===========INFORMACION MODIFICADA========== 	" +
				 "\n*  Programa [" +forma.getPrograma()+"] "+
				 "\n*  Actividad ["+forma.getActividad()+"] " +
				 "\n*  Via Ingreso [" +forma.getViasIngreso("viaingreso_"+posMapa)+"] "+
				 "\n*  Ocupacion ["+forma.getViasIngreso("ocupacion_"+posMapa)+"] " +
				 "\n*  solicitar [" +forma.getViasIngreso("solicitar_"+posMapa)+"] "+
				 "\n*  Programar ["+forma.getViasIngreso("programar_"+posMapa)+"] " +
				 "\n*  Ejecutar ["+forma.getViasIngreso("ejecutar_"+posMapa)+"] "+
				 "\n========================================================\n\n\n ";
				tipoLog=ConstantesBD.tipoRegistroLogModificacion;
			}
		}
		if(tipoLogGenerar==3)
		{
			if(esEliminacion)
			{
				log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
				 "\n*  Programa [" +forma.getPrograma()+"] "+
				 "\n*  Actividad ["+forma.getActividad()+"] " +
				 "\n*  Regimen [" +forma.getRegimenGrupoEtareo()+"] "+
				 "\n*  Grupo Etareo ["+forma.getGruposEtareos("grupoetareo_"+posMapa)+"] " +
				 "\n*  Frecuencia [" +forma.getGruposEtareos("frecuencia_"+posMapa)+"] "+
				 "\n========================================================\n\n\n ";
				tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
				
			}
			else
			{
				log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
				 "\n   ===========INFORMACION MODIFICADA========== 	" +
				 "\n*  Programa [" +forma.getPrograma()+"] "+
				 "\n*  Actividad ["+forma.getActividad()+"] " +
				 "\n*  Regimen [" +forma.getRegimenGrupoEtareo()+"] "+
				 "\n*  Grupo Etareo ["+forma.getGruposEtareos("grupoetareo_"+posMapa)+"] " +
				 "\n*  Frecuencia [" +forma.getGruposEtareos("frecuencia_"+posMapa)+"] "+
				 "\n========================================================\n\n\n ";
				tipoLog=ConstantesBD.tipoRegistroLogModificacion;
			}
		}
		if(tipoLogGenerar==4)
		{
			if(esEliminacion)
			{
				log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
				 "\n*  Programa [" +forma.getPrograma()+"] "+
				 "\n*  Actividad ["+forma.getActividad()+"] " +
				 "\n*  Regimen [" +forma.getMetas("regimen_"+posMapa)+"] "+
				 "\n*  Grupo Etareo ["+forma.getMetas("meta_"+posMapa)+"] " +
				 "\n========================================================\n\n\n ";
				tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
				
			}
			else
			{
				log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
				 "\n   ===========INFORMACION MODIFICADA========== 	" +
				 "\n*  Programa [" +forma.getPrograma()+"] "+
				 "\n*  Actividad ["+forma.getActividad()+"] " +
				 "\n*  Regimen [" +forma.getMetas("regimen_"+posMapa)+"] "+
				 "\n*  Grupo Etareo ["+forma.getMetas("meta_"+posMapa)+"] " +
				 "\n========================================================\n\n\n ";
				tipoLog=ConstantesBD.tipoRegistroLogModificacion;
			}
		}
		LogsAxioma.enviarLog(ConstantesBD.logCajasCodigo,log,tipoLog,usuario.getLoginUsuario());

	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarMeta(Connection con, ActividadesProgramasPYPForm forma, ActividadesProgramasPYP mundo, UsuarioBasico usuario) 
	{
		int numReg=Integer.parseInt(forma.getMetas().get("numRegistros")+"");
		int regEliminados=Integer.parseInt(forma.getMetasEliminados().get("numRegistros")+"");
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		//se encuentra entre la transaccion
		{
			//eliminar.
			for(int el=0;el<regEliminados;el++)
			{
				if(enTransaccion)
				{
					enTransaccion=mundo.eliminarRegistroMeta(con,forma.getCodigo().split(ConstantesBD.separadorSplit)[0],forma.getMetasEliminados("regimen_"+el)+"");
					//this.generarLog(forma,usuario,true, el);
				}
				else
				{
					logger.error("ERROR ELIMINANDO META");
					el=regEliminados;
				}
			}
			//insertar modificar.
	        for(int k=0;k<numReg;k++)
	        {
	        	if(enTransaccion)
	        	{
		        	//modificar
		        	if((forma.getMetas("tiporegistro_"+k)+"").equalsIgnoreCase("BD")&&enTransaccion)
		        	{
		        		if(registroModificadoMetas(con,forma,mundo,k))
		        		{
		        			enTransaccion=mundo.modificarRegistroMetas(con,forma.getCodigo().split(ConstantesBD.separadorSplit)[0],forma.getMetas("regimen_"+k)+"",forma.getMetas("meta_"+k)+"");
		        			//this.generarLog(forma,usuario,false, k);
		        		}
		        	}
		        	//insertar
		        	if((forma.getMetas("tiporegistro_"+k)+"").equalsIgnoreCase("MEM")&& enTransaccion)
		        	{
		        		enTransaccion=mundo.insertarRegistroMeta(con,forma.getCodigo().split(ConstantesBD.separadorSplit)[0],forma.getMetas("regimen_"+k)+"",forma.getMetas("meta_"+k)+"");
		        	}
	        	}
	        	else
	        	{
	        		logger.error("error modificando - insertando  METAS");
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

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param k
	 * @return
	 */
	private boolean registroModificadoMetas(Connection con, ActividadesProgramasPYPForm forma, ActividadesProgramasPYP mundo, int k) 
	{
		return mundo.existeModificacionMetas(con,forma.getCodigo().split(ConstantesBD.separadorSplit)[0],forma.getMetas("regimen_"+k)+"",forma.getMetas("meta_"+k)+"");
	}

	/**
	 * 
	 * @param forma
	 */
	private void eliminarMeta(ActividadesProgramasPYPForm forma) 
	{
		int pos=forma.getRegEliminar();
		int numReg=Integer.parseInt(forma.getMetas().get("numRegistros")+"");
		int ultimoRegistro=numReg-1;
		int regEliminados=Integer.parseInt(forma.getMetasEliminados().get("numRegistros")+"");
		String indices[]={"regimen_","meta_","tiporegistro_"};
		if((forma.getMetas("tiporegistro_"+pos)+"").equalsIgnoreCase("BD"))
		{
			for(int ind=0;ind<indices.length;ind++)
			{
				forma.setMetasEliminados(indices[ind]+""+regEliminados,forma.getMetas(indices[ind]+""+pos));
			}
			forma.setMetasEliminados("numRegistros",(regEliminados+1)+"");
		}
		for(int i=pos;i<ultimoRegistro;i++)
		{
			for(int ind=0;ind<indices.length;ind++)
			{
				forma.setMetas(indices[ind]+""+i,forma.getMetas(indices[ind]+""+(i+1)));
			}
		}
		for(int ind=0;ind<indices.length;ind++)
		{
			forma.getMetas().remove(indices[ind]+""+ultimoRegistro);
		}
		forma.setMetas("numRegistros",ultimoRegistro);
	}

	/**
	 * 
	 * @param forma
	 */
	private void nuevaMeta(ActividadesProgramasPYPForm forma) 
	{
		int numReg=Integer.parseInt(forma.getMetas().get("numRegistros")+"");
		forma.setMetas("regimen_"+numReg,"");
		forma.setMetas("meta_"+numReg,"");
		forma.setMetas("tiporegistro_"+numReg,"MEM");
		forma.setMetas("numRegistros",(numReg+1)+"");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarGruposEtareos(Connection con, ActividadesProgramasPYPForm forma, ActividadesProgramasPYP mundo, UsuarioBasico usuario) 
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
					enTransaccion=mundo.eliminarRegistroGrupoEtareo(con,forma.getCodigo().split(ConstantesBD.separadorSplit)[0],forma.getRegimenGrupoEtareo(),forma.getGruposEtareosEliminados("grupoetareo_"+el)+"");
					//this.generarLog(forma,usuario,true, el);
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
		        		if(registroModificadoGrupoEtareo(con,forma,mundo,k))
		        		{
		        			enTransaccion=mundo.modificarRegistroGrupoEtareo(con,forma.getCodigo().split(ConstantesBD.separadorSplit)[0],forma.getRegimenGrupoEtareo(),forma.getGruposEtareos("grupoetareo_"+k)+"",forma.getGruposEtareos("frecuencia_"+k)+"",forma.getGruposEtareos("tipofrecuencia_"+k)+"");
		        			//this.generarLog(forma,usuario,false, k);
		        		}
		        	}
		        	//insertar
		        	if((forma.getGruposEtareos("tiporegistro_"+k)+"").equalsIgnoreCase("MEM")&& enTransaccion)
		        	{
		        		enTransaccion=mundo.insertarRegistroGrupoEtareo(con,forma.getCodigo().split(ConstantesBD.separadorSplit)[0],forma.getRegimenGrupoEtareo(),forma.getGruposEtareos("grupoetareo_"+k)+"",forma.getGruposEtareos("frecuencia_"+k)+"",forma.getGruposEtareos("tipofrecuencia_"+k)+"");
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

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param k
	 * @return
	 */
	private boolean registroModificadoGrupoEtareo(Connection con, ActividadesProgramasPYPForm forma, ActividadesProgramasPYP mundo,int pos) 
	{
		return mundo.existeModificacionGrupoEtareo(con,forma.getCodigo().split(ConstantesBD.separadorSplit)[0],forma.getRegimenGrupoEtareo(),forma.getGruposEtareos("grupoetareo_"+pos)+"",forma.getGruposEtareos("frecuencia_"+pos)+"",forma.getGruposEtareos("tipofrecuencia_"+pos)+"");
	}


	/**
	 * 
	 * @param forma
	 */
	private void eliminarGrupoEtareo(ActividadesProgramasPYPForm forma) 
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

	/**
	 * 
	 * @param forma
	 */
	private void nuevGrupoEtareo(ActividadesProgramasPYPForm forma) 
	{
		int numReg=Integer.parseInt(forma.getGruposEtareos().get("numRegistros")+"");
		forma.setGruposEtareos("grupoetareo_"+numReg,"");
		forma.setGruposEtareos("frecuencia_"+numReg,"");
		forma.setGruposEtareos("tiporegistro_"+numReg,"MEM");
		forma.setGruposEtareos("numRegistros",(numReg+1)+"");
		
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarViasIngreso(Connection con, ActividadesProgramasPYPForm forma, ActividadesProgramasPYP mundo, UsuarioBasico usuario) 
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
					enTransaccion=mundo.eliminarRegistroViaIngreso(con,forma.getCodigo().split(ConstantesBD.separadorSplit)[0],forma.getViasIngresoEliminados("viaingreso_"+el)+"",forma.getViasIngresoEliminados("ocupacion_"+el)+"");
					this.generarLog(forma,mundo,usuario,true,2, el);
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
		        		if(registroModificadoViaIngreso(con,forma,mundo,k))
		        		{
		        			enTransaccion=mundo.modificarRegistroViaIngreso(con,forma.getCodigo().split(ConstantesBD.separadorSplit)[0],forma.getViasIngreso("viaingreso_"+k)+"",forma.getViasIngreso("ocupacion_"+k)+"",UtilidadTexto.getBoolean(forma.getViasIngreso("solicitar_"+k)+""),UtilidadTexto.getBoolean(forma.getViasIngreso("programar_"+k)+""),UtilidadTexto.getBoolean(forma.getViasIngreso("ejecutar_"+k)+""));
							this.generarLog(forma,mundo,usuario,false,2, k);
		        		}
		        	}
		        	//insertar
		        	if((forma.getViasIngreso("tiporegistro_"+k)+"").equalsIgnoreCase("MEM")&& enTransaccion)
		        	{
		        		enTransaccion=mundo.insertarRegistroViaIngreso(con,forma.getCodigo().split(ConstantesBD.separadorSplit)[0],forma.getViasIngreso("viaingreso_"+k)+"",forma.getViasIngreso("ocupacion_"+k)+"",UtilidadTexto.getBoolean(forma.getViasIngreso("solicitar_"+k)+""),UtilidadTexto.getBoolean(forma.getViasIngreso("programar_"+k)+""),UtilidadTexto.getBoolean(forma.getViasIngreso("ejecutar_"+k)+""));
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

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param k
	 * @return
	 */
	private boolean registroModificadoViaIngreso(Connection con, ActividadesProgramasPYPForm forma, ActividadesProgramasPYP mundo,int pos) 
	{
		return mundo.existeModificacionViaIngreso(con,forma.getCodigo().split(ConstantesBD.separadorSplit)[0],forma.getViasIngreso("viaingreso_"+pos)+"",forma.getViasIngreso("ocupacion_"+pos)+"",UtilidadTexto.getBoolean(forma.getViasIngreso("solicitar_"+pos)+""),UtilidadTexto.getBoolean(forma.getViasIngreso("programar_"+pos)+""),UtilidadTexto.getBoolean(forma.getViasIngreso("ejecutar_"+pos)+""));
	}

	/**
	 * 
	 * @param forma
	 */
	private void eliminarViaIngreso(ActividadesProgramasPYPForm forma) 
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

	/**
	 * 
	 * @param forma
	 */
	private void nuevaViaIngreso(ActividadesProgramasPYPForm forma) 
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
	 * 
	 * @param forma
	 * @param mundo
	 */
	private void cargarForma(Connection con,ActividadesProgramasPYPForm forma, ActividadesProgramasPYP mundo) 
	{
		forma.setCodigo(mundo.getCodigo());
		forma.setInstitucion(mundo.getInstitucion());
		forma.setPrograma(mundo.getPrograma());
		forma.setActividad(mundo.getActividad());
		forma.setEmbarazo(mundo.isEmbarazo());
		forma.setSemanasGestacion(mundo.getSemanasGestacion());
		forma.setRequerido(mundo.isRequerido());
		forma.setPermitirEjecutar(mundo.isPermitirEjecutar());
		forma.setActivo(mundo.isActivo());
		forma.setDescArchivo(mundo.getDescArchivo());
		forma.setDiagnosticos(mundo.getDiagnosticos());
		forma.setFinalidadConsulta(mundo.getFinalidadConsulta());
		forma.setFinalidadServicio(mundo.getFinalidadServicio());
		forma.setTipoServicio(Utilidades.obtenerTipoServicio(con, Utilidades.obtenerArtSerActividadPYP(con,mundo.getActividad())));
		forma.setNaturalezaServicio(Utilidades.obtenerNaturalezaServicio(con,mundo.getServicioActividad()));
	}

	/**
	 * 
	 * @param forma
	 * @param mundo
	 */
	private void cargarMundo(ActividadesProgramasPYPForm forma, ActividadesProgramasPYP mundo) 
	{
		mundo.reset();
		mundo.setCodigo(forma.getCodigo());
		mundo.setInstitucion(forma.getInstitucion());
		mundo.setPrograma(forma.getPrograma());
		mundo.setActividad(forma.getActividad());
		mundo.setEmbarazo(forma.isEmbarazo());
		mundo.setSemanasGestacion(forma.getSemanasGestacion());
		mundo.setRequerido(forma.isRequerido());
		mundo.setActivo(forma.isActivo());
		mundo.setDescArchivo(forma.getDescArchivo());
		mundo.setDiagnosticos(forma.getDiagnosticos());
		mundo.setFinalidadConsulta(forma.getFinalidadConsulta());
		mundo.setFinalidadServicio(forma.getFinalidadServicio());
		mundo.setPermitirEjecutar(forma.isPermitirEjecutar());
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario 
	 */
	private void accionCargarActivadesProgramasPYP(Connection con, ActividadesProgramasPYPForm forma, ActividadesProgramasPYP mundo, UsuarioBasico usuario) 
	{
		mundo.consultarActivadesProgramasPYP(con,forma.getPrograma().split(ConstantesBD.separadorSplit)[0],usuario.getCodigoInstitucion());
		int numero=Utilidades.convertirAEntero(mundo.getActProgPYP().get("numRegistros")+"");
		
		for(int i=0;i<numero;i++)
		{
			mundo.getActProgPYP().put("numero_"+i,i+1);
		}
		
		forma.setActProgPYP((HashMap)mundo.getActProgPYP().clone());
	}

}
