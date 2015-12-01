package com.servinte.axioma.mundo.impl.facturacion;

import java.util.ArrayList;
import java.util.List;
import org.axioma.util.log.Log4JManager;


import com.princetonsa.dto.facturacion.DTOEstanciaViaIngCentroCosto;
import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IEstanciaViaIngCentroCostoDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.IEstanciaViaIngCentroCostoMundo;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.orm.EntidadesSubcontratadas;
import com.servinte.axioma.orm.EstanciaViaIngCentroCosto;
import com.servinte.axioma.orm.ViasIngreso;

/**
 * 
 * @author axioma
 *
 */
public class EstanciaViaIngCentroCostoMundo implements IEstanciaViaIngCentroCostoMundo {
	
	
	IEstanciaViaIngCentroCostoDAO DAOEstancia;
	
	
	public EstanciaViaIngCentroCostoMundo(){
		
		DAOEstancia= FacturacionFabricaDAO.crearEstanciaViaIngresoDAO();
	}
	
	

	
	
	

	

	/**
	 * METODO QUE RECIBE UN OBJETO  Y LO INSERTA EN LA BASE DE DATOS
	 * @author 
	 * @param objecto
	 */
	public void insertar(EstanciaViaIngCentroCosto objeto){
		DAOEstancia.insertar(objeto);
		
	}

	
	/**
	 * METODO QUE RECIBE UN OBJETO Y LOS MODIFICA EN LA BASE DE DATOS 
	 * @author 
	 * @param objecto
	 */
	public void modificar(EstanciaViaIngCentroCosto objeto){
		DAOEstancia.modificar(objeto);
		
	}
	/**
	 * METODO QUE RECIBE UN OBJETO Y LOS ELIMINA DE LA BASE DE DATOS
	 * @author 
	 * @param objecto
	 */
	public void eliminar( EstanciaViaIngCentroCosto objeto){
		DAOEstancia.eliminar(objeto);
		
	}

	
	/**
	 * METOD QUE RECIBE UN ID Y RETORNA UN TIPO DE OBJETO DE LA BASE DE DATOS
	 * @author 
	 * @param objeto
	 * @param id
	 * @return
	 */
	public EstanciaViaIngCentroCosto buscarxId(Number id){
		
		return  DAOEstancia.buscarxId(id);
		
	}









	@Override
	public void insertarEstancia(DTOEstanciaViaIngCentroCosto dtoEstancia) {
		/*
		 * Convertir
		 */
		EstanciaViaIngCentroCosto estancia= new EstanciaViaIngCentroCosto();
		CentrosCosto centroCosto= new CentrosCosto();
		centroCosto.setCodigo(dtoEstancia.getCentroCosto());
		ViasIngreso viaIngreso= new ViasIngreso();
		viaIngreso.setCodigo(dtoEstancia.getViaIngreso());
		EntidadesSubcontratadas entidades= new EntidadesSubcontratadas();
		entidades.setCodigoPk(dtoEstancia.getEntidadSubcontratada());
		estancia.setCentrosCosto(centroCosto);
		estancia.setEntidadesSubcontratadas(entidades);
		estancia.setViasIngreso(viaIngreso);
		this.insertar(estancia);
		
	}




	@Override
	public List<DTOEstanciaViaIngCentroCosto> listarEstanciasxEntidadesSubContratadas(
			Long idEntidadSubContratada) {
		
		
		List<EstanciaViaIngCentroCosto> listaEntidades= DAOEstancia.listarEstanciasxEntidadesSubContratadas(idEntidadSubContratada);
		List<DTOEstanciaViaIngCentroCosto> listaDTO= new ArrayList<DTOEstanciaViaIngCentroCosto>();
		
		try {
			
		
			for ( EstanciaViaIngCentroCosto entidad: listaEntidades){
			
				listaDTO.add(convertirEntidadDTO(entidad));
				
			}
		}
		catch (Exception e) {
			Log4JManager.info(e.getMessage());
			Log4JManager.error(e.getMessage());
			
		}
		

		return listaDTO;
	}
	
	/**
	 * 
	 * @param entidadEstacias
	 * @return
	 */
	public DTOEstanciaViaIngCentroCosto convertirEntidadDTO(EstanciaViaIngCentroCosto entidadEstacias){
		
		DTOEstanciaViaIngCentroCosto estancias= new DTOEstanciaViaIngCentroCosto();
		estancias.setCodigoPk(entidadEstacias.getCodigoPk());
		estancias.setCentroCosto(entidadEstacias.getCentrosCosto().getCodigo());
		estancias.setNombreCentroCosto(entidadEstacias.getCentrosCosto().getNombre());
		estancias.setViaIngreso(entidadEstacias.getViasIngreso().getCodigo());
		estancias.setNombreViaIngreso(entidadEstacias.getViasIngreso().getNombre());
		estancias.setEntidadSubcontratada(entidadEstacias.getEntidadesSubcontratadas().getCodigoPk());
		
		return estancias;
		
	}
	
	
	/**
	 * 
	 * @param estancia
	 * @return
	 */
	public EstanciaViaIngCentroCosto convertirDTOaEntidad(DTOEstanciaViaIngCentroCosto estancia ){
		
		EstanciaViaIngCentroCosto entidad= new EstanciaViaIngCentroCosto();
		entidad.setCodigoPk(estancia.getCodigoPk());
		
		CentrosCosto centro = new CentrosCosto();
		centro.setCodigo(estancia.getCentroCosto());
		entidad.setCentrosCosto(centro);
		
		ViasIngreso via= new ViasIngreso();
		via.setCodigo(estancia.getViaIngreso());
		
		entidad.setViasIngreso(via);
		
		EntidadesSubcontratadas entidadSubConEnti= new EntidadesSubcontratadas();
		entidadSubConEnti.setCodigoPk(estancia.getEntidadSubcontratada());
		
		entidad.setEntidadesSubcontratadas(entidadSubConEnti);
		
		return entidad;
		
	}
	
	
	/**
	 * 
	 * @param listDTO
	 */
	public void modificarDTOEstanciaViaIngCentroCosto(List<DTOEstanciaViaIngCentroCosto> listDTO) {
		
		/*
		 * 
		 */
		for(DTOEstanciaViaIngCentroCosto dto: listDTO ){
			
			if( dto.isActivo()){
				
				this.modificar(convertirDTOaEntidad(dto));
			}
			else{
				this.eliminar(convertirDTOaEntidad(dto));
				
			}			
			
		}
		
		
	}
	
	
	public boolean validarExistencia(int viaIngreso, int centroCosto, List<DTOEstanciaViaIngCentroCosto> listaEstancia){

			 boolean retorno=false;

			 for( DTOEstanciaViaIngCentroCosto dto :listaEstancia ){
				 if( (dto.getViaIngreso())==viaIngreso){

				 	retorno=true;
				 	break;
				 }
			 }

			 	return retorno;

	}
		
		
	
	
	
	
	
	
	
	
	
	

}
