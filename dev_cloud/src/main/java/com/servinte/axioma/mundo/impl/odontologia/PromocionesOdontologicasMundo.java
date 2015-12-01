package com.servinte.axioma.mundo.impl.odontologia;

import java.util.ArrayList;

import util.ConstantesBD;

import com.princetonsa.dto.odontologia.DtoFiltroReportePromocionesOdontologicas;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaReportePromocionesOdontologicas;
import com.servinte.axioma.dao.fabrica.odontologia.PromocionesOdontologicasFabricaDAO;
import com.servinte.axioma.dao.interfaz.odontologia.IPromocionesOdontologicasDAO;
import com.servinte.axioma.mundo.fabrica.odontologia.PromocionesOdontologicasFabricaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.IPromocionesOdontologicasMundo;
import com.servinte.axioma.orm.PromocionesOdontologicas;

public class PromocionesOdontologicasMundo  implements IPromocionesOdontologicasMundo {
	
IPromocionesOdontologicasDAO dao;
	
	/**
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Javier Gonzalez
	 */
	public PromocionesOdontologicasMundo() {
		dao = PromocionesOdontologicasFabricaDAO.crearPromocionesOdontologicasDAO();
	}
	
	@Override
	public ArrayList<PromocionesOdontologicas> listarPromocionesOdontologicas() {
		return dao.listarPromocionesOdontologicas();
	}

	@Override
	public ArrayList<PromocionesOdontologicas> listarTodosPorCiudad(
			String codigoCiudad, String codigoPais, String codigoDto) {
		return dao.listarTodosPorCiudad(codigoCiudad,codigoPais,codigoDto);
	}

	public ArrayList<PromocionesOdontologicas> listarTodosPorCentroAtencionYCiudad(int centroAtencion, String codigoCiudad, String codigoPais, String codigoDto ){
		return dao.listarTodosPorCentroAtencionYCiudad(centroAtencion, codigoCiudad, codigoPais, codigoDto);
	}
	
	public ArrayList<PromocionesOdontologicas> listarTodosPorCentroAtencionYRegion(int centroAtencion, long codigoRegion){
		return dao.listarTodosPorCentroAtencionYRegion(centroAtencion, codigoRegion);
	}

	@Override
	public ArrayList<PromocionesOdontologicas> listarTodosPorRegion(
			long codigoRegion) {
		return dao.listarTodosPorRegion(codigoRegion);
	}
	
	public ArrayList<PromocionesOdontologicas> listarTodosPorCentroAtencion(int centroAtencion){
		return dao.listarTodosPorCentroAtencion(centroAtencion);
	}
	
	
	/**
	 * Este método se encarga de consultar las promociones odontológicas en el sistema
	 * @param filtroPromocion
	 * @return ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas>
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> consultarPromocionesOdontologicas(
			DtoFiltroReportePromocionesOdontologicas filtroPromocion){
		return dao.consultarPromocionesOdontologicas(filtroPromocion);
	}
	
	/**
	 * Este método se encarga de consultar las promociones odontológicas
	 * y ordenarlas para el formato pdf y excel
	 * 
	 * @return ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas>
	 * 
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> consolidarInfoReportePromociones(
			DtoFiltroReportePromocionesOdontologicas filtroPromociones) {
		
		IPromocionesOdontologicasMundo mundo = PromocionesOdontologicasFabricaMundo
				.crearPromocionesOdontologicasMundo();
		ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> resultadoPromociones = mundo
				.consultarPromocionesOdontologicas(filtroPromociones);
		
		ArrayList<Integer> codigosPromocion=new ArrayList<Integer>();
		ArrayList<Integer> codigosConvenios=null;
		ArrayList<Integer> consecutivosCentrosAtencion=null;
		StringBuilder tmpConvenios= null;
		StringBuilder tmpCentrosAtencion= null;
		
		//SE ORGANIZA LA INFORMACION DE LA CONSULTA SE AGREGA CONVENIOS Y CENTROS DE ATENCION
		//EN UNA SOLA FILA
		for(int i=0;i<resultadoPromociones.size();i++){
		
			if(!codigosPromocion.contains(resultadoPromociones.get(i).getCodigoPkPromocion())){
				
				tmpConvenios=new StringBuilder();
				tmpCentrosAtencion=new StringBuilder();
				consecutivosCentrosAtencion=new ArrayList<Integer>();
				codigosConvenios=new ArrayList<Integer>();
				
				for(int j=i;j<resultadoPromociones.size();j++){
					if(resultadoPromociones.get(i).getCodigoPkPromocion()==resultadoPromociones.get(j).getCodigoPkPromocion()){
						if(!codigosConvenios.contains(resultadoPromociones.get(j).getCodigoConvenio())&&resultadoPromociones.get(j).getNombreConvenio()!=null
								&&!resultadoPromociones.get(j).getNombreConvenio().equals("TODOS")){
							if(tmpConvenios.length()!=0)
								tmpConvenios.append("\n");
							tmpConvenios.append(" -"+resultadoPromociones.get(j).getNombreConvenio());
							codigosConvenios.add(resultadoPromociones.get(j).getCodigoConvenio());
						}
						if(!consecutivosCentrosAtencion.contains(resultadoPromociones.get(j).getConsecutivoCentroAtencion())&&resultadoPromociones.get(j).getCentroAtencion()!=null
								&&!resultadoPromociones.get(j).getCentroAtencion().equals("TODOS")){
							if(tmpCentrosAtencion.length()!=0)
								tmpCentrosAtencion.append("\n");
							tmpCentrosAtencion.append(" -"+resultadoPromociones.get(j).getCentroAtencion());
							consecutivosCentrosAtencion.add(resultadoPromociones.get(j).getConsecutivoCentroAtencion());
						}
					}
				}
				resultadoPromociones.get(i).setNombreConvenio(tmpConvenios.toString());
				resultadoPromociones.get(i).setCentroAtencion(tmpCentrosAtencion.toString());
				codigosPromocion.add(resultadoPromociones.get(i).getCodigoPkPromocion());
			}else{
				resultadoPromociones.get(i).setCodigoPkPromocion(ConstantesBD.codigoNuncaValido);
			}
		}
		
		
		//SE BORRAN LAS DEMAS FILAS  
		ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> listaDefinitiva = new ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas>();

		for (DtoResultadoConsultaReportePromocionesOdontologicas retorno : resultadoPromociones) {
			if (retorno.getCodigoPkPromocion()!=ConstantesBD.codigoNuncaValido) {
				listaDefinitiva.add(retorno);
			}
		}
		
		return listaDefinitiva;
		
	}
	
	
	/**
	 * Este método se encarga de consultar las promociones odontológicas
	 * y ordenarlas para reporte en archivo plano
	 * 
	 * @return ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas>
	 * 
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> consolidarInfoReportePromocionesPlano(
			DtoFiltroReportePromocionesOdontologicas filtroPromociones) {
		
		IPromocionesOdontologicasMundo mundo = PromocionesOdontologicasFabricaMundo
				.crearPromocionesOdontologicasMundo();
		ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> resultadoPromociones = mundo
				.consultarPromocionesOdontologicas(filtroPromociones);
		
		return resultadoPromociones;
		
	}
}
