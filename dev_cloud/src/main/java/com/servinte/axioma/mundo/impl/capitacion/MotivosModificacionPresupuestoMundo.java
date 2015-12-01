package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ILogParametrizacionPresupuestoCapDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IMotivosModificacionPresupuestoDAO;
import com.servinte.axioma.dto.capitacion.DtoMotivosModifiPresupuesto;
import com.servinte.axioma.mundo.interfaz.capitacion.ILogParametrizacionPresupuestoCapMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IMotivosModificacionPresupuestoMundo;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.MotivosModifiPresupuesto;
import com.servinte.axioma.orm.Usuarios;

public class MotivosModificacionPresupuestoMundo implements IMotivosModificacionPresupuestoMundo{


	/**
	 * Dao con servicios a base de datos
	 */
	private  IMotivosModificacionPresupuestoDAO motivosModificacionPresupuestoDAO ; 
	private  ILogParametrizacionPresupuestoCapDAO logParametrizacionPresupuestoCapDAO;


	/**
	 * Constructor de clase
	 */
	public MotivosModificacionPresupuestoMundo() {
		motivosModificacionPresupuestoDAO = CapitacionFabricaDAO.crearMotivosModificacionPresupuesto();
		logParametrizacionPresupuestoCapDAO = CapitacionFabricaDAO.crearLogParametrizacionPresupuestoCapDAO();
	}


	/**
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IMotivosModificacionPresupuestoMundo#consultarTodosMotivosModificacion()
	 */
	public ArrayList<DtoMotivosModifiPresupuesto> consultarTodosMotivosModificacion(){
		//SE CONSULTAN LOS MOTIVOS DE MODIFICACION
		ArrayList<DtoMotivosModifiPresupuesto> resultado = 	 motivosModificacionPresupuestoDAO.consultarTodosMotivosModificacion();
		
		//LISTA TEMPORAL 
		ArrayList<DtoMotivosModifiPresupuesto> resultadoTotal = new ArrayList<DtoMotivosModifiPresupuesto>();
		
		//SE CAMBIAN LOS VALORES S - N POR VALORES BOOLEANOS 
		for (DtoMotivosModifiPresupuesto motivosModifiPresupuesto : resultado) {
			if (motivosModifiPresupuesto.getActivo().equals(ConstantesBD.estadoActivoChecbox)) {
				motivosModifiPresupuesto.setEstado(true);
			}else{
				motivosModifiPresupuesto.setEstado(false);
			}
			motivosModifiPresupuesto.setPermiteEliminar(puedeEliminar(motivosModifiPresupuesto));
			resultadoTotal.add(motivosModifiPresupuesto);
		}

		return resultadoTotal;


	}

	/**
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IMotivosModificacionPresupuestoMundo#guardarModificarMotivosModificacion(com.princetonsa.mundo.UsuarioBasico, java.util.ArrayList)
	 */
	public void guardarModificarMotivosModificacion(UsuarioBasico usuario,ArrayList<DtoMotivosModifiPresupuesto> lista)
	{
		for (DtoMotivosModifiPresupuesto dtoMotivosModifiPresupuesto : lista)
		{
				
			MotivosModifiPresupuesto motivoGuardar; motivoGuardar = new MotivosModifiPresupuesto();

			//SE CAMBIAN VALORES BOOLEAN POR CADENAS S-N PARA PERSISTIR EN BASE DEDATOS.
			if (dtoMotivosModifiPresupuesto.getEstado()==true) {
				motivoGuardar.setActivo(ConstantesBD.estadoActivoChecbox);
			}else{
				motivoGuardar.setActivo(ConstantesBD.estadoInActivoChecbox);
			}

			// SE OBTIENE EL ID DEL USUARIO PARA LA FK
			Usuarios usuarioFk = new Usuarios();
			usuarioFk.setLogin(usuario.getLoginUsuario());
			motivoGuardar.setUsuarios(usuarioFk);


			//SE OBTIENE LA INSTITUCION PARA LA FK 
			Instituciones institucion = new Instituciones();
			institucion.setCodigo(usuario.getCodigoInstitucionInt());
			motivoGuardar.setInstituciones(institucion);


			// SE OBTIENE LA FECHA ACTUAL PARA INSERTAR 
			motivoGuardar.setFechaModifica(UtilidadFecha.getFechaActualTipoBD());

			//SE OBTIENE LA HORA DE MODIFICACIONES
			motivoGuardar.setHoraModifica(UtilidadFecha.getHoraActual());
			

			motivoGuardar.setCodigo(dtoMotivosModifiPresupuesto.getCodigo());
			motivoGuardar.setDescripcion(dtoMotivosModifiPresupuesto.getDescripcion());
			
			if (dtoMotivosModifiPresupuesto.getCodigoPk()>0) {
				motivoGuardar.setCodigoPk(dtoMotivosModifiPresupuesto.getCodigoPk());
			}
			
			//SE PERISISTE LA INFORMACION
			motivosModificacionPresupuestoDAO.insertar(motivoGuardar);
		}
	}


	/**
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IMotivosModificacionPresupuestoMundo#eliminarMotivosModificacion(java.util.ArrayList)
	 */
	public void eliminarMotivosModificacion(ArrayList<Long> listaAEliminar){
		//SE BUSCA EL OBJETO POR ID Y LUEGO SE ELIMINA
		for (Long idMotivoModificacion : listaAEliminar) {
			motivosModificacionPresupuestoDAO.eliminar(motivosModificacionPresupuestoDAO.buscarxId(idMotivoModificacion));
		}
	}


	/**
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IMotivosModificacionPresupuestoMundo#consultaFiltro(java.lang.String, java.lang.String, java.lang.Boolean)
	 */
	public ArrayList<DtoMotivosModifiPresupuesto> consultaFiltro(String codigo, String descripcion,Boolean Activo){
		//LISTA CON MOTIVOS CONSULTADOS POR FILTRO
		ArrayList<DtoMotivosModifiPresupuesto> resultado = 	motivosModificacionPresupuestoDAO.consultaFiltro(codigo, descripcion, Activo);
		
		//LISTA TEMPORAL
		ArrayList<DtoMotivosModifiPresupuesto> resultadoTotal = new ArrayList<DtoMotivosModifiPresupuesto>();
		
		//SE CAMBIAN LOS VALORES S-N POR BOOLEAN
		for (DtoMotivosModifiPresupuesto motivosModifiPresupuesto : resultado) {
			if (motivosModifiPresupuesto.getActivo().equals(ConstantesBD.estadoActivoChecbox)) {
				motivosModifiPresupuesto.setEstado(true);
			}else{
				motivosModifiPresupuesto.setEstado(false);
			}
			motivosModifiPresupuesto.setPermiteEliminar(puedeEliminar(motivosModifiPresupuesto));
			resultadoTotal.add(motivosModifiPresupuesto);
		}
		return resultadoTotal;
	}


	/**
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IMotivosModificacionPresupuestoMundo#puedeEliminar(com.servinte.axioma.orm.MotivosModifiPresupuesto)
	 */
	 public Boolean puedeEliminar(MotivosModifiPresupuesto motivosModifiPresupuesto){
		  return logParametrizacionPresupuestoCapDAO.existeLogParametrizacionMotivoModificacion(motivosModifiPresupuesto.getCodigoPk());
		 }
	

}
