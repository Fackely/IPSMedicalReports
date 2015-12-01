/**
 * 
 */
package com.servinte.axioma.servicio.impl.facturacion;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IEntregaMedicamentosInsumosEntSubcontratadasMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAurorizacionesEntSubCapitacionMundo;
import com.servinte.axioma.orm.UsuariosEntidadSub;
import com.servinte.axioma.servicio.interfaz.facturacion.IEntregaMedicamentosInsumosEntSubcontratadasServicio;

/**
 * @author Cristhian Murillo
 *
 */
public class EntregaMedicamentosInsumosEntSubcontratadasServicio implements IEntregaMedicamentosInsumosEntSubcontratadasServicio
{
	IEntregaMedicamentosInsumosEntSubcontratadasMundo entregaMedicamentosInsumosEntSubcontratadasMundo;
	IAurorizacionesEntSubCapitacionMundo autorizacionesEntidadesSubMundo;

	
	/**
	 * Constructor
	 */
	public EntregaMedicamentosInsumosEntSubcontratadasServicio() 
	{
		entregaMedicamentosInsumosEntSubcontratadasMundo = FacturacionFabricaMundo.crearEntregaMedicamentosInsumosEntSubcontratadas();
		autorizacionesEntidadesSubMundo = ManejoPacienteFabricaMundo.crearAutorizacionesEntidadesSubMundo();
	}

	
	
	@Override
	public ArrayList<DtoEntidadSubcontratada> listarEntidadesSubXCentroCostoActivo(int codCentroCosto) {
		return entregaMedicamentosInsumosEntSubcontratadasMundo.listarEntidadesSubXCentroCostoActivo(codCentroCosto);
	}

	@Override
	public ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> obtenerAutorizacionesPorEntSub(
			DtoAutorizacionEntSubcontratadasCapitacion dtoEntregaMedicamentosInsumosEntSubcontratadas) {
		return autorizacionesEntidadesSubMundo.obtenerAutorizacionesPorEntSub(dtoEntregaMedicamentosInsumosEntSubcontratadas);
	}

	@Override
	public ArrayList<UsuariosEntidadSub> buscarUsuariosEntidadSubPorUsuarioEntidad(
			String login, long entidadSub) {
		return entregaMedicamentosInsumosEntSubcontratadasMundo.buscarUsuariosEntidadSubPorUsuarioEntidad(login, entidadSub);
	}


	@Override
	public boolean guardarEntregaMedicamentoInsumoEntSub(DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacion, UsuarioBasico usuario, int codigoPersona) 
	{
		return entregaMedicamentosInsumosEntSubcontratadasMundo.guardarEntregaMedicamentoInsumoEntSub(dtoAutorizacionEntSubcontratadasCapitacion, usuario, codigoPersona);
	}



	@Override
	public boolean guardarRegistroEntregaMedicamentoInsumoEntSub(DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacion,	UsuarioBasico usuario) {
		return entregaMedicamentosInsumosEntSubcontratadasMundo.guardarRegistroEntregaMedicamentoInsumoEntSub(dtoAutorizacionEntSubcontratadasCapitacion, usuario);
	}


}


