package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.princetonsa.dto.tesoreria.DtoFaltanteSobrante;
import com.servinte.axioma.orm.FaltanteSobrante;
import com.servinte.axioma.orm.FaltanteSobranteHome;

/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class FaltanteSobranteDelegate extends FaltanteSobranteHome{
	
	
	/**
	 * Retorna una lista de DTO con la informaci&oacute;n b&aacute;sica de los detalles faltantes
	 * del movimiento de caja enviado
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<DtoFaltanteSobrante> obtenerFaltantesSobrantesPorMovimiento(long idMovimiento)
	{
		List<DtoFaltanteSobrante> listafaltanteSobrante = (List<DtoFaltanteSobrante>) sessionFactory.getCurrentSession()
			.createCriteria(FaltanteSobrante.class)
			.createAlias("detFaltanteSobrantes", "det_falt_sobra")
			.createAlias("detFaltanteSobrantes.docSopMovimCajas", "doc_soporte")
			.createAlias("doc_soporte.movimientosCaja", "movimiento_caja")
			.createAlias("doc_soporte.formasPago", "forma_pago")
			
			.add(Restrictions.eq("movimiento_caja.codigoPk", idMovimiento))
			
			.setProjection(Projections.projectionList()
				.add( Projections.property("det_falt_sobra.codigoPk"), "idDetalleFaltanteSobrante")
				.add( Projections.property("det_falt_sobra.valorDiferencia"), "valorDiferencia")
				.add( Projections.property("det_falt_sobra.tipoDiferencia"), "tipoDiferencia")
				.add( Projections.property("forma_pago.descripcion"), "formaPago")
				.add( Projections.property("forma_pago.consecutivo"), "tipoFormaPago")
			)
			.setResultTransformer( Transformers.aliasToBean(DtoFaltanteSobrante.class) )
	        .list();
		
		return listafaltanteSobrante;
	}

	
	/**
	 * 
	 * Este m&eacute;todo se encarga de consular los datos del registro faltante
	 * sobrante por ID.
	 * 
	 * @param FaltanteSobrante
	 * @return FaltanteSobrante
	 * @author, Angela Maria Aguirre
	 *
	 */
	public FaltanteSobrante consultarRegistroFaltanteSobrantePorID(FaltanteSobrante registro){
		
		return (FaltanteSobrante)sessionFactory.getCurrentSession()
		.createCriteria(FaltanteSobrante.class)		
		.add(Restrictions.eq("codigoPk", registro.getCodigoPk()))
		.uniqueResult();
	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de actualizar el registro
	 * faltante sobrante.
	 * 
	 * @param FaltanteSobrante
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarFaltanteSobrante(FaltanteSobrante registro){
		boolean save = false;
		try{
			super.merge(registro);
			save = true;
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el historial de faltante sobrante: ",e);
		}		
		return save;
	}
}
