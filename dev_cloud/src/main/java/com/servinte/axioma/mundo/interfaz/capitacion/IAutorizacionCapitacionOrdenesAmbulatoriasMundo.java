package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.mundo.InstitucionBasica;
import com.servinte.axioma.dto.capitacion.DtoAutorizacionCapitacionOrdenAmbulatoria;

public interface IAutorizacionCapitacionOrdenesAmbulatoriasMundo {

	
	/**
	 * Método que se encarga de validar la descripción del servicio de acuerdo a la información del campo
	 * Días Tramite Autorización Capitación de la funcionalidad de Grupos de Servicios
	 * Cambio RQF-02 -0025 Autorizaciones Capitación Subcontratada
	 * 
	 * @author Camilo Gómez
	 * 
	 * @param dtoAutorizAmbulatoria
	 * @param institucion
	 * @return dtoAutorizacionCapitacionOrdenAmbulatoria
	 */
	public DtoAutorizacionCapitacionOrdenAmbulatoria validarDescripcionServicio(DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizAmbulatoria,InstitucionBasica institucion); 
	
	
	/**
	 * Método que se encarga de validar si la orden está asociada a una autorización de capitación subcontratada.
	 * Cambio RQF-02 -0025 Autorizaciones Capitación Subcontratada
	 * 
	 * @author Camilo Gómez
	 * @param dtoAutorizCapitaOrdenAmbu
	 * @return DtoAutorizacionCapitacionOrdenAmbulatoria
	 */
	public ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria> existeAutorizacionesOrdenAmbul(DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizCapitaOrdenAmbu);
	
	/**
	 * Método que se encarga de anular la autorización cuando la Orden Ambulatoria es anulada
	 * Cambio RQF-02 -0025 Autorizaciones Capitación Subcontratada
	 * 
	 * @author Camilo Gómez
	 * @param dtoAutorizCapitaOrdenAmbu
	 * @return DtoAutorizacionCapitacionOrdenAmbulatoria
	 */
	public DtoAutorizacionCapitacionOrdenAmbulatoria anularAutorizacionOrdenAnulada(DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizCapitaOrdenAmbu);
	
	/**
	 * Método que se encarga de asociar la solicitud de Orden Ambulatoria a la Autorización de Capitación 
	 * que ya existe de la Orden Ambulatoria
	 * Cambio RQF-02 -0025 Autorizaciones Capitación Subcontratada
	 * 
	 * @author Camilo Gómez
	 * @param dtoAsociaSoliciAutorizOrdenAmbu
	 */
	public void asociarSolicitudAutorizacionOrdenAmbulatoria(DtoAutorizacionCapitacionOrdenAmbulatoria dtoAsociaSoliciAutorizOrdenAmbu);
	
	/**
	 * Método que se encarga de valdiar el centro de costo de la Autorización con el centro de costo de la cuenta del paciente
	 * Cambio RQF-02 -0025 Autorizaciones Capitación Subcontratada
	 * 
	 * @author Camilo Gómez
	 * @param dtoAutorizCapitaOrdenAmbul
	 * @return dtoAutorizCapitaOrdenAmbul
	 */
	//public DtoAutorizacionCapitacionOrdenAmbulatoria validarCentroCostoAutorizacionOrdenAmbul(DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizCapitaOrdenAmbul);
}
