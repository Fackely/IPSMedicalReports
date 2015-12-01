package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import util.adjuntos.DTOArchivoAdjunto;

import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IAdjuntoMovimientosCajaMundo;
import com.servinte.axioma.orm.AdjuntosMovimientosCaja;
import com.servinte.axioma.servicio.interfaz.tesoreria.IAdjuntoMovimientosCajaServicio;

public class AdjuntoMovimientosCajaServicio  implements IAdjuntoMovimientosCajaServicio {

	
	IAdjuntoMovimientosCajaMundo adjuntoMundo ;
	
	
	/**
	 * 
	 * Método constructor de la clase
	 * 
	 */
	public AdjuntoMovimientosCajaServicio(){
		adjuntoMundo=TesoreriaFabricaMundo.crearAdjuntoMovimientosCaja();
		
	}
	
	
	
	@Override
	public List<AdjuntosMovimientosCaja> listaAdjuntoMovimientosCaja(
			long codigoMovimiento) {
		
		return adjuntoMundo.listaAdjuntoMovimientosCaja(codigoMovimiento);
	}

	@Override
	public void insertar(AdjuntosMovimientosCaja objeto) {
		adjuntoMundo.insertar(objeto);
		
	}

	@Override
	public void modificar(AdjuntosMovimientosCaja objeto) {
		adjuntoMundo.modificar(objeto);
		
	}

	@Override
	public void eliminar(AdjuntosMovimientosCaja objeto) {
		adjuntoMundo.eliminar(objeto);
	}

	@Override
	public AdjuntosMovimientosCaja buscarxId(Number id) {
		
		return adjuntoMundo.buscarxId(id);
	}
	
	@Override
	public ArrayList<DTOArchivoAdjunto> consultarDocumentosSoporteAdjuntos(long idMovimiento){
		return adjuntoMundo.consultarDocumentosSoporteAdjuntos(idMovimiento);
	}
	
	@Override
	public Boolean guardarAdjuntosMovimientosCaja(AdjuntosMovimientosCaja dtoAdjuntos){
		
		return adjuntoMundo.guardarAdjuntosMovimientosCaja(dtoAdjuntos);
	}
	
	@Override
	public ArrayList<DTOArchivoAdjunto> confirmarAdjuntos (ArrayList<DTOArchivoAdjunto> dtoAdjuntos, long idMovimiento, String login){
		return adjuntoMundo.confirmarAdjuntos(dtoAdjuntos, idMovimiento, login);
	}
	
	@Override
	public boolean adjuntoConfirmado(){
		return adjuntoMundo.adjuntoConfirmado();
	}
}
