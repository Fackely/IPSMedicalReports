package com.servinte.axioma.mundo.impl.tesoreria;


import java.util.ArrayList;
import java.util.List;

import util.UtilidadFecha;
import util.adjuntos.DTOArchivoAdjunto;

import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IAdjuntoMovimientosCajaDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.IAdjuntoMovimientosCajaMundo;
import com.servinte.axioma.orm.AdjuntosMovimientosCaja;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.Usuarios;

public class AdjuntoMovimientosCajaMundo implements IAdjuntoMovimientosCajaMundo {
	
	
	private IAdjuntoMovimientosCajaDAO adjuntoDao;
	/**
	 * Almacena true si se pudo adjuntar el archivo, de lo contrario almacena false.
	 */
	public boolean confirmado = false;
	
	public AdjuntoMovimientosCajaMundo(){
		adjuntoDao= TesoreriaFabricaDAO.crearAdjuntosMovimientos();
		
	}
	
	

	@Override
	public List<AdjuntosMovimientosCaja> listaAdjuntoMovimientosCaja(
			long codigoMovimiento) {
		
		return adjuntoDao.listaAdjuntoMovimientosCaja(codigoMovimiento);
	}

	@Override
	public void insertar(AdjuntosMovimientosCaja objeto) {
		adjuntoDao.insertar(objeto);
		
	}

	@Override
	public void modificar(AdjuntosMovimientosCaja objeto) {
		adjuntoDao.modificar(objeto);
		
	}

	@Override
	public void eliminar(AdjuntosMovimientosCaja objeto) {
		adjuntoDao.eliminar(objeto);
		
	}

	@Override
	public AdjuntosMovimientosCaja buscarxId(Number id) {
		return adjuntoDao.buscarxId(id);
	}
	
	@Override
	public ArrayList<DTOArchivoAdjunto> consultarDocumentosSoporteAdjuntos(long idMovimiento){
		
		return adjuntoDao.consultarDocumentosSoporteAdjuntos(idMovimiento);
	}
	
	@Override
	public Boolean guardarAdjuntosMovimientosCaja(AdjuntosMovimientosCaja dtoAdjuntos){
		
		return guardarAdjuntosMovimientosCaja(dtoAdjuntos);
	}
	
	@Override
	public ArrayList<DTOArchivoAdjunto> confirmarAdjuntos (ArrayList<DTOArchivoAdjunto> dtoAdjuntos, long idMovimiento, String login){
		
		IAdjuntoMovimientosCajaDAO dao = TesoreriaFabricaDAO.crearAdjuntosMovimientos();
		DTOArchivoAdjunto adjuntoConfirmado = new DTOArchivoAdjunto(); 
		
		MovimientosCaja movimientosCaja = new MovimientosCaja();
		Usuarios usuario = new Usuarios();
		
		movimientosCaja.setCodigoPk(idMovimiento);
		usuario.setLogin(login);
		
		
		for(DTOArchivoAdjunto archivo:dtoAdjuntos) {
			
			if (archivo.isActivo()) {
				
				adjuntoConfirmado= archivo;
				
				AdjuntosMovimientosCaja adjunto = new AdjuntosMovimientosCaja();
				
				adjunto.setFecha(UtilidadFecha.getFechaActualTipoBD());
				adjunto.setHora(UtilidadFecha.getHoraActual());
				adjunto.setNombreGenerado(archivo.getNombreGenerado());
				adjunto.setNombreOriginal(archivo.getNombreOriginal());
				adjunto.setMovimientosCaja(movimientosCaja);
				adjunto.setUsuarios(usuario);
				
				dao.guardarAdjuntosMovimientosCaja(adjunto);
				
				confirmado = true;
				adjuntoConfirmado.setActivo(false);
			}
		}
		
		return dtoAdjuntos;
	}
	
	@Override
	public boolean adjuntoConfirmado(){
		return confirmado;
	}

}
