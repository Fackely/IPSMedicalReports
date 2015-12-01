package com.servinte.axioma.dao.impl.tesoreria;

import java.util.List;

import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.servinte.axioma.dao.interfaz.tesoreria.IDocSopMovimCajasDAO;
import com.servinte.axioma.orm.DocSopMovimCajas;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.delegate.tesoreria.DocSopMovimCajasDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IDocSopMovimCajasDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see DocSopMovimCajasDelegate
 */
public class DocSopMovimCajasHibernateDAO implements IDocSopMovimCajasDAO {

	private DocSopMovimCajasDelegate docSopMovimCajasDelegate;

	
	public DocSopMovimCajasHibernateDAO() {
		docSopMovimCajasDelegate  = new DocSopMovimCajasDelegate();
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IDocSopMovimCajasDAO#obtenerDetallesDocumentosParaAceptacion(long)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerDetallesDocumentosParaAceptacion(long idMovimiento) {
		return docSopMovimCajasDelegate.obtenerDetallesDocumentosParaAceptacion(idMovimiento);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IDocSopMovimCajasDAO#obtenerDetallesDocumentosParaAceptacionEntidad(com.princetonsa.dto.tesoreria.DtoDetalleDocSopor, long)
	 */
	@Override
	public DtoDetalleDocSopor obtenerDetallesDocumentosParaAceptacionEntidad(DtoDetalleDocSopor dtoDetDocumento, long idMovimiento) {
		
		return docSopMovimCajasDelegate.obtenerDetallesDocumentosParaAceptacionEntidad(dtoDetDocumento, idMovimiento);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IDocSopMovimCajasDAO#obtenerDetallesDocumentosParaAceptacionEfectivo(long)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerDetallesDocumentosParaAceptacionEfectivo(long idMovimiento) {
		
		return docSopMovimCajasDelegate.obtenerDetallesDocumentosParaAceptacionEfectivo(idMovimiento);
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IDocSopMovimCajasDAO#obtenerDocSoportePorMovimiento(long)
	 */
	@Override
	public List<DocSopMovimCajas> obtenerDocSoportePorMovimiento(long codigoMovimientosCaja){
		
		return docSopMovimCajasDelegate.obtenerDocSoportePorMovimiento(codigoMovimientosCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IDocSopMovimCajasDAO#obtenerEntrTransYCajaMayorPpalNoFormaPagoNinguna(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerEntrTransYCajaMayorPpalNoFormaPagoNinguna(MovimientosCaja movimientosCaja) {
		
		return docSopMovimCajasDelegate.obtenerEntrTransYCajaMayorPpalNoFormaPagoNinguna(movimientosCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IDocSopMovimCajasDAO#obtenerAceptacionesCajaFormaPagoNinguno(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerAceptacionesCajaNoFormaPagoNinguno(MovimientosCaja movimientosCaja) {
		
		return docSopMovimCajasDelegate.obtenerAceptacionesCajaNoFormaPagoNinguno(movimientosCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.IDocSopMovimCajasDAO#obtenerTotalesEntregasFormaPagoNinguno(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerTotalesEntregasFormaPagoNinguno(MovimientosCaja movimientosCaja) {
		
		return docSopMovimCajasDelegate.obtenerTotalesEntregasFormaPagoNinguno(movimientosCaja);
	}
}
