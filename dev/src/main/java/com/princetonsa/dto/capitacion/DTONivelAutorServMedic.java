package com.princetonsa.dto.capitacion;

import java.io.Serializable;

public class DTONivelAutorServMedic implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int codigoPk;
	private int nivelAutorizacion;
	private Integer codigoEspecialidadServicio;
	private String acronimoTipoServicio;
	private Integer codigoGrupoServicio;
	
	private Integer subgrupoArticulo;
	private Integer codigoGrupoIdArticulo;
	private Integer claseGrupoIdArticulo;
	private Integer claseArticulo;
	private String acronimoNaturalezaId;
	private Integer institucionNaturalezaId;
	
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}
	public int getCodigoPk() {
		return codigoPk;
	}
	public void setNivelAutorizacion(int nivelAutorizacion) {
		this.nivelAutorizacion = nivelAutorizacion;
	}
	public int getNivelAutorizacion() {
		return nivelAutorizacion;
	}

	public String getAcronimoTipoServicio() {
		return acronimoTipoServicio;
	}
	public void setAcronimoTipoServicio(String acronimoTipoServicio) {
		this.acronimoTipoServicio = acronimoTipoServicio;
	}
	public Integer getCodigoEspecialidadServicio() {
		return codigoEspecialidadServicio;
	}
	public void setCodigoEspecialidadServicio(Integer codigoEspecialidadServicio) {
		this.codigoEspecialidadServicio = codigoEspecialidadServicio;
	}
	public Integer getCodigoGrupoServicio() {
		return codigoGrupoServicio;
	}
	public void setCodigoGrupoServicio(Integer codigoGrupoServicio) {
		this.codigoGrupoServicio = codigoGrupoServicio;
	}
	
	public Integer getCodigoGrupoIdArticulo() {
		return codigoGrupoIdArticulo;
	}
	public void setCodigoGrupoIdArticulo(Integer codigoGrupoIdArticulo) {
		this.codigoGrupoIdArticulo = codigoGrupoIdArticulo;
	}
	public Integer getClaseGrupoIdArticulo() {
		return claseGrupoIdArticulo;
	}
	public void setClaseGrupoIdArticulo(Integer claseGrupoIdArticulo) {
		this.claseGrupoIdArticulo = claseGrupoIdArticulo;
	}
	public Integer getClaseArticulo() {
		return claseArticulo;
	}
	public void setClaseArticulo(Integer claseArticulo) {
		this.claseArticulo = claseArticulo;
	}
	public String getAcronimoNaturalezaId() {
		return acronimoNaturalezaId;
	}
	public void setAcronimoNaturalezaId(String acronimoNaturalezaId) {
		this.acronimoNaturalezaId = acronimoNaturalezaId;
	}
	public Integer getInstitucionNaturalezaId() {
		return institucionNaturalezaId;
	}
	public void setInstitucionNaturalezaId(Integer institucionNaturalezaId) {
		this.institucionNaturalezaId = institucionNaturalezaId;
	}
	public Integer getSubgrupoArticulo() {
		return subgrupoArticulo;
	}
	public void setSubgrupoArticulo(Integer subgrupoArticulo) {
		this.subgrupoArticulo = subgrupoArticulo;
	}
	
	

	
}
