package com.servinte.axioma.servicio.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DTOMontosCobro;
import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IMontosCobroMundo;
import com.servinte.axioma.orm.MontosCobro;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.servicio.interfaz.facturacion.IMontosCobroServicio;

/**
 * Esta clase se encarga de ejecutar los métodos de negocio para
 * la entidad Montos de Cobro
 * 
 * @author Angela Maria Aguirre
 * @since 26/08/2010
 */
public class MontosCobroServicio implements IMontosCobroServicio {
	
	IMontosCobroMundo mundo;
	
	public MontosCobroServicio(){
		mundo = FacturacionFabricaMundo.crearMontosCobroMundo();
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los montos de cobro de un 
	 * convenio específico por su id y la fecha de vigencia
	 * 
	 * @param DTOMontosCobro
	 * @return ArrayList<DTOMontosCobroDetalle>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOResultadoBusquedaDetalleMontos> obtenerMontosCobro(DTOMontosCobro monto){
		return mundo.obtenerMontosCobro(monto);
	}
	
	/**
	 * 
	 * Este Método se encarga de retornar los montos de cobro
	 * organizados con sus detalles según la vía de ingreso y el
	 * tipo de paciente
	 * 
	 * @param DtoConvenio
	 * @return ArrayList<DTOMontosCobro>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public ArrayList<DTOMontosCobro> obtenerMontosCobroEstructurado(
			DTOMontosCobro montosCobro,UsuarioBasico usuarioSesion ){
		return mundo.obtenerMontosCobroEstructurado(montosCobro, usuarioSesion);
	}
	
	/**
	 * 
	 * Este método se encarga de eliminar el registro
	 * de Montos de Cobro
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean eliminarMontoCobro(int idMontoCobro){
		return mundo.eliminarMontoCobro(idMontoCobro);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar la fecha de vigencia máxima
	 * para un monto de cobro específico. 
	 * 
	 * @param int
	 * @return DtoConvenio
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOMontosCobro obtenerFechaMaximaMonto(int codigo){
		return mundo.obtenerFechaMaximaMonto(codigo);
	}
	
	/**
	 * 
	 * Este Método se encarga de guardar los datos de un monto de
	 * cobro
	 * 
	 * @param DTOMontosCobro monto, UsuarioBasico usuarioSesion
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void guarDatosDetalleMontoCobro(DTOMontosCobro monto, UsuarioBasico usuarioSesion){
		mundo.guarDatosDetalleMontoCobro(monto, usuarioSesion);
	}
	
	/**
	 * 
	 * Este Método se encarga de guardar un monto de
	 * cobro
	 * 
	 * @param MontosCobro
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean guardarMontosCobro(MontosCobro montoCobro,Usuarios usuario){
		return mundo.guardarMontosCobro(montoCobro,usuario);
	}
	
	/**
	 * 
	 * Este Método se encarga de buscar un monto de
	 * cobro por su id
	 * 
	 * @param MontosCobro
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public MontosCobro obtenerMontoCobroPorID(int id){
		return mundo.obtenerMontoCobroPorID(id);
	}

	@Override
	public DTOResultadoBusquedaDetalleMontos obtenerDetalleMontoCobroPorId(
			int codigoDetalleMonto) {
		return mundo.obtenerDetalleMontoCobroPorId(codigoDetalleMonto);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar el monto de cobro de un 
	 * convenio específico por su id y la fecha de vigencia seleccionada
	 * por el usuario
	 * 
	 * @param DTOMontosCobro
	 * @return DTOMontosCobro
	 * @author, Angela Maria Aguirre
	 *
	 */
	public DTOMontosCobro obtenerMontosCobroPorConvenioYFechaVigenciaPostulada(DTOMontosCobro monto){
		return mundo.obtenerMontosCobroPorConvenioYFechaVigenciaPostulada(monto);
	}
	
	/**
	 * 
	 * Este Método se encarga de retornar los montos de cobro
	 * organizados con sus detalles según el convenvio y la fecha 
	 * de vigencia del monto de cobro
	 * 
	 * @param DTOMontosCobro
	 * @return ArrayList<DTOMontosCobro>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DTOMontosCobro> consultaMontosCobroEstructurado(DTOMontosCobro dtoMontoFiltro){
		return mundo.consultaMontosCobroEstructurado(dtoMontoFiltro);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los montos de cobro de un 
	 * convenio específico por su id y la fecha de vigencia, agrupados
	 * por el convenio y la fecha de vigencia 
	 * 
	 * @return ArrayList<MontosCobro>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<MontosCobro> obtenerMontosCobroOrdenado(MontosCobro montoCobro){
		return mundo.obtenerMontosCobroOrdenado(montoCobro);
	}

}
