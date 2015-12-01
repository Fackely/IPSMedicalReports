package com.servinte.axioma.orm.delegate.facturacion.convenio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.facturacion.DTOFacturasConvenios;
import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturacion.DtoReporteValoresFacturadosPorConvenio;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.ConveniosHome;
import com.servinte.axioma.orm.Facturas;


/**
 * Esta clase se encarga de de manejar las transaccciones relacionadas
 * con la entidad Convenios
 * 
 * @author Cristhian Murillo
 */
@SuppressWarnings("unchecked")
public class ConvenioDelegate extends ConveniosHome {

	
	/**
	 * Método que lista convenios Odontológicos 
	 */
	public List<Convenios> listaConveniosOdontologicos(){
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Convenios.class, "convenios");
		return (ArrayList<Convenios>)criteria.list();
	}	
	
	/**
	 * Lista los convenios activos asociados a la institución.
	 * Los convenios son filtrados por:
	 * Convenios que tengan contratos vigentes (fecha actual que esté dentro del rango de fechas del contrato)
	 * Tipo Atención = Odontológica
	 * Es convenio tarjeta cliente = NO o isNull
	 * 
	 * @author Cristhian Murillo
	 * @param codInstitucion
	 * @return ArrayList<Convenios>
	 */
	public ArrayList<Convenios> listaConveniosActivosPorInstitucionIngresoOdonto(int codInstitucion)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Convenios.class, "convenios");
		
		criteria.createAlias("convenios.instituciones"		, "institucion");
		criteria.createAlias("convenios.contratoses"		, "contratoses");
		
		criteria.add(Restrictions.eq("convenios.activo", true));
		criteria.add(Restrictions.eq("institucion.codigo", codInstitucion));
		criteria.add(Restrictions.eq("convenios.tipoAtencion", ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico));
		
		// El campo esConvTarCli = 'N' o sea nulo.
		Disjunction disjunctionOR = Restrictions.disjunction();  
			disjunctionOR.add( Restrictions.eq("convenios.esConvTarCli", ConstantesBD.acronimoNo) );  
			disjunctionOR.add( Restrictions.isNull("convenios.esConvTarCli"));
		criteria.add(disjunctionOR);
		//----------------------------------------------------------------------------
		
		criteria.add(Restrictions.ne("contratoses.numeroContrato", "null"));
		criteria.add(Restrictions.isNotNull("contratoses.numeroContrato"));
		
		Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
		
		criteria.add(Restrictions.ge("contratoses.fechaFinal", fechaActual));
		criteria.add(Restrictions.le("contratoses.fechaInicial", fechaActual));
		
		criteria.addOrder(Order.asc("convenios.nombre"));
		
		return (ArrayList<Convenios>)criteria.list();
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar todos los convenios
	 * que manejen montos. 
	 * 
	 * @return ArrayList<DtoConvenio>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DtoConvenio> obtenerConveniosManejanMonto(){			
				
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(Convenios.class, "convenio");
		
		criteria.add(Restrictions.eq("convenio.manejaMontos", ConstantesBD.acronimoSi))
			.add(Restrictions.eq("convenio.activo", true));
		criteria.setProjection(Projections.projectionList()
			.add(Projections.property("convenio.codigo"),"codigo")
			.add(Projections.property("convenio.nombre"),"descripcion"));
		criteria.addOrder(Order.asc("convenio.nombre"));
		criteria.setResultTransformer(Transformers.aliasToBean(DtoConvenio.class));
		
		ArrayList<DtoConvenio> listaConvenios=(ArrayList<DtoConvenio>)criteria.list();
				
		return listaConvenios;
	}	
	
	
	
	/**
	 * 
	 * Este Método se encarga de consultar un convenio
	 * por su ID
	 * 
	 * @param int
	 * @return Convenios 
	 * @author, Angela Maria Aguirre
	 */
	public DtoConvenio buscarConvenio(int id) 
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Convenios.class, "convenio");
		criteria.createAlias("convenio.tiposRegimen", "tipoRegimen");
		
		criteria.add(Restrictions.eq("convenio.codigo", id));
		
		criteria.setProjection(Projections.projectionList()
			.add(Projections.property("convenio.codigo")			,"codigo")
			.add(Projections.property("convenio.nombre")			,"descripcion")
			.add(Projections.property("tipoRegimen.acronimo")		,"acronimoTipoRegimen")
			.add(Projections.property("convenio.manejaMontos")		,"convenioManejaMontos")
			.add(Projections.property("convenio.activo")			,"activo"));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoConvenio.class));
		DtoConvenio convenio = (DtoConvenio)criteria.uniqueResult();
		
		return convenio;	
		
	}

	/**
	 * Listar los contratos asignados al convenio
	 * @param convenio
	 * @return Lista de contratos
	 */
	public ArrayList<DtoContrato> listarContratosConvenio(DtoConvenio convenio) {
		
		Date fechaActual=UtilidadFecha.getFechaActualTipoBD();
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(Contratos.class,"contratos");
	
		criteria.add(Restrictions.eq("contratos.convenios.codigo", convenio.getCodigo()));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("contratos.codigo"),"codigo")
				.add(Projections.property("contratos.numeroContrato"),"numeroContrato")
				.add(Projections.property("contratos.fechaInicial"),"fechaInicialDate")
				.add(Projections.property("contratos.fechaFinal"),"fechaFinalDate"));
	
		criteria.add(Restrictions.le("contratos.fechaInicial", fechaActual));
		criteria.add(Restrictions.ge("contratos.fechaFinal", fechaActual));

		criteria.setResultTransformer(Transformers.aliasToBean(DtoContrato.class));
		ArrayList<DtoContrato>listaContratos=(ArrayList<DtoContrato>)criteria.list();
	
		convenio.setListContrato(listaContratos);
		
		return listaContratos;
	}
	
	
	
	/**
	 * 
	 * Este Método se encarga de consultar un convenio por su id
	 * 
	 * @param int id
	 * @return Convenios
	 *
	 */
	public Convenios findById(int id) 
	{
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Convenios.class, "convenios");
		
		criteria.createAlias("convenios.tiposContrato", "tiposContrato");
		
		criteria.add(Restrictions.eq("codigo", id));
		
		Convenios convenio = (Convenios)criteria.uniqueResult();
		
		return convenio;		
	}
	
	
	/**
	 * Lista los convenios activos e inactivos asociados a la institución.
	 * Tipo Atención = Odontológica
	 * Es convenio tarjeta cliente = NO o isNull
	 * @param codInstitucion
	 * @return ArrayList<Convenios>
	 * @author Diana Carolina G
	 */
	
	public ArrayList<Convenios> listarConveniosActivosInactivosOdont(int codInstitucion){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Convenios.class, "convenios");
		criteria.createAlias("convenios.instituciones"		, "institucion");
		criteria.createAlias("convenios.contratoses"		, "contratoses");
		
		criteria.add(Restrictions.eq("institucion.codigo", codInstitucion));
		criteria.add(Restrictions.eq("convenios.tipoAtencion", ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico));
		

		Disjunction disjunctionOR = Restrictions.disjunction();  
			disjunctionOR.add( Restrictions.eq("convenios.esConvTarCli", ConstantesBD.acronimoNo) );  
			disjunctionOR.add( Restrictions.isNull("convenios.esConvTarCli"));
		criteria.add(disjunctionOR);
		
		criteria.addOrder(Order.asc("convenios.nombre"));
		
		return (ArrayList<Convenios>)criteria.list();
		
	}
	
	
	/**
	 * Retorna el convenio (unique result) del detalle del cargo asociado a una solicitud(orden) enviada.
	 *
	 * @author Cristhian Murillo
	 * @param numeroSolicitud
	 * @return ArrayList<Convenios>
	*/
	public ArrayList<Convenios> obtenerConvenioDetCargoPorSolicitud(int numeroSolicitud, int codInstitucion)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Convenios.class, "convenios");
		
		criteria.createAlias("convenios.detCargoses"		, "detCargoses", Criteria.LEFT_JOIN);
		criteria.createAlias("detCargoses.solicitudes"		, "solicitudes", Criteria.LEFT_JOIN);
		criteria.createAlias("convenios.instituciones"		, "institucion", Criteria.LEFT_JOIN);
		criteria.createAlias("convenios.contratoses"		, "contratoses", Criteria.LEFT_JOIN);
		
		criteria.add(Restrictions.eq("convenios.activo"				, true));
		criteria.add(Restrictions.eq("institucion.codigo"			, codInstitucion));
		criteria.add(Restrictions.eq("solicitudes.numeroSolicitud"	, numeroSolicitud));
		
		return (ArrayList<Convenios>)criteria.list();
	}
	
	
	
	
	/**
	 * Retorna los convenios activos asociados al ingreso paciente y filtrados por institución
	 * @param codInstitucion
	 * @param codIngreso
	 * @return ArrayList<Convenios>
	 */
	public ArrayList<Convenios> obtenerConveniosPorIngresoPaciente(int codInstitucion, int codIngreso)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Convenios.class, "convenios");
		
		criteria.createAlias("convenios.instituciones"		, "institucion");
		criteria.createAlias("convenios.subCuentases"		, "subCuentases");
		criteria.createAlias("convenios.tiposContrato"		, "tiposContrato");
		criteria.createAlias("subCuentases.ingresos"		, "ingresos");
		criteria.createAlias("convenios.contratoses"		, "contratoses");
		
		criteria.add(Restrictions.eq("convenios.activo"		, true));
		criteria.add(Restrictions.eq("institucion.codigo"	, codInstitucion));
		criteria.add(Restrictions.eq("ingresos.id"			, codIngreso));
		
		return (ArrayList<Convenios>)criteria.list();
	}

	
	/**
	 * Lista los convenios activos e inactivos de una empresa seleccionada
	 * cuando el c&oacute;digo de la empresa es v&aacute;lido o todos los convenios
	 * cuando el c&oacute;digo de la empresa no es v&aacute;lido.
	 * 
	 * @param codigoEmpresa
	 * @return ArrayList<Convenios>
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<Convenios> listarConveniosPorEmpresa(int codigoEmpresa) {

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(Convenios.class,"convenio")
				.createAlias("convenio.empresas", "empresa");

		if (codigoEmpresa != ConstantesBD.codigoNuncaValido) {
			criteria.add(Restrictions.eq("empresa.codigo", codigoEmpresa));
		}

		criteria.addOrder(Property.forName("convenio.nombre").asc());

		return (ArrayList<Convenios>) criteria.list();

	}

	/**
	 * M&eacute;todo encargado de obtener los valores facturados por convenios.
	 * 
	 * @param dto
	 * @return ArrayList<DTOFacturasConvenios>
	 * @author Luis Fernando Hincapi&eacute; Ospina
	 */
	public ArrayList<DTOFacturasConvenios> obtenerValoresFacturadosConvenio(
			DtoReporteValoresFacturadosPorConvenio dto) {

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(Facturas.class, "factura")
				.createAlias("factura.convenios"					, "convenio")
				.createAlias("factura.contratos"					, "contrato")
				.createAlias("convenio.empresas"					, "empresa")
				.createAlias("empresa.terceros"						, "tercero")
				.createAlias("factura.centroAtencionByCentroAten"	, "centroAtencion")
				.setProjection(Projections.projectionList()
						.add(Projections.property("factura.fecha")					, "fechaFactura")
						.add(Projections.property("factura.estadosFacturaF.codigo")	, "estadoFactura")
						.add(Projections.property("convenio.codigo")				, "codigoConvenio")
						.add(Projections.property("convenio.nombre")				, "nombreConvenio")
						.add(Projections.property("contrato.numeroContrato")		, "numeroContrato")
						.add(Projections.property("factura.valorTotal")				,"valor")
						.add(Projections.property("tercero.numeroIdentificacion")	,"nit")
						.add(Projections.property("empresa.razonSocial")			,"nombreEmpresa")
						.add(Projections.property("empresa.codigo")					,"codigoEmpresa"));

		criteria.add(Restrictions.between("factura.fecha", dto.getFechaInicial(), dto.getFechaFinal()));

		if ((!UtilidadTexto.isEmpty(dto.getCiudadDeptoPais()))
				&& (!dto.getCiudadDeptoPais().trim().equals(ConstantesBD.codigoNuncaValido + ""))) {
			criteria.createAlias("centroAtencion.ciudades", "ciudad");
			criteria.createAlias("ciudad.departamentos", "departamento");
			criteria.createAlias("departamento.paises", "pais");

			criteria.add(Restrictions.eq("departamento.id.codigoDepartamento", dto.getCodigoDpto()));
			criteria.add(Restrictions.eq("ciudad.id.codigoCiudad", dto.getCodigoCiudad()));
		} else {
			criteria.createAlias("centroAtencion.ciudades", "ciudad");
			criteria.createAlias("ciudad.paises", "pais");
		}
		criteria.add(Restrictions.eq("pais.codigoPais", dto.getCodigoPais()));

		if (dto.getCodigoEmpresaInstitucion() != ConstantesBD.codigoNuncaValidoLong) {
			criteria.createAlias("centroAtencion.empresasInstitucion", "institucion");

			criteria.add(Restrictions.eq("institucion.codigo", dto.getCodigoEmpresaInstitucion()));
		}

		if (dto.getConsecutivoCentroAtencion() != ConstantesBD.codigoNuncaValido) {
			criteria.add(Restrictions.eq("centroAtencion.consecutivo", dto.getConsecutivoCentroAtencion()));
		}

		if (dto.getCodigoEmpresa() != ConstantesBD.codigoNuncaValido) {
			criteria.add(Restrictions.eq("empresa.codigo", dto.getCodigoEmpresa()));
		}

		if (dto.getCodigoConvenio() != ConstantesBD.codigoNuncaValido) {
			criteria.add(Restrictions.eq("convenio.codigo", dto.getCodigoConvenio()));
		}

		criteria.addOrder(Order.asc("empresa.razonSocial"));
		criteria.addOrder(Order.asc("convenio.nombre"));

		criteria.setResultTransformer(Transformers.aliasToBean(DTOFacturasConvenios.class));

		ArrayList<DTOFacturasConvenios> listaValoresFacturadosConvenio = 
			(ArrayList<DTOFacturasConvenios>)criteria.list();

		return listaValoresFacturadosConvenio;

	}
	
	
	
	/**
	 * Lista los convenios activos asociados a la institución.
	 * 
	 * @author Cristhian Murillo
	 * @param codInstitucion
	 * @return ArrayList<Convenios>
	 */
	public ArrayList<Convenios> listarConveniosActivosPorInstitucion(int codInstitucion)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Convenios.class, "convenios");
		
		criteria.createAlias("convenios.instituciones"	, "institucion");
		criteria.createAlias("convenios.tiposContrato"	, "tiposContrato");
		
		criteria.add(Restrictions.eq("convenios.activo"		, true));
		
		if(codInstitucion != ConstantesBD.codigoNuncaValido){
			criteria.add(Restrictions.eq("institucion.codigo"	, codInstitucion));
		}
		
		criteria.addOrder(Order.asc("convenios.nombre"));
		
		return (ArrayList<Convenios>)criteria.list();
	}
	
	
	
	
	/**
	 * Lista los convenios Capitados activos asociados a la institución.
	 * 
	 * @author Cristhian Murillo
	 * @param codInstitucion
	 * @return ArrayList<Convenios>
	*/
	public ArrayList<Convenios> listarConveniosCapitadosActivosPorInstitucion(int codInstitucion)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Convenios.class, "convenios");
		
		criteria.createAlias("convenios.instituciones"		, "institucion");
		criteria.createAlias("convenios.tiposContrato"		, "tiposContrato");
		
		criteria.add(Restrictions.eq("convenios.activo"						, true));
		criteria.add(Restrictions.eq("institucion.codigo"					, codInstitucion));
		criteria.add(Restrictions.eq("tiposContrato.codigo"					, ConstantesBD.codigoTipoContratoCapitado));
		criteria.add(Restrictions.eq("convenios.capitacionSubcontratada"	, ConstantesBD.acronimoSiChar));
		
		criteria.addOrder(Order.asc("convenios.nombre"));
		
		return (ArrayList<Convenios>)criteria.list();
	}
	
	/**
	 * Obtiene el codigo del convenio, asociado al usuario capitado en el momento
	 * de realizar una autorizaci&oacute;n de ingreso estancia
	 * @param codPaciente
	 * @return ArrayList<Convenios>
	 * @author Diana Carolina G
	 */
	public ArrayList<Convenios>  obtenerConvenioPorUsuarioCapitado(int codPaciente){
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Convenios.class, "convenios");
		criteria.createAlias("convenios.pacienteses"		, "pacienteses");
		
		criteria.add(Restrictions.eq("pacienteses.codigoPaciente"		, codPaciente));
		
		return (ArrayList<Convenios>)criteria.list();
	}
	

	/**
	 * M&eacute;todo encargado de obtener el tipo de contrato asociado 
	 * a un convenio en espec&iacute;fico
	 * @param codigo
	 * @return DtoConvenio
	 * @author Diana Carolina G
	 */
	public DtoConvenio obtenerTipoContratoConvenio(int codigo){
		
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(Convenios.class,"convenio")
		.createAlias("convenio.tiposContrato", "tiposContrato");
		
		criteria.add(Restrictions.eq("convenio.codigo", codigo));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("tiposContrato.codigo"), "codigoTipoContrato")
				.add(Projections.property("tiposContrato.nombre"), "nombreTipoContrato"));
		
		criteria.setResultTransformer(Transformers.aliasToBean(DtoConvenio.class));
		DtoConvenio convenio = (DtoConvenio)criteria.uniqueResult();
		
		return convenio;
		
	}
	
	
	
/**
	 * Lista los convenios activos/inactivos asociados a la institución.
	 * 
	 * @author Ricardo Ruiz
	 * @param codInstitucion
	 * @return ArrayList<Convenios>
	 */
	public ArrayList<Convenios> listarConveniosPorInstitucion(int codInstitucion)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Convenios.class, "convenios");
		criteria.createAlias("convenios.instituciones"	, "institucion");
				
		if(codInstitucion != ConstantesBD.codigoNuncaValido){
			criteria.add(Restrictions.eq("institucion.codigo"	, codInstitucion));
		}
		
		criteria.addOrder(Order.asc("convenios.nombre"));
		
		return (ArrayList<Convenios>)criteria.list();
	}
	
	
	
	
	/**
	 * Lista los convenios Capitados activos/inactivos asociados a la institución.
	 * 
	 * @author Ricardo Ruiz
	 * @param codInstitucion
	 * @return ArrayList<Convenios>
	*/
	public ArrayList<Convenios> listarConveniosCapitadosPorInstitucion(int codInstitucion)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Convenios.class, "convenios");
		
		criteria.createAlias("convenios.instituciones"		, "institucion");
		criteria.createAlias("convenios.tiposContrato"		, "tiposContrato");
		
		criteria.add(Restrictions.eq("institucion.codigo"					, codInstitucion));
		criteria.add(Restrictions.eq("tiposContrato.codigo"					, ConstantesBD.codigoTipoContratoCapitado));
		criteria.add(Restrictions.eq("convenios.capitacionSubcontratada"	, ConstantesBD.acronimoSiChar));
		
		criteria.addOrder(Order.asc("convenios.nombre"));
		
		return (ArrayList<Convenios>)criteria.list();
	}
	
	/**
	 * Lista los convenios Capitados activos/inactivos asociados a la institución.
	 * 
	 * @author Cesar Gomez
	 * @param codInstitucion
	 * @return ArrayList<DtoConvenio>
	*/
	public ArrayList<DtoConvenio> listarTodosConveniosCapitadosPorInstitucion(int codInstitucion)
	{
		 String consulta="SELECT new com.princetonsa.dto.facturacion.DtoConvenio(conv.codigo, conv.nombre) " +
					      "FROM Convenios conv " +
					      	"INNER JOIN conv.instituciones inst " +
					      	"INNER JOIN conv.tiposContrato tp "+
					      "WHERE inst.codigo = :codigoInstitucion "+
					      	"AND tp.codigo =:tipoContrato " +
					      "ORDER BY conv.nombre ";
			  
			  Query query = sessionFactory.getCurrentSession().createQuery(consulta);
			  query.setParameter("codigoInstitucion", codInstitucion, Hibernate.INTEGER);
			  query.setParameter("tipoContrato", ConstantesBD.codigoTipoContratoCapitado, Hibernate.INTEGER);
			  ArrayList<DtoConvenio> listaServicios = (ArrayList<DtoConvenio>)query.list();
			  return listaServicios;
	}
	
	/**
	 * Lista los convenios activos en el sistema
	 *  
	 * @return ArrayList<Convenios>
	 * @author Camilo Gómez
	 */
	public ArrayList<Convenios> listarConveniosActivos()
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Convenios.class, "convenios");
		
		criteria.add(Restrictions.eq("convenios.activo"		,true));
		
		criteria.setProjection(Projections.distinct(Projections.projectionList()				
				.add(Projections.property("convenios.nombre"),"nombre")
				.add(Projections.property("convenios.codigo"),"codigo")
			));			
				
		criteria.addOrder(Order.asc("convenios.nombre"));
		
		criteria.setResultTransformer(Transformers.aliasToBean(Convenios.class));
		ArrayList<Convenios>lista=(ArrayList<Convenios>)criteria.list();
		return lista;
	}
	
	
	/**
	 * Lista todos los convenios en el sistema
	 *  
	 * @return ArrayList<Convenios>
	 * @author Camilo Gómez
	 */
	public ArrayList<Convenios> listarConvenios()
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Convenios.class, "convenios");
				criteria.createAlias("convenios.tiposContrato"		, "tiposContrato");
		
	    criteria.add(Restrictions.eq("tiposContrato.codigo"					, ConstantesBD.codigoTipoContratoCapitado));
	    
		criteria.setProjection(Projections.distinct(Projections.projectionList()				
				.add(Projections.property("convenios.nombre"),"nombre")
				.add(Projections.property("convenios.codigo"),"codigo")
			));			
				
		criteria.addOrder(Order.asc("convenios.nombre"));
		
		criteria.setResultTransformer(Transformers.aliasToBean(Convenios.class));
		ArrayList<Convenios>lista=(ArrayList<Convenios>)criteria.list();
		return lista;
	}
	
	/**
	 * Lista los convenios Capitados activos asociados a la institución que manejen presupuesto.
	 * 
	 * @author Ricardo Ruiz
	 * @param codInstitucion
	 * @return ArrayList<Convenios>
	*/
	public ArrayList<Convenios> listarConveniosCapitadosActivosPorInstitucionManejaPresupuesto(int codInstitucion)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Convenios.class, "convenios");
		
		criteria.createAlias("convenios.instituciones"		, "institucion");
		criteria.createAlias("convenios.tiposContrato"		, "tiposContrato");
		
		criteria.add(Restrictions.eq("convenios.activo"						, true));
		criteria.add(Restrictions.eq("institucion.codigo"					, codInstitucion));
		criteria.add(Restrictions.eq("tiposContrato.codigo"					, ConstantesBD.codigoTipoContratoCapitado));
		criteria.add(Restrictions.eq("convenios.manejaPresupCapitacion"	, ConstantesBD.acronimoSiChar));
		//criteria.add(Restrictions.ne("convenios.capitacionSubcontratada"	, "\0"));
		criteria.add(Restrictions.isNotNull("convenios.capitacionSubcontratada"));
		
		criteria.addOrder(Order.asc("convenios.nombre"));
		ArrayList<Convenios> convenios=(ArrayList<Convenios>)criteria.list();
		return convenios;
	}
	
	/**
	 * Lista los convenios Capitados activos/inactivos asociados a la institución y que tengan una
	 * parametrización de presupuesto para un mes determinado. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codInstitucion
	 * @param esCapitacionSubcontratada
	 * @param mesAnio
	 * @return ArrayList<Convenios>
	*/
	public ArrayList<Convenios> listarConveniosConParametrizacionPresupuestoPorInstitucionPorCapitacion(int codInstitucion, char esCapitacionSubcontratada, Calendar mesAnio)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Convenios.class, "convenios");
		
		criteria.createAlias("convenios.instituciones"		, "institucion");
		criteria.createAlias("convenios.tiposContrato"		, "tiposContrato");
		criteria.createAlias("convenios.contratoses"		, "contrato");
		criteria.createAlias("contrato.paramPresupuestosCaps", "presupuesto");
		criteria.createAlias("presupuesto.valorizacionPresCapGens", "valorizacion");
		
		criteria.add(Restrictions.eq("institucion.codigo"					, codInstitucion));
		criteria.add(Restrictions.eq("tiposContrato.codigo"					, ConstantesBD.codigoTipoContratoCapitado));
		criteria.add(Restrictions.eq("convenios.manejaPresupCapitacion"	, ConstantesBD.acronimoSiChar));
		criteria.add(Restrictions.eq("convenios.capitacionSubcontratada"	, esCapitacionSubcontratada));
		criteria.add(Restrictions.eq("presupuesto.anioVigencia"	, String.valueOf(mesAnio.get(Calendar.YEAR))));
		criteria.add(Restrictions.eq("valorizacion.mes"	, mesAnio.get(Calendar.MONTH)));
		criteria.addOrder(Order.asc("convenios.nombre"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);	
		return (ArrayList<Convenios>)criteria.list();
	}

	
	public static void main(String[] args) {
		ConvenioDelegate delegate=new ConvenioDelegate();
		DtoReporteValoresFacturadosPorConvenio dto=new DtoReporteValoresFacturadosPorConvenio();
		dto.setFechaInicial(UtilidadFecha.conversionFormatoFechaStringDate("18-06-2011"));
		dto.setFechaFinal(UtilidadFecha.conversionFormatoFechaStringDate("18-06-2011"));
		delegate.obtenerValoresFacturadosConvenio(dto);
	}
}
