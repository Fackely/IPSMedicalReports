package com.princetonsa.mundo.consultaExterna;

import java.nio.charset.CodingErrorAction;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.consultaExterna.ServiAdicionalesXProfAtenOdontoDao;
import com.princetonsa.dto.consultaExterna.DtoServiciosAdicionalesProfesionales;

public class ServiAdicionalesXProfAtenOdonto {

	private static Logger logger = Logger.getLogger(ServiAdicionalesXProfAtenOdonto.class);
	
	public static ServiAdicionalesXProfAtenOdontoDao getServiAdicionalesXProfAtenOdontoDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getServiAdicionalesXProfAtenOdontoDao();
	}

	/**
	 * Metodo utilizado para consultar los servicios adicioanales asociados a un profesional
	 * @param codProfesional
	 * @return
	 */
	public ArrayList<DtoServiciosAdicionalesProfesionales> consultarServiciosAdicionalesProfesionales(int codProfesional,int codEstandar) {
		
		return getServiAdicionalesXProfAtenOdontoDao().consultarServiciosAdicionalesProfesionales(codProfesional, codEstandar);
	}

	
	/**
	 * Metodo para realizar el proceso de Insercion de un Servicio Adicional a un Profesional
	 * @param con
	 * @param listServiciosSel
	 * @param codProfesional
	 * @param loginUsuario
	 * @param codInstitucion
	 * @return
	 */
	public boolean insertarServiciosAdicionales(Connection con,HashMap<String, Object> listServiciosSel, int codProfesional,String loginUsuario,int codInstitucion) {
		
		logger.info("Entro al mundo");
		return getServiAdicionalesXProfAtenOdontoDao().insertarServiciosAdicionales(con,listServiciosSel,codProfesional,loginUsuario,codInstitucion);
	}

	/**
	 * Metodo para realiazar el proceso de Eliminacion de un servicio adicional, asociado a un Profesional
	 * @param codigoServicio
	 * @return
	 */
	public boolean eliminarServicioExistente(int codigoServicio, int codMedico, int codInstitucion) {
		
		return getServiAdicionalesXProfAtenOdontoDao().eliminarServicioExistente(codigoServicio,codMedico,codInstitucion);
	}
	
	/**
	 * Metodo para obtener los codigos (separados por comas) de los servicios adicionales de un profasional 
	 * @param arrayDtoServicios
	 * @return
	 */
    public String obtenerCodigosServicios(ArrayList<DtoServiciosAdicionalesProfesionales> arrayDtoServicios)
    {	
    	String codigosInsertados="";
    	
    	for(int j=0;j<arrayDtoServicios.size();j++)
		 {
    		DtoServiciosAdicionalesProfesionales dto=new DtoServiciosAdicionalesProfesionales();
    		dto=arrayDtoServicios.get(j);
	        codigosInsertados+=dto.getCodigoServicio()+",";		
		 }
    	logger.info("codigosInsertados (MUNDO) >>"+codigosInsertados);
    	return codigosInsertados;
    }

    
    /**
     * Metodo para realizar la validacion de los datos requeridos para la modificacion o insercion de un servicio adicional 
     * @param codProfesionalSel
     * @param listServiciosSel
     * @return
     */
	public ActionErrors validarDatosNuevos(String codProfesionalSel,HashMap<String, Object> listServiciosSel, ArrayList<DtoServiciosAdicionalesProfesionales> listServiciosAdicionales) {
		
	ActionErrors errores = new ActionErrors();
	int numRegistros=Utilidades.convertirAEntero(listServiciosSel.get("numRegistros").toString());
	int numRegsSerAd=listServiciosAdicionales.size();
		
		if( codProfesionalSel.equals("") || codProfesionalSel.equals(ConstantesBD.codigoNuncaValido) )
		{
			errores.add("descripcion",new ActionMessage("errors.required", "El Profesional "));
		
		}else if(numRegistros<=0&&numRegsSerAd<=0)
		{
			errores.add("descripcion",new ActionMessage("errors.notEspecific","Se debe seleccionar la menos un servicio"));
		}
		
		return errores;
	}

	
	
}
