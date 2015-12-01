package com.servinte.axioma.mundo.interfaz.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOMontosCobro;
import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.MontosCobro;
import com.servinte.axioma.orm.Usuarios;

/**
 * Esta clase se encarga de definir los m�todos de negocio de 
 * la entidad Montos de Cobro
 * 
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public interface IMontosCobroMundo {
	
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
	public ArrayList<DTOResultadoBusquedaDetalleMontos> obtenerMontosCobro(DTOMontosCobro monto);
	
	/**
	 * 
	 * Este M�todo se encarga de consultar los montos de cobro de un 
	 * convenio espec�fico por su id y la fecha de vigencia
	 * 
	 * @param DtoConvenio
	 * @return ArrayList<DTOMontosCobro>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOMontosCobro> obtenerMontosCobroEstructurado(DTOMontosCobro montosCobro,UsuarioBasico usuarioSesion);
	
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
	public boolean eliminarMontoCobro(int idMontoCobro);
	
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
	public DTOMontosCobro obtenerFechaMaximaMonto(int codigo);
	
	/**
	 * 
	 * Este M�todo se encarga de guardar los datos de un monto de
	 * cobro
	 * 
	 * @param DTOMontosCobro monto, UsuarioBasico usuarioSesion
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void guarDatosDetalleMontoCobro(DTOMontosCobro monto, UsuarioBasico usuarioSesion);
	
	/**
	 * 
	 * Este M�todo se encarga de guardar un monto de
	 * cobro
	 * 
	 * @param MontosCobro, Usuarios
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarMontosCobro(MontosCobro montoCobro,Usuarios usuario);
	
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
	public MontosCobro obtenerMontoCobroPorID(int id);

	public DTOResultadoBusquedaDetalleMontos obtenerDetalleMontoCobroPorId(
			int codigoDetalleMonto);
	
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
	public DTOMontosCobro obtenerMontosCobroPorConvenioYFechaVigenciaPostulada(DTOMontosCobro monto);
	
	/**
	 * 
	 * Este M�todo se encarga de retornar los montos de cobro
	 * organizados con sus detalles seg�n el convenvio y la fecha 
	 * de vigencia del monto de cobro
	 * 
	 * @param DTOMontosCobro
	 * @return ArrayList<DTOMontosCobro>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOMontosCobro> consultaMontosCobroEstructurado(DTOMontosCobro dtoMontoFiltro);
	
	
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
	public ArrayList<MontosCobro> obtenerMontosCobroOrdenado(MontosCobro montoCobro);

}
