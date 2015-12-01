package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITransportadoraValoresMundo;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.TransportadoraValores;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITransportadoraValoresServicio;

/**
 * Implementaci&oacute;n de la interfaz {@link ITransportadoraValoresServicio}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public class TransportadoraValoresServicio  implements ITransportadoraValoresServicio{

	private ITransportadoraValoresMundo transportadoraValoresMundo;
	
	
	
	public TransportadoraValoresServicio() {
		transportadoraValoresMundo =  TesoreriaFabricaMundo.crearTransportadoraValoresMundo();
	}

	@Override
	public StringBuilder armarArchivo(TransportadoraValores dto, String titulo,
			ArrayList<CentroAtencion> listaCentros,
			ArrayList<String> listaCodigoCentro) {
		
		return transportadoraValoresMundo.armarArchivo(dto, titulo, listaCentros, listaCodigoCentro);
	}

	@Override
	public String[] cargarCodigoCentro(TransportadoraValores dto) {
		
		return transportadoraValoresMundo.cargarCodigoCentro(dto);
	}

	@Override
	public ArrayList<Integer> cargarCodigosPkCentroTransportadora(
			TransportadoraValores dto) {
		
		return transportadoraValoresMundo.cargarCodigosPkCentroTransportadora(dto);
	}

	@Override
	public List<TransportadoraValores> consultar(TransportadoraValores dtoTransportadora, int institucion) {
		
		return transportadoraValoresMundo.consultar(dtoTransportadora, institucion);
	}

	@Override
	public void eliminar(TransportadoraValores dtoTransportadora) {
		transportadoraValoresMundo.eliminar(dtoTransportadora);
	}

	@Override
	public void eliminiarCentroAtencionTransportadora(
			ArrayList<Integer> listaCodigoCentroAtencion) {
		transportadoraValoresMundo.eliminiarCentroAtencionTransportadora(listaCodigoCentroAtencion);
		
	}

	@Override
	public ArrayList<CentroAtencion> filtrarListaCentros(
			ArrayList<CentroAtencion> listaCentrosAtencion,
			ArrayList<String> listaCodigoCentros) {
		
		return transportadoraValoresMundo.filtrarListaCentros(listaCentrosAtencion, listaCodigoCentros);
	}

	@Override
	public void guardar(TransportadoraValores dtoTransportadora,
			UsuarioBasico usuario, ArrayList<String> listaCodigo,
			ArrayList<CentroAtencion> listaCentrosAtencion) {
		transportadoraValoresMundo.guardar(dtoTransportadora, usuario, listaCodigo, listaCentrosAtencion);
		
	}

	@Override
	public void guardarLog(StringBuilder estructuraArchivo,
			UsuarioBasico usuario, int tipoRegistro) {
		transportadoraValoresMundo.guardarLog(estructuraArchivo, usuario, tipoRegistro);
		
	}

	@Override
	public void modificar(TransportadoraValores dtoTransportadoraValores,
			UsuarioBasico usuario, ArrayList<Integer> listaCodigoEliminar,
			ArrayList<String> listaCodigoGuardar,
			ArrayList<CentroAtencion> listaCentroAtencion) {
		transportadoraValoresMundo.modificar(dtoTransportadoraValores, usuario, listaCodigoEliminar, listaCodigoGuardar, listaCentroAtencion);
	}

	@Override
	public List<TransportadoraValores> listarTodos(
			TransportadoraValores dtoTransportadora, int institucion,
			int consecutivoCentroAtencion) {
		
		return transportadoraValoresMundo.listarTodos(dtoTransportadora, institucion, consecutivoCentroAtencion);
	}

}
