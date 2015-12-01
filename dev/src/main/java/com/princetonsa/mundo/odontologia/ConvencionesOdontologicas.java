package com.princetonsa.mundo.odontologia;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.Imagen;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.odontologia.ConvencionesOdontologicasDao;
import com.princetonsa.dto.odontologia.DtoConvencionesOdontologicas;
import com.princetonsa.enu.general.CarpetasArchivos;

public class ConvencionesOdontologicas {

	private static Logger logger = Logger.getLogger(ConvencionesOdontologicas.class);
	
	public static ConvencionesOdontologicasDao getConvencionesOdontologicasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConvencionesOdontologicasDao();
	}

	/**
	 * Metodo para consultar las convenciones Odongologicas Asociadas a una Institucion
	 * @param codInstitucion
	 * @return
	 */
	public ArrayList<DtoConvencionesOdontologicas> consultarConvencionesOdontologicas(int codInstitucion) {
		
		return getConvencionesOdontologicasDao().consultarConvencionesOdontologicas(codInstitucion);
	}

	/**
	 * 
	 * @param con
	 * @param nuevaConvencion
	 * @param loginUsuario
	 * @param codigoInstitucionInt
	 * @return
	 */
	public int crearNuevaConvencionOdontologica(Connection con,	DtoConvencionesOdontologicas nuevaConvencion, String loginUsuario,int codInstitucion) {
		
		return getConvencionesOdontologicasDao().crearNuevaConvencionOdontologica(con,nuevaConvencion,loginUsuario,codInstitucion);
	}
	
	/**
	 * Metodo para realizar el proceso de eliminacion de una convencion
	 * @param codigoConvencion
	 * @return
	 */
	public boolean eliminarConvencion(int codigoConvencion) {
		
	       return getConvencionesOdontologicasDao().eliminarConvencion(codigoConvencion);
	}

	/**
	 * 
	 * @param nuevaConvencion
	 * @param consecutivoConvencion
	 * @return
	 */
	public boolean modificarConvencionOdontologica(Connection con,DtoConvencionesOdontologicas nuevaConvencion,int consecutivoConvencion, String loginUsuario) {
		
		return getConvencionesOdontologicasDao().modificarConvencionOdontologica(con,nuevaConvencion,consecutivoConvencion,loginUsuario);
	}
	
	
	/**
	 * Método para realizar Validaciones de Campos requeridos en el formulario 
	 * @param nuevaConvencion
	 * @param convencionesOdontologicas
	 * @return
	 */
	public ActionErrors validacionNuevosDatos(DtoConvencionesOdontologicas nuevaConvencion,ArrayList<DtoConvencionesOdontologicas> convencionesOdontologicas) 
	{
		
		ActionErrors errores = new ActionErrors();
		
		
		
		if(nuevaConvencion.getNombre().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required", "El Nombre "));
		}
		if(nuevaConvencion.getTipo().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required", "El Tipo "));
		}
		
		if(nuevaConvencion.getActivo().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required", "Activo "));
		}
		
		return errores;
	}
	
	/**
	 * Método que se encarga de Validar si se realizaron cambios a la Convención
	 * @param nuevaConvencion
	 * @param dtoConvenciones
	 * @return
	 */
	public boolean validarExistenciaCambios(DtoConvencionesOdontologicas nuevaConvencion,DtoConvencionesOdontologicas dtoConvenciones) {
		
		boolean existioModificacion= true;
		
		logger.info("Validacion CAMBIOS EXISTENTES");
		if(nuevaConvencion.getCodigo().equals(dtoConvenciones.getCodigo()))
		 {
			if(nuevaConvencion.getNombre().equals(dtoConvenciones.getNombre()))
					{
						if(nuevaConvencion.getTipo().equals(dtoConvenciones.getTipo()))
						{
							if(nuevaConvencion.getColor().equals(dtoConvenciones.getColor()))
						    	{
								if(nuevaConvencion.getBorde().equals(dtoConvenciones.getBorde()))
							    	{
									if(nuevaConvencion.getTrama()==dtoConvenciones.getTrama())
									{
										if(nuevaConvencion.getImagen()==dtoConvenciones.getImagen())
										{
											if(nuevaConvencion.getActivo().equals(dtoConvenciones.getActivo()))											
											{											
												existioModificacion=false;												  
											}	 
										}	 
									}	
							    }	
						 }
					}
		 }
		 }
		logger.info("valor >> "+existioModificacion);
		return existioModificacion;
	}
	
	/**
	 * Genera la imagen
	 * @param DtoConvencionesOdontologicas dto
	 * @param String codigoPk
	 * */
	public String generarImagen(DtoConvencionesOdontologicas dto,String codigoPk)
	{
		String nombreArchivo = "";
		
		try
		{
			nombreArchivo = "convencion_"+codigoPk;			
			File archivo = new File(CarpetasArchivos.CONVENCION.getRutaFisica(),nombreArchivo+".jpg");
			
			logger.info("Nombre archivo:"+nombreArchivo+" ruta: "+CarpetasArchivos.CONVENCION.getRutaFisica()+" "+archivo.getName());
			
	        if(!Imagen.exportar(
	                dto.getImagenBits(),
	                150,
	                150,
	                archivo))
	            logger.info("Error exportando la imagen");
		}
		catch(Exception ioe)
		{
		   logger.info("Error exportando la imagen: "+ioe.getMessage());
		}
		
		return (nombreArchivo+".jpg");
	}
}	
