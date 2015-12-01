package com.servinte.axioma.servicio.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.List;

import util.adjuntos.DTOArchivoAdjunto;

import com.servinte.axioma.orm.AdjuntosMovimientosCaja;

public interface IAdjuntoMovimientosCajaServicio {

	
	
	/**
	 * 
	 * Este método se encarga de
	 * 
	 *
	 */
	public List<AdjuntosMovimientosCaja> listaAdjuntoMovimientosCaja(long codigoMovimiento);
	
	
	/**
	 * METODO QUE RECIBE UN OBJETO  Y LO INSERTA EN LA BASE DE DATOS
	 * @author 
	 * @param objecto
	 */
	public void insertar(AdjuntosMovimientosCaja objeto);

	
	/**
	 * METODO QUE RECIBE UN OBJETO Y LOS MODIFICA EN LA BASE DE DATOS 
	 * @author 
	 * @param objecto
	 */
	public void modificar(AdjuntosMovimientosCaja objeto);
	
	
	/**
	 * METODO QUE RECIBE UN OBJETO Y LOS ELIMINA DE LA BASE DE DATOS
	 * @author 
	 * @param objecto
	 */
	public void eliminar( AdjuntosMovimientosCaja objeto);

	
	/**
	 * METOD QUE RECIBE UN ID Y RETORNA UN TIPO DE OBJETO DE LA BASE DE DATOS
	 * @author 
	 * @param objeto
	 * @param id
	 * @return
	 */
	public AdjuntosMovimientosCaja buscarxId(Number id);
	
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
	public Boolean guardarAdjuntosMovimientosCaja(AdjuntosMovimientosCaja dtoAdjuntos);
	
	/**
	 * Este m&eacute;todo se encarga de obtener un listado de los documentos que el usuario
	 * ha confirmado desea almacenar en la base de datos.
	 * @param dtoAdjuntos listado de archivos a almacenar en el sistema
	 * @return ArrayList<DTOArchivoAdjunto>
	 * 
	 * @author Yennifer Guerrero
	 */
	public ArrayList<DTOArchivoAdjunto> confirmarAdjuntos (ArrayList<DTOArchivoAdjunto> dtoAdjuntos, long idMovimiento, String login);
	
	/**
	 * Este mètodo se encarga de indicar si el archivo fue
	 * adjuntado con éxito.
	 * 
	 * @return confirmado
	 *
	 * @autor Yennifer Guerrero
	 */
	public boolean adjuntoConfirmado();
	
}
