package com.servinte.axioma.vista.odontologia.recomendaciones;

import java.util.List;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.actionform.odontologia.RecomendacionesContratoForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.excepcion.MundoRuntimeExcepcion;
import com.servinte.axioma.mundo.excepcion.autenticacion.VerificacionIngresoUnicosException;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.InstitucionesHome;
import com.servinte.axioma.orm.RecomendacionesContOdonto;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.servicio.excepcion.ServicioException;
import com.servinte.axioma.servicio.fabrica.odontologia.recomendacion.RecomendacionSERVICIOFabrica;
import com.servinte.axioma.servicio.interfaz.odontologia.recomendacion.IRecomendacionesContOdontoServicio;

/**
 * 
 * @author Edgar Carvajal
 *
 */
public class RecomendacionesContView 
{
	
	/**
	 * SERVICIO RECOMENDACIONES 
	 */
	private IRecomendacionesContOdontoServicio servicioRecomendacionI;
	
	
	/**
	 * CONSTRUTOR
	 */
	public RecomendacionesContView()
	{
		 this.servicioRecomendacionI = RecomendacionSERVICIOFabrica.crearRecomendacionesCont();
	}
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoRecomendaciones
	 * @param institucion
	 * @return
	 */
	public List<RecomendacionesContOdonto> cargarRecomendaciones(UsuarioBasico usuario)
	{
		RecomendacionesContOdonto dtoRecomendacion = new RecomendacionesContOdonto();
		List<RecomendacionesContOdonto> listaTmpRecomendacione= servicioRecomendacionI.obtenerRecomendacionesContrato(dtoRecomendacion, usuario.getCodigoInstitucionInt()); 
		return listaTmpRecomendacione;
	}
		
	
	/**
	 * METODO PARA GUARDAR LAS RECOMENDACIONES 
	 * @author Edgar Carvajal Ruiz
	 */
	public  ActionErrors guardarRecomendacion(UsuarioBasico usuario , RecomendacionesContratoForm forma )
	{
		
		/*
		 * SETTERA OBJETO RECOMENDACIONS 
		 */
		ActionErrors errores = new ActionErrors();
		
		
		forma.getDtoRecomendaciones().setUsuarios(new UsuariosDelegate().findById(usuario.getLoginUsuario()));
		forma.getDtoRecomendaciones().setFechaModifica( UtilidadFecha.getFechaActual());
		forma.getDtoRecomendaciones().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoRecomendaciones().setInstituciones(new  InstitucionesHome().findById(usuario.getCodigoInstitucionInt()) );
		
		/*
		 *GUARDAR RECOMENDACIONES
		 */
		try
		{
				
			 forma.getDtoRecomendaciones().setCodigo(UtilidadBD.obtenerSiguienteValorSecuencia("odontologia.seq_reccontodo_codigo")+"");
			 forma.getDtoRecomendaciones().setDescripcion(forma.getDtoRecomendaciones().getDescripcion().trim());
			 /*
			  * LLAMAR GUADAR RECOMENDACIONES 
			  */
			 servicioRecomendacionI.guardarRecomendaciones(forma.getDtoRecomendaciones());
		}
		
		catch (VerificacionIngresoUnicosException e) 
		{
			errores.add("",	new ActionMessage("errors.notEspecific",e.getMessage()));
		} 
		
		
		catch (ServicioException e) 
		{
			//TODO PENSAR ERRORES A NIVEL DE SERVICIO ???
		}
		
		return errores;
	}
	
	
	
	
	/**
	 * METODO PARA MODIFICAR LAS RECOMENDACIONES 
	 * @author Edgar Carvajal Ruiz
	 */
	public ActionErrors modificarRecomendacion(UsuarioBasico usuario , RecomendacionesContratoForm forma )
	{
		
		
		/*
		 * SETTERA OBJETO RECOMENDACIONS 
		 */
		ActionErrors errores = new ActionErrors();
		
		
		/*
		 * SETTERA OBJETO RECOMENDACIONS 
		 */
		forma.getDtoRecomendaciones().setUsuarios(new Usuarios());
		forma.getDtoRecomendaciones().getUsuarios().setLogin(usuario.getLoginUsuario());
		
		forma.getDtoRecomendaciones().setFechaModifica( UtilidadFecha.getFechaActual());
		forma.getDtoRecomendaciones().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoRecomendaciones().setInstituciones(new Instituciones());
		forma.getDtoRecomendaciones().getInstituciones().setCodigo(usuario.getCodigoInstitucionInt());
		
		forma.getDtoRecomendaciones().setCodigo(forma.getDtoRecomendaciones().getCodigo().trim());
		forma.getDtoRecomendaciones().setDescripcion(forma.getDtoRecomendaciones().getDescripcion().trim());
		
		/*
		 *GUARDAR RECOMENDACIONES
		 */
		
		try{
			
			servicioRecomendacionI.modificarRecomendaciones(forma.getDtoRecomendaciones());
		}
		
		catch (VerificacionIngresoUnicosException e) 
		{
			errores.add("",	new ActionMessage("errors.notEspecific",e.getMessage()));
		}
		catch (ServicioException e) {
		
		}
	
		
		
		return errores;
	}
	
	
	
	
	
	
	/**
	 * EL PROCESO NO ESTA DOCUMENTADO 
	 * METODO  PAR ELIMNAR RECOMENDACIONES 
	 * @author Edgar Carvajal Ruiz
	 */
	public ActionErrors eliminarRecomendacion(RecomendacionesContratoForm forma)
	{
		ActionErrors errores = new ActionErrors();
		
		RecomendacionesContOdonto recomendacion= forma.getListaRecomendaciones().get(forma.getPostArray()) ;
		try {
		
			servicioRecomendacionI.eliminarRecomendaciones(recomendacion);
		}
		catch (MundoRuntimeExcepcion e) {
			errores.add("",	new ActionMessage("errors.notEspecific",e.getMessage()));
		}
		catch (ServicioException e) 
		{
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
		
		return errores;
	}
	
	
	

}
