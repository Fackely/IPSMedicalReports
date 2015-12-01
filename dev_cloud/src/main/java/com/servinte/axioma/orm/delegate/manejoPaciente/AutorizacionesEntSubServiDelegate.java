package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.UtilidadTexto;

import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.AutorizacionesEntSubServiHome;

/**
 * @author Cristhian Murillo
 * 
 *         Clase que contiene logica del negocio sobre el modelo
 */
@SuppressWarnings("unchecked")
public class AutorizacionesEntSubServiDelegate extends
		AutorizacionesEntSubServiHome {

	/**
	 * Lista todos AutorizacionesEntSubServi
	 * 
	 * @author Cristhian Murillo
	 * @return ArrayList<AutorizacionesEntSubServi>
	 */
	public ArrayList<AutorizacionesEntSubServi> listarTodosAutorizacionesEntSubServi() {
		return (ArrayList<AutorizacionesEntSubServi>) sessionFactory
				.getCurrentSession()

				.createCriteria(AutorizacionesEntSubServi.class)

				.list();
	}

	/**
	 * Lista por Autorizacion entidad subcontratada.
	 * 
	 * @author Cristhian Murillo
	 * @param dtoParametros
	 * @return ArrayList<AutorizacionesEntSubServi>
	 */
	public ArrayList<AutorizacionesEntSubServi> listarAutorizacionesEntSubServiPorAutoEntSub(
			DtoAutorizacionEntSubcontratadasCapitacion dtoParametros) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AutorizacionesEntSubServi.class, "autorizacionesEntSubServi");

		criteria.createAlias("autorizacionesEntSubServi.autorizacionesEntidadesSub", "autorizacionesEntidadesSub");

		criteria.add(Restrictions.eq("autorizacionesEntidadesSub.consecutivo", dtoParametros.getAutorizacion()));

		return (ArrayList<AutorizacionesEntSubServi>) criteria.list();
	}

	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos un registro de
	 * servicio de una autorización de entidad subcontratada
	 * 
	 * @param AutorizacionesEntSubServi
	 *            servicio
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 * 
	 */
	public boolean guardarServicioAutorizacionEntidadSub(
			AutorizacionesEntSubServi servicio) {
		/**
		 * Se elimina la captura de la excepción en este nivel, ya que no se manejan excepciones por cada transacción
		 * de esta manera se deja la captura de la excepción en el mundo.
		 */
		boolean save = true;
		super.persist(servicio);
		return save;
	}

	/**
	 * Este metodo permite modificar un valor para el registro previamente ingresado
	 * @param AutorizacionesEntSubServi servicio
	 * @return
	 * @author Diana Ruiz
	 */
	
	public boolean modificarServicioAutorizacionEntidadSub(
			AutorizacionesEntSubServi servicio) {
		boolean save = true;
		try {
			super.merge(servicio);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro del "
					+ "servicio de la autorizacion de entidad subcontratada: ",
					e);
		}
		return save;
	}
	
	
	
	
	/**
	 * 
	 * Este Método se encarga de consultar los datos de los servicios
	 * de una autorización de capitación, a través del id de su respectiva
	 * autorización de entidad subcontratada y el tipo tarifario del servicio
	 * 
	 * @param long idAutorEntSub, String tipoTarifario
	 * @return ArrayList<DtoServiciosAutorizaciones>
	 * @author Angela Maria Aguirre
	 * 
	 */
	public ArrayList<DtoServiciosAutorizaciones> obtenerDetalleServiciosAutorCapitacion(long idAutorEntSub,String tipoTarifario) {
			
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AutorizacionesEntSubServi.class, "autorizacionesEntSubServi");
		criteria.createAlias("autorizacionesEntSubServi.autorizacionesEntidadesSub","autorizacionesEntidadesSub");
		criteria.createAlias("autorizacionesEntSubServi.servicios","servicio");		
		criteria.createAlias("autorizacionesEntSubServi.nivelAutorizacion","nivelAutorizacion", Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionesEntSubServi.contratos","contratos", Criteria.LEFT_JOIN);
		
		//En caso de ser solicitud
		criteria.createAlias("autorizacionesEntidadesSub.autoEntsubSolicitudeses","autoentsubsolicitudes", Criteria.LEFT_JOIN);
		criteria.createAlias("autoentsubsolicitudes.solicitudes","solicitud", Criteria.LEFT_JOIN);
		criteria.createAlias("solicitud.diagnosticos","diagnostico", Criteria.LEFT_JOIN);
		//En caso de ser Orden Ambulatoria
		criteria.createAlias("autorizacionesEntidadesSub.autoEntsubOrdenambulas","autoentsubordenesamb", Criteria.LEFT_JOIN);
		criteria.createAlias("autoentsubordenesamb.ordenesAmbulatorias","ordenAmbulatoria", Criteria.LEFT_JOIN);
		criteria.createAlias("ordenAmbulatoria.diagnosticos","diagnosticoOrdenAmb", Criteria.LEFT_JOIN);
		//En caso de ser Peticion
		criteria.createAlias("autorizacionesEntidadesSub.autoEntsubPeticioneses","autoentsubpeticiones", Criteria.LEFT_JOIN);
		criteria.createAlias("autoentsubpeticiones.peticionQx","peticion", Criteria.LEFT_JOIN);
		criteria.createAlias("peticion.diagnosticos","diagnosticoPeticion", Criteria.LEFT_JOIN);
		//En caso de venir de Ingreso Estancia
		criteria.createAlias("autorizacionesEntidadesSub.autorizacionesCapitacionSubs","autorizacionCapitacion", Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionCapitacion.autorizacionesEstanciaCapitas","autorizacionEstanciaCapitacion", Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionEstanciaCapitacion.autorizacionesIngreEstancia","autorizacionIngresoEstancia", Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionIngresoEstancia.ingresosEstancia","ingresoEstancia", Criteria.LEFT_JOIN);
		criteria.createAlias("ingresoEstancia.diagnosticosByFkIeDxPpal","diagnosticoIngresoEstancia", Criteria.LEFT_JOIN);
		
		criteria.createAlias("servicio.referenciasServicios","referenciaServicio");
		criteria.createAlias("referenciaServicio.tarifariosOficiales","tarifarioOficial");	
		
		criteria.add(Restrictions.eq("autorizacionesEntidadesSub.consecutivo",idAutorEntSub));		
		
		if(!UtilidadTexto.isEmpty(tipoTarifario)){
			criteria.add(Restrictions.eq("tarifarioOficial.codigo",Integer.valueOf(tipoTarifario).intValue()));
		}		
				
		ProjectionList projectionList = Projections.projectionList();
		//Por si viene de una Solicitud
		projectionList.add(Projections.property("solicitud.consecutivoOrdenesMedicas"),"numeroOrden");
		projectionList.add(Projections.property("solicitud.fechaSolicitud"),"fechaOrden");
		//Por si viene de una Orden Ambulatoria
		projectionList.add(Projections.property("ordenAmbulatoria.codigo"),"numeroOrdenAmb");
		projectionList.add(Projections.property("ordenAmbulatoria.consecutivoOrden"),"consecutivoOrdenAmb");
		projectionList.add(Projections.property("ordenAmbulatoria.fecha"),"fechaOrdenAmb");
		
		projectionList.add(Projections.property("peticion.codigo"),"numeroPeticion");
		projectionList.add(Projections.property("peticion.fechaPeticion"),"fechaPeticion");
		projectionList.add(Projections.property("servicio.codigo"),"codigoServicio");
		projectionList.add(Projections.property("referenciaServicio.codigoPropietario"),"codigoPropietario");
		projectionList.add(Projections.property("referenciaServicio.descripcion"),"descripcionServicio");
		projectionList.add(Projections.property("autorizacionesEntSubServi.cantidad"),"cantidadAutorizadaServicio");
		projectionList.add(Projections.property("nivelAutorizacion.descripcion"),"descripcionNivelAutorizacion");
		projectionList.add(Projections.property("autorizacionesEntSubServi.valorTarifa"),"valorServicio");
		projectionList.add(Projections.property("contratos.codigo"),"contratoConvenioResponsable");
		
		//Por si viene de una Solicitud
		projectionList.add(Projections.property("diagnostico.id.acronimo"),"diagnostico");
		projectionList.add(Projections.property("diagnostico.id.tipoCie"),"tipoCieDx");
		projectionList.add(Projections.property("diagnostico.nombre"),"descripcionDiagnostico");
		//Por si viene de una Orden Ambulatoria
		projectionList.add(Projections.property("diagnosticoOrdenAmb.id.acronimo"),"diagnosticoOrdenAmb");
		projectionList.add(Projections.property("diagnosticoOrdenAmb.id.tipoCie"),"tipoCieDxOrdenAmb");
		projectionList.add(Projections.property("diagnosticoOrdenAmb.nombre"),"descripcionDiagnosticoOrdenAmb");
		//Por si viene de una Peticion
		projectionList.add(Projections.property("diagnosticoPeticion.id.acronimo"),"diagnosticoPeticion");
		projectionList.add(Projections.property("diagnosticoPeticion.id.tipoCie"),"tipoCieDxPeticion");
		projectionList.add(Projections.property("diagnosticoPeticion.nombre"),"descripcionDiagnosticoPeticion");
		//Por si viene de una Ingreso Estancia
		projectionList.add(Projections.property("diagnosticoIngresoEstancia.id.acronimo"),"diagnosticoIngEst");
		projectionList.add(Projections.property("diagnosticoIngresoEstancia.id.tipoCie"),"tipoCieDxIngEst");
		projectionList.add(Projections.property("diagnosticoIngresoEstancia.nombre"),"descripcionDiagnosticoIngEst");
		projectionList.add(Projections.property("autorizacionesEntSubServi.cantidad"),"cantidadSolicitada");
		
		criteria.setProjection(projectionList);
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoServiciosAutorizaciones.class));
		ArrayList<DtoServiciosAutorizaciones> listaServicios = (ArrayList<DtoServiciosAutorizaciones>)criteria.list();
		
		
		return listaServicios;

	}
	

}
