package com.servinte.axioma.orm.delegate.odontologia;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.dto.odontologia.DtoFiltroReportePromocionesOdontologicas;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaReportePromocionesOdontologicas;
import com.servinte.axioma.orm.PromocionesOdontologicas;
import com.servinte.axioma.orm.PromocionesOdontologicasHome;


/**
 * 
 * @author axioma
 *
 */
public class PromocionesOdontologicasDelegate extends PromocionesOdontologicasHome
{
	

	/**
	 * Retorna PromocionesOdontologicas
	 * 
	 * @return PromocionesOdontologicas
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<PromocionesOdontologicas> listarPromocionesOdontologicas ( ){
		

		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(PromocionesOdontologicas.class);
		criteria.addOrder(Property.forName("nombre").asc());
		
		return (ArrayList<PromocionesOdontologicas>) criteria.list();
		
		/*
		return (ArrayList<PromocionesOdontologicas>) sessionFactory.getCurrentSession()
				.createCriteria(PromocionesOdontologicas.class)
				.list();
			*/	
		
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<PromocionesOdontologicas> listarTodosPorCiudad(String codigoCiudad, String codigoPais, String codigoDto){
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(PromocionesOdontologicas.class,"promocion")
			
			 .createAlias("promocion.detPromocionesOdos", "detPromocionesOd")
			 .createAlias("detPromocionesOd.detCaPromocionesOdos", "detCaPromocionesOd")
			 .createAlias("detCaPromocionesOd.centroAtencion", "centroAtencion")
			 .createAlias("centroAtencion.ciudades", "ciudades");
		
			criteria.add(Restrictions.eq("ciudades.id.codigoCiudad", codigoCiudad))
			 .add(Restrictions.eq("ciudades.id.codigoPais", codigoPais))
			 .add(Restrictions.eq("ciudades.id.codigoDepartamento", codigoDto))
			 .addOrder(Property.forName("nombre").asc());
		
			return (ArrayList<PromocionesOdontologicas>) criteria.list();
		
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<PromocionesOdontologicas> listarTodosPorCentroAtencionYCiudad(int centroAtencion, String codigoCiudad, String codigoPais, String codigoDto ){
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(PromocionesOdontologicas.class,"promocion")
			 .createAlias("promocion.detPromocionesOdos", "detPromocionesOd")
			 .createAlias("detPromocionesOd.detCaPromocionesOdos", "detCaPromocionesOd")
			 .createAlias("detCaPromocionesOd.centroAtencion", "centroAtencion")
			 .createAlias("centroAtencion.ciudades", "ciudades");
			
			criteria.add(Restrictions.eq("centroAtencion.consecutivo", centroAtencion))
				.add(Restrictions.eq("ciudades.id.codigoCiudad", codigoCiudad))
				.add(Restrictions.eq("ciudades.id.codigoPais", codigoPais))
				.add(Restrictions.eq("ciudades.id.codigoDepartamento", codigoDto))
				.addOrder(Property.forName("nombre").asc());
			
		return (ArrayList<PromocionesOdontologicas>) criteria.list();
	}
	

	@SuppressWarnings("unchecked")
	public ArrayList<PromocionesOdontologicas> listarTodosPorCentroAtencionYRegion(int centroAtencion, long codigoRegion){
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(PromocionesOdontologicas.class,"promocion")
		.createAlias("promocion.detPromocionesOdos", "detPromocionesOd")
			 .createAlias("detPromocionesOd.detCaPromocionesOdos", "detCaPromocionesOd")
			 .createAlias("detCaPromocionesOd.centroAtencion", "centroAtencion")
			 .createAlias("centroAtencion.regionesCobertura", "regionesCobertura")
			 .createAlias("centroAtencion.ciudades", "ciudades");
		
		criteria.add(Restrictions.eq("centroAtencion.consecutivo", centroAtencion))
				.add(Restrictions.eq("regionesCobertura.codigo", codigoRegion))
				.addOrder(Property.forName("nombre").asc());
		
		return (ArrayList<PromocionesOdontologicas>) criteria.list();
		
	}
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<PromocionesOdontologicas> listarTodosPorRegion(long codigoRegion ){
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(PromocionesOdontologicas.class,"promocion")
			 .createAlias("promocion.detPromocionesOdos", "detPromocionesOd")
			 .createAlias("detPromocionesOd.detCaPromocionesOdos", "detCaPromocionesOd")
			 .createAlias("detCaPromocionesOd.centroAtencion", "centroAtencion")
			 .createAlias("centroAtencion.regionesCobertura", "regionesCobertura");
		
		criteria.add(Restrictions.eq("regionesCobertura.codigo", codigoRegion))
				.addOrder(Property.forName("nombre").asc());
		
		return (ArrayList<PromocionesOdontologicas>) criteria.list();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public ArrayList<PromocionesOdontologicas> listarTodosPorCentroAtencion(int centroAtencion){
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(PromocionesOdontologicas.class,"promocion")
			 .createAlias("promocion.detPromocionesOdos", "detPromocionesOd")
			 .createAlias("detPromocionesOd.detCaPromocionesOdos", "detCaPromocionesOd")
			 .createAlias("detCaPromocionesOd.centroAtencion", "centroAtencion");
		
		criteria.add(Restrictions.eq("centroAtencion.consecutivo", centroAtencion))
				.addOrder(Property.forName("nombre").asc());
		
		return (ArrayList<PromocionesOdontologicas>) criteria.list();
	}
	
	
	/**
	 * Este método se encarga de consultar las promociones odontológicas
	 * y ordenarlas 
	 * @param dtoFiltro dto con datos ingresados en la forma reporte promociones odontológicas
	 * @return ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas>
	 * 
	 * @author Fabian Becerra
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> consultarPromocionesOdontologicas(DtoFiltroReportePromocionesOdontologicas dtoFiltro){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PromocionesOdontologicas.class, "promocion")
		.createAlias("promocion.detPromocionesOdos", "detPromocionesOd")
		
		.createAlias("detPromocionesOd.regionesCobertura", "region",Criteria.LEFT_JOIN)
		.createAlias("detPromocionesOd.ciudades", "ciudad",Criteria.LEFT_JOIN)
		
		.createAlias("detPromocionesOd.detCaPromocionesOdos", "detCaPromocionesOdo",Criteria.LEFT_JOIN)
		.createAlias("detCaPromocionesOdo.centroAtencion", "centroAtencion",Criteria.LEFT_JOIN)
		
		.createAlias("detPromocionesOd.categoriasAtencion", "categoriasAtencion",Criteria.LEFT_JOIN)
		
		.createAlias("detPromocionesOd.detConvPromocionesOdos", "detConvPromocionesOdo",Criteria.LEFT_JOIN)
		.createAlias("detConvPromocionesOdo.convenios", "convenios",Criteria.LEFT_JOIN)
		
		.createAlias("detPromocionesOd.sexo", "sexo",Criteria.LEFT_JOIN)
		
		.createAlias("detPromocionesOd.estadosCiviles", "estadoCivil",Criteria.LEFT_JOIN)
		
		.createAlias("detPromocionesOd.programas", "programas",Criteria.LEFT_JOIN)
		
		.createAlias("detPromocionesOd.ocupaciones", "ocupaciones",Criteria.LEFT_JOIN)
		
		.createAlias("promocion.usuarios", "usuario",Criteria.LEFT_JOIN)
		;
		
		ProjectionList projection = Projections.projectionList();
		
		//FECHA DE GENERACION DE LA PROMOCION 
		if(dtoFiltro.getFechaGenInicial()!=null&& dtoFiltro.getFechaGenFinal()!=null){
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.between("promocion.fechaGeneracion",dtoFiltro.getFechaGenInicial(), dtoFiltro.getFechaGenFinal()));
			disjunction.add(Restrictions.between("promocion.fechaModifica",dtoFiltro.getFechaGenInicial(), dtoFiltro.getFechaGenFinal()));
			criteria.add(disjunction);
		}
		
		//FECHA DE VIGENCIA DE LA PROMOCION
		if(dtoFiltro.getFechaVigInicial()!=null&&dtoFiltro.getFechaVigFinal()!=null){
			criteria.add(Restrictions.between("promocion.fechaInicialVigencia",dtoFiltro.getFechaGenInicial(), dtoFiltro.getFechaGenFinal()));
			criteria.add(Restrictions.between("promocion.fechaFinalVigencia",dtoFiltro.getFechaGenInicial(), dtoFiltro.getFechaGenFinal()));
		}
		
		//PAIS
		if (!UtilidadTexto.isEmpty(dtoFiltro.getCodigoPaisSeleccionado())&&!dtoFiltro.getCodigoPaisSeleccionado().trim().equals(ConstantesBD.codigoNuncaValido+"")) {
			
			criteria.createAlias("centroAtencion.ciudades", "ciudadesCentrosAtencion",Criteria.LEFT_JOIN);
			criteria.createAlias("ciudadesCentrosAtencion.departamentos", "departamento",Criteria.LEFT_JOIN);
			criteria.createAlias("departamento.paises", "pais",Criteria.LEFT_JOIN);
			
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.eq("pais.codigoPais", dtoFiltro.getCodigoPaisSeleccionado()));
			disjunction.add(Restrictions.isNull("pais.codigoPais"));
			criteria.add(disjunction);
			
		}
		
		//CIUDAD
		if (!UtilidadTexto.isEmpty(dtoFiltro.getCiudadDeptoPais()) && !dtoFiltro.getCiudadDeptoPais().trim().equals(ConstantesBD.codigoNuncaValido+"")) {
			
			String vec[]= dtoFiltro.getCiudadDeptoPais().split(ConstantesBD.separadorSplit);			
			dtoFiltro.setCodigoCiudad(vec[0]);
			dtoFiltro.setCodigoDpto(vec[1]);
			dtoFiltro.setCodigoPais(vec[2]);
			criteria.add(Restrictions.eq("ciudad.id.codigoCiudad", dtoFiltro.getCodigoCiudad()))
			.add(Restrictions.eq("ciudad.id.codigoPais", dtoFiltro.getCodigoPais()))
			.add(Restrictions.eq("ciudad.id.codigoDepartamento", dtoFiltro.getCodigoDpto()));
		}
		
		//REGION
		if (dtoFiltro.getCodigoRegionSeleccionada() > 0) {
			criteria.add(Restrictions.eq("region.codigo", dtoFiltro.getCodigoRegionSeleccionada()));
			
		}
		
		//INSTITUCION
		if(dtoFiltro.getInstitucionMultiempresa().equals(ConstantesBD.acronimoSi)){
			criteria.createAlias("centroAtencion.empresasInstitucion", "empresaInstitucion",Criteria.LEFT_JOIN);
			if (dtoFiltro.getCodigoEmpresaInstitucion() > 0) {
				criteria.add(Restrictions.eq("empresaInstitucion.codigo",dtoFiltro.getCodigoEmpresaInstitucion()));
			}
		}else{
			criteria.createAlias("centroAtencion.instituciones", "empresaInstitucion",Criteria.LEFT_JOIN);
			Disjunction disjunction = Restrictions.disjunction();
			int codigoInstitucion=(int)dtoFiltro.getCodigoInstitucion();
			disjunction.add(Restrictions.eq("empresaInstitucion.codigo",codigoInstitucion));
			disjunction.add(Restrictions.isNull("empresaInstitucion.codigo"));
			criteria.add(disjunction);
		}
				
		
		//CENTRO ATENCION
		if (dtoFiltro.getConsecutivoCentroAtencionSeleccionado() > 0) {
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(Restrictions.eq("centroAtencion.consecutivo",dtoFiltro.getConsecutivoCentroAtencionSeleccionado()));
			disjunction.add(Restrictions.isNull("centroAtencion.consecutivo"));
			criteria.add(disjunction);
		}
		
		//EDAD PACIENTE
		if (dtoFiltro.getRangoEdadInicialPaciente()!=null&&dtoFiltro.getRangoEdadFinalPaciente()!=null) {
			
			criteria.add(Restrictions.between("detPromocionesOd.edadInicial",dtoFiltro.getRangoEdadInicialPaciente(),dtoFiltro.getRangoEdadFinalPaciente()));
			criteria.add(Restrictions.between("detPromocionesOd.edadFinal",dtoFiltro.getRangoEdadInicialPaciente(),dtoFiltro.getRangoEdadFinalPaciente()));
			
		}
		
		//SEXO
		if(!UtilidadTexto.isEmpty(dtoFiltro.getCodigoSexoPaciente()) && !dtoFiltro.getCodigoSexoPaciente().equals("-2")){
			int sexoPaciente = Integer.parseInt(dtoFiltro.getCodigoSexoPaciente());
			criteria.add(Restrictions.eq("sexo.codigo", sexoPaciente));
		}
		
		//ESTADO CIVIL
		if(!dtoFiltro.getEstadoCivilPaciente().equals(ConstantesBD.codigoNuncaValido+"")){
			criteria.add(Restrictions.eq("estadoCivil.acronimo", dtoFiltro.getEstadoCivilPaciente()));
		}
		
		//OCUPACION
		if(!dtoFiltro.getCodigoOcupacion().equals(ConstantesBD.codigoNuncaValido+"")){
			criteria.add(Restrictions.eq("ocupaciones.codigo", Integer.parseInt(dtoFiltro.getCodigoOcupacion())));
		}
		
		//CONVENIOS
		if(!dtoFiltro.getCodigoConvenio().equals(ConstantesBD.codigoNuncaValido+"")){
			criteria.add(Restrictions.eq("convenios.codigo", Integer.parseInt(dtoFiltro.getCodigoConvenio())));
		}
		
		//ESPECIALIDAD
		if(dtoFiltro.getCodigoEspecialidad()!=ConstantesBD.codigoNuncaValido){
			criteria.createAlias("programas.especialidades", "especialidades");
			criteria.add(Restrictions.eq("especialidades.codigo", dtoFiltro.getCodigoEspecialidad()));
			
		}
		
		//PROGRAMAS
		if(dtoFiltro.getCodigoPrograma() > 0){
			criteria.add(Restrictions.eq("programas.codigo",dtoFiltro.getCodigoPrograma()));
		}
		
		//ESTADO PROMOCION
		if(!dtoFiltro.getEstadoPromocion().equals(ConstantesBD.codigoNuncaValido+"")){
			criteria.add(Restrictions.eq("promocion.activo",dtoFiltro.getEstadoActivoPromocion()));
		}
		
	    projection.add(Projections.property("promocion.codigoPk"),"codigoPkPromocion");
		projection.add(Projections.property("region.descripcion"),"region");
		projection.add(Projections.property("ciudad.descripcion"),"ciudad");
		projection.add(Projections.property("categoriasAtencion.descripcion"),"categoriaAtencion");
		projection.add(Projections.property("categoriasAtencion.codigo"),"codigoCategoriaAtencion");
		projection.add(Projections.property("centroAtencion.descripcion"),"centroAtencion");
		projection.add(Projections.property("centroAtencion.consecutivo"),"consecutivoCentroAtencion");
		projection.add(Projections.property("promocion.nombre"),"nombrePromocion");
		projection.add(Projections.property("convenios.nombre"),"nombreConvenio");
		projection.add(Projections.property("convenios.codigo"),"codigoConvenio");
		projection.add(Projections.property("detPromocionesOd.nroHijos"),"nroHijos");
		projection.add(Projections.property("detPromocionesOd.edadInicial"),"edadInicial");
		projection.add(Projections.property("detPromocionesOd.edadFinal"),"edadFinal");
		projection.add(Projections.property("sexo.nombre"),"sexo");
		projection.add(Projections.property("estadoCivil.nombre"),"estadoCivil");
		projection.add(Projections.property("promocion.fechaInicialVigencia"),"fechaIniVigencia");
		projection.add(Projections.property("promocion.fechaFinalVigencia"),"fechaFinVigencia");
		projection.add(Projections.property("promocion.horaInicialVigencia"),"horaIniVigencia");
		projection.add(Projections.property("promocion.horaFinalVigencia"),"horaFinVigencia");
		
		projection.add(Projections.property("detPromocionesOd.porcentajeDescuento"),"porcentajeDescuento");
		projection.add(Projections.property("detPromocionesOd.valorDescuento"),"valorDescuento");
		projection.add(Projections.property("detPromocionesOd.porcentajeHonorario"),"porcentajeHonorario");
		projection.add(Projections.property("detPromocionesOd.valorHonorario"),"valorHonorario");
		
		projection.add(Projections.property("programas.codigoPrograma"),"codigoPrograma");
	
		
		projection.add(Projections.property("ocupaciones.nombre"),"ocupacion");
		
		projection.add(Projections.property("promocion.fechaModifica"),"fechaModifica");
		projection.add(Projections.property("promocion.horaModifica"),"horaModifica");
		
		projection.add(Projections.property("usuario.login"),"loginUsuario");
		
		projection.add(Projections.property("promocion.activo"),"estadoPromocion");
		
		
		criteria.setProjection(Projections.distinct(projection));
		criteria.addOrder( Order.asc("promocion.nombre") );
		
		criteria.setResultTransformer( Transformers.aliasToBean(DtoResultadoConsultaReportePromocionesOdontologicas.class));
		
		ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> listadoConsultaPromociones=
			(ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas>)criteria.list();
		
		return listadoConsultaPromociones; 
	}

}
