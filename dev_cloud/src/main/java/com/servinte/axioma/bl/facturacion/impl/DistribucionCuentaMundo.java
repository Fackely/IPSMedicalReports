package com.servinte.axioma.bl.facturacion.impl;

import java.util.List;

import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;

import com.servinte.axioma.bl.facturacion.interfaz.IDistribucionCuentaMundo;
import com.servinte.axioma.delegate.facturacion.DistribucionCuentaDelegate;
import com.servinte.axioma.dto.facturacion.BackupSubCuentaDto;
import com.servinte.axioma.dto.facturacion.InfoCreacionHistoricoCargosDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Clase que implementa los servicios de Negocio correspondiente a la lógica asociada a la
 * Distribucion de Cuenta
 * 
 * @author hermorhu
 * @created 24-Nov-2012 
 */
public class DistribucionCuentaMundo implements IDistribucionCuentaMundo {

	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.facturacion.interfaz.IDistribucionCuentaMundo#crearHistoricoCargosXLiquidacionDistribucion(com.servinte.axioma.dto.facturacion.InfoCreacionHistoricoCargosDto)
	 */
	@Override
	public boolean guardarHistoricoCargosXLiquidacionDistribucion(InfoCreacionHistoricoCargosDto infoCreacionHistoricoCargosDto) throws IPSException {
		DistribucionCuentaDelegate delegate = null;
		Long idLogDistribucionCuenta = null;
		List<BackupSubCuentaDto> backUpSubCuentas = null;
		boolean operacionExitosa = false;
		
		try{
			HibernateUtil.beginTransaction();
			delegate = new DistribucionCuentaDelegate();
			
			idLogDistribucionCuenta = delegate.guardarLogDistribucionCuenta(infoCreacionHistoricoCargosDto);
			
			if(idLogDistribucionCuenta != null){
			
				//En caso que la Distribucion sea Automatica
				if(infoCreacionHistoricoCargosDto.getClaseDistribucion().equals(ConstantesIntegridadDominio.acronimoAutomatica)){
					backUpSubCuentas = delegate.obtenerInfoBackUpSubCuentas(infoCreacionHistoricoCargosDto.getCodigoIngreso());
					
					for(BackupSubCuentaDto backupSubCuentaDto : backUpSubCuentas){
					
						operacionExitosa = delegate.generarInfoBackUpSubCuenta(backupSubCuentaDto, idLogDistribucionCuenta);
	
						if(operacionExitosa){
							operacionExitosa = delegate.generarInfoBackUpDetCargos(backupSubCuentaDto.getIdSubCuenta(), idLogDistribucionCuenta);
						}
						if(operacionExitosa){
							operacionExitosa = delegate.generarInfoBackUpDetCargosArtConsumo(backupSubCuentaDto.getIdSubCuenta(), idLogDistribucionCuenta);
						}
						if(operacionExitosa){
							operacionExitosa = delegate.generarInfoBackUpSolicitudesSubCuenta(backupSubCuentaDto.getIdSubCuenta(), idLogDistribucionCuenta);
						}
					} 
				}
				//En caso que la Distribucion sea Manual
				else if(infoCreacionHistoricoCargosDto.getClaseDistribucion().equals(ConstantesIntegridadDominio.acronimoManual)){
					if(infoCreacionHistoricoCargosDto.getNumeroSolicitudes() != null && !infoCreacionHistoricoCargosDto.getNumeroSolicitudes().isEmpty()){
						
						operacionExitosa = delegate.generarInfoBackUpDetCargosPorSolicitudes(infoCreacionHistoricoCargosDto.getNumeroSolicitudes(), idLogDistribucionCuenta);
						
						if(operacionExitosa){
							operacionExitosa = delegate.generarInfoBackUpDetCargosArtConsumoPorSolicitudes(infoCreacionHistoricoCargosDto.getNumeroSolicitudes(), idLogDistribucionCuenta);
						}
						if(operacionExitosa){
							operacionExitosa = delegate.generarInfoBackUpSolicitudesSubCuentaPorSolicitudes(infoCreacionHistoricoCargosDto.getNumeroSolicitudes(), idLogDistribucionCuenta);
						}
					}
					
				}
			}	
			
			HibernateUtil.endTransaction();
		} catch (IPSException ipsme) {
			operacionExitosa = false;
			HibernateUtil.abortTransaction();
			throw ipsme;
		} catch (Exception e) {
			operacionExitosa = false;
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		
		return operacionExitosa;
	}

}
