package com.servinte.axioma.orm.delegate.tesoreria;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;

import com.princetonsa.dto.tesoreria.DTOCambioResponsableDetFaltanteSobrante;
import com.servinte.axioma.orm.DetFaltanteSobrante;
import com.servinte.axioma.orm.DetFaltanteSobranteHome;

/**
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto detalle faltante sobrante.
 * 
 * @author Angela Maria Aguirre
 * @since 22/07/2010
 */
public class DetFaltanteSobranteDelegate extends DetFaltanteSobranteHome {

	/**
	 * 
	 * Este m&eacute;todo se encarga de consular por ID los datos del detalle de un
	 * registro faltante sobrante
	 * 
	 * @param DetFaltanteSobrante
	 * @return DetFaltanteSobrante
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DetFaltanteSobrante consultarRegistroFaltanteSobrantePorID(DetFaltanteSobrante registro){
		
		return (DetFaltanteSobrante)sessionFactory.getCurrentSession()
		.createCriteria(DetFaltanteSobrante.class)		
		.add(Restrictions.eq("codigoPk", registro.getCodigoPk()))
		.uniqueResult();
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de actualizar el detalle
	 * de un faltante sobrante
	 * 
	 * @param DetFaltanteSobrante
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarFaltanteSobrante(DetFaltanteSobrante registro){
		boolean save = false;
		try{
			super.merge(registro);
			save = true;
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo actualizar el historial del detalle de faltante sobrante: ",e);
		}		
		return save;
	}
	
	/**
	 * Este m&eacute;todo se encarga de consultar los datos necesarios
	 * para el cambio del responsable del detalle de faltante/sobrante
	 * 
	 * @param DTOCambioResponsableDetFaltanteSobrante, con los datos necesarios para realizar la consulta  
	 * @return List<DTOCambioResponsableDetFaltanteSobrante>, con los datos obtenidos en la consulta
	 * @author Angela Aguirre
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DTOCambioResponsableDetFaltanteSobrante> busquedaFaltantesSobrantes(DTOCambioResponsableDetFaltanteSobrante dto){
		Criteria criteria = sessionFactory.getCurrentSession()
		.createCriteria(DetFaltanteSobrante.class, "det_falt_sobra")
			.createCriteria("det_falt_sobra.faltanteSobrante","faltante_sobrante")
			.createAlias("det_falt_sobra.usuarios", "usuario_responsable")
			.createAlias("det_falt_sobra.docSopMovimCajas", "documento_soporte")
			.createAlias("documento_soporte.movimientosCaja", "movimiento_caja")
			.createAlias("movimiento_caja.turnoDeCaja", "turno_caja")
			.createAlias("turno_caja.centroAtencion", "centro_atencion")
			.createAlias("turno_caja.cajas", "caja")			
			.createAlias("turno_caja.usuarios", "cajero")
			.createAlias("movimiento_caja.tiposMovimientoCaja", "tipo_movimiento_caja")
			
			.add(Restrictions.between("faltante_sobrante.fechaModifica",
						dto.getFechaGeneracionInicial(), dto.getFechaGeneracionFin()));
		
		if(!UtilidadTexto.getBoolean(dto.getFuncionalidadConsultaHistorico())){
			criteria.add(Restrictions.eq("faltante_sobrante.estado", 
					ConstantesIntegridadDominio.acronimoEstadoFaltanteSobranteGenerado))
			.add(Restrictions.eq("det_falt_sobra.contabilizado", ConstantesBD.acronimoNo));
		}else{
			if(!UtilidadTexto.isEmpty(dto.getEstadoFaltanteSobrante())){			
				criteria.add(Restrictions.eq("faltante_sobrante.estado",dto.getEstadoFaltanteSobrante()));
			}			
		}
		if(dto.getConsecutivoCA()!= ConstantesBD.codigoNuncaValido){
			criteria.add(Restrictions.eq("centro_atencion.consecutivo",dto.getConsecutivoCA()));
		}
		if(!UtilidadTexto.isEmpty(dto.getTipoDiferencia())){
			criteria.add(Restrictions.eq("det_falt_sobra.tipoDiferencia",dto.getTipoDiferencia()));
		}
		if(dto.getConsecutivo()!=null){
			criteria.add(Restrictions.eq("det_falt_sobra.consecutivo",dto.getConsecutivo()));
		}
		if( dto.getConsecutivoCaja() != ConstantesBD.codigoNuncaValido){
			criteria.add(Restrictions.eq("caja.consecutivo",dto.getConsecutivoCaja()));		
		}
		if(!UtilidadTexto.isEmpty(dto.getLoginCajero())){
			criteria.add(Restrictions.eq("cajero.login",dto.getLoginCajero()));
		}
		if(dto.getGeneradoEn() != ConstantesBD.codigoNuncaValido){			
			criteria.add(Restrictions.eq("tipo_movimiento_caja.codigo",dto.getGeneradoEn()));
		}		
		
		criteria.setProjection(Projections.projectionList()
				.add( Projections.property("det_falt_sobra.codigoPk"), "idDetalleFaltanteSobrante")
				.add( Projections.property("det_falt_sobra.tipoDiferencia"), "tipoDiferencia")			
				.add( Projections.property("faltante_sobrante.fechaModifica"), "fechaGeneracionInicial")
				.add( Projections.property("faltante_sobrante.horaModifica"), "horaModifica")
				.add( Projections.property("tipo_movimiento_caja.descripcion"), "nombreGeneradoEn")
				.add( Projections.property("det_falt_sobra.valorDiferencia"), "valor")
				.add( Projections.property("usuario_responsable.login"), "loginUsuarioResponsable")
				.add(Projections.property("det_falt_sobra.consecutivo"), "consecutivo")
				.add(Projections.property("tipo_movimiento_caja.descripcion"),"nombreGeneradoEn")
				.add(Projections.property("faltante_sobrante.estado"),"estadoFaltanteSobrante")
				.add(Projections.property("det_falt_sobra.contabilizado"),"contabilizado"))
		.addOrder(Order.desc("faltante_sobrante.fechaModifica"))	
		.setResultTransformer( Transformers.aliasToBean(DTOCambioResponsableDetFaltanteSobrante.class));
		ArrayList<DTOCambioResponsableDetFaltanteSobrante> listafaltanteSobrante=(ArrayList)criteria.list();
		
		return listafaltanteSobrante;
		
	}	

	/**
	 * Devuelve el total de los detalles de los Faltantes / Sobrantes asociados a las Entregas a Transportadora de Valores
	 *
	 * @param movimientosCaja
	 * @param fecha
	 * @param estado
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public double obtenerTotalDiferenciaEfectivoEntregasTransportadora (Set<Long> setIdDetalleFaltanteSobrante) 
	{

		double valorTotalFaltanteSobrante = 0;
		
		try{
			
			List<BigDecimal> listaValores = sessionFactory.getCurrentSession().createCriteria(DetFaltanteSobrante.class)
			.setProjection(Projections.property("valorDiferencia"))
			.add(Restrictions.in("codigoPk", setIdDetalleFaltanteSobrante.toArray())) 
			.list();
			
			if(listaValores!=null && listaValores.size()>0){
				
				for (BigDecimal bigDecimal : listaValores) {
				
					valorTotalFaltanteSobrante += bigDecimal.doubleValue();
				}
			}
		
		} catch (Exception e) {
			
			return 0;
		}
		
		return valorTotalFaltanteSobrante;
	}
}
