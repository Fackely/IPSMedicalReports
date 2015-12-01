/**
 * 
 */
package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;
import java.util.Iterator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.odontologia.UtilidadOdontologia;

import com.princetonsa.dto.odontologia.DtoFiltroConsultaServiciosPaciente;
import com.princetonsa.dto.odontologia.DtoMotivosCambioServicio;
import com.princetonsa.dto.odontologia.DtoProgHallazgoPieza;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosAnterioresCita;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosNuevosCita;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
import com.princetonsa.dto.odontologia.DtoSolictudCambioServicioCita;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.CitasOdontologicas;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.MotivosCambiosServicios;
import com.servinte.axioma.orm.ProgServAnterioresCita;
import com.servinte.axioma.orm.ProgServNuevosCita;
import com.servinte.axioma.orm.Programas;
import com.servinte.axioma.orm.ProgramasHallazgoPieza;
import com.servinte.axioma.orm.Servicios;
import com.servinte.axioma.orm.SolicitudCambioServicio;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.odontologia.MotivosCambioServicioDelegate;
import com.servinte.axioma.orm.delegate.odontologia.ProgServAnterioresCitaDelegate;
import com.servinte.axioma.orm.delegate.odontologia.SolicitudCambioServicioDelegate;

/**
 * @author armando
 *
 */
public class SolicitudCambioServicioMundo 
{

	/**
	 * 
	 * @param dtoSolicitud
	 * @return
	 */
	public static boolean generarSolicitudCambioServicio(DtoSolictudCambioServicioCita dtoSolicitud) 
	{
		try
		{
			HibernateUtil.beginTransaction();
			SolicitudCambioServicioDelegate dao=new SolicitudCambioServicioDelegate();
			SolicitudCambioServicio dto=new SolicitudCambioServicio();
			CitasOdontologicas cita=new CitasOdontologicas();
			cita.setCodigoPk(dtoSolicitud.getCita().getCodigoPk());
			dto.setCitasOdontologicas(cita);
			dto.setConfirmacionAutomatica(ConstantesBD.acronimoNo);
			dto.setEstado(dtoSolicitud.getEstado());
			dto.setFechaSolicita(UtilidadFecha.conversionFormatoFechaABD(dtoSolicitud.getFechaSolicitud()));
			dto.setHoraSolicita(dtoSolicitud.getHoraSolicitud().substring(0, 5));
			dto.setEstadoCita(dtoSolicitud.getEstadoCita());
			Usuarios usuario=new Usuarios();
			usuario.setLogin(dtoSolicitud.getUsuarioSolicita());
			dto.setUsuariosByUsuarioSolicita(usuario);
			dto.setObservacionesGenerales(dtoSolicitud.getObservacionesGenerales());
			Instituciones instituciones=new Instituciones();
			instituciones.setCodigo(dtoSolicitud.getInstitucion());
			dto.setInstituciones(instituciones);
			
			dao.persist(dto);
			if(dto.getCodigoPk()>0)
			{
				dtoSolicitud.setCodigoPk(dto.getCodigoPk());
				//insercion de los programas.servicios nuevos.
				for(DtoProgramasServiciosNuevosCita programas:dtoSolicitud.getProgServNuevos())
				{
					ProgServNuevosCitaDelegate daoDetalle=new ProgServNuevosCitaDelegate();
					ProgServNuevosCita dtoDetalle=new ProgServNuevosCita();
					dtoDetalle.setCambio(programas.getCambio());
					if(programas.getCambio().equals(ConstantesBD.acronimoSi))
					{
						MotivosCambioServicioDelegate daoMotivos=new MotivosCambioServicioDelegate();
						MotivosCambiosServicios motivos=daoMotivos.findById(programas.getMotivo().getCodigoPk());
						dtoDetalle.setMotivosCambiosServicios(motivos);
					}
					
					dtoDetalle.setObservaciones(programas.getObservaciones());
			
					if(programas.getCodigoPrograma()>0)
					{
						Programas programa=new Programas();
						programa.setCodigo(Long.valueOf(programas.getCodigoPrograma()));
						dtoDetalle.setProgramas(programa);
						ProgramasHallazgoPieza php=new ProgramasHallazgoPieza();
						php.setCodigoPk(programas.getProgramaHallazgoPieza().getCodigoPk());
						dtoDetalle.setProgramasHallazgoPieza(php);
						Servicios servicio=new Servicios();
						servicio.setCodigo(programas.getServicio().getCodigoServicio());
						dtoDetalle.setServicios(servicio);
						dtoDetalle.setSolicitudCambioServicio(dto);
					
						/*
						 * Se guarda el valor de la tarifa del servicio Nuevo
						 */
						DtoServicioOdontologico dtoServicio = programas.getServicio();
						
						if(dtoServicio!=null){
							
							if(dtoServicio.getInfoTarifa()!=null && dtoServicio.getInfoTarifa().getValorTarifaTotalConDctos()!=null){
								
								dtoDetalle.setValorUnitario(dtoServicio.getInfoTarifa().getValorTarifaTotalConDctos());
							}
						}

						daoDetalle.persist(dtoDetalle);
						if(dtoDetalle.getCodigoPk()>0)
						{
							programas.setCodigoPk(dtoDetalle.getCodigoPk());
						}
						else
						{
							HibernateUtil.abortTransaction();
							return false;
						}
					}
				}
				
				//inserción de los programas.servicios viejos.
				for(DtoProgramasServiciosAnterioresCita programas:dtoSolicitud.getProgServAnteriores())
				{
					ProgServAnterioresCitaDelegate daoDetalle=new ProgServAnterioresCitaDelegate();
					ProgServAnterioresCita dtoDetalle=new ProgServAnterioresCita();
					dtoDetalle.setCambio(programas.getCambio());
					if(programas.getCambio().equals(ConstantesBD.acronimoSi))
					{
						MotivosCambioServicioDelegate daoMotivos=new MotivosCambioServicioDelegate();
						MotivosCambiosServicios motivos=daoMotivos.findById(programas.getMotivo().getCodigoPk());
						dtoDetalle.setMotivosCambiosServicios(motivos);
					}
					dtoDetalle.setObservaciones(programas.getObservaciones());
					
					if(programas.getCodigoPrograma()>0)
					{
						Programas programa=new Programas();
						programa.setCodigo(Long.valueOf(programas.getCodigoPrograma()));
						dtoDetalle.setProgramas(programa);
						ProgramasHallazgoPieza php=new ProgramasHallazgoPieza();
						php.setCodigoPk(programas.getProgramaHallazgoPieza().getCodigoPk());
						dtoDetalle.setProgramasHallazgoPieza(php);
						Servicios servicio=new Servicios();
						servicio.setCodigo(programas.getServicio());
						dtoDetalle.setServicios(servicio);
						dtoDetalle.setSolicitudCambioServicio(dto);
						
						/*
						 * Se guarda el valor de la tarifa del servicio Nuevo
						 */
						
						DtoServicioOdontologico dtoServicio = programas.getDtoServicio();
						
						if(dtoServicio!=null){
							
							if(dtoServicio.getInfoTarifa()!=null && dtoServicio.getInfoTarifa().getValorTarifaTotalConDctos()!=null){
								
								dtoDetalle.setValorUnitario(dtoServicio.getInfoTarifa().getValorTarifaTotalConDctos());
							}
						}
						
						daoDetalle.persist(dtoDetalle);
						
						if(dtoDetalle.getCodigoPk()>0)
						{
							programas.setCodigoPk(dtoDetalle.getCodigoPk());
						}
						else
						{
							HibernateUtil.abortTransaction();
							return false;
						}
					}
				}
				
				HibernateUtil.endTransaction();
				return true;
			}
			else
			{
				HibernateUtil.abortTransaction();
				return false;
			}
		}
		catch(Exception e)
		{
			HibernateUtil.abortTransaction();
			e.printStackTrace();
			return false;
		}
		
	}

	/**
	 * 
	 * @param solicitud
	 */
	public static boolean confirmarSolicitudCambioServicio(
			DtoSolictudCambioServicioCita solicitud,String confirmacionAutomatica) 
	{
		try
		{
			HibernateUtil.beginTransaction();
			SolicitudCambioServicioDelegate dao=new SolicitudCambioServicioDelegate();
			SolicitudCambioServicio dto=new SolicitudCambioServicio();
			dto=dao.findById(solicitud.getCodigoPk());
			dto.setConfirmacionAutomatica(confirmacionAutomatica);
			dto.setEstado(solicitud.getEstado());
			dto.setFechaConfirma(UtilidadFecha.conversionFormatoFechaABD(solicitud.getFechaConfirma()));
			dto.setHoraConfirma(solicitud.getHoraConfirma().substring(0,5));
			Usuarios usuario=new Usuarios();
			usuario.setLogin(solicitud.getUsuarioConfirma());
			dto.setUsuariosByUsuarioConfirma(usuario);
			dao.persist(dto);
			HibernateUtil.endTransaction();
			return true;
			
		}
		catch(Exception e)
		{
			HibernateUtil.abortTransaction();
			e.printStackTrace();
			return false;
		}
	}

	public static DtoSolictudCambioServicioCita cargarSolicitudCambioServicio(int codigoSolitud,int codigoInstitucion) 
	{
		HibernateUtil.beginTransaction();
		DtoSolictudCambioServicioCita solicitud=new DtoSolictudCambioServicioCita();
		SolicitudCambioServicioDelegate dao=new SolicitudCambioServicioDelegate();
		SolicitudCambioServicio dto=new SolicitudCambioServicio();
		dto=dao.findById(codigoSolitud);
		solicitud.setCita(CitaOdontologica.obtenerCitaOdontologica((int)(dto.getCitasOdontologicas().getCodigoPk()), codigoInstitucion));
		solicitud.setCodigoPaciente(solicitud.getCita().getCodigoPaciente());
		solicitud.setCodigoPk(dto.getCodigoPk());
		solicitud.setConfirmacionAutomatica(dto.getConfirmacionAutomatica());
		solicitud.setEstado(dto.getEstado());
		if(solicitud.getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoConfirmado))
		{
			solicitud.setUsuarioConfirma(dto.getUsuariosByUsuarioConfirma().getLogin());
			solicitud.setFechaConfirma(UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaConfirma()));
			solicitud.setHoraConfirma(dto.getHoraConfirma());
		}
		solicitud.setFechaSolicitud(UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaSolicita()));
		solicitud.setHoraSolicitud(dto.getHoraSolicita());
		solicitud.setUsuarioSolicita(dto.getUsuariosByUsuarioSolicita().getLogin());
		solicitud.setInstitucion(codigoInstitucion);
		solicitud.setObservacionesGenerales(dto.getObservacionesGenerales());
		
		ArrayList<DtoServicioOdontologico> serviciosCita=cargarServiciosPaciente(dto,solicitud,codigoInstitucion);
		
		solicitud.setProgServAnteriores(cargarProgramaServiciosAnteriores(dto,serviciosCita));
		solicitud.setProgServNuevos(cargarProgramaServiciosNuevos(dto,solicitud,codigoInstitucion,serviciosCita));
		
		HibernateUtil.endTransaction();
		return solicitud;
	}

	/**
	 * 
	 * @param dto
	 * @param solicitud 
	 * @param codigoInstitucion 
	 * @param serviciosCita 
	 * @return
	 */
	private static ArrayList<DtoProgramasServiciosNuevosCita> cargarProgramaServiciosNuevos(SolicitudCambioServicio dto,DtoSolictudCambioServicioCita solicitud, int codigoInstitucion, ArrayList<DtoServicioOdontologico> serviciosCita) 
	{
		ArrayList<DtoProgramasServiciosNuevosCita> servicios=new ArrayList<DtoProgramasServiciosNuevosCita>();
		Iterator iterador=dto.getProgServNuevosCitas().iterator();
		while(iterador.hasNext())
		//for(ProgServNuevosCita servicio:(ArrayList<ProgServNuevosCita>)dto.getProgServNuevosCitas())
		{
			ProgServNuevosCita servicio=(ProgServNuevosCita)iterador.next();
			DtoProgramasServiciosNuevosCita detalle=new DtoProgramasServiciosNuevosCita();
			detalle.setCambio(servicio.getCambio());
			detalle.setCodigoPk(servicio.getCodigoPk());
			detalle.setCodigoPrograma((int)(servicio.getProgramas().getCodigo()));
			DtoMotivosCambioServicio motivo=new DtoMotivosCambioServicio();
			if(servicio.getMotivosCambiosServicios()!=null && servicio.getMotivosCambiosServicios().getCodigoPk()>0)
			{
				motivo.setCodigo(servicio.getMotivosCambiosServicios().getCodigo());
				motivo.setCodigoPk(servicio.getMotivosCambiosServicios().getCodigoPk());
				motivo.setDescripcion(servicio.getMotivosCambiosServicios().getDescripcion());
				motivo.setTipo(servicio.getMotivosCambiosServicios().getTipo());
			}
			detalle.setMotivo(motivo);
			detalle.setObservaciones(servicio.getObservaciones());
			DtoProgHallazgoPieza php=new DtoProgHallazgoPieza();
			php.setCodigoPk((int)(servicio.getProgramasHallazgoPieza().getCodigoPk()));
			detalle.setSolicitud(servicio.getSolicitudCambioServicio().getCodigoPk());
			cargarServicioProgramaDetalleNuevos(detalle,servicio,serviciosCita);
			servicios.add(detalle);
		}
		return servicios;
	}

	/**
	 * 
	 * @param detalle
	 * @param dto 
	 * @param serviciosCita
	 */
	private static void cargarServicioProgramaDetalleNuevos(DtoProgramasServiciosNuevosCita detalle,ProgServNuevosCita dto, ArrayList<DtoServicioOdontologico> serviciosCita) 
	{
		for(DtoServicioOdontologico servicio:serviciosCita)
		{
			if(dto.getProgramasHallazgoPieza().getCodigoPk()==servicio.getProgramaHallazgoPieza().getCodigoPk()&&dto.getServicios().getCodigo()==servicio.getCodigoServicio())
			{
				detalle.setProgramaHallazgoPieza(servicio.getProgramaHallazgoPieza());
				detalle.setServicio(servicio);
				break;
			}
		}
	}

	/**
	 * 
	 * @param dto 
	 * @param solicitud 
	 * @param detalle
	 * @param codigoInstitucion 
	 */
	private static ArrayList<DtoServicioOdontologico> cargarServiciosPaciente(SolicitudCambioServicio dto, DtoSolictudCambioServicioCita solicitud, int codigoInstitucion) 
	{
		DtoFiltroConsultaServiciosPaciente filtro=new DtoFiltroConsultaServiciosPaciente();
		filtro.setInstitucion(codigoInstitucion);
		filtro.setUnidadAgenda(solicitud.getCita().getAgendaOdon().getUnidadAgenda());
		filtro.setCodigoMedico(dto.getUsuariosByUsuarioSolicita().getPersonas().getCodigo());
		filtro.setCodigoPaciente(solicitud.getCita().getCodigoPaciente());
		String tipoServicio="";
		if(solicitud.getCita().getTipo().equals(ConstantesIntegridadDominio.acronimoTipoAtencionTratamiento))
		{
			tipoServicio="'"+ConstantesBD.codigoServicioProcedimiento+"', '"+ConstantesBD.codigoServicioCargosConsultaExterna+"'";
		}
		else
		{
			tipoServicio="'"+ConstantesBD.codigoServicioCargosConsultaExterna+"'";
		}
		//
		filtro.setTipoServicio(tipoServicio);
		filtro.setCodigoCita(solicitud.getCita().getCodigoPk());
		
		filtro.setActivo(true);
		filtro.setValidadrPresupuestoContratado(ConstantesBD.acronimoSi);
		filtro.setCambiarServicioOdontologico(ConstantesBD.acronimoNo);
		filtro.setCasoBusquedaServicio(ConstantesBD.acronimoConPlanTrataActivoEnProceso);
		filtro.setBuscarPlanTratamiento(true);
		ArrayList<DtoServicioOdontologico> servicios=UtilidadOdontologia.obtenerServiciosPaciente(filtro);
		return servicios;
		
	}

	/**
	 * 
	 * @param dto
	 * @param serviciosCita 
	 * @return
	 */
	private static ArrayList<DtoProgramasServiciosAnterioresCita> cargarProgramaServiciosAnteriores(SolicitudCambioServicio dto, ArrayList<DtoServicioOdontologico> serviciosCita) 
	{
		ArrayList<DtoProgramasServiciosAnterioresCita> servicios=new ArrayList<DtoProgramasServiciosAnterioresCita>();
		Iterator iterador=dto.getProgServAnterioresCitas().iterator();
		while(iterador.hasNext())
		//for(ProgServAnterioresCita servicio:(ArrayList<ProgServAnterioresCita>)dto.getProgServAnterioresCitas())
		{
			ProgServAnterioresCita servicio=(ProgServAnterioresCita)iterador.next();
			DtoProgramasServiciosAnterioresCita detalle=new DtoProgramasServiciosAnterioresCita();
			detalle.setCodigoPk(servicio.getCodigoPk());

			detalle.setCambio(servicio.getCambio());
			DtoMotivosCambioServicio motivo=new DtoMotivosCambioServicio();
			if(servicio.getMotivosCambiosServicios()!=null && servicio.getMotivosCambiosServicios().getCodigoPk()>0)
			{
				motivo.setCodigo(servicio.getMotivosCambiosServicios().getCodigo());
				motivo.setCodigoPk(servicio.getMotivosCambiosServicios().getCodigoPk());
				motivo.setDescripcion(servicio.getMotivosCambiosServicios().getDescripcion());
				motivo.setTipo(servicio.getMotivosCambiosServicios().getTipo());
			}
			detalle.setMotivo(motivo);
			detalle.setObservaciones(servicio.getObservaciones());
			
			detalle.setCodigoPrograma((int)(servicio.getProgramas().getCodigo()));
			detalle.setSolicitud((int)(servicio.getSolicitudCambioServicio().getCodigoPk()));
			cargarServicioProgramaDetalleAnteriores(detalle, servicio, serviciosCita);
			servicios.add(detalle);
		}
		return servicios;
	}
	
	/**
	 * 
	 * @param detalle
	 * @param dto 
	 * @param serviciosCita
	 */
	private static void cargarServicioProgramaDetalleAnteriores(DtoProgramasServiciosAnterioresCita detalle,ProgServAnterioresCita dto, ArrayList<DtoServicioOdontologico> serviciosCita) 
	{
		for(DtoServicioOdontologico servicio:serviciosCita)
		{
			if(dto.getProgramasHallazgoPieza().getCodigoPk()==servicio.getProgramaHallazgoPieza().getCodigoPk()&&dto.getServicios().getCodigo()==servicio.getCodigoServicio())
			{
				detalle.setProgramaHallazgoPieza(servicio.getProgramaHallazgoPieza());
				detalle.setDtoServicio(servicio);
				detalle.setServicio(servicio.getCodigoServicio());
				break;
			}
		}
	}

	public static boolean anularSolicitud(DtoSolictudCambioServicioCita solicitud) 
	{
		try
		{
			HibernateUtil.beginTransaction();
			SolicitudCambioServicioDelegate dao=new SolicitudCambioServicioDelegate();
			SolicitudCambioServicio dto=new SolicitudCambioServicio();
			dto=dao.findById(solicitud.getCodigoPk());
			dto.setEstado(solicitud.getEstado());
			dto.setFechaAnulacion(UtilidadFecha.conversionFormatoFechaABD(solicitud.getFechaAnulacion()));
			dto.setHoraAnulacion(solicitud.getHoraAnulacion().substring(0,5));
			Usuarios usuario=new Usuarios();
			usuario.setLogin(solicitud.getUsuarioAnulacion());
			dto.setUsuariosByUsuarioAnulacion(usuario);
			MotivosCambioServicioDelegate daoMotivos=new MotivosCambioServicioDelegate();
			MotivosCambiosServicios motivos=daoMotivos.findById(solicitud.getMotivoAnulacion());
			dto.setMotivosCambiosServicios(motivos);
			dao.persist(dto);
			HibernateUtil.endTransaction();
			return true;
			
		}
		catch(Exception e)
		{
			HibernateUtil.abortTransaction();
			e.printStackTrace();
			return false;
		}
	}
	

}
