package com.princetonsa.enu.manejoPaciente;

/**
 * Clase encargada de listar los tipos de 
 * Autorización de Ingreso Estancia
 * 
 * @author Diana Carolina G
 */
public enum EnumTipoAutorizacion {
	
	//mensaje.tipoAutorizacion.Servicios
	SERVICIOS("mensaje.tipoAutorizacion.Servicios", 1),
	
	//mensaje.tipoSalida.MedicamentosInsumos
	MEDICAMENTOSINSUMOS("mensaje.tipoAutorizacion.MedicamentosInsumos", 2);
	
	
	private final String recurso;
	private final int codigo;
	
	EnumTipoAutorizacion(String recurso, int codigo)
	{
		this.recurso=recurso;
		this.codigo=codigo;
	}

	public String getRecurso() {
		return recurso;
	}

	public int getCodigo() {
		return codigo;
	}

}
