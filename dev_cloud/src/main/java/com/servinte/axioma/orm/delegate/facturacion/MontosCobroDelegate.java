package com.servinte.axioma.orm.delegate.facturacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.facturacion.DTOMontosCobro;
import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.princetonsa.dto.facturacion.DtoBusquedaMontosCobro;
import com.princetonsa.dto.facturacion.DtoFiltroBusquedaMontosCobro;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.MontosCobro;
import com.servinte.axioma.orm.MontosCobroHome;
import com.servinte.axioma.orm.delegate.facturacion.convenio.ConvenioDelegate;

/**
 * Esta clase se encarga de ejecutar los métodos para
 * la entidad Montos Cobro
 * 
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
@SuppressWarnings("unchecked")
public class MontosCobroDelegate extends MontosCobroHome 
{
	
	/**
	 * Este Método se encarga de consultar los montos de cobro de un 
	 * convenio específico por su id y la fecha de vigencia
	 * 
	 * @param DtoConvenio
	 * @return ArrayList<DTOMontosCobroDetalle>
	 * @author, Angela Maria Aguirre
	 */
	public ArrayList<DTOResultadoBusquedaDetalleMontos> obtenerMontosCobro(DTOMontosCobro monto){		
				
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(MontosCobro.class,"montoCobro")
		.createAlias("montoCobro.convenios", "convenio")
		.createAlias("montoCobro.detalleMontos", "detalleMonto")
		.createAlias("detalleMonto.viasIngreso", "viaIngreso")
		.createAlias("detalleMonto.tiposPaciente", "tipoPaciente")
		.createAlias("detalleMonto.estratosSociales", "estratoSocial")
		.createAlias("detalleMonto.tiposMonto", "tipoMonto")
		.createAlias("detalleMonto.tiposAfiliado", "tipoAfiliado")	
		.createAlias("detalleMonto.naturalezaPacientes", "naturalezaPacientes",Criteria.LEFT_JOIN)
		.createAlias("detalleMonto.detalleMontoGeneral", "detalleMontoGeneral",Criteria.LEFT_JOIN);
		
	
		if(monto.getConvenio()!=null && 
				monto.getConvenio().getCodigo()!=ConstantesBD.codigoNuncaValido){
			criteria.add(Restrictions.eq("convenio.codigo", monto.getConvenio().getCodigo()));
		}
		if(monto.getFechaVigenciaConvenio()!=null){
			criteria.add(Restrictions.eq("montoCobro.vigenciaInicial", monto.getFechaVigenciaConvenio()));
		}
		if(monto.getTipoTransaccion()!= null && !monto.getTipoTransaccion().equals("")){
			if(monto.getTipoTransaccion().equals("consultaMontosCobro")){
				if(monto.getListaDetalles()!=null){
					DTOResultadoBusquedaDetalleMontos filtro = monto.getListaDetalles().get(0);
					if(filtro.getViaIngresoID()!=ConstantesBD.codigoNuncaValido){
						criteria.add(Restrictions.eq("viaIngreso.codigo", filtro.getViaIngresoID()));
					}
					if(!UtilidadTexto.isEmpty(filtro.getTipoPacienteAcronimo())){
						criteria.add(Restrictions.eq("tipoPaciente.acronimo", filtro.getTipoPacienteAcronimo()));
					}
					if(filtro.getTipoAfiliadoAcronimo()==ConstantesBD.codigoTipoAfiliadoBeneficiario.charAt(0) ||
							filtro.getTipoAfiliadoAcronimo()==ConstantesBD.codigoTipoAfiliadoCotizante.charAt(0)){
						criteria.add(Restrictions.eq("tipoAfiliado.acronimo", filtro.getTipoAfiliadoAcronimo()));
					}
					if(filtro.getEstratoID()!=ConstantesBD.codigoNuncaValido){
						criteria.add(Restrictions.eq("estratoSocial.codigo", filtro.getEstratoID()));
					}
					if(filtro.getNaturalezaID()!=null && filtro.getNaturalezaID()!=ConstantesBD.codigoNuncaValido){
						criteria.add(Restrictions.eq("naturalezaPacientes.codigo", filtro.getNaturalezaID()));
					}
					if(filtro.getTipoMontoID()!=ConstantesBD.codigoNuncaValido){
						criteria.add(Restrictions.eq("tipoMonto.codigo", filtro.getTipoMontoID()));
					}
					if(!UtilidadTexto.isEmpty(filtro.getTipoDetalleAcronimo())){
						criteria.add(Restrictions.eq("detalleMonto.tipoDetalle", filtro.getTipoDetalleAcronimo()));
					}
					if(filtro.getPorcentaje()!=null && filtro.getPorcentaje()>0){
						criteria.add(Restrictions.eq("detalleMontoGeneral.porcentaje", filtro.getPorcentaje()));
					}
					if(filtro.getValor()!=null && filtro.getValor()>0){
						criteria.add(Restrictions.eq("detalleMontoGeneral.valor", filtro.getValor()));
					}
				}
			}			 
		}
		criteria.add(Restrictions.gt("montoCobro.codigo", 0));
		criteria.add(Restrictions.eq("detalleMonto.activo", true));
		
		criteria.setProjection(Projections.projectionList()				
			.add(Projections.property("montoCobro.codigo"),"idMontoCobro")
			.add(Projections.property("viaIngreso.codigo"),"viaIngresoID")
			.add(Projections.property("viaIngreso.nombre"),"viaIngresoNombre")
			.add(Projections.property("tipoPaciente.acronimo"),"tipoPacienteAcronimo")
			.add(Projections.property("tipoPaciente.nombre"),"tipoPacienteNombre")
			.add(Projections.property("estratoSocial.codigo"),"estratoID")
			.add(Projections.property("estratoSocial.descripcion"),"estratoDescripcion")
			.add(Projections.property("tipoMonto.codigo"),"tipoMontoID")
			.add(Projections.property("tipoMonto.nombre"),"tipoMontoNombre")
			.add(Projections.property("detalleMonto.tipoDetalle"),"tipoDetalleAcronimo")
			.add(Projections.property("detalleMonto.detalleCodigo"),"detalleCodigo")			
			.add(Projections.property("tipoAfiliado.acronimo"),"tipoAfiliadoAcronimo")
			.add(Projections.property("tipoAfiliado.nombre"),"tipoAfiliadoNombre")
			.add(Projections.property("naturalezaPacientes.codigo"),"naturalezaID")
			.add(Projections.property("naturalezaPacientes.nombre"),"naturalezaNombre")
			.add(Projections.property("detalleMontoGeneral.valor"),"valor")
			.add(Projections.property("detalleMontoGeneral.porcentaje"),"porcentaje")
			.add(Projections.property("detalleMontoGeneral.cantidadMonto"),"cantidadMonto")	
		);
				
		if(monto.getTipoTransaccion()!= null && !monto.getTipoTransaccion().equals("")){
			if(monto.getTipoTransaccion().equals("consultaMontosCobro")){
				
				criteria.addOrder(Order.asc("viaIngreso.nombre"));	
				criteria.addOrder(Order.asc("tipoPaciente.nombre"));
				criteria.addOrder(Order.asc("tipoAfiliado.nombre"));
				criteria.addOrder(Order.asc("estratoSocial.descripcion"));
			}
		}else{
			criteria.addOrder(Order.asc("viaIngreso.nombre"));
			criteria.addOrder(Order.asc("tipoPaciente.nombre"));
		}
		
		criteria.setResultTransformer(Transformers.aliasToBean(DTOResultadoBusquedaDetalleMontos.class));	
		
		ArrayList<DTOResultadoBusquedaDetalleMontos> listaMontos = 
			(ArrayList<DTOResultadoBusquedaDetalleMontos>)criteria.list();		
		return listaMontos;		
	}	
	
	
	/**
	 * Este Método se encarga de consultar los montos de cobro de un 
	 * convenio específico por su id y la fecha de vigencia, agrupados
	 * por el convenio y la fecha de vigencia 
	 * 
	 * @return ArrayList<MontosCobro>
	 * @author, Angela Maria Aguirre
	 */
	public ArrayList<MontosCobro> obtenerMontosCobroOrdenado(MontosCobro montoCobro){
		
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(MontosCobro.class,"montoCobro")
		.createAlias("montoCobro.convenios", "convenio");		
		
		criteria.add(Restrictions.gt("montoCobro.codigo", 0));
		criteria.add(Restrictions.eq("montoCobro.activo", true));
		
		
		if(montoCobro.getConvenios()!=null && montoCobro.getConvenios().getCodigo()>0){
			criteria.add(Restrictions.eq("convenio.codigo", montoCobro.getConvenios().getCodigo()));
		}		
		if(montoCobro.getVigenciaInicial()!=null){
			criteria.add(Restrictions.eq("montoCobro.vigenciaInicial", montoCobro.getVigenciaInicial()));
		}
		
		criteria.addOrder(Order.asc("convenio.nombre"));		
		criteria.addOrder(Order.asc("montoCobro.vigenciaInicial"));
		
		ArrayList<MontosCobro> listaMontos = (ArrayList<MontosCobro>)criteria.list();
		
		for(MontosCobro monto:listaMontos){
			monto.getConvenios().getCodigo();
			monto.getConvenios().getNombre();
		}
		
		return listaMontos;
	}	
	
	
	/**
	 * Este método se encarga de eliminar el registro
	 * de Montos de Cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 */
	public boolean eliminarMontoCobro(int idMontoCobro){
		boolean save = true;					
		try{
			MontosCobro montoCobro = super.findById(idMontoCobro);		
			super.delete(montoCobro);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo eliminar el registro de " +
					"Montos de Cobro: ",e);
		}				
		return save;		
	} 
	
	
	/**
	 * Este Método se encarga de consultar la fecha de vigencia máxima
	 * para un monto de cobro específico. 
	 * 
	 * @param int
	 * @return DtoConvenio
	 * @author, Angela Maria Aguirre
	*/
	public DTOMontosCobro obtenerFechaMaximaMonto(int codigo){
		try
		{
			HibernateUtil.beginTransaction();
		
			Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(MontosCobro.class, "monto")
				.createAlias("monto.convenios", "convenio",Criteria.LEFT_JOIN);
					
			
			Date fechaActual=UtilidadFecha.getFechaActualTipoBD();
			
			criteria.add(Restrictions.eq("convenio.codigo", codigo))
					.add(Restrictions.gt("monto.codigo", 0))
					.add(Restrictions.le("monto.vigenciaInicial", fechaActual));		
			
			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("monto.codigo"),"montoCobroID")
					.add(Projections.property("monto.vigenciaInicial"),"fechaVigenciaConvenio"));
			
			criteria.addOrder(Order.desc("monto.vigenciaInicial"));
			criteria.setResultTransformer(Transformers.aliasToBean(DTOMontosCobro.class));		
			
			ArrayList<DTOMontosCobro> listaMontos = (ArrayList<DTOMontosCobro>)criteria.list();		
			if(listaMontos!=null && listaMontos.size()>0){
				return listaMontos.get(0);									
			}						
		}
		catch (Exception e)
		{
			HibernateUtil.abortTransaction();
			Log4JManager.error(e);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.endTransaction();
		}
		return null;
	}
	
	
	/**
	 * obtenerMontosCobroBusqueda
	 * @param dtoFiltro DtoBusquedaMontosCobro
	 * @return
	 *
	 * @autor 
	 */
	public DtoBusquedaMontosCobro obtenerMontosCobroBusqueda(DtoFiltroBusquedaMontosCobro dtoFiltro) 
	{
		DtoBusquedaMontosCobro resultado=new DtoBusquedaMontosCobro();
		Date fecha=new Date();
		
		if(UtilidadTexto.isEmpty(dtoFiltro.getFechaAperturaCuenta())){
			fecha=UtilidadFecha.getFechaActualTipoBD();
		}
		else
		{
			fecha=UtilidadFecha.conversionFormatoFechaStringDate(dtoFiltro.getFechaAperturaCuenta());
		}
		
		/**
		 * Se consulta los montos de cobro por convenio y fecha vigencia
		 */
		String consulta = "SELECT codigo AS cod FROM montos_cobro " +
				"WHERE convenio = :codigoConvenio AND vigencia_inicial = GETVIGENCIACONVENIO(:codigoConvenio, :fecha)";
		SQLQuery criteriaGeneral = sessionFactory.getCurrentSession().createSQLQuery(consulta);
		criteriaGeneral.setParameter("codigoConvenio", dtoFiltro.getConvenio(), StandardBasicTypes.INTEGER);
		criteriaGeneral.setParameter("fecha", fecha, StandardBasicTypes.DATE);
		//Se convierte el resultado de la consulta a entero ya que este viene como BigDecimal
		criteriaGeneral.addScalar("cod", StandardBasicTypes.INTEGER);
		List<Integer> montos = (List<Integer>)criteriaGeneral.list();
		
		Log4JManager.info("dtoFiltro.getConvenio()-->"+dtoFiltro.getConvenio());
		Log4JManager.info("fecha-->"+fecha);
		
		if(montos!=null && !montos.isEmpty())
		{
			Criteria criteria = sessionFactory.getCurrentSession().createCriteria(MontosCobro.class,"montoCobro")
				.createAlias("montoCobro.detalleMontos"			, "detalleMonto")
				.createAlias("detalleMonto.viasIngreso"			, "viaIngreso")
				.createAlias("detalleMonto.tiposPaciente"		, "tipoPaciente")
				.createAlias("detalleMonto.estratosSociales"	, "estratoSocial")
				.createAlias("detalleMonto.tiposMonto"			, "tipoMonto")
				.createAlias("detalleMonto.tiposAfiliado"		, "tipoAfiliado")	
				.createAlias("detalleMonto.naturalezaPacientes"	, "naturalezaPacientes",Criteria.LEFT_JOIN)
				.createAlias("detalleMonto.detalleMontoGeneral"	, "detalleMontoGeneral",Criteria.LEFT_JOIN)
				.add(Restrictions.in("montoCobro.codigo", montos));
			
			criteria.add(Restrictions.eq("detalleMonto.viasIngreso.codigo"		,dtoFiltro.getViaIngreso()));
			criteria.add(Restrictions.eq("detalleMonto.tiposAfiliado.acronimo"	,dtoFiltro.getTipoAfiliado()));
			criteria.add(Restrictions.eq("detalleMonto.tiposPaciente.acronimo"	,dtoFiltro.getTipoPaciente()));
			criteria.add(Restrictions.eq("detalleMonto.estratosSociales.codigo"	,dtoFiltro.getClasificacionSocioEconomica()));
			criteria.add(Restrictions.eq("detalleMonto.activo"					,true)); 
			
			if (dtoFiltro.getNaturalezaPaciente() == -1 || dtoFiltro.getNaturalezaPaciente() == 0) {
				criteria.add(Restrictions.isNull("naturalezaPacientes.codigo"));
			} else {
				criteria.add(Restrictions.eq("naturalezaPacientes.codigo",dtoFiltro.getNaturalezaPaciente()));
			}
		
			Log4JManager.info("Montos Cobro-->"+montos.toString());
			Log4JManager.info("dtoFiltro.getViaIngreso()-->"+dtoFiltro.getViaIngreso());
			Log4JManager.info("dtoFiltro.getTipoAfiliado()-->"+dtoFiltro.getTipoAfiliado());
			Log4JManager.info("dtoFiltro.getTipoPaciente()-->"+dtoFiltro.getTipoPaciente());
			Log4JManager.info("dtoFiltro.getClasificacionSocioEconomica()-->"+dtoFiltro.getClasificacionSocioEconomica());
			Log4JManager.info("dtoFiltro.getNaturalezaPaciente()-->"+dtoFiltro.getNaturalezaPaciente());

			criteria.setProjection(Projections.projectionList()
					.add(Projections.property("viaIngreso.codigo")					,"viaIngresoID")
					.add(Projections.property("viaIngreso.nombre")					,"viaIngresoNombre")
					.add(Projections.property("tipoPaciente.acronimo")				,"tipoPacienteAcronimo")
					.add(Projections.property("tipoPaciente.nombre")				,"tipoPacienteNombre")
					.add(Projections.property("estratoSocial.codigo")				,"estratoID")
					.add(Projections.property("estratoSocial.descripcion")			,"estratoDescripcion")
					.add(Projections.property("tipoMonto.codigo")					,"tipoMontoID")
					.add(Projections.property("tipoMonto.nombre")					,"tipoMontoNombre")
					.add(Projections.property("detalleMonto.tipoDetalle")			,"tipoDetalleAcronimo")
					.add(Projections.property("detalleMonto.detalleCodigo")			,"detalleCodigo")
					.add(Projections.property("tipoAfiliado.acronimo")				,"tipoAfiliadoAcronimo")
					.add(Projections.property("tipoAfiliado.nombre")				,"tipoAfiliadoNombre")
					.add(Projections.property("naturalezaPacientes.codigo")			,"naturalezaID")
					.add(Projections.property("naturalezaPacientes.nombre")			,"naturalezaNombre")
					.add(Projections.property("detalleMontoGeneral.valor")			,"valor")
					.add(Projections.property("detalleMontoGeneral.porcentaje")		,"porcentaje")
					.add(Projections.property("detalleMontoGeneral.cantidadMonto")	,"cantidadMonto")
				);
						
			criteria.addOrder(Order.asc("detalleMonto.tipoDetalle"));
			criteria.setResultTransformer(Transformers.aliasToBean(DTOResultadoBusquedaDetalleMontos.class));
				
			ArrayList<DTOResultadoBusquedaDetalleMontos> listaMontos = (ArrayList<DTOResultadoBusquedaDetalleMontos>)criteria.list();
			
			if(listaMontos!=null&&listaMontos.size()>0)
			{
				resultado.setResultado(new ResultadoBoolean(true));
				resultado.setMontosCobro(listaMontos);
			}
			else
			{
				resultado.setMontosCobro(new ArrayList<DTOResultadoBusquedaDetalleMontos>());
				resultado.setResultado(new ResultadoBoolean(false,"No existe un Monto de Cobro que aplique al Convenio del Paciente."));
			}
		}
		else
		{
			ConvenioDelegate daoConvenio=new ConvenioDelegate();
			Convenios conv=daoConvenio.findById(dtoFiltro.getConvenio());
			resultado.setResultado(new ResultadoBoolean(false,"No existe un Monto vigente para el Convenio "+conv.getNombre()+". Por favor verifique"));
		}
		return resultado;
	}
	
	
	/**
	 * Este Método se encarga de guardar un monto de
	 * cobro
	 * 
	 * @param MontosCobro
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 */
	public boolean guardarMontosCobro(MontosCobro montoCobro){
		boolean save = true;					
		try{
			super.persist(montoCobro);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro de " +
					"Montos de Cobro: ",e);
		}				
		return save;				
	}

	
	/**
	 * @param codigoDetalleMonto
	 * @return
	 *
	 * @autor Cristhian Murillo
	*/
	public DTOResultadoBusquedaDetalleMontos obtenerDetalleMontoCobroPorId( int codigoDetalleMonto) 
	{
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(MontosCobro.class,"montoCobro")
		.createAlias("montoCobro.detalleMontos", "detalleMonto")
		.createAlias("detalleMonto.viasIngreso", "viaIngreso")
		.createAlias("detalleMonto.tiposPaciente", "tipoPaciente")
		.createAlias("detalleMonto.estratosSociales", "estratoSocial")
		.createAlias("detalleMonto.tiposMonto", "tipoMonto")
		.createAlias("detalleMonto.tiposAfiliado", "tipoAfiliado")	
		.createAlias("detalleMonto.naturalezaPacientes", "naturalezaPacientes",Criteria.LEFT_JOIN)
		.createAlias("detalleMonto.detalleMontoGeneral", "detalleMontoGeneral",Criteria.LEFT_JOIN)
		.add(Restrictions.eq("detalleMonto.detalleCodigo", codigoDetalleMonto));
		

		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("viaIngreso.codigo"),"viaIngresoID")
				.add(Projections.property("viaIngreso.nombre"),"viaIngresoNombre")
				.add(Projections.property("tipoPaciente.acronimo"),"tipoPacienteAcronimo")
				.add(Projections.property("tipoPaciente.nombre"),"tipoPacienteNombre")
				.add(Projections.property("estratoSocial.codigo"),"estratoID")
				.add(Projections.property("estratoSocial.descripcion"),"estratoDescripcion")
				.add(Projections.property("tipoMonto.codigo"),"tipoMontoID")
				.add(Projections.property("tipoMonto.nombre"),"tipoMontoNombre")
				.add(Projections.property("detalleMonto.tipoDetalle"),"tipoDetalleAcronimo")
				.add(Projections.property("detalleMonto.detalleCodigo"),"detalleCodigo")
				.add(Projections.property("tipoAfiliado.acronimo"),"tipoAfiliadoAcronimo")
				.add(Projections.property("tipoAfiliado.nombre"),"tipoAfiliadoNombre")
				.add(Projections.property("naturalezaPacientes.codigo"),"naturalezaID")
				.add(Projections.property("naturalezaPacientes.nombre"),"naturalezaNombre")
				.add(Projections.property("detalleMontoGeneral.valor"),"valor")
				.add(Projections.property("detalleMontoGeneral.porcentaje"),"porcentaje")
				.add(Projections.property("detalleMontoGeneral.cantidadMonto"),"cantidadMonto")
			);
					
			criteria.addOrder(Order.asc("detalleMonto.tipoDetalle"));
			criteria.setResultTransformer(Transformers.aliasToBean(DTOResultadoBusquedaDetalleMontos.class));
			
			DTOResultadoBusquedaDetalleMontos resultado=(DTOResultadoBusquedaDetalleMontos)criteria.uniqueResult();
			
		return resultado;
	}
	

	/**
	 * Este Método se encarga de consultar el monto de cobro de un 
	 * convenio específico por su id y la fecha de vigencia seleccionada
	 * por el usuario
	 * 
	 * @param DTOMontosCobro
	 * @return DTOMontosCobro
	 * @author, Angela Maria Aguirre
	 */
	public DTOMontosCobro obtenerMontosCobroPorConvenioYFechaVigenciaPostulada(DTOMontosCobro monto){
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(MontosCobro.class,"montoCobro");
		
		criteria.add(Restrictions.eq("convenio.codigo", monto.getConvenio().getCodigo()))
		.add(Restrictions.eq("montoCobro.vigenciaInicial", monto.getFechaVigenciaConvenio()))
		.add(Restrictions.gt("montoCobro.codigo", 0));
		
		DTOMontosCobro montoCobro = (DTOMontosCobro)criteria.uniqueResult();
		
		return montoCobro;
	}
	
	
	/**
	 * Este Método se encarga de obtener el monto de cobro
	 * por su id
	 * 
	 * @param int id
	 * @return MontosCobro
	 * @author, Angela Maria Aguirre
	 */
	public MontosCobro obtenerMontoCobroPorID(int id){
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(MontosCobro.class,"montoCobro")
		.createAlias("montoCobro.convenios", "convenio");
		
		criteria.add(Restrictions.eq("montoCobro.codigo", id));
		
		MontosCobro montoCobro  = (MontosCobro)criteria.uniqueResult();
		if(montoCobro!=null){		
			montoCobro.getConvenios().getCodigo();
			montoCobro.getConvenios().getNombre();
		}		
		return montoCobro;
	}
	
	
}
