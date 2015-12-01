package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
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
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.facturacion.CentrosCostoEntidadesSubcontratadasForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.CentrosCostoEntidadesSubcontratadas;
import com.servinte.axioma.dto.facturacion.EntidadSubContratadaDto;

/**
 * 
 * @author Angela María Angel amangel@axioma-md.com
 *
 */
@SuppressWarnings("unchecked")
public class CentrosCostoEntidadesSubcontratadasAction extends Action{
	
	private Logger logger = Logger.getLogger(CentrosCostoEntidadesSubcontratadasAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{	
		Connection con = null;
		try{
			if(form instanceof CentrosCostoEntidadesSubcontratadasForm)
			{	


				//se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//se obtiene el paciente cargado en sesion.
				//PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				//se obtiene la institucion
				//InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				//se instancia la forma
				CentrosCostoEntidadesSubcontratadasForm forma = (CentrosCostoEntidadesSubcontratadasForm)form;		

				//se instancia el mundo
				CentrosCostoEntidadesSubcontratadas mundo = new CentrosCostoEntidadesSubcontratadas();

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				//se instancia la variable para manejar los errores.
				//ActionErrors errores=new ActionErrors();

				forma.setMensaje(new ResultadoBoolean(false));

				//se instancia la variable para manejar los errores.


				logger.info("\n\n***************************************************************************");
				logger.info(" 	  EL ESTADO DE CENTROS COSTO ENTIDADES SUBCONTRATADAS ES ====>> "+estado);
				logger.info("\n***************************************************************************");

				// ESTADO --> NULL
				if(estado == null)
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");

					return mapping.findForward("paginaError");
				}			
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(con, forma, mundo, mapping, usuario,request);					   			
				}	
				else if(estado.equals("empezarConsulta"))
				{
					UtilidadBD.cerrarConexion(con);
					return this.accionEmpezarConsulta(forma, mundo, mapping, usuario,request);					   			
				}				
				else if(estado.equals("guardarNuevo"))
				{
					UtilidadBD.cerrarConexion(con);
					return this.accionGuardarNuevo(forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("buscarRegistros"))
				{
					return this.accionBuscarRegistros(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("buscarRegistrosConsulta"))
				{
					return this.accionBuscarRegistrosConsulta(con, forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("nuevoRegistro"))
				{				
					UtilidadBD.cerrarConexion(con);
					forma.resetNuevo(usuario.getCodigoInstitucionInt());
					return mapping.findForward("principal");			
				}
				else if(estado.equals("modificarRegistro"))
				{
					UtilidadBD.cerrarConexion(con);
					return this.accionModificarRegistro(forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("guardarModificar"))
				{
					UtilidadBD.cerrarConexion(con);
					return this.accionGuardarModificar(forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("eliminarRegistro"))
				{
					UtilidadBD.cerrarConexion(con);
					return this.accionEliminar(forma, mundo, mapping, usuario,request);
				}
				else if (estado.equals("ordenar"))
				{
					UtilidadBD.cerrarConexion(con);
					accionOrdenarMapa(forma, mundo);
					return mapping.findForward("principal");
				}
				else if (estado.equals("ordenarCon"))
				{
					UtilidadBD.cerrarConexion(con);
					accionOrdenarCon(forma, mundo);
					return mapping.findForward("principalconsulta");
				}
				else if (estado.equals("buscarEntidadPorCentroCosto"))
				{
					return accionBuscarEntidadPorCentroCosto(forma, mapping, mundo, request, usuario);
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
	 * Metodo que ordena las columnas
	 * @return
	 */
	private void accionOrdenarCon(CentrosCostoEntidadesSubcontratadasForm forma, CentrosCostoEntidadesSubcontratadas mundo)
	{
		String[] indices = mundo.indicesCentrosCostoEntiSub;

		int numReg = Utilidades.convertirAEntero(forma.getCentrosCostoEntiSub("numRegistros")+"");
		if (!forma.getCentrosCostoEntiSub().get("numRegistros").equals("0"))
		{
			logger.info("\n\n\n********IMPRIMO EL MAPA*******"+forma.getCentrosCostoEntiSub());
			forma.setCentrosCostoEntiSub(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getCentrosCostoEntiSub(), numReg));
			forma.setUltimoPatron(forma.getPatronOrdenar());
			forma.setCentrosCostoEntiSub("numRegistros",numReg+"");
		}
		else
		{
			forma.setEstado("buscarRegistros");
		}
	}

	/**
	 * Metodo que ordena las columnas
	 * @return
	 */
	private void accionOrdenarMapa(CentrosCostoEntidadesSubcontratadasForm forma, CentrosCostoEntidadesSubcontratadas mundo)
	{
		String[] indices = mundo.indicesCentrosCostoEntiSub;

		int numReg = Utilidades.convertirAEntero(forma.getCentrosCostoEntiSub("numRegistros")+"");
		if (!forma.getCentrosCostoEntiSub().get("numRegistros").equals("0"))
		{
			logger.info("\n\n\n********IMPRIMO EL MAPA*******"+forma.getCentrosCostoEntiSub());
			forma.setCentrosCostoEntiSub(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getCentrosCostoEntiSub(), numReg));
			forma.setUltimoPatron(forma.getPatronOrdenar());
			forma.setCentrosCostoEntiSub("numRegistros",numReg+"");
		}
		else
		{
			forma.setEstado("buscarRegistros");
		}
	}

	/**
	 * Metodo para eliminar un registro de centro de costo por entidades subcontratadas
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEliminar(CentrosCostoEntidadesSubcontratadasForm forma, CentrosCostoEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{
		boolean elimino=false;
				
		elimino=mundo.eliminarCentroCostoEntiSub(Utilidades.convertirAEntero(forma.getCentrosCostoEntiSub("consecutivo_"+forma.getIndiceModificar())+""));
				
		if(elimino)
		{
			int i= forma.getIndiceModificar();
			
			if(mundo.guardarLogCentrosCostoEntiSub(forma.getCentrosCostoEntiSub("centroCosto_"+i)+"", forma.getCentrosCostoEntiSub("entidadSubcontratada_"+i)+"", "S", "", "", "", "", usuario))
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			else
				forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
		}
		else
			forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
		
		forma.setCentrosCostoEntiSub(mundo.consultarCentrosCostoEntiSub(Utilidades.convertirAEntero(forma.getCentroAtencionSel()+"")));
			
		return mapping.findForward("principal");
	}
	
	/**
	 * Metodo que modifica la informacion de un registro de  centro de costo por entidades subcontratadas
	 * @param forma
     * @param con
     * @param mapping
     * @return
	 */
	private ActionForward accionGuardarModificar(CentrosCostoEntidadesSubcontratadasForm forma, CentrosCostoEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{		
		ActionErrors errores = new ActionErrors();
		boolean prioridad=false;
		
		forma.setGuardo(false);
		
		for(int j=0;j<(Utilidades.convertirAEntero(forma.getCentrosCostoEntiSub("numRegistros")+""));j++)
		{	
			//Se valida si el centro de costo es de tipo area= SubAlmacen y si ya tiene una entidad subcontratada asociada si es asi no permite asociar una nueva
			if(Utilidades.convertirAEntero(forma.getCentrosCostoEntiSub("centroCosto_"+j)+"") == (forma.getCentroCostoSel()) && forma.getIndiceModificar() != j)
			{	
				//se valida que la prioridad por cada centro de atencion/centro de costo/entidad subcontratada sea secuencial aunmentando en uno
				if(Utilidades.convertirAEntero(forma.getCentrosCostoEntiSub("entidadSubcontratada_"+j)+"") == (forma.getEntidadSubcontratadaSel()) && forma.getIndiceModificar() != j)
				{
					if(Utilidades.convertirAEntero(forma.getCentrosCostoEntiSub("nroPrioridad_"+j)+"") == Utilidades.convertirAEntero(forma.getPrioridad()) && forma.getIndiceModificar() != j)
					{
						prioridad=true;
					}
				}	
			}
		}		
		
		if(prioridad)
			errores.add("descripcion",new ActionMessage("prompt.generico","La prioridad para el Centro de Costo  y la Entidad Subcontratada ya fue Asignada."));
		
	
		if(!errores.isEmpty())	
		{
			forma.setEstado("modificarRegistro");
			saveErrors(request,errores);
		}
		else
		{	
			forma.setGuardo(mundo.actualizarCentroCostoEntiSub(forma.getPrioridad(), forma.getRespOtrosUsuariosCk(), usuario, Utilidades.convertirAEntero(forma.getCentrosCostoEntiSub("consecutivo_"+forma.getIndiceModificar())+"")));
						
			if(forma.isGuardo())
			{
				int i= forma.getIndiceModificar();
				
				forma.setGuardo(mundo.guardarLogCentrosCostoEntiSub(forma.getCentrosCostoEntiSub("centroCosto_"+i)+"", forma.getCentrosCostoEntiSub("entidadSubcontratada_"+i)+"", "N", forma.getCentrosCostoEntiSub("nroPrioridad_"+i)+"", forma.getPrioridad(), forma.getCentrosCostoEntiSub("respOtrosUsuarios_"+i)+"", forma.getRespOtrosUsuariosCk(), usuario));
				
				if(forma.isGuardo())
					forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
				else
					forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
			}
			else
				forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
			
			forma.setCentrosCostoEntiSub(mundo.consultarCentrosCostoEntiSub(Utilidades.convertirAEntero(forma.getCentroAtencionSel()+"")));
		}
		return mapping.findForward("principal");
	}
	
	/**
	 * Metodo que modifica la informacion de un registro de  centro de costo por entidades subcontratadas
	 * @param forma
     * @param con
     * @param mapping
     * @return
	 */
	private ActionForward accionModificarRegistro(CentrosCostoEntidadesSubcontratadasForm forma, CentrosCostoEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{				
		forma.setCentroCostoSel(Utilidades.convertirAEntero(forma.getCentrosCostoEntiSub("centroCosto_"+forma.getIndiceModificar())+""));
		forma.setEntidadSubcontratadaSel(Utilidades.convertirAEntero(forma.getCentrosCostoEntiSub("entidadSubcontratada_"+forma.getIndiceModificar())+""));
		forma.setPrioridad(forma.getCentrosCostoEntiSub("nroPrioridad_"+forma.getIndiceModificar())+"");
		forma.setRespOtrosUsuariosCk(forma.getCentrosCostoEntiSub("respOtrosUsuarios_"+forma.getIndiceModificar())+"");
		
		return mapping.findForward("principal");
	}
	
	/**
	 * Metodo que guarda la informacion de centros de costo por entidades subcontratadas
	 * @param forma
     * @param con
     * @param mapping
     * @return
	 */
	private ActionForward accionGuardarNuevo(CentrosCostoEntidadesSubcontratadasForm forma, CentrosCostoEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{		
		ActionErrors errores = new ActionErrors();		
		HashMap temp= new HashMap();
		int i=0;
		int prioridad=0;
		boolean tipoarea=false;
		
		for(int j=0;j<(Utilidades.convertirAEntero(forma.getCentrosCostoEntiSub("numRegistros")+""));j++)
		{	
			//Se valida si el centro de costo es de tipo area= SubAlmacen y si ya tiene una entidad subcontratada asociada si es asi no permite asociar una nueva
			if(Utilidades.convertirAEntero(forma.getCentrosCostoEntiSub("centroCosto_"+j)+"") == (forma.getCentroCostoSel()))
			{
				if(Utilidades.convertirAEntero(forma.getCentrosCostoEntiSub("tipoArea_"+j)+"") == 3)
				{
					tipoarea=true;
					errores.add("descripcion",new ActionMessage("prompt.generico","Centro de Costo con Entidad Subcontratada Asignada. Por favor verifique"));
				}
				else
				{
					//se valida que la prioridad por cada centro de atencion/centro de costo/entidad subcontratada sea secuencial aunmentando en uno
					
					// ================================================================
					/*
					 * Anexo 786 - Cambio versión 1.3
					 * Se cambia la siguiente validación para el campo Prioridad:
					 * Se quita de la validación "Por cada Centro de Atención/ Centro de costo / Entidad Subcontratada" la Entidad Subcontratada.
					 * 
					 * Al comentar estas lineas siguientes (el if) solo queda validando por entro de Atención/ Centro de costo y no tiene en cuenta la Entidad Subcontratada
					 * 
					 *  - Cristhian Murillo
					*/	
					/* if(Utilidades.convertirAEntero(forma.getCentrosCostoEntiSub("entidadSubcontratada_"+j)+"") == (forma.getEntidadSubcontratadaSel())){ */
						temp.put("centroCosto_"+i, forma.getCentrosCostoEntiSub("centroCosto_"+j));
						temp.put("entidadSubcontratada_"+i, forma.getCentrosCostoEntiSub("entidadSubcontratada_"+j));
						temp.put("nroPrioridad_"+i, forma.getCentrosCostoEntiSub("nroPrioridad_"+j));
						i++;
					/* } */	
				}	
			}
		}		
		
		for(int l=0;l<i;l++)
		{
			int aux=0;
			for(int m=l; m<i;m++)
			{
				if(Utilidades.convertirAEntero(temp.get("nroPrioridad_"+l)+"") > Utilidades.convertirAEntero(temp.get("nroPrioridad_"+m)+"")){
					aux=Utilidades.convertirAEntero(temp.get("nroPrioridad_"+m)+"");
					temp.put("nroPrioridad_"+m,Utilidades.convertirAEntero(temp.get("nroPrioridad_"+l)+""));
					temp.put("nroPrioridad_"+l,aux);
				}
			}
		}		
		
		logger.info("\n\nMAPA TEMP------->"+temp);
		
		if(i>0)
		{
			prioridad= Utilidades.convertirAEntero(temp.get("nroPrioridad_"+(i-1))+"")+1;
			if(Utilidades.convertirAEntero(forma.getPrioridad()) != prioridad)
				errores.add("descripcion",new ActionMessage("prompt.generico","La prioridad para el Centro de Atención y el Centro de Costo Seleccionados debe ser el número "+prioridad));
		}
		else
		{
			if(!tipoarea && Utilidades.convertirAEntero(forma.getPrioridad()) != 1)
				errores.add("descripcion",new ActionMessage("prompt.generico","La prioridad para el Centro de Atención y el Centro de Costo Seleccionados debe ser el número "+1));
		}
		
		

		// ================================================================
		/*
		 * Anexo 786 - Cambio versión 1.3
		 * -Se debe validar que los registros sean únicos y no se repitan.
		 * 
		 *  - Cristhian Murillo
		 */
		for(int c=0;c<(Utilidades.convertirAEntero(forma.getCentrosCostoEntiSub("numRegistros")+""));c++)
		{
			if(Utilidades.convertirAEntero(forma.getCentrosCostoEntiSub("centroCosto_"+c)+"") == (forma.getCentroCostoSel()))
			{
				if(Utilidades.convertirAEntero(forma.getCentrosCostoEntiSub("entidadSubcontratada_"+c)+"") == (forma.getEntidadSubcontratadaSel()))
				{
					errores.add("descripcion",new ActionMessage("prompt.generico","No se puede seleccionar la misma combinación de  Centro de Atención, Centro de Costo y Entidad Subcontratada"));
					break;
				}
				
			}
		}
		// ================================================================
		
		
		
		
		
		if(!errores.isEmpty())		
			{
				forma.setEstado("nuevoRegistro");
				saveErrors(request,errores);
			}		
		else
		{
			int conse= mundo.insertarNuevoRegistro(forma.getCentroCostoSel(), forma.getEntidadSubcontratadaSel(), forma.getPrioridad(), forma.getRespOtrosUsuariosCk(), usuario);
					
			if(conse > 0)
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			else
				forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
			
			forma.setCentrosCostoEntiSub(mundo.consultarCentrosCostoEntiSub(Utilidades.convertirAEntero(forma.getCentroAtencionSel()+"")));
		}		
		
		return mapping.findForward("principal");
	}
	
	/**
     * Metodo que realiza busqueda de registros por centro de atencion 
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionBuscarRegistros(Connection con, CentrosCostoEntidadesSubcontratadasForm forma, CentrosCostoEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		con = UtilidadBD.abrirConexion();
		int n=0;
		//ActionErrors errores = new ActionErrors();
		String tiposArea = ConstantesBD.codigoTipoAreaDirecto+ConstantesBD.separadorSplit+ConstantesBD.codigoTipoAreaSubalmacen;
		forma.setCentrosCostoPorCAtencionMap(Utilidades.obtenerCentrosCosto(usuario.getCodigoInstitucionInt(), tiposArea, false, forma.getCentroAtencionSel(), false));
				
		forma.setCentrosCostoEntiSub(mundo.consultarCentrosCostoEntiSub(Utilidades.convertirAEntero(forma.getCentroAtencionSel()+"")));
				
		forma.setEntidadesSubcontratadasMap("numRegistros", n);
		UtilidadBD.cerrarConexion(con);
		
		return mapping.findForward("principal");
	}
		
	/**
     * Metodo que realiza busqueda de registros por centro de atencion 
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionBuscarRegistrosConsulta(Connection con, CentrosCostoEntidadesSubcontratadasForm forma, CentrosCostoEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		con = UtilidadBD.abrirConexion();
		ArrayList<HashMap<String, Object>> entidades= new ArrayList<HashMap<String, Object>>();
		
		//ActionErrors errores = new ActionErrors();

		forma.setCentrosCostoPorCAtencionMap(Utilidades.obtenerCentrosCosto(usuario.getCodigoInstitucionInt(), "", true, forma.getCentroAtencionSel(), true));
		
		logger.info("\n\nCENTROS DE COSTO X CENTRO ATENCION-------->"+forma.getCentrosCostoPorCAtencionMap());
		
		forma.setCentrosCostoEntiSub(mundo.consultarCentrosCostoEntiSub(Utilidades.convertirAEntero(forma.getCentroAtencionSel()+"")));
				
		entidades= (UtilidadesManejoPaciente.obtenerEntidadesSubcontratadas(con, usuario.getCodigoInstitucionInt()));
	
		 for(int i=0; i<entidades.size();i++)
		{	
		   HashMap mapaAux = (HashMap)entidades.get(i);
		   forma.setEntidadesSubcontratadasMap("codigo_"+i, mapaAux.get("consecutivo"));
		   forma.setEntidadesSubcontratadasMap("descripcion_"+i, mapaAux.get("descripcion"));
		}
		 
		forma.setEntidadesSubcontratadasMap("numRegistros", entidades.size());
		UtilidadBD.cerrarConexion(con);
		
		return mapping.findForward("principalconsulta");
	}
	
	/**
     * Inicia en el forward de Centros Costo Entidades Subcontratadas
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionEmpezar(Connection con, CentrosCostoEntidadesSubcontratadasForm forma, CentrosCostoEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		try{
			//ActionErrors errores = new ActionErrors();
			forma.reset(usuario.getCodigoInstitucionInt());
	
			con = UtilidadBD.abrirConexion();
			ArrayList<HashMap<String, Object>> entidades= new ArrayList<HashMap<String, Object>>();
			
			forma.setCentroAtencionSel(usuario.getCodigoCentroAtencion());
			String tiposArea = ConstantesBD.codigoTipoAreaDirecto+ConstantesBD.separadorSplit+ConstantesBD.codigoTipoAreaSubalmacen;
			forma.setCentrosCostoPorCAtencionMap(Utilidades.obtenerCentrosCosto(usuario.getCodigoInstitucionInt(), tiposArea, false, forma.getCentroAtencionSel(), false));
					
			forma.setCentrosCostoEntiSub(mundo.consultarCentrosCostoEntiSub(Utilidades.convertirAEntero(forma.getCentroAtencionSel()+"")));	
						
			forma.setCentrosAtencionMap(Utilidades.obtenerCentrosAtencionInactivos(usuario.getCodigoInstitucionInt(), false));
			
			int n=0;
			forma.setEntidadesSubcontratadasMap("numRegistros", n);
		}
		catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.cerrarConexion(con);
		}
		return mapping.findForward("principal");
	}
	
	/**
     * Inicia en el forward de Centros Costo Entidades Subcontratadas para Consulta
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezarConsulta(CentrosCostoEntidadesSubcontratadasForm forma, CentrosCostoEntidadesSubcontratadas mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//ActionErrors errores = new ActionErrors();
		forma.reset(usuario.getCodigoInstitucionInt());
			
		forma.setCentroAtencionSel(usuario.getCodigoCentroAtencion());
		
		forma.setCentrosCostoPorCAtencionMap(Utilidades.obtenerCentrosCosto(usuario.getCodigoInstitucionInt(), "", true, forma.getCentroAtencionSel(), true));
				
		forma.setCentrosCostoEntiSub(mundo.consultarCentrosCostoEntiSub(Utilidades.convertirAEntero(forma.getCentroAtencionSel()+"")));	
		
		forma.setCentrosAtencionMap(Utilidades.obtenerCentrosAtencionInactivos(usuario.getCodigoInstitucionInt(), false));
		
		return mapping.findForward("principalconsulta");
	}
	
	/**
     * Inicia en el forward de Centros Costo Entidades Subcontratadas para Consulta
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionBuscarEntidadPorCentroCosto(CentrosCostoEntidadesSubcontratadasForm forma, ActionMapping mapping, 
							CentrosCostoEntidadesSubcontratadas mundo, HttpServletRequest request, UsuarioBasico usuario) 
	{
		Connection con=null;
		ActionErrors errores = new ActionErrors();
		try{
			con=UtilidadBD.abrirConexion();
			String entidadInterna = ValoresPorDefecto.getEntidadSubcontratadaCentrosCostoAutorizacionCapitacionSubcontratada(usuario.getCodigoInstitucionInt());
			if(entidadInterna != null && !entidadInterna.trim().isEmpty()){
				try{
					
					//Add Javier, para validar que patrones como "-1-Otra" no tome un espacio vacio como codigo de entidad
					entidadInterna = entidadInterna.trim();
					String codigoEntidad= "";
					String nombreEntidad= "";					
					if(entidadInterna.startsWith("-")){
						codigoEntidad="-"+entidadInterna.split("-")[1];
						nombreEntidad=entidadInterna.split("-")[2];
					}
					else{
						codigoEntidad=entidadInterna.split("-")[0];
						nombreEntidad=entidadInterna.split("-")[1];
					}
					//Fin Javier
					
					
					// Se verifica el centro de costo selecciónado para saber como se consultan las entidades subcontratadas
					int codigoCentroCosto=forma.getCentroCostoSel();
					if(codigoCentroCosto > 0){
						int numRegistros=Integer.valueOf(forma.getCentrosCostoPorCAtencionMap().get("numRegistros").toString());
						String tipoEntidadEjecuta=null;
						for(int i=0;i<numRegistros;i++){
							if(forma.getCentrosCostoPorCAtencionMap().get("codigo_"+i) != null){
								int codCC=Integer.valueOf(forma.getCentrosCostoPorCAtencionMap().get("codigo_"+i).toString());
								Object teeCC=forma.getCentrosCostoPorCAtencionMap().get("tipoentidad_"+i);
								if(teeCC != null && codigoCentroCosto==codCC){
									tipoEntidadEjecuta=teeCC.toString();
									break;
								}
							}
						}
						forma.setEntidadesSubcontratadasMap("numRegistros", 0);
						if(tipoEntidadEjecuta != null){
							if(tipoEntidadEjecuta.equals(ConstantesIntegridadDominio.acronimoInterna)){
								forma.setEntidadesSubcontratadasMap("numRegistros", 1);
								forma.setEntidadesSubcontratadasMap("codigo_0", codigoEntidad);
								forma.setEntidadesSubcontratadasMap("descripcion_0", nombreEntidad);
								forma.setEntidadSubcontratadaSel(Integer.parseInt(codigoEntidad));
							}
							else{
								forma.setEntidadSubcontratadaSel(ConstantesBD.codigoNuncaValido);
								forma.setEntidadesSubcontratadasMap(mundo.consultarEntidadesSubSinInterna(con, Integer.valueOf(codigoEntidad)));
							}
						}
					}
					else{
						forma.setEntidadesSubcontratadasMap("numRegistros", 0);
					}
				}
				catch (ArrayIndexOutOfBoundsException aiobe) {
					Log4JManager.error("LA ENTIDAD SUBCONTRATADA PARAMETRIZADA NO ESTA BIEN DEFINIDA o NO TIENE UN FORMATO VALIDO");
					errores.add("",new ActionMessage("errors.parametroGeneral", "Entidad Subcontratada para Centros de Costo Internos"));
				}
			}
			else{
				errores.add("",new ActionMessage("errors.parametroGeneral", "Entidad Subcontratada para Centros de Costo Internos"));
				saveErrors(request, errores);
			}
		}
		catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return mapping.findForward("principal");
	}
}