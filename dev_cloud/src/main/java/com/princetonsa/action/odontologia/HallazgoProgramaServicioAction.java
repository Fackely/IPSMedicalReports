
package com.princetonsa.action.odontologia;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.UtilidadBD;
import util.UtilidadLog;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.odontologia.HallazgoProgramaServicioForm;
import com.princetonsa.dto.odontologia.DtoDetalleHallazgoProgramaServicio;
import com.princetonsa.dto.odontologia.DtoEquivalentesHallazgoProgramaServicio;
import com.princetonsa.dto.odontologia.DtoHallazgoVsProgramaServicio;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoPrograma;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.DetalleHallazgoProgramaServicio;
import com.princetonsa.mundo.odontologia.HallazgoVsProgramaServicio;
import com.princetonsa.mundo.odontologia.HallazgosOdontologicos;
import com.princetonsa.mundo.odontologia.Programa;
import com.princetonsa.mundo.odontologia.ProgramasOdontologicos;
import com.princetonsa.sort.odontologia.SortGenerico;

public class HallazgoProgramaServicioAction  extends Action {

	
	private Logger logger = Logger.getLogger(HallazgoProgramaServicioAction.class);
	public ActionForward execute(	ActionMapping mapping, 
			ActionForm form,
			HttpServletRequest request, 
			HttpServletResponse response)throws Exception 
{
		if (form instanceof HallazgoProgramaServicioForm) 
		{
		
			HallazgoProgramaServicioForm forma = (HallazgoProgramaServicioForm) form;
			ActionErrors errores = new ActionErrors();
			
		logger.info("\n\n\n\n\n ESTADO ---------------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+forma.getEstado()+"\n\n\n");
		UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
		if (forma.getEstado().equals("empezar") || forma.getEstado().equals("empezarConsulta")) 
		{
		return accionEmpezar(mapping, forma, usuario);
		}
		if (forma.getEstado().equals("empezarDetalle")) 
		{
		return accionEmpezarDetalle(mapping, forma, usuario);
		}
		
		if (forma.getEstado().equals("ingresar")) 
		{
		return accionIngresar(mapping, forma, usuario);
		}
		
		if (forma.getEstado().equals("ingresarDetalle")) 
		{
		return accionIngresarDetalle(mapping, forma, usuario);
		}
		
		if (forma.getEstado().equals("ingresarEquivalentes")) 
		{
		return accionIngresarEquivalentes(mapping, forma, usuario);
		}
		if (forma.getEstado().equals("ingresarEquivalentesModificar")) 
		{
		return accionIngresarEquivalentesModificar(mapping, forma, usuario);			
		}			
		if (forma.getEstado().equals("modificar")) 
		{
		return accionModificar(mapping, forma, usuario);
		}
		
		if (forma.getEstado().equals("modificarDetalle")) 
		{
		return accionModificarDetalle(mapping, forma, usuario, request);
		}
		
		if (forma.getEstado().equals("guardarModificar")) 
		{
		return accionGuardarModificar(mapping, forma, usuario);
		}
		
		if (forma.getEstado().equals("guardarModificarDetalle")) 
		{
			return accionGuardarModificarDetalle(mapping, forma, usuario, request);
		}
		
		if (forma.getEstado().equals("guardar")) 
		{
			return accionGuardar(mapping, forma, usuario);
		}
		
		if (forma.getEstado().equals("eliminar")) 
		{
		return accionEliminar(mapping, forma, usuario);
		}
		
		if (forma.getEstado().equals("guardarDetalle")) 
		{
			
		return accionGuardarDetalle(mapping, forma, usuario,request);
		}
		
		else if (forma.getEstado().equals("ordenar")) 
		{
			return accionOrdenar(mapping, forma, usuario);
		}
		
		else if (forma.getEstado().equals("ordenarDetalle")) 
		{
			return accionOrdenarDetalle(mapping, forma, usuario);
		}
		
		else if (forma.getEstado().equals("cargarPrograma"))
		{
			forma.setEstado("ingresarDetalle");
			 return mapping.findForward("paginaDetalle"); 
		}
		else if(forma.getEstado().equals("mostrarErroresGuardarDetalle"))
		{
			return mapping.findForward("paginaDetalle");
		}
		else if(forma.getEstado().equals("mostrarErroresGuardar"))
		{
			return mapping.findForward("paginaPrincipal");
		}
		else if(forma.getEstado().equals("armarSubEquivalentes"))
		{
			this.cargarSubEquivalentes(forma, response);
		}	
		else if(forma.getEstado().equals("eliminarDetalle"))
		{
			DtoDetalleHallazgoProgramaServicio  dtoBorrar =forma.getListaDetalles().get(forma.getPosArrayDetalle()); 
			boolean aplicaProgramas=Boolean.FALSE;
			
			if(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi) )
			{
				aplicaProgramas=Boolean.TRUE;
			}
			
			if( DetalleHallazgoProgramaServicio.existenDetalleHallazgoPlanTratamiento(dtoBorrar,aplicaProgramas))
			{
				errores.add("",	new ActionMessage("errors.notEspecific"," El Hallazgo ya existe en plan de Tratamiento "));
				saveErrors(request, errores);
			}
			else
			{
				DetalleHallazgoProgramaServicio.eliminar(dtoBorrar)	;
				DtoEquivalentesHallazgoProgramaServicio dtoEquivalentes= new DtoEquivalentesHallazgoProgramaServicio();
				dtoEquivalentes.setDetallehallazgo(new InfoDatosDouble(dtoBorrar.getCodigo() ,""));
				DetalleHallazgoProgramaServicio.eliminarEquivalentes(dtoEquivalentes);
			}
			
			return accionEmpezarDetalle(mapping,forma, usuario);
			
		}
		
			
		
		
		}
		
		
		
		return null;
}

	
	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarDetalleHallazgo(
			HallazgoProgramaServicioForm forma, UsuarioBasico usuario) {
			
			forma.setDtoDetalle(forma.getListaDetalles().get(forma.getPosArrayDetalle())) ;
			DtoDetalleHallazgoProgramaServicio dtoEliminar = new DtoDetalleHallazgoProgramaServicio();
			dtoEliminar.setCodigo(forma.getDtoDetalle().getCodigo());
			DetalleHallazgoProgramaServicio.eliminar(dtoEliminar);
			
			return null;
	}


	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionIngresarEquivalentesModificar(
			ActionMapping mapping, HallazgoProgramaServicioForm forma,
			UsuarioBasico usuario) {
		
		
		
		
		
		DtoEquivalentesHallazgoProgramaServicio dtoWhere = new DtoEquivalentesHallazgoProgramaServicio();
		dtoWhere.getDetallehallazgo().setCodigo(forma.getDtoDetalle().getCodigo());
		DetalleHallazgoProgramaServicio.eliminarEquivalentes(dtoWhere);
		
		
		
//		logger.info(forma.getListaDetalles().get(forma.getPosArrayDetalle()).getNumeroSuperficies() + "== "+ forma.getDtoDetalle().getNumeroSuperficies());
		
		
		if(forma.getListaDetalles().get(forma.getPosArrayDetalle()).getNumeroSuperficies() != forma.getDtoDetalle().getNumeroSuperficies())
		{
			forma.setCambioNumeroSuperficies(true);
			
		
		
		ArrayList<DtoEquivalentesHallazgoProgramaServicio> listaeq = new ArrayList<DtoEquivalentesHallazgoProgramaServicio>();
		
		
		for(DtoDetalleHallazgoProgramaServicio i : forma.getListaDetalles())
		{
			
			DtoEquivalentesHallazgoProgramaServicio dto = new DtoEquivalentesHallazgoProgramaServicio();
			dto.setActivo(false);
			dto.setDatosfechaUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
			
		   if(i.getNumeroSuperficies() == forma.getDtoDetalle().getNumeroSuperficies())
		   {
				if(forma.getTipodeRelacion().equals("Programa"))
				{
					dto.setDetallehallazgo2(new InfoDatosDouble(i.getCodigo(), i.getPrograma().getNombre()));
				}
				else
				{
					dto.setDetallehallazgo2(new InfoDatosDouble(i.getCodigo(), i.getServicio().getNombre()));
				}
				
				
				listaeq.add(dto);
		   }
		}
		
		forma.setListaEquivalentes(listaeq);
		}
		
		return mapping.findForward("paginaDetalle");
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionIngresarEquivalentes(ActionMapping mapping,
			HallazgoProgramaServicioForm forma, UsuarioBasico usuario) {
		
		
		
		ArrayList<DtoEquivalentesHallazgoProgramaServicio> listaeq = new ArrayList<DtoEquivalentesHallazgoProgramaServicio>();
		
		
		for(DtoDetalleHallazgoProgramaServicio i : forma.getListaDetalles())
		{
			
			DtoEquivalentesHallazgoProgramaServicio dto = new DtoEquivalentesHallazgoProgramaServicio();
			dto.setActivo(false);
			dto.setDatosfechaUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
			
		   if(i.getNumeroSuperficies() == forma.getDtoDetalle().getNumeroSuperficies())
		   {
			   
			   //Aplica para programas
				if(forma.getTipodeRelacion().equals("Programa"))
				{
					dto.setDetallehallazgo2(new InfoDatosDouble(i.getCodigo(), i.getPrograma().getNombre()));
				}
				else
				{
					dto.setDetallehallazgo2(new InfoDatosDouble(i.getCodigo(), i.getServicio().getNombre()));
				}
				
				
				//tmp para cargar los Equivalentes en Base de Datos
				DtoEquivalentesHallazgoProgramaServicio temporalEquivalente = new DtoEquivalentesHallazgoProgramaServicio();
				temporalEquivalente.getDetallehallazgo().setCodigo(i.getCodigo());
				
				//Cargarmos los Equivalentes de BD
				ArrayList<DtoEquivalentesHallazgoProgramaServicio> listTmp= DetalleHallazgoProgramaServicio.cargarEquivalentes(temporalEquivalente, forma.getTipodeRelacion());
				
				//adiccionamos a la Lista
				dto.setListaSubEquivalentes(listTmp);
				
				//adiccionamos dto a la lista
				listaeq.add(dto);
		   }
		}
		
		forma.setListaEquivalentes(listaeq);
		
		
		return mapping.findForward("paginaDetalle");
		
	}

	
	
	/**
	 * Arma los Sub Equivalentes en xml
	 * @param forma
	 */
	private void cargarSubEquivalentes(HallazgoProgramaServicioForm forma , HttpServletResponse response)
	{
		
		
		String xmlEquivalentes="";
		
		// Dto para settear el equivalentes seleccionado
		
		DtoEquivalentesHallazgoProgramaServicio dto = forma.getListaEquivalentes().get(forma.getPostIndiceEquivalentes());
		
		
		for(DtoEquivalentesHallazgoProgramaServicio dtoEquivalentes : forma.getListaEquivalentes())
		{
			for(DtoEquivalentesHallazgoProgramaServicio dtoSubEquivalentes: dto.getListaSubEquivalentes())
			{
				
				if(dtoSubEquivalentes.getDetallehallazgo2().getCodigo().equals(dtoEquivalentes.getDetallehallazgo2().getCodigo()))
				{
					xmlEquivalentes+="<sub>";
					xmlEquivalentes+="<cod-detalle>";
					xmlEquivalentes+=dtoEquivalentes.getDetallehallazgo2().getCodigo();
					xmlEquivalentes+="</cod-detalle>";
					xmlEquivalentes+="<cod-prog-ser>";
					xmlEquivalentes+=dtoEquivalentes.getDetallehallazgo2().getNombre();
					xmlEquivalentes+="</cod-prog-ser>";	
					xmlEquivalentes+="</sub>";	
				}
			}
		}
		
		
		
		 forma.setRespuestaXml(xmlEquivalentes);
		
		 response.setContentType("text/xml");
		 response.setHeader("Cache-Control", "no-cache");
		 response.setCharacterEncoding("UTF-8");
	
		 try 
		 {
			 
			 response.getWriter().write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			 response.getWriter().write("<respuesta>");
			 response.getWriter().write(forma.getRespuestaXml());
			 response.getWriter().write("</respuesta>");
		}
		catch (IOException e) 
		{
		
			e.printStackTrace();
		}
		
		
			
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminar(ActionMapping mapping,
			HallazgoProgramaServicioForm forma, UsuarioBasico usuario) {
		
		HallazgoVsProgramaServicio.eliminar(forma.getListaHallazgoProgramaServicio().get(forma.getPosArray()));
        UtilidadLog.generarLog(usuario, forma.getListaHallazgoProgramaServicio().get(forma.getPosArray()) , null , ConstantesBD.tipoRegistroLogEliminacion, 1007);
		
        forma.setMensaje("Proceso Realizado Exitosamente!");
		return accionEmpezar(mapping, forma, usuario);
		
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionOrdenarDetalle(ActionMapping mapping,
			HallazgoProgramaServicioForm forma, UsuarioBasico usuario) {
		
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		
		Collections.sort(forma.getListaDetalles(),sortG);
		forma.setEstado("empezarDetalle");
		return mapping.findForward("paginaDetalle");
		
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarModificarDetalle(ActionMapping mapping,
														HallazgoProgramaServicioForm forma, 
														UsuarioBasico usuario, 
														HttpServletRequest request) 
	{
	
		
		
		
	    forma.getDtoDetalle().setDatosfechaUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		
       // logger.info(UtilidadLog.obtenerString(forma.getDtoDetalle(), true));
		//logger.info(UtilidadLog.obtenerString(forma.getListaDetalles().get(forma.getPosArrayDetalle()), true));
	    
	    
		DetalleHallazgoProgramaServicio.modificar(forma.getDtoDetalle(), forma.getListaDetalles().get(forma.getPosArrayDetalle()));
		
		//UtilidadLog.generarLog(usuario, forma.getListaDetalles().get(forma.getPosArrayDetalle()) , forma.getDtoDetalle() , ConstantesBD.tipoRegistroLogModificacion, 1007);
	
		
		Connection con = UtilidadBD.abrirConexion();
		
		
		for(DtoEquivalentesHallazgoProgramaServicio i :forma.getListaEquivalentes())
		{
			
			if(i.isActivo())
			{
				if(forma.getTipodeRelacion().equals("Programa"))
				{
					i.setDetallehallazgo(new InfoDatosDouble(forma.getDtoDetalle().getCodigo(), forma.getDtoDetalle().getPrograma().getNombre()));
				}
				else
				{
					i.setDetallehallazgo(new InfoDatosDouble(forma.getDtoDetalle().getCodigo(), forma.getDtoDetalle().getServicio().getNombre()));
				}
	           
				
				DtoEquivalentesHallazgoProgramaServicio dtoContrario = new DtoEquivalentesHallazgoProgramaServicio();
				dtoContrario = (DtoEquivalentesHallazgoProgramaServicio)i.clone();
				
				DetalleHallazgoProgramaServicio.guardarEquivalente(i,con);
	            
			    i.setDetallehallazgo(dtoContrario.getDetallehallazgo2());
				i.setDetallehallazgo2(dtoContrario.getDetallehallazgo());		
				
				DetalleHallazgoProgramaServicio.guardarEquivalente(i, con);
				
				
			 }
			
		}
		
		accionGuardarDetalleEquivalente(mapping, forma, usuario, request, con);
		
		UtilidadBD.closeConnection(con);
		
		return accionEmpezarDetalle(mapping, forma, usuario);
	}


	/** 
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificarDetalle(ActionMapping mapping,
												HallazgoProgramaServicioForm forma, UsuarioBasico usuario , HttpServletRequest request) 
	{
	
		
		forma.setCambioNumeroSuperficies(false);
		//logger.info(UtilidadLog.obtenerString(forma.getListaDetalles().get(forma.getPosArrayDetalle()).clone(),true));
		
		forma.setDtoDetalle((DtoDetalleHallazgoProgramaServicio) forma.getListaDetalles().get(forma.getPosArrayDetalle()).clone());
		 
		 //DetalleHallazgoProgramaServicio.existenHallazgosPlanTratamiento(forma.getDtoDetalle());
		
				
		DtoPrograma dtoWhere = new DtoPrograma();
		dtoWhere.setInstitucion(usuario.getCodigoInstitucionInt());
		forma.setListaProgramas(Programa.cargar(dtoWhere));
		
		
       ArrayList<DtoEquivalentesHallazgoProgramaServicio> listaeq = new ArrayList<DtoEquivalentesHallazgoProgramaServicio>();
		
       for(DtoDetalleHallazgoProgramaServicio i : forma.getListaDetalles())
       {
			DtoEquivalentesHallazgoProgramaServicio dto = new DtoEquivalentesHallazgoProgramaServicio();
			dto.setActivo(false);
			dto.setDatosfechaUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
			if(forma.getTipodeRelacion().equals("Programa"))
			{
				dto.setDetallehallazgo2(new InfoDatosDouble(i.getCodigo(), i.getPrograma().getNombre()));
			}
			else
			{
				dto.setDetallehallazgo2(new InfoDatosDouble(i.getCodigo(), i.getServicio().getNombre()));
			}
			
			if((dto.getDetallehallazgo2().getCodigo() != forma.getListaDetalles().get(forma.getPosArrayDetalle()).getCodigo() ) && (i.getNumeroSuperficies()== forma.getListaDetalles().get(forma.getPosArrayDetalle()).getNumeroSuperficies()))
			{
				listaeq.add(dto);
			}
			else{
				logger.info("EL MISMO ES "+forma.getListaDetalles().get(forma.getPosArrayDetalle()).getCodigo());
			}
		}
		
		ArrayList<DtoEquivalentesHallazgoProgramaServicio> listaEnBD = new ArrayList<DtoEquivalentesHallazgoProgramaServicio>();
		DtoEquivalentesHallazgoProgramaServicio dtowhereEq= new DtoEquivalentesHallazgoProgramaServicio();
		
		if(forma.getTipodeRelacion().equals("Programa"))
		{
			dtowhereEq.setDetallehallazgo(new InfoDatosDouble(forma.getDtoDetalle().getCodigo(), forma.getDtoDetalle().getPrograma().getNombre()));
		}
		else
		{
			dtowhereEq.setDetallehallazgo(new InfoDatosDouble(forma.getDtoDetalle().getCodigo(), forma.getDtoDetalle().getServicio().getNombre()));
		}
		
		listaEnBD = DetalleHallazgoProgramaServicio.cargarEquivalentes(dtowhereEq, forma.getTipodeRelacion());
		
		
//		for(DtoEquivalentesHallazgoProgramaServicio ie:listaEnBD ){
//			
//			logger.info(ie.getDetallehallazgo2().getCodigo());
//		}
		
		for(DtoEquivalentesHallazgoProgramaServicio dtoE : listaeq)
		{
			if(this.existeEnEquivalentes(dtoE.getDetallehallazgo2().getCodigo(), listaEnBD ))
			{
				dtoE.setActivo(true);
			}
		}
		
		
		forma.setListaEquivalentes(listaeq);
		
		
		if(forma.getDtoDetalle().getPorDefecto().equals(ConstantesBD.acronimoSi))
		{
			forma.setTieneSuperficiePorDefecto(ConstantesBD.acronimoNo);
		}
			
		//
		
	    return mapping.findForward("paginaDetalle"); 
	}
	
	
	/**
	 * 
	 * @param datos
	 * @param array
	 * @return
	 */
	public boolean existeEnEquivalentes(double datos , ArrayList<DtoEquivalentesHallazgoProgramaServicio> array){
		
		boolean temporal = false;
		for(DtoEquivalentesHallazgoProgramaServicio dto : array){
			
			
			if(dto.getDetallehallazgo2().getCodigo() == datos){
				
				
				temporal =  true;
			    break;
			}
		}
		return temporal;
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardarModificar(ActionMapping mapping,
			HallazgoProgramaServicioForm forma, UsuarioBasico usuario) {
		
	    forma.getDtoHallazgoProgramaServicio().setDatosfechaUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		
//      logger.info(UtilidadLog.obtenerString("EL DTO*************************"+forma.getDtoHallazgoProgramaServicio(), true));
//		
//		logger.info(UtilidadLog.obtenerString("LA LISTA **********************"+forma.getListaHallazgoProgramaServicio().get(forma.getPosArray()), true));
	    HallazgoVsProgramaServicio.modificar(forma.getDtoHallazgoProgramaServicio(), forma.getListaHallazgoProgramaServicio().get(forma.getPosArray()));
		
		
		UtilidadLog.generarLog(usuario, forma.getListaHallazgoProgramaServicio().get(forma.getPosArray()) , forma.getDtoHallazgoProgramaServicio() , ConstantesBD.tipoRegistroLogModificacion, 1007);
		
		forma.setMensaje("Proceso Realizado Exitosamente!");
		
		
		
		
		return accionEmpezar(mapping, forma, usuario);
	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionModificar(ActionMapping mapping,
			HallazgoProgramaServicioForm forma, UsuarioBasico usuario) {
		
		
		
		//logger.info("ANTES DE COPYYYYYYYYYYYYYYYYYYYYYYYYY"+UtilidadLog.obtenerString(forma.getListaHallazgoProgramaServicio().get(forma.getPosArray()).clone(),true));
		
		//SERIALIZANDO
		
		try {
		FileOutputStream fo = new FileOutputStream("datos.obj"); 
		ObjectOutputStream oo = new ObjectOutputStream(fo); //otro objeto que necesitamos instanciar para la serializacion
		
			oo.writeObject(forma.getListaHallazgoProgramaServicio().get(forma.getPosArray()));
			oo.close();
		} catch (IOException e) {
			e.printStackTrace();
		} //aqui ocurre la serializacion de la clase Usuario
		
		
		try {
		FileInputStream fi = new FileInputStream("datos.obj"); 
		ObjectInputStream oi = new ObjectInputStream(fi); //objeto que instanciamos para deserializar
		forma.setDtoHallazgoProgramaServicio((DtoHallazgoVsProgramaServicio)oi.readObject()); //aqui ocurre realmente la deserializacion del objeto fijense en el cast que hay que hacer (Usuario)
		
		
			oi.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		//forma.setDtoHallazgoProgramaServicio((DtoHallazgoVsProgramaServicio) forma.getListaHallazgoProgramaServicio().get(forma.getPosArray()).clone());
		
		/*
		InfoDatosDouble origen=forma.getListaHallazgoProgramaServicio().get(forma.getPosArray()).getHallazgo();
		DtoHallazgoVsProgramaServicio destino = new DtoHallazgoVsProgramaServicio();
		destino.setHallazgo(new InfoDatosDouble(origen.getCodigo(), origen.getNombre()));
		destino.setCodigo(forma.getListaHallazgoProgramaServicio().get(forma.getPosArray()).getCodigo());
		destino.setInstitucion(forma.getListaHallazgoProgramaServicio().get(forma.getPosArray()).getInstitucion());
		forma.setDtoHallazgoProgramaServicio(destino);
	     */
//		logger.info("DESPUES  COPYYYYYYYYYYYYYYYYYYYYYYYYY DTO"+UtilidadLog.obtenerString(forma.getDtoHallazgoProgramaServicio(),true));
//		
//		logger.info("DESPUES  COPYYYYYYYYYYYYYYYYYYYYYYYYY LISTA "+UtilidadLog.obtenerString( forma.getListaHallazgoProgramaServicio().get(forma.getPosArray()),true));
//		
//		logger.info("LA POSICION DEL ARRAY"+forma.getPosArray());
		Connection con= UtilidadBD.abrirConexion();
		HallazgosOdontologicos hallazgo = new HallazgosOdontologicos();
		forma.setListaHallazgos(hallazgo.consultarHallazgosDentales(con, usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
	    return mapping.findForward("paginaPrincipal"); 
	}


	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarDetalle(ActionMapping mapping,
			HallazgoProgramaServicioForm forma, UsuarioBasico usuario , HttpServletRequest request) {
		
		forma.getDtoDetalle().setDatosfechaUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
		forma.getDtoDetalle().setHallazgoVsProgramaServicio(forma.getDtoHallazgoProgramaServicio().getCodigo());
		forma.getDtoDetalle().setOrden(forma.getListaDetalles().size());
		
		
		Connection con= UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		double codigoDetalle=DetalleHallazgoProgramaServicio.guardar(forma.getDtoDetalle() , con);
		
		
		if(codigoDetalle<=0)
		{
			logger.error("NO INSERTA Detalle");
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
			////hacer error
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
		}
		
		
		 
		
		/*
		 *GUARDAR EQUIVALENTES 
		 */
		for(DtoEquivalentesHallazgoProgramaServicio i :forma.getListaEquivalentes())
		{
			if(i.isActivo())
			{
				DtoEquivalentesHallazgoProgramaServicio dtoContrario = new DtoEquivalentesHallazgoProgramaServicio();
				i.getDetallehallazgo().setCodigo(codigoDetalle);
				dtoContrario = (DtoEquivalentesHallazgoProgramaServicio)i.clone();
				
//				logger.info("-->"+i.getDetallehallazgo2().getCodigo());
				
				if(DetalleHallazgoProgramaServicio.guardarEquivalente(i, con)<=0)
				{
					logger.error("NO INSERTA EQUIVALENTE ");
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
				}
				
				
				i.setDetallehallazgo(dtoContrario.getDetallehallazgo2());
				i.setDetallehallazgo2(dtoContrario.getDetallehallazgo());
				
				
				
				
				
				if(DetalleHallazgoProgramaServicio.guardarEquivalente(i, con)<=0)
				{
					logger.error("NO INSERTA EQUIVALENTE 2 ");
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
				}
				
			}
		}
		
		
		accionGuardarDetalleEquivalente(mapping, forma, usuario, request, con);
		
		
		logger.info("inserta 100%%%");
		forma.setEstado("resumen");
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		
		return accionEmpezarDetalle(mapping, forma, usuario);
	}


		
	
	/**
	 * ACCION GUARDAR DETALLE EQUIVALENTE
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param con
	 */
	private void accionGuardarDetalleEquivalente(ActionMapping mapping, HallazgoProgramaServicioForm forma, UsuarioBasico usuario , HttpServletRequest request , Connection con )
	{
		
		ArrayList<Double> lista = new ArrayList<Double>();
		
		for ( DtoEquivalentesHallazgoProgramaServicio objDetHallazgo: forma.getListaEquivalentes())
		{
			DtoEquivalentesHallazgoProgramaServicio dtoContrario = (DtoEquivalentesHallazgoProgramaServicio)objDetHallazgo.clone();
			Double dato = new Double(dtoContrario.getDetallehallazgo().getCodigo());
			lista.add(dato); 
		}
		
		
		
		
		for(Double codigo : lista)
		{
			for(DtoEquivalentesHallazgoProgramaServicio i :  forma.getListaEquivalentes())
			{
				if(i.isActivo())
				{
//					logger.info("i-> "+i.getDetallehallazgo().getCodigo() +" C->"+ codigo.doubleValue());
					
					if(i.getDetallehallazgo().getCodigo()>0 && codigo.doubleValue()>0)
					{
						
						DtoEquivalentesHallazgoProgramaServicio dto= new DtoEquivalentesHallazgoProgramaServicio();
						dto.setDatosfechaUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
						dto.setDetallehallazgo(new InfoDatosDouble(codigo, ""));
						dto.setDetallehallazgo2(new InfoDatosDouble(i.getDetallehallazgo().getCodigo(), ""));
						
						int cantidadRegistro= DetalleHallazgoProgramaServicio.cargarEquivalentes(dto, forma.getTipodeRelacion()).size();
						
						if( cantidadRegistro<=0)
						{
							if(i.getDetallehallazgo().getCodigo().doubleValue() !=codigo.doubleValue())
							{
								
								
								
								DtoEquivalentesHallazgoProgramaServicio dtoContrario = new DtoEquivalentesHallazgoProgramaServicio();
								i.getDetallehallazgo2().setCodigo(codigo);
								dtoContrario = (DtoEquivalentesHallazgoProgramaServicio)i.clone();
								
								
								
								if(DetalleHallazgoProgramaServicio.guardarEquivalente(i, con)<=0)
								{
									logger.error("NO INSERTA EQUIVALENTE ");
									UtilidadBD.abortarTransaccion(con);
									UtilidadBD.closeConnection(con);
									//return ComunAction.accionSalirCasoError(mapping, request, con, logger, "", "errors.problemasBd", true);
								}
							}	
						}
					}
				}
			}
		}
	}


	
	
	

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionIngresarDetalle(ActionMapping mapping,
			HallazgoProgramaServicioForm forma, UsuarioBasico usuario) 
	{
		
		forma.getDtoDetalle().reset();
		DtoPrograma dtoWhere = new DtoPrograma();
		dtoWhere.setInstitucion(usuario.getCodigoInstitucionInt());
		forma.setListaProgramas(Programa.cargar(dtoWhere));
		
		
		
		
		
		
		return mapping.findForward("paginaDetalle");
		
	}


	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarDetalle(ActionMapping mapping,		HallazgoProgramaServicioForm forma, UsuarioBasico usuario) {
		
		forma.setCodigoHallazgos("");
		forma.setDtoHallazgoProgramaServicio( (DtoHallazgoVsProgramaServicio) forma.getListaHallazgoProgramaServicio().get(forma.getPosArray()).clone());
		
		if( forma.getDtoHallazgoProgramaServicio().getAplicaSuperificie().endsWith(ConstantesIntegridadDominio.acronimoAplicaASuperficie))
		{
			forma.setAplicaSuperficie(ConstantesBD.valorFalseLargoString);
//			logger.info("  SI  APLICA SUPERFICIE ");
//			logger.info(" activa campo Numero de superficies");
		}
		
		
		
		DtoDetalleHallazgoProgramaServicio dtoWhere = new DtoDetalleHallazgoProgramaServicio();
		dtoWhere.setHallazgoVsProgramaServicio(forma.getDtoHallazgoProgramaServicio().getCodigo());
		
		forma.setListaDetalles(DetalleHallazgoProgramaServicio.cargar(dtoWhere, usuario.getCodigoInstitucionInt()));
		
		String listado="";
		
		if(forma.getListaDetalles().size() >0)
		{
			for(int w=0; w<forma.getListaDetalles().size(); w++)
			{
				if(w>0)
				{
					listado+=",";
				}
				if(forma.getTipodeRelacion().equals("Programa"))
				{
				listado+=forma.getListaDetalles().get(w).getPrograma().getCodigo()+"";
				}
				else
				{
					listado+=forma.getListaDetalles().get(w).getServicio().getCodigo()+"";
				}
			}
		}
		forma.setCodigoHallazgos(listado);
		
		if(forma.getTipodeRelacion().equals("Programa"))
		{
			String tmpBusquedaServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt()); 	
			
			for( DtoDetalleHallazgoProgramaServicio i : forma.getListaDetalles())
			{
			       	i.setListaServiciosPrograma(ProgramasOdontologicos.cargarDetallePrograma(i.getPrograma().getCodigo(),tmpBusquedaServicios  ));
			}
		}
		
		
		
		
		
		for( DtoDetalleHallazgoProgramaServicio j : forma.getListaDetalles())
		{
			DtoEquivalentesHallazgoProgramaServicio temporal = new DtoEquivalentesHallazgoProgramaServicio();
			
			if(forma.getTipodeRelacion().equals("Programa"))
			{
				temporal.setDetallehallazgo(new InfoDatosDouble(j.getCodigo(),j.getPrograma().getNombre()));
		
			}
			else
			{
				temporal.setDetallehallazgo(new InfoDatosDouble(j.getCodigo(),j.getServicio().getNombre()));
			}
	       	
			j.setListaEquivalentes(DetalleHallazgoProgramaServicio.cargarEquivalentes(temporal, forma.getTipodeRelacion()));
	       	
//	       logger.info("RESULTADO === "+j.getListaEquivalentes().size());      	
		}
	
		
		
		
		accionTieneSuperficiePorDefecto(forma);
		
		
		return mapping.findForward("paginaDetalle");
		
	}



	
	/**
	 * 
	 * @param forma
	 */
	private void accionTieneSuperficiePorDefecto(
												HallazgoProgramaServicioForm forma) 
	{
	
		boolean bandera= Boolean.FALSE;
	
		for ( DtoDetalleHallazgoProgramaServicio dtoDetalle: forma.getListaDetalles())
		{
			if(dtoDetalle.getPorDefecto().equals(ConstantesBD.acronimoSi))
			{
				bandera=Boolean.TRUE;
			}
		}
		
		
		if(bandera)
		{
			forma.setTieneSuperficiePorDefecto(ConstantesBD.acronimoSi) ;
		}
	}


	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionOrdenar(ActionMapping mapping,
			HallazgoProgramaServicioForm forma, UsuarioBasico usuario) {
		
//		logger.info("patron->" + forma.getPatronOrdenar());
//		logger.info("des -->" + forma.getEsDescendente() );
		
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		
		Collections.sort(forma.getListaHallazgoProgramaServicio(),sortG);
		forma.setEstado("empezar");
		forma.setMensaje("");
		return mapping.findForward("paginaPrincipal");
		
	}
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionGuardar(ActionMapping mapping,
			HallazgoProgramaServicioForm forma, UsuarioBasico usuario)
	{
		//double codigo = 0;
		forma.getDtoHallazgoProgramaServicio().setDatosfechaUsuarioModifica(new DtoInfoFechaUsuario(usuario.getLoginUsuario()));
	    forma.getDtoHallazgoProgramaServicio().setInstitucion(usuario.getCodigoInstitucionInt());
	    HallazgoVsProgramaServicio.guardar(forma.getDtoHallazgoProgramaServicio());
	    //codigo = HallazgoVsProgramaServicio.guardar(forma.getDtoHallazgoProgramaServicio());
//	    logger.info(">>> Codigo = "+codigo);
	    forma.setEstado("empezar"); 
	    forma.setMensaje("Proceso Realizado Exitosamente!");
	    return accionEmpezarHallazgo(mapping, forma, usuario);
	}

	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionIngresar(ActionMapping mapping,
			HallazgoProgramaServicioForm forma, UsuarioBasico usuario) {
		
		Connection con= UtilidadBD.abrirConexion();
		HallazgosOdontologicos hallazgo = new HallazgosOdontologicos();
		forma.setListaHallazgos(hallazgo.consultarHallazgosDentales(con, usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		forma.setMensaje("");
		return mapping.findForward("paginaPrincipal");
		
	}
	
	
	
	
	/**
	 * accionEmpezarHallazgo
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarHallazgo(ActionMapping mapping,
			HallazgoProgramaServicioForm forma, UsuarioBasico usuario) 
	{
		
		forma.resetEmpezar();
		
		if(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi)){
			forma.setTipodeRelacion("Programa");
		}
		else
		{
			
			forma.setTipodeRelacion("Servicio");
		}
		
		
		
		DtoHallazgoVsProgramaServicio dtoWhere = new DtoHallazgoVsProgramaServicio();
		dtoWhere.setInstitucion(usuario.getCodigoInstitucionInt());
		forma.setListaHallazgoProgramaServicio(HallazgoVsProgramaServicio.cargar(dtoWhere));
		String listado="";
	    forma.resetEmpezar();
		forma.setListaHallazgoProgramaServicio(HallazgoVsProgramaServicio.cargar(new DtoHallazgoVsProgramaServicio()));
		
		if(forma.getListaHallazgoProgramaServicio().size() >0)
		
			{
			
				for(int w=0; w<forma.getListaHallazgoProgramaServicio().size(); w++)
				{
					if(w>0)
					{
						listado+=",";
					}
					listado+=forma.getListaHallazgoProgramaServicio().get(w).getHallazgo().getCodigo()+"";
				}
			}
		
		
//		logger.info("***************************** CONCATENANDO EL CODIGO ***************************************");
		forma.setCodigoHallazgos(listado);
//		logger.info("CODIGOS :"+listado);
		return mapping.findForward("paginaPrincipal");
	}
	
	
	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping,
			HallazgoProgramaServicioForm forma, UsuarioBasico usuario) {
		
		forma.reset();
		
		if(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi)){
			forma.setTipodeRelacion("Programa");
		}
		else
		{
			
			forma.setTipodeRelacion("Servicio");
		}
		
		
		
		DtoHallazgoVsProgramaServicio dtoWhere = new DtoHallazgoVsProgramaServicio();
		dtoWhere.setInstitucion(usuario.getCodigoInstitucionInt());
		forma.setListaHallazgoProgramaServicio(HallazgoVsProgramaServicio.cargar(dtoWhere));
		String listado="";
	    forma.reset();
		forma.setListaHallazgoProgramaServicio(HallazgoVsProgramaServicio.cargar(new DtoHallazgoVsProgramaServicio()));
		
		if(forma.getListaHallazgoProgramaServicio().size() >0)
		
			{
			
				for(int w=0; w<forma.getListaHallazgoProgramaServicio().size(); w++)
				{
					if(w>0)
					{
						listado+=",";
					}
					listado+=forma.getListaHallazgoProgramaServicio().get(w).getHallazgo().getCodigo()+"";
				}
			}
		
		
//		logger.info("***************************** CONCATENANDO EL CODIGO ***************************************");
		forma.setCodigoHallazgos(listado);
//		logger.info("CODIGOS :"+listado);
		return mapping.findForward("paginaPrincipal");
	}
	
	
}
