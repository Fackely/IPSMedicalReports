package com.servinte.axioma.orm.delegate.manejoPaciente;

import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import util.ConstantesBD;

import com.princetonsa.dto.capitacion.DTOPacienteCapitado;
import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.IngresosEstancia;
import com.servinte.axioma.orm.IngresosEstanciaHome;
import com.servinte.axioma.orm.Pacientes;

/**
 * Esta clase se encarga de ejecutar los métodos de 
 * negocio de la entidad ingresos estancia
 * 
 * @author Angela Maria Aguirre
 * @since 14/01/2011
 */
public class IngresosEstanciaDelegate extends IngresosEstanciaHome 
{
	
	/**
	 * 
	 * Este método se encarga de obtener el registro
	 * de un ingreso estancia por su ID
	 * 
	 * @param int
	 * @return boolean 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public IngresosEstancia obtenerIngresoEstancia(long id){
		try{
			return super.findById(id);	
			
		} catch (Exception e) {
			Log4JManager.error("No se pudo obtener el ingreso estancia: ",e);
		}				
		return null;		
	} 
	
	
	/**
	 * 
	 * Este Método se encarga de actualizar el registro de un ingreso estancia
	 * 
	 * @param IngresosEstancia
	 * @return boolean
	 * @author, Angela Maria Aguirre
	 *
	 */
	public boolean actualizarIngresoEstancia(IngresosEstancia ingreso){
		boolean save = false;
		try{
			super.merge(ingreso);
			save = true;
		}catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo actualizar el registro de ingreso estancia: ",e);
		}		
		return save;
	}
	
	
	
	@Override
	public void attachDirty(IngresosEstancia instance) {
		super.attachDirty(instance);
	}
	
	
	@Override
	public IngresosEstancia findById(long id) {
		return super.findById(id);
	}
	
	

}
