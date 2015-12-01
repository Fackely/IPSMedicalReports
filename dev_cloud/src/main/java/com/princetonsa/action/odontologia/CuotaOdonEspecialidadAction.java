package com.princetonsa.action.odontologia;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadLog;

import com.princetonsa.actionform.odontologia.CuotaOdonEspecialidadForm;
import com.princetonsa.dto.odontologia.DtoDetalleCuotasOdontoEsp;
import com.princetonsa.enums.odontologia.ETiposCuota;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.orm.CuotasOdontEspecialidad;
import com.servinte.axioma.orm.DetalleCuotasOdontoEsp;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.orm.delegate.administracion.InstitucionDelegate;
import com.servinte.axioma.servicio.fabrica.administracion.EspecialidadServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.cuotaOdontologica.CuotaOdontologicaServicioFabrica;
import com.servinte.axioma.servicio.interfaz.administracion.IEspecialidadServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.cuotaOndotologica.ICuotaOdonEspecialidadServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.cuotaOndotologica.IDetalleCuotaOdonEspeServicio;
import com.servinte.axioma.vista.odontologia.recomendaciones.CuotasOdontoEspecialidadView;



/**
 * 
 * @author axioma
 *
 */
public class CuotaOdonEspecialidadAction extends Action 

{
	
	/**
	 * 
	 */
	
	
	private IEspecialidadServicio especialidadS= EspecialidadServicioFabrica.crearEspecialidadServicio();
	private ICuotaOdonEspecialidadServicio cuotasServicio= CuotaOdontologicaServicioFabrica.crearCuotaOdonEspecialidad();
	private IDetalleCuotaOdonEspeServicio detalleServicio =CuotaOdontologicaServicioFabrica.crearDetalleOdonEspecialidad(); 
	
	
	
	
	
	
	/**
	 * 
	 */
	public ActionForward execute(	ActionMapping mapping, ActionForm form, HttpServletRequest request, 	HttpServletResponse response) throws Exception
	{
		
		if(form instanceof CuotaOdonEspecialidadForm )
		{
		
			CuotaOdonEspecialidadForm forma = (CuotaOdonEspecialidadForm)form;
			/*
			 *INSTANCIA DE USUARIO BASICO 
			 */
			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			if(forma.getEstado().equals("empezar"))
			{
				accionEmpezar(forma, usuario);
				return mapping.findForward("paginaPrincipal");
			}
			
			else if(forma.getEstado().equals("consultarEspecialidad"))
			{
				accionConsultaEspecialidad(forma, usuario);
				accionRecalcularTamanio(forma);
				return mapping.findForward("paginaPrincipal");
			}
			
			
			else if(forma.getEstado().equals("adicionarLista"))
			{
				accionAdicionarListaDetalle(forma);
				return mapping.findForward("paginaPrincipal");
			}
			
			
			else if(forma.getEstado().equals("guardarEncabezado"))
			{
				accionGuardarEncabezado(forma, usuario);
				forma.setEstado("resumen"); // TODO FALTA LAS EXCEPTIONES 
				
				return mapping.findForward("paginaPrincipal");
			}
			
			
			else if(forma.getEstado().equals("guardarDetalle") )
			{
				
				/*
				 *ARMAR USARIO, HORA Y FECHA  
				 */
				forma.getDtoCuota().setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				forma.getDtoCuota().setHoraModifica(UtilidadFecha.getHoraActual());
				forma.getDtoCuota().setUsuarios(new Usuarios());
				forma.getDtoCuota().getUsuarios().setLogin(usuario.getLoginUsuario());
				forma.getDtoCuota().setInstituciones(new Instituciones());
				forma.getDtoCuota().getInstituciones().setCodigo(usuario.getCodigoInstitucionInt());
				
				/*
				 * INSERTAR
				 */
				cuotasServicio.insertar(forma.getDtoCuota() , forma.getListaDetallesEspecialidad());
				/*
				 * CONSULTAR REGISTRO 
				 */
			  	CuotasOdontEspecialidad cuota=cuotasServicio.buscaxId(forma.getDtoCuota().getCodigoPk());
			  	
			  	/*
			  	 *ARMAR LOG 
			  	 */
			  	forma.setDtoLogCuotas2(CuotasOdontoEspecialidadView.armarDtoClon(cuota) );
			  	/*
			  	 * GENERAR LOG
			  	 */
				UtilidadLog.generarLog(usuario, forma.getDtoLogCuotas(),forma.getDtoLogCuotas2() ,  ConstantesBD.tipoRegistroLogModificacion , ConstantesBD.codigoCuotoOdontEspecialidad);
				
				
				
				forma.setEstado("resumen");
				
				return mapping.findForward("paginaPrincipal");
			}
		
			
			else if(forma.getEstado().equals("eliminarDetalle"))
			{
				
				accionEliminarDetalle(forma);
				return mapping.findForward("paginaPrincipal");
			}
		
			
			else if(forma.getEstado().equals("verificarDetalleCuota"))
			{
				return mapping.findForward("verficacion");
			}
			
			
			else if(forma.getEstado().equals("validacionEleccion"))
			{
				accionValidarEleccion(forma, usuario);
				return mapping.findForward("paginaPrincipal");	
			}
			
			else if(forma.getEstado().equals("modificarCuota"))
			{
				
				if( forma.getListaDetallesEspecialidad().size()>0 )
				{
					forma.setExisteEspecialidad(Boolean.TRUE);
				}
				else
				{
					forma.setEstado("empezar");
				}	
				
				
				return mapping.findForward("paginaPrincipal");	
				
			}
			
			
			
			
		}
		
		
		
		return null;
	}








	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 * @param usuario
	 */
	private void accionEmpezar(CuotaOdonEspecialidadForm forma,
			UsuarioBasico usuario) {
		forma.reset();
		accionCargarEspecialidades(forma, usuario);
		forma.setEstado("empezar");
	}







	/**
	 * ACCION CONSULTO ESPECIALIDAD 
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 * @param usuario
	 */
	private void accionConsultaEspecialidad(CuotaOdonEspecialidadForm forma, UsuarioBasico usuario) 
	{
		
		
		
		forma.setExisteEspecialidad(Boolean.FALSE);
		forma.setListaDetallesEspecialidad(new ArrayList<DtoDetalleCuotasOdontoEsp>());
		
		/*
		 * 1. Settear 
		 */
		CuotasOdontEspecialidad dtoCuota= new CuotasOdontEspecialidad();
		dtoCuota.setEspecialidades(new Especialidades());
		dtoCuota.getEspecialidades().setCodigo(forma.getDtoEspecialidad().getCodigo());
		dtoCuota.setInstituciones(new Instituciones());
		dtoCuota.getInstituciones().setCodigo(usuario.getCodigoInstitucionInt()); 
		
		
		/*
		 *2 .BUSCAR SI EXISTE UNA CUOTA CON ESTA ESPECIALIDAD  
		 */
		
		CuotasOdontEspecialidad dtoCuotaTmp=null;
		
		if( forma.getDtoEspecialidad().getCodigo()>0)
		{
			 dtoCuotaTmp= cuotasServicio.consultarAvanzadaCuotaEspecialidad(dtoCuota);
		}
		
		/*
		 *3 Validacion 
		 */
		if(dtoCuotaTmp!=null)
		{	
			 // modificar
			
			forma.setDtoCuota(dtoCuotaTmp);
			
			/*
			 * ARMARDO DTO LOG
			 */
			forma.setDtoLogCuotas(CuotasOdontoEspecialidadView.armarDtoClon(dtoCuotaTmp));
			
			
			
			
			
			if(forma.getDtoCuota().getEspecialidades()!=null && forma.getDtoCuota().getEspecialidades().getNombre()!=null )
			{
				forma.setNombreEspecialidad(forma.getDtoCuota().getEspecialidades().getNombre());
			}
			
			
			if(forma.getDtoCuota().getTipoValor()!=null)
			{
				forma.setNombreTipoCuota(ETiposCuota.cargarNombreEnum(forma.getDtoCuota().getTipoValor()));
			}
			
			if(forma.getDtoCuota().getTipoValor()!=null)
			{
				
				forma.setTipoCouta(forma.getDtoCuota().getTipoValor());
			}
			/*
			 *CARGAR LA CUOTAS 
			 */
			
			if( forma.getDtoCuota().getDetalleCuotasOdontoEsps()!=null)
			{
				accionArmarDetalle(forma);
			}
		
		}
		
		else 
		{
			forma.setEstado("empezar");
		}
	}







	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 */
	private void accionArmarDetalle(CuotaOdonEspecialidadForm forma) {
		
		
		Iterator it= forma.getDtoCuota().getDetalleCuotasOdontoEsps().iterator();
		
		forma.setListaDetallesEspecialidad(new ArrayList<DtoDetalleCuotasOdontoEsp>());
		
		
		while(it.hasNext())
		{
			 DetalleCuotasOdontoEsp entidad =(DetalleCuotasOdontoEsp)it.next();
			 DtoDetalleCuotasOdontoEsp dtoDetalle= new DtoDetalleCuotasOdontoEsp();
			 
			 dtoDetalle.setCodigoPk(entidad.getCodigoPk());
			 dtoDetalle.setNroCuotas(entidad.getNroCuotas());
			 dtoDetalle.setPorcentaje(entidad.getPorcentaje());
			 dtoDetalle.setTipoCuota(entidad.getTipoCuota());
			 dtoDetalle.setActivo(Boolean.TRUE);
			 dtoDetalle.setValor(entidad.getValor());
			 
			 forma.getListaDetallesEspecialidad().add(dtoDetalle);
		}

		
		/*
		 * ORDENAMIENTO
		 */
		if ( forma.getListaDetallesEspecialidad().size()>0 )
		{	
			SortGenerico sortG=new SortGenerico("AyudanteNroCuotas",true);
			Collections.sort(forma.getListaDetallesEspecialidad() ,sortG);
		}
	}







	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 */
	
	private void accionAdicionarListaDetalle(CuotaOdonEspecialidadForm forma) 
	{
		
		DtoDetalleCuotasOdontoEsp dtoDetCoutas  = new DtoDetalleCuotasOdontoEsp();
		forma.getListaDetallesEspecialidad().add(dtoDetCoutas);
		accionRecalcularTamanio(forma);
	}








	/**
	 * ACCION RECALCULAR
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 */
	private void accionRecalcularTamanio(CuotaOdonEspecialidadForm forma) 
	{
		
		int contador=0;
		
		for(DtoDetalleCuotasOdontoEsp dto: forma.getListaDetallesEspecialidad())
		{
			if( dto.getActivo() )
			{
				contador++;
			}
			
		}
		
		if(contador>=24)
		{
			forma.setTamanoListaMayo24(Boolean.TRUE);
		}
		else
		{
			forma.setTamanoListaMayo24(Boolean.FALSE);
		}
		
	}







	/**
	 * ACCION ELIMINAR DETALLE
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 */
	private void accionEliminarDetalle(CuotaOdonEspecialidadForm forma) 
	{
	
		DtoDetalleCuotasOdontoEsp dtoDetalleTmp = forma.getListaDetallesEspecialidad().get(forma.getPostArrayLisEspecialidad());
		if(dtoDetalleTmp.getCodigoPk()>0)
		{
			dtoDetalleTmp.setActivo(Boolean.FALSE);
		}
		else
		{
			forma.getListaDetallesEspecialidad().remove(forma.getPostArrayLisEspecialidad());
		}
		
		accionRecalcularTamanio(forma);
		
	}







	/**
	 * ACCION VALIDAR ELECCION
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 */
	private void accionValidarEleccion(CuotaOdonEspecialidadForm forma,  UsuarioBasico usuario) {
		
		
	
		
		/*
		 * TOMAR LA ESPECIALIDAD SELECCIONADA 
		 */
		for(Especialidades dtoEntidades:  forma.getListaEspecialidades() )
		{
			if( forma.getDtoEspecialidad().getCodigo()==dtoEntidades.getCodigo())
			{
				forma.getDtoCuota().setEspecialidades(dtoEntidades);
			}
		}
		
		/*
		 * SETTER EL DTO CUOTA
		 */
		forma.getDtoCuota().setTipoValor(forma.getTipoCouta());
		forma.getDtoCuota().setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		forma.getDtoCuota().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoCuota().setUsuarios( new UsuariosDelegate().findById(usuario.getLoginUsuario()));
		forma.getDtoCuota().setInstituciones(new Instituciones());
		forma.getDtoCuota().getInstituciones().setCodigo(usuario.getCodigoInstitucionInt());
				
		cuotasServicio.insertar(forma.getDtoCuota());
		
		
		if(forma.getBorraDetalle().equals(ConstantesBD.acronimoSi))
		{
			
			detalleServicio.eliminarDetallesxCuota(forma.getDtoCuota());
			
			forma.setListaDetallesEspecialidad(new ArrayList<DtoDetalleCuotasOdontoEsp>());
			forma.setEstado("empezarDetalle");
		}
		
		else if(forma.getBorraDetalle().equals(ConstantesBD.acronimoNo))
		{
			//SETTEAR INFORMACION 
			accionArmarDetalle(forma);
			forma.setEstado("empezarDetalle");
		}
	}

	
	




	/**
	 * ACCION GUARDAR  ENCABEZADO
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 * @param usuario
	 */
	private void accionGuardarEncabezado(CuotaOdonEspecialidadForm forma, UsuarioBasico usuario) 
	{
	
		/*
		 * TOMAR LA ESPECIALIDAD SELECCIONADA 
		 */
		for(Especialidades dtoEntidades:  forma.getListaEspecialidades() )
		{
			if( forma.getDtoEspecialidad().getCodigo()==dtoEntidades.getCodigo())
			{
				forma.getDtoCuota().setEspecialidades(dtoEntidades);
			}
		}
		
		/*
		 * SETTER EL DTO CUOTA
		 */
		forma.getDtoCuota().setTipoValor(forma.getTipoCouta());
		forma.getDtoCuota().setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		forma.getDtoCuota().setHoraModifica(UtilidadFecha.getHoraActual());
		forma.getDtoCuota().setUsuarios( new UsuariosDelegate().findById(usuario.getLoginUsuario()));
		forma.getDtoCuota().setInstituciones(new InstitucionDelegate().findById(usuario.getCodigoInstitucionInt()));
		
		

		/*
		 *INSERTAR   
		 */
		cuotasServicio.insertar(forma.getDtoCuota());
		/*
		 *	LOG CUOTA 2 
		 */
		
		/*
		 * ATRIBUTOS DE PRESENTACION
		 */
		forma.setNombreEspecialidad(forma.getDtoCuota().getEspecialidades().getNombre());
		forma.setNombreTipoCuota(ETiposCuota.cargarNombreEnum(forma.getDtoCuota().getTipoValor()));
		
	}





	
	/**
	 * ACCION CARGAR ESPECIALIDAD 
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 * @param usuario
	 */
	private void accionCargarEspecialidades(CuotaOdonEspecialidadForm forma, UsuarioBasico usuario) 
	{
	
		Especialidades dtoEspecialidades = new Especialidades();
		dtoEspecialidades.setInstituciones(new  Instituciones());
		dtoEspecialidades.setTipoEspecialidad(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica);
		dtoEspecialidades.getInstituciones().setCodigo(usuario.getCodigoInstitucionInt());
		forma.setListaEspecialidades(especialidadS.listarEspe(dtoEspecialidades));
		
	}
	
	


	
	
}
