package com.princetonsa.action.odontologia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.actionform.odontologia.RecomendacionesServicioProgramaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.helper.odontologia.RecomendaServicioProgramaHelper;
import com.servinte.axioma.orm.Programas;
import com.servinte.axioma.orm.RecomSerproSerpro;
import com.servinte.axioma.orm.RecomendacionesContOdonto;
import com.servinte.axioma.orm.RecomendacionesServProg;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.orm.delegate.administracion.InstitucionDelegate;
import com.servinte.axioma.servicio.fabrica.odontologia.recomendacion.RecomendacionSERVICIOFabrica;
import com.servinte.axioma.servicio.interfaz.odontologia.recomendacion.IRecomendacionSerProgSerProgSERVICIO;
import com.servinte.axioma.servicio.interfaz.odontologia.recomendacion.IRecomendacionServicioProgramaServicio;


/**
 * 
 * @author axioma
 *
 */
public class RecomendacionesServicioProgramaAction extends Action{
	
	
	/*
	 *INTERFAZ SERVICIO  
	 */
	IRecomendacionServicioProgramaServicio recomenSERVICIO = RecomendacionSERVICIOFabrica.crearRecomendacionServicioPrograma();
	IRecomendacionSerProgSerProgSERVICIO  recomenProgSer = RecomendacionSERVICIOFabrica.crearRecomendacionSerProgSerPro();
	
	
	
	
	/**
	 * 
	 */
	public ActionForward execute(	ActionMapping mapping, ActionForm form, HttpServletRequest request, 	HttpServletResponse response) throws Exception
	{
		
		/**
		 * 
		 */
		if(form instanceof RecomendacionesServicioProgramaForm)
		{
			
			
			RecomendacionesServicioProgramaForm forma = (RecomendacionesServicioProgramaForm)form;
			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			ActionErrors errores = new ActionErrors();
			
			
			
			if(forma.getEstado().equals("empezar"))
			{
				forma.reset();
				
				return mapping.findForward("paginaPrincipal");
			}
			
			else if(forma.getEstado().equals("adicionarPrograma"))
			{
				accionAdicionarPrograma(request, forma, errores);
				return mapping.findForward("paginaPrincipal");
			}
			
			else if(forma.getEstado().equals("adicionarRecomendaciones"))
			{
				
				accionAdicionarRecomendaciones(forma);
				return mapping.findForward("paginaPrincipal");
			}
			
			else if(forma.getEstado().equals("eliminarProgramaServicio") )
			{
				accionEliminarProgramaServicios(forma);
				accionCargarCodigoProgramaservicios(forma);
				return mapping.findForward("paginaPrincipal");
			}
		
			else if(forma.getEstado().equals("eliminarRecomendacion"))
			{
				
				 accionEliminarRecomendaciones(forma);
				 accionCargarCodigosRecomendaciones(forma);
				 return mapping.findForward("paginaPrincipal");
			}
			
			
			else if(forma.getEstado().equals("guardar"))
			{
				forma.getDtoRecomenServicioPrograma().setUsuarios(new UsuariosDelegate().findById(usuario.getLoginUsuario()));
				forma.getDtoRecomenServicioPrograma().setFechaModifica(UtilidadFecha.getFechaActual());
				forma.getDtoRecomenServicioPrograma().setHoraModifica(UtilidadFecha.getHoraActual());
				forma.getDtoRecomenServicioPrograma().setInstituciones(new InstitucionDelegate().findById(usuario.getCodigoInstitucionInt()));
				
				recomenSERVICIO.guardarRecomendacionesServicioPrograma(forma.getDtoRecomenServicioPrograma());
				forma.setEstado("resumen");
				return mapping.findForward("paginaPrincipal");
			}
			
			
		
			
			
			
		}
			
	
		
		return null;
		
	}





	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 */
	private void accionEliminarProgramaServicios(
			RecomendacionesServicioProgramaForm forma) {
		Iterator iter = forma.getDtoRecomenServicioPrograma().getRecomSerproSerpros().iterator();
		 while (iter.hasNext())
		 {
			 RecomSerproSerpro dto = (RecomSerproSerpro)iter.next();
			 if( dto.getProgramas().getCodigo()==forma.getPostArrayServicioPrograma() )
			 {
				 iter.remove();
			 }
		 }
	}




	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 */
	private void accionEliminarRecomendaciones(
			RecomendacionesServicioProgramaForm forma) {
		Iterator iter = forma.getDtoRecomenServicioPrograma().getRecomendacionesContOdontos().iterator();
		 while(iter.hasNext())
		 {
			 RecomendacionesContOdonto dto = (RecomendacionesContOdonto)iter.next();
			 if(dto.getCodigoPk()==forma.getPostArrayRecomendacion())
			 {
				 iter.remove();
			 }
		 }
	}





	private void accionAdicionarRecomendaciones(RecomendacionesServicioProgramaForm forma) 
	{
		forma.getDtoRecomenServicioPrograma().getRecomendacionesContOdontos().add((Object)forma.getDtoRecomendaciones());
		//RecomendaServicioProgramaHelper.ordenamientoRecomendacionServicio(forma.getDtoRecomenServicioPrograma().getRecomendacionesContOdontos());
		forma.setDtoRecomendaciones(new RecomendacionesContOdonto());
		accionCargarCodigosRecomendaciones(forma);
	}




	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param request
	 * @param forma
	 * @param errores
	 */
	private void accionAdicionarPrograma(HttpServletRequest request,RecomendacionesServicioProgramaForm forma, ActionErrors errores) {
		
		RecomendacionesServProg dtotmp = new RecomendacionesServProg();
		
		dtotmp = recomenProgSer.consultarRecomendacionProgramaServicio(forma.getDtoSerProSerPro());
		
		/*
		 * 1. VALIDAR SI EXISTE INFORMACION EN LA BUSQUEDA 
		 */
		boolean listaDtoLLena=dtotmp.getRecomendacionesContOdontos().size()>0;
		
 
		/*
		 * 2. VALIDAR SI YA EXISTE INFORMACION DE RECOMENDACION CARGADA 
		 */
		if(forma.getDtoRecomenServicioPrograma().getRecomSerproSerpros().size()>0)
		{
			accionSettearRecomendacionServicio(request, forma, errores,	listaDtoLLena);
		}
		else
		{
			accionSetterNuevaRecomendacionServicio(forma, dtotmp,listaDtoLLena);
		}
		
		
		
	
		
		//sort= forma.getDtoRecomenServicioPrograma().getRecomSerproSerpros().;
		
		//dtotmp  (forma.getDtoRecomenServicioPrograma().getRecomSerproSerpros(),new  SortRecomenServicioPrograma());
	}



/**
 * 
 * @author Edgar Carvajal Ruiz
 * @param forma
 * @param dtotmp
 * @param listaDtoLLena
 */
	private void accionSetterNuevaRecomendacionServicio(
														RecomendacionesServicioProgramaForm forma,
														RecomendacionesServProg dtotmp, 
														boolean listaDtoLLena) {
		//3. SI NO EXISTE INFORMACION CARGAR
		
		
		//SI LA LISTA ES DIFERENTE DE VACIA
		if(listaDtoLLena)
		{
			forma.setDtoRecomenServicioPrograma(dtotmp);
			accionCargarCodigoProgramaservicios(forma);
			accionCargarCodigosRecomendaciones(forma);
			Set tmpHasSet= RecomendaServicioProgramaHelper.ordenamientoRecomendacionServicio(forma.getDtoRecomenServicioPrograma().getRecomSerproSerpros());
			forma.getDtoRecomenServicioPrograma().setRecomSerproSerpros(tmpHasSet);
		}
		else
		{
			
			forma.getDtoRecomenServicioPrograma().getRecomSerproSerpros().add((Object) forma.getDtoSerProSerPro() );
			forma.setDtoSerProSerPro(new RecomSerproSerpro());
			forma.getDtoSerProSerPro().setProgramas(new Programas());
			accionCargarCodigoProgramaservicios(forma);
			accionCargarCodigosRecomendaciones(forma);
		}
	}



/**
 * 
 * @author Edgar Carvajal Ruiz
 * @param request
 * @param forma
 * @param errores
 * @param listaDtoLLena
 */
	private void accionSettearRecomendacionServicio(HttpServletRequest request, 
													RecomendacionesServicioProgramaForm forma, 
													ActionErrors errores,
													boolean listaDtoLLena) {
		/*
		 * SI NO EXISTE RECOMENDACION CON PROGRAMA SERVICIO ADICCION A LA LISTA
		 */
		if(listaDtoLLena)
		{
			String tmpNombre =forma.getDtoSerProSerPro().getProgramas().getNombre();
			errores.add("", new ActionMessage("errors.notEspecific",	" Existe parametrización previa de recomendaciones para el programa o servicio "+tmpNombre+" seleccionado. Por favor verifique "));
			saveErrors(request, errores);
		}
		else
		{
			
			forma.getDtoRecomenServicioPrograma().getRecomSerproSerpros().add((Object) forma.getDtoSerProSerPro() );
			
			/*
			 * ordenamiento
			 */
			Set tmpHashSet=	RecomendaServicioProgramaHelper.ordenamientoRecomendacionServicio(forma.getDtoRecomenServicioPrograma().getRecomSerproSerpros());
			
			/*
			 * adicionamos nueva informacion
			 */
			forma.getDtoRecomenServicioPrograma().setRecomSerproSerpros(tmpHashSet);
			
			/*
			 * hacemos limpieza del metodo 
			 */
			forma.setDtoSerProSerPro(new RecomSerproSerpro());
			forma.getDtoSerProSerPro().setProgramas(new Programas());
			
			/*
			 *cargamos los nuevos codigos insertados de recomendaciones y servicios 
			 */
			accionCargarCodigoProgramaservicios(forma);
			accionCargarCodigosRecomendaciones(forma);
			
		}
	}

	
	

	
	
	/**
	 * METODO PARA LLENAR LOS CODIGOS DE PROGRAMAS O SERVICIOS EN EL ATRIBUTO LISTACODIGOPROGRAMAS
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 */
	private void accionCargarCodigoProgramaservicios(RecomendacionesServicioProgramaForm forma) 
	{
		//INSTANCIA DE ITERADOR
		Iterator iter = forma.getDtoRecomenServicioPrograma().getRecomSerproSerpros().iterator();
		forma.setListaCodigos(new ArrayList<Integer>()); //LIMPIAR LISTA
		
		//ITERACION
		while(iter.hasNext())
		{
			RecomSerproSerpro dto=  (RecomSerproSerpro)iter.next(); //CONVERTIR ITER A DTO
			forma.getListaCodigos().add((int)dto.getProgramas().getCodigo()) ; //ADICIONAR
		}
		
		forma.setListaCodigoProgramaServicios(UtilidadTexto.convertirArrayIntegerACodigosSeparadosXComas(forma.getListaCodigos()));
	}



	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 */
	private void accionCargarCodigosRecomendaciones(RecomendacionesServicioProgramaForm forma) 
	{
		
		
		Iterator iter = forma.getDtoRecomenServicioPrograma().getRecomendacionesContOdontos().iterator();
		
		forma.setListaCodigos(new ArrayList<Integer>()); //LIMPIAR LISTA
		
		
		while(iter.hasNext())
		{
			RecomendacionesContOdonto  dto = (RecomendacionesContOdonto)iter.next(); // iter
			forma.getListaCodigos().add(dto.getCodigoPk()); // ADAPTAR CODIGO LISTA 
		}
		
		forma.setListaCodigoRecomendaciones("");
		forma.setListaCodigoRecomendaciones(UtilidadTexto.convertirArrayIntegerACodigosSeparadosXComas(forma.getListaCodigos()))  ;
		
	}
	
	

}
