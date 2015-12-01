package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.List;

import util.adjuntos.DTOArchivoAdjunto;

import com.servinte.axioma.dao.interfaz.IBaseDAO;
import com.servinte.axioma.orm.AdjuntosMovimientosCaja;



public interface IAdjuntoMovimientosCajaDAO extends IBaseDAO<AdjuntosMovimientosCaja> {
	
	public List<AdjuntosMovimientosCaja> listaAdjuntoMovimientosCaja(long codigoMovimiento);
	
	/**
	 * Este m&eacute;todo se encarga de consultar los documentos de soporte
	 * del detalle de un movimiento de caja.
	 * @param idMovimiento identificador del movimiento de caja.
	 * @return DTOArchivoAdjunto
	 * 
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DTOArchivoAdjunto> consultarDocumentosSoporteAdjuntos(long idMovimiento);
	
	/**
	 * Este m&eacute;todo se encarga de almacenar el archivo adjunto que el usuario ha 
	 * confirmado.
	 * @param dtoAdjuntos
	 * @return Boolean retorna true si el adjunto ha sido guardado con &eacute;xito.
	 * 
	 * @author Yennifer Guerrero
	 */
	public Boolean guardarAdjuntosMovimientosCaja(AdjuntosMovimientosCaja adjunto);
}
