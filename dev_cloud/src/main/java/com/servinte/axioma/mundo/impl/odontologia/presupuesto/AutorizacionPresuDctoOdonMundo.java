
package com.servinte.axioma.mundo.impl.odontologia.presupuesto;

import java.math.BigDecimal;
import java.util.ArrayList;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;

import com.princetonsa.dto.odontologia.DtoDescuentoOdontologicoAtencion;
import com.princetonsa.dto.odontologia.DtoDetalleDescuentoOdontologico;
import com.princetonsa.mundo.odontologia.DescuentoOdontologico;
import com.princetonsa.mundo.odontologia.DetalleDescuentoOdontologico;
import com.servinte.axioma.dao.fabrica.odontologia.presupuesto.PresupuestoFabricaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IAutorizacionPresuDctoOdonDAO;
import com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IAutorizacionPresuDctoOdonMundo;
import com.servinte.axioma.orm.AutorizacionPresuDctoOdon;



/**
 * Esta clase se encarga de implementar la l&oacute;gica de negocio 
 * para la entidad {@link AutorizacionPresuDctoOdon}
 *
 * @autor Jorge Armando Agudelo Quintero
 * @see IAutorizacionPresuDctoOdonDAO
 */

public class AutorizacionPresuDctoOdonMundo implements IAutorizacionPresuDctoOdonMundo {

	
	IAutorizacionPresuDctoOdonDAO autorizacionPresuDctoOdonDAO;
	
	/**
	 * Constructor de la clase
	 */
	public AutorizacionPresuDctoOdonMundo() {
		inicializar();
	}
	
	/**
	 * M&eacute;todo que se encarga de inicializar el objeto DAO encargado de manejar 
	 * la capa de integraci&oacute;n de los objetos {@link AutorizacionPresuDctoOdon}
	 */
	private void inicializar() {
		
		autorizacionPresuDctoOdonDAO = PresupuestoFabricaDAO.crearAutorizacionPresuDctoOdonDAO();
	}
	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IAutorizacionPresuDctoOdonMundo#eliminarAutorizacionPresuDctoOdon(long)
	 */
	@Override
	public boolean eliminarAutorizacionPresuDctoOdon(long codigoAutorizacionPresuDctoOdon) {
		
		return autorizacionPresuDctoOdonDAO.eliminarAutorizacionPresuDctoOdon(codigoAutorizacionPresuDctoOdon);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IAutorizacionPresuDctoOdonMundo#guardarAutorizacionPresuDctoOdon(com.servinte.axioma.orm.AutorizacionPresuDctoOdon, long, java.lang.String)
	 */
	@Override
	public boolean guardarAutorizacionPresuDctoOdon(AutorizacionPresuDctoOdon autorizacionPresuDctoOdon, long codigoDescuento, String tipoDescuento) {
		
		int diasVigencia = ConstantesBD.codigoNuncaValido;
		
		if(tipoDescuento.equals(ConstantesIntegridadDominio.acronimoPorValorPresupuesto)){
			
			DtoDetalleDescuentoOdontologico descuentoOdontologico =  new DtoDetalleDescuentoOdontologico();
			
			descuentoOdontologico.setCodigo(codigoDescuento);

			ArrayList<DtoDetalleDescuentoOdontologico> descuentosOdontologicos = DetalleDescuentoOdontologico.cargar(descuentoOdontologico);
			
			for (DtoDetalleDescuentoOdontologico descuento : descuentosOdontologicos) {
				
				BigDecimal dias =  new BigDecimal(descuento.getDiasVigencia());
				diasVigencia = dias.intValue();
				
				break;
			}
			
		}else if(tipoDescuento.equals(ConstantesIntegridadDominio.acronimoPorAtencion)){
		
			DtoDescuentoOdontologicoAtencion descuentoOdontologicoAtencion =  new DtoDescuentoOdontologicoAtencion();
			
			descuentoOdontologicoAtencion.setConsecutivo(codigoDescuento);

			ArrayList<DtoDescuentoOdontologicoAtencion> descuentosAtencion = DescuentoOdontologico.cargarAtencion(descuentoOdontologicoAtencion);
			
			for (DtoDescuentoOdontologicoAtencion descuento : descuentosAtencion) {
				
				diasVigencia =  descuento.getDiasVigencia();
				break;
			}
		}
		
		if(diasVigencia > 0){
			
			autorizacionPresuDctoOdon.setDiasVigencia(diasVigencia);
			return autorizacionPresuDctoOdonDAO.guardarAutorizacionPresuDctoOdon(autorizacionPresuDctoOdon);
		
		}else{
			
			return false;
		}
	}

}
