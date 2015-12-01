package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.mundo.InstitucionBasica;
import com.servinte.axioma.dto.capitacion.DtoAutorizacionCapitacionOrdenAmbulatoria;

public interface IAutorizacionCapitacionOrdenesAmbulatoriasMundo {

	
	/**
	 * M�todo que se encarga de validar la descripci�n del servicio de acuerdo a la informaci�n del campo
	 * D�as Tramite Autorizaci�n Capitaci�n de la funcionalidad de Grupos de Servicios
	 * Cambio RQF-02 -0025 Autorizaciones Capitaci�n Subcontratada
	 * 
	 * @author Camilo G�mez
	 * 
	 * @param dtoAutorizAmbulatoria
	 * @param institucion
	 * @return dtoAutorizacionCapitacionOrdenAmbulatoria
	 */
	public DtoAutorizacionCapitacionOrdenAmbulatoria validarDescripcionServicio(DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizAmbulatoria,InstitucionBasica institucion); 
	
	
	/**
	 * M�todo que se encarga de validar si la orden est� asociada a una autorizaci�n de capitaci�n subcontratada.
	 * Cambio RQF-02 -0025 Autorizaciones Capitaci�n Subcontratada
	 * 
	 * @author Camilo G�mez
	 * @param dtoAutorizCapitaOrdenAmbu
	 * @return DtoAutorizacionCapitacionOrdenAmbulatoria
	 */
	public ArrayList<DtoAutorizacionCapitacionOrdenAmbulatoria> existeAutorizacionesOrdenAmbul(DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizCapitaOrdenAmbu);
	
	/**
	 * M�todo que se encarga de anular la autorizaci�n cuando la Orden Ambulatoria es anulada
	 * Cambio RQF-02 -0025 Autorizaciones Capitaci�n Subcontratada
	 * 
	 * @author Camilo G�mez
	 * @param dtoAutorizCapitaOrdenAmbu
	 * @return DtoAutorizacionCapitacionOrdenAmbulatoria
	 */
	public DtoAutorizacionCapitacionOrdenAmbulatoria anularAutorizacionOrdenAnulada(DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizCapitaOrdenAmbu);
	
	/**
	 * M�todo que se encarga de asociar la solicitud de Orden Ambulatoria a la Autorizaci�n de Capitaci�n 
	 * que ya existe de la Orden Ambulatoria
	 * Cambio RQF-02 -0025 Autorizaciones Capitaci�n Subcontratada
	 * 
	 * @author Camilo G�mez
	 * @param dtoAsociaSoliciAutorizOrdenAmbu
	 */
	public void asociarSolicitudAutorizacionOrdenAmbulatoria(DtoAutorizacionCapitacionOrdenAmbulatoria dtoAsociaSoliciAutorizOrdenAmbu);
	
	/**
	 * M�todo que se encarga de valdiar el centro de costo de la Autorizaci�n con el centro de costo de la cuenta del paciente
	 * Cambio RQF-02 -0025 Autorizaciones Capitaci�n Subcontratada
	 * 
	 * @author Camilo G�mez
	 * @param dtoAutorizCapitaOrdenAmbul
	 * @return dtoAutorizCapitaOrdenAmbul
	 */
	//public DtoAutorizacionCapitacionOrdenAmbulatoria validarCentroCostoAutorizacionOrdenAmbul(DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizCapitaOrdenAmbul);
}
