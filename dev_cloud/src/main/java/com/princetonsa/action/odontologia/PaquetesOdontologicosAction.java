/**
 * 
 */
package com.princetonsa.action.odontologia;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.odontologia.PaquetesOdontologicosForm;
import com.princetonsa.dto.odontologia.DtoPrograma;
import com.princetonsa.dto.odontologia.DtoProgramasPaqueteOdonto;
import com.princetonsa.dto.odontologia.DtoServiciosPaqueteOdon;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.Especialidades;
import com.princetonsa.mundo.odontologia.PaquetesOdontologicosMundo;
import com.princetonsa.mundo.odontologia.Programa;

/**
 * @author armando
 *
 */
public class PaquetesOdontologicosAction extends Action 
{
	
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(PaquetesOdontologicosAction.class);
	
	/**
	 * 
	 */
	public ActionForward execute(	ActionMapping mapping,
		 	ActionForm form,
		 	HttpServletRequest request,
		 	HttpServletResponse response) throws Exception
	{
		
	
		if(form instanceof PaquetesOdontologicosForm)
		{
			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			PaquetesOdontologicosForm forma=(PaquetesOdontologicosForm) form;
			logger.info("ESTADO: "+forma.getEstado());
			if(forma.getEstado().equals("empezarInsertar"))
			{
				forma.reset();
				forma.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
				forma.setEspecialidades(Especialidades.cargarEspecialidadesTipo(usuario.getCodigoInstitucionInt(), ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica));
				forma.setEsPorProgramas(UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt())));
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("insertar"))
			{
				if(PaquetesOdontologicosMundo.insertarPaquete(forma.getPaqueteOdontologico(),usuario))
				{
					forma.setMensaje(new ResultadoBoolean(true,"EL REGISTRO DEL PAQUETE ODONTOLOGICO \""+forma.getPaqueteOdontologico().getDescripcion()+"\" SE GUARDO EXITOSAMENTE."));
					return mapping.findForward("resumen");
				}
				else
				{
					forma.setMensaje(new ResultadoBoolean(true,"EL REGISTRO DEL PAQUETE ODONTOLOGICO NO SE PUDO GUARDAR. POR FAVOR VERIFIQUE."));
					return mapping.findForward("principal");
				}
			}
			else if(forma.getEstado().equals("cargarPrograma"))
			{
				DtoProgramasPaqueteOdonto dtoPrograma=new DtoProgramasPaqueteOdonto();
				DtoPrograma temporal=new DtoPrograma();
				temporal.setCodigo(forma.getCodigoPrograma());
				temporal.setInstitucion(usuario.getCodigoInstitucionInt());
				dtoPrograma.setPrograma(Programa.cargar(temporal).get(0));//buscamos por llave primaria, entonces siempre trae un solo registro.
				dtoPrograma.setCantidad(1);//postular el valor 1
				dtoPrograma.setExisteBD(false);
				forma.getPaqueteOdontologico().getProgramasPaqueteOdonto().add(dtoPrograma);
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("eliminarPrograma"))
			{
				forma.getPaqueteOdontologico().getProgramasPaqueteOdonto().remove(forma.getRegistroSeleccionado());
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("cargarServicio"))
			{
				DtoServiciosPaqueteOdon dtoServicio=new DtoServiciosPaqueteOdon();
				dtoServicio.setServicio(forma.getCodigoServicio());
				dtoServicio.setDescripcionServicio(forma.getNombreServicio());
				dtoServicio.setCantidad(1);//postular el valor 1
				dtoServicio.setExisteBD(false);
				forma.getPaqueteOdontologico().getServiciosPaqueteOdonto().add(dtoServicio);
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("eliminarServicio"))
			{
				forma.getPaqueteOdontologico().getServiciosPaqueteOdonto().remove(forma.getRegistroSeleccionado());
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("empezarConsulta"))
			{
				forma.reset();
				forma.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
				forma.setEspecialidades(Especialidades.cargarEspecialidadesTipo(usuario.getCodigoInstitucionInt(), ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica));
				forma.setEsPorProgramas(UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt())));
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("consultar"))
			{
				forma.setArrayPaquetesOdontologicos(PaquetesOdontologicosMundo.cargarPaquetesOdontologico(forma.getCodigoPaqueteConsulta(),forma.getDescripcionPaqueteConsulta(),forma.getCodigoEspecialidadConsulta()));
				if(forma.getArrayPaquetesOdontologicos().size()<=0)
					forma.setMensaje(new ResultadoBoolean(true,"No existen registros en el sistema que coincidan con la informaci&oacute;n solicitada. Por favor Verifique."));
				else
					forma.setMensaje(new ResultadoBoolean(false));
				if(forma.getArrayPaquetesOdontologicos().size()==1)
				{
					forma.setPaqueteOdontologico(PaquetesOdontologicosMundo.cargarPaqueteOdontologico(forma.getArrayPaquetesOdontologicos().get(0).getCodigoPk()));
					return mapping.findForward("resumen");
				}
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("detallePaquete"))
			{
				forma.setPaqueteOdontologico(PaquetesOdontologicosMundo.cargarPaqueteOdontologico(forma.getArrayPaquetesOdontologicos().get(forma.getRegistroSeleccionado()).getCodigoPk()));
				return mapping.findForward("resumen");
			}
			else if(forma.getEstado().equals("empezarModificacion"))
			{
				forma.reset();
				forma.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
				forma.setEspecialidades(Especialidades.cargarEspecialidadesTipo(usuario.getCodigoInstitucionInt(), ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica));
				forma.setEsPorProgramas(UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt())));
				return mapping.findForward("empezarConsulta");
			}
			else if(forma.getEstado().equals("consultarModificacion"))
			{
				return accionConsultarModificacion(forma,mapping);
			}
			else if(forma.getEstado().equals("eliminarPaqueteModificacion"))
			{
				PaquetesOdontologicosMundo.eliminarPaqueteOdontologico(forma.getArrayPaquetesOdontologicos().get(forma.getRegistroSeleccionado()).getCodigoPk());
				return accionConsultarModificacion(forma,mapping);
			}
			else if(forma.getEstado().equals("cargarModificacion"))
			{
				forma.setPaqueteOdontologico(PaquetesOdontologicosMundo.cargarPaqueteOdontologico(forma.getArrayPaquetesOdontologicos().get(forma.getRegistroSeleccionado()).getCodigoPk()));
				forma.setCodigoPaqueteModificacion(forma.getPaqueteOdontologico().getCodigo());
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("guardarModificacion"))
			{
				if(PaquetesOdontologicosMundo.modificarPaquete(forma.getPaqueteOdontologico(),usuario))
				{
					forma.setMensaje(new ResultadoBoolean(true,"EL REGISTRO DEL PAQUETE ODONTOLOGICO \""+forma.getPaqueteOdontologico().getDescripcion()+"\" SE GUARDO EXITOSAMENTE."));
					return mapping.findForward("resumen");
				}
				else
				{
					forma.setMensaje(new ResultadoBoolean(true,"EL REGISTRO DEL PAQUETE ODONTOLOGICO NO SE PUDO GUARDAR. POR FAVOR VERIFIQUE."));
					return mapping.findForward("principal");
				}
			}
			else if(forma.getEstado().equals("eliminarProgramaModificacion"))
			{
				if(PaquetesOdontologicosMundo.eliminarPrograma(forma.getPaqueteOdontologico().getProgramasPaqueteOdonto().get(forma.getRegistroSeleccionado()).getCodigoPk()))
				{
					forma.setMensaje(new ResultadoBoolean(true,"Registro Eliminado Exitosamente."));
				}
				else
				{
					forma.setMensaje(new ResultadoBoolean(true,"El registro no se pudo eliminar. Por favor Verifique."));
				}
				forma.setPaqueteOdontologico(PaquetesOdontologicosMundo.cargarPaqueteOdontologico(forma.getPaqueteOdontologico().getCodigoPk()));
				forma.setCodigoPaqueteModificacion(forma.getPaqueteOdontologico().getCodigo());
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("eliminarServicioModificacion"))
			{
				if(PaquetesOdontologicosMundo.eliminarServicio(forma.getPaqueteOdontologico().getServiciosPaqueteOdonto().get(forma.getRegistroSeleccionado()).getCodigoPk()))
				{
					forma.setMensaje(new ResultadoBoolean(true,"Registro Eliminado Exitosamente."));
				}
				else
				{
					forma.setMensaje(new ResultadoBoolean(true,"El registro no se pudo eliminar. Por favor Verifique."));
				}
				return mapping.findForward("principal");
			}
			else if(forma.getEstado().equals("volverEmpezarConsulta"))
			{
				return mapping.findForward("empezarConsulta");
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * 
	 * @param forma
	 * @param mapping 
	 */
	private ActionForward accionConsultarModificacion(PaquetesOdontologicosForm forma, ActionMapping mapping) 
	{
		forma.setArrayPaquetesOdontologicos(PaquetesOdontologicosMundo.cargarPaquetesOdontologico(forma.getCodigoPaqueteConsulta(),forma.getDescripcionPaqueteConsulta(),forma.getCodigoEspecialidadConsulta()));
		if(forma.getArrayPaquetesOdontologicos().size()<=0)
			forma.setMensaje(new ResultadoBoolean(true,"No existen registros en el sistema que coincidan con la informaci&oacute;n solicitada. Por favor Verifique."));
		else
			forma.setMensaje(new ResultadoBoolean(false));
		if(forma.getArrayPaquetesOdontologicos().size()==1)
		{
			forma.setPaqueteOdontologico(PaquetesOdontologicosMundo.cargarPaqueteOdontologico(forma.getArrayPaquetesOdontologicos().get(0).getCodigoPk()));
			return mapping.findForward("principal");
		}
		return mapping.findForward("empezarConsulta");
	}

}
