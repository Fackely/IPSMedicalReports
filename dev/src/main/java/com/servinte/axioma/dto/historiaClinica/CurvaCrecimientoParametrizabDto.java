package com.servinte.axioma.dto.historiaClinica;

import java.util.Date;

/**
 * CurvaCrecimientoParametrizabDto
 * @author Juan Camilo Gaviria Acosta
 */
public class CurvaCrecimientoParametrizabDto {

	private Integer id;
	private Integer codigoSexo;
	private Integer idCurvaPredecesor;
	private String tituloGrafica;
	private String colorTitulo;
	private String descripcion;
	private String colorDescripcion;
	private Integer edadInicial;
	private Integer edadFinal;
	private Boolean activo;
	private Boolean indicadorError;
	private Date fechaCreacion;
	private String colorParaEliminar="#FFFFFF";
	private ImagenParametrizadaDto dtoImagenesParametrizadas = new ImagenParametrizadaDto();
	private ImagenParametrizadaDto dtoImagenParametrizadaAntigua;

	public CurvaCrecimientoParametrizabDto() {
		this.fechaCreacion = new Date(System.currentTimeMillis());
	}

	/**
	 * Constructor utilizado para clonar un objeto de tipo CurvaCrecimientoParametrizabDto
	 * @param dto
	 */
	public CurvaCrecimientoParametrizabDto(CurvaCrecimientoParametrizabDto dto) {
		this.id = dto.id;
		this.codigoSexo = dto.codigoSexo;
		this.idCurvaPredecesor = dto.idCurvaPredecesor;
		this.tituloGrafica = dto.tituloGrafica;
		this.colorTitulo = dto.colorTitulo;
		this.descripcion = dto.descripcion;
		this.colorDescripcion = dto.colorDescripcion;
		this.edadInicial = dto.edadInicial;
		this.edadFinal = dto.edadFinal;
		this.activo = dto.activo;
		this.indicadorError = dto.indicadorError;
		this.fechaCreacion = dto.fechaCreacion;
		this.colorParaEliminar = dto.colorParaEliminar;
		this.dtoImagenesParametrizadas = dto.dtoImagenesParametrizadas;		
		this.dtoImagenParametrizadaAntigua = dto.dtoImagenParametrizadaAntigua;	
	}

	/**
	 * @param id
	 * @param codigoSexo
	 * @param idCurvaPredecesor
	 * @param tituloGrafica
	 * @param colorTitulo
	 * @param descripcion
	 * @param colorDescripcion
	 * @param edadInicial
	 * @param edadFinal
	 * @param activo
	 * @param indicadorError
	 * @param fechaCreacion
	 */
	public CurvaCrecimientoParametrizabDto(Integer id, Integer codigoSexo,
			Integer idCurvaPredecesor, String tituloGrafica,
			String colorTitulo, String descripcion, String colorDescripcion,
			Integer edadInicial, Integer edadFinal, Boolean activo,
			Boolean indicadorError, Date fechaCreacion) {
		super();
		this.id = id;
		this.codigoSexo = codigoSexo;
		this.idCurvaPredecesor = idCurvaPredecesor;
		this.tituloGrafica = tituloGrafica;
		this.colorTitulo = colorTitulo;
		this.descripcion = descripcion;
		this.colorDescripcion = colorDescripcion;
		this.edadInicial = edadInicial;
		this.edadFinal = edadFinal;
		this.activo = activo;
		this.indicadorError = indicadorError;
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * @param id
	 * @param codigoSexo
	 * @param idCurvaPredecesor
	 * @param tituloGrafica
	 * @param colorTitulo
	 * @param descripcion
	 * @param colorDescripcion
	 * @param edadInicial
	 * @param edadFinal
	 * @param activo
	 * @param indicadorError
	 * @param fechaCreacion
	 * @param idImagenes
	 * @param imagenIzquierda
	 * @param imagenDerecha
	 * @param imagenCurva
	 * @param activoImagenes
	 * @param fechaCreacionImagenes
	 */
	public CurvaCrecimientoParametrizabDto(Integer id, Integer codigoSexo,
			Integer idCurvaPredecesor, String tituloGrafica,
			String colorTitulo, String descripcion, String colorDescripcion,
			Integer edadInicial, Integer edadFinal, Boolean activo,
			Boolean indicadorError, Date fechaCreacion,Integer idImagenes, 
			String imagenIzquierda, String imagenDerecha, String imagenCurva, 
			Boolean activoImagenes, Date fechaCreacionImagenes) {
		super();
		this.id = id;
		this.codigoSexo = codigoSexo;
		this.idCurvaPredecesor = idCurvaPredecesor;
		this.tituloGrafica = tituloGrafica;
		this.colorTitulo = colorTitulo;
		this.descripcion = descripcion;
		this.colorDescripcion = colorDescripcion;
		this.edadInicial = edadInicial;
		this.edadFinal = edadFinal;
		this.activo = activo;
		this.indicadorError = indicadorError;
		this.fechaCreacion = fechaCreacion;
		
		this.dtoImagenesParametrizadas.setId(idImagenes);
		this.dtoImagenesParametrizadas.setImagenIzquierda(imagenIzquierda);
		this.dtoImagenesParametrizadas.setImagenDerecha(imagenDerecha);
		this.dtoImagenesParametrizadas.setImagenCurva(imagenCurva);
		this.dtoImagenesParametrizadas.setActivo(activoImagenes);
		this.dtoImagenesParametrizadas.setFechaCreacion(fechaCreacionImagenes);
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCodigoSexo() {
		return codigoSexo;
	}
	public void setCodigoSexo(Integer codigoSexo) {
		this.codigoSexo = codigoSexo;
	}
	public Integer getIdCurvaPredecesor() {
		return idCurvaPredecesor;
	}
	public void setIdCurvaPredecesor(Integer idCurvaPredecesor) {
		this.idCurvaPredecesor = idCurvaPredecesor;
	}
	public String getTituloGrafica() {
		return tituloGrafica;
	}
	public void setTituloGrafica(String tituloGrafica) {
		this.tituloGrafica = tituloGrafica;
	}
	public String getColorTitulo() {
		return colorTitulo;
	}
	public void setColorTitulo(String colorTitulo) {
		this.colorTitulo = colorTitulo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getColorDescripcion() {
		return colorDescripcion;
	}
	public void setColorDescripcion(String colorDescripcion) {
		this.colorDescripcion = colorDescripcion;
	}
	public Integer getEdadInicial() {
		return edadInicial;
	}
	public void setEdadInicial(Integer edadInicial) {
		this.edadInicial = edadInicial;
	}
	public Integer getEdadFinal() {
		return edadFinal;
	}
	public void setEdadFinal(Integer edadFinal) {
		this.edadFinal = edadFinal;
	}
	public Boolean getActivo() {
		return activo;
	}
	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
	public Boolean getIndicadorError() {
		return indicadorError;
	}
	public void setIndicadorError(Boolean indicadorError) {
		this.indicadorError = indicadorError;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public ImagenParametrizadaDto getDtoImagenesParametrizadas() {
		return dtoImagenesParametrizadas;
	}
	public void setDtoImagenesParametrizadas(
			ImagenParametrizadaDto dtoImagenesParametrizadas) {
		this.dtoImagenesParametrizadas = dtoImagenesParametrizadas;
	}
	public ImagenParametrizadaDto getDtoImagenParametrizadaAntigua() {
		return dtoImagenParametrizadaAntigua;
	}
	public void setDtoImagenParametrizadaAntigua(ImagenParametrizadaDto dtoImagenParametrizadaAntigua) {
		this.dtoImagenParametrizadaAntigua = dtoImagenParametrizadaAntigua;
	}
	public String getColorParaEliminar() {
		return colorParaEliminar;
	}
	public void setColorParaEliminar(String colorParaEliminar) {
		this.colorParaEliminar = colorParaEliminar;
	}
}