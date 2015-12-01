/**
 * 
 */
package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;
import java.util.List;

/**
 * @author jeilones
 * @created 17/06/2013
 *
 */
public class NotasEnfermeriaRecuperacionDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1128195121216841779L;

	private boolean existeNotasEnfermeria;
	private boolean existeNotasRecuperacion;
	
	private NotaEnfermeriaDto notaEnfermeriaCaptura;
	private NotaRecuperacionDto notaRecuperacionCaptura;
	
	private List<NotaEnfermeriaDto>listaNotasEnfermeria;
	private List<NotaRecuperacionDto>listaNotasRecuperacion;
	
	public boolean isExisteNotasEnfermeria() {
		return existeNotasEnfermeria;
	}
	public void setExisteNotasEnfermeria(boolean existeNotasEnfermeria) {
		this.existeNotasEnfermeria = existeNotasEnfermeria;
	}
	public boolean isExisteNotasRecuperacion() {
		return existeNotasRecuperacion;
	}
	public void setExisteNotasRecuperacion(boolean existeNotasRecuperacion) {
		this.existeNotasRecuperacion = existeNotasRecuperacion;
	}
	public NotaEnfermeriaDto getNotaEnfermeriaCaptura() {
		return notaEnfermeriaCaptura;
	}
	public void setNotaEnfermeriaCaptura(NotaEnfermeriaDto notaEnfermeriaCaptura) {
		this.notaEnfermeriaCaptura = notaEnfermeriaCaptura;
	}
	public NotaRecuperacionDto getNotaRecuperacionCaptura() {
		return notaRecuperacionCaptura;
	}
	public void setNotaRecuperacionCaptura(
			NotaRecuperacionDto notaRecuperacionCaptura) {
		this.notaRecuperacionCaptura = notaRecuperacionCaptura;
	}
	public List<NotaEnfermeriaDto> getListaNotasEnfermeria() {
		return listaNotasEnfermeria;
	}
	public void setListaNotasEnfermeria(List<NotaEnfermeriaDto> listaNotasEnfermeria) {
		this.listaNotasEnfermeria = listaNotasEnfermeria;
	}
	public List<NotaRecuperacionDto> getListaNotasRecuperacion() {
		return listaNotasRecuperacion;
	}
	public void setListaNotasRecuperacion(
			List<NotaRecuperacionDto> listaNotasRecuperacion) {
		this.listaNotasRecuperacion = listaNotasRecuperacion;
	}
}
