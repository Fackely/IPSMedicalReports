package com.servinte.axioma.servicio.interfaz.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.UsuariosEntidadSub;

/**
 * @author Cristhian Murillo
 */
public interface IEntregaMedicamentosInsumosEntSubcontratadasServicio 
{
	
	
	/**
	 * Retorna las EntidadesSubcontratadas activas para el centro de costo
	 * de la funcionalidad CentroCosto por Unidades Subcontratadas.
	 * 
	 * @param codCentroCosto
	 * @return ArrayList<EntidadesSubcontratadas>
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubXCentroCostoActivo (int codCentroCosto);
	
	
	/**
	 * Retorna las Autorizaciones de la EntidadesSubcontratada enviada.
	 * Filtrando por su estado
	 * @param DtoAutorizacionEntSubcontratadasCapitacion
	 * @return ArrayList<DtoEntregaMedicamentosInsumosEntSubcontratadas>
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> obtenerAutorizacionesPorEntSub(DtoAutorizacionEntSubcontratadasCapitacion dtoEntregaMedicamentosInsumosEntSubcontratadas);
	
	
	
	/**
	 * Busca registros por los parametros de busqueda.
	 * Este método puede ser utilizado para validar si el usuario tiene permisos sobre la entidad subcontratada.
	 * 
	 * @author Cristhian Murillo
	 * @param login
	 * @param entidadSub
	 * @return ArrayList<UsuariosEntidadSub>
	 */
	public ArrayList<UsuariosEntidadSub> buscarUsuariosEntidadSubPorUsuarioEntidad(String login, long entidadSub);
	
	
	
	/**
	 * Guarda la autorizacion seleccionada, la solicitud de pedido, el despacho del pedido 
	 * y el registro de la entrega de medicamentos e insumos.
	 * @param DtoAutorizacionEntSubcontratadasCapitacion
	 * @param usuario
 	 * @param codigoPersona
	 * String loginUsuario, int codigoPersona
	 * @return boolean
	 * 
	 * @author Cristhian Murillo
	 */
	public boolean guardarEntregaMedicamentoInsumoEntSub(DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacion, UsuarioBasico usuario, int codigoPersona);
	
	
	

	/**
	 * Guarda la autorizacion seleccionada, la solicitud de pedido, el despacho del pedido 
	 * y el registro de la entrega de medicamentos e insumos.
	 * @param dtoAutorizacionEntSubcontratadasCapitacion
	 * @param usuario
	 * @return boolean
	 */
	public boolean guardarRegistroEntregaMedicamentoInsumoEntSub(DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacion, UsuarioBasico usuario);
	
}
