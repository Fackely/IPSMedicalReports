package com.servinte.axioma.servicio.impl.facturacion;

import java.util.List;

import com.princetonsa.dto.facturacion.DTOEstanciaViaIngCentroCosto;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IEstanciaViaIngCentroCostoMundo;
import com.servinte.axioma.orm.EstanciaViaIngCentroCosto;
import com.servinte.axioma.servicio.interfaz.facturacion.IEstanciaViaIngCentroCostoServicio;

public class EstanciaViaIngCentroCostoServicio implements IEstanciaViaIngCentroCostoServicio {

	IEstanciaViaIngCentroCostoMundo mundoEstancia;
	
	public EstanciaViaIngCentroCostoServicio(){
		mundoEstancia=FacturacionFabricaMundo.crearEstanciaViaIngCentroCostoMundo();
	}
	
	
	@Override
	public EstanciaViaIngCentroCosto buscarxId(Number id) {
		return mundoEstancia.buscarxId(id);
	}

	@Override
	public void eliminar(EstanciaViaIngCentroCosto objeto) {
		mundoEstancia.eliminar(objeto);
	}

	@Override
	public void insertar(EstanciaViaIngCentroCosto objeto) {
		mundoEstancia.insertar(objeto);
	}

	@Override
	public void insertarEstancia(DTOEstanciaViaIngCentroCosto dtoEstancia) {
		mundoEstancia.insertarEstancia(dtoEstancia);
	}

	@Override
	public List<DTOEstanciaViaIngCentroCosto> listarEstanciasxEntidadesSubContratadas(
			Long idEntidadSubContratada) {
		return mundoEstancia.listarEstanciasxEntidadesSubContratadas(idEntidadSubContratada);
	}

	@Override
	public void modificar(EstanciaViaIngCentroCosto objeto) {
		mundoEstancia.modificar(objeto);
		
	}

	@Override
	public void modificarDTOEstanciaViaIngCentroCosto(
			List<DTOEstanciaViaIngCentroCosto> listDTO) {
		mundoEstancia.modificarDTOEstanciaViaIngCentroCosto(listDTO);
	}


	@Override
	public boolean validarExistencia(int viaIngreso, int centroCosto,
			List<DTOEstanciaViaIngCentroCosto> listaEstancia) {
		return mundoEstancia.validarExistencia(viaIngreso, centroCosto, listaEstancia);
	}





	

}
