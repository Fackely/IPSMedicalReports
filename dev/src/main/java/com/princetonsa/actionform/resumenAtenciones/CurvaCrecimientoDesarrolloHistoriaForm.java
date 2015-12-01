package com.princetonsa.actionform.resumenAtenciones;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.struts.validator.ValidatorForm;

import util.UtilidadBD;

import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseCrecimientoDesarrolloDao;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dto.historiaClinica.HistoricoImagenPlantillaDto;

/**
 * @author Juan Camilo Gaviria Acosta
 */
public class CurvaCrecimientoDesarrolloHistoriaForm extends ValidatorForm
{	
	private PersonaBasica paciente;
	private UsuarioBasico usuario;
	private List<HistoricoImagenPlantillaDto> dtoHistoricoImagenPlantilla;
	private HistoricoImagenPlantillaDto curvaSeleccionada;
	private Integer indiceCurvaSeleccionada;
	private Boolean mostrarDetalles;
	private String edadCalculada;
	private String codigoValEvol;
	private String funcionalidad;
	private boolean hayCurvasAnteriores;
	
	public String getFuncionalidad() {
		return funcionalidad;
	}
	public void setFuncionalidad(String funcionalidad) {
		this.funcionalidad = funcionalidad;
	}
	public PersonaBasica getPaciente() {
		return paciente;
	}
	public void setPaciente(PersonaBasica paciente) {
		this.paciente = paciente;
	}
	public List<HistoricoImagenPlantillaDto> getDtoHistoricoImagenPlantilla() {
		return dtoHistoricoImagenPlantilla;
	}
	public void setDtoHistoricoImagenPlantilla(
			List<HistoricoImagenPlantillaDto> dtoHistoricoImagenPlantilla) {
		this.dtoHistoricoImagenPlantilla = dtoHistoricoImagenPlantilla;
	}
	public HistoricoImagenPlantillaDto getCurvaSeleccionada() {
		return curvaSeleccionada;
	}
	public void setCurvaSeleccionada(HistoricoImagenPlantillaDto curvaSeleccionada) {
		this.curvaSeleccionada = curvaSeleccionada;
	}
	public Integer getIndiceCurvaSeleccionada() {
		return indiceCurvaSeleccionada;
	}
	public void setIndiceCurvaSeleccionada(Integer indiceCurvaSeleccionada) {
		this.indiceCurvaSeleccionada = indiceCurvaSeleccionada;
	}
	public Boolean getMostrarDetalles() {
		return mostrarDetalles;
	}
	public void setMostrarDetalles(Boolean mostrarDetalles) {
		this.mostrarDetalles = mostrarDetalles;
	}
	public String getEdadCalculada() {
		return edadCalculada;
	}
	public void setEdadCalculada(String edadCalculada) {
		this.edadCalculada = edadCalculada;
	}
	public String getCodigoValEvol() {
		return codigoValEvol;
	}
	public void setCodigoValEvol(String codigoValEvol) {
		this.codigoValEvol = codigoValEvol;
	}
	public UsuarioBasico getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioBasico usuario) {
		this.usuario = usuario;
	}
	public void reset() {
		paciente = null;
		dtoHistoricoImagenPlantilla = null;
		curvaSeleccionada = null;
		indiceCurvaSeleccionada = null;
		mostrarDetalles = false;
		edadCalculada = null;
	}
	
	public boolean getHayCurvas(){
		if(dtoHistoricoImagenPlantilla.size() > 0)
			return true;
		else
			return false;
	}
	public boolean isHayCurvasAnteriores() {
		return hayCurvasAnteriores;
	}
	public void setHayCurvasAnteriores(boolean hayCurvasAnteriores) {
		this.hayCurvasAnteriores = hayCurvasAnteriores;
	}
	
	//ESTE METODO SE DEBE BORRAR DESPUES DE HACER LA MIGRACION DE LAS CURVAS ANTERIORES A LA VERSION ACTUAL
	public boolean getHayCurvasAnteriores2(){
		Connection con = UtilidadBD.abrirConexion();
		
		HashMap<String, String> mp = new HashMap<String, String>();
		mp.put("nroConsulta", "1");
		mp.put("codigoPaciente", paciente.getCodigoPersona()+"");
		mp.put("institucion", usuario.getCodigoInstitucionInt()+"");
		
		HashMap<String, String> curvasAnteriores = SqlBaseCrecimientoDesarrolloDao.consultarInformacion(con, mp);
		
		try 
		{
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		if(Integer.valueOf(curvasAnteriores.get("numRegistros")) > 0)
			return true;
		else
			return false;
	}
	
}