
package com.servinte.axioma.orm.delegate.facturacion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.HistoricoEncabezado;
import com.servinte.axioma.orm.HistoricoEncabezadoHome;

/**
 * 
 * Esta clase se encarga de manejar las transaccciones relacionadas
 * con el objeto {@link HistoricoEncabezado}.
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public class HistoricoEncabezadoDelegate extends HistoricoEncabezadoHome{

	/**
	 * Método que se encarga de realizar
	 * la consulta del objeto {@link HistoricoEncabezado}
	 * 
	 * @param codigoHistoricoEncabezado
	 * @return
	 */
	public HistoricoEncabezado findById (long codigoHistoricoEncabezado) {
		
		HistoricoEncabezado historicoEncabezado = null;
		
		try{
			historicoEncabezado = super.findById(codigoHistoricoEncabezado);
		}
		catch (Exception e) {
			Log4JManager.error("No se pudo obtener el registro del Historico Encabezado: ",e);
		}
		
		return historicoEncabezado;
	}
	
	/**
	 * Método que se encarga de realizar
	 * el registro del objeto {@link HistoricoEncabezado}
	 * 
	 * @param historicoEncabezado
	 * @return
	 */
	public boolean insertar (HistoricoEncabezado historicoEncabezado){
		
		boolean resultado = false;
		
		try{
			super.persist(historicoEncabezado);
			resultado = true;
		}
		catch (Exception e) {
			resultado = false;
			Log4JManager.error("No se pudo guardar el registro del Historico Encabezado: ",e);
		}
		
		return resultado;
	}
	
	
	/**
	 * Método que se encarga de eliminar
	 * el registro del objeto {@link HistoricoEncabezado}
	 * 
	 * @param historicoEncabezado
	 * @return
	 */
	public boolean eliminar (HistoricoEncabezado historicoEncabezado){
		
		boolean resultado = false;
		
		try{
			super.delete(historicoEncabezado);
			resultado = true;
		}
		catch (Exception e) {
			resultado = false;
			Log4JManager.error("No se pudo eliminar el registro del Historico Encabezado: ",e);
		}
		
		return resultado;
	}
}