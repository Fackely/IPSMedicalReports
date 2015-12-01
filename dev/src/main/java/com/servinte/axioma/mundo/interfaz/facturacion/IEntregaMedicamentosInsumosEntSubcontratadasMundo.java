package com.servinte.axioma.mundo.interfaz.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.UsuariosEntidadSub;



/**
 * Define la l&oacute;gica de negocio relacionada con los Terceros
 * @author Cristhian Murillo
 */
public interface IEntregaMedicamentosInsumosEntSubcontratadasMundo {
	
	/**
	 * Retorna las EntidadesSubcontratadas activas para el centro de costo
	 * de la funcionalidad CentroCosto por Unidades Subcontratadas.
	 * 
	 * @param codCentroCosto
	 * @return ArrayList<DtoEntidadSubcontratada>
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubXCentroCostoActivo (int codCentroCosto);
	
	
	
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
	 * @param dtoAutorizacionEntSubcontratadasCapitacion
	 * @param usuario
	 * @param codigoPersona
	 * @return boolean
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

