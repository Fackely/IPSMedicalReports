package com.servinte.axioma.dao.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOMontosCobro;
import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.servinte.axioma.dao.interfaz.facturacion.IMontosCobroDAO;
import com.servinte.axioma.orm.MontosCobro;
import com.servinte.axioma.orm.delegate.facturacion.MontosCobroDelegate;

/**
 * Esta clase se encarga de definir los m�todos para
 * la entidad Montos de Cobro
 * 
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class MontosCobroHibernateDAO implements IMontosCobroDAO {
	
	/**
	 * Instancia de MontosCobroDelegate
	 */
	MontosCobroDelegate delegate;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public MontosCobroHibernateDAO(){
		delegate = new MontosCobroDelegate();
	}
	
	/**
	 * 
	 * Este M�todo se encarga de consultar los montos de cobro de un 
	 * convenio espec�fico por su id y la fecha de vigencia
	 * 
	 * @param DTOMontosCobro
	 * @return ArrayList<DTOMontosCobroDetalle>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOResultadoBusquedaDetalleMontos> obtenerMontosCobro(DTOMontosCobro monto){
		return delegate.obtenerMontosCobro(monto);
	}
	
	/**
	 * 
	 * Este m�todo se encarga de eliminar el registro
	 * de Montos de Cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarMontoCobro(int idMontoCobro){
		return delegate.eliminarMontoCobro(idMontoCobro);
	}
	
	/**
	 * 
	 * Este M�todo se encarga de consultar la fecha de vigencia m�xima
	 * para un monto de cobro espec�fico. 
	 * 
	 * @param int
	 * @return DtoConvenio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOMontosCobro obtenerFechaMaximaMonto(int codigo){
		return delegate.obtenerFechaMaximaMonto(codigo);
	}
	
	/**
	 * 
	 * Este M�todo se encarga de guardar un monto de
	 * cobro
	 * 
	 * @param MontosCobro
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarMontosCobro(MontosCobro montoCobro){
		return delegate.guardarMontosCobro(montoCobro);
	}
	
	/**
	 * 
	 * Este M�todo se encarga de buscar un monto de
	 * cobro por su id
	 * 
	 * @param MontosCobro
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public MontosCobro obtenerMontoCobroPorID(int id){
		return delegate.obtenerMontoCobroPorID(id);
	}

	@Override
	public DTOResultadoBusquedaDetalleMontos obtenerDetalleMontoCobroPorId(
			int codigoDetalleMonto) {
		return delegate.obtenerDetalleMontoCobroPorId(codigoDetalleMonto);
	}
	
	/**
	 * 
	 * Este M�todo se encarga de consultar el monto de cobro de un 
	 * convenio espec�fico por su id y la fecha de vigencia seleccionada
	 * por el usuario
	 * 
	 * @param DTOMontosCobro
	 * @return DTOMontosCobro
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOMontosCobro obtenerMontosCobroPorConvenioYFechaVigenciaPostulada(DTOMontosCobro monto){
		return delegate.obtenerMontosCobroPorConvenioYFechaVigenciaPostulada(monto);
	}
	
	/**
	 * 
	 * Este M�todo se encarga de consultar los montos de cobro de un 
	 * convenio espec�fico por su id y la fecha de vigencia, agrupados
	 * por el convenio y la fecha de vigencia 
	 * 
	 * @return ArrayList<MontosCobro>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<MontosCobro> obtenerMontosCobroOrdenado(MontosCobro montoCobro){
		return delegate.obtenerMontosCobroOrdenado(montoCobro);
	}


}
