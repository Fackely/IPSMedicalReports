package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.inventario.DtoClaseInventario;
import com.servinte.axioma.orm.AutorizacionesEntSubArticu;
import com.servinte.axioma.orm.AutorizacionesEntSubArticuHome;
import com.servinte.axioma.orm.delegate.inventario.ClaseInventarioDelegate;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
@SuppressWarnings("unchecked")
public class AutorizacionesEntSubArticuDelegate extends AutorizacionesEntSubArticuHome
{
	
	/**
	 * Lista todos AutorizacionesEntSubServi
	 * @author Cristhian Murillo
	 * @return ArrayList<AutorizacionesEntSubArticu>
	 */
	public ArrayList<AutorizacionesEntSubArticu> listarTodosAautorizacionesEntSubArticu()
	{
		return (ArrayList<AutorizacionesEntSubArticu>) sessionFactory.getCurrentSession()
		
			.createCriteria(AutorizacionesEntSubArticu.class)
		
			.list();
	}
	
	
	/**
	 * Lista por Autorizacionentidad subcontratada.
	 * @author Cristhian Murillo
	 * @param dtoParametros
	 * @return ArrayList<DtoArticulosAutorizaciones>
	 */
	public ArrayList<DtoArticulosAutorizaciones> listarautorizacionesEntSubArticuPorAutoEntSub(DtoAutorizacionEntSubcontratadasCapitacion dtoParametros)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AutorizacionesEntSubArticu.class, "autorizacionesEntSubArticu");
		
		criteria.createAlias("autorizacionesEntSubArticu.autorizacionesEntidadesSub"		, "autorizacionesEntidadesSub"		, Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionesEntSubArticu.articulo"							, "articulo"						, Criteria.LEFT_JOIN);
		criteria.createAlias("articulo.unidadMedida"										, "unidadMedida"					, Criteria.LEFT_JOIN);
		criteria.createAlias("articulo.naturalezaArticulo"									, "naturalezaArticulo"				, Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionesEntSubArticu.autorizacionArticuloDespachos"		, "autorizacionArticuloDespachos"	, Criteria.LEFT_JOIN);
		criteria.createAlias("autorizacionArticuloDespachos.articuloByArticuloAutorizado"	, "articuloByArticuloAutorizado"	, Criteria.LEFT_JOIN);
		criteria.createAlias("articulo.formaFarmaceutica"									, "formaFarmaceutica"				, Criteria.LEFT_JOIN);
		
		
		criteria.add(Restrictions.eq("autorizacionesEntidadesSub.consecutivo"		, dtoParametros.getAutorizacion()));
		
		ProjectionList projectionList = Projections.projectionList(); 
		{
			projectionList.add(Projections.property("autorizacionesEntidadesSub.consecutivo") , "autorizacion");
			
			projectionList.add(Projections.property("autorizacionesEntSubArticu.codigoPk") 		, "autorizacionPropia");
			
			//--Medicamento
			projectionList.add(Projections.property("articulo.codigo")				, "codigoArticulo");
			projectionList.add(Projections.property("articulo.descripcion")			, "descripcionArticulo");
			projectionList.add(Projections.property("articulo.concentracion")		, "concentracionArticulo");
			projectionList.add(Projections.property("formaFarmaceutica.nombre")	, "formaFarmaceuticaArticulo");
			projectionList.add(Projections.property("unidadMedida.acronimo")		, "unidadMedidaArticulo");
			
			//- Formulación 
			projectionList.add(Projections.property("autorizacionesEntSubArticu.dosis")				, "dosisFormulacion");
			projectionList.add(Projections.property("autorizacionesEntSubArticu.frecuencia")		, "frecuenciaFormulacion");
			projectionList.add(Projections.property("autorizacionesEntSubArticu.via")				, "viaFormulacion"); 
			projectionList.add(Projections.property("autorizacionesEntSubArticu.diasTratamiento")	, "diasTratamientoFormulacion");
			projectionList.add(Projections.property("autorizacionesEntSubArticu.nroDosisTotal")		, "totalUnidadesFormulacion");
			projectionList.add(Projections.property("autorizacionesEntSubArticu.valorTarifa")		, "valorArticulo");
			
			//-- Generales
			projectionList.add(Projections.sum("autorizacionArticuloDespachos.cantidadEquivalente"), "despachoTotal");
			//projectionList.add(Projections.property(""), "despachoEntregar");						//este valor es para ingresar
			//projectionList.add(Projections.property(""), "observacionesArticulo"); 				//este valor es para ingresar
			projectionList.add(Projections.property("naturalezaArticulo.esMedicamento")				, "esMedicamento");
			
			// Se agrupan todas las propiedades para hacer Projections.sum
			projectionList.add( Projections.groupProperty("autorizacionesEntidadesSub.consecutivo"));
			projectionList.add( Projections.groupProperty("autorizacionesEntSubArticu.codigoPk"));
			projectionList.add( Projections.groupProperty("articulo.codigo"));
			projectionList.add( Projections.groupProperty("articulo.descripcion"));
			projectionList.add( Projections.groupProperty("articulo.concentracion"));
			projectionList.add( Projections.groupProperty("formaFarmaceutica.nombre"));
			projectionList.add( Projections.groupProperty("unidadMedida.acronimo"));
			projectionList.add( Projections.groupProperty("autorizacionesEntSubArticu.dosis"));
			projectionList.add( Projections.groupProperty("autorizacionesEntSubArticu.frecuencia"));
			projectionList.add( Projections.groupProperty("autorizacionesEntSubArticu.via"));
			projectionList.add( Projections.groupProperty("autorizacionesEntSubArticu.diasTratamiento"));
			projectionList.add( Projections.groupProperty("autorizacionesEntSubArticu.nroDosisTotal"));
			projectionList.add( Projections.groupProperty("autorizacionesEntSubArticu.valorTarifa"));
			projectionList.add( Projections.groupProperty("naturalezaArticulo.esMedicamento"));

			criteria.setProjection(projectionList);
		}
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoArticulosAutorizaciones.class));
		
		return (ArrayList<DtoArticulosAutorizaciones>)criteria.list();
	}
	
	
	
	/**
	 * Metodo de la super clase finById
	 * @param id
	 * @return AutorizacionesEntSubArticu
	 */
	public AutorizacionesEntSubArticu obtenerAutorizacionesEntSubArticuPorId(long id) {
		return super.findById(id);
	}

	
	
	/**
	 * Metodo de la super clase attachDirty
	 * @param instance
	 */
	public void attachDirtyAutorizacionesEntSubArticu(AutorizacionesEntSubArticu instance) {
		super.attachDirty(instance);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos un registro de
	 * artículo de una autorización de entidad subcontratada
	 * 
	 * @param AutorizacionesEntSubArticu artículo
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarArticuloAutorizacionEntidadSub(AutorizacionesEntSubArticu articulo){
		boolean save = true;					
		try{
			super.persist(articulo);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro del " +
					"articulo de la autorizacion de entidad subcontratada: ",e);
		}				
		return save;				
	} 
	
	/**
	 * 
	 * Este Método se encarga de consultar los datos de los artículos
	 * de una autorización de capitación, a través del id de su respectiva
	 * autorización de entidad subcontratada
	 * 
	 * @param long idAutorEntSub
	 * @return ArrayList<DtoArticulosAutorizaciones>
	 * @author Angela Maria Aguirre
	 * 
	 */
	public ArrayList<DtoArticulosAutorizaciones> obtenerDetalleArticulosAutorCapitacion(long idAutorEntSub) {
			
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AutorizacionesEntSubArticu.class, "autorizacionesEntSubArticulo");
		criteria.createAlias("autorizacionesEntSubArticulo.autorizacionesEntidadesSub","autorizacionesEntidadesSub");
		criteria.createAlias("autorizacionesEntSubArticulo.articulo","articulo");
		criteria.createAlias("autorizacionesEntSubArticulo.contratos","contratos", Criteria.LEFT_JOIN);
		
		criteria.createAlias("articulo.naturalezaArticulo","naturalezaArticulo");
		criteria.createAlias("autorizacionesEntSubArticulo.unidosisXArticulo"	,"unidosisXArticulo", Criteria.LEFT_JOIN);
		criteria.createAlias("unidosisXArticulo.unidadMedida"					,"unidadMedida"		, Criteria.LEFT_JOIN);		
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
		
		criteria.add(Restrictions.eq("autorizacionesEntidadesSub.consecutivo",idAutorEntSub));
		
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
		projectionList.add(Projections.property("articulo.codigo"),"codigoArticulo");
		projectionList.add(Projections.property("articulo.descripcion"),"descripcionArticulo");
		projectionList.add(Projections.property("articulo.subgrupo"),"codigoSubGrupoArticulo");
		projectionList.add(Projections.property("naturalezaArticulo.nombre"),"naturalezaArticulo");
		projectionList.add(Projections.property("autorizacionesEntSubArticulo.valorTarifa"),"valorArticulo");
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
		//Campos de Articulo
		projectionList.add(Projections.property("unidadMedida.nombre"),"unidadMedidaArticulo");
		projectionList.add(Projections.property("naturalezaArticulo.esMedicamento"),"esMedicamento");
		projectionList.add(Projections.property("autorizacionesEntSubArticulo.dosis"),"dosisFormulacion");
		projectionList.add(Projections.property("autorizacionesEntSubArticulo.nroDosisTotal"),"cantidadSolicitada");
		projectionList.add(Projections.property("autorizacionesEntSubArticulo.frecuencia"),"frecuenciaFormulacion");
		projectionList.add(Projections.property("autorizacionesEntSubArticulo.diasTratamiento"),"diasTratamientoFormulacion");
		projectionList.add(Projections.property("autorizacionesEntSubArticulo.via"),"viaFormulacion");
		projectionList.add(Projections.property("autorizacionesEntSubArticulo.tipoFrecuencia"),"tipoFrecuenciaFormulacion");
		
			
		criteria.setProjection(projectionList);
		criteria.setResultTransformer(Transformers.aliasToBean(DtoArticulosAutorizaciones.class));
		ArrayList<DtoArticulosAutorizaciones> listaArticulos = (ArrayList<DtoArticulosAutorizaciones>)criteria.list();
		
		if(listaArticulos != null && !listaArticulos.isEmpty()){
			ClaseInventarioDelegate claseInvDelegate=new ClaseInventarioDelegate();
			for(DtoArticulosAutorizaciones dtoArticulo:listaArticulos){
				DtoClaseInventario dtoClaseInv = new DtoClaseInventario();
				dtoClaseInv= claseInvDelegate.obtenerClaseInventarioPorSungrupo(dtoArticulo.getCodigoSubGrupoArticulo());
				dtoArticulo.setCodigoClaseInvArticulo(dtoClaseInv.getCodigo());
				dtoArticulo.setNombreClaseInvArticulo(dtoClaseInv.getNombre());
			}
		}
				
		return listaArticulos;
	}	
	
	
}
