package com.servinte.axioma.mundo.impl.odontologia.presupuesto;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.odontologia.DtoIngresosOdontologicos;
import com.princetonsa.dto.odontologia.DtoPresupuestoContratado;
import com.princetonsa.dto.odontologia.DtoPresupuestosOdontologicosContratados;
import com.princetonsa.dto.odontologia.DtoPresupuestosOdontologicosContratadosConPromocion;
import com.princetonsa.dto.odontologia.DtoViewPresupuesTotalesConv;
import com.servinte.axioma.dao.fabrica.odontologia.presupuesto.PresupuestoFabricaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IPresupuestoContratadoDAO;
import com.servinte.axioma.mundo.fabrica.odontologia.presupuesto.PresupuestoFabricaMundo;
import com.servinte.axioma.mundo.interfaz.presupuesto.IPresupuestoContratadoMundo;
import com.servinte.axioma.mundo.interfaz.presupuesto.IViewPresupuestoTotalesConvMundo;
import com.servinte.axioma.orm.ViewPresupuestoTotalesConv;


/**
 * 
 * Esta clase se encarga de implementar la l&oacute;gica 
 * de negocios para el presupuesto contratado.
 *
 * @author Yennifer Guerrero
 * @since  08/09/2010
 *
 */
public class PresupuestoContratadoMundo implements IPresupuestoContratadoMundo {
	
	@Override
	public ArrayList<DtoIngresosOdontologicos>  consolidarInfoIngresosValIniConPresupuesto (ArrayList<DtoIngresosOdontologicos> listaIngresosConPresupuesto){
		
		if (listaIngresosConPresupuesto != null) {
			
			IViewPresupuestoTotalesConvMundo mundo = PresupuestoFabricaMundo.crearViewPresupuestoTotalesConvMundo();
			IPresupuestoContratadoDAO dao = PresupuestoFabricaDAO.crearPresupuestoContratado();
			Long codigoPresupuesto = null;
			
			DtoViewPresupuesTotalesConv dto = new DtoViewPresupuesTotalesConv();
			DtoPresupuestoContratado dtoPresupContratado = new DtoPresupuestoContratado();
			
			for (DtoIngresosOdontologicos registro : listaIngresosConPresupuesto) {
				
				codigoPresupuesto = registro.getCodigoPkPresupuesto();
				dto.setCodigoPresupuesto(codigoPresupuesto);
			
				List<ViewPresupuestoTotalesConv>  listViewPresupuesto= mundo.obtenerViewPresupuesto(dto);
				
				String valorPresupuestoTMP="";
				
				for(ViewPresupuestoTotalesConv viewPresupuesto: listViewPresupuesto )
				{
					if(viewPresupuesto.getId().getValorSubtotalContratado().doubleValue()>0)
					{
						//Se obtiene el valor del presupuesto contratado.
						double valorTemp = viewPresupuesto.getId().getValorSubtotalContratado().doubleValue();
						valorPresupuestoTMP = UtilidadTexto.formatearValores(valorTemp);
					}
				}
				
				dtoPresupContratado.setCodigoPresupuesto(dto.getCodigoPresupuesto());
				
				Long numeroContrato = null;
				List<DtoPresupuestoContratado> listaContratos= dao.obtenerContratosPresupuestoOdonto(dtoPresupContratado);
				
				for (DtoPresupuestoContratado dtoPresupuestoContratado : listaContratos) {
					//Se obtiene el n&uacute;mero del contrato del presupuesto.
					numeroContrato=dtoPresupuestoContratado.getConsecutivoContrato();
				}
				
				//Se asignan el n&uacute;mero del contrato y el valor del presupuesto.
				registro.setValorPresupuesto(valorPresupuestoTMP);
				registro.setNumeroContrato(String.valueOf(numeroContrato));
				
				
			}//fin for
			
			return listaIngresosConPresupuesto;
		}
		
		return null;
	}
	
	@Override
	public ArrayList<DtoIntegridadDominio> listarIndicativoContrato(){
		
		String[] listadoEstado = new String[]{
				ConstantesIntegridadDominio.acronimoContratadoContratado,
				ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente,
				ConstantesIntegridadDominio.acronimoContratadoTerminado,
				ConstantesIntegridadDominio.acronimoContratadoCancelado};
		
		Connection con=UtilidadBD.abrirConexion();
		
		ArrayList<DtoIntegridadDominio> listaIndicativoContrato = Utilidades.generarListadoConstantesIntegridadDominio(
				con, listadoEstado, false);
		UtilidadBD.closeConnection(con);
		
		for (DtoIntegridadDominio registro : listaIndicativoContrato) {
			
			String indicativo =  registro.getAcronimo();
			
			if (indicativo.trim().equals(ConstantesIntegridadDominio.acronimoContratadoContratado)) {
				registro.setDescripcion("Contratado");
			}
			
			if (indicativo.trim().equals(ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente)) {
				registro.setDescripcion("Suspendido Temporalmente");
			}
			
			if (indicativo.trim().equals(ConstantesIntegridadDominio.acronimoContratadoTerminado)) {
				registro.setDescripcion("Terminado");
			}
			
			if (indicativo.trim().equals(ConstantesIntegridadDominio.acronimoContratadoCancelado)) {
				registro.setDescripcion("Cancelado");
			}
		}
		
		
		
		return listaIndicativoContrato;
	}

	@Override
	public ArrayList<DtoPresupuestosOdontologicosContratados> consolidarConsultaPresupuestosContratados(ArrayList<DtoPresupuestosOdontologicosContratados> listaPresupuestosContratados) {
		
	if (listaPresupuestosContratados != null) {
				
				IViewPresupuestoTotalesConvMundo mundo = PresupuestoFabricaMundo.crearViewPresupuestoTotalesConvMundo();
				IPresupuestoContratadoDAO dao = PresupuestoFabricaDAO.crearPresupuestoContratado();
				Long codigoPresupuesto = null;
				
				DtoViewPresupuesTotalesConv dto = new DtoViewPresupuesTotalesConv();
				DtoPresupuestoContratado dtoPresupContratado = new DtoPresupuestoContratado();
				
				for (DtoPresupuestosOdontologicosContratados registro :  listaPresupuestosContratados) {
					
					codigoPresupuesto = registro.getCodigoPkPresupuesto();
					dto.setCodigoPresupuesto(codigoPresupuesto);
				
					List<ViewPresupuestoTotalesConv>  listViewPresupuesto= mundo.obtenerViewPresupuesto(dto);
					
					//String valorPresupuestoTMP="";
					double valor = 0;
					
					for(ViewPresupuestoTotalesConv viewPresupuesto: listViewPresupuesto )
					{
						if(viewPresupuesto.getId().getValorSubtotalContratado().doubleValue()>0)
						{
							//Se obtiene el valor del presupuesto contratado.
							double valorTemp = viewPresupuesto.getId().getValorSubtotalContratado().doubleValue();
							valor = valor + valorTemp;
							//valorPresupuestoTMP = UtilidadTexto.formatearValores(valorTemp);
						}
					}
					
					dtoPresupContratado.setCodigoPresupuesto(dto.getCodigoPresupuesto());
					
					Long numeroContrato = null;
					List<DtoPresupuestoContratado> listaContratos= dao.obtenerContratosPresupuestoOdonto(dtoPresupContratado);
					
					for (DtoPresupuestoContratado dtoPresupuestoContratado : listaContratos) {
						//Se obtiene el n&uacute;mero del contrato del presupuesto.
						numeroContrato=dtoPresupuestoContratado.getConsecutivoContrato();
					}
					
					//Se asignan el n&uacute;mero del contrato y el valor del presupuesto.
					registro.setValorPresupuesto(valor);
					registro.setNumeroContrato(numeroContrato);
					
					
				}//fin for
				
				return listaPresupuestosContratados;
			}
		
		return null;
		
		
		
	}
	
	public ArrayList<DtoPresupuestosOdontologicosContratadosConPromocion>  consolidarConsultaPresupuestosContratadosConPromocion (ArrayList<DtoPresupuestosOdontologicosContratadosConPromocion> listaPresupuestosContratados){
		
		return null;
	}
	
}
