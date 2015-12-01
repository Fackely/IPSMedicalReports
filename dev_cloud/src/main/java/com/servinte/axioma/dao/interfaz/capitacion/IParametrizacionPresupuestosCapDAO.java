package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.ArrayList;

import com.servinte.axioma.dto.capitacion.DtoNivelesAtencionPresupuestoParametrizacionGeneral;
import com.servinte.axioma.dto.capitacion.DtoParamPresupCap;
import com.servinte.axioma.orm.NivelAtencion;
import com.servinte.axioma.orm.ParamPresupuestosCap;

/**
 * Define la logica de negocio relacionada con la parametrización
 * del presupuesto de capitación
 * @author diecorqu
 * 
 */
public interface IParametrizacionPresupuestosCapDAO {

	/**
	 * Este método retorna la parametrizacion de presupuesto capitado 
	 * por contrato y fecha de vigencia del contrato
	 * 
	 * @param codigo del contrato
	 * @param año de vigencia del contrato
	 * @return PametrizacionPresupuesto
	 * @author diecorqu
	 */
	public ParamPresupuestosCap obtenerParametrizacionPresupuestoCapitado(int codContrato, String anioVigencia);
	
	/**
	 * Este método retorna la parametrizacion de presupuesto capitado 
	 * por codigo
	 * 
	 * @param codigo de parametrizacion de presupuesto
	 * @return PametrizacionPresupuesto
	 * @author diecorqu
	 */
	public ParamPresupuestosCap findById(long codParametrizacion);
	
	/**
	 * Este método lista todas las parametrizaciones de presupuesto 
	 * capitado creadas
	 * 
	 * @return ArrayList<ParamPresupuestosCap>
	 * @author diecorqu
	 */
	public ArrayList<ParamPresupuestosCap> listarParametrizacionesPresupuestoCap();
	
	/**
	 * Este método lista las parametrizaciones de presupuesto capitado 
	 * por contrato
	 * 
	 * @param codigo del contrato
	 * @return ArrayList<DtoParamPresupCap>
	 * @author diecorqu
	 */
	public ArrayList<DtoParamPresupCap> listarParametrizacionesPresupuestoCapxContrato(int codContrato);
	
	/**
	 * Este método se encarga de consultar los grupos de servicio y 
	 * clases de inventario que existen por nivel de atención para un 
	 * contrato seleccionado, retornando un arreglo de dto's  con 
	 * esta información
	 * 
	 * @param ArrayList<NivelAtencion> lista de niveles de un contrato determinado 
	 * @return ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> 
	 * @author diecorqu
	 */
	public ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> 
	obtenerNivelesAtencionPresupuestoParametrizacionGen
			(ArrayList<NivelAtencion> listaNivelesContrato);
	
	/**
	 * Este método se encarga de consultar los grupos de servicio y 
	 * clases de inventario que existen por nivel de atención para un 
	 * contrato seleccionado de una parametrizacion de presupuesto de 
	 * capitación existen, retornando un arreglo de dto's  con 
	 * esta información
	 * 
	 * @param ArrayList<NivelAtencion> lista de niveles de un contrato determinado 
	 * @param long código Parametrizacion
	 * @return ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> 
	 * @author diecorqu
	 */
	public ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> 
							obtenerNivelesAtencionPresupuestoParametrizacionExistentes
									(ArrayList<NivelAtencion> listaNivelesContrato, 
									 long codigoParametrizacion);
	
	/**
	 * Este método verifica si existe una parametrizacion de presupuesto 
	 * capitado para el contrato y la fecha de vigencia ingresados
	 * 
	 * @param codigo del contrato
	 * @param año de vigencia del contrato
	 * @return boolean con el resultado de la operación
	 * @author diecorqu
	 */
	public boolean existeParametrizacionPresupuesto(int codContrato, String anioVigencia);
	
	/**
	 * Este método guarda la parametrizacion de presupuesto capitado ingresada
	 * 
	 * @param Parametrizacion Presupuesto
	 * @return boolean con el resultado de la operación de guardado
	 * @author diecorqu
	 */
	public boolean guardarParametrizacionPresupuesto(
			ParamPresupuestosCap parametrizacionPresupuesto);
	
	/**
	 * Este método modifica la parametrizacion de presupuesto capitado
	 * 
	 * @param Parametrizacion Presupuesto
	 * @return boolean con el resultado de la operación de guardado
	 * @author diecorqu
	 */
	public ParamPresupuestosCap modificarParametrizacionPresupuesto(
			ParamPresupuestosCap parametrizacionPresupuesto);
	
	/**
	 * Este método elimina la parametrizacion de presupuesto capitado
	 * 
	 * @param código parametrización a eliminar
	 * @author diecorqu
	 */
	public void eliminarParametrizacionPresupuesto(ParamPresupuestosCap parametrizacion);
	
	/**
	 * Este método verifica si existe una parametrizacion de presupuesto 
	 * capitado para el convenio y la fecha de vigencia ingresados
	 * 
	 * @param codigo del convenio
	 * @param año de vigencia del contrato
	 * @return boolean con el resultado de la operación
	 * @author Ricardo Ruiz
	 */
	public boolean existeParametrizacionPresupuestoConvenio(int codConvenio, String anioVigencia);
	
	/**
	 * @author diecorqu
	 * 
	 * Verifica si el nivel pasado por parámetro esta asociado a una Parametrización
	 * del Presupuesto de Capitación
	 * @param codNivelAtencion
	 * @return boolean
	 */
	public boolean existeNivelAtencionPresupuestoCapitacion(long codNivelAtencion);
	
	/**
	 * Este método obtiene el valor de la parametrización de presupuesto
	 * capitado detallado para el convenio, contrato, servicio y la fecha de vigencia ingresados
	 * 	
	 * @param codContrato
	 * @param anioVigencia
	 * @param mes
	 * @param codigoGrupoServicio
	 * @param consecutivoNivelAtencion
	 * @return Double con el resultado de la operación
	 * @author Ricardo Ruiz
	 */
	public Double obtenerValorParametrizacionPresupuestoDetalladoServicios(int codContrato, String anioVigencia, 
				int mes, int codigoGrupoServicio, long consecutivoNivelAtencion);
	
	/**
	 * Este método obtiene el valor de la parametrización de presupuesto
	 * capitado General para el convenio, contrato, Servicio/Articulo y la fecha de vigencia ingresados
	 * 	
	 * @param codContrato
	 * @param anioVigencia
	 * @param mes
	 * @param codigoGrupoServicio
	 * @param consecutivoNivelAtencion
	 * @return Double con el resultado de la operación
	 * @author Ricardo Ruiz
	 */
	public Double obtenerValorParametrizacionPresupuestoGeneralServiciosArticulos(int codContrato, String anioVigencia, 
				int mes, long consecutivoNivelAtencion, String servicioArticulo);
	
	/**
	 * Este método obtiene el valor de la parametrización de presupuesto
	 * capitado detallado para el convenio, contrato, articulo y la fecha de vigencia ingresados
	 * 	
	 * @param codContrato
	 * @param anioVigencia
	 * @param mes
	 * @param codigoClaseInventario
	 * @param consecutivoNivelAtencion
	 * @return Double con el resultado de la operación
	 * @author Ricardo Ruiz
	 */
	public Double obtenerValorParametrizacionPresupuestoDetalladoArticulos(int codContrato, String anioVigencia, 
				int mes, int codigoClaseInventario, long consecutivoNivelAtencion);
	
}
